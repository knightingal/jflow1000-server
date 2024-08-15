package org.nanking.knightingal.dao;

import java.util.Optional;

import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.bean.Flow1000Section;

/**
 * @author Knightingal
 */
public interface Local1000ImgDao {

    // List<Flow1000Img> queryBySectionId(Long sectionId);

    // /**
    //  * img列表入库
    //  * @param flow1000ImgList img列表
    //  */
    // void insertFlow1000Img(List<Flow1000Img> flow1000ImgList);

    // /**
    //  * 更新数据库img信息
    //  * @param flow1000Img img
    //  */
    // void updateFlow1000Img(Flow1000Img flow1000Img);

    Optional<Flow1000Img> searchFlow1000ImgByNameAndFlow1000Section(String name, Flow1000Section flow1000Section);

    /**
     * 根据section id，删除section下的img记录
     * @param sectionId section id
     */
    void deleteById(Long sectionId);

    Flow1000Img saveAndFlush(Flow1000Img entity);
}
