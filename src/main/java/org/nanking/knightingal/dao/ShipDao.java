package org.nanking.knightingal.dao;

import org.nanking.knightingal.annotation.Repo;
import org.nanking.knightingal.ship.Ship;

import java.util.List;

@Repo("shipRepo")
public interface ShipDao {
    Ship saveAndFlush(Ship ship);

    List<Ship> findAll();
}
