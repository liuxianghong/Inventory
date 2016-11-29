package cn.liuxh.service;

import cn.liuxh.mapper.GoodsMapper;
import cn.liuxh.model.Goods;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxianghong on 2016/11/28.
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public List getAllE(@Param(value = "start") int start, @Param(value = "rows") int rows){
        return  goodsMapper.getAllE(start, rows);
    }
    public List getAll(){
        return goodsMapper.getAll();
    }

    public int count(){
        return goodsMapper.count();
    }

    public int update(Goods product){
        return goodsMapper.update(product);
    }
    public int add(Goods product){
        return goodsMapper.add(product);
    }
    public int delete(@Param(value="id")int id){
        return goodsMapper.delete(id);
    }

    public Goods getGoodsBySeriesNo(String seriesNo){
        return goodsMapper.getGoodsBySeriesNo(seriesNo);
    }

    public int updateSkuLocation(Goods product) {
        return goodsMapper.updateSkuLocation(product);
    }

    public int importGoods(List godds)  {
        return goodsMapper.importGoods(godds);
    }
}
