package cn.liuxh.service;

import cn.liuxh.mapper.GroupMapper;
import cn.liuxh.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuxianghong on 2017/2/24.
 */
@Service
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;

    public Group getGroupInfo(String name){
        Group user=groupMapper.findGroupInfo(name);
        //User user=null;
        return user;
    }
}
