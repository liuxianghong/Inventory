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

    List getAll(@Param(value = "start") int start, @Param(value = "rows") int rows,@Param(value = "groupId") int groupId);

    List getAllSku(@Param(value="id")int id);

    int count(@Param(value = "groupId") int groupId);

    List getAllLocationOrdersE(@Param(value = "start") int start, @Param(value = "rows") int rows,@Param(value = "groupId") int groupId);
    int SkuCount(@Param(value = "groupId") int groupId);

    int update(LocationCheckOrder product);
    int add(LocationCheckOrder product);
    int delete(@Param(value="id")int id);

    LocationCheckOrder getDetailById(@Param(value="id")int id);
    List<Goods> getDetailByLocationNo(@Param(value = "id") String id, @Param(value = "groupId") int groupId);

    int deleteSku(@Param(value="id")int id);
    int addSku(List list, int groupId);
    List<LocationSku> getSkus(@Param(value="orderId")int id);

    int truncate(@Param(value = "groupId") int groupId);
}
