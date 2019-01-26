package com.walker.test.dao.mapper;

import com.walker.test.dao.entity.Role;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {

    @Results(id="roles",value={
            @Result(column = "role_id", property = "roleId"),
            @Result(column = "role_name", property ="roleName"),
            @Result(column="role_desc", property = "roleDesc")
    })
    @Select("select * from role")
    List<Role> findAll();

    @Select("SELECT role_name FROM role WHERE role_id = (SELECT role_id FROM user_role WHERE user_id = #{userId})")
    @Results({@Result(column = "role_name", property = "roleName")})
    Role findRoleNameByUserId(@Param("userId") int userId);

}
