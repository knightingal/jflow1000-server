package org.nanking.knightingal.bean;

import java.util.List;

/**
 * @author Knightingal
 */
public class SectionDetail {

    public SectionDetail(Long id, String dirName, Long picPage, List<ImgDetail> pics, String album, String title, String mtime) {
        this.dirName = dirName;
        this.picPage = picPage;
        this.pics = pics;
        this.album = album;
        this.id = id;
        this.title = title;
        this.mtime = mtime;
        
    }

    public SectionDetail() {
    }

    private Long id;

    private String dirName;

    private Long picPage;

    private List<ImgDetail> pics;

    private String album;

    private String title;

    private String mtime;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public Long getPicPage() {
        return picPage;
    }

    public void setPicPage(Long picPage) {
        this.picPage = picPage;
    }

    public List<ImgDetail> getPics() {
        return pics;
    }

    public void setPics(List<ImgDetail> pics) {
        this.pics = pics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
      return title;
    }

    public String getMtime() {
      return mtime;
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
