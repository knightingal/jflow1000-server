package org.nanking.knightingal.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApkConfigVO {
    private String applicationId;

    private Long versionCode;

    private String versionName;

    private String apkName;

    private String downloadUrl;
}
