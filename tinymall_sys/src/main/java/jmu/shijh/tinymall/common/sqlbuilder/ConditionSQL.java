package jmu.shijh.tinymall.common.sqlbuilder;

public class ConditionSQL extends BaseQuerySQL<ConditionSQL>{
    public ConditionSQL(Object dto, String tableName) {
        super(dto, tableName);
    }
    public ConditionSQL c(Condition condition) {
        condition.removeLogic();
        return conditions(condition);
    }

    public ConditionSQL cs(Conditions conditions) {
        return conditions(conditions.toArray());
    }

    public ConditionSQL css(Conditions... conditions) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Conditions condition : conditions) {
            builder.append(cs(condition));
        }
        builder.append(")");
        if (builder.length() > 2) {
            unsafeWhere(builder.toString());
        }
        return this;
    }
}
