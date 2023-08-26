package org.nanking.knightingal.dao;

import org.nanking.knightingal.annotation.Repo;
import org.nanking.knightingal.bean.AlbumConfig;

import java.util.List;

@Repo("local1000AlbumConfigRepo")
public interface Local1000AlbumConfigDao {

    List<AlbumConfig> findAll();
}
