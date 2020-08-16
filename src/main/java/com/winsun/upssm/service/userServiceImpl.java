package com.winsun.upssm.service;

import com.winsun.upssm.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {
    @Autowired
    userMapper userMapper;

    @Override
    public String login(String username, String password) {

        return userMapper.login(username, password);
    }

    @Override
    public void registered(String username, String password) {
         userMapper.registered(username,password);
    }
}
