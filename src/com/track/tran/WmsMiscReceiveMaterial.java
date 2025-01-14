package com.track.tran;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.EmployeeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.SalesDetailDAO;
import com.track.db.util.TblControlUtil;
import com.track.pda.posServlet;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsMiscReceiveMaterial implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsMiscReceiveMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsMiscReceiveMaterial_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = new DateUtils();
        StrUtils su = new StrUtils();
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

	public WmsMiscReceiveMaterial() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = true;
		String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
		if(!nonstocktype.equalsIgnoreCase("Y") )	
	    {
			flag = processInvMst(m);
	    }else{
	    	flag=true;
	    }   
		
		if (flag) {

			flag = processMovHis_IN(m);

		}
		if (flag) {

			flag = processRecvDet(m);

		}
		
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
		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));

			//No need to check now if batch is provided. Check only for NOBATCH.
			if ("NOBATCH".equals(map.get(IConstants.INV_BATCH))) {
				flag = _InvMstDAO.isExisit(htInvMst, "");				
			}
			double invumqty = Double.valueOf((String)map.get(IConstants.INV_QTY)) * Double.valueOf((String)map.get("UOMQTY"));
			if (flag) {

				StringBuffer sql = new StringBuffer(" SET ");
				sql.append(IDBConstants.QTY + " = QTY +'"
						+ String.valueOf(invumqty) + "'");
				sql.append("," + IDBConstants.USERFLD3 + " = '"
						+ map.get(IConstants.INV_EXP_DATE) + "'");
				sql.append("," + IDBConstants.USERFLD4 + " = '"
						+ map.get(IConstants.INV_BATCH) + "'");
				sql.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");
				sql.append("," + IConstants.EXPIREDATE + " = '"
						+ map.get(IConstants.EXPIREDATE) + "'");
				
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");

			} else if (!flag) {
				// INSERT DATA TO INV MST

				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map
						.get(IConstants.INV_BATCH));
				htInvMst.put(IDBConstants.QTY, String.valueOf(invumqty));
				htInvMst.put(IDBConstants.USERFLD3, map
						.get(IConstants.INV_EXP_DATE));
				htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.RECVDATE).toString().replaceAll("/", "") + "000000");
				htInvMst.put(IDBConstants.STATUS, "");
				htInvMst.put(IConstants.EXPIREDATE,map.get(IConstants.EXPIREDATE));
				flag = _InvMstDAO.insertInvMst(htInvMst);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	/* ************Modification History*********************************
	   Oct 13 2014 Bruhan, Description: To Change transaction date as Iconstant.TRAN_DATE
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
			htRecvHis.put("DIRTYPE",  TransactionConstants.GOODSRECEIPT);
			htRecvHis.put("ORDNUM",  map.get(IConstants.PONO));
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.REMARKS)));
			htRecvHis.put("BATNO", map.get(IConstants.INV_BATCH));
			htRecvHis.put("QTY", map.get(IConstants.INV_QTY));
			if((String)map.get(IConstants.UNITMO)!=null)
				htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UNITMO));
			String reason = (String)map.get(IConstants.RSNDESC);
            if(reason.equalsIgnoreCase("NOREASONCODE"))
            	{
            		reason="";
            	}
            
            String remarks = (String)map.get(IConstants.REMARKS);
            String empno=(String)map.get(IConstants.EMPNO);
            String empname=employeedao.getEmpname((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EMPNO),"");
           /* if (remarks.length()>0) 
            	{
            		remarks=remarks+",";
            	}*/
            
            if(empno.length()>0) {
       		      htRecvHis.put(IDBConstants.REMARKS, "PDA:"+empname+","+reason+","+remarks);
            	}
            else
       	     {
       		htRecvHis.put(IDBConstants.REMARKS, "PDA:"+reason+","+remarks);
       	     }
       		
		  	htRecvHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

