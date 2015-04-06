package com.sohu.multilanguage.util;

/**
 * 最大堆，用于存储weak map的的数据
 *
 * @author XiongHui
 */
class MaxBinaryHeap extends BinaryHeap {
  MaxBinaryHeap() {
    super();
  }

  MaxBinaryHeap(int initialCapacity) {
    super(initialCapacity);
  }

  @Override
  public boolean locate(int k) {
    while (k > 1) {
      int j = k >> 1;
      if (compare(queue[j], queue[k])) {
        return false;
      }
      MapCarrier tmp = queue[j];
      queue[j] = queue[k];
      queue[j].setPosition(j);
      queue[k] = tmp;
      queue[k].setPosition(k);
      k = j;
    }
    return true;
  }

  @Override
  public boolean compare(MapCarrier fCover, MapCarrier sCover) {
    return fCover.compareTo(sCover) >= 0;
  }
}
