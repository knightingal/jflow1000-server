package org.nanking.knightingal.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Knightingal
 */
public class FileUtil {

    public String getFileNameByUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return new File(url.getFile()).getName();
        } catch (MalformedURLException e) {
            return null;
        }

    }
}
