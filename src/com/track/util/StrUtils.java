package com.track.util;

import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import com.track.gates.DbBean;

public class StrUtils {
	String alphabet = "";
	String Quo = "\"";
	private static String LANGUAGE = "en";
	private static String COUNTRY = "SG";

	public StrUtils() {
	}

	public static Vector parseString(String a, String delim) {
		Vector resString = new Vector();
		StringTokenizer tokens = new StringTokenizer(a, delim);
//		int m = tokens.countTokens();
		while (tokens.hasMoreTokens()) {
			resString.addElement(tokens.nextToken());
		}
		return resString;
	}

	public static float Round(float Rval, int Rpl) {
		float p = (float) Math.pow(10, Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);
		return (float) tmp / p;
	}

	public static double RoundDB(double Rval, int Rpl) {
		double p = (double) Math.pow(10, Rpl);
		Rval = Rval * p;
		double tmp = Math.round(Rval);
		return (double) tmp / p;
	}

	public static Vector parseStringGetAll(String inParseString, String inDelim) {
		Vector result = null;
		if (inParseString != null) {
			result = new Vector();
			StringTokenizer tokenizer = new StringTokenizer(inParseString, inDelim, true);
			String token = "";
			String last = inDelim;
			while (tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken();
				if (token.compareTo(inDelim) != 0) {
					result.add(token);
				} else {
					if (last.compareTo(inDelim) == 0) {
						result.add("");
					}
				}
				last = token;
			}
		}
		return result;
	}

	public static String TrunkateDecimal(String strInput) {

		try {
			return strInput.substring(0, strInput.indexOf("."));
		} catch (Exception e) {
			System.out.println("Exceiption : StrUtils :: TrunkateDecimal() " + e.getMessage());
			return "0";
		}

	}

	public static String TrunkateDecimalForImportData(String strInput) {
		String result = "";

		try {
			if (strInput.contains(".")) {
				result = strInput.substring(0, strInput.indexOf("."));
			} else {
				result = strInput;
			}
		} catch (Exception e) {
			System.out.println("Exceiption : StrUtils :: TrunkateDecimal() " + e.getMessage());
			return "0";
		}
		return result;
	}

	public static String replaceSingleQuoteBydoubleQuote(String str) {
		if (fString(str).length() > 0) {
			if (str.indexOf("'") != -1) {
				str = str.replaceAll("'", "''");

			}
		}
		return str;
	}

	public static String PO_NUM(String inputStr) {
		String str = "";
		try {
			str = (inputStr.equalsIgnoreCase("") || (inputStr.equalsIgnoreCase("NULL"))) ? "" : inputStr.trim();
		} catch (Exception e) {
		}
		return str;
	}

	public static String fString(String inputStr) {
		String str = "";
		try {
			str = (inputStr.equalsIgnoreCase("") || (inputStr.equalsIgnoreCase("NULL"))) ? "" : inputStr.trim();
		} catch (Exception e) {
		}
		return str;
	}

	public static String getSQLDate(String inputStr, String del) {
		String str = "";
		try {
			String[] s = inputStr.split(del, 3);
			str = s[2].trim() + "-" + s[1].trim() + "-" + s[0].trim();
		} catch (Exception e) {
		}
		return str.trim();
	}

	public static String getUserDate(String inputStr, String del, String show) {
		String str = "";
		try {
			String[] s = inputStr.trim().split(del, 3);
			str = s[2].trim() + show.trim() + s[1].trim() + show.trim() + s[0].trim();
		} catch (Exception e) {
		}
		return str.trim();
	}

	public static String getSQLDate(String nd) {
		String fDate = "";
		try {
			fDate = nd.substring(6, 10) + nd.substring(3, 5) + nd.substring(0, 2);
		} catch (Exception e) {
		}
		return fDate;
	}

	public static String arrayToString(String[] a, String separator) {
		if (a == null || separator == null) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		if (a.length > 0) {
			result.append(a[0]);
			for (int i = 1; i < a.length; i++) {
				result.append(separator);
				result.append(a[i]);
			}
		}
		return result.toString();
	}

	public static void reverse(String[] b) {
		int left = 0; // index of leftmost element
		int right = b.length - 1; // index of rightmost element

		while (left < right) {
			// exchange the left and right elements
			String temp = b[left];
			b[left] = b[right];
			b[right] = temp;
			// move the bounds toward the center
			left++;
			right--;
		}
	}

	/**
	 * @mthod : changeFloatValueWithSpcifiedDecimalPoint(float a, int decimal)
	 * @decription : chage the given float value into specified decimal points.
	 * @param a
	 * @param decimal
	 * @return
	 */
	public static float formatNumber(float a, int decimal) {
		float val = 0;
		try {
			int actualDec = (a + "").indexOf(".");
			if (actualDec == -1) {
				actualDec = 0;
			} else {
				actualDec = actualDec + decimal + 1;
			}
			if ((a + "").length() > actualDec) {
				val = new Float((a + "").substring(0, actualDec)).floatValue();
			} else {
				val = new Float(a).floatValue();
			}
			return val;
		} catch (Exception e) {
			return val;
		}

	}

	/**
	 * @mthod : changeFloatValueWithSpcifiedDecimalPoint(float a, int decimal)
	 * @decription : chage the given float value into specified decimal points.
	 * @param a
	 * @param decimal
	 * @return
	 */
	public static BigDecimal formatNumber(BigDecimal a, int decimal, boolean hideZeros) {
		BigDecimal val = new BigDecimal(0);
		String valStr = "0";
		int total_length = 0;
		int decimal_position = 0;
		int value_after_decimal = 0;
//		int value_before_decimal = 0;

		try {
			valStr = a + "";
			total_length = valStr.length();
			decimal_position = (valStr).lastIndexOf(".");
			if (decimal_position >= 1) {
//				if (decimal_position > 1) {
//					value_before_decimal = Integer.parseInt(valStr.substring(0, decimal_position - 1));
//				} else {
//					value_before_decimal = Integer.parseInt(valStr.substring(0, decimal_position));
//				}
				if (decimal_position > 0)
					value_after_decimal = Integer.parseInt(valStr.substring(decimal_position + 1, total_length));
				if (value_after_decimal > 0 & value_after_decimal < 10) {
					valStr = valStr.substring(0, decimal_position) + "." + value_after_decimal;
				} else if (value_after_decimal > 10) {
					valStr = valStr.substring(0, decimal_position + 3);
				} else if (value_after_decimal == 0) {
					if (hideZeros) {
						valStr = valStr.substring(0, decimal_position);
					} else {
						valStr = valStr.substring(0, decimal_position) + ".00";
					}
				}
			}
			val = new BigDecimal(valStr);
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return val;

		}
	}

	public static double formatNumber2Double(double a, int decimal) {
		double val = 0;
		try {
			int actualDec = (a + "").indexOf(".");
			if (actualDec == -1) {
				actualDec = 0;
			} else {
				actualDec = actualDec + decimal + 1;
			}
			if ((a + "").length() > actualDec) {
				val = new Float((a + "").substring(0, actualDec)).doubleValue();
			} else {
				val = new Float(a).doubleValue();
			}
			return val;
		} catch (Exception e) {
			return val;
		}

	}

	public static String getHtmlFormatNumber(BigDecimal number, int dispDec) {
		String qty_org = "";
		String qty_str = number.toString();
		NumberFormat formatter = new DecimalFormat("###0.000#");
		try {
			qty_str = formatter.format(number.doubleValue());
			if (Integer.parseInt(qty_str.substring(qty_str.lastIndexOf(".") + 1, qty_str.length())) > 0) {
				qty_org = qty_str.substring(0, qty_str.lastIndexOf(".") + 4);
			} else {
				qty_org = qty_str.substring(0, qty_str.lastIndexOf("."));
			}
		} catch (Exception e) {
			System.out.println("Exception getHtmlFormatNumber :: " + e.toString());
		}
		return qty_org;
	}

