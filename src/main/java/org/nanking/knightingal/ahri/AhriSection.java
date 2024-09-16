package org.nanking.knightingal.ahri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

public class AhriSection {

  public AhriSection(String sectionName) {
    this.sectionName = sectionName;
  }

  @Getter
  private List<AhriImage> imageList = new ArrayList<>();

  private Set<String> existImageName = new HashSet<>();

  @Getter
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
    ahriImages.forEach(image -> addAhriImage(image));
  }

}
