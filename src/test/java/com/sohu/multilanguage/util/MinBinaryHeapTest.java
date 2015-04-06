package com.sohu.multilanguage.util;


/**
 * 测试最小堆
 *
 * @author XiongHui
 */
public class MinBinaryHeapTest extends BinaryHeapSup {
  private static final BinaryHeap HEAP = new MinBinaryHeap();

  public void testAll() throws Exception {
    allTest(HEAP);
  }
}
