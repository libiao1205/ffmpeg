package com.example.demo.util.google;

/**
 * @author libiao
 * @date 2022/2/14
 */
public class NameValue {
    private StringBuilder sb = new StringBuilder();

    public void add(String name, Object value) {
        sb.append("&");
        sb.append(name);
        sb.append("=");
        sb.append(value);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
