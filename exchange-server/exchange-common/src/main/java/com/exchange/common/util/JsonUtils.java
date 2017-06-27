package com.exchange.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils
{
    
    public static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    
    public static <T> T JsonToObject(String json, Class<T> clazz)
    {
        if (!StringUtils.isEmpty(json) && clazz != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            try
            {
                return mapper.readValue(json, clazz);
            }
            catch (Exception e)
            {
                logger.error("", e);
            }
        }
        return null;
    }
    
    public static String ObjectToJsonString(Object o)
    {
        if (o != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            try
            {
                return mapper.writeValueAsString(o);
            }
            catch (Exception e)
            {
                logger.error("对象转json失败！", e);
            }
        }
        return null;
    }
    
    public static <T> T deserialize(byte[] data, Class<T> clazz)
    {
        String json = null;
        try
        {
            json = new String(data, "utf-8").toString();
            return JsonUtils.JsonToObject(json, clazz);
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("Json转码失败！" + json);
        }
        return null;
    }
    
    public static String serialize(Object target)
    {
        return JsonUtils.ObjectToJsonString(target);
    }
    
    public static <T> T deserialize(String data, Class<T> clazz)
    {
        String json = null;
        try
        {
            return JsonUtils.JsonToObject(data, clazz);
        }
        catch (Exception e)
        {
            logger.error("" + json);
        }
        return null;
    }
}
