package com.cloud.ReksaDanaSaham.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;

public class GeneralFunctionUtils {
	private static final String TAG = "ReksaDana GeneralFunctionUtils";

	public static void removeTitleAndLogoActionBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = activity.getActionBar();
			actionBar.setDisplayShowTitleEnabled(false);// title
			actionBar.setDisplayShowHomeEnabled(false);// logo
		}

	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

//	public static boolean hasJellyBean() {
//		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
//	}

	public static void setDisplayHomeAsUpEnabled(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = activity.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);// back
		}

	}

	

}
