package org.nanking.knightingal.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Knightingal
 */
public class FileUtil {

    public String getFileNameByUrl(String urlString)  {
        try {
            URL url = new URI(urlString).toURL();
            return new File(url.getFile()).getName();
        } catch (MalformedURLException | URISyntaxException e) {
            return null;
        }

    }
}
