/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package apache.harmony.math;

import static org.matheclipse.basic.Util.*;

import org.matheclipse.basic.Config;
import org.matheclipse.basic.ObjectMemoryExceededException;

/**
 * Static library that provides the basic arithmetic mutable operations for
 * {@link BigInteger}. The operations provided are listed below.
 * <ul type="circle">
 * <li>Addition.</li>
 * <li>Subtraction.</li>
 * <li>Comparison.</li>
 * </ul>
 * In addition to this, some <i><b>Inplace</b></i> (mutable) methods are
 * provided.
 * 
 * @author Intel Middleware Product Division
 * @author Instituto Tecnologico de Cordoba
 */
class Elementary {

	/** Just to denote that this class can't be instantied */
	private Elementary() {
	}

	/**
	 * Compares two arrays. All elements are treated as unsigned integers. The
	 * magnitude is the bit chain of elements in big-endian order.
	 * 
	 * @param a
	 *            the first array
	 * @param b
	 *            the second array
	 * @param size
	 *            the size of arrays
	 * @return 1 if a > b, -1 if a < b, 0 if a == b
	 */
	static int compareArrays(final int[] a, final int[] b, final int size) {
		int i;
		for (i = size - 1; (i >= 0) && (a[i] == b[i]); i--) {
			checkCanceled();
			;
		}
		return ((i < 0) ? BigInteger.EQUALS
				: (a[i] & 0xFFFFFFFFL) < (b[i] & 0xFFFFFFFFL) ? BigInteger.LESS
						: BigInteger.GREATER);
	}

	/** @see BigInteger#plus(BigInteger) */
	static BigInteger add(BigInteger op1, BigInteger op2) {
		int resDigits[];
		int resSign;
		int op1Sign = op1._sign;
		int op2Sign = op2._sign;

		if (op1Sign == 0) {
			return op2;
		}
		if (op2Sign == 0) {
			return op1;
		}
		int op1Len = op1._size;
		int op2Len = op2._size;

		if (op1Len + op2Len == 2) {
			long a = (op1._words[0] & 0xFFFFFFFFL);
			long b = (op2._words[0] & 0xFFFFFFFFL);
			long res;
			int valueLo;
			int valueHi;

			if (op1Sign == op2Sign) {
				res = a + b;
				valueLo = (int) res;
				valueHi = (int) (res >>> 32);
				// axelclk
				return ((valueHi == 0) ? BigInteger.newInstance(op1Sign,
						valueLo) : BigInteger.newInstance(op1Sign, 2,
						new int[] { valueLo, valueHi }));
				// return ((valueHi == 0) ? new BigInteger(op1Sign, valueLo)
				// : new BigInteger(op1Sign, 2, new int[] { valueLo,
				// valueHi }));
			}
			return BigInteger.valueOf((op1Sign < 0) ? (b - a) : (a - b));
		} else if (op1Sign == op2Sign) {
			resSign = op1Sign;
			// an augend should not be shorter than addend
			resDigits = (op1Len >= op2Len) ? add(op1._words, op1Len,
					op2._words, op2Len) : add(op2._words, op2Len, op1._words,
					op1Len);
		} else { // signs are different
			int cmp = ((op1Len != op2Len) ? ((op1Len > op2Len) ? 1 : -1)
					: compareArrays(op1._words, op2._words, op1Len));

			if (cmp == BigInteger.EQUALS) {
				return BigInteger.ZERO;
			}
			// a minuend should not be shorter than subtrahend
			if (cmp == BigInteger.GREATER) {
				resSign = op1Sign;
				resDigits = subtract(op1._words, op1Len, op2._words, op2Len);
			} else {
				resSign = op2Sign;
				resDigits = subtract(op2._words, op2Len, op1._words, op1Len);
			}
		}
		// axelclk
		BigInteger res = BigInteger.newInstance(resSign, resDigits.length,
				resDigits);
		// BigInteger res = new BigInteger(resSign, resDigits.length,
		// resDigits);
		res.cutOffLeadingZeroes();
		return res;
	}

