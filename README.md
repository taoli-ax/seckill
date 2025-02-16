# Get started
1. config `mysql` as data source and config maven to run pom.xml
2. start SeckillApplication


### day-1
1. 建立一个Spring项目和`pom.xml`
2. 配置数据库 `application.properties`
3. 添加配置类注入`@SpringbootApplication` 和 `@MapperScan`
4. 配置`generatorConfig.xml`,并运行`generatorSqlmap`逆向生成 -mappers/和 -PO/
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

### day-3
1. jmeter发送100次请求，抢购100件商品，结果令人费解：
   - 100次请求都成功了
   - 商品还剩83件
   - 所有响应成功能代表接口没问题？ 不能，显然系统抗住了请求，但是逻辑上有问题！

2. 100次请求每个用户都说自己抢到了商品，但是库存对不上，这就导致了OverSell问题！
3. 解决方法是加入redis,用Lua脚本控制库存，配置
   - 添加`jedis`到pom
   - 增加`JedisConfig` 配置类 实现 `@Bean` 控制 `redisFactory`方法
   - 增加`RedisService` set get方法
4. 一顿操作猛如虎，看看有没有效果
   - jmeter 发起110次请求，redis库存100变为0，有10个用户抢购失败，100个用户抢购成功，数据库还是100
   - 成功！Lua不但解决库存扣减问题，还保护了数据库

### day-4
`vagrant ssh ed769b3` 连接虚拟机

1. rocketmq commands:
   无脑启动broker和namesrv
```shell
### start namesrv
$ nohup sh bin/mqnamesrv &

### verify namesrv
$ tail -f ~/logs/rocketmqlogs/namesrv.log
The Name Server boot success...

### start broker
$ nohup sh bin/mqbroker -n localhost:9876 &

### verify broker
$ tail -f ~/logs/rocketmqlogs/broker.log 
The broker[broker-a,192.169.1.2:10911] boot success...
```
检查日志出现指定文字
```shell
 grep -i --color " The Name Server boot success. serializeType=JSON"  ~/logs/rocketmqlogs/namesrv.log
```
关闭namesrv , broker
```shell

$ sh bin/mqshutdown broker
The mqbroker(36695) is running...
Send shutdown request to mqbroker(36695) OK

$ sh bin/mqshutdown namesrv
The mqnamesrv(36664) is running...
Send shutdown request to mqnamesrv(36664) OK
```

2. debug: 远程服务器的namesrv , broker都启动正常，监听端口9876和10911也正常，为什么本地client发送的消息失败，
   - 报错:`Caused by: org.apache.rocketmq.remoting.exception.RemotingConnectException:
   connect to 127.0.0.1:10911 failed`
   - 原因: `conf/broker.conf`配置的IP1是本地回环127.0.0.1导致客户机连接了本地的127.0.0.1而不是服务的Host,
   应该配置为服务器的公网IP或者vagrant定义的IP `192.168.33.10`

3. namesrv broker client这三者是什么关系？
   client 发送消息时先查询namesrv种的broker地址，然后发给broker
   client 监听消息时同样先问namesrv种的broker地址，然后从broker拉取消息
   为什么要查询呢，因为broker每隔一段时间向namesrv报告自己的位置
   namesrv有broker路由信息和topic信息可供查询

### day-5
1. 创建订单系统
2. 雪花算法
   - 选择一个任意时间戳
   - 位运算，与或，抑或运算
   - 4个部分组成，TimeStamp,DataCenter,Machine,Sequence
   - 左移量，TimeStamp 最大， Sequence不需要动
   - 占位 TimeStamp 22, DataCenter 5, Machine 5
   - 构造方法参数 datacenterId, machineId
   - 异步线程锁定方法 synchronized nextId()
      - 从当前的毫秒开始计算
      - 如果同一个millsecond内创建了两次，那么sequence+1
        - 再如果sequence达到最大值则归零，并等待下一毫秒到来
      - 如果不是同一毫秒内创建的请求，sequence直接从0开始计算
        - 返回TimeStamp<<22 | datacenter<5 | machineId <<5 | sequence
   Q: 为什么最后要用按位或|
   A: 因为或运算能够无损合并数据
3. Dao,Mapper,Mapper.xml,添加了lockStock方法
```sql
update seckill_order set avaliable_stock= avaliable_stock-1, lock_stock = lock_stock + 1 where avaliable_stock>0
```
4. Controller `seckill/buy/userId/activityId`
5. Service  `createOrder`+ `RocketMQSerivce.sendMessage`
6. Rocket `OrderConsumer`锁定库存并推进订单状态为待付款