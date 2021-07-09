package jmu.shijh.tinymall.common.sqlbuilder;

import jmu.shijh.tinymall.common.annotation.PrimaryField;
import jmu.shijh.tinymall.common.annotation.UpdateField;
import jmu.shijh.tinymall.common.util.Str;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;

@Slf4j
public class ConditionUpdateSQL extends BaseQuerySQL<ConditionUpdateSQL>{
    private final Object data;
    private final String tableName;

    public ConditionUpdateSQL(Object data, Object dto, String tableName) {
        super(dto, tableName);
        this.data = data;
        this.tableName = tableName;
        sql = new SQL();
    }

    public ConditionUpdateSQL(Object dto, String tableName) {
        super(dto, tableName);
        this.data = null;
        this.tableName = tableName;
        sql = new SQL();
    }

    public ConditionUpdateSQL setVal(String column, Object value) {
        sql.SET(column + "=" + value.toString());
        return this;
    }

    @Override
    public String toString() {
        sql.UPDATE(tableName);
        if (data == null) return super.toString();
        Field[] fields = data.getClass().getDeclaredFields();
        String name = "";
        boolean hasFoundId = false;
        try {
            for (Field field : fields) {
                name = field.getName();
                //1 尝试找主键 主键不能更新
                if (!hasFoundId) {
                    PrimaryField pAnno = field.getDeclaredAnnotation(PrimaryField.class);
                    if (pAnno != null) {
                        hasFoundId = true;
                        continue;
                    }
                }
                UpdateField annotation = field.getDeclaredAnnotation(UpdateField.class);
                //3 判断是否需要更新
                if (annotation == null) continue;
                field.setAccessible(true);
                String alias = annotation.value().isEmpty() ? name : annotation.value();
                //4 判断是否驼峰转下划线
                if(isCamelToUnderscore()) alias = Str.toUnderscore(alias);
                if (field.get(data) != null) {
                    sql.SET(Str.f("{}=#{{}}", name, alias));
                }
            }
        } catch (IllegalAccessException e) {
            log.error("update sql build failed, couldn't access {} of {}", name, data.getClass().getSimpleName());
            return null;
        }
        return super.toString();
    }
}
