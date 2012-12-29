package org.matheclipse.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextExtend extends EditText {

	private SymjaActivity _parent = null;
	public int _prevPos = 0;
	public boolean _isTextEditorReturn = true;

	public EditTextExtend(Context context, AttributeSet atts) {
		super(context, atts);
		_parent = (SymjaActivity) context;  
	}

	public EditTextExtend(Context context) {
		super(context);
		_parent = (SymjaActivity) context;
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		if (_parent != null) {
			if (_prevPos != getSelectionStart()) {
				_parent.updateSuggestions();
				_prevPos = getSelectionStart();
			}
		}
	}

	@Override
	public boolean onCheckIsTextEditor() {
		return _isTextEditorReturn;
	}

}