	public static String formatNumberString(String str) {
		String retStr = "";
		try {
			if (!str.equalsIgnoreCase("")) {
				BigDecimal strVal = new BigDecimal(str);
				retStr = formatNumber(strVal, 2, true).toString();
			}
		} catch (Exception e) {
			retStr = "0";
		}
		return retStr;
	}

	public static String formatHTML(String field) {
		String retStr = "";
		try {
			if (field.trim().length() > 0) {
				retStr = field.replaceAll("\"", "&quot;");
				retStr += "&nbsp";
			}
		} catch (Exception e) {
		}
		return retStr;
	}

	public static String formatHTMLTag(String field) {
		String retStr = "";
		try {
			if (field.trim().length() > 0) {
				retStr = field.replaceAll("\"", "&quot;");
			}
		} catch (Exception e) {
		}
		return retStr;
	}

	public static String removeQuotes(String field) {
		String retStr = "";
		try {
			if (field.trim().length() > 0) {
				retStr = field.replaceAll("\"", "");
				retStr = retStr.replaceAll("'", "");
			}
		} catch (Exception e) {
		}
		return retStr;
	}

	public static String getStringSeparatedByCommasFromList(List l) {
		String strMail = "";
		try {
			for (int jj = 0; jj < l.size(); jj++) {
				String str = ((String) l.get(jj)).trim();
				if (jj == l.size() - 1)
					strMail += str;
				else {
					strMail += str + ",";
				}

			}
			return strMail;
		} catch (Exception e) {
			return strMail;
		}
	}

	public static String getStringSeparatedByQuots(String wono) {
		String[] arr = wono.split(",");
		String str = "";
		for (int i = 0; i < arr.length; i++) {
			String w = (arr[i]).trim();
			if (arr.length - 1 == i) {
				str += "'" + w + "'";
			} else {
				str += "'" + w + "',";
			}
		}
		return str;
	}

	public static String getStringSeparatedByQuotsFromArray(String[] strArr) {
		String str = "";
		try {
			for (int i = 0; i < strArr.length; i++) {
				String s = strArr[i];
				if (i == strArr.length - 1)
					str += "'" + s + "'";
				else
					str += "'" + s + "',";
			}

		} catch (Exception e) {
		}
		return str;
	}

	public static String getStringSeparatedByQuotsFromList(List list) {
		String str = "";
		try {
			for (int i = 0; i < list.size(); i++) {
				String s = (String) list.get(i);
				if (i == list.size() - 1)
					str += "'" + s + "'";
				else
					str += "'" + s + "',";
			}

		} catch (Exception e) {
		}
		return str;
	}

	public static String replaceCharacters2Send(String str) {
		if (fString(str).length() > 0) {
			if (str.indexOf("#") != -1)
				str = str.replaceAll("#", "HASH");

			if (str.indexOf("&") != -1)
				str = str.replaceAll("&", "AMPRASAND");

			if (str.indexOf("!") != -1)
				str = str.replaceAll("!", "ESCLAMATION");

			if (str.indexOf("\\@") != -1)
				str = str.replaceAll("\\@", "AT_THE_RATE");

			if (str.indexOf("%") != -1)
				str = str.replaceAll("%", "PERCENTAGE");

			if (str.indexOf("_") != -1)
				str = str.replaceAll("_", "UNDERSCORE");

			if (str.indexOf(">") != -1)
				str = str.replaceAll(">", "GREATERTHAN");

			if (str.indexOf("<") != -1)
				str = str.replaceAll("<", "LESSTHAN");

			if (str.indexOf("~") != -1)
				str = str.replaceAll("~", "TILDE");

			if (str.indexOf("`") != -1)
				str = str.replaceAll("`", "GRAVE");

			if (str.indexOf("\\$") != -1)
				str = str.replaceAll("\\$", "DOLLAR");

			if (str.indexOf("\\^") != -1)
				str = str.replaceAll("\\^", "CARET");

			if (str.indexOf("\\*") != -1)
				str = str.replaceAll("\\*", "ASTERSTIK");

			if (str.indexOf("\\(") != -1)
				str = str.replaceAll("\\(", "OPENPAR");

			if (str.indexOf("\\)") != -1)
				str = str.replaceAll("\\)", "CLOSETHESIS");

			if (str.indexOf("\\{") != -1)
				str = str.replaceAll("\\{", "STARTCURLY");

			if (str.indexOf("\\}") != -1)
				str = str.replaceAll("\\}", "ENDBRACKETS");

			/*
			 * if (str.indexOf("[") != -1) str = str.replaceAll("[", "OPENSQUAREBRACKETS");
			 */

			if (str.indexOf("\\]") != -1)
				str = str.replaceAll("\\]", "SQUARE");

			if (str.indexOf("\\|") != -1)
				str = str.replaceAll("\\|", "PIPE");

			if (str.indexOf("\\\\") != -1)
				str = str.replaceAll("\\\\", "BACKSLASH");

			if (str.indexOf(":") != -1)
				str = str.replaceAll(":", "COLON");

			if (str.indexOf("\\;") != -1)
				str = str.replaceAll("\\;", "SEMI");

			if (str.indexOf("\"") != -1)
				str = str.replaceAll("\"", "DOUBLEQOUTS");

			if (str.indexOf(",") != -1)
				str = str.replaceAll(",", "COMMA");

			if (str.indexOf("\\.") != -1)
				str = str.replaceAll("\\.", "DOT");

			if (str.indexOf("\\?") != -1)
				str = str.replaceAll("\\?", "QUESTIONMARK");

			if (str.indexOf("/") != -1)
				str = str.replaceAll("/", "FORWARDSLASH");

			if (str.indexOf("\\+") != -1)
				str = str.replaceAll("\\+", "PLUSH");

		}
		return str;
	}

	// Validate Alpha-Numeric and special chars
	public static boolean isValidCharacter(String str) {
		boolean flag = false;

		if (fString(str).length() > 0) {
			str = str.replaceAll("[a-zA-Z0-9\\#\\'\\&\\!\\@\\$\\%\\-\\,\\_]", "");
			if (str.length() == 0) {
				flag = true;
			}

		}
		return flag;
	}

	public static String isValidAlphaNumeric(String str) {
//		boolean flag = false;
		String specialChars = "";
		if (fString(str).length() > 0) {
			// str = str.replaceAll("[a-zA-Z0-9\\-\\/._]",""); by Bruhan on 12 jan 15 cannot
			// accept '/' in product ID
			str = str.replaceAll("[a-zA-Z0-9\\-._/]", "");
			/*
			 * if(str.length()==0) { flag=true; }
			 */
			specialChars = str;
			if (specialChars.indexOf(" ") != -1) {
				specialChars = specialChars + " (Space)";
			}

		}
		return specialChars;
	}

	public static String isValidAlphaNumericWithSpace(String str) {
//		boolean flag = false;
		String specialChars = "";
		if (fString(str).length() > 0) {
			// str = str.replaceAll("[a-zA-Z0-9\\-\\/._]",""); by Bruhan on 12 jan 15 cannot
			// accept '/' in product ID
			str = str.replaceAll("[a-zA-Z0-9\\-._/]", "");
			str = str.replaceAll(" ", "");
			/*
			 * if(str.length()==0) { flag=true; }
			 */
			specialChars = str;
		}
		return specialChars;
	}

