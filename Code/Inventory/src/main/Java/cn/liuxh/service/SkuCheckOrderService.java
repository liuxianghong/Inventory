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

    public List getAll(){
        return skuCheckOrderMapper.getAll();
    }

    public int count(){
        return skuCheckOrderMapper.count();
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
}
