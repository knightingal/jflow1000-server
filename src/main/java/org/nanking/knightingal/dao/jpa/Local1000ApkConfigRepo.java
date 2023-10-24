package org.nanking.knightingal.dao.jpa;

import org.nanking.knightingal.bean.AlbumConfig;
import org.nanking.knightingal.bean.ApkConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Local1000ApkConfigRepo extends JpaRepository<ApkConfig, Long>, JpaSpecificationExecutor<ApkConfig> {
}
