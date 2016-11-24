package cn.liuxh.service;

import cn.liuxh.mapper.OrderMapper;
import cn.liuxh.model.Order;
import cn.liuxh.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuxianghong on 16/11/24.
 */
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public Order getOrder(){
        Order order = orderMapper.getOrder();
        return order;
    }
}
