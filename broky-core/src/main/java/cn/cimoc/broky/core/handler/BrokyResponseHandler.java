package cn.cimoc.broky.core.handler;

import cn.cimoc.broky.core.BrokyResult;
import cn.cimoc.broky.core.BrokyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
/**
 * @author LGZ
 * <p>
 */
@Slf4j
@ControllerAdvice
public class BrokyResponseHandler extends BaseBrokyResponseHandler {

    public BrokyResponseHandler() {
        this(DEFAULT_ANN);
    }

    public BrokyResponseHandler(String ann) {
        super(ann);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof BrokyResult) {
            // 如果返回的数据已经被封装，直接返回
            return o;
        }
        // 其余常规的数据返回，直接调用工具类封装
        return BrokyUtils.ajaxReturn(o);
    }
}
