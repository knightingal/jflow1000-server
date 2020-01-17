package org.nanking.knightingal.controller;

import org.nanking.knightingal.service.WsMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Knightingal
 */
@RestController
@RequestMapping("/dev")
public class DevController {

    @Autowired
    WsMsgService wsMsgService;

    @Value("${encryptUtil.passwd}")
    private String passwd;

    @RequestMapping("/passwd")
    public String getPasswd() {
        return passwd;
    }

    @RequestMapping("/send-msg")
    public String sendMsg(@RequestParam(name = "msg", defaultValue = "") String msg) {
        wsMsgService.sendWsMsg(msg);

        return "";
    }
}
