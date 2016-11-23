package cn.liuxh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by liuxianghong on 2016/11/23.
 */

@Controller
public class MainController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index.jsp";
    }

}
