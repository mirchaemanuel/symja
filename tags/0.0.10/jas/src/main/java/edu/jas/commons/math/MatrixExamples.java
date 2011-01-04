package edu.jas.commons.math;

import edu.jas.arith.BigRational;
import edu.jas.vector.GenMatrix;
import edu.jas.vector.GenMatrixRing;
import edu.jas.vector.GenVector;
import edu.jas.vector.GenVectorModul;


/**
 * Example that computes a solution of a linear equation system.
 * 
 * 
 */
public class MatrixExamples {

  public static void main(String[] argv) {
    example1();
  }

  public static void example1() {
    BigRational r1, r2, r3, r4, r5, r6, fac;
    r1 = new BigRational(1, 10);
    r2 = new BigRational(6, 5);
    r3 = new BigRational(1, 9);
    r4 = new BigRational(1, 1);
    r5 = r2.sum(r3);
    r6 = r1.multiply(r4);

    fac = new BigRational();

    BigRational[][] aa = new BigRational[][] { { r1, r2, r3 }, { r4, r5, r6 },
        { r2, r1, r3 } };
    GenMatrixRing<BigRational> mfac = new GenMatrixRing<BigRational>(fac,
        aa.length, aa[0].length);
    GenMatrix<BigRational> a = new GenMatrix<BigRational>(mfac, CMFieldElementUtil
        .<BigRational> toList(aa));
    System.out.println("system = " + a);

    BigRational[] ba = new BigRational[] { r1, r2, r3 };
    GenVectorModul<BigRational> vfac = new GenVectorModul<BigRational>(fac,
        ba.length);
    GenVector<BigRational> b = new GenVector<BigRational>(vfac, CMFieldElementUtil
        .<BigRational> toList(ba));
    System.out.println("right hand side = " + b);

    GaussElimination<BigRational> ge = new GaussElimination<BigRational>();
    GenVector<BigRational> x = ge.solve(a, b);
    System.out.println("solution = " + x);

  }

}
