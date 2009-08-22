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
 * Static library that provides all multiplication of {@link BigInteger} methods.
 *
 * @author Intel Middleware Product Division
 * @author Instituto Tecnologico de Cordoba
 */
class Multiplication {

    /** Just to denote that this class can't be instantied. */
    private Multiplication() {}

    /**
     * Break point in digits (number of {@code int} elements)
     * between Karatsuba and Pencil and Paper multiply.
     */
    static final int whenUseKaratsuba = 63; // an heuristic value

    /**
     * An array with powers of ten that fit in the type {@code int}.
     * ({@code 10^0,10^1,...,10^9})
     */
    static final int tenPows[] = {
        1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
    };
    
    /**
     * An array with powers of five that fit in the type {@code int}.
     * ({@code 5^0,5^1,...,5^13})
     */
    static final int fivePows[] = {
        1, 5, 25, 125, 625, 3125, 15625, 78125, 390625,
        1953125, 9765625, 48828125, 244140625, 1220703125
    };

    /**
     * An array with the first powers of ten in {@code BigInteger} version.
     * ({@code 10^0,10^1,...,10^31})
     */
    static final BigInteger[] bigTenPows = new BigInteger[32];

    /**
     * An array with the first powers of five in {@code BigInteger} version.
     * ({@code 5^0,5^1,...,5^31})
     */
    static final BigInteger bigFivePows[] = new BigInteger[32];
    
    

    static {
        int i;
        long fivePow = 1L;
        
        for (i = 0; i <= 18; i++) {
            checkCanceled();
						bigFivePows[i] = BigInteger.valueOfStatic(fivePow);
            bigTenPows[i] = BigInteger.valueOfStatic(fivePow << i);
            fivePow *= 5;
        }
        for (; i < bigTenPows.length; i++) {
            checkCanceled();
						bigFivePows[i] = bigFivePows[i - 1].times(bigFivePows[1]);
            bigTenPows[i] = bigTenPows[i - 1].times(BigInteger.TEN);
        }
    }

    /**
     * Performs a multiplication of two BigInteger and hides the algorithm used.
     * @see BigInteger#times(BigInteger)
     */
    static BigInteger multiply(BigInteger x, BigInteger y) {
        return karatsuba(x, y);
    }

    /**
     * Performs the multiplication with the Karatsuba's algorithm.
     * <b>Karatsuba's algorithm:</b>
     *<tt>
     *             u = u<sub>1</sub> * B + u<sub>0</sub><br>
     *             v = v<sub>1</sub> * B + v<sub>0</sub><br>
     *
     *
     *  u*v = (u<sub>1</sub> * v<sub>1</sub>) * B<sub>2</sub> + ((u<sub>1</sub> - u<sub>0</sub>) * (v<sub>0</sub> - v<sub>1</sub>) + u<sub>1</sub> * v<sub>1</sub> +
     *  u<sub>0</sub> * v<sub>0</sub> ) * B + u<sub>0</sub> * v<sub>0</sub><br>
     *</tt>
     * @param op1 first factor of the product
     * @param op2 second factor of the product
     * @return {@code op1 * op2}
     * @see #multiply(BigInteger, BigInteger)
     */
    static BigInteger karatsuba(BigInteger op1, BigInteger op2) {
        BigInteger temp;
        if (op2._size > op1._size) {
            temp = op1;
            op1 = op2;
            op2 = temp;
        }
        if (op2._size < whenUseKaratsuba) {
            return multiplyPAP(op1, op2);
        }
        /*  Karatsuba:  u = u1*B + u0
         *              v = v1*B + v0
         *  u*v = (u1*v1)*B^2 + ((u1-u0)*(v0-v1) + u1*v1 + u0*v0)*B + u0*v0
         */
        // ndiv2 = (op1.numberLength / 2) * 32
        int ndiv2 = (op1._size & 0xFFFFFFFE) << 4;
        BigInteger upperOp1 = op1.shiftRight(ndiv2);
        BigInteger upperOp2 = op2.shiftRight(ndiv2);
        BigInteger lowerOp1 = op1.minus(upperOp1.shiftLeft(ndiv2));
        BigInteger lowerOp2 = op2.minus(upperOp2.shiftLeft(ndiv2));

        BigInteger upper = karatsuba(upperOp1, upperOp2);
        BigInteger lower = karatsuba(lowerOp1, lowerOp2);
        BigInteger middle = karatsuba( upperOp1.minus(lowerOp1),
                lowerOp2.minus(upperOp2));
        middle = middle.plus(upper).plus(lower);
        middle = middle.shiftLeft(ndiv2);
        upper = upper.shiftLeft(ndiv2 << 1);

        return upper.plus(middle).plus(lower);
    }

