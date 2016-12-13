package cn.liuxh.mapper;

import cn.liuxh.model.PickOrder;
import cn.liuxh.model.SortOrders;
import cn.liuxh.model.SortSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 2016/11/30.
 */
public interface SortOrdersMapper {
    List getAll(@Param(value="start")int start, @Param(value="rows")int rows);

    List getAllSkuPage(@Param(value="start")int start, @Param(value="rows")int rows);

    List getAllSku(@Param(value="orderName")String orderName);

    List getAllSkuCountAndSort(@Param(value="orderName")String orderName,@Param(value="start")int start, @Param(value="rows")int rows);

    int count();
    int selectSkUCount();

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
    int truncate();

    int adPickOrder(PickOrder order);
    int addPickSkus(List list);

    int updatePickOrderState(@Param(value="id")int id, @Param(value="state")int state);
    int updatePickOrderPickState(@Param(value="id")int id, @Param(value="state")int state);

    PickOrder getPickOrderDetailById(@Param(value="id")int id);

    int lockPickOrderById(@Param(value="id")int id,@Param(value="uid")int uid);

    List getAllPickOrders(@Param(value="start")int start, @Param(value="rows")int rows
            ,@Param(value="state")int state, @Param(value="location")String location);
    int getPickOrdersCount(@Param(value="start")int start, @Param(value="rows")int rows
            ,@Param(value="state")int state, @Param(value="location")String location);

    List getAllPickSkus(@Param(value="id")int orderid);
    List getAllPickLocations();

    int deletePickSku(@Param(value="id")int id);

    PickOrder getUnPickOrder(PickOrder order);

    List getAllPickSku(@Param(value="start")int start, @Param(value="rows")int rows);

    int selectPickSkuCount();

    int selectPickOrderCount();

    int updatePickSkus(List skus);

    int setSetting(@Param(value="key")String key,@Param(value="value") String value);

    String getSetting(@Param(value="key")String key);
}
