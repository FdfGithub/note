package com.qianjing.note.controller;


import com.google.common.collect.Maps;
import com.qianjing.note.common.Const;
import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.common.ResponseCode;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.service.IHistoryService;
import com.qianjing.note.service.IUserService;
import com.qianjing.note.to.HistoryTO;
import com.qianjing.note.vo.HistoryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private IHistoryService historyService;



    @RequestMapping("/selectHistory")
    public ServerResponse<List<HistoryVO>> selectHistory(HistoryTO to){
        if (RequestHolder.getId() == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        Map<String,Object> map = Maps.newHashMap();

        map.put(Const.SEARCH_DATE,to.getSearchDate());
        if (StringUtils.isNotEmpty(to.getKeyword())){
            map.put(Const.KEYWORD,to.getKeyword().trim());
        }
        if (!to.getOperate().equals(Const.Operation.All)){
            map.put(Const.Operation.OPERATE,to.getOperate());
        }
        return historyService.selectHistory(map);
    }
}
