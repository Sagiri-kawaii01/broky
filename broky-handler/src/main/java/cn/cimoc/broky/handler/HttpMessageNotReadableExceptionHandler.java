package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author LGZ
 * <p>
 */
@ControllerAdvice
public interface HttpMessageNotReadableExceptionHandler {
    BrokyResult handle(HttpMessageNotReadableException e);
}
