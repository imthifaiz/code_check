package com.track.dao;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class CountryNCurrencyDAO extends BaseDAO {
	public static String TABLE_NAME = "CurrencyMst";
	
	private boolean printQuery = MLoggerConstant.COUNTRYNCURRENCYDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.COUNTRYNCURRENCYDAO_PRINTPLANTMASTERLOG;

	public boolean isPrintQuery() {
		return printQuery;
	}

	public void setPrintQuery(boolean printQuery) {
		this.printQuery = printQuery;
	}


	public boolean isPrintLog() {
		return printLog;
	}

	public void setPrintLog(boolean printLog) {
		this.printLog = printLog;
	}

	StrUtils _StrUtils = null;

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
 public CountryNCurrencyDAO ()
 {
	 
 }
	 public ArrayList<Map<String, String>> getCurrencyList(Hashtable ht) throws Exception {

	        java.sql.Connection con = null;
	        ArrayList<Map<String, String>> al = new ArrayList<>();
	        try {
	                con = com.track.gates.DbBean.getConnection();
	               
	                StringBuffer sQry = new StringBuffer("SELECT COUNTRY_NAME , CURRENCY_CODE  from "
	                                + "COUNTRY_CURRENCY");
	                if(!ht.isEmpty()){
	                	sQry.append(" WHERE  " + formCondition(ht));
	                }
	                this.mLogger.query(this.printQuery, sQry.toString());
	                System.out.println(sQry);
	                al =  selectData(con, sQry.toString());

	        } catch (Exception e) {
	                this.mLogger.exception(this.printLog, "", e);
	                throw e;
	        } finally {
	                if (con != null) {
	                        DbBean.closeConnection(con);
	                }
	        }
	        return al;
	}
	 
	 public ArrayList getCountryCurrency(String plant,String currency) throws Exception {

	        java.sql.Connection con = null;
	        ArrayList al = new ArrayList();
	        try {
	                con = com.track.gates.DbBean.getConnection();
	               
	                String sQry = "select COUNTRY_CODE from "
	                                + "["
	                                + plant
	                                + "_"
	                                + "COUNTRYMASTER] where plant='"
	                                + plant
	                                + "' and CURRENCY = '"+currency+"' "; 
	                this.mLogger.query(this.printQuery, sQry);
	                System.out.println(sQry);

	                al = selectData(con, sQry);

	        } catch (Exception e) {
	                this.mLogger.exception(this.printLog, "", e);
	                throw e;
	        } finally {
	                if (con != null) {
	                        DbBean.closeConnection(con);
	                }
	        }
	        return al;
	}

		
	 public ArrayList getCountryCodeByCountry(String plant,String country) throws Exception {

	        java.sql.Connection con = null;
	        ArrayList al = new ArrayList();
	        try {
	                con = com.track.gates.DbBean.getConnection();
	               
	                String sQry = "select COUNTRY_CODE from "
	                                + "["
	                                + plant
	                                + "_"
	                                + "COUNTRYMASTER] where plant='"
	                                + plant
	                                + "' and COUNTRYNAME = '"+country+"' "; 
	                this.mLogger.query(this.printQuery, sQry);
	                System.out.println(sQry);

	                al = selectData(con, sQry);

	        } catch (Exception e) {
	                this.mLogger.exception(this.printLog, "", e);
	                throw e;
	        } finally {
	                if (con != null) {
	                        DbBean.closeConnection(con);
	                }
	        }
	        return al;
	}
			public ArrayList getCountryCode(String country) throws Exception {
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				boolean flag = false;
				String sQry = "SELECT CURRENCY_CODE FROM "
						+"COUNTRY_CURRENCY " 
						+ "WHERE COUNTRY_NAME='"+country+"' ";
			
				this.mLogger.query(this.printQuery, sQry);
				al = selectData(con, sQry);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			
			return al;
		}

}
