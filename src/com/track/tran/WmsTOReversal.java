package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.*;
import com.track.db.util.TempUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsTOReversal implements WmsTran, IMLogger{
	private boolean printLog = MLoggerConstant.WmsTOReversal_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsTOReversal_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
	private MLogger mLogger = new MLogger();
	TempUtil _TempUtil = new  TempUtil();
	ArrayList ALIssueddetails = new ArrayList();
	ArrayList ALRecvddetails = new ArrayList();
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
	
	public WmsTOReversal() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			
			flag = processTodetForReversal(m);
			
			
			if (flag) {

				flag = processInvAdd(m);

			}
			if (flag) {

				flag = processInvRemove(m);

			}

			if (flag) {

				{

					flag = processMovHis_OUT(m);

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						this.mLogger.exception(this.printLog, "", e);
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
	
	public boolean processTodetForReversal(Map map) throws Exception {

		boolean flag = false;
		try {
			// update receive qty in podet
			ToDetDAO _ToDetDAO = new ToDetDAO();
			ToHdrDAO _ToHdrDAO = new ToHdrDAO();
			RecvDetDAO _RevDetDAO = new RecvDetDAO();
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			 ALIssueddetails = new ArrayList();

			Hashtable htCondiToPick = new Hashtable();
			Hashtable htCondiToRecv = new Hashtable();
			StringBuffer query = new StringBuffer("");
					
			
			htCondiToPick.put("PLANT", map.get("PLANT"));
			htCondiToPick.put("tono", map.get("TONO"));
			htCondiToPick.put("tolno", map.get("TOLNNO"));
			htCondiToPick.put("item", map.get("ITEM"));
			htCondiToPick.put("batch", map.get("BATCH"));
			
			htCondiToRecv.put("PLANT", map.get("PLANT"));
			htCondiToRecv.put("pono", map.get("TONO"));
			htCondiToRecv.put("lnno", map.get("TOLNNO"));
			htCondiToRecv.put("item", map.get("ITEM"));
			htCondiToRecv.put("batch", map.get("BATCH"));
			
			double reverseqty = Double.parseDouble((String) map.get("QTY"));
			String qty = "";
			ALIssueddetails = _ToDetDAO.getpickedDetailsforreverse(map.get("PLANT").toString(), htCondiToPick);
			
			if (ALIssueddetails.size() > 0) {
				for (int i = 0; i < ALIssueddetails.size(); i++) {
					Map m = (Map) ALIssueddetails.get(i);
					
					if(reverseqty == 0) break;
					
					  String tono = (String)m.get("tono");
					  double issueqty = Double.parseDouble((String)m.get("PICKQTY"));
			          String crat = (String)m.get("CRAT");
			          String ID = (String)m.get("ID").toString();
			          /*if(reverseqty >= issueqty)
			          {
			        	  reverseqty = (reverseqty-issueqty);
			        	  qty = "0";
			        	  
			          }
			          else
			          {
			        	  qty = Double.toString(issueqty - reverseqty);
			        	  reverseqty = 0;
			        	  
			          }*/
			          
			          String updateissuedet = "";
			          //String updaterecvdet = "";
			          if(reverseqty >= issueqty)
			          {
			          updateissuedet = "set pickqty = pickqty-" +issueqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			          //updaterecvdet = "set recqty = recqty-" +issueqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			          }
			          else
			          {
			        	  updateissuedet = "set pickqty = pickqty-" +reverseqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
				         // updaterecvdet = "set recqty = recqty-" +reverseqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
				          issueqty=reverseqty;
			          }
			          
			          //updateissuedet = "set pickqty = " +qty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			          //updaterecvdet = "set recqty = " +qty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			          
			          htCondiToPick.put("CRAT", crat);
			          htCondiToPick.put("ID", ID);
			          if(reverseqty>0)
			          {		
			        	  boolean isexists = _ToDetDAO.isExisitTOPick(htCondiToPick);
			 			 if(isexists)
			 			 {
				          flag = _ToDetDAO.updateToPickTable(updateissuedet, htCondiToPick, "");
				          //flag = _RevDetDAO.update(updaterecvdet, htCondiToRecv, "",(String) map.get("PLANT"));
				          reverseqty=reverseqty-issueqty;
			 			 }
			          }					
				}
				htCondiToPick.remove("CRAT");
				htCondiToPick.remove("ID");
			}		
			
			
			double reverseqty1 = Double.parseDouble((String) map.get("QTY"));
			 ALRecvddetails = _RevDetDAO.getRcvdDetailsforreverse(map.get("PLANT").toString(), htCondiToRecv);
			 if (ALRecvddetails.size() > 0) {
					for (int i = 0; i < ALRecvddetails.size(); i++) {
						Map m = (Map) ALRecvddetails.get(i);
						
						if(reverseqty1 == 0) break;
						
						  String tono = (String)m.get("pono");
						  double issueqty = Double.parseDouble((String)m.get("RECQTY"));
				          String crat = (String)m.get("CRAT");
				          String ID = (String)m.get("ID").toString();
				          
				          String updaterecvdet = "";
				          if(reverseqty1 >= issueqty)
				          {				          
				          updaterecvdet = "set recqty = recqty-" +issueqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
				          }
				          else
				          {				        	  
					          updaterecvdet = "set recqty = recqty-" +reverseqty1 +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
					          issueqty=reverseqty1;
				          }
				          htCondiToRecv.put("CRAT", crat);
				          htCondiToRecv.put("ID", ID);
				          if(reverseqty1>0)
				          {		
				        	  boolean isexists = _ToDetDAO.isExisitTOPick(htCondiToPick);
				 			 if(isexists)
				 			 {
					          
					          flag = _RevDetDAO.update(updaterecvdet, htCondiToRecv, "",(String) map.get("PLANT"));
					          reverseqty1=reverseqty1-issueqty;
				 			 }
				          }					
					}
					htCondiToPick.remove("CRAT");
					htCondiToPick.remove("ID");
				}
			 
			 
			 	htCondiToPick.put("PICKQTY", "0.000");			
				boolean isexists = _ToDetDAO.isExisitTOPick(htCondiToPick);
				 if(isexists)
				 {
					flag = _ToDetDAO.deleteTOPick(htCondiToPick);
				 }
			 
				 htCondiToRecv.put("RECQTY", "0.000");
				 boolean isexistsrecv = _RevDetDAO.isExisit(htCondiToRecv,(String) map.get("PLANT"));
				 if(isexistsrecv)
				 {
					flag = _RevDetDAO.deleteRECVDET(htCondiToRecv);
				 }
			if (!flag) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  TODET :"
								+ " " + map.get("ITEM"));
			} else {
				flag = true;
				
				

			}
			
			
			if (flag) {
				
			String updateToDet = "";

			updateToDet = "set qtypick= isNull(qtypick,0) - " + map.get(IConstants.QTY)	+ ", qtyrc= isNull(qtyrc,0) - " + map.get(IConstants.QTY)	+", pickstatus='O',lnstat='O'";
			
			Hashtable htToDet = new Hashtable();
			htToDet.put("PLANT", map.get("PLANT"));
			htToDet.put("tono", map.get("TONO"));
			htToDet.put("tolnno", map.get("TOLNNO"));
			htToDet.put("item", map.get("ITEM"));
		
			flag = _ToDetDAO.update(updateToDet, htToDet, "");
			
			if (!flag) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  TODET :"
								+ " " + map.get("ITEM"));
			} else {
				flag = true;
				 boolean isExists = _ToDetDAO.isExisit(htToDet,"  qtypick =CAST('0' AS DECIMAL(18,3)) ");
                 if(isExists){
                	 updateToDet = "";

                	 updateToDet = "set pickstatus='N',LNSTAT='N' ";
                     flag = _ToDetDAO.update(updateToDet, htToDet, "");   
                 }

			}
						
			
			}	
			
			
			String updateToHdr = "";
			Hashtable htConditoHdr = new Hashtable();
			htConditoHdr.put("PLANT", map.get("PLANT"));
			htConditoHdr.put("tono", map.get("TONO"));
			
			flag = _ToDetDAO.isExisit(htConditoHdr," isnull(pickstatus,'') in ('O','C')");

			if (flag)
				updateToHdr = "set  STATUS='O',PickStaus='O' ";
			else
				updateToHdr = "set STATUS='N',PickStaus='N' ";

			flag = _ToHdrDAO.update(updateToHdr, htConditoHdr, "");

			if (!flag) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  TONO :"
								+ " " + map.get("TONO"));
			} else {
				flag = true;
			}
			
		} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw new Exception("Product Reversed  Failed, Error In update  TODET");
	}

	return flag;
}
	
