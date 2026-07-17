package org.nanking.knightingal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanking.knightingal.dao.ShipDao;
import org.nanking.knightingal.dao.ShipImgDetailDao;
import org.nanking.knightingal.dao.jpa.ShipImgDetailRepo;
import org.nanking.knightingal.runnable.ShipDownloadRunnable;
import org.nanking.knightingal.ship.Ship;
import org.nanking.knightingal.ship.ShipImgDetail;
import org.nanking.knightingal.util.AvifUtil;
import org.nanking.knightingal.util.NaviPageParse;
import org.nanking.knightingal.util.WebpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.nanking.knightingal.controller.Local1000Controller.WEBP_SUFFIX;

/**
 * Controller for importing ship data from HTML pages and batch-downloading associated images.
 */
@RequestMapping("/ship")
@RestController
public class ShipController {
    private static final Logger LOG = LogManager.getLogger(ShipController.class);

    @Value("${ship.base.path}")
    public String shipBasePath;

    /** Constructs a ShipController with the given ship and ship image detail DAOs. */
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

    @GetMapping("/parseShipSize")
    public ResponseEntity<?> parseShipSize() {
        List<ShipImgDetail> allShipImgDetail = shipImgDetailRepo.searchShipImgDetailByFileStatus(1);
        for (ShipImgDetail shipImgDetail: allShipImgDetail) {
            try {
                String[] imgPaths = NaviPageParse.parseImgUrl(shipImgDetail.getImgUrl());
                File targetPath = Paths.get(shipBasePath, imgPaths[0]).toFile();
                Path targetFilePath = Paths.get(shipBasePath, imgPaths[0], imgPaths[1]);
                if (!targetFilePath.toFile().exists()) {
                    throw new Exception("File not found: " + targetFilePath.toString());
                } else {
                    LOG.info("parseShipSize, file {} exist", targetFilePath.toString());
                    int width;
                    int height;

                    try {
                        if (targetFilePath.getFileName().endsWith(WEBP_SUFFIX)) {
                            InputStream fileInputStream = new FileInputStream(targetFilePath.toFile());
                            WebpUtil.WebpImageSize webpImageSize = WebpUtil.parseWebpImage(fileInputStream);
                            fileInputStream.close();
                            width = webpImageSize.width;
                            height = webpImageSize.height;
                        } else if (targetFilePath.getFileName().endsWith(".avif")) {
                            AvifUtil.ImgSize imgSize = AvifUtil.parseImgSize(targetFilePath.toFile());
                            width = imgSize.getWidth();
                            height = imgSize.getHeight();
                        } else {
                            BufferedImage sourceImg = ImageIO.read(Files.newInputStream(targetFilePath));
                            width = sourceImg.getWidth();
                            height = sourceImg.getHeight();
                        }
                        shipImgDetail.setHeight(height);
                        shipImgDetail.setWidth(width);
                        shipImgDetail.setFileStatus(3);

                        LOG.info("width:{}, height:{} ", width, height);
                        shipImgDetailDao.saveAndFlush(shipImgDetail);
                    } catch (Exception e) {
                        LOG.error("parse {} failed", targetFilePath.toString(), e);
                    }
                }
            } catch (Exception e) {
                LOG.error(e);
            }

        }

        return ResponseEntity.ok().build();
    }

    /** Downloads images for all pending ship image details using a thread pool, skipping PDFs and already-existing files. */
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

    /** Parses all HTM files in the Documents directory and imports each as a ship entity into the database. */
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
