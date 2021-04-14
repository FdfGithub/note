package com.qianjing.note.service;


import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.pojo.User;
import com.qianjing.note.vo.UserInfoVO;

import javax.servlet.http.HttpSession;

public interface IUserService {
    ServerResponse<UserInfoVO> selectUserById(Long id);

    ServerResponse<Void> insertUser(User user);

    ServerResponse<String> getCodeByAccount(String account, String type);

    ServerResponse<UserInfoVO> selectUserByAccountAndPassword(String account, String password);

    boolean selectUserByAccount(String account);

    ServerResponse<Void> updatePasswordByAccount(String account, String password);

    ServerResponse<Void> authCode(String code, HttpSession session);
}
