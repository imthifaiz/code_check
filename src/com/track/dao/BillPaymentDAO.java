package com.track.dao;

import java.sql.Connection;
import com.track.constants.IConstants;
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
@SuppressWarnings({"rawtypes", "unchecked"})
public class BillPaymentDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.BillPaymentDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.BillPaymentDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addPaymentHdr(Hashtable ht, String plant)throws Exception {
//		boolean flag = false;
		int billHdrId = 0;
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
			query = "INSERT INTO ["+ plant +"_FINPAYMENTHDR] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			billHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return billHdrId;
	}
	
	public int updatePaymentHeader(String paymentHdrId,Hashtable ht, String plant)throws Exception {
//		boolean flag = false;
		int billHdrId = 2;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
		try {
			 /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String SETQUERY=" ";//FIELDS = "", VALUES = "",;
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
//				FIELDS += key + ",";
//				VALUES += "?,";
				if(i<ht.size()-1)
				{
					SETQUERY+=key+"='"+value+"',";
				}
				else
				{
					SETQUERY+=key+"='"+value+"'";
				}
				
			}
			query = "UPDATE ["+ plant +"_FINPAYMENTHDR] SET "+SETQUERY+" WHERE ID="+paymentHdrId;

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			billHdrId = ps.executeUpdate();
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return billHdrId;
	}
	
	
	// RESVI START PAYMENT
	  public int Paymentcount(String plant, String afrmDate, String atoDate)
				throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int Paymentcount = 0;
			String sCondition = "";
			String dtCondStr =    " AND (SUBSTRING(PAYMENT_DATE, 7, 4) + '-' + SUBSTRING(PAYMENT_DATE, 4, 2) + '-' + SUBSTRING(PAYMENT_DATE, 1, 2))";
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
						+ "FINPAYMENTHDR" + "]" + " WHERE " + IConstants.PLANT
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
	  
	// RESVI START PAYMENT
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
	  					+ "FINPAYMENTPDC" + "]" + " WHERE " + IConstants.PLANT
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


	
	public boolean addMultiplePaymentDet(List<Hashtable<String, String>> billDetInfoList, String plant) 
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
			
			for (Hashtable<String, String> billDetInfo : billDetInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = billDetInfo.keys();
				for (int i = 0; i < billDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) billDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINPAYMENTDET]  ("
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
	
	public boolean addPaymentDet(Hashtable ht, String plant)throws Exception {
		boolean flag = false;
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
			query = "INSERT INTO ["+ plant +"_FINPAYMENTDET] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query);
				   
			this.mLogger.query(this.printQuery, query);
			flag = execute_NonSelectQuery(ps, args);
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
	
	public boolean addPaymentAttachments(List<Hashtable<String, String>>  paymentAttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> paymentDetInfo : paymentAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = paymentDetInfo.keys();
				for (int i = 0; i < paymentDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) paymentDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINPAYMENTATTACHMENTS]  ("
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
	
	public String getAdvanceAmountByOrder(String pono, String plant)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String advancePayment = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),'0') AS ADVANCE_PAYMENT FROM ["+ plant +"_FINPAYMENTDET] WHERE "
					+ " TYPE='ADVANCE' AND PONO=? AND PLANT=?";

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,pono);  
			stmt.setString(2,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				advancePayment = rs.getString("ADVANCE_PAYMENT");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return advancePayment;
	}
	
	public String getAdvancebBalanceByOrder(String pono, String plant)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String advancePayment = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(BALANCE),'0') AS ADVANCE_PAYMENT FROM ["+ plant +"_FINPAYMENTDET] WHERE "
					+ " TYPE='ADVANCE' AND PONO=? AND PLANT=?";

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,pono);  
			stmt.setString(2,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				advancePayment = rs.getString("ADVANCE_PAYMENT");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return advancePayment;
	}
	
	public List getAdvancebBalanceByOrderlist(String pono, String plant)
			throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> advancePaymentList = new ArrayList<>();
