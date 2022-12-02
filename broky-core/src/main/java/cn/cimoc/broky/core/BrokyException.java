package cn.cimoc.broky.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LGZ
 * <p>
 */
@Getter
@Setter
public class BrokyException extends RuntimeException {
    private Integer errCode;

    public BrokyException(Integer errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public BrokyException(BrokyError err) {
        super(err.getErrMsg());
        this.errCode = err.getErrCode();
    }
}
