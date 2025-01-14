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
import com.track.db.object.DoHdr;
import com.track.db.object.estHdr;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class EstHdrDAO extends BaseDAO {

	StrUtils _StrUtils = null;
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.ESTHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ESTHDRDAO_PRINTPLANTMASTERLOG;
	public static String TABLE_HEADER = "ESTHDR";
	
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

	public EstHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public static String TABLE_NAME = "ESTHDR";
	public static String plant = "";

	public EstHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_" + "ESTHDR" + "]";
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "ESTHDR" + "]");
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
	public Map selectcustRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "CUSTMST" + "]");
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

	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "ESTHDR" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

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

	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_ESTHDR]");
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

	public boolean isExisit(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_ESTHDR]");
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
 
	public ArrayList selectOutGoingESTHDR(String query, Hashtable ht)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "ESTHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "CUSTMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.estno ='"
						+ ht.get("ESTNO") + "' and a.CustCode=b.custno";

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

	public ArrayList selectESTHDR(String query, Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "ESTHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				this.mLogger.log(0, "condition preisent stage 3");
				sql.append(" WHERE ");

				conditon = formCondition(ht);

				sql.append(conditon);

			}
			//this.mLogger.query(this.printQuery, sql.toString());
			System.out.println(sql.toString());
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

	 public String getOrderTypeForEST(String aPlant, String aEstno) throws Exception {
          String OrderType = "";
          Hashtable<String, String> ht = new Hashtable();
          ht.put("PLANT", aPlant);
          ht.put("ESTNO", aEstno);
          String query = " case  ordertype when '' then 'ESTIMATE ORDER' else upper(isnull(ordertype,'')) end  AS  ORDERTYPE ";
          Map m = selectRow(query, ht);
          OrderType = (String) m.get("ORDERTYPE");
          return OrderType;
  }
	
	public ArrayList selectESTHDR(String query, Hashtable ht, String extCond)
			throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		
		String plant = (String) ht.get("PLANT");

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ plant + "_" + "ESTHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = formCondition(ht);

				sql.append(conditon);

			}
			if (extCond.length() > 0)
				sql.append(" " + extCond);
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

	public boolean insertESTHDR(Hashtable ht) throws Exception {
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "ESTHDR" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Estimate order created already");
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}
	/* creted by navas */
	 public boolean insertestHdr(estHdr esthdr) throws Exception {
			boolean insertFlag = false;
			boolean flag = false;
			int HdrId = 0;
			Connection connection = null;
			PreparedStatement ps = null;
		    String query = "";
			try {	    
				connection = DbBean.getConnection();
				query = "INSERT INTO ["+esthdr.getPLANT()+"_"+TABLE_HEADER+"]" + 
						"      ([PLANT]" + 
						"      ,[ESTNO]" + 
				   		"      ,[ORDDATE]" + 
				   		"      ,[ORDERTYPE]" + 
				   		"      ,[DELDATE]" + 
				   		"      ,[STATUS]" + 
				   		"      ,[CRAT]" + 
				   		"      ,[CRBY]" + 
				   		"      ,[UPAT]" + 
				   		"      ,[UPBY]" + 
				   		"      ,[CustCode]" + 
				   		"      ,[CustName]" + 
				   		"      ,[JobNum]" + 
				   		"      ,[PersonInCharge]" + 
				   		"      ,[contactNum]" + 
				   		"      ,[Address]" + 
				   		"      ,[Address2]" + 
				   		"      ,[Address3]" + 
				   		"      ,[CollectionDate]" + 
				   		"      ,[CollectionTime]" + 
				   		"      ,[Remark1]" + 
				   		"      ,[Remark2]" + 
				   		"      ,[Remark3]" + 
				   		"      ,[SHIPPINGID]" + 
				   		"      ,[SHIPPINGCUSTOMER]" + 
				   		"      ,[CURRENCYID]" + 
				   		"      ,[DELIVERYDATE]" + 
				   		"      ,[TIMESLOTS]" + 
				   		"      ,[OUTBOUND_GST]" + 
				   		"      ,[STATUS_ID]" + 
				   		"      ,[EXPIREDATE]" + 
				   		"      ,[SHIPCONTACTNAME]" +
						"      ,[SHIPDESGINATION]" +
						"      ,[SHIPWORKPHONE]" +
						"      ,[SHIPHPNO]" +
						"      ,[SHIPEMAIL]" +
						"      ,[SHIPCOUNTRY]" +
						"      ,[SHIPADDR1]" +
						"      ,[SHIPADDR2]" +
						"      ,[SHIPADDR3]" +
						"      ,[SHIPADDR4]" +
						"      ,[SHIPSTATE]" +
						"      ,[SHIPZIP]" +
				   		"      ,[DONO]" + 
				   		"      ,[EMPNO]" + 
				   		"      ,[ORDERDISCOUNT]" + 
				   		"      ,[SHIPPINGCOST]" + 
				   		"      ,[INCOTERMS]" + 
				   		"      ,[PAYMENTTYPE]" + 
				   		"      ,[DELIVERYDATEFORMAT]" + 
				   		"      ,[APPROVESTATUS]" + 
				   		"      ,[SALES_LOCATION]" + 
				   		"      ,[ORDER_STATUS]" + 
				   		"      ,[DISCOUNT]" + 
				   		"      ,[DISCOUNT_TYPE]" +
						"      ,[ADJUSTMENT]" + 
						"      ,[ITEM_RATES]" +
						"      ,[CURRENCYUSEQT]" +	
						"      ,[ORDERDISCOUNTTYPE]" +
						"      ,[TAXID]" +
						"      ,[ISDISCOUNTTAX]" +
						"      ,[ISORDERDISCOUNTTAX]" +
						"      ,[ISSHIPPINGTAX]" +
						"      ,[PROJECTID]" +
						"      ,[TAXTREATMENT]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				if(connection != null){
				   ps = connection.prepareStatement(query);
				   ps.setString(1, esthdr.getPLANT());
				   ps.setString(2, esthdr.getESTNO());
				   ps.setString(3, esthdr.getORDDATE());
				   ps.setString(4, esthdr.getORDERTYPE());
				   ps.setString(5, esthdr.getDELDATE());
				   ps.setString(6, esthdr.getSTATUS());
				   ps.setString(7, esthdr.getCRAT());
				   ps.setString(8, esthdr.getCRBY());
				   ps.setString(9, esthdr.getUPAT());
				   ps.setString(10, esthdr.getUPBY());
				   ps.setString(11, esthdr.getCustCode());
				   ps.setString(12, esthdr.getCustName());
				   ps.setString(13, esthdr.getJobNum());
				   ps.setString(14, esthdr.getPersonInCharge());
				   ps.setString(15, esthdr.getContactNum());
				   ps.setString(16, esthdr.getAddress());
				   ps.setString(17, esthdr.getAddress2());
				   ps.setString(18, esthdr.getAddress3());
				   ps.setString(19, esthdr.getCollectionDate());
				   ps.setString(20, esthdr.getCollectionTime());
				   ps.setString(21, esthdr.getRemark1());
				   ps.setString(22, esthdr.getRemark2());
				   ps.setString(23, esthdr.getRemark3());
				   ps.setString(24, esthdr.getSHIPPINGID());
				   ps.setString(25, esthdr.getSHIPPINGCUSTOMER());
				   ps.setString(26, esthdr.getCURRENCYID());
				   ps.setString(27, esthdr.getDELIVERYDATE());
				   ps.setString(28, esthdr.getTIMESLOTS());
				   ps.setString(29, Double.toString(esthdr.getOUTBOUND_GST()));
				   ps.setString(30, esthdr.getSTATUS_ID());
				   ps.setString(31, esthdr.getEXPIREDATE());
				   ps.setString(32, esthdr.getSHIPCONTACTNAME());
					ps.setString(33, esthdr.getSHIPDESGINATION());
					ps.setString(34, esthdr.getSHIPWORKPHONE());
					ps.setString(35, esthdr.getSHIPHPNO());
					ps.setString(36, esthdr.getSHIPEMAIL());
					ps.setString(37, esthdr.getSHIPCOUNTRY());
					ps.setString(38, esthdr.getSHIPADDR1());
					ps.setString(39, esthdr.getSHIPADDR2());
					ps.setString(40, esthdr.getSHIPADDR3());
					ps.setString(41, esthdr.getSHIPADDR4());
					ps.setString(42, esthdr.getSHIPSTATE());
					ps.setString(43, esthdr.getSHIPZIP());
				   ps.setString(44, esthdr.getDONO());
				   ps.setString(45, esthdr.getEMPNO());
				  
				   ps.setString(46, Double.toString(esthdr.getORDERDISCOUNT()));
				   ps.setString(47, Double.toString(esthdr.getSHIPPINGCOST()));
				   ps.setString(48, esthdr.getINCOTERMS());
				   ps.setString(49, esthdr.getPAYMENTTYPE());
				   ps.setString(50, Short.toString(esthdr.getDELIVERYDATEFORMAT()));
				   ps.setString(51, esthdr.getAPPROVESTATUS());
				   ps.setString(52, esthdr.getSALES_LOCATION());
				   ps.setString(53, esthdr.getORDER_STATUS());
				   ps.setString(54, Double.toString(esthdr.getDISCOUNT()));
				   ps.setString(55, esthdr.getDISCOUNT_TYPE());
				   ps.setString(56, Double.toString(esthdr.getADJUSTMENT()));
				   ps.setString(57, Short.toString(esthdr.getITEM_RATES()));
				 ps.setDouble(58, esthdr.getCURRENCYUSEQT());
				 ps.setString(59, esthdr.getORDERDISCOUNTTYPE());
				 ps.setInt(60, esthdr.getTAXID());
				 ps.setShort(61, esthdr.getISDISCOUNTTAX());
				 ps.setShort(62, esthdr.getISORDERDISCOUNTTAX());
				 ps.setShort(63, esthdr.getISSHIPPINGTAX());
				 ps.setInt(64, esthdr.getPROJECTID());
				   ps.setString(65, esthdr.getTAXTREATMENT());
				   
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   insertFlag = true;
				   }
				   else
				   {
					   throw new SQLException("Creating Sales estimate failed, no rows affected.");
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
	
	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "ESTHDR" + "]");
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
	
	
	 public String getUnitCostBasedOnCurIDSelected(String aPlant, String estNO,String estlnno,String aItem) throws Exception {
	      java.sql.Connection con = null;
	      String UnitCostForSelCurrency = "";
	      try {
	              
	              con = DbBean.getConnection();
	     

	      StringBuffer SqlQuery = new StringBuffer(" select CAST(UnitPrice *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_ESTHDR WHERE ESTNO ='"+estNO+"')) AS DECIMAL(20,2)) AS UNITCOST ");
	      SqlQuery.append(" from "+aPlant+"_estdet where item='"+aItem+"' and estno ='"+estNO+"' and estlnno ='"+estlnno+"' ");

	      
	      System.out.println(SqlQuery.toString());
	          Map m = this.getRowOfData(con, SqlQuery.toString());

	          UnitCostForSelCurrency = (String) m.get("UNITCOST");

	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return UnitCostForSelCurrency;
	  }
	
	 public String getUnitCostCovertedTolocalCurrency(String aPlant, String estNO,String unitCost) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select cast ("+unitCost+" / CAST((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_ESTHDR WHERE ESTNO =R.ESTNO)) AS DECIMAL(20,5)) ");
         SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST FROM "+aPlant+"_ESTHDR R WHERE ESTNO='"+estNO+"' ");

          System.out.println(SqlQuery.toString());
             Map m = this.getRowOfData(con, SqlQuery.toString());

             UnitCostForSelCurrency = (String) m.get("UNITCOST");

         } catch (Exception e) {
                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return UnitCostForSelCurrency;
     }
	 
	 public String getUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String estNO,String aItem) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select CAST(UnitPrice *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_ESTHDR WHERE ESTNO ='"+estNO+"')) AS DECIMAL(20,5)) AS UNITCOST ");
         SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

         
         System.out.println(SqlQuery.toString());
             Map m = this.getRowOfData(con, SqlQuery.toString());

             UnitCostForSelCurrency = (String) m.get("UNITCOST");

         } catch (Exception e) {
                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return UnitCostForSelCurrency;
     }
