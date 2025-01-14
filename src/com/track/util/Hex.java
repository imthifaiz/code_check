package com.track.util;

public class Hex {
	private Hex() {
	} // static methods only

	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toString(byte[] ba, int offset, int length) {
		char[] buf = new char[length * 2];
		int j = 0;
		int k;

		for (int i = offset; i < offset + length; i++) {
			k = ba[i];
			buf[j++] = hexDigits[(k >>> 4) & 0x0F];
			buf[j++] = hexDigits[k & 0x0F];
		}
		return new String(buf);
	}

	public static String toString(byte[] ba) {
		return toString(ba, 0, ba.length);
	}

	public static String toString(byte b) {
		byte[] bArr = { b };

		return toString(bArr);
	}

	public static String toString(int[] ia, int offset, int length) {
		char[] buf = new char[length * 8];
		int j = 0;
		int k;

		for (int i = offset; i < offset + length; i++) {
			k = ia[i];
			buf[j++] = hexDigits[(k >>> 28) & 0x0F];
			buf[j++] = hexDigits[(k >>> 24) & 0x0F];
			buf[j++] = hexDigits[(k >>> 20) & 0x0F];
			buf[j++] = hexDigits[(k >>> 16) & 0x0F];
			buf[j++] = hexDigits[(k >>> 12) & 0x0F];
			buf[j++] = hexDigits[(k >>> 8) & 0x0F];
			buf[j++] = hexDigits[(k >>> 4) & 0x0F];
			buf[j++] = hexDigits[k & 0x0F];
		}
		return new String(buf);
	}

	public static String toString(int[] ia) {
		return toString(ia, 0, ia.length);
	}

	public static String toString(int i) {
		int[] iArr = { i };
		return toString(iArr);
	}

	public static String toReversedString(byte[] b, int offset, int length) {
		char[] buf = new char[length * 2];
		int j = 0;

		for (int i = offset + length - 1; i >= offset; i--) {
			buf[j++] = hexDigits[(b[i] >>> 4) & 0x0F];
			buf[j++] = hexDigits[b[i] & 0x0F];
		}
		return new String(buf);
	}

	public static String toReversedString(byte[] b) {
		return toReversedString(b, 0, b.length);
	}

	public static byte[] fromString(String hex) {
		int len = hex.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int i = 0, j = 0;
		if ((len % 2) == 1)
			buf[j++] = (byte) fromDigit(hex.charAt(i++));

		while (i < len) {
			buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4) | fromDigit(hex
					.charAt(i++)));
		}
		return buf;
	}

	public static byte[] fromReversedString(String hex) {
		int len = hex.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int j = 0;
		if ((len % 2) == 1)
			throw new IllegalArgumentException(
					"string must have an even number of digits");

		while (len > 0) {
			buf[j++] = (byte) (fromDigit(hex.charAt(--len)) | (fromDigit(hex
					.charAt(--len)) << 4));
		}
		return buf;
	}

	public static char toDigit(int n) {
		try {
			return hexDigits[n];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException(n
					+ " is out of range for a hex digit");
		}
	}

	public static int fromDigit(char ch) {
		if (ch >= '0' && ch <= '9')
			return ch - '0';
		if (ch >= 'A' && ch <= 'F')
			return ch - 'A' + 10;
		if (ch >= 'a' && ch <= 'f')
			return ch - 'a' + 10;

		throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
	}

	public static String byteToString(int n) {
		char[] buf = { hexDigits[(n >>> 4) & 0x0F], hexDigits[n & 0x0F] };
		return new String(buf);
	}

	public static String shortToString(int n) {
		char[] buf = { hexDigits[(n >>> 12) & 0x0F],
				hexDigits[(n >>> 8) & 0x0F], hexDigits[(n >>> 4) & 0x0F],
				hexDigits[n & 0x0F] };
		return new String(buf);
	}

	public static String intToString(int n) {
		char[] buf = new char[8];

		for (int i = 7; i >= 0; i--) {
			buf[i] = hexDigits[n & 0x0F];
			n >>>= 4;
		}
		return new String(buf);
	}

	public static String longToString(long n) {
		char[] buf = new char[16];

		for (int i = 15; i >= 0; i--) {
			buf[i] = hexDigits[(int) n & 0x0F];
			n >>>= 4;
		}
		return new String(buf);
	}

	public static String dumpString(byte[] data, int offset, int length,
			String m) {
		if (data == null)
			return m + "null\n";

		StringBuffer sb = new StringBuffer(length * 3);
		if (length > 32)
			sb.append(m).append("Hexadecimal dump of ").append(length).append(
					" bytes...\n");

		// each line will list 32 bytes in 4 groups of 8 each
		int end = offset + length;
		String s;
		int l = Integer.toString(length).length();
		if (l < 4)
			l = 4;
		for (; offset < end; offset += 32) {
			if (length > 32) {
				s = "         " + offset;
				sb.append(m).append(s.substring(s.length() - l)).append(": ");
			}
			int i = 0;
			for (; i < 32 && offset + i + 7 < end; i += 8)
				sb.append(toString(data, offset + i, 8)).append(' ');

			if (i < 32) {
				for (; i < 32 && offset + i < end; i++)
					sb.append(byteToString(data[offset + i]));
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public static String dumpString(byte[] data) {
		return (data == null) ? "null\n" : dumpString(data, 0, data.length, "");
	}

	public static String dumpString(byte[] data, String m) {
		return (data == null) ? "null\n" : dumpString(data, 0, data.length, m);
	}

	public static String dumpString(byte[] data, int offset, int length) {
		return dumpString(data, offset, length, "");
	}

	public static String dumpString(int[] data, int offset, int length, String m) {
		if (data == null)
			return m + "null\n";

		StringBuffer sb = new StringBuffer(length * 3);
		if (length > 8)
			sb.append(m).append("Hexadecimal dump of ").append(length).append(
					" integers...\n");

		// each line will list 32 bytes in 8 groups of 4 each (1 int)
		int end = offset + length;
		String s;
		int x = Integer.toString(length).length();
		if (x < 8)
			x = 8;
		for (; offset < end;) {
			if (length > 8) {
				s = "         " + offset;
				sb.append(m).append(s.substring(s.length() - x)).append(": ");
			}
			for (int i = 0; i < 8 && offset < end; i++)
				sb.append(intToString(data[offset++])).append(' ');
			sb.append('\n');
		}
		return sb.toString();
	}

	public static String dumpString(int[] data) {
		return dumpString(data, 0, data.length, "");
	}

	public static String dumpString(int[] data, String m) {
		return dumpString(data, 0, data.length, m);
	}

	public static String dumpString(int[] data, int offset, int length) {
		return dumpString(data, offset, length, "");
	}

	public static String ReplC2(String inText) {
		Global.trace("\n Start Text :" + inText);
		String rStr = "C2";
		int count = 2;
		int index = inText.indexOf(rStr);
		String startStr = "";
		String endStr = "";
		if (index >= 0) {
			if ((index + count) < inText.length()) {
				endStr = inText.substring(index + count);
				endStr = ReplC2(endStr);
			}
			if (index > 0)
				startStr = inText.substring(0, index);
			inText = startStr + endStr;
		}
		return (inText);
	}

}