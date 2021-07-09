package jmu.shijh.tinymall.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jmu.shijh.tinymall.domain.entity.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 唯一用户名
     */
    private String username;


    private String userDesc;

    private String nickName;

    /**
     * 唯一电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatar;

    private String token;

    private Long roleId;

}
