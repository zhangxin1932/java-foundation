package com.zy.foundation.lang;

import com.zy.foundation.lang.utils.TimeUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class TimeUtilsTest {

    @Test
    public void f1() {
        String str = TimeUtils.zonedDateTime2Str(ZonedDateTime.now());
        System.out.println(str);
        System.out.println(TimeUtils.str2ZonedDateTime(str));
    }

    @Test
    public void f2() {
        String str = TimeUtils.localDateTime2Str(LocalDateTime.now());
        System.out.println(str);
        System.out.println(TimeUtils.str2LocalDateTime(str));
    }

    @Test
    public void f3() {
        String str = TimeUtils.localDate2Str(LocalDate.now());
        System.out.println(str);
        System.out.println(TimeUtils.str2LocalDate(str));
    }

    @Test
    public void f4() {
        String str = TimeUtils.zonedDateTime2Str1(ZonedDateTime.now());
        System.out.println(str);
        System.out.println(TimeUtils.str2LocalDate1(str));
    }

    @Test
    public void testOfInstant() {
        System.out.println(LocalDateTime.now());
        System.out.println(TimeUtils.ofInstant(LocalDateTime.now()));
    }

}
