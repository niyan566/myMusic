package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    User login(User user);

    @Select("select * from `user` where id=#{id}")
    User selectById(Integer id);

    @Select("select * from `user` where username=#{username}")
    User selectByUsername(String username);
}
