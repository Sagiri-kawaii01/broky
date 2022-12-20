package cn.cimoc.broky.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author LGZ
 * <p>
 */
@Aspect
public class BrokyLogAspect {
    private final BrokyLogHandler logHandler;

    private final ObjectMapper objectMapper;

    private final LogConfig logConfig;

    public BrokyLogAspect(BrokyLogHandler logHandler, ObjectMapper objectMapper, LogConfig logConfig) {
        this.logHandler = logHandler;
        this.objectMapper = objectMapper;
        this.logConfig = logConfig;
    }

    @Pointcut(value = "(@within(cn.cimoc.broky.log.BrokyLog) || @annotation(cn.cimoc.broky.log.BrokyLog))")
    public void logCut() {

    }

    @Around(value = "logCut()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        long startAt = System.currentTimeMillis();
        Object keys = pjp.proceed();
        long endAt = System.currentTimeMillis();
        BrokyLogHandlerConfig handlerConfig = new BrokyLogHandlerConfigBuilder().setStartAt(startAt).setEndAt(endAt).build();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        BrokyLog anno = method.getAnnotation(BrokyLog.class);
        if (anno == null) {
            anno = method.getDeclaringClass().getAnnotation(BrokyLog.class);
        }
        setRuntimeFromAnnotation(anno, method, handlerConfig);
        if (needLog(handlerConfig)) {
            logHandler.handler(pjp, keys, null, handlerConfig);
        }
        return keys;
    }

    @AfterThrowing(pointcut = "logCut()", throwing = "e")
    public void doExceptionMyLog(JoinPoint jp, Throwable e) {
        logHandler.handler(jp, null, e, new BrokyLogHandlerConfigBuilder().build());
    }

    private void setRuntimeFromAnnotation(BrokyLog anno, Method method, BrokyLogHandlerConfig handlerConfig) {
        if (anno.runTime() == -1) {
            handlerConfig.setRunTime(anno.runTime());
        }
    }

    protected boolean needLog(BrokyLogHandlerConfig handlerConfig) {
        return handlerConfig.getEndAt() - handlerConfig.getStartAt() >= handlerConfig.getRunTime();
    }

    @Getter
    public static class LogConfig {
        private LogConfig(Long runTime, Boolean excFullShow, Integer resultLength){
            this.runTime = runTime;
            this.excFullShow = excFullShow;
            this.resultLength = resultLength;
        }
        private final Long runTime;
        private final Boolean excFullShow;
        private final Integer resultLength;
    }

    public static class LogConfigBuilder {
        private Long runTime = null;
        private Boolean excFullShow = null;
        private Integer resultLength = null;
        public LogConfigBuilder() {

        }
        public LogConfigBuilder setRunTime(Long runTime) {
            if (null == runTime) {
                runTime = 0L;
            }
            this.runTime = runTime;
            return this;
        }

        public LogConfigBuilder setExcFullShow(Boolean excFullShow) {
            if (null == excFullShow) {
                excFullShow = true;
            }
            this.excFullShow = excFullShow;
            return this;
        }

        public LogConfigBuilder setResultLength(Integer resultLength) {
            if (null == resultLength) {
                resultLength = 0;
            }
            this.resultLength = resultLength;
            return this;
        }

        public LogConfig build() {
            return new LogConfig(runTime, excFullShow, resultLength);
        }
    }

    private class BrokyLogHandlerConfigBuilder {
        private BrokyLogHandlerConfigBuilder(){}
        private Long startAt = 0L;
        private Long endAt = 0L;
        public BrokyLogHandlerConfigBuilder setStartAt(Long startAt) {
            this.startAt = startAt;
            return this;
        }

        public BrokyLogHandlerConfigBuilder setEndAt(Long endAt) {
            this.endAt = endAt;
            return this;
        }

        public BrokyLogHandlerConfig build() {
            return new BrokyLogHandlerConfig(objectMapper, logConfig.runTime, logConfig.excFullShow, logConfig.resultLength, this.startAt, this.endAt);
        }
    }
}
