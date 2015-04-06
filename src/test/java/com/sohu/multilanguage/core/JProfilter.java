package com.sohu.multilanguage.core;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import com.sohu.multilanguage.util.MemoryUtil;

/**
 * JProfilter监控内存和CUP
 *
 * @author XiongHui
 */
public class JProfilter extends SupCompare {
  private static final int THREAD_COUNT = 10;

  private static final Multilanguage LANGUAGE = new Multilanguage("language");

  @Override
  public void setUp() throws Exception {
    MemoryUtil.printMemoryInfo();
  }

  public void testMonitor() throws Exception {
    final Random random = new SecureRandom();
    final Map<String, String> keyMap = new TreeMap<String, String>();
    final CountDownLatch endLatch = new CountDownLatch(1);
    for (int i = 0; i < THREAD_COUNT; ++i) {
      Thread thread = new Thread() {
        @Override
        public void run() {
          for (;;) {
            int i1 = random.nextInt(LANGTYPYS.length);
            int i2 = random.nextInt(FOLDER_NAMES.length);
            int i3 = random.nextInt(FILE_NAMES.length);
            String key = LANGTYPYS[i1] + "/" + FOLDER_NAMES[i2] + "/" + FILE_NAMES[i3];
            if (!keyMap.containsKey(key)) {
              synchronized (JProfilter.class) {
                String result = keyMap.put(key, "");
                if (result == null) {
                  if (keyMap.size() == LANGTYPYS.length * FOLDER_NAMES.length * FILE_NAMES.length) {
                    for (String s : keyMap.keySet()) {
                      System.out.println(s);
                    }
                  }
                }
              }
            }
            LANGUAGE.getTip(LANGTYPYS[i1], FOLDER_NAMES[i2], FILE_NAMES[i3], "0001");
          }
        }
      };
      thread.start();
    }
    endLatch.await();
  }
}
