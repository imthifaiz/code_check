package com.track.util;

public class ThrowableUtil {
	public static String getMessage(Throwable t) {
		return t.getMessage() == null ? t.toString() : t.getMessage();
	}
}
