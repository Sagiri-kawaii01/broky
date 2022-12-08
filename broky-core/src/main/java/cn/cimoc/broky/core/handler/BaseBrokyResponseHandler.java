package cn.cimoc.broky.core.handler;

import cn.cimoc.broky.core.BrokyResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LGZ
 *
 */
public abstract class BaseBrokyResponseHandler implements ResponseBodyAdvice<Object> {

    public static final String DEFAULT_ANN = "RESPONSE-RESULT-ANN";

    private final String ANN;

    public BaseBrokyResponseHandler() {
        this(DEFAULT_ANN);
    }

    public BaseBrokyResponseHandler(String ann) {
        ANN = ann;
    }

    /**
     * 从request中的attribute判断是否被标记
     * 标记的过程通过拦截器实现
     * supports方法的返回值将决定是否执行下面的beforeBodyWrite方法
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes sra = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        Assert.notNull(sra, "sra is null");
        HttpServletRequest request = sra.getRequest();
        BrokyResponse brokyResponse = (BrokyResponse) request.getAttribute(ANN);
        return brokyResponse != null;
    }
}
