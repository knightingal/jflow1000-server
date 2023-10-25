package org.nanking.knightingal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequestMapping("/apkConfig")
@RestController
public class ApkConfigController {

    @Value("${apk.filepath.base}")
    private String apkFilePathBase;
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadPackage(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath = apkFilePathBase + fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