//		List<String> args = null;
		try {
			con = DbBean.getConnection();
			String query = "SELECT * FROM ["+ plant +"_FINPAYMENTDET] WHERE "
					+ " TYPE='ADVANCE' AND PONO='"+ pono +"' AND PLANT='"+ plant +"' AND LNNO='0'";
		
			//PreparedStatement ps = con.prepareStatement(query);  
			/*
			 * args.add((String) pono); args.add((String) plant);
			 */			
			this.mLogger.query(this.printQuery, query);			
			advancePaymentList = selectData(con, query);

			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return advancePaymentList;
	}
	
	
	public String getpaymentamountByOrder(String pono, String plant)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String advancePayment = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),'0') AS ADVANCE_PAYMENT FROM ["+ plant +"_FINPAYMENTDET] WHERE "
					+ " TYPE='REGULAR' AND PONO=? AND PLANT=?";

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,pono);  
			stmt.setString(2,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				advancePayment = rs.getString("ADVANCE_PAYMENT");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return advancePayment;
	}
	
	public String getreceivedAmountByBill(String pono, String billHdrId, String plant)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String advancePayment = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),'0') AS RECEIVED_AMOUNT FROM ["+ plant +"_FINPAYMENTDET] WHERE "
					+ " TYPE='REGULAR' AND PONO=? AND BILLHDRID = ? AND PLANT=?";

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,pono);  
			stmt.setString(2,billHdrId);  
			stmt.setString(3,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				advancePayment = rs.getString("RECEIVED_AMOUNT");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return advancePayment;
	}
	
	public List getCreditDetails(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> creditDetailsList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ISNULL(REFERENCE,'') REFERENCE,PAYMENT_DATE,b.AMOUNT,b.BALANCE,b.PAYHDRID,ISNULL(b.PONO, '') PONO,b.ID,b.ADVANCEFROM,CURRENCYUSEQT "
					+ "FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTHDR] a JOIN " 
					+ "[" + ht.get("PLANT") + "_FINPAYMENTDET] b " + 
					"ON a.ID = b.PAYHDRID WHERE b.TYPE='ADVANCE' AND a.VENDNO=? AND CAST(b.BALANCE AS decimal(25,5)) > '0' AND a.ISPDCPROCESS != '1'"
					+ "AND b.PLANT = ? ";
			  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("VENDNO"));
			if (ht.get("PONO") != null) {
			//args.add((String) ht.get("PONO"));
				query=query+" AND b.PONO='" + ht.get("PONO")+ "'";
			}
			
			if (ht.get("REFERENCE") != null) {
				//args.add((String) ht.get("REFERENCE"));
				query=query+"AND a.REFERENCE='" + ht.get("REFERENCE")+ "'";
				}
			/*
			 * else query=query+" AND b.BALANCE > 0 ";
			 */
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);	
			PreparedStatement ps = con.prepareStatement(query);
			creditDetailsList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return creditDetailsList;
	}
	
	
	
	public String getBalanceAmountByBill(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String balanceAmount = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL((TOTAL_AMOUNT - ( SELECT ISNULL(SUM(AMOUNT),'')  FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] WHERE BILLHDRID = ? AND TYPE=?)),0) AS BALANCE " + 
					" FROM " + "[" + ht.get("PLANT") + "_FINBILLHDR] WHERE ID=? AND PONO=? AND PLANT=?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,(String) ht.get("BILLHDRID"));  
			stmt.setString(2,"REGULAR");  
			stmt.setString(3,(String) ht.get("BILLHDRID"));  
			stmt.setString(4,(String) ht.get("PONO"));  
			stmt.setString(5,(String) ht.get("PLANT"));  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				balanceAmount = rs.getString("BALANCE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return balanceAmount;
	}
	
	public String getBalanceAmountByBillid(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String balanceAmount = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL((TOTAL_AMOUNT - ( SELECT ISNULL(SUM(AMOUNT),'')  FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] WHERE BILLHDRID = ? AND TYPE=?)),0) AS BALANCE " + 
					" FROM " + "[" + ht.get("PLANT") + "_FINBILLHDR] WHERE ID=? AND PLANT=?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,(String) ht.get("BILLHDRID"));  
			stmt.setString(2,"REGULAR");  
			stmt.setString(3,(String) ht.get("BILLHDRID"));  
			stmt.setString(4,(String) ht.get("PLANT"));  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				balanceAmount = rs.getString("BALANCE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return balanceAmount;
	}
	
	public List getMaxLineNoForAdvancePayment(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> advancePaymentList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT MAX(LNNO) AS LNNO, SUM(BALANCE) AS BALANCE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE PAYHDRID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("PAYHDRID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			advancePaymentList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return advancePaymentList;
	}
	
	public boolean updatePaymentDet(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_FINPAYMENTDET]");
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
	
	public String getpaymentMadeyBill(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String paymentMade = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),0) AS PAYMENT_MADE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE PONO= ? AND TYPE= ? AND PLANT= ?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,(String) ht.get("PONO"));  
			stmt.setString(2,"REGULAR");  
			stmt.setString(3,(String) ht.get("PLANT"));
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				paymentMade = rs.getString("PAYMENT_MADE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return paymentMade;
	}
	
	public String getpaymentMadeyBillwithbillno(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String paymentMade = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),0) AS PAYMENT_MADE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE PONO= ? AND TYPE= ? AND PLANT= ? AND BILLHDRID = ?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,(String) ht.get("PONO"));  
			stmt.setString(2,"REGULAR");  
			stmt.setString(3,(String) ht.get("PLANT"));
			stmt.setString(4,(String) ht.get("BILLHDRID"));
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				paymentMade = rs.getString("PAYMENT_MADE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return paymentMade;
	}
	
	public String getpaymentMadeyBillwithexpno(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String paymentMade = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),0) AS PAYMENT_MADE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE TYPE= ? AND PLANT= ? AND EXPHDRID = ?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,"REGULAR");  
			stmt.setString(2,(String) ht.get("PLANT"));
			stmt.setString(3,(String) ht.get("EXPHDRID"));
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				paymentMade = rs.getString("PAYMENT_MADE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return paymentMade;
	}
	
	public String getConvpaymentMadeyBillwithbillno(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String paymentMade = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),0) AS PAYMENT_MADE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE PONO= ? AND TYPE= ? AND PLANT= ? AND BILLHDRID = ?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,(String) ht.get("PONO"));  
			stmt.setString(2,"REGULAR");  
			stmt.setString(3,(String) ht.get("PLANT"));
			stmt.setString(4,(String) ht.get("BILLHDRID"));
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				paymentMade = rs.getString("PAYMENT_MADE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return paymentMade;
	}
	
	public String getpaymentMadeyBillbibid(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String paymentMade = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),0) AS PAYMENT_MADE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE TYPE= ? AND PLANT= ? AND BILLHDRID = ?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,"REGULAR");  
			stmt.setString(2,(String) ht.get("PLANT"));
			stmt.setString(3,(String) ht.get("BILLHDRID"));
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				paymentMade = rs.getString("PAYMENT_MADE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return paymentMade;
	}
	
	public String getpaymentMadeyEXPbibid(Hashtable ht)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String paymentMade = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(AMOUNT),0) AS PAYMENT_MADE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE TYPE= ? AND PLANT= ? AND EXPHDRID = ?";
			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setString(1,"REGULAR");  
			stmt.setString(2,(String) ht.get("PLANT"));
			stmt.setString(3,(String) ht.get("EXPHDRID"));
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				paymentMade = rs.getString("PAYMENT_MADE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return paymentMade;
	}
	public List getBillPaymentHdrList(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT *,ISNULL((SELECT top 1 ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] C WHERE C.CURRENCYID = A.CURRENCYID), '') DISPLAY FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTHDR] A "
					+ "WHERE ID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentList;
	}
	public List getBillPaymentDetList(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT ISNULL(EXPHDRID,'0') AS EXPHDRID,* FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE PAYHDRID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("PAYHDRID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	public List getBillPaymentDetListByType(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE PAYHDRID = ? AND PLANT = ? AND TYPE= 'REGULAR'";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("PAYHDRID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	public boolean updatePaymentHdr(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_FINPAYMENTHDR]");
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
	
	public List getBillPaymentAttachListById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentAttachList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTATTACHMENTS] "
					+ "WHERE PAYHDRID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("PAYHDRID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentAttachList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentAttachList;
	}
	public List getBillPaymentAttachByPrimId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentAttachList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTATTACHMENTS] "
					+ "WHERE ID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentAttachList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentAttachList;
	}
	public int deleteBillPaymentAttachByPrimId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
