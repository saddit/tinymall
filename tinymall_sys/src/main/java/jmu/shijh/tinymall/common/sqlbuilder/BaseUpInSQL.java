package jmu.shijh.tinymall.common.sqlbuilder;

import org.apache.ibatis.jdbc.SQL;

@SuppressWarnings("unchecked")
public abstract class BaseUpInSQL<T> extends SQL {
    protected String tableName;
    private Boolean camelToUnderscore = null;

    public T useCamelToUnderscore() {
        this.camelToUnderscore = true;
        return (T)this;
    }

    protected String getSqlTableName(String tableNameSql) {
        String[] s = tableNameSql.split(" ");
        String tn = s[0];
        if (s.length == 2) {
            tn = s[1];
        }
        return tn;
    }

    protected boolean isCamelToUnderscore() {
        if(camelToUnderscore != null) {
            return camelToUnderscore;
        }
        return SqlBuilderConfig.useCamelToUnderscore;
    }
}
