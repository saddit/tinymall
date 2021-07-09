package jmu.shijh.tinymall.domain.enums;

public enum ActivityStatus {
    UNDEPLOY(0),
    DEPLOYED(1),
    INVALID(2);

    Integer code;

    ActivityStatus(int code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }

    public boolean equal(Integer status) {
        return code.equals(status);
    }
}
