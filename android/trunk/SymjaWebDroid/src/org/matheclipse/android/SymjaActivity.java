/*
 * Copyright (c) 2011, Pieter Greyling (http://www.pietergreyling.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer.
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following disclaimer
 *   in the documentation and/or other materials provided with the
 *   distribution.
 * - Neither the name "Pieter Greyling" nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.matheclipse.android;

import static org.matheclipse.android.util.CommonAndroidCodeLibrary.makeToast;
import static org.matheclipse.android.util.CommonAndroidCodeLibrary.showOkAlertDialog;
import static org.matheclipse.android.util.CommonAndroidCodeLibrary.stringFromAssetFile;
import static org.matheclipse.android.util.CommonAndroidCodeLibrary.stringFromOutputStream;
import static org.matheclipse.android.util.CommonAndroidCodeLibrary.stringFromPrivateApplicationFile;
import static org.matheclipse.android.util.CommonAndroidCodeLibrary.stringToPrivateApplicationFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main Symja activity.
 * 
 * Derived from Pieter Greyling's <code>CocoaDroidActivity.java</code> from the
 * book &quot;Practical Android Projects&quot;
 */
public class SymjaActivity extends SymjaBase implements View.OnClickListener {
	protected static final String TAG = "SymjaActivity";

	protected TextView _txtOutput = null;
	protected Button _symEnter = null;
	protected Button _numEnter = null;
	protected Button _cmdLoadScratch = null;
	protected Button _cmdSaveScratch = null;
	protected Button _cmdClear = null;
	protected ListView _outputListView = null;

	OutputStringArrayAdapter _outputArrayAdapter = null;
	ArrayList<String> _outputArrayList = new ArrayList<String>();
	// The output stream that forms the communication
	// channel with the Symja interpreter
	protected ByteArrayOutputStream _outputStream = null;

	// The embedded Symja interpreter instance reference
	protected WebInterpreter _commandInterpreter = null;

