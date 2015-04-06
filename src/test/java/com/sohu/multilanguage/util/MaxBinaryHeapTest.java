package com.sohu.multilanguage.util;

/**
 * 测试最大堆
 *
 * @author XiongHui
 */
public class MaxBinaryHeapTest extends BinaryHeapSup {
  private static final BinaryHeap HEAP = new MaxBinaryHeap();

  public void testAll() throws Exception {
    allTest(HEAP);
  }
}
