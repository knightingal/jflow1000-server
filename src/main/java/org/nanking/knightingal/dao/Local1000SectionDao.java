package org.nanking.knightingal.dao;

import org.nanking.knightingal.bean.Flow1000Section;

import java.util.List;

/**
 * @author Knightingal
 */
public interface Local1000SectionDao {
    Flow1000Section queryFlow1000SectionById(int id);

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
     * 根据id删除section记录
     * @param id section id
     */
    void deleteFlow1000SectionById(int id);
}
