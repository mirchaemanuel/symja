package org.matheclipse.android.util;

import android.util.Log;

public class Logger {
	Log log;
	String tag;
	boolean debugEnabled;

	public Logger(Class clazz) {
		tag = clazz.getName();
		debugEnabled = false;
	}

	public static Logger getLogger(Class clazz) {
		return new Logger(clazz);
	}

	public int debug(String msg) {
		return Log.d(tag, msg);
	}

	public int error(String msg) {
		return Log.e(tag, msg);
	}

	public int info(String msg) {
		return Log.i(tag, msg);
	}

	public int warn(String msg) {
		return Log.w(tag, msg);
	}
	
	public boolean isDebugEnabled() {
		return debugEnabled;

	}

	public boolean isInfoEnabled() {
		return false;

	}
}
