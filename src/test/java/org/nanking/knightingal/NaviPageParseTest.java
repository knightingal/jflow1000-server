package org.nanking.knightingal;

import org.junit.jupiter.api.Test;
import org.nanking.knightingal.util.NaviPageParse;

public class NaviPageParseTest {

    @Test
    public void testParsePage() {
        try {
            NaviPageParse.parsePage("/home/knightingal/Documents/Battleship Photo Index BB-7 USS ILLINOIS.htm");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
