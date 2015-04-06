package com.sohu.multilanguage.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.sohu.multilanguage.logging.MultiLanguageLog;
import com.sohu.multilanguage.logging.MultiLanguageLogFactory;

/**
 * 该类主要用于解析系统的多语文件，具体解析规则{@link #resolveFile}
 *
 * @author XiongHui
 */
class FileReader {
  private static final MultiLanguageLog LOG = MultiLanguageLogFactory.getLog(FileReader.class);

  // 语种资源文件的编码字符集名称
  // 设置为UTF-8而非UTF-16是因为html只支持UTF-8而不支持UTF-16
  private static final String CHARSET_NAME = "utf-8";

  // 文件夹路径分隔符
  private static final String PATH = "/";

  // 文件的key、value分割符号
  private static final char SPLIT = '=';

  // 单个斜杠分隔符
  private static final char SINGLE_NOTE = '/';

  // 注释：//...注释一行，多行注释请使用多个//...
  private static final String NOTE = "//";

  /**
   * 通过多语类型编码、文件夹名称和文件名称加载多语
   *
   * @param langType 多语类型
   * @param folderName 文件夹名称
   * @param fileName 文件名称
   */
  static Map<String, String> loadFile(String relativePath, String langType, String folderName,
      String fileName) {
    StringBuilder builder = new StringBuilder();
    if (relativePath != null) {
      builder.append(relativePath).append(PATH);
    }
    String path =
        builder.append(langType).append(PATH).append(folderName).append(PATH).append(fileName)
            .toString();
    Map<String, String> fileMap = null;
    InputStreamReader inputReader = null;
    BufferedReader reader = null;
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream inStream = loader.getResourceAsStream(path);
    // 存在文件，则读取文件数据
    if (inStream == null) {
      LOG.info("File " + path + " does not exist");
      return null;
    }
    inStream = new BufferedInputStream(inStream);
    try {
      inputReader = new InputStreamReader(inStream, CHARSET_NAME);
      reader = new BufferedReader(inputReader);
      fileMap = resolveFile(reader);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    } finally {
      try {
        reader.close();
      } catch (Exception e) {
      } finally {
        try {
          inputReader.close();
        } catch (Exception e) {
        } finally {
          try {
            inStream.close();
          } catch (Exception e) {
          }
        }
      }
    }
    return fileMap;
  }

  /**
   * 处理过程：按行读取字符串，去掉注释部分，把////转换成//，按{@link #SPLIT}分割字符串，去掉编码key前后的空格字符。
   *
   * @warning 1、解析时会把//后面的内容当做注释忽略掉，如果需要使用//则要用////代替（不支持/*...* /注释）；
   * @warning 2、解析当前行若没有发现=，会忽略当前行；
   * @warning 3、解析时会处理编码key，把它前后的（\t:tab制表符、\r:回车、\n:换行、\f:走纸换页、\b:退格和空格符号 ）去掉；但不会处理编码对应的值value。
   */
  private static Map<String, String> resolveFile(BufferedReader reader) throws Exception {
    Map<String, String> fileMap = new HashMap<String, String>();
    while (true) {
      String line = reader.readLine();
      // 此时到达行末尾
      if (line == null) {
        break;
      }
      int len = line.length();
      StringBuilder keyBuilder = new StringBuilder(len);
      StringBuilder valueBuilder = new StringBuilder(len);
      StringBuilder noteBuilder = new StringBuilder(4);
      // 是否没有等号
      boolean noneEqual = true;
      for (int i = 0; i < len; i++) {
        if (line.charAt(i) == '/') {
          noteBuilder.append(line.charAt(i));
          if ((NOTE + NOTE).equals(noteBuilder.toString())) {
            /** 把////转换为// */
            if (noneEqual) {
              keyBuilder.append(NOTE);
            } else {
              valueBuilder.append(NOTE);
            }
            noteBuilder.setLength(0);
          }
          continue;
        }
        if (noteBuilder.length() > 0) {
          if (noteBuilder.length() == 1) {
            if (noneEqual) {
              keyBuilder.append(SINGLE_NOTE);
            } else {
              valueBuilder.append(SINGLE_NOTE);
            }
            noteBuilder.setLength(0);
          } else {
            break;
          }
        }
        if (noneEqual) {
          if (line.charAt(i) == SPLIT) {
            noneEqual = false;
            continue;
          }
          keyBuilder.append(line.charAt(i));
        } else {
          valueBuilder.append(line.charAt(i));
        }
      }
      if (noneEqual) {
        // 若不包括SPLIT，则忽略该行
        continue;
      }
      if (noteBuilder.length() == 1) {
        valueBuilder.append(SINGLE_NOTE);
      }
      // 去掉编码前后的（\t:tab制表符、\r:回车、\n:换行、\f:走纸换页、\b:退格和空格符号）
      String key = keyBuilder.toString().trim();
      // 不处理编码对应值的（\t:tab制表符、\r:回车、\n:换行、\f:走纸换页、\b:退格和空格符号）
      String value = valueBuilder.toString();
      fileMap.put(key, value);
    }
    return fileMap;
  }
}
