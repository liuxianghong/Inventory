package cn.liuxh.mapper;

import cn.liuxh.model.LocationCheckOrder;
import cn.liuxh.model.SkuCheckOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 16/11/28.
 */
public interface LocationCheckOrderMapper {

    List getAll();

    List getAllSku(@Param(value="id")int id);

    int count();

    int update(LocationCheckOrder product);
    int add(LocationCheckOrder product);
    int delete(@Param(value="id")int id);

    LocationCheckOrder getDetailById(@Param(value="id")int id);
}
