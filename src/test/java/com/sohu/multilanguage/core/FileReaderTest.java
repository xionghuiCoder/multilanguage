package com.sohu.multilanguage.core;

import junit.framework.TestCase;

public class FileReaderTest extends TestCase {

  /**
   * 校验FileReader的loadFile方法
   */
  public void testResolveFile() {
    Multilanguage language = new Multilanguage("language");
    for (int i = 1; i <= 10; i++) {
      String langcode = "00";
      if (i < 10) {
        langcode += "0";
      }
      printResult(language, "english", langcode + i);
    }
  }

  private void printResult(Multilanguage language, String langtype, String langcode) {
    String tip = language.getTip(langtype, "01", "test_annotation.properties", langcode);
    System.out.println(tip);
  }
}
