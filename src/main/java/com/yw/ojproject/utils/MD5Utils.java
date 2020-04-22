package com.yw.ojproject.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @program: shiro
 *
 * @description:
 *
 * @author: YW
 *
 * @create: 2020-04-21 21:48
 **/
public class MD5Utils {
    public static String MD5Pwd(String username, String pwd) {
        // 加密算法MD5
        // salt盐 username + salt
        // 迭代次数
        String md5Pwd = new SimpleHash("MD5", pwd,
                ByteSource.Util.bytes(username), 2).toHex();
        return md5Pwd;
    }
}
