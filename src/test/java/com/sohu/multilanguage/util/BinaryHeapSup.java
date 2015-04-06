package com.sohu.multilanguage.util;

import java.lang.reflect.Field;
import java.util.Random;

import junit.framework.TestCase;

/**
 * 二项堆测试抽象类
 *
 * @author XiongHui
 */
public class BinaryHeapSup extends TestCase {
  private static final int COUNT = 10;

  private static final Random RANDOM = new Random();

  public void testNone() {

  }

  protected void allTest(BinaryHeap heap) throws Exception {
    addTest(heap);
    replaceTopTest(heap);
    popTest(heap);
    removeTest(heap);
  }

  private void addTest(BinaryHeap heap) throws Exception {
    System.out.println("test add begin");
    for (int i = 0; i < COUNT; i++) {
      System.out.println("now size is: " + heap.size());
      heap.add(buildMapCarrier());
    }
    System.out.println("now size is: " + heap.size());
    printHeap(heap);
    System.out.println("test add end");
    System.out.println();
  }

  private void replaceTopTest(BinaryHeap heap) throws Exception {
    System.out.println("test replace pop begin");
    for (int i = 0; i < COUNT; i++) {
      System.out.println("now size is: " + heap.size());
      printHeap(heap);
      heap.replaceTop(buildMapCarrier());
    }
    System.out.println("now size is: " + heap.size());
    System.out.println("test replace pop end");
  }

  private void popTest(BinaryHeap heap) throws Exception {
    System.out.println("test pop begin");
    while (heap.size() > 0) {
      System.out.println("now size is: " + heap.size());
      printHeap(heap);
      heap.pop();
    }
    System.out.println("now size is: " + heap.size());
    System.out.println("test pop end");
  }

  private void removeTest(BinaryHeap heap) throws Exception {
    System.out.println("test remove begin");
    for (int i = 0; i < COUNT; i++) {
      heap.add(buildMapCarrier());
    }
    for (int i = 0; i < COUNT; i++) {
      System.out.println("now size is: " + heap.size());
      printHeap(heap);
      heap.remove(1);
    }
    System.out.println("now size is: " + heap.size());
    System.out.println("test remove end");
  }

  /**
   * 利用反射创建一个新MapCarrier
   *
   * @return
   * @throws Exception
   */
  private MapCarrier buildMapCarrier() throws Exception {
    MapCarrier mapCarry = new MapCarrier(null, false, null, null);
    int count = RANDOM.nextInt(99) + 1;
    Field coutField = MapCarrier.class.getDeclaredField("count");
    coutField.setAccessible(true);
    coutField.set(mapCarry, count);
    System.out.println("new count is: " + count);
    return mapCarry;
  }

  /**
   * 格式化输出元素
   *
   * @param heap
   * @throws Exception
   */
  private void printHeap(BinaryHeap heap) throws Exception {
    int size = heap.size();
    int high = 0, tmp = 1;
    while (size > 0) {
      size -= tmp;
      high++;
      tmp *= 2;
    }
    int next = 1, incremnt = 0;
    for (MapCarrier mapCarry : heap) {
      Field coutField = MapCarrier.class.getDeclaredField("count");
      coutField.setAccessible(true);
      int count = (Integer) coutField.get(mapCarry);
      System.out.print(count);
      if (count < 10) {
        System.out.print("*");
      }
      int pow = (int) Math.pow(2, high - 1);
      for (int i = 0; i < pow + tmp - 1; i++) {
        System.out.print("**");
      }
      incremnt++;
      if (incremnt == next) {
        next *= 2;
        incremnt = 0;
        tmp = tmp / 2;
        high--;
        System.out.println();
      }
    }
    System.out.println();
  }
}
