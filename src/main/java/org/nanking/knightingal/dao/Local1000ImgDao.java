package org.nanking.knightingal.dao;


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


    /**
     * 根据section id，删除section下的img记录
     * @param sectionId section id
     */
    void deleteById(Long sectionId);
}
