package org.nanking.knightingal.controller;

import org.nanking.knightingal.bean.PicDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/local1000")
@RestController
public class Local1000Controller {

    @RequestMapping("/picDetailAjax")
    public PicDetail picDetailAjax(@RequestParam(value = "id", defaultValue = "1") int id) {
        return new PicDetail(id);
    }
}
