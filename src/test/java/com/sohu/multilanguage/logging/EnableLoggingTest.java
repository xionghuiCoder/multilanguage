package com.sohu.multilanguage.logging;

import junit.framework.TestCase;

import com.sohu.multilanguage.core.Multilanguage;

/**
 * 测试日志的输出
 *
 * @author XiongHui
 */
public class EnableLoggingTest extends TestCase {
  public void testRightLogging() {
    Multilanguage language = new Multilanguage("language");
    System.out.println("right file");
    language.getFileMap("english", "01", "test.properties");
  }

  public void testWrongLogging() {
    Multilanguage language = new Multilanguage("language");
    System.out.println("wrong file with log:");
    language.getFileMap("english", "01", "none_file");
  }
}
