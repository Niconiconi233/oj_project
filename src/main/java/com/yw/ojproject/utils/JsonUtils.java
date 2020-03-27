package com.yw.ojproject.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;

/**
* @program: ojproject
*
* @description: json工具类
*
* @author: YW
*
* @create: 2020-03-11 20:57
**/
@Slf4j
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
    public static <T> List<T> jsonStringToList(String json, Class obj){
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

    /**
     *  将文件流转为json对象，文件存放路径与配置文件路径规范一致
     * @param
     * @return
     * @throws
     */
    public static Object ResolveJsonFileToObject(String filename){
        String str= ResolveJsonFileToString(filename);
        JSONObject jo = JSONObject.parseObject(str);
        return jo;
    }


    /**
     *  通过文件名获取获取json格式字符串，
     * @param filename 文件存放路径与配置文件路径规范一致
     * @return ResolveJsonFileToString
     * @throws
     */
    public static String ResolveJsonFileToString(String filename){

        BufferedReader br = null;
        String result = null;
        try {

//            br = new BufferedReader(new InputStreamReader(getInputStream(path)));
            br = new BufferedReader(new InputStreamReader(getResFileStream(filename),"UTF-8"));
            StringBuffer message=new StringBuffer();
            String line = null;
            while((line = br.readLine()) != null) {
                message.append(line);
            }
            if (br != null) {
                br.close();
            }
            String defaultString=message.toString();
            result=defaultString.replace("\r\n", "").replaceAll(" +", "");
            log.info("result={}",result);

        } catch (IOException e) {
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream in = classloader.getResourceAsStream(filename);
                br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuffer message=new StringBuffer();
                String line = null;
                while((line = br.readLine()) != null) {
                    message.append(line);
                }
                if (br != null) {
                    br.close();
                }
                if (in != null){
                    in.close();
                }
                String defaultString=message.toString();
                result=defaultString.replace("\r\n", "").replaceAll(" +", "");
                log.debug("for jar result={}",result);
            }catch (Exception e1){
                e1.printStackTrace();
            }

        }
        return result;
    }



    private static File getResFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) { // 如果同级目录没有，则去config下面找
            log.debug("不在同级目录，进入config目录查找");
            file = new File("config/"+filename);
        }
        Resource resource = new FileSystemResource(file);
        if (!resource.exists()) { //config目录下还是找不到，那就直接用classpath下的
            log.debug("不在config目录，进入classpath目录查找");
            file = ResourceUtils.getFile("classpath:"+filename);
        }
        return file;
    }

    /**
     *  通过文件名获取classpath路径下的文件流
     * @param
     * @return
     * @throws
     */
    private static FileInputStream getResFileStream(String filename) throws FileNotFoundException {
        FileInputStream fin = null;
        File file = getResFile(filename);
        log.info("getResFile path={}",file);
        fin = new FileInputStream(file);
        return fin;
    }

    /**
    * @Description: 获取文件流
    * @Param: [filename]
    * @return: java.io.FileOutputStream
    * @Author: YW
    * @Date: 
    */
    private static FileOutputStream getResFileOutStream(String filename) throws FileNotFoundException {
        FileOutputStream fout = null;
        File file = getResFile(filename);
        log.info("getResFile path={}", file);
        fout = new FileOutputStream(file);
        return fout;
    }

    /**
    * @Description: 将对象存入json文件
    * @Param: [object, filename]
    * @return: java.lang.String
    * @Author: YW
    * @Date: 
    */
    public static String ResolveObjectToJsonFile(Object object, String filename) throws IOException {
        String res = objectToJson(object);
        FileOutputStream fout = getResFileOutStream(filename);
        fout.write(res.getBytes());
        return filename;
    }
}
