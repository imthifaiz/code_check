package com.track.db.util;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.CustomerReturnDAO;
import com.track.dao.DNPLDetDAO;
import com.track.dao.DNPLHdrDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POBeanDAO;
import com.track.dao.PackingDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ShipHisDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.tran.WmsDNPL;
import com.track.tran.WmsIssueMaterial;
import com.track.tran.WmsIssueWOInvcheck;
import com.track.tran.WmsIssuingByPDA;
//Added By Bruhan,June 26 2014, To process mobile sales order
import com.track.tran.WmsMobileSales;
import com.track.tran.WmsOBISSUEReversal;
import com.track.tran.WmsPickIssue;
import com.track.tran.WmsPicking;
import com.track.tran.WmsPickingByPDA;
import com.track.tran.WmsPickingRandom;
import com.track.tran.WmsPickingWOInvcheck;
import com.track.tran.WmsSignatureCapture;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

import net.sf.json.JSONObject;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DOUtil {

	DoHdrDAO _DoHdrDAO = null;
	DNPLHdrDAO _DNPLHdrDAO = null;
	DoDetDAO _DoDetDAO = null;
	DNPLDetDAO _DNPLDetDAO = null;
	DoTransferHdrDAO _DoTranHdrDAO = null;
	DoTransferDetDAO _DoTranDetDAO = null;
	CustomerBeanDAO customerBeanDAO = null;
	StrUtils strUtils = new StrUtils();
	private boolean printLog = MLoggerConstant.TOUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private POBeanDAO poBeanDAO = new POBeanDAO();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public DOUtil() {
		_DoDetDAO = new DoDetDAO();
		_DoHdrDAO = new DoHdrDAO();
		_DNPLHdrDAO = new DNPLHdrDAO();
		_DNPLDetDAO = new DNPLDetDAO();
		customerBeanDAO =new CustomerBeanDAO(); 
		_DoDetDAO.setmLogger(mLogger);
		_DoHdrDAO.setmLogger(mLogger);
		_DoTranHdrDAO = new DoTransferHdrDAO();
		_DoTranDetDAO = new DoTransferDetDAO();
		_DoTranHdrDAO.setmLogger(mLogger);
		_DoTranDetDAO.setmLogger(mLogger);
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

	/////////// PDA Methods ///////////////
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
			// System.out.println(xmlStr);
		} catch (Exception e) {

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

//		String sItem = "";
//		String sPONO = "";
//		String sVendNo = "";
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

		}
		return openItem;
	}

	public boolean saveDoHdrDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_DoHdrDAO.setmLogger(mLogger);
			flag = _DoHdrDAO.insertDoHdr(ht);
		} catch (Exception e) {
			MLogger.log(-1, "saveDoHdrDetails() ::  Exception ############### "
					+ e.getMessage());
			throw e;
		}
		return flag;
	}

	public ArrayList getDoDetDetails(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectDoDet(query, ht);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}

	public ArrayList getDoDetDetails(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectDoDet(query, ht, extCond);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}

	public ArrayList getOutGoingDoHdrDetails(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.selectOutGoingDoHdr(query, ht);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}

	
	public ArrayList getdnplDetails(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectdnplDoDet(query, ht);
		} catch (Exception e) {

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

			throw e;
		}
		return al;
	}

	public ArrayList getCustomerReturnOrderNo(String query, Hashtable ht,
			String extCond) throws Exception {
		ArrayList al = new ArrayList();
		ShipHisDAO _ShipHisDAO = new ShipHisDAO();
		try {
			_ShipHisDAO.setmLogger(mLogger);

			al = _ShipHisDAO.selectCustomerReterunOrderNo(query, ht, extCond);

		} catch (Exception e) {

			throw e;
		}
		return al;
	}

	public ArrayList getDoReverseHdrDetails(String query, Hashtable ht,
			String extCond) throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.selectDoHdr(query, ht, extCond);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}

	public String getCustCode(String aPlant, String aPONO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aPONO);

		String query = " ISNULL( " + "custCode" + ",'') as " + "custCode" + " ";
		_DoHdrDAO.setmLogger(mLogger);
		Map m = _DoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custCode");

		return custCode;
	}

	public String getJobNum(String aPlant, String aPONO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aPONO);

		String query = " ISNULL( " + "jobNum" + ",'') as " + "jobNum" + " ";
		_DoHdrDAO.setmLogger(mLogger);
		Map m = _DoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("jobNum");
		return custCode;
	}
	//Start code by Bruhan for getting Orderdate in report instead currentdate on 25/March/2013
	public String getOrderDate(String aPlant, String aPONO) throws Exception {
		String OrderDate = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aPONO);

		String query = " ISNULL( " + "collectionDate +' '+LEFT(CollectionTime, 2)+':'+RIGHT(CollectionTime, 2)" + ",'') as " + "date" + " ";
		_DoHdrDAO.setmLogger(mLogger);
		Map m = _DoHdrDAO.selectRow(query, ht);

		OrderDate = (String) m.get("date");
		return OrderDate;
	}
	
	//End code by Bruhan for getting Orderdate in report instead currentdate on 25/March/2013
	
	public String getDohdrcol(Hashtable ht,String col) throws Exception {
		String custCode = "";

//		Hashtable ht = new Hashtable();
//		ht.put("PLANT", aPlant);
//		ht.put("DONO", aPONO);

		String query = " ISNULL( " + col+ ",'') as " + col + " ";
		_DoHdrDAO.setmLogger(mLogger);
		Map m = _DoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get(col);
		return custCode;
	}
	
	public String getDodetcol(Hashtable ht,String col) throws Exception {
		String custCode = "";

//		Hashtable ht = new Hashtable();
//		ht.put("PLANT", aPlant);
//		ht.put("DONO", aPONO);

		String query = " ISNULL( " + col+ ",'') as " + col + " ";
		_DoDetDAO.setmLogger(mLogger);
		Map m = _DoDetDAO.selectRow(query, ht);

		custCode = (String) m.get(col);
		return custCode;
	}

	
	public boolean saveDoDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_DoHdrDAO.setmLogger(mLogger);
			flag = _DoDetDAO.insertDoDet(ht);
		} catch (Exception e) {

		//	throw e;
		}
		return flag;
	}
   /***Created by Bruhan to add price editable on edit outbound order on 26/02/2013*****
     **********************  Modification History  ******************************
     *** Bruhan,Oct 27 2014,Description: To check update quantity less than picking quantity,add condition for update dodetail
     ***** Bruhan,March 12 2015,Description: To remove number format in qtyOr,qtyPick,qtyIs
	*/ 
   public boolean updateDoDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;
		boolean updateHdrFlag=false;
		boolean isExitFlag=false;
	    ShipHisDAO shipHisDAO = new ShipHisDAO();
		String price=ht.get("UNITPRICE").toString();
		String qty =ht.get("ORDQTY").toString();
		String PRODUCTDELIVERYDATE =ht.get("PRODUCTDELIVERYDATE").toString();
		//String PrdRemarks =ht.get(IDBConstants.DODET_COMMENT1).toString(); 
		Hashtable htUpdatedodet = new Hashtable();
		htUpdatedodet.clear();
		htUpdatedodet.put(IDBConstants.PLANT, ht.get("PLANT"));
		htUpdatedodet.put(IDBConstants.DODET_DONUM, ht.get("DONO"));
		htUpdatedodet.put("DOLNNO", ht.get("DOLNNO"));
		htUpdatedodet.put(IDBConstants.ITEM, ht.get("ITEM"));
		
		try {
	    		//check update quantity less than picking quantity
			    String q = "";
			    String qtyOr="",qtyPick="",qtyIs="",pickstatus="";//,lnstat="";
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", ht.get("PLANT"));
				htCond.put("dono",  ht.get("DONO"));
				htCond.put("dolnno", ht.get("DOLNNO"));
				htCond.put("item", ht.get("ITEM"));
				q = " isnull(qtyor,0) qtyor,isnull(qtyPick,0) as qtyPick,isnull(qtyIs,0) as qtyIs,isnull(pickstatus,'') pickstatus,isnull(lnstat,'') lnstat ";
				_DoHdrDAO.setmLogger(mLogger);
				_DoDetDAO.setmLogger(mLogger);
				ArrayList al = _DoDetDAO.selectDoDet(q, htCond,	" plant <> ''");
				for (int i = 0; i < al.size(); i++) {
					Map m = (Map) al.get(i);
					/*qtyOr = StrUtils.formatNum((String) m.get("qtyor"));
					qtyPick = StrUtils.formatNum((String) m.get("qtyPick"));
					qtyIs = StrUtils.formatNum((String) m.get("qtyIs"));*/
					qtyOr = (String) m.get("qtyor");
					qtyPick =(String) m.get("qtyPick");
					qtyIs =(String) m.get("qtyIs");
					pickstatus =(String) m.get("pickstatus");
//					lnstat = (String) m.get("lnstat");
				}
				 
				if(Double.parseDouble(qty) < Double.parseDouble(qtyPick))
				{
					throw new Exception(" Order Qty entered should not be less than already Picked Qty:"+qtyPick);
				}
				if(Double.parseDouble(qtyIs) < Double.parseDouble(qtyPick))
				{
					throw new Exception(" Issue the Picked Qty before mofiying the Order Qty ");
				}
			String query="";
		    DoDetDAO _DoDetDAO = new DoDetDAO();
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			String updateDoHdr = "";
			Hashtable htConditoHdr = new Hashtable();
			htConditoHdr.put("PLANT", ht.get("PLANT"));
			htConditoHdr.put("dono", ht.get("DONO"));
		    //Update dodet and check dohdr condition
		    if(pickstatus.equals("C") && Double.parseDouble(qtyOr)!=Double.parseDouble(qty))
		    {
		    	//query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='O',LNSTAT='O'";
		    	query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",PICKSTATUS='O',LNSTAT='O'";
		    	_DoDetDAO.setmLogger(mLogger);
				flag = _DoDetDAO.update(query,htUpdatedodet,"");
		    	isExitFlag =_DoHdrDAO.isExisit(htConditoHdr," isnull(STATUS,'') in ('C') and isnull(PICKSTAUS,'') in ('C')");
		    	if (isExitFlag && Double.parseDouble(qtyOr)!=Double.parseDouble(qty)){
					updateDoHdr = "set  status='O',PickStaus='O' ";
					updateHdrFlag=true;
				}
		    	
		    	isExitFlag =_DoHdrDAO.isExisit(htConditoHdr," isnull(STATUS,'') in ('O') and isnull(PICKSTAUS,'') in ('C')");
		    	if (isExitFlag && Double.parseDouble(qtyOr)!=Double.parseDouble(qty)){
					updateDoHdr = "set  status='O',PickStaus='O' ";
					updateHdrFlag=true;
				}
		    }
		    else if(pickstatus.equals("O") && Double.parseDouble(qty)==Double.parseDouble(qtyPick))
		    {
		    		//query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='C',LNSTAT='C'";
		    	query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",PICKSTATUS='C',LNSTAT='C', PRODUCTDELIVERYDATE='"+ PRODUCTDELIVERYDATE +"'";
		    	_DoDetDAO.setmLogger(mLogger);
				flag = _DoDetDAO.update(query,htUpdatedodet,"");
		    	isExitFlag = _DoDetDAO.isExisit(htConditoHdr,	"pickStatus in ('O','N')");
		    	if (isExitFlag)
		    	{
		    		updateHdrFlag=false;
		    	}
				else
				{
					updateDoHdr = "set  Status='C',PickStaus='C' ";
					updateHdrFlag=true;
		    	}
		    }
		    else
		    {
		    	//query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",COMMENT1='" +PrdRemarks+"'";
		    	query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ", PRODUCTDELIVERYDATE='"+ PRODUCTDELIVERYDATE +"'";
		    	_DoDetDAO.setmLogger(mLogger);
				flag = _DoDetDAO.update(query,htUpdatedodet,"");
		    	updateHdrFlag=false;
		    }
			
		    //update DOHDR
			 if(flag){
				if(updateHdrFlag==true){
				 flag = _DoHdrDAO.update(updateDoHdr, htConditoHdr, "");
				}
			    		flag = _DoTranDetDAO.update(query,htUpdatedodet,"");
                        htUpdatedodet.clear();
                        htUpdatedodet.put(IDBConstants.PLANT, ht.get("PLANT"));
                        htUpdatedodet.put(IDBConstants.DODET_DONUM, ht.get("DONO"));
                        htUpdatedodet.put("DOLNO", ht.get("DOLNNO"));
                        htUpdatedodet.put(IDBConstants.ITEM, ht.get("ITEM"));
                        boolean isExists = shipHisDAO.isExisit(htUpdatedodet);
                        if(isExists){
					        query = "set UNITPRICE=" + price+ ",ORDQTY=" + qty;
					        flag = shipHisDAO.updateShipHis(query,htUpdatedodet,"");
			             }
			}
                    
		} catch (Exception e) {

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

			throw e;

		}
		return maxDo;
	}

	public String load_all_open_do_details_xml(String plant) throws Exception {
		String xmlStr = "", dono = "", custName = "", jobNum = "";
		ArrayList alDoHdr = null;
		String queryDoHdr = "";
		Hashtable htDoHdr = null;
		htDoHdr = new Hashtable();
		queryDoHdr = " dono,custName,jobNum";
		htDoHdr.put("plant", plant);
		try {
			alDoHdr = getDoHdrDetails(queryDoHdr, htDoHdr, " and STATUS <> 'C'");
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Dos total ='"
					+ String.valueOf(alDoHdr.size()) + "'");

			if (alDoHdr.size() > 0) {
				for (int i = 0; i < alDoHdr.size(); i++) {

					Map map1 = (Map) alDoHdr.get(i);
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
				throw new Exception("No open Outbound order found for picking");
			}
		} catch (Exception e) {

			throw e;

		}
		return xmlStr;
	}

	public String load_all_open_complete_do_for_print_details_xml(String plant)
			throws Exception {
		String xmlStr = "", dono = "", custName = "", jobNum = "";
		ArrayList alDoHdr = null;
		String queryDoHdr = "";
		Hashtable htDoHdr = null;
		htDoHdr = new Hashtable();
		queryDoHdr = " dono,custName,jobNum";
		htDoHdr.put("PLANT", plant);
		try {
			alDoHdr = getDoHdrDetails(queryDoHdr, htDoHdr, " and STATUS <> 'N'");
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Dos total ='"
					+ String.valueOf(alDoHdr.size()) + "'");

			if (alDoHdr.size() > 0) {
				for (int i = 0; i < alDoHdr.size(); i++) {

					Map map1 = (Map) alDoHdr.get(i);
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
				throw new Exception("No open Outbound order found for picking");
			}
		} catch (Exception e) {

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

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("DoDet total ='"
					+ String.valueOf(alDoDet.size()) + "'");

			if (alDoDet.size() > 0) {
				for (int i = 0; i < alDoDet.size(); i++) {

					Map map1 = (Map) alDoDet.get(i);
					dono = (String) map1.get("dono");
					dolno = (String) map1.get("dolnno");
					item = (String) map1.get("item");
					// String extraCon = " LOC = 'SHIPPINGAREA' AND QTY >0 ";
					String extraCon = " LOC LIKE 'SHIPPINGAREA%' AND QTY >0 ";
					InvMstDAO inM = new InvMstDAO();
					inM.setmLogger(mLogger);
					String loc = inM.getItemLoc(aPlant, item, extraCon);
					itemDesc = new ItemMstDAO().getItemDesc(aPlant, item);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("doNum", dono);
					xmlStr += XMLUtils.getXMLNode("doLnNo", dolno);
					xmlStr += XMLUtils.getXMLNode("item", item);
					xmlStr += XMLUtils.getXMLNode("itemDesc", itemDesc);
					xmlStr += XMLUtils.getXMLNode("loc", loc);
					xmlStr += XMLUtils.getEndNode("record");

				}

				xmlStr += XMLUtils.getEndNode("DoDet");

			} else {
				throw new Exception("No item found for order " + aDoNum);
			}
		} catch (Exception e) {
			throw e;

		}
		return xmlStr;
	}

	public boolean updateDoHdr(Hashtable ht) throws Exception {
		boolean flag = false;

		Hashtable htCond = new Hashtable();
		htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put(IDBConstants.DOHDR_DONUM, (String) ht
				.get(IDBConstants.DOHDR_DONUM));
		try {

			StringBuffer updateQyery = new StringBuffer("set ");

			updateQyery.append(IDBConstants.DOHDR_JOB_NUM + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_JOB_NUM) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_CONTACT_NUM + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_CONTACT_NUM) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_COL_DATE + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_COL_DATE) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_REMARK1 + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_REMARK1) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_REMARK2 + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_REMARK2) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_REMARK3 + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_REMARK3) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_CUST_NAME + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_CUST_NAME) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_CUST_CODE + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_CUST_CODE) + "'");
			updateQyery
					.append(","
							+ IDBConstants.DOHDR_PERSON_INCHARGE
							+ " = '"
							+ (String) ht
									.get(IDBConstants.DOHDR_PERSON_INCHARGE)
							+ "'");
			updateQyery.append("," + IDBConstants.DOHDR_ADDRESS + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_ADDRESS) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_ADDRESS2 + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_ADDRESS2) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_ADDRESS3 + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_ADDRESS3) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_COL_TIME + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_COL_TIME) + "'");
			updateQyery.append("," + IDBConstants.ORDERTYPE + " = '"
					+ (String) ht.get(IDBConstants.ORDERTYPE) + "'");
			updateQyery.append("," + IDBConstants.CURRENCYID + " = '"
					+ (String) ht.get(IDBConstants.CURRENCYID) + "'");
			updateQyery.append("," + IDBConstants.DELIVERYDATE + " = '"
					+ (String) ht.get(IDBConstants.DELIVERYDATE) + "'");
			updateQyery.append("," + IDBConstants.TIMESLOTS + " = '"
					+ (String) ht.get(IDBConstants.TIMESLOTS) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_GST + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_GST) + "'");
			updateQyery.append("," + IDBConstants.DOHDR_EMPNO + " = '"
					+ (String) ht.get(IDBConstants.DOHDR_EMPNO) + "'");                            
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
			updateQyery.append("," + IDBConstants.PAYMENTTYPE + " = '"
					+ (String) ht.get(IDBConstants.PAYMENTTYPE) + "'");
			updateQyery.append("," + IDBConstants.POHDR_DELIVERYDATEFORMAT + " = '"
					+ (String) ht.get(IDBConstants.POHDR_DELIVERYDATEFORMAT) + "'");
			updateQyery.append("," + IDBConstants.TAXTREATMENT + " = '"
					+ (String) ht.get(IDBConstants.TAXTREATMENT) + "'");
			updateQyery.append("," + IDBConstants.SALES_LOCATION + " = '"
					+ (String) ht.get(IDBConstants.SALES_LOCATION) + "'");

			_DoHdrDAO.setmLogger(mLogger);
			flag = _DoHdrDAO.update(updateQyery.toString(), htCond,"");//" AND STATUS ='N'" commented to allow the header details to be updated
			
			if(flag){
			 updateQyery = new StringBuffer("set ");
			 updateQyery.append(" CNAME = '"+ (String) ht.get(IDBConstants.DOHDR_CUST_NAME) + "'");
			 boolean isExistsRecords = new ShipHisDAO().isExisit(htCond, "");
			 if(isExistsRecords){
			 flag = new ShipHisDAO().update(updateQyery.toString(), htCond,"");
			 }
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Processed Outbound Order Cannot be modified");
		}
		return flag;
	}

	/*************Created by Bruhan for wip reporting on 05/may/2014************************************************************
	 *******************Modification History  **************************************************************************************
     *** Bruhan,Oct 30 2014,Description: To include QTYPICK in link
	*/ 
	public String listDODET(String plant, String aDONO, String rflag)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
//		String where = "";
		String desc="";
