package com.track.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class LoanHdrDAO extends BaseDAO {

	public static String plant = "";
	private boolean printQuery = MLoggerConstant.LOANHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.LOANHDRDAO_PRINTPLANTMASTERLOG;

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

	public LoanHdrDAO() {
		_StrUtils = new StrUtils();
	}

	public LoanHdrDAO(String plant) {
		this.plant = plant;
		TABLE_NAME = "[" + plant + "_LOANHDR" + "]";
	}

	public static String TABLE_NAME = "LOANHDR";
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public Map selectRow(String query, Hashtable ht) throws Exception {

		Map map = new HashMap();

		java.sql.Connection con = null;

		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "LOANHDR" + "]");
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

		// connection
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "LOANHDR" + "]");
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
			sql.append(" FROM " + TABLE_NAME);
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
			sql.append(" FROM " +  "[" + ht.get("PLANT") + "_" + "LOANHDR" + "]");
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

	public ArrayList selectLoanHdr(String query, Hashtable ht) throws Exception {

		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "LOANHDR" + "]");
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

	public ArrayList selectHdr(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "LOANHDR" + "]");

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

	public ArrayList selectLoanAssigneeHdrDetails(String query, Hashtable ht,
			String extCond) throws Exception {

		ArrayList alData = new ArrayList();

		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "LOANHDR" + "] A,");
		sql.append("[" + ht.get("PLANT") + "_" + "LOAN_ASSIGNEE_MST" + "] B");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {

				sql.append(" WHERE ");

				conditon = "a.plant='" + ht.get("PLANT") + "' and a.ORDNO ='"
						+ ht.get("DONO") + "' and a.custname=b.cname";

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

	public boolean insertLoanHdr(Hashtable ht) throws Exception {
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
					+ "LOANHDR" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Loan order created already");
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
			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_" + "LOANHDR" + "]");

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

	public String getNextOrder(String Plant) throws Exception {

		String MaxDo = "";
		try {
			String query = " isnull(max(" + "ORDNO" + "),'')  as PoNO";
			Hashtable ht = new Hashtable();

			ht.put("PLANT", Plant);

			String extCond = " substring(ORDNO,2,4)='"
					+ DateUtils.getDateYYMM() + "'";
			Map m = selectRow(query, ht, extCond);
			MaxDo = (String) m.get("PoNO");

			if (MaxDo.length() == 0 || MaxDo.equalsIgnoreCase(null)
					|| MaxDo == "") {

				MaxDo = DateUtils.getDateYYMM() + "00000";
			}
			String temp = MaxDo.replace('L', '0');
			int nextNum = Integer.parseInt(temp) + 1;
			MaxDo = String.valueOf(nextNum);
			if (MaxDo.length() < 9)
				MaxDo = "0" + MaxDo;
			MaxDo = "L" + MaxDo;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return MaxDo;
	}

	public boolean insertintoLoanPick(Hashtable ht) throws Exception {

		boolean insertFlag = false;
		java.sql.Connection conn = null;
		String TABLE = "LOAN_PICK";
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
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_" + TABLE
					+ "]" + "(" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
					+ ")";
			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception(" Error in Inserting in ");

		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

		return insertFlag;
	}

	public Boolean removeOrder(String plant2, String dono) throws SQLException {
		Connection connection = null;
		try {
			connection = DbBean.getConnection();
			String sql = "DELETE FROM [" + plant2 + "_"
					+ "LOANHDR] WHERE ORDNO='" + dono + "' AND PLANT='"
					+ plant2 + "'";
			this.mLogger.query(this.printQuery, sql.toString());
			this.DeleteRow(connection, sql);
			return Boolean.valueOf(true);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return Boolean.valueOf(false);
		} finally {
			DbBean.closeConnection(connection);
		}
	}

	public ArrayList selectHdrForPda(String query, Hashtable ht,
			String extCond, Boolean isIssue) throws Exception {

		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String pisckStatusChheck = "";
		if (isIssue) {
			pisckStatusChheck = "and isnull(b.pickstatus,'') <> 'C'";
		}
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "LOANHDR" + "]a," + "["
				+ ht.get("PLANT") + "_"
				+ "LOANDET ] b WHERE a.ORDNO=b.ORDNO AND a.PLANT='"
				+ ht.get("PLANT") + "' " + pisckStatusChheck + "");
		String conditon = "";
		try {
			con = com.track.gates.DbBean.getConnection();
			if (extCond.length() > 0)
				sql.append(" " + extCond);
			sql.append(" " + " group by a.ORDNO, a.CustName, a.Remark1 ORDER BY a.ORDNO desc");
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

	public String getLocation(String plant2, String orderNo,
			String locaionColomString) throws Exception {
		String location = "";
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + locaionColomString
					+ " from " + "[" + plant2 + "_" + "LOANHDR" + "]");
			sql.append(" WHERE PLANT='" + plant2 + "' and ORDNO='" + orderNo
					+ "'");

			this.mLogger.query(this.printQuery, sql.toString());
			Map map = getRowOfData(con, sql.toString());
			location = (String) map.get(locaionColomString);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return location;

	}
	
	public ArrayList getLoanOrderHderDetails(String plant, String orderno)
	throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
	
		StringBuffer sQry = new StringBuffer(
				"select a.ordno,");
		sQry
				.append(" isnull(b.name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
		sQry.append(" from  " + "[" + plant + "_" + "loanhdr" + "]" + " a,"
				+ "[" + plant + "_" + "loan_assignee_mst" + "]" + " b");
		sQry
				.append(" where a.plant='"
						+ plant
						+ "' and a.ordno like '"
						+ orderno
						+ "%' and isnull(a.status,'') <> 'C' and a.custName=b.cname");
	
		this.mLogger.query(this.printQuery, sQry.toString());
	
		al = selectData(con, sQry.toString());
		TABLE_NAME = "LOANHDR";
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
	
	public ArrayList getLoanOrderAssigneeDetails(String plant, String orderno)
		throws Exception {
	
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
	
		StringBuffer sQry = new StringBuffer(
				"select a.ordno,isnull(custname,'') as custname,");
		sQry
				.append(" isnull(name,'') contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3 ");
		sQry.append(" from  " + "[" + plant + "_" + "loanhdr" + "]" + " a,"
				+ "[" + plant + "_" + "loan_assignee_mst" + "]" + " b");
		sQry.append(" where a.plant='" + plant + "' and a.ordno like '"
				+ orderno + "%'  and a.custname=b.cname"); // and
		// isnull(a.pickstatus,'')
		// <> 'C'
	
		this.mLogger.query(this.printQuery, sQry.toString());
	
		al = selectData(con, sQry.toString());
		TABLE_NAME = "LOANHDR";
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
	
	
	public boolean updateLOReciptHeader(String query, Hashtable htCondition, String extCond)
    throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
		try {
			     con = com.track.gates.DbBean.getConnection();
			     StringBuffer sql = new StringBuffer(" UPDATE " + "["
			                     + htCondition.get("PLANT") + "_LOAN_RECIPT_HDR]");
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
	
	 public String getUnitCostBasedOnCurIDSelectedForOrder(String aPlant, String doNO,String aItem) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select CAST(isnull(RENTALPRICE,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_LOANHDR WHERE ORDNO ='"+doNO+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY  + ")) AS UNITCOST ");
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
		 public String getUnitCostBasedOnCurIDSelected(String aPlant, String aORDNO,String aORDLNNO,String aItem) throws Exception {
         java.sql.Connection con = null;
         String UnitCostForSelCurrency = "";
         try {
                 
                 con = DbBean.getConnection();
        

         StringBuffer SqlQuery = new StringBuffer(" select  isnull(RENTALPRICE,0)*(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_LOANHDR WHERE ORDNO ='"+aORDNO+"')) AS UNITCOST ");
         SqlQuery.append(" from "+aPlant+"_LOANDET where item='"+aItem+"' and ORDNO='"+aORDNO+"' and ORDLNNO='"+aORDLNNO+"' ");

         
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
	  public String getUnitCostBasedOnCurIDSelectedForOrderWTC(String aPlant, String doNO,String aItem) throws Exception {
	       java.sql.Connection con = null;
	       String UnitCostForSelCurrency = "";
	       try {
	               
	               con = DbBean.getConnection();
	      

	       StringBuffer SqlQuery = new StringBuffer(" select isnull(RENTALPRICE,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_LOANHDR WHERE ORDNO ='"+doNO+"')) AS UNITCOST ");
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
	
	  public String getCustomerCode(String plant, String dono,String aType) throws Exception {
 	      java.sql.Connection con = null;
 	      String custcode = "";
 	     String SqlQuery="";
 	      try {
 	      
 	       con = DbBean.getConnection();
 	       if(aType.equals("RENTAL"))
 	       {
 	           SqlQuery = " select isnull(custcode,'') custcode FROM ["+plant+"_LOANHDR]  WHERE ORDNO ='"+dono+"'";
 	       }
 	      /* else if(aType.equals("ESTIMATE"))
 	       {
 	    	   SqlQuery = " select isnull(custcode,'') custcode FROM ["+plant+"_ESTHDR]  WHERE ESTNO='"+dono+"'";
 	       }
 	      else if(aType.equals("PDAOB"))
	       {
	    	   SqlQuery = " select isnull(custno,'') custcode FROM ["+plant+"_CUSTMST]  WHERE CNAME='"+dono+"'";
	       }*/
          System.out.println(SqlQuery.toString());
 	       Map m = this.getRowOfData(con, SqlQuery);
 	       custcode = (String) m.get("custcode");
 	       
 	      } catch (Exception e) {
 	              this.mLogger.exception(this.printLog, "", e);
 	              throw e;
 	      } finally {
 	              if (con != null) {
 	                      DbBean.closeConnection(con);
 	              }
 	      }
 	      return custcode;
 	  }
	  
	  public String getOBDiscountSelectedItem(String aPlant, String doNO,String aItem,String aType) throws Exception {
          java.sql.Connection con = null;
          String OBDiscount= "";
          String CustCode="";
          try {
          con = DbBean.getConnection();
          CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
          String custcode =getCustomerCode(aPlant,doNO,aType);
           
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

	
	 public Map getLOReciptHeaderDetails(String plant) throws Exception {

         MLogger.log(1, this.getClass() + " getDOSReciptHeaderDetails()");
         Map m = new HashMap();
         java.sql.Connection con = null;
         String scondtn ="";
         try {
                 
                 con = DbBean.getConnection();
                 
                 StringBuffer sql = new StringBuffer("  SELECT  ");
                  sql.append("ISNULL(ORDERHEADER,'') AS HDR1,ISNULL(TOHEADER,'') AS HDR2 ,ISNULL(FROMHEADER,'') AS HDR3,ISNULL(DATE,'') AS DATE,ISNULL(ORDERNO,'') AS ORDERNO,");
                 sql.append("ISNULL(REFNO,'') AS REFNO,ISNULL(SONO,'') AS SONO,ISNULL(ITEM,'') AS ITEM,");
                 sql.append("ISNULL(DESCRIPTION,'') AS DESCRIPTION,ISNULL(ORDERQTY,'') AS ORDERQTY,ISNULL(UOM,'') AS UOM ,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2,ISNULL(FOOTER3,'') AS F3,ISNULL(FOOTER4,'') AS F4,");
                 sql.append("ISNULL(REMARK1,'') AS REMARK1,ISNULL(DELDATE,'') AS DELDATE,ISNULL(ORDDISCOUNT,'') AS ORDDISCOUNT,ISNULL(EMPNAME,'') AS EMPNAME,ISNULL(PrintOrientation,'Landscape') AS PrintOrientation,");
                 sql.append("ISNULL(PREPAREDBY,'') AS PREPAREDBY,ISNULL(SELLER,'') AS SELLER,ISNULL(SELLERAUTHORIZED,'') AS SELLERAUTHORIZED,ISNULL(BUYER,'') AS BUYER,ISNULL(BUYERAUTHORIZED,'') AS BUYERAUTHORIZED,  ");
                 sql.append("ISNULL(COMPANYNAME,'') AS COMPANYNAME,ISNULL(COMPANYSTAMP,'') AS COMPANYSTAMP,ISNULL(SIGNATURE,'') AS SIGNATURE,ISNULL(VAT,'') AS VAT,ISNULL(CUSTOMERVAT,'') AS CUSTOMERVAT,ISNULL(SHIPPINGCOST,'') AS SHIPPINGCOST,  ");
                 sql.append("ISNULL(TOTALAFTERDISCOUNT,'') AS TOTALAFTERDISCOUNT,ISNULL(ORDDATE,'') AS ORDDATE,ISNULL(SHIPTO,'') AS SHIPTO,ISNULL(NETAMT,'') AS NETAMT,ISNULL(TAXAMT,'') AS TAXAMT,ISNULL(TOTALAMT,'') AS TOTALAMT,ISNULL(RATE,'') AS RATE,ISNULL(PAYMENTTYPE,'') AS PAYMENTTYPE,  ");
                 sql.append("ISNULL(AMOUNT,'') AS AMOUNT,ISNULL(TOTALTAX,'') AS TOTALTAX,ISNULL(TOTALWITHTAX,'') AS TOTALWITHTAX, ");
                 sql.append("ISNULL(DISPLAYBYORDERTYPE,'0') AS DISPLAYBYORDERTYPE,ISNULL(PRINTDETAILDESC,'0') AS PRINTDETAILDESC,ISNULL(PRINTCUSTREMARKS,'0') AS PRINTCUSTREMARKS,ISNULL(PRINTPRDREMARKS,'0') AS PRINTPRDREMARKS,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND, ");
                 sql.append("ISNULL(TOTALROUNDOFF,'0') AS TOTALROUNDOFF,ISNULL(ROUNDOFF,'') AS ROUNDOFF,ISNULL(PRINTWITHEMPLOYEE,'0') AS PRINTWITHEMPLOYEE,ISNULL(PRINTWITHPRDID,'0') AS PRINTWITHPRDID,ISNULL(FOOTER5,'') AS F5,ISNULL(FOOTER6,'') AS F6,ISNULL(FOOTER7,'') AS F7,ISNULL(FOOTER8,'') AS F8,ISNULL(FOOTER9,'') AS F9 ");
                 sql.append(" FROM " + "[" + plant + "_"
                                 + "LOAN_RECIPT_HDR] where plant='"+plant+"'");
          
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


}
