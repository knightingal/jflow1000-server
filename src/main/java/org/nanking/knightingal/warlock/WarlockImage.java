package org.nanking.knightingal.warlock;

import java.io.File;

public class WarlockImage {

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

  public WarlockImage(String name, File file) {
    this.name = name;
    this.file = file;
  }
}
