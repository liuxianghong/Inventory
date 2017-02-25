package cn.liuxh.service;

import cn.liuxh.mapper.SkuCheckOrderMapper;
import cn.liuxh.model.SkuCheckOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxianghong on 2016/11/28.
 */
@Service
public class SkuCheckOrderService {
    @Autowired
    private SkuCheckOrderMapper skuCheckOrderMapper;

    public List getAll(int start, int rows, int groupId){
        return skuCheckOrderMapper.getAll(start, rows, groupId);
    }

    public int count(int groupId){
        return skuCheckOrderMapper.count(groupId);
    }

    public int update(SkuCheckOrder product){
        return skuCheckOrderMapper.update(product);
    }
    public int add(SkuCheckOrder product){
        return skuCheckOrderMapper.add(product);
    }
    public int delete(@Param(value="id")int id){
        return skuCheckOrderMapper.delete(id);
    }
    public SkuCheckOrder getDetailById(@Param(value="id")int id){
        return skuCheckOrderMapper.getDetailById(id);
    }

    public boolean haveGoods(String seriesNo,int groupId) { return skuCheckOrderMapper.haveGoods(seriesNo,groupId) > 0;}

    public int truncate(int groupId) {return skuCheckOrderMapper.truncate(groupId);}

    public int importSkus(List skus) {return skuCheckOrderMapper.importSkus(skus);}
}
