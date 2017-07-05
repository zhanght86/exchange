package com.exchange.web.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exchange.web.controller.BaseController;
import com.exchange.web.info.SysUser;
import com.exchange.web.service.SysUserService;
import com.exchange.web.util.ResponseCode;
import com.exchange.web.util.Result;

/**
 * 系统功能模块
 */
@Controller
@RequestMapping("system")
public class LoginController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private SysUserService sysUserService;
    
    /**
     * 登录
     *
     * @param loginName 登录名
     * @param password 密码
     * @param platform 终端类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestParam String loginName, @RequestParam String password, @RequestParam int platform,
        HttpServletRequest request)
        throws Exception
    {
        // 极限验证二次服务验证
        if (!verifyCaptcha(request))
        {
            return Result.instance(ResponseCode.verify_captcha_error.getCode(),
                ResponseCode.verify_captcha_error.getMsg());
        }
        SysUser user = sysUserService.selectByLoginName(loginName);
        if (user == null)
        {
            return Result.instance(ResponseCode.unknown_account.getCode(), ResponseCode.unknown_account.getMsg());
        }
        if (user.getStatus() == 3)
        {
            return Result.instance(ResponseCode.forbidden_account.getCode(), ResponseCode.forbidden_account.getMsg());
        }
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(loginName, password));
        LoginInfo loginInfo = sysUserService.login(user, subject.getSession().getId(), platform);
        subject.getSession().setAttribute("loginInfo", loginInfo);
        log.debug("登录成功");
        return Result.success(loginInfo);
    }
}
