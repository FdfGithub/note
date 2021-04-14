package com.qianjing.note.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qianjing.note.common.Const;
import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.dao.IHistoryMapper;
import com.qianjing.note.pojo.History;
import com.qianjing.note.service.IHistoryService;
import com.qianjing.note.util.DateTimeUtil;
import com.qianjing.note.vo.HistoryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("historyService")
public class HistoryServiceImpl implements IHistoryService {

    @Autowired
    private IHistoryMapper historyMapper;

    @Override
    public ServerResponse<List<HistoryVO>> selectHistory(Map<String,Object> map) {
        Date date = (Date) map.get(Const.SEARCH_DATE);
        Integer operate = (Integer) map.get(Const.Operation.OPERATE);
        String keyword = (String) map.get(Const.KEYWORD);
        if (keyword!=null){
            keyword = "%"+keyword+"%";
        }
        Long userId = RequestHolder.getId();
        List<String> days = getSearchDates(date, userId);
        List<History> list = historyMapper.selectHistoryByDate(days, userId,operate,keyword);
        if (list == null || list.size() == 0) {
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(assembleHistoryVOList(list, days));
    }


    /*封装HistoryVO数据*/
    private List<HistoryVO> assembleHistoryVOList(List<History> histories, List<String> days) {
        List<HistoryVO> voList = Lists.newArrayList();
        for (String day : days) {
            day = day.replace("%", "");
            HistoryVO vo = new HistoryVO();
            //key代表操作时间点，value代表笔记id+笔记名称
            Map<String, String> map = Maps.newTreeMap((o1, o2) -> {
                Date date1 = DateTimeUtil.strToDate(o1.split(StringUtils.SPACE)[1], "HH:mm");
                Date date2 = DateTimeUtil.strToDate(o2.split(StringUtils.SPACE)[1], "HH:mm");
                if ((o1.split(StringUtils.SPACE)[0].equals("上午") ||
                        o1.split(StringUtils.SPACE)[0].equals("中午")) &&
                        o2.split(StringUtils.SPACE)[0].equals("下午")) {
                    return 1;
                } else if (date1.getTime() > date2.getTime()) {
                    return -1;
                } else if (date1.getTime() < date2.getTime()) {
                    return 1;
                }
                return 0;
            });
            for (History history : histories) {
                if (day.equals(DateTimeUtil.dateToStr(history.getCreateTime(), "yyyy-MM-dd"))) {
                    map.put(DateTimeUtil.getTime12(history.getCreateTime()),
                            history.getNoteId() + "_" + history.getNoteName());
                }
            }
            vo.setDate(day);
            vo.setMap(map);
            voList.add(vo);
        }
        return voList;
    }

    /*map进行置换*/


    /*获得需要查序的所有日期*/
    public List<String> getSearchDates(Date firstDay, Long userId) {
        List<String> days = Lists.newArrayList();
        int count = 0;
        String day;
        int max = historyMapper.selectCountByUserIdAndDate(userId, DateTimeUtil.dateToStr(firstDay));
        if (max > Const.PageParam.DEFAULT_SIZE) {
            max = Const.PageParam.DEFAULT_SIZE;
        }
        while (count < max) {
            day = DateTimeUtil.dateToStr(firstDay, "yyyy-MM-dd") + "%";
            int result = historyMapper.selectCountByDate(day, userId);
            count += result;
            if (result > 0) {
                days.add(day);
            }
            firstDay.setDate(firstDay.getDate() - 1);
        }
        return days;
    }
}
