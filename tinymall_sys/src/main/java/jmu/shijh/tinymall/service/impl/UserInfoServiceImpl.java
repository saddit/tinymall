package jmu.shijh.tinymall.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.exception.UserStateException;
import jmu.shijh.tinymall.common.util.CryptoUtils;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.common.util.Str;
import jmu.shijh.tinymall.domain.dto.UserDTO;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import jmu.shijh.tinymall.mapper.RoleMapper;
import jmu.shijh.tinymall.mapper.UserInfoMapper;
import jmu.shijh.tinymall.service.UserInfoService;
import org.apache.shiro.codec.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    public UserInfoMapper userMapper;

    @Override
    public UserInfo getUserInfo(String key) {
        UserDTO dto = new UserDTO();
        if (key.matches("[0-9]{11}")) {
            dto.setPhone(key);
        } else if (key.matches("[0-9]{19}")) {
            dto.setUserId(Long.valueOf(key));
        } else if (key.matches(Str.EMAIL_REGEX)) {
            dto.setEmail(key);
        } else {
            dto.setUsername(key);
        }
        return getUserInfo(dto);
        //return lambdaQuery().eq(UserInfo::getEmail, key).or()
        //                    .eq(UserInfo::getUsername, key).or()
        //                    .eq(UserInfo::getPhone, key).or()
        //                    .eq(UserInfo::getUserId, key).one();
    }

    @Override
    public UserInfo getUserInfo(UserDTO dto) {
        return userMapper.selectOne(
                Wrappers.lambdaQuery(UserInfo.class)
                        .eq(Str.notEmpty(dto.getEmail()), UserInfo::getEmail, dto.getEmail())
                        .eq(Str.notEmpty(dto.getUsername()), UserInfo::getUsername, dto.getUsername())
                        .eq(Str.notEmpty(dto.getUserId()), UserInfo::getUserId, dto.getUserId())
                        .eq(Str.notEmpty(dto.getPhone()), UserInfo::getPhone, dto.getPhone())
        );
    }

    @Transactional
    @Override
    public void addNewUser(UserInfo userInfo) {
        userInfo.setRoleId(1L);
        String salt = UUID.randomUUID().toString();
        userInfo.setPassword(CryptoUtils.MD5Hash(userInfo.getPassword(), salt).toHex());
        userInfo.setSalt(salt);
        if (!save(userInfo)) {
            throw new CustomException("注册失败");
        }
    }

    @Override
    public void avoidExist(String name, String value) throws CustomException {
        UserDTO dto = new UserDTO();
        switch (name) {
            case "username":
                dto.setUsername(value);
                break;
            case "phone":
                dto.setPhone(value);
                break;
            case "email":
                dto.setEmail(value);
                break;
            default: return;
        }
        if (getUserInfo(dto) != null) {
            throw new CustomException(value + "已被注册");
        }
    }

}
