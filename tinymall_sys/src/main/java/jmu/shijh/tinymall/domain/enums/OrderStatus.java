package jmu.shijh.tinymall.domain.enums;

public enum OrderStatus {
    /**
     * 0-未支付
     */
    NO_PAY(0),
    /**
     * 1-待发货
     */
    WAIT_EXPR(1),
    /**
     * 2-待收货
     */
    WAIT_RECEIVE(2),
    /**
     * 3-待退货
     */
    WAIT_REFUND(3),
    /**
     * 4-已退货
     */
    HAS_REFUND(4),
    /**
     * 5-已完成
     */
    FINISH(5),
    /**
     * 6-失效
     */
    INVALID(6);

    Integer code;

    OrderStatus(int code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }

    public boolean equal(Integer status) {
        return code.equals(status);
    }
}
