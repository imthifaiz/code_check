package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.BomDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.SalesDetailDAO;
import com.track.db.util.TblControlUtil;
import com.track.pda.posServlet;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsLocationTransfer implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsLocationTransfer_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLocationTransfer_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
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

	public WmsLocationTransfer() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		boolean flag1 = false;
		
            try{
		flag = processInvMst(m);
	

		if (flag) {
		    flag1 = processMovHis_OUT(m);
		
		}
		if (flag1) {
            
			
		    flag1 = processMovHis_IN(m);
		    
		    

		}
		
		if (flag1) {

			flag1 = processSALESDetail(m);

		}
		if (flag1) {
			if(m.get(IConstants.ISPDA)=="ISPDA")
			flag1 = processPOS(m);

		}
            }catch(Exception e){
            throw e;
            }
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		boolean flag2=false;
try{
		flag = processInvRemove(map);

		if (flag) {
			flag = processInvAdd(map);
			flag2=processBOM(map);
		}
}catch( Exception e){
   throw e; 
}
		return flag;

	}

	private boolean processInvRemove(Map map) throws Exception {
		boolean flag = false;
		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		Hashtable htInvMst = new Hashtable();
		try{
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
		htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
		if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
			htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
		}
		//TODO:To uncomment when FIFO is implemented
		//htInvMst.put(IDBConstants.INVID, map.get(IConstants.INVID));
		String extCond = "QTY > 0";

		flag = _InvMstDAO.isExisit(htInvMst, extCond);

		if (flag) {
//			Get details in ascending order of CRAT for that batch
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				double inqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				extCond = "QTY >= " + inqty;
			}else{
				extCond = "QTY > 0";
			}
			ArrayList alStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
				quantityToAdjust = quantityToAdjust * Double.valueOf((String)map.get("UOMQTY"));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;					
					StringBuffer sql1 = new StringBuffer(" SET ");
					sql1.append(IDBConstants.QTY + " = QTY -'"
							+ adjustedQuantity + "'");
					//extCond = "AND QTY >='" + map.get(IConstants.QTY) + "' ";
					extCond = "";
					Hashtable htInvMstReduce = new Hashtable();
					htInvMstReduce.clear();

					htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htInvMstReduce.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
					htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
					htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
					//TODO:To uncomment when FIFO is implemented
					//htInvMstReduce.put(IDBConstants.INVID, map.get(IConstants.INVID));

					flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, extCond);
					if (!flag) {
						throw new Exception("Could not update");
					}
					quantityToAdjust -= adjustedQuantity;
				}
			}

		} else {
			throw new Exception("Product not found in the location");
		}
	    }catch( Exception e){
	    	this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Not Enough Qty available for Batch :"+(String)map.get(IConstants.BATCH));
	    }
		return flag;
	}

	private boolean processInvAdd(Map map) throws Exception {
		boolean flag = false;
	
		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();
                try{
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC2));
		htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
		//No need to check now if batch is provided. Check only for NOBATCH.
		if ("NOBATCH".equals(map.get(IConstants.BATCH))) {
			flag = _InvMstDAO.isExisit(htInvMst, "");				
		} else {
		htInvMst.put(IDBConstants.CREATED_AT,  map.get(IConstants.ISSUEDATE).toString().replaceAll("/", "") + "000000");
		flag = _InvMstDAO.isExisit(htInvMst, "");
		}
		//get expiredate for inventory update
		      String expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC),(String)map.get(IConstants.BATCH));
		//get expiredate for inventory update end
		
		      double inqty =  Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
		if (flag) {
			
			StringBuffer sql1 = new StringBuffer(" SET ");
			sql1.append(IDBConstants.QTY + " = QTY +'"
					+ String.valueOf(inqty) + "'");
			
		
			sql1.append(" , "+IDBConstants.EXPIREDATE+"='"
					+ expiredate + "'");
								
			flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
			
			

		} else {
			htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
			
			
			htInvMst.put(IDBConstants.EXPIREDATE,  expiredate);                                        
			//htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htInvMst.put(IDBConstants.CREATED_AT,  map.get(IConstants.ISSUEDATE).toString().replaceAll("/", "") + "000000");
				
			flag = _InvMstDAO.insertInvMst(htInvMst);
			
			

		}
	    }catch( Exception e){
	       throw e; 
	    }
		return flag;
	}
	
	
	private boolean processBOM(Map map) throws Exception {
		boolean flag = false;
		boolean itemflag=false;
		boolean bomupdateflag=false;
		String extcond="";
		
		ItemMstDAO _ItemMstDAO =new ItemMstDAO();
		BomDAO _BomDAO =new BomDAO() ;
		try{
	
			Hashtable htBomMst = new Hashtable();
			htBomMst.clear();
			htBomMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			htBomMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
			htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

			flag = _BomDAO.isExisit(htBomMst);
			
			if(flag)
			{
				Hashtable htBomUpdateMst = new Hashtable();
				htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomUpdateMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
				htBomUpdateMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));
		

				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.PARENT_LOC + " ='"
						+ map.get(IConstants.LOC2) + "'");
				sql1.append(" , "+IDBConstants.CHILD_LOC+"='"
						+ map.get(IConstants.LOC2) + "'");
				
				bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst,"");
				if(bomupdateflag)
				{   
					boolean invResult=false;
					boolean queryUpdateInv=false;
					
					
					InvMstDAO invMstDAO = new InvMstDAO();
					
					
					Hashtable htInvParentBom = new Hashtable();
					htInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
				    htInvParentBom.put("ITEM",  map.get(IConstants.ITEM));
					htInvParentBom.put("LOC", map.get(IConstants.LOC2));
					htInvParentBom.put("USERFLD4", map.get(IConstants.BATCH));
				
						if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
							
							Hashtable htInsertInvParentBom = new Hashtable();
							htInsertInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
							htInsertInvParentBom.put("ITEM", map.get(IConstants.ITEM));
							htInsertInvParentBom.put("LOC",  map.get(IConstants.LOC2));
							htInsertInvParentBom.put("USERFLD4", map.get(IConstants.BATCH));
							htInsertInvParentBom.put("QTY",  map.get("INV_QTY1"));
										
							invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
							
							if (!invResult) {
								throw new Exception(
										"Unable to process kitting location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    } /*else {
				throw new Exception("Parent Product should have atleast one child to do stock move");
			}
		}
		else
		{
			return itemflag ;
		}*/
		}catch(Exception e){
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
                }

		return bomupdateflag;

	}
	/* * ************Modification History*********************************
	 Oct 21 2014 Bruhan, Description: To Change TRAN_DATE as Transaction date
	 April 29 2015 Bruhan, Description: To remove resndesc from IDBCOnstants.REMARKS
	*/
	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		EmployeeDAO employeedao =  new EmployeeDAO ();
		movHisDao.setmLogger(mLogger);
		try {

			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.LT_TRAN_OUT);
			htRecvHis.put("ORDNUM",  map.get(IConstants.PONO) == null ? map.get(IConstants.TRANID) : map.get(IConstants.PONO));//	TODO : Check for TRANID
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			if((String)map.get(IConstants.UNITMO)!=null)
			htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UNITMO));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, "-" + map.get(IConstants.QTY));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.USERFLD1, map.get(IConstants.LOC2));
			htRecvHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd((String) map.get(IConstants.TRAN_DATE)));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			String reason = (String)map.get(IConstants.RSNDESC);
			
			if(reason.equalsIgnoreCase("NOREASONCODE"))
        	{
        		reason="";
        	}
        
            String remarks = (String)map.get(IConstants.REMARKS);
           /* if (remarks.length()>0) 
        	{
        		remarks=remarks+",";
        	}*/
            String empno=(String)map.get(IConstants.EMPNO);
            String pda = (String)map.get("PDA");
            String empname=employeedao.getEmpname((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EMPNO),"");
            if(empno.length()>0) {
     		      //htRecvHis.put(IDBConstants.REMARKS, "PDA:"+remarks+reason+","+empno);
            //	htRecvHis.put(IDBConstants.REMARKS, remarks+reason+","+empno);
            	htRecvHis.put(IDBConstants.REMARKS, pda+empname+","+reason+","+remarks);
          	}
           else
     	     {
     		  //htRecvHis.put(IDBConstants.REMARKS, "PDA:"+remarks+reason);
        	 //  htRecvHis.put(IDBConstants.REMARKS, remarks+reason);
        	   htRecvHis.put(IDBConstants.REMARKS, pda+","+reason+remarks);
     	     }
            
			flag = movHisDao.insertIntoMovHis(htRecvHis);
			
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	/* * ************Modification History*********************************
	 Oct 21 2014 Bruhan, Description: To Change TRAN_DATE as Transaction date
	  April 29 2015 Bruhan, Description: To remove resndesc from IDBCOnstants.REMARKS
	*/
	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		EmployeeDAO employeedao =  new EmployeeDAO ();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.LT_TRAN_IN);
			htRecvHis.put("ORDNUM",  map.get(IConstants.PONO) == null ? map.get(IConstants.TRANID) : map.get(IConstants.PONO));//	TODO : Check for TRANID
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			if((String)map.get(IConstants.UNITMO)!=null)
			htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UNITMO));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htRecvHis.put(IDBConstants.USERFLD1, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd((String) map.get(IConstants.TRAN_DATE)));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
            String reason = (String)map.get(IConstants.RSNDESC);
			if(reason.equalsIgnoreCase("NOREASONCODE"))
        	{
        		reason="";
        	}
        
            String remarks = (String)map.get(IConstants.REMARKS);
           /* if (remarks.length()>0) 
        	{
        		remarks=remarks+",";
        	}*/
            String empno=(String)map.get(IConstants.EMPNO);
            String pda = (String)map.get("PDA");
            String empname=employeedao.getEmpname((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EMPNO),"");
            if(empno.length()>0) {
     		      //htRecvHis.put(IDBConstants.REMARKS, "PDA:"+remarks+reason+","+empno);
            //	htRecvHis.put(IDBConstants.REMARKS, remarks+reason+","+empno);
            	htRecvHis.put(IDBConstants.REMARKS, pda+empname+","+reason+","+remarks);
          	}
           else
     	     {
     		  //htRecvHis.put(IDBConstants.REMARKS, "PDA:"+remarks+reason);
        	 //  htRecvHis.put(IDBConstants.REMARKS, remarks+reason);
        	   htRecvHis.put(IDBConstants.REMARKS, pda+","+reason+remarks);
     	     }
          
			flag = movHisDao.insertIntoMovHis(htRecvHis);
			
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	public boolean processSALESDetail(Map map) throws Exception {
		SalesDetailDAO salesdao = new SalesDetailDAO();
		EmployeeDAO employeedao =  new EmployeeDAO ();
		String formatdt = DateUtils.getDate();
		StrUtils strUtils= new StrUtils();
		ItemMstDAO itM = new ItemMstDAO();
		itM.setmLogger(mLogger);
		boolean flag = false;
		Hashtable htm = new Hashtable();
		try {
			//Check Transaction ID already exists or not - Azees 11.8.19
			htm.clear();			
			htm.put(IConstants.PLANT, map.get(IConstants.PLANT));
			htm.put(IConstants.TRANID, map.get(IConstants.PONO) == null ? map.get(IConstants.TRANID) : map.get(IConstants.PONO));			
			flag = salesdao.isExisit(htm,"");
			if(flag==false)
			{  
				String ISPDA=(String)map.get(IConstants.ISPDA);
				   if(ISPDA.length()>0 && ISPDA.equalsIgnoreCase("ISPDA")) {
					   new TblControlUtil().updateTblControlPDASeqNo((String)map.get(IConstants.PLANT),"STOCKMOVE","SM",(String)map.get(IConstants.PONO),true);
			   }
			}
			htm.clear();
	    htm.put("EMPNAME", "");
		htm.put(IConstants.PLANT, map.get(IConstants.PLANT));
		htm.put(IConstants.ITEM, map.get(IConstants.ITEM));
		String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
		htm.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(sDesc));
		htm.put(IConstants.BATCH,  map.get(IConstants.BATCH));
		htm.put(IConstants.QTY, map.get(IConstants.QTY));
		htm.put(IDBConstants.PRICE, "0");
		htm.put(IDBConstants.PAYMENTMODE, "");
        htm.put(IConstants.TRANID, map.get(IConstants.PONO) == null ? map.get(IConstants.TRANID) : map.get(IConstants.PONO));
		htm.put("TranType","MOVEWITHBATCH");
		htm.put(IDBConstants.LOC, map.get(IConstants.LOC));
		htm.put("TOLOC", map.get(IConstants.LOC2));
		htm.put(IDBConstants.POS_DISCOUNT, "0");
		htm.put("PURCHASEDATE", DateUtils.getDateinyyyy_mm_dd(formatdt));
		htm.put("PURCHASETIME", DateUtils.Time());
		String reason = (String)map.get(IConstants.RSNDESC);
	    if(reason.equalsIgnoreCase("NOREASONCODE")) reason="";
	    htm.put(IConstants.RSNCODE, reason);
	    htm.put(IConstants.REMARKS, (String)map.get(IConstants.REMARKS));
		htm.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htm.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
		htm.put(IConstants.UNITMO,  map.get(IConstants.UNITMO));
		String empname=employeedao.getEmpname((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EMPNO),"");
		htm.put("EMPNAME",empname);
		     
		flag = salesdao.insertIntoSalesDet(htm);
		
		
		/*if(flag)
		{    //new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,"GRRECEIPT","GR");  
			String ISPDA=(String)map.get(IConstants.ISPDA);
		   if(ISPDA.length()>0 && ISPDA.equalsIgnoreCase("ISPDA")) {
			   new TblControlUtil().updateTblControlPDASeqNo((String)map.get(IConstants.PLANT),"STOCKMOVE","SM",(String)map.get(IConstants.PONO),true);
		   }
			
			
			 
	  }*/

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	
	public boolean processPOS(Map map) throws Exception {
		POSHdrDAO poshdr =  new POSHdrDAO();
		POSDetDAO posdet =  new POSDetDAO();
		poshdr.setmLogger(mLogger);
	    posdet.setmLogger(mLogger);
		String formatdt = DateUtils.getDate();
		StrUtils strUtils= new StrUtils();
		ItemMstDAO itM = new ItemMstDAO();
		itM.setmLogger(mLogger);
		boolean flag = false;
		boolean istranidExist=false;
		
		posServlet _posServlet=new posServlet();
		Hashtable<String, String> htTrandId = new Hashtable<String, String>();
		htTrandId.put("POSTRANID", (String)(map.get(IConstants.PONO) == null ? map.get(IConstants.TRANID) : map.get(IConstants.PONO)));
		istranidExist=_posServlet.isExisit((String)map.get(IConstants.PLANT), htTrandId);
		try {
			 if(istranidExist) {
				  Hashtable	ht=new Hashtable();
				  ht.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
                  ht.put(IDBConstants.POS_TRANID, map.get(IConstants.PONO) == null ? map.get(IConstants.TRANID) : map.get(IConstants.PONO));
				  ht.put(IDBConstants.POS_ITEM,map.get(IConstants.ITEM));
				  String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
	              ht.put(IDBConstants.POS_ITEMDESC,strUtils.InsertQuotes(sDesc));
	              ht.put(IDBConstants.POS_LOC,map.get(IConstants.LOC));
	              ht.put("TOLOC", map.get(IConstants.LOC2));
	              ht.put(IDBConstants.POS_BATCH, map.get(IConstants.BATCH));
	              ht.put(IDBConstants.POS_QTY,map.get(IConstants.QTY));
	              ht.put(IDBConstants.POSDET_STATUS,"C");
	              ht.put(IDBConstants.POS_UNITPRICE,"0");
	              ht.put(IDBConstants.POS_TOTALPRICE,"0");
	              ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
	              ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
	              ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	              ht.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
	              ht.put("POSTYPE","MOVEWITHBATCH");
	              ht.put(IConstants.EXPIREDATE,"");
	              ht.put(IConstants.UNITMO,map.get(IConstants.UNITMO));
	              flag =  posdet.insertIntoPosDet(ht);
			 } else {
				  Hashtable	hthdr=new Hashtable();
				  hthdr.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
				  hthdr.put(IDBConstants.POS_TRANID, map.get(IConstants.PONO));
				  hthdr.put("RECEIPTNO", map.get(IConstants.PONO));
				  hthdr.put(IDBConstants.STATUS, "C");
				  hthdr.put(IDBConstants.LOC,map.get(IConstants.LOC));
				  hthdr.put("TOLOC", map.get(IConstants.LOC2));
				  String reason = (String)map.get(IConstants.RSNDESC);
	              if(reason.equalsIgnoreCase("NOREASONCODE")) reason="";
				  hthdr.put(IDBConstants.RSNCODE, reason);
	              hthdr.put(IDBConstants.REMARKS,(String)map.get(IConstants.REMARKS));
	              hthdr.put(IConstants.EMPNO,map.get(IConstants.EMPNO));
				  hthdr.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
				  hthdr.put(IDBConstants.POS_TRANTM, DateUtils.Time());
				  hthdr.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	              hthdr.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
	              flag =  poshdr.insertIntoPosHdr(hthdr);
	                
				 if(flag) {
					 Hashtable	ht=new Hashtable();
					  ht.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
	                  ht.put(IDBConstants.POS_TRANID, map.get(IConstants.PONO));
					  ht.put(IDBConstants.POS_ITEM,map.get(IConstants.ITEM));
					  String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
		              ht.put(IDBConstants.POS_ITEMDESC,strUtils.InsertQuotes(sDesc));
		              ht.put(IDBConstants.POS_LOC,map.get(IConstants.LOC));
		              ht.put("TOLOC", map.get(IConstants.LOC2));
		              ht.put(IDBConstants.POS_BATCH, map.get(IConstants.BATCH));
		              ht.put(IDBConstants.POS_QTY,map.get(IConstants.QTY));
		              ht.put(IDBConstants.POSDET_STATUS,"C");
		              ht.put(IDBConstants.POS_UNITPRICE,"0");
		              ht.put(IDBConstants.POS_TOTALPRICE,"0");
		              ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
		              ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
		              ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		              ht.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
		              ht.put("POSTYPE","MOVEWITHBATCH");
		              ht.put(IConstants.EXPIREDATE,"");
		              ht.put(IConstants.UNITMO,map.get(IConstants.UNITMO));
	              flag =  posdet.insertIntoPosDet(ht);
				 }
			 }

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
}