public String getminSellingUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String estNO,String aItem) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select CAST(MINSPRICE *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_ESTHDR WHERE ESTNO ='"+estNO+"')) AS DECIMAL(20,5)) AS UNITCOST ");
         SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

         
         System.out.println(SqlQuery.toString());
             Map m = this.getRowOfData(con, SqlQuery.toString());

             UnitCostForSelCurrency = (String) m.get("UNITCOST");

         } catch (Exception e) {
                 this.mLogger.exception(this.printLog, "", e);
                 throw e;
         } finally {
                 if (con != null) {
                         DbBean.closeConnection(con);
                 }
         }
         return UnitCostForSelCurrency;
     } 	 
	 
	public String getCurrencyUseQT(String plant, String estNO) throws Exception {
		java.sql.Connection con = null;
		String getCurrencyUseQT = "";
		try {

			con = DbBean.getConnection();

			StringBuffer SqlQuery = new StringBuffer(
					" SELECT CURRENCYUSEQT FROM [" + plant + "_"
							+ "CURRENCYMST] where plant='" + plant
							+ "' AND CURRENCYID = (SELECT CURRENCYID from "
							+ plant + "_ESTHDR WHERE ESTNO ='" + estNO + "')");

			System.out.println(SqlQuery.toString());
			Map m = this.getRowOfData(con, SqlQuery.toString());

			getCurrencyUseQT = (String) m.get("CURRENCYUSEQT");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return getCurrencyUseQT;
	}
   	
	public String getNextOrder(String Plant) throws Exception {
		String MaxEstNo = "";
		try {
			String query = " isnull(max(" + "ESTNO" + "),'')  as ESTNO";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", Plant);

			String extCond = " substring(ESTNO,2,4)='" + DateUtils.getDateYYMM()
					+ "'";
			Map m = selectRow(query, ht, extCond);
			MaxEstNo = (String) m.get("ESTNO");

			if (MaxEstNo.length() == 0 || MaxEstNo.equalsIgnoreCase(null)
					|| MaxEstNo == "") {
				MaxEstNo = DateUtils.getDateYYMM() + "00000";
			}
			String temp = MaxEstNo.replace('O', '0');

			int nextNum = Integer.parseInt(temp) + 1;
			MaxEstNo = String.valueOf(nextNum);

			if (MaxEstNo.length() < 9)
				MaxEstNo = "0" + MaxEstNo;
			MaxEstNo = "O" + MaxEstNo;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return MaxEstNo;
	}
	
	public Boolean removeOrder(String plant2, String estno) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_" + "ESTHDR] WHERE ESTNO='"
					+ estno + "' AND PLANT='" + plant2 + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			this.mLogger.log(0, "[ERROR] : " + e);
			return Boolean.valueOf(false);
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	}
	
	public Map getESTReciptHeaderDetails(String plant) throws Exception {

        MLogger.log(1, this.getClass() + " getESTReciptHeaderDetails()");
        Map m = new HashMap();
        java.sql.Connection con = null;
        String scondtn ="";
        try {
                
                con = DbBean.getConnection();
                
                StringBuffer sql = new StringBuffer("  SELECT  ");
                sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,");//
                sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM ,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4 ");
                sql.append(" FROM " + "[" + plant + "_" + "ESTIMATE_RECIPT_HDR] where plant='"+plant+"'");
               
                this.mLogger.query(this.printQuery, sql.toString());
                m = getRowOfData(con, sql.toString());

        } catch (Exception e) {

                this.mLogger.exception(this.printLog, "", e);
                throw e;
        } finally {
                if (con != null) {
                        DbBean.closeConnection(con);
                }
        }
        return m;

}	
 public Map getESTReciptInvoiceHeaderDetails(String plant) throws Exception {

        MLogger.log(1, this.getClass() + " getESTReciptHeaderDetails()");
        Map m = new HashMap();
        java.sql.Connection con = null;
        String scondtn ="";
        try {                 
                con = DbBean.getConnection();
                StringBuffer sql = new StringBuffer("  SELECT  ");
                sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(PROJECT,'') AS PROJECT,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTCUSTERMS,'0') AS PRINTCUSTERMS,ISNULL(PCUSREMARKS,'0') AS PCUSREMARKS,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS,");
                sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(AMT,'') AS AMT, ");
                sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,ISNULL(TOTAL,'Total With Tax') AS TOTAL,'Email' AS EMAIL,'Fax' AS FAX,'Tel' AS TELEPHONE,");
                sql.append(" ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
                sql.append(" ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(ADJUSTMENT,'') AS ADJUSTMENT, ");
                sql.append(" ISNULL(UOM,'') AS UOM,ISNULL(PrintOrientation,'Landscape') AS PrintOrientation,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND, ");
                sql.append(" ISNULL(REMARK1,'') AS REMARK1,ISNULL(REMARK2,'') AS REMARK2,ISNULL(EMPLOYEENAME,'') AS EMPLOYEENAME,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(PRINTWITHEMPLOYEE,'0') AS PRINTWITHEMPLOYEE,ISNULL(CUSTOMERRCBNO,'') AS CUSTOMERRCBNO, ");
                sql.append(" ISNULL(DELIVERYDATE,'') AS DELIVERYDATE,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRINTWITHHSCODE,'') AS PRINTWITHHSCODE,ISNULL(PRINTWITHCOO,'') AS PRINTWITHCOO,ISNULL(PRINTWITHHSCODE,'0') AS PRINTWITHHSCODE, ");
                sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(CUSTOMERUENNO,'') AS CUSTOMERUENNO,ISNULL(BRAND,'') AS BRAND,ISNULL(HSCODE,'') AS HSCODE,ISNULL(COO,'') AS COO,ISNULL(COMPANYNAME,'') AS COMPANYNAME,ISNULL(COMPANYDATE,'') AS COMPANYDATE,ISNULL(COMPANYSIG,'') AS COMPANYSIG,ISNULL(COMPANYSTAMP,'') AS COMPANYSTAMP,ISNULL(TOTALAFTERDISCOUNT,'') AS TOTALAFTERDISCOUNT,ISNULL(SHIPTO,'') AS SHIPTO,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(AUTHSIGNATURE,'') AS AUTHSIGNATURE,ISNULL(ROUNDOFFTOTALWITHDECIMAL,'') AS ROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTROUNDOFFTOTALWITHDECIMAL,'0') AS PRINTROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTWITHPRODUCT,'0') AS PRINTWITHPRODUCT,ISNULL(PRINTWITHDISCOUNT,'0') AS PRINTWITHDISCOUNT,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(NETRATE,'') NETRATE, ");
                sql.append(" ISNULL(PRINTWITHSHIPINGADD,'0') AS PRINTWITHSHIPINGADD,ISNULL(PRINTSHIPPINGCOST,'0') AS PRINTSHIPPINGCOST,ISNULL(PRINTADJUSTMENT,'0') AS PRINTADJUSTMENT,ISNULL(PRINTORDERDISCOUNT,'0') AS PRINTORDERDISCOUNT, ");
                sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTWITHCUSTOMERUENNO,'0') AS PRINTWITHCUSTOMERUENNO,ISNULL(CALCULATETAXWITHSHIPPINGCOST,'0') AS CALCULATETAXWITHSHIPPINGCOST,ISNULL(PRINTWITHREFNO,'0') AS PRINTWITHREFNO ");
                sql.append(" FROM " + "[" + plant + "_"+ "ESTIMATE_RECIPT_INVOICE_HDR] where plant='"+plant+"'");
                
                this.mLogger.query(this.printQuery, sql.toString());
                m = getRowOfData(con, sql.toString());

        } catch (Exception e) {

                this.mLogger.exception(this.printLog, "", e);
                throw e;
        } finally {
                if (con != null) {
                        DbBean.closeConnection(con);
                }
        }
        return m;

}
 
 public boolean updateEOReciptHeader(String query, Hashtable htCondition, String extCond)
	     throws Exception {
				boolean flag = false;
				java.sql.Connection con = null;
			try {
				     con = com.track.gates.DbBean.getConnection();
				     StringBuffer sql = new StringBuffer(" UPDATE " + "["
				                     + htCondition.get("PLANT") + "_ESTIMATE_RECIPT_HDR]");
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



	public boolean updateEOReciptInvoiceHeader(String query, Hashtable htCondition, String extCond)
	throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                + htCondition.get("PLANT") + "_ESTIMATE_RECIPT_INVOICE_HDR]");
		sql.append(" ");
		sql.append(query);
		
		sql.append(" WHERE ");
		String conditon = formCondition(htCondition);
		sql.append(conditon);
		
		
		
		if (extCond.length() != 0) {
		        sql.append(extCond);
		}
		this.mLogger.query(this.printQuery, sql.toString());
		System.out.println("sql.toString()");
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

	public String getUnitCostBasedOnCurIDSelectedForOrderWTC(String aPlant, String estNO,String aItem) throws Exception {
        java.sql.Connection con = null;
        String UnitCostForSelCurrency = "";
        try {
                
                con = DbBean.getConnection();
       

        StringBuffer SqlQuery = new StringBuffer(" select isnull(UnitPrice,0) *isnull((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_ESTHDR WHERE ESTNO ='"+estNO+"')),0) AS UNITCOST ");
        SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

        
        System.out.println(SqlQuery.toString());
            Map m = this.getRowOfData(con, SqlQuery.toString());

            UnitCostForSelCurrency = (String) m.get("UNITCOST");

        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e;
        } finally {
                if (con != null) {
                        DbBean.closeConnection(con);
                }
        }
        return UnitCostForSelCurrency;
    }
	 	
	public ArrayList getEstDetailForSalesOrder(Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		
		String plant = (String) ht.get("a.PLANT");
		String item =  (String) ht.get("b.ITEM");

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer("select a.ESTNO, a.CollectionDate, a.EXPIREDATE, a.CustName, b.ITEM, SUM(b.QTYOR) QTYOR "+ 
				"FROM ["+plant+"_ESTHDR] a join ["+plant+"_ESTDET] b on a.ESTNO = b.ESTNO");
		String conditon = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			sql.append(" AND CASE WHEN ((ISNULL(EXPIREDATE,'') <> '')) THEN"+
				" CAST((SUBSTRING(isnull(EXPIREDATE,''), 7, 4) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 4, 2) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 1, 2)) AS date) " + 
				" ELSE convert(date, getdate(), 23) END >= convert(date, getdate(), 23)  GROUP BY a.ESTNO, a.CollectionDate, a.EXPIREDATE, a.CustName, b.ITEM");
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
	
	public ArrayList getPendingSalesEstimate(String plant, String numberOfDecimal) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			String aQuery = "SELECT A.ESTNO,DELIVERYDATE,CustName,CollectionDate,"
					+ "CONVERT(DATE, A.CollectionDate, 103) AS DEL_DATE, "
					+"SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+"))) AS TOTAL_PRICE"
					+ " FROM ["+plant+"_ESTHDR] A JOIN ["+plant+"_ESTDET] B ON A.ESTNO=B.ESTNO  WHERE B.STATUS <> 'C' "
					+ "GROUP BY A.ESTNO,CUSTNAME,CollectionDate,A.DELIVERYDATE  "
					+ "ORDER BY CONVERT(DATE, COLLECTIONDATE, 103)";
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
	
	public ArrayList getLastTranEstDetail(Hashtable ht) throws Exception {
		boolean flag = false;
		ArrayList alData = new ArrayList();
		String plant = (String) ht.get("a.PLANT");
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer("SELECT TOP 1 * FROM ["+plant+"_ESTHDR] a join ["+plant+"_ESTDET] b on a.ESTNO = b.ESTNO");
		String conditon = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			if (ht.size() > 0) {
				sql.append(" WHERE ");
				conditon = formCondition(ht);
				sql.append(conditon);
			}
			sql.append(" ORDER BY CollectionDate DESC");
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

	/* CREATED BY NAVAS */
public List<estHdr> getestHdr(Hashtable ht, String afrmDate, String atoDate) throws Exception {
	   boolean flag = false;
	   int journalHdrId = 0;
	   Connection connection = null;
	   PreparedStatement ps = null;
	   String query = "", fields = "", dtCondStr = "";
	   List<estHdr> doHeaders = new ArrayList<estHdr>();
	   List<String> args = null;
	   try {
		   args = new ArrayList<String>();
		   connection = DbBean.getConnection();
		   query = "SELECT [PLANT]" + 
					"      ,[ESTNO]" + 
			   		"      ,[ORDDATE]" + 
			   		"      ,[ORDERTYPE]" + 
			   		"      ,[DELDATE]" + 
			   		"      ,[STATUS]" + 
			   		"      ,[CRAT]" + 
			   		"      ,[CRBY]" + 
			   		"      ,[UPAT]" + 
			   		"      ,[UPBY]" + 
			   		"      ,[CustCode]" + 
			   		"      ,[CustName]" + 
			   		"      ,[JobNum]" + 
			   		"      ,[PersonInCharge]" + 
			   		"      ,[contactNum]" + 
			   		"      ,[Address]" + 
			   		"      ,[Address2]" + 
			   		"      ,[Address3]" + 
			   		"      ,[CollectionDate]" + 
			   		"      ,[CollectionTime]" + 
			   		"      ,[Remark1]" + 
			   		"      ,[Remark2]" + 
			   		"      ,[Remark3]" + 
			   		"      ,[SHIPPINGID]" + 
			   		"      ,[SHIPPINGCUSTOMER]" + 
			   		"      ,[CURRENCYID]" + 
			   		"      ,[DELIVERYDATE]" + 
			   		"      ,[TIMESLOTS]" + 
			   		"      ,[OUTBOUND_GST]" + 
			   		"      ,[STATUS_ID]" + 
			   		"      ,[EXPIREDATE]" + 
			   		"      ,[SHIPCONTACTNAME]" +
					"           ,[SHIPDESGINATION]" +
					"           ,[SHIPWORKPHONE]" +
					"           ,[SHIPHPNO]" +
					"           ,[SHIPEMAIL]" +
					"           ,[SHIPCOUNTRY]" +
					"           ,[SHIPADDR1]" +
					"           ,[SHIPADDR2]" +
					"           ,[SHIPADDR3]" +
					"           ,[SHIPADDR4]" +
					"           ,[SHIPSTATE]" +
					"           ,[SHIPZIP]" +
			   		"      ,[DONO]" + 
			   		"      ,[EMPNO]" + 
			   		"      ,[ORDERDISCOUNT]" + 
			   		"      ,[SHIPPINGCOST]" + 
			   		"      ,[INCOTERMS]" + 
			   		"      ,[PAYMENTTYPE]" + 
			   		"      ,ISNULL(DELIVERYDATEFORMAT,0) DELIVERYDATEFORMAT" + 
			   		"      ,[APPROVESTATUS]" + 
			   		"      ,[SALES_LOCATION]" + 
			   		"      ,[DISCOUNT]" + 
			   		"      ,ISNULL([DISCOUNT_TYPE], '') DISCOUNT_TYPE" +
					"      ,[ADJUSTMENT]" + 
					"      ,ISNULL([ITEM_RATES], 0) ITEM_RATES" +
					"      ,ISNULL([CURRENCYUSEQT], 0) CURRENCYUSEQT" +	
					"      ,ISNULL([ORDERDISCOUNTTYPE], '') ORDERDISCOUNTTYPE" +
					"      ,ISNULL([TAXID], 0) TAXID" +
					"      ,ISNULL([ISDISCOUNTTAX], 0) ISDISCOUNTTAX" +
					"      ,ISNULL([ISORDERDISCOUNTTAX], 0) ISORDERDISCOUNTTAX" +
					"      ,ISNULL([ISSHIPPINGTAX], 0) ISSHIPPINGTAX" +
					"      ,ISNULL([PROJECTID], '') PROJECTID" +
					"      ,[TAXTREATMENT]" + 
					
		   		"      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS FROM ["+ ht.get("PLANT") +"_"+TABLE_HEADER+"] WHERE ";
		   Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enum1.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				fields += key + "=? AND ";
				args.add(value);
			}			
			query += fields.substring(0, fields.length() - 5);
			
			dtCondStr =" AND ISNULL(CollectionDate,'')<>'' AND CAST((SUBSTRING(CollectionDate, 7, 4) + '-' + SUBSTRING(CollectionDate, 4, 2) + '-' + SUBSTRING(CollectionDate, 1, 2)) AS date)";
			
			if (afrmDate.length() > 0) {
				query +=  dtCondStr + "  >= '" + afrmDate + "' ";
				if (atoDate.length() > 0) {
					query += dtCondStr+ " <= '" + atoDate + "' ";
				}
			} else {
				if (atoDate.length() > 0) {
					query += dtCondStr+ " <= '" + atoDate + "' ";
				}
			}
			
			query += " ORDER BY  CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) DESC , ESTNO DESC";
			ps = connection.prepareStatement(query);
			if(connection != null){
				int i = 1;
				for (Object arg : args) {         
				    if (arg instanceof Integer) {
				        ps.setInt(i++, (Integer) arg);
				    } else if (arg instanceof Long) {
				        ps.setLong(i++, (Long) arg);
				    } else if (arg instanceof Double) {
				        ps.setDouble(i++, (Double) arg);
				    } else if (arg instanceof Float) {
				        ps.setFloat(i++, (Float) arg);
				    } else {
				        ps.setNString(i++, (String) arg);
				    }
				}
			    /* Execute the Query */  
				ResultSet rst  = ps.executeQuery();
				while (rst.next()) {	
					estHdr esthdr = new estHdr();
					ResultSetToObjectMap.loadResultSetIntoObject(rst, esthdr);
					doHeaders.add(esthdr);
				}
			}			
	   }catch (Exception e) {
		   this.mLogger.exception(this.printLog, "", e);
			throw e;
	   } finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
	   return doHeaders;
	}

