package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.BomDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.SalesDetailDAO;
import com.track.dao.ShipHisDAO;
import com.track.db.util.TblControlUtil;
import com.track.pda.posServlet;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsMiscIssueMaterial implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsMiscIssueMaterial_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
	private MLogger mLogger = new MLogger();
	StrUtils su = new StrUtils();
	String lineqty = "", batch = "", balqty = "";
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

	public WmsMiscIssueMaterial() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
        boolean bomflag=false;
        
			flag = processIssueHis(m);
			if(flag)
			{
		        String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
		        if(!nonstocktype.equalsIgnoreCase("Y"))	
			    {
		        
					flag = processInvMst(m);
					flag=processBOM(m);
			    }else{
					double actqty = Double.valueOf((String) m.get(IConstants.QTY));
			    	flag = processMovHis_OUT(m, actqty);
			    	flag=true;
			    }
			}
		   
		/*if (flag) {
			
			 flag = processMovHis_OUT(m);
	
		}*/
		
		
		
		
		if (flag) {

			flag = processSALESDetail(m);

		}
		if (flag) {

			flag = processPOS(m);

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
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			//htInvMst.put(IDBConstants.INVID, map.get(IConstants.INVID));
			//StringBuffer sql = new StringBuffer(" SET ");
			//sql.append("QTY" + " = QTY - " + map.get(IConstants.QTY) + " ");
			//extCond = "AND QTY >='" + map.get(IConstants.QTY) + "' ";			
			//flag = _InvMstDAO.update(sql.toString(), htInvMst,extCond);
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
			double invumqty = Double.valueOf(actqty) * Double.valueOf((String)map.get("UOMQTY"));

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
						sql.append(" SET QTY" + " = QTY - " + invumqty + " ");

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
						sql.append(" SET QTY" + " = QTY - " + invumqty + " ");

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
			}
                        
                        if(!flag){
                        
                        
                            throw new Exception( "Error in picking Product : Invalid  Batch Details ");
                        }

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
	
	
	private boolean processBOM(Map map) throws Exception {
		boolean flag = false;
		boolean itemflag=false;
		
		boolean resultBomDelete=false;
		boolean movhisResult=false;
		String extcond="";
		
				
		ItemMstDAO _ItemMstDAO =new ItemMstDAO();
		BomDAO _BomDAO =new BomDAO() ;
		MovHisDAO movHisDao = new MovHisDAO();
		
		try {
		
			Hashtable htBomMst = new Hashtable();
			htBomMst.clear();
			htBomMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			htBomMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
			htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

			flag = _BomDAO.isExisit(htBomMst);
				
			
			if (flag) {
				
				  	
				
				 Hashtable htUpdateBOM = new Hashtable();
    			 htUpdateBOM .put(IDBConstants.PLANT, map.get(IConstants.PLANT));
    			 htUpdateBOM .put("PARENT_PRODUCT", map.get(IConstants.ITEM));
    			 htUpdateBOM.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC));
    			 htUpdateBOM.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));
					
    		
    			 StringBuffer sql1 = new StringBuffer(" SET ");
 				 sql1.append(" " +"STATUS" + " = 'C' ");
				 sql1.append("," + IDBConstants.UPDATED_AT1 + " = '"
 						+ dateUtils.getDateTime() + "'");
 				 flag=_BomDAO.update(sql1.toString(), htUpdateBOM, " ");
			}
		 //}
		   else{
			 flag=true;
		   }
			
		} //end try
        catch (Exception e) {

			this.mLogger.exception(this.printLog, "Kitting Misc Issue", e);
			throw e;

		}
		
        return true ;
	}

	/* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
	   April 27 2015 Bruhan, Description: To change condition reason to remarks
	*/
	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		EmployeeDAO employeedao =  new EmployeeDAO ();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.GOODSISSUE);
			htRecvHis.put("ORDNUM",  map.get(IConstants.DONO));
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));

			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put("QTY", map.get(IConstants.QTY));
			if((String)map.get(IConstants.UNITMO)!=null)
				htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UNITMO));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			String reason = (String)map.get(IConstants.RSNDESC);
            if(reason.equalsIgnoreCase("NOREASONCODE"))
            	{
            		reason="";
            	}
            
            String remarks = (String)map.get(IConstants.REMARKS);
            /*if (remarks.length()>0) 
            	{
            		remarks=remarks+",";
            	}*/
            String empno=(String)map.get(IConstants.EMPNO);
            String empname=employeedao.getEmpname((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EMPNO),"");
            if (remarks.length()>0) 
            {
            	if(empno.length()>0) {
            		 htRecvHis.put(IDBConstants.REMARKS, "PDA:"+empname+","+reason+","+remarks);
            	}
            	else
            	{
            		htRecvHis.put(IDBConstants.REMARKS, "PDA:"+reason+","+remarks);
            	}
            	  
            }
       		
		    htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			flag = movHisDao.insertIntoMovHis(htRecvHis);
		
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
   /* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To include Issue Date
	   April 27 2015 Bruhan, Description: To change condition reason to remarks
	*/
	public boolean processIssueHis(Map map) throws Exception {
		boolean flag = false;
		 String rems="";
		
		ShipHisDAO shiphstdao = new ShipHisDAO();
		shiphstdao.setmLogger(mLogger);
		try {
			Hashtable htRecvDet = new Hashtable();
			
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		//	htRecvDet.put(IDBConstants.DODET_DONUM, map.get(IConstants.DONO));
			htRecvDet.put(IDBConstants.DODET_DONUM, "");
			htRecvDet.put("DOLNO", map.get(IConstants.DODET_DOLNNO));
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, "");
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			// Retrieve item description
			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvDet.put("ORDQTY", map.get(IConstants.QTY));
			htRecvDet.put("REVERSEQTY","0");
			htRecvDet.put("STATUS","C");
			htRecvDet.put("INVOICENO",map.get(IConstants.DONO));
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvDet.put(IDBConstants.ISSUEDATE,  map.get(IConstants.ISSUEDATE));
			htRecvDet.put(IDBConstants.TRAN_TYPE, "GOODSISSUE");			
            String reason = (String)map.get(IConstants.RSNDESC);
            if(reason.equalsIgnoreCase("NOREASONCODE"))
            	{
            		reason="";
            	}
         
            
            String remarks = (String)map.get(IConstants.REMARKS);
            if (remarks.length()>0) ;
            	{
            		remarks=remarks+",";
            	}
       		htRecvDet.put("REMARK", remarks+reason);
       		String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));
       		if(nonstocktype.equals("Y"))	
		    {	
	        	htRecvDet.put("PICKQTY", map.get(IConstants.QTY));
				flag = shiphstdao.insertShipHis(htRecvDet);
		    }
	        else
	        {			
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			String extCond = "";
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				double invumqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				extCond = "QTY >= " + String.valueOf(invumqty);
			}else{
				extCond = "QTY > 0";
			}
			InvMstDAO _InvMstDAO = new InvMstDAO();
			ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, extCond);
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
					adjustedQuantity =  StrUtils.RoundDB(adjustedQuantity, IConstants.DECIMALPTS);					
					String IQty = String.valueOf(adjustedQuantity);
					 IQty = StrUtils.formatThreeDecimal(IQty);
					// htRecvDet.clear();
					 
					 Hashtable hrRecCheck = new Hashtable();
					 hrRecCheck.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					 hrRecCheck.put(IDBConstants.DODET_DONUM, map.get(IConstants.DONO));
						flag = shiphstdao.isExisit(hrRecCheck);
						if(flag==false)
						{    
							 /*new TblControlUtil().updateTblControlPDASeqNo((String)map.get(IConstants.PLANT),"GIRECEIPT","GI",(String)map.get(IConstants.DONO),true);*/
							// new TblControlUtil().updateTblControlIESeqNo((String)map.get(IConstants.PLANT), "GINO", "GI", (String)map.get(IConstants.DONO));
					    }
			
			htRecvDet.put("PICKQTY", IQty);			
       		htRecvDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
			flag = shiphstdao.insertShipHis(htRecvDet);
			
			/*if(flag)
			{    //new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,"GRRECEIPT","GR");  
				 new TblControlUtil().updateTblControlPDASeqNo((String)map.get(IConstants.PLANT),"GIRECEIPT","GI",(String)map.get(IConstants.DONO),true);
				 
		    }*/
			quantityToAdjust -= adjustedQuantity;
				}
			}
	        }

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
	    htm.put("EMPNAME", "");
		htm.put(IConstants.PLANT, map.get(IConstants.PLANT));
		htm.put(IConstants.ITEM, map.get(IConstants.ITEM));
		String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
		htm.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(sDesc));
		htm.put(IConstants.BATCH,  map.get(IConstants.BATCH));
		htm.put(IConstants.QTY, map.get(IConstants.QTY));
		htm.put(IDBConstants.PRICE, "0");
		htm.put(IDBConstants.PAYMENTMODE, "");
        htm.put("TranId", map.get(IConstants.DONO));
		htm.put("TranType","GOODSISSUEWITHBATCH");
		htm.put(IDBConstants.LOC, map.get(IConstants.LOC));
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
		htTrandId.put("POSTRANID", (String)map.get(IConstants.DONO));
		istranidExist=_posServlet.isExisit((String)map.get(IConstants.PLANT), htTrandId);
		try {
			 if(istranidExist) {
				  Hashtable	ht=new Hashtable();
				  ht.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
                  ht.put(IDBConstants.POS_TRANID,map.get(IConstants.DONO));
                  ht.put("DOLNNO", map.get(IConstants.DODET_DOLNNO));
                  ht.put(IDBConstants.POS_ITEM,map.get(IConstants.ITEM));
				  String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
	              ht.put(IDBConstants.POS_ITEMDESC,strUtils.InsertQuotes(sDesc));
	              ht.put(IDBConstants.POS_LOC,map.get(IConstants.LOC));
	              ht.put(IDBConstants.POS_BATCH, map.get(IConstants.BATCH));
	              ht.put(IDBConstants.POS_QTY,map.get(IConstants.QTY));
	              ht.put(IDBConstants.POSDET_STATUS,"C");
	              ht.put(IDBConstants.POS_UNITPRICE,"0");
	              ht.put(IDBConstants.POS_TOTALPRICE,"0");
	              ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
	              ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
	              ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	              ht.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
	              ht.put("POSTYPE","GOODSISSUEWITHBATCH");
	              ht.put(IConstants.EXPIREDATE,"");
	              ht.put(IConstants.UNITMO,map.get(IConstants.UNITMO));
	              flag =  posdet.insertIntoPosDet(ht);
			 } else {
				  Hashtable	hthdr=new Hashtable();
				  hthdr.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
				  hthdr.put(IDBConstants.POS_TRANID,map.get(IConstants.DONO));
				  hthdr.put("RECEIPTNO", map.get(IConstants.DONO));
				 
				  hthdr.put(IDBConstants.STATUS, "C");
				  hthdr.put(IDBConstants.LOC,map.get(IConstants.LOC));
				  String reason = (String)map.get(IConstants.RSNDESC);
	              if(reason.equalsIgnoreCase("NOREASONCODE")) reason="";
				  hthdr.put(IDBConstants.RSNCODE, reason);
	              hthdr.put(IDBConstants.REMARKS,(String)map.get(IConstants.REMARKS));
				  hthdr.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
				  hthdr.put(IDBConstants.POS_TRANTM, DateUtils.Time());
				  hthdr.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	              hthdr.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
	              hthdr.put(IDBConstants.EMPNO,map.get(IConstants.EMPNO));
	              flag =  poshdr.insertIntoPosHdr(hthdr);
	                
				 if(flag) {
				  Hashtable	ht=new Hashtable();
				  ht.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
                  ht.put(IDBConstants.POS_TRANID,map.get(IConstants.DONO));
                  ht.put("DOLNNO", map.get(IConstants.DODET_DOLNNO));
                  ht.put(IDBConstants.POS_ITEM,map.get(IConstants.ITEM));
				  String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
	              ht.put(IDBConstants.POS_ITEMDESC,strUtils.InsertQuotes(sDesc));
	              ht.put(IDBConstants.POS_LOC,map.get(IConstants.LOC));
	              ht.put(IDBConstants.POS_BATCH, map.get(IConstants.BATCH));
	              ht.put(IDBConstants.POS_QTY,map.get(IConstants.QTY));
	              ht.put(IDBConstants.POSDET_STATUS,"C");
	              ht.put(IDBConstants.POS_UNITPRICE,"0");
	              ht.put(IDBConstants.POS_TOTALPRICE,"0");
	              ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
	              ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
	              ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	              ht.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
	              ht.put("POSTYPE","GOODSISSUEWITHBATCH");
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

	public boolean processMovHis_OUT(Map map, double qty) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		EmployeeDAO employeedao =  new EmployeeDAO ();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.GOODSISSUE);
			htRecvHis.put("ORDNUM",  map.get(IConstants.DONO));
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put("BATNO", batch);
			htRecvHis.put("QTY", String.valueOf(qty));
			if((String)map.get(IConstants.UNITMO)!=null)
				htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UNITMO));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
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
				            String empname=employeedao.getEmpname((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EMPNO),"");
				            if (remarks.length()>0) 
				            {
				            	if(empno.length()>0) {
				            		 htRecvHis.put(IDBConstants.REMARKS, "PDA:"+empname+","+reason+","+remarks);
				            	}
				            	else
				            	{
				            		htRecvHis.put(IDBConstants.REMARKS, "PDA:"+reason+","+remarks);
				            	}
				            	  
				            }
				       		
						    htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

			flag = movHisDao.insertIntoMovHis(htRecvHis);
			
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
}