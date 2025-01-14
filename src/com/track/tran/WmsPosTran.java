package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsPosTran implements WmsTran, IMLogger {

	private boolean printLog = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
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

	public WmsPosTran() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = true;

		if (flag) {
			String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
			if(!nonstocktype.equalsIgnoreCase("Y"))	
		    {
			flag = processInvMst(m);
		    }
			else
			{				
				double actqty = Double.valueOf((String) m.get(IConstants.QTY));
				flag = processMovHis_OUT(m,actqty);
			}				

		}

		
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();

			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.BATCH, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			StringBuffer sql = new StringBuffer(" ");
			List invlist = _InvMstDAO.selectInvMstByCrat(
					"item,loc,userfld4 as batch,qty,crat,ID", htInvMst);
			if (invlist.size()==0){
				throw new Exception("Not enough Inventory Qty available for Product :"+(String)map.get(IConstants.ITEM)+ " in Location :"+(String)map.get(IConstants.LOC));
			}
			String actualqty = "";
			double actqty = 0;
			double lnqty = 0, balancqty = 0;double totalqty=0;
			actualqty = (String) map.get(IConstants.QTY);
			//actualqty = StrUtils.TrunkateDecimalForImportData(actualqty);
			actqty = Double.valueOf(actualqty);
			//Calculate total qty in the loc
			for(int j=0;j<invlist.size();j++)
			{
				Map lineitem = (Map) invlist.get(j);
				String lineitemqty = (String) lineitem.get("qty");
				//lineitemqty = StrUtils.TrunkateDecimalForImportData(lineitemqty);
				totalqty = totalqty +Double.valueOf(lineitemqty);
				
			}
			if(actqty<=totalqty){
	        	double invumqty = Double.valueOf(actqty) * Double.valueOf((String)map.get("UOMQTY"));
	        	actqty=invumqty;
	        	if(invumqty<=totalqty){
			search: for (int i = 0; i < invlist.size(); i++) {
				Map lineitem = (Map) invlist.get(i);
				lineqty = (String) lineitem.get("qty");
				
				//lineqty = StrUtils.TrunkateDecimalForImportData(lineqty);
				lnqty = Double.valueOf(lineqty);
				batch = (String) lineitem.get("batch");
				htInvMst.put(IConstants.USERFLD4, batch);
				htInvMst.put(IDBConstants.INVID, lineitem.get(IDBConstants.INVID));
				if (actqty > 0) {
					
					if (actqty <= lnqty && lnqty > 0) {						
						sql.append(" SET QTY" + " = QTY - " + actqty + " ");

//System.out.println("Inside Actual qty Lesser ");
						flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
						sql.delete(0, sql.length());
						if (flag == true&&actqty>0)
							flag = processMovHis_OUT(map, actqty);
						actqty = 0;					
					}
					if (actqty > lnqty && lnqty > 0) {						
						sql.append(" SET QTY" + " = QTY - " + lnqty + " ");

						flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
						sql.delete(0, sql.length());
						balancqty = actqty - lnqty;
						actqty = balancqty;
						if (flag == true&&lnqty>0)
							flag = processMovHis_OUT(map, lnqty);
//System.out.println("Inside Actual qty greater");
					}else if (actqty == lnqty && lnqty > 0) {
						sql.append(" SET QTY" + " = QTY - " + actqty + " ");

//System.out.println("Inside Actual qty  equal");
						flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
						sql.delete(0, sql.length());
						if (flag == true&&actqty>0)
							flag = processMovHis_OUT(map, actqty);
						actqty = 0;
					}
				}

			}}
			else{
				flag=false;
			}}
			else{
				flag=false;
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public boolean processMovHis_OUT(Map map, double qty) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			if(map.get("DIRTYPE") =="TAXINVOICE_OUT")			
				htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_DIRECT_TAX_INVOICE);
			else if(map.get("DIRTYPE") == TransactionConstants.EDIT_DIRECT_TAX_INVOICE)			
				htRecvHis.put("DIRTYPE", TransactionConstants.EDIT_DIRECT_TAX_INVOICE);
			else
			htRecvHis.put("DIRTYPE", "GOODSISSUE");
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			if((String)map.get(IConstants.UOM)!=null)
			htRecvHis.put(IConstants.UOM,  map.get(IConstants.UOM));

			htRecvHis.put("BATNO", batch);
			htRecvHis.put("QTY", String.valueOf(qty));

			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.MOVHIS_ORDNUM));	


			flag = movHisDao.insertIntoMovHis(htRecvHis);
			
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

}
