/*
 * Copyright (C) 2011 The Guava Authors
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

package com.google.gwt.thirdparty.guava.common.util.concurrent;

import com.google.gwt.thirdparty.guava.common.annotations.Beta;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

/**
 * Abstract {@link ListeningExecutorService} implementation that creates
 * {@link ListenableFutureTask} instances for each {@link Runnable} and {@link Callable} submitted
 * to it. These tasks are run with the abstract {@link #execute execute(Runnable)} method.
 *
 * <p>In addition to {@link #execute}, subclasses must implement all methods related to shutdown and
 * termination.
 *
 * @author Chris Povirk
 * @since 14.0
 */
@Beta
public abstract class AbstractListeningExecutorService
    extends AbstractExecutorService implements ListeningExecutorService {

  @Override protected final <T> ListenableFutureTask<T> newTaskFor(Runnable runnable, T value) {
    return ListenableFutureTask.create(runnable, value);
  }

  @Override protected final <T> ListenableFutureTask<T> newTaskFor(Callable<T> callable) {
    return ListenableFutureTask.create(callable);
  }

  @Override public ListenableFuture<?> submit(Runnable task) {
    return (ListenableFuture<?>) super.submit(task);
  }

  @Override public <T> ListenableFuture<T> submit(Runnable task, @Nullable T result) {
    return (ListenableFuture<T>) super.submit(task, result);
  }

  @Override public <T> ListenableFuture<T> submit(Callable<T> task) {
    return (ListenableFuture<T>) super.submit(task);
  }
}
