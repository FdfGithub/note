package com.qianjing.note.vo;

import java.util.Map;

public class HistoryVO {
    private String date;//几年几月几号
    private Map<String,String> map;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }


    @Override
    public String toString() {
        return "HistoryVO{" +
                "date='" + date + '\'' +
                ", map=" + map +
                '}';
    }
}
