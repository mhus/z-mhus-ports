/*
 * Copyright (C) 2007 The Guava Authors
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

import com.google.gwt.thirdparty.guava.common.annotations.GwtCompatible;
import com.google.gwt.thirdparty.guava.common.annotations.GwtIncompatible;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Multiset implementation backed by a {@link HashMap}.
 *
 * @author Kevin Bourrillion
 * @author Jared Levy
 * @since 2.0 (imported from Google Collections Library)
 */
@GwtCompatible(serializable = true, emulated = true)
public final class HashMultiset<E> extends AbstractMapBasedMultiset<E> {

  /**
   * Creates a new, empty {@code HashMultiset} using the default initial
   * capacity.
   */
  public static <E> HashMultiset<E> create() {
    return new HashMultiset<E>();
  }

  /**
   * Creates a new, empty {@code HashMultiset} with the specified expected
   * number of distinct elements.
   *
   * @param distinctElements the expected number of distinct elements
   * @throws IllegalArgumentException if {@code distinctElements} is negative
   */
  public static <E> HashMultiset<E> create(int distinctElements) {
    return new HashMultiset<E>(distinctElements);
  }

  /**
   * Creates a new {@code HashMultiset} containing the specified elements.
   * 
   * <p>This implementation is highly efficient when {@code elements} is itself
   * a {@link Multiset}.
   * 
   * @param elements the elements that the multiset should contain
   */
  public static <E> HashMultiset<E> create(Iterable<? extends E> elements) {
    HashMultiset<E> multiset =
        create(Multisets.inferDistinctElements(elements));
    Iterables.addAll(multiset, elements);
    return multiset;
  }

  private HashMultiset() {
    super(new HashMap<E, Count>());
  }

  private HashMultiset(int distinctElements) {
    super(Maps.<E, Count>newHashMapWithExpectedSize(distinctElements));
  }

  /**
   * @serialData the number of distinct elements, the first element, its count,
   *     the second element, its count, and so on
   */
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    Serialization.writeMultiset(this, stream);
  }

  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    int distinctElements = Serialization.readCount(stream);
    setBackingMap(
        Maps.<E, Count>newHashMapWithExpectedSize(distinctElements));
    Serialization.populateMultiset(this, stream, distinctElements);
  }

  @GwtIncompatible("Not needed in emulated source.")
  private static final long serialVersionUID = 0;
}
