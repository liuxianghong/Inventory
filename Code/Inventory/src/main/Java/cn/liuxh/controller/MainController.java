package cn.liuxh.controller;

import cn.liuxh.model.Group;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuxianghong on 2016/11/23.
 */

@Controller
public class MainController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {
            System.out.println("index:"+" pw: "+group.getPw() + " name" + group.getName());
            return "index";
        }
        return "login";
    }

}
