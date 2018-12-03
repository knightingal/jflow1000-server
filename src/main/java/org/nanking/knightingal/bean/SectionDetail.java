package org.nanking.knightingal.bean;

public class SectionDetail {

    public SectionDetail(String dirName, String picPage) {
        this.dirName = dirName;
        this.picPage = picPage;
    }

    private String dirName;

    private String picPage;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getPicPage() {
        return picPage;
    }

    public void setPicPage(String picPage) {
        this.picPage = picPage;
    }
}
