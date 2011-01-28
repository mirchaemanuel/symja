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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import org.matheclipse.basic.Config;
import org.matheclipse.basic.ObjectMemoryExceededException;

/**
 * @author Intel Middleware Product Division
 * @author Instituto Tecnologico de Cordoba
 */
public class BigInteger implements Comparable<BigInteger>, Serializable {
	// public class BigInteger extends Number<BigInteger> implements
	// Comparable<BigInteger>, Serializable {

	/** @ar.org.fitc.spec_ref */
	private static final long serialVersionUID = -8287574255936472291L;

	/* Fields used for the internal representation. */

	/** The magnitude of this in the little-endian representation. */
	transient int _words[];

	/** The length of this in measured in ints. Can be less than digits.length(). */
	transient int _size;

	/** The sign of this. */
	transient int _sign;

	/* Static Fields */

	/** @ar.org.fitc.spec_ref */
	public static final BigInteger ONE = new BigInteger(1, 1);

	/** @ar.org.fitc.spec_ref */
	public static final BigInteger TWO = new BigInteger(1, 2);
	 
	/** @ar.org.fitc.spec_ref */
	public static final BigInteger TEN = new BigInteger(1, 10);

	/** @ar.org.fitc.spec_ref */
	public static final BigInteger ZERO = new BigInteger(0, 0);

	/** The {@code BigInteger} constant -1. */
	public static final BigInteger MINUS_ONE = new BigInteger(-1, 1);

	/** The {@code BigInteger} constant 0 used for comparison. */
	static final int EQUALS = 0;

	/** The {@code BigInteger} constant 1 used for comparison. */
	static final int GREATER = 1;

	/** The {@code BigInteger} constant -1 used for comparison. */
	static final int LESS = -1;

	/** All the @ BigInteger} numbers in the range [0,10] are cached. */
	static final BigInteger[] SMALL_VALUES = { ZERO, ONE, new BigInteger(1, 2), new BigInteger(1, 3), new BigInteger(1, 4),
			new BigInteger(1, 5), new BigInteger(1, 6), new BigInteger(1, 7), new BigInteger(1, 8), new BigInteger(1, 9), TEN };

	/**/
	private transient int firstNonzeroDigit = -2;

	/* Serialized Fields */

	/** @ar.org.fitc.spec_ref */
	private int signum;

	/** @ar.org.fitc.spec_ref */
	private byte[] magnitude;

	// /////////////////////
	// Factory creation. //
	/**
	 * Holds factory for LargeInteger with variable size arrays.
	 */
	/**
	 * Holds factory for LargeInteger with variable size arrays.
	 */
	// private static final ArrayFactory<BigInteger> ARRAY_FACTORY = new
	// ArrayFactory<BigInteger>() {
	//
	// @Override
	// protected BigInteger create(int capacity) {
	// return new BigInteger(capacity);
	// }
	//
	// };
	/**
	 * Holds the factory for LargeInteger with no intrinsic array (wrapper
	 * instances).
	 */
	// private static final ObjectFactory<BigInteger> NO_ARRAY_FACTORY = new
	// ObjectFactory<BigInteger>() {
	//
	// @Override
	// protected BigInteger create() {
	// return new BigInteger();
	// }
	//
	// };
	/* package private */static BigInteger newInstance(int sign, int numberLength, int[] digits) {
		BigInteger z;
		// if (Config.SERVER_MODE) {
		// z = NO_ARRAY_FACTORY.object();
		// } else {
		z = new BigInteger();
		// }
		z._sign = sign;
		z._size = numberLength;
		z._words = digits;
		return z;
	}

	/* package private */static BigInteger newInstance(int sign, int value) {
		BigInteger z;
		// if (Config.SERVER_MODE) {
		// z = NO_ARRAY_FACTORY.object();
		// } else {
		z = new BigInteger();
		// }
		z._sign = sign;
		z._size = 1;
		z._words = new int[] { value };
		return z;
	}

	/* package private */static BigInteger newInstance(int sign, long val) {
		BigInteger z;
		// if (Config.SERVER_MODE) {
		// z = NO_ARRAY_FACTORY.object();
		// } else {
		z = new BigInteger();
		// }
		// PRE: (val >= 0) && (sign >= -1) && (sign <= 1)
		z._sign = sign;
		if ((val & 0xFFFFFFFF00000000L) == 0) {
			// It fits in one 'int'
			z._size = 1;
			z._words = new int[] { (int) val };
		} else {
			z._size = 2;
			z._words = new int[] { (int) val, (int) (val >> 32) };
		}
		return z;
	}

