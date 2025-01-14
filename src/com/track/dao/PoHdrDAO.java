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
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PoHdrDAO extends BaseDAO {

	public static String plant = "";
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.POHDRDAO_PRINTPLANTMASTERLOG;

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

	public PoHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public PoHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_POHDR" + "]";
	}

	public static String TABLE_NAME = "POHDR";

	public Boolean removeOrder(String plant, String pono) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant + "_" + "POHDR] WHERE PONO='"
					+ pono + "' AND PLANT='" + plant + "'";
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

	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "POHDR" + "]");
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
					+ "[" + ht.get("PLANT") + "_" + "POHDR" + "]");
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
        
    public String getOrderTypeForPO(String aPlant, String aPono) throws Exception {
            String OrderType = "";

            Hashtable ht = new Hashtable();
            ht.put("PLANT", aPlant);
            ht.put("PONO", aPono);
            String query = " case  isnull(ordertype,'')  when '' then 'INBOUND ORDER' else upper(ordertype) end AS ORDERTYPE  ";
            Map m = selectRow(query, ht);
            OrderType = (String) m.get("ORDERTYPE");
            return OrderType;
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "POHDR" + "]");
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
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "POHDR" + "]");
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

	public ArrayList selectPoHdr(String query, Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "POHDR" + "]");
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

	public ArrayList selectPoHdr(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "POHDR" + "]");

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
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}

	public ArrayList selectPoHdrNewOrders(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "POHDR" + "] a, [" + ht.get("PLANT")
				+ "_" + "PODET" + "] b WHERE a.pono=b.pono AND a.PLANT='"
				+ ht.get("PLANT") + "' ");

//		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();
			if (extCond.length() > 0)
				sql.append(" " + extCond);

			sql.append(" " + " group by a.pono, a.CustName, a.collectiondate,a.status,a.Remark1 order by CAST((SUBSTRING(collectiondate, 7, 4) + SUBSTRING(collectiondate, 4, 2) + SUBSTRING(collectiondate, 1, 2)) AS date )desc ");
			this.mLogger.query(this.printQuery, sql.toString());

			alData = selectData(con, sql.toString());

		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
	}
        
    public ArrayList selectPoHdrNewOrdersByItem(String query, Hashtable ht,
                    String extCond) throws Exception {
//            boolean flag = false;
            ArrayList alData = new ArrayList();
            java.sql.Connection con = null;

            StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
                            + ht.get("PLANT") + "_" + "POHDR" + "] a, [" + ht.get("PLANT")
                            + "_" + "PODET" + "] b WHERE a.pono=b.pono AND a.PLANT='"+ ht.get("PLANT") + "' AND b.ITEM like'"+ ht.get("ITEM") + "%' ");

