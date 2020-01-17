package org.nanking.knightingal.bean;

import java.util.List;

/**
 * @author Knightingal
 */
public class SectionContent {

    public SectionContent(String dirName, int picPage, List<String> pics) {
        this.dirName = dirName;
        this.picpage = picPage;
        this.pics = pics;
    }

    private String dirName;

    private int picpage;

    private List<String> pics;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getPicpage() {
        return picpage;
    }

    public void setPicpage(int picPage) {
        this.picpage = picPage;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
