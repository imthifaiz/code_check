package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.StockTakeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.db.util.ItemUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsUploadStockTakeSheet  implements WmsTran,IMLogger{

	DateUtils dateUtils = null;
	StockTakeDAO _StockTakeDAO = null;
	ItemUtil itemdao = null;
	ItemMstDAO _ItemMstDAO=null;
	LocMstDAO locdao = null;
    StrUtils su =null;
	public WmsUploadStockTakeSheet() {
		_StockTakeDAO = new StockTakeDAO();
		dateUtils = new DateUtils();
		itemdao = new ItemUtil();
       _ItemMstDAO= new ItemMstDAO();
		locdao = new LocMstDAO();
	    su = new StrUtils();
	}
		
		private MLogger mLogger = new MLogger();

		public MLogger getmLogger() {
			return mLogger;
		}

		public void setmLogger(MLogger mLogger) {
			this.mLogger = mLogger;
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
		
		public boolean processWmsTran(Map m) throws Exception {
			boolean flag = false;
		

			MLogger.log(0,"2.insert LocMSt - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 1");
			flag = processCountSheet(m);
			MLogger.log(0, "processCountSheet() : Transaction : " + flag);
			//if(flag== true)
				//flag=processMovHis(m);

			return flag;
		}
		
		@SuppressWarnings("unchecked")
		public boolean processCountSheet(Map map) throws Exception {
			boolean flag = false;
			//String uom="";

			this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
			_StockTakeDAO.setmLogger(mLogger);
			//Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();
			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, (String)map.get(IConstants.ITEM));
			htInvMst.put("BATCH", (String)map.get(IDBConstants.BATCH));
			htInvMst.put(IDBConstants.LOC, (String)map.get(IConstants.LOC));
						
			//
			
			//check for existence of item
			itemdao.setmLogger(mLogger);
			locdao.setmLogger(mLogger);
			boolean result=itemdao.isExistsItemMst((String)map.get(IConstants.ITEM), (String)map.get("PLANT"));
			
			//check for valid location
			Hashtable htcdn = new Hashtable();
			htcdn.put(IConstants.PLANT, map.get(IConstants.PLANT));
			htcdn.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
			boolean  result1=locdao.isExisit(htcdn, "");	
			 if(result==true&&result1==true){
				 
				 String Mitem = "",Mloc = "",Mbatch = "",Mqty = "0",STKQTY="",INVFLAG="0",DIFFQTY="0",MUOM="",
						 stkno="",gino="",grno="",itemcost="0",login_user="",PLANT="",sDesc="",baseCurrency="";
				 
				 PLANT = StrUtils.fString((String) map.get(IConstants.PLANT));
				 login_user = StrUtils.fString((String) map.get(IConstants.LOGIN_USER));
				 sDesc = StrUtils.fString((String) map.get(IConstants.ITEM_DESC));
				 Mitem = StrUtils.fString((String) map.get(IConstants.ITEM));
                 Mloc = StrUtils.fString((String) map.get(IConstants.LOC));
                 Mbatch = StrUtils.fString((String) map.get(IDBConstants.BATCH));
                 STKQTY = StrUtils.fString((String) map.get("QTY"));
                 INVFLAG = StrUtils.fString((String) map.get("INVFLAG"));
                 DIFFQTY = StrUtils.fString((String) map.get("DIFFQTY"));
                 MUOM = StrUtils.fString((String) map.get("UOM"));
                 itemcost = StrUtils.fString((String) map.get("ITEMCOST"));
                 stkno = StrUtils.fString((String) map.get("STNO"));
                 gino = StrUtils.fString((String) map.get("GINO"));
                 grno = StrUtils.fString((String) map.get("GRNO"));
                 baseCurrency = StrUtils.fString((String) map.get("BASECURRENCY"));
                 
                 int indxgino = Integer.valueOf((String) map.get("indxgino"));
                 int indxgrno = Integer.valueOf((String) map.get("indxgrno"));
				 
				 Mqty=STKQTY;
				 double stk = Double.parseDouble(Mqty);
                 double diff = Double.parseDouble(DIFFQTY);
                 
                 String diffval = StrUtils.addZeroes((diff), "3");
					
					if(diff!=0) {
					if(diff>0) {

						indxgino =indxgino +1;
						Hashtable htIssueDet = new Hashtable();
						htIssueDet.put(IDBConstants.PLANT, PLANT);
						htIssueDet.put(IDBConstants.DODET_DONUM, "");
						htIssueDet.put(IDBConstants.CUSTOMER_NAME, "");
						htIssueDet.put(IDBConstants.ITEM, Mitem);
						htIssueDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
						htIssueDet.put("BATCH", Mbatch);
						htIssueDet.put(IDBConstants.LOC, Mloc);
						htIssueDet.put("LOC1", Mloc);
						htIssueDet.put("DOLNO", String.valueOf(indxgino));
						htIssueDet.put("ORDQTY", diffval.replaceAll("-",""));
						htIssueDet.put("PICKQTY", diffval.replaceAll("-",""));
						htIssueDet.put("REVERSEQTY", "0");
						htIssueDet.put("STATUS", "C");
						htIssueDet.put("TRAN_TYPE", TransactionConstants.STOCK_TAKE);
						htIssueDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htIssueDet.put(IDBConstants.CREATED_BY, login_user);
						htIssueDet.put(IDBConstants.ISSUEDATE, DateUtils.getDateinddmmyyyy(DateUtils.getDate()));
						htIssueDet.put("REMARK", "");
						htIssueDet.put("INVOICENO", gino);
						htIssueDet.put(IDBConstants.CURRENCYID, baseCurrency);
						htIssueDet.put("UNITPRICE", (String) map.get("ITEMPRICE"));
						flag = new ShipHisDAO().insertShipHis(htIssueDet);
						
						Hashtable htMovChk = new Hashtable();
						htMovChk.clear();
						htMovChk.put(IDBConstants.PLANT, PLANT);
						htMovChk.put("DIRTYPE", TransactionConstants.STOCK_TAKE_OUT);
						htMovChk.put(IDBConstants.ITEM, Mitem);
						htMovChk.put(IDBConstants.QTY, diffval.replaceAll("-",""));
						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, grno);
						htMovChk.put(IDBConstants.LOC, Mloc);
						htMovChk.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						boolean isAdded = new MovHisDAO().isExisit(htMovChk,"");//fix duplicate on Movhis
						if(!isAdded) {
						
						Hashtable htMovhis = new Hashtable();
						htMovhis.clear();
						htMovhis.put(IDBConstants.PLANT, PLANT);
						htMovhis.put("DIRTYPE", TransactionConstants.STOCK_TAKE_OUT);
						htMovhis.put(IDBConstants.ITEM, Mitem);
						htMovhis.put("MOVTID", "OUT");
						htMovhis.put("RECID", "");
						htMovhis.put(IDBConstants.CUSTOMER_CODE, "");
						htMovhis.put(IDBConstants.MOVHIS_ORDNUM, gino);
						htMovhis.put(IDBConstants.LOC, Mloc);
						htMovhis.put(IDBConstants.CREATED_BY, login_user);
						htMovhis.put("BATNO", Mbatch);
						htMovhis.put("UOM", MUOM);
						htMovhis.put("QTY", diffval.replaceAll("-",""));
						htMovhis.put("USERFLD1", "");
						htMovhis.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htMovhis.put("REMARKS", "STOCK_OUT"+" "+stkno);
						flag = new MovHisDAO().insertIntoMovHis(htMovhis);
						
						}
						
					} else {

						indxgrno =indxgrno +1;
 						Hashtable htRecvDet = new Hashtable();
 						htRecvDet.put(IDBConstants.PLANT, PLANT);
 						htRecvDet.put("PONO","");
 						htRecvDet.put(IDBConstants.ITEM, Mitem);
 						htRecvDet.put(IDBConstants.ITEM_DESC, sDesc);
 						htRecvDet.put(IDBConstants.LOC, Mloc);
 						htRecvDet.put("BATCH", Mbatch);
 						htRecvDet.put("ORDQTY", diffval.replaceAll("-",""));
 						htRecvDet.put("RECQTY", diffval.replaceAll("-",""));
 						htRecvDet.put(IDBConstants.CURRENCYID, baseCurrency);
 						htRecvDet.put("UNITCOST", itemcost);				
 						htRecvDet.put("TRAN_TYPE", TransactionConstants.STOCK_TAKE);
 						htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
 						htRecvDet.put(IDBConstants.CREATED_BY, login_user);
 						htRecvDet.put("STATUS", "C");
 						htRecvDet.put("GRNO", grno);
 						htRecvDet.put(IDBConstants.RECVDATE, DateUtils.getDateinddmmyyyy(DateUtils.getDate()));
 						htRecvDet.put(IDBConstants.LNNO, String.valueOf(indxgrno));
 						flag = new RecvDetDAO().insertRecvDet(htRecvDet);
 						
 						Hashtable htMovChk = new Hashtable();
 						htMovChk.clear();
 						htMovChk.put(IDBConstants.PLANT, PLANT);
 						htMovChk.put("DIRTYPE", TransactionConstants.STOCK_TAKE);
 						htMovChk.put(IDBConstants.ITEM, Mitem);
 						htMovChk.put(IDBConstants.QTY, diffval.replaceAll("-",""));
 						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, grno);
 						htMovChk.put(IDBConstants.LOC, Mloc);
 						htMovChk.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
 						boolean isAdded = new MovHisDAO().isExisit(htMovChk,"");//fix duplicate on Movhis
 						if(!isAdded) {
 						Hashtable htMovhis = new Hashtable();
 						htMovhis.clear();
 						htMovhis.put(IDBConstants.PLANT, PLANT);
 						htMovhis.put("DIRTYPE", TransactionConstants.STOCK_TAKE);
 						htMovhis.put(IDBConstants.ITEM, Mitem);
 						htMovhis.put("MOVTID", "IN");
 						htMovhis.put("RECID", "");
 						htMovhis.put(IDBConstants.CUSTOMER_CODE, "");
 						htMovhis.put(IDBConstants.MOVHIS_ORDNUM, grno);
 						htMovhis.put(IDBConstants.LOC, Mloc);
 						htMovhis.put(IDBConstants.CREATED_BY, login_user);
 						htMovhis.put("BATNO", Mbatch);
 						htMovhis.put("UOM", MUOM);
 						htMovhis.put("QTY", diffval.replaceAll("-",""));
 						htMovhis.put("USERFLD1", "");
 						htMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
 						htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
 						htMovhis.put("REMARKS", "STOCK_IN"+" "+stkno);
 						flag = new MovHisDAO().insertIntoMovHis(htMovhis);
 						}
 						
					}
					}
					

 			
				 
				 flag = _StockTakeDAO.isExisit(htInvMst, "");
					if (flag) {
						StringBuffer sql = new StringBuffer(" SET ");

						sql.append("" + IDBConstants.QTY + " = " + IDBConstants.QTY
								+ " + '" + map.get(IConstants.QTY) + "',DIFFQTY= DIFFQTY"
								+ " + '" + map.get("DIFFQTY") + "',INVFLAG= '"
								+ map.get("INVFLAG") + "',REMARKS='" + map.get(IConstants.REMARKS) + "' , " + IDBConstants.UOM + " = '" + map.get(IConstants.UOM) +"'");

						flag = _StockTakeDAO.update(sql.toString(), htInvMst, "");

					} else {
						htInvMst.clear();
						htInvMst.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
						htInvMst.put(IDBConstants.ITEM, (String)map.get(IConstants.ITEM));
						htInvMst.put(IDBConstants.QTY, (String) map.get(IConstants.QTY));
						htInvMst.put(IDBConstants.LOC, (String) map.get(IConstants.LOC));
						htInvMst.put("BATCH", (String)map.get(IDBConstants.BATCH));
						//uom=_ItemMstDAO.getItemUOM((String)map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));
						htInvMst.put(IDBConstants.UOM, (String) map.get(IConstants.UOM));
						//htInvMst.put("UOM", uom);
						htInvMst.put(IDBConstants.STATUS, "C");
						htInvMst.put("STKNO", stkno);
						htInvMst.put("DIFFQTY", (String) map.get("DIFFQTY"));
						htInvMst.put("INVFLAG", (String) map.get("INVFLAG"));
						htInvMst.put("CR_DATE",  DateUtils.getDate());
						htInvMst.put(IDBConstants.USERFLD1,su.InsertQuotes( new ItemMstDAO().getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM))));
						htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htInvMst.put(IDBConstants.CREATED_BY, (String) map.get(IConstants.LOGIN_USER));
						htInvMst.put(IDBConstants.REMARKS, (String) map.get(IConstants.REMARKS));

						flag = _StockTakeDAO.insertStkTake(htInvMst);

					}
					
					if(flag) {
						
						htInvMst.clear();
		      			htInvMst.put(IDBConstants.PLANT, PLANT);
		      			htInvMst.put(IDBConstants.ITEM, Mitem);
		      			htInvMst.put(IDBConstants.LOC, Mloc);
		      			htInvMst.put(IDBConstants.BATCH, Mbatch);
		      			flag = new InvMstDAO().isExisit(htInvMst, "");
						if(!flag) {
							
							htInvMst.put(IDBConstants.EXPIREDATE, "");
							htInvMst.put(IDBConstants.QTY,Mqty);
							htInvMst.put(IDBConstants.USERFLD3, "");
							htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDate().toString().replaceAll("/", "") + "000000");
							htInvMst.put(IDBConstants.CREATED_BY, login_user);
							htInvMst.put(IDBConstants.STATUS, "");
				
							flag = new InvMstDAO().insertInvMst(htInvMst);
							
						} else {
						
						StringBuffer sql1 = new StringBuffer(" SET ");
      				    sql1.append(IDBConstants.QTY + " = '"+ stk + "', ");
      					sql1.append(IDBConstants.UPDATED_AT + " = '"+ DateUtils.getDateTime() + "', ");
      					sql1.append(IDBConstants.UPDATED_BY + " = '"+ login_user + "'");
      					flag = new InvMstDAO().update(sql1.toString(), htInvMst, "");
      					
						}
					}

			 }else
			 {
				 throw new Exception("One of the Product Id or Location is not created in product or location master.");
			 }
			return flag;
		}
		/*@SuppressWarnings("unchecked")
		public boolean processMovHis(Map map) throws Exception {
			boolean flag = false;
			MovHisDAO movHisDao = new MovHisDAO();
			this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
			movHisDao.setmLogger(mLogger);
			try {
				Hashtable htmov = new Hashtable();
				htmov.clear();
				htmov.put(IDBConstants.PLANT, map.get("PLANT"));
				htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_INV_UPLOAD);
				htmov.put(IDBConstants.LOC, map.get("LOC"));
				htmov.put("Qty", map.get(IConstants.QTY));
				htmov.put(IDBConstants.ITEM, map.get("ITEM"));
				htmov.put("BATNO", map.get(IDBConstants.BATCH));
				htmov.put(IDBConstants.MOVTID, "IN");
				htmov.put(IDBConstants.RECID, "");
				htmov.put(IDBConstants.PONO, "");
				htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
				htmov.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			    htmov .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			  

				flag = movHisDao.insertIntoMovHis(htmov);

			} catch (Exception e) {
				throw e;
			}
			return flag;
		}*/
		
		@SuppressWarnings("unchecked")
		public boolean processMovHis(Map map) throws Exception {
			boolean flag = false;
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			try {
				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put("BATNO", map.get(IDBConstants.BATCH));
				htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htRecvHis.put("DIRTYPE",TransactionConstants.CNT_STOCK_TAKE_UPLOAD);
				htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htRecvHis.put("MOVTID", "");
				htRecvHis.put("RECID", "");
				htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
				if((String)map.get(IConstants.UOM)!=null)
				htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UOM));
				htRecvHis.put(IDBConstants.CREATED_BY, map
						.get(IConstants.LOGIN_USER));
				htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
						.getDateinyyyy_mm_dd(dateUtils.getDate()));
				htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));

				flag = movHisDao.insertIntoMovHis(htRecvHis);

			} catch (Exception e) {
				throw e;
			}
			return flag;
		}


}
