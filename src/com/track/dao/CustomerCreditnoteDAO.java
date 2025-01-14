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

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class CustomerCreditnoteDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.CustomerCreditnoteDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.CustomerCreditnoteDAO_PRINTPLANTMASTERLOG;
	
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
	
	public String plant = "";

	public int addCreditnoteHdr(Hashtable ht, String plant) throws Exception {
		boolean flag = false;
		int CreditHdrId = 0;
		int count = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();
			connection = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				args.add(value);
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
				// VALUES += value+",";
			}
			query = "INSERT INTO [" + plant + "_FINCUSTOMERCREDITNOTEHDR] (" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1) + ")";

			if (connection != null) {
				/* Create PreparedStatement object */
				ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

				// this.mLogger.query(this.printQuery, query);
				// invoiceHdrId = execute_NonSelectQueryGetLastInsert(ps, args);
				count = ps.executeUpdate();
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					CreditHdrId = rs.getInt(1);
				}
			}
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return CreditHdrId;
	}

	public boolean addMultipleCreditnoteDet(List<Hashtable<String, String>> CreditnoteDetInfoList, String plant)
			throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			connection = DbBean.getConnection();

			for (Hashtable<String, String> CreditnoteDetInfo : CreditnoteDetInfoList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = CreditnoteDetInfo.keys();
				for (int i = 0; i < CreditnoteDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) CreditnoteDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}
				query = "INSERT INTO [" + plant + "_FINCUSTOMERCREDITNOTEDET]  ("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				System.out.println(query);
				System.out.println(args.toString());
				if (connection != null) {
					/* Create PreparedStatement object */
					ps = connection.prepareStatement(query);
					// this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if (flag) {
						args.clear();
					}
				}
			}
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return flag;
	}

	public boolean addCreditnoteAttachments(List<Hashtable<String, String>> creditAttachmentList, String plant)
			throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			connection = DbBean.getConnection();

			for (Hashtable<String, String> CreditnoteDetInfo : creditAttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = CreditnoteDetInfo.keys();
				for (int i = 0; i < CreditnoteDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) CreditnoteDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}
				query = "INSERT INTO [" + plant + "_FINCUSTOMERCREDITNOTEATTACHMENTS]  ("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				if (connection != null) {
					/* Create PreparedStatement object */
					ps = connection.prepareStatement(query);
					// this.mLogger.query(this.printQuery, query);
					flag = execute_NonSelectQuery(ps, args);
					if (flag) {
						args.clear();
					}
				}
			}
		} catch (Exception e) {
			//this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return flag;
	}

	public ArrayList getCustomerCreditSummaryView(Hashtable ht, String afrmDate, String atoDate, String plant,
			String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "", dtCondStr = "", extraCon = "";
		StringBuffer sql;
		Hashtable htData = new Hashtable();
		try {

			dtCondStr = "and ISNULL(A.CREDIT_DATE,'')<>'' AND CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + '-' + SUBSTRING(a.CREDIT_DATE, 4, 2) + '-' + SUBSTRING(a.CREDIT_DATE, 1, 2)) AS date)";
			extraCon = "order by A.ID desc,CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + SUBSTRING(A.CREDIT_DATE, 4, 2) + SUBSTRING(A.CREDIT_DATE, 1, 2)) AS date) desc";

			if (afrmDate.length() > 0) {
				sCondition = sCondition + dtCondStr + "  >= '" + afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" + atoDate + "'  ";
				}
			}

			if (custname.length() > 0) {
				custname = new StrUtils().InsertQuotes(custname);
				sCondition = sCondition + " AND V.CNAME LIKE '%" + custname + "%' ";
			}
			sql = new StringBuffer("select A.ID,CREDIT_DATE,INVOICE,A.DONO,A.CUSTNO,A.CREDITNOTE,A.REFERENCE,");
			sql.append(" V.CNAME,ISNULL((select CURRENCYID from " + plant
					+ "_DOHDR p where p.DONO=A.DONO ),ISNULL(CURRENCYID,'')) as CURRENCYID,");
			sql.append(" ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + plant +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1) CURRENCYUSEQT,");
			sql.append(" ISNULL((select BALANCE from " + plant
					+ "_FINRECEIVEDET p where p.CREDITNOTEHDRID=A.ID ),'') as BALANCE,");
			sql.append(" CREDIT_STATUS,cast(TOTAL_AMOUNT as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY
					+ ")) as TOTAL_AMOUNT");
			sql.append(" from " + plant + "_FINCUSTOMERCREDITNOTEHDR A JOIN " + plant
					+ "_CUSTMST V ON V.CUSTNO=A.CUSTNO WHERE A.PLANT='" + plant + "'" + sCondition);

			if (ht.get("INVOICE") != null) {
				sql.append(" AND A.INVOICE = '" + ht.get("INVOICE") + "'");
			}
			
			if (ht.get("CREDITNOTE") != null) {
				sql.append(" AND A.CREDITNOTE = '" + ht.get("CREDITNOTE") + "'");
			}
			
			if (ht.get("REFERENCE") != null) {
				sql.append(" AND A.REFERENCE = '" + ht.get("REFERENCE") + "'");
			}
			
			if (ht.get("CREDIT_STATUS") != null) {
				sql.append(" AND A.CREDIT_STATUS = '" + ht.get("CREDIT_STATUS") + "'");
			}

			System.out.println(sql.toString());
			arrList = selectForReport(sql.toString(), htData, extraCon);

		} catch (Exception e) {
			System.out.println(e);
		}
		return arrList;
	}

	public ArrayList selectForReport(String query, Hashtable ht, String extCond) throws Exception {
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

			// this.mLogger.query(this.printQuery, sql.toString());
			System.out.println(sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {

			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}

	public List getCustCreditnoteHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> custcrdnoteHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT A.PLANT,A.ID,A.CUSTNO,A.CREDITNOTE,A.DONO,ISNULL(A.GINO,'') GINO,ISNULL(A.SORETURN,'') SORETURN,A.CREDIT_DATE,ISNULL(ORDER_DISCOUNT,'0') ORDER_DISCOUNT,A.ITEM_RATES,A.EMPNO,A.SUB_TOTAL,A.SHIPPINGCOST,A.ADJUSTMENT,A.DISCOUNT,A.REFERENCE,A.INVOICE,A.TAXAMOUNT,"
					+"ISNULL((select BALANCE from " + (String)ht.get("PLANT") + "_FINRECEIVEDET p where p.CREDITNOTEHDRID=A.ID ),'') as BALANCE,ISNULL(ORDERDISCOUNTTYPE,'') as ORDERDISCOUNTTYPE,ISNULL(CURRENCYID,'') as CURRENCYID,"
					+ "ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1) CURRENCYUSEQT,"
					+ "A.DISCOUNT_TYPE,A.DISCOUNT_ACCOUNT,A.TOTAL_AMOUNT,A.CREDIT_STATUS,A.NOTE,A.TERMSCONDITIONS,A.CRAT,A.CRBY,A.UPAT,A.UPBY,A.ADJUSTMENT_LABEL,ISNULL(SALES_LOCATION,'') SALES_LOCATION FROM"
					+ "[" + (String)ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEHDR] AS A WHERE A.ID = ? AND A.PLANT =? ";
			
			
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));

			// this.mLogger.query(this.printQuery, query);

			custcrdnoteHdrList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return custcrdnoteHdrList;
	}
	public List getCov_CustCreditnoteHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> custcrdnoteHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT A.PLANT,A.ID,A.CUSTNO,A.CREDITNOTE,A.DONO,ISNULL(A.GINO,'') GINO,ISNULL(A.SORETURN,'') SORETURN,A.CREDIT_DATE,"
					+ "ISNULL(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,A.ITEM_RATES,A.EMPNO,ISNULL(CURRENCYID,'') as CURRENCYID,ISNULL(A.PROJECTID,0) PROJECTID,"
					+ "ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1) CURRENCYUSEQT,"
					+ "(ISNULL(A.SUB_TOTAL,0) * ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1)) SUB_TOTAL,"
					+ "(ISNULL(A.SHIPPINGCOST,0) * ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1)) SHIPPINGCOST,"
					+ "(ISNULL(A.ADJUSTMENT,0) * ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1)) ADJUSTMENT,"
					+ "(case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINCUSTOMERCREDITNOTEDET] WHERE HDRID = A.ID ),1)) end) DISCOUNT,"
					+ "(case when ISNULL(ORDERDISCOUNTTYPE,'%') ='%' then ISNULL(ORDER_DISCOUNT, 0) else (ISNULL(ORDER_DISCOUNT, 0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+ht.get("PLANT")+"_FINCUSTOMERCREDITNOTEDET] WHERE HDRID = A.ID ),1)) end) ORDER_DISCOUNT,"
					+ "A.REFERENCE,A.INVOICE,ISNULL(ISDISCOUNTTAX,'0') ISDISCOUNTTAX,ISNULL(ISORDERDISCOUNTTAX,'0') ISORDERDISCOUNTTAX,ISNULL(TAXID,'0') TAXID,"
					+ "(ISNULL(A.TAXAMOUNT,0) * ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1)) TAXAMOUNT,"
					+ "ISNULL((select BALANCE from " + (String)ht.get("PLANT") + "_FINRECEIVEDET p where p.CREDITNOTEHDRID=A.ID ),'') as BALANCE,"
					+ "A.DISCOUNT_TYPE,A.DISCOUNT_ACCOUNT,"
					+ "(ISNULL(A.TOTAL_AMOUNT,0) * ISNULL((SELECT TOP 1 B.CURRENCYUSEQT FROM " + ht.get("PLANT") +"_FINCUSTOMERCREDITNOTEDET B WHERE B.HDRID=A.ID),1)) TOTAL_AMOUNT,"
					+ "ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,"
					+ "A.CREDIT_STATUS,A.NOTE,A.TERMSCONDITIONS,A.CRAT,A.CRBY,A.UPAT,A.UPBY,A.ADJUSTMENT_LABEL,ISNULL(SALES_LOCATION,'') SALES_LOCATION FROM"
					+ "[" + (String)ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEHDR] AS A WHERE A.ID = ? AND A.PLANT =? ";
			
			
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));

			// this.mLogger.query(this.printQuery, query);

			custcrdnoteHdrList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return custcrdnoteHdrList;
	}
	
	public List getCustCreditnoteHdrByIdforupdate(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> custcrdnoteHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT * FROM [" + (String)ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEHDR] AS A WHERE A.ID = ? AND A.PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));

			// this.mLogger.query(this.printQuery, query);

			custcrdnoteHdrList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return custcrdnoteHdrList;
	}
	
	
	public List getCustCrdnoteDedtByHrdId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> custcrdnoteDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT PLANT,ID,LNNO,HDRID,ACCOUNT_NAME,ITEM,QTY,UNITPRICE,DISCOUNT,ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,TAX_TYPE,AMOUNT,CRAT FROM"
					+ "[" + ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEDET] WHERE HDRID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));

			// this.mLogger.query(this.printQuery, query);

			custcrdnoteDetList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return custcrdnoteDetList;
	}
	
	public List getConv_CustCrdnoteDedtByHrdId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> custcrdnoteDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query = "";
		List<String> args = null;
		try {
			/* Instantiate the list */
			args = new ArrayList<String>();

			con = com.track.gates.DbBean.getConnection();
			query = "SELECT PLANT,ID,LNNO,HDRID,ACCOUNT_NAME,ITEM,QTY,(ISNULL(UNITPRICE, 0)*CURRENCYUSEQT) UNITPRICE,"
					+ "(case when DISCOUNT_TYPE='%' then ISNULL(DISCOUNT, 0) else (ISNULL(DISCOUNT, 0)*CURRENCYUSEQT) end) DISCOUNT,ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,TAX_TYPE,"
					+ "(ISNULL(AMOUNT, 0)*CURRENCYUSEQT) AMOUNT,CRAT FROM"
					+ "[" + ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEDET] WHERE HDRID = ? AND PLANT =? ";
			PreparedStatement ps = con.prepareStatement(query);

			/* Storing all the query param argument in list squentially */
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));

			// this.mLogger.query(this.printQuery, query);

			custcrdnoteDetList = selectData(ps, args);

		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return custcrdnoteDetList;
	}
	
	public ArrayList getEditCreditnoteDetails(Hashtable ht, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			InvoiceDAO movHisDAO = new InvoiceDAO();
			//movHisDAO.setmLogger(mLogger);
	          
	           sql = new StringBuffer("select A.PLANT,A.ID,A.CUSTNO,A.CREDITNOTE,A.DONO,A.CREDIT_DATE,A.ITEM_RATES,ISNULL(A.ORDER_DISCOUNT,'') AS ORDER_DISCOUNT,ISNULL(GINO,'') AS GINO,A.SUB_TOTAL,A.SHIPPINGCOST,A.ADJUSTMENT,A.DISCOUNT,A.REFERENCE,A.INVOICE,");
	           sql.append(" EMPNO,ISNULL((SELECT FNAME FROM " + plant +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME,A.DISCOUNT_TYPE,A.DISCOUNT_ACCOUNT,A.TOTAL_AMOUNT,A.CREDIT_STATUS,A.NOTE,V.CNAME,");
	           sql.append(" ISNULL(CURRENCYID, '') CURRENCYID,ISNULL(B.CURRENCYUSEQT,0) CURRENCYUSEQT,ISNULL(A.SHIPPINGCUSTOMER, '') SHIPPINGCUSTOMER,ISNULL(A.PAYMENT_TERMS, '') PAYMENT_TERMS,ISNULL(A.SHIPPINGID, '') SHIPPINGID ,");
	           sql.append(" ISNULL(A.PROJECTID,0) PROJECTID,ISNULL(A.ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,ISNULL(A.TAXID,0) TAXID,ISNULL(A.ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(A.ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,ISNULL(A.OUTBOUD_GST,0) OUTBOUD_GST, ");
	           sql.append( "ISNULL((SELECT UNITPRICE FROM "+ ht.get("PLANT") +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as BASECOST,");
	           sql.append( "ISNULL((SELECT ITEMDESC FROM "+ ht.get("PLANT") +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as ITEMDESC,");
	           sql.append(" ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+ht.get("PLANT")+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,");
	           sql.append(" B.ID as DETID,B.DISCOUNT_TYPE as ITEM_DISCOUNT_TYPE,B.LNNO,B.HDRID,B.ACCOUNT_NAME,B.ITEM,B.QTY,B.UNITPRICE,B.DISCOUNT as ITEM_DISCOUNT,B.TAX_TYPE,B.AMOUNT,A.TERMSCONDITIONS,A.ADJUSTMENT_LABEL,");
	           sql.append(" ISNULL(SALES_LOCATION,'') SALES_LOCATION,ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE A.SALES_LOCATION = C.STATE),'') as STATE_PREFIX,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,");
	           sql.append(" ISNULL((SELECT CATLOGPATH FROM "+ plant +"_ITEMMST C WHERE B.ITEM = C.ITEM),'') as CATLOGPATH, ISNULL((SELECT COUNT(*) FROM " + plant +"_FINCUSTOMERCREDITNOTEATTACHMENTS N where A.ID=N.HDRID),0) as ATTACHNOTE_COUNT ");
	           sql.append(" from " + plant +"_FINCUSTOMERCREDITNOTEHDR A JOIN " + plant +"_FINCUSTOMERCREDITNOTEDET B ON A.ID=HDRID JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO WHERE A.PLANT='"+ plant+"'" + sCondition);			           
			           
                   
			            
 				if (ht.get("ID") != null) {
					  sql.append(" AND A.ID = '" + ht.get("ID") + "'");
				    }   
				   
				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
     
	
		 }catch (Exception e) {
			 System.out.println(e.getMessage());
		}
		return arrList;
	}
	
	 public int updateCreditNoteHdr(Hashtable ht) throws Exception {
		  int creditNoteHdrId = 0;

			Hashtable htCond = new Hashtable();
			htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
			htCond.put("ID", (String) ht
					.get("ID"));
			try {
				
				StringBuffer updateQyery = new StringBuffer("set ");

				updateQyery.append("CUSTNO" + " = '"
						+ (String) ht.get("CUSTNO") + "'");
				updateQyery.append("," + "INVOICE" + " = '"
						+ (String) ht.get("INVOICE") + "'");		
				updateQyery.append("," + "DONO" + " = '"
						+ (String) ht.get("DONO") + "'");				
				updateQyery.append("," + "CREDIT_DATE" + " = '"
						+ (String) ht.get("CREDIT_DATE") + "'");
				updateQyery.append("," + "CREDITNOTE" + " = '"
						+ (String) ht.get("CREDITNOTE") + "'");				
				updateQyery.append("," + "EMPNO" + " = '"
						+ (String) ht.get("EMPNO") + "'");		
				updateQyery.append("," + "ITEM_RATES" + " = '"
						+ (String) ht.get("ITEM_RATES") + "'");				
				updateQyery.append("," + "DISCOUNT" + " = '"
						+ (String) ht.get("DISCOUNT") + "'");
				updateQyery.append("," + "DISCOUNT_TYPE" + " = '"
						+ (String) ht.get("DISCOUNT_TYPE") + "'");
				updateQyery.append("," + "DISCOUNT_ACCOUNT" + " = '"
						+ (String) ht.get("DISCOUNT_ACCOUNT") + "'");
				updateQyery.append("," + "SHIPPINGCOST" + " = '"
						+ (String) ht.get("SHIPPINGCOST") + "'");		
				updateQyery.append("," + "ADJUSTMENT" + " = '"
						+ (String) ht.get("ADJUSTMENT") + "'");				
				updateQyery.append("," + "SUB_TOTAL" + " = '"
						+ (String) ht.get("SUB_TOTAL") + "'");
				updateQyery.append("," + "TOTAL_AMOUNT" + " = '"
						+ (String) ht.get("TOTAL_AMOUNT") + "'");				
				updateQyery.append("," + "CREDIT_STATUS" + " = '"
						+ (String) ht.get("CREDIT_STATUS") + "'");		
				updateQyery.append("," + "NOTE" + " = '"
						+ (String) ht.get("NOTE") + "'");				
				updateQyery.append("," + "TERMSCONDITIONS" + " = '"
						+ (String) ht.get("TERMSCONDITIONS") + "'");
				updateQyery.append("," + "UPAT" + " = '"
						+ (String) ht.get("UPAT") + "'");
				updateQyery.append("," + "UPBY" + " = '"
						+ (String) ht.get("UPBY") + "'");
				updateQyery.append("," + "ADJUSTMENT_LABEL" + " = '"
						+ (String) ht.get("ADJUSTMENT_LABEL") + "'");
				updateQyery.append("," + "REFERENCE" + " = '"
						+ (String) ht.get("REFERENCE") + "'");
				updateQyery.append("," + "TAXTREATMENT" + " = '"
						+ (String) ht.get("TAXTREATMENT") + "'");
				updateQyery.append("," + "TAXAMOUNT" + " = '"
						+ (String) ht.get("TAXAMOUNT") + "'");
				updateQyery.append("," + "GINO" + " = '"
						+ (String) ht.get("GINO") + "'");
				updateQyery.append("," + "SALES_LOCATION" + " = '"
						+ (String) ht.get("SALES_LOCATION") + "'");
				updateQyery.append("," + "CURRENCYUSEQT" + " = '"
						+ (String) ht.get("CURRENCYUSEQT") + "'");
				updateQyery.append("," + "ORDERDISCOUNTTYPE" + " = '"
						+ (String) ht.get("ORDERDISCOUNTTYPE") + "'");
				updateQyery.append("," + "TAXID" + " = '"
						+ (String) ht.get("TAXID") + "'");
				updateQyery.append("," + "ISDISCOUNTTAX" + " = '"
						+ (String) ht.get("ISDISCOUNTTAX") + "'");
				updateQyery.append("," + "ISORDERDISCOUNTTAX" + " = '"
						+ (String) ht.get("ISORDERDISCOUNTTAX") + "'");
				updateQyery.append("," + "OUTBOUD_GST" + " = '"
						+ (String) ht.get("OUTBOUD_GST") + "'");
				updateQyery.append("," + "PROJECTID" + " = '"
						+ (String) ht.get("PROJECTID") + "'");
				updateQyery.append("," + "SHIPPINGID" + " = '"
						+ (String) ht.get("SHIPPINGID") + "'");
				updateQyery.append("," + "SHIPPINGCUSTOMER" + " = '"
						+ (String) ht.get("SHIPPINGCUSTOMER") + "'");
				updateQyery.append("," + "PAYMENT_TERMS" + " = '"
						+ (String) ht.get("PAYMENT_TERMS") + "'");
				updateQyery.append("," + "SHIPCONTACTNAME" + " = '"
						+ (String) ht.get("SHIPCONTACTNAME") + "'");
				updateQyery.append("," + "SHIPDESGINATION" + " = '"
						+ (String) ht.get("SHIPDESGINATION") + "'");
				updateQyery.append("," + "SHIPWORKPHONE" + " = '"
						+ (String) ht.get("SHIPWORKPHONE") + "'");
				updateQyery.append("," + "SHIPHPNO" + " = '"
						+ (String) ht.get("SHIPHPNO") + "'");
				updateQyery.append("," + "SHIPEMAIL" + " = '"
						+ (String) ht.get("SHIPEMAIL") + "'");
				updateQyery.append("," + "SHIPCOUNTRY" + " = '"
						+ (String) ht.get("SHIPCOUNTRY") + "'");
				updateQyery.append("," + "SHIPADDR1" + " = '"
						+ (String) ht.get("SHIPADDR1") + "'");
				updateQyery.append("," + "SHIPADDR2" + " = '"
						+ (String) ht.get("SHIPADDR2") + "'");
				updateQyery.append("," + "SHIPADDR3" + " = '"
						+ (String) ht.get("SHIPADDR3") + "'");
				updateQyery.append("," + "SHIPADDR4" + " = '"
						+ (String) ht.get("SHIPADDR4") + "'");
				updateQyery.append("," + "SHIPSTATE" + " = '"
						+ (String) ht.get("SHIPSTATE") + "'");
				updateQyery.append("," + "SHIPZIP" + " = '"
						+ (String) ht.get("SHIPZIP") + "'");
		
				
				creditNoteHdrId =update(updateQyery.toString(), htCond,"");
			
	  } catch (Exception e) {
			throw new Exception("Edit Invoice Cannot be modified");
		}
		return creditNoteHdrId;
	  }
	 
	 public int update(String query, Hashtable htCondition, String extCond)
	            throws Exception {
	    boolean flag = false;
	    int creditNoteHdrId = 0;
	    java.sql.Connection con = null;
	    try {
	            con = com.track.gates.DbBean.getConnection();
	            StringBuffer sql = new StringBuffer(" UPDATE " + "["
	                            + htCondition.get("PLANT") + "_FINCUSTOMERCREDITNOTEHDR]");
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
	            		creditNoteHdrId=1;
	               
				

	    } catch (Exception e) {
	           
	            throw e;
	    } finally {
	            if (con != null) {
	                    DbBean.closeConnection(con);
	            }
	    }
	    return creditNoteHdrId;
	}
	 
	 public boolean deleteCreditNoteDet(String plant,String TranId)
		        throws Exception {
				boolean deleteprdForTranId = false;
				PreparedStatement ps = null;
				Connection con = null;
				try {
				        con = DbBean.getConnection();
				        
				        
				        String sQry = "DELETE FROM " + "[" + plant + "_FINCUSTOMERCREDITNOTEDET" + "]"
				                        + " WHERE HDRID ='"+TranId+"'";
				        
				        ps = con.prepareStatement(sQry);
				        int iCnt = ps.executeUpdate();
				        if (iCnt > 0)
				                deleteprdForTranId = true;
				} catch (Exception e) {
				      
				} finally {
				        DbBean.closeConnection(con, ps);
				}
				
				return deleteprdForTranId;
		}
	 public List getCustCrdnoteAttachByHrdId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> custcrdnoteAttachList = new ArrayList<>();
			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT * FROM"
						+ "[" + ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEATTACHMENTS] WHERE HDRID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));

				// this.mLogger.query(this.printQuery, query);

				custcrdnoteAttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return custcrdnoteAttachList;
		}
	 public List getCustCrdnoteAttachByPrimId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			List<Map<String, String>> custcrdnoteAttachList = new ArrayList<>();
			Map<String, String> map = null;
			String query = "";
			List<String> args = null;
			try {
				/* Instantiate the list */
				args = new ArrayList<String>();

				con = com.track.gates.DbBean.getConnection();
				query = "SELECT * FROM"
						+ "[" + ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEATTACHMENTS] WHERE ID = ? AND PLANT =? ";
				PreparedStatement ps = con.prepareStatement(query);

				/* Storing all the query param argument in list squentially */
				args.add((String) ht.get("ID"));
				args.add((String) ht.get("PLANT"));

				// this.mLogger.query(this.printQuery, query);

				custcrdnoteAttachList = selectData(ps, args);

			} catch (Exception e) {
				// this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return custcrdnoteAttachList;
		}
	 public int deleteCreditAttachByPrimId(Hashtable ht) throws Exception {
			java.sql.Connection con = null;
			Map<String, String> map = null;
			String query="";
			int count=0;
			try {			
			    
				con = com.track.gates.DbBean.getConnection();
				query="DELETE FROM " + "[" + ht.get("PLANT") + "_FINCUSTOMERCREDITNOTEATTACHMENTS] "
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
	 public boolean isExisit(Hashtable ht, String extCond) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM [" + ht.get("A.PLANT") + "_FINCUSTOMERCREDITNOTEHDR] A ");
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

	 public ArrayList getCustomerCreditNote(Hashtable ht, String afrmDate, String atoDate, String plant,
				String custname) {
			ArrayList arrList = new ArrayList();
			String sCondition = "", dtCondStr = "", extraCon = "";
			StringBuffer sql;
			Hashtable htData = new Hashtable();
			try {

				dtCondStr = "and ISNULL(A.CREDIT_DATE,'')<>'' AND CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + '-' + SUBSTRING(a.CREDIT_DATE, 4, 2) + '-' + SUBSTRING(a.CREDIT_DATE, 1, 2)) AS date)";
				extraCon = "order by A.ID desc,CAST((SUBSTRING(A.CREDIT_DATE, 7, 4) + SUBSTRING(A.CREDIT_DATE, 4, 2) + SUBSTRING(A.CREDIT_DATE, 1, 2)) AS date) desc";

				if (afrmDate.length() > 0) {
					sCondition = sCondition + dtCondStr + "  >= '" + afrmDate + "'  ";
					if (atoDate.length() > 0) {
						sCondition = sCondition + dtCondStr + " <= '" + atoDate + "'  ";
					}
				} else {
					if (atoDate.length() > 0) {
						sCondition = sCondition + dtCondStr + " <= '" + atoDate + "'  ";
					}
				}

				if (custname.length() > 0) {
					custname = new StrUtils().InsertQuotes(custname);
					sCondition = sCondition + " AND V.CNAME LIKE '%" + custname + "%' ";
				}
				 sql = new StringBuffer("SELECT ID,CREDIT_DATE,INVOICE,DONO,CUSTNO,CREDITNOTE,REFERENCE,CNAME,CURRENCYID,BALANCE,CREDIT_STATUS,TOTAL_AMOUNT,");
		           sql.append(" cast(TOTAL_AMOUNT * ISNULL((SELECT CURRENCYUSEQT FROM ["+ plant +"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1) as DECIMAL(20,5)) as BASE_TOTAL_AMOUNT, ");
		           sql.append(" cast(ISNULL((select BALANCE from ["+ plant +"_FINRECEIVEDET] p where p.CREDITNOTEHDRID=A.ID ),0) as DECIMAL(20,5)) * ISNULL((SELECT CURRENCYUSEQT FROM ["+ plant +"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID),1) as BASE_BALANCE");
		           sql.append(" FROM (select A.ID,CREDIT_DATE,INVOICE,A.DONO,A.CUSTNO,A.CREDITNOTE,A.REFERENCE, V.CNAME,");
		           sql.append(" ISNULL((select CURRENCYID from ["+ plant +"_DOHDR] p where p.DONO=A.DONO ),'') as CURRENCYID, ");
		           sql.append(" ISNULL((select BALANCE from ["+ plant +"_FINRECEIVEDET] p where p.CREDITNOTEHDRID=A.ID ),'') as BALANCE, CREDIT_STATUS,");
		           sql.append(" cast(TOTAL_AMOUNT as DECIMAL(20,5)) as TOTAL_AMOUNT from ["+ plant +"_FINCUSTOMERCREDITNOTEHDR] A JOIN ["+ plant +"_CUSTMST] V ON V.CUSTNO=A.CUSTNO");
		           sql.append(" WHERE A.PLANT='"+ plant +"'" + sCondition);

				if (ht.get("INVOICE") != null) {
					sql.append(" AND A.INVOICE = '" + ht.get("INVOICE") + "'");
				}
				
				if (ht.get("CREDITNOTE") != null) {
					sql.append(" AND A.CREDITNOTE = '" + ht.get("CREDITNOTE") + "'");
				}
				
				if (ht.get("REFERENCE") != null) {
					sql.append(" AND A.REFERENCE = '" + ht.get("REFERENCE") + "'");
				}
				
				if (ht.get("CREDIT_STATUS") != null) {
					sql.append(" AND A.CREDIT_STATUS = '" + ht.get("CREDIT_STATUS") + "'");
				}
				sql.append(" ) A ");
				System.out.println(sql.toString());
				arrList = selectForReport(sql.toString(), htData, extraCon);

			} catch (Exception e) {
				System.out.println(e);
			}
			return arrList;
		}
	 
	 public boolean deleteCustomerCrdnote(String plant,String TranId)
		        throws Exception {
				boolean deleteprdForTranId = false;
				PreparedStatement ps = null;
				PreparedStatement pshdr = null;
				PreparedStatement psatt = null;
				Connection con = null;
				try {
				        con = DbBean.getConnection();
				        
				        
				        String sQry = "DELETE FROM " + "[" + plant + "_FINCUSTOMERCREDITNOTEHDR" + "]"
				                        + " WHERE ID ='"+TranId+"'";
				        this.mLogger.query(this.printQuery, sQry);
				        ps = con.prepareStatement(sQry);
				        int iCnt = ps.executeUpdate();
				        if (iCnt > 0)
				                deleteprdForTranId = true;
				        if(deleteprdForTranId) {
				        sQry = "DELETE FROM " + "[" + plant + "_FINCUSTOMERCREDITNOTEDET" + "]"
		                        + " WHERE HDRID ='"+TranId+"'";
				        this.mLogger.query(this.printQuery, sQry);
				        pshdr = con.prepareStatement(sQry);
				        int iCnthdr = pshdr.executeUpdate();
				       
				     
				         sQry = "DELETE FROM " + "[" + plant + "_FINCUSTOMERCREDITNOTEATTACHMENTS" + "]"
			                    + " WHERE HDRID ='"+TranId+"'";
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
	 
	 public boolean isExisit(Hashtable ht) throws Exception {
		 boolean flag = false;
		 java.sql.Connection con = null;
		 try {
			 con = com.track.gates.DbBean.getConnection();
			 
			 StringBuffer sql = new StringBuffer(" SELECT ");
			 sql.append("COUNT(*) ");
			 sql.append(" ");
			 sql.append(" FROM " + "["+ht.get("PLANT")+"_FINCUSTOMERCREDITNOTEHDR]");
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
	 
	 public boolean isExisitTo(Hashtable ht) throws Exception {
		 boolean flag = false;
		 java.sql.Connection con = null;
		 try {
			 con = com.track.gates.DbBean.getConnection();
			 
			 StringBuffer sql = new StringBuffer(" SELECT ");
			 sql.append("COUNT(*) ");
			 sql.append(" ");
			 sql.append(" FROM " + "["+ht.get("PLANT")+"_TOHDR]");
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
}