	public static String replaceCharacters2Recv(String str) {

		if (fString(str).length() > 0) {
			if (str.indexOf("HASH") != -1)
				str = str.replaceAll("HASH", "#");
			if (str.indexOf("HYPEN") != -1)
				str = str.replaceAll("HYPEN", "-");
			if (str.indexOf("UNDERSCORE") != -1)
				str = str.replaceAll("UNDERSCORE", "_");
			if (str.indexOf("SINGLEQOUTS") != -1)
				str = str.replaceAll("SINGLEQOUTS", "\'");

			if (str.indexOf("DOUBLEQOUTS") != -1)
				str = str.replaceAll("DOUBLEQOUTS", "\"");

			if (str.indexOf("AMPRASAND") != -1)
				str = str.replaceAll("AMPRASAND", "&");
			if (str.indexOf("GREATERTHAN") != -1)
				str = str.replaceAll("GREATERTHAN", ">");

			if (str.indexOf("LESSTHAN") != -1)
				str = str.replaceAll("LESSTHAN", "<");

			if (str.indexOf("ESCLAMATION") != -1)
				str = str.replaceAll("ESCLAMATION", "!");

			if (str.indexOf("AT_THE_RATE") != -1)
				str = str.replaceAll("AT_THE_RATE", "@");

			if (str.indexOf("PERCENTAGE") != -1)
				str = str.replaceAll("PERCENTAGE", "%");

			if (str.indexOf("COMMA") != -1)
				str = str.replaceAll("COMMA", ",");

			if (str.indexOf("EQUALS") != -1)
				str = str.replaceAll("EQUALS", "=");

			if (str.indexOf("TILDE") != -1)
				str = str.replaceAll("TILDE", "~");

			if (str.indexOf("GRAVE") != -1)
				str = str.replaceAll("GRAVE", "`");

			if (str.indexOf("DOLLAR") != -1)
				str = str.replaceAll("DOLLAR", "\\$");

			if (str.indexOf("CARET") != -1)
				str = str.replaceAll("CARET", "^");

			if (str.indexOf("ASTERSTIK") != -1)
				str = str.replaceAll("ASTERSTIK", "*");

			if (str.indexOf("OPENPAR") != -1)
				str = str.replaceAll("OPENPAR", "(");

			if (str.indexOf("CLOSETHESIS") != -1)
				str = str.replaceAll("CLOSETHESIS", ")");

			if (str.indexOf("STARTCURLY") != -1)
				str = str.replaceAll("STARTCURLY", "{");

			if (str.indexOf("ENDBRACKETS") != -1)
				str = str.replaceAll("ENDBRACKETS", "}");

			/*
			 * if (str.indexOf("OPENSQUAREBRACKETS") != -1) str = str.replaceAll("[", "[");
			 */

			if (str.indexOf("CLOSESQUAREBRACKETS") != -1)
				str = str.replaceAll("CLOSESQUAREBRACKETS", "]");

			if (str.indexOf("PIPE") != -1)
				str = str.replaceAll("PIPE", "|");

			if (str.indexOf("BACKSLASH") != -1)
				str = str.replaceAll("BACKSLASH", "\\\\");

			if (str.indexOf("COLON") != -1)
				str = str.replaceAll("COLON", ":");

			if (str.indexOf("SEMI") != -1)
				str = str.replaceAll("SEMI", ";");

			if (str.indexOf("DOUBLEQOUTS") != -1)
				str = str.replaceAll("DOUBLEQOUTS", "\"");

			if (str.indexOf("COMMA") != -1)
				str = str.replaceAll("COMMA", ",");

			if (str.indexOf("DOT") != -1)
				str = str.replaceAll("DOT", ".");

			if (str.indexOf("QUESTIONMARK") != -1)
				str = str.replaceAll("QUESTIONMARK", "?");

			if (str.indexOf("FORWARDSLASH") != -1)
				str = str.replaceAll("FORWARDSLASH", "/");

			if (str.indexOf("PLUSH") != -1)
				str = str.replaceAll("PLUSH", "+");

		}

		return str;
	}

	public static String replaceCharacters2SendPDA(String str) {

		if ((fString(str).length() > 0) && (str != null)) {
			if (str.indexOf("#") != -1)
				str = str.replaceAll("#", "HASH");

			if (str.indexOf("&") != -1)
				str = str.replaceAll("&", "AMPRASAND");

			if (str.indexOf("!") != -1)
				str = str.replaceAll("!", "ESCLAMATION");

			if (str.indexOf("\\@") != -1)
				str = str.replaceAll("\\@", "AT_THE_RATE");

			if (str.indexOf("%") != -1)
				str = str.replaceAll("%", "PERCENTAGE");

			if (str.indexOf("_") != -1)
				str = str.replaceAll("_", "UNDERSCORE");

			if (str.indexOf(">") != -1)
				str = str.replaceAll(">", "GREATERTHAN");

			if (str.indexOf("<") != -1)
				str = str.replaceAll("<", "LESSTHAN");

			if (str.indexOf("~") != -1)
				str = str.replaceAll("~", "TILDE");

			if (str.indexOf("`") != -1)
				str = str.replaceAll("`", "GRAVE");

			if (str.indexOf("\\$") != -1)
				str = str.replaceAll("\\$", "DOLLAR");

			if (str.indexOf("\\^") != -1)
				str = str.replaceAll("\\^", "CARET");

			if (str.indexOf("\\*") != -1)
				str = str.replaceAll("\\*", "ASTERSTIK");

			if (str.indexOf("\\(") != -1)
				str = str.replaceAll("\\(", "OPENPAR");

			if (str.indexOf("\\)") != -1)
				str = str.replaceAll("\\)", "CLOSETHESIS");

			if (str.indexOf("\\{") != -1)
				str = str.replaceAll("\\{", "STARTCURLY");

			if (str.indexOf("\\}") != -1)
				str = str.replaceAll("\\}", "ENDBRACKETS");

			/*
			 * if (str.indexOf("[") != -1) str = str.replaceAll("[", "OPENSQUAREBRACKETS");
			 */

			if (str.indexOf("\\]") != -1)
				str = str.replaceAll("\\]", "SQUARE");

			if (str.indexOf("\\|") != -1)
				str = str.replaceAll("\\|", "PIPE");

			if (str.indexOf("\\\\") != -1)
				str = str.replaceAll("\\\\", "BACKSLASH");

			if (str.indexOf(":") != -1)
				str = str.replaceAll(":", "COLON");

			if (str.indexOf("\\;") != -1)
				str = str.replaceAll("\\;", "SEMI");

			if (str.indexOf("\"") != -1)
				str = str.replaceAll("\"", "DOUBLEQOUTS");

			if (str.indexOf(",") != -1)
				str = str.replaceAll(",", "COMMA");

			if (str.indexOf("\\.") != -1)
				str = str.replaceAll("\\.", "DOT");

			if (str.indexOf("\\?") != -1)
				str = str.replaceAll("\\?", "QUESTIONMARK");

			if (str.indexOf("/") != -1)
				str = str.replaceAll("/", "FORWARDSLASH");

			if (str.indexOf("\\+") != -1)
				str = str.replaceAll("\\+", "PLUSH");

		}
		return str;
	}

	public static String displayDecimal(String val) {

		try {

			if (val.contains(".")) {
				String afterDecimalQty = val.substring(val.indexOf(".") + 1);
				String beforeDecimalQty = val.substring(0, val.indexOf("."));

				if (Long.parseLong(afterDecimalQty) > 0) {
					val = beforeDecimalQty + "." + afterDecimalQty;
				} else {
					val = String.valueOf(Long.parseLong(beforeDecimalQty));
				}
			}

			return val;
		} catch (Exception e) {
			System.out.println("displayDecimal : Val : " + e.getMessage());
			return val;
		}

	}

