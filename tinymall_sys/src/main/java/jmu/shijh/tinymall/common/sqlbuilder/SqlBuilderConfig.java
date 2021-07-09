package jmu.shijh.tinymall.common.sqlbuilder;


import com.sun.javafx.logging.PulseLogger;
import jmu.shijh.tinymall.common.util.SFIdWoker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SqlBuilderConfig {
    public static Boolean useCamelToUnderscore = null;
    public static SFIdWoker snowFlake = null;
    public static Boolean logicDelete = null;
    public static String logicDeleteColumn = null;
    public static String logicDeleteVal = null;
    public static String logicNotDeleteVal = null;

    @Bean
    public SFIdWoker snowFlakeID(
            @Value("${snowflake.work-id:1}") int workId,
            @Value("${snowflake.data-center-id:1}") int dataCenterId,
            @Value("${snowflake.sequence:1}") int sequence
    ) {
        SFIdWoker snowFlakeID = new SFIdWoker(workId, dataCenterId, sequence);
        snowFlake = snowFlakeID;
        return snowFlakeID;
    }

    @Value("${mybatis-plus.configuration.map-underscore-to-camel-case:false}")
    public void setUseCamelToUnderscore(Boolean b) {
        useCamelToUnderscore = b;
    }

    @Value("${mybatis-plus.global-config.db-config.logic-delete-field:false}")
    public void setLogicDeleteColumn(String column) {
        if(column.equals("false")) {
            logicDelete = false;
            return;
        }
        logicDeleteColumn = column;
        logicDelete = true;
    }

    @Value("${mybatis-plus.global-config.db-config.logic-delete-value:1}")
    public void setLogicDeleteVal(String value) {
        logicDeleteVal = value;
    }

    @Value("${mybatis-plus.global-config.db-config.logic-not-delete-value:1}")
    public void setLogicNotDeleteVal(String value) {
        logicNotDeleteVal = value;
    }
}
