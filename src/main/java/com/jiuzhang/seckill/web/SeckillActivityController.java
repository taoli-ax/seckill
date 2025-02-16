package com.jiuzhang.seckill.web;

import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.dao.SeckillCommodityDao;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.db.po.SeckillCommodity;
import com.jiuzhang.seckill.db.po.SeckillOrder;
import com.jiuzhang.seckill.services.SeckillActivityService;
import com.jiuzhang.seckill.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class SeckillActivityController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private SeckillActivityService seckillActivityService;

    @RequestMapping("/addSeckillActivity")
    public String addSeckillActivity() {
        return "add_activity";
    }

    @Autowired
    private SeckillActivityDao seckillActivityDao;

//    @ResponseBody
    @RequestMapping("/addSeckillActivityAction")
    public String addSeckillActivityAction(
            @RequestParam("name") String name,
            @RequestParam("commodityId") long commodityId,
            @RequestParam("seckillPrice")BigDecimal seckillPrice,
            @RequestParam("oldPrice") BigDecimal oldPrice,
            @RequestParam("seckillNumber") long seckillNumber,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            Map<String,Object> resultMap
            ) throws ParseException {
            startTime = startTime.substring(0, 10) + startTime.substring(11);
            endTime = endTime.substring(0, 10) + endTime.substring(11);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhh:mm");
            SeckillActivity seckillActivity = new SeckillActivity();
            seckillActivity.setName(name);
            seckillActivity.setCommodityId(commodityId);
            seckillActivity.setSeckillPrice(seckillPrice);
            seckillActivity.setOldPrice(oldPrice);
            seckillActivity.setTotalStock(seckillNumber);
            seckillActivity.setAvailableStock(new Integer(""+seckillNumber));
            seckillActivity.setLockStock(0L);
            seckillActivity.setActivityStatus(1);
            seckillActivity.setStartTime(sdf.parse(startTime));
            seckillActivity.setEndTime(sdf.parse(endTime));
            seckillActivityDao.inertSeckillActivity(seckillActivity);
            resultMap.put("seckillActivity", seckillActivity);
            return "add_success";
    }

    @RequestMapping("/seckills")
    public String ActivityList(Map<String, Object> resultMap) {
        List<SeckillActivity> seckillActivities = seckillActivityDao.querySeckillActivitysByStatus(1);
        resultMap.put("seckillActivities", seckillActivities);
        return "seckill_activity";
    }

    @Autowired
    private SeckillCommodityDao seckillCommodityDao;

    @RequestMapping("/item/{seckillActivityId}")
    public String itemPage(@PathVariable long seckillActivityId, Map<String,Object> resultMap) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        resultMap.put("seckillActivity", seckillActivity);
        Long commodityId = seckillActivity.getCommodityId();
        SeckillCommodity seckillCommodity = seckillCommodityDao.querySeckillCommodityById(commodityId);
        resultMap.put("seckillActivity",seckillActivity);
        resultMap.put("seckillCommodity", seckillCommodity);
        resultMap.put("seckillPrice",seckillActivity.getSeckillPrice());
        resultMap.put("oldPrice",seckillActivity.getOldPrice());
        resultMap.put("seckillCommodityId",commodityId);
        resultMap.put("seckillCommodityName",seckillCommodity.getCommodityName());
        resultMap.put("seckillCommodityDesc", seckillCommodity.getCommodityDesc());
        return "seckill_item";
    }

    @RequestMapping("/seckill/buy/{userId}/{seckillActivityId}")
    public ModelAndView seckillCommodity(@PathVariable long userId, @PathVariable long seckillActivityId){
        ModelAndView modelAndView = new ModelAndView();
        Boolean stockAvailableResult= false;

        try {
            stockAvailableResult = seckillActivityService.stockDeductValidator(seckillActivityId);
            if(stockAvailableResult){
                SeckillOrder order = seckillActivityService.createSeckillOrder(userId, seckillActivityId);
                modelAndView.addObject("seckillOrderNumber", order.getOrderNo());
                modelAndView.addObject("resultInfo", "恭喜，库存已抢到，创建订单成功");
            }else {
                modelAndView.addObject("resultInfo", "来晚了，库存不足，无法创建订单");
            }

        }catch (Exception e) {

            log.error("秒杀系统异常"+e.getMessage());
            modelAndView.addObject("resultInfo","秒杀系统异常");
        }
        modelAndView.setViewName("seckill_result");
        return modelAndView;
    }
}
