package org.matheclipse.core.convert.experimental;

/**
 * Write a description of class 'univariate factorization over Z' here.
 * 
 * @author jeremy watts 
 * @version 16/03/07
 * @modified 10/08/08
 */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnivariateFactorizationOverZ {
	// instance variables - replace the example below with your own
	public static void main(String[] args) {

		int runIndex;
		int index;

		int polynomialDegree = 5;
		//243-405*x+270*x^2-90*x^3+15*x^4-x^5
		BigInteger[] polynomial = new BigInteger[polynomialDegree + 1];
//		polynomial[6] = new BigInteger("1");
		polynomial[5] = new BigInteger("-1");
		polynomial[4] = new BigInteger("15");
		polynomial[3] = new BigInteger("-90");
		polynomial[2] = new BigInteger("270");
		polynomial[1] = new BigInteger("-405");
		polynomial[0] = new BigInteger("243");

		// polynomial[9] = new BigInteger("27");
		// polynomial[8] = new BigInteger("-18");
		// polynomial[7] = new BigInteger("63");
		// polynomial[6] = new BigInteger("-36");
		// polynomial[5] = new BigInteger("45");
		// polynomial[4] = new BigInteger("-18");
		// polynomial[3] = new BigInteger("9");
		// polynomial[2] = new BigInteger("0");
		// polynomial[1] = new BigInteger("0");
		// polynomial[0] = new BigInteger("0");

		List univariateFactorizationOverZresult;

		System.out.println("polynomial is ");
		for (index = polynomialDegree; index >= 0; index--) {
			System.out.print(polynomial[index]);
			if (index > 0) {
				System.out.print("x^");
				System.out.print(index);
				System.out.print(" + ");
			}
		}
		System.out.println();
		System.out.println();

		univariateFactorizationOverZresult = univariateFactorizationOverZ(polynomial, polynomialDegree);

		System.out.println("factors are");
		for (runIndex = 0; runIndex <= (univariateFactorizationOverZresult.size() - 1); runIndex = runIndex + 3) {
			System.out.print("(");
			for (index = (Integer) univariateFactorizationOverZresult.get(runIndex); index >= 0; index--) {
				BigInteger temp = ((BigInteger[]) univariateFactorizationOverZresult.get(runIndex + 1))[index];
				if (!temp.equals(BigInteger.ZERO)) {
					if (!temp.equals(BigInteger.ONE) || index <= 0) {
						System.out.print(temp);
					}
					if (index > 0) {
						System.out.print("x^");
						System.out.print(index);
						System.out.print(" + ");
					}
				}
			}
			System.out.print(")");

			Integer itemp = (Integer) univariateFactorizationOverZresult.get(runIndex + 2);
			if (itemp != 1) {
				System.out.print("^");
				System.out.print((Integer) univariateFactorizationOverZresult.get(runIndex + 2));
			}
		}
		System.out.println();
		System.out.println();

	}

	public static List univariateFactorizationOverZ(BigInteger[] polynomial, int polynomialDegree) {

		/*
		 * finds a factorization of a univariate polynomial over the integers
		 */

		BigInteger[][][] dummyOne;
		BigInteger[][][] dummyTwo;
		BigInteger[] dummy;
		BigInteger[] polynomialPrime;

		BigInteger content;

		int index;
		int columnIndex;
		int dummyOneDegree;
		int dummyTwoDegree;
		int polynomialPrimeDegree;
		int runIndex;
		int factorizationCount;
		int startPoint;
		int sum;
		int passes;

		boolean match;
		boolean loopHalt;
		boolean exchange;
		boolean halt;
		boolean negate;

		List univariatePolynomialGCDresult;
		List polynomialDivisionResult;
		List zassenhausResult;
		List<Object> dummyList = new ArrayList<Object>();
		List<Object> factors = new ArrayList<Object>();
		List<Object> newFactors = new ArrayList<Object>();

		/* is leading coefficient negative? */
		negate = false;
		if (polynomial[polynomialDegree].compareTo(BigInteger.ZERO) == -1) {
			negate = true;
			for (index = 0; index <= polynomialDegree; index++) {
				polynomial[index] = polynomial[index].negate();
			}
		}

		/* make polynomial primitive */
		content = polynomial[0].gcd(polynomial[1]);

		if (polynomialDegree > 1) {
			for (index = 2; index <= polynomialDegree; index++) {
				content = content.gcd(polynomial[index]);
			}
		}

		for (index = 0; index <= polynomialDegree; index++) {
			polynomial[index] = polynomial[index].divide(content);
		}

		if (negate == true) {
			content = content.negate();
		}

		dummy = new BigInteger[1];
		dummy[0] = content;
		factors.add(0);
		factors.add(dummy);

		/* determine square-free decomposition */
		factors.add(polynomialDegree);
		factors.add(polynomial);
		halt = false;
		while (halt == false) {
			polynomialPrime = new BigInteger[polynomialDegree];
			polynomialPrimeDegree = polynomialDegree - 1;
			for (index = polynomialPrimeDegree; index >= 0; index--) {
				polynomialPrime[index] = polynomial[index + 1].multiply(BigInteger.valueOf(index + 1));
			}

			dummyOne = new BigInteger[polynomialDegree + 1][3][3];
			for (index = 0; index <= polynomialDegree; index++) {
				dummyOne[index][1][1] = polynomial[index];
				dummyOne[index][1][2] = BigInteger.ONE;

				dummyOne[index][2][1] = BigInteger.ZERO;
				dummyOne[index][2][2] = BigInteger.ONE;
			}

			dummyTwo = new BigInteger[polynomialPrimeDegree + 1][3][3];
			for (index = 0; index <= polynomialPrimeDegree; index++) {
				dummyTwo[index][1][1] = polynomialPrime[index];
				dummyTwo[index][1][2] = BigInteger.ONE;

				dummyTwo[index][2][1] = BigInteger.ZERO;
				dummyTwo[index][2][2] = BigInteger.ONE;
			}

			univariatePolynomialGCDresult = univariatePolynomialGCD(dummyOne, dummyTwo, polynomialDegree, polynomialPrimeDegree, 100);

			if (((Integer) univariatePolynomialGCDresult.get(0) == 0)
					&& (((BigInteger[][][]) univariatePolynomialGCDresult.get(1))[0][1][1].compareTo(BigInteger.ONE) == 0)) {
				halt = true;
			} else {
				factors.remove(factors.size() - 1);
				factors.remove(factors.size() - 1);

				dummyTwoDegree = (Integer) univariatePolynomialGCDresult.get(0);
				dummyTwo = new BigInteger[dummyTwoDegree + 1][3][3];
				for (index = 0; index <= dummyTwoDegree; index++) {
					dummyTwo[index][1][1] = ((BigInteger[][][]) univariatePolynomialGCDresult.get(1))[index][1][1];
					dummyTwo[index][1][2] = BigInteger.ONE;

					dummyTwo[index][2][1] = BigInteger.ZERO;
					dummyTwo[index][2][2] = BigInteger.ONE;
				}

				polynomialDivisionResult = polynomialDivision(dummyOne, dummyTwo, polynomialDegree, dummyTwoDegree);

				dummy = new BigInteger[(Integer) polynomialDivisionResult.get(0) + 1];
				for (index = 0; index <= (Integer) polynomialDivisionResult.get(0); index++) {
					dummy[index] = ((BigInteger[][][]) polynomialDivisionResult.get(2))[index][1][1];
				}
				factors.add((Integer) polynomialDivisionResult.get(0));
				factors.add(dummy);

				dummy = new BigInteger[dummyTwoDegree + 1];
				for (index = 0; index <= dummyTwoDegree; index++) {
					dummy[index] = dummyTwo[index][1][1];
				}
				factors.add(dummyTwoDegree);
				factors.add(dummy);

				polynomialDegree = (Integer) univariatePolynomialGCDresult.get(0);
				for (index = 0; index <= polynomialDegree; index++) {
					polynomial[index] = ((BigInteger[][][]) univariatePolynomialGCDresult.get(1))[index][1][1];
				}
			}
		}

		/* factorize each of the square-free factors */
		factorizationCount = 1;
		while (factorizationCount != 0) {
			factorizationCount = 0;
			for (runIndex = 0; runIndex <= (factors.size() - 1); runIndex = runIndex + 2) {
				if ((Integer) factors.get(runIndex) > 0) {
					zassenhausResult = zassenhausAlgorithm((BigInteger[]) factors.get(runIndex + 1), (Integer) factors.get(runIndex));

					for (index = 0; index <= (zassenhausResult.size() - 1); index = index + 4) {
						if (zassenhausResult.size() == 2) {
							newFactors.add((Integer) zassenhausResult.get(index));
							newFactors.add((BigInteger[]) zassenhausResult.get(index + 1));
						} else {
							factorizationCount++;
							newFactors.add((Integer) zassenhausResult.get(index));
							newFactors.add((BigInteger[]) zassenhausResult.get(index + 1));
							newFactors.add((Integer) zassenhausResult.get(index + 2));
							newFactors.add((BigInteger[]) zassenhausResult.get(index + 3));
						}
					}
				} else {
					newFactors.add((Integer) factors.get(runIndex));
					newFactors.add((BigInteger[]) factors.get(runIndex + 1));
				}
			}

			/* copy 'new factors' back to 'factors' */
			factors.clear();
			for (index = 0; index <= (newFactors.size() - 1); index = index + 2) {
				dummy = new BigInteger[(Integer) newFactors.get(index) + 1];
				for (columnIndex = (Integer) newFactors.get(index); columnIndex >= 0; columnIndex--) {
					dummy[columnIndex] = ((BigInteger[]) newFactors.get(index + 1))[columnIndex];
				}
				factors.add((Integer) newFactors.get(index));
				factors.add(dummy);
			}
			newFactors.clear();
		}

		/* add exponents to factors */
		newFactors.clear();
		for (index = 0; index <= (factors.size() - 1); index = index + 2) {
			dummy = new BigInteger[(Integer) factors.get(index) + 1];
			for (columnIndex = (Integer) factors.get(index); columnIndex >= 0; columnIndex--) {
				dummy[columnIndex] = ((BigInteger[]) factors.get(index + 1))[columnIndex];
			}
			newFactors.add((Integer) factors.get(index));
			newFactors.add(dummy);
			newFactors.add(1);
		}
		factors.clear();

		for (index = 0; index <= (newFactors.size() - 1); index = index + 3) {
			dummy = new BigInteger[(Integer) newFactors.get(index) + 1];
			for (columnIndex = (Integer) newFactors.get(index); columnIndex >= 0; columnIndex--) {
				dummy[columnIndex] = ((BigInteger[]) newFactors.get(index + 1))[columnIndex];
			}
			factors.add((Integer) newFactors.get(index));
			factors.add(dummy);
			factors.add((Integer) newFactors.get(index + 2));
		}

		/* look for any 'repeat factors' */
		startPoint = 0;
		while (startPoint <= (factors.size() - 6)) {
			match = true;
			while (match == true) {
				match = false;
				loopHalt = false;
				runIndex = startPoint + 3;
				while ((runIndex <= (factors.size() - 3)) & (loopHalt == false)) {
					if ((Integer) factors.get(startPoint) == (Integer) factors.get(runIndex)) {
						halt = false;
						index = (Integer) factors.get(startPoint);
						while ((index >= 0) & (halt == false)) {
							if (((BigInteger[]) factors.get(startPoint + 1))[index].compareTo(((BigInteger[]) factors.get(runIndex + 1))[index]) != 0) {
								halt = true;
							} else {
								index--;
							}
						}

						if (halt == false) {
							match = true;
							loopHalt = true;

							sum = (Integer) factors.get(startPoint + 2) + (Integer) factors.get(runIndex + 2);
							factors.remove(startPoint + 2);
							factors.add(startPoint + 2, sum);

							factors.remove(runIndex);
							factors.remove(runIndex);
							factors.remove(runIndex);
						}
					}
					runIndex = runIndex + 3;
				}
			}
			startPoint = startPoint + 3;
		}

		/* uniquely order factors */
		passes = 1;
		while (passes != 0) {
			passes = 0;
			for (index = 0; index <= (factors.size() - 4); index = index + 3) {
				exchange = false;
				if (((Integer) factors.get(index + 2) > (Integer) factors.get(index + 5))
						|| (((Integer) factors.get(index + 2) == (Integer) factors.get(index + 5)) && ((Integer) factors.get(index) > (Integer) factors
								.get(index + 3)))) {
					exchange = true;
				}

				if ((((Integer) factors.get(index + 2) == (Integer) factors.get(index + 5)) && ((Integer) factors.get(index) == (Integer) factors
						.get(index + 3)))) {
					halt = false;
					runIndex = (Integer) factors.get(index);
					while ((runIndex >= 0) & (halt == false)) {
						if (((BigInteger[]) (factors.get(index + 1)))[runIndex].compareTo(((BigInteger[]) (factors.get(index + 4)))[runIndex]) == 0) {
							runIndex--;
						} else {
							halt = true;
						}
					}

					if (halt == true) {
						if ((((BigInteger[]) (factors.get(index + 1)))[runIndex].abs())
								.compareTo(((BigInteger[]) (factors.get(index + 4)))[runIndex].abs()) == 1) {
							exchange = true;
						}
					}
				}

				if (exchange == true) {
					dummyList.clear();
					dummyList.add(factors.get(index));
					dummyList.add(factors.get(index + 1));
					dummyList.add(factors.get(index + 2));
					factors.set(index, factors.get(index + 3));
					factors.set(index + 1, factors.get(index + 4));
					factors.set(index + 2, factors.get(index + 5));
					factors.set(index + 3, dummyList.get(0));
					factors.set(index + 4, dummyList.get(1));
					factors.set(index + 5, dummyList.get(2));
					passes++;
				}
			}
		}

		return (factors);
	}

	public static List zassenhausAlgorithm(BigInteger[] aPolynomial, int aPolynomialDegree) {

		/*
		 * finds at most two factors of a univariate, squarefree, primitive
		 * polynomial over the integers, uses the Zassenhaus-Berlekamp algorithm
		 * note: this is not a front-end procedure
		 */

		BigInteger[][][] dummyOne;
		BigInteger[][][] dummyTwo;
		BigInteger[] sBezout;
		BigInteger[] tBezout;
		BigInteger[] dummy;
		BigInteger[] Gpolynomial;
		BigInteger[] Hpolynomial;
		BigInteger[] trialFactor = new BigInteger[0];
		BigInteger[] Ppolynomial;
		BigInteger[] PprimePolynomial;
		BigInteger[] copyArray;
		BigInteger[] otherFactor = new BigInteger[0];
		BigInteger[] normalisedPolynomial;

		BigInteger leadingCoefficient = aPolynomial[aPolynomialDegree];
		BigInteger highestAbsoluteCoefficient;
		BigInteger highestAbsoluteGpolynomialCoefficient;
		BigInteger highestAbsoluteHpolynomialCoefficient;
		BigInteger testPrime;
		BigInteger modifier = BigInteger.ZERO;
		BigInteger inverseElement;
		BigInteger content;

		int runIndex;
		int index;
		int value;
		int rowIndex;
		int columnIndex;
		int attemptNumber;
		int partitionNumber = 0;
		int pValue;
		int dummyOneDegree;
		int dummyTwoDegree;
		int trialFactorDegree = 0;
		int toleranceLimit = 50;
		int otherFactorDegree = 0;
		int kIndex;
		int sBezoutDegree;
		int tBezoutDegree;
		int HpolynomialDegree;
		int dummyDegree;
		int GpolynomialDegree;
		int removalIndex;
		int attemptIndex;
		int indexCount;
		int normalisedPolynomialDegree;
		int PpolynomialPrimeDegree = aPolynomialDegree - 1;

		boolean halt;
		boolean loopHalt;
		boolean QdenominatorsAllOne;

		Random generator = new Random();

		List polynomialDivisionResult;
		List extendedEuclideanAlgorithmModularResult;
		List polynomialArithmeticModularResult;
		List berlekampFactorizationResult;
		List henselStepResult;
		List inverseElementResult;
		List<Integer> removalSet = new ArrayList<Integer>();
		List<Integer> removalSetCompliment = new ArrayList<Integer>();
		List<Object> Glist = new ArrayList<Object>();
		List<Object> Hlist = new ArrayList<Object>();
		List<Object> factors = new ArrayList<Object>();
		List<Object> resultFactors = new ArrayList<Object>();

		/* find value of highest absolute coefficient */
		highestAbsoluteCoefficient = BigInteger.ZERO;
		for (index = 0; index <= aPolynomialDegree; index++) {
			if ((aPolynomial[index].abs()).compareTo(highestAbsoluteCoefficient) == 1) {
				highestAbsoluteCoefficient = aPolynomial[index].abs();
			}
		}

		/* initialise 'P' */
		Ppolynomial = new BigInteger[aPolynomialDegree + 1];
		for (index = 0; index <= aPolynomialDegree; index++) {
			Ppolynomial[index] = aPolynomial[index];
		}

		/* find derivative of 'P' */
		PprimePolynomial = new BigInteger[aPolynomialDegree];
		for (index = PpolynomialPrimeDegree; index >= 0; index--) {
			PprimePolynomial[index] = Ppolynomial[index + 1].multiply(BigInteger.valueOf(index + 1));
		}

		/* find value of 'p' such that gcd(P,P') = 1 over Fp */
		pValue = 2;
		halt = false;
		while (halt == false) {
			testPrime = BigInteger.valueOf(pValue);
			if (testPrime.isProbablePrime(100) == true) {
				extendedEuclideanAlgorithmModularResult = extendedEuclideanAlgorithmModular(Ppolynomial, PprimePolynomial,
						aPolynomialDegree, PpolynomialPrimeDegree, testPrime); // BigInteger.valueOf(pValue));
				if ((Boolean) extendedEuclideanAlgorithmModularResult.get(0) == false) {
					if ((((Integer) extendedEuclideanAlgorithmModularResult.get(1) == 0) && (((BigInteger[]) extendedEuclideanAlgorithmModularResult
							.get(2))[0].compareTo(BigInteger.ONE) == 0))
							&& ((leadingCoefficient.divideAndRemainder(BigInteger.valueOf(pValue)))[1].compareTo(BigInteger.ZERO) != 0)) {
						halt = true;
					} else {
						pValue++;
					}
				} else {
					pValue++;
				}
			} else {
				pValue++;
			}
		}

		normalisedPolynomialDegree = aPolynomialDegree;
		normalisedPolynomial = new BigInteger[normalisedPolynomialDegree + 1];
		for (index = 0; index <= normalisedPolynomialDegree; index++) {
			normalisedPolynomial[index] = aPolynomial[index];
		}
		BigInteger pValueB = BigInteger.valueOf(pValue);
		inverseElementResult = multiplicativeInverse(leadingCoefficient, pValueB);
		inverseElement = (BigInteger) inverseElementResult.get(1);

		/* normalise polynomial over Fp */
		for (index = 0; index <= normalisedPolynomialDegree; index++) {
			normalisedPolynomial[index] = normalisedPolynomial[index].multiply(inverseElement);

			if ((normalisedPolynomial[index].abs()).compareTo(pValueB) != -1) {
				normalisedPolynomial[index] = (normalisedPolynomial[index].divideAndRemainder(pValueB))[1];
			}

			if (normalisedPolynomial[index].compareTo(BigInteger.ZERO) == -1) {
				normalisedPolynomial[index] = normalisedPolynomial[index].add(pValueB);
			}
		}

		/* factorize 'P' over Fp */
		berlekampFactorizationResult = berlekampAlgorithm(normalisedPolynomial, normalisedPolynomialDegree, pValue);

		/*
		 * calculate number of ways of selecting two partitions from the number of
		 * factors
		 */
		attemptNumber = 1;
		for (index = (berlekampFactorizationResult.size() / 2); index > (berlekampFactorizationResult.size() / 2) - 2; index--) {
			attemptNumber = attemptNumber * index;
		}
		attemptNumber = 10 * (attemptNumber / 2);

		if ((berlekampFactorizationResult.size() / 2) > 1) {
			indexCount = 0;
			for (runIndex = 0; runIndex <= (berlekampFactorizationResult.size() - 1); runIndex = runIndex + 2) {
				indexCount = indexCount + runIndex;
				copyArray = new BigInteger[(Integer) berlekampFactorizationResult.get(runIndex) + 1];
				factors.add((Integer) berlekampFactorizationResult.get(runIndex));
				for (index = (Integer) berlekampFactorizationResult.get(runIndex); index >= 0; index--) {
					copyArray[index] = ((BigInteger[]) berlekampFactorizationResult.get(runIndex + 1))[index];
				}
				factors.add(copyArray);
			}

			loopHalt = false;
			attemptIndex = 1;
			while ((loopHalt == false) & (attemptIndex <= attemptNumber)) {
				/* initialise 'G' and 'H' lists */
				Glist.clear();
				Hlist.clear();
				removalSet.clear();
				removalSetCompliment.clear();
				for (runIndex = 0; runIndex <= (berlekampFactorizationResult.size() - 1); runIndex = runIndex + 2) {
					removalSet.add(runIndex);
				}

				halt = false;
				while (halt == false) {
					partitionNumber = generator.nextInt((berlekampFactorizationResult.size() / 2));
					if (partitionNumber != 0) {
						halt = true;
					}
				}

				/* construct 'removal set' */
				for (index = 1; index <= partitionNumber; index++) {
					removalIndex = generator.nextInt(removalSet.size());
					value = removalSet.get(removalIndex);
					removalSet.remove(removalIndex);
					removalSetCompliment.add(value);
				}

				for (runIndex = 0; runIndex <= (removalSet.size() - 1); runIndex++) {
					Glist.add(factors.get(removalSet.get(runIndex)));
					Glist.add(factors.get(removalSet.get(runIndex) + 1));
				}

				for (runIndex = 0; runIndex <= (removalSetCompliment.size() - 1); runIndex++) {
					Hlist.add(factors.get(removalSetCompliment.get(runIndex)));
					Hlist.add(factors.get(removalSetCompliment.get(runIndex) + 1));
				}

				/* form 'G' & 'H' polynomials */
				if (removalSet.size() == 1) {
					/* contains only one factor */
					GpolynomialDegree = (Integer) Glist.get(0);
					Gpolynomial = new BigInteger[GpolynomialDegree + 1];
					for (index = GpolynomialDegree; index >= 0; index--) {
						Gpolynomial[index] = ((BigInteger[]) Glist.get(1))[index];
					}
				} else {
					/* contains two or more factors so need to multiply */
					GpolynomialDegree = (Integer) Glist.get(0);
					Gpolynomial = new BigInteger[GpolynomialDegree + 1];
					for (index = GpolynomialDegree; index >= 0; index--) {
						Gpolynomial[index] = ((BigInteger[]) Glist.get(1))[index];
					}

					for (runIndex = 0; runIndex <= (Glist.size() - 3); runIndex = runIndex + 2) {
						dummyDegree = (Integer) Glist.get(runIndex + 2);
						dummy = new BigInteger[dummyDegree + 1];
						for (index = dummyDegree; index >= 0; index--) {
							dummy[index] = ((BigInteger[]) Glist.get(runIndex + 3))[index];
						}

						polynomialArithmeticModularResult = polynomialArithmeticModular(Gpolynomial, dummy, GpolynomialDegree, dummyDegree,
								pValueB, '*');
						GpolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
						Gpolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);
					}
				}

				if (removalSetCompliment.size() == 1) {
					/* contains only one factor */
					HpolynomialDegree = (Integer) Hlist.get(0);
					Hpolynomial = new BigInteger[HpolynomialDegree + 1];
					for (index = HpolynomialDegree; index >= 0; index--) {
						Hpolynomial[index] = ((BigInteger[]) Hlist.get(1))[index];
					}
				} else {
					/* contains two or more factors so need to multiply */
					HpolynomialDegree = (Integer) Hlist.get(0);
					Hpolynomial = new BigInteger[HpolynomialDegree + 1];
					for (index = HpolynomialDegree; index >= 0; index--) {
						Hpolynomial[index] = ((BigInteger[]) Hlist.get(1))[index];
					}

					for (runIndex = 0; runIndex <= (Hlist.size() - 3); runIndex = runIndex + 2) {
						dummyDegree = (Integer) Hlist.get(runIndex + 2);
						dummy = new BigInteger[dummyDegree + 1];
						for (index = dummyDegree; index >= 0; index--) {
							dummy[index] = ((BigInteger[]) Hlist.get(runIndex + 3))[index];
						}

						polynomialArithmeticModularResult = polynomialArithmeticModular(Hpolynomial, dummy, HpolynomialDegree, dummyDegree,
								pValueB, '*');
						HpolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
						Hpolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);
					}
				}

				/* multiply Gpolynomial by 'leading coefficient' over Fp */
				for (index = 0; index <= GpolynomialDegree; index++) {
					Gpolynomial[index] = Gpolynomial[index].multiply(leadingCoefficient);

					if ((Gpolynomial[index].abs()).compareTo(pValueB) != -1) {
						Gpolynomial[index] = (Gpolynomial[index].divideAndRemainder(pValueB))[1];
					}

					if (Gpolynomial[index].compareTo(BigInteger.ZERO) == -1) {
						Gpolynomial[index] = Gpolynomial[index].add(pValueB);
					}
				}

				/* find GCD of 'G' & 'H' */
				extendedEuclideanAlgorithmModularResult = extendedEuclideanAlgorithmModular(Gpolynomial, Hpolynomial, GpolynomialDegree,
						HpolynomialDegree, pValueB);
				sBezoutDegree = (Integer) extendedEuclideanAlgorithmModularResult.get(3);
				sBezout = (BigInteger[]) extendedEuclideanAlgorithmModularResult.get(4);
				tBezoutDegree = (Integer) extendedEuclideanAlgorithmModularResult.get(5);
				tBezout = (BigInteger[]) extendedEuclideanAlgorithmModularResult.get(6);

				if ((((Integer) extendedEuclideanAlgorithmModularResult.get(1) == 0) && (((BigInteger[]) extendedEuclideanAlgorithmModularResult
						.get(2))[0].compareTo(BigInteger.ONE) == 0))) {
					/* carry out 'hensel lifting' */
					kIndex = 1;
					halt = false;
					while ((kIndex <= toleranceLimit) & (halt == false)) {
						henselStepResult = henselStep(Ppolynomial, Gpolynomial, Hpolynomial, sBezout, tBezout, aPolynomialDegree,
								GpolynomialDegree, HpolynomialDegree, sBezoutDegree, tBezoutDegree, pValueB.pow(kIndex));

						GpolynomialDegree = (Integer) henselStepResult.get(0);
						Gpolynomial = (BigInteger[]) henselStepResult.get(1);
						HpolynomialDegree = (Integer) henselStepResult.get(2);
						Hpolynomial = (BigInteger[]) henselStepResult.get(3);
						sBezoutDegree = (Integer) henselStepResult.get(4);
						sBezout = (BigInteger[]) henselStepResult.get(5);
						tBezoutDegree = (Integer) henselStepResult.get(6);
						tBezout = (BigInteger[]) henselStepResult.get(7);

						highestAbsoluteGpolynomialCoefficient = BigInteger.ZERO;
						for (index = 0; index <= GpolynomialDegree; index++) {
							if ((Gpolynomial[index].abs()).compareTo(highestAbsoluteGpolynomialCoefficient) == 1) {
								highestAbsoluteGpolynomialCoefficient = Gpolynomial[index].abs();
							}
						}

						highestAbsoluteHpolynomialCoefficient = BigInteger.ZERO;
						for (index = 0; index <= HpolynomialDegree; index++) {
							if ((Hpolynomial[index].abs()).compareTo(highestAbsoluteHpolynomialCoefficient) == 1) {
								highestAbsoluteHpolynomialCoefficient = Hpolynomial[index].abs();
							}
						}

						if (((highestAbsoluteGpolynomialCoefficient).compareTo(highestAbsoluteCoefficient) == 1)
								|| ((highestAbsoluteHpolynomialCoefficient).compareTo(highestAbsoluteCoefficient) == 1)) {
							halt = true;
							modifier = (pValueB.pow(kIndex)).pow(2);
						} else {
							kIndex++;
						}

					}

					/* form 'trial factor' */
					trialFactorDegree = GpolynomialDegree;
					trialFactor = new BigInteger[trialFactorDegree + 1];
					for (index = GpolynomialDegree; index >= 0; index--) {
						if ((Gpolynomial[index].abs()).compareTo(highestAbsoluteCoefficient) == 1) {
							trialFactor[index] = Gpolynomial[index].subtract(modifier);
						} else {
							trialFactor[index] = Gpolynomial[index];
						}
					}

					/* remove any 'content' from 'trial factor' */
					content = trialFactor[0].gcd(trialFactor[1]);

					if (trialFactorDegree > 1) {
						for (index = 2; index <= trialFactorDegree; index++) {
							content = content.gcd(trialFactor[index]);
						}
					}

					for (index = 0; index <= trialFactorDegree; index++) {
						trialFactor[index] = trialFactor[index].divide(content);
					}

					/* divide polynomial by 'trial factor' */
					dummyOneDegree = aPolynomialDegree;
					dummyOne = new BigInteger[dummyOneDegree + 1][3][3];
					for (index = 0; index <= aPolynomialDegree; index++) {
						dummyOne[index][1][1] = Ppolynomial[index];
						dummyOne[index][1][2] = BigInteger.ONE;

						dummyOne[index][2][1] = BigInteger.ZERO;
						dummyOne[index][2][2] = BigInteger.ONE;
					}

					dummyTwoDegree = trialFactorDegree;
					dummyTwo = new BigInteger[dummyTwoDegree + 1][3][3];
					for (index = 0; index <= trialFactorDegree; index++) {
						dummyTwo[index][1][1] = trialFactor[index];
						dummyTwo[index][1][2] = BigInteger.ONE;

						dummyTwo[index][2][1] = BigInteger.ZERO;
						dummyTwo[index][2][2] = BigInteger.ONE;
					}

					polynomialDivisionResult = polynomialDivision(dummyOne, dummyTwo, dummyOneDegree, dummyTwoDegree);

					QdenominatorsAllOne = true;
					index = 0;
					while ((index <= (Integer) polynomialDivisionResult.get(0)) & (QdenominatorsAllOne == true)) {
						if (((BigInteger[][][]) polynomialDivisionResult.get(2))[index][1][2].compareTo(BigInteger.ONE) != 0) {
							QdenominatorsAllOne = false;
						} else {
							index++;
						}
					}

					otherFactorDegree = (Integer) polynomialDivisionResult.get(0);
					otherFactor = new BigInteger[otherFactorDegree + 1];
					for (index = otherFactorDegree; index >= 0; index--) {
						otherFactor[index] = ((BigInteger[][][]) polynomialDivisionResult.get(2))[index][1][1];
					}

					if ((((Integer) polynomialDivisionResult.get(1) == 0) & (((BigInteger[][][]) polynomialDivisionResult.get(3))[0][1][1]
							.compareTo(BigInteger.ZERO) == 0))
							& (QdenominatorsAllOne == true)) {
						loopHalt = true;
					} else {
						/* try another partition */
						attemptIndex++;
					}

				} else {
					/* try another partition */
					attemptIndex++;
				}
			}

			if (loopHalt == true) {
				resultFactors.add(trialFactorDegree);
				resultFactors.add(trialFactor);
				resultFactors.add(otherFactorDegree);
				resultFactors.add(otherFactor);
			} else {
				/* no factors found return as irreducible */
				resultFactors.add(aPolynomialDegree);
				resultFactors.add(Ppolynomial);
			}

		} else {
			/* 'P' is irreducible */
			resultFactors.add(aPolynomialDegree);
			resultFactors.add(Ppolynomial);
		}

		return (resultFactors);
	}

  
  
  
  
	public static List berlekampAlgorithm(BigInteger[] aPolynomial, int aPolynomialDegree, int qValue)
		{    
		    /* Returns a factorization for a squarefree, univariate, monic polynomial over
		     * Fq, where 'q' is necessarily prime. This is not a 'front-end'
		     * procedure.  Note also that no provision has to be made for the
		     * polynomial being non-monic as the square-free procedure will deal
		     * with that eventuality
		     */
		    BigInteger[][] berlekampMatrix;
		    BigInteger[][] Mmatrix;
		    BigInteger[] vVector;
		    BigInteger[] multipliers;
		    BigInteger[] row;
		    BigInteger[] berlekampProduct;
		    BigInteger[] newRow;
		    BigInteger[] originalRow;
		    BigInteger[] gPolynomial;
		    BigInteger[] uPolynomial;
		    
		    BigInteger inverseElement;
		    BigInteger dummy;
		    BigInteger originalElement;
		    
		    int uPolynomialDegree;
		    int gPolynomialDegree;
		    int gcdIndex;
		    int sValue;
		    int vVectorDegree;
		    int rIndex;
		    int kIndex;
		    int kValue;
		    int index;
		    int mIndex;
		    int factorIndex;
		    int rowIndex;
		    int columnIndex;
		    int berlekampSize;
		    int nValue;
		    int iIndex;
		    
		    
		    boolean halt;
		    boolean gcdEqualsU;
		    boolean pivotColumnFound;
		    boolean pass;
		    
		    
		    List modularGCDresult;
		    List polynomialDivisionOverFiniteFieldResult;
		    List inverseElementResult;
		    List<Object>factors = new ArrayList<Object>();
		    List<BigInteger[]>vVectorsList = new ArrayList<BigInteger[]>();

		    
		    
		    /* convert coefficients so that they lie in the range 0 to (qValue - 1) */
		    for (index = 0; index <= aPolynomialDegree; index++) {
		        if ((aPolynomial[index].abs()).compareTo(new BigInteger(String.valueOf(qValue))) != -1) {
		            aPolynomial[index] = (aPolynomial[index].divideAndRemainder(new BigInteger(String.valueOf(qValue))))[1];
		        }
		        
		        if (aPolynomial[index].compareTo(BigInteger.ZERO) == -1) {
		            aPolynomial[index] = aPolynomial[index].add(new BigInteger(String.valueOf(qValue)));
		        }
		    }
		    
		    /* carry out 'degree reduction' */
		    halt = false;
		    while ((halt == false) && (aPolynomialDegree > 0)) {
		        if (aPolynomial[aPolynomialDegree].compareTo(BigInteger.ZERO) != 0) {
		            halt = true;
		        }
		        else {
		            aPolynomialDegree--;
		        }
		    }
		    
			
		    if (aPolynomialDegree > 0) {
		        nValue = aPolynomialDegree;
			    berlekampMatrix = new BigInteger[nValue][nValue];
			    Mmatrix = new BigInteger[nValue][nValue];

			
			    /* write (1,0,...,0) to row zero */
			    for (columnIndex = 0; columnIndex <= (nValue - 1); columnIndex++) {
			        if (columnIndex == 0) {
			            berlekampMatrix[0][columnIndex] = BigInteger.ONE;
			        }
			        else {
			            berlekampMatrix[0][columnIndex] = BigInteger.ZERO;
			        }
			    }
			
			    berlekampProduct = new BigInteger[nValue];
		        newRow = new BigInteger[nValue];
		        originalRow = new BigInteger[nValue];
		        row = new BigInteger[nValue];
		    
		        for (index = (nValue - 1); index >= 0; index--) {
		            berlekampProduct[index] = aPolynomial[index].negate();
		            newRow[index] = berlekampProduct[index];
		            originalRow[index] = berlekampProduct[index];
		        }
			
			    for (mIndex = 1; mIndex <= (nValue - 1) * qValue; mIndex++) {
			        /* find x^q */
		            if (nValue < mIndex) {
		                for (index = (nValue - 1); index >= 1; index--) {
		                    newRow[index] = (berlekampProduct[nValue - 1].multiply(originalRow[index])).add(berlekampProduct[index - 1]);
		                
		                    if ((newRow[index].abs()).compareTo(new BigInteger(String.valueOf(qValue))) != -1) {
		                        newRow[index] = (newRow[index].divideAndRemainder(new BigInteger(String.valueOf(qValue))))[1];
		                    }
		        
		                    if (newRow[index].compareTo(BigInteger.ZERO) == -1) {
		                        newRow[index] = newRow[index].add(new BigInteger(String.valueOf(qValue)));
		                    }
		                }
		            
		                newRow[0] = berlekampProduct[nValue - 1].multiply(originalRow[0]);
		                
		                if ((newRow[0].abs()).compareTo(new BigInteger(String.valueOf(qValue))) != -1) {
		                    newRow[0] = (newRow[0].divideAndRemainder(new BigInteger(String.valueOf(qValue))))[1];
		                }
		        
		                if (newRow[0].compareTo(BigInteger.ZERO) == -1) {
		                    newRow[0] = newRow[0].add(new BigInteger(String.valueOf(qValue)));
		                }
		            
		                for (index = (nValue - 1); index >= 0; index--) {
		                    berlekampProduct[index] = newRow[index];
		                }
		            }
		            else {
		                if (nValue > mIndex) {
		                    for (index = (nValue - 1); index >= 0; index--) {
		                        if (index == mIndex) {
		                            newRow[index] = BigInteger.ONE;
		                        }
		                        else {
		                            newRow[index] = BigInteger.ZERO;
		                        }
		                    }
		                }
		                else {
		                    for (index = (nValue - 1); index >= 0; index--) {
		                        newRow[index] = aPolynomial[index].negate();
		                        if (newRow[index].compareTo(BigInteger.ZERO) == -1) {
		                            newRow[index] = newRow[index].add(new BigInteger(String.valueOf(qValue)));
		                        }
		                    }
		                }
		            }

			    
			        if (mIndex%qValue == 0) {
		                for (index = (nValue - 1); index >= 0; index--) {
		                    berlekampMatrix[mIndex/qValue][index] = newRow[index];
		                }
			        }
		        }
		    
		    
		        for (rowIndex = 0; rowIndex <= (nValue - 1); rowIndex++) {
		            for (columnIndex = 0; columnIndex <= (nValue - 1); columnIndex++) {
		                if (rowIndex == columnIndex) {
		                    Mmatrix[rowIndex][columnIndex] = berlekampMatrix[rowIndex][columnIndex].subtract(BigInteger.ONE);
		                    if (Mmatrix[rowIndex][columnIndex].compareTo(BigInteger.ZERO) == -1) {
		                        Mmatrix[rowIndex][columnIndex] = Mmatrix[rowIndex][columnIndex].add(new BigInteger(String.valueOf(qValue)));
		                    }
		                }
		                else {
		                    Mmatrix[rowIndex][columnIndex] = berlekampMatrix[rowIndex][columnIndex];
		                }
		            }
		        }
		        
		        
		        for (kIndex = 1; kIndex <= (nValue - 1); kIndex++) {
		            iIndex = kIndex;
		            halt = false;
		            while ((iIndex <= (nValue - 1)) & (halt == false)) {
		                if (Mmatrix[kIndex][iIndex].compareTo(BigInteger.ZERO) == 0) {
		                    iIndex++;
		                }
		                else {
		                    halt = true;
		                }
		            }
		            
		            pass = false;
		            if (halt == true) {
		                pass = true;
		            }
		            
		            if ((halt == false) && (kIndex < (nValue - 1))) {
		                iIndex = 0;
		                halt = false;
		                pivotColumnFound = false;
		                while ((iIndex <= (kIndex - 1)) && (pivotColumnFound == false)) {
		                    if (Mmatrix[kIndex][iIndex].compareTo(BigInteger.ZERO) == 0) {
		                        iIndex++;
		                    }
		                    else {
		                        rowIndex = 0;
		                        halt = false;
		                        while ((rowIndex < kIndex) && (halt == false)) {
		                            if (Mmatrix[rowIndex][iIndex].compareTo(BigInteger.ZERO) == 0) {
		                                rowIndex++;
		                            }
		                            else {
		                                halt = true;
		                            }
		                        }
		                        
		                        if (halt == false) {
		                            pivotColumnFound = true;
		                            halt = true;
		                        }
		                        else {
		                            iIndex++;
		                        }
		                    }
		                }
		                
		                if (pivotColumnFound == true) {
		                    pass = true;
		                }
		                else {
		                    halt = false;
		                }
		            }
		            
		        
		            if (pass == true) {
		                /* find inverse element */
		                inverseElementResult = multiplicativeInverse(Mmatrix[kIndex][iIndex], new BigInteger(String.valueOf(qValue)));
		                inverseElement = (BigInteger)inverseElementResult.get(1);
		                
		                /* nomalize column 'i' */
		                for (rowIndex = 0; rowIndex <= (nValue - 1); rowIndex++) {
		                    Mmatrix[rowIndex][iIndex] = Mmatrix[rowIndex][iIndex].multiply(inverseElement);
		                    
		                    if ((Mmatrix[rowIndex][iIndex].abs()).compareTo(new BigInteger(String.valueOf(qValue))) != -1) {
		                        Mmatrix[rowIndex][iIndex] = (Mmatrix[rowIndex][iIndex].divideAndRemainder(new BigInteger(String.valueOf(qValue))))[1];
		                    }
		                }
		            
		                /* interchange this column with column 'k' */
		                for (rowIndex = 0; rowIndex <= (nValue - 1); rowIndex++) {
		                    dummy = Mmatrix[rowIndex][iIndex];
		                    Mmatrix[rowIndex][iIndex] = Mmatrix[rowIndex][kIndex];
		                    Mmatrix[rowIndex][kIndex] = dummy;
		                }
		            
		            
		                /* eliminate rest of row k */
		                multipliers = new BigInteger[nValue];
		                for (iIndex = 0; iIndex <= (nValue - 1); iIndex++) {
		                    multipliers[iIndex] = Mmatrix[kIndex][iIndex];
		                }
		                for (iIndex = 0; iIndex <= (nValue - 1); iIndex++) {
		                    if (iIndex != kIndex) {
		                        for (rowIndex = 1; rowIndex <= (nValue - 1); rowIndex++) {
		                            Mmatrix[rowIndex][iIndex] = Mmatrix[rowIndex][iIndex].subtract((Mmatrix[rowIndex][kIndex].multiply(multipliers[iIndex])));
		                        
		                            if ((Mmatrix[rowIndex][iIndex].abs()).compareTo(new BigInteger(String.valueOf(qValue))) != -1) {
		                                Mmatrix[rowIndex][iIndex] = (Mmatrix[rowIndex][iIndex].divideAndRemainder(new BigInteger(String.valueOf(qValue))))[1];
		                            }
		        
		                            if (Mmatrix[rowIndex][iIndex].compareTo(BigInteger.ZERO) == -1) {
		                                Mmatrix[rowIndex][iIndex] = Mmatrix[rowIndex][iIndex].add(new BigInteger(String.valueOf(qValue)));
		                            }
		                        }
		                    }
		                }
		            
		            }
		        }
		        
		        /* convert 'M' to 'M - I' */
		        for (rowIndex = 0; rowIndex <= (nValue - 1); rowIndex++) {
		            for (columnIndex = 0; columnIndex <= (nValue - 1); columnIndex++) {
		                if (rowIndex == columnIndex) {
		                    Mmatrix[rowIndex][columnIndex] = (BigInteger.ONE).subtract(Mmatrix[rowIndex][columnIndex]);
		                }
		                else {
		                    Mmatrix[rowIndex][columnIndex] = (BigInteger.ZERO).subtract(Mmatrix[rowIndex][columnIndex]);
		                }
		                
		                if ((Mmatrix[rowIndex][columnIndex].abs()).compareTo(new BigInteger(String.valueOf(qValue))) != -1) {
		                    Mmatrix[rowIndex][columnIndex] = (Mmatrix[rowIndex][columnIndex].divideAndRemainder(new BigInteger(String.valueOf(qValue))))[1];
		                }
		        
		                if (Mmatrix[rowIndex][columnIndex].compareTo(BigInteger.ZERO) == -1) {
		                    Mmatrix[rowIndex][columnIndex] = Mmatrix[rowIndex][columnIndex].add(new BigInteger(String.valueOf(qValue)));
		                }
		            }
		        }
		        
		        
		        /* read off non-zero rows */
		        for (rowIndex = 0; rowIndex <= (nValue - 1); rowIndex++) {
		            columnIndex = 0;
		            halt = false;
		            while ((columnIndex <= (nValue - 1)) && (halt == false)) {
		                if (Mmatrix[rowIndex][columnIndex].compareTo(BigInteger.ZERO) != 0) {
		                    halt = true;
		                }
		                else {
		                    columnIndex++;
		                }
		            }
		    
		            if (halt == true) {
		                row = new BigInteger[nValue];
		                for (index = 0; index <= (nValue - 1); index++) {
		                    row[index] = Mmatrix[rowIndex][index];
		                }
		            
		                vVectorsList.add(row);
		            }
		        }
		    

		        kValue = vVectorsList.size();
		        factorIndex = 0;
		        factors.add(aPolynomialDegree);
		        factors.add(aPolynomial);
		        uPolynomial = new BigInteger[nValue + 1];
		        while ((factors.size() < 2 * kValue) && (factorIndex <= factors.size() - 1)) {
		            /* define 'u polynomial' */
		            uPolynomialDegree = (Integer)factors.get(factorIndex);
		            uPolynomial = new BigInteger[uPolynomialDegree + 1];
		            for (index = 0; index <= (Integer)factors.get(factorIndex); index++) {
		                uPolynomial[index] = ((BigInteger[])factors.get(factorIndex + 1))[index];
		            }
		        
		        
		            rIndex = 1;
		            while (rIndex <= (kValue - 1)) {
		                /* find degree of 'v vector' */
		                halt = false;
		                vVectorDegree = nValue - 1;
		                while ((vVectorDegree >= 0) & (halt == false)) {
		                    if (((BigInteger[])vVectorsList.get(rIndex))[vVectorDegree].compareTo(BigInteger.ZERO) == 0) {
		                        vVectorDegree--;
		                    }
		                    else {
		                        halt = true;
		                    }
		                }

		    
		                /* initialise 'v vector' */
		                vVector = new BigInteger[vVectorDegree + 1];
		                for (columnIndex = 0; columnIndex <= vVectorDegree; columnIndex++) {
		                    vVector[columnIndex] = ((BigInteger[])vVectorsList.get(rIndex))[columnIndex];
		                }
		    
		                originalElement = vVector[0];
		            
		                sValue = 0;
		                halt = false;
		                while ((sValue <= (qValue - 1)) & (halt == false)) {
		                    vVector[0] = vVector[0].add(new BigInteger(String.valueOf(sValue)));

		                    modularGCDresult = extendedEuclideanAlgorithmModular(uPolynomial, vVector, uPolynomialDegree, vVectorDegree, new BigInteger(String.valueOf(qValue)));
		                    
	                        if ((Boolean)modularGCDresult.get(0) == false) {
		                        gPolynomialDegree = (Integer)modularGCDresult.get(1);
		                        gPolynomial = (BigInteger[])modularGCDresult.get(2);
		                        
		                        
		                        if (gPolynomialDegree == uPolynomialDegree) {
		                            gcdEqualsU = true;
		                            gcdIndex = 0;
		                            while ((gcdIndex <= gPolynomialDegree) & (gcdEqualsU == true)) {
		                                if (gPolynomial[gcdIndex].compareTo(uPolynomial[gcdIndex]) != 0) {
		                                    gcdEqualsU = false;
		                                }
		                                else {
		                                    gcdIndex++;
		                                }
		                            }
		                        }
		                        else {
		                            gcdEqualsU = false;
		                        }

		        
		                        if ((!((gPolynomialDegree == 0) && (gPolynomial[0].compareTo(BigInteger.ONE) == 0))) && (gcdEqualsU == false)) {
	                                /* factor found  */
	                                halt = true;
	                                factors.remove(factorIndex);
	                                factors.remove(factorIndex);
	                            
	                                polynomialDivisionOverFiniteFieldResult = polynomialDivisionOverFiniteField(uPolynomial, gPolynomial, uPolynomialDegree, gPolynomialDegree, new BigInteger(String.valueOf(qValue)));
	                                uPolynomialDegree = (Integer)polynomialDivisionOverFiniteFieldResult.get(1);
	                                uPolynomial = (BigInteger[])polynomialDivisionOverFiniteFieldResult.get(3);
	                            
	                                factors.add(factorIndex, gPolynomial);
	                                factors.add(factorIndex, gPolynomialDegree);
	                            
	                                factors.add(factorIndex, uPolynomial);
	                                factors.add(factorIndex, uPolynomialDegree);
		                        }
		                        else {
		                            sValue++;
		                        }
		                        vVector[0] = originalElement;
		                    }
		                    else {
		                        sValue++;
		                    }
		                }

		                if (halt == false) {
		                    rIndex++;
		                }
		            }
		        
		            if (rIndex > (kValue - 1)) {
		                factorIndex = factorIndex + 2;
		            }
		        
		        }
		    }
		    else {
		        factors.add(aPolynomialDegree);
		        factors.add(aPolynomial);
		    }
		    
		    
		    
		    return(factors);
		}
	public static List henselStep(BigInteger[] fPolynomial, BigInteger[] gPolynomial, BigInteger[] hPolynomial,
			BigInteger[] sPolynomial, BigInteger[] tPolynomial, int fPolynomialDegree, int gPolynomialDegree, int hPolynomialDegree,
			int sPolynomialDegree, int tPolynomialDegree, BigInteger mValue) {

		/*
		 * takes as input an element 'm' in a commutative ring, and polynomials
		 * f,g,h,s,t in R[x] such that :- f congruent to gh mod m and sg + th
		 * congruent to 1 mod m lc(f) must not be a zero divisor mod m, h is monic,
		 * deg f = n = deg g + deg h deg s < deg h and deg t < deg g
		 * 
		 * outputs polynomials g*,h*,s*,t* in R[x] such that f congruent to g*h* mod
		 * m^2 and s*g* + t*h* congruent to 1 mod m^2, h* is monic, g* congruent to
		 * g mod m h* congruent to h mod m, s* congruent to s mod m t* congruent to
		 * t mod m, deg g* = deg g, deg h* = deg h deg s* < deg h*, deg t* < deg g*
		 */

		BigInteger[] gStarPolynomial;
		BigInteger[] hStarPolynomial;
		BigInteger[] sbPolynomial;
		BigInteger[] tePolynomial;
		BigInteger[] qgPolynomial;
		BigInteger[] ghPolynomial;
		BigInteger[] sePolynomial;
		BigInteger[] qPolynomial;
		BigInteger[] rPolynomial;
		BigInteger[] ePolynomial;
		BigInteger[] sgStarPolynomial;
		BigInteger[] thStarPolynomial;
		BigInteger[] bPolynomial;
		BigInteger[] cPolynomial;
		BigInteger[] dPolynomial;
		BigInteger[] tStarPolynomial;
		BigInteger[] sStarPolynomial;
		BigInteger[] tbPolynomial;
		BigInteger[] cgStarPolynomial;
		BigInteger[] dummyPolynomial;
		BigInteger[] unityPolynomial = new BigInteger[1];

		int index;
		int gStarPolynomialDegree;
		int hStarPolynomialDegree;
		int tePolynomialDegree;
		int sbPolynomialDegree;
		int qgPolynomialDegree;
		int ghPolynomialDegree;
		int sePolynomialDegree;
		int qPolynomialDegree;
		int rPolynomialDegree;
		int ePolynomialDegree;
		int sgStarPolynomialDegree;
		int thStarPolynomialDegree;
		int bPolynomialDegree;
		int cPolynomialDegree;
		int dPolynomialDegree;
		int tStarPolynomialDegree;
		int sStarPolynomialDegree;
		int tbPolynomialDegree;
		int cgStarPolynomialDegree;
		int dummyPolynomialDegree;
		int unityPolynomialDegree = 0;

		boolean halt;

		List polynomialDivisionOverFiniteFieldResult;
		List polynomialArithmeticModularResult;
		List<Object> result = new ArrayList<Object>(4);

		unityPolynomial[0] = BigInteger.ONE;

		/* find 'gh' mod m^2 */
		polynomialArithmeticModularResult = polynomialArithmeticModular(gPolynomial, hPolynomial, gPolynomialDegree, hPolynomialDegree,
				mValue.pow(2), '*');
		ghPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		ghPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'e' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(fPolynomial, ghPolynomial, fPolynomialDegree,
				ghPolynomialDegree, mValue.pow(2), '-');
		ePolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		ePolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'se' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(sPolynomial, ePolynomial, sPolynomialDegree, ePolynomialDegree,
				mValue.pow(2), '*');
		sePolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		sePolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'q' and 'r' by dividing 'se' by 'h' mod m^2 */
		polynomialDivisionOverFiniteFieldResult = polynomialDivisionOverFiniteField(sePolynomial, hPolynomial, sePolynomialDegree,
				hPolynomialDegree, mValue.pow(2));
		qPolynomialDegree = (Integer) polynomialDivisionOverFiniteFieldResult.get(1);
		qPolynomial = (BigInteger[]) polynomialDivisionOverFiniteFieldResult.get(3);
		rPolynomialDegree = (Integer) polynomialDivisionOverFiniteFieldResult.get(2);
		rPolynomial = (BigInteger[]) polynomialDivisionOverFiniteFieldResult.get(4);

		/* find 'te' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(tPolynomial, ePolynomial, tPolynomialDegree, ePolynomialDegree,
				mValue.pow(2), '*');
		tePolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		tePolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'qg' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(qPolynomial, gPolynomial, qPolynomialDegree, gPolynomialDegree,
				mValue.pow(2), '*');
		qgPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		qgPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* form 'g*' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(gPolynomial, tePolynomial, gPolynomialDegree,
				tePolynomialDegree, mValue.pow(2), '+');
		dummyPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		dummyPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		polynomialArithmeticModularResult = polynomialArithmeticModular(dummyPolynomial, qgPolynomial, dummyPolynomialDegree,
				qgPolynomialDegree, mValue.pow(2), '+');
		gStarPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		gStarPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* form 'h*' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(hPolynomial, rPolynomial, hPolynomialDegree, rPolynomialDegree,
				mValue.pow(2), '+');
		hStarPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		hStarPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'sg*' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(sPolynomial, gStarPolynomial, sPolynomialDegree,
				gStarPolynomialDegree, mValue.pow(2), '*');
		sgStarPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		sgStarPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'th*' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(tPolynomial, hStarPolynomial, tPolynomialDegree,
				hStarPolynomialDegree, mValue.pow(2), '*');
		thStarPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		thStarPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* form 'b' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(sgStarPolynomial, thStarPolynomial, sgStarPolynomialDegree,
				thStarPolynomialDegree, mValue.pow(2), '+');
		dummyPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		dummyPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		polynomialArithmeticModularResult = polynomialArithmeticModular(dummyPolynomial, unityPolynomial, dummyPolynomialDegree,
				unityPolynomialDegree, mValue.pow(2), '-');
		bPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		bPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'sb' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(sPolynomial, bPolynomial, sPolynomialDegree, bPolynomialDegree,
				mValue.pow(2), '*');
		sbPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		sbPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'c' and 'd' by dividing 'sb' by 'h*' mod m^2 */
		polynomialDivisionOverFiniteFieldResult = polynomialDivisionOverFiniteField(sbPolynomial, hStarPolynomial, sbPolynomialDegree,
				hStarPolynomialDegree, mValue.pow(2));
		cPolynomialDegree = (Integer) polynomialDivisionOverFiniteFieldResult.get(1);
		cPolynomial = (BigInteger[]) polynomialDivisionOverFiniteFieldResult.get(3);
		dPolynomialDegree = (Integer) polynomialDivisionOverFiniteFieldResult.get(2);
		dPolynomial = (BigInteger[]) polynomialDivisionOverFiniteFieldResult.get(4);

		/* find 's*' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(sPolynomial, dPolynomial, sPolynomialDegree, dPolynomialDegree,
				mValue.pow(2), '-');
		sStarPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		sStarPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'tb' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(tPolynomial, bPolynomial, tPolynomialDegree, bPolynomialDegree,
				mValue.pow(2), '*');
		tbPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		tbPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* find 'cg*' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(cPolynomial, gStarPolynomial, cPolynomialDegree,
				gStarPolynomialDegree, mValue.pow(2), '*');
		cgStarPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		cgStarPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		/* form 't*' */
		polynomialArithmeticModularResult = polynomialArithmeticModular(tPolynomial, tbPolynomial, tPolynomialDegree,
				tbPolynomialDegree, mValue.pow(2), '-');
		dummyPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		dummyPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		polynomialArithmeticModularResult = polynomialArithmeticModular(dummyPolynomial, cgStarPolynomial, dummyPolynomialDegree,
				cgStarPolynomialDegree, mValue.pow(2), '-');
		tStarPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
		tStarPolynomial = (BigInteger[]) polynomialArithmeticModularResult.get(1);

		result.add(gStarPolynomialDegree);
		result.add(gStarPolynomial);
		result.add(hStarPolynomialDegree);
		result.add(hStarPolynomial);
		result.add(sStarPolynomialDegree);
		result.add(sStarPolynomial);
		result.add(tStarPolynomialDegree);
		result.add(tStarPolynomial);

		return (result);

	}

	public static List extendedEuclideanAlgorithmModular(BigInteger[] polynomialA, BigInteger[] polynomialB, int degreeA,
			int degreeB, BigInteger qValue) {

		/*
		 * finds the modular GCD of two polynomials over Fq and also the 'bezout
		 * identity' where xP1 + yP2 = gcd
		 * 
		 * neither polynomial may be the zero polynomial else an error is returned
		 */

		BigInteger[] polynomialOne;
		BigInteger[] polynomialTwo;
		BigInteger[] xPolynomial;
		BigInteger[] yPolynomial;
		BigInteger[] tempPolynomial;
		BigInteger[] product;
		BigInteger[] quotient;
		BigInteger[] dummy;
		BigInteger[] bezoutX = new BigInteger[0];
		BigInteger[] bezoutY = new BigInteger[0];
		BigInteger[] gcd = new BigInteger[0];

		BigInteger inverseElement;

		int degreeOne = degreeA;
		int degreeTwo = degreeB;
		int xPolynomialDegree;
		int yPolynomialDegree;
		int tempPolynomialDegree;
		int productDegree;
		int gcdDegree = 0;
		int index;
		int quotientDegree;
		int dummyDegree;
		int bezoutXdegree = 0;
		int bezoutYdegree = 0;

		boolean loopHalt;
		boolean halt;
		boolean interchange = false;
		boolean error;

		List polynomialDivisionOverFiniteFieldResult;
		List inverseElementResult;
		List polynomialArithmeticModularResult;

		List<Object> result = new ArrayList<Object>(4);

		polynomialOne = new BigInteger[degreeOne + 1];
		for (index = 0; index <= degreeOne; index++) {
			polynomialOne[index] = polynomialA[index];
		}

		polynomialTwo = new BigInteger[degreeTwo + 1];
		for (index = 0; index <= degreeTwo; index++) {
			polynomialTwo[index] = polynomialB[index];
		}

		/* check degrees of entered polynomials, ie. ensure deg(P1) > deg(P2) */
		if (degreeOne < degreeTwo) {
			interchange = true;
			/* interchange P1 and P2 */
			dummy = new BigInteger[degreeOne + 1];
			for (index = 0; index <= degreeOne; index++) {
				dummy[index] = polynomialOne[index];
			}

			polynomialOne = new BigInteger[degreeTwo + 1];
			for (index = 0; index <= degreeTwo; index++) {
				polynomialOne[index] = polynomialTwo[index];
			}

			polynomialTwo = new BigInteger[degreeOne + 1];
			for (index = 0; index <= degreeOne; index++) {
				polynomialTwo[index] = dummy[index];
			}

			dummyDegree = degreeOne;
			degreeOne = degreeTwo;
			degreeTwo = dummyDegree;
		}

		/* convert coefficients */
		for (index = 0; index <= degreeOne; index++) {
			if ((polynomialOne[index].abs()).compareTo(qValue) != -1) {
				polynomialOne[index] = (polynomialOne[index].divideAndRemainder(qValue))[1];
			}

			if (polynomialOne[index].compareTo(BigInteger.ZERO) == -1) {
				polynomialOne[index] = polynomialOne[index].add(qValue);
			}
		}

		for (index = 0; index <= degreeTwo; index++) {
			if ((polynomialTwo[index].abs()).compareTo(qValue) != -1) {
				polynomialTwo[index] = (polynomialTwo[index].divideAndRemainder(qValue))[1];
			}

			if (polynomialTwo[index].compareTo(BigInteger.ZERO) == -1) {
				polynomialTwo[index] = polynomialTwo[index].add(qValue);
			}
		}

		/* carry out 'degree reduction' */
		halt = false;
		while ((halt == false) & (degreeOne > 0)) {
			if (polynomialOne[degreeOne].compareTo(BigInteger.ZERO) != 0) {
				halt = true;
			} else {
				degreeOne--;
			}
		}

		halt = false;
		while ((halt == false) & (degreeTwo > 0)) {
			if (polynomialTwo[degreeTwo].compareTo(BigInteger.ZERO) != 0) {
				halt = true;
			} else {
				degreeTwo--;
			}
		}

		if ((!((degreeOne == 0) && (polynomialOne[degreeOne].compareTo(BigInteger.ZERO) == 0)))
				&& (!((degreeTwo == 0) && (polynomialTwo[degreeTwo].compareTo(BigInteger.ZERO) == 0)))) {
			error = false;
			/* initialise bezoutX, bezoutY, xPolynomial & yPolynomial */
			bezoutXdegree = 0;
			bezoutX = new BigInteger[bezoutXdegree + 1];
			bezoutX[0] = BigInteger.ONE;

			bezoutYdegree = 0;
			bezoutY = new BigInteger[bezoutYdegree + 1];
			bezoutY[0] = BigInteger.ZERO;

			xPolynomialDegree = 0;
			xPolynomial = new BigInteger[xPolynomialDegree + 1];
			xPolynomial[0] = BigInteger.ZERO;

			yPolynomialDegree = 0;
			yPolynomial = new BigInteger[yPolynomialDegree + 1];
			yPolynomial[0] = BigInteger.ONE;

			loopHalt = false;
			while (loopHalt == false) {
				polynomialDivisionOverFiniteFieldResult = polynomialDivisionOverFiniteField(polynomialOne, polynomialTwo, degreeOne,
						degreeTwo, qValue);

				quotientDegree = (Integer) polynomialDivisionOverFiniteFieldResult.get(1);
				quotient = new BigInteger[quotientDegree + 1];
				for (index = 0; index <= quotientDegree; index++) {
					quotient[index] = ((BigInteger[]) polynomialDivisionOverFiniteFieldResult.get(3))[index];
				}

				/* set 'tempPolynomial := xPolynomial' */
				tempPolynomialDegree = xPolynomialDegree;
				tempPolynomial = new BigInteger[tempPolynomialDegree + 1];
				for (index = 0; index <= tempPolynomialDegree; index++) {
					tempPolynomial[index] = xPolynomial[index];
				}

				/* form 'product = quotient * xPolynomial' */
				polynomialArithmeticModularResult = polynomialArithmeticModular(quotient, xPolynomial, quotientDegree, xPolynomialDegree,
						qValue, '*');
				productDegree = (Integer) polynomialArithmeticModularResult.get(0);
				product = new BigInteger[productDegree + 1];
				for (index = 0; index <= productDegree; index++) {
					product[index] = ((BigInteger[]) polynomialArithmeticModularResult.get(1))[index];
				}

				/* find 'xPolynomial := bezoutX - product' */
				polynomialArithmeticModularResult = polynomialArithmeticModular(bezoutX, product, bezoutXdegree, productDegree, qValue, '-');
				xPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
				xPolynomial = new BigInteger[xPolynomialDegree + 1];
				for (index = 0; index <= xPolynomialDegree; index++) {
					xPolynomial[index] = ((BigInteger[]) polynomialArithmeticModularResult.get(1))[index];
				}

				/* set 'bezoutX := tempPolynomial' */
				bezoutXdegree = tempPolynomialDegree;
				bezoutX = new BigInteger[bezoutXdegree + 1];
				for (index = 0; index <= bezoutXdegree; index++) {
					bezoutX[index] = tempPolynomial[index];
				}

				/* set 'tempPolynomial := yPolynomial' */
				tempPolynomialDegree = yPolynomialDegree;
				tempPolynomial = new BigInteger[tempPolynomialDegree + 1];
				for (index = 0; index <= tempPolynomialDegree; index++) {
					tempPolynomial[index] = yPolynomial[index];
				}

				/* form 'product := quotient * yPolynomial' */
				polynomialArithmeticModularResult = polynomialArithmeticModular(quotient, yPolynomial, quotientDegree, yPolynomialDegree,
						qValue, '*');
				productDegree = (Integer) polynomialArithmeticModularResult.get(0);
				product = new BigInteger[productDegree + 1];
				for (index = 0; index <= productDegree; index++) {
					product[index] = ((BigInteger[]) polynomialArithmeticModularResult.get(1))[index];
				}

				/* find 'yPolynomial := bezoutY - product' */
				polynomialArithmeticModularResult = polynomialArithmeticModular(bezoutY, product, bezoutYdegree, productDegree, qValue, '-');
				yPolynomialDegree = (Integer) polynomialArithmeticModularResult.get(0);
				yPolynomial = new BigInteger[yPolynomialDegree + 1];
				for (index = 0; index <= yPolynomialDegree; index++) {
					yPolynomial[index] = ((BigInteger[]) polynomialArithmeticModularResult.get(1))[index];
				}

				/* set 'bezoutY := tempPolynomial' */
				bezoutYdegree = tempPolynomialDegree;
				bezoutY = new BigInteger[bezoutYdegree + 1];
				for (index = 0; index <= bezoutYdegree; index++) {
					bezoutY[index] = tempPolynomial[index];
				}

				if (((Integer) polynomialDivisionOverFiniteFieldResult.get(2) == 0)
						&& (((BigInteger[]) polynomialDivisionOverFiniteFieldResult.get(4))[0].compareTo(BigInteger.ZERO) == 0)) {
					loopHalt = true;
					gcdDegree = degreeTwo;
					gcd = new BigInteger[gcdDegree + 1];
					for (index = 0; index <= gcdDegree; index++) {
						gcd[index] = polynomialTwo[index];
					}
				} else {
					degreeOne = degreeTwo;
					polynomialOne = new BigInteger[degreeOne + 1];
					for (index = 0; index <= degreeTwo; index++) {
						polynomialOne[index] = polynomialTwo[index];
					}

					degreeTwo = (Integer) polynomialDivisionOverFiniteFieldResult.get(2);
					polynomialTwo = new BigInteger[degreeTwo + 1];
					for (index = 0; index <= degreeTwo; index++) {
						polynomialTwo[index] = ((BigInteger[]) polynomialDivisionOverFiniteFieldResult.get(4))[index];
					}
				}
			}

			if (interchange == true) {
				/* interchange 'bezout x' and 'bezout y' */
				dummy = new BigInteger[bezoutXdegree + 1];
				for (index = 0; index <= bezoutXdegree; index++) {
					dummy[index] = bezoutX[index];
				}

				bezoutX = new BigInteger[bezoutYdegree + 1];
				for (index = 0; index <= bezoutYdegree; index++) {
					bezoutX[index] = bezoutY[index];
				}

				bezoutY = new BigInteger[bezoutXdegree + 1];
				for (index = 0; index <= bezoutXdegree; index++) {
					bezoutY[index] = dummy[index];
				}

				dummyDegree = bezoutXdegree;
				bezoutXdegree = bezoutYdegree;
				bezoutYdegree = dummyDegree;
			}

			/* normalize gcd */
			inverseElementResult = multiplicativeInverse(gcd[gcdDegree], qValue);
			inverseElement = (BigInteger) inverseElementResult.get(1);

			for (index = 0; index <= gcdDegree; index++) {
				gcd[index] = inverseElement.multiply(gcd[index]);

				if ((gcd[index].abs()).compareTo(qValue) != -1) {
					gcd[index] = (gcd[index].divideAndRemainder(qValue))[1];
				}

				if (gcd[index].compareTo(BigInteger.ZERO) == -1) {
					gcd[index] = gcd[index].add(qValue);
				}
			}

			for (index = 0; index <= bezoutXdegree; index++) {
				bezoutX[index] = inverseElement.multiply(bezoutX[index]);

				if ((bezoutX[index].abs()).compareTo(qValue) != -1) {
					bezoutX[index] = (bezoutX[index].divideAndRemainder(qValue))[1];
				}

				if (bezoutX[index].compareTo(BigInteger.ZERO) == -1) {
					bezoutX[index] = bezoutX[index].add(qValue);
				}
			}

			for (index = 0; index <= bezoutYdegree; index++) {
				bezoutY[index] = inverseElement.multiply(bezoutY[index]);

				if ((bezoutY[index].abs()).compareTo(qValue) != -1) {
					bezoutY[index] = (bezoutY[index].divideAndRemainder(qValue))[1];
				}

				if (bezoutY[index].compareTo(BigInteger.ZERO) == -1) {
					bezoutY[index] = bezoutY[index].add(qValue);
				}
			}
		} else {
			error = true;
		}

		result.add(error);
		result.add(gcdDegree);
		result.add(gcd);
		result.add(bezoutXdegree);
		result.add(bezoutX);
		result.add(bezoutYdegree);
		result.add(bezoutY);

		return (result);
	}

	public static List univariatePolynomialGCD(BigInteger[][][] polynomialOne, BigInteger[][][] polynomialTwo, int degreeOne,
			int degreeTwo, int workingDecimalPlaceNumber) {
		/*
		 * finds the polynomial GCD of two univariate polynomials. neither
		 * polynomial may be the zero polynomial
		 */

		BigInteger[][][] polynomialGCD = new BigInteger[0][][];
		BigInteger[][][] dummy;
		BigInteger[][] dummyValue = new BigInteger[3][3];
		BigInteger[] complexContent = new BigInteger[3];
		BigInteger[] complexMultiplier = new BigInteger[3];
		BigInteger[] complexContentPolynomialOne = new BigInteger[3];
		BigInteger[] complexContentPolynomialTwo = new BigInteger[3];
		BigInteger[] denominatorArray;
		BigInteger[] numberOne = new BigInteger[3];
		BigInteger[] numberTwo = new BigInteger[3];
		BigDecimal[] divisionResult = new BigDecimal[3];

		BigInteger realContent = BigInteger.ONE;
		BigInteger realContentPolynomialOne = BigInteger.ONE;
		BigInteger realContentPolynomialTwo = BigInteger.ONE;
		BigInteger realLCM;
		BigInteger polynomialOneConstant;
		BigInteger polynomialTwoConstant;
		BigInteger divisorConstant;
		BigInteger realMultiplier = BigInteger.ONE;

		int index;
		int runIndex;
		int fieldIndex;
		int GCDdegree = 0;
		int dummyDegree;

		boolean isOverZi = false;
		boolean halt;
		boolean polynomialOneComplex;
		boolean polynomialTwoComplex;

		List polynomialDivisionResult;
		List complexGCDResult;
		List lcmResult;

		List<Object> result = new ArrayList<Object>(2);

		/* check degrees of entered polynomials, ie. ensure deg(P1) > deg(P2) */
		if (degreeOne < degreeTwo) {
			/* interchange P1 and P2 */
			dummy = new BigInteger[degreeOne + 1][3][3];
			for (index = 0; index <= degreeOne; index++) {
				dummy[index][1][1] = polynomialOne[index][1][1];
				dummy[index][1][2] = polynomialOne[index][1][2];

				dummy[index][2][1] = polynomialOne[index][2][1];
				dummy[index][2][2] = polynomialOne[index][2][2];
			}

			polynomialOne = new BigInteger[degreeTwo + 1][3][3];
			for (index = 0; index <= degreeTwo; index++) {
				polynomialOne[index][1][1] = polynomialTwo[index][1][1];
				polynomialOne[index][1][2] = polynomialTwo[index][1][2];

				polynomialOne[index][2][1] = polynomialTwo[index][2][1];
				polynomialOne[index][2][2] = polynomialTwo[index][2][2];
			}

			polynomialTwo = new BigInteger[degreeOne + 1][3][3];
			for (index = 0; index <= degreeOne; index++) {
				polynomialTwo[index][1][1] = dummy[index][1][1];
				polynomialTwo[index][1][2] = dummy[index][1][2];

				polynomialTwo[index][2][1] = dummy[index][2][1];
				polynomialTwo[index][2][2] = dummy[index][2][2];
			}

			dummyDegree = degreeOne;
			degreeOne = degreeTwo;
			degreeTwo = dummyDegree;
		}

		/* are polynomials over Q or Q[i]? if so then convert to Z or Z[i] */
		/* load all denominators */
		denominatorArray = new BigInteger[2 * (degreeOne + 1)];
		runIndex = 0;
		for (index = 0; index <= degreeOne; index++) {
			denominatorArray[runIndex] = polynomialOne[index][1][2];
			runIndex++;
			denominatorArray[runIndex] = polynomialOne[index][2][2];
			runIndex++;
		}

		lcmResult = lcm(denominatorArray[0], denominatorArray[1]);
		realLCM = (BigInteger) lcmResult.get(1);
		for (index = 2; index <= (runIndex - 1); index++) {
			lcmResult = lcm(realLCM, denominatorArray[index]);
			realLCM = (BigInteger) lcmResult.get(1);
		}
		polynomialOneConstant = realLCM;

		for (index = 0; index <= degreeOne; index++) {
			polynomialOne[index][1][1] = polynomialOne[index][1][1].multiply(realLCM);
			realContent = polynomialOne[index][1][1].gcd(polynomialOne[index][1][2]);
			polynomialOne[index][1][1] = polynomialOne[index][1][1].divide(realContent);
			polynomialOne[index][1][2] = polynomialOne[index][1][2].divide(realContent);

			polynomialOne[index][2][1] = polynomialOne[index][2][1].multiply(realLCM);
			realContent = polynomialOne[index][2][1].gcd(polynomialOne[index][2][2]);
			polynomialOne[index][2][1] = polynomialOne[index][2][1].divide(realContent);
			polynomialOne[index][2][2] = polynomialOne[index][2][2].divide(realContent);
		}

		denominatorArray = new BigInteger[2 * (degreeTwo + 1)];
		runIndex = 0;
		for (index = 0; index <= degreeTwo; index++) {
			denominatorArray[runIndex] = polynomialTwo[index][1][2];
			runIndex++;
			denominatorArray[runIndex] = polynomialTwo[index][2][2];
			runIndex++;
		}

		lcmResult = lcm(denominatorArray[0], denominatorArray[1]);
		realLCM = (BigInteger) lcmResult.get(1);
		for (index = 2; index <= (runIndex - 1); index++) {
			lcmResult = lcm(realLCM, denominatorArray[index]);
			realLCM = (BigInteger) lcmResult.get(1);
		}
		polynomialTwoConstant = realLCM;

		divisorConstant = polynomialOneConstant.gcd(polynomialTwoConstant);

		for (index = 0; index <= degreeTwo; index++) {
			polynomialTwo[index][1][1] = polynomialTwo[index][1][1].multiply(realLCM);
			realContent = polynomialTwo[index][1][1].gcd(polynomialTwo[index][1][2]);
			polynomialTwo[index][1][1] = polynomialTwo[index][1][1].divide(realContent);
			polynomialTwo[index][1][2] = polynomialTwo[index][1][2].divide(realContent);

			polynomialTwo[index][2][1] = polynomialTwo[index][2][1].multiply(realLCM);
			realContent = polynomialTwo[index][2][1].gcd(polynomialTwo[index][2][2]);
			polynomialTwo[index][2][1] = polynomialTwo[index][2][1].divide(realContent);
			polynomialTwo[index][2][2] = polynomialTwo[index][2][2].divide(realContent);
		}

		/* are polynomials over Z or Z[i]? */
		index = 0;
		polynomialOneComplex = false;
		while ((polynomialOneComplex == false) & (index <= degreeOne)) {
			if (polynomialOne[index][2][1].compareTo(BigInteger.ZERO) == 0) {
				index++;
			} else {
				polynomialOneComplex = true;
			}
		}

		index = 0;
		polynomialTwoComplex = false;
		while ((polynomialTwoComplex == false) & (index <= degreeTwo)) {
			if (polynomialTwo[index][2][1].compareTo(BigInteger.ZERO) == 0) {
				index++;
			} else {
				polynomialTwoComplex = true;
			}
		}

		if ((polynomialOneComplex == true) | (polynomialTwoComplex == true)) {
			isOverZi = true;
		} else {
			isOverZi = false;
		}

		/* find 'contents' */
		if (polynomialOneComplex == true) {
			/* find complex content of 'P1' */
			if (degreeOne > 0) {
				numberOne[1] = polynomialOne[degreeOne][1][1];
				numberOne[2] = polynomialOne[degreeOne][2][1];
				numberTwo[1] = polynomialOne[degreeOne - 1][1][1];
				numberTwo[2] = polynomialOne[degreeOne - 1][2][1];

				complexGCDResult = complexExtendedEuclideanAlgorithm(numberOne, numberTwo, workingDecimalPlaceNumber);
				complexContentPolynomialOne[1] = ((BigInteger[]) complexGCDResult.get(0))[1];
				complexContentPolynomialOne[2] = ((BigInteger[]) complexGCDResult.get(0))[2];

				for (index = (degreeOne - 2); index >= 0; index--) {
					numberOne[1] = complexContentPolynomialOne[1];
					numberOne[2] = complexContentPolynomialOne[2];
					numberTwo[1] = polynomialOne[index][1][1];
					numberTwo[2] = polynomialOne[index][2][1];
					complexGCDResult = complexExtendedEuclideanAlgorithm(numberOne, numberTwo, workingDecimalPlaceNumber);
					complexContentPolynomialOne[1] = ((BigInteger[]) complexGCDResult.get(0))[1];
					complexContentPolynomialOne[2] = ((BigInteger[]) complexGCDResult.get(0))[2];
				}
			} else {
				complexContentPolynomialOne[1] = polynomialTwo[degreeOne][1][1];
				complexContentPolynomialOne[2] = polynomialTwo[degreeOne][2][1];
			}

			for (index = degreeOne; index >= 0; index--) {
				divisionResult = complexArithmetic(new BigDecimal(polynomialOne[index][1][1]), new BigDecimal(polynomialOne[index][2][1]),
						new BigDecimal(complexContentPolynomialOne[1]), new BigDecimal(complexContentPolynomialOne[2]), '/',
						workingDecimalPlaceNumber);
				polynomialOne[index][1][1] = (divisionResult[1]).toBigInteger();
				polynomialOne[index][2][1] = (divisionResult[2]).toBigInteger();
			}
		} else {
			if (degreeOne > 0) {
				realContentPolynomialOne = polynomialOne[degreeOne][1][1].gcd(polynomialOne[degreeOne - 1][1][1]);
				for (index = (degreeOne - 2); index >= 0; index--) {
					realContentPolynomialOne = realContentPolynomialOne.gcd(polynomialOne[index][1][1]);
				}

				if (polynomialOne[degreeOne][1][1].compareTo(BigInteger.ZERO) == -1) {
					realContentPolynomialOne = realContentPolynomialOne.negate();
				}
			} else {
				realContentPolynomialOne = polynomialOne[degreeOne][1][1];
			}

			for (index = degreeOne; index >= 0; index--) {
				polynomialOne[index][1][1] = polynomialOne[index][1][1].divide(realContentPolynomialOne);
			}
		}

		if (polynomialTwoComplex == true) {
			/* find complex content of 'P2' */
			if (degreeTwo > 0) {
				numberOne[1] = polynomialTwo[degreeTwo][1][1];
				numberOne[2] = polynomialTwo[degreeTwo][2][1];
				numberTwo[1] = polynomialTwo[degreeTwo - 1][1][1];
				numberTwo[2] = polynomialTwo[degreeTwo - 1][2][1];
				complexGCDResult = complexExtendedEuclideanAlgorithm(numberOne, numberTwo, workingDecimalPlaceNumber);
				complexContentPolynomialTwo[1] = ((BigInteger[]) complexGCDResult.get(0))[1];
				complexContentPolynomialTwo[2] = ((BigInteger[]) complexGCDResult.get(0))[2];

				for (index = (degreeTwo - 2); index >= 0; index--) {
					numberOne[1] = complexContentPolynomialTwo[1];
					numberOne[2] = complexContentPolynomialTwo[2];
					numberTwo[1] = polynomialTwo[index][1][1];
					numberTwo[2] = polynomialTwo[index][2][1];
					complexGCDResult = complexExtendedEuclideanAlgorithm(numberOne, numberTwo, workingDecimalPlaceNumber);
					complexContentPolynomialTwo[1] = ((BigInteger[]) complexGCDResult.get(0))[1];
					complexContentPolynomialTwo[2] = ((BigInteger[]) complexGCDResult.get(0))[2];
				}
			} else {
				complexContentPolynomialTwo[1] = polynomialTwo[degreeTwo][1][1];
				complexContentPolynomialTwo[2] = polynomialTwo[degreeTwo][2][1];
			}

			for (index = degreeTwo; index >= 0; index--) {
				divisionResult = complexArithmetic(new BigDecimal(polynomialTwo[index][1][1]), new BigDecimal(polynomialTwo[index][2][1]),
						new BigDecimal(complexContentPolynomialTwo[1]), new BigDecimal(complexContentPolynomialTwo[2]), '/',
						workingDecimalPlaceNumber);
				polynomialTwo[index][1][1] = (divisionResult[1]).toBigInteger();
				polynomialTwo[index][2][1] = (divisionResult[2]).toBigInteger();
			}
		} else {
			if (degreeTwo > 0) {
				realContentPolynomialTwo = polynomialTwo[degreeTwo][1][1].gcd(polynomialTwo[degreeTwo - 1][1][1]);
				for (index = (degreeTwo - 2); index >= 0; index--) {
					realContentPolynomialTwo = realContentPolynomialTwo.gcd(polynomialTwo[index][1][1]);
				}

				if (polynomialTwo[degreeTwo][1][1].compareTo(BigInteger.ZERO) == -1) {
					realContentPolynomialTwo = realContentPolynomialTwo.negate();
				}
			} else {
				realContentPolynomialTwo = polynomialTwo[degreeTwo][1][1];
			}

			for (index = degreeTwo; index >= 0; index--) {
				polynomialTwo[index][1][1] = polynomialTwo[index][1][1].divide(realContentPolynomialTwo);
			}
		}

		if ((polynomialTwoComplex == false) & (polynomialTwoComplex == false)) {
			realMultiplier = realContentPolynomialOne.gcd(realContentPolynomialTwo);
		} else {
			numberOne[1] = complexContentPolynomialOne[1];
			numberOne[2] = complexContentPolynomialOne[2];
			numberTwo[1] = complexContentPolynomialTwo[1];
			numberTwo[2] = complexContentPolynomialTwo[2];
			complexGCDResult = complexExtendedEuclideanAlgorithm(numberOne, numberTwo, workingDecimalPlaceNumber);
			complexMultiplier[1] = ((BigInteger[]) complexGCDResult.get(0))[1];
			complexMultiplier[2] = ((BigInteger[]) complexGCDResult.get(0))[2];
		}

		halt = false;
		while (halt == false) {

			polynomialDivisionResult = polynomialDivision(polynomialOne, polynomialTwo, degreeOne, degreeTwo);

			if (((Integer) polynomialDivisionResult.get(1) == 0)
					&& ((((BigInteger[][][]) polynomialDivisionResult.get(3))[0][1][1].compareTo(BigInteger.ZERO) == 0) && (((BigInteger[][][]) polynomialDivisionResult
							.get(3))[0][2][1].compareTo(BigInteger.ZERO) == 0))) {
				halt = true;
				GCDdegree = degreeTwo;
				polynomialGCD = new BigInteger[GCDdegree + 1][3][3];
				for (index = 0; index <= GCDdegree; index++) {
					polynomialGCD[index][1][1] = polynomialTwo[index][1][1];
					polynomialGCD[index][1][2] = polynomialTwo[index][1][2];

					polynomialGCD[index][2][1] = polynomialTwo[index][2][1];
					polynomialGCD[index][2][2] = polynomialTwo[index][2][2];
				}
			} else {
				degreeOne = degreeTwo;
				polynomialOne = new BigInteger[degreeOne + 1][3][3];
				for (index = 0; index <= degreeTwo; index++) {
					polynomialOne[index][1][1] = polynomialTwo[index][1][1];
					polynomialOne[index][1][2] = polynomialTwo[index][1][2];

					polynomialOne[index][2][1] = polynomialTwo[index][2][1];
					polynomialOne[index][2][2] = polynomialTwo[index][2][2];
				}

				degreeTwo = (Integer) polynomialDivisionResult.get(1);
				polynomialTwo = new BigInteger[degreeTwo + 1][3][3];
				for (index = 0; index <= degreeTwo; index++) {
					polynomialTwo[index][1][1] = ((BigInteger[][][]) polynomialDivisionResult.get(6))[index][1][1];
					polynomialTwo[index][1][2] = ((BigInteger[][][]) polynomialDivisionResult.get(6))[index][1][2];

					polynomialTwo[index][2][1] = ((BigInteger[][][]) polynomialDivisionResult.get(6))[index][2][1];
					polynomialTwo[index][2][2] = ((BigInteger[][][]) polynomialDivisionResult.get(6))[index][2][2];
				}

				if (isOverZi == true) {
					/* find complex content of 'polynomialTwo' */
					if (degreeTwo > 0) {
						numberOne[1] = polynomialTwo[degreeTwo][1][1];
						numberOne[2] = polynomialTwo[degreeTwo][2][1];
						numberTwo[1] = polynomialTwo[degreeTwo - 1][1][1];
						numberTwo[2] = polynomialTwo[degreeTwo - 1][2][1];
						complexGCDResult = complexExtendedEuclideanAlgorithm(numberOne, numberTwo, workingDecimalPlaceNumber);
						complexContent[1] = ((BigInteger[]) complexGCDResult.get(0))[1];
						complexContent[2] = ((BigInteger[]) complexGCDResult.get(0))[2];

						for (index = (degreeTwo - 2); index >= 0; index--) {
							numberOne[1] = complexContent[1];
							numberOne[2] = complexContent[2];
							numberTwo[1] = polynomialTwo[index][1][1];
							numberTwo[2] = polynomialTwo[index][2][1];
							complexGCDResult = complexExtendedEuclideanAlgorithm(numberOne, numberTwo, workingDecimalPlaceNumber);
							complexContent[1] = ((BigInteger[]) complexGCDResult.get(0))[1];
							complexContent[2] = ((BigInteger[]) complexGCDResult.get(0))[2];
						}
					} else {
						complexContent[1] = polynomialTwo[degreeTwo][1][1];
						complexContent[2] = polynomialTwo[degreeTwo][2][1];
					}

					for (index = degreeTwo; index >= 0; index--) {
						divisionResult = complexArithmetic(new BigDecimal(polynomialTwo[index][1][1]), new BigDecimal(
								polynomialTwo[index][2][1]), new BigDecimal(complexContent[1]), new BigDecimal(complexContent[2]), '/',
								workingDecimalPlaceNumber);
						polynomialTwo[index][1][1] = (divisionResult[1]).toBigInteger();
						polynomialTwo[index][2][1] = (divisionResult[2]).toBigInteger();
					}
				} else {
					/* find content of 'polynomialTwo' */
					if (degreeTwo > 0) {
						realContent = polynomialTwo[degreeTwo][1][1].gcd(polynomialTwo[degreeTwo - 1][1][1]);
						for (index = (degreeTwo - 2); index >= 0; index--) {
							realContent = realContent.gcd(polynomialTwo[index][1][1]);
						}

						if (polynomialTwo[degreeTwo][1][1].compareTo(BigInteger.ZERO) == -1) {
							realContent = realContent.negate();
						}
					} else {
						realContent = polynomialTwo[degreeTwo][1][1];
					}

					for (index = degreeTwo; index >= 0; index--) {
						polynomialTwo[index][1][1] = polynomialTwo[index][1][1].divide(realContent);
					}
				}
			}
		}

		/* replace 'content' */
		if ((polynomialTwoComplex == false) & (polynomialTwoComplex == false)) {
			for (index = GCDdegree; index >= 0; index--) {
				polynomialGCD[index][1][1] = polynomialGCD[index][1][1].multiply(realMultiplier);
			}
		} else {
			dummyValue[1][1] = complexMultiplier[1];
			dummyValue[1][2] = BigInteger.ONE;

			dummyValue[2][1] = complexMultiplier[2];
			dummyValue[2][2] = BigInteger.ONE;

			for (index = GCDdegree; index >= 0; index--) {
				polynomialGCD[index] = fractionalComplexArithmetic(dummyValue, polynomialGCD[index], '*');
			}
		}

		/* replace 'denominators' & simplify */
		for (index = 0; index <= GCDdegree; index++) {
			for (fieldIndex = 1; fieldIndex <= 2; fieldIndex++) {
				realContent = polynomialGCD[index][fieldIndex][1].gcd(divisorConstant);
				polynomialGCD[index][fieldIndex][1] = polynomialGCD[index][fieldIndex][1].divide(realContent);
				polynomialGCD[index][fieldIndex][2] = divisorConstant.divide(realContent);
			}
		}

		result.add(GCDdegree);
		result.add(polynomialGCD);

		return (result);

	}

	public static List complexExtendedEuclideanAlgorithm(BigInteger[] numberOne, BigInteger[] numberTwo, int workingDecimalPlaceNumber) {
		/*
		 * returns the gcd of two gaussian integers, and also the complex Bezout
		 * relation mn2 + nn1 = gcd(n1,n2)
		 */

		BigDecimal[] gcd = new BigDecimal[3];
		BigDecimal[] mValue = new BigDecimal[3];
		BigDecimal[] nValue = new BigDecimal[3];
		BigDecimal[] product; // new BigDecimal[3];
		BigDecimal[] dividend = new BigDecimal[3];
		BigDecimal[] divisor = new BigDecimal[3];
		BigDecimal[] quotient; // = new BigDecimal[3];
		BigDecimal[] remainder; // = new BigDecimal[3];
		BigDecimal[] tempValue = new BigDecimal[3];
		BigDecimal[] xValue = new BigDecimal[3];
		BigDecimal[] yValue = new BigDecimal[3];
		BigDecimal[] lastxValue = new BigDecimal[3];
		BigDecimal[] lastyValue = new BigDecimal[3];

		BigInteger[] gcdResult = new BigInteger[3];
		BigInteger[] mValueResult = new BigInteger[3];
		BigInteger[] nValueResult = new BigInteger[3];

		BigDecimal magnitude;
		BigDecimal magnitudeOne;
		BigDecimal magnitudeTwo;

		List<Object> result = new ArrayList<Object>(3);

		magnitudeOne = complexMagnitude(new BigDecimal(numberOne[1]), new BigDecimal(numberOne[2]), workingDecimalPlaceNumber);
		magnitudeTwo = complexMagnitude(new BigDecimal(numberTwo[1]), new BigDecimal(numberTwo[2]), workingDecimalPlaceNumber);

		magnitude = BigDecimal.ONE;
		if ((!((magnitudeOne.compareTo(BigDecimal.ZERO) == 0) || (magnitudeTwo.compareTo(BigDecimal.ZERO) == 0)))) {
			if (magnitudeOne.compareTo(magnitudeTwo) == 1) {
				dividend[1] = new BigDecimal(numberOne[1]);
				dividend[2] = new BigDecimal(numberOne[2]);
				divisor[1] = new BigDecimal(numberTwo[1]);
				divisor[2] = new BigDecimal(numberTwo[2]);
			} else {
				dividend[1] = new BigDecimal(numberTwo[1]);
				dividend[2] = new BigDecimal(numberTwo[2]);
				divisor[1] = new BigDecimal(numberOne[1]);
				divisor[2] = new BigDecimal(numberOne[2]);
			}

			xValue[1] = BigDecimal.ZERO;
			xValue[2] = BigDecimal.ZERO;
			lastxValue[1] = BigDecimal.ONE;
			lastxValue[2] = BigDecimal.ZERO;
			yValue[1] = BigDecimal.ONE;
			yValue[2] = BigDecimal.ZERO;
			lastyValue[1] = BigDecimal.ZERO;
			lastyValue[2] = BigDecimal.ZERO;
			while (magnitude.compareTo(BigDecimal.ZERO) != 0) {
				quotient = complexArithmetic(dividend[1], dividend[2], divisor[1], divisor[2], '/', workingDecimalPlaceNumber);

				/* round quotient to nearest whole integers */
				quotient[1] = quotient[1].setScale(0, BigDecimal.ROUND_HALF_UP);
				quotient[2] = quotient[2].setScale(0, BigDecimal.ROUND_HALF_UP);

				/* find remainder */
				product = complexArithmetic(quotient[1], quotient[2], divisor[1], divisor[2], '*', workingDecimalPlaceNumber);
				remainder = complexArithmetic(dividend[1], dividend[2], product[1], product[2], '-', workingDecimalPlaceNumber);

				dividend[1] = divisor[1];
				dividend[2] = divisor[2];
				divisor[1] = remainder[1];
				divisor[2] = remainder[2];

				tempValue[1] = xValue[1];
				tempValue[2] = xValue[2];
				xValue = complexArithmetic(quotient[1], quotient[2], xValue[1], xValue[2], '*', workingDecimalPlaceNumber);
				xValue = complexArithmetic(lastxValue[1], lastxValue[2], xValue[1], xValue[2], '-', workingDecimalPlaceNumber);
				lastxValue[1] = tempValue[1];
				lastxValue[2] = tempValue[2];

				tempValue[1] = yValue[1];
				tempValue[2] = yValue[2];
				yValue = complexArithmetic(quotient[1], quotient[2], yValue[1], yValue[2], '*', workingDecimalPlaceNumber);
				yValue = complexArithmetic(lastyValue[1], lastyValue[2], yValue[1], yValue[2], '-', workingDecimalPlaceNumber);
				lastyValue[1] = tempValue[1];
				lastyValue[2] = tempValue[2];

				magnitude = complexMagnitude(remainder[1], remainder[2], workingDecimalPlaceNumber);
			}

			gcd[1] = dividend[1];
			gcd[2] = dividend[2];

			mValue[1] = lastxValue[1];
			mValue[2] = lastxValue[2];

			nValue[1] = lastyValue[1];
			nValue[2] = lastyValue[2];
		} else {
			if ((new BigDecimal(numberOne[1]).compareTo(BigDecimal.ZERO) == 0)
					&& (new BigDecimal(numberOne[2]).compareTo(BigDecimal.ZERO) == 0)) {
				gcd[1] = new BigDecimal(numberTwo[1]);
				gcd[2] = new BigDecimal(numberTwo[2]);

				mValue[1] = BigDecimal.ONE;
				mValue[2] = BigDecimal.ZERO;

				nValue[1] = BigDecimal.ZERO;
				nValue[2] = BigDecimal.ZERO;
			} else {
				gcd[1] = new BigDecimal(numberOne[1]);
				gcd[2] = new BigDecimal(numberOne[2]);

				mValue[1] = BigDecimal.ZERO;
				mValue[2] = BigDecimal.ZERO;

				nValue[1] = BigDecimal.ONE;
				nValue[2] = BigDecimal.ZERO;
			}
		}

		/* convert results to 'BigInteger' format */
		gcdResult[1] = gcd[1].toBigInteger();
		gcdResult[2] = gcd[2].toBigInteger();

		mValueResult[1] = mValue[1].toBigInteger();
		mValueResult[2] = mValue[2].toBigInteger();

		nValueResult[1] = nValue[1].toBigInteger();
		nValueResult[2] = nValue[2].toBigInteger();

		result.add(gcdResult);
		result.add(mValueResult);
		result.add(nValueResult);

		return (result);
	}

	public static BigDecimal complexMagnitude(BigDecimal realPart, BigDecimal imaginaryPart, int workingDecimalPlaceNumber) {

		/*
		 * returns magnitude of a complex number
		 */

		BigDecimal realPartSquared;
		BigDecimal imaginaryPartSquared;
		BigDecimal magnitudeSquared;
		BigDecimal result;

		realPartSquared = realPart.multiply(realPart);
		realPartSquared = realPartSquared.setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);

		imaginaryPartSquared = imaginaryPart.multiply(imaginaryPart);
		imaginaryPartSquared = imaginaryPartSquared.setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);

		magnitudeSquared = realPartSquared.add(imaginaryPartSquared);
		magnitudeSquared = magnitudeSquared.setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);

		result = bigRoot(magnitudeSquared, 2, workingDecimalPlaceNumber);

		return (result);
	}

	public static BigDecimal[] complexArithmetic(BigDecimal realPartNumberOne, BigDecimal imaginaryPartNumberOne,
			BigDecimal realPartNumberTwo, BigDecimal imaginaryPartNumberTwo, char sign, int workingDecimalPlaceNumber) {
		/* Returns uncorrected +,-,*-/ of two complex numbers */
		BigDecimal[] result = new BigDecimal[3];

		switch (sign) {
		case '+':
			result[1] = realPartNumberOne.add(realPartNumberTwo);
			result[1] = result[1].setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);

			result[2] = imaginaryPartNumberOne.add(imaginaryPartNumberTwo);
			result[2] = result[2].setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);
			break;

		case '-':
			result[1] = realPartNumberOne.subtract(realPartNumberTwo);
			result[1] = result[1].setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);

			result[2] = imaginaryPartNumberOne.subtract(imaginaryPartNumberTwo);
			result[2] = result[2].setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);
			break;

		case '*':
			result[1] = (realPartNumberOne.multiply(realPartNumberTwo))
					.subtract((imaginaryPartNumberOne.multiply(imaginaryPartNumberTwo)));
			result[1] = result[1].setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);

			result[2] = (realPartNumberOne.multiply(imaginaryPartNumberTwo)).add((imaginaryPartNumberOne.multiply(realPartNumberTwo)));
			result[2] = result[2].setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);
			break;

		case '/':
			result[1] = ((realPartNumberOne.multiply(realPartNumberTwo)).add((imaginaryPartNumberOne.multiply(imaginaryPartNumberTwo))))
					.divide(((realPartNumberTwo.multiply(realPartNumberTwo)).add((imaginaryPartNumberTwo.multiply(imaginaryPartNumberTwo)))),
							workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);
			result[2] = ((imaginaryPartNumberOne.multiply(realPartNumberTwo)).subtract((realPartNumberOne
					.multiply(imaginaryPartNumberTwo)))).divide(((realPartNumberTwo.multiply(realPartNumberTwo)).add((imaginaryPartNumberTwo
					.multiply(imaginaryPartNumberTwo)))), workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);
			break;
		}

		return (result);
	}

	public static BigDecimal bigRoot(BigDecimal argument, int root, int workingDecimalPlaceNumber) {

		/*
		 * returns uncorrected root of a BigDecimal - uses the Newton Raphson
		 * method. argument must be positive
		 */

		BigDecimal result;
		BigDecimal xn;
		BigDecimal oldxn;
		BigDecimal xnPlusOne;
		BigDecimal numerator;
		BigDecimal denominator;
		BigDecimal quotient;
		BigDecimal constant;

		int index;
		int runIndex;
		int iterationNumber = 200;
		constant = new BigDecimal(root);

		boolean halt;

		if (argument.compareTo(BigDecimal.ZERO) != 0) {
			xn = argument;
			oldxn = xn;
			halt = false;
			runIndex = 1;
			while ((halt == false) & (runIndex <= iterationNumber)) {
				oldxn = xn;
				numerator = xn;
				denominator = numerator;

				numerator = numerator.pow(root);
				denominator = denominator.pow(root - 1);

				denominator = (constant.multiply(denominator));
				numerator = (numerator.subtract(argument));

				if (denominator.compareTo(BigDecimal.ZERO) == 0) {
					halt = true;
				} else {
					quotient = (numerator.divide(denominator, workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP));

					xnPlusOne = (xn.subtract(quotient));
					xnPlusOne = xnPlusOne.setScale(workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP);

					xn = xnPlusOne;

					if (xnPlusOne.compareTo(oldxn) == 0) {
						halt = true;
					}
				}

				runIndex++;
			}
			result = xn;
		} else {
			result = BigDecimal.ZERO;
		}

		return (result);
	}

	public static List lcm(BigInteger numberOne, BigInteger numberTwo) {
		/*
		 * returns the 'lowest common multiple' of two numbers
		 */
		BigInteger lcm = BigInteger.ONE;
		BigInteger gcd;

		boolean error = false;

		List<Object> result = new ArrayList<Object>(2);

		if (!((numberOne.compareTo(BigInteger.ZERO) == 0) || (numberTwo.compareTo(BigInteger.ZERO) == 0))) {
			gcd = numberOne.gcd(numberTwo);
			lcm = (numberOne.multiply(numberTwo)).divide(gcd);
		} else {
			error = true;
		}

		result.add(error);
		result.add(lcm);

		return (result);
	}

	public static List polynomialDivision(BigInteger[][][] polynomialCoefficientsOne, BigInteger[][][] polynomialCoefficientsTwo,
			int degreeOne, int degreeTwo) {
		/*
		 * divides two polynomials P1, P2 over the integers, rationals and Z[i],
		 * Q[i] fields. Note P2 should not be zero
		 */

		BigInteger[][][] Rcoefficients;
		BigInteger[][][] Qcoefficients;
		BigInteger[][][] RcoefficientsPseudo;
		BigInteger[][][] QcoefficientsPseudo;
		BigInteger[][][] newQcoefficients;
		BigInteger[][][] newRcoefficients;
		BigInteger[][][] Gcoefficients;
		BigInteger[][][] dummyCoefficients;
		BigInteger[][][] product;
		BigInteger[][][] polynomialOneAugmented;
		BigInteger[][][] polynomialTwoAugmented;
		BigInteger[][] dummyValue = new BigInteger[3][3];
		BigInteger[][] bValue = new BigInteger[3][3];

		int[] dummyExponents;

		int index;
		int runIndex;
		int indexOne;
		int indexTwo;
		int Rdegree;
		int Qdegree;
		int Gdegree;
		int newQdegree;
		int newRdegree = 0;
		int dummyLength;
		int exchangeNumber;
		int productDegree;
		int dummyValueInteger;
		int zeroDegreeCount = 0;
		int delta;

		boolean halt;
		boolean terminate;

		List<Object> result = new ArrayList<Object>(7);

		/* find delta */
		if ((degreeOne - degreeTwo + 1) > 0) {
			delta = degreeOne - degreeTwo + 1;
		} else {
			delta = 0;
		}

		/* find b^delta */
		bValue[1][1] = BigInteger.ONE;
		bValue[1][2] = BigInteger.ONE;
		bValue[2][1] = BigInteger.ZERO;
		bValue[2][2] = BigInteger.ONE;
		for (index = 1; index <= delta; index++) {
			bValue = fractionalComplexArithmetic(bValue, polynomialCoefficientsTwo[degreeTwo], '*');
		}

		if (degreeOne >= degreeTwo) {
			/* initalise R and Q */
			Rdegree = degreeOne;
			Rcoefficients = new BigInteger[Rdegree + 1][3][3];
			for (index = 0; index <= Rdegree; index++) {
				Rcoefficients[index][1][1] = polynomialCoefficientsOne[index][1][1];
				Rcoefficients[index][1][2] = polynomialCoefficientsOne[index][1][2];

				Rcoefficients[index][2][1] = polynomialCoefficientsOne[index][2][1];
				Rcoefficients[index][2][2] = polynomialCoefficientsOne[index][2][2];
			}

			Qdegree = 0;
			Qcoefficients = new BigInteger[Qdegree + 1][3][3];
			Qcoefficients[0][1][1] = BigInteger.ZERO;
			Qcoefficients[0][1][2] = BigInteger.ONE;
			Qcoefficients[0][2][1] = BigInteger.ZERO;
			Qcoefficients[0][2][2] = BigInteger.ONE;

			/* start algorithm */
			terminate = false;
			while ((Rdegree >= degreeTwo) & (terminate == false)) {
				/* find G */
				Gdegree = Rdegree - degreeTwo;
				Gcoefficients = new BigInteger[Gdegree + 1][3][3];
				for (index = 0; index <= Gdegree; index++) {
					if (index < Gdegree) {
						Gcoefficients[index][1][1] = BigInteger.ZERO;
						Gcoefficients[index][1][2] = BigInteger.ONE;

						Gcoefficients[index][2][1] = BigInteger.ZERO;
						Gcoefficients[index][2][2] = BigInteger.ONE;
					} else {
						Gcoefficients[index] = fractionalComplexArithmetic(Rcoefficients[Rdegree], polynomialCoefficientsTwo[degreeTwo], '/');
					}
				}

				/* form Q := Q + G */
				if (Qdegree <= Gdegree) {
					newQdegree = Gdegree;
				} else {
					newQdegree = Qdegree;
				}

				polynomialOneAugmented = new BigInteger[newQdegree + 1][3][3];
				for (index = 0; index <= newQdegree; index++) {
					if (index > Qdegree) {
						polynomialOneAugmented[index][1][1] = BigInteger.ZERO;
						polynomialOneAugmented[index][1][2] = BigInteger.ONE;
						polynomialOneAugmented[index][2][1] = BigInteger.ZERO;
						polynomialOneAugmented[index][2][2] = BigInteger.ONE;
					} else {
						polynomialOneAugmented[index][1][1] = Qcoefficients[index][1][1];
						polynomialOneAugmented[index][1][2] = Qcoefficients[index][1][2];
						polynomialOneAugmented[index][2][1] = Qcoefficients[index][2][1];
						polynomialOneAugmented[index][2][2] = Qcoefficients[index][2][2];
					}
				}

				polynomialTwoAugmented = new BigInteger[newQdegree + 1][3][3];
				for (index = 0; index <= newQdegree; index++) {
					if (index > Gdegree) {
						polynomialTwoAugmented[index][1][1] = BigInteger.ZERO;
						polynomialTwoAugmented[index][1][2] = BigInteger.ONE;
						polynomialTwoAugmented[index][2][1] = BigInteger.ZERO;
						polynomialTwoAugmented[index][2][2] = BigInteger.ONE;
					} else {
						polynomialTwoAugmented[index][1][1] = Gcoefficients[index][1][1];
						polynomialTwoAugmented[index][1][2] = Gcoefficients[index][1][2];
						polynomialTwoAugmented[index][2][1] = Gcoefficients[index][2][1];
						polynomialTwoAugmented[index][2][2] = Gcoefficients[index][2][2];
					}
				}

				newQcoefficients = new BigInteger[newQdegree + 1][3][3];
				for (index = 0; index <= newQdegree; index++) {
					newQcoefficients[index] = fractionalComplexArithmetic(polynomialOneAugmented[index], polynomialTwoAugmented[index], '+');
				}

				index = newQdegree;
				halt = false;
				while ((index >= 0) & (halt == false)) {
					if ((newQcoefficients[index][1][1].compareTo(BigInteger.ZERO) == 0)
							&& (newQcoefficients[index][2][1].compareTo(BigInteger.ZERO) == 0)) {
						index--;
					} else {
						halt = true;
					}
				}

				if (halt == true) {
					newQdegree = index;
				} else {
					newQdegree = 0;
				}

				Qdegree = newQdegree;
				Qcoefficients = new BigInteger[Qdegree + 1][3][3];
				for (index = 0; index <= Qdegree; index++) {
					Qcoefficients[index][1][1] = newQcoefficients[index][1][1];
					Qcoefficients[index][1][2] = newQcoefficients[index][1][2];

					Qcoefficients[index][2][1] = newQcoefficients[index][2][1];
					Qcoefficients[index][2][2] = newQcoefficients[index][2][2];
				}

				/* find GP2 */
				dummyCoefficients = new BigInteger[(Gdegree + 1) * (degreeTwo + 1) + 1][3][3];
				dummyExponents = new int[(Gdegree + 1) * (degreeTwo + 1) + 1];
				index = 1;
				for (indexOne = 0; indexOne <= Gdegree; indexOne++) {
					for (indexTwo = 0; indexTwo <= degreeTwo; indexTwo++) {
						dummyCoefficients[index] = fractionalComplexArithmetic(Gcoefficients[indexOne], polynomialCoefficientsTwo[indexTwo],
								'*');
						dummyExponents[index] = indexOne + indexTwo;
						index++;
					}
				}

				dummyLength = index - 1;

				exchangeNumber = 1;
				while (exchangeNumber > 0) {
					exchangeNumber = 0;
					for (index = 1; index <= (dummyLength - 1); index++) {
						if (dummyExponents[index] > dummyExponents[index + 1]) {
							dummyValue[1][1] = dummyCoefficients[index][1][1];
							dummyValue[1][2] = dummyCoefficients[index][1][2];

							dummyValue[2][1] = dummyCoefficients[index][2][1];
							dummyValue[2][2] = dummyCoefficients[index][2][2];

							dummyCoefficients[index][1][1] = dummyCoefficients[index + 1][1][1];
							dummyCoefficients[index][1][2] = dummyCoefficients[index + 1][1][2];

							dummyCoefficients[index][2][1] = dummyCoefficients[index + 1][2][1];
							dummyCoefficients[index][2][2] = dummyCoefficients[index + 1][2][2];

							dummyCoefficients[index + 1][1][1] = dummyValue[1][1];
							dummyCoefficients[index + 1][1][2] = dummyValue[1][2];

							dummyCoefficients[index + 1][2][1] = dummyValue[2][1];
							dummyCoefficients[index + 1][2][2] = dummyValue[2][2];

							dummyValueInteger = dummyExponents[index];
							dummyExponents[index] = dummyExponents[index + 1];
							dummyExponents[index + 1] = dummyValueInteger;

							exchangeNumber++;
						}
					}
				}

				exchangeNumber = 1;
				while (exchangeNumber > 0) {
					exchangeNumber = 0;
					for (index = 1; index <= (dummyLength - 1); index++) {
						if (dummyExponents[index] == dummyExponents[index + 1]) {
							if ((dummyCoefficients[index + 1][1][1].compareTo(BigInteger.ZERO) != 0)
									|| (dummyCoefficients[index + 1][2][1].compareTo(BigInteger.ZERO) != 0)) {
								dummyCoefficients[index] = fractionalComplexArithmetic(dummyCoefficients[index], dummyCoefficients[index + 1], '+');
								dummyCoefficients[index + 1][1][1] = BigInteger.ZERO;
								dummyCoefficients[index + 1][1][2] = BigInteger.ONE;
								dummyCoefficients[index + 1][2][1] = BigInteger.ZERO;
								dummyCoefficients[index + 1][2][2] = BigInteger.ONE;
								exchangeNumber++;
							}
						}
					}
				}

				productDegree = Gdegree + degreeTwo;
				product = new BigInteger[productDegree + 1][3][3];
				indexTwo = 0;
				for (indexOne = 0; indexOne <= productDegree; indexOne++) {
					product[indexOne][1][1] = BigInteger.ZERO;
					product[indexOne][1][2] = BigInteger.ONE;
					product[indexOne][2][1] = BigInteger.ZERO;
					product[indexOne][2][2] = BigInteger.ONE;
					for (indexTwo = 1; indexTwo <= dummyLength; indexTwo++) {
						if (indexOne == dummyExponents[indexTwo]) {
							product[indexOne] = fractionalComplexArithmetic(product[indexOne], dummyCoefficients[indexTwo], '+');
						}
					}
				}

				/* find 'R - GP2' */
				if (Rdegree <= productDegree) {
					newRdegree = productDegree;
				} else {
					newRdegree = Rdegree;
				}

				polynomialOneAugmented = new BigInteger[newRdegree + 1][3][3];
				for (index = 0; index <= newRdegree; index++) {
					if (index > Rdegree) {
						polynomialOneAugmented[index][1][1] = BigInteger.ZERO;
						polynomialOneAugmented[index][1][2] = BigInteger.ONE;
						polynomialOneAugmented[index][2][1] = BigInteger.ZERO;
						polynomialOneAugmented[index][2][2] = BigInteger.ONE;
					} else {
						polynomialOneAugmented[index][1][1] = Rcoefficients[index][1][1];
						polynomialOneAugmented[index][1][2] = Rcoefficients[index][1][2];
						polynomialOneAugmented[index][2][1] = Rcoefficients[index][2][1];
						polynomialOneAugmented[index][2][2] = Rcoefficients[index][2][2];
					}
				}

				polynomialTwoAugmented = new BigInteger[newRdegree + 1][3][3];
				for (index = 0; index <= newRdegree; index++) {
					if (index > productDegree) {
						polynomialTwoAugmented[index][1][1] = BigInteger.ZERO;
						polynomialTwoAugmented[index][1][2] = BigInteger.ONE;
						polynomialTwoAugmented[index][2][1] = BigInteger.ZERO;
						polynomialTwoAugmented[index][2][2] = BigInteger.ONE;
					} else {
						polynomialTwoAugmented[index][1][1] = product[index][1][1];
						polynomialTwoAugmented[index][1][2] = product[index][1][2];
						polynomialTwoAugmented[index][2][1] = product[index][2][1];
						polynomialTwoAugmented[index][2][2] = product[index][2][2];
					}
				}

				newRcoefficients = new BigInteger[newRdegree + 1][3][3];
				for (index = 0; index <= newRdegree; index++) {
					newRcoefficients[index] = fractionalComplexArithmetic(polynomialOneAugmented[index], polynomialTwoAugmented[index], '-');
				}

				index = newRdegree;
				halt = false;
				while ((index >= 0) & (halt == false)) {
					if ((newRcoefficients[index][1][1].compareTo(BigInteger.ZERO) == 0)
							&& (newRcoefficients[index][2][1].compareTo(BigInteger.ZERO) == 0)) {
						index--;
					} else {
						halt = true;
					}
				}

				if (halt == true) {
					newRdegree = index;
				} else {
					newRdegree = 0;
				}

				Rdegree = newRdegree;
				Rcoefficients = new BigInteger[Rdegree + 1][3][3];
				for (index = 0; index <= Rdegree; index++) {
					Rcoefficients[index][1][1] = newRcoefficients[index][1][1];
					Rcoefficients[index][1][2] = newRcoefficients[index][1][2];

					Rcoefficients[index][2][1] = newRcoefficients[index][2][1];
					Rcoefficients[index][2][2] = newRcoefficients[index][2][2];
				}

				if ((Rdegree == 0) & (degreeTwo == 0)) {
					if (zeroDegreeCount > 0) {
						terminate = true;
					}
					zeroDegreeCount++;
				}
			}
		} else {
			Rdegree = degreeOne;
			Rcoefficients = new BigInteger[Rdegree + 1][3][3];
			for (index = 0; index <= Rdegree; index++) {
				Rcoefficients[index][1][1] = polynomialCoefficientsOne[index][1][1];
				Rcoefficients[index][1][2] = polynomialCoefficientsOne[index][1][2];

				Rcoefficients[index][2][1] = polynomialCoefficientsOne[index][2][1];
				Rcoefficients[index][2][2] = polynomialCoefficientsOne[index][2][2];
			}

			Qdegree = 0;
			Qcoefficients = new BigInteger[Qdegree + 1][3][3];
			Qcoefficients[0][1][1] = BigInteger.ZERO;
			Qcoefficients[0][1][2] = BigInteger.ONE;
			Qcoefficients[0][2][1] = BigInteger.ZERO;
			Qcoefficients[0][2][2] = BigInteger.ONE;
		}

		/* find 'pseudo Q and R' */
		QcoefficientsPseudo = new BigInteger[Qdegree + 1][3][3];
		for (index = Qdegree; index >= 0; index--) {
			QcoefficientsPseudo[index] = fractionalComplexArithmetic(bValue, Qcoefficients[index], '*');
		}

		RcoefficientsPseudo = new BigInteger[Rdegree + 1][3][3];
		for (index = Rdegree; index >= 0; index--) {
			RcoefficientsPseudo[index] = fractionalComplexArithmetic(bValue, Rcoefficients[index], '*');
		}

		result.add(Qdegree);
		result.add(Rdegree);
		result.add(Qcoefficients);
		result.add(Rcoefficients);
		result.add(bValue);
		result.add(QcoefficientsPseudo);
		result.add(RcoefficientsPseudo);

		return (result);
	}

	public static BigInteger[][] fractionalComplexArithmetic(BigInteger[][] numberOne, BigInteger[][] numberTwo, char sign) {
		/* Returns +,-,*-/ of two fractional complex numbers */
		BigInteger[][] result = new BigInteger[3][3];
		BigInteger[][] numberTwoConjugate = new BigInteger[3][3];
		BigInteger[] divisor;// = new BigInteger[3];

		numberTwoConjugate[1][1] = numberTwo[1][1];
		numberTwoConjugate[1][2] = numberTwo[1][2];
		numberTwoConjugate[2][1] = numberTwo[2][1].negate();
		numberTwoConjugate[2][2] = numberTwo[2][2];

		switch (sign) {
		case '+':
			result[1] = fractionalArithmetic(numberOne[1], numberTwo[1], '+');
			result[2] = fractionalArithmetic(numberOne[2], numberTwo[2], '+');
			break;

		case '-':
			result[1] = fractionalArithmetic(numberOne[1], numberTwo[1], '-');
			result[2] = fractionalArithmetic(numberOne[2], numberTwo[2], '-');
			break;

		case '*':
			result[1] = fractionalArithmetic(fractionalArithmetic(numberOne[1], numberTwo[1], '*'), fractionalArithmetic(numberOne[2],
					numberTwo[2], '*'), '-');
			result[2] = fractionalArithmetic(fractionalArithmetic(numberOne[1], numberTwo[2], '*'), fractionalArithmetic(numberOne[2],
					numberTwo[1], '*'), '+');
			break;

		case '/':
			divisor = fractionalArithmetic(fractionalArithmetic(numberTwo[1], numberTwo[1], '*'), fractionalArithmetic(numberTwo[2],
					numberTwo[2], '*'), '+');
			result[1] = fractionalArithmetic(fractionalArithmetic(numberOne[1], numberTwoConjugate[1], '*'), fractionalArithmetic(
					numberOne[2], numberTwoConjugate[2], '*'), '-');
			result[2] = fractionalArithmetic(fractionalArithmetic(numberOne[1], numberTwoConjugate[2], '*'), fractionalArithmetic(
					numberOne[2], numberTwoConjugate[1], '*'), '+');
			result[1] = fractionalArithmetic(result[1], divisor, '/');
			result[2] = fractionalArithmetic(result[2], divisor, '/');
			break;
		}

		return (result);
	}

	public static BigInteger[] fractionalArithmetic(BigInteger[] fractionOne, BigInteger[] fractionTwo, char sign) {
		/* Returns uncorrected +,-,*-/ of two complex numbers */

		BigInteger[] result = new BigInteger[3];
		BigInteger lcm;
		BigInteger hcf;

		switch (sign) {
		case '+':
			lcm = (fractionOne[2].multiply(fractionTwo[2])).divide(fractionOne[2].gcd(fractionTwo[2]));
			result[1] = ((lcm.divide(fractionOne[2])).multiply(fractionOne[1]))
					.add((lcm.divide(fractionTwo[2])).multiply(fractionTwo[1]));
			result[2] = lcm;
			break;

		case '-':
			lcm = (fractionOne[2].multiply(fractionTwo[2])).divide(fractionOne[2].gcd(fractionTwo[2]));
			result[1] = ((lcm.divide(fractionOne[2])).multiply(fractionOne[1])).subtract((lcm.divide(fractionTwo[2]))
					.multiply(fractionTwo[1]));
			result[2] = lcm;
			break;

		case '*':
			result[1] = fractionOne[1].multiply(fractionTwo[1]);
			result[2] = fractionOne[2].multiply(fractionTwo[2]);
			break;

		case '/':
			result[1] = fractionOne[1].multiply(fractionTwo[2]);
			result[2] = fractionOne[2].multiply(fractionTwo[1]);
			break;
		}

		hcf = (result[1].abs()).gcd(result[2]);
		result[1] = result[1].divide(hcf);
		result[2] = result[2].divide(hcf);

		if (result[2].compareTo(BigInteger.ZERO) == -1) {
			result[1] = result[1].negate();
			result[2] = result[2].negate();
		}

		return (result);
	}

	public static List polynomialDivisionOverFiniteField(BigInteger[] polynomialA, BigInteger[] polynomialB, int degreeA,
			int degreeB, BigInteger qValue) {
		/*
		 * divides two polynomials P1, P2 over the the finite field Fq, where 'q'
		 * must be prime note: P2 should not be zero else an error is returned
		 */

		BigInteger[] polynomialOne;
		BigInteger[] polynomialTwo;
		BigInteger[] dummyPolynomial;
		BigInteger[] newRpolynomial;
		BigInteger[] Gpolynomial;
		BigInteger[] Qpolynomial = new BigInteger[0];
		BigInteger[] Rpolynomial = new BigInteger[0];
		BigInteger[] newQpolynomial;
		BigInteger[] product;

		BigInteger inverseElement;

		int degreeOne = degreeA;
		int degreeTwo = degreeB;
		int productDegree;
		int newRdegree;
		int index;
		int Gdegree;
		int Rdegree = 0;
		int Qdegree = 0;
		int newQdegree;
		int exchangeNumber;
		int dummyValue;
		int indexOne;
		int indexTwo;
		int zeroDegreeCount = 0;

		boolean halt;
		boolean terminate;
		boolean error;

		List multiplicationResult;
		List inverseElementResult;
		List<Object> result = new ArrayList<Object>(4);

		polynomialOne = new BigInteger[degreeOne + 1];
		for (index = 0; index <= degreeOne; index++) {
			polynomialOne[index] = polynomialA[index];
		}

		polynomialTwo = new BigInteger[degreeTwo + 1];
		for (index = 0; index <= degreeTwo; index++) {
			polynomialTwo[index] = polynomialB[index];
		}

		/* convert coefficients */
		for (index = 0; index <= degreeOne; index++) {
			if ((polynomialOne[index].abs()).compareTo(qValue) != -1) {
				polynomialOne[index] = (polynomialOne[index].divideAndRemainder(qValue))[1];
			}

			if (polynomialOne[index].compareTo(BigInteger.ZERO) == -1) {
				polynomialOne[index] = polynomialOne[index].add(qValue);
			}
		}

		for (index = 0; index <= degreeTwo; index++) {
			if ((polynomialTwo[index].abs()).compareTo(qValue) != -1) {
				polynomialTwo[index] = (polynomialTwo[index].divideAndRemainder(qValue))[1];
			}

			if (polynomialTwo[index].compareTo(BigInteger.ZERO) == -1) {
				polynomialTwo[index] = polynomialTwo[index].add(qValue);
			}
		}

		/* carry out 'degree reduction' */
		halt = false;
		while ((halt == false) & (degreeOne > 0)) {
			if (polynomialOne[degreeOne].compareTo(BigInteger.ZERO) != 0) {
				halt = true;
			} else {
				degreeOne--;
			}
		}

		halt = false;
		while ((halt == false) & (degreeTwo > 0)) {
			if (polynomialTwo[degreeTwo].compareTo(BigInteger.ZERO) != 0) {
				halt = true;
			} else {
				degreeTwo--;
			}
		}

		if (!((degreeTwo == 0) && (polynomialTwo[degreeTwo].compareTo(BigInteger.ZERO) == 0))) {
			error = false;
			if (degreeOne >= degreeTwo) {
				/* initalise R and Q */
				Rdegree = degreeOne;
				Rpolynomial = new BigInteger[Rdegree + 1];
				for (index = 0; index <= Rdegree; index++) {
					Rpolynomial[index] = polynomialOne[index];
				}

				Qdegree = 0;
				Qpolynomial = new BigInteger[Qdegree + 1];
				Qpolynomial[0] = BigInteger.ZERO;

				/* start algorithm */
				terminate = false;
				while ((Rdegree >= degreeTwo) & (terminate == false)) {
					/* find G */
					Gdegree = Rdegree - degreeTwo;
					Gpolynomial = new BigInteger[Gdegree + 1];
					for (index = 0; index <= Gdegree; index++) {
						if (index < Gdegree) {
							Gpolynomial[index] = BigInteger.ZERO;
						} else {
							/* find 'inverse element' */
							inverseElementResult = multiplicativeInverse(polynomialTwo[degreeTwo], qValue);
							inverseElement = (BigInteger) inverseElementResult.get(1);

							Gpolynomial[index] = Rpolynomial[Rdegree].multiply(inverseElement);

							if ((Gpolynomial[index].abs()).compareTo(qValue) != -1) {
								Gpolynomial[index] = (Gpolynomial[index].divideAndRemainder(qValue))[1];
							}

							if (Gpolynomial[index].compareTo(BigInteger.ZERO) == -1) {
								Gpolynomial[index] = Gpolynomial[index].add(qValue);
							}
						}
					}

					if (Qdegree <= Gdegree) {
						newQdegree = Gdegree;
					} else {
						newQdegree = Qdegree;
					}

					newQpolynomial = new BigInteger[newQdegree + 1];
					for (index = 0; index <= newQdegree; index++) {
						if ((index <= Qdegree) & (index <= Gdegree)) {
							newQpolynomial[index] = Qpolynomial[index].add(Gpolynomial[index]);

							if ((newQpolynomial[index].abs()).compareTo(qValue) != -1) {
								newQpolynomial[index] = (newQpolynomial[index].divideAndRemainder(qValue))[1];
							}

							if (newQpolynomial[index].compareTo(BigInteger.ZERO) == -1) {
								newQpolynomial[index] = newQpolynomial[index].add(qValue);
							}
						} else {
							if (index > Qdegree) {
								newQpolynomial[index] = Gpolynomial[index];
							} else {
								newQpolynomial[index] = Qpolynomial[index];
							}
						}
					}

					Qpolynomial = new BigInteger[newQdegree + 1];
					for (index = 0; index <= newQdegree; index++) {
						Qpolynomial[index] = newQpolynomial[index];
					}

					index = newQdegree;
					halt = false;
					while ((index >= 0) & (halt == false)) {
						if (Qpolynomial[index].compareTo(BigInteger.ZERO) == 0) {
							index--;
						} else {
							halt = true;
						}
					}

					if (halt == true) {
						Qdegree = index;
					} else {
						Qdegree = 0;
					}

					/* find GP2 */
					multiplicationResult = polynomialArithmeticModular(Gpolynomial, polynomialTwo, Gdegree, degreeTwo, qValue, '*');
					productDegree = (Integer) multiplicationResult.get(0);
					product = (BigInteger[]) multiplicationResult.get(1);

					if (Rdegree <= productDegree) {
						newRdegree = productDegree;
					} else {
						newRdegree = Rdegree;
					}

					newRpolynomial = new BigInteger[newRdegree + 1];
					for (index = 0; index <= newRdegree; index++) {
						if ((index <= Rdegree) & (index <= productDegree)) {
							newRpolynomial[index] = Rpolynomial[index].subtract(product[index]);

							if ((newRpolynomial[index].abs()).compareTo(qValue) != -1) {
								newRpolynomial[index] = (newRpolynomial[index].divideAndRemainder(qValue))[1];
							}

							if (newRpolynomial[index].compareTo(BigInteger.ZERO) == -1) {
								newRpolynomial[index] = newRpolynomial[index].add(qValue);
							}
						} else {
							if (index > Rdegree) {
								newRpolynomial[index] = product[index];
							} else {
								newRpolynomial[index] = Rpolynomial[index];
							}
						}
					}

					Rpolynomial = new BigInteger[newRdegree + 1];
					for (index = 0; index <= newRdegree; index++) {
						Rpolynomial[index] = newRpolynomial[index];
					}

					index = newRdegree;
					halt = false;
					while ((index >= 0) & (halt == false)) {
						if (Rpolynomial[index].compareTo(BigInteger.ZERO) == 0) {
							index--;
						} else {
							halt = true;
						}
					}

					if (halt == true) {
						Rdegree = index;
					} else {
						Rdegree = 0;
					}

					if ((Rdegree == 0) & (degreeTwo == 0)) {
						if (zeroDegreeCount > 0) {
							terminate = true;
						}
						zeroDegreeCount++;
					}
				}
			} else {
				Rdegree = degreeOne;
				Rpolynomial = new BigInteger[Rdegree + 1];
				for (index = 0; index <= Rdegree; index++) {
					Rpolynomial[index] = polynomialOne[index];
				}

				Qdegree = 0;
				Qpolynomial = new BigInteger[Qdegree + 1];
				Qpolynomial[0] = BigInteger.ZERO;
			}

			/* carry out 'degree reductions' on results */
			halt = false;
			while ((halt == false) & (Qdegree > 0)) {
				if (Qpolynomial[Qdegree].compareTo(BigInteger.ZERO) != 0) {
					halt = true;
				} else {
					Qdegree--;
				}
			}

			halt = false;
			while ((halt == false) & (Rdegree > 0)) {
				if (Rpolynomial[Rdegree].compareTo(BigInteger.ZERO) != 0) {
					halt = true;
				} else {
					Rdegree--;
				}
			}

		} else {
			error = true;
		}

		result.add(error);
		result.add(Qdegree);
		result.add(Rdegree);
		result.add(Qpolynomial);
		result.add(Rpolynomial);

		return (result);
	}

	public static List polynomialArithmeticModular(BigInteger[] polynomialA, BigInteger[] polynomialB, int degreeA, int degreeB,
			BigInteger qValue, char sign) {

		/*
		 * carries out polynomial arithmetic '*','+','-' over a finite field Fq,
		 * where 'q' must be prime
		 */

		BigInteger[] dummyOne;
		BigInteger[] dummyTwo;
		BigInteger[] polynomialOne;
		BigInteger[] polynomialTwo;
		BigInteger[] resultPolynomial = new BigInteger[0];
		BigInteger[] dummyPolynomial = new BigInteger[(degreeA + 1) * (degreeB + 1) + 1];
		int[] dummyExponents = new int[(degreeA + 1) * (degreeB + 1) + 1];

		BigInteger bigDummyValue;

		int degreeOne = degreeA;
		int degreeTwo = degreeB;
		int index;
		int indexOne;
		int indexTwo;
		int dummyLength;
		int exchangeNumber;
		int resultDegree = 0;
		int dummyValue;

		boolean halt;

		List<Object> result = new ArrayList<Object>(4);

		polynomialOne = new BigInteger[degreeOne + 1];
		for (index = 0; index <= degreeOne; index++) {
			polynomialOne[index] = polynomialA[index];
		}

		polynomialTwo = new BigInteger[degreeTwo + 1];
		for (index = 0; index <= degreeTwo; index++) {
			polynomialTwo[index] = polynomialB[index];
		}

		/* convert polynomial coefficients */
		for (index = 0; index <= degreeOne; index++) {
			if ((polynomialOne[index].abs()).compareTo(qValue) != -1) {
				polynomialOne[index] = (polynomialOne[index].divideAndRemainder(qValue))[1];
			}

			if (polynomialOne[index].compareTo(BigInteger.ZERO) == -1) {
				polynomialOne[index] = polynomialOne[index].add(qValue);
			}
		}

		for (index = 0; index <= degreeTwo; index++) {
			if ((polynomialTwo[index].abs()).compareTo(qValue) != -1) {
				polynomialTwo[index] = (polynomialTwo[index].divideAndRemainder(qValue))[1];
			}

			if (polynomialTwo[index].compareTo(BigInteger.ZERO) == -1) {
				polynomialTwo[index] = polynomialTwo[index].add(qValue);
			}
		}

		/* carry out 'degree reduction' */
		halt = false;
		while ((halt == false) & (degreeOne > 0)) {
			if (polynomialOne[degreeOne].compareTo(BigInteger.ZERO) != 0) {
				halt = true;
			} else {
				degreeOne--;
			}
		}

		halt = false;
		while ((halt == false) & (degreeTwo > 0)) {
			if (polynomialTwo[degreeTwo].compareTo(BigInteger.ZERO) != 0) {
				halt = true;
			} else {
				degreeTwo--;
			}
		}

		switch (sign) {
		case '+':
			if (degreeOne > degreeTwo) {
				resultDegree = degreeOne;
			} else {
				resultDegree = degreeTwo;
			}

			dummyOne = new BigInteger[resultDegree + 1];
			for (index = 0; index <= resultDegree; index++) {
				if (index <= degreeOne) {
					dummyOne[index] = polynomialOne[index];
				} else {
					dummyOne[index] = BigInteger.ZERO;
				}
			}

			dummyTwo = new BigInteger[resultDegree + 1];
			for (index = 0; index <= resultDegree; index++) {
				if (index <= degreeTwo) {
					dummyTwo[index] = polynomialTwo[index];
				} else {
					dummyTwo[index] = BigInteger.ZERO;
				}
			}

			resultPolynomial = new BigInteger[resultDegree + 1];
			for (index = 0; index <= resultDegree; index++) {
				resultPolynomial[index] = dummyOne[index].add(dummyTwo[index]);

				if ((resultPolynomial[index].abs()).compareTo(qValue) != -1) {
					resultPolynomial[index] = (resultPolynomial[index].divideAndRemainder(qValue))[1];
				}

				if (resultPolynomial[index].compareTo(BigInteger.ZERO) == -1) {
					resultPolynomial[index] = resultPolynomial[index].add(qValue);
				}
			}
			break;

		case '-':
			if (degreeOne > degreeTwo) {
				resultDegree = degreeOne;
			} else {
				resultDegree = degreeTwo;
			}

			dummyOne = new BigInteger[resultDegree + 1];
			for (index = 0; index <= resultDegree; index++) {
				if (index <= degreeOne) {
					dummyOne[index] = polynomialOne[index];
				} else {
					dummyOne[index] = BigInteger.ZERO;
				}
			}

			dummyTwo = new BigInteger[resultDegree + 1];
			for (index = 0; index <= resultDegree; index++) {
				if (index <= degreeTwo) {
					dummyTwo[index] = polynomialTwo[index];
				} else {
					dummyTwo[index] = BigInteger.ZERO;
				}
			}

			resultPolynomial = new BigInteger[resultDegree + 1];
			for (index = 0; index <= resultDegree; index++) {
				resultPolynomial[index] = dummyOne[index].subtract(dummyTwo[index]);

				if ((resultPolynomial[index].abs()).compareTo(qValue) != -1) {
					resultPolynomial[index] = (resultPolynomial[index].divideAndRemainder(qValue))[1];
				}

				if (resultPolynomial[index].compareTo(BigInteger.ZERO) == -1) {
					resultPolynomial[index] = resultPolynomial[index].add(qValue);
				}
			}
			break;

		case '*':
			/* form polynomial product */
			index = 1;
			for (indexOne = 0; indexOne <= degreeOne; indexOne++) {
				for (indexTwo = 0; indexTwo <= degreeTwo; indexTwo++) {
					dummyPolynomial[index] = polynomialOne[indexOne].multiply(polynomialTwo[indexTwo]);

					if ((dummyPolynomial[index].abs()).compareTo(qValue) != -1) {
						dummyPolynomial[index] = (dummyPolynomial[index].divideAndRemainder(qValue))[1];
					}

					if (dummyPolynomial[index].compareTo(BigInteger.ZERO) == -1) {
						dummyPolynomial[index] = dummyPolynomial[index].add(qValue);
					}

					dummyExponents[index] = indexOne + indexTwo;
					index++;
				}
			}

			dummyLength = index - 1;

			exchangeNumber = 1;
			while (exchangeNumber > 0) {
				exchangeNumber = 0;
				for (index = 1; index <= (dummyLength - 1); index++) {
					if (dummyExponents[index] > dummyExponents[index + 1]) {
						bigDummyValue = dummyPolynomial[index];
						dummyPolynomial[index] = dummyPolynomial[index + 1];
						dummyPolynomial[index + 1] = bigDummyValue;

						dummyValue = dummyExponents[index];
						dummyExponents[index] = dummyExponents[index + 1];
						dummyExponents[index + 1] = dummyValue;

						exchangeNumber++;
					}
				}
			}

			exchangeNumber = 1;
			while (exchangeNumber > 0) {
				exchangeNumber = 0;
				for (index = 1; index <= (dummyLength - 1); index++) {
					if (dummyExponents[index] == dummyExponents[index + 1]) {
						if (dummyPolynomial[index + 1].compareTo(BigInteger.ZERO) != 0) {
							dummyPolynomial[index] = dummyPolynomial[index].add(dummyPolynomial[index + 1]);

							if ((dummyPolynomial[index].abs()).compareTo(qValue) != -1) {
								dummyPolynomial[index] = (dummyPolynomial[index].divideAndRemainder(qValue))[1];
							}

							if (dummyPolynomial[index].compareTo(BigInteger.ZERO) == -1) {
								dummyPolynomial[index] = dummyPolynomial[index].add(qValue);
							}

							dummyPolynomial[index + 1] = BigInteger.ZERO;

							exchangeNumber++;
						}
					}
				}
			}

			resultDegree = degreeOne + degreeTwo;
			resultPolynomial = new BigInteger[resultDegree + 1];
			indexTwo = 0;
			for (indexOne = 0; indexOne <= resultDegree; indexOne++) {
				resultPolynomial[indexOne] = BigInteger.ZERO;

				for (indexTwo = 1; indexTwo <= dummyLength; indexTwo++) {
					if (indexOne == dummyExponents[indexTwo]) {
						resultPolynomial[indexOne] = resultPolynomial[indexOne].add(dummyPolynomial[indexTwo]);

						if ((resultPolynomial[indexOne].abs()).compareTo(qValue) != -1) {
							resultPolynomial[indexOne] = (resultPolynomial[indexOne].divideAndRemainder(qValue))[1];
						}

						if (resultPolynomial[indexOne].compareTo(BigInteger.ZERO) == -1) {
							resultPolynomial[indexOne] = resultPolynomial[indexOne].add(qValue);
						}
					}
				}
			}
			break;
		}

		/* check degree */
		halt = false;
		while ((halt == false) & (resultDegree > 0)) {
			if (resultPolynomial[resultDegree].compareTo(BigInteger.ZERO) != 0) {
				halt = true;
			} else {
				resultDegree--;
			}
		}

		result.add(resultDegree);
		result.add(resultPolynomial);

		return (result);
	}

	public static List multiplicativeInverse(BigInteger number, BigInteger modulo) {
		/*
		 * finds the multiplicative inverse of an element note 'number' and 'modulo'
		 * must both be co-prime. 'modulo' must also be positive
		 */

		BigInteger multiplicativeInverse = BigInteger.ONE;

		boolean error;

		List extendedEuclideanAlgorithmResult;
		List<Object> result = new ArrayList<Object>(4);

		if ((number.abs()).compareTo(modulo) != -1) {
			number = (number.divideAndRemainder(modulo))[1];
		}

		if (number.compareTo(BigInteger.ZERO) == -1) {
			number = number.add(modulo);
		}

		extendedEuclideanAlgorithmResult = extendedEuclideanAlgorithm(number, modulo);
		if (((BigInteger) extendedEuclideanAlgorithmResult.get(0)).compareTo(BigInteger.ONE) == 0) {
			error = false;

			multiplicativeInverse = (BigInteger) extendedEuclideanAlgorithmResult.get(2);

			if ((multiplicativeInverse.abs()).compareTo(modulo) != -1) {
				multiplicativeInverse = (multiplicativeInverse.divideAndRemainder(modulo))[1];
			}

			if (multiplicativeInverse.compareTo(BigInteger.ZERO) == -1) {
				multiplicativeInverse = multiplicativeInverse.add(modulo);
			}
		} else {
			error = true;
		}

		result.add(error);
		result.add(multiplicativeInverse);

		return (result);

	}

	public static List extendedEuclideanAlgorithm(BigInteger numberOne, BigInteger numberTwo) {
		/* returns the gcd of two positive numbers plus the bezout relation */
		BigInteger[] divisionResult;// = new BigInteger[2];
		BigInteger dividend;
		BigInteger divisor;
		BigInteger quotient;
		BigInteger remainder;
		BigInteger xValue;
		BigInteger yValue;
		BigInteger tempValue;
		BigInteger lastxValue;
		BigInteger lastyValue;
		BigInteger gcd = BigInteger.ONE;
		BigInteger mValue = BigInteger.ONE;
		BigInteger nValue = BigInteger.ONE;

		boolean error = false;

		List<Object> result = new ArrayList<Object>(4);

		remainder = BigInteger.ONE;
		xValue = BigInteger.ZERO;
		lastxValue = BigInteger.ONE;
		yValue = BigInteger.ONE;
		lastyValue = BigInteger.ZERO;
		if ((!((numberOne.compareTo(BigInteger.ZERO) == 0) || (numberTwo.compareTo(BigInteger.ZERO) == 0)))
				&& (((numberOne.compareTo(BigInteger.ZERO) == 1) && (numberTwo.compareTo(BigInteger.ZERO) == 1)))) {
			if (numberOne.compareTo(numberTwo) == 1) {
				dividend = numberOne;
				divisor = numberTwo;
			} else {
				dividend = numberTwo;
				divisor = numberOne;
			}

			while (remainder.compareTo(BigInteger.ZERO) != 0) {
				divisionResult = dividend.divideAndRemainder(divisor);
				quotient = divisionResult[0];
				remainder = divisionResult[1];

				dividend = divisor;
				divisor = remainder;

				tempValue = xValue;
				xValue = lastxValue.subtract(quotient.multiply(xValue));
				lastxValue = tempValue;

				tempValue = yValue;
				yValue = lastyValue.subtract(quotient.multiply(yValue));
				lastyValue = tempValue;
			}

			gcd = dividend;
			mValue = lastxValue;
			nValue = lastyValue;
		} else {
			error = true;
		}

		result.add(gcd);
		result.add(mValue);
		result.add(nValue);

		return (result);
	}

}
