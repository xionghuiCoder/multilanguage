package com.sohu.multilanguage.core.reader;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * java.util.ResourceBundle读取.properties配置文件
 *
 * @author XiongHui
 */
public class ResourceBundleReader {
  private static final String PATH = "/";

  public static ResourceBundle loadFile(String relativePath, String langType, String folderName,
      String fileName) {
    StringBuilder builder = new StringBuilder();
    if (relativePath != null) {
      builder.append(relativePath).append(PATH);
    }
    String path =
        builder.append(langType).append(PATH).append(folderName).append(PATH).append(fileName)
            .toString();
    ResourceBundle rb = ResourceBundle.getBundle(path, Locale.getDefault());
    return rb;
  }
}
