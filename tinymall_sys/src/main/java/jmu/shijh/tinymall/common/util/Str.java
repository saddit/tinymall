package jmu.shijh.tinymall.common.util;

import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.helpers.MessageFormatter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Str {
    //language=RegExp
    public static final String EMAIL_REGEX = ".*@.*\\..*";
    //language=RegExp
    public static final String PHONE_REGEX = "[0-9]{11}";

    public static StringBuilder builder() {
        return new StringBuilder();
    }

    public static String f(String format, Object... args) {
        return MessageFormatter.arrayFormat(format,args).getMessage();
    }

    public static String toUnderscore(String camelStr) {
        char[] chars = camelStr.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                builder.append('_').append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "url decode error";
        }
    }
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s,"utf-8");
        } catch (UnsupportedEncodingException e) {
            return "url encode error";
        }
    }

    /**
     * 经测试效率最高的剪裁方法
     */
    public static String sub(String s, int begin, int end) {
        return new String(s.toCharArray(), begin, end);
    }

    public static String removeEnd(String s) {
        return sub(s, 0, s.length()-1);
    }

    public static String removeHead(String s) {
        return sub(s, 1, s.length());
    }

    public static String hexEncode(byte[] bytes) {

        return HexUtils.toHexString(bytes);
    }

    public static byte[] hexDecode(String s) {
        return HexUtils.fromHexString(s);
    }

    public static String b64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] b64Decode(String s) {
        return Base64.getDecoder().decode(s);
    }

    /**
     * 如果为null或者空字符串都认为是Empty
     *
     * @param s
     * @return
     */
    public static boolean empty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean notEmpty(Object s) {
        if (s instanceof String) {
            return !empty((String) s);
        } else {
            return s != null;
        }
    }

    /**
     * 正则匹配，返回所有符合条件的表达式 <br/>
     *
     * @param regex 正则表达式
     * @param str 目标字符串
     * @return <b>String[]</b> 所有匹配到的字符串
     */
    public static String[] match(String regex, String str) {
        Matcher matcher = Pattern.compile(regex).matcher(str);
        List<String> groups = new ArrayList<>();
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String s = matcher.group(i);
                groups.add(s);
            }
        }
        return groups.toArray(Cl.emptyArr(String.class));
    }

    /**
     * 返回正则匹配的第一个值，如果没有匹配到任何值则返回null
     *
     * @param regex 正则表达式
     * @param str 目标字符串
     * @return String or null
     */
    public static String matchFst(String regex, String str) {
        String[] groups = match(regex, str);
        return groups.length > 0 ? groups[0] : null;
    }
}