	/**
	 * Performs {@code res = a + b}.
	 */
	private static void add(int res[], int a[], int aSize, int b[], int bSize) {
		// PRE: a.length < max(aSize, bSize)

		int i;
		long carry = (a[0] & 0xFFFFFFFFL) + (b[0] & 0xFFFFFFFFL);

		res[0] = (int) carry;
		carry >>= 32;

		if (aSize >= bSize) {
			for (i = 1; i < bSize; i++) {
				checkCanceled();
				carry += (a[i] & 0xFFFFFFFFL) + (b[i] & 0xFFFFFFFFL);
				res[i] = (int) carry;
				carry >>= 32;
			}
			for (; i < aSize; i++) {
				checkCanceled();
				carry += a[i] & 0xFFFFFFFFL;
				res[i] = (int) carry;
				carry >>= 32;
			}
		} else {
			for (i = 1; i < aSize; i++) {
				checkCanceled();
				carry += (a[i] & 0xFFFFFFFFL) + (b[i] & 0xFFFFFFFFL);
				res[i] = (int) carry;
				carry >>= 32;
			}
			for (; i < bSize; i++) {
				checkCanceled();
				carry += b[i] & 0xFFFFFFFFL;
				res[i] = (int) carry;
				carry >>= 32;
			}
		}
		if (carry != 0) {
			res[i] = (int) carry;
		}
	}

	/** @see BigInteger#minus(BigInteger) */
	static BigInteger subtract(BigInteger op1, BigInteger op2) {
		int resSign;
		int resDigits[];
		int op1Sign = op1._sign;
		int op2Sign = op2._sign;

		if (op2Sign == 0) {
			return op1;
		}
		if (op1Sign == 0) {
			return op2.opposite();
		}
		int op1Len = op1._size;
		int op2Len = op2._size;
		if (op1Len + op2Len == 2) {
			long a = (op1._words[0] & 0xFFFFFFFFL);
			long b = (op2._words[0] & 0xFFFFFFFFL);
			if (op1Sign < 0) {
				a = -a;
			}
			if (op2Sign < 0) {
				b = -b;
			}
			return BigInteger.valueOf(a - b);
		}
		int cmp = ((op1Len != op2Len) ? ((op1Len > op2Len) ? 1 : -1)
				: Elementary.compareArrays(op1._words, op2._words, op1Len));

		if (cmp == BigInteger.LESS) {
			resSign = -op2Sign;
			resDigits = (op1Sign == op2Sign) ? subtract(op2._words, op2Len,
					op1._words, op1Len) : add(op2._words, op2Len, op1._words,
					op1Len);
		} else {
			resSign = op1Sign;
			if (op1Sign == op2Sign) {
				if (cmp == BigInteger.EQUALS) {
					return BigInteger.ZERO;
				}
				resDigits = subtract(op1._words, op1Len, op2._words, op2Len);
			} else {
				resDigits = add(op1._words, op1Len, op2._words, op2Len);
			}
		}
		// axelclk
		BigInteger res = BigInteger.newInstance(resSign, resDigits.length,
				resDigits);
		// BigInteger res = new BigInteger (resSign, resDigits.length,
		// resDigits);
		res.cutOffLeadingZeroes();
		return res;
	}

	/**
	 * Performs {@code res = a - b}. It is assumed the magnitude of a is not
	 * less than the magnitude of b.
	 */
	private static void subtract(int res[], int a[], int aSize, int b[],
			int bSize) {
		// PRE: a[] >= b[]
		int i;
		long borrow = 0;

		for (i = 0; i < bSize; i++) {
			checkCanceled();
			borrow += (a[i] & 0xFFFFFFFFL) - (b[i] & 0xFFFFFFFFL);
			res[i] = (int) borrow;
			borrow >>= 32; // -1 or 0
		}
		for (; i < aSize; i++) {
			checkCanceled();
			borrow += a[i] & 0xFFFFFFFFL;
			res[i] = (int) borrow;
			borrow >>= 32; // -1 or 0
		}
	}

