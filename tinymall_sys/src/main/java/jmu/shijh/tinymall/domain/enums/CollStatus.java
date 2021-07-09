package jmu.shijh.tinymall.domain.enums;

public enum CollStatus {
    NORMAL,
    INVALID;

    public Integer code() {
        return this.ordinal();
    }

    public boolean equal(Integer status) {
        return this.ordinal() == status;
    }
}
