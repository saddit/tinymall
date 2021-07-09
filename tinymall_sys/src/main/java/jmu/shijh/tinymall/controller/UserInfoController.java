package jmu.shijh.tinymall.controller;


import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.annotation.ParamChecks;
import jmu.shijh.tinymall.common.annotation.PrimaryField;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.EmailContent;
import jmu.shijh.tinymall.domain.dto.RegisterDTO;
import jmu.shijh.tinymall.domain.dto.UserBaseUpdate;
import jmu.shijh.tinymall.domain.dto.UserPrimaryUpdate;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import jmu.shijh.tinymall.domain.vo.UserVO;
import jmu.shijh.tinymall.mq.sender.EmailMessageSender;
import jmu.shijh.tinymall.service.UserInfoService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.Duration;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {
    public static final String DEFAULT_AVATAR = "https://i0.hdslb.com/bfs/article/276d75e536d2ad801dfa8ff02484cd5365b530e2.png@270w_270h_progressive.webp";
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private EmailMessageSender emailMessageSender;
    @Autowired
    private UserInfoService userService;
    @Autowired
    private UserIdentity user;

    @GetMapping("/reg/email/{email}")
    @ParamCheck(value = "格式有误", regexp = Str.EMAIL_REGEX)
    public JsonResp emailReg(@PathVariable String email) {
        ValidateCode code = new ValidateCode(5);
        redisTemplate.opsForValue().set(RedisKeys.getRegisterEmailKey(email), code.getCode(), Duration.ofMinutes(5));
        emailMessageSender.send(
                new EmailContent()
                        .setTitle("验证码，请在5分钟内使用")
                        .setContent(Str.f("验证码：{}",code.getCode()))
                        .to(email)
        );
        return R.ok().msg("发送成功").build();
    }

    @PostMapping("/update/pwd")
    @ParamChecks({
        @ParamCheck(include = "newPrimary",regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", value = "密码必须八位以上,且包含英文与数字"),
        @ParamCheck(include = "oldPrimary")
    })
    @RequiresPermissions("userinfo:update")
    public JsonResp changePwd(@RequestBody(required = false) UserPrimaryUpdate primaryInfo) {
        String oldPwd = primaryInfo.getOldPrimary();
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getUsername(), oldPwd));
        } catch (AuthenticationException e) {
            throw new CustomException("旧密码不正确");
        }
        if (!userService.updateById(
                new UserInfo().setUserId(user.getId())
                            .setPassword(CryptoUtils.MD5Hash(primaryInfo.getOldPrimary(), user.getSalt()).toHex())
        )) {
            throw new CustomException("修改失败");
        }
        emailMessageSender.send(new EmailContent().to(user.getEmail())
                .setTitle("密码变更通知")
                .setContent("您的账号于" + Times.stringOfNow() + "变更了密码")
        );
        return R.ok().build();
    }

    @PostMapping("/update/email")
    @ParamCheck
    @RequiresPermissions("userinfo:update")
    public JsonResp changeEmail(@RequestBody(required = false) UserPrimaryUpdate primaryInfo) {
        verifyEmail(primaryInfo.getOldPrimary(), primaryInfo.getOldCode());
        verifyEmail(primaryInfo.getNewPrimary(), primaryInfo.getNewCode());
        if (!userService.updateById(
                new UserInfo().setUserId(user.getId())
                        .setEmail(primaryInfo.getNewPrimary())
        )) {
            throw new CustomException("变更失败");
        }
        emailMessageSender.send(new EmailContent().to(primaryInfo.getOldPrimary())
                .setTitle("邮箱变更通知")
                .setContent("您的账号于" + Times.stringOfNow() + "弃用了该邮箱")
        );
        emailMessageSender.send(new EmailContent().to(primaryInfo.getNewPrimary())
                .setTitle("邮箱变更通知")
                .setContent("您的账号于" + Times.stringOfNow() + "成功绑定该邮箱")
        );
        return R.ok().build();
    }

    @GetMapping("/check/{name}/{value}")
    public JsonResp checkExist(@PathVariable("name") String name,
                               @PathVariable("value") String value) {
        userService.avoidExist(name,value);
        return R.ok().data("ok").build();
    }

    private void verifyEmail(String email, String code) throws CustomException{
        String vCode = (String) redisTemplate.opsForValue().get(RedisKeys.getRegisterEmailKey(email));
        if (!code.equals(vCode)) {
            throw new CustomException("邮箱验证码有误");
        }
        redisTemplate.delete(RedisKeys.getRegisterEmailKey(email));
    }

    @PostMapping("/register")
    @ParamChecks({
            @ParamCheck(include = "password",regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", value = "密码必须八位以上,且包含英文与数字"),
            @ParamCheck(include = "username",regexp = "[A-Za-z]+[0-9]+", value = "用户名必须以英文开头,不能使用特殊符号"),
            @ParamCheck(include = "email", regexp = Str.EMAIL_REGEX, value = "邮箱格式有误"),
            @ParamCheck(include = "phone", regexp = Str.PHONE_REGEX, value = "电话号码不合法"),
            @ParamCheck(include = {"emailCode"})
    })
    public JsonResp registerUser(@RequestBody(required = false) RegisterDTO info) {
        userService.avoidExist("username", info.getUsername());
        userService.avoidExist("phone", info.getPhone());
        userService.avoidExist("email", info.getEmail());
        verifyEmail(info.getEmail(), info.getEmailCode());
        //TODO verifyPhone
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(info, userInfo);
        userInfo.setAvatar(DEFAULT_AVATAR);
        userService.addNewUser(userInfo);
        return R.ok().msg("注册成功").data(userInfo.getUserId()).build();
    }

    @PostMapping("/update")
    @RequiresPermissions("userinfo:update")
    @ParamCheck(include = "userDesc", required = false, lengthLE = 150, value = "简介不能超过150字")
    public JsonResp updateInfo(@RequestBody(required = false) UserBaseUpdate userInfo) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(userInfo, info);
        info.setUserId(user.getId());
        boolean s = userService.updateById(info);
        if (!s) throw new CustomException("更新失败");
        return R.ok().msg("更新成功").build();
    }

    @GetMapping("/query")
    @RequiresPermissions("userinfo:select")
    public JsonResp getUserInfo() {
        UserInfo userInfo = userService.getUserInfo(user.getUserId().toString());
        UserVO info = new UserVO();
        BeanUtils.copyProperties(userInfo,info);
        String lToken = (String) redisTemplate.opsForValue().get(RedisKeys.getLoginTokenKey(user.getId()));
        info.setToken(lToken);
        info.setRoleId(userInfo.getRoleId());
        return R.ok().data(info).build();
    }
}
