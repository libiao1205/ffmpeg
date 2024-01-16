package com.example.demo.util.reflect;

import org.springframework.core.annotation.Order;

/**
 * @author libiao
 * @date 2023/9/22
 */

public class Student {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    @Custom
    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    @Custom
    public void setAge(Integer age) {
        this.age = age;
    }
}
