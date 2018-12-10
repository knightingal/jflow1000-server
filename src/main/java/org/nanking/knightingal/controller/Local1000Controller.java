package org.nanking.knightingal.controller;

import org.nanking.knightingal.bean.*;
import org.nanking.knightingal.dao.Local1000Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/picIndexAjax")
    public List<PicIndex> picIndexAjax(@RequestParam(value="time_stamp", defaultValue="19700101000000")String timeStamp) {
        List<Flow1000Section> flow1000SectionList = local1000Dao.queryFlow1000SectionByCreateTime(timeStamp);
        List<PicIndex> picIndexList = new ArrayList<>();

        for (Flow1000Section flow1000Section : flow1000SectionList) {
            picIndexList.add(new PicIndex(
                    flow1000Section.getId(),
                    flow1000Section.getDirName(),
                    flow1000Section.getCreateTime()
            ));
        }

        return picIndexList;
    }

    @RequestMapping(value="/urls1000", method={RequestMethod.POST})
    public void urls1000(@RequestBody Urls1000Body urls1000Body) {

    }
}
