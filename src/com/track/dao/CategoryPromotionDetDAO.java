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

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.DoDet;
import com.track.db.object.HrEmpType;
import com.track.db.object.PoDet;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoHdr;
import com.track.db.object.PosPrd_ClassPromotionDet;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CategoryPromotionDetDAO  extends BaseDAO {

	public static String TABLE_NAME = "POSPRD_CLASSPROMOTIONDET";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.CATEGORYPROMOTIONDETDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CATEGORYPROMOTIONDETDAO_PRINTPLANTMASTERLOG;

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

	public CategoryPromotionDetDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "POSPRD_CLASSPROMOTIONDET" + "]";
	}

	public CategoryPromotionDetDAO() {

	}
    
    public boolean addPosProductDet(PosPrd_ClassPromotionDet PoDet) throws Exception {
 		boolean insertFlag = false;
 		boolean flag = false;
 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+PoDet.getPLANT()+"_POSPRD_CLASSPROMOTIONDET]" + 
 					"           ([PLANT]" + 
 					"           ,[LNNO]" + 
 					"           ,[HDRID]" + 
 					"           ,[BUY_PRD_CLS_ID]" + 
 					"           ,[BUY_QTY]" + 
 					"           ,[GET_ITEM]" + 
 					"           ,[GET_QTY]" + 
 					"           ,[PROMOTION_TYPE]" + 
 					"           ,[PROMOTION]" + 
 					"           ,[LIMIT_OF_USAGE]" + 
 					"           ,[CRAT]" + 
 					"           ,[CRBY],[UPAT],[UPBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, PoDet.getPLANT());
	 			ps.setInt(2, PoDet.getLNNO());
	 			ps.setInt(3, PoDet.getHDRID());
	 			ps.setString(4, PoDet.getBUY_PRD_CLS_ID());
	 			ps.setString(5, PoDet.getBUY_QTY());
	 			ps.setString(6, PoDet.getGET_ITEM());
	 			ps.setBigDecimal(7, PoDet.getGET_QTY());
	 			ps.setString(8, PoDet.getPROMOTION_TYPE());
	 			ps.setBigDecimal(9, PoDet.getPROMOTION());
	 			ps.setDouble(10, PoDet.getLIMIT_OF_USAGE());
	 			ps.setString(11, PoDet.getCRAT());
	 			ps.setString(12, PoDet.getCRBY());
	 			ps.setString(13, PoDet.getUPAT());
	 			ps.setString(14, PoDet.getUPBY());


 			   int count=ps.executeUpdate();
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Product Promotion failed, no rows affected.");
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
 		return insertFlag;
 	}
    
	public boolean updatePoDetpoEdit(PosPrd_ClassPromotionDet PoDet,int polnno) throws Exception {
		boolean updateFlag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {
			connection = DbBean.getConnection();
			

				query = "UPDATE ["+PoDet.getPLANT()+"_POSPRD_CLASSPROMOTIONDET] SET " + 
						"           [PLANT] = ?" + 
						"           ,[LNNO] = ?" + 
	 					"           ,[HDRID] = ?" + 
	 					"           ,[BUY_PRD_CLS_ID] = ?" + 
	 					"           ,[BUY_QTY] = ?" + 
	 					"           ,[GET_ITEM] = ?" + 
	 					"           ,[GET_QTY] = ?" + 
	 					"           ,[PROMOTION_TYPE] = ?" + 
	 					"           ,[PROMOTION] = ?" + 
	 					"           ,[LIMIT_OF_USAGE] = ?" + 
	 					"           ,[UPAT] = ?" + 
	 					"           ,[UPBY] = ?" + 
						"     WHERE ID = ? AND LNNO = ? ";
				if(connection != null){
					ps = connection.prepareStatement(query);
				   	ps.setString(1, PoDet.getPLANT());
				   	ps.setInt(2, PoDet.getLNNO());
		 			ps.setInt(3, PoDet.getHDRID());
		 			ps.setString(4, PoDet.getBUY_PRD_CLS_ID());
		 			ps.setString(5, PoDet.getBUY_QTY());
		 			ps.setString(6, PoDet.getGET_ITEM());
		 			ps.setBigDecimal(7, PoDet.getGET_QTY());
		 			ps.setString(8, PoDet.getPROMOTION_TYPE());
		 			ps.setBigDecimal(9, PoDet.getPROMOTION());
		 			ps.setDouble(10, PoDet.getLIMIT_OF_USAGE());
		 			ps.setString(11, PoDet.getUPAT());
		 			ps.setString(12, PoDet.getUPBY());
		 			ps.setInt(13, PoDet.getID());
		 			ps.setInt(14, polnno);
		 			
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   updateFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Updating Purchase Order detail failed, no rows affected.");
				   }
				   this.mLogger.query(this.printQuery, query);		
				}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Item already added to out going order");
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return updateFlag;
	}
	
	public ArrayList  getPrdPrmDetById(String plant, int Id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ArrayList al = new ArrayList();
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,LNNO,HDRID,ISNULL(BUY_PRD_CLS_ID,'')BUY_PRD_CLS_ID,ISNULL(BUY_QTY,'')BUY_QTY,ISNULL(GET_ITEM,'')GET_ITEM,ISNULL(GET_QTY,'')GET_QTY,ISNULL(PROMOTION_TYPE,'')PROMOTION_TYPE,ISNULL(PROMOTION,'')PROMOTION,ISNULL(LIMIT_OF_USAGE,'')LIMIT_OF_USAGE,CRAT,CRBY,UPAT,UPBY" + 
			" FROM ["+ plant +"_POSPRD_CLASSPROMOTIONDET] WHERE HDRID='"+Id+"'";
			System.out.println(query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
//	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, posItemPromotionHdr);
	                }   
			this.mLogger.query(this.printQuery, query);
			al = selectData(connection, query);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		
		return al;
	}
	  
	public  PosPrd_ClassPromotionDet getPrdPrmDetById(String plant, int Id ,int poline)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ArrayList al = new ArrayList();
		String query = "";
		PosPrd_ClassPromotionDet posPrd_ClassPromotionDet=new PosPrd_ClassPromotionDet();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,LNNO,HDRID,ISNULL(BUY_PRD_CLS_ID,'')BUY_PRD_CLS_ID,ISNULL(BUY_QTY,'')BUY_QTY,ISNULL(GET_ITEM,'')GET_ITEM,ISNULL(GET_QTY,'')GET_QTY,ISNULL(PROMOTION_TYPE,'')PROMOTION_TYPE,ISNULL(PROMOTION,'')PROMOTION,ISNULL(LIMIT_OF_USAGE,'')LIMIT_OF_USAGE,CRAT,CRBY,UPAT,UPBY" + 
					" FROM ["+ plant +"_POSPRD_CLASSPROMOTIONDET] WHERE HDRID='"+Id+"' AND LNNO='"+poline+"'";
			System.out.println(query);
			if(connection != null){
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet rst = ps.executeQuery();
				while (rst.next()) {
//	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, posItemPromotionHdr);
				}   
				this.mLogger.query(this.printQuery, query);
				al = selectData(connection, query);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		
		return posPrd_ClassPromotionDet;
	}
	
	
	
	public ArrayList  getPrdPrmDetById(String plant, String Id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ArrayList al = new ArrayList();
		String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,LNNO,HDRID,ISNULL(BUY_PRD_CLS_ID,'')BUY_PRD_CLS_ID,ISNULL(BUY_QTY,'')BUY_QTY,ISNULL(GET_ITEM,'')GET_ITEM,ISNULL(GET_QTY,'')GET_QTY,ISNULL(PROMOTION_TYPE,'')PROMOTION_TYPE,ISNULL(PROMOTION,'')PROMOTION,ISNULL(LIMIT_OF_USAGE,'')LIMIT_OF_USAGE,CRAT,CRBY,UPAT,UPBY" + 
					" FROM ["+ plant +"_POSPRD_CLASSPROMOTIONDET] WHERE HDRID='"+Id+"'";
			System.out.println(query);
			if(connection != null){
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet rst = ps.executeQuery();
				while (rst.next()) {
//	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, posItemPromotionHdr);
				}   
				this.mLogger.query(this.printQuery, query);
				al = selectData(connection, query);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		
		return al;
	}
	
	  
	public boolean DeletePromotionDet(String plant, int Hdrid)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        String sQry = "DELETE FROM " + "[" + plant +"_POSPRD_CLASSPROMOTIONDET] WHERE HDRID='"+Hdrid+"'";
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
