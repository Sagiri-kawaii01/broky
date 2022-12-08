package cn.cimoc.broky.core;

import cn.cimoc.broky.core.interceptor.BrokyResponseInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LGZ
 *
 */
public class BrokyWebConfig implements WebMvcConfigurer {
    @Resource
    BrokyResponseInterceptor brokyResponseInterceptor;

    /**
     * 关闭默认的消息转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(httpMessageConverter -> httpMessageConverter.getClass() == StringHttpMessageConverter.class);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 使用自定义注解返回数据
        registry.addInterceptor(brokyResponseInterceptor);
    }
}