public estHdr getestHdrById(String plant,String estno)throws Exception {
	boolean flag = false;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
    estHdr esthdr = new estHdr();
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT [PLANT]" + 
				"      ,[ESTNO]" + 
		   		"      ,[ORDDATE]" + 
		   		"      ,[ORDERTYPE]" + 
		   		"      ,[DELDATE]" + 
		   		"      ,[STATUS]" + 
		   		"      ,[CRAT]" + 
		   		"      ,[CRBY]" + 
		   		"      ,[UPAT]" + 
		   		"      ,[UPBY]" + 
		   		"      ,[CustCode]" + 
		   		"      ,[CustName]" + 
		   		"      ,[JobNum]" + 
		   		"      ,[PersonInCharge]" + 
		   		"      ,[contactNum]" + 
		   		"      ,[Address]" + 
		   		"      ,[Address2]" + 
		   		"      ,[Address3]" + 
		   		"      ,[CollectionDate]" + 
		   		"      ,[CollectionTime]" + 
		   		"      ,[Remark1]" + 
		   		"      ,[Remark2]" + 
		   		"      ,[Remark3]" + 
		   		"      ,[SHIPPINGID]" + 
		   		"      ,[SHIPPINGCUSTOMER]" + 
		   		"      ,[CURRENCYID]" + 
		   		"      ,[DELIVERYDATE]" + 
		   		"      ,[TIMESLOTS]" + 
		   		"      ,[OUTBOUND_GST]" + 
		   		"      ,[STATUS_ID]" + 
		   		"      ,[EXPIREDATE]" + 
		   		"      ,[SHIPCONTACTNAME]" +
				"           ,[SHIPDESGINATION]" +
				"           ,[SHIPWORKPHONE]" +
				"           ,[SHIPHPNO]" +
				"           ,[SHIPEMAIL]" +
				"           ,[SHIPCOUNTRY]" +
				"           ,[SHIPADDR1]" +
				"           ,[SHIPADDR2]" +
				"           ,[SHIPADDR3]" +
				"           ,[SHIPADDR4]" +
				"           ,[SHIPSTATE]" +
				"           ,[SHIPZIP]" +
		   		"      ,[DONO]" + 
		   		"      ,[EMPNO]" + 
		   		"      ,CASE WHEN ISNULL(ORDERDISCOUNTTYPE,'0') = '%' THEN ORDERDISCOUNT ELSE (ORDERDISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS ORDERDISCOUNT" +
		   		"      ,(ISNULL([SHIPPINGCOST], 0) * ISNULL(CURRENCYUSEQT,1)) SHIPPINGCOST" +
		   		"      ,[INCOTERMS]" + 
		   		"      ,[PAYMENTTYPE]" + 
		   		"      ,ISNULL(DELIVERYDATEFORMAT,0) DELIVERYDATEFORMAT" + 
		   		"      ,[APPROVESTATUS]" + 
		   		"      ,[SALES_LOCATION]" + 
		   		"      ,[ORDER_STATUS]" + 
		   		"      ,CASE WHEN ISNULL(DISCOUNT_TYPE,'0') = '%' THEN ISNULL([DISCOUNT], 0) ELSE (ISNULL([DISCOUNT], 0) * ISNULL(CURRENCYUSEQT,1))END AS DISCOUNT" +
		   		"      ,ISNULL([DISCOUNT_TYPE], '') DISCOUNT_TYPE" +
		   		"      ,(ISNULL([ADJUSTMENT], 0) * ISNULL(CURRENCYUSEQT,1)) ADJUSTMENT" +
				"      ,ISNULL([ITEM_RATES], 0) ITEM_RATES" +
				"      ,ISNULL([CURRENCYUSEQT], 0) CURRENCYUSEQT" +	
				"      ,ISNULL([ORDERDISCOUNTTYPE], '') ORDERDISCOUNTTYPE" +
				"      ,ISNULL([TAXID], 0) TAXID" +
				"      ,ISNULL([ISDISCOUNTTAX], 0) ISDISCOUNTTAX" +
				"      ,ISNULL([ISORDERDISCOUNTTAX], 0) ISORDERDISCOUNTTAX" +
				"      ,ISNULL([ISSHIPPINGTAX], 0) ISSHIPPINGTAX" +
				"      ,ISNULL([PROJECTID], '') PROJECTID" +
				"      ,[TAXTREATMENT]" + 
		   		"      ,ISNULL(ORDER_STATUS,'Open') ORDER_STATUS FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE "+
		   		" ESTNO=? AND PLANT=?";

		if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ps.setString(1, estno);
			   ps.setString(2, plant);
			   ResultSet rst = ps.executeQuery();
			   while (rst.next()) {
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, esthdr);
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
	return esthdr;
}

	/*NAVAS EDITED DEC24 */
