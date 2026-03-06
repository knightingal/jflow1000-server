package org.nanking.knightingal.dao.jpa;

import org.nanking.knightingal.ship.ShipImgDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ShipImgDetailRepo extends JpaRepository<ShipImgDetail, Long>, JpaSpecificationExecutor<ShipImgDetail> {

    List<ShipImgDetail> searchShipImgDetailByFileStatus(int fileStatus);

}
