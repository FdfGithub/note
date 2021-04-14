package com.qianjing.note.vo;

public class NoteInfoByOrdinaryTemplateVO extends NoteInfo {

    private String describe;
    private String status;
    private String noteStartTime;
    private String noteStartDate;


    public String getNoteStartDate() {
        return noteStartDate;
    }

    public void setNoteStartDate(String noteStartDate) {
        this.noteStartDate = noteStartDate;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoteStartTime() {
        return noteStartTime;
    }

    public void setNoteStartTime(String noteStartTime) {
        this.noteStartTime = noteStartTime;
    }


    @Override
    public String toString() {
        return "NoteInfoByOrdinaryTemplateVO{" +
                "noteId=" + getNoteId() +
                ", noteName='" + getNoteName() + '\'' +
                ", describe='" + describe + '\'' +
                ", status=" + status +
                ", noteStartTime='" + noteStartTime + '\'' +
                ", noteCreateTime='" + getNoteCreateTime() + '\'' +
                ", noteUpdateTime='" + getNoteUpdateTime() + '\'' +
                '}';
    }
}
