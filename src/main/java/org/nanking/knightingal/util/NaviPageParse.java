package org.nanking.knightingal.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class NaviPageParse {
    public static void parsePage(String naviPagePath) throws IOException {
        Document doc = Jsoup.parse(new File(naviPagePath));
        Elements elements = doc.selectXpath("//th[text()='Image Description'][1]/../..//img/../../..");


        for (int i = 0; i < elements.size(); i++) {
            System.out.println(elements.get(i).tag());
            Element element = elements.get(i);
            Elements aElements = element.selectXpath(".//img/..");
            if (aElements.size() == 1) {
                String hrefValue = aElements.get(0).attribute("href").getValue();
                System.out.println(hrefValue);
            }

            Elements td3 = element.selectXpath("./td[3]");
            if (td3.size() == 1) {
                System.out.println(td3.html());
            }
            Elements td4 = element.selectXpath("./td[4]");
            if (td4.size() == 1) {
                System.out.println(td4.html());
            }
        }

    }
}
