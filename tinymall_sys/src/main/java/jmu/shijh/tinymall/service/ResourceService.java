package jmu.shijh.tinymall.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Function;

/**
 * @author sjh
 */
public interface ResourceService {

    /**
     * 上传资源
     * @param file 文件
     * @param pattern 访问资源的 url 前置
     * @param path 保存资源文件的路径
     * @param checkMethod 文件类型检查方法
     * @return 资源访问路径
     * @throws IOException 文件存取异常
     */
    String upload(MultipartFile file, String pattern, String path, Function<String, Boolean> checkMethod) throws IOException;
}
