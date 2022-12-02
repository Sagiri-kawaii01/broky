package cn.cimoc.broky.spring.boot.autoconfigure;

import cn.cimoc.broky.core.BrokyException;
import cn.cimoc.broky.log.BrokyHandledExceptionPool;
import cn.cimoc.broky.log.BrokyLogAspect;
import cn.cimoc.broky.log.BrokyLogHandler;
import cn.cimoc.broky.log.DefaultBrokyLogHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author LGZ
 * <p>
 */
@Configuration
@ConditionalOnExpression("${broky.enable:true} && ${broky.log.enable:true}")
public class BrokyLogAutoConfiguration {

    @Resource
    BrokyLogProperties brokyLogProperties;

    @Resource
    ObjectMapper objectMapper;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = BrokyLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
    public BrokyHandledExceptionPool handledExceptionPool() {
        BrokyHandledExceptionPool pool = new BrokyHandledExceptionPool();
        pool.addHandledException(BrokyException.class);
        return pool;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = BrokyLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
    public BrokyLogHandler brokyLogHandler(BrokyHandledExceptionPool brokyHandledExceptionPool) {
        return new DefaultBrokyLogHandler(brokyHandledExceptionPool);
    }


    @Bean
    @ConditionalOnProperty(prefix = BrokyLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
    public BrokyLogAspect logAspect(BrokyLogHandler brokyLogHandler) {
        BrokyLogAspect.LogConfig logConfig = new BrokyLogAspect.LogConfigBuilder()
                .setRunTime(brokyLogProperties.getRunTime())
                .setExcFullShow(brokyLogProperties.getExcFullShow())
                .setResultLength(brokyLogProperties.getResultLength())
                .build();
        return new BrokyLogAspect(brokyLogHandler, objectMapper, logConfig);
    }
}
