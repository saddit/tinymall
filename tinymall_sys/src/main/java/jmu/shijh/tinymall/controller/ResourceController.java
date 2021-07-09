package jmu.shijh.tinymall.controller;

import jmu.shijh.tinymall.common.config.properties.ResourcePathProperties;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.CryptoUtils;
import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.R;
import jmu.shijh.tinymall.service.ResourceService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequestMapping("/resource")
@RestController
public class ResourceController {
    @Autowired
    private ResourcePathProperties resourceProp;

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/img/upload")
    @RequiresPermissions("image:upload")
    public JsonResp uploadImg(@RequestBody MultipartFile file) throws IOException {
        String url = resourceService.upload(file,resourceProp.getImagePattern(), resourceProp.getImagePath(),
                Cl.list("jpg", "jpeg", "png", "gif")::contains);
        return R.ok()
                .msg("上传成功")
                .data(resourceProp.getApiAddress() + url)
                .build();
    }

    @PostMapping("/html/upload")
    @RequiresPermissions("html:upload")
    public JsonResp uploadHtml(@RequestBody MultipartFile file) throws IOException {
        String url = resourceService.upload(file, resourceProp.getHtmlPattern(), resourceProp.getHtmlPath(), "html"::equals);
        return R.ok()
                .msg("上传成功")
                .data(resourceProp.getApiAddress() + url)
                .build();
    }
}
