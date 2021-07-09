package jmu.shijh.tinymall.controller;


import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(value = "/oauth/error", produces = "application/json;charset=utf-8")
public class AuthExceptionController {
    @RequestMapping("/token")
    public JsonResp tokenException(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Object exception = request.getAttribute("exception");
            if (exception == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return R.error().build();
            }
            request.removeAttribute("exception");
            throw (AuthenticationException) exception;
        } catch (ClassCastException ignore) {}

        return R.error().msg("Unknown error").build();
    }

}