    /**
     * Multiplies two BigIntegers.
     * Implements traditional scholar algorithm described by Knuth.
     *
     * <br><tt>
     *         <table border="0">
     * <tbody>
     *
     *
     * <tr>
     * <td align="center">A=</td>
     * <td>a<sub>3</sub></td>
     * <td>a<sub>2</sub></td>
     * <td>a<sub>1</sub></td>
     * <td>a<sub>0</sub></td>
     * <td></td>
     * <td></td>
     * </tr>
     *
     *<tr>
     * <td align="center">B=</td>
     * <td></td>
     * <td>b<sub>2</sub></td>
     * <td>b<sub>1</sub></td>
     * <td>b<sub>1</sub></td>
     * <td></td>
     * <td></td>
     * </tr>
     *
     * <tr>
     * <td></td>
     * <td></td>
     * <td></td>
     * <td>b<sub>0</sub>*a<sub>3</sub></td>
     * <td>b<sub>0</sub>*a<sub>2</sub></td>
     * <td>b<sub>0</sub>*a<sub>1</sub></td>
     * <td>b<sub>0</sub>*a<sub>0</sub></td>
     * </tr>
     *
     * <tr>
     * <td></td>
     * <td></td>
     * <td>b<sub>1</sub>*a<sub>3</sub></td>
     * <td>b<sub>1</sub>*a<sub>2</sub></td>
     * <td>b<sub>1</sub>*a1</td>
     * <td>b<sub>1</sub>*a0</td>
     * </tr>
     *
     * <tr>
     * <td>+</td>
     * <td>b<sub>2</sub>*a<sub>3</sub></td>
     * <td>b<sub>2</sub>*a<sub>2</sub></td>
     * <td>b<sub>2</sub>*a<sub>1</sub></td>
     * <td>b<sub>2</sub>*a<sub>0</sub></td>
     * </tr>
     *
     *<tr>
     * <td></td>
     *<td>______</td>
     * <td>______</td>
     * <td>______</td>
     * <td>______</td>
     * <td>______</td>
     * <td>______</td>
     *</tr>
     *
     * <tr>
     *
     * <td align="center">A*B=R=</td>
     * <td align="center">r<sub>5</sub></td>
     * <td align="center">r<sub>4</sub></td>
     * <td align="center">r<sub>3</sub></td>
     * <td align="center">r<sub>2</sub></td>
     * <td align="center">r<sub>1</sub></td>
     * <td align="center">r<sub>0</sub></td>
     * <td></td>
     * </tr>
     *
     * </tbody>
     * </table>
     *
     *</tt>
     *
     * @param op1 first factor of the multiplication {@code  op1 >= 0}
     * @param op2 second factor of the multiplication {@code  op2 >= 0}
     * @return a {@code BigInteger} of value {@code  op1 * op2}
     */
    static BigInteger multiplyPAP(BigInteger a, BigInteger b) {
        // PRE: a >= b
        int aLen = a._size;
        int bLen = b._size;
        int resLength = aLen + bLen;
        int resSign = (a._sign != b._sign) ? -1 : 1;
        // A special case when both numbers don't exceed int
        if (resLength == 2) {
            long val = (a._words[0] & 0xFFFFFFFFL)
            * (b._words[0] & 0xFFFFFFFFL);
            int valueLo = (int)val;
            int valueHi = (int)(val >>> 32);
//          axelclk
            return ((valueHi == 0)
                ? BigInteger.newInstance(resSign, valueLo)
                : BigInteger.newInstance(resSign, 2, new int[]{valueLo, valueHi}));
//            return ((valueHi == 0)
//            ? new BigInteger(resSign, valueLo)
//            : new BigInteger(resSign, 2, new int[]{valueLo, valueHi}));
        }
        int[] aDigits = a._words;
        int[] bDigits = b._words;
        if (Config.SERVER_MODE) {
			if (Config.BIGINTEGER_MAX_SIZE < resLength) {
				throw new ObjectMemoryExceededException("BigInteger", resLength);
			}
		}
        int resDigits[] = new int[resLength];
        long carry;
        long bDigit;
        int i, j, m;
        // Common case
        for (j = 0; j < bLen; j++) {
            checkCanceled();
						carry = 0;
            bDigit = (bDigits[j] & 0xFFFFFFFFL);
            for (i = 0, m = j; i < aLen; i++, m++) {
                checkCanceled();
								carry += (aDigits[i] & 0xFFFFFFFFL)
                * bDigit
                + (resDigits[m] & 0xFFFFFFFFL);
                resDigits[m] = (int)carry;
                carry >>>= 32;
            }
            resDigits[m] = (int) carry;
        }
//      axelclk
        BigInteger result = BigInteger.newInstance(resSign, resLength, resDigits);
//        BigInteger result = new BigInteger(resSign, resLength, resDigits);
        result.cutOffLeadingZeroes();
        return result;
    }

