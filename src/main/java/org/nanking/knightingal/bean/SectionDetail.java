package org.nanking.knightingal.bean;

import java.util.List;

/**
 * @author Knightingal
 */
public class SectionDetail {

  private Long id;

  private String dirName;

  private Long picPage;

  private List<ImgDetail> pics;

  private String album;

  private String title;

  private String mtime;

  private String clientStatus;

  public String getClientStatus() {
    return clientStatus;
  }

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

  public static final class SectionDetailBuilder {
    private Long id;
    private String dirName;
    private Long picPage;
    private List<ImgDetail> pics;
    private String album;
    private String title;
    private String mtime;
    private String clientStatus;

    private SectionDetailBuilder() {
    }

    public static SectionDetailBuilder instance() {
      return new SectionDetailBuilder();
    }

    public SectionDetailBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public SectionDetailBuilder dirName(String dirName) {
      this.dirName = dirName;
      return this;
    }

    public SectionDetailBuilder picPage(Long picPage) {
      this.picPage = picPage;
      return this;
    }

    public SectionDetailBuilder pics(List<ImgDetail> pics) {
      this.pics = pics;
      return this;
    }

    public SectionDetailBuilder album(String album) {
      this.album = album;
      return this;
    }

    public SectionDetailBuilder title(String title) {
      this.title = title;
      return this;
    }

    public SectionDetailBuilder mtime(String mtime) {
      this.mtime = mtime;
      return this;
    }

    public SectionDetailBuilder clientStatus(String clientStatus) {
      this.clientStatus = clientStatus;
      return this;
    }

    public SectionDetail build() {
      SectionDetail sectionDetail = new SectionDetail();
      sectionDetail.pics = this.pics;
      sectionDetail.album = this.album;
      sectionDetail.title = this.title;
      sectionDetail.mtime = this.mtime;
      sectionDetail.picPage = this.picPage;
      sectionDetail.clientStatus = this.clientStatus;
      sectionDetail.id = this.id;
      sectionDetail.dirName = this.dirName;
      return sectionDetail;
    }
  }
}
