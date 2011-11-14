package org.matheclipse.android;

import org.matheclipse.android.SymjaActivity.TextSize;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SymjaBase extends Activity {
	protected EditTextExtend _txtInput = null;
	private CandidateView _mCandidateView;
	public KeyboardViewExtend _myKeyboardView;
	private LinearLayout _mainLayout;

	int _suggestionCursorPos = 0;
	boolean _suggestionTaken = false;
	boolean _backUpOne = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get a handle on and configure the input and text fields
		_txtInput = (EditTextExtend) findViewById(R.id.txt_input);
		_txtInput.setTextSize(TextSize.NORMAL);
		_txtInput.setTypeface(Typeface.MONOSPACE);

		_myKeyboardView = (KeyboardViewExtend) findViewById(R.id.keyboard);
		_mCandidateView = (CandidateView) findViewById(R.id.candidate);
		_mainLayout = (LinearLayout) findViewById(R.id.wrapView);

		_txtInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				enableKeyboardVisibility();
			}
		});

		_txtInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (_suggestionTaken == false) {
					updateSuggestions();
				}
				if (_suggestionTaken == true) {
					_txtInput.setSelection(_suggestionCursorPos, _suggestionCursorPos);
				}
				_suggestionTaken = false;
				if (_backUpOne == true) {
					int start = _txtInput.getSelectionStart();
					if (start > 0) {
						_txtInput.setSelection(start - 1, start - 1);
					}
				}
				_backUpOne = false;
				_txtInput._prevPos = _txtInput.getSelectionStart();
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Keyboard tempKeyboard;

		int visibility = _myKeyboardView.getVisibility();
		tempKeyboard = _myKeyboardView.getKeyboard();

		if (_myKeyboardView != null)
			_mainLayout.removeView(_myKeyboardView);
		_myKeyboardView = new KeyboardViewExtend(this);
		_myKeyboardView.setId(R.id.keyboard);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		_mainLayout.addView(_myKeyboardView, lp);
		_myKeyboardView.setKeyboard(tempKeyboard);
		_myKeyboardView.setVisibility(visibility);
		_myKeyboardView.myOnConfigurationChanged(newConfig);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			int visibility = _myKeyboardView.getVisibility();
			if (visibility == View.VISIBLE) {
				_myKeyboardView.setVisibility(View.GONE);
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		int visibility = _myKeyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			_myKeyboardView.setVisibility(View.GONE);
		} else {
			finish();
			System.exit(0);
		}
		return;
	}

	public void enableKeyboardVisibility() {
		int visibility = _myKeyboardView.getVisibility();
		switch (visibility) {
		case View.GONE:
		case View.INVISIBLE:
			// _myKeyboardView.makeKeyboardView();
			_myKeyboardView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void handleBackspace() {
		int start = _txtInput.getSelectionStart();
		int end = _txtInput.getSelectionEnd();
		String textToInsert = "";
		if (start != end) {
			_txtInput.getText().replace(Math.min(start, end), Math.max(start, end), textToInsert, 0, textToInsert.length());
		} else if (start != 0) {
			_txtInput.getText().replace(start - 1, start, textToInsert, 0, textToInsert.length());
		}
	}

	public void handleEnter() {
	}

	public void sendText(String textToInsert) {
		int start = _txtInput.getSelectionStart();
		int end = _txtInput.getSelectionEnd();
		_txtInput.getText().replace(Math.min(start, end), Math.max(start, end), textToInsert, 0, textToInsert.length());
	}

	void swipeUp() {
		_txtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
	}

	void swipeDown() {
		_txtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
	}

	void swipeLeft() {
		_txtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
	}

	void swipeRight() {
		_txtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
	}

	public void sendSuggestionText(String textToInsert) {
		int start = _txtInput.getSelectionStart();
		_mCandidateView.clear();

		Character tempChar;
		int reverse;
		int forward;
		// scan forwards and backwards and find the full word, then update
		// suggestions
		for (reverse = start - 1; reverse >= 0; reverse--) {
			tempChar = _txtInput.getText().toString().charAt(reverse);
			if (Character.isLetter(tempChar) || Character.isDigit(tempChar) || (tempChar == '_')) {
				continue;
			} else {
				reverse++;
				break;
			}
		}
		if (reverse < 0) {
			reverse = 0;
		}
		for (forward = start; forward < _txtInput.getText().toString().length(); forward++) {
			tempChar = _txtInput.getText().toString().charAt(forward);
			if (Character.isLetter(tempChar) || Character.isDigit(tempChar) || (tempChar == '_')) {
				continue;
			} else {
				break;
			}
		}
		if (forward > _txtInput.getText().toString().length()) {
			forward = _txtInput.getText().toString().length() - 1;
		}
		if (forward < 0) {
			forward = 0;
		}
		if (textToInsert.endsWith("()") || textToInsert.endsWith("[]")) {
			_suggestionCursorPos = reverse + textToInsert.length() - 1;
		} else if (textToInsert.endsWith("[,x]")) {
			_suggestionCursorPos = reverse + textToInsert.length() - 3;
		} else {
			_suggestionCursorPos = reverse + textToInsert.length();
		}
		_suggestionTaken = true;
		_txtInput.getText().replace(reverse, forward, textToInsert, 0, textToInsert.length());

	}

	public void updateSuggestions() {
		int start = _txtInput.getSelectionStart();
		int end = _txtInput.getSelectionEnd();

		if (start != end) {
			_mCandidateView.clear();
			return;
		}

		Character tempChar;
		int reverse;
		int forward;
		// scan forwards and backwards and find the full word, then update
		// suggestions
		for (reverse = start - 1; reverse >= 0; reverse--) {
			tempChar = _txtInput.getText().toString().charAt(reverse);
			if (Character.isLetter(tempChar) || Character.isDigit(tempChar) || (tempChar == '_')) {
				continue;
			} else {
				reverse++;
				break;
			}
		}
		if (reverse < 0) {
			reverse = 0;
		}
		for (forward = start; forward < _txtInput.getText().toString().length(); forward++) {
			tempChar = _txtInput.getText().toString().charAt(forward);
			if (Character.isLetter(tempChar) || Character.isDigit(tempChar) || (tempChar == '_')) {
				continue;
			} else {
				break;
			}
		}
		if (forward > _txtInput.getText().toString().length()) {
			forward = _txtInput.getText().toString().length() - 1;
		}
		if (forward < 0) {
			forward = 0;
		}
		_mCandidateView.updateSuggestions(_txtInput.getText().toString().substring(reverse, forward), true, true);
	}

}
