package com.track.db.util;

import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.track.constants.IConstants;
import com.track.dao.ItemSesBeanDAO;
import com.track.gates.DbBean;

public class ImportUtils {
	
	private ItemSesBeanDAO itemSesBeanDAO = new ItemSesBeanDAO();

	// quantity validation
	public boolean isCorrectQuantityFormat(String quantity) {

		boolean result = false;
		String prefixPart = "";
		String surfixPart = "";

		if (quantity.contains(".")) {
			prefixPart = quantity.substring(0, quantity.indexOf("."));
		        if(prefixPart.length()==0)prefixPart="0";
			surfixPart = quantity.substring(prefixPart.length() + 1, quantity
					.length());

			if (surfixPart.contains(".")) {
				result = false;
			} else {
				try {
					int value = Integer.parseInt(surfixPart);
					if (value >= 0) {
						int pvalue = Integer.parseInt(prefixPart);
						if (pvalue >= 0) {
							result = true;
						}
					}
				} catch (Exception ex) {
					result = false;
				}
			}
		} else {
			try {
				int value = Integer.parseInt(quantity);
				if (value >= 0) {
					result = true;
				}
			} catch (Exception ex) {
				result = false;
			}
		}

		return result;
	}

	// UnitPrice validation
	public boolean isCorrectUnitPriceFormat(String quantity) {

		boolean result = false;
		String prefixPart = "";
		String surfixPart = "";

		if (quantity.contains(".")) {
			prefixPart = quantity.substring(0, quantity.indexOf("."));
                        if(prefixPart.length()==0)prefixPart="0";
			surfixPart = quantity.substring(prefixPart.length() + 1, quantity.length());
			if (surfixPart.contains(".")) {
				result = false;
			} else {
				
				try {
                                         long value = Long.parseLong(surfixPart);
                                  
					if (value >= 0) {
						int pvalue = Integer.parseInt(prefixPart);
						if (pvalue >= 0) {
							result = true;
						}
                                            
					}
				} catch (Exception ex) {
                                 System.out.println("Exception :"+ex.getMessage());
					result = false;
				}
			}
		} else {
			try {
				int value = Integer.parseInt(quantity);
				if (value >= 0) {
					result = true;
				}
			} catch (Exception ex) {
				result = false;
			}
		}

		return result;
	}
	
	// Equivalent Currency validation
		public boolean isCorrectEquivalentCurrencyFormat(String quantity) {

			boolean result = false;
			String prefixPart = "";
			String surfixPart = "";

			if (quantity.contains(".")) {
				prefixPart = quantity.substring(0, quantity.indexOf("."));
	                        if(prefixPart.length()==0)prefixPart="0";
				surfixPart = quantity.substring(prefixPart.length() + 1, quantity.length());
				if (surfixPart.contains(".")) {
					result = false;
				} else {
					
					try {
	                                         long value = Long.parseLong(surfixPart);
	                                  
						if (value >= 0) {
							int pvalue = Integer.parseInt(prefixPart);
							if (pvalue >= 0) {
								result = true;
							}
	                                            
						}
					} catch (Exception ex) {
	                                 System.out.println("Exception :"+ex.getMessage());
						result = false;
					}
				}
			} else {
				try {
					int value = Integer.parseInt(quantity);
					if (value >= 0) {
						result = true;
					}
				} catch (Exception ex) {
					result = false;
				}
			}

			return result;
		}

	// date validation
	public boolean isCorrectDateFormat(String str_date) {

		boolean result = false;

		try {
			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			date = (Date) formatter.parse(str_date);
			result = true;
		} catch (ParseException e) {
			result = false;
		}

		return result;
	}

	// date validation with regular expression
	public boolean isCorrectDateFormatUsingRegex(String str_date) {

		boolean result = false;
                String expression = "^(0[1-9]|[1][0-9]|[2][0-9]|3[0-1])[/]?(0[1-9]|[1][0-2])[/]?(18|19|20|21)\\d{2}$";
		//String expression = DbBean.ImportDataDateFormat;

		CharSequence inputStr = str_date;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			result = true;
		}

		return result;
	}

	// time validation
	public boolean isCorrectTimepartFormat(String str_time) {

		boolean result = false;
		String strTime = str_time.trim();

		if (strTime.length() == 4) {
			String hourPart = strTime.substring(0, 2);
			String minPart = strTime.substring(2, 4);

			try {
				int hvalue = Integer.parseInt(hourPart);
				int mvalue = Integer.parseInt(minPart);
				if ((hvalue <= 24) && (mvalue <= 59)) {
					result = true;
				}
			} catch (Exception ex) {
				result = false;
			}
		}

		return result;
	}
	
	// active field validation
	public boolean isCorrectActiveFormat(String str_ac) {

		boolean result = false;
		String strAc = str_ac.trim();

		if(strAc.length() == 1){
			if(("N".equalsIgnoreCase(strAc)) || ("Y".equalsIgnoreCase(strAc))){
				result = true;
			}
		}

		return result;
	}
	
	//Apply to all validation
	public boolean isCorrectApplyuom(String str_ac) {

		boolean result = false;
		String strAc = str_ac.trim();

		if(strAc.length() == 1){
			if(("N".equalsIgnoreCase(strAc)) || ("Y".equalsIgnoreCase(strAc))){
				result = true;
			}
		}

		return result;
	}
	
	// kiting field validation
	public boolean isCorrectKitingFormat(String str_ac) {

		boolean result = false;
		String strAc = str_ac.trim();

		if(strAc.length() == 1){
			if(("N".equalsIgnoreCase(strAc)) || ("K".equalsIgnoreCase(strAc))){
				result = true;
			}
		}

		return result;
	}
	
	// field's value length validation against database
	@SuppressWarnings("unchecked")
	public boolean isValidDataLeangthForColum(String aPlant, String aTable, String aColum, String avalue){
		
		boolean result = false;
		
		Hashtable ht = new Hashtable();
		ht.put(IConstants.PLANT, aPlant);
		ht.put(IConstants.TABLE_NAME, aTable);
		
		try {
			ResultSetMetaData md = itemSesBeanDAO.getTableMetaData(ht);
			int col = md.getColumnCount();
			
			for (int i = 1; i <= col; i++){
				  String col_name = md.getColumnName(i);
				  
				  if(col_name.equalsIgnoreCase(aColum)){
					  int col_length = md.getColumnDisplaySize(i);
					  
					  if(col_length > avalue.length()){
						  result = true;
					  }
					  break;
				  }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
