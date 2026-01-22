package org.nanking.knightingal.dao;

import java.util.Optional;

import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.bean.Flow1000Section;

/**
 * @author Knightingal
 */
public interface Local1000ImgDao {

  Optional<Flow1000Img> searchFlow1000ImgByNameAndFlow1000Section(String name, Flow1000Section flow1000Section);

  /**
   * 根据section id，删除section下的img记录
   * 
   * @param sectionId section id
   */
  void deleteById(Long sectionId);

  Flow1000Img saveAndFlush(Flow1000Img entity);
}
