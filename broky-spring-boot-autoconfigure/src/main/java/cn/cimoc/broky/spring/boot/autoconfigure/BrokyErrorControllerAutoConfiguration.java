package cn.cimoc.broky.spring.boot.autoconfigure;

import cn.cimoc.broky.core.controller.BrokyErrorController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author LGZ
 * <p>
 */
@Configuration
@ConditionalOnProperty(prefix = BrokyProperties.PREFIX, name = "error", havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingBean(BrokyErrorController.class)
@ComponentScan(basePackages = "cn.cimoc.broky.core.controller")
public class BrokyErrorControllerAutoConfiguration {
}
