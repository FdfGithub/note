package com.qianjing.note.controller;


import com.qianjing.note.common.Const;
import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.common.ResponseCode;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.pojo.User;
import com.qianjing.note.service.IUserService;
import com.qianjing.note.to.RegisterInfoTO;
import com.qianjing.note.vo.UserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 查找用户信息
     * @param id
     * @return
     */
    @RequestMapping("/getUserInfo")
    public ServerResponse<UserInfoVO> getUserInfo(Long id) {
        if (id == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        return userService.selectUserById(id);
    }

    /**
     * 用户登录
     * @param account
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public ServerResponse<UserInfoVO> login(String account, String password, HttpSession session) {
        if (account == null || password == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        ServerResponse<UserInfoVO> response = userService.selectUserByAccountAndPassword(account, password);
        if (response.isSuccess()){
            session.setAttribute("user",response.getData());
            session.setMaxInactiveInterval(-1);
        }
        return response;
    }

    /**
     * 用户注册
     * @param to
     * @param session
     * @return
     */
    @RequestMapping("/register")
    public ServerResponse<Void> register(RegisterInfoTO to, HttpSession session) {
        if (to == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        ServerResponse<Void> response = userService.authCode(to.getCode(), session);
        if (response.isSuccess()) {
            return userService.insertUser(assembleUser(to));
        }
        return response;
    }

    /**
     * 包装User类
     * @param to
     * @return
     */
    private User assembleUser(RegisterInfoTO to) {
        User user = new User();
        if (Const.EMAIL.equals(to.getType())) {
            user.setUserEmail(to.getAccount());
        } else {
            user.setUserPhone(to.getAccount());
        }
        user.setUserPassword(to.getPassword());
        return user;
    }

    /**
     * 获取验证码
     * @param account
     * @param type
     * @param session
     * @return
     */
    @RequestMapping("/getCode")
    public ServerResponse<String> getCode(String account, String type, HttpSession session) {
        if (account == null || type == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        ServerResponse<String> response = userService.getCodeByAccount(account, type);
        if (response.isSuccess()) {
            session.setAttribute("code", response.getData());
            session.setMaxInactiveInterval(60);
            response.setData(StringUtils.EMPTY);
        }
        return response;
    }

    /**
     * 检验账号是否重复
     * @param account
     * @return
     */
    @RequestMapping("/isRepeat")
    public boolean isRepeat(String account) {
        if (account == null) {
            return false;
        }
        return userService.selectUserByAccount(account);
    }

    /**
     * 安全认证（修改密码）
     * @param account
     * @param code
     * @param session
     * @return
     */
    @RequestMapping("/safeAuth")
    public ServerResponse<Void> safeAuth(String account, String code, HttpSession session) {
        if (StringUtils.isEmpty(account)||StringUtils.isEmpty(code)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        ServerResponse<Void> response = userService.authCode(code,session);
        if (response.isSuccess()){
            session.setAttribute("account",account);
            session.setMaxInactiveInterval(60*5);
        }
        return response;
    }

    /**
     * 修改密码
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("/forgetPassword")
    public ServerResponse<Void> forgetPassword(String password, HttpSession session) {
        if (StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        String account = (String)session.getAttribute("account");
        if (StringUtils.isEmpty(account)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.AUTH_TIMEOUT.getCode(),
                    "验证超时，无法继续修改密码，请重新验证");
        }
        return userService.updatePasswordByAccount(account,password);
    }


    @RequestMapping("/isLogin")
    public boolean isLogin(){
        return RequestHolder.getId()!=null;
    }


    @RequestMapping("/logout")
    public void logout(HttpSession session){
        session.removeAttribute("user");
    }
}
