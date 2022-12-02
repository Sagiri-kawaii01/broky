package cn.cimoc.broky.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author LGZ
 * <p>
 */
@Data
@AllArgsConstructor
public class BrokyLogHandlerConfig {
    private ObjectMapper objectMapper;
    private Long runTime;
    private Boolean excFullShow;
    private Integer resultLength;
    private Long startAt;
    private Long endAt;
}
