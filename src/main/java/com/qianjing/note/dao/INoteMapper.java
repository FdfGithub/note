package com.qianjing.note.dao;


import com.qianjing.note.pojo.Note;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository("noteMapper")
public interface INoteMapper {

    boolean insertNote(Note note);

    List<Note> selectNotesLikeDate(@Param("userId") Long userId, @Param("searchDate") String searchDate,@Param("keyword") String keyword, @Param("status") Integer status);


    List<Date> selectNotesByMonth(@Param("time") String time, @Param("userId") Long userId);


    List<Note> selectNotesByIds(@Param("ids") List<Long> ids,@Param("userId") Long userId);

    boolean updateNote(Note note);

    Long selectTemplateIdById(Long id);

    boolean deleteNote(Long id);

    String selectNoteName(Long id);
}
