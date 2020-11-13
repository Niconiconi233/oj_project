package com.yw.ojproject.realm;

import com.yw.ojproject.entity.User;
import com.yw.ojproject.service.UserServer;
import com.yw.ojproject.utils.MD5Utils;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountException;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: shiro
 *
 * @description:
 *
 * @author: YW
 *
 * @create: 2020-04-21 20:47
 **/
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    UserServer userServer;

    /**
    * @Description: 鉴权
    * @Param: [principalCollection]
    * @return: org.apache.shiro.authz.AuthorizationInfo
    * @Author: YW
    * @Date:
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userServer.findUserByUsername(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        Set<String> permission = new HashSet<>();
        if(user.isAdmin())
        {
            roles.add("admin");
            permission.add("user:norm");
            permission.add("user:admin");
        }else if(user.isSuperAdmin())
        {
            roles.add("superadmin");
            permission.add("user:norm");
            permission.add("user:admin");
            permission.add("user:superadmin");
        }else
        {
            roles.add("norm");
            permission.add("user:norm");
        }
        info.setRoles(roles);
        info.setStringPermissions(permission);
        return info;
    }

    /**
    * @Description: 登录
    * @Param: [authenticationToken]
    * @return: org.apache.shiro.authc.AuthenticationInfo
    * @Author: YW
    * @Date:
    */
    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
    {
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());

        if(userName == null)
        {
            throw new AccountException("用户名不正确");
        }else if(userPwd == null)
        {
            throw new AccountException("密码不正确");
        }
        //从数据库获取对应用户名的用户
        User user = userServer.findUserByUsername(userName);

        if(user == null)
        {
            throw new UnknownAccountException("用户不存在");
        }
        if(user.isDisabled())
        {
            throw new LockedAccountException("账户已被锁定");
        }
        if(user.getPassword().compareTo(MD5Utils.MD5Pwd(userName, userPwd)) != 0)
        {
            throw new AuthenticationException("用户名或密码不正确");
        }
        ByteSource salt = ByteSource.Util.bytes(userName);
        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), salt, getName());
    }
}
