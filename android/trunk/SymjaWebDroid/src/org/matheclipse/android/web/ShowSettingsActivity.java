package org.matheclipse.android.web;

import org.matheclipse.android.web.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
public class ShowSettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

	}

}

