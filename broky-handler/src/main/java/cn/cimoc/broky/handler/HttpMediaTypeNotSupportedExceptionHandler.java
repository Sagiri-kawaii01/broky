package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public interface HttpMediaTypeNotSupportedExceptionHandler {
    BrokyResult handle(HttpMediaTypeNotSupportedException e);
}
