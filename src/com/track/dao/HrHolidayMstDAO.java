package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HolidayMstPojo;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrHolidayMst;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

public class HrHolidayMstDAO {

	public static String TABLE_HEADER = "HRHOLIDAYMST";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrHolidayMstDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrHolidayMstDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addHoliday(HrHolidayMst Holiday) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ Holiday.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[HOLIDAY_DATE]" + 
					"           ,[HOLIDAY_DESC]" + 
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, Holiday.getPLANT());
				   ps.setString(2, Holiday.getHOLIDAY_DATE());
				   ps.setString(3, Holiday.getHOLIDAY_DESC());
				   ps.setString(4, DateUtils.getDateTime());
				   ps.setString(5, Holiday.getCRBY());
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
					   throw new SQLException("Creating holidays failed, no rows affected.");
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
	
	public List<HrHolidayMst> getAllHoliday(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrHolidayMst> HrHolidayMstList=new ArrayList<HrHolidayMst>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrHolidayMst hrHolidayMst=new HrHolidayMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrHolidayMst);
	                   HrHolidayMstList.add(hrHolidayMst);
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
		return HrHolidayMstList;
	}
	
	
	public HrHolidayMst getHolidayById(String plant, int id)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrHolidayMst hrHolidayMst=new HrHolidayMst();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrHolidayMst);
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
		return hrHolidayMst;
	}
	
	public int updateHoliday(HrHolidayMst Holiday, String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ Holiday.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+Holiday.getPLANT()+"',HOLIDAY_DATE='"+Holiday.getHOLIDAY_DATE()+"',HOLIDAY_DESC='"+Holiday.getHOLIDAY_DESC()+"',UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+Holiday.getID();
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
					   throw new SQLException("Updating holiday master failed, no rows affected.");
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
	
	public boolean DeleteHoliday(String plant, int id)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"
			                        + " WHERE ID ='"+id+"'";
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
	
	public boolean IsHoliday(String plant, String holidaydate) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
	    HrEmpType hrEmpType=new HrEmpType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HOLIDAY_DATE='"+holidaydate+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   status = true;
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
		return status;
	}
	
	public List<HrHolidayMst> IsHolidaylist(String plant, String holidaydate)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrHolidayMst> HrHolidayMstList=new ArrayList<HrHolidayMst>();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HOLIDAY_DATE='"+holidaydate+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrHolidayMst hrHolidayMst=new HrHolidayMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrHolidayMst);
	                   HrHolidayMstList.add(hrHolidayMst);
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
		return HrHolidayMstList;
	}
	
	
	public boolean IsHolidayedit(String plant, String id, String holidaydate) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
	    HrEmpType hrEmpType=new HrEmpType();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE HOLIDAY_DATE='"+holidaydate+"' AND ID !='"+id+"'";
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   status = true;
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
		return status;
	}
	
	public List<HolidayMstPojo> getHolidaybydate(String plant,String fromdate, String todate) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HolidayMstPojo> HolidayMstPojoList=new ArrayList<HolidayMstPojo>();
	    List<HolidayMstPojo> HolidayMstPojofilter=new ArrayList<HolidayMstPojo>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrHolidayMst hrHolidayMst=new HrHolidayMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrHolidayMst);
	                   HolidayMstPojo HolidayMstPojo =new HolidayMstPojo();
	                   HolidayMstPojo.setHDATE(datetiint(hrHolidayMst.getHOLIDAY_DATE()));
	                   HolidayMstPojo.setHOLIDAY_DATE(hrHolidayMst.getHOLIDAY_DATE());
	                   HolidayMstPojo.setHOLIDAY_DESC(hrHolidayMst.getHOLIDAY_DESC());
	                   HolidayMstPojoList.add(HolidayMstPojo);
	                }  
				   
				   int hfromdate = datetiint(fromdate);
				   int htodate = datetiint(todate);
		           HolidayMstPojofilter = HolidayMstPojoList.stream().filter((a) -> a.getHDATE() >= hfromdate && htodate >= a.getHDATE()).collect(Collectors.toList());
		            
		            
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
		return HolidayMstPojofilter;
	}
	
	public int datetiint(String holiday) {
         String hdatesplit[] = holiday.split("/");
         String hdate = hdatesplit[2]+hdatesplit[1]+hdatesplit[0];
         return Integer.valueOf(hdate);
	}
	
}
