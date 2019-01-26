package com.walker.test.dao.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionMapper {

    @Select("SELECT p.per_name FROM permission p LEFT JOIN role_permission rp ON rp.`per_id`=p.`per_id`\n" +
            " LEFT JOIN user_role ur ON ur.`role_id` = rp.`role_id` WHERE ur.`user_id` = #{userId}\n"+
            "  GROUP BY p.`per_id` ORDER BY p.`per_id` ASC")
    List<String> findPersUnderRoleIdByUserId(@Param("userId")int userId);

    @Select(" SELECT p.per_name FROM permission p LEFT JOIN user_permission up ON p.`per_id`=up.`per_id` WHERE up.`user_id` =#{userId}")
    List<String> findPersByUserId(@Param("userId") int userId);


}
