package cn.cimoc.broky.spring.boot.autoconfigure;

import cn.cimoc.broky.core.BrokyException;
import cn.cimoc.broky.handler.*;
import cn.cimoc.broky.log.BrokyHandledExceptionPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author LGZ
 * <p>
 */
@Configuration
@ConditionalOnProperty(prefix = BrokyProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class BrokyHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = BrokyLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
    public BrokyHandledExceptionPool handledExceptionPool() {
        BrokyHandledExceptionPool pool = new BrokyHandledExceptionPool();
        pool.addHandledException(BrokyException.class);
        return pool;
    }

    @Bean
    @ConditionalOnProperty(prefix = BrokyHandlerProperties.PREFIX, name = "http-message-not-readable-handler", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public BaseHttpMessageNotReadableExceptionHandler httpMessageNotReadableExceptionHandler() {
        return new DefaultHttpMessageNotReadableExceptionHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = BrokyHandlerProperties.PREFIX, name = "valid-param-handler", havingValue = "true")
    @ConditionalOnMissingBean
    public BaseValidParamExceptionHandler validParamExceptionHandler() {
        return new DefaultValidParamExceptionHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = BrokyHandlerProperties.PREFIX, name = "method-not-support-handler", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public BaseHttpRequestMethodNotSupportedExceptionHandler httpRequestMethodNotSupportedExceptionHandler() {
        return new DefaultHttpRequestMethodNotSupportedExceptionHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = BrokyHandlerProperties.PREFIX, name = "media-type-not-support-handler", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public BaseHttpMediaTypeNotSupportedExceptionHandler httpMediaTypeNotSupportedExceptionHandler() {
        return new DefaultBaseHttpMediaTypeNotSupportedExceptionHandler();
    }
}
