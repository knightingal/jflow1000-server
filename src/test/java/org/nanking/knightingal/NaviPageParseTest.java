package org.nanking.knightingal;

import org.junit.jupiter.api.Test;
import org.nanking.knightingal.ship.Ship;
import org.nanking.knightingal.util.NaviPageParse;

import java.net.MalformedURLException;

public class NaviPageParseTest {

    @Test
    public void testParsePage() {
        try {
            Ship ship = NaviPageParse.parsePage("/home/knightingal/Documents/Battleship Photo Index BB-47 WASHINGTON.htm");
            ship.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseImgUrl() throws MalformedURLException {
        String[] strings = NaviPageParse.parseImgUrl("http://navsource.net/archives/01/010/011060.jpg");
        strings.toString();
    }
}
