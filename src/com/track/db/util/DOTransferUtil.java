package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ToDetDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.tran.WmsDoTransferPicking;
import com.track.tran.WmsDoTransferPickingRandom;
import com.track.tran.WmsPickingRandom;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.XMLUtils;
import com.track.util.StrUtils;

public class DOTransferUtil {

	DoTransferHdrDAO _DoTransferHdrDAO = null;
	DoTransferDetDAO _DoTransferDetDAO = null;
	private boolean printLog = MLoggerConstant.TOUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public DOTransferUtil() {
		_DoTransferHdrDAO  = new DoTransferHdrDAO();
		_DoTransferDetDAO = new DoTransferDetDAO();

	}

	public boolean saveToHdrDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			flag = _DoTransferHdrDAO.insertDOTRANSFERHDR(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public ArrayList getToDetDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoTransferDetDAO.setmLogger(mLogger);
			al = _DoTransferDetDAO.selectDOTRANSFERDET(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getToDetDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoTransferDetDAO.setmLogger(mLogger);
			al = _DoTransferDetDAO.selectDOTRANSFERDET(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getToHdrDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			al = _DoTransferHdrDAO.selectDOTRANSFERHDR(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getDoTransferHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			al = _DoTransferHdrDAO.selectDOTRANSFERHDR(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public String getCustCode(String aPlant, String aTONO) throws Exception {

		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("TONO", aTONO);

		String query = " ISNULL( " + "custCode" + ",'') as " + "custCode" + " ";
		_DoTransferHdrDAO.setmLogger(mLogger);
		Map m = _DoTransferHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custCode");

		return custCode;
	}

	public ArrayList getOutGoingToHdrDetailsPda(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			al = _DoTransferHdrDAO.selectTransferOrderHdrForPda(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public ArrayList getOutGoingToHdrDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			al = _DoTransferHdrDAO.selectTransferOrderHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public boolean saveToDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_DoTransferDetDAO.setmLogger(mLogger);
			flag = _DoTransferDetDAO.insertDOTRANSFERDET(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public String getNextTo(String aPlant) throws Exception {

		String maxDo = "";
		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			maxDo = _DoTransferHdrDAO.getMaxDo(aPlant);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return maxDo;
	}

	public String load_all_open_to_details_xml(String plant) throws Exception {

		String xmlStr = "", dono = "", custName = "", jobNum = "";
		ArrayList alToHdr = null;
		String queryToHdr = "";
		Hashtable htToHdr = null;
		htToHdr = new Hashtable();
		queryToHdr = " tono,custName,jobNum";
		// condition
		htToHdr.put("plant", plant);
		try {
			alToHdr = getDoTransferHdrDetails(queryToHdr, htToHdr, " and STATUS <> 'C'");

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Dos total ='"
					+ String.valueOf(alToHdr.size()) + "'");

			if (alToHdr.size() > 0) {
				for (int i = 0; i < alToHdr.size(); i++) {

					Map map1 = (Map) alToHdr.get(i);
					dono = (String) map1.get("dono");
					custName = (String) map1.get("custName");
					jobNum = (String) map1.get("jobNum");

					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("doNum", dono);
					xmlStr += XMLUtils.getXMLNode("custName", custName);
					xmlStr += XMLUtils.getXMLNode("jobNum", jobNum);
					xmlStr += XMLUtils.getEndNode("record");

				}

				xmlStr += XMLUtils.getEndNode("Dos");

			} else {
				throw new Exception("No open transfer order found for picking");
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return xmlStr;
	}

	
	
	
	public ArrayList listDoTransferPickingDet(String plant, String aDONO)
	throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("DONO", aDONO);
	
	
			q = "dono,dolnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname";
			_DoTransferDetDAO.setmLogger(mLogger);
			al = _DoTransferDetDAO.selectDOTRANSFERDET(q, htCond,
			"  and plant <> '' and pickstatus <>'C' and  LNSTAT <> 'C'  order by dolnno");
		
			
	} catch (Exception e) {
		throw e;
	}
	return al;
}

	
	public ArrayList getToHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			al = _DoTransferHdrDAO.selectDOTRANSFERHDR(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public String getVendorName(String aPlant, String aTONO) throws Exception {

		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("tono", aTONO);

		String query = " ISNULL( " + "custName" + ",'') as " + "custName" + " ";
		_DoTransferHdrDAO.setmLogger(mLogger);
		Map m = _DoTransferHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custName");

		return custCode;
	}

	
	public String getTransOrderItemDetails(String plant, String tono,
			Boolean isForReceive) {

		String xmlStr = "";
		ArrayList al = null;
		String receivedsql = "";
		if (isForReceive) {
			receivedsql = "isnull(qtyrc,0) as QTYPICK";
		} else {
			receivedsql = "isnull(QTYPICK,0) AS QTYPICK";
		}
		String query = " TONO , TOLNNO , ITEM," + " ITEMDESC, QTYOR ,"
				+ receivedsql + ",UNITMO ,ISNULL(USERFLD2,'') AS REMARKS ";
		String extCond = "AND lnstat <> 'C' ";
		if (!isForReceive) {
			extCond = extCond + " AND pickStatus <> 'C'";
		}
		Hashtable ht = new Hashtable();
		ht.put("TONO", tono);
		ht.put("PLANT", plant);
		try {
			_DoTransferDetDAO.setmLogger(mLogger);
			_DoTransferHdrDAO.setmLogger(mLogger);
			al = _DoTransferDetDAO.selectDOTRANSFERDET(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("tono", (String) map
							.get("TONO"));
					xmlStr += XMLUtils.getXMLNode("tolnno", (String) map
							.get("TOLNNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) map
							.get("ITEMDESC"));
					xmlStr += XMLUtils.getXMLNode("qtyor", (String) map
							.get("QTYOR"));
					xmlStr += XMLUtils.getXMLNode("qtypk", (String) map
							.get("QTYPICK"));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) map
							.get("REMARKS"));

					xmlStr += XMLUtils.getXMLNode("fromLoc", _DoTransferHdrDAO
							.getLocation(plant, tono, "FROMWAREHOUSE"));
					xmlStr += XMLUtils.getXMLNode("toLoc", _DoTransferHdrDAO
							.getLocation(plant, tono, "TOWAREHOUSE"));

					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	

	public ArrayList listOutGoingIssueTODET(String plant, String aTONO)
			throws Exception {
		String q = "";
		
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("TONO", aTONO);
			q = "tono,tolnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname";
			_DoTransferDetDAO.setmLogger(mLogger);
			al = _DoTransferDetDAO.selectDOTRANSFERDET(q, htCond,
					" and plant <> '' and lnstat <>'C' order by tolnno");

			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	
	
	public ArrayList getDoTransferDetails(String query, Hashtable ht)
	throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			al = _DoTransferHdrDAO.selectDoTransferHdr(query, ht);
		} catch (Exception e) {

			throw e;
		}
		
		return al;
		
	}


	public boolean process_DoTransferPickingForPC(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;

		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.TOPICK
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.TODET_TONUM) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.TODET_TOLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_DoTransferPicking(obj);

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
			throw e;
		}

		return flag;
	}
	
	public String process_DoTransferPickingForPDA(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Wms_DoTransferPicking(obj);

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
			String xmlStr = XMLUtils.getXMLMessage(0,
					"Error in Transfer item : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product transfered successfully!");

		} else {

		}
		return xmlStr;
	}


	private boolean process_Wms_DoTransferPicking(Map map) throws Exception {

		boolean flag = false;

		
		WmsTran tran = new WmsDoTransferPicking();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		
		flag = tran.processWmsTran(map);

		return flag;
	}


		

	public String getJobNum(String aPlant, String aPONO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aPONO);

		String query = " ISNULL( " + "jobNum" + ",'') as " + "jobNum" + " ";
		_DoTransferHdrDAO.setmLogger(mLogger);
		Map m = _DoTransferHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("jobNum");
		return custCode;
	}

	
	public Boolean removeRow(String plant, String tono, String userId) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			_DoTransferDetDAO.setmLogger(mLogger);
			_DoTransferHdrDAO.setmLogger(mLogger);
			ut = DbBean.getUserTranaction();
			ut.begin();
			Boolean removeHeader = this._DoTransferHdrDAO.removeOrder(plant, tono);
			Boolean removeDetails = this._DoTransferDetDAO.DoremoveOrderDetails(plant, tono);
				
			Hashtable htMovHis = createMoveHisHashtable(plant, tono, userId);
			Boolean movementHiss = movHisDao.insertIntoMovHis(htMovHis);
			if (removeHeader & removeDetails & movementHiss) {
				DbBean.CommitTran(ut);
				flag = Boolean.valueOf(true);
			} else {
				DbBean.RollbackTran(ut);
				flag = Boolean.valueOf(false);
			}
		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = Boolean.valueOf(false);
		}
		return flag;
	}

	private Hashtable createMoveHisHashtable(String plant, String tono,
			String userId) {
		Hashtable<String, String> htMovHis = new Hashtable<String, String>();

		htMovHis.put(IDBConstants.PLANT, plant);
		htMovHis.put("DIRTYPE", "DEL_TRANSFER_ORDER");
		htMovHis.put(IDBConstants.ITEM, " ");
		htMovHis.put("BATNO", " ");
		htMovHis.put("ORDNUM", tono);

		htMovHis.put("MOVTID", " ");
		htMovHis.put("RECID", " ");
		htMovHis.put(IDBConstants.LOC, " ");

		htMovHis.put(IDBConstants.CREATED_BY, userId);
		htMovHis.put(IDBConstants.TRAN_DATE, DateUtils
				.getDateinyyyy_mm_dd(DateUtils.getDate()));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

		return htMovHis;
	}
	
	
	public boolean saveDoTransferHdrDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_DoTransferHdrDAO.setmLogger(mLogger);
			ht.remove(IDBConstants.CURRENCYID);
			flag = _DoTransferHdrDAO.insertDOTRANSFERHDR(ht);
		} catch (Exception e) {
			MLogger.log(-1, "saveDoTransferHdrDetails() ::  Exception ############### "
					+ e.getMessage());
			throw e;
		}
		return flag;
	}
	
	public boolean  saveDoTransferDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_DoTransferDetDAO.setmLogger(mLogger);
			flag = _DoTransferDetDAO.insertDOTRANSFERDET(ht);
		} catch (Exception e) {

			throw e;
		}
		return flag;
	}

	
	//Codings for PDA start
	
	public String getOpenDoTransferOutBoundOrder(String Plant) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " a.dono,a.CustName,a.Remark1 ";
		String extCond = " ";
		//String extCond = " and isnull(a.pickStaus,'') ";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		try {
			
			
			al =_DoTransferHdrDAO.selectDoTransferHdrForOutBound(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("dono"));
					xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils.replaceCharacters2SendPDA(map
							.get("CustName").toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils.replaceCharacters2SendPDA(map
							.get("Remark1").toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}
	
	
	public String getDoOutBoundOrderItemDetails(String Plant, String dono,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList al = null;
		DoDetDAO dodetDao = new DoDetDAO();
		dodetDao.setmLogger(mLogger);
		String qtyPick="";

		String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,isnull(QTYIS,0) QTYISSUE,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String extCond = " lnstat <> 'C' ";
		if (!isReceived) {
			extCond = extCond + " and PickStatus <> 'C'  ";  //here P is dummy value 
		}
		
		extCond = extCond + " ORDER BY ITEM ";
		
		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		//
		try {
			al = _DoTransferDetDAO.selectDoTransferDet(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map
							.get("DONO"));
					xmlStr += XMLUtils.getXMLNode("dolnno", (String) map
							.get("DOLNNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils.replaceCharacters2SendPDA(map
							.get("ITEMDESC").toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					qtyPick=_DoTransferDetDAO.getDoTransferPickQty(Plant, (String) map.get("DONO"), (String) map.get("ITEM"),(String) map.get("DOLNNO"));
					
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum(qtyPick));
					
					xmlStr += XMLUtils.getXMLNode("qtyissue", StrUtils.formatNum((String) map
							.get("QTYISSUE")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", "");
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
		}
		return xmlStr;
	}
	
	public String getOpenDoTransferoutBoundOrderSupDetails(String Plant, String OrdNum) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " a.dono,a.CustName,a.Remark1 ";
		String extCond = " AND a.DONO = '" + OrdNum
				+ "' and isnull(a.status, '') <> 'C'";
		Hashtable ht = new Hashtable();
		ht.put("PLANT", Plant);

		try {
			al = _DoTransferHdrDAO.selectDoTransferHdrForOutBound(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils.replaceCharacters2SendPDA(map
							.get("CustName").toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils.replaceCharacters2SendPDA( map
							.get("Remark1").toString()));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}
	
	
	public String getDoTransferBatchDetails(String plant, String item,
		 String fromLoc, String batch)
			throws Exception {
		String result = "";
		String result1 = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		java.sql.Connection con = null;

		try {
			
			
			InvMstDAO _InvMstDAO =new InvMstDAO(); 
		        _InvMstDAO.setmLogger(mLogger);
			alData = _InvMstDAO.getOutBoundPickingBatchByWMS(plant, item,fromLoc,batch);
			if (alData.size() > 0) {
				int totalValue = 0;
				result1 += XMLUtils.getXMLHeader();
				result1 += XMLUtils.getStartNode("BatchDetails total='"
						+ String.valueOf(alData.size()) + "'");
				for (Map<String, String> hashMap : alData) {

						result1 += XMLUtils.getStartNode("record");
						result1 += XMLUtils.getXMLNode("batchId", hashMap
								.get("batch"));
						result1 += XMLUtils.getXMLNode("qty", hashMap
								.get("qty"));
						result1 += XMLUtils.getEndNode("record");
					
				}
			
				
				
				result1 += XMLUtils.getEndNode("BatchDetails");
			} else {
				result1 = XMLUtils.getXMLMessage(1, "Batch Detiails Not found");
			}

			return result1;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	
	public String getDoTransferLocDetails(String plant,String user) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		LocMstDAO _LocMstDAO  =new LocMstDAO ();
		int totalValue = 0;

		//
		try {
			al = _LocMstDAO.getLocByWMS(plant,user);
			
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocList total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("loc", (String) map
							.get("loc"));
					xmlStr += XMLUtils.getXMLNode("locdesc", strUtils.replaceCharacters2SendPDA((String) map
							.get("locdesc").toString()));
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("LocList");
			}


		} catch (Exception e) {
			

		}

		return xmlStr;
	}
	
	//start code by Bruhan for random outbound transfer on 19 june 2013
	
	public String getRandomOutBoundTransferOrderItemDetails(String Plant, String dono,
				Boolean isReceived) {
		
		String xmlStr = "",qtyPick="";
		ArrayList al = null;
		DoDetDAO dodetDao = new DoDetDAO();
		dodetDao.setmLogger(mLogger);
		
		String query = " DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE ";
		
		//String query = " ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYIS ";
			String extCond = " lnstat <> 'C' ";
			if (!isReceived) {
				extCond = extCond + " and PickStatus <> 'C' ";
			}

			extCond = extCond + " GROUP BY DONO,ITEM,ITEMDESC,UNITMO ORDER BY ITEM ";

			Hashtable ht = new Hashtable();
			ht.put("DONO", dono);
			ht.put("PLANT", Plant);
		//
		try {
			al = _DoTransferDetDAO.selectDoTransferDet(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map
							.get("DONO"));
					
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils.replaceCharacters2SendPDA(map
							.get("ITEMDESC").toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					qtyPick=_DoTransferDetDAO.getDoTransferPickQty_Random(Plant, (String) map.get("DONO"), (String) map.get("ITEM"));
					
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum(qtyPick));
					
					xmlStr += XMLUtils.getXMLNode("qtyissue", StrUtils.formatNum((String) map
							.get("QTYISSUE")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", "");
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
		}
		return xmlStr;
		
			
		}
		
	
	public ArrayList getDOTransferDetDetailsRandom(String Plant,
				String DONO,String item) {
	    	ArrayList al = null;

			try {
				al = _DoTransferDetDAO.getDOTransferDetDetailsRandom(Plant, DONO, item);
				 
			} catch(Exception e ){
	    		this.mLogger.exception(this.printLog, "", e);
	    	}
			
	    return al;
	    }
	
	
	public String process_DoTransferPickingForPDA_Random(Map obj) {
			boolean flag = false;
			UserTransaction ut = null;
			try {
				ut = com.track.gates.DbBean.getUserTranaction();
				ut.begin();
				flag = process_Wms_DoTransferPicking_Random(obj);
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
				String xmlStr = XMLUtils.getXMLMessage(0,
						"Error in picking Product : " + obj.get(IConstants.ITEM)
								+ " Order");
				return xmlStr;
			}

			String xmlStr = "";
			if (flag == true) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : "
						+ obj.get(IConstants.ITEM) + "  picked successfully!");
			} else {
				xmlStr = XMLUtils.getXMLMessage(0, "Error in picking Product : "
						+ obj.get(IConstants.ITEM) + " Order");
			}
			return xmlStr;
		}
	
	
	private boolean process_Wms_DoTransferPicking_Random(Map map) throws Exception {
			boolean flag = false;

			WmsTran tran = new WmsDoTransferPickingRandom();
			((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
			flag = tran.processWmsTran(map);
			return flag;
		}
	

	//end code by Bruhan for random outbound transfer on 19 june 2013
}
