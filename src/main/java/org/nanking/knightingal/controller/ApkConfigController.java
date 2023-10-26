package org.nanking.knightingal.controller;

import com.android.ide.common.process.ProcessException;
import com.android.tools.apk.analyzer.AaptInvoker;
import com.android.tools.apk.analyzer.AndroidApplicationInfo;
import com.android.tools.apk.analyzer.ApkAnalyzerCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequestMapping("/apkConfig")
@RestController
@Slf4j
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

            AaptInvoker aaptInvoker = ApkAnalyzerCli.getAaptInvokerFromSdk("/home/knightingal/Android/Sdk");
            List<String> dumpBadgings = aaptInvoker.dumpBadging(dest);

            AndroidApplicationInfo apkInfo = AndroidApplicationInfo.parseBadging(dumpBadgings);
            log.debug("versionName:{}" , apkInfo.versionName);
            log.debug("versionCode:{}" , apkInfo.versionCode);
            log.debug("packageId:{}" , apkInfo.packageId);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (ProcessException e) {
            throw new RuntimeException(e);
        }
    }
}
