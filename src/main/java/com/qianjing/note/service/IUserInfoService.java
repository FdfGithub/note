package com.qianjing.note.service;


import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.pojo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface IUserInfoService {
    ServerResponse<UserInfo> selectUserInfoById();

    ServerResponse<String> updateHeadUrl(MultipartFile file);
}
