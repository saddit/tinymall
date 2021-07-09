package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author 显卡的香气
 */
@Data
@Accessors(chain = true)
public class RedisCache implements Serializable {
    private Object key;
    private Object value;
    private Duration duration;
}
