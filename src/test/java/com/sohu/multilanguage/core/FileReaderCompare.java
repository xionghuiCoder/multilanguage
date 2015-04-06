package com.sohu.multilanguage.core;

import java.util.ResourceBundle;

import junit.framework.TestCase;

import com.sohu.multilanguage.core.reader.PropertiesReader;
import com.sohu.multilanguage.core.reader.ResourceBundleReader;
import com.sohu.multilanguage.util.MemoryUtil;

/**
 * 三种文件读取方法的性能比较<br />
 *
 * 1、{@link FileReader} <br />
 * 2、{@link PropertiesReader} <br />
 * 3、{@link ResourceBundleReader}
 *
 * @author XiongHui
 */
public class FileReaderCompare extends TestCase {
  private static final int LOOP = 1000;

  @Override
  public void setUp() throws Exception {
    MemoryUtil.printMemoryInfo();
  }

  public void testFileReader() {
    System.out.print("File reader " + LOOP + " times, it cost ");
    String relativePath = "language";
    String langType = "english";
    String folderName = "01";
    String fileName = "test.properties";
    long bg = System.currentTimeMillis();
    for (int i = 0; i < LOOP; i++) {
      FileReader.loadFile(relativePath, langType, folderName, fileName);
    }
    System.out.println(System.currentTimeMillis() - bg + "ms");
  }

  public void testPropertiesReader() {
    System.out.print("Properties reader " + LOOP + " times, it cost ");
    String relativePath = "language";
    String langType = "english";
    String folderName = "01";
    String fileName = "test.properties";
    long bg = System.currentTimeMillis();
    for (int i = 0; i < LOOP; i++) {
      PropertiesReader.loadFile(relativePath, langType, folderName, fileName);
    }
    System.out.println(System.currentTimeMillis() - bg + "ms");
  }

  public void testResourceBundleReader() {
    System.out.print("ResourceBundle reader " + LOOP + " times, it cost ");
    String relativePath = "language";
    String langType = "english";
    String folderName = "01";
    String fileName = "test";
    long bg = System.currentTimeMillis();
    for (int i = 0; i < LOOP; i++) {
      ResourceBundleReader.loadFile(relativePath, langType, folderName, fileName);
      ResourceBundle.clearCache();
    }
    System.out.println(System.currentTimeMillis() - bg + "ms");
  }

  @Override
  public void tearDown() {
    // 尝试gc
    System.gc();
  }
}
