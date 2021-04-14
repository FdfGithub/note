package com.qianjing.note.pojo;



import com.qianjing.note.pojo.template.OrdinaryTemplate;

import java.io.Serializable;
import java.util.Date;

public class Note{
    private Long id;
    private Long templateId;
    private String templateName;
    private String noteName;
    private Date createTime;
    private Date updateTime;
    private Long userId;
    private OrdinaryTemplate ordinaryTemplate;

    public Note(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Note() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrdinaryTemplate getOrdinaryTemplate() {
        return ordinaryTemplate;
    }

    public void setOrdinaryTemplate(OrdinaryTemplate ordinaryTemplate) {
        this.ordinaryTemplate = ordinaryTemplate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
