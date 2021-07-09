package jmu.shijh.tinymall.domain.enums;

public enum ProductStatus {
    NORMAL(0),
    NO_DEPLOY(1),
    INVALID(2);

    Integer code;

    ProductStatus(int code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }

    public boolean equal(Integer status) {
        return code.equals(status);
    }
}
