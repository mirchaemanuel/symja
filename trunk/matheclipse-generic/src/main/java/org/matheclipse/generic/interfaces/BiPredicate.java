/*
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

import javax.annotation.Nullable;

/**
 * A BiPredicate can determine a true or false value for any input of its
 * parameterized types. 
 * 
 * <p>
 * Implementors of BiPredicate which may cause side effects upon evaluation are
 * strongly encouraged to state this fact clearly in their API documentation.
 * 
 * <p>
 * <b>NOTE:</b> This interface <i>could</i> technically extend
 * {@link BiFunction}, since a predicate is just a special case of a function (one
 * that returns a boolean). However, since implementing this would entail
 * changing the signature of the {@link #apply} method to return a
 * {@link Boolean} instead of a {@code boolean}, which would in turn allow
 * people to return {@code null} from their BiPredicate, which would in turn
 * enable code that looks like this
 * {@code if (myPredicate.apply(myObject)) ... } to throw a
 * {@link NullPointerException}, it was decided not to make this change.
 * 
 */
public interface BiPredicate<T> {
	/**
   * Applies this Predicate to the given objects.
   *
   * @return the value of this Predicate when applied to input {@code t1} and {@code t2}.
   */
	boolean apply(@Nullable T t1, @Nullable T t2);
}
