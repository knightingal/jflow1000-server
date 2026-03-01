package org.nanking.knightingal.dao;

import org.nanking.knightingal.annotation.Repo;
import org.nanking.knightingal.ship.Ship;
import org.nanking.knightingal.ship.ShipImgDetail;

import java.util.List;

@Repo("shipImgDetailRepo")
public interface ShipImgDetailDao {
    ShipImgDetail saveAndFlush(ShipImgDetail shipImgDetail);

    List<ShipImgDetail> findAll();
}
