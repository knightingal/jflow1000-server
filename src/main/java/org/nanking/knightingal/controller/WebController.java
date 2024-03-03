package org.nanking.knightingal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/web")
public class WebController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }
    
}
