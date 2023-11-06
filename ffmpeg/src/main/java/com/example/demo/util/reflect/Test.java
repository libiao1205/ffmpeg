package com.example.demo.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author libiao
 * @date 2023/9/22
 */
public class Test {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Test test = new Test();
        Student student = new Student();
        for (Method method : Student.class.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().equals(Custom.class)) {
                    for (Class<?> parameterType : method.getParameterTypes()) {
                        method.invoke(student, test.getValue(parameterType));
                    }
                }
            }
        }
        System.out.println(student.getName() + ", " + student.getAge());
    }

    private Object getValue(Class<?> parameterType) {
        if (parameterType.getName().endsWith("Integer")) {
            return 18;
        }
        if (parameterType.getName().endsWith("String")) {
            return "lb";
        }
        return null;
    }
}
