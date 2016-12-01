package cn.liuxh.controller;

import cn.liuxh.model.User;
import cn.liuxh.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Created by liuxianghong on 16/11/23.
 */

@Controller
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo() {
        User user = userService.getUserInfo();
        if(user!=null){
            System.out.println("user.getName():"+user.getName());
        }
        return user;
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User login(User user){
        int ret = userService.login(user);
        return user;
    }
}
