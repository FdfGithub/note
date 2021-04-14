package com.qianjing.note.service.impl;


import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.dao.IUserInfoMapper;
import com.qianjing.note.pojo.UserInfo;
import com.qianjing.note.service.IUserInfoService;
import com.qianjing.note.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("userInfoService")
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private IUserInfoMapper userInfoMapper;

    @Value("${headUrl_pre}")
    private String headUrlPre;

    @Override
    public ServerResponse<UserInfo> selectUserInfoById() {
        UserInfo userInfo = userInfoMapper.selectUserInfoById(RequestHolder.getId());
        if (userInfo == null) {
            return ServerResponse.createByError();
        }
        //对head_url进行拼接
        userInfo.setHeadUrl(headUrlPre + userInfo.getHeadUrl());
        return ServerResponse.createBySuccess(userInfo);
    }

    @Value("${localAddress_pre}")
    private String localAddressPre;

    @Transactional
    public ServerResponse<String> updateHeadUrl(MultipartFile file) {
        Long id = RequestHolder.getId();
        String localAddress = localAddressPre + id + "/";
        File parent = new File(localAddress);
        if (!parent.exists()) {
            parent.mkdirs();
        } else {
            FileUtil.delFile(parent);
        }
        String filename = file.getOriginalFilename();
        filename = UUID.randomUUID().
                toString().replace("-", "") + FileUtil.getSuffix(filename);
        /*headUri保存到数据库中*/
        String headUri = id + "/" + filename;
        String headUrl = headUrlPre + headUri;
        try {
            file.transferTo(new File(parent, filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean result = userInfoMapper.updateUserInfoById(id, headUri);
        if (!result) {
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(headUrl);
    }


}
