package org.nanking.knightingal.bean;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * @author Knightingal
 */
@Entity
public class Flow1000Section implements Serializable{

    public Flow1000Section() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String dirName;

    private String createTime;

    private String cover;

    private String album;

    private int coverWidth;

    private int coverHeight;

    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus = ClientStatus.NONE;


    @OneToMany(mappedBy = "flow1000Section", cascade = CascadeType.PERSIST)
    private List<Flow1000Img> images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ClientStatus getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(ClientStatus clientStatus) {
        this.clientStatus = clientStatus;
    }

    public List<Flow1000Img> getImages() {
        return images;
    }

    public void setImages(List<Flow1000Img> images) {
        this.images = images;
    }

    public enum ClientStatus {
        NONE,
        PENDING,
        LOCAL
    }

}
