package com.qianjing.note.controller;



import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.common.ResponseCode;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.pojo.UserInfo;
import com.qianjing.note.service.IUserInfoService;
import com.qianjing.note.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询用户的基本信息
     */
    @RequestMapping("/selectUserInfo")
    public ServerResponse<UserInfo> selectUserInfo(){
        if (RequestHolder.getId() ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        return userInfoService.selectUserInfoById();
    }



    /**
     * 上传头像
     */
    @RequestMapping("/changeHead")
    public ServerResponse<String> changeHead(MultipartFile upload) throws IOException {
        if (RequestHolder.getId()==0){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (upload==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return userInfoService.updateHeadUrl(upload);
    }

}
