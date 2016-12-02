package cn.liuxh.service;

import cn.liuxh.mapper.UserMapper;
import cn.liuxh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuxianghong on 16/11/23.
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserInfo(String name){
        User user=userMapper.findUserInfo(name);
        //User user=null;
        return user;
    }

    public int login(User usr){
        return userMapper.login(usr);
    }

    public User getUserInfoById(int name){
        User user=userMapper.getUserInfoById(name);
        return user;
    }
}