public boolean updateestHdr(estHdr esthdr) throws Exception {
	boolean updateFlag = false;
	boolean flag = false;
	int HdrId = 0;
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
	try {	    
		connection = DbBean.getConnection();
		query = "UPDATE ["+esthdr.getPLANT()+"_"+TABLE_HEADER+"] SET" + 
				"       [PLANT] = ?" + 
				"      ,[ESTNO] = ?" + 
		   		"      ,[ORDDATE] = ?" + 
		   		"      ,[ORDERTYPE] = ?" + 
		   		"      ,[DELDATE] = ?" + 
		   		"      ,[STATUS] = ?" + 
		   		"      ,[CRAT] = ?" + 
		   		"      ,[CRBY] = ?" + 
		   		"      ,[UPAT] = ?" + 
		   		"      ,[UPBY] = ?" + 
		   		"      ,[CustCode] = ?" + 
		   		"      ,[CustName] = ?" + 
		   		"      ,[JobNum] = ?" + 
		   		"      ,[PersonInCharge] = ?" + 
		   		"      ,[contactNum] = ?" + 
		   		"      ,[Address] = ?" + 
		   		"      ,[Address2] = ?" + 
		   		"      ,[Address3] = ?" + 
		   		"      ,[CollectionDate] = ?" + 
		   		"      ,[CollectionTime] = ?" + 
		   		"      ,[Remark1] = ?" + 
		   		"      ,[Remark2] = ?" + 
		   		"      ,[Remark3] = ?" + 
		   		"      ,[SHIPPINGID] = ?" + 
		   		"      ,[SHIPPINGCUSTOMER] = ?" + 
		   		"      ,[CURRENCYID] = ?" + 
		   		"      ,[DELIVERYDATE] = ?" + 
		   		"      ,[TIMESLOTS] = ?" + 
		   		"      ,[OUTBOUND_GST] = ?" + 
		   		"      ,[STATUS_ID] = ?" + 
		   		"      ,[EXPIREDATE] = ?" + 
		   		"			,[SHIPCONTACTNAME] = ?" +
				"           ,[SHIPDESGINATION] = ?" +
				"           ,[SHIPWORKPHONE] = ?" +
				"           ,[SHIPHPNO] = ?" +
				"           ,[SHIPEMAIL] = ?" +
				"           ,[SHIPCOUNTRY] = ?" +
				"           ,[SHIPADDR1] = ?" +
				"           ,[SHIPADDR2] = ?" +
				"           ,[SHIPADDR3] = ?" +
				"           ,[SHIPADDR4] = ?" +
				"           ,[SHIPSTATE] = ?" +
				"           ,[SHIPZIP] = ?" +
		   		"      ,[DONO] = ?" + 
		   		"      ,[EMPNO] = ?" + 
		   		"      ,[ORDERDISCOUNT] = ?" + 
		   		"      ,[SHIPPINGCOST] = ?" + 
		   		"      ,[INCOTERMS] = ?" + 
		   		"      ,[PAYMENTTYPE] = ?" + 
		   		"      ,[DELIVERYDATEFORMAT] = ?" + 
		   		"      ,[APPROVESTATUS] = ?" + 
		   		"      ,[SALES_LOCATION] = ?" + 
		   		"      ,[ORDER_STATUS] = ?" + 
		   		"      ,[DISCOUNT] = ?" + 
		   		"      ,[DISCOUNT_TYPE] = ?" +
				"      ,[ADJUSTMENT] = ?" + 
				"      ,[ITEM_RATES] = ?" +
				"      ,[CURRENCYUSEQT] = ?" +	
				"      ,[ORDERDISCOUNTTYPE] = ?" +
				"      ,[TAXID] = ?" +
				"      ,[ISDISCOUNTTAX] = ?" +
				"      ,[ISORDERDISCOUNTTAX] = ?" +
				"      ,[ISSHIPPINGTAX] = ?" +
				"      ,[PROJECTID] = ?" +
				"      ,[TAXTREATMENT] = ? WHERE [ESTNO] = ?";

		if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, esthdr.getPLANT());
			   ps.setString(2, esthdr.getESTNO());
			   ps.setString(3, esthdr.getORDDATE());
			   ps.setString(4, esthdr.getORDERTYPE());
			   ps.setString(5, esthdr.getDELDATE());
			   ps.setString(6, esthdr.getSTATUS());
			   ps.setString(7, esthdr.getCRAT());
			   ps.setString(8, esthdr.getCRBY());
			   ps.setString(9, esthdr.getUPAT());
			   ps.setString(10, esthdr.getUPBY());
			   ps.setString(11, esthdr.getCustCode());
			   ps.setString(12, esthdr.getCustName());
			   ps.setString(13, esthdr.getJobNum());
			   ps.setString(14, esthdr.getPersonInCharge());
			   ps.setString(15, esthdr.getContactNum());
			   ps.setString(16, esthdr.getAddress());
			   ps.setString(17, esthdr.getAddress2());
			   ps.setString(18, esthdr.getAddress3());
			   ps.setString(19, esthdr.getCollectionDate());
			   ps.setString(20, esthdr.getCollectionTime());
			   ps.setString(21, esthdr.getRemark1());
			   ps.setString(22, esthdr.getRemark2());
			   ps.setString(23, esthdr.getRemark3());
			   ps.setString(24, esthdr.getSHIPPINGID());
			   ps.setString(25, esthdr.getSHIPPINGCUSTOMER());
			   ps.setString(26, esthdr.getCURRENCYID());
			   ps.setString(27, esthdr.getDELIVERYDATE());
			   ps.setString(28, esthdr.getTIMESLOTS());
			   ps.setString(29, Double.toString(esthdr.getOUTBOUND_GST()));
			   ps.setString(30, esthdr.getSTATUS_ID());
			   ps.setString(31, esthdr.getEXPIREDATE());
			   ps.setString(32, esthdr.getSHIPCONTACTNAME());
				ps.setString(33, esthdr.getSHIPDESGINATION());
				ps.setString(34, esthdr.getSHIPWORKPHONE());
				ps.setString(35, esthdr.getSHIPHPNO());
				ps.setString(36, esthdr.getSHIPEMAIL());
				ps.setString(37, esthdr.getSHIPCOUNTRY());
				ps.setString(38, esthdr.getSHIPADDR1());
				ps.setString(39, esthdr.getSHIPADDR2());
				ps.setString(40, esthdr.getSHIPADDR3());
				ps.setString(41, esthdr.getSHIPADDR4());
				ps.setString(42, esthdr.getSHIPSTATE());
				ps.setString(43, esthdr.getSHIPZIP());
			    ps.setString(44, esthdr.getDONO());
			    ps.setString(45, esthdr.getEMPNO());
			  
			   ps.setString(46, Double.toString(esthdr.getORDERDISCOUNT()));
			   ps.setString(47, Double.toString(esthdr.getSHIPPINGCOST()));
			   ps.setString(48, esthdr.getINCOTERMS());
			   ps.setString(49, esthdr.getPAYMENTTYPE());
			   ps.setString(50, Short.toString(esthdr.getDELIVERYDATEFORMAT()));
			   ps.setString(51, esthdr.getAPPROVESTATUS());
			   ps.setString(52, esthdr.getSALES_LOCATION());
			   ps.setString(53, esthdr.getORDER_STATUS());
			   ps.setString(54, Double.toString(esthdr.getDISCOUNT()));
			   ps.setString(55, esthdr.getDISCOUNT_TYPE());
			   ps.setString(56, Double.toString(esthdr.getADJUSTMENT()));
			   ps.setString(57, Short.toString(esthdr.getITEM_RATES()));
			 ps.setDouble(58, esthdr.getCURRENCYUSEQT());
			 ps.setString(59, esthdr.getORDERDISCOUNTTYPE());
			 ps.setInt(60, esthdr.getTAXID());
			 ps.setShort(61, esthdr.getISDISCOUNTTAX());
			 ps.setShort(62, esthdr.getISORDERDISCOUNTTAX());
			 ps.setShort(63, esthdr.getISSHIPPINGTAX());
			 ps.setInt(64, esthdr.getPROJECTID());
			   ps.setString(65, esthdr.getTAXTREATMENT());
			   //EDIT
			   ps.setString(66, esthdr.getESTNO());
			   
		   int count=ps.executeUpdate();
		   if(count>0)
		   {
			   updateFlag = true;
		   }
		   else
		   {
			   throw new SQLException("Creating Estimate Order failed, no rows affected.");
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

	/* created by resvi */
public String getOBDiscountSelectedItemByCustomer(String aPlant, String custcode,String aItem,String aType) throws Exception {
    java.sql.Connection con = null;
    String OBDiscount= "";
    String CustCode="";
    try {
    con = DbBean.getConnection();
     
    StringBuffer SqlQuery = new StringBuffer(" SELECT ISNULL(OBDISCOUNT,'0.00') OBDISCOUNT FROM ["+aPlant+"_MULTI_PRICE_MAPPING]  WHERE  ITEM='"+aItem+"' AND CUSTOMER_TYPE_ID IN ");
    SqlQuery.append(" (SELECT CUSTOMER_TYPE_ID FROM ["+aPlant+"_CUSTMST] WHERE CUSTNO  ='" + custcode + "')");
    System.out.println(SqlQuery.toString());
    Map m = this.getRowOfData(con, SqlQuery.toString());
    
    if(m.size()>0)
    {
         OBDiscount=(String) m.get("OBDISCOUNT");
         if(OBDiscount.equals("") || OBDiscount.equalsIgnoreCase(null))
         {
             OBDiscount="0";
         }
         else
         {
         	OBDiscount = (String) m.get("OBDISCOUNT");
         }
    }

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return OBDiscount;
}



public String getminSellingUnitCostBasedOnCurIDSelectedForOrderByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
            con = DbBean.getConnection();
   

    StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(MINSPRICE,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
    SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

    
    System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());

        UnitCostForSelCurrency = (String) m.get("UNITCOST");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return UnitCostForSelCurrency;
}
//RESVI START
public int Estimatecount(String plant, String afrmDate, String atoDate)
throws Exception {
PreparedStatement ps = null;
ResultSet rs = null;
int Salescount = 0;
String sCondition = "";
String dtCondStr = " AND (SUBSTRING(ORDDATE, 7, 4) + '-' + SUBSTRING(ORDDATE, 4, 2) + '-' + SUBSTRING(ORDDATE, 1, 2))";
if (afrmDate.length() > 0) {
sCondition = " " + dtCondStr +" >= '" + afrmDate
+ "' ";
if (atoDate.length() > 0) {
sCondition = sCondition + " " + dtCondStr +" <= '" + atoDate
+ "' ";
}
}
Connection con = null;
try {
con = DbBean.getConnection();
String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
+ "ESTHDR" + "]" + " WHERE " + IConstants.PLANT
+ " = '" + plant.toUpperCase() + "'"+ sCondition;
this.mLogger.query(this.printQuery, sQry);
ps = con.prepareStatement(sQry);
rs = ps.executeQuery();
while (rs.next()) {
Salescount = rs.getInt(1);
}
} catch (Exception e) {
this.mLogger.exception(this.printLog, "", e);
} finally {
DbBean.closeConnection(con, ps);
}
return Salescount;
}

//RESVI ENDS
public String getUnitCostBasedOnCurIDSelectedForOrderByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
            con = DbBean.getConnection();
   

    StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(UnitPrice,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
    SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

    
    System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());

        UnitCostForSelCurrency = (String) m.get("UNITCOST");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return UnitCostForSelCurrency;
}
public String getUnitCostBasedOnCurIDSelectedForOrderWTCByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
            con = DbBean.getConnection();
   

    StringBuffer SqlQuery = new StringBuffer(" select isnull(UnitPrice,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') AS UNITCOST ");
    SqlQuery.append(" from "+aPlant+"_itemmst where item='"+aItem+"' ");

    
    System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());

        UnitCostForSelCurrency = (String) m.get("UNITCOST");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return UnitCostForSelCurrency;
}

}


