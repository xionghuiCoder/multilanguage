package com.sohu.multilanguage.util;

/**
 * 最小堆，用于存储soft map的的数据
 *
 * @author XiongHui
 */
class MinBinaryHeap extends BinaryHeap {
  MinBinaryHeap() {
    super();
  }

  MinBinaryHeap(int initialCapacity) {
    super(initialCapacity);
  }

  @Override
  public boolean locate(int k) {
    fixDown(k);
    return false;
  }

  @Override
  public boolean compare(MapCarrier fCover, MapCarrier sCover) {
    return fCover.compareTo(sCover) <= 0;
  }
}
