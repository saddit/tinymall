package jmu.shijh.tinymall.common.aop;


import jmu.shijh.tinymall.common.exception.*;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.R;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public JsonResp sqlError(SQLException e) {
        return R.error()
                .msg("提交数据存在异常")
                .help(e.getMessage())
                .build();
    }

    @ExceptionHandler
    public JsonResp pageException(PageException e) {
        return R.error()
                .msg("分页信息有误")
                .help(e.getMessage())
                .build();
    }

    @ExceptionHandler
    public JsonResp paramError(ParamCheckException e) {
        return R.error()
                .help("检查参数是否缺少")
                .msg(e.getMessage())
                .build();
    }

    @ExceptionHandler({AuthorizationException.class})
    public JsonResp shiroError(Exception e) {
        return R.cus(403)
                .help("请登录正确账号")
                .msg("权限不足")
                .build();
    }

    @ExceptionHandler
    public JsonResp shiroAuthenticationError(AuthenticationException e) {
        return R.cus(4031)
                .help(e.getMessage())
                .msg("登录失败")
                .build();
    }

    @ExceptionHandler
    public JsonResp loginError(UserStateException e) {
        return R.error()
                .msg(e.getMessage())
                .build();
    }

    @ExceptionHandler
    public JsonResp ioe(IOException e) {
        return R.error()
                .msg("请求出错了")
                .help(e.getMessage())
                .build();
    }

    @ExceptionHandler
    public JsonResp customError(CustomException e)   {
        return R.error()
                .msg(e.getMessage())
                .data(Cl.map().add("errorData", e.getErrorData()).build())
                .help(e.getErrorData() == null ? null: "请参考errorData")
                .build();
    }
}
