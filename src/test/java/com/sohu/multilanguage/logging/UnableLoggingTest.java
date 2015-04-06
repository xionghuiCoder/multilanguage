package com.sohu.multilanguage.logging;

import junit.framework.TestCase;

import com.sohu.multilanguage.core.Multilanguage;

/**
 * 测试{@link MultiLanguageLogFactory#LOG_UNABLE}
 *
 * @author XiongHui
 */
public class UnableLoggingTest extends TestCase {
  public void testUnableLogging() {
    System.setProperty(MultiLanguageLogFactory.LOG_UNABLE, "true");
    Multilanguage language = new Multilanguage("language");
    System.out.println("wrong file without log");
    language.getFileMap("english", "01", "none_file");
  }
}
