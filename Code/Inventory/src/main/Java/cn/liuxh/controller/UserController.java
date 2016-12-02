package cn.liuxh.controller;

import cn.liuxh.model.User;
import cn.liuxh.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuxianghong on 16/11/23.
 */

@Controller
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    public static UserController userController;

    UserController(){
        userController = this;
    }

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo() {
        User user = userService.getUserInfo("admin");
        if(user!=null){
            System.out.println("user.getName():"+user.getName());
        }
        return user;
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public Object login(@RequestBody User user){
        try {
            Map map = new HashMap();
            map.put("result", "1");
            map.put("msg", "用户名或密码不正确");

            String userName = user.getName();
            String password = user.getPw();

            System.out.println("userlogin: "+userName + " " + password);
            User userResult = userService.getUserInfo(userName);

            if (userResult != null) {
                if (userResult.getPw().equals(password)) {
                    map.put("data",userResult);
                    map.put("result", "0");
                    map.put("msg", "登录成功");
                }
            }
            return map;
        }catch (Exception e){
            Map map = new HashMap();
            map.put("result", "2");
            map.put("msg", e.toString());
            return map;
        }
    }

    public boolean haveUser(int id) {
        User user = userService.getUserInfoById(id);
        return user != null;
    }
}
