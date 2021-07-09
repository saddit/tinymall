package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * null
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
@Accessors(chain = true)
public class UserInfo implements Serializable {
    /**
     * 
     */
    @TableId
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

    private String userDesc;

    private String nickName;

    /**
     * 密码 md5hash
     */
    private String password;

    /**
     * md5hash salt
     */
    private String salt;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 用户角色
     */
    private Long roleId;

    /**
     * 0-正常 1-封号
     */
    private Integer userStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}