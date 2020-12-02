package com.bigun.wifioscilloscope.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class ExceptionUtil {
	public static void printException(Exception e, String tag) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		Log.e(tag, sw.toString());
	}
}
