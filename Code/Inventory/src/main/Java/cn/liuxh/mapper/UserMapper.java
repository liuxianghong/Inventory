package cn.liuxh.mapper;

import cn.liuxh.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by liuxianghong on 16/11/23.
 */
public interface UserMapper {
    User findUserInfo(@Param(value="userName")String name);
    int login(User usr);
    User getUserInfoById(@Param(value="id")int id);
}
