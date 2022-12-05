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

### 基本使用

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
        "name": 'Sagiri-kawaii01',
        "age": 22
    }
}
```



### 高级用法

**基本使用**只介绍了正常返回数据时的使用方式，如果我们不需要返回数据，或者需要返回错误信息时，自然不可能走正常的 return，去返回上面例子中的 MyPojo 了

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

BrokyError 中提供了一些基本的错误信息，根据需要，开发者可以在使用过程中，继承 BrokyError 来自定义新的错误信息

---

对于情况2，运行时异常，Broky 也同样会对异常信息进行数据封装

如果你熟悉 SpringBoot，可能知道 SpringBoot 对于运行时的异常处理方式是通过 BasicErrorController 来处理的

Broky 对
