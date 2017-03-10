package cn.liuxh.service;

import cn.liuxh.mapper.SortOrdersMapper;
import cn.liuxh.model.PickOrder;
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

    public List getAll(int start,int rows,int groupId){
        return sortOrdersMapper.getAll(start, rows, groupId);
    }
    public List getAllSku(@Param(value="orderName")String orderName,int groupId) {
        return sortOrdersMapper.getAllSku(orderName,groupId);
    }

    public List getAllSkuCountAndSort(String orderName,int start,int rows,int groupId) {
        return sortOrdersMapper.getAllSkuCountAndSort(orderName,start,rows,groupId);
    }

    public List getAllSkuPage(int start, int rows, int groupId){
        return sortOrdersMapper.getAllSkuPage(start, rows, groupId);
    }

    public int selectSkUCount(int groupId) {
        return sortOrdersMapper.selectSkUCount(groupId);
    }

    public int count(int groupId){
        return sortOrdersMapper.count(groupId);
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

    public int deleteSku(@Param(value="orderName")String orderName,int grouId){
        return sortOrdersMapper.deleteSku(orderName,grouId);
    }
    public int addSku(List list){
        return sortOrdersMapper.addSku(list);
    }
    public int updateSku(List list){
        return sortOrdersMapper.updateSku(list);
    }
//    List<SortSku> getSkus(@Param(value="orderId")int id) {
//        return sortOrdersMapper.getSkus(id);
//    }

    public int importOrders(SortOrders list){return sortOrdersMapper.importOrders(list);}
    public int importSkus(List list){return sortOrdersMapper.importSkus(list);}

    public int truncate(int groupId) {return sortOrdersMapper.truncate(groupId);}

    public int truncateSort(int groupId) {return sortOrdersMapper.truncateSort(groupId);}

    public int adPickOrder(PickOrder order){return sortOrdersMapper.adPickOrder(order);}
    public int addPickSkus(List list){return sortOrdersMapper.addPickSkus(list);}

    public int updatePickOrderState(int id, int state){return sortOrdersMapper.updatePickOrderState(id,state);}
    public int updatePickOrderPickState(int id, int state){return sortOrdersMapper.updatePickOrderPickState(id,state);}

    public PickOrder getPickOrderDetailById(int id){
        return sortOrdersMapper.getPickOrderDetailById(id);
    }

    public int lockPickOrderById(int id,int uid){
        return sortOrdersMapper.lockPickOrderById(id,uid);
    }

    public List getAllPickOrders(int start,int rows,int state,String location){
        return sortOrdersMapper.getAllPickOrders(start, rows, state, location);
    }

    public int getPickOrdersCount(int start,int rows,int state,String location){
        return sortOrdersMapper.getPickOrdersCount(start, rows, state, location);
    }

    public List getAllPickSkus(@Param(value="orderName")int orderid) {
        return sortOrdersMapper.getAllPickSkus(orderid);
    }

    public List getAllPickLocations() {
        return sortOrdersMapper.getAllPickLocations();
    }

    public int deletePickSku(@Param(value="id")int id){
        return sortOrdersMapper.deletePickSku(id);
    }

    public PickOrder getUnPickOrder(PickOrder order){
        return sortOrdersMapper.getUnPickOrder(order);
    }

    public List getAllPickSku(int start,int rows,int groupId){
        return sortOrdersMapper.getAllPickSku(start, rows,groupId);
    }

    public int selectPickSkuCount(int groupId) {
        return sortOrdersMapper.selectPickSkuCount(groupId);
    }

    public int selectPickOrderCount(int groupId) {
        return sortOrdersMapper.selectPickOrderCount(groupId);
    }

    public int updatePickSkus(List skus) {
        return sortOrdersMapper.updatePickSkus(skus);
    }

    public int setSetting(String key, String value, int groupId){
        return sortOrdersMapper.setSetting(key,value,groupId);
    }

    public String getSetting(String key, int groupId) {
        return sortOrdersMapper.getSetting(key,groupId);
    }

    public int allOrderCount(){return sortOrdersMapper.allOrderCount();}

    public int truncateZero(){return sortOrdersMapper.truncateZero();}

    public List searchRemake(String remake, int groupId){
        return sortOrdersMapper.searchRemake(remake,groupId);
    }
}
