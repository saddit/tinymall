package jmu.shijh.tinymall.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPrimaryUpdate implements Serializable {
    private String oldPrimary;
    private String newPrimary;
    private String oldCode;
    private String newCode;
}
