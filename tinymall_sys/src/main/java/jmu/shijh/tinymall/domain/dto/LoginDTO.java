package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    /**
     * 手机号、用户名、邮箱
     */
    private String username;
    private String password;
    private String verifyCode;
    private Boolean isRememberMe = false;
}
