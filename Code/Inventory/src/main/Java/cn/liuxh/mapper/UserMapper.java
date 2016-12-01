package cn.liuxh.mapper;

import cn.liuxh.model.User;

/**
 * Created by liuxianghong on 16/11/23.
 */
public interface UserMapper {
    public User findUserInfo();
    int login(User usr);
}
