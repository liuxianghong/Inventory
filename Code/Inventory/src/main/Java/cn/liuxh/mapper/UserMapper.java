package cn.liuxh.mapper;

import cn.liuxh.model.User;
import cn.liuxh.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 16/11/23.
 */
public interface UserMapper {
    User findUserInfo(@Param(value="userName")String name);
    int login(User usr);
    User getUserInfoById(@Param(value="id")int id);

    List getAllUsers(@Param(value="start")int start, @Param(value="rows")int rows);
    int selectCount();
    int updateUser(User User);
    int addUser(User User);
    int delUser(@Param(value="id")int id);
}
