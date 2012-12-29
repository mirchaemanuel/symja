package org.matheclipse.android.web;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextExtend extends EditText {

	private Context _parent = null;
	public int _prevPos = 0;
	public boolean _isTextEditorReturn = true;

	public EditTextExtend(Context context, AttributeSet atts) {
		super(context, atts);
		_parent = context;
	}

	public EditTextExtend(Context context) {
		super(context);
		_parent = context;
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		if (_parent != null) {
			if (_prevPos != getSelectionStart()) {
				((SymjaActivity) _parent).updateSuggestions();
				_prevPos = getSelectionStart();
			}
		}
	}

	@Override
	public boolean onCheckIsTextEditor() {
		return _isTextEditorReturn;
	}

//	@Override
//	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//		// TODO Auto-generated method stub
//		super.onFocusChanged(focused, direction, previouslyFocusedRect);
//	}

}
