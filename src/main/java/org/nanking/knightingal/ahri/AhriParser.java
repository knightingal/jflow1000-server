package org.nanking.knightingal.ahri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.nanking.knightingal.bean.AlbumConfig;
import org.nanking.knightingal.bean.Flow1000Img;
import org.nanking.knightingal.bean.Flow1000Section;

public class AhriParser {

  private AhriParser() {
    // private constructor
  }


  /**
   * Build Flow1000Section object
   * @param flow1000SectionOption
   * @param albumConfig
   * @param section
   * @return
   */
  public static Flow1000Section buildFlow1000Section(Optional<Flow1000Section> flow1000SectionOption,
      AlbumConfig albumConfig, File section) {
    Flow1000Section flow1000Section;
    if (!flow1000SectionOption.isPresent()) {
      flow1000Section = new Flow1000Section();
      flow1000Section.setAlbum(albumConfig.getName());
      flow1000Section.setDirName(section.getName());
      flow1000Section.setName(section.getName());
      if (isTimeStampe(section.getName())) {
        flow1000Section.setCreateTime(section.getName().substring(0, 14));
      } else {
        flow1000Section.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(section.lastModified()));
      }
    } else {
      flow1000Section = flow1000SectionOption.get();
    }
    return flow1000Section;
  }

  public static boolean isImageFile(File file) {
    String fileName = file.getName().toLowerCase();
    return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
        || fileName.endsWith(".png") || fileName.endsWith(".webp")
        || fileName.endsWith(".avif");
  }

  public static int fileNameComparator(File file1, File file2) {
    if (file1.getName().contains("-") && file2.getName().contains("-")) {
      int num1 = Integer.parseInt(file1.getName().split("-")[0]);
      int num2 = Integer.parseInt(file2.getName().split("-")[0]);
      return num1 - num2;
    } else {
      try {
        String fileName1 = file1.getName();
        String fileName2 = file2.getName();
        String[] fileSplite1 = fileName1.split("\\.");
        String[] fileSplite2 = fileName2.split("\\.");
        int num1 = Integer.parseInt(fileSplite1[0]);
        int num2 = Integer.parseInt(fileSplite2[0]);
        return num1 - num2;
      } catch (Exception e) {
        return (int) (file1.lastModified() - file2.lastModified());
      }
    }
  }

  /**
   * Build Flow1000Img object
   * @param flow1000Optional
   * @param flow1000Section
   * @param albumConfig
   * @param image
   * @return
   */
  public static Flow1000Img buildFlow1000Img(
      Optional<Flow1000Img> flow1000Optional, 
      Flow1000Section flow1000Section, 
      AlbumConfig albumConfig, 
      File image
  ) {
      Flow1000Img flow1000Img;
      if (!flow1000Optional.isPresent()) {
        flow1000Img = new Flow1000Img();
        if (albumConfig.isEncrypted()) {
          flow1000Img.setName(image.getName() + ".bin");
        } else {
          flow1000Img.setName(image.getName());
        }
        flow1000Img.setFlow1000Section(flow1000Section);
      } else {
        flow1000Img = flow1000Optional.get();
      }

      return flow1000Img;
  }

  private static boolean isTimeStampe(String str) {
    try {
      new SimpleDateFormat("yyyyMMddHHmmss").parse(str);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
