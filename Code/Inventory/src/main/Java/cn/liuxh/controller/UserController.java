package cn.liuxh.controller;

import cn.liuxh.model.Group;
import cn.liuxh.model.User;
import cn.liuxh.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
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


    @RequestMapping("/getAllUsersE")
    @ResponseBody
    public Map getAllUsersE(HttpServletRequest request,@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="rows", defaultValue="1000") int rows) throws Exception{

        Map map = new HashMap();
        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {
            int start = (page-1)*rows;
            List students = userService.getAllUsers(start,rows,group.getId());

            map.put("rows",students);
            map.put("total", userService.selectCount());
        }
        return map;
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    @ResponseBody
    public int saveUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String userId = request.getParameter("id");
            String name =  request.getParameter("userName");
            String pw =  request.getParameter("password");
            String nickName =  request.getParameter("nickName");

            System.out.println("saveUser:"+" userId: "+userId + " name: "+ name);

            User user = new User();
            user.setName(name);
            user.setPw(pw);
            user.setNickName(nickName);
            int ret = 0;
            if (!userId.isEmpty()) {
                //修改用户信息
                user.setId(Integer.parseInt(userId));
                ret = userService.updateUser(user);
                //ret = userService.updateUser(user);
            } else {
                //ret = userService.addUser(user);
                ret = userService.addUser(user);
            }
            System.out.println("saveUser:"+" ret: "+ret);
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    @ResponseBody
    public int delUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String id = request.getParameter("id");
            System.out.println("saveUser:"+" ParameterMap: "+request.getParameterMap().toString());

            int ret = 0;
            if (!id.isEmpty()) {
                ret = userService.delUser(Integer.parseInt(id));
            }
            System.out.println("saveUser:"+" ret: "+ret);
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
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

    public User getUser(int id) {
        return userService.getUserInfoById(id);
    }

}
