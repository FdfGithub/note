package com.qianjing.note.vo;

import com.google.common.collect.Sets;

import java.util.Set;

public class NotesVO {
    private Set<NoteInfoByOrdinaryTemplateVO> ordinaryNotes = Sets.newHashSet();

    public Set<NoteInfoByOrdinaryTemplateVO> getOrdinaryNotes() {
        return ordinaryNotes;
    }

    public void setOrdinaryNotes(Set<NoteInfoByOrdinaryTemplateVO> ordinaryNotes) {
        this.ordinaryNotes = ordinaryNotes;
    }
}
