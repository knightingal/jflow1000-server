package org.nanking.knightingal.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nanking.knightingal.bean.Flow1000Section;

@Mapper
public interface Local1000Dao {
    Flow1000Section queryFlow1000SectionById(int id);
}
