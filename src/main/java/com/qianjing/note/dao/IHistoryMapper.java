package com.qianjing.note.dao;


import com.qianjing.note.pojo.History;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("historyMapper")
public interface IHistoryMapper {
    boolean insertHistory(History history);

    List<History> selectHistoryByUserId(Long userId);

    List<History> selectHistoryByDate(@Param("dates") List<String> dates, @Param("userId") Long userId,
                        @Param("operation") Integer operation,@Param("keyword")String keyword);

    int selectCountByDate(@Param("date") String date, @Param("userId") Long userId);


    int selectCountByUserIdAndDate(@Param("userId")Long userId,@Param("date")String date);
}
