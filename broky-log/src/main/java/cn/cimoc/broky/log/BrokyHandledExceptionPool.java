package cn.cimoc.broky.log;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LGZ
 * <p>
 */
public class BrokyHandledExceptionPool {
    private static final Set<Class<? extends Throwable>> pool = new HashSet<>();

    public void addHandledException(Class<? extends Throwable> e) {
        pool.add(e);
    }

    public boolean exist(Class<? extends Throwable> e) {
        return pool.contains(e);
    }
}
