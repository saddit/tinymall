package jmu.shijh.tinymall.common.util;

import com.rabbitmq.client.AMQP;
import jmu.shijh.tinymall.domain.dto.MonthIncomeDTO;
import lombok.experimental.UtilityClass;

import java.io.Serializable;

@UtilityClass
public class RedisKeys {
    public static final String ZSET_ACTIVITY_KEY = ":activity:zset";

    private final String LOGIN_CODE_SUFFIX = ":login:code";
    private final String LOGIN_TOKEN_SUFFIX = ":login:token";
    private final String LOGIN_SMS_CODE_SUFFIX = ":login:sms:code";
    private final String REGISTER_EMAIL_CODE_SUFFIX = ":register:email:code";
    private final String REGISTER_SMS_CODE_SUFFIX = ":register:sms:code";
    private final String HOT_PRODUCT_SUFFIX = ":product:hot:list";
    private final String HOT_ACTIVITY_SUFFIX = ":activity:hot:list";
    private final String SHOP_ACTIVITY_SUFFIX = ":shop:activity";
    private final String CATE_ALL_SUFFIX = ":category:all";
    private final String CATE_SHOP_PV_SUFFIX = ":category:shop:pv";
    private final String PV_ACTIVITY_SUFFIX = ":activity:pv";
    private final String INCOME_MONTH_SUFFIX = ":income:month";
    private final String RECOMMEND_PRODUCT_SUFFIX = ":product:recommend:list";

    public String getLoginTokenKey(Serializable uniqueId) {
        return get(uniqueId, LOGIN_TOKEN_SUFFIX);
    }

    public String getLoginCodeKey(Serializable uniqueId) {
        return uniqueId.hashCode() + LOGIN_CODE_SUFFIX;
    }

    public String getLoginSmsKey(Serializable uniqueId) {
        return get(uniqueId, LOGIN_SMS_CODE_SUFFIX);
    }

    public String getCategoryAllKey() {
        return get("", CATE_ALL_SUFFIX);
    }

    public String getCategoryPvKey(Serializable shopId) {
        return get(shopId, CATE_SHOP_PV_SUFFIX);
    }

    public String getRegisterEmailKey(Serializable uniqueId) {
        return uniqueId.hashCode() + REGISTER_EMAIL_CODE_SUFFIX;
    }

    public String getMonthIncomeKey(Serializable dto) {
        return dto.hashCode() + INCOME_MONTH_SUFFIX;
    }

    public String getActivityPvKey(Serializable uniqueId) {
        return get(uniqueId, PV_ACTIVITY_SUFFIX);
    }

    public String getActivityHotKey() {
        return get(0,HOT_ACTIVITY_SUFFIX);
    }

    public String getRegisterSmsKey(Serializable uniqueId) {
        return uniqueId.hashCode() + REGISTER_SMS_CODE_SUFFIX;
    }

    public String getProductHotKey() {
        return HOT_PRODUCT_SUFFIX;
    }

    public String getShopActivityKey(Serializable uniqueId) {
        return uniqueId.hashCode() + SHOP_ACTIVITY_SUFFIX;
    }

    private String get(Serializable id, String suffix) {
        return id.hashCode() + suffix;
    }
}
