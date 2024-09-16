package org.nanking.knightingal.ahri;
import java.io.File;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AhriImage {

  private String name;

  private File file;

}
