package org.nanking.knightingal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/web")
public class WebController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "knightingal");
        return "index";
    }

    @GetMapping("/qr")
    public void qrImage(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.addHeader("content-type", "image/png");
        File qrFile = new File("./qrcode.png");
        InputStream fileIs = new FileInputStream(qrFile);
        while (true) {
            byte[] buffer = new byte[1024];
            int readLen = fileIs.read(buffer);
            if (readLen <= 0) {
                break;
            }
            httpServletResponse.getOutputStream().write(buffer, 0, readLen);

        }
        fileIs.close();
    }
    
}
