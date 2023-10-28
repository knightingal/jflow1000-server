package org.nanking.knightingal.controller;

//import com.android.ide.common.process.ProcessException;
//import com.android.tools.apk.analyzer.AaptInvoker;
//import com.android.tools.apk.analyzer.AndroidApplicationInfo;
//import com.android.tools.apk.analyzer.ApkAnalyzerCli;
import lombok.extern.slf4j.Slf4j;
import org.nanking.knightingal.bean.ApkConfig;
import org.nanking.knightingal.dao.Local1000ApkConfigDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequestMapping("/apkConfig")
@RestController
@Slf4j
public class ApkConfigController {

    private static final Pattern packagePattern = Pattern.compile("package: name='(.*)' versionCode='(.*)' versionName='(.*)' platformBuildVersionName='(.*)'");
    final private Local1000ApkConfigDao local1000ApkConfigDao;

    @Value("${apk.filepath.base}")
    private String apkFilePathBase;

    @Value("${apk.filepath.aapt.path}")
    private String aaptPath;

    public ApkConfigController(Local1000ApkConfigDao local1000ApkConfigDao) {
        this.local1000ApkConfigDao = local1000ApkConfigDao;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadPackage(@RequestParam("file") MultipartFile file) {
        String fileName = new File(Objects.requireNonNull(file.getOriginalFilename())).getName();
        String filePath = apkFilePathBase + fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);

            Process exec = Runtime.getRuntime().exec(new String[]{aaptPath, "dump", "badging", dest.getAbsolutePath()});
            List<String> dumpBadgings = new BufferedReader(
                    new InputStreamReader(exec.getInputStream()
            )).lines().filter(line -> line.startsWith("package:")).toList();

            String packageId = "";
            Long versionCode = null;
            String versionName = "";

            boolean parseSucc = false;

            if (!dumpBadgings.isEmpty()) {
                String line = dumpBadgings.get(0);
                Matcher matcher = packagePattern.matcher(line);
                if (matcher.matches()) {
                    packageId = matcher.group(1);
                    versionCode = Long.parseLong(matcher.group(2));
                    versionName = matcher.group(3);
                    System.out.println("packageId=" + packageId + ", versionCode=" + versionCode + ", versionName=" + versionName);
                    parseSucc = true;
                }
            }
            if (!parseSucc) {
                throw new Exception("failed to parse apk file");
            }
            local1000ApkConfigDao.saveAndFlush(ApkConfig.builder()
                    .apkName(fileName).applicationId(packageId).versionName(versionName).versionCode(versionCode)
                    .uploadTime(new Date())
                    .build());

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
//        } catch (ProcessException e) {
//            throw new RuntimeException(e);
        }
    }
}
