package com.project.ribbon.test;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
    public Integer categoryId(@Param("categoryid") Integer categoryid);
    public Integer inherentId(@Param("inherentid") Integer inherentid);
    public Integer userId(@Param("userid") Integer userid);
}
