package org.nanking.knightingal.controller;

import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.bean.Flow1000Section;
import org.nanking.knightingal.bean.ImgDetail;
import org.nanking.knightingal.bean.SectionDetail;
import org.nanking.knightingal.dao.Local1000Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/local1000")
@RestController
public class Local1000Controller {

    @Autowired
    private Local1000Dao local1000Dao;

    @RequestMapping("/picDetailAjax")
    public SectionDetail picDetailAjax(@RequestParam(value = "id", defaultValue = "1") int id) {
        Flow1000Section flow1000Section = local1000Dao.queryFlow1000SectionById(id);
        List<Flow1000Img> flow1000ImgList = local1000Dao.queryFlow1000ImgBySectionId(id);
        List<ImgDetail> imgDetailList = new ArrayList<>();
        for (Flow1000Img flow1000Img : flow1000ImgList) {
            imgDetailList.add(new ImgDetail(
                    flow1000Img.getName(),
                    flow1000Img.getWidth(),
                    flow1000Img.getHeight())
            );
        }

        return new SectionDetail(flow1000Section.getDirName(), flow1000Section.getId(), imgDetailList);
    }
}
