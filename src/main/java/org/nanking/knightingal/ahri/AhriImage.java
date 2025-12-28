package org.nanking.knightingal.ahri;
import java.io.File;


public class AhriImage {

  private String name;

  private File file;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public AhriImage(String name, File file) {
    this.name = name;
    this.file = file;
  }
}
