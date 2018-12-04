package org.nanking.knightingal.controller;

import org.nanking.knightingal.bean.Flow1000Section;
import org.nanking.knightingal.bean.SectionDetail;
import org.nanking.knightingal.dao.Local1000Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/local1000")
@RestController
public class Local1000Controller {

    @Autowired
    private Local1000Dao local1000Dao;

    @RequestMapping("/picDetailAjax")
    public SectionDetail picDetailAjax(@RequestParam(value = "id", defaultValue = "1") int id) {
        Flow1000Section flow1000Section = local1000Dao.queryFlow1000SectionById(3);
        return new SectionDetail("1", "1");
    }
}
