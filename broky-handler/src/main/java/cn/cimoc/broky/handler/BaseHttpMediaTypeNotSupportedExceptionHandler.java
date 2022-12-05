package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public abstract class BaseHttpMediaTypeNotSupportedExceptionHandler implements BrokyExceptionHandler {
    abstract BrokyResult handle(HttpMediaTypeNotSupportedException e);

    @Override
    public BrokyResult handle(Exception e) {
        return handle((HttpMediaTypeNotSupportedException) e);
    }
}
