# Get started
1. config `mysql` as data source and config maven to run pom.xml
2. start SeckillApplication


### day-1
1. 建立一个Spring项目和`pom.xml`
2. 配置数据库 `application.properties`
3. 添加配置类注入`@SpringbootApplication` 和 `@MapperScan`
4. 配置`generatorConfig.xml`,并运`generatorSqlmap`行生成 -mappers/和 -PO/
5. 编写Dao的接口和实现类，进一步封装Mapper类
6. 编写`DaoTest` 测试`seckillActivityMapper` 和 `seckillActivityDao`


### day-2
1. public and template 静态资源包含了前端页面
2. 秒杀活动4个接口:
   - `/addActivity` 输出一个秒杀活动页面，由用户表单发起`add_activity.html`
   - `/addActivityAction`  创建一个秒杀活动，发送成功返回`add_success.html`
   - `/seckills` 展示所有秒杀活动列表，返回到页面`seckill_activity.html`
   - `/item/queryActivityCommodityId` 查询某个活动商品的详情页面 `seckill_item.html`
3. 注解包括:
    - `@SpringBootApplication`
    - `@MapperScan`
    - `@RestController`
    - `@RequestMapping`
    - `@Autoweird`
    - `@PathVariable`
    - `@RequestParam`
4. 用到的java基本语法包括：
    - `Mapper<String, Object> resultMap`
    - `new SimpleDateFormat()`

Q&A:
1. resultMap是如何传递给模板文件的？
    Map<String, Object> resultMap 是一个视图参数，用来传递上下文，类似django视图中的context
2. DaoImpl和Mapper的联系和区别
    DaoImpl是封装Mapper的接口，对数据库进行查询
    Mapper是封装的接口名称和mapper.xml一起查询数据库
3. Restful包括了哪些方面
    - 响应状态码和提示信息200 OK, 201 CREATED, 401 Unauthorized,500 INTERNAL SERVER ERROR
    - 请求方法由动词Get(Select) Post(create) Put(update) Delete(delete) Head Option
    - 使用 https协议
    - URL带版本好v1
    - Endpoint包含了名词的复数，与数据表名相应
    - URL带有过滤信息Filtering
    - Hypermedia API
    - 返回json格式
    - OAuth 2.0
4. Restful最佳实践
    - 动词 + 宾语 GET /articles
    - X-HTTP-Method-Override
    - 复数 URL
    - 避免多级 URL，应该使用查询字符串表达 `GET /authors/12?categories=2`
    - 响应包括 HTTP 状态码和数据两部分
    - HTTP 响应头包括 Content-Type属性要设为application/json,请求头包括Accept: application/json
    - 发生错误时，不要返回 200 状态码
    - 根域名提供所有链接 HATEOAS
