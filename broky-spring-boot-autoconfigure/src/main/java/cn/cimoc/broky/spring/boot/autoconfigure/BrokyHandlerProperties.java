package cn.cimoc.broky.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LGZ
 * <p>
 */
@Data
@ConfigurationProperties(prefix = BrokyHandlerProperties.PREFIX)
public class BrokyHandlerProperties {
    public static final String PREFIX = "broky.handler";

    /**
     * request 请求参数异常捕获，默认启用
     */
    private Boolean httpMessageNotReadableHandler = true;

    /**
     * 405 错误捕获，默认开启
     */
    private Boolean methodNotSupportHandler = true;

    /**
     * 415 错误捕获，默认开启
     */
    private Boolean mediaTypeNotSupportHandler = true;

    /**
     * javax.validation 参数验证的异常捕获，默认关闭
     */
    private Boolean validParamHandler = false;
}