//		String uom="",;
		String nonstkflag="";
		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("dono", aDONO);
			//start code by Bruhan to add price editable on edit outbound order on 26/02/2013
			q = "dono,dolnno,pickstatus as lnstat,item,ISNULL(PRODUCTDELIVERYDATE,'') PRODUCTDELIVERYDATE,isnull(itemdesc,'') itemdesc,isnull(unitmo,'') uom,isnull(unitprice,0)*isnull(currencyuseqt,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,isnull(pickstatus,'') pickstatus,isnull(comment1,'') as prdRemarks,isnull(prodgst,0) prodgst,CAST(isnull(unitprice,0)*isnull(currencyuseqt,0) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as Convcost ";
			//End code by Bruhan to add price editable on edit outbound order on 26/02/2013
			_DoHdrDAO.setmLogger(mLogger);
			_DoDetDAO.setmLogger(mLogger);
			ArrayList al = _DoDetDAO.selectDoDet(q, htCond,
					" plant <> '' order by dolnno");
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

			if (al.size() > 0) {
				ItemMstDAO _ItemMstDAO = new ItemMstDAO();
				_ItemMstDAO.setmLogger(mLogger);
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				ItemUtil _itemutil = new ItemUtil();
				_itemutil.setmLogger(mLogger);
				for (int i = 0; i < al.size(); i++) {

					Map m = (Map) al.get(i);
					String dono = (String) m.get("dono");
					String dolnno = (String) m.get("dolnno");
//					String ordertype = _DoHdrDAO.getOrderType(plant, dono);
					String item = (String) m.get("item");
					//start code by Bruhan to add price editable on edit outbound order on 26/02/2013
					String itemdesc = (String) m.get("itemdesc");
				    String prdRemarks = (String) m.get("prdRemarks");
					String itemuom = (String) m.get("uom");
					String PRODUCTDELIVERYDATE = (String) m.get("PRODUCTDELIVERYDATE");
					//End code by Bruhan to add price editable on edit outbound order on 26/02/2013
					
					
					/*String ConvertedUnitPrice = (String) m.get("unitprice");
					String price = StrUtils.currencyWtoutSymbol(ConvertedUnitPrice);*/
					
					String price = (String) m.get("unitprice");
					String qtyor =(String) m.get("qtyor");
					String qtyis =(String) m.get("qtyis");
					String qtyPick = (String) m.get("qtyPick");
					String convcost =	(String) m.get("Convcost");
					//float priceValue="".equals(price) ? 0.0f :  Float.parseFloat(price);
					float qtyOrVal="".equals(qtyor) ? 0.0f :  Float.parseFloat(qtyor);
					float qtyIssueVal="".equals(qtyis) ? 0.0f :  Float.parseFloat(qtyis);
					float qtyPickVal="".equals(qtyPick) ? 0.0f :  Float.parseFloat(qtyPick);
					/*convcost=convcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");*/
					double convcostVal ="".equals(convcost) ? 0.0d :  Double.parseDouble(convcost);
					convcost = StrUtils.addZeroes(convcostVal, numberOfDecimal);
					/*price=String.valueOf(priceValue);
					if(priceValue==0f){
						price="0.00000";
					}else{
						price=price.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}*/if(qtyOrVal==0f){
						qtyor="0.000";
					}else{
						qtyor=qtyor.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}if(qtyIssueVal==0f){
						qtyis="0.000";
					}else{
						qtyis=qtyis.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}if(qtyPickVal==0f){
						qtyPick="0.000";
					}else{
						qtyPick=qtyPick.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					
					String ListPrice = new DOUtil().getConvertedUnitCostForProduct(plant,dono,item);   
					String listprice = StrUtils.currencyWtoutSymbol(ListPrice);
					//String price = StrUtils.currencyWtoutSymbol((String) m.get("unitprice"));
 	                 String OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,dono,item,"OUTBOUND");     
	                 if (OBDiscount.equalsIgnoreCase("null")	|| OBDiscount == null || OBDiscount == ""){
							OBDiscount="0.00";
					 }
	                 String discounttype="";
	                 int plusIndex = OBDiscount.indexOf("%");
	                 if (plusIndex != -1) {
	                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	                	 	discounttype = "BYPERCENTAGE";
	                     }
	                 else
	                 {
	                	 discounttype="BYPRICE";
	                 }
	                 
					
			    	List itemList	=_itemutil.qryItemMst(item,plant," ");
					 for(int j =0; j<itemList.size(); j++) {
						 Vector vecItem   = (Vector)itemList.get(j);
							desc     = (String)vecItem.get(1);					
//							uom=(String)vecItem.get(3);
							nonstkflag = (String)vecItem.get(16);
						}
					//to get stockonhand and outgoing quantity
			    	 String minstkqty="",maxstkqty="",stockonhand="",outgoingqty="";
			    	 ItemMstUtil itemutil= new ItemMstUtil();
	                    ArrayList listQry = itemutil.getDOItemList(plant,item," and  isnull(itemmst.userfld1,'N')='N' ");
	                    Map mQty=(Map)listQry.get(0);
	                    if(mQty.size()>0){
		                    stockonhand=StrUtils.fString((String) mQty.get("STOCKONHAND"));
		                    outgoingqty=StrUtils.fString((String) mQty.get("OUTGOINGQTY"));
		                    minstkqty=StrUtils.fString((String) mQty.get("STKQTY"));
		                    maxstkqty=StrUtils.fString((String) mQty.get("MAXSTKQTY"));
	                    }
                    //to get stockonhand and outgoing quantity end
				 
				 
					String pickstat = (String) m.get("pickstatus");
					String lnstat = (String) m.get("lnstat");
					String prodgst=(String) m.get("prodgst");
					//start code by Bruhan to add price editable on edit outbound order on 26/02/2013
					link = "<a href=\"" + "modiSODET.jsp?DONO=" + dono +"&PICKSTATUS=" + pickstat
							+ "&DOLNNO=" + dolnno + "&ITEM=" + item + "&DESC=" + StrUtils.replaceCharacters2Send(itemdesc) +"&QTY=" + qtyor+"&QTYPICK=" + qtyPick  
								+"&PRICE=" + new java.math.BigDecimal(convcost).toPlainString() + "&UOM=" + itemuom + "&RFLAG="+ rflag + "&NONSTOCKFLG="+ nonstkflag+ "&PRDREMARKS="+ prdRemarks + "&MINSTKQTY=" + minstkqty +"&MAXSTKQTY=" + maxstkqty +"&PRODUCTDELIVERYDATE=" + PRODUCTDELIVERYDATE  
                                +"&STOCKONHAND=" + stockonhand +"&OUTGOINGQTY=" + outgoingqty +"&LISTPRICE=" + listprice +"&CUSDIS=" + OBDiscount +"&PRODGST=" + prodgst +"&DISTYPE=" + discounttype +"&COSTRD=" + new java.math.BigDecimal(price).toPlainString() +"\">";
					//End code by Bruhan to add price editable on edit outbound order on 26/02/2013
					//if (pickstat.equals("N")) {
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"10%\">"
								+ dolnno
								+ "</td>"
								/*+ "<td width=\"11%\" align = center>"
								+ sqlBean.formatHTML(ordertype)
								+ "</td>"*/
								+ "<td width=\"11%\" align = left>"
								+ link
								+ "<FONT COLOR=\"blue\" align = center >"
								+ sqlBean.formatHTML(item) + "</a></font>"
								+ "</td>"
								+ "<td width=\"18%\" align = left>"
								+ sqlBean.formatHTML(desc)
								+ "</td>"
								+ "<td width=\"11%\" align = center>"
								+ sqlBean.formatHTML(new java.math.BigDecimal(convcost).toPlainString())
								+ "</td>"
								+ "<td width=\"11%\" align = center>"
								+ sqlBean.formatHTML(qtyor)
								+ "</td>"
								+ "<td width=\"9%\" align = center >"
								+ sqlBean.formatHTML(qtyPick)
								+ "</td>"
								+ "<td width=\"9%\" align = center >"
								+ sqlBean.formatHTML(qtyis)
								+ "</td>"
								+ "<td width=\"5%\" align = left>"
								+ itemuom
								+ "<td width=\"5%\" align = center>"
							+ lnstat + "</td>" + "</tr>";
				

				}
			} else {
				result += "<tr valign=\"middle\"><td colspan=\"9\" align=\"center\">"
						+ " Please add in Products " + "</td></tr>";
			}
		} catch (Exception e) {
			throw e;
		}

		return result;
	}
        
    public String listDODETForMobileEnquiry(String plant, String aDONO, String rflag)
                    throws Exception {
            String q = "";
            String link = "";
            String result = "";
//            String where = "";
            String desc="";
            String uom="",nonstkflag="";
            try {
                    Hashtable htCond = new Hashtable();
                    htCond.put("PLANT", plant);
                    htCond.put("dono", aDONO);
                    q = "dono,dolnno,pickstatus as lnstat,item,isnull(unitprice,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,isnull(pickstatus,'') pickstatus ";
                    _DoHdrDAO.setmLogger(mLogger);
                    _DoDetDAO.setmLogger(mLogger);
                    ArrayList al = _DoDetDAO.selectDoDet(q, htCond,
                                    " plant <> '' order by dolnno");

                    if (al.size() > 0) {
                            ItemMstDAO _ItemMstDAO = new ItemMstDAO();
                            _ItemMstDAO.setmLogger(mLogger);
                            InvMstDAO _InvMstDAO = new InvMstDAO();
                            _InvMstDAO.setmLogger(mLogger);
                            ItemUtil _itemutil = new ItemUtil();
                            _itemutil.setmLogger(mLogger);
                            for (int i = 0; i < al.size(); i++) {

                                    Map m = (Map) al.get(i);
                                    String dono = (String) m.get("dono");
                                    String dolnno = (String) m.get("dolnno");
                                    String ordertype = _DoHdrDAO.getOrderType(plant, dono);
                                    String item = (String) m.get("item");
//                                    String price = StrUtils.currencyWtoutSymbol((String) m.get("unitprice"));
//                                    String qtyor = StrUtils.formatNum((String) m.get("qtyor"));
//                                    String qtyPick = StrUtils.formatNum((String) m.get("qtyPick"));
//                                    String qtyis = StrUtils.formatNum((String) m.get("qtyis"));
                            List itemList   =_itemutil.qryItemMst(item,plant," ");
                             for(int j =0; j<itemList.size(); j++) {
                                     Vector vecItem   = (Vector)itemList.get(j);
                                            desc     = (String)vecItem.get(1);                                      
                                            uom=(String)vecItem.get(3);
                                            nonstkflag = (String)vecItem.get(16);
                                    }
                                    
                                    String pickstat = (String) m.get("pickstatus");
                                    String lnstat = (String) m.get("lnstat");
                                    link = "<a href=\"" + "modiSODET.jsp?DONO=" + dono
                                                    + "&DOLNNO=" + dolnno + "&ITEM=" + item + "&RFLAG="
                                                    + rflag + "\">";
                                    if (pickstat.equals("N")) {
                                            result += "<tr valign=\"middle\">"
                                                            + "<td align=\"center\" width=\"10%\">"
                                                            + dolnno
                                                            + "</td>"
                                                            + "<td width=\"11%\" align = center>"
                                                            + sqlBean.formatHTML(ordertype)
                                                            + "</td>"
                                                            + "<td width=\"11%\" align = center>"
                                                            + link
                                                            + "<FONT COLOR=\"blue\" align = center >"
                                                            + sqlBean.formatHTML(item)
                                                            + "</a></font>"
                                                            + "</td>"
                                                            + "<td width=\"18%\" align = center>"
                                                            + sqlBean.formatHTML(desc)
                                                            + "</td>"
                                                            + "<td width=\"5%\" align = center>"
                                                            + uom
                                                            + "</td>"
                                                            + "<td width=\"5%\" align = center>"
                                                            + lnstat + "</td>" + "</tr>";
                                    } else {
                                            result += "<tr valign=\"middle\">"
                                                            + "<td align=\"center\" width=\"10%\">";
                                                            if(nonstkflag.equalsIgnoreCase("Y"))
                                                                    result += link;
                                            result +=       dolnno + "</td>"
                                                            + "<td width=\"11%\" align = center>"
                                                            + sqlBean.formatHTML(ordertype)
                                                            + "</td>"
                                                            + "<td width=\"11%\" align = center>"
                                                            +"<FONT COLOR=\"blue\" align = center>"
                                                            + sqlBean.formatHTML(item)
                                                            + "</a></font>"
                                                            + "</td>"
                                                            + "<td width=\"18%\" align = center>"
                                                            + sqlBean.formatHTML(desc)
                                                            + "</td>"
                                                            + "<td width=\"5%\" align = center>"
                                                            + uom
                                                            + "</td>"
                                                            + "<td width=\"5%\" align = center>"
                                                            + lnstat + "</td>" + "</tr>";

                                    }

                            }
                    } else {
                            result += "<tr valign=\"middle\"><td colspan=\"9\" align=\"center\">"
                                            + " Please add in Products " + "</td></tr>";
                    }
            } catch (Exception e) {
                    throw e;
            }

            return result;
    }

	public String listDODETToPrint(String plant, String aDONO, String rflag)
			throws Exception {
		String q = "";
//		String link = "";
		String result = "";
		try {
			ItemMstDAO _ItemMstDAO = new ItemMstDAO();
			_ItemMstDAO.setmLogger(mLogger);
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("dono", aDONO);
//			String ordertype = _DoHdrDAO.getOrderType(plant, aDONO);
			q = "dono,dolnno,lnstat,item,itemdesc,isnull(unitprice,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick ";
			_DoDetDAO.setmLogger(mLogger);
			ArrayList al = _DoDetDAO.selectDoDet(q, htCond,
					" plant <> '' order by dolnno");

			if (al.size() > 0) {
				for (int i = 0; i < al.size(); i++) {

					Map m = (Map) al.get(i);
//					String dono = (String) m.get("dono");
					String dolnno = (String) m.get("dolnno");
					String item = (String) m.get("item");
					String qtyor = StrUtils.formatNum((String) m.get("qtyor"));
					String desc = _ItemMstDAO.getItemDesc(plant, item);
					String unit = (String) m.get("unitprice");
					String lnstat = (String) m.get("lnstat");
					String qtyPick = StrUtils.formatNum((String) m.get("qtyPick"));
					String qtyis = StrUtils.formatNum((String) m.get("qtyis"));
//					link = "<a href=\"" + "modiSODET.jsp?DONO=" + dono
//							+ "&DOLNNO=" + dolnno + "&ITEM=" + item + "&RFLAG="
//							+ rflag + "\">";
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"10%\">"
							+ dolnno
							+ "</td>"
							/*+ "<td width=\"11%\" align = center>"
							+ sqlBean.formatHTML(ordertype)
							+ "</td>"*/
							+ "<td width=\"11%\" align = center>"
							+ "<FONT COLOR=\"blue\" align = center >"
							+ sqlBean.formatHTML(item) + "</a></font>"
							+ "</td>"
							+ "<td width=\"18%\" align = center>"
							+ sqlBean.formatHTML(desc)
							+ "</td>"
							+ "<td width=\"11%\" align = center>"
							+ sqlBean.formatHTML(unit)
							+ "</td>"
							+ "<td width=\"11%\" align = center>"
							+ sqlBean.formatHTML(qtyor)
							+ "</td>"
							+ "<td width=\"11%\" align = center >"
							+ sqlBean.formatHTML(qtyPick)
							+ "</td>"
							+ "<td width=\"11%\" align = center >"
							+ sqlBean.formatHTML(qtyis)
							+ "</td>"
							+ "<td width=\"5%\" align = center>"
							+ lnstat
							+ "</td>" + "</tr>";

				}
			} else {
				result += "<tr valign=\"middle\"><td colspan=\"9\" align=\"center\">"
						+ " Please add in Products " + "</td></tr>";
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	
	public ArrayList listOutGoingPickingDODET(String plant, String aDONO)
			throws Exception {
		String q = "";
    		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("dono", aDONO);
			q = "dono,dolnno,pickstatus as lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY";
			_DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectDoDet(q, htCond,
					" plant <> '' and pickstatus <>'C' order by dolnno");

			
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
        
    public ArrayList listDODETDetails(String plant,String aDONO)
                    throws Exception {
            String q = "";
//            String link = "";
//            String result = "";
//            String where = "";
            ArrayList al = null;

            try {
                    Hashtable htCond = new Hashtable();
                    htCond.put("PLANT", plant);
                    htCond.put("dono", aDONO);
                    q = "dono,dolnno, lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname";
                    _DoDetDAO.setmLogger(mLogger);
                    al = _DoDetDAO.selectDoDet(q, htCond,
                                    " plant <> '' and lnstat <>'C' order by dolnno");

                  
            } catch (Exception e) {
                    throw e;
            }
            return al;
    }
 //Start code added by Bruhan for outbound order check on 3 Apr 2013   
 public ArrayList listDODETDetailsfordocheck(String plant,String aDONO)
    throws Exception {
	 	String q = "";
//	 	String link = "";
//	 	String result = "";
//	 	String where = "";
	 	ArrayList al = null;

	 	try {
	 		Hashtable htCond = new Hashtable();
	 		htCond.put("PLANT", plant);
	 		htCond.put("dono", aDONO);
	 		q = "dono,item,itemdesc,sum(ISNULL(qtyor,0)) as qtyor,sum(ISNULL(qtyPick,0)) as qtypick,sum(ISNULL(qtyis,0)) as qtyis";
	 		_DoDetDAO.setmLogger(mLogger);
	 		al = _DoDetDAO.selectDoDet(q, htCond,
                    " plant <> '' and lnstat <>'C' group by item,dono,ItemDesc order by item,dono");

  
	 	} catch (Exception e) {
	 		throw e;
	 	}
	 	return al;
}
 
 
//End code added by Bruhan for outbound order check on 3 Apr 2013   
	public ArrayList listOutGoingReverseDODET(String plant, String aDONO)
			throws Exception {
		String q = "";
//		String link = "";
//		String result = "";
//		String where = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("a.PLANT", plant);

			htCond.put("a.dono", aDONO);
			q = "a.dono,a.dolnno,a.lnstat,a.item,isnull(max(a.qtyor),0) as qtyor,isnull(sum(a.qtyis),0) as qtyis,isnull(sum(b.Pickqty),0) as qtyPick,a.userfld3 as custname,b.loc1,b.batch,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";
			_DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO
					.selectReverseDoDet(
							q,
							htCond,
							" a.plant <> ''  and a.qtypick > 0 and b.status<>'C' and a.dolnno=b.dolno and a.dono=b.dono and a.item=b.item group  by a.dono,b.batch,a.dolnno,a.item,b.loc1,b.batch,a.userfld3,a.lnstat,UNITMO",
							plant);
//and a.item not in (Select item from [" + plant + "_" + "ITEMMST] where NONSTKFLAG='Y')
			
		} catch (Exception e) {
			throw e;
		}
		return al;
	}

	public ArrayList listOutGoingIssueDODET(String plant, String aDONO)
			throws Exception {
		String q = "";
//		String link = "";
//		String result = "";
//		String where = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("dono", aDONO);
			q = "dono,dolnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY,isnull(unitprice,0) as unitprice";
			_DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO
					.selectDoDet(
							q,
							htCond,
							" plant <> ''  and isnull(qtyis,0) <> isnull(qtyPick,0)  and lnstat <>'C' order by dolnno");

		} catch (Exception e) {
			throw e;
		}
		return al;
	}
	
	 public ArrayList dnpl(String plant, String aDONO, String invoiceNo)
		      throws Exception {
		     String q = "";
//		     String link = "";
//		     String result = "";
//		     String where = "";
		     ArrayList al = null;

		     try {
		      Hashtable htCond = new Hashtable();
		      htCond.put("A.PLANT", plant);
		      htCond.put("A.dono", aDONO);
		      htCond.put("INVOICENO", invoiceNo);
		      q = "distinct A.dono,dolnno,lnstat,a.item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,a.loc as loc,a.userfld4 as batch,a.userfld3 as custname,"
		         +" (select itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as Itemdesc,"
		         + "(select stkuom from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as uom,"
		         +"CASE when NETWEIGHT <>0 THEN NETWEIGHT ELSE (SELECT ISNULL(NETWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) END netweight,"
		          +"CASE when GROSSWEIGHT <>0 THEN GROSSWEIGHT ELSE (SELECT ISNULL(GROSSWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) END grossweight,"
		         +" (select isnull(hscode,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as hscode,"
		         +" (select isnull(coo,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as coo,"
		         + "isnull(PACKING,'')packing,isnull(DIMENSION,'') dimension,'' as PLNO,''as DNNO,'' as TOTALNETWEIGHT,'' as TOTALGROSSWEIGHT,'' as NETPACKING,'' as NETDIMENSION,'' as DNPLREMARKS";
		      _DoDetDAO.setmLogger(mLogger);
		      al = _DoDetDAO
		        .selectdnplDoDet(
		          q,
		          htCond,
		          " a.plant <> ''  and lnstat in ('O', 'C') order by dolnno");

		     } catch (Exception e) {
		      throw e;
		     }
		     return al;
		  }
	
	public ArrayList listOutGoingIssueBulkDODET(String plant, String aDONO)
			throws Exception {
		String q = "";
//		String link = "";
//		String result = "";
//		String where = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("dono", aDONO);
			q = "dono,dolnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname,"
		    +" (select itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as Itemdesc,ISNULL(( select top 1 LOC_ID from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM ),'') as location,ISNULL(UNITMO,'') as uom ";
			_DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO
					.selectDoDet(
							q,
							htCond,
							" plant <> ''  and lnstat <>'C' order by dolnno");

		} catch (Exception e) {
			throw e;
		}
		return al;
	}

	public ArrayList listShippingPrint(String plant, String aDONO, String ShipNo)
			throws Exception {
		String q = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("dono", aDONO);
			htCond.put("USERFLD1", ShipNo);
			q = "  DONO as dono, DOLNO as dolnno,ITEM as item,ITEMDESC as ItemDesc,BATCH,PICKQTY as QtyPick ";

			al = shipDao.selectShipHis(q, htCond,
					" plant <> ''  order by DOLNO");

		} catch (Exception e) {
			throw e;
		}

		return al;
	}

	public String getDoDetails_picking(String Plant) {
		String xmlStr = "";
		ArrayList al = null;

		String query = "dono,dolnno,item,isnull(qtyor,0) as qtyor,isnull(qtyPick,0) as qtyPick";
		String extCond = " pickStatus <> 'C'";
		Hashtable ht = new Hashtable();
		ht.put("plant", Plant);
		//
		try {
			_DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectDoDet(query, ht, extCond);
			MLogger.log(0, "Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("dono"));
					xmlStr += XMLUtils.getXMLNode("polnno", (String) map
							.get("dolnno"));
					xmlStr += XMLUtils.getXMLNode("qtyor", (String) map
							.get("qtyor"));
					xmlStr += XMLUtils.getXMLNode("qtyrc", (String) map
							.get("qtyPick"));
					xmlStr += XMLUtils.getXMLNode("vendor", getVendorName(
							Plant, (String) map.get("dono")));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", new ItemMstDAO()
							.getItemDesc(Plant, (String) map.get("item")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {

		}
		return xmlStr;
	}

	public boolean process_IssueMaterial(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.OBISSUE
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.DODET_DONUM) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.DODET_DOLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_Issuing(obj);

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

	public boolean process_BulkIssueMaterial(Map obj) throws Exception {
		boolean flag = false;
//		UserTransaction ut = null;
		try {
			

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.OBBULKISSUE
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.DODET_DONUM) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.DODET_DOLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.INV_BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.INV_QTY));

			flag = process_Wms_Issuing(obj);

			
		} catch (Exception e) {			
			throw e;
		}

		return flag;
	}
	
	private boolean process_Wms_Issuing(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsIssueMaterial();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}

	public String getVendorName(String aPlant, String aPONO) throws Exception {

		String custCode = "";
		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("dono", aPONO);
		String query = " ISNULL( " + "custName" + ",'') as " + "custName" + " ";
		Map m = _DoHdrDAO.selectRow(query, ht);
		custCode = (String) m.get("custName");

		return custCode;
	}

	public boolean savePackingDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {
			_DoHdrDAO.setmLogger(mLogger);
			flag = _DoHdrDAO.insertPckalloc(ht);
		} catch (Exception e) {

			throw e;
		}

		return flag;
	}

	public String getOpenOutBoundOrder(String Plant) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " a.dono,isnull(a.CustName,'') CustName,isnull(a.Remark1,'')Remark1,isnull(a.collectiondate,'')orddate,isnull(a.status,'') status ";
		String extCond = " and isnull(a.status,'') <> 'C'and ORDER_STATUS <>'Draft'";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		try {
			al = dohdrDao.selectDoHdrForOutBound(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map
							.get("dono"));
					xmlStr += XMLUtils.getXMLNode("customer", (String) StrUtils.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("orderdate", (String) map
							.get("orddate"));
					xmlStr += XMLUtils.getXMLNode("status", (String) map
							.get("status"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}
	
	

	public String getOpenOutPickBoundOrder(String Plant) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = "a.dono,isnull(a.CustName,'') CustName,isnull(a.Remark1,'')Remark1,isnull(a.collectiondate,'')orddate,isnull(a.status,'') status ";
		String extCond = " and isnull(qtyis,0) <> isnull(qtyPick,0) and isnull(a.status,'') <> 'C'";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		try {
			al = dohdrDao.selectDoHdrForSalesIssue(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map
							.get("dono"));
					xmlStr += XMLUtils.getXMLNode("customer", (String) StrUtils.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					/*xmlStr += XMLUtils.getXMLNode("item", (String) strUtils
							.replaceCharacters2SendPDA(map.get("item")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("itemdesc", (String) strUtils
							.replaceCharacters2SendPDA(map.get("itemdesc")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", (String) strUtils
							.replaceCharacters2SendPDA(map.get("qtyor")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtypick", (String) strUtils
							.replaceCharacters2SendPDA(map.get("qtypick")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyis", (String) strUtils
							.replaceCharacters2SendPDA(map.get("qtyis")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("uom", (String) strUtils
							.replaceCharacters2SendPDA(map.get("UNITMO")
									.toString()));
					*/
					xmlStr += XMLUtils.getXMLNode("orderdate", (String) map
							.get("orddate"));
					xmlStr += XMLUtils.getXMLNode("status", (String) map
							.get("status"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}
	

	public String getOutBoundOrderItemDetails(String Plant, String dono,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList al = null;
		DoDetDAO dodetDao = new DoDetDAO();
		dodetDao.setmLogger(mLogger);

		String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String extCond = " lnstat <> 'C' ";
		if (!isReceived) {
			extCond = extCond + " and PickStatus <> 'C' ";
		}

		extCond = extCond + " ORDER BY ITEM ";

		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		//
		try {
			al = dodetDao.selectDoDet(query, ht, extCond);
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
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("QTYPICK")));
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

	public String process_DoPickingForPDA(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_DoPicking(obj);
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
							+ " ,"+e.getMessage());
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

	public boolean process_DoPickingByRange(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
				ut = com.track.gates.DbBean.getUserTranaction();
				ut.begin();
				flag = process_Wms_DoPicking(obj);
				if (flag == true) {
					DbBean.CommitTran(ut);
					flag = true;
				} else {
					DbBean.RollbackTran(ut);
					flag = false;
				}
				this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.OBPICKING
					+ " :: END : Transction returned : " + flag);

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return flag;
	}

	public boolean process_DoPickingForPC(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();

			ut.begin();
			flag = process_Wms_DoPicking(obj);

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

	private boolean process_Wms_DoPicking(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsPicking();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
	

public boolean process_DoPickIssueForPC(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();

			ut.begin();
			flag = process_Wms_DoPickIssue(obj);

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

public boolean process_DoPickIssueForPC_ut(Map obj) throws Exception {
	boolean flag = false;
	try {
		flag = process_Wms_DoPickIssue(obj);

	} catch (Exception e) {
		flag = false;
		throw e;
	}

	return flag;
}
	
	private boolean process_Wms_DoPickIssue(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsPickIssue();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
	public String getOpenoutBoundOrderSupDetails(String Plant, String OrdNum) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " a.dono,a.CustName,isnull(a.Remark1,'') Remark1 ";
		String extCond = " AND a.DONO = '" + OrdNum
				+ "' and isnull(a.status, '') <> 'C'";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);

		try {
			al = dohdrDao.selectDoHdrForOutBound(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", (String) StrUtils.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}

	public String getOpenoutBoundOrderSupDetailsForPrint(String Plant,
			String OrdNum) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " a.dono,a.CustName,a.Remark1 ";
		String extCond = " AND a.DONO = '" + OrdNum + "'"; 
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);

		//
		try {
			al = dohdrDao.selectDoHdrForOutBound(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", (String) StrUtils.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}

	public String getCompanyDetailsForPrint(String Plant) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " PLANT, PLNTDESC, TELNO, USERFLD2, EMAIL, ADD1,ADD2, ADD3, ADD4, COUNTY, ZIP, ISNULL(WEBSITE, '') AS WEBSITE ";
		String extCond = ""; // AND a.DONO = '" + OrdNum
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);

		//
		try {
			al = dohdrDao.selectCompanyDetails(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("CompanyDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("PLANT", map.get("PLANT")
							.toString());
					xmlStr += XMLUtils.getXMLNode("PLNTDESC", map.get(
							"PLNTDESC").toString());
					xmlStr += XMLUtils.getXMLNode("TELNO", map.get("TELNO")
							.toString());
					xmlStr += XMLUtils.getXMLNode("USERFLD2", map.get(
							"USERFLD2").toString());
					xmlStr += XMLUtils.getXMLNode("EMAIL", map.get("EMAIL")
							.toString());
					xmlStr += XMLUtils.getXMLNode("ADD1", map.get("ADD1")
							.toString());
					xmlStr += XMLUtils.getXMLNode("ADD2", map.get("ADD2")
							.toString());
					xmlStr += XMLUtils.getXMLNode("ADD3", map.get("ADD3")
							.toString());
					xmlStr += XMLUtils.getXMLNode("ADD4", map.get("ADD4")
							.toString());
					xmlStr += XMLUtils.getXMLNode("COUNTY", map.get("COUNTY")
							.toString());
					xmlStr += XMLUtils.getXMLNode("ZIP", map.get("ZIP")
							.toString());
					xmlStr += XMLUtils.getXMLNode("WEBSITE", map.get("WEBSITE")
							.toString());
					xmlStr += XMLUtils.getEndNode("record");

				}
				xmlStr += XMLUtils.getEndNode("CompanyDetails");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}

	public String getOpenShipHisOrder(String Plant) {
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;
		String query = " DISTINCT DONO,CNAME,'' AS REMARK ";// ISNULL(REMARK,'')
		String extCond = "  isnull(status,'') <> 'C' AND isnull(status,'') <> 'N' AND DONO <>'MISC-ISSUE' ORDER BY DONO DESC";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		try {
			al = shipDao.selectShipHis(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map.get("DONO"));
					xmlStr += XMLUtils.getXMLNode("supplier", StrUtils.replaceCharacters2SendPDA((String) map.get("CNAME")));
					xmlStr += XMLUtils.getXMLNode("remarks", StrUtils.replaceCharacters2SendPDA((String) map.get("REMARK")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}

	public String getOpenDOHDROrder(String Plant) {
		DoHdrDAO dohdrdao = new DoHdrDAO();
		
		dohdrdao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;
		String query = " DISTINCT DONO,custName,'' AS REMARK ";// ISNULL(REMARK,'')
		//update on Nov-22 Removing status cond check for New order
		String extCond = " and isnull(status,'') <> 'C'  AND DONO <>'MISC-ISSUE' ORDER BY DONO DESC";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		try {
			al = dohdrdao.selectDoHdr(query, ht, extCond);
			
			
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map.get("DONO"));
					xmlStr += XMLUtils.getXMLNode("supplier", StrUtils.replaceCharacters2SendPDA((String) map.get("custName")));
					xmlStr += XMLUtils.getXMLNode("remarks", StrUtils.replaceCharacters2SendPDA((String) map.get("REMARK")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}

	
	public String getClosedShipHisOrderForPrint(String Plant) {
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;
		String query = " DISTINCT DONO,CNAME,'' AS REMARK ";// ISNULL(REMARK,'')
		String extCond = "  isnull(status,'') = 'C' AND DONO <>'MISC-ISSUE' ";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		try {
			al = shipDao.selectShipHis(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("DONO"));
					xmlStr += XMLUtils.getXMLNode("supplier", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("CNAME")));
					xmlStr += XMLUtils.getXMLNode("remarks", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("REMARK")));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}

	public String getServerTimeForPrint(String Plant) {
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		String xmlStr = "";
		String serTime = "";
//		ArrayList al = null;
//		String query = " DISTINCT DONO,CNAME,'' AS REMARK ";
//		String extCond = "  isnull(status,'') = 'C' AND DONO <>'MISC-ISSUE' ";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		try {
			serTime = DateUtils.getDateAtTime();
			if (serTime.length() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(1) + "'");

				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("pono", serTime);
				xmlStr += XMLUtils.getXMLNode("supplier", serTime);
				xmlStr += XMLUtils.getXMLNode("remarks", serTime);
				xmlStr += XMLUtils.getEndNode("record");

				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return xmlStr;
	}

	public String getShipHisItemDetails(String Plant, String dono) {

		String xmlStr = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);

		//String query = "  DOLNO, ITEM,ITEMDESC,SUM(PICKQTY) as PICKQTY, ORDQTY  ";
		//String extCond = " isnull(status,'')  <> 'C' AND PICKQTY > 0 group by DOLNO,item,itemdesc, ORDQTY  ORDER BY ITEM ";
		
		String query = "  DOLNNO AS DOLNO, ITEM,ITEMDESC,SUM(ISNULL(QTYPICK,0)-ISNULL(QTYIS,0)) as PICKQTY, QTYOR AS ORDQTY  ";
		String extCond = " isnull(lnstat,'')  <> 'C' group by DOLNNO,item,itemdesc, QTYOR  ORDER BY ITEM ";
		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		
		
		
		try {
			al = shipDao.selectPDAShipHis(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					ItemMstDAO itemDao = new ItemMstDAO();
					itemDao.setmLogger(mLogger);
					String uom = StrUtils.fString(itemDao.getItemUOM(Plant,
							(String) map.get("ITEM")));
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dolno", (String) map
							.get("DOLNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("ITEMDESC")));
					
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("PICKQTY")));
												
					
					xmlStr += XMLUtils.getXMLNode("odrQty", StrUtils.formatNum((String) map
							.get("ORDQTY")));
					xmlStr += XMLUtils.getXMLNode("uom", uom);
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {

		}
		return xmlStr;
	}
	
	public String getShipHisItemDetailsPDA(String Plant, String dono) {

		String xmlStr = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);

		
		String query = "  DOLNNO AS DOLNO, ITEM,ITEMDESC,SUM(ISNULL(QTYPICK,0)-ISNULL(QTYIS,0)) as PICKQTY, QTYOR AS ORDQTY  ";
		String extCond = " isnull(lnstat,'')  <> 'C' AND ISNULL(QTYPICK,0) > 0  AND ISNULL(QTYPICK,0) > ISNULL(QTYIS,0) group by DOLNNO,item,itemdesc, QTYOR  ORDER BY ITEM ";
		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		try {
			al = shipDao.selectPDAShipHis(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					ItemMstDAO itemDao = new ItemMstDAO();
					itemDao.setmLogger(mLogger);
					String uom = StrUtils.fString(itemDao.getItemUOM(Plant,
							(String) map.get("ITEM")));
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dolno", (String) map
							.get("DOLNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("ITEMDESC")));
					
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("PICKQTY")));
												
					
					xmlStr += XMLUtils.getXMLNode("odrQty", StrUtils.formatNum((String) map
							.get("ORDQTY")));
					xmlStr += XMLUtils.getXMLNode("uom", uom);
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {

		}
		return xmlStr;
	}
	public String getShipHisItemDetailsForPrint(String Plant, String dono) {

		String xmlStr = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);

		String query = "  DOLNO, ITEM,ITEMDESC,SUM(PICKQTY) as PICKQTY, ORDQTY  ";
		String extCond = " isnull(status,'')  = 'C'  group by DOLNO,item,itemdesc, ORDQTY ";
		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		
		try {
			al = shipDao.selectShipHis(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					ItemMstDAO itemDao = new ItemMstDAO();
					itemDao.setmLogger(mLogger);
					String uom = StrUtils.fString(itemDao.getItemUOM(Plant,
							(String) map.get("ITEM")));
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dolno", (String) map
							.get("DOLNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("ITEMDESC")));
					xmlStr += XMLUtils.getXMLNode("qtypk", (String) map
							.get("PICKQTY"));
					xmlStr += XMLUtils.getXMLNode("odrQty", (String) map
							.get("ORDQTY"));
					xmlStr += XMLUtils.getXMLNode("uom", uom);
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {

		}
		return xmlStr;
	}

	public String getCustomerDetailsForPrint(String Plant, String dono) {
		String xmlStr = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);

		String query = "  dh.Remark2 as remarks2, cus.ADDR1 as address1, cus.ADDR2 as address2, cus.ADDR3 as address3, cus.ADDR4 as address4  ";
	
		String extCond = "";
		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		//
		try {
			al = shipDao.selectCustomerDetails(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					ItemMstDAO itemDao = new ItemMstDAO();
					itemDao.setmLogger(mLogger);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("remarks2", (String) map
							.get("remarks2"));
					xmlStr += XMLUtils.getXMLNode("address1", (String) map
							.get("address1"));
					xmlStr += XMLUtils.getXMLNode("address2", (String) map
							.get("address2"));
					xmlStr += XMLUtils.getXMLNode("address3", (String) map
							.get("address3"));
					xmlStr += XMLUtils.getXMLNode("address4", (String) map
							.get("address4"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {

		}
		return xmlStr;
	}

	public String getShipHisItemDetailsForPDA(String Plant, String dono) {

		String xmlStr = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);

		String query = "  DOLNO, ITEM,ITEMDESC,SUM(PICKQTY) as PICKQTY ";
		String extCond = " isnull(status,'')  <> 'C' group by DOLNO,item,itemdesc ";
		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		//
		try {
			al = shipDao.selectShipHis(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					ItemMstDAO itemDao = new ItemMstDAO();
					itemDao.setmLogger(mLogger);
					String uom = StrUtils.fString(itemDao.getItemUOM(Plant,
							(String) map.get("ITEM")));
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dolno", (String) map
							.get("DOLNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
							.replaceCharacters2SendPDA((String) map
									.get("ITEMDESC")));
					xmlStr += XMLUtils.getXMLNode("qtypk", (String) map
							.get("PICKQTY"));
					xmlStr += XMLUtils.getXMLNode("uom", uom);
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {

		}
		return xmlStr;
	}

	public String process_ship_confirmation(Map obj) throws Exception {
		String xmlStr = "";
		boolean flag = false;
		UserTransaction ut = null;
		Map sMap = null;
//		Map mapLine = null;
		List<String> orderList = new ArrayList<String>();
		List<String> productList = new ArrayList<String>();
		List<String> quantityPicked = new ArrayList<String>();

		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			orderList = (ArrayList<String>) obj.get("ORDER_NO_LIST");
			productList = (ArrayList<String>) obj.get("PRODUCT_LIST");
			quantityPicked = (ArrayList<String>) obj.get("QTY_LIST");
			if (orderList.size() > 0) {
				int i = 0;
				ItemMstDAO itMdao = new ItemMstDAO();
				itMdao.setmLogger(mLogger);
				for (String orderLintNo : orderList) {
					sMap = new HashMap();
					sMap.put(IConstants.PLANT, (String) obj.get(IConstants.PLANT));
					sMap.put(IConstants.DODET_DONUM, (String) obj.get(IConstants.DODET_DONUM));
					sMap.put(IConstants.LOGIN_USER, (String) obj.get(IConstants.LOGIN_USER));
					sMap.put(IConstants.QTY, StrUtils.removeFormat((String) quantityPicked.get(i)));
					sMap.put(IConstants.REMARKS, (String) obj.get(IConstants.REMARKS));
					sMap.put(IConstants.DODET_DOLNNO, orderLintNo);
					System.out.println("orderLintNo" + orderLintNo);
					sMap.put(IConstants.ITEM, (String) productList.get(i));
					sMap.put(IConstants.ITEM_DESC, itMdao.getItemDesc((String) obj.get(IConstants.PLANT), productList.get(i)));
					sMap.put("SHIPPINGNO", "");
					flag = process_wms_ship_confirmation(sMap);
					i++;
				}
			} else {
				xmlStr = XMLUtils.getXMLMessage(1,
						" No Ship Details Found to Confirm for DO :"
								+ obj.get(IConstants.DODET_DONUM) + " Order");

			}

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
			xmlStr = XMLUtils.getXMLMessage(1,
					"Error in Ship Confirm for Do  : "
							+ obj.get(IConstants.DODET_DONUM) + " Order");
			throw e;
		}

		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(0, "DO  : "
					+ obj.get(IConstants.DODET_DONUM)
					+ "  Confirmed successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(1,
					"Error in Ship Confirm for Do  : "
							+ obj.get(IConstants.DODET_DONUM) + " Order");
		}
		return xmlStr;
	}

	private boolean process_wms_ship_confirmation(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsIssueMaterial();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}

		public boolean deleteOBDetLineDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			DoDetDAO _doDetDAO = new DoDetDAO();
			ShipHisDAO _shipHisDAO = new ShipHisDAO();
			Hashtable httoDet = new Hashtable();
			_doDetDAO.setmLogger(mLogger);

			httoDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
			httoDet.put(IDBConstants.DODET_DONUM, ht.get(IDBConstants.DODET_DONUM));
			httoDet.put(IDBConstants.DODET_DOLNNO, ht.get(IDBConstants.DODET_DOLNNO));
			httoDet.put(IDBConstants.DODET_ITEM, ht.get(IDBConstants.DODET_ITEM));

			flag = _doDetDAO.delete(httoDet);
			if (flag) {

				DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
//				boolean dodetflag = 
						_DoTransferDetDAO.deleteDoDet(httoDet);
					 //delete dodet multi remarks
	                        Hashtable htRemarksDel = new Hashtable();
		                    htRemarksDel.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
		                    htRemarksDel.put(IDBConstants.DODET_DONUM, ht.get(IDBConstants.DODET_DONUM));
		                    htRemarksDel.put(IDBConstants.DODET_DOLNNO,ht.get(IDBConstants.DODET_DOLNNO));
		                    htRemarksDel.put(IDBConstants.DODET_ITEM, ht.get(IDBConstants.DODET_ITEM));
		                    flag = _doDetDAO.deleteDoMultiRemarks(htRemarksDel);
	                   
	                   //delete dodet multi remarks end
				try{
	    		    //To arrange the line no's
	    		        httoDet.remove(IDBConstants.DODET_DOLNNO);
	    		        httoDet.remove(IDBConstants.DODET_ITEM);
	    		        String  sql =  "SET DOLNNO =DOLNNO-1 ";
		                String extraCond=" AND DOLNNO > '" + ht.get(IDBConstants.DODET_DOLNNO) + "' ";
		                _doDetDAO.update(sql,httoDet,extraCond);
		                _DoTransferDetDAO.update(sql,httoDet,extraCond);
		                
		               
	                    if (flag) {
                    
	                              //update DODET_REMARKS
		                            Hashtable htRemarks = new Hashtable();
		                            htRemarks.put(IDBConstants.DODET_DONUM, ht.get(IDBConstants.DODET_DONUM));
		                            String  sqlRemarks =  "SET DOLNNO =DOLNNO-1 ";
		                            String extraCond1=" AND DOLNNO > '" + ht.get(IDBConstants.DODET_DOLNNO) + "' ";
		                            _doDetDAO.updateDoMultiRemarks(sqlRemarks,htRemarks,extraCond1,(String)ht.get(IDBConstants.PLANT));
	                            
	                            //update DODET_REMARKS end
	                    }
		                            
		           //To arrange the line no's in shiphis  
		                boolean shiphisflag = false;
		                shiphisflag = _shipHisDAO.isExisit(httoDet);
                      if(shiphisflag)
                      {  
                    	  sql =  "SET DOLNO =DOLNO-1 ";
                    	  extraCond=" AND CAST(DOLNO as int) > " + ht.get(IDBConstants.DODET_DOLNNO)  + " ";
                    	  _shipHisDAO.update(sql,httoDet,extraCond);
		               
                      }    
		                
	            }catch(Exception e){}
				MovHisDAO movhisDao = new MovHisDAO();
				Hashtable htmovHis = new Hashtable();
				movhisDao.setmLogger(mLogger);
				htmovHis.clear();
				htmovHis.put(IDBConstants.PLANT, (String) ht
						.get(IDBConstants.PLANT));
				htmovHis.put("DIRTYPE", "SALES_ORDER_DELETE_PRODUCT");
				htmovHis.put(IDBConstants.ITEM, (String) ht
						.get(IDBConstants.DODET_ITEM));
				htmovHis.put(IDBConstants.MOVHIS_ORDNUM, (String) ht
						.get(IDBConstants.DODET_DONUM));
				htmovHis.put(IDBConstants.MOVHIS_ORDLNO, (String) ht
						.get(IDBConstants.DODET_DOLNNO));
				htmovHis.put(IDBConstants.CREATED_BY, (String) ht
						.get(IDBConstants.CREATED_BY));
				htmovHis.put("MOVTID", "");
				htmovHis.put("RECID", "");
				htmovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htmovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

				flag = movhisDao.insertIntoMovHis(htmovHis);
			}

		} catch (Exception e) {
			throw e;
		}

		return flag;
	}

	public Boolean removeRow(String plant, String dono, String userId) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			ut = DbBean.getUserTranaction();
			ut.begin();
			String query="",q = "",estno="",estlnno="",qty="",item="",queryest="",updateEstHdr="",updatestatus="";
			EstDetDAO _EstDetDAO = new EstDetDAO();
			EstHdrDAO _EstHdrDAO = new EstHdrDAO();
			Hashtable htdet = new Hashtable();
			htdet.put(IDBConstants.PLANT, plant);
			htdet.put(IDBConstants.DODET_DONUM, dono);
			
			query = "isnull(estno,'') estno";
			ArrayList alhdrdet = _DoHdrDAO.selectDoHdr(query,htdet);
			for (int j = 0; j < alhdrdet.size(); j++) {
				Map m1 = (Map) alhdrdet.get(j);
				estno = (String) m1.get("estno");
				if(estno.length()>0){
					q = " isnull(item,'')item,isnull(estno,'') estno,isnull(estlnno,'') as estlnno,isnull(qtyor,0)as qty ";
					ArrayList aldet = _DoDetDAO.selectDoDet(q, htdet,	" plant <> ''");
					for (int i = 0; i < aldet.size(); i++) {	
							Map m = (Map) aldet.get(i);
							estno = (String) m.get("estno");
							estlnno =(String) m.get("estlnno");
							qty =(String) m.get("qty");
							item =(String) m.get("item");
							
							
							Hashtable htestCond = new Hashtable();
							htestCond.put("PLANT", plant);
							htestCond.put("estno", estno);
							htestCond.put("estlnno", estlnno);
							htestCond.put("item", item);
				
							queryest = "set qtyis= isNull(qtyis,0) - " + qty ;
							flag = _EstDetDAO.update(queryest, htestCond, "");
				
							updatestatus = "set STATUS=CASE WHEN qtyis > 0 THEN 'O' ELSE 'N' END";
							flag = _EstDetDAO.update(updatestatus, htestCond, "");
						
						}
					Hashtable htestCond = new Hashtable();
					htestCond.put("PLANT", plant);
					htestCond.put("estno", estno);
					htestCond.put("estlnno", estlnno);
					htestCond.put("item", item);
					flag = _EstDetDAO.isExisit(htestCond," qtyis > 0 ");	
					if(flag)
						updateEstHdr = "set  status='Pending',ORDER_STATUS='PARTIALLY PROCESSED' ";
					else
						updateEstHdr = "set  status='Pending',ORDER_STATUS='Open' ";
					Hashtable htCond = new Hashtable();
					htCond.put("PLANT", plant);
					htCond.put("estno", estno);
				
					flag = _EstHdrDAO.update(updateEstHdr, htCond, "");
				}
				else
				{
					flag= true;
				}
			}
			Boolean removeHeader = this._DoHdrDAO.removeOrder(plant, dono);
			Boolean removeDetails = this._DoDetDAO.removeOrderDetails(plant,dono);
//			Boolean removeShipHisDetails = 
					new ShipHisDAO().removeOrder(plant,dono);
			
		    Boolean removeDoTransferHeader = this._DoTranHdrDAO.removeOrder(plant, dono);
		    Boolean removeDoTransferdetails = this._DoTranDetDAO.DoremoveOrderDetails(plant, dono);
 //delete  dodet multi remarks 
            if(removeDetails){
               
                Hashtable htRemarksDel = new Hashtable();
                htRemarksDel.put(IDBConstants.PLANT,plant);
                htRemarksDel.put(IDBConstants.DODET_DONUM,  dono);
 				if(this._DoDetDAO.isExisitDoMultiRemarks(htRemarksDel))
                {
                   flag = _DoDetDAO.deleteDoMultiRemarks(htRemarksDel);
                }
            }
           //delete dodet multi remarks end
			Hashtable htMovHis = createMoveHisHashtable(plant, dono, userId);
			Boolean movementHiss = movHisDao.insertIntoMovHis(htMovHis);
			if (flag & removeHeader & removeDetails & removeDoTransferHeader & removeDoTransferdetails & movementHiss) {
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
	public Boolean removeOrder(Hashtable ht , String condtn) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		String plant="",dono="",item="",loginuser="";
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			ut = DbBean.getUserTranaction();
			ut.begin();
			plant = (String)ht.get(IDBConstants.PLANT);
			dono = (String)ht.get(IDBConstants.DONO);
			item = (String)ht.get(IDBConstants.PRODUCTID);
			loginuser = (String)ht.get(IDBConstants.LOGIN_USER);
			Boolean removeHeader = this._DoHdrDAO.removeOrder(plant, dono);
			Boolean removeDetails = this._DoDetDAO.removeOrderDetails(plant,
					dono);
			Boolean remtranHdr = this._DoTranHdrDAO.removeOrder(plant, dono);
			Boolean remtranDetails = this._DoTranDetDAO.removeOrder(plant, dono);
			Hashtable htMovHis = createMoveHisHashtable(plant, dono, loginuser,item);
			Boolean movementHiss = movHisDao.insertIntoMovHis(htMovHis);
			if (removeHeader & removeDetails & remtranHdr & remtranDetails & movementHiss) {
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
	private Hashtable createMoveHisHashtable(String plant, String dono,
			String userId) {
		Hashtable<String, String> htMovHis = new Hashtable<String, String>();

		htMovHis.put(IDBConstants.PLANT, plant);
		htMovHis.put("DIRTYPE", "DELETE_SALES_ORDER");
		htMovHis.put(IDBConstants.ITEM, " ");
		htMovHis.put("BATNO", " ");
		htMovHis.put("ORDNUM", dono);

		htMovHis.put("MOVTID", " ");
		htMovHis.put("RECID", " ");
		htMovHis.put(IDBConstants.LOC, " ");

		htMovHis.put(IDBConstants.CREATED_BY, userId);
		htMovHis.put(IDBConstants.TRAN_DATE, DateUtils
				.getDateinyyyy_mm_dd(DateUtils.getDate()));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

		return htMovHis;
	}
	private Hashtable createMoveHisHashtable(String plant, String dono,
			String userId,String item) {
		Hashtable<String, String> htMovHis = new Hashtable<String, String>();

		htMovHis.put(IDBConstants.PLANT, plant);
		htMovHis.put("DIRTYPE", TransactionConstants.DEL_REGISTRATION_ORDER);
	
		htMovHis.put("BATNO", " ");
		htMovHis.put("ORDNUM", dono);
		htMovHis.put("ITEM", item);
		htMovHis.put("MOVTID", " ");
		htMovHis.put("RECID", " ");
		htMovHis.put(IDBConstants.LOC, " ");

		htMovHis.put(IDBConstants.CREATED_BY, userId);
		htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

		return htMovHis;
	}
	public ArrayList listDODetilstoPrint(String plant, String aDONO)
			throws Exception {
//		String q = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		try {
			al = shipDao.getDoDetailToPrint(plant, aDONO);

		} catch (Exception e) {
			throw e;
		}

		return al;
	}
	
	public ArrayList listDODetilstoPrintNew(String plant, String aDONO,String afromdate,String atodate)
	throws Exception {
//		String q = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		try {
			al = shipDao.getDoDetailToPrintNew(plant, aDONO,afromdate,atodate);
		
		} catch (Exception e) {
			throw e;
		}
		
		return al;
	}
	
	public ArrayList listVoidDetilstoPrint(String plant, String aDONO,String afromdate,String atodate)
			throws Exception {
//		String q = "";
		ArrayList al = null;
		ShipHisDAO shipDao = new ShipHisDAO();
		shipDao.setmLogger(mLogger);
		try {
			al = shipDao.getVoidDetailToPrint(plant, aDONO,afromdate,atodate);
			
		} catch (Exception e) {
			throw e;
		}
		
		return al;
	}

	public boolean isOpenOutBoundOrder(String plant, String dono)
			throws Exception {

		boolean flag = false;
		try {

			DoHdrDAO _doHdrDAO = new DoHdrDAO();
			_doHdrDAO.setmLogger(mLogger);

			Hashtable htCondidoHdr = new Hashtable();
			htCondidoHdr.put("PLANT", plant);
			htCondidoHdr.put("dono", dono);

			flag = _doHdrDAO.isExisit(htCondidoHdr, " STATUS ='N'");

			/*if (!flag) {
				throw new Exception(
						" Processed Outbound Order Cannot be modified");
			}*/

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public String getReturnCustomerDetailsForPrint(String Plant, String dono) {

		String xmlStr = "";
		ArrayList al = null;
		CustomerReturnDAO cusRetDao = new CustomerReturnDAO();
		cusRetDao.setmLogger(mLogger);

		String query = "  cus.CNAME as CNAME, cus.ADDR1 as address1, cus.ADDR2 as address2, cus.ADDR3 as address3, cus.ADDR4 as address4  ";
		
		String extCond = "";
		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		//
		try {
			al = cusRetDao.selectReturnCustomerDetails(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					ItemMstDAO itemDao = new ItemMstDAO();
					itemDao.setmLogger(mLogger);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("CNAME", (String) map
							.get("CNAME"));
					xmlStr += XMLUtils.getXMLNode("address1", (String) map
							.get("address1"));
					xmlStr += XMLUtils.getXMLNode("address2", (String) map
							.get("address2"));
					xmlStr += XMLUtils.getXMLNode("address3", (String) map
							.get("address3"));
					xmlStr += XMLUtils.getXMLNode("address4", (String) map
							.get("address4"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {

		}
		return xmlStr;
	}
	
	
	 public Map getDOReceiptHdrDetails(String aplant,String ordertype) throws Exception {
	        Map m =  new HashMap();
	     
	        m=_DoHdrDAO.getDOReciptHeaderDetails(aplant,ordertype);
	         return m;
	 }
	 public Map getDOReceiptHdrDetailsDO(String aplant,String ordertype) throws Exception {
	        Map m =  new HashMap();
	     
	        m=_DoHdrDAO.getDOReciptHeaderDetailsDO(aplant,ordertype);
	         return m;
	 }
	    
	    public Map getDOReceiptInvoiceHdrDetails(String aplant,String orderType) throws Exception {
	        Map m =  new HashMap();
	       
	        m=_DoHdrDAO.getDOReciptInvoiceHeaderDetails(aplant,orderType);
	         return m;
	 }
	    
   
	    public Map getDOReceiptInvoiceHdrDetailsDO(String aplant,String orderType) throws Exception {
	        Map m =  new HashMap();
	       
	        m=_DoHdrDAO.getDOReciptInvoiceHeaderDetailsDO(aplant,orderType);
	         return m;
	 }
    public boolean isExitsDoLine(Hashtable ht)
                    throws Exception {

            boolean flag = false;
            try {

                    DoDetDAO _dodetDAO = new DoDetDAO();
                    _dodetDAO.setmLogger(mLogger);
                    flag = _dodetDAO.isExisit(ht);

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }

            return flag;
    }
	public boolean isNewStatusDONO(String pono, String plant) throws Exception {
		boolean exists = false;
		try {
			String extCon = " STATUS IN ('O','C') ";
			_DoDetDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.OUT_DONO, pono);
			if (isExistOntable(ht, extCon))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	
	private boolean isExistOntable(Hashtable ht, String extCon) throws Exception {
		boolean exists = false;
		try {
			_DoDetDAO.setmLogger(mLogger);
			if (_DoDetDAO.getCountDoNo(ht, extCon) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	
	public boolean isExistDONO(String pono, String plant) throws Exception {
		boolean exists = false;
		try {
			_DoDetDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.OUT_DONO, pono);
			if (isExistOntable(ht, ""))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	
	public int getMaxDoLnNo(String dono, String plant) throws Exception {
		int maxDoLnNo = 0;
		try {
			_DoDetDAO.setmLogger(mLogger);
			
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.OUT_DONO, dono);
			
			maxDoLnNo = _DoDetDAO.getMaxDoLnNo(ht);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return maxDoLnNo;
	}
        
    public ArrayList getMobileEnQuiryOrderDetails(String query, Hashtable ht)
                    throws Exception {
            ArrayList al = new ArrayList();

            try {
                    _DoHdrDAO.setmLogger(mLogger);
                    al = _DoHdrDAO.selectMobileEnquiryDoHdr(query, ht);
            } catch (Exception e) {

                    throw e;
            }
            return al;
    }
    
    public boolean process_IssueMaterialWOInvcheck(Map obj) throws Exception {
            boolean flag = false;
            UserTransaction ut = null;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();

                    this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.OBISSUE
                                    + " :: " + SConstant.ORDERNO + " : "
                                    + (String) obj.get(IConstants.DODET_DONUM) + " :: "
                                    + SConstant.ORDERLN + " : "
                                    + (String) obj.get(IConstants.DODET_DOLNNO) + " :: "
                                    + SConstant.ITEM + " : "
                                    + (String) obj.get(IConstants.ITEM) + " :: "
                                    + SConstant.BATCH + " : "
                                    + (String) obj.get(IConstants.INV_BATCH) + " :: "
                                    + SConstant.QTY + " : "
                                    + (String) obj.get(IConstants.INV_QTY));

                    flag = process_Wms_IssuingWOInvcheck(obj);

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
    
    public boolean process_DoPickingWOInvCheck(Map obj) throws Exception {
            boolean flag = false;
            UserTransaction ut = null;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();

                    ut.begin();
                    flag = process_Wms_DoPickingWOInvcheck(obj);

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

    private boolean process_Wms_DoPickingWOInvcheck(Map map) throws Exception {
            boolean flag = false;

            WmsTran tran = new WmsPickingWOInvcheck();
            ((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
            flag = tran.processWmsTran(map);
            return flag;
    }

    private boolean process_Wms_IssuingWOInvcheck(Map map) throws Exception {
            boolean flag = false;

            WmsTran tran = new WmsIssueWOInvcheck();
            ((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
            flag = tran.processWmsTran(map);

            return flag;
    }
    
    public String getClassList(String plant,String ClassId) {

		String xmlStr = "";
	        List listQry =null;
	        ItemUtil itemUtil = new ItemUtil();
//		XMLUtils xmlUtils = new XMLUtils();
		itemUtil.setmLogger(mLogger);
		try {
			listQry = qryClassMst(plant,ClassId);

			if (listQry.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("products total='"+ String.valueOf(listQry.size()) + "'");
				for (int i = 0; i < listQry.size(); i++) {
				    Vector vecItem   = (Vector)listQry.get(i);
				     
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("classId", (String)vecItem.get(0));
                                        xmlStr += XMLUtils.getXMLNode("classDesc", StrUtils.replaceCharacters2Send((String)vecItem.get(1)));
    					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("products");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    public String getAttendanList(String plant,String Attendan) {

		String xmlStr = "";
	        List listQry =null;
	        ItemUtil itemUtil = new ItemUtil();
//		XMLUtils xmlUtils = new XMLUtils();
		itemUtil.setmLogger(mLogger);
		try {
			listQry = qryAttendan(plant,Attendan);

			if (listQry.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("products total='"+ String.valueOf(listQry.size()) + "'");
				for (int i = 0; i < listQry.size(); i++) {
				    Vector vecItem   = (Vector)listQry.get(i);
				   	xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("AttendanId", (String)vecItem.get(0));
                                        xmlStr += XMLUtils.getXMLNode("AttendanName", StrUtils.replaceCharacters2Send((String)vecItem.get(1)));
    					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("products");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    
    public List qryClassMst(String plant, String aClass)
	throws Exception {
		List listQry = new ArrayList();
		try {
			
			listQry = _DoDetDAO.queryClassMst(plant,aClass);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
    }
    
    public List qryAttendan(String plant, String aAttendan)
	throws Exception {
		List listQry = new ArrayList();
		try {
			
			listQry = _DoDetDAO.queryAttendan(plant,aAttendan);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listQry;
    }
    
    public String getCheckInDetails(String Plant, String ClassId, String Status) {

		String xmlStr = "";
		ArrayList al = null;

		
		try {
			
			al = _DoDetDAO.selectClassDetails(Plant, ClassId, Status);

			if (al.size() > 0) {
			    String strStatus="";
			    String strPassStatus="";
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
				
					
					
					Map map = (Map) al.get(i);
					strStatus=(String) map.get("LNSTAT");
					trantype= (String) map.get("TRANTYPE");
					if (strStatus.equals("N"))
					{
						strPassStatus="Registered";
					}
					else if (strStatus.equals("C")&& trantype.equalsIgnoreCase("Attendance"))
					{
						strPassStatus="Attended";
					}
					else if (strStatus.equals("O")&& trantype.equalsIgnoreCase("Clockin"))
					{
						strPassStatus="ClockIn";
					}
					else if (strStatus.equals("C")&& trantype.equalsIgnoreCase("Clockin"))
					{
						strPassStatus="ClockOut";
					}
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ORDERNO", (String) map.get("DONO"));
					xmlStr += XMLUtils.getXMLNode("ATTENDANID", (String) map.get("CUSTCODE"));
					xmlStr += XMLUtils.getXMLNode("ATTENDANNAME", (String) map.get("CUSTNAME"));
					xmlStr += XMLUtils.getXMLNode("STATUS", strPassStatus);
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
    
    
    public String getAttendanDetails(String Plant, String Attendan) {

		String xmlStr = "";
		ArrayList al = null;

		
		try {
			
			al = _DoDetDAO.selectAttendanDetails(Plant, Attendan);

			if (al.size() > 0) {
			    String strStatus="";
			    String strPassStatus="";
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
				
					
					
					Map map = (Map) al.get(i);
					strStatus=(String) map.get("LNSTAT");
					trantype = (String) map.get("TRANTYPE");
					if (strStatus.equals("N"))
					{
						strPassStatus="Registered";
					}
					else if (strStatus.equals("C")&&trantype.equalsIgnoreCase("Attendance"))
					{
						strPassStatus="Attended";
					}
					else if (strStatus.equals("O")&&trantype.equalsIgnoreCase("Clockin"))
					{
						strPassStatus="ClockIn";
					}
					else if (strStatus.equals("C")&&trantype.equalsIgnoreCase("Clockin"))
					{
						strPassStatus="ClockOut";
					}
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ORDERNO", (String) map.get("DONO"));
					xmlStr += XMLUtils.getXMLNode("ATTENDANID", (String) map.get("CUSTCODE"));
					xmlStr += XMLUtils.getXMLNode("ATTENDANNAME", (String) map.get("CUSTNAME"));
					xmlStr += XMLUtils.getXMLNode("STATUS", strPassStatus);
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
    
    /*public String getMovementHisClockin(String Plant, String item,String customer) {

		String xmlStr = "",extcondition="",dono="",trandate="",dirtype="";
		
		HTReportUtil reportUtil = new HTReportUtil();
		Hashtable ht = new Hashtable();
		try {
			extcondition = " select dono from "+Plant+"_dohdr where custcode=";
			extcondition = extcondition +" ( select custno from "+Plant+"_custmst where custno='"+customer+"'" ;
			extcondition += " or hpno ='"+ customer +"'"+")";
			ht.put("b.item", item);
			ht.put("a.ordertype", IDBConstants.MOBILE_REGISTRATION);

			List dolist = reportUtil.getMobileAttendanceList(ht, extcondition, Plant);
			for (int i = 0; i < dolist.size(); i++) {
				Map linemap = (Map) dolist.get(i);
				dono = (String)linemap.get("dono");
							}
			ht.clear();
			ht.put("ORDNUM", dono);
			String extcondtn= " DIRTYPE IN ('"+ TransactionConstants.CHECK_IN+"','"+TransactionConstants.CHECK_OUT+"')";
			List movhislst = reportUtil.getMovHisClockInOutList(ht, Plant, extcondtn); 
			String trDate= "";
			
			if (movhislst.size() > 0) {
			    String strStatus="";
			    String strPassStatus="";
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"+ String.valueOf(movhislst.size()) + "'");
				for (int i = 0; i < movhislst.size(); i++) {
					
					Map map = (Map) movhislst.get(i);
					trDate=(String)map.get("CRAT");
		               if (trDate.length()>8) {
		                          
		                    trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
		                    }
					trandate = (String) map.get("TRANDATE");
					
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("DIRTYPE", (String) map.get("DIRTYPE"));
					xmlStr += XMLUtils.getXMLNode("TRANDATE", trandate);
					xmlStr += XMLUtils.getXMLNode("TRANTIME", trDate);
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}*/
    
  //Start code added by Bruhan for base currency on Aug 28th 2012.
    public String getConvertedUnitCostForProduct(String aPlant, String doNO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrder(aPlant, doNO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public String getminSellingConvertedUnitCostForProduct(String aPlant, String doNO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getminSellingUnitCostBasedOnCurIDSelectedForOrder(aPlant, doNO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
    
	public String getminSellingConvertedUnitCostForProductimport(String aPlant, String currencyID) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getminSellingUnitCostBasedOnCurIDSelectedForImportOrder(aPlant, currencyID);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
    public String getConvertedUnitCostToLocalCurrency(String aPlant, String doNO,String unitCost) throws Exception {
        String localCurrencyConvertedUnitCost="";
          try{
               localCurrencyConvertedUnitCost= _DoHdrDAO.getUnitCostCovertedTolocalCurrency(aPlant, doNO,unitCost);
          }catch(Exception e){
              throw e;
          }
        return localCurrencyConvertedUnitCost;
    }
    
    public String getCurrencyUseQT(String plant,String doNO) throws Exception{
    	String currencyUseQT = "";
    	try{
    		currencyUseQT = _DoHdrDAO.getCurrencyUseQT(plant,doNO);
    	}
    	catch(Exception e ){
    		this.mLogger.exception(this.printLog, "", e);
    	}
    	
    	return currencyUseQT;
    }
    

  //End code added by Bruhan for base currency on Aug 28th 2012.
    
    
   
    
 
//Start code by Bruhan for ouboundorder by product on 27Feb2013   
 public ArrayList listOutboundSummaryByProd(String item, String plant)
    throws Exception {
    	String q = "";
//    	String link = "";
//    	String result = "";
//    	String where = "";
    	ArrayList al = null;
    	try {
    		Hashtable htCond = new Hashtable();
    		htCond.put("PLANT", plant);
    		htCond.put("item", item);
    		q = "dono,dolnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtypick,0) as qtypick,loc as loc,userfld4 as batch,isnull(userfld3,'') as cname,isnull(UNITMO,'') as uom,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";
    		_DoDetDAO.setmLogger(mLogger);
    		al = _DoDetDAO.selectDoDet(q, htCond," plant <> '' and lnstat <> 'C' order by dolnno");
    		} catch (Exception e) {
    this.mLogger.exception(this.printLog, "", e);
    throw e;
    	}

    	return al;
}
 //End code by Bruhan for ouboundorder by product on 27Feb2013

//Start code by Bruhan for ouboundorder pick issue(random) on 8 Apr 2013
 public ArrayList listDODETDetailswithscanqty(String plant,String aDONO)
 throws Exception {
	 	String q = "";
//	 	String link = "";
//	 	String result = "";
//	 	String where = "";
	 	ArrayList al = null;

	 	try {
	 		Hashtable htCond = new Hashtable();
	 		htCond.put("PLANT", plant);
	 		htCond.put("dono", aDONO);
	 		
	 		q = "dono,item,itemdesc,sum(ISNULL(qtyor,0)) as qtyor,sum(ISNULL(qtyPick,0)) as qtypick,sum(ISNULL(qtyis,0)) as qtyis,"
	 			+"isnull((select SUM(qty)  from "+plant+"_RANDOM_SCAN_TEMP where "+plant+"_RANDOM_SCAN_TEMP.ORDERNO =a.DONO" 
	 			+" and "+plant+"_RANDOM_SCAN_TEMP.ITEM =a.ITEM group by item),0)as scannedqty";
	 		_DoDetDAO.setmLogger(mLogger);
	 		al = _DoDetDAO.selectDoDet(q, htCond,
                 " plant <> '' and lnstat <>'C' group by item,dono,ItemDesc order by item,dono");


	 	} catch (Exception e) {
	 		throw e;
	 	}
	 	return al;
}



public String getPickingItemDetails(String aPlant, String aDono,String aItem,String user) {
				String xmlStr = "";
				ArrayList al = null;
				try {
					al = _DoDetDAO.selectPickItemDetails(aPlant, aDono,aItem);
					if (al.size() > 0) {
//					    String trantype="";
						xmlStr += XMLUtils.getXMLHeader();
						xmlStr += XMLUtils.getStartNode("PickitemDetails total='"+ String.valueOf(al.size()) + "'");
						for (int i = 0; i < al.size(); i++) {
							Map map = (Map) al.get(i);
							xmlStr += XMLUtils.getStartNode("record");
							xmlStr += XMLUtils.getXMLNode("ITEM", (String) map.get("ITEM"));
							xmlStr += XMLUtils.getXMLNode("ITEMDESC",  StrUtils.replaceCharacters2Send((String) map.get("ITEMDESC")));
							xmlStr += XMLUtils.getXMLNode("UOM", (String) map.get("UOM"));
							xmlStr += XMLUtils.getEndNode("record");
						}
						xmlStr += XMLUtils.getEndNode("PickitemDetails");
					}
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				return xmlStr;
			}

public String getRandomOutBoundOrderOrderDetails(String Plant, String dono,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList al = null;
		DoDetDAO dodetDao = new DoDetDAO();
		dodetDao.setmLogger(mLogger);

		//String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String query = " DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME ";
		String extCond = " ";
		if (!isReceived) {
			extCond = extCond + " PickStatus <> 'C' ";
		}

		extCond = extCond + " GROUP BY DONO,USERFLD3,ITEM,ITEMDESC,UNITMO ORDER BY DONO DESC";

		Hashtable ht = new Hashtable();
		//ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		//
		try {
			al = dodetDao.selectDoDet(query, ht, extCond);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map
							.get("DONO"));
					
					xmlStr += XMLUtils.getXMLNode("customer",  (String) StrUtils.replaceCharacters2SendPDA(map.get("CUSTOMERNAME")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("QTYPICK")));
					xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
							.get("QTYISSUE")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", "");
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
		}
		return xmlStr;
	}
	
 		 
 		public String getRandomOutBoundOrderItemDetails(String Plant, String dono,
 				Boolean isReceived) {
 			String xmlStr = "";
 			ArrayList al = null;
 			DoDetDAO dodetDao = new DoDetDAO();
 			dodetDao.setmLogger(mLogger);

 			//String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
 			String query = " DOLNNO,DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL((select u.QPUOM from "+Plant+"_UOM u where u.UOM=UNITMO),1) as UOMQTY,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME ";
 			String extCond = " ";
 			if (!isReceived) {
 				extCond = extCond + " PickStatus <> 'C' ";
 			}

 			extCond = extCond + " GROUP BY DOLNNO,DONO,USERFLD3,ITEM,ITEMDESC,UNITMO ORDER BY DOLNNO ";

 			Hashtable ht = new Hashtable();
 			ht.put("DONO", dono);
 			ht.put("PLANT", Plant);
 			//
 			try {
 				al = dodetDao.selectDoDet(query, ht, extCond);
 				if (al.size() > 0) {
 					xmlStr += XMLUtils.getXMLHeader();
 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
 							+ String.valueOf(al.size()) + "'");
 					for (int i = 0; i < al.size(); i++) {
 						Map map = (Map) al.get(i);
 						xmlStr += XMLUtils.getStartNode("record");
 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
 								.get("DONO"));
 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
 								.get("DONO"));
 						xmlStr += XMLUtils.getXMLNode("customer",  (String) StrUtils.replaceCharacters2SendPDA(map.get("CUSTOMERNAME")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("item", (String) map
 								.get("ITEM"));
 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
 								.get("QTYOR")));
 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
 								.get("QTYPICK")));
 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
 								.get("QTYISSUE")));
 						xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
 						xmlStr += XMLUtils.getXMLNode("remarks", "");
 						xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
 								.get("UOMQTY"));
 						xmlStr += XMLUtils.getEndNode("record");
 					}
 					xmlStr += XMLUtils.getEndNode("itemDetails");
 				}

 			} catch (Exception e) {
 			}
 			return xmlStr;
 		}
 		
 		public String getRandomOutBoundOrderIssueItemDetails(String Plant, String dono,
 				Boolean isReceived) {
 			String xmlStr = "";
 			ArrayList al = null;
 			DoDetDAO dodetDao = new DoDetDAO();
 			DoHdrDAO DoHdrDAO = new DoHdrDAO();
 			dodetDao.setmLogger(mLogger);
 			DoHdrDAO.setmLogger(mLogger);

 			//String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
 			String query = "dono,cname,container,issuedate,dolno,item,batch,loc,ordqty,pickqty,itemdesc ,ISNULL((select ISNULL(UNITMO,'') from " + Plant + "_"+ "DODET a where c.DONO=a.DONO and c.DOLNO=a.DOLNNO and c.ITEM=a.ITEM),'') uom  ";
 			String extCond = " ";
 //			if (!isReceived) {
 				extCond = extCond + " status <> 'C' and LOC like'%SHIPPINGAREA%' ";
 	//		}

 			extCond = extCond + " ORDER BY DOLNO ";

 			Hashtable ht = new Hashtable();
 			ht.put("DONO", dono);
 			ht.put("PLANT", Plant);
 			//
 			try {
 				al = dodetDao.selectDoDetForIssue(query, ht, extCond);
 				String cSign = StrUtils.fString(DoHdrDAO.getSignatureCheck(Plant,dono));
 				if (al.size() > 0) {
 					xmlStr += XMLUtils.getXMLHeader();
 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
 							+ String.valueOf(al.size()) + "'");
 					for (int i = 0; i < al.size(); i++) {
 						Map map = (Map) al.get(i);
 						xmlStr += XMLUtils.getStartNode("record");
 						
 						xmlStr += XMLUtils.getXMLNode("item", (String) map
 								.get("item"));
 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("itemdesc")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
 								.get("ordqty")));
 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
 								.get("pickqty")));
 						
 						xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("uom"));
 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
 								.get("dono"));
 						
 						xmlStr += XMLUtils.getXMLNode("customer",  (String) StrUtils.replaceCharacters2SendPDA(map.get("cname")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("orddate", (String) map
 								.get("issuedate"));
 						xmlStr += XMLUtils.getXMLNode("polnno", (String) map
 								.get("dolno"));
 						xmlStr += XMLUtils.getXMLNode("loc", ((String) map
 								.get("loc")));
 						xmlStr += XMLUtils.getXMLNode("batch", ((String) map
 								.get("batch")));
 						xmlStr += XMLUtils.getXMLNode("container", ((String) map
 								.get("container")));
 						xmlStr += XMLUtils.getXMLNode("csign", cSign);
 						
 						
 						xmlStr += XMLUtils.getEndNode("record");
 					}
 					xmlStr += XMLUtils.getEndNode("itemDetails");
 				}

 			} catch (Exception e) {
 			}
 			return xmlStr;
 		}

 		
 		public String getRandomOutBoundOrderProductctDetailsScanByProduct(String Plant, String item,
 				Boolean isReceived) {
 			String xmlStr = "";
 			ArrayList al = null;
 			DoDetDAO dodetDao = new DoDetDAO();
 			dodetDao.setmLogger(mLogger);

 			//String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
 			String query = " DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,isnull(TRANDATE,'')ORDDATE,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME ";
 			String extCond = " ";
 			if (!isReceived) {
 				extCond = extCond + " PickStatus <> 'C' and dono not in (SELECT dono from ["+Plant+"_DOHDR] q where q.dono=a.dono and ORDER_STATUS='Draft' )";
 			}
 			extCond = extCond + " GROUP BY DONO,USERFLD3,ITEM,ITEMDESC,UNITMO,TRANDATE ORDER BY ORDDATE DESC ";

 			Hashtable ht = new Hashtable();
 			ht.put("ITEM", item);
 			ht.put("PLANT", Plant);
 			//
 			try {
 				al = dodetDao.selectDoDet(query, ht, extCond);
 				if (al.size() > 0) {
 					xmlStr += XMLUtils.getXMLHeader();
 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
 							+ String.valueOf(al.size()) + "'");
 					for (int i = 0; i < al.size(); i++) {
 						Map map = (Map) al.get(i);
 						xmlStr += XMLUtils.getStartNode("record");
 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
 								.get("DONO"));
 						xmlStr += XMLUtils.getXMLNode("customer",  (String) StrUtils.replaceCharacters2SendPDA(map.get("CUSTOMERNAME")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("item", (String) map
 								.get("ITEM"));
 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
 								.get("QTYOR")));
 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
 								.get("QTYPICK")));
 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
 								.get("QTYISSUE")));
 						xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
 						xmlStr += XMLUtils.getXMLNode("remarks", "");
 						//xmlStr += XMLUtils.getXMLNode("pickstatus", (String) map.get("PICKSTATUS"));
 						xmlStr += XMLUtils.getEndNode("record");
 					}
 					xmlStr += XMLUtils.getEndNode("itemDetails");
 				}

 			} catch (Exception e) {
 			}
 			return xmlStr;
 		}
 		
 		
 		public String getoutBoundOrder_item_orderline_details(String Plant, String dono,String item,
 				Boolean isReceived) {
 			String xmlStr = "";
 			ArrayList al = null;
 			ItemMstDAO itemMstDAO = new ItemMstDAO();
 			DoDetDAO dodetDao = new DoDetDAO();
 			DoHdrDAO DoHdrDAO = new DoHdrDAO();
 			dodetDao.setmLogger(mLogger);
 			itemMstDAO.setmLogger(mLogger);
 			DoHdrDAO.setmLogger(mLogger);

 			//String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
 			String query = " ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL((select u.QPUOM from "+Plant+"_UOM u where u.UOM=UNITMO),1) as UOMQTY,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE ";
 			String extCond = " lnstat <> 'C' ";
 			if (!isReceived) {
 				extCond = extCond + " and PickStatus <> 'C' ";
 			}

 			extCond = extCond + " GROUP BY DOLNNO,ITEM,ITEMDESC,UNITMO ORDER BY DOLNNO ";

 			Hashtable ht = new Hashtable();
 			ht.put("DONO", dono);
 			ht.put("PLANT", Plant);
 			ht.put("ITEM", item);
 			//
 			try {
 				String nonStock = StrUtils.fString(itemMstDAO.getNonStockFlag(Plant,item));
 				String cSign = StrUtils.fString(DoHdrDAO.getSignatureCheck(Plant,item));
 				al = dodetDao.selectDoDet(query, ht, extCond);
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
 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
 								.get("QTYOR")));
 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
 								.get("QTYPICK")));
 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
 								.get("QTYISSUE")));
 						xmlStr += XMLUtils.getXMLNode("uom", (String) map
 								.get("UNITMO"));
 						xmlStr += XMLUtils.getXMLNode("remarks", "");
 						xmlStr += XMLUtils.getXMLNode("nonStock", nonStock);
 						xmlStr += XMLUtils.getXMLNode("csign", cSign);
 						xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
 								.get("UOMQTY"));
 						xmlStr += XMLUtils.getEndNode("record");
 					}
 					xmlStr += XMLUtils.getEndNode("itemDetails");
 				}

 			} catch (Exception e) {
 			}
 			return xmlStr;
 		}

 		public String getoutBoundOrder_item_orderline_detailsnext(String Plant, String dono,String item,
 				String start,String end,Boolean isReceived) {
 			String xmlStr = "";
 			ArrayList al = null;
 			DoDetDAO dodetDao = new DoDetDAO();
 			dodetDao.setmLogger(mLogger);

 		//	String query = " DOLNNO,DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME,ISNULL((SELECT COUNT(*)from ["+Plant+"_DODET] WHERE DONO = '"+dono+"' AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO";
 			String query = " with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY DOLNNO)) AS ID,DOLNNO,DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME,ISNULL((SELECT COUNT(*)from ["+Plant+"_DODET] WHERE DONO = '"+dono+"' AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO from ["+Plant+"_DODET] a WHERE DONO = '"+dono+"' AND PLANT = '"+Plant+"' "; 
 			
 			String extCond = "";
 			if (!isReceived) {
 				query = query + " and PickStatus <> 'C' ";
 			}

 	//		extCond = extCond + " AND DOLNNO >="+start+" and DOLNNO<="+end+" GROUP BY DOLNNO,DONO,USERFLD3,ITEM,ITEMDESC,UNITMO ORDER BY DOLNNO";
 			query = query + " GROUP BY DOLNNO,DONO,USERFLD3,ITEM,ITEMDESC,UNITMO ) SELECT * FROM S WHERE ID >="+start+" and ID<="+end+" ORDER BY DOLNNO ";
 			Hashtable ht = new Hashtable();
 			/*ht.put("DONO", dono);
 			ht.put("PLANT", Plant);*/
 			//
 			try {
 				al = dodetDao.selectDoDetForPage(query, ht, extCond);
 				if (al.size() > 0) {
 					xmlStr += XMLUtils.getXMLHeader();
 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
 							+ String.valueOf(al.size()) + "'");
 					for (int i = 0; i < al.size(); i++) {
 						Map map = (Map) al.get(i);
 						xmlStr += XMLUtils.getStartNode("record");
 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
 								.get("DONO"));
 						
 						xmlStr += XMLUtils.getXMLNode("customer",  (String) StrUtils.replaceCharacters2SendPDA(map.get("CUSTOMERNAME")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("item", (String) map
 								.get("ITEM"));
 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
 								.get("QTYOR")));
 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
 								.get("QTYPICK")));
 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
 								.get("QTYISSUE")));
 						xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
 						xmlStr += XMLUtils.getXMLNode("remarks", "");
 						xmlStr += XMLUtils.getXMLNode("count", (String) map
 								.get("TOTPO"));
 						
 						xmlStr += XMLUtils.getEndNode("record");
 					}
 					xmlStr += XMLUtils.getEndNode("itemDetails");
 				}

 			} catch (Exception e) {
 			}
 			return xmlStr;
 		}
 		public String getoutBoundOrder_item_Byitem(String Plant, String dono,String item,
 				String start,String end,Boolean isReceived) {
 			String xmlStr = "";
 			ArrayList al = null;
 			DoDetDAO dodetDao = new DoDetDAO();
 			dodetDao.setmLogger(mLogger);
 			String query = " with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY DOLNNO)) AS ID,DOLNNO,DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL((select u.QPUOM from ["+Plant+"_UOM] as u where u.UOM=UNITMO),1) as UOMQTY,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME,ISNULL((SELECT COUNT(*)from ["+Plant+"_DODET] WHERE DONO = '"+dono+"' AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO from ["+Plant+"_DODET] a WHERE ITEM LIKE '%"+item+"%' AND DONO = '"+dono+"' AND PLANT = '"+Plant+"' ";
 		//	String query = " DOLNNO,DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME,ISNULL((SELECT COUNT(*)from ["+Plant+"_DODET] WHERE DONO = '"+dono+"' AND PLANT = '"+Plant+"' and lnstat <> 'C'),0) TOTPO";
 			String extCond = "";
 			if (!isReceived) {
 			//	extCond = extCond + " PickStatus <> 'C' ";
 				query = query + " and PickStatus <> 'C' ";
 			}
 		//	extCond = extCond + " AND DOLNNO >="+start+" and DOLNNO<="+end+" GROUP BY DOLNNO,DONO,USERFLD3,ITEM,ITEMDESC,UNITMO ORDER BY DOLNNO";
 			query = query + " GROUP BY DOLNNO,DONO,USERFLD3,ITEM,ITEMDESC,UNITMO ) SELECT * FROM S WHERE ID >="+start+" and ID<="+end+" ORDER BY DOLNNO ";
 			Hashtable ht = new Hashtable();
 			/*ht.put("ITEM", item);
 			ht.put("DONO", dono);
 			ht.put("PLANT", Plant);*/
 			//
 			try {
 				al = dodetDao.selectDoDetForPage(query, ht, extCond);
 				if (al.size() > 0) {
 					xmlStr += XMLUtils.getXMLHeader();
 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
 							+ String.valueOf(al.size()) + "'");
 					for (int i = 0; i < al.size(); i++) {
 						Map map = (Map) al.get(i);
 						xmlStr += XMLUtils.getStartNode("record");
 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
 								.get("DONO"));
 						
 						xmlStr += XMLUtils.getXMLNode("customer",  (String) StrUtils.replaceCharacters2SendPDA(map.get("CUSTOMERNAME")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("item", (String) map
 								.get("ITEM"));
 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
 										.toString()));
 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
 								.get("QTYOR")));
 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
 								.get("QTYPICK")));
 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
 								.get("QTYISSUE")));
 						xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
 						xmlStr += XMLUtils.getXMLNode("remarks", "");
 						xmlStr += XMLUtils.getXMLNode("count", (String) map
 								.get("TOTPO"));
 						xmlStr += XMLUtils.getXMLNode("uomqty", (String) map.get("UOMQTY"));
 						xmlStr += XMLUtils.getEndNode("record");
 					}
 					xmlStr += XMLUtils.getEndNode("itemDetails");
 				}

 			} catch (Exception e) {
 			}
 			return xmlStr;
 		}
 		
 		public String process_DoPickingForPDA_Random(Map obj) {
 			boolean flag = false;
 			UserTransaction ut = null;
 			try {
 				ut = com.track.gates.DbBean.getUserTranaction();
 				ut.begin();
 				flag = process_Wms_DoPicking_Random(obj);
 				if (flag == true) {
 					DbBean.CommitTran(ut);
 					flag = true;
 				} else {
 					DbBean.RollbackTran(ut);
 					flag = false;
 				}
 			} catch (Exception e) {
 				flag = false;
 				String xmlStr ="";
 				DbBean.RollbackTran(ut);
 				if(obj.containsKey("msgflag")){
	 				String msg =(String) obj.get("msgflag");
	 				xmlStr	= XMLUtils.getXMLMessage(0,
	 						msg + " For Product : " + obj.get(IConstants.ITEM).toString()
	 								+ " Order");
 				}else {
 					xmlStr = XMLUtils.getXMLMessage(0,
 	 						"Error in picking Product : " + obj.get(IConstants.ITEM)
 	 								+ " Order");
 				}
 				return xmlStr;
 			}

 			String xmlStr = "";
 			if (flag == true) {
 				if(obj.containsKey("msgflag")){
 					String msg =(String) obj.get("msgflag");
 					xmlStr = XMLUtils.getXMLMessage(1, msg + " For Product : "
 	 						+ obj.get(IConstants.ITEM).toString());
 				}else {
 					xmlStr = XMLUtils.getXMLMessage(1, "Product : "
 	 						+ obj.get(IConstants.ITEM) + "  picked successfully!");	
 				} 				
 			} else {
 				if(obj.containsKey("msgflag")){
 	 				String msg =(String) obj.get("msgflag");
 	 				xmlStr	= XMLUtils.getXMLMessage(0,
 	 						msg + " For Product : " + obj.get(IConstants.ITEM).toString()
 	 								+ " Order");
 				}else {
 					xmlStr = XMLUtils.getXMLMessage(0, "Error in picking Product : "
 	 						+ obj.get(IConstants.ITEM) + " Order");	
 				}
 				
 			}
 			return xmlStr;
 		}
 		public JSONObject process_DoPickingForPDA_Random_STD(Map obj) {
 			boolean flag = false;
 			UserTransaction ut = null;
 			JSONObject resultJson = new JSONObject();
 			try {
 				ut = com.track.gates.DbBean.getUserTranaction();
 				ut.begin();
 				flag = process_Wms_DoPicking_Random(obj);
 				if (flag == true) {
 					DbBean.CommitTran(ut);
 					flag = true;
 				} else {
 					DbBean.RollbackTran(ut);
 					flag = false;
 				}
 			} catch (Exception e) {
 				flag = false;
// 				String xmlStr ="";
 				DbBean.RollbackTran(ut);
 				if(obj.containsKey("msgflag")){
	 				String msg =(String) obj.get("msgflag");
//	 				xmlStr	= XMLUtils.getXMLMessage(0,
//	 						msg + " For Product : " + obj.get(IConstants.ITEM).toString()
//	 								+ " Order");
	 				resultJson.put("message",msg + " For Product : " + obj.get(IConstants.ITEM).toString()
								+ " Order");	
				 resultJson.put("status","0");
				 resultJson.put("LineNo",obj.get("LineNo").toString());
 				}else {
// 					xmlStr = XMLUtils.getXMLMessage(0,
// 	 						"Error in picking Product : " + obj.get(IConstants.ITEM)
// 	 								+ " Order");
 					resultJson.put("message","Error in picking Product : " + obj.get(IConstants.ITEM)
								+ " Order");
			 resultJson.put("status","0");
			 resultJson.put("LineNo",obj.get("LineNo").toString());
 				}
 				return resultJson;
 			}

// 			String xmlStr = "";
 			if (flag == true) {
 				if(obj.containsKey("msgflag")){
 					String msg =(String) obj.get("msgflag");
// 					xmlStr = XMLUtils.getXMLMessage(1, msg + " For Product : "
// 	 						+ obj.get(IConstants.ITEM).toString());
 					resultJson.put("message",msg + " For Product : "
 	 						+ obj.get(IConstants.ITEM).toString());
		            resultJson.put("status","1");
		            resultJson.put("LineNo",obj.get("LineNo").toString());
 				}else {
// 					xmlStr = XMLUtils.getXMLMessage(1, "Product : "
// 	 						+ obj.get(IConstants.ITEM) + "  picked successfully!");	
 					resultJson.put("message","Product : "
 	 						+ obj.get(IConstants.ITEM) + "  picked successfully!");	
		            resultJson.put("status","1");
		            resultJson.put("LineNo",obj.get("LineNo").toString());
 				} 				
 			} else {
 				if(obj.containsKey("msgflag")){
 	 				String msg =(String) obj.get("msgflag");
// 	 				xmlStr	= XMLUtils.getXMLMessage(0,
// 	 						msg + " For Product : " + obj.get(IConstants.ITEM).toString()
// 	 								+ " Order");
 	 				resultJson.put("message",msg + " For Product : " + obj.get(IConstants.ITEM).toString()
								+ " Order");
		            resultJson.put("status","0");
		            resultJson.put("LineNo",obj.get("LineNo").toString());
 				}else {
// 					xmlStr = XMLUtils.getXMLMessage(0, "Error in picking Product : "
// 	 						+ obj.get(IConstants.ITEM) + " Order");	
 					resultJson.put("message","Error in picking Product : "
 	 						+ obj.get(IConstants.ITEM) + " Order");	
	            resultJson.put("status","0");
	            resultJson.put("LineNo",obj.get("LineNo").toString());
 				}
 				
 			}
 			return resultJson;
 		}

 		private boolean process_Wms_DoPicking_Random(Map map) throws Exception {
 			boolean flag = false;

 			WmsTran tran = new WmsPickingRandom();
 			((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
 			flag = tran.processWmsTran(map);
 			return flag;
 		}
 		
 		 public ArrayList getDODetDetails(String Plant,
 				String DONO,String item) {
 	    	ArrayList al = null;

 			try {
 				al = _DoDetDAO.getDODetDetails(Plant, DONO, item);
 				 
 			} catch(Exception e ){
 	    		this.mLogger.exception(this.printLog, "", e);
 	    	}
 			
 	    return al;
 	    }
 		 
 		 public ArrayList getDODetDetailsRandom(String Plant,
  				String DONO,String item) {
  	    	ArrayList al = null;

  			try {
  				al = _DoDetDAO.getDODetDetailsRandom(Plant, DONO, item);
  				 
  			} catch(Exception e ){
  	    		this.mLogger.exception(this.printLog, "", e);
  	    	}
  			
  	    return al;
  	    }
 		 
 		 public ArrayList getDODetDetailsIssueRandom(String Plant,
   				String DONO,String item) {
   	    	ArrayList al = null;

   			try {
   				al = _DoDetDAO.getDODetDetailsIssueRandom(Plant, DONO, item);
   				 
   			} catch(Exception e ){
   	    		this.mLogger.exception(this.printLog, "", e);
   	    	}
   			
   	    return al;
   	    }
 		 
 		  public ArrayList getContainerSummary(Hashtable ht, String afrmDate,String atoDate,String aPlant) throws Exception {
 	            ArrayList arrList = new ArrayList();
 	            String sCondition = " ";
 	           //-----Added by Bruhan on Feb 7 2014, Description: Include From and To date in container summary search
 	           String fdate="",tdate="";
               if (afrmDate.length()>5)
            	   fdate    = afrmDate.substring(6)+ afrmDate.substring(3,5)+afrmDate.substring(0,2);

            	   if(atoDate==null) atoDate=""; else atoDate = atoDate.trim();
            	   if (atoDate.length()>5)
            	   tdate    = atoDate.substring(6)+ atoDate.substring(3,5)+atoDate.substring(0,2);
          	   
 	          
 	           if (fdate.length() > 0) {

					sCondition = sCondition + " AND  SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) >= '"// 
							+ fdate + "'  ";
					if (tdate.length() > 0) {
						sCondition = sCondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)<= '"
								+ tdate + "'  ";
					}
				    } else {
					if (tdate.length() > 0) {
						sCondition = sCondition + "  SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" 
								+ tdate + "'  ";
					}
				   }
 	           
 	           		if (ht.size() > 0) {
					if (ht.get("CUSTTYPE") != null) {
						String custtype = ht.get("CUSTTYPE").toString();
						sCondition= sCondition+" AND cname in (select CNAME from "+aPlant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
						ht.remove("CUSTTYPE");
					}
				}
                 //--Added by Bruhan end
 	            try {
 	                    InvMstDAO _InvMstDAO = new InvMstDAO();
 	                    _InvMstDAO.setmLogger(mLogger);
 	                   //sCondition = sCondition  + " group by cname,container,dono,item,itemdesc,batch";
 	                   sCondition=sCondition + " order by cname,container,dono,item,batch";
 	                    // Added stkuom to the query
 	                    String aQuery = "select isnull(cname,'')cname,isnull(container,'NOCONTAINER')container,dono,item,(select isnull(ITEMDESC,'') from "+aPlant+"_itemmst where item=a.item)itemdesc,batch, isnull(pickqty,0) qty,issuedate as transactiondate  from "
 	                                    + "["
 	                                    + aPlant
 	                                    + "_"
 	                                    + "shiphis"
 	                                    + "] A"
 	                                   
 	                                   //-----Command and modified by Bruhan on Feb 26 2014, Description: To display NOCONTAINER data's in container summary
 	                                   //+ "  where container is not null and container <> '' and container <> 'NOCONTAINER' and pickqty > 0";
 	                                  //+ "  where  pickqty > 0 and DONO <> 'MISC-ISSUE' and DONO like 'S%' and DONO not like 'SM%' ";     
 	                                  + "  where  pickqty > 0 and DONO <> 'MISC-ISSUE' and DONO in (SELECT SS.DONO FROM ["+aPlant+"_DOHDR] SS) ";     
 	                    arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

 	            } catch (Exception e) {
 	                    this.mLogger.exception(this.printLog, "", e);
 	            }
 	            return arrList;
 	    }

//start code added by Bruhan for outbound order reversal on 1 oct 2013  
 public ArrayList listOutboundSummaryReverseDODET(String plant, String aDONO)
 	throws Exception {
 String q = "";
 ArrayList al = null;

 try {
 	Hashtable htCond = new Hashtable();
 	htCond.put("b.PLANT", plant);
 	htCond.put("b.DONO", aDONO);
 	q = "b.dono dono,dolno,loc1 loc,b.item,b.itemdesc,isnull(ordqty,0) as qtyor,isnull(sum(Pickqty),0) as qtyissue,batch,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY ";     
 	_DoDetDAO.setmLogger(mLogger);
 	al = _DoDetDAO.selectReverseDoDet(q, htCond,
 			" a.plant <> '' and a.DONO=b.DONO and a.DOLNNO=b.DOLNO and STATUS='C' and a.ITEM=b.ITEM  group  by b.dono,dolno,loc1,b.item,b.itemdesc,ordqty,batch,UNITMO having isnull(sum(Pickqty),0)>0 order by cast(dolno as int)",plant);

 	
 } catch (Exception e) {
 	this.mLogger.exception(this.printLog, "", e);
 	throw e;
 }

 return al;
 }
 
 public boolean process_OBISSUEReversal(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;

		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Wms_OBISSUEReversal(obj);
			
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

	public boolean process_Wms_OBISSUEReversal(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsOBISSUEReversal();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}
//end code added by Bruhan for outbound order reversal on 1 oct 2013
 
	/* 
     * Created By Bruhan,June 26 2014, To process mobile sales order
     *  ************Modification History*********************************
     * 
     */
	public Boolean process_mobile_sales(Hashtable<String, String> requestData)
	throws Exception {
		Boolean result = Boolean.valueOf(false);
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			WmsTran tran = new WmsMobileSales();
			result = tran.processWmsTran(requestData);
			if (result == true) {
				DbBean.CommitTran(ut);
				result = true;
			} else {
				DbBean.RollbackTran(ut);
				result = false;
			}
		
		} catch (Exception e) {
			result = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return result;
	}
	
	
	/*---Added by Bruhan on July 4 2014, Description:To get PDA Mobile Sales Summary by Order
	 *******   Modification History
	 * Bruhan, Aug 20 2014, To handle special character for customer name
	 * 
	 * 
	 */
	public String get_pda_mobilesales_summary_by_order(Hashtable ht, 
    		String afrmDate,String atoDate,String itemDesc,String plant,String userid) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",customerstatusid="",customerstatusdesc="",customertypeid ="",customertypedesc="";
		String xmlStr = "";//,fdate="",tdate="";

		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(B.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	      }
	      		
			 String dtCondStr =    " and ISNULL(CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
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
				String extraCond	=" group by b.dono,a.custcode,a.custname,a.ordertype ,a.collectiondate order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
				
				String aQuery = "select  b.dono,a.custcode,a.custname,a.ordertype,sum(b.qtyor) qtyor,isnull(sum(b.qtyis),0) as qtyissue from "
					+ "["
					+ plant
					+ "_"
					+ "dohdr] a,"
					+ "["
					+ plant
					+ "_"
					+ "dodet] b"
					
					+"  where a.dono=b.dono  " + sCondition ;
				StringBuffer sql_DO = new StringBuffer(aQuery);
				
			    if (ht.size() > 0) {
						
						if (ht.get("A.DONO") != null) {
							sql_DO.append(" AND A.DONO = '" + ht.get("A.DONO") + "'");
						}
																		
						if (ht.get("A.CUSTNAME") != null) {
							sql_DO.append(" AND A.CUSTNAME LIKE '%" + ht.get("A.CUSTNAME") + "%'");
						}
						
						if (ht.get("A.ORDERTYPE") != null) {
							sql_DO.append(" AND A.ORDERTYPE = '" + ht.get("A.ORDERTYPE")
									+ "'");
						}
						
						if (ht.get("B.ITEM") != null) {
							sql_DO.append(" AND B.ITEM = '" + ht.get("B.ITEM") + "'");
						}
						
							if (ht.get("B.LNSTAT") != null) {
							if((ht.get("B.LNSTAT").equals("N/O")))
							{
								sql_DO.append(" AND B.LNSTAT IN('N','O')");
							}
							else
							{
							    sql_DO.append(" AND B.LNSTAT = '" + ht.get("B.LNSTAT") + "'");
							}
						}
						
							
					}
					arrList = movHisDAO.selectMobileSalesForReport(sql_DO.toString(),extraCond);
			    	if (arrList.size() > 0) {
//						    String trantype="";
							xmlStr += XMLUtils.getXMLHeader();
							xmlStr += XMLUtils.getStartNode("OrderDetail total='"+ String.valueOf(arrList.size()) + "'");
							for (int i = 0; i < arrList.size(); i++) {
								Map map = (Map) arrList.get(i);
							    customerstatusid  = customerBeanDAO.getCustomerStatusId(plant,(String) map.get("custname"));
								if(customerstatusid == null || customerstatusid.equals(""))
								{
								   customerstatusdesc="";
								}
								else
								{
								  customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(plant,customerstatusid);
								}
 								customertypeid = customerBeanDAO.getCustomerTypeId(plant,(String)map.get("custname"));
		              			 if(customertypeid == null || customertypeid.equals(""))
		              			 {
		              				customertypedesc="";
		              			 }
		              			 else
		              			 {
		              				customertypedesc = customerBeanDAO.getCustomerTypeDesc(plant,customertypeid);
		              			 }
		              			 
								xmlStr += XMLUtils.getStartNode("record");
								xmlStr += XMLUtils.getXMLNode("orderno", (String) map.get("dono"));
								xmlStr += XMLUtils.getXMLNode("custcode",  (String) map.get("custcode"));
								xmlStr += XMLUtils.getXMLNode("custname",  StrUtils.replaceCharacters2SendPDA((String) map.get("custname")));
								xmlStr += XMLUtils.getXMLNode("orderqty",  (String) map.get("qtyor"));
								xmlStr += XMLUtils.getXMLNode("issueqty",  (String) map.get("qtyissue"));
								xmlStr += XMLUtils.getXMLNode("custstatus",  StrUtils.replaceCharacters2SendPDA((String)customerstatusdesc));
								xmlStr += XMLUtils.getXMLNode("custtype",  StrUtils.replaceCharacters2SendPDA((String)customertypedesc));
								xmlStr += XMLUtils.getEndNode("record");
							}
							
							xmlStr += XMLUtils.getEndNode("OrderDetail");
						}
			
						
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsIssuePrintingListDO:", e);
		}
		return xmlStr;
	
	}

	/*---Added by Bruhan on July 8 2014, Description:To get PDA Mobile Sales Summary by Product
	 *******   Modification History
	 * Bruhan,July 31 2014,Description: To include totalinvoice 
	 * 
	 * 
	 */ 
 public String get_pda_mobilesales_summary_by_product(Hashtable ht, 
    		String afrmDate,String atoDate,String itemDesc,String plant,String userid) {
		ArrayList arrList = new ArrayList();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		String sCondition = "";
		String xmlStr = "";//,fdate="",tdate="";
		 String extraCond="";
		 String aQuery="";

		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			 if (itemDesc.length() > 0 ) {
			        if (itemDesc.indexOf("%") != -1) {
			        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
			        }
		            sCondition = " and replace(B.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		      }
			
	      		
			 String dtCondStr =    " and ISNULL(CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
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
             if (ht.get("A.DONO") == null) {
			  extraCond	=" group by b.item,b.itemdesc,b.unitprice order by b.item ";
				
			  aQuery = "select  b.item,isnull(b.itemdesc,'') itemdesc,sum(b.qtyor) qtyor,isnull(sum(b.qtyis),0) as qtyissue,isnull(b.unitprice,0) as unitprice,ISNULL(sum(b.qtyis*b.unitprice),0) total,ISNULL(sum(b.qtyor*b.unitprice),0) totalinvoice from "
					+ "["
					+ plant
					+ "_"
					+ "dohdr] a,"
					+ "["
					+ plant
					+ "_"
					+ "dodet] b"
					
					+"  where a.dono=b.dono  " + sCondition ;
             }
             if (ht.get("A.DONO") != null) {
   			  extraCond	=" group by b.dolnno,b.item,b.itemdesc,b.unitprice order by b.dolnno ";
   				
   			  aQuery = "select  b.dolnno,b.item,isnull(b.itemdesc,'') itemdesc,sum(b.qtyor) qtyor,isnull(sum(b.qtyis),0) as qtyissue,isnull(b.unitprice,0) as unitprice,ISNULL(sum(b.qtyis*b.unitprice),0) total,ISNULL(sum(b.qtyor*b.unitprice),0) totalinvoice from "
   					+ "["
   					+ plant
   					+ "_"
   					+ "dohdr] a,"
   					+ "["
   					+ plant
   					+ "_"
   					+ "dodet] b"
   					
   					+"  where a.dono=b.dono  " + sCondition ;
                }
             
				StringBuffer sql_DO = new StringBuffer(aQuery);
				
			    if (ht.size() > 0) {
						
						if (ht.get("A.DONO") != null) {
							sql_DO.append(" AND A.DONO  = '" + ht.get("A.DONO") + "'");
						}
																		
						if (ht.get("A.CUSTNAME") != null) {
							sql_DO.append(" AND A.CUSTNAME LIKE '%" + ht.get("A.CUSTNAME") + "%'");
						}
						
						if (ht.get("A.ORDERTYPE") != null) {
							sql_DO.append(" AND A.ORDERTYPE = '" + ht.get("A.ORDERTYPE")
									+ "'");
						}
						
						if (ht.get("B.ITEM") != null) {
							sql_DO.append(" AND B.ITEM = '" + ht.get("B.ITEM") + "'");
						}
						
											
						if (ht.get("B.LNSTAT") != null) {
							if((ht.get("B.LNSTAT").equals("N/O")))
							{
								sql_DO.append(" AND B.LNSTAT IN('N','O')");
							}
							else
							{
							    sql_DO.append(" AND B.LNSTAT = '" + ht.get("B.LNSTAT") + "'");
							}
						}
							
					}
					//arrList = movHisDAO.selectForReport(sql_DO.toString(),ht,extraCond);
                   arrList = movHisDAO.selectMobileSalesForReport(sql_DO.toString(),extraCond);
					if (arrList.size() > 0) {
//						    String trantype="";
							xmlStr += XMLUtils.getXMLHeader();
							xmlStr += XMLUtils.getStartNode("OrderDetail total='"+ String.valueOf(arrList.size()) + "'");
							for (int i = 0; i < arrList.size(); i++) {
								Map map = (Map) arrList.get(i);
								String listprice= StrUtils.fString(itemMstDAO.getItemPrice(plant,(String) map.get("item")));
								//String listprice = new DOUtil().getConvertedUnitCostForProduct(plant,(String) map.get("dono"),(String) map.get("item"));  
								String itemDetailDesc=StrUtils.fString(itemMstDAO.getItemDetailDesc(plant,(String) map.get("item")));
								xmlStr += XMLUtils.getStartNode("record");
								xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
								xmlStr += XMLUtils.getXMLNode("itemdesc",  (String) StrUtils.replaceCharacters2SendPDA( map.get("itemdesc").toString()));
								xmlStr += XMLUtils.getXMLNode("orderqty",  (String) map.get("qtyor"));
								xmlStr += XMLUtils.getXMLNode("issueqty",  (String) map.get("qtyissue"));
								xmlStr += XMLUtils.getXMLNode("unitprice",  (String) map.get("unitprice"));
								xmlStr += XMLUtils.getXMLNode("total",  (String) map.get("total"));
								xmlStr += XMLUtils.getXMLNode("totalinvoice",  (String) map.get("totalinvoice"));
								xmlStr += XMLUtils.getXMLNode("itemDetailDesc",  (String) StrUtils.replaceCharacters2SendPDA(itemDetailDesc));
								xmlStr += XMLUtils.getXMLNode("listprice",   StrUtils.currencyWtoutSymbol(listprice));
								xmlStr += XMLUtils.getEndNode("record");
							}
							
							xmlStr += XMLUtils.getEndNode("OrderDetail");
						}
			
		
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsIssuePrintingListDO:", e);
		}
		return xmlStr;
	
	}
    
	/*---Added by Bruhan on July 14 2014, Description:To get PDA Mobile Sales Outbound order details
	 *******   Modification History
	 * 
	 * 
	 * 
	 */ 
    public String getPDAMobileSales_OutboundOrder_details(String plant,String custname,String dono,String userid) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		String customerstatusid="",customerstatusdesc="";
		ArrayList al = null;

		String query = " dono,custname,isnull(jobNum,'') jobnum,status ";
		String extCond = " and isnull(status,'') <> 'C' ";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", plant);
		if (custname.length() > 0) {
			ht.put("CUSTNAME", custname);
		}
		if (dono.length() > 0) {
			ht.put("DONO",dono);
		}
		
		try {
			al = dohdrDao.selectPDAMobileSalesDoHdr_details(query, ht, extCond);

		if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					customerstatusid  = customerBeanDAO.getCustomerStatusId(plant,(String) map.get("custname"));
					if(customerstatusid == null || customerstatusid.equals(""))
					{
						 customerstatusdesc="";
					}
					else
					{
						 customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(plant,customerstatusid);
					}
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map.get("dono"));
					xmlStr += XMLUtils.getXMLNode("custname", (String) StrUtils.replaceCharacters2SendPDA(map.get("custname").toString()));
					xmlStr += XMLUtils.getXMLNode("refno", (String) StrUtils.replaceCharacters2SendPDA(map.get("jobnum").toString()));
					xmlStr += XMLUtils.getXMLNode("status", (String) map.get("status"));
					xmlStr += XMLUtils.getXMLNode("custstatus", StrUtils.replaceCharacters2SendPDA(customerstatusdesc));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}
    
    /*---Added by Bruhan on July 18 2014, Description:To get PDA Mobile Sales Outbound order details to header update
	 *******   Modification History
	 * 
	 * 
	 * 
	 */ 
    public String getPDAMobileSales_OutboundOrder_details(String plant,String dono,String userid) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;
		String query = "dono,isnull(outbound_Gst,0) outbound_Gst,isnull(ordertype,'') ,custName, (select isnull(display,'') display from "+"["+plant+"_CURRENCYMST] where currencyid = a.currencyid) currencyid,custCode,jobNum, ordertype,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,isnull(remark1,'') remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') timeslots,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as sTelno,isnull(STATUS_ID,'') as statusid  ";
//		String extCond="";
		Hashtable ht = new Hashtable();
		ht.put("PLANT", plant);
		ht.put("DONO",dono);
		try {
			al = dohdrDao.selectOutGoingDoHdr(query, ht);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
						xmlStr += XMLUtils.getXMLNode("dono", (String) map.get("dono"));
						xmlStr += XMLUtils.getXMLNode("ordertype", (String) map.get("ordertype"));
						xmlStr += XMLUtils.getXMLNode("outbound_Gst", (String) map.get("outbound_Gst"));
						xmlStr += XMLUtils.getXMLNode("currencyid", (String) map.get("currencyid"));
						xmlStr += XMLUtils.getXMLNode("jobNum", (String) StrUtils.replaceCharacters2SendPDA(map.get("jobNum").toString()));
						xmlStr += XMLUtils.getXMLNode("custName", (String) StrUtils.replaceCharacters2SendPDA(map.get("custName").toString()));
						xmlStr += XMLUtils.getXMLNode("custCode", (String) map.get("custCode"));
						xmlStr += XMLUtils.getXMLNode("contactname", (String) StrUtils.replaceCharacters2SendPDA(map.get("contactname").toString()));
						xmlStr += XMLUtils.getXMLNode("contactNum", (String) map.get("contactNum"));
						xmlStr += XMLUtils.getXMLNode("telno", (String) map.get("telno"));
						xmlStr += XMLUtils.getXMLNode("email", (String) map.get("email"));
						xmlStr += XMLUtils.getXMLNode("add1", (String) map.get("add1"));
						xmlStr += XMLUtils.getXMLNode("add2", (String) map.get("add2"));
						xmlStr += XMLUtils.getXMLNode("add3", (String) map.get("add3"));
						xmlStr += XMLUtils.getXMLNode("add4", (String) map.get("add4"));
	    				xmlStr += XMLUtils.getXMLNode("country", (String) map.get("country"));
						xmlStr += XMLUtils.getXMLNode("zip", (String) map.get("zip"));
						xmlStr += XMLUtils.getXMLNode("remarks", (String) map.get("remarks"));
						xmlStr += XMLUtils.getXMLNode("address", (String) map.get("address"));
						xmlStr += XMLUtils.getXMLNode("address2", (String) map.get("address2"));
						xmlStr += XMLUtils.getXMLNode("address3", (String) map.get("address3"));
						xmlStr += XMLUtils.getXMLNode("collectionDate", (String) map.get("collectionDate"));
						xmlStr += XMLUtils.getXMLNode("collectionTime", (String) map.get("collectionTime"));
						xmlStr += XMLUtils.getXMLNode("remark1", (String) map.get("remark1"));
						xmlStr += XMLUtils.getXMLNode("remark2", (String) map.get("remark2"));
						xmlStr += XMLUtils.getXMLNode("statusid", (String) map.get("statusid"));
						xmlStr += XMLUtils.getXMLNode("sCust_Name", (String) map.get("sCust_Name"));
						xmlStr += XMLUtils.getXMLNode("sContact_Name", (String) map.get("sContact_Name"));
						xmlStr += XMLUtils.getXMLNode("sAddr1", (String) map.get("sAddr1"));
						xmlStr += XMLUtils.getXMLNode("sAddr2", (String) map.get("sAddr2"));
						xmlStr += XMLUtils.getXMLNode("sCity", (String) map.get("sCity"));
						xmlStr += XMLUtils.getXMLNode("sCountry", (String) map.get("sCountry"));
						xmlStr += XMLUtils.getXMLNode("sZip", (String) map.get("sZip"));
						xmlStr += XMLUtils.getXMLNode("sTelno", (String) map.get("sTelno"));
				   xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}
    /*---Added by Bruhan on July 15 2014, Description:To get PDA Mobile Sales Outbound order item details
	 *******   Modification History
	 * 
	 * 
	 * 
	 */ 
    public String getPDAMobileSalesOutBoundOrderItemDetails(String Plant, String dono,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList al = null;
		DoDetDAO dodetDao = new DoDetDAO();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		dodetDao.setmLogger(mLogger);

		String query = " DONO, DOLNNO ,ITEM,ITEMDESC,QTYOR,isnull(QTYPICK,0) AS QTYPICK,ISNULL(QTYIS,0) QTYIS,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS,PICKSTATUS,LNSTAT,ISNULL(UNITPRICE,0)*ISNULL(CURRENCYUSEQT,0) AS UNITPRICE,ISNULL(COMMENT1,'') REMARKS  ";
		String extCond = " ";
		if (!isReceived) {
			extCond = extCond + " ";
		}
		extCond = extCond + " plant <> '' ORDER BY DOLNNO ";

		Hashtable ht = new Hashtable();
		ht.put("DONO", dono);
		ht.put("PLANT", Plant);
		//
		try {
			al = dodetDao.selectDoDet(query, ht, extCond);
			//String ConvertedUnitPrice= new DoHdrDAO().getUnitCostBasedOnCurIDSelected(plant, dono,dolnno,item);
			//String price = StrUtils.currencyWtoutSymbol(ConvertedUnitPrice);
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					
							
					Map map = (Map) al.get(i);
					//String listprice= StrUtils.fString(itemMstDAO.getItemPrice(Plant,(String) map.get("ITEM")));
					String listprice = new DOUtil().getConvertedUnitCostForProduct(Plant,(String) map.get("DONO"),(String) map.get("ITEM"));
					listprice = StrUtils.currencyWtoutSymbol(listprice);
					String itemDetailDesc=StrUtils.fString(itemMstDAO.getItemDetailDesc(Plant,(String) map.get("ITEM")));
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map.get("DONO"));
					xmlStr += XMLUtils.getXMLNode("dolnno", (String) map.get("DOLNNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC").toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map.get("QTYPICK")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", "");
					xmlStr += XMLUtils.getXMLNode("qtyissue", StrUtils.formatNum((String) map.get("QTYIS")));
					xmlStr += XMLUtils.getXMLNode("pickstatus", (String) map.get("PICKSTATUS"));
					xmlStr += XMLUtils.getXMLNode("lnstat", (String) map.get("LNSTAT"));
					String ConvertedUnitPrice= new DoHdrDAO().getUnitCostBasedOnCurIDSelected(Plant, dono,(String) map.get("DOLNNO"),(String) map.get("ITEM"));
					String price = StrUtils.currencyWtoutSymbol(ConvertedUnitPrice);
					xmlStr += XMLUtils.getXMLNode("unitprice", price);
					String remarks= new DoDetDAO().getMultiRemarks(Plant, dono,(String) map.get("DOLNNO"),(String) map.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("remarks",  (String) StrUtils.replaceCharacters2SendPDA(remarks));
					xmlStr += XMLUtils.getXMLNode("itemDetailDesc",  (String) StrUtils.replaceCharacters2SendPDA(itemDetailDesc));
					xmlStr += XMLUtils.getXMLNode("listprice",   StrUtils.currencyWtoutSymbol(listprice));
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
		}
		return xmlStr;
	}

    public ArrayList listDODetilstoPrintshiphis(String plant, String aDONO,String afromdate,String atodate)
    		throws Exception {
//    			String q = "";
    			ArrayList al = null;
    			ShipHisDAO shipDao = new ShipHisDAO();
    			shipDao.setmLogger(mLogger);
    			try {
    				al = shipDao.getDoDetailToPrintshiphis(plant, aDONO,afromdate,atodate);
    			
    			} catch (Exception e) {
    				throw e;
    			}
    			
    			return al;
    		} 
    public ArrayList getOBIssueList(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.selectOBIssueList(query, ht, extCond);
		} catch (Exception e) {

			throw e;
		}
		return al;
	} 
 public boolean saveDoMultiRemarks(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			_DoDetDAO.setmLogger(mLogger);
			flag = _DoDetDAO.insertDoMultiRemarks(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
    
    public ArrayList listDoMultiRemarks(String plant, String aDONO,String aDOLNNO)
			throws Exception {
		String q = "";
//		String link = "";
//		String result = "";
//		String where = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("dono", aDONO);
			htCond.put("dolnno", aDOLNNO);
			q = "isnull(remarks,'') remarks";
		   _DoDetDAO.setmLogger(mLogger);
			al = _DoDetDAO.selectDoMultiRemarks(q,htCond," plant <> '' order by id_remarks");
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
   public String getOBDiscountSelectedItem(String aPlant, String doNO,String aItem,String aType) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getOBDiscountSelectedItem(aPlant, doNO,aItem,aType);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
   
   public ArrayList listDODETDetailsforcopyfunc(String plant, String dono)
			throws Exception {
		String q = "";
		ArrayList al = null;

		try {
			
			 Hashtable htCond = new Hashtable();
             htCond.put("PLANT", plant);
             htCond.put("dono", dono);
             q = "dono,dolnno,item,isnull(itemdesc,'') itemdesc,isnull(unitmo,'') uom,isnull(unitprice,0)*isnull(currencyuseqt,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,isnull(comment1,'') as prdRemarks,CAST(isnull(unitprice,0)*isnull(currencyuseqt,0) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as conunitprice";
            
             al = _DoDetDAO.selectDoDet(q, htCond," plant <> '' order by dolnno");

             
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
   
 
public boolean saveWMSDoHdrDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_DoHdrDAO.setmLogger(mLogger);
			flag = _DoHdrDAO.insertDoHdr(ht);
		} catch (Exception e) {
			MLogger.log(-1, "saveDoHdrDetails() ::  Exception ############### "
					+ e.getMessage());
			throw new Exception("outgoing order created already");
		}
		return flag;
	}
 public boolean saveDoDetTempDetails(Hashtable ht) throws Exception {
	boolean flag = false;

	try {
		_DoHdrDAO.setmLogger(mLogger);
		flag = _DoDetDAO.insertDoDetTemp(ht);
	} catch (Exception e) {

		//throw e;
	}
	return flag;
}

public String getPDAMobileSalesOutBoundOrderDoTempDetails(String Plant, String dono,
		Boolean isReceived) {
	String xmlStr = "";
	ArrayList al = null;
	DoDetDAO dodetDao = new DoDetDAO();
	dodetDao.setmLogger(mLogger);

	String query = " DONO,ITEM,ITEMDESC,ISNULL(ITEMDETAILDESC,'') ITEMDETAILDESC,QTYOR,UNITMO,ISNULL(LISTPRICE,0) AS LISTPRICE,ISNULL(UNITPRICE,0) AS UNITPRICE,ISNULL(REMARKS,'') AS REMARKS  ";
	String extCond = " ";
	if (!isReceived) {
		extCond = extCond + " ";
	}
	extCond = extCond + " plant <> '' and ItemDesc <> 'NEWDO' ORDER BY ID_DONO ";

	Hashtable ht = new Hashtable();
	ht.put("DONO", dono);
	ht.put("PLANT", Plant);
	//
	try {
		al = dodetDao. selectDoDetTemp(query, ht, extCond);
		//String ConvertedUnitPrice= new DoHdrDAO().getUnitCostBasedOnCurIDSelected(plant, dono,dolnno,item);
		//String price = StrUtils.currencyWtoutSymbol(ConvertedUnitPrice);
		if (al.size() > 0) {
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("itemDetails total='"
					+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("dono", (String) map.get("DONO"));
		    	xmlStr += XMLUtils.getXMLNode("item", (String) map.get("ITEM"));
				xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC").toString()));
				xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map.get("QTYOR")));
				xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
				xmlStr += XMLUtils.getXMLNode("listprice",(String) map.get("LISTPRICE"));
				xmlStr += XMLUtils.getXMLNode("unitprice",(String) map.get("UNITPRICE"));
				xmlStr += XMLUtils.getXMLNode("remarks", (String) map.get("REMARKS"));
				xmlStr += XMLUtils.getXMLNode("itemDetailDesc", (String) map.get("ITEMDETAILDESC"));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("itemDetails");
		}

	} catch (Exception e) {
	}
	return xmlStr;
}

public String getMobileSalesConvertedUnitCostForProduct(String aPlant, String doNO,String aItem,String currency) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _DoHdrDAO.getMobileSalesUnitCostBasedOnCurIDSelectedForOrder(aPlant, doNO,aItem,currency);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

public ArrayList listOutboundSummaryBymultiple(String plant,String afrmDate,String atoDate,String custname,String dono,String item)
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
	             if(dono.length()>0)
	             {
	            	 htCond.put("b.DONO", dono);
	             }
	             
	    		
	    		if (custname.length()>0){
                 	custname = StrUtils.InsertQuotes(custname);
                 	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                 }
	    		
	    		dtCondStr =    " and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
				extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date),b.dono,dolnno ";
			
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
				String aQuery = "select b.dono,b.dolnno,a.custname,b.item,c.itemdesc,b.lnstat,isnull(b.qtyor,0) as qtyor,isnull(b.qtypick,0) as qtypick,isnull(b.qtyis,0) as qtyis, isnull(b.qtyPick,0) as qtyPick,b.pickstatus,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=UNITMO),1) UOMQTY from "
						+ "["
						+ plant
						+ "_"
						+ "dohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "dodet] b,"
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c where a.dono=b.dono and b.ITEM=c.item and b.lnstat <> 'C' " + sCondition ;

				
				al = movHisDAO.selectForReport(aQuery, htCond,extraCon);
		 
	    	} catch (Exception e) {
	    		this.mLogger.exception(this.printLog, "", e);
	    		throw e;
	    	}

	    	return al;
	}

public String process_Signature(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_Signature(obj);
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
					"Error in Customer Signature Capture:" + obj.get(IConstants.DONO)
							+ " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Order : "
					+ obj.get(IConstants.DONO) + "  Signature Capture Successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in Customer Signature Capture : "
					+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}
private boolean process_Wms_Signature(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsSignatureCapture();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}

public String getSignatureOrderStatus(String Plant, String dono) {
	DoHdrDAO dohdrDao = new DoHdrDAO();
	dohdrDao.setmLogger(mLogger);
	String xmlStr = "";
	ArrayList al = null;

	String query = " isnull(status,'') orderstatus ";
	String extCond = "";
	Hashtable ht = new Hashtable();

	ht.put("PLANT", Plant);
	ht.put("DONO", dono);

	try {
		al = dohdrDao.getSignatureOrderStatus(query, ht, extCond);

		if (al.size() > 0) {
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ItemDetails");
			for (int i = 0; i < al.size(); i++) {

				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("orderstatus", (String) map.get("orderstatus"));

			}
			xmlStr += XMLUtils.getEndNode("ItemDetails");
		}

	} catch (Exception e) {

	}

	return xmlStr;
}

public String getOutboundOrderByItem(String Plant,String Item) {
	DoHdrDAO dohdrDao = new DoHdrDAO();
	dohdrDao.setmLogger(mLogger);
    String xmlStr = "";
    ArrayList al = null;
    String query = " a.dono as dono ,isnull(a.CustName,'') as CustName,isnull(a.Remark1,'')  as Remark1,  isnull(a.CollectionDate,'') as ORDDATE,b.DOLNNO as DOLNNO ,b.ITEM as ITEM, b.ITEMDESC as ITEMDESC ,b.QTYOR as QTYOR,isnull(b.QTYPICK,0) AS QTYPICK,isnull(b.QTYIS,0) AS QTYISSUE,ISNULL(UNITMO,'') UNITMO,ISNULL(b.USERFLD2,'') AS REMARKS,isnull(status,'') STATUS";
    String extCond = " and a.status <> 'C'  and b.LNSTAT <>'C' ORDER BY a.dono desc ";
    Hashtable ht = new Hashtable();

    ht.put("PLANT", Plant);
    ht.put("ITEM", Item);
    //
    try {
            al = dohdrDao.selectDoHdrNewOrdersByItem(query, ht, extCond);

            if (al.size() > 0) {
                    xmlStr += XMLUtils.getXMLHeader();
                    xmlStr += XMLUtils.getStartNode("itemDetails total='"
                                    + String.valueOf(al.size()) + "'");
                    for (int i = 0; i < al.size(); i++) {
                            Map map = (Map) al.get(i);
                            xmlStr += XMLUtils.getStartNode("record");
                            xmlStr += XMLUtils.getXMLNode("dono", (String) map
                                            .get("dono"));

                            xmlStr += XMLUtils.getXMLNode("customer", (String) StrUtils.replaceCharacters2SendPDA(map.get("CustName")
                                                            .toString()));
                       
                        xmlStr += XMLUtils.getXMLNode("orderdate", (String) map.get("ORDDATE"));
                       
                         xmlStr += XMLUtils.getXMLNode("dolnno", (String) map
                                        .get("DOLNNO"));
                          xmlStr += XMLUtils.getXMLNode("item", (String) map
                                        .get("ITEM"));

                        xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
                                                        .toString()));
                        xmlStr += XMLUtils.getXMLNode("qtypick", StrUtils.formatNum((String) map
                                        .get("QTYPICK")));
                        xmlStr += XMLUtils.getXMLNode("qtyissue", StrUtils.formatNum((String) map
                                        .get("QTYISSUE")));
                        xmlStr += XMLUtils.getXMLNode("uom", (String) map
                                        .get("UNITMO"));
                       // xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
                                       //.replaceCharacters2SendPDA(map.get("REMARKS")
                                                     //   .toString()));
                        xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils.replaceCharacters2SendPDA(map.get("Remark1").toString()));
                        xmlStr += XMLUtils.getXMLNode("status", (String) map
                                .get("STATUS"));
                            xmlStr += XMLUtils.getEndNode("record");
                    }
                    xmlStr += XMLUtils.getEndNode("itemDetails");
            }

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    }

    return xmlStr;
}

public String process_DoPickingForPDA_Random_ByPDA(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_DoPicking_Random_ByPDA(obj);
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
public JSONObject process_DoPickingForPDA_Random_ByPDAList(Map obj) {
	boolean flag = false;
	UserTransaction ut = null;
	JSONObject resultJson = new JSONObject();
	try {
		ut = com.track.gates.DbBean.getUserTranaction();
		ut.begin();
		flag = process_Wms_DoPicking_Random_ByPDA(obj);
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
//		String xmlStr = XMLUtils.getXMLMessage(0,
//				"Error in picking Product : " + obj.get(IConstants.ITEM)
//						+ " Order");
		    resultJson.put("message","Error in picking Product : " + obj.get(IConstants.ITEM)
					+ " Order");
            resultJson.put("status","0");
            resultJson.put("LineNo",obj.get("LineNo").toString());
		return resultJson;
	}

//	String xmlStr = "";
	if (flag == true) {
//		xmlStr = XMLUtils.getXMLMessage(1, "Product : "
//				+ obj.get(IConstants.ITEM) + "  picked successfully!");
		resultJson.put("message","Product : "
				+ obj.get(IConstants.ITEM) + "  picked successfully!");
        resultJson.put("status","1");
        resultJson.put("LineNo",obj.get("LineNo").toString());
	} else {
//		xmlStr = XMLUtils.getXMLMessage(0, "Error in picking Product : "
//				+ obj.get(IConstants.ITEM) + " Order");
		 resultJson.put("message","Error in picking Product : "
					+ obj.get(IConstants.ITEM) + " Order");
         resultJson.put("status","0");
         resultJson.put("LineNo",obj.get("LineNo").toString());
	}
	return resultJson;
}
private boolean process_Wms_DoPicking_Random_ByPDA(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsPickingByPDA();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}

public String process_DoIssue_Random_ByPDA(Map obj) {
	boolean flag = false;
	UserTransaction ut = null;
	try {
		ut = com.track.gates.DbBean.getUserTranaction();
		ut.begin();
		flag = process_Wms_DoIssue_Random_ByPDA(obj);
		if (flag == true) {
			DbBean.CommitTran(ut);
			flag = true;
		} else {
			DbBean.RollbackTran(ut);
			flag = false;
		}
	} catch (Exception e) {
		flag = false;
		String xmlStr ="";
		DbBean.RollbackTran(ut);
		if(obj.containsKey("msgflag")){
			String msg =(String) obj.get("msgflag");
			xmlStr	= XMLUtils.getXMLMessage(0,
					msg + " For Product : " + StrUtils.replaceCharacters2Send(obj.get(IConstants.ITEM).toString())
							+ " Order");
		}else {
		xmlStr = XMLUtils.getXMLMessage(0,
				"Error in issuing product : " + obj.get(IConstants.ITEM)
						+ " Order");
		}
		return xmlStr;
	}

	String xmlStr = "";
	if (flag == true) {
		if(obj.containsKey("msgflag")){
			String msg =(String) obj.get("msgflag");
				xmlStr = XMLUtils.getXMLMessage(1, msg + " For Product : "
						+ obj.get(IConstants.ITEM).toString());
		}else {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  issued successfully!");
		}
		
	} else {
		xmlStr = XMLUtils.getXMLMessage(0, "Error in issuing Product : "
				+ obj.get(IConstants.ITEM) + " Order");
	}
	return xmlStr;
}
public JSONObject process_DoIssue_Random_ByPDAList(Map obj) {
	boolean flag = false;
	UserTransaction ut = null;
	JSONObject resultJson = new JSONObject();
	try {
		ut = com.track.gates.DbBean.getUserTranaction();
		ut.begin();
		flag = process_Wms_DoIssue_Random_ByPDA(obj);
		if (flag == true) {
			DbBean.CommitTran(ut);
			flag = true;
		} else {
			DbBean.RollbackTran(ut);
			flag = false;
		}
	} catch (Exception e) {
		flag = false;
//		String xmlStr ="";
		DbBean.RollbackTran(ut);
		if(obj.containsKey("msgflag")){
			String msg =(String) obj.get("msgflag");
//			xmlStr	= XMLUtils.getXMLMessage(0,
//					msg + " For Product : " + StrUtils.replaceCharacters2Send(obj.get(IConstants.ITEM).toString())
//							+ " Order");
			resultJson.put("message",msg + " For Product : " + StrUtils.replaceCharacters2Send(obj.get(IConstants.ITEM).toString())
					+ " Order");
	        resultJson.put("status","0");
	        resultJson.put("LineNo",obj.get("LineNo").toString());
		}else {
//			xmlStr = XMLUtils.getXMLMessage(0,
//					"Error in issuing product : " + obj.get(IConstants.ITEM)
//							+ " Order");
		resultJson.put("message","Error in issuing product : " + obj.get(IConstants.ITEM)
				+ " Order");
        resultJson.put("status","0");
        resultJson.put("LineNo",obj.get("LineNo").toString());
		}
		return resultJson;
	}

//	String xmlStr = "";
	if (flag == true) {
		if(obj.containsKey("msgflag")){
			String msg =(String) obj.get("msgflag");
//				xmlStr = XMLUtils.getXMLMessage(1, msg + " For Product : "
//						+ obj.get(IConstants.ITEM).toString());
				resultJson.put("message",msg + " For Product : "
						+ obj.get(IConstants.ITEM).toString());
		        resultJson.put("status","1");
		        resultJson.put("LineNo",obj.get("LineNo").toString());
		}else {
//			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
//					+ obj.get(IConstants.ITEM) + "  issued successfully!");
			resultJson.put("message", "Product : "
					+ obj.get(IConstants.ITEM) + "  issued successfully!");
	        resultJson.put("status","1");
	        resultJson.put("LineNo",obj.get("LineNo").toString());
		}
		
	} 
//	else {
//		xmlStr = XMLUtils.getXMLMessage(0, "Error in issuing Product : "
//				+ obj.get(IConstants.ITEM) + " Order");
//	}
	return resultJson;
}

public boolean updateMovHis(Map map) throws Exception {
	boolean flag = false;
	MovHisDAO movHisDao = new MovHisDAO();
	CustMstDAO _CustMstDAO = new CustMstDAO();
	movHisDao.setmLogger(mLogger);
	_CustMstDAO.setmLogger(mLogger);
	try {
		Hashtable<String, Object> htMovHis = new Hashtable();
		htMovHis.clear();
		htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htMovHis.put("DIRTYPE", TransactionConstants.EDITDNPL);
		htMovHis.put(IDBConstants.ITEM, "");
		htMovHis.put("BATNO", "");
		//htMovHis.put(IDBConstants.QTY, '0');
		htMovHis.put(IDBConstants.POHDR_JOB_NUM, "");
		htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DNPLDET_DONUM) + "_" + map.get("INVOICENO"));
		htMovHis.put("MOVTID", "");
		htMovHis.put("RECID", "");
		htMovHis.put(IDBConstants.LOC, "");
		htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
		htMovHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htMovHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes((String)map.get(IConstants.REMARKS)));
		
		flag = movHisDao.insertIntoMovHis(htMovHis);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
}


private boolean process_Wms_DoIssue_Random_ByPDA(Map map) throws Exception {
	boolean flag = false;

	WmsTran tran = new WmsIssuingByPDA();
	((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
	flag = tran.processWmsTran(map);
	return flag;
}

	

	/**
	 * Provides list of top issued products in the given period
	 * 
	 * @param plant
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList getTopIssuedProducts(String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			String aQuery = "select ITEM AS PRODUCT, ITEMDESC, SUM(ISNULL(PICKQTY, 0)) as ISSUEDQTY from " + "[" + plant
					+ "_" + "SHIPHIS] where CONVERT(DATETIME, ISSUEDATE, 103) between '" + fromDate + "' and '" + toDate
					+ "' group by ITEM, ITEMDESC order by 3 desc";

			al = _DoDetDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	/**
	 * Provides total issue value in the given period
	 * 
	 * @param plant
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList getTotalIssue(String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			String aQuery = "SELECT SUM(ISNULL(QTYOR, 0) * (ISNULL(UNITPRICE, 0))) as TOTAL_ISSUE,"
					+ "SUM(ISNULL(QTYOR, 0)) as TOTAL_PICKQTY FROM " 
					+ "[" + plant + "_DOHDR] A JOIN [" + plant + "_DODET] B on A.DONO = B.DONO  "
					+ "WHERE CONVERT(DATETIME, CollectionDate, 103) between '" + fromDate + "' and '" + toDate + "'";

			al = _DoDetDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}	
	
	public boolean process_DNPL(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();

			ut.begin();
			//flag = copy_DO_TO_DNPL(obj);
			flag = process_Wms_DNPL(obj);

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

	
	public boolean update_DNPL(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();

			ut.begin();
			flag = updateDNPLHDR(obj);
		    flag = updateMovHis(obj);

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
	
	public boolean updateDNPLHDR(Map ht) throws Exception {
		boolean flag = false;

		Hashtable htCond = new Hashtable();
		htCond.put(IConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put(IConstants.DODET_DONUM, (String) ht
				.get(IConstants.DODET_DONUM));
		htCond.put(IConstants.INVOICENO, (String) ht
				.get(IConstants.INVOICENO));
		try {

			StringBuffer updateQyery = new StringBuffer("set ");

			
			
			updateQyery.append("" + IConstants.DNNO + " = '"
					+ (String) ht.get(IConstants.DNNO) + "'");
			updateQyery.append("," + IConstants.PLNO + " = '"
					+ (String) ht.get(IConstants.PLNO) + "'");
			updateQyery.append("," + IConstants.TOTALNETWEIGHT + " = '"
					+ (String) ht.get(IConstants.TOTALNETWEIGHT) + "'");
			updateQyery.append("," + IConstants.TOTALGROSSWEIGHT + " = '"
					+ (String) ht.get(IConstants.TOTALGROSSWEIGHT) + "'");
			updateQyery.append("," + IConstants.NETPACKING + " = '"
					+ (String) ht.get(IConstants.NETPACKING) + "'");		
			updateQyery.append("," + IConstants.NETDIMENSION + " = '"
					+ (String) ht.get(IConstants.NETDIMENSION) + "'");
			updateQyery.append("," + IConstants.DNPLREMARKS + " = '"
					+ (String) ht.get(IConstants.DNPLREMARKS) + "'");
			PackingDAO  dao = new PackingDAO();
			dao.setmLogger(mLogger);
			flag = dao.updateDNPLHDR(updateQyery.toString(), htCond,"");
			
			if(flag){
				/*htCond.remove(IDBConstants.POS_TRANID);
				htCond.put(IDBConstants.DOHDR_DONUM, (String) ht.get(IDBConstants.POS_TRANID));*/
			 updateQyery = new StringBuffer("set ");
			 updateQyery.append("" + IConstants.PACKING + " = '"
						+ (String) ht.get(IConstants.PACKING) + "'");
			 updateQyery.append("," + IConstants.UPDATED_AT + " = '"
						+ (String) ht.get(IConstants.UPDATED_AT) + "'");
			 updateQyery.append("," + IConstants.UPBY + " = '"
						+ (String) ht.get(IConstants.UPBY) + "'");
			 updateQyery.append("," +IConstants.NETWEIGHT + " = '"
						+ (String) ht.get(IConstants.NETWEIGHT) + "'");
				updateQyery.append("," + IConstants.GROSSWEIGHT + " = '"
						+ (String) ht.get(IConstants.GROSSWEIGHT) + "'");		
				updateQyery.append("," + IConstants.DIMENSION + " = '"
						+ (String) ht.get(IConstants.DIMENSION) + "'");
				
				PackingDAO  packingdao = new PackingDAO();
				packingdao.setmLogger(mLogger);
				flag = packingdao.updateDNPLDET(updateQyery.toString(), htCond,"");
			 
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Processed Goods Issue Cannot be modified");
		}
		return flag;
	}

	public boolean updateDNPLHDR_GINO(Map ht, String Type) throws Exception {
		boolean flag = false;
		Hashtable htCond = new Hashtable();
		try {
			if(Type.equalsIgnoreCase("DNPLHDR"))
					{	
			 htCond = new Hashtable();
		htCond.put(IConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put("ID", Integer.toString((int) ht.get("ID")));
		

			StringBuffer updateQyery = new StringBuffer("set ");			
			
			updateQyery.append(" TOTAlNETWEIGHT = '"
					+ (String) ht.get("TOTAlNETWEIGHT") + "'");
			updateQyery.append(", TOTAlGROSSWEIGHT = '"
					+ (String) ht.get("TOTAlGROSSWEIGHT") + "'");
			updateQyery.append(", TOTAlPACKING = '"
					+ (String) ht.get("TOTAlPACKING") + "'");		
			updateQyery.append(", TOTAlDIMENSION = '"
					+ (String) ht.get("TOTAlDIMENSION") + "'");
			updateQyery.append(", NOTE = '"
					+ (String) ht.get("NOTE") + "'");
			updateQyery.append("," + IConstants.UPDATED_BY + " = '"
					+ (String) ht.get(IConstants.UPDATED_BY) + "'");
			updateQyery.append("," + IConstants.UPDATED_AT + " = '"
					+ (String) ht.get(IConstants.UPDATED_AT) + "'");
			PackingDAO  dao = new PackingDAO();
			dao.setmLogger(mLogger);
			flag = dao.updateDNPLHDR(updateQyery.toString(), htCond,"");
					
					}
					else{
						
						htCond = new Hashtable();
						htCond.put(IConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
						htCond.put("HDRID", (String) ht.get("HDRID"));
						htCond.put("LNNO", (String) ht.get("LNNO"));
						
						StringBuffer updateQyery = new StringBuffer("set ");
						 
			 updateQyery.append("" + IConstants.PACKING + " = '"
						+ (String) ht.get(IConstants.PACKING) + "'");
			 updateQyery.append("," +IConstants.NETWEIGHT + " = '"
						+ (String) ht.get(IConstants.NETWEIGHT) + "'");
				updateQyery.append("," + IConstants.GROSSWEIGHT + " = '"
						+ (String) ht.get(IConstants.GROSSWEIGHT) + "'");		
				updateQyery.append("," + IConstants.DIMENSION + " = '"
						+ (String) ht.get(IConstants.DIMENSION) + "'");
				updateQyery.append("," + IConstants.UPDATED_BY + " = '"
						+ (String) ht.get(IConstants.UPDATED_BY) + "'");
				updateQyery.append("," + IConstants.UPDATED_AT + " = '"
						+ (String) ht.get(IConstants.UPDATED_AT) + "'");
				PackingDAO  packingdao = new PackingDAO();
				packingdao.setmLogger(mLogger);
				flag = packingdao.updateDNPLDET(updateQyery.toString(), htCond,"");
			 
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Processed Goods Issue Cannot be modified");
		}
		return flag;
	}
	
	
	
	public boolean updateDNPLHDR_STATUS(Hashtable htCond, String extCond) throws Exception {
		boolean flag = false;

		
		try {

			StringBuffer updateQyery = new StringBuffer("set ");

			updateQyery.append(extCond);
			PackingDAO  dao = new PackingDAO();
			dao.setmLogger(mLogger);
			flag = dao.updateDNPLHDR(updateQyery.toString(), htCond,"");
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Processed Goods Issue Cannot be modified");
		}
		return flag;
	}
	
//	private boolean copy_DO_TO_DNPL(Map map) throws Exception {
//		boolean flag = false;
//		try {
//			String DONO = (String) map.get(IConstants.DODET_DONUM); 
//			Hashtable ht = new Hashtable();
//			ht.put(IConstants.PLANT, map.get(IConstants.PLANT));
//			ht.put(IConstants.DOHDR_DONUM, map.get(IConstants.DOHDR_DONUM));
//			Map mDoHdr = _DoHdrDAO.selectRow("SHIPPINGCUSTOMER,Address,SHIPMOD,RECSTAT,contactNum,USERFLG1,STATUS,PAYMENT_STATUS,USERFLG5,LBLCAT,UPBY,USERFLG4,USERFLG3,CURRENCYID,USERFLG2,CRBY,EMPNO,SHIPVIA,SHIPPINGCOST,LBLTYPE,PRIORTY,TERMS,UPAT,USERFLG6,DONO,DELDATE,SHIPPINGID,TIMESLOTS,DNPLREMARKS,JobNum,PersonInCharge,STATUS_ID,POTYPE,PLANT,POSTAT,Remark2,ESTNO,Remark3,ORDERDISCOUNT,Remark1,PickStaus,ORDTRK2,INCOTERMS,ORDTRK1,USERDBL1,USERDBL2,CustCode,Address2,Address3,DELIVERYDATE,TOTQTY,OUTBOUND_GST,COMMENT2,COMMENT1,CustName,USERDBL5,USERDBL6,LBLGROUP,USERDBL3,USERDBL4,TOTWGT,CRAT ", ht, "");
//			mDoHdr.put("PLANT", map.get(IConstants.PLANT));
//			mDoHdr.put("DONO", map.get(IConstants.DODET_DONUM));
//			mDoHdr.put(IConstants.DOHDR_COL_DATE, map.get(IConstants.DOHDR_COL_DATE));
//			mDoHdr.put(IConstants.DOHDR_COL_TIME, map.get(IConstants.DOHDR_COL_TIME));
//			if(DONO.substring(0, 1).equalsIgnoreCase("T"))
//			{			
//			mDoHdr.put(IConstants.DOHDR_CUST_CODE, map.get(IConstants.DOHDR_CUST_CODE));
//			mDoHdr.put(IConstants.DOHDR_CUST_NAME, map.get(IConstants.DOHDR_CUST_NAME));
//			}
//			mDoHdr.put("DNNO", map.get("DNNO"));
//			mDoHdr.put("PLNO", map.get("PLNO"));
//			mDoHdr.put("TOTALGROSSWEIGHT", map.get("TOTALGROSSWEIGHT"));
//			mDoHdr.put("TOTALNETWEIGHT", map.get("TOTALNETWEIGHT"));
//			mDoHdr.put("NETDIMENSION", map.get("NETDIMENSION"));
//			mDoHdr.put("NETPACKING", map.get("NETPACKING"));
//			mDoHdr.put("INVOICENO", map.get("INVOICENO"));
//			mDoHdr.put("DNPLREMARKS", map.get("DNPLREMARKS"));
//			try {
//				_DNPLHdrDAO.insertDNPLHDR(mDoHdr);
//			}catch(Exception e) {
//				this.mLogger.exception(this.printLog, "", e);
//				//	Ignore there may be a Primary Key error
//			}
//			ht = new Hashtable();
//			ht.put("A." + IConstants.PLANT, map.get(IConstants.PLANT));
//			ht.put("A." + IConstants.DOHDR_DONUM, map.get(IConstants.DOHDR_DONUM));
//			
//			ht.put("A.INVOICENO", map.get("INVOICENO"));
//			POSHdrDAO  _POSHdrDAO = new POSHdrDAO();
//			ArrayList alDoDet = new ArrayList();
//			if(DONO.substring(0, 1).equalsIgnoreCase("T"))
//			{
//				ht.put("DOLNO", map.get(IConstants.DODET_DOLNNO));
//				String qurey="PLANT,DONO, DOLNO as DOLNNO,STATUS as PickStatus,STATUS as LNSTAT,ITEM,ItemDesc,ISSUEDATE as TRANDATE,UNITPRICE,ORDQTY as QTYOR,ORDQTY as QTYIS,PICKQTY as QtyPick,"
//				 +"LOC,ISNULL((select top 1 STKUOM from "+"["+ ht.get("PLANT")+"_ITEMMST] where ITEM=A.ITEM and  ITEMDESC=A.ITEMDESC),'') as UNITMO,ItemDesc as USERFLD1,CNAME as USERFLD3,"
//				+"ISNULL((select top 1 CURRENCYUSEQT from "+"["+ ht.get("PLANT")+"_CURRENCYMST] where CURRENCYID=A.CURRENCYID),0) as CURRENCYUSEQT,0 as PRODGST,0 as NETWEIGHT,0 as GROSSWEIGHT,0 as USERDBL1,0 as USERDBL2,0 as USERDBL3,0 as USERDBL4,0 as USERDBL5,0 as USERDBL6";
//				alDoDet = _POSHdrDAO.selectdnplPosDet(qurey, ht, "A.Status='C'");			
//				}
//			else
//			{
//				ht.put(IConstants.DODET_DOLNNO, map.get(IConstants.DODET_DOLNNO));
//				alDoDet = _DoDetDAO.selectdnplDoDet("distinct B.*", ht, "A.Status='C'");
//			}
//			Iterator iterDoDet = alDoDet.iterator();
//			while(iterDoDet.hasNext()) {
//				Map mDoDet = (Map)iterDoDet.next();
//				mDoDet.put("SRNO", map.get("SRNO"));
//				mDoDet.put("INVOICENO", map.get("INVOICENO"));
//				_DNPLDetDAO.insertDNPLDET(mDoDet);
//			}
//			flag = true;
//		} catch (Exception e) {
//			this.mLogger.exception(this.printLog, "", e);
//			throw e;
//		}
//		return flag;
//	}
	private boolean process_Wms_DNPL(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsDNPL();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
	public String getConvertedUnitCostForProductWTC(String aPlant, String doNO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTC(aPlant, doNO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	public ArrayList getDODetDetailsRandomMultiUOM(String Plant,
				String DONO,String item,String uom) {
	    	ArrayList al = null;

			try {
				al = _DoDetDAO.getDODetDetailsRandomMultiUOM(Plant, DONO, item,uom);
				 
			} catch(Exception e ){
	    		this.mLogger.exception(this.printLog, "", e);
	    	}
			
	    return al;
	    }
	public String getDoUomWithQty(String plant1,String user,String dono,String item) {

		String xmlStr = "";
		ArrayList al = null;
		DoDetDAO dao = new DoDetDAO();
		dao.setmLogger(mLogger);

		try {
			al = dao.getDoUOMByOrder(plant1,user,dono,item);

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
	public String getComittedStockByProduct(String aItem, String plant) throws Exception {
		String comittedStock = "";		
		DoDetDAO dao = new DoDetDAO();
		try {
			dao.setmLogger(mLogger);
			comittedStock = dao.getComittedStockByProduct(aItem, plant);
			if(comittedStock.equalsIgnoreCase(""))
				comittedStock="0.000";
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return comittedStock;
	}
	
	public String getQtyToBeShpdByProduct(String aItem, String plant) throws Exception {
		String qtyToBeShpd = "";		
		DoDetDAO dao = new DoDetDAO();
		try {
			dao.setmLogger(mLogger);
			qtyToBeShpd = dao.getQtyToBeShpdByProduct(aItem, plant);
			if(qtyToBeShpd.equalsIgnoreCase(""))
				qtyToBeShpd="0.000";
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return qtyToBeShpd;
	}
	
	public ArrayList getTotalIssueByProduct(String item, String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		DoDetDAO dao = new DoDetDAO();
		try {
			dao.setmLogger(mLogger);
			al = _DoDetDAO.getTotalIssueByProduct(item, plant, fromDate, toDate);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	public ArrayList getTotalIssueQtyByProduct(String item, String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		DoDetDAO dao = new DoDetDAO();
		try {
			dao.setmLogger(mLogger);
			al = _DoDetDAO.getTotalIssueQtyByProduct(item, plant, fromDate, toDate);
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
			_DoHdrDAO.setmLogger(mLogger);
			ordersList = _DoHdrDAO.getOrderDetailsForInvoice(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return ordersList;
	}	
	
	public List getPreviousOrderDetails(Hashtable ht, String rows) throws Exception {
		List ordersList = new ArrayList();
		try {
			_DoHdrDAO.setmLogger(mLogger);
			ordersList = _DoHdrDAO.getPreviousOrderDetails(ht, rows);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return ordersList;
	}
	
	public String getOrderDateOnly(String aPlant, String aPONO) throws Exception {
		String OrderDate = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aPONO);

		String query = " ISNULL( " + "collectionDate" + ",'') as " + "date" + " ";
		_DoHdrDAO.setmLogger(mLogger);
		Map m = _DoHdrDAO.selectRow(query, ht);

		OrderDate = (String) m.get("date");
		return OrderDate;
	}
	
	public String getUnitCostForProduct(String aPlant, String doNO,String aItem,String DO_LN_NUM) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getUnitCostSelectedForOrder(aPlant, doNO,aItem,DO_LN_NUM);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public String getUnitCostForProductWithoutlineno(String aPlant, String doNO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getUnitCostSelectedForOrderWithoutLineno(aPlant, doNO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	public String getCustName(String aPlant, String aPONO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("DONO", aPONO);

		String query = " ISNULL( " + "CustName" + ",'') as " + "CustName" + " ";
		_DoHdrDAO.setmLogger(mLogger);
		Map m = _DoHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("CustName");

		return custCode;
	}
	
	public ArrayList getSalesSummary(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		String aQuery = "";
		try {
			if(period.equalsIgnoreCase("Last 30 days")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select CONVERT(DATE,getdate()) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This Month")) {
				aQuery ="with cte as" + 
				" (" + 
				" select EOMONTH(GETDATE()) as   n" + 
				" union all" + 
				" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
				" )";
			} else if(period.equalsIgnoreCase("This quarter")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select getdate() as   n, datename(month,getdate()) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(dd,-1,n)> DATEADD(month, -2, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last month")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select EOMONTH(GETDATE(),-1) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last quarter")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select dateadd(MONTH,-3,getdate()) as   n, datename(month,dateadd(MONTH,-3,getdate()) ) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(MONTH,-1,n)>= DATEADD(month,-5,getdate())" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}
			
			if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
				aQuery += " SELECT CONVERT(nvarchar, n, 107) SOLD_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
						" SELECT CONVERT(DATE, A.CollectionDate, 103) AS CDATE,SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+")))" + 
						" AS TOTAL_PRICE FROM   ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO  " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY CONVERT(DATE, A.CollectionDate, 103) )" + 
						" b on a.n = b.CDATE order by n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) SOLD_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
						" SELECT datename(month, (CONVERT(DATE, A.CollectionDate, 103))) AS CDATE,SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+")))" + 
						" AS TOTAL_PRICE FROM   ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY datename(month, (CONVERT(DATE, A.CollectionDate, 103))) )" + 
						" b on a.m = b.CDATE order by n";
			}
			al = _DoHdrDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getSalesOrderDeliveryDate(String plant, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.getSalesOrderDeliveryDate(plant,numberOfDecimal);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getTopSalesItem(String plant, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.getTopSalesItem(plant,fromDate,toDate,numberOfDecimal);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getTopSalesItemDetail(String plant, String period, String fromDate, String toDate, String item, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		String aQuery = "";
		try {
			if(period.equalsIgnoreCase("Last 30 days")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select CONVERT(DATE,getdate()) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This Month")) {
				aQuery ="with cte as" + 
				" (" + 
				" select EOMONTH(GETDATE()) as   n" + 
				" union all" + 
				" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
				" )";
			} else if(period.equalsIgnoreCase("This quarter")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select getdate() as   n, datename(month,getdate()) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(dd,-1,n)> DATEADD(month, -2, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last month")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select EOMONTH(GETDATE(),-1) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last quarter")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select dateadd(MONTH,-3,getdate()) as   n, datename(month,dateadd(MONTH,-3,getdate()) ) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(MONTH,-1,n)>= DATEADD(month,-5,getdate())" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}
			
			if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
				aQuery += " SELECT CONVERT(nvarchar, n, 107) SOLD_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
						" SELECT CONVERT(DATE, A.CollectionDate, 103) AS CDATE,SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+")))" + 
						" AS TOTAL_PRICE FROM   ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO  " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"' AND ITEM='"+item+"' " + 
						" GROUP BY CONVERT(DATE, A.CollectionDate, 103) )" + 
						" b on a.n = b.CDATE order by n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) SOLD_DATE,ISNULL(TOTAL_PRICE,0) as TOTAL_PRICE from cte a left join (" + 
						" SELECT datename(month, (CONVERT(DATE, A.CollectionDate, 103))) AS CDATE,SUM(CAST((UNITPRICE * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITPRICE*OUTBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+")))" + 
						" AS TOTAL_PRICE FROM   ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"' AND ITEM='"+item+"' " + 
						" GROUP BY datename(month, (CONVERT(DATE, A.CollectionDate, 103))) )" + 
						" b on a.m = b.CDATE order by n";
			}
			al = _DoHdrDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getGriSummaryDashboard(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		String aQuery = "";
		try {
			if(period.equalsIgnoreCase("Last 30 days")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select CONVERT(DATE,getdate()) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This Month")) {
				aQuery ="with cte as" + 
				" (" + 
				" select EOMONTH(GETDATE()) as   n" + 
				" union all" + 
				" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
				" )";
			} else if(period.equalsIgnoreCase("This quarter")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select getdate() as   n, datename(month,getdate()) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(dd,-1,n)> DATEADD(month, -2, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last month")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select EOMONTH(GETDATE(),-1) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last quarter")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select dateadd(MONTH,-3,getdate()) as   n, datename(month,dateadd(MONTH,-3,getdate()) ) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(MONTH,-1,n)>= DATEADD(month,-5,getdate())" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}
			
			if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
				aQuery += " SELECT CONVERT(nvarchar, n, 107) ISSUEDATE,CAST(ISNULL(TOTAL_ISSUE_QTY,0) AS DECIMAL(18,3)) as TOTAL_ISSUE_QTY from cte a left join (" + 
						" SELECT CONVERT(DATE, ISSUEDATE, 103) AS ISSUEDATE, SUM(B.QTY) TOTAL_ISSUE_QTY " + 
						" FROM   ["+plant+"_SHIPHIS] A JOIN ["+plant+"_FINGINOTOINVOICE] B ON A.INVOICENO = B.GINO  " + 
						" WHERE CONVERT(DATETIME, ISSUEDATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY CONVERT(DATE, ISSUEDATE, 103) )" + 
						" b on a.n = b.ISSUEDATE order by n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) ISSUEDATE,CAST(ISNULL(TOTAL_ISSUE_QTY,0) AS DECIMAL(18,3)) as TOTAL_ISSUE_QTY from cte a left join (" + 
						" SELECT datename(month, (CONVERT(DATE, ISSUEDATE, 103))) AS ISSUEDATE, SUM(B.QTY) TOTAL_ISSUE_QTY " + 
						" FROM   ["+plant+"_SHIPHIS] A JOIN ["+plant+"_FINGINOTOINVOICE] B ON A.INVOICENO = B.GINO " + 
						" WHERE CONVERT(DATETIME, ISSUEDATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY datename(month, (CONVERT(DATE, ISSUEDATE, 103))) )" + 
						" b on a.m = b.ISSUEDATE order by n";
			}
			al = _DoHdrDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getSalesOrderExpiredDeliveryDate(String plant, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.getSalesOrderExpiredDeliveryDate(plant,numberOfDecimal);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getReadyToPackOrders(String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			String aQuery = "SELECT * FROM ("
					+"SELECT A.DONO, A.CustName, B.DOLNNO, B.UNITMO, A.CollectionDate, B.QTYOR, "
					+"(SUM(B.QTYOR)-  - (SELECT SUM(QTYOR) FROM ["+plant+"_DODET] WHERE ITEM=B.ITEM AND ISNULL(QTYOR,0) <>ISNULL(QTYIS,0) AND LNSTAT <> 'C')) AS AVL_QTY FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B "
					+ "ON A.DONO = B.DONO JOIN ["+plant+"_INVMST] C ON B.ITEM = C.ITEM"
					+" WHERE DELIVERYDATEFORMAT = '1' AND CONVERT(DATETIME, DELIVERYDATE, 103) between '" + fromDate + "' and '" + toDate + "' "
					+"AND C.LOC NOT LIKE 'SHIPPINGAREA%' AND C.LOC NOT LIKE 'TEMP_TO%' "
					+"GROUP BY A.DONO, A.CustName, B.DOLNNO, B.UNITMO, A.CollectionDate, B.QTYOR, B.ITEM "
					+") A WHERE AVL_QTY >= QTYOR ORDER BY DONO";
			al = _DoHdrDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	
	public ArrayList dnplGINO(String plant, String aDONO, String aGINO, String invoiceNo)
		      throws Exception {
		     String q = "";
//		     String link = "";
//		     String result = "";
//		     String where = "";
		     ArrayList al = null;

		     try {
		      Hashtable htCond = new Hashtable();
		      htCond.put("A.PLANT", plant);
		      htCond.put("A.dono", aDONO);
		      htCond.put("INVOICENO", aGINO);
		      q = "distinct A.dono,dolnno,lnstat,a.item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,a.loc as loc,a.userfld4 as batch,a.userfld3 as custname,"
		         +" (select itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as Itemdesc,"
		         + " isnull(B.UNITMO,'') as uom,0 as HID,"
		         +"CASE when NETWEIGHT <>0 THEN NETWEIGHT ELSE (SELECT ISNULL(NETWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) END netweight,"
		          +"CASE when GROSSWEIGHT <>0 THEN GROSSWEIGHT ELSE (SELECT ISNULL(GROSSWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) END grossweight,"
		         +" (select isnull(hscode,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as hscode,"
		         +" (select isnull(DIMENSION,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as dimension,"
		         +" (select isnull(coo,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as coo,"
		         +" (isnull((select top 1 isnull(INVOICE,'') from ["+plant+"_FININVOICEHDR] where PLANT =a.PLANT and GINO =a.INVOICENO),'')) as INVOICE,"
		         + "isnull(PACKING,'')packing,'' as PLNO,''as DNNO,'' as TOTALNETWEIGHT,'' as TOTALGROSSWEIGHT,'' as NETPACKING,'' as NETDIMENSION,'' as DNPLREMARKS,ISSUEDATE as ISSUEDATE";
		      
		      //removed dimension from existing and added dimension from itemmst table by imthi
//		      + "isnull(PACKING,'')packing,isnull(DIMENSION,'') dimension,'' as PLNO,''as DNNO,'' as TOTALNETWEIGHT,'' as TOTALGROSSWEIGHT,'' as NETPACKING,'' as NETDIMENSION,'' as DNPLREMARKS";
		      _DoDetDAO.setmLogger(mLogger);
		      al = _DoDetDAO
		        .selectdnplDoDet(
		          q,
		          htCond,
		          " a.plant <> ''  and lnstat in ('O', 'C') and A.ITEM not in (SELECT ITEM FROM "+plant+"_DNPLDET D JOIN "+plant+"_DNPLHDR H on D.HDRID=H.ID WHERE D.ITEM=A.ITEM AND D.LNNO=A.DOLNO AND H.DONO=A.DONO AND H.GINO=A.INVOICENO)  order by dolnno");

		     } catch (Exception e) {
		      throw e;
		     }
		     return al;
		  }
	
	public String getConvertedUnitCostForProductByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderByCurrency(aPlant, currencyID,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public String getConvertedUnitCostForProductWTCByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTCByCurrency(aPlant, currencyID,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public String getminSellingConvertedUnitCostForProductByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getminSellingUnitCostBasedOnCurIDSelectedForOrderByCurrency(aPlant, currencyID,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public String getOBDiscountSelectedItemByCustomer(String aPlant, String custcode,String aItem,String aType) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _DoHdrDAO.getOBDiscountSelectedItemByCustomer(aPlant, custcode,aItem,aType);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public ArrayList getOrderNoForOrderIssue(Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();
		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.getOrderNoForOrderIssue(ht);
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
	
	//Author: Azees  Create date: August 08,2021  Description: Show only order no.
	public ArrayList getOrderNoForOrderReverse(Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();
		try {
			_DoHdrDAO.setmLogger(mLogger);
			al = _DoHdrDAO.getOrderNoForOrderReverse(ht);
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
	
	//created by vicky Used for PDA Sales order popup with pagenation
	public String getPDApopupOutBoundOrder(String Plant,String start,String end,String dono) {
		DoHdrDAO dohdrDao = new DoHdrDAO();
		dohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		
		String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY a.dono desc)) AS ID,ISNULL((SELECT COUNT(*)  FROM ["+Plant+"_DOHDR] as a, ["+Plant+"_DODET] as b  WHERE b.DONO LIKE '%' AND b.DONO = a.DONO AND b.PLANT = a.PLANT and a.DONO LIKE '%"+dono+"%'),0) TOTPO , a.dono as dono ,isnull(a.CustName,'') as CustName,isnull(a.collectiondate,'') orderdate,isnull(a.status,'') status,isnull(a.Remark1,'') as Remark1  FROM ["+Plant+"_DOHDR] as a, ["+Plant+"_DODET] as b where  a.dono = b.dono AND a.PLANT = b.PLANT and a.status <> 'C' and ORDER_STATUS <>'Draft' group by a.dono, a.CustName, a.collectiondate,a.status,a.Remark1)SELECT * FROM S where ID >='"+start+"' and ID<='"+end+"' order by CAST((SUBSTRING(orderdate, 7, 4) + SUBSTRING(orderdate, 4, 2) + SUBSTRING(orderdate, 1, 2)) AS date )desc";
		String extCond ="";
		Hashtable ht = new Hashtable();

		try {
			al = dohdrDao.selectItemForPDA(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("dono", (String) map
							.get("dono"));
					xmlStr += XMLUtils.getXMLNode("customer", (String) StrUtils.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("orderdate", (String) map
							.get("orddate"));
					xmlStr += XMLUtils.getXMLNode("status", (String) map
							.get("status"));
					xmlStr += XMLUtils.getXMLNode("count", (String) map
							.get("TOTPO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {

		}

		return xmlStr;
	}
	//created by vicky Used for PDA sales order AutoSuggestion
		public String getPDAOutBoundOrderAutoSuggestion(String Plant,String dono) {

			String xmlStr = "";
			ArrayList al = null;
			DoHdrDAO dohdrDao = new DoHdrDAO();
			dohdrDao.setmLogger(mLogger);
			String query = "dono,custName,custcode,status,collectiondate";
			String extCond = "and dono like '%"+dono+"%' and status <> 'C' and ORDER_STATUS <>'Draft' ORDER BY CONVERT(date, CollectionDate, 103) desc";
			Hashtable ht = new Hashtable();
			ht.put("PLANT", Plant);
			//
			try {
				dohdrDao.setmLogger(mLogger);
				al = dohdrDao.selectDoHdr(query, ht, extCond);

				if (al.size() > 0) {
					xmlStr += XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("itemDetails total='"
							+ String.valueOf(al.size()) + "'");
					for (int i = 0; i < al.size(); i++) {
						Map map = (Map) al.get(i);
						xmlStr += XMLUtils.getStartNode("record");
						xmlStr += XMLUtils.getXMLNode("dono", (String) map
								.get("dono"));
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
		//created by vicky Used for PDA Sales Issue order popup with pagenation
		public String getPDApopupOutBoundOrderIssue(String Plant,String start,String end,String dono) {
			DoHdrDAO dohdrDao = new DoHdrDAO();
			dohdrDao.setmLogger(mLogger);
			String xmlStr = "";
			ArrayList al = null;

			
			String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY a.dono desc)) AS ID,ISNULL((SELECT COUNT(*)  FROM ["+Plant+"_DOHDR] as a, ["+Plant+"_DODET] as b  WHERE b.DONO LIKE '%' AND b.DONO = a.DONO AND b.PLANT = a.PLANT and a.DONO LIKE '%"+dono+"%'),0) TOTPO , a.dono as dono ,isnull(a.CustName,'') as CustName,isnull(a.collectiondate,'') orderdate,isnull(a.status,'') status  FROM ["+Plant+"_DOHDR] as a, ["+Plant+"_DODET] as b where  a.dono = b.dono AND a.PLANT = b.PLANT and isnull(qtyis,0) <> isnull(qtyPick,0) and a.status <> 'C' and ORDER_STATUS <>'Draft' group by a.dono, a.CustName, a.collectiondate,a.status,a.Remark1)SELECT * FROM S where ID >='"+start+"' and ID<='"+end+"' order by CAST((SUBSTRING(orderdate, 7, 4) + SUBSTRING(orderdate, 4, 2) + SUBSTRING(orderdate, 1, 2)) AS date )desc";
			String extCond ="";
			Hashtable ht = new Hashtable();

			try {
				al = dohdrDao.selectItemForPDA(query, ht, extCond);

				if (al.size() > 0) {
					xmlStr += XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("orders total='"
							+ String.valueOf(al.size()) + "'");
					for (int i = 0; i < al.size(); i++) {
						Map map = (Map) al.get(i);
						xmlStr += XMLUtils.getStartNode("record");
						xmlStr += XMLUtils.getXMLNode("dono", (String) map
								.get("dono"));
						xmlStr += XMLUtils.getXMLNode("customer", (String) StrUtils.replaceCharacters2SendPDA(map.get("CustName")
										.toString()));
						xmlStr += XMLUtils.getXMLNode("orderdate", (String) map
								.get("orddate"));
						xmlStr += XMLUtils.getXMLNode("status", (String) map
								.get("status"));
						xmlStr += XMLUtils.getXMLNode("count", (String) map
								.get("TOTPO"));
						xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils.replaceCharacters2SendPDA(map.get("Remark1")
										.toString()));
						
						xmlStr += XMLUtils.getEndNode("record");
					}
					xmlStr += XMLUtils.getEndNode("orders");
				}

			} catch (Exception e) {

			}

			return xmlStr;
		}
		//created by vicky Used for PDA sales Issue order AutoSuggestion
				public String getPDAOutBoundOrderIssueAutoSuggestion(String Plant,String dono) {

					String xmlStr = "";
					ArrayList al = null;
					DoHdrDAO dohdrDao = new DoHdrDAO();
					dohdrDao.setmLogger(mLogger);
					String query = "dono,custName,custcode,status,collectiondate";
					String extCond = "and dono like '%"+dono+"%' and isnull(qtyis,0) <> isnull(qtyPick,0) and status <> 'C' and ORDER_STATUS <>'Draft' ORDER BY CONVERT(date, CollectionDate, 103) desc";
					Hashtable ht = new Hashtable();
					ht.put("PLANT", Plant);
					//
					try {
						dohdrDao.setmLogger(mLogger);
						al = dohdrDao.selectDoHdr(query, ht, extCond);

						if (al.size() > 0) {
							xmlStr += XMLUtils.getXMLHeader();
							xmlStr += XMLUtils.getStartNode("itemDetails total='"
									+ String.valueOf(al.size()) + "'");
							for (int i = 0; i < al.size(); i++) {
								Map map = (Map) al.get(i);
								xmlStr += XMLUtils.getStartNode("record");
								xmlStr += XMLUtils.getXMLNode("dono", (String) map
										.get("dono"));
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
				//Created by Vicky Desc:Used for Validating Sales Order item for PDA based on UOM
				public String getoutBoundOrder_item_orderline_details(String Plant, String dono,String item,String uom,
		 				Boolean isReceived) {
		 			String xmlStr = "";
		 			ArrayList al = null;
		 			ItemMstDAO itemMstDAO = new ItemMstDAO();
		 			DoDetDAO dodetDao = new DoDetDAO();
		 			DoHdrDAO DoHdrDAO = new DoHdrDAO();
		 			dodetDao.setmLogger(mLogger);
		 			itemMstDAO.setmLogger(mLogger);
		 			DoHdrDAO.setmLogger(mLogger);

		 			String query = " ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL((select u.QPUOM from "+Plant+"_UOM u where u.UOM=UNITMO),1) as UOMQTY,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE ";
		 			String extCond = " lnstat <> 'C' and UNITMO LIKE '%"+uom+"%'";
		 			if (!isReceived) {
		 				extCond = extCond + " and PickStatus <> 'C' ";
		 			}

		 			extCond = extCond + " GROUP BY DOLNNO,ITEM,ITEMDESC,UNITMO ORDER BY DOLNNO ";

		 			Hashtable ht = new Hashtable();
		 			ht.put("DONO", dono);
		 			ht.put("PLANT", Plant);
		 			ht.put("ITEM", item);
		 			//
		 			try {
		 				String nonStock = StrUtils.fString(itemMstDAO.getNonStockFlag(Plant,item));
		 				String cSign = StrUtils.fString(DoHdrDAO.getSignatureCheck(Plant,item));
		 				al = dodetDao.selectDoDet(query, ht, extCond);
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
		 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
		 								.get("QTYOR")));
		 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
		 								.get("QTYPICK")));
		 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
		 								.get("QTYISSUE")));
		 						xmlStr += XMLUtils.getXMLNode("uom", (String) map
		 								.get("UNITMO"));
		 						xmlStr += XMLUtils.getXMLNode("remarks", "");
		 						xmlStr += XMLUtils.getXMLNode("nonStock", nonStock);
		 						xmlStr += XMLUtils.getXMLNode("csign", cSign);
		 						xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
		 								.get("UOMQTY"));
		 						xmlStr += XMLUtils.getEndNode("record");
		 					}
		 					xmlStr += XMLUtils.getEndNode("itemDetails");
		 				}

		 			} catch (Exception e) {
		 			}
		 			return xmlStr;
		 		}
				//Created by Vicky Desc:Used for getting Sales Order item For UOM popup Screen in PDA
				public String getoutBoundOrder_item_order_ForUOMPopup(String Plant, String dono,String item,String uom,
		 				Boolean isReceived) {
		 			String xmlStr = "";
		 			ArrayList al = null;
		 			ItemMstDAO itemMstDAO = new ItemMstDAO();
		 			DoDetDAO dodetDao = new DoDetDAO();
		 			DoHdrDAO DoHdrDAO = new DoHdrDAO();
		 			dodetDao.setmLogger(mLogger);
		 			itemMstDAO.setmLogger(mLogger);
		 			DoHdrDAO.setmLogger(mLogger);

		 			String query = " ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL((select u.QPUOM from "+Plant+"_UOM u where u.UOM=UNITMO),1) as UOMQTY,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE ";
		 			String extCond = " lnstat <> 'C' ";
		 			if (!isReceived) {
		 				extCond = extCond + " and PickStatus <> 'C' ";
		 			}

		 			extCond = extCond + " GROUP BY ITEM,ITEMDESC,UNITMO ";

		 			Hashtable ht = new Hashtable();
		 			ht.put("DONO", dono);
		 			ht.put("PLANT", Plant);
		 			ht.put("ITEM", item);
		 			//
		 			try {
		 				String nonStock = StrUtils.fString(itemMstDAO.getNonStockFlag(Plant,item));
		 				String cSign = StrUtils.fString(DoHdrDAO.getSignatureCheck(Plant,item));
		 				al = dodetDao.selectDoDet(query, ht, extCond);
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
		 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils.replaceCharacters2SendPDA(map.get("ITEMDESC")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
		 								.get("QTYOR")));
		 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
		 								.get("QTYPICK")));
		 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
		 								.get("QTYISSUE")));
		 						xmlStr += XMLUtils.getXMLNode("uom", (String) map
		 								.get("UNITMO"));
		 						xmlStr += XMLUtils.getXMLNode("remarks", "");
		 						xmlStr += XMLUtils.getXMLNode("nonStock", nonStock);
		 						xmlStr += XMLUtils.getXMLNode("csign", cSign);
		 						xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
		 								.get("UOMQTY"));
		 						xmlStr += XMLUtils.getEndNode("record");
		 					}
		 					xmlStr += XMLUtils.getEndNode("itemDetails");
		 				}

		 			} catch (Exception e) {
		 			}
		 			return xmlStr;
		 		}		
				
				
				
				public String getConvertedAverageUnitCostForProductByCurrency(String aPlant, String uom,String aItem) throws Exception {
			        String ConvertedUnitCost="";
			          try{
			               ConvertedUnitCost= _DoHdrDAO.getAverageUnitCost(aPlant, uom,aItem);
			          }catch(Exception e){
			              throw e;
			          }
			        return ConvertedUnitCost;
			    }
				
				
				public boolean autoprocess_DoPickIssueForPC(Map obj) throws Exception {
					boolean flag = false;
					UserTransaction ut = null;
					try {
						flag = process_Wms_DoPickIssue(obj);

						if (flag == true) {
							flag = true;
						} else {
							flag = false;
						}
					} catch (Exception e) {
						flag = false;
						throw e;
					}

					return flag;
				}
				
				//created by vicky Used for PDA checking sales order details
				public String getPDACheckingSalesOrder(String Plant,String dono) {

					String xmlStr = "";
					ArrayList al = null;
					DoHdrDAO dohdrDao = new DoHdrDAO();
					dohdrDao.setmLogger(mLogger);
					//String query = "Select dono,dolno,cname,item,itemdesc,batch,ordqty,status,sum(PICKQTY)as pickqty FROM ["+Plant+"_SHIPHIS] where dono= '"+dono+"' and STATUS='C' group by  dono,dolno,cname,item,itemdesc,batch,ordqty,status";
					String query ="with d as(Select dono,cname,item,itemdesc,batch,status,sum(PICKQTY)as pickqty FROM ["+Plant+"_SHIPHIS] where dono= '"+dono+"' and STATUS='C' group by  dono,cname,item,itemdesc,batch,status)Select*,isnull((select sum(a.QTYOR)from "+Plant+"_dodet a where a.DONO=d.DONO and a.ITEM=d.ITEM),0)ordqty from d";
					//String extCond = "where dono= '%"+dono+"%' and STATUS='C' group by  dono,dolno,cname,item,batch,ordqty,status";
					Hashtable ht = new Hashtable();
					ht.put("PLANT", Plant);
					//
					try {
						dohdrDao.setmLogger(mLogger);
						al = dohdrDao.selectForReport(query);

						if (al.size() > 0) {
							xmlStr += XMLUtils.getXMLHeader();
							xmlStr += XMLUtils.getStartNode("itemDetails total='"
									+ String.valueOf(al.size()) + "'");
							for (int i = 0; i < al.size(); i++) {
								Map map = (Map) al.get(i);
								xmlStr += XMLUtils.getStartNode("record");
								xmlStr += XMLUtils.getXMLNode("dono", (String) map
										.get("dono"));
								xmlStr += XMLUtils.getXMLNode("customer", (String) map
										.get("cname"));
								xmlStr += XMLUtils.getXMLNode("item", (String) map
										.get("item"));
								xmlStr += XMLUtils.getXMLNode("itemdesc", (String) map
										.get("itemdesc"));
								xmlStr += XMLUtils.getXMLNode("status", (String) map
										.get("batch"));
								xmlStr += XMLUtils.getXMLNode("qtyor", (String) map
										.get("ordqty"));
								xmlStr += XMLUtils.getXMLNode("qtyis", (String) map
										.get("pickqty"));
								xmlStr += XMLUtils.getEndNode("record");
							}
							xmlStr += XMLUtils.getEndNode("itemDetails");
						}

					} catch (Exception e) {
						this.mLogger.exception(this.printLog, "", e);
					}
					return xmlStr;
				}
				//created by vicky Used for PDA Sales Check order popup with pagenation
				public String getPDApopupSalesOrderCheck(String Plant,String start,String end,String dono) {
					DoHdrDAO dohdrDao = new DoHdrDAO();
					dohdrDao.setmLogger(mLogger);
					String xmlStr = "";
					ArrayList al = null;

					
					String query = "with S AS (SELECT (ROW_NUMBER() OVER (ORDER BY CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2))AS DATE) desc,a.dono desc)) AS ID,ISNULL((SELECT COUNT(*)  FROM ["+Plant+"_DOHDR] as a, ["+Plant+"_DODET] as b  WHERE b.DONO LIKE '%' AND b.DONO = a.DONO AND b.PLANT = a.PLANT and a.DONO LIKE '%"+dono+"%'),0) TOTPO , a.dono as dono ,isnull(a.CustName,'') as CustName,isnull(a.collectiondate,'') orderdate,isnull(a.status,'') status,isnull(a.Remark1,'') as Remark1  FROM ["+Plant+"_DOHDR] as a, ["+Plant+"_DODET] as b where  a.dono = b.dono AND a.PLANT = b.PLANT and a.status = 'C' and ORDER_STATUS <>'Draft' group by a.dono, a.CustName, a.collectiondate,a.status,a.Remark1)SELECT * FROM S where ID >='"+start+"' and ID<='"+end+"'";
					String extCond ="";
					Hashtable ht = new Hashtable();

					try {
						al = dohdrDao.selectItemForPDA(query, ht, extCond);

						if (al.size() > 0) {
							xmlStr += XMLUtils.getXMLHeader();
							xmlStr += XMLUtils.getStartNode("orders total='"
									+ String.valueOf(al.size()) + "'");
							for (int i = 0; i < al.size(); i++) {
								Map map = (Map) al.get(i);
								xmlStr += XMLUtils.getStartNode("record");
								xmlStr += XMLUtils.getXMLNode("dono", (String) map
										.get("dono"));
								xmlStr += XMLUtils.getXMLNode("customer", (String) strUtils
										.replaceCharacters2SendPDA(map.get("CustName")
												.toString()));
								xmlStr += XMLUtils.getXMLNode("orderdate", (String) map
										.get("orddate"));
								xmlStr += XMLUtils.getXMLNode("status", (String) map
										.get("status"));
								xmlStr += XMLUtils.getXMLNode("count", (String) map
										.get("TOTPO"));
								xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils
										.replaceCharacters2SendPDA(map.get("Remark1")
												.toString()));
								
								xmlStr += XMLUtils.getEndNode("record");
							}
							xmlStr += XMLUtils.getEndNode("orders");
						}

					} catch (Exception e) {

					}

					return xmlStr;
				}
				//created by vicky Used for PDA getting Items Sales Check order
				public String getRandomSalesCheckOrderItemDetails(String Plant, String dono,
		 				Boolean isReceived) {
		 			String xmlStr = "";
		 			ArrayList al = null;
		 			DoDetDAO dodetDao = new DoDetDAO();
		 			dodetDao.setmLogger(mLogger);

		 			String query = " DOLNNO,DONO,ITEM,ITEMDESC,ISNULL(UNITMO,'') UNITMO,ISNULL((select u.QPUOM from "+Plant+"_UOM u where u.UOM=UNITMO),1) as UOMQTY,ISNULL(SUM(QTYOR),0) QTYOR,ISNULL(SUM(QTYPICK),0) QTYPICK ,ISNULL(SUM(QTYIS),0) QTYISSUE,ISNULL(USERFLD3,'') CUSTOMERNAME ";
		 			String extCond = " ";
		 			extCond = extCond + " dono='"+dono+"' GROUP BY DOLNNO,DONO,USERFLD3,ITEM,ITEMDESC,UNITMO ORDER BY DOLNNO ";

		 			Hashtable ht = new Hashtable();
		 			//ht.put("DONO", dono);
		 			ht.put("PLANT", Plant);
		 			//
		 			try {
		 				al = dodetDao.selectDoDet(query, ht, extCond);
		 				if (al.size() > 0) {
		 					xmlStr += XMLUtils.getXMLHeader();
		 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
		 							+ String.valueOf(al.size()) + "'");
		 					for (int i = 0; i < al.size(); i++) {
		 						Map map = (Map) al.get(i);
		 						xmlStr += XMLUtils.getStartNode("record");
		 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
		 								.get("DONO"));
		 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
		 								.get("DONO"));
		 						xmlStr += XMLUtils.getXMLNode("customer",  (String) strUtils
		 								.replaceCharacters2SendPDA(map.get("CUSTOMERNAME")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("item", (String) map
		 								.get("ITEM"));
		 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
		 								.replaceCharacters2SendPDA(map.get("ITEMDESC")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
		 								.get("QTYOR")));
		 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
		 								.get("QTYPICK")));
		 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
		 								.get("QTYISSUE")));
		 						xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
		 						xmlStr += XMLUtils.getXMLNode("remarks", "");
		 						xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
		 								.get("UOMQTY"));
		 						xmlStr += XMLUtils.getEndNode("record");
		 					}
		 					xmlStr += XMLUtils.getEndNode("itemDetails");
		 				}

		 			} catch (Exception e) {
		 			}
		 			return xmlStr;
		 		}
		 		public String getSalesOrder_item_Byitem(String Plant, String dono,String item,
		 				String start,String end,Boolean isReceived) {
		 			String xmlStr = "";
		 			ArrayList al = null;
		 			DoDetDAO dodetDao = new DoDetDAO();
		 			dodetDao.setmLogger(mLogger);
		 			
		 			String query = "with d as(SELECT (ROW_NUMBER() OVER ( ORDER BY batch)) AS ID,dono,cname,item,itemdesc,batch,status,sum(PICKQTY)as pickqty,ISNULL((SELECT COUNT(*)from ["+Plant+"_SHIPHIS] WHERE DONO = '"+dono+"' AND PLANT = '"+Plant+"' and status = 'C'),0) TOTPO FROM ["+Plant+"_SHIPHIS] where ITEM LIKE '%"+item+"%' and dono= '"+dono+"' and STATUS='C' group by  dono,cname,item,itemdesc,batch,status)Select*,isnull((select sum(a.QTYOR)from "+Plant+"_dodet a where a.DONO=d.DONO and a.ITEM=d.ITEM),0)ordqty from d WHERE ID >="+start+" and ID<="+end+" ORDER BY BATCH";
		 			String extCond = "";
		 	
		 			Hashtable ht = new Hashtable();
		 			//
		 			try {
		 				al = dodetDao.selectDoDetForPage(query, ht, extCond);
		 				if (al.size() > 0) {
		 					xmlStr += XMLUtils.getXMLHeader();
		 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
		 							+ String.valueOf(al.size()) + "'");
		 					for (int i = 0; i < al.size(); i++) {
		 						Map map = (Map) al.get(i);
		 						xmlStr += XMLUtils.getStartNode("record");
		 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
		 								.get("dono"));
		 						
		 						xmlStr += XMLUtils.getXMLNode("customer",  (String) strUtils
		 								.replaceCharacters2SendPDA(map.get("cname")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("item", (String) map
		 								.get("item"));
		 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
		 								.replaceCharacters2SendPDA(map.get("itemdesc")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
		 								.get("ordqty")));
		 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
		 								.get("pickqty")));
		 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
		 								.get("pickqty")));
		 						xmlStr += XMLUtils.getXMLNode("uom", (String) map.get("UNITMO"));
		 						xmlStr += XMLUtils.getXMLNode("remarks", "");
		 						xmlStr += XMLUtils.getXMLNode("count", (String) map
		 								.get("TOTPO"));
		 						/*xmlStr += XMLUtils.getXMLNode("uomqty", (String) map.get("UOMQTY"));*/
		 						xmlStr += XMLUtils.getEndNode("record");
		 					}
		 					xmlStr += XMLUtils.getEndNode("itemDetails");
		 				}

		 			} catch (Exception e) {
		 			}
		 			return xmlStr;
		 		}
		 		//created by vicky Used for PDA Sales Check order item validation
		 		public String getSalesOrder_item_validation(String Plant, String dono,String item,
		 				String start,String end,Boolean isReceived) {
		 			String xmlStr = "";
		 			ArrayList al = null;
		 			DoDetDAO dodetDao = new DoDetDAO();
		 			dodetDao.setmLogger(mLogger);
		 			
		 			String query = "with d as(SELECT (ROW_NUMBER() OVER ( ORDER BY batch)) AS ID,dono,cname,item,itemdesc,batch,status,sum(PICKQTY)as pickqty FROM ["+Plant+"_SHIPHIS] where ITEM='"+item+"' and dono= '"+dono+"' and STATUS='C' group by  dono,cname,item,itemdesc,batch,status)Select*,isnull((select sum(a.QTYOR)from "+Plant+"_dodet a where a.DONO=d.DONO and a.ITEM=d.ITEM),0)ordqty from d";
		 			String extCond = "";
		 	
		 			Hashtable ht = new Hashtable();
		 			//
		 			try {
		 				al = dodetDao.selectDoDetForPage(query, ht, extCond);
		 				if (al.size() > 0) {
		 					xmlStr += XMLUtils.getXMLHeader();
		 					xmlStr += XMLUtils.getStartNode("itemDetails total='"
		 							+ String.valueOf(al.size()) + "'");
		 					for (int i = 0; i < al.size(); i++) {
		 						Map map = (Map) al.get(i);
		 						xmlStr += XMLUtils.getStartNode("record");
		 						xmlStr += XMLUtils.getXMLNode("dono", (String) map
		 								.get("dono"));
		 						
		 						xmlStr += XMLUtils.getXMLNode("customer",  (String) strUtils
		 								.replaceCharacters2SendPDA(map.get("cname")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("item", (String) map
		 								.get("item"));
		 						xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils
		 								.replaceCharacters2SendPDA(map.get("itemdesc")
		 										.toString()));
		 						xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
		 								.get("ordqty")));
		 						xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
		 								.get("pickqty")));
		 						xmlStr += XMLUtils.getXMLNode("qtyis", StrUtils.formatNum((String) map
		 								.get("pickqty")));
		 				
		 						xmlStr += XMLUtils.getEndNode("record");
		 					}
		 					xmlStr += XMLUtils.getEndNode("itemDetails");
		 				}

		 			} catch (Exception e) {
		 			}
		 			return xmlStr;
		 		}
 }
