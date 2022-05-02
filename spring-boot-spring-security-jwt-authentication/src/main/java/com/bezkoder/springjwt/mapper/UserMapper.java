package com.bezkoder.springjwt.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    Boolean insertUser(Long id,String username,String email,String password);
}
