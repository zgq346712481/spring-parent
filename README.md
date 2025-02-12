# EmilyInfrustructure基础框架

> Oauth2相关代码请移步到feature_bak分支查看

### maven父pom和子pom的版本号批量修改

##### 1 设置新的版本号

```
./mvnw versions:set -DnewVersion=2.4.1
```

##### 2 撤销设置

```
./mvnw versions:revert
```

##### 3 提交设置

```
./mvnw versions:commit
```
##### 4.项目打包(同时处理项目所依赖的包)

```
mvn clean install -pl emily-spring-boot-starter -am
```
或
```
./mvnw clean install -pl emily-spring-boot-starter -am
```



| 参数 | 全程                   | 说明                                                         |
| ---- | ---------------------- | ------------------------------------------------------------ |
| -pl  | --projects             | 选项后可跟随{groupId}:{artifactId}或者所选模块的相对路径(多个模块以逗号分隔) |
| -am  | --also-make            | 表示同时处理选定模块所依赖的模块                             |
| -amd | --also-make-dependents | 表示同时处理依赖选定模块的模块                               |
| -N   | --non-                 | 表示不递归子模块                                             |
| -rf  | --resume-frm           | 表示从指定模块开始继续处理                                   |

### 打tag标签

##### 1.添加tag

```
git tag -a version1.0 -m 'first version'
```

##### 2.提交tag

```
git push origin --tags
```

