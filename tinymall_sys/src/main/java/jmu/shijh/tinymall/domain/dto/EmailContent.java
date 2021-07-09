package jmu.shijh.tinymall.domain.dto;

import jmu.shijh.tinymall.common.util.Cl;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class EmailContent implements Serializable {
    private String title;
    private String content;
    private List<String> to;

    public EmailContent to(String... to) {
        this.to = Cl.list(to);
        return this;
    }
}
