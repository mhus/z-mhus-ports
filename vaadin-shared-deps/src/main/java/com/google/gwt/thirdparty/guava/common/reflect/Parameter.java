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

package com.google.gwt.thirdparty.guava.common.reflect;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import com.google.gwt.thirdparty.guava.common.annotations.Beta;
import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import javax.annotation.Nullable;

/**
 * Represents a method or constructor parameter.
 *
 * @author Ben Yu
 * @since 14.0
 */
@Beta
public final class Parameter implements AnnotatedElement {

  private final Invokable<?, ?> declaration;
  private final int position;
  private final TypeToken<?> type;
  private final ImmutableList<Annotation> annotations;

  Parameter(
      Invokable<?, ?> declaration,
      int position,
      TypeToken<?> type,
      Annotation[] annotations) {
    this.declaration = declaration;
    this.position = position;
    this.type = type;
    this.annotations = ImmutableList.copyOf(annotations);
  }

  /** Returns the type of the parameter. */
  public TypeToken<?> getType() {
    return type;
  }

  /** Returns the {@link Invokable} that declares this parameter. */
  public Invokable<?, ?> getDeclaringInvokable() {
    return declaration;
  }

  @Override public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
    return getAnnotation(annotationType) != null;
  }

  @Override
  @Nullable
  public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
    checkNotNull(annotationType);
    for (Annotation annotation : annotations) {
      if (annotationType.isInstance(annotation)) {
        return annotationType.cast(annotation);
      }
    }
    return null;
  }

  @Override public Annotation[] getAnnotations() {
    return getDeclaredAnnotations();
  }

  @Override public Annotation[] getDeclaredAnnotations() {
    return annotations.toArray(new Annotation[annotations.size()]);
  }

  @Override public boolean equals(@Nullable Object obj) {
    if (obj instanceof Parameter) {
      Parameter that = (Parameter) obj;
      return position == that.position && declaration.equals(that.declaration);
    }
    return false;
  }

  @Override public int hashCode() {
    return position;
  }

  @Override public String toString() {
    return type + " arg" + position;
  }
}
