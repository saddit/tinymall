package jmu.shijh.tinymall.domain.vo;

import jmu.shijh.tinymall.domain.entity.ShopCar;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopCarVO implements Serializable {
    private Long shopId;
    private String shopName;
    private String shopIcon;
    private List<CarItem> items;
}
