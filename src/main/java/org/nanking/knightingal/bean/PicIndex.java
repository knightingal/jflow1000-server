package org.nanking.knightingal.bean;

/**
 * @author Knightingal
 */
public class PicIndex {

    public PicIndex() {
    }

    public PicIndex(int index, String name, String mtime, String cover, int coverWidth, int coverHeight) {
        this.index = index;
        this.name = name;
        this.mtime = mtime;
        this.cover = cover;
        this.coverWidth = coverWidth;
        this.coverHeight = coverHeight;
    }

    private int index;

    private String name;

    private String mtime;

    private String cover;

    private int coverWidth;

    private int coverHeight;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getCoverWidth() {
        return coverWidth;
    }

    public void setCoverWidth(int coverWidth) {
        this.coverWidth = coverWidth;
    }

    public int getCoverHeight() {
        return coverHeight;
    }

    public void setCoverHeight(int coverHeight) {
        this.coverHeight = coverHeight;
    }
}
