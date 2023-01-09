package com.xlj.framework.filter.web_security;


import com.xlj.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SQLInjectShield {

    /**
     * 关键字
     */
    public static Map<String, String> SQL_KEY_WORDS;
    /**
     * 特殊字符
     * 原有配置： "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t"
     * 现有配置： 允许,以传入多个排序条件 允许.以允许join表排序
     */
    public static String KES_SYMBOL = "[ _`~!@#$%^&*()+=|{}':;'\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
    private static Pattern p = Pattern.compile(KES_SYMBOL);

    static {
        SQL_KEY_WORDS = new HashMap<>();
        String sqlKeyWordsStr = "add|all|alter|analyze|and|as|asc|asensitive|before|between|bigint|binary|blob|both|by|call|cascade|case|change|char|character|check|collate|column|condition|connection|constraint" +
                "|continue|convert|create|cross|current_date|current_time|current_timestamp|current_user|cursor|database|databases|day_hour|day_microsecond|day_minute|day_second|dec|decimal|declare|default|delayed" +
                "|delete|desc|describe|deterministic|distinct|distinctrow|div|double|drop|dual|each|else|elseif|enclosed|escaped|exists|exit|explain|false|fetch|float|float4|float8|for|force|foreign|from|fulltext" +
                "|goto|grant|group|having|high_priority|hour_microsecond|hour_minute|hour_second|if|ignore|in|index|infile|inner|inout|insensitive|insert|int|int1|int2|int3|int4|int8|integer|interval|into|is|iterate" +
                "|join|key|keys|kill|label|leading|leave|left|like|limit|linear|lines|load|localtime|localtimestamp|lock|long|longblob|longtext|loop|low_priority|match|mediumblob|mediumint|mediumtext|middleint|minute_microsecond" +
                "|minute_second|mod|modifies|natural|not|no_write_to_binlog|null|numeric|on|optimize|option|optionally|or|order|out|outer|outfile|precision|primary|procedure|purge|raid0|range|read|reads|real|references|regexp" +
                "|release|rename|repeat|replace|require|restrict|return|revoke|right|rlike|schema|schemas|second_microsecond|select|sensitive|separator|set|show|smallint|spatial|specific|sql|sqlexception|sqlstate|sqlwarning|sql_big_result" +
                "|sql_calc_found_rows|sql_small_result|ssl|starting|straight_join|table|terminated|then|tinyblob|tinyint|tinytext|to|trailing|trigger|true|undo|union|unique|unlock|unsigned|update|usage|use|using|utc_date|utc_time" +
                "|utc_timestamp|values|varbinary|varchar|varcharacter|varying|when|where|while|with|write|x509|xor|year_month|zerofill";
        SQL_KEY_WORDS = Arrays.stream(sqlKeyWordsStr.split("\\|")).collect(Collectors.toMap(t -> t, t -> t, (t1, t2) -> t1));
    }

    public static boolean containSpecialWord(String str) {
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static void sqlValidate(String param) {
        if (StringUtils.isBlank(param)) {
            return;
        }
        param = param.trim().toLowerCase();

        // 判断是否包含特殊字符
        if (containSpecialWord(param)) {
            throw new ServiceException("您发送请求中的参数中含有非法字符，请检查排序参数");
        }

        // 判断是否包含关键字
        if (SQL_KEY_WORDS.containsKey(param)) {
            throw new ServiceException("您发送请求中的参数中含有非法字符，请检查排序参数");
        }
    }

}
