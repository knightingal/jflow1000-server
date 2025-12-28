package org.nanking.knightingal.dao;

import org.nanking.knightingal.annotation.Repo;
import org.nanking.knightingal.bean.ApkConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

@Repo("local1000ApkConfigRepo")
public interface Local1000ApkConfigDao {

  Optional<ApkConfig> findOne(@Nullable Specification<ApkConfig> spec);

  List<ApkConfig> findAll(@Nullable Specification<ApkConfig> spec);

  Page<ApkConfig> findAll(Specification<ApkConfig> spec, Pageable pageable);

  ApkConfig saveAndFlush(ApkConfig entity);
}
