package com.sohu.multilanguage.core;

import java.util.Map;

/**
 * 多语操作Facade类
 *
 * @author XiongHui
 */
public final class Multilanguage {
  // 多语文件的相对路径
  private String relativePath = null;

  public Multilanguage() {}

  public Multilanguage(String relativePath) {
    this.relativePath = relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  /**
   * 读取获取整个文件多语
   */
  public Map<String, String> getFileMap(String langType, String folderName, String fileName) {
    if (langType == null || folderName == null || fileName == null) {
      return null;
    }
    return LanguageTranslator.getFileMap(relativePath, langType, folderName, fileName);
  }

  /**
   * 读取单条tip多语
   */
  public String getTip(String langType, String folderName, String fileName, String langcode) {
    if (langType == null || folderName == null || fileName == null) {
      return null;
    }
    return LanguageTranslator.getTip(relativePath, langType, folderName, fileName, langcode);
  }

  /**
   * 批量读取tip多语
   */
  public Map<String, String> getTips(String langType, String folderName, String fileName,
      String[] langcodes) {
    if (langType == null || folderName == null || fileName == null) {
      return null;
    }
    return LanguageTranslator.getTips(relativePath, langType, folderName, fileName, langcodes);
  }

  /**
   * 通过参数获取单条多语
   */
  public String getTipByParams(String langType, String folderName, String fileName,
      String langcode, String[] params) {
    if (langType == null || folderName == null || fileName == null) {
      return null;
    }
    return LanguageTranslator.getTipByParams(relativePath, langType, folderName, fileName,
        langcode, params);
  }

  /**
   * 通过参数获取多条多语
   */
  public Map<String, String> getTipsByParams(String langType, String folderName, String fileName,
      String[] langcodes, String[] params) {
    if (langType == null || folderName == null || fileName == null) {
      return null;
    }
    return LanguageTranslator.getTipsByParams(relativePath, langType, folderName, fileName,
        langcodes, params);
  }

  /**
   * 通过多条参数获取多条多语
   */
  public Map<String, String> getTipsByParamses(String langType, String folderName, String fileName,
      String[] langcodes, String[][] params) {
    if (langType == null || folderName == null || fileName == null) {
      return null;
    }
    return LanguageTranslator.getTipsByParamses(relativePath, langType, folderName, fileName,
        langcodes, params);
  }
}
