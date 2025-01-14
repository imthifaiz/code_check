 package com.track.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.AuditDAO;
import com.track.dao.BaseDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.CostofgoodsLanded;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;


@SuppressWarnings({"rawtypes", "unchecked"})
public class HTReportUtil extends BaseDAO{

	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.HTReportUtil_PRINTPLANTMASTERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
		
	}

	public HTReportUtil() {
	}

	/**
	 * method : getInvList(String aPlant,String aWhid,String aBinno,String
	 * aItem,String aBatch) description : get inventory details
	 * 
	 * @param
	 * @return ArrayList
	 */
	public ArrayList getInvList(String aPlant, String aWhid, String aBinno,
			String aItem, String aBatch) {
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			invdao.setmLogger(mLogger);
			Hashtable ht = new Hashtable();

			if (StrUtils.fString(aWhid).length() > 0)
				ht.put("A.WHID", aWhid);
						
			if (StrUtils.fString(aBinno).length() > 0)
				ht.put("A.BINNO", aBinno);
			if (StrUtils.fString(aItem).length() > 0)
				ht.put("A.ITEM", aItem);
			if (StrUtils.fString(aBatch).length() > 0)
				ht.put("BATCH", aBatch);

//			String aQuery = "SELECT DISTINCT A.WHID as WHID,A.BINNO as BINNO,A.ITEM as ITEM,B.ITEMDESC as ITEMDESC,A.BATCH as BATCH,QTY ,B.STKUOM as UOM from INVMST A,ITEMMST  B where A.PLANT =B.PLANT AND A.PLANT ='"
//					+ aPlant + "' AND  A.ITEM =B.ITEM ";

		} catch (Exception e) {

		}
		return arrList;
	}

	public ArrayList getWipList(String aPlant, String aWhid, String aBinno,
			String aItem, String aBatch, String aOpSeq, String aCrBy) {
		ArrayList arrList = new ArrayList();
		try {
			InvMstDAO invdao = new InvMstDAO();
			Hashtable ht = new Hashtable();
			invdao.setmLogger(mLogger);

			if (StrUtils.fString(aWhid).length() > 0)
				ht.put("A.WHID", aWhid);
			if (StrUtils.fString(aBinno).length() > 0)
				ht.put("A.BINNO", aBinno);
			if (StrUtils.fString(aItem).length() > 0)
				ht.put("A.ITEM", aItem);
			if (StrUtils.fString(aBatch).length() > 0)
				ht.put("BATCH", aBatch);
			if (StrUtils.fString(aOpSeq).length() > 0)
				ht.put("A.OPSEQ", aOpSeq);
			if (StrUtils.fString(aCrBy).length() > 0)
				ht.put("A.CRBY", aCrBy);

//			String aQuery = "SELECT DISTINCT A.WHID as WHID,A.BINNO as BINNO,A.ITEM as ITEM,B.ITEMDESC as ITEMDESC,A.BATCH as BATCH,QTY ,A.OPSEQ as OPSEQ,A.CRBY as CRBY,B.STKUOM as UOM from WIPMST A,ITEMMST  B where A.PLANT =B.PLANT AND A.PLANT ='"
//					+ aPlant + "' AND  A.ITEM =  B.ITEM ";

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getWipList:", e);
		}
		return arrList;
	}

	public ArrayList getMovHisList(Hashtable ht, String afrmDate, String atoDate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}

			String aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC,ISNULL(TRANDATE,'') AS TRANDATE ,CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY FROM MOVHIS WHERE PLANT='SIS' "
					+ sCondition;
			String extCond = " order by crat ";
			arrList = movHisDAO.selectForReport(aQuery, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getMovHisList:", e);

		}
		return arrList;
	}

	public ArrayList getMovHisExcelList(Hashtable ht, String afrmDate,
			String atoDate, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			String aQuery = "";
			if (plant.equalsIgnoreCase("track"))
				aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC,ISNULL(TRANDATE,'') AS TRANDATE ,CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY FROM "
						+ "[MOVHIS] WHERE PLANT='" + plant + "'" + sCondition;
			else
				aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC,ISNULL(TRANDATE,'') AS TRANDATE ,CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY FROM "
						+ "["
						+ plant
						+ "_"
						+ "MOVHIS] WHERE PLANT='"
						+ plant
						+ "'" + sCondition;
			String extCond = " order by crat ";
			arrList = movHisDAO.selectForReport(aQuery, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getMovHisExcelList:", e);

		}
		return arrList;
	}

	// WORK ORDER SUMMARY
	public ArrayList getMovHisList(Hashtable ht, String aPlant,String afrmDate, String atoDate, String aUserID,String sProductDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
            if(sProductDesc.length()>0){
                sProductDesc  = " AND ITEM IN(SELECT ITEM FROM "+aPlant+"_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(sProductDesc.replaceAll(" ",""))+"%')" ;
            }
//			UserLocUtil userLocUtil = new UserLocUtil();
//			userLocUtil.setmLogger(mLogger);
			String condAssignedLocforUser = " ";
//			condAssignedLocforUser = userLocUtil.getUserLocAssigned(aUserID,aPlant, "LOC");

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			ht.put("PLANT", aPlant);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			String aQuery = "";
			if (aPlant.equalsIgnoreCase("track")) {
				aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC,ISNULL(TRANDATE,'') AS TRANDATE ,CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY,isnull((Select ITEMDESC from [" +aPlant+"_itemmst] A where  a.ITEM=b.ITEM),'') ITEMDESC FROM  "
						+ "["
						+ "MOVHIS"
						+ "] B  WHERE PLANT='"
						+ aPlant
						+ "' "
						+ "and CRBY not in('218202180','20212022')  " + sCondition;
			} else {
				aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC,ISNULL(TRANDATE,'') AS TRANDATE ,CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY,isnull((Select ITEMDESC from ["+aPlant+"_itemmst] A where  a.ITEM=b.ITEM),'') ITEMDESC FROM  "

						+ "["
						+ aPlant
						+ "_"
						+ "MOVHIS"
						+ "] B WHERE PLANT='"
						+ aPlant
						+ "' "
						+ "and CRBY not in('218202180','20212022')  "
						+ sCondition
						+ condAssignedLocforUser +sProductDesc;
			}
			String extCond = " order by crat ";
			arrList = movHisDAO.selectForReport(aQuery, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	//---Modified by Bruhan on June 17 2014, Description: To include toexceltransactiondate
	public ArrayList getMovHisListWithRemarks(Hashtable ht, String aPlant,String afrmDate, String atoDate, String aUserID,String sProductDesc,String sReasonCode,String sloctypeid,String sloctypeid2,String sloctypeid3,String sloc,String sdirtype,String stype,String prdclsid,String prdtypeid,String prdbrandid,String prddeptid,String remarks) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			
            if(sProductDesc.length()>0){
                sProductDesc  = " AND B.ITEM IN(SELECT ITEM FROM "+aPlant+"_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(sProductDesc.replaceAll(" ",""))+"%')" ;	
            }
//			UserLocUtil userLocUtil = new UserLocUtil();
//			userLocUtil.setmLogger(mLogger);
			String condAssignedLocforUser = " ",cnsval="";
//			condAssignedLocforUser = userLocUtil.getUserLocAssigned(aUserID,aPlant, "LOC");

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//ht.put("PLANT", aPlant);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND B.TRANDATE  >= '" + DateUtils.getDateinyyyy_mm_dd(afrmDate)
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND B.TRANDATE <= '" + DateUtils.getDateinyyyy_mm_dd(atoDate)
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND B.TRANDATE  <= '" + DateUtils.getDateinyyyy_mm_dd(atoDate)
							+ "'  ";
				}
			}
			
			if (sReasonCode != null && sReasonCode!="") {
				sCondition = sCondition + "  AND SUBSTRING(B.REMARKS, CHARINDEX(',',B.REMARKS) + 1, LEN(B.REMARKS)) LIKE '%" + sReasonCode+ "%' and B.REMARKS IS NOT NULL AND B.REMARKS <> ''";
						
			}
			if (remarks != null && remarks!="") {
				sCondition = sCondition +" AND B.REMARKS like '%"+remarks+"%'";
						
			}
			
			if (sloc != null && sloc!="") {
            	sCondition = sCondition+" AND B.LOC like '%"+sloc+"%'";
            }
            
            if (sloctypeid != null && sloctypeid!="") {
    			sCondition = sCondition + " AND B.LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,sloctypeid)+" )";
    		}
            
            if (sloctypeid2 != null && sloctypeid2!="") {
    			sCondition = sCondition + " AND B.LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,sloctypeid2)+" )";
    		}
			
			if (sloctypeid3 != null && sloctypeid3!="") {
				sCondition = sCondition + " AND B.LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,sloctypeid3)+" )";
			}
            
            if(sdirtype.length()>0 && stype==""){
            	if (sdirtype.equalsIgnoreCase("Purchase Estimate Order") ) {
            		String[] inbounddirtype = TransactionConstants.purchaseestimatedirtype;
            		for(int j=0;j<inbounddirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+inbounddirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +inbounddirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Purchase Order") ) {
            		String[] inbounddirtype = TransactionConstants.inbounddirtype;
            		for(int j=0;j<inbounddirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+inbounddirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +inbounddirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Sales Order") ) {
            		String[] outbounddirtype = TransactionConstants.outbounddirtype;
            		for(int j=0;j<outbounddirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+outbounddirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +outbounddirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Consignment Order") ) {
            		String[] transferdirtype = TransactionConstants.transferdirtype;
            		for(int j=0;j<transferdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+transferdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +transferdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Rental And Service Order") ) {
            		String[] loandirtype = TransactionConstants.loandirtype;
            		for(int j=0;j<loandirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+loandirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +loandirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Work Order") ) {
            		String[] workdirtype = TransactionConstants.workdirtype;
            		for(int j=0;j<workdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+workdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +workdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Goods_Receipt_Issue") ) {
            		String[] miscdirtype = TransactionConstants.goods_receipt_Issue;
            		for(int j=0;j<miscdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+miscdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +miscdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Stock Move") ) {
            		String[] loctransferdirtype = TransactionConstants.stockmove;
            		for(int j=0;j<loctransferdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+loctransferdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +loctransferdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	
            	else if (sdirtype.equalsIgnoreCase("Stock Take") ) {
            		String[] stocktakedirtype = TransactionConstants.stocktake;
            		for(int j=0;j<stocktakedirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+stocktakedirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +stocktakedirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Salese Estimate Order") ) {
            		String[] estimatedirtype = TransactionConstants.estimatedirtype;
            		for(int j=0;j<estimatedirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+estimatedirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +estimatedirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Kitting And DeKitting") ) {
            		String[] paymentdirtype = TransactionConstants.kitting;
            		for(int j=0;j<paymentdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+paymentdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +paymentdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("De-Kitting") ) {
            		String[] paymentdirtype = TransactionConstants.ProcessingReceive;
            		for(int j=0;j<paymentdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+paymentdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +paymentdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Kitting") ) {
            		String[] paymentdirtype = TransactionConstants.SemiProcessedProduct;
            		for(int j=0;j<paymentdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+paymentdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +paymentdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Expenses") ) {
            		String[] vdirtype = TransactionConstants.expenses;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Bill") ) {
            		String[] vdirtype = TransactionConstants.bill;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Payment") ) {
            		String[] vdirtype = TransactionConstants.payment;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Expenses") ) {
            		String[] vdirtype = TransactionConstants.expenses;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Debit Note") ) {
            		String[] vdirtype = TransactionConstants.purchase_creditnote;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Invoice") ) {
            		String[] vdirtype = TransactionConstants.invoice;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Payment Received") ) {
            		String[] vdirtype = TransactionConstants.payment_received;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Credit Note") ) {
            		String[] vdirtype = TransactionConstants.sales_creditnote;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("TAX_FILE") ) {
            		String[] vdirtype = TransactionConstants.tax_file;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Holiday") ) {
            		String[] vdirtype = TransactionConstants.holiday;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Leave Type") ) {
            		String[] vdirtype = TransactionConstants.leavetype;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Salary Type") ) {
            		String[] vdirtype = TransactionConstants.salarytype;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Bulk Payslip") ) {
            		String[] vdirtype = TransactionConstants.bulkpayslip;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
            		sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Employee Type") ) {
            		String[] vdirtype = TransactionConstants.employeetype;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Apply Leave") ) {
            		String[] vdirtype = TransactionConstants.applyleave;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}
            	else if (sdirtype.equalsIgnoreCase("Payroll") ) {
            		String[] vdirtype = TransactionConstants.payroll;
            		for(int j=0;j<vdirtype.length;j++){
            			if(cnsval == ""){cnsval="'"+vdirtype[j]+"'";}
            			else{cnsval=cnsval + ",'" +vdirtype[j]+"'";} 
            		}
    			sCondition = sCondition + " AND DIRTYPE IN ( "+cnsval+" )" ;
            	}

            }
            if(stype != null && stype!="")
            {
            	sCondition = sCondition + " AND DIRTYPE IN ( "+stype+" )" ;
            }
            
 			if(prdclsid != null && prdclsid!="") 
            {
            	sCondition = sCondition + " AND B.ITEM IN(SELECT ITEM FROM  ["+ aPlant+ "_"+ "ITEMMST"+ "] WHERE PRD_CLS_ID='"+prdclsid+"')" ;
            }
 			
 			if(prddeptid != null && prddeptid!="") 
            {
            	sCondition = sCondition + " AND B.ITEM IN(SELECT ITEM FROM  ["+ aPlant+ "_"+ "ITEMMST"+ "] WHERE PRD_DEPT_ID='"+prddeptid+"')" ;
            }
            if(prdtypeid != null && prdtypeid!="") 
            {
            	sCondition = sCondition + " AND B.ITEM IN(SELECT ITEM FROM  ["+ aPlant+ "_"+ "ITEMMST"+ "] WHERE ITEMTYPE='"+prdtypeid+"')" ;
            }
            if(prdbrandid != null && prdbrandid!="") 
            {
            	sCondition = sCondition + " AND B.ITEM IN(SELECT ITEM FROM  ["+ aPlant+ "_"+ "ITEMMST"+ "] WHERE PRD_BRAND_ID='"+prdbrandid+"')" ;
            }
            
			String aQuery = "";
			if (aPlant.equalsIgnoreCase("track")) {
			/*aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC," 
						+ "ISNULL(TRANDATE,'') AS TRANDATE ," 
						+ " ISNULL(CAST((SUBSTRING(CRAT, 1, 4) + SUBSTRING(CRAT, 5, 2) + SUBSTRING(CRAT, 7, 2)) AS date),'')  AS toexceltransactiondate,"
						+ "CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,isnull(CRBY,'')CRBY,replace(ISNULL(REMARKS,''),'''','') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY,isnull((Select top 1 ITEMDESC from [" +aPlant+"_itemmst] A where  a.ITEM=b.ITEM),'') ITEMDESC FROM  "
						+ "["
						+ "MOVHIS"
						+ "] B  WHERE PLANT='"
						+ aPlant
						+ "' "
						+ "and isnull(CRBY,'') not in('218202180','20212022') AND ISNULL(USERFLG6,'Y')<>'N' " + sCondition;
			} else {
					aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC," 
						+ "ISNULL(TRANDATE,'') AS TRANDATE ," 
						+ " ISNULL(CAST((SUBSTRING(CRAT, 1, 4) + SUBSTRING(CRAT, 5, 2) + SUBSTRING(CRAT, 7, 2)) AS date),'')  AS toexceltransactiondate,"
						+"CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,isnull(CRBY,'')CRBY,replace(ISNULL(REMARKS,''),'''','') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY,isnull((Select top 1 ITEMDESC from ["+aPlant+"_itemmst] A where  a.ITEM=b.ITEM),'') ITEMDESC FROM  "
						+ "["
						+ aPlant
						+ "_"
						+ "MOVHIS"
						+ "] B WHERE PLANT='"
						+ aPlant
						+ "' "
						+ "and isnull(CRBY,'') not in('218202180','20212022') AND ISNULL(USERFLG6,'Y')<>'N' "  // userFlg6 is used if the record in movhis should not show in report
						+ sCondition
						+ condAssignedLocforUser +sProductDesc;*/
				aQuery = "SELECT isnull(B.JOBNUM,'') as JOBNUM,ISNULL(B.ORDNUM,'') as ORDNUM,ISNULL(B.LOC,'') AS LOC," 
						+ "ISNULL(B.TRANDATE,'') AS TRANDATE ," 
						+ " ISNULL(CAST((SUBSTRING(B.CRAT, 1, 4) + SUBSTRING(B.CRAT, 5, 2) + SUBSTRING(B.CRAT, 7, 2)) AS date),'')  AS toexceltransactiondate,"
						+ "B.CRAT,B.DIRTYPE,ISNULL(B.ITEM,'') as ITEM,isnull(B.CUSTNO,'') as CUSTOMER,isnull(B.PONO,'') as PONO,isnull(B.CRBY,'')CRBY,replace(ISNULL(B.REMARKS,''),'''','') As REMARKS,ISNULL(B.BATNO,'') AS BATNO,ISNULL(B.QTY,0) AS QTY,ISNULL(A.ITEMDESC,'') AS ITEMDESC FROM  "
						+ "["
						+ "MOVHIS"
						+ "] B B LEFT JOIN ["+aPlant+"_itemmst] A ON A.ITEM=B.ITEM WHERE B.PLANT='"
						+ aPlant
						+ "' "
						+ "and isnull(B.CRBY,'') not in('218202180','20212022') AND ISNULL(B.USERFLG6,'Y')<>'N' " + sCondition;
			} else {
					aQuery = "SELECT isnull(B.JOBNUM,'') as JOBNUM,ISNULL(B.ORDNUM,'') as ORDNUM,ISNULL(B.LOC,'') AS LOC," 
						+ "ISNULL(B.TRANDATE,'') AS TRANDATE ," 
						+ " ISNULL(CAST((SUBSTRING(B.CRAT, 1, 4) + SUBSTRING(B.CRAT, 5, 2) + SUBSTRING(B.CRAT, 7, 2)) AS date),'')  AS toexceltransactiondate,"
						+"B.CRAT,B.DIRTYPE,ISNULL(B.ITEM,'') as ITEM,isnull(B.CUSTNO,'') as CUSTOMER,isnull(B.PONO,'') as PONO,isnull(B.CRBY,'')CRBY,replace(ISNULL(B.REMARKS,''),'''','') As REMARKS,ISNULL(B.BATNO,'') AS BATNO,ISNULL(B.QTY,0) AS QTY,ISNULL(A.ITEMDESC,'') AS ITEMDESC FROM  "
						+ "["
						+ aPlant
						+ "_"
						+ "MOVHIS"
						+ "] B LEFT JOIN ["+aPlant+"_itemmst] A ON A.ITEM=B.ITEM WHERE B.PLANT='"
						+ aPlant
						+ "' "
						+ "and isnull(B.CRBY,'') not in('218202180','20212022') AND ISNULL(B.USERFLG6,'Y')<>'N' "  // userFlg6 is used if the record in movhis should not show in report
						+ sCondition
						+ condAssignedLocforUser +sProductDesc;
			}
			String extCond = " order by B.crat DESC";
			arrList = movHisDAO.selectForReport(aQuery, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getMovHisClockInOutList(Hashtable ht, String aPlant, String extconditn) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
                       
			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
//			String condAssignedLocforUser = " ";
		
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			ht.put("PLANT", aPlant);
			if (extconditn.length() > 0) {
				sCondition =  "  AND  "+extconditn ;
				
			}			
			String aQuery = "";
			
				aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC,ISNULL(TRANDATE,'') AS TRANDATE ,CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull(CUSTNO,'') as CUSTOMER,isnull(PONO,'') as PONO,CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY FROM  "

						+ "["
						+ aPlant
						+ "_"
						+ "MOVHIS"
						+ "]  WHERE PLANT='"
						+ aPlant
						+ "' "
						+ "and CRBY not in('218202180','20212022')  "
						+ sCondition;
			
			String extCond = " order by crat ";
			arrList = movHisDAO.selectForReport(aQuery, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	
	public ArrayList getAuditList(Hashtable ht, String aPlant, String afrmDate,String atoDate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";//, sCondition1 = "", sCondition2 = "";
		try {
			AuditDAO auditDAO = new AuditDAO();
			auditDAO.setmLogger(mLogger);

			if (afrmDate.length() > 0) {
				sCondition = sCondition + " and CRDT  >= '" + afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " and CRDT <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " and CRDT  <= '" + atoDate
							+ "'  ";
				}
			}
			String aQuery = "";

			aQuery = "SELECT isnull(USERID,'') as USERID,ISNULL(COMPANY,'') as COMPANY,ISNULL(TRANSACTIONID,'') AS TRANSACTIONID,CRDT,isnull(logmessage,'') as LOGMSG FROM  "

					+ "["
					+ aPlant
					+ "_"
					+ "AUDIT"
					+ "] WHERE COMPANY <>''"
					+ sCondition;

			String extCond = " order by crdt ";
			arrList = auditDAO.selectForReport(aQuery, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getAuditList:", e);
		}
		return arrList;
	}
	//CREATED BY NAVAS FEB20
		public ArrayList getConsignmentWorkOrderSummaryList(Hashtable ht, String afrmDate,
				String atoDate, String dirType, String plant, String itemDesc, String custname,String viewdate,String UOM) {
					
//			String fromdate="",todate="";
			ArrayList arrList = new ArrayList();
			String sCondition = "";
			try {
				MovHisDAO movHisDAO = new MovHisDAO();
				movHisDAO.setmLogger(mLogger);
			         
				//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
					        if (itemDesc.length() > 0 ) {
						        if (itemDesc.indexOf("%") != -1) {
						        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
						        }
						        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
					        }
	                        
	                          if (custname.length()>0){
	                         	custname = StrUtils.InsertQuotes(custname);
	                         	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
	                         }
	                        
	                           
	                        	 if (afrmDate.length() > 0) {
	                              	sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
	                              	+ "'  ";
	                              	if (atoDate.length() > 0) {
	                              		sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
	                              		+ "'  ";
	                              	}
	                              } else {
	                              	if (atoDate.length() > 0) {
	                              		sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
	                              		+ "'  ";
	                              	}
	                              }
	                       
	                                   
				
						String sCondition1 = "";
				       if (itemDesc.length() > 0 ) {
	    		        if (itemDesc.indexOf("%") != -1) {
	    		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
	    		        }
	    		        sCondition1 = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	    	        }
	                
	              	 if (custname.length()>0){
	                 	custname = StrUtils.InsertQuotes(custname);
	                 	sCondition1 = sCondition1 + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
	                 }
					
			     String dtCondStr="";
					 String extraCon="";
						
							dtCondStr =    " and ISNULL(a.CollectionDate,'')<>'' AND (SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2))";
							extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
						
	                if (afrmDate.length() > 0) {
	                	sCondition1 = sCondition1 + dtCondStr + "  >= '" 
	    						+ afrmDate
	    						+ "'  ";
	    				if (atoDate.length() > 0) {
	    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
	    					+ atoDate
	    					+ "'  ";
	    				}
	    			} else {
	    				if (atoDate.length() > 0) {
	    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
	    					+ atoDate
	    					+ "'  ";
	    				}
	    			}   
	              
	             
				if (dirType.equalsIgnoreCase("CONSIGNMENT")) {
					
					if (ht.size() > 0) {
						if (ht.get("CUSTTYPE") != null) {
							String custtype = ht.get("CUSTTYPE").toString();
							sCondition1= sCondition1+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
							ht.remove("CUSTTYPE");
						}
					}
					
					String aQuery = "select distinct b.tono,b.tolnno,isnull(a.CRBY,'') as users,isnull(a.ordertype,'') as ordertype,isnull(a.Remark1,'') as remarks1,isnull(a.Remark3,'') as remarks3,isnull(a.Remark2,'') as remarks2,isnull(a.jobNum,'') jobNum,a.custname,a.fromwarehouse,a.towarehouse,b.item,isnull(c.itemdesc,'') itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(b.unitprice,0) unitprice,isnull(a.collectionDate,'') TRANDATE,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as CollectionDate,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyac,0) qtyac,isnull(b.QTYRC,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,isnull(a.deliverydate,'')deliverydate,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'')as UOM,isnull(a.status,'')as status,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname,ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,ORDER_STATUS from "
							+ "["
							+ plant
							+ "_"
							+ "tohdr] a,"
							+ "["
							+ plant
							+ "_"
							+ "todet] b,"
							+ "["
							+ plant
							+ "_"
							+"ITEMMST] c where a.tono=b.tono and b.ITEM=c.item and a.custcode <> '' " + sCondition1 ;

					
					arrList = movHisDAO.selectForReport(aQuery, ht,extraCon);
				}
				
				if (dirType.equalsIgnoreCase("CONSIGNMENTPRODUCTREMARKS")) {
					
					if (ht.size() > 0) {
						if (ht.get("CUSTTYPE") != null) {
							String custtype = ht.get("CUSTTYPE").toString();
							sCondition1= sCondition1+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
							ht.remove("CUSTTYPE");
						}
					}
					extraCon=" order by CollectionDate,b.tono,d.id_remarks";
					String aQuery = "select distinct b.tono,b.tolnno,isnull(a.ordertype,'') as ordertype,isnull(a.Remark1,'') as remarks2,isnull(a.Remark3,'') as remarks2,isnull(a.Remark2,'') as remarks2,isnull(a.jobNum,'') jobNum,a.custname,b.item,isnull(c.itemdesc,'') itemdesc,isnull(c.Remark1,'') as DetailItemDesc,a.fromwarehouse,a.towarehouse,isnull(b.unitprice,0) unitprice,isnull(a.collectionDate,'') TRANDATE,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as CollectionDate,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.QTYRC,0) as QTYRC, isnull(b.qtyPick,0) as qtyPick, isnull(b.qtyac,0) as qtyac,b.pickstatus,isnull(a.deliverydate,'')deliverydate,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status,'')as status,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname,isnull(d.remarks,'') prdremarks,d.id_remarks from "
							+ "["
							+ plant
							+ "_"
							+ "tohdr] a,"
							+ "["
							+ plant
							+ "_"
							+ "todet] b,"
							+ "["
							+ plant
							+ "_"
							+"ITEMMST] c,"
							+ "["
		  					+ plant
		  					+ "_"
		  					+"TODET_REMARKS] d where a.tono=b.tono and b.ITEM=c.item and b.tolnno=d.tolnno and b.tono = d.tono  " + sCondition1 ;

					
					arrList = movHisDAO.selectForReport(aQuery, ht,extraCon);
				}
				if (dirType.equalsIgnoreCase("CONSIGNMENTPRICE")) {
					String aQuery = "select b.tono,b.tolnno,isnull(a.ordertype,'') as ordertype,isnull(a.jobNum,'') jobNum,isnull(a.Remark1,'') as remarks3,isnull(a.Remark3,'') as remarks2,isnull(a.Remark2,'') as remarks2,a.custname,a.fromwarehouse,a.towarehouse,b.item,isnull(b.unitprice,0) unitprice,isnull(B.TRANDATE,'') TRANDATE,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.QTYRC,0) as QTYRC,isnull(b.QTYAC,0) as QTYAC, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT from "
							+ "["
							+ plant
							+ "_"
							+ "tohdr] a,"
							+ "["
							+ plant
							+ "_"
							+ "todet] b where a.tono=b.tono  " + sCondition +" order by b.item ";

					
					arrList = movHisDAO.selectForReport(aQuery, ht);
				}
				

				

			

			} catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getConsignmentWorkOrderSummaryList:", e);
			}
			return arrList;
		}
		//END BY NAVAS
	public ArrayList getWorkOrderSummaryList(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc, String custname,String viewdate,String UOM, String ispos) {
				
//		String fromdate="",todate="";
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			String posext="";
		   	if(ispos.equalsIgnoreCase("3"))
		   		posext =  " AND ORDERTYPE = 'POS' " ;
		   	else if(ispos.equalsIgnoreCase("2"))
		   		posext =  " AND ORDERTYPE != 'POS' " ;
		         
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
				        if (itemDesc.length() > 0 ) {
					        if (itemDesc.indexOf("%") != -1) {
					        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
					        }
					        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
				        }
                        
                          if (custname.length()>0){
                         	custname = StrUtils.InsertQuotes(custname);
                         	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                         }
                        
                           
                        	 if (afrmDate.length() > 0) {
                              	sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
                              	+ "'  ";
                              	if (atoDate.length() > 0) {
                              		sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
                              		+ "'  ";
                              	}
                              } else {
                              	if (atoDate.length() > 0) {
                              		sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
                              		+ "'  ";
                              	}
                              }
                       
                                   
			
					String sCondition1 = "";
			       if (itemDesc.length() > 0 ) {
    		        if (itemDesc.indexOf("%") != -1) {
    		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
    		        }
    		        sCondition1 = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
    	        }
                
              	 if (custname.length()>0){
                 	custname = StrUtils.InsertQuotes(custname);
                 	sCondition1 = sCondition1 + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                 }
				
		     String dtCondStr="";
				 String extraCon="";
				 if (dirType.equalsIgnoreCase("INBOUND") || dirType.equalsIgnoreCase("PRODUCTREMARKS")) {

					 if (ht.size() > 0) {
							if (ht.get("SUPPLIERTYPE") != null) {
								String suppliertype = ht.get("SUPPLIERTYPE").toString();
								sCondition1= sCondition1+" AND custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
								ht.remove("SUPPLIERTYPE");
							}
						}
					 
				
					 if(viewdate.equals("ByOrderDate")){
						 dtCondStr =    " and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
						  extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) desc,pono desc ";
						 if (afrmDate.length() > 0) {
		                	sCondition1 = sCondition1 + dtCondStr + "  >= '" 
		    						+ afrmDate
		    						+ "'  ";
		    				if (atoDate.length() > 0) {
		    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
		    					+ atoDate
		    					+ "'  ";
		    				}
		    			} else {
		    				if (atoDate.length() > 0) {
		    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
		    					+ atoDate
		    					+ "'  ";
		    				}
		    			}   
		             
				   } else if(viewdate.equals("ByDeliveryDate")){
					     dtCondStr =    " and ISNULL(a.DelDate,'')<>'' AND (SUBSTRING(a.DelDate, 7, 4) + '-' + SUBSTRING(a.DelDate, 4, 2) + '-' + SUBSTRING(a.DelDate, 1, 2))";
					     extraCon=" order by CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) desc";
					     if (afrmDate.length() > 0) {
		                	sCondition1 = sCondition1 + dtCondStr + "  >= '" 
		    						+ afrmDate
		    						+ "'  ";
		    				if (atoDate.length() > 0) {
		    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
		    					+ atoDate
		    					+ "'  ";
		    				}
		    			} else {
		    				if (atoDate.length() > 0) {
		    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
		    					+ atoDate
		    					+ "'  ";
		    				}
		    			}   
		                
				   }
					 
				 }
                
				if (!dirType.equalsIgnoreCase("INBOUND")) {
					
					
					
						dtCondStr =    " and ISNULL(a.CollectionDate,'')<>'' AND (SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2))";
						extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date)  desc,dono desc";
					
                if (afrmDate.length() > 0) {
                	sCondition1 = sCondition1 + dtCondStr + "  >= '" 
    						+ afrmDate
    						+ "'  ";
    				if (atoDate.length() > 0) {
    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
    					+ atoDate
    					+ "'  ";
    				}
    			} else {
    				if (atoDate.length() > 0) {
    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
    					+ atoDate
    					+ "'  ";
    				}
    			}   
               
				 }
				
               
	          
                if (dirType.equalsIgnoreCase("INBOUND")) {
				   	String aQuery="";
                	if(viewdate.equals("ByOrderDate")){
				       aQuery = "select distinct b.pono,b.polnno,isnull(a.ordertype,'')as ordertype,isnull(a.CRBY,'') as users,isnull(a.Remark1,'') as remarks,isnull(a.jobNum,'') jobNum,a.custname,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.CollectionDate,'') TRANDATE,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS CollectionDate ,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) as qty,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.UNITMO,'')as UOM,isnull(a.remark3,'') remarks3,"
				    	+ "ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,"
         				+ "CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS FROM "
						+ "["
						+ plant
						+ "_"
						+ "pohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "podet] b, " 
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c where a.pono=b.pono and b.ITEM=c.item  " + sCondition1;
                	} else if(viewdate.equals("ByDeliveryDate")){
                		  aQuery = "select distinct b.pono,b.polnno,isnull(a.ordertype,'')as ordertype,isnull(a.Remark1,'') as remarks,isnull(a.jobNum,'') jobNum,a.custname,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.deldate,'') TRANDATE,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) AS CollectionDate ,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) as qty,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.UNITMO,'')as UOM,isnull(a.remark3,'') remarks3,"
                				+ "ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'') TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,"
                   				+ "CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS FROM "
         						+ "["
         						+ plant
         						+ "_"
         						+ "pohdr] a,"
         						+ "["
         						+ plant
         						+ "_"
         						+ "podet] b, " 
         						+ "["
         						+ plant
         						+ "_"
         						+"ITEMMST] c where a.pono=b.pono and b.ITEM=c.item  " + sCondition1;
                	}

				arrList = movHisDAO.selectForReport(aQuery, ht,extraCon);
			}
                if(dirType.equalsIgnoreCase("PRODUCTREMARKS")){
                	extraCon=" order by CollectionDate,b.pono,d.id_remarks";
              	  String aQuery="";
              	if(viewdate.equals("ByOrderDate")){
              		aQuery = "select distinct b.pono,b.polnno,isnull(a.ordertype,'')as ordertype,isnull(a.Remark1,'') as remarks,isnull(a.jobNum,'') jobNum,a.custname,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.CollectionDate,'') TRANDATE,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS CollectionDate ,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) as qty,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(a.remark3,'') remarks3,isnull(d.remarks,'') prdremarks,d.id_remarks from "
          					+ "["
          					+ plant
          					+ "_"
          					+ "pohdr] a,"
          					+ "["
          					+ plant
          					+ "_"
          					+ "podet] b, " 
          					+ "["
          					+ plant
          					+ "_"
          					+"ITEMMST] c, "
          					+ "["
          					+ plant
          					+ "_"
          					+"PODET_REMARKS] d where a.pono=b.pono and b.ITEM=c.item and b.polnno=d.polnno and b.pono=d.pono " + sCondition1;
             	} else if(viewdate.equals("ByDeliveryDate")){
             		  aQuery = "select distinct b.pono,b.polnno,isnull(a.ordertype,'')as ordertype,isnull(a.Remark1,'') as remarks,isnull(a.jobNum,'') jobNum,a.custname,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.deldate,'') TRANDATE,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) AS CollectionDate ,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) as qty,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(a.remark3,'') remarks3,isnull(d.remarks,'') prdremarks,d.id_remarks from "
      						+ "["
      						+ plant
      						+ "_"
      						+ "pohdr] a,"
      						+ "["
      						+ plant
      						+ "_"
      						+ "podet] b, " 
      						+ "["
      						+ plant
          					+ "_"
          					+"ITEMMST] c, "
          					+ "["
          					+ plant
          					+ "_"
          					+"PODET_REMARKS] d where a.pono=b.pono and b.ITEM=c.item and b.polnno=d.polnno and b.pono=d.pono " + sCondition1;
             	}
  			       
          		arrList = movHisDAO.selectForReport(aQuery, ht,extraCon);
          	}
			if (dirType.equalsIgnoreCase("OUTBOUND")) {
				
				if (ht.size() > 0) {
					String Ststype ="";
					if (ht.get("B.LNSTAT") != null) {
						Ststype = ht.get("B.LNSTAT").toString();
					}
					if (ht.get("CUSTTYPE") != null) {
						String custtype = ht.get("CUSTTYPE").toString(); 
						sCondition1= sCondition1+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
						ht.remove("CUSTTYPE");
					}
					if(Ststype.equalsIgnoreCase("INTRANSIT")) {
						ht.remove("B.LNSTAT");
						sCondition1 = sCondition1+"AND  A.DONO IN(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant+"_DNPLHDR DN JOIN "+plant+"_DNPLDET DT ON DN.ID=DT.HDRID JOIN "+plant+"_SHIPPINGHDR D ON DT.HDRID = D.DNPLID WHERE "
								+ "DN.DONO= B.DONO AND DN.GINO=GINO AND DT.ITEM = B.ITEM AND DT.LNNO = B.DOLNNO AND D.INTRANSIT_STATUS='INTRANSIT' and isnull(d.DELIVERY_STATUS,'')=''),'') )";
					}else if(Ststype.equalsIgnoreCase("DELIVERED")) {
						ht.remove("B.LNSTAT");
						sCondition1 = sCondition1+"AND  A.DONO IN(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant+"_DNPLHDR DN JOIN "+plant+"_DNPLDET DT ON DN.ID=DT.HDRID JOIN "+plant+"_SHIPPINGHDR D ON DT.HDRID = D.DNPLID WHERE "
								+ "DN.DONO= B.DONO AND DN.GINO=GINO AND DT.ITEM = B.ITEM AND DT.LNNO = B.DOLNNO AND D.DELIVERY_STATUS='DELIVERED'),'') )";
					}
					if (ht.get("B.LNSTAT") != null) {
						sCondition1 = sCondition1+" AND a.DONO not in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant +"_DNPLHDR DN join "+plant +"_DNPLDET DT on DN.ID=DT.HDRID join "+plant +"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = b.ITEM and DT.LNNO = b.DOLNNO and d.DELIVERY_STATUS='DELIVERED'),'') ) ";
						sCondition1 = sCondition1+" AND a.DONO not in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant +"_DNPLHDR DN join "+plant +"_DNPLDET DT on DN.ID=DT.HDRID join "+plant +"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = b.ITEM and DT.LNNO = b.DOLNNO and d.INTRANSIT_STATUS='INTRANSIT'),'') ) ";
					}
					
				}
				
				String aQuery = "select distinct b.dono,b.dolnno,isnull(a.CRBY,'') as users,isnull(a.ordertype,'') as ordertype,isnull(a.Remark1,'') as remarks,isnull(a.Remark3,'') as remarks2,isnull(a.jobNum,'') jobNum,a.custname,b.item,isnull(c.itemdesc,'') itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(b.unitprice,0) unitprice,isnull(a.collectionDate,'') TRANDATE,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as CollectionDate,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,isnull(a.deliverydate,'')deliverydate,isnull(a.timeslots,'') deliverytime,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'')as UOM,isnull(a.status_id,'')as status_id,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname,ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,ORDER_STATUS,ISNULL((select isnull(Customer_Status_Id,'') CustomerStatusId FROM ["+plant+"_CUSTMST]  WHERE CUSTNO=a.custcode),'') CustomerStatusId,ISNULL((select isnull(Customer_Type_Id,'') CustomerTypeId FROM ["+plant+"_CUSTMST]  WHERE CUSTNO=a.custcode),'') Customer_Type_Id,ISNULL((SELECT TOP 1 ISNULL(D.DELIVERY_STATUS,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= b.DONO AND DN.GINO=GINO and DT.ITEM = b.ITEM and DT.LNNO = b.DOLNNO),'')as DELIVERY_STATUS,ISNULL((SELECT TOP 1 ISNULL(D.INTRANSIT_STATUS,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= b.DONO AND DN.GINO=GINO and DT.ITEM = b.ITEM and DT.LNNO = b.DOLNNO),'')as INTRANSIT_STATUS from "
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
						+"ITEMMST] c where a.dono=b.dono and b.ITEM=c.item and a.custcode <> '' " +posext+ sCondition1 ;

				
				arrList = movHisDAO.selectForReport(aQuery, ht,extraCon);
			}
			
			if (dirType.equalsIgnoreCase("OUTBOUNDPRODUCTREMARKS")) {
				
				if (ht.size() > 0) {
					if (ht.get("CUSTTYPE") != null) {
						String custtype = ht.get("CUSTTYPE").toString();
						sCondition1= sCondition1+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
						ht.remove("CUSTTYPE");
					}
				}
				extraCon=" order by CollectionDate,b.dono,d.id_remarks";
				String aQuery = "select distinct b.dono,b.dolnno,isnull(a.ordertype,'') as ordertype,isnull(a.Remark1,'') as remarks,isnull(a.Remark3,'') as remarks2,isnull(a.jobNum,'') jobNum,a.custname,b.item,isnull(c.itemdesc,'') itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(b.unitprice,0) unitprice,isnull(a.collectionDate,'') TRANDATE,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as CollectionDate,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,isnull(a.deliverydate,'')deliverydate,isnull(a.timeslots,'') deliverytime,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname,isnull(d.remarks,'') prdremarks,d.id_remarks from "
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
						+"ITEMMST] c,"
						+ "["
	  					+ plant
	  					+ "_"
	  					+"DODET_REMARKS] d where a.dono=b.dono and b.ITEM=c.item and b.dolnno=d.dolnno and b.dono = d.dono  " + sCondition1 ;

				
				arrList = movHisDAO.selectForReport(aQuery, ht,extraCon);
			}
			if (dirType.equalsIgnoreCase("OUTBOUNDPRICE")) {
				String aQuery = "select b.dono,b.dolnno,isnull(a.ordertype,'') as ordertype,isnull(a.jobNum,'') jobNum,a.custname,b.item,isnull(b.unitprice,0) unitprice,isnull(B.TRANDATE,'') TRANDATE,b.lnstat ,isnull(b.qtyor,0) qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT from "
						+ "["
						+ plant
						+ "_"
						+ "dohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "dodet] b where a.dono=b.dono  " + sCondition +" order by b.item ";

				
				arrList = movHisDAO.selectForReport(aQuery, ht);
			}
			if (dirType.equalsIgnoreCase("TRANSFER")) {
				String aQuery = "select b.tono,'' as ordertype,a.jobNum,a.fromwarehouse,a.towarehouse,a.custname,b.item,b.itemdesc,B.TRANDATE TRANDATE,b.pickstatus,b.lnstat,isnull(a.CRBY,'') users,isnull(b.qtyor,0) qtyor,isnull(b.qtypick,0) as qtypick,isnull(b.UNITMO,'')as UOM,isnull(b.qtyrc,0) as qtyrc,"
						+ "collectionDate as OrderDate," 
						+ " CAST((SUBSTRING(a.collectionDate, 7, 4) + SUBSTRING(a.collectionDate, 4, 2) + SUBSTRING(a.collectionDate, 1, 2)) AS date)  AS toexceltransactiondate,"
						+ "isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype from "
						+ "["
						+ plant
						+ "_"
						+ "tohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "todet] b," 
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c where a.tono=b.tono and b.ITEM=c.item and a.custcode <> ''	" + sCondition;
				arrList = movHisDAO.selectForReport(aQuery, ht);

			}

			if (dirType.equalsIgnoreCase("RECEIVE")) {
				String aQuery = "select distinct a.pono,'' as ordertype,a.cname,a.item,isnull(a.itemdesc,'') itemdesc,isnull(a.batch,'') batch,isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,isnull(a.putawayqty,0) putawayqty,isnull(a.reverseqty,'')reverseqty,isnull(a.loc,'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat  from "
						+ "["
						+ plant
						+ "_"
						+ "recvdet] a,"
						+ "["
						+ plant
						+ "_"
						+ "podet] b where a.pono<>'' and a.pono=b.pono "
						+ sCondition;
				arrList = movHisDAO.selectForReport(aQuery, ht);
			}

			if (dirType.equalsIgnoreCase("ISSUE")) {
				String aQuery = "select a.dono,'' as ordertype,a.cname,a.item,isnull(a.itemdesc,'') itemdesc,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.reverseqty,0) reverseqty,isnull(b.qtyis,0) issueqty,isnull(a.loc,'') loc,isnull(a.status,0) status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat   from "
						+ "["
						+ plant
						+ "_"
						+ "shiphis] a, "
						+ "["
						+ plant
						+ "_"
						+ "dodet] b where a.dono<>''  and a.dono=b.dono"
						+ sCondition;
				;
				arrList = movHisDAO.selectForReport(aQuery, ht);
			}
			if (dirType.equalsIgnoreCase("WORKORDER")) {
				String aQuery = "select b.wono,isnull(a.Remark1,'') as remarks,a.jobNum,a.custname,b.item,b.itemdesc,a.collectionDate TRANDATE,isnull(b.lnstat,'') lnstat ,isnull(b.qtyor,0) as orderqty, isnull(b.qtyfin,0) as finishedqty, isnull(b.wstatus,'') wstatus,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype," 
						+ " CAST((SUBSTRING(a.collectionDate, 7, 4) + SUBSTRING(a.collectionDate, 4, 2) + SUBSTRING(a.collectionDate, 1, 2)) AS date)  AS toexceltransactiondate from "
						+ "["
						+ plant
						+ "_"
						+ "wohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "wodet] b,"
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c where a.wono=b.wono and b.ITEM=c.item " + sCondition1 ;

				
				arrList = movHisDAO.selectForReport(aQuery, ht);
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getWorkOrderSummaryList:", e);
		}
		return arrList;
	}
	
	
	public ArrayList getTotalStockSaleLstWithDates(Hashtable ht, String fromdt, String todt, String productDesc) throws Exception {
	    ArrayList alData = new ArrayList();
	    Connection con = null;
        String extCond = "";
        String fdate = "", tdate = "";
        String custtypecond = "";

        if (fromdt == null) fromdt = ""; else fromdt = fromdt.trim();
        if (fromdt.length() > 5)
            fdate = fromdt.substring(6) + fromdt.substring(3, 5) + fromdt.substring(0, 2);

        if (todt == null) todt = ""; else todt = todt.trim();
        if (todt.length() > 5)
            tdate = todt.substring(6) + todt.substring(3, 5) + todt.substring(0, 2);
        if (ht.size() > 0) {
			if (ht.get("CUSTTYPE") != null) {
				String custtype = ht.get("CUSTTYPE").toString();
				custtypecond = " AND CNAME in (select VNAME from "+ht.get("PLANT")+"_VENDMST where SUPPLIER_TYPE_ID like '"+custtype+"%')";
				ht.remove("CUSTTYPE");
			}
		}

       

        if (productDesc.length() > 0) {
            if (productDesc.contains("%")) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
            }
            extCond = " and replace(ITEMDESC,' ','') like '%" + productDesc.replaceAll(" ", "") + "%' ";
        } else {
            extCond = " AND PONO <> ''";
        }

        String condition = "";
        if (ht.size() > 0) {
            condition = formCondition(ht);
        }

        String bcondition = "";
        if (fdate.length() > 0) {
            bcondition = " AND SUBSTRING(RECVDATE, 7, 4) + SUBSTRING(RECVDATE, 4, 2) + SUBSTRING(RECVDATE, 1, 2) >= '" + fdate + "'  ";
            if (tdate.length() > 0) {
                bcondition += " AND SUBSTRING(RECVDATE, 7, 4) + SUBSTRING(RECVDATE, 4, 2) + SUBSTRING(RECVDATE, 1, 2) <= '" + tdate + "'  ";
            }
        } else {
            if (tdate.length() > 0) {
                bcondition = " AND SUBSTRING(RECVDATE, 7, 4) + SUBSTRING(RECVDATE, 4, 2) + SUBSTRING(RECVDATE, 1, 2) <= '" + tdate + "'  ";
            }
        }



        StringBuilder sql = new StringBuilder(" SELECT DISTINCT ITEM, (SELECT ISNULL(ITEMDESC, '') FROM [" + ht.get("PLANT") + "_itemmst] WHERE item = a.item) ITEMDESC, CNAME, '' AS CUSTCODE, SUM(RECQTY) PICKQTY ");
        sql.append(" FROM [" + ht.get("PLANT") + "_RECVDET] A");
        sql.append(" WHERE PONO IN (SELECT SS.PONO FROM [" + ht.get("PLANT") + "_POHDR] SS) AND ");
        sql.append(condition).append(bcondition).append(extCond).append(custtypecond);
        sql.append(" GROUP BY ITEM, CNAME");

	    try {
	        con = com.track.gates.DbBean.getConnection();

	        this.mLogger.query(this.printLog, sql.toString());
	        alData = selectData(con, sql.toString());

	    } catch (Exception e) {
	        this.mLogger.exception(this.printLog, "", e);
	        throw e;
	    } finally {
	        if (con != null) {
	            DbBean.closeConnection(con);
	        }
	    }
	    return alData;
	}

	 public ArrayList getSupplier(Hashtable ht,String plant) throws Exception {

	        java.sql.Connection con = null;
	        ArrayList al = new ArrayList();
	        try {
	                con = com.track.gates.DbBean.getConnection();
	                UserLocUtil userLocUtil = new UserLocUtil();
	                userLocUtil.setmLogger(mLogger);
	                         
	               
	                String sQry = "select distinct custcode from "
	                                + "["
	                                + plant
	                                + "_"
	                                + "pohdr] where custcode like '"+ht.get("CUSTCODE")+"%'"
	                                       
	                                + " ORDER BY custcode ";
	                //this.mLogger.query(this.printQuery, sQry);
	                 al = selectData(con, sQry);

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
	
	
	public ArrayList getSupplierPOInvoiceSummary(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String viewdate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	        if (ht.size() > 0) {
				if (ht.get("SUPPLIERTYPE") != null) {
					String suppliertype = ht.get("SUPPLIERTYPE").toString();
					sCondition= sCondition+" AND custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
					ht.remove("SUPPLIERTYPE");
				}
			}
	        String dtCondStr="",extraCon="";
	        StringBuffer sql;
	   		        if(viewdate.equals("ByOrderDate")){
			        	 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
						 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
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
		           
				   } else if(viewdate.equals("ByDeliveryDate")){
					     dtCondStr =    " and ISNULL(a.DelDate,'')<>'' AND (SUBSTRING(a.DelDate, 7, 4) + '-' + SUBSTRING(a.DelDate, 4, 2) + '-' + SUBSTRING(a.DelDate, 1, 2))";
					     extraCon=" order by a.custname,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) ";
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
				}  else
				{
					 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
					 extraCon= "order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) desc,a.pono desc,a.custname";
					 
					 if (afrmDate.length() > 0) {
                     	sCondition = sCondition + dtCondStr + " >= '" 
         						+ afrmDate
         						+ "'  ";
         				if (atoDate.length() > 0) {
         					sCondition = sCondition + dtCondStr+ " <= '" 
         					+ atoDate
         					+ "'  ";
         				}
         			} else {
         				if (atoDate.length() > 0) {
         					sCondition = sCondition + dtCondStr + "  <= '" 
         					+ atoDate
         					+ "'  ";
         				}
         			}
				}
		                          
                        //start code by radhika for wildcard search supplier name for inbound order summary with price on 26 aug 13
                        if (custname.length()>0){
                         	custname = StrUtils.InsertQuotes(custname);
                         	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                         }
                                             
        	      // query changed by radhika to get remarks in summary inbound order details(with cost)  CAST((ISNULL((b.qtyor*unitcost*inbound_gst) / 100,0)) as decimal(20,7)) taxval
                        if(viewdate.equals("ByOrderDate")){
                        	sql = new StringBuffer(" select distinct a.custcode,a.CURRENCYID,a.CURRENCYUSEQT,isnull(a.CRBY,'') as users,a.pono,b.polnno,b.UNITMO,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");
            				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
            				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, isnull(b.unitcost,0) unitcost,ISNULL((((unitcost*inbound_gst)/100)*b.qtyor),0) taxval,");
            				sql.append("b.crat,ISNULL(b.qtyor*unitcost,0) OrderCost,ISNULL(b.qtyrc*unitcost,0) RecvCost,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'') as UOM,isnull(a.status_id,'')as status_id,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
            				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS,TAXID ");
            				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
            				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
            				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
            				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                          }
                          else if(viewdate.equals("ByDeliveryDate")){
                        	sql = new StringBuffer(" select distinct a.custcode,a.CURRENCYID,a.CURRENCYUSEQT,a.pono,b.polnno,b.item,isnull(a.CRBY,'') as users,b.UNITMO,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,");
              				sql.append("a.custname,b.polnno,isnull(a.DelDate,'') as CollectionDate  ,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
              				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, isnull(b.UNITCOST_AOD,0) unitcost,ISNULL((((UNITCOST_AOD*inbound_gst)/100)*b.qtyor),0) taxval,");
              				sql.append("b.crat,ISNULL(b.qtyor*UNITCOST_AOD,0) OrderCost,ISNULL(b.qtyrc*UNITCOST_AOD,0) RecvCost,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'') as UOM,isnull(a.status_id,'')as status_id,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
              				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS,TAXID ");
              				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
              				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
              				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
              				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                          }else{
                        	sql = new StringBuffer(" select distinct a.custcode,a.CURRENCYID,a.CURRENCYUSEQT,a.pono,b.polnno,b.item,isnull(a.CRBY,'') as users,b.UNITMO,c.itemdesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");
              				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
              				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, isnull(isnull(b.UNITCOST_AOD,b.UNITCOST),0) unitcost,ISNULL((((isnull(b.UNITCOST_AOD,b.UNITCOST)*inbound_gst)/100)*b.qtyor),0) taxval,");
              				sql.append("b.crat,ISNULL(b.qtyor*isnull(b.UNITCOST_AOD,b.UNITCOST),0) OrderCost,ISNULL(b.qtyrc*isnull(b.UNITCOST_AOD,b.UNITCOST),0) RecvCost,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(b.UNITMO,'') as UOM,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
              				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS,TAXID ");
              				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
              				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
              				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
              				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                          }
            				
        				
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
	
	public ArrayList getSupplierPOInvoiceSummaryByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String viewdate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	        
	        if (ht.size() > 0) {
				if (ht.get("SUPPLIERTYPE") != null) {
					String suppliertype = ht.get("SUPPLIERTYPE").toString();
					sCondition= sCondition+" AND custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
					ht.remove("SUPPLIERTYPE");
				}
			}
	        
	        String dtCondStr="",extraCon="";
	        StringBuffer sql;
               		        
			        if(viewdate.equals("ByOrderDate")){
			        	System.out.println("come in"+viewdate);
						 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
						 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
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
		           
				   } else if(viewdate.equals("ByDeliveryDate")){
					     dtCondStr =    " and ISNULL(a.DelDate,'')<>'' AND (SUBSTRING(a.DelDate, 7, 4) + '-' + SUBSTRING(a.DelDate, 4, 2) + '-' + SUBSTRING(a.DelDate, 1, 2))";
					     extraCon=" order by a.custname,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) ";
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
				}  else
				{
					 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
					 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
					 
					 if (afrmDate.length() > 0) {
                     	sCondition = sCondition + dtCondStr + " >= '" 
         						+ afrmDate
         						+ "'  ";
         				if (atoDate.length() > 0) {
         					sCondition = sCondition + dtCondStr+ " <= '" 
         					+ atoDate
         					+ "'  ";
         				}
         			} else {
         				if (atoDate.length() > 0) {
         					sCondition = sCondition + dtCondStr + "  <= '" 
         					+ atoDate
         					+ "'  ";
         				}
         			}
				}
		                    if (custname.length()>0){
                         	custname = StrUtils.InsertQuotes(custname);
                         	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                         }
                                             
        	      // query changed by radhika to get remarks in summary inbound order details(with cost) 
                        if(viewdate.equals("ByOrderDate")){
                        	sql = new StringBuffer(" select distinct a.custcode,a.pono,b.polnno,b.item,c.itemdesc,isnull(a.CRBY,'') as users,b.UNITMO,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");
            				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
            				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, isnull(b.unitcost,0) unitcost,");
            				sql.append("(ISNULL(b.qtyor*unitcost,0)*isnull(b.prodgst,0))/100 as taxval,");
            				sql.append("b.crat,ISNULL(b.qtyor*unitcost,0) OrderCost,ISNULL(b.qtyrc*unitcost,0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.prodgst,0) as Tax,isnull(b.UNITMO,'') as UOM,isnull(a.remark3,'') remarks3, ");
            				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
            				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
            				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
            				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
            				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                          }
                          else if(viewdate.equals("ByDeliveryDate")){
                        	sql = new StringBuffer(" select distinct a.custcode,a.pono,b.polnno,isnull(a.CRBY,'') as users,b.UNITMO,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,");
              				sql.append("a.custname,b.polnno,isnull(a.DelDate,'') as CollectionDate  ,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
              				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, isnull(b.unitcost,0) unitcost,");
              				sql.append("(ISNULL(b.qtyor*unitcost,0)*isnull(b.prodgst,0))/100 as taxval,");
              				sql.append("b.crat,ISNULL(b.qtyor*unitcost,0) OrderCost,ISNULL(b.qtyrc*unitcost,0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.UNITMO,'') as UOM,isnull(b.prodgst,0) as Tax,isnull(a.remark3,'') remarks3, ");
              				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
              				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
              				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
              				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
              				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                          }else{
                        	sql = new StringBuffer(" select distinct a.custcode,a.pono,b.polnno,isnull(a.CRBY,'') as users,b.item,c.itemdesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");
              				sql.append("a.custname,b.polnno,b.UNITMO,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
              				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, isnull(b.unitcost,0) unitcost,");
              				sql.append("(ISNULL(b.qtyor*unitcost,0)*isnull(b.prodgst,0))/100 as taxval,");
              				sql.append("b.crat,ISNULL(b.qtyor*unitcost,0) OrderCost,ISNULL(b.qtyrc*unitcost,0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.UNITMO,'') as UOM,isnull(b.prodgst,0) as Tax,isnull(a.remark3,'') remarks3, ");
              				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
              				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
              				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
              				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
              				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                          }
            				
        				
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
	
	 public ArrayList getCustomer(Hashtable ht,String plant) throws Exception {

	        java.sql.Connection con = null;
	        ArrayList al = new ArrayList();
	        try {
	                con = com.track.gates.DbBean.getConnection();
	                UserLocUtil userLocUtil = new UserLocUtil();
	                userLocUtil.setmLogger(mLogger);
	                         
	               
	                String sQry = "select distinct custcode from "
	                                + "["
	                                + plant
	                                + "_"
	                                + "dohdr] where custcode like '"+ht.get("CUSTCODE")+"%'"
	                                       
	                                + " ORDER BY custcode ";
	                //this.mLogger.query(this.printQuery, sQry);
	            
	                 al = selectData(con, sQry);

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
	 
//	 resvi starts
	 
	 public ArrayList getConsignmentDOInvoiceSummary(Hashtable ht, String afrmDate,
			 String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype) {
			ArrayList arrList = new ArrayList();
			String sCondition = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			StringBuffer sql = new StringBuffer();
			try {
				MovHisDAO movHisDAO = new MovHisDAO();
				movHisDAO.setmLogger(mLogger);
			     if (itemDesc.length() > 0 ) {
			        if (itemDesc.indexOf("%") != -1) {
			        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
			        }
			        sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		        }
	           if (custname.length()>0){
	                	custname = StrUtils.InsertQuotes(custname);
	                 	sCondition =  sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
	              }
	           if (ht.size() > 0) {
					if (ht.get("CUSTTYPE") != null) {
						String custtype = ht.get("CUSTTYPE").toString();
						sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
						ht.remove("CUSTTYPE");
					}
				}
	                         
			    String dtCondStr =    " and ISNULL(a.recvdate,'')<>'' AND CAST((SUBSTRING(a.recvdate, 7, 4) + '-' + SUBSTRING(a.recvdate, 4, 2) + '-' + SUBSTRING(a.recvdate, 1, 2)) AS date)";
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
	                      sCondition = sCondition +" and (a.PONO like 'C%' ) and  a.PONO not like 'SM%' ";
	                      if(sorttype.equalsIgnoreCase("PRODUCT"))
	                      {       
	                      		extraCon="GROUP BY a.ITEM,a.PONO,a.CNAME,a.RECQTY,B.RECQTY,A.RECVDATE,B.UNITCOST,B.UOM,A.users,A.LNNO),"
	                        		  +"Ranked AS ("
	                        		  +"SELECT custcode,tono,users,UOM,item,itemdesc,orderType,GINO,fromwarehouse,towarehouse,custname,issuedate,trandate,jobnum,remarks,remarks2,"
	                        		  +"qtyor ,qtypick ,qty,qtyac,unitprice,issPrice,prd_brand_id,PRD_DEPT_ID,prd_cls_id,itemtype,status,Tax,empname,taxval,"
	                        		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
	                        		  +" from SORTTYPE)"
	                        		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
	                      }
	                      else if(sorttype.equalsIgnoreCase("CUSTOMER"))
	                      {
	                      		extraCon="GROUP BY a.CNAME,A.PONO,a.ITEM,a.RECQTY,B.RECQTY,A.RECVDATE,B.UNITCOST,B.UOM,A.users,A.LNNO),"
	                          		  +"Ranked AS ("
	                          		  +"SELECT custcode,tono,item,users,UOM,itemdesc,orderType,GINO,fromwarehouse,towarehouse,custname,issuedate,trandate,jobnum,remarks,remarks2,"
	                          		  +"qtyor ,qtypick ,qty,qtyac,unitprice,issPrice,prd_brand_id,PRD_DEPT_ID,prd_cls_id,itemtype,status,Tax,empname,taxval,"
	                          		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
	                          		  +" from SORTTYPE)"
	                          		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
	                    
	                      }    
	                      
	                      else{    
	                    	  	extraCon = " order by A.CNAME, A.PONO,A.ITEM,CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date)";
	                      }
	                if(!sorttype.equalsIgnoreCase("")){      
	                sql.append(" WITH SORTTYPE AS( ");}
	                sql.append("SELECT A.PONO as tono,A.ITEM as item,ISNULL(A.RECQTY,0) as qtypick,ISNULL(B.RECQTY,0) as qty,A.RECVDATE as issuedate,");
					sql.append("ISNULL(B.UNITCOST,0) as unitprice,(ISNULL(B.RECQTY,0)*ISNULL(B.UNITCOST,0))issPrice,A.CNAME as custname,");
					sql.append("CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
					
					sql.append("ISNULL((SELECT TOP 1 GINO FROM ["+ plant +"_TO_PICK] WHERE ITEM=A.ITEM and TONO=A.PONO  and LNNO=A.LNNO and GINO  IS NOT NULL),'') as GINO,");
					
					sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_TODET] WHERE ITEM=A.ITEM and TONO=A.PONO and UNITMO=B.UOM GROUP BY ITEM,TONO,UNITMO),0) qtyor,");
					sql.append("ISNULL((SELECT SUM(QTYAC) FROM ["+ plant +"_TODET] WHERE ITEM=A.ITEM and TONO=A.PONO and UNITMO=B.UOM GROUP BY ITEM,TONO,UNITMO),0) qtyac,");
					sql.append("(SELECT CustCode  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) custcode,");
					
					sql.append("(SELECT FROMWAREHOUSE  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) fromwarehouse,");
					sql.append("(SELECT TOWAREHOUSE  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) towarehouse,");
					
					sql.append("(SELECT ORDERTYPE  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) orderType,");
					sql.append("(SELECT JOBNUM  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) jobNum,");
					sql.append(" ISNULL((((ISNULL(B.RECQTY,0)*ISNULL(B.unitcost,0)) * (SELECT ISNULL(CONSIGNMENT_GST,0) FROM ["+ plant +"_TOHDR]  WHERE TONO=A.PONO))/100),0) taxval,");
					sql.append("ISNULL((SELECT ISNULL(CONSIGNMENT_GST,0) FROM ["+ plant +"_TOHDR]  WHERE TONO=A.PONO),0) Tax,");				
					sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.PONO) remarks,");
					sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.PONO) remarks2,");
					sql.append("(SELECT ISNULL(TAXID,'') FROM ["+ plant +"_TOHDR] Z WHERE TONO=A.PONO) TAXID,");
					sql.append("(SELECT ISNULL(STATUS,'') FROM ["+ plant +"_TOHDR]  WHERE TONO=A.PONO) status,");
					sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
					sql.append("(SELECT ISNULL(PRD_DEPT_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) PRD_DEPT_ID,");
					sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
					sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
					sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
					sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_tohdr where TONO=A.PONO)),'') as empname, ");
					sql.append("B.UOM,");
					sql.append("A.users");
					
					sql.append(" FROM (SELECT PONO,ITEM,ITEMDESC,LNNO,RECVDATE,CNAME,ISNULL(CRBY, '') AS users,SUM(RECQTY)RECQTY FROM ["+ plant +"_RECVDET] WHERE ");
					sql.append("PONO<>'GOODSISSUE' GROUP BY PONO,ITEM,ITEMDESC,RECVDATE,CNAME,LNNO,CRBY) A, ");
					sql.append("(SELECT r.PONO,r.ITEM,TOLNNO,SUM(ORDQTY)ORDQTY ,SUM(RECQTY)RECQTY,MAX(ISNULL(r.UNITCOST,0) ) unitcost, ");
					sql.append("RECVDATE,isnull(UNITMO,'') as UOM  FROM ["+ plant +"_RECVDET] r ,[" + plant + "_" + "TODET" + "] s  WHERE s.tono=r.pono and s.TOLNNO=LNNO and s.item=r.item and r.PONO<>'GOODSISSUE' AND RECEIVESTATUS='C' GROUP BY r.PONO,r.ITEM,RECVDATE,UNITMO,TOLNNO)  B ");
					sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM and  A.LNNO=B.TOLNNO AND A.RECVDATE=B.RECVDATE "+ sCondition);
					
					   if (ht.get("TONO") != null) {
				    	   sql.append(" AND A.PONO = '" + ht.get("TONO") + "'");
				       } 
					    
					   if (ht.get("ITEM") != null) {
					    	   sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
					   }  
					   if (ht.get("JOBNUM") != null) {
	                	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  TONO=A.PONO)");
	                   }
		               
				       if (ht.get("PickStaus") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("ORDERTYPE") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("FROMWAREHOUSE") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE FROMWAREHOUSE ='" + ht.get("FROMWAREHOUSE") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("TOWAREHOUSE") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE TOWAREHOUSE ='" + ht.get("TOWAREHOUSE") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("STATUS_ID") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE STATUS ='" + ht.get("STATUS_ID") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("EMPNO") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("ITEMTYPE") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
				       }
				       if (ht.get("PRD_BRAND_ID") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
				       }
				       if (ht.get("PRD_CLS_ID") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
				       }
				       if (ht.get("PRD_DEPT_ID") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
				       }
				
					arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
					// End code modified by radhika for outorderwithprice on 18/12/12 	
			} catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getConsignmentDOInvoiceSummary:", e);
			}
			return arrList;
		}
//	 resvi ends
	 
	 
//   Resvi start By Product
	 public ArrayList getConsignmentDOInvoiceSummaryByProductGst(Hashtable ht, String afrmDate,
			 String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype) {
			ArrayList arrList = new ArrayList();
			String sCondition = "";
			Hashtable htData = new Hashtable();
			String extraCon="";
			StringBuffer sql = new StringBuffer();
			try {
				MovHisDAO movHisDAO = new MovHisDAO();
				movHisDAO.setmLogger(mLogger);
			     if (itemDesc.length() > 0 ) {
			        if (itemDesc.indexOf("%") != -1) {
			        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
			        }
			        sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		        }
	           if (custname.length()>0){
	                	custname = StrUtils.InsertQuotes(custname);
	                 	sCondition =  sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
	              }
	           if (ht.size() > 0) {
					if (ht.get("CUSTTYPE") != null) {
						String custtype = ht.get("CUSTTYPE").toString();
						sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
						ht.remove("CUSTTYPE");
					}
				}
	                         
			    String dtCondStr =    " and ISNULL(a.recvdate,'')<>'' AND CAST((SUBSTRING(a.recvdate, 7, 4) + '-' + SUBSTRING(a.recvdate, 4, 2) + '-' + SUBSTRING(a.recvdate, 1, 2)) AS date)";
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
	                      sCondition = sCondition +" and (a.PONO like 'C%' ) and  a.PONO not like 'SM%' ";
	                      if(sorttype.equalsIgnoreCase("PRODUCT"))
	                      {       
	                      		extraCon="GROUP BY a.ITEM,a.PONO,a.CNAME,a.RECQTY,B.RECQTY,A.RECVDATE,B.UNITCOST,B.UOM,A.users),"
	                        		  +"Ranked AS ("
	                        		  +"SELECT custcode,tono,users,UOM,item,itemdesc,orderType,fromwarehouse,towarehouse,custname,issuedate,trandate,jobnum,remarks,remarks2,"
	                        		  +"qtyor ,qtypick ,qty,qtyac,unitprice,issPrice,prd_brand_id,PRD_DEPT_ID,prd_cls_id,itemtype,status,Tax,empname,taxval,"
	                        		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
	                        		  +" from SORTTYPE)"
	                        		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
	                      }
	                      else if(sorttype.equalsIgnoreCase("CUSTOMER"))
	                      {
	                      		extraCon="GROUP BY a.CNAME,A.PONO,a.ITEM,a.RECQTY,B.RECQTY,A.RECVDATE,B.UNITCOST,B.UOM,A.users),"
	                          		  +"Ranked AS ("
	                          		  +"SELECT custcode,tono,item,users,UOM,itemdesc,orderType,fromwarehouse,towarehouse,custname,issuedate,trandate,jobnum,remarks,remarks2,"
	                          		  +"qtyor ,qtypick ,qty,qtyac,unitprice,issPrice,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status,Tax,empname,taxval,"
	                          		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
	                          		  +" from SORTTYPE)"
	                          		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
	                    
	                      }    
	                      
	                      else{    
	                    	  	extraCon = " order by A.CNAME, A.PONO,A.ITEM,CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date)";
	                      }
	                if(!sorttype.equalsIgnoreCase("")){      
	                sql.append(" WITH SORTTYPE AS( ");}
	                sql.append("SELECT A.PONO as tono,A.ITEM as item,ISNULL(A.RECQTY,0) as qtypick,ISNULL(B.RECQTY,0) as qty,A.RECVDATE as issuedate,");
					sql.append("ISNULL(B.UNITCOST,0) as unitprice,(ISNULL(B.RECQTY,0)*ISNULL(B.UNITCOST,0))issPrice,A.CNAME as custname,");
					sql.append("CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
					sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_TODET] WHERE ITEM=A.ITEM and TONO=A.PONO and UNITMO=B.UOM GROUP BY ITEM,TONO,UNITMO),0) qtyor,");
					sql.append("ISNULL((SELECT SUM(QTYAC) FROM ["+ plant +"_TODET] WHERE ITEM=A.ITEM and TONO=A.PONO and UNITMO=B.UOM GROUP BY ITEM,TONO,UNITMO),0) qtyac,");
					sql.append("(SELECT CustCode  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) custcode,");
					sql.append("(SELECT FROMWAREHOUSE  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) fromwarehouse,");
					sql.append("(SELECT TOWAREHOUSE  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) towarehouse,");
					sql.append("(SELECT ORDERTYPE  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) orderType,");
					sql.append("(SELECT JOBNUM  FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) jobNum,");
					sql.append(" ISNULL((((ISNULL(B.RECQTY,0)*ISNULL(B.unitcost,0)) * (SELECT ISNULL(CONSIGNMENT_GST,0) FROM ["+ plant +"_TOHDR]  WHERE TONO=A.PONO))/100),0) taxval,");
					sql.append("ISNULL((SELECT ISNULL(CONSIGNMENT_GST,0) FROM ["+ plant +"_TOHDR]  WHERE TONO=A.PONO),0) Tax,");				
					sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.PONO) remarks,");
					sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.PONO) remarks2,");
					sql.append("(SELECT ISNULL(STATUS,'') FROM ["+ plant +"_TOHDR]  WHERE TONO=A.PONO) status,");
					sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
					sql.append("(SELECT ISNULL(PRD_DEPT_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) PRD_DEPT_ID,");
					sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
					sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
					sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
					sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_tohdr where TONO=A.PONO)),'') as empname, ");
					sql.append("B.UOM,");
					sql.append("A.users");
					
					sql.append(" FROM (SELECT PONO,ITEM,ITEMDESC,LNNO,RECVDATE,CNAME,ISNULL(CRBY, '') AS users,SUM(RECQTY)RECQTY FROM ["+ plant +"_RECVDET] WHERE ");
					sql.append("PONO<>'GOODSISSUE' GROUP BY PONO,ITEM,ITEMDESC,RECVDATE,CNAME,LNNO,CRBY) A, ");
					sql.append("(SELECT r.PONO,r.ITEM,TOLNNO,SUM(ORDQTY)ORDQTY ,SUM(RECQTY)RECQTY,MAX(ISNULL(r.UNITCOST,0) ) unitcost, ");
					sql.append("RECVDATE,isnull(UNITMO,'') as UOM  FROM ["+ plant +"_RECVDET] r ,[" + plant + "_" + "TODET" + "] s  WHERE s.tono=r.pono and s.TOLNNO=LNNO and s.item=r.item and r.PONO<>'GOODSISSUE' AND RECEIVESTATUS='C' GROUP BY r.PONO,r.ITEM,RECVDATE,UNITMO,TOLNNO)  B ");
					sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM and  A.LNNO=B.TOLNNO AND A.RECVDATE=B.RECVDATE "+ sCondition);
					
					   if (ht.get("TONO") != null) {
				    	   sql.append(" AND A.PONO = '" + ht.get("TONO") + "'");
				       } 
					    
					   if (ht.get("ITEM") != null) {
					    	   sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
					   }  
					   if (ht.get("JOBNUM") != null) {
	                	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  TONO=A.PONO)");
	                   }
		               
				       if (ht.get("PickStaus") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("ORDERTYPE") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("FROMWAREHOUSE") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE FROMWAREHOUSE ='" + ht.get("FROMWAREHOUSE") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("TOWAREHOUSE") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE TOWAREHOUSE ='" + ht.get("TOWAREHOUSE") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("STATUS_ID") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE STATUS ='" + ht.get("STATUS_ID") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("EMPNO") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND  TONO=A.PONO)");
				       }
				       if (ht.get("ITEMTYPE") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
				       }
				       if (ht.get("PRD_BRAND_ID") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
				       }
				       if (ht.get("PRD_CLS_ID") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
				       }
				       if (ht.get("PRD_DEPT_ID") != null) {
				    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
				       }
				
					arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
					// End code modified by radhika for outorderwithprice on 18/12/12 	
			} catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getConsignmentDOInvoiceSummary:", e);
			}
			return arrList;
		}
//	  resvi ends

	 public ArrayList getCustomerDOInvoiceSummary(Hashtable ht, String afrmDate,
				String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype, String ispos) {
			ArrayList arrList = new ArrayList();
			String sCondition = "";
			String extraCon="";
//			String taxval;
			StringBuffer sql = new StringBuffer();
			try {
				MovHisDAO movHisDAO = new MovHisDAO();
				movHisDAO.setmLogger(mLogger);
				
				String posext="";
			   	if(ispos.equalsIgnoreCase("3"))
			   		posext =  " AND ORDERTYPE = 'POS' " ;
			   	else if(ispos.equalsIgnoreCase("2"))
			   		posext =  " AND ORDERTYPE != 'POS' " ;
				
				//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
    	        if (itemDesc.length() > 0 ) {
    		        if (itemDesc.indexOf("%") != -1) {
    		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
    		        }
    		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
    	        }
	             if (custname.length()>0){
	                  	custname = StrUtils.InsertQuotes(custname);
	                   	sCondition =  sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
	                }
	             if (ht.size() > 0) {
	 				if (ht.get("CUSTTYPE") != null) {
	 					String custtype = ht.get("CUSTTYPE").toString();
	 					sCondition= sCondition+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
	 					ht.remove("CUSTTYPE");
	 				}
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
	                        if(sorttype.equalsIgnoreCase("CUSTOMER"))
	                        {
	                        	extraCon="GROUP BY A.CustCode,a.CustName,A.DONO,B.DOLNNO,b.ITEM,c.Remark1,c.ItemDesc,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
	                            		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.PRD_DEPT_ID,c.itemtype,a.status_id,OUTBOUND_GST,EMPNO,qtyor,qtypick,qtyis,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
	                            		  +"Ranked AS ("
	                            		  +"SELECT custcode,dono,UOM,dolnno,item,itemdesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
	                            		  +"qtyor ,qtypick ,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
	                            		  +"RANK() OVER (PARTITION BY custcode   ORDER BY ordPrice desc) rnk"
	                            		  +" from SORTTYPE)"
	                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY custcode)desc";
	                      
	                        }
	                        else if(sorttype.equalsIgnoreCase("PRODUCT"))
	                        {
	                        	extraCon="GROUP BY b.ITEM,c.Remark1,c.ItemDesc ,A.DONO,B.DOLNNO,A.CustCode,a.CustName,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
	                            		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.PRD_DEPT_ID,c.prd_cls_id,c.itemtype,a.status_id,OUTBOUND_GST,EMPNO,qtyor,qtypick,qtyis,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
	                            		  +"Ranked AS ("
	                            		  +"SELECT custcode,dono,UOM,dolnno,item,itemdesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
	                            		  +"qtyor ,qtypick ,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
	                            		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY ordPrice desc) rnk"
	                            		  +" from SORTTYPE)"
	                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY ITEM)desc";
	                       
	                        }
	                        else{
	                        	extraCon = " order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
	                        }
					// Start code modified by radhika for outorderwithprice on 18/12/12 
	                if(!sorttype.equalsIgnoreCase("")){      
	                sql.append(" WITH SORTTYPE AS( ");}
	                sql.append(" select distinct a.custcode,a.dono,b.dolnno,b.item,c.itemdesc,isnull(a.CRBY,'') as users,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,a.custname,");
					sql.append("a.CollectionDate,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS trandate,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,isnull(b.UNITMO,'') as UOM,isnull(a.Remark3,'') as remarks2,b.lnstat,isnull(b.qtyor,0) qtyor," );
					sql.append("isnull(b.qtypick,0) qtypick,isnull(b.qtyis,0) qty, isnull(b.unitprice,0) unitprice,ISNULL(((((qtyor*b.unitprice)*OUTBOUND_GST)/100)),0) as taxval,");
					sql.append("b.crat, ISNULL(qtyor*b.unitprice,0) ordPrice, ISNULL(qtyis*b.unitprice,0) issPrice,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') deliverytime,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(a.OUTBOUND_GST,0) as Tax,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname ");
					sql.append(",ISNULL((select isnull(Customer_Status_Id,'') CustomerStatusId FROM ["+plant+"_CUSTMST]  WHERE CUSTNO=a.custcode),'') CustomerStatusId,ISNULL((select isnull(Customer_Type_Id,'') CustomerTypeId FROM ["+plant+"_CUSTMST]  WHERE CUSTNO=a.custcode),'') Customer_Type_Id ");
					sql.append(",ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXID,'') TAXID,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ");
					sql.append(" from " + "[" + plant + "_" + "dohdr" + "] a,");
					sql.append( "[" + plant + "_" + "dodet" + "] b,["+ plant + "_" + "ITEMMST]c");
					sql.append(" where a.dono=b.dono and b.ITEM=c.item and a.custcode <> '' " +posext+ sCondition);
					arrList = movHisDAO.selectForReport(sql.toString(), ht,extraCon);
					// End code modified by radhika for outorderwithprice on 18/12/12 	
			} catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getCustomerDOInvoiceSummary:", e);
			}
			return arrList;
		}
	 
	//CREATED BY NAVAS FEB20
	 
		 public ArrayList getCustomerTOInvoiceSummary(Hashtable ht, String afrmDate,
					String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype) {
				ArrayList arrList = new ArrayList();
				String sCondition = "";
				String extraCon="";
//				String taxval;
				StringBuffer sql = new StringBuffer();
				try {
					MovHisDAO movHisDAO = new MovHisDAO();
					movHisDAO.setmLogger(mLogger);
					//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	 	        if (itemDesc.length() > 0 ) {
	 		        if (itemDesc.indexOf("%") != -1) {
	 		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
	 		        }
	 		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	 	        }
		             if (custname.length()>0){
		                  	custname = StrUtils.InsertQuotes(custname);
		                   	sCondition =  sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
		                }
		             if (ht.size() > 0) {
		 				if (ht.get("CUSTTYPE") != null) {
		 					String custtype = ht.get("CUSTTYPE").toString();
		 					sCondition= sCondition+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
		 					ht.remove("CUSTTYPE");
		 				}
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
		                        if(sorttype.equalsIgnoreCase("CUSTOMER"))
		                        {
		                        	extraCon="GROUP BY A.CustCode,a.CustName,A.TONO,B.TOLNNO,a.fromwarehouse,a.towarehouse,b.ITEM,c.Remark1,c.ItemDesc,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
		                            		  +"b.CRAT,DELIVERYDATE,c.prd_brand_id,c.prd_cls_id,c.PRD_DEPT_ID,c.itemtype,CONSIGNMENT_GST,EMPNO,qtyor,qtyac,qtypick,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
		                            		  +"Ranked AS ("
		                            		  +"SELECT custcode,tono,UOM,tolnno,item,itemdesc,a.fromwarehouse,a.towarehouse,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
		                            		  +"qtyor ,qtypick ,qty,qtyac,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
		                            		  +"RANK() OVER (PARTITION BY custcode   ORDER BY ordPrice desc) rnk"
		                            		  +" from SORTTYPE)"
		                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY custcode)desc";
		                      
		                        }
		                        else if(sorttype.equalsIgnoreCase("PRODUCT"))
		                        {
		                        	extraCon="GROUP BY b.ITEM,c.Remark1,c.ItemDesc ,A.TONO,B.TOLNNO,a.fromwarehouse,a.towarehouse,A.CustCode,a.CustName,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
		                            		  +"b.CRAT,DELIVERYDATE,c.prd_brand_id,c.prd_cls_id,c.PRD_DEPT_ID,c.itemtype,CONSIGNMENT_GST,EMPNO,qtyor,qtypick,qtyac,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
		                            		  +"Ranked AS ("
		                            		  +"SELECT custcode,tono,UOM,tolnno,item,itemdesc,a.fromwarehouse,a.towarehouse,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
		                            		  +"qtyor ,qtypick ,qty,qtyac,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
		                            		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY ordPrice desc) rnk"
		                            		  +" from SORTTYPE)"
		                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY ITEM)desc";
		                       
		                        }
		                        else{
		                        	extraCon = " order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
		                        }
						// Start code modified by radhika for outorderwithprice on 18/12/12 
		                if(!sorttype.equalsIgnoreCase("")){      
		                sql.append(" WITH SORTTYPE AS( ");}
		                sql.append(" select distinct a.custcode,a.tono,b.tolnno,b.item,c.itemdesc,isnull(a.CRBY,'') as users,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,a.custname,");
						sql.append("a.CollectionDate,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS trandate,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,isnull(b.UNITMO,'') as UOM,isnull(a.Remark3,'') as remarks2,b.lnstat,isnull(b.qtyor,0) qtyor," );
						sql.append("isnull(b.qtypick,0) qtypick, isnull(b.qtyac,0) qtyac,isnull(b.unitprice,0) unitprice,isnull(b.QTYRC,0) as qty,ISNULL(((((qtyor*b.unitprice)*CONSIGNMENT_GST)/100)),0) as taxval,");
						sql.append("(SELECT FROMWAREHOUSE FROM ["+ plant +"_TOHDR] WHERE TONO=A.TONO) fromwarehouse,");
						sql.append("(SELECT TAXID FROM ["+ plant +"_TOHDR] WHERE TONO=A.TONO) TAXID,");
						sql.append("(SELECT TOWAREHOUSE FROM ["+ plant +"_TOHDR] WHERE TONO=A.TONO) towarehouse,");
						sql.append("b.crat, ISNULL(qtyor*b.unitprice,0) ordPrice, isnull(deliverydate,'') deliverydate,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.CONSIGNMENT_GST,0) as Tax,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname ");
						sql.append(",ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ");
						sql.append(" from " + "[" + plant + "_" + "tohdr" + "] a,");
						sql.append( "[" + plant + "_" + "todet" + "] b,["+ plant + "_" + "ITEMMST]c");
						sql.append(" where a.tono=b.tono and b.ITEM=c.item and a.custcode <> '' " + sCondition);
						arrList = movHisDAO.selectForReport(sql.toString(), ht,extraCon);
						// End code modified by radhika for outorderwithprice on 18/12/12 	
				} catch (Exception e) {
					this.mLogger.exception(this.printLog,
							"Exception :repportUtil :: getCustomerTOInvoiceSummary:", e);
				}
				return arrList;
			}
		 //END BY NAVAS
	 
		//CREATED BY NAVAS FEB20
		 
		 public ArrayList getCustomerTOInvoiceSummaryByProductGst(Hashtable ht, String afrmDate,
					String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype) {
				ArrayList arrList = new ArrayList();
				String sCondition = "";
				String extraCon="";
				StringBuffer sql = new StringBuffer();
				try {
					MovHisDAO movHisDAO = new MovHisDAO();
					movHisDAO.setmLogger(mLogger);
					//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		        if (itemDesc.length() > 0 ) {
			        if (itemDesc.indexOf("%") != -1) {
			        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
			        }
			        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		        }
		             if (custname.length()>0){
		                  	custname = StrUtils.InsertQuotes(custname);
		                   	sCondition =  sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
		                }
		             if (ht.size() > 0) {
		 				if (ht.get("CUSTTYPE") != null) {
		 					String custtype = ht.get("CUSTTYPE").toString();
		 					sCondition= sCondition+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
		 					ht.remove("CUSTTYPE");
		 				}
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
		                        if(sorttype.equalsIgnoreCase("CUSTOMER"))
		                        {
		                        	extraCon="GROUP BY A.CustCode,a.CustName,A.TONO,B.TOLNNO,b.ITEM,c.Remark1,c.ItemDesc,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
		                            		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.PRD_DEPT_ID,c.itemtype,a.status_id,b.PRODGST,EMPNO,qtyor,qtypick,qtyac,qtyis,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
		                            		  +"Ranked AS ("
		                            		  +"SELECT custcode,tono,UOM,tolnno,item,itemdesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
		                            		  +"qtyor ,qtypick ,qty,qtyac,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
		                            		  +"RANK() OVER (PARTITION BY custcode   ORDER BY ordPrice desc) rnk"
		                            		  +" from SORTTYPE)"
		                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY custcode)desc";
		                      
		                        }
		                        else if(sorttype.equalsIgnoreCase("PRODUCT"))
		                        {
		                        	extraCon="GROUP BY b.ITEM,c.Remark1,c.ItemDesc ,A.TONO,B.TOLNNO,A.CustCode,a.CustName,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
		                            		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.PRD_DEPT_ID,c.itemtype,a.status_id,b.PRODGST,EMPNO,qtyor,qtypick,qtyis,qtyac,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
		                            		  +"Ranked AS ("
		                            		  +"SELECT custcode,tono,tolnno,item,itemdesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
		                            		  +"qtyor ,qtypick ,UOM,qty,qtyac,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
		                            		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY ordPrice desc) rnk"
		                            		  +" from SORTTYPE)"
		                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY ITEM)desc";
		                       
		                        }
		                        else{
		                        	extraCon = " order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
		                        }
						// Start code modified by radhika for outorderwithprice on 18/12/12 
		                if(!sorttype.equalsIgnoreCase("")){      
		                sql.append(" WITH SORTTYPE AS( ");}
		                sql.append(" select distinct a.custcode,a.tono,b.tolnno,isnull(a.CRBY,'') as users,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,a.custname,");
						sql.append("a.CollectionDate,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS trandate,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,isnull(a.Remark3,'') as remarks2,isnull(b.UNITMO,'') as UOM,b.lnstat,isnull(b.qtyor,0) qtyor," );
						sql.append("isnull(b.qtypick,0) qtypick,isnull(b.qtyis,0) qty,isnull(b.qtyac,0) qtyac, isnull(b.unitprice,0) unitprice,ISNULL((((b.unitprice*b.PRODGST)/100)*b.qtyis),0) as taxval,");
						sql.append("b.crat, ISNULL(qtyor*b.unitprice,0) ordPrice, ISNULL(qtyis*b.unitprice,0) issPrice,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') deliverytime,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.prd_dept_id,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.PRODGST,0) as Tax,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname ");
						sql.append(",ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ");
						sql.append(" from " + "[" + plant + "_" + "tohdr" + "] a,");
						sql.append( "[" + plant + "_" + "todet" + "] b,["+ plant + "_" + "ITEMMST]c");
						sql.append(" where a.tono=b.tono and b.ITEM=c.item and a.custcode <> '' " + sCondition);
						arrList = movHisDAO.selectForReport(sql.toString(), ht,extraCon);
						// End code modified by radhika for outorderwithprice on 18/12/12 	
				} catch (Exception e) {
					this.mLogger.exception(this.printLog,
							"Exception :repportUtil :: getCustomerTOInvoiceSummary:", e);
				}
				return arrList;
			}
		 //END BY NAVAS
		 
	 public ArrayList getCustomerDOInvoiceSummaryByProductGst(Hashtable ht, String afrmDate,
				String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype, String ispos) {
			ArrayList arrList = new ArrayList();
			String sCondition = "";
			String extraCon="";
			StringBuffer sql = new StringBuffer();
			try {
				MovHisDAO movHisDAO = new MovHisDAO();
				movHisDAO.setmLogger(mLogger);
				
				String posext="";
			   	if(ispos.equalsIgnoreCase("3"))
			   		posext =  " AND ORDERTYPE = 'POS' " ;
			   	else if(ispos.equalsIgnoreCase("2"))
			   		posext =  " AND ORDERTYPE != 'POS' " ;
				
				//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
 	        if (itemDesc.length() > 0 ) {
 		        if (itemDesc.indexOf("%") != -1) {
 		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
 		        }
 		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
 	        }
	             if (custname.length()>0){
	                  	custname = StrUtils.InsertQuotes(custname);
	                   	sCondition =  sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
	                }
	             if (ht.size() > 0) {
	 				if (ht.get("CUSTTYPE") != null) {
	 					String custtype = ht.get("CUSTTYPE").toString();
	 					sCondition= sCondition+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
	 					ht.remove("CUSTTYPE");
	 				}
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
	                        if(sorttype.equalsIgnoreCase("CUSTOMER"))
	                        {
	                        	extraCon="GROUP BY A.CustCode,a.CustName,A.DONO,B.DOLNNO,b.ITEM,c.Remark1,c.ItemDesc,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
	                            		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.PRD_DEPT_ID,c.prd_cls_id,c.itemtype,a.status_id,b.PRODGST,EMPNO,qtyor,qtypick,qtyis,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
	                            		  +"Ranked AS ("
	                            		  +"SELECT custcode,dono,UOM,dolnno,item,itemdesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
	                            		  +"qtyor ,qtypick ,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
	                            		  +"RANK() OVER (PARTITION BY custcode   ORDER BY ordPrice desc) rnk"
	                            		  +" from SORTTYPE)"
	                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY custcode)desc";
	                      
	                        }
	                        else if(sorttype.equalsIgnoreCase("PRODUCT"))
	                        {
	                        	extraCon="GROUP BY b.ITEM,c.Remark1,c.ItemDesc ,A.DONO,B.DOLNNO,A.CustCode,a.CustName,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,a.Remark3,b.lnstat,b.UNITMO,a.CRBY,"
	                            		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.PRD_DEPT_ID,c.itemtype,a.status_id,b.PRODGST,EMPNO,qtyor,qtypick,qtyis,b.unitprice,A.SALES_LOCATION,TAXTREATMENT),"
	                            		  +"Ranked AS ("
	                            		  +"SELECT custcode,dono,dolnno,item,itemdesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,remarks2,lnstat,users,"
	                            		  +"qtyor ,qtypick ,UOM,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
	                            		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY ordPrice desc) rnk"
	                            		  +" from SORTTYPE)"
	                            		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY ITEM)desc";
	                       
	                        }
	                        else{
	                        	extraCon = " order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
	                        }
					// Start code modified by radhika for outorderwithprice on 18/12/12 
	                if(!sorttype.equalsIgnoreCase("")){      
	                sql.append(" WITH SORTTYPE AS( ");}
	                sql.append(" select distinct a.custcode,a.dono,b.dolnno,isnull(a.CRBY,'') as users,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,a.custname,");
					sql.append("a.CollectionDate,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS trandate,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,isnull(a.Remark3,'') as remarks2,isnull(b.UNITMO,'') as UOM,b.lnstat,isnull(b.qtyor,0) qtyor," );
					sql.append("isnull(b.qtypick,0) qtypick,isnull(b.qtyis,0) qty, isnull(b.unitprice,0) unitprice,ISNULL((((b.unitprice*b.PRODGST)/100)*b.qtyis),0) as taxval,");
					sql.append("b.crat, ISNULL(qtyor*b.unitprice,0) ordPrice, ISNULL(qtyis*b.unitprice,0) issPrice,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') deliverytime,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.PRODGST,0) as Tax,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname ");
					sql.append(",ISNULL((select isnull(Customer_Status_Id,'') CustomerStatusId FROM ["+plant+"_CUSTMST]  WHERE CUSTNO=a.custcode),'') CustomerStatusId,ISNULL((select isnull(Customer_Type_Id,'') CustomerTypeId FROM ["+plant+"_CUSTMST]  WHERE CUSTNO=a.custcode),'') Customer_Type_Id ");
					sql.append(",ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ");
					sql.append(" from " + "[" + plant + "_" + "dohdr" + "] a,");
					sql.append( "[" + plant + "_" + "dodet" + "] b,["+ plant + "_" + "ITEMMST]c");
					sql.append(" where a.dono=b.dono and b.ITEM=c.item and a.custcode <> '' " + posext+sCondition);
					arrList = movHisDAO.selectForReport(sql.toString(), ht,extraCon);
					// End code modified by radhika for outorderwithprice on 18/12/12 	
			} catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getCustomerDOInvoiceSummary:", e);
			}
			return arrList;
		}

	public ArrayList getReceiveSummaryList(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND  substring(a.crat,1,4)+'-'+substring(a.crat,5,2)+'-'+ substring(a.crat,7,2)  >= '"
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND substring(a.crat,1,4)+'-'+substring(a.crat,5,2)+'-'+ substring(a.crat,7,2) <= '"
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ "  substring(a.crat,1,4)+'-'+substring(a.crat,5,2)+'-'+ substring(a.crat,7,2)  <= '"
							+ atoDate + "'  ";
				}
			}

			if (dirType.equalsIgnoreCase("RECEIVE")) {
				StringBuffer aQuery = new StringBuffer();

				aQuery
						.append("select distinct a.pono,a.cname,a.item,isnull(a.itemdesc,'') itemdesc,isnull(a.batch,'') batch,isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(a.loc,'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby  from "
								+ "["
								+ plant
								+ "_"
								+ "recvdet] a,["
								+ plant
								+ "_POHDR] b"
								+ " where a.pono<>''  "
								+ sCondition);
			 	arrList = movHisDAO.selectForReport(aQuery.toString(), ht);

			}

			if (dirType.equalsIgnoreCase("ISSUE")) {
				String aQuery = "select a.dono,a.cname,a.item,isnull(a.itemdesc,'') itemdesc,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.reverseqty,0) reverseqty,isnull(b.qtyis,0) issueqty,isnull(a.loc,0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat   from "
						+ "["
						+ plant
						+ "_"
						+ "shiphis] a, "
						+ "["
						+ plant
						+ "_"
						+ "dodet] b where a.dono<>''  and a.dono=b.dono"
						+ sCondition;
				arrList = movHisDAO.selectForReport(aQuery, ht);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getReceiveSummaryList:", e);
		}
		return arrList;
	}
	
	 public ArrayList getPickingSummaryListByProductGst(Hashtable ht, String afrmDate,
				String atoDate, String dirType, String plant,String itemDesc) {
			ArrayList arrList = new ArrayList();
			String sCondition = "";
			
			try {
				MovHisDAO movHisDAO = new MovHisDAO();
				movHisDAO.setmLogger(mLogger);
				//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		        if (itemDesc.length() > 0 ) {
			        if (itemDesc.indexOf("%") != -1) {
			        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
			        }
		            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		        }
		     if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '" 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"
							+ atoDate + "'  ";
				}
			   } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) <= '" 
							+ atoDate + "'  ";
				}
			  }
		    String extraCond	=sCondition	+ "AND c.item = a.item AND c.ID = a.ID AND c.USERFLD4 = a.BATCH ";
			StringBuffer sql_DO = new StringBuffer();
			sql_DO.append("select a.dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,case a.status when 'C' then isnull(a.pickqty,0) "
							+ "else '0' end as issueqty , isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat," 
							+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull(b.UNITMO,'') as uom,"  
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,isnull(b.PRODGST,0) as Tax, "
							+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo  from "
							+ "["
							+ plant
							+ "_"
							+ "shiphis] a, "
							+ "["
							+ plant
							+ "_"
							+ "dodet] b, ["+plant+"_dohdr] d," 
							+"["+plant+"_ITEMMST] e  where a.dono<>''  and a.dono=b.dono AND a.dolno=b.dolnno AND d.DONO = b.DONO AND a.ITEM=e.ITEM"
							+ sCondition);
			if (ht.size() > 0) {
					if (ht.get("a.ITEM") != null) {
						sql_DO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
					}
					if (ht.get("a.CNAME") != null) {
						sql_DO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
					}
					if (ht.get("a.DONO") != null) {
						sql_DO.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
					}
					if (ht.get("a.BATCH") != null) {
						sql_DO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
					}
					if (ht.get("a.ITEMDESC") != null) {
						sql_DO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
								+ "'");
					}
					if (ht.get("d.ORDERTYPE") != null) {
						sql_DO.append(" AND d.ORDERTYPE = '" + ht.get("d.ORDERTYPE")
								+ "'");
					}
				
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_DO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_DO.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_DO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_DO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_DO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_DO.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				if (ht.get("INVOICENO") != null) {
					sql_DO.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
				}
			StringBuffer sql_Loan = new StringBuffer();
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			//---Modified by Bruhan on May 23 2014, Description: To include unit price and total in Goods Issue Summary Report
				sql_Loan.append("select a.ordno as dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') as expiredate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull(b.UNITMO,'') as uom," 
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
							+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo  from "
							+ "["
							+ plant
							+ "_"
							+ "loan_pick] a, "
							+ "["
							+ plant
							+ "_"
							+ "loandet] b,  "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["
							+plant
							+"_ITEMMST] e where a.ordno<>'' and a.ORDLNNO=b.ORDLNNO and a.ordno=b.ordno and  a.item=b.item AND a.ITEM=e.ITEM "
							+ extraCond);
				if (ht.size() > 0) {
					if (ht.get("a.ITEM") != null) {
						sql_Loan.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
					}
					if (ht.get("a.CNAME_LON") != null) {
						sql_Loan.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME_LON")
								+ "%'");
					}
					if (ht.get("a.DONO") != null) {
						sql_Loan
								.append(" AND A.ORDNO = '" + ht.get("a.DONO") + "'");
					}
					if (ht.get("a.BATCH") != null) {
						sql_Loan.append(" AND a.BATCH = '" + ht.get("a.BATCH")
								+ "'");
					}
					if (ht.get("a.ITEMDESC") != null) {
						sql_Loan.append(" AND a.ITEMDESC = '"
								+ ht.get("a.ITEMDESC") + "'");
					}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Loan.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Loan.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Loan.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Loan.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Loan.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Loan.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
			
		}

			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			/*StringBuffer sql_Transfer = new StringBuffer();
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			//---Modified by Bruhan on May 23 2014, Description: To include unit price and total in Goods Issue Summary Report
			sql_Transfer.append("select distinct a.tono as dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') expiredate ," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull(b.UNITMO,'') as uom," 
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
							+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo    from "
							+ "["
							+ plant
							+ "_"
							+ "to_pick] a, "
							+ "["
							+ plant
							+ "_"
							+ "todet] b ,  "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["
							+plant
							+"_ITEMMST] e where a.tono<>'' and a.TOLNO=b.TOLNNO and a.tono=b.tono and a.item=b.item AND a.ITEM=e.ITEM "
							+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Transfer.append(" AND A.ITEM = '" + ht.get("a.ITEM")
							+ "'");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_Transfer.append(" AND A.CNAME LIKE '%"
							+ ht.get("a.CNAME_TO") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Transfer.append(" AND A.TONO = '" + ht.get("a.DONO")
							+ "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Transfer.append(" AND a.BATCH = '" + ht.get("a.BATCH")
							+ "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Transfer.append(" AND a.ITEMDESC = '"+ ht.get("a.ITEMDESC") + "'");
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Transfer.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Transfer.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Transfer.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Transfer.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Transfer.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				
			// Comment
				} */
				// Comment
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			//---Modified by Bruhan on May 23 2014, Description: To include unit price and total in Goods Issue Summary Report
			StringBuffer sql_Miscissue = new StringBuffer();
			sql_Miscissue.append("select a.dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.pickqty,0) as issueqty,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,isnull(a.status,'') as lnstat, "  
				        	+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate,"
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull((Select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.dono and batch=a.batch and DOLNNO=a.DOLNO),'')as uom,"
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,ISNULL((SELECT TOP 1 ISNULL(OUTBOUND_GST,'') FROM "+plant+"_POSHDR WHERE PLANT=A.PLANT AND POSTRANID=A.DONO),'') as Tax, "	
							+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo  from "
					    	+ "["
							+ plant
							+ "_"
							+ "shiphis] a," 
							+"["
							+plant
							+"_ITEMMST] e where (a.TRAN_TYPE='GOODSISSUE' OR a.TRAN_TYPE='TAXINVOICE') AND a.ITEM=e.ITEM "//and a.item =  '" + ht.get("a.ITEM") + "' AND A.DONO = '" + ht.get("a.DONO") + "' AND    A.CNAME LIKE '" + ht.get("a.CNAME") + "' AND a.BATCH = '" + ht.get("a.BATCH") + "' AND a.ITEMDESC = '" + ht.get("a.ITEMDESC") + "'"
							+ sCondition);
			
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Miscissue.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_Miscissue.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Miscissue.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Miscissue.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Miscissue.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Miscissue.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
	
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Miscissue.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Miscissue.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Miscissue.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				if (ht.get("INVOICENO") != null) {
					sql_DO.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
			}
							
				Integer sqlFinal = 0;
				if (ht.get("d.ORDERTYPE") != null){
					sqlFinal = 4;
				}else{
				if (ht.get("a.CNAME") != null) {
					if (ht.get("a.CNAME_LON") != null) {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 1;
						} else {
							sqlFinal = 2;
						}
					} else {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 3;
						} else {
							sqlFinal = 4;
						}
					}
				} else {
					if (ht.get("a.CNAME_LON") != null) {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 5;
						} else {
							sqlFinal = 6;
						}
					} else {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 7;
						} else {
							sqlFinal = 1;
						}
					}
				}
			}
	StringBuffer sql_Final = new StringBuffer();
				switch (sqlFinal) {
				case 1: {
					sql_Final.append(" SELECT * FROM (  ");
					sql_Final.append(sql_DO.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Loan.toString());
					/*sql_Final.append(" UNION  ");
					sql_Final.append(sql_Transfer.toString());*/
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Miscissue.toString());
				}
					break;
				case 2: {
					sql_Final.append(sql_DO.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Loan.toString());
				}
					break;
				case 3: {
					/*sql_Final.append(sql_DO.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Transfer.toString());*/
				}
					break;
				case 4: {
					sql_Final.append(sql_DO.toString());
				}
					break;
				case 5: {
					sql_Final.append(sql_Loan.toString());
					sql_Final.append(" UNION  ");
//					sql_Final.append(sql_Transfer.toString());
				}
					break;
				case 6: {
					sql_Final.append(sql_Loan.toString());
				}
					break;
				case 7: {
//					sql_Final.append(sql_Transfer.toString());
				}
					break;
				}
				//sql_Final.append("order by a.item,a.issuedate");
				sql_Final.append(" ) a order by a.item, CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date) desc ");
				
				System.out.println(sql_Final.toString());
				if (dirType.equalsIgnoreCase("ISSUE")) {

					arrList = movHisDAO.selectForReport(sql_Final.toString(),
							new Hashtable<String, String>());
				}

			} catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getPickingSummaryList:", e);
			}
			return arrList;
		}

	/**************Modification History*********************************
     * Bruhan on Feb 27 2014, Description: To display location data's in uppercase
     * Bruhan on May 23 2014, Description: To include unit price and total in Goods Issue Summary Report
       Bruhan on Sep 26 2014, Description: To change transaction date as issued date
       Bruhan on Oct 01 2014, Description: To check isnull in issuedate
	   Bruhan on Oct 15 2014, Description: To change transaction date as issued date for Misc Issue,Loan Order and Transfer Order
	   Bruhan on Oct 31 2014, Description: To replace from date and to date as issue date and include issuedate in queries
     */
	 public ArrayList getPickingSummaryList(Hashtable ht, String afrmDate,
				String atoDate, String dirType, String plant,String itemDesc) {
			ArrayList arrList = new ArrayList();
			String sCondition = "";
			
			try {
				MovHisDAO movHisDAO = new MovHisDAO();
				movHisDAO.setmLogger(mLogger);
				//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		        if (itemDesc.length() > 0 ) {
			        if (itemDesc.indexOf("%") != -1) {
			        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
			        }
		            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		        }
		     if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '" 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"
							+ atoDate + "'  ";
				}
			   } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) <= '" 
							+ atoDate + "'  ";
				}
			  }
		    String extraCond	=sCondition	+ "AND c.item = a.item AND c.ID = a.ID AND c.USERFLD4 = a.BATCH ";
			StringBuffer sql_DO = new StringBuffer();
			sql_DO.append("select a.dono,(SELECT TAXID FROM ["+plant+"_DOHDR] WHERE DONO=a.DONO) AS TAXID,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,case a.status when 'C' then isnull(a.pickqty,0) "
							+ "else '0' end as issueqty , isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat," 
							+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull(b.UNITMO,'') as uom," 
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,isnull(d.OUTBOUND_GST,0) as Tax, "
							+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo  from "
 							+ "["
							+ plant
							+ "_"
							+ "shiphis] a, "
							+ "["
							+ plant
							+ "_"
							+ "dodet] b, ["+plant+"_dohdr] d," 
							+"["+plant+"_ITEMMST] e  where a.dono<>''  and a.dono=b.dono AND a.dolno=b.dolnno AND d.DONO = b.DONO AND a.ITEM=e.ITEM"
							+ sCondition);
			if (ht.size() > 0) {
					if (ht.get("a.ITEM") != null) {
						sql_DO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
					}
					if (ht.get("a.CNAME") != null) {
						sql_DO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
					}
					if (ht.get("a.DONO") != null) {
						sql_DO.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
					}
					if (ht.get("a.BATCH") != null) {
						sql_DO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
					}
					if (ht.get("a.ITEMDESC") != null) {
						sql_DO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
								+ "'");
					}
					if (ht.get("d.ORDERTYPE") != null) {
						sql_DO.append(" AND d.ORDERTYPE = '" + ht.get("d.ORDERTYPE")
								+ "'");
					}
				
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_DO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_DO.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_DO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_DO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_DO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_DO.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				
			
				if (ht.get("INVOICENO") != null) {
					sql_DO.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
				}
			StringBuffer sql_Loan = new StringBuffer();
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			//---Modified by Bruhan on May 23 2014, Description: To include unit price and total in Goods Issue Summary Report
				sql_Loan.append("select a.ordno as dono,ISNULL((SELECT TAXID FROM ["+plant+"_DOHDR] WHERE DONO=a.ORDNO),0) AS TAXID,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') as expiredate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ " isnull(b.UNITMO,'') as uom,"
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
							+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo  from "
							+ "["
							+ plant
							+ "_"
							+ "loan_pick] a, "
							+ "["
							+ plant
							+ "_"
							+ "loandet] b,  "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["
							+plant
							+"_ITEMMST] e where a.ordno<>'' and a.ORDLNNO=b.ORDLNNO and a.ordno=b.ordno and  a.item=b.item AND a.ITEM=e.ITEM "
							+ extraCond);
				if (ht.size() > 0) {
					if (ht.get("a.ITEM") != null) {
						sql_Loan.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
					}
					if (ht.get("a.CNAME_LON") != null) {
						sql_Loan.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME_LON")
								+ "%'");
					}
					if (ht.get("a.DONO") != null) {
						sql_Loan
								.append(" AND A.ORDNO = '" + ht.get("a.DONO") + "'");
					}
					if (ht.get("a.BATCH") != null) {
						sql_Loan.append(" AND a.BATCH = '" + ht.get("a.BATCH")
								+ "'");
					}
					if (ht.get("a.ITEMDESC") != null) {
						sql_Loan.append(" AND a.ITEMDESC = '"
								+ ht.get("a.ITEMDESC") + "'");
					}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Loan.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Loan.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Loan.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Loan.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Loan.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Loan.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
			
		}

			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			/*StringBuffer sql_Transfer = new StringBuffer();
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			//---Modified by Bruhan on May 23 2014, Description: To include unit price and total in Goods Issue Summary Report
			sql_Transfer.append("select distinct a.tono as dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') expiredate ," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ " isnull(b.UNITMO,'' )as uom,"
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
							+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo   from "
							+ "["
							+ plant
							+ "_"
							+ "to_pick] a, "
							+ "["
							+ plant
							+ "_"
							+ "todet] b ,  "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["
							+plant
							+"_ITEMMST] e where a.tono<>'' and a.TOLNO=b.TOLNNO and a.tono=b.tono and a.item=b.item AND a.ITEM=e.ITEM "
							+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Transfer.append(" AND A.ITEM = '" + ht.get("a.ITEM")
							+ "'");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_Transfer.append(" AND A.CNAME LIKE '%"
							+ ht.get("a.CNAME_TO") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Transfer.append(" AND A.TONO = '" + ht.get("a.DONO")
							+ "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Transfer.append(" AND a.BATCH = '" + ht.get("a.BATCH")
							+ "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Transfer.append(" AND a.ITEMDESC = '"+ ht.get("a.ITEMDESC") + "'");
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Transfer.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Transfer.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Transfer.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Transfer.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Transfer.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				
			// Comment
				} */

				// Comment
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			//---Modified by Bruhan on May 23 2014, Description: To include unit price and total in Goods Issue Summary Report
			StringBuffer sql_Miscissue = new StringBuffer();
			sql_Miscissue.append("select a.dono,ISNULL((SELECT TAXID FROM ["+plant+"_DOHDR] WHERE DONO=a.DONO),0) AS TAXID,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) as issueqty,isnull(a.pickqty,0) pickqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,isnull(a.status,'') as lnstat, "  
				        	+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate,"
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
							+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
							+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "CASE WHEN a.TRAN_TYPE='KITTING' THEN isnull((Select TOP 1 CHILDUOM from "+plant+"_SEMI_PROCESSDET where CHILD_PRODUCT=a.item and GINO=a.INVOICENO and CHILD_PRODUCT_BATCH=a.batch),'') WHEN a.TRAN_TYPE='DE-KITTING' THEN isnull((Select TOP 1 PARENTUOM from "+plant+"_PROCESSING_RECEIVEHDR where PARENT_PRODUCT=a.item and GINO=a.INVOICENO and PARENT_PRODUCT_BATCH=a.batch),'') ELSE isnull((Select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.INVOICENO and batch=a.batch and DOLNNO=a.DOLNO),'') END as uom,"
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,ISNULL((SELECT TOP 1 ISNULL(OUTBOUND_GST,'') FROM "+plant+"_POSHDR WHERE PLANT=A.PLANT AND POSTRANID=A.INVOICENO),'') as Tax, "	
							+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo  from "
					    	+ "["
							+ plant
							+ "_"
							+ "shiphis] a," 
							+"["
							+plant
							+"_ITEMMST] e where (A.TRAN_TYPE='GOODSISSUEWITHBATCH' OR a.TRAN_TYPE='GOODSISSUE' OR a.TRAN_TYPE='TAXINVOICE' OR a.TRAN_TYPE='KITTING' OR a.TRAN_TYPE='DE-KITTING' OR a.TRAN_TYPE='STOCK_TAKE') AND a.ITEM=e.ITEM "//and a.item =  '" + ht.get("a.ITEM") + "' AND A.DONO = '" + ht.get("a.DONO") + "' AND    A.CNAME LIKE '" + ht.get("a.CNAME") + "' AND a.BATCH = '" + ht.get("a.BATCH") + "' AND a.ITEMDESC = '" + ht.get("a.ITEMDESC") + "'"
							+ sCondition);
			
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Miscissue.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_Miscissue.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Miscissue.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Miscissue.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Miscissue.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Miscissue.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
	
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Miscissue.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Miscissue.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Miscissue.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				
				if (ht.get("INVOICENO") != null) {
					sql_Miscissue.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
			}
			
			StringBuffer sql_Invoice = new StringBuffer();
			sql_Invoice.append("SELECT B.INVOICE AS dono,B.TAXID AS TAXID,  (SELECT CNAME FROM ["+plant+"_CUSTMST] WHERE CUSTNO=B.CUSTNO) AS cname," + 
					"C.ITEM,isnull((select itemdesc from "+plant+"_ITEMMST where item=C.item ),'') as itemdesc, isnull(a.CRBY,'') as users," + 
					"isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty," + 
					"case a.status when 'C' then isnull(a.pickqty,0) else '0' end as issueqty , " + 
					"isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark," + 
					"a.crat,a.crby,'' AS lnstat, " + 
					"isnull((SELECT TOP 1 expiredate FROM ["+plant+"_INVMST] " + 
					"WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," + 
					"SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," + 
					"ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate, " + 
					"CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate," + 
					"ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') " + 
					"AS toexceltransactiondate," + 
					"isnull(C.UOM,'') as uom,isnull((select prd_brand_id from ["+plant+"_ITEMMST] where item=a.item ),'')  as prd_brand_id," + 
					"isnull((select prd_cls_id from ["+plant+"_ITEMMST] where item=a.item ),'') as prd_cls_id," + 
					"isnull((select PRD_DEPT_ID from ["+plant+"_ITEMMST] where item=a.item ),'') as PRD_DEPT_ID," + 
					"isnull((select itemtype from ["+plant+"_ITEMMST] where item=a.item ),'') as itemtype," + 
					"isnull(B.OUTBOUD_GST,0) as Tax, isnull(a.unitprice,0) unitprice," + 
					"case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate," + 
					"ISNULL((INVOICENO),'') as InvoiceNo  " + 
					"FROM ["+plant+"_SHIPHIS] A JOIN ["+plant+"_FININVOICEHDR] B ON A.INVOICENO = B.GINO " + 
					"JOIN ["+plant+"_FININVOICEDET] C ON B.ID = C.INVOICEHDRID AND A.ITEM=C.ITEM AND A.DOLNO=C.LNNO WHERE B.DONO='' "
							+ sCondition);
			
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Invoice.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_Invoice.append(" AND B.CUSTNO LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Invoice.append(" AND B.INVOICE = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Invoice.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Invoice.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Invoice.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Invoice.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Invoice.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Invoice.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
	
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Invoice.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Invoice.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Invoice.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Invoice.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Invoice.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				
				if (ht.get("INVOICENO") != null) {
					sql_Invoice.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
			}
							
				Integer sqlFinal = 1;
		/*		if (ht.get("d.ORDERTYPE") != null){
					sqlFinal = 4;
				}else{
				if (ht.get("a.CNAME") != null) {
					if (ht.get("a.CNAME_LON") != null) {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 1;
						} else {
							sqlFinal = 2;
						}
					} else {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 3;
						} else {
							sqlFinal = 4;
						}
					}
				} else {
					if (ht.get("a.CNAME_LON") != null) {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 5;
						} else {
							sqlFinal = 6;
						}
					} else {
						if (ht.get("a.CNAME_TO") != null) {
							sqlFinal = 7;
						} else {
							sqlFinal = 1;
						}
					}
				}
			}*/
	StringBuffer sql_Final = new StringBuffer();
				switch (sqlFinal) {
				case 1: {
					sql_Final.append(" SELECT * FROM (  ");
					sql_Final.append(sql_DO.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Loan.toString());
					/*sql_Final.append(" UNION  ");
					sql_Final.append(sql_Transfer.toString());*/
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Miscissue.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Invoice.toString());
				}
					break;
				case 2: {
					sql_Final.append(sql_DO.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Loan.toString());
				}
					break;
				case 3: {
					/*sql_Final.append(sql_DO.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Transfer.toString());*/
				}
					break;
				case 4: {
					sql_Final.append(sql_DO.toString());
				}
					break;
				case 5: {
					/*sql_Final.append(sql_Loan.toString());
					sql_Final.append(" UNION  ");
					sql_Final.append(sql_Transfer.toString());*/
				}
					break;
				case 6: {
					sql_Final.append(sql_Loan.toString());
				}
					break;
				case 7: {
					/*sql_Final.append(sql_Transfer.toString());*/
				}
					break;
				}
				//sql_Final.append("order by a.item,a.issuedate");
				//sql_Final.append(" ) a order by a.item, CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date) desc ");
				sql_Final.append(" ) a order by CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date) desc,A.InvoiceNo DESC,A.DONO DESC,A.ITEM ");
				
				System.out.println(sql_Final.toString());
				if (dirType.equalsIgnoreCase("ISSUE")) {

					arrList = movHisDAO.selectForReport(sql_Final.toString(),
							new Hashtable<String, String>());
				}

			} catch (Exception e) {
				this.mLogger.exception(this.printLog,
						"Exception :repportUtil :: getPickingSummaryList:", e);
			}
			return arrList;
		}


	/**************Modification History*********************************
	        Bruhan on Oct 31 2014, Description: To replace from date and to date as issue date and include issuedate in queries
     */
	public ArrayList getPickingSummaryItemList(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant,String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
			
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '"
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '" 
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) <= '"
							+ atoDate + "'  ";
				}
			}
			
			String extraCond=	sCondition	+ "AND c.item = a.item AND c.loc = a.LOC AND c.USERFLD4 = a.BATCH ";
			StringBuffer sql_DO = new StringBuffer();
			sql_DO
					.append("select distinct a.item from "
							+ "["
							+ plant
							+ "_"
							+ "shiphis] a, "
							+ "["
							+ plant
							+ "_"
							+ "dodet] b, ["+plant+"_dohdr] d,"
							+"["+plant+"_ITEMMST] e where a.dono<>''  and a.dono=b.dono AND a.dolno=b.dolnno AND d.DONO = b.DONO and a.ITEM=e.ITEM"
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_DO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_DO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_DO.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_DO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_DO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("d.ORDERTYPE") != null) {
					sql_DO.append(" AND d.ORDERTYPE = '" + ht.get("d.ORDERTYPE")
							+ "'");
				}
			if (ht.get("C.PRD_CLS_ID") != null) {
					sql_DO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
			
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_DO.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
						+ "'");
			}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_DO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_DO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}	
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_DO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
				if (ht.get("a.LOC") != null) {
					sql_DO.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_DO.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_DO.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_DO.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				
				if (ht.get("INVOICENO") != null) {
					sql_DO.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
				
				
			}

			StringBuffer sql_Loan = new StringBuffer();
			sql_Loan
					.append("select distinct a.item  "
							+ " from "
							+ "["
							+ plant
							+ "_"
							+ "loan_pick] a, "
							+ "["
							+ plant
							+ "_"
							+ "loandet] b,  "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["+plant+"_ITEMMST] e where a.ordno<>''  and a.ordno=b.ordno and a.ITEM=e.ITEM "
							+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Loan.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME_LON") != null) {
					sql_Loan.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME_LON")
							+ "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Loan
							.append(" AND A.ORDNO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Loan.append(" AND a.BATCH = '" + ht.get("a.BATCH")
							+ "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Loan.append(" AND a.ITEMDESC = '"
							+ ht.get("a.ITEMDESC") + "'");
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Loan.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Loan.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Loan.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Loan.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Loan.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				if (ht.get("a.LOC") != null) {
					sql_Loan.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Loan.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}		
				
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Loan.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Loan.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				
			}

			StringBuffer sql_Transfer = new StringBuffer();
			sql_Transfer
					.append("select distinct a.item  "
							+ " from "
							+ "["
							+ plant
							+ "_"
							+ "to_pick] a, "
							+ "["
							+ plant
							+ "_"
							+ "todet] b ,  "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["+plant+"_ITEMMST] e where a.tono<>'' and a.TOLNO=b.TOLNNO and a.tono=b.tono and a.ITEM=e.ITEM "
							+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Transfer.append(" AND A.ITEM = '" + ht.get("a.ITEM")
							+ "'");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_Transfer.append(" AND A.CNAME LIKE '%"
							+ ht.get("a.CNAME_TO") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Transfer.append(" AND A.TONO = '" + ht.get("a.DONO")
							+ "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Transfer.append(" AND a.BATCH = '" + ht.get("a.BATCH")
							+ "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Transfer.append(" AND a.ITEMDESC = '"
							+ ht.get("a.ITEMDESC") + "'");
				}
			if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Transfer.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Transfer.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Transfer.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Transfer.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Transfer.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
				if (ht.get("a.LOC") != null) {
					sql_Transfer.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Transfer.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Transfer.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Transfer.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				
			}
			StringBuffer sql_Miscissue = new StringBuffer();
			sql_Miscissue
					.append("select distinct a.item  from "
							
							+ "["
							+ plant
							+ "_"
							+ "shiphis] a,"
 							+"["+plant+"_ITEMMST]e where a.TRAN_TYPE='GOODSISSUE' or a.TRAN_TYPE='TAXINVOICE' OR a.TRAN_TYPE='KITTING' OR a.TRAN_TYPE='STOCK_TAKE' and a.ITEM=e.ITEM "
							+ sCondition);
			
			
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Miscissue.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_Miscissue.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Miscissue.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Miscissue.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Miscissue.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
			if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Miscissue.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Miscissue.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Miscissue.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Miscissue.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Miscissue.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Miscissue.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				if (ht.get("INVOICENO") != null) {
					sql_Miscissue.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
			}
			
			
			Integer sqlFinal = 0;
			if (ht.get("d.ORDERTYPE") != null){
				sqlFinal = 4;
			}else{
			if (ht.get("a.CNAME") != null) {
				if (ht.get("a.CNAME_LON") != null) {
					if (ht.get("a.CNAME_TO") != null) {
						sqlFinal = 1;
					} else {
						sqlFinal = 2;
					}
				} else {
					if (ht.get("a.CNAME_TO") != null) {
						sqlFinal = 3;
					} else {
						sqlFinal = 4;
					}
				}
			} else {
				if (ht.get("a.CNAME_LON") != null) {
					if (ht.get("a.CNAME_TO") != null) {
						sqlFinal = 5;
					} else {
						sqlFinal = 6;
					}
				} else {
					if (ht.get("a.CNAME_TO") != null) {
						sqlFinal = 7;
					} else {
						sqlFinal = 1;
					}
				}
			}
			}
			StringBuffer sql_Final = new StringBuffer();
			switch (sqlFinal) {
			case 1: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Loan.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Miscissue.toString());
				
				
			}
				break;
			case 2: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Loan.toString());
				
			}
				break;
			case 3: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
				
			}
				break;
			case 4: {
				sql_Final.append(sql_DO.toString());
				
			}
				break;
			case 5: {
				sql_Final.append(sql_Loan.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
				
			}
				break;
			case 6: {
				sql_Final.append(sql_Loan.toString());
				
			}
				break;
			case 7: {
				sql_Final.append(sql_Transfer.toString());
				
			}
				break;
			}
			//sql_Final.append("order by a.item ");
           
			if (dirType.equalsIgnoreCase("ISSUE")) {

				arrList = movHisDAO.selectForReport(sql_Final.toString(),
						new Hashtable<String, String>());
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}

	
	

	 /*************Modification History*********************************
	       Bruhan on Feb 27 2014, Description: To display location data's in uppercase
	       Bruhan on May 27 2014, Description: To include unitcost and total
	       Bruhan on Sep 26 2014, Description: To include Receive Date
	       Bruhan on Oct 30 2014, Description: To replace from date and to date as receive date and include receive date in queries
	    */
	public ArrayList getGoodsRecieveSummaryList(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {
			
                         
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
                         
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) >= '"// 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2)<= '" 
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		   }
			if (ht.size() > 0) {
				if (ht.get("FILTER") != null) {
					String filter = ht.get("FILTER").toString();
					if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND a.PONO Like'P%' ";}
					else if(filter.equalsIgnoreCase("Loan Order")){sCondition= sCondition+" AND (a.PONO Like'RS%' OR a.PONO Like'R%') ";}
					else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND a.PONO Like'T%' ";}
					else{sCondition= sCondition+" AND (A.PONO Like'GR%' OR A.PONO Like'GN%') ";}
				}
			}
			sCondition = sCondition + "  ";
			extraOrderbyCon =" order by a.item,  ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'')";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("select distinct a.pono,ISNULL(a.lnno,'')lnno,ISNULL(CNAME,'') as cname,a.item,isnull((select itemdesc from "+plant+"_ITEMMST where item=a.item),'') as itemdesc,upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,"
							+" case when a.PONO Like 'P%' then isnull((select UNITMO from "+plant+"_PODET where item=a.item and PONO=a.pono and POLNNO=a.LNNO),'') when a.pono Like 'GR%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'GN%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'T%' then isnull((select UNITMO from "+plant+"_TODET where item=a.item and TONO=a.pono and TOLNNO=a.LNNO),'') when a.pono Like 'S%' then isnull((select UNITMO from "+plant+"_LOANDET where item=a.item and ORDNO=a.pono and ORDLNNO=a.LNNO),'') else'' end as uom,"
							+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
							+  "ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate,"
							+ "CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id,isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id,isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype,  "
							+ "isnull(isnull(b.unitcost_aod,a.unitcost),0) unitcost,isnull(isnull(b.unitcost_aod,a.unitcost)*a.recqty,0) total,recvdate,isnull((select inbound_gst from "+plant+"_pohdr where pono=a.pono ),'') as Tax, isnull(((isnull(b.unitcost_aod,a.unitcost) * (select inbound_gst from "+plant+"_pohdr where pono=a.pono ))*a.recqty)/100,'') as taxval   from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a left join ["+plant+"_podet] b on a.pono = b.pono and a.item = b.item and a.LNNO = b.POLNNO "
						    + " where  a.pono<>'' "
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")+ "%' and tran_type='IB'");
				}
				if (ht.get("a.CNAME_LOAN") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%" + ht.get("a.CNAME_LOAN") + "%' and tran_type='LO' ");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%"+ ht.get("a.CNAME_TO") + "%'  and tran_type='TO'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
			   if (ht.get("ORDERTYPE") != null) {
				  sql_PO.append("  AND a.PONO IN (SELECT PONO FROM ["+plant+"_POHDR] WHERE ORDERTYPE  = '"+ ht.get("ORDERTYPE")+ "' )"  ); 
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC") 
							+ "'" );
					
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID2  = '"+ ht.get("LOC_TYPE_ID2")
							+ "' AND LOC=a.LOC)"  );
				}
				
				
				//end
				if (ht.get("PRD_CLS_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_CLS_ID  = '"+ ht.get("PRD_CLS_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_BRAND_ID  = '"+ ht.get("PRD_BRAND_ID")+ "' )"  );
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE ITEMTYPE  = '"+ ht.get("ITEMTYPE")+ "' )"  );
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}
	
public ArrayList getGoodsRecieveSummaryListlocalexpenses(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {
			
                         
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
                         
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) >= '"// 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2)<= '" 
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		   }
			if (ht.size() > 0) {
				if (ht.get("FILTER") != null) {
					String filter = ht.get("FILTER").toString();
					if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND A.PONO Like'P%' ";}
					else if(filter.equalsIgnoreCase("Loan Order")){sCondition= sCondition+" AND (a.PONO Like'RS%' OR a.PONO Like'R%') ";}
					else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND A.PONO Like'T%' ";}
					else{sCondition= sCondition+" AND (A.PONO Like'GR%' OR A.PONO Like'GN%') ";}
				}
			}
			sCondition = sCondition + "  ";
			extraOrderbyCon =" order by a.item,  ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'')";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("select distinct a.pono,ISNULL(lnno,'')lnno,ISNULL(CNAME,'') as cname,a.item,isnull((select itemdesc from "+plant+"_ITEMMST where item=a.item ),'') as itemdesc,upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,"
							+" case when a.PONO Like 'P%' then isnull((select UNITMO from "+plant+"_PODET where item=a.item and PONO=a.pono and POLNNO=a.LNNO),'') when a.pono Like 'GR%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'GN%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'T%' then isnull((select UNITMO from "+plant+"_TODET where item=a.item and TONO=a.pono and TOLNNO=a.LNNO),'') when a.pono Like 'S%' then isnull((select UNITMO from "+plant+"_LOANDET where item=a.item and ORDNO=a.pono and ORDLNNO=a.LNNO),'') else'' end as uom,"
							+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
							+  "ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate,"
							+ "CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id,isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id,isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype,  "
							+ "isnull((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) + (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/s.CURRENCYUSEQT,0) unitcost,"
							+ "isnull(((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) + (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/s.CURRENCYUSEQT)*a.recqty,0) total,recvdate,"
							+ "isnull((inbound_gst),'') as Tax, isnull(((((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) + (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/s.CURRENCYUSEQT)* (p.inbound_gst)) *a.recqty)/100,0) as taxval   from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a, ["+plant+"_pohdr] p, ["+plant+"_podet] s "
						    + " where A.PONO=P.PONO AND s.pono=A.pono and s.ITEM=A.ITEM and a.pono<>'' "
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")+ "%' and tran_type='IB'");
				}
				if (ht.get("a.CNAME_LOAN") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%" + ht.get("a.CNAME_LOAN") + "%' and tran_type='LO' ");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%"+ ht.get("a.CNAME_TO") + "%'  and tran_type='TO'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
			   if (ht.get("ORDERTYPE") != null) {
				  sql_PO.append("  AND a.PONO IN (SELECT PONO FROM ["+plant+"_POHDR] WHERE ORDERTYPE  = '"+ ht.get("ORDERTYPE")+ "' )"  ); 
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC") 
							+ "'" );
					
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID2  = '"+ ht.get("LOC_TYPE_ID2")
							+ "' AND LOC=a.LOC)"  );
				}
				
				
				//end
				if (ht.get("PRD_CLS_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_CLS_ID  = '"+ ht.get("PRD_CLS_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_BRAND_ID  = '"+ ht.get("PRD_BRAND_ID")+ "' )"  );
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE ITEMTYPE  = '"+ ht.get("ITEMTYPE")+ "' )"  );
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}
	
	public ArrayList getGoodsRecieveSummaryListByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {
			
                         
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
                         
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) >= '"// 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2)<= '" 
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		   }
			if (ht.size() > 0) {
				if (ht.get("FILTER") != null) {
					String filter = ht.get("FILTER").toString();
					//if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND a.PONO Like'P%' ";}
					if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND A.PONO IN (SELECT PONO FROM ["+plant+"_POHDR]) ";}
					else if(filter.equalsIgnoreCase("Loan Order")){sCondition= sCondition+" AND (a.PONO Like'RS%' OR a.PONO Like'R%') ";}
					//else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND a.PONO Like'T%' ";}
					else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND A.PONO (SELECT TONO FROM ["+plant+"_TOHDR] ";}
					else if(filter.equalsIgnoreCase("Bill")){sCondition= sCondition+" AND A.GRNO (SELECT GRNO FROM ["+plant+"_FINBILLHDR] ";}
					else{sCondition= sCondition+" AND (A.PONO Like'GR%' OR A.PONO Like'GN%') ";}
				}
			}
			sCondition = sCondition + "  ";
			extraOrderbyCon =" order by a.item,  CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date) desc";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("select distinct pono,ISNULL(lnno,'')lnno,ISNULL(CNAME,'') as cname,item,isnull((select itemdesc from "+plant+"_ITEMMST where item=a.item ),'') as itemdesc,upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,"
							+" case when PONO Like 'P%' then isnull((select UNITMO from "+plant+"_PODET where item=a.item and PONO=a.pono and POLNNO=a.LNNO),'') when pono Like 'GR%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when pono Like 'GN%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when pono Like 'T%' then isnull((select UNITMO from "+plant+"_TODET where item=a.item and TONO=a.pono and TOLNNO=a.LNNO),'') when pono Like 'S%' then isnull((select UNITMO from "+plant+"_LOANDET where item=a.item and ORDNO=a.pono and ORDLNNO=a.LNNO),'') else'' end as uom,"
							+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
							+  "ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate,"
							+ "CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id,isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id,isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'') as PRD_DEPT_ID,isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype,  "
							+ "isnull(a.unitcost,0) unitcost,isnull(a.unitcost*a.recqty,0) total,recvdate,isnull((select isnull(prodgst,0) from "+plant+"_podet where pono=a.pono and polnno=a.lnno),'') as Tax,isnull(((a.unitcost * (select isnull(prodgst,0) from "+plant+"_podet where pono=a.pono and polnno=a.lnno ))*a.recqty)/100,'') as taxval   from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a "
						    + " where a.pono<>'' "
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")+ "%'");
				}
				if (ht.get("a.CNAME_LOAN") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%" + ht.get("a.CNAME_LOAN") + "%' and tran_type='LO' ");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%"+ ht.get("a.CNAME_TO") + "%'  and tran_type='TO'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
			   if (ht.get("ORDERTYPE") != null) {
				  sql_PO.append("  AND a.PONO IN (SELECT PONO FROM ["+plant+"_POHDR] WHERE ORDERTYPE  = '"+ ht.get("ORDERTYPE")+ "' )"  ); 
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC") 
							+ "'" );
					
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID2  = '"+ ht.get("LOC_TYPE_ID2")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID3  = '"+ ht.get("LOC_TYPE_ID3")
							+ "' AND LOC=a.LOC)"  );
				}
				
				
				
				//end
				if (ht.get("PRD_CLS_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_CLS_ID  = '"+ ht.get("PRD_CLS_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_DEPT_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_DEPT_ID  = '"+ ht.get("PRD_DEPT_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_BRAND_ID  = '"+ ht.get("PRD_BRAND_ID")+ "' )"  );
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE ITEMTYPE  = '"+ ht.get("ITEMTYPE")+ "' )"  );
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}

	public ArrayList getGoodsRecieveSummaryListByProductGstlocalexpenses(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {
			
                         
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
                         
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) >= '"// 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2)<= '" 
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		   }
			if (ht.size() > 0) {
				if (ht.get("FILTER") != null) {
					String filter = ht.get("FILTER").toString();
					if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND a.PONO Like'P%' ";}
					else if(filter.equalsIgnoreCase("Loan Order")){sCondition= sCondition+" AND (a.PONO Like'RS%' OR a.PONO Like'R%') ";}
					else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND a.PONO Like'T%' ";}
					else{sCondition= sCondition+" AND (A.PONO Like'GR%' OR A.PONO Like'GN%') ";}
				}
			}
			sCondition = sCondition + "  ";
			extraOrderbyCon =" order by a.item,  CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date) desc";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("select distinct a.pono,ISNULL(lnno,'')lnno,ISNULL(CNAME,'') as cname,a.item,isnull((select itemdesc from "+plant+"_ITEMMST where item=a.item ),'') as itemdesc,upper(isnull(a.batch,'')) batch, isnull(a.CRBY,'') as users,isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,"
							+" case when a.PONO Like 'P%' then isnull((select UNITMO from "+plant+"_PODET where item=a.item and PONO=a.pono and POLNNO=a.LNNO),'') when a.pono Like 'GR%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'GN%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'T%' then isnull((select UNITMO from "+plant+"_TODET where item=a.item and TONO=a.pono and TOLNNO=a.LNNO),'') when a.pono Like 'S%' then isnull((select UNITMO from "+plant+"_LOANDET where item=a.item and ORDNO=a.pono and ORDLNNO=a.LNNO),'') else'' end as uom,"
							+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
							+  "ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate,"
							+ "CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id,isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'')  as PRD_DEPT_ID,isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id,isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype,  "
							+ "isnull((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) + (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0))/100))/s.CURRENCYUSEQT,0) unitcost,"
							+ "isnull(((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) + (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0))/100))/s.CURRENCYUSEQT)*a.recqty,0) total,recvdate,"
							+ "isnull((select isnull(prodgst,0) from "+plant+"_podet where pono=a.pono and polnno=a.lnno),'') as Tax,isnull(((((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) + (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * (((isnull(P.SHIPPINGCOST,0)+isnull(P.LOCALEXPENSES,0)+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0))/100))/s.CURRENCYUSEQT)* (p.inbound_gst)) *a.recqty)/100,0) as taxval   from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a, ["+plant+"_pohdr] p, ["+plant+"_podet] s "
						    + " where A.PONO=P.PONO AND s.pono=A.pono and s.ITEM=A.ITEM and a.pono<>'' "
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")+ "%' and tran_type='IB'");
				}
				if (ht.get("a.CNAME_LOAN") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%" + ht.get("a.CNAME_LOAN") + "%' and tran_type='LO' ");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%"+ ht.get("a.CNAME_TO") + "%'  and tran_type='TO'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
			   if (ht.get("ORDERTYPE") != null) {
				  sql_PO.append("  AND a.PONO IN (SELECT PONO FROM ["+plant+"_POHDR] WHERE ORDERTYPE  = '"+ ht.get("ORDERTYPE")+ "' )"  ); 
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC") 
							+ "'" );
					
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID2  = '"+ ht.get("LOC_TYPE_ID2")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID3  = '"+ ht.get("LOC_TYPE_ID3")
							+ "' AND LOC=a.LOC)"  );
				}
				
				//end
				if (ht.get("PRD_CLS_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_CLS_ID  = '"+ ht.get("PRD_CLS_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_DEPT_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_DEPT_ID  = '"+ ht.get("PRD_DEPT_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_BRAND_ID  = '"+ ht.get("PRD_BRAND_ID")+ "' )"  );
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE ITEMTYPE  = '"+ ht.get("ITEMTYPE")+ "' )"  );
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}

	
	public ArrayList getGoodsRecieveSummaryListWithOrderType(Hashtable ht,
			String afrmDate, String atoDate, String dirType, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(a.crat,1,8) >= '"
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(a.crat,1,8)<= '" 
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(a.crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			}

			sCondition = sCondition
					+ " AND c.item = a.item AND c.loc = a.loc AND c.userfld4 = a.batch ";

			StringBuffer sql_PO = new StringBuffer();
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			//---Modified by Bruhan on May 27 2014, Description: To include unitcost and total
			sql_PO
					.append("select distinct a.pono,b.custname as cname,a.item,isnull(a.itemdesc,'') itemdesc,(select stkuom from "+plant+"_ITEMMST where item=a.item ) as uom,upper(isnull(a.batch,'')) batch,isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,isnull(c.expiredate, '') expiredate," 
							+  "ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate,"
							+ "CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') AS toexceltransactiondate,"
							+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype, "
							+"isnull(unitcost,0) unitcost,isnull(unitcost*recqty,0) total  from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a, "
							+ "["
							+ plant
							+ "_"
							+ "pohdr] b, "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["+plant+"_ITEMMST] e where a.pono<>''  and a.pono=b.pono and a.ITEM=e.ITEM"
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND B.CUSTNAME LIKE '%" + ht.get("a.CNAME")
							+ "%'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("b.ORDERTYPE") != null) {
					sql_PO.append(" AND b.ORDERTYPE = '"
							+ ht.get("b.ORDERTYPE") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC")
							+ "' ");
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_PO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_PO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_PO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}

				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
			}
			sql_PO.append(" order by a.crat");
			
			

			arrList = movHisDAO.selectForReport(sql_PO.toString(),
					new Hashtable<String, String>());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}
	
	public ArrayList getGoodsRecieveSummaryItemListWithOrderType(Hashtable ht,
			String afrmDate, String atoDate, String dirType, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(a.crat,1,8) >= '"// +'-'+substring(a.crat,6,2)+'-'+
						// substring(a.crat,9,2)
						// >=
						// '"
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(a.crat,1,8)<= '" 
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(a.crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			}

			sCondition = sCondition
					+ " and c.item = a.item AND c.loc = a.loc and c.userfld4 = a.batch ";

			StringBuffer sql_PO = new StringBuffer();
			sql_PO
					.append("select distinct A.item from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a, "
							+ "["
							+ plant
							+ "_"
							+ "pohdr] b, "
							+ "["
							+ plant
							+ "_"
							+ "invmst] c,"
							+"["+plant+"_ITEMMST] e where a.pono<>'' and a.pono=b.pono  and a.ITEM=e.ITEM"
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")
							+ "%'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
				if (ht.get("b.ORDERTYPE") != null) {
					sql_PO.append(" AND b.ORDERTYPE = '"
							+ ht.get("b.ORDERTYPE") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC")
							+ "'");
				}
				
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				
				
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_PO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_PO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_PO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}

				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
			}
					
			

			arrList = movHisDAO.selectForReport(sql_PO.toString(),
					new Hashtable<String, String>());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsRecieveSummaryItemList:", e);
		}
		return arrList;
	}
	
	
	public ArrayList getGoodsRecieveSummaryItemListWithOutOrderType(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc)
	 {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(a.crat,1,8) >= '"
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(a.crat,1,8)<= '" 
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(a.crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			}

			sCondition = sCondition
					+ " and b.item = a.item AND b.loc = a.loc and b.userfld4 = a.batch ";

			StringBuffer sql_PO = new StringBuffer();
			sql_PO
					.append("select distinct A.item from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a, "
							+ "["
							+ plant
							+ "_"
							+ "invmst] b,"
							+"["+plant+"_ITEMMST] e where a.pono<>''  and a.ITEM=e.ITEM"
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")
							+ "%'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
				
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC")
							+ "'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				
				
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_PO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_PO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_PO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
				}
				
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
			}
		
			arrList = movHisDAO.selectForReport(sql_PO.toString(),
					new Hashtable<String, String>());
			
		

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsRecieveSummaryItemList:", e);
		}
		return arrList;
	}
    public ArrayList getsalesChargableRate(String plant) {
    ArrayList arrList = new ArrayList();
    Hashtable ht= new Hashtable();
    try {
            MovHisDAO movHisDAO = new MovHisDAO();
            movHisDAO.setmLogger(mLogger);
            

                    String aQuery = "select SALES_CHARGE_BY,CASE WHEN SALES_CHARGE_BY ='FLATRATE' THEN CAST(SALES_FR_DOLLARS AS VARCHAR) +'.'+CAST(SALES_FR_CENTS AS VARCHAR)" + 
                                    "ELSE  CAST(SALES_PERCENT AS VARCHAR) END AS SALESRATE  from plntmst WHERE PLANT ='"+plant+"'";
                                  
                    
                    arrList = movHisDAO.selectForReport(aQuery, ht);
          
    } catch (Exception e) {
            this.mLogger.exception(this.printLog,
                            "Exception :repportUtil :: getMovHisList:", e);
    }
    return arrList;
    }
    public ArrayList getTotalNoofEnQuires(Hashtable ht, String afrmDate,
               String atoDate, String dirType, String plant, String itemDesc) {
       ArrayList arrList = new ArrayList();
       String sCondition = "";
//       String noofOrders="";
       try {
               MovHisDAO movHisDAO = new MovHisDAO();
               movHisDAO.setmLogger(mLogger);
               //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	   	        if (itemDesc.length() > 0 ) {
	   		        if (itemDesc.indexOf("%") != -1) {
	   		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
	   		        }
	   		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	   	        }
               if (afrmDate.length() > 0) {
                       sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate + "'  ";
                       if (atoDate.length() > 0) {
                               sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate + "'  ";
                       }
               } else {
                       if (atoDate.length() > 0) {
                               sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate  + "'  ";
                       }
               }
              
             
                       String aQuery = "select count(*) AS NO_OF_ORDERS ,(SELECT CAST( ENQUIRY_FR_DOLLARS AS VARCHAR)+'.'+ CAST(ENQUIRY_FR_CENTS AS VARCHAR) FROM PLNTMST WHERE PLANT='"+plant+"' ) AS RATE  from "
                                       + "["
                                       + plant
                                       + "_"
                                       + "dohdr] a,"
                                       + "["
                                       + plant
                                       + "_"
                                       + "dodet] b where a.dono=b.dono  " + sCondition ;

                       
                       arrList = movHisDAO.selectForReport(aQuery, ht);
            
              
         

       } catch (Exception e) {
               this.mLogger.exception(this.printLog,
                               "Exception :repportUtil :: getMovHisList:", e);
       }
       return arrList;
    }

    public ArrayList getMobileEnquirySummaryList(Hashtable ht, String afrmDate,
                    String atoDate, String dirType, String plant, String itemDesc) {
            ArrayList arrList = new ArrayList();
            String sCondition = "";
            try {
                    MovHisDAO movHisDAO = new MovHisDAO();
                    movHisDAO.setmLogger(mLogger);
                    //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        	        if (itemDesc.length() > 0 ) {
        		        if (itemDesc.indexOf("%") != -1) {
        		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
        		        }
        		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
        	        }
                    if (afrmDate.length() > 0) {
                            sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
                                            + "'  ";
                            if (atoDate.length() > 0) {
                                    sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
                                                    + "'  ";
                            }
                    } else {
                            if (atoDate.length() > 0) {
                                    sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
                                                    + "'  ";
                            }
                    }
                    
                    if (dirType.equalsIgnoreCase("MOBILE_ENQUIRY")) {
                            ArrayList al=this.getTotalNoofEnQuires( ht,  afrmDate, atoDate,  dirType,  plant,  itemDesc);
                            String noofEnQuires="0",enQuiryRate="0";
                        if(al.size()>0){
                            Map lineArr = (Map) al.get(0);
                            noofEnQuires = StrUtils.fString((String) lineArr.get("NO_OF_ORDERS"));
                            enQuiryRate = StrUtils.fString((String) lineArr.get("RATE"));
                        }else{
                            noofEnQuires="0";
                            enQuiryRate="0";
                        }
                        
                            String aQuery = "select b.dono,isnull(a.ordertype,'') as ordertype,a.jobNum,a.custname,b.item,(SELECT ITEMDESC FROM "+plant+"_ITEMMST WHERE ITEM=b.ITEM) as itemdesc,b.unitprice,a.CollectionDate TRANDATE,a.CollectionTime TIME ,b.lnstat ,b.qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,'"+noofEnQuires+"' as No_Enquires,'"+enQuiryRate+"' as EnquiryRate from "
                                            + "["
                                            + plant
                                            + "_"
                                            + "dohdr] a,"
                                            + "["
                                            + plant
                                            + "_"
                                            + "dodet] b where a.dono=b.dono  " + sCondition ;

                            
                            arrList = movHisDAO.selectForReport(aQuery, ht);
                    }
                   

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog,
                                    "Exception :repportUtil :: getMovHisList:", e);
            }
            return arrList;
    }
    public ArrayList getMobileRegisterSummaryList(Hashtable ht, String afrmDate,
            String atoDate, String dirType, String plant, String itemDesc) {
    ArrayList arrList = new ArrayList();
    String sCondition = "";
    try {
            MovHisDAO movHisDAO = new MovHisDAO();
            movHisDAO.setmLogger(mLogger);
           //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
            if (afrmDate.length() > 0) {
            				sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
                                    + "'  ";
                    if (atoDate.length() > 0) {
                            sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
                                            + "'  ";
                    }
            } else {
                    if (atoDate.length() > 0) {
                            sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
                                            + "'  ";
                    }
            }
            
            if (dirType.equalsIgnoreCase("MOBILE_REGISTER")) {
//                    ArrayList al=this.getTotalNoofEnQuires( ht,  afrmDate, atoDate,  dirType,  plant,  itemDesc);
//                    String noofEnQuires="0",enQuiryRate="0";
//                if(al.size()>0){
//                    Map lineArr = (Map) al.get(0);
//                    noofEnQuires = StrUtils.fString((String) lineArr.get("NO_OF_ORDERS"));
//                    enQuiryRate = StrUtils.fString((String) lineArr.get("RATE"));
//                }else{
//                    noofEnQuires="0";
//                    enQuiryRate="0";
//                }
                //Added trantype
                    String aQuery = "select b.dono,isnull(a.ordertype,'') as ordertype,a.jobNum,a.custname,a.custcode,b.item,(SELECT ITEMDESC FROM "+plant+"_ITEMMST WHERE ITEM=b.ITEM) as itemdesc,b.unitprice,a.CollectionDate TRANDATE,a.CollectionTime TIME ,b.lnstat ,isnull(b.trantype,'') trantype,b.qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,isnull(deliverydate,'')deliverydate,isnull(timeslots,'') deliverytime from "
                                    + "["
                                    + plant
                                    + "_"
                                    + "dohdr] a,"
                                    + "["
                                    + plant
                                    + "_"
                                    + "dodet] b where a.dono=b.dono  " + sCondition ;

                    
                    arrList = movHisDAO.selectForReport(aQuery, ht);
            }
           

    } catch (Exception e) {
            this.mLogger.exception(this.printLog,
                            "Exception :repportUtil :: getMovHisList:", e);
    }
    return arrList;
}
    public ArrayList getMobileAttendanceList(Hashtable ht, String donoarray,
             String plant) {
    ArrayList arrList = new ArrayList();
//    String sCondition = "";
    try {
            MovHisDAO movHisDAO = new MovHisDAO();
            movHisDAO.setmLogger(mLogger);
           
                    String aQuery = "select b.dono,isnull(a.ordertype,'') as ordertype,a.jobNum,a.custname,b.item,(SELECT ITEMDESC FROM "+plant+"_ITEMMST WHERE ITEM=b.ITEM) as itemdesc,b.unitprice,a.CollectionDate TRANDATE,a.CollectionTime TIME ,b.lnstat ,b.qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,isnull(deliverydate,'')deliverydate,isnull(timeslots,'') deliverytime from "
                                    + "["
                                    + plant
                                    + "_"
                                    + "dohdr] a,"
                                    + "["
                                    + plant
                                    + "_"
                                    + "dodet] b where a.dono=b.dono  " + " and b.dono in("+donoarray+")" ;
                   
                    arrList = movHisDAO.selectForReport(aQuery, ht);
             } catch (Exception e) {
            this.mLogger.exception(this.printLog,
                            "Exception :repportUtil :: getMovHisList:", e);
    }
    return arrList;
}    
    public ArrayList getAttendanceClockinList(Hashtable ht, String afrmDate,
            String atoDate, String trantype, String plant, String itemDesc) throws Exception {
   ArrayList arrList = new ArrayList();
   String sCondition = "";
   java.sql.Connection con = null;
   try {
	   con = com.track.gates.DbBean.getConnection();
           MovHisDAO movHisDAO = new MovHisDAO();
           movHisDAO.setmLogger(mLogger);
           StringBuffer aQuery = new StringBuffer();
           //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
          if (afrmDate.length() > 0) {
          				sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
                                  + "'  ";
                  if (atoDate.length() > 0) {
                          sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
                                          + "'  ";
                  }
          } else {
                  if (atoDate.length() > 0) {
                          sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
                                          + "'  ";
                  }
          }
          String aQuery2 = "select b.dono,isnull(a.ordertype,'') as ordertype,a.jobNum,a.custname,b.item,(SELECT ITEMDESC FROM "+plant+"_ITEMMST WHERE ITEM=b.ITEM) as itemdesc,b.unitprice,a.CollectionDate TRANDATE,a.CollectionTime TIME ,b.lnstat ,b.qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,isnull(deliverydate,'')deliverydate,isnull(timeslots,'') deliverytime from "
          + "["
          + plant
          + "_"
          + "dohdr] a,"
          + "["
          + plant
          + "_"
          + "dodet] b where a.dono=b.dono  " + sCondition ;
          if (ht.size() > 0) {
       	   aQuery2 += "  AND";
		String  condtn =  formCondition(ht);
       	   aQuery2 += " " + condtn;
			} 
          ht.remove("b.trantype");
                   String aQuery1 = "select b.dono,isnull(a.ordertype,'') as ordertype,a.jobNum,a.custname,b.item,(SELECT ITEMDESC FROM "+plant+"_ITEMMST WHERE ITEM=b.ITEM) as itemdesc,b.unitprice,a.CollectionDate TRANDATE,a.CollectionTime TIME ,b.lnstat ,b.qtyor,isnull(b.qtyis,0) as qty, isnull(b.qtyPick,0) as qtyPick, b.pickstatus,isnull(deliverydate,'')deliverydate,isnull(timeslots,'') deliverytime from "
                                   + "["
                                   + plant
                                   + "_"
                                   + "dohdr] a,"
                                   + "["
                                   + plant
                                   + "_"
                                   + "dodet] b where a.dono=b.dono AND B.TRANTYPE is NULL   "+sCondition ;
                   if (ht.size() > 0) {
                	   aQuery1 += "  AND";
                	  String  condtn =  formCondition(ht);
                	   aQuery1 += " " + condtn;
       			}                          
                aQuery.append(aQuery1);
                aQuery.append(" UNION  ");
                aQuery.append(aQuery2);
System.out.println("query"+aQuery.toString());
                arrList = selectData(con, aQuery.toString());         
            } catch (Exception e) {
           this.mLogger.exception(this.printLog,
                           "Exception :repportUtil :: getMovHisList:", e);
           
			throw e;
   }
            finally{
            	if (con != null) {
    				DbBean.closeConnection(con);
    			}
            }
   return arrList;
}       
    
    public ArrayList getMobileShoppingSummary(Hashtable ht, String afrmDate,
                           String atoDate, String dirType, String plant, String itemDesc,String custcode) {
                   ArrayList arrList = new ArrayList();
                   String sCondition = "";
                   try {
                       
                           MovHisDAO movHisDAO = new MovHisDAO();
                           movHisDAO.setmLogger(mLogger);
                         //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	               	        if (itemDesc.length() > 0 ) {
	               		        if (itemDesc.indexOf("%") != -1) {
	               		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
	               		        }
	               		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	               	        }
                           if (afrmDate.length() > 0) {
                                   sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
                                                   + "'  ";
                                   if (atoDate.length() > 0) {
                                           sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
                                                           + "'    ";
                                   }
                           } else {
                                   if (atoDate.length() > 0) {
                                           sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
                                                           + "'  ";
                                   }
                           }
                           
                           if(custcode.length()>0){
                             sCondition= sCondition+  " and a.custcode='"+custcode+"'   ";
                           }
                                   
                                   StringBuffer sql = new StringBuffer(" select distinct a.custcode,a.dono,a.item,b.ordertype,b.custname,a.contactnum,b.ISMEMBER,b.orddate,");
                                   sql.append("b.trandate, b.jobnum,b.lnstat,isnull(b.qtyor,0) qtyor," );
                                   sql.append(" isnull(b.qtypick,0) qtypick,isnull(b.qty,0) qty,isnull(b.unitprice,0) unitprice,isnull(b.total,0) total,");
                                   sql.append(" isnull(b.deliverydate,'')deliverydate,isnull(b.deliverytime,'') deliverytime  ");

                                   sql.append(" from");
                                   sql.append("(");
                                   sql.append(" select a.custcode,a.contactnum,a.dono,b.item");
                                   sql.append(" from " + "[" + plant + "_" + "dohdr" + "] a,");
                                   sql.append( "[" + plant + "_" + "dodet" + "] b");
                                   sql.append(" where a.DONO=b.dono");
                                   sql.append(" group by a.CustCode,a.contactnum,a.DONO,b.item,b.qtyis) a,");
                                   sql.append("(select a.custcode,(SELECT ISMEMBER FROM "+plant+"_CUSTMST WHERE CUSTNO=a.custcode) as ISMEMBER,a.custname,a.contactnum,a.dono,isnull(a.jobnum,'') jobnum,isnull(a.ordertype,'') ordertype,b.item,");
                                   sql.append(" b.lnstat,isnull(b.qtyor,0) qtyor,isnull(b.qtypick,0) qtypick,isnull(b.qtyis,0) qty,");
                                   sql.append(" isnull(b.unitprice,0) unitprice,b.crat,");
                                   sql.append(" isnull(a.collectionDate,'')+' '+isnull(a.collectionTime,'') as orddate, b.trandate as trandate,ISNULL(qtypick*unitprice,0) total,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') deliverytime  ");

                                   sql.append(" from " + "[" + plant + "_" + "dohdr" + "] a,");
                                   sql.append( "[" + plant + "_" + "dodet" + "] b");
                                   sql.append(" where a.DONO=b.dono) b");
                                   sql.append(" where a.custcode=b.custcode and a.dono=b.dono and a.item=b.item  "  + sCondition);
                                   arrList = movHisDAO.selectForReport(sql.toString(), ht);
           
                   } catch (Exception e) {
                           this.mLogger.exception(this.printLog,
                                           "Exception :repportUtil :: getCustomerDOInvoiceSummary:", e);
                   }
                   return arrList;
           }
           
    public ArrayList getsalesChargableRate(String plant,String itemDesc,String afrmDate,String atoDate,Hashtable ht) {
            ArrayList arrList = new ArrayList();
        
            String sCondition = "";
            try {
                    MovHisDAO movHisDAO = new MovHisDAO();
                    movHisDAO.setmLogger(mLogger);
                    
             
                  //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        	        if (itemDesc.length() > 0 ) {
        		        if (itemDesc.indexOf("%") != -1) {
        		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
        		        }
        		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
        	        }
                if (afrmDate.length() > 0) {
                        sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
                                        + "'  ";
                        if (atoDate.length() > 0) {
                                sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
                                                + "'    ";
                        }
                } else {
                        if (atoDate.length() > 0) {
                                sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
                                                + "'  ";
                        }
                }
                    
                    
                String aQuery = " select count(distinct a.dono) AS NO_OF_ORDERS ,(select SALES_CHARGE_BY  from plntmst WHERE PLANT ='"+plant+"' )AS SALES_CHARGE_BY,(select CASE WHEN SALES_CHARGE_BY ='FLATRATE' THEN CAST(SALES_FR_DOLLARS AS VARCHAR) +'.'+CAST(SALES_FR_CENTS AS VARCHAR)" 
                 + "ELSE  CAST(SALES_PERCENT AS VARCHAR) END   from plntmst WHERE PLANT ='"+plant+"' )AS SALESRATE  from "
                                + "["
                                + plant
                                + "_"
                                + "dohdr] a,"
                                + "["
                                + plant
                                + "_"
                                + "dodet] b where a.dono=b.dono  " + sCondition ;
                                
                                System.out.println(aQuery);

                
                arrList = movHisDAO.selectForReport(aQuery, ht);

                        
                  
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog,
                                    "Exception :repportUtil :: getMovHisList:", e);
            }
            return arrList;
    }
    public ArrayList getOrderDetailsExportList(Hashtable ht, String dirType, String plant) {
		ArrayList arrList = new ArrayList();
//		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
               if (dirType.equalsIgnoreCase("INBOUND")) {
				String aQuery = "select a.pono,polnno,isnull(jobnum,'') as jobnum,isnull(ordertype,'')ordertype,isnull(collectiondate,'')collectiondate,isnull(collectiontime,'')collectiontime,isnull(remark1,'') as remarks,isnull(inbound_gst,'')inbound_gst,isnull(custcode,'')custcode,item,isnull((select ItemDesc from "+plant+"_ITEMMST where ITEM=b.item),'')as itemdesc,isnull(USERFLD4,'') as manufacturer,qtyor,unitmo,unitcost,isnull(currencyid,'')currencyid,isnull(remark3,'') remarks3, "
						+ " isnull(a.orderdiscount,0) orderdiscount,isnull(a.shippingcost,0) shippingcost,isnull(a.incoterms,'') incoterms,isnull(a.shippingcustomer,'')shippingcustomer,isnull(a.LOCALEXPENSES,'') localexpenses,isnull(a.PAYMENTTYPE,'') paymenttype, "
						+ "ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,"
         				+ "CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT FROM "
						+ "["
						+ plant
						+ "_"
						+ "pohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "podet] b where a.pono=b.pono and a.PLANT<>'' " ; 
						

				arrList = movHisDAO.selectForReport(aQuery, ht);
			}
			if (dirType.equalsIgnoreCase("RENTAL")) {
    				String aQuery = "select a.ORDNO,ORDLNNO,a.LOC as frLoc,a.LOC1 as toLoc,isnull(a.jobNum,'') as jobnum,isnull(ordertype,'')ordertype,isnull(collectiondate,'')collectiondate,isnull(collectiontime,'')collectiontime,isnull(remark1,'') as remarks,isnull(a.rental_gst,'')rental_gst,isnull(custcode,'')custcode,item,isnull((select ItemDesc from "+plant+"_ITEMMST where ITEM=b.item),'')as itemdesc,isnull(a.USERFLD4,'') as manufacturer,qtyor,unitmo,b.RENTALPRICE,isnull(currencyid,'')currencyid, "
    						+ " isnull(a.orderdiscount,0) orderdiscount,isnull(a.shippingcost,0) shippingcost,isnull(a.shippingcustomer,'')shippingcustomer,isnull(a.PAYMENTTYPE,'') paymenttype from "
    						+ "["
    						+ plant
    						+ "_"
    						+ "loanhdr] a,"
    						+ "["
    						+ plant
    						+ "_"
    						+ "loandet] b where a.ORDNO=b.ORDNO and a.PLANT<>'' " ; 
    						

    				arrList = movHisDAO.selectForReport(aQuery, ht);
    			}
			if (dirType.equalsIgnoreCase("OUTBOUND")) {
				String aQuery = "select a.dono,dolnno,isnull(jobnum,'') as jobnum,isnull(ordertype,'')ordertype,isnull(collectiondate,'')collectiondate,isnull(collectiontime,'')collectiontime,isnull(remark1,'') as remarks,isnull(remark3,'') as remarks2,isnull(custcode,'')custcode,item,isnull((select ItemDesc from "+plant+"_ITEMMST where ITEM=b.item),'')as itemdesc,qtyor,unitmo,unitprice,isnull(currencyid,'')currencyid,isnull(empno,'')empno, "
						+ " isnull(a.orderdiscount,0) orderdiscount,isnull(a.shippingcost,0) shippingcost,isnull(a.incoterms,'') incoterms,isnull(a.shippingcustomer,'')shippingcustomer,isnull(a.PAYMENTTYPE,'') paymenttype, "
						+ "ISNULL(SALES_LOCATION,'') SALES_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT FROM "
						+ "["
						+ plant
						+ "_"
						+ "dohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "dodet] b where a.dono=b.dono and a.PLANT<>'' "; 
					arrList = movHisDAO.selectForReport(aQuery, ht);
			}
			
			if (dirType.equalsIgnoreCase("TRANSFER")) {
				String aQuery = "select a.tono,tolnno,isnull(jobnum,'') as jobnum,isnull(custcode,'')custcode,isnull(fromwarehouse,'')fromloc,isnull(towarehouse,'')toloc,isnull(collectiondate,'')collectiondate,isnull(collectiontime,'')collectiontime,isnull(remark1,'') as remarks,item,isnull(itemdesc,'')itemdesc,qtyor,unitmo from "
						+ "["
						+ plant
						+ "_"
						+ "tohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "todet] b	where a.tono=b.tono and a.PLANT<>'' " ;
				arrList = movHisDAO.selectForReport(aQuery, ht);

			}
			
			if (dirType.equalsIgnoreCase("CONSIGNMENT")) {
				String aQuery = "select a.tono,tolnno,isnull(jobnum,'') as jobnum,isnull(custcode,'')custcode,isnull(fromwarehouse,'')fromloc,isnull(towarehouse,'')toloc,isnull(collectiondate,'')collectiondate,isnull(collectiontime,'')collectiontime,isnull(remark1,'') as remarks,item,isnull(itemdesc,'')itemdesc,qtyor,unitmo from "
						+ "["
						+ plant
						+ "_"
						+ "tohdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "todet] b	where a.tono=b.tono and a.PLANT<>'' " ;
				arrList = movHisDAO.selectForReport(aQuery, ht);

			}

			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getMovHisList:", e);
		}
		return arrList;
	}

  //End code by radhika for export IB/OB/TO data same as IB/OB/TO download template on 10 sep 2013	

//start code by radhika for received orders history on 24 sep 2013    
public ArrayList getReceivedInboundOrdersSummary(Hashtable ht, String afrmDate,
			String atoDate,String plant, String custname) {
	System.out.println("Come in 2");
		ArrayList arrList = new ArrayList();
		String sCondition = "",extcond="", dtCondStr="";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			                         
			
			  dtCondStr ="and ISNULL(a.recvdate,'')<>'' AND CAST((SUBSTRING(a.recvdate, 7, 4) + '-' + SUBSTRING(a.recvdate, 4, 2) + '-' + SUBSTRING(a.recvdate, 1, 2)) AS date)";
			  if (ht.size() > 0) 
			  {
				  if (ht.get("SUPPLIERTYPE") != null) {
					  String suppliertype = ht.get("SUPPLIERTYPE").toString();
					  sCondition= sCondition+" AND b.custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
					  ht.remove("SUPPLIERTYPE");
				  }
				  if (ht.get("a.PONO") != null) {
					  String pono = ht.get("a.PONO").toString();
					  sCondition= sCondition+" AND A.PONO = '"+pono+"'";
					  ht.remove("a.PONO");
				  }
				  if (ht.get("JobNum") != null) {
					  String jobpono = ht.get("JobNum").toString();
					  sCondition= sCondition+" AND JobNum = '"+jobpono+"'";
					  ht.remove("JobNum");
				  }
			  }
				if (afrmDate.length() > 0) {
               	sCondition = sCondition + dtCondStr + " >= '" 
   						+ afrmDate
   						+ "'  order by (recvdate) desc,pono desc";
   				if (atoDate.length() > 0) {
   					sCondition = sCondition + dtCondStr+ " <= '" 
   					+ atoDate
   					+ "'  order by (recvdate) desc,pono desc";
   				}
   			} else {
   				if (atoDate.length() > 0) {
   					sCondition = sCondition + dtCondStr + "  <= '" 
   					+ atoDate
   					+ "'  order by (recvdate) desc,pono desc";
   				}
   			}
    			if (custname.length()>0){
                             	custname = StrUtils.InsertQuotes(custname);
                             	sCondition = sCondition + " AND B.CUSTNAME LIKE '%"+custname+"%' " ;
                 }
    			
    			StringBuffer sql_PO = new StringBuffer();
    			//below query changed by radhika for getting invoice no in goods receipt summary report on 13 aug 2013
    			
    				 sql_PO
  					.append("select distinct a.PONO pono,B.CURRENCYID,B.CURRENCYUSEQT,JobNum,custname,SUBSTRING(a.crat,1,8) as crat,isnull(a.recvdate,'') as recvdate,inbound_gst,ISNULL(a.crby,'')receivedby,ISNULL(b.STATUS_ID,'') as status_id,"
  								+"((select isnull(SUM(RECQTY*UNITCOST),0) from" 
								+ "["
								+ plant
								+ "_"
								+"RECVDET]  where plant =b.plant and PONO=b.PONO  and isnull(recvdate,'')=isnull(a.recvdate,'') and crby=a.crby)*inbound_gst) /100 taxval, B.TAXID as TAXID,  "
 								+"(select isnull(SUM(RECQTY*UNITCOST),0) from" 
 								+ "["
 								+ plant
 								+ "_"
 								+"RECVDET]  where plant =b.plant and PONO=b.PONO  and isnull(recvdate,'')=isnull(a.recvdate,'') and crby=a.crby)subtotal  from "// and TRAN_TYPE='IB' BY SAMATHA removed because subtotal is wrong if there are non stock Items
 						    	+ "["
  							+ plant
  							+ "_"
  							+ "recvdet] a, "
  							+ "["
  							+ plant
  							+ "_"
  							+ "pohdr] b where a.PONO=b.PONO " // and  TRAN_TYPE='IB'
  							+ sCondition);	
          	              arrList = movHisDAO.selectForReport(sql_PO.toString(), ht,extcond);
    			 
    			
    	
    		} catch (Exception e) {
    			this.mLogger.exception(this.printLog,
    					"Exception :repportUtil :: getReceivedInboundOrdersSummary:", e);
    		}
    		return arrList;
    	}

public ArrayList getReceivedInboundOrdersSummarylocalexpenses(Hashtable ht, String afrmDate,
		String atoDate,String plant, String custname) {
System.out.println("Come in 2");
	ArrayList arrList = new ArrayList();
	String sCondition = "",extcond="", dtCondStr="";
	try {
		MovHisDAO movHisDAO = new MovHisDAO();
		movHisDAO.setmLogger(mLogger);
		                         
		
		  dtCondStr ="and ISNULL(a.recvdate,'')<>'' AND CAST((SUBSTRING(a.recvdate, 7, 4) + '-' + SUBSTRING(a.recvdate, 4, 2) + '-' + SUBSTRING(a.recvdate, 1, 2)) AS date)";
			if (afrmDate.length() > 0) {
           	sCondition = sCondition + dtCondStr + " >= '" 
						+ afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + dtCondStr + "  <= '" 
					+ atoDate
					+ "'  ";
				}
			}
		  if (ht.size() > 0) 
		  {
				if (ht.get("SUPPLIERTYPE") != null) {
								String suppliertype = ht.get("SUPPLIERTYPE").toString();
								sCondition= sCondition+" AND b.custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
								ht.remove("SUPPLIERTYPE");
				}
				  if (ht.get("a.PONO") != null) {
					  String pono = ht.get("a.PONO").toString();
					  sCondition= sCondition+" AND A.PONO = '"+pono+"'";
					  ht.remove("a.PONO");
				  }
				  if (ht.get("JobNum") != null) {
					  String jobpono = ht.get("JobNum").toString();
					  sCondition= sCondition+" AND A.JobNum = '"+jobpono+"'";
					  ht.remove("JobNum");
				  }
		  }
			if (custname.length()>0){
                         	custname = StrUtils.InsertQuotes(custname);
                         	sCondition = sCondition + " AND B.CUSTNAME LIKE '%"+custname+"%' " ;
             }
			
			StringBuffer sql_PO = new StringBuffer();
			//below query changed by radhika for getting invoice no in goods receipt summary report on 13 aug 2013
			
				 sql_PO
					.append("WITH temptable AS (select a.PONO pono,B.CURRENCYID,B.CURRENCYUSEQT,JobNum,custname,SUBSTRING(a.crat,1,8) as crat,isnull(a.recvdate,'') as recvdate,inbound_gst,ISNULL(a.crby,'')receivedby,ISNULL(b.STATUS_ID,'') as status_id,RECQTY,"
							+"isnull(((isnull(A.unitcost*s.CURRENCYUSEQT,0) +isnull((SELECT (SUM(LANDED_COST)/COUNT(LANDED_COST)) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and s.POLNNO = d.LNNO),0) + "
							+ "(isnull(A.unitcost,0)*s.CURRENCYUSEQT) * (((isnull(b.LOCALEXPENSES,0)+CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on " + 
							" c.ID = d.BILLHDRID and c.PONO = a.PONO and s.POLNNO = d.LNNO) is null THEN SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+ plant+"_podet] s where s.pono=A.pono),0)),0))/100)/ (s.CURRENCYUSEQT)),0) as unitcost from" 	
					    	+ "["+ plant+ "_"+ "recvdet] a, ["+ plant+ "_"+ "pohdr] b,["+ plant+"_podet] s where a.PONO=b.PONO and LNNO = POLNNO and s.pono=A.pono and s.ITEM=A.ITEM) "
							+ "select A.pono,B.CURRENCYID,B.CURRENCYUSEQT,A.JobNum,A.CustName as custname,A.crat,A.receivedby,A.recvdate,A.status_id,A.INBOUND_GST as inbound_gst,B.TAXID as TAXID,"
							+ "((SUM(RECQTY*unitcost))*A.INBOUND_GST) /100 taxval,SUM(RECQTY*unitcost) as subtotal"
							+" from temptable A, ["+ plant+"_pohdr] b where a.PONO=b.PONO "
							+ sCondition);
				 		extcond+=" GROUP BY A.pono,B.CURRENCYID,B.CURRENCYUSEQT,A.JobNum,A.CustName,A.crat,A.receivedby,A.recvdate,A.status_id,A.INBOUND_GST,B.TAXID order by CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) desc,A.PONO desc";
      	              arrList = movHisDAO.selectForReport(sql_PO.toString(), ht,extcond);
			 
			
	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getReceivedInboundOrdersSummary:", e);
		}
		return arrList;
	}
  
    public ArrayList getIssuedOutboundOrdersSummary(Hashtable ht, String afrmDate,
    			String atoDate,String plant, String custname,String ispos) {
    		ArrayList arrList = new ArrayList();
    		String sCondition = "",extcond="",sCondition1 = "";//,sCondition2="";
    		try {
    			MovHisDAO movHisDAO = new MovHisDAO();
    			movHisDAO.setmLogger(mLogger);
    			
    			String posext="";
    			String posext1="";
    		   	if(ispos.equalsIgnoreCase("3"))
    		   		posext =  " AND ORDERTYPE = 'POS' " ;
    		   	else if(ispos.equalsIgnoreCase("2"))
    		   		posext =  " AND ORDERTYPE != 'POS' " ;
    		   	
    		   	if(ispos.equalsIgnoreCase("3"))
    		   		posext1 =  " AND a.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='POS' AND  DONO=a.DONO)";
			   	else if(ispos.equalsIgnoreCase("2"))
			   		posext1 =  " AND a.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE !='POS' AND  DONO=a.DONO)";
                              
    			if (afrmDate.length() > 0) {

    				sCondition = sCondition + " AND  SUBSTRING(a.issuedate, 7, 4) + SUBSTRING(a.issuedate, 4, 2) + SUBSTRING(a.issuedate, 1, 2) >= '"// 
    						+ afrmDate + "'  ";
    				if (atoDate.length() > 0) {
    					sCondition = sCondition + " AND SUBSTRING(a.issuedate, 7, 4) + SUBSTRING(a.issuedate, 4, 2) + SUBSTRING(a.issuedate, 1, 2) <= '" 
    							+ atoDate + "'  ";
    				}
    			} else {
    				if (atoDate.length() > 0) {
    					sCondition = sCondition + "  SUBSTRING(a.issuedate, 7, 4) + SUBSTRING(a.issuedate, 4, 2) + SUBSTRING(a.issuedate, 1, 2) <= '" 
    							+ atoDate + "'  ";
    				}
    			}
    			if (custname.length()>0){
                             	custname = StrUtils.InsertQuotes(custname);
                             	sCondition = sCondition + " AND B.CUSTNAME LIKE '%"+custname+"%' " ;
                 }
    			
    			if (ht.size() > 0) {
					if (ht.get("CUSTTYPE") != null) {
						String custtype = ht.get("CUSTTYPE").toString();
						sCondition= sCondition+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
						ht.remove("CUSTTYPE");
					}
					else if (ht.get("a.DONO") != null) {
						String dono = ht.get("a.DONO").toString();
						sCondition= sCondition+" AND a.DONO = '"+dono+"'";
					}
					else if (ht.get("b.STATUS_ID") != null) {
						String staus = ht.get("b.STATUS_ID").toString();
						sCondition= sCondition+" AND b.STATUS_ID = '"+staus+"'";
						sCondition1= sCondition1+" AND b.STATUS = '"+staus+"'";
					}
					
				}
    			
    			
    			boolean isLocationProvided = false;
    			if (ht.size() > 0) {
	    			if (ht.get("LOC") != null && !"".equals(ht.get("LOC"))) {
	    				String loc = ht.get("LOC").toString();
	    				sCondition = sCondition+" AND A.LOC ='"+loc+"'";			
						ht.remove("LOC");	
						isLocationProvided = true;
	    	        }
	    			if (ht.get("LOC_TYPE_ID") != null && !"".equals(ht.get("LOC_TYPE_ID"))) {
						String loctypeid = ht.get("LOC_TYPE_ID").toString();
						sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
						ht.remove("LOC_TYPE_ID");
						isLocationProvided = true;
					}
	    			if (ht.get("LOC_TYPE_ID2") != null && !"".equals(ht.get("LOC_TYPE_ID2"))) {
						String loctypeid2 = ht.get("LOC_TYPE_ID2").toString();
						sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )";
						ht.remove("LOC_TYPE_ID2");
						isLocationProvided = true;
					}
	    			if (ht.get("LOC_TYPE_ID3") != null && !"".equals(ht.get("LOC_TYPE_ID3"))) {
	    				String loctypeid3 = ht.get("LOC_TYPE_ID3").toString();
	    				sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,loctypeid3)+" )";
	    				ht.remove("LOC_TYPE_ID3");
	    				isLocationProvided = true;
	    			}
	    			if ("LOCATION".equals(ht.get("SORT"))) {
						ht.remove("SORT");
						isLocationProvided = true;
					}
    			}
    			
    			String locationColumn = "";
				if (isLocationProvided) {
					locationColumn = "a.loc,";
				}
				
    			extcond = " group by a.DONO," + locationColumn + "CustName,issuedate,outbound_gst,a.crby,status_id "; 
 
    			StringBuffer sql_PO = new StringBuffer();
    			
    			sql_PO.append("select distinct A.DONO dono," + locationColumn + " custname,issuedate,ISNULL(outbound_gst,'')outbound_gst,ISNULL(a.crby,'')issuedby,ISNULL(b.STATUS_ID,'') as status_id,"
						+" isnull(SUM(PICKQTY*UNITPRICE),0) subtotal,isnull(((SUM(PICKQTY*UNITPRICE)*outbound_gst)/100),0) taxval"
						+" from "
				    	+ "["
						+ plant
						+ "_"
						+ "shiphis] a, "
						+ "["
						+ plant
						+ "_"
						+ "dohdr] b where a.DONO=b.DONO and  a.DONO <> 'GOODSISSUE' AND a.STATUS='C'" 
						+ posext+sCondition+sCondition1+extcond
						+ " UNION ALL "
						+ "select distinct A.DONO dono,"+ locationColumn + "custname,issuedate,ISNULL(outbound_gst,'')outbound_gst,ISNULL(a.crby,'')issuedby,ISNULL(b.STATUS,'') as status_id,"
    							+" isnull(SUM(PICKQTY*UNITPRICE),0) subtotal,isnull(((SUM(PICKQTY*UNITPRICE)*outbound_gst)/100),0) taxval"
								+" from "
						    	+ "["
    							+ plant
    							+ "_"
    							+ "shiphis] a, "
    							+ "["
    							+ plant
    							+ "_"
    							+ "poshdr] b where a.DONO=b.POSTRANID and  a.DONO like 'T%' and  a.DONO <> 'GOODSISSUE' AND a.STATUS='C'" 
    							+ posext1+sCondition+sCondition1+" group by a.DONO,"+ locationColumn +"CustName,issuedate,outbound_gst,a.crby,b.STATUS");
    			if (isLocationProvided) {
    				sql_PO.append( " order by LOC");
    			}
    			ht= new Hashtable();
    			extcond="";
            	arrList = movHisDAO.selectForReport(sql_PO.toString(), ht,extcond);
    	
    		} catch (Exception e) {
    			this.mLogger.exception(this.printLog,
    					"Exception :repportUtil :: getIssuedOutboundOrdersSummary:", e);
    		}
    		return arrList;
    	}
    
    public ArrayList getVoidSummary(Hashtable ht, String afrmDate,String atoDate,String plant,String customer,String order,String outletcode,String terminalcode,String cust_name,String sales_person,String aITEM,String aPRD_BRAND,String aPRD_CLS,String aPRD_TYPE){
    	ArrayList arrList = new ArrayList();
    	String sCondition = "";
    	try {
    		MovHisDAO movHisDAO = new MovHisDAO();
    		movHisDAO.setmLogger(mLogger);
    		
    		if (afrmDate.length() > 0) {
    			
    			sCondition = sCondition + " AND  SUBSTRING(a.VOID_DATE, 7, 4) + '-' + SUBSTRING(a.VOID_DATE, 4, 2) + '-' + SUBSTRING(a.VOID_DATE, 1, 2) >= '"// 
    					+ afrmDate + "'  ";
    			if (atoDate.length() > 0) {
    				sCondition = sCondition + " AND SUBSTRING(a.VOID_DATE, 7, 4) + '-' + SUBSTRING(a.VOID_DATE, 4, 2) + '-' + SUBSTRING(a.VOID_DATE, 1, 2) <= '" 
    						+ atoDate + "'  ";
    			}
    		} else {
    			if (atoDate.length() > 0) {
    				sCondition = sCondition + "  SUBSTRING(a.VOID_DATE, 7, 4) + '-' + SUBSTRING(a.VOID_DATE, 4, 2) + '-' + SUBSTRING(a.VOID_DATE, 1, 2) <= '" 
    						+ atoDate + "'  ";
    			}
    		}
    		if (customer.length()>0){
    			customer = StrUtils.InsertQuotes(customer);
    			sCondition = sCondition + " AND a.CUSTNAME LIKE '%"+customer+"%' " ;
    		}
    		
    		if (order.length()>0){
    			sCondition = sCondition + " AND a.DONO LIKE '%"+order+"%' " ;
    		}
    		
    		if (outletcode.length()>0){
    			sCondition = sCondition + " AND a.OUTLET LIKE '%"+outletcode+"%' " ;
    		}
    		
    		if (terminalcode.length()>0){
    			sCondition = sCondition + " AND a.TERMINAL LIKE '%"+terminalcode+"%' " ;
    		}
    		
    		if (cust_name.length()>0){
    			sCondition = sCondition + " AND a.VOID_EMPNO LIKE '%"+cust_name+"%' " ;
    		}
    		
    		if (sales_person.length()>0){
    			sCondition = sCondition + " AND a.EMPNO LIKE '%"+sales_person+"%' " ;
    		}
    		
    		if (aITEM.length()>0){
    			sCondition = sCondition + " AND a.DONO IN (SELECT DONO FROM "+plant+"_dodet WHERE ITEM LIKE '%"+aITEM+"%') " ;
    		}

    		if (aPRD_BRAND.length()>0){
    			sCondition = sCondition + " AND a.DONO IN (SELECT DONO FROM "+plant+"_dodet a WHERE a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + aPRD_BRAND + "' AND  ITEM=a.ITEM)) " ;
    		}

    		if (aPRD_CLS.length()>0){
    			sCondition = sCondition + " AND a.DONO IN (SELECT DONO FROM "+plant+"_dodet a WHERE a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + aPRD_CLS + "' AND  ITEM=a.ITEM)) " ;
    		}

    		if (aPRD_TYPE.length()>0){
    			sCondition = sCondition + " AND a.DONO IN (SELECT DONO FROM "+plant+"_dodet a WHERE a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + aPRD_TYPE + "' AND  ITEM=a.ITEM)) " ;
    		}
    		
    		
    		StringBuffer sql_PO = new StringBuffer();
    		
    		sql_PO.append("select distinct a.DONO dono,a.custname,a.VOID_DATE as CollectionDate,ISNULL(a.outbound_gst,'')outbound_gst,a.ISORDERDISCOUNTTAX,"
    				+" ISNULL((select sum(b.QTYOR * b.UNITPRICE) from "+plant+"_dodet b where b.dono = a.dono),0) subtotal,0 taxval,a.ORDERDISCOUNT,a.ORDERDISCOUNTTYPE,a.TAXID"
    				+" from "
    				+ "["
    				+ plant
    				+ "_"
    				+ "dohdr] a "
    				+ " where a.ORDERTYPE ='POS' AND a.ORDER_STATUS='CANCELLED'" 
    				+ sCondition);
    		
    		ht= new Hashtable();
    		arrList = movHisDAO.selectForReport(sql_PO.toString(), ht);
    		
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog,
    				"Exception :repportUtil :: getIssuedOutboundOrdersSummary:", e);
    	}
    	return arrList;
    }
    
    public ArrayList getPOSFOCSummary(Hashtable ht, String afrmDate,String atoDate,String plant,String customer,String order,String outletcode,String terminalcode,String cust_name,String sales_person,String aITEM,String aPRD_BRAND,String aPRD_CLS,String aPRD_TYPE){
    	ArrayList arrList = new ArrayList();
    	String sCondition = "";
    	try {
    		MovHisDAO movHisDAO = new MovHisDAO();
    		movHisDAO.setmLogger(mLogger);
    		
    		if (afrmDate.length() > 0) {
    			
    			sCondition = sCondition + " AND  SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2) >= '" 
    					+ afrmDate + "'  ";
    			if (atoDate.length() > 0) {
    				sCondition = sCondition + " AND SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2) <= '" 
    						+ atoDate + "'  ";
    			}
    		} else {
    			if (atoDate.length() > 0) {
    				sCondition = sCondition + "  SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2) <= '" 
    						+ atoDate + "'  ";
    			}
    		}
    		if (customer.length()>0){
    			customer = StrUtils.InsertQuotes(customer);
    			sCondition = sCondition + " AND a.CUSTNAME LIKE '%"+customer+"%' " ;
    		}
    		
    		if (order.length()>0){
    			sCondition = sCondition + " AND a.DONO LIKE '%"+order+"%' " ;
    		}
    		
    		if (outletcode.length()>0){
    			sCondition = sCondition + " AND a.OUTLET LIKE '%"+outletcode+"%' " ;
    		}
    		
    		if (terminalcode.length()>0){
    			sCondition = sCondition + " AND a.TERMINAL LIKE '%"+terminalcode+"%' " ;
    		}
    		
    		if (cust_name.length()>0){
    			sCondition = sCondition + " AND a.EMPNO LIKE '%"+cust_name+"%' " ;
    		}
    		
    		if (sales_person.length()>0){
    			sCondition = sCondition + " AND a.EMPNO LIKE '%"+sales_person+"%' " ;
    		}
    		
    		if (aITEM.length()>0){
    			sCondition = sCondition + " AND d.ITEM LIKE '%"+aITEM+"%' " ;
    		}
    		
    		if (aPRD_BRAND.length()>0){
    			sCondition = sCondition + " AND d.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + aPRD_BRAND + "' AND  ITEM=d.ITEM) " ;
    		}
    		
    		if (aPRD_CLS.length()>0){
    			sCondition = sCondition + " AND d.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + aPRD_CLS + "' AND  ITEM=d.ITEM) " ;
    		}
    		
    		if (aPRD_TYPE.length()>0){
    			sCondition = sCondition + " AND d.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + aPRD_TYPE + "' AND  ITEM=d.ITEM) " ;
    		}
    		
    		
    		StringBuffer sql_PO = new StringBuffer();
    		
    		sql_PO.append("select distinct a.DONO dono,a.custname,a.CollectionDate,ISNULL(a.outbound_gst,'')outbound_gst,a.ISORDERDISCOUNTTAX,d.QTYOR,d.item,d.ItemDesc,"
    				+" ISNULL((select sum(b.QTYOR * b.UNITPRICE) from "+plant+"_dodet b where b.dono = a.dono),0) subtotal,0 taxval,a.ORDERDISCOUNT,a.ORDERDISCOUNTTYPE,a.TAXID"
    				+" from "
    				+ "["
    				+ plant
    				+ "_"
    				+ "dohdr] a join "+plant+"_dodet d ON a.dono=d.dono"
    				+ " where a.ORDERTYPE ='POS' AND d.ISFOC=1 " 
    				+ sCondition);
    		
    		ht= new Hashtable();
    		arrList = movHisDAO.selectForReport(sql_PO.toString(), ht);
    		
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog,
    				"Exception :repportUtil :: getIssuedOutboundOrdersSummary:", e);
    	}
    	return arrList;
    }
    
    public ArrayList getPOSreturnSummary(Hashtable ht, String afrmDate,String atoDate,String plant,String customer,String order,String outletcode,String terminalcode,String cust_name,String sales_person,String aITEM,String aPRD_BRAND,String aPRD_CLS,String aPRD_TYPE){
    	ArrayList arrList = new ArrayList();
    	String sCondition = "";
    	try {
    		MovHisDAO movHisDAO = new MovHisDAO();
    		movHisDAO.setmLogger(mLogger);
    		
    		if (afrmDate.length() > 0) {
    			
    			sCondition = sCondition + " AND  SUBSTRING(a.PRDATE, 7, 4) + '-' + SUBSTRING(a.PRDATE, 4, 2) + '-' + SUBSTRING(a.PRDATE, 1, 2) >= '" 
    					+ afrmDate + "'  ";
    			if (atoDate.length() > 0) {
    				sCondition = sCondition + " AND SUBSTRING(a.PRDATE, 7, 4) + '-' + SUBSTRING(a.PRDATE, 4, 2) + '-' + SUBSTRING(a.PRDATE, 1, 2) <= '" 
    						+ atoDate + "'  ";
    			}
    		} else {
    			if (atoDate.length() > 0) {
    				sCondition = sCondition + "  SUBSTRING(a.PRDATE, 7, 4) + '-' + SUBSTRING(a.PRDATE, 4, 2) + '-' + SUBSTRING(a.PRDATE, 1, 2) <= '" 
    						+ atoDate + "'  ";
    			}
    		}
    		if (customer.length()>0){
    			customer = StrUtils.InsertQuotes(customer);
    			sCondition = sCondition + " AND a.CUSTNAME LIKE '%"+customer+"%' " ;
    		}
    		
    		if (order.length()>0){
    			sCondition = sCondition + " AND a.DONO LIKE '%"+order+"%' " ;
    		}
    		
    		if (outletcode.length()>0){
    			sCondition = sCondition + " AND a.OUTLET LIKE '%"+outletcode+"%' " ;
    		}
    		
    		if (terminalcode.length()>0){
    			sCondition = sCondition + " AND a.TERMINAL LIKE '%"+terminalcode+"%' " ;
    		}
    		
    		if (cust_name.length()>0){
    			sCondition = sCondition + " AND a.EMPNO LIKE '%"+cust_name+"%' " ;
    		}
    		
    		if (sales_person.length()>0){
    			sCondition = sCondition + " AND a.EMPNO LIKE '%"+sales_person+"%' " ;
    		}
    		
    		if (aITEM.length()>0){
    			sCondition = sCondition + " AND a.PRNO IN (SELECT PRNO FROM "+plant+"_POSRETURNDET WHERE ITEM LIKE '%"+aITEM+"%') " ;
    		}
    		
    		if (aPRD_BRAND.length()>0){
    			sCondition = sCondition + " AND a.PRNO IN (SELECT PRNO FROM "+plant+"_POSRETURNDET a WHERE a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + aPRD_BRAND + "' AND  ITEM=a.ITEM)) " ;
    		}
    		
    		if (aPRD_CLS.length()>0){
    			sCondition = sCondition + " AND a.PRNO IN (SELECT PRNO FROM "+plant+"_POSRETURNDET a WHERE a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + aPRD_CLS + "' AND  ITEM=a.ITEM)) " ;
    		}
    		
    		if (aPRD_TYPE.length()>0){
    			sCondition = sCondition + " AND a.PRNO IN (SELECT PRNO FROM "+plant+"_POSRETURNDET a WHERE a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + aPRD_TYPE + "' AND  ITEM=a.ITEM)) " ;
    		}
    		
    		
    		StringBuffer sql_PO = new StringBuffer();
    		
    		sql_PO.append("select distinct a.PRNO,ISNULL(a.DONO,'') dono,a.custname,a.PRDATE CollectionDate,ISNULL(a.outbound_gst,'')outbound_gst,"
    				+" ISNULL((select top 1 ORDERDISCOUNT from "+plant+"_DOHDR b where b.dono = a.dono),0) ORDERDISCOUNT,"
    				+" ISNULL((select top 1 ORDERDISCOUNTTYPE from "+plant+"_DOHDR b where b.dono = a.dono),'%') ORDERDISCOUNTTYPE,"
    				+" ISNULL((select top 1 ISORDERDISCOUNTTAX from "+plant+"_DOHDR b where b.dono = a.dono),0) ISORDERDISCOUNTTAX,"
    				+" ISNULL((select sum(b.QTY * b.UNITPRICE) from "+plant+"_POSRETURNDET b where b.prno = a.prno),0) subtotal,0 taxval,a.TAXID"
    				+" from "
    				+ "["
    				+ plant
    				+ "_"
    				+ "POSRETURNHDR] a "
    				+ " where a.PLANT='"+plant+"' " 
    				+ sCondition);
    		
    		ht= new Hashtable();
    		arrList = movHisDAO.selectForReport(sql_PO.toString(), ht);
    		
    	} catch (Exception e) {
    		this.mLogger.exception(this.printLog,
    				"Exception :repportUtil :: getIssuedOutboundOrdersSummary:", e);
    	}
    	return arrList;
    }

    public ArrayList getPOSreturnDetailToPrint(String plant, String orderno,String afrmDate,String atoDate,String aPRNO)
			throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
//		boolean flag = false;
			String dtCondStr="",sCondition="";
			
			dtCondStr =    "  and ISNULL(b.PRDATE,'')<>'' AND CAST((SUBSTRING(b.PRDATE, 7, 4) + '-' + SUBSTRING(b.PRDATE, 4, 2) + '-' + SUBSTRING(b.PRDATE, 1, 2)) AS date)";
			
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
			
			String ConvertUnitCostToOrderCurrency = " ISNULL((CAST(ISNULL(UNITPRICE,0) *(SELECT CURRENCYUSEQT  FROM [" +plant+"_POSRETURNDET] b "
					+ " where PRNO = '"
					+ aPRNO
					+ "' AND PLANT = '"
					+ plant
					+ "' and a.PRLNNO = b.PRLNNO and a.ITEM=b.ITEM) AS DECIMAL(20,4)) ),0) ";
			
			
			StringBuffer sQry = new StringBuffer(
					" SELECT a.PRLNNO as DOLNO,ITEM,ITEMDESC,"
					+ "ISNULL((SELECT top 1 DISCOUNT FROM ["+ plant +"_DODET] s WHERE s.ITEM=a.ITEM and s.DONO=b.DONO and s.DOLNNO=a.PRLNNO),0) AS DISCOUNT,"
					+ "ISNULL((SELECT top 1 DISCOUNT_TYPE FROM ["+ plant +"_DODET] s WHERE s.ITEM=a.ITEM and s.DONO=b.DONO and s.DOLNNO=a.PRLNNO),'') AS DISCOUNT_TYPE,"
					+ "(SELECT top 1 STKUOM FROM "
							+ "["
							+ plant
							+ "_itemmst] WHERE ITEM=a.ITEM"
							+ ") AS UOM," 
							+ConvertUnitCostToOrderCurrency
							+ " AS UPRICE," 
							+ "ISNULL("+ConvertUnitCostToOrderCurrency		
							+ " * SUM(QTY),0) AS PRICE,MAX(QTY) AS ORDQTY," 
							+"b.PRDATE as Trandate,"
							+ "b.CURRENCYID as CurrencyID"
							+ " FROM "
							+ "[" + plant + "_" + "POSRETURNDET" + "] a,["+ plant +"_POSRETURNHDR] b " + "");
			sQry.append(" where a.plant='" + plant + "' and a.PRNO = '" + aPRNO + "' and a.prno=b.prno "+sCondition
					+ " group by a.PRNO,b.DONO,PRLNNO,item,itemdesc,UNITPRICE,b.PRDATE,b.CURRENCYID");
			
			this.mLogger.query(this.printLog, sQry.toString());
			al = selectData(con, sQry.toString());
			
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
    
    //imthi start from consignment withprice
    public ArrayList getConsignmentSummary(Hashtable ht, String afrmDate,
			String atoDate,String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",extcond="",sCondition1 = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
                          
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  SUBSTRING(a.recvdate, 7, 4) + SUBSTRING(a.recvdate, 4, 2) + SUBSTRING(a.recvdate, 1, 2) >= '"// 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND SUBSTRING(a.recvdate, 7, 4) + SUBSTRING(a.recvdate, 4, 2) + SUBSTRING(a.recvdate, 1, 2) <= '" 
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  SUBSTRING(a.recvdate, 7, 4) + SUBSTRING(a.recvdate, 4, 2) + SUBSTRING(a.recvdate, 1, 2) <= '" 
							+ atoDate + "'  ";
				}
			}
			if (custname.length()>0){
                         	custname = StrUtils.InsertQuotes(custname);
                         	sCondition = sCondition + " AND B.CUSTNAME LIKE '%"+custname+"%' " ;
             }
			
			if (ht.size() > 0) {
				if (ht.get("CUSTTYPE") != null) {
					String custtype = ht.get("CUSTTYPE").toString();
					sCondition= sCondition+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
					ht.remove("CUSTTYPE");
				}
				else if (ht.get("a.PONO") != null) {
					String tono = ht.get("a.PONO").toString();
					sCondition= sCondition+" AND a.PONO = '"+tono+"'";
				}
				
				//ORIGINAL
				else if (ht.get("B.STATUS") != null) {
					String staus = ht.get("B.STATUS").toString();
					sCondition= sCondition+" AND B.STATUS = '"+staus+"'";
					sCondition1= sCondition1+" AND B.STATUS = '"+staus+"'";
				}
				else if (ht.get("C.STATUS") != null) {
					String staus = ht.get("C.STATUS").toString();
					sCondition= sCondition+" AND C.STATUS = '"+staus+"'";
					sCondition1= sCondition1+" AND C.STATUS = '"+staus+"'";
				}
				
			}
			
			//ORIGINAL
//			extcond = " group by a.PONO,CustName,recvdate,consignment_gst,a.crby,b.status ";
			
			//REPLICATING 
//			extcond = " group by a.PONO,c.CustName,recvdate,consignment_gst,a.crby,c.status ";
			
			//NEW ONE
			extcond = " group by a.PONO,CustName,recvdate,consignment_gst,a.crby,b.status ";


			StringBuffer sqlConsignment = new StringBuffer();
			
			sqlConsignment.append("select distinct A.PONO pono,custname,recvdate,ISNULL(consignment_gst,'')consignment_gst,ISNULL(a.crby,'')issuedby,ISNULL(b.STATUS,'') as status,"
					+" isnull(SUM(RECQTY*UNITCOST),0) subtotal,isnull(((SUM(RECQTY*UNITCOST)*consignment_gst)/100),0) taxval"
				+" from "
				+ "["
				+ plant
				+ "_"
				+ "recvdet] a, "
				+ "["
				+ plant
				+ "_"
				+ "tohdr] b	where a.PONO=b.TONO AND A.RECEIVESTATUS='C'" 
				+ sCondition+sCondition1+extcond );
			
//			select distinct A.PONO pono,custname,recvdate,ISNULL(consignment_gst,'')consignment_gst,ISNULL(a.crby,'')issuedby,ISNULL(b.STATUS,'') as status, 
//			isnull(SUM(RECQTY*UNITPRICE),0) subtotal,isnull(((SUM(RECQTY*UNITPRICE)*consignment_gst)/100),0) taxval 
//			from [TEST_recvdet] a, [TEST_tohdr] b, [test_TODET] C
//			where a.PONO=b.TONO AND B.STATUS='C' AND  SUBSTRING(a.recvdate, 7, 4) + SUBSTRING(a.recvdate, 4, 2) + SUBSTRING(a.recvdate, 1, 2) >= '20210111'   
//			group by a.PONO,CustName,recvdate,consignment_gst,a.crby,b.status
			
			//REPLICATING 4 TABLES
//			sql_PO.append("select distinct A.PONO pono,custname,recvdate,ISNULL(consignment_gst,'')consignment_gst,ISNULL(a.crby,'')issuedby,ISNULL(c.STATUS,'') as status,"
//					+" isnull(SUM(RECQTY*UNITPRICE),0) subtotal,isnull(((SUM(RECQTY*UNITPRICE)*consignment_gst)/100),0) taxval"
//							+" from "
//					    	+ "["
//							+ plant
//							+ "_"
//							+ "recvdet] a, "
//							+ "["
//							+ plant
//							+ "_"
//							+ "todet] b, "
//							+ "["
//							+ plant
//							+ "_"
//							+ "tohdr] c	where a.PONO=b.TONO AND C.STATUS='C'" 
//							+ sCondition+sCondition1+extcond );
			
			
			//original QUERY
//			sql_PO.append("select distinct A.PONO pono,custname,recvdate,ISNULL(consignment_gst,'')consignment_gst,ISNULL(a.crby,'')issuedby,ISNULL(b.STATUS,'') as status,"
//							+" isnull(SUM(RECQTY*UNITCOST),0) subtotal,isnull(((SUM(RECQTY*UNITCOST)*consignment_gst)/100),0) taxval"
//						+" from "
//						+ "["
//						+ plant
//						+ "_"
//						+ "recvdet] a, "
//						+ "["
//						+ plant
//						+ "_"
//						+ "tohdr] b where a.PONO=b.TONO AND B.STATUS='C'" 
//						+ sCondition+sCondition1+extcond );
			
			
			ht= new Hashtable();
			extcond="";
        	arrList = movHisDAO.selectForReport(sqlConsignment.toString(), ht,extcond);
	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getConsignmentSummary:", e);
		}
		return arrList;
	}
//imthi end from consignmentwithprice line 295     

    //START : By Samatha on 23/4/2014
  	public ArrayList getPrdRecvIssueDetails(String aPlant, String aItem,String afrmDate,String atoDate) throws Exception {
  		ArrayList arrList = new ArrayList();
  		Hashtable ht =new Hashtable ();
  		
  		    String sCondition=""; 
  		    String sDtCond="",sAfterDtCond=""; 
  	          
  		  if (afrmDate.length() > 0) {

		    	sDtCond = sDtCond + " AND   REPLACE(TRANDATE,'-','') >= '"+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + " AND  REPLACE(TRANDATE,'-','')<= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + "   REPLACE(TRANDATE,'-','') <= '" + atoDate + "'  ";
				}
			}
		    if (atoDate.length() > 0) {
		    	sAfterDtCond = sAfterDtCond + " AND  REPLACE(TRANDATE,'-','') > '" + atoDate + "'  ";
			}
  		        
  			try {
  				InvMstDAO _InvMstDAO = new InvMstDAO();
  				_InvMstDAO.setmLogger(mLogger);
  					
  				String aQuery ="  SELECT 'RECV' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "]  where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and DIRTYPE in ('MISC_RECV','ORD_RECV','REPORTING') union all "
  						+"  SELECT 'RECV' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','REPORTING_REV') union all "
  						+"  SELECT 'ISSUE' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') union all "
  						+"  SELECT 'ISSUE' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','WIP_REVERSE') union all "
  						+"  SELECT 'RECVED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and DIRTYPE in ('MISC_RECV','ORD_RECV','REPORTING') union all "
  						+"  SELECT 'RECVED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','REPORTING_REV') union all "
  						+"  SELECT 'ISSUED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP')  "
  				        +"  SELECT 'ISSUED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','WIP_REVERSE') ";
  					
  				arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

  			} catch (Exception e) {
  				this.mLogger.exception(this.printLog, "", e);
  			}
  			return arrList;
  		}
  	 //END : By Samatha on 23/4/2014


	public ArrayList getPrdRecvIssueDetails(String aPlant, String aItem,String afrmDate,String atoDate,String loc) throws Exception {
  		ArrayList arrList = new ArrayList();
  		Hashtable ht =new Hashtable ();
  		
  		    String sCondition=""; 
  		    String sDtCond="",sAfterDtCond=""; 
  	          
  		  if (afrmDate.length() > 0) {

		    	sDtCond = sDtCond + " AND   REPLACE(TRANDATE,'-','') >= '"+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + " AND  REPLACE(TRANDATE,'-','')<= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + "   REPLACE(TRANDATE,'-','') <= '" + atoDate + "'  ";
				}
			}
		    if (atoDate.length() > 0) {
		    	sAfterDtCond = sAfterDtCond + " AND  REPLACE(TRANDATE,'-','') > '" + atoDate + "'  ";
			}
		    
  		        
  			try {
  				InvMstDAO _InvMstDAO = new InvMstDAO();
  				_InvMstDAO.setmLogger(mLogger);
  					
  			/*	String aQuery ="  SELECT 'RECV' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "]  where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and  DIRTYPE IN ('ORD_RECV','MISC_RECV' ,'LT_TRAN_IN','OB_TRANSFER_PIC_IN','PIC_TO_IN','PIC_TRAN_IN','TO_RECV','KIT_PARENT','LOAN_PICK_IN') and MOVTID='IN' union all "
  						+"  SELECT 'RECV' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV') union all "
  						+"  SELECT 'ISSUE' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and  DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','OB_REVERSE')  and MOVTID ='OUT'  union all "
  						+"  SELECT 'ISSUE' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,SUBSTRING(CAST(QTY AS VARCHAR),2,LEN(QTY)-1)  AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and  DIRTYPE  in ('LOAN_RECV_OUT','LT_TRAN_OUT','OB_TRANSFER_PIC_OUT','PIC_TO_OUT','PIC_TRAN_OUT','TO_REVERSE','LOAN_RECV_OUT','OB_REVERSE_OUT') and MOVTID ='OUT'  union all "
  						+"  SELECT 'ISSUE' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sDtCond+" and DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','LOAN_RECV_IN','DEKITTING','TO_REVERSE','OB_REVERSE') and MOVTID ='IN' union all "
  						+"  SELECT 'RECVED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+"  and DIRTYPE IN ('ORD_RECV','MISC_RECV' ,'LT_TRAN_IN','OB_TRANSFER_PIC_IN','PIC_TO_IN','PIC_TRAN_IN','TO_RECV','KIT_PARENT','LOAN_PICK_IN') and MOVTID='IN'  union all "
  						+"  SELECT 'RECVED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV') union all "
  						+"  SELECT 'ISSUED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','OB_REVERSE')  and MOVTID ='OUT'  union all "
  						+"  SELECT 'ISSUED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,SUBSTRING(CAST(QTY AS VARCHAR),2,LEN(QTY)-1)  AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and    DIRTYPE  in ('LOAN_RECV_OUT','LT_TRAN_OUT','OB_TRANSFER_PIC_OUT','PIC_TO_OUT','PIC_TRAN_OUT','TO_REVERSE','LOAN_RECV_OUT','OB_REVERSE_OUT') and MOVTID ='OUT'   union all "
  						+"  SELECT 'ISSUED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,SUBSTRING(crat,7,2)+'/'+ ISNULL(SUBSTRING(crat,5,2)+'/'+ SUBSTRING(crat,1,4),'')  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+" and DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','LOAN_RECV_IN','DEKITTING','TO_REVERSE','OB_REVERSE') and MOVTID ='IN' ";
  				*/	
  				
  				
  				String aQuery ="  SELECT 'RECV' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "]  where PLANT='"+aPlant+"' and ITEM ='"+aItem+"' and loc like '"+loc+"%'  "+sDtCond+" and   DIRTYPE in ('ORD_RECV','MISC_RECV','KIT_PARENT','REPORTING') and MOVTID='IN' union all "
  						//+" SELECT * FROM ( SELECT top 1 'RECV' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,'' AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sDtCond+" and DIRTYPE in ('CNT_INV_UPLOAD')  ORDER BY CRAT DESC ) A union all "
  						+"  SELECT 'RECV' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sDtCond+" and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV','REPORTING_REV') union all "
  						+"  SELECT 'TRANSFER IN' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sDtCond+" and  DIRTYPE in ('LT_TRAN_IN','LOAN_PICK_IN','PIC_TO_IN','LOAN_RECV_IN','TO_REVERSE','TO_RECV','PIC_TRAN_IN','OB_REVERSE','OB_TRANSFER_PIC_IN','OBISSUE_REVERSE')  and MOVTID='IN' union all "
  						+"  SELECT 'TRANSFER OUT' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sDtCond+" and  DIRTYPE in ('LT_TRAN_OUT','LOAN_RECV_OUT','PIC_TO_OUT','TO_REVERSE','PIC_TRAN_OUT','OB_REVERSE_OUT','OB_REVERSE','OB_TRANSFER_PIC_OUT') and MOVTID ='OUT'  union all "
  						+"  SELECT 'ISSUE' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sDtCond+" and  DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') and MOVTID ='OUT'  union all "
  						+"  SELECT 'ISSUE' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%'  "+sDtCond+" and  DIRTYPE in ('POS_REFUND','DEKITTING','WIP_REVERSE') and MOVTID ='IN' union all "
  						
  						+"  SELECT 'RECVED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "]  where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%'  "+sAfterDtCond+"   and DIRTYPE in ('ORD_RECV','MISC_RECV','KIT_PARENT','REPORTING') and MOVTID='IN' union all "
  						//+"  SELECT * FROM (  SELECT top 1 'RECVED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,'' AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sAfterDtCond+" and DIRTYPE in ('CNT_INV_UPLOAD')  ORDER BY CRAT DESC ) A union all "
  						+"  SELECT 'RECVED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sAfterDtCond+" and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV','REPORTING_REV') union all "
  						+"  SELECT 'TRANSFER IN_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_POHDR] WHERE PLANT='"+aPlant+"' and PONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sAfterDtCond+" and  DIRTYPE in ('LT_TRAN_IN','LOAN_PICK_IN','PIC_TO_IN','LOAN_RECV_IN','TO_REVERSE','TO_RECV','PIC_TRAN_IN','OB_REVERSE','OB_TRANSFER_PIC_IN','OBISSUE_REVERSE')  and MOVTID='IN' union all "
  						+"  SELECT 'TRANSFER OUT_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  "+sAfterDtCond+"  and loc like '"+loc+"%' and  DIRTYPE in ('LT_TRAN_OUT','LOAN_RECV_OUT','PIC_TO_OUT','TO_REVERSE','PIC_TRAN_OUT','OB_REVERSE_OUT','OB_REVERSE','OB_TRANSFER_PIC_OUT') and MOVTID ='OUT'  union all "
  						+"  SELECT 'ISSUED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'   and loc like '"+loc+"%' "+sAfterDtCond+" and  DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') and MOVTID ='OUT'  union all "
  						+"  SELECT 'ISSUED_AFTER' As TRANTYPE,DIRTYPE,ISNULL(ORDNUM,'') AS ORDNO,ISNULL((SELECT CUSTNAME from ["+aPlant+"_DOHDR] WHERE PLANT='"+aPlant+"' and DONO=ORDNUM ),'') AS CNAME,ITEM,(SELECT ITEMDESC FROM ["+aPlant+"_ITEMMST] WHERE PLANT='"+aPlant+"' AND ITEM ='"+aItem+"') AS ITEMDESC,BATNO AS BATCH,'-'+CAST( QTY AS VARCHAR) AS QTY,LOC,ISNULL(REMARKS,'') as REMARK ,TRANDATE  AS TRANDATE,CRBY   from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT='"+aPlant+"' and ITEM ='"+aItem+"'  and loc like '"+loc+"%' "+sAfterDtCond+" and  DIRTYPE in ('POS_REFUND','DEKITTING','WIP_REVERSE') and MOVTID ='IN'  ";
  					
  				arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

  			} catch (Exception e) {
  				this.mLogger.exception(this.printLog, "", e);
  			}
  			return arrList;
  		}

  
  //---Added by Bruhan on April 23 2014, Description:To get Item for Label Print Goods Issue summary   
    public ArrayList getGoodsIssuePrintItemList(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant,String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			  if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(crat,1,8) >= '"
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(crat,1,8)<= '" 
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(a.crat,1,8) <= '"
							+ atoDate + "'  ";
				}
			}
			
			String extraCond=	sCondition	+ " ";
			StringBuffer sql_DO = new StringBuffer();
			sql_DO
					.append("select distinct item from "
							+ "["
							+ plant
							+ "_"
							+ "shiphis] a "
							+"where dono<>'' "
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_DO.append(" AND ITEM = '" + ht.get("ITEM") + "'");
				}
				if (ht.get("CNAME") != null) {
					sql_DO.append(" AND CNAME LIKE '%" + ht.get("CNAME") + "%'");
				}
				if (ht.get("DONO") != null) {
					sql_DO.append(" AND DONO = '" + ht.get("DONO") + "'");
				}
				if (ht.get("BATCH") != null) {
					sql_DO.append(" AND BATCH = '" + ht.get("BATCH") + "'");
				}
				if (ht.get("ITEMDESC") != null) {
					sql_DO.append(" AND ITEMDESC = '" + ht.get("ITEMDESC") + "'");
				}
				if (ht.get("ORDERTYPE") != null) {
					sql_DO.append(" AND DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  DONO=a.DONO)");
							
				}
				if (ht.get("PRD_CLS_ID") != null) {
					sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
				
				if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
					sql_DO.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' and REMARK IS NOT NULL AND REMARK <> ''");
							
				}
				
				if (ht.get("LOC") != null) {
					sql_DO.append(" AND LOC LIKE '%" + ht.get("LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_DO.append("   AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
	
					
			}

			StringBuffer sql_Loan = new StringBuffer();
			sql_Loan
					.append("select distinct item  "
							+ " from "
							+ "["
							+ plant
							+ "_"
							+ "loan_pick] a "
							+" where ordno<>'' "
							+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_Loan.append(" AND ITEM = '" + ht.get("ITEM") + "'");
				}
				if (ht.get("CNAME_LON") != null) {
					sql_Loan.append(" AND CNAME LIKE '%" + ht.get("CNAME_LON")
							+ "%'");
				}
				if (ht.get("DONO") != null) {
					sql_Loan
							.append(" AND ORDNO = '" + ht.get("DONO") + "'");
				}
				if (ht.get("BATCH") != null) {
					sql_Loan.append(" AND BATCH = '" + ht.get("BATCH")
							+ "'");
				}
				if (ht.get("ITEMDESC") != null) {
					sql_Loan.append(" AND ITEMDESC = '"
							+ ht.get("ITEMDESC") + "'");
				}
				
				if (ht.get("PRD_CLS_ID") != null) {
					sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
				
				
				if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
					sql_Loan.append(" AND SUBSTRING(REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' and REMARK IS NOT NULL AND REMARK <> ''");
							
				}
				if (ht.get("LOC") != null) {
					sql_Loan.append(" AND LOC LIKE '%" + ht.get("LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Loan.append("   AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
			}

			StringBuffer sql_Transfer = new StringBuffer();
			sql_Transfer
					.append("select distinct item  "
							+ " from "
							+ "["
							+ plant
							+ "_"
							+ "to_pick] a "
							+" where tono<>''  "
							+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_Transfer.append(" AND ITEM = '" + ht.get("ITEM")
							+ "'");
				}
				if (ht.get("CNAME_TO") != null) {
					sql_Transfer.append(" AND CNAME LIKE '%"
							+ ht.get("CNAME_TO") + "%'");
				}
				if (ht.get("DONO") != null) {
					sql_Transfer.append(" AND TONO = '" + ht.get("DONO")
							+ "'");
				}
				if (ht.get("BATCH") != null) {
					sql_Transfer.append(" AND BATCH = '" + ht.get("BATCH")
							+ "'");
				}
				if (ht.get("ITEMDESC") != null) {
					sql_Transfer.append(" AND ITEMDESC = '"
							+ ht.get("ITEMDESC") + "'");
				}
				if (ht.get("PRD_CLS_ID") != null) {
					sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
				
				if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
					sql_Transfer.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' and REMARK IS NOT NULL AND REMARK <> ''");
							
				}
				
				if (ht.get("LOC") != null) {
					sql_Transfer.append(" AND LOC LIKE '%" + ht.get("LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Transfer.append("   AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				
			}
			StringBuffer sql_Miscissue = new StringBuffer();
			sql_Miscissue
					.append("select distinct item  from "
							
							+ "["
							+ plant
							+ "_"
							+ "shiphis] a "
 							+" where a.TRAN_TYPE='GOODSISSUE' "
							+ sCondition);
			
			
			if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_Miscissue.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
				}
				if (ht.get("CNAME") != null) {
					sql_Miscissue.append(" AND CNAME LIKE '%" + ht.get("CNAME") + "%'");
				}
				if (ht.get("DONO") != null) {
					sql_Miscissue.append(" AND DONO = '" + ht.get("DONO") + "'");
				}
				if (ht.get("BATCH") != null) {
					sql_Miscissue.append(" AND BATCH = '" + ht.get("BATCH") + "'");
				}
				if (ht.get("ITEMDESC") != null) {
					sql_Miscissue.append(" AND ITEMDESC = '" + ht.get("ITEMDESC")
							+ "'");
				}
				if (ht.get("PRD_CLS_ID") != null) {
					sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
				
				if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
					sql_Miscissue.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' and REMARK IS NOT NULL AND REMARK <> ''");
							
				}
				

				if (ht.get("LOC") != null) {
					sql_Miscissue.append(" AND LOC LIKE '%" + ht.get("LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Miscissue.append("  AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
			}
			
			
			Integer sqlFinal = 0;
			if (ht.get("CNAME") != null) {
				if (ht.get("CNAME_LON") != null) {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 1;
					} else {
						sqlFinal = 2;
					}
				} else {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 3;
					} else {
						sqlFinal = 4;
					}
				}
			} else {
				if (ht.get("CNAME_LON") != null) {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 5;
					} else {
						sqlFinal = 6;
					}
				} else {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 7;
					} else {
						sqlFinal = 1;
					}
				}
			}
			StringBuffer sql_Final = new StringBuffer();
			switch (sqlFinal) {
			case 1: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Loan.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Miscissue.toString());
				
				
			}
				break;
			case 2: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Loan.toString());
				
			}
				break;
			case 3: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
				
			}
				break;
			case 4: {
				sql_Final.append(sql_DO.toString());
				
			}
				break;
			case 5: {
				sql_Final.append(sql_Loan.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
				
			}
				break;
			case 6: {
				sql_Final.append(sql_Loan.toString());
				
			}
				break;
			case 7: {
				sql_Final.append(sql_Transfer.toString());
				
			}
				break;
			}
			//sql_Final.append("order by a.item ");
           
			if (dirType.equalsIgnoreCase("GOODS ISSUE PRINT")) {

				arrList = movHisDAO.selectForReport(sql_Final.toString(),
						new Hashtable<String, String>());
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsIssuePrintItemList:", e);
		}
		return arrList;
	}

    //---End Added by Bruhan on April 23 2014, Description:To get Item for Label Print Goods Issue summary   

    /*---Added by Bruhan on April 23 2014, Description:For Label Print Goods Issue summary
    *  ************Modification History*********************************
    * Sep 18 2014, Description: To include Print Status
    */
    public ArrayList getGoodsIssuePrintingList(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant,String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(crat,1,8) >= '"// 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(crat,1,8)<= '"
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			}
			
		
//		String extraCond	=sCondition	+ "AND item = item AND loc = LOC AND USERFLD4 = BATCH ";
		StringBuffer sql_DO = new StringBuffer();
		sql_DO.append("select dono,cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) issueqty, "
						+ "isnull(upper(a.loc),0) loc,isnull(status,'') status, " + 
						" isnull((Select STKUOM from ["+plant+"_itemmst]  where  ITEM=a.item ),'') uom,isnull(printstatus,'N') printstatus " 
						+"from "
							+ "["
						+ plant
						+ "_"
						+ "shiphis] a "
						+"  where dono<>'' "
						+ sCondition);
		if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_DO.append(" AND ITEM = '" + ht.get("ITEM") + "'");
				}
				if (ht.get("CNAME") != null) {
					sql_DO.append(" AND CNAME LIKE '%" + ht.get("CNAME") + "%'");
				}
				if (ht.get("DONO") != null) {
					sql_DO.append(" AND DONO = '" + ht.get("DONO") + "'");
				}
				if (ht.get("BATCH") != null) {
					sql_DO.append(" AND BATCH = '" + ht.get("BATCH") + "'");
				}
				if (ht.get("ITEMDESC") != null) {
					sql_DO.append(" AND ITEMDESC = '" + ht.get("ITEMDESC")
							+ "'");
				}
				if (ht.get("ORDERTYPE") != null) {
					sql_DO.append(" AND DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  DONO=a.DONO)");
							
				}
				
				if (ht.get("PRD_CLS_ID") != null) {
					sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				
				if (ht.get("ITEMTYPE") != null) {
					sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
			
				if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
					sql_DO.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
					
				}
				if (ht.get("LOC") != null) {
					sql_DO.append(" AND LOC LIKE '%" + ht.get("LOC")
						+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_DO.append("  AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("PRINTSTATUS") != null) {
					
					if((ht.get("PRINTSTATUS").equals("N"))){
							sql_DO.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
					}
					else if((ht.get("PRINTSTATUS").equals("C"))){
						sql_DO.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
					}
				}
			
			
			}
		
		sql_DO.append(" group by dono,item,itemdesc,loc,batch,cname,status,printstatus"); 	

		StringBuffer sql_Loan = new StringBuffer();
		sql_Loan
				.append("select ordno as dono,cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) issueqty,isnull(upper(loc),0) loc,isnull(status,'') status, "
						+ " isnull((Select STKUOM from ["+plant+"_itemmst] where  ITEM=a.item ),'') uom,isnull(printstatus,'N') printstatus from "
						+ "["
						+ plant
						+ "_"
						+ "loan_pick] a "
						
						+" Where ordno<>'' "
						+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_Loan.append(" AND ITEM = '" + ht.get("ITEM") + "'");
				}
				if (ht.get("CNAME_LON") != null) {
					sql_Loan.append(" AND CNAME LIKE '%" + ht.get("CNAME_LON")
							+ "%'");
				}
				if (ht.get("DONO") != null) {
					sql_Loan
							.append(" AND ORDNO = '" + ht.get("DONO") + "'");
				}
				if (ht.get("BATCH") != null) {
					sql_Loan.append(" AND BATCH = '" + ht.get("BATCH")
							+ "'");
				}
				if (ht.get("ITEMDESC") != null) {
					sql_Loan.append(" AND ITEMDESC = '"
							+ ht.get("ITEMDESC") + "'");
				}
				
				if (ht.get("PRD_CLS_ID") != null) {
					sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				
				if (ht.get("ITEMTYPE") != null) {
					sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
			
				if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
					sql_Loan.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
					
				}
			
				if (ht.get("LOC") != null) {
					sql_Loan.append(" AND LOC LIKE '%" + ht.get("LOC")
						+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Loan.append("  AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
					
				if (ht.get("PRINTSTATUS") != null) {
					
					if((ht.get("PRINTSTATUS").equals("N"))){
							sql_Loan.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
					}
					else if((ht.get("PRINTSTATUS").equals("C"))){
						sql_Loan.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
					}
				}
		
	    }
			sql_Loan.append(" group by ordno,item,itemdesc,loc,batch,cname,status,printstatus"); 	 	
			
			StringBuffer sql_Transfer = new StringBuffer();
			sql_Transfer.append("select distinct tono as dono,cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) issueqty,isnull(upper(a.loc),0) loc,isnull(status,'') status,"
						+ " isnull((Select STKUOM from ["+plant+"_itemmst]  where  ITEM = a.item),'') uom,isnull(printstatus,'N') printstatus from "
						+ "["
						+ plant
						+ "_"
						+ "to_pick] a "
						+" where a.tono<>'' "
						+ sCondition);
			if (ht.size() > 0) {
			if (ht.get("ITEM") != null) {
				sql_Transfer.append(" AND ITEM = '" + ht.get("ITEM")
						+ "'");
			}
			if (ht.get("CNAME_TO") != null) {
				sql_Transfer.append(" AND CNAME LIKE '%"
						+ ht.get("CNAME_TO") + "%'");
			}
			if (ht.get("DONO") != null) {
				sql_Transfer.append(" AND TONO = '" + ht.get("DONO")
						+ "'");
			}
			if (ht.get("BATCH") != null) {
				sql_Transfer.append(" AND BATCH = '" + ht.get("BATCH")
						+ "'");
			}
			if (ht.get("ITEMDESC") != null) {
				sql_Transfer.append(" AND ITEMDESC = '"+ ht.get("ITEMDESC") + "'");
			}
			
			if (ht.get("PRD_CLS_ID") != null) {
				sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
			}
			if (ht.get("PRD_BRAND_ID") != null) {
				sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
			}
			
			if (ht.get("ITEMTYPE") != null) {
				sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
			}	
			
			if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
				sql_Transfer.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
					
			}
			
			if (ht.get("LOC") != null) {
				sql_Transfer.append(" AND LOC LIKE '%" + ht.get("LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Transfer.append("  AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			
			if (ht.get("PRINTSTATUS") != null) {
				
				if((ht.get("PRINTSTATUS").equals("N"))){
						sql_Transfer.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
				}
				else if((ht.get("PRINTSTATUS").equals("C"))){
					sql_Transfer.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
				}
			}
			
		
		}
			
		sql_Transfer.append(" group by tono,item,itemdesc,loc,batch,cname,status,printstatus"); 
			
			
		StringBuffer sql_Miscissue = new StringBuffer();
		sql_Miscissue.append("select dono,'' as cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) as issueqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status," 
						+ " isnull((Select STKUOM from ["+plant+"_itemmst]  where  ITEM=a.item ),'') uom,isnull(printstatus,'N') printstatus  "
						+"  from "		
						+ "["
						+ plant
						+ "_"
						+ "shiphis] a "
						+" where dono='GOODSISSUE'"
						+ sCondition);
		
		if (ht.size() > 0) {
			if (ht.get("ITEM") != null) {
				sql_Miscissue.append(" AND ITEM = '" + ht.get("ITEM") + "'");
			}
			if (ht.get("CNAME") != null) {
				sql_Miscissue.append(" AND CNAME LIKE '%" + ht.get("CNAME") + "%'");
			}
			
			
			if (ht.get("DONO") != null) {
				sql_Miscissue.append(" AND DONO = '" + ht.get("DONO") + "'");
			}
			if (ht.get("BATCH") != null) {
				sql_Miscissue.append(" AND BATCH = '" + ht.get("BATCH") + "'");
			}
			if (ht.get("ITEMDESC") != null) {
				sql_Miscissue.append(" AND ITEMDESC = '" + ht.get("ITEMDESC")
						+ "'");
			}
			if (ht.get("PRD_CLS_ID") != null) {
				sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
			}
			if (ht.get("PRD_BRAND_ID") != null) {
				sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
			}
			
			if (ht.get("ITEMTYPE") != null) {
				sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
			}	
			if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
				sql_Miscissue.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
					
			}
			if (ht.get("LOC") != null) {
				sql_Miscissue.append(" AND LOC LIKE '%" + ht.get("LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Miscissue.append("   AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			
			if (ht.get("PRINTSTATUS") != null) {
				if((ht.get("PRINTSTATUS").equals("N"))){
							sql_Miscissue.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
					}
					else if((ht.get("PRINTSTATUS").equals("C"))){
						sql_Miscissue.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
					}
				}
		}
		   sql_Miscissue.append(" group by dono,item,itemdesc,loc,batch,cname,status,printstatus"); 	 	 	
		
			Integer sqlFinal = 0;
			if (ht.get("CNAME") != null) {
				if (ht.get("CNAME_LON") != null) {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 1;
					} else {
						sqlFinal = 2;
					}
				} else {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 3;
					} else {
						sqlFinal = 4;
					}
				}
			} else {
				if (ht.get("CNAME_LON") != null) {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 5;
					} else {
						sqlFinal = 6;
					}
				} else {
					if (ht.get("CNAME_TO") != null) {
						sqlFinal = 7;
					} else {
						sqlFinal = 1;
					}
				}
			}
			StringBuffer sql_Final = new StringBuffer();
			switch (sqlFinal) {
			case 1: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Loan.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Miscissue.toString());
			}
				break;
			case 2: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Loan.toString());
			}
				break;
			case 3: {
				sql_Final.append(sql_DO.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
			}
				break;
			case 4: {
				sql_Final.append(sql_DO.toString());
			}
				break;
			case 5: {
				sql_Final.append(sql_Loan.toString());
				sql_Final.append(" UNION  ");
				sql_Final.append(sql_Transfer.toString());
			}
				break;
			case 6: {
				sql_Final.append(sql_Loan.toString());
			}
				break;
			case 7: {
				sql_Final.append(sql_Transfer.toString());
			}
				break;
			}
		sql_Final.append(" order by a.item");
			System.out.println(sql_Final.toString());
			if (dirType.equalsIgnoreCase("GOODS ISSUE PRINT")) {

				arrList = movHisDAO.selectForReport(sql_Final.toString(),
						new Hashtable<String, String>());
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsIssuePrintingList:", e);
		}
		return arrList;
	}
// //---End Added by Bruhan on April 23 2014, Description:For Label Print Goods Issue summary 
    

   /*-Added by Bruhan on June 10 2014, Description:To Label Print Listing with type
    *  ************Modification History*********************************
    * Sep 18 2014, Description: To include Print Status
    */
    public ArrayList getGoodsIssuePrintingListWithType(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant,String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
			if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(crat,1,8) >= '"// 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(crat,1,8)<= '"
							+ atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			}
			
			if(dirType.equals("ISSUE")){
//				String extraCond	=sCondition	+ "AND item = item AND loc = LOC AND USERFLD4 = BATCH ";
				StringBuffer sql_DO = new StringBuffer();
				sql_DO.append("select dono,cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) issueqty, "
								+ "isnull(upper(a.loc),0) loc,isnull(status,'') status, " + 
								" isnull((Select STKUOM from ["+plant+"_itemmst]  where  ITEM=a.item ),'') uom,isnull(printstatus,'N') printstatus " 
								+"from "
									+ "["
								+ plant
								+ "_"
								+ "shiphis] a "
								+"  where dono<>'' and dono <>'GOODSISSUE'"
								+ sCondition);
				if (ht.size() > 0) {
						if (ht.get("ITEM") != null) {
							sql_DO.append(" AND ITEM = '" + ht.get("ITEM") + "'");
						}
						if (ht.get("CNAME") != null) {
							sql_DO.append(" AND CNAME LIKE '%" + ht.get("CNAME") + "%'");
						}
						if (ht.get("DONO") != null) {
							sql_DO.append(" AND DONO = '" + ht.get("DONO") + "'");
						}
						if (ht.get("BATCH") != null) {
							sql_DO.append(" AND BATCH = '" + ht.get("BATCH") + "'");
						}
						if (ht.get("ITEMDESC") != null) {
							sql_DO.append(" AND ITEMDESC = '" + ht.get("ITEMDESC")
									+ "'");
						}
						if (ht.get("ORDERTYPE") != null) {
							sql_DO.append(" AND DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  DONO=a.DONO)");
									
						}
						
						if (ht.get("PRD_CLS_ID") != null) {
							sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
						}
						if (ht.get("PRD_BRAND_ID") != null) {
							sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
						}
						
						if (ht.get("ITEMTYPE") != null) {
							sql_DO.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
						}	
					
						if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
							sql_DO.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
							
						}
						if (ht.get("LOC") != null) {
							sql_DO.append(" AND LOC LIKE '%" + ht.get("LOC")
								+ "%'"  );
						}
						if (ht.get("LOC_TYPE_ID") != null) {
							sql_DO.append("  AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
						}
						if (ht.get("PRINTSTATUS") != null) {
							
							if((ht.get("PRINTSTATUS").equals("N"))){
									sql_DO.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
							}
							else if((ht.get("PRINTSTATUS").equals("C"))){
								sql_DO.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
							}
						}
					
					}
				
				sql_DO.append(" group by dono,item,itemdesc,loc,batch,cname,status,printstatus"); 		
				sql_DO.append(" order by item");
		 	 	                                                                                                                                         
				
//					Integer sqlFinal = 0;
					if (ht.get("CNAME") != null) {
						if (ht.get("CNAME_LON") != null) {
							if (ht.get("CNAME_TO") != null) {
//								sqlFinal = 1;
							} else {
//								sqlFinal = 2;
							}
						} else {
							if (ht.get("CNAME_TO") != null) {
//								sqlFinal = 3;
							} else {
//								sqlFinal = 4;
							}
						}
					} else {
						if (ht.get("CNAME_LON") != null) {
							if (ht.get("CNAME_TO") != null) {
//								sqlFinal = 5;
							} else {
//								sqlFinal = 6;
							}
						} else {
							if (ht.get("CNAME_TO") != null) {
//								sqlFinal = 7;
							} else {
//								sqlFinal = 1;
							}
						}
					}
					
					//if (dirType.equalsIgnoreCase("GOODS ISSUE PRINT")) {
		
						arrList = movHisDAO.selectForReport(sql_DO.toString(),
								new Hashtable<String, String>());
					//}
				} // ISSUE END
			//Loan Start 
			if(dirType.equals("LOAN")){
//				String extraCond	=sCondition	+ "AND item = item AND loc = LOC AND USERFLD4 = BATCH ";
				StringBuffer sql_Loan = new StringBuffer();
				sql_Loan
						.append("select ordno as dono,cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) issueqty,isnull(upper(loc),0) loc,isnull(status,'') status, "
								+ " isnull((Select STKUOM from ["+plant+"_itemmst] where  ITEM=a.item ),'') uom,isnull(printstatus,'N') printstatus from "
								+ "["
								+ plant
								+ "_"
								+ "loan_pick] a "
								
								+" Where ordno<>'' "
								+ sCondition);
					if (ht.size() > 0) {
						if (ht.get("ITEM") != null) {
							sql_Loan.append(" AND ITEM = '" + ht.get("ITEM") + "'");
						}
						if (ht.get("CNAME_LON") != null) {
							sql_Loan.append(" AND CNAME LIKE '%" + ht.get("CNAME_LON")
									+ "%'");
						}
						if (ht.get("DONO") != null) {
							sql_Loan
									.append(" AND ORDNO = '" + ht.get("DONO") + "'");
						}
						if (ht.get("BATCH") != null) {
							sql_Loan.append(" AND BATCH = '" + ht.get("BATCH")
									+ "'");
						}
						if (ht.get("ITEMDESC") != null) {
							sql_Loan.append(" AND ITEMDESC = '"
									+ ht.get("ITEMDESC") + "'");
						}
						
						if (ht.get("PRD_CLS_ID") != null) {
							sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
						}
						
						if (ht.get("PRD_BRAND_ID") != null) {
							sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
						}
						
						if (ht.get("ITEMTYPE") != null) {
							sql_Loan.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
						}	
					
						if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
							sql_Loan.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
							
						}
					
						if (ht.get("LOC") != null) {
							sql_Loan.append(" AND LOC LIKE '%" + ht.get("LOC")
								+ "%'"  );
						}
						if (ht.get("LOC_TYPE_ID") != null) {
							sql_Loan.append("  AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
						}
					    
						if (ht.get("PRINTSTATUS") != null) {
							
							if((ht.get("PRINTSTATUS").equals("N"))){
									sql_Loan.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
							}
							else if((ht.get("PRINTSTATUS").equals("C"))){
								sql_Loan.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
							}
						}
				
			    }
					sql_Loan.append(" group by ordno,item,itemdesc,loc,batch,cname,status,printstatus"); 	
					sql_Loan.append(" order by item");
		 	 	
					arrList = movHisDAO.selectForReport(sql_Loan.toString(),new Hashtable<String, String>());
					
				
			} // Loan End
			
			//Transfer Start
			if(dirType.equals("TRANSFER"))
			{
//				String extraCond	=sCondition	+ "AND item = item AND loc = LOC AND USERFLD4 = BATCH ";
				StringBuffer sql_Transfer = new StringBuffer();
				sql_Transfer.append("select distinct tono as dono,cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) issueqty,isnull(upper(a.loc),0) loc,isnull(status,'') status,"
							+ " isnull((Select STKUOM from ["+plant+"_itemmst]  where  ITEM = a.item),'') uom,isnull(printstatus,'N') printstatus from "
							+ "["
							+ plant
							+ "_"
							+ "to_pick] a "
							+" where a.tono<>'' "
							+ sCondition);
				if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_Transfer.append(" AND ITEM = '" + ht.get("ITEM")
							+ "'");
				}
				if (ht.get("CNAME_TO") != null) {
					sql_Transfer.append(" AND CNAME LIKE '%"
							+ ht.get("CNAME_TO") + "%'");
				}
				if (ht.get("DONO") != null) {
					sql_Transfer.append(" AND TONO = '" + ht.get("DONO")
							+ "'");
				}
				if (ht.get("BATCH") != null) {
					sql_Transfer.append(" AND BATCH = '" + ht.get("BATCH")
							+ "'");
				}
				if (ht.get("ITEMDESC") != null) {
					sql_Transfer.append(" AND ITEMDESC = '"+ ht.get("ITEMDESC") + "'");
				}
				
				if (ht.get("PRD_CLS_ID") != null) {
					sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				
				if (ht.get("ITEMTYPE") != null) {
					sql_Transfer.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
				
				if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
					sql_Transfer.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
						
				}
				
				if (ht.get("LOC") != null) {
					sql_Transfer.append(" AND LOC LIKE '%" + ht.get("LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Transfer.append("  AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
					if (ht.get("PRINTSTATUS") != null) {
					
					if((ht.get("PRINTSTATUS").equals("N"))){
							sql_Transfer.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
					}
					else if((ht.get("PRINTSTATUS").equals("C"))){
						sql_Transfer.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
					}
				}
			
			}
				
			sql_Transfer.append(" group by tono,item,itemdesc,loc,batch,cname,status,printstatus"); 
			sql_Transfer.append(" order by item");
			
					
			arrList = movHisDAO.selectForReport(sql_Transfer.toString(),new Hashtable<String, String>());


			}//Transfer End
			
			//Misc Issue Start
			if(dirType.equals("MISC_ISSUE"))
			{
//				String extraCond	=sCondition	+ "AND item = item AND loc = LOC AND USERFLD4 = BATCH ";
				StringBuffer sql_Miscissue = new StringBuffer();
				sql_Miscissue.append("select dono,'' as cname,item,isnull(itemdesc,'') itemdesc,isnull(batch,'') batch,isnull(sum(pickqty),0) as issueqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status," 
								+ " isnull((Select STKUOM from ["+plant+"_itemmst]  where  ITEM=a.item ),'') uom,isnull(printstatus,'N') printstatus  "
								+"  from "		
								+ "["
								+ plant
								+ "_"
								+ "shiphis] a "
								+" where dono='GOODSISSUE'"
								+ sCondition);
				
				if (ht.size() > 0) {
					if (ht.get("ITEM") != null) {
						sql_Miscissue.append(" AND ITEM = '" + ht.get("ITEM") + "'");
					}
					if (ht.get("CNAME") != null) {
						sql_Miscissue.append(" AND CNAME LIKE '%" + ht.get("CNAME") + "%'");
					}
					
					
					if (ht.get("DONO") != null) {
						sql_Miscissue.append(" AND DONO = '" + ht.get("DONO") + "'");
					}
					if (ht.get("BATCH") != null) {
						sql_Miscissue.append(" AND BATCH = '" + ht.get("BATCH") + "'");
					}
					if (ht.get("ITEMDESC") != null) {
						sql_Miscissue.append(" AND ITEMDESC = '" + ht.get("ITEMDESC")
								+ "'");
					}
					if (ht.get("PRD_CLS_ID") != null) {
						sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
					}
					if (ht.get("PRD_BRAND_ID") != null) {
						sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
					}
					
					if (ht.get("ITEMTYPE") != null) {
						sql_Miscissue.append(" AND ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
					}	
					if (ht.get("REMARK") != null && ht.get("REMARK")!="") {
						sql_Miscissue.append(" AND SUBSTRING(REMARK, CHARINDEX(',',REMARK) + 1, LEN(REMARK)) LIKE '%" + ht.get("REMARK")+ "%' AND REMARK IS NOT NULL AND REMARK <> ''");
							
					}
					if (ht.get("LOC") != null) {
						sql_Miscissue.append(" AND LOC LIKE '%" + ht.get("LOC")
								+ "%'"  );
					}
					if (ht.get("LOC_TYPE_ID") != null) {
						sql_Miscissue.append("   AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
					}
					
					if (ht.get("PRINTSTATUS") != null) {
						
						if((ht.get("PRINTSTATUS").equals("N"))){
								sql_Miscissue.append(" AND ISNULL(PRINTSTATUS,'N') <> 'C'");
						}
						else if((ht.get("PRINTSTATUS").equals("C"))){
							sql_Miscissue.append(" AND ISNULL(PRINTSTATUS,'N') = 'C'");
						}
					}
					
				}
				   sql_Miscissue.append(" group by dono,item,itemdesc,loc,batch,cname,status,printstatus");  
				   sql_Miscissue.append(" order by item");
			
				   arrList = movHisDAO.selectForReport(sql_Miscissue.toString(),new Hashtable<String, String>());
						
			}// Misc Issue End
			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsIssuePrintingListDO:", e);
		}
		return arrList;
	
	}
   //---Added by Bruhan on June 10 2014, Description:To Label Print Listing with type 
    
    // Added by radhika for outbound order multiple printout... This method not included in spechut source.
    public ArrayList getDOSummaryToPrint(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String custname,String custtype,String type) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",extraCon="",dtCondStr="",dtcondpick="";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			            if (custname.length()>0){
                         	custname = StrUtils.InsertQuotes(custname);
                         	sCondition =  sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                         }
			 if(type.equalsIgnoreCase("PICKISSUE")){
				 	dtCondStr  = "  and ISNULL(b.issuedate,'')<>'' AND CAST((SUBSTRING(b.issuedate, 7, 4) + '-' + SUBSTRING(b.issuedate, 4, 2) + '-' + SUBSTRING(b.issuedate, 1, 2)) AS date)";
				 	dtcondpick = "  and ISNULL(issuedate,'')<>'' AND CAST((SUBSTRING(issuedate, 7, 4) + '-' + SUBSTRING(issuedate, 4, 2) + '-' + SUBSTRING(issuedate, 1, 2)) AS date)";
			 }
			 else
				 {
				 	dtCondStr =    "  and ISNULL(CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
				 }
                        if (afrmDate.length() > 0) {
                        	sCondition = sCondition +dtCondStr+ " >= '" 
            						+ afrmDate
            						+ "'  ";
                        	dtcondpick = dtcondpick+ " >= '"+ afrmDate+ "'  ";
            				if (atoDate.length() > 0) {
            					sCondition = sCondition + dtCondStr + " <= '" 
            					+ atoDate
            					+ "'  ";
            					dtcondpick = dtcondpick +" AND CAST((SUBSTRING(issuedate, 7, 4) + '-' + SUBSTRING(issuedate, 4, 2) + '-' + SUBSTRING(issuedate, 1, 2)) AS date) <= '"+ atoDate+ "'  "; 	
            				}
            			} else {
            				if (atoDate.length() > 0) {
            					sCondition = sCondition +dtCondStr+ "  <= '" 
            					+ atoDate
            					+ "'  ";
            					dtcondpick = dtcondpick+"  <= '"+ atoDate+ "'  ";
            				}
            			} 
                        
                        if (custtype != null && custtype!="") {
                			sCondition = sCondition + " AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
                		}
                        
                        //This Condition for vision server add by Azees 27.7.20 -Removed 9.1.20 -- Azees
                        //if(dirType.equalsIgnoreCase("OB_PRINT_PRICE"))
                        	//sCondition = sCondition + " AND b.DONO IN (SELECT DONO  FROM ["+plant+"_FININVOICEHDR] WHERE DONO=b.DONO)";
                        
                        if(type.equalsIgnoreCase("PICKISSUE")){
                        	extraCon = "  group by  b.cname,b.dono,a.empno order by b.cname,b.dono ";
                          
                        	StringBuffer sql1 = new StringBuffer(" select b.dono,b.cname as custname,");
                        	sql1.append("(SELECT CUSTCODE  FROM ["+plant+"_DOHDR] WHERE DONO=b.DONO) custcode,");
            				sql1.append("(SELECT ORDERTYPE  FROM ["+plant+"_DOHDR] WHERE DONO=b.DONO) ordertype,");
            				sql1.append("(SELECT JOBNUM  FROM ["+plant+"_DOHDR] WHERE DONO=b.DONO) jobnum,");
            				sql1.append("(SELECT STATUS  FROM ["+plant+"_DOHDR] WHERE DONO=b.DONO) status,");
            				sql1.append("(SELECT ISNULL(STATUS_ID,'') FROM ["+plant+"_DOHDR]  WHERE DONO=b.DONO) status_id,");
            				sql1.append("isnull((select isnull(fname,'') from ["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'')empname,");
            				sql1.append("(SELECT SUM(QTYOR) FROM ["+plant+"_DODET] WHERE  DONO=b.DONO GROUP BY DONO) qtyor,");
            				sql1.append("(SELECT SUM(PICKQTY) FROM ["+plant+"_SHIPHIS] WHERE  DONO=b.DONO"+dtcondpick+" ) qtypick,");
            				sql1.append("isnull(sum(b.PICKQTY),0) qty ");
            				sql1.append(" from " + "[" + plant + "_" + "dohdr" + "] a,");
            				sql1.append( "[" + plant + "_" + "SHIPHIS" + "] b");
            				sql1.append(" where a.dono=b.dono and b.DONO<>'GOODSISSUE' and b.STATUS='C'" + sCondition);
            				arrList = movHisDAO.selectForReport(sql1.toString(), ht,extraCon);
            				
                        }
                        else{
        		            extraCon = "  group by  a.custcode,a.dono,ordertype,a.custname,a.CollectionDate,jobnum,status,status_id,empno order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
                       
        		            StringBuffer sql = new StringBuffer(" select distinct a.custcode,a.dono,isnull(a.ordertype,'') ordertype,a.custname,");
        		            sql.append("a.CollectionDate,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS trandate,isnull(a.jobnum,'') jobnum,a.status,isnull(sum(b.qtyor),0) qtyor," );
        		            sql.append("isnull(sum(b.qtypick),0) qtypick,isnull(sum(b.qtyis),0) qty, ");
        		            sql.append("isnull(a.status_id,'')as status_id,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname ");
        		            sql.append(" from " + "[" + plant + "_" + "dohdr" + "] a,");
        		            sql.append( "[" + plant + "_" + "dodet" + "] b");
        		            sql.append(" where a.dono=b.dono and a.custcode <> '' " + sCondition);
        		            arrList = movHisDAO.selectForReport(sql.toString(), ht,extraCon);
                        }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerDOInvoiceSummary:", e);
		}
		return arrList;
	}
 
    // Added by radhika for best selling customer and product report for out bound... This method not included in spechut source. 
  /*  public ArrayList getbestsellingCustomerProductDOInvoiceSummary(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype,String smrytype) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	        if (custname.length()>0){
                         	custname = StrUtils.InsertQuotes(custname);
                         	sCondition =  sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                         }
	        if (ht.size() > 0) {
				if (ht.get("CUSTTYPE") != null) {
					String custtype = ht.get("CUSTTYPE").toString();
					sCondition= sCondition+" AND custname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
					ht.remove("CUSTTYPE");
				}
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
        		//end 
                String extraCon="";
                /*
                if(smrytype.equalsIgnoreCase("Issue Smry") && sorttype.equalsIgnoreCase("PRODUCT"))
                {       
                	extraCon="GROUP BY b.ITEM,b.ItemDesc ,A.DONO,B.DOLNNO,A.CustCode,a.CustName,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,b.lnstat,"
                  		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.itemtype,a.status_id,OUTBOUND_GST,EMPNO),"
                  		  +"Ranked AS ("
                  		  +"SELECT custcode,dono,dolnno,item,ItemDesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,lnstat,"
                  		  +"qtyor ,qtypick ,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,"
                  		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
                  		  +" from SORTTYPE)"
                  		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
                }
                else if(smrytype.equalsIgnoreCase("Issue Smry") && sorttype.equalsIgnoreCase("CUSTOMER"))
                {
                	extraCon="GROUP BY A.CustCode,a.CustName,A.DONO,B.DOLNNO,b.ITEM,b.ItemDesc,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,b.lnstat,"
                    		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.itemtype,a.status_id,OUTBOUND_GST,EMPNO),"
                    		  +"Ranked AS ("
                    		  +"SELECT custcode,dono,dolnno,item,ItemDesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,lnstat,"
                    		  +"qtyor ,qtypick ,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,"
                    		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
                    		  +" from SORTTYPE)"
                    		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
              
                }
                else if(smrytype.equalsIgnoreCase("Order Smry") && sorttype.equalsIgnoreCase("CUSTOMER"))
                {
                	extraCon="GROUP BY A.CustCode,a.CustName,A.DONO,B.DOLNNO,b.ITEM,b.ItemDesc,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,b.lnstat,"
                    		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.itemtype,a.status_id,OUTBOUND_GST,EMPNO),"
                    		  +"Ranked AS ("
                    		  +"SELECT custcode,dono,dolnno,item,ItemDesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,lnstat,"
                    		  +"qtyor ,qtypick ,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,"
                    		  +"RANK() OVER (PARTITION BY custcode   ORDER BY ordPrice desc) rnk"
                    		  +" from SORTTYPE)"
                    		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY custcode)desc";
              
                }
                else
                {
                	extraCon="GROUP BY b.ITEM,b.ItemDesc ,A.DONO,B.DOLNNO,A.CustCode,a.CustName,a.ORDERTYPE,a.CollectionDate,a.jobnum,a.Remark1,b.lnstat,"
                    		  +"b.CRAT,DELIVERYDATE,TIMESLOTS,c.prd_brand_id,c.prd_cls_id,c.itemtype,a.status_id,OUTBOUND_GST,EMPNO),"
                    		  +"Ranked AS ("
                    		  +"SELECT custcode,dono,dolnno,item,ItemDesc,ordertype,custname,CollectionDate,trandate,jobnum,remarks,lnstat,"
              
                    		  +"qtyor ,qtypick ,qty,unitprice,CRAT,ordPrice,issPrice,deliverydate,deliverytime,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,"
                    		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY ordPrice desc) rnk"
                    		  +" from SORTTYPE)"
                    		  +"SELECT * FROM Ranked ORDER BY sum(ordPrice)  OVER (PARTITION BY ITEM)desc";
               
                }
 
				// Start code modified by radhika for outorderwithprice on 18/12/12 
				StringBuffer sql = new StringBuffer(" WITH SORTTYPE AS( ");
				sql.append("select a.custcode,a.dono,b.dolnno,b.item,b.itemdesc,isnull(a.ordertype,'') ordertype,a.custname,a.CollectionDate," );
				sql.append("CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) AS trandate,");
				sql.append("isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
				sql.append("max(isnull(b.qtyor,0)) qtyor,max(isnull(b.qtypick,0)) qtypick,max(isnull(b.qtyis,0)) qty,max(isnull(b.unitprice,0)) unitprice,b.crat,");
				sql.append("sum(ISNULL(qtyor*b.unitprice,0)) ordPrice,sum(ISNULL(qtyis*b.unitprice,0)) issPrice,isnull(deliverydate,'') deliverydate,");
				sql.append("isnull(timeslots,'') deliverytime,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,");
				sql.append("isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(a.OUTBOUND_GST,0) as Tax,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname ");
				sql.append(" from " + "[" + plant + "_" + "dohdr" + "] a,");
				sql.append( "[" + plant + "_" + "dodet" + "] b,["+ plant + "_" + "ITEMMST]c");
				sql.append(" where a.dono=b.dono and b.ITEM=c.item " + sCondition);
				arrList = movHisDAO.selectForReport(sql.toString(), ht,extraCon);
				// End code modified by radhika for outorderwithprice on 18/12/12 	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerDOInvoiceSummary:", e);
		}
		return arrList;
	} 
   */ 

 public ArrayList getPickingSummarybyfilter(Hashtable ht, String afrmDate,
			String atoDate, String filter, String plant,String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			StringBuffer sql_Final = new StringBuffer();
		
			if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	     if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '" 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		  }
			
	
	     
		String extraCond	=sCondition	+ "AND c.item = a.item AND c.ID = a.ID AND c.USERFLD4 = a.BATCH ";
		if(filter.equalsIgnoreCase("Sales Order")){
				StringBuffer sql_DO = new StringBuffer();
				sql_DO.append("select a.dono,d.TAXID,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,case a.status when 'C' then isnull(a.pickqty,0) "
					    + "else '0' end as issueqty , isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat," 
						+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ "isnull(b.UNITMO,'') as uom," 
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,isnull(d.OUTBOUND_GST,0) as Tax, "
						+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo from "
						+ "["
						+ plant
						+ "_"
						+ "shiphis] a, "
						+ "["
						+ plant
						+ "_"
						+ "dodet] b, ["+plant+"_dohdr] d," 
						+"["+plant+"_ITEMMST] e  where a.dono<>''  and a.dono=b.dono AND a.dolno=b.dolnno AND d.DONO = b.DONO AND a.ITEM=e.ITEM"
						+ sCondition);
		if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_DO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_DO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_DO.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_DO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_DO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("d.ORDERTYPE") != null) {
					sql_DO.append(" AND d.ORDERTYPE = '" + ht.get("d.ORDERTYPE")
							+ "'");
				}
			
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_DO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_DO.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
						+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_DO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_DO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");
			}
			
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_DO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_DO.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}
		
			if (ht.get("INVOICENO") != null) {
				sql_DO.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
			}
			}
		
			sql_Final.append(sql_DO.toString());
		}else if(filter.equalsIgnoreCase("Loan Order")){
				StringBuffer sql_Loan = new StringBuffer();
				sql_Loan.append("select a.ordno as dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') as expiredate," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ " isnull(b.UNITMO,'') as uom,"
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
						+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo  from "
						+ "["
						+ plant
						+ "_"
						+ "loan_pick] a, "
						+ "["
						+ plant
						+ "_"
						+ "loandet] b,  "
						+ "["
						+ plant
						+ "_"
						+ "invmst] c,"
						+"["
						+plant
						+"_ITEMMST] e where a.ordno<>'' and a.ORDLNNO=b.ORDLNNO and a.ordno=b.ordno and  a.item=b.item AND a.ITEM=e.ITEM "
						+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Loan.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME_LON") != null) {
					sql_Loan.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME_LON")
							+ "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Loan
							.append(" AND A.ORDNO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Loan.append(" AND a.BATCH = '" + ht.get("a.BATCH")
							+ "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Loan.append(" AND a.ITEMDESC = '"
							+ ht.get("a.ITEMDESC") + "'");
				}
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_Loan.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_Loan.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_Loan.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
						+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_Loan.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");
			}
			
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_Loan.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_Loan.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}
		
			}
			sql_Final.append(sql_Loan.toString());
		}else if(filter.equalsIgnoreCase("Transfer Order")){
				StringBuffer sql_Transfer = new StringBuffer();
				sql_Transfer.append("select distinct a.tono as dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') expiredate ," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ " isnull(b.UNITMO,'') uom,"
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
						+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo   from "
						+ "["
						+ plant
						+ "_"
						+ "to_pick] a, "
						+ "["
						+ plant
						+ "_"
						+ "todet] b ,  "
						+ "["
						+ plant
						+ "_"
						+ "invmst] c,"
						+"["
						+plant
						+"_ITEMMST] e where a.tono<>'' and a.TOLNO=b.TOLNNO and a.tono=b.tono and a.item=b.item AND a.ITEM=e.ITEM "
						+ extraCond);
		if (ht.size() > 0) {
			if (ht.get("a.ITEM") != null) {
				sql_Transfer.append(" AND A.ITEM = '" + ht.get("a.ITEM")
						+ "'");
			}
			if (ht.get("a.CNAME_TO") != null) {
				sql_Transfer.append(" AND A.CNAME LIKE '%"
						+ ht.get("a.CNAME_TO") + "%'");
			}
			if (ht.get("a.DONO") != null) {
				sql_Transfer.append(" AND A.TONO = '" + ht.get("a.DONO")
						+ "'");
			}
			if (ht.get("a.BATCH") != null) {
				sql_Transfer.append(" AND a.BATCH = '" + ht.get("a.BATCH")
						+ "'");
			}
			if (ht.get("a.ITEMDESC") != null) {
				sql_Transfer.append(" AND a.ITEMDESC = '"+ ht.get("a.ITEMDESC") + "'");
			}
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_Transfer.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_Transfer.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_Transfer.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
						+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_Transfer.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");
			}
			
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_Transfer.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_Transfer.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}
		
					
			}
			sql_Final.append(sql_Transfer.toString());			
			
		}else if(filter.equalsIgnoreCase("Invoice")){
			StringBuffer sql_Invoice = new StringBuffer();
			sql_Invoice.append("SELECT B.INVOICE AS dono,b.TAXID as TAXID, (SELECT CNAME FROM ["+plant+"_CUSTMST] WHERE CUSTNO=B.CUSTNO) AS cname," + 
					"C.ITEM item,isnull((select itemdesc from "+plant+"_ITEMMST where item=C.item ),'') as itemdesc, isnull(a.CRBY,'') as users," + 
					"isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty," + 
					"case a.status when 'C' then isnull(a.pickqty,0) else '0' end as issueqty , " + 
					"isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark," + 
					"a.crat,a.crby,'' AS lnstat, " + 
					"isnull((SELECT TOP 1 expiredate FROM ["+plant+"_INVMST] " + 
					"WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," + 
					"SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," + 
					"ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate, " + 
					"CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate," + 
					"ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') " + 
					"AS toexceltransactiondate," + 
					"isnull(C.UOM,'') as uom,isnull((select prd_brand_id from ["+plant+"_ITEMMST] where item=a.item ),'')  as prd_brand_id," + 
					"isnull((select prd_cls_id from ["+plant+"_ITEMMST] where item=a.item ),'') as prd_cls_id," + 
					"isnull((select PRD_DEPT_ID from ["+plant+"_ITEMMST] where item=a.item ),'') as PRD_DEPT_ID," + 
					"isnull((select itemtype from ["+plant+"_ITEMMST] where item=a.item ),'') as itemtype," + 
					"isnull(B.OUTBOUD_GST,0) as Tax, isnull(a.unitprice,0) unitprice," + 
					"case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate," + 
					"ISNULL((INVOICENO),'') as InvoiceNo  " + 
					"FROM ["+plant+"_SHIPHIS] A JOIN ["+plant+"_FININVOICEHDR] B ON A.INVOICENO = B.GINO " + 
					"JOIN ["+plant+"_FININVOICEDET] C ON B.ID = C.INVOICEHDRID AND A.ITEM=C.ITEM AND A.DOLNO=C.LNNO WHERE B.DONO='' "
							+ sCondition);
			
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Invoice.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_Invoice.append(" AND B.CUSTNO LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Invoice.append(" AND B.INVOICE = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Invoice.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Invoice.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("C.PRD_CLS_ID") != null) {
					sql_Invoice.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
							+ "'");
				}
				if (ht.get("C.PRD_DEPT_ID") != null) {
					sql_Invoice.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
					+ "'");
				}
				if (ht.get("C.PRD_BRAND_ID") != null) {
					sql_Invoice.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
							+ "'");
				}
				if (ht.get("C.ITEMTYPE") != null) {
					sql_Invoice.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
							+ "'");
	
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_Invoice.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
						
				}
				

				if (ht.get("a.LOC") != null) {
					sql_Invoice.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
							+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_Invoice.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_Invoice.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_Invoice.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
				}
				
				if (ht.get("INVOICENO") != null) {
					sql_Invoice.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
				}
			}
			sql_Final.append(sql_Invoice.toString());
		}else{
			
			String TRAN_TYPE="GOODSISSUE";
			if(filter.equalsIgnoreCase("Tax Invoice"))
			  TRAN_TYPE= "TAXINVOICE";
			else if(filter.equalsIgnoreCase("Kitting"))
				  TRAN_TYPE= "KITTING";
			else if(filter.equalsIgnoreCase("De-Kitting"))
				TRAN_TYPE= "DE-KITTING";
			else if(filter.equalsIgnoreCase("Stock Take"))
				TRAN_TYPE= "STOCK_TAKE";
			
			StringBuffer sql_Miscissue = new StringBuffer();
			sql_Miscissue.append("select 0 as TAXID,a.dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.pickqty,0) as issueqty,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,isnull(a.status,'') as lnstat,"  
					    +" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ "CASE WHEN a.TRAN_TYPE='KITTING' THEN isnull((Select TOP 1 CHILDUOM from "+plant+"_SEMI_PROCESSDET where CHILD_PRODUCT=a.item and GINO=a.dono and CHILD_PRODUCT_BATCH=a.batch),'') ELSE isnull((Select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.dono and batch=a.batch and DOLNNO=a.DOLNO),'') END as uom,"
						//+ "isnull((Select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.dono and batch=a.batch and DOLNNO=a.DOLNO),'')as uom,"
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,ISNULL((SELECT TOP 1 ISNULL(OUTBOUND_GST,'') FROM "+plant+"_POSHDR WHERE PLANT=A.PLANT AND POSTRANID=A.DONO),'') as Tax, "	
						+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo  from "
				    	+ "["
						+ plant
						+ "_"
						+ "shiphis] a," 
						+"["
						+plant
						+"_ITEMMST] e where a.TRAN_TYPE like '%"+TRAN_TYPE+"%'  AND a.ITEM=e.ITEM "//and a.item =  '" + ht.get("a.ITEM") + "' AND A.DONO = '" + ht.get("a.DONO") + "' AND    A.CNAME LIKE '" + ht.get("a.CNAME") + "' AND a.BATCH = '" + ht.get("a.BATCH") + "' AND a.ITEMDESC = '" + ht.get("a.ITEMDESC") + "'"
						+ sCondition);
		
		if (ht.size() > 0) {
			if (ht.get("a.ITEM") != null) {
				sql_Miscissue.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
			}
			if (ht.get("a.CNAME") != null) {
				sql_Miscissue.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
			}
			if (ht.get("a.DONO") != null) {
				sql_Miscissue.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
			}
			if (ht.get("a.BATCH") != null) {
				sql_Miscissue.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
			}
			if (ht.get("a.ITEMDESC") != null) {
				sql_Miscissue.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
						+ "'");
			}
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_Miscissue.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_Miscissue.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
						+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_Miscissue.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_Miscissue.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");

			}
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_Miscissue.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_Miscissue.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Miscissue.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}
					
			if (ht.get("INVOICENO") != null) {
				sql_Miscissue.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
			}
		}
						
			sql_Final.append(sql_Miscissue.toString());	
		}
			
			//sql_Final.append("order by a.item,a.issuedate");
			//sql_Final.append("order by a.item, CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date) desc ");
			sql_Final.append("order by CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date) desc,A.DONO DESC,A.ITEM ");
			
			System.out.println(sql_Final.toString());
			
				arrList = movHisDAO.selectForReport(sql_Final.toString(),
						new Hashtable<String, String>());
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}

 
 public ArrayList getPickingSummarybyfilterByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String filter, String plant,String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			StringBuffer sql_Final = new StringBuffer();
		
			if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	     if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) >= '" 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2)<= '"
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(ISSUEDATE,7,4)+SUBSTRING(ISSUEDATE,4,2)+SUBSTRING(ISSUEDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		  }
			
	
	     
		String extraCond	=sCondition	+ "AND c.item = a.item AND c.ID = a.ID AND c.USERFLD4 = a.BATCH ";
		if(filter.equalsIgnoreCase("Outbound Order")){
				StringBuffer sql_DO = new StringBuffer();
				sql_DO.append("select a.dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,case a.status when 'C' then isnull(a.pickqty,0) "
					    + "else '0' end as issueqty , isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat," 
						+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ "isnull(b.UNITMO,'') as uom,"
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,isnull(b.PRODGST,0) as Tax, "
						+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo  from "
						+ "["
						+ plant
						+ "_"
						+ "shiphis] a, "
						+ "["
						+ plant
						+ "_"
						+ "dodet] b, ["+plant+"_dohdr] d," 
						+"["+plant+"_ITEMMST] e  where a.dono<>''  and a.dono=b.dono AND a.dolno=b.dolnno AND d.DONO = b.DONO AND a.ITEM=e.ITEM"
						+ sCondition);
		if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_DO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_DO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_DO.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_DO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_DO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("d.ORDERTYPE") != null) {
					sql_DO.append(" AND d.ORDERTYPE = '" + ht.get("d.ORDERTYPE")
							+ "'");
				}
			
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_DO.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_DO.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
						+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_DO.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_DO.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");
			}
			
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_DO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_DO.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_DO.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}
			if (ht.get("INVOICENO") != null) {
				sql_DO.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
			}
			}
		
			sql_Final.append(sql_DO.toString());
		}else if(filter.equalsIgnoreCase("Loan Order")){
				StringBuffer sql_Loan = new StringBuffer();
				sql_Loan.append("select a.ordno as dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') as expiredate," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ "isnull(b.UNITMO,'') as uom,"
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
						+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo  from "
						+ "["
						+ plant
						+ "_"
						+ "loan_pick] a, "
						+ "["
						+ plant
						+ "_"
						+ "loandet] b,  "
						+ "["
						+ plant
						+ "_"
						+ "invmst] c,"
						+"["
						+plant
						+"_ITEMMST] e where a.ordno<>'' and a.ORDLNNO=b.ORDLNNO and a.ordno=b.ordno and  a.item=b.item AND a.ITEM=e.ITEM "
						+ extraCond);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_Loan.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME_LON") != null) {
					sql_Loan.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME_LON")
							+ "%'");
				}
				if (ht.get("a.DONO") != null) {
					sql_Loan
							.append(" AND A.ORDNO = '" + ht.get("a.DONO") + "'");
				}
				if (ht.get("a.BATCH") != null) {
					sql_Loan.append(" AND a.BATCH = '" + ht.get("a.BATCH")
							+ "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_Loan.append(" AND a.ITEMDESC = '"
							+ ht.get("a.ITEMDESC") + "'");
				}
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_Loan.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_Loan.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_Loan.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
				+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_Loan.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");
			}
			
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_Loan.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_Loan.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_Loan.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}
		
			}
			sql_Final.append(sql_Loan.toString());
		}else if(filter.equalsIgnoreCase("Transfer Order")){
				StringBuffer sql_Transfer = new StringBuffer();
				sql_Transfer.append("select distinct a.tono as dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.pickqty,0) issueqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,b.lnstat, isnull(c.EXPIREDATE, '') expiredate ," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ "isnull(b.UNITMO,'') as uom,"
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,'' as Tax, "
						+"0 as unitprice,0 as total,a.issuedate,'' as InvoiceNo   from "
						+ "["
						+ plant
						+ "_"
						+ "to_pick] a, "
						+ "["
						+ plant
						+ "_"
						+ "todet] b ,  "
						+ "["
						+ plant
						+ "_"
						+ "invmst] c,"
						+"["
						+plant
						+"_ITEMMST] e where a.tono<>'' and a.TOLNO=b.TOLNNO and a.tono=b.tono and a.item=b.item AND a.ITEM=e.ITEM "
						+ extraCond);
		if (ht.size() > 0) {
			if (ht.get("a.ITEM") != null) {
				sql_Transfer.append(" AND A.ITEM = '" + ht.get("a.ITEM")
						+ "'");
			}
			if (ht.get("a.CNAME_TO") != null) {
				sql_Transfer.append(" AND A.CNAME LIKE '%"
						+ ht.get("a.CNAME_TO") + "%'");
			}
			if (ht.get("a.DONO") != null) {
				sql_Transfer.append(" AND A.TONO = '" + ht.get("a.DONO")
						+ "'");
			}
			if (ht.get("a.BATCH") != null) {
				sql_Transfer.append(" AND a.BATCH = '" + ht.get("a.BATCH")
						+ "'");
			}
			if (ht.get("a.ITEMDESC") != null) {
				sql_Transfer.append(" AND a.ITEMDESC = '"+ ht.get("a.ITEMDESC") + "'");
			}
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_Transfer.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_Transfer.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
						+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_Transfer.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_Transfer.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");
			}
			
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_Transfer.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_Transfer.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}	
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_Transfer.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}	
			}
			sql_Final.append(sql_Transfer.toString());
		}else{
			String TRAN_TYPE="GOODSISSUE";
			if(filter.equalsIgnoreCase("Tax Invoice"))
			  TRAN_TYPE= "TAXINVOICE";
			else if(filter.equalsIgnoreCase("Kitting"))
				  TRAN_TYPE= "KITTING";
			else if(filter.equalsIgnoreCase("De-Kitting"))
				TRAN_TYPE= "DE-KITTING";
			else if(filter.equalsIgnoreCase("Stock Take"))
				TRAN_TYPE= "STOCK_TAKE";
			
			StringBuffer sql_Miscissue = new StringBuffer();
			sql_Miscissue.append("select a.dono,a.cname,a.item,isnull(e.itemdesc,'') itemdesc,isnull(a.CRBY,'') as users,isnull(a.batch,'') batch,isnull(a.pickqty,0) as issueqty,isnull(a.ordqty,0) ordqty,isnull(a.pickqty,0) pickqty,isnull(a.reverseqty,0) reverseqty,isnull(upper(a.loc),0) loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,isnull(a.status,'') as lnstat,"  
					    +" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
						+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," 
						+ "ISNULL(issuedate,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4)) transactiondate,"
						+ " CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
						+ "ISNULL(CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date),'') AS toexceltransactiondate,"
						+ "isnull((Select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.dono and batch=a.batch and DOLNNO=a.DOLNO),'')as uom,"
						+"isnull(e.prd_brand_id,'') as prd_brand_id,isnull(e.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(e.prd_cls_id,'') as prd_cls_id,isnull(e.itemtype,'') as itemtype,ISNULL((SELECT TOP 1 ISNULL(OUTBOUND_GST,'') FROM "+plant+"_POSHDR WHERE PLANT=A.PLANT AND POSTRANID=A.DONO),'') as Tax, "	
						+"isnull(a.unitprice,0) unitprice,case a.status when 'C' then isnull(a.pickqty*a.unitprice,0) else '0' end as total,a.issuedate,ISNULL((INVOICENO),'') as InvoiceNo  from "
				    	+ "["
						+ plant
						+ "_"
						+ "shiphis] a," 
						+"["
						+plant
						+"_ITEMMST] e where a.TRAN_TYPE='"+TRAN_TYPE+"' AND a.ITEM=e.ITEM "//and a.item =  '" + ht.get("a.ITEM") + "' AND A.DONO = '" + ht.get("a.DONO") + "' AND    A.CNAME LIKE '" + ht.get("a.CNAME") + "' AND a.BATCH = '" + ht.get("a.BATCH") + "' AND a.ITEMDESC = '" + ht.get("a.ITEMDESC") + "'"
						+ sCondition);
		
		if (ht.size() > 0) {
			if (ht.get("a.ITEM") != null) {
				sql_Miscissue.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
			}
			if (ht.get("a.CNAME") != null) {
				sql_Miscissue.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME") + "%'");
			}
			if (ht.get("a.DONO") != null) {
				sql_Miscissue.append(" AND A.DONO = '" + ht.get("a.DONO") + "'");
			}
			if (ht.get("a.BATCH") != null) {
				sql_Miscissue.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
			}
			if (ht.get("a.ITEMDESC") != null) {
				sql_Miscissue.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
						+ "'");
			}
			if (ht.get("C.PRD_CLS_ID") != null) {
				sql_Miscissue.append(" AND e.PRD_CLS_ID = '" + ht.get("C.PRD_CLS_ID")
						+ "'");
			}
			if (ht.get("C.PRD_DEPT_ID") != null) {
				sql_Miscissue.append(" AND e.PRD_DEPT_ID = '" + ht.get("C.PRD_DEPT_ID")
				+ "'");
			}
			if (ht.get("C.PRD_BRAND_ID") != null) {
				sql_Miscissue.append(" AND e.PRD_BRAND_ID = '" + ht.get("C.PRD_BRAND_ID")
						+ "'");
			}
			if (ht.get("C.ITEMTYPE") != null) {
				sql_Miscissue.append(" AND e.ITEMTYPE = '" + ht.get("C.ITEMTYPE")
						+ "'");

			}
			if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
				sql_Miscissue.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' AND a.REMARK IS NOT NULL AND a.REMARK <> ''");
					
			}
			

			if (ht.get("a.LOC") != null) {
				sql_Miscissue.append(" AND a.LOC LIKE '%" + ht.get("a.LOC")
						+ "%'"  );
			}
			if (ht.get("LOC_TYPE_ID") != null) {
				sql_Miscissue.append("   AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
			}
			if (ht.get("LOC_TYPE_ID2") != null) {
				sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,(String)ht.get("LOC_TYPE_ID2"))+") ");
			}
			if (ht.get("LOC_TYPE_ID3") != null) {
				sql_Miscissue.append("  AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,(String)ht.get("LOC_TYPE_ID3"))+") ");
			}
			if (ht.get("INVOICENO") != null) {
				sql_Miscissue.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
			}
		}
						
			sql_Final.append(sql_Miscissue.toString());	
		}
			
			//sql_Final.append("order by a.item,a.issuedate");
			sql_Final.append("order by a.item, CAST((SUBSTRING(a.issuedate,7,4) + SUBSTRING(a.issuedate,4,2) + SUBSTRING(a.issuedate,1,2)) AS date) desc ");
			
			System.out.println(sql_Final.toString());
			
				arrList = movHisDAO.selectForReport(sql_Final.toString(),
						new Hashtable<String, String>());
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}
 
 public ArrayList getReportIBSummaryDetailsByCost(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		   
	          }
			
			 if (ht.size() > 0) {
					if (ht.get("SUPPLIERTYPE") != null) {
						String suppliertype = ht.get("SUPPLIERTYPE").toString();
						sCondition= sCondition+" AND a.cname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
						ht.remove("SUPPLIERTYPE");
					}
				}

	        		dtCondStr ="and ISNULL(A.RECVDATE,'')<>'' AND CAST((SUBSTRING(A.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
	        		extraCon= "order by CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) desc, A.PONO desc, A.ITEM";    			        
			       
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
		           
			           if (custname.length()>0){
                   	custname = StrUtils.InsertQuotes(custname);
                   	sCondition = sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
                    } 
			           //htData.put("A.PONO <> ''");
			           sql = new StringBuffer(" SELECT A.PONO as pono,(SELECT CURRENCYID FROM [" + plant + "_POHDR] WHERE PONO=A.PONO) CURRENCYID,(SELECT CURRENCYUSEQT  FROM [" + plant + "_POHDR] WHERE PONO=A.PONO) CURRENCYUSEQT, A.ITEM as item ,B.RECQTY as qty,A.RECVDATE,B.UNITCOST as unitcost,(B.RECQTY*B.UNITCOST)RecvCost,A.CNAME as custname,");
                    sql.append(" CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
                    sql.append(" (SELECT SUM(QTYOR) FROM [" + plant + "_" + "PODET" + "] WHERE ITEM=A.ITEM AND PONO=A.PONO and UNITMO=B.UOM GROUP BY PONO,ITEM,UNITMO) qtyor, ");
                    sql.append(" (SELECT ORDERTYPE  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) ordertype,");
			           sql.append(" (SELECT JOBNUM  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) jobNum,");
			           sql.append(" (SELECT ISNULL(INBOUND_GST,0) FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) Tax,");
			           sql.append(" ((B.RECQTY*B.UNITCOST) * (SELECT ISNULL(INBOUND_GST,0) FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO))/100 taxval,");
			           sql.append(" (SELECT ISNULL(REMARK1,'') FROM [" + plant + "_" + "POHDR" + "] R WHERE PONO=A.PONO) remarks,");
			           sql.append(" (SELECT ISNULL(REMARK3,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) remarks3,");
			           sql.append(" (SELECT ISNULL(STATUS_ID,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) status_id, ");
			           sql.append(" (SELECT ISNULL(PRD_CLS_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_cls_id, ");
			           sql.append(" (SELECT ISNULL(ITEMTYPE,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemtype, ");
			           sql.append(" (SELECT ISNULL(PRD_BRAND_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_brand_id, ");
			           sql.append(" (SELECT ISNULL(PRD_DEPT_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) PRD_DEPT_ID, ");
			           sql.append("(SELECT ISNULL(ITEMDESC,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemdesc, ");
			           sql.append(" A.GRNO, ");
			           sql.append(" B.UOM, ");
			           sql.append(" A.users, ");
			           sql.append(" (SELECT TAXTREATMENT  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) TAXTREATMENT,");
			           sql.append(" (SELECT TAXID  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) TAXID,");
			           sql.append(" (SELECT PURCHASE_LOCATION  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) PURCHASE_LOCATION,");
			           sql.append(" (SELECT CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) REVERSECHARGE,");
			           sql.append(" (SELECT CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) GOODSIMPORT ");
			           sql.append(" FROM");
			           sql.append(" (SELECT PONO,ITEM,RECVDATE,CNAME,ISNULL(CRBY, '') AS users,ISNULL(GRNO, '') AS GRNO FROM [" + plant + "_" + "RECVDET" + "] WHERE TRAN_TYPE='IB' GROUP BY PONO,ITEM,ITEMDESC,RECVDATE,CNAME,GRNO,CRBY) A,");
			           sql.append(" (SELECT r.PONO,r.ITEM,SUM(ORDQTY)ORDQTY,isnull(UNITMO,'') as UOM ,SUM(RECQTY)RECQTY,MAX(ISNULL(C.UNITCOST_AOD,C.UNITCOST) ) UNITCOST,RECVDATE,ISNULL(GRNO, '') AS GRNO ");
			           sql.append(" FROM [" + plant + "_" + "RECVDET" + "] r  , [" + plant + "_" + "PODET" + "] c  ");
			           sql.append("WHERE c.pono=r.pono and c.POLNNO=LNNO and c.item=r.item and TRAN_TYPE='IB' GROUP BY r.PONO,r.ITEM,RECVDATE,UNITMO,GRNO)  B ");
			           sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM  AND A.RECVDATE=B.RECVDATE AND A.GRNO=B.GRNO  " + sCondition );
			           
			            if (ht.get("JOBNUM") != null) {
	       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
	       			    }
			            if (ht.get("ITEM") != null) {
    					  sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
    				    }
     				if (ht.get("PONO") != null) {
    					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
    				    }
     				if (ht.get("GRNO") != null) {
     					sql.append(" AND A.GRNO = '" + ht.get("GRNO") + "'");
     				}
    				    if (ht.get("STATUS") != null) {
    					  sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  PONO=A.PONO)");
    				    }
     				if (ht.get("ORDERTYPE") != null) {
    					   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
     				}
    				   if (ht.get("ITEMTYPE") != null) {
    					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
    				    }
    				   if (ht.get("PRD_CLS_ID") != null) {
    					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
    				   }
    				   if (ht.get("PRD_BRAND_ID") != null) {
    					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
    				   }
    				   if (ht.get("PRD_DEPT_ID") != null) {
      					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
      				   }
    				   if (ht.get("STATUS_ID") != null) {
    					sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  PONO=A.PONO)");
    				   }
    				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
         
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}

    public ArrayList getReportIBSummaryDetailsByCostlocalexpenses(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		   
	          }
			
			 if (ht.size() > 0) {
					if (ht.get("SUPPLIERTYPE") != null) {
						String suppliertype = ht.get("SUPPLIERTYPE").toString();
						sCondition= sCondition+" AND a.cname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
						ht.remove("SUPPLIERTYPE");
					}
				}

	        		dtCondStr ="and ISNULL(A.RECVDATE,'')<>'' AND CAST((SUBSTRING(A.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
	        		extraCon= "order by CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) desc, A.PONO desc, A.ITEM ";    			        
			       
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
		           
			           if (custname.length()>0){
                      	custname = StrUtils.InsertQuotes(custname);
                      	sCondition = sCondition + " AND A.custname LIKE '%"+custname+"%' " ;
                       } 
			           //htData.put("A.PONO <> ''");
			           sql = new StringBuffer("WITH temptable AS ( SELECT A.PONO as pono,A.ITEM as item ,B.RLNNO as lnno,B.RECQTY as qty,A.RECVDATE,B.UNITCOST as unitcost,(B.RECQTY*B.UNITCOST) RecvCost,A.CNAME as custname,");
                       sql.append(" CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
                       sql.append(" (SELECT SUM(QTYOR) FROM [" + plant + "_" + "PODET" + "] WHERE ITEM=A.ITEM AND PONO=A.PONO and UNITMO=B.UOM GROUP BY PONO,ITEM,UNITMO) qtyor, ");
                       sql.append(" (SELECT ORDERTYPE  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) ordertype,");
			           sql.append(" (SELECT JOBNUM  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) jobNum,");
			           sql.append(" (SELECT ISNULL(INBOUND_GST,0) FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) Tax,");
			           sql.append(" ((B.RECQTY*B.UNITCOST) * (SELECT ISNULL(INBOUND_GST,0) FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO))/100 taxval,");
			           sql.append(" (SELECT ISNULL(REMARK1,'') FROM [" + plant + "_" + "POHDR" + "] R WHERE PONO=A.PONO) remarks,");
			           sql.append(" (SELECT ISNULL(REMARK3,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) remarks3,");
			           sql.append(" (SELECT ISNULL(STATUS_ID,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) status_id, ");
			           sql.append(" (SELECT ISNULL(PRD_CLS_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_cls_id, ");
			           sql.append(" (SELECT ISNULL(ITEMTYPE,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemtype, ");
			           sql.append(" (SELECT ISNULL(PRD_BRAND_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_brand_id, ");
			           sql.append(" (SELECT ISNULL(PRD_DEPT_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) PRD_DEPT_ID, ");
			           sql.append("(SELECT ISNULL(ITEMDESC,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemdesc, ");
			           sql.append(" (SELECT TAXID  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) TAXID,");
			           sql.append("(SELECT E.LANDED_COST from [" + plant + "_RECVDET] C LEFT JOIN [" + plant + "_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN [" + plant + "_FINBILLDET] E ON D.ID = E.BILLHDRID ");
			           sql.append(" WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM ");
			           sql.append(" GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST");
			           sql.append(" ) AS LANDED_COST,");
			           sql.append(" A.GRNO, ");
			           sql.append(" B.UOM, ");
			           sql.append ("A.users");
			           sql.append(" FROM");
			           sql.append(" (SELECT PONO,LNNO,ITEM,RECVDATE,CNAME,ISNULL(CRBY, '') AS users,ISNULL(GRNO, '') AS GRNO FROM [" + plant + "_" + "RECVDET" + "] WHERE TRAN_TYPE='IB' GROUP BY PONO,LNNO,ITEM,ITEMDESC,RECVDATE,CNAME,GRNO,CRBY) A,");
			           sql.append(" (SELECT R.PONO,R.ITEM,SUM(ORDQTY)ORDQTY ,SUM(RECQTY)RECQTY,R.LNNO as RLNNO,MAX(isnull(R.unitcost,0)) UNITCOST,RECVDATE,ISNULL(p.UNITMO, '') AS UOM,ISNULL(GRNO, '') AS GRNO ");
			           sql.append(" FROM [" + plant + "_" + "RECVDET" + "] R, [" + plant + "_" + "PODET" + "] P");
			           sql.append(" WHERE P.PONO=R.PONO and R.LNNO=P.POLNNO and r.item=p.item and TRAN_TYPE='IB' GROUP BY R.PONO,R.ITEM,RECVDATE,GRNO,UNITMO,R.LNNO)  B ");
			           sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM  AND A.RECVDATE=B.RECVDATE AND A.GRNO=B.GRNO AND A.lnno=b.RLNNO ) ");
			           sql.append(" SELECT A.GRNO,P.CURRENCYID,P.CURRENCYUSEQT,A.users,A.RECVDATE,A.Tax,A.custname,A.item,A.itemdesc,A.itemtype,A.itemtype,A.jobNum,A.ordertype,A.pono,A.prd_brand_id,A.PRD_DEPT_ID,A.prd_cls_id,A.qty,A.qtyor,A.remarks,A.status_id,A.TAXID,A.trandate,A.trandate, ");
			           sql.append(" isnull(A.UOM,'') as UOM ,isnull((((A.qty * ((isnull(A.unitcost*(s.CURRENCYUSEQT),0) + ISNULL(A.LANDED_COST,0) + (isnull(A.unitcost,0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (A.LANDED_COST) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT)))*Tax)/100),0) as taxval,");
			           sql.append(" isnull((A.qty * ((isnull(A.unitcost*(s.CURRENCYUSEQT),0) + ISNULL(A.LANDED_COST,0) + (isnull(A.unitcost,0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (A.LANDED_COST) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT))),0) as RecvCost,");
			           /*sql.append(" isnull(((isnull(A.unitcost*(s.CURRENCYUSEQT),0) + ISNULL(A.LANDED_COST,0) + (isnull(A.unitcost,0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (A.LANDED_COST) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT)),0) as unitcost,");*/
			           sql.append("  isnull(((CASE WHEN (A.LANDED_COST) is null THEN (isnull(s.UNITCOST_AOD,0)*(s.CURRENCYUSEQT)*(s.QTYOR) * (((isnull(P.LOCALEXPENSES,0)+ P.SHIPPINGCOST))/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST_AOD*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))) ELSE isnull(s.UNITCOST*(s.CURRENCYUSEQT),0) + ISNULL(A.LANDED_COST,0) END)/ (s.CURRENCYUSEQT)),0) as unitcost,");
			           sql.append("ISNULL(P.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(P.TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(P.REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
			           sql.append("CASE WHEN ISNULL(P.GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT ");
			           sql.append(" from temptable A, [" + plant + "_POHDR] P, [" + plant + "_podet] s WHERE A.PONO=P.PONO AND A.UOM=s.UNITMO and s.pono=A.pono and s.POLNNO = A.lnno and s.ITEM=A.ITEM "+ sCondition );
			            if (ht.get("JOBNUM") != null) {
	       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
	       			    }
			            if (ht.get("ITEM") != null) {
       					  sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
       				    }
        				if (ht.get("PONO") != null) {
       					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
       				    }
        				if (ht.get("GRNO") != null) {
        					sql.append(" AND A.GRNO = '" + ht.get("GRNO") + "'");
        				}
       				    if (ht.get("STATUS") != null) {
       					  sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  PONO=A.PONO)");
       				    }
        				if (ht.get("ORDERTYPE") != null) {
       					   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
        				}
       				   if (ht.get("ITEMTYPE") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
       				    }
       				   if (ht.get("PRD_CLS_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				   if (ht.get("PRD_BRAND_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				 if (ht.get("PRD_DEPT_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				   if (ht.get("STATUS_ID") != null) {
       					sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  PONO=A.PONO)");
       				   }
       				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
            
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
    
    public ArrayList getReportIBSummaryDetailsByCostByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		  try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		   
	          }
						 
			 if (ht.size() > 0) {
					if (ht.get("SUPPLIERTYPE") != null) {
						String suppliertype = ht.get("SUPPLIERTYPE").toString();
						sCondition= sCondition+" AND a.cname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
						ht.remove("SUPPLIERTYPE");
					}
				}

	        		dtCondStr ="and ISNULL(A.RECVDATE,'')<>'' AND CAST((SUBSTRING(A.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
	        		extraCon= "order by A.CNAME, A.PONO,A.ITEM,CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) ";    			        
			       
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
		           
			           if (custname.length()>0){
                      	custname = StrUtils.InsertQuotes(custname);
                      	sCondition = sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
                       } 
			           //htData.put("A.PONO <> ''");
			           sql = new StringBuffer(" SELECT A.PONO as pono,A.ITEM as item ,B.RECQTY as qty,A.RECVDATE,B.UNITCOST as unitcost,(B.RECQTY*B.UNITCOST)RecvCost,A.CNAME as custname,");
                       sql.append(" CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
                       sql.append(" (SELECT SUM(QTYOR) FROM [" + plant + "_" + "PODET" + "] WHERE ITEM=A.ITEM AND PONO=A.PONO and UNITMO=B.UOM GROUP BY PONO,ITEM,UNITMO) qtyor, ");
                       sql.append(" (SELECT ORDERTYPE  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) ordertype,");
			           sql.append(" (SELECT JOBNUM  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) jobNum,");
			           sql.append(" (SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM) Tax,");
			           sql.append(" ((B.RECQTY*B.UNITCOST) * (SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM))/100  taxval,");
			           sql.append(" (SELECT ISNULL(REMARK1,'') FROM [" + plant + "_" + "POHDR" + "] R WHERE PONO=A.PONO) remarks,");
			           sql.append(" (SELECT ISNULL(REMARK3,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) remarks3,");
			           sql.append(" (SELECT ISNULL(STATUS_ID,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) status_id, ");
			           sql.append(" (SELECT ISNULL(PRD_CLS_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_cls_id, ");
			           sql.append(" (SELECT ISNULL(ITEMTYPE,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemtype, ");
			           sql.append(" (SELECT ISNULL(PRD_BRAND_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_brand_id, ");
			           sql.append(" (SELECT ISNULL(PRD_DEPT_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) PRD_DEPT_ID, ");
			           sql.append("(SELECT ISNULL(ITEMDESC,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemdesc, ");
			           sql.append(" A.GRNO, ");
			           sql.append(" B.UOM, ");
			           sql.append(" A.users, ");
			           sql.append(" (SELECT TAXTREATMENT  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) TAXTREATMENT,");
			           sql.append(" (SELECT PURCHASE_LOCATION  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) PURCHASE_LOCATION,");
			           sql.append(" (SELECT CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) REVERSECHARGE,");
			           sql.append(" (SELECT CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) GOODSIMPORT ");
			           sql.append(" FROM");
			           sql.append(" (SELECT PONO,ITEM,RECVDATE,CNAME,ISNULL(CRBY, '') AS users,ISNULL(GRNO, '') AS GRNO FROM [" + plant + "_" + "RECVDET" + "] WHERE TRAN_TYPE='IB' GROUP BY PONO,ITEM,ITEMDESC,RECVDATE,CNAME,GRNO,CRBY) A,");
			           sql.append(" (SELECT r.PONO,r.ITEM,isnull(UNITMO,'') as UOM,SUM(ORDQTY)ORDQTY ,SUM(RECQTY)RECQTY,MAX(ISNULL(r.UNITCOST,0) ) UNITCOST,RECVDATE,ISNULL(GRNO, '') AS GRNO ");
			           sql.append(" FROM [" + plant + "_" + "RECVDET" + "]r, [" + plant + "_" + "PODET" + "]c");
			           sql.append(" WHERE c.pono=r.pono and c.POLNNO=LNNO and c.item=r.item and TRAN_TYPE='IB' GROUP BY r.PONO,r.ITEM,RECVDATE,GRNO,UNITMO)  B ");
			           sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM  AND A.RECVDATE=B.RECVDATE AND A.GRNO=B.GRNO  " + sCondition );
			           
			            if (ht.get("JOBNUM") != null) {
	       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
	       			    }
			            if (ht.get("ITEM") != null) {
       					  sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
       				    }
        				if (ht.get("PONO") != null) {
       					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
       				    }
        				if (ht.get("GRNO") != null) {
        					sql.append(" AND A.GRNO='" + ht.get("GRNO") + "'");
        				}
       				    if (ht.get("STATUS") != null) {
       					  sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  PONO=A.PONO)");
       				    }
        				if (ht.get("ORDERTYPE") != null) {
       					   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
        				}
       				   if (ht.get("ITEMTYPE") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
       				    }
       				   if (ht.get("PRD_CLS_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				   if (ht.get("PRD_BRAND_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				 if (ht.get("PRD_DEPT_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				   if (ht.get("STATUS_ID") != null) {
       					sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  PONO=A.PONO)");
       				   }
       				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
            
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
  
    public ArrayList getReportIBSummaryDetailsByCostByProductGstlocalexpenses(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		  try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		   
	          }
						 
			 if (ht.size() > 0) {
					if (ht.get("SUPPLIERTYPE") != null) {
						String suppliertype = ht.get("SUPPLIERTYPE").toString();
						sCondition= sCondition+" AND a.cname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
						ht.remove("SUPPLIERTYPE");
					}
				}

	        		dtCondStr ="and ISNULL(A.RECVDATE,'')<>'' AND CAST((SUBSTRING(A.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
	        		extraCon= "order by A.custname, A.PONO,A.ITEM,CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) ";    			        
			       
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
		           
			           if (custname.length()>0){
                      	custname = StrUtils.InsertQuotes(custname);
                      	sCondition = sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
                       } 
			           //htData.put("A.PONO <> ''");
			           sql = new StringBuffer(" WITH temptable AS (SELECT A.PONO as pono,A.ITEM as item ,B.RECQTY as qty,A.RECVDATE,B.UNITCOST as unitcost,(B.RECQTY*B.UNITCOST)RecvCost,A.CNAME as custname,");
                       sql.append(" CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
                       sql.append(" (SELECT SUM(QTYOR) FROM [" + plant + "_" + "PODET" + "] WHERE ITEM=A.ITEM AND PONO=A.PONO and UNITMO = B.UOM GROUP BY PONO,ITEM,UNITMO) qtyor, ");
                       sql.append(" (SELECT ORDERTYPE  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) ordertype,");
			           sql.append(" (SELECT JOBNUM  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) jobNum,");
			           sql.append(" (SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM) Tax,");
			           sql.append(" ((B.RECQTY*B.UNITCOST) * (SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM))/100  taxval,");
			           sql.append(" (SELECT ISNULL(REMARK1,'') FROM [" + plant + "_" + "POHDR" + "] R WHERE PONO=A.PONO) remarks,");
			           sql.append(" (SELECT ISNULL(REMARK3,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) remarks3,");
			           sql.append(" (SELECT ISNULL(STATUS_ID,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) status_id, ");
			           sql.append(" (SELECT ISNULL(PRD_CLS_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_cls_id, ");
			           sql.append(" (SELECT ISNULL(PRD_DEPT_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) PRD_DEPT_ID, ");
			           sql.append(" (SELECT ISNULL(ITEMTYPE,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemtype, ");
			           sql.append(" (SELECT ISNULL(PRD_BRAND_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_brand_id, ");
			           sql.append("(SELECT ISNULL(ITEMDESC,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemdesc, ");
			           sql.append("(SELECT E.LANDED_COST from [" + plant + "_RECVDET] C LEFT JOIN [" + plant + "_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN [" + plant + "_FINBILLDET] E ON D.ID = E.BILLHDRID ");
			           sql.append(" WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM ");
			           sql.append(" GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST");
			           sql.append(" ) AS LANDED_COST,");
			           sql.append(" A.GRNO, ");
			           sql.append(" B.UOM, ");
			           sql.append("A.users");
			           sql.append(" FROM");
			           sql.append(" (SELECT PONO,LNNO,ITEM,RECVDATE,CNAME,ISNULL(CRBY, '') AS users, ISNULL(GRNO, '') AS GRNO FROM [" + plant + "_" + "RECVDET" + "] WHERE TRAN_TYPE='IB' GROUP BY PONO,LNNO,ITEM,ITEMDESC,RECVDATE,CNAME,GRNO,CRBY) A,");
			           sql.append(" (SELECT R.PONO,R.ITEM,SUM(ORDQTY)ORDQTY ,SUM(RECQTY)RECQTY,MAX(isnull(R.unitcost,0)) UNITCOST,RECVDATE,ISNULL(p.UNITMO, '') AS UOM,ISNULL(GRNO, '') AS GRNO ");
			           sql.append(" FROM [" + plant + "_" + "RECVDET" + "] R, [" + plant + "_" + "PODET" + "] P");
			           sql.append(" WHERE P.PONO=R.PONO and R.LNNO=P.POLNNO and r.item=p.item and TRAN_TYPE='IB' GROUP BY R.PONO,R.ITEM,RECVDATE,GRNO,UNITMO)  B ");
			           sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM  AND A.RECVDATE=B.RECVDATE AND A.GRNO=B.GRNO) ");
			           sql.append(" SELECT A.GRNO,A.users,A.RECVDATE,A.Tax,A.custname,A.item,A.itemdesc,A.itemtype,A.itemtype,A.jobNum,A.ordertype,A.pono,A.PRD_DEPT_ID,A.prd_brand_id,A.prd_cls_id,A.qty,A.qtyor,A.remarks,A.status_id,A.trandate,A.trandate, ");
			           sql.append(" isnull(A.UOM,'') as UOM ,isnull((((A.qty * ((isnull(A.unitcost*(s.CURRENCYUSEQT),0) + ISNULL(A.LANDED_COST,0) + (isnull(A.unitcost,0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (A.LANDED_COST) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT)))*Tax)/100),0) as taxval,");
			           sql.append(" isnull((A.qty * ((isnull(A.unitcost*(s.CURRENCYUSEQT),0) + ISNULL(A.LANDED_COST,0) + (isnull(A.unitcost,0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (A.LANDED_COST) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT))),0) as RecvCost,");
			           sql.append(" isnull(((isnull(A.unitcost*(s.CURRENCYUSEQT),0) + ISNULL(A.LANDED_COST,0) + (isnull(A.unitcost,0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+CASE WHEN (A.LANDED_COST) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT)),0) as unitcost,");
			           sql.append("ISNULL(P.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(P.TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(P.REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
			           sql.append("CASE WHEN ISNULL(P.GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT ");
			           sql.append(" from temptable A, [" + plant + "_POHDR] P, [" + plant + "_podet] s WHERE A.PONO=P.PONO and A.UOM=s.UNITMO AND s.pono=A.pono and s.ITEM=A.ITEM "+ sCondition );
			           
			            if (ht.get("JOBNUM") != null) {
	       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
	       			    }
			            if (ht.get("ITEM") != null) {
       					  sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
       				    }
        				if (ht.get("PONO") != null) {
       					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
       				    }
        				if (ht.get("GRNO") != null) {
        					sql.append(" AND A.GRNO='" + ht.get("GRNO") + "'");
        				}
       				    if (ht.get("STATUS") != null) {
       					  sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  PONO=A.PONO)");
       				    }
        				if (ht.get("ORDERTYPE") != null) {
       					   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
        				}
       				   if (ht.get("ITEMTYPE") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
       				    }
       				   if (ht.get("PRD_CLS_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				if (ht.get("PRD_DEPT_ID") != null) {
      					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
      				   }
       				   if (ht.get("PRD_BRAND_ID") != null) {
       					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
       				   }
       				   if (ht.get("STATUS_ID") != null) {
       					sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  PONO=A.PONO)");
       				   }
       				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
            
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
  
  public ArrayList getPOSummaryToPrint(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String suppliername,String type) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",extraCon="";
		 Hashtable htData = new Hashtable();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			            if (suppliername.length()>0){
			            	suppliername = StrUtils.InsertQuotes(suppliername);
			            	sCondition =  sCondition + " AND A.CNAME LIKE '%"+suppliername+"%' " ;
			            }
                       
			            String dtCondStr =    " and ISNULL(a.recvdate,'')<>'' AND CAST((SUBSTRING(a.recvdate, 7, 4) + '-' + SUBSTRING(a.recvdate, 4, 2) + '-' + SUBSTRING(a.recvdate, 1, 2)) AS date)";
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
			            if (ht.size() > 0) {
							if (ht.get("SUPPLIERTYPE") != null) {
								String suppliertype = ht.get("SUPPLIERTYPE").toString();
								sCondition= sCondition+" AND a.cname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
								ht.remove("SUPPLIERTYPE");
							}
						}
                        extraCon = "  group by  a.cname,a.pono order by a.cname,a.pono ";
                        
                       StringBuffer sql = new StringBuffer(" select a.pono,a.cname as custname,");
                       sql.append(" (SELECT CUSTCODE  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=a.PONO) custcode,");
                       sql.append(" (SELECT ORDERTYPE  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=a.PONO) ordertype,");
                       sql.append(" (SELECT STATUS  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=a.PONO) status,");
                       sql.append(" (SELECT ISNULL(STATUS_ID,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=a.PONO) status_id, ");
                       sql.append(" (SELECT JOBNUM  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=a.PONO) jobnum,");
                       sql.append(" (SELECT ISNULL(SUM(QTYOR),0) FROM [" + plant + "_" + "PODET" + "] WHERE  PONO=A.PONO ");
                       sql.append("  AND ITEM IN(SELECT ITEM FROM [" + plant + "_" + "ITEMMST" + "] WHERE  NONSTKFLAG='N')) qtyor, ");
                       sql.append("  isnull(sum(RECQTY),0) qty ");
                       sql.append(" from " + "[" + plant + "_" + "recvdet" + "] a");
                       sql.append(" where TRAN_TYPE='IB' " + sCondition);
				 	         
				       if (ht.get("JOBNUM") != null) {
                    	   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
                       }
		            
				       if (ht.get("PONO") != null) {
				    	   sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
				       }
				       if (ht.get("GRNO") != null) {
				    	   sql.append(" AND A.GRNO = '" + ht.get("GRNO") + "'");
				       }
				       if (ht.get("STATUS") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  PONO=A.PONO)");
				       }
				       if (ht.get("ORDERTYPE") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
				       }
 				  
				       if (ht.get("STATUS_ID") != null) {
				    	   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  PONO=A.PONO)");
				       }
 				       
				       arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
				} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :InboundOrderHandlerServlet :: getPOSummaryToPrint:", e);
		}
		return arrList;
	}


  public ArrayList getCustomerDOInvoiceIssueSummary(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype,String ispos) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		     if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
           if (custname.length()>0){
                	custname = StrUtils.InsertQuotes(custname);
                 	sCondition =  sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
              }
           if (ht.size() > 0) {
				if (ht.get("CUSTTYPE") != null) {
					String custtype = ht.get("CUSTTYPE").toString();
					sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
					ht.remove("CUSTTYPE");
				}
			}
      		boolean isLocationProvided = false;
      		
       		if (ht.size() > 0) {
       			if (ht.get("LOC") != null && !"".equals(ht.get("LOC"))) {
       				String loc = ht.get("LOC").toString();
       				sCondition = sCondition+" AND A.LOC = '"+loc+"'";			
       				ht.remove("LOC");	
       				isLocationProvided = true;
       	        }
       			if (ht.get("LOC_TYPE_ID") != null && !"".equals(ht.get("LOC_TYPE_ID"))) {
       				String loctypeid = ht.get("LOC_TYPE_ID").toString();
       				sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
       				ht.remove("LOC_TYPE_ID");
       				isLocationProvided = true;
       			}
       			if (ht.get("LOC_TYPE_ID2") != null && !"".equals(ht.get("LOC_TYPE_ID2"))) {
       				String loctypeid2 = ht.get("LOC_TYPE_ID2").toString();
       				sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )";
       				ht.remove("LOC_TYPE_ID2");
       				isLocationProvided = true;
       			}
       			if (ht.get("LOC_TYPE_ID3") != null && !"".equals(ht.get("LOC_TYPE_ID3"))) {
       				String loctypeid3 = ht.get("LOC_TYPE_ID3").toString();
       				sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,loctypeid3)+" )";
       				ht.remove("LOC_TYPE_ID3");
       				isLocationProvided = true;
       			}
       			if ("LOCATION".equals(ht.get("SORT"))) {
       				ht.remove("SORT");
       				isLocationProvided = true;
       			}
       		}
       		
       		String AlocationColumn = "";
       		String RlocationColumn = "";
       		String locationColumn = "";
       			if (isLocationProvided) {
       				locationColumn = "LOC,";
       				RlocationColumn = "r.LOC,";
       				AlocationColumn = "A.LOC,";       				
       			}
       			
    		    String dtCondStr =    " and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
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
                          //sCondition = sCondition +" and (a.DONO like 'S%' OR a.DONO like 'TI%') and  a.DONO not like 'SM%' ";
                          sCondition = sCondition +" and a.DONO in (SELECT SS.DONO FROM ["+plant+"_DOHDR] SS) ";
                          if(sorttype.equalsIgnoreCase("PRODUCT"))
                          {       
                          		extraCon="GROUP BY A.INVOICENO,a.ITEM,a.DONO,a.CNAME,a.PICKQTY,B.PICKQTY,B.PickStatus,b.LNSTAT,A.DOLNO,A.ISSUEDATE,B.UNITPRICE,B.UOM," + AlocationColumn + "A.users),"
                            		  +"Ranked AS ("
                            		  +"SELECT custcode,dono,users,UOM,item,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,INVOICENO,"
                            		  +"qtyor ,DELIVERY_STATUS,INTRANSIT_STATUS,LNSTAT,TAXID,qtypick ,qty,unitprice,issPrice,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
                            		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
                            		  +" from SORTTYPE)"
                            		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
                          }
                          else if(sorttype.equalsIgnoreCase("CUSTOMER"))
                          {
                          		extraCon="GROUP BY A.INVOICENO,a.CNAME,A.DONO,a.ITEM,a.PICKQTY,B.PICKQTY,B.PickStatus,b.LNSTAT,A.DOLNO,A.ISSUEDATE,B.UNITPRICE,B.UOM," + AlocationColumn + "A.users),"
                              		  +"Ranked AS ("
                              		  +"SELECT custcode,dono,item,users,UOM,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,INVOICENO,"
                              		  +"qtyor ,DELIVERY_STATUS,INTRANSIT_STATUS,LNSTAT,TAXID,qtypick ,qty,unitprice,issPrice,prd_brand_id,PRD_DEPT_ID,prd_cls_id,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
                              		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
                              		  +" from SORTTYPE)"
                              		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
                        
                          }
                          else if(sorttype.equalsIgnoreCase("LOCATION"))
                          {
                          		extraCon="GROUP BY A.INVOICENO,a.CNAME,a.LOC,A.DONO,a.ITEM,a.PICKQTY,B.PICKQTY,B.PickStatus,b.LNSTAT,A.DOLNO,A.ISSUEDATE,B.UNITPRICE,B.UOM,A.users),"
                              		  +"Ranked AS ("
                              		  +"SELECT custcode,dono,item,users,UOM,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,INVOICENO,"
                              		  +"qtyor ,DELIVERY_STATUS,INTRANSIT_STATUS,LNSTAT,TAXID,qtypick ,qty,unitprice,issPrice,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION," + locationColumn
                              		  +"RANK() OVER (PARTITION BY LOC   ORDER BY issPrice desc) rnk"
                              		  +" from SORTTYPE)"
                              		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY LOC)desc";
                        
                          }
                          else if(sorttype.equalsIgnoreCase("DATE"))
                          {       
                        	  extraCon="GROUP BY A.INVOICENO,a.ITEM,a.DONO,a.CNAME,a.PICKQTY,B.PICKQTY,B.PickStatus,b.LNSTAT,A.DOLNO,A.ISSUEDATE,B.UNITPRICE,B.UOM," + AlocationColumn + "A.users),"
                        			  +"Ranked AS ("
                        			  +"SELECT custcode,dono,users,UOM,item,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,INVOICENO,"
                        			  +"qtyor ,DELIVERY_STATUS,INTRANSIT_STATUS,LNSTAT,TAXID,qtypick ,qty,unitprice,issPrice,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
                        			  +"RANK() OVER (PARTITION BY ISSUEDATE   ORDER BY ISSUEDATE desc) rnk"
                        			  +" from SORTTYPE)"
                        			  +"SELECT * FROM Ranked ORDER BY issuedate DESC, rnk";
                          }
                          
                          else{    
                        	  	extraCon = " order by CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date) desc,A.DONO desc";
                          }
                    if(!sorttype.equalsIgnoreCase("")){      
                    sql.append(" WITH SORTTYPE AS( ");}
                    sql.append("SELECT A.DONO as dono,A.ITEM as item,ISNULL(A.PICKQTY,0) as qtypick,ISNULL(B.PICKQTY,0) as qty,A.ISSUEDATE as issuedate,");
    				sql.append("ISNULL(B.UNITPRICE,0) as unitprice,(ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0))issPrice,A.CNAME as custname,");
    				sql.append("CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date) as trandate,");
    				sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_DODET] WHERE ITEM=A.ITEM and DONO=A.DONO and UNITMO=B.UOM GROUP BY ITEM,DONO,UNITMO),0) qtyor,");
    				sql.append("(SELECT CustCode  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) custcode,");
    				sql.append("(SELECT ORDERTYPE  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) orderType,");
    				sql.append("(SELECT JOBNUM  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) jobNum,");
    				sql.append(" ISNULL((((ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0)) * (SELECT ISNULL(OUTBOUND_GST,0) FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO))/100),0) taxval,");
    				sql.append("ISNULL((SELECT ISNULL(OUTBOUND_GST,0) FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO),0) Tax,");				
    				sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks,");
    				sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks2,");
    				sql.append("(SELECT ISNULL(STATUS_ID,'') FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO) status_id,");
    				sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
    				sql.append("(SELECT ISNULL(PRD_DEPT_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) PRD_DEPT_ID,");
    				sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
    				sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
    				sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
    				sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_dohdr where DONO=A.DONO)),'') as empname, ");
    				sql.append("A.INVOICENO,");
    				sql.append("B.UOM,");
    				sql.append("A.users,");
    				sql.append(AlocationColumn);
    				sql.append(" (SELECT TAXTREATMENT  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) TAXTREATMENT,");
    				sql.append(" (SELECT TAXID  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) TAXID,");
    		        sql.append(" (SELECT SALES_LOCATION  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) SALES_LOCATION,PickStatus,LNSTAT, ");
    		        sql.append("ISNULL((SELECT TOP 1 ISNULL(D.DELIVERY_STATUS,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = A.ITEM and DT.LNNO = A.DOLNO),'')as DELIVERY_STATUS, ");
    		        sql.append("ISNULL((SELECT TOP 1 ISNULL(D.INTRANSIT_STATUS,'') FROM "+plant+"_DNPLHDR DN join "+plant+"_DNPLDET DT on DN.ID=DT.HDRID join "+plant+"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = A.ITEM and DT.LNNO = A.DOLNO),'')as INTRANSIT_STATUS ");
    				sql.append(" FROM (SELECT DONO,"+locationColumn +"ITEM,ITEMDESC,DOLNO,ISSUEDATE,CNAME,ISNULL(CRBY, '') AS users,ISNULL(INVOICENO, '') AS INVOICENO,SUM(PICKQTY)PICKQTY FROM ["+ plant +"_SHIPHIS] WHERE ");
    				sql.append("DONO<>'GOODSISSUE' GROUP BY DONO,"+locationColumn+"ITEM,ITEMDESC,ISSUEDATE,CNAME,INVOICENO,DOLNO,CRBY) A, ");
    				sql.append("(SELECT r.DONO,"+RlocationColumn+"r.ITEM,DOLNNO,SUM(ORDQTY)ORDQTY ,SUM(PICKQTY)PICKQTY,MAX(ISNULL(r.UNITPRICE,0) ) unitprice,s.PickStatus,s.LNSTAT,  ");
    				sql.append("ISSUEDATE,isnull(UNITMO,'') as UOM, ISNULL(INVOICENO, '') AS INVOICENO FROM ["+ plant +"_SHIPHIS] r ,[" + plant + "_" + "DODET" + "] s  WHERE s.dono=r.dono and s.DOLNNO=DOLNO and s.item=r.item and r.DONO<>'GOODSISSUE' AND STATUS='C' GROUP BY r.DONO,"+RlocationColumn+"r.ITEM,ISSUEDATE,UNITMO,INVOICENO,DOLNNO,s.PickStatus,s.LNSTAT)  B ");
    				sql.append(" WHERE A.DONO=B.DONO AND A.ITEM=b.ITEM and  A.DOLNO=B.DOLNNO AND A.ISSUEDATE=B.ISSUEDATE AND A.INVOICENO=B.INVOICENO "+ sCondition);
    				
    				   if (ht.get("DONO") != null) {
    			    	   sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
    			       } 
    				   if (ht.get("INVOICENO") != null) {
    			    	   sql.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
    			       } 
    				   if (ht.get("ITEM") != null) {
    				    	   sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
    				   }  
    				   if (ht.get("JOBNUM") != null) {
                    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  DONO=A.DONO)");
                       }
    	               if (ht.get("STATUS") != null) {
//    			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  DONO=A.DONO)");
    	            	   if(ht.get("STATUS").equals("DELIVERED")){
    	            		   sql.append(" AND a.DONO in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant +"_DNPLHDR DN join "+plant +"_DNPLDET DT on DN.ID=DT.HDRID join "+plant +"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = A.ITEM and DT.LNNO = A.DOLNO and d.DELIVERY_STATUS='" + ht.get("STATUS") + "'),'') ) ");
    	            	   }else if(ht.get("STATUS").equals("INTRANSIT")){
    	            		   sql.append(" AND a.DONO in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant +"_DNPLHDR DN join "+plant +"_DNPLDET DT on DN.ID=DT.HDRID join "+plant +"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = A.ITEM and DT.LNNO = A.DOLNO and d.INTRANSIT_STATUS='" + ht.get("STATUS") + "' and isnull(d.DELIVERY_STATUS,'')=''),'') ) ");
    	            	   }else {
    			    	   sql.append(" AND b.LNSTAT = '" + ht.get("STATUS") + "' ");
    	            	   sql.append(" AND a.DONO not in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant +"_DNPLHDR DN join "+plant +"_DNPLDET DT on DN.ID=DT.HDRID join "+plant +"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = A.ITEM and DT.LNNO = A.DOLNO and d.DELIVERY_STATUS='DELIVERED'),'') ) ");
    	            	   sql.append(" AND a.DONO not in(ISNULL((SELECT TOP 1 ISNULL(D.ORDNUM,'') FROM "+plant +"_DNPLHDR DN join "+plant +"_DNPLDET DT on DN.ID=DT.HDRID join "+plant +"_SHIPPINGHDR D on DT.HDRID = D.DNPLID where DN.DONO= A.DONO AND DN.GINO=GINO and DT.ITEM = A.ITEM and DT.LNNO = A.DOLNO and d.INTRANSIT_STATUS='INTRANSIT'),'') ) ");
    	            	   }
    			       }
    			       if (ht.get("PickStaus") != null) {
//    			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND  DONO=A.DONO)");
    			    	   sql.append(" AND b.PickStatus = '" + ht.get("PickStaus") + "' ");
    			       }
    			       if (ht.get("ORDERTYPE") != null) {
    			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  DONO=A.DONO)");
    			       }
    			       
    				   	if(ispos.equalsIgnoreCase("3"))
    				   		sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='POS' AND  DONO=A.DONO)");
    				   	else if(ispos.equalsIgnoreCase("2"))
    				   		sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE !='POS' AND  DONO=A.DONO)");
    				  
    			       if (ht.get("STATUS_ID") != null) {
    			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  DONO=A.DONO)");
    			       }
    			       if (ht.get("EMPNO") != null) {
    			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND  DONO=A.DONO)");
    			       }
    			       if (ht.get("ITEMTYPE") != null) {
    			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
    			       }
    			       if (ht.get("PRD_BRAND_ID") != null) {
    			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
    			       }
    			       if (ht.get("PRD_CLS_ID") != null) {
    			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
    			       }
    			       if (ht.get("PRD_DEPT_ID") != null) {
    			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
    			       }
    			       if (ht.get("LOC") != null || ht.get("LOC_TYPE_ID") != null || ht.get("LOC_TYPE_ID2") != null || ht.get("LOC_TYPE_ID3") != null  ) {
    			    	   sql.append(" AND A.LOC IN (SELECT LOC from [" + plant +"_SHIPHIS] WHERE LOC='" + ht.get("LOC") + "')");
    			       }
    			
    				arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
    				// End code modified by radhika for outorderwithprice on 18/12/12 		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerDOInvoiceIssueSummary:", e);
		}
		return arrList;
	}
  
		//CREATED BY NAVAS
		
		public ArrayList getCustomerTOInvoiceIssueSummary(Hashtable ht, String afrmDate,
		String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
		MovHisDAO movHisDAO = new MovHisDAO();
		movHisDAO.setmLogger(mLogger);
		if (itemDesc.length() > 0 ) {
		if (itemDesc.indexOf("%") != -1) {
		itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		}
		sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		}
		if (custname.length()>0){
		custname = StrUtils.InsertQuotes(custname);
		sCondition = sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
		}
		if (ht.size() > 0) {
		if (ht.get("CUSTTYPE") != null) {
		String custtype = ht.get("CUSTTYPE").toString();
		sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
		ht.remove("CUSTTYPE");
		}
		}
		
		String dtCondStr = " and ISNULL(a.RECVDATE,'')<>'' AND CAST((SUBSTRING(a.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
		if (afrmDate.length() > 0) {
		sCondition = sCondition +dtCondStr+ " >= '"
		+ afrmDate
		+ "' ";
		if (atoDate.length() > 0) {
		sCondition = sCondition + dtCondStr + " <= '"
		+ atoDate
		+ "' ";
		}
		} else {
		if (atoDate.length() > 0) {
		sCondition = sCondition +dtCondStr+ " <= '"
		+ atoDate
		+ "' ";
		}
		}
		sCondition = sCondition +" and (a.PONO like 'C%') ";
		if(sorttype.equalsIgnoreCase("PRODUCT"))
		{
		extraCon="GROUP BY A.GRNO,a.ITEM,a.PONO,a.CNAME,a.RECQTY,B.RECQTY,A.RECVDATE,B.UNITCOST,B.UOM,A.users),"
		+"Ranked AS ("
		+"SELECT custcode,tono,users,UOM,item,itemdesc,orderType,fromlocation,tolocation,custname,RECVDATE,trandate,jobnum,remarks,remarks2,GRNO,"
		+"qtyor,qtyac,qtypick ,qty,unitcost,issPrice,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
		+"RANK() OVER (PARTITION BY ITEM ORDER BY issPrice desc) rnk"
		+" from SORTTYPE)"
		+"SELECT * FROM Ranked ORDER BY sum(issPrice) OVER (PARTITION BY ITEM)desc";
		}
		else if(sorttype.equalsIgnoreCase("CUSTOMER"))
		{
		extraCon="GROUP BY A.GRNO,a.CNAME,A.PONO,a.ITEM,a.RECQTY,B.RECQTY,A.RECVDATE,B.UNITCOST,B.UOM,A.users),"
		+"Ranked AS ("
		+"SELECT custcode,tono,item,users,UOM,itemdesc,orderType,fromlocation,tolocation,custname,RECVDATE,trandate,jobnum,remarks,remarks2,GRNO,"
		+"qtyor,qtyac,qtypick ,qty,unitcost,issPrice,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status,Tax,empname,taxval,TAXTREATMENT,SALES_LOCATION,"
		+"RANK() OVER (PARTITION BY custcode ORDER BY issPrice desc) rnk"
		+" from SORTTYPE)"
		+"SELECT * FROM Ranked ORDER BY sum(issPrice) OVER (PARTITION BY custcode)desc";
		
		}
		
		else{
		extraCon = " order by A.CNAME, A.PONO,A.ITEM,CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date)";
		}
		if(!sorttype.equalsIgnoreCase("")){
		sql.append(" WITH SORTTYPE AS( ");}
		sql.append("SELECT A.PONO as tono,A.ITEM as item,ISNULL(A.RECQTY,0) as qtypick,ISNULL(B.RECQTY,0) as qty,A.RECVDATE as issuedate,");
		sql.append("ISNULL(B.UNITCOST,0) as unitcost,(ISNULL(B.RECQTY,0)*ISNULL(B.UNITCOST,0))issPrice,A.CNAME as custname,");
		sql.append("CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
		sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_TODET] WHERE ITEM=A.ITEM and TONO=A.PONO and UNITMO=B.UOM GROUP BY ITEM,TONO,UNITMO),0) qtyor,");
		sql.append("ISNULL((SELECT SUM(QTYAC) FROM ["+ plant +"_TODET] WHERE ITEM=A.ITEM and TONO=A.PONO and UNITMO=B.UOM GROUP BY ITEM,TONO,UNITMO),0) qtyac,");
		sql.append("(SELECT CustCode FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) custcode,");
		sql.append("(SELECT ORDERTYPE FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) orderType,");
		sql.append("(SELECT FROMWAREHOUSE FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) fromlocation,");
		sql.append("(SELECT TOWAREHOUSE FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) tolocation,");
		sql.append("(SELECT JOBNUM FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) jobNum,");
		sql.append(" ISNULL((((ISNULL(B.RECQTY,0)*ISNULL(B.unitcost,0)) * (SELECT ISNULL(CONSIGNMENT_GST,0) FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO))/100),0) taxval,");
		sql.append("ISNULL((SELECT ISNULL(CONSIGNMENT_GST,0) FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO),0) Tax,");
		sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.PONO) remarks,");
		sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.PONO) remarks2,");
		sql.append("(SELECT ISNULL(STATUS,'') FROM ["+ plant +"_TOHDR] WHERE TONO=A.PONO) status_id,");
		sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
		sql.append("(SELECT ISNULL(PRD_DEPT_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) PRD_DEPT_ID,");
		sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
		sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
		sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
		sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_tohdr where TONO=A.PONO)),'') as empname, ");
		sql.append("A.GRNO,");
		sql.append("B.UOM,");
		sql.append("A.users,");
		sql.append(" (SELECT TAXTREATMENT FROM [" + plant + "_" + "TOHDR" + "] WHERE TONO=A.PONO) TAXTREATMENT,");
		sql.append(" (SELECT SALES_LOCATION FROM [" + plant + "_" + "TOHDR" + "] WHERE TONO=A.PONO) SALES_LOCATION ");
		sql.append(" FROM (SELECT PONO,ITEM,ITEMDESC,LNNO,RECVDATE,CNAME,ISNULL(CRBY, '') AS users,ISNULL(GRNO, '') AS GRNO,SUM(RECQTY) RECQTY FROM ["+ plant +"_RECVDET] ");
		sql.append(" GROUP BY PONO,ITEM,ITEMDESC,RECVDATE,CNAME,GRNO,LNNO,CRBY) A, ");
		sql.append("(SELECT r.PONO,r.ITEM,TOLNNO,SUM(ORDQTY)ORDQTY ,SUM(RECQTY)RECQTY,MAX(ISNULL(r.UNITCOST,0) ) unitcost, ");
		sql.append("RECVDATE,isnull(UNITMO,'') as UOM, ISNULL(GRNO, '') AS GRNO FROM ["+ plant +"_RECVDET] r ,[" + plant + "_" + "TODET" + "] s WHERE s.tono=r.pono and s.TOLNNO=r.LNNO and s.item=r.item AND r.RECEIVESTATUS='C' GROUP BY r.PONO,r.ITEM,RECVDATE,UNITMO,GRNO,TOLNNO) B ");
		sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM and A.LNNO=B.TOLNNO AND A.RECVDATE=B.RECVDATE AND A.GRNO=B.GRNO "+ sCondition);
		
		if (ht.get("TONO") != null) {
		sql.append(" AND A.PONO = '" + ht.get("TONO") + "'");
		}
		if (ht.get("INVOICENO") != null) {
		sql.append(" AND A.GRNO = '" + ht.get("GRNO") + "'");
		}
		if (ht.get("ITEM") != null) {
		sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
		}
		if (ht.get("JOBNUM") != null) {
		sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND TONO=A.PONO)");
		}
		if (ht.get("STATUS") != null) {
		sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND TONO=A.PONO)");
		}
		if (ht.get("PickStaus") != null) {
		sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND TONO=A.PONO)");
		}
		if (ht.get("ORDERTYPE") != null) {
		sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND TONO=A.PONO)");
		}
		if (ht.get("FROMWAREHOUSE") != null) {
			sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE FROMWAREHOUSE ='" + ht.get("FROMWAREHOUSE") + "' AND TONO=A.PONO)");
			}
		if (ht.get("TOWAREHOUSE") != null) {
			sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE TOWAREHOUSE ='" + ht.get("TOWAREHOUSE") + "' AND TONO=A.PONO)");
			}
		//STATUSID
		if (ht.get("STATUS") != null) {
		sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND TONO=A.PONO)");
		}
		if (ht.get("EMPNO") != null) {
		sql.append(" AND A.PONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND TONO=A.PONO)");
		}
		if (ht.get("ITEMTYPE") != null) {
		sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND ITEM=A.ITEM)");
		}
		if (ht.get("PRD_BRAND_ID") != null) {
		sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND ITEM=A.ITEM)");
		}
		if (ht.get("PRD_CLS_ID") != null) {
		sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND ITEM=A.ITEM)");
		}
		if (ht.get("PRD_DEPT_ID") != null) {
			sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND ITEM=A.ITEM)");
			}
		arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
		// End code modified by radhika for outorderwithprice on 18/12/12
		} catch (Exception e) {
		this.mLogger.exception(this.printLog,
		"Exception :repportUtil :: getCustomerTOInvoiceIssueSummary:", e);
		}
		return arrList;
		}
		//END BY NAVAS

  public ArrayList getCustomerDOInvoiceIssueSummaryByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype,String ispos) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		     if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
           if (custname.length()>0){
                	custname = StrUtils.InsertQuotes(custname);
                 	sCondition =  sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
              }
           if (ht.size() > 0) {
				if (ht.get("CUSTTYPE") != null) {
					String custtype = ht.get("CUSTTYPE").toString();
					sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
					ht.remove("CUSTTYPE");
				}
			}
              
           
           
           boolean isLocationProvided = false;
      		if (ht.size() > 0) {
      			if (ht.get("LOC") != null && ht.get("LOC")!="") {
      				String loc = ht.get("LOC").toString();
      				sCondition = sCondition+" AND A.LOC like '%"+loc+"%'";			
      				ht.remove("LOC");	
      				isLocationProvided = true;
      	        }
      			if (ht.get("LOC_TYPE_ID") != null && ht.get("LOC_TYPE_ID") != "") {
      				String loctypeid = ht.get("LOC_TYPE_ID").toString();
      				sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
      				ht.remove("LOC_TYPE_ID");
      				isLocationProvided = true;
      			}
      			if (ht.get("LOC_TYPE_ID2") != null && ht.get("LOC_TYPE_ID2") != "") {
      				String loctypeid2 = ht.get("LOC_TYPE_ID2").toString();
      				sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )";
      				ht.remove("LOC_TYPE_ID2");
      				isLocationProvided = true;
      			}
      			if (ht.get("LOC_TYPE_ID3") != null && ht.get("LOC_TYPE_ID3") != "") {
      				String loctypeid3 = ht.get("LOC_TYPE_ID3").toString();
      				sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,loctypeid3)+" )";
      				ht.remove("LOC_TYPE_ID3");
      				isLocationProvided = true;
      			}
      		}
      		String AlocationColumn = "";
      		String RlocationColumn = "";
      		String locationColumn = "";
      			if (isLocationProvided) {
      				AlocationColumn = "A.loc,";
      				RlocationColumn = "r.loc,";
      				locationColumn = "loc,";
      			}
           
           
		    String dtCondStr =    " and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
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
                      sCondition = sCondition +" and (a.DONO like 'S%' OR a.DONO like 'TI%') and  a.DONO not like 'SM%' ";
                      if(sorttype.equalsIgnoreCase("PRODUCT"))
                      {       
                      		extraCon="GROUP BY  A.INVOICENO,a.ITEM,a.DONO,a.CNAME,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM,A.users),"
                        		  +"Ranked AS ("
                        		  +"SELECT custcode,dono,users,UOM,item,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                        		  +"qtyor ,qtypick ,qty,unitprice,issPrice,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,TAXTREATMENT,SALES_LOCATION,"
                        		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
                        		  +" from SORTTYPE)"
                        		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
                      }
                      else if(sorttype.equalsIgnoreCase("CUSTOMER"))
                      {
                      		extraCon="GROUP BY A.INVOICENO,a.CNAME,A.DONO,a.ITEM,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM,A.users),"
                          		  +"Ranked AS ("
                          		  +"SELECT custcode,dono,users,UOM,item,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                          		  +"qtyor ,qtypick ,qty,unitprice,issPrice,PRD_DEPT_ID,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,TAXTREATMENT,SALES_LOCATION,"
                          		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
                          		  +" from SORTTYPE)"
                          		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
                    
                      }    
                      
                      else{    
                    	  	extraCon = " order by A.CNAME, A.DONO,A.ITEM,CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date)";
                      }
                if(!sorttype.equalsIgnoreCase("")){      
                sql.append(" WITH SORTTYPE AS( ");}
                sql.append("SELECT A.DONO as dono,A.ITEM as item,ISNULL(A.PICKQTY,0) as qtypick,ISNULL(B.PICKQTY,0) as qty,A.ISSUEDATE as issuedate,");
				sql.append("ISNULL(B.UNITPRICE,0) as unitprice,(ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0))issPrice,A.CNAME as custname,");
				sql.append("CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date) as trandate,");
				sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_DODET] WHERE ITEM=A.ITEM and DONO=A.DONO and UNITMO=B.UOM  GROUP BY ITEM,DONO,UNITMO),0) qtyor,");
				sql.append("(SELECT CustCode  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) custcode,");
				sql.append("(SELECT ORDERTYPE  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) orderType,");
				sql.append("(SELECT JOBNUM  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) jobNum,");
				sql.append(" ISNULL((((ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0)) * (SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM))/100),0)  taxval,");
				sql.append("ISNULL((SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM),0)  Tax,");
				sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks,");
				sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks2,");
				sql.append("(SELECT ISNULL(STATUS_ID,'') FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO) status_id,");
				sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
				sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
				sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
				sql.append("(SELECT ISNULL(PRD_DEPT_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) PRD_DEPT_ID,");
				sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
				sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_dohdr where DONO=A.DONO)),'') as empname, ");
				sql.append("A.INVOICENO,");
				sql.append("B.UOM,");
				sql.append("A.users,");
				sql.append(AlocationColumn);
				sql.append(" (SELECT TAXTREATMENT  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) TAXTREATMENT,");
		        sql.append(" (SELECT SALES_LOCATION  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) SALES_LOCATION ");
				sql.append(" FROM (SELECT DONO,DOLNO,"+locationColumn +"ITEM,ITEMDESC,ISSUEDATE,CNAME,ISNULL(CRBY, '') AS users,ISNULL(INVOICENO, '') AS INVOICENO,SUM(PICKQTY)PICKQTY FROM ["+ plant +"_SHIPHIS] WHERE ");
				sql.append("DONO<>'GOODSISSUE' GROUP BY DONO,"+locationColumn +"ITEM,ITEMDESC,ISSUEDATE,CNAME,INVOICENO,DOLNO,CRBY) A, ");
				sql.append("(SELECT r.DONO,"+RlocationColumn+"r.ITEM,DOLNNO,isnull(UNITMO,'') as UOM,SUM(ORDQTY)ORDQTY ,SUM(PICKQTY)PICKQTY,MAX(ISNULL(r.UNITPRICE,0) ) unitprice, ");
				sql.append("ISSUEDATE, ISNULL(INVOICENO, '') AS INVOICENO FROM ["+ plant +"_SHIPHIS]r  , [" + plant + "_" + "DODET" + "] s  WHERE s.dono=r.dono and s.DOLNNO=DOLNO and s.item=r.item and r.DONO<>'GOODSISSUE' AND STATUS='C' GROUP BY r.DONO,"+RlocationColumn+"r.ITEM,UNITMO,ISSUEDATE,INVOICENO,DOLNNO)  B ");
				sql.append(" WHERE A.DONO=B.DONO AND A.DOLNO=B.DOLNNO and A.ITEM=b.ITEM  AND A.ISSUEDATE=B.ISSUEDATE AND A.INVOICENO=B.INVOICENO "+ sCondition);
				
				   if (ht.get("DONO") != null) {
			    	   sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
			       } 
				   if (ht.get("INVOICENO") != null) {
			    	   sql.append(" AND A.INVOICENO = '" + ht.get("INVOICENO") + "'");
			       } 
				   if (ht.get("ITEM") != null) {
				    	   sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
				   }  
				   if (ht.get("JOBNUM") != null) {
                	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  DONO=A.DONO)");
                   }
	               if (ht.get("STATUS") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("PickStaus") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("ORDERTYPE") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  DONO=A.DONO)");
			       }
			       
			       if(ispos.equalsIgnoreCase("3"))
				   		sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='POS' AND  DONO=A.DONO)");
				   	else if(ispos.equalsIgnoreCase("2"))
				   		sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE !='POS' AND  DONO=A.DONO)");
				  
			       if (ht.get("STATUS_ID") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("EMPNO") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("ITEMTYPE") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_BRAND_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_CLS_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_DEPT_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("LOC") != null || ht.get("LOC_TYPE_ID") != null || ht.get("LOC_TYPE_ID2") != null || ht.get("LOC_TYPE_ID3") != null  ) {
			    	   sql.append(" AND A.LOC IN (SELECT LOC from [" + plant +"_SHIPHIS] WHERE LOC='" + ht.get("LOC") + "')");
			       }
			
				arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
				// End code modified by radhika for outorderwithprice on 18/12/12 	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerDOInvoiceIssueSummary:", e);
		}
		return arrList;
	}
  
  public ArrayList getEstimateOrderSummaryList(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc, String custname,String viewby) {
				
//		String fromdate="",todate="";
		ArrayList arrList = new ArrayList();
//		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		 	
					String sCondition1 = "";
			 if (itemDesc.length() > 0 ) {
			    	if (itemDesc.indexOf("%") != -1) {
  		        		itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
  		        	}
  		        	sCondition1 = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
			    }
              
             if (custname.length()>0){
            		 custname = StrUtils.InsertQuotes(custname);
            		/* sCondition1 = sCondition1 + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;*/
            		 sCondition1 = sCondition1 + "  A.CUSTNAME LIKE '%"+custname+"%' AND " ;
               }
				
		     String dtCondStr="";
			 String extraCon="";
			 if(dirType.equalsIgnoreCase("ESTIMATEISSUE"))
			 	{
					 if(viewby.equals("ByExpiryDate")){
						 dtCondStr =   " ISNULL(A.EXPIREDATE,'')<>'' AND  CAST((SUBSTRING(a.EXPIREDATE, 7, 4) + '-' + SUBSTRING(a.EXPIREDATE, 4, 2) + '-' + SUBSTRING(a.EXPIREDATE, 1, 2)) AS date) " ;
						 extraCon=" order by CAST((SUBSTRING(a.EXPIREDATE, 7, 4) + SUBSTRING(a.EXPIREDATE, 4, 2) + SUBSTRING(a.EXPIREDATE, 1, 2)) AS date) ";
					 }
					 else
					 {
						 dtCondStr =    " and ISNULL(e.CollectionDate,'')<>'' AND  CAST((SUBSTRING(e.CollectionDate, 7, 4) + '-' + SUBSTRING(e.CollectionDate, 4, 2) + '-' + SUBSTRING(e.CollectionDate, 1, 2)) AS date)";
						 extraCon=" order by CAST((SUBSTRING(e.CollectionDate, 7, 4) + SUBSTRING(e.CollectionDate, 4, 2) + SUBSTRING(e.CollectionDate, 1, 2)) AS date) ";
					 }
			 	}
			 else
			 	{
				 	if(viewby.equals("ByExpiryDate")){
				 		dtCondStr =   " ISNULL(A.EXPIREDATE,'')<>'' AND  CAST((SUBSTRING(a.EXPIREDATE, 7, 4) + '-' + SUBSTRING(a.EXPIREDATE, 4, 2) + '-' + SUBSTRING(a.EXPIREDATE, 1, 2)) AS date) " ;
				 		extraCon=" order by CAST((SUBSTRING(a.EXPIREDATE, 7, 4) + SUBSTRING(a.EXPIREDATE, 4, 2) + SUBSTRING(a.EXPIREDATE, 1, 2)) AS date) ";
				 	}
				 	else
				 	{
						dtCondStr =    " ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
						extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
					}
				 
			 	}
		             
				    if (afrmDate.length() > 0) {
		                	sCondition1 = sCondition1 + dtCondStr + "  >= '" 
		    						+ afrmDate
		    						+ "'  ";
		    				if (atoDate.length() > 0) {
		    					sCondition1 = sCondition1+" AND "+dtCondStr+ " <= '" 
		    					+ atoDate
		    					+ "'  ";
		    				}
		    			} else {
		    				if (atoDate.length() > 0) {
		    					sCondition1 = sCondition1 +dtCondStr+ " <= '" 
		    					+ atoDate
		    					+ "'  ";
		    				}
		    			}   
			
			if (dirType.equalsIgnoreCase("ESTIMATE")) {
				String prdbrand="",prdcls="",prdtype="",prddep="";
				if (ht.size() > 0) {
					if (ht.get("C.PRD_BRAND_ID") != null) {
						prdbrand = ht.get("C.PRD_BRAND_ID").toString();
					}
					if (ht.get("C.PRD_CLS_ID") != null) {
						prdcls = ht.get("C.PRD_CLS_ID").toString();
					}
					if (ht.get("C.ITEMTYPE") != null) {
						prdtype = ht.get("C.ITEMTYPE").toString();
					}
					if (ht.get("C.PRD_DEPT_ID") != null) {
						prddep = ht.get("C.PRD_DEPT_ID").toString();
					}

				}
				if(prdbrand.length()>0){
					sCondition1 = sCondition1+" and b.item in (select item from "+plant+"_ITEMMST where PRD_BRAND_ID='"+prdbrand+"')";
					ht.remove("C.PRD_BRAND_ID");
				}
				if(prdcls.length()>0){
					sCondition1 = sCondition1+" and b.item in (select item from "+plant+"_ITEMMST where PRD_CLS_ID='"+prdcls+"')";
					ht.remove("C.PRD_CLS_ID");
				}
				
				if(prdtype.length()>0){
					sCondition1 = sCondition1+" and b.item in (select item from "+plant+"_ITEMMST where ITEMTYPE='"+prdtype+"')";
					ht.remove("C.ITEMTYPE");
				}
				
				if(prddep.length()>0){
					sCondition1 = sCondition1+" and b.item in (select item from "+plant+"_ITEMMST where PRD_DEPT_ID='"+prddep+"')";
					ht.remove("C.PRD_DEPT_ID");
				}
							
				String aQuery = "select distinct b.estno,b.estlnno,isnull(a.Remark1,'') as remarks,a.jobNum,isnull(d.DONO,'') as dono,isnull(a.CRBY,'') as users,a.custname,b.item,b.itemdesc,b.unitprice,a.collectionDate TRANDATE,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as CollectionDate,CAST((SUBSTRING(a.EXPIREDATE, 7, 4) + SUBSTRING(a.EXPIREDATE, 4, 2) + SUBSTRING(a.EXPIREDATE, 1, 2)) AS date) expdate,isnull(a.status,'') as status,b.qtyor,isnull(d.qtyor,0) as qtyis,isnull(b.UNITMO,'') as UOM,isnull(d.lnstat,'')as dostatus,"

						+"isnull((select isnull(prd_brand_id,'') from["+plant+"_ITEMMST] where item=b.ITEM),'') as prd_brand_id,"
						+"isnull((select isnull(prd_cls_id,'') from["+plant+"_ITEMMST] where item=b.ITEM),'') as prd_cls_id,"
						+"isnull((select isnull(PRD_DEPT_ID,'') from["+plant+"_ITEMMST] where item=b.ITEM),'') as PRD_DEPT_ID,"
						+"isnull((select isnull(itemtype,'') from["+plant+"_ITEMMST] where item=b.ITEM),'') as itemtype,"
						+"isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname,isnull(a.expiredate,'')as expiredate, "
						+" CASE WHEN ISNULL(A.EXPIREDATE,'')='' then '#000000'"  
	                    + " WHEN SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  <= CONVERT(VARCHAR(8), GETDATE(), 112) then '#FF0000'"  
	                    + " else '#000000' END AS COLORCODE,isnull(remark3,'') remarks3,isnull(A.TAXTREATMENT,'') as TAXTREATMENT from ("
						+ "["
						+ plant
						+ "_"
						+ "esthdr] a join "
						+ "["
						+ plant
						+ "_"
						+ "estdet] b on a.estno=b.estno) left join"
						+ "["
						+ plant
						+ "_"
						+"dodet] d on d.estno=b.estno and d.estlnno=b.estlnno where " + sCondition1 ;
						

				
				arrList = movHisDAO.selectForReport(aQuery, ht,extraCon+",b.estno,b.estlnno,isnull(d.DONO,''),b.item");
			}
			
			if (dirType.equalsIgnoreCase("ESTIMATEISSUE")) {
									
				String aQuery = "select distinct b.estno,b.estlnno,isnull(a.Remark1,'') as remarks,a.jobNum,isnull(d.DONO,'') as dono,isnull(a.CRBY,'') as users,a.custname,b.item,b.itemdesc,b.unitprice,e.collectionDate TRANDATE,CAST((SUBSTRING(e.CollectionDate, 7, 4) + SUBSTRING(e.CollectionDate, 4, 2) + SUBSTRING(e.CollectionDate, 1, 2)) AS date) as CollectionDate,CAST((SUBSTRING(a.EXPIREDATE, 7, 4) + SUBSTRING(a.EXPIREDATE, 4, 2) + SUBSTRING(a.EXPIREDATE, 1, 2)) AS date) expdate,isnull(a.status,'') as status,b.qtyor,d.qtyor as qtyis,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(d.lnstat,'')as dostatus,isnull((select isnull(fname,'') from["+plant+"_EMP_MST] where EMPNO=a.EMPNO),'') as empname,isnull(b.UNITMO,'')as UOM,isnull(a.expiredate,'')as expiredate, "+

						" CASE WHEN ISNULL(A.EXPIREDATE,'')='' then '#000000'" + 
	                     " WHEN SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  <= CONVERT(VARCHAR(8), GETDATE(), 112) then '#FF0000'" + 
	                     " else '#000000' END AS COLORCODE,isnull(A.TAXTREATMENT,'') as TAXTREATMENT from "
						+ "["
						+ plant
						+ "_"
						+ "esthdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "estdet] b,"
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c,"
						+ "["
						+ plant
						+ "_"
						+"dodet] d,"
						+ "["
						+ plant
						+ "_"
						+"dohdr] e where a.estno=b.estno and b.ITEM=c.item and d.estlnno=b.estlnno and a.ESTNO=e.estno and d.DONO=e.dono " + (sCondition1.trim().startsWith("and ") ? "" : " and ") + sCondition1; 
				arrList = movHisDAO.selectForReport(aQuery, ht,extraCon+",b.estno,isnull(d.DONO,''),b.item");
			}
			
			if(dirType.equalsIgnoreCase("ESTIMATEPRODUCTREMARKS")) {
				
		    	extraCon=" order by CollectionDate,b.estno,d.id_remarks";
				String aQuery = "select distinct b.estno,b.estlnno,isnull(a.Remark1,'') as remarks,isnull(a.Remark3,'') as remarks2,b.item,isnull(b.UNITMO,'')as UOM,isnull(c.itemdesc,'') " 
				       +" itemdesc,isnull(c.Remark1,'') as DetailItemDesc, isnull(d.remarks,'') prdremarks,d.id_remarks," +
				       "CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as CollectionDate from "
						+ "["
						+ plant
						+ "_"
						+ "esthdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "estdet] b,"
						+ "["
						+ plant
						+ "_"
						+"ITEMMST] c,"
						+ "["
	  					+ plant
	  					+ "_"
	  					+"ESTIMATE_REMARKS] d where " + sCondition1 + "and a.estno=b.estno and b.ITEM=c.item and b.estlnno=d.estlnno and b.estno = d.estno  "  ;

				
				arrList = movHisDAO.selectForReport(aQuery, ht,extraCon);
			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
	"Exception :repportUtil :: getEstimateOrderSummaryList:", e);		}
		return arrList;
	}
  
  public ArrayList getCustomerDOAvgCostIssueSummary(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype,String currencyid,String baseCurrency) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",fdate="",tdate="";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		     if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
         if (custname.length()>0){
              	custname = StrUtils.InsertQuotes(custname);
               	sCondition =  sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
            }
         if (ht.size() > 0) {
				if (ht.get("CUSTTYPE") != null) {
					String custtype = ht.get("CUSTTYPE").toString();
					sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
					ht.remove("CUSTTYPE");
				}
			}
                       
		    String dtCondStr =    " and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
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
                    sCondition=sCondition+" and (A.DONO like 'S%' OR A.DONO like 'TI%') and A.DONO not like 'SM%' ";
                    // Date range to get avg cost
                    if (afrmDate.length()>5)
                        fdate   = afrmDate.substring(0,4)+ afrmDate.substring(5,7)+afrmDate.substring(8,10);
                        
                    if (atoDate.length()>5)
                         tdate  = atoDate.substring(0,4)+ atoDate.substring(5,7)+atoDate.substring(8,10); 
                    String sRecvCondition=""; 
                    if (fdate.length() > 0) {
                            sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)  >= '" + fdate
                                            + "'  ";
                            if (tdate.length() > 0) {
                                    sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + tdate
                                                    + "'  ";
                            }
                    } else {
                            if (tdate.length() > 0) {
                                    sRecvCondition = sRecvCondition + " and substring(CRAT,1,8) <= '" + tdate
                                                    + "'  ";
                            }
                    }
                    
                    String ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                            "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                            "   WHERE  CURRENCYID=ISNULL((SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO),'"+baseCurrency+"')) AS DECIMAL(20,4)) ) ";

                    StringBuffer sqlavgcost = new StringBuffer(" (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] WHERE ITEM=A.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB')  AND UNITCOST >0 AND UNITCOST IS NOT NULL "+sRecvCondition+")>0  THEN ");
                    sqlavgcost.append(" (Select ISNULL(CAST(ISNULL(SUM(C.UNITCOST),0)/SUM(C.RECQTY) AS DECIMAL(20,6)),0) AS AVERGAGE_COST from ");
                    sqlavgcost.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,6)) ");
                    sqlavgcost.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO)) AS DECIMAL(20,6)) ");
                    sqlavgcost.append("   * RECQTY AS DECIMAL(20,6)) AS UNITCOST ");
                    sqlavgcost.append("   from "+plant+"_RECVDET R where item =A.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> ''  AND TRAN_TYPE ='IB' AND ORDQTY >0 " + sRecvCondition +"  ) C )  ");
                    sqlavgcost.append("   ELSE   CAST(((SELECT sum(COST) FROM  ["+plant+"_itemmst] where ITEM=A.ITEM)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,6))   END ) AS AVERAGE_COST   ");

                    
                    if(sorttype.equalsIgnoreCase("PRODUCT"))
                    {       
                    		extraCon="GROUP BY a.ITEM,a.DONO,a.CNAME,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM),"
                      		  +"Ranked AS ("
                      		  +"SELECT custcode,dono,item,itemdesc,UOM,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                      		  +"qtyor ,qtypick ,qty,unitprice,issPrice,prd_brand_id,PRD_DEPT_ID,prd_cls_id,itemtype,status_id,Tax,empname,AVERAGE_COST,TAXTREATMENT,SALES_LOCATION,"
                      		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
                      		  +" from SORTTYPE)"
                      		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
                    }
                    else if(sorttype.equalsIgnoreCase("CUSTOMER"))
                    {
                    		extraCon="GROUP BY a.CNAME,A.DONO,a.ITEM,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM),"
                        		  +"Ranked AS ("
                        		  +"SELECT custcode,dono,item,UOM,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                        		  +"qtyor ,qtypick ,qty,unitprice,issPrice,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status_id,Tax,empname,AVERAGE_COST,TAXTREATMENT,SALES_LOCATION,"
                        		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
                        		  +" from SORTTYPE)"
                        		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
                  
                    }    
                    
                    else{    
                  	  	extraCon = " order by A.CNAME, A.DONO,A.ITEM,CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date)";
                    }
              if(!sorttype.equalsIgnoreCase("")){      
                sql.append(" WITH SORTTYPE AS( ");}
                sql.append("SELECT A.DONO as dono,A.ITEM as item,ISNULL(A.PICKQTY,0) as qtyPick,ISNULL(B.PICKQTY,0) as qty,A.ISSUEDATE as issuedate,");
				sql.append("ISNULL(B.UNITPRICE,0) as unitprice,(ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0))issPrice,A.CNAME as custname,");
				sql.append("CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date) as trandate,");
				sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_DODET] WHERE ITEM=A.ITEM AND DONO=A.DONO and UNITMO=B.UOM GROUP BY DONO,ITEM,UNITMO),0) qtyor,");
				sql.append("(SELECT CustCode  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) custcode,");
				sql.append("(SELECT ORDERTYPE  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) orderType,");
				sql.append("(SELECT JOBNUM  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) jobNum,");
				sql.append(" ISNULL((((ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0)) * (SELECT ISNULL(OUTBOUND_GST,0) FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO))/100),0) taxval,");
				sql.append("ISNULL((SELECT ISNULL(OUTBOUND_GST,0) FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO),0) Tax,");
				sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks,");
				sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks2,");
				sql.append("(SELECT ISNULL(STATUS_ID,'') FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO) status_id,");
				sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
				sql.append("(SELECT ISNULL(PRD_DEPT_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) PRD_DEPT_ID,");
				sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
				sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
				sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
				sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_dohdr where DONO=A.DONO)),'') as empname, ");
				sql.append("B.UOM,");
				sql.append(" (SELECT TAXTREATMENT  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) TAXTREATMENT,");
		        sql.append(" (SELECT SALES_LOCATION  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) SALES_LOCATION, ");
				sql.append(sqlavgcost.toString());
				sql.append(" FROM (SELECT DONO,ITEM,ITEMDESC,ISSUEDATE,CNAME,SUM(PICKQTY)PICKQTY FROM ["+ plant +"_SHIPHIS] WHERE ");
				sql.append("DONO<>'GOODSISSUE' GROUP BY DONO,ITEM,ITEMDESC,ISSUEDATE,CNAME) A, ");
				sql.append("(SELECT r.DONO,r.ITEM,isnull(UNITMO,'') as UOM,SUM(ORDQTY)ORDQTY ,SUM(PICKQTY)PICKQTY,MAX(ISNULL(r.UNITPRICE,0) ) unitprice, ");
				sql.append("ISSUEDATE FROM ["+ plant +"_SHIPHIS]r  , [" + plant + "_" + "DODET" + "] s    WHERE s.dono=r.dono and s.DOLNNO=DOLNO and s.item=r.item and r.DONO<>'GOODSISSUE' AND STATUS='C' GROUP BY r.DONO,r.ITEM,UNITMO,ISSUEDATE)  B ");
				sql.append(" WHERE A.DONO=B.DONO AND A.ITEM=b.ITEM  AND A.ISSUEDATE=B.ISSUEDATE "+ sCondition);
				
				   if (ht.get("DONO") != null) {
			    	   sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
			       } 
				   if (ht.get("ITEM") != null) {
				    	   sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
				   }  
				   if (ht.get("JOBNUM") != null) {
              	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  DONO=A.DONO)");
                 }
	               if (ht.get("STATUS") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("PickStaus") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("ORDERTYPE") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  DONO=A.DONO)");
			       }
				  
			       if (ht.get("STATUS_ID") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("EMPNO") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("ITEMTYPE") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_BRAND_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_CLS_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_DEPT_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
			       }
			
				arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
				// End code modified by radhika for outorderwithprice on 18/12/12 	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerDOInvoiceIssueSummary:", e);
		}
		return arrList;
	}
//FEB23 CREATED BY NAVAS
  public ArrayList getCustomerTOAvgCostIssueSummaryByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype,String currencyid,String baseCurrency) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",fdate="",tdate="";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		     if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
     if (custname.length()>0){
          	custname = StrUtils.InsertQuotes(custname);
           	sCondition =  sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
        }
     if (ht.size() > 0) {
				if (ht.get("CUSTTYPE") != null) {
					String custtype = ht.get("CUSTTYPE").toString();
					sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
					ht.remove("CUSTTYPE");
				}
			}
                   
		    String dtCondStr =    " and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
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
                // Date range to get avg cost
                if (afrmDate.length()>5)
                    fdate   = afrmDate.substring(0,4)+ afrmDate.substring(5,7)+afrmDate.substring(8,10);
                    
                if (atoDate.length()>5)
                     tdate  = atoDate.substring(0,4)+ atoDate.substring(5,7)+atoDate.substring(8,10); 
                String sRecvCondition=""; 
                if (fdate.length() > 0) {
                        sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)  >= '" + fdate
                                        + "'  ";
                        if (tdate.length() > 0) {
                                sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + tdate
                                                + "'  ";
                        }
                } else {
                        if (tdate.length() > 0) {
                                sRecvCondition = sRecvCondition + " and substring(CRAT,1,8) <= '" + tdate
                                                + "'  ";
                        }
                }
                
                String ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                        "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                        "   WHERE  CURRENCYID=ISNULL((SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO),'"+baseCurrency+"')) AS DECIMAL(20,6)) ) ";

                StringBuffer sqlavgcost = new StringBuffer(" (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] WHERE ITEM=A.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB')  AND UNITCOST >0 AND UNITCOST IS NOT NULL "+sRecvCondition+")>0  THEN ");
                sqlavgcost.append(" (Select ISNULL(CAST(ISNULL(SUM(C.UNITCOST),0)/SUM(C.RECQTY) AS DECIMAL(20,6)),0) AS AVERGAGE_COST from ");
                sqlavgcost.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,6)) ");
                sqlavgcost.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO)) AS DECIMAL(20,6)) ");
                sqlavgcost.append("   * RECQTY AS DECIMAL(20,6)) AS UNITCOST ");
                sqlavgcost.append("   from "+plant+"_RECVDET R where item =A.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> ''  AND TRAN_TYPE ='IB' AND ORDQTY >0 " + sRecvCondition +"  ) C )  ");
                sqlavgcost.append("   ELSE   CAST(((SELECT sum(COST) FROM  ["+plant+"_itemmst] where ITEM=A.ITEM)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4))   END ) AS AVERAGE_COST   ");

                
                if(sorttype.equalsIgnoreCase("PRODUCT"))
                {       
                		extraCon="GROUP BY a.ITEM,a.TONO,a.CNAME,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM),"
                  		  +"Ranked AS ("
                  		  +"SELECT custcode,tono,item,UOM,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                  		  +"qtyor ,qtypick ,qty,unitprice,issPrice,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,AVERAGE_COST,TAXTREATMENT,SALES_LOCATION,"
                  		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
                  		  +" from SORTTYPE)"
                  		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
                }
                else if(sorttype.equalsIgnoreCase("CUSTOMER"))
                {
                		extraCon="GROUP BY a.CNAME,A.TONO,a.ITEM,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM),"
                    		  +"Ranked AS ("
                    		  +"SELECT custcode,tono,item,UOM,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                    		  +"qtyor ,qtypick ,qty,unitprice,issPrice,prd_brand_id,prd_cls_id,itemtype,status_id,Tax,empname,AVERAGE_COST,TAXTREATMENT,SALES_LOCATION,"
                    		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
                    		  +" from SORTTYPE)"
                    		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
              
                }    
                
                else{    
              	  	extraCon = " order by A.CNAME, A.TONO,A.ITEM,CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date)";
                }
          if(!sorttype.equalsIgnoreCase("")){      
            sql.append(" WITH SORTTYPE AS( ");}
            sql.append("SELECT A.TONO as tono,A.ITEM as item,ISNULL(A.PICKQTY,0) as qtyPick,ISNULL(B.PICKQTY,0) as qty,A.ISSUEDATE as issuedate,");
				sql.append("ISNULL(B.UNITPRICE,0) as unitprice,(ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0))issPrice,A.CNAME as custname,");
				sql.append("CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date) as trandate,");
				sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_TODET] WHERE ITEM=A.ITEM AND TONO=A.TONO GROUP BY TONO,ITEM),0) qtyor,");
				sql.append("(SELECT CustCode  FROM ["+ plant +"_TOHDR] WHERE TONO=A.TONO) custcode,");
				sql.append("(SELECT ORDERTYPE  FROM ["+ plant +"_TOHDR] WHERE TONO=A.TONO) orderType,");
				sql.append("(SELECT JOBNUM  FROM ["+ plant +"_TOHDR] WHERE TONO=A.TONO) jobNum,");
				sql.append(" ISNULL((((ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0)) * (SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM))/100),0) taxval,");
				sql.append("ISNULL((SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM),0) Tax,");
				sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.TONO) remarks,");
				sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_TOHDR] R WHERE TONO=A.TONO) remarks2,");
				sql.append("(SELECT ISNULL(STATUS_ID,'') FROM ["+ plant +"_TOHDR]  WHERE TONO=A.TONO) status_id,");
				sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
				sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
				sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
				sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
				sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_tohdr where TONO=A.TONO)),'') as empname, ");
				sql.append("B.UOM ,");
				sql.append(" (SELECT TAXTREATMENT  FROM [" + plant + "_" + "TOHDR" + "] WHERE TONO=A.TONO) TAXTREATMENT,");
		        sql.append(" (SELECT SALES_LOCATION  FROM [" + plant + "_" + "TOHDR" + "] WHERE TONO=A.TONO) SALES_LOCATION, ");
				sql.append(sqlavgcost.toString());
				sql.append(" FROM (SELECT TONO,ITEM,ITEMDESC,ISSUEDATE,CNAME,SUM(PICKQTY)PICKQTY FROM ["+ plant +"_SHIPHIS] WHERE ");
				sql.append("TONO<>'GOODSISSUE' GROUP BY TONO,ITEM,ITEMDESC,ISSUEDATE,CNAME) A, ");
				sql.append("(SELECT r.TONO,r.ITEM,isnull(UNITMO,'') as UOM,SUM(ORDQTY)ORDQTY ,SUM(PICKQTY)PICKQTY,MAX(ISNULL(r.UNITPRICE,0) ) unitprice, ");
				sql.append("ISSUEDATE FROM ["+ plant +"_SHIPHIS]r  , [" + plant + "_" + "TODET" + "]s  WHERE s.tono=r.tono and s.TOLNNO=TOLNO and s.item=r.item and r.TONO<>'GOODSISSUE' AND STATUS='C' GROUP BY r.TONO,r.ITEM,UNITMO,ISSUEDATE)  B ");
				sql.append(" WHERE A.TONO=B.TONO AND A.ITEM=b.ITEM  AND A.ISSUEDATE=B.ISSUEDATE "+ sCondition);
				
				   if (ht.get("TONO") != null) {
			    	   sql.append(" AND A.TONO = '" + ht.get("TONO") + "'");
			       } 
				   if (ht.get("ITEM") != null) {
				    	   sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
				   }  
				   if (ht.get("JOBNUM") != null) {
          	   sql.append(" AND A.TONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  TONO=A.TONO)");
             }
	               if (ht.get("STATUS") != null) {
			    	   sql.append(" AND A.TONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  TONO=A.TONO)");
			       }
			       if (ht.get("PickStaus") != null) {
			    	   sql.append(" AND A.TONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND  TONO=A.TONO)");
			       }
			       if (ht.get("ORDERTYPE") != null) {
			    	   sql.append(" AND A.TONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  TONO=A.TONO)");
			       }
				  
			       if (ht.get("STATUS_ID") != null) {
			    	   sql.append(" AND A.TONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  TONO=A.TONO)");
			       }
			       if (ht.get("EMPNO") != null) {
			    	   sql.append(" AND A.TONO IN (SELECT TONO from [" + plant +"_TOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND  TONO=A.TONO)");
			       }
			       if (ht.get("ITEMTYPE") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_BRAND_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_CLS_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
			       }
			
				arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
				// End code modified by Bruhan for outorderwithprice on 18/12/12 	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerTOInvoiceIssueSummary:", e);
		}
		return arrList;
	}
  //END BY NAVAS
 
  public ArrayList getCustomerDOAvgCostIssueSummaryByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype,String currencyid,String baseCurrency) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",fdate="",tdate="";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		     if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(a.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
       if (custname.length()>0){
            	custname = StrUtils.InsertQuotes(custname);
             	sCondition =  sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
          }
       if (ht.size() > 0) {
				if (ht.get("CUSTTYPE") != null) {
					String custtype = ht.get("CUSTTYPE").toString();
					sCondition= sCondition+" AND cname in (select CNAME from "+plant+"_CUSTMST where CUSTOMER_TYPE_ID like '"+custtype+"%')";
					ht.remove("CUSTTYPE");
				}
			}
                     
		    String dtCondStr =    " and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
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
                  // Date range to get avg cost
                  if (afrmDate.length()>5)
                      fdate   = afrmDate.substring(0,4)+ afrmDate.substring(5,7)+afrmDate.substring(8,10);
                      
                  if (atoDate.length()>5)
                       tdate  = atoDate.substring(0,4)+ atoDate.substring(5,7)+atoDate.substring(8,10); 
                  String sRecvCondition=""; 
                  if (fdate.length() > 0) {
                          sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)  >= '" + fdate
                                          + "'  ";
                          if (tdate.length() > 0) {
                                  sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + tdate
                                                  + "'  ";
                          }
                  } else {
                          if (tdate.length() > 0) {
                                  sRecvCondition = sRecvCondition + " and substring(CRAT,1,8) <= '" + tdate
                                                  + "'  ";
                          }
                  }
                  
                  String ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                          "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                          "   WHERE  CURRENCYID=ISNULL((SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO),'"+baseCurrency+"')) AS DECIMAL(20,6)) ) ";

                  StringBuffer sqlavgcost = new StringBuffer(" (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] WHERE ITEM=A.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB')  AND UNITCOST >0 AND UNITCOST IS NOT NULL "+sRecvCondition+")>0  THEN ");
                  sqlavgcost.append(" (Select ISNULL(CAST(ISNULL(SUM(C.UNITCOST),0)/SUM(C.RECQTY) AS DECIMAL(20,6)),0) AS AVERGAGE_COST from ");
                  sqlavgcost.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,6)) ");
                  sqlavgcost.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO)) AS DECIMAL(20,6)) ");
                  sqlavgcost.append("   * RECQTY AS DECIMAL(20,6)) AS UNITCOST ");
                  sqlavgcost.append("   from "+plant+"_RECVDET R where item =A.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> ''  AND TRAN_TYPE ='IB' AND ORDQTY >0 " + sRecvCondition +"  ) C )  ");
                  sqlavgcost.append("   ELSE   CAST(((SELECT sum(COST) FROM  ["+plant+"_itemmst] where ITEM=A.ITEM)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4))   END ) AS AVERAGE_COST   ");

                  
                  if(sorttype.equalsIgnoreCase("PRODUCT"))
                  {       
                  		extraCon="GROUP BY a.ITEM,a.DONO,a.CNAME,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM),"
                    		  +"Ranked AS ("
                    		  +"SELECT custcode,dono,item,UOM,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                    		  +"qtyor ,qtypick ,qty,unitprice,issPrice,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status_id,Tax,empname,AVERAGE_COST,TAXTREATMENT,SALES_LOCATION,"
                    		  +"RANK() OVER (PARTITION BY ITEM   ORDER BY issPrice desc) rnk"
                    		  +" from SORTTYPE)"
                    		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY ITEM)desc";
                  }
                  else if(sorttype.equalsIgnoreCase("CUSTOMER"))
                  {
                  		extraCon="GROUP BY a.CNAME,A.DONO,a.ITEM,a.PICKQTY,B.PICKQTY,A.ISSUEDATE,B.UNITPRICE,B.UOM),"
                      		  +"Ranked AS ("
                      		  +"SELECT custcode,dono,item,UOM,itemdesc,orderType,custname,issuedate,trandate,jobnum,remarks,remarks2,taxval,"
                      		  +"qtyor ,qtypick ,qty,unitprice,issPrice,prd_brand_id,prd_cls_id,PRD_DEPT_ID,itemtype,status_id,Tax,empname,AVERAGE_COST,TAXTREATMENT,SALES_LOCATION,"
                      		  +"RANK() OVER (PARTITION BY custcode   ORDER BY issPrice desc) rnk"
                      		  +" from SORTTYPE)"
                      		  +"SELECT * FROM Ranked ORDER BY sum(issPrice)  OVER (PARTITION BY custcode)desc";
                
                  }    
                  
                  else{    
                	  	extraCon = " order by A.CNAME, A.DONO,A.ITEM,CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date)";
                  }
            if(!sorttype.equalsIgnoreCase("")){      
              sql.append(" WITH SORTTYPE AS( ");}
              sql.append("SELECT A.DONO as dono,A.ITEM as item,ISNULL(A.PICKQTY,0) as qtyPick,ISNULL(B.PICKQTY,0) as qty,A.ISSUEDATE as issuedate,");
				sql.append("ISNULL(B.UNITPRICE,0) as unitprice,(ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0))issPrice,A.CNAME as custname,");
				sql.append("CAST((SUBSTRING(A.ISSUEDATE, 7, 4) + SUBSTRING(A.ISSUEDATE, 4, 2) + SUBSTRING(A.ISSUEDATE, 1, 2)) AS date) as trandate,");
				sql.append("ISNULL((SELECT SUM(QTYOR) FROM ["+ plant +"_DODET] WHERE ITEM=A.ITEM AND DONO=A.DONO GROUP BY DONO,ITEM),0) qtyor,");
				sql.append("(SELECT CustCode  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) custcode,");
				sql.append("(SELECT ORDERTYPE  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) orderType,");
				sql.append("(SELECT JOBNUM  FROM ["+ plant +"_DOHDR] WHERE DONO=A.DONO) jobNum,");
				sql.append(" ISNULL((((ISNULL(B.PICKQTY,0)*ISNULL(B.UNITPRICE,0)) * (SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST]  WHERE ITEM=A.ITEM))/100),0) taxval,");
				sql.append("ISNULL((SELECT ISNULL(PRODGST,0) FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM),0) Tax,");
				sql.append("(SELECT ISNULL(REMARK1,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks,");
				sql.append("(SELECT ISNULL(REMARK3,'') FROM ["+ plant +"_DOHDR] R WHERE DONO=A.DONO) remarks2,");
				sql.append("(SELECT ISNULL(STATUS_ID,'') FROM ["+ plant +"_DOHDR]  WHERE DONO=A.DONO) status_id,");
				sql.append("(SELECT ISNULL(PRD_CLS_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_cls_id,");
				sql.append("(SELECT ISNULL(PRD_DEPT_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) PRD_DEPT_ID,");
				sql.append("(SELECT ISNULL(ITEMTYPE,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemtype,");
				sql.append("(SELECT ISNULL(PRD_BRAND_ID,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) prd_brand_id,");
				sql.append("(SELECT ISNULL(ITEMDESC,'') FROM ["+ plant +"_ITEMMST] WHERE ITEM=A.ITEM) itemdesc, ");
				sql.append("isnull((select isnull(fname,'') from["+ plant +"_EMP_MST] where EMPNO in(select EMPNO from "+ plant +"_dohdr where DONO=A.DONO)),'') as empname, ");
				sql.append("B.UOM ,");
				sql.append(" (SELECT TAXTREATMENT  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) TAXTREATMENT,");
		        sql.append(" (SELECT SALES_LOCATION  FROM [" + plant + "_" + "DOHDR" + "] WHERE DONO=A.DONO) SALES_LOCATION, ");
				sql.append(sqlavgcost.toString());
				sql.append(" FROM (SELECT DONO,ITEM,ITEMDESC,ISSUEDATE,CNAME,SUM(PICKQTY)PICKQTY FROM ["+ plant +"_SHIPHIS] WHERE ");
				sql.append("DONO<>'GOODSISSUE' GROUP BY DONO,ITEM,ITEMDESC,ISSUEDATE,CNAME) A, ");
				sql.append("(SELECT r.DONO,r.ITEM,isnull(UNITMO,'') as UOM,SUM(ORDQTY)ORDQTY ,SUM(PICKQTY)PICKQTY,MAX(ISNULL(r.UNITPRICE,0) ) unitprice, ");
				sql.append("ISSUEDATE FROM ["+ plant +"_SHIPHIS]r  , [" + plant + "_" + "DODET" + "]s  WHERE s.dono=r.dono and s.DOLNNO=DOLNO and s.item=r.item and r.DONO<>'GOODSISSUE' AND STATUS='C' GROUP BY r.DONO,r.ITEM,UNITMO,ISSUEDATE)  B ");
				sql.append(" WHERE A.DONO=B.DONO AND A.ITEM=b.ITEM  AND A.ISSUEDATE=B.ISSUEDATE "+ sCondition);
				
				   if (ht.get("DONO") != null) {
			    	   sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
			       } 
				   if (ht.get("ITEM") != null) {
				    	   sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
				   }  
				   if (ht.get("JOBNUM") != null) {
            	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  DONO=A.DONO)");
               }
	               if (ht.get("STATUS") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("PickStaus") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE PickStaus ='" + ht.get("PickStaus") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("ORDERTYPE") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  DONO=A.DONO)");
			       }
				  
			       if (ht.get("STATUS_ID") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("EMPNO") != null) {
			    	   sql.append(" AND A.DONO IN (SELECT DONO from [" + plant +"_DOHDR] WHERE EMPNO ='" + ht.get("EMPNO") + "' AND  DONO=A.DONO)");
			       }
			       if (ht.get("ITEMTYPE") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_BRAND_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_CLS_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
			       }
			       if (ht.get("PRD_DEPT_ID") != null) {
			    	   sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_DEPT_ID ='" + ht.get("PRD_DEPT_ID") + "' AND  ITEM=A.ITEM)");
			       }
			
			       
				arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
				// End code modified by Bruhan for outorderwithprice on 18/12/12 	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getCustomerDOInvoiceIssueSummary:", e);
		}
		return arrList;
	}
  public ArrayList getLabelPrintProductWithBatch(Hashtable ht, String dirType, String plant,String itemDesc,int start,int end) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			String sQryCondition=" WHERE ID >="+start+" and ID<="+end+"";
	      /*  if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = sCondition + " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }*/
			
			if (itemDesc.length() > 0 ) {
				if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
				
				sCondition = sCondition + " and ITEM in(Select item from ["+ plant +"_itemmst] where replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%') ";
			}
	    
//	     String extraCond	=sCondition	+ "AND item = item AND loc = LOC AND USERFLD4 = BATCH ";
		StringBuffer sql_DO = new StringBuffer();
		sql_DO.append("SELECT * FROM (SELECT (ROW_NUMBER() OVER ( ORDER BY ITEM)) AS ID,item,(select itemdesc from ["+ plant +"_itemmst] where ITEM=a.item)itemdesc, "
						+ "isnull((Select STKUOM from ["+ plant +"_itemmst] where  ITEM=a.item ),'') uom," + 
						" isnull(a.userfld4,'') batch,isnull(upper(a.loc),0) loc ,isnull(a.printstatus,'N') printstatus,a.qty  " 
						+"from "
							+ "["
						+ plant
						+ "_"
						+ "invmst] a"
						+"  where a.item<>''AND QTY > 0 "+sCondition);
		if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_DO.append(" AND a.ITEM = '" + ht.get("ITEM") + "'");
				}
				
				
				if (ht.get("BATCH") != null) {
					sql_DO.append(" AND a.userfld4 = '" + ht.get("BATCH") + "'");
				}
				
				
				
				if (ht.get("PRD_CLS_ID") != null) {
					sql_DO.append(" AND a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_DO.append(" AND a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				
				if (ht.get("ITEMTYPE") != null) {
					sql_DO.append(" AND a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
			
				
				if (ht.get("LOC") != null) {
					sql_DO.append(" AND a.LOC LIKE '%" + ht.get("LOC")
						+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_DO.append("  AND a.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("PRINTSTATUS") != null) {
					
					if((ht.get("PRINTSTATUS").equals("N"))){
							sql_DO.append(" AND ISNULL(a.PRINTSTATUS,'N') <> 'C'");
					}
					else if((ht.get("PRINTSTATUS").equals("C"))){
						sql_DO.append(" AND ISNULL(a.PRINTSTATUS,'N') = 'C'");
					}
				}
			
				
			}
		sql_DO.append( " )A" + sQryCondition);
		//sql_DO.append(" group by item,itemdesc,loc,batch,cname,status,printstatus"); 	
					arrList = movHisDAO.selectForReport(sql_DO.toString(),
						new Hashtable<String, String>());
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getLabelPrintProductWithBatch:", e);
		}
		return arrList;
	}
  
  public long getLabelPrintProductWithBatchCount(Hashtable ht, String dirType, String plant,String itemDesc) {
	  long RecCount =0;
		String sCondition = "";
		PreparedStatement ps = null;
        ResultSet rs = null;
//        List listQty = new ArrayList();
        Connection con = null;
       
		try {
			 con = DbBean.getConnection();
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//String sQryCondition=" WHERE ID >="+start+" and ID<="+end+"";
	        /*if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = sCondition + " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }*/
			
			if (itemDesc.length() > 0 ) {
				if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
				
				sCondition = sCondition + " and ITEM in(Select item from ["+ plant +"_itemmst] where replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%') ";
			}
	    
//	     String extraCond	=sCondition	+ "AND item = item AND loc = LOC AND USERFLD4 = BATCH ";
		StringBuffer sql_DO = new StringBuffer();
		sql_DO.append("SELECT COUNT(*) FROM (SELECT (ROW_NUMBER() OVER ( ORDER BY ITEM)) AS ID,item,(select itemdesc from ["+ plant +"_itemmst] where ITEM=a.item)itemdesc, "
						+ "isnull((Select STKUOM from ["+ plant +"_itemmst] where  ITEM=a.item ),'') uom," + 
						" isnull(a.userfld4,'') batch,isnull(upper(a.loc),0) loc ,isnull(a.printstatus,'N') printstatus,a.qty  " 
						+"from "
							+ "["
						+ plant
						+ "_"
						+ "invmst] a"
						+"  where a.item<>'' AND QTY > 0"+sCondition);
		if (ht.size() > 0) {
				if (ht.get("ITEM") != null) {
					sql_DO.append(" AND a.ITEM = '" + ht.get("ITEM") + "'");
				}
				
				
				if (ht.get("BATCH") != null) {
					sql_DO.append(" AND a.userfld4 = '" + ht.get("BATCH") + "'");
				}
						
				
				if (ht.get("PRD_CLS_ID") != null) {
					sql_DO.append(" AND a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=a.ITEM)");
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_DO.append(" AND a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=a.ITEM)");
				}
				
				if (ht.get("ITEMTYPE") != null) {
					sql_DO.append(" AND a.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=a.ITEM)");
				}	
			
				
				if (ht.get("LOC") != null) {
					sql_DO.append(" AND a.LOC LIKE '%" + ht.get("LOC")
						+ "%'"  );
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_DO.append("  AND a.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,(String)ht.get("LOC_TYPE_ID"))+") ");
				}
				
				if (ht.get("PRINTSTATUS") != null) {
					
					if((ht.get("PRINTSTATUS").equals("N"))){
							sql_DO.append(" AND ISNULL(a.PRINTSTATUS,'N') <> 'C'");
					}
					else if((ht.get("PRINTSTATUS").equals("C"))){
						sql_DO.append(" AND ISNULL(a.PRINTSTATUS,'N') = 'C'");
					}
				}
						
		  
			}
		sql_DO.append( " )A");// + sQryCondition);
		System.out.println(sql_DO.toString());
		//sql_DO.append(" group by item,itemdesc,loc,batch,cname,status,printstatus"); 	
		 ps = con.prepareStatement(sql_DO.toString());
         rs = ps.executeQuery();
          while (rs.next()) {
                 RecCount= rs.getInt(1);
               }
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getLabelPrintProductWithBatchCount:", e);
		}
		 finally {
             DbBean.closeConnection(con, ps);
     }
		 return RecCount;
	}
  

  public ArrayList getSalesReturn(Hashtable ht, String aPlant,String afrmDate, String atoDate,String sProductDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
			//CHNAGECODE-002 DIRTYPE CHANGE BY RADHIKA INCLUDED MULTIPLE TYPE SELECTION
          if(sProductDesc.length()>0){
              sProductDesc  = " AND ITEM IN(SELECT ITEM FROM "+aPlant+"_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(sProductDesc.replaceAll(" ",""))+"%')" ;	
          }
			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
			String condAssignedLocforUser = " ";//,cnsval="";

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			ht.put("PLANT", aPlant);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}


          	sCondition = sCondition + " AND DIRTYPE IN ( 'OBISSUE_REVERSE' )" ;
        	  //sCondition = sCondition + " AND DIRTYPE = OBISSUE_REVERSE" ;
          
			String aQuery = "";
			if (aPlant.equalsIgnoreCase("track")) {
			aQuery = "SELECT isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC," 
						+ "ISNULL(TRANDATE,'') AS TRANDATE ," 
						+ " ISNULL(CAST((SUBSTRING(CRAT, 1, 4) + SUBSTRING(CRAT, 5, 2) + SUBSTRING(CRAT, 7, 2)) AS date),'')  AS toexceltransactiondate,"
						+ "CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull((SELECT C.CNAME FROM ["+aPlant+"_DOHDR] D JOIN ["+aPlant+"_CUSTMST] C ON D.CUSTCODE = C.CUSTNO WHERE D.DONO = B.ORDNUM),'') as CUSTOMER" +
						" ,isnull((SELECT SUM(E.QTYOR) FROM ["+aPlant+"_DODET] E WHERE E.DONO = B.ORDNUM AND E.ITEM=B.ITEM),0) as QTYOR,isnull((SELECT SUM(I.QTYIS) FROM ["+aPlant+"_DODET] I  WHERE I.DONO = B.ORDNUM AND I.ITEM=B.ITEM),0) as QTYIS,isnull(PONO,'') as PONO,isnull(CRBY,'')CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY,isnull((Select ITEMDESC from [" +aPlant+"_itemmst] A where  a.ITEM=b.ITEM),'') ITEMDESC ,isnull(b.QTY,0) as QTYRETURN FROM  "
						+ "["
						+ "MOVHIS"
						+ "] B  WHERE PLANT='"
						+ aPlant
						+ "' "
						+ "and isnull(CRBY,'') not in('218202180','20212022') AND ISNULL(USERFLG6,'Y')<>'N' " + sCondition;
			} else {
					aQuery = "SELECT  isnull(JOBNUM,'') as JOBNUM,ISNULL(ORDNUM,'') as ORDNUM,ISNULL(LOC,'') AS LOC," 
						+ "ISNULL(TRANDATE,'') AS TRANDATE ," 
						+ " ISNULL(CAST((SUBSTRING(CRAT, 1, 4) + SUBSTRING(CRAT, 5, 2) + SUBSTRING(CRAT, 7, 2)) AS date),'')  AS toexceltransactiondate,"
						+"CRAT,DIRTYPE,ISNULL(ITEM,'') as ITEM,isnull((SELECT C.CNAME FROM ["+aPlant+"_DOHDR] D JOIN ["+aPlant+"_CUSTMST] C ON D.CUSTCODE = C.CUSTNO WHERE D.DONO = B.ORDNUM),'') as" +
						" CUSTOMER,isnull((SELECT SUM(I.QTYIS) FROM ["+aPlant+"_DODET] I  WHERE I.DONO = B.ORDNUM AND I.ITEM=B.ITEM),0) as QTYIS,isnull((SELECT SUM(E.QTYOR) FROM ["+aPlant+"_DODET] E WHERE E.DONO = B.ORDNUM AND E.ITEM=B.ITEM),0) as QTYOR" +
						" ,isnull(PONO,'') as PONO,isnull(CRBY,'')CRBY,ISNULL(REMARKS,'') As REMARKS,ISNULL(BATNO,'') AS BATNO,ISNULL(QTY,0) AS QTY,isnull((Select ITEMDESC from ["+aPlant+"_itemmst] A where  a.ITEM=b.ITEM),'') ITEMDESC ,isnull(b.QTY,0) as QTYRETURN FROM  "
						+ "["
						+ aPlant
						+ "_"
						+ "MOVHIS"
						+ "] B WHERE PLANT='"
						+ aPlant
						+ "' "
						+ "and isnull(CRBY,'') not in('218202180','20212022') AND ISNULL(USERFLG6,'Y')<>'N' "  // userFlg6 is used if the record in movhis should not show in report
						+ sCondition
						+ condAssignedLocforUser +sProductDesc;
			}
			String extCond = " order by crat ";
			arrList = movHisDAO.selectForReport(aQuery, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
  public ArrayList getGoodsIssueWithPriceSummary(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String sorttype) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		Hashtable htData = new Hashtable();
		String extraCon="";
		StringBuffer sql = new StringBuffer();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
		     
         if (custname.length()>0){
              	custname = StrUtils.InsertQuotes(custname);
               	sCondition =  sCondition + " AND A.CustName LIKE '%"+custname+"%' " ;
            }
         
                       
		    String dtCondStr =    " and ISNULL(a.TRANDT,'')<>'' AND CAST((SUBSTRING(a.TRANDT, 7, 4) + '-' + SUBSTRING(a.TRANDT, 4, 2) + '-' + SUBSTRING(a.TRANDT, 1, 2)) AS date)";
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
                    
                  	  	extraCon = " order by A.POSTRANID DESC,CAST((SUBSTRING(A.TRANDT, 7, 4) + SUBSTRING(A.TRANDT, 4, 2) + SUBSTRING(A.TRANDT, 1, 2)) AS date) DESC";
                    
              if(!sorttype.equalsIgnoreCase("")){      
              sql.append(" WITH SORTTYPE AS( ");}
              sql.append("SELECT POSTRANID as dono,ISNULL((select sum(PICKQTY) from "+ plant +"_SHIPHIS where A.POSTRANID=DONO),0) qty,TRANDT as  issuedate, ");
				sql.append("A.CustName as custname,CAST((SUBSTRING(A.TRANDT, 7, 4) + SUBSTRING(A.TRANDT, 4, 2) + SUBSTRING(A.TRANDT, 1, 2)) AS date) as trandate, ");
				sql.append("CustCode as custcode,ORDERTYPE as orderType,A.JobNum as jobnum,A.EMPNO as empname,A.STATUS as status_id,A.OUTBOUND_GST as Tax,");
				sql.append("ISNULL((select sum(PICKQTY*UNITPRICE) from "+ plant +"_SHIPHIS where A.POSTRANID=DONO),0) rate,A.ORDERDISCOUNT as orderdiscount,A.SHIPPINGCOST as shippingcost,");
				sql.append("ISNULL(REMARK1,'')  remarks,");
				sql.append("SUBSTRING((ISNULL((select distinct ',' + isnull(invoiceno, '') from "+ plant +"_SHIPHIS where A.POSTRANID=DONO and (INVOICENO!='' or  INVOICENO is not null) FOR XML PATH('')),'') ), 2, 1000000) as InvoiceNo,");
				sql.append("ISNULL(REMARK3,'')  remarks2");
				sql.append(" FROM "+ plant +"_POSHDR A WHERE A.STATUS in ('O','C','N') AND A.RECSTATUS='I' "+ sCondition);
				
				
				
				   if (ht.get("DONO") != null) {
			    	   sql.append(" AND A.POSTRANID = '" + ht.get("DONO") + "'");
			       } 
				  
				   
				   if (ht.get("JOBNUM") != null) {
              	   sql.append(" AND A.JOBNUM ='" + ht.get("JOBNUM") + "'");
                 }
	               if (ht.get("PickStaus") != null) {
			    	   sql.append(" AND A.STATUS ='" + ht.get("PickStaus") + "'");
			       }
			       
			       if (ht.get("ORDERTYPE") != null) {
			    	   sql.append(" AND A.ORDERTYPE  ='" + ht.get("ORDERTYPE") + "' ");
			       }
				  
			       
			       if (ht.get("EMPNO") != null) {
			    	   sql.append(" AND A.EMPNO  = '" + ht.get("EMPNO") + "' ");
			       }
			       
			       if (ht.get("INVOICENO") != null) {
			    	   sql.append(" AND A.POSTRANID  = (select distinct DONO from "+ plant +"_SHIPHIS where INVOICENO='" + ht.get("INVOICENO") + "') ");
			       }
			       
			
				arrList = movHisDAO.selectForReport(sql.toString(), htData,extraCon);
				// End code modified by radhika for outorderwithprice on 18/12/12 	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getGoodsIssueWithPriceDetails:", e);
		}
		return arrList;
	}
	public ArrayList getSupplierPOInvoiceLocalExpSummary(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String viewdate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	        if (ht.size() > 0) {
				if (ht.get("SUPPLIERTYPE") != null) {
					String suppliertype = ht.get("SUPPLIERTYPE").toString();
					sCondition= sCondition+" AND custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
					ht.remove("SUPPLIERTYPE");
				}
			}
	        String dtCondStr="",extraCon="";
	        StringBuffer sql;
	   		        if(viewdate.equals("ByOrderDate")){
			        	 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
						 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
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
		           
				   } else if(viewdate.equals("ByDeliveryDate")){
					     dtCondStr =    " and ISNULL(a.DelDate,'')<>'' AND (SUBSTRING(a.DelDate, 7, 4) + '-' + SUBSTRING(a.DelDate, 4, 2) + '-' + SUBSTRING(a.DelDate, 1, 2))";
					     extraCon=" order by a.custname,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) ";
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
				}  else
				{
					 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
					 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
					 
					 if (afrmDate.length() > 0) {
                   	sCondition = sCondition + dtCondStr + " >= '" 
       						+ afrmDate
       						+ "'  ";
       				if (atoDate.length() > 0) {
       					sCondition = sCondition + dtCondStr+ " <= '" 
       					+ atoDate
       					+ "'  ";
       				}
       			} else {
       				if (atoDate.length() > 0) {
       					sCondition = sCondition + dtCondStr + "  <= '" 
       					+ atoDate
       					+ "'  ";
       				}
       			}
				}
		                          
                      //start code by radhika for wildcard search supplier name for inbound order summary with price on 26 aug 13
                      if (custname.length()>0){
                       	custname = StrUtils.InsertQuotes(custname);
                       	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                       }
                                           
      	      // query changed by radhika to get remarks in summary inbound order details(with cost) 
                      if(viewdate.equals("ByOrderDate")){

                      	sql = new StringBuffer(" select distinct a.custcode,a.CURRENCYID,a.CURRENCYUSEQT,isnull(a.CRBY,'') as users,b.UNITMO,a.pono,b.polnno,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");

          				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
          				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0)+ isnull((SELECT (SUM(LANDED_COST)/COUNT(LANDED_COST)) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,ISNULL((((((isnull(b.unitcost*b.CURRENCYUSEQT,0)+ isnull((SELECT (SUM(LANDED_COST)/COUNT(LANDED_COST)) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT)*inbound_gst)/100)*b.qtyor),0) taxval,TAXID,");
          				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0)+ isnull((SELECT (SUM(LANDED_COST)/COUNT(LANDED_COST)) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + isnull((SELECT (SUM(LANDED_COST)/COUNT(LANDED_COST)) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(b.UNITMO,'') as UOM,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
          				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
          				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
          				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
          				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
          				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
          				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                        }
                        else if(viewdate.equals("ByDeliveryDate")){

                      	sql = new StringBuffer(" select distinct a.custcode,a.CURRENCYID,a.CURRENCYUSEQT,isnull(a.CRBY,'') as users,b.UNITMO,a.pono,b.polnno,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,");

            				sql.append("a.custname,b.polnno,isnull(a.DelDate,'') as CollectionDate  ,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
            				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,ISNULL((((((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT)*inbound_gst)/100)*b.qtyor),0) taxval,TAXID,");
            				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.UNITMO,'') as UOM,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
            				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
            				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
            				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
            				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
            				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                        }else{

                      	sql = new StringBuffer(" select distinct a.custcode,a.CURRENCYID,a.CURRENCYUSEQT,isnull(a.CRBY,'') as users,b.UNITMO,isnull((select TAXID from "+plant+"_POHDR where PONO=a.PONO ),'') as TAXID,a.pono,b.polnno,b.item,c.itemdesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");

            				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
            				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0) + isnull((SELECT (SUM(C.RECQTY*LANDED_COST)/SUM(C.RECQTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT (COUNT(LANDED_COST)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,");
    						sql.append("ISNULL((((((isnull(b.unitcost*b.CURRENCYUSEQT,0) + isnull((SELECT (SUM(C.RECQTY*LANDED_COST)/SUM(C.RECQTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT (COUNT(LANDED_COST)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT)*inbound_gst)/100)*b.qtyor),0) taxval,TAXID,");
            				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) +isnull((SELECT (SUM(C.RECQTY*LANDED_COST)/SUM(C.RECQTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT (COUNT(LANDED_COST)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,");
    						sql.append("ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) +isnull((SELECT (SUM(C.RECQTY*LANDED_COST)/SUM(C.RECQTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT (COUNT(LANDED_COST)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'') as UOM,isnull(a.status_id,'')as status_id,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
            				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
            				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
            				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
            				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
            				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                        }
          				
      				
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
  public ArrayList getSupplierPOInvoiceLocalExpSummaryByProductGst(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String viewdate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	        
	        if (ht.size() > 0) {
				if (ht.get("SUPPLIERTYPE") != null) {
					String suppliertype = ht.get("SUPPLIERTYPE").toString();
					sCondition= sCondition+" AND custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
					ht.remove("SUPPLIERTYPE");
				}
			}
	        
	        String dtCondStr="",extraCon="";
	        StringBuffer sql;
             		        
			        if(viewdate.equals("ByOrderDate")){
			        	System.out.println("come in"+viewdate);
						 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
						 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
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
		           
				   } else if(viewdate.equals("ByDeliveryDate")){
					     dtCondStr =    " and ISNULL(a.DelDate,'')<>'' AND (SUBSTRING(a.DelDate, 7, 4) + '-' + SUBSTRING(a.DelDate, 4, 2) + '-' + SUBSTRING(a.DelDate, 1, 2))";
					     extraCon=" order by a.custname,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) ";
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
				}  else
				{
					 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
					 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
					 
					 if (afrmDate.length() > 0) {
                   	sCondition = sCondition + dtCondStr + " >= '" 
       						+ afrmDate
       						+ "'  ";
       				if (atoDate.length() > 0) {
       					sCondition = sCondition + dtCondStr+ " <= '" 
       					+ atoDate
       					+ "'  ";
       				}
       			} else {
       				if (atoDate.length() > 0) {
       					sCondition = sCondition + dtCondStr + "  <= '" 
       					+ atoDate
       					+ "'  ";
       				}
       			}
				}
		                    if (custname.length()>0){
                       	custname = StrUtils.InsertQuotes(custname);
                       	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                       }
                                           
      	      // query changed by radhika to get remarks in summary inbound order details(with cost) 
                      if(viewdate.equals("ByOrderDate")){

                      	sql = new StringBuffer(" select distinct a.custcode,a.pono,b.polnno,b.item,c.itemdesc,isnull(a.CRBY,'') as users,b.UNITMO,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");

          				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
          				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,");
          				sql.append("(ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0)*isnull(b.prodgst,0))/100 as taxval,");
          				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'') as UOM,isnull(a.status_id,'')as status_id,isnull(b.prodgst,0) as Tax,isnull(a.remark3,'') remarks3, ");
          				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
          				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
          				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
          				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
          				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
          				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                        }
                        else if(viewdate.equals("ByDeliveryDate")){

                      	sql = new StringBuffer(" select distinct a.custcode,a.pono,b.polnno,isnull(a.CRBY,'') as users,b.UNITMO,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,");
						
            				sql.append("a.custname,b.polnno,isnull(a.DelDate,'') as CollectionDate  ,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
            				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,");
            				sql.append("(ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0)*isnull(b.prodgst,0))/100 as taxval,");
            				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(b.UNITMO,'') as UOM,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.prodgst,0) as Tax,isnull(a.remark3,'') remarks3,");
            				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
            				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
            				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
            				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
            				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                        }else{

                      	sql = new StringBuffer(" select distinct a.custcode,a.pono,b.polnno,isnull(a.CRBY,'') as users,b.UNITMO,b.item,c.itemdesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");
						
            				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
            				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0) + isnull((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + ");
    						sql.append("(isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0) + CASE WHEN (SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,");
            				sql.append("(ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + isnull((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0) + CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0)*isnull(b.prodgst,0))/100 as taxval,");
            				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + isnull((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0) + CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,");
    						sql.append("ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + isnull((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0) + CASE WHEN (SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO and b.POLNNO = d.LNNO) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,");
							sql.append("isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.PRD_DEPT_ID,'') as PRD_DEPT_ID,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'') as UOM,isnull(a.status_id,'')as status_id,isnull(b.prodgst,0) as Tax,isnull(a.remark3,'') remarks3,");
            				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
              				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
            				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
            				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
            				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
            				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                        }
          				
      				
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
  
  public ArrayList getSupplierPOInvoiceLocalExpSummaryForInventory(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname,String viewdate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	        if (ht.size() > 0) {
				if (ht.get("SUPPLIERTYPE") != null) {
					String suppliertype = ht.get("SUPPLIERTYPE").toString();
					sCondition= sCondition+" AND custname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
					ht.remove("SUPPLIERTYPE");
				}
			}
	        String dtCondStr="",extraCon="";
	        StringBuffer sql;
	   		        if(viewdate.equals("ByOrderDate")){
			        	 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
						 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
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
		           
				   } else if(viewdate.equals("ByDeliveryDate")){
					     dtCondStr =    " and ISNULL(a.DelDate,'')<>'' AND (SUBSTRING(a.DelDate, 7, 4) + '-' + SUBSTRING(a.DelDate, 4, 2) + '-' + SUBSTRING(a.DelDate, 1, 2))";
					     extraCon=" order by a.custname,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) ";
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
				}  else
				{
					 dtCondStr ="and ISNULL(a.CollectionDate,'')<>'' AND CAST((SUBSTRING(a.CollectionDate, 7, 4) + '-' + SUBSTRING(a.CollectionDate, 4, 2) + '-' + SUBSTRING(a.CollectionDate, 1, 2)) AS date)";
					 extraCon= "order by a.custname,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";
					 
					 if (afrmDate.length() > 0) {
                 	sCondition = sCondition + dtCondStr + " >= '" 
     						+ afrmDate
     						+ "'  ";
     				if (atoDate.length() > 0) {
     					sCondition = sCondition + dtCondStr+ " <= '" 
     					+ atoDate
     					+ "'  ";
     				}
     			} else {
     				if (atoDate.length() > 0) {
     					sCondition = sCondition + dtCondStr + "  <= '" 
     					+ atoDate
     					+ "'  ";
     				}
     			}
				}
		                          
                    //start code by radhika for wildcard search supplier name for inbound order summary with price on 26 aug 13
                    if (custname.length()>0){
                     	custname = StrUtils.InsertQuotes(custname);
                     	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
                     }
                                         
    	      // query changed by radhika to get remarks in summary inbound order details(with cost) 
                    if(viewdate.equals("ByOrderDate")){

                    	sql = new StringBuffer(" select distinct a.custcode,isnull(a.CRBY,'') as users,b.UNITMO,a.pono,b.polnno,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");

        				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
        				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0)+ (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+ a.SHIPPINGCOST )*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,ISNULL((((((isnull(b.unitcost*b.CURRENCYUSEQT,0)+ (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+ a.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT)*inbound_gst)/100)*b.qtyor),0) taxval,TAXID,");
        				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0)+ (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+ a.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+ a.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(b.UNITMO,'') as UOM,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
        				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
        				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
        				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
        				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
        				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
        				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                      }
                      else if(viewdate.equals("ByDeliveryDate")){

                    	sql = new StringBuffer(" select distinct a.custcode,isnull(a.CRBY,'') as users,b.UNITMO,a.pono,b.polnno,b.item,c.itemdesc,isnull(c.Remark1,'') as DetailItemDesc,isnull(a.ordertype,'') ordertype,");

          				sql.append("a.custname,b.polnno,isnull(a.DelDate,'') as CollectionDate  ,CAST((SUBSTRING(a.DelDate, 7, 4) + SUBSTRING(a.DelDate, 4, 2) + SUBSTRING(a.DelDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
          				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,ISNULL((((((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT)*inbound_gst)/100)*b.qtyor),0) taxval,TAXID,");
          				sql.append("b.crat,ISNULL(b.qtyor*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.SHIPPINGCOST,0)+isnull(a.LOCALEXPENSES,0)+isnull((SELECT SUM(LANDED_COST) FROM [" + plant + "_FINBILLHDR] c join [" + plant + "_FINBILLDET] d on c.ID = d.BILLHDRID and c.PONO = a.PONO),0))*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(a.status_id,'')as status_id,isnull(b.UNITMO,'') as UOM,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
          				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
            				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
          				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
          				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
          				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
          				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                      }else{

                    	sql = new StringBuffer(" select distinct a.custcode,isnull(a.CRBY,'') as users,b.UNITMO,a.pono,b.polnno,b.item,c.itemdesc,isnull(a.ordertype,'') ordertype,isnull(a.DELDATE,'') DELDATE,");

          				sql.append("a.custname,b.polnno,a.CollectionDate  ,CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) as trandate ,isnull(a.jobnum,'') jobnum,isnull(a.Remark1,'') as remarks,b.lnstat,");
          				sql.append("isnull(b.qtyor,0) qtyor,isnull(b.qtyrc,0) qty, ISNULL(((isnull(ISNULL(b.UNITCOST_AOD,b.UNITCOST)*b.CURRENCYUSEQT,0) + (isnull(ISNULL(b.UNITCOST_AOD,b.UNITCOST)*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+ a.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.UNITCOST_AOD,s.UNITCOST)*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) unitcost,");
  						sql.append("ISNULL((((((isnull(ISNULL(b.UNITCOST_AOD,b.UNITCOST)*b.CURRENCYUSEQT,0) + (isnull(ISNULL(b.UNITCOST_AOD,b.UNITCOST)*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+a.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.UNITCOST_AOD,s.UNITCOST)*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT)*inbound_gst)/100)*b.qtyor),0) taxval,TAXID,");
          				sql.append("b.crat,ISNULL(b.qtyor*((isnull(ISNULL(b.UNITCOST_AOD,b.UNITCOST)*b.CURRENCYUSEQT,0) + (isnull(ISNULL(b.UNITCOST_AOD,b.UNITCOST)*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+a.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.UNITCOST_AOD,s.UNITCOST)*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) OrderCost,");
  						sql.append("ISNULL(b.qtyrc*((isnull(b.unitcost*b.CURRENCYUSEQT,0) +isnull((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM),0) + (isnull(b.unitcost*b.CURRENCYUSEQT,0) * (((isnull(a.LOCALEXPENSES,0)+CASE WHEN (SELECT (COUNT(LANDED_COST)) from [" + plant + "_RECVDET] c join [" + plant + "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join [" + plant + "_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =a.pono and c.LNNO=b.POLNNO and e.ITEM = b.ITEM) is null THEN a.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=b.pono),0)),0))/100))/b.CURRENCYUSEQT),0) RecvCost,isnull(c.prd_brand_id,'') as prd_brand_id,isnull(c.prd_cls_id,'') as prd_cls_id,isnull(c.itemtype,'') as itemtype,isnull(b.UNITMO,'') as UOM,isnull(a.status_id,'')as status_id,isnull(inbound_gst,0) as Tax,isnull(a.remark3,'') remarks3, ");
          				sql.append("ISNULL(PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
            				sql.append("CASE WHEN ISNULL(GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT,ORDER_STATUS ");
          				sql.append(" from " + "[" + plant + "_" + "pohdr" + "] a,");
          				sql.append( "[" + plant + "_" + "podet" + "] b,["+ plant + "_" + "ITEMMST]c" );
          				sql.append(" where a.pono=b.pono and b.ITEM=c.item "  + sCondition);
          				arrList = movHisDAO.selectForReport(sql.toString(), ht, extraCon);
                      }
        				
    				
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
  
  public ArrayList getReportIBSummaryDetailsByCostlocalexpensesForInventory(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		   
	          }
			
			 if (ht.size() > 0) {
					if (ht.get("SUPPLIERTYPE") != null) {
						String suppliertype = ht.get("SUPPLIERTYPE").toString();
						sCondition= sCondition+" AND a.cname in (select VNAME from "+plant+"_VENDMST where SUPPLIER_TYPE_ID like '"+suppliertype+"%')";
						ht.remove("SUPPLIERTYPE");
					}
				}

	        		dtCondStr ="and ISNULL(A.RECVDATE,'')<>'' AND CAST((SUBSTRING(A.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
	        		extraCon= "order by A.custname, A.PONO,A.ITEM,CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) ";    			        
			       
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
		           
			           if (custname.length()>0){
                    	custname = StrUtils.InsertQuotes(custname);
                    	sCondition = sCondition + " AND A.CNAME LIKE '%"+custname+"%' " ;
                     } 
			           //htData.put("A.PONO <> ''");
			           sql = new StringBuffer("WITH temptable AS ( SELECT A.PONO as pono,A.ITEM as item ,B.RECQTY as qty,A.RECVDATE,B.UNITCOST as unitcost,(B.RECQTY*B.UNITCOST) RecvCost,A.CNAME as custname,");
                     sql.append(" CAST((SUBSTRING(A.RECVDATE, 7, 4) + SUBSTRING(A.RECVDATE, 4, 2) + SUBSTRING(A.RECVDATE, 1, 2)) AS date) as trandate,");
                     sql.append(" (SELECT SUM(QTYOR) FROM [" + plant + "_" + "PODET" + "] WHERE ITEM=A.ITEM AND PONO=A.PONO and UNITMO=B.UOM GROUP BY PONO,ITEM,UNITMO) qtyor, ");
                     sql.append(" (SELECT ORDERTYPE  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) ordertype,");
			           sql.append(" (SELECT JOBNUM  FROM [" + plant + "_" + "POHDR" + "] WHERE PONO=A.PONO) jobNum,");
			           sql.append(" (SELECT ISNULL(INBOUND_GST,0) FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) Tax,");
			           sql.append(" ((B.RECQTY*B.UNITCOST) * (SELECT ISNULL(INBOUND_GST,0) FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO))/100 taxval,");
			           sql.append(" (SELECT ISNULL(REMARK1,'') FROM [" + plant + "_" + "POHDR" + "] R WHERE PONO=A.PONO) remarks,");
			           sql.append(" (SELECT ISNULL(REMARK3,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) remarks3,");
			           sql.append(" (SELECT ISNULL(STATUS_ID,'') FROM [" + plant + "_" + "POHDR" + "]  WHERE PONO=A.PONO) status_id, ");
			           sql.append(" (SELECT ISNULL(PRD_CLS_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_cls_id, ");
			           sql.append(" (SELECT ISNULL(ITEMTYPE,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemtype, ");
			           sql.append(" (SELECT ISNULL(PRD_BRAND_ID,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) prd_brand_id, ");
			           sql.append("(SELECT ISNULL(ITEMDESC,'') FROM [" + plant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) itemdesc, ");
			           sql.append("(SELECT E.LANDED_COST from [" + plant + "_RECVDET] C LEFT JOIN [" + plant + "_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN [" + plant + "_FINBILLDET] E ON D.ID = E.BILLHDRID ");
			           sql.append(" WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM ");
			           sql.append(" GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST");
			           sql.append(" ) AS LANDED_COST,");
			           sql.append(" A.GRNO, ");
			           sql.append(" B.UOM, ");
			           sql.append ("A.users");
			           sql.append(" FROM");
			           sql.append(" (SELECT PONO,LNNO,ITEM,RECVDATE,CNAME,ISNULL(CRBY, '') AS users,ISNULL(GRNO, '') AS GRNO FROM [" + plant + "_" + "RECVDET" + "] WHERE TRAN_TYPE='IB' GROUP BY PONO,LNNO,ITEM,ITEMDESC,RECVDATE,CNAME,GRNO,CRBY) A,");
			           sql.append(" (SELECT R.PONO,R.ITEM,SUM(ORDQTY)ORDQTY ,SUM(RECQTY)RECQTY,MAX(isnull(R.unitcost,0)) UNITCOST,RECVDATE,ISNULL(p.UNITMO, '') AS UOM,ISNULL(GRNO, '') AS GRNO ");
			           sql.append(" FROM [" + plant + "_" + "RECVDET" + "] R, [" + plant + "_" + "PODET" + "] P");
			           sql.append(" WHERE P.PONO=R.PONO and R.LNNO=P.POLNNO and r.item=p.item and TRAN_TYPE='IB' GROUP BY R.PONO,R.ITEM,RECVDATE,GRNO,UNITMO)  B ");
			           sql.append(" WHERE A.PONO=B.PONO AND A.ITEM=b.ITEM  AND A.RECVDATE=B.RECVDATE AND A.GRNO=B.GRNO) ");
			           sql.append(" SELECT A.GRNO,A.users,A.RECVDATE,A.Tax,A.custname,A.item,A.itemdesc,A.itemtype,A.itemtype,A.jobNum,A.ordertype,A.pono,A.prd_brand_id,A.prd_cls_id,A.qty,A.qtyor,A.remarks,A.status_id,A.trandate,A.trandate, ");
			           sql.append(" isnull(A.UOM,'') as UOM ,isnull((((A.qty * ((isnull(ISNULL(s.unitcost_aod,A.unitcost)*(s.CURRENCYUSEQT),0) + (isnull(ISNULL(s.unitcost_aod,A.unitcost),0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+P.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.unitcost_aod,s.unitcost)*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT)))*Tax)/100),0) as taxval,");
			           sql.append(" isnull((A.qty * ((isnull(ISNULL(s.unitcost_aod,A.unitcost)*(s.CURRENCYUSEQT),0) + (isnull(ISNULL(s.unitcost_aod,A.unitcost),0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+P.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.unitcost_aod,s.unitcost)*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT))),0) as RecvCost,");
			           sql.append(" isnull(((isnull(ISNULL(s.unitcost_aod,A.unitcost)*(s.CURRENCYUSEQT),0) + (isnull(ISNULL(s.unitcost_aod,A.unitcost),0)*(s.CURRENCYUSEQT) * (((isnull(P.LOCALEXPENSES,0)+ P.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.unitcost_aod,s.unitcost)*s.CURRENCYUSEQT) from [" + plant + "_podet] s where s.pono=A.pono),0)),0))/100))/ (s.CURRENCYUSEQT)),0) as unitcost,");
			           sql.append("ISNULL(P.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(P.TAXTREATMENT,'')  TAXTREATMENT,CASE WHEN ISNULL(P.REVERSECHARGE,0)=0 THEN 'N' ELSE 'Y' END REVERSECHARGE,");
			           sql.append("CASE WHEN ISNULL(P.GOODSIMPORT,0)=0 THEN 'N' ELSE 'Y' END GOODSIMPORT ");
			           sql.append(" from temptable A, [" + plant + "_POHDR] P, [" + plant + "_podet] s WHERE A.PONO=P.PONO AND A.UOM=s.UNITMO and s.pono=A.pono and s.ITEM=A.ITEM "+ sCondition );
			            if (ht.get("JOBNUM") != null) {
	       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
	       			    }
			            if (ht.get("ITEM") != null) {
     					  sql.append(" AND A.ITEM = '" + ht.get("ITEM") + "'");
     				    }
      				if (ht.get("PONO") != null) {
     					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
     				    }
      				if (ht.get("GRNO") != null) {
      					sql.append(" AND A.GRNO = '" + ht.get("GRNO") + "'");
      				}
     				    if (ht.get("STATUS") != null) {
     					  sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS ='" + ht.get("STATUS") + "' AND  PONO=A.PONO)");
     				    }
      				if (ht.get("ORDERTYPE") != null) {
     					   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
      				}
     				   if (ht.get("ITEMTYPE") != null) {
     					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE ITEMTYPE ='" + ht.get("ITEMTYPE") + "' AND  ITEM=A.ITEM)");
     				    }
     				   if (ht.get("PRD_CLS_ID") != null) {
     					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_CLS_ID ='" + ht.get("PRD_CLS_ID") + "' AND  ITEM=A.ITEM)");
     				   }
     				   if (ht.get("PRD_BRAND_ID") != null) {
     					 sql.append(" AND A.ITEM IN (SELECT ITEM from [" + plant +"_ITEMMST] WHERE PRD_BRAND_ID ='" + ht.get("PRD_BRAND_ID") + "' AND  ITEM=A.ITEM)");
     				   }
     				   if (ht.get("STATUS_ID") != null) {
     					sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE STATUS_ID ='" + ht.get("STATUS_ID") + "' AND  PONO=A.PONO)");
     				   }
     				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
          
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}
  
  public ArrayList getGoodsRecieveSummaryListlocalexpensesForInventory(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {
			
                       
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
                       
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) >= '"// 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2)<= '" 
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		   }
			if (ht.size() > 0) {
				if (ht.get("FILTER") != null) {
					String filter = ht.get("FILTER").toString();
					if(filter.equalsIgnoreCase("Inbound Order")){sCondition= sCondition+" AND A.PONO Like'P%' ";}
					else if(filter.equalsIgnoreCase("Loan Order")){sCondition= sCondition+" AND (a.PONO Like'RS%' OR a.PONO Like'R%') ";}
					else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND A.PONO Like'T%' ";}
					else{sCondition= sCondition+" AND (A.PONO Like'GR%' OR A.PONO Like'GN%') ";}
				}
			}
			sCondition = sCondition + "  ";
			extraOrderbyCon =" order by a.item,  ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'')";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("select distinct a.pono,ISNULL(lnno,'')lnno,ISNULL(CNAME,'') as cname,a.item,isnull((select itemdesc from "+plant+"_ITEMMST where item=a.item ),'') as itemdesc,upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,"
							+" case when a.PONO Like 'P%' then isnull((select UNITMO from "+plant+"_PODET where item=a.item and PONO=a.pono and POLNNO=a.LNNO),'') when a.pono Like 'GR%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'GN%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'T%' then isnull((select UNITMO from "+plant+"_TODET where item=a.item and TONO=a.pono and TOLNNO=a.LNNO),'') when a.pono Like 'S%' then isnull((select UNITMO from "+plant+"_LOANDET where item=a.item and ORDNO=a.pono and ORDLNNO=a.LNNO),'') else'' end as uom,"
							+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
							+  "ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," 
							+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate,"
							+ "CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
							+ "ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') AS toexceltransactiondate,"
							+ "isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id,isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id,isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype,  "
							+ "isnull((isnull(isnull(s.unitcost_aod,a.unitcost)*(s.CURRENCYUSEQT),0) + (isnull(isnull(s.unitcost_aod,a.unitcost)*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(d.qtyor*isnull(d.unitcost_aod,d.unitcost)*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/s.CURRENCYUSEQT,0) unitcost,"
							+ "isnull(((isnull(isnull(s.unitcost_aod,a.unitcost)*(s.CURRENCYUSEQT),0) + (isnull(isnull(s.unitcost_aod,a.unitcost)*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(d.qtyor*isnull(d.unitcost_aod,d.unitcost)*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/s.CURRENCYUSEQT)*a.recqty,0) total,recvdate,"
							+ "isnull((inbound_gst),'') as Tax, isnull(((((isnull(isnull(s.unitcost_aod,a.unitcost)*(s.CURRENCYUSEQT),0) + (isnull(isnull(s.unitcost_aod,a.unitcost)*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)+SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(d.qtyor*isnull(d.unitcost_aod,d.unitcost)*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/s.CURRENCYUSEQT)* (p.inbound_gst)) *a.recqty)/100,0) as taxval   from "
							+ "["
							+ plant
							+ "_"
							+ "recvdet] a, ["+plant+"_pohdr] p, ["+plant+"_podet] s "
						    + " where A.PONO=P.PONO AND s.pono=A.pono and s.ITEM=A.ITEM and a.pono<>'' "
							+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")+ "%' and tran_type='IB'");
				}
				if (ht.get("a.CNAME_LOAN") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%" + ht.get("a.CNAME_LOAN") + "%' and tran_type='LO' ");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%"+ ht.get("a.CNAME_TO") + "%'  and tran_type='TO'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
			   if (ht.get("ORDERTYPE") != null) {
				  sql_PO.append("  AND a.PONO IN (SELECT PONO FROM ["+plant+"_POHDR] WHERE ORDERTYPE  = '"+ ht.get("ORDERTYPE")+ "' )"  ); 
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC") 
							+ "'" );
					
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID2  = '"+ ht.get("LOC_TYPE_ID2")
							+ "' AND LOC=a.LOC)"  );
				}
				
				
				//end
				if (ht.get("PRD_CLS_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_CLS_ID  = '"+ ht.get("PRD_CLS_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_BRAND_ID  = '"+ ht.get("PRD_BRAND_ID")+ "' )"  );
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE ITEMTYPE  = '"+ ht.get("ITEMTYPE")+ "' )"  );
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}
  /*public ArrayList getBillSummaryView(Hashtable ht, String afrmDate,
			String atoDate, String plant, String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "",dtCondStr="",extraCon="";
		StringBuffer sql;
		 Hashtable htData = new Hashtable();
		try {
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);

	        		dtCondStr ="and ISNULL(A.BILL_DATE,'')<>'' AND CAST((SUBSTRING(A.BILL_DATE, 7, 4) + '-' + SUBSTRING(a.BILL_DATE, 4, 2) + '-' + SUBSTRING(a.BILL_DATE, 1, 2)) AS date)";
	        		extraCon= "order by VNAME, A.PONO,CAST((SUBSTRING(A.BILL_DATE, 7, 4) + SUBSTRING(A.BILL_DATE, 4, 2) + SUBSTRING(A.BILL_DATE, 1, 2)) AS date) ";    			        
			       
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
		           
			           if (custname.length()>0){
                 	custname = StrUtils.InsertQuotes(custname);
                 	sCondition = sCondition + " AND VNAME LIKE '%"+custname+"%' " ;
                  } 
			          
			           sql = new StringBuffer("select BILL_DATE,BILL,A.PONO,A.VENDNO,");
			           sql.append(" ISNULL(VNAME,'') as VNAME,ISNULL((select CURRENCYID from " + plant +"_POHDR p where p.PONO=A.PONO ),'') as CURRENCYID,");
			           sql.append(" BILL_STATUS,DUE_DATE,TOTAL_AMOUNT,((ISNULL((select SUM(UNITCOST) from " + plant +"_PODET p where p.PONO=A.PONO ),0))-TOTAL_AMOUNT) as BALANCE_DUE");
			           sql.append(" from " + plant +"_FINBILLHDR A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO WHERE A.PLANT='"+ plant+"'" + sCondition);			           
                     
			            if (ht.get("JOBNUM") != null) {
	       				   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE JOBNUM ='" + ht.get("JOBNUM") + "' AND  PONO=A.PONO)");
	       			    }
			        
   				if (ht.get("PONO") != null) {
  					  sql.append(" AND A.PONO = '" + ht.get("PONO") + "'");
  				    }
   				
  				    
   				if (ht.get("ORDERTYPE") != null) {
  					   sql.append(" AND A.PONO IN (SELECT PONO from [" + plant +"_POHDR] WHERE ORDERTYPE ='" + ht.get("ORDERTYPE") + "' AND  PONO=A.PONO)");
   				}
  				   
  				   
  				  arrList = movHisDAO.selectForReport(sql.toString(), htData, extraCon);
       
	
		 }catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getSupplierPOInvoiceSummary:", e);
		}
		return arrList;
	}*/
  
  public ArrayList getGoodsRecieveSummaryList_new(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
                       
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) >= '"// 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2)<= '" 
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		   }
			if (ht.size() > 0) {
				if (ht.get("FILTER") != null) {
					String filter = ht.get("FILTER").toString();
					//if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND a.PONO Like'P%' ";}
					if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND A.PONO IN (SELECT PONO FROM ["+plant+"_POHDR]) ";}
					else if(filter.equalsIgnoreCase("Loan Order")){sCondition= sCondition+" AND (a.PONO Like'RS%' OR a.PONO Like'R%') ";}
					//else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND a.PONO Like'T%' ";}
					//else if(filter.equalsIgnoreCase("Bill")){sCondition= sCondition+" AND a.PONO Like'B%' ";}
					else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND A.PONO (SELECT TONO FROM ["+plant+"_TOHDR] ";}
					else if(filter.equalsIgnoreCase("Bill")){sCondition= sCondition+" AND A.GRNO in (SELECT GRNO FROM ["+plant+"_FINBILLHDR]) ";}
					else if(filter.equalsIgnoreCase("De-Kitting")){sCondition= sCondition+" AND (A.TRAN_TYPE='DE-KITTING') ";}
					else if(filter.equalsIgnoreCase("Kitting")){sCondition= sCondition+" AND (A.TRAN_TYPE='KITTING') ";}
					else if(filter.equalsIgnoreCase("Stock Take")){sCondition= sCondition+" AND (A.TRAN_TYPE='STOCK_TAKE') ";}
					//else{sCondition= sCondition+" AND (A.GRNO Like'GR%' OR A.PONO Like'GN%') ";}
					else{sCondition= sCondition+" AND (A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' OR A.TRAN_TYPE='MR') ";}
				}
			}
			sCondition = sCondition + "  ";
			//extraOrderbyCon =" order by a.item,  CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date) desc";
			extraOrderbyCon =" order by CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date) desc,GRNO DESC,a.item";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT * FROM ( "
				+ "select distinct A.TRAN_TYPE,a.CURRENCYID,B.CURRENCYUSEQT,a.PLANT,a.pono,ISNULL(a.lnno,'')lnno,ISNULL(CNAME,'') as cname,a.item,isnull((select itemdesc from "+plant+"_ITEMMST where item=a.item),'') as itemdesc,isnull(a.grno,'') grno,upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby,"
				+" case when a.PONO Like 'P%' then isnull((select UNITMO from "+plant+"_PODET where item=a.item and PONO=a.pono and POLNNO=a.LNNO),'') when a.pono Like 'GR%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'GN%' then isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') when a.pono Like 'T%' then isnull((select UNITMO from "+plant+"_TODET where item=a.item and TONO=a.pono and TOLNNO=a.LNNO),'') when a.pono Like 'S%' then isnull((select UNITMO from "+plant+"_LOANDET where item=a.item and ORDNO=a.pono and ORDLNNO=a.LNNO),'') else'' end as uom,"
				+" isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," 
				+  "ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate,isnull(PRINTQTY,0) PRINTQTY," 
				+ "SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate,"
				+ "CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate,"
				+ "ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') AS toexceltransactiondate,"
				+ "isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id,isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id,isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'') as PRD_DEPT_ID,isnull((select TAXID from "+plant+"_POHDR where PONO=a.PONO ),'') as TAXID,isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype,  "
				+ "isnull(isnull(b.unitcost_aod,a.unitcost),0) unitcost,isnull(isnull(b.unitcost_aod,a.unitcost)*a.recqty,0) total,recvdate,isnull((select TOP 1 inbound_gst from "+plant+"_pohdr where pono=a.pono ),'') as Tax, isnull(((isnull(b.unitcost_aod,a.unitcost) * (select TOP 1 inbound_gst from "+plant+"_pohdr where pono=a.pono ))*a.recqty)/100,'') as taxval   from "
				+ "["
				+ plant
				+ "_"
				+ "recvdet] a left join ["+plant+"_podet] b on a.pono = b.pono and a.item = b.item and a.LNNO = b.POLNNO "
			    + " where  a.pono<>'' AND a.PONO NOT Like 'C%' "
				+" UNION " + 
				"SELECT distinct A.TRAN_TYPE,a.CURRENCYID,B.CURRENCYUSEQT,a.PLANT,B.BILL as pono, ISNULL(C.LNNO,'') lnno, (SELECT VNAME FROM "+plant+"_VENDMST WHERE VENDNO=B.VENDNO) AS cname, C.ITEM," + 
				"isnull((select ITEMDESC from "+plant+"_ITEMMST where ITEM=C.ITEM),'') as ITEMDESC,isnull(a.grno,'') grno," + 
				"upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0) ordqty," + 
				"isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc," + 
				"isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby, ISNULL(C.UOM,'') uom," + 
				"isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST " + 
				"WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," + 
				"ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate,isnull(PRINTQTY,0) PRINTQTY," + 
				"SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," + 
				"CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate," + 
				"ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') " + 
				"AS toexceltransactiondate," + 
				"isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id," + 
				"isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id," + 
				"isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'') as PRD_DEPT_ID," + 
				"B.TAXID,"+
				"isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype," + 
				"isnull(isnull(c.COST,a.unitcost),0) unitcost," + 
				"isnull(isnull(c.COST,a.unitcost)*a.recqty,0) total,recvdate," + 
				"isnull(B.INBOUND_GST,'') as Tax, " + 
				"isnull(((isnull(c.COST,a.unitcost) * " + 
				"(B.INBOUND_GST))*a.recqty)/100,'') as taxval  " + 
				"FROM ["+plant+"_recvdet] A JOIN ["+plant+"_FINBILLHDR] B on A.GRNO = B.GRNO  JOIN ["+plant+"_FINBILLDET] C ON B.ID = C.BILLHDRID   AND A.ITEM=C.ITEM  AND A.LNNO=C.LNNO " +
				"where B.PONO='' AND B.BILL <> '' "
				+" UNION " + 
				"SELECT distinct A.TRAN_TYPE,a.CURRENCYID,1 as CURRENCYUSEQT,a.PLANT,'' as pono, ISNULL(a.lnno,'') lnno, '' AS cname, A.ITEM," + 
				"isnull((select ITEMDESC from "+plant+"_ITEMMST where ITEM=A.ITEM),'') as ITEMDESC,isnull(a.grno,'') grno," + 
				"upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0) ordqty," + 
				"isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc," + 
				"isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby, " +
				"case when A.TRAN_TYPE='STOCK_TAKE' then isnull((select INVENTORYUOM from "+plant+"_ITEMMST where ITEM=A.ITEM),'') else isnull((select PURCHASEUOM from "+plant+"_ITEMMST where ITEM=A.ITEM),'') end uom," + 
				"isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST " + 
				"WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," + 
				"ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate,isnull(PRINTQTY,0) PRINTQTY," + 
				"SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," + 
				"CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate," + 
				"ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') " + 
				"AS toexceltransactiondate," + 
				"isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id," + 
				"isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id," + 
				"isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'') as PRD_DEPT_ID," + 
				"isnull((select TAXID from "+plant+"_POHDR where PONO=a.PONO ),'') as TAXID," +
				"isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype," + 
				"isnull(a.unitcost,0) unitcost," + 
				"isnull(a.unitcost*a.recqty,0) total,recvdate," + 
				"'' as Tax, " + 
				"isnull(((isnull(a.unitcost,0) * (0))*a.recqty)/100,'') as taxval  " + 
				"FROM ["+plant+"_recvdet] A where A.TRAN_TYPE='DE-KITTING' OR A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' OR A.TRAN_TYPE='MR' OR A.TRAN_TYPE='STOCK_TAKE' "
				+" UNION " + 
				"SELECT distinct A.TRAN_TYPE,a.CURRENCYID,1 as CURRENCYUSEQT,a.PLANT,'' as pono, '' lnno, '' AS cname, A.ITEM," + 
				"isnull((select ITEMDESC from "+plant+"_ITEMMST where ITEM=A.ITEM),'') as ITEMDESC,isnull(a.grno,'') grno," + 
				"upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0) ordqty," + 
				"isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc," + 
				"isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby, " +
				"isnull((select PURCHASEUOM from "+plant+"_ITEMMST where ITEM=A.ITEM),'') uom," + 
				"isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST " + 
				"WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," + 
				"ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate,isnull(PRINTQTY,0) PRINTQTY," + 
				"SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," + 
				"CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate," + 
				"ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') " + 
				"AS toexceltransactiondate," + 
				"isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id," + 
				"isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id," + 
				"isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'') as PRD_DEPT_ID," + 
				"isnull((select TAXID from "+plant+"_POHDR where PONO=a.PONO ),'') as TAXID,"+
				"isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype," + 
				"isnull(a.unitcost,0) unitcost," + 
				"isnull(a.unitcost*a.recqty,0) total,recvdate," + 
				"'' as Tax, " + 
				"isnull(((isnull(a.unitcost,0) * (0))*a.recqty)/100,'') as taxval  " + 
				"FROM ["+plant+"_recvdet] A where A.TRAN_TYPE='KITTING' "
				+") A WHERE A.PLANT='"+plant+"' "+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")+ "%'");
				}
				if (ht.get("a.CNAME_LOAN") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%" + ht.get("a.CNAME_LOAN") + "%' and tran_type='LO' ");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%"+ ht.get("a.CNAME_TO") + "%'  and tran_type='TO'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
			   if (ht.get("ORDERTYPE") != null) {
				  sql_PO.append("  AND a.PONO IN (SELECT PONO FROM ["+plant+"_POHDR] WHERE ORDERTYPE  = '"+ ht.get("ORDERTYPE")+ "' )"  ); 
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC") 
							+ "'" );
					
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID2  = '"+ ht.get("LOC_TYPE_ID2")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID3  = '"+ ht.get("LOC_TYPE_ID3")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("a.GRNO") != null) {
					sql_PO.append(" AND a.GRNO = '" + ht.get("a.GRNO") 
							+ "'" );
					
				}
				
				//end
				if (ht.get("PRD_CLS_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_CLS_ID  = '"+ ht.get("PRD_CLS_ID")+ "' )"  );
					
				}
				
				if (ht.get("PRD_DEPT_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_DEPT_ID  = '"+ ht.get("PRD_DEPT_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_BRAND_ID  = '"+ ht.get("PRD_BRAND_ID")+ "' )"  );
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE ITEMTYPE  = '"+ ht.get("ITEMTYPE")+ "' )"  );
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}
  
  public ArrayList getGoodsRecieveSummaryListlocalexpenses_new(Hashtable ht, String afrmDate,
			String atoDate, String dirType, String plant, String itemDesc) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {
			
                       
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
                       
			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {

			sCondition = sCondition + " AND  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) >= '"// 
					+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2)<= '" 
						+ atoDate + "'  ";
			}
		   } else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + "  SUBSTRING(RECVDATE,7,4)+SUBSTRING(RECVDATE,4,2)+SUBSTRING(RECVDATE,1,2) <= '" 
						+ atoDate + "'  ";
			}
		   }
			if (ht.size() > 0) {
				if (ht.get("FILTER") != null) {
					String filter = ht.get("FILTER").toString();
					//if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND A.PONO Like'P%'  ";}
					if(filter.equalsIgnoreCase("Purchase Order")){sCondition= sCondition+" AND A.PONO IN (SELECT PONO FROM ["+plant+"_POHDR]) ";}
					else if(filter.equalsIgnoreCase("Loan Order")){sCondition= sCondition+" AND (a.PONO Like'RS%' OR a.PONO Like'R%') ";}
					//else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND A.PONO Like'T%' ";}
					//else if(filter.equalsIgnoreCase("Bill")){sCondition= sCondition+" AND A.PONO Like'B%' ";}
					else if(filter.equalsIgnoreCase("Transfer Order")){sCondition= sCondition+" AND A.PONO (SELECT TONO FROM ["+plant+"_TOHDR] ";}
					else if(filter.equalsIgnoreCase("Bill")){sCondition= sCondition+" AND A.GRNO (SELECT GRNO FROM ["+plant+"_FINBILLHDR] ";}
					else{sCondition= sCondition+" AND (A.PONO Like'GR%' OR A.PONO Like'GN%') ";}
				}
			}
			sCondition = sCondition + "  ";
			extraOrderbyCon =" order by a.item,  CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date) desc";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT * FROM (" + 
					"select distinct a.PLANT,a.CURRENCYID,p.CURRENCYUSEQT,isnull(a.grno,'') grno,a.pono,ISNULL(lnno,'')lnno,ISNULL(CNAME,'') as cname,a.item," + 
					"isnull((select itemdesc from "+plant+"_ITEMMST where item=a.item ),'') as itemdesc," + 
					"upper(isnull(a.batch,'')) batch,isnull(a.CRBY,'') as users, isnull(a.ordqty,0)ordqty,isnull(a.recqty,0)recqty,a.putawayqty," + 
					"isnull(a.reverseqty,0)reverseqty,isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark," + 
					"a.crat,a.crby, " + 
					"case when a.PONO Like 'P%' then " + 
					"isnull((select UNITMO from "+plant+"_PODET where item=a.item and PONO=a.pono and POLNNO=a.LNNO),'') " + 
					"when a.pono Like 'GR%' then " + 
					"isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') " + 
					"when a.pono Like 'GN%' then " + 
					"isnull((select UNITMO from "+plant+"_POSDET where item=a.item and POSTRANID=a.pono and batch=a.batch and DOLNNO=a.LNNO),'') " + 
					"when a.pono Like 'T%' then " + 
					"isnull((select UNITMO from "+plant+"_TODET where item=a.item and TONO=a.pono and TOLNNO=a.LNNO),'') " + 
					"when a.pono Like 'S%' then " + 
					"isnull((select UNITMO from "+plant+"_LOANDET where item=a.item and ORDNO=a.pono and ORDLNNO=a.LNNO),'') else'' end as uom, " + 
					"" + 
					"isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST " + 
					"WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," + 
					"" + 
					"ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," + 
					"SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," + 
					"CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate," + 
					"ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') " + 
					"AS toexceltransactiondate," + 
					"isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id," + 
					"isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id," + 
					"isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'') as PRD_DEPT_ID," + 
					"isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype,  " + 
					"isnull((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from " + 
					"["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) " + 
					"+ (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * (((isnull(P.LOCALEXPENSES,0)" + 
					"+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/" + 
					"NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/" + 
					"s.CURRENCYUSEQT,0) unitcost," + 
					"isnull(((isnull(a.unitcost*(s.CURRENCYUSEQT),0) " + 
					"+ isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D " + 
					"ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) " + 
					"+ (isnull(a.unitcost*(s.CURRENCYUSEQT),0) " + 
					"* (((isnull(P.LOCALEXPENSES,0)" + 
					"+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/" + 
					"NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d " + 
					"where d.pono=A.pono),0)),0))/100))/s.CURRENCYUSEQT)*a.recqty,0) total,recvdate," + 
					"isnull((inbound_gst),'') as Tax,p.TAXID, " + 
					"isnull(((((isnull(a.unitcost*(s.CURRENCYUSEQT),0) + " + 
					"isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) + (isnull(a.unitcost*(s.CURRENCYUSEQT),0) * " + 
					"(((isnull(P.LOCALEXPENSES,0)+CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C " + 
					"LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/" + 
					"NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/" + 
					"s.CURRENCYUSEQT)* (p.inbound_gst)) *a.recqty)/100,0) as taxval  " + 
					" from ["+plant+"_recvdet] a, ["+plant+"_pohdr] p, ["+plant+"_podet] s  " + 
					" where A.PONO=P.PONO AND s.pono=A.pono and s.ITEM=A.ITEM and a.pono<>''  " + 
					" UNION " + 
					" SELECT a.PLANT,a.CURRENCYID,b.CURRENCYUSEQT,isnull(a.grno,'') grno,B.BILL as pono,ISNULL(C.LNNO,'') lnno, (SELECT VNAME FROM "+plant+"_VENDMST WHERE VENDNO=B.VENDNO) AS cname, C.ITEM, " + 
					"isnull((select ITEMDESC from "+plant+"_ITEMMST where ITEM=C.ITEM),'') as ITEMDESC,upper(isnull(a.batch,'')) batch," + 
					"isnull(a.CRBY,'') as users, isnull(a.ordqty,0) ordqty, isnull(a.recqty,0)recqty,a.putawayqty,isnull(a.reverseqty,0)reverseqty," + 
					"isnull(upper(a.loc),'')loc,isnull(a.status,'') status,isnull(a.remark,'')remark,a.crat,a.crby, ISNULL(C.UOM,'') uom," + 
					"isnull((SELECT TOP 1 expiredate FROM "+plant+"_INVMST " + 
					"WHERE PLANT=A.PLANT AND ITEM=A.ITEM AND LOC =A.LOC AND USERFLD4 =A.BATCH),'') expiredate," + 
					"ISNULL(RECVDATE,SUBSTRING(a.crat,7,2)+'/'+ SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4))  as transactiondate," + 
					"SUBSTRING(a.crat,7,2)+'/'+ ISNULL(SUBSTRING(a.crat,5,2)+'/'+ SUBSTRING(a.crat,1,4),'') as transactioncratdate," + 
					"CAST((SUBSTRING(a.crat, 1, 4) + SUBSTRING(a.crat, 5, 2) + SUBSTRING(a.crat, 7, 2)) AS date)  AS toexceltransactioncratdate," + 
					"ISNULL(CAST((SUBSTRING(a.recvdate,7,4) + SUBSTRING(a.recvdate,4,2) + SUBSTRING(a.recvdate,1,2)) AS date),'') " + 
					"AS toexceltransactiondate," + 
					"isnull((select prd_brand_id from "+plant+"_ITEMMST where item=a.item ),'')  as prd_brand_id," + 
					"isnull((select prd_cls_id from "+plant+"_ITEMMST where item=a.item ),'') as prd_cls_id," + 
					"isnull((select PRD_DEPT_ID from "+plant+"_ITEMMST where item=a.item ),'') as PRD_DEPT_ID," + 
					"isnull((select itemtype from "+plant+"_ITEMMST where item=a.item ),'') as itemtype," + 
					"isnull((isnull(a.unitcost*(c.CURRENCYUSEQT),0) + isnull((SELECT E.LANDED_COST from " + 
					"["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) " + 
					"+ (isnull(a.unitcost*(c.CURRENCYUSEQT),0) * (((" + 
					"CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/" + 
					"NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d where d.pono=A.pono),0)),0))/100))/" + 
					"c.CURRENCYUSEQT,0) unitcost," + 
					"isnull(((isnull(a.unitcost*(c.CURRENCYUSEQT),0) " + 
					"+ isnull((SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D " + 
					"ON C.PONO = D.PONO AND C.GRNO = D.GRNO LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST),0) " + 
					"+ (isnull(a.unitcost*(c.CURRENCYUSEQT),0) " + 
					"* (((" + 
					"CASE WHEN (SELECT E.LANDED_COST from ["+plant+"_RECVDET] C LEFT JOIN ["+plant+"_FINBILLHDR] D ON C.PONO = D.PONO AND C.GRNO = D.GRNO " + 
					"LEFT JOIN ["+plant+"_FINBILLDET] E ON D.ID = E.BILLHDRID  " + 
					"WHERE C.PONO = A.PONO AND C.LNNO = A.LNNO  AND C.GRNO = A.GRNO AND E.ITEM = A.ITEM " + 
					"GROUP BY C.PONO,C.LNNO,C.GRNO,E.ITEM,E.LANDED_COST) is null THEN SHIPPINGCOST ELSE 0 END)*100)/" + 
					"NULLIF((ISNULL((select SUM(d.qtyor*d.UNITCOST*d.CURRENCYUSEQT) from ["+plant+"_podet] d " + 
					"where d.pono=A.pono),0)),0))/100))/c.CURRENCYUSEQT)*a.recqty,0) total,recvdate," + 
					"isnull(B.INBOUND_GST,'') as Tax,B.TAXID as TAXID, " + 
					"isnull(((isnull(c.COST,a.unitcost) * " + 
					"(B.INBOUND_GST))*a.recqty)/100,'') as taxval  " + 
					"FROM ["+plant+"_recvdet] A JOIN ["+plant+"_FINBILLHDR] B on A.GRNO = B.GRNO  JOIN ["+plant+"_FINBILLDET] C ON B.ID = C.BILLHDRID " + 
					"where B.PONO='' AND B.BILL<>'' " + 
					") A WHERE A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				if (ht.get("a.ITEM") != null) {
					sql_PO.append(" AND A.ITEM = '" + ht.get("a.ITEM") + "'");
				}
				if (ht.get("a.CNAME") != null) {
					sql_PO.append(" AND A.CNAME LIKE '%" + ht.get("a.CNAME")+ "%'");
				}
				if (ht.get("a.CNAME_LOAN") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%" + ht.get("a.CNAME_LOAN") + "%' and tran_type='LO' ");
				}
				if (ht.get("a.CNAME_TO") != null) {
					sql_PO.append(" AND a.CNAME LIKE  '%"+ ht.get("a.CNAME_TO") + "%'  and tran_type='TO'");
				}
				if (ht.get("a.PONO") != null) {
					sql_PO.append(" AND A.PONO = '" + ht.get("a.PONO") + "'");
				}
			   if (ht.get("ORDERTYPE") != null) {
				  sql_PO.append("  AND a.PONO IN (SELECT PONO FROM ["+plant+"_POHDR] WHERE ORDERTYPE  = '"+ ht.get("ORDERTYPE")+ "' )"  ); 
				}
				if (ht.get("a.BATCH") != null) {
					sql_PO.append(" AND a.BATCH = '" + ht.get("a.BATCH") + "'");
				}
				if (ht.get("a.ITEMDESC") != null) {
					sql_PO.append(" AND a.ITEMDESC = '" + ht.get("a.ITEMDESC")
							+ "'");
				}
				if (ht.get("a.LOC") != null) {
					sql_PO.append(" AND a.LOC = '" + ht.get("a.LOC") 
							+ "'" );
					
				}
				if (ht.get("LOC_TYPE_ID") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID  = '"+ ht.get("LOC_TYPE_ID")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID2") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID2  = '"+ ht.get("LOC_TYPE_ID2")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("LOC_TYPE_ID3") != null) {
					sql_PO.append("  AND a.LOC IN (SELECT LOC FROM ["+plant+"_LOCMST] WHERE LOC_TYPE_ID3  = '"+ ht.get("LOC_TYPE_ID3")
							+ "' AND LOC=a.LOC)"  );
				}
				if (ht.get("a.GRNO") != null) {
					sql_PO.append(" AND a.GRNO = '" + ht.get("a.GRNO") 
							+ "'" );
				}
				
				//end
				if (ht.get("PRD_CLS_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_CLS_ID  = '"+ ht.get("PRD_CLS_ID")+ "' )"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_BRAND_ID  = '"+ ht.get("PRD_BRAND_ID")+ "' )"  );
				}
				if (ht.get("PRD_DEPT_ID") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE PRD_DEPT_ID  = '"+ ht.get("PRD_DEPT_ID")+ "' )"  );
				}
				if (ht.get("ITEMTYPE") != null) {
					sql_PO.append("  AND a.ITEM IN (SELECT ITEM FROM ["+plant+"_ITEMMST] WHERE ITEMTYPE  = '"+ ht.get("ITEMTYPE")+ "' )"  );
				}
				if (ht.get("a.REMARK") != null && ht.get("a.REMARK")!="") {
					sql_PO.append(" AND SUBSTRING(a.REMARK, CHARINDEX(',',a.REMARK) + 1, LEN(a.REMARK)) LIKE '%" + ht.get("a.REMARK")+ "%' and a.REMARK IS NOT NULL AND a.REMARK <> ''");
							
				}
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPickingSummaryList:", e);
		}
		return arrList;
	}
  
  
  
  public ArrayList getPosSalesorderSummaryByOrderwise(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" GROUP BY A.DONO,A.CRAT,A.OUTLET,A.DELDATE,A.CustCode,A.CustName,A.JobNum,A.TAXID,C.ISZERO,A.OUTBOUND_GST ORDER BY CAST((SUBSTRING(A.DELDATE,7,4) + SUBSTRING(A.DELDATE,4,2) + SUBSTRING(A.DELDATE,1,2)) AS date) desc";
			
			StringBuffer sql_PO = new StringBuffer();
			//sql_PO.append("SELECT A.DELDATE AS SALESDATE,A.DONO,A.CRAT,A.CustCode,A.CustName,A.JobNum,CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END AS TAXAMOUNT,A.OUTLET AS OUTLET,(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) + (CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END)) AS TOTAL_PRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			sql_PO.append("SELECT A.DELDATE AS SALESDATE,A.DONO,A.CRAT,A.CustCode,A.CustName,A.JobNum,CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END AS TAXAMOUNT,A.OUTLET AS OUTLET,(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) + (CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END)) - (isnull((SELECT (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE FROM "+plant+"_POSEXCHANGEDET PE WHERE PE.EXDONO = A.DONO),0)) AS TOTAL_PRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT,isnull((SELECT (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE FROM "+plant+"_POSEXCHANGEDET PE WHERE PE.EXDONO = A.DONO),0) EXPRICE FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				
				if (ht.get("ORDERNO") != null) {
					String ORDERNO = (String) ht.get("ORDERNO");
					sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
				}
				if (ht.get("CUSTOMERCODE") != null) {
					String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
					sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
				}
				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
				}
				if (ht.get("SALESMAN") != null) {
					String SALESMAN = (String) ht.get("SALESMAN");
					sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
				}
				if (ht.get("ITEM") != null) {
					String ITEM = (String) ht.get("ITEM");
					sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
				}

				if (ht.get("PRD_CLS_ID") != null) {
					String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
					sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
					sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
				}
				if (ht.get("PRD_TYPE_ID") != null) {
					String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
					sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
				}
				if (ht.get("PAYMENTTYPE") != null) {
					String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
					sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
				}
			
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderSummaryByOrderwise:", e);
		}
		return arrList;
	}
  
  
  public ArrayList getPosSalesorderSummaryByOrderwisewithtax(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" GROUP BY A.DONO,A.CRAT,A.OUTLET,A.ITEM_RATES,A.DELDATE,A.CustCode,A.CustName,A.JobNum,A.TAXID,C.ISZERO,A.OUTBOUND_GST ORDER BY CAST((SUBSTRING(A.DELDATE,7,4) + SUBSTRING(A.DELDATE,4,2) + SUBSTRING(A.DELDATE,1,2)) AS date) desc";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT A.DELDATE AS SALESDATE,A.DONO,A.CRAT,A.CustCode,A.CustName,A.JobNum,"
					+ "(CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END) END ELSE 0 END) AS TAXAMOUNT,"
					+ "A.OUTLET AS OUTLET,(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,"
					+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) + (CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END) END ELSE 0 END)) END) - (isnull((SELECT (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE FROM "+plant+"_POSEXCHANGEDET PE WHERE PE.EXDONO = A.DONO),0)) AS TOTAL_PRICE,"
					+ "SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT,isnull((SELECT (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE FROM "+plant+"_POSEXCHANGEDET PE WHERE PE.EXDONO = A.DONO),0) EXPRICE,"
					+ "ISNULL((SELECT STRING_AGG(cm.PAYMENTMODE,',') FROM "+ plant+"_POS_PAYMODE_AMOUNT AS cm WHERE cm.DONO = A.DONO),'') AS PAYMENTMODE"
					+ " FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				
				if (ht.get("ORDERNO") != null) {
					String ORDERNO = (String) ht.get("ORDERNO");
					sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
				}
				if (ht.get("CUSTOMERCODE") != null) {
					String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
					sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
				}
				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
				}
				if (ht.get("SALESMAN") != null) {
					String SALESMAN = (String) ht.get("SALESMAN");
					sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
				}
				if (ht.get("ITEM") != null) {
					String ITEM = (String) ht.get("ITEM");
					sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
				}

				if (ht.get("PRD_CLS_ID") != null) {
					String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
					sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
					sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
				}
				if (ht.get("PRD_TYPE_ID") != null) {
					String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
					sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
				}
				if (ht.get("PAYMENTTYPE") != null) {
					String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
					sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
				}
			
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderSummaryByOrderwise:", e);
		}
		return arrList;
	}
  
  public ArrayList getPosSalesDiscountSummaryByOrderwise(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
	  ArrayList arrList = new ArrayList();
	  String sCondition = "";
	  String extraOrderbyCon="";
	  
	  try {
		  
		  MovHisDAO movHisDAO = new MovHisDAO();
		  movHisDAO.setmLogger(mLogger);
		  
		  if (afrmDate.length() > 0) {
			  sCondition = sCondition
					  + " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
					  + afrmDate + "' AS date)";
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  } else {
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  }
		  
		  
		  sCondition = sCondition + "  ";
		  extraOrderbyCon =" GROUP BY A.DONO,A.OUTLET,A.DELDATE,A.CustCode,A.CustName,A.JobNum,A.TAXID,C.ISZERO,A.OUTBOUND_GST ORDER BY CAST((SUBSTRING(A.DELDATE,7,4) + SUBSTRING(A.DELDATE,4,2) + SUBSTRING(A.DELDATE,1,2)) AS date) desc";
		  
		  StringBuffer sql_PO = new StringBuffer();
		  sql_PO.append("SELECT A.DELDATE AS SALESDATE,A.DONO,A.CustCode,A.CustName,A.JobNum,CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END AS TAXAMOUNT,A.OUTLET AS OUTLET,(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) + (CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END)) AS TOTAL_PRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND B.DISCOUNT<>0 AND A.PLANT='"+plant+"'"+ sCondition);
		  if (ht.size() > 0) {
			  
			  if (ht.get("ORDERNO") != null) {
				  String ORDERNO = (String) ht.get("ORDERNO");
				  sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
			  }
			  if (ht.get("CUSTOMERCODE") != null) {
				  String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
				  sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
			  }
			  if (ht.get("OUTLET") != null) {
				  String OUTLET = (String) ht.get("OUTLET");
				  sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
			  }
			  if (ht.get("TERMINAL") != null) {
				  String TERMINAL = (String) ht.get("TERMINAL");
				  sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
			  }
			  if (ht.get("CASHIER") != null) {
				  String CASHIER = (String) ht.get("CASHIER");
				  sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
			  }
			  if (ht.get("SALESMAN") != null) {
				  String SALESMAN = (String) ht.get("SALESMAN");
				  sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
			  }
			  if (ht.get("ITEM") != null) {
				  String ITEM = (String) ht.get("ITEM");
				  sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
			  }
			  
			  if (ht.get("PRD_CLS_ID") != null) {
				  String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
				  sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
				  
			  }
			  if (ht.get("PRD_BRAND_ID") != null) {
				  String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
				  sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
			  }
			  if (ht.get("PRD_TYPE_ID") != null) {
				  String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
				  sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
			  }
			  
			  
		  }
		  
		  
		  arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
		  
		  
	  } catch (Exception e) {
		  this.mLogger.exception(this.printLog,
				  "Exception :repportUtil :: getPosSalesorderSummaryByOrderwise:", e);
	  }
	  return arrList;
  }
  
  
  
  public ArrayList getPosSalesorderSummaryByProductWise(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" GROUP BY B.ITEM,B.ItemDesc";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT  B.ITEM,B.ItemDesc AS ITEMDESC,SUM(B.QTYOR) AS QTY,SUM(B.UNITPRICE * B.QTYOR) AS AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				
				if (ht.get("ORDERNO") != null) {
					String ORDERNO = (String) ht.get("ORDERNO");
					sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
				}
				if (ht.get("CUSTOMERCODE") != null) {
					String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
					sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
				}
				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
				}
				if (ht.get("SALESMAN") != null) {
					String SALESMAN = (String) ht.get("SALESMAN");
					sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
				}
				if (ht.get("ITEM") != null) {
					String ITEM = (String) ht.get("ITEM");
					sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
				}

				if (ht.get("PRD_CLS_ID") != null) {
					String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
					sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
					sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
				}
				if (ht.get("PRD_TYPE_ID") != null) {
					String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
					sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
				}
				if (ht.get("PAYMENTTYPE") != null) {
					String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
					sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
				}
			
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderSummaryByProductWise:", e);
		}
		return arrList;
	}
  
  
  public ArrayList getPosSalesorderSummaryByCustomerwise(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" GROUP BY A.CustCode,A.CustName,A.TAXID,C.ISZERO,A.OUTBOUND_GST,B.TAX_TYPE) AS Z  GROUP BY Z.CustCode,Z.CustName";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT Z.CustCode AS CUSTOMER,Z.CustName AS CUSTOMERNAME,SUM(ISNULL(Z.TAXAMOUNT,0)) AS TAXAMOUNT,SUM(ISNULL(Z.AMOUNT,0)) AS AMOUNT,SUM(ISNULL(Z.DISCOUNT_AMOUNT,0)) AS DISCOUNT_AMOUNT,SUM(ISNULL(Z.TOTAL_PRICE,0)) AS TOTAL_PRICE FROM (SELECT A.CustCode,A.CustName,CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END AS TAXAMOUNT,(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) + (CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END)) AS TOTAL_PRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				
				if (ht.get("ORDERNO") != null) {
					String ORDERNO = (String) ht.get("ORDERNO");
					sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
				}
				if (ht.get("CUSTOMERCODE") != null) {
					String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
					sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
				}
				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
				}
				if (ht.get("SALESMAN") != null) {
					String SALESMAN = (String) ht.get("SALESMAN");
					sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
				}
				if (ht.get("ITEM") != null) {
					String ITEM = (String) ht.get("ITEM");
					sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
				}

				if (ht.get("PRD_CLS_ID") != null) {
					String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
					sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
					sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
				}
				if (ht.get("PRD_TYPE_ID") != null) {
					String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
					sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
				}
				if (ht.get("PAYMENTTYPE") != null) {
					String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
					sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
				}
			
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderSummaryByCustomerwise:", e);
		}
		return arrList;
	}
  
  
  public ArrayList getPosSalesorderSummaryByCustomerwiseWithTax(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" GROUP BY A.CustCode,A.ITEM_RATES,A.CustName,A.TAXID,C.ISZERO,A.OUTBOUND_GST,B.TAX_TYPE) AS Z  GROUP BY Z.CustCode,Z.CustName";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT Z.CustCode AS CUSTOMER,Z.CustName AS CUSTOMERNAME,SUM(ISNULL(Z.TAXAMOUNT,0)) AS TAXAMOUNT,SUM(ISNULL(Z.AMOUNT,0)) AS AMOUNT,SUM(ISNULL(Z.DISCOUNT_AMOUNT,0)) AS DISCOUNT_AMOUNT,SUM(ISNULL(Z.TOTAL_PRICE,0)) AS TOTAL_PRICE FROM (SELECT A.CustCode,A.CustName,"
					+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END) AS TAXAMOUNT,"
					+ "(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,"
					+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) ELSE (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) "
					+ "ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END ) END) AS TOTAL_PRICE,"
					+ "SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				
				if (ht.get("ORDERNO") != null) {
					String ORDERNO = (String) ht.get("ORDERNO");
					sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
				}
				if (ht.get("CUSTOMERCODE") != null) {
					String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
					sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
				}
				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
				}
				if (ht.get("SALESMAN") != null) {
					String SALESMAN = (String) ht.get("SALESMAN");
					sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
				}
				if (ht.get("ITEM") != null) {
					String ITEM = (String) ht.get("ITEM");
					sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
				}

				if (ht.get("PRD_CLS_ID") != null) {
					String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
					sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
					sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
				}
				if (ht.get("PRD_TYPE_ID") != null) {
					String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
					sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
				}
				if (ht.get("PAYMENTTYPE") != null) {
					String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
					sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
				}
			
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderSummaryByCustomerwise:", e);
		}
		return arrList;
	}
  
  
  
  public ArrayList getPosSalesorderSummaryByCategorywise(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" GROUP BY D.PRD_CLS_ID,A.TAXID,C.ISZERO,A.OUTBOUND_GST,B.TAX_TYPE) AS Z GROUP BY Z.PRD_CLS_ID";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT Z.PRD_CLS_ID AS CATEGORY, SUM(Z.QTY) AS QTY, SUM(Z.TOTAL_PRICE) AS AMOUNT FROM (SELECT D.PRD_CLS_ID,(SUM(B.QTYOR)) as QTY,CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END AS TAXAMOUNT,(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) + (CASE WHEN ISNULL(A.TAXID,0) > 0 THEN CASE WHEN C.ISZERO = 1 THEN 0 ELSE ((SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100) * A.OUTBOUND_GST END ELSE 0 END)) AS TOTAL_PRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				
				if (ht.get("ORDERNO") != null) {
					String ORDERNO = (String) ht.get("ORDERNO");
					sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
				}
				if (ht.get("CUSTOMERCODE") != null) {
					String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
					sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
				}
				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
				}
				if (ht.get("SALESMAN") != null) {
					String SALESMAN = (String) ht.get("SALESMAN");
					sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
				}
				if (ht.get("ITEM") != null) {
					String ITEM = (String) ht.get("ITEM");
					sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
				}

				if (ht.get("PRD_CLS_ID") != null) {
					String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
					sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
					sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
				}
				if (ht.get("PRD_TYPE_ID") != null) {
					String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
					sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
				}
				if (ht.get("PAYMENTTYPE") != null) {
					String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
					sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
				}
			
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderSummaryByCategorywise:", e);
		}
		return arrList;
	}
  
  public ArrayList getPosSalesorderSummaryByCategorywiseWithTax(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" GROUP BY D.PRD_CLS_ID,A.TAXID,C.ISZERO,A.OUTBOUND_GST,B.TAX_TYPE,A.ITEM_RATES) AS Z GROUP BY Z.PRD_CLS_ID";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT Z.PRD_CLS_ID AS CATEGORY, SUM(Z.QTY) AS QTY, SUM(Z.TOTAL_PRICE) AS AMOUNT FROM (SELECT D.PRD_CLS_ID,(SUM(B.QTYOR)) as QTY,"
					+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END) AS TAXAMOUNT,"
					+ "(SUM(B.UNITPRICE * B.QTYOR)) as AMOUNT,"
					+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) ELSE (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) "
					+ "ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END ) END) AS TOTAL_PRICE,"
					+ "SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				
				if (ht.get("ORDERNO") != null) {
					String ORDERNO = (String) ht.get("ORDERNO");
					sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
				}
				if (ht.get("CUSTOMERCODE") != null) {
					String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
					sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
				}
				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
				}
				if (ht.get("SALESMAN") != null) {
					String SALESMAN = (String) ht.get("SALESMAN");
					sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
				}
				if (ht.get("ITEM") != null) {
					String ITEM = (String) ht.get("ITEM");
					sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
				}

				if (ht.get("PRD_CLS_ID") != null) {
					String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
					sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
					
				}
				if (ht.get("PRD_BRAND_ID") != null) {
					String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
					sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
				}
				if (ht.get("PRD_TYPE_ID") != null) {
					String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
					sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
				}
				if (ht.get("PAYMENTTYPE") != null) {
					String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
					sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
				}
			
				
			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderSummaryByCategorywise:", e);
		}
		return arrList;
	}
  public ArrayList getPosSalesorderSummaryByTerminalwise(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
	  ArrayList arrList = new ArrayList();
	  String sCondition = "";
	  String extraOrderbyCon="";
	  
	  try {
		  
		  MovHisDAO movHisDAO = new MovHisDAO();
		  movHisDAO.setmLogger(mLogger);
		  
		  if (afrmDate.length() > 0) {
			  sCondition = sCondition
					  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) >= CAST('"
					  + afrmDate + "' AS date)";
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  } else {
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  }
		  
		  
		  sCondition = sCondition + "  ";
		  extraOrderbyCon =" GROUP BY D.ORDDATE,D.OUTLET,D.TERMINAL ORDER BY ORDDATE DESC ";
		  
		  StringBuffer sql_PO = new StringBuffer();
		  sql_PO.append("SELECT D.ORDDATE,FORMAT(CAST((SUBSTRING(D.ORDDATE,4,2)+'/'+SUBSTRING(D.ORDDATE,1,2)+'/'+SUBSTRING(D.ORDDATE,7,4)) AS DATE), 'ddd') AS ORDDAY,D.OUTLET,D.TERMINAL,(SUM((D.TOTAL_PRICE) - (CASE WHEN ISNULL(D.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((D.TOTAL_PRICE)/100)*D.DISCOUNT, 0) ELSE ISNULL(D.DISCOUNT, 0) END) )) AS TOTALPRICE,"
		  		+ "SUM(D.UNITCOST) AS TOTALCOST "
		  		//+ ",ISNULL((select TOP 1 CRAT AS FROMDATE from "+plant+"_POSTILLMONEY S where S.TMDATE=D.ORDDATE),'') AS FROMDATE, "
		  		//+ "ISNULL((select TOP 1 CRAT AS TODATE from "+plant+"_POSSHIFTCLOSEHDR S where S.SHIFTDATE=D.ORDDATE ORDER BY CRAT DESC),'') AS TODATE, "
		  		//+ "ISNULL((select TOP 1 TERMINAL_STARTTIME from "+plant+"_POSOUTLETSTERMINALS S where S.TERMINAL=D.TERMINAL),'00:00') AS TERMINAL_STARTTIME, "
		  		//+ "ISNULL((select TOP 1 TERMINAL_ENDTIME from "+plant+"_POSOUTLETSTERMINALS S where S.TERMINAL=D.TERMINAL),'00:00') AS TERMINAL_ENDTIME  "
		  		+ "FROM (SELECT A.ORDDATE,A.OUTLET,A.TERMINAL,A.DISCOUNT_TYPE,A.DISCOUNT,SUM((B.UNITCOST * B.QTYOR)) AS UNITCOST,(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) AS TOTAL_PRICE "
		  		+ "FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND A.PLANT='"+plant+"' "+ sCondition +" GROUP BY A.ORDDATE,A.OUTLET,A.TERMINAL,A.OUTBOUND_GST,A.DISCOUNT_TYPE,A.DISCOUNT) AS D WHERE OUTLET !='' ");
		  if (ht.size() > 0) {
			  
			  /*if (ht.get("ORDERNO") != null) {
				  String ORDERNO = (String) ht.get("ORDERNO");
				  sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
			  }
			  if (ht.get("CUSTOMERCODE") != null) {
				  String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
				  sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
			  }*/
			  if (ht.get("OUTLET") != null) {
				  String OUTLET = (String) ht.get("OUTLET");
				  sql_PO.append(" AND D.OUTLET = '" + OUTLET + "'");
			  }
			  if (ht.get("TERMINAL") != null) {
				  String TERMINAL = (String) ht.get("TERMINAL");
				  sql_PO.append(" AND D.TERMINAL = '" + TERMINAL + "'");
			  }
			  /*if (ht.get("CASHIER") != null) {
				  String CASHIER = (String) ht.get("CASHIER");
				  sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
			  }
			  if (ht.get("SALESMAN") != null) {
				  String SALESMAN = (String) ht.get("SALESMAN");
				  sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
			  }
			  if (ht.get("ITEM") != null) {
				  String ITEM = (String) ht.get("ITEM");
				  sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
			  }
			  
			  if (ht.get("PRD_CLS_ID") != null) {
				  String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
				  sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
				  
			  }
			  if (ht.get("PRD_BRAND_ID") != null) {
				  String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
				  sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
			  }
			  if (ht.get("PRD_TYPE_ID") != null) {
				  String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
				  sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
			  }
			  if (ht.get("PAYMENTTYPE") != null) {
				  String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
				  sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
			  }*/
			  
			  
		  }
		  
		  
		  arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
		  
		  
	  } catch (Exception e) {
		  this.mLogger.exception(this.printLog,
				  "Exception :repportUtil :: getPosSalesorderSummaryByTerminalwise:", e);
	  }
	  return arrList;
  }
  
  public ArrayList getPosSalesorderSummaryByTerminalwiseWithExg(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
	  ArrayList arrList = new ArrayList();
	  String sCondition = "";
	  String extraOrderbyCon="";
	  
	  try {
		  
		  MovHisDAO movHisDAO = new MovHisDAO();
		  movHisDAO.setmLogger(mLogger);
		  
		  if (afrmDate.length() > 0) {
			  sCondition = sCondition
					  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) >= CAST('"
					  + afrmDate + "' AS date)";
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  } else {
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  }
		  
		  
		  sCondition = sCondition + "  ";
		  extraOrderbyCon =" GROUP BY D.ORDDATE,D.OUTLET,D.TERMINAL ORDER BY CONVERT(DATETIME,ORDDATE,103)  DESC ";
		  
		  StringBuffer sql_PO = new StringBuffer();
		  sql_PO.append("SELECT D.ORDDATE,FORMAT(CAST((SUBSTRING(D.ORDDATE,4,2)+'/'+SUBSTRING(D.ORDDATE,1,2)+'/'+SUBSTRING(D.ORDDATE,7,4)) AS DATE), 'ddd') AS ORDDAY,D.OUTLET,D.TERMINAL,(SUM((D.TOTAL_PRICE) - (CASE WHEN ISNULL(D.DISCOUNT_TYPE,'%') = '%' "
		  		+ "THEN ISNULL(((D.TOTAL_PRICE)/100)*D.DISCOUNT, 0) ELSE ISNULL(D.DISCOUNT, 0) END) ) - SUM(D.EXPRICE)) AS TOTALPRICE,"
		  		+ "(SUM(D.UNITCOST)-SUM(D.EXCOST)) AS TOTALCOST "
		  		//+ ",ISNULL((select TOP 1 CRAT AS FROMDATE from "+plant+"_POSTILLMONEY S where S.TMDATE=D.ORDDATE),'') AS FROMDATE, "
		  		//+ "ISNULL((select TOP 1 CRAT AS TODATE from "+plant+"_POSSHIFTCLOSEHDR S where S.SHIFTDATE=D.ORDDATE ORDER BY CRAT DESC),'') AS TODATE, "
		  		//+ "ISNULL((select TOP 1 TERMINAL_STARTTIME from "+plant+"_POSOUTLETSTERMINALS S where S.TERMINAL=D.TERMINAL),'00:00') AS TERMINAL_STARTTIME, "
		  		//+ "ISNULL((select TOP 1 TERMINAL_ENDTIME from "+plant+"_POSOUTLETSTERMINALS S where S.TERMINAL=D.TERMINAL),'00:00') AS TERMINAL_ENDTIME  "
		  		+ "FROM (SELECT A.ORDDATE,A.OUTLET,A.TERMINAL,A.DISCOUNT_TYPE,A.DISCOUNT,ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) AS EXPRICE,ISNULL(SUM(ISNULL(IT.COST,0)*PE.QTY),0) AS EXCOST,SUM((B.UNITCOST * B.QTYOR)) AS UNITCOST,(SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) AS TOTAL_PRICE "
		  		+ "FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO LEFT JOIN "+plant+"_POSEXCHANGEDET AS PE ON PE.EXDONO = A.DONO LEFT JOIN "+plant+"_ITEMMST AS IT ON IT.ITEM=PE.ITEM  WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND ISNULL(A.ORDDATE,'') !='' AND A.PLANT='"+plant+"' "+ sCondition +" GROUP BY A.ORDDATE,A.OUTLET,A.TERMINAL,A.OUTBOUND_GST,A.DISCOUNT_TYPE,A.DISCOUNT) AS D WHERE OUTLET !='' ");
		  if (ht.size() > 0) {
			  
			  /*if (ht.get("ORDERNO") != null) {
				  String ORDERNO = (String) ht.get("ORDERNO");
				  sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
			  }
			  if (ht.get("CUSTOMERCODE") != null) {
				  String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
				  sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
			  }*/
			  if (ht.get("OUTLET") != null) {
				  String OUTLET = (String) ht.get("OUTLET");
				  sql_PO.append(" AND D.OUTLET = '" + OUTLET + "'");
			  }
			  if (ht.get("TERMINAL") != null) {
				  String TERMINAL = (String) ht.get("TERMINAL");
				  sql_PO.append(" AND D.TERMINAL = '" + TERMINAL + "'");
			  }
			  /*if (ht.get("CASHIER") != null) {
				  String CASHIER = (String) ht.get("CASHIER");
				  sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
			  }
			  if (ht.get("SALESMAN") != null) {
				  String SALESMAN = (String) ht.get("SALESMAN");
				  sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
			  }
			  if (ht.get("ITEM") != null) {
				  String ITEM = (String) ht.get("ITEM");
				  sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
			  }
			  
			  if (ht.get("PRD_CLS_ID") != null) {
				  String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
				  sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
				  
			  }
			  if (ht.get("PRD_BRAND_ID") != null) {
				  String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
				  sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
			  }
			  if (ht.get("PRD_TYPE_ID") != null) {
				  String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
				  sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
			  }
			  if (ht.get("PAYMENTTYPE") != null) {
				  String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
				  sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
			  }*/
			  
			  
		  }
		  
		  
		  arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
		  
		  
	  } catch (Exception e) {
		  this.mLogger.exception(this.printLog,
				  "Exception :repportUtil :: getPosSalesorderSummaryByTerminalwiseWithExg:", e);
	  }
	  return arrList;
  }
  
  
  public ArrayList getPosSalesorderSummaryByTerminalwiseWithExgWithTax(Hashtable ht, String afrmDate, String atoDate,String ordertype, String plant) {
	  ArrayList arrList = new ArrayList();
	  String sCondition = "";
	  String extraOrderbyCon="";
	  
	  try {
		  
		  MovHisDAO movHisDAO = new MovHisDAO();
		  movHisDAO.setmLogger(mLogger);
		  
		  if (afrmDate.length() > 0) {
			  sCondition = sCondition
					  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) >= CAST('"
					  + afrmDate + "' AS date)";
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  } else {
			  if (atoDate.length() > 0) {
				  sCondition = sCondition
						  + " AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(A.ORDDATE, 4, 2) + '-' + SUBSTRING(A.ORDDATE, 1, 2)) AS date) <= CAST('"
						  + atoDate + "' AS date)";
			  }
		  }
		  
		  
		  sCondition = sCondition + "  ";
		  //extraOrderbyCon =" GROUP BY D.ORDDATE,D.OUTLET,D.TERMINAL,D.GST,D.EGST ORDER BY CONVERT(DATETIME,ORDDATE,103)  DESC ";
		  extraOrderbyCon =" GROUP BY D.ORDDATE,D.OUTLET,D.TERMINAL ORDER BY CONVERT(DATETIME,ORDDATE,103)  DESC ";
		  
		  StringBuffer sql_PO = new StringBuffer();
		  sql_PO.append("SELECT D.ORDDATE,FORMAT(CAST((SUBSTRING(D.ORDDATE,4,2)+'/'+SUBSTRING(D.ORDDATE,1,2)+'/'+SUBSTRING(D.ORDDATE,7,4)) AS DATE), 'ddd') AS ORDDAY,D.OUTLET,D.TERMINAL,(SUM((D.TOTAL_PRICE) - (CASE WHEN ISNULL(D.DISCOUNT_TYPE,'%') = '%' "
		  		+ "THEN ISNULL(((D.TOTAL_PRICE)/100)*D.DISCOUNT, 0) ELSE ISNULL(D.DISCOUNT, 0) END) ) - SUM(D.EXPRICE)) AS TOTALPRICE,(SUM(ISNULL(D.GST,0))-SUM(ISNULL(D.EGST,0))) AS TAXAMOUNT,"
		  		+ "(SUM(D.UNITCOST)-SUM(D.EXCOST)) AS TOTALCOST "
		  		//+ ",ISNULL((select TOP 1 CRAT AS FROMDATE from "+plant+"_POSTILLMONEY S where S.TMDATE=D.ORDDATE),'') AS FROMDATE, "
		  		//+ "ISNULL((select TOP 1 CRAT AS TODATE from "+plant+"_POSSHIFTCLOSEHDR S where S.SHIFTDATE=D.ORDDATE ORDER BY CRAT DESC),'') AS TODATE, "
		  		//+ "ISNULL((select TOP 1 TERMINAL_STARTTIME from "+plant+"_POSOUTLETSTERMINALS S where S.TERMINAL=D.TERMINAL),'00:00') AS TERMINAL_STARTTIME, "
		  		//+ "ISNULL((select TOP 1 TERMINAL_ENDTIME from "+plant+"_POSOUTLETSTERMINALS S where S.TERMINAL=D.TERMINAL),'00:00') AS TERMINAL_ENDTIME  "
		  		+ "FROM (SELECT A.ORDDATE,A.OUTLET,A.TERMINAL,A.DISCOUNT_TYPE,A.DISCOUNT,"
		  		+ "isnull((SELECT ISNULL(SUM(ISNULL(IT.COST,0)*PE.QTY),0) AS EXCOST FROM "+plant+"_POSEXCHANGEDET PE JOIN "+plant+"_ITEMMST AS IT ON IT.ITEM=PE.ITEM  WHERE PE.EXDONO = A.DONO),0) EXCOST,"
		  		//+ "ISNULL(SUM(ISNULL(IT.COST,0)*PE.QTY),0) AS EXCOST,"
		  		+ "SUM((B.UNITCOST * B.QTYOR)) AS UNITCOST,"
		  		+ "isnull((SELECT (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE FROM "+plant+"_POSEXCHANGEDET PE WHERE PE.EXDONO = A.DONO),0) EXPRICE,"
		  		//+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0) ELSE (ISNULL(SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY),0)) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) END) AS EXPRICE,"
		  		+ "(CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) ELSE (SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * B.QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) + (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) "
		  		+ "ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END ) END) AS TOTAL_PRICE,"
		  		+ "CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) )) - (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/(100+A.OUTBOUND_GST))*100) ELSE (((SUM((B.UNITPRICE * QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ))/100)* A.OUTBOUND_GST) END  AS GST,"
		  		+ "isnull((SELECT (CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END) FROM "+plant+"_POSEXCHANGEDET PE WHERE PE.EXDONO = A.DONO),0) EGST  "
		  		//+ "CASE WHEN ISNULL(A.ITEM_RATES,1) = 1 THEN (SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)) - (((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY))/(100+A.OUTBOUND_GST))*100) ELSE ((SUM(ISNULL(PE.UNITPRICE,0)*PE.QTY)/100)* A.OUTBOUND_GST) END  AS EGST "
		  		//+ "FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO LEFT JOIN "+plant+"_POSEXCHANGEDET AS PE ON PE.EXDONO = A.DONO LEFT JOIN "+plant+"_ITEMMST AS IT ON IT.ITEM=PE.ITEM  WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND ISNULL(A.ORDDATE,'') !='' AND A.PLANT='"+plant+"' "+ sCondition +" GROUP BY A.ORDDATE,A.OUTLET,A.TERMINAL,A.OUTBOUND_GST,A.DISCOUNT_TYPE,A.DISCOUNT,A.ITEM_RATES) AS D WHERE OUTLET !='' ");
		  		+ "FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO  WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = '"+ordertype+"' AND ISNULL(A.ORDDATE,'') !='' AND A.PLANT='"+plant+"' "+ sCondition +" GROUP BY A.DONO,A.ORDDATE,A.OUTLET,A.TERMINAL,A.OUTBOUND_GST,A.DISCOUNT_TYPE,A.DISCOUNT,A.ITEM_RATES) AS D WHERE OUTLET !='' ");
		  if (ht.size() > 0) {
			  
			  /*if (ht.get("ORDERNO") != null) {
				  String ORDERNO = (String) ht.get("ORDERNO");
				  sql_PO.append(" AND A.DONO = '" + ORDERNO + "'");
			  }
			  if (ht.get("CUSTOMERCODE") != null) {
				  String CUSTOMERCODE = (String) ht.get("CUSTOMERCODE");
				  sql_PO.append(" AND A.CustCode = '" + CUSTOMERCODE + "'");
			  }*/
			  if (ht.get("OUTLET") != null) {
				  String OUTLET = (String) ht.get("OUTLET");
				  sql_PO.append(" AND D.OUTLET = '" + OUTLET + "'");
			  }
			  if (ht.get("TERMINAL") != null) {
				  String TERMINAL = (String) ht.get("TERMINAL");
				  sql_PO.append(" AND D.TERMINAL = '" + TERMINAL + "'");
			  }
			  /*if (ht.get("CASHIER") != null) {
				  String CASHIER = (String) ht.get("CASHIER");
				  sql_PO.append(" AND A.EMPNO = '" + CASHIER+ "'");
			  }
			  if (ht.get("SALESMAN") != null) {
				  String SALESMAN = (String) ht.get("SALESMAN");
				  sql_PO.append(" AND B.SALESMAN = '" + SALESMAN + "'");
			  }
			  if (ht.get("ITEM") != null) {
				  String ITEM = (String) ht.get("ITEM");
				  sql_PO.append(" AND B.ITEM = '" + ITEM + "'");
			  }
			  
			  if (ht.get("PRD_CLS_ID") != null) {
				  String  PRD_CLS_ID = (String) ht.get("PRD_CLS_ID");
				  sql_PO.append("  AND D.PRD_CLS_ID  = '"+ PRD_CLS_ID+ "'"  );
				  
			  }
			  if (ht.get("PRD_BRAND_ID") != null) {
				  String  PRD_BRAND_ID = (String) ht.get("PRD_BRAND_ID");
				  sql_PO.append("  AND D.PRD_BRAND_ID  = '"+ PRD_BRAND_ID+ "'"  );
			  }
			  if (ht.get("PRD_TYPE_ID") != null) {
				  String  PRD_TYPE_ID = (String) ht.get("PRD_TYPE_ID");
				  sql_PO.append("  AND D.PRD_TYPE_ID  = '"+ PRD_TYPE_ID+ "'"  );
			  }
			  if (ht.get("PAYMENTTYPE") != null) {
				  String  PAYMENTTYPE = (String) ht.get("PAYMENTTYPE");
				  sql_PO.append("  AND A.PAYMENTTYPE  = '"+ PAYMENTTYPE+ "'"  );
			  }*/
			  
			  
		  }
		  
		  
		  arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
		  
		  
	  } catch (Exception e) {
		  this.mLogger.exception(this.printLog,
				  "Exception :repportUtil :: getPosSalesorderSummaryByTerminalwiseWithExg:", e);
	  }
	  return arrList;
  }
  
  
  public ArrayList getPosSalesorderDetailByDono(String dono,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);

			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT B.ITEM,B.ItemDesc,B.QTYOR AS QTY,B.UNITPRICE,(B.UNITPRICE * B.QTYOR) as AMOUNT,((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' "
					+ "THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) AS TOTAL_PRICE,"
					+ "(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) "
					+ "ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B "
					+ "ON A.DONO=B.DONO WHERE A.DONO = '"+dono+"' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.PLANT='"+plant+"'");

			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderDetailByDono:", e);
		}
		return arrList;
	}
  
  public ArrayList getPosSalesorderDetailByCustomer(String CustNo,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);

			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT B.ITEM,B.ItemDesc,SUM(B.QTYOR) AS QTY,SUM(B.UNITPRICE) AS UNITPRICE,SUM(B.UNITPRICE * B.QTYOR) as AMOUNT,"
					+ "SUM((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) "
					+ "ELSE ISNULL(B.DISCOUNT, 0) END) ) AS TOTAL_PRICE,SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) "
					+ "ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B "
					+ "ON A.DONO=B.DONO WHERE A.CustCode = '"+CustNo+"' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.PLANT='"+plant+"' GROUP BY B.ITEM,B.ItemDesc");

			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderDetailByCustomer:", e);
		}
		return arrList;
	}
  
  public ArrayList getPosSalesorderDetailByProduct(String Item,String ordertype, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);

			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT A.DONO,A.DELDATE,A.CustCode,A.CustName,B.ITEM,B.QTYOR AS QTY,B.UNITPRICE,(B.UNITPRICE * B.QTYOR) as AMOUNT,((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' "
					+ "THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) AS TOTAL_PRICE,(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' "
					+ "THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO "
					+ "WHERE B.ITEM = '"+Item+"' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.PLANT='"+plant+"'");

			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderDetailByProduct:", e);
		}
		return arrList;
	}
  
  public ArrayList getPosSalesorderDetailByProduct(String Item,String ordertype, String plant,String afrmDate,String atoDate) {
	  ArrayList arrList = new ArrayList();
	  String sCondition = "";
	  String extraOrderbyCon="";
	  String dtCondStr="";
	  
	  try {
		  
		  MovHisDAO movHisDAO = new MovHisDAO();
		  movHisDAO.setmLogger(mLogger);
		  
		  dtCondStr =    " and ISNULL(A.DELDATE,'')<>'' AND (SUBSTRING(A.DELDATE, 7, 4) + '-' + SUBSTRING(A.DELDATE, 4, 2) + '-' + SUBSTRING(A.DELDATE, 1, 2))";
		  extraOrderbyCon=" order by CAST((SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)) AS date) desc";
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
		  
		  StringBuffer sql_PO = new StringBuffer();
		  sql_PO.append("SELECT A.DONO,A.DELDATE,A.CustCode,A.CustName,B.ITEM,B.QTYOR AS QTY,B.UNITPRICE,(B.UNITPRICE * B.QTYOR) as AMOUNT,((B.UNITPRICE * B.QTYOR) - (CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' "
				  + "THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END) ) AS TOTAL_PRICE,(CASE WHEN ISNULL(B.DISCOUNT_TYPE,'%') = '%' "
				  + "THEN ISNULL(((B.UNITPRICE * QTYOR)/100)*B.DISCOUNT, 0) ELSE ISNULL(B.DISCOUNT, 0) END)  AS DISCOUNT_AMOUNT FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO "
				  + "WHERE B.ITEM = '"+Item+"' AND A.ORDERTYPE = '"+ordertype+"' AND A.ORDER_STATUS != 'CANCELLED' AND A.PLANT='"+plant+"'"+sCondition);
		  
		  arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
		  
		  
	  } catch (Exception e) {
		  this.mLogger.exception(this.printLog,
				  "Exception :repportUtil :: getPosSalesorderDetailByProduct:", e);
	  }
	  return arrList;
  }
  
  
  public ArrayList getPosShiftCloseSummary(Hashtable ht, String afrmDate, String atoDate, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);
			
			if (afrmDate.length() > 0) {
				sCondition = sCondition
						+ " AND CAST((SUBSTRING(A.SHIFTDATE, 7, 4) + '-' + SUBSTRING(A.SHIFTDATE, 4, 2) + '-' + SUBSTRING(A.SHIFTDATE, 1, 2)) AS date) >= CAST('"
						+ afrmDate + "' AS date)";
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.SHIFTDATE, 7, 4) + '-' + SUBSTRING(A.SHIFTDATE, 4, 2) + '-' + SUBSTRING(A.SHIFTDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition
							+ " AND CAST((SUBSTRING(A.SHIFTDATE, 7, 4) + '-' + SUBSTRING(A.SHIFTDATE, 4, 2) + '-' + SUBSTRING(A.SHIFTDATE, 1, 2)) AS date) <= CAST('"
							+ atoDate + "' AS date)";
				}
			}
			
			
			sCondition = sCondition + "  ";
			extraOrderbyCon =" ORDER BY CAST((SUBSTRING(A.SHIFTDATE,7,4) + SUBSTRING(A.SHIFTDATE,4,2) + SUBSTRING(A.SHIFTDATE,1,2)) AS date) desc";
			
			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT A.ID,B.ID AS SHIFTID,A.OUTLET,A.TERMINAL,B.EMPLOYEE_ID AS EMPNO,A.SHIFTDATE,C.FNAME,A.SALESCOUNT,ISNULL(A.TOTALSALESCOST,0) AS TOTALSALESCOST,A.CASH,(A.TOTALSALES - A.CASH) AS OTHERSALES,A.VOIDEDSALES,A.TOTALDISCOUNT,A.FLOATAMOUNT,A.RETURNEDAMOUNT,A.EXPENSE,A.EXPECTEDDRAWERAMOUNT,A.DRAWERAMOUNT,(A.TOTALSALES - (A.VOIDEDSALES + A.RETURNEDAMOUNT)) AS TOTALAMOUNT FROM "+plant+"_POSSHIFTCLOSEHDR AS A LEFT JOIN "+plant+"_POSTILLMONEY AS B ON A.TILLMONEYID=B.ID LEFT JOIN "+plant+"_EMP_MST AS C ON C.EMPNO = B.EMPLOYEE_ID WHERE A.SHIFTDATE != '' AND A.PLANT='"+plant+"'"+ sCondition);
			if (ht.size() > 0) {
				

				if (ht.get("OUTLET") != null) {
					String OUTLET = (String) ht.get("OUTLET");
					sql_PO.append(" AND A.OUTLET = '" + OUTLET + "'");
				}
				if (ht.get("TERMINAL") != null) {
					String TERMINAL = (String) ht.get("TERMINAL");
					sql_PO.append(" AND A.TERMINAL = '" + TERMINAL + "'");
				}
				if (ht.get("CASHIER") != null) {
					String CASHIER = (String) ht.get("CASHIER");
					sql_PO.append(" AND B.EMPLOYEE_ID = '" + CASHIER+ "'");
				}

			}

		
			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosShiftCloseSummary:", e);
		}
		return arrList;
	}
  
  public ArrayList getPosShiftCloseDetailByid(int id, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);

			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT \r\n"
					+ "B.EMPLOYEE_ID AS EMPNO,\r\n"
					+ "C.FNAME,\r\n"
					+ "B.CRAT AS FROMDATE,\r\n"
					+ "A.CRAT AS TODATE,\r\n"
					+ "A.TOTALSALES,A.SALESCOUNT,\r\n"
					+ "A.VOIDEDSALES,\r\n"
					+ "A.RETURNEDAMOUNT,"
					+ "ISNULL(A.VOIDSALESCOUNT,0) AS VOIDSALESCOUNT,"
					+ "ISNULL(A.VOIDITEMCOUNT,0) AS VOIDITEMCOUNT,"
					+ "ISNULL(A.EXCHANGE,0) AS EXCHANGE,"
					+ "ISNULL(A.EXCHANGECOUNT,0) AS EXCHANGECOUNT,"
					+ "A.TOTALDISCOUNT,\r\n"
					+ "A.FLOATAMOUNT,\r\n"
					+ "A.EXPENSE,\r\n"
					+ "A.CASH,\r\n"
					+ "A.EXPECTEDDRAWERAMOUNT,\r\n"
					+ "A.DRAWERAMOUNT,\r\n"
					+ "A.OUTLET,\r\n"
					+ "D.OUTLET_NAME,\r\n"
					+ "A.TERMINAL,\r\n"
					+ "E.TERMINAL_NAME,\r\n"
					+ "B.ID AS SHIFTID   \r\n"
					+ "FROM "+plant+"_POSSHIFTCLOSEHDR AS A LEFT JOIN "+plant+"_POSTILLMONEY AS B ON A.TILLMONEYID=B.ID LEFT JOIN "+plant+"_EMP_MST AS C \r\n"
					+ "ON C.EMPNO = B.EMPLOYEE_ID LEFT JOIN "+plant+"_POSOUTLETS AS D ON D.OUTLET = A.OUTLET \r\n"
					+ "LEFT JOIN "+plant+"_POSOUTLETSTERMINALS AS E ON E.OUTLET = A.OUTLET AND E.TERMINAL=A.TERMINAL WHERE A.ID ='"+id+"'");

			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderDetailByProduct:", e);
		}
		return arrList;
	}
  
  public ArrayList getPosShiftClosePaymentModeByHdrid(int id, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);

			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT PAYMENTMODE,SALESCOUNT,AMOUNT FROM "+plant+"_POSSALESBYPAYMODE WHERE HDRID='"+id+"'");

			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: getPosSalesorderDetailByProduct:", e);
		}
		return arrList;
	}
  
  public ArrayList getPaymentModeByDono(String dono, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String extraOrderbyCon="";
		
		try {

			MovHisDAO movHisDAO = new MovHisDAO();
			movHisDAO.setmLogger(mLogger);

			StringBuffer sql_PO = new StringBuffer();
			sql_PO.append("SELECT * FROM "+plant+"_POS_PAYMODE_AMOUNT WHERE DONO='"+dono+"'");

			arrList = movHisDAO.selectForReport(sql_PO.toString(), new Hashtable<String, String>(), extraOrderbyCon);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :repportUtil :: get pos paymode:", e);
		}
		return arrList;
	}
  
  public int updatePaymodebydono(String plant,String paytype,String dono) {
		int cnt=0;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DbBean.getConnection();
	        
	        String sQry = "UPDATE [" + plant + "_POS_PAYMODE_AMOUNT] SET PAYMENTMODE ='"+paytype+"'  WHERE DONO='"+dono+"'";
	        ps = con.prepareStatement(sQry);
	        cnt = ps.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
	        DbBean.closeConnection(con, ps);
	}
		return cnt;
	}
	
  
  public String getdrawerampunt(String plant,int id) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		String amt = "";
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT * FROM "+plant+"_POSSHIFTCLOSEHDR WHERE ID ='"+id+"'");
		try {
			con = com.track.gates.DbBean.getConnection();
			alData = selectData(con, sql.toString());
			
			if(alData.size() > 0) {
				amt = alData.get(0).get("DRAWERAMOUNT");
			}else {
				amt = "0";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return amt;
	}
  
  public String getexpdrawerampunt(String plant,int id) throws Exception {
//		boolean flag = false;
		ArrayList<Map<String, String>> alData = new ArrayList<>();
		String amt = "";
		java.sql.Connection con = null;
		StringBuffer sql = new StringBuffer("SELECT * FROM "+plant+"_POSSHIFTCLOSEHDR WHERE ID ='"+id+"'");
		try {
			con = com.track.gates.DbBean.getConnection();
			alData = selectData(con, sql.toString());
			
			if(alData.size() > 0) {
				amt = alData.get(0).get("EXPECTEDDRAWERAMOUNT");
			}else {
				amt = "0";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return amt;
	}

}
