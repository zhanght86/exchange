package com.exchange.web.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandlerInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception
    {
        // User user = (User) request.getSession().getAttribute(Const.SESSION_USER);
        //// String path = request.getServletPath();
        // LOGGER.info("servletPath=" + request.getServletPath());
        // if (user != null) {
        // return true;
        // }
        //// return true;
        //// }
        // response.sendRedirect(request.getContextPath() + "/");
        // return false;
        return true;
    }
    
}
