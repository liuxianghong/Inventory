package cn.liuxh.service;

import cn.liuxh.mapper.OrderMapper;
import cn.liuxh.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List getAllOrders(int start,int rows){
        return orderMapper.getAllOrders(start, rows);
    }

    public int selectCount(){
        return orderMapper.selectCount();
    }
}
