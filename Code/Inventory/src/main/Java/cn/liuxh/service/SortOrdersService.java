package cn.liuxh.service;

import cn.liuxh.mapper.SortOrdersMapper;
import cn.liuxh.model.SortOrders;
import cn.liuxh.model.SortSku;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxianghong on 2016/11/30.
 */
@Service
public class SortOrdersService {
    @Autowired
    private SortOrdersMapper sortOrdersMapper;

    public List getAll(int start,int rows){
        return sortOrdersMapper.getAll(start, rows);
    }
    public List getAllSku(@Param(value="orderName")String orderName) {
        return sortOrdersMapper.getAllSku(orderName);
    }

    public int count(){
        return sortOrdersMapper.count();
    }

    public int update(SortOrders product){
        return sortOrdersMapper.update(product);
    }
    public int add(SortOrders product){
        return sortOrdersMapper.add(product);
    }
    public int delete(@Param(value="id")int id){
        return sortOrdersMapper.delete(id);
    }
    public SortOrders getDetailById(@Param(value="id")int id){
        return sortOrdersMapper.getDetailById(id);
    }

    public SortOrders getDetailByLocationNo(String id){
        return sortOrdersMapper.getDetailByLocationNo(id);
    }

    public int deleteSku(@Param(value="orderName")String orderName){
        return sortOrdersMapper.deleteSku(orderName);
    }
    public int addSku(List list){
        return sortOrdersMapper.addSku(list);
    }
    public int updateSku(List list){
        return sortOrdersMapper.updateSku(list);
    }
    List<SortSku> getSkus(@Param(value="orderId")int id) {
        return sortOrdersMapper.getSkus(id);
    }

    public int importOrders(SortOrders list){return sortOrdersMapper.importOrders(list);}
    public int importSkus(List list){return sortOrdersMapper.importSkus(list);}
}
