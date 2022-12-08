## Broky —— Sring Boot 初学者的好伙伴

## 介绍

还在为返回数据的封装烦恼吗，系统报错了直接变成另一种返回？

又或者是烦恼于多个项目 AOP 日志的复制粘贴？

还是说对于后端的基本异常捕获手足无措？

使用 Broky，这些统统都不用你来完成！

## 数据封装

Broky 将所有返回给前端的数据（包括任何异常）封装为 [BrokyResult](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-core/src/main/java/cn/cimoc/broky/core/BrokyResult.java)

例如一个简单的无数据成功返回信息：

```json
{
    "errCode": 200,
    "errMsg": "成功",
    "data": null
}
```

### <span id="base">基本使用</span>

Broky 提供了注解来灵活地开启自动数据封装：[@BrokyResponse](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-core/src/main/java/cn/cimoc/broky/core/BrokyResponse.java)

@BrokyResponse 既可以加在 Controller 类上，也可以加在其中的方法上，被标记的方法在返回数据时，最终会被封装为上述形式

1. 加在类上，等同于加载所有内部方法上
2. 加在方法上时，此方法开启自动封装数据

Broky 只提供数据的封装，而 json 数据的转换则需要使用 SpringBoot 的 @ResponseBody 注解或者 @RestController

例如：

```java
@RequestMapping("/test")
@RestController
@BrokyResponse
public class TestController {
    @GetMapping("/t1")
    public MyPojo t1() {
        return new Mypojo();
    }
}
```

假设 MyPojo 类中包含两个属性，姓名和年龄，那么最终的返回数据是这种格式：

```json
{
    "errCode": 200,
    "errMsg": "成功",
    "data": {
        "name": "Sagiri-kawaii01",
        "age": 22
    }
}
```



### 高级用法

<a id="#base">基本使用</a>只介绍了正常返回数据时的使用方式，如果我们不需要返回数据，或者需要返回错误信息时，自然不可能走正常的 return，去返回上面例子中的 MyPojo 了

同时，错误信息可能出现在任何地方，可能是 Controller（参数校验）、Service（业务逻辑）、以及未知的运行时错误（RuntimeException） 

这些错误可以分为两类：

1. 开发者认定的错误
2. 运行中意料之外的错误

---

对于情况1，Broky 提供了统一的错误抛出机制：[BrokyException](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-core/src/main/java/cn/cimoc/broky/core/BrokyException.java)

BrokyException 接收两个参数：错误码和错误信息，并且最终会被 Broky 捕获，封装成 BrokyResult 的形式返回

例如在某个业务过程中：

```java
public MyPojo example() {
    // 数据库查询，发现账号的部分功能被限制，不能继续执行业务功能
    if (banned) {
        throw new BrokyException(4010, "您的群聊功能被限制，暂时无法使用");
    }
}
```

在抛出 BrokyException 后，业务会立马终止，并直接进入数据返回阶段，这时，上面抛出的异常最终会被封装为如下的返回数据：

```json
{
    "errCode": 4010,
    "errMsg": "您的群聊功能被限制，暂时无法使用",
    "data": null
}
```

当然，从编码风格的角度来看，例子中的错误处理并不合适，更合适的做法是使用常量池来统一管理错误信息

因此，Broky 提供了 [BrokyError](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-core/src/main/java/cn/cimoc/broky/core/BrokyError.java)，BrokyException 亦可以接受此类参数

BrokyError 中提供了一些基本的错误信息，根据需要，开发者可以在使用过程中，继承 BrokyError 来自定义新的错误信息。

此外，抛出异常的语句过于冗长，Broky 提供了 [BrokyBaseController](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-core/src/main/java/cn/cimoc/broky/core/controller/BrokyBaseController.java) 和 [BrokyBaseService](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-core/src/main/java/cn/cimoc/broky/core/service/BrokyBaseService.java)，开发者可以将自己的 Controller 和 Service 继承这两个类，就能使用简短的 `success` 和 `fail` 方法

---

对于情况2，运行时异常，Broky 也同样会对异常信息进行数据封装

如果你熟悉 SpringBoot，可能知道 SpringBoot 对于运行时的异常处理方式是通过 BasicErrorController 来处理的

Broky 默认提供了 BrokyErrorController 作为 bean 来覆盖上述异常处理器，当然，如果你有自己的需求要开发，也可以在<a href="#config">配置</a>中关闭这个功能



## 异常捕获

异常捕获是开发中非常关键的步骤，一个能让用户看到莫名其妙的错误的系统，可用性算不上高

业务中的逻辑错误信息容易处理

而系统级别的错误最易被遗漏

Broky 提供了一些基本的系统级别的异常捕获，并返回了友好的提示信息

目前支持以下几种异常捕获

