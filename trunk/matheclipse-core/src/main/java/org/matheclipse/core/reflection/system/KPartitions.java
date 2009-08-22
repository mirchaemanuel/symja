package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.generic.combinatoric.KPartitionsList;

public class KPartitions extends AbstractFunctionEvaluator {

	public KPartitions() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {

		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST) && (functionList.get(2) instanceof IInteger)) {
			final IAST listArg0 = (IAST) functionList.get(1);
			// final int n = listArg0.size() - 1;
			final int k = ((IInteger) functionList.get(2)).getBigNumerator().intValue();
			final IAST result = F.function(F.List);
			// IAST part;
			// IAST temp;
			final KPartitionsList<IExpr, IAST> iter = new KPartitionsList<IExpr, IAST>(listArg0, k, F.function(F.List), AST.COPY, 1);
			for (IAST part : iter) {
				if (part == null) {
					break;
				}
				// part = F.function(F.List);
				// // System.out.println("Part:");
				// int j = 0;
				// for (int i = 1; i < partitionsIndex.length; i++) {
				// checkCanceled();
				// // System.out.println(partitionsIndex[i] + ",");
				// temp = F.function(F.List);
				// for (int m = j+1; m < partitionsIndex[i]+1; m++) {
				// temp.add(listArg0.get(m));
				// }
				// j = partitionsIndex[i];
				// part.add(temp);
				// }
				//
				// temp = F.function(F.List);
				// for (int m = j+1; m < n+1; m++) {
				// temp.add(listArg0.get(m));
				// }
				// part.add(temp);
				result.add(part);
			}
			// final KPartitionsIterable iter = new KPartitionsIterable(n, k);
			// for (int[] partitionsIndex : iter) {
			// if (partitionsIndex==null) {
			// break;
			// }
			// part = F.function(F.List);
			// // System.out.println("Part:");
			// int j = 0;
			// for (int i = 1; i < partitionsIndex.length; i++) {
			// checkCanceled();
			// // System.out.println(partitionsIndex[i] + ",");
			// temp = F.function(F.List);
			// for (int m = j+1; m < partitionsIndex[i]+1; m++) {
			// temp.add(listArg0.get(m));
			// }
			// j = partitionsIndex[i];
			// part.add(temp);
			// }
			//
			// temp = F.function(F.List);
			// for (int m = j+1; m < n+1; m++) {
			// temp.add(listArg0.get(m));
			// }
			// part.add(temp);
			// result.add(part);
			// }

			return result;
		}
		return null;
	}

}
