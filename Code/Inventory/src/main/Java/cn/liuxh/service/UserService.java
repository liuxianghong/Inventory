package cn.liuxh.service;

import cn.liuxh.mapper.UserMapper;
import cn.liuxh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Created by liuxianghong on 16/11/23.
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserInfo(){
        User user=userMapper.findUserInfo();
        //User user=null;
        return user;
    }

}