private boolean processInvAdd(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			Hashtable htInvMst = new Hashtable();

			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			double reverseqty = Double.parseDouble((String) map.get("QTY"));
			
			if (ALIssueddetails.size() > 0) {
				for (int i = 0; i < ALIssueddetails.size(); i++) {
					Map m = (Map) ALIssueddetails.get(i);
					{
						if(reverseqty==0) break;
						String ID = (String)m.get("ID").toString();
						String PQTY = (String)m.get("PICKQTY").toString();
						double issueqty = Double.parseDouble(PQTY);
						htInvMst.put(IDBConstants.INVID, ID);
                       
			StringBuffer sql1 = new StringBuffer(" SET ");
			//sql1.append(IDBConstants.QTY + " = QTY +'" + map.get(IConstants.QTY) + "'");
			if(reverseqty >= issueqty)
			{
			double invumqty = issueqty * Double.valueOf((String)map.get("UOMQTY"));
			sql1.append(IDBConstants.QTY + " = QTY +'" + invumqty + "'");
			}
			else
			{
				double invumqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
				sql1.append(IDBConstants.QTY + " = QTY +'" + invumqty + "'");
				issueqty=reverseqty;
			}
			sql1.append("," + IDBConstants.UPDATED_AT + " = '"
					+ dateUtils.getDateTime() + "'");

			if(reverseqty>0)
			{
			flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
			reverseqty = (reverseqty-issueqty);
			}
					}
				}
				 ALIssueddetails = new ArrayList();
			}
			
			if(flag)
			{
			   flag=true;
			   boolean bomexistsflag = false;
			   boolean bomupdateflag=false;
			   BomDAO _BomDAO =new BomDAO() ;
			   Hashtable htBomMst = new Hashtable();
				htBomMst.clear();
				htBomMst.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
				htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomMst.put("PARENT_PRODUCT_LOC",  "TEMP_TO" + "_" + map.get(IConstants.LOC));
				htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));
				bomexistsflag = _BomDAO.isExisit(htBomMst);
				if(bomexistsflag)
				{
					Hashtable htBomUpdateMst = new Hashtable();
					htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
					htBomUpdateMst.put("PARENT_PRODUCT_LOC",  "TEMP_TO" + "_" + map.get(IConstants.LOC));
					htBomUpdateMst.put("PARENT_PRODUCT_BATCH",  map.get(IConstants.BATCH));
					StringBuffer bomsql = new StringBuffer(" SET ");
					bomsql.append(IDBConstants.PARENT_LOC + " ='"
							+ map.get(IConstants.LOC) + "'");
					bomsql.append(" , "+IDBConstants.CHILD_LOC+"='"
							+ map.get(IConstants.LOC) + "'");
					bomsql.append("," +"STATUS" + " = 'A' ");
										bomupdateflag= _BomDAO.update(bomsql.toString(),htBomUpdateMst,"");
				}

			} else {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update InvMst InvMst :"
								+ " " + map.get(IConstants.ITEM));
			}
			
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