其它tag操作参考：[tag操作指南](https://blog.csdn.net/Emily/article/details/78839295?ops_request_misc=%7B%22request%5Fid%22%3A%22158685673019724835840750%22%2C%22scm%22%3A%2220140713.130056874..%22%7D&request_id=158685673019724835840750&biz_id=0&utm_source=distribute.pc_search_result.none-task-blog-blog_SOOPENSEARCH-1)

------
### 自研框架-emily(艾米莉)配置
```java
#设置开启用户请求日志拦截器模式，默认:true
spring.emily.api-log.enable=true
#设置开启日志debug模式，默认:false
spring.emily.api-log.debug=false
#是否开启抛出的异常拦截，默认：true
spring.emily.exception.enable=true
#是否开启json转换器配置,默认：true
spring.emily.jackson2.converter.enable=true

#设置开启返回结果包装，默认:true
spring.emily.return-value.enable=true
#设置https配置开关,默认false
spring.emily.https.enable=false
#RedisTemplate组件开关，默认:false
spring.emily.redis.enable=false
#限流组件开关，默认:false
spring.emily.rate-limit.enable=true
#防止重复提交组件开关，默认:false
spring.emily.idempotent.enable=false

#RestTemplate组件
#Http RestTemplate组件开关，默认true
spring.emily.http-client.enable=true
#Http RestTemplate拦截器开关，记录请求响应日志，默认true
spring.emily.http-client.enable-interceptor=true
#http连接读取超时时间，默认5000毫秒
spring.emily.http-client.read-time-out=5000
#http连接连接超时时间，默认10000毫秒
spring.emily.http-client.connect-time-out=10000

#RestTemplate组件-Spring Cloud客户端负载均衡
#Http RestTemplate组件开关，默认true
spring.emily.cloud.http-client-loadbalancer.enable=true
#Http RestTemplate拦截器开关，记录请求响应日志，默认true
spring.emily.cloud.http-client-loadbalancer.enable-interceptor=true
#http连接读取超时时间，默认1000毫秒
spring.emily.cloud.http-client-loadbalancer.read-time-out=1000
#http连接连接超时时间，默认1000毫秒
spring.emily.cloud.http-client-loadbalancer.connect-time-out=1000

##API路由设置
#是否开启所有接口的前缀prefix,默认前面添加api
spring.emily.web.path.enable-all-prefix=true
#自定义添加前缀,默认api
spring.emily.web.path.prefix=api
#区分大小写,默认false
spring.emily.web.path.case-sensitive=false
#是否缓存匹配规则,默认null等于true
spring.emily.web.path.cache-patterns=true
#是否去除前后空格,默认false
spring.emily.web.path.trim-tokens=false
#设置URL末尾是否支持斜杠，默认true,如/a/b/有效，/a/b也有效
spring.emily.web.path.use-trailing-slash-match=true
#忽略URL前缀控制器设置,默认空
spring.emily.web.path.ignore-controller-url-prefix=

##跨域设置
#开启跨域设置，默认false
spring.emily.web.cors.enable=false
#设置允许哪些源来访问,多个源用逗号分开
spring.emily.web.cors.allowed-origins=
#允许HTTP请求方法
spring.emily.web.cors.allowed-methods=GET,POST
#设置用户可以拿到的字段
spring.emily.web.cors.allowed-headers=
#设置浏览器是否应该发送凭据cookie
spring.emily.web.cors.allow-credentials=true
#设置响应HEAD,默认无任何设置，不可以使用*号
spring.emily.web.cors.exposed-headers=
#设置多长时间内不需要发送预检验请求，可以缓存该结果，默认1800秒
spring.emily.web.cors.max-age=1800

#是否开启数据源组件, 默认：true
spring.emily.datasource.enabled=true
#默认数据源配置，默认：default
spring.emily.datasource.default-config=default
#驱动名称
spring.emily.datasource.config.default.driver-class-name=oracle.jdbc.OracleDriver
#配置url
spring.emily.datasource.config.default.url=jdbc:oracle:thin:@1x.1x.8x.1x:1521:xxx
#用户名
spring.emily.datasource.config.default.username=xx
#用户密码
spring.emily.datasource.config.default.password=xx
#数据库连接池类型
spring.emily.datasource.config.default.db-type=com.alibaba.druid.pool.DruidDataSource

#是否开启mybatis拦截组件, 默认：true
spring.emily.mybatis.enabled=true
#是否还要检查超类或者接口，默认：false
spring.emily.mybatis.check-inherited=false

#日志组件
#启动日志访问组件，默认false
spring.emily.logback.enabled=true

#日志文件存放路径，默认是:./logs
spring.emily.logback.appender.file-path=./logs
#如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true
spring.emily.logback.appender.append=true
#如果是 true，日志会被安全的写入文件，即使其他的FileAppender也在向此文件做写入操作，效率低，默认是 false|Support multiple-JVM writing to the same log file
spring.emily.logback.appender.prudent=false
#设置是否将输出流刷新，确保日志信息不丢失，默认：true
spring.emily.logback.appender.immediate-flush=true
#是否报告内部状态信息，默认；false
spring.emily.logback.appender.report-state=true

#是否开启基于文件大小和时间的SizeAndTimeBasedRollingPolicy归档策略
#默认是基于TimeBasedRollingPolicy的时间归档策略，默认false
spring.emily.logback.appender.rolling-policy.type=size_and_time_based
#设置要保留的最大存档文件数量，以异步方式删除旧文件,默认 7
spring.emily.logback.appender.rolling-policy.max-history=2
#最大日志文件大小 KB、MB、GB，默认:500MB
spring.emily.logback.appender.rolling-policy.max-file-size=10KB
#控制所有归档文件总大小 KB、MB、GB，默认:5GB
spring.emily.logback.appender.rolling-policy.total-size-cap=5GB
#设置重启服务后是否清除历史日志文件，默认：false
spring.emily.logback.appender.rolling-policy.clean-history-on-start=true

#是否开启异步记录Appender，默认false
spring.emily.logback.appender.async.enabled=false
#队列的最大容量，默认为 256
spring.emily.logback.appender.async.queue-size=256
#默认，当队列还剩余 20% 的容量时，会丢弃级别为 TRACE, DEBUG 与 INFO 的日志，仅仅只保留 WARN 与 ERROR 级别的日志。想要保留所有的事件，可以设置为 0
spring.emily.logback.appender.async.discarding-threshold=0
# 根据所引用 appender 队列的深度以及延迟， AsyncAppender 可能会耗费长时间去刷新队列。
# 当 LoggerContext 被停止时， AsyncAppender stop 方法会等待工作线程指定的时间来完成。
# 使用 maxFlushTime 来指定最大的刷新时间，单位为毫秒。在指定时间内没有被处理完的事件将会被丢弃。这个属性的值的含义与 Thread.join(long)) 相同
# 默认是 1000毫秒
spring.emily.logback.appender.async.max-flush-time=1000
# 在队列满的时候 appender 会阻塞而不是丢弃信息。设置为 true，appender 不会阻塞你的应用而会将消息丢弃，默认为 false
spring.emily.logback.appender.async.never-block=false

#日志级别,即该等级之上才会输出，ERROR > WARN > INFO > DEBUG > TRACE >ALL, 默认：DEBUG
spring.emily.logback.root.level=info
#通用日志输出格式，默认：[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] [%-5level] [%-36.36logger{36}:%-4.4line] : %msg%n
spring.emily.logback.root.pattern=[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] [%-5level] [%-36.36logger{36}:%-4.4line] : %msg%n
#基础日志文件路径
spring.emily.logback.root.file-path=base

#日志级别,即该等级之上才会输出，ERROR > WARN > INFO > DEBUG > TRACE >ALL, 默认：DEBUG
spring.emily.logback.group.level=info
#模块日志输出格式，默认：%msg%n
spring.emily.logback.group.pattern=[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] [%-5level] [%-36.36logger{36}:%-4.4line] : %msg%n
#是否将模块日志输出到控制台，默认：false
spring.emily.logback.group.console=true

#日志级别,即该等级之上才会输出，ERROR > WARN > INFO > DEBUG > TRACE >ALL, 默认：DEBUG
spring.emily.logback.module.level=info
#模块日志输出格式，默认：%msg%n
spring.emily.logback.module.pattern=[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] [%-5level] [%-36.36logger{36}:%-4.4line] : %msg%n
#是否将模块日志输出到控制台，默认：false
spring.emily.logback.module.console=true

```

##### consul服务查询、删除接口

- 查询服务接口Get

```java
http://127.0.0.1:8500/v1/agent/checks
```

- 删除consul服务接口PUT方法

```
http://127.0.0.1:8500/v1/agent/service/deregister/instance-id(实例ID)
```

##### IDEA快捷键

- 查询类的所有方法：

  ```
  F+command+F12
  ```

  

