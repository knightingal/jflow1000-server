package org.nanking.knightingal.bean;

public class Flow1000Section {

    public Flow1000Section() {
    }

    public Flow1000Section(int id, String name, String dirName, String createTime, String cover, String album) {
        this.id = id;
        this.name = name;
        this.dirName = dirName;
        this.createTime = createTime;
        this.cover = cover;
        this.album = album;
    }

    private int id;

    private String name;

    private String dirName;

    private String createTime;

    private String cover;

    private String album;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
