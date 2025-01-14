package com.track.db.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.StockTakeDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsStockTake;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class StockTakeUtil {

	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.StockTakeUtil_PRINTPLANTMASTERLOG;
	private boolean printQuery = MLoggerConstant.STOCKTAKEDAO_PRINTPLANTMASTERQUERY;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public StockTakeUtil() {

	}

	/**
	 * method : getInvList(String aPlant,String aWhid,String aBinno,String
	 * aItem,String aBatch) description : get inventory details
	 * 
	 * @param
	 * @return ArrayList
	 */
	public ArrayList getInvList(String aPlant, String aWhid, String aBinno,
			String aItem, String aBatch) {
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			invdao.setmLogger(mLogger);
			Hashtable ht = new Hashtable();

			if (StrUtils.fString(aWhid).length() > 0)
				ht.put("A.WHID", aWhid);
			if (StrUtils.fString(aBinno).length() > 0)
				ht.put("A.BINNO", aBinno);
			if (StrUtils.fString(aItem).length() > 0)
				ht.put("A.ITEM", aItem);
			if (StrUtils.fString(aBatch).length() > 0)
				ht.put("BATCH", aBatch);

			String aQuery = "SELECT DISTINCT A.WHID as WHID,A.BINNO as BINNO,A.ITEM as ITEM,B.ITEMDESC as ITEMDESC,A.BATCH as BATCH,QTY ,B.STKUOM as UOM from INVMST A,ITEMMST  B where A.PLANT =B.PLANT AND A.PLANT ='"
					+ aPlant + "' AND  A.ITEM =B.ITEM ";
			// arrList = invdao.selectForReport(aQuery,ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getMovHisList(Hashtable ht, String afrmDate, String atoDate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}

			String aQuery = "SELECT JOBNUM,TRANDATE,CRAT,DIRTYPE,ITEM,CUSTOMER,CRBY FROM MOVHIS WHERE PLANT='SIS' "
					+ sCondition;
			arrList = movHisDAO.selectForReport(aQuery, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getStockTakeDetails() {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		Hashtable ht = new Hashtable();

		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			String aQuery = " select ITEM,USERFLD1 AS DESCRIP,UPPER(LOC) LOC,BATCH,isnull((select qty from invmst where plant = stktake.plant and item =stktake.item and loc =stktake.loc and userfld4 = stktake.batch ),0) ";
			aQuery = aQuery
					+ " as INVQTY,qty as QTY,(ISNULL(QTY,'0')-ISNULL((select qty from invmst where plant = stktake.plant and item =stktake.item and loc =stktake.loc and userfld4 = stktake.batch ),'0')) AS DIFFQTY  from stktake";
			arrList = movHisDAO.selectForReport(aQuery, ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
	@SuppressWarnings("all")
	public ArrayList getStockTakeDetails(String plant,
			HashMap<String, String> inputValues,String itemdesc,String loctypeid) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
                StrUtils su = new StrUtils();
		Hashtable ht = new Hashtable();

		try {
                        if(itemdesc.length()>0){
                            sCondition = " and replace(stktake.USERFLD1,' ','') like '%"+ new StrUtils().InsertQuotes(itemdesc.replaceAll(" ","")) + "%' ";
                        }
                        if (loctypeid != null && loctypeid!="") {
                   			sCondition = sCondition + "  AND LOC in(select LOC from " +plant+"_LOCMST where LOC_TYPE_ID like '%"+loctypeid+"%')";
                   									}
                           
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (inputValues.size() > 0) {
				sCondition = sCondition +" AND " + movHisDAO.formCondition(inputValues);
			}			
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			String aQuery = "with t as( select stktake.ITEM,stktake.USERFLD1 AS DESCRIP,UPPER(stktake.LOC) LOC, BATCH,isnull(CR_DATE,'')TRANDATE,stktake.UOM as STKUOM,"
					+ " isnull(  ( "
					+ " select sum(qty) from "
					+ plant
					+ "_invmst where plant = stktake.plant and item =stktake.item and loc =stktake.loc and userfld4 = stktake.batch "
					+ ") , 0 ) "
					+ " as INVSTKQTY,qty as QTY,(ISNULL(QTY,'0')-ISNULL( (select sum(qty) from "
					+ plant
					+ "_invmst as invmst where plant = stktake.plant and item =stktake.item and loc =stktake.loc and userfld4 = stktake.batch ),'0')) AS DIFFQTY, (SELECT isnull(UnitPrice,0) UnitPrice FROM ["
					+ plant
					+ "_ITEMMST] WHERE ITEM=stktake.item ) as UnitPrice ," 
					+ " (SELECT isnull(PRD_BRAND_ID,'') PRD_BRAND_ID FROM ["
					+ plant
					+"_ITEMMST] WHERE ITEM=stktake.item ) as PRD_BRAND_ID,(SELECT isnull(ITEMTYPE,'') ITEMTYPE FROM ["
					+ plant
					+ "_ITEMMST] WHERE ITEM=stktake.item ) as ITEMTYPE,(SELECT isnull(PRD_CLS_ID,'') PRD_CLS_ID FROM [" 
					+ plant
					+ "_ITEMMST] WHERE ITEM=stktake.item ) as PRD_CLS_ID from "
					+ plant + "_stktake as stktake," 
					+ plant
					+"_ITEMMST b WHERE stktake.PLANT='"+plant+"' AND stktake.ITEM=b.ITEM "+sCondition+") "
					+"select *,convert(int,(INVSTKQTY/(ISNULL((select u.QPUOM from "+plant+"_UOM u where u.UOM=STKUOM),1)))) INVQTY, "
					+"case when (ISNULL((select u.QPUOM from "+plant+"_UOM u where u.UOM=STKUOM),1))<=INVSTKQTY then INVSTKQTY - (convert(int,(INVSTKQTY/(ISNULL((select u.QPUOM from "+plant+"_UOM u where u.UOM=STKUOM),1))))*(ISNULL((select u.QPUOM from "+plant+"_UOM u where u.UOM=STKUOM),1))) else INVSTKQTY end PCSQTY "
					+"from t stktake order by stktake.LOC, stktake.ITEM";

			
			this.mLogger.query(this.printQuery, aQuery.toString());
			arrList = movHisDAO.selectForReport(aQuery, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	

	public String process_StockTake(Map<String, String> hashMapData) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_StockTake(hashMapData);

			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Stock take for Product : "
					+ hashMapData.get(IConstants.ITEM) + " is successful!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in Stock take for Product : "
							+ hashMapData.get(IConstants.ITEM) + " ");
		}
		return xmlStr;
	}

	private boolean process_Wms_StockTake(Map<String, String> hashMapData)
			throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsStockTake();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(hashMapData);

		return flag;
	}

	public boolean ResetStkTake() {
		boolean flgdel = false;
		StockTakeDAO stkdao = new StockTakeDAO();
		try {
			stkdao.setmLogger(mLogger);
			flgdel = stkdao.deleteStkTake();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flgdel;
	}

	public boolean ResetStkTake(String plant) {
		boolean flgdel = false;
		StockTakeDAO stkdao = new StockTakeDAO();
		try {
			stkdao.setmLogger(mLogger);
			flgdel = stkdao.deleteStkTake(plant);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return flgdel;
	}

	public String getAllStockTakeDetails(HashMap<String, String> inputHashMap)
			throws Exception {
		try {
			ArrayList<Map<String, String>> resultArrayList = new ArrayList<>();

			String xmlStr = "";
			StockTakeDAO stockTakeDAO = new StockTakeDAO();
			stockTakeDAO.setmLogger(mLogger);
			resultArrayList = stockTakeDAO.getAllStockTakeDetails(inputHashMap);
			if (resultArrayList.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr = xmlStr
						+ XMLUtils.getStartNode("itemDetails total='"
								+ resultArrayList.size() + "'");
				Integer i = 1;
				for (Map<String, String> hashMap : resultArrayList) {
					xmlStr = xmlStr + XMLUtils.getStartNode("list");
					xmlStr = xmlStr
							+ XMLUtils.getXMLNode("ENTRY", i.toString());
					xmlStr = xmlStr
							+ XMLUtils.getXMLNode("ITEM", hashMap.get("ITEM"));
					xmlStr = xmlStr
							+ XMLUtils
									.getXMLNode("BATCH", hashMap.get("BATCH"));
					xmlStr = xmlStr
							+ XMLUtils.getXMLNode("LOC", hashMap.get("LOC"));
					xmlStr = xmlStr
							+ XMLUtils.getXMLNode("QTY", hashMap.get("QTY"));
					xmlStr = xmlStr + XMLUtils.getEndNode("list");
					i++;
				}
			}
			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");
			return xmlStr;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

	}

	public Boolean removeStockTake(HashMap<String, String> inputHashMap)
			throws Exception {
		try {
			String loginUser = inputHashMap.get("USER");
			inputHashMap.remove("USER");
			StockTakeDAO stockTakeDAO = new StockTakeDAO();
			stockTakeDAO.setmLogger(mLogger);
			Boolean valueReturn = stockTakeDAO
					.deleteStkTakeDetail(inputHashMap);

			if (valueReturn.booleanValue()) {
				MovHisDAO movHisDao = new MovHisDAO();
				movHisDao.setmLogger(mLogger);

				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, inputHashMap
						.get(IConstants.PLANT));
				htRecvHis.put("DIRTYPE", TransactionConstants.STOCK_TAKE_RESET);
				htRecvHis.put(IDBConstants.ITEM, StrUtils.fString(inputHashMap
						.get(IConstants.ITEM)));
				htRecvHis.put("MOVTID", "");
				htRecvHis.put("RECID", "");
				htRecvHis.put(IDBConstants.LOC, StrUtils.fString(inputHashMap
						.get(IConstants.LOC)));
				htRecvHis.put(IDBConstants.QTY, "0");
				htRecvHis.put(IDBConstants.CREATED_BY, loginUser);
				htRecvHis.put(IDBConstants.TRAN_DATE, new DateUtils()
						.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

				valueReturn = movHisDao.insertIntoMovHis(htRecvHis);
			}

			return valueReturn;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}

	public String removeStockTakeDetail(HashMap<String, String> inputHashMap)
			throws Exception {
		try {
			String xmlStr = "";
			StockTakeDAO stockTakeDAO = new StockTakeDAO();
			stockTakeDAO.setmLogger(mLogger);
			Boolean resultArrayList = stockTakeDAO
					.deleteStkTakeDetail(inputHashMap);
			if (resultArrayList) {
				xmlStr = XMLUtils.getXMLMessage(0, "Item Sucessfully removed!");
			} else {
				xmlStr = XMLUtils
						.getXMLMessage(1, "Unable to Remove the Item!");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	public ArrayList getInvListManualSummaryWithOutPriceMultiUom(Hashtable ht, String aPlant,String aItem,String productDesc,String loctypeid,String loctypeid2,String loctypeid3,String loc,String UOM) throws Exception {
        ArrayList arrList = new ArrayList();        
      //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        String sCondition ="";
	    if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        
         sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }
	  	        
        if (!loc.equalsIgnoreCase("") && !loc.equalsIgnoreCase("")) {
        	sCondition = sCondition+" AND A.LOC like '%"+loc+"%'";
        }
        
        if (!loctypeid.equalsIgnoreCase("") && !loctypeid.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}
        
        if (!loctypeid2.equalsIgnoreCase("") && !loctypeid2.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
		}
        if (!loctypeid3.equalsIgnoreCase("") && !loctypeid3.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
		}


        try {
        		StockTakeDAO _InvMstDAO = new StockTakeDAO();
                _InvMstDAO.setmLogger(mLogger);
               sCondition = sCondition  + " GROUP BY u.ID,u.item,b.REMARK1,u.loc,u.batch,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.INVENTORYUOM,QPUOM,u.CR_DATE,u.INVFLAG,u.CRBY,u.REMARKS  order by item ,loc  ";

               String aQuery="";
				if(UOM.length()>0){

					aQuery = "SELECT u.ID,u.ITEM,u.LOC,b.REMARK1,b.ITEMDESC,b.prd_cls_id AS PRDCLSID,b.PRD_DEPT_ID,b.PRD_BRAND_ID,b.MODEL,b.ITEMTYPE,"
							+ " u.BATCH, '"+UOM+"' INVENTORYUOM,ISNULL(SUM(a.QTY), 0) AS QTY,b.STKUOM,CASE WHEN ISNULL(QPUOM, 0) > 0 THEN (CONVERT(FLOAT, (SUM(a.QTY) / QPUOM)))"
							+ " ELSE SUM(a.QTY) END AS INVUOMQTY,u.CR_DATE CRDATE,SUM(u.QTY) AS STKTAKEQTY,SUM(u.DIFFQTY) AS DIFFQTY,u.INVFLAG,"
							+ " u.CRBY,u.REMARKS FROM "+aPlant+"_STKTAKE u JOIN "+aPlant+"_itemmst b ON u.item = b.item"
							+ " LEFT JOIN "+aPlant+"_UOM UOM1 ON '"+UOM+"' = UOM1.UOM LEFT JOIN"
							+ " (SELECT item, userfld4, loc, SUM(QTY) AS QTY FROM "+aPlant+"_invmst"
							+ " GROUP BY item, userfld4, loc) a ON u.ITEM = a.item AND u.BATCH = a.userfld4 AND u.LOC = a.LOC"
							+ " WHERE u.STATUS = 'N'";
				}
				else
				{
					aQuery = "SELECT u.ID,u.ITEM,u.LOC,b.REMARK1,b.ITEMDESC,b.prd_cls_id AS PRDCLSID,b.PRD_DEPT_ID,b.PRD_BRAND_ID,b.MODEL,b.ITEMTYPE,"
							+ " u.BATCH,b.INVENTORYUOM,ISNULL(SUM(a.QTY), 0) AS QTY,b.STKUOM,CASE WHEN ISNULL(QPUOM, 0) > 0 THEN (CONVERT(FLOAT, (SUM(a.QTY) / QPUOM)))"
							+ " ELSE SUM(a.QTY) END AS INVUOMQTY,u.CR_DATE CRDATE,SUM(u.QTY) AS STKTAKEQTY,SUM(u.DIFFQTY) AS DIFFQTY,u.INVFLAG,"
							+ " u.CRBY,u.REMARKS FROM "+aPlant+"_STKTAKE u JOIN "+aPlant+"_itemmst b ON u.item = b.item"
							+ " LEFT JOIN "+aPlant+"_UOM UOM1 ON b.INVENTORYUOM = UOM1.UOM LEFT JOIN"
							+ " (SELECT item, userfld4, loc, SUM(QTY) AS QTY FROM "+aPlant+"_invmst"
							+ " GROUP BY item, userfld4, loc) a ON u.ITEM = a.item AND u.BATCH = a.userfld4 AND u.LOC = a.LOC"
							+ " WHERE u.STATUS = 'N'";
				}
                arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return arrList;
}
}
