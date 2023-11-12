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

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCoverWidth(int coverWidth) {
        this.coverWidth = coverWidth;
    }

    public void setCoverHeight(int coverHeight) {
        this.coverHeight = coverHeight;
    }

    public void setClientStatus(ClientStatus clientStatus) {
        this.clientStatus = clientStatus;
    }

    public static enum ClientStatus {
        NONE,
        PENDING,
        LOCAL
    }
}
