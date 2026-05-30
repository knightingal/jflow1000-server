package org.nanking.knightingal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.nanking.knightingal.bean.ApkConfig;
import org.nanking.knightingal.dao.Local1000ApkConfigDao;
import org.nanking.knightingal.util.QrCodeUtil;
import org.nanking.knightingal.vo.ApkConfigVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.zxing.WriterException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web controller that serves HTML pages for APK browsing and QR code generation.
 */
@Controller
@RequestMapping("/web")
public class WebController {

  private final Local1000ApkConfigDao local1000ApkConfigDao;

  @Value("${apk.download.url.prefix}")
  private String apkDownloadUrlPrefix;

  /** Constructs a WebController with the given APK config DAO. */
  public WebController(Local1000ApkConfigDao local1000ApkConfigDao) {
    this.local1000ApkConfigDao = local1000ApkConfigDao;
  }

  /** Renders the index page with all APK packages sorted by version code in descending order. */
  @GetMapping("/index")
  public String index(Model model) {

    List<ApkConfig> one = local1000ApkConfigDao.findAll((Specification<ApkConfig>) (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Order versionCode = builder.desc(root.get("versionCode"));
      return query
          .orderBy(versionCode)
          .where(predicates.toArray(new Predicate[] {}))
          .getRestriction();
    });

    List<ApkConfigVO> apkConfigVo = one.stream().map(apkConfig -> new ApkConfigVO(
        apkConfig.getApplicationId(),
        apkConfig.getVersionCode(),
        apkConfig.getVersionName(),
        apkConfig.getApkName(),
        apkDownloadUrlPrefix + toApkVersionedName(apkConfig))).toList();
    model.addAttribute("apkConfigVoList", apkConfigVo);

    return "index";
  }

  private String toApkVersionedName(ApkConfig apkConfig) {
    return apkConfig.getApplicationId() + "_" + apkConfig.getVersionCode() + "_" + apkConfig.getVersionName() + ".apk";
  }

  /** Generates and returns a QR code PNG image for the given content string. */
  @GetMapping("/qr")
  public void qrImage(@Param("content") String content, HttpServletResponse httpServletResponse)
      throws IOException, WriterException {
    httpServletResponse.addHeader("content-type", "image/png");
    QrCodeUtil.generateQrCode(content, httpServletResponse.getOutputStream());
  }

}
