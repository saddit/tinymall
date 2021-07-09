package jmu.shijh.tinymall.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jmu.shijh.tinymall.TinymallApplication;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.domain.dto.LoginDTO;
import jmu.shijh.tinymall.domain.dto.UserDTO;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import jmu.shijh.tinymall.mapper.CategoryMapper;
import jmu.shijh.tinymall.mapper.UserInfoMapper;
import jmu.shijh.tinymall.mq.config.OrderMQ;
import jmu.shijh.tinymall.mq.sender.OrderMessageSender;
import jmu.shijh.tinymall.service.ProductService;
import jmu.shijh.tinymall.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.PrintingResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TinymallApplication.class)
class UserInfoServiceImplTest {

    @Autowired
    private UserInfoService service;
    @Autowired
    SessionsSecurityManager securityManager;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    private MockMvc mock;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OrderMessageSender orderMessageSender;
    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Test
    void reduceStock() throws Exception {
        System.out.println(productService.reduceStockBatch(Cl.list(1402921274946228224L, 1411611386240491521L, 1412267402690953218L, 1412961567888347137L, 1413312774041489410L), 1));
    }

    @Test
    void delayMessage() throws Exception {
        orderMessageSender.delaySendOrder(123L, Times.SEC * 10, OrderMQ.ORDER_CANCEL_TYPE);
    }

    @Test
    void cate() throws Exception {
        System.out.println(categoryMapper.selectCateProd());
    }

    @Test
    void getUserInfo() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("123");
        System.out.println(service.getUserInfo(userDTO));
    }
    
    @Test
    void testLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword("123");
        loginDTO.setUsername("shijh");
        loginDTO.setVerifyCode("123");
        mock.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(loginDTO))
                                        
        ).andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    void insert() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("test");
        userInfo.setPassword("test");
        userInfo.setEmail("test");
        userInfo.setPhone("test");
        userInfoMapper.insert(userInfo);
        System.out.println(userInfo.getUserId());
    }
}