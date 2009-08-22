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
 * Static library that provides all the <b>bit level</b> operations for
 * {@link BigInteger}. The operations are:
 * <ul type="circle">
 * <li>Left Shifting</li>
 * <li>Right Shifting</li>
 * <li>Bit clearing</li>
 * <li>Bit setting</li>
 * <li>Bit counting</li>
 * <li>Bit testing</li>
 * <li>Getting of the lowest bit set</li>
 * </ul>
 * All operations are provided in immutable way, and some in both mutable and
 * immutable.
 * 
 * @author Intel Middleware Product Division
 * @author Instituto Tecnologico de Cordoba
 */
class BitLevel {

	/** Just to denote that this class can't be instantied. */
	private BitLevel() {
	}

	/** @see BigInteger#bitLength() */
	static int bitLength(BigInteger val) {
		if (val._sign == 0) {
			return 0;
		}
		int bLength = (val._size << 5);
		int highDigit = val._words[val._size - 1];

		if (val._sign < 0) {
			int i = val.getFirstNonzeroDigit();
			// We reduce the problem to the positive case.
			if (i == val._size - 1) {
				highDigit--;
			}
		}
		// Subtracting all sign bits
		bLength -= Integer.numberOfLeadingZeros(highDigit);
		return bLength;
	}

	/** @see BigInteger#bitCount() */
	static int bitCount(BigInteger val) {
		int bCount = 0;

		if (val._sign == 0) {
			return 0;
		}

		int i = val.getFirstNonzeroDigit();
		;
		if (val._sign > 0) {
			for (; i < val._size; i++) {
				checkCanceled();
				bCount += Integer.bitCount(val._words[i]);
			}
		} else {// (sign < 0)
			// this digit absorbs the carry
			bCount += Integer.bitCount(-val._words[i]);
			for (i++; i < val._size; i++) {
				checkCanceled();
				bCount += Integer.bitCount(~val._words[i]);
			}
			// We take the complement sum:
			bCount = (val._size << 5) - bCount;
		}
		return bCount;
	}

	/**
	 * Performs a fast bit testing for positive numbers. The bit to to be tested
	 * must be in the range {@code [0, val.bitLength()-1]}
	 */
	static boolean testBit(BigInteger val, int n) {
		// PRE: 0 <= n < val.bitLength()
		return ((val._words[n >> 5] & (1 << (n & 31))) != 0);
	}

	/**
	 * Check if there are 1s in the lowest bits of this BigInteger
	 * 
	 * @param numberOfBits
	 *            the number of the lowest bits to check
	 * @return false if all bits are 0s, true otherwise
	 */
	static boolean nonZeroDroppedBits(int numberOfBits, int digits[]) {
		int intCount = numberOfBits >> 5;
		int bitCount = numberOfBits & 31;
		int i;

		for (i = 0; (i < intCount) && (digits[i] == 0); i++) {
			checkCanceled();
			;
		}
		return ((i != intCount) || (digits[i] << (32 - bitCount) != 0));
	}

	/** @see BigInteger#shiftLeft(int) */
	static BigInteger shiftLeft(BigInteger source, int count) {
		int intCount = count >> 5;
		count &= 31; // %= 32
		int resLength = source._size + intCount + ((count == 0) ? 0 : 1);
		if (Config.SERVER_MODE) {
			if (Config.BIGINTEGER_MAX_SIZE < resLength) {
				throw new ObjectMemoryExceededException("BigInteger", resLength);
			}
		}
		int resDigits[] = new int[resLength];

		shiftLeft(resDigits, source._words, intCount, count);
		// axelclk
		BigInteger result = BigInteger.newInstance(source._sign, resLength,
				resDigits);
		// BigInteger result = new BigInteger(source._sign, resLength,
		// resDigits);
		result.cutOffLeadingZeroes();
		return result;
	}

	/**
	 * Performs {@code val <<= count}.
	 */
	// val should have enough place (and one digit more)
	static void inplaceShiftLeft(BigInteger val, int count) {
		int intCount = count >> 5; // count of integers
		val._size += intCount
				+ (Integer.numberOfLeadingZeros(val._words[val._size - 1])
						- (count & 31) >= 0 ? 0 : 1);
		shiftLeft(val._words, val._words, intCount, count & 31);
		val.cutOffLeadingZeroes();
		val.unCache();
	}

	/**
	 * Abstractly shifts left an array of integers in little endian (i.e. shift
	 * it right). Total shift distance in bits is intCount * 32 + count
	 * 
	 * @param result
	 *            the destination array
	 * @param source
	 *            the source array
	 * @param intCount
	 *            the shift distance in integers
	 * @param count
	 *            an additional shift distance in bits
	 */
	static void shiftLeft(int result[], int source[], int intCount, int count) {
		if (count == 0) {
			System.arraycopy(source, 0, result, intCount, result.length
					- intCount);
		} else {
			int rightShiftCount = 32 - count;

			result[result.length - 1] = 0;
			for (int i = result.length - 1; i > intCount; i--) {
				checkCanceled();
				result[i] |= source[i - intCount - 1] >>> rightShiftCount;
				result[i - 1] = source[i - intCount - 1] << count;
			}
		}

		for (int i = 0; i < intCount; i++) {
			checkCanceled();
			result[i] = 0;
		}
	}

