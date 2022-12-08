## Broky —— Sring Boot 初学者的好伙伴

[![Github stars](https://img.shields.io/github/stars/Sagiri-kawaii01/broky?logo=github)](https://github.com/Sagiri-kawaii01/broky)  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![](https://shields.io/github/v/release/Sagiri-kawaii01/broky?display_name=release&include_prereleases&sort=date)]([Releases · Sagiri-kawaii01/broky (github.com)](https://github.com/Sagiri-kawaii01/broky/releases)) ![](https://img.shields.io/badge/spring--boot-v2.3.0.RELEASE+-green)

## 介绍

还在为返回数据的封装烦恼吗，系统报错了直接变成另一种返回？

又或者是烦恼于多个项目 AOP 日志的复制粘贴？

还是说对于后端的基本异常捕获手足无措？

使用 Broky，这些统统都不用你来完成！

## Maven 导入

目前最新版本为 1.0

```xml
<dependency>
    <groupId>cn.cimoc</groupId>
    <artifactId>broky-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

## 使用教程

[Broky —— Sring Boot 初学者的好伙伴](https://cimoc.cn/2022/12/08/broky/)

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

