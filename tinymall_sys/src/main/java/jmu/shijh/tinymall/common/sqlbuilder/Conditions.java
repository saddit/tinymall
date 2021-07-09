package jmu.shijh.tinymall.common.sqlbuilder;


import jmu.shijh.tinymall.common.sqlbuilder.enums.Logic;
import jmu.shijh.tinymall.common.sqlbuilder.enums.Rule;

import java.util.LinkedList;
import java.util.List;

public class Conditions {
    private final List<Condition> list;

    public Conditions() {
        this.list = new LinkedList<>();
    }

    public Conditions get(Logic logic, String column, Rule rule, String fieldName) {
        list.add(Condition.get(logic,column,rule,fieldName));
        return this;
    }

    public Conditions get(String column,Rule rule, String fieldName) {
        Condition condition = Condition.get(column, fieldName);
        condition.setRule(rule);
        list.add(condition);
        return this;
    }

    public Conditions get(Logic logic, String column, String fieldName) {
        Condition condition = Condition.get(column, fieldName);
        condition.setLogic(logic);
        list.add(condition);
        return this;
    }

    public Conditions get(String column, String fieldName) {
        list.add(Condition.get(column,fieldName));
        return this;
    }

    public Condition[] toArray() {
        return list.toArray(new Condition[0]);
    }
}