	private ArrayList<String> _txtInputHistory = new ArrayList<String>();
	private int _txtInputIndex = -1;
	private String _toggleHistoryEntry;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Log.d(TAG, "onCreate(): ...");
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);

		initialize();
	}

	/**
	 * Sets up Activity user interface controls and resources.
	 */
	protected void initialize() {
		// set a custom title from the strings table
		setTitle(getString(R.string.app_desc));

		_txtOutput = (TextView) findViewById(R.id.txt_output);
		_txtOutput.setTextSize(TextSize.NORMAL);
		_txtOutput.setTypeface(Typeface.MONOSPACE);
		_txtOutput.setTextColor(Color.GREEN);
		_txtOutput.setBackgroundColor(Color.DKGRAY);

		// get a handle on the Sym command button and its event handler
		_symEnter = (Button) findViewById(R.id.cmd_sym);
		_symEnter.setOnClickListener(this);

		// get a handle on the Num command button and its event handler
		_numEnter = (Button) findViewById(R.id.cmd_num);
		_numEnter.setOnClickListener(this);

		// get a handle on the scratchpad buttons and event handling
		_cmdLoadScratch = (Button) findViewById(R.id.cmd_load_scratch);
		_cmdLoadScratch.setOnClickListener(this);
		_cmdSaveScratch = (Button) findViewById(R.id.cmd_save_scratch);
		_cmdSaveScratch.setOnClickListener(this);

		// button for clearing buffers
		_cmdClear = (Button) findViewById(R.id.cmd_clear);
		_cmdClear.setOnClickListener(this);

		// set up and get a handle on the output list view using an array
		// adapter
		_outputListView = (ListView) findViewById(R.id.lst_output);
		_outputArrayAdapter = new OutputStringArrayAdapter(this, _outputArrayList);
		_outputListView.setAdapter(_outputArrayAdapter);

		// keyboard = (KeyboardView) findViewById(R.id.calcKeyboard);
		// setUp(new EditText[]{_txtInput},keyboard);

		_outputListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				enableKeyboardVisibility();
				return false;
			}
		});

		// show the startup about banner
		// showAbout();

		// and let the interpreter show a little sample
		// String DSin = "D[Sin[x]^2,x]";
		// evalCodeStringSync(DSin);
		// _txtInput.setText(DSin);

		_txtInput.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
						if (_txtInputIndex == -1) {
							// do nothing
						} else if (_txtInputIndex == 0) {
							_txtInputIndex = _txtInputIndex - 1;
							_txtInput.setText(_toggleHistoryEntry);
							_txtInput.setSelection(_toggleHistoryEntry.length());
						} else {
							_txtInputIndex = _txtInputIndex - 1;
							_txtInput.setText(_txtInputHistory.get(_txtInputIndex));
							_txtInput.setSelection(_txtInputHistory.get(_txtInputIndex).length());
						}
					} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
						if ((_txtInputIndex + 1) < _txtInputHistory.size()) {
							if (_txtInputIndex == -1) {
								_toggleHistoryEntry = _txtInput.getText().toString();
								_txtInputIndex = 0;
								_txtInput.setText(_txtInputHistory.get(_txtInputIndex));
								_txtInput.setSelection(_txtInputHistory.get(_txtInputIndex).length());
							} else {
								_txtInputIndex = _txtInputIndex + 1;
								_txtInput.setText(_txtInputHistory.get(_txtInputIndex));
								_txtInput.setSelection(_txtInputHistory.get(_txtInputIndex).length());
							}
						}
						// } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
						// return true;
					}
				}
				return false;
			}

		});

		try {
			String fileName3 = "symjaHistory";

			FileInputStream input3 = openFileInput(fileName3);

			InputStreamReader input3reader = new InputStreamReader(input3);
			BufferedReader buffreader = new BufferedReader(input3reader);

			String line;

			_txtInputHistory.clear();
			while ((line = buffreader.readLine()) != null) {
				_txtInputHistory.add(line);
			}

			input3.close();
		} catch (java.io.IOException except) {
		}

	}

	/** Called when the activity is put into background. */
	@Override
	public void onPause() {
		try {
			super.onPause();

			String fileName3 = "symjaHistory";

			OutputStreamWriter out3 = new OutputStreamWriter(openFileOutput(fileName3, MODE_PRIVATE));

			for (int lineLoop = 0; lineLoop < _txtInputHistory.size(); lineLoop++) {
				out3.write(_txtInputHistory.get(lineLoop));
				out3.write("\n");
			}

			out3.close();

		} catch (java.io.IOException except) {
		}
	}

	/**
	 * Start up our script engine with a copyright notice.  
	 */
	protected void showAbout() {
		writeOutput("Symja - Computer Algebra System");
		_txtInput.setText("");
	}

	/**
	 * Write code evaluation output to the result text view and roll the array
	 * list with the stack of previous output results.
	 * 
	 * @param result
	 */
	protected void writeOutput(String result) {
		if (0 == result.length() || "".equals(result.trim())) {
			result = "-- null or empty result --";
		}
		Log.d(TAG, "writeOutput(): " + result);
		// always add previous result to index 0; it is the top of the list
		_outputArrayList.add(0, _txtOutput.getText().toString());
		_outputArrayAdapter.notifyDataSetChanged();
		_txtOutput.setText(result); // to the scratch output area
	}

	/**
	 * Clear the input/output buffers with Clear button.
	 */
	protected void clearBuffers() {
		// always add previous buffer to index 0; it is the top of the list
		_outputArrayList.add(0, _txtOutput.getText().toString());
		_outputArrayAdapter.notifyDataSetChanged();
		_txtInput.setText("");
		_txtOutput.setText("");
	}

	/**
	 * Centralized onClick listener for all views, particularly buttons.
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		Log.d(TAG, "onClick(): ".concat(v.toString()));
		String codeString = _txtInput.getText().toString();
		switch (v.getId()) {
		case R.id.cmd_sym:
			new EvalCodeStringAsyncTask(null).execute(codeString);
			Toast.makeText(SymjaActivity.this, "Symbolic evaluation requested\nfrom symjaweb.appspot.com", Toast.LENGTH_SHORT).show();
			break;
		case R.id.cmd_num:
			new EvalCodeStringAsyncTask("N").execute(codeString);
			Toast.makeText(SymjaActivity.this, "Numeric evaluation requested\nfrom symjaweb.appspot.com", Toast.LENGTH_SHORT).show();
			break;
		case R.id.cmd_load_scratch:
			loadScratchFiles();
			break;
		case R.id.cmd_save_scratch:
			saveScratchFiles();
			break;
		case R.id.cmd_clear:
			_txtInput.setText("");
			// clearBuffers();
			break;
		default:
			// do nothing
			break;
		}
	}

	/**
	 * Implement our application menu using an XML menu layout and the ADK
	 * MenuInflater.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// always first delegate to the base class in case of system menus
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.symjadroid_main_menu, menu);
		// true for a visible menu, false for an invisible one
		return true;
	}

	/**
	 * Respond to our application menu events.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int mnu_id = item.getItemId();
		switch (mnu_id) {
		case R.id.menu_itm_work_load:
			loadWorkFile();
			return true;
		case R.id.menu_itm_work_save:
			saveWorkFile();
			return true;
		case R.id.menu_itm_samples_load:
			loadSamplesAssetFile();
			return true;
		case R.id.menu_itm_app_about:
			showAbout();
			return true;
		case R.id.mainMenuPreferences:
			startActivity(new Intent(this, ShowSettingsActivity.class));
			return true;
		default: // not our items
			return super.onOptionsItemSelected(item); // pass item id up
		}
	}

	/**
	 * Loads the example snippets from the samples asset file. Note that we
	 * provide illustrative exception alerts which might or might not be a wise
	 * thing for end-user applications in general.
	 */
	private void loadSamplesAssetFile() {
		String buffer = "";
		try {
			buffer = stringFromAssetFile(this, getString(R.string.file_name_samples));
			_txtInput.setText(buffer);
		} catch (Throwable t) {
			Log.e(TAG, "loadSamplesAssetFile(): LOAD FAILED!", t);
			showOkAlertDialog(this, String.format("%s\n%s", getString(R.string.exception_on_samples_file_load), t.toString()),
					getString(R.string.title_samples_file_load));
		}
	}

	/**
	 * Reads work previously saved to the scratch files. Note that we provide
	 * illustrative exception alerts which might or might not be a wise thing for
	 * end-user applications in general.
	 */
	protected void loadScratchFiles() {
		String scratch_input = "";
		String scratch_output = "";
		try {
			scratch_input = stringFromPrivateApplicationFile(this, getString(R.string.file_name_scratch_input));
			scratch_output = stringFromPrivateApplicationFile(this, getString(R.string.file_name_scratch_output));
			_txtInput.setText(scratch_input);
			_txtOutput.setText(scratch_output);
		} catch (Throwable t) {
			Log.e(TAG, "loadScratchFiles(): LOAD FAILED!", t);
			showOkAlertDialog(this, String.format("%s\n%s", getString(R.string.exception_on_scratch_files_load), t.toString()),
					getString(R.string.title_scratch_files_load));
		}
	}

	/**
	 * Writes work to be saved to the scratch files. Note that we provide
	 * illustrative exception alerts which might or might not be a wise thing for
	 * end-user applications in general.
	 */
	protected void saveScratchFiles() {
		String scratch_input = _txtInput.getText().toString();
		String scratch_output = _txtOutput.getText().toString();
		try {
			stringToPrivateApplicationFile(this, getString(R.string.file_name_scratch_input), scratch_input);
			stringToPrivateApplicationFile(this, getString(R.string.file_name_scratch_output), scratch_output);
			makeToast(this, "Scratch Files saved");
		} catch (Throwable t) {
			Log.e(TAG, "saveScratchFiles(): SAVE FAILED!", t);
			showOkAlertDialog(this, String.format("%s\n%s", getString(R.string.exception_on_scratch_files_save), t.toString()),
					getString(R.string.title_scratch_files_save));
		}
	}

	/**
	 * Reads work previously saved to the work file. Note that we provide
	 * illustrative exception alerts which might or might not be a wise thing for
	 * end-user applications in general.
	 */
	protected void loadWorkFile() {
		String buffer = "";
		try {
			buffer = stringFromPrivateApplicationFile(this, getString(R.string.file_name_work));
			_txtInput.setText(buffer);
		} catch (Throwable t) {
			Log.e(TAG, "loadWorkFile(): LOAD FAILED!", t);
			showOkAlertDialog(this, String.format("%s\n%s", getString(R.string.exception_on_work_file_load), t.toString()),
					getString(R.string.title_work_file_load));
		}
	}

	/**
	 * Writes work to be saved to the work file. Note that we provide illustrative
	 * exception alerts which might or might not be a wise thing for end-user
	 * applications in general.
	 */
	protected void saveWorkFile() {
		String work = _txtInput.getText().toString();
		try {
			stringToPrivateApplicationFile(this, getString(R.string.file_name_work), work);
			makeToast(this, "Work File saved");
		} catch (Throwable t) {
			Log.e(TAG, "saveWorkFile(): SAVE FAILED!", t);
			showOkAlertDialog(this, String.format("%s\n%s", getString(R.string.exception_on_work_file_save), t.toString()),
					getString(R.string.title_work_file_save));
		}
	}

	/**
	 * Interpret and execute (evaluate) the given code fragment. This version of
	 * evalCodeString is reserved by convention for internally invoking non-user
	 * initiated interpreter code evaluation, i.e., from code. It is not invoked
	 * by the EvalCodeStringAsyncTask whereas the companion evalCodeString()
	 * method is.
	 * 
	 * @param codeString
	 * @return The result of the evaluation drawn off the interpreter output
	 *         stream.
	 */
	// protected String evalCodeStringSync(String codeString) {
	// Log.d(TAG, "evalCodeStringSync(): " + codeString);
	// // invoke eval bypassing use of an EvalCodeStringAsyncTask instance
	// String result = evalCodeString(codeString, null);
	// if (0 == result.length() || "".equals(result.trim())) {
	// result = "-- null or empty result --";
	// }
	// writeOutput(result);
	// // also place on input area since the user might not have entered this
	// // the method might have been initiated by code and not by the Enter
	// // button
	// _txtInput.setText(codeString);
	// return result;
	// }

	/**
	 * Interpret and execute (evaluate) the given code fragment. It is invoked by
	 * the EvalCodeStringAsyncTask.
	 * 
	 * @param codeString
	 * @return The result of the evaluation drawn off the interpreter output
	 *         stream.
	 */
	protected String evalCodeString(final String codeString, final String function) {
		Log.d(TAG, "evalCodeString(): " + codeString);

		_txtInputHistory.add(0, codeString);
		if (_txtInputHistory.size() == 100) {
			_txtInputHistory.remove(99);
		}
		_txtInputIndex = -1;

		String result = null;

		// set up and direct the input and output streams
		try {
			// _inputStream = inputStreamFromString(codeString);
			_outputStream = new ByteArrayOutputStream();

			// fire up the command interpreter to evaluate the source code
			// buffer
			_commandInterpreter = new WebInterpreter(codeString, _outputStream);
			try {
				_commandInterpreter.eval(function);
				// extract the resulting text output from the stream
				result = stringFromOutputStream(_outputStream); 
			} catch (Throwable t) {
				Log.e(TAG, String.format("evalCodeString(): UNSUPPORTED OPERATION!\n[\n%s\n]\n%s", codeString, t.toString()), t);
				result = ("UNSUPPORTED OPERATION!\n[\n" + codeString + "\n]\n" + t.toString());
			}
		} catch (Throwable t) {
			Log.e(TAG, String.format("evalCodeString(): UNSUPPORTED OPERATION!\n[\n%s\n]\n%s", codeString, t.toString()), t);
			result = ("UNSUPPORTED OPERATION!\n[\n" + codeString + "\n]\n" + t.toString());
		}

		return result;
	}

	/**
	 * Apply the percentile progress value passed into this method by performing
	 * some useful application operation.
	 * 
	 * @param progressPercent
	 */
	protected void setProgressPercent(Integer progressPercent) {
		Log.d(TAG, "setProgressPercent(): " + progressPercent.toString());
	}

	/**
	 * Handle program code interpretation as asynchronous operations. More on
	 * Android threading here:
	 * http://developer.android.com/resources/articles/painless-threading.html
	 * http://developer.android.com/reference/android/os/AsyncTask.html
	 * android.os.AsyncTask<Params, Progress, Result>
	 */
	protected class EvalCodeStringAsyncTask extends AsyncTask<String, Integer, String> {
		final String fFunction;

		public EvalCodeStringAsyncTask(String function) {
			fFunction = function;
		}

		protected String doInBackground(String... codeString) {
			String result = "";
			publishProgress((int) (10)); // just to demonstrate how
			Log.d(TAG, "doInBackground() [code]: \n" + codeString[0]);
			result = evalCodeString(codeString[0], fFunction);
			Log.d(TAG, "doInBackground() [eval]: \n" + result);
			publishProgress((int) (100)); // just to demonstrate how
			return result;
		}

		/**
		 * We leave this here for the sake of completeness. Progress update is not
		 * implemented.
		 * 
		 * @param progress
		 */
		@Override
		protected void onProgressUpdate(Integer... progress) {
			setProgressPercent(progress[0]);
		}

		/**
		 * Update the GUI output work result edit field.
		 * 
		 * @param result
		 */
		@Override
		protected void onPostExecute(String result) {
			writeOutput(result);
		}
	}

	/**
	 * Custom String ArrayAdapter class that allows us to manipulate the row
	 * colors etc.
	 */
	protected class OutputStringArrayAdapter extends ArrayAdapter<String> {
		OutputStringArrayAdapter(Context context, ArrayList<String> stringArrayList) {
			super(context, android.R.layout.simple_list_item_1, stringArrayList);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TextView txt = new TextView(this.getContext());
			txt.setTextColor(Color.GREEN);
			txt.setTextSize(TextSize.SMALL);
			txt.setText(this.getItem(position));
			return txt;
		}
	}

	protected class TextSize {
		protected static final int SMALL = 14;
		protected static final int NORMAL = 16;
		protected static final int LARGE = 18;
	}

}
