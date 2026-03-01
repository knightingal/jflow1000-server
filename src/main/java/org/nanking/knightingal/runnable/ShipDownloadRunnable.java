package org.nanking.knightingal.runnable;

import org.nanking.knightingal.dao.ShipImgDetailDao;
import org.nanking.knightingal.ship.ShipImgDetail;

public class ShipDownloadRunnable implements Runnable {

    private ShipImgDetailDao shipImgDetailDao;
    private ShipImgDetail shipImgDetail;

    public ShipDownloadRunnable(ShipImgDetailDao shipImgDetailDao, ShipImgDetail shipImgDetail) {
        super();
        this.shipImgDetailDao = shipImgDetailDao;
        this.shipImgDetail = shipImgDetail;
    }

    @Override
    public void run() {

    }
}
