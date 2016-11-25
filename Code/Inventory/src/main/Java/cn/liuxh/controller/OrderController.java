package cn.liuxh.controller;

import cn.liuxh.model.Order;
import cn.liuxh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxianghong on 16/11/24.
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/getOrder")
    @ResponseBody
    public Order getOrder() {
        Order order = orderService.getOrder();
        return order;
    }

    @RequestMapping("/getAllOrders")
    @ResponseBody
    public Map getAllOrders(int page,int rows) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        List students = orderService.getAllOrders(start,rows);
        map.put("rows",students);
        map.put("total", orderService.selectCount());
        System.out.println("getAllOrders:"+students.toString()+" page: "+page + " rows: "+ rows);
        return map;
    }

    /**
     * 保存用户
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    @ResponseBody
    public int saveOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String userId = request.getParameter("id");
            String name =  request.getParameter("name");
            String format = request.getParameter("format");
            String code = request.getParameter("code");
            String number = request.getParameter("number");
            String site = request.getParameter("site");

            System.out.println("saveOrder:"+" userId: "+userId + " name: "+ name);

            Order user = new Order();
            user.setName(name);
            user.setFormat(format);
            user.setCode(code);
            user.setNumber(Integer.parseInt(number));
            user.setSite(site);
            boolean ret = false;
            if (ret) {
                //修改用户信息
                user.setId(Integer.parseInt(userId));
                //ret = userService.updateUser(user);
            } else {
                //ret = userService.addUser(user);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 1;
    }
}