//		Map<String, String> map = null;
		String query="";
		int count=0;
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="DELETE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTATTACHMENTS] "
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
	
	public String getadvancefrompaymentdet(String paymentHdrId, String plant)
			throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String advancefrom = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(ADVANCEFROM,'0') AS ADVANCEFROM FROM ["+ plant +"_FINPAYMENTDET] WHERE "
					+ " TYPE='ADVANCE' AND PAYHDRID = ? AND PLANT=?";

			PreparedStatement stmt = con.prepareStatement(query);   
			stmt.setString(1,paymentHdrId);  
			stmt.setString(2,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				advancefrom = rs.getString("ADVANCEFROM");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return advancefrom;
	}
	
	public List getBillPaymentDetListByadvance(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE PAYHDRID = ? AND PLANT = ? AND TYPE= 'ADVANCE'";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("PAYHDRID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	
	public void addPaymentpdc(Hashtable ht, String plant)throws Exception {
//		boolean flag = false;
//		int billHdrId = 0;
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
			query = "INSERT INTO ["+ plant +"_FINPAYMENTPDC] ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
//			billHdrId = 
					execute_NonSelectQueryGetLastInsert(ps, args);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}

	}
	
	public boolean addMultiplePaymentpdc(List<Hashtable<String, String>> billpdcInfoList, String plant) 
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
			
			for (Hashtable<String, String> billDetInfo : billpdcInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = billDetInfo.keys();
				for (int i = 0; i < billDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) billDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO ["+ plant +"_FINPAYMENTPDC]  ("
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
	
	public List getpdcbipayid(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTPDC] "
					+ "WHERE PAYMENTID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("PAYMENTID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	
	public int deletepdcbypayid(String plant,String id) throws Exception {
		java.sql.Connection con = null;
//		Map<String, String> map = null;
		String query="";
		int count=0;
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="DELETE FROM " + "[" + plant + "_FINPAYMENTPDC] "
					+ "WHERE PAYMENTID = "+id+" AND PLANT = '"+ plant +"' AND STATUS != 'PROCESSED'";
			PreparedStatement ps = con.prepareStatement(query);  
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
	
	public int updatePaymentpdc(String paymentpdcId,String SETQUERY, String plant)throws Exception {
//		boolean flag = false;
		int billHdrId = 2;
		Connection connection = null;
		PreparedStatement ps = null;
//		List<String> args = null;
	    String query = "";
		try {
			 /*Instantiate the list*/
//		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			query = "UPDATE ["+ plant +"_FINPAYMENTPDC] SET "+SETQUERY+" WHERE ID="+paymentpdcId;

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			billHdrId = ps.executeUpdate();
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return billHdrId;
	}
	
	public List getpdcpayment(Hashtable ht, String afrmDate,
			String atoDate,String VENDNO,String BANK,
			String CHECQUE_NO,String TYPE,String STATUS) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		String sCondition = "",dtCondStr="",extraCon="",dtCondStrrev="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
		    dtCondStr ="and (ISNULL(CHEQUE_DATE,'')<>'' AND CAST((SUBSTRING(CHEQUE_DATE, 7, 4) + '-' + SUBSTRING(CHEQUE_DATE, 4, 2) + '-' + SUBSTRING(CHEQUE_DATE, 1, 2)) AS date)";
		    dtCondStrrev ="or ISNULL(CHEQUE_REVERSAL_DATE,'')<>'' AND CAST((SUBSTRING(CHEQUE_REVERSAL_DATE, 7, 4) + '-' + SUBSTRING(CHEQUE_REVERSAL_DATE, 4, 2) + '-' + SUBSTRING(CHEQUE_REVERSAL_DATE, 1, 2)) AS date)";
		    //extraCon= " order by CAST((SUBSTRING(CHEQUE_DATE, 7, 4) + SUBSTRING(CHEQUE_DATE, 4, 2) + SUBSTRING(CHEQUE_DATE, 1, 2)) AS date) desc";
		    extraCon= " order by PAYMENTID desc";
		    if (afrmDate.length() > 0) {
              	sCondition = sCondition + dtCondStr + "  >= '" 
  						+ afrmDate
  						+ "'  ";
              	sCondition = sCondition + dtCondStrrev + "  >= '" 
  						+ afrmDate
  						+ "' ) ";
  				if (atoDate.length() > 0) {
  					sCondition = sCondition +dtCondStr+ " <= '" 
  					+ atoDate
  					+ "'  ";
  					sCondition = sCondition +dtCondStrrev+ " <= '" 
  		  					+ atoDate
  		  					+ "' ) ";
  				}
  			  } else {
  				if (atoDate.length() > 0) {
  					sCondition = sCondition +dtCondStr+ " <= '" 
  					+ atoDate
  					+ "'  ";
  					sCondition = sCondition +dtCondStrrev+ " <= '" 
  		  					+ atoDate
  		  					+ "' ) ";
  				}
  		     	}
		    
		    if(TYPE.equalsIgnoreCase("PROCESS"))
		    	sCondition = sCondition +" AND STATUS='NOT PROCESSED' ";
		    else
		    {
		    	if(STATUS.equalsIgnoreCase("PROCESSED"))
		    		sCondition = sCondition +" AND STATUS='PROCESSED' ";
		    	else if(STATUS.equalsIgnoreCase("NOT PROCESSED"))
		    		sCondition = sCondition +" AND STATUS='NOT PROCESSED' ";
		    	else	
		    	sCondition = sCondition +" AND STATUS IN ('NOT PROCESSED','PROCESSED') ";
		    }
			con = com.track.gates.DbBean.getConnection();
			query="SELECT *,CASE WHEN VENDNO='N/A' THEN ISNULL(ACCOUNT_NAME,'Loan from Financial Institution') else '' END ACCOUNT,ISNULL((SELECT VNAME FROM " + "[" + ht.get("PLANT") +"_VENDMST] V WHERE V.VENDNO=P.VENDNO),'') SUPPLIER FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTPDC] P "
					+ "WHERE VENDNO like '%"+VENDNO+"%' AND BANK_BRANCH like '%"+BANK+"%' AND CHECQUE_NO  like '%"+CHECQUE_NO+"%' AND PLANT = ? " + sCondition + extraCon;
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	
	public int updatePaymentPDC(String paymentId,Hashtable ht, String plant)throws Exception {
//		boolean flag = false;
		int billHdrId = 2;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
	    String query = "";
		try {
			 /*Instantiate the list*/
		    args = new ArrayList<String>();		    
			connection = DbBean.getConnection();			
			String SETQUERY=" ";//FIELDS = "", VALUES = "",
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
//				FIELDS += key + ",";
//				VALUES += "?,";
				if(i<ht.size()-1)
				{
					SETQUERY+=key+"='"+value+"',";
				}
				else
				{
					SETQUERY+=key+"='"+value+"'";
				}
				
			}
			query = "UPDATE ["+ plant +"_FINPAYMENTPDC] SET "+SETQUERY+" WHERE ID="+paymentId;

			if(connection != null){
				  /*Create  PreparedStatement object*/
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   
			this.mLogger.query(this.printQuery, query);
			billHdrId = ps.executeUpdate();
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return billHdrId;
	}


	
	public List getpaymentdetbyid(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
					+ "WHERE ID = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	
	public int deleteBillPaymentdet(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
//		Map<String, String> map = null;
		String query="";
		int count=0;
		try {			
		    
			con = com.track.gates.DbBean.getConnection();
			query="DELETE FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTDET] "
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
	
	public boolean deletePayment(String plant,String TranId)
	        throws Exception {
			boolean deleteForTranId = false;
			PreparedStatement ps = null;
			PreparedStatement pshdr = null;
			PreparedStatement psatt = null;
//			PreparedStatement psattpdr = null;
			
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant + "_FINPAYMENTHDR" + "]"
			                        + " WHERE ID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0)
			        	deleteForTranId = true;
			        if(deleteForTranId) {
			        sQry = "DELETE FROM " + "[" + plant + "_FINPAYMENTDET" + "]"
	                        + " WHERE PAYHDRID ='"+TranId+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        pshdr = con.prepareStatement(sQry);
//			        int iCnthdr = 
			        		pshdr.executeUpdate();
			       
			     
			         sQry = "DELETE FROM " + "[" + plant + "_FINPAYMENTATTACHMENTS" + "]"
		                    + " WHERE PAYHDRID ='"+TranId+"'";
				    this.mLogger.query(this.printQuery, sQry);
				    psatt = con.prepareStatement(sQry);
//				    int iCntatt = 
				    		psatt.executeUpdate();
				    
				    sQry = "DELETE FROM " + "[" + plant + "_FINPAYMENTPDC" + "]"
		                    + " WHERE PAYMENTID ='"+TranId+"'";
				    this.mLogger.query(this.printQuery, sQry);
//				    psattpdr = con.prepareStatement(sQry);
//				    int iCntattpdr = 
				    		psatt.executeUpdate();
				    
			        }
			        
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deleteForTranId;
 	}
	
	public List getBillPaymentHdrListbyref(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT * FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTHDR] "
					+ "WHERE REFERENCE = ? AND PLANT = ?";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("[REFERENCE]"));
			args.add((String) ht.get("PLANT"));			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentList;
	}
	
	public List getPaymentPdcForDashboard(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
//		String sCondition = "",dtCondStr="",extraCon="";
		List<String> args = null;
		try {
			args = new ArrayList<String>();
			con = com.track.gates.DbBean.getConnection();
			query = "SELECT PAYMENTID,PAYMENT_DATE, "
					+"ISNULL((SELECT VNAME FROM ["+plant+"_VENDMST] V WHERE V.VENDNO = A.VENDNO),'') SUPPLIER, "
					+"BANK_BRANCH,CHECQUE_NO,CHEQUE_DATE,ISNULL(CHEQUE_REVERSAL_DATE,'') CHEQUE_REVERSAL_DATE,CAST(CHEQUE_AMOUNT AS DECIMAL(18,"+numberOfDecimal+")) CHEQUE_AMOUNT "
					+ " FROM ["+plant+"_FINPAYMENTPDC] A WHERE STATUS = 'NOT PROCESSED'  AND PLANT = ? "
					+" AND CONVERT(DATETIME, CHEQUE_DATE, 103) between '" + fromDate + "' and '" + toDate + "' "
					+" OR CONVERT(DATETIME, CHEQUE_REVERSAL_DATE, 103) between '" + fromDate + "' and '" + toDate + "' ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/			
			args.add(plant);			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	
	public List getpdcpaymentById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> billPaymentDetList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";		
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="SELECT *,CASE WHEN VENDNO='N/A' THEN ISNULL(ACCOUNT_NAME,'Loan from Financial Institution') else '' END ACCOUNT,ISNULL((SELECT VNAME FROM " + "[" + ht.get("PLANT") +"_VENDMST] V WHERE V.VENDNO=P.VENDNO),'') SUPPLIER FROM " + "[" + ht.get("PLANT") + "_FINPAYMENTPDC] P "
					+ "WHERE ID = ? AND PLANT = ? ";
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));			
			
			
			this.mLogger.query(this.printQuery, query);			
			billPaymentDetList = selectData(ps, args);			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return billPaymentDetList;
	}
	
	public String getPdcAmountPayableForDashboard(String plant, String fromDate, String toDate, 
			String numberOfDecimal) throws Exception {
	      java.sql.Connection con = null;
	      String pdcAmount = "";
	      String SqlQuery="";
	      try {
		      con = DbBean.getConnection();
		      SqlQuery = "SELECT CAST(ISNULL(SUM(CHEQUE_AMOUNT),0) AS DECIMAL(18,"+numberOfDecimal+")) PDC_AMOUNT FROM ["+plant+"_FINPAYMENTPDC] " 
		        		+ " WHERE STATUS = 'NOT PROCESSED' "
						+ " AND CONVERT(DATETIME, CHEQUE_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
		        		+ " OR CONVERT(DATETIME, CHEQUE_REVERSAL_DATE, 103) BETWEEN '"+fromDate+"' AND '"+toDate+"' ";
		      System.out.println(SqlQuery.toString());
		      Map m = this.getRowOfData(con, SqlQuery);
		      pdcAmount = (String) m.get("PDC_AMOUNT");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return pdcAmount;
	  }
	
	
	public String getBankChargeFromPaymentHdr(String plant, String ID) throws Exception {
	      java.sql.Connection con = null;
	      String bankcharge = "";
	      String SqlQuery="";
	      try {
		      con = DbBean.getConnection();
		      SqlQuery="SELECT BANK_CHARGE FROM " + "[" + plant + "_FINPAYMENTHDR] "
						+ "WHERE ID = '"+ID+"' AND PLANT = '"+plant+"'";
		      System.out.println(SqlQuery.toString());
		      Map m = this.getRowOfData(con, SqlQuery);
		      bankcharge = (String) m.get("BANK_CHARGE");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return bankcharge;
	  }
	
	public String gettotalpayforapexpenses(String plant, String ID) throws Exception {
	      java.sql.Connection con = null;
	      String amount = "";
	      String SqlQuery="";
	      try {
		      con = DbBean.getConnection();
		      SqlQuery="SELECT CASE WHEN ((SELECT ISNULL(SUM(ISNULL(AMOUNT,0)),0) AS AMOUNT FROM " + plant + "_FINPAYMENTDET WHERE EXPHDRID= '"+ID+"' "
			      		+ "GROUP BY EXPHDRID) != '') THEN (SELECT ISNULL(SUM(ISNULL(AMOUNT,0)),0) AS AMOUNT FROM " + plant + "_FINPAYMENTDET WHERE EXPHDRID= '"+ID+"' "
			      		+ "GROUP BY EXPHDRID) ELSE 0 END AS AMOUNT";
		      System.out.println(SqlQuery.toString());
		      Map m = this.getRowOfData(con, SqlQuery);
		      amount = (String) m.get("AMOUNT");
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return amount;
	  }
	
	public List getpaydetbykeyandexpense(String uuid, String plant)
			throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> advancePaymentList = new ArrayList<>();
//		List<String> args = null;
		try {
			con = DbBean.getConnection();
			String query = "SELECT * FROM ["+ plant +"_FINPAYMENTDET] WHERE CREDITAPPLYKEY='"+ uuid +"' AND PLANT='"+ plant +"' AND BILLHDRID='0'";
		
			//PreparedStatement ps = con.prepareStatement(query);  
			/*
			 * args.add((String) pono); args.add((String) plant);
			 */			
			this.mLogger.query(this.printQuery, query);			
			advancePaymentList = selectData(con, query);

			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return advancePaymentList;
	}
	
	
	public String getpayamtbalance(int HdrId, String plant)throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String balancePayment = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(SUM(BALANCE),'0') AS BALANCE FROM ["+ plant +"_FINPAYMENTDET] WHERE "
					+ " TYPE='ADVANCE' AND PAYHDRID = ? AND PLANT=?";

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setInt(1,HdrId);  
			stmt.setString(2,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				balancePayment = rs.getString("BALANCE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return balancePayment;
	}
	
	public String getpaydebitdate(int HdrId, String plant)throws Exception {
		java.sql.Connection con = null;
//		ArrayList al = new ArrayList();
		String PaymentDate = "";
		try {
			con = DbBean.getConnection();
			String query = "SELECT ISNULL(PAYMENT_DATE,'0') AS PAYMENT_DATE FROM ["+ plant +"_FINPAYMENTHDR] WHERE ID = ? AND PLANT=?";

			PreparedStatement stmt = con.prepareStatement(query);  
			stmt.setInt(1,HdrId);  
			stmt.setString(2,plant);  
			ResultSet rs=stmt.executeQuery();  
			while(rs.next()){ 
				PaymentDate = rs.getString("PAYMENT_DATE");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return PaymentDate;
	}
	
}
