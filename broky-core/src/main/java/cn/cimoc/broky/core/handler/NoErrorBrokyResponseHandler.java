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

import java.util.Map;

/**
 * @author LGZ
 * <p>
 */
@Slf4j
@ControllerAdvice
public class NoErrorBrokyResponseHandler extends BaseBrokyResponseHandler{
    public NoErrorBrokyResponseHandler() {
        this(DEFAULT_ANN);
    }

    public NoErrorBrokyResponseHandler(String ann) {
        super(ann);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof BrokyResult) {
            // 如果返回的数据已经被封装，直接返回
            return o;
        }
        if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            if (isErrorResult(map)) {
                return BrokyUtils.ajaxReturn((Integer) map.get("status"), "请求路径:" + map.get("path") + ", 错误信息:" + map.get("error"));
            }
        }
        // 其余常规的数据返回，直接调用工具类封装
        return BrokyUtils.ajaxReturn(o);
    }

    private boolean isErrorResult(Map<String, Object> map) {
        return map.size() == 4 && map.containsKey("status") && map.containsKey("path") && map.containsKey("error") && map.containsKey("timestamp");
    }
}
