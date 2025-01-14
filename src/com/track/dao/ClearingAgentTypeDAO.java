package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.object.ClearingAgentTypeDET;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.EmployeeLeaveDETpojo;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.LeaveTypePojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class ClearingAgentTypeDAO {
	public static String TABLE_HEADER = "CLEARING_AGENT_TYPE_MST";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.ClearAgentTypeDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ClearAgentTypeDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addClearAgentTypedet(ClearingAgentTypeDET ClearAgentTypedet) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ ClearAgentTypedet.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
				//	"           ,[ID]" + 
					"           ,[CLEARING_AGENT_ID]" +  
					"           ,[TRANSPORTID]" +
					"           ,[CONTACTNAME]" +
					"           ,[TELNO]" +
					"           ,[EMAIL]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, ClearAgentTypedet.getPLANT());
				//   ps.setInt(2, ClearAgentTypedet.getID());
				   ps.setString(2, ClearAgentTypedet.getCLEARING_AGENT_ID());
				   ps.setInt(3, ClearAgentTypedet.getTRANSPORTID());
				   ps.setString(4, ClearAgentTypedet.getCONTACTNAME());
				   ps.setString(5, ClearAgentTypedet.getTELNO());
				   ps.setString(6, ClearAgentTypedet.getEMAIL());
				   ps.setString(7, dateutils.getDate());
				   ps.setString(8, ClearAgentTypedet.getCRBY());
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	HdrId = rs.getInt(1);
		                    
		                }
				   }
				   else
				   {
					   throw new SQLException("Creating Clearing Agent type failed, no rows affected.");
				   }
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
		return HdrId;
	}
	
	public ArrayList getclearAgentDetails(String  custid, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select ID,ISNULL(CLEARING_AGENT_ID,'') CLEARING_AGENT_ID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(CONTACTNAME,'') CONTACTNAME,ISNULL(TELNO,'') TELNO,ISNULL(EMAIL,'') EMAIL"
					+ ",ISNULL(A.TRANSPORTID,0) TRANSPORTID,ISNULL ((SELECT TRANSPORT_MODE FROM " + plant +"_TRANSPORT_MODE_MST B where B.ID=A.TRANSPORTID),'') as TRANSPORT from "
					+ "["+ plant +"_CLEARING_AGENT_TYPE_MST] A where PLANT ='"	+ plant + "' AND CLEARING_AGENT_ID ='"	+ custid + "' "+ extraCon
					+ " ORDER BY ID ";
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
	
	public ArrayList<Map<String, String>> selectData(Connection conn, String query) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, String> map = null;

		ArrayList<Map<String, String>> arrayList = new ArrayList<>();
		try {

			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				map = new HashMap<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));
				}
				arrayList.add(map);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return arrayList;
	}
	
	
	public boolean DeleteClearagentdet(String plant, String empid)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"
			                        + " WHERE CLEARING_AGENT_ID ='"+empid+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0) {
			        	deletestatus = true;
			        }
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deletestatus;
 	}
	
	
	

	
	
	
	/*
	 * public List<ClearingAgentTypeDET> ClearAgentlistpojo(String plant, int
	 * empnoid, String leaveyear) throws Exception { Connection connection = null;
	 * PreparedStatement ps = null; String query = ""; List<ClearingAgentTypeDET>
	 * Clearagentlist=new ArrayList<ClearingAgentTypeDET>(); HrLeaveTypeDAO
	 * hrLeaveTypeDAO = new HrLeaveTypeDAO(); try { connection =
	 * DbBean.getConnection(); query = "SELECT * FROM ["+ plant
	 * +"_"+TABLE_HEADER+"] WHERE LEAVEYEAR='"+leaveyear+"' AND EMPNOID="+empnoid;
	 * 
	 * if(connection != null){ ps = connection.prepareStatement(query,
	 * Statement.RETURN_GENERATED_KEYS); ResultSet rst = ps.executeQuery(); while
	 * (rst.next()) { ClearingAgentTypeDET clearingAgentTypeDET=new
	 * ClearingAgentTypeDET(); ResultSetToObjectMap.loadResultSetIntoObject(rst,
	 * clearingAgentTypeDET); // HrLeaveType leavetypename =
	 * hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDET.getLEAVETYPEID());
	 * if(clearingAgentTypeDET.getTRANSPORTID() > 0) { ClearingAgentTypeDET
	 * ClearingAgentTypeDET = new ClearingAgentTypeDET();
	 * ClearingAgentTypeDET.setPLANT(clearingAgentTypeDET.getPLANT()); //
	 * ClearingAgentTypeDET.setID(employeeLeaveDET.getID()); //
	 * ClearingAgentTypeDET.setEMPNOID(employeeLeaveDET.getEMPNOID()); //
	 * ClearingAgentTypeDET.setLEAVETYPE(leavetypename.getLEAVETYPE());
	 * ClearingAgentTypeDET.setCONTACTNAME(clearingAgentTypeDET.getCONTACTNAME());
	 * ClearingAgentTypeDET.setTELNO(clearingAgentTypeDET.getTELNO());
	 * ClearingAgentTypeDET.setEMAIL(clearingAgentTypeDET.getEMAIL()); //
	 * ClearingAgentTypeDET.setLEAVEBALANCE(clearingAgentTypeDET.getLEAVEBALANCE());
	 * // ClearingAgentTypeDET.setNOTE(clearingAgentTypeDET.getNOTE()); //
	 * ClearingAgentTypeDET.setLOP(Integer.valueOf(leavetypename.getISNOPAYLEAVE()))
	 * ; Clearagentlist.add(ClearingAgentTypeDET); }else {
	 * 
	 * }
	 * 
	 * } this.mLogger.query(this.printQuery, query);
	 * 
	 * } } catch (Exception e) { this.mLogger.exception(this.printLog, "", e);
	 * 
	 * throw e; } finally { if (connection != null) {
	 * DbBean.closeConnection(connection); } } return Clearagentlist; }
	 */
	
	
	
	public boolean isExists(Connection conn, String sql) throws Exception {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					exists = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return exists;
	}
	
	
}
