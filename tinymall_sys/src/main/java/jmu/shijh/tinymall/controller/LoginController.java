package jmu.shijh.tinymall.controller;

import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.exception.LoginFailureException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.LoginDTO;
import jmu.shijh.tinymall.domain.dto.UserDTO;
import jmu.shijh.tinymall.service.UserInfoService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.shiro.sms.SmsToken;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private HttpSession session;

    @GetMapping(value = "/validateCode/{width}/{height}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getValidateCode(@PathVariable(value = "width", required = false) Integer width,
                                @PathVariable(value = "height", required = false) Integer height,
                                HttpServletResponse response) throws IOException {
        if (width == null || height == null) {
            width = 120;
            height = 25;
        } else if (width < 10 || height <10){
            width = 120;
            height = 25;
        }
        ValidateCode validateCode = new ValidateCode(width, height, 4);
        redisTemplate.opsForValue()
                .set(RedisKeys.getLoginCodeKey(session.getId()), validateCode.getCode().toLowerCase(), Duration.ofMinutes(3));
        ImageIO.write(validateCode.getBuffImg(), "jpeg", response.getOutputStream());
    }

    @PostMapping("/phone/code")
    public JsonResp getSmsCode(@RequestBody String phone) {
        if (userInfoService.getUserInfo(new UserDTO().setPhone(phone)) == null) {
            return R.error().msg("手机号不存在").build();
        }
        String code = new ValidateCode(5).getCode();
        redisTemplate.opsForValue().set(RedisKeys.getLoginSmsKey(phone), code, Duration.ofMinutes(5));
        //TODO 使用sms消息队列发送验证码
        return R.ok().msg("发送成功,5分钟内有效").build();
    }

    private void verifyCode(LoginDTO dto) {
        String code = (String) redisTemplate.opsForValue()
                .get(RedisKeys.getLoginCodeKey(session.getId()));
        if (!dto.getVerifyCode().toLowerCase().equals(code)) {
            throw new LoginFailureException("验证码错误");
        }
        redisTemplate.delete(RedisKeys.getLoginCodeKey(session.getId()));
    }

    private String generalLogin(AuthenticationToken token, String failureMsg) throws LoginFailureException {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            throw new LoginFailureException(failureMsg);
        }
        UserIdentity principal = (UserIdentity) subject.getPrincipal();
        String lToken = (String) redisTemplate.opsForValue().get(RedisKeys.getLoginTokenKey(principal.getId()));
        if (lToken == null) {
            try {
                lToken = CryptoUtils.encryptAES(Jwt.generateToken(principal.getId()), CryptoKeys.AES_KEY);
            } catch (GeneralSecurityException e) {
                return null;
            }
            redisTemplate.opsForValue().set(RedisKeys.getLoginTokenKey(principal.getId()), lToken);
        }
        return lToken;
    }

    @PostMapping("/login")
    @ParamCheck(value = "账号密码和验证码不能为空", exclude = {"isRememberMe"})
    public JsonResp login(@RequestBody LoginDTO loginDTO) {
        verifyCode(loginDTO);
        UsernamePasswordToken upToken = new UsernamePasswordToken();
        upToken.setPassword(loginDTO.getPassword().toCharArray());
        upToken.setUsername(loginDTO.getUsername());
        upToken.setRememberMe(loginDTO.getIsRememberMe());
        String token = generalLogin(upToken, "用户名或密码错误");
        UserIdentity principal = (UserIdentity) SecurityUtils.getSubject().getPrincipal();
        return R.ok().msg("登陆成功").data(
                Cl.map().add("token", token)
                        .add("userId", principal.getUserId())
                        .add("username", principal.getUsername())
                        .add("roleId", principal.getRole().getRoleId())
                        .build()
        ).build();
    }

    @PostMapping("/login/phone")
    @ParamCheck(value = "手机号和验证码不能为空", include = {"username", "password"})
    public JsonResp loginByPhone(@RequestBody LoginDTO dto) {
        SmsToken smsToken = new SmsToken(dto.getUsername(), dto.getPassword());
        String token = generalLogin(smsToken, "手机号不存在或验证码错误");
        UserIdentity principal = (UserIdentity) SecurityUtils.getSubject().getPrincipal();
        return R.ok().msg("登陆成功").data(
                Cl.map().add("token", token)
                        .add("userId", principal.getUserId())
                        .add("username", principal.getUsername())
                        .build()
        ).build();
    }

    @GetMapping("/logout")
    public JsonResp logout() {
        SecurityUtils.getSubject().logout();
        return R.ok().build();
    }

}