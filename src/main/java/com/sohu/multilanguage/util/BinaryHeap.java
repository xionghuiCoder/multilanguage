package com.sohu.multilanguage.util;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 抽象二项堆
 *
 * @author XiongHui
 */
abstract class BinaryHeap implements Iterable<MapCarrier> {
  private static final int MAXIMUM_CAPACITY = 1 << 30;

  private static final int DEFAULT_INITIAL_CAPACITY = 16;

  protected MapCarrier[] queue;

  private volatile int size;

  BinaryHeap() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  BinaryHeap(int initialCapacity) {
    if (initialCapacity <= 0) {
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    }
    // Find a power of 2 >= initialCapacity
    initialCapacity = this.roundUpToPowerOf2(initialCapacity);
    queue = new MapCarrier[initialCapacity];
  }

  private int roundUpToPowerOf2(int number) {
    return number >= MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : number > 1 ? Integer
        .highestOneBit(number - 1 << 1) : 1;
  }

  /**
   * heap中增加一个新元素
   *
   * @param mapCarry
   */
  public void add(MapCarrier mapCarry) {
    if ((++size) == queue.length) {
      queue = Arrays.copyOf(queue, 2 * queue.length);
    }
    queue[size] = mapCarry;
    queue[size].setPosition(size);
    fixUp(size);
  }

  /**
   * 向上提升
   *
   * @param k
   */
  private void fixUp(int k) {
    while (k > 1) {
      int j = k >> 1;
      if (compare(queue[j], queue[k])) {
        break;
      }
      MapCarrier tmp = queue[j];
      queue[j] = queue[k];
      queue[j].setPosition(j);
      queue[k] = tmp;
      queue[k].setPosition(k);
      k = j;
    }
  }

  /**
   * 向下下降
   *
   * @param k
   */
  protected void fixDown(int k) {
    while (true) {
      int j = k << 1;
      if (j > size) {
        break;
      }
      int index = j;
      if (j == size) {
        if (compare(queue[k], queue[j])) {
          break;
        }
      } else {
        if (compare(queue[j + 1], queue[j])) {
          index = j + 1;
        }
        if (compare(queue[k], queue[index])) {
          break;
        }
      }
      MapCarrier tmp = queue[index];
      queue[index] = queue[k];
      queue[index].setPosition(index);
      queue[k] = tmp;
      queue[k].setPosition(k);
      k = index;
    }
  }

  /**
   * 获取顶端元素
   */
  public MapCarrier getPop() {
    return queue[1];
  }

  /**
   * 弹出顶端元素并重新调整结构
   */
  public MapCarrier pop() {
    if (size == 0) {
      return null;
    }
    MapCarrier top = queue[1];
    queue[1] = queue[size];
    queue[1].setPosition(1);
    queue[size--] = null;
    fixDown(1);
    return top;
  }

  /**
   * 替换顶端元素
   *
   * @param mapCarry
   */
  public void replaceTop(MapCarrier mapCarry) {
    queue[1] = mapCarry;
    queue[1].setPosition(1);
    fixDown(1);
  }

  /**
   * 删除position处的元素
   *
   * @param position
   */
  public void remove(int position) {
    if (position == 0) {
      return;
    }
    if (position < 0 || position > size) {
      throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + size);
    }
    if (position == size) {
      queue[size--] = null;
      return;
    }
    queue[position] = queue[size];
    queue[position].setPosition(position);
    queue[size--] = null;
    fixUp(position);
    fixDown(position);
  }

  /**
   * 返回二项堆的元素总数
   *
   * @return
   */
  public int size() {
    return size;
  }

  @Override
  public Iterator<MapCarrier> iterator() {
    return new Itr();
  }

  private class Itr implements Iterator<MapCarrier> {
    int cursor = 1;
    int lastRet = -1;

    @Override
    public boolean hasNext() {
      return cursor <= size;
    }

    @Override
    public MapCarrier next() {
      int i = cursor;
      if (i > size) {
        throw new NoSuchElementException();
      }
      cursor = i + 1;
      return queue[lastRet = i];
    }

    @Override
    public void remove() {
      if (lastRet < 0) {
        throw new IllegalStateException();
      }
      try {
        BinaryHeap.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
      } catch (IndexOutOfBoundsException ex) {
        throw new ConcurrentModificationException();
      }
    }
  }

  protected abstract boolean locate(int k);

  protected abstract boolean compare(MapCarrier fCover, MapCarrier sCover);
}
