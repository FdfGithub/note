package com.qianjing.note.service.impl;


import com.qianjing.note.common.Const;
import com.qianjing.note.common.ResponseCode;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.dao.IUserInfoMapper;
import com.qianjing.note.dao.IUserMapper;
import com.qianjing.note.pojo.User;
import com.qianjing.note.service.IUserService;
import com.qianjing.note.util.DateTimeUtil;
import com.qianjing.note.util.MD5Util;
import com.qianjing.note.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;


@Service("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private IUserInfoMapper userInfoMapper;

    @Override
    public ServerResponse<UserInfoVO> selectUserById(Long id) {
        User user = userMapper.selectUserById(id);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        return ServerResponse.createBySuccess("登录成功", assembleUserInfoVO(user));
    }

    @Value("${headUri_default}")
    private String headUriDefault;

    @Autowired
    private MD5Util md5Util;

    @Transactional
    @Override
    public ServerResponse<Void> insertUser(User user) {
        if ((user.getUserEmail() == null && user.getUserPhone() == null) || user.getUserPassword() == null) {
            return ServerResponse.createByErrorMessage("注册信息错误");
        }
        user.setUserPassword(md5Util.MD5EncodeUtf8(user.getUserPassword()));
        boolean result = userMapper.insertUser(user);
        //拿到用户id，然后把用户信息表也给创建了
        if (!result){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        result = userInfoMapper.insertUserInfo(user.getId(), headUriDefault,"");
        if (result) {
            return ServerResponse.createBySuccessMessage("注册成功");
        }
        return ServerResponse.createByErrorMessage("系统错误，注册失败");
    }

    @Override
    public ServerResponse<String> getCodeByAccount(String account, String type) {
        String code = "";
        try {
            code = Const.EMAIL.equals(type) ? sendCodeByEmail(account) : sendCodeByPhone(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (code == null) {
            return ServerResponse.createByErrorMessage("验证码发送失败，请重新获取");
        }
        return ServerResponse.createBySuccess("验证码已发送", code);
    }



    @Override
    public ServerResponse<UserInfoVO> selectUserByAccountAndPassword(String account, String password) {
        User user = userMapper.selectUserByAccountAndPassword(account, md5Util.MD5EncodeUtf8(password));
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PASSWORD_FAIL.getCode(),
                    "密码错误");
        }
        return ServerResponse.createBySuccess("登录成功", assembleUserInfoVO(user));
    }

    private UserInfoVO assembleUserInfoVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        vo.setId(user.getId());
        vo.setEmail(user.getUserEmail());
        vo.setPhone(user.getUserPhone());
        //时间处理
        vo.setUpdateTime(DateTimeUtil.dateToStr(user.getUpdateTime()));
        return vo;
    }

    @Override
    public boolean selectUserByAccount(String account) {
        int resultNum = userMapper.selectUserByAccount(account);
        return resultNum > 0;
    }

    public ServerResponse<Void> updatePasswordByAccount(String account, String password) {
        boolean result = userMapper.updatePasswordByAccount(account, password);
        if (result) {
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }
        return ServerResponse.createByError();
    }

    public ServerResponse<Void> authCode(String code, HttpSession session) {
        String localCode = (String) session.getAttribute("code");
        if (localCode == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.CODE_INVALID.getCode(),
                    "验证码已经失效");
        }
        if (localCode.equals(code)) {
            session.removeAttribute("code");
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.CODE_FAIL.getCode(),
                "验证码错误");
    }


    private String randomCode() {
        //100000-999999
        int code = (int) (Math.random() * 900000 + 100000);
        return String.valueOf(code);
    }



    @Autowired
    JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String me;


    private String sendCodeByEmail(String email) {
        String code = randomCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【千镜-望记】 注册");
        message.setText("验证码为：" + code);
        message.setFrom(me);
        message.setTo(email);
        mailSender.send(message);
        return code;
    }

    private String sendCodeByPhone(String phone) {
        return null;
    }
}
