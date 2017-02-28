package cn.liuxh.controller;

import cn.liuxh.model.Group;
import cn.liuxh.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuxianghong on 2017/2/24.
 */
@Controller
@SessionAttributes("group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map adminLogin(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        map.put("result", "1");

        try {
            String name =  request.getParameter("userName");
            String pw =  request.getParameter("password");

            System.out.println("adminLogin:"+" password: "+pw + " name: "+ name);

            Group groupResult = groupService.getGroupInfo(name);

            if (groupResult != null) {
                if (groupResult.getPw().equals(pw)) {

                    map.put("data",groupResult);
                    map.put("result", "0");
                    map.put("msg", "登录成功");
                    modelMap.addAttribute("group",groupResult);
                } else {
                    map.put("msg", "用户名或密码不正确");
                }
            } else {
                map.put("msg", "用户名和密码不正确");
            }
            return map;

        } catch (Exception e) {
            // TODO: handle exception
            map.put("msg", e.toString());
        }
        return map;
    }
}
