package com.example.demo.util.qqwry;

import java.io.IOException;

/**
 * @author libiao
 * @date 2021/12/30
 */
public class TestMain {
    public static void main(String[] args) throws IOException {
        String IP = "47.244.30.144";
        String dataFile = "D:\\qqwry\\qqwry.dat";
        IPSeeker.I.init(dataFile);
        String country = IPSeeker.I.getAddress(IP);
        System.out.println(country);
    }

}
