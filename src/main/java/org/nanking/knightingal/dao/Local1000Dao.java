package org.nanking.knightingal.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.bean.Flow1000Section;

import java.util.List;

@Mapper
public interface Local1000Dao {
    Flow1000Section queryFlow1000SectionById(int id);

    List<Flow1000Img> queryFlow1000ImgBySectionId(int sectionId);

    List<Flow1000Section> queryFlow1000SectionByCreateTime(String timeStamp);

    void insertFlow1000Section(Flow1000Section flow1000Section);

    void insertFlow1000Img(List<Flow1000Img> flow1000ImgList);

    void updateFlow1000Img(Flow1000Img flow1000Img);

    void deleteFlow1000SectionById(int id);

    void deleteFlow1000ImgBySectionId(int sectionId);
}