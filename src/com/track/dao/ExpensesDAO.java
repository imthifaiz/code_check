package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.track.constants.MLoggerConstant;
import com.track.db.object.CostofgoodsLanded;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ExpensesDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.ExpensesDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ExpensesDAO_PRINTPLANTMASTERLOG;

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
	
	public int addexpenseHdr(Hashtable ht, String plant)throws Exception {
//		boolean flag = false;
		int addexpenseHdrHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
		try {
			 /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "?,";
			}
			query = "INSERT INTO ["+ plant +"_FINEXPENSESHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			addexpenseHdrHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return addexpenseHdrHdrId;
	}
	public boolean addMultipleExpenseDet(List<Hashtable<String, String>> expenseDetInfoList, String plant) 
			throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> expenseDetInfo : expenseDetInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = expenseDetInfo.keys();
				for (int i = 0; i < expenseDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) expenseDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINEXPENSESDET]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return flag;		
	}
	
	public boolean addExpenseAttachments(List<Hashtable<String, String>>  expenseAttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> expenseDetInfo : expenseAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = expenseDetInfo.keys();
				for (int i = 0; i < expenseDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) expenseDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINEXPENSESATTACHMENTS]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
				}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return flag;
	}
	
	public String GetHDRMAXId(String aPlant)
	{	
		String ExpId = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);

		String query = "ISNULL(MAX(ID),1) as ID";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"FINEXPENSESHDR",aPlant);

		ExpId = (String) m.get("ID");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return ExpId;
	}
	public Map selectRow(String query, Hashtable ht, String TABLE_NAME, String aPlant) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+aPlant+"_"+ TABLE_NAME);
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(this.printQuery, sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return map;
	}
	public int update(String query, Hashtable htCondition, String extCond)
            throws Exception {
    boolean flag = false;
    int expenseHdrId = 0;
    java.sql.Connection con = null;
    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE " + "["
                            + htCondition.get("PLANT") + "_FINEXPENSESHDR"+"]");
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
            		expenseHdrId=1;
                this.mLogger.query(this.printQuery, sql.toString());
			

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return expenseHdrId;
}
	public boolean deleteExpenseDet(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FINEXPENSESDET" + "]"
			                        + " WHERE EXPENSESHDRID ='"+TranId+"'";
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
	
	 public List getExpenseAttachByHrdId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> AttachList = new ArrayList<>();
//			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT * FROM"
						+ "[" + ht.get("PLANT") + "_FINEXPENSESATTACHMENTS] WHERE EXPENSESHDRID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));

				// this.mLogger.query(this.printQuery, query);

				AttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return AttachList;
		}
	 public List getExpenseAttachByPrimId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> AttachList = new ArrayList<>();
//			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT * FROM"
						+ "[" + ht.get("PLANT") + "_FINEXPENSESATTACHMENTS] WHERE ID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));

				// this.mLogger.query(this.printQuery, query);

				AttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return AttachList;
		}
	 public int deleteExpenseAttachByPrimId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
//			Map<String, String> map = null;
			String query="";
			int count=0;
			try {			
			    
				con = com.track.gates.DbBean.getConnection();
				query="DELETE FROM " + "[" + ht.get("PLANT") + "_FINEXPENSESATTACHMENTS] "
						+ "WHERE ID = ? AND PLANT = ?";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				ps.setString(1, (String) ht.get("ID"));
				ps.setString(2, (String) ht.get("PLANT"));	
				
				//this.mLogger.query(this.printQuery, query);	
				count=ps.executeUpdate();
				
			} catch (Exception e) {
				//this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return count;
		}
	 
	 public List getExpensehdrbyponoandshipment(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> AttachList = new ArrayList<>();
//			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT * FROM"
						+ "[" + ht.get("PLANT") + "_FINEXPENSESHDR] WHERE SHIPMENT_CODE = ? AND PONO =?";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add((String) ht.get("SHIPMENT_CODE"));
				args.add((String) ht.get("PONO"));

				// this.mLogger.query(this.printQuery, query);

				AttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return AttachList;
		}
	 
	 public List<String> getExpesesHDR(String pono,String plant,String shipCode) {
			List<String> mainObj=new ArrayList<>();
			String query = "SELECT ID FROM [" + plant + "_FINEXPENSESHDR] WHERE PONO ='"+ pono + "' AND SHIPMENT_CODE='"+shipCode+"' ";
			Connection con = null;
			try {
				con = DbBean.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rset = stmt.executeQuery(query);
				while (rset.next()) {
					mainObj.add(rset.getString("id"));
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return mainObj;
		}
	 
	 public List<CostofgoodsLanded> getExpesesDET(List<String> expensesid,String plant) {
			List<CostofgoodsLanded> lstObj=new ArrayList<>();
			CostofgoodsLanded dbObj=null;
			StringBuffer expenseshdrid=new StringBuffer();
			for(int i=0;i<expensesid.size();i++) {
				expenseshdrid.append("'").append(expensesid.get(i)).append("'").append(",");;
			}
			expenseshdrid.deleteCharAt(expenseshdrid.length() - 1);
			String query = "SELECT EXPENSES_ACCOUNT,EXPENSESHDRID,AMOUNT FROM [" + plant + "_FINEXPENSESDET] WHERE EXPENSESHDRID IN (" + expenseshdrid.toString() + ")";
			Connection con = null;
			try {
				con = DbBean.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rset = stmt.executeQuery(query);
				while (rset.next()) {
					dbObj=new CostofgoodsLanded();
					dbObj.setAccount_name(rset.getString("EXPENSES_ACCOUNT"));
					dbObj.setAmount(Double.parseDouble(rset.getString("AMOUNT")));
					lstObj.add(dbObj);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return lstObj;
		}
	 
	 

}
