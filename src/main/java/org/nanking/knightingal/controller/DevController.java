package org.nanking.knightingal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev")
public class DevController {

    @Value("${encryptUtil.passwd}")
    private String passwd;

    @RequestMapping("/passwd")
    public String getPasswd() {
        return passwd;
    }
}