private boolean processInvRemove(Map map) throws Exception {
	boolean flag = false;
	try {
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Hashtable htInvMst = new Hashtable();

		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC2));
		htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
		
		double reverseqty = Double.parseDouble((String) map.get("QTY"));
		if (ALRecvddetails.size() > 0) {
		}
		else {
		//Reverse not based on revdet id
			Hashtable htCondiToRecv = new Hashtable();
			htCondiToRecv.put("PLANT", map.get("PLANT"));
			htCondiToRecv.put("pono", map.get("TONO"));
			htCondiToRecv.put("lnno", map.get("TOLNNO"));
			htCondiToRecv.put("item", map.get("ITEM"));
			htCondiToRecv.put("batch", map.get("BATCH"));
			ALRecvddetails = new RecvDetDAO().getRcvdDetinvforreverse(map.get("PLANT").toString(), map.get(IConstants.LOC2).toString(), map.get("ITEM").toString(),map.get("BATCH").toString(),htCondiToRecv);
			
		}
		if (ALRecvddetails.size() > 0) {
			for (int i = 0; i < ALRecvddetails.size(); i++) {
				Map m = (Map) ALRecvddetails.get(i);
				{
					if(reverseqty==0) break;
					String ID = (String)m.get("ID").toString();
					String RQTY = (String)m.get("RECQTY").toString();
					double issueqty = Double.parseDouble(RQTY);
					htInvMst.put(IDBConstants.INVID, ID);
					String cond ="";
					if(reverseqty >= issueqty)
					{
						double invumqty = issueqty * Double.valueOf((String)map.get("UOMQTY"));
						 cond =" QTY >="+ invumqty;
					}
					else
					{
						double invumqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
						 cond =" QTY >="+ invumqty;
					}
			flag = _InvMstDAO.isExisit(htInvMst,cond );
			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				if(reverseqty >= issueqty)
				{
					double invumqty = issueqty * Double.valueOf((String)map.get("UOMQTY"));
					sql1.append(IDBConstants.QTY + " = QTY -'" + invumqty + "'");
				}
				else
				{
					double invumqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
					sql1.append(IDBConstants.QTY + " = QTY -'" + invumqty + "'");
					issueqty=reverseqty;
				}

				if(reverseqty>0)
				{
					flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
					reverseqty = (reverseqty-issueqty);
				}
				    }
			else {
				throw new Exception("Product Reversed  Failed, Not enough Inventory quantity for the product :"
						+ " " + map.get(IConstants.ITEM));
			}
			}
		}
			ALRecvddetails = new ArrayList();

		}
		 else {
				throw new Exception("Product Reversed Failed, Not enough Inventory quantity for the product :"
						+ " " + map.get(IConstants.ITEM));
			}
	} catch (Exception e) {

		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
}

