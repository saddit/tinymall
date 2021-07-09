package jmu.shijh.tinymall.common.sqlbuilder.enums;

public enum Rule {
    EQ("="),NEQ("!="),
    GE(">="),GT(">"),
    LE("<="),LT("<"),
    ISN("is NULL"),ISNN("is not NULL"),
    REGEXP("regexp"),
    LIKE_R("like"),
    LIKE_LR("like"),
    LIKE_L("like");

    String name;
    Rule(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
