package org.nanking.knightingal.dao.jpa;

import java.util.List;

import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.dao.Local1000ImgDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Local1000ImgRepo extends Local1000ImgDao, JpaRepository<Flow1000Img, Long>{

    // List<Flow1000Img> queryBySectionId(Long sectionId);
}
