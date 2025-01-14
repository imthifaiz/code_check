 package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * 
 * @author Shayanthan K
 * @version 1.0.0
 * @created Aug 25, 2010 - 3:50:06 PM
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ItemSesBeanDAO extends BaseDAO {

	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.ITEMSESBEANDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.ITEMSESBEANDAO_PRINTPLANTMASTERLOG;

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

	private String tblName = "ITEMMST";

	public boolean insertIntoItemMst(Hashtable ht) throws Exception {
		boolean insertedItemmst = false;
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
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ tblName + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedItemmst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertedItemmst;
	}

	/**
	 * method : deleteItemMst(Hashtable ht) description : Delete the existing
	 * record from Itementory master(ITEMMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean deleteItemMst(Hashtable ht) throws Exception {
		boolean deleteItemMst = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			String sql = "DELETE " + "[" + ht.get("PLANT") + "_" + tblName
					+ "]" + " WHERE " + sCondition;
			ps = con.prepareStatement(sql);
			this.mLogger.query(this.printQuery, sql);
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

	/**
	 * method : deleteItemsmt(Hashtable ht) description : Delete the existing
	 * record from Item master(ITEMMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
		public List queryItemMst(String aItem, String plant, String cond) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		Connection con = null;
		
		try {

			con = DbBean.getConnection();
			String sCondition = "WHERE AIM.ALTERNATE_ITEM_NAME  ='"
					+ aItem.toUpperCase()
					+ "' AND AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT ";
			String sQry = "SELECT DISTINCT IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1, isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID FROM "
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST "
					+ "] as IM, "
					+ "["
					+ plant
					+ "_ALTERNATE_ITEM_MAPPING] as AIM "
					+ sCondition
					+ cond
					+ " ORDER BY IM.ITEM ";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, StrUtils.fString((String) rs
						.getString("ITEMDESC")));
				lineVec.add(2, StrUtils.fString((String) rs
						.getString("ITEMTYPE")));
				lineVec.add(3, StrUtils
						.fString((String) rs.getString("STKUOM")));
				lineVec.add(4, StrUtils.fString((String) rs
						.getString("REMARK1")));
				lineVec.add(5, StrUtils.fString((String) rs
						.getString("REMARK2")));
				lineVec.add(6, StrUtils.fString((String) rs
						.getString("REMARK3")));
				lineVec.add(7, StrUtils.fString((String) rs
						.getString("REMARK4")));
				lineVec.add(8, StrUtils
						.fString((String) rs.getString("STKQTY")));
				lineVec
						.add(9, StrUtils
								.fString((String) rs.getString("ASSET")));
				lineVec.add(10, StrUtils.fString((String) rs
						.getString("PRD_CLS_ID")));
				lineVec.add(11, StrUtils.fString((String) rs
						.getString("ISACTIVE")));
				lineVec.add(12, StrUtils.fString((String) rs
						.getString("UNITPRICE")));
				lineVec
						.add(13, StrUtils
								.fString((String) rs.getString("COST")));
				lineVec.add(14, StrUtils.fString((String) rs
						.getString("DISCOUNT")));
				lineVec.add(15, StrUtils.fString((String) rs
						.getString("USERFLD1")));
				lineVec.add(16, StrUtils.fString((String) rs
						.getString("NONSTKFLAG")));
				lineVec.add(17, StrUtils.fString((String) rs
						.getString("NONSTKTYPEID")));

				listQty.add(lineVec);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}
        
    public List queryItemMstCond(String aItem, String plant, String cond) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    List listQty = new ArrayList();
    Connection con = null;
    
    try {

            con = DbBean.getConnection();
            String sCondition = "WHERE AIM.ALTERNATE_ITEM_NAME LIKE '"
                            + aItem.toUpperCase()
                            + "%' AND AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT ";
            String sQry = "SELECT DISTINCT IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1, isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID FROM "
                            + "["
                            + plant
                            + "_"
                            + "ITEMMST "
                            + "] as IM, "
                            + "["
                            + plant
                            + "_ALTERNATE_ITEM_MAPPING] as AIM "
                            + sCondition
                            + cond
                            + " ORDER BY IM.ITEM ";
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    Vector lineVec = new Vector();
                    lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                    lineVec.add(1, StrUtils.fString((String) rs
                                    .getString("ITEMDESC")));
                    lineVec.add(2, StrUtils.fString((String) rs
                                    .getString("ITEMTYPE")));
                    lineVec.add(3, StrUtils
                                    .fString((String) rs.getString("STKUOM")));
                    lineVec.add(4, StrUtils.fString((String) rs
                                    .getString("REMARK1")));
                    lineVec.add(5, StrUtils.fString((String) rs
                                    .getString("REMARK2")));
                    lineVec.add(6, StrUtils.fString((String) rs
                                    .getString("REMARK3")));
                    lineVec.add(7, StrUtils.fString((String) rs
                                    .getString("REMARK4")));
                    lineVec.add(8, StrUtils
                                    .fString((String) rs.getString("STKQTY")));
                    lineVec
                                    .add(9, StrUtils
                                                    .fString((String) rs.getString("ASSET")));
                    lineVec.add(10, StrUtils.fString((String) rs
                                    .getString("PRD_CLS_ID")));
                    lineVec.add(11, StrUtils.fString((String) rs
                                    .getString("ISACTIVE")));
                    lineVec.add(12, StrUtils.fString((String) rs
                                    .getString("UNITPRICE")));
                    lineVec
                                    .add(13, StrUtils
                                                    .fString((String) rs.getString("COST")));
                    lineVec.add(14, StrUtils.fString((String) rs
                                    .getString("DISCOUNT")));
                    lineVec.add(15, StrUtils.fString((String) rs
                                    .getString("USERFLD1")));
                    lineVec.add(16, StrUtils.fString((String) rs
                                    .getString("NONSTKFLAG")));
                    lineVec.add(17, StrUtils.fString((String) rs
                                    .getString("NONSTKTYPEID")));

                    listQty.add(lineVec);
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return listQty;
    }
    
    /*Bruhan changes*/ 
    public List queryItemMstDetails(String aItem, String plant) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            List listQty = new ArrayList();
            Connection con = null;
            
            try {
            //  Start code modified by Bruhan for product brand on 11/9/12 
                    con = DbBean.getConnection();
                    String sCondition = " WHERE IM.ITEM  = '"+ aItem.toUpperCase() + "'";
    				String sQry = "SELECT  IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,isnull(cast(STKQTY as decimal(18,3)),0) STKQTY,ASSET,PRD_CLS_ID,PRD_DEPT_ID,ISACTIVE,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST,isnull(MINSPRICE,'0') MINSPRICE, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1, isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID ,PRD_BRAND_ID,isnull(ITEM_LOC,'')ITEM_LOC,isnull(CATLOGPATH,'')CATLOGPATH, "
                                    +" ISNULL(cast(MAXSTKQTY as decimal(18,3)),0) MAXSTKQTY,ISNULL(VENDNO,'') VENDNO,ISNULL(LOC_ID,'') LOC_ID,"
                                    + "  ISNULL((select ISNULL(SUM(QTY),0) from " + plant + "_INVMST where ITEM=IM.ITEM  group by item),0) STOCKONHAND, "
                                    + "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYPICK),0) from [" + plant + "_DODET] D JOIN [" + plant + "_DOHDR] H ON D.DONO=H.DONO where ITEM=IM.ITEM and H.ORDER_STATUS!='Draft' and PICKSTATUS <>'C' group by item),0) OUTGOINGQTY,ISNULL(PRODGST,0) PRODGST,isnull(NETWEIGHT,0) NETWEIGHT,isnull(GROSSWEIGHT,0) GROSSWEIGHT,isnull(HSCODE,'') HSCODE,isnull(COO,'') COO,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,isnull(RENTALPRICE,'0') RENTALPRICE,isnull(SERVICEPRICE,'0') SERVICEPRICE,PURCHASEUOM,SALESUOM,RENTALUOM,SERVICEUOM,INVENTORYUOM,isnull(ISBASICUOM,'0') ISBASICUOM,isnull(ISCOMPRO,'0') ISCOMPRO,isnull(CPPI,'BYPRICE') CPPI,isnull(INCPRICE,'0') INCPRICE,isnull(INCPRICEUNIT,'%') INCPRICEUNIT,isnull(ISCHILDCAL,'0') ISCHILDCAL,isnull(ISPOSDISCOUNT,'0') ISPOSDISCOUNT,ISNULL(ISNEWARRIVAL,'0') ISNEWARRIVAL,ISNULL(ISTOPSELLING,'0') ISTOPSELLING,ISNULL(DIMENSION,'') DIMENSION, "
        		                    + "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYRC),0) from [" + plant + "_PODET] D JOIN [" + plant + "_POHDR] H ON D.PONO=H.PONO where ITEM=IM.ITEM and H.ORDER_STATUS!='Draft' and LNSTAT <>'C' group by item),0) INCOMINGQTY,"
                                    + "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYIS),0) from [" + plant + "_LOANDET] where ITEM=IM.ITEM   and PICKSTATUS <>'C' group by item),0) OUTGOINGQTYLOAN FROM"
        		                    + "["
                                    + plant
                                    + "_"
                                    + "ITEMMST "
                                    + "] as IM "
                                    + sCondition;
                                   
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Vector lineVec = new Vector();
                        lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                        lineVec.add(1, StrUtils.fString((String) rs.getString("ITEMDESC")));
                        lineVec.add(2, StrUtils.fString((String) rs.getString("ITEMTYPE")));
                        lineVec.add(3, StrUtils.fString((String) rs.getString("STKUOM")));
                        lineVec.add(4, StrUtils.fString((String) rs.getString("REMARK1")));
                        lineVec.add(5, StrUtils.fString((String) rs.getString("REMARK2")));
                        lineVec.add(6, StrUtils.fString((String) rs.getString("REMARK3")));
                        lineVec.add(7, StrUtils.fString((String) rs.getString("REMARK4")));
                        lineVec.add(8, StrUtils.fString((String) rs.getString("STKQTY")));
                        lineVec.add(9, StrUtils.fString((String) rs.getString("ASSET")));
                        lineVec.add(10, StrUtils.fString((String) rs.getString("PRD_CLS_ID")));
                        lineVec.add(11, StrUtils.fString((String) rs.getString("ISACTIVE")));
                        lineVec.add(12, StrUtils.fString((String) rs.getString("UNITPRICE")));
                        lineVec.add(13, StrUtils.fString((String) rs.getString("COST")));
                        lineVec.add(14, StrUtils.fString((String) rs.getString("MINSPRICE")));
                        lineVec.add(15, StrUtils.fString((String) rs.getString("DISCOUNT")));
                        lineVec.add(16, StrUtils.fString((String) rs.getString("USERFLD1")));
                        lineVec.add(17, StrUtils.fString((String) rs.getString("NONSTKFLAG")));
                        lineVec.add(18, StrUtils.fString((String) rs.getString("NONSTKTYPEID")));
                        lineVec.add(19, StrUtils.fString((String) rs.getString("PRD_BRAND_ID")));
                        lineVec.add(20, StrUtils.fString((String) rs.getString("ITEM_LOC")));
                        lineVec.add(21, StrUtils.fString((String) rs.getString("MAXSTKQTY")));
						lineVec.add(22, StrUtils.fString((String) rs.getString("STOCKONHAND")));
                        lineVec.add(23, StrUtils.fString((String) rs.getString("OUTGOINGQTY")));
						lineVec.add(24, StrUtils.fString((String) rs.getString("PRODGST")));
						lineVec.add(25, StrUtils.fString((String) rs.getString("NETWEIGHT")));
						lineVec.add(26, StrUtils.fString((String) rs.getString("GROSSWEIGHT")));
						lineVec.add(27, StrUtils.fString((String) rs.getString("HSCODE")));
						lineVec.add(28, StrUtils.fString((String) rs.getString("COO")));
						lineVec.add(29, StrUtils.fString((String) rs.getString("VINNO")));
						lineVec.add(30, StrUtils.fString((String) rs.getString("MODEL")));
						lineVec.add(31, StrUtils.fString((String) rs.getString("RENTALPRICE")));
						lineVec.add(32, StrUtils.fString((String) rs.getString("SERVICEPRICE")));
						lineVec.add(33, StrUtils.fString((String) rs.getString("PURCHASEUOM")));
						lineVec.add(34, StrUtils.fString((String) rs.getString("SALESUOM")));
						lineVec.add(35, StrUtils.fString((String) rs.getString("RENTALUOM")));
						lineVec.add(36, StrUtils.fString((String) rs.getString("SERVICEUOM")));
						lineVec.add(37, StrUtils.fString((String) rs.getString("INVENTORYUOM")));
						lineVec.add(38, StrUtils.fString((String) rs.getString("ISBASICUOM")));
						lineVec.add(39, StrUtils.fString((String) rs.getString("OUTGOINGQTYLOAN")));
						lineVec.add(40, StrUtils.fString((String) rs.getString("CATLOGPATH")));
						lineVec.add(41, StrUtils.fString((String) rs.getString("INCOMINGQTY")));
						lineVec.add(42, StrUtils.fString((String) rs.getString("ISCOMPRO")));
						lineVec.add(43, StrUtils.fString((String) rs.getString("CPPI")));
						lineVec.add(44, StrUtils.fString((String) rs.getString("INCPRICE")));
						lineVec.add(45, StrUtils.fString((String) rs.getString("INCPRICEUNIT")));
						lineVec.add(46, StrUtils.fString((String) rs.getString("PRD_DEPT_ID")));
						lineVec.add(47, StrUtils.fString((String) rs.getString("VENDNO")));
						lineVec.add(48, StrUtils.fString((String) rs.getString("ISCHILDCAL")));
						lineVec.add(49, StrUtils.fString((String) rs.getString("ISPOSDISCOUNT")));
						lineVec.add(50, StrUtils.fString((String) rs.getString("ISNEWARRIVAL")));
						lineVec.add(51, StrUtils.fString((String) rs.getString("ISTOPSELLING")));
						lineVec.add(52, StrUtils.fString((String) rs.getString("DIMENSION")));
						lineVec.add(53, StrUtils.fString((String) rs.getString("LOC_ID")));
                        listQty.add(lineVec);
                    //  End code modified by Bruhan for product brand on 11/9/12 
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return listQty;
    }
    
    public List queryItemMstDetailsforpurchase(String aItem, String plant) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            List listQty = new ArrayList();
            Connection con = null;
            
            try {
            //  Start code modified by Bruhan for product brand on 11/9/12 
                    con = DbBean.getConnection();
                    String sCondition = " WHERE IM.ITEM  = '"+ aItem.toUpperCase() + "'";
    				String sQry = "SELECT  IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,isnull(cast(STKQTY as decimal(18,3)),0) STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST,isnull(MINSPRICE,'0') MINSPRICE, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1, isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID ,PRD_BRAND_ID,isnull(ITEM_LOC,'')ITEM_LOC,isnull(CATLOGPATH,'')CATLOGPATH, "
                                    +" ISNULL(cast(MAXSTKQTY as decimal(18,3)),0) MAXSTKQTY,"
                                    + "  ISNULL((select ISNULL(SUM(QTY),0) from " + plant + "_INVMST where ITEM=IM.ITEM  group by item),0) STOCKONHAND, "
                                    + "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYPICK),0) from [" + plant + "_DODET] D JOIN [" + plant + "_DOHDR] H ON D.DONO=H.DONO where ITEM=IM.ITEM and H.ORDER_STATUS!='Draft' and PICKSTATUS <>'C' group by item),0) OUTGOINGQTY,ISNULL(PRODGST,0) PRODGST,isnull(NETWEIGHT,0) NETWEIGHT,isnull(GROSSWEIGHT,0) GROSSWEIGHT,isnull(HSCODE,'') HSCODE,isnull(COO,'') COO,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,isnull(RENTALPRICE,'0') RENTALPRICE,isnull(SERVICEPRICE,'0') SERVICEPRICE,PURCHASEUOM,SALESUOM,RENTALUOM,SERVICEUOM,INVENTORYUOM,isnull(ISBASICUOM,'0') ISBASICUOM,isnull(ISPOSDISCOUNT,'0') ISPOSDISCOUNT, "
        		                    + "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYRC),0) from [" + plant + "_PODET] D JOIN [" + plant + "_POHDR] H ON D.PONO=H.PONO where ITEM=IM.ITEM and H.ORDER_STATUS!='Draft' and LNSTAT <>'C' group by item),0) INCOMINGQTY,"
                                    + "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYIS),0) from [" + plant + "_LOANDET] where ITEM=IM.ITEM   and PICKSTATUS <>'C' group by item),0) OUTGOINGQTYLOAN FROM"
        		                    + "["
                                    + plant
                                    + "_"
                                    + "ITEMMST "
                                    + "] as IM "
                                    + sCondition;
                                   
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Vector lineVec = new Vector();
                        lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                        lineVec.add(1, StrUtils.fString((String) rs.getString("ITEMDESC")));
                        lineVec.add(2, StrUtils.fString((String) rs.getString("ITEMTYPE")));
                        lineVec.add(3, StrUtils.fString((String) rs.getString("STKUOM")));
                        lineVec.add(4, StrUtils.fString((String) rs.getString("REMARK1")));
                        lineVec.add(5, StrUtils.fString((String) rs.getString("REMARK2")));
                        lineVec.add(6, StrUtils.fString((String) rs.getString("REMARK3")));
                        lineVec.add(7, StrUtils.fString((String) rs.getString("REMARK4")));
                        lineVec.add(8, StrUtils.fString((String) rs.getString("STKQTY")));
                        lineVec.add(9, StrUtils.fString((String) rs.getString("ASSET")));
                        lineVec.add(10, StrUtils.fString((String) rs.getString("PRD_CLS_ID")));
                        lineVec.add(11, StrUtils.fString((String) rs.getString("ISACTIVE")));
                        lineVec.add(12, StrUtils.fString((String) rs.getString("UNITPRICE")));
                        lineVec.add(13, StrUtils.fString((String) rs.getString("COST")));
                        lineVec.add(14, StrUtils.fString((String) rs.getString("MINSPRICE")));
                        lineVec.add(15, StrUtils.fString((String) rs.getString("DISCOUNT")));
                        lineVec.add(16, StrUtils.fString((String) rs.getString("USERFLD1")));
                        lineVec.add(17, StrUtils.fString((String) rs.getString("NONSTKFLAG")));
                        lineVec.add(18, StrUtils.fString((String) rs.getString("NONSTKTYPEID")));
                        lineVec.add(19, StrUtils.fString((String) rs.getString("PRD_BRAND_ID")));
                        lineVec.add(20, StrUtils.fString((String) rs.getString("ITEM_LOC")));
                        lineVec.add(21, StrUtils.fString((String) rs.getString("MAXSTKQTY")));
						lineVec.add(22, StrUtils.fString((String) rs.getString("STOCKONHAND")));
                        lineVec.add(23, StrUtils.fString((String) rs.getString("OUTGOINGQTY")));
						lineVec.add(24, StrUtils.fString((String) rs.getString("PRODGST")));
						lineVec.add(25, StrUtils.fString((String) rs.getString("NETWEIGHT")));
						lineVec.add(26, StrUtils.fString((String) rs.getString("GROSSWEIGHT")));
						lineVec.add(27, StrUtils.fString((String) rs.getString("HSCODE")));
						lineVec.add(28, StrUtils.fString((String) rs.getString("COO")));
						lineVec.add(29, StrUtils.fString((String) rs.getString("VINNO")));
						lineVec.add(30, StrUtils.fString((String) rs.getString("MODEL")));
						lineVec.add(31, StrUtils.fString((String) rs.getString("RENTALPRICE")));
						lineVec.add(32, StrUtils.fString((String) rs.getString("SERVICEPRICE")));
						lineVec.add(33, StrUtils.fString((String) rs.getString("PURCHASEUOM")));
						lineVec.add(34, StrUtils.fString((String) rs.getString("SALESUOM")));
						lineVec.add(35, StrUtils.fString((String) rs.getString("RENTALUOM")));
						lineVec.add(36, StrUtils.fString((String) rs.getString("SERVICEUOM")));
						lineVec.add(37, StrUtils.fString((String) rs.getString("INVENTORYUOM")));
						lineVec.add(38, StrUtils.fString((String) rs.getString("ISBASICUOM")));
						lineVec.add(39, StrUtils.fString((String) rs.getString("OUTGOINGQTYLOAN")));
						lineVec.add(40, StrUtils.fString((String) rs.getString("CATLOGPATH")));
						lineVec.add(41, StrUtils.fString((String) rs.getString("INCOMINGQTY")));
						lineVec.add(42, StrUtils.fString((String) rs.getString("ISPOSDISCOUNT")));
                        listQty.add(lineVec);
                    //  End code modified by Bruhan for product brand on 11/9/12 
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return listQty;
    }

	/**
	 * method : updateItemMst(Hashtable htUpdate,Hashtable htCondition)
	 * description : update the existing record from Itementory master(ITEMMST)
	 * 
	 * @param : Hashtable ht
	 * @return : boolean - true / false
	 * @throws Exception
	 */
	public boolean updateItemMst(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean updateItemmst = false;
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
			String sQry = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ tblName + "]" + " SET " + sUpdate + " WHERE "
					+ sCondition;

			this.mLogger.query(this.printQuery, sQry);

			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				updateItemmst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return updateItemmst;
	}

	/**
	 * method : getCountItemmst(Hashtable ht) description : get the count of
	 * records in Itemmst for the given condition
	 * 
	 * @param : Hashtable ht
	 * @return : int - count
	 * @throws Exception
	 */
	public int getCountItemMst(Hashtable ht) throws Exception {
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
			String sQry = "SELECT COUNT(" + IConstants.ITEM + ") FROM " + "["
					+ ht.get("PLANT") + "_" + tblName + "]" + " WHERE "
					+ sCondition;
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

	/**
	 * @method : getItemDetails(String aItem)
	 * @description : get the item details for the given item
	 * @param aItem
	 * @return
	 */
	public ArrayList getItemDetails(String aItem) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sql = "SELECT ITEM,ITEMDESC,STKUOM,USERFLD1,STKQTY,ASSET,USERFLD1 FROM ITEMMST WHERE ITEM = '"
					+ aItem.toUpperCase() + "'";
			this.mLogger.query(this.printQuery, sql);
			ps = con.prepareCall(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				arrList.add(0, StrUtils.fString((String) rs.getString("ITEM")));
				arrList.add(1, StrUtils.fString((String) rs.getString(
						"ITEMDESC").trim()));
				arrList.add(2, StrUtils
						.fString((String) rs.getString("STKUOM")));
				arrList.add(3, StrUtils.fString((String) rs
						.getString("USERFLD1")));
				arrList.add(4, StrUtils
						.fString((String) rs.getString("STKQTY")));
				arrList
						.add(3, StrUtils
								.fString((String) rs.getString("ASSET")));
				
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
	}

	/**
	 * @method : queryItemMst(String aItem)
	 * @description : get the record details for the given item
	 * @param aItem
	 * @return list
	 */
	public List queryItemMst(String aItem) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = StrUtils.fString(aItem).length() > 0 ? sCondition = "WHERE ITEM LIKE  '"
					+ aItem.toUpperCase() + "%'"
					: "";
			String sQry = "SELECT ITEM,ITEMDESC,ARTIST,TITLE,MEDIUM,REMARK1,ITEMCONDITION,STATUS,ISACTIVE FROM ITEMMST "
					+ sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, StrUtils.fString((String) rs
						.getString("ITEMDESC")));
				lineVec.add(2, StrUtils
						.fString((String) rs.getString("ARTIST")));
				lineVec
						.add(3, StrUtils
								.fString((String) rs.getString("TITLE")));
				lineVec.add(4, StrUtils
						.fString((String) rs.getString("MEDIUM")));
				lineVec.add(5, StrUtils.fString((String) rs
						.getString("REMARK1")));
				lineVec.add(6, StrUtils.fString((String) rs
						.getString("ITEMCONDITION")));
				lineVec.add(7, StrUtils
						.fString((String) rs.getString("STATUS")));
				lineVec.add(8, StrUtils.fString((String) rs
						.getString("ISACTIVE")));
				listQty.add(lineVec);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}

	public List queryItemMst(List listQryFields, Hashtable ht) throws Exception {
//		boolean deleteItemmst = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sCondition = "";
		String sQryFields = "";
		List listQryResult = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			// generate the condition string
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key + " = '" + value + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
			// get qry fields
			for (int i = 0; i < listQryFields.size(); i++) {
				sQryFields = StrUtils.fString((String) listQryFields.get(i))
						+ ",";
			}
			sQryFields = (sQryFields.length() > 0) ? sQryFields.substring(0,
					sQryFields.length() - 1) : "";
			String sQry = "SELECT " + sQryFields + " FROM " + tblName
					+ " WHERE " + sCondition;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector vecLine = new Vector();
				for (int i = 0; i < listQryFields.size(); i++) {
					String fieldName = (String) listQryFields.get(i);
					fieldName = rs.getString(fieldName);
					vecLine.add(i, fieldName);
				}
				listQryResult.add(vecLine);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQryResult;
	}
	public List queryAlternateItemMstForSearchCriteria(String aItem, String aItemDesc, String aPrd_Cls_id, String aPrd_Tyep,String aPrdBrand,String aprddept,
            String plant, String cond) {
    PreparedStatement ps = null;
//    ResultSet rs = null;
    List listQty = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
          String  sCondition ="";
                //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
             sCondition = sCondition +  " WHERE ItemTab.ITEM=AlterTab.ITEM And REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND ItemTab.ITEM LIKE  '"
                            + aItem.toUpperCase() + "%'";
        if(aPrd_Cls_id.length()>0){
            sCondition= sCondition +" AND PRD_CLS_ID= '"+aPrd_Cls_id+"' ";    
        }
        if(aprddept.length()>0){
        	sCondition= sCondition +" AND PRD_DEPT_ID= '"+aprddept+"' ";    
        }
        if(aPrd_Tyep.length()>0){
            sCondition= sCondition +" AND ITEMTYPE= '"+aPrd_Tyep+"' ";    
        }
       
        if(aPrdBrand.length()>0){
            sCondition= sCondition +" AND PRD_BRAND_ID= '"+aPrdBrand+"' ";    
        }
     
            String sQry = "SELECT ItemTab.ITEM,ITEMDESC,ALTERNATE_ITEM_NAME[ALTERNATEITEM] FROM "
            + plant + "_" + "ITEMMST ItemTab," + "[" + plant + "_" + "ALTERNATE_ITEM_MAPPING" + "] AlterTab " +sCondition+" "  + cond;
            this.mLogger.query(this.printQuery, sQry);
            listQty = selectData(con, sQry);	
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return listQty;
}
	  public List queryItemMstForSearchCriteria(String aItem, String aItemDesc, String aPrd_Cls_id, String aPrd_Tyep,String aPrdBrand,String aprddept,String astocktype,
            String plant, String cond) {
    PreparedStatement ps = null;
//    ResultSet rs = null;
    List listQty = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
          String  sCondition ="";
                //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
             sCondition = sCondition +  " WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND ITEM LIKE  '"
                            + aItem.toUpperCase() + "%'";
        if(aPrd_Cls_id.length()>0){
            sCondition= sCondition +" AND PRD_CLS_ID= '"+aPrd_Cls_id+"' ";    
        }
        if(aPrd_Tyep.length()>0){
            sCondition= sCondition +" AND ITEMTYPE= '"+aPrd_Tyep+"' ";    
        }
       
        if(aPrdBrand.length()>0){
            sCondition= sCondition +" AND PRD_BRAND_ID= '"+aPrdBrand+"' ";    
        }
        if(aprddept.length()>0){
            sCondition= sCondition +" AND PRD_DEPT_ID= '"+aprddept+"' ";    
        }
        
        
        if(astocktype.length()>0){
        	sCondition= sCondition +" AND NONSTKFLAG= '"+astocktype+"' ";    
        }
     
            String sQry = "SELECT ITEM,ITEMDESC,ITEMTYPE," + 
            "STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,PRD_BRAND_ID,isnull(PRD_DEPT_ID,'') PRD_DEPT_ID,isnull(VENDNO,'') VENDNO," + 
            "ISACTIVE,UNITPRICE,isnull(MINSPRICE,0) as MINSPRICE,ISNULL(COST,0) COST,ISNULL(DISCOUNT,0)DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL," + 
            "ISNULL(NONSTKFLAG,'N') NONSTKFLAG,NONSTKTYPEID,isnull(ITEM_LOC,'')ITEM_LOC,ISNULL(MAXSTKQTY,0) MAXSTKQTY,ISNULL(PRODGST,0)PRODGST,ISNULL(NETWEIGHT,0)NETWEIGHT,ISNULL(GROSSWEIGHT,0)GROSSWEIGHT,isnull(HSCODE,'')HSCODE,isnull(COO,'')COO,isnull(SALESUOM,'') SALESUOM,isnull(PURCHASEUOM,'') PURCHASEUOM,ISNULL(RENTALUOM,'') RENTALUOM,isnull(RENTALPRICE,0) RENTALPRICE,ISNULL(INVENTORYUOM,'') INVENTORYUOM,ISNULL(ISBASICUOM,'') ISBASICUOM,ISNULL(DIMENSION,'') DIMENSION FROM "
             + "[" + plant + "_" + "ITEMMST " + "] "+sCondition+" "  + cond;
            this.mLogger.query(this.printQuery, sQry);
            listQty = selectData(con, sQry);	
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    return listQty;
}
	  
	  public List queryAlternateItemList(String aItem, String aItemDesc, String aPrd_Cls_id, String aPrd_Tyep,String aPrdBrand,String aprddept,
	            String plant, String cond) {
	    PreparedStatement ps = null;
//	    ResultSet rs = null;
	    List listQty = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	          String  sCondition ="";
	                //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	             sCondition = sCondition +  " SELECT ITEM FROM "+  "[" + plant + "_" + "ITEMMST " + "]  WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND ITEM LIKE  '"
	                            + aItem.toUpperCase() + "%'";
	        if(aPrd_Cls_id.length()>0){
	            sCondition= sCondition +" AND PRD_CLS_ID= '"+aPrd_Cls_id+"' ";    
	        }
	        if(aprddept.length()>0){
	        	sCondition= sCondition +" AND PRD_DEPT_ID= '"+aprddept+"' ";    
	        }
	        if(aPrd_Tyep.length()>0){
	            sCondition= sCondition +" AND ITEMTYPE= '"+aPrd_Tyep+"' ";    
	        }
	       
	        if(aPrdBrand.length()>0){
	            sCondition= sCondition +" AND PRD_BRAND_ID= '"+aPrdBrand+"' ";    
	        }
	     
	            String sQry = "SELECT ITEM,ALTERNATE_ITEM_NAME FROM "
	             + "[" + plant + "_" + "ALTERNATE_ITEM_MAPPING " + "]  WHERE ITEM IN ( "+sCondition+" )  AND ITEM <> ALTERNATE_ITEM_NAME "  + cond;
	            this.mLogger.query(this.printQuery, sQry);
	            listQty = selectData(con, sQry);	
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return listQty;
	}
	
	public List queryItemMstForSearchCriteria(String aItem, String aItemDesc, String aPrd_Cls_id, String aPrd_Tyep,
			String plant, String cond) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
			String sCondition = "WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND ITEM LIKE  '"
					+ aItem.toUpperCase() + "%'";
                    if(aPrd_Cls_id.length()>0){
                        sCondition= sCondition +" AND PRD_CLS_ID= '"+aPrd_Cls_id+"' ";    
                    }
		    if(aPrd_Tyep.length()>0){
		        sCondition= sCondition +" AND ITEMTYPE= '"+aPrd_Tyep+"' ";    
		    }
			String sQry = "SELECT ITEM,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,UNITPRICE,COST,ISNULL(USERFLD1,'N')USERFLD1,ISNULL(NONSTKFLAG,'N') NONSTKFLAG,NONSTKTYPEID FROM "
					+ "[" + plant + "_" + "ITEMMST " + "]" + sCondition + cond;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, StrUtils.fString((String) rs
						.getString("ITEMDESC")));
				lineVec.add(2, StrUtils.fString((String) rs
						.getString("ITEMTYPE")));
				lineVec.add(3, StrUtils
						.fString((String) rs.getString("STKUOM")));
				lineVec.add(4, StrUtils.fString((String) rs
						.getString("REMARK1")));
				lineVec.add(5, StrUtils.fString((String) rs
						.getString("REMARK2")));
				lineVec.add(6, StrUtils.fString((String) rs
						.getString("REMARK3")));
				lineVec.add(7, StrUtils.fString((String) rs
						.getString("REMARK4")));
				lineVec.add(8, StrUtils
						.fString((String) rs.getString("STKQTY")));
				lineVec
						.add(9, StrUtils
								.fString((String) rs.getString("ASSET")));
				lineVec.add(10, StrUtils.fString((String) rs
						.getString("PRD_CLS_ID")));
				lineVec.add(11, StrUtils.fString((String) rs
						.getString("ISACTIVE")));
				lineVec.add(12, StrUtils.fString((String) rs
						.getString("UNITPRICE")));
				lineVec
						.add(13, StrUtils
								.fString((String) rs.getString("COST")));
				lineVec
				.add(14, StrUtils
						.fString((String) rs.getString("USERFLD1")));
				lineVec
				.add(15, StrUtils
						.fString((String) rs.getString("NONSTKFLAG")));
				lineVec
				.add(16, StrUtils
						.fString((String) rs.getString("NONSTKTYPEID")));
				
				listQty.add(lineVec);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}
        
    public long getProductCount(String aItem, String aItemDesc, String aPrd_Cls_id, String aPrd_Tyep,String aPrdBrand,String aPrdDept,
                    String plant, String cond)
     {
       long RecCount =0;
        PreparedStatement ps = null;
        ResultSet rs = null;
//        List listQty = new ArrayList();
        Connection con = null;
        try {
                con = DbBean.getConnection();
              //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC    
              String   sCondition =   " WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND ITEM LIKE  '"
                                + aItem.toUpperCase() + "%'";
            if(aPrd_Cls_id.length()>0){
                sCondition= sCondition +" AND PRD_CLS_ID= '"+aPrd_Cls_id+"' ";    
            }
            if(aPrd_Tyep.length()>0){
                sCondition= sCondition +" AND ITEMTYPE= '"+aPrd_Tyep+"' ";    
            }
           //  Start code modified by Bruhan for product brand on 11/9/12 
            if(aPrdBrand.length()>0){
                sCondition= sCondition +" AND PRD_BRAND_ID= '"+aPrdBrand+"' ";    
            }
            if(aPrdDept.length()>0){
                sCondition= sCondition +" AND PRD_DEPT_ID= '"+aPrdDept+"' ";    
            }
         
                String sQry = "SELECT COUNT(*) FROM " + 
                "(SELECT (ROW_NUMBER() OVER ( ORDER BY ITEM)) AS ID,ITEM,ITEMDESC,ITEMTYPE," + 
                "STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,PRD_BRAND_ID,PRD_DEPT_ID," + 
                "ISACTIVE,UNITPRICE,isnull(MINSPRICE,0) as MINSPRICE,COST,ISNULL(USERFLD1,'N')USERFLD1," + 
                "ISNULL(NONSTKFLAG,'N') NONSTKFLAG,NONSTKTYPEID FROM "
                 + "[" + plant + "_" + "ITEMMST " + "] )A" + sCondition + cond;
                this.mLogger.query(this.printQuery, sQry);
                ps = con.prepareStatement(sQry);
                rs = ps.executeQuery();
                 while (rs.next()) {
                        RecCount= rs.getInt(1);
                      }
             //  End code modified by Bruhan for product brand on 11/9/12 
      }catch(Exception e){MLogger.log(0,"Exception :repportUtil :: getStockTakeDetails:"+e.toString() );}
        finally {
                            DbBean.closeConnection(con, ps);
                    }
      return RecCount;
    }
 public List queryAlternetItemMstForSearchCriteriaNew(String aItem, String aItemDesc, String aPrd_Cls_id, String aPrd_Tyep,String aPrdBrand,String aPrdDept,
            String plant, String cond,int start,int end) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    List listQty = new ArrayList();
    Connection con = null;
    try {
            con = DbBean.getConnection();
          String  sCondition ="";
            String  sQryCondition = " WHERE ID >="+start+" and ID<="+end+" ";
          //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
             sCondition = sCondition +  " WHERE ItemTab.ITEM=AlterTab.ITEM And REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND ItemTab.ITEM LIKE  '"
                            + aItem.toUpperCase() + "%'";
        if(aPrd_Cls_id.length()>0){
            sCondition= sCondition +" AND PRD_CLS_ID= '"+aPrd_Cls_id+"' ";    
        }
        if(aPrd_Tyep.length()>0){
            sCondition= sCondition +" AND ITEMTYPE= '"+aPrd_Tyep+"' ";    
        }
       
        if(aPrdBrand.length()>0){
            sCondition= sCondition +" AND PRD_BRAND_ID= '"+aPrdBrand+"' ";    
        }
        if(aPrdDept.length()>0){
            sCondition= sCondition +" AND PRD_DEPT_ID= '"+aPrdDept+"' ";    
        }
			if(aItem.length()>0){
            sCondition= sCondition +" AND ItemTab.ITEM= '"+aItem+"' ";    
        }
     
     
            String sQry = "SELECT * FROM " + 
            "(SELECT (ROW_NUMBER() OVER ( ORDER BY ItemTab.ITEM)) AS ID,ItemTab.ITEM,ITEMDESC,ALTERNATE_ITEM_NAME [ALTERNATEITEM],ITEMTYPE," + 
            "STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,PRD_BRAND_ID,PRD_DEPT_ID," + 
            "ISACTIVE,UNITPRICE,isnull(MINSPRICE,0) as MINSPRICE,COST,ISNULL(USERFLD1,'N')USERFLD1," + 
            "ISNULL(NONSTKFLAG,'N') NONSTKFLAG,NONSTKTYPEID,isnull(ITEM_LOC,'')ITEM_LOC,ISNULL(MAXSTKQTY,0) MAXSTKQTY,ISNULL(DISCOUNT,0.0)DISCOUNT,ProdGST,ISNULL(PRINTSTATUS,'') PRINTSTATUS  FROM "
             + plant + "_ITEMMST ItemTab," + plant + "_ALTERNATE_ITEM_MAPPING AlterTab "+ sCondition+" )A" +sQryCondition + cond;
            this.mLogger.query(this.printQuery, sQry);
            ps = con.prepareStatement(sQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                    Vector lineVec = new Vector();
                        lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                        lineVec.add(1, StrUtils.fString((String) rs.getString("ITEMDESC")));
                        lineVec.add(2, StrUtils.fString((String) rs.getString("ALTERNATEITEM")));                       
                        lineVec.add(3, StrUtils.fString((String) rs.getString("ID")));
                       
						
                     listQty.add(lineVec);
            }
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    } finally {
            DbBean.closeConnection(con, ps);
    }
    this.mLogger.query(this.printLog, "listQty :" + listQty.size());
    return listQty;
}
 
 //imthi 29-06-2022
 public ArrayList getitemWithCust(String aCustName,
			String plant,String aCustType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;
		String extCond="";
		try {
			con = DbBean.getConnection();
				String sQry = "SELECT ITEM,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,UNITPRICE,COST,ISNULL(USERFLD1,'N')USERFLD1,ISNULL(NONSTKFLAG,'N') NONSTKFLAG,NONSTKTYPEID FROM "
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST"
					+ "]";
			
			this.mLogger.query(this.printQuery, sQry);
			
			arrList = selectForReport(sQry, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
 
//  Start code modifed by Bruhan for product brand on 11/9/12    
    public List queryItemMstForSearchCriteriaNew(String aItem, String aItemDesc, String aPrd_Cls_id, String aPrd_Tyep,String aPrdBrand,String aPrdDept,String astocktype,
                    String plant, String cond,int start,int end) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            List listQty = new ArrayList();
            Connection con = null;
            try {
                    con = DbBean.getConnection();
                  String  sCondition ="";
                    //String  sQryCondition = " WHERE ID >="+start+" and ID<="+end+" ";
                    String  sQryCondition = " WHERE PLANT ='"+plant+"' ";
                  //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
                     sCondition = sCondition +  " WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(aItemDesc.replaceAll(" ",""))+"%' AND ITEM LIKE  '"
                                    + aItem.toUpperCase() + "%'";
                if(aPrd_Cls_id.length()>0){
                    sCondition= sCondition +" AND PRD_CLS_ID= '"+aPrd_Cls_id+"' ";    
                }
                if(aPrd_Tyep.length()>0){
                    sCondition= sCondition +" AND ITEMTYPE= '"+aPrd_Tyep+"' ";    
                }
               
                if(aPrdBrand.length()>0){
                    sCondition= sCondition +" AND PRD_BRAND_ID= '"+aPrdBrand+"' ";    
                }
                if(aPrdDept.length()>0){
                    sCondition= sCondition +" AND PRD_DEPT_ID= '"+aPrdDept+"' ";    
                }
 				if(aItem.length()>0){
                    sCondition= sCondition +" AND ITEM= '"+aItem+"' ";    
                }
 				if(astocktype.length()>0){
 					sCondition= sCondition +" AND NONSTKFLAG= '"+astocktype+"' ";    
 				}
             
             
                    String sQry = "SELECT * FROM " + 
                    "(SELECT (ROW_NUMBER() OVER ( ORDER BY ITEM)) AS ID,ITEM,ITEMDESC,ITEMTYPE," + 
                    "STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,PRD_BRAND_ID,PRD_DEPT_ID," + 
                    "ISACTIVE,UNITPRICE,isnull(MINSPRICE,0) as MINSPRICE,COST,ISNULL(USERFLD1,'N')USERFLD1,ISNULL(NETWEIGHT,'0')NETWEIGHT,ISNULL(GROSSWEIGHT,'0')GROSSWEIGHT,ISNULL(HSCODE,'')HSCODE,ISNULL(COO,'')COO," + 
                    "ISNULL(NONSTKFLAG,'N') NONSTKFLAG,NONSTKTYPEID,isnull(ITEM_LOC,'')ITEM_LOC,ISNULL(MAXSTKQTY,0) MAXSTKQTY,ISNULL(DISCOUNT,0.0)DISCOUNT,ISNULL(PRINTSTATUS,'') PRINTSTATUS,"+
                    "ISNULL(PRODGST,0)PRODGST,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,isnull(RENTALPRICE,'0') RENTALPRICE,isnull(SERVICEPRICE,'0') SERVICEPRICE,PURCHASEUOM,SALESUOM,RENTALUOM,SERVICEUOM,INVENTORYUOM,ISNULL(ISBASICUOM,'0') ISBASICUOM, "
                    + "ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOG,PLANT FROM [" + plant + "_" + "ITEMMST " + "] "+sCondition+" )A" +sQryCondition + cond;
                    this.mLogger.query(this.printQuery, sQry);
                    ps = con.prepareStatement(sQry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                            Vector lineVec = new Vector();
                                lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
                                lineVec.add(1, StrUtils.fString((String) rs.getString("ITEMDESC")));
                                lineVec.add(2, StrUtils.fString((String) rs.getString("ITEMTYPE")));
                                lineVec.add(3, StrUtils.fString((String) rs.getString("STKUOM")));
                                lineVec.add(4, StrUtils.fString((String) rs.getString("REMARK1")));
                                lineVec.add(5, StrUtils.fString((String) rs.getString("REMARK2")));
                                lineVec.add(6, StrUtils.fString((String) rs.getString("REMARK3")));
                                lineVec.add(7, StrUtils.fString((String) rs.getString("REMARK4")));
                                lineVec.add(8, StrUtils.fString((String) rs.getString("STKQTY")));
                                lineVec.add(9, StrUtils.fString((String) rs.getString("ASSET")));
                                lineVec.add(10, StrUtils.fString((String) rs.getString("PRD_CLS_ID")));
                                lineVec.add(11, StrUtils.fString((String) rs.getString("ISACTIVE")));
                                lineVec.add(12, StrUtils.fString((String) rs.getString("UNITPRICE")));
                                lineVec.add(13, StrUtils.fString((String) rs.getString("COST")));
                                lineVec.add(14, StrUtils.fString((String) rs.getString("USERFLD1")));
                                lineVec.add(15, StrUtils.fString((String) rs.getString("NONSTKFLAG")));
                                lineVec.add(16, StrUtils.fString((String) rs.getString("NONSTKTYPEID")));
                                lineVec.add(17, StrUtils.fString((String) rs.getString("ID")));
                                lineVec.add(18, StrUtils.fString((String) rs.getString("MINSPRICE")));
                                lineVec.add(19, StrUtils.fString((String) rs.getString("PRD_BRAND_ID")));
                                lineVec.add(20, StrUtils.fString((String) rs.getString("ITEM_LOC")));
								lineVec.add(21, StrUtils.fString((String) rs.getString("MAXSTKQTY")));
								lineVec.add(22, StrUtils.fString((String) rs.getString("DISCOUNT")));
								lineVec.add(23, StrUtils.fString((String) rs.getString("PRINTSTATUS")));
								lineVec.add(24, StrUtils.fString((String) rs.getString("PRODGST")));
								lineVec.add(25, StrUtils.fString((String) rs.getString("NETWEIGHT")));
								lineVec.add(26, StrUtils.fString((String) rs.getString("GROSSWEIGHT")));
								lineVec.add(27, StrUtils.fString((String) rs.getString("HSCODE")));
								lineVec.add(28, StrUtils.fString((String) rs.getString("COO")));
								lineVec.add(29, StrUtils.fString((String) rs.getString("VINNO")));
								lineVec.add(30, StrUtils.fString((String) rs.getString("MODEL")));
								lineVec.add(31, StrUtils.fString((String) rs.getString("RENTALPRICE")));
								lineVec.add(32, StrUtils.fString((String) rs.getString("SERVICEPRICE")));
								lineVec.add(33, StrUtils.fString((String) rs.getString("PURCHASEUOM")));
								lineVec.add(34, StrUtils.fString((String) rs.getString("SALESUOM")));
								lineVec.add(35, StrUtils.fString((String) rs.getString("RENTALUOM")));
								lineVec.add(36, StrUtils.fString((String) rs.getString("SERVICEUOM")));
								lineVec.add(37, StrUtils.fString((String) rs.getString("INVENTORYUOM")));
								lineVec.add(38, StrUtils.fString((String) rs.getString("ISBASICUOM")));
								lineVec.add(39, StrUtils.fString((String) rs.getString("CATALOG")));
								lineVec.add(40, StrUtils.fString((String) rs.getString("PRD_DEPT_ID")));
								listQty.add(lineVec);
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            } finally {
                    DbBean.closeConnection(con, ps);
            }
            return listQty;
    }
//  End code added by Bruhan for product brand on 11/9/12 
	// retrieve distinct products
	public List queryProducts(String plant, String cond) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List listQty = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sCondition = "";
			// sQry is changed by Bruhan for getting non stock flag
			String sQry = "SELECT distinct ITEM,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE, isnull(UNITPRICE,0) as UNITPRICE,isnull(DISCOUNT,0) as DISCOUNT,isnull(MINSPRICE,0) as MINSPRICE,isnull(NONSTKFLAG,'N') NONSTKFLAG,ISNULL(PRODGST,'') PRODGST FROM "
					+ "[" + plant + "_" + "ITEMMST " + "]" + sCondition + cond;
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Vector lineVec = new Vector();
				lineVec.add(0, StrUtils.fString((String) rs.getString("ITEM")));
				lineVec.add(1, StrUtils.fString((String) rs
						.getString("ITEMDESC")));
				lineVec.add(2, StrUtils.fString((String) rs
						.getString("ITEMTYPE")));
				lineVec.add(3, StrUtils
						.fString((String) rs.getString("STKUOM")));
				lineVec.add(4, StrUtils.fString((String) rs
						.getString("REMARK1")));
				lineVec.add(5, StrUtils.fString((String) rs
						.getString("REMARK2")));
				lineVec.add(6, StrUtils.fString((String) rs
						.getString("REMARK3")));
				lineVec.add(7, StrUtils.fString((String) rs
						.getString("REMARK4")));
				lineVec.add(8, StrUtils
						.fString((String) rs.getString("STKQTY")));// Qty
				lineVec
						.add(9, StrUtils
								.fString((String) rs.getString("ASSET")));
				lineVec.add(10, StrUtils.fString((String) rs
						.getString("PRD_CLS_ID")));
				lineVec.add(11, StrUtils.fString((String) rs
						.getString("ISACTIVE")));
				lineVec.add(12, StrUtils.fString((String) rs
						.getString("UNITPRICE"))); // Price
				lineVec.add(13, StrUtils.fString((String) rs
						.getString("DISCOUNT"))); // Discount
				lineVec.add(14, StrUtils.fString((String) rs
						.getString("MINSPRICE"))); // Minimum selling price
				// Added by Bruhan for nonstock flag
				lineVec.add(15, StrUtils.fString((String) rs
						.getString("NONSTKFLAG"))); 
lineVec.add(16, StrUtils.fString((String) rs
						.getString("PRODGST"))); // Discount


//Ended
				listQty.add(lineVec);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;
	}

	public ArrayList getStockReorderItemList() throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList listQty = new ArrayList();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			String sQry = " SELECT A.ITEM,A.ITEMDESC,A.USERFLD1,SUM(B.QTY) FROM ITEMMST AS A, INVMST AS B WHERE A.ITEM = B.ITEM AND  A.USERFLD1 >= 1 "
					+ " AND  A.USERFLD1 >= (SELECT SUM(B.QTY) FROM INVMST AS B WHERE A.ITEM = B.ITEM) GROUP BY A.ITEM,A.ITEMDESC,A.USERFLD1";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList arrLine = new ArrayList();
				arrLine.add(0, StrUtils.fString((String) rs.getString(1)));
				arrLine.add(1, StrUtils.fString((String) rs.getString(2)));
				arrLine.add(2, StrUtils.fString((String) rs.getString(3)));
				arrLine.add(3, StrUtils.fString((String) rs.getString(4)));
				listQty.add(arrLine);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return listQty;

	}
	
	public ResultSetMetaData  getTableMetaData(Hashtable ht) throws Exception {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		ResultSetMetaData md = null;
		
		try {
			con = DbBean.getConnection();

			String sQry = "SELECT top 0 * FROM [" + ht.get(IConstants.PLANT) + "_" + ht.get(IConstants.TABLE_NAME) + "]";
			
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			md = rs.getMetaData();

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return md;
	}
	public boolean insertIntoItemdetDesc(Hashtable ht) throws Exception {
		boolean insertedItemmst = false;
		PreparedStatement ps = null;
		Connection con = null;
		String TABLE="ITEMDET_DESC";
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
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedItemmst = true;
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		
		return insertedItemmst;
	}
	public boolean insertIntoAdditionalItem(Hashtable ht) throws Exception {
		boolean insertedItemmst = false;
		PreparedStatement ps = null;
		Connection con = null;
		String TABLE="ADDITIONALITEM";
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
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedItemmst = true;
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		
		return insertedItemmst;
	}
	public boolean insertIntoOBCustomerDiscount(Hashtable ht) throws Exception {
		boolean insertedItemmst = false;
		PreparedStatement ps = null;
		Connection con = null;
		String TABLE="MULTI_PRICE_MAPPING";
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
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedItemmst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertedItemmst;
	}
	
	public boolean insertIntoIBSupplierDiscount(Hashtable ht) throws Exception {
		boolean insertedItemmst = false;
		PreparedStatement ps = null;
		Connection con = null;
		String TABLE="MULTI_COST_MAPPING";
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
			String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ TABLE + "]" + " ("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, sQry);
			ps = con.prepareStatement(sQry);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				insertedItemmst = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return insertedItemmst;
	}
	
	public boolean updateItemImage(String plant,String item,String catalogPath) {
		
		Connection con = null;
		boolean insertedItemImage = false;
		try {
			con = DbBean.getConnection();
			String query = "update "+plant+"_"+ tblName+" set CATLOGPATH ='"+catalogPath+"' where ITEM ='"+item+"'";
			
			this.mLogger.query(this.printQuery, query);
			PreparedStatement ps = con.prepareStatement(query);
			int iCnt = ps.executeUpdate();
			
			if (iCnt > 0)
				insertedItemImage = true;
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}	
		}
		
		return insertedItemImage;
	}
	
	public boolean updateAddItemImage(String plant,String item,String catalogPath,String time) {
		
		Connection con = null;
		boolean insertedItemImage = false;
		try {
			con = DbBean.getConnection();
			String query = "update "+plant+"_CATALOGMST set CATLOGPATH ='"+catalogPath+"' where PRODUCTID ='"+item+"' and USERTIME1='"+time+"'" ;
			
			this.mLogger.query(this.printQuery, query);
			PreparedStatement ps = con.prepareStatement(query);
			int iCnt = ps.executeUpdate();
			
			if (iCnt > 0)
				insertedItemImage = true;
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}	
		}
		
		return insertedItemImage;
	}
	
	public boolean updatelogoImage(String plant,String catalogPath) {
		
		Connection con = null;
		boolean insertedItemImage = false;
		try {
			con = DbBean.getConnection();
			String query = "update PLNTMST set LOGOPATH ='"+catalogPath+"' where PLANT ='"+plant+"'";
			
			this.mLogger.query(this.printQuery, query);
			PreparedStatement ps = con.prepareStatement(query);
			int iCnt = ps.executeUpdate();
			
			if (iCnt > 0)
				insertedItemImage = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}	
		}
		
		return insertedItemImage;
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
	  
	  public ArrayList getUserItemDetails(String selectList, Hashtable ht,
				String extCond) throws Exception {
			boolean flag = false;

			Connection con = null;
			ArrayList alResult = new ArrayList();
			StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
					+ "[" + ht.get("PLANT") + "_" + tblName + "]");

			try {
				con = DbBean.getConnection();

				String conditon = "";
				if (ht.size() > 0) {
					sql.append(" WHERE ");
					conditon = formCondition(ht);
					sql.append(conditon);
				}
				if (extCond.length() > 0) {
					sql.append(" ");
					sql.append(extCond);
				}
				this.mLogger.query(this.printQuery, sql.toString());
				alResult = selectData(con, sql.toString());
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con);
			}

			return alResult;

		}
	  
		public boolean isAlreadtCustPrdExists(String aUser,String aLoc,String plant) throws Exception {
			Connection con = null;
			boolean isExists = false;
			try {
				con = DbBean.getConnection();
				String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_CUSTOMERPRODUCT] WHERE "
	                                        + "  CUSTNO ='"+aUser.toUpperCase()+"' AND " + "ITEM"+ "='" + aLoc.toUpperCase()
	                                        + "'";
				this.mLogger.query(this.printQuery, sQry);
				isExists = isExists(con, sQry);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con);
			}
			return isExists;
		}
		
		public boolean deleteUserCustPrd(String custno, String plant) throws Exception {

			boolean deleteItemMst = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = DbBean.getConnection();
				String sQry = "DELETE FROM " + "[" + plant + "_CUSTOMERPRODUCT]"
						+ " WHERE CUSTNO ='"+custno.toUpperCase()+"'";
				ps = con.prepareStatement(sQry);
				this.mLogger.query(this.printQuery, sQry);
				deleteItemMst = DeleteRow(con, sQry);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(con, ps);
			}
			return deleteItemMst;
		}
		
		public boolean insertIntoCustPrdMst(Hashtable ht) throws Exception {
			boolean insertedCycleCount = false;

			Connection conn = null;
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
				String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_CUSTOMERPRODUCT]" + "("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";

				this.mLogger.query(this.printQuery, query.toString());

				insertedCycleCount = insertData(conn, query);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				DbBean.closeConnection(conn);
			}

			return insertedCycleCount;
		}
		
		public boolean insertItemSupplier(Hashtable ht) throws Exception {
			boolean insertedItemmst = false;
			PreparedStatement ps = null;
			Connection con = null;
			String TABLE="ITEM_SUPPLIER";
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
				String sQry = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
						+ TABLE + "]" + " ("
						+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";
				this.mLogger.query(this.printQuery, sQry);
				ps = con.prepareStatement(sQry);
				int iCnt = ps.executeUpdate();
				if (iCnt > 0)
					insertedItemmst = true;

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			} finally {
				DbBean.closeConnection(con, ps);
			}

			return insertedItemmst;
		}
		
		public ArrayList getSupplierCostList(String  item,String  plant) throws Exception {
			
			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
				con = com.track.gates.DbBean.getConnection();
				
				boolean flag = false;
				String sQry = "WITH A AS (SELECT DISTINCT H.CustCode,ISNULL((SELECT TOP 1 V.VNAME FROM "+ plant+"_VENDMST V WHERE H.CustCode=V.VENDNO),'') CustName,D.UNITCOST FROM "+ plant+"_PODET D JOIN "+ plant+ "_POHDR H ON H.PONO=D.PONO where ITEM='"+ item+"' "
						+ " UNION "
						+ "SELECT DISTINCT H.VENDNO AS CustCode,ISNULL((SELECT TOP 1 V.VNAME FROM "+ plant+"_VENDMST V WHERE H.VENDNO=V.VENDNO),'') CustName,D.COST FROM "+ plant+"_FINBILLDET D JOIN "+ plant+ "_FINBILLHDR H ON H.ID=D.BILLHDRID  where ITEM='"+ item+"' "
						+ ") SELECT DISTINCT * FROM A";
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
}