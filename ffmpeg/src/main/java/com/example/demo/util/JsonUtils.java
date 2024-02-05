package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * 将任意对象转变为json字符串
     */
    public static <T> String toJson(T object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("jsonUtils error: {}, message={}", object, e.getLocalizedMessage());
        }
        return json;
    }

    /**
     * 将任意对象转变为map
     *
     * @return
     */
    public static <T> Map toMap(T object) {
        Map map = null;
        try {
            String json = toJson(object);
            map = objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            log.error("jsonUtils error: {}, message={}", object, e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 将json字符串转变为任意对象（单一对象）
     */
    public static <T> T toObject(String json, Class<T> tClass) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        if (Objects.equals(tClass.getName(), "java.lang.String") && !json.startsWith("\"")) {
            return (T) json;
        }
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error("jsonUtils error: {}, message={}", json, e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 将json字符串转变为任意对象（单一对象）
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        T object = (T) json;
        try {
            object = objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("jsonUtils error: {}, message={}", json, e.getLocalizedMessage());
        }
        return object;
    }

    /**
     * 将map转变为任意对象（单一对象）
     */
    public static <T> T toObject(Map<String, Object> map, Class<T> tClass) {
        T object = null;
        try {
            String json = toJson(map);
            object = objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error("jsonUtils error: {}, message={}", toJson(map), e.getLocalizedMessage());
        }
        return object;
    }

    public static <T> T toObject(Object object, Class<T> tClass) {
        String json;
        if (object instanceof String) {
            json = (String) object;
        } else {
            json = toJson(object);
        }
        return toObject(json, tClass);
    }

    /**
     * 将json字符串转变为对象集合（json字符串为对象数组字符串）
     */
    public static <T> List<T> toList(String json, Class<T> elementClass) {
        List<T> list = null;
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass);
            list = objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("jsonUtils error: {}, message={}", json, e.getLocalizedMessage());
        }
        return list;
    }

    public static <T> List<T> toList(Object object, Class<T> elementClass) {
        String json;
        if (object instanceof String) {
            json = (String) object;
        } else {
            json = toJson(object);
        }
        return toList(json, elementClass);
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("jsonUtils error: {}, message={}", json, e.getLocalizedMessage());
        }
        return null;
    }

}
