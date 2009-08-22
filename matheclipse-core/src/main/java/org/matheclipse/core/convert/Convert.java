package org.matheclipse.core.convert;

import static org.matheclipse.core.expression.F.List;

import org.apache.commons.math.linear.ArrayFieldVector;
import org.apache.commons.math.linear.BlockFieldMatrix;
import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.FieldVector;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;

/**
 * Conversions between an IExpr object and misc other object class types
 */
public class Convert {

	private Convert() {
	}

	public static IExpr doubleToExprTranspose(final double[][] dd) {
		final IAST list = List();
		try {
			for (int j = 0; j < dd[0].length; j++) {
				final IAST row = List();
				for (int i = 0; i < dd.length; i++) {
					row.add(F.num(dd[i][j]));
				}
				list.add(row);
			}
			list.addEvalFlags(IAST.IS_MATRIX);
			return list;
		} catch (final Throwable ex) {
		}
		return null;
	}

	/**
	 * Returns a FieldMatrix if possible
	 * 
	 * @param listMatrix
	 * @return
	 * @throws ClassCastException
	 * @throws IndexOutOfBoundsException
	 */
	public static FieldMatrix<IExpr> list2Matrix(final IAST listMatrix) throws ClassCastException, IndexOutOfBoundsException {
		if (listMatrix == null) {
			return null;
		}
		final Object header = listMatrix.head();
		if (header != F.List) {
			return null;
		}

		IAST currInRow = (IAST) listMatrix.get(1);
		if (currInRow.size() == 1) {
			// special case 0-Matrix
			IExpr[][] array = new IExpr[0][0];
			return new BlockFieldMatrix<IExpr>(array);
		}
		final int rowSize = listMatrix.size() - 1;
		final int colSize = currInRow.size() - 1;

		final IExpr[][] elements = new IExpr[rowSize][colSize];
		for (int i = 1; i < rowSize + 1; i++) {
			currInRow = (IAST) listMatrix.get(i);
			if (currInRow.head() != F.List) {
				return null;
			}
			for (int j = 1; j < colSize + 1; j++) {
				elements[i - 1][j - 1] = currInRow.get(j);
			}
		}
		return new BlockFieldMatrix<IExpr>(elements);
	}

	/**
	 * Converts a FieldMatrix to the list expression representation
	 * 
	 * @param matrix
	 * @return
	 * @throws ClassCastException
	 * @throws IndexOutOfBoundsException
	 */
	public static IAST matrix2List(final FieldMatrix<IExpr> matrix) {
		if (matrix == null) {
			return null;
		}
		final int rowSize = matrix.getRowDimension();
		final int colSize = matrix.getColumnDimension();

		final IAST out = F.List();
		IAST currOutRow;
		for (int i = 0; i < rowSize; i++) {
			currOutRow = F.List();
			out.add(currOutRow);
			for (int j = 0; j < colSize; j++) {
				IExpr expr = matrix.getEntry(i, j);
				if (expr instanceof INumber) {
					currOutRow.add(matrix.getEntry(i, j));
				} else {
					currOutRow.add(F.Expand(matrix.getEntry(i, j)));
				}
			}
		}
		out.addEvalFlags(IAST.IS_MATRIX);
		return out;
	}

	public static FieldVector<IExpr> list2Vector(final IAST listVector) throws ClassCastException, IndexOutOfBoundsException {
		if (listVector == null) {
			return null;
		}
		final Object header = listVector.head();
		if (header != F.List) {
			return null;
		}

		final int rowSize = listVector.size() - 1;

		final IExpr[] elements = new IExpr[rowSize];
		for (int i = 0; i < rowSize; i++) {
			elements[i] = listVector.get(i + 1);
		}
		return new ArrayFieldVector<IExpr>(elements);
	}

	public static IAST vector2List(final FieldVector<IExpr> vector) {
		if (vector == null) {
			return null;
		}
		final int rowSize = vector.getDimension();

		final IAST out = F.function(F.List);
		for (int i = 0; i < rowSize; i++) {
			out.add(vector.getEntry(i));
		}
		out.addEvalFlags(IAST.IS_VECTOR);
		return out;
	}

	/**
	 * Convert an expression into a JScience polynomial. Throws different
	 * exceptions if the conversion is not possible:<br>
	 * 
	 * @param exprPoly
	 *          an expression which should be converted into a JScience polynomial
	 * @param variables
	 *          a list of the variables which could occur in the polynomial
	 * @return the corresponding JScience polynomial
	 * @throws ArithmeticException
	 *           if the exponent of a <code>Power</code> expression doesn't fit
	 *           into a Java <code>int</code>
	 * @throws ClassCastException
	 *           if the expression is an AST with an unsuitable head (i.e. no
	 *           <code>Plus, Times, Power</code> head)
	 */
	// public static Polynomial<IExpr> expr2Polynomial(final IExpr exprPoly,
	// final List<IExpr> variables) throws ArithmeticException,
	// ClassCastException {
	// if (exprPoly instanceof IAST) {
	// final IAST ast = (IAST) exprPoly;
	// Polynomial<IExpr> result = null;
	// Polynomial<IExpr> p = null;
	// if (ast.isASTSizeGE(F.Plus, 2)) {
	// IExpr expr = ast.get(1);
	// result = expr2Polynomial(expr, variables);
	// for (int i = 2; i < ast.size(); i++) {
	// checkCanceled();
	// expr = ast.get(i);
	// p = expr2Polynomial(expr, variables);
	// result = result.plus(p);
	// }
	// return result;
	// } else if (ast.isASTSizeGE(F.Times, 2)) {
	// IExpr expr = ast.get(1);
	// result = expr2Polynomial(expr, variables);
	// for (int i = 2; i < ast.size(); i++) {
	// checkCanceled();
	// expr = ast.get(i);
	// p = expr2Polynomial(expr, variables);
	// result = result.times(p);
	// }
	// return result;
	// } else if (ast.isAST(F.Power, 3)) {
	// final IExpr expr = ast.get(1);
	// for (int i = 0; i < variables.size(); i++) {
	// checkCanceled();
	// if (variables.get(i).equals(expr)) {
	// return Polynomial.valueOf(F.C1, (ISymbol) expr).pow(
	// ((IInteger) ast.get(2)).toInt());
	// }
	// }
	// return Constant.valueOf((IExpr) ast);
	// }
	// } else if (exprPoly instanceof ISymbol) {
	// for (int i = 0; i < variables.size(); i++) {
	// checkCanceled();
	// if (variables.get(i).equals(exprPoly)) {
	// return Polynomial.valueOf(F.C1, (ISymbol) exprPoly);
	// }
	// }
	// }
	//
	// // check if this expression contains any variable
	// for (int i = 0; i < variables.size(); i++) {
	// checkCanceled();
	// if (!exprPoly.isFree(variables.get(i))) {
	// throw new ClassCastException();
	// }
	// }
	//
	// return Constant.valueOf(exprPoly);
	//
	// }
}
