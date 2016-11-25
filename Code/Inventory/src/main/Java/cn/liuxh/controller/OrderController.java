package cn.liuxh.controller;

import cn.liuxh.model.Order;
import cn.liuxh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
