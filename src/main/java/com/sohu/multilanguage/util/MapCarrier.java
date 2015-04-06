package com.sohu.multilanguage.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * map的包装类，用于控制软、弱引用，map的键值以及最大、最小堆
 *
 * @author XiongHui
 */
class MapCarrier implements Comparable<MapCarrier> {
  private final String key;

  // fileMap的弱引用或软引用
  private Reference<Map<String, String>> mapReference;

  private BinaryHeap heap;

  private int position;

  private int count = 1;

  MapCarrier(String key, boolean isSoft, Map<String, String> map, BinaryHeap heap) {
    this.key = key;
    if (isSoft) {
      mapReference = new SoftReference<Map<String, String>>(map);
    } else {
      mapReference = new WeakReference<Map<String, String>>(map);
    }
    this.heap = heap;
  }

  public String getKey() {
    return key;
  }

  public Map<String, String> getMap() {
    return mapReference.get();
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public void resetCount() {
    count = 1;
  }

  public boolean incrementCount() {
    // 上限
    if (count == Integer.MAX_VALUE) {
      return false;
    }
    count++;
    return heap.locate(position);
  }

  public void setHeap(BinaryHeap heap) {
    this.heap = heap;
  }

  // 降级为弱引用
  public void weak() {
    mapReference = new WeakReference<Map<String, String>>(getMap());
  }

  // 升级为软引用
  public void soft() {
    mapReference = new SoftReference<Map<String, String>>(getMap());
  }

  @Override
  public int compareTo(MapCarrier c) {
    return count - c.count;
  }
}
