package cn.cimoc.broky.core.interceptor;

import cn.cimoc.broky.core.BrokyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author LGZ
 *
 */
@Slf4j
public class BrokyResponseInterceptor implements HandlerInterceptor {

    private final String ANN;

    public BrokyResponseInterceptor() {
        this("RESPONSE-RESULT-ANN");
    }

    public BrokyResponseInterceptor(String ann) {
        ANN = ann;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取将要进入的控制器以及具体方法
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            // 如果控制器被标记或方法被标记，写入attribute
            if (clazz.isAnnotationPresent(BrokyResponse.class)) {
                request.setAttribute(ANN, clazz.getAnnotation(BrokyResponse.class));
            } else if (method.isAnnotationPresent(BrokyResponse.class)) {
                request.setAttribute(ANN, method.getAnnotation(BrokyResponse.class));
            }
        }
        return true;
    }
}
