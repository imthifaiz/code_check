package com.track.gates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import com.track.constants.MLoggerConstant;

/********************************************************************************************************
 * PURPOSE : A class for Building the SQL Strings Dynamically
 *******************************************************************************************************/
public class TableList {

	/********************************************************************************************************
	 * PURPOSE : Method for adding the Field elements of database table in the
	 * ArrayList , from the external file - TABLES_SQL.properties : Used in
	 * places like request.getParameter(field_name) where the field names can be
	 * taken from Arraylist returned this method PARAMETER 1 : Database table
	 * name RETURNS : Arraylist containing all the fields mentioned against the
	 * table name in the external file
	 *******************************************************************************************************/
	public ArrayList getTableArray(String tableName) {

		Properties pr;
		InputStream ip;
		ArrayList al = new ArrayList();
		String tableString = null;

		try {

			pr = new Properties();
			ip = new FileInputStream(new File(MLoggerConstant.PROPS_FOLDER + "/TABLES_SQL.properties"));
			pr.load(ip);
			tableString = pr.getProperty(tableName);
			StringTokenizer st = new StringTokenizer(tableString, ",");

			while (st.hasMoreTokens()) {
				al.add(st.nextElement());
			}

		} catch (FileNotFoundException fe) {
			System.out.println("Could not load the File TABLE_SQL");
			DbBean.writeError("TablelistBean", "getTableArray()", fe);
		} catch (Exception e) {
			DbBean.writeError("TablelistBean", "getTableArray()", e);
		}
		return al;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for constructing the sql insert statement with table
	 * fields ( Used alongwith getValueString() method ) PARAMETER 1 : Arraylist
	 * containing the fields to be inserted inthe database RETURNS : First part
	 * of sql insert statement ( the next part is provided by getValueString()
	 * method )
	 *******************************************************************************************************/
	public String getFieldString(ArrayList al) {
		String fields = "";

		for (int i = 0; i < al.size(); i++) {
			if (i == (al.size() - 1))
				fields = fields + (String) al.get(i);
			else
				fields = fields + (String) al.get(i) + ",";
		}

		return fields;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for constructing the sql insert statement with table
	 * field values ( Used alongwith getFieldString() method ) PARAMETER 1 :
	 * Arraylist containing the field values to be inserted inthe database
	 * RETURNS : Second part of sql insert statement ( the previuos part is
	 * provided by getFieldString() method )
	 *******************************************************************************************************/
	public String getValueString(ArrayList al) {
		String values = "";
		String insertValue;

		for (int i = 0; i < al.size(); i++) {
			insertValue = formatString((String) al.get(i)); // replaces '
			// character with
			// two ' characters
			if (i == (al.size() - 1))
				values = values + "'" + insertValue + "'";
			else
				values = values + "'" + insertValue + "',";
		}
		return values;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for constructing the sql update statement PARAMETER 1 :
	 * Arraylist containing fields to be modified PARAMETER 2 : Arraylist
	 * containing new values respective to fields in the other Arraylist RETURNS
	 * : Sql update string
	 *******************************************************************************************************/

	public String buildUpdateString(ArrayList al, ArrayList ral) {
		String toUpdateString = "";
		String insertValue;

		for (int i = 0; i < ral.size(); i++) {
			insertValue = formatString((String) ral.get(i)); // replaces '
			// character
			// with two '
			// characters

			if (i == (ral.size() - 1))
				toUpdateString += al.get(i) + "='" + insertValue + "'";
			else
				toUpdateString += al.get(i) + "='" + insertValue + "',";
		}
		return toUpdateString;
	}

	/********************************************************************************************************
	 * PURPOSE : A method for inserting single quotes in SQL string by adding up
	 * another single quote PARAMETER 1 : Sql string RETURNS : Formatted sql
	 * string
	 *******************************************************************************************************/
	private String formatString(String str) {
		StringBuffer strB = new StringBuffer();
		int len = str.length();
		char ch;

		// start scan throught the string
		for (int i = 0; i < len; i++) {
			ch = str.charAt(i);
			switch ((int) ch) {
			case 39:
				strB.append("'");
			default:
				strB.append(ch);
			}
		}

		return strB.toString();
	}

}
