package jmu.shijh.tinymall.service.impl;

import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.CryptoUtils;
import jmu.shijh.tinymall.service.ResourceService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author sjh
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private UserIdentity user;

    @Override
    public String upload(MultipartFile file, String pattern, String path, Function<String, Boolean> checkMethod) throws IOException {
        File dir = new File(StringUtils.substringAfter(path, "file:///"));
        String basePattern = StringUtils.substringBeforeLast(pattern, "**");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new CustomException("文件创建失败");
        }
        String suffix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        if (!checkMethod.apply(suffix.toLowerCase())) {
            throw new CustomException("不支持" + suffix + "类型");
        }
        String fileName = CryptoUtils
                .MD5Hash(file.getOriginalFilename() + file.getSize(), user.getSalt()) + "." + suffix;
        File save = new File(dir, fileName);
        if (!save.exists()) {
            file.transferTo(save);
        }
        return basePattern + fileName;
    }
}
