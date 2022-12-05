package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public abstract class BaseValidParamExceptionHandler implements BrokyExceptionHandler {
    abstract BrokyResult handle(MethodArgumentNotValidException e);

    @Override
    public BrokyResult handle(Exception e) {
        return handle((MethodArgumentNotValidException) e);
    }
}
