/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.matheclipse.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CandidateView extends View {

	private static final int OUT_OF_BOUNDS = -1;

	private SymjaActivity mService;
	private List<String> mSuggestions;
	private int mSelectedIndex;
	private int mTouchX = OUT_OF_BOUNDS;
	private Drawable mSelectionHighlight;
	private boolean mTypedWordValid;

	private Rect mBgPadding;

	private static final int MAX_SUGGESTIONS = 1024;
	private static final int SCROLL_PIXELS = 20;

	private int[] mWordWidth = new int[MAX_SUGGESTIONS];
	private int[] mWordX = new int[MAX_SUGGESTIONS];

	private static final int X_GAP = 10;

	private static final List<String> EMPTY_LIST = new ArrayList<String>();

	private int mColorNormal;
	private int mColorRecommended;
	private int mColorOther;
	private int mVerticalPadding;
	private Paint mPaint;
	private boolean mScrolled;
	private int mTargetScrollX;

	private int mTotalWidth;

	private GestureDetector mGestureDetector;

	public static String[] KEYWORDS = { "Abs", "AddTo", "And", "Apart", "Append", "Apply", "ArcCos", "ArcSin", "ArcTan", "Arg",
			"Array", "AtomQ", "Binomial", "Blank", "Block", "Break", "CartesianProduct", "Cases", "Catalan", "CatalanNumber", "Ceiling",
			"CharacteristicPolynomial", "ChessboardDistance", "Chop", "Clear", "ClearAll", "Complement", "Complex", "ComplexInfinity",
			"ComposeList", "CompoundExpression", "Condition", "Conjugate", "ConstantArray", "Continue", "ContinuedFraction", "CoprimeQ",
			"Cos", "Cosh", "Cot", "Cross", "Csc", "Curl", "D", "Decrement", "Default", "Definition", "Denominator", "Depth", "Det",
			"DiagonalMatrix", "DigitQ", "Dimensions", "Divergence", "DivideBy", "Dot", "Drop", "Eigenvalues", "Eigenvectors", "Equal",
			"Erf", "EuclidianDistance", "EulerGamma", "EulerPhi", "EvenQ", "Exp", "Expand", "ExpandAll", "ExtendedGCD", "Extract",
			"Factor", "Factorial", "Factorial2", "FactorInteger", "FactorSquareFree", "FactorTerms", "Fibonacci", "FindRoot", "First",
			"Fit", "FixedPoint", "Floor", "Fold", "FoldList", "For", "FreeQ", "FromCharacterCode", "FromContinuedFraction", "FullForm",
			"Function", "Gamma", "GCD", "Glaisher", "GoldenRatio", "Greater", "GreaterEqual", "GroebnerBasis", "HarmonicNumber", "Head",
			"HilbertMatrix", "Hold", "Horner", "IdentityMatrix", "If", "Im", "Increment", "Infinity", "Inner", "IntegerPartitions",
			"IntegerQ", "Integrate", "Intersection", "Inverse", "JacobiMatrix", "JacobiSymbol", "JavaForm", "Join", "Khinchin",
			"KOrderlessPartitions", "KPartitions", "Last", "LCM", "LeafCount", "Length", "Less", "LessEqual", "LetterQ", "Level",
			"Limit", "LinearProgramming", "LinearSolve", "Log", "LowerCaseQ", "LUDecomposition", "ManhattanDistance", "Map", "MapAll",
			"MapThread", "MatchQ", "MatrixPower", "MatrixQ", "Max", "Mean", "Median", "MemberQ", "Min", "Mod", "Module", "MoebiusMu",
			"Most", "Multinomial", "Negative", "Nest", "NestList", "NextPrime",
			"NIntegrate",
			"NonCommutativeMultiply",
			"NonNegative",
			"Norm",
			"Not",
			"NRoots",
			"NumberQ",
			"Numerator",
			"NumericQ",
			"OddQ",
			"Or",
			"Order",
			"OrderedQ",
			"Out",
			"Outer",
			"Package",
			"ParametricPlot",
			"Part",
			"Partition",
			"Pattern",
			"Permutations",
			// "Plot",
			// "Plot3D",
			"Plus", "PolynomialExtendedGCD", "PolynomialGCD", "PolynomialLCM", "PolynomialQ", "PolynomialQuotient",
			"PolynomialQuotientRemainder", "PolynomialRemainder", "Position", "Positive", "PossibleZeroQ", "Power", "PowerExpand",
			"PowerMod", "PreDecrement", "PreIncrement", "Prepend", "PrimeQ", "PrimitiveRoots", "Print", "Product", "Quotient",
			"RandomInteger", "RandomReal", "Range", "Rational", "Rationalize", "Re", "ReplaceAll", "ReplaceRepeated", "Rest", "Return",
			"Reverse", "RootIntervals", "Roots", "RotateLeft", "RotateRight", "Rule", "RuleDelayed", "SameQ", "Sec", "Select", "Set",
			"SetAttributes", "SetDelayed", "Sign", "SignCmp", "Simplify", "Sin", "SingularValueDecomposition", "Sinh", "Solve", "Sort",
			"Sqrt", "SquaredEuclidianDistance", "SquareFreeQ", "StringDrop", "StringJoin", "StringLength", "StringTake", "Subsets",
			"SubtractFrom", "Sum", "SyntaxLength", "SyntaxQ", "Table", "Take", "Tan", "Tanh", "Taylor", "Thread", "Through", "Times",
			"TimesBy", "Timing", "ToCharacterCode", "Together", "ToString", "Total", "ToUnicode", "Tr", "Trace", "Transpose",
			"TrigReduce", "TrigToExp", "TrueQ", "Trunc", "Unequal", "Union", "UnsameQ", "UpperCaseQ", "ValueQ", "VandermondeMatrix",
			"Variables", "VectorQ", "While" };
	public static String[] CONSTANTS = { "D[,x]", "Degree", "E", "False", "I", "Integrate[,x]", "Null", "Pi", "True" };
	public static List<String> mPossibleCompletions = new ArrayList<String>();
	public static List<String> mPossibleCompletionsRsrvd = new ArrayList<String>();

	static {
		for (int i = 0; i < KEYWORDS.length; i++) {
			mPossibleCompletions.add(KEYWORDS[i]);
		}
		for (int i = 0; i < CONSTANTS.length; i++) {
			mPossibleCompletionsRsrvd.add(CONSTANTS[i]);
		}
		Collections.sort(mPossibleCompletions, new MyComparator());
		Collections.sort(mPossibleCompletionsRsrvd, new MyComparator());
	}

	public void init(Context context) {
		mService = (SymjaActivity) context;
		mSelectionHighlight = context.getResources().getDrawable(android.R.drawable.list_selector_background);
		mSelectionHighlight.setState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused,
				android.R.attr.state_window_focused, android.R.attr.state_pressed });

		Resources r = context.getResources();

		setBackgroundColor(r.getColor(R.color.candidate_background));

		mColorNormal = r.getColor(R.color.candidate_normal);
		mColorRecommended = r.getColor(R.color.candidate_recommended);
		mColorOther = r.getColor(R.color.candidate_other);
		mVerticalPadding = r.getDimensionPixelSize(R.dimen.candidate_vertical_padding);

		mPaint = new Paint();
		mPaint.setColor(mColorNormal);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(r.getDimensionPixelSize(R.dimen.candidate_font_height));
		mPaint.setStrokeWidth(0);

		mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				mScrolled = true;
				int sx = getScrollX();
				sx += distanceX;
				if (sx < 0) {
					sx = 0;
				}
				if (sx + getWidth() > mTotalWidth) {
					sx -= distanceX;
				}
				mTargetScrollX = sx;
				scrollTo(sx, getScrollY());
				invalidate();
				return true;
			}
		});
		setHorizontalFadingEdgeEnabled(true);
		setWillNotDraw(false);
		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);

	}

	public CandidateView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Construct a CandidateView for showing suggested words for completion.
	 * 
	 * @param context
	 * @param attrs
	 */
	public CandidateView(Context context, AttributeSet atts) {
		super(context, atts);
		init(context);
	}

	@Override
	public int computeHorizontalScrollRange() {
		return mTotalWidth;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = resolveSize(50, widthMeasureSpec);

		// Get the desired height of the icon menu view (last row of items does
		// not have a divider below)
		Rect padding = new Rect();
		mSelectionHighlight.getPadding(padding);
		final int desiredHeight = ((int) mPaint.getTextSize()) + mVerticalPadding + padding.top + padding.bottom;

		// Maximum possible width and desired height
		setMeasuredDimension(measuredWidth, resolveSize(desiredHeight, heightMeasureSpec));
	}

	/**
	 * If the canvas is null, then only touch calculations are performed to pick
	 * the target candidate.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if (canvas != null) {
			super.onDraw(canvas);
		}
		mTotalWidth = 0;
		if (mSuggestions == null)
			return;

		if (mBgPadding == null) {
			mBgPadding = new Rect(0, 0, 0, 0);
			if (getBackground() != null) {
				getBackground().getPadding(mBgPadding);
			}
		}
		int x = 0;
		final int count = mSuggestions.size();
		final int height = getHeight();
		final Rect bgPadding = mBgPadding;
		final Paint paint = mPaint;
		final int touchX = mTouchX;
		final int scrollX = getScrollX();
		final boolean scrolled = mScrolled;
		final boolean typedWordValid = mTypedWordValid;
		final int y = (int) (((height - mPaint.getTextSize()) / 2) - mPaint.ascent());

		for (int i = 0; i < count; i++) {
			String suggestion = mSuggestions.get(i);
			float textWidth = paint.measureText(suggestion);
			final int wordWidth = (int) textWidth + X_GAP * 2;

			mWordX[i] = x;
			mWordWidth[i] = wordWidth;
			paint.setColor(mColorNormal);
			if (touchX + scrollX >= x && touchX + scrollX < x + wordWidth && !scrolled) {
				if (canvas != null) {
					canvas.translate(x, 0);
					mSelectionHighlight.setBounds(0, bgPadding.top, wordWidth, height);
					mSelectionHighlight.draw(canvas);
					canvas.translate(-x, 0);
				}
				mSelectedIndex = i;
			}

			if (canvas != null) {
				if ((i == 1 && !typedWordValid) || (i == 0 && typedWordValid)) {
					paint.setFakeBoldText(true);
					paint.setColor(mColorRecommended);
				} else if (i != 0) {
					paint.setColor(mColorOther);
				}
				canvas.drawText(suggestion, x + X_GAP, y, paint);
				paint.setColor(mColorOther);
				canvas.drawLine(x + wordWidth + 0.5f, bgPadding.top, x + wordWidth + 0.5f, height + 1, paint);
				paint.setFakeBoldText(false);
			}
			x += wordWidth;
		}
		mTotalWidth = x;
		if (mTargetScrollX != getScrollX()) {
			scrollToTarget();
		}
	}

	private void scrollToTarget() {
		int sx = getScrollX();
		if (mTargetScrollX > sx) {
			sx += SCROLL_PIXELS;
			if (sx >= mTargetScrollX) {
				sx = mTargetScrollX;
				requestLayout();
			}
		} else {
			sx -= SCROLL_PIXELS;
			if (sx <= mTargetScrollX) {
				sx = mTargetScrollX;
				requestLayout();
			}
		}
		scrollTo(sx, getScrollY());
		invalidate();
	}

	public void updateSuggestions(String partialText, boolean completions, boolean typedWordValid) {
		Iterator completionIterator;
		String tempString;
		clear();
		if (partialText.length() > 0) {
			partialText = partialText.toLowerCase();
			completionIterator = mPossibleCompletionsRsrvd.iterator();
			while (completionIterator.hasNext()) {
				tempString = (String) completionIterator.next();
				if (tempString.toLowerCase().startsWith(partialText)) {
					mSuggestions.add(tempString);
					setVisibility(View.VISIBLE);
				}
			}
			completionIterator = mPossibleCompletions.iterator();
			while (completionIterator.hasNext()) {
				tempString = (String) completionIterator.next();
				if (tempString.toLowerCase().startsWith(partialText)) {
					// mSuggestions.add(tempString);
					mSuggestions.add(tempString + "[]");
					setVisibility(View.VISIBLE);
				}
			}
		}
		mTypedWordValid = typedWordValid;
		scrollTo(0, 0);
		mTargetScrollX = 0;
		// Compute the total width
		onDraw(null);
		invalidate();
		requestLayout();
	}

	public void clear() {
		mSuggestions = new ArrayList<String>();
		mTouchX = OUT_OF_BOUNDS;
		mSelectedIndex = -1;
		setVisibility(View.GONE);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {

		if (mGestureDetector.onTouchEvent(me)) {
			return true;
		}

		int action = me.getAction();
		int x = (int) me.getX();
		int y = (int) me.getY();
		mTouchX = x;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mScrolled = false;
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			if (y <= 0) {
				// Fling up!?
				if (mSelectedIndex >= 0) {
					mService.sendSuggestionText(mSuggestions.get(mSelectedIndex));
					mSelectedIndex = -1;
				}
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			if (!mScrolled) {
				if (mSelectedIndex >= 0) {
					mService.sendSuggestionText(mSuggestions.get(mSelectedIndex));
				}
			}
			mSelectedIndex = -1;
			removeHighlight();
			requestLayout();
			break;
		}
		return true;
	}

	/**
	 * For flick through from keyboard, call this method with the x coordinate of
	 * the flick gesture.
	 * 
	 * @param x
	 */
	public void takeSuggestionAt(float x) {
		mTouchX = (int) x;
		// To detect candidate
		onDraw(null);
		if (mSelectedIndex >= 0) {
			mService.sendSuggestionText(mSuggestions.get(mSelectedIndex));
		}
		invalidate();
	}

	private void removeHighlight() {
		mTouchX = OUT_OF_BOUNDS;
		invalidate();
	}

	public static class MyComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			if (o1.length() > o2.length()) {
				return 1;
			} else if (o1.length() < o2.length()) {
				return -1;
			} else {
				return o1.compareTo(o2);
			}
		}
	}

}
