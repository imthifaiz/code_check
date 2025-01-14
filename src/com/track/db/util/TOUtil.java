package com.track.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstBeanDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.tran.WmsTOPickReceive;
import com.track.tran.WmsTOPicking;
import com.track.tran.WmsTOReversal;
import com.track.tran.WmsToPickingRandom;
import com.track.tran.WmsToReceiveMaterial;
import com.track.tran.WmsToReceiveMaterialRandom;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class TOUtil {

	ToHdrDAO _ToHdrDAO = null;
	ToDetDAO _ToDetDAO = null;
	StrUtils strUtils = new StrUtils();
        private boolean printQuery = MLoggerConstant.SHIPHISDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TOUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public TOUtil() {
		_ToHdrDAO = new ToHdrDAO();
		_ToDetDAO = new ToDetDAO();

	}

	public boolean saveToHdrDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {
			_ToHdrDAO.setmLogger(mLogger);
			flag = _ToHdrDAO.insertToHdr(ht);

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
			_ToDetDAO.setmLogger(mLogger);
			al = _ToDetDAO.selectToDet(query, ht);
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
			_ToDetDAO.setmLogger(mLogger);
			al = _ToDetDAO.selectToDet(query, ht, extCond);
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
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectToHdr(query, ht);
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
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectToHdr(query, ht, extCond);
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
		_ToHdrDAO.setmLogger(mLogger);
		Map m = _ToHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custCode");

		return custCode;
	}
	//Start code by Bruhan to get custname for transfer order random on 8May2013
	public String getCustName(String aPlant, String aTONO) throws Exception {

		String custName = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("TONO", aTONO);

		String query = " ISNULL( " + "custName" + ",'') as " + "custName" + " ";
		_ToHdrDAO.setmLogger(mLogger);
		Map m = _ToHdrDAO.selectRow(query, ht);

		custName = (String) m.get("custName");

		return custName;
	}
	//End code by Bruhan to get custname for transfer order random on 8May2013
	public ArrayList getOutGoingToHdrDetailsPda(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectTransferOrderHdrForPda(query, ht);
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
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectTransferOrderHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public ArrayList getOutGoingToHdrDetail(String query, Hashtable ht)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectConsignmentOrderHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	//copy
	public ArrayList getOutGoingToHdrDetailGino(String query, Hashtable ht , String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectConsignmentOrderGino(query, ht ,extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public boolean saveToDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_ToDetDAO.setmLogger(mLogger);
			flag = _ToDetDAO.insertToDet(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public String getNextTo(String aPlant) throws Exception {

		String maxDo = "";
		try {
			_ToHdrDAO.setmLogger(mLogger);
			maxDo = _ToHdrDAO.getMaxDo(aPlant);

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
			alToHdr = getDoHdrDetails(queryToHdr, htToHdr, " and STATUS <> 'C'");

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
				throw new Exception("No open Consignment order found for picking");
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return xmlStr;
	}

	public String load_all_to_items_xml(String aPlant, String aToNum)
			throws Exception {

		String xmlStr = "", dono = "", dolno = "", item = "", itemDesc = "";
		ArrayList alToDet = null;
		String querytoDet = "";
		Hashtable httoDet = null;
		httoDet = new Hashtable();
		querytoDet = " tono,tolnno,item";
		// condition
		httoDet.put("plant", aPlant);
		httoDet.put("TONO", aToNum);
		try {
			alToDet = getToDetDetails(querytoDet, httoDet, " LNSTAT <> 'C'");

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ToDet total ='"
					+ String.valueOf(alToDet.size()) + "'");

			if (alToDet.size() > 0) {
				for (int i = 0; i < alToDet.size(); i++) {

					Map map1 = (Map) alToDet.get(i);
					dono = (String) map1.get("dono");
					dolno = (String) map1.get("dolnno");
					item = (String) map1.get("item");
					String extraCon = " LOC = 'SHIPPINGAREA' AND QTY >0 ";
					String loc = new InvMstDAO().getItemLoc(aPlant, item,
							extraCon);
					// itemDesc= (String) map1.get("itemDesc");
					itemDesc = new ItemMstDAO().getItemDesc(aPlant, item);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("toNum", dono);
					xmlStr += XMLUtils.getXMLNode("toLnNo", dolno);
					xmlStr += XMLUtils.getXMLNode("item", item);
					xmlStr += XMLUtils.getXMLNode("itemDesc", itemDesc);
					xmlStr += XMLUtils.getXMLNode("loc", loc);
					xmlStr += XMLUtils.getEndNode("record");

				}

				xmlStr += XMLUtils.getEndNode("ToDet");

			} else {
				throw new Exception("No Product found for order " + aToNum);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return xmlStr;
	}

	public boolean updateToHdr(Hashtable ht) throws Exception {
		boolean flag = false;

		Hashtable htCond = new Hashtable();
		htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put(IDBConstants.TOHDR_TONUM, (String) ht
				.get(IDBConstants.TOHDR_TONUM));
		try {

			StringBuffer updateQyery = new StringBuffer("set ");

			updateQyery.append(IDBConstants.TOHDR_FROM_WAREHOUSE + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_FROM_WAREHOUSE) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_TO_WAREHOUSE + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_TO_WAREHOUSE) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_CONTACT_NUM + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_CONTACT_NUM) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_COL_DATE + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_COL_DATE) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_REMARK1 + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_REMARK1) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_REMARK2 + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_REMARK2) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_CUST_NAME + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_CUST_NAME) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_CUST_CODE + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_CUST_CODE) + "'");
			updateQyery
					.append(","
							+ IDBConstants.TOHDR_PERSON_INCHARGE
							+ " = '"
							+ (String) ht
									.get(IDBConstants.TOHDR_PERSON_INCHARGE)
							+ "'");
			updateQyery.append("," + IDBConstants.TOHDR_ADDRESS + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_ADDRESS) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_ADDRESS2 + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_ADDRESS2) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_ADDRESS3 + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_ADDRESS3) + "'");
			updateQyery.append("," + IDBConstants.TOHDR_COL_TIME + " = '"
					+ (String) ht.get(IDBConstants.TOHDR_COL_TIME) + "'");
			/*updateQyery.append("," + IDBConstants.STATUS + " = '"
					+ (String) ht.get(IDBConstants.STATUS) + "'");*/
			 updateQyery.append("," + IDBConstants.TOHDR_SCUST_NAME + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_SCUST_NAME) + "'");
			 updateQyery.append("," + IDBConstants.TOHDR_SCONTACT_NAME + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_SCONTACT_NAME) + "'");
			 updateQyery.append("," + IDBConstants.TOHDR_SADDR1 + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_SADDR1) + "'");
			 updateQyery.append("," + IDBConstants.TOHDR_SADDR2 + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_SADDR2) + "'");
			 updateQyery.append("," + IDBConstants.TOHDR_SCITY + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_SCITY) + "'");
			 updateQyery.append("," + IDBConstants.TOHDR_SCOUNTRY + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_SCOUNTRY) + "'");
			 updateQyery.append("," + IDBConstants.TOHDR_SZIP + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_SZIP) + "'");
			 updateQyery.append("," + IDBConstants.TOHDR_STELNO + " = '"
			                 + (String) ht.get(IDBConstants.TOHDR_STELNO) + "'");
			 updateQyery.append("," +IDBConstants.TOHDR_JOB_NUM + " = '"
						+ (String) ht.get(IDBConstants.TOHDR_JOB_NUM) + "'");


			flag = _ToHdrDAO.update(updateQyery.toString(), htCond, " AND STATUS ='N'");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Processed Consignment Order Cannot be modified");
		}

		return flag;
	}

	/**********************  Modification History  ******************************
	 *** Bruhan,Nov 10 2014,Description: To include QTYPIKC,QTYRC,PICKSTAT in link
   */ 
	public String listTODET(String plant, String aTONO, String rFlag)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("TONO", aTONO);
			q = "tono,tolnno,lnstat,item,isnull(itemdesc,'') itemdesc,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(unitmo,'') uom,isnull(qtyPick,0) as qtyPick,isnull(pickstatus,'') as pickstatus ,isnull(comment1,'') as prdRemarks ";
			_ToDetDAO.setmLogger(mLogger);
			ArrayList al = _ToDetDAO.selectToDet(q, htCond,
					" and plant <> '' order by tolnno");

			if (al.size() > 0) {
				ItemMstDAO _ItemMstDAO = new ItemMstDAO();
				_ItemMstDAO.setmLogger(mLogger);
				new InvMstDAO();
				for (int i = 0; i < al.size(); i++) {

					Map m = (Map) al.get(i);
					String tono = (String) m.get("tono");
					String tolnno = (String) m.get("tolnno");
					String item = (String) m.get("item");
					String itemdesc = (String) m.get("itemdesc");
					String uom = (String) m.get("uom");
					String qtyor = StrUtils.formatNum((String) m.get("qtyor"));
					String qtyPick =StrUtils.formatNum((String) m.get("qtyPick"));
					String qtyrc = StrUtils.formatNum((String) m.get("qtyrc"));
					String desc = _ItemMstDAO.getItemDesc(plant, item);
			//		String uom = _ItemMstDAO.getItemUOM(plant, item);
					String lnstat = (String) m.get("lnstat");
					String pickstat = (String) m.get("pickstatus");
				    String prdRemarks = (String) m.get("prdRemarks");
							link = "<a href=\"" + "modiTODet.jsp?TONO=" + tono + "&QTYPICK=" + qtyPick + "&QTYRC=" + qtyrc+ "&PICKSTATUS=" + pickstat
							+ "&TOLNNO=" + tolnno + "&DESC=" + StrUtils.replaceCharacters2Send(itemdesc) +"&QTY=" + qtyor + "&ITEM=" + item + "&UOM=" + uom + "&RFLAG="+ rFlag + "&PRDREMARKS="+ prdRemarks +"\">";

					if (pickstat.equals("N")) {
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"10%\">"
								+ tolnno
								+ "</td>"
								+ "<td width=\"17%\">"
								+ link
								+ "<FONT COLOR=\"blue\">"
								+ sqlBean.formatHTML(item)
								+ "</a></font>"
								+ "</td>"
								+ "<td width=\"27%\">"
								+ sqlBean.formatHTML(desc)
								+ "</td>"
								+ "<td width=\"12%\" align =\"center\">"
								+ sqlBean.formatHTML(qtyor)
								+ "</td>"
								+ "<td width=\"12%\" align =\"center\" >"
								+ sqlBean.formatHTML(qtyPick)
								+ "</td>"
								+ "<td width=\"12%\" align =\"center\" >"
								+ sqlBean.formatHTML(qtyrc)
								+ "</td>"
								+ "<td width=\"5%\" align =\"center\">"
								+ uom
								+ "</td>"
								+ "<td width=\"5%\" align =\"center\">"
								+ lnstat
								+ "</td>"
								+ "</tr>";
					} else {
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"10%\">"
								+ tolnno
								+ "</td>"
								+ "<td width=\"17%\">"
								+ link
								+ "<FONT COLOR=\"blue\">"
								+ sqlBean.formatHTML(item)
								+ "</a></font>"
								+ "</td>"
								+ "<td width=\"27%\">"
								+ sqlBean.formatHTML(desc)
								+ "</td>"
								+ "<td width=\"12%\" align =\"center\">"
								+ sqlBean.formatHTML(qtyor)
								+ "</td>"
								+ "<td width=\"12%\" align =\"center\" >"
								+ sqlBean.formatHTML(qtyPick)
								+ "</td>"
								+ "<td width=\"12%\" align =\"center\">"
								+ sqlBean.formatHTML(qtyrc)
								+ "</td>"
								+ "<td width=\"5%\" align =\"center\">"
								+ uom
								+ "</td>"
								+ "<td width=\"5%\" align =\"center\">"
								+ lnstat
								+ "</td>"
								+ "</tr>";

					}

				}
			} else {
				result += "<tr valign=\"middle\"><td colspan=\"8\" align=\"center\">"
						+ " Please add in Products " + "</td></tr>";

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return result;
	}

	public ArrayList getToHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {

		ArrayList al = new ArrayList();

		try {
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectToHdr(query, ht, extCond);
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

		// String query=" custCode ";
		String query = " ISNULL( " + "custName" + ",'') as " + "custName" + " ";
		_ToHdrDAO.setmLogger(mLogger);
		Map m = _ToHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custName");

		return custCode;
	}

	public String getOpenTransferOrder(String Plant, Boolean isReveiced) {
		ToHdrDAO tohdrDao = new ToHdrDAO();
		tohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " a.tono as tono, a.CustName as CustName,a.Remark1 as Remark1";
		String extCond = " and isnull(a.status,'') <> 'C'";
		if (!isReveiced) {
			extCond = extCond + " and b.pickStatus <> 'C'";
		}
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);
		//
		try {
			al = tohdrDao.selectToHdrOpenNew(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("pono", (String) map
							.get("tono"));
					xmlStr += XMLUtils.getXMLNode("supplier", (String) StrUtils
							.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils
							.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}
			System.out.println(xmlStr);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
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
		
		extCond = extCond + " ORDER BY ITEM ";
		
		Hashtable ht = new Hashtable();
		ht.put("TONO", tono);
		ht.put("PLANT", plant);
		try {
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToDetDAO.selectToDet(query, ht, extCond);

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
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("QTYPICK")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) map
							.get("REMARKS"));

					xmlStr += XMLUtils.getXMLNode("fromLoc", _ToHdrDAO
							.getLocation(plant, tono, "FROMWAREHOUSE"));
					xmlStr += XMLUtils.getXMLNode("toLoc", _ToHdrDAO
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

	public String getOpenTransferOrderSupDetails(String Plant, String OrdNum) {

		ToHdrDAO tohdrDao = new ToHdrDAO();
		tohdrDao.setmLogger(mLogger);

		String xmlStr = "";
		ArrayList al = null;

		String query = " dono,CustName,Remark1 ";
		String extCond = " AND TONO = '" + OrdNum + "' and status <> 'C'";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);

		try {
			al = tohdrDao.selectToHdr(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", (String) map
							.get("CustName"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) map
							.get("Remark1"));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	public String getOpenTransOrderSupDetails(String Plant, String OrdNum) {

		ToHdrDAO tohdrDao = new ToHdrDAO();
		tohdrDao.setmLogger(mLogger);
		String xmlStr = "";
		ArrayList al = null;

		String query = " tono,CustName,Remark1 ";
		String extCond = " AND TONO = '" + OrdNum + "' and status <> 'C'";
		Hashtable ht = new Hashtable();

		ht.put("PLANT", Plant);

		try {
			al = tohdrDao.selectToHdr(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", (String) StrUtils
							.replaceCharacters2SendPDA(map.get("CustName")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) StrUtils
							.replaceCharacters2SendPDA(map.get("Remark1")
									.toString()));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	public ArrayList listTODETForPicking(String plant, String aTONO)
			throws Exception {
		String q = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("TONO", aTONO);
			q = "tono,tolnno,pickstatus,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname,"+
                        "(select itemdesc from "+plant+"_ITEMMST where plant = a.plant and ITEM= a.item) as itemdesc," + 
                        "ISNULL(UNITMO,'') as stkuom";
			_ToDetDAO.setmLogger(mLogger);
			al = _ToDetDAO.selectToDet(q, htCond,
					" and plant <> '' and ISNULL(pickstatus,'') <>'C' order by tolnno");

			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}


    public ArrayList listTODETForReceiving(String plant, String aTONO)
                    throws Exception {
            String q = "";
            ArrayList al = null;

            try {
                    Hashtable htCond = new Hashtable();
                    htCond.put("PLANT", plant);
                    htCond.put("TONO", aTONO);
                    q = "tono,tolnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname";
                    _ToDetDAO.setmLogger(mLogger);
                    al = _ToDetDAO.selectToDet(q, htCond,
                                    " and plant <> '' and ISNULL(lnstat,'') <>'C' order by tolnno");

                    
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }

            return al;
    }
    /**
	 * Author Bruhan. 3 Aug 2012
	 * method : listTODETForBulkReceiving(String plant, String tono)
	 * description : gets the information to recieve the ordered numbers.)
	 * 
	 * @param : String plant, String tono
	 * @return : String
	 * @throws Exception
	 */
     public ArrayList<String> listTODETForBulkReceiving(String plant, String tono)
                     throws Exception {
             String query = "";
             ArrayList al = new ArrayList<String>();
             Connection con  = null;
     ToDetDAO toDao = new ToDetDAO();
       toDao.setmLogger(mLogger);

             try {
                     con = DbBean.getConnection();
                     query = "select PLANT,TONO,TOLNO,ITEM,BATCH,SUM(PICKQTY)as PICKQTY,"
                    	 +" (select itemdesc from ["+plant+"_ITEMMST] where PLANT =pickTbl.PLANT and ITEM =pickTbl.ITEM) as Itemdesc,(select stkuom from ["+plant+"_ITEMMST] where PLANT =pickTbl.PLANT and ITEM =pickTbl.ITEM) as uom,"
                             + " isnull((SELECT SUM(RECQTY)FROM  ["+plant+"_RECVDET]  where plant ='"+ plant+ "' and PONO = '"+tono+"'  and LNNO=pickTbl.TOLNO " 
                             + "and BATCH = pickTbl.BATCH and ITEM = pickTbl.ITEM and ISNULL(RECEIVESTATUS,'') <> 'C'),0) as RECVQTY"
                             + " from ["+plant+"_TO_PICK] pickTbl where plant= '"+ plant+ "' and tono = '"+tono+"' and ISNULL(status, '') <> 'C' group by PLANT,TONO,TOLNO,ITEM,BATCH order by cast(TOLNO as int)";
                     this.mLogger.query(this.printQuery, query.toString());
         al = toDao.selectData(con, query.toString());

                     } 
                     catch (Exception e) {
                             this.mLogger.exception(this.printLog, "", e);
                             throw e;
                     } finally {
                             if (con != null) {
                 DbBean.closeConnection(con);
                             }
         }
              return al;
     }
     
     
    public ArrayList listTODETDetails(String plant, String aTONO)
                    throws Exception {
            String q = "";
            ArrayList al = null;

            try {
                    Hashtable htCond = new Hashtable();
                    htCond.put("PLANT", plant);
                    htCond.put("TONO", aTONO);
                    q = "tono,tolnno,lnstat as pickstatus,item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname";
                    _ToDetDAO.setmLogger(mLogger);
                    al = _ToDetDAO.selectToDet(q, htCond,
                                    " and plant <> '' and ISNULL(lnstat,'') <>'C' order by tolnno");

                    
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }

            return al;
    }

	public String process_ToPickingForPDA(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_ToPicking(obj);

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
					"Error in Consignment picking Product : "
							+ obj.get(IConstants.ITEM) + " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  picked successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in process_ToPicking  Product : "
							+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}

	public boolean process_ToPickingForPC(Map obj) throws Exception {
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

			flag = process_Wms_ToPicking(obj);

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
	//Start added by Bruhan for Bulk picking on 15 Mar 2013. 
	public boolean process_BulkToPickingForPC(Map obj) throws Exception {
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

			flag = process_Wms_ToPicking(obj);
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
	//End added by Bruhan for Bulk picking on 15 Mar 2013. 

	public boolean process_Wms_ToPicking(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsTOPicking();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}

	public ArrayList getToHdrDetailsReceiving(String query, Hashtable ht,
			String extCond) throws Exception {

		ArrayList al = new ArrayList();

		try {
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectToHdrReciving(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public ArrayList getAssigneeHdrDetails(String query, Hashtable ht,
			String extCond) throws Exception {

		ArrayList al = new ArrayList();

		try {
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToHdrDAO.selectAssigneeHdrReciving(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}

	public String getBatchDetailsForTO(String plant, String item,
			String orderNo, String orderLineNo, String fromLoc)
			throws Exception {
		String result = "";
		String result1 = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			_ToHdrDAO.setmLogger(mLogger);
			ToDetDAO _todetDAO = new ToDetDAO();
			_todetDAO.setmLogger(mLogger);
			alData = _todetDAO.getToOrderBatchListToRecv(plant, orderNo,
					orderLineNo, item);
			if (alData.size() > 0) {
				int totalValue = 0;
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				for (Map<String, String> hashMap : alData) {

					String sBatch = (String) hashMap.get("batch");
					String spickQty = (String) hashMap.get("pickQty");
					String srecvQty = (String) hashMap.get("recQty");

					double availQty = Double.parseDouble(spickQty)
							- Double.parseDouble(srecvQty);
					availQty = StrUtils.RoundDB(availQty, IConstants.DECIMALPTS);
					if (availQty > 0) {
						totalValue++;
						result1 += XMLUtils.getStartNode("record");
						result1 += XMLUtils.getXMLNode("batchId", hashMap
								.get("batch"));
						result1 += XMLUtils.getXMLNode("qty", (StrUtils.formatNum(String.valueOf(availQty))));
						result1 += XMLUtils.getXMLNode("uom", itemMstDAO
								.getItemUOM(plant, item));
						
						result1 += XMLUtils.getXMLNode("loc", hashMap
								.get("loc"));
				
						result1 += XMLUtils.getEndNode("record");
					}
				}
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ totalValue + "'");
				result += result1;
				result += XMLUtils.getEndNode("BatchDetails");
			} else {
				result = XMLUtils.getXMLMessage(1, "Batch Detiails Not found");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
	}
        
    public String getQtyForBatchToRecvForTO(String plant, String item,
                    String orderNo, String orderLineNo, String batch,String fromLoc)
                    throws Exception {
            String result = "";
            String result1 = "";
            ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
            java.sql.Connection con = null;
            String xmlStr="";

            try {
                    con = DbBean.getConnection();
                    _ToHdrDAO.setmLogger(mLogger);
                    ToDetDAO _todetDAO = new ToDetDAO();
                    _todetDAO.setmLogger(mLogger);
                    alData = _todetDAO.getToOrderQtyToRecvForBatch(plant, orderNo,
                                    orderLineNo, item,batch,fromLoc); 
                                    
                if (alData.size() > 0) {
                   
                    ItemMstDAO itemMstDAO = new ItemMstDAO();
                    itemMstDAO.setmLogger(mLogger);
                    for (Map<String, String> hashMap : alData) {

                            
                            String spickQty = (String) hashMap.get("pickQty");
                            String srecvQty = (String) hashMap.get("recQty");

                            double availQty = Double.parseDouble(spickQty)
                                            - Double.parseDouble(srecvQty);
             availQty = StrUtils.RoundDB(availQty, IConstants.DECIMALPTS);
             	String availabqty = String.valueOf(availQty);
                         if(availabqty.length()>0){
                            xmlStr = XMLUtils.getXMLHeader();
                            xmlStr += XMLUtils.getStartNode("InvAvailQty");
                            xmlStr += XMLUtils.getXMLNode("status", "0");
                            xmlStr += XMLUtils.getXMLNode("description", "");
                             xmlStr += XMLUtils.getXMLNode("AVAILQTY", (StrUtils.formatNum(String.valueOf(availQty))));
                            xmlStr += XMLUtils.getXMLNode("uom", itemMstDAO
                                            .getItemUOM(plant, item));
                            //result1 += XMLUtils.getXMLNode("loc", fromLoc);
                           // xmlStr += XMLUtils.getXMLNode("loc", hashMap.get("loc"));
                            xmlStr += XMLUtils.getEndNode("InvAvailQty");
                        }else{
                            xmlStr = XMLUtils.getXMLMessage(1, "Batch Details Not found");
                        }
                       }
                        

                } else {
                        xmlStr = XMLUtils.getXMLMessage(1, "Not a valid batch ");
                }
                
                 

                    return xmlStr;
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            } finally {
                    DbBean.closeConnection(con);
            }
    }

	public String getBatchDetails(String plant, String item, String orderNo,
			String orderLineNo) throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		java.sql.Connection con = null;

		StringBuffer sql = new StringBuffer(
				"select distinct  batch,sum(pickqty) as pickQty,isnull((select sum(recqty) from  ["
						+ plant
						+ "_Recvdet] "
						+ " where plant ='"
						+ plant
						+ "' and pono ='"
						+ orderNo
						+ "' and lnno='"
						+ orderLineNo
						+ "' and item ='"
						+ item
						+ "' and batch =a.batch),0) as recQty"
						+ " from ["
						+ plant
						+ "_TO_PICK] as a where plant='"
						+ plant
						+ "' and  tono ='"
						+ orderNo
						+ "' and tolno='"
						+ orderLineNo
						+ "' and  item='"
						+ item
						+ "'  and  batch like '%' and PickQty > 0 group by batch");
		try {
			con = DbBean.getConnection();
			_ToHdrDAO.setmLogger(mLogger);
			alData = this._ToHdrDAO.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils
							.getXMLNode("BATCH", hashMap.get("batch"));
					result += XMLUtils.getXMLNode("PICK_QTY", hashMap
							.get("pickQty"));
					result += XMLUtils.getXMLNode("RECEIVED_QTY", hashMap
							.get("recQty"));
					result += XMLUtils.getEndNode("record");
				}

				result += XMLUtils.getEndNode("BatchDetails");
			} else {
				result = XMLUtils.getXMLMessage(1, "Item Not found");
			}

			return result;
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
	}

	public String getBatchDetails(String plant, String item, String tono)
			throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer(
				"select batch, (pickqty-isnull(reverseqty,0)) as pickqty,ordqty from ["
						+ plant + "_TO_PICK]  where plant='" + plant
						+ "' and item='" + item + "' and tono='" + tono + "'");

		try {
			con = DbBean.getConnection();
			_ToHdrDAO.setmLogger(mLogger);
			alData = this._ToHdrDAO.selectData(con, sql.toString());
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils
							.getXMLNode("BATCH", hashMap.get("batch"));
					result += XMLUtils.getXMLNode("PICK_QTY", hashMap
							.get("pickqty"));
					result += XMLUtils.getXMLNode("OREDERD_QTY", hashMap
							.get("ordqty"));
					result += XMLUtils.getEndNode("record");
				}

				result += XMLUtils.getEndNode("BatchDetails");
			} else {
				result = XMLUtils.getXMLMessage(1, "Item Not found");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
	}

	public String process_ToReceiveMaterialForPDA(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_ToReceiving(obj);

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

	public boolean process_ToReceiveMaterialForPC(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.TORECV
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

			flag = process_Wms_ToReceiving(obj);

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
	
	 /**
	 * Author Bruhan. 3 Aug 2012
	 * method : process_BulkReceiveMaterialForPC(Map obj) description : passes the information to process the checked ordered numbers.)
	 * 
	 * @param : Map obj
	 * @return : boolean
	 * @throws Exception
	 */
	public boolean process_BulkReceiveMaterialForPC(Map obj) throws Exception {
		boolean flag = false;
		//start code by Bruhan for transfer receive bulk fine tunning on 07 august 2013
		UserTransaction ut = null;
		try {	
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.TORECV
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

			flag = process_Wms_ToReceiving(obj);
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

	public boolean process_Wms_ToReceiving(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsToReceiveMaterial();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}

	public String getJobNum(String plant, String toNum) throws Exception {

		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", plant);
		ht.put("TONO", toNum);

		String query = " ISNULL( " + "jobNum" + ",'') as " + "jobNum" + " ";
		_ToHdrDAO.setmLogger(mLogger);
		Map m = _ToHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("jobNum");

		return custCode;
	}

	public boolean deleteTransferDetLineDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {
			ToDetDAO _toDetDAO = new ToDetDAO();
			RecvDetDAO recvdetDAO = new RecvDetDAO();
			_toDetDAO.setmLogger(mLogger);
			Hashtable httoDet = new Hashtable();
			httoDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
			httoDet.put(IDBConstants.TODET_TONUM, ht.get(IDBConstants.TODET_TONUM));
			httoDet.put(IDBConstants.TODET_TOLNNO, ht.get(IDBConstants.TODET_TOLNNO));
			httoDet.put(IDBConstants.TODET_ITEM, ht.get(IDBConstants.TODET_ITEM));

			flag = _toDetDAO.delete(httoDet);
            try{
    		    //To arrange the line no's
    		        httoDet.remove(IDBConstants.TODET_TOLNNO);
    		        httoDet.remove(IDBConstants.TODET_ITEM);
    		        String  sql =  "SET TOLNNO =TOLNNO-1 ";
                            String extraCond=" AND TOLNNO > '" + ht.get(IDBConstants.TODET_TOLNNO) + "' ";
                            _toDetDAO.update(sql,httoDet,extraCond);
                          
                      //To arrange the line no's in topick
                        boolean topickflag = false;
                        topickflag = _toDetDAO.isExisitTOPick(httoDet);
                         if(topickflag)
                         { 
                           sql =  "SET TOLNO =TOLNO-1 ";
                           extraCond=" AND CAST(TOLNO as int) > " + ht.get(IDBConstants.TODET_TOLNNO) + " ";
                           _toDetDAO.updateToPickTable(sql,httoDet,extraCond);
                         }
                         
                       //To arrange the line no's in recvdet 
                         boolean recvdetflag = false;
                         Hashtable htDet = new Hashtable();
                         htDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
                         htDet.put(IDBConstants.PODET_PONUM, ht.get(IDBConstants.TODET_TONUM));
                       
                         recvdetflag = recvdetDAO.isExisit(htDet, (String)ht.get(IDBConstants.PLANT));
                       if(recvdetflag)
                       {
                     	  sql =  "SET LNNO =LNNO-1 ";
                     	  extraCond=" AND CAST(LNNO as int) > " + ht.get(IDBConstants.TODET_TOLNNO)  + " ";
                     	  recvdetDAO.update(sql,htDet,extraCond,(String)ht.get(IDBConstants.PLANT));
                       } 
                           
                           
            }catch(Exception e){}
			if (flag) {
				// Insert record into Movhis
				MovHisDAO movhisDao = new MovHisDAO();
				Hashtable htmovHis = new Hashtable();
				htmovHis.clear();
				htmovHis.put(IDBConstants.PLANT, (String) ht
						.get(IDBConstants.PLANT));
				htmovHis.put("DIRTYPE", "TRAN_DEL_ITEM");
				htmovHis.put(IDBConstants.ITEM, (String) ht
						.get(IDBConstants.TODET_ITEM));
				htmovHis.put(IDBConstants.MOVHIS_ORDNUM, (String) ht
						.get(IDBConstants.TODET_TONUM));
				htmovHis.put(IDBConstants.MOVHIS_ORDLNO, (String) ht
						.get(IDBConstants.TODET_TOLNNO));
				htmovHis.put(IDBConstants.CREATED_BY, (String) ht
						.get(IDBConstants.CREATED_BY));
				htmovHis.put("MOVTID", "");
				htmovHis.put("RECID", "");
				htmovHis.put(IDBConstants.TRAN_DATE, new DateUtils()
						.getDateinyyyy_mm_dd(new DateUtils().getDate()));
				htmovHis.put(IDBConstants.CREATED_AT, new DateUtils()
						.getDateTime());

				flag = movhisDao.insertIntoMovHis(htmovHis);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public Boolean removeRow(String plant, String tono, String userId) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			ut = DbBean.getUserTranaction();
			ut.begin();
			Boolean removeHeader = this._ToHdrDAO.removeOrder(plant, tono);
			Boolean removeDetails = this._ToDetDAO.removeOrderDetails(plant,
					tono);
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
		htMovHis.put("DIRTYPE", "DELETE_CONSIGNMENT_ORDER");
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


    public boolean isOpenTransferOrder(String plant,String tono) throws Exception {

            boolean flag = false;
            try {
                   
                    ToHdrDAO _ToHdrDAO = new ToHdrDAO(plant);
                    _ToHdrDAO.setmLogger(mLogger);

                 
                            Hashtable htCondiPoHdr = new Hashtable();
                            htCondiPoHdr.put("PLANT",plant);
                            htCondiPoHdr.put("tono", tono);
                            
                            flag = _ToHdrDAO.isExisit(htCondiPoHdr, " STATUS ='N'");
                        
                         /*   if (!flag){
                                throw new Exception(" Processed Transfer Order Cannot be modified");
                            }*/
                   

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }

            return flag;
    }
 public boolean isPrintedTransferOrder(String plant,String tono) throws Exception {

        boolean flag = false;
        try {
               
                ToHdrDAO _ToHdrDAO = new ToHdrDAO(plant);
                _ToHdrDAO.setmLogger(mLogger);

             
                        Hashtable htCondiPoHdr = new Hashtable();
                        htCondiPoHdr.put("PLANT",plant);
                        htCondiPoHdr.put("tono", tono);
                        
                        flag = _ToHdrDAO.isExisit(htCondiPoHdr, " ISPRINTED ='Y'");
                    
                        if (flag){
                            throw new Exception(" Printed Consignment Order Cannot be modified");
                        }
               

        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e;
        }

        return flag;
}
    
    public boolean isExitsToLine(Hashtable ht)
                    throws Exception {

            boolean flag = false;
            try {

                    ToDetDAO _todetDAO = new ToDetDAO();
                    _todetDAO.setmLogger(mLogger);
                    flag = _todetDAO.isExisit(ht);

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }

            return flag;
    }
    
    public ArrayList listTODetilstoPrint(String plant, String aTono)
                    throws Exception {
            String q = "";
            ArrayList al = null;
          ToDetDAO toDao = new ToDetDAO();
            toDao.setmLogger(mLogger);
            try {
                  al = toDao.getDoDetailToPrint(plant,aTono);

            } catch (Exception e) {
                    throw e;
            }

            return al;
    }
    
    public ArrayList listTODetilstoPrintTo(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String custname,String custtype)
            throws Exception {
    String q = "";
    ArrayList al = null;
  ToDetDAO toDao = new ToDetDAO();
    toDao.setmLogger(mLogger);
    try {
          al = toDao.getDoDetailToPrintTo(ht,afrmDate,atoDate,dirType,plant, custname,custtype);

    } catch (Exception e) {
            throw e;
    }

    return al;
}


        public ArrayList getToPickDetailsToConfirm(String plant, String orderno)
                    throws Exception {

            java.sql.Connection con = null;
            ArrayList al = new ArrayList();
            ToDetDAO toDao = new ToDetDAO();
              toDao.setmLogger(mLogger);
            try {
                    con = com.track.gates.DbBean.getConnection();

                    boolean flag = false;

                    StringBuffer sQry = new StringBuffer( " SELECT TOLNO,ITEM,ITEMDESC,(select stkuom from "+plant+"_ITEMMST where item =a.item) as Uom,BATCH,LOC,ORDQTY,PICKQTY FROM " + "[" + plant + "_" + "TO_PICK" + "] a " + "");
                    sQry.append(" where plant='"+ plant+ "' and tono = '" + orderno+ "' and isnull(status,'')=''");

                    this.mLogger.query(this.printQuery, sQry.toString());
                    al = toDao.selectData(con, sQry.toString());

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
        
        
        public Map getReceiptHdrDetails(String aplant,String orderType) throws Exception {
	        Map m =  new HashMap();
	     
	        m=_ToHdrDAO.getReciptHeaderDetails(aplant,orderType);
	         return m;
	 }
        
        public Map getTOReceiptHdrDetails(String aplant) throws Exception {
	        Map m =  new HashMap();
	     
	        m=_ToHdrDAO.getTOReciptHeaderDetails(aplant);
	         return m;
	 }
        
        
public boolean isNewStatusTONO(String tono, String plant) throws Exception {
    		boolean exists = false;
    		try {
    			String extCon = " STATUS IN ('O','C') ";
    			_ToDetDAO.setmLogger(mLogger);
    			Hashtable ht = new Hashtable();
    			ht.put(IConstants.PLANT, plant);
    			ht.put(IConstants.TR_TONO, tono);
    			if (isExistOntable(ht,extCon))
    				exists = true;
    		} catch (Exception e) {
    			this.mLogger.exception(this.printLog, "", e);
    		}
    		return exists;
    	}
        
    	public boolean isExistTONO(String tono, String plant) throws Exception {
    		boolean exists = false;
    		try {
    			_ToDetDAO.setmLogger(mLogger);
    			Hashtable ht = new Hashtable();
    			ht.put(IConstants.PLANT, plant);
    			ht.put(IConstants.TR_TONO, tono);
    			if (isExistOntable(ht, ""))
    				exists = true;
    		} catch (Exception e) {
    			this.mLogger.exception(this.printLog, "", e);
    		}
    		return exists;
    	}
        
        private boolean isExistOntable(Hashtable ht, String extCon) throws Exception {
    		boolean exists = false;
    		try {
    			_ToDetDAO.setmLogger(mLogger);
    			if (_ToDetDAO.getCountToNo(ht,extCon) > 0)
    				exists = true;
    		} catch (Exception e) {
    			this.mLogger.exception(this.printLog, "", e);
    		}
    		return exists;
    	}
        
        public int getMaxToLnNo(String tono, String plant) throws Exception {
    		int maxDoLnNo = 0;
    		try {
    			_ToDetDAO.setmLogger(mLogger);
    			
    			Hashtable ht = new Hashtable();
    			ht.put(IConstants.PLANT, plant);
    			ht.put(IConstants.TR_TONO, tono);
    			
    			maxDoLnNo = _ToDetDAO.getMaxToLnNo(ht);
    			
    		} catch (Exception e) {
    			this.mLogger.exception(this.printLog, "", e);
    		}
    		
    		return maxDoLnNo;
    	}
        
  	
        
        
      
        
 //Start code by Bruhan for transfer pick issue(random) on 9 May 2013
        
  public ArrayList getTODetDetails(String Plant,String TONO,String item) {
 			ArrayList al = null;

 			try {
 				al = _ToDetDAO.getTODetDetails(Plant, TONO, item);
 				 
 			} catch(Exception e ){
 	    		this.mLogger.exception(this.printLog, "", e);
 	    	}
 			
 	    return al;
 	    }       
        
 //End code by Bruhan for transfer pick issue(random) on 9 May 2013
  
  public String getPickingItemDetailsTransfer(String aPlant, String aTono,String aItem,String user) {
		String xmlStr = "";
		ArrayList al = null;
		try {
			
			al = _ToDetDAO.selectPickItemDetails(aPlant, aTono,aItem);
			if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("PickitemDetailsTransfer total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ITEM", (String) map.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("ITEMDESC",  StrUtils.replaceCharacters2Send((String) map.get("ITEMDESC")));
					xmlStr += XMLUtils.getXMLNode("UOM", (String) map.get("UOM"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("PickitemDetailsTransfer");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
  
  public String getReceivingItemDetailsTransfer(String aPlant, String aTono,String aItem,String user) {
		String xmlStr = "";
		ArrayList al = null;
		try {
			
			al = _ToDetDAO. selectReceiveItemDetails(aPlant, aTono,aItem);
			if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("PickitemDetailsTransfer total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ITEM", (String) map.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("ITEMDESC",  StrUtils.replaceCharacters2Send((String) map.get("ITEMDESC")));
					xmlStr += XMLUtils.getXMLNode("UOM", (String) map.get("UOM"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("PickitemDetailsTransfer");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

  
  public String process_ToPickingForPDARandom(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = process_Wms_ToPicking_Random(obj);

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
					"Error in Consignment picking Product : "
							+ obj.get(IConstants.ITEM) + " Order");
			return xmlStr;
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  picked successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in process_ToPicking  Product : "
							+ obj.get(IConstants.ITEM) + " Order");
		}
		return xmlStr;
	}
  
  public boolean process_Wms_ToPicking_Random(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsToPickingRandom();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}
  
  public ArrayList getTODetDetailsRandom(String Plant,
			String TONO,String item) {
    	ArrayList al = null;

		try {
			al = _ToDetDAO.getTODetDetailsRandom(Plant, TONO, item);
			 
		} catch(Exception e ){
    		this.mLogger.exception(this.printLog, "", e);
    	}
		
    return al;
    }
  public ArrayList getTODetDetailsRecvRandom(String Plant,
			String TONO,String item) {
  	ArrayList al = null;

		try {
			al = _ToDetDAO.getTODetDetailsRecvRandom(Plant, TONO, item);
			 
		} catch(Exception e ){
  		this.mLogger.exception(this.printLog, "", e);
  	}
		
  return al;
  }


  
  public String getTransOrderItemDetailsRandom(String plant, String tono,
			Boolean isForReceive) {

		String xmlStr = "";
		ArrayList al = null;
		String receivedsql = "";
		if (isForReceive) {
			receivedsql = "SUM(ISNULL(qtyrc,0)) as QTYPICK";
			              
		} else {
			receivedsql = "SUM(ISNULL(QTYPICK,0)) AS QTYPICK";
			
		}
		String query = " TONO ,  ITEM," + " ITEMDESC, SUM(ISNULL(QTYOR,0)) QTYOR ,"
				+ receivedsql + ",UNITMO ,ISNULL(USERFLD2,'') AS REMARKS ";
		String extCond = "AND lnstat <> 'C' ";
		if (!isForReceive) {
			extCond = extCond + " AND pickStatus <> 'C' GROUP BY TONO,ITEM,ITEMDESC,UNITMO,ISNULL(USERFLD2,'') ";
		}
		
		extCond = extCond + " ORDER BY ITEM ";
		
		Hashtable ht = new Hashtable();
		ht.put("TONO", tono);
		ht.put("PLANT", plant);
		try {
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToDetDAO.selectToDet(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("tono", (String) map
							.get("TONO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("QTYPICK")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) map
							.get("REMARKS"));

					xmlStr += XMLUtils.getXMLNode("fromLoc", _ToHdrDAO
							.getLocation(plant, tono, "FROMWAREHOUSE"));
					xmlStr += XMLUtils.getXMLNode("toLoc", _ToHdrDAO
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
  
  public String getBatchDetailsForTORandom(String plant, 
			String orderNo, String item, String batch)
			throws Exception {
		String result = "";
		String result1 = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			_ToHdrDAO.setmLogger(mLogger);
			ToDetDAO _todetDAO = new ToDetDAO();
			_todetDAO.setmLogger(mLogger);
			alData = _todetDAO.getToOrderBatchListToRecvRandom(plant, orderNo,
					item,batch);
			if (alData.size() > 0) {
				int totalValue = 0;
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				for (Map<String, String> hashMap : alData) {

					String sBatch = (String) hashMap.get("batch");
					String spickQty = (String) hashMap.get("pickQty");
					String srecvQty = (String) hashMap.get("recQty");

					double availQty = Double.parseDouble(spickQty)
							- Double.parseDouble(srecvQty);
					availQty = StrUtils.RoundDB(availQty, IConstants.DECIMALPTS);
					if (availQty > 0) {
						totalValue++;
						result1 += XMLUtils.getStartNode("record");
						result1 += XMLUtils.getXMLNode("batchId", hashMap
								.get("batch"));
						result1 += XMLUtils.getXMLNode("qty", (StrUtils.formatNum(String.valueOf(availQty))));
						result1 += XMLUtils.getXMLNode("uom", itemMstDAO
								.getItemUOM(plant, item));
						
						result1 += XMLUtils.getXMLNode("loc", hashMap
								.get("loc"));
				
						result1 += XMLUtils.getEndNode("record");
					}
				}
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ totalValue + "'");
				result += result1;
				result += XMLUtils.getEndNode("BatchDetails");
			} else {
				result = XMLUtils.getXMLMessage(1, "Batch Detiails Not found");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
	}
  
  public String getBatchDetailsByBatchTORandom(String plant, 
			String orderNo, String item, String batch)
			throws Exception {
		String result = "";
		String result1 = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			_ToHdrDAO.setmLogger(mLogger);
			ToDetDAO _todetDAO = new ToDetDAO();
			_todetDAO.setmLogger(mLogger);
			alData = _todetDAO.getToOrderBatchListByBatchToRecvRandom(plant, orderNo,
					item,batch);
			if (alData.size() > 0) {
				int totalValue = 0;
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				for (Map<String, String> hashMap : alData) {

					String sBatch = (String) hashMap.get("batch");
					String spickQty = (String) hashMap.get("pickQty");
					String srecvQty = (String) hashMap.get("recQty");

					double availQty = Double.parseDouble(spickQty)
							- Double.parseDouble(srecvQty);
					availQty = StrUtils.RoundDB(availQty, IConstants.DECIMALPTS);
					if (availQty > 0) {
						totalValue++;
						result1 += XMLUtils.getStartNode("record");
						result1 += XMLUtils.getXMLNode("batchId", hashMap
								.get("batch"));
						result1 += XMLUtils.getXMLNode("qty", (StrUtils.formatNum(String.valueOf(availQty))));
						result1 += XMLUtils.getXMLNode("uom", itemMstDAO
								.getItemUOM(plant, item));
						
						result1 += XMLUtils.getXMLNode("loc", hashMap
								.get("loc"));
				
						result1 += XMLUtils.getEndNode("record");
					}
				}
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ totalValue + "'");
				result += result1;
				result += XMLUtils.getEndNode("BatchDetails");
			} else {
				result = XMLUtils.getXMLMessage(1, "Batch Detiails Not found");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
	}

  public String getBatchDetailsForRecvQtyTORandom(String plant, 
			String orderNo, String item, String loc,String batch)
			throws Exception {
		String result = "";
		String result1 = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		java.sql.Connection con = null;

		try {
			con = DbBean.getConnection();
			_ToHdrDAO.setmLogger(mLogger);
			ToDetDAO _todetDAO = new ToDetDAO();
			_todetDAO.setmLogger(mLogger);
			alData = _todetDAO.getToOrderBatchListToRecvQtyRandom(plant, orderNo,
					item,loc,batch);
			if (alData.size() > 0) {
				int totalValue = 0;
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				for (Map<String, String> hashMap : alData) {

					String sBatch = (String) hashMap.get("batch");
					String spickQty = (String) hashMap.get("pickQty");
					String srecvQty = (String) hashMap.get("recQty");

					double availQty = Double.parseDouble(spickQty)
							- Double.parseDouble(srecvQty);
					availQty = StrUtils.RoundDB(availQty, IConstants.DECIMALPTS);
					if (availQty > 0) {
						totalValue++;
						result1 += XMLUtils.getStartNode("record");
						result1 += XMLUtils.getXMLNode("batchId", hashMap
								.get("batch"));
						result1 += XMLUtils.getXMLNode("qty", (StrUtils.formatNum(String.valueOf(availQty))));
						result1 += XMLUtils.getXMLNode("uom", itemMstDAO
								.getItemUOM(plant, item));
						
						result1 += XMLUtils.getXMLNode("loc", hashMap
								.get("loc"));
				
						result1 += XMLUtils.getEndNode("record");
					}
				}
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ totalValue + "'");
				result += result1;
				result += XMLUtils.getEndNode("BatchDetails");
			} else {
				result = XMLUtils.getXMLMessage(1, "Batch Detiails Not found");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
	}
      
  public String getTransOrderItemDetailsRecvRandom(String plant, String tono,
			Boolean isForReceive) {

		String xmlStr = "";
		ArrayList al = null;
		String receivedsql = "";
		
		
		
		String query = " TONO ,  ITEM," + " ITEMDESC, SUM(ISNULL(qtyrc,0)) as QTYRECV,SUM(ISNULL(QTYPICK,0)) AS QTYPICK,SUM(ISNULL(QTYOR,0)) AS QTYOR"
			+ ",UNITMO ,ISNULL(USERFLD2,'') AS REMARKS ";
		String extCond = "AND lnstat <> 'C' ";
		//if (!isForReceive) {
			extCond = extCond + " GROUP BY TONO,ITEM,ITEMDESC,UNITMO,ISNULL(USERFLD2,'')";
		//}
		
		extCond = extCond + " ORDER BY ITEM ";
		
		Hashtable ht = new Hashtable();
		ht.put("TONO", tono);
		ht.put("PLANT", plant);
		try {
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			al = _ToDetDAO.selectToDet(query, ht, extCond);

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
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) StrUtils
							.replaceCharacters2SendPDA(map.get("ITEMDESC")
									.toString()));
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("QTYPICK")));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtyrecv", StrUtils.formatNum((String) map
							.get("QTYRECV")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) map
							.get("REMARKS"));

					xmlStr += XMLUtils.getXMLNode("fromLoc", _ToHdrDAO
							.getLocation(plant, tono, "FROMWAREHOUSE"));
					xmlStr += XMLUtils.getXMLNode("toLoc", _ToHdrDAO
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
  
  
  public String process_ToReceiveMaterialForPDARandom(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			
			flag = process_Wms_ToReceivingRandom(obj);

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

  
  public boolean process_Wms_ToReceivingRandom(Map map) throws Exception {

		boolean flag = false;
		
		WmsTran tran = new WmsToReceiveMaterialRandom();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);

		return flag;
	}

    /**********************  Modification History  ******************************
     *** Bruhan,Nov 10 2014,Description: To check update quantity less than picking quantity,add condition for update TODET
     * **** Bruhan,March 12 2015,Description: To remove number format in qtyOr,qtyRc,qtyPick
	*/ 
 public boolean updateToDetDetails(Hashtable ht) throws Exception {
	 	boolean flag = false;
		boolean updateHdrFlag=false;
		boolean isExitFlag=false;
		String qty =ht.get("ORDQTY").toString(); 
		String PrdRemarks =ht.get(IDBConstants.DODET_COMMENT1).toString(); 
		Hashtable htUpdatetodet = new Hashtable();
		htUpdatetodet.clear();
		htUpdatetodet.put(IDBConstants.PLANT, ht.get("PLANT"));
		htUpdatetodet.put(IDBConstants.TODET_TONUM, ht.get("TONO"));
		htUpdatetodet.put("TOLNNO", ht.get("TOLNNO"));
		htUpdatetodet.put(IDBConstants.ITEM, ht.get("ITEM"));
	    try {
		//check update quantity less than picking quantity
	    String q = "";
	    String qtyOr="",qtyPick="",qtyRc="",pickstatus="",lnstat="";
		Hashtable htCond = new Hashtable();
		htCond.put("PLANT", ht.get("PLANT"));
		htCond.put("tono",  ht.get("TONO"));
		htCond.put("tolnno", ht.get("TOLNNO"));
		htCond.put("item", ht.get("ITEM"));
		q = " isnull(qtyor,0) qtyor,isnull(qtyPick,0) as qtyPick,isnull(qtyrc,'') qtyrc,isnull(pickstatus,'') pickstatus,isnull(lnstat,'') lnstat ";
		_ToHdrDAO.setmLogger(mLogger);
		_ToDetDAO.setmLogger(mLogger);
		ArrayList al = _ToDetDAO.selectToDet(q, htCond,	" and plant <> ''");
		for (int i = 0; i < al.size(); i++) {
			Map m = (Map) al.get(i);
			//qtyOr = StrUtils.formatNum((String) m.get("qtyor"));
			//qtyPick = StrUtils.formatNum((String) m.get("qtyPick"));
			//qtyRc = StrUtils.formatNum((String) m.get("qtyrc"));
			qtyOr = (String) m.get("qtyor");
			qtyPick = (String) m.get("qtyPick");
			qtyRc = (String) m.get("qtyrc");
			pickstatus =(String) m.get("pickstatus");
			lnstat = (String) m.get("lnstat");
		}
		 
		if(Double.parseDouble(qty) < Double.parseDouble(qtyPick))
		{
			throw new Exception(" Order Qty entered should not be less than already Picked Qty:"+qtyPick);
		}
		else if(Double.parseDouble(qty) < Double.parseDouble(qtyRc))
		{
			throw new Exception(" Order Qty entered should not be less than already Received Qty:"+qtyRc);
		}
		
		
		String query="";
	    ToDetDAO _ToDetDAO = new ToDetDAO();
		ToHdrDAO _ToHdrDAO = new ToHdrDAO();
		String updateToHdr = "";
		Hashtable htCondiToHdr = new Hashtable();
		htCondiToHdr.put("PLANT", ht.get("PLANT"));
		htCondiToHdr.put("tono", ht.get("TONO"));
	    //Update Todet and check Tohdr condition
	    if(pickstatus.equals("C") && Double.parseDouble(qtyOr)!=Double.parseDouble(qty))
	    {
	    	
	    	if(lnstat.equals("C") && Double.parseDouble(qtyPick)>0 && Double.parseDouble(qtyRc) > 0)
	    	{
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='O',LNSTAT='O'";
	    	}
	    	else  if(lnstat.equals("C") && Double.parseDouble(qtyPick)>0 && Double.parseDouble(qtyRc)== 0)
	    	{
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='O',LNSTAT='N'";
	    	}
	    	else  if(lnstat.equals("C") && Double.parseDouble(qtyPick)==0 && Double.parseDouble(qtyRc)== 0)
	    	{
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='N',LNSTAT='N'";
	    	}
	    	else
	    	{
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='O'";
	    	}
	    	
	    	
	    	_ToDetDAO.setmLogger(mLogger);
			flag = _ToDetDAO.update(query,htUpdatetodet,"");
	    	isExitFlag =_ToHdrDAO.isExisit(htCondiToHdr," isnull(STATUS,'') in ('C') and isnull(PICKSTAUS,'') in ('C')");
	    	if (isExitFlag && Double.parseDouble(qtyOr)!=Double.parseDouble(qty)){
				updateToHdr = "set  status='O',PickStaus='O' ";
				updateHdrFlag=true;
			}
	    	
	    	isExitFlag =_ToHdrDAO.isExisit(htCondiToHdr," isnull(STATUS,'') in ('O') and isnull(PICKSTAUS,'') in ('C')");
	    	if (isExitFlag && Double.parseDouble(qtyOr)!=Double.parseDouble(qty)){
				updateToHdr = "set  status='O',PickStaus='O' ";
				updateHdrFlag=true;
			}
	    }
	    else if(pickstatus.equals("O") && Double.parseDouble(qty)==Double.parseDouble(qtyPick))
	    {
	    	if(lnstat.equals("O") && Double.parseDouble(qty)==Double.parseDouble(qtyRc)){
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='C',LNSTAT='C'";
	          }
	    	else
	    	{
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='C'";
	    	}
	    	
	    	_ToDetDAO.setmLogger(mLogger);
			flag = _ToDetDAO.update(query,htUpdatetodet,"");
	    	isExitFlag = _ToDetDAO.isExisit(htCondiToHdr,	"lnstat in ('O','N')");
	    	if (isExitFlag)
	    	{
	    		updateToHdr = "set Status='O', pickstaus='C'";
				updateHdrFlag=true;
	    	}
			else
			{
				updateToHdr = "set  Status='C',pickstaus='C'";
				updateHdrFlag=true;
	    	}
	    	
	    	
	    }
	    
	    else
	    {
	    	if(pickstatus.equals("N") && Double.parseDouble(qty)==0 && Double.parseDouble(qtyPick)==0 && Double.parseDouble(qtyRc)==0)
	    	{
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"',PICKSTATUS='C',LNSTAT='C'";
		    	_ToDetDAO.setmLogger(mLogger);
				flag = _ToDetDAO.update(query,htUpdatetodet,"");
		    	isExitFlag = _ToDetDAO.isExisit(htCondiToHdr,	"lnstat in ('O','N')");
		    	if (isExitFlag)
		    	{
		    		updateToHdr = "set  Status='O',PickStaus='O' ";
					updateHdrFlag=true;
		    	}
				else
				{
					updateToHdr = "set  Status='C',PickStaus='C' ";
					updateHdrFlag=true;
		    	}
	    	}
	    	else
	    	{
	    		query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"'";
		    	_ToDetDAO.setmLogger(mLogger);
				flag = _ToDetDAO.update(query,htUpdatetodet,"");
		    	updateHdrFlag=false;
	    	}
	    }
	           //update TOHDR
	    		if(flag){
	    			if(updateHdrFlag==true){
	    				flag = _ToHdrDAO.update(updateToHdr, htCondiToHdr, "");
	    			}
	    		//update TO_PICK
				Hashtable htConToPick = new Hashtable();
				htConToPick.put("PLANT", ht.get("PLANT"));
				htConToPick.put("tono",  ht.get("TONO"));
				htConToPick.put("tolno", ht.get("TOLNNO"));
				htConToPick.put("item", ht.get("ITEM"));
                boolean isExists = _ToDetDAO.isExisitTOPick(htConToPick);
                if(isExists){
			        query = "set ORDQTY=" + qty;
			        flag = _ToDetDAO.updateToPickTable(query,htConToPick,"");
	             }
               //update RECVDET
                Hashtable htConToRecv = new Hashtable();
                htConToRecv.put("PLANT", ht.get("PLANT"));
                htConToRecv.put("pono",  ht.get("TONO"));
                htConToRecv.put("lnno", ht.get("TOLNNO"));
                htConToRecv.put("item", ht.get("ITEM"));
                boolean isExistsRecv = _ToDetDAO.isExisitTOReceive(htConToRecv);
                if(isExistsRecv){
			        query = "set ORDQTY=" + qty;
			        flag = _ToDetDAO.updateToRecvTable(query,htConToRecv,"");
	             }
	       }
          
		} catch (Exception e) {

			throw e;
		}
		return flag;
	}
  
 /*public boolean updateToDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;
		String qty =ht.get("ORDQTY").toString(); 
		String PrdRemarks =ht.get(IDBConstants.DODET_COMMENT1).toString(); 
		Hashtable htUpdatedodet = new Hashtable();
		htUpdatedodet.clear();
		htUpdatedodet.put(IDBConstants.PLANT, ht.get("PLANT"));
		htUpdatedodet.put(IDBConstants.TODET_TONUM, ht.get("TONO"));
		htUpdatedodet.put("TOLNNO", ht.get("TOLNNO"));
		htUpdatedodet.put(IDBConstants.ITEM, ht.get("ITEM"));
	try {
			String query = "set QTYOR=" + qty + ",COMMENT1='" +PrdRemarks+"'";
			_ToDetDAO.setmLogger(mLogger);
			flag = _ToDetDAO.update(query,htUpdatedodet,"");
          
		} catch (Exception e) {

			throw e;
		}
		return flag;
	}*/
  
  public ArrayList getFromLocDetailsForTono(String plant, String tono) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			

			alResult = new LocMstBeanDAO().getLocDetails(
					"LOC,LOCDESC,USERFLD1,ISACTIVE,COMNAME,RCBNO,ADD1,ADD2,ADD3,ADD4,COUNTRY,ZIP,TELNO,FAX,CHKSTATUS,ISOUTLET", htCondition,
					" and loc  =(select FROMWAREHOUSE FROM ["+plant+"_TOHDR] where plant='"+plant+"' and tono  ='" + tono + "')")  ;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e); 
		}

		return alResult;
	}
  
//start code added by Bruhan for transfer order reversal on 20 August 2013  
public ArrayList listTransferSummaryReverseTODET(String plant, String aTONO)
	throws Exception {
String q = "";
ArrayList al = null;

try {
	Hashtable htCond = new Hashtable();
	htCond.put("a.PLANT", plant);
	htCond.put("a.TONO", aTONO);
	q = " a.tono,a.tolnno,a.pickstatus,a.item,isnull(max(a.qtyor),0) as qtyor,isnull(sum(b.pickqty),0) as qtypick,b.batch,"+
              "(select itemdesc from "+plant+"_ITEMMST where plant = a.plant and ITEM= a.item) as itemdesc,ISNULL(UNITMO,'') as UNITMO" ; 
              
	_ToDetDAO.setmLogger(mLogger);
	al = _ToDetDAO.selectReverseToDet(q, htCond,
			" and a.plant <> '' and ISNULL(a.lnstat,'') ='O' and a.tolnno=b.tolno and a.tono=b.tono and a.item=b.item  group  by a.tono,a.tolnno,a.item,a.pickstatus,a.PLANT,b.batch,UNITMO");

	
} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
	throw e;
}

return al;
}

public List<Map<String, String>> listConsignmentReverseGINO(String plant, String tono, String gino){
	List<Map<String, String>> returnOrderList = null;
try {
	returnOrderList = _ToHdrDAO.listConsignmentReverseGINO(plant, tono, gino);
} catch (Exception e) {
	e.printStackTrace();
}
return returnOrderList;
}

public boolean process_TOReversal(Map obj) throws Exception {
	boolean flag = false;
	UserTransaction ut = null;

	try {
		ut = com.track.gates.DbBean.getUserTranaction();
		ut.begin();
		
		flag = process_Wms_TOReversal(obj);
		
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

public boolean process_Wms_TOReversal(Map map) throws Exception {

	boolean flag = false;

	WmsTran tran = new WmsTOReversal();
	((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
	flag = tran.processWmsTran(map);

	return flag;
}

//End code added by Bruhan for transfer order reversal on 20 August 2013
 

//Start code by Bruhan for transfer order by product on 23sep2013   
public ArrayList listTransferSummaryByProd(String item, String plant)
   throws Exception {
   	String q = "";
   	String link = "";
   	String result = "";
   	String where = "";
   	ArrayList al = null;
   	try {
   		Hashtable htCond = new Hashtable();
   		htCond.put("a.PLANT", plant);
   		htCond.put("a.item", item);
   		q = "a.tono,tolnno,pickstatus,item,isnull(qtyor,0) as qtyor,isnull(qtypick,0) as qtypick,isnull(fromwarehouse,'') as fromloc,isnull(towarehouse,'') as toloc,isnull(a.userfld4,'') as batch,isnull(custname,'') as cname";
   		_ToDetDAO.setmLogger(mLogger);
   		al = _ToDetDAO.selectToDetailsbyProd(q, htCond," a.plant <> '' and pickstatus <> 'C' and a.tono=b.tono and a.PLANT=b.PLANT order by tolnno");
   		} catch (Exception e) {
   this.mLogger.exception(this.printLog, "", e);
   throw e;
   	}

   	return al;
}
//End code by Bruhan for transfer order by product on 23sep2013

//start code by Bruhan for transfer order pick receive at one step on 16 jan 15
public boolean process_ToPickReceiveForPC(Map obj) throws Exception {
	boolean flag = false;
	UserTransaction ut = null;

	try {
		ut = com.track.gates.DbBean.getUserTranaction();
		ut.begin();
		
		flag = process_Wms_ToPickReceive(obj);
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

public boolean process_Wms_ToPickReceive(Map map) throws Exception {

	boolean flag = false;

	WmsTran tran = new WmsTOPickReceive();
	((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
	flag = tran.processWmsTran(map);

	return flag;
}

	public String getOrderDate(String aPlant, String aTONO) throws Exception {
		String OrderDate = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("TONO", aTONO);

		String query = " ISNULL( " + "collectionDate" + ",'') as " + "date" + " ";
		_ToHdrDAO.setmLogger(mLogger);
		Map m = _ToHdrDAO.selectRow(query, ht);

		OrderDate = (String) m.get("date");
		return OrderDate;
	}
	
public boolean saveWMSToHdrDetails(Hashtable ht) throws Exception {

		boolean flag = false;

		try {
			_ToHdrDAO.setmLogger(mLogger);
			flag = _ToHdrDAO.insertToHdr(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Consignment order created already");
		}

		return flag;
	}
public boolean saveToMultiRemarks(Hashtable ht) throws Exception {
	boolean flag = false;
	try {
		_ToDetDAO.setmLogger(mLogger);
		flag = _ToDetDAO.insertToMultiRemarks(ht);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}

	return flag;
}

public Map getTOReceiptInvoiceHdrDetails(String aplant,String orderType) throws Exception {
    Map m =  new HashMap();
   
    m=_ToHdrDAO.getTOReciptInvoiceHeaderDetails(aplant,orderType);
     return m;
}

public String getOBDiscountSelectedItemByCustomer(String aPlant, String custcode,String aItem,String aType) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _ToHdrDAO.getOBDiscountSelectedItemByCustomer(aPlant, custcode,aItem,aType);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

public String getConvertedUnitCostForProduct(String aPlant, String toNO,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _ToHdrDAO.getUnitCostBasedOnCurIDSelectedForOrder(aPlant, toNO,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

public String getConvertedUnitCostForProductWTC(String aPlant, String toNO,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _ToHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTC(aPlant, toNO,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

public String getminSellingConvertedUnitCostForProductByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _ToHdrDAO.getminSellingUnitCostBasedOnCurIDSelectedForOrderByCurrency(aPlant, currencyID,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

		//Azees feb_21
		public ArrayList listConsignmentReverseTODET(String plant, String aTONO)
		throws Exception {
		String q = "";
		ArrayList al = null;
		
		try {
		Hashtable htCond = new Hashtable();
		htCond.put("a.PLANT", plant);
		htCond.put("a.TONO", aTONO);
		q = " a.tono,a.tolnno,a.pickstatus,a.item,isnull(max(a.qtyor),0) as qtyor,isnull(sum(b.pickqty),0) as qtypick,isnull((a.qtyac),0) as invoiceqty,isnull((a.QtyPick),0) as picktotqty,b.batch,isnull(account_name,'')account_name,UNITPRICE,CURRENCYUSEQT,DISCOUNT_TYPE,DISCOUNT,"+
		"(select itemdesc from "+plant+"_ITEMMST where plant = a.plant and ITEM= a.item) as itemdesc,ISNULL(UNITMO,'') as UNITMO" ;
		
		_ToDetDAO.setmLogger(mLogger);
		al = _ToDetDAO.selectReverseToDet(q, htCond,
		" and a.plant <> '' and a.tolnno=b.tolno and a.tono=b.tono and a.item=b.item group by a.tono,a.tolnno,a.item,a.pickstatus,a.PLANT,b.batch,UNITMO,account_name,UNITPRICE,CURRENCYUSEQT,DISCOUNT_TYPE,DISCOUNT,a.qtyac,a.QtyPick");
		
		
		} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
		}
		
		return al;
		}
		
		public String getOrderDateOnly(String aPlant, String aTONO) throws Exception {
			String OrderDate = "";

			Hashtable ht = new Hashtable();
			ht.put("PLANT", aPlant);
			ht.put("TONO", aTONO);

			String query = " ISNULL( " + "collectionDate" + ",'') as " + "date" + " ";
			_ToHdrDAO.setmLogger(mLogger);
			Map m = _ToHdrDAO.selectRow(query, ht);

			OrderDate = (String) m.get("date");
			return OrderDate;
		}
		
		public String getCurrencyUseQT(String plant,String toNO) throws Exception{
	    	String currencyUseQT = "";
	    	try{
	    		currencyUseQT = _ToHdrDAO.getCurrencyUseQT(plant,toNO);
	    	}
	    	catch(Exception e ){
	    		this.mLogger.exception(this.printLog, "", e);
	    	}
	    	
	    	return currencyUseQT;
	    }

}
 
