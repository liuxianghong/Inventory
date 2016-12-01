package cn.liuxh.service;

import cn.liuxh.mapper.LocationCheckOrderMapper;
import cn.liuxh.mapper.SkuCheckOrderMapper;
import cn.liuxh.model.LocationCheckOrder;
import cn.liuxh.model.LocationSku;
import cn.liuxh.model.SkuCheckOrder;
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

    public List getAll(int start,int rows){
        return locationCheckOrderMapper.getAll(start, rows);
    }
    public List<LocationSku> getAllSku(@Param(value="id")int id) {
        return locationCheckOrderMapper.getAllSku(id);
    }

    public int count(){
        return locationCheckOrderMapper.count();
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

    public LocationCheckOrder getDetailByLocationNo(String id){
        return locationCheckOrderMapper.getDetailByLocationNo(id);
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

}
