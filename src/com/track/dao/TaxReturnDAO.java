package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class TaxReturnDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.TaxReturnDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TaxReturnDAO_PRINTPLANTMASTERLOG;

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

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	public boolean addTaxReturn(Hashtable<String, String> ht, String plant, String TABLE_NAME) throws Exception {
		boolean accountCreated = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();
			connection = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			query = "INSERT INTO [" + plant + "_"+TABLE_NAME+"] (" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1) + ")";

			if (connection != null) {
				/* Create PreparedStatement object */
				ps = connection.prepareStatement(query);

				this.mLogger.query(this.printQuery, query);
				accountCreated = execute_NonSelectQuery(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return accountCreated;
	}
	
	public int addTaxReturnForSG(Hashtable<String, String> ht, String plant, String TABLE_NAME) throws Exception {
		int accountCreated = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();
			connection = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			query = "INSERT INTO [" + plant + "_"+TABLE_NAME+"] (" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1) + ")";

			if (connection != null) {
				/* Create PreparedStatement object */
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

				this.mLogger.query(this.printQuery, query);
				accountCreated = execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return accountCreated;
	}
	
	public int update(String query, Hashtable htCondition, String extCond, String TABLE_NAME)
            throws Exception {
    boolean flag = false;
    int upHdrId = 0;
    java.sql.Connection con = null;
    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE " + "["
                            + htCondition.get("PLANT") + "_" + TABLE_NAME + "]");
            sql.append(" ");
            sql.append(query);
            
            sql.append(" WHERE ");
            String conditon = formCondition(htCondition);
            sql.append(conditon);

            if (extCond.length() != 0) {
                    sql.append(extCond);
            }
            
            	flag = updateData(con, sql.toString());
            	if(flag)
            		upHdrId=1;
                this.mLogger.query(this.printQuery, sql.toString());
			

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return upHdrId;
}

	
	public boolean isExists(Hashtable ht,String TABLE_NAME, String plant, String extCond)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sQry = new StringBuffer("SELECT count(*) from "
					+plant+"_"+ TABLE_NAME);
			sQry.append(" WHERE ");
			String conditon = formCondition(ht);
			sQry.append(conditon);
			if (extCond.length() > 0) {
				sQry.append("  ");

				sQry.append(" " + extCond);
			}
			this.mLogger.query(this.printQuery, sQry.toString());
			ps = con.prepareStatement(sQry.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					isExists = true;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return isExists;
	}
	
	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" AND ");
				conditon = formCondition(ht);
				sql.append(" " + conditon);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}

			this.mLogger.query(this.printQuery, sql.toString());
			al = selectData(con, sql.toString());
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
	public boolean deleteInvoiceDet(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FINTAXFILEADJUSTMENT" + "]"
			                        + " WHERE ID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0)
			                deleteprdForTranId = true;
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deleteprdForTranId;
	}
	public List<Map<String, String>> getTaxAdjusment_Box7(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> taxReturnAdjusmentList=new ArrayList<>();
		String query="";
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINTAXFILEADJUSTMENT] "
					+ "WHERE PLANT ='"+ht.get("PLANT")+"' AND TAXHDR_ID='"+ht.get("TAXHDR_ID")+"'";
			//PreparedStatement ps = con.prepareStatement(query);  		
			this.mLogger.query(this.printQuery, query);			
			taxReturnAdjusmentList = selectData(con, query.toString());		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return taxReturnAdjusmentList;
	}

}
