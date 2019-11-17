package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.RedisUtil;
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
    @Autowired
    private RedisUtil redisUtil;

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
        //双重校验防止缓存击穿，同时加同步锁进行阻塞，保护mysql数据库
        Object o = redisUtil.get(username);
        if(o==null){
            synchronized (this){
                o = redisUtil.get(username);
                if(o == null){
                    User user = userService.userLogin(username,password);
                    if(user!=null){
                        mv.addObject("user",user);
                        boolean b = redisUtil.put(username,user,20000);
                        if(b == true){
                            mv.addObject("result","查询数据库成功并放入redis缓存！");
                            logger.info(username+",查询数据库成功并放入redis缓存！");
                            return mv;
                        }else{
                            mv.addObject("result","查询数据库成功，放入redis缓存失败！");
                            logger.info(username+",查询数据库成功，放入redis缓存失败！");
                            return mv;
                        }
                    }else{
                        mv.addObject("result","查询数据库失败！");
                        logger.info(username+",查询缓存和数据库失败！");
                        return mv;

                    }

                }
            }
        }
        User u = (User)o;
        mv.addObject("user",u);
        mv.addObject("result","查询缓存成功！");
        logger.info(username+",查询缓存成功，直接返回！");
        return mv;
    }



}
