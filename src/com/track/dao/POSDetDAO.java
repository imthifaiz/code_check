

package com.track.dao;

import com.track.constants.IConstants;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.Vector;

public class POSDetDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.POSDETDAO_PRINTPLANTMASTERQUERY;
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
	private boolean printLog = MLoggerConstant.POSDETDAO_PRINTPLANTMASTERLOG;
	public static final String TABLE_NAME = "POSDET";
	public boolean insertIntoPosDet(Hashtable ht) throws Exception {
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
			//sql.append("tranid,item,");
			sql.append("item,");
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

    public boolean deleteProductForTranId(String plant,String itemList,String TranId,String batchlist)
                    throws Exception {
            boolean deleteprdForTranId = false;
            PreparedStatement ps = null;
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    if (itemList.length()==0)
                     itemList="''";
                    
                    String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
                                    + " WHERE " + IConstants.ITEM + " in (" + itemList + ") AND POSTRANID ='"+TranId+"'"
                    				+ " AND "  + IConstants.BATCH + " in (" + batchlist + ")";
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
    
    public boolean deleteProductInForTranId(String plant,String itemList,String TranId)
                    throws Exception {
            boolean deleteprdForTranId = false;
            PreparedStatement ps = null;
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    if (itemList.length()==0)
                     itemList="''";
                    
                    String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
                                    + " WHERE " + IConstants.ITEM + "   in (" + itemList + ") AND POSTRANID ='"+TranId+"'";
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
        
	
    public ArrayList listTranIdForPos(String plant,String loc) throws Exception {

            ArrayList al = new ArrayList();
            java.sql.Connection con = null;
            String scondtn ="";
            try {
                    
                    con = DbBean.getConnection();
                    
                    StringBuffer sql = new StringBuffer(" select   A.POSTRANID,ISNULL((SELECT COUNT(*) FROM "+plant+"_POSDET WHERE POSTRANID = A.POSTRANID),0) AS NOOFITEMS FROM "+plant+"_POSHDR A  ");
                    sql.append(" WHERE A.PLANT = '"+plant+"' AND STATUS <>'C' ORDER BY A.POSTRANID ");

                   
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
	
	public ArrayList getDistinctTranId(String plant,String loc,String trantype) throws Exception {

		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			if(loc.length()>0){
                           scondtn =  "AND  B.LOC ='"+loc+"' ";
                        }
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer(" select  COUNT(B.ITEM)  AS NOOFITEMS , A.POSTRANID FROM "+plant+"_POSHDR A ,"+plant+"_POSDET B  ");
			sql.append(" WHERE A.PLANT = '"+plant+"' AND  B.POSTYPE ='"+trantype+"' AND A.POSTRANID=B.POSTRANID "+ scondtn +" AND STATUS IN('O','H') group by A.POSTRANID  ORDER BY A.POSTRANID ");

		       
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
	
	public ArrayList getDistinctStokeMoveTranId(String plant,String tranid,String trantype) throws Exception {

		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		String scondtn ="";
		try {
			if(tranid.length()>0){
                           scondtn =  "AND  B.POSTRANID like '%"+tranid+"' ";
                        }
			
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer(" select COUNT(B.ITEM)  AS NOOFITEMS ,A.POSTRANID FROM "+plant+"_POSHDR A ,"+plant+"_POSDET B  ");
			sql.append(" WHERE A.PLANT = '"+plant+"' AND  B.POSTYPE ='"+trantype+"' AND A.POSTRANID=B.POSTRANID "+ scondtn +"  group by A.POSTRANID  ORDER BY A.POSTRANID ");

		       
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
        
        
  
    public List listProductsForPOSTranID(String plant, String cond) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            List listQty = new ArrayList();
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                    String sCondition = "";
                    String sQry = "SELECT distinct ITEM,ITEMDESC,BATCH,QTY, CAST(isnull(UNITPRICE,0) AS DECIMAL(10,2)) as UNITPRICE,CAST(isnull(TOTALPRICE,0) AS DECIMAL(10,2)) as TOTALPRICE,isnull(DISCOUNT,'') as DISCOUNT FROM "
                                    + "[" + plant + "_" + TABLE_NAME + "]" + sCondition + cond;
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            Vector lineVec = new Vector();
                            lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                            lineVec.add(1, StrUtils.fString((String) rs
                                            .getString("ITEMDESC")));
                            lineVec.add(2, StrUtils.fString((String) rs
                                            .getString("QTY")));
                            lineVec.add(3, StrUtils.fString((String) rs.getString("UNITPRICE")));
                            lineVec.add(4, StrUtils.fString((String) rs
                                            .getString("TOTALPRICE")));
                            lineVec.add(5, StrUtils.fString((String) rs
                                    .getString("DISCOUNT")));
                            lineVec.add(6, StrUtils.fString((String) rs
                                    .getString("BATCH")));
                    
                            listQty.add(lineVec);
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return listQty;
    }
    
   /* public List listProductsForPOSTranID(String plant,String tranId, String cond) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List listQty = new ArrayList();
        Connection con = null;
        try {
                con = DbBean.getConnection();
                String sCondition = "";
                String sQry = "SELECT distinct ITEM,ITEMDESC,BATCH,QTY, CAST(isnull(UNITPRICE,0) AS DECIMAL(10,2)) as UNITPRICE,CAST(isnull(TOTALPRICE,0) AS DECIMAL(10,2)) as TOTALPRICE,isnull(DISCOUNT,'') as DISCOUNT,isnull(EXPIREDATE,'') as EXPIREDATE,"+
                		"EMPNO,REFNO,hdr.TRANDT as TRANDATE,RSNCODE as REASONCODE,det.LOC as LOC,hdr.TOLOC as TOLOC," +
                		"(SELECT FNAME FROM ["+plant+"_emp_mst] WHERE EMPNO= hdr.EMPNO) as EMPNAME,"
                                + "REMARKS FROM [" + plant + "_" + TABLE_NAME + "] det JOIN "+"[" + plant + "_POSHDR"+"] hdr ON det.POSTRANID = hdr.POSTRANID WHERE det.POSTRANID='"+tranId+"' AND "+sCondition + cond;
                this.mLogger.query(this.printQuery, sQry);
               
                ps = con.prepareStatement(sQry);
                rs = ps.executeQuery();
                while (rs.next()) {
                        Vector lineVec = new Vector();
                        lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                        lineVec.add(1, StrUtils.fString((String) rs
                                        .getString("ITEMDESC")));
                        lineVec.add(2, StrUtils.fString((String) rs
                                        .getString("QTY")));
                        lineVec.add(3, StrUtils.fString((String) rs.getString("UNITPRICE")));
                        lineVec.add(4, StrUtils.fString((String) rs
                                        .getString("TOTALPRICE")));
                        lineVec.add(5, StrUtils.fString((String) rs
                                .getString("DISCOUNT")));
                        lineVec.add(6, StrUtils.fString((String) rs
                                .getString("BATCH")));
                        lineVec.add(7, StrUtils.fString((String) rs
                                .getString("EXPIREDATE")));
                        lineVec.add(8, StrUtils.fString((String) rs
                                .getString("EMPNO")));
                        lineVec.add(9, StrUtils.fString((String) rs
                                .getString("REFNO")));
                        lineVec.add(10, StrUtils.fString((String) rs
                                .getString("TRANDATE")));
                        lineVec.add(11, StrUtils.fString((String) rs
                                .getString("REASONCODE")));
                        lineVec.add(12, StrUtils.fString((String) rs
                                .getString("REMARKS")));
                        lineVec.add(13, StrUtils.fString((String) rs
                                .getString("EMPNAME")));
                        lineVec.add(14, StrUtils.fString((String) rs
                                .getString("LOC")));
                        
                        lineVec.add(15, StrUtils.fString((String) rs
                                .getString("TOLOC")));
                        
                        
                        listQty.add(lineVec);
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        } finally {
                DbBean.closeConnection(con, ps);
        }
        return listQty;
}*/
    
    public List listProductsForPOSTranID(String plant,String tranId, String cond) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List listQty = new ArrayList();
        Connection con = null;
        try {
                con = DbBean.getConnection();
                String sCondition = "";
                String sQry = "SELECT distinct ITEM,ITEMDESC,BATCH,QTY, isnull(UNITPRICE,0)  as UNITPRICE,CAST(isnull(TOTALPRICE,0) AS DECIMAL(18,5)) as TOTALPRICE,isnull(DISCOUNT,'') as DISCOUNT,isnull(EXPIREDATE,'') as EXPIREDATE,"+
                		"EMPNO,REFNO,hdr.TRANDT as TRANDATE,RSNCODE as REASONCODE,det.LOC as LOC,L.LOCDESC as LOCDESC,hdr.TOLOC as TOLOC," +
                		"(SELECT FNAME FROM ["+plant+"_emp_mst] WHERE EMPNO= hdr.EMPNO) as EMPNAME,(SELECT LOCDESC FROM ["+plant+"_LOCMST] WHERE LOC = hdr.TOLOC) AS TOLOCDESC,"+
                	//	"(SELECT TOP 1 isnull(STKUOM,'') FROM ["+plant+"_ITEMMST] I WHERE I.PLANT='"+plant+"' and I.ITEM=ITEM) as STKUOM,"
					      "isnull (det.UNITMO,'') as STKUOM,"
                		+"ISNULL((select distinct INVOICENO from ["+plant+"_SHIPHIS] where DONO=det.POSTRANID and ITEM=det.ITEM and BATCH=det.BATCH),'' ) INVOICENO,"
                                + "REMARKS FROM [" + plant + "_" + TABLE_NAME + "] det JOIN "+"[" + plant + "_POSHDR"+"] hdr ON det.POSTRANID = hdr.POSTRANID JOIN "+"[" + plant + "_LOCMST"+"] L ON det.LOC=L.LOC WHERE det.POSTRANID='"+tranId+"' AND "+sCondition + cond;
                this.mLogger.query(this.printQuery, sQry);
               
                ps = con.prepareStatement(sQry);
                rs = ps.executeQuery();
                while (rs.next()) {
                        Vector lineVec = new Vector();
                        lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                        lineVec.add(1, StrUtils.fString((String) rs
                                        .getString("ITEMDESC")));
                        lineVec.add(2, StrUtils.fString((String) rs
                                        .getString("QTY")));
                        lineVec.add(3, StrUtils.fString((String) rs.getString("UNITPRICE")));
                        lineVec.add(4, StrUtils.fString((String) rs
                                        .getString("TOTALPRICE")));
                        lineVec.add(5, StrUtils.fString((String) rs
                                .getString("DISCOUNT")));
                        lineVec.add(6, StrUtils.fString((String) rs
                                .getString("BATCH")));
                        lineVec.add(7, StrUtils.fString((String) rs
                                .getString("EXPIREDATE")));
                        lineVec.add(8, StrUtils.fString((String) rs
                                .getString("EMPNO")));
                        lineVec.add(9, StrUtils.fString((String) rs
                                .getString("REFNO")));
                        lineVec.add(10, StrUtils.fString((String) rs
                                .getString("TRANDATE")));
                        lineVec.add(11, StrUtils.fString((String) rs
                                .getString("REASONCODE")));
                        lineVec.add(12, StrUtils.fString((String) rs
                                .getString("REMARKS")));
                        lineVec.add(13, StrUtils.fString((String) rs
                                .getString("EMPNAME")));
                        lineVec.add(14, StrUtils.fString((String) rs
                                .getString("LOC")));
                        
                        lineVec.add(15, StrUtils.fString((String) rs
                                .getString("TOLOC")));
                        lineVec.add(16, StrUtils.fString((String) rs
                                .getString("LOCDESC")));
                        lineVec.add(17, StrUtils.fString((String) rs
                                .getString("TOLOCDESC")));
						lineVec.add(18, StrUtils.fString((String) rs
                                .getString("STKUOM")));	
						lineVec.add(19, StrUtils.fString((String) rs
                                .getString("INVOICENO")));	
                        listQty.add(lineVec);
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        } finally {
                DbBean.closeConnection(con, ps);
        }
        return listQty;
}
    public List listHoldProductsForPOSTranID(String plant,String tranId, String cond) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List listQty = new ArrayList();
        Connection con = null;
        try {
                con = DbBean.getConnection();
                String sCondition = "";
                String sQry = "SELECT distinct ITEM,ITEMDESC,BATCH,QTY, CAST(isnull(UNITPRICE,0) AS DECIMAL(10,2)) as UNITPRICE," +
                		"CAST(isnull(TOTALPRICE,0) AS DECIMAL(10,2)) as TOTALPRICE,isnull(DISCOUNT,'') as DISCOUNT," +
                		"isnull(EXPIREDATE,'') as EXPIREDATE," +
                		"EMPNO,REFNO,hdr.TRANDT as TRANDATE,RSNCODE as REASONCODE, det.LOC as LOC," +
                		"(SELECT LOCDESC FROM ["+plant+"_LOCMST] WHERE LOC= det.LOC) as LOCDESC," +
                		"(SELECT FNAME FROM ["+plant+"_emp_mst] WHERE EMPNO= hdr.EMPNO) as EMPNAME,"+
                		"REMARKS,UNITMO FROM "
                                + "[" + plant + "_" + TABLE_NAME + "] det JOIN "+"[" + plant + "_POSHDR"+"] hdr ON det.POSTRANID = hdr.POSTRANID WHERE det.POSTRANID='"+tranId+"' AND "+sCondition + cond;
                this.mLogger.query(this.printQuery, sQry);
               // System.out.println("Testing by Deepu"+sQry);
                
               
                ps = con.prepareStatement(sQry);
                rs = ps.executeQuery();
                while (rs.next()) {
                        Vector lineVec = new Vector();
                        lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                        lineVec.add(1, StrUtils.fString((String) rs
                                        .getString("ITEMDESC")));
                        lineVec.add(2, StrUtils.fString((String) rs
                                        .getString("QTY")));
                        lineVec.add(3, StrUtils.fString((String) rs.getString("UNITPRICE")));
                        lineVec.add(4, StrUtils.fString((String) rs
                                        .getString("TOTALPRICE")));
                        lineVec.add(5, StrUtils.fString((String) rs
                                .getString("DISCOUNT")));
                        lineVec.add(6, StrUtils.fString((String) rs
                                .getString("BATCH")));
                        lineVec.add(7, StrUtils.fString((String) rs
                                .getString("EXPIREDATE")));
                        
                        lineVec.add(8, StrUtils.fString((String) rs
                                .getString("EMPNO")));
                        lineVec.add(9, StrUtils.fString((String) rs
                                .getString("REFNO")));
                        lineVec.add(10, StrUtils.fString((String) rs
                                .getString("TRANDATE")));
                        lineVec.add(11, StrUtils.fString((String) rs
                                .getString("REASONCODE")));
                        lineVec.add(12, StrUtils.fString((String) rs
                                .getString("REMARKS")));
                        lineVec.add(13, StrUtils.fString((String) rs
                                .getString("EMPNAME")));
                        lineVec.add(14, StrUtils.fString((String) rs
                                .getString("LOC")));
						lineVec.add(15, StrUtils.fString((String) rs
                                .getString("LOCDESC")));
                        lineVec.add(16, StrUtils.fString((String) rs
                                .getString("UNITMO")));				

                        listQty.add(lineVec);
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        } finally {
                DbBean.closeConnection(con, ps);
        }
        return listQty;
}
    public boolean deleteProductForTranIdPrice(String plant,String itemList,String TranId,String batchlist,String UNITPRICE,String Location)
            throws Exception {
    boolean deleteprdForTranId = false;
    PreparedStatement ps = null;
    Connection con = null;
    try {
            con = DbBean.getConnection();
            if (itemList.length()==0)
             itemList="''";
            
            String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
                            + " WHERE " + IConstants.ITEM + " in (" + itemList + ") AND POSTRANID ='"+TranId+"'"
            				+ " AND "  + IConstants.BATCH + " in (" + batchlist + ") AND UNITPRICE in (" + UNITPRICE + ") AND LOC in (" + Location + ")";
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
public ArrayList getPosDistinct(String plant,String TranId) throws Exception {

		
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			
			StringBuffer sql = new StringBuffer("  SELECT distinct ");			
			sql.append("item,");
			sql.append("itemdesc,");
			sql.append("unitprice");
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"+ TABLE_NAME+"]"+" where plant='"+plant+"' AND POSTRANID='"+TranId+"'");			
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
public boolean deletePosTranId(String plant,String TranId)
        throws Exception {
		boolean deleteprdForTranId = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
		        con = DbBean.getConnection();
		        
		        
		        String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
		                        + " WHERE POSTRANID ='"+TranId+"'";
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
public boolean deleteProductUOMForTranId(String plant,String itemList,String TranId,String batchlist,String uomlist)
        throws Exception {
boolean deleteprdForTranId = false;
PreparedStatement ps = null;
Connection con = null;
try {
        con = DbBean.getConnection();
        if (itemList.length()==0)
         itemList="''";
        
        String sQry = "DELETE FROM " + "[" + plant + "_" + TABLE_NAME + "]"
                        + " WHERE " + IConstants.ITEM + " in (" + itemList + ") AND POSTRANID ='"+TranId+"'"
        				+ " AND "  + IConstants.BATCH + " in (" + batchlist + ")"
        				+ " AND "  + IConstants.UNITMO + " in (" + uomlist + ")";
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
}

