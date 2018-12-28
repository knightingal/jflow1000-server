package org.nanking.knightingal.bean;

import java.util.List;

public class SectionDetail {

    public SectionDetail(String dirName, int picPage, List<ImgDetail> pics) {
        this.dirName = dirName;
        this.picPage = picPage;
        this.pics = pics;
    }

    public SectionDetail() {
    }

    private Integer id;

    private String dirName;

    private int picPage;

    private List<ImgDetail> pics;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getPicPage() {
        return picPage;
    }

    public void setPicPage(int picPage) {
        this.picPage = picPage;
    }

    public List<ImgDetail> getPics() {
        return pics;
    }

    public void setPics(List<ImgDetail> pics) {
        this.pics = pics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SectionDetail{" +
                "id=" + id +
                ", dirName='" + dirName + '\'' +
                ", picPage=" + picPage +
                ", pics=" + pics +
                '}';
    }
}
