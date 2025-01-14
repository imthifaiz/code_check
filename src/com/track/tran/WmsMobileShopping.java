package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;

import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;

import com.track.tables.CATALOGMST;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsMobileShopping implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERINFO;
	private CustMstDAO _custmst = null;
	private DOUtil _doutil = null;
	private DOTransferUtil _DOTransferUtil = null;

	private StrUtils _StrUtils = null;

	DateUtils _dateutils = null;
	String lineqty = "", batch = "", balqty = "";
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	public WmsMobileShopping() {
		_dateutils = new DateUtils();
		_doutil = new DOUtil();
		_DOTransferUtil = new DOTransferUtil();
		_custmst = new CustMstDAO();
		_StrUtils = new StrUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
	    boolean flag = true;
        try{
            if (flag) {

                    flag = processdohdr(m);

            }
            if (flag){
                     flag = processdodet(m);  
            }
       
          }catch(Exception e){
              throw e;
          }
	return flag;
		
	}

	private boolean processdodet(Map m) throws Exception {
		boolean flag = false;
		// TODO Auto-generated method stub
		try {
			int iIndex = 1;
                       
			Vector<CATALOGMST> catloglist = new Vector<CATALOGMST>();
			catloglist = (Vector<CATALOGMST>) m.get("shoppingbag");
                      
			String dono = (String) m.get(IDBConstants.DODET_DONUM);

			Hashtable htdodet = new Hashtable();
			String plant = (String) m.get(IDBConstants.PLANT);
			_doutil.setmLogger(mLogger);
			_DOTransferUtil.setmLogger(mLogger);
			for (Iterator iterator = catloglist.iterator(); iterator.hasNext();) {
				CATALOGMST catalogmst = (CATALOGMST) iterator.next();
				String item = catalogmst.getProductID();
				int qty = (int) catalogmst.getQuantity();
				float price = catalogmst.getPrice();
				String itemDesc = new ItemMstDAO().getItemDesc(plant, item);
				String strqty = String.valueOf(qty);			
				String strprice = String.valueOf(price);
				
				htdodet.put(IDBConstants.PLANT, plant);
				htdodet.put(IDBConstants.DODET_DONUM, dono);
				String uom = new ItemMstDAO().getItemUOM(plant, item);
				htdodet.put(IDBConstants.DODET_ITEM, item);
				htdodet.put("ITEMDESC", _StrUtils.InsertQuotes(itemDesc));
				htdodet.put("UNITMO", uom);
				htdodet.put(IDBConstants.DODET_ITEM_DESC, _StrUtils.InsertQuotes(itemDesc));

				htdodet.put(IDBConstants.DODET_JOB_NUM, "");
				htdodet.put(IDBConstants.DODET_CUST_NAME, "");// add customer
				// name
				htdodet.put(IDBConstants.DODET_QTYOR, strqty);
				htdodet.put(IDBConstants.DODET_UNITPRICE, strprice);
				htdodet.put(IDBConstants.DODET_QTYIS, "0");

				htdodet.put(IDBConstants.DODET_DOLNNO, String.valueOf(iIndex));
				htdodet.put(IDBConstants.TRAN_DATE, _dateutils.getDateinyyyy_mm_dd(_dateutils.getDate()));

				iIndex += 1;
				
			    	htdodet.put(IDBConstants.DODET_LNSTATUS, "N");
			    	htdodet.put(IDBConstants.DODET_PICKSTATUS, "N");
			 

				flag = _doutil.saveDoDetDetails(htdodet);
				if (flag)
					flag = _DOTransferUtil.saveDoTransferDetDetails(htdodet);
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.mLogger.exception(this.printLog, "", e);
                        System.out.println("Exception :: " + e.toString());
			throw e;
		}

		return flag;
	}

	public boolean processdohdr(Map map) throws Exception {

		boolean flag = false;
		String extCond = "", dono = "", deldate = "", collectionTime = "";

		try {

			dono = (String) map.get(IDBConstants.DODET_DONUM);

			deldate = _dateutils.getDate();
			collectionTime = _dateutils.getTimeHHmm();
			boolean result = false;
			String custCode = "", custName = "", address = "", address2 = "", address3 = "", HPNO = "";
			Hashtable ht = new Hashtable();

			ht.put(IDBConstants.USER_ID, (String) map
							.get(IDBConstants.USER_ID));
			ht.put(IDBConstants.PLANT, (String) map.get(IDBConstants.PLANT));

			List cutomerdetlst = _custmst
					.selectCustMst(
							"CUSTNO,USER_ID,CNAME,ADDR1,ADDR2,ADDR3,ADDR4,HPNO",
							ht, "");
			_doutil.setmLogger(mLogger);
			_DOTransferUtil.setmLogger(mLogger);
			for (int i = 0; i < cutomerdetlst.size(); i++) {
				Map linemap = (Map) cutomerdetlst.get(0);
				custCode = (String) linemap.get("CUSTNO");
				custName = (String) linemap.get("CNAME");
				address = (String) linemap.get("ADDR1");
				address2 = (String) linemap.get("ADDR2");
				address3 = (String) linemap.get("ADDR3");
				HPNO = (String) linemap.get("HPNO");

			}
			ht.remove(IDBConstants.USER_ID);
			ht.put(IDBConstants.PLANT, map.get(IDBConstants.PLANT));
			ht.put(IDBConstants.DOHDR_DONUM, dono);
			ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
			ht.put(IDBConstants.DOHDR_CUST_NAME, custName);
			ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, custName);
			ht.put(IDBConstants.DOHDR_JOB_NUM, "");
			
			ht.put("REMARK1", map.get(IDBConstants.REMARKS));
			ht.put(IDBConstants.DOHDR_CONTACT_NUM, HPNO);
			ht.put(IDBConstants.ORDERTYPE, "Mobile Order");
			ht.put(IDBConstants.DOHDR_ADDRESS, address);
			ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
			ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
			ht.put(IDBConstants.DOHDR_COL_DATE, _dateutils.getDate());
			ht.put(IDBConstants.DOHDR_COL_TIME, _dateutils.getTimeHHmm());
			ht.put(IDBConstants.STATUS, "N");
			ht.put(IDBConstants.CREATED_AT, _dateutils.getDateTime());
			ht.put(IDBConstants.CURRENCYID, "SGD");
			ht.put(IDBConstants.CREATED_BY, (String) map.get(IDBConstants.USER_ID));
			ht.put(IDBConstants.DELIVERYDATE, map.get(IDBConstants.DELIVERYDATE));
			ht.put(IDBConstants.TIMESLOTS, map.get(IDBConstants.TIMESLOTS));
			flag = _doutil.saveDoHdrDetails(ht);
			if (flag) {				
				ht.remove(IDBConstants.ORDERTYPE);
				flag = _DOTransferUtil.saveDoTransferHdrDetails(ht);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			int qty=0;
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			//htRecvHis.put("DIRTYPE", TransactionConstants.POS_TRANSACTION);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));

			htRecvHis.put("BATNO", "");
			htRecvHis.put("QTY", String.valueOf(qty));

			htRecvHis.put(IDBConstants.CREATED_BY, map
					.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, "");

			htRecvHis.put(IDBConstants.TRAN_DATE, _dateutils
					.getDateinyyyy_mm_dd(_dateutils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, _dateutils.getDateTime());

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

}
