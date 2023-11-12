package org.nanking.knightingal.bean;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Knightingal
 */
@Getter
@Builder
public class PicIndex {

    private long index;

    private String name;

    private String mtime;

    private String cover;

    private int coverWidth;

    private int coverHeight;

    private String album;

    private ClientStatus clientStatus;
    public static enum ClientStatus {
        NONE,
        PENDING,
        LOCAL
    }
}
