package cn.liuxh.mapper;

import cn.liuxh.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 16/11/24.
 */
public interface OrderMapper {
    Order getOrder();
    List getAllOrders(@Param(value="start")int start, @Param(value="rows")int rows);
    int selectCount();
    int updateOrder(Order order);
    int addOrder(Order order);
    int delOrder(@Param(value="id")int id);
}
