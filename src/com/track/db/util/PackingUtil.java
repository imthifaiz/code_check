package com.track.db.util;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BaseDAO;

import com.track.dao.DNPLDetDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.PackingDAO;
import com.track.dao.RsnMst;
import com.track.dao.UomDAO;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class PackingUtil {

	private static MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.PackingUtil_PRINTPLANTMASTERQUERY;
	StrUtils strUtils=new StrUtils();
    BaseDAO basedao = new BaseDAO();
    DoDetDAO _DoDetDAO = new DoDetDAO();
    DNPLDetDAO _DNPLDetDAO = new DNPLDetDAO(); 
	private Boolean printLog;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public PackingUtil() {

	}

	public boolean isExistsPacking(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		PackingDAO dao = new PackingDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(htLoc);

		} catch (Exception e) {

			throw e;
		}

		return isExists;
	}

	public boolean insertPacking(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			PackingDAO itemDao = new PackingDAO();
			itemDao.setmLogger(mLogger);
			inserted = itemDao.insertIntoPacking(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean updatePacking(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			PackingDAO dao = new PackingDAO();
			dao.setmLogger(mLogger);
			update = dao.updatePacking(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}
	
	

	public boolean deletePacking(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			PackingDAO dao = new PackingDAO();
			dao.setmLogger(mLogger);
			deleted = dao.deletePacking(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}

	public Map getPackingDetails(String apacking) throws Exception {

		PackingDAO dao = new PackingDAO();
		dao.setmLogger(mLogger);
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PACKING, apacking);
		Map map = new HashMap();
		try {

			String sql = " PACKING,ISACTIVE";

		} catch (Exception e) {

			throw e;
		}
		return map;

	}

	public  ArrayList getPackingDetails(String plant,
			String cond) {

		ArrayList al = null;
		PackingDAO dao = new PackingDAO();
		dao.setmLogger(mLogger);
		try {
			al = dao.getPackingDetails(plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList selectdnplHDR(String query, Hashtable ht) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from " + "["
				+ ht.get("PLANT") + "_" + "DNPLHDR" + "]");
		String conditon = "";

		try {
			con = com.track.gates.DbBean.getConnection();

			if (ht.size() > 0) {
				this.mLogger.log(0, "condition preisent stage 3");
				sql.append(" WHERE ");

				conditon = basedao.formCondition(ht);

				sql.append(conditon);

			}
			this.mLogger.query(this.printQuery, sql.toString());
			alData = basedao.selectData(con, sql.toString());
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
	
	public ArrayList dnpl(String plant, String aDONO, String invoiceNo)
		      throws Exception {
		     String q = "";
		     String link = "";
		     String result = "";
		     String where = "";
		     ArrayList al = null;

		     try {
		      Hashtable htCond = new Hashtable();
		      htCond.put("A.PLANT", plant);
		      htCond.put("A.dono", aDONO);
		      htCond.put("INVOICENO", invoiceNo);
		      q = "distinct A.dono,dolnno,unitmo as uom,itemdesc as Itemdesc,netweight,grossweight,lnstat,a.item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,a.loc as loc,a.userfld4 as batch,"
		         +" (select PLNO from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as PLNO,"
		         + "(select DNNO from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as DNNO,"
		         +" (select isnull(TOTALGROSSWEIGHT,'') from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as TOTALGROSSWEIGHT,"
		         +" (select isnull(TOTALNETWEIGHT,'') from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as TOTALNETWEIGHT,"
		         +" (select isnull(NETPACKING,'') from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as NETPACKING,"
		         +" (select isnull(NETDIMENSION,'') from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as NETDIMENSION,"
		         +" (select isnull(DNPLREMARKS,'') from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as DNPLREMARKS,"
		         +" (select isnull(CustName,'') from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as custname,"
		         +" (select isnull(JobNum,'') from ["+plant+"_DNPLHDR] where PLANT =a.PLANT and DONO =a.DONO and INVOICENO = a.INVOICENO) as JOBNUM,"
		         +" (select isnull(hscode,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as hscode,"
	             +" (select isnull(coo,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as coo,"
		         + "isnull(PACKING,'')packing,isnull(DIMENSION,'') dimension";
		      _DNPLDetDAO.setmLogger(mLogger);
		      al = _DNPLDetDAO
		        .selectdnplDet(
		          q,
		          htCond,
		          " a.plant <> '' ");

		     } catch (Exception e) {
		      throw e;
		     }
		     return al;
		  }

	public ArrayList dnplGINO(String plant, String aDONO, String aGINO, String invoiceNo,String HID)
		      throws Exception {
		     String q = "";
		     String link = "";
		     String result = "";
		     String where = "";
		     ArrayList al = null;

		     try {
		      Hashtable htCond = new Hashtable();
		      htCond.put("A.PLANT", plant);
		      if(strUtils.fString(aDONO).length() > 0) htCond.put("A.dono", aDONO);
		      htCond.put("A.ID", HID);
		      if(strUtils.fString(aGINO).length() > 0)htCond.put("A.GINO", aGINO);
		      if(strUtils.fString(invoiceNo).length() > 0) htCond.put("INVOICE", invoiceNo);
		      q = "distinct ISNULL(A.dono,'') dono,lnno as dolnno,UOM as uom,ISNULL(INVOICE,'') INVOICE,"
		    	 +" isnull((select top 1 isnull(ID,0) from "+plant+"_FININVOICEHDR where PLANT =a.PLANT and INVOICE =A.INVOICE ),0) as HID,"	  
	             +" (select top 1 isnull(ITEMDESC,'') from "+plant+"_ITEMMST where PLANT =a.PLANT and ITEM =b.ITEM ) as Itemdesc,"
	             +" netweight,grossweight,'' lnstat,b.item,isnull(qty,0) as qtyor,isnull(qty,0) as qtyis,isnull(qty,0) as qtyPick,"
	             +" '' as loc,'' as batch, A.PACKINGLIST  as PLNO,A.DELIVERYNOTE as DNNO, isnull(TOTALGROSSWEIGHT,'') as TOTALGROSSWEIGHT," 
	             +" isnull(TOTALNETWEIGHT,'') as TOTALNETWEIGHT,isnull(TOTAlPACKING,'') as NETPACKING,"
				 +" isnull(TOTAlDIMENSION,'') as NETDIMENSION, isnull(NOTE,'') as DNPLREMARKS,"
				 +" (select isnull(CNAME,'') from "+plant+"_CUSTMST C where C.PLANT =a.PLANT and C.CUSTNO = A.CUSTNO ) as custname,"
				 +" '' as JOBNUM,isnull(hscode,'') as hscode, isnull(coo,'') as coo,"
		         + "isnull(PACKING,'')packing,isnull(DIMENSION,'') dimension,isnull(CARRIER,'') CARRIER,"
				 
		         + "isnull(S.SHIPPING_STATUS,'') as SHIPSTATUS,isnull(SHIPPING_DATE,'') as SHIPDATE,"
		         + "isnull(S.INTRANSIT_STATUS,'') as INTRANSITSTATUS,isnull(S.INTRANSIT_DATE,'') as INTRANSITDATE,isnull(S.DELIVERY_STATUS,'') as DELIVERYSTATUS,isnull(S.DELIVERY_DATE,'') as DELIVERYDATE,"
		         + "isnull(S.TRANSPORTID,'') as TRANSPORT,isnull(S.CLEARING_AGENT_ID,'') as CLEARAGENT,isnull(S.CONTACTNAME,'') as CONTACTNAME,isnull(S.TRACKINGNO,'') as TRACKINGNO,"
		         + "isnull(S.FREIGHT_FORWARDERID,'') as FREIGHT,isnull(S.DURATIONOFJOURNEY,'') as JOURNEY,isnull(S.DNPLID,'') as SHIPDNPLID"
		         
				 +  " from ["+plant+"_DNPLDET] B JOIN ["+plant+"_DNPLHDR] A ON B.HDRID=A.ID LEFT JOIN ["+plant+"_SHIPPINGHDR] S on b.HDRID=S.DNPLID ";
		      _DNPLDetDAO.setmLogger(mLogger);
		      al = _DNPLDetDAO
		        .selectdnpl(
		          q,
		          htCond,
		          " a.plant <> '' ");

		     } catch (Exception e) {
		      throw e;
		     }
		     return al;
		  }

	
	public ArrayList getPackingIssueSummary(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		     
         if (custname.length()>0){
              	custname = new StrUtils().InsertQuotes(custname);
               	sCondition =  sCondition + " AND CustName LIKE '%"+custname+"%' " ;
            }
         
                       
		    String dtCondStr =    " and ISNULL(DNPLDATE,'')<>'' AND CAST((SUBSTRING(DNPLDATE, 7, 4) + '-' + SUBSTRING(DNPLDATE, 4, 2) + '-' + SUBSTRING(DNPLDATE, 1, 2)) AS date)";
                    if (afrmDate.length() > 0) {
                    	sCondition = sCondition +dtCondStr+ " >= '" 
        						+ afrmDate
        						+ "'  ";
        				if (atoDate.length() > 0) {
        					sCondition = sCondition + dtCondStr + " <= '" 
        					+ atoDate
        					+ "'  ";
        				}
        			} else {
        				if (atoDate.length() > 0) {
        					sCondition = sCondition +dtCondStr+ "  <= '" 
        					+ atoDate
        					+ "'  ";
        				}
        			}
                    
                  	  	extraCon = " order by A.ID DESC,CAST((SUBSTRING(DNPLDATE, 7, 4) + SUBSTRING(DNPLDATE, 4, 2) + SUBSTRING(DNPLDATE, 1, 2)) AS date) DESC";
                    
              if(!sorttype.equalsIgnoreCase("")){      
              sql.append(" WITH SORTTYPE AS( ");}
              
				sql.append("Select A.ID as HID,DONO as DONO,ISNULL((select top 1 D.CNAME from "+ plant +"_CUSTMST D where D.CUSTNO=A.CUSTNO),'') as CustName,DNPLDATE as CollectionDate, ");
				sql.append("INVOICE as INVOICENO,GINO as GINO,'' as JobNum,DELIVERYNOTE as DNNO,PACKINGLIST as PLNO,TOTALNETWEIGHT as TOTALNETWEIGHT,TOTALGROSSWEIGHT as TOTALGROSSWEIGHT,");
				sql.append("ISNULL(NOTE,'')  remarks,TOTAlDIMENSION as NETDIMENSION,TOTAlPACKING as NETPACKING,S.DELIVERY_STATUS as DELSTS,S.DNPLID");
//				sql.append("ISNULL(NOTE,'')  remarks,TOTAlDIMENSION as NETDIMENSION,TOTAlPACKING as NETPACKING,");
//				sql.append("ISNULL((select top 1 z.DELIVERY_STATUS from test_SHIPPINGHDR z where A.ID=z.DNPLID),'')as DELSTS,");
//				sql.append("ISNULL((select top 1 z.DNPLID from test_SHIPPINGHDR z where A.ID=z.DNPLID),'')as DNPLID");
				
				
				sql.append(" FROM "+ plant +"_DNPLHDR A LEFT JOIN "+plant+"_SHIPPINGHDR S on A.ID=S.DNPLID WHERE A.PLANT='"+plant+"' "+ sCondition);
//				sql.append(" FROM "+ plant +"_DNPLHDR A WHERE A.PLANT='"+plant+"' "+ sCondition);
				
				
				
				   if (ht.get("DONO") != null) {
			    	   sql.append(" AND DONO = '" + ht.get("DONO") + "'");
			       } 
				  
				   
				   if (ht.get("JOBNUM") != null) {
              	   sql.append(" AND JOBNUM ='" + ht.get("JOBNUM") + "'");
                 } 
			       if (ht.get("CustName") != null) {
			    	   sql.append(" AND A.CUSTNO  = '" + ht.get("CustName") + "' ");
			       }
			       
			       if (ht.get("INVOICENO") != null) {
			    	   sql.append(" AND INVOICE  = '"+ ht.get("INVOICENO") + "' ");
			       }
			       
			       if (ht.get("GINO") != null) {
			    	   sql.append(" AND GINO  = '"+ ht.get("GINO") + "' ");
			       }
				arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
				// End code modified by radhika for outorderwithprice on 18/12/12 	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :PackingUtil :: getPackingIssueDetails:", e);
		}
		return arrList;
	}
	
}