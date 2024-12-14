package org.nanking.knightingal.controller;

import org.nanking.knightingal.service.WsMsgService;
import org.nanking.knightingal.util.ApplicationContextProvider;
import org.nanking.knightingal.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;


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

    @GetMapping("/aes-test")
    public void aesTestHandle(HttpServletResponse servletResponse) throws Exception {
      EncryptUtil encryptUtil = (EncryptUtil) ApplicationContextProvider.getBean("encryptUtil");
      servletResponse.addHeader("access-control-allow-origin", "*");
      servletResponse.getOutputStream().write(encryptUtil.encrypt("hello".getBytes()));
    }

    @RequestMapping("/send-msg")
    public String sendMsg(@RequestParam(name = "msg", defaultValue = "") String msg) {
        wsMsgService.sendWsMsg(msg);

        return "";
    }
}