| 异常类别               | 处理器                                               | 默认 |
| ---------------------- | ---------------------------------------------------- | ---- |
| 参数读取异常           | DefaultHttpMessageNotReadableExceptionHandler        | 开启 |
| 405错误                | DefaultHttpRequestMethodNotSupportedExceptionHandler | 开启 |
| 415错误                | DefaultHttpMediaTypeNotSupportedExceptionHandler     | 开启 |
| validation参数验证失败 | DefaultValidParamExceptionHandler                    | 关闭 |

 当然，对于以上错误类型，你也可以自己实现，只要继承相应的抽象类就能覆盖 Broky 的默认实现了！

除此以外，你也可以添加更多自己实现的系统异常捕获，只要实现[BrokyExceptionHandler](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-handler/src/main/java/cn/cimoc/broky/handler/BrokyExceptionHandler.java)接口就能利用到 Broky 的数据封装功能



## 日志处理

使用 Spring Boot 必定离不开日志系统，而很多时候我们并不需要多么复制的日志处理

同时，每新建一个项目都要复制日志代码也太过繁琐

因此，Broky 提供了注解式的日志处理系统

### 快速使用

对于需要记录日志的方法或控制器，只需要在上面加上注解 `@BrokyLog` 即可

注解包含以下 4 个参数

| 参数名      | 含义                                                 | 默认值   |
| ----------- | ---------------------------------------------------- | -------- |
| runTime     | 限定方法运行时间，超过则记录日志，优先级大于配置文件 | -1       |
| module      | 模块名，用来区分业务                                 | 默认模块 |
| optType     | 类型名，用来区分业务                                 | 默认类型 |
| description | 说明                                                 | 默认说明 |

### 日志内容

被注解标记的方法或控制器会记录运行日志，内容 [BrokyLogVO](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-log/src/main/java/cn/cimoc/broky/log/BrokyLogVO.java) 如下：

* 类名
* 方法名
* 请求参数
* 请求路径
* ip地址
* 收到请求的时间
* 方法执行时长
* 异常名
* 异常信息
* 返回值
* 模块名
* 类型名
* 说明

### 相关配置

当然，并不是标记了注解就一定会记录日志

Broky 提供了 runTime 配置属性，用来控制日志的记录条件，当方法运行时间超过 runTime（单位毫秒）时，才会记录日志

此外，当方法出现异常时，日志信息也会有部分变动：

首先，方法执行时长会被置为 -1，并且记录下相关的异常信息

配置中也可以修改是否显示全部的异常信息（即报错的堆栈信息）

更多配置详见 <a href="#config">配置</a>

### 自定义处理器

开发者可以实现 [BrokyLogHandler](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-log/src/main/java/cn/cimoc/broky/log/BrokyLogHandler.java) 接口，并注册成 bean，就能替换掉默认的日志处理 [DefaultBrokyLogHandler](https://github.com/Sagiri-kawaii01/broky/blob/master/broky-log/src/main/java/cn/cimoc/broky/log/DefaultBrokyLogHandler.java)

## <span id="config">配置</span>

> properties

```properties
## broky 基本配置
# broky的总开关，默认为true
broky.enable
# 注解在拦截器中的标记字段，一般无需更改
broky.ann
# 是否启用 BrokyErrorController，默认为true
broky.error

## broky 异常处理配置
# request请求参数异常处理器，默认为true
broky.handler.http-message-not-readable-handler
# 405不允许的请求方式错误处理器，默认为true
broky.handler.method-not-support-handler
# 415不支持的body类型错误处理器，默认为true
broky.handler.media-type-not-support-handler
# javax.validation参数验证失败处理器，默认为false
broky.handler.valid-param-handler

## broky 日志配置
# 日志的总开关，默认为true
broky.log.enable
# 当方法运行时长超过这个值时记录日志，单位毫秒，默认为0
broky.log.run-time
# 方法出现异常时是否展示全部信息，默认为true
broky.log.exc-full-show
# 输出日志的长度限制，0表示全部输出，默认为0
broky.log.result-length
```

> yml

```yaml
broky:
  ## broky 基本配置
  # broky的总开关，默认为true
  enable:
  # 注解在拦截器中的标记字段，一般无需更改
  ann:
  # 是否启用 BrokyErrorController，默认为true
  error:
  ## broky 异常处理配置
  handler:
    # request请求参数异常处理器，默认为true
    broky.handler.http-message-not-readable-handler:
    # 405不允许的请求方式错误处理器，默认为true
    broky.handler.method-not-support-handler:
    # 415不支持的body类型错误处理器，默认为true
    broky.handler.media-type-not-support-handler:
    # javax.validation参数验证失败处理器，默认为false
    broky.handler.valid-param-handler:
  ## broky 日志配置
  log:
    # 日志的总开关，默认为true
    broky.log.enable:
    # 当方法运行时长超过这个值时记录日志，单位毫秒，默认为0
    broky.log.run-time:
    # 方法出现异常时是否展示全部堆栈信息，默认为false
    broky.log.exc-full-show:
    # 输出日志的长度限制，0表示全部输出，默认为0
    broky.log.result-length:
```

