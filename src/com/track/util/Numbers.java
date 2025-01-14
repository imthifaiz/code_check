package com.track.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Numbers {
	
	public String Round(Double d,int scale,RoundingMode rm)
	{
		 BigDecimal bd = new BigDecimal(d).setScale(scale, rm);
	     Double roundedValue = bd.doubleValue();
	     return String.format("%."+scale+"f", roundedValue);
	}

	
	public static String toMillionFormat(double inputNumber, int numberOfDecimals) {
		DecimalFormat n = (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US); 
		DecimalFormatSymbols symbols = n.getDecimalFormatSymbols();
		symbols.setCurrencySymbol(""); // Don't use null.
		n.setDecimalFormatSymbols(symbols);
		n.setNegativePrefix("-");
		n.setNegativeSuffix("");
		n.setMinimumFractionDigits(numberOfDecimals);
		n.setMaximumFractionDigits(numberOfDecimals);
		return n.format(inputNumber);
	}
	
	public static String toMillionFormat(BigDecimal inputNumber, Integer numberOfDecimals) {
		return toMillionFormat(inputNumber.doubleValue(), numberOfDecimals.intValue());
	}
	
	public static String toMillionFormat(BigDecimal inputNumber, int numberOfDecimals) {
		return toMillionFormat(inputNumber.doubleValue(), numberOfDecimals);
	}
	
	public static String toMillionFormat(String inputNumber, int numberOfDecimals) {
		return toMillionFormat(Double.parseDouble(inputNumber.replaceAll(",", "")), numberOfDecimals);
	}
	
	public static String toMillionFormat(String inputNumber, String numberOfDecimals) {
		return toMillionFormat(Double.parseDouble(inputNumber.replaceAll(",", "")), Integer.parseInt(numberOfDecimals));
	}
	
	public static String toMillionFormat(Double inputNumber, int numberOfDecimals) {
		return toMillionFormat(inputNumber.doubleValue(), numberOfDecimals);
	}
	
	public static String toMillionFormat(double inputNumber, String numberOfDecimals) {
		return toMillionFormat(inputNumber, Integer.parseInt(numberOfDecimals));
	}
}
