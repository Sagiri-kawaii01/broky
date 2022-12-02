package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyUtils;
import cn.cimoc.broky.core.BrokyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

/**
 * @author LGZ
 * <p>
 */
@Slf4j
public class DefaultHttpMediaTypeNotSupportedExceptionHandler implements HttpMediaTypeNotSupportedExceptionHandler {
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public BrokyResult handle(HttpMediaTypeNotSupportedException e) {
        log.warn(e.getMessage());
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        Iterator<MediaType> iterator = e.getSupportedMediaTypes().iterator();
        while (iterator.hasNext()) {
            MediaType next = iterator.next();
            builder.append(next.getType()).append('/').append(next.getSubtype());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append(']');
        if (e.getContentType() == null) {
            return BrokyUtils.ajaxReturn(415, "不支持的参数类型");
        }
        return BrokyUtils.ajaxReturn(415, "不支持的参数类型:" + e.getContentType().getType() + "/" + e.getContentType().getSubtype() + ", 支持的参数类型:" + builder);
    }
}
