package cn.liuxh.mapper;

import cn.liuxh.model.SkuCheckOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 2016/11/28.
 */
public interface SkuCheckOrderMapper {

    List getAll();

    int count();

    int update(SkuCheckOrder product);
    int add(SkuCheckOrder product);
    int delete(@Param(value="id")int id);

    SkuCheckOrder getDetailById(@Param(value="id")int id);

    int haveGoods(@Param(value="seriesNo")String id);
}
