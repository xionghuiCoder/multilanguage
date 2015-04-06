package com.sohu.multilanguage.util;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局缓存，可以防止内存泄露；<br />
 * 用于控制软引用、弱引用、最大堆和最小堆之间的交互
 *
 * @author XiongHui
 */
public enum ReferenceHeapMediator {
  MEDIATOR;

  private final Lock lock = new ReentrantLock();

  // 软引用标记
  private SoftReference<Object> softObjectReference = new SoftReference<Object>(new Object());
  private Map<String, MapCarrier> softMap = new HashMap<String, MapCarrier>();
  private BinaryHeap minHeap = new MinBinaryHeap();

  // 弱引用标记
  private WeakReference<Object> weakObjectReference = new WeakReference<Object>(new Object());
  private Map<String, MapCarrier> weakMap = new HashMap<String, MapCarrier>();
  private BinaryHeap maxHeap = new MaxBinaryHeap();

  /**
   * 查找map<br />
   * 普通情况下时间复杂度为O(log(n))，若发生了垃圾回收，时间复杂度为O(n*log(n))
   *
   * @param key
   * @return
   */
  public Map<String, String> get(String key) {
    lock.lock();
    try {
      reBuildReference();
      MapCarrier mapCarry = softMap.get(key);
      if (mapCarry == null) {
        mapCarry = weakMap.get(key);
      }
      Map<String, String> map = (mapCarry == null ? null : mapCarry.getMap());
      if (map != null) {
        boolean needLift = mapCarry.incrementCount();
        if (needLift) {
          tryExchange();
        }
      }
      reBalance();
      return map;
    } finally {
      lock.unlock();
    }
  }

  /**
   * 添加一个新map<br />
   * 时间复杂度为O(n*log(n))
   *
   * @param key
   * @param map
   */
  public void put(String key, Map<String, String> map) {
    lock.lock();
    try {
      reBuildReference();
      MapCarrier mapCarry = new MapCarrier(key, false, map, maxHeap);
      maxHeap.add(mapCarry);
      weakMap.put(key, mapCarry);
      reBalance();
    } finally {
      lock.unlock();
    }
  }

  /**
   * 尝试重新构建引用
   */
  private void reBuildReference() {
    Object obj = softObjectReference.get();
    // 是否回收了软引用
    if (obj == null) {
      // 释放soft内存
      softMap = new HashMap<String, MapCarrier>();
      minHeap = new MinBinaryHeap();
      softObjectReference = new SoftReference<Object>(new Object());
    }
    obj = weakObjectReference.get();
    // 是否回收了弱引用
    if (obj == null) {
      // 释放weak内存
      weakMap = new HashMap<String, MapCarrier>();
      maxHeap = new MaxBinaryHeap();
      weakObjectReference = new WeakReference<Object>(new Object());
    }
  }

  /**
   * 尝试交换最大堆(weak堆)的最大元素和最小堆(soft堆)的最小元素<br />
   * 下面两种情况下不进行交换：<br />
   * 1、最大堆的最大元素的count小于最小堆的最小元素的count<br />
   * 2、软引用或弱引用被回收。
   */
  private void tryExchange() {
    MapCarrier minTop = minHeap.getPop();
    MapCarrier maxTop = maxHeap.getPop();
    if (minTop == null || maxTop == null) {
      return;
    }
    // 强引用map
    Map<String, String> minMap = minTop.getMap();
    Map<String, String> maxMap = maxTop.getMap();
    if (minMap != null && maxMap != null & minTop.compareTo(maxTop) < 0) {
      String minKey = minTop.getKey();
      weakMap.put(minKey, minTop);
      softMap.remove(minKey);
      String maxKey = maxTop.getKey();
      softMap.put(maxKey, maxTop);
      weakMap.remove(maxKey);
      minTop.setHeap(maxHeap);
      minTop.weak();
      maxTop.setHeap(minHeap);
      maxTop.soft();
      minHeap.replaceTop(maxTop);
      maxHeap.replaceTop(minTop);
    }
  }

  /**
   * 平衡最大堆(weak堆)和最小堆(soft堆)<br />
   * 平衡后: weak堆的size-最小堆的size<=1
   */
  private void reBalance() {
    int incrementSize = softMap.size() - weakMap.size();
    // weak reference可能被回收
    if (incrementSize > 0) {
      incrementSize = ((incrementSize & 1) == 1 ? incrementSize / 2 + 1 : incrementSize / 2);
      do {
        MapCarrier top = minHeap.pop();
        // 强引用map
        Map<String, String> map = top.getMap();
        if (map == null) {
          return;
        }
        // 重新计数以便公平统计
        top.resetCount();
        top.setHeap(maxHeap);
        top.weak();
        maxHeap.add(top);
        String key = top.getKey();
        softMap.remove(key);
        weakMap.put(key, top);
        incrementSize--;
      } while (incrementSize > 0);
      // 最小堆的count重新计数以便公平统计
      for (MapCarrier c : minHeap) {
        if (c.getMap() == null) {
          return;
        }
        c.resetCount();
      }
    } else if (incrementSize < -1) {
      incrementSize = (-incrementSize) / 2;
      do {
        MapCarrier top = maxHeap.pop();
        // 强引用map
        Map<String, String> map = top.getMap();
        if (map == null) {
          return;
        }
        top.setHeap(minHeap);
        top.soft();
        minHeap.add(top);
        String key = top.getKey();
        weakMap.remove(key);
        softMap.put(key, top);
        incrementSize--;
      } while (incrementSize > 0);
    }
  }
}
