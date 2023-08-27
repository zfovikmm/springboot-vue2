package com.zm.springbootvue.mapper;

import com.zm.springbootvue.entity.User;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from sys_user")
    List<User> findAll();

    @Insert("INSERT INTO sys_user(username,password,nickname,email,phone,address) values (#{username},#{password},#{nickname},#{email},#{phone},#{address})")
    int insert(User user);


    int update(User user);

    @Delete("delete from sys_user where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    //@Select("select * from sys_user where username like concat('%',#{username},'%') limit #{pageNum},#{pageSize}")
    List<User> selectPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("id") Integer id, @Param("username") String username,@Param("nickname") String nickname);

    //@Select("select count(*) from sys_user where username like concat('%',#{username},'%')")
    Integer selectTotal(@Param("id") Integer id,@Param("username") String username,@Param("nickname") String nickname);

    @Select("select count(*) from sys_user")
    Integer selectCount();

    @Select("select * from sys_user limit #{pageNum},#{pageSize}")
    List<User> selectAll(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);


    //@Delete("delete from sys_user where id in (#{ids})")
    Boolean deleteBatch(@Param("list") List<Integer> ids);

    //批量插入
    Boolean insertUserList(@Param("userList") List<User> userList);

    //登录
    @Select("select * from sys_user WHERE username = #{username} and password = #{password}")
    User getInfo(@Param("username") String username,@Param("password") String password);
}
