package org.nanking.knightingal.bean;

/**
 * @author Knightingal
 */
public class PicIndex {

  private long index;

  private String name;

  private String mtime;

  private String cover;

  private int coverWidth;

  private int coverHeight;

  private String album;

  private String title;

  private ClientStatus clientStatus;

  public static enum ClientStatus {
    NONE,
    PENDING,
    LOCAL
  }

  public long getIndex() {
    return index;
  }

  public void setIndex(long index) {
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

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ClientStatus getClientStatus() {
    return clientStatus;
  }

  public void setClientStatus(ClientStatus clientStatus) {
    this.clientStatus = clientStatus;
  }

  public PicIndex(long index, String name, String mtime, String cover, int coverWidth, int coverHeight, String album,
      String title, ClientStatus clientStatus) {
    this.index = index;
    this.name = name;
    this.mtime = mtime;
    this.cover = cover;
    this.coverWidth = coverWidth;
    this.coverHeight = coverHeight;
    this.album = album;
    this.title = title;
    this.clientStatus = clientStatus;
  }
}
