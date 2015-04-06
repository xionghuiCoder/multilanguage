package com.sohu.multilanguage.core;

import junit.framework.TestCase;

/**
 * {@link Multilanguage}测试类
 *
 * @author XiongHui
 */
public class MultilanguageTest extends TestCase {

  public void testRelativePath() {
    System.out.print("test relativePath: ");
    Multilanguage language = new Multilanguage("language");
    String tip = language.getTip("english", "01", "test.properties", "0001");
    System.out.println(tip);
  }

  public void testWithoutRelativePath() {
    System.out.print("test without relativePath: ");
    Multilanguage language = new Multilanguage();
    String tip = language.getTip("language/english", "01", "test.properties", "0001");
    System.out.println(tip);
  }
}
