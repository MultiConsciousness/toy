package com.walker.test.controller;

import com.walker.test.common.SysConstant;
import com.walker.test.dao.vo.LoginVo;
import com.walker.test.service.user.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@EnableCaching
public class AuthController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public String login(LoginVo user){
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(),user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try{
            token.setRememberMe(user.isRememberMe());
            subject.login(token);
            return "forward:/view/home";
        }catch (AccountException e){
            System.out.println(SysConstant.AuthenAndAuthor.ACCOUNT_NOT_EXIST+e.getMessage());
            return "forward:/view/index";
        }catch (AuthenticationException e){
            System.out.println(SysConstant.AuthenAndAuthor.ERROR_ACCOUNT_OR_PWD+e.getMessage());
            return "forward:/view/index";
        }
    }
}
