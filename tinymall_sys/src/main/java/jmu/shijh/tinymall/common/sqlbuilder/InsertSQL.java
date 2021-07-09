package jmu.shijh.tinymall.common.sqlbuilder;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import jmu.shijh.tinymall.common.annotation.PrimaryField;
import jmu.shijh.tinymall.common.annotation.UpdateField;
import jmu.shijh.tinymall.common.util.Cl;
import jmu.shijh.tinymall.common.util.Str;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InsertSQL extends BaseUpInSQL<InsertSQL> {
    private final List<?> data;

    public InsertSQL(List<?> batchData, String tableName) {
        this.data = batchData;
        this.tableName = tableName;
    }

    private String[] getIntoValueArray(String[] nameArray, int index) {
        String[] intoValueNames = new String[nameArray.length];
        for (int i = 0; i<intoValueNames.length; i++) {
            String cur = nameArray[i];
            if (cur.matches(".*\\$")) {
                nameArray[i] = Str.removeEnd(cur);
                //mybatis内部根据是否有L转化成长整形 此处无安全性风险可直接添加
                intoValueNames[i] = String.valueOf(SqlBuilderConfig.snowFlake.generate());
            } else {
                intoValueNames[i] = Str.f("#{list[{}].{}}",index, cur);
            }
        }
        return intoValueNames;
    }

    private String getFieldColumn(Field field, Annotation annotation) {
        String alias = field.getName();
        if(isCamelToUnderscore()) alias = Str.toUnderscore(alias);
        if (annotation instanceof UpdateField) {
            UpdateField anno = (UpdateField) annotation;
            alias = anno.value().isEmpty() ? alias : anno.value();
        }
        else if (annotation instanceof PrimaryField) {
            PrimaryField anno = (PrimaryField) annotation;
            alias = anno.value().isEmpty() ? alias : anno.value();
        }
        return alias;
    }

    @Override
    public String toString() {
        INSERT_INTO(tableName);
        if (data.size() == 0) return null;
        Class<?> dataClass = data.get(0).getClass();
        Field[] fields = dataClass.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>(fields.length);
        boolean hasFoundId = false;
        for (Field field : fields) {
            //找主键 如果主键自增则跳过不插入主键
            if (!hasFoundId) {
                PrimaryField anno = field.getDeclaredAnnotation(PrimaryField.class);
                if (anno != null) {
                    hasFoundId = true;
                    if (anno.snowFlake()) {
                        fieldNames.add(getFieldColumn(field,anno) + "$");
                        continue;
                    } else if (anno.auto()) {
                        continue;
                    } else {
                        fieldNames.add(getFieldColumn(field,anno));
                        continue;
                    }
                }
            }
            //2 判断是否允许插入
            UpdateField upAnno = field.getDeclaredAnnotation(UpdateField.class);
            if (upAnno != null) {
                fieldNames.add(getFieldColumn(field, upAnno));
            }
        }
        String[] names = fieldNames.toArray(Cl.emptyArr(String.class));
        for (int i = 0; i < data.size(); i++) {
            INTO_VALUES(getIntoValueArray(names, i));
        }
        //雪花算法检测规则可能造成对 ID 列名的一些修改 需要处理后在INTO_COLUMNS 看起来不太优雅 但是性能比清晰的划分功能更高
        INTO_COLUMNS(names);
        return super.toString();
    }
}
