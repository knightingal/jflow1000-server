package org.nanking.knightingal.bean;

import java.sql.Date;

public class PicIndex {

    public PicIndex() {
    }

    public PicIndex(int index, String name, Date mtime) {
        this.index = index;
        this.name = name;
        this.mtime = mtime;
    }

    private int index;

    private String name;

    private Date mtime;

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

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }
}
