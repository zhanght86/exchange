package com.exchange.web.system.security.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exchange.web.util.ResponseCode;
import com.exchange.web.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author ouyangan
 * @Date 2016/11/1/19:32
 * @Description 登录过滤器
 */
public class ShiroAuthenticationFilter extends PassThruAuthenticationFilter
{
    private static Logger log = LoggerFactory.getLogger(ShiroAuthenticationFilter.class);
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
        throws Exception
    {
        log.debug("登录过滤器");
        if (isLoginRequest(request, response))
        {
            return true;
        }
        else
        {
            saveRequest(request);
            if (((HttpServletRequest)request).getHeader("Accept").contains("application/json"))
            {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=UTF-8");
                Result result =
                    new Result(ResponseCode.unauthenticated.getCode(), ResponseCode.unauthenticated.getMsg());
                response.getWriter().append(new ObjectMapper().writeValueAsString(result));
                response.getWriter().flush();
                response.getWriter().close();
            }
            /*else
            {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=UTF-8");
                ((HttpServletResponse)response).sendRedirect("/shiro");
                log.debug(":::"+((HttpServletRequest)request).getServletPath());
            }*/
            return false;
        }
    }
}
