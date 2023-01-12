package org.nanking.knightingal.dao.jpa;

import java.util.List;

import org.nanking.knightingal.bean.Flow1000Section;
import org.nanking.knightingal.dao.Local1000SectionDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Local1000SectionRepo extends Local1000SectionDao, JpaRepository<Flow1000Section, Integer>, JpaSpecificationExecutor<Flow1000Section> {

    @Query("select s from Flow1000Section s where s.id=:id")
    Flow1000Section queryFlow1000SectionById(@Param("id") int id);

    @Query("select s from Flow1000Section s where s.createTime > :timeStamp")
    List<Flow1000Section> queryFlow1000SectionByCreateTime(@Param("timeStamp") String timeStamp);


    // @Query("select s from Flow1000Section s where s.creatTime > :timeStamp")
    // List<Flow1000Section> queryFlow1000Section(Flow1000Section flow1000Section);

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
