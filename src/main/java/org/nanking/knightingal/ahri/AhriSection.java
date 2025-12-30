package org.nanking.knightingal.ahri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AhriSection {

  public AhriSection(String sectionName) {
    this.sectionName = sectionName;
  }

  private List<AhriImage> imageList = new ArrayList<>();

  private Set<String> existImageName = new HashSet<>();

  private String sectionName;

  public boolean checkImageNameExist(String imageName) {
    return existImageName.contains(imageName);
  }

  public void addAhriImage(AhriImage ahriImage) {
    if (checkImageNameExist(ahriImage.getName())) {
      return;
    }

    existImageName.add(ahriImage.getName());
    imageList.add(ahriImage);
  }

  public void addAhriImages(List<AhriImage> ahriImages) {
    ahriImages.forEach(this::addAhriImage);
  }

  public List<AhriImage> getImageList() {
    return imageList;
  }

  public void setImageList(List<AhriImage> imageList) {
    this.imageList = imageList;
  }

  public Set<String> getExistImageName() {
    return existImageName;
  }

  public void setExistImageName(Set<String> existImageName) {
    this.existImageName = existImageName;
  }

  public String getSectionName() {
    return sectionName;
  }

  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
  }
}
