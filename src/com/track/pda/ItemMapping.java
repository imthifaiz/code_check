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

import com.track.constants.IConstants;
import com.track.db.util.ItemUtil;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class ItemMapping extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 4634283840157236680L;

	private static final String CONTENT_TYPE = "text/html";

	ItemUtil _itemUtil;

	String action = "", _sKeyItem = "", _sMapItem = "", _sItemDesc = "",
			_xmlStr = "";

	public void init() throws ServletException {
		_itemUtil = new ItemUtil();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		action = StrUtils.fString(request.getParameter("action"));
		_sKeyItem = StrUtils.fString(request.getParameter("KEYITEM"))
				.toUpperCase();
		_sMapItem = StrUtils.fString(request.getParameter("MAPITEM"))
				.toUpperCase();

		try {
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
					.getParameter("PLANT")), StrUtils.fString(request
							.getParameter("LOGIN_USER")) + " PDA_USER"));
			_itemUtil.setmLogger(mLogger);
			
			if (action.equalsIgnoreCase("check_key_item")) {
				_xmlStr = "";
				boolean itemExists = _itemUtil.isExistsItemMst(_sKeyItem);
				if (itemExists) {
					_xmlStr = XMLUtils.getXMLMessage(0, _sKeyItem);
				} else {
					_xmlStr = XMLUtils
							.getXMLMessage(1,
									"Key Product number not valid. Please try with correct item number");
				}
			}

			// check whether the map item is mapped already
			else if (action.equalsIgnoreCase("check_map_item")) {
				boolean mapExists = _itemUtil.isExistsItemMap(_sMapItem);

				if (!mapExists) {
					_xmlStr = XMLUtils.getXMLMessage(0, _sMapItem);
				} else {
					_xmlStr = XMLUtils.getXMLMessage(1,
							"Map Product exists already");
				}
			}

			else if (action.equalsIgnoreCase("load_map_item")) {
				_xmlStr = "";
				ArrayList arrList = _itemUtil.getKeyItem4MapItem(_sMapItem);
				_xmlStr += XMLUtils.getXMLHeader();
				_xmlStr += XMLUtils.getStartNode("record");
				_xmlStr += XMLUtils.getXMLNode("KEYITEM", (String) arrList
						.get(0));
				_xmlStr += XMLUtils.getXMLNode("DESCRIPTION", StrUtils
						.replaceCharacters2Send((String) arrList.get(1)));
				_xmlStr += XMLUtils.getXMLNode("MAPITEM", _sMapItem);
				_xmlStr += XMLUtils.getEndNode("record");
			}

			else if (action.equalsIgnoreCase("load_key_item")) {
				_xmlStr = "";
				ArrayList arrayList = _itemUtil.getItemDetails(_sKeyItem);
				_xmlStr += XMLUtils.getXMLHeader();
				_xmlStr += XMLUtils.getStartNode("record");
				_xmlStr += XMLUtils.getXMLNode("DESCRIPTION", StrUtils
						.replaceCharacters2Send((String) arrayList.get(1)));
				_xmlStr += XMLUtils.getEndNode("record");
			}

			else if (action.equalsIgnoreCase("do_process")) {
				_xmlStr = "";
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, IConstants.PLANT_VAL);
				ht.put(IConstants.KEY_ITEM, _sKeyItem);
				ht.put(IConstants.MAP_ITEM, _sMapItem);
				boolean flgInsert = _itemUtil.insertItemMap(ht);
				if (flgInsert) {
					_xmlStr = XMLUtils.getXMLMessage(0,
							"Item mapped successfully");
				} else {
					_xmlStr = XMLUtils.getXMLMessage(1,
							"Failed to mapping the item");
				}
			}

		} catch (Exception e) {
		}
		if (_xmlStr.equalsIgnoreCase("")) {
			_xmlStr = XMLUtils.getXMLMessage(1,
					"Process Failed due to Exception");
		}
		out.write(_xmlStr);
		out.close();
	}

	public void destroy() {
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