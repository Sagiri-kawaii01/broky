package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyUtils;
import cn.cimoc.broky.core.BrokyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * @author LGZ
 * <p>
 */
@Slf4j
public class DefaultHttpRequestMethodNotSupportedExceptionHandler implements HttpRequestMethodNotSupportedExceptionHandler{
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public BrokyResult handle(HttpRequestMethodNotSupportedException e) {
        log.warn(e.getMessage());
        if (null == e.getSupportedMethods()) {
            return BrokyUtils.ajaxReturn(405, "不支持的请求方式:" + e.getMethod());
        }
        return BrokyUtils.ajaxReturn(405, "不支持的请求方式:" + e.getMethod() + ", 请使用[" + String.join(", ", e.getSupportedMethods()) + "]");
    }
}
