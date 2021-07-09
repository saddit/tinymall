package jmu.shijh.tinymall.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDTO {

    private static final long serialVersionUID = 1L;
    private Long userId;

    /**
     * 唯一用户名
     */
    private String username;

    /**
     * 唯一电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;


    /**
     * 用户角色
     */
    private Long roleId;

    /**
     * 0-正常 1-封号
     */
    private Integer userStatus;
}