//            String conditon = "";

            try {
                    con = com.track.gates.DbBean.getConnection();
                    if (extCond.length() > 0)
                            sql.append(" " + extCond);

                    this.mLogger.query(this.printQuery, sql.toString());

                    alData = selectData(con, sql.toString());

            } catch (Exception e) {
                    throw e;
            } finally {
                    if (con != null) {
                            DbBean.closeConnection(con);
                    }
            }
            return alData;
    }

	public ArrayList selectPoHdrReciving(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "POHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "VENDMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.pono like'"
						+ ht.get("PONO") + "%' and a.custname=b.vname";

				sql.append(conditon);

			}
			extCond = "";
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

	public ArrayList selectSupplierHdrReciving(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "POHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "VENDMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.pono ='"
						+ ht.get("PONO") + "' and a.CustCode=b.vendno";

				sql.append(conditon);

			}
			extCond = "";
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

	 //not by Draft - azees
	public ArrayList selectSupplierHdrRecivingWithext(String query, Hashtable ht,
			String extCond) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "POHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "VENDMST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.pono ='"
						+ ht.get("PONO") + "' and a.CustCode=b.vendno";

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

	
	public boolean insertPoHdr(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "POHDR" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("incoming order created already");
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	
	public boolean updatePO(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "POHDR" + "]");
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
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}

	public String getMaxPO(String Plant) throws Exception {
		String MaxDo = "";
		try {
			String query = " max(" + "DoNO" + ")  as DoNO";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", Plant);
			Map m = selectRow(query, ht);
			MaxDo = (String) m.get("DoNO");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return MaxDo;

	}

	public String getNextOrder(String Plant) throws Exception {
		String MaxDo = "";
		try {
			String query = " isnull(max(" + "PoNO" + "),'')  as PoNO";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", Plant);
			String extCond = " substring(PoNo,2,4)='" + DateUtils.getDateYYMM()
					+ "'";
			Map m = selectRow(query, ht, extCond);
			MaxDo = (String) m.get("PoNO");
			if (MaxDo.length() == 0 || MaxDo.equalsIgnoreCase(null)
					|| MaxDo == "") {
				MaxDo = DateUtils.getDateYYMM() + "00000";
			}
			String temp = MaxDo.replace('I', '0');

			int nextNum = Integer.parseInt(temp) + 1;
			MaxDo = String.valueOf(nextNum);
			if (MaxDo.length() < 9)
				MaxDo = "0" + MaxDo;
			MaxDo = "I" + MaxDo;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return MaxDo;
	}
	public String getCurrencyID(String aPlant, String aPono) throws Exception {
		String currencyid = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("PONO", aPono);

		String query = " isnull(Currencyid,'') as currencyid ";
		
		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			currencyid = (String) m.get("currencyid");
		} else {
			currencyid = "";
		}
		if (currencyid.equalsIgnoreCase(null) || currencyid.length() == 0) {
			currencyid = "";
		}
		return currencyid;
	}
        
    //By Samatha to get unit price based on the Currency ID selected for IBOrder
     public String getUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String aPONO,String aItem) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select CAST(ISNULL(COST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO ='"+aPONO+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST ");
                 //StringBuffer SqlQuery = new StringBuffer(" select ISNULL(COST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO ='"+aPONO+"'))  AS UNITCOST ");
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
     
     public String getUnitCostBasedOnCurIDSelectedForOrderCurrency(String aPlant, String currency,String aItem) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select CAST(ISNULL(COST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"') AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST ");
                 //StringBuffer SqlQuery = new StringBuffer(" select ISNULL(COST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO ='"+aPONO+"'))  AS UNITCOST ");
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
     
    public String getUnitCostCovertedTolocalCurrency(String aPlant, String aPONO,String unitCost) throws Exception {
        java.sql.Connection con = null;
        String UnitCostForSelCurrency = "";
        try {
                
                con = DbBean.getConnection();
       

        StringBuffer SqlQuery = new StringBuffer(" select  ("+unitCost+" / CAST((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO =R.PONO)) AS DECIMAL(20,5)) ");
        //SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST FROM "+aPlant+"_POHDR R WHERE PONO='"+aPONO+"' ");
        SqlQuery.append(" ) AS UNITCOST FROM "+aPlant+"_POHDR R WHERE PONO='"+aPONO+"' ");

        
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
    

	public String getOrderType(String aPlant, String aPono) throws Exception {
		String orderType = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("PONO", aPono);

		String query = " isnull(ORDERTYPE,'') as ORDERTYPE ";
		
		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			orderType = (String) m.get("ORDERTYPE");
		} else {
			orderType = "";
		}
		if (orderType.equalsIgnoreCase(null) || orderType.length() == 0) {
			orderType = "";
		}
		return orderType;
	}
	
	 public Map getPOReciptHeaderDetails(String plant) throws Exception {

         MLogger.log(1, this.getClass() + " getPOSReciptHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
//         String scondtn ="";
         try {
                 
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE ,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTSUPTERMS,'0') AS PRINTSUPTERMS,ISNULL(PSUPREMARKS,'0') AS PSUPREMARKS,");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM ,ISNULL(PROJECT,'') AS PROJECT ,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5,ISNULL(PROJECT,'') AS PROJECT,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,   ISNULL(EMPLOYEE,'') AS EMPLOYEE,ISNULL(PRINTEMPLOYEE,'0') AS PRINTEMPLOYEE,  ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE, ");
                 sql.append(" ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(GRNO,'') GRNO,ISNULL(GRNDATE,'') GRNDATE, ");
                 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(SHIPTO,'') SHIPTO,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTBARCODE,'0') AS PRINTBARCODE,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(PRINTRECMLOC,'0') AS PRINTRECMLOC,ISNULL(PRINTWITHPRODUCTREMARKS,'0') AS PRINTWITHPRODUCTREMARKS, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM,ISNULL(RCBNO,'') AS RCBNO,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(SUPPLIERRCBNO,'') AS SUPPLIERRCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(SUPPLIERUENNO,'') AS SUPPLIERUENNO,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(AUTHSIGNATURE,'') AS AUTHSIGNATURE, ");
                 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHSUPPLIERUENNO,'0') AS PRINTWITHSUPPLIERUENNO,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE ");
                 sql.append(" FROM " + "[" + plant + "_" + "INBOUND_RECIPT_HDR] where plant='"+plant+"'");

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
	 
	 public Map getPOReciptInvoiceHeaderDetails(String plant) throws Exception {
         MLogger.log(1, this.getClass() + " getPOSReciptHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
//         String scondtn ="";
         try {
                 con = DbBean.getConnection();
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                 sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTSUPTERMS,'0') AS PRINTSUPTERMS,ISNULL(PSUPREMARKS,'0') as PSUPREMARKS, ");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(AMT,'') AS AMT, ");
                 sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,ISNULL(TOTAL,'Total With Tax') AS TOTAL,ISNULL(UOM,'') AS UOM,ISNULL(EMPLOYEE,'') AS EMPLOYEE,ISNULL(PRINTEMPLOYEE,'0') AS PRINTEMPLOYEE, ISNULL(PROJECT,'') AS PROJECT,ISNULL(ADJUSTMENT,'') AS ADJUSTMENT , ");
                 sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
                 sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(BILLNO,'') AS BILLNO,ISNULL(BILLDATE,'') AS BILLDATE, ");
   				 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(SHIPTO,'')SHIPTO, "); 
                 sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTWITHDISCOUNT,'0') PRINTWITHDISCOUNT,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(NETRATE,'') NETRATE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                 sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM, ISNULL(SHIPTO, '') AS SHIPTO,ISNULL(RCBNO, '') AS RCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(SUPPLIERUENNO,'') AS SUPPLIERUENNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') PRINTWITHBRAND,ISNULL(SUPPLIERRCBNO,'') SUPPLIERRCBNO,ISNULL(GRNO,'') GRNO,ISNULL(TOTALAFTERDISCOUNT,'') TOTALAFTERDISCOUNT,ISNULL(GRNDATE,'') GRNDATE,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(AUTHSIGNATURE,'') AS AUTHSIGNATURE,ISNULL(ROUNDOFFTOTALWITHDECIMAL,'') AS ROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTROUNDOFFTOTALWITHDECIMAL,'0') AS PRINTROUNDOFFTOTALWITHDECIMAL, ");
                 sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE,ISNULL(PRINTWITHSUPPLIERUENNO,'0') AS PRINTWITHSUPPLIERUENNO,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(SHOWPREVIOUSPURCHASECOST,'0') AS SHOWPREVIOUSPURCHASECOST,ISNULL(CALCULATETAXWITHSHIPPINGCOST,'0') AS CALCULATETAXWITHSHIPPINGCOST ");
                 sql.append(" FROM " + "[" + plant + "_"+ "INBOUND_RECIPT_INVOICE_HDR] where plant='"+plant+"'");
          
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
	 
		public boolean updatePOReciptHeader(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_INBOUND_RECIPT_HDR]");
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
		
 

		public boolean updatePOReciptInvoiceHeader(String query, Hashtable htCondition, String extCond)
		throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                + htCondition.get("PLANT") + "_INBOUND_RECIPT_INVOICE_HDR]");
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
		
		 public ArrayList getSupplierList(String plant,String custname) throws Exception {

		        java.sql.Connection con = null;
		        ArrayList al = new ArrayList();
		        try {
		                con = com.track.gates.DbBean.getConnection();
		                UserLocUtil userLocUtil = new UserLocUtil();
		                userLocUtil.setmLogger(mLogger);
		                         
		               
		                String sQry = "select distinct isnull(custcode,'') custcode ,isnull(custname,'') custname  from "
		                                + "["
		                                + plant
		                                + "_"
		                                + "pohdr] where plant='"
		                                + plant
		                                + "' and custcode like '"+custname+"%' or custname like  '"+custname+"%' " 
		                                + " ORDER BY custcode ";
		                this.mLogger.query(this.printQuery, sQry);
		                System.out.println(sQry);

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
		 
		 
	public String getCurrencyUseQT(String plant, String aPono)
			throws Exception {
		java.sql.Connection con = null;
		String getCurrencyUseQT = "";
		try {

			con = DbBean.getConnection();

			StringBuffer SqlQuery = new StringBuffer(
					" SELECT CURRENCYUSEQT FROM [" + plant + "_"
							+ "CURRENCYMST] where plant='" + plant
							+ "' AND CURRENCYID = (SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO ='"+aPono+"')");

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
        
    //By Samatha to get unit cost based on the Currency ID selected for IBOrder on 20/09/2013
     public String getUnitCostBasedOnCurIDSelected(String aPlant, String aPONO,String aPolnno,String aItem) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select CAST(UNITCOST *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO ='"+aPONO+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST ");
         SqlQuery.append(" from "+aPlant+"_PODET where item='"+aItem+"' and PONO='"+aPONO+"' and POLNNO='"+aPolnno+"' ");

         
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
    public ArrayList selectIBRecvList(String query, Hashtable ht, String extCond)
 			throws Exception {
// 		boolean flag = false;
 		ArrayList alData = new ArrayList();
 		java.sql.Connection con = null;

 		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
 				+ ht.get("PLANT") + "_" + "RECVDET" + "] A");

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
 			
 			//sql.append("ORDER BY GRNO DESC");
 			this.mLogger.query(this.printQuery, sql.toString());
          	alData = selectData(con, sql.toString());

 		} catch (Exception e) {
 			throw e;
 		} finally {
 			if (con != null) {
 				DbBean.closeConnection(con);
 			}
 		}
 		return alData;
 	}
     
    public String getSuppliercode(String aPlant, String aPono) throws Exception {
		String custcode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("PONO", aPono);

		String query = " isNull(CustCode,'') as custcode ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht);

		custcode = (String) m.get("custcode");
		return custcode;
	}	
		    
    public String getIBDiscountSelectedItem(String aPlant, String poNO,String aItem) throws Exception {
        java.sql.Connection con = null;
        String IBDiscount= "";
//        String CustCode="";
        try {
        con = DbBean.getConnection();
//        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
        String suppliercode =getDiscountSupplierCode(aPlant,poNO);
        StringBuffer SqlQuery = new StringBuffer(" SELECT ISNULL(IBDISCOUNT,'0.00') IBDISCOUNT FROM ["+aPlant+"_MULTI_COST_MAPPING]  WHERE  ITEM='"+aItem+"' AND VENDNO ='" + suppliercode + "'");
        System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());
        
        if(m.size()>0)
        {
	            IBDiscount=(String) m.get("IBDISCOUNT");
	            if(IBDiscount.equals("") || IBDiscount.equalsIgnoreCase(null))
	            {
	                IBDiscount="0";
	            }
	            else
	            {
	            	IBDiscount = (String) m.get("IBDISCOUNT");
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
        return IBDiscount;
    }
    
    public String getIBDiscountSelectedItemVNO(String aPlant, String suppliercode,String aItem) throws Exception {
        java.sql.Connection con = null;
        String IBDiscount= "";
//        String CustCode="";
        try {
        con = DbBean.getConnection();
//        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
        StringBuffer SqlQuery = new StringBuffer(" SELECT ISNULL(IBDISCOUNT,'0.00') IBDISCOUNT FROM ["+aPlant+"_MULTI_COST_MAPPING]  WHERE  ITEM='"+aItem+"' AND VENDNO ='" + suppliercode + "'");
        System.out.println(SqlQuery.toString());
        Map m = this.getRowOfData(con, SqlQuery.toString());
        
        if(m.size()>0)
        {
	            IBDiscount=(String) m.get("IBDISCOUNT");
	            if(IBDiscount.equals("") || IBDiscount.equalsIgnoreCase(null))
	            {
	                IBDiscount="0";
	            }
	            else
	            {
	            	IBDiscount = (String) m.get("IBDISCOUNT");
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
        return IBDiscount;
    }
        
    public String getDiscountSupplierCode(String plant, String pono) throws Exception {
	      java.sql.Connection con = null;
	      String suppliercode = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = " select isnull(custcode,'') vendno FROM ["+plant+"_POHDR]  WHERE pono='"+pono+"'";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       suppliercode = (String) m.get("vendno");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return suppliercode;
	  }
    public String getUnitCostBasedOnCurIDSelectedForOrderWTC(String aPlant, String aPONO,String aItem) throws Exception {
        java.sql.Connection con = null;
        String UnitCostForSelCurrency = "";
        try {
                
                con = DbBean.getConnection();
       

        
                StringBuffer SqlQuery = new StringBuffer(" select ISNULL(COST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO ='"+aPONO+"'))  AS UNITCOST ");
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
    
    public String getUnitCostBasedOnCurIDSelectedForOrderWTCcurrency(String aPlant, String currency,String aItem) throws Exception {
        java.sql.Connection con = null;
        String UnitCostForSelCurrency = "";
        try {
                
                con = DbBean.getConnection();
       

        
                StringBuffer SqlQuery = new StringBuffer(" select ISNULL(COST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')  AS UNITCOST ");
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
    
    public List getOrderDetailsForBilling(Hashtable ht) throws Exception {
    	java.sql.Connection con = null;
		List<Map<String, String>> ordersList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			
			query = "SELECT A.PONO,B.POLNNO,(SELECT ISNULL(DISPLAY,'') DISPLAY FROM [" + ht.get("PLANT") + "_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID) CURRENCYID,A.CURRENCYID CURRENCY_CODE,"
					+"ISNULL(A.INBOUND_GST,0) INBOUND_GST, ISNULL(A.ORDERDISCOUNT,0) ORDERDISCOUNT, ISNULL(SHIPPINGCOST,0) SHIPPINGCOST,ISNULL(B.CURRENCYUSEQT,1) CURRENCYUSEQT,"
					+"ISNULL(LOCALEXPENSES,0) LOCALEXPENSES,B.ITEM,C.ITEMDESC,ISNULL(B.UNITCOST,0)*ISNULL(B.CURRENCYUSEQT,0) UNITCOST,B.UNITMO,B.QTYOR,C.CATLOGPATH,ISNULL(B.UNITCOST,0) BASECOST,"
					+"CAST(ISNULL(UNITCOST,0)* ISNULL(B.CURRENCYUSEQT,0) AS DECIMAL(20,5)) AS CONVCOST,ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION, "
					+"ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE A.PURCHASE_LOCATION = C.STATE),'') as STATE_PREFIX, "
					+"ISNULL(A.EMPNO,0) EMPNO,ISNULL((SELECT FNAME FROM " +ht.get("PLANT")  +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME"
					+" FROM [" + ht.get("PLANT") + "_POHDR] A JOIN [" + ht.get("PLANT") + "_PODET] B ON A.PONO = B.PONO "
					+" JOIN [" + ht.get("PLANT") + "_ITEMMST] C ON B.ITEM = C.ITEM "
							+ "WHERE A.PLANT=? AND A.PONO=? ORDER BY POLNNO";
			
			PreparedStatement ps = con.prepareStatement(query);  
			/*Storing all the query param argument in list sequentially*/
			args.add((String)ht.get("PLANT"));
			args.add((String) ht.get("PONO"));
			this.mLogger.query(this.printQuery, query);			
			ordersList = selectData(ps, args);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}		
		return ordersList;
    }
    
    public List getBillingDetailsByGRNO(Hashtable ht) throws Exception {
    	java.sql.Connection con = null;
    	List<Map<String, String>> ordersList = new ArrayList<>();
//    	Map<String, String> map = null;
    	String query="";
    	List<String> args = null;
    	try {
    	/*Instantiate the list*/
    	args = new ArrayList<String>();

    	con = com.track.gates.DbBean.getConnection();
    	String plant =(String) ht.get("PLANT");
    	query = "SELECT A.PONO,B.RECVDATE,ISNULL(A.TRANSPORTID,'0') TRANSPORTID,B.LNNO,(SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID) CURRENCYID,A.CURRENCYID CURRENCY_CODE," +
    	"ISNULL(A.INBOUND_GST,0) INBOUND_GST, CASE WHEN ISNULL(A.ORDERDISCOUNTTYPE,'0') = '%' THEN A.ORDERDISCOUNT ELSE (A.ORDERDISCOUNT * ISNULL(A.CURRENCYUSEQT,1))END AS ORDERDISCOUNT, ISNULL(SHIPPINGCOST,0)*ISNULL(A.CURRENCYUSEQT,1) SHIPPINGCOST,ISNULL(B.UNITCOST,0) BASECOST," +
    	"ISNULL(LOCALEXPENSES,0) LOCALEXPENSES,B.ITEM,C.ITEMDESC,ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT," +
    	"ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE A.PURCHASE_LOCATION = C.STATE),'') as STATE_PREFIX,ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_PODET] WHERE PONO = A.PONO),1) CURRENCYUSEQT, "+
    	"ISNULL(B.UNITCOST,0)* ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_PODET] WHERE PONO = A.PONO AND ITEM = B.ITEM AND LNNO = B.LNNO),1) UNITCOST," +
    	"(ISNULL((SELECT ISNULL(UNITCOST_AOD,UNITCOST) AS UNITCOST_AOD FROM "+ "[" + ht.get("PLANT") + "_PODET] D WHERE B.ITEM = D.ITEM AND A.PONO = D.PONO AND B.LNNO = D.POLNNO),COST)) UNITCOST_AOD, " +
    	"ISNULL(A.EMPNO,0) EMPNO,ISNULL((SELECT FNAME FROM " +ht.get("PLANT")  +"_EMP_MST E where E.EMPNO=A.EMPNO),'') as EMP_NAME," +
    	"B.RECQTY,C.CATLOGPATH,CAST(ISNULL(UNITCOST,0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_PODET] WHERE PONO = A.PONO AND ITEM = B.ITEM AND LNNO = B.LNNO),1) AS DECIMAL(20,5)) AS CONVCOST" +
    	" FROM ["+plant+"_POHDR] A JOIN ["+plant+"_RECVDET] B ON A.PONO = B.PONO JOIN ["+plant+"_ITEMMST] C ON B.ITEM = C.ITEM " +
    	" WHERE GRNO = ? AND A.PONO = ? AND A.PLANT = ? ORDER BY LNNO";

    	PreparedStatement ps = con.prepareStatement(query);
    	/*Storing all the query param argument in list sequentially*/
    	args.add((String)ht.get("GRNO"));
    	args.add((String) ht.get("PONO"));
    	args.add((String) ht.get("PLANT"));
    	this.mLogger.query(this.printQuery, query);
    	ordersList = selectData(ps, args);
    	} catch (Exception e) {
    	this.mLogger.exception(this.printLog, "", e);
    	throw e;
    	} finally {
    	if (con != null) {
    	DbBean.closeConnection(con);
    	}
    	}
    	return ordersList;
    	}
    
    public String getActualShippingCostForBill(String plant, String pono) throws Exception {
	      java.sql.Connection con = null;
	      String shippinCost = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT (a.SHIPPINGCOST - ( SELECT ISNULL(SUM(SHIPPINGCOST),0) FROM  ["+plant+"_FINBILLHDR] b"
	        		+ " WHERE b.PONO = a.PONO) ) AS SHIPPINGCOST FROM ["+plant+"_POHDR] a " + 
	        		"WHERE a.PONO = '"+pono+"' GROUP BY PONO, SHIPPINGCOST";
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
    
    public String getActualDiscoutForBill(String plant, String pono) throws Exception {
	      java.sql.Connection con = null;
	      String orderdiscount = "";
	     String SqlQuery="";
	      try {
	        con = DbBean.getConnection();
	        SqlQuery = "SELECT (a.ORDERDISCOUNT - ( SELECT ISNULL(SUM(CAST(ISNULL(b.ORDER_DISCOUNT,'0') AS float)),0) FROM  ["+plant+"_FINBILLHDR] b"
	        		+ " WHERE b.PONO = a.PONO) ) AS ORDERDISCOUNT FROM ["+plant+"_POHDR] a " + 
	        		"WHERE a.PONO = '"+pono+"' GROUP BY PONO, ORDERDISCOUNT";
	        System.out.println(SqlQuery.toString());
	       Map m = this.getRowOfData(con, SqlQuery);
	       orderdiscount = (String) m.get("ORDERDISCOUNT");
	       
	      } catch (Exception e) {
	              this.mLogger.exception(this.printLog, "", e);
	              throw e;
	      } finally {
	              if (con != null) {
	                      DbBean.closeConnection(con);
	              }
	      }
	      return orderdiscount;
	  }
    public List getPreviousOrderDetails(Hashtable ht, String rows) throws Exception {
    	java.sql.Connection con = null;
		List<Map<String, String>> ordersList = new ArrayList<>();
//		Map<String, String> map = null;
		String query="", condition = "", unitCostCond = "";
		List<String> args = null;
		try {
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			
			/*Storing all the query param argument in list sequentially*/
			condition += " WHERE A.PLANT = ? ";
			args.add((String)ht.get("PLANT"));
			if(!((String)ht.get("ITEM")).equalsIgnoreCase("")) {
				condition += " AND B.ITEM = ? ";
				args.add((String)ht.get("ITEM"));
			}
			if(!((String)ht.get("VENDNO")).equalsIgnoreCase("")) {
				condition += " AND A.CustCode = ? ";
				args.add((String)ht.get("VENDNO"));
			}
			
			unitCostCond = "CASE WHEN UNITMO='" + ht.get("UOM") + "' THEN UNITCOST ELSE ISNULL((select ISNULL(QPUOM,1) from [" + ht.get("PLANT") + "_UOM] where UOM='" + ht.get("UOM") + "'),1) * (UNITCOST / (ISNULL((select ISNULL(QPUOM,1) from ["+ ht.get("PLANT") +"_UOM] where UOM=UNITMO),1))) END  AS UNITCOST";
			
			query = "SELECT TOP "+ rows +" a.PONO,a.CustCode,c.VNAME,ITEM,CollectionDate,"+unitCostCond+" FROM [" + ht.get("PLANT") + "_POHDR] a JOIN [" + ht.get("PLANT") + "_PODET] B ON a.PONO = b.PONO"
					+" JOIN [" + ht.get("PLANT") + "_VENDMST] c on a.CustCode = c.VENDNO "
					+ condition
					+" ORDER BY CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) DESC";

			PreparedStatement ps = con.prepareStatement(query);
			this.mLogger.query(this.printQuery, query);			
			ordersList = selectData(ps, args);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}		
		return ordersList;
    }
    
    public boolean addPoHdr(PoHdr poHdr) throws Exception {
 		boolean insertFlag = false;
// 		boolean flag = false;
// 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+poHdr.getPLANT()+"_POHDR]" + 
 					"           ([PLANT]" + 
 					"           ,[PONO]" +
 					"           ,[STATUS]" +
 					"           ,[ORDERTYPE]" +
 					"           ,[DELDATE]" +
 					"           ,[CRAT]" +
 					"           ,[CRBY]" +
 					"           ,[UPAT]" +
 					"           ,[UPBY]" +
 					"           ,[CustCode]" +
 					"           ,[CustName]" +
 					"           ,[JobNum]" +
 					"           ,[PersonInCharge]" +
 					"           ,[contactNum]" +
 					"           ,[Address]" +
 					"           ,[Address2]" +
 					"           ,[Address3]" +
 					"           ,[CollectionDate]" +
 					"           ,[CollectionTime]" +
 					"           ,[Remark1]" +
 					"           ,[Remark2]" +
 					"           ,[SHIPPINGID]" +
 					"           ,[SHIPPINGCUSTOMER]" +
 					"           ,[INBOUND_GST]" +
 					"           ,[CURRENCYID]" +
 					"           ,[STATUS_ID]" +
 					"           ,[REMARK3]" +
 					"           ,[ORDERDISCOUNT]" +
 					"           ,[SHIPPINGCOST]" +
 					"           ,[INCOTERMS]" +
 					"           ,[ADJUSTMENT]" +
 					"           ,[PAYMENTTYPE]" +
 					"           ,[DELIVERYDATEFORMAT]" +
 					"           ,[PURCHASE_LOCATION]" +
 					"           ,[TAXTREATMENT]" +
 					"           ,[REVERSECHARGE]" +
 					"           ,[ORDER_STATUS]" +
 					"           ,[LOCALEXPENSES]" +
 					"           ,[ISDISCOUNTTAX]" +
 					"           ,[ISSHIPPINGTAX]" +
 					"           ,[TAXID]" +
 					"           ,[ISTAXINCLUSIVE]" +
 					"           ,[ORDERDISCOUNTTYPE]" +
 					"           ,[CURRENCYUSEQT]" +
 					"           ,[PROJECTID]" +
 					"           ,[TRANSPORTID]" +
 					"           ,[PAYMENT_TERMS]" +
 					"           ,[EMPNO]" +
 					"           ,[SHIPCONTACTNAME]" +
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
 					"           ,[GOODSIMPORT],[POESTNO],[UKEY],[APPROVAL_STATUS],[REASON]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query);
	 			ps.setString(1, poHdr.getPLANT());
	 			ps.setString(2, poHdr.getPONO());
	 			ps.setString(3, poHdr.getSTATUS());
	 			ps.setString(4, poHdr.getORDERTYPE());
	 			ps.setString(5, poHdr.getDELDATE());
	 			ps.setString(6, poHdr.getCRAT());
	 			ps.setString(7, poHdr.getCRBY());
	 			ps.setString(8, poHdr.getUPAT());
	 			ps.setString(9, poHdr.getUPBY());
	 			ps.setString(10, poHdr.getCustCode());
	 			ps.setString(11, poHdr.getCustName());
	 			ps.setString(12, poHdr.getJobNum());
	 			ps.setString(13, poHdr.getPersonInCharge());
	 			ps.setString(14, poHdr.getContactNum());
	 			ps.setString(15, poHdr.getAddress());
	 			ps.setString(16, poHdr.getAddress2());
	 			ps.setString(17, poHdr.getAddress3());
	 			ps.setString(18, poHdr.getCollectionDate());
	 			ps.setString(19, poHdr.getCollectionTime());
	 			ps.setString(20, poHdr.getRemark1());
	 			ps.setString(21, poHdr.getRemark2());
	 			ps.setString(22, poHdr.getSHIPPINGID());
	 			ps.setString(23, poHdr.getSHIPPINGCUSTOMER());
	 			ps.setDouble(24, poHdr.getINBOUND_GST());
	 			ps.setString(25, poHdr.getCURRENCYID());
	 			ps.setString(26, poHdr.getSTATUS_ID());
	 			ps.setString(27, poHdr.getREMARK3());
	 			ps.setDouble(28, poHdr.getORDERDISCOUNT());
	 			ps.setDouble(29, poHdr.getSHIPPINGCOST());
	 			ps.setString(30, poHdr.getINCOTERMS());
	 			ps.setDouble(31, poHdr.getADJUSTMENT());
	 			ps.setString(32, poHdr.getPAYMENTTYPE());
	 			ps.setDouble(33, poHdr.getDELIVERYDATEFORMAT());
	 			ps.setString(34, poHdr.getPURCHASE_LOCATION());
	 			ps.setString(35, poHdr.getTAXTREATMENT());
	 			ps.setDouble(36, poHdr.getREVERSECHARGE());
	 			ps.setString(37, poHdr.getORDER_STATUS());
	 			ps.setDouble(38, poHdr.getLOCALEXPENSES());
	 			ps.setDouble(39, poHdr.getISDISCOUNTTAX());
	 			ps.setDouble(40, poHdr.getISSHIPPINGTAX());
	 			ps.setInt(41, poHdr.getTAXID());
	 			ps.setDouble(42, poHdr.getISTAXINCLUSIVE());
	 			ps.setString(43, poHdr.getORDERDISCOUNTTYPE());
	 			ps.setDouble(44, poHdr.getCURRENCYUSEQT());
	 			ps.setDouble(45, poHdr.getPROJECTID());
	 			ps.setDouble(46, poHdr.getTRANSPORTID());
	 			ps.setString(47, poHdr.getPAYMENT_TERMS());
	 			ps.setString(48, poHdr.getEMPNO());
	 			ps.setString(49, poHdr.getSHIPCONTACTNAME());
				ps.setString(50, poHdr.getSHIPDESGINATION());
				ps.setString(51, poHdr.getSHIPWORKPHONE());
				ps.setString(52, poHdr.getSHIPHPNO());
				ps.setString(53, poHdr.getSHIPEMAIL());
				ps.setString(54, poHdr.getSHIPCOUNTRY());
				ps.setString(55, poHdr.getSHIPADDR1());
				ps.setString(56, poHdr.getSHIPADDR2());
				ps.setString(57, poHdr.getSHIPADDR3());
				ps.setString(58, poHdr.getSHIPADDR4());
				ps.setString(59, poHdr.getSHIPSTATE());
				ps.setString(60, poHdr.getSHIPZIP());
	 			ps.setDouble(61, poHdr.getGOODSIMPORT());
	 			ps.setString(62, poHdr.getPOESTNO());
	 			ps.setString(63, poHdr.getUKEY());
	 			ps.setString(64, poHdr.getAPPROVAL_STATUS());
	 			ps.setString(65, poHdr.getREASON());

 			   int count=ps.executeUpdate();
				/*
				 * ResultSet rs = ps.getGeneratedKeys(); if (rs.next()){ invoiceHdrId =
				 * rs.getInt(1); }
				 */
 			   if(count>0)
 			   {
 				   insertFlag = true;
 			   }
 			   else
 			   {
 				   throw new SQLException("Creating Purchase Order failed, no rows affected.");
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
    
    public int addPoHdrReturnKey(PoHdr poHdr) throws Exception {
 		int pohdrkey = 0;
// 		boolean flag = false;
// 		int HdrId = 0;
 		Connection connection = null;
 		PreparedStatement ps = null;
 	    String query = "";
 		try {	    
 			connection = DbBean.getConnection();
 			query = "INSERT INTO ["+poHdr.getPLANT()+"_POHDR]" + 
 					"           ([PLANT]" + 
 					"           ,[PONO]" +
 					"           ,[STATUS]" +
 					"           ,[ORDERTYPE]" +
 					"           ,[DELDATE]" +
 					"           ,[CRAT]" +
 					"           ,[CRBY]" +
 					"           ,[UPAT]" +
 					"           ,[UPBY]" +
 					"           ,[CustCode]" +
 					"           ,[CustName]" +
 					"           ,[JobNum]" +
 					"           ,[PersonInCharge]" +
 					"           ,[contactNum]" +
 					"           ,[Address]" +
 					"           ,[Address2]" +
 					"           ,[Address3]" +
 					"           ,[CollectionDate]" +
 					"           ,[CollectionTime]" +
 					"           ,[Remark1]" +
 					"           ,[Remark2]" +
 					"           ,[SHIPPINGID]" +
 					"           ,[SHIPPINGCUSTOMER]" +
 					"           ,[INBOUND_GST]" +
 					"           ,[CURRENCYID]" +
 					"           ,[STATUS_ID]" +
 					"           ,[REMARK3]" +
 					"           ,[ORDERDISCOUNT]" +
 					"           ,[SHIPPINGCOST]" +
 					"           ,[INCOTERMS]" +
 					"           ,[ADJUSTMENT]" +
 					"           ,[PAYMENTTYPE]" +
 					"           ,[DELIVERYDATEFORMAT]" +
 					"           ,[PURCHASE_LOCATION]" +
 					"           ,[TAXTREATMENT]" +
 					"           ,[REVERSECHARGE]" +
 					"           ,[ORDER_STATUS]" +
 					"           ,[LOCALEXPENSES]" +
 					"           ,[ISDISCOUNTTAX]" +
 					"           ,[ISSHIPPINGTAX]" +
 					"           ,[TAXID]" +
 					"           ,[ISTAXINCLUSIVE]" +
 					"           ,[ORDERDISCOUNTTYPE]" +
 					"           ,[CURRENCYUSEQT]" +
 					"           ,[PROJECTID]" +
 					"           ,[TRANSPORTID]" +
 					"           ,[PAYMENT_TERMS]" +
 					"           ,[EMPNO]" +
 					"           ,[SHIPCONTACTNAME]" +
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
 					"           ,[GOODSIMPORT],[POESTNO],[UKEY],[APPROVAL_STATUS],[REASON],[PURCHASEPEPPOLID],[PEPPOLINVOICEUUID],[POSENDSTATUS],[RESPONCECODE],[RESPONCEMESSAGE]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

 			if(connection != null){
	 			ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	 			ps.setString(1, poHdr.getPLANT());
	 			ps.setString(2, poHdr.getPONO());
	 			ps.setString(3, poHdr.getSTATUS());
	 			ps.setString(4, poHdr.getORDERTYPE());
	 			ps.setString(5, poHdr.getDELDATE());
	 			ps.setString(6, poHdr.getCRAT());
	 			ps.setString(7, poHdr.getCRBY());
	 			ps.setString(8, poHdr.getUPAT());
	 			ps.setString(9, poHdr.getUPBY());
	 			ps.setString(10, poHdr.getCustCode());
	 			ps.setString(11, poHdr.getCustName());
	 			ps.setString(12, poHdr.getJobNum());
	 			ps.setString(13, poHdr.getPersonInCharge());
	 			ps.setString(14, poHdr.getContactNum());
	 			ps.setString(15, poHdr.getAddress());
	 			ps.setString(16, poHdr.getAddress2());
	 			ps.setString(17, poHdr.getAddress3());
	 			ps.setString(18, poHdr.getCollectionDate());
	 			ps.setString(19, poHdr.getCollectionTime());
	 			ps.setString(20, poHdr.getRemark1());
	 			ps.setString(21, poHdr.getRemark2());
	 			ps.setString(22, poHdr.getSHIPPINGID());
	 			ps.setString(23, poHdr.getSHIPPINGCUSTOMER());
	 			ps.setDouble(24, poHdr.getINBOUND_GST());
	 			ps.setString(25, poHdr.getCURRENCYID());
	 			ps.setString(26, poHdr.getSTATUS_ID());
	 			ps.setString(27, poHdr.getREMARK3());
	 			ps.setDouble(28, poHdr.getORDERDISCOUNT());
	 			ps.setDouble(29, poHdr.getSHIPPINGCOST());
	 			ps.setString(30, poHdr.getINCOTERMS());
	 			ps.setDouble(31, poHdr.getADJUSTMENT());
	 			ps.setString(32, poHdr.getPAYMENTTYPE());
	 			ps.setDouble(33, poHdr.getDELIVERYDATEFORMAT());
	 			ps.setString(34, poHdr.getPURCHASE_LOCATION());
	 			ps.setString(35, poHdr.getTAXTREATMENT());
	 			ps.setDouble(36, poHdr.getREVERSECHARGE());
	 			ps.setString(37, poHdr.getORDER_STATUS());
	 			ps.setDouble(38, poHdr.getLOCALEXPENSES());
	 			ps.setDouble(39, poHdr.getISDISCOUNTTAX());
	 			ps.setDouble(40, poHdr.getISSHIPPINGTAX());
	 			ps.setInt(41, poHdr.getTAXID());
	 			ps.setDouble(42, poHdr.getISTAXINCLUSIVE());
	 			ps.setString(43, poHdr.getORDERDISCOUNTTYPE());
	 			ps.setDouble(44, poHdr.getCURRENCYUSEQT());
	 			ps.setDouble(45, poHdr.getPROJECTID());
	 			ps.setDouble(46, poHdr.getTRANSPORTID());
	 			ps.setString(47, poHdr.getPAYMENT_TERMS());
	 			ps.setString(48, poHdr.getEMPNO());
	 			ps.setString(49, poHdr.getSHIPCONTACTNAME());
				ps.setString(50, poHdr.getSHIPDESGINATION());
				ps.setString(51, poHdr.getSHIPWORKPHONE());
				ps.setString(52, poHdr.getSHIPHPNO());
				ps.setString(53, poHdr.getSHIPEMAIL());
				ps.setString(54, poHdr.getSHIPCOUNTRY());
				ps.setString(55, poHdr.getSHIPADDR1());
				ps.setString(56, poHdr.getSHIPADDR2());
				ps.setString(57, poHdr.getSHIPADDR3());
				ps.setString(58, poHdr.getSHIPADDR4());
				ps.setString(59, poHdr.getSHIPSTATE());
				ps.setString(60, poHdr.getSHIPZIP());
	 			ps.setDouble(61, poHdr.getGOODSIMPORT());
	 			ps.setString(62, poHdr.getPOESTNO());
	 			ps.setString(63, poHdr.getUKEY());
	 			ps.setString(64, poHdr.getAPPROVAL_STATUS());
	 			ps.setString(65, poHdr.getREASON());
	 			ps.setInt(66, poHdr.getPURCHASEPEPPOLID());
	 			ps.setString(67, poHdr.getPEPPOLINVOICEUUID());
	 			ps.setString(68, poHdr.getPOSENDSTATUS());
	 			ps.setString(69, poHdr.getRESPONCECODE());
	 			ps.setString(70, poHdr.getRESPONCEMESSAGE());

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
    
    
    public boolean updatePoHdr(PoHdr poHdr) throws Exception {
		boolean updateFlag = false;
//		boolean flag = false;
//		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "UPDATE ["+poHdr.getPLANT()+"_POHDR] SET" + 
					"           [PLANT] = ?" + 
					"           ,[PONO] = ?" +
					"           ,[STATUS] = ?" +
					"           ,[ORDERTYPE] = ?" +
					"           ,[DELDATE] = ?" +
					"           ,[UPAT] = ?" +
					"           ,[UPBY] = ?" +
					"           ,[CustCode] = ?" +
					"           ,[CustName] = ?" +
					"           ,[JobNum] = ?" +
					"           ,[PersonInCharge] = ?" +
					"           ,[contactNum] = ?" +
					"           ,[Address] = ?" +
					"           ,[Address2] = ?" +
					"           ,[Address3] = ?" +
					"           ,[CollectionDate] = ?" +
					"           ,[CollectionTime] = ?" +
					"           ,[Remark1] = ?" +
					"           ,[Remark2] = ?" +
					"           ,[SHIPPINGID] = ?" +
					"           ,[SHIPPINGCUSTOMER] = ?" +
					"           ,[INBOUND_GST] = ?" +
					"           ,[CURRENCYID] = ?" +
					"           ,[STATUS_ID] = ?" +
					"           ,[REMARK3] = ?" +
					"           ,[ORDERDISCOUNT] = ?" +
					"           ,[SHIPPINGCOST] = ?" +
					"           ,[INCOTERMS] = ?" +
					"           ,[ADJUSTMENT] = ?" +
					"           ,[PAYMENTTYPE] = ?" +
					"           ,[DELIVERYDATEFORMAT] = ?" +
					"           ,[PURCHASE_LOCATION] = ?" +
					"           ,[TAXTREATMENT] = ?" +
					"           ,[REVERSECHARGE] = ?" +
					"           ,[ORDER_STATUS] = ?" +
					"           ,[LOCALEXPENSES] = ?" +
					"           ,[ISDISCOUNTTAX] = ?" +
					"           ,[ISSHIPPINGTAX] = ?" +
					"           ,[TAXID] = ?" +
					"           ,[ISTAXINCLUSIVE] = ?" +
					"           ,[ORDERDISCOUNTTYPE] = ?" +
					"           ,[CURRENCYUSEQT] = ?" +
					"           ,[PROJECTID] = ?" +
					"           ,[TRANSPORTID] = ?" +
					"           ,[PAYMENT_TERMS] = ?" +
					"           ,[EMPNO] = ?" +
					"           ,[SHIPCONTACTNAME] = ?" +
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
					"           ,[GOODSIMPORT] = ?" +
					"           ,[UKEY] = ?" +
					"           ,[APPROVAL_STATUS] = ?" +
					"           ,[REASON] = ? WHERE [PONO] = ?";

			if(connection != null){
			   ps = connection.prepareStatement(query);
			   ps.setString(1, poHdr.getPLANT());
	 			ps.setString(2, poHdr.getPONO());
	 			ps.setString(3, poHdr.getSTATUS());
	 			ps.setString(4, poHdr.getORDERTYPE());
	 			ps.setString(5, poHdr.getDELDATE());
	 			ps.setString(6, poHdr.getUPAT());
	 			ps.setString(7, poHdr.getUPBY());
	 			ps.setString(8, poHdr.getCustCode());
	 			ps.setString(9, poHdr.getCustName());
	 			ps.setString(10, poHdr.getJobNum());
	 			ps.setString(11, poHdr.getPersonInCharge());
	 			ps.setString(12, poHdr.getContactNum());
	 			ps.setString(13, poHdr.getAddress());
	 			ps.setString(14, poHdr.getAddress2());
	 			ps.setString(15, poHdr.getAddress3());
	 			ps.setString(16, poHdr.getCollectionDate());
	 			ps.setString(17, poHdr.getCollectionTime());
	 			ps.setString(18, poHdr.getRemark1());
	 			ps.setString(19, poHdr.getRemark2());
	 			ps.setString(20, poHdr.getSHIPPINGID());
	 			ps.setString(21, poHdr.getSHIPPINGCUSTOMER());
	 			ps.setDouble(22, poHdr.getINBOUND_GST());
	 			ps.setString(23, poHdr.getCURRENCYID());
	 			ps.setString(24, poHdr.getSTATUS_ID());
	 			ps.setString(25, poHdr.getREMARK3());
	 			ps.setDouble(26, poHdr.getORDERDISCOUNT());
	 			ps.setDouble(27, poHdr.getSHIPPINGCOST());
	 			ps.setString(28, poHdr.getINCOTERMS());
	 			ps.setDouble(29, poHdr.getADJUSTMENT());
	 			ps.setString(30, poHdr.getPAYMENTTYPE());
	 			ps.setDouble(31, poHdr.getDELIVERYDATEFORMAT());
	 			ps.setString(32, poHdr.getPURCHASE_LOCATION());
	 			ps.setString(33, poHdr.getTAXTREATMENT());
	 			ps.setDouble(34, poHdr.getREVERSECHARGE());
	 			ps.setString(35, poHdr.getORDER_STATUS());
	 			ps.setDouble(36, poHdr.getLOCALEXPENSES());
	 			ps.setDouble(37, poHdr.getISDISCOUNTTAX());
	 			ps.setDouble(38, poHdr.getISSHIPPINGTAX());
	 			ps.setInt(39, poHdr.getTAXID());
	 			ps.setDouble(40, poHdr.getISTAXINCLUSIVE());
	 			ps.setString(41, poHdr.getORDERDISCOUNTTYPE());
	 			ps.setDouble(42, poHdr.getCURRENCYUSEQT());
	 			ps.setInt(43, poHdr.getPROJECTID());
	 			ps.setInt(44, poHdr.getTRANSPORTID());
	 			ps.setString(45, poHdr.getPAYMENT_TERMS());
	 			ps.setString(46, poHdr.getEMPNO());
	 			ps.setString(47, poHdr.getSHIPCONTACTNAME());
				ps.setString(48, poHdr.getSHIPDESGINATION());
				ps.setString(49, poHdr.getSHIPWORKPHONE());
				ps.setString(50, poHdr.getSHIPHPNO());
				ps.setString(51, poHdr.getSHIPEMAIL());
				ps.setString(52, poHdr.getSHIPCOUNTRY());
				ps.setString(53, poHdr.getSHIPADDR1());
				ps.setString(54, poHdr.getSHIPADDR2());
				ps.setString(55, poHdr.getSHIPADDR3());
				ps.setString(56, poHdr.getSHIPADDR4());
				ps.setString(57, poHdr.getSHIPSTATE());
				ps.setString(58, poHdr.getSHIPZIP());
	 			ps.setDouble(59, poHdr.getGOODSIMPORT());
	 			ps.setString(60, poHdr.getUKEY());
	 			ps.setString(61, poHdr.getAPPROVAL_STATUS());
	 			ps.setString(62, poHdr.getREASON());
	 			ps.setString(63, poHdr.getPONO());
	 			
			   int count=ps.executeUpdate();
			   if(count>0)
			   {
				   updateFlag = true;
			   }
			   else
			   {
				   throw new SQLException("Updateing Purchase Order failed, no rows affected.");
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
    
	public PoHdr getPoHdrByPono(String plant, String pono)throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    PoHdr poHdr=new PoHdr();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,PONO,STATUS,ISNULL(UKEY,'') UKEY,ISNULL(APPROVAL_STATUS,'') APPROVAL_STATUS,ISNULL(REASON,'') REASON,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO, ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,JobNum,PersonInCharge,contactNum,Address,Address2,Address3,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,CURRENCYID,STATUS_ID,REMARK3,CASE WHEN ISNULL(ORDERDISCOUNTTYPE,'0') = '%' THEN ORDERDISCOUNT ELSE (ORDERDISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,0)*ISNULL(CURRENCYUSEQT,1) SHIPPINGCOST,0,INCOTERMS,ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,TAXTREATMENT,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,LOCALEXPENSES,ISNULL(ISDISCOUNTTAX,'0') AS ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,'0') AS ISSHIPPINGTAX,ISNULL(TAXID,'0') AS TAXID,ISNULL(ISTAXINCLUSIVE,'0') AS ISTAXINCLUSIVE,ISNULL(ORDERDISCOUNTTYPE,'') AS ORDERDISCOUNTTYPE,ISNULL(CURRENCYUSEQT,1) AS CURRENCYUSEQT,ISNULL(POESTNO,'') POESTNO,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,ISNULL(PURCHASEPEPPOLID,0) PURCHASEPEPPOLID,ISNULL(PEPPOLINVOICEUUID,'') PEPPOLINVOICEUUID,ISNULL(POSENDSTATUS,'') POSENDSTATUS,ISNULL(RESPONCECODE,'') RESPONCECODE,ISNULL(RESPONCEMESSAGE,'') RESPONCEMESSAGE FROM ["+ plant +"_POHDR] WHERE PONO='"+pono+"'";
			System.out.println(query);
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, poHdr);
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
		return poHdr;
	}
	
	public List<PoHdr> getPoHdrSummary(Hashtable ht, String afrmDate, String atoDate) throws Exception {
//		   boolean flag = false;
//		   int journalHdrId = 0;
		   Connection connection = null;
		   PreparedStatement ps = null;
		   String query = "", fields = "", dtCondStr = "";
		   List<PoHdr> poHeaders = new ArrayList<PoHdr>();
		   List<String> args = null;
		   try {
			   args = new ArrayList<String>();
			   connection = DbBean.getConnection();
			   query = "SELECT PLANT,ID,PONO,STATUS,ISNULL(UKEY,'') UKEY,ISNULL(APPROVAL_STATUS,'') APPROVAL_STATUS,ISNULL(REASON,'') REASON,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO,ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,JobNum,PersonInCharge,contactNum,Address,Address2,Address3,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,CURRENCYID,STATUS_ID,REMARK3,CASE WHEN ISNULL(ORDERDISCOUNTTYPE,'0') = '%' THEN ORDERDISCOUNT ELSE (ORDERDISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,0)*ISNULL(CURRENCYUSEQT,1) SHIPPINGCOST,0,INCOTERMS,ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,TAXTREATMENT,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,LOCALEXPENSES,ISNULL(ISDISCOUNTTAX,'0') AS ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,'0') AS ISSHIPPINGTAX,ISNULL(TAXID,'0') AS TAXID,ISNULL(ISTAXINCLUSIVE,'0') AS ISTAXINCLUSIVE,ISNULL(ORDERDISCOUNTTYPE,'') AS ORDERDISCOUNTTYPE,ISNULL(CURRENCYUSEQT,1) AS CURRENCYUSEQT,ISNULL(POESTNO,'') POESTNO,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,ISNULL(PURCHASEPEPPOLID,0) PURCHASEPEPPOLID,ISNULL(PEPPOLINVOICEUUID,'') PEPPOLINVOICEUUID,ISNULL(POSENDSTATUS,'') POSENDSTATUS,ISNULL(RESPONCECODE,'') RESPONCECODE,ISNULL(RESPONCEMESSAGE,'') RESPONCEMESSAGE FROM ["+ ht.get("PLANT") +"_POHDR] WHERE ";
			   Enumeration enum1 = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enum1.nextElement());
					String value = StrUtils.fString((String) ht.get(key));
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
				
				query += " ORDER BY CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) DESC, PONO DESC";
				this.mLogger.query(this.printQuery, query);
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
						PoHdr pohdr = new PoHdr();
						ResultSetToObjectMap.loadResultSetIntoObject(rst, pohdr);
						poHeaders.add(pohdr);
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
		   return poHeaders;
		}

	public List<PoHdr> getPoHdrSummaryByApprove(Hashtable ht, String afrmDate, String atoDate, String astatus) throws Exception {
//		   boolean flag = false;
//		   int journalHdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		String query = "", fields = "", dtCondStr = "";
		List<PoHdr> poHeaders = new ArrayList<PoHdr>();
		List<String> args = null;
		try {
			args = new ArrayList<String>();
			connection = DbBean.getConnection();
			query = "SELECT PLANT,ID,PONO,STATUS,ISNULL(UKEY,'') UKEY,ISNULL(APPROVAL_STATUS,'') APPROVAL_STATUS,ISNULL(REASON,'') REASON,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO,ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,JobNum,PersonInCharge,contactNum,Address,Address2,Address3,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,CURRENCYID,STATUS_ID,REMARK3,CASE WHEN ISNULL(ORDERDISCOUNTTYPE,'0') = '%' THEN ORDERDISCOUNT ELSE (ORDERDISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,0)*ISNULL(CURRENCYUSEQT,1) SHIPPINGCOST,0,INCOTERMS,ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,TAXTREATMENT,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,LOCALEXPENSES,ISNULL(ISDISCOUNTTAX,'0') AS ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,'0') AS ISSHIPPINGTAX,ISNULL(TAXID,'0') AS TAXID,ISNULL(ISTAXINCLUSIVE,'0') AS ISTAXINCLUSIVE,ISNULL(ORDERDISCOUNTTYPE,'') AS ORDERDISCOUNTTYPE,ISNULL(CURRENCYUSEQT,1) AS CURRENCYUSEQT,ISNULL(POESTNO,'') POESTNO,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,ISNULL(PURCHASEPEPPOLID,0) PURCHASEPEPPOLID,ISNULL(PEPPOLINVOICEUUID,'') PEPPOLINVOICEUUID,ISNULL(POSENDSTATUS,'') POSENDSTATUS,ISNULL(RESPONCECODE,'') RESPONCECODE,ISNULL(RESPONCEMESSAGE,'') RESPONCEMESSAGE,ISNULL(PURCHASEPEPPOLID,0) PURCHASEPEPPOLID,ISNULL(PEPPOLINVOICEUUID,'') PEPPOLINVOICEUUID,ISNULL(POSENDSTATUS,'') POSENDSTATUS,ISNULL(RESPONCECODE,'') RESPONCECODE,ISNULL(RESPONCEMESSAGE,'') RESPONCEMESSAGE FROM ["+ ht.get("PLANT") +"_POHDR] WHERE ";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
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
			query += astatus;
			query += " ORDER BY CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) DESC, PONO DESC";
			this.mLogger.query(this.printQuery, query);
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
					PoHdr pohdr = new PoHdr();
					ResultSetToObjectMap.loadResultSetIntoObject(rst, pohdr);
					poHeaders.add(pohdr);
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
		return poHeaders;
	}
	
	public int Ordercount(String plant, String afrmDate, String atoDate)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int Ordercount = 0;
		String sCondition = "";
		String dtCondStr =    " AND (SUBSTRING(CollectionDate, 7, 4) + '-' + SUBSTRING(CollectionDate, 4, 2) + '-' + SUBSTRING(CollectionDate, 1, 2))";
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
					+ "POHDR" + "]" + " WHERE " + IConstants.PLANT
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
    
	public ArrayList getOrderNoForOrderReceipt(Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String query = "", plant = "";//conditon = "", 
		try {
			con = com.track.gates.DbBean.getConnection();
			plant = (String)ht.get("PLANT");
			query = "SELECT * FROM (" + 
					" SELECT PONO AS ORDERNO, custcode AS VENDNO, CollectionDate AS ORD_DATE "+
					" FROM ["+plant+"_POHDR] WHERE PLANT = '"+plant+"'";
					if(((String)ht.get("ORDNO")).length()>0) {
						query += " AND PONO LIKE '%"+(String)ht.get("ORDNO")+"%'";
					}
					if(((String)ht.get("VENDNO")).length()>0) {
						query += " AND CUSTCODE = '"+(String)ht.get("VENDNO")+"'";
					}
			query += " UNION ALL " + 
					" SELECT BILL AS ORDERNO, VENDNO, BILL_DATE AS ORD_DATE "
					+ " FROM ["+plant+"_FINBILLHDR] WHERE PLANT='"+plant+"' AND PONO = '' AND GRNO <>'' ";
					if(((String)ht.get("ORDNO")).length()>0) {
						query += " AND BILL LIKE '%"+(String)ht.get("ORDNO")+"%'";
					}
					if(((String)ht.get("VENDNO")).length()>0) {
						query += " AND VENDNO = '"+(String)ht.get("VENDNO")+"'";
					}
			query += " ) A ORDER  BY CONVERT(date, A.ORD_DATE, 103) DESC";

			alData = selectData(con, query.toString());

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
	
	public Map getPurchaseEstimateHdrDetails(String plant) throws Exception {
        MLogger.log(1, this.getClass() + " getPurchaseEstimateHdrDetails()");
        Map m = new HashMap();
        java.sql.Connection con = null;
//        String scondtn ="";
        try {
                con = DbBean.getConnection();
                StringBuffer sql = new StringBuffer("  SELECT  ");
                sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(TERMS,'') AS TERMS ,ISNULL(TERMSDETAILS,'') AS TERMSDETAILS,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRINTWITHPROJECT,'0') AS PRINTWITHPROJECT,ISNULL(TRANSPORT_MODE,'') AS TRANSPORT_MODE,ISNULL(PRINTWITHTRANSPORT_MODE,'0') AS PRINTWITHTRANSPORT_MODE,ISNULL(PRINTXTRADETAILS,'0') AS PRINTXTRADETAILS,ISNULL(PRINTSUPTERMS,'0') AS PRINTSUPTERMS,ISNULL(PSUPREMARKS,'0') as PSUPREMARKS, ");
                sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(RATE,'') AS RATE ,ISNULL(TAXAMOUNT,'') AS TAXAMOUNT,ISNULL(AMT,'') AS AMT, ");
                sql.append("ISNULL(SUBTOTAL,'') AS SUBTOTAL,ISNULL(TOTALTAX,'') AS TOTALTAX,ISNULL(TOTAL,'Total With Tax') AS TOTAL,ISNULL(UOM,'') AS UOM,ISNULL(EMPLOYEE,'') AS EMPLOYEE,ISNULL(PRINTEMPLOYEE,'0') AS PRINTEMPLOYEE, ISNULL(PROJECT,'') AS PROJECT,ISNULL(ADJUSTMENT,'') AS ADJUSTMENT , ");
                sql.append("ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2 ,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,ISNULL(FOOTER5,'') AS F5,ISNULL(PRODUCTRATESARE,'') AS PRODUCTRATESARE, ");
                sql.append("ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9,ISNULL(BILLNO,'') AS BILLNO,ISNULL(BILLDATE,'') AS BILLDATE, ");
  				 sql.append("ISNULL(REMARK1,'') REMARK1,ISNULL(REMARK2,'') REMARK2,ISNULL(DELIVERYDATE,'') DELIVERYDATE,ISNULL(EXPIREDATE,'') EXPIREDATE,ISNULL(SHIPTO,'')SHIPTO, "); 
                sql.append("ISNULL(COMPANYDATE,'')COMPANYDATE,ISNULL(COMPANYNAME,'') COMPANYNAME,ISNULL(COMPANYSTAMP,'') COMPANYSTAMP,ISNULL(COMPANYSIG,'') COMPANYSIG,ISNULL(PRINTWITHDISCOUNT,'0') PRINTWITHDISCOUNT,ISNULL(DISCOUNT,'') DISCOUNT,ISNULL(NETRATE,'') NETRATE,ISNULL(PrintOrientation,'Landscape') PrintOrientation, ");
                sql.append(" ISNULL(ORDERDISCOUNT,'') AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,ISNULL(INCOTERM,'') AS INCOTERM, ISNULL(SHIPTO, '') AS SHIPTO,ISNULL(RCBNO, '') AS RCBNO,ISNULL(UENNO,'') AS UENNO,ISNULL(SUPPLIERUENNO,'') AS SUPPLIERUENNO,ISNULL(PRINTWITHPRODUCTREMARKS,'0') PRINTWITHPRODUCTREMARKS,ISNULL(PRINTWITHBRAND,'0') PRINTWITHBRAND,ISNULL(SUPPLIERRCBNO,'') SUPPLIERRCBNO,ISNULL(GRNO,'') GRNO,ISNULL(TOTALAFTERDISCOUNT,'') TOTALAFTERDISCOUNT,ISNULL(GRNDATE,'') GRNDATE,ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(AUTHSIGNATURE,'') AS AUTHSIGNATURE,ISNULL(ROUNDOFFTOTALWITHDECIMAL,'') AS ROUNDOFFTOTALWITHDECIMAL,ISNULL(PRINTROUNDOFFTOTALWITHDECIMAL,'0') AS PRINTROUNDOFFTOTALWITHDECIMAL, ");
                sql.append(" ISNULL(PRODUCTDELIVERYDATE,'') AS PRODUCTDELIVERYDATE,ISNULL(PRINTWITHEXPIREDATE,'') AS PRINTWITHEXPIREDATE,ISNULL(PRINTWITHPRODUCTDELIVERYDATE,'0') AS PRINTWITHPRODUCTDELIVERYDATE,ISNULL(PRINTWITHCOMPANYSIG,'0') AS PRINTWITHCOMPANYSIG,ISNULL(PRINTWITHCOMPANYSEAL,'0') AS PRINTWITHCOMPANYSEAL,ISNULL(PRINTWITHDELIVERYDATE,'0') AS PRINTWITHDELIVERYDATE,ISNULL(PRINTWITHSUPPLIERUENNO,'0') AS PRINTWITHSUPPLIERUENNO,ISNULL(PRINTWITHUENNO,'0') AS PRINTWITHUENNO,ISNULL(SHOWPREVIOUSPURCHASECOST,'0') AS SHOWPREVIOUSPURCHASECOST,ISNULL(CALCULATETAXWITHSHIPPINGCOST,'0') AS CALCULATETAXWITHSHIPPINGCOST ");
                sql.append(" FROM " + "[" + plant + "_"+ "PURCHASE_ESTIMATE_HDR] where plant='"+plant+"'");
         
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
	 
		public boolean updatePurchaseEstimateHeader(String query, Hashtable htCondition, String extCond)
		     throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
		     con = com.track.gates.DbBean.getConnection();
		     StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                     + htCondition.get("PLANT") + "_PURCHASE_ESTIMATE_HDR]");
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
		
		public List<PoHdr> getPoHdrSummaryByApproveforpeppol(Hashtable ht, String afrmDate, String atoDate, String astatus) throws Exception {
//			   boolean flag = false;
//			   int journalHdrId = 0;
			Connection connection = null;
			PreparedStatement ps = null;
			String query = "", fields = "", dtCondStr = "";
			List<PoHdr> poHeaders = new ArrayList<PoHdr>();
			List<String> args = null;
			try {
				args = new ArrayList<String>();
				connection = DbBean.getConnection();
				query = "SELECT PLANT,ID,PONO,STATUS,ISNULL(UKEY,'') UKEY,ISNULL(APPROVAL_STATUS,'') APPROVAL_STATUS,ISNULL(REASON,'') REASON,ISNULL(PROJECTID,0) PROJECTID,ISNULL(TRANSPORTID,0) TRANSPORTID,ISNULL(EMPNO,0) EMPNO,ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS,ORDERTYPE,DELDATE,CRAT,CRBY,UPAT,UPBY,CustCode,CustName,JobNum,PersonInCharge,contactNum,Address,Address2,Address3,CollectionDate,CollectionTime,Remark1,Remark2,SHIPPINGID,SHIPPINGCUSTOMER,INBOUND_GST,CURRENCYID,STATUS_ID,REMARK3,CASE WHEN ISNULL(ORDERDISCOUNTTYPE,'0') = '%' THEN ORDERDISCOUNT ELSE (ORDERDISCOUNT * ISNULL(CURRENCYUSEQT,1))END AS ORDERDISCOUNT,ISNULL(SHIPPINGCOST,0)*ISNULL(CURRENCYUSEQT,1) SHIPPINGCOST,0,INCOTERMS,ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,PAYMENTTYPE,DELIVERYDATEFORMAT,ISNULL(PURCHASE_LOCATION,'') AS PURCHASE_LOCATION,TAXTREATMENT,ORDER_STATUS,REVERSECHARGE,GOODSIMPORT,LOCALEXPENSES,ISNULL(ISDISCOUNTTAX,'0') AS ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,'0') AS ISSHIPPINGTAX,ISNULL(TAXID,'0') AS TAXID,ISNULL(ISTAXINCLUSIVE,'0') AS ISTAXINCLUSIVE,ISNULL(ORDERDISCOUNTTYPE,'') AS ORDERDISCOUNTTYPE,ISNULL(CURRENCYUSEQT,1) AS CURRENCYUSEQT,ISNULL(POESTNO,'') POESTNO,ISNULL(SHIPCONTACTNAME,'') SHIPCONTACTNAME,ISNULL(SHIPDESGINATION,'') SHIPDESGINATION,ISNULL(SHIPWORKPHONE,'') SHIPWORKPHONE,ISNULL(SHIPHPNO,'') SHIPHPNO,ISNULL(SHIPEMAIL,'') SHIPEMAIL,ISNULL(SHIPCOUNTRY,'') SHIPCOUNTRY,ISNULL(SHIPADDR1,'') SHIPADDR1,ISNULL(SHIPADDR2,'') SHIPADDR2,ISNULL(SHIPADDR3,'') SHIPADDR3,ISNULL(SHIPADDR4,'') SHIPADDR4,ISNULL(SHIPSTATE,'') SHIPSTATE,ISNULL(SHIPZIP,'') SHIPZIP,ISNULL(PURCHASEPEPPOLID,0) PURCHASEPEPPOLID,ISNULL(PEPPOLINVOICEUUID,'') PEPPOLINVOICEUUID,ISNULL(POSENDSTATUS,'') POSENDSTATUS,ISNULL(RESPONCECODE,'') RESPONCECODE,ISNULL(RESPONCEMESSAGE,'') RESPONCEMESSAGE,ISNULL(PURCHASEPEPPOLID,0) PURCHASEPEPPOLID,ISNULL(PEPPOLINVOICEUUID,'') PEPPOLINVOICEUUID,ISNULL(POSENDSTATUS,'') POSENDSTATUS,ISNULL(RESPONCECODE,'') RESPONCECODE,ISNULL(RESPONCEMESSAGE,'') RESPONCEMESSAGE FROM ["+ ht.get("PLANT") +"_POHDR] WHERE ISNULL(PURCHASEPEPPOLID,0) != 0 AND ";
				Enumeration enum1 = ht.keys();
				for (int i = 0; i < ht.size(); i++) {
					String key = StrUtils.fString((String) enum1.nextElement());
					String value = StrUtils.fString((String) ht.get(key));
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
				query += astatus;
				query += " ORDER BY ID DESC";
				this.mLogger.query(this.printQuery, query);
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
						PoHdr pohdr = new PoHdr();
						ResultSetToObjectMap.loadResultSetIntoObject(rst, pohdr);
						poHeaders.add(pohdr);
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
			return poHeaders;
		}
		
		
		public boolean updateProductHdr(String query, Hashtable htCondition, String extCond)
				throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" UPDATE " + "["
						+ htCondition.get("PLANT") + "_" + "PRODUCTRECEIVEHDR" + "]");
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
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return flag;
		}
		
		public Map selectProductRow(String query, Hashtable ht) throws Exception {
			Map map = new HashMap();

			java.sql.Connection con = null;
			try {

				con = com.track.gates.DbBean.getConnection();
				StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
						+ "[" + ht.get("PLANT") + "_" + "PRODUCTRECEIVEHDR" + "]");
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
}
