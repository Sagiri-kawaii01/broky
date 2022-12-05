package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyResult;

/**
 * @author LGZ
 * <p>
 */
public interface BrokyExceptionHandler {
    BrokyResult handle(Exception e);
}
