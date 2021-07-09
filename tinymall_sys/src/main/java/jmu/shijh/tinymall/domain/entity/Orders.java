package jmu.shijh.tinymall.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import jmu.shijh.tinymall.domain.dto.OrderSubmit;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * null
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders implements Serializable {

    /**
     * 订单编号
     */
    @TableId
    private Long ordersId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 卖家商店id
     */
    private Long shopId;

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
     * 申请退款时间
     */
    private LocalDateTime refundTime;

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
     * 1-true
     */
    private Boolean deleted;

    /**
     * 发货地址
     */
    private String expressAddress;

    private Long couponId;

    /**
     * 运费价格
     */
    private BigDecimal expressPrice;

    /**
     * 用户收货地址id
     */
    private Long addressId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Orders() {
        this.totalPrice = BigDecimal.ZERO;
        this.discountPrice = BigDecimal.ZERO;
        this.expressPrice = BigDecimal.ZERO;
    }

    public Orders(long oid, Long userId, Long shopId, Long addressId) {
        this();
        this.ordersId = oid;
        this.shopId = shopId;
        this.userId = userId;
        this.addressId = addressId;
    }
}