	public static String InsertQuotes(String field) {
		String retStr = "";
		try {
			if (field.trim().length() > 0) {
				retStr = field.replaceAll("'", "''");
			}
		} catch (Exception e) {
		}
		return retStr;
	}

	public static String RemoveDoubleQuotesToSingle(String field) {
		String retStr = "";
		try {
			if (field.trim().length() > 0) {
				retStr = field.replaceAll("''", "'");
			}
		} catch (Exception e) {
		}
		return retStr;
	}

	public static String insertSinDblQuotes(String field) {
		String retStr = "";
		try {
			if (field.trim().length() > 0) {
				retStr = field.replaceAll("\"", "\"");
				retStr = retStr.replaceAll("'", "\'");
			}
		} catch (Exception e) {
		}
		return retStr;
	}

	public static String insertEscp(String field) {
		String retStr = "";
		try {
			if (field.trim().length() > 0) {
				retStr = field.replaceAll("SINGLEQOUTS", "\\\\\\\'");

				// retStr = retStr.replaceAll("DOUBLEQOUTS", "\"");
				retStr = retStr.replaceAll("DOUBLEQOUTS", "&quot;");
				retStr = retStr.replaceAll("HASH", "#");
				retStr = retStr.replaceAll("AMPRASAND", "&");
				retStr = retStr.replaceAll("COMMA", ",");
				retStr = retStr.replaceAll("ESCLAMATION", "!");
				retStr = retStr.replaceAll("AT_THE_RATE", "@");
				retStr = retStr.replaceAll("PERCENTAGE", "%");
				retStr = retStr.replaceAll("EQUALS", "=");

			}
		} catch (Exception e) {
		}
		return retStr;
	}

	/*
	 * public static String forHTMLTag(String aTagFragment){ final StringBuffer
	 * result = new StringBuffer(); final StringCharacterIterator iterator = new
	 * StringCharacterIterator(aTagFragment); char character = iterator.current();
	 * while (character != StringCharacterIterator.DONE ){ if (character == '<') {
	 * result.append("&lt;"); } else if (character == '>') { result.append("&gt;");
	 * } else if (character == '\"') { result.append("&quot;"); } else if (character
	 * == '\'') { result.append("&#039;"); } else if (character == '\\') {
	 * result.append("&#092;"); } else if (character == '&') {
	 * result.append("&amp;"); } else { //the char is not a special one //add it to
	 * the result as is result.append(character); } character = iterator.next(); }
	 * return result.toString(); }
	 */

