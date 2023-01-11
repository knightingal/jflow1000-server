package org.nanking.knightingal.dao;

import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.bean.Flow1000Section;

import java.util.List;

/**
 * @author Knightingal
 */
public interface Local1000Dao {
    Flow1000Section queryFlow1000SectionById(int id);

    List<Flow1000Img> queryFlow1000ImgBySectionId(int sectionId);

    List<Flow1000Section> queryFlow1000SectionByCreateTime(String timeStamp);

    List<Flow1000Section> queryFlow1000Section(Flow1000Section flow1000Section);

    /**
     * 根据sectoinName模糊查询
     * @param name name参数，需已拼接%
     * 
     * @return 返回查询结果
     */
    List<Flow1000Section> searchFlow1000SectionByName(String name);

    void insertFlow1000Section(Flow1000Section flow1000Section);

    /**
     * img列表入库
     * @param flow1000ImgList img列表
     */
    void insertFlow1000Img(List<Flow1000Img> flow1000ImgList);

    /**
     * 更新数据库img信息
     * @param flow1000Img img
     */
    void updateFlow1000Img(Flow1000Img flow1000Img);

    /**
     * 根据id删除section记录
     * @param id section id
     */
    void deleteFlow1000SectionById(int id);

    /**
     * 根据section id，删除section下的img记录
     * @param sectionId section id
     */
    void deleteFlow1000ImgBySectionId(int sectionId);
}