	/**
	 * Addss the value represented by {@code b} to the value represented by
	 * {@code a}. It is assumed the magnitude of a is not less than the
	 * magnitude of b.
	 * 
	 * @return {@code a + b}
	 */
	private static int[] add(int a[], int aSize, int b[], int bSize) {
		// PRE: a[] >= b[]
		if (Config.SERVER_MODE) {
			if (Config.BIGINTEGER_MAX_SIZE < aSize + 1) {
				throw new ObjectMemoryExceededException("BigInteger", aSize + 1);
			}
		}
		int res[] = new int[aSize + 1];
		add(res, a, aSize, b, bSize);
		return res;
	}

	/**
	 * Performs {@code op1 += op2}. {@code op1} must have enough place to store
	 * the result (i.e. {@code op1.bitLength() >= op2.bitLength()}). Both
	 * should be positive (i.e. {@code op1 >= op2}).
	 * 
	 * @param op1
	 *            the input minuend, and the output result.
	 * @param op2
	 *            the addend
	 */
	static void inplaceAdd(BigInteger op1, BigInteger op2) {
		// PRE: op1 >= op2 > 0
		add(op1._words, op1._words, op1._size, op2._words, op2._size);
		op1._size = Math.min(Math.max(op1._size, op2._size) + 1,
				op1._words.length);
		op1.cutOffLeadingZeroes();
		op1.unCache();
	}

	/**
	 * Adds an integer value to the array of integers remembering carry.
	 * 
	 * @return a possible generated carry (0 or 1)
	 */
	static int inplaceAdd(int a[], final int aSize, final int addend) {
		long carry = addend & 0xFFFFFFFFL;

		for (int i = 0; (carry != 0) && (i < aSize); i++) {
			checkCanceled();
			carry += a[i] & 0xFFFFFFFFL;
			a[i] = (int) carry;
			carry >>= 32;
		}
		return (int) carry;
	}

	/**
	 * Performs: {@code op1 += addend}. The number must to have place to hold a
	 * possible carry.
	 */
	static void inplaceAdd(BigInteger op1, final int addend) {
		int carry = inplaceAdd(op1._words, op1._size, addend);
		if (carry == 1) {
			op1._words[op1._size] = 1;
			op1._size++;
		}
		op1.unCache();
	}

	/**
	 * Performs {@code op1 -= op2}. {@code op1} must have enough place to store
	 * the result (i.e. {@code op1.bitLength() >= op2.bitLength()}). Both
	 * should be positive (what implies that {@code op1 >= op2}).
	 * 
	 * @param op1
	 *            the input minuend, and the output result.
	 * @param op2
	 *            the subtrahend
	 */
	static void inplaceSubtract(BigInteger op1, BigInteger op2) {
		// PRE: op1 >= op2 > 0
		subtract(op1._words, op1._words, op1._size, op2._words, op2._size);
		op1.cutOffLeadingZeroes();
		op1.unCache();
	}

	/**
	 * Performs {@code res = b - a}
	 */
	private static void inverseSubtract(int res[], int a[], int aSize, int b[],
			int bSize) {
		int i;
		long borrow = 0;
		if (aSize < bSize) {
			for (i = 0; i < aSize; i++) {
				checkCanceled();
				borrow += (b[i] & 0xFFFFFFFFL) - (a[i] & 0xFFFFFFFFL);
				res[i] = (int) borrow;
				borrow >>= 32; // -1 or 0
			}
			for (; i < bSize; i++) {
				checkCanceled();
				borrow += b[i] & 0xFFFFFFFFL;
				res[i] = (int) borrow;
				borrow >>= 32; // -1 or 0
			}
		} else {
			for (i = 0; i < bSize; i++) {
				checkCanceled();
				borrow += (b[i] & 0xFFFFFFFFL) - (a[i] & 0xFFFFFFFFL);
				res[i] = (int) borrow;
				borrow >>= 32; // -1 or 0
			}
			for (; i < aSize; i++) {
				checkCanceled();
				borrow -= a[i] & 0xFFFFFFFFL;
				res[i] = (int) borrow;
				borrow >>= 32; // -1 or 0
			}
		}

	}

