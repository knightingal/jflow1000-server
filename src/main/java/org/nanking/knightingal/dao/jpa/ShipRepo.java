package org.nanking.knightingal.dao.jpa;

import org.nanking.knightingal.ship.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShipRepo extends JpaRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {
}
