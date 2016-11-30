package cn.liuxh.mapper;

import cn.liuxh.model.SortOrders;
import cn.liuxh.model.SortSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 2016/11/30.
 */
public interface SortOrdersMapper {
    List getAll();

    List getAllSku(@Param(value="orderName")String orderName);

    int count();

    int update(SortOrders product);
    int add(SortOrders product);
    int delete(@Param(value="id")int id);

    SortOrders getDetailById(@Param(value="id")int id);
    SortOrders getDetailByLocationNo(@Param(value="id")String id);

    int deleteSku(@Param(value="orderName")String orderName);
    int addSku(List list);
    int updateSku(List list);
    List<SortSku> getSkus(@Param(value="orderId")int id);

    int importOrders(SortOrders list);
    int importSkus(List list);
}