/* ************Modification History*********************************
	   Oct 13 2014 Bruhan, Description: To Change RECVDATE
	 */
	public boolean processRecvDet(Map map) throws Exception {
		boolean flag = false;
		RecvDetDAO recvdetDao = new RecvDetDAO();
		recvdetDao.setmLogger(mLogger);
		POSHdrDAO POSHdrDAO = new POSHdrDAO();
		POSHdrDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvDet.put(IDBConstants.PODET_PONUM, map.get(IConstants.PONO));
			String plant = (String)map.get(IConstants.PLANT);
			flag = recvdetDao.isExisit(htRecvDet,plant);
			if(flag==false)
			{  
			    String ISPDA=(String)map.get(IConstants.ISPDA);
			    
			    Hashtable htPosDet = new Hashtable();
			    htPosDet.clear();
				
				htPosDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htPosDet.put(IDBConstants.POS_TRANID, map.get(IConstants.PONO));
				String plantt = (String)map.get(IConstants.PLANT);
				flag = POSHdrDAO.isExisit(htPosDet,"");
				if(flag==false)
				{ 
					
				
			    
			   /*if(ISPDA.length()>0 && ISPDA.equalsIgnoreCase("ISPDA")) {  
				   // new TblControlUtil().updateTblControlPDASeqNo((String)map.get(IConstants.PLANT),"GRRECEIPT","GR",(String)map.get(IConstants.PONO),true);
				   new TblControlUtil().updateTblControlIESeqNo((String)map.get(IConstants.PLANT),"GRN","GN",(String)map.get(IConstants.PONO));
			   }*/
			   
				}
			}
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		//	htRecvDet.put(IDBConstants.PODET_PONUM, map.get(IConstants.PONO));
			htRecvDet.put(IDBConstants.PODET_PONUM,"");
			htRecvDet.put("LNNO", map.get(IConstants.DODET_DOLNNO));
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, "");
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			// Retrieve item description
			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
			htRecvDet.put("BATCH", map.get(IConstants.INV_BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
//			htRecvDet.put("ORDQTY", "");
			htRecvDet.put("RECQTY", map.get(IConstants.INV_QTY));
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
            String rem = (String)map.get(IConstants.REMARKS);
            String reason = (String)map.get(IConstants.RSNDESC);
            if(reason.equalsIgnoreCase("NOREASONCODE")) reason="";
            if (rem.length()>0) rem=rem+",";
			htRecvDet.put("REMARK", rem+reason);
			htRecvDet.put("GRNO", map.get(IConstants.PONO));
		    htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "MR");
		    htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
		    htRecvDet.put("EXPIRYDAT",map.get(IConstants.EXPIREDATE));
		    htRecvDet.put(IConstants.EMPNO,map.get(IConstants.EMPNO));
		     
			flag = recvdetDao.insertRecvDet(htRecvDet);
			
			
			/*if(flag)
			{   //  new TblControlUtil().updateTblControlPDASeqNo((String)map.get(IConstants.PLANT),"GRRECEIPT","GR",(String)map.get(IConstants.PONO),true);
			    String ISPDA=(String)map.get(IConstants.ISPDA);
			   if(ISPDA.length()>0 && ISPDA.equalsIgnoreCase("ISPDA")) {
				   new TblControlUtil().updateTblControlPDASeqNo((String)map.get(IConstants.PLANT),"GRRECEIPT","GR",(String)map.get(IConstants.PONO),true);
			   }
			}*/

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processPodetForReceive(Map map) throws Exception {
		boolean flag = false;
		// update receive qty in podet
		PoDetDAO _PoDetDAO = new PoDetDAO((String) map.get("PLANT"));
		PoHdrDAO _PoHdrDAO = new PoHdrDAO((String) map.get("PLANT"));
		_PoDetDAO.setmLogger(mLogger);
		_PoHdrDAO.setmLogger(mLogger);
		Hashtable htCondiPoDet = new Hashtable();
		StringBuffer query = new StringBuffer("");

		// getQty from podet
		htCondiPoDet.put("PLANT", map.get("PLANT"));
		htCondiPoDet.put("pono", map.get("PONO"));
		htCondiPoDet.put("polnno", map.get("POLNNO"));

		query.append("isnull(QtyOr,0) as QtyOr");
		query.append(",isnull(QtyRc,0) as QtyRc");

		Map mQty = _PoDetDAO.selectRow(query.toString(), htCondiPoDet);

		double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
		double rcQty = Double.parseDouble((String) mQty.get("QtyRc"));

		double tranQty = Double.parseDouble((String) map
				.get(IConstants.INV_QTY));

		String queryPoDet = "";
		String queryPoHdr = "";

		if (ordQty == rcQty + tranQty) {
			queryPoDet = "set qtyrc= isNull(qtyrc,0) + "
					+ map.get(IConstants.INV_QTY) + " , LNSTAT='C' ";

		} else {
			queryPoDet = "set qtyrc= isNull(qtyrc,0) + "
					+ map.get(IConstants.INV_QTY) + " , LNSTAT='O' ";

		}

		flag = _PoDetDAO.update(queryPoDet, htCondiPoDet, "");

		if (flag) {
			Hashtable htCondiPoHdr = new Hashtable();
			htCondiPoHdr.put("PLANT", (String) map.get("PLANT"));
			htCondiPoHdr.put("pono", map.get("PONO"));

			flag = _PoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");

			if (flag)
				queryPoHdr = "set STATUS='O' ";
			else
				queryPoHdr = "set STATUS='C' ";

			flag = _PoHdrDAO.updatePO(queryPoHdr, htCondiPoHdr, "");

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
		htm.put(IConstants.BATCH,  map.get(IConstants.INV_BATCH));
		htm.put(IConstants.QTY, map.get(IConstants.INV_QTY));
		htm.put(IDBConstants.PRICE, "0");
		htm.put(IDBConstants.PAYMENTMODE, "");
        htm.put("TranId", map.get(IConstants.PONO));
		htm.put("TranType","GOODSRECEIPTWITHBATCH");
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
		htTrandId.put("POSTRANID", (String)map.get(IConstants.PONO));
		istranidExist=_posServlet.isExisit((String)map.get(IConstants.PLANT), htTrandId);
		try {
			 if(istranidExist) {
				  Hashtable	ht=new Hashtable();
				  ht.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
                  ht.put(IDBConstants.POS_TRANID, map.get(IConstants.PONO));
                  ht.put("DOLNNO", map.get(IConstants.DODET_DOLNNO));
				  ht.put(IDBConstants.POS_ITEM,map.get(IConstants.ITEM));
				  String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
	              ht.put(IDBConstants.POS_ITEMDESC,strUtils.InsertQuotes(sDesc));
	              ht.put(IDBConstants.POS_LOC,map.get(IConstants.LOC));
	              ht.put(IDBConstants.POS_BATCH, map.get(IConstants.INV_BATCH));
	              ht.put(IDBConstants.POS_QTY,map.get(IConstants.INV_QTY));
	              ht.put(IDBConstants.POSDET_STATUS,"C");
	              ht.put(IDBConstants.POS_UNITPRICE,"0");
	              ht.put(IDBConstants.POS_TOTALPRICE,"0");
	              ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
	              ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
	              ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	              ht.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
	              ht.put("POSTYPE","GOODSRECEIPTWITHBATCH");
	              ht.put(IConstants.UNITMO,map.get(IConstants.UNITMO));
	              ht.put(IConstants.EXPIREDATE,map.get(IConstants.EXPIREDATE));
	              flag =  posdet.insertIntoPosDet(ht);
			 } else {
				  Hashtable	hthdr=new Hashtable();
				  hthdr.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
				  hthdr.put(IDBConstants.POS_TRANID, map.get(IConstants.PONO));
				  hthdr.put("RECEIPTNO", map.get(IConstants.PONO));
				  hthdr.put(IDBConstants.STATUS, "C");
				  hthdr.put(IDBConstants.LOC,map.get(IConstants.LOC));
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
                  ht.put("DOLNNO", map.get(IConstants.DODET_DOLNNO));
				  ht.put(IDBConstants.POS_ITEM,map.get(IConstants.ITEM));
				  String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT),(String) map.get(IConstants.ITEM));
	              ht.put(IDBConstants.POS_ITEMDESC,strUtils.InsertQuotes(sDesc));
	              ht.put(IDBConstants.POS_LOC,map.get(IConstants.LOC));
	              ht.put(IDBConstants.POS_BATCH, map.get(IConstants.INV_BATCH));
	              ht.put(IDBConstants.POS_QTY,map.get(IConstants.INV_QTY));
	              ht.put(IDBConstants.POSDET_STATUS,"C");
	              ht.put(IDBConstants.POS_UNITPRICE,"0");
	              ht.put(IDBConstants.POS_TOTALPRICE,"0");
	              ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
	              ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
	              ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	              ht.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
	              ht.put("POSTYPE","GOODSRECEIPTWITHBATCH");
	              ht.put(IConstants.EXPIREDATE,map.get(IConstants.EXPIREDATE));
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