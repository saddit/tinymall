package jmu.shijh.tinymall.common.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResp implements Serializable {
    private Boolean success;
    private Integer code;
    private Object data;
    private String message;
    private String help;
}
