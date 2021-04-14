package com.qianjing.note.common;

/**
 * Created by geely
 */
public class Const {
    public static final String EMAIL = "email";
    public static final String ID = "id";
    public static final String SEARCH_DATE = "searchDate";
    public static final String KEYWORD = "keyword";


    public interface  Operation{
        final String OPERATE = "operate";
        final Integer All = -1;
        final Integer CREATE = 1;//创建
        final Integer UPDATE = 2;//删除
        final Integer DELETE = 3;//修改
    }


    public interface NoteStatus{
        final String STATUS = "status";
        final Integer NO_START = 0;
        final Integer DOING = 1;
        final  Integer END = 2;
    }

    public interface Time{
        final String POINTERDate = "pointerDate";
        final String TIME_SPOT = "timeSpot";
    }


    public interface Note{
        final String NOTE_NAME = "noteName";
    }

    public interface TemplateType{
        final String ORDINARY = "ordinary";
    }

    public interface PageParam{
        final String PAGE_NUM = "pageNum";
        final String PAGE_SIZE = "pageSize";
        final int DEFAULT_SIZE = 10;
    }
}
