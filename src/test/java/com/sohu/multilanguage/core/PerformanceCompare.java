package com.sohu.multilanguage.core;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.sohu.multilanguage.util.MemoryUtil;

/**
 * 三种缓存方法的性能比较<br />
 *
 * 1、{@link #testNoneCache} <br />
 * 2、{@link #testSoftCache} <br />
 * 3、{@link #testStrongCache}
 *
 * @author XiongHui
 */
public class PerformanceCompare extends SupCompare {
  private static final int TIME = 10000;

  private static final String[][] PARAMS = new String[TIME][];

  static {
    // 初始化参数
    Random random = new SecureRandom();
    for (int i = 0; i < TIME; i++) {
      String[] param = new String[4];
      int index = random.nextInt(LANGTYPYS.length);
      param[0] = LANGTYPYS[index];
      index = random.nextInt(FOLDER_NAMES.length);
      param[1] = FOLDER_NAMES[index];
      index = random.nextInt(FILE_NAMES.length);
      param[2] = FILE_NAMES[index];
      int langcode = random.nextInt(100);
      // 补全长度为四位
      if (langcode < 10) {
        param[3] = "000" + langcode;
      } else {
        param[3] = "00" + langcode;
      }
      PARAMS[i] = param;
    }
  }

  @Override
  public void setUp() throws Exception {
    MemoryUtil.printMemoryInfo();
  }

  public void testNoneCache() {
    System.out.print("none cache, get key " + TIME + " times, it cost ");
    long bg = System.currentTimeMillis();
    for (int i = 0; i < TIME; i++) {
      Map<String, String> fileMap =
          FileReader.loadFile("language", PARAMS[i][0], PARAMS[i][1], PARAMS[i][2]);
      fileMap.get(PARAMS[i][3]);
    }
    System.out.println(System.currentTimeMillis() - bg + "ms");
  }

  public void testSoftCache() {
    System.out.print("soft cache, get key " + TIME + " times, it cost ");
    long bg = System.currentTimeMillis();
    Multilanguage language = new Multilanguage("language");
    for (int i = 0; i < TIME; i++) {
      language.getTip(PARAMS[i][0], PARAMS[i][1], PARAMS[i][2], PARAMS[i][3]);
    }
    System.out.println(System.currentTimeMillis() - bg + "ms");
  }

  public void testStrongCache() {
    System.out.print("strong cache, get key " + TIME + " times, it cost ");
    long bg = System.currentTimeMillis();
    // 缓存所有的fileMap
    Map<String, Map<String, String>> filesMap = new HashMap<String, Map<String, String>>();
    for (int i = 0; i < TIME; i++) {
      String key = PARAMS[i][0] + PARAMS[i][1] + PARAMS[i][2];
      Map<String, String> fileMap = filesMap.get(key);
      if (fileMap == null) {
        fileMap = FileReader.loadFile("language", PARAMS[i][0], PARAMS[i][1], PARAMS[i][2]);
        filesMap.put(key, fileMap);
      }
      fileMap.get(PARAMS[i][3]);
    }
    System.out.println(System.currentTimeMillis() - bg + "ms");
  }

  @Override
  public void tearDown() {
    // 尝试gc
    System.gc();
  }
}
