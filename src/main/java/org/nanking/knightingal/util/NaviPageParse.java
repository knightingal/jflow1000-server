package org.nanking.knightingal.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nanking.knightingal.ship.Ship;
import org.nanking.knightingal.ship.ShipImgDetail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NaviPageParse {
    public static Ship parsePage(String naviPagePath) throws IOException {
        Ship.ShipBuilder shipBuilder = new Ship.ShipBuilder();
        File pageFile = new File(naviPagePath);

        Document doc = Jsoup.parse(pageFile);
        shipBuilder
                .shipType(0)
                .shipImgDetails(new ArrayList<>())
                .originHtmlFileName(pageFile.getName());
        Elements shipNames = doc.selectXpath("//h1");
        if (shipNames.size() >= 2) {
            String shipName = shipNames.get(1).text();
            System.out.println(shipName);
            shipBuilder.shipName(shipName);
        }
        Ship ship = shipBuilder.build();



        Elements imgTrs = doc.selectXpath("//th[text()='Image Description'][1]/../..//img/../../..");
        for (int i = 0; i < imgTrs.size(); i++) {
            ShipImgDetail.ShipImgDetailBuilder shipImgDetailBuilder = new ShipImgDetail.ShipImgDetailBuilder();
            shipImgDetailBuilder.ship(ship);

            System.out.println(imgTrs.get(i).tag());
            Element element = imgTrs.get(i);

            Elements tdElements = element.selectXpath("./td");
            if (tdElements.size() < 4) {
                continue;
            }

            Elements aElements = element.selectXpath(".//img/..");
            if (aElements.size() == 1) {
                if (!aElements.get(0).hasAttr("href")) {
                    continue;
                }
                String hrefValue = aElements.get(0).attribute("href").getValue();
                System.out.println(hrefValue);
                shipImgDetailBuilder.imgUrl(hrefValue);
            }

            Elements td3 = element.selectXpath("./td[3]");
            if (td3.size() == 1) {
                System.out.println(td3.html());
                shipImgDetailBuilder.imgDescription(td3.html());
            }
            Elements td4 = element.selectXpath("./td[4]");
            if (td4.size() == 1) {
                System.out.println(td4.html());
                shipImgDetailBuilder.source(td4.html());
            }
            ship.getShipImgDetails().add(shipImgDetailBuilder.build());
        }

        return ship;
    }

}
