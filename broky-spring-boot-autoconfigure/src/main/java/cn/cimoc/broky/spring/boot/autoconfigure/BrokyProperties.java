package cn.cimoc.broky.spring.boot.autoconfigure;

import cn.cimoc.broky.core.handler.BaseBrokyResponseHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LGZ
 * <p>
 */
@Data
@ConfigurationProperties(prefix = BrokyProperties.PREFIX)
public class BrokyProperties {

    public static final String PREFIX = "broky";

    /**
     * 是否启用
     */
    private Boolean enable = true;

    /**
     * ResponseResult注解的标记字段
     */
    private String ann = BaseBrokyResponseHandler.DEFAULT_ANN;

    /**
     * 是否启用broky的errorController来覆盖默认的
     */
    private Boolean error = true;

}
