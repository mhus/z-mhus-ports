/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gwt.thirdparty.guava.common.collect;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkArgument;
import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import com.google.gwt.thirdparty.guava.common.annotations.Beta;
import com.google.gwt.thirdparty.guava.common.annotations.GwtIncompatible;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

/**
 * A non-blocking queue which automatically evicts elements from the head of the queue when
 * attempting to add new elements onto the queue and it is full.
 *
 * <p>An evicting queue must be configured with a maximum size. Each time an element is added
 * to a full queue, the queue automatically removes its head element. This is different from
 * conventional bounded queues, which either block or reject new elements when full.
 *
 * <p>This class is not thread-safe, and does not accept null elements.
 *
 * @author Kurt Alfred Kluever
 * @since 15.0
 */
@Beta
@GwtIncompatible("java.util.ArrayDeque")
public final class EvictingQueue<E> extends ForwardingQueue<E> {

  private final Queue<E> delegate;
  private final int maxSize;

  private EvictingQueue(int maxSize) {
    checkArgument(maxSize >= 0, "maxSize (%s) must >= 0", maxSize);
    this.delegate = new ArrayDeque<E>(maxSize);
    this.maxSize = maxSize;
  }

  /**
   * Creates and returns a new evicting queue that will hold up to {@code maxSize} elements.
   *
   * <p>When {@code maxSize} is zero, elements will be evicted immediately after being added to the
   * queue.
   */
  public static <E> EvictingQueue<E> create(int maxSize) {
    return new EvictingQueue<E>(maxSize);
  }

  @Override protected Queue<E> delegate() {
    return delegate;
  }

  /**
   * Adds the given element to this queue. If the queue is currently full, the element at the head
   * of the queue is evicted to make room.
   *
   * @return {@code true} always
   */
  @Override public boolean offer(E e) {
    return add(e);
  }

  /**
   * Adds the given element to this queue. If the queue is currently full, the element at the head
   * of the queue is evicted to make room.
   *
   * @return {@code true} always
   */
  @Override public boolean add(E e) {
    checkNotNull(e);  // check before removing
    if (maxSize == 0) {
      return true;
    }
    if (size() == maxSize) {
      delegate.remove();
    }
    delegate.add(e);
    return true;
  }

  @Override public boolean addAll(Collection<? extends E> collection) {
    return standardAddAll(collection);
  }

  @Override
  public boolean contains(Object object) {
    return delegate().contains(checkNotNull(object));
  }

  @Override
  public boolean remove(Object object) {
    return delegate().remove(checkNotNull(object));
  }

  // TODO(user): Do we want to checkNotNull each element in containsAll, removeAll, and retainAll?

  // TODO(user): Do we want to add EvictingQueue#isFull()?
}