    /**
     * Multiplies an array of integers by an integer value
     * and saves the result in {@code res}.
     * @param a the array of integers
     * @param aSize the number of elements of intArray to be multiplied
     * @param factor the multiplier
     * @return the top digit of production
     */
    private static int multiplyByInt(int res[], int a[], final int aSize, final int factor) {
        long carry = 0;

        for (int i = 0; i < aSize; i++) {
            checkCanceled();
						carry += (a[i] & 0xFFFFFFFFL) * (factor & 0xFFFFFFFFL);
            res[i] = (int)carry;
            carry >>>= 32;
        }
        return (int)carry;
    }

    
    /**
     * Multiplies an array of integers by an integer value.
     * @param a the array of integers
     * @param aSize the number of elements of intArray to be multiplied
     * @param factor the multiplier
     * @return the top digit of production
     */
    static int multiplyByInt(int a[], final int aSize, final int factor) {
        return multiplyByInt(a, a, aSize, factor);
    }
    
    /**
     * Multiplies a number by a positive integer.
     * @param val an arbitrary {@code BigInteger}
     * @param factor a positive {@code int} number
     * @return {@code val * factor}
     */
    static BigInteger multiplyByPositiveInt(BigInteger val, int factor) {
        int resSign = val._sign;
        if (resSign == 0) {
            return BigInteger.ZERO;
        }
        int aNumberLength = val._size;
        int[] aDigits = val._words;
        
        if (aNumberLength == 1) {
            long res = (aDigits[0] & 0xFFFFFFFFL) * (factor);
            int resLo = (int)res;
            int resHi = (int)(res >>> 32);
//          axelclk
            return ((resHi == 0)
                ? BigInteger.newInstance(resSign, resLo)
                : BigInteger.newInstance(resSign, 2, new int[]{resLo, resHi}));
//            return ((resHi == 0)
//            ? new BigInteger(resSign, resLo)
//            : new BigInteger(resSign, 2, new int[]{resLo, resHi}));
        }
        // Common case
        int resLength = aNumberLength + 1;
        if (Config.SERVER_MODE) { 
			if (Config.BIGINTEGER_MAX_SIZE < resLength) {
				throw new ObjectMemoryExceededException("BigInteger", resLength);
			}
		}
        int resDigits[] = new int[resLength];
        
        resDigits[aNumberLength] = multiplyByInt(resDigits, aDigits, aNumberLength, factor);
//      axelclk
        BigInteger result = BigInteger.newInstance(resSign, resLength, resDigits);
//        BigInteger result = new BigInteger(resSign, resLength, resDigits);
        result.cutOffLeadingZeroes();
        return result;
    }

    static BigInteger pow(BigInteger base, int exponent) {
        // PRE: exp > 0
        BigInteger res = BigInteger.ONE;
        BigInteger acc = base;

        for (; exponent > 1; exponent >>= 1) {
            checkCanceled();
						if ((exponent & 1) != 0) {
                // if odd, multiply one more time by acc
                res = res.times(acc);
            }
            // acc = base^(2^i)
            //a limit where karatsuba performs a faster square than the square algorithm
            if ( acc._size == 1 ){
                acc = acc.times(acc); // square
            }
            else{
//            axelclk
                acc = BigInteger.newInstance(1, square(acc._words, acc._size));
//                acc = new BigInteger(1, square(acc._words, acc._size));
            }
        }
        // exponent == 1, multiply one more time
        res = res.times(acc);
        return res;
    }

