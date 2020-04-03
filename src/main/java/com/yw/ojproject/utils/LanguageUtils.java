package com.yw.ojproject.utils;/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-04-03 20:45
**/
public class LanguageUtils {
    public static String getLanguageType(String language)
    {
        switch (language)
        {
            case "C":
                return "c_lang";
            case "C++":
                return "cpp_lang";
            case "Java":
                return "java_lang";
            case "Python2":
                return "py2_lang";
            case "Python3":
                return "py3_lang";
            default:
                return null;
        }
    }

}
