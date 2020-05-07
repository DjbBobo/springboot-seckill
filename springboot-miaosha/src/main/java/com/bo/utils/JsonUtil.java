package com.bo.utils;

import com.bo.exception.CMSException;
import com.bo.result.ResultCodeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Jackson工具类
 */
public class JsonUtil {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper mapper;

    /**
     * 对象转String
     * @param obj
     * @return
     */
    public String toString(Object obj){

        if (obj == null){
            return null;
        }
        if (obj.getClass() == String.class){
            return (String)obj;
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错:"+obj,e);
            return null;
        }
    }

    /**
     * String转指定clazz对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toBean(String json,Class<T> clazz){
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * String转List集合
     * @param json
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> List<E> toList(String json, Class<E> clazz) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }

    }

}
