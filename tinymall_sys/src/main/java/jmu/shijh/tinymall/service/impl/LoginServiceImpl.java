package jmu.shijh.tinymall.service.impl;


import jmu.shijh.tinymall.mapper.UserInfoMapper;
import jmu.shijh.tinymall.service.LoginService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserInfoMapper userInfoMapper;

}
