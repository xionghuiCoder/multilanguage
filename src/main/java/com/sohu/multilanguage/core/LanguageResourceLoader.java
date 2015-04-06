package com.sohu.multilanguage.core;

import java.util.Map;

import com.sohu.multilanguage.util.ReferenceHeapMediator;

/**
 * 用于缓存多语文件的值，主要功能是控制多线程问题。<br />
 * 支持不同的文件同时读取，读取过一个文件后不会再次读取
 *
 * @author XiongHui
 */
class LanguageResourceLoader {
  // 读取多语文件的锁前缀
  private static final String LANGUAGE_FILE = "language_file_";

  /** 加载文件中的多语键值对，此处需要处理同步问题 */
  static Map<String, String> getFileMap(String relativePath, String langType, String folderName,
      String fileName) {
    String key =
        new StringBuilder().append(relativePath).append(langType).append(folderName)
            .append(fileName).toString();
    Map<String, String> valueMap = ReferenceHeapMediator.MEDIATOR.get(key);
    if (valueMap != null) {
      return valueMap;
    }
    // 加prefix前缀是为了防止同步时控制了其它操作的同步
    // windows不区分文件和文件夹的大小写，而unix/linux区分，所以字符串锁转换为小写
    String lockKey = LANGUAGE_FILE + key.toLowerCase();
    // intern()为每个唯一的字符序列生成一个且仅生成一个String引用
    String intern = lockKey.intern();
    // 锁定：同一String锁定，不同String之间不锁
    synchronized (intern) {
      valueMap = ReferenceHeapMediator.MEDIATOR.get(key);
      // 文件已经读取过，则不需要再次读取
      if (valueMap != null) {
        return valueMap;
      }
      valueMap = FileReader.loadFile(relativePath, langType, folderName, fileName);
      if (valueMap != null) {
        ReferenceHeapMediator.MEDIATOR.put(key, valueMap);
      }
    }
    return valueMap;
  }
}
