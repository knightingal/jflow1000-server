package org.nanking.knightingal.bean;

import java.util.List;

/**
 * @author Knightingal
 */
public class PicIndexPage {

  public PicIndexPage(List<PicIndex> rows, int total) {
    this.rows = rows;
    this.total = total;
  }

  private final List<PicIndex> rows;

  private final int total;

  public List<PicIndex> getRows() {
    return rows;
  }

  public int getTotal() {
    return total;
  }
}
