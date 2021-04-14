package com.qianjing.note.service;

import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.vo.HistoryVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IHistoryService {

    ServerResponse<List<HistoryVO>> selectHistory(Map<String,Object> map);
}
