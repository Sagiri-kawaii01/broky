package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public abstract class BaseHttpMessageNotReadableExceptionHandler implements BrokyExceptionHandler{
    abstract BrokyResult handle(HttpMessageNotReadableException e);

    @Override
    public BrokyResult handle(Exception e) {
        return handle((HttpMessageNotReadableException) e);
    }
}
