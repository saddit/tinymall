package jmu.shijh.tinymall.common.sqlbuilder;


import jmu.shijh.tinymall.common.sqlbuilder.enums.Logic;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Rule;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Condition {
    private Logic logic;
    private String column;
    private Rule rule;
    private String fieldName;

    public Condition() {
        logic = Logic.AND;
        rule = Rule.EQ;
    }

    public Condition(String column, String fieldName) {
        this();
        this.column = column;
        this.fieldName = fieldName;
    }

    public void removeLogic() {
        logic = Logic.EMPTY;
    }

    public String toString() {
        String expr = "#{" + fieldName + "}";
        switch (rule) {
            case LIKE_L: expr = "%"+expr;break;
            case LIKE_R: expr = expr + "%";break;
            case LIKE_LR: expr = "%"+expr+"%";break;
            default: break;
        }
        return logic + " " + column + " " + rule + " " + expr;
    }

    public static Condition get(Logic logic, String column, Rule rule, String fieldName) {
        return new Condition(logic,column,rule,fieldName);
    }

    public static Condition get(String column,Rule rule, String fieldName) {
        Condition condition = new Condition(column, fieldName);
        condition.setRule(rule);
        return condition;
    }

    public static Condition get(Logic logic, String column, String fieldName) {
        Condition condition = new Condition(column, fieldName);
        condition.setLogic(logic);
        return condition;
    }

    public static Condition get(String column, String fieldName) {
        return new Condition(column,fieldName);
    }

    public static Conditions s() {
        return new Conditions();
    }
}
