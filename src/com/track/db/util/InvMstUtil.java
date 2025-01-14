package com.track.db.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.POBeanDAO;
import com.track.dao.RsnMst;
import com.track.gates.DbBean;
import com.track.tran.WmsLocationTransfer;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
/*- ************Modification History*********************************
Dec 5 2014 Bruhan, Description:New Method:process_PutAway
Dec 5 2014 Bruhan, Description:New Method:process_Wms_PutAway
Jan 2, 2015,Bruhan, Description:New Method:loadAssignInvLocationsXmlPDA
*/
public class InvMstUtil {

	private DoHdrDAO _DoHdrDAO = null;
	private DoDetDAO _DoDetDAO = null;
	private InvMstDAO _InvMstDAO = null;
	private ItemMstDAO _ItemMstDAO = null;
	private POBeanDAO poBeanDAO = new POBeanDAO();
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.InvMstUtil_PRINTPLANTMASTERLOG;
	

	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public InvMstUtil() {
		_DoDetDAO = new DoDetDAO();
		_DoHdrDAO = new DoHdrDAO();
		_InvMstDAO = new InvMstDAO();
		_ItemMstDAO = new ItemMstDAO();

		_DoDetDAO.setmLogger(mLogger);
		_DoHdrDAO.setmLogger(mLogger);
		_InvMstDAO.setmLogger(mLogger);
		_ItemMstDAO.setmLogger(mLogger);

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
			this.mLogger.exception(this.printLog, "", e);
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

		}
		return listQry;
	}

	public ArrayList getPOList() throws Exception {
		ArrayList listQry = new ArrayList();
		try {
			poBeanDAO.setmLogger(mLogger);
			listQry = poBeanDAO.getPOList();
		} catch (Exception e) {

		}
		return listQry;
	}

	// PDA Methods	// //////////////////////////
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

		}
		return xmlStr;
	}

	public ArrayList getDoDetDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectDoDet(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getDoDetDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectDoDet(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getDoHdrDetails(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.selectDoHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getDoHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.selectDoHdr(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public boolean saveDoDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_DoHdrDAO.setmLogger(mLogger);
			flag = _DoDetDAO.insertDoDet(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public String getNextDo() throws Exception {

		String maxDo = "";
		try {
			_DoHdrDAO.setmLogger(mLogger);
			maxDo = _DoHdrDAO.getMaxDo("SIS");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return maxDo;
	}

	public String load_item_details_xml(String aPlant, String aItem)
			throws Exception {

		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = null;
		htInvMst = new Hashtable();
		queryDoHdr = " item,userFld1,loc,status";

		htInvMst.put("plant", aPlant);
		htInvMst.put("item", aItem);
		try {
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO.selectInvMst(queryDoHdr, htInvMst, "");

			if (alInvMst.size() > 0) {

				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("item", (String) map1
							.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", _ItemMstDAO
							.getItemDesc(aPlant, aItem));
					xmlStr += XMLUtils.getXMLNode("itemLoc", (String) map1
							.get("loc"));
					xmlStr += XMLUtils.getXMLNode("itemStatus", (String) map1
							.get("status"));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				throw new Exception("No details found for item : " + aItem);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return xmlStr;
	}

	public String QueryInventory_xml(String aPlant, String aItem)
			throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = null;
		htInvMst = new Hashtable();
		queryDoHdr = " item,userFld1,loc";

		htInvMst.put("plant", aPlant);
		htInvMst.put("item", aItem);
		try {
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO.selectInvMstDetails(queryDoHdr, htInvMst, "");

			if (alInvMst.size() > 0) {

				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("item", (String) map1
							.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", _ItemMstDAO
							.getItemDesc(aPlant, aItem));
					xmlStr += XMLUtils.getXMLNode("itemLoc", (String) map1
							.get("loc"));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				throw new Exception("No details found for item : " + aItem);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return xmlStr;
	}

	public String load_all_do_items_xml(String aPlant, String aDoNum)
			throws Exception {

		String xmlStr = "", dono = "", dolno = "", item = "", itemDesc = "";
		ArrayList alDoDet = null;
		String queryDoDet = "";
		Hashtable htDoDet = null;
		htDoDet = new Hashtable();
		queryDoDet = " dono,dolnno,item";
		htDoDet.put("plant", aPlant);
		htDoDet.put("dono", aDoNum);
		try {
			alDoDet = getDoDetDetails(queryDoDet, htDoDet, " LNSTAT <> 'C'");
			MLogger.log(0, "Total Product : " + alDoDet.size());

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("DoDet total ='"
					+ String.valueOf(alDoDet.size()) + "'");

			if (alDoDet.size() > 0) {
				for (int i = 0; i < alDoDet.size(); i++) {

					Map map1 = (Map) alDoDet.get(i);
					dono = (String) map1.get("dono");
					dolno = (String) map1.get("dolno");
					item = (String) map1.get("item");
					itemDesc = (String) map1.get("itemDesc");

					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("doNum", dono);
					xmlStr += XMLUtils.getXMLNode("doLnNo", dolno);
					xmlStr += XMLUtils.getXMLNode("item", item);
					xmlStr += XMLUtils.getXMLNode("itemDesc", itemDesc);
					xmlStr += XMLUtils.getEndNode("record");

				}

				xmlStr += XMLUtils.getEndNode("DoDet");

			} else {
				throw new Exception("No item found for order " + aDoNum);
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);

			throw e;

		}
		return xmlStr;
	}

	public String load_lotdetails_xml(String aPlant, String aItem,
			String batch, String Loc, String dummy) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = null;
		htInvMst = new Hashtable();
		queryDoHdr = "userfld4,isnull(qty,'') as qty";
		// condition
		htInvMst.put("plant", aPlant);
		htInvMst.put("item", aItem);
		htInvMst.put("userfld4", batch);
		htInvMst.put("loc", Loc);
		try {
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO.selectInvMst(queryDoHdr, htInvMst, "");
			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("userfld2", (String) map1
							.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", (String) map1
							.get("qty"));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				throw new Exception("Invalid batch : " + batch);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return xmlStr;
	}

	public String load_lotdetails_xml(String aPlant, String aItem,
			String batch, String loc) throws Exception {

		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = null;
		htInvMst = new Hashtable();
		queryDoHdr = "userfld4,isnull(qty,'') as qty";
		htInvMst.put("plant", aPlant);
		htInvMst.put("item", aItem);
		htInvMst.put("userfld4", batch);
		htInvMst.put("loc", loc);
		try {
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO.selectInvMst(queryDoHdr, htInvMst, "");

			if (alInvMst.size() > 0) {

				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("userfld2", (String) map1
							.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", (String) map1
							.get("qty"));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				throw new Exception("Invalid batch : " + batch);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}
        
        
    public boolean process_LocationTransferforPC(Map obj) throws Exception {
            String xmlStr = "";
            boolean flag = false;
            UserTransaction ut = null;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();

                    flag = process_Wms_LocationTranfer(obj);

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
    public boolean process_MultiLocationTransferforPC(Map obj) throws Exception {
        String xmlStr = "";
        boolean flag = false;
        UserTransaction ut = null;
        try {
                flag = process_Wms_LocationTranfer(obj);

        } catch (Exception e) {
                flag = false;
               
              throw e;
        }
     
        return flag;
}
	public String process_LocationTransfer(Map obj) {
		String xmlStr = "";
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			flag = process_Wms_LocationTranfer(obj);

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
			xmlStr = XMLUtils.getXMLMessage(0, "Error in transfering item : "
					+ obj.get(IConstants.ITEM) + " to location : "
					+ obj.get(IConstants.LOC2) + " :: " + e.getMessage());

		}
		if (flag == true) {

			if (obj.get(IConstants.TRAN_TYPE).toString().equalsIgnoreCase(
					"LOC_TRANSFER")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : "
						+ obj.get(IConstants.ITEM) + " transfered to "
						+ obj.get(IConstants.LOC2) + " successfully!");
			} else if (obj.get(IConstants.TRAN_TYPE).toString()
					.equalsIgnoreCase("TRANSFER_OUT")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : "
						+ obj.get(IConstants.ITEM)
						+ " has transfered OUT* successfull!");
			} else if (obj.get(IConstants.TRAN_TYPE).toString()
					.equalsIgnoreCase("TRANSFER_IN")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : "
						+ obj.get(IConstants.ITEM)
						+ " has transfered IN* successfull!");
			}

		} else {
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in transfering Product : "
							+ obj.get(IConstants.ITEM) + " to location : "
							+ obj.get(IConstants.LOC2));
		}
		return xmlStr;
	}
	public JSONObject process_LocationTransfer_STD(Map obj) {
		String xmlStr = "";
		boolean flag = false;
		UserTransaction ut = null;
		JSONObject resultJson = new JSONObject();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			flag = process_Wms_LocationTranfer(obj);

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
			xmlStr = XMLUtils.getXMLMessage(0, "Error in transfering item : "
					+ obj.get(IConstants.ITEM) + " to location : "
					+ obj.get(IConstants.LOC2) + " :: " + e.getMessage());
		
				resultJson.put("message", "Error in transfering item : "
						+ obj.get(IConstants.ITEM) + " to location : "
						+ obj.get(IConstants.LOC2) + " :: " + e.getMessage());
		 resultJson.put("status","0");
		 resultJson.put("LineNo",obj.get("LineNo").toString());
		}
		if (flag == true) {

			if (obj.get(IConstants.TRAN_TYPE).toString().equalsIgnoreCase(
					"LOC_TRANSFER")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : "
						+ obj.get(IConstants.ITEM) + " transfered to "
						+ obj.get(IConstants.LOC2) + " successfully!");
				
				resultJson.put("message", "Product : "
						+ obj.get(IConstants.ITEM) + " transfered to "
						+ obj.get(IConstants.LOC2) + " successfully!");
		 resultJson.put("status","1");
		 resultJson.put("LineNo",obj.get("LineNo").toString());
		 
			} else if (obj.get(IConstants.TRAN_TYPE).toString()
					.equalsIgnoreCase("TRANSFER_OUT")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : "
						+ obj.get(IConstants.ITEM)
						+ " has transfered OUT* successfull!");
				
				resultJson.put("message",  "Product : "
						+ obj.get(IConstants.ITEM)
						+ " has transfered OUT* successfull!");
		 resultJson.put("status","1");
		 resultJson.put("LineNo",obj.get("LineNo").toString());
		 
			} else if (obj.get(IConstants.TRAN_TYPE).toString()
					.equalsIgnoreCase("TRANSFER_IN")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : "
						+ obj.get(IConstants.ITEM)
						+ " has transfered IN* successfull!");
				
				resultJson.put("message", "Product : "
						+ obj.get(IConstants.ITEM)
						+ " has transfered IN* successfull!");
		 resultJson.put("status","1");
		 resultJson.put("LineNo",obj.get("LineNo").toString());
			}

		} else {
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in transfering Product : "
							+ obj.get(IConstants.ITEM) + " to location : "
							+ obj.get(IConstants.LOC2));
			

			resultJson.put("message", "Error in transfering Product : "
					+ obj.get(IConstants.ITEM) + " to location : "
					+ obj.get(IConstants.LOC2));
	 resultJson.put("status","0");
	 resultJson.put("LineNo",obj.get("LineNo").toString());
		}
		return resultJson;
	}

	private boolean process_Wms_LocationTranfer(Map map) throws Exception {
		boolean flag = false;
		WmsTran tran = new WmsLocationTransfer();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;

	}

	public String getItemDetails(String Plant, String Item) {

		String xmlStr = "";
		ArrayList al = null;

		String query = "item,userfld4,loc,qty ";
		String extCond = " qty >0 ORDER BY item, userfld4 ";
		Hashtable ht = new Hashtable();
		ht.put("item", Item);
		ht.put("plant", Plant);
		try {
			_InvMstDAO.setmLogger(mLogger);
			_ItemMstDAO.setmLogger(mLogger);
			al = _InvMstDAO.selectInvMst(query, ht, extCond);

			if (al.size() > 0) {
			    String itemDesc =_ItemMstDAO .getItemDesc(Plant,Item);
			    String uom =_ItemMstDAO.getItemDesc(Plant,Item);
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
					xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom));
					xmlStr += XMLUtils.getXMLNode("loc", (String) map.get("loc"));
					xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	/*Start code by vicky on 10-11-2019*/
        public String getItemDetails(String Plant, String Item, String uom) {

		String xmlStr = "";
		ArrayList al = null;

		try {
			_InvMstDAO.setmLogger(mLogger);
			_ItemMstDAO.setmLogger(mLogger);
			al = _InvMstDAO.getAvaliableQtyForInvProductUOMPDA(Plant, Item,uom);

			if (al.size() > 0) {
				String itemDesc =_ItemMstDAO .getItemDesc(Plant,Item);
				String uom1 =_ItemMstDAO.getInvUOM(Plant,Item);
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
				    if("".equals(uom)){
					xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom1));
				    }
				    else
				    {
				    	xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom));
				    }
					xmlStr += XMLUtils.getXMLNode("loc", (String) map.get("loc"));
					xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	
	public String getInvItemDetailsForLoc(String Plant, String aLoc, String uom) {

        String xmlStr = "";
        ArrayList al = null;

    
        try {
                _InvMstDAO.setmLogger(mLogger);
                _ItemMstDAO.setmLogger(mLogger);
                al = _InvMstDAO.getAvaliableQtyForInvLocationUOMPDA(Plant, aLoc, uom);

                if (al.size() > 0) {
                 
                        xmlStr += XMLUtils.getXMLHeader();
                        xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
                        for (int i = 0; i < al.size(); i++) {
                                Map map = (Map) al.get(i);
                                xmlStr += XMLUtils.getStartNode("record");
                                String itemDesc =_ItemMstDAO .getItemDesc(Plant,(String) map.get("item"));
                			    String uom1 =_ItemMstDAO.getInvUOM(Plant,(String) map.get("item"));
                                xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
                                xmlStr += XMLUtils.getXMLNode("loc", (String) map.get("loc"));
                                xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
            					xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
            					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
            					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
                                 xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
                                 if("".equals(uom)){
                 					xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom1));
                 				    }
                 				    else
                 				    {
                 				    	xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom));
                 				    }
                                xmlStr += XMLUtils.getEndNode("record");
                        }
                        xmlStr += XMLUtils.getEndNode("itemDetails");
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return xmlStr;
}
/*End code by vicky on 10-11-2019*/
    public String getInvItemDetailsForLoc(String Plant, String aLoc) {

            String xmlStr = "";
            ArrayList al = null;

            String query = "item,userfld4,loc,qty";
            String extCond = " qty >0 ORDER BY item, userfld4 ";
            Hashtable ht = new Hashtable();
            ht.put("Loc", aLoc);
            ht.put("plant", Plant);
            try {
                    _InvMstDAO.setmLogger(mLogger);
                    _ItemMstDAO.setmLogger(mLogger);
                    al = _InvMstDAO.selectInvMst(query, ht, extCond);

                    if (al.size() > 0) {
                     
                            xmlStr += XMLUtils.getXMLHeader();
                            xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
                            for (int i = 0; i < al.size(); i++) {
                                    Map map = (Map) al.get(i);
                                    xmlStr += XMLUtils.getStartNode("record");
                                    String itemDesc =_ItemMstDAO .getItemDesc(Plant,(String) map.get("item"));
                    			    String uom =_ItemMstDAO.getItemDesc(Plant,(String) map.get("item"));
                                    xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
                                    xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
                					xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
                                     xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
                                     xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom));
                                    xmlStr += XMLUtils.getEndNode("record");
                            }
                            xmlStr += XMLUtils.getEndNode("itemDetails");
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return xmlStr;
    }

    public ArrayList getItemList(String Plant, String Item, String extCond) {
    	String xmlStr = "";
		ArrayList al = null;

		String query = "item,userfld4,loc,qty,ID ";
		Hashtable ht = new Hashtable();
		ht.put("item", Item);
		ht.put("plant", Plant);
		//
		try {
			_InvMstDAO.setmLogger(mLogger);
			al = _InvMstDAO.selectInvMst(query, ht, extCond);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return al;
    }
	public ArrayList getItemList(String Plant, String Item) {

		return getItemList(Plant, Item, " qty >0");
	}

	public String loadLocationsXml2(String aPlant) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		Hashtable htInvMst = new Hashtable();

		htInvMst.put("PLANT", aPlant);

		LocMstDAO locMstDAO = new LocMstDAO();
		locMstDAO.setmLogger(mLogger);
		try {
			alInvMst = locMstDAO.getStockMoveLocByWMS(aPlant);

			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocationList total='"
						+ String.valueOf(alInvMst.size()) + "'");

				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					String locDescription = (String) map1.get("locdesc");
					String locName = (String) map1.get("loc");

					xmlStr += XMLUtils.getStartNode("LocationDetail");
					xmlStr += XMLUtils.getXMLNode("locationId", locName);
					xmlStr += XMLUtils
							.getXMLNode("description", locDescription);
					xmlStr += XMLUtils.getEndNode("LocationDetail");
				}
				xmlStr += XMLUtils.getEndNode("LocationList");
			} else {
				throw new Exception("No location found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	public String loadLocationsXml(String aPlant,String user) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = new Hashtable();
		queryDoHdr = " LOC,LOCDESC";
		htInvMst.put("PLANT", aPlant);
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);

		LocMstDAO locMstDAO = new LocMstDAO();
		locMstDAO.setmLogger(mLogger);
		try {
			alInvMst = _InvMstDAO.getStockTransferLocByWMS(aPlant, "",user);
			// alInvMst = locMstDAO.selectLocMst(queryDoHdr, htInvMst, "");

			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocationList total='"
						+ String.valueOf(alInvMst.size()) + "'");

				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					String locDescription = "";
					String locName = (String) map1.get("FromLoc");
					xmlStr += XMLUtils.getStartNode("LocationDetail");
					xmlStr += XMLUtils.getXMLNode("locationId", locName);
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getEndNode("LocationDetail");
				}
				xmlStr += XMLUtils.getEndNode("LocationList");
			} else {
				throw new Exception("No location found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	
	public String loadLocationsXmlPDA(String aPlant,String aLoc,String user) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = new Hashtable();
		queryDoHdr = " LOC,LOCDESC";
		htInvMst.put("PLANT", aPlant);
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);

		LocMstDAO locMstDAO = new LocMstDAO();
		locMstDAO.setmLogger(mLogger);
		try {
			alInvMst = _InvMstDAO.getStockTransferLocByPDA(aPlant, aLoc,user);
		
			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocationList total='"
						+ String.valueOf(alInvMst.size()) + "'");

			
				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					String locDescription = (String) map1.get("LocDesc");
					String locName = (String) map1.get("FromLoc");

					xmlStr += XMLUtils.getStartNode("LocationDetail");
					xmlStr += XMLUtils.getXMLNode("locationId", strUtils.replaceCharacters2SendPDA(locName));
					xmlStr += XMLUtils.getXMLNode("description",strUtils.replaceCharacters2SendPDA(locDescription));
					xmlStr += XMLUtils.getEndNode("LocationDetail");

				}
				xmlStr += XMLUtils.getEndNode("LocationList");
			} else {
				throw new Exception("No location found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	//Samatha
	public String load_inv_details_for_Lot_xml(String aPlant, String aItem,
			String batch) throws Exception {

		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = null;
		htInvMst = new Hashtable();
		queryDoHdr = "loc,isnull(qty,'') as qty";
		// condition
		htInvMst.put("plant", aPlant);
		htInvMst.put("item", aItem);
		htInvMst.put("userfld4", batch);
		try {
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO
					.selectInvMst(queryDoHdr, htInvMst, " qty > 0");

			if (alInvMst.size() > 0) {

				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("loc", (String) map1
							.get("loc"));
					xmlStr += XMLUtils.getXMLNode("qty", (String) map1
							.get("qty"));

				}

				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				throw new Exception("Invalid batch : " + batch);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		System.out.println("[XML] : " + xmlStr);
		return xmlStr;
	}

	public String getbatchDetails(String Plant, String Item,String loc,String aUser,String batch) throws Exception {
		String xmlStr = "";
		ArrayList al = null;
                
                UserLocUtil userLocUtil = new UserLocUtil();
                userLocUtil.setmLogger(mLogger);
                String condAssignedLocforUser = " "; 
               // condAssignedLocforUser =  userLocUtil.getUserLocAssigned(aUser,Plant,"LOC");
                
		
                
         String query = "isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) qty," 
        		 +" SUBSTRING(crat,1,2)+'/'+ ISNULL(SUBSTRING(crat,3,2)+'/'+ SUBSTRING(crat,5,4),'') as CRAT," 
         		+"(SELECT ISNULL(NONSTKFLAG,'') from "+ Plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID,ISNULL(EXPIREDATE,'') EXPIRYDATE";
                //String extCond = " USERFLD4 LIKE  '"+ batch + "%" + "' and qty >0  ORDER BY userfld4,CRAT";
				 String extCond = " USERFLD4 LIKE '%' and qty >0  ORDER BY userfld4,CRAT";
         
         //loc like'" + loc + "%" + "'");
		Hashtable ht = new Hashtable();
		ht.put("item", Item);
		ht.put("plant", Plant);
	    ht.put("loc", loc);
		try {
			_InvMstDAO.setmLogger(mLogger);
			al = _InvMstDAO.selectInvMst(query, ht,  extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("batch", (String) map
							.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
					xmlStr += XMLUtils.getXMLNode("createddate", (String) map.get("CRAT"));
					xmlStr += XMLUtils.getXMLNode("invid", (String) map.get("ID"));
					xmlStr += XMLUtils.getXMLNode("expirydate", (String) map.get("EXPIRYDATE"));
					
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	
	public String getbatchDetailsbyexpirydate(String Plant, String Item,String loc,String aUser,String batch) throws Exception {
		String xmlStr = "";
		ArrayList al = null;
                
                UserLocUtil userLocUtil = new UserLocUtil();
                userLocUtil.setmLogger(mLogger);
                String condAssignedLocforUser = " "; 
               // condAssignedLocforUser =  userLocUtil.getUserLocAssigned(aUser,Plant,"LOC");
                
		/*String query = "item,userfld4,loc,SUM(qty) as qty,isnull(expiredate,'')expirydate,(SELECT ISNULL(NONSTKFLAG,'') from "+ Plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK ";
		String extCond = " USERFLD4 LIKE '%" + batch + "%' and qty >0 GROUP BY ITEM,LOC,userfld4,expiredate ORDER BY BATCH,expiredate";*/
         String query = "isnull(item,'') item,isnull(userfld4,'') userfld4,isnull(loc,'') loc,isnull(qty,0) qty,isnull(expiredate,'')expirydate,(SELECT ISNULL(NONSTKFLAG,'') from "+ Plant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK,ID ";
         String extCond = " USERFLD4 LIKE '%" + batch + "%' and qty >0  ORDER BY userfld4,expiredate";
		Hashtable ht = new Hashtable();
		ht.put("item", Item);
		ht.put("plant", Plant);
	    ht.put("loc", loc);
		try {
			_InvMstDAO.setmLogger(mLogger);
			al = _InvMstDAO.selectInvMst(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					//xmlStr += XMLUtils.getXMLNode("item", (String) map
					//		.get("item"));
					//xmlStr += XMLUtils.getXMLNode("itemDesc", "");
					xmlStr += XMLUtils.getXMLNode("batch", (String) map
							.get("userfld4"));
					//xmlStr += XMLUtils.getXMLNode("loc", (String) map
					//		.get("loc"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
					xmlStr += XMLUtils.getXMLNode("expirydate", (String) map.get("expirydate"));
					xmlStr += XMLUtils.getXMLNode("invid", (String) map.get("ID"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

	@SuppressWarnings("all")
	public String getBatchDetail(String aPlant, String aItem) throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY from "
				+ aPlant + "_INVMST WHERE PLANT='" + aPlant + "' AND ITEM='"
				+ aItem + "' AND QTY >0 ");

		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			this.mLogger.query(itM.isPrintQuery(), sql.toString());

			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("batchId", hashMap
							.get("USERFLD4"));
					result += XMLUtils.getXMLNode("qty", hashMap.get("QTY"));
					result += XMLUtils.getEndNode("record");
				}
				result += XMLUtils.getEndNode("BatchDetails");
			}
			return result;
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

	}

	public String getBatchDetailWithLocation(String aPlant, String aItem,String user)
			throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
                UserLocUtil userLocUtil = new UserLocUtil();
                userLocUtil.setmLogger(mLogger);
                String condAssignedLocforUser = " "; 
                condAssignedLocforUser =  userLocUtil.getUserLocAssigned(user,aPlant,"LOC");
                StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY, LOC from "
				+ aPlant + "_INVMST WHERE PLANT='" + aPlant + "' AND ITEM='"
				+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' " +condAssignedLocforUser);

		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			this.mLogger.query(itM.isPrintQuery(), sql.toString());

			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("batchId", hashMap.get("USERFLD4"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
					result += XMLUtils.getEndNode("record");
				}
				result += XMLUtils.getEndNode("BatchDetails");
			}
			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

	}

	
	public String getBatchDetailWithLocationRandom(String aPlant, String aItem,String aBatch,String user)
	throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
        UserLocUtil userLocUtil = new UserLocUtil();
        userLocUtil.setmLogger(mLogger);
        StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY, LOC from "
		+ aPlant + "_INVMST WHERE PLANT='" + aPlant + "' AND ITEM='"
		+ aItem + "'  AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%'");
		//+ aItem + "'  AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND USERFLD4 LIKE '" + aBatch + "%' ");

	 try {
		con = DbBean.getConnection();
		InvMstDAO itM = new InvMstDAO();
		this.mLogger.query(itM.isPrintQuery(), sql.toString());
	
		alData = itM.selectData(con, sql.toString());
		if (alData.size() > 0) {
			result = XMLUtils.getXMLHeader();
			result += XMLUtils.getStartNode("BatchDetails total='"
					+ alData.size() + "'");
	
			for (Map<String, String> hashMap : alData) {
				result += XMLUtils.getStartNode("record");
				result += XMLUtils.getXMLNode("batchId", hashMap.get("USERFLD4"));
				result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
				result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
				result += XMLUtils.getEndNode("record");
			}
			result += XMLUtils.getEndNode("BatchDetails");
		}
		return result;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}

}
	
	public String getBatchDetailByBatch(String aPlant, String aItem,String aBatch,String user)
	throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
        UserLocUtil userLocUtil = new UserLocUtil();
        userLocUtil.setmLogger(mLogger);
         StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY, LOC from "
		+ aPlant + "_INVMST WHERE PLANT='" + aPlant + "' AND ITEM='"
		+ aItem + "'  AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND USERFLD4 LIKE '" + aBatch + "%' ");

	 try {
		con = DbBean.getConnection();
		InvMstDAO itM = new InvMstDAO();
		this.mLogger.query(itM.isPrintQuery(), sql.toString());
	
		alData = itM.selectData(con, sql.toString());
		if (alData.size() > 0) {
			result = XMLUtils.getXMLHeader();
			result += XMLUtils.getStartNode("BatchDetails total='"
					+ alData.size() + "'");
	
			for (Map<String, String> hashMap : alData) {
				result += XMLUtils.getStartNode("record");
				result += XMLUtils.getXMLNode("batchId", hashMap.get("USERFLD4"));
				result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
				result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
				result += XMLUtils.getEndNode("record");
			}
			result += XMLUtils.getEndNode("BatchDetails");
		}
		return result;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}

}
	
	public String getBatchDetailWithOutLocation(String aPlant, String aItem,String batch,String user)
	throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
        UserLocUtil userLocUtil = new UserLocUtil();
        userLocUtil.setmLogger(mLogger);
        //String condAssignedLocforUser = " "; 
        //condAssignedLocforUser =  userLocUtil.getUserLocAssigned(user,aPlant,"LOC");
        StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY, LOC from "
		+ aPlant + "_INVMST WHERE PLANT='" + aPlant + "' AND ITEM='"
		+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND USERFLD4='" + batch +"' AND USERFLD4<>'NOBATCH' ");

	 try {
		con = DbBean.getConnection();
		InvMstDAO itM = new InvMstDAO();
		this.mLogger.query(itM.isPrintQuery(), sql.toString());
	
		alData = itM.selectData(con, sql.toString());
		if (alData.size() > 0) {
			result = XMLUtils.getXMLHeader();
			result += XMLUtils.getStartNode("BatchDetails total='"
					+ alData.size() + "'");
	
			for (Map<String, String> hashMap : alData) {
				result += XMLUtils.getStartNode("record");
				result += XMLUtils.getXMLNode("batchId", hashMap.get("USERFLD4"));
				result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
				result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
				result += XMLUtils.getEndNode("record");
			}
			result += XMLUtils.getEndNode("BatchDetails");
		}
		return result;
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}

}



	
	
	@SuppressWarnings("all")
	public String getBatchDetail(String aPlant, String aItem, String loc,String userID)
			throws Exception {
		String result = "";
	    
	    UserLocUtil userLocUtil = new UserLocUtil();
	    userLocUtil.setmLogger(mLogger);
	    String condAssignedLocforUser = " "; 
	    condAssignedLocforUser =  userLocUtil.getUserLocAssigned(userID,aPlant,"LOC");
            
	    ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY from "
				+ aPlant + "_INVMST WHERE LOC='" + loc + "' AND PLANT='"
				+ aPlant + "' AND ITEM='" + aItem + "' AND QTY >0 " +condAssignedLocforUser);
				
		sql.append(" ORDER BY USERFLD4 , QTY ");

		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			this.mLogger.query(itM.isPrintQuery(), sql.toString());

			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("batchId", hashMap
							.get("USERFLD4"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					result += XMLUtils.getEndNode("record");
				}

				result += XMLUtils.getEndNode("BatchDetails");
			}
			MLogger.log(0, "[XML] " + result);
			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

	}

	@SuppressWarnings("all")
	public String getBatchDetailForTransferOrder(String aPlant, String aItem,
			String loc,String userID) throws Exception {
		String result = "";
	    UserLocUtil userLocUtil = new UserLocUtil();
	    userLocUtil.setmLogger(mLogger);
	    String condAssignedLocforUser = " "; 
	    condAssignedLocforUser =  userLocUtil.getUserLocAssigned(userID,aPlant,"LOC");
	    ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY from "
				+ aPlant + "_INVMST WHERE LOC='" + loc + "' AND PLANT='"
				+ aPlant + "' AND ITEM='" + aItem + "' AND QTY >0 "+condAssignedLocforUser);

		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			ItemMstDAO itemM = new ItemMstDAO();
			itemM.setmLogger(mLogger);
			itM.setmLogger(mLogger);
			this.mLogger.query(itM.isPrintQuery(), sql.toString());

			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
                                String uom =itemM.getItemUOM(aPlant, aItem);
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("batchId", hashMap.get("USERFLD4"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					result += XMLUtils.getXMLNode("uom",uom);
					result += XMLUtils.getXMLNode("loc", loc);
					result += XMLUtils.getEndNode("record");
				}

				result += XMLUtils.getEndNode("BatchDetails");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

	}
        
        
    public boolean isValidInventoryData(String aPlant,String aItem, String aLoc,String batch,String Qty,String RsnCode,String user) throws Exception {
            boolean isValidInventory = false;
            try {
                Hashtable htInvMst = new Hashtable();
                
                htInvMst.clear();
                htInvMst.put(IDBConstants.PLANT, aPlant);
                if(aItem.length()>0){
                htInvMst.put(IDBConstants.ITEM,aItem);
                isValidInventory = _InvMstDAO.isExisit(htInvMst,"  QTY >0");
                if(!isValidInventory){
                    throw new Exception(" Scan/Enter a Valid Product ID");
                }
                }
                
                if(aLoc.length()>0){
                    UserLocUtil uslocUtil = new UserLocUtil();
                    uslocUtil.setmLogger(mLogger);
                    String extraCon= uslocUtil.getUserLocAssigned(user,aPlant,"LOC");
                htInvMst.put(IDBConstants.LOC, aLoc);
                isValidInventory = _InvMstDAO.isExisit(htInvMst,"  QTY >0"+extraCon);
	                if(!isValidInventory){
	                    throw new Exception("No Inventory found!");
	                }
                }
                
                if(batch.length()>0){
                htInvMst.put(IDBConstants.USERFLD4, batch);
                isValidInventory = _InvMstDAO.isExisit(htInvMst,"  QTY >0");
                if(!isValidInventory){
                    throw new Exception(" Scan/Enter a Valid Batch");
                }
                }
                
                if(Qty.length()>0){
                int tranQty = Integer.parseInt(Qty.trim());
                isValidInventory = _InvMstDAO.isExisit(htInvMst,"  QTY >= "+tranQty);
                if(!isValidInventory){
                    throw new Exception("Issuing qty should less than available Qty!");
                }
                } 
                if(RsnCode.length()>0){
                RsnMst rnmstDAo =   new RsnMst ();
                rnmstDAo.setmLogger(mLogger);

                Hashtable htrsnMst = new Hashtable();
                
                htrsnMst.clear();
                htrsnMst.put(IDBConstants.PLANT, aPlant);
                htrsnMst.put(IDBConstants.RSNCODE,RsnCode);
                boolean isValidrsnCode =rnmstDAo.isExists(htrsnMst);
                   
                  
                if(!isValidrsnCode){
                    throw new Exception("Please Enter a Valid Reason Code");
                }
                }

            } catch (Exception e) {
            throw e;
            }
            return isValidInventory;
    }
    
    
    
    
    
    
    @SuppressWarnings("all")
    public String getAvailableInventoryQty(String aPlant, String aItem,
                    String loc,String batch,String userID) throws Exception {
        String Qty="";
        UserLocUtil userLocUtil = new UserLocUtil();
        userLocUtil.setmLogger(mLogger);
        String condAssignedLocforUser = " "; 
        condAssignedLocforUser =  userLocUtil.getUserLocAssigned(userID,aPlant,"LOC");
        ArrayList<Map<String, String>> alData = new ArrayList<>();
            java.sql.Connection con = null;
            StringBuffer sql = new StringBuffer("SELECT QTY from "
                            + aPlant + "_INVMST WHERE LOC='" + loc + "' AND PLANT='"
                            + aPlant + "' AND ITEM='" + aItem + "' AND USERFLD4='"+batch+"' AND LOC='"+loc+"' AND QTY >0 "+condAssignedLocforUser);

            try {
                    con = DbBean.getConnection();
                    InvMstDAO itM = new InvMstDAO();
                    ItemMstDAO itemM = new ItemMstDAO();
                    itemM.setmLogger(mLogger);
                    itM.setmLogger(mLogger);
                    this.mLogger.query(itM.isPrintQuery(), sql.toString());

                    alData = itM.selectData(con, sql.toString());
                    if (alData.size() > 0) {
                        for (Map<String, String> hashMap : alData) {
                               Qty= hashMap.get("QTY");
                        }
                    }

                    return Qty;
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            } finally {
                    DbBean.closeConnection(con);
            }

    }
    
    public boolean isValidInventory(String aPlant,String aItem, String aLoc,String batch) throws Exception {
            boolean isValidInventory = false;
            try {
                Hashtable htInvMst = new Hashtable();
                
                htInvMst.put(IDBConstants.PLANT, aPlant);
              
                htInvMst.put(IDBConstants.ITEM,aItem);
                htInvMst.put(IDBConstants.LOC, aLoc);
                htInvMst.put(IDBConstants.BATCH , batch);
                isValidInventory = _InvMstDAO.isExisit(htInvMst,"  QTY >0");
                if(!isValidInventory){
                    throw new Exception(" Scan/Enter a Valid Product ID");
                }
           
            
            } catch (Exception e) {
            throw e;
            }
            return isValidInventory;
    }

    /* To load transfer Pick batch */
    @SuppressWarnings("all")
	public String getBatchDetailForTransferRandom(String aPlant, String aItem,
			String loc,String batch,String userID) throws Exception {
		String result = "";
	    UserLocUtil userLocUtil = new UserLocUtil();
	    userLocUtil.setmLogger(mLogger);
	    String condAssignedLocforUser = " "; 
	    condAssignedLocforUser =  userLocUtil.getUserLocAssigned(userID,aPlant,"LOC");
	    ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT USERFLD4, QTY from "
				+ aPlant + "_INVMST WHERE  LOC='" + loc + "'  AND PLANT='"
				+ aPlant + "' AND ITEM='" + aItem + "' AND QTY >0 "+condAssignedLocforUser);

		//+ aPlant + "_INVMST WHERE  LOC='" + loc + "' AND USERFLD4 LIKE '%" + batch + "%' AND PLANT='"

		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			ItemMstDAO itemM = new ItemMstDAO();
			itemM.setmLogger(mLogger);
			itM.setmLogger(mLogger);
			this.mLogger.query(itM.isPrintQuery(), sql.toString());

			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
                                String uom =itemM.getItemUOM(aPlant, aItem);
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("batchId", hashMap.get("USERFLD4"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					result += XMLUtils.getXMLNode("uom",uom);
					result += XMLUtils.getXMLNode("loc", loc);
					result += XMLUtils.getEndNode("record");
				}

				result += XMLUtils.getEndNode("BatchDetails");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

	}
    
    @SuppressWarnings("all")
	public String getBatchDetailByBatchTransferRandom(String aPlant, String aItem,
			String loc,String batch,String userID) throws Exception {
		String result = "";
	    UserLocUtil userLocUtil = new UserLocUtil();
	    userLocUtil.setmLogger(mLogger);
	    String condAssignedLocforUser = " "; 
	    condAssignedLocforUser =  userLocUtil.getUserLocAssigned(userID,aPlant,"LOC");
	    ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT USERFLD4, QTY from "
				+ aPlant + "_INVMST WHERE  LOC='" + loc + "' AND USERFLD4 LIKE '%" + batch + "%' AND PLANT='"
				+ aPlant + "' AND ITEM='" + aItem + "' AND QTY >0 "+condAssignedLocforUser);


		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			ItemMstDAO itemM = new ItemMstDAO();
			itemM.setmLogger(mLogger);
			itM.setmLogger(mLogger);
			this.mLogger.query(itM.isPrintQuery(), sql.toString());

			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
                                String uom =itemM.getItemUOM(aPlant, aItem);
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("batchId", hashMap.get("USERFLD4"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					result += XMLUtils.getXMLNode("uom",uom);
					result += XMLUtils.getXMLNode("loc", loc);
					result += XMLUtils.getEndNode("record");
				}

				result += XMLUtils.getEndNode("BatchDetails");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

	}
    
    
    public String getLocDetailWithBatch(String aPlant, String user,String aItem,String batch,String loc)
	throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		        UserLocUtil userLocUtil = new UserLocUtil();
		        userLocUtil.setmLogger(mLogger);
		        StringBuffer sql = new StringBuffer("SELECT USERFLD4,sum(QTY) QTY,LOC," 
		        +"(SELECT ISNULL(NONSTKFLAG,'') from "+ aPlant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK FROM "
				+ aPlant + "_INVMST A WHERE PLANT='" + aPlant + "' AND ITEM='"
				+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%'  AND USERFLD4 ='" + batch + "'  AND LOC LIKE '%" + loc + "%' group by item,loc,userfld4" );
		
		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			this.mLogger.query(itM.isPrintQuery(), sql.toString());
		
			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("LocDetails total='"
						+ alData.size() + "'");
		
				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					//result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					result += XMLUtils.getXMLNode("isnonstock", hashMap.get("ISNONSTOCK"));
					result += XMLUtils.getEndNode("record");
				}
				result += XMLUtils.getEndNode("LocDetails");
			}
			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		
   }
    
    public String checkLocDetailWithBatch(String aPlant, String user,String aItem,String batch,String loc)
	throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		        UserLocUtil userLocUtil = new UserLocUtil();
		        userLocUtil.setmLogger(mLogger);
		         StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY, LOC from "
				+ aPlant + "_INVMST WHERE PLANT='" + aPlant + "' AND ITEM='"
				+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%'  AND USERFLD4 ='" + batch + "'  AND LOC = '" + loc + "' " );
		        		
		try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			this.mLogger.query(itM.isPrintQuery(), sql.toString());
		
			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("LocDetails total='"
						+ alData.size() + "'");
		
				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					result += XMLUtils.getEndNode("record");
				}
				result += XMLUtils.getEndNode("LocDetails");
			}
			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		
   }
  
  //---Added by Bruhan on March 6 2014, Description: To Process WIP Wms --ManufacturingModuelsChange
   /* private boolean process_Wms_MoveToWIPforPC(Map map) throws Exception {
		boolean flag = false;
		WmsMoveToWIP tran = new WmsMoveToWIP();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;

	}*/
    
    //---Added by Bruhan on March 6 2014, Description: To Process WIP Reverse Wms --ManufacturingModuelsChange
    /*public boolean process_WIPReverseforPC(Map obj) throws Exception {
        String xmlStr = "";
        boolean flag = false;
        UserTransaction ut = null;
        try {
                flag = process_Wms_WIPReverse(obj);

        } catch (Exception e) {
                flag = false;
               
              throw e;
        }
     
        return flag;
  }*/
    //---Added by Bruhan on March 6 2014, Description: To Process WIP Reverse Wms --ManufacturingModuelsChange
   /* private boolean process_Wms_WIPReverse(Map map) throws Exception {
		boolean flag = false;
		WmsWIPReverse tran = new WmsWIPReverse();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
	try{
			flag = tran.processWmsTran(map);
	}catch(Exception e){
		flag = false;
        throw e;
	}
		return flag;

	}
  //start code by Bruhan for wip reporting  on 08/may/2014     
    public boolean process_WIPReporting(Map obj) throws Exception {
        String xmlStr = "";
        boolean flag = false;
        UserTransaction ut = null;
        try {
                flag = process_Wms_WIPReporting(obj);

        } catch (Exception e) {
                flag = false;
               
              throw e;
        }
     
        return flag;
  }
    
    private boolean process_Wms_WIPReporting(Map map) throws Exception {
		boolean flag = false;
		WmsWIPReporting tran = new WmsWIPReporting();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;

	}*/

  //end

 /*---Added by Bruhan on July 9 2014, Description:PDA Inventory Query by location with location type
	 ********** Modification History *************************************
	 * 
	 * 
	 * 
	 */
    public String getInvItemDetailsForLocWithLocType(String Plant, String aLoc,String aLocType) {
        String xmlStr = "";
        ArrayList al = null;
        String extLocTypeCond="";

        String query = "item,userfld4,loc,qty,(select itemdesc from "+Plant+"_ITEMMST where item= "+Plant+"_INVMST.item) as ItemDesc,(select stkuom from "+Plant+"_ITEMMST where item= "+Plant+"_INVMST.item) as uom ";
        String extCond = " qty >0 ORDER BY item, userfld4 ";
        if(aLocType.length() > 0)
        {
        	   extLocTypeCond=" loc in(Select loc from "+Plant+"_locmst where loc_type_id='" +  aLocType + "')";
        }
           
        Hashtable ht = new Hashtable();
        if(aLoc.length() > 0)
        {
        	ht.put("Loc", aLoc);
        }
         ht.put("plant", Plant);
        try {
                _InvMstDAO.setmLogger(mLogger);
                _ItemMstDAO.setmLogger(mLogger);
                al = _InvMstDAO.selectInvMst(query, ht, extCond,extLocTypeCond);

                if (al.size() > 0) {
                 
                        xmlStr += XMLUtils.getXMLHeader();
                        xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
                        for (int i = 0; i < al.size(); i++) {
                                Map map = (Map) al.get(i);
                                xmlStr += XMLUtils.getStartNode("record");
                                xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
                                xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA((String) map.get("ItemDesc")));
                                xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
                                 xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
                              xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("uom"));
                                xmlStr += XMLUtils.getEndNode("record");
                        }
                        xmlStr += XMLUtils.getEndNode("itemDetails");
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return xmlStr;
    }
    /*---Added by Bruhan on July 9 2014, Description:PDA Inventory Query by product with product class,type and brand 
		 ********** Modification History *************************************
	 
	 * 
	 * 
	 * 
	 */     
    public String getItemDetailsWithType(String Plant, String Item,String searchbyone,String searchvalueone) {
    	String xmlStr = "";
		ArrayList al = null;
		 String extTypeCond="";
		String query = "item,userfld4,loc,qty ";
		String extCond = " qty >0 ORDER BY item, userfld4 ";
		Hashtable ht = new Hashtable();
		 if(Item.length() > 0)
		 {
		    ht.put("item", Item);
		 }
		ht.put("plant", Plant);
		
		if (searchbyone.equals("ProductClassID")){
			extTypeCond ="  ITEM IN (SELECT ITEM FROM "+Plant+"_ITEMMST WHERE PRD_CLS_ID = '" + searchvalueone+ "')";
		}
		if (searchbyone.equals("ProductTypeID")){
			extTypeCond ="  ITEM IN (SELECT ITEM FROM "+Plant+"_ITEMMST WHERE ITEMTYPE = '" + searchvalueone + "')";
		}
		if (searchbyone.equals("ProductBrandID")){
			extTypeCond ="  ITEM IN (SELECT ITEM FROM "+Plant+"_ITEMMST WHERE PRD_BRAND_ID = '" + searchvalueone + "')";
		}
	
		try {
			_InvMstDAO.setmLogger(mLogger);
			_ItemMstDAO.setmLogger(mLogger);
			al = _InvMstDAO.selectInvMst(query, ht, extCond,extTypeCond);

			if (al.size() > 0) {
			    String itemDesc =_ItemMstDAO .getItemDesc(Plant,Item);
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
					xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("loc", (String) map.get("loc"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
    //start code by Bruhan for wip reporting reversal on 14/oct/2014    
    /*public boolean process_WIPReportingReversal(Map obj) throws Exception {
        String xmlStr = "";
        boolean flag = false;
        UserTransaction ut = null;
        try {
                flag = process_Wms_WIPReportingReversal(obj);

        } catch (Exception e) {
                flag = false;
               
              throw e;
        }
     
        return flag;
  }*/
    
   /* private boolean process_Wms_WIPReportingReversal(Map map) throws Exception {
		boolean flag = false;
		WmsWIPReportingReversal tran = new WmsWIPReportingReversal();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;

	}*/
    
    /*---Added by Bruhan on nov 19 2014, Description:for wip adjustment function 
	 ********** Modification History *************************************
 
 * 
 * 
 * 
 */   
   /* public boolean process_WIPAdjustment(Map obj) throws Exception {
        String xmlStr = "";
        boolean flag = false;
        UserTransaction ut = null;
        try {
                flag = process_Wms_WIPAdjustment(obj);

        } catch (Exception e) {
                flag = false;
               
              throw e;
        }
     
        return flag;
  }*/
    
  /*  private boolean process_Wms_WIPAdjustment(Map map) throws Exception {
		boolean flag = false;
		WmsWIPAdjustment tran = new WmsWIPAdjustment();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;

	}*/
 


 //end  added by Bruhan for putaway       
	public String loadAssignLocationsXmlPDA(String aPlant,String aLoc,String user) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = new Hashtable();
		queryDoHdr = " LOC,LOCDESC";
		htInvMst.put("PLANT", aPlant);
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);

		LocMstDAO locMstDAO = new LocMstDAO();
		locMstDAO.setmLogger(mLogger);
		try {
			alInvMst = _InvMstDAO.getAssignLocByPDA(aPlant, aLoc,user);
		
			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocationList total='"
						+ String.valueOf(alInvMst.size()) + "'");

			for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					String locDescription = (String) map1.get("LocDesc");
					String locName = (String) map1.get("FromLoc");

					xmlStr += XMLUtils.getStartNode("LocationDetail");
					xmlStr += XMLUtils.getXMLNode("locationId", strUtils.replaceCharacters2SendPDA(locName));
					xmlStr += XMLUtils.getXMLNode("description",strUtils.replaceCharacters2SendPDA(locDescription));
					xmlStr += XMLUtils.getEndNode("LocationDetail");
				}
				xmlStr += XMLUtils.getEndNode("LocationList");
			} else {
				throw new Exception("No location found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

 public String loadAssignInvLocationsXmlPDA(String aPlant,String aLoc,String aItem,String user) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = new Hashtable();
		queryDoHdr = " LOC,LOCDESC";
		htInvMst.put("PLANT", aPlant);
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);

		LocMstDAO locMstDAO = new LocMstDAO();
		locMstDAO.setmLogger(mLogger);
		try {
			alInvMst = _InvMstDAO.getAssignInvLocByPDA(aPlant, aLoc,aItem,user);
		
			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocationList total='"
						+ String.valueOf(alInvMst.size()) + "'");

				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					String locDescription = (String) map1.get("LocDesc");
					String locName = (String) map1.get("Loc");
					xmlStr += XMLUtils.getStartNode("LocationDetail");
					xmlStr += XMLUtils.getXMLNode("loc", strUtils.replaceCharacters2SendPDA(locName));
					xmlStr += XMLUtils.getXMLNode("desc",strUtils.replaceCharacters2SendPDA(locDescription));
					xmlStr += XMLUtils.getEndNode("LocationDetail");
				}
				xmlStr += XMLUtils.getEndNode("LocationList");
				System.out.print(xmlStr);
			} else {
				throw new Exception("No location found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}
 
 
 /*public String validate_locationbck(String aPlant,String aLoc,String aItem,String user) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = new Hashtable();
		queryDoHdr = " LOC,LOCDESC";
		htInvMst.put("PLANT", aPlant);
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);

		LocMstDAO locMstDAO = new LocMstDAO();
		locMstDAO.setmLogger(mLogger);
		
		String PLANT = StrUtils.fString(request.getParameter("PLANT"));
		String dono = StrUtils.fString(request.getParameter("DONO"));
		String item = StrUtils.fString(request.getParameter("ITEM"));
		String loc = StrUtils.fString(request.getParameter("LOC"));
		try {
			alInvMst = _InvMstDAO.getAssignInvLocByPDA(aPlant, aLoc,aItem,user);
		
			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocationList total='"
						+ String.valueOf(alInvMst.size()) + "'");

				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					String locDescription = (String) map1.get("LocDesc");
					String locName = (String) map1.get("Loc");
					xmlStr += XMLUtils.getStartNode("LocationDetail");
					xmlStr += XMLUtils.getXMLNode("loc", strUtils.replaceCharacters2SendPDA(locName));
					xmlStr += XMLUtils.getXMLNode("desc",strUtils.replaceCharacters2SendPDA(locDescription));
					xmlStr += XMLUtils.getEndNode("LocationDetail");
				}
				xmlStr += XMLUtils.getEndNode("LocationList");
				System.out.print(xmlStr);
			} else {
				throw new Exception("No location found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}*/

  
 private String validate_location(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "",xmlStr="";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String dono = StrUtils.fString(request.getParameter("DONO"));
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String loc = StrUtils.fString(request.getParameter("LOC"));
			
			ArrayList alInvMst = null;
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO.ValidateOBLocation(plant,dono,item,loc);
									
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
			
			if (alInvMst.size() > 0) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Location found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Location not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("locationDetails");
				

			
		} catch (Exception e) {
			throw e;
		}
		// MLogger.log(0, "validate_location() Ends");
		return xmlStr;
	}
 
 public String getOBLocDetailsByCreatedDate(String aPlant, String user,String aItem,String loc)
	throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		java.sql.Connection con = null;
		        UserLocUtil userLocUtil = new UserLocUtil();
		        userLocUtil.setmLogger(mLogger);
		        /*StringBuffer sql = new StringBuffer("SELECT ISNULL(LOC,'') LOC,ISNULL(USERFLD4,'') BATCH,ISNULL(QTY,'') QTY, " 
		        +" SUBSTRING(CRAT,7,2)+'/'+ ISNULL(SUBSTRING(CRAT,5,2)+'/'+ SUBSTRING(CRAT,1,4),'') as CRAT,(SELECT ISNULL(LOCDESC,'') from "+ aPlant + "_LOCMST WHERE LOC=A.LOC) LOCDESC  from "
				+ aPlant + "_INVMST A WHERE PLANT='" + aPlant + "' AND ITEM='"
				+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND LOC LIKE '%" + loc + "%' ORDER BY LOC,CRAT");*/
		
		        StringBuffer sql = new StringBuffer("SELECT DISTINCT ISNULL(LOC,'') LOC, " 
				        +" (SELECT ISNULL(LOCDESC,'') from "+ aPlant + "_LOCMST WHERE LOC=A.LOC) LOCDESC  from "
						+ aPlant + "_INVMST A WHERE PLANT='" + aPlant + "' AND ITEM='"
						+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND LOC LIKE '%" + loc + "%' ORDER BY LOC");
		        try {
			con = DbBean.getConnection();
			InvMstDAO itM = new InvMstDAO();
			this.mLogger.query(itM.isPrintQuery(), sql.toString());
		
			alData = itM.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("LocDetails total='"
						+ alData.size() + "'");
		
				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
					result += XMLUtils.getXMLNode("batch", hashMap.get("BATCH"));
					result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
					//result += XMLUtils.getXMLNode("expirydate", hashMap.get("EXPIRYDATE"));
					result += XMLUtils.getXMLNode("createddate", hashMap.get("CRAT"));
					result += XMLUtils.getXMLNode("locdesc", hashMap.get("LOCDESC"));
					result += XMLUtils.getEndNode("record");
				}
				result += XMLUtils.getEndNode("LocDetails");
			}
			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		
   }
 public String getOBLocDetailsByExpiryDate(String aPlant, String user,String aItem,String loc)
			throws Exception {
				String result = "";
				ArrayList<Map<String, String>> alData = new ArrayList<>();
				java.sql.Connection con = null;
				        UserLocUtil userLocUtil = new UserLocUtil();
				        userLocUtil.setmLogger(mLogger);
				        StringBuffer sql = new StringBuffer("SELECT ISNULL(LOC,'') LOC,ISNULL(USERFLD4,'') BATCH,ISNULL(QTY,'') QTY, " 
				        +"ISNULL(EXPIREDATE,'') EXPIRYDATE,(SELECT ISNULL(LOCDESC,'') from "+ aPlant + "_LOCMST WHERE LOC=A.LOC) LOCDESC from "
						+ aPlant + "_INVMST A WHERE PLANT='" + aPlant + "' AND ITEM='"
						+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND LOC LIKE '%" + loc + "%' ORDER BY LOC,EXPIREDATE");
				
				try {
					con = DbBean.getConnection();
					InvMstDAO itM = new InvMstDAO();
					this.mLogger.query(itM.isPrintQuery(), sql.toString());
				
					alData = itM.selectData(con, sql.toString());
					if (alData.size() > 0) {
						result = XMLUtils.getXMLHeader();
						result += XMLUtils.getStartNode("LocDetails total='"
								+ alData.size() + "'");
				
						for (Map<String, String> hashMap : alData) {
							result += XMLUtils.getStartNode("record");
							result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
							result += XMLUtils.getXMLNode("batch", hashMap.get("BATCH"));
							result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
							result += XMLUtils.getXMLNode("expirydate", hashMap.get("EXPIRYDATE"));
							result += XMLUtils.getXMLNode("locdesc", hashMap.get("LOCDESC"));
							result += XMLUtils.getEndNode("record");
						}
						result += XMLUtils.getEndNode("LocDetails");
					}
					return result;
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					DbBean.closeConnection(con);
				}
				
		   }
 
 public String getOBBatchDetailsByCreatedDate(String aPlant, String user,String aItem,String loc,String batch)
			throws Exception {
				String result = "";
				ArrayList<Map<String, String>> alData = new ArrayList<>();
				java.sql.Connection con = null;
				        UserLocUtil userLocUtil = new UserLocUtil();
				        userLocUtil.setmLogger(mLogger);
				        StringBuffer sql = new StringBuffer("SELECT ISNULL(LOC,'') LOC,ISNULL(USERFLD4,'') BATCH,ISNULL(QTY,'') QTY, " 
				        +" SUBSTRING(CRAT,7,2)+'/'+ ISNULL(SUBSTRING(CRAT,5,2)+'/'+ SUBSTRING(CRAT,1,4),'') as CRAT,"
				        + " ,(SELECT ISNULL(NONSTKFLAG,'') from "+ aPlant + "_ITEMMST WHERE ITEM=A.ITEM) ISNONSTOCK from "
						+ aPlant + "_INVMST A WHERE PLANT='" + aPlant + "' AND ITEM='"
						+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND LOC ='" + loc + "' " +
								" AND USERFLD4 LIKE '%" + batch + "%'ORDER BY LOC,USERFLD4,CRAT");
				
				try {
					con = DbBean.getConnection();
					InvMstDAO itM = new InvMstDAO();
					this.mLogger.query(itM.isPrintQuery(), sql.toString());
				
					alData = itM.selectData(con, sql.toString());
					if (alData.size() > 0) {
						result = XMLUtils.getXMLHeader();
						result += XMLUtils.getStartNode("LocDetails total='"
								+ alData.size() + "'");
				
						for (Map<String, String> hashMap : alData) {
							result += XMLUtils.getStartNode("record");
							result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
							result += XMLUtils.getXMLNode("batch", hashMap.get("BATCH"));
							result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
							//result += XMLUtils.getXMLNode("expirydate", hashMap.get("EXPIRYDATE"));
							result += XMLUtils.getXMLNode("createddate", hashMap.get("CRAT"));
							result += XMLUtils.getEndNode("record");
						}
						result += XMLUtils.getEndNode("LocDetails");
					}
					return result;
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					DbBean.closeConnection(con);
				}
				
		   }
		 public String getOBBatchDetailsByExpiryDate(String aPlant, String user,String aItem,String loc,String batch)
					throws Exception {
						String result = "";
						ArrayList<Map<String, String>> alData = new ArrayList<>();
						java.sql.Connection con = null;
						        UserLocUtil userLocUtil = new UserLocUtil();
						        userLocUtil.setmLogger(mLogger);
						        StringBuffer sql = new StringBuffer("SELECT ISNULL(LOC,'') LOC,ISNULL(USERFLD4,'') BATCH,ISNULL(QTY,'') QTY, " 
						        +"ISNULL(EXPIREDATE,'') EXPIRYDATE from "
								+ aPlant + "_INVMST WHERE PLANT='" + aPlant + "' AND ITEM='"
								+ aItem + "' AND QTY >0  AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP%' AND LOC ='" + loc + "'  AND USERFLD4 LIKE '%" + batch + "%'ORDER BY LOC,USERFLD4,EXPIREDATE");
						
						try {
							con = DbBean.getConnection();
							InvMstDAO itM = new InvMstDAO();
							this.mLogger.query(itM.isPrintQuery(), sql.toString());
						
							alData = itM.selectData(con, sql.toString());
							if (alData.size() > 0) {
								result = XMLUtils.getXMLHeader();
								result += XMLUtils.getStartNode("LocDetails total='"
										+ alData.size() + "'");
						
								for (Map<String, String> hashMap : alData) {
									result += XMLUtils.getStartNode("record");
									result += XMLUtils.getXMLNode("loc", hashMap.get("LOC"));
									result += XMLUtils.getXMLNode("batch", hashMap.get("BATCH"));
									result += XMLUtils.getXMLNode("qty", StrUtils.formatNum(hashMap.get("QTY")));
									result += XMLUtils.getXMLNode("expirydate", hashMap.get("EXPIRYDATE"));
									//result += XMLUtils.getXMLNode("createddate", hashMap.get("CRAT"));
									result += XMLUtils.getEndNode("record");
								}
								result += XMLUtils.getEndNode("LocDetails");
							}
							return result;
						} catch (Exception e) {
							this.mLogger.exception(this.printLog, "", e);
							throw e;
						} finally {
							DbBean.closeConnection(con);
						}
						
				   }
	/**
	 * Provides list of products that are below the minimum quantity level
	 * 
	 * @param plant
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList getLowQuantityProducts(String plant) throws Exception {
		ArrayList al = null;
		try {
			String aQuery = "select IT.ITEM AS PRODUCT, IT.ITEMDESC, ISNULL(IT.INVENTORYUOM,'') INVENTORYUOM, AVG(STKQTY) AS MIN_QUANTITY, SUM(IM.QTY) AS CURR_QUANTITY, ((SUM(IM.QTY)/AVG(STKQTY))*100) as TOTAL_AVG from ["
					+ plant + "_ITEMMST] AS IT INNER JOIN [" + plant
					+ "_INVMST] AS IM ON IT.ITEM = IM.ITEM group by IT.ITEM,IT.ITEMDESC, IT.INVENTORYUOM HAVING AVG(STKQTY) > SUM(IM.QTY)";

			al = _InvMstDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	public String getMutiUombatchDetails(String Plant, String Item,String loc,String aUser,String batch,String uom) throws Exception {
		String xmlStr = "";
		ArrayList al = null;
                
                UserLocUtil userLocUtil = new UserLocUtil();
                userLocUtil.setmLogger(mLogger);
         
		try {
			_InvMstDAO.setmLogger(mLogger);
			al = new InvMstDAO().getAvaliableQtyForBatchMutiUOMPDA(Plant, Item, loc, batch,uom);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("batch", (String) map
							.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
					xmlStr += XMLUtils.getXMLNode("createddate", (String) map.get("CRAT"));
					xmlStr += XMLUtils.getXMLNode("invid", (String) map.get("ID"));
					xmlStr += XMLUtils.getXMLNode("expirydate", (String) map.get("EXPIRYDATE"));
					
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	
	public String getMutiUombatchDetailsForBatchAvlQty(String Plant, String Item,String loc,String aUser,String batch,String uom) throws Exception {
		String xmlStr = "";
		ArrayList al = null;
                
                UserLocUtil userLocUtil = new UserLocUtil();
                userLocUtil.setmLogger(mLogger);
         
		try {
			_InvMstDAO.setmLogger(mLogger);
			al = new InvMstDAO().getAvaliableQtyForSingleBatchMutiUOMPDA(Plant, Item, loc, batch,uom);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("batch", (String) map
							.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
					xmlStr += XMLUtils.getXMLNode("createddate", (String) map.get("CRAT"));
					xmlStr += XMLUtils.getXMLNode("invid", (String) map.get("ID"));
					xmlStr += XMLUtils.getXMLNode("expirydate", (String) map.get("EXPIRYDATE"));
					
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	
	public String getMutiUombatchDetailsForSpinner(String Plant, String Item,String loc,String aUser,String batch,String uom) throws Exception {
		String xmlStr = "";
		ArrayList al = null;
                
                UserLocUtil userLocUtil = new UserLocUtil();
                userLocUtil.setmLogger(mLogger);
         
		try {
			_InvMstDAO.setmLogger(mLogger);
			al = new InvMstDAO().getAvaliableQtyForBatchMutiUOMPDASpinner(Plant, Item, loc, batch,uom);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("batch", (String) map
							.get("userfld4"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
					xmlStr += XMLUtils.getXMLNode("uomqty", StrUtils.formatNum((String) map.get("UOMQTY")));
					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
					xmlStr += XMLUtils.getXMLNode("createddate", (String) map.get("CRAT"));
					xmlStr += XMLUtils.getXMLNode("invid", (String) map.get("ID"));
					xmlStr += XMLUtils.getXMLNode("expirydate", (String) map.get("EXPIRYDATE"));
					
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	public String getInvItemDetailsForLoc_PDA(String Plant, String aLoc, String uom, String item) {

        String xmlStr = "";
        ArrayList al = null;

    
        try {
                _InvMstDAO.setmLogger(mLogger);
                _ItemMstDAO.setmLogger(mLogger);
                al = _InvMstDAO.getAvaliableQtyForInvLocationUOMPDAwithItem(Plant, aLoc, uom,item);

                if (al.size() > 0) {
                 
                        xmlStr += XMLUtils.getXMLHeader();
                        xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
                        for (int i = 0; i < al.size(); i++) {
                                Map map = (Map) al.get(i);
                                xmlStr += XMLUtils.getStartNode("record");
                                String itemDesc =_ItemMstDAO .getItemDesc(Plant,(String) map.get("item"));
                			    String uom1 =_ItemMstDAO.getInvUOM(Plant,(String) map.get("item"));
                                xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
                                xmlStr += XMLUtils.getXMLNode("loc", (String) map.get("loc"));
                                xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
            					xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
            					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
            					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
                                 xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
                                 if("".equals(uom)){
                 					xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom1));
                 				    }
                 				    else
                 				    {
                 				    	xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom));
                 				    }
                                xmlStr += XMLUtils.getEndNode("record");
                        }
                        xmlStr += XMLUtils.getEndNode("itemDetails");
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return xmlStr;
}
    public String getItemDetails_PDA(String Plant, String Item, String uom,String loc) {

	String xmlStr = "";
	ArrayList al = null;

	try {
		_InvMstDAO.setmLogger(mLogger);
		_ItemMstDAO.setmLogger(mLogger);
		al = _InvMstDAO.getAvaliableQtyForInvProductUOMPDAwithLoc(Plant,Item,uom,loc);

		if (al.size() > 0) {
			String itemDesc =_ItemMstDAO .getItemDesc(Plant,Item);
			String uom1 =_ItemMstDAO.getInvUOM(Plant,Item);
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("itemDetails total='"
					+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
				xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
			    if("".equals(uom)){
				xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom1));
			    }
			    else
			    {
			    	xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom));
			    }
				xmlStr += XMLUtils.getXMLNode("loc", (String) map.get("loc"));
				xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
				xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
				xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
				xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("itemDetails");
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return xmlStr;
}
    public String getInvItemDetailsFor_Prd_Loc_PDA(String Plant, String aLoc, String uom, String item) {

        String xmlStr = "";
        ArrayList al = null;

    
        try {
                _InvMstDAO.setmLogger(mLogger);
                _ItemMstDAO.setmLogger(mLogger);
                al = _InvMstDAO.getAvaliableQtyForInvPrd_LocUOMPDA(Plant, aLoc, uom,item);

                if (al.size() > 0) {
                 
                        xmlStr += XMLUtils.getXMLHeader();
                        xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
                        for (int i = 0; i < al.size(); i++) {
                                Map map = (Map) al.get(i);
                                xmlStr += XMLUtils.getStartNode("record");
                                String itemDesc =_ItemMstDAO .getItemDesc(Plant,(String) map.get("item"));
                			    String uom1 =_ItemMstDAO.getInvUOM(Plant,(String) map.get("item"));
                                xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
                                xmlStr += XMLUtils.getXMLNode("loc", (String) map.get("loc"));
                                xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2SendPDA(itemDesc));
            					xmlStr += XMLUtils.getXMLNode("batch", (String) map.get("userfld4"));
            					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("pcsqty")));
            					xmlStr += XMLUtils.getXMLNode("isnonstock",(String) map.get("ISNONSTOCK"));
                                 xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("qty")));
                                 if("".equals(uom)){
                 					xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom1));
                 				    }
                 				    else
                 				    {
                 				    	xmlStr += XMLUtils.getXMLNode("uom", StrUtils.replaceCharacters2SendPDA(uom));
                 				    }
                                xmlStr += XMLUtils.getEndNode("record");
                        }
                        xmlStr += XMLUtils.getEndNode("itemDetails");
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return xmlStr;
}
  //created by vicky Desc:Used for getting All To locations in PDA stock move
	public String loadLocationsForPDA(String aPlant,String aLoc,String user) throws Exception {
		String xmlStr = "";
		ArrayList alInvMst = null;
		String queryDoHdr = "";
		Hashtable htInvMst = new Hashtable();
		queryDoHdr = " LOC,LOCDESC";
		htInvMst.put("PLANT", aPlant);
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);

		LocMstDAO locMstDAO = new LocMstDAO();
		locMstDAO.setmLogger(mLogger);
		try {
			alInvMst = _InvMstDAO.getStockTransferLocForPDA(aPlant, aLoc,user);
		
			if (alInvMst.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("LocationList total='"
						+ String.valueOf(alInvMst.size()) + "'");

			
				for (int i = 0; i < alInvMst.size(); i++) {
					Map map1 = (Map) alInvMst.get(i);
					String locDescription = (String) map1.get("LocDesc");
					String locName = (String) map1.get("FromLoc");

					xmlStr += XMLUtils.getStartNode("LocationDetail");
					//xmlStr += XMLUtils.getXMLNode("locationId", strUtils.replaceCharacters2SendPDA(locName));
					//xmlStr += XMLUtils.getXMLNode("description",strUtils.replaceCharacters2SendPDA(locDescription));
					xmlStr += XMLUtils.getXMLNode("locationId", locName);
					xmlStr += XMLUtils.getXMLNode("description",locDescription);
					xmlStr += XMLUtils.getEndNode("LocationDetail");

				}
				xmlStr += XMLUtils.getEndNode("LocationList");
			} else {
				throw new Exception("No location found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}
	//Created by Vicky Used for Sales Check PDA
	public String getbatchDetailsPDACheck(String Plant,String dono,String Item,String aUser,String batch,String uom) throws Exception {
		String xmlStr = "";
		ArrayList al = null;
                
         
		try {
			_InvMstDAO.setmLogger(mLogger);
			al = new InvMstDAO().getDetailsForBatchPDACheck(Plant,dono,Item, batch,uom);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("batch", (String) map
							.get("batch"));
					xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("pickqty")));
					xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("ordqty")));
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
			MLogger.log(0, "Value of xml : " + xmlStr);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
	//Created by Vicky Used for Sales Check PDA Batch Validation
		public String getbatchValidatePDACheck(String Plant,String dono,String Item,String aUser,String batch,String uom) throws Exception {
			String xmlStr = "";
			ArrayList al = null;
	                
	         
			try {
				_InvMstDAO.setmLogger(mLogger);
				al = new InvMstDAO().getValidateForBatchPDASalesCheck(Plant,dono,Item, batch,uom);
				if (al.size() > 0) {
					xmlStr += XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("itemDetails total='"
							+ String.valueOf(al.size()) + "'");
					for (int i = 0; i < al.size(); i++) {
						Map map = (Map) al.get(i);
						xmlStr += XMLUtils.getStartNode("record");
						xmlStr += XMLUtils.getXMLNode("batch", (String) map
								.get("batch"));
						xmlStr += XMLUtils.getXMLNode("qty", StrUtils.formatNum((String) map.get("pickqty")));
						xmlStr += XMLUtils.getXMLNode("pcsqty", StrUtils.formatNum((String) map.get("ordqty")));
						
						xmlStr += XMLUtils.getEndNode("record");
					}
					xmlStr += XMLUtils.getEndNode("itemDetails");
				}
				MLogger.log(0, "Value of xml : " + xmlStr);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return xmlStr;
		}

	
}
    
    
