
package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.DOUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsPickingWOInvcheck implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsPicking_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
        DOUtil doutil = new DOUtil();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public WmsPickingWOInvcheck() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
                
             
			flag = processDodetForPick(m);

			if (flag) {
                        
                        System.out.println("Skipped the Inventory Check");
			}
			if (flag) {

				{
					flag = processMovHis_OUT(m);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						MLogger.log(0, "Exception locationTransfer"
								+ e.getMessage());
					}
				}
				if (flag) {

					flag = processMovHis_IN(m);

				}

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processDodetForPick(Map map) throws Exception {
        String ExpiryDate="";
		boolean flag = false;
		try {
			DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			_DoHdrDAO.setmLogger(mLogger);
	  DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
	    _DoTransferHdrDAO.setmLogger(mLogger);
			Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");
			// getQty from podet

			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("dono", map.get("PONO"));
			htCondiPoDet.put("dolnno", map.get("POLNNO"));

			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");

			Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);

			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double qtyPick = Double.parseDouble((String) mQty.get("qtyPick"));

			double tranQty = Double.parseDouble((String) map
					.get(IConstants.QTY));
			double sumqty = qtyPick + tranQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			String queryPoDet = "";
			String queryPoHdr = "";
			// Added By Samatha extracond to Controll the Pick/Issue Qty Excced
			// the Order Qty Aug 24 1010
			String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + "+ map.get(IConstants.QTY);
			if (ordQty == sumqty) {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " , PickStatus='C',Lnstat='O' ";

			} else {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " , PickStatus='O',Lnstat='O' ";

			}

			flag = _DoDetDAO.update(queryPoDet, htCondiPoDet, extraCond);

			if (flag) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("dono", map.get("PONO"));

				flag = _DoDetDAO.isExisit(htCondiPoHdr,
						"pickStatus in ('O','N')");
				if (flag)
					queryPoHdr = "set  STATUS='O',PickStaus='O' ";
				else
					queryPoHdr = "set PickStaus='C' ";

				flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				
				/*InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				Hashtable htInvMstExpDate = new Hashtable();
				htInvMstExpDate.clear();
				htInvMstExpDate.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMstExpDate.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMstExpDate.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMstExpDate.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				
				
				ExpiryDate= _InvMstDAO.getInvExpireDate( (String)map.get(IConstants.PLANT), 
						(String)map.get(IConstants.ITEM),(String) map.get(IConstants.LOC),
						(String)map.get(IConstants.BATCH));*/
			
				 
				boolean pickdet = false;
				Hashtable htPickDet = new Hashtable();
				htPickDet.clear();

				htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htPickDet.put("DONO", map.get(IConstants.PODET_PONUM));
				htPickDet.put("DOLNO", map.get(IConstants.PODET_POLNNO));
				htPickDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.CUSTOMER_NAME));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htPickDet.put(IDBConstants.ITEM_DESC,su.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
				htPickDet.put("BATCH", map.get(IConstants.BATCH));
				htPickDet.put(IDBConstants.LOC, map.get(IConstants.LOC2));
				htPickDet.put("LOC1", map.get(IConstants.LOC));
				htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
				htPickDet.put("PICKQTY", map.get(IConstants.QTY));
				htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htPickDet.put("REMARK", map.get(IConstants.INV_EXP_DATE));
				htPickDet.put("ExpiryDat", ExpiryDate);
			
				Hashtable htCurrency = new Hashtable();
				htCurrency.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htCurrency.put("DONO", map.get(IConstants.PODET_PONUM));
				String currencyid = doutil.getDohdrcol(htCurrency, "CURRENCYID");
				htCurrency.put("DOLNNO", map.get(IConstants.PODET_POLNNO));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					
				String unitprice = doutil.getDodetcol(htCurrency, "UNITPRICE");

			htPickDet.put(IDBConstants.CURRENCYID, currencyid);
			htPickDet.put("UNITPRICE", unitprice);
			htPickDet.put("STATUS", "O");
			
				pickdet = _DoDetDAO.insertPickDet(htPickDet);

			///Update status of DOtransferhdr	
				queryPoHdr = "set STATUS='C' ";
				
		flag=_DoTransferHdrDAO.update(queryPoHdr, htCondiPoHdr, "");			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Pick Qty Exceeded the Order Qty to Pick");
		}
		return flag;
	}





	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.PIC_TRAN_OUT);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, "-" + map.get(IConstants.QTY));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map
					.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map
					.get(IConstants.PODET_PONUM));
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map
					.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
					.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.INV_EXP_DATE));
			
			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));

			htRecvHis.put("DIRTYPE", TransactionConstants.PIC_TRAN_IN);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map
					.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map
					.get(IConstants.PODET_PONUM));
			htRecvHis.put(IDBConstants.CREATED_BY, map
					.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
					.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.INV_EXP_DATE));

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

}