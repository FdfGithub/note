package com.qianjing.note.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qianjing.note.common.Const;
import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.dao.IHistoryMapper;
import com.qianjing.note.dao.INoteMapper;
import com.qianjing.note.dao.IOrdinaryTemplateMapper;
import com.qianjing.note.pojo.History;
import com.qianjing.note.pojo.Note;
import com.qianjing.note.pojo.template.OrdinaryTemplate;
import com.qianjing.note.service.INoteService;
import com.qianjing.note.util.DateTimeUtil;
import com.qianjing.note.vo.NoteInfoByOrdinaryTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("noteService")
public class NoteServiceImpl implements INoteService {

    @Autowired
    private INoteMapper noteMapper;

    @Autowired
    private IOrdinaryTemplateMapper ordinaryTemplateMapper;//经典模板

    @Autowired
    private IHistoryMapper historyMapper;
    /**
     * 插入和修改共用一个service方法
     * 根据参数是否有id值判断到底是插入操作，还是删除操作
     * @param note
     * @param template
     * @return
     */
    @Transactional
    public ServerResponse<NoteInfoByOrdinaryTemplateVO> insertNoteByOrdinaryTemplate(Note note, OrdinaryTemplate template) {
        template = note.getId() == null ? insertTemplate(template) : updateTemplate(template);
        if (template == null) {
            return ServerResponse.createByError();
        }
        //把操作时间点记录到History表中
        History history = assembleHistory(note);

        note.setTemplateId(template.getId());
        note = note.getId() == null?insertNote(note):updateNote(note);
        if (note == null) {
            return ServerResponse.createByError();
        }
        history.setNoteId(note.getId());
        boolean result = historyMapper.insertHistory(history);
        if (!result){
            return ServerResponse.createByError();
        }
        NoteInfoByOrdinaryTemplateVO vo = assembleNoteInfoByOrdinaryTemplateVO(note);
        assembleNoteInfoByOrdinaryTemplateVO(vo, template);
        return ServerResponse.createBySuccess(vo);
    }



    public ServerResponse<Void> updateStatus(Long noteId){
       return null;
    }




    @Transactional
    public ServerResponse<Void> deleteNoteById(Long noteId){
        Long templateId = noteMapper.selectTemplateIdById(noteId);
        if (templateId==null){
            return ServerResponse.createByErrorMessage("删除笔记失败");
        }
        boolean result = ordinaryTemplateMapper.deleteTemplate(templateId);
        if (!result){
            return ServerResponse.createByErrorMessage("删除笔记失败");
        }
        result = noteMapper.deleteNote(noteId);
        if (!result){
            return ServerResponse.createByErrorMessage("删除笔记失败");
        }
        History history = assembleHistory(new Note(noteId,RequestHolder.getId()));//userId noteId
        history.setOperation(Const.Operation.DELETE);
        history.setNoteId(noteId);
        result = historyMapper.insertHistory(history);
        if (!result){
            return ServerResponse.createByErrorMessage("删除笔记失败");
        }
        return ServerResponse.createBySuccessMessage("删除笔记成功");
    }