	/* Start code added by Bruhan for checking special characters */
	public static String forHTMLTag(String aTagFragment) {
		final StringBuilder result = new StringBuilder();
		try {
			final StringCharacterIterator iterator = new StringCharacterIterator(aTagFragment);
			char character = iterator.current();
			while (character != CharacterIterator.DONE) {
				if (character == '<') {
					result.append("&lt;");
				} else if (character == '>') {
					result.append("&gt;");
				} else if (character == '&') {
					result.append("&amp;");
				} else if (character == '\"') {
					result.append("&quot;");
				} else if (character == '\t') {
					addCharEntity(9, result);
				} else if (character == '!') {
					addCharEntity(33, result);
				} else if (character == '#') {
					addCharEntity(35, result);
				} else if (character == '$') {
					addCharEntity(36, result);
				} else if (character == '%') {
					addCharEntity(37, result);
				} else if (character == '\'') {
					addCharEntity(39, result);
				} else if (character == '(') {
					addCharEntity(40, result);
				} else if (character == ')') {
					addCharEntity(41, result);
				} else if (character == '*') {
					addCharEntity(42, result);
				} else if (character == '+') {
					addCharEntity(43, result);
				} else if (character == ',') {
					addCharEntity(44, result);
				} else if (character == '-') {
					addCharEntity(45, result);
				} else if (character == '.') {
					addCharEntity(46, result);
				} else if (character == '/') {
					addCharEntity(47, result);
				} else if (character == ':') {
					addCharEntity(58, result);
				} else if (character == ';') {
					addCharEntity(59, result);
				} else if (character == '=') {
					addCharEntity(61, result);
				} else if (character == '?') {
					addCharEntity(63, result);
				} else if (character == '@') {
					addCharEntity(64, result);
				} else if (character == '[') {
					addCharEntity(91, result);
				} else if (character == '\\') {
					addCharEntity(92, result);
				} else if (character == ']') {
					addCharEntity(93, result);
				} else if (character == '^') {
					addCharEntity(94, result);
				} else if (character == '_') {
					addCharEntity(95, result);
				} else if (character == '`') {
					addCharEntity(96, result);
				} else if (character == '{') {
					addCharEntity(123, result);
				} else if (character == '|') {
					addCharEntity(124, result);
				} else if (character == '}') {
					addCharEntity(125, result);
				} else if (character == '~') {
					addCharEntity(126, result);
				} else {
					// the char is not a special one
					// add it to the result as is
					result.append(character);
				}
				character = iterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String forHTMLTagPopup(String aTagFragment) {
		final StringBuilder result = new StringBuilder();
		try {
			final StringCharacterIterator iterator = new StringCharacterIterator(aTagFragment);
			char character = iterator.current();
			while (character != CharacterIterator.DONE) {
				if (character == '<') {
					result.append("&lt;");
				} else if (character == '>') {
					result.append("&gt;");
				} else if (character == '&') {
					result.append("&amp;");
				} else if (character == '\"') {
					// result.append("&quot;");
					addCharEntity(34, result);
				} else if (character == '\t') {
					addCharEntity(9, result);
				} else if (character == '!') {
					addCharEntity(33, result);
				} else if (character == '#') {
					addCharEntity(35, result);
				} else if (character == '$') {
					addCharEntity(36, result);
				} else if (character == '%') {
					addCharEntity(37, result);
				} else if (character == '\'') {
					addCharEntity(39, result);
				} else if (character == '(') {
					addCharEntity(40, result);
				} else if (character == ')') {
					addCharEntity(41, result);
				} else if (character == '*') {
					addCharEntity(42, result);
				} else if (character == '+') {
					addCharEntity(43, result);
				} else if (character == ',') {
					addCharEntity(44, result);
				} else if (character == '-') {
					addCharEntity(45, result);
				} else if (character == '.') {
					addCharEntity(46, result);
				} else if (character == '/') {
					addCharEntity(47, result);
				} else if (character == ':') {
					addCharEntity(58, result);
				} else if (character == ';') {
					addCharEntity(59, result);
				} else if (character == '=') {
					addCharEntity(61, result);
				} else if (character == '?') {
					addCharEntity(63, result);
				} else if (character == '@') {
					addCharEntity(64, result);
				} else if (character == '[') {
					addCharEntity(91, result);
				} else if (character == '\\') {
					addCharEntity(92, result);
					addCharEntity(92, result);
				} else if (character == ']') {
					addCharEntity(93, result);
				} else if (character == '^') {
					addCharEntity(94, result);
				} else if (character == '_') {
					addCharEntity(95, result);
				} else if (character == '`') {
					addCharEntity(96, result);
				} else if (character == '{') {
					addCharEntity(123, result);
				} else if (character == '|') {
					addCharEntity(124, result);
				} else if (character == '}') {
					addCharEntity(125, result);
				} else if (character == '~') {
					addCharEntity(126, result);
				} else {
					// the char is not a special one
					// add it to the result as is
					result.append(character);
				}
				character = iterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	private static void addCharEntity(Integer aIdx, StringBuilder aBuilder) {
		String padding = "";
		if (aIdx <= 9) {
			padding = "00";
		} else if (aIdx <= 99) {
			padding = "0";
		} else {
			// no prefix
		}
		String number = padding + aIdx.toString();
		aBuilder.append("&#" + number + ";");
	}
	/* End code added by Bruhan for checking special characters */

	public static String replaceCharacters2Send1(String str) {
		if (fString(str).length() > 0) {
			if (str.indexOf("#") != -1)
				str = str.replaceAll("#", "HASH");
			if (str.indexOf("\'") != -1)
				str = str.replaceAll("\'", "SINGLEQOUTS");
			if (str.indexOf("\"") != -1)
				str = str.replaceAll("\"", "DOUBLEQOUTS");
			if (str.indexOf("&") != -1)
				str = str.replaceAll("&", "AMPRASAND");

		}
		return str;
	}

	public static String formatNum(String number) {
//		NumberFormat numberformat;
		String numFormatStr = "";
//		boolean result = false;
		String pattern = "#,##0.000";
		if (number != null && number.length() > 0) {

			// For Decimal Format

//			NumberFormat nf = NumberFormat.getNumberInstance(new Locale(LANGUAGE, COUNTRY));
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(pattern);
			numFormatStr = df.format(Double.parseDouble(number));
			/* numFormatStr = StrUtils.checkZeros(numFormatStr); */
		} else {
			numFormatStr = "";
		}
//System.out.print("Numberformatstr"+numFormatStr);	
		return numFormatStr;
	}

	public static String formatDecimalNumber(String number) {
//		NumberFormat numberformat;
		String numFormatStr = "";
//		boolean result = false;
		String pattern = "#,##0.00";
		if (number.length() > 0 && number != null) {

			// For Decimal Format

//			NumberFormat nf = NumberFormat.getNumberInstance(new Locale(LANGUAGE, COUNTRY));
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(pattern);
			numFormatStr = df.format(Double.parseDouble(number));
			numFormatStr = StrUtils.checkZeros(numFormatStr);
		} else {
			numFormatStr = "";
		}
//System.out.print("Numberformatstr"+numFormatStr);	
		return numFormatStr;
	}

	public static String formattwodecNum(String number) {
//		NumberFormat numberformat;
		String numFormatStr = "";
//		boolean result = false;
		String pattern = "#,##0.00";
		if (number.length() > 0 && number != null) {

			// For Decimal Format

//			NumberFormat nf = NumberFormat.getNumberInstance(new Locale(LANGUAGE, COUNTRY));
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(pattern);
			numFormatStr = df.format(Double.parseDouble(number));

		} else {
			numFormatStr = "";
		}
//System.out.print("Numberformatstr"+numFormatStr);	
		return numFormatStr;
	}

	public static String formatThreeDecimal(String number) {
//		NumberFormat numberformat;
		String pattern = "###0.000";
		String numFormatStr = "";
		if (number.length() > 0 && number != null) {
			if (number.contains(".")) {
//				NumberFormat nf = NumberFormat.getNumberInstance(new Locale(LANGUAGE, COUNTRY));
				DecimalFormat df = new DecimalFormat();
				df.applyPattern(pattern);
				numFormatStr = df.format(Double.parseDouble(number));
			}
		}
		return numFormatStr;
	}

	public static String formatCurrency(String number) {
		NumberFormat numberformat;

		String numFormatStr = "";
		numberformat = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY));
		numFormatStr = numberformat.format(Double.parseDouble(number));
		return numFormatStr;
	}

	/*
	 * Commented for modyfying currency vt four decimal public static String
	 * currencyWtoutSymbol(String number) { NumberFormat numberformat; String
	 * retFormatStr=""; String numFormatStr=""; numberformat =
	 * NumberFormat.getCurrencyInstance(new Locale(LANGUAGE,COUNTRY)); numFormatStr
	 * = numberformat.format(Double.parseDouble(number)); retFormatStr =
	 * numFormatStr.substring(1, numFormatStr.length());
	 * 
	 * return retFormatStr; }
	 */
	public static String currencyWtoutSymbol(String number) {
//		NumberFormat numberformat;
//		//Get No of decimal points
		int decpts = Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY);
		String appendStr;
		if (Float.parseFloat(number) == 0.0f) {
			appendStr = appendZeros(decpts);
		} else {
			appendStr = appendHashs(decpts);
		}
//		String pattern="###0."+appendStr;
//		String retFormatStr="";
//		String numFormatStr="";
//		numberformat = NumberFormat.getNumberInstance(new Locale(LANGUAGE,COUNTRY));
//		DecimalFormat decformat = new DecimalFormat();
//		decformat.applyPattern(pattern);
//		numFormatStr = decformat.format(Double.parseDouble(number));
		// retFormatStr = numFormatStr.substring(1, numFormatStr.length());
//		NumberFormat numberformat;
		String numFormatStr = "";
//		boolean result = false;
		String pattern = "#,##0." + appendStr;
		if (number.length() > 0 && number != null) {

			// For Decimal Format

//			NumberFormat nf = NumberFormat.getNumberInstance(new Locale(LANGUAGE, COUNTRY));
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(pattern);
			numFormatStr = df.format(Double.parseDouble(number));

		} else {
			numFormatStr = "";
		}

		return numFormatStr;
	}

	public static String currencyWtoutCommSymbol(String number) {
//		NumberFormat numberformat;
//		//Get No of decimal points
		int decpts = Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY);
		String appendStr = "";
		if (number != null && number.length() > 0 && Float.parseFloat(number) == 0.0f) {
			appendStr = appendZeros(decpts);
		} else {
			appendStr = appendHashs(decpts);
		}

//		String pattern="###0."+appendStr;
//		String retFormatStr="";
//		String numFormatStr="";
//		numberformat = NumberFormat.getNumberInstance(new Locale(LANGUAGE,COUNTRY));
//		DecimalFormat decformat = new DecimalFormat();
//		decformat.applyPattern(pattern);
//		numFormatStr = decformat.format(Double.parseDouble(number));
		// retFormatStr = numFormatStr.substring(1, numFormatStr.length());
//		NumberFormat numberformat;
		String numFormatStr = "";
//		boolean result = false;
		String pattern = "###0." + appendStr;
		if (number != null && number.length() > 0) {

			// For Decimal Format

//			NumberFormat nf = NumberFormat.getNumberInstance(new Locale(LANGUAGE, COUNTRY));
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(pattern);
			numFormatStr = df.format(Double.parseDouble(number));

		} else {
			numFormatStr = "";
		}

		return numFormatStr;
	}

	public static String appendZeros(int number) {
		String appendStr = "";
		for (int i = 0; i < number; i++) {
			appendStr = appendStr + "0";
		}

		return appendStr;
	}

	public static String appendHashs(int number) {
		String appendStr = "";
		for (int i = 0; i < number; i++) {
			appendStr = appendStr + "#";
		}

		return appendStr;
	}

	public static String removeFormat(String number) {
		String appendString = "";
		String[] splitformat = number.split(",");
		for (int i = 0; i < splitformat.length; i++) {

			appendString = appendString + splitformat[i];
		}

		return appendString;

	}

	public static String checkZeros(String number) {
//		boolean flag = true;
		String result = number;
		if (number.contains(".")) {
			String strarr[] = number.split("\\.");
//System.out.println("arralen"+strarr.length);		

			if (strarr[1].equalsIgnoreCase("000")) {
				result = strarr[0];
			}
			if (strarr[1].equalsIgnoreCase("00")) {
				result = strarr[0];
			}
			if (strarr[1].equalsIgnoreCase("0")) {
				result = strarr[0];
			}
		}
		return result;
	}

	public static String trimTrailingSpace(String str) {
		StringCharacterIterator iter = new StringCharacterIterator(str);
		int len = str.length();
		for (char c = iter.last(); c != CharacterIterator.DONE && c <= ' '; c = iter.previous()) {
			len--;
		}
		return ((len < str.length())) ? str.substring(0, len) : str;
	}

	/*
	 * public String RemoveSlash(String field) { String retStr = ""; try { if
	 * (field.trim().length() > 0) { retStr = field.replaceAll("/", ""); } } catch
	 * (Exception e) { } return retStr; }
	 */

	public static void main(String[] args) {
		try {
			String val = "1.25";

			if (val.contains(".")) {
				String afterDecimalQty = val.substring(val.indexOf(".") + 1);
				String beforeDecimalQty = val.substring(0, val.indexOf("."));

				if (Long.parseLong(afterDecimalQty) > 0) {
					val = beforeDecimalQty + "." + afterDecimalQty;
				} else {
					val = String.valueOf(Long.parseLong(beforeDecimalQty));
				}
			}

			System.out.println("Val : " + val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String replaceHtmlEntity(String str) {
		if (fString(str).length() > 0) {
			if (str.indexOf("&lt;") != -1)
				str = str.replaceAll("&lt;", "<");

			if (str.indexOf("&gt;") != -1)
				str = str.replaceAll("&gt;", ">");

			if (str.indexOf("&amp;") != -1)
				str = str.replaceAll("&amp;", "&");

			if (str.indexOf("&quot;") != -1)
				str = str.replaceAll("&quot;", "\"");

			if (str.indexOf("&#09;") != -1)
				str = str.replaceAll("&#09;", "\t");

			if (str.indexOf("&#033;") != -1)
				str = str.replaceAll("&#033;", "!");

			if (str.indexOf("&#035;") != -1)
				str = str.replaceAll("&#035;", "#");

			if (str.indexOf("&#036;") != -1)
				str = str.replaceAll("&#036;", "\\$");

			if (str.indexOf("&#037;") != -1)
				str = str.replaceAll("&#037;", "%");

			if (str.indexOf("&#039;") != -1)
				str = str.replaceAll("&#039;", "\'");

			if (str.indexOf("&#040;") != -1)
				str = str.replaceAll("&#040;", "(");

			if (str.indexOf("&#041;") != -1)
				str = str.replaceAll("&#041;", ")");

			if (str.indexOf("&#042;") != -1)
				str = str.replaceAll("&#042;", "*");

			if (str.indexOf("&#043;") != -1)
				str = str.replaceAll("&#043;", "+");

			if (str.indexOf("&#044;") != -1)
				str = str.replaceAll("&#044;", ",");
			if (str.indexOf("&#045;") != -1)
				str = str.replaceAll("&#045;", "-");

			if (str.indexOf("&#046;") != -1)
				str = str.replaceAll("&#046;", ".");

			if (str.indexOf("&#047;") != -1)
				str = str.replaceAll("&#047;", "/");

			if (str.indexOf("&#058;") != -1)
				str = str.replaceAll("&#058;", ":");

			if (str.indexOf("&#059;") != -1)
				str = str.replaceAll("&#059;", ";");

			if (str.indexOf("&#061;") != -1)
				str = str.replaceAll("&#061;", "=");

			if (str.indexOf("&#063;") != -1)
				str = str.replaceAll("&#063;", "?");

			if (str.indexOf("&#064;") != -1)
				str = str.replaceAll("&#064;", "@");

			if (str.indexOf("&#091;") != -1)
				str = str.replaceAll("&#091;", "[");

			if (str.indexOf("&#092;") != -1)
				str = str.replaceAll("&#092;", "\\\\");

			if (str.indexOf("&#093;") != -1)
				str = str.replaceAll("&#093;", "]");

			if (str.indexOf("&#094;") != -1)
				str = str.replaceAll("&#094;", "^");

			if (str.indexOf("&#095;") != -1)
				str = str.replaceAll("&#095;", "_");
			if (str.indexOf("&#096;") != -1)
				str = str.replaceAll("&#096;", "`");

			if (str.indexOf("&#123;") != -1)
				str = str.replaceAll("&#123;", "{");

			if (str.indexOf("&#124;") != -1)
				str = str.replaceAll("&#124;", "|");

			if (str.indexOf("&#125;") != -1)
				str = str.replaceAll("&#125;", "}");

			if (str.indexOf("&#126;") != -1)
				str = str.replaceAll("&#126;", "~");
		}
		return str;
	}

	public static String replaceCharacters2Str(String inputStr) {

		final StringBuilder result = new StringBuilder();
		String str = "";
		try {
			str = (inputStr.equalsIgnoreCase("") || (inputStr.equalsIgnoreCase("NULL"))) ? "" : inputStr.trim();
			final StringCharacterIterator iterator = new StringCharacterIterator(str);
			char character = iterator.current();
			while (character != CharacterIterator.DONE) {
				if (str.indexOf("~") != -1)
					str = str.replaceAll("~", "SCTILDESC");
				else if (str.indexOf("`") != -1)
					str = str.replaceAll("`", "SCBACKTICKSC");

				else if (str.indexOf("!") != -1)
					str = str.replaceAll("!", "SCEXCLAMATIONSC");

				else if (str.indexOf("@") != -1)
					str = str.replaceAll("@", "SCATTHERATESC");

				else if (str.indexOf("#") != -1)
					str = str.replaceAll("#", "SCHASHSC");

				else if (str.indexOf("$") != -1)
					str = str.replaceAll("\\$", "SCDOLLARSC");

				else if (str.indexOf("%") != -1)
					str = str.replaceAll("%", "SCPERCENTAGESC");

				else if (str.indexOf("^") != -1)
					str = str.replaceAll("\\^", "SCCARETSC");

				else if (str.indexOf("&") != -1)
					str = str.replaceAll("&", "SCAMPRASANDSC");

				else if (str.indexOf("*") != -1)
					str = str.replaceAll("\\*", "SCASTERISKSC");

				else if (str.indexOf("(") != -1)
					str = str.replaceAll("\\(", "SCLEFTPARENTHESISSC");

				else if (str.indexOf(")") != -1)
					str = str.replaceAll("\\)", "SCRIGHTPARENTHESISSC");

				else if (str.indexOf("_") != -1)
					str = str.replaceAll("_", "SCUNDERSCORESC");

				else if (str.indexOf("-") != -1)
					str = str.replaceAll("\\-", "SCMINUSSC");

				else if (str.indexOf("+") != -1)
					str = str.replaceAll("\\+", "SCPLUSSC");

				else if (str.indexOf("=") != -1)
					str = str.replaceAll("=", "SCEQUALSIGNSC");

				else if (str.indexOf("{") != -1)
					str = str.replaceAll("\\{", "SCLEFTBRACESC");

				else if (str.indexOf("}") != -1)
					str = str.replaceAll("}", "SCRIGHTBRACESC");

				else if (str.indexOf("]") != -1)
					str = str.replaceAll("]", "SCRIGHTBRACKETSC");

				else if (str.indexOf("\\[") != -1)
					str = str.replaceAll("\\[", "SCLEFTBRACKETSC");

				else if (str.indexOf("|") != -1)
					str = str.replaceAll("\\|", "SCVERTICALBARSC");

				else if (str.indexOf("\\") != -1)
					str = str.replaceAll("\\\\", "SCBACKSLASHSC");

				else if (str.indexOf(":") != -1)
					str = str.replaceAll(":", "SCKOLONSC");

				else if (str.indexOf(";") != -1)
					str = str.replaceAll(";", "SCSEMOCOLONSC");

				else if (str.indexOf("\"") != -1)
					str = str.replaceAll("\"", "SCDOUBLEQOUTSSC");

				else if (str.indexOf("\'") != -1)
					str = str.replaceAll("\'", "SCSINGLEQOUTSSC");

				else if (str.indexOf(">") != -1)
					str = str.replaceAll(">", "SCGREATERTHANSC");

				else if (str.indexOf("<") != -1)
					str = str.replaceAll("<", "SCLESSTHANSC");

				else if (str.indexOf(".") != -1)
					str = str.replaceAll("\\.", "SCFULLSTOPSC");

				else if (str.indexOf("?") != -1)
					str = str.replaceAll("\\?", "SCQUESTIONMARKSC");

				else if (str.indexOf("/") != -1)
					str = str.replaceAll("/", "SCSLASHSC");
				else if (str.indexOf(",") != -1)
					str = str.replaceAll(",", "SCCOMMASC");
				else {
					// the char is not a special one
					// add it to the result as is
					result.append(character);
				}
				character = iterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.toString();
	}

	public static String replaceStr2Char(String str) {
		if (fString(str).length() > 0) {
			if (str.indexOf("SCTILDESC") != -1)
				str = str.replaceAll("SCTILDESC", "~");

			if (str.indexOf("SCBACKTICKSC") != -1)
				str = str.replaceAll("SCBACKTICKSC", "`");

			if (str.indexOf("SCEXCLAMATIONSC") != -1)
				str = str.replaceAll("SCEXCLAMATIONSC", "!");

			if (str.indexOf("SCATTHERATESC") != -1)
				str = str.replaceAll("SCATTHERATESC", "@");

			if (str.indexOf("SCHASHSC") != -1)
				str = str.replaceAll("SCHASHSC", "#");

			if (str.indexOf("SCDOLLARSC") != -1)
				str = str.replaceAll("SCDOLLARSC", "\\$");

			if (str.indexOf("SCPERCENTAGESC") != -1)
				str = str.replaceAll("SCPERCENTAGESC", "%");

			if (str.indexOf("SCCARETSC") != -1)
				str = str.replaceAll("SCCARETSC", "\\^");

			if (str.indexOf("SCAMPRASANDSC") != -1)
				str = str.replaceAll("SCAMPRASANDSC", "&");

			if (str.indexOf("SCASTERISKSC") != -1)
				str = str.replaceAll("SCASTERISKSC", "*");

			if (str.indexOf("SCLEFTPARENTHESISSC") != -1)
				str = str.replaceAll("SCLEFTPARENTHESISSC", "\\(");

			if (str.indexOf("SCRIGHTPARENTHESISSC") != -1)
				str = str.replaceAll("SCRIGHTPARENTHESISSC", "\\)");

			if (str.indexOf("SCUNDERSCORESC") != -1)
				str = str.replaceAll("SCUNDERSCORESC", "_");

			if (str.indexOf("SCMINUSSC") != -1)
				str = str.replaceAll("SCMINUSSC", "\\-");

			if (str.indexOf("SCPLUSSC") != -1)
				str = str.replaceAll("SCPLUSSC", "\\+");

			if (str.indexOf("SCEQUALSIGNSC") != -1)
				str = str.replaceAll("SCEQUALSIGNSC", "=");

			if (str.indexOf("SCLEFTBRACESC") != -1)
				str = str.replaceAll("SCLEFTBRACESC", "{");

			if (str.indexOf("SCRIGHTBRACESC") != -1)
				str = str.replaceAll("SCRIGHTBRACESC", "}");

			if (str.indexOf("SCRIGHTBRACKETSC") != -1)
				str = str.replaceAll("SCRIGHTBRACKETSC", "]");

			if (str.indexOf("SCLEFTBRACKETSC") != -1)
				str = str.replaceAll("SCLEFTBRACKETSC", "[");

			if (str.indexOf("SCVERTICALBARSC") != -1)
				str = str.replaceAll("SCVERTICALBARSC", "|");

			if (str.indexOf("SCBACKSLASHSC") != -1)
				str = str.replaceAll("SCBACKSLASHSC", "\\\\");

			if (str.indexOf("SCKOLONSC") != -1)
				str = str.replaceAll("SCKOLONSC", ":");

			if (str.indexOf("SCSEMOCOLONSC") != -1)
				str = str.replaceAll("SCSEMOCOLONSC", ";");

			if (str.indexOf("SCDOUBLEQOUTSSC") != -1)
				str = str.replaceAll("SCDOUBLEQOUTSSC", "\"");

			if (str.indexOf("SCSINGLEQOUTSSC") != -1)
				str = str.replaceAll("SCSINGLEQOUTSSC", "\'");

			if (str.indexOf("SCGREATERTHANSC") != -1)
				str = str.replaceAll("SCGREATERTHANSC", ">");

			if (str.indexOf("SCLESSTHANSC") != -1)
				str = str.replaceAll("SCLESSTHANSC", "<");

			if (str.indexOf("SCFULLSTOPSC") != -1)
				str = str.replaceAll("SCFULLSTOPSC", ".");

			if (str.indexOf("SCQUESTIONMARKSC") != -1)
				str = str.replaceAll("SCQUESTIONMARKSC", "?");

			if (str.indexOf("SCSLASHSC") != -1)
				str = str.replaceAll("SCSLASHSC", "/");

			if (str.indexOf("SCCOMMASC") != -1)
				str = str.replaceAll("SCCOMMASC", ",");

		}
		return str;
	}

	public static String RemoveSlash(String field) {
		final StringBuilder result = new StringBuilder();
		try {
			final StringCharacterIterator iterator = new StringCharacterIterator(field);
			char character = iterator.current();
			while (character != CharacterIterator.DONE) {
				if (character == '<') {
					result.append("");
				} else if (character == '>') {
					result.append("");
				} else if (character == '&') {
					result.append("");
				} else if (character == '\"') {
					// result.append("&quot;");
					result.append("");
				} else if (character == '\t') {
					result.append("");
				} else if (character == '!') {
					result.append("");
				} else if (character == '#') {
					result.append("");
				} else if (character == '$') {
					result.append("");
				} else if (character == '%') {
					result.append("");
				} else if (character == '\'') {
					result.append("");
				} else if (character == '(') {
					result.append("");
				} else if (character == ')') {
					result.append("");
				} else if (character == '*') {
					result.append("");
				} else if (character == '+') {
					result.append("");
				} else if (character == ',') {
					result.append("");
				} else if (character == '-') {
					result.append("");
				}

				else if (character == '/') {
					result.append("");
				} else if (character == ':') {
					result.append("");
				} else if (character == ';') {
					result.append("");
				} else if (character == '=') {
					result.append("");
				} else if (character == '?') {
					result.append("");
				} else if (character == '@') {
					result.append("");
				} else if (character == '[') {
					result.append("");
				} else if (character == '\\') {
					result.append("");
				} else if (character == ']') {
					result.append("");
				} else if (character == '^') {
					result.append("");
				} else if (character == '_') {
					result.append("");
				} else if (character == '`') {
					result.append("");
				} else if (character == '{') {
					result.append("");
				} else if (character == '|') {
					result.append("");
				} else if (character == '}') {
					result.append("");
				} else if (character == '~') {
					result.append("");
				} else if (character == '.') {
					result.append("");
				} else {
					// the char is not a special one
					// add it to the result as is
					result.append(character);
				}
				character = iterator.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String replaceCharacters2RecvMaxBarcode(String str) {

		if (fString(str).length() > 0) {
			if (str.indexOf("HASH") != -1)
				str = str.replaceAll("HASH", "#");
			if (str.indexOf("HYPEN") != -1)
				str = str.replaceAll("HYPEN", "-");
			if (str.indexOf("UNDERSCORE") != -1)
				str = str.replaceAll("UNDERSCORE", "_");
			if (str.indexOf("SINGLEQOUTS") != -1)
				str = str.replaceAll("SINGLEQOUTS", "\'");

			if (str.indexOf("DOUBLEQOUTS") != -1)
				str = str.replaceAll("DOUBLEQOUTS", "\"");

			if (str.indexOf("AMPRASAND") != -1)
				str = str.replaceAll("AMPRASAND", "&");
			if (str.indexOf("GREATERTHAN") != -1)
				str = str.replaceAll("GREATERTHAN", ">");

			if (str.indexOf("LESSTHAN") != -1)
				str = str.replaceAll("LESSTHAN", "<");

			if (str.indexOf("ESCLAMATION") != -1)
				str = str.replaceAll("ESCLAMATION", "!");

			if (str.indexOf("AT_THE_RATE") != -1)
				str = str.replaceAll("AT_THE_RATE", "@");

			if (str.indexOf("PERCENTAGE") != -1)
				str = str.replaceAll("PERCENTAGE", "%");

			if (str.indexOf("COMMA") != -1)
				str = str.replaceAll("COMMA", ",");

			if (str.indexOf("EQUALS") != -1)
				str = str.replaceAll("EQUALS", "=");

			if (str.indexOf("TILDE") != -1)
				str = str.replaceAll("TILDE", "~");

			if (str.indexOf("GRAVE") != -1)
				str = str.replaceAll("GRAVE", "`");

			if (str.indexOf("DOLLAR") != -1)
				str = str.replaceAll("DOLLAR", "$");

			if (str.indexOf("CARET") != -1)
				str = str.replaceAll("CARET", "^");

			if (str.indexOf("ASTERSTIK") != -1)
				str = str.replaceAll("ASTERSTIK", "*");

			if (str.indexOf("OPENPAR") != -1)
				str = str.replaceAll("OPENPAR", "(");

			if (str.indexOf("CLOSETHESIS") != -1)
				str = str.replaceAll("CLOSETHESIS", ")");

			if (str.indexOf("STARTCURLY") != -1)
				str = str.replaceAll("STARTCURLY", "{");

			if (str.indexOf("ENDBRACKETS") != -1)
				str = str.replaceAll("ENDBRACKETS", "}");

			/*
			 * if (str.indexOf("OPENSQUAREBRACKETS") != -1) str = str.replaceAll("[", "[");
			 */

			if (str.indexOf("SQUARE") != -1)
				str = str.replaceAll("SQUARE", "]");

			if (str.indexOf("PIPE") != -1)
				str = str.replaceAll("PIPE", "|");

			if (str.indexOf("BACKSLASH") != -1)
				str = str.replaceAll("BACKSLASH", "\\");

			if (str.indexOf("COLON") != -1)
				str = str.replaceAll("COLON", ":");

			if (str.indexOf("SEMI") != -1)
				str = str.replaceAll("SEMI", ";");

			if (str.indexOf("DOUBLEQOUTS") != -1)
				str = str.replaceAll("DOUBLEQOUTS", "\"");

			if (str.indexOf("COMMA") != -1)
				str = str.replaceAll("COMMA", ",");

			if (str.indexOf("DOT") != -1)
				str = str.replaceAll("DOT", ".");

			if (str.indexOf("QUESTIONMARK") != -1)
				str = str.replaceAll("QUESTIONMARK", "?");

			if (str.indexOf("FORWARDSLASH") != -1)
				str = str.replaceAll("FORWARDSLASH", "/");

			if (str.indexOf("PLUSH") != -1)
				str = str.replaceAll("PLUSH", "+");

		}

		return str;
	}

	public static String replaceCharacters2SendMaxBarcode(String str) {
		if (fString(str).length() > 0) {
			if (str.indexOf("#") != -1)
				str = str.replaceAll("#", "HASH");

			if (str.indexOf("&") != -1)
				str = str.replaceAll("&", "AMPRASAND");

			if (str.indexOf("!") != -1)
				str = str.replaceAll("!", "ESCLAMATION");

			if (str.indexOf("\\@") != -1)
				str = str.replaceAll("\\@", "AT_THE_RATE");

			if (str.indexOf("%") != -1)
				str = str.replaceAll("%", "PERCENTAGE");

			if (str.indexOf("_") != -1)
				str = str.replaceAll("_", "UNDERSCORE");

			if (str.indexOf(">") != -1)
				str = str.replaceAll(">", "GREATERTHAN");

			if (str.indexOf("<") != -1)
				str = str.replaceAll("<", "LESSTHAN");

			if (str.indexOf("~") != -1)
				str = str.replaceAll("~", "TILDE");

			if (str.indexOf("`") != -1)
				str = str.replaceAll("`", "GRAVE");

			if (str.indexOf("\\$") != -1)
				str = str.replaceAll("\\$", "DOLLAR");

			if (str.indexOf("\\^") != -1)
				str = str.replaceAll("\\^", "CARET");

			if (str.indexOf("\\*") != -1)
				str = str.replaceAll("\\*", "ASTERSTIK");

			if (str.indexOf("\\(") != -1)
				str = str.replaceAll("\\(", "OPENPAR");

			if (str.indexOf("\\)") != -1)
				str = str.replaceAll("\\)", "CLOSETHESIS");

			if (str.indexOf("\\{") != -1)
				str = str.replaceAll("\\{", "STARTCURLY");

			if (str.indexOf("\\}") != -1)
				str = str.replaceAll("\\}", "ENDBRACKETS");

			/*
			 * if (str.indexOf("[") != -1) str = str.replaceAll("[", "OPENSQUAREBRACKETS");
			 */

			if (str.indexOf("\\]") != -1)
				str = str.replaceAll("\\]", "SQUARE");

			if (str.indexOf("\\|") != -1)
				str = str.replaceAll("\\|", "PIPE");

			if (str.indexOf("\\\\") != -1)
				str = str.replaceAll("\\\\", "BACKSLASH");

			if (str.indexOf(":") != -1)
				str = str.replaceAll(":", "COLON");

			if (str.indexOf("\\;") != -1)
				str = str.replaceAll("\\;", "SEMI");

			if (str.indexOf("\"") != -1)
				str = str.replaceAll("\"", "DOUBLEQOUTS");

			if (str.indexOf(",") != -1)
				str = str.replaceAll(",", "COMMA");

			if (str.indexOf("\\.") != -1)
				str = str.replaceAll("\\.", "DOT");

			if (str.indexOf("\\?") != -1)
				str = str.replaceAll("\\?", "QUESTIONMARK");

			if (str.indexOf("/") != -1)
				str = str.replaceAll("/", "FORWARDSLASH");

			if (str.indexOf("\\+") != -1)
				str = str.replaceAll("\\+", "PLUSH");

		}
		return str;
	}

	public static boolean isCharAllowed(String str) {

		boolean flag = false;
		if ((fString(str).length() > 0) && (str != null)) {
			if (str.indexOf("'") != -1)
				flag = true;
			if (str.indexOf("[") != -1)
				flag = true;
		}
		return flag;
	}

	public static String addZeroes(double value, String numberOfDecimal) {
		String pattern = "###.0";
		if (numberOfDecimal.equalsIgnoreCase("1")) {
			pattern = "###0.0";
		} else if (numberOfDecimal.equalsIgnoreCase("2")) {
			pattern = "###0.00";
		} else if (numberOfDecimal.equalsIgnoreCase("3")) {
			pattern = "###0.000";
		} else if (numberOfDecimal.equalsIgnoreCase("4")) {
			pattern = "###0.0000";
		} else if (numberOfDecimal.equalsIgnoreCase("5")) {
			pattern = "###0.00000";
		}
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(value);
	}

}
