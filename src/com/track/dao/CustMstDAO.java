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
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

public class CustMstDAO extends BaseDAO {
	public static String TABLE_NAME = "CustMst";
	public String plant = "";
	private boolean printQuery = MLoggerConstant.CUSTMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CUSTMSTDAO_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CUSTMSTDAO_PRINTPLANTMASTERINFO;

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

	public CustMstDAO() {
		// this._StrUtils = new StrUtils();
	}

	public CustMstDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + TABLE_NAME + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ TABLE_NAME);
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
	public Map selectRowByCmpy(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "+
					"["+ht.get("plant")+ "_"+TABLE_NAME+"]");
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
	public String getCustomerId(String Plant)
	{
		String sCustCode="";
		String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
		TblControlDAO _TblControlDAO =new TblControlDAO();
		_TblControlDAO.setmLogger(mLogger);
		Hashtable  ht=new Hashtable();
		     
		String query=" isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT,Plant);
		ht.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
		try{
		  boolean exitFlag=false; boolean resultflag=false;
		  exitFlag=_TblControlDAO.isExisit(ht,"", Plant);
		  if (exitFlag==false)
		  { 
		                    
		   	Map htInsert=null;
		   	Hashtable htTblCntInsert  = new Hashtable();
		        
		   	htTblCntInsert.put(IDBConstants.PLANT,Plant);
		   	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
		   	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"C");
		    htTblCntInsert.put("MINSEQ","0000");
		    htTblCntInsert.put("MAXSEQ","9999");
		    htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
		    htTblCntInsert.put(IDBConstants.CREATED_BY, "");
		    htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
		    insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,Plant);
		    sCustCode="C"+"0001";
		   }
		   else
		   {
		           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
		         
		     Map m= _TblControlDAO.selectRow(query, ht,"");
		     sBatchSeq=(String)m.get("NXTSEQ");
		          
		     int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
		          
		     String updatedSeq=Integer.toString(inxtSeq);
		     if(updatedSeq.length()==1)
		     {
		         sZero="000";
		     }
		     else if(updatedSeq.length()==2)
		     {
		       	  sZero="00";
		     }
		     else if(updatedSeq.length()==3)
		     {
		           sZero="0";
		     }
		           
		          
		     Map htUpdate = null;
		          
		     Hashtable htTblCntUpdate = new Hashtable();
		     htTblCntUpdate.put(IDBConstants.PLANT,Plant);
		     htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
		     htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"C");
		     StringBuffer updateQyery=new StringBuffer("set ");
		     updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
		   
		     boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",Plant);
		     sCustCode="C"+sZero+updatedSeq;
		    }
		   } catch(Exception e)
		   {
		   		mLogger.exception(true,
		    	"ERROR IN PAGE - MobileCustomerRegister ", e);
		   }
     return sCustCode;
		
	}
	public String getCustName(String aPlant, String aCustCode) throws Exception {

		String custName = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("custNo", aCustCode);
		TABLE_NAME = aPlant+"_CustMst";
		String query = " isNull(cName,'') as custName ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht);

		custName = (String) m.get("custName");
		TABLE_NAME = "CustMst";
		return custName;
	}
	
	public String getCustEmail(String aPlant, String aCustCode) throws Exception {

		String custemail = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("custNo", aCustCode);
		TABLE_NAME = aPlant+"_CustMst";
		String query = " isNull(CUSTOMEREMAIL,'') as CUSTOMEREMAIL ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht);

		custemail = (String) m.get("CUSTOMEREMAIL");
		TABLE_NAME = "CustMst";
		return custemail;
	}
	
	public String getCustBank(String aPlant, String aCustCode) throws Exception {

		String custName = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("custNo", aCustCode);
		TABLE_NAME = aPlant+"_CustMst";
		String query = " isNull(BANKNAME,'') as BANKNAME ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht);

		custName = (String) m.get("BANKNAME");
		TABLE_NAME = "CustMst";
		return custName;
	}
	public String getCustByIDORHP(String aPlant, String aCustCode) throws Exception {

		ArrayList alData =new ArrayList();
		java.sql.Connection con = null;
		String custid="";
		StringBuffer sql = new StringBuffer(" SELECT isnull(custno,'') custno from "+
				"["+aPlant+"_"+ TABLE_NAME+"]");
				try {
			con = com.track.gates.DbBean.getConnection();			
				sql.append(" WHERE ");				
				sql.append(" CUSTNO='"+aCustCode+ "'");
				sql.append("OR  HPNO='"+aCustCode+ "'");
			this.mLogger.query(this.printQuery, sql.toString());
			
			alData = selectData(con, sql.toString());
			if(alData.size()>0){
			Map lineArr = (Map)alData.get(0);
			custid = (String)lineArr.get("custno");}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return custid;
	}
	public String getCustByRow(String aPlant, String aCustCode,String colName,String selCol) throws Exception {

		String custName = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put(colName, aCustCode);

		String query = " isNull(cName,'') as custName,isNull(EMAIL,'') as EMAIL ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRowByCmpy(query, ht);

		custName = (String) m.get(selCol);
		return custName;
	}

	public String getNextCustCode(String aPlant) throws Exception {

		String defaultCustCode = "C10000000";
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);

		String query = " ISNULL(max(custNo),'') as custCode ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht);

		custCode = (String) m.get("custCode");

		try {
			if (custCode == "" || custCode == null || custCode.length() <= 0
					|| custCode == "0") {

				return defaultCustCode;
			} else {
				custCode = custCode.replace('C', '0');
				int temp = Integer.parseInt(custCode);
				temp = temp + 1;
				custCode = "C" + String.valueOf(temp);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return custCode;
	}

	public ArrayList selectCustMst(String query, Hashtable ht) throws Exception {

		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ TABLE_NAME);
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}

	public ArrayList selectCustMst(String query, Hashtable ht, String extCondi)
			throws Exception {

		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		TABLE_NAME="CUSTMST";
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "["+ht.get(IDBConstants.PLANT)+"_"+TABLE_NAME+"]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			if (extCondi.length() > 0)
				sql.append(" and " + extCondi);
		
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return alData;
	}

	public boolean isExisit(Hashtable ht, String extCond) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + ht.get(IDBConstants.PLANT)+"_"+TABLE_NAME);
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append(" and " + extCond);

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

	public boolean isExisit(String sql) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
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

	public boolean insertCustMst(Hashtable ht) throws Exception {

		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + TABLE_NAME + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

		return insertFlag;
	}

	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + TABLE_NAME);
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");
			String conditon = formCondition(htCondition);
			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}

			flag = updateData(con, sql.toString());
			this.mLogger.query(this.printQuery, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return flag;
	}
	 public boolean updateMst(Hashtable htUpdate, Hashtable htCondition)
		throws Exception {
			boolean update = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sUpdate = " ", sCondition = " ";
			
				// generate the condition string
				Enumeration enumUpdate = htUpdate.keys();
				for (int i = 0; i < htUpdate.size(); i++) {
					String key = StrUtils
							.fString((String) enumUpdate.nextElement());
					String value = StrUtils.fString((String) htUpdate.get(key));
					sUpdate += key.toUpperCase() + " = '" + value + "',";
				}
			
				// generate the update string
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
						+ TABLE_NAME + "] " + sUpdate + sCondition;
				this.mLogger.query(this.printQuery, stmt);
				ps = con.prepareStatement(stmt);
				int iCnt = ps.executeUpdate();
				if (iCnt > 0)
					update = true;
			
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			
			return update;
		}
	public boolean isExisitEmail(Hashtable ht, String extCond) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			 sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "CUSTMST" + "]");
			sql.append(" WHERE  " + formCondition(ht));
			if (extCond.length() > 0)
				sql.append(" and " + extCond);

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
	
	public ArrayList getCustomerForReportPgn(Hashtable ht,String productDesc,int start,int end) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
	    String extCond = "";
	    String custtypecond="";
	    
	    
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND CNAME in (select CNAME from "+ht.get("M.PLANT")+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		      if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
                       extCond = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
                }
		                       
                String condition = "";
            	if (ht.size() > 0) {
   				  condition = formCondition(ht);
    			}
            
            	String  sQryCondition = " WHERE ID >="+start+" and ID<="+end+" ";
                /*StringBuffer sql = new StringBuffer(" SELECT DISTINCT ITEM,(select isnull(ITEMDESC,'') from [" + ht.get("PLANT") +"_itemmst] where item=a.item)ITEMDESC,CNAME,'' AS CUSTCODE,SUM(PICKQTY)PICKQTY ");  
                sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "SHIPHIS" + "] A");
                sql.append("  WHERE  DONO<>'MISC-ISSUE' AND STATUS='C' AND " + condition + extCond +custtypecond);
               	sql.append(" GROUP BY ITEM,CNAME ");*/
            	
            	StringBuffer sql = new StringBuffer("SELECT * FROM (SELECT (ROW_NUMBER() OVER ( ORDER BY I.ITEM)) AS ID,ISNULL(C.CUSTOMER_TYPE_ID,'') AS CUSTTYPE,ISNULL(C.CUSTNO,'') AS CUSTID,ISNULL(C.CNAME,'') AS CUSTOMERNAME,ISNULL(I.ITEM,'') AS ITEM,ISNULL(I.UnitPrice,'') AS UnitPrice,ISNULL(I.ITEMDESC,'') AS ITEMDESC,ISNULL(M.OBDISCOUNT,'0.00') AS OBDISCOUNT");
            	sql.append(" FROM [" + ht.get("M.PLANT") +"_MULTI_PRICE_MAPPING] M JOIN [" + ht.get("M.PLANT") +"_CUSTMST] C ON M.CUSTOMER_TYPE_ID = C.CUSTOMER_TYPE_ID");
            	sql.append(" JOIN [" + ht.get("M.PLANT") +"_ITEMMST] I ON M.ITEM = I.ITEM WHERE " + condition + extCond +custtypecond);
            	sql.append(") A" +sQryCondition +" ORDER BY CUSTTYPE,CUSTID,ITEM");
                
		
		try {
			con = com.track.gates.DbBean.getConnection();
		
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;

	}
	
	public ArrayList getCustomerCount(Hashtable ht,String productDesc) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
	    String extCond = "";
	    String custtypecond="";
	    
	    
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND CNAME in (select CNAME from "+ht.get("M.PLANT")+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		      if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
                       extCond = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
                }
		                       
                String condition = "";
            	if (ht.size() > 0) {
   				  condition = formCondition(ht);
    			}
            
                   	
            	StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS RECORDS FROM (SELECT ISNULL(C.CUSTOMER_TYPE_ID,'') AS CUSTTYPE,ISNULL(C.CUSTNO,'') AS CUSTID,ISNULL(C.CNAME,'') AS CUSTOMERNAME,ISNULL(I.ITEM,'') AS ITEM,ISNULL(I.UnitPrice,'') AS UnitPrice,ISNULL(I.ITEMDESC,'') AS ITEMDESC,ISNULL(M.OBDISCOUNT,'0.00') AS OBDISCOUNT");
            	sql.append(" FROM [" + ht.get("M.PLANT") +"_MULTI_PRICE_MAPPING] M JOIN [" + ht.get("M.PLANT") +"_CUSTMST] C ON M.CUSTOMER_TYPE_ID = C.CUSTOMER_TYPE_ID");
            	sql.append(" JOIN [" + ht.get("M.PLANT") +"_ITEMMST] I ON M.ITEM = I.ITEM WHERE " + condition + extCond +custtypecond);
            	sql.append(") A");
                
		
		try {
			con = com.track.gates.DbBean.getConnection();
		
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;

	}
	
	public ArrayList getCustomerForReport(Hashtable ht,String productDesc) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
	    String extCond = "";
	    String custtypecond="";
	    
	    
		
		if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND CNAME in (select CNAME from "+ht.get("M.PLANT")+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}
		      if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
                       extCond = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
                }
		                       
                String condition = "";
            	if (ht.size() > 0) {
   				  condition = formCondition(ht);
    			}
            	
            	StringBuffer sql = new StringBuffer("SELECT ISNULL(C.CUSTOMER_TYPE_ID,'') AS CUSTTYPE,ISNULL(C.CUSTNO,'') AS CUSTID,ISNULL(C.CNAME,'') AS CUSTOMERNAME,ISNULL(I.UnitPrice,'') AS UnitPrice,ISNULL(I.ITEM,'') AS ITEM,ISNULL(I.ITEMDESC,'') AS ITEMDESC,ISNULL(M.OBDISCOUNT,'0.00') AS OBDISCOUNT");
            	sql.append(" FROM [" + ht.get("M.PLANT") +"_MULTI_PRICE_MAPPING] M JOIN [" + ht.get("M.PLANT") +"_CUSTMST] C ON M.CUSTOMER_TYPE_ID = C.CUSTOMER_TYPE_ID");
            	sql.append(" JOIN [" + ht.get("M.PLANT") +"_ITEMMST] I ON M.ITEM = I.ITEM WHERE " + condition + extCond +custtypecond);
            	sql.append(" ORDER BY CUSTTYPE,CUSTID,ITEM");
                
		
		try {
			con = com.track.gates.DbBean.getConnection();
		
			this.mLogger.query(this.printQuery, sql.toString());
			alData = selectData(con, sql.toString());
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;

	}
	
	public ArrayList getTopCustomer(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = " SELECT TOP 5 A.CUSTCODE,A.CUSTNAME,SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+"))) AS TOTAL_PRICE FROM  " + 
					" ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO " + 
					" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'"+
					" GROUP BY A.CUSTCODE,A.CUSTNAME ORDER BY TOTAL_PRICE DESC";
			al = selectData(con, aQuery.toString());
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
	
	public ArrayList getNewCustomers(String plant) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT TOP 5 CUSTNO,CNAME,NAME,HPNO,EMAIL FROM ["+plant+"_CUSTMST] "
					+ "WHERE ISNULL(CRAT,'') <> '' ORDER BY CAST((SUBSTRING(CRAT, 1, 4) + '-' +" + 
					" SUBSTRING(CRAT, 5, 2) + '-' + SUBSTRING(CRAT, 7, 2)) AS date) DESC";
			al = selectData(con, aQuery.toString());
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
	
	public ArrayList getTotalCustomers(String plant) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT COUNT(*) AS TOTAL_CUSTOMERS FROM ["+plant+"_CUSTMST]";
			al = selectData(con, aQuery.toString());
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
	public JSONObject getCustomerName(String plant, String name) {
		JSONObject mainObj = new JSONObject();
		String query = "SELECT * FROM [" + plant + "_CUSTMST] WHERE CUSTNO = '" + name + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				
				mainObj.put("CUSTNO", rset.getString("CUSTNO"));
				mainObj.put("CNAME", rset.getString("CNAME"));
			}
		} catch (NamingException | SQLException e) {
			mainObj.put("responseText", "failed");
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return mainObj;
	}
	public String getCustomerNameByNo(Hashtable ht)
			throws Exception {
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			String vendorName = "";
			try {
			con = DbBean.getConnection();
			String query = "SELECT CUSTOMER_TYPE_DESC FROM " + "[" + ht.get("PLANT") + "_CUSTOMER_TYPE_MST] "
			+ "WHERE CUSTOMER_TYPE_ID=? AND PLANT= ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,(String) ht.get("CUSTOMER_TYPE_ID"));
			stmt.setString(2,(String) ht.get("PLANT"));
			ResultSet rs=stmt.executeQuery();
			while(rs.next()){
				vendorName = rs.getString("CUSTOMER_TYPE_DESC");
			}
			} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			}finally {
			if (con != null) {
			DbBean.closeConnection(con);
			}
			}
			return vendorName;
			}
	
	
	public String getCustomerNameBycode(String plant, String code) {
		String name = "";
		String query = "SELECT * FROM [" + plant + "_CUSTMST] WHERE CUSTNO = '" + code + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				name = rset.getString("CNAME");
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return name;
	}
	
	public String getCustomerEmailBycode(String plant, String code) {
		String EMAIL = "";
		String query = "SELECT ISNULL(CUSTOMEREMAIL,'') AS CUSTOMEREMAIL FROM [" + plant + "_CUSTMST] WHERE CUSTNO = '" + code + "'";
		Connection con = null;
		try {
			con = DbBean.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				EMAIL = rset.getString("CUSTOMEREMAIL");
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return EMAIL;
	}
	
	public String getCustomerNoByName(Hashtable ht)
			throws Exception {
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			String vendorName = "";
			try {
			con = DbBean.getConnection();
			String query = "SELECT CUSTNO FROM " + "[" + ht.get("PLANT") + "_CUSTMST] "
			+ "WHERE CNAME=? AND PLANT= ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,(String) ht.get("CNAME"));
			stmt.setString(2,(String) ht.get("PLANT"));
			ResultSet rs=stmt.executeQuery();
			while(rs.next()){
				vendorName = rs.getString("CUSTNO");
			}
			} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			}finally {
			if (con != null) {
			DbBean.closeConnection(con);
			}
			}
			return vendorName;
			}
	
	public boolean addcustomerUser(Hashtable<String, String>  ht, String plant)throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO CUSTOMER_INFO ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

		return insertFlag;
	}
	public boolean isExistsCustomerUser(String aCustomer, String plant)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COUNT(*) FROM " + "[CUSTOMER_INFO]" + " WHERE USER_ID = '"
					+ aCustomer.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
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
	
	public ArrayList getCustomerUserDetails(String  custid, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select ID,ISNULL(USER_ID,'') USER_ID,ISNULL(USER_NAME,'') USER_NAME,ISNULL(USER_HPNO,'') USER_HPNO,ISNULL(USER_EMAIL,'') USER_EMAIL,ISNULL(PASSWORD,'') PASSWORD,CUSTNO,ISNULL(CUSTDESC,'') CUSTDESC,ISNULL(ISMANAGERAPPACCESS,'') ISMANAGERAPPACCESS from "
					+ "[CUSTOMER_INFO] where PLANT ='"	+ plant + "' AND CUSTNO ='"	+ custid + "' "+ extraCon
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
	
	public boolean Deletecustomeruser(String  custid,String plant)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[CUSTOMER_INFO]"
			                        + " WHERE PLANT ='"	+ plant + "' AND CUSTNO ='"	+ custid + "'";
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
	
	public boolean updateAppQty(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "APPORDERQTYCONFIG" + "]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");
			String conditon = formCondition(htCondition);
			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			this.mLogger.query(this.printQuery, sql.toString());
			flag = updateData(con, sql.toString());
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
	

	public boolean insertAppQty(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				//String value = StrUtils.fString((String) ht.get(key));
				String value = StrUtils.fString(String.valueOf(ht.get(key)));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "APPORDERQTYCONFIG" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			System.out.println( query);
			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	public String getcustomercodebyname(String plant,String custname) throws Exception {
		java.sql.Connection con = null;
		String CUSTNO = "";
		try {
			
			con = DbBean.getConnection();
			StringBuffer SqlQuery = new StringBuffer(" select isnull(CUSTNO,'') CUSTNO from ["+plant+"_CUSTMST] where PLANT='"+plant+"' and  CNAME='"+custname+"'") ;
			
			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());
			
			CUSTNO = (String) m.get("CUSTNO");
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return CUSTNO;
	}


}