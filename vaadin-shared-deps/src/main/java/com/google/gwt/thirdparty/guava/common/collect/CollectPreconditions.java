/*
 * Copyright (C) 2008 The Guava Authors
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

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkState;

import com.google.gwt.thirdparty.guava.common.annotations.GwtCompatible;

/**
 * Precondition checks useful in collection implementations.
 */
@GwtCompatible
final class CollectPreconditions {

  static void checkEntryNotNull(Object key, Object value) {
    if (key == null) {
      throw new NullPointerException("null key in entry: null=" + value);
    } else if (value == null) {
      throw new NullPointerException("null value in entry: " + key + "=null");
    }
  }

  static int checkNonnegative(int value, String name) {
    if (value < 0) {
      throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
    }
    return value;
  }

  /**
   * Precondition tester for {@code Iterator.remove()} that throws an exception with a consistent
   * error message.
   */
  static void checkRemove(boolean canRemove) {
    checkState(canRemove, "no calls to next() since the last call to remove()");
  }
}
