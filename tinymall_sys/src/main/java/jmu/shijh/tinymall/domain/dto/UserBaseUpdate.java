package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBaseUpdate implements Serializable {
    private Long userId;

    private String userDesc;

    private String nickName;

    /**
     * 头像地址
     */
    private String avatar;
}
