package com.lcm.test.redistest.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-22 11:03
 **/

public class JsonUtil {

    public static Map Json2Map(String str) {
        return JSON.parseObject(str, Map.class);
    }

    public static <T> T Json2Object(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz);
    }

    public static String Object2Json(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

}
