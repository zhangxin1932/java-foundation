package com.zy.foundation.lang.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LotteryUtils {

    public static void main(String[] args) {
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        List<DayOfWeek> doubleColorList = List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        List<DayOfWeek> grandLottoList = List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY);
        // 一三五买，二四日开，三五一看结果
        if (doubleColorList.contains(dayOfWeek)) {
            doubleColorBall();
        }
        // 日二四买，一三六开, 二四六看结果
        if (grandLottoList.contains(dayOfWeek)) {
            doubleColorBall();
        }
    }

    /**
     * 双色球
     */
    private static void doubleColorBall() {
        int[] redBalls = generateBalls(6, 33);
        int[] blueBalls = generateBalls(1, 16);

        // 输出双色球号码
        System.out.println("【双色球】--------------------------------");
        System.out.println("【双色球】红球号码: " + Arrays.toString(redBalls));
        System.out.println("【双色球】蓝球号码: " + Arrays.toString(blueBalls));
    }

    /**
     * 大乐透
     */
    private static void grandLotto() {
        int[] redBalls = generateBalls(5, 35);
        int[] blueBalls = generateBalls(2, 12);

        // 输出大乐透号码
        System.out.println("【大乐透】--------------------------------");
        System.out.println("【大乐透】红球号码: " + Arrays.toString(redBalls));
        System.out.println("【大乐透】蓝球号码: " + Arrays.toString(blueBalls));
    }

    /**
     * 按顺序生成球的号码，不重复
     * @param count
     * @param range
     * @return
     */
    public static int[] generateBalls(int count, int range) {
        int[] arr = new int[count];
        boolean[] used = new boolean[range + 1];
        for (int i = 0; i < count; i++) {
            int num = ThreadLocalRandom.current().nextInt(1, range + 1);
            while (used[num]) {
                num = ThreadLocalRandom.current().nextInt(1,range + 1);
            }
            arr[i] = num;
            used[num] = true;
        }
        Arrays.sort(arr);
        return arr;
    }

}
