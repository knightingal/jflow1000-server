package org.nanking.knightingal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanking.knightingal.dao.ShipDao;
import org.nanking.knightingal.dao.ShipImgDetailDao;
import org.nanking.knightingal.dao.jpa.ShipImgDetailRepo;
import org.nanking.knightingal.runnable.ShipDownloadRunnable;
import org.nanking.knightingal.ship.Ship;
import org.nanking.knightingal.ship.ShipImgDetail;
import org.nanking.knightingal.util.NaviPageParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequestMapping("/ship")
@RestController
public class ShipController {
    private static final Logger LOG = LogManager.getLogger(DevController.class);

    @Value("${ship.base.path}")
    public String shipBasePath;

    public ShipController(ShipDao shipDao, ShipImgDetailDao shipImgDetailDao) {
        this.shipDao = shipDao;
        this.shipImgDetailDao = shipImgDetailDao;
    }

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4,
            4,
            20, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    private ShipDao shipDao;
    private ShipImgDetailDao shipImgDetailDao;
    @Autowired
    private ShipImgDetailRepo shipImgDetailRepo;

    @GetMapping("/batchDownloadImg")
    public ResponseEntity<?> batchDownloadImg() {
        List<ShipImgDetail> allShipImgDetail = shipImgDetailRepo.searchShipImgDetailByFileStatus(0);
        for (ShipImgDetail shipImgDetail: allShipImgDetail) {
            try {
                String[] imgPaths = NaviPageParse.parseImgUrl(shipImgDetail.getImgUrl());
                LOG.info("process url:{}", shipImgDetail.getImgUrl());
                if (imgPaths[1].endsWith(".pdf")) {
                    LOG.info("skip pdf file");
                    shipImgDetail.setFileStatus(2);
                    shipImgDetailDao.saveAndFlush(shipImgDetail);
                    continue;
                }
                File targetPath = Paths.get(shipBasePath, imgPaths[0]).toFile();
                if (!targetPath.exists()) {
                    if (!targetPath.mkdirs()) {
                        throw new Exception("failed to create path:" + targetPath.getPath());
                    }
                }
                Path targetFilePath = Paths.get(shipBasePath, imgPaths[0], imgPaths[1]);
                if (!targetFilePath.toFile().exists()) {
                    threadPoolExecutor.submit(new ShipDownloadRunnable(shipImgDetailDao, shipImgDetail, targetFilePath.toString()));
                } else {
                    LOG.info("file {} exist", targetFilePath.toString());
                    shipImgDetail.setFileStatus(1);
                    shipImgDetailDao.saveAndFlush(shipImgDetail);
                }
            } catch (Exception e) {
                LOG.error(e);
            }
        }

        return ResponseEntity.ok().build();
    }

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
