package org.nanking.knightingal.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;

@Entity
public class ApkConfig implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicationId;

    private Long versionCode;

    private String versionName;

    private String apkName;

    private Date uploadTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public ApkConfig(String applicationId, Long versionCode, String versionName, Date uploadTime) {
        this.applicationId = applicationId;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.uploadTime = uploadTime;
    }

    public ApkConfig(Long id, String applicationId, Long versionCode, String versionName, String apkName, Date uploadTime) {
        this.id = id;
        this.applicationId = applicationId;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.apkName = apkName;
        this.uploadTime = uploadTime;
    }

    public ApkConfig() {
    }
}
