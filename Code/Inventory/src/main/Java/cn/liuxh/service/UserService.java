package cn.liuxh.service;

import cn.liuxh.mapper.UserMapper;
import cn.liuxh.model.User;
import cn.liuxh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List getAllUsers(int start, int rows,int groupId){
        return userMapper.getAllUsers(start, rows,groupId);
    }

    public int selectCount(){
        return userMapper.selectCount();
    }

    public int updateUser(User User) {
        return  userMapper.updateUser(User);
    }

    public int addUser(User User){
        return  userMapper.addUser(User);
    }

    public int delUser(int id){
        return  userMapper.delUser(id);
    }
}
