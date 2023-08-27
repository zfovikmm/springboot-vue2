package com.zm.springbootvue.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zm.springbootvue.controller.UserDTO.UserDTO;
import com.zm.springbootvue.entity.User;
import com.zm.springbootvue.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public int save(User user){
        if(user.getId() == null){
           return userMapper.insert(user);
        }else{
           return userMapper.update(user);
        }
    }

    public List<User> list(){
        return userMapper.findAll();
    }

    //批量插入
    public Boolean saveBatch(List<User> userList){
        return userMapper.insertUserList(userList);
    }

    public boolean login(User user) {
        User userload = userMapper.getInfo(user.getUsername(),user.getPassword());
        return userload != null;
    }

}
