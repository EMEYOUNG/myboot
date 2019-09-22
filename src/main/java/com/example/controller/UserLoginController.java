package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = {"/user"})
public class UserLoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);
    @Autowired
    private UserService userService;

    /**
     * 跳转到用户登录页面
     * @return 登录页面
     */
    @RequestMapping(value = {"/loginHtml"})
    public String loginHtml(){
        return "userLogin";
    }
    /**
     * 获取用户名与密码，用户登录
     * @return 登录成功页面
     */
    @RequestMapping(value = {"/userLogin"})
    public ModelAndView userLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, ModelAndView mv){

        mv.setViewName("/result");
        User user = userService.userLogin(username,password);
        if(user != null){
            mv.addObject("title","查询成功！");
            logger.info(username+",查询成功！");
        }else{
            mv.addObject("title","查询失败！");
            logger.info(username+",查询失败！");
        }
        return mv;
    }

}
