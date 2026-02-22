package org.nanking.knightingal.controller;

import org.nanking.knightingal.dao.ShipDao;
import org.nanking.knightingal.ship.Ship;
import org.nanking.knightingal.util.NaviPageParse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ship")
@RestController
public class ShipController {

    public ShipController(ShipDao shipDao) {
        this.shipDao = shipDao;
    }

    private ShipDao shipDao;

    @GetMapping("/import")
    public ResponseEntity<?> importShip() {
        try {
            Ship ship = NaviPageParse.parsePage("/home/knightingal/Documents/Battleship Photo Index BB-7 USS ILLINOIS.htm");
            ship = shipDao.saveAndFlush(ship);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