/* * ************Modification History*********************************
 Oct 21 2014 Bruhan, Description: To Change TRAN_DATE as Transaction date
*/
public boolean processMovHis_IN(Map map) throws Exception {
	boolean flag = false;
	MovHisDAO movHisDao = new MovHisDAO();
	CustMstDAO _CustMstDAO = new CustMstDAO();
	movHisDao.setmLogger(mLogger);
	_CustMstDAO.setmLogger(mLogger);
	try {

		Hashtable<String, String> htMovhis = new Hashtable<String, String>();
		htMovhis.clear();
		htMovhis.put(IDBConstants.PLANT,(String) map.get(IConstants.PLANT));
		htMovhis.put("DIRTYPE", TransactionConstants.TO_REVERSE_IN);
		htMovhis.put(IDBConstants.ITEM, (String)map.get(IConstants.ITEM));
		htMovhis.put("BATNO",(String) map.get(IConstants.BATCH));
		htMovhis.put(IDBConstants.QTY,(String) map.get(IConstants.QTY));
		if((String)map.get(IConstants.CUSTOMER_CODE)!=null)
			htMovhis.put(IDBConstants.CUSTOMER_CODE, (String) map.get(IConstants.CUSTOMER_CODE));
		if((String)map.get(IConstants.UOM)!=null)
			htMovhis.put(IDBConstants.UOM, (String) map.get(IConstants.UOM));
		htMovhis.put("MOVTID", "IN");
		htMovhis.put("RECID", "");
		htMovhis.put(IDBConstants.LOC,(String) map.get(IConstants.LOC));
		htMovhis.put(IDBConstants.MOVHIS_ORDNUM,(String) map.get(IConstants.TODET_TONUM));
		htMovhis.put(IDBConstants.CREATED_BY,(String) map.get(IConstants.LOGIN_USER));
		htMovhis.put(IDBConstants.TRAN_DATE, (String) map.get(IConstants.TRAN_DATE));
		htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htMovhis.put(IDBConstants.REMARKS , "");

		flag = movHisDao.insertIntoMovHis(htMovhis);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
}

/* * ************Modification History*********************************
Oct 21 2014 Bruhan, Description: To Change TRAN_DATE as Transaction date
*/
public boolean processMovHis_OUT(Map map) throws Exception {
	boolean flag = false;
	MovHisDAO movHisDao = new MovHisDAO();
	CustMstDAO _CustMstDAO = new CustMstDAO();
	movHisDao.setmLogger(mLogger);
	_CustMstDAO.setmLogger(mLogger);
	try {

		Hashtable<String, String> htMovhis = new Hashtable<String, String>();
		htMovhis.clear();
		htMovhis.put(IDBConstants.PLANT,(String) map.get(IConstants.PLANT));
		htMovhis.put("DIRTYPE", TransactionConstants.TO_REVERSE_OUT);
		htMovhis.put(IDBConstants.ITEM, (String)map.get(IConstants.ITEM));
		htMovhis.put("BATNO",(String) map.get(IConstants.BATCH));
		htMovhis.put(IDBConstants.QTY,"-" +(String) map.get(IConstants.QTY));
		if((String)map.get(IConstants.CUSTOMER_CODE)!=null)
			htMovhis.put(IDBConstants.CUSTOMER_CODE, (String) map.get(IConstants.CUSTOMER_CODE));
		if((String)map.get(IConstants.UOM)!=null)
			htMovhis.put(IDBConstants.UOM, (String) map.get(IConstants.UOM));
		htMovhis.put("MOVTID", "OUT");
		htMovhis.put("RECID", "");
		htMovhis.put(IDBConstants.LOC,(String) map.get(IConstants.LOC2));
		htMovhis.put(IDBConstants.MOVHIS_ORDNUM,(String) map.get(IConstants.TODET_TONUM));
		htMovhis.put(IDBConstants.CREATED_BY,(String) map.get(IConstants.LOGIN_USER));
		htMovhis.put(IDBConstants.TRAN_DATE,(String) map.get(IConstants.TRAN_DATE));
		htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htMovhis.put(IDBConstants.REMARKS , "");

		flag = movHisDao.insertIntoMovHis(htMovhis);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
}
	

}
