package org.nanking.knightingal.vo;

public class ApkConfigVO {
  private String applicationId;

  private Long versionCode;

  private String versionName;

  private String apkName;

  private String downloadUrl;

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public Long getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(Long versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  public String getApkName() {
    return apkName;
  }

  public void setApkName(String apkName) {
    this.apkName = apkName;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public ApkConfigVO(String applicationId, Long versionCode, String versionName, String apkName, String downloadUrl) {
    this.applicationId = applicationId;
    this.versionCode = versionCode;
    this.versionName = versionName;
    this.apkName = apkName;
    this.downloadUrl = downloadUrl;
  }
}
