package com.webproject.backend.mapper;

import com.webproject.backend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    List<User> getUsers(@Param("username")String username);

    @Select("select * from user where userId = #{userId}")
    User getUser(@Param("userId")int userId);

    @Select("select * from user")
    Set<User> getAllUsers();

    @Insert("insert into user (username,password,age,gender) values(#{username},#{password},#{age}," +
            "#{gender})")
    void insertUser(@Param("username")String username,@Param("password")String password,@Param("age")int age,
                    @Param("gender")String gender);
}
