package com.sohu.multilanguage.logging.impl;

import java.io.Serializable;

import com.sohu.multilanguage.logging.MultiLanguageLog;

/**
 * 禁用log或者没有引入commons-logging.jar时使用该log
 *
 * @author XiongHui
 */
public class NullLogger implements MultiLanguageLog, Serializable {
  private static final long serialVersionUID = 711131452761300222L;

  @Override
  public void debug(Object message) {}

  @Override
  public void debug(Object message, Throwable t) {}

  @Override
  public void error(Object message) {}

  @Override
  public void error(Object message, Throwable t) {}

  @Override
  public void fatal(Object message) {}

  @Override
  public void fatal(Object message, Throwable t) {}

  @Override
  public void info(Object message) {}

  @Override
  public void info(Object message, Throwable t) {}

  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  @Override
  public boolean isErrorEnabled() {
    return false;
  }

  @Override
  public boolean isFatalEnabled() {
    return false;
  }

  @Override
  public boolean isInfoEnabled() {
    return false;
  }

  @Override
  public boolean isTraceEnabled() {
    return false;
  }

  @Override
  public boolean isWarnEnabled() {
    return false;
  }

  @Override
  public void trace(Object message) {}

  @Override
  public void trace(Object message, Throwable t) {}

  @Override
  public void warn(Object message) {}

  @Override
  public void warn(Object message, Throwable t) {}
}
