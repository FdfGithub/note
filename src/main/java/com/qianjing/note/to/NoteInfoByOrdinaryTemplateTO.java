package com.qianjing.note.to;

import java.util.Date;

public class NoteInfoByOrdinaryTemplateTO {
    private Long noteId;
    private String noteName;
    private String describe;
    private String startTime;//此刻开始

/*
    private Integer status;//startTime有个选项为此刻开始，那么status就应该为DOING，而不是NO_START
*/

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /*    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }*/
}
