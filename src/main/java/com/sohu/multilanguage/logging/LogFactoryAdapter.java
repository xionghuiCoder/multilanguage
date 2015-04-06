package com.sohu.multilanguage.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.multilanguage.logging.impl.MultiLanguageLogImpl;

/**
 * LogFactory包装类
 *
 * @author XiongHui
 */
class LogFactoryAdapter {
  private LogFactory logFactory;

  private LogFactoryAdapter(LogFactory logFactory) {
    this.logFactory = logFactory;
  }

  public Object getAttribute(String name) {
    if (logFactory == null) {
      return null;
    }
    return logFactory.getAttribute(name);
  }

  public String[] getAttributeNames() {
    if (logFactory == null) {
      return null;
    }
    return logFactory.getAttributeNames();
  }

  public MultiLanguageLog getInstance(Class<?> clazz) {
    if (logFactory == null) {
      return null;
    }
    Log log = logFactory.getInstance(clazz);
    MultiLanguageLog poolLog = new MultiLanguageLogImpl(log);
    return poolLog;
  }

  public MultiLanguageLog getInstance(String name) {
    if (logFactory == null) {
      return null;
    }
    Log log = logFactory.getInstance(name);
    MultiLanguageLog poolLog = new MultiLanguageLogImpl(log);
    return poolLog;
  }

  public void release() {
    if (logFactory != null) {
      logFactory.release();
    }
  }

  public void removeAttribute(String name) {
    if (logFactory != null) {
      logFactory.removeAttribute(name);
    }
  }

  public void setAttribute(String name, Object value) {
    if (logFactory != null) {
      logFactory.setAttribute(name, value);
    }
  }

  public static LogFactoryAdapter getFactory() {
    LogFactory factory = LogFactory.getFactory();
    return new LogFactoryAdapter(factory);
  }

  public static MultiLanguageLog getLog(Class<?> clazz) {
    Log log = LogFactory.getLog(clazz);
    MultiLanguageLog poolLog = new MultiLanguageLogImpl(log);
    return poolLog;
  }

  public static MultiLanguageLog getLog(String name) {
    Log log = LogFactory.getLog(name);
    MultiLanguageLog poolLog = new MultiLanguageLogImpl(log);
    return poolLog;
  }

  public static void release(ClassLoader classLoader) {
    LogFactory.release(classLoader);
  }

  public static void releaseAll() {
    LogFactory.releaseAll();
  }

  public static String objectId(Object o) {
    if (o == null) {
      return "null";
    } else {
      return o.getClass().getName() + "@" + System.identityHashCode(o);
    }
  }
}
