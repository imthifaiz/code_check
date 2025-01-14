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

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.PoHdr;
import com.track.db.object.PosItemPromotionHdr;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PosItemPromotionHdrDAO extends BaseDAO {

	public static String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POSITEMPROMOTIONHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POSITEMPROMOTIONHDRDAO_PRINTPLANTMASTERLOG;

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

	public PosItemPromotionHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public PosItemPromotionHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_POSITEMPROMOTIONHDR" + "]";
	}

	public static String TABLE_NAME = "POSITEMPROMOTIONHDR";

	

	 public ArrayList selectForReport(String query, Hashtable ht, String extCond)
				throws Exception {
			boolean flag = false;
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
	 
    public int addPosProductHdrReturnKey(PosItemPromotionHdr poHdr) throws Exception {
 		int pohdrkey = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+poHdr.getPLANT()+"_POSITEMPROMOTIONHDR]" + 
 					"           ([PLANT]" + 
 					"           ,[PROMOTION_NAME]" +
 					"           ,[PROMOTION_DESC]" +
 					"           ,[CUSTOMER_TYPE_ID]" +
 					"           ,[START_DATE]" +
 					"           ,[START_TIME]" +
 					"           ,[END_DATE]" +
 					"           ,[END_TIME]" +
// 					"           ,[LIMIT_OF_USAGE]" +
 					"           ,[BY_VALUE]" +
 					"           ,[NOTES]" +
 					"           ,[IsActive]" +
 					"           ,[CRAT]" +
 					"           ,[CRBY]" +
 					"           ,[UPAT]" +
 					"           ,[UPBY]" +
 					"           ,[OUTLET]" +
 					"           ,[UNITPRICE]" +
 					"           ,[QTY]" +
 					"           ,[DISCOUNT]" +
 					"           ,[DISCOUNT_TYPE]" +
 					"           ,[LIMIT_OF_USAGE]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	 			ps.setString(1, poHdr.getPLANT());
	 			ps.setString(2, poHdr.getPROMOTION_NAME());
	 			ps.setString(3, poHdr.getPROMOTION_DESC());
	 			ps.setString(4, poHdr.getCUSTOMER_TYPE_ID());
	 			ps.setString(5, poHdr.getSTART_DATE());
	 			ps.setString(6, poHdr.getSTART_TIME());
	 			ps.setString(7, poHdr.getEND_DATE());
	 			ps.setString(8, poHdr.getEND_TIME());
//	 			ps.setDouble(9, poHdr.getLIMIT_OF_USAGE());
	 			ps.setInt(9, poHdr.getBY_VALUE());
	 			ps.setString(10, poHdr.getNOTES());
	 			ps.setString(11, poHdr.getIsActive());
	 			ps.setString(12, poHdr.getCRAT());
	 			ps.setString(13, poHdr.getCRBY());
	 			ps.setString(14, poHdr.getUPAT());
	 			ps.setString(15, poHdr.getUPBY());
	 			ps.setString(16, poHdr.getOUTLET());
	 			ps.setDouble(17, poHdr.getUNITPRICE());
	 			ps.setDouble(18, poHdr.getQTY());
	 			ps.setDouble(19, poHdr.getDISCOUNT());
	 			ps.setString(20, poHdr.getDISCOUNT_TYPE());
	 			ps.setDouble(21, poHdr.getLIMIT_OF_USAGE());

 			   int count=ps.executeUpdate();
				
 			   ResultSet rs = ps.getGeneratedKeys(); 
 			   if (rs.next()){ 
					  pohdrkey = rs.getInt(1); 
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
 		return pohdrkey;
 	}
    
    public boolean updateProductHdr(PosItemPromotionHdr poHdr,int id) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "UPDATE ["+poHdr.getPLANT()+"_POSITEMPROMOTIONHDR] SET" + 
					"           [PLANT] = ?" + 
					"           ,[PROMOTION_NAME] = ?" +
 					"           ,[PROMOTION_DESC] = ?" +
 					"           ,[CUSTOMER_TYPE_ID] = ?" +
 					"           ,[START_DATE] = ?" +
 					"           ,[START_TIME] = ?" +
 					"           ,[END_DATE] = ?" +
 					"           ,[END_TIME] = ?" +
// 					"           ,[LIMIT_OF_USAGE] = ?" +
 					"           ,[BY_VALUE] = ?" +
 					"           ,[NOTES] = ?" +
 					"           ,[IsActive] = ?" +
 					"           ,[UPAT] = ?" +
 					"           ,[UPBY] = ?" +
 					"           ,[OUTLET] = ?" +
 					"           ,[UNITPRICE] = ?" +
 					"           ,[QTY] = ?" +
 					"           ,[DISCOUNT] = ?" +
 					"           ,[DISCOUNT_TYPE] = ?" +
					"           ,[LIMIT_OF_USAGE] = ? WHERE [ID] = "+id+" ";

			if(connection != null){
				ps = connection.prepareStatement(query);
				ps.setString(1, poHdr.getPLANT());
				ps.setString(2, poHdr.getPROMOTION_NAME());
	 			ps.setString(3, poHdr.getPROMOTION_DESC());
	 			ps.setString(4, poHdr.getCUSTOMER_TYPE_ID());
	 			ps.setString(5, poHdr.getSTART_DATE());
	 			ps.setString(6, poHdr.getSTART_TIME());
	 			ps.setString(7, poHdr.getEND_DATE());
	 			ps.setString(8, poHdr.getEND_TIME());
//	 			ps.setDouble(9, poHdr.getLIMIT_OF_USAGE());
	 			ps.setInt(9, poHdr.getBY_VALUE());
	 			ps.setString(10, poHdr.getNOTES());
	 			ps.setString(11, poHdr.getIsActive());
	 			ps.setString(12, poHdr.getUPAT());
	 			ps.setString(13, poHdr.getUPBY());
	 			ps.setString(14, poHdr.getOUTLET());
	 			ps.setDouble(15, poHdr.getUNITPRICE());
	 			ps.setDouble(16, poHdr.getQTY());
	 			ps.setDouble(17, poHdr.getDISCOUNT());
	 			ps.setString(18, poHdr.getDISCOUNT_TYPE());
	 			ps.setDouble(19, poHdr.getLIMIT_OF_USAGE());
//	 			ps.setInt(15, poHdr.getID());
	 			
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   updateFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Updating Product Promotion failed, no rows affected.");
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
		return updateFlag;
	}
   
    public ArrayList getProductPromotionStartsWithName(String aCustName,
			String plant) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;	
		
		try {
			con = DbBean.getConnection();

//			String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,LIMIT_OF_USAGE,ISNULL(IsActive,'')IsActive  FROM "
					String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,ISNULL(IsActive,'')IsActive  FROM "
					+ "["
					+ plant
					+ "_"
					+ "POSITEMPROMOTIONHDR"
					+ "]"
					+ " WHERE PROMOTION_NAME LIKE '" + aCustName + "%' or PROMOTION_DESC LIKE '%" + aCustName + "%' ORDER BY PROMOTION_NAME ASC ";
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry); 
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
    
    
	public PosItemPromotionHdr getPosHdrById(String plant, int Id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PosItemPromotionHdr posItemPromotionHdr=new PosItemPromotionHdr();
		try {	    
			connection = DbBean.getConnection();
//			query = "SELECT PLANT,ID,PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,LIMIT_OF_USAGE,ISNULL(IsActive,'')IsActive FROM ["+ plant +"_POSITEMPROMOTIONHDR] WHERE ID='"+Id+"'";
			query = "SELECT PLANT,ID,PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,ISNULL(IsActive,'')IsActive FROM ["+ plant +"_POSITEMPROMOTIONHDR] WHERE ID='"+Id+"'";
			System.out.println(query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
//	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, posItemPromotionHdr);
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
		return posItemPromotionHdr;
	}
	
	public PosItemPromotionHdr getPosHdrById(String plant, String Id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		String query = "";
		PosItemPromotionHdr posItemPromotionHdr=new PosItemPromotionHdr();
		try {	    
			connection = DbBean.getConnection();
//			query = "SELECT PLANT,ID,PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,LIMIT_OF_USAGE,ISNULL(IsActive,'')IsActive FROM ["+ plant +"_POSITEMPROMOTIONHDR] WHERE ID='"+Id+"'";
			query = "SELECT PLANT,ID,PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,ISNULL(IsActive,'')IsActive FROM ["+ plant +"_POSITEMPROMOTIONHDR] WHERE ID='"+Id+"'";
			System.out.println(query);
			if(connection != null){
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet rst = ps.executeQuery();
				while (rst.next()) {
//	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, posItemPromotionHdr);
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
		return posItemPromotionHdr;
	}
	
	public ArrayList getProductPromotionDetails(String plant,String Id) throws Exception {

		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
//				sQry = "SELECT PLANT,ID,PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,LIMIT_OF_USAGE,ISNULL(IsActive,'')IsActive FROM ["+ plant +"_POSITEMPROMOTIONHDR] WHERE PLANT='"+plant+"' AND ID='"+Id+"'";
				sQry = "SELECT PLANT,ID,PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,ISNULL(IsActive,'')IsActive,ISNULL(OUTLET,'')OUTLET,ISNULL(UNITPRICE,0)UNITPRICE,ISNULL(QTY,0)QTY,ISNULL(DISCOUNT,0)DISCOUNT,ISNULL(DISCOUNT_TYPE,'')DISCOUNT_TYPE,ISNULL(LIMIT_OF_USAGE,0)LIMIT_OF_USAGE FROM ["+ plant +"_POSITEMPROMOTIONHDR] WHERE PLANT='"+plant+"' AND ID='"+Id+"'";
			
			
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
    
	public ArrayList getProductPromotionDetailsNew(String plant,String Id) throws Exception {
		
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String sQry;
			
//			SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,ISNULL(IsActive,'')IsActive,
//			STRING_AGG(OUTLET, ',') AS OUTLET ,ISNULL(UNITPRICE,0)UNITPRICE,ISNULL(QTY,0)QTY,ISNULL(DISCOUNT,0)DISCOUNT,ISNULL(DISCOUNT_TYPE,'')DISCOUNT_TYPE,ISNULL(LIMIT_OF_USAGE,0)LIMIT_OF_USAGE 
//			FROM [TEST_POSITEMPROMOTIONHDR] WHERE PLANT='TEST' AND ID in (3,4)
//			GROUP BY PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,IsActive,LIMIT_OF_USAGE,DISCOUNT_TYPE,DISCOUNT,QTY,UNITPRICE,NOTES,BY_VALUE;
			
			sQry = "SELECT PLANT,PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,ISNULL(NOTES,'')NOTES,ISNULL(BY_VALUE,'')BY_VALUE,START_DATE,START_TIME,END_DATE,END_TIME,"
					+ "ISNULL(IsActive,'')IsActive,ISNULL(UNITPRICE,0)UNITPRICE,ISNULL(QTY,0)QTY,ISNULL(DISCOUNT,0)DISCOUNT,ISNULL(DISCOUNT_TYPE,'')DISCOUNT_TYPE,ISNULL(LIMIT_OF_USAGE,0)LIMIT_OF_USAGE,"
					+ "STRING_AGG(OUTLET +'$'+ CAST(ID AS nvarchar), ',') AS OUTLET FROM ["+ plant +"_POSITEMPROMOTIONHDR] WHERE PLANT='"+plant+"' AND ID in("+Id+") "
					+ "GROUP BY PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,IsActive,LIMIT_OF_USAGE,DISCOUNT_TYPE,DISCOUNT,QTY,UNITPRICE,NOTES,BY_VALUE,PLANT;";
			
			
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
	
    
   	public ArrayList getProductPromotionsWithNameold(String Outlet,String prm,String prmdesc,String plant, String cond) throws Exception {
   		PreparedStatement ps = null;
   		ResultSet rs = null;
   		ArrayList arrList = new ArrayList();
   		Hashtable ht= new Hashtable();
   		Connection con = null;	
   		String ext="";
   		if(prm.equalsIgnoreCase("")) {
   		ext = " WHERE (PROMOTION_DESC LIKE '%" + prmdesc + "%') AND (ISNULL(OUTLET,'') LIKE '%" + Outlet + "%' )"  + cond;
   		}else {
   			ext = " WHERE (PROMOTION_NAME LIKE '%" + prm + "%') AND (ISNULL(OUTLET,'') LIKE '%" + Outlet + "%' )"  + cond;
   		}
   		try {
   			con = DbBean.getConnection();

//   			String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,LIMIT_OF_USAGE,ID,ISNULL(IsActive,'')IsActive  FROM "
   					String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,ID,ISNULL(IsActive,'')IsActive "
   					+ ",ISNULL((SELECT top 1 O.OUTLET_NAME FROM "+  plant +"_POSOUTLETS O WHERE O.OUTLET = A.OUTLET),'') OUTLET_NAME FROM "
   					+ "["
   					+ plant
   					+ "_"
   					+ "POSITEMPROMOTIONHDR"
   					+ "] A"
   					+ ext;
   			arrList = selectForReport(sQry, ht, "");
   			this.mLogger.query(this.printQuery, sQry);
   			
   		} catch (Exception e) {
   			this.mLogger.exception(this.printLog, "", e);
   		} finally {
   			DbBean.closeConnection(con, ps);
   		}
   		return arrList;

   	}
    
   	public ArrayList getProductPromotionsWithName(String Outlet,String prm,String prmdesc,String plant, String cond) throws Exception {
   		PreparedStatement ps = null;
   		ResultSet rs = null;
   		ArrayList arrList = new ArrayList();
   		Hashtable ht= new Hashtable();
   		Connection con = null;	
   		String ext="";
   		if(prm.equalsIgnoreCase("")) {
   			ext = " WHERE (PROMOTION_DESC LIKE '%" + prmdesc + "%') AND (ISNULL(OUTLET,'') LIKE '%" + Outlet + "%' )"  + cond;
   		}else {
   			ext = " WHERE (PROMOTION_NAME LIKE '%" + prm + "%') AND (ISNULL(OUTLET,'') LIKE '%" + Outlet + "%' )"  + cond;
   		}
   		try {
   			con = DbBean.getConnection();
   			
//   			String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,LIMIT_OF_USAGE,ID,ISNULL(IsActive,'')IsActive  FROM "
   			String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,ISNULL(IsActive,'')IsActive "
   					+ ",STRING_AGG(id, ',') AS ID,STRING_AGG(OUTLET, ',') AS OUTLET_NAME  FROM "
   					+ "["
   					+ plant
   					+ "_"
   					+ "POSITEMPROMOTIONHDR"
   					+ "] A"
   					+ ext;
   			arrList = selectForReport(sQry, ht, "");
   			this.mLogger.query(this.printQuery, sQry);
   			
   		} catch (Exception e) {
   			this.mLogger.exception(this.printLog, "", e);
   		} finally {
   			DbBean.closeConnection(con, ps);
   		}
   		return arrList;
   		
   	}
   	
	public ArrayList getProductPromotionsWithName(String aCustName,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

//			String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,LIMIT_OF_USAGE,ISNULL(IsActive,'')IsActive  FROM "
					String sQry = "SELECT PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,ISNULL(IsActive,'')IsActive  FROM "
					+ "["
					+ plant
					+ "_"
					+ "POSITEMPROMOTIONHDR"
					+ "]"
			+ " WHERE (PROMOTION_NAME LIKE '" + aCustName + "%' or PROMOTION_DESC LIKE '%" + aCustName + "%')"  + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	
	public boolean DeletePromotionHdr(String plant, int Hdrid)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        String sQry = "DELETE FROM " + "[" + plant +"_POSITEMPROMOTIONHDR] WHERE ID='"+Hdrid+"'";
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
    
}
