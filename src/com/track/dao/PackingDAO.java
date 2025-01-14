package com.track.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class PackingDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.RSNMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.RSNMST_PRINTPLANTMASTERLOG;

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

	public static final String TABLE_NAME = "PACKINGMST";
	public static final String TABLE_NAME1 = "DNPLHDR";
	public static final String TABLE_NAME2 = "DNPLDET";
	
	public PackingDAO() {
	}

	public boolean isExists(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
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

	public boolean insertIntoPacking(Hashtable ht) throws Exception {
		boolean insertedInv = false;
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
			insertedInv = insertData(con, query);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertedInv;
	}

	public boolean updateDNPLHDR(String query, Hashtable htCondition, String extCond)
            throws Exception {
    boolean flag = false;
    java.sql.Connection con = null;
    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE " + "["
                            + htCondition.get("PLANT") + "_" + TABLE_NAME1 + "]");
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

	public boolean updateDNPLDET(String query, Hashtable htCondition, String extCond)
            throws Exception {
    boolean flag = false;
    java.sql.Connection con = null;
    try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE " + "["
                            + htCondition.get("PLANT") + "_" + TABLE_NAME2 + "]");
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

	
	public ArrayList getPackingDetails(String plant,
			String cond) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String packing = "";
			String sQry = "select distinct PACKINGID,PACKING,ISACTIVE from "
					+ "[" + plant + "_" + "PACKINGMST] where PACKING like '"
					+ packing + "%' " + cond
					+ " ORDER BY PACKING ";
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

	public boolean updatePacking(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";
			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}
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
					+ TABLE_NAME + "]" + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}

	public boolean deletePacking(java.util.Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
					+ "]");
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

	public ArrayList getPacking(String plant) throws Exception {

		MLogger.log(1, this.getClass() + " getUom()");
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer("  SELECT  ");
			sql.append("uom,");
			sql.append("uomdesc");
			
			sql.append(" ");
			sql.append(" FROM " + "[" + plant + "_"
					+ "uom] WHERE ISACTIVE='Y'");
			sql.append(" ORDER BY rsnCode");
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
	public List getDnplHdrById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> dnplHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="Select A.ID as HID,ISNULL(DONO,'') as DONO,A.CUSTNO,ISNULL((select top 1 D.CNAME from "+ ht.get("PLANT") +"_CUSTMST D where D.CUSTNO=A.CUSTNO),'') as CustName,DNPLDATE as CollectionDate, "
			+ "ISNULL(INVOICE,'') as INVOICENO,ISNULL(GINO,'') as GINO,'' as JobNum,ISNULL(DELIVERYNOTE,'') as DNNO,ISNULL(PACKINGLIST,'') as PLNO,ISNULL(TOTALNETWEIGHT,'') as TOTALNETWEIGHT, "
			+ "ISNULL(TOTALGROSSWEIGHT,'') as TOTALGROSSWEIGHT,ISNULL(NOTE,'') NOTE,ISNULL(TOTAlDIMENSION,'') as NETDIMENSION,ISNULL(TOTAlPACKING,'') as NETPACKING,"
			+ "ISNULL((select top 1 ISNULL(SHIPPINGID,'') from "+ ht.get("PLANT") +"_DOHDR D where D.DONO=A.DONO),'') as SHIPPINGID "
			+ "FROM [" + ht.get("PLANT") + "_DNPLHDR] A WHERE ID = ? AND PLANT =? ";
			
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			dnplHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return dnplHdrList;
	}

	public List getDnplDetByHdrId(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> InvoiceDetList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query = "Select distinct B.ID,lnno as dolnno,ISNULL(UOM,'') as UOM,ITEM,"
		             +" ISNULL((select isnull(ITEMDESC,'') from "+ht.get("PLANT")+"_ITEMMST where ITEM =B.ITEM),'') as ITEMDESC,"
		             +" ISNULL(netweight,'') netweight,ISNULL(grossweight,'') grossweight,ISNULL(b.item,''),isnull(qty,0) as QTY,"
		             +" isnull(hscode,'') as HSCODE, isnull(coo,'') as COO,"
					 + "ISNULL((SELECT CASE WHEN PRD_BRAND_ID = 'NOBRAND' THEN '' ELSE PRD_BRAND_ID END AS BRAND FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=B.ITEM),'') AS BRAND,"
					 + "ISNULL((SELECT REMARK1 FROM ["+ ht.get("PLANT") +"_ITEMMST] WHERE ITEM=B.ITEM),'') AS DETAILDESC,"
					 +" isnull(PACKING,'')packing,isnull(DIMENSION,'') dimension"
					 +  " from ["+ht.get("PLANT")+"_DNPLDET] B WHERE HDRID = ? AND PLANT =? ORDER BY ID ";
			
			
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("HDRID"));
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
	
	public List getDnplShippingById(Hashtable ht) throws Exception {
		java.sql.Connection con = null;
		List<Map<String, String>> dnplHdrList = new ArrayList<>();
		Map<String, String> map = null;
		String query="";
		List<String> args = null;
		try {			
			/*Instantiate the list*/
		    args = new ArrayList<String>();
		    
			con = com.track.gates.DbBean.getConnection();
			query="Select isnull(CARRIER,'') CARRIER, "
		         + "isnull(S.SHIPPING_STATUS,'') as SHIPSTATUS,isnull(SHIPPING_DATE,'') as SHIPDATE,"
		         + "isnull(S.INTRANSIT_STATUS,'') as INTRANSITSTATUS,isnull(S.INTRANSIT_DATE,'') as INTRANSITDATE,isnull(S.DELIVERY_STATUS,'') as DELIVERYSTATUS,isnull(S.DELIVERY_DATE,'') as DELIVERYDATE,"
		         + "isnull(S.TRANSPORTID,'') as TRANSPORT,isnull(S.CLEARING_AGENT_ID,'') as CLEARAGENT,isnull(S.CONTACTNAME,'') as CONTACTNAME,isnull(S.TRACKINGNO,'') as TRACKINGNO,"
		         + "isnull(S.FREIGHT_FORWARDERID,'') as FREIGHT,isnull(S.DURATIONOFJOURNEY,'') as JOURNEY,isnull(S.DNPLID,'') as SHIPDNPLID "
			+ "FROM [" + ht.get("PLANT") + "_SHIPPINGHDR] s WHERE DNPLID = ? AND PLANT =? ";
			
			PreparedStatement ps = con.prepareStatement(query);  
			
			/*Storing all the query param argument in list squentially*/
			args.add((String) ht.get("ID"));
			args.add((String) ht.get("PLANT"));
			
			this.mLogger.query(this.printQuery, query);
			
			dnplHdrList = selectData(ps, args);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return dnplHdrList;
	}
}