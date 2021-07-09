package jmu.shijh.tinymall.common.sqlbuilder;


import jmu.shijh.tinymall.common.sqlbuilder.enums.LikeType;
import jmu.shijh.tinymall.common.util.Str;
import org.apache.ibatis.jdbc.SQL;

import java.io.IOException;
import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public abstract class BaseQuerySQL<T> {
    private final Object dto;
    protected SQL sql;

    private Boolean camelToUnderscore = null;

    public T useCamelToUnderscore() {
        this.camelToUnderscore = true;
        return (T)this;
    }

    protected boolean isCamelToUnderscore() {
        if(camelToUnderscore != null) {
            return camelToUnderscore;
        }
        return SqlBuilderConfig.useCamelToUnderscore;
    }

    protected boolean isLogicDelete() {
        return SqlBuilderConfig.logicDelete;
    }

    protected String logicColumn() {
        return SqlBuilderConfig.logicDeleteColumn;
    }

    protected String getSqlTableName(String tableNameSql) {
        String[] s = tableNameSql.split(" ");
        String tn = s[0];
        if (s.length == 2) {
            tn = s[1];
        }
        return tn;
    }

    public BaseQuerySQL(Object dto, String tableName) {
        this.dto = dto;
        this.sql = new SQL();
        sql.FROM(tableName);
        if(SqlBuilderConfig.logicDelete) {
            sql.WHERE(getSqlTableName(tableName) + "."+ logicColumn() + "=" + SqlBuilderConfig.logicNotDeleteVal);
        }
    }

    public T groupBy(boolean condition, String... column) {
        if (condition) {
            sql.GROUP_BY(column);
        }
        return (T)this;
    }

    public T join(String... joinSql) {
        sql.JOIN(joinSql);
        return (T)this;
    }

    public T join(Boolean condition, String... joinSql) {
        if(condition) {
            sql.JOIN(joinSql);
        }
        return (T)this;
    }

    public T select(String... columns) {
        sql.SELECT(columns);
        return (T)this;
    }

    public T select(Boolean condition, String... columns) {
        if(condition) {
            sql.SELECT(columns);
        }
        return (T)this;
    }

    public T group(String... columns) {
        sql.GROUP_BY(columns);
        return (T)this;
    }

    public T unsafeWhere(String whereSql) {
        sql.WHERE(whereSql);
        return (T)this;
    }

    public T conditions(Condition... conditions) {
        StringBuilder stringBuilder = new StringBuilder();
        if(conditions.length > 1) {
            stringBuilder.append("(");
        }
        boolean isFst = true;
        for (Condition condition : conditions) {
            if (isNotNULL(condition.getFieldName())) {
                if (isFst) {
                    condition.removeLogic();
                    isFst = false;
                }
                stringBuilder.append(" ").append(condition.toString());
            }
        }
        if(conditions.length > 1) {
            stringBuilder.append(")");
        }
        String s = stringBuilder.toString();
        if (s.equals("()") || s.isEmpty()) {
            return (T) this;
        }
        return unsafeWhere(s);
    }

    public T having(String havingSql) {
        sql.HAVING(havingSql);
        return (T)this;
    }

    public T isNULL(String column) {
        sql.WHERE(Str.f("{} is NULL", column));
        return (T)this;
    }

    private void where(String whereSql, String column, String fieldName) {
        if (column == null) {
            if (isCamelToUnderscore()) {
                column = Str.toUnderscore(fieldName);
            }else {
                column = fieldName;
            }
        }
        if (isNotNULL(fieldName)) {
            sql.WHERE(Str.f(whereSql, column, fieldName));
        }
    }

    private boolean isNotNULL(String fieldName) {
        try {
            Field field = dto.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object o = field.get(dto);
            if (o == null) {
                return false;
            } else if (o instanceof String) {
                return !((String) o).isEmpty();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public T eq(String column, String fieldName) {
        where("{} = #{{}}", column, fieldName);
        return (T)this;
    }

    public T eqVal(String column, Object value) {
        if (value != null) {
            unsafeWhere(column + "=" + value);
        }
        return (T)this;
    }

    public T gtVal(String column, Object value) {
        if(value != null) {
            unsafeWhere(column + ">" + value);
        }
        return (T)this;
    }

    public T neq(String column, String fieldName) {
        where("{} != #{{}}", column, fieldName);
        return (T)this;
    }

    public T le(String column, String fieldName) {
        where("{} <= #{{}}", column, fieldName);
        return (T)this;
    }

    public T lt(String column, String fieldName) {
        where("{} < #{{}}", column, fieldName);
        return (T)this;
    }

    public T ge(String column, String fieldName) {
        where("{} >= #{{}}", column, fieldName);
        return (T)this;
    }

    public T gt(String column, String fieldName) {
        where("{} > #{{}}", column, fieldName);
        return (T)this;
    }

    public T or() {
        sql.OR();
        return (T)this;
    }

    public T like(String column, String fieldName, LikeType type) {
        fieldName = "#{"+fieldName+"}";
        switch (type) {
            case L: fieldName = "%" + fieldName;break;
            case R: fieldName = fieldName + "%";break;
            case LR: fieldName = "%" + fieldName + "%";break;
        }
        where("{} like {}", column, fieldName);
        return (T)this;
    }

    public T regexp(String column, String fieldName) {
        where("{} regexp #{{}}", column, fieldName);
        return (T)this;
    }

    @Override
    public String toString() {
        return sql.toString();
    }
}
