package cn.cimoc.broky.core;

/**
 * @author LGZ
 *
 */
public abstract class BaseCase {
    public void success() {
        throw new BrokyException(BrokyError.SUCCESS);
    }

    public void fail(BrokyError error) {
        throw new BrokyException(error);
    }
}
