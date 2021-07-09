package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {
    /**
     * 唯一用户名
     */
    private String username;

    /**
     * 唯一电话
     */
    private String phone;

    /**
     * 唯一邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像地址
     */
    private String avatar;

    private String emailCode;
    private String smsCode;
}
