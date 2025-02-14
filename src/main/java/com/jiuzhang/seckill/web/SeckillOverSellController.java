package com.jiuzhang.seckill.web;

import com.jiuzhang.seckill.services.SeckillActivityService;
import com.jiuzhang.seckill.services.SeckillOverSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeckillOverSellController {
    @Autowired
    private SeckillOverSellService seckillOverSellService;

    @Autowired
    private SeckillActivityService seckillActivityService;
    /**
     *抢购处理 v1
     * @param seckillActivityId
     * @return
     */
//    @ResponseBody
//    @RequestMapping("/activity/{seckillActivityId}")
//    public String seckillOverSell(@PathVariable Long seckillActivityId, Model model) {
//        return seckillOverSellService.processSeckill(seckillActivityId);
//    }
    @ResponseBody
    @RequestMapping("/activity/{seckillActivityId}")
    public String seckillOverSell(@PathVariable Long seckillActivityId) {
        boolean hasStock = seckillActivityService.stockDeductValidator(seckillActivityId);
        return hasStock ? "恭喜，抢购成功!" : "你来晚了，抢购失败";
    }

}