	/**
	 * Subtracts the value represented by {@code b} from the value represented
	 * by {@code a}. It is assumed the magnitude of a is not less than the
	 * magnitude of b.
	 * 
	 * @return {@code a - b}
	 */
	private static int[] subtract(int a[], int aSize, int b[], int bSize) {
		// PRE: a[] >= b[]
		int res[] = new int[aSize];
		subtract(res, a, aSize, b, bSize);
		return res;
	}

	/**
	 * Same as
	 * 
	 * @link #inplaceSubtract(BigInteger, BigInteger), but without the
	 *       restriction of non-positive values
	 * @param op1
	 *            should have enough space to save the result
	 * @param op2
	 */
	static void completeInPlaceSubtract(BigInteger op1, BigInteger op2) {
		int resultSign = op1.compareTo(op2);
		if (op1._sign == 0) {
			System.arraycopy(op2._words, 0, op1._words, 0, op2._size);
			op1._sign = -op2._sign;
		} else if (op1._sign != op2._sign) {
			add(op1._words, op1._words, op1._size, op2._words, op2._size);
			op1._sign = resultSign;
		} else {
			int sign = unsignedArraysCompare(op1._words, op2._words, op1._size,
					op2._size);
			if (sign > 0) {
				subtract(op1._words, op1._words, op1._size, op2._words,
						op2._size); // op1 = op1 - op2
				// op1.sign remains equal
			} else {
				inverseSubtract(op1._words, op1._words, op1._size, op2._words,
						op2._size); // op1 = op2 - op1
				op1._sign = -op1._sign;
			}
		}
		op1._size = Math.max(op1._size, op2._size) + 1;
		op1.cutOffLeadingZeroes();
		op1.unCache();
	}

	/**
	 * Same as
	 * 
	 * @link #inplaceAdd(BigInteger, BigInteger), but without the restriction of
	 *       non-positive values
	 * @param op1
	 *            any number
	 * @param op2
	 *            any number
	 */
	static void completeInPlaceAdd(BigInteger op1, BigInteger op2) {
		if (op1._sign == 0)
			System.arraycopy(op2._words, 0, op1._words, 0, op2._size);
		else if (op2._sign == 0)
			return;
		else if (op1._sign == op2._sign)
			add(op1._words, op1._words, op1._size, op2._words, op2._size);
		else {
			int sign = unsignedArraysCompare(op1._words, op2._words, op1._size,
					op2._size);
			if (sign > 0)
				subtract(op1._words, op1._words, op1._size, op2._words,
						op2._size);
			else {
				inverseSubtract(op1._words, op1._words, op1._size, op2._words,
						op2._size);
				op1._sign = -op1._sign;
			}
		}
		op1._size = Math.max(op1._size, op2._size) + 1;
		op1.cutOffLeadingZeroes();
		op1.unCache();
	}

	/**
	 * Compares two arrays, representing unsigned integer in little-endian
	 * order. Returns +1,0,-1 if a is - respective - greater, equal or lesser
	 * then b
	 */
	private static int unsignedArraysCompare(int[] a, int[] b, int aSize,
			int bSize) {
		if (aSize > bSize)
			return 1;
		else if (aSize < bSize)
			return -1;

		else {
			int i;
			for (i = aSize - 1; i >= 0 && a[i] == b[i]; i--)
				;
			return i < 0 ? BigInteger.EQUALS
					: ((a[i] & 0xFFFFFFFFL) < (b[i] & 0xFFFFFFFFL) ? BigInteger.LESS
							: BigInteger.GREATER);
		}
	}

}
