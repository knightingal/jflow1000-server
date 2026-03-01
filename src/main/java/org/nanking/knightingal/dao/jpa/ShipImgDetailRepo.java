package org.nanking.knightingal.dao.jpa;

import org.nanking.knightingal.ship.ShipImgDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShipImgDetailRepo extends JpaRepository<ShipImgDetail, Long>, JpaSpecificationExecutor<ShipImgDetail> {
}
