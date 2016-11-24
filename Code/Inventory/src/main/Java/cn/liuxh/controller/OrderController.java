package cn.liuxh.controller;

import cn.liuxh.model.Order;
import cn.liuxh.model.Product;
import cn.liuxh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