	/* package private */static BigInteger newInstance(int signum, int digits[]) {
		BigInteger z;
		// if (Config.SERVER_MODE) {
		// z = NO_ARRAY_FACTORY.object();
		// } else {
		z = new BigInteger();
		// }
		if (digits.length == 0) {
			z._sign = 0;
			z._size = 1;
			z._words = new int[] { 0 }; // axelclk
			// digits = new int[] { 0 }; // axelxlk
		} else {
			z._sign = signum;
			z._size = digits.length;
			z._words = digits;
			z.cutOffLeadingZeroes();
		}
		return z;
	}

	private BigInteger() {
	}

	/**
	 * Returns the big integer for the specified character sequence in decimal
	 * number.
	 * 
	 * @param chars
	 *          the character sequence.
	 * @return {@link #valueOf(CharSequence, int) valueOf(chars, 10)}
	 */
	public static BigInteger valueOf(String chars) {
		return valueOf(chars, 10);
	}

	/* Public Constructors */

	/** @ar.org.fitc.spec_ref */
	public BigInteger(int numBits, Random rnd) {
		if (numBits < 0) {
			// math.1B=numBits must be non-negative
			throw new IllegalArgumentException("BigInteger");// Messages.getString(
			// "math.1B"));
			// //$NON-NLS-1$
		}
		if (numBits == 0) {
			_sign = 0;
			_size = 1;
			_words = new int[] { 0 };
		} else {
			_sign = 1;
			_size = (numBits + 31) >> 5;
			if (Config.SERVER_MODE) {
				if (Config.BIGINTEGER_MAX_SIZE < _size) {
					throw new ObjectMemoryExceededException("BigInteger", _size);
				}
			}
			_words = new int[_size];
			for (int i = 0; i < _size; i++) {
				_words[i] = rnd.nextInt();
			}
			// Using only the necessary bits
			_words[_size - 1] >>>= (-numBits) & 31;
			cutOffLeadingZeroes();
		}
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger(int bitLength, int certainty, Random rnd) {
		if (bitLength < 2) {
			// math.1C=bitLength < 2
			throw new ArithmeticException("BigInteger");//Messages.getString("math.1C"
			// ));
			// //$NON-NLS-1$
		}
		BigInteger me = Primality.consBigInteger(bitLength, certainty, rnd);
		_sign = me._sign;
		_size = me._size;
		_words = me._words;
	}

	/** @ar.org.fitc.spec_ref */
	private BigInteger(String val) {
		this(val, 10);
	}

	/** @ar.org.fitc.spec_ref */
	private BigInteger(String val, int radix) {
		if (val == null) {
			throw new NullPointerException();
		}
		if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
			// math.11=Radix out of range
			throw new NumberFormatException("BigInteger");// Messages.getString(
			// "math.11"));
			// //$NON-NLS-1$
		}
		if (val.length() == 0) {
			// math.12=Zero length BigInteger
			throw new NumberFormatException("BigInteger");// Messages.getString(
			// "math.12"));
			// //$NON-NLS-1$
		}
		BigInteger me = Conversion.string2BigInteger(val, radix);
		_sign = me._sign;
		_size = me._size;
		_words = me._words;
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger(int signum, byte[] magnitude) {
		if (magnitude == null) {
			throw new NullPointerException();
		}
		if ((signum < -1) || (signum > 1)) {
			// math.13=Invalid signum value
			throw new NumberFormatException("BigInteger");// Messages.getString(
			// "math.13"));
			// //$NON-NLS-1$
		}
		if (signum == 0) {
			for (byte element : magnitude) {
				if (element != 0) {
					// math.14=signum-magnitude mismatch
					throw new NumberFormatException("BigInteger");// Messages.getString(
					// "math.14"));
					// //$NON-NLS-1$
				}
			}
		}
		if (magnitude.length == 0) {
			_sign = 0;
			_size = 1;
			_words = new int[] { 0 };
		} else {
			_sign = signum;
			putBytesPositiveToIntegers(magnitude);
			cutOffLeadingZeroes();
		}
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger(byte[] val) {
		if (val.length == 0) {
			// math.12=Zero length BigInteger
			throw new NumberFormatException("BigInteger");// Messages.getString(
			// "math.12"));
			// //$NON-NLS-1$
		}
		if (val[0] < 0) {
			_sign = -1;
			putBytesNegativeToIntegers(val);
		} else {
			_sign = 1;
			putBytesPositiveToIntegers(val);
		}
		cutOffLeadingZeroes();
	}

	/* Package Constructors */

	/**
	 * Constructs a number which array is of size 1.
	 * 
	 * @param sign
	 *          the sign of the number
	 * @param value
	 *          the only one digit of array
	 */
	private BigInteger(int sign, int value) {
		this._sign = sign;
		_size = 1;
		_words = new int[] { value };
	}

	/**
	 * Constructs a number without to create new space. This construct should be
	 * used only if the three fields of representation are known.
	 * 
	 * @param sign
	 *          the sign of the number
	 * @param numberLength
	 *          the length of the internal array
	 * @param digits
	 *          a reference of some array created before
	 */
	// private BigInteger(int sign, int numberLength, int[] digits) {
	// this._sign = sign;
	// this._size = numberLength;
	// this._words = digits;
	// }
	/**
	 * Creates a new {@code BigInteger} whose value is equal to the specified
	 * {@code long}.
	 * 
	 * @param sign
	 *          the sign of the number
	 * @param val
	 *          the value of the new {@code BigInteger}.
	 */
	private BigInteger(int sign, long val) {
		// PRE: (val >= 0) && (sign >= -1) && (sign <= 1)
		this._sign = sign;
		if ((val & 0xFFFFFFFF00000000L) == 0) {
			// It fits in one 'int'
			_size = 1;
			_words = new int[] { (int) val };
		} else {
			_size = 2;
			_words = new int[] { (int) val, (int) (val >> 32) };
		}
	}

	/**
	 * Creates a new {@code BigInteger} with the given sign and magnitude. This
	 * constructor does not create a copy, so any changes to the reference will
	 * affect the new number.
	 * 
	 * @param signum
	 *          The sign of the number represented by {@code digits}
	 * @param digits
	 *          The magnitude of the number
	 */
	// BigInteger(int signum, int digits[]) {
	// if (digits.length == 0) {
	// _sign = 0;
	// _size = 1;
	// digits = new int[] { 0 };
	// } else {
	// _sign = signum;
	// _size = digits.length;
	// this._words = digits;
	// cutOffLeadingZeroes();
	// }
	// }
	/* Public Methods */

	/** @ar.org.fitc.spec_ref */
	public static BigInteger valueOf(long val) {
		if (val < 0) {
			if (val != -1) {
				// axelclk
				return BigInteger.newInstance(-1, -val);
				// return new BigInteger(-1, -val);
			}
			return MINUS_ONE;
		} else if (val <= 10) {
			return SMALL_VALUES[(int) val];
		} else {// (val > 10)
			// axelclk
			return BigInteger.newInstance(1, val);
			// return new BigInteger(1, val);
		}
	}

	/** @ar.org.fitc.spec_ref */
	/* package private */static BigInteger valueOfStatic(long val) {
		if (val < 0) {
			if (val != -1) {
				// axelclk
				return new BigInteger(-1, -val);
				// return new BigInteger(-1, -val);
			}
			return MINUS_ONE;
		} else if (val <= 10) {
			return SMALL_VALUES[(int) val];
		} else {// (val > 10)
			// axelclk
			return new BigInteger(1, val);
			// return new BigInteger(1, val);
		}
	}

	public java.math.BigInteger toJavaBigInteger() {
		return new java.math.BigInteger(toByteArray());
	}

	/** @ar.org.fitc.spec_ref */
	public byte[] toByteArray() {
		if (this._sign == 0) {
			return new byte[] { 0 };
		}
		BigInteger temp = this;
		int bitLen = bitLength();
		int iThis = getFirstNonzeroDigit();
		int bytesLen = (bitLen >> 3) + 1;
		/*
		 * Puts the little-endian int array representing the magnitude of this
		 * BigInteger into the big-endian byte array.
		 */
		byte[] bytes = new byte[bytesLen];
		int firstByteNumber = 0;
		int highBytes;
		int digitIndex = 0;
		int bytesInInteger = 4;
		int digit;
		int hB;

		if (bytesLen - (_size << 2) == 1) {
			bytes[0] = (byte) ((_sign < 0) ? -1 : 0);
			highBytes = 4;
			firstByteNumber++;
		} else {
			hB = bytesLen & 3;
			highBytes = (hB == 0) ? 4 : hB;
		}

		digitIndex = iThis;
		bytesLen -= iThis << 2;

		if (_sign < 0) {
			digit = -temp._words[digitIndex];
			digitIndex++;
			if (digitIndex == _size) {
				bytesInInteger = highBytes;
			}
			for (int i = 0; i < bytesInInteger; i++, digit >>= 8) {
				bytes[--bytesLen] = (byte) digit;
			}
			while (bytesLen > firstByteNumber) {
				digit = ~temp._words[digitIndex];
				digitIndex++;
				if (digitIndex == _size) {
					bytesInInteger = highBytes;
				}
				for (int i = 0; i < bytesInInteger; i++, digit >>= 8) {
					bytes[--bytesLen] = (byte) digit;
				}
			}
		} else {
			while (bytesLen > firstByteNumber) {
				digit = temp._words[digitIndex];
				digitIndex++;
				if (digitIndex == _size) {
					bytesInInteger = highBytes;
				}
				for (int i = 0; i < bytesInInteger; i++, digit >>= 8) {
					bytes[--bytesLen] = (byte) digit;
				}
			}
		}
		return bytes;
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger abs() {
		// axelclk
		return ((_sign < 0) ? newInstance(1, _size, _words) : this);
		// return ((_sign < 0) ? new BigInteger(1, _size, _words) : this);
	}

	public BigInteger negate() {
		return opposite();
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger opposite() {
		// axelclk
		return ((_sign == 0) ? this : newInstance(-_sign, _size, _words));
		// return ((_sign == 0) ? this : new BigInteger(-_sign, _size, _words));
	}

	public BigInteger add(long val) {
		return Elementary.add(this, valueOf(val));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger add(BigInteger val) {
		return Elementary.add(this, val);
	}

	public BigInteger subtract(long val) {
		return Elementary.subtract(this, valueOf(val));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger subtract(BigInteger val) {
		return Elementary.subtract(this, val);
	}

	public BigInteger plus(long val) {
		return Elementary.add(this, valueOf(val));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger plus(BigInteger val) {
		return Elementary.add(this, val);
	}

	public BigInteger minus(long val) {
		return Elementary.subtract(this, valueOf(val));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger minus(BigInteger val) {
		return Elementary.subtract(this, val);
	}

	/** @ar.org.fitc.spec_ref */
	public int signum() {
		return _sign;
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger shiftRight(int n) {
		if ((n == 0) || (_sign == 0)) {
			return this;
		}
		return ((n > 0) ? BitLevel.shiftRight(this, n) : BitLevel.shiftLeft(this, -n));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger shiftLeft(int n) {
		if ((n == 0) || (_sign == 0)) {
			return this;
		}
		return ((n > 0) ? BitLevel.shiftLeft(this, n) : BitLevel.shiftRight(this, -n));
	}

	/** @ar.org.fitc.spec_ref */
	public int bitLength() {
		return BitLevel.bitLength(this);
	}

	/** @ar.org.fitc.spec_ref */
	public boolean testBit(int n) {
		if (n == 0) {
			return ((_words[0] & 1) != 0);
		}
		if (n < 0) {
			// math.15=Negative bit address
			throw new ArithmeticException("BigInteger");//Messages.getString("math.15"
			// ));
			// //$NON-NLS-1$
		}
		int intCount = n >> 5;
		if (intCount >= _size) {
			return (_sign < 0);
		}
		int digit = _words[intCount];
		int i;
		n = (1 << (n & 31)); // int with 1 set to the needed position
		if (_sign < 0) {
			int firstNonZeroDigit = getFirstNonzeroDigit();
			if (intCount < firstNonZeroDigit) {
				return false;
			} else if (firstNonZeroDigit == intCount) {
				digit = -digit;
			} else {
				digit = ~digit;
			}
		}
		return ((digit & n) != 0);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger setBit(int n) {
		if (!testBit(n)) {
			return BitLevel.flipBit(this, n);
		} else {
			return this;
		}
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger clearBit(int n) {
		if (testBit(n)) {
			return BitLevel.flipBit(this, n);
		} else {
			return this;
		}
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger flipBit(int n) {
		if (n < 0) {
			// math.15=Negative bit address
			throw new ArithmeticException("BigInteger");//Messages.getString("math.15"
			// ));
			// //$NON-NLS-1$
		}
		return BitLevel.flipBit(this, n);
	}

	/** @ar.org.fitc.spec_ref */
	public int getLowestSetBit() {
		if (_sign == 0) {
			return -1;
		}
		// (sign != 0) implies that exists some non zero digit
		int i = getFirstNonzeroDigit();
		return ((i << 5) + Integer.numberOfTrailingZeros(_words[i]));
	}

	/** @ar.org.fitc.spec_ref */
	public int bitCount() {
		return BitLevel.bitCount(this);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger not() {
		return Logical.not(this);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger and(BigInteger val) {
		return Logical.and(this, val);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger or(BigInteger val) {
		return Logical.or(this, val);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger xor(BigInteger val) {
		return Logical.xor(this, val);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger andNot(BigInteger val) {
		return Logical.andNot(this, val);
	}

	/** @ar.org.fitc.spec_ref */
	public int intValue() {
		return (_sign * _words[0]);
	}

	/** @ar.org.fitc.spec_ref */
	// @Override
	public long longValue() {
		long value = (_size > 1) ? (((long) _words[1]) << 32) | (_words[0] & 0xFFFFFFFFL) : (_words[0] & 0xFFFFFFFFL);
		return (_sign * value);
	}

	public long toLong() throws ArithmeticException {
		if (_size > 2) {
			throw new ArithmeticException("toLong: number to large");
		}
		long value = (_size > 1) ? (((long) _words[1]) << 32) | (_words[0] & 0xFFFFFFFFL) : (_words[0] & 0xFFFFFFFFL);
		return (_sign * value);
	}

	public int toInt() throws ArithmeticException {
		if (_size > 1) {
			throw new ArithmeticException("toInt: number to large");
		}
		return (_sign * _words[0]);
	}

	/** @ar.org.fitc.spec_ref */
	// @Override
	// public float floatValue() {
	// return (float) doubleValue();
	// }
	/** @ar.org.fitc.spec_ref */
	// @Override
	public double doubleValue() {
		return Conversion.bigInteger2Double(this);
	}

	/** @ar.org.fitc.spec_ref */
	public int compareTo(BigInteger val) {
		if (_sign > val._sign) {
			return GREATER;
		}
		if (_sign < val._sign) {
			return LESS;
		}
		if (_size > val._size) {
			return _sign;
		}
		if (_size < val._size) {
			return -val._sign;
		}
		// Equal sign and equal numberLength
		return (_sign * Elementary.compareArrays(_words, val._words, _size));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger min(BigInteger val) {
		return ((this.compareTo(val) == LESS) ? this : val);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger max(BigInteger val) {
		return ((this.compareTo(val) == GREATER) ? this : val);
	}

	/** @ar.org.fitc.spec_ref */
	@Override
	public int hashCode() {
		if (_words.length > 0) {
			return _words[0];
		}
		return 0;
		// return intValue();
	}

	/** @ar.org.fitc.spec_ref */
	@Override
	public boolean equals(Object x) {
		return ((x instanceof BigInteger) && (compareTo((BigInteger) x) == EQUALS));
	}

	/** @ar.org.fitc.spec_ref */
	@Override
	public String toString() {
		return Conversion.toDecimalScaledString(this, 0);
	}

	/** @ar.org.fitc.spec_ref */
	public String toString(int radix) {
		return Conversion.bigInteger2String(this, radix);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger gcd(BigInteger val) {
		BigInteger val1 = this.abs();
		BigInteger val2 = val.abs();
		// To avoid a possible division by zero
		if (val1.signum() == 0) {
			return val2;
		} else if (val2.signum() == 0) {
			return val1;
		}

		// Optimization for small operands
		// (op2.bitLength() < 64) and (op1.bitLength() < 64)
		if (((val1._size == 1) || ((val1._size == 2) && (val1._words[1] > 0)))
				&& (val2._size == 1 || (val2._size == 2 && val2._words[1] > 0))) {
			return BigInteger.valueOf(Division.gcdBinary(val1.longValue(), val2.longValue()));
		}

		return Division.gcdBinary(val1.copy(), val2.copy());

	}

	public BigInteger times(long val) {
		return times(valueOf(val));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger times(BigInteger val) {
		// This let us to throw NullPointerException when val == null
		if (val._sign == 0) {
			return ZERO;
		}
		if (_sign == 0) {
			return ZERO;
		}
		return Multiplication.multiply(this, val);
	}

	public BigInteger multiply(long val) {
		return times(valueOf(val));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger multiply(BigInteger val) {
		return times(valueOf(val));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger pow(int exp) {
		if (exp < 0) {
			// math.16=Negative exponent
			throw new ArithmeticException("BigInteger");//Messages.getString("math.16"
			// ));
			// //$NON-NLS-1$
		}
		if (exp == 0) {
			return ONE;
		} else if (exp == 1 || equals(ONE) || equals(ZERO)) {
			return this;
		}
		return Multiplication.pow(this, exp);

	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger[] divideAndRemainder(BigInteger divisor) {
		int divisorSign = divisor._sign;
		if (divisorSign == 0) {
			// math.17=BigInteger divide by zero
			throw new ArithmeticException("BigInteger");//Messages.getString("math.17"
			// ));
			// //$NON-NLS-1$
		}
		int divisorLen = divisor._size;
		int[] divisorDigits = divisor._words;
		if (divisorLen == 1) {
			return Division.divideAndRemainderByInteger(this, divisorDigits[0], divisorSign);
		}
		// res[0] is a quotient and res[1] is a remainder:
		int[] thisDigits = _words;
		int thisLen = _size;
		int cmp = (thisLen != divisorLen) ? ((thisLen > divisorLen) ? 1 : -1) : Elementary.compareArrays(thisDigits, divisorDigits,
				thisLen);
		if (cmp < 0) {
			return new BigInteger[] { ZERO, this };
		}
		int thisSign = _sign;
		int quotientLength = thisLen - divisorLen + 1;
		int remainderLength = divisorLen;
		int quotientSign = ((thisSign == divisorSign) ? 1 : -1);
		// if (Config.SERVER_MODE) {
		// if (Config.BIGINTEGER_MAX_SIZE < quotientLength) {
		// throw new ObjectMemoryExceededException("BigInteger", quotientLength);
		// }
		// }
		int quotientDigits[] = new int[quotientLength];
		int remainderDigits[] = Division.divide(quotientDigits, quotientLength, thisDigits, thisLen, divisorDigits, divisorLen);
		// axelclk
		BigInteger result0 = newInstance(quotientSign, quotientLength, quotientDigits);
		BigInteger result1 = newInstance(thisSign, remainderLength, remainderDigits);
		// BigInteger result0 = new BigInteger(quotientSign, quotientLength,
		// quotientDigits);
		// BigInteger result1 = new BigInteger(thisSign, remainderLength,
		// remainderDigits);
		result0.cutOffLeadingZeroes();
		result1.cutOffLeadingZeroes();
		return new BigInteger[] { result0, result1 };
	}

	public BigInteger divide(long divisor) {
		return divide(valueOf(divisor));
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger divide(BigInteger divisor) {
		if (divisor._sign == 0) {
			// math.17=BigInteger divide by zero
			throw new ArithmeticException("BigInteger");//Messages.getString("math.17"
			// ));
			// //$NON-NLS-1$
		}
		int divisorSign = divisor._sign;
		if (divisor.isOne()) {
			return ((divisor._sign > 0) ? this : this.opposite());
		}
		int thisSign = _sign;
		int thisLen = _size;
		int divisorLen = divisor._size;
		if (thisLen + divisorLen == 2) {
			long val = (_words[0] & 0xFFFFFFFFL) / (divisor._words[0] & 0xFFFFFFFFL);
			if (thisSign != divisorSign) {
				val = -val;
			}
			return valueOf(val);
		}
		int cmp = ((thisLen != divisorLen) ? ((thisLen > divisorLen) ? 1 : -1) : Elementary.compareArrays(_words, divisor._words,
				thisLen));
		if (cmp == EQUALS) {
			return ((thisSign == divisorSign) ? ONE : MINUS_ONE);
		}
		if (cmp == LESS) {
			return ZERO;
		}
		int resLength = thisLen - divisorLen + 1;

		// if (Config.SERVER_MODE) {
		// if (Config.BIGINTEGER_MAX_SIZE < resLength) {
		// throw new ObjectMemoryExceededException("BigInteger", _size);
		// }
		// }
		int resDigits[] = new int[resLength];
		int resSign = ((thisSign == divisorSign) ? 1 : -1);
		if (divisorLen == 1) {
			Division.divideArrayByInt(resDigits, _words, thisLen, divisor._words[0]);
		} else {
			Division.divide(resDigits, resLength, _words, thisLen, divisor._words, divisorLen);
		}
		// axelclk
		BigInteger result = newInstance(resSign, resLength, resDigits);
		// BigInteger result = new BigInteger(resSign, resLength, resDigits);
		result.cutOffLeadingZeroes();
		return result;
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger remainder(BigInteger divisor) {
		if (divisor._sign == 0) {
			// math.17=BigInteger divide by zero
			throw new ArithmeticException("BigInteger");//Messages.getString("math.17"
			// ));
			// //$NON-NLS-1$
		}
		int thisLen = _size;
		int divisorLen = divisor._size;
		if (((thisLen != divisorLen) ? ((thisLen > divisorLen) ? 1 : -1) : Elementary.compareArrays(_words, divisor._words, thisLen)) == LESS) {
			return this;
		}
		int resLength = divisorLen;

		int resDigits[] = new int[resLength];
		if (resLength == 1) {
			resDigits[0] = Division.remainderArrayByInt(_words, thisLen, divisor._words[0]);
		} else {
			int qLen = thisLen - divisorLen + 1;
			resDigits = Division.divide(null, qLen, _words, thisLen, divisor._words, divisorLen);
		}
		// axelclk
		BigInteger result = newInstance(_sign, resLength, resDigits);
		// BigInteger result = new BigInteger(_sign, resLength, resDigits);
		result.cutOffLeadingZeroes();
		return result;
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger modInverse(BigInteger m) {
		if (m._sign <= 0) {
			// math.18=BigInteger: modulus not positive
			throw new ArithmeticException("BigInteger");//Messages.getString("math.18"
			// ));
			// //$NON-NLS-1$
		}
		// If both are even, no inverse exists
		if (!(testBit(0) || m.testBit(0))) {
			// math.19=BigInteger not invertible.
			throw new ArithmeticException("BigInteger");//Messages.getString("math.19"
			// ));
			// //$NON-NLS-1$
		}
		if (m.isOne()) {
			return ZERO;
		}

		// From now on: (m > 1)
		BigInteger res = Division.modInverseMontgomery(abs().mod(m), m);
		if (res._sign == 0) {
			// math.19=BigInteger not invertible.
			throw new ArithmeticException("BigInteger");//Messages.getString("math.19"
			// ));
			// //$NON-NLS-1$
		}

		res = ((_sign < 0) ? m.minus(res) : res);
		return res;

	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger modPow(BigInteger exponent, BigInteger m) {
		if (m._sign <= 0) {
			// math.18=BigInteger: modulus not positive
			throw new ArithmeticException("BigInteger");//Messages.getString("math.18"
			// ));
			// //$NON-NLS-1$
		}
		BigInteger base = this;

		if (m.isOne() | (exponent._sign > 0 & base._sign == 0)) {
			return BigInteger.ZERO;
		}
		if (base._sign == 0 && exponent._sign == 0) {
			return BigInteger.ONE;
		}
		if (exponent._sign < 0) {
			base = modInverse(m);
			exponent = exponent.opposite();
		}
		// From now on: (m > 0) and (exponent >= 0)
		BigInteger res = (m.testBit(0)) ? Division.oddModPow(base.abs(), exponent, m) : Division.evenModPow(base.abs(), exponent, m);
		if ((base._sign < 0) && exponent.testBit(0)) {
			// -b^e mod m == ((-1 mod m) * (b^e mod m)) mod m
			res = m.minus(BigInteger.ONE).times(res).mod(m);
		}
		// else exponent is even, so base^exp is positive
		return res;
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger mod(BigInteger m) {
		if (m._sign <= 0) {
			// math.18=BigInteger: modulus not positive
			throw new ArithmeticException("BigInteger");//Messages.getString("math.18"
			// ));
			// //$NON-NLS-1$
		}
		BigInteger rem = remainder(m);
		return ((rem._sign < 0) ? rem.plus(m) : rem);
	}

	/** @ar.org.fitc.spec_ref */
	public boolean isProbablePrime(int certainty) {
		return Primality.isProbablePrime(abs(), certainty);
	}

	/** @ar.org.fitc.spec_ref */
	public BigInteger nextProbablePrime() {
		if (_sign < 0) {
			// math.1A=start < 0: {0}
			throw new ArithmeticException("BigInteger");//Messages.getString("math.1A"
			// ,
			// this)); //$NON-NLS-1$
		}
		return Primality.nextProbablePrime(this);
	}

	public BigInteger nextProbablePrime(int n) {
		if (_sign < 0 || n <= 0) {
			// math.1A=start < 0: {0}
			throw new ArithmeticException("BigInteger");//Messages.getString("math.1A"
			// ,
			// this)); //$NON-NLS-1$
		}
		BigInteger temp = this;
		for (int i = 0; i < n; i++) {
			temp = Primality.nextProbablePrime(temp);
		}
		return temp;
	}

	/** @ar.org.fitc.spec_ref */
	public static BigInteger probablePrime(int bitLength, Random rnd) {
		return new BigInteger(bitLength, 100, rnd);
	}

	/* Private Methods */

	/** Decreases {@code numberLength} if there are zero high elements. */
	final void cutOffLeadingZeroes() {
		while ((_size > 0) && (_words[--_size] == 0)) {
			;
		}
		if (_words[_size++] == 0) {
			_sign = 0;
		}
	}

	/** Tests if {@code this.abs()} is equals to {@code ONE} */
	public boolean isOne() {
		return ((_size == 1) && (_words[0] == 1));
	}

	/**
	 * Puts a big-endian byte array into a little-endian int array.
	 */
	private void putBytesPositiveToIntegers(byte[] byteValues) {
		int bytesLen = byteValues.length;
		int highBytes = bytesLen & 3;
		_size = (bytesLen >> 2) + ((highBytes == 0) ? 0 : 1);
		if (Config.SERVER_MODE) {
			if (Config.BIGINTEGER_MAX_SIZE < _size) {
				throw new ObjectMemoryExceededException("BigInteger", _size);
			}
		}
		_words = new int[_size];
		int i = 0;
		// Put bytes to the int array starting from the end of the byte array
		while (bytesLen > highBytes) {
			_words[i++] = (byteValues[--bytesLen] & 0xFF) | (byteValues[--bytesLen] & 0xFF) << 8 | (byteValues[--bytesLen] & 0xFF) << 16
					| (byteValues[--bytesLen] & 0xFF) << 24;
		}
		// Put the first bytes in the highest element of the int array
		for (int j = 0; j < bytesLen; j++) {
			_words[i] = (_words[i] << 8) | (byteValues[j] & 0xFF);
		}
	}

	/**
	 * Puts a big-endian byte array into a little-endian applying two complement.
	 */
	private void putBytesNegativeToIntegers(byte[] byteValues) {
		int bytesLen = byteValues.length;
		int highBytes = bytesLen & 3;
		_size = (bytesLen >> 2) + ((highBytes == 0) ? 0 : 1);
		if (Config.SERVER_MODE) {
			if (Config.BIGINTEGER_MAX_SIZE < _size) {
				throw new ObjectMemoryExceededException("BigInteger", _size);
			}
		}
		_words = new int[_size];
		int i = 0;
		// Setting the sign
		_words[_size - 1] = -1;
		// Put bytes to the int array starting from the end of the byte array
		while (bytesLen > highBytes) {
			_words[i] = (byteValues[--bytesLen] & 0xFF) | (byteValues[--bytesLen] & 0xFF) << 8 | (byteValues[--bytesLen] & 0xFF) << 16
					| (byteValues[--bytesLen] & 0xFF) << 24;
			if (_words[i] != 0) {
				_words[i] = -_words[i];
				firstNonzeroDigit = i;
				i++;
				while (bytesLen > highBytes) {
					_words[i] = (byteValues[--bytesLen] & 0xFF) | (byteValues[--bytesLen] & 0xFF) << 8
							| (byteValues[--bytesLen] & 0xFF) << 16 | (byteValues[--bytesLen] & 0xFF) << 24;
					_words[i] = ~_words[i];
					i++;
				}
				break;
			}
			i++;
		}
		if (highBytes != 0) {
			// Put the first bytes in the highest element of the int array
			if (firstNonzeroDigit != -2) {
				for (int j = 0; j < bytesLen; j++) {
					_words[i] = (_words[i] << 8) | (byteValues[j] & 0xFF);
				}
				_words[i] = ~_words[i];
			} else {
				for (int j = 0; j < bytesLen; j++) {
					_words[i] = (_words[i] << 8) | (byteValues[j] & 0xFF);
				}
				_words[i] = -_words[i];
			}
		}
	}

	int getFirstNonzeroDigit() {
		if (firstNonzeroDigit == -2) {
			int i;
			if (this._sign == 0) {
				i = -1;
			} else {
				for (i = 0; _words[i] == 0; i++)
					;
			}
			firstNonzeroDigit = i;
		}
		return firstNonzeroDigit;
	}

	/*
	 * Returns a copy of the current instance to achieve immutability
	 */
	public BigInteger copy() {
		int[] copyDigits = new int[_size];
		System.arraycopy(_words, 0, copyDigits, 0, _size);
		// axelclk
		return newInstance(_sign, _size, copyDigits);
		// return new BigInteger(_sign, _size, copyDigits);
	}

	public BigInteger copyNew() {
		int[] copyDigits = new int[_size];
		System.arraycopy(_words, 0, copyDigits, 0, _size);
		BigInteger z = new BigInteger();
		z._sign = _sign;
		z._size = _size;
		z._words = copyDigits;
		return z;
	}

	// public void recycle() {
	// NO_ARRAY_FACTORY.recycle(this);
	// }

	/** @ar.org.fitc.spec_ref */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		_sign = signum;
		putBytesPositiveToIntegers(magnitude);
		cutOffLeadingZeroes();
	}

	/** @ar.org.fitc.spec_ref */
	private void writeObject(ObjectOutputStream out) throws IOException {
		signum = signum();
		magnitude = abs().toByteArray();
		out.defaultWriteObject();
	}

	void unCache() {
		firstNonzeroDigit = -2;
	}

	/** @ar.org.fitc.spec_ref */
	public String getString() {
		return Conversion.toDecimalScaledString(this, 0);
	}

	// @Override
	public boolean isLargerThan(BigInteger that) {
		return (compareTo(that) == GREATER);
	}

	public boolean isLessThan(BigInteger that) {
		return (compareTo(that) == LESS);
	}

	public boolean isNegative() {
		return (compareTo(ZERO) == LESS);
	}

	public boolean isPositive() {
		return (compareTo(ZERO) == GREATER);
	}

	public boolean isZero() {
		return (compareTo(ZERO) == EQUALS);
	}

	public boolean isEven() {
		return !testBit(0);
	}

	public boolean isOdd() {
		return testBit(0);
	}

	// @Override
	// public Text toText() {
	// return Text.valueOf(Conversion.toDecimalScaledString(this, 0));
	// }

	/*
	 * Returns the large integer for the specified character sequence in the
	 * specified radix.
	 * 
	 * @param csq the character sequence to parse.
	 * 
	 * @param radix the radix of the representation.
	 * 
	 * @return <code>LargeInteger.parse(csq, radix, cursor)</code>
	 * 
	 * @throws NumberFormatException if error when parsing.
	 */
	public static BigInteger valueOf(String val, int radix) {
		if (val == null) {
			throw new NullPointerException();
		}
		if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
			// math.11=Radix out of range
			throw new NumberFormatException("BigInteger");// Messages.getString(
			// "math.11"));
			// //$NON-NLS-1$
		}
		if (val.length() == 0) {
			// math.12=Zero length BigInteger
			throw new NumberFormatException("BigInteger");// Messages.getString(
			// "math.12"));
			// //$NON-NLS-1$
		}
		BigInteger me = Conversion.string2BigInteger(val, radix);
		return me;
	}

	public static BigInteger valueOf(BigInteger val) {
		int[] words = new int[val._size];
		System.arraycopy(val._words, 0, words, 0, val._size);
		// axelclk
		return newInstance(val._sign, words);
		// return new BigInteger(val._sign, words);
	}

	public static void main(String[] args) {
		BigInteger i = valueOf(1000000).nextProbablePrime();
		System.out.println(i);
	}
}
