package com.zy.foundation.lang.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class CurlUtils {

    static String[] cmdParts1 = {"curl", "-H", "Host: www.chineseconverter.com", "-H", "Cache-Control: max-age=0", "--compressed", "https://www.chineseconverter.com/zh-cn/convert/chinese-stroke-order-tool"};
    static String[] cmdParts2 = {"curl", "-H", "Cache-Control: max-age=0", "--compressed", "https://www.chineseconverter.com/zh-cn/convert/chinese-stroke-order-tool"};

    public static void main(String[] args) {
        // 成功
//        System.out.println(execCmdParts(cmdParts1));
        // 成功
//        System.out.println(execCmdParts(cmdParts2));
        // 失败
//        System.out.println(execCmd(String.join(" ", cmdParts1)));
        // 成功
        System.out.println(execCmd(String.join(" ", cmdParts2)));
    }

    public static String execCmdParts(String[] cmdParts) {
        ProcessBuilder process = new ProcessBuilder(cmdParts);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();
        } catch (IOException e) {
            log.error("failed to execute execCmdParts.", e);
        }
        return null;
    }

    private static String execCmd(String command) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("line=" + line);
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            log.error("failed to execute execCmd.", e);
        }
        return output.toString();
    }

}
