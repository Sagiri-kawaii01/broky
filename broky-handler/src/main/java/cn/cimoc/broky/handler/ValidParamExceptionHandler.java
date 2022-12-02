package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public interface ValidParamExceptionHandler {
    BrokyResult handle(MethodArgumentNotValidException e);
}
