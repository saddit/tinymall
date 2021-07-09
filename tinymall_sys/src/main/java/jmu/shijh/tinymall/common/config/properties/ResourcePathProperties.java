package jmu.shijh.tinymall.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PrivateKey;

/**
 * @author 显卡的香气
 */
@Data
@ConfigurationProperties(prefix = "resource")
public class ResourcePathProperties {
    private String videoPath;
    private String videoPattern;

    private String imagePath;
    private String imagePattern;

    private String htmlPath;
    private String htmlPattern;

    private String apiAddress;

}
