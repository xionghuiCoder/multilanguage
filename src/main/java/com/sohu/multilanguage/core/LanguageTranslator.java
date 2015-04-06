package com.sohu.multilanguage.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class LanguageTranslator {
  // 转义左符号
  private static char LEFT_BRACKET = '{';

  // 转义右符号
  private static char RIGHT_BRACKET = '}';


  /**
   * 读取文件多语
   */
  static Map<String, String> getFileMap(String relativePath, String langType, String folderName,
      String fileName) {
    Map<String, String> valueMap =
        LanguageResourceLoader.getFileMap(relativePath, langType, folderName, fileName);
    return valueMap;
  }

  /**
   * 通过多语类型编码、文件夹编码、文件名称和多语值查找多语
   *
   * @warning folderName会自动转换成小写、fileName会自动转换成小写
   */
  static String getTip(String relativePath, String langType, String folderName, String fileName,
      String langcode) {
    Map<String, String> valueMap = getFileMap(relativePath, langType, folderName, fileName);
    if (valueMap == null) {
      return null;
    }
    String value = valueMap.get(langcode);
    return value;
  }

  /**
   * 批量获取踢tip提示信息的多语
   */
  static Map<String, String> getTips(String relativePath, String langType, String folderName,
      String fileName, String[] langcodes) {
    Map<String, String> valueMap = getFileMap(relativePath, langType, folderName, fileName);
    if (valueMap == null || langcodes == null) {
      return valueMap;
    }
    Map<String, String> tipsMap = new HashMap<String, String>();
    for (String langcode : langcodes) {
      String value = valueMap.get(langcode);
      tipsMap.put(langcode, value);
    }
    return tipsMap;
  }

  /**
   * 通过参数设置多语，设置规则：将params里的第i个参数替换掉到多语字符串的{i}<br />
   * ps：该替换算法时间复杂度为O(n)，即message的长度
   */
  static String getTipByParams(String relativePath, String langType, String folderName,
      String fileName, String langcode, String[] params) {
    String tip = getTip(relativePath, langType, folderName, fileName, langcode);
    return replaceParams(tip, params);
  }

  /**
   * 使用params替换tip
   *
   * @param tip
   * @param params
   * @return
   */
  private static String replaceParams(String tip, String[] params) {
    if (tip != null && params != null && params.length > 0) {
      StringBuilder msgBuilder = new StringBuilder();
      boolean begin = false;
      StringBuilder midBuilder = new StringBuilder();
      // 非负数正则表达式
      Pattern pattern = Pattern.compile("^[1-9]\\d*|0$");
      for (int i = 0, len = tip.length(); i < len; i++) {
        char now = tip.charAt(i);
        if (now == LEFT_BRACKET) {
          if (begin) {
            msgBuilder.append(midBuilder);
            midBuilder.setLength(0);
          }
          midBuilder.append(now);
          begin = true;
          continue;
        }
        if (begin) {
          if (now == RIGHT_BRACKET) {
            String number = midBuilder.substring(1);
            Matcher matcher = pattern.matcher(number);
            boolean b = matcher.matches();
            if (b) {
              int position = Integer.parseInt(number);
              if (params.length > position) {
                String replaceStr = params[position];
                if (replaceStr != null) {
                  msgBuilder.append(replaceStr);
                  midBuilder.setLength(0);
                  begin = false;
                  continue;
                }
              }
            }
            msgBuilder.append(midBuilder).append(now);
            midBuilder.setLength(0);
            begin = false;
          } else {
            midBuilder.append(now);
          }
          continue;
        }
        msgBuilder.append(now);
      }
      if (begin) {
        msgBuilder.append(midBuilder);
      }
      tip = msgBuilder.toString();
    }
    return tip;
  }

  /**
   * 通过参数设置多条多语
   */
  static Map<String, String> getTipsByParams(String relativePath, String langType,
      String folderName, String fileName, String[] langcodes, String[] params) {
    Map<String, String> tipsMap =
        LanguageTranslator.getTips(relativePath, langType, folderName, fileName, langcodes);
    for (Map.Entry<String, String> e : tipsMap.entrySet()) {
      String key = e.getKey();
      String value = replaceParams(e.getValue(), params);
      tipsMap.put(key, value);
    }
    return tipsMap;
  }

  /**
   * 通过多条参数设置多条多语
   */
  static Map<String, String> getTipsByParamses(String relativePath, String langType,
      String folderName, String fileName, String[] langcodes, String[][] params) {
    Map<String, String> valueMap = getFileMap(relativePath, langType, folderName, fileName);
    if (valueMap == null || langcodes == null || params == null || langcodes.length < params.length) {
      return valueMap;
    }
    Map<String, String> tipsMap = new HashMap<String, String>();
    for (int i = 0, len = langcodes.length; i < len; i++) {
      String tip = valueMap.get(langcodes[i]);
      tip = replaceParams(tip, params[i]);
      tipsMap.put(langcodes[i], tip);
    }
    return tipsMap;
  }
}
