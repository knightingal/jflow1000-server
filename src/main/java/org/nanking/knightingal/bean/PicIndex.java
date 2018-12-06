package org.nanking.knightingal.bean;

public class PicIndex {

    public PicIndex() {
    }

    public PicIndex(int index, String name, String mtime) {
        this.index = index;
        this.name = name;
        this.mtime = mtime;
    }

    private int index;

    private String name;

    private String mtime;

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
}
