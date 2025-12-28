package org.nanking.knightingal.bean;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * @author Knightingal
 */
@Entity
public class Flow1000Img implements Serializable{

    public Flow1000Img() {
    }

    public Flow1000Img(Long id, String name, int inCover, int width, int height) {
        this.id = id;
        this.name = name;
        this.inCover = inCover;
        this.width = width;
        this.height = height;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private int inCover;

    private int width;

    private int height;

    private String src;

    private String href;

    @ManyToOne
    @JoinColumn(name = "sectionId", referencedColumnName = "id")
    @JsonIgnore
    private Flow1000Section flow1000Section;

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

    public int getInCover() {
        return inCover;
    }

    public void setInCover(int inCover) {
        this.inCover = inCover;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Flow1000Section getFlow1000Section() {
        return flow1000Section;
    }

    public void setFlow1000Section(Flow1000Section flow1000Section) {
        this.flow1000Section = flow1000Section;
    }
}
