package cn.liuxh.mapper;

import cn.liuxh.model.Goods;
import cn.liuxh.model.LocationCheckOrder;
import cn.liuxh.model.LocationSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 16/11/28.
 */
public interface LocationCheckOrderMapper {

    List getAll(@Param(value="start")int start, @Param(value="rows")int rows);

    List getAllSku(@Param(value="id")int id);

    int count();

    int update(LocationCheckOrder product);
    int add(LocationCheckOrder product);
    int delete(@Param(value="id")int id);

    LocationCheckOrder getDetailById(@Param(value="id")int id);
    List<Goods> getDetailByLocationNo(@Param(value="id")String id);

    int deleteSku(@Param(value="id")int id);
    int addSku(List list);
    List<LocationSku> getSkus(@Param(value="orderId")int id);
}
