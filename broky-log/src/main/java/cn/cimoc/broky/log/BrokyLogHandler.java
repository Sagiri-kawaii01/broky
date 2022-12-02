package cn.cimoc.broky.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;

/**
 * @author LGZ
 * <p>
 */
public interface BrokyLogHandler {
    void handler(JoinPoint jp, Object keys, Throwable e, BrokyLogHandlerConfig handlerConfig);
}
