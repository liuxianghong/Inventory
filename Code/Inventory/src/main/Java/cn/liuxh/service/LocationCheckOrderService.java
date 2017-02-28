package cn.liuxh.service;

import cn.liuxh.mapper.LocationCheckOrderMapper;
import cn.liuxh.model.Goods;
import cn.liuxh.model.LocationCheckOrder;
import cn.liuxh.model.LocationSku;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxianghong on 16/11/28.
 */

@Service
public class LocationCheckOrderService {
    @Autowired
    private LocationCheckOrderMapper locationCheckOrderMapper;

    public List getAll(int start,int rows, int groupId){
        return locationCheckOrderMapper.getAll(start, rows,groupId);
    }
    public List<LocationSku> getAllSku(@Param(value="id")int id) {
        return locationCheckOrderMapper.getAllSku(id);
    }

    public int count(int groupId){
        return locationCheckOrderMapper.count(groupId);
    }

    public List getAllLocationOrdersE(int start, int rows, int groupId){
        return locationCheckOrderMapper.getAllLocationOrdersE(start, rows,groupId);
    }
    public int SkuCount(int groupId){
        return locationCheckOrderMapper.SkuCount(groupId);
    }

    public int update(LocationCheckOrder product){
        return locationCheckOrderMapper.update(product);
    }
    public int add(LocationCheckOrder product){
        return locationCheckOrderMapper.add(product);
    }
    public int delete(@Param(value="id")int id){
        return locationCheckOrderMapper.delete(id);
    }
    public LocationCheckOrder getDetailById(@Param(value="id")int id){
        return locationCheckOrderMapper.getDetailById(id);
    }

    public List<Goods> getDetailByLocationNo(String id,int groupId){
        return locationCheckOrderMapper.getDetailByLocationNo(id,groupId);
    }

    public int deleteSku(@Param(value="id")int id){
        return locationCheckOrderMapper.deleteSku(id);
    }
    public int addSku(List list){
        return locationCheckOrderMapper.addSku(list);
    }
    List<LocationSku> getSkus(@Param(value="orderId")int id) {
        return locationCheckOrderMapper.getSkus(id);
    }

    public int truncate(int groupId) {return locationCheckOrderMapper.truncate(groupId);}

    public Goods selectedGood(String seriesNo,int groupId) {return locationCheckOrderMapper.selectedGood(seriesNo,groupId);}
}
