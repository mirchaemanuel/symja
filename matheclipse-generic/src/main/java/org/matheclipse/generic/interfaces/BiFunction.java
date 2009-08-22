/*
 * Copyright (C) 2007 Google Inc.
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

package org.matheclipse.generic.interfaces;

import com.google.common.base.Nullable;

/**
 * A BiFunction provides a transformation on two objects and returns the resulting
 * object.  For example, a {@code StringToIntegerFunction} may implement
 * <code>Function&lt;String,Integer&gt;</code> and transform integers in String
 * format to Integer format.
 *
 * <p>The transformation on the source objects does not necessarily result in
 * an object of a different type.  
 *
 * <p>Implementors of BiFunction which may cause side effects upon evaluation are
 * strongly encouraged to state this fact clearly in their API documentation.
 *
 */
public interface BiFunction<F1,F2,T> {

  /**
   * Applys the function to an object of types {@code F1} and {@code F2}, resulting in an object
   * of type {@code T}.  Note that types {@code F1}, {@code F2} and {@code T} may or may not
   * be the same.
   * 
   * @param from1 The first source object.
   * @param from2 The second source object.
   * @return The resulting object.
   */
  T apply(@Nullable F1 from1, @Nullable F2 from2);
}
