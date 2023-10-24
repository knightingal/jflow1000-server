package org.nanking.knightingal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RequestMapping("/apkConfig")
@RestController
public class ApkConfigController {

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadPackage(File apk) {
        return ResponseEntity.ok().build();
    }
}
