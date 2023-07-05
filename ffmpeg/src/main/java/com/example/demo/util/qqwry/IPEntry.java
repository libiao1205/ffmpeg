package com.example.demo.util.qqwry;

/**
 * @author libiao
 * @date 2021/12/30
 */
public class IPEntry {
    public String beginIp;
    public String endIp;
    public String country;
    public String area;

    /**
     * 构造函数
     */

    public IPEntry() {
        beginIp = endIp = country = area = "";
    }

    @Override
    public String toString() {
        return this.area + "  " + this.country + "IP范围:" + this.beginIp + "-"
                + this.endIp;
    }

}
