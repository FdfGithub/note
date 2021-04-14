package com.qianjing.note.controller;


import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.qianjing.note.common.Const;
import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.common.ResponseCode;
import com.qianjing.note.common.ServerResponse;
import com.qianjing.note.pojo.Note;
import com.qianjing.note.pojo.template.OrdinaryTemplate;
import com.qianjing.note.service.INoteService;
import com.qianjing.note.service.IUserService;
import com.qianjing.note.to.NoteInfoByOrdinaryTemplateTO;
import com.qianjing.note.util.DateTimeUtil;
import com.qianjing.note.vo.NoteInfoByOrdinaryTemplateVO;
import com.qianjing.note.vo.QueryVO;
import com.qianjing.note.vo.SearchNotesVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private INoteService noteService;

    @Autowired
    private IUserService userService;


    /**
     * 添加一条笔记
     */
    /*
     *       1、创建记事模板；
     *       2、创建笔记对象
     *       3、联系笔记和模板
     * */
    @RequestMapping("/addNoteByOrdinaryTemplate")
    public ServerResponse<NoteInfoByOrdinaryTemplateVO> addNoteByOrdinaryTemplate(NoteInfoByOrdinaryTemplateTO to) {
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (to == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        OrdinaryTemplate template = assembleOrdinaryTemplate(to);
        Note note = assembleNote(to, template.getTemplateName());
        note.setUserId(RequestHolder.getId());
        return noteService.insertNoteByOrdinaryTemplate(note, template);
    }


    @RequestMapping("/getNotesByOrdinary")
    public ServerResponse<PageInfo> getNotesByOrdinary(SearchNotesVO vo) {
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (vo==null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put(Const.SEARCH_DATE, DateTimeUtil.dateToStr(vo.getSearchDate(),"yyyy-MM-dd"));
        map.put(Const.PageParam.PAGE_NUM, vo.getPageNum());
        map.put(Const.PageParam.PAGE_SIZE, vo.getPageSize());
        String keyword = vo.getKeyword();
        if(!"null".equals(keyword)){
            map.put(Const.KEYWORD,keyword.trim());
        }
        return noteService.selectNotesByOrdinary(map);
    }


    /**
     * 模糊查询
     *
     * @param
     * @return
     */
    @RequestMapping("/calendarView")
    public ServerResponse<Map<Integer,Integer>> calendarView(Date pointerDate) {
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (pointerDate==null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put(Const.Time.POINTERDate, DateTimeUtil.dateToStr(pointerDate,"yyyy-MM"));
        return noteService.selectNotesByMonth(map);
    }

    /**
     * 根据noteId查笔记
     * @param
     * @return
     */
    @RequestMapping("/getNotesByNoteId")
    public ServerResponse<NoteInfoByOrdinaryTemplateVO> getNotesByNoteId(Long noteId,HttpSession session){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (noteId==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return noteService.selectNoteByNoteId(noteId);
    }



    /**
     * 根据多个noteId查笔记
     * @param
     * @return
     */
    @RequestMapping("/getNotesByNoteIds")
    public ServerResponse<PageInfo> getNotesByNoteIds(QueryVO vo, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "4") Integer pageSize){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (vo==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Map<String,Integer> map = Maps.newHashMap();
        map.put(Const.PageParam.PAGE_NUM, pageNum);
        map.put(Const.PageParam.PAGE_SIZE, pageSize);
        return noteService.selectNotesByNoteIds(vo.getNoteIds(),map);
    }


    /**
     * 查询所有状态为待做的
     * @param
     * @param
     * @return
     */
    @RequestMapping("/getNotesByStatusIsNoStart")
    public ServerResponse<PageInfo> getNotesByStatusIsNoStart(SearchNotesVO vo){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (vo==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put(Const.SEARCH_DATE, DateTimeUtil.dateToStr(vo.getSearchDate(),"yyyy-MM-dd"));
        map.put(Const.PageParam.PAGE_NUM, vo.getPageNum());
        map.put(Const.PageParam.PAGE_SIZE, vo.getPageSize());
        String keyword = vo.getKeyword();
        if(!"null".equals(keyword)){
            map.put(Const.KEYWORD,keyword.trim());
        }
        return noteService.selectNotesByStatus(map,Const.NoteStatus.NO_START);
    }


    /**
     * 查询所有状态为正在做的
     * @param
     * @param
     * @return
     */
    @RequestMapping("/getNotesByStatusIsDoing")
    public ServerResponse<PageInfo> getNotesByStatusIsDoing(SearchNotesVO vo){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (vo==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put(Const.SEARCH_DATE, DateTimeUtil.dateToStr(vo.getSearchDate(),"yyyy-MM-dd"));
        map.put(Const.PageParam.PAGE_NUM, vo.getPageNum());
        map.put(Const.PageParam.PAGE_SIZE, vo.getPageSize());
        String keyword = vo.getKeyword();
        if(!"null".equals(keyword)){
            map.put(Const.KEYWORD,keyword.trim());
        }
        return noteService.selectNotesByStatus(map,Const.NoteStatus.DOING);
    }



    /**
     * 查询所有状态为已结束的
     * @param
     * @param
     * @return
     */
    @RequestMapping("/getNotesByStatusIsEnd")
    public ServerResponse<PageInfo> getNotesByStatusIsEnd(SearchNotesVO vo){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (vo==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put(Const.SEARCH_DATE, DateTimeUtil.dateToStr(vo.getSearchDate(),"yyyy-MM-dd"));
        map.put(Const.PageParam.PAGE_NUM, vo.getPageNum());
        map.put(Const.PageParam.PAGE_SIZE, vo.getPageSize());
        String keyword = vo.getKeyword();
        if(!"null".equals(keyword)){
            map.put(Const.KEYWORD,keyword.trim());
        }
        return noteService.selectNotesByStatus(map,Const.NoteStatus.END);
    }




/*   1、详细内容    前端直接处理
         2、修改
         3、删除
         4、备注
         5、我已完成
*/

    /**
     *  根据 noteId 修改笔记内容
     * @param
     * @return
     */
    @RequestMapping("/updateNoteContent")
    public ServerResponse<NoteInfoByOrdinaryTemplateVO> updateNoteContent(NoteInfoByOrdinaryTemplateTO to){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (to == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        OrdinaryTemplate template = assembleOrdinaryTemplate(to);
        Note note = assembleNote(to, template.getTemplateName());
        //得利用noteId查出templateId,然后封装到template上
        Long templateId = noteService.selectTemplateIdByNoteId(note.getId());
        if (templateId!=null){
            template.setId(templateId);
        }
        /*修改不需要用户id的辅助，因为id本身就是唯一的，但是我要记录history表*/
        note.setUserId(RequestHolder.getId());
        return noteService.insertNoteByOrdinaryTemplate(note, template);
    }


    /**
     *  根据 noteId 删除笔记内容
     * @param
     * @return
     */
    @RequestMapping("/deleteNote")
    public ServerResponse<Void> deleteNote(Long noteId){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (noteId==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return noteService.deleteNoteById(noteId);
    }


    /**
     * 我已完成（实质也是修改笔记内容）
     * @param
     * @return
     */
    @RequestMapping("/doingOver")
    public ServerResponse<Void> doingOver(Long noteId){
        if (RequestHolder.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录");
        }
        if (noteId==null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return noteService.updateStatus(noteId);
    }







    private OrdinaryTemplate assembleOrdinaryTemplate(NoteInfoByOrdinaryTemplateTO to) {
        OrdinaryTemplate template = new OrdinaryTemplate();
        template.setDescribe(to.getDescribe());
        template.setTemplateName(Const.TemplateType.ORDINARY);
        if (to.getStartTime()!=null){
//            template.setStartTime(DateTimeUtil.strToDate(to.getStartTime().replace("T", StringUtils.SPACE), "yyyy-MM-dd HH:mm"));
            //设置状态
            setStatus(template);
        }
        return template;
    }


    private Note assembleNote(NoteInfoByOrdinaryTemplateTO to, String templateName) {
        Note note = new Note();
        // 封装笔记的id
        if (to.getNoteId()!=null){
            note.setId(to.getNoteId());
        }
        note.setNoteName(to.getNoteName());
        note.setTemplateName(templateName);
        return note;
    }

    private void setStatus(OrdinaryTemplate template) {
        long start = template.getStartTime().getTime();
        long now = new Date().getTime();
        if (start - now > 0) {
            template.setStatus(Const.NoteStatus.NO_START);
        }else {
            template.setStatus(Const.NoteStatus.DOING);
        }
    }


}
