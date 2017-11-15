package com.coffeers.app.framework.controller.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by jack on 2017/6/27.
 * json工具类，用于处理json与pojo之间的转化，基于Jackson实现
 */
public final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将POJO转化为json
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String toJson(Object object) {
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            LOGGER.error("convert POJO to JSON failure ", e);
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * 将json转为pojo
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(json,type);
        }catch (Exception e){
            LOGGER.error("convert JSON to POJO failure",e);
            throw new RuntimeException(e);
        }
        return pojo;
    }


    public static String getProgramaList2(Object object){

        //ObjectMapper和StringWriter都是jackson中的，通过这两个可以实现对list的序列化
        ObjectMapper mapper = new ObjectMapper();
        StringWriter w = new StringWriter();
        //Convert between List and JSON
        try {
            mapper.writeValue(w, object);//开始序列化
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.err.println("finally");
        }
        System.out.println(w.toString()); //输出json格式的字符串
        return w.toString(); //将json格式的字符串返回给前台
    }

}