package org.nanking.knightingal.bean;

public class Flow1000Img {

    public Flow1000Img() {
    }

    public Flow1000Img(int id, String name, int sectionId, int inCover, int width, int height) {
        this.id = id;
        this.name = name;
        this.sectionId = sectionId;
        this.inCover = inCover;
        this.width = width;
        this.height = height;
    }

    private int id;

    private String name;

    private int sectionId;

    private int inCover;

    private int width;

    private int height;

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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
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
}
