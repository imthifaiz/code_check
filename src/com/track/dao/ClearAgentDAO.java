package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

public class ClearAgentDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.CLEARAGENTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CLEARAGENTDAO_PRINTPLANTMASTERLOG;

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

	public ClearAgentDAO() {

	}


public ArrayList getClearingAgentDetails(String DesigID, String plant, String cond)
		throws Exception {
	
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		boolean flag = false;
		String sQry = "select distinct CLEARING_AGENT_ID,CLEARING_AGENT_NAME,ISACTIVE from "
				+ "["
				+ plant
				+ "_"
				+ "CLEARING_AGENT_MST] where CLEARING_AGENT_ID like '"
				+ DesigID +"%' OR CLEARING_AGENT_NAME LIKE '" + DesigID 
				+ "%' " + cond
				+ " ORDER BY CLEARING_AGENT_ID ";
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

public ArrayList getClearingAgentDetailsByTrasnport(String TRANSPORTID,String DesigID, String plant, String cond)
		throws Exception {
	
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		
		boolean flag = false;
		String sQry = "select distinct A.CLEARING_AGENT_ID,CLEARING_AGENT_NAME,M.CONTACTNAME,TRANSPORTID,ISNULL((SELECT TOP 1 T.TRANSPORT_MODE FROM "+plant+"_TRANSPORT_MODE_MST T WHERE T.ID=M.TRANSPORTID),'') AS TRANSPORT from "
				+ "["
				+ plant
				+ "_"
				+ "CLEARING_AGENT_MST] A JOIN "+plant+"_CLEARING_AGENT_TYPE_MST M ON A.CLEARING_AGENT_ID=M.CLEARING_AGENT_ID where ISACTIVE='Y' AND TRANSPORTID='"+TRANSPORTID+"' AND (A.CLEARING_AGENT_ID like '"
				+ DesigID +"%' OR CLEARING_AGENT_NAME LIKE '" + DesigID 
				+ "%') " + cond
				+ " ORDER BY CLEARING_AGENT_ID ";
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

public boolean insertClearagentMst(Hashtable ht) throws Exception {
	boolean insertedInv = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
		String FIELDS = "", VALUES = "";
		String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE((String) ht.get("PLANT"));//Get Country Based DateTime -Azees 06.22
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = StrUtils.fString((String) enum1.nextElement());
			String value = StrUtils.fString((String) ht.get(key));
			FIELDS += key + ",";
			VALUES += "'" + value + "',";
		}
		String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
				+ "CLEARING_AGENT_MST" + "]" + "("
				+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
		this.mLogger.query(this.printQuery, query);
		insertedInv = insertData(con, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);

		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return insertedInv;
}


public boolean updateClearingAgentMst(Hashtable htUpdate, Hashtable htCondition)
		throws Exception {
	boolean update = true;
	PreparedStatement ps = null;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		String sUpdate = " ", sCondition = " ";
		Enumeration enumUpdate = htUpdate.keys();
		for (int i = 0; i < htUpdate.size(); i++) {
			String key = StrUtils
					.fString((String) enumUpdate.nextElement());
			String value = StrUtils.fString((String) htUpdate.get(key));
			sUpdate += key.toUpperCase() + " = '" + value + "',";
		}

		Enumeration enumCondition = htCondition.keys();
		for (int i = 0; i < htCondition.size(); i++) {
			String key = StrUtils.fString((String) enumCondition
					.nextElement());
			String value = StrUtils.fString((String) htCondition.get(key));
			sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
					+ "' AND ";

		}
		sUpdate = (sUpdate.length() > 0) ? " SET "
				+ sUpdate.substring(0, sUpdate.length() - 1) : "";
		sCondition = (sCondition.length() > 0) ? " WHERE  "
				+ sCondition.substring(0, sCondition.length() - 4) : "";

		String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
				+ "CLEARING_AGENT_MST" + "]" + sUpdate + sCondition;
		this.mLogger.query(this.printQuery, stmt);
		ps = con.prepareStatement(stmt);
		int iCnt = ps.executeUpdate();
		if (iCnt > 0)
			update = true;

	} catch (Exception e) {
		throw e;
	} finally {
		DbBean.closeConnection(con, ps);
	}

	return update;
}

public boolean deleteClearingAgent(java.util.Hashtable ht) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "CLEARING_AGENT_MST"
				+ "]");
		sql.append(" WHERE " + formCondition(ht));

		this.mLogger.query(this.printQuery, sql.toString());
		delete = updateData(con, sql.toString());
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}

	return delete;
}

public boolean isExistsClearagent(Hashtable ht) throws Exception {

	boolean flag = false;
	java.sql.Connection con = null;

	try {
		con = com.track.gates.DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" 1 ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "CLEARING_AGENT_MST"
				+ "]");
		sql.append(" WHERE  " + formCondition(ht));
		this.mLogger.query(this.printQuery, sql.toString());
		flag = isExists(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return flag;
}



	public int getCountClearingAgentMst(Hashtable ht) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iCntItemmst = 0;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sQry = "SELECT COUNT(CLEARING_AGENT_NAME) FROM " + "["
					+ ht.get("PLANT") + "_CLEARING_AGENT_MST" + "]" + " WHERE "
					+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				iCntItemmst = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return iCntItemmst;
	}
	
	public String getClearingAgentByName(String plant, String agentname)throws Exception {
		java.sql.Connection connection = null;
	    String query = "";
	    String transfername= "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT CLEARING_AGENT_ID FROM ["+ plant +"_"+"CLEARING_AGENT_MST"+"] WHERE CLEARING_AGENT_NAME="+ "'" +agentname +"'" ;

			if(connection != null){
				Map m = this.getRowOfData(connection, query.toString());

				transfername = (String) m.get("CLEARING_AGENT_ID");
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return transfername;
	}

}
