package jmu.shijh.tinymall.common.sqlbuilder;


import jmu.shijh.tinymall.common.annotation.PrimaryField;
import jmu.shijh.tinymall.common.annotation.UpdateField;
import jmu.shijh.tinymall.common.util.Str;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 默认使用 {@link PrimaryField} 注解的属性作为 where 条件 <br/>
 * 指定 <b>keyFieldName</b> 则使用该值作为 where 条件 <br/>
 * 也可使用方法 <b>where()</b> 指定自己编写的 where 子句
 */
@Slf4j
public class UpdateSQL extends BaseUpInSQL<UpdateSQL> {
    private final Object data;
    private String keyFieldName;
    private String whereSql;

    public UpdateSQL(Object data, String tableName) {
        this.data = data;
        this.tableName = tableName;
        if (SqlBuilderConfig.logicDelete) {
            WHERE(getSqlTableName(tableName)+"."+SqlBuilderConfig.logicDeleteColumn+"="+SqlBuilderConfig.logicNotDeleteVal);
        }
    }

    public UpdateSQL(Object data, String tableName, String keyFieldName) {
        this(data,tableName);
        this.keyFieldName = keyFieldName;
    }

    public UpdateSQL where(String whereSql) {
        this.whereSql = whereSql;
        return this;
    }

    @Override
    public String toString() {
        UPDATE(tableName);
        Field[] fields = data.getClass().getDeclaredFields();
        String name = "";
        String idFieldName = "";
        String tableId = "";
        String whereBy = "";
        boolean hasFoundId = false;
        try {
            for (Field field : fields) {
                name = field.getName();
                //1 尝试找主键 主键不能更新
                if (!hasFoundId) {
                    PrimaryField pAnno = field.getDeclaredAnnotation(PrimaryField.class);
                    if (pAnno != null) {
                        hasFoundId = true;
                        idFieldName = name;
                        tableId = pAnno.value().isEmpty() ? name : pAnno.value();
                        continue;
                    }
                }
                UpdateField annotation = field.getDeclaredAnnotation(UpdateField.class);
                //2 判断是否使用自定义的 where 条件
                if (!Str.empty(keyFieldName) && keyFieldName.equals(name)) {
                    whereBy = annotation == null || annotation.value().isEmpty() ? name : annotation.value();
                }
                //3 判断是否需要更新
                if (annotation == null) continue;
                field.setAccessible(true);
                String alias = annotation.value().isEmpty() ? name : annotation.value();
                //4 判断是否驼峰转下划线
                if(isCamelToUnderscore()) alias = Str.toUnderscore(alias);
                if (field.get(data) != null) {
                    SET(Str.f("{}=#{{}}", name, alias));
                }
            }
            if(Str.empty(whereSql)) {
                if (Str.empty(keyFieldName) || Str.empty(whereBy)) {
                    keyFieldName = idFieldName;
                    whereBy = tableId;
                }
                WHERE(Str.f("{}=#{{}}", whereBy, keyFieldName));
            } else {
                WHERE(whereSql);
            }
        } catch (IllegalAccessException e) {
            log.error("update sql build failed, couldn't access {} of {}", name, data.getClass().getSimpleName());
            return null;
        }
        return super.toString();
    }
}
