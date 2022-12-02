package cn.cimoc.broky.spring.boot.autoconfigure;

import cn.cimoc.broky.core.*;
import cn.cimoc.broky.core.controller.BrokyErrorController;
import cn.cimoc.broky.core.handler.BaseBrokyResponseHandler;
import cn.cimoc.broky.core.handler.BrokyExceptionHandler;
import cn.cimoc.broky.core.handler.BrokyResponseHandler;
import cn.cimoc.broky.core.handler.NoErrorBrokyResponseHandler;
import cn.cimoc.broky.core.interceptor.BrokyResponseInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author LGZ
 * <p>
 */
@Configuration
@EnableConfigurationProperties({BrokyProperties.class, BrokyHandlerProperties.class, BrokyLogProperties.class})
@ConditionalOnProperty(prefix = BrokyProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class BrokyAutoConfiguration {
    @Resource
    BrokyProperties brokyProperties;

    @Bean
    public BrokyResponseInterceptor brokyResponseInterceptor() {
        return new BrokyResponseInterceptor(brokyProperties.getAnn());
    }

    @Bean
    public BrokyWebConfig brokyWebConfig() {
        return new BrokyWebConfig();
    }

    @Bean
    @ConditionalOnBean(BrokyErrorController.class)
    @ConditionalOnMissingBean(BaseBrokyResponseHandler.class)
    public BrokyResponseHandler brokyResponseHandler() {
        return new BrokyResponseHandler(brokyProperties.getAnn());
    }

    @Bean
    @ConditionalOnMissingBean({BrokyErrorController.class, BaseBrokyResponseHandler.class})
    public NoErrorBrokyResponseHandler noErrorBrokyResponseHandler() {
        return new NoErrorBrokyResponseHandler(brokyProperties.getAnn());
    }

    @Bean
    public BrokyExceptionHandler brokyExceptionHandler() {
        return new BrokyExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
