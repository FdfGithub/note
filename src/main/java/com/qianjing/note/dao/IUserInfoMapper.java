package com.qianjing.note.dao;


import com.qianjing.note.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("userInfoMapper")
public interface IUserInfoMapper {
    boolean insertUserInfo(@Param("id") Long id, @Param("headUrl") String headUrl, @Param("defaultValue") String defaultValue);

    UserInfo selectUserInfoById(@Param("id") Long id);

    boolean updateUserInfoById(@Param("id") Long id,@Param("headUrl")String headUrl);
}
