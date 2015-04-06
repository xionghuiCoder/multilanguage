package com.sohu.multilanguage.logging;

import com.sohu.multilanguage.logging.impl.NullLogger;

/**
 * log factory的代理类，当没有禁用multilanguage的多语并引用了commons-logging.jar时使用commons log，否则使用自定义的
 * {@link NullLogger}
 *
 * @author XiongHui
 */
public class MultiLanguageLogFactory {
  public static final String LOG_UNABLE = "com.sohu.multilanguage.log.unable";

  private static final String CLAZZ = "org.apache.commons.logging.LogFactory";

  private static boolean logUnable;

  private static boolean commonsLogExist;

  private LogFactoryAdapter logFactoryAdapter;

  static {
    logUnable = Boolean.getBoolean(LOG_UNABLE);
    if (!logUnable) {
      try {
        Class.forName(CLAZZ);
        commonsLogExist = true;
      } catch (ClassNotFoundException e) {
        // swallow it
        // System.out.println("\"" + CLAZZ + "\" is not existed");
      }
    }
  }

  public MultiLanguageLogFactory(LogFactoryAdapter logFactoryAdapter) {
    this.logFactoryAdapter = logFactoryAdapter;
  }

  public Object getAttribute(String name) {
    if (logFactoryAdapter == null) {
      return null;
    }
    return logFactoryAdapter.getAttribute(name);
  }

  public String[] getAttributeNames() {
    if (logFactoryAdapter == null) {
      return null;
    }
    return logFactoryAdapter.getAttributeNames();
  }

  public MultiLanguageLog getInstance(Class<?> clazz) {
    if (logFactoryAdapter == null) {
      return null;
    }
    return logFactoryAdapter.getInstance(clazz);
  }

  public MultiLanguageLog getInstance(String name) {
    if (logFactoryAdapter == null) {
      return null;
    }
    return logFactoryAdapter.getInstance(name);
  }

  public void release() {
    if (logFactoryAdapter != null) {
      logFactoryAdapter.release();
    }
  }

  public void removeAttribute(String name) {
    if (logFactoryAdapter != null) {
      logFactoryAdapter.removeAttribute(name);
    }
  }

  public void setAttribute(String name, Object value) {
    if (logFactoryAdapter != null) {
      logFactoryAdapter.setAttribute(name, value);
    }
  }

  public static MultiLanguageLogFactory getFactory() {
    if (logUnable || !commonsLogExist) {
      return new MultiLanguageLogFactory(null);
    }
    LogFactoryAdapter logFactoryAdapter = LogFactoryAdapter.getFactory();
    return new MultiLanguageLogFactory(logFactoryAdapter);
  }

  public static MultiLanguageLog getLog(Class<?> clazz) {
    if (logUnable || !commonsLogExist) {
      return new NullLogger();
    }
    return LogFactoryAdapter.getLog(clazz);
  }

  public static MultiLanguageLog getLog(String name) {
    if (logUnable || !commonsLogExist) {
      return new NullLogger();
    }
    return LogFactoryAdapter.getLog(name);
  }

  public static void release(ClassLoader classLoader) {
    if (logUnable || !commonsLogExist) {
      return;
    }
    LogFactoryAdapter.release(classLoader);
  }

  public static void releaseAll() {
    if (logUnable || !commonsLogExist) {
      return;
    }
    LogFactoryAdapter.releaseAll();
  }

  public static String objectId(Object o) {
    return LogFactoryAdapter.objectId(o);
  }
}
