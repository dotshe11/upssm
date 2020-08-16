package com.winsun.upssm.mapper;

import com.winsun.upssm.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface userMapper {

    @Select("select username from user where username=#{username} and password=#{password}")
    String login(String username, String password);


    @Insert("insert into user(username,password) values(#{username},#{password})")
    void registered(String username, String password);
}
