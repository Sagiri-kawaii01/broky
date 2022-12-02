package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public interface HttpRequestMethodNotSupportedExceptionHandler {
    BrokyResult handle(HttpRequestMethodNotSupportedException e);
}
