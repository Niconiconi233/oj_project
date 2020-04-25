package com.yw.ojproject.utils;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
* @program: ojproject
*
* @description: 随机工具类
*
* @author: YW
*
* @create: 2020-03-21 15:06
**/
public class RandUtils {
    public static String generateRandomFilename(){
        String RandomFilename = "";
        Random rand = new Random();//生成随机数
        int random = rand.nextInt();

        Calendar calCurrent = Calendar.getInstance();
        int intDay = calCurrent.get(Calendar.DATE);
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        String now = String.valueOf(intYear) + "_" + String.valueOf(intMonth) + "_" +
                String.valueOf(intDay) + "_";

        RandomFilename = now + String.valueOf(random > 0 ? random : ( -1) * random);

        return RandomFilename;
    }

    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    public static List<CVSUtils.CSVEntity> makeRandomUser(String pre, String suff, Integer from, Integer to, Integer pwdl)
    {
        List<CVSUtils.CSVEntity> result = new LinkedList<>();
        int idx = 0;
        for(int i = from; i <= to; ++i)
        {
            result.add(idx, new CVSUtils.CSVEntity(pre + i + suff, getRandomString(pwdl)));
            ++idx;
        }
        return result;
    }
}
