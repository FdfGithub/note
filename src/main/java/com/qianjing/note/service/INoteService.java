package com.qianjing.note.service;


import com.github.pagehelper.PageInfo;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.pojo.Note;
import com.qianjing.note.pojo.template.OrdinaryTemplate;
import com.qianjing.note.vo.NoteInfoByOrdinaryTemplateVO;

import java.util.List;
import java.util.Map;

public interface INoteService {
    ServerResponse<NoteInfoByOrdinaryTemplateVO> insertNoteByOrdinaryTemplate(Note note, OrdinaryTemplate template);

    ServerResponse<PageInfo> selectNotesByOrdinary(Map<String, Object> map);

    ServerResponse<Map<Integer,Integer>> selectNotesByMonth(Map<String, Object> map);

    ServerResponse<PageInfo> selectNotesByNoteIds(List<Long> noteIds, Map<String, Integer> map);

    ServerResponse<PageInfo> selectNotesByStatus(Map<String, Object> map, Integer status);

    Long selectTemplateIdByNoteId(Long noteId);

    ServerResponse<Void> deleteNoteById(Long noteId);

    ServerResponse<Void> updateStatus(Long noteId);

    ServerResponse<NoteInfoByOrdinaryTemplateVO> selectNoteByNoteId(Long noteId);
}
