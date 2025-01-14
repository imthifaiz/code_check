
package com.track.dao;

import com.track.constants.IConstants;

import com.track.constants.IDBConstants;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

public class POSHdrDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POSHDRDAO_PRINTPLANTMASTERQUERY;
	public MLogger getmLogger() {
		return mLogger;
	}
	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
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
	private boolean printLog = MLoggerConstant.POSHDRDAO_PRINTPLANTMASTERLOG;
	public static final String TABLE_NAME = "POSHDR";
	public boolean insertIntoPosHdr(Hashtable ht) throws Exception {
		boolean inserted = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE_NAME + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			inserted = insertData(con, query);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return inserted;
	}
	//Retrieve Sales Details
	public ArrayList getSalesDet(String plant) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,item,");
			sql.append("itemdesc,");
			sql.append("qty,unitprice,purchasedate,purchasetime");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] ");
			sql.append(" ORDER BY tranid,item");
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
	
	public ArrayList getPosDetails(String plant,String TranId) throws Exception {
	
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,item,");
			sql.append("itemdesc,");
			sql.append("qty,unitprice,totalprice");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"+ TABLE_NAME+"]"+" where plant='"+plant+"' AND POSTRANID='"+TranId+"'");
			sql.append(" ORDER BY crat");
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
        
    public boolean update(String query, Hashtable htCondition, String extCond)
                    throws Exception {
            boolean flag = false;
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
    
    public boolean updatePOSRECIPTHeader(String query, Hashtable htCondition, String extCond)
                    throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" UPDATE " + "["
                                    + htCondition.get("PLANT") + "_POS_RECIPT_HDR]");
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
    
    public Map getPOSReciptHeaderDetails(String plant) throws Exception {

        MLogger.log(1, this.getClass() + " getPOSReciptHeaderDetails()");
        Map m = new HashMap();
        java.sql.Connection con = null;
        String scondtn ="";
        try {
                
                con = DbBean.getConnection();
                
                StringBuffer sql = new StringBuffer("  SELECT  PAYMENT_MODE ,ISNULL(SALES_REP,'') SALES_REP ,SERIAL_NO ,ISNULL(RECEIPT_NO1,'') as RECEIPT_NO ,PRODUCT,PROD_DESC ,UNIT_PRICE ,QTY ,UNITMO ,DISCOUNT ,TOTAL ,SUBTOTAL ,TAX ,TOTAL_AMT ,AMOUNT_PAID ,CHANGE, ");
                sql.append("HEADER AS HDR,ISNULL(GREETINGS1,'') AS G1 ,ISNULL(GREETINGS2,'') AS G2,ISNULL(PrintOrientation,'') AS PrintOrientation,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2,ISNULL(ADDRBYLOC,'0') AS ADDRBYLOC,ISNULL(DOWNLOADPDF,'0') AS DOWNLOADPDF ");
                sql.append(" ");
                sql.append(" FROM " + "[" + plant + "_"
                                + "POS_RECIPT_HDR] where plant='"+plant+"'");
         
                
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
    public boolean deleteProductForTranId(String plant,String itemList)
                    throws Exception {
            boolean deleteprdForTranId = false;
            PreparedStatement ps = null;
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
                                    + " WHERE " + IConstants.ITEM + " in (" + itemList + ")";
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
	
	
	public ArrayList getDistinctTranId(String plant) throws Exception {

		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT count(*) ");
			sql.append("tranid,");

		sql.append(" FROM " + "[" + plant + "_"+ TABLE_NAME+"] where plant='"+plant+"'");
		
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
	public ArrayList getDistinctSales(String plant,String startdt,String enddt,String item,String loc,String recptno,String extracondtn) throws Exception {

		MLogger.log(1, this.getClass() + " getDistinctSales()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
				if(startdt.length()>0)
					{startdt    = startdt.substring(6)+"-"+ startdt.substring(3,5)+"-"+startdt.substring(0,2);
					scondtn="and purchasedate>='"+startdt+"'";}
				if(enddt.length()>0)
					{startdt    = startdt.substring(6)+"-"+ startdt.substring(3,5)+"-"+startdt.substring(0,2);
					scondtn=scondtn+"and purchasedate<='"+enddt+"'";}
				if(item.length()>0)
					scondtn=scondtn+"and item='"+item+"'";
				if(loc.length()>0)
					scondtn=scondtn+"and loc='"+loc+"'";
				if(recptno.length()>0)
					scondtn=scondtn+"and Tranid='"+recptno+"'";
				StringBuffer sql = new StringBuffer("  SELECT  ");
				sql.append("tranid,sum(unitprice) as price");
		
				sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+scondtn+extracondtn);
			
			
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
           public boolean isExisit(Hashtable ht, String extCond) throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
                    con = com.track.gates.DbBean.getConnection();
                    StringBuffer sql = new StringBuffer(" SELECT ");
                    sql.append("COUNT(*) ");
                    sql.append(" ");
                    sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");
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
	//Retrieve sales by tranid
	public ArrayList getSalesByTranid(String plant,String tranid,String condtn) throws Exception {

		
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("tranid,item,");
			sql.append("itemdesc,");
			sql.append("qty,unitprice,purchasedate,purchasetime,paymentmode");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+"and tranid='"+tranid+"'"+scondtn);
			
			//			sql.append(" ORDER BY tranid,item");
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
	//Retrieve sales by tranid
	public ArrayList getSalesforPDFTranid(String plant,String tranid,String condtn) throws Exception {

		MLogger.log(1, this.getClass() + " getReasonCode()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("ITEM,ITEMDESC,QTY,UNITPRICE, QTY * UNITPRICE AS PRICE");
			
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "sales_detail] where plant='"+plant+"'"+"and tranid='"+tranid+"'"+scondtn);
			
			
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
        
        

        public  String genPosTranId(String plant,String aUser,String Loc)
        throws  Exception {
         StrUtils   strUtils = new StrUtils();
       
       String posTranID ="";
       POSHdrDAO poshdr = new  POSHdrDAO();
        

        try {
           Hashtable ht = new Hashtable();
            posTranID =Loc+"_"+new DateUtils().getDateInyyMMddHHmmssSSS();
            ht.put(IDBConstants.PLANT, plant);
            ht.put(IDBConstants.POS_TRANID, posTranID);
            ht.put(IDBConstants.POSHDR_STATUS, "N");
            ht.put(IDBConstants.POS_TRANDT, new DateUtils().getDate());
            ht.put(IDBConstants.POS_TRANTM, new DateUtils().Time());
            ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
            ht.put(IDBConstants.CREATED_BY,aUser);
            poshdr.insertIntoPosHdr(ht);

        }

        catch (Exception e) {
        this.mLogger.exception(this.printLog, "", e);
        throw e;
        }
        return posTranID;
        }
   public  String genPosManualTranId(String plant,String aUser,String Loc,String posTranID)
                throws  Exception {
                 StrUtils   strUtils = new StrUtils();
                 POSHdrDAO poshdr = new  POSHdrDAO();
                  try {
                   Hashtable ht = new Hashtable();
                    //posTranID =Loc+"_"+new DateUtils().getDateInyyMMddHHmmssSSS();
                    ht.put(IDBConstants.PLANT, plant);
                    ht.put(IDBConstants.POS_TRANID, posTranID);
                    ht.put(IDBConstants.POSHDR_STATUS, "N");
                    ht.put(IDBConstants.POS_TRANDT, new DateUtils().getDate());
                    ht.put(IDBConstants.POS_TRANTM, new DateUtils().Time());
                    ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
                    ht.put(IDBConstants.CREATED_BY,aUser);
                    //ht.put("TRANTYPE",trantype);
                    poshdr.insertIntoPosHdr(ht);

                }

                catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e;
                }
                return posTranID;
      }
   public Map getPOSReceiveReciptHeaderDetails(String plant) throws Exception {

       MLogger.log(1, this.getClass() + " getPOSReceiveReciptHeaderDetails()");
       Map m = new HashMap();
       java.sql.Connection con = null;
       String scondtn ="";
       try {
               
               con = DbBean.getConnection();
               
               StringBuffer sql = new StringBuffer("  SELECT  PAYMENT_MODE ,ISNULL(SALES_REP,'') SALES_REP ,SERIAL_NO ,ISNULL(RECEIPT_NO,'') as RECEIPT_NO ,PRODUCT,PROD_DESC ,UNIT_COST ,QTY ,UNITMO ,DISCOUNT ,TOTAL ,SUBTOTAL ,TAX ,TOTAL_AMT ,AMOUNT_PAID ,CHANGE, ");
               sql.append("HEADER AS HDR,ISNULL(GREETINGS1,'') AS G1,ISNULL(PrintOrientation,'') AS PrintOrientation ,ISNULL(GREETINGS2,'') AS G2,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2,ISNULL(ADDRBYLOC,'0') AS ADDRBYLOC,ISNULL(DOWNLOADPDF,'0') AS DOWNLOADPDF ");
               sql.append(" ");
               sql.append(" FROM " + "[" + plant + "_"
                               + "POSRECEIVE_RECIPT_HDR] where plant='"+plant+"'");
        
               
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
   
   public Map getPOSShiftCloseReceiptHdrDetails(String plant) throws Exception {

       MLogger.log(1, this.getClass() + " getPOSShiftCloseReceiptHdrDetails()");
       Map m = new HashMap();
       java.sql.Connection con = null;
       String scondtn ="";
       try {
               
               con = DbBean.getConnection();
               
               StringBuffer sql = new StringBuffer("  SELECT   ISNULL(PRINTWITHPRODUCT,'0') AS PRINTWITHPRODUCT,ISNULL(PRINTWITHCATEGORY,'0') AS PRINTWITHCATEGORY,ISNULL(PRINTWITHBRAND,'0') AS PRINTWITHBRAND,ISNULL(PRINTWITHCLOSINGSTOCK,'0') AS PRINTWITHCLOSINGSTOCK ");
               sql.append(" ");
               sql.append(" FROM " + "[" + plant + "_"
                               + "POS_SHIFT_CLOSE_RECIPT_HDR] where plant='"+plant+"'");
        
               
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
public Map getPOSMoveHeaderDetails(String plant) throws Exception {

    MLogger.log(1, this.getClass() + " getPOSReceiveReciptHeaderDetails()");
    Map m = new HashMap();
    java.sql.Connection con = null;
    String scondtn ="";
    try {
            
            con = DbBean.getConnection();
            
            StringBuffer sql = new StringBuffer("  SELECT  PAYMENT_MODE ,ISNULL(SALES_REP,'') SALES_REP ,SERIAL_NO ,ISNULL(RECEIPT_NO,'') as RECEIPT_NO ,PRODUCT,PROD_DESC ,UNIT_COST ,QTY ,UNITMO,DISCOUNT ,TOTAL ,SUBTOTAL ,TAX ,TOTAL_AMT ,AMOUNT_PAID ,CHANGE, ");
            sql.append("HEADER AS HDR,ISNULL(GREETINGS1,'') AS G1,ISNULL(PrintOrientation,'') AS PrintOrientation ,ISNULL(GREETINGS2,'') AS G2,ISNULL(FOOTER1,'') AS F1,ISNULL(FOOTER2,'') AS F2,ISNULL(ADDRBYLOC,'0') AS ADDRBYLOC,ISNULL(DOWNLOADPDF,'0') AS DOWNLOADPDF ");
            sql.append(" ");
            sql.append(" FROM " + "[" + plant + "_"
                            + "STOCKMOVE_HDR] where plant='"+plant+"'");
     
            
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
public boolean updatePOSMoveHeader(String query, Hashtable htCondition, String extCond)
        throws Exception {
boolean flag = false;
java.sql.Connection con = null;
try {
        con = com.track.gates.DbBean.getConnection();
        StringBuffer sql = new StringBuffer(" UPDATE " + "["
                        + htCondition.get("PLANT") + "_STOCKMOVE_HDR]");
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

public boolean updatePOSReceiveRECIPTHeader(String query, Hashtable htCondition, String extCond)
        throws Exception {
boolean flag = false;
java.sql.Connection con = null;
try {
        con = com.track.gates.DbBean.getConnection();
        StringBuffer sql = new StringBuffer(" UPDATE " + "["
                        + htCondition.get("PLANT") + "_POSRECEIVE_RECIPT_HDR]");
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
// thanzi
public boolean updatePOSShiftCloseReceiveRECIPT(String query, Hashtable htCondition, String extCond)
        throws Exception {
boolean flag = false;
java.sql.Connection con = null;
try {
        con = com.track.gates.DbBean.getConnection();
        StringBuffer sql = new StringBuffer(" UPDATE " + "["
                        + htCondition.get("PLANT") + "_POS_SHIFT_CLOSE_RECIPT_HDR]");
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
//end

public ArrayList selectPosHdr(String query, Hashtable ht) throws Exception {
//	boolean flag = false;
	ArrayList<Map<String, String>> alData = new ArrayList<>();
	java.sql.Connection con = null;
	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ ht.get("PLANT") + "_" + "POSHDR" + "]");
	String conditon = "";

	try {
		con = com.track.gates.DbBean.getConnection();

		if (ht.size() > 0) {
			this.mLogger.log(0, "condition preisent stage 3");
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
public  String insPosManualTranId(String aUser,String Loc,Hashtable ht1)
        throws  Exception {
	String posTranID=(String)ht1.get(IDBConstants.POS_TRANID);
         StrUtils   strUtils = new StrUtils();
         POSHdrDAO poshdr = new  POSHdrDAO();
          try {
        	  
           Hashtable ht = new Hashtable();            
            ht.put(IDBConstants.PLANT, ht1.get(IDBConstants.PLANT));
            ht.put(IDBConstants.POS_TRANID, ht1.get(IDBConstants.POS_TRANID));
            ht.put(IDBConstants.POSHDR_STATUS, "N");
            ht.put(IDBConstants.POS_TRANDT, new DateUtils().getDate());
            ht.put(IDBConstants.POS_TRANTM, new DateUtils().Time());
            ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
            ht.put(IDBConstants.CREATED_BY,aUser);
            
            ht.put(IDBConstants.DOHDR_CUST_CODE, ht1.get(IDBConstants.DOHDR_CUST_CODE));
			ht.put(IDBConstants.DOHDR_CUST_NAME, ht1.get(IDBConstants.DOHDR_CUST_NAME));
			ht.put(IDBConstants.DOHDR_JOB_NUM, ht1.get(IDBConstants.DOHDR_JOB_NUM));
			ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, ht1.get(IDBConstants.DOHDR_PERSON_INCHARGE));
			ht.put(IDBConstants.DOHDR_CONTACT_NUM, ht1.get(IDBConstants.DOHDR_CONTACT_NUM));
			ht.put(IDBConstants.DOHDR_ADDRESS, ht1.get(IDBConstants.DOHDR_ADDRESS));
			ht.put(IDBConstants.DOHDR_ADDRESS2, ht1.get(IDBConstants.DOHDR_ADDRESS2));
			ht.put(IDBConstants.DOHDR_ADDRESS3, ht1.get(IDBConstants.DOHDR_ADDRESS3));			
			ht.put(IDBConstants.DOHDR_REMARK1, ht1.get(IDBConstants.DOHDR_REMARK1));								
			ht.put(IDBConstants.DOHDR_REMARK3, ht1.get(IDBConstants.DOHDR_REMARK3));								
			ht.put(IDBConstants.ORDERTYPE, ht1.get(IDBConstants.ORDERTYPE));
			ht.put(IDBConstants.CURRENCYID, ht1.get(IDBConstants.CURRENCYID));											
			ht.put(IDBConstants.DELIVERYDATE, ht1.get(IDBConstants.DELIVERYDATE));
			ht.put(IDBConstants.DOHDR_GST, ht1.get(IDBConstants.DOHDR_GST));										
			ht.put(IDBConstants.SHIPPINGCUSTOMER, ht1.get(IDBConstants.SHIPPINGCUSTOMER));
			ht.put(IDBConstants.SHIPPINGID, ht1.get(IDBConstants.SHIPPINGID));
			ht.put(IDBConstants.ORDERDISCOUNT, ht1.get(IDBConstants.ORDERDISCOUNT));
			ht.put(IDBConstants.SHIPPINGCOST, ht1.get(IDBConstants.SHIPPINGCOST));
			ht.put(IDBConstants.INCOTERMS, ht1.get(IDBConstants.INCOTERMS));
			ht.put(IDBConstants.RECSTATUS, "I");
			ht.put(IDBConstants.CASHCUST, ht1.get(IDBConstants.CASHCUST));
			ht.put(IDBConstants.PAYMENTTYPE, ht1.get(IDBConstants.PAYMENTTYPE));
			ht.put(IDBConstants.POSHDR_DELIVERYDATEFORMAT, ht1.get(IDBConstants.POSHDR_DELIVERYDATEFORMAT));
			//ht.put(IDBConstants.CUSTOMERTYPEDESC1, ht1.get(IDBConstants.CUSTOMERTYPEDESC1));
			//ht.put(IDBConstants.PAYMENTTYPE, ht1.get(IDBConstants.PAYMENTTYPE));
            poshdr.insertIntoPosHdr(ht);

        }

        catch (Exception e) {
        this.mLogger.exception(this.printLog, "", e);
        throw e;
        }
        return posTranID;
}
public String getOrderTypeForTaxInv(String aPlant, String aDono) throws Exception {
    String OrderType = "";
    Hashtable<String, String> ht = new Hashtable<>();
    ht.put("PLANT", aPlant);
    ht.put("POSTRANID", aDono);
    String query = " case  ordertype when '' then 'TAX INVOICE' else upper(isnull(ordertype,'')) end  AS  ORDERTYPE ";
    Map m = selectRow(query, ht);
    OrderType = (String) m.get("ORDERTYPE");
    return OrderType;
}
public Map selectRow(String query, Hashtable ht) throws Exception {
	Map map = new HashMap();
	java.sql.Connection con = null;
	try {

		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "[" + ht.get("PLANT") + "_" + "POSHDR" + "]");
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
public ArrayList selectPosHdr(String query, Hashtable ht, String extCond)
		throws Exception {
//	boolean flag = false;
	ArrayList alData = new ArrayList();
	
	String plant = (String) ht.get("PLANT");

	java.sql.Connection con = null;

	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ plant + "_" + "POSHDR" + "] D ");
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
public ArrayList selectdnplPosDet(String query, Hashtable ht, String extCond) throws Exception {
	ArrayList alData = new ArrayList();
	java.sql.Connection con = null;


	StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
			+ (ht.get("PLANT") == null ? ht.get("A.PLANT") : ht.get("PLANT")) + "_" + "SHIPHIS" + "] A "); 
					
	String conditon = "";
	try {
		con = com.track.gates.DbBean.getConnection();
		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = "a.plant='" + (ht.get("PLANT") == null ? ht.get("A.PLANT") : ht.get("PLANT")) + "' and a.dono ='"
					+ ht.get("A.DONO") + "' and a.INVOICENO = '" + ht.get("INVOICENO") + "'";
			conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
		}else {
			if (extCond.length() > 0)
				sql.append(" WHERE " + extCond);
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
public boolean deletePosHdr(java.util.Hashtable ht) throws Exception {
	boolean delete = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();

		StringBuffer sql = new StringBuffer(" DELETE ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME + "]");
		sql.append(" WHERE " + formCondition(ht));

		this.mLogger.query(this.printQuery, sql.toString());
		delete = updateData(con, sql.toString());
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return delete;
}
public String getUnitCostCovertedTolocalCurrency(String aPlant, String poNO,String unitCost) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
    	con = DbBean.getConnection();
        StringBuffer SqlQuery = new StringBuffer(" select  cast ("+unitCost+" as float) / cast ((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POSHDR WHERE POSTRANID =R.POSTRANID))  aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) ");
        SqlQuery.append(" AS UNITCOST FROM "+aPlant+"_POSHDR R WHERE POSTRANID='"+poNO+"' ");


   // 
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
public String getLocalCurrency(String aPlant, String CURRENCY,String item) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
    	con = DbBean.getConnection();
        StringBuffer SqlQuery = new StringBuffer(" select isnull(UnitPrice,0) * cast ((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID= '"+CURRENCY+"') ");
        SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST from "+aPlant+"_itemmst where item='"+item+"'");


   // 
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
public String getlocalCurrencyCovertedToUnitCost(String aPlant, String poNO,String unitCost) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
    	con = DbBean.getConnection();
        StringBuffer SqlQuery = new StringBuffer(" select ISNULL("+unitCost+" * cast ((SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+aPlant+"_POSHDR WHERE POSTRANID =R.POSTRANID))  ");
        SqlQuery.append(" aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")),0) AS UNITCOST FROM "+aPlant+"_POSHDR R WHERE POSTRANID='"+poNO+"' ");


   // 
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
public String getCurrencyUseQT(String plant, String posNO) throws Exception {
	java.sql.Connection con = null;
	String getCurrencyUseQT = "";
	try {

		con = DbBean.getConnection();

		StringBuffer SqlQuery = new StringBuffer(
				" SELECT CURRENCYUSEQT FROM [" + plant + "_"
						+ "CURRENCYMST] where plant='" + plant
						+ "' AND CURRENCYID = (SELECT CURRENCYID from "
						+ plant + "_POSHDR WHERE POSTRANID ='" + posNO + "')");

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
public String getLocalCurrencyConvert(String aPlant, String CURRENCY,String item) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
    	con = DbBean.getConnection();
        StringBuffer SqlQuery = new StringBuffer(" select cast (isnull(UnitPrice,0) * (SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID= '"+CURRENCY+"') ");
        SqlQuery.append("  aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST from "+aPlant+"_itemmst where item='"+item+"'");


   // 
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
public String getminSellingLocalCurrencyConvert(String aPlant, String CURRENCY,String item) throws Exception {
    java.sql.Connection con = null;
    String UnitCostForSelCurrency = "";
    try {
            
    	con = DbBean.getConnection();
        StringBuffer SqlQuery = new StringBuffer(" select cast (isnull(MINSPRICE,0) * (SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST]  WHERE  CURRENCYID= '"+CURRENCY+"') ");
        SqlQuery.append("  aS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) AS UNITCOST from "+aPlant+"_itemmst where item='"+item+"'");


   // 
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
public String getOBDiscountSelectedItem(String plant, String currencyid,String itemNo,String custcode) throws Exception {
    java.sql.Connection con = null;
    String OBDiscount= "";
    String CustCode="";
    try {
    con = DbBean.getConnection();
    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
     
    StringBuffer SqlQuery = new StringBuffer(" SELECT ISNULL(OBDISCOUNT,'0.00') OBDISCOUNT FROM ["+plant+"_MULTI_PRICE_MAPPING]  WHERE  ITEM='"+itemNo+"' AND CUSTOMER_TYPE_ID IN ");
    SqlQuery.append(" (SELECT CUSTOMER_TYPE_ID FROM ["+plant+"_CUSTMST] WHERE CUSTNO  ='" + custcode + "')");
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


public boolean insertIntoPosOutletPrice(Hashtable ht) throws Exception {
	boolean insertedPosOutletPrice = false;
	PreparedStatement ps = null;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String FIELDS = "", VALUES = "";
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = StrUtils.fString((String) enum1.nextElement());
			String value = StrUtils.fString((String) ht.get(key));
			FIELDS += key.toUpperCase() + ",";
			VALUES += "'" + value + "',";
		}
		String sQry = "INSERT INTO " + "[POS_OUTLET_PRICE]" + " ("
				+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		int iCnt = ps.executeUpdate();
		if (iCnt > 0)
			insertedPosOutletPrice = true;

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}

	return insertedPosOutletPrice;
}

public boolean insertIntoPosOutletminmax(Hashtable ht) throws Exception {
	boolean insertedPosOutletminmax = false;
	PreparedStatement ps = null;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String FIELDS = "", VALUES = "";
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = StrUtils.fString((String) enum1.nextElement());
			String value = StrUtils.fString((String) ht.get(key));
			FIELDS += key.toUpperCase() + ",";
			VALUES += "'" + value + "',";
		}
		String sQry = "INSERT INTO " + "[POS_OUTLET_MINMAX]" + " ("
				+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		int iCnt = ps.executeUpdate();
		if (iCnt > 0)
			insertedPosOutletminmax = true;

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}

	return insertedPosOutletminmax;
}

public boolean deletePosOutletminmax(String Outlet, String plant) throws Exception {
	boolean deleteItemMst = false;
	PreparedStatement ps = null;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "DELETE FROM POS_OUTLET_MINMAX "
				+ " WHERE " + IConstants.OUTLETS_CODE + "='"
				+ Outlet.toUpperCase() + "' and PLANT = '"+plant+"' ";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		int iCnt = ps.executeUpdate();
		if (iCnt > 0)
			deleteItemMst = true;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}

	return deleteItemMst;
}

public boolean deletePosOutletPrice(String Outlet, String plant) throws Exception {
	boolean deleteItemMst = false;
	PreparedStatement ps = null;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "DELETE FROM POS_OUTLET_PRICE "
				+ " WHERE " + IConstants.OUTLETS_CODE + "='"
				+ Outlet.toUpperCase() + "' and PLANT = '"+plant+"' ";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		int iCnt = ps.executeUpdate();
		if (iCnt > 0)
			deleteItemMst = true;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	
	return deleteItemMst;
}

public boolean updatePosOutletPrice(Hashtable htUpdate, Hashtable htCondition)
		throws Exception {
	boolean updatePosOutletPrice = false;
	PreparedStatement ps = null;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sUpdate = "", sCondition = "";

		// generate the condition string
		Enumeration enum1Update = htUpdate.keys();
		for (int i = 0; i < htUpdate.size(); i++) {
			String key = StrUtils.fString((String) enum1Update
					.nextElement());
			String value = StrUtils.fString((String) htUpdate.get(key));
			sUpdate += key.toUpperCase() + " = '" + value + "',";
		}

		// generate the update string
		Enumeration enum1Condition = htCondition.keys();
		for (int i = 0; i < htCondition.size(); i++) {
			String key = StrUtils.fString((String) enum1Condition
					.nextElement());
			String value = StrUtils.fString((String) htCondition.get(key));
			sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
					+ "' AND ";
		}
		sUpdate = (sUpdate.length() > 0) ? sUpdate.substring(0, sUpdate
				.length() - 1) : "";
		sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
				sCondition.length() - 4) : "";
		String sQry = "UPDATE " + "[POS_OUTLET_PRICE]" + " SET " + sUpdate + " WHERE "+ sCondition;

		this.mLogger.query(this.printQuery, sQry);

		ps = con.prepareStatement(sQry);
		int iCnt = ps.executeUpdate();
		if (iCnt > 0)
			updatePosOutletPrice = true;

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con, ps);
	}

	return updatePosOutletPrice;
}


public boolean updatePosOutletminmax(Hashtable htUpdate, Hashtable htCondition)
		throws Exception {
	boolean updatePosOutletminmax = false;
	PreparedStatement ps = null;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sUpdate = "", sCondition = "";

		// generate the condition string
		Enumeration enum1Update = htUpdate.keys();
		for (int i = 0; i < htUpdate.size(); i++) {
			String key = StrUtils.fString((String) enum1Update
					.nextElement());
			String value = StrUtils.fString((String) htUpdate.get(key));
			sUpdate += key.toUpperCase() + " = '" + value + "',";
		}

		// generate the update string
		Enumeration enum1Condition = htCondition.keys();
		for (int i = 0; i < htCondition.size(); i++) {
			String key = StrUtils.fString((String) enum1Condition
					.nextElement());
			String value = StrUtils.fString((String) htCondition.get(key));
			sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
					+ "' AND ";
		}
		sUpdate = (sUpdate.length() > 0) ? sUpdate.substring(0, sUpdate
				.length() - 1) : "";
		sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
				sCondition.length() - 4) : "";
		String sQry = "UPDATE " + "[POS_OUTLET_MINMAX]" + " SET " + sUpdate + " WHERE "+ sCondition;

		this.mLogger.query(this.printQuery, sQry);

		ps = con.prepareStatement(sQry);
		int iCnt = ps.executeUpdate();
		if (iCnt > 0)
			updatePosOutletminmax = true;

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con, ps);
	}

	return updatePosOutletminmax;
}

public int getCountPosOutletPrice(Hashtable ht) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int iCntItemmst = 0;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sCondition = "";
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = StrUtils.fString((String) enum1.nextElement());
			String value = StrUtils.fString((String) ht.get(key));
			sCondition += key + " = '" + value + "' AND ";
		}
		sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
				sCondition.length() - 4) : "";
		String sQry = "SELECT COUNT(" + IConstants.ITEM + ") FROM " + "[POS_OUTLET_PRICE]" + " WHERE "+ sCondition;
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			iCntItemmst = rs.getInt(1);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return iCntItemmst;
}

public int getCountPosOutletminmax(Hashtable ht) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int iCntItemmst = 0;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sCondition = "";
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = StrUtils.fString((String) enum1.nextElement());
			String value = StrUtils.fString((String) ht.get(key));
			sCondition += key + " = '" + value + "' AND ";
		}
		sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
				sCondition.length() - 4) : "";
		String sQry = "SELECT COUNT(" + IConstants.ITEM + ") FROM " + "[POS_OUTLET_MINMAX]" + " WHERE "+ sCondition;
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			iCntItemmst = rs.getInt(1);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return iCntItemmst;
}

public ArrayList getsalesdetailsforsummary(String plant,Hashtable ht, String afrmDate,
		String atoDate) {
	ArrayList arrList = new ArrayList();
	String sCondition = "",dtCondStr="",extraCon="";
	StringBuffer sql;
	 Hashtable htData = new Hashtable();
	try {
		

        		dtCondStr ="and ISNULL(A.CRAT,'')<>'' AND CAST((SUBSTRING(A.CRAT, 1, 4) + '-' + SUBSTRING(a.CRAT, 5, 2) + '-' + SUBSTRING(a.CRAT, 7, 2)) AS date)";
        		extraCon= "order by A.TRANID DESC,CAST((SUBSTRING(A.CRAT, 1, 4) + '-' + SUBSTRING(a.CRAT, 5, 2) + '-' + SUBSTRING(a.CRAT, 7, 2)) AS date) DESC ";
		       
				   if (afrmDate.length() > 0) {
	              	sCondition = sCondition + dtCondStr + "  >= '" 
	  						+ afrmDate
	  						+ "'  ";
	  				if (atoDate.length() > 0) {
	  					sCondition = sCondition +dtCondStr+ " <= '" 
	  					+ atoDate
	  					+ "'  ";
	  				}
	  			  } else {
	  				if (atoDate.length() > 0) {
	  					sCondition = sCondition +dtCondStr+ " <= '" 
	  					+ atoDate
	  					+ "'  ";
	  				}
	  		     	}   
	           
		        
                 
		           
		       sql = new StringBuffer("with ct as ( ");				           
		       sql.append("select DISTINCT A.CRAT as CRAT,A.POSTRANID as TRANID,ISNULL(A.LOC,'') as LOC,ISNULL(A.TOLOC,'') as TOLOC,A.TRANDT as PURCHASEDATE,STATUS");	
		       sql.append(" from " + plant +"_POSHDR A,"+plant+"_POSDET B  WHERE A.PLANT='"+ plant+"' and  B.POSTYPE ='MOVEWITHBATCH' " + sCondition+" ) ");			           
		       sql.append("select A.TRANID as TRANID,A.LOC as LOC,A.TOLOC as TOLOC,A.PURCHASEDATE as PURCHASEDATE,STATUS from ct as A where A.TRANID!='' ");
		           
		           if (ht.get("TRANID") != null) {
	  					  sql.append(" AND A.TRANID = '" + ht.get("TRANID") + "'");
	  				    }
				   if (ht.get("FROM_LOC") != null) {
	  					  sql.append(" AND LOC = '" + ht.get("FROM_LOC") + "'");
	  				    }
				   if (ht.get("TOLOC") != null) {
	  					  sql.append(" AND A.TOLOC = '" + ht.get("TOLOC") + "'");
	  				    }
				   
		           
				  arrList = selectForReport(sql.toString(), htData, extraCon);
   

	 }catch (Exception e) {
		this.mLogger.exception(this.printLog,
				"Exception :POSHdrDAO :: getsalesdetailsforsummary:", e);
	}
	return arrList;
}

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

}

