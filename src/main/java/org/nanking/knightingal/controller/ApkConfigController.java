package org.nanking.knightingal.controller;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.nanking.knightingal.bean.ApkConfig;
import org.nanking.knightingal.dao.Local1000ApkConfigDao;
import org.nanking.knightingal.vo.ApkConfigVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Value("${apk.download.url.prefix}")
    private String apkDownloadUrlPrefix;

    public ApkConfigController(Local1000ApkConfigDao local1000ApkConfigDao) {
        this.local1000ApkConfigDao = local1000ApkConfigDao;
    }

    @GetMapping("/newest/package/{id}")
    public ResponseEntity<ApkConfigVO> newestPackage(@PathVariable("id") String packageId) {
        Page<ApkConfig> one = local1000ApkConfigDao.findAll((Specification<ApkConfig>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate packagePredicate = builder.equal(root.get("applicationId"), packageId);
            predicates.add(packagePredicate);
            Order versionCode = builder.desc(root.get("versionCode"));
            return query
                    .orderBy(versionCode)
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        }, Pageable.ofSize(1));
        if (one.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            ApkConfig apkConfig = one.getContent().get(0);
            ApkConfigVO vo = ApkConfigVO.builder()
                    .apkName(apkConfig.getApkName())
                    .applicationId(apkConfig.getApplicationId())
                    .versionCode(apkConfig.getVersionCode())
                    .versionName(apkConfig.getVersionName())
                    .downloadUrl(apkDownloadUrlPrefix + toApkVersionedName(apkConfig))
                    .build();
            return ResponseEntity.ok().body(vo);
        }
    }

    private String toApkVersionedName(ApkConfig apkConfig) {
        return apkConfig.getApplicationId() + "_" + apkConfig.getVersionCode() + "_" + apkConfig.getVersionName() + ".apk";
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadPackage(@RequestParam("file") MultipartFile file) {
        String fileName = new File(Objects.requireNonNull(file.getOriginalFilename())).getName();
        String filePath = apkFilePathBase + fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);

            Process exec = Runtime.getRuntime().exec(new String[]{aaptPath, "dump", "badging", dest.getAbsolutePath()});
            List<String> dumpBadgingList = new BufferedReader(
                    new InputStreamReader(exec.getInputStream()
            )).lines().filter(line -> line.startsWith("package:")).toList();

            String packageId = "";
            Long versionCode = null;
            String versionName = "";

            boolean parseSucc = false;

            if (!dumpBadgingList.isEmpty()) {
                String line = dumpBadgingList.get(0);
                Matcher matcher = packagePattern.matcher(line);
                if (matcher.matches()) {
                    packageId = matcher.group(1);
                    versionCode = Long.parseLong(matcher.group(2));
                    versionName = matcher.group(3);
                    log.info("packageId={}, versionCode={}, versionName={}", packageId, versionCode, versionName);
                    parseSucc = true;
                }
            }
            if (!parseSucc) {
                throw new Exception("failed to parse apk file");
            }
            ApkConfig apkConfig = ApkConfig.builder()
                    .applicationId(packageId).versionName(versionName).versionCode(versionCode)
                    .uploadTime(new Date())
                    .build();
            apkConfig.setApkName(toApkVersionedName(apkConfig));

            String destApkName = apkFilePathBase + toApkVersionedName(apkConfig);
            if (!dest.renameTo(new File(destApkName))) {
                throw new Exception("failed to rename apk to " + destApkName);
            }
            local1000ApkConfigDao.saveAndFlush(apkConfig);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
