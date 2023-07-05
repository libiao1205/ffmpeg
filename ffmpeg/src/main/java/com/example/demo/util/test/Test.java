package com.example.demo.util.test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author libiao
 * @date 2022/2/28
 */
public class Test {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startLocalDate = LocalDateTime.parse("2022-02-28 12:11",formatter);
        LocalDateTime endLocalDate = LocalDateTime.parse("2022-02-28 17:11",formatter);
        Long time = LocalDateTime.now(ZoneId.of("+8")).toInstant(ZoneOffset.of("+8")).toEpochMilli() - startLocalDate.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(time /1000/60/60);
        //剩余未设置的虚假数
        long num = 500;
        //还需要执行的次数
        long timeNum = time / (10*60*1000);
        //首先判断是不是小于已设置的值或者已经到了结束时间或者是最后一次
        if (num <= 0 || timeNum <= 1) {
            System.out.println("结束");
        } else {
            double random = Math.random();
            System.out.println(random * num);
            num = (long) (random * num / timeNum * 2);
        }
        System.out.println(num);
    }
}
