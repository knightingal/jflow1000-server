package org.nanking.knightingal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanking.knightingal.dao.ShipDao;
import org.nanking.knightingal.ship.Ship;
import org.nanking.knightingal.util.NaviPageParse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FilenameFilter;

@RequestMapping("/ship")
@RestController
public class ShipController {
    private static final Logger LOG = LogManager.getLogger(DevController.class);

    public ShipController(ShipDao shipDao) {
        this.shipDao = shipDao;
    }

    private ShipDao shipDao;

    @GetMapping("/import")
    public ResponseEntity<?> importShip() {
        try {
            File file = new File("/home/knightingal/Documents/");
            String[] htmFileArray = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".htm");
                }
            });

            for (String htm: htmFileArray) {
                try {
                    Ship ship = NaviPageParse.parsePage("/home/knightingal/Documents/" + htm);
                    ship = shipDao.saveAndFlush(ship);
                } catch (Exception e) {
                    LOG.error("cannot parse page:{}, ", htm, e);
                    throw e;
                }
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
