package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.db.util.CCUtil;
import com.track.db.util.ItemUtil;
import com.track.gates.DbBean;
import com.track.gates.defaultsBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class StockTake extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 6384336609117751347L;
	private static final String CONTENT_TYPE = "text/html";
	private ItemUtil _itemUtil;
	private DateUtils _dateUtils;
	private CCUtil _ccUtils;
	private UserTransaction _userTrn;
	private String action = "";
	private String _sItemno = "", _sLoc = "", _sQty = "", _sUserId = "",
			_xmlStr = "";

	public void init() throws ServletException {
		_itemUtil = new ItemUtil();

		_dateUtils = new DateUtils();
		_ccUtils = new CCUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
						.getParameter("LOGIN_USER")) + " PDA_USER"));
		_itemUtil.setmLogger(mLogger);
		_ccUtils.setmLogger(mLogger);
		action = StrUtils.fString(request.getParameter("action"));
		_sItemno = StrUtils.fString(request.getParameter("ITEMNO"))
				.toUpperCase();
		_sLoc = StrUtils.fString(request.getParameter("LOC")).toUpperCase();
		_sQty = StrUtils.fString(request.getParameter("QTY"));
		_sUserId = StrUtils.fString(request.getParameter("USERID"))
				.toUpperCase();
		try {

			if (action.equalsIgnoreCase("check_item")) {
				_xmlStr = "";
				boolean itemExists = _itemUtil.isExistsItemMst(_sItemno);
				if (itemExists) {
					_xmlStr = XMLUtils.getXMLMessage(0, _sItemno);
				} else {
					ArrayList arrKeyItem = _itemUtil
							.getKeyItem4MapItem(_sItemno);
					if (arrKeyItem.size() > 0) {
						String keyItem = (String) arrKeyItem.get(0);
						if (keyItem.length() > 0) {
							boolean keyItemExists = _itemUtil
									.isExistsItemMst(keyItem);
							if (keyItemExists) {
								_xmlStr = XMLUtils.getXMLMessage(0, keyItem);
							} else {
								_xmlStr = XMLUtils
										.getXMLMessage(1,
												"Product Number not valid. Please enter correct Product Number");
							}
						} else {
							_xmlStr = XMLUtils
									.getXMLMessage(1,
											"Product Number not valid. Please enter correct Product Number");
						}
					} else {
						_xmlStr = XMLUtils
								.getXMLMessage(1,
										"Product Number not valid. Please enter correct Product Number");
					}
				}
			}

			else if (action.equalsIgnoreCase("loc_item_status")) {
				_xmlStr = "";
				boolean flgInsertLoc = false;
				boolean flg = _itemUtil.isExistsLoc4Item(_sItemno, _sLoc);
				if (flg == false) {
					Hashtable htLoc = new Hashtable();
					htLoc.put(IConstants.PLANT, IConstants.PLANT_VAL);
					htLoc.put(IConstants.ITEM, _sItemno);
					htLoc.put(IConstants.LOC, _sLoc);
					htLoc.put(IConstants.CREATED_AT, _dateUtils.getDateTime());
					htLoc.put(IConstants.CREATED_BY, _sUserId);
					flgInsertLoc = _itemUtil.insertItemLoc(htLoc);
				}
				if ((flgInsertLoc == true) || (flg == true)) {
					_xmlStr = _itemUtil.getItemDetails4Inbound(_sItemno, _sLoc);
				}
				if (_xmlStr.equalsIgnoreCase("")) {
					_xmlStr = XMLUtils.getXMLMessage(1,
							"No Stock for this Item");
				}
			} else if (action.equalsIgnoreCase("add_map_item")) {
				_xmlStr = "";
				boolean flgInsert = false;
				String _sKeyItem = StrUtils.fString(
						request.getParameter("ITEMNO")).toUpperCase();
				String _sMapItem = StrUtils.fString(
						request.getParameter("MAPITEM")).toUpperCase();

				if (!(_itemUtil.isExistsItemMap(_sMapItem))) {
					Hashtable ht = new Hashtable();
					ht.put(IConstants.PLANT, IConstants.PLANT_VAL);
					ht.put(IConstants.KEY_ITEM, _sKeyItem);
					ht.put(IConstants.MAP_ITEM, _sMapItem);
					flgInsert = _itemUtil.insertItemMap(ht);
				} else {
					flgInsert = true;
				}
				if (flgInsert) {
					_xmlStr = XMLUtils.getXMLMessage(0,
							"Item mapped successfully");
				} else {
					_xmlStr = XMLUtils.getXMLMessage(1,
							"Failed to mapping the item");
				}

			}

			else if (action.equalsIgnoreCase("do_process")) {

				defaultsBean _defBean = new defaultsBean();
				_userTrn = DbBean.getUserTranaction();
				_userTrn.begin();
				boolean flgStk = _ccUtils.doStockTakeProcess(_sItemno, _sLoc,
						_sQty, _sUserId);
				if (flgStk == true) {
					try {
						_userTrn.commit();
					} catch (Exception ee) {
					}
					_xmlStr = XMLUtils.getXMLMessage(0, "Process Success");
					_defBean.insertLog(_sUserId, "STOCKTAKE", _sItemno
							+ " Success");
				} else {
					try {
						_userTrn.rollback();
					} catch (Exception ee) {
					}
					_xmlStr = XMLUtils.getXMLMessage(1, "Process Failed");
					_defBean.insertLog(_sUserId, "STOCKTAKE", _sItemno
							+ " Fails");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (_xmlStr.equalsIgnoreCase("")) {
			_xmlStr = XMLUtils.getXMLMessage(1,
					"Process Failed due to Exception");
		}
		out.write(_xmlStr);
		out.close();
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

}