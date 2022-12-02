package cn.cimoc.broky.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author LGZ
 * <p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrokyResult implements Serializable {
    protected Integer errCode;
    protected String errMsg;
    protected Object data;
}
