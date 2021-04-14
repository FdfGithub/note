package com.qianjing.note.vo;

public class NoteInfo {
    private Long noteId;
    private String noteName;
    private String noteCreateTime;
    private String noteUpdateTime;

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

    public String getNoteCreateTime() {
        return noteCreateTime;
    }

    public void setNoteCreateTime(String noteCreateTime) {
        this.noteCreateTime = noteCreateTime;
    }

    public String getNoteUpdateTime() {
        return noteUpdateTime;
    }

    public void setNoteUpdateTime(String noteUpdateTime) {
        this.noteUpdateTime = noteUpdateTime;
    }

}
