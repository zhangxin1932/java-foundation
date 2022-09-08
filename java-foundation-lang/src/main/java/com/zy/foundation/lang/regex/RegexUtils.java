package com.zy.foundation.lang.regex;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public abstract class RegexUtils {
    public static final String REGEX_IPV4 = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

    /**
     *
     * @param str 待校验的字符串
     * @param regex 正则规则: 正则规则可以放入配置文件中定义, 如 classpath 下的 config.properties 文件
     *              [a-zA-Z-9_,.-]  ---->>>>这里匹配数字,字母,下划线,逗号,点,中划线
     * @return 若校验成功, 返回true,否则返回false
     */
    public static boolean validRegex(String str, String regex) {
        if (Objects.isNull(str) || Objects.isNull(regex)) {
            return false;
        }
        return Pattern.compile(regex).matcher(str).matches();
    }

    private static void f1() {
        // String str = "it meet a good endpoint.";
        String str = "it meet a good endpoint.";
        Pattern pattern = Pattern.compile("( a .* endpoint([,;. ]))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            System.out.println(matcher.start());
            System.out.println(matcher.end());
            System.out.println(str.substring(matcher.start(), matcher.end()));
        }
    }

    /**
     * 非贪婪模式
     * 贪婪模式
     */
    private static void f2() {
        // String str = "it meet a good endpoint.";
        String str = "it meet a good endpoint, just a xxx endpoint.";
        // 非贪婪模式: a good endpoint
        // Pattern pattern = Pattern.compile("( a .*? endpoint([,;. ]))", Pattern.CASE_INSENSITIVE);
        // 贪婪模式: a good endpoint, just a xxx endpoint.
        Pattern pattern = Pattern.compile("( a .* endpoint([,;. ]))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            System.out.println(matcher.start());
            System.out.println(matcher.end());
            System.out.println(str.substring(matcher.start(), matcher.end()));
        }
        System.out.println("----------------------------------------------");
        if (matcher.find(10)) {
            System.out.println(matcher.start());
            System.out.println(matcher.end());
            System.out.println(str.substring(matcher.start(), matcher.end()));
        }
    }

    /**
     * 非贪婪模式
     * 贪婪模式
     */
    private static void f3() {
        // String str = "it meet a good endpoint.";
        String str = "you say hello, i say hello.";
        // 非贪婪模式: a good endpoint
        // Pattern pattern = Pattern.compile("( a .*? endpoint([,;. ]))", Pattern.CASE_INSENSITIVE);
        // 贪婪模式: a good endpoint, just a xxx endpoint.
        Pattern pattern = Pattern.compile("( hello([,;. ]))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            System.out.println(matcher.start());
            System.out.println(matcher.end());
            System.out.println(str.substring(matcher.start(), matcher.end()));
        }
        System.out.println("----------------------------------------------");
        if (matcher.find(10)) {
            System.out.println(matcher.start());
            System.out.println(matcher.end());
            System.out.println(str.substring(matcher.start(), matcher.end()));
        }
    }

    public static void main(String[] args) {
        f3();
    }

}
