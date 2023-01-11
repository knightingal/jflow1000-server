package org.nanking.knightingal.dao.jpa;

import org.nanking.knightingal.bean.Flow1000Section;
import org.nanking.knightingal.dao.Local1000SectionDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Local1000SectionRepo extends Local1000SectionDao, JpaRepository<Flow1000Section, Integer> {
}
