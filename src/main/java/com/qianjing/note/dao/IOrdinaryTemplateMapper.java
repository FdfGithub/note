package com.qianjing.note.dao;


import com.qianjing.note.pojo.template.OrdinaryTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("ordinaryTemplateMapper")
public interface IOrdinaryTemplateMapper {

    boolean insertTemplate(OrdinaryTemplate template);


    OrdinaryTemplate selectTemplate(Long id);


    boolean updateTemplate(OrdinaryTemplate template);


    boolean deleteTemplate(Long id);


    boolean updateAllStatus();
}
