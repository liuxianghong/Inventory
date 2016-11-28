package cn.liuxh.mapper;

import cn.liuxh.model.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 2016/11/28.
 */
public interface GoodsMapper {
    List getAllE(@Param(value="start")int start, @Param(value="rows")int rows);
    List getAll();

    int count();

    int update(Goods product);
    int add(Goods product);
    int delete(@Param(value="id")int id);

    Goods getGoodsBySeriesNo(String seriesNo);
}