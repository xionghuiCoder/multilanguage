package com.sohu.multilanguage.core.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sohu.multilanguage.logging.MultiLanguageLog;
import com.sohu.multilanguage.logging.MultiLanguageLogFactory;

/**
 * java.util.Properties读取.properties配置文件
 *
 * @author XiongHui
 */
public class PropertiesReader {
  private static final MultiLanguageLog LOG = MultiLanguageLogFactory
      .getLog(PropertiesReader.class);

  private static final String PATH = "/";

  public static Properties loadFile(String relativePath, String langType, String folderName,
      String fileName) {
    StringBuilder builder = new StringBuilder();
    if (relativePath != null) {
      builder.append(relativePath).append(PATH);
    }
    String path =
        builder.append(langType).append(PATH).append(folderName).append(PATH).append(fileName)
            .toString();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream inStream = loader.getResourceAsStream(path);
    if (inStream == null) {
      LOG.info("File " + path + " does not exist");
      return null;
    }
    Properties p = new Properties();
    try {
      p.load(inStream);
    } catch (IOException e) {
      LOG.error(e.getMessage());
    }
    return p;
  }
}
