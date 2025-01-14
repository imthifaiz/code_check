package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class InvoiceDAO extends BaseDAO {
	public static String TABLE_NAME = "FININVOICEHDR";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.InvoiceDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.InvoiceDAO_PRINTPLANTMASTERLOG;

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
	
	public InvoiceDAO() {
		_StrUtils = new StrUtils();
	}
	
	public int addInvoiceHdr(Hashtable ht, String plant)throws Exception {
		boolean flag = false;
		int invoiceHdrId = 0;
		int count = 0;
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
				System.out.println(key);
				String value = StrUtils.fString((String) ht.get(key));
				
				System.out.println("---------  "+value);
				args.add(value);
				FIELDS += key + ",";
				VALUES += "'"+value+"',";
				//VALUES += value+",";
			}
			query = "INSERT INTO ["+ plant +"_FININVOICEHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			//invoiceHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
			count = ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			   if (rs.next()){
				   invoiceHdrId = rs.getInt(1);
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
		return invoiceHdrId;
	}
	
	
	// RESVI START PAYMENT
	  public int Paymentcount(String plant, String afrmDate, String atoDate)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int Paymentcount = 0;
			String sCondition = "";
			String dtCondStr =    " AND (SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2))";
			if (afrmDate.length() > 0) {
	        	sCondition = " " + dtCondStr +"  >= '" + afrmDate
	        	+ "'  ";
	        	if (atoDate.length() > 0) {
	        		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
	        		+ "'  ";
	        	}
	        }
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						+ "FINRECEIVEHDR" + "]" + " WHERE " + IConstants.PLANT
						+ " = '" + plant.toUpperCase() + "'"+ sCondition;
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Paymentcount = rs.getInt(1);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return Paymentcount;
		}

	//RESVI ENDS
	  
	  
	//RESVI START PAYMENT RECEIVE
	  public int PaymentCount(String plant, String afrmDate, String atoDate)
	  			throws Exception {
	  		PreparedStatement ps = null;
	  		ResultSet rs = null;
	  		int PaymentCount = 0;
	  		String sCondition = "";
	  		String dtCondStr =    " AND (SUBSTRING(CHEQUE_DATE, 7, 4) + '-' + SUBSTRING(CHEQUE_DATE, 4, 2) + '-' + SUBSTRING(CHEQUE_DATE, 1, 2))";
	  		if (afrmDate.length() > 0) {
	     	sCondition = " " + dtCondStr +"  >= '" + afrmDate
	     	+ "'  ";
	     	if (atoDate.length() > 0) {
	     		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
	     		+ "'  ";
	     	}
	     }
	  		Connection con = null;
	  		try {
	  			con = DbBean.getConnection();
	  			String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
	  					+ "FINRECEIVEPDC" + "]" + " WHERE " + IConstants.PLANT
	  					+ " = '" + plant.toUpperCase() + "'"+ sCondition;
	  			this.mLogger.query(this.printQuery, sQry);
	  			ps = con.prepareStatement(sQry);
	  			rs = ps.executeQuery();
	  			while (rs.next()) {
	  				PaymentCount = rs.getInt(1);
	  			}
	  		} catch (Exception e) {
	  			this.mLogger.exception(this.printLog, "", e);
	  		} finally {
	  			DbBean.closeConnection(con, ps);
	  		}
	  		return PaymentCount;
	  	}

	  //RESVI ENDS
	public boolean addMultipleInvoiceDet(List<Hashtable<String, String>> invoiceDetInfoList, String plant) 
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
			
			for (Hashtable<String, String> invoiceDetInfo : invoiceDetInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = invoiceDetInfo.keys();
				for (int i = 0; i < invoiceDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) invoiceDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FININVOICEDET]  ("
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
	
	public boolean addInvoiceAttachments(List<Hashtable<String, String>>  invoiceAttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> invoiceDetInfo : invoiceAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = invoiceDetInfo.keys();
				for (int i = 0; i < invoiceDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) invoiceDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FININVOICEATTACHMENTS]  ("
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
	
	public ArrayList selectInvoiceHdr(String query, Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "FININVOICEHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				MLogger.log(0, "condition preisent stage 3");
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
	
	public void updategino(String query, Hashtable htCondition, String extCond)
            throws Exception {
    boolean flag = false;
    int invoiceHdrId = 0;
    java.sql.Connection con = null;
    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(query);

            String conditon = "";
			if (htCondition.size() > 0) {
				sql.append(" AND ");
				conditon = formCondition(htCondition);
				sql.append(" " + conditon);
			}


            if (extCond.length() != 0) {
                    sql.append(extCond);
            }
            
            	updateData(con, sql.toString());
            	
                this.mLogger.query(this.printQuery, sql.toString());
			

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }

}
	
	public int update(String query, Hashtable htCondition, String extCond)
            throws Exception {
    boolean flag = false;
    int invoiceHdrId = 0;
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
            		invoiceHdrId=1;
                this.mLogger.query(this.printQuery, sql.toString());
			

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return invoiceHdrId;
}
	public boolean deleteInvoiceDet(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FININVOICEDET" + "]"
			                        + " WHERE INVOICEHDRID ='"+TranId+"'";
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
	public List getInvoiceHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> invoiceHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, CUSTNO,ISNULL(GINO,'') GINO,ISNULL(TAXID,'0') TAXID,INVOICE,ISNULL(DONO,'') DONO,ISNULL(TRANSPORTID,'') TRANSPORTID,INVOICE_DATE,DUE_DATE,PAYMENT_TERMS,OUTBOUD_GST,EMPNO,ITEM_RATES,ISNULL(ORDER_DISCOUNT,0) as ORDER_DISCOUNT,ISNULL(ORIGIN,0) as ORIGIN,ISNULL(DEDUCT_INV,0) as DEDUCT_INV,"
					+ "DISCOUNT,DISCOUNT_TYPE,DISCOUNT_ACCOUNT,SHIPPINGCOST,ADJUSTMENT,SUB_TOTAL,ISNULL(TAX_STATUS,'') TAX_STATUS,ISNULL(JobNum,'') JobNum,"
					+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1) CURRENCYUSEQT, "
					+ " ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP, "
					+ "TOTAL_AMOUNT,BILL_STATUS,NOTE,TERMSCONDITIONS,CRAT,CRBY,UPAT,UPBY,ISNULL(ISEXPENSE,0) as ISEXPENSE,ISNULL(TAXAMOUNT,0) as TAXAMOUNT,ISNULL(INCOTERMS,'') as INCOTERMS,ISNULL(SHIPPINGID,'') as SHIPPINGID,ISNULL(SHIPPINGCUSTOMER,'') as SHIPPINGCUSTOMER,ISNULL(ORDERTYPE,'') AS ORDERTYPE FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEHDR] A WHERE ID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			invoiceHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return invoiceHdrList;
	}
	public List getConvInvoiceHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> invoiceHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, CUSTNO,ISNULL(GINO,'') GINO,INVOICE,ISNULL(DONO,'') DONO,INVOICE_DATE,DUE_DATE,PAYMENT_TERMS,EMPNO,ITEM_RATES,ISNULL(ORDER_DISCOUNT,0) as ORDER_DISCOUNT,ISNULL(ORIGIN,0) as ORIGIN,ISNULL(DEDUCT_INV,0) as DEDUCT_INV,"
					+ "(case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1)) end) DISCOUNT,"
					+ "DISCOUNT_TYPE,DISCOUNT_ACCOUNT,"
					+ "(ISNULL(SHIPPINGCOST, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1)) SHIPPINGCOST, "
					+ "(ISNULL(ADJUSTMENT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1))  ADJUSTMENT,"
					+ "(ISNULL(SUB_TOTAL, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1)) SUB_TOTAL, "
					+ "ISNULL(TAX_STATUS,'') TAX_STATUS,ISNULL(A.EMPNO,0) EMPNO,ISNULL((SELECT FNAME FROM " +ht.get("PLANT")  +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME,ISNULL(A.ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,ISNULL(A.TAXID,'0') TAXID,ISNULL(A.ISDISCOUNTTAX,'0') ISDISCOUNTTAX,ISNULL(A.ISORDERDISCOUNTTAX,'0') ISORDERDISCOUNTTAX,ISNULL(A.ISSHIPPINGTAX,'0') ISSHIPPINGTAX,ISNULL(A.PROJECTID,'0') PROJECTID,ISNULL(A.TRANSPORTID,0) TRANSPORTID,"
					+ "(ISNULL(TOTAL_AMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1)) TOTAL_AMOUNT,"
					+ "BILL_STATUS,NOTE,TERMSCONDITIONS,CRAT,CRBY,UPAT,UPBY,ISNULL(ISEXPENSE,0) as ISEXPENSE,"
					+ "(ISNULL(TAXAMOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1)) as TAXAMOUNT,"
					+ " ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1) CURRENCYUSEQT, "
					+ " ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,"
					+ " ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,"
					+ "ISNULL(INCOTERMS,'') as INCOTERMS,ISNULL(SHIPPINGID,'') as SHIPPINGID,ISNULL(SHIPPINGCUSTOMER,'') as SHIPPINGCUSTOMER,ISNULL(ORDERTYPE,'') ORDERTYPE FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEHDR] A WHERE ID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			invoiceHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return invoiceHdrList;
	}
	public List getInvoiceHdrByCustNo(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> invoiceHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, CUSTNO,INVOICE,DONO,INVOICE_DATE,DUE_DATE,PAYMENT_TERMS,EMPNO,ITEM_RATES,"
					+ "DISCOUNT,DISCOUNT_TYPE,DISCOUNT_ACCOUNT,SHIPPINGCOST,ADJUSTMENT,SUB_TOTAL,"
					+ "TOTAL_AMOUNT,BILL_STATUS,NOTE,TERMSCONDITIONS,CRAT,CRBY,UPAT,UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEHDR] WHERE CUSTNO = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("CUSTNO"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			invoiceHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return invoiceHdrList;
	}
	
	public List getInvoiceHdrByCustNoadvance(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> invoiceHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, CUSTNO,INVOICE,DONO,INVOICE_DATE,DUE_DATE,PAYMENT_TERMS,EMPNO,ITEM_RATES,"
					+ "ISNULL(CURRENCYID, '') CURRENCYID,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FININVOICEDET] WHERE INVOICEHDRID = A.ID ),1) CURRENCYUSEQT, "
					+ "DISCOUNT,DISCOUNT_TYPE,DISCOUNT_ACCOUNT,SHIPPINGCOST,ADJUSTMENT,SUB_TOTAL,"
					+ "TOTAL_AMOUNT,BILL_STATUS,NOTE,TERMSCONDITIONS,CRAT,CRBY,UPAT,UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEHDR] A WHERE CUSTNO = ? AND PLANT =? AND BILL_STATUS != 'PAID' AND BILL_STATUS != 'Draft' AND BILL_STATUS != 'CANCELLED'";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("CUSTNO"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			invoiceHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return invoiceHdrList;
	}
	
	public List getInvoiceHdrByCustNocredit(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> invoiceHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID,ISNULL(GINO,'') AS GINO, CUSTNO,INVOICE,DONO,INVOICE_DATE,DUE_DATE,PAYMENT_TERMS,EMPNO,ITEM_RATES,"
					+ "DISCOUNT,DISCOUNT_TYPE,DISCOUNT_ACCOUNT,SHIPPINGCOST,ADJUSTMENT,SUB_TOTAL,"
					+ "TOTAL_AMOUNT,BILL_STATUS,NOTE,TERMSCONDITIONS,CRAT,CRBY,UPAT,UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEHDR] WHERE CUSTNO = ? AND PLANT =? AND BILL_STATUS != 'CANCELLED' AND DEDUCT_INV != '1' AND CREDITNOTESSTATUS = '0' ORDER BY ID DESC";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("CUSTNO"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			invoiceHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return invoiceHdrList;
	}
	
	public List getInvoiceDetByHdrId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> InvoiceDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, LNNO,INVOICEHDRID,ITEM,ACCOUNT_NAME,QTY,UNITPRICE,DISCOUNT,ISNULL(DISCOUNT_TYPE,0) DISCOUNT_TYPE,TAX_TYPE,ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE,"
					+ "AMOUNT,ISNULL(UOM,'') UOM,ISNULL(LOC,'') LOC,ISNULL(BATCH, '') BATCH,"
					+ "ISNULL((SELECT ITEMDESC FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS ITEMDESC,"
					+ "ISNULL((SELECT HSCODE FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS HSCODE,"
					+ "ISNULL((SELECT COO FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS COO,"
					+ "ISNULL((SELECT CASE WHEN PRD_BRAND_ID = 'NOBRAND' THEN '' ELSE PRD_BRAND_ID END AS BRAND FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS BRAND,"
					+ "ISNULL((SELECT REMARK1 FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS DETAILDESC,"
					+ "CRAT,CRBY,UPAT,UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEDET] A LEFT JOIN "
			+ "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
					+ " WHERE INVOICEHDRID = ? AND A.PLANT =? ORDER BY A.LNNO";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("INVOICEHDRID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			InvoiceDetList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return InvoiceDetList;
	}
	
	public List getConvInvoiceDetByHdrId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> InvoiceDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ID, LNNO,INVOICEHDRID,ITEM,ACCOUNT_NAME,QTY,"
					+ "(UNITPRICE*ISNULL(CURRENCYUSEQT,1)) UNITPRICE,ISNULL(CURRENCYUSEQT,1) CURRENCYUSEQT,"
					+ "(case when DISCOUNT_TYPE='%' then DISCOUNT else (DISCOUNT*ISNULL(CURRENCYUSEQT,1)) end) DISCOUNT,"
					+ "ISNULL(DISCOUNT_TYPE,0) DISCOUNT_TYPE,TAX_TYPE,ISNULL(GSTPERCENTAGE,0) GSTPERCENTAGE,"
					+ "(AMOUNT*ISNULL(CURRENCYUSEQT,1)) AMOUNT,"
					+ "ISNULL(UOM,'') UOM,ISNULL(LOC,'') LOC,ISNULL(BATCH, '') BATCH,"
					+ "ISNULL((SELECT ITEMDESC FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS ITEMDESC,"
					+ "ISNULL((SELECT HSCODE FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS HSCODE,"
					+ "ISNULL((SELECT COO FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS COO,"
					+ "ISNULL((SELECT CASE WHEN PRD_BRAND_ID = 'NOBRAND' THEN '' ELSE PRD_BRAND_ID END AS BRAND FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS BRAND,"
					+ "ISNULL((SELECT REMARK1 FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=A.ITEM),'') AS DETAILDESC,"
					+ "CRAT,CRBY,UPAT,UPBY  FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEDET] A LEFT JOIN "
			+ "[" + ht.get("PLANT") + "_COMPANY_CONFIG] B ON A.TAX_TYPE = B.GSTTYPE"
					+ " WHERE INVOICEHDRID = ? AND A.PLANT =? ORDER BY A.LNNO";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("INVOICEHDRID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			InvoiceDetList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return InvoiceDetList;
	}
	
	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM [" + ht.get("A.PLANT") + "_FININVOICEDET] A JOIN [" + ht.get("A.PLANT")+"_FININVOICEHDR] B "
					+ "ON A.INVOICEHDRID = B.ID ");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append("  " + extCond);

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
	public List getInvoiceAttachByInvId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> InvoiceAttachList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEATTACHMENTS]"
					+ " WHERE INVOICEHDRID = ? AND PLANT =?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("INVOICEHDRID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			InvoiceAttachList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return InvoiceAttachList;
	}
	public List getInvoiceAttachByPrimId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> InvoiceAttachList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM "
			+ "[" + ht.get("PLANT") + "_FININVOICEATTACHMENTS]"
					+ " WHERE ID = ? AND PLANT =?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			InvoiceAttachList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return InvoiceAttachList;
	}
	public int deleteInvAttachByPrimId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		Map<String, String> map = null;
		String query="";
		int count=0;
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="DELETE FROM " + "[" + ht.get("PLANT") + "_FININVOICEATTACHMENTS] "
					+ "WHERE ID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			ps.setString(1, (String) ht.get("ID"));
			ps.setString(2, (String) ht.get("PLANT"));	
			
			this.mLogger.query(this.printQuery, query);	
			count=ps.executeUpdate();
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return count;
	}
	
	
	public String getActualShippingCostForinvoice(String plant, String dono) throws Exception {
	      java.sql.Connection con = null;
	      String shippinCost = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT (a.SHIPPINGCOST - ( SELECT ISNULL(SUM(SHIPPINGCOST),0) FROM  ["+plant+"_FININVOICEHDR] b"
	        		+ " WHERE b.DONO = a.DONO) ) AS SHIPPINGCOST FROM ["+plant+"_DOHDR] a " + 
	        		"WHERE a.DONO = '"+dono+"' GROUP BY DONO, SHIPPINGCOST";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       shippinCost = (String) m.get("SHIPPINGCOST");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return shippinCost;
	  }
	
	public String getActualOrderDiscountCostForinvoice(String plant, String dono) throws Exception {
	      java.sql.Connection con = null;
	      String odiscCost = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT (a.ORDERDISCOUNT - ( SELECT ISNULL(SUM(ORDER_DISCOUNT),0) FROM  ["+plant+"_FININVOICEHDR] b"
	        		+ " WHERE b.DONO = a.DONO) ) AS ORDER_DISCOUNT FROM ["+plant+"_DOHDR] a " + 
	        		"WHERE a.DONO = '"+dono+"' GROUP BY DONO, ORDERDISCOUNT";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       odiscCost = (String) m.get("ORDER_DISCOUNT");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return odiscCost;
	  }
	
	public String getActualShippingCostForconsignmentinvoice(String plant, String tono) throws Exception {
	      java.sql.Connection con = null;
	      String shippinCost = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT (a.SHIPPINGCOST - ( SELECT ISNULL(SUM(SHIPPINGCOST),0) FROM  ["+plant+"_FININVOICEHDR] b"
	        		+ " WHERE b.JobNum = a.TONO) ) AS SHIPPINGCOST FROM ["+plant+"_TOHDR] a " + 
	        		"WHERE a.TONO = '"+tono+"' GROUP BY TONO, SHIPPINGCOST";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       shippinCost = (String) m.get("SHIPPINGCOST");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return shippinCost;
	  }
	
	public String getActualOrderDiscountCostForconsignmentinvoice(String plant, String tono) throws Exception {
	      java.sql.Connection con = null;
	      String odiscCost = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT (a.ORDERDISCOUNT - ( SELECT ISNULL(SUM(ORDER_DISCOUNT),0) FROM  ["+plant+"_FININVOICEHDR] b"
	        		+ " WHERE b.JobNum = a.TONO) ) AS ORDER_DISCOUNT FROM ["+plant+"_TOHDR] a " + 
	        		"WHERE a.TONO = '"+tono+"' GROUP BY TONO, ORDERDISCOUNT";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       odiscCost = (String) m.get("ORDER_DISCOUNT");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return odiscCost;
	  }
	
	public boolean deleteInvoice(String plant,String TranId)
	        throws Exception {
			boolean deleteprdForTranId = false;
			PreparedStatement ps = null;
			PreparedStatement pshdr = null;
			PreparedStatement psatt = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FININVOICEHDR" + "]"
			                        + " WHERE ID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0)
			                deleteprdForTranId = true;
			        if(deleteprdForTranId) {
			        sQry = "DELETE FROM " + "[" + plant + "_FININVOICEDET" + "]"
	                        + " WHERE INVOICEHDRID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        pshdr = con.prepareStatement(sQry);
			        int iCnthdr = pshdr.executeUpdate();
			       
			     
			         sQry = "DELETE FROM " + "[" + plant + "_FININVOICEATTACHMENTS" + "]"
		                    + " WHERE INVOICEHDRID ='"+TranId+"'";
				    this.mLogger.query(this.printQuery, sQry);
				    psatt = con.prepareStatement(sQry);
				    int iCntatt = psatt.executeUpdate();
				    
			        }
			        
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deleteprdForTranId;
 	}
	
	public String  getTotalPayableForDashboard(String plant, String fromDate, String toDate, 
			String numberOfDecimal) throws Exception {
	      java.sql.Connection con = null;
	      String amountPayable = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT CAST(ISNULL(SUM(AMOUNTPAID),0) AS DECIMAL(18,"+numberOfDecimal+")) AS AMOUNT_PAYABLE FROM ["+plant+"_FINPAYMENTHDR] WHERE  " 
					+ " VENDNO IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=6 AND ACCOUNTDETAILTYPE=18) "
					+ " AND PAID_THROUGH NOT IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=3 AND ACCOUNTDETAILTYPE=8) "
					+ " AND CONVERT(DATETIME, PAYMENT_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' ";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       amountPayable = (String) m.get("AMOUNT_PAYABLE");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return amountPayable;
	  }	
	
	public List getReceivableAmountByCustomerForDashboard(String plant, String fromDate, String toDate, 
			String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> InvoiceDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			
			query = "SELECT ISNULL((SELECT CNAME FROM ["+plant+"_CUSTMST] C WHERE C.CUSTNO = A.CUSTNO),'') CUSTOMER,SUM(AMOUNT_RECEIVED) AS AMOUNT_RECEIVABLE,SUM(PDC_AMOUNT) AS PDC_AMOUNT,"
					+" (SUM(AMOUNT_RECEIVED) + SUM(PDC_AMOUNT)) TOTAL_RECEIVABLE FROM ("
					+" SELECT CUSTNO,CAST(SUM(AMOUNTRECEIVED) AS DECIMAL(18,2)) AS AMOUNT_RECEIVED, 0 AS PDC_AMOUNT FROM ["+plant+"_FINRECEIVEHDR] WHERE" 
					+" CUSTNO IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=3 AND ACCOUNTDETAILTYPE=7)"  
					+" AND DEPOSIT_TO NOT IN (SELECT ACCOUNT_NAME FROM ["+plant+"_FINCHARTOFACCOUNTS] WHERE ACCOUNTTYPE=3 AND ACCOUNTDETAILTYPE=8)"  
					+" AND CONVERT(DATETIME, RECEIVE_DATE, 103) BETWEEN ? AND ? GROUP BY CUSTNO"
					+" UNION"
					+" SELECT CUSTNO, 0 AS AMOUNT_PAYABLE, CAST(ISNULL(SUM(CHEQUE_AMOUNT),0) AS DECIMAL(18,2)) PDC_AMOUNT FROM ["+plant+"_FINRECEIVEPDC]  WHERE" 
					+" STATUS = 'NOT PROCESSED'  AND CONVERT(DATETIME, CHEQUE_DATE, 103) BETWEEN ? AND ?"  
					+" OR CONVERT(DATETIME, CHEQUE_REVERSAL_DATE, 103) BETWEEN ? AND ? GROUP BY CUSTNO"
					+" ) A GROUP BY CUSTNO";
			

			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(toDate);
			args.add(fromDate);
			args.add(toDate);
			
			this.mLogger.query(this.printQuery, query);
			
			InvoiceDetList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return InvoiceDetList;
	}
	
	public ArrayList dnplINVOICE(String plant, String invoiceNo)
		      throws Exception {
		     String q = "";
		     String link = "";
		     String result = "";
		     String where = "";
		     ArrayList al = null;

		     try {
		      Hashtable htCond = new Hashtable();
		      htCond.put("A.PLANT", plant);
		      
		      
		      q ="SELECT B.INVOICEHDRID as HID,'' dono,B.LNNO dolnno,ISNULL((SELECT CNAME FROM ["+plant+"_CUSTMST] C WHERE C.CUSTNO = A.CUSTNO),'') custname,"
		      +" b.ITEM item,ISNULL(itemdesc,'') as Itemdesc,isnull(B.QTY,0) as qtyis,ISNULL(SALESUOM,'') as uom,"
  		  +" ISNULL(NETWEIGHT,0) netweight,ISNULL(GROSSWEIGHT,0) grossweight,isnull(hscode,'') hscode, isnull(coo,'') coo,"
  		  +" '' packing,'' dimension,'' as PLNO,''as DNNO,'' as TOTALNETWEIGHT,'' as TOTALGROSSWEIGHT,'' as NETPACKING,"
  		  +" '' as NETDIMENSION,'' as DNPLREMARKS,INVOICE "
  		  +" FROM "+plant+"_FININVOICEDET B JOIN "+plant+"_FININVOICEHDR A ON A.ID=B.INVOICEHDRID JOIN "+plant+"_ITEMMST I ON I.ITEM =B.ITEM"
		      +" WHERE A.INVOICE='"+invoiceNo+"' ";
		      
		      String extCond=" AND A.DONO = ''  and  a.plant <> '' and B.ITEM not in (SELECT ITEM FROM "+plant+"_DNPLDET D JOIN "+plant+"_DNPLHDR H on D.HDRID=H.ID WHERE D.ITEM=B.ITEM AND D.LNNO=B.LNNO AND H.INVOICE=A.INVOICE) "; 
		      
		      al= selectForReport(q, htCond, extCond);
		      

		     } catch (Exception e) {
		      throw e;
		     }
		     return al;
		  }
	//  RESVI START
	  public int Invoicecount(String plant, String afrmDate, String atoDate)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int Invoicecount = 0;
			String sCondition = "";
			String dtCondStr =    " AND (SUBSTRING(Invoice_date, 7, 4) + '-' + SUBSTRING(Invoice_date, 4, 2) + '-' + SUBSTRING(Invoice_date, 1, 2))";
			if (afrmDate.length() > 0) {
	        	sCondition = " " + dtCondStr +"  >= '" + afrmDate
	        	+ "'  ";
	        	if (atoDate.length() > 0) {
	        		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
	        		+ "'  ";
	        	}
	        }
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						+ "FININVOICEHDR" + "]" + " WHERE " + IConstants.PLANT
						+ " = '" + plant.toUpperCase() + "'"+ sCondition;
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				rs = ps.executeQuery();
				while (rs.next()) {
					Invoicecount = rs.getInt(1);
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return Invoicecount;
		}

	//RESVI ENDS

	  /* Method to get no. of Invoices without Cost Of Goods Sold Entry based on Item */
	  public int invoiceWoCOGSCount(String item, String plant)throws Exception {
		  PreparedStatement ps = null;
		  ResultSet rs = null;
		  int invoiceCount = 0;
		  Connection con = null;
		  try {
			  con = DbBean.getConnection();
			  String sQry = "SELECT COUNT(*) AS PENDING_INVOICES "
			  + "FROM ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] B ON A.ID =B.INVOICEHDRID" + 
			  " WHERE A.BILL_STATUS <> 'Draft' AND ISNULL(B.IS_COGS_SET,'')<>'Y' AND B.ITEM='"+item+"'";
			  this.mLogger.query(this.printQuery, sQry);
			  ps = con.prepareStatement(sQry);
			  rs = ps.executeQuery();
			  while (rs.next()) {
				  invoiceCount = rs.getInt(1);
			  }
		  } catch (Exception e) {
			  this.mLogger.exception(this.printLog, "", e);
		  } finally {
			  DbBean.closeConnection(con, ps);
		  }
		  return invoiceCount;
	  }
	  
	  public List invoiceWoCOGS(String item, String plant) throws Exception {
		  java.sql.Connection con = null;
			List InvoiceDetList = new ArrayList<>();
			Map<String, String> map = null;
			String query="";
			List<String> args = null;
			try {		
				/*Instantiate the list*/
			    args = new ArrayList<String>();			    
				con = com.track.gates.DbBean.getConnection();
				query="SELECT A.INVOICE,B.INVOICEHDRID,B.LNNO,B.ITEM,B.QTY,A.INVOICE_DATE FROM ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] B ON A.ID =B.INVOICEHDRID " + 
					"WHERE A.BILL_STATUS <> 'Draft' AND ISNULL(B.IS_COGS_SET,'')<>'Y' AND B.ITEM = ? " + 
					"ORDER BY CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date), A.INVOICE";
				PreparedStatement ps = con.prepareStatement(query);  
				
				/*Storing all the query param argument in list squentially*/
				args.add(item);				
				this.mLogger.query(this.printQuery, query);
				InvoiceDetList = selectData(ps, args);				
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return InvoiceDetList;
	  }
	  
	  public int update_is_cogs_set(String hdrId, String lnno, String item, String plant) throws Exception {
	    boolean flag = false;
	    int invoiceHdrId = 0;
	    java.sql.Connection con = null;
	    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE [" + plant + "_FININVOICEDET] SET IS_COGS_SET = 'Y' ");
            sql.append("WHERE INVOICEHDRID='"+hdrId+"' AND LNNO='"+lnno+"' AND ITEM='"+item+"'");
        	flag = updateData(con, sql.toString());
        	if(flag)
        		invoiceHdrId=1;
            this.mLogger.query(this.printQuery, sql.toString());
	    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
	    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
	    }
	    return invoiceHdrId;
	}
	  
	  public boolean updateInvoiceDet(String query, Hashtable htCondition, String extCond)
			     throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
			     con = com.track.gates.DbBean.getConnection();
			     StringBuffer sql = new StringBuffer(" UPDATE " + "["
			                     + htCondition.get("PLANT") + "_FININVOICEDET]");
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
	  
	  public boolean isExisitInv(String plant, String invoice) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM ["+ plant+"_FININVOICEDET] A JOIN ["+ plant+"_FININVOICEHDR] B "
						+ "ON A.INVOICEHDRID = B.ID ");
				sql.append(" WHERE  B.INVOICE = '"+ invoice+"'");


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
	  
	  public boolean isExisitgino(String plant, String gino) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM ["+ plant+"_FININVOICEHDR] ");
				sql.append(" WHERE  GINO = '"+ gino+"'");


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
	  
	  public boolean isGINOPaid(String plant, String gino) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" FROM [" + plant+"_FININVOICEHDR]");
				sql.append(" WHERE GINO='"+gino+"' AND PLANT='"+plant+"' AND (BILL_STATUS='Draft' or BILL_STATUS='Open')");

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
	  
		public List selectRemarks(String query, Hashtable ht) throws Exception {
			List remarksList = new ArrayList();

			java.sql.Connection con = null;
			try {

				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
						+ "[" + ht.get("PLANT") + "_" + "INVOICEDET_REMARKS" + "]");
				sql.append(" WHERE ");
				String conditon = formCondition(ht);
				sql.append(conditon);
				this.mLogger.query(this.printQuery, sql.toString());

				remarksList = selectData(con, sql.toString());

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return remarksList;
		}
		
		 public boolean isExisitInvoiceMultiRemarks(Hashtable ht) throws Exception {
	         this.mLogger.log(1, this.getClass() + " isExisit()");
	         boolean flag = false;
	         java.sql.Connection con = null;
	         try {
	                 con = com.track.gates.DbBean.getConnection();
	                 StringBuffer sql = new StringBuffer(" SELECT ");
	                 sql.append("COUNT(*) ");
	                 sql.append(" ");
	                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "INVOICEDET_REMARKS" + "]");
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
		 
		 public boolean deleteInvoiceMultiRemarks(Hashtable ht) throws Exception {
	         boolean delete = false;
	         java.sql.Connection con = null;
	         try {
	                 con = DbBean.getConnection();
	                 StringBuffer sql = new StringBuffer(" DELETE ");
	                 sql.append(" ");
	                 sql.append(" FROM " + "[" + ht.get("PLANT") + "_INVOICEDET_REMARKS]");
	                 sql.append(" WHERE " + formCondition(ht));
	                 this.mLogger.query(true, sql.toString());
	                 
	                 delete = updateData(con, sql.toString());
	                 
	         } catch (Exception e) {
	                 this.mLogger.exception(true, "", e);
	                 throw e;
	         } finally {
	                 if (con != null) {
	                         DbBean.closeConnection(con);
	                 }
	         }
	         return delete;
	 }
		 
		 public boolean insertInvoiceMultiRemarks(Hashtable ht) throws Exception {
				boolean insertFlag = false;
				java.sql.Connection conn = null;
				try {
					conn = DbBean.getConnection();
					String FIELDS = "", VALUES = "";
					Enumeration enum1 = ht.keys();
					for (int i = 0; i < ht.size(); i++) {
						String key = _StrUtils.fString((String) enum1.nextElement());
						String value = _StrUtils.fString((String) ht.get(key));
						FIELDS += key + ",";
						VALUES += "'" + value + "',";
					}
					String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_INVOICEDET_REMARKS]" + "("
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
		 
		 public boolean isExisit(Hashtable ht) throws Exception {
				boolean flag = false;
				java.sql.Connection con = null;
				try {
					con = com.track.gates.DbBean.getConnection();

					StringBuffer sql = new StringBuffer(" SELECT ");
					sql.append("COUNT(*) ");
					sql.append(" ");
					sql.append(" FROM " + "["+ht.get("PLANT")+"_FININVOICEHDR]");
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
		 
		 public boolean isExisitSo(Hashtable ht) throws Exception {
			 boolean flag = false;
			 java.sql.Connection con = null;
			 try {
				 con = com.track.gates.DbBean.getConnection();
				 
				 StringBuffer sql = new StringBuffer(" SELECT ");
				 sql.append("COUNT(*) ");
				 sql.append(" ");
				 sql.append(" FROM " + "["+ht.get("PLANT")+"_FINSORETURN]");
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
		 
		 public int Ordercount(String plant, String afrmDate, String atoDate)
					throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				int Ordercount = 0;
				String sCondition = "";
				String dtCondStr =    " AND (SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2))";
				if (afrmDate.length() > 0) {
		          	sCondition = " " + dtCondStr +"  >= '" + afrmDate
		          	+ "'  ";
		          	if (atoDate.length() > 0) {
		          		sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
		          		+ "'  ";
		          	}
		          }
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
							+ "FININVOICEHDR" + "]" + " WHERE " + IConstants.PLANT
							+ " = '" + plant.toUpperCase() + "'"+ sCondition;
					this.mLogger.query(this.printQuery, sQry);
					ps = con.prepareStatement(sQry);
					rs = ps.executeQuery();
					while (rs.next()) {
						Ordercount = rs.getInt(1);
					}
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				} finally {
					DbBean.closeConnection(con, ps);
				}
				return Ordercount;
			}

		 public int Ordercount(String plant, String INVOICEHDRID)
				 throws Exception {
			 PreparedStatement ps = null;
			 ResultSet rs = null;
			 int Ordercount = 0;
			 String sCondition =    " AND INVOICEHDRID="+INVOICEHDRID;
			 Connection con = null;
			 try {
				 con = DbBean.getConnection();
				 String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
						 + "FININVOICEDET" + "]" + " WHERE " + IConstants.PLANT
						 + " = '" + plant.toUpperCase() + "'"+ sCondition;
				 this.mLogger.query(this.printQuery, sQry);
				 ps = con.prepareStatement(sQry);
				 rs = ps.executeQuery();
				 while (rs.next()) {
					 Ordercount = rs.getInt(1);
				 }
			 } catch (Exception e) {
				 this.mLogger.exception(this.printLog, "", e);
			 } finally {
				 DbBean.closeConnection(con, ps);
			 }
			 return Ordercount;
		 }
		 
		 
		 public int updatepeppolstatus(String hdrId, String docid, String plant) throws Exception {
			    boolean flag = false;
			    int invoiceHdrId = 0;
			    java.sql.Connection con = null;
			    try {
		            con = com.track.gates.DbBean.getConnection();
		            StringBuffer sql = new StringBuffer(" UPDATE [" + plant + "_FININVOICEHDR] SET PEPPOL_STATUS = '1', PEPPOL_DOC_ID ='"+docid+"' ");
		            sql.append("WHERE ID='"+hdrId+"'");
		        	flag = updateData(con, sql.toString());
		        	if(flag)
		        		invoiceHdrId=1;
		            this.mLogger.query(this.printQuery, sql.toString());
			    } catch (Exception e) {
		            this.mLogger.exception(this.printLog, "", e);
		            throw e;
			    } finally {
		            if (con != null) {
		                    DbBean.closeConnection(con);
		            }
			    }
			    return invoiceHdrId;
			}

		 
}
