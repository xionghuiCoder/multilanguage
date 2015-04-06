package com.sohu.multilanguage.core;

import junit.framework.TestCase;

/**
 * {@link LanguageTranslator}测试类
 *
 * @author XiongHui
 */
public class LanguageTranslatorTest extends TestCase {
  private static final String[] params = {"first", "second"};

  static {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    for (String s : params) {
      builder.append("\"");
      builder.append(s);
      builder.append("\"");
      builder.append(",");
    }
    builder.deleteCharAt(builder.length() - 1);
    builder.append("}");
    System.out.println(builder);
  }

  /**
   * {@link LanguageTranslator#getTipByParams} test
   */
  public void testReadLangByParams() {
    Multilanguage language = new Multilanguage("language");
    for (int i = 1; i <= 15; i++) {
      String langcode = "00";
      if (i < 10) {
        langcode += "0";
      }
      printResult(language, "english", langcode + i, params);
    }
    for (int i = 1; i <= 15; i++) {
      String langcode = "00";
      if (i < 10) {
        langcode += "0";
      }
      printResult(language, "simpchn", langcode + i, params);
    }
    for (int i = 1; i <= 15; i++) {
      String langcode = "00";
      if (i < 10) {
        langcode += "0";
      }
      printResult(language, "tradchn", langcode + i, params);
    }
  }

  private void printResult(Multilanguage language, String langtype, String langcode, String[] params) {
    String tip = language.getTip(langtype, "01", "test.properties", langcode);
    System.out.println(tip);
    String result = language.getTipByParams(langtype, "01", "test.properties", langcode, params);
    System.out.println(result);
    System.out.println();
  }
}