    /**
     *  Performs a<sup>2</sup>
     *  @param a The number to square.
     *  @param length The lenght of the number to square.
     */ 
    static int[] square(int[] a, int s) {
        int [] t = new int [s<<1];
        long cs;
        long aI;
        for(int i=0; i<s; i++){
            checkCanceled();
						cs = 0;
            aI = (0xFFFFFFFFL & a[i]);
            for (int j=i+1; j<s; j++){
                checkCanceled();
								cs += (0xFFFFFFFFL & t[i+j]) + aI * (0xFFFFFFFFL & a[j]) ;
                t[i+j] = (int) cs;
                cs >>>= 32;
            }
            
            t[i+s] = (int) cs;
        }
        BitLevel.shiftLeft( t, t, 0, 1 );
        cs = 0;
        
        for(int i=0, index = 0; i< s; i++, index++){
            checkCanceled();
						aI = (0xFFFFFFFFL & a[i]);
            cs += aI * aI  + (t[index] & 0xFFFFFFFFL);
            t[index] = (int) cs;
            cs >>>= 32;
            index++;
            cs += t[index] & 0xFFFFFFFFL ;
            t[index] = (int)cs;
            cs >>>= 32;
        }
        return t;
    }

    /**
     * Multiplies a number by a power of ten.
     * This method is used in {@code BigDecimal} class.
     * @param val the number to be multiplied
     * @param exp a positive {@code long} exponent
     * @return {@code val * 10<sup>exp</sup>}
     */
    static BigInteger multiplyByTenPow(BigInteger val, long exp) {
        // PRE: exp >= 0
        return ((exp < tenPows.length)
        ? multiplyByPositiveInt(val, tenPows[(int)exp])
        : val.times(powerOf10(exp)));
    }
    
    /**
     * It calculates a power of ten, which exponent could be out of 32-bit range.
     * Note that internally this method will be used in the worst case with
     * an exponent equals to: {@code Integer.MAX_VALUE - Integer.MIN_VALUE}.
     * @param exp the exponent of power of ten, it must be positive.
     * @return a {@code BigInteger} with value {@code 10<sup>exp</sup>}.
     */
    static BigInteger powerOf10(long exp) {
        // PRE: exp >= 0
        int intExp = (int)exp;
        // "SMALL POWERS"
        if (exp < bigTenPows.length) {
            // The largest power that fit in 'long' type
            return bigTenPows[intExp];
        } else if (exp <= 50) {
            // To calculate:    10^exp
            return BigInteger.TEN.pow(intExp);
        } else if (exp <= 1000) {
            // To calculate:    5^exp * 2^exp
            return bigFivePows[1].pow(intExp).shiftLeft(intExp);
        }
        // "LARGE POWERS"
        /*
         * To check if there is free memory to allocate a BigInteger of the
         * estimated size, measured in bytes: 1 + [exp / log10(2)]
         */
        long byteArraySize = 1 + (long)(exp / 2.4082399653118496);
        
        if (byteArraySize > Runtime.getRuntime().freeMemory()) {
            // math.01=power of ten too big
            throw new OutOfMemoryError("Multiplication.powerOf10");//Messages.getString("math.01")); //$NON-NLS-1$
        }
        if (exp <= Integer.MAX_VALUE) {
            // To calculate:    5^exp * 2^exp
            return bigFivePows[1].pow(intExp).shiftLeft(intExp);
        }
        /*
         * "HUGE POWERS"
         * 
         * This branch probably won't be executed since the power of ten is too
         * big.
         */
        // To calculate:    5^exp
        BigInteger powerOfFive = bigFivePows[1].pow(Integer.MAX_VALUE);
        BigInteger res = powerOfFive;
        long longExp = exp - Integer.MAX_VALUE;
        
        intExp = (int)(exp % Integer.MAX_VALUE);
        while (longExp > Integer.MAX_VALUE) {
            checkCanceled();
						res = res.times(powerOfFive);
            longExp -= Integer.MAX_VALUE;
        }
        res = res.times(bigFivePows[1].pow(intExp));
        // To calculate:    5^exp << exp
        res = res.shiftLeft(Integer.MAX_VALUE);
        longExp = exp - Integer.MAX_VALUE;
        while (longExp > Integer.MAX_VALUE) {
            checkCanceled();
						res = res.shiftLeft(Integer.MAX_VALUE);
            longExp -= Integer.MAX_VALUE;
        }
        res = res.shiftLeft(intExp);
        return res;
    }
    
    /**
     * Multiplies a number by a power of five.
     * This method is used in {@code BigDecimal} class.
     * @param val the number to be multiplied
     * @param exp a positive {@code int} exponent
     * @return {@code val * 5<sup>exp</sup>}
     */
    static BigInteger multiplyByFivePow(BigInteger val, int exp) {
        // PRE: exp >= 0
        if (exp < fivePows.length) {
            return multiplyByPositiveInt(val, fivePows[exp]);
        } else if (exp < bigFivePows.length) {
            return val.times(bigFivePows[exp]);
        } else {// Large powers of five
            return val.times(bigFivePows[1].pow(exp));
        }
    }
}
