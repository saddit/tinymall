package jmu.shijh.tinymall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.domain.dto.UserDTO;
import jmu.shijh.tinymall.domain.entity.UserInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shijh
 * @since 2021-05-20
 */
public interface UserInfoService extends IService<UserInfo> {
    UserInfo getUserInfo(String key);
    UserInfo getUserInfo(UserDTO dto);

    void addNewUser(UserInfo userInfo);

    void avoidExist(String name, String value) throws CustomException;
}
