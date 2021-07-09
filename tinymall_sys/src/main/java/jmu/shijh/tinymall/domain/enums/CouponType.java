package jmu.shijh.tinymall.domain.enums;

/**
 * @author sjh
 */

public enum CouponType {
    /**
     * 满减
     */
    FULL_CUT(0),
    /**
     * 百分比
     */
    PERCENT(1);

    Integer code;

    CouponType(int code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }

    public boolean equal(Integer status) {
        return code.equals(status);
    }
}
