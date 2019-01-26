package com.walker.test.dao.mapper;

import com.walker.test.dao.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    @Results(id = "userMap",value={
            @Result(column = "user_id",property = "userId"),
            @Result(column = "account",property = "account"),
            @Result(column = "password",property = "password"),
            @Result(column = "is_lock",property = "lock")
    })
    @Select("select user_id,account,password from user")
    List<User> findAll();

    @Select("select user_id,password,salt,is_lock from user where account = #{account}")
    @ResultMap("userMap")
    User findInfoByAccount(@Param("account") String account);

    @Select("select user_id from user where account = #{account}")
    @Results({@Result(column="user_id", property = "userId")})
    int findUserIdByAccount(@Param("account") String account);
}
