package jmu.shijh.tinymall.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import jmu.shijh.tinymall.domain.entity.OrdersDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sjh
 */
@Data
public class OrdersVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableId(type = IdType.INPUT)
    private Long ordersId;
    private Long userId;
    private Long productUid;

    /**
     * 订单总价格
     */
    private BigDecimal totalPrice;

    /**
     * 优惠减免价格
     */
    private BigDecimal discountPrice;

    /**
     * 状态 0-未支付 1-待发货 2-待收货 3-待退货 4-已退货 5-已完成 6-失效
     */
    private Integer ordersStatus;

    /**
     * 支付单号
     */
    private Long payId;

    /**
     * 支付类型 银行卡 微信 支付宝
     */
    private String payType;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 收货时间
     */
    private LocalDateTime finishTime;

    /**
     * 快递单号
     */
    private String expressId;

    /**
     * 快递公司名
     */
    private String expressName;

    /**
     * 发货时间
     */
    private LocalDateTime expressTime;

    /**
     * 发货地址
     */
    private String expressAddress;

    /**
     * 运费价格
     */
    private BigDecimal expressPrice;

    /**
     * 用户收货地址id
     */
    private Long addressId;

    //VIEW OBJECT
    private List<OrdersDetailVO> items;
    private String shopName;
    private String shopIcon;
}
