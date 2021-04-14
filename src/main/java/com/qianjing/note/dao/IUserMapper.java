package com.qianjing.note.dao;


import com.qianjing.note.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("userMapper")
public interface IUserMapper {
    User selectUserById(Long id);

    boolean insertUser(User user);

    Integer selectUserByAccount(String account);

    User selectUserByAccountAndPassword(@Param("account") String account, @Param("password") String password);

    boolean updatePasswordByAccount(@Param("account") String account, @Param("password") String password);
}
