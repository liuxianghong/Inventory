package cn.liuxh.mapper;

import cn.liuxh.model.Group;
import org.apache.ibatis.annotations.Param;

/**
 * Created by liuxianghong on 2017/2/24.
 */
public interface GroupMapper {
    Group findGroupInfo(@Param(value="admin")String name);
}
