package com.yw.ojproject.config;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @program: shiro
 *
 * @description:
 *
 * @author: YW
 *
 * @create: 2020-04-22 00:06
 **/
public class SessionConfig extends DefaultWebSessionManager {

    private static final String AUTH_TOKEN = "authToken";
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public SessionConfig() {
        super();
        //设置有效时间24小时
        setGlobalSessionTimeout(MILLIS_PER_HOUR * 24);
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response)
    {
        String id  = WebUtils.toHttp(request).getHeader(AUTH_TOKEN);
        if(!StringUtils.isEmpty(id) && !"null".equals(id))
        {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }else
        {
            return super.getSessionId(request, response);
        }
    }
}
