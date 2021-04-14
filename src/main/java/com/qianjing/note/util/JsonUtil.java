package com.qianjing.note.util;

import com.qianjing.note.pojo.Note;

public class JsonUtil {

    public static String objToJson(Note note){
        StringBuilder sb = new StringBuilder();
        sb.append("{'noteId':'").append(note.getId()).append("'")
            .append(",").append("'noteName':'").append(note.getNoteName())
        .append("'}");
        return sb.toString();
    }

}
