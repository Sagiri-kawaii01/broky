package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public abstract class BaseHttpRequestMethodNotSupportedExceptionHandler implements BrokyExceptionHandler {
    abstract BrokyResult handle(HttpRequestMethodNotSupportedException e);

    @Override
    public BrokyResult handle(Exception e) {
        return handle((HttpRequestMethodNotSupportedException) e);
    }
}
