package org.nanking.knightingal.bean;

public class ImgDetail {

    public ImgDetail() {
    }

    public ImgDetail(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    private String name;

    private int width;

    private int height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
