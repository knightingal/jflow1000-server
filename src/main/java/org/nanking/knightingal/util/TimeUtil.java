package org.nanking.knightingal.util;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Knightingal
 */
public class TimeUtil {

    @Autowired
    private DateFormat fmt;

    public String timeStamp() {
        return fmt.format(new Date());
    }
}
