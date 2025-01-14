package com.track.db.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
//---Added by Bruhan on March 28 2014, Description:To WmsIBRECEIVEReverse
import com.track.tran.WmsIBRECEIVEReversal;
import com.track.tran.WmsProductReceiveMaterial;
//---End Added by Bruhan on March 28 2014, Description:To WmsIBRECEIVEReverse
import com.track.tran.WmsReceiveMaterial;
import com.track.tran.WmsReceiveMaterialRandom;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class POUtil {
	PoHdrDAO _PoHdrDAO = null;
	PoDetDAO _PoDetDAO = null;
	private POBeanDAO poBeanDAO = new POBeanDAO();
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.POUtil_PRINTPLANTMASTERLOG;
	private boolean printQuery = MLoggerConstant.POUtil_PRINTPLANTMASTERQUERY;
	
	StrUtils strUtils = new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public POUtil() {
		_PoDetDAO = new PoDetDAO();
		_PoHdrDAO = new PoHdrDAO();

	}

	public Boolean removeRow(String plant, String pono, String loginUser) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			Hashtable ht= new Hashtable();
			ht.put("PLANT", plant);
			ht.put("PONO", pono);
			movHisDao.setmLogger(mLogger);
			_PoHdrDAO.setmLogger(mLogger);
			_PoDetDAO.setmLogger(mLogger);
			ut = DbBean.getUserTranaction();
			ut.begin();
			Boolean removeHeader = this._PoHdrDAO.removeOrder(plant, pono);
			Boolean removeDetails = this._PoDetDAO.removeOrderDetails(plant,pono);
 		//delete & insert podet multi remarks 
            if(removeDetails){
               
                Hashtable htPoRemarksDel = new Hashtable();
                htPoRemarksDel.put(IDBConstants.PLANT,plant);
                htPoRemarksDel.put(IDBConstants.PODET_PONUM, pono);
				if(this._PoDetDAO.isExisitPoMultiRemarks(htPoRemarksDel))
                {
                   flag = this._PoDetDAO.deletePoMultiRemarks(htPoRemarksDel);
                }
            }
           //delete podet multi remarks end
			 boolean isExistsinRecv= new  RecvDetDAO().isExisit(ht, (String) ht.get(IDBConstants.PLANT));
			 if(isExistsinRecv){
				Boolean removeRecvDetails = new RecvDetDAO().deleteRecvDet(ht); // in case of non Stock Item
			}
			
			Hashtable htMovHis = createMoveHisHashtable(plant, pono, loginUser);
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

	private Hashtable createMoveHisHashtable(String plant, String pono,
			String loginUser) {
		Hashtable<String, String> htMovHis = new Hashtable<String, String>();
		htMovHis.put(IDBConstants.PLANT, plant);
		htMovHis.put("DIRTYPE", "DELETE_PURCHASE_ORDER");
		htMovHis.put(IDBConstants.ITEM, " ");
		htMovHis.put("BATNO", " ");
		htMovHis.put("ORDNUM", pono);
		htMovHis.put("MOVTID", " ");
		htMovHis.put("RECID", " ");
		htMovHis.put(IDBConstants.LOC, " ");
		htMovHis.put(IDBConstants.CREATED_BY, loginUser);
		htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		return htMovHis;
	}

	/**
	 * @method : getCountOpenPO4Item(String aItem)
	 * @description : Find the count of new / open PO nos of given item.
	 * @param ht
	 * @return
	 * @throws Exception
	 */

	public int getCountOpenPO4Item(String aItem) throws Exception {
		int iCntPO = 0;
		try {
			poBeanDAO.setmLogger(mLogger);
			iCntPO = poBeanDAO.getCountOpenPO4Item(aItem);
		} catch (Exception e) {
			System.out.println("Exception :: " + e.toString());
		}
		return iCntPO;
	}

	/**
	 * @method : isValidOpenPO(String aPONO)
	 * @description : check the given po is valid and it's open to receive
	 * @param aPONO
	 * @return
	 * @throws Exception
	 */
	public int getCountOpenItems4PO(String aPONO) throws Exception {
		int countOpenItems4PO = 0;
		try {
			poBeanDAO.setmLogger(mLogger);
			countOpenItems4PO = poBeanDAO.getCountOpenItems4PO(aPONO);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return countOpenItems4PO;
	}

	/**
	 * @method : getCountOpenPO4Vendor(String aVednNo)
	 * @description :
	 * @param aPONO
	 * @return
	 * @throws Exception
	 */
	public int getCountOpenPO4Vendor(String aVednNo) throws Exception {
		int countOpenItems4PO = 0;
		try {
			poBeanDAO.setmLogger(mLogger);
			countOpenItems4PO = poBeanDAO.getCountOpenPO4Vendor(aVednNo);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return countOpenItems4PO;
	}

	/**
	 * @method : getPONO4Vendor(String aVendNo)
	 * @description : get PONO for the given vendor number
	 * @param aVendNo
	 * @return
	 * @throws Exception
	 */
	public String getPONO4Vendor(String aVendNo) throws Exception {
		String sPONO = "";
		try {
			poBeanDAO.setmLogger(mLogger);
			sPONO = poBeanDAO.getPONO4Vendor(aVendNo);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sPONO;
	}

	/**
	 * 
	 * @param listQryFields
	 * @param ht
	 * @return
	 * @throws Exception
	 */
	public ArrayList queryPODET_Col(List listQryFields, Hashtable ht,
			String extraCondition) throws Exception {
		ArrayList listQry = new ArrayList();
		try {
			poBeanDAO.setmLogger(mLogger);
			listQry = poBeanDAO.queryPODET_Col(listQryFields, ht,
					extraCondition);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}

	public ArrayList getPOList() throws Exception {
		ArrayList listQry = new ArrayList();
		try {
			poBeanDAO.setmLogger(mLogger);
			listQry = poBeanDAO.getPOList();
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
	}

	// //// PDA Methods	// //////////////////////////
	public String getOpenItemDetailsPDA(String aPONO) {
		String xmlStr = "";
		try {
			poBeanDAO.setmLogger(mLogger);
			ArrayList itemList = poBeanDAO.getOpenItemDetails4PO(aPONO);
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Item total ='"
					+ String.valueOf(itemList.size()) + "'");
			for (int i = 0; i < itemList.size(); i++) {
				ArrayList itemLine = (ArrayList) itemList.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("ITEM", (String) itemLine.get(0));
				xmlStr += XMLUtils.getXMLNode("DESC", StrUtils
						.replaceCharacters2Send((String) itemLine.get(1)));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("Item");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

	/**
	 * @method : getOpenPODetailsPDA(String aItem)
	 * @description : get PO Details for the given item
	 * @param aItem
	 * @return
	 */
	public String getOpenPODetails4PDA(String searchCode, String type) {
		String xmlStr = "";
		try {
			ArrayList poList = new ArrayList();
			List qryFields = new ArrayList();
			qryFields.add(IConstants.PONO); // PONO
			qryFields.add(IConstants.USERFLD3); // VENDCode
			qryFields.add(IConstants.DEL_DATE); // DELIVERY DATE
			// extra condition if any
			String extraCondition = " AND (ISNULL(" + IConstants.LNSTAT
					+ ",'') = 'N' OR ISNULL(" + IConstants.LNSTAT
					+ ",'') = 'O')";

			if (type.equalsIgnoreCase("ITEM")) {
				Hashtable htCondition = new Hashtable();
				htCondition.put(IConstants.ITEM, searchCode);
				poList = this.queryPODET_Col(qryFields, htCondition,
						extraCondition);
			} else if (type.equalsIgnoreCase("VENDOR")) {
				Hashtable htCondition = new Hashtable();
				htCondition.put(IConstants.USERFLD2, searchCode);
				poList = this.queryPODET_Col(qryFields, htCondition,
						extraCondition);
			}

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("PO total ='"
					+ String.valueOf(poList.size()) + "'");

			for (int i = 0; i < poList.size(); i++) {
				ArrayList itemLine = (ArrayList) poList.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("PONO", (String) itemLine.get(0));
				xmlStr += XMLUtils.getXMLNode("VENDNO", StrUtils
						.replaceCharacters2Send((String) itemLine.get(1)));
				xmlStr += XMLUtils.getXMLNode("DELDATE", (String) itemLine
						.get(2));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("PO");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

	/**
	 * @method : getPODetails2RecvPDA(String aItem, String aPONO, String
	 *         aVendNo)
	 * @decription : get the
	 * @param aItem
	 * @param aPONO
	 * @param aVendNo
	 * @return
	 */
	public String getPODetails2RecvPDA(String aItem, String aPONO,
			String aVendNo) {
		String xmlStr = "";

		String sItem = "";
		String sPONO = "";
		String sVendNo = "";
		ArrayList poList_col = new ArrayList();
		List qryFields = new ArrayList();

		qryFields.add(IConstants.PONO); // PONO
		qryFields.add(IConstants.ITEM); // ITEM
		qryFields.add(IConstants.USERFLD1); // ITEMDESCRIPTION
		qryFields.add(IConstants.USERFLD2); // VENDCODE
		qryFields.add(IConstants.USERFLD3); // VENDNAME
		qryFields.add(IConstants.UNITMO); // UNITMO
		qryFields.add(IConstants.QTY_ORDER); // QTY ORDER
		qryFields.add(IConstants.QTY_RECV); // QTY RECEIVE
		String extraCondition = " AND (ISNULL(" + IConstants.LNSTAT
				+ ",'') = 'N' OR ISNULL(" + IConstants.LNSTAT + ",'') = 'O')";
		Hashtable htCondition = new Hashtable();
		try {
			if (aItem.length() > 0 && aPONO.length() > 0
					&& aVendNo.length() > 0) {

				htCondition.clear();
				htCondition.put(IConstants.ITEM, aItem);
				htCondition.put(IConstants.PONO, aPONO);
				htCondition.put(IConstants.USERFLD2, aVendNo);
			} else if (aItem.length() > 0 && aPONO.length() <= 0
					&& aVendNo.length() <= 0) {

				htCondition.clear();
				htCondition.put(IConstants.ITEM, aItem);
			} else if (aItem.length() > 0 && aPONO.length() > 0
					&& aVendNo.length() <= 0) {

				htCondition.clear();
				htCondition.put(IConstants.ITEM, aItem);
				htCondition.put(IConstants.PONO, aPONO);
			} else if (aItem.length() <= 0 && aPONO.length() > 0
					&& aVendNo.length() <= 0) {

				htCondition.clear();
				htCondition.put(IConstants.PONO, aPONO);
			} else if (aItem.length() <= 0 && aPONO.length() > 0
					&& aVendNo.length() > 0) {

				htCondition.clear();
				htCondition.put(IConstants.PONO, aPONO);
				htCondition.put(IConstants.USERFLD2, aVendNo);
			} else if (aItem.length() <= 0 && aPONO.length() <= 0
					&& aVendNo.length() > 0) {

				htCondition.clear();
				htCondition.put(IConstants.USERFLD2, aVendNo);
			}
			poList_col = this.queryPODET_Col(qryFields, htCondition,
					extraCondition);
			for (int i = 0; i < poList_col.size(); i++) {
				ArrayList arrLine = (ArrayList) poList_col.get(i);

				BigDecimal bBalQty = new BigDecimal((String) arrLine.get(6))
						.subtract(new BigDecimal((String) arrLine.get(7)));

				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("PONO", (String) arrLine.get(0));
				xmlStr += XMLUtils.getXMLNode("ITEM", (String) arrLine.get(1));
				xmlStr += XMLUtils.getXMLNode("ITEMDESC", StrUtils
						.replaceCharacters2Send((String) arrLine.get(2)));
				xmlStr += XMLUtils.getXMLNode("VENDORCODE", (String) arrLine
						.get(3));
				xmlStr += XMLUtils.getXMLNode("VENDORNAME", StrUtils
						.replaceCharacters2Send((String) arrLine.get(4)));
				xmlStr += XMLUtils
						.getXMLNode("UNITMO", (String) arrLine.get(5));
				xmlStr += XMLUtils.getXMLNode("QTY_ORDER", (String) arrLine
						.get(6));
				xmlStr += XMLUtils.getXMLNode("QTY_RECV", (String) arrLine
						.get(7));
				xmlStr += XMLUtils
						.getXMLNode("QTY_BALANCE", bBalQty.toString());
				xmlStr += XMLUtils.getEndNode("record");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr.trim();
	}

	/**
	 * @method : isOpenItem4PONO(String aPONO,String aItem)
	 * @description : check the givenn item and pono is valid to receive
	 * @param aPONO
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public boolean isOpenItem4PONO(String aPONO, String aItem) throws Exception {
		boolean openItem = false;
		try {
			Hashtable htCondition = new Hashtable();
			htCondition.clear();
			if (StrUtils.fString(aItem).length() > 0)
				htCondition.put(IConstants.ITEM, aItem);
			if (StrUtils.fString(aPONO).length() > 0)
				htCondition.put(IConstants.PONO, aPONO);

			List qryFields = new ArrayList();
			qryFields.add(IConstants.PONO); // PONO
			String extraCondition = " AND (ISNULL(" + IConstants.LNSTAT
					+ ",'') = 'N' OR ISNULL(" + IConstants.LNSTAT
					+ ",'') = 'O')";
			ArrayList poList_col = this.queryPODET_Col(qryFields, htCondition,
					extraCondition);
			if (poList_col.size() > 0)
				openItem = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return openItem;
	}

	public boolean savePoHdrDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {
			_PoHdrDAO.setmLogger(mLogger);
			flag = _PoHdrDAO.insertPoHdr(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			//throw e;
       throw new Exception("incoming order created already");
		}

		return flag;
	}

	public boolean updatePoHdr(Hashtable ht) throws Exception {

		boolean flag = false;

		Hashtable htCond = new Hashtable();
		htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put(IDBConstants.POHDR_PONUM, (String) ht.get(IDBConstants.POHDR_PONUM));
		try {

			StringBuffer updateQyery = new StringBuffer("set ");
			updateQyery.append(IDBConstants.POHDR_JOB_NUM + " = '"
					+ (String) ht.get(IDBConstants.POHDR_JOB_NUM) + "'");
			updateQyery.append("," + IDBConstants.POHDR_CONTACT_NUM + " = '"
					+ (String) ht.get(IDBConstants.POHDR_CONTACT_NUM) + "'");
			updateQyery.append("," + IDBConstants.POHDR_COL_DATE + " = '"
					+ (String) ht.get(IDBConstants.POHDR_COL_DATE) + "'");
		        updateQyery.append("," + IDBConstants.POHDR_DELDATE + " = '"
		                    + (String) ht.get(IDBConstants.POHDR_DELDATE) + "'");
			updateQyery.append("," + IDBConstants.POHDR_REMARK1 + " = '"
					+ (String) ht.get(IDBConstants.POHDR_REMARK1) + "'");
			updateQyery.append("," + IDBConstants.POHDR_REMARK2 + " = '"
					+ (String) ht.get(IDBConstants.POHDR_REMARK2) + "'");
			updateQyery.append("," + IDBConstants.POHDR_REMARK3 + " = '"
					+ (String) ht.get(IDBConstants.POHDR_REMARK3) + "'");
		        updateQyery.append("," + IDBConstants.POHDR_CUST_CODE + " = '"
		                    + (String) ht.get(IDBConstants.POHDR_CUST_CODE) + "'");
			updateQyery.append("," + IDBConstants.POHDR_CUST_NAME + " = '"
					+ (String) ht.get(IDBConstants.POHDR_CUST_NAME) + "'");
			updateQyery.append(","+ IDBConstants.POHDR_PERSON_INCHARGE
							+ " = '"
							+ (String) ht.get(IDBConstants.POHDR_PERSON_INCHARGE)
							+ "'");
			updateQyery.append("," + IDBConstants.POHDR_ADDRESS + " = '"
					+ (String) ht.get(IDBConstants.POHDR_ADDRESS) + "'");
			updateQyery.append("," + IDBConstants.POHDR_ADDRESS2 + " = '"
					+ (String) ht.get(IDBConstants.POHDR_ADDRESS2) + "'");
			updateQyery.append("," + IDBConstants.POHDR_ADDRESS3 + " = '"
					+ (String) ht.get(IDBConstants.POHDR_ADDRESS3) + "'");
			updateQyery.append("," + IDBConstants.POHDR_COL_TIME + " = '"
					+ (String) ht.get(IDBConstants.POHDR_COL_TIME) + "'");
			updateQyery.append("," + IDBConstants.ORDERTYPE + " = '"
					+ (String) ht.get(IDBConstants.ORDERTYPE) + "'");
			updateQyery.append("," + IDBConstants.POHDR_GST + " = '"
					+ (String) ht.get(IDBConstants.POHDR_GST) + "'");
			updateQyery.append("," + IDBConstants.CURRENCYID + " = '"
					+ (String) ht.get(IDBConstants.CURRENCYID) + "'");
			
			
		    updateQyery.append("," + IDBConstants.SHIPPINGCUSTOMER + " = '"
			        + (String) ht.get(IDBConstants.SHIPPINGCUSTOMER) + "'");
			updateQyery.append("," + IDBConstants.SHIPPINGID+ " = '"
			        + (String) ht.get(IDBConstants.SHIPPINGID) + "'");
			updateQyery.append("," + IDBConstants.ORDERDISCOUNT + " = '"
			        + (String) ht.get(IDBConstants.ORDERDISCOUNT) + "'");
			updateQyery.append("," + IDBConstants.SHIPPINGCOST + " = '"
			        + (String) ht.get(IDBConstants.SHIPPINGCOST) + "'");
			updateQyery.append("," + IDBConstants.INCOTERMS + " = '"
			        + (String) ht.get(IDBConstants.INCOTERMS) + "'");
		    updateQyery.append("," + IDBConstants.ORDSTATUSID + " = '"
                    + (String) ht.get(IDBConstants.ORDSTATUSID) + "'");
			updateQyery.append("," + IDBConstants.LOCALEXPENSES + " = '"
                    + (String) ht.get(IDBConstants.LOCALEXPENSES) + "'");
			updateQyery.append("," + IDBConstants.PAYMENTTYPE + " = '"
                    + (String) ht.get(IDBConstants.PAYMENTTYPE) + "'");
			updateQyery.append("," + IDBConstants.POHDR_DELIVERYDATEFORMAT + " = '"
                    + (String) ht.get(IDBConstants.POHDR_DELIVERYDATEFORMAT) + "'");
			updateQyery.append("," + IDBConstants.TAXTREATMENT + " = '"
                    + (String) ht.get(IDBConstants.TAXTREATMENT) + "'");
			updateQyery.append("," + IDBConstants.PURCHASE_LOCATION + " = '"
                    + (String) ht.get(IDBConstants.PURCHASE_LOCATION) + "'");
			updateQyery.append("," + IDBConstants.REVERSECHARGE + " = '"
                    + (String) ht.get(IDBConstants.REVERSECHARGE) + "'");
			updateQyery.append("," + IDBConstants.GOODSIMPORT + " = '"
                    + (String) ht.get(IDBConstants.GOODSIMPORT) + "'");

			_PoHdrDAO.setmLogger(mLogger);
			flag = _PoHdrDAO.updatePO(updateQyery.toString(), htCond,"");//" AND STATUS ='N' ");
			if(flag){
				 updateQyery = new StringBuffer("set ");
				 updateQyery.append(" CNAME = '"+ (String) ht.get(IDBConstants.POHDR_CUST_NAME) + "'");
				 boolean isExistsinRecv= new  RecvDetDAO().isExisit(htCond, (String) ht.get(IDBConstants.PLANT));
				 if(isExistsinRecv){
				 flag = new RecvDetDAO().update(updateQyery.toString(), htCond,"",(String) ht.get(IDBConstants.PLANT));
				 }
				}


		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Processed Inbound Order Cannot be modified");
		}

		return flag;
	}

	public ArrayList getPoDetDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoDet(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getPoHdrDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoHdrDAO.setmLogger(mLogger);
			al = _PoHdrDAO.selectPoHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getPoHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoHdrDAO.setmLogger(mLogger);
			al = _PoHdrDAO.selectPoHdr(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getPoHdrDetailsReceiving(String query, Hashtable ht,
			String extCond) throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoHdrDAO.setmLogger(mLogger);
			al = _PoHdrDAO.selectPoHdrReciving(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getSupplierHdrDetails(String query, Hashtable ht,
			String extCond) throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoHdrDAO.setmLogger(mLogger);
			al = _PoHdrDAO.selectSupplierHdrReciving(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public boolean savePoDetDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {

		
			_PoDetDAO.setmLogger(mLogger);
			flag = _PoDetDAO.insertPoDet(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public String getNextPO() throws Exception {

		String maxPo = "";
		try {
			_PoHdrDAO.setmLogger(mLogger);
			String maxPO = _PoHdrDAO.getMaxPO("SIS");

		} catch (Exception e) {

			throw e;

		}

		return maxPo;
	}



	public String getItem_Details_xml(String plant, String item) {

		String xmlStr = "", pono = "", polnno = "", loc = "", itemDesc = "";
		ArrayList alPoDet = null;
		ArrayList alPoHdr = null;

		Hashtable htPoDet = new Hashtable();
		String queryPoDet = " pono,polnno,item";
		// condition
		htPoDet.put("item", item);
		htPoDet.put("plant", plant);

		try {
			alPoDet = getPoDetDetails(queryPoDet, htPoDet);

			if (alPoDet.size() > 0) {
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				for (int i = 0; i < alPoDet.size(); i++) {

					Map map1 = (Map) alPoDet.get(i);
					pono = (String) map1.get("pono");
					polnno = (String) map1.get("polnno");
					item = (String) map1.get("item");
					itemDesc = itemMstDAO.getItemDesc(plant, item);
					

				}

				Hashtable htPoHdr = new Hashtable();
				String queryPoHdr = "pono,jobNum,custName";
				// condition
				htPoHdr.put("pono", pono);
				htPoHdr.put("plant", plant);
				// query

				// condition
				alPoHdr = getPoHdrDetails(queryPoHdr, htPoHdr);

				if (alPoHdr.size() > 0) {

					xmlStr = XMLUtils.getXMLHeader();
					xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

					for (int i = 0; i < alPoHdr.size(); i++) {

						Map map = (Map) alPoHdr.get(i);
						xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
						xmlStr = xmlStr
								+ XMLUtils.getXMLNode("description", "");
						xmlStr = xmlStr + XMLUtils.getXMLNode("plant", plant);
						xmlStr = xmlStr + XMLUtils.getXMLNode("item", item);
						xmlStr = xmlStr
								+ XMLUtils.getXMLNode("itemDesc", itemDesc);
						xmlStr = xmlStr + XMLUtils.getXMLNode("loc", loc);
						xmlStr = xmlStr
								+ XMLUtils.getXMLNode("pono", (String) map
										.get("pono"));
						xmlStr = xmlStr + XMLUtils.getXMLNode("poLno", polnno);
						xmlStr = xmlStr
								+ XMLUtils.getXMLNode("jobNum", (String) map
										.get("jobNum"));
						xmlStr = xmlStr
								+ XMLUtils.getXMLNode("custName", (String) map
										.get("custName"));

					}
					xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");
				} else {
					xmlStr = XMLUtils.getXMLMessage(1,
							" Order Details not found for Product : " + item);
				}

			} else {

				xmlStr = XMLUtils.getXMLMessage(1,
						" Order not created for Product : " + item);
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
			return xmlStr;
		}

		return xmlStr;
	}

	public String getItemDetails(String Plant, String item) {

		String xmlStr = "";
		ArrayList al = null;

		String query = "pono,polnno,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc";
		String extCond = " lnstat <> 'C'";
		Hashtable ht = new Hashtable();
		ht.put("item", item);
		//
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoDet(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("pono"));
					xmlStr += XMLUtils.getXMLNode("polnno", (String) map
							.get("polnno"));
					xmlStr += XMLUtils.getXMLNode("qtyor", (String) map
							.get("qtyor"));
					xmlStr += XMLUtils.getXMLNode("qtyrc", (String) map
							.get("qtyrc"));
					xmlStr += XMLUtils.getXMLNode("vendor", getVendorName(
							"SIS", (String) map.get("pono")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

	public String getItemDetailsByPO(String Plant, String PoNo) {

		String xmlStr = "";
		ArrayList al = null;

		String query = "pono,polnno,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc";
		String extCond = " lnstat <> 'C'";
		Hashtable ht = new Hashtable();
		ht.put("PONO", PoNo);
		ht.put("PLANT", Plant);
		//
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoDet(query, ht, extCond);
			MLogger.log(0, "Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("pono"));
					xmlStr += XMLUtils.getXMLNode("polnno", (String) map
							.get("polnno"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", new ItemMstDAO()
							.getItemDesc(Plant, (String) map.get("item")));
					xmlStr += XMLUtils.getXMLNode("qtyor", (String) map
							.get("qtyor"));
					xmlStr += XMLUtils.getXMLNode("qtyrc", (String) map
							.get("qtyrc"));
					xmlStr += XMLUtils.getXMLNode("vendor", getVendorName(
							Plant, (String) map.get("pono")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	public String process_ReceiveMaterial(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.PODET_PONUM) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.PODET_POLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_Receiving(obj);

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: END : Transction returned : " + flag);
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
					"Error in receiving item : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  received successfully!");

		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in receiving Product : "
					+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}
	
	public String process_ReceiveMaterial_Random(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.PODET_PONUM) + " :: "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_Receiving_Random(obj);

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: END : Transction returned : " + flag);
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
					"Error in receiving item : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  received successfully!");

		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in receiving Product : "
					+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}
	public JSONObject process_ReceiveMaterial_Random_STD(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.PODET_PONUM) + " :: "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_Receiving_Random(obj);

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: END : Transction returned : " + flag);
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
	            JSONObject resultJsonInt = new JSONObject();
	            resultJson.put("message", "Error in receiving item : " + obj.get(IConstants.ITEM)
						+ " Order");
	            resultJson.put("status","0");
	            resultJson.put("LineNo",obj.get("LineNo").toString());
	            
	            return resultJson;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  received successfully!");
			
			 resultJson.put("message","Product : "
						+ obj.get(IConstants.ITEM) + "  received successfully!");	
			 resultJson.put("status","1");
			 resultJson.put("LineNo",obj.get("LineNo").toString());

		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in receiving Product : "
					+ obj.get(IConstants.ITEM) + " Order");
			
			 resultJson.put("message","Error in receiving Product : "
						+ obj.get(IConstants.ITEM) + " Order");
			 resultJson.put("status","0");
			 resultJson.put("LineNo",obj.get("LineNo").toString());
		}
		 return resultJson;
	}

	public String process_ReceiveMaterialByWMS(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.PODET_PONUM) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.PODET_POLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_Receiving(obj);
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: END : Transction returned : " + flag);

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
					"Error in receiving item : " + obj.get(IConstants.ITEM)
							+ " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  received successfully!");

		} else {

		}
		return xmlStr;
	}


    public boolean process_ReceiveMaterialByRange(Map obj) throws Exception {
            boolean flag = false;
            UserTransaction ut = null;
            
            try {
            		ut = com.track.gates.DbBean.getUserTranaction();
            		ut.begin();
            		this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
                                + " :: " + SConstant.ORDERNO + " : "
                                + (String) obj.get(IConstants.PODET_PONUM) + " :: "
                                + SConstant.ORDERLN + " : "
                                + (String) obj.get(IConstants.PODET_POLNNO) + " :: "
                                + SConstant.ITEM + " : "
                                + (String) obj.get(IConstants.ITEM) + " :: "
                                + SConstant.BATCH + " : "
                                + (String) obj.get(IConstants.INV_BATCH) + " :: "
                                + SConstant.QTY + " : "
                                + (String) obj.get(IConstants.INV_QTY));

                    flag = this.process_Wms_Receiving(obj);
                    if (flag == true) {
        				DbBean.CommitTran(ut);
        				flag = true;
        			} else {
        				DbBean.RollbackTran(ut);
        				flag = false;
        			}
                    this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
                                    + " :: END : Transction returned : " + flag);


                } catch (Exception e) {
                	DbBean.RollbackTran(ut);
        			flag = false;	
        			throw e;
                     
                }
                      return flag;
                }
                
	public Boolean processMultiReceiveMaterialByWMS(Map obj) throws Exception {
		boolean flag = false;
		//start code by Bruhan for inbound receive bulk fine tunning on 07 august 2013
		UserTransaction ut = null;

		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.PODET_PONUM) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.PODET_POLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_Receiving(obj);
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: END : Transction returned : " + flag);

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = false;	
			throw e;
		}
		return flag;
	}

	private boolean process_Wms_Receiving(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsReceiveMaterial();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}
	
	public Boolean processProductReceiveMaterialByWMS(Map obj) throws Exception {
		boolean flag = false;
		//start code by Bruhan for inbound receive bulk fine tunning on 07 august 2013
		UserTransaction ut = null;
		
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.PODET_PONUM) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.PODET_POLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));
			
			flag = process_Product_Wms_Receiving(obj);
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.RECEVING
					+ " :: END : Transction returned : " + flag);
			
		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = false;	
			throw e;
		}
		return flag;
	}
	
	private boolean process_Product_Wms_Receiving(Map map) throws Exception {
		
		boolean flag = false;
		
		WmsTran tran = new WmsProductReceiveMaterial();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		
		return flag;
	}
private boolean process_Wms_Receiving_Random(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsReceiveMaterialRandom();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}
          


	/**********************  Modification History  ******************************
	 *** Bruhan,Oct 28 2014,Description: To add link when stock item lnstat equals("N") to allow ammend quanity once inbound order has processed
   	 *** Bruhan,Oct 30 2014,Description: To include QTYRECEIVED in link
	 */ 
	public String listPODET(String aPONO, String plant,String rflag) throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		String nonstkflag="";
		try {
			Hashtable htCond = new Hashtable();
			htCond.put("pono", aPONO);
			q = "pono,polnno,lnstat,item,qtyor,isnull(qtyrc,0) as qtyrc,isnull(PRODUCTDELIVERYDATE,'') as PRODUCTDELIVERYDATE,isnull(userfld4,'') as manufacturer,isnull(unitcost,0)*isnull(currencyuseqt,0)unitcost,isnull(comment1,'') as prdRemarks,isnull(prodgst,0) prodgst,UNITMO,CAST(isnull(unitcost,0)*isnull(currencyuseqt,0) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as Convcost";
			_PoDetDAO.setmLogger(mLogger);
			ArrayList al = _PoDetDAO.selectPoDet(q, htCond," plant <> '' order by polnno", plant);

			if (al.size() > 0) {
				PoHdrDAO _PoHdrDAO = new PoHdrDAO();
				ItemMstDAO _ItemMstDAO = new ItemMstDAO();
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_ItemMstDAO.setmLogger(mLogger);
				_InvMstDAO.setmLogger(mLogger);
				for (int i = 0; i < al.size(); i++) {
					Map m = (Map) al.get(i);
					String pono = (String) m.get("pono");
					String polnno = (String) m.get("polnno");
					String item = (String) m.get("item");
					String uom = (String) m.get("UNITMO");
                                        String prdRemarks = (String) m.get("prdRemarks");
				        String desc="",detaildesc="",supplier="",minstkqty="",maxstkqty="",stockonhand="",incomingqty="";
					Map mitem = _ItemMstDAO.getItemDetailDescription(plant, item);
                                        if(mitem.size()>0){
                                            desc = StrUtils.fString((String) mitem.get("itemDesc"));
                                            detaildesc = StrUtils.fString((String) mitem.get("DetailItemDesc"));
                                        //    StkUom  =StrUtils.fString((String) mitem.get("uom"));
                                            nonstkflag = StrUtils.fString((String) mitem.get("NONSTKFLAG"));
                                          
                                        }else{
                                            desc=" "; detaildesc=" ";supplier=""; minstkqty="0.00";
                                        }
                    //to get stockonhand and incoming quantity
                     ItemMstUtil itemutil= new ItemMstUtil();
                    ArrayList listQry = itemutil.getItemList(plant,item,desc," and  isnull(itemmst.userfld1,'N')='N' ");
                    Map mQty=(Map)listQry.get(0);
                    if(mQty.size()>0){
	                    stockonhand=StrUtils.fString((String) mQty.get("STOCKONHAND"));
	                    incomingqty=StrUtils.fString((String) mQty.get("INCOMINGQTY"));
	                    minstkqty=StrUtils.fString((String) mQty.get("MINSTKQTY"));
	                    maxstkqty=StrUtils.fString((String) mQty.get("MAXSTKQTY"));
                    }
                    //to get stockonhand and incoming quantity end
                   
					String orderType = StrUtils.fString(_PoHdrDAO.getOrderType(plant, pono));
					//nonstkflag= _ItemMstDAO.getNonStockFlag(plant, item);
					supplier =(String) m.get("manufacturer");
					
					String lnstat = (String) m.get("lnstat");
					String prodgst=(String) m.get("prodgst");
					String PRODUCTDELIVERYDATE=(String) m.get("PRODUCTDELIVERYDATE");
                  //added by Bruhan to get unitcost based on currency selected on 15 Nov 2013
				  //String ConvertedUnitCost= new PoHdrDAO().getUnitCostBasedOnCurIDSelected(plant, pono,polnno,item);
					/*String ConvertedUnitCost = (String) m.get("unitcost");
				    String cost = StrUtils.currencyWtoutSymbol(ConvertedUnitCost);
				    String qtyOr = StrUtils.formatNum((String) m.get("qtyor"));
					String qtyRc = StrUtils.formatNum((String) m.get("qtyrc"));*/
			       
					String cost = (String) m.get("unitcost");
					String qtyOr = (String) m.get("qtyor");
					String qtyRc =	(String) m.get("qtyrc");
					String convcost =	(String) m.get("Convcost");
					//float unitCostValue="".equals(cost) ? 0.0f :  Float.parseFloat(cost);
					float qtyOrValue="".equals(qtyOr) ? 0.0f :  Float.parseFloat(qtyOr);
					float qtyRcValue="".equals(qtyRc) ? 0.0f :  Float.parseFloat(qtyRc);
					convcost=convcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					/*cost=String.valueOf(unitCostValue);
					if(unitCostValue==0f){
						cost="0.00000";
						convcost="0.00000";
					}else{
						cost=cost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");						
					}*/if(qtyOrValue==0f){
						qtyOr="0.000";
					}else{
						qtyOr=qtyOr.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}if(qtyRcValue==0f){
						qtyRc="0.000";
					}else{
						qtyRc=qtyRc.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					/* Start by Abhilash on 25/09/2019 */
					PlantMstDAO plantMstDAO = new PlantMstDAO();
					String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
					
					double dCost = Double.parseDouble(convcost);
					convcost = StrUtils.addZeroes(dCost, numberOfDecimal);
					/* End by Abhilash on 25/09/2019 */
					
				    String ListCost = getConvertedUnitCostForProduct(plant,pono,item);   
					String listcost = StrUtils.currencyWtoutSymbol(ListCost);
					String IBDiscount=getIBDiscountSelectedItem(plant,pono,item);  
					if (IBDiscount.equalsIgnoreCase("null")	|| IBDiscount == null || IBDiscount == ""){
						IBDiscount="0.00";
					}
				    String discounttype="";
                    int plusIndex = IBDiscount.indexOf("%");
                    if (plusIndex != -1) {
                    IBDiscount = IBDiscount.substring(0, plusIndex);
                         discounttype = "BYPERCENTAGE";
                    }
                    else
                    {
                    	 discounttype = "BYCOST";
                    }
                    link = "<a href=\"" + "modiPODET.jsp?PONO=" + pono +"&RECVSTATUS=" + lnstat
				                    + "&POLNNO=" + polnno + "&ITEM=" + item + "&DESC=" + StrUtils.replaceCharacters2Send(desc) +"&QTY=" + qtyOr+"&QTYRECEIVED=" + qtyRc   
				                    +"&COST=" + new java.math.BigDecimal(convcost).toPlainString() + "&UOM=" + uom + "&RFLAG="+ rflag + "&NONSTOCKFLG="+ nonstkflag+ "&PRDREMARKS="+ prdRemarks +"&MINSTKQTY=" + minstkqty +"&MAXSTKQTY=" + maxstkqty +"&STOCKONHAND=" + stockonhand + "&PRODUCTDELIVERYDATE=" + PRODUCTDELIVERYDATE				                    
				                    +"&INCOMINGQTY=" + incomingqty +"&LISTCOST=" + listcost +"&SUPDIS=" + IBDiscount  +"&PRODGST=" + prodgst +"&DISTYPE=" + discounttype +"&COSTRD=" + new java.math.BigDecimal(cost).toPlainString() +"\">";
                    
                    

					String select = "";

					if (lnstat.equals("N")) {

						select = "<INPUT Type=Checkbox  style=\""
								+ "border:0;background=#dddddd\"" + " name=\""
								+ "chkdDoNo\"" + " value=\"" + item
								+ "\">&nbsp;&nbsp;&nbsp;";

						result += "<tr valign=\"middle\">" +
					
								"<td align=\"center\" width=\"5%\">"  
								+ polnno + "</td>"
								/*+ "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"
								+ sqlBean.formatHTML(orderType) + "</td>"*/
								+ "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"+ link
								+ "<FONT COLOR=\"blue\">"
								+ sqlBean.formatHTML(item) +"</a></font>" + "</td>"
								+ "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"
								+ sqlBean.formatHTML(desc) + "</td>"
                                + "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"
                                + sqlBean.formatHTML(detaildesc) + "</td>"
                                + "<td width=\"7%\" align = center>"
								+ sqlBean.formatHTML(new java.math.BigDecimal(convcost).toPlainString())
								+ "</td>"
                                + "<td width=\"10%\" align = left>&nbsp;&nbsp;&nbsp;"
                                + sqlBean.formatHTML(supplier) + "</td>"
								+ "<td width=\"7%\" align = center>" + qtyOr
								+ "</td>" + "<td width=\"7%\" align = center>"
								+ qtyRc + "</td>"
                                + "<td width=\"6%\" align = left>"
                                + sqlBean.formatHTML(uom) + "</td>"
								+ "<td width=\"5%\" align = center>" + lnstat
								+ "</td>" + "</tr>";
					} else

					{
						select = "<INPUT Type=Checkbox  style=\""
								+ "border:0;background=#dddddd\"" + " name=\""
								+ "chkdDoNo\"" + " value=\"" + item
								+ "\">&nbsp;&nbsp;&nbsp;";

						result += "<tr valign=\"middle\">";
							
						/*if(nonstkflag.equalsIgnoreCase("Y"))
									
						result +=	"<td align=\"center\" width=\"5%\">"+link;
						else*/
							result +=	
							"<td align=\"center\" width=\"5%\">"
								+ polnno + "</td>"
					   /* + "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"
					    + sqlBean.formatHTML(orderType) + "</td>"*/
					    + "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"+ link +
					    "<FONT COLOR=\"blue\">"
					    + sqlBean.formatHTML(item)+ "</a></font>" + "</td>"
					    + "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"
					    + sqlBean.formatHTML(desc) + "</td>"
					    + "<td width=\"15%\" align = left>&nbsp;&nbsp;&nbsp;"
					    + sqlBean.formatHTML(detaildesc) + "</td>"
					    + "<td width=\"7%\" align = center>"
						+ sqlBean.formatHTML(convcost)+ "</td>"
					    + "<td width=\"10%\" align = left>&nbsp;&nbsp;&nbsp;"
					    + sqlBean.formatHTML(supplier) + "</td>"
					    + "<td width=\"7%\" align = center>" + qtyOr
					    + "</td>" + "<td width=\"7%\" align = center>"
					    + qtyRc + "</td>"
					    + "<td width=\"6%\" align = left>"
					    + sqlBean.formatHTML(uom) + "</td>"
					    + "<td width=\"5%\" align = center>" + lnstat
					    + "</td>" + "</tr>";
					}

				}
			} else {
				result += "<tr valign=\"middle\"><td colspan=\"10\" align=\"center\">"
						+ " Please add in Products " + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	public ArrayList listInboundSummaryPODET(String aPONO, String plant)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		ArrayList al = null;
		try {
			String COMP_INDUSTRY =new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
			Hashtable htCond = new Hashtable();
			htCond.put("pono", aPONO);
			if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
			q = "pono,polnno,lnstat,item,( select top 1 itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ) as ItemDesc,( select top 1 PRD_DEPT_ID from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ) as location,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as sname,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";
			else
			q = "pono,polnno,lnstat,item,( select top 1 itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ) as ItemDesc,ISNULL(( select top 1 LOC_ID from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ),'') as location,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as sname,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoDet(q, htCond,
					" plant <> '' and lnstat <> 'C' order by polnno", plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	
	public ArrayList listInboundSummaryProductReceiveDET(String aPONO, String plant)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		ArrayList al = null;
		try {
			String COMP_INDUSTRY =new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
			Hashtable htCond = new Hashtable();
			htCond.put("b.ICRNO", aPONO);
			
			
//			if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//				q = "pono,polnno,lnstat,item,( select top 1 itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ) as ItemDesc,( select top 1 PRD_DEPT_ID from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ) as location,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as sname,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";
//			else
				q = "b.ICRNO as pono,a.LNNO polnno,a.LNSTAT as lnstat,item,( select top 1 itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ) as ItemDesc,ISNULL(( select top 1 LOC_ID from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ),'') as location,isnull(a.QTY,0) as qtyor,isnull(a.QTYRC,0) as qtyrc,isnull(a.NOTE,'') as ref,isnull('','') as sname,isnull(a.UOM,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=a.UOM),1) UOMQTY ";
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectProductreceiveDet(q, htCond,
					" a.plant <> '' and a.lnstat <> 'C' order by LNNO", plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		
		return al;
	}

	//---Modified by Bruhan on March 28 2014, Description: Include Item Description in select statement
	public ArrayList listInboundSummaryReversePODET(String aPONO, String plant)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		ArrayList al = null;
		try {
			Hashtable htCond = new Hashtable();
			htCond.put("a.pono", aPONO);
			q = "a.pono,a.polnno,a.lnstat,a.item,a.itemdesc,isnull(max(a.qtyor),0) as qtyor,isnull(sum(b.recQty),0) as qtyrc,isnull(a.userfld2,'') as ref,isnull(a.userfld3,'') as sname,b.loc,b.batch,isnull(b.expirydat,'') expirydate,isnull(a.UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=a.UNITMO),1) UOMQTY ";
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO
					.selectReversePoDet(
							q,
							htCond,
							" a.plant <> '' and a.polnno=b.lnno and a.pono=b.pono and a.item=b.item  " +
							"group  by a.pono,b.batch,a.polnno,a.item,a.itemdesc,b.loc,b.batch,a.userfld2,a.userfld3,a.lnstat,b.expirydat,a.UNITMO order by a.polnno",
							plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public String getCustCode(String aPlant, String aPONO) throws Exception {

		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("pono", aPONO);

		// String query=" custCode ";
		String query = " ISNULL( " + "custCode" + ",'') as " + "custCode" + " ";
		_PoHdrDAO.setmLogger(mLogger);
		Map m = _PoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custCode");

		return custCode;
	}
	public String getCurrencyID(String aPlant, String aPONO) throws Exception {

		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("pono", aPONO);

		// String query=" custCode ";
		String query = " ISNULL( " + "CURRENCYID" + ",'') as " + "CURRENCYID" + " ";
		_PoHdrDAO.setmLogger(mLogger);
		Map m = _PoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("CURRENCYID");

		return custCode;
	}
	public String getPodetColdata(Hashtable ht,String col) throws Exception {

		String custCode = "";
            	String query = " ISNULL( " + col + ",'') as " + col + " ";
		_PoDetDAO.setmLogger(mLogger);
		Map m = _PoDetDAO.selectRow(query, ht);

		custCode = (String) m.get(col);

		return custCode;
	}
	public String getJobNum(String aPlant, String aPONO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("pono", aPONO);

		String query = " ISNULL( " + "jobNum" + ",'') as " + "jobNum" + " ";
		_PoHdrDAO.setmLogger(mLogger);
		Map m = _PoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("jobNum");

		return custCode;
	}

	public String getVendorName(String aPlant, String aPONO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("pono", aPONO);

		// String query=" custCode ";
		String query = " ISNULL( " + "custName" + ",'') as " + "custName" + " ";
		_PoHdrDAO.setmLogger(mLogger);
		Map m = _PoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custName");

		return custCode;
	}

	public String getOpenInBoundOrder(String Plant) {
		PoHdrDAO pohdrDao = new PoHdrDAO();
		pohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;
		String query = " a.pono as pono ,isnull(a.CustName,'') as CustName,isnull(a.collectiondate,'') orderdate,isnull(a.status,'') status,isnull(a.Remark1,'') as Remark1 ";
		String extCond = " and a.status <> 'C' and ORDER_STATUS <>'Draft'";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		//
		try {
			al = pohdrDao.selectPoHdrNewOrders(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("pono"));

					xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils
							.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("orderdate", (String) map
							.get("orderdate"));
					xmlStr += XMLUtils.getXMLNode("status", (String) map
							.get("status"));

					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
							.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
        
    public String getOpenInBoundOrderByItem(String Plant,String Item) {
            PoHdrDAO pohdrDao = new PoHdrDAO();
            pohdrDao.setmLogger(mLogger);
            String xmlStr = "";
            ArrayList al = null;
            String query = " a.pono as pono ,isnull(a.CustName,'') as CustName,isnull(a.Remark1,'')  as Remark1,  isnull(a.CollectionDate,'') as ORDDATE,b.POLNNO as POLNNO ,b.ITEM as ITEM, b.ITEMDESC as ITEMDESC ,b.QTYOR as QTYOR,isnull(b.QTYRC,0) AS QTYRC,UNITMO ,ISNULL(b.USERFLD2,'') AS REMARKS,isnull(status,'') STATUS";
            String extCond = " and a.status <> 'C' and b.LNSTAT <>'C'and a.pono not in (SELECT pono from ["+Plant+"_POHDR] q where q.pono=a.pono and ORDER_STATUS='Draft' ) order by CAST((SUBSTRING(collectiondate, 7, 4) + SUBSTRING(collectiondate, 4, 2) + SUBSTRING(collectiondate, 1, 2)) AS date )desc ";
            Hashtable ht = new Hashtable();

            ht.put("PLANT", Plant);
            ht.put("ITEM", Item);
            //
            try {
                    al = pohdrDao.selectPoHdrNewOrdersByItem(query, ht, extCond);

                    if (al.size() > 0) {
                            xmlStr += XMLUtils.getXMLHeader();
                            xmlStr += XMLUtils.getStartNode("itemDetails total='"
                                            + String.valueOf(al.size()) + "'");
                            for (int i = 0; i < al.size(); i++) {
                                    Map map = (Map) al.get(i);
                                    xmlStr += XMLUtils.getStartNode("record");
                                    xmlStr += XMLUtils.getXMLNode("pono", (String) map
                                                    .get("pono"));

                                    xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils
                                                    .replaceCharacters2SendPDA(map.get("CustName")
                                                                   .toString()));
                                    
                                
                                xmlStr += XMLUtils.getXMLNode("orderdate", (String) map.get("ORDDATE"));
                                /*xmlStr += XMLUtils.getXMLNode("polnno", (String) map
                                                .get("POLNNO"));
                                xmlStr += XMLUtils.getXMLNode("item", (String) map
                                                .get("ITEM"));

                                xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
                                                .replaceCharacters2SendPDA(map.get("ITEMDESC")
                                                                .toString()));
                                xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
                                                .get("QTYOR")));
                                xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
                                                .get("QTYRC")));
                                xmlStr += XMLUtils.getXMLNode("uom", (String) map
                                                .get("UNITMO"));*/
                                xmlStr += XMLUtils.getXMLNode("status", (String) map
                                        .get("STATUS"));
                                xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
                                                .replaceCharacters2SendPDA(map.get("Remark1")
                                                                .toString()));
                                
                                    xmlStr += XMLUtils.getEndNode("record");
                            }
                            xmlStr += XMLUtils.getEndNode("itemDetails");
                    }

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }

            return xmlStr;
    }

public String getRandomInBoundOrderItemDetails(String Plant, String PoNo) {

		String xmlStr = "";
		ArrayList al = null;
		String query = " POLNNO,ITEM,ITEMDESC,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) AS QTYRC,ISNULL(UNITMO,'') UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String extCond = " lnstat <> 'C' GROUP BY POLNNO,ITEM,ITEMDESC,UNITMO,USERFLD2 ORDER BY POLNNO ";
		Hashtable ht = new Hashtable();
		ht.put("PONO", PoNo);
		ht.put("PLANT", Plant);
		//
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoDet(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));

					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
							.get("QTYRC")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
							.replaceCharacters2SendPDA(map.get("REMARKS")
									.toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
public String getRandomInBoundOrderItemDetailsNext(String Plant, String PoNo,String start,String end) {

	String xmlStr = "";
	ArrayList al = null;
	/*String query = " POLNNO,ITEM,ITEMDESC,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) AS QTYRC,ISNULL(UNITMO,'') UNITMO ,ISNULL(USERFLD2,'') AS REMARKS,ISNULL((SELECT COUNT(*)from ["+Plant+"_PODET] WHERE PONO = '"+PoNo+"' AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO";
	String extCond = " lnstat <> 'C' AND POLNNO >="+start+" and POLNNO<="+end+" GROUP BY POLNNO,ITEM,ITEMDESC,UNITMO,USERFLD2 ORDER BY POLNNO ";*/
	String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY POLNNO)) AS ID,POLNNO,ITEM,ITEMDESC,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) AS QTYRC,ISNULL(UNITMO,'') UNITMO ,ISNULL(USERFLD2,'') AS REMARKS,ISNULL((SELECT COUNT(*) from ["+Plant+"_PODET] WHERE PONO = '"+PoNo+"'  AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO from ["+Plant+"_PODET] WHERE PONO = '"+PoNo+"' AND PLANT = '"+Plant+"' and lnstat <> 'C' GROUP BY POLNNO,ITEM,ITEMDESC,UNITMO,USERFLD2) SELECT * FROM S WHERE ID >= '"+start+"' and ID<='"+end+"' ORDER BY POLNNO";
	String extCond ="";
	Hashtable ht = new Hashtable();
/*	ht.put("PONO", PoNo);
	ht.put("PLANT", Plant);*/
	//
	try {
		_PoDetDAO.setmLogger(mLogger);
	//	al = _PoDetDAO.selectPoDet(query, ht, extCond);
		al = _PoDetDAO.selectPoDetForPage(query, ht, extCond);
		if (al.size() > 0) {
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("itemDetails total='"
					+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("item", (String) map
						.get("ITEM"));

				xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
						.replaceCharacters2SendPDA(map.get("ITEMDESC")
								.toString()));
				xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
						.get("QTYOR")));
				xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
						.get("QTYRC")));
				xmlStr += XMLUtils.getXMLNode("uom", (String) map
						.get("UNITMO"));
				xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
						.replaceCharacters2SendPDA(map.get("REMARKS")
								.toString()));
				xmlStr += XMLUtils.getXMLNode("count", (String) map
						.get("TOTPO"));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("itemDetails");
		}

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return xmlStr;
}
public String getRandomInBoundOrderItemNextItem(String Plant, String PoNo,String start,String end,String itemNo) {

	String xmlStr = "";
	ArrayList al = null;
	//String query = " POLNNO,ITEM,ITEMDESC,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) AS QTYRC,ISNULL(UNITMO,'') UNITMO ,ISNULL(USERFLD2,'') AS REMARKS,ISNULL((SELECT COUNT(*)from ["+Plant+"_PODET] WHERE PONO = '"+PoNo+"' AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO";
	String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY POLNNO)) AS ID,POLNNO,ITEM,ITEMDESC,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) AS QTYRC,ISNULL(UNITMO,'') UNITMO ,ISNULL(USERFLD2,'') AS REMARKS,ISNULL((SELECT COUNT(*) from ["+Plant+"_PODET] WHERE PONO = '"+PoNo+"' AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO from ["+Plant+"_PODET] WHERE PONO = '"+PoNo+"' AND PLANT = '"+Plant+"' and ITEM LIKE '%"+itemNo+"%' and lnstat <> 'C' GROUP BY POLNNO,ITEM,ITEMDESC,UNITMO,USERFLD2) SELECT * FROM S WHERE ID >= '"+start+"' and ID<='"+end+"' ORDER BY POLNNO";
//	String extCond = " lnstat <> 'C' AND POLNNO >="+start+" and POLNNO<="+end+" GROUP BY POLNNO,ITEM,ITEMDESC,UNITMO,USERFLD2 ORDER BY POLNNO ";
	String extCond = "";
	Hashtable ht = new Hashtable();
	/*ht.put("ITEM", itemNo);
	ht.put("PONO", PoNo);
	ht.put("PLANT", Plant);*/
	//
	try {
		_PoDetDAO.setmLogger(mLogger);
		al = _PoDetDAO.selectPoDetForPage(query, ht, extCond);

		if (al.size() > 0) {
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("itemDetails total='"
					+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("item", (String) map
						.get("ITEM"));

				xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
						.replaceCharacters2SendPDA(map.get("ITEMDESC")
								.toString()));
				xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
						.get("QTYOR")));
				xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
						.get("QTYRC")));
				xmlStr += XMLUtils.getXMLNode("uom", (String) map
						.get("UNITMO"));
				xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
						.replaceCharacters2SendPDA(map.get("REMARKS")
								.toString()));
				xmlStr += XMLUtils.getXMLNode("count", (String) map
						.get("TOTPO"));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("itemDetails");
		}

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return xmlStr;
}
public String getItemFromProductMst(String Plant,String start,String end,String itemNo) {

	String xmlStr = "";
	ArrayList al = null;
	String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY IM.ITEM )) AS ID,ISNULL((SELECT COUNT(*)  FROM ["+Plant+"_ITEMMST] as IM, ["+Plant+"_ALTERNATE_ITEM_MAPPING] as AIM  WHERE AIM.ALTERNATE_ITEM_NAME LIKE '%' AND AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT and IM.ITEM LIKE '%"+itemNo+"%'),0) TOTPO , IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,CATLOGPATH,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE, isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1,  isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID,ISNULL(PRD_BRAND_ID,'') BRAND,ISNULL(IM.VINNO,'') VINNO,ISNULL(IM.MODEL,'') MODEL,ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(INVENTORYUOM,'') as INVENTORYUOM FROM ["+Plant+"_ITEMMST] as IM, ["+Plant+"_ALTERNATE_ITEM_MAPPING] as AIM  WHERE AIM.ALTERNATE_ITEM_NAME LIKE '%' AND AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT and IM.ITEM LIKE '%"+itemNo+"%')SELECT * FROM S where ID >='"+start+"' and ID<='"+end+"'  ORDER BY ITEM";
	String extCond = "";
	Hashtable ht = new Hashtable();

	//
	try {
		_PoDetDAO.setmLogger(mLogger);
		al = _PoDetDAO.selectItemForPDA(query, ht, extCond);

		if (al.size() > 0) {
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("itemDetails total='"
					+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", (String) map
						.get("ITEM"));
				xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
						.replaceCharacters2SendPDA(map.get("ITEMDESC")
								.toString()));
				xmlStr += XMLUtils.getXMLNode("uom", (String) map
						.get("STKUOM"));
				xmlStr += XMLUtils.getXMLNode("purchaseuom",(String) map
						.get("PURCHASEUOM"));
				xmlStr += XMLUtils.getXMLNode("salesuom", (String) map
						.get("SALESUOM"));;
				xmlStr += XMLUtils.getXMLNode("inventoryuom", (String) map
						.get("INVENTORYUOM"));
				xmlStr += XMLUtils.getXMLNode("nonstock",(String) map
						.get("ISACTIVE"));
				xmlStr += XMLUtils.getXMLNode("nonstockflag",(String) map
						.get("NONSTKFLAG"));
				xmlStr += XMLUtils.getXMLNode("count", (String) map
						.get("TOTPO"));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("itemDetails");
		}

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return xmlStr;
}
public String getItemFromInvMst(String Plant,String start,String end,String itemNo) {

	String xmlStr = "";
	ArrayList al = null;
	//String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY IM.ITEM )) AS ID,ISNULL((SELECT COUNT(*)  FROM ["+Plant+"_INVMST] as IM, ["+Plant+"_ITEMMST] as AIM  WHERE  IM.LOC LIKE '%%' AND AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT and IM.ITEM LIKE '%"+itemNo+"%'),0) TOTPO ,ISNULL((SELECT QPUOM FROM ["+Plant+"_UOM] as u where u.UOM=AIM.SALESUOM),1)as QPUOM, IM.ITEM ITEM ,ITEMDESC,IM.LOC,IM.QTY,REMARK1,STKQTY,AIM.ISACTIVE, isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID,ISNULL(SALESUOM,'') as SALESUOM FROM ["+Plant+"_INVMST] as IM, ["+Plant+"_ITEMMST] as AIM  WHERE  IM.LOC LIKE '%%' and AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT and IM.ITEM LIKE '%"+itemNo+"%')SELECT * FROM S where ID >='"+start+"' and ID<='"+end+"'   ORDER BY ITEM";
	String query = "with S AS (SELECT distinct  IM.ITEM ITEM ,ITEMDESC,REMARK1,AIM.ISACTIVE, isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID,ISNULL((select u.QPUOM from ["+Plant+"_UOM] as u where u.UOM=SALESUOM),1) as UOMQTY,ISNULL(SALESUOM,'') as SALESUOM,ISNULL(INVENTORYUOM,'') as INVENTORYUOM FROM ["+Plant+"_INVMST] as IM, ["+Plant+"_ITEMMST] as AIM WHERE IM.QTY>0 and IM.LOC LIKE '%%' and AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT and IM.ITEM LIKE '%"+itemNo+"%')Select * from (SELECT (ROW_NUMBER() OVER ( ORDER BY ITEM )) AS ID,ISNULL((SELECT COUNT(*) FROM S),0) TOTPO ,* FROM S) as tbl where ID >='"+start+"' and ID<='"+end+"' ORDER BY ITEM";
	String extCond = "";
	Hashtable ht = new Hashtable();

	//
	try {
		_PoDetDAO.setmLogger(mLogger);
		al = _PoDetDAO.selectItemForPDA(query, ht, extCond);

		if (al.size() > 0) {
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("itemDetails total='"
					+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", (String) map
						.get("ITEM"));
				xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
						.replaceCharacters2SendPDA(map.get("ITEMDESC")
								.toString()));
				xmlStr += XMLUtils.getXMLNode("salesuom", (String) map
						.get("SALESUOM"));
				xmlStr += XMLUtils.getXMLNode("inventoryuom", (String) map
						.get("INVENTORYUOM"));
				xmlStr += XMLUtils.getXMLNode("nonstock",(String) map
						.get("ISACTIVE"));
				xmlStr += XMLUtils.getXMLNode("nonstockflag",(String) map
						.get("NONSTKFLAG"));
				xmlStr += XMLUtils.getXMLNode("count", (String) map
						.get("TOTPO"));
				xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
						.get("UOMQTY"));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("itemDetails");
		}

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return xmlStr;
}
	public String getInBoundOrderItemDetails(String Plant, String PoNo) {

		String xmlStr = "";
		ArrayList al = null;

		String query = " PONO, POLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYRC,0) AS QTYRC,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String extCond = " lnstat <> 'C' ORDER BY ITEM ";
		Hashtable ht = new Hashtable();
		ht.put("PONO", PoNo);
		ht.put("PLANT", Plant);
		//
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoDet(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
				//	xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("PONO"));
					xmlStr += XMLUtils.getXMLNode("polnno", (String) map
							.get("POLNNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));

					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
							.get("QTYRC")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
							.replaceCharacters2SendPDA(map.get("REMARKS")
									.toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
public String getInBoundOrderReceiptItem(String Plant, String PoNo,String Item) {

		String xmlStr = "";
		ArrayList al = null;

		String query = " PONO, POLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYRC,0) AS QTYRC,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String extCond = " lnstat <> 'C' ORDER BY ITEM ";
		Hashtable ht = new Hashtable();
		ht.put("PONO", PoNo);
		ht.put("PLANT", Plant);
		ht.put("ITEM", Item);
		//
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoDet(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("PONO"));
					xmlStr += XMLUtils.getXMLNode("polnno", (String) map
							.get("POLNNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));

					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
							.get("QTYRC")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
							.replaceCharacters2SendPDA(map.get("REMARKS")
									.toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	//Start code modified by Bruhan for base Currency inclusion on Aug 15 2012 
	public ArrayList getInBoundOrderItemDetailsForReport(String Plant,
			String PoNo) {

		String xmlStr = "";
		ArrayList al = null;
		Connection con = null;
		PlantMstUtil _PlantMstUtil = new PlantMstUtil();
		try {
			con = DbBean.getConnection();
			String ConvertUnitCostToOrderCurrency = " ISNULL((CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM [" +Plant+"_PODET] b "
					+ " where PONO = '"
					+ PoNo
					+ "' AND PLANT = '"
					+ Plant
					+ "' and a.POLNNO = b.polnno) AS DECIMAL(20,4)) ),0) ";

			String query = "select a.POLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYRC,0) AS QTYRC, UNITMO , "
					+ ConvertUnitCostToOrderCurrency
					+ " as UnitCost,"
					+ "isnull(Userfld4,'') as ITEMMANUFACTURER,(SELECT CURRENCYID from "
					+ Plant
					+ "_POHDR WHERE PONO ='"
					+ PoNo
					+ "' ) as CurrencyID"
					+ " from ["
					+ Plant
					+ "_PODET] a WHERE PONO = '"
					+ PoNo
					+ "' AND PLANT = '"
					+ Plant + "'";

			_PoDetDAO.setmLogger(mLogger);
			this.mLogger.query(this.printQuery, query.toString());
			al = _PoDetDAO.selectData(con, query.toString());

			return al;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return null;
		}finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

	}
	//End code modified by Bruhan for base Currency inclusion on Aug 15 2012   
	
    public ArrayList getLineDetailsForOrder(String Query, Hashtable ht,String ExtraCond) {

            ArrayList al = null;

            try {
                    _PoDetDAO.setmLogger(mLogger);
                    al = _PoDetDAO.selectPoDet(Query, ht, ExtraCond);

                    return al;

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    return null;
            }

    }

	public String getOpenInBoundOrderSupDetails(String Plant, String OrdNum) {
		PoHdrDAO pohdrDao = new PoHdrDAO();
		pohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " pono,CustName,Remark1 ";
		String extCond = " AND PONO = '" + OrdNum + "' and status <> 'C'";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);

		//
		try {
			al = pohdrDao.selectPoHdr(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("CustName")));
					xmlStr += XMLUtils.getXMLNode("remarks", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("Remark1")));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	public boolean isOpenInboundOrder(String plant, String pono)
			throws Exception {

		boolean flag = false;
		try {

			PoHdrDAO _poHdrDAO = new PoHdrDAO(plant);
			_poHdrDAO.setmLogger(mLogger);

			Hashtable htCondipoHdr = new Hashtable();
			htCondipoHdr.put("PLANT", plant);
			htCondipoHdr.put("pono", pono);

			flag = _poHdrDAO.isExisit(htCondipoHdr, " STATUS ='N'");

			if (!flag) {
				throw new Exception(
						" Processed Inbound Order Cannot be modified");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
        
    public boolean isExitsPoLine(Hashtable ht)
                    throws Exception {

            boolean flag = false;
            try {

                    PoDetDAO _podetDAO = new PoDetDAO();
                    _podetDAO.setmLogger(mLogger);
                    flag = _podetDAO.isExisit(ht);

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }

            return flag;
    }
        
    public ArrayList listInboundSummaryByProd(String item, String plant)
                    throws Exception {
            String q = "";
            String link = "";
            String result = "";
            String where = "";
            ArrayList al = null;
            try {
                    Hashtable htCond = new Hashtable();
                    htCond.put("item", item);
                    q = "pono,polnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as sname,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";
                    _PoDetDAO.setmLogger(mLogger);
                    al = _PoDetDAO.selectPoDet(q, htCond,
                                    " plant <> '' and lnstat <> 'C' order by polnno", plant);
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }

            return al;
    }
    
    //not by Draft - azees
    public ArrayList listInboundSummaryByProdWithext(String item, String plant)
            throws Exception {
    String q = "";
    String link = "";
    String result = "";
    String where = "";
    ArrayList al = null;
    try {
            Hashtable htCond = new Hashtable();
            htCond.put("item", item);
            q = "pono,polnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(userfld2,'') as ref,isnull(userfld3,'') as sname,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";
            _PoDetDAO.setmLogger(mLogger);
            al = _PoDetDAO.selectPoDet(q, htCond,
                            " plant <> '' and lnstat <> 'C' and pono not in (SELECT pono from ["+plant+"_POHDR] q where q.pono=a.pono and ORDER_STATUS='Draft' ) order by polnno", plant);
    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            throw e;
    }

    return al;
}
    
    public Map getPOReceiptHdrDetails(String aplant) throws Exception {
         Map m =  new HashMap();
         PoHdrDAO   dao = new  PoHdrDAO ();
         m=dao.getPOReciptHeaderDetails(aplant);
         return m;
   }
    
    public Map getPOReceiptInvoiceHdrDetails(String aplant) throws Exception {
        Map m =  new HashMap();
        PoHdrDAO   dao = new  PoHdrDAO ();
        m=dao.getPOReciptInvoiceHeaderDetails(aplant);
         return m;
 }
  
    public Map getPurchaseEstimateHdrDetails(String aplant) throws Exception {
        Map m =  new HashMap();
        PoHdrDAO   dao = new  PoHdrDAO ();
        m=dao.getPurchaseEstimateHdrDetails(aplant);
         return m;
 }

 public boolean isNewStatusPONO(String pono, String plant) throws Exception {
		boolean exists = false;
		try {
			String extCon = " STATUS IN ('O','C') ";
			_PoDetDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.IN_PONO, pono);
			if (isExistOntable(ht, extCon))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}

	public boolean isExistPONO(String pono, String plant) throws Exception {
		boolean exists = false;
		try {
			_PoDetDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.IN_PONO, pono);
			if (isExistOntable(ht, ""))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	
	public int getMaxPoLnNo(String pono, String plant) throws Exception {
		int maxPoLnNo = 0;
		try {
			_PoDetDAO.setmLogger(mLogger);
			
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.IN_PONO, pono);
			
			maxPoLnNo = _PoDetDAO.getMaxPoLnNo(ht);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return maxPoLnNo;
	}

	private boolean isExistOntable(Hashtable ht, String extCon) throws Exception {
		boolean exists = false;
		try {
			_PoDetDAO.setmLogger(mLogger);
			if (_PoDetDAO.getCountPoNo(ht, extCon) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}

    public String getConvertedUnitCostForProduct(String aPlant, String aPONO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _PoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrder(aPlant, aPONO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
    
    public String getConvertedUnitCostForProductsCurrency(String aPlant, String currency,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _PoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderCurrency(aPlant, currency,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }

    public String getConvertedUnitCostToLocalCurrency(String aPlant, String aPONO,String unitCost) throws Exception {
        String localCurrencyConvertedUnitCost="";
          try{
               localCurrencyConvertedUnitCost= _PoHdrDAO.getUnitCostCovertedTolocalCurrency(aPlant, aPONO,unitCost);
          }catch(Exception e){
              throw e;
          }
        return localCurrencyConvertedUnitCost;
    }
    
    public String getCurrencyUseQT(String plant,String aPono) throws Exception{
    	String currencyUseQT = "";
    	try{
    		currencyUseQT = _PoHdrDAO.getCurrencyUseQT(plant,aPono);
    	}
    	catch(Exception e ){
    		this.mLogger.exception(this.printLog, "", e);
    	}
    	
    	return currencyUseQT;
    }
    
    public ArrayList getPODetDetailsRandom(String Plant,
			String PONO,String item) {
    	ArrayList al = null;

		try {
			al = _PoDetDAO.getPODetDetailsRandom(Plant, PONO, item);
			 
		} catch(Exception e ){
    		this.mLogger.exception(this.printLog, "", e);
    	}
		
    return al;
    }
    
    /**********************  Modification History  ******************************
     *** Bruhan,Oct 28 2014,Description: To check update quantity less than receiving quantity,add condition for update podetail
     **** Bruhan,March 12 2015,Description: To remove number format in qtyOr,qtyRc
	*/ 
     public boolean updatePoDetDetails(Hashtable ht) throws Exception {
            boolean flag = false;
            boolean updateHdrFlag=false;
            boolean isExitFlag=false;
            RecvDetDAO recvdetDAO = new RecvDetDAO();
            String plant =ht.get("PLANT").toString();
            String cost=ht.get(IDBConstants.PODET_UNITCOST).toString();
            String qty =ht.get(IDBConstants.PODET_QTYOR).toString(); 
			String PRODUCTDELIVERYDATE =ht.get(IDBConstants.PODET_PRODUCTDELIVERYDATE).toString();
            //String PrdRemarks =ht.get(IDBConstants.PODET_COMMENT1).toString(); 
            Hashtable htUpdateRecvdet = new Hashtable();
            htUpdateRecvdet.clear();
            htUpdateRecvdet.put(IDBConstants.PLANT, ht.get("PLANT"));
            htUpdateRecvdet.put(IDBConstants.PODET_PONUM, ht.get("PONO"));
            htUpdateRecvdet.put("POLNNO", ht.get("POLNNO"));
            htUpdateRecvdet.put(IDBConstants.ITEM, ht.get("ITEM"));
            try {
                 String q = "";
			    String qtyOr="",qtyRc="",lnstat="";
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", ht.get("PLANT"));
				htCond.put("pono",  ht.get("PONO"));
				htCond.put("polnno", ht.get("POLNNO"));
				htCond.put("item", ht.get("ITEM"));
				q = " isnull(qtyor,0) qtyor,isnull(qtyrc,0) as qtyrc,isnull(lnstat,'') lnstat ";
				_PoHdrDAO.setmLogger(mLogger);
				_PoDetDAO.setmLogger(mLogger);
				ArrayList al = _PoDetDAO.selectPoDet(q, htCond,	" plant <> ''");
				for (int i = 0; i < al.size(); i++) {
					Map m = (Map) al.get(i);
					qtyOr = (String) m.get("qtyor");
					qtyRc =(String) m.get("qtyrc");
					lnstat = (String) m.get("lnstat");
				}
				 
				if(Double.parseDouble(qty) < Double.parseDouble(qtyRc))
				{
					throw new Exception(" Order Qty entered should not be less than already Received Qty:"+qtyRc);
				}
			     String query="";
			     String updateDoHdr = "";
				 Hashtable htConditoHdr = new Hashtable();
				 htConditoHdr.put("PLANT", ht.get("PLANT"));
				 htConditoHdr.put("pono", ht.get("PONO"));
				//Update podet and check pohdr condition
				 if(lnstat.equals("C") && Double.parseDouble(qtyOr)!=Double.parseDouble(qty))
				    {
				    	query = "set QTYOR=" + qty + ",UNITCOST=" + cost+ ",PRODUCTDELIVERYDATE='" + PRODUCTDELIVERYDATE +"',LNSTAT='O'";
				    	_PoDetDAO.setmLogger(mLogger);
	                    flag = _PoDetDAO.update(query,htUpdateRecvdet,"");
	                    
	                    isExitFlag =_PoHdrDAO.isExisit(htConditoHdr," isnull(STATUS,'') in ('C')");
				    	if (isExitFlag && Double.parseDouble(qtyOr)!=Double.parseDouble(qty)){
							updateDoHdr = "set  status='O'";
							updateHdrFlag=true;
						  
						}
				    }
				    else if(lnstat.equals("O") && Double.parseDouble(qty)==Double.parseDouble(qtyRc))
				    {
				    	query = "set QTYOR=" + qty + ",UNITCOST=" + cost+ ",PRODUCTDELIVERYDATE='" + PRODUCTDELIVERYDATE+ "',LNSTAT='C'";
				    	_PoDetDAO.setmLogger(mLogger);
	                    flag = _PoDetDAO.update(query,htUpdateRecvdet,"");
	                    isExitFlag = _PoDetDAO.isExisit(htConditoHdr,	"lnstat in ('O','N')");
				    	if (isExitFlag)
				    	{
				    		updateHdrFlag=false;
				    	}
						else
						{
							updateDoHdr = "set  status='C' ";
							updateHdrFlag=true;
				    	}
				    }
				    else
				    {
				    	 query = "set QTYOR=" + qty + ",UNITCOST=" + cost+ ",PRODUCTDELIVERYDATE='" + PRODUCTDELIVERYDATE+ "'";
				    	 _PoDetDAO.setmLogger(mLogger);
		                 flag = _PoDetDAO.update(query,htUpdateRecvdet,"");
				    	 updateHdrFlag=false;
				  }
				 			 
                    //Update POHDR
                    if(flag){
        				if(updateHdrFlag==true){
        				 flag = _PoHdrDAO.updatePO(updateDoHdr, htConditoHdr, "");
        			}
                    htUpdateRecvdet.clear();
                    htUpdateRecvdet.put(IDBConstants.PLANT, ht.get("PLANT"));
                    htUpdateRecvdet.put(IDBConstants.PODET_PONUM, ht.get("PONO"));
                    htUpdateRecvdet.put("LNNO", ht.get("POLNNO"));
                    htUpdateRecvdet.put(IDBConstants.ITEM, ht.get("ITEM"));
                    boolean isExists = recvdetDAO.isExisit(htUpdateRecvdet,plant);
                    if(isExists){
                    	query = "set UNITCOST=" + cost+ ",ORDQTY=" + qty;
                    	flag = recvdetDAO.update(query,htUpdateRecvdet,"",plant);
                    }
              } 
            } catch (Exception e) {

                    throw e;
            }
            return flag;
    }
    
    public boolean deleteIBLineDetails(Hashtable ht) throws Exception {
            boolean flag = false;

            try {
                    PoDetDAO _poDetDAO = new PoDetDAO();
                     RecvDetDAO recvdetDAO = new RecvDetDAO();
                    Hashtable htDet = new Hashtable();
                    _poDetDAO.setmLogger(mLogger);

                    htDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
                    htDet.put(IDBConstants.PODET_PONUM, ht.get(IDBConstants.PODET_PONUM));
                    htDet.put(IDBConstants.PODET_POLNNO, ht.get(IDBConstants.PODET_POLNNO));
                    htDet.put(IDBConstants.PODET_ITEM, ht.get(IDBConstants.PODET_ITEM));

                    flag = _poDetDAO.delete(htDet);
                    
                    /*to delete recdet data for nonstock item at delete product
                    boolean recvdetflag1 = false;
                    Hashtable htrevDet = new Hashtable();
                	htrevDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
                	htrevDet.put(IDBConstants.PODET_PONUM, ht.get(IDBConstants.PODET_PONUM));
                    htrevDet.put("LNNO", ht.get(IDBConstants.PODET_POLNNO));
                    htrevDet.put(IDBConstants.PODET_ITEM, ht.get(IDBConstants.PODET_ITEM));

                    recvdetflag1 = recvdetDAO.isExisit(htrevDet, (String)ht.get(IDBConstants.PLANT));
                    if(recvdetflag1){
                    	
                    	
                    	flag = recvdetDAO.deleteRecvDet(htrevDet);
                    }*/
                    
                    //delete podet multi remarks
                    if(flag){
	                   
	                    Hashtable htPoRemarksDel = new Hashtable();
	                    htPoRemarksDel.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
	                    htPoRemarksDel.put(IDBConstants.PODET_PONUM, ht.get(IDBConstants.PODET_PONUM));
	                    htPoRemarksDel.put(IDBConstants.PODET_POLNNO,ht.get(IDBConstants.PODET_POLNNO));
	                    htPoRemarksDel.put(IDBConstants.PODET_ITEM, ht.get(IDBConstants.PODET_ITEM));
	                    flag = _poDetDAO.deletePoMultiRemarks(htPoRemarksDel);
                    }
                   //delete podet multi remarks end
                    if (flag) {

                          
                            try{
                        //To arrange the line no's
                            htDet.remove(IDBConstants.PODET_POLNNO);
                            htDet.remove(IDBConstants.PODET_ITEM);
                            String  sql =  "SET POLNNO =POLNNO-1 ";
                            String extraCond=" AND POLNNO > '" + ht.get(IDBConstants.PODET_POLNNO) + "' ";
                            _poDetDAO.update(sql,htDet,extraCond);
                          
                            //update PODET_REMARKS
	                            Hashtable htPoRemarks = new Hashtable();
	                            htPoRemarks.put(IDBConstants.PODET_PONUM, ht.get(IDBConstants.PODET_PONUM));
	                            String  sqlRemarks =  "SET POLNNO =POLNNO-1 ";
	                            String extraCond1=" AND POLNNO > '" + ht.get(IDBConstants.PODET_POLNNO) + "' ";
	                            _poDetDAO.updatePoMultiRemarks(sqlRemarks,htPoRemarks,extraCond1,(String)ht.get(IDBConstants.PLANT));
                            
                            //update PODET_REMARKS end
                     
                      //To arrange the line no's in recvdet 
                        boolean recvdetflag = false;
                        recvdetflag = recvdetDAO.isExisit(htDet, (String)ht.get(IDBConstants.PLANT));
                      if(recvdetflag)
                      {
                    	  sql =  "SET LNNO =LNNO-1 ";
                    	  extraCond=" AND CAST(LNNO as int) > " + ht.get(IDBConstants.PODET_POLNNO)  + " ";
                    	  recvdetDAO.update(sql,htDet,extraCond,(String)ht.get(IDBConstants.PLANT));
                    	  
                      }      
                            
                            
                }catch(Exception e){}
                            MovHisDAO movhisDao = new MovHisDAO();
                            Hashtable htmovHis = new Hashtable();
                            movhisDao.setmLogger(mLogger);
                            htmovHis.clear();
                            htmovHis.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
                            htmovHis.put("DIRTYPE", "PURCHASER_ORDER_DELETE_PRODUCT");
                            htmovHis.put(IDBConstants.ITEM, (String) ht.get(IDBConstants.DODET_ITEM));
                            htmovHis.put(IDBConstants.MOVHIS_ORDNUM, (String) ht.get(IDBConstants.PODET_PONUM));
                            htmovHis.put(IDBConstants.MOVHIS_ORDLNO, (String) ht.get(IDBConstants.PODET_POLNNO));
                            htmovHis.put(IDBConstants.CREATED_BY, (String) ht.get(IDBConstants.CREATED_BY));
                            htmovHis.put("MOVTID", "");
                            htmovHis.put("RECID", "");
                            htmovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
                            htmovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());  
                            flag = movhisDao.insertIntoMovHis(htmovHis);
                    }

            } catch (Exception e) {
                    throw e;
            }

            return flag;
    }
    
  //---Added by Bruhan on March 28 2014, Description:To WmsIBRECEIVEReverse
    public boolean process_IBRECEIVEReversal(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;

		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Wms_IBRECEIVEReversal(obj);
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = false;			
			throw e;
		}

		return flag;
	}
    
    public boolean process_Wms_IBRECEIVEReversal(Map map) throws Exception {
		boolean flag = false;
		WmsTran tran = new WmsIBRECEIVEReversal();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
     
    public ArrayList getIBRecvList(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoHdrDAO.setmLogger(mLogger);
			al = _PoHdrDAO.selectIBRecvList(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
    public ArrayList listPODetilstoPrintNew(String plant, String aPONO,String extcond)
    		throws Exception {
    			String q = "";
    			ArrayList al = null;
    			RecvDetDAO _RecvDetDAO = new RecvDetDAO();
    			_RecvDetDAO.setmLogger(mLogger);
    			try {
    				al = _RecvDetDAO.getPoDetailToPrintNew(plant, aPONO,extcond);
    			
    			} catch (Exception e) {
    				throw e;
    			}
    			
    			return al;
    		}
    
	public boolean savePoMultiRemarks(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			_PoDetDAO.setmLogger(mLogger);
			flag = _PoDetDAO.insertPoMultiRemarks(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
	public ArrayList listPoMultiRemarks(String plant, String aPONO,String aPOLNNO)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("pono", aPONO);
			htCond.put("polnno", aPOLNNO);
			q = "isnull(remarks,'') remarks";
		   _PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectPoMultiRemarks(q,htCond," plant <> '' order by id_remarks");
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
public ArrayList listPODETDetailsforcopyfunc(String plant, String pono)
			throws Exception {
		String q = "";
		ArrayList al = null;

		try {
			
			 Hashtable htCond = new Hashtable();
             htCond.put("PLANT", plant);
             htCond.put("pono", pono);
             q = "pono,polnno,item,isnull(itemdesc,'') itemdesc,isnull(unitmo,'') uom,isnull(unitcost,0)*isnull(currencyuseqt,0)unitcost,CAST((isnull(unitcost,0)*isnull(currencyuseqt,0)) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as convunitcost,isnull(qtyor,0) as qtyor,isnull(comment1,'') as prdRemarks";
            
             al = _PoDetDAO.selectPoDet(q, htCond," plant <> '' order by polnno");

             
		} catch (Exception e) {
			throw e;
		}
		return al;
	} 

public String getIBDiscountSelectedItem(String aPlant,String poNO,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _PoHdrDAO.getIBDiscountSelectedItem(aPlant, poNO,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

public String getIBDiscountSelectedItemVNO(String aPlant,String vendno,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _PoHdrDAO.getIBDiscountSelectedItemVNO(aPlant, vendno,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}
public ArrayList listInboundSummaryBymultiple(String plant,String afrmDate,String atoDate,String supname,String pono,String item)
	    throws Exception {
			String sCondition = "",dtCondStr="",extraCon="";
			MovHisDAO movHisDAO = new MovHisDAO();
	    	ArrayList al = null;
	    	try {
	    		 Hashtable htCond = new Hashtable();
	             htCond.put("b.PLANT", plant);
	             if(item.length()>0)
	             {
	            	 htCond.put("b.ITEM", item);
	             }
	             if(pono.length()>0)
	             {
	            	 htCond.put("b.PONO", pono);
	             }
	             
	    		
	    		if (supname.length()>0){
	    			supname = new StrUtils().InsertQuotes(supname);
                 	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+supname+"%' " ;
                 }
	    		
	    		dtCondStr =    " and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
				extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date),b.pono,polnno ";
			
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
				String aQuery = "select b.pono,b.polnno,a.custname,b.item,c.itemdesc,b.lnstat,isnull(b.qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,b.lnstat,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY  from "
						+ "["
						+ plant
						+ "_"
						+ "pohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "podet] b,"
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c where a.pono=b.pono and b.ITEM=c.item and b.lnstat <> 'C' " + sCondition ;

				
				al = movHisDAO.selectForReport(aQuery, htCond,extraCon);
		 
	    	} catch (Exception e) {
	    		this.mLogger.exception(this.printLog, "", e);
	    		throw e;
	    	}

	    	return al;
	}

//not by Draft - azees
public ArrayList listInboundSummaryBymultipleWithext(String plant,String afrmDate,String atoDate,String supname,String pono,String item)
	    throws Exception {
			String sCondition = "",dtCondStr="",extraCon="";
			MovHisDAO movHisDAO = new MovHisDAO();
	    	ArrayList al = null;
	    	try {
	    		 Hashtable htCond = new Hashtable();
	             htCond.put("b.PLANT", plant);
	             if(item.length()>0)
	             {
	            	 htCond.put("b.ITEM", item);
	             }
	             if(pono.length()>0)
	             {
	            	 htCond.put("b.PONO", pono);
	             }
	             
	    		
	    		if (supname.length()>0){
	    			supname = new StrUtils().InsertQuotes(supname);
                 	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+supname+"%' " ;
                 }
	    		
	    		dtCondStr =    " and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
				extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date),b.pono,polnno ";
			
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
				String aQuery = "select b.pono,b.polnno,a.custname,b.item,c.itemdesc,b.lnstat,isnull(b.qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,b.lnstat,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY  from "
						+ "["
						+ plant
						+ "_"
						+ "pohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "podet] b,"
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c where a.pono=b.pono and b.ITEM=c.item and b.lnstat <> 'C' and ORDER_STATUS <>'Draft' " + sCondition ;

				
				al = movHisDAO.selectForReport(aQuery, htCond,extraCon);
		 
	    	} catch (Exception e) {
	    		this.mLogger.exception(this.printLog, "", e);
	    		throw e;
	    	}

	    	return al;
	}

	/**
	 * Provides list of top received products in the given period
	 * 
	 * @param plant
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList getTopReceivedProducts(String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			String aQuery = "select ITEM AS PRODUCT, ITEMDESC, SUM(ISNULL(RECQTY, 0)) as RECEIVEDQTY from " + "[" + plant
					+ "_" + "RECVDET] where CONVERT(DATETIME, RECVDATE, 103) between '" + fromDate + "' and '" + toDate
					+ "' group by ITEM, ITEMDESC order by 3 desc";

			al = _PoDetDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	/**
	 * Provides total receipt value in the given period
	 * 
	 * @param plant
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList getTotalReceipt(String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			String aQuery = "SELECT SUM(ISNULL(QTYOR, 0) * (ISNULL(UNITCOST, 0))) as TOTAL_RECEIPT,"
					+ " SUM(ISNULL(QTYOR, 0)) as TOTAL_RECQTY FROM " 
					+ "[" + plant + "_POHDR] A JOIN [" + plant + "_PODET] B ON A.PONO = B.PONO "
					+ "WHERE CONVERT(DATETIME, CollectionDate, 103) between '" + fromDate + "' and '" + toDate + "'";

			al = _PoDetDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	
	public String getinboundOrder_item_orderline_details(String Plant, String pono,String item,
				Boolean isReceived) {
			String xmlStr = "";
			ArrayList al = null;
			

			//String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
			String query = " ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) QTYRC  ";
			String extCond = " lnstat <> 'C' AND ";
			/*if (!isReceived) {
				extCond ="";
			}*/

			extCond += "  PLANT <> '' GROUP BY ITEM,ITEMDESC,UNITMO ORDER BY ITEM ";

			Hashtable ht = new Hashtable();
			ht.put("PONO", pono);
			ht.put("PLANT", Plant);
			ht.put("ITEM", item);
			//
			try {
				al = _PoDetDAO.selectPoDet(query, ht, extCond);
				if (al.size() > 0) {
					xmlStr += XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("itemDetails total='"
							+ String.valueOf(al.size()) + "'");
					for (int i = 0; i < al.size(); i++) {
						Map map = (Map) al.get(i);
						xmlStr += XMLUtils.getStartNode("record");
						xmlStr += XMLUtils.getXMLNode("pono", (String) map
								.get("PONO"));
						
						xmlStr += XMLUtils.getXMLNode("item", (String) map
								.get("ITEM"));
						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
								.replaceCharacters2SendPDA(map.get("ITEMDESC")
										.toString()));
						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
								.get("QTYOR")));
						xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
								.get("QTYRC")));
						
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
	public String getConvertedUnitCostForProductWTC(String aPlant, String aPONO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _PoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTC(aPlant, aPONO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public String getConvertedUnitCostForProductWTCcurrency(String aPlant, String currency,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _PoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTCcurrency(aPlant, currency,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	public ArrayList getPODetDetailsRandomMutiUOM(String Plant,
			String PONO,String item,String uom) {
    	ArrayList al = null;

		try {
			al = _PoDetDAO.getPODetDetailsRandomMutiUOM(Plant, PONO, item,uom);
			 
		} catch(Exception e ){
    		this.mLogger.exception(this.printLog, "", e);
    	}
		
    return al;
    }
	public String getUomWithQty(String plant1,String user,String pono,String item) {

		String xmlStr = "";
		ArrayList al = null;
		PoDetDAO dao = new PoDetDAO();
		dao.setmLogger(mLogger);

		try {
			al = dao.getUOMByOrder(plant1,user,pono,item);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("uom total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("uom"));
					 xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
								.get("uomqty"));  
					  
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("uom");
				System.out.println(xmlStr);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
	
	/*Start code by Abhilash on 02-11-2019*/
	public String getQtyToBeRecvByProduct(String aItem, String plant) throws Exception {
		String qtyToBeRecv = "";		
		try {
			_PoDetDAO.setmLogger(mLogger);
			qtyToBeRecv = _PoDetDAO.getQtyToBeRecvByProduct(aItem, plant);
			if(qtyToBeRecv.equalsIgnoreCase(""))
				qtyToBeRecv="0.000";
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return qtyToBeRecv;
	}
	
	public ArrayList getTotalReceiptByProduct(String item, String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.getTotalReceiptByProduct(item, plant, fromDate, toDate);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	public ArrayList getTotalInvQtyByProduct(String item, String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.getTotalQtyReceiptByProduct(item, plant, fromDate, toDate);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	/*End code by Abhilash on 02-11-2019*/
	public List getOrderDetailsForBilling(Hashtable ht) throws Exception {
		List ordersList = new ArrayList();
		try {
			_PoHdrDAO.setmLogger(mLogger);
			ordersList = _PoHdrDAO.getOrderDetailsForBilling(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return ordersList;
	}
	
	public List getBillingDetailsByGRNO(Hashtable ht) throws Exception {
		List ordersList = new ArrayList();
		try {
			_PoHdrDAO.setmLogger(mLogger);
			ordersList = _PoHdrDAO.getBillingDetailsByGRNO(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return ordersList;
	}
	
	public List getPreviousOrderDetails(Hashtable ht, String rows) throws Exception {
		List ordersList = new ArrayList();
		try {
			_PoHdrDAO.setmLogger(mLogger);
			ordersList = _PoHdrDAO.getPreviousOrderDetails(ht, rows);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return ordersList;
	}
	
	public ArrayList getOrderNoForOrderReceipt(Hashtable ht) throws Exception {
		ArrayList al = new ArrayList();
		try {
			_PoHdrDAO.setmLogger(mLogger);
			al = _PoHdrDAO.getOrderNoForOrderReceipt(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	//created by vicky Used for PDA purchase order popup
	public String getPDApopupInBoundOrder(String Plant,String start,String end,String pono) {

		String xmlStr = "";
		ArrayList al = null;
		String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY a.pono desc)) AS ID,ISNULL((SELECT COUNT(*)  FROM ["+Plant+"_POHDR] as a, ["+Plant+"_PODET] as b  WHERE b.PONO LIKE '%' AND b.PONO = a.PONO AND b.PLANT = a.PLANT and a.PONO LIKE '%"+pono+"%'),0) TOTPO , a.pono as pono ,isnull(a.CustName,'') as CustName,isnull(a.collectiondate,'') orderdate,isnull(a.status,'') status,isnull(a.Remark1,'') as Remark1  FROM ["+Plant+"_POHDR] as a, ["+Plant+"_PODET] as b where  a.pono = b.pono AND a.PLANT = b.PLANT and a.status <> 'C' and ORDER_STATUS <>'Draft' group by a.pono, a.CustName, a.collectiondate,a.status,a.Remark1)SELECT * FROM S where ID >='"+start+"' and ID<='"+end+"' order by CAST((SUBSTRING(orderdate, 7, 4) + SUBSTRING(orderdate, 4, 2) + SUBSTRING(orderdate, 1, 2)) AS date )desc";
		String extCond = "";
		Hashtable ht = new Hashtable();

		//
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectItemForPDA(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("pono"));

					xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils
							.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("orderdate", (String) map
							.get("orderdate"));
					xmlStr += XMLUtils.getXMLNode("status", (String) map
							.get("status"));
					xmlStr += XMLUtils.getXMLNode("count", (String) map
							.get("TOTPO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
							.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	//created by vicky Used for PDA purchase order AutoSuggestion
	public String getPDApopupInBoundOrder(String Plant,String pono) {

		String xmlStr = "";
		ArrayList al = null;
		String query = "pono,custName,custcode,status,collectiondate";
		String extCond = "and pono like '%"+pono+"%' and status <> 'C' and ORDER_STATUS <>'Draft' ORDER BY CONVERT(date, CollectionDate, 103) desc";
		Hashtable ht = new Hashtable();
		ht.put("PLANT", Plant);
		//
		try {
			_PoHdrDAO.setmLogger(mLogger);
			al = _PoHdrDAO.selectPoHdr(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("pono"));
					xmlStr += XMLUtils.getXMLNode("supplier", (String) map
							.get("CustName"));
									
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	//created by vicky Used for PDA Item AutoSuggestion
	public String getItemFromProductMstForAutoSuggestion(String Plant,String itemNo) {

		String xmlStr = "";
		ArrayList al = null;
		String query= "SELECT DISTINCT IM.ITEM ITEM ,ITEMDESC,IM.ISACTIVE ,isnull(NONSTKFLAG,'N') NONSTKFLAG FROM ["+Plant+"_ITEMMST ] as IM JOIN ["+Plant+"_ALTERNATE_ITEM_MAPPING] as AIM ON AIM.ITEM = IM.ITEM  LEFT JOIN ["+Plant+"_INVMST] as INVM ON IM.ITEM = INVM.ITEM WHERE  IM.ITEM LIKE '"+itemNo+"%' AND AIM.PLANT = IM.PLANT  GROUP BY IM.ITEM ,ITEMDESC,IM.ISACTIVE,NONSTKFLAG ORDER BY IM.ITEM ";
		String extCond = "";
		Hashtable ht = new Hashtable();

		//
		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectItemForPDA(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("nonstock",(String) map
							.get("ISACTIVE"));
					xmlStr += XMLUtils.getXMLNode("nonstockflag",(String) map
							.get("NONSTKFLAG"));
				
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	//Created by Vicky Desc:Used for Validating Purchase Order item for PDA based on UOM
	public String getinboundOrder_item_orderline_details(String Plant, String pono,String item,String uom,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList al = null;
		
		String query = " ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) QTYRC  ";
		String extCond = " lnstat <> 'C' and UNITMO LIKE '%"+uom+"%' AND ";
		
		extCond += "  PLANT <> '' GROUP BY POLNNO,ITEM,ITEMDESC,UNITMO ORDER BY POLNNO ";

		Hashtable ht = new Hashtable();
		ht.put("PONO", pono);
		ht.put("PLANT", Plant);
		ht.put("ITEM", item);
		//
		try {
			al = _PoDetDAO.selectPoDet(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("PONO"));
					
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
							.get("QTYRC")));
					
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
	//Created by Vicky Desc:Used for getting Purchase Order item For UOM popup Screen in PDA
	public String getinboundOrder_item_order_ForUOMPopup(String Plant, String pono,String item,String uom,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList al = null;
		
		String query = " ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYRC),0) QTYRC  ";
		String extCond = " lnstat <> 'C' AND ";
		
		extCond += "  PLANT <> '' GROUP BY UNITMO ";

		Hashtable ht = new Hashtable();
		ht.put("PONO", pono);
		ht.put("PLANT", Plant);
		ht.put("ITEM", item);
		//
		try {
			al = _PoDetDAO.selectPoDet(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("PONO"));
					
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtyrc", StrUtils.formatNum((String) map
							.get("QTYRC")));
					
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
	
	public String getProductCurrencyID(String aPlant, String aPONO) throws Exception {

		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ICRNO", aPONO);

		// String query=" custCode ";
		String query = " ISNULL( " + "CURRENCYID" + ",'') as " + "CURRENCYID" + " ";
		_PoHdrDAO.setmLogger(mLogger);
		Map m = _PoHdrDAO.selectProductRow(query, ht);

		custCode = (String) m.get("CURRENCYID");

		return custCode;
	}
	
	public ArrayList getPorductReceiveDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_PoDetDAO.setmLogger(mLogger);
			al = _PoDetDAO.selectProductreceiveDet(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
}