	/** @see BigInteger#shiftRight(int) */
	static BigInteger shiftRight(BigInteger source, int count) {
		int intCount = count >> 5; // count of integers
		count &= 31; // count of remaining bits
		if (intCount >= source._size) {
			return ((source._sign < 0) ? BigInteger.MINUS_ONE : BigInteger.ZERO);
		}
		int i;
		int resLength = source._size - intCount;
		if (Config.SERVER_MODE) {
			if (Config.BIGINTEGER_MAX_SIZE < resLength + 1) {
				throw new ObjectMemoryExceededException("BigInteger",
						resLength + 1);
			}
		}
		int resDigits[] = new int[resLength + 1];

		shiftRight(resDigits, resLength, source._words, intCount, count);
		if (source._sign < 0) {
			// Checking if the dropped bits are zeros (the remainder equals to
			// 0)
			for (i = 0; (i < intCount) && (source._words[i] == 0); i++) {
				checkCanceled();
				;
			}
			// If the remainder is not zero, add 1 to the result
			if ((i < intCount)
					|| ((count > 0) && ((source._words[i] << (32 - count)) != 0))) {
				for (i = 0; (i < resLength) && (resDigits[i] == -1); i++) {
					checkCanceled();
					resDigits[i] = 0;
				}
				if (i == resLength) {
					resLength++;
				}
				resDigits[i]++;
			}
		}
		// axelclk
		BigInteger result = BigInteger.newInstance(source._sign, resLength,
				resDigits);
		// BigInteger result = new BigInteger(source._sign, resLength,
		// resDigits);
		result.cutOffLeadingZeroes();
		return result;
	}

	/**
	 * Performs {@code val >>= count} where {@code val} is a positive number.
	 */
	static void inplaceShiftRight(BigInteger val, int count) {
		int sign = val.signum();
		if (count == 0 || val.signum() == 0)
			return;
		int intCount = count >> 5; // count of integers
		val._size -= intCount;
		if (!shiftRight(val._words, val._size, val._words, intCount, count & 31)
				&& sign < 0) {
			// remainder not zero: add one to the result
			int i;
			for (i = 0; (i < val._size) && (val._words[i] == -1); i++) {
				checkCanceled();
				val._words[i] = 0;
			}
			if (i == val._size) {
				val._size++;
			}
			val._words[i]++;
		}
		val.cutOffLeadingZeroes();
		val.unCache();
	}

	/**
	 * Shifts right an array of integers. Total shift distance in bits is
	 * intCount * 32 + count.
	 * 
	 * @param result
	 *            the destination array
	 * @param resultLen
	 *            the destination array's length
	 * @param source
	 *            the source array
	 * @param intCount
	 *            the number of elements to be shifted
	 * @param count
	 *            the number of bits to be shifted
	 * @return dropped bit's are all zero (i.e. remaider is zero)
	 */
	static boolean shiftRight(int result[], int resultLen, int source[],
			int intCount, int count) {
		int i;
		boolean allZero = true;
		for (i = 0; i < intCount; i++)
			allZero &= source[i] == 0;
		if (count == 0) {
			System.arraycopy(source, intCount, result, 0, resultLen);
			i = resultLen;
		} else {
			int leftShiftCount = 32 - count;

			allZero &= (source[i] << leftShiftCount) == 0;
			for (i = 0; i < resultLen - 1; i++) {
				checkCanceled();
				result[i] = (source[i + intCount] >>> count)
						| (source[i + intCount + 1] << leftShiftCount);
			}
			result[i] = (source[i + intCount] >>> count);
			i++;
		}

		return allZero;
	}

	/**
	 * Performs a flipBit on the BigInteger, returning a BigInteger with the the
	 * specified bit flipped.
	 * 
	 * @param intCount:
	 *            the index of the element of the digits array where the
	 *            operation will be performed
	 * @param bitNumber:
	 *            the bit's position in the intCount element
	 */
	static BigInteger flipBit(BigInteger val, int n) {
		int resSign = (val._sign == 0) ? 1 : val._sign;
		int intCount = n >> 5;
		int bitN = n & 31;
		int resLength = Math.max(intCount + 1, val._size) + 1;
		if (Config.SERVER_MODE) {
			if (Config.BIGINTEGER_MAX_SIZE < resLength) {
				throw new ObjectMemoryExceededException("BigInteger", resLength);
			}
		}
		int resDigits[] = new int[resLength];
		int i;

		int bitNumber = 1 << bitN;
		System.arraycopy(val._words, 0, resDigits, 0, val._size);

		if (val._sign < 0) {
			if (intCount >= val._size) {
				resDigits[intCount] = bitNumber;
			} else {
				// val.sign<0 y intCount < val.numberLength
				int firstNonZeroDigit = val.getFirstNonzeroDigit();
				if (intCount > firstNonZeroDigit) {
					resDigits[intCount] ^= bitNumber;
				} else if (intCount < firstNonZeroDigit) {
					resDigits[intCount] = -bitNumber;
					for (i = intCount + 1; i < firstNonZeroDigit; i++) {
						checkCanceled();
						resDigits[i] = -1;
					}
					resDigits[i] = resDigits[i]--;
				} else {
					i = intCount;
					resDigits[i] = -((-resDigits[intCount]) ^ bitNumber);
					if (resDigits[i] == 0) {
						for (i++; resDigits[i] == -1; i++) {
							checkCanceled();
							resDigits[i] = 0;
						}
						resDigits[i]++;
					}
				}
			}
		} else {// case where val is positive
			resDigits[intCount] ^= bitNumber;
		}
		// axelclk
		BigInteger result = BigInteger.newInstance(resSign, resLength,
				resDigits);
		// BigInteger result = new BigInteger(resSign, resLength, resDigits);
		result.cutOffLeadingZeroes();
		return result;
	}
}
