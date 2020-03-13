package com.yw.ojproject.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-11 20:57
**/
public class JsonUtils {
    /**
     * JSON字符串转换成对象
     */
    public static <T> T jsonStringToObject(String jsonStr,Class<T> obj){
        try{
            return JSONObject.parseObject(jsonStr, obj);
        }catch(Exception e){
            System.out.println("将JSON串{}转换成 指定对象失败:"+jsonStr);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转JSON
     */
    public static <T> String objectToJson(T obj){
        try{
            String json=JSONObject.toJSONString(obj);
            return json;
        }catch(Exception e){
            System.out.println("将指定对象转成JSON串{}失败:"+obj.toString());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * List<T>对象转成json
     */
    public static<T> String listToJsonString(List<T> objList){
        try{
            String json=JSONObject.toJSONString(objList);
            return json;
        }catch(Exception e){
            System.out.println("将对象列表转成JSON串{}失败:"+objList.toString());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json转换成对象列表List<T>
     */
    public static <T> List<T> jsonStringToList(String json,Class<T> obj){
        try{
            return JSONArray.parseArray(json, obj);
        }catch(Exception e){
            System.out.println("将JSON串{}转成对象列表失败:"+json);
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 将JSON串转换为JSONOBJECT
     */
    public static JSONObject stringTOJSONObject(String json){
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(json);
        } catch (Exception e) {
            System.out.println("JSON串{} 转换成 JsonObject失败"+json);
        }
        return jsonObject;
    }
}
