package jmu.shijh.tinymall.common.sqlbuilder;


import jmu.shijh.tinymall.common.sqlbuilder.enums.LikeType;

public class QuerySQL extends BaseQuerySQL<QuerySQL> {
    public QuerySQL(Object dto, String tableName) {
        super(dto, tableName);
    }

    public QuerySQL eq(String field) {
        return super.eq(null, field);
    }

    public QuerySQL eqAll(String... fieldNames) {
        for (String field : fieldNames) {
            super.eq(null, field);
        }
        return this;
    }

    public QuerySQL neqAll(String... fieldNames) {
        for (String field : fieldNames) {
            super.neq(null, field);
        }
        return this;
    }

    public QuerySQL neq(String field) {
        return super.neq(null, field);
    }

    public QuerySQL like(String field, LikeType type) {
        return super.like(null, field, type);
    }

    public QuerySQL regexp(String field) {
        return super.regexp(null, field);
    }
}
