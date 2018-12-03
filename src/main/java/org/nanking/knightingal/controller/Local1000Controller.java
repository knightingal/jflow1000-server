package org.nanking.knightingal.controller;

import org.nanking.knightingal.bean.SectionDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/local1000")
@RestController
public class Local1000Controller {

    @RequestMapping("/picDetailAjax")
    public SectionDetail picDetailAjax(@RequestParam(value = "id", defaultValue = "1") int id) {
        return new SectionDetail("1", "1");
    }
}