    public ServerResponse<PageInfo> selectNotesByStatus(Map<String, Object> map, Integer status) {
        /*在查询之前，刷新一下数据库
         * 把所有用户的所有笔记，超过开始时间的笔记并且状态仍未0（待开始的），就改为正在做
         * */
        ordinaryTemplateMapper.updateAllStatus();
        Long id = RequestHolder.getId();
        int pageNum = (Integer) map.get(Const.PageParam.PAGE_NUM);
        int pageSize = (Integer) map.get(Const.PageParam.PAGE_SIZE);
        String keyword = (String) map.get(Const.KEYWORD);
        String searchDate = (String) map.get(Const.SEARCH_DATE);
        if (keyword!=null){
            keyword = "%"+keyword+"%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Note> noteList = noteMapper.selectNotesLikeDate(id, searchDate + "%",keyword, status);
        if (noteList == null || noteList.size() == 0) {
            return ServerResponse.createByErrorMessage("抱歉，没找着~~~");
        }
        PageInfo pageInfo = new PageInfo(noteList);
        List<NoteInfoByOrdinaryTemplateVO> voList = assembleNoteList(noteList);
        pageInfo.setList(voList);
        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse<PageInfo> selectNotesByOrdinary(Map<String, Object> map) {
        return selectNotesByStatus(map, null);
    }






    public ServerResponse<Map<Integer,Integer>> selectNotesByMonth(Map<String, Object> map) {
        Long id = RequestHolder.getId();
        String date = (String) map.get(Const.Time.POINTERDate);
        List<Date> dates = noteMapper.selectNotesByMonth(date + "%", id);
        if (dates == null || dates.size() == 0) {
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(assembleMap(dates));
    }



    public ServerResponse<NoteInfoByOrdinaryTemplateVO> selectNoteByNoteId(Long noteId){
        List<Long> list = Lists.newArrayList();
        list.add(noteId);
        List<Note> notes = noteMapper.selectNotesByIds(list,RequestHolder.getId());
        if (notes == null || notes.size() != 1) {
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(assembleNoteList(notes).get(0));
    }


    public ServerResponse<PageInfo> selectNotesByNoteIds(List<Long> noteIds, Map<String, Integer> map) {
        int pageNum = (Integer) map.get(Const.PageParam.PAGE_NUM);
        int pageSize = (Integer) map.get(Const.PageParam.PAGE_SIZE);
        PageHelper.startPage(pageNum, pageSize);
        List<Note> notes = noteMapper.selectNotesByIds(noteIds,RequestHolder.getId());
        if (notes == null || notes.size() == 0) {
            return ServerResponse.createByErrorMessage("这一天，你还没有记录~~");
        }
        PageInfo pageInfo = new PageInfo(notes);
        pageInfo.setList(assembleNoteList(notes));
        return ServerResponse.createBySuccess(pageInfo);
    }



    public Long selectTemplateIdByNoteId(Long noteId){
        return noteMapper.selectTemplateIdById(noteId);
    }


    /**
     * 封装一个History对象
     * @param
     * @return
     */
    private History assembleHistory(Note note){
        History history = new History();
        history.setNoteName(note.getNoteName()==null?noteMapper.selectNoteName(note.getId()):note.getNoteName());
        history.setOperation(note.getId()==null?Const.Operation.CREATE:Const.Operation.UPDATE);
        history.setUserId(note.getUserId());
        return history;
    }


/*第一个Integer代表几号，第二个Integer代表有几条数据*/
    private Map<Integer, Integer> assembleMap(List<Date> dates) {
        Map<Integer, Integer> resultMap = Maps.newHashMap();
        Set<String> days = Sets.newHashSet();
        for (Date date : dates) {
            String day = DateTimeUtil.getDay(DateTimeUtil.dateToStr(date));
            days.add(day);
        }
        for (String day : days) {
            Integer count = 0;
            for (Date date : dates) {
                if (day.equals(DateTimeUtil.getDay(DateTimeUtil.dateToStr(date)))){
                    count++;
                }
            }
            resultMap.put(Integer.parseInt(day),count);
        }
        return resultMap;
    }


    private List<NoteInfoByOrdinaryTemplateVO> assembleNoteList(List<Note> notes) {
        List<NoteInfoByOrdinaryTemplateVO> list = Lists.newArrayList();
        for (Note note : notes) {
            NoteInfoByOrdinaryTemplateVO vo = assembleNoteInfoByOrdinaryTemplateVO(note);
            assembleNoteInfoByOrdinaryTemplateVO(vo, note.getOrdinaryTemplate());
            list.add(vo);
        }
        return list;
    }


    /**
     * 将笔记对象持久化
     *
     * @param note
     * @return
     */
    private Note insertNote(Note note) {
        boolean result = noteMapper.insertNote(note);
        if (result) {
            return note;
        }
        return null;
    }

    /**
     * 修改笔记内容
     *
     * @param
     * @return
     */
    private Note updateNote(Note note) {
        boolean result = noteMapper.updateNote(note);
        if (result) {
            return note;
        }
        return null;
    }


    /**
     * 创建经典模板
     *
     * @param template
     * @return
     */
    private OrdinaryTemplate insertTemplate(OrdinaryTemplate template) {
        boolean result = ordinaryTemplateMapper.insertTemplate(template);
        if (result) {
            return template;
        }
        return null;
    }


    /**
     * 修改经典模板
     */
    private OrdinaryTemplate updateTemplate(OrdinaryTemplate template) {
        boolean result = ordinaryTemplateMapper.updateTemplate(template);
        if (result) {
            return template;
        }
        return null;
    }


    /**
     * 笔记封装
     *
     * @param note
     * @return
     */
    private NoteInfoByOrdinaryTemplateVO assembleNoteInfoByOrdinaryTemplateVO(Note note) {
        NoteInfoByOrdinaryTemplateVO vo = new NoteInfoByOrdinaryTemplateVO();
        vo.setNoteId(note.getId());
        vo.setNoteName(note.getNoteName());
        if (note.getCreateTime() == null && note.getUpdateTime() == null) {
            vo.setNoteCreateTime(DateTimeUtil.dateToStr(new Date()));
            vo.setNoteUpdateTime(DateTimeUtil.dateToStr(new Date()));
        } else {
            vo.setNoteCreateTime(DateTimeUtil.dateToStr(note.getCreateTime()));
            vo.setNoteUpdateTime(DateTimeUtil.dateToStr(note.getUpdateTime()));
        }
        return vo;
    }

    /**
     * 模板封装
     *
     * @param vo
     * @param template
     */
    private void assembleNoteInfoByOrdinaryTemplateVO(NoteInfoByOrdinaryTemplateVO vo, OrdinaryTemplate template) {
        vo.setDescribe(template.getDescribe());
        if (template.getStatus()!=null){
            vo.setStatus(template.getStatus().equals(Const.NoteStatus.NO_START)?"待做":(template.getStatus().equals(Const.NoteStatus.DOING)?"正在做":"已结束"));
        }
        if (template.getStartTime()!=null){
            vo.setNoteStartTime(DateTimeUtil.getTime12(template.getStartTime()));
            vo.setNoteStartDate(DateTimeUtil.dateToStr(template.getStartTime(),"yyyy-MM-dd"));
        }
    }
}
