package org.nanking.knightingal.warlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WarlockSection {

  public WarlockSection(String sectionName) {
    this.sectionName = sectionName;
  }

  private List<WarlockImage> imageList = new ArrayList<>();

  private Set<String> existImageName = new HashSet<>();

  private String sectionName;

  public boolean checkImageNameExist(String imageName) {
    return existImageName.contains(imageName);
  }

  public void addWarlockImage(WarlockImage warlockImage) {
    if (checkImageNameExist(warlockImage.getName())) {
      return;
    }

    existImageName.add(warlockImage.getName());
    imageList.add(warlockImage);
  }

  public void addWarlockImages(List<WarlockImage> warlockImages) {
    warlockImages.forEach(this::addWarlockImage);
  }

  public List<WarlockImage> getImageList() {
    return imageList;
  }

  public void setImageList(List<WarlockImage> imageList) {
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
