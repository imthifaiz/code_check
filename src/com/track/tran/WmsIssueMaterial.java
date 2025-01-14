package com.track.tran;

import java.text.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.BomDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ShipHisDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsIssueMaterial implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsIssueMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsIssueMaterial_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
    StrUtils su = null;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public WmsIssueMaterial() {
		dateUtils = new DateUtils();
	    su = new StrUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			
	        String creditLimitBy=StrUtils.fString((String) m.get(IConstants.CREDIT_LIMIT_BY));
	        
	        if(creditLimitBy.equalsIgnoreCase("NOLIMIT")) {
	        	flag = processDodetForIssue(m);
				if (flag) {
					flag = processInvMst(m);
				}
	        }else{
	        	DOUtil _DOUtil = new DOUtil();
				DoDetDAO _DoDetDAO = new DoDetDAO();	
				String PLANT = StrUtils.fString((String) m.get(IConstants.PLANT));
				String DO_NUM = StrUtils.fString((String) m.get(IConstants.DODET_DONUM));
				String DO_LN_NUM = StrUtils.fString((String) m.get(IConstants.DODET_DOLNNO));
				String ITEM = StrUtils.fString((String)	m.get(IConstants.ITEM));
				String CustName = StrUtils.fString((String)	m.get(IConstants.CUSTOMER_CODE));
				String issuingQty = StrUtils.fString((String) m.get(IConstants.QTY));
				
				StringBuffer query = new StringBuffer("");		
				Hashtable htCondiPoDet = new Hashtable();
				htCondiPoDet.put("PLANT", PLANT);
				htCondiPoDet.put("item", ITEM);
				htCondiPoDet.put("dono", DO_NUM);
				htCondiPoDet.put("dolnno", DO_LN_NUM);
				
				query.append("isnull(QtyOr,0) as QtyOr");
				query.append(",isnull(qtyPick,0) as qtyPick");
				query.append(",isnull(QtyIs,0) as QtyIs");
				Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);
				double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
				double pickQty = Double.parseDouble((String) mQty.get("qtyPick"));
				double isQty = Double.parseDouble((String) mQty.get("QtyIs"));
				
				double tranQty = Double.parseDouble(issuingQty);
				tranQty = StrUtils.RoundDB(tranQty, IConstants.DECIMALPTS);
				
				double sumqty = isQty + tranQty;		
				String ListPrice = new DOUtil().getUnitCostForProduct(PLANT,DO_NUM,ITEM,DO_LN_NUM); 

				double dListPrice = Double.parseDouble(ListPrice);
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("DONO", DO_NUM);
				String doHdrQuery = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,isnull(ORDERDISCOUNT,0) as OrderDiscount";
				ArrayList arraydohdr = new ArrayList();
			    arraydohdr = _DOUtil.getDoHdrDetails(doHdrQuery, htCond);
			    Map dataMap = (Map) arraydohdr.get(0);	    
			    String gstValue = dataMap.get("outbound_Gst").toString();
			    Float gstpercentage =  Float.parseFloat(((String) gstValue));
			    
			    String orderDiscountValue = dataMap.get("OrderDiscount").toString();
			    Float orderDiscountpercentage =  Float.parseFloat(((String) orderDiscountValue));
			    
			    double dOrderPrice = dListPrice*Double.parseDouble(issuingQty);
			    dOrderPrice = StrUtils.RoundDB(dOrderPrice,2);
			    double discountQty = (dOrderPrice*orderDiscountpercentage)/100;
			    discountQty = StrUtils.RoundDB(discountQty,2);
				double tax = (dOrderPrice*gstpercentage)/100;
		        tax = StrUtils.RoundDB(tax,2);
		        double issuedamount = dOrderPrice+tax-discountQty;
		        issuedamount = StrUtils.RoundDB(issuedamount,2);
		        
		        DateFormat currentdate = new SimpleDateFormat("dd/MM/yyyy");
		    	Date date = new Date();
		    	String current_date=currentdate.format(date); 
		    	
		    	Calendar firstDayOfMonth = Calendar.getInstance();   
		    	firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
		    	String startDate = currentdate.format(firstDayOfMonth.getTime()); 
		    	System.out.println(firstDayOfMonth.getTime()+"  "+startDate);
		    	
		        Calendar lastDayOfMonth = Calendar.getInstance();
		        lastDayOfMonth.set(Calendar.DATE, lastDayOfMonth.getActualMaximum(Calendar.DATE));
		        String endDate= currentdate.format(lastDayOfMonth.getTime());
		        System.out.println(firstDayOfMonth.getTime()+"  "+endDate); 
		        
		        if(creditLimitBy.equalsIgnoreCase("DAILY")) {
		        	startDate = current_date;
		        	endDate = current_date;
		        }
		        
		        String creditLimit=StrUtils.fString((String) m.get(IConstants.CREDITLIMIT));
				String outstdamt = new InvoicePaymentUtil().getOutStdAmt(PLANT,(String) m.get(IConstants.CUSTOMER_CODE), startDate, endDate);
				
				if(Double.parseDouble(creditLimit)-((Double.parseDouble(outstdamt)+issuedamount))>=0){
					flag = processDodetForIssue(m);
					if (flag) {
						flag = processInvMst(m);
					}
				}else{
					double allowedCreditval= Double.parseDouble(creditLimit)-(Double.parseDouble(outstdamt));
					if (allowedCreditval>0){
					   double taxPerQty = (dListPrice*gstpercentage)/100;
					   taxPerQty = StrUtils.RoundDB(taxPerQty,2);
					   double discountPerQty = (dListPrice*orderDiscountpercentage)/100;
					   discountPerQty = StrUtils.RoundDB(discountPerQty,2);
					   double dListPricePerQty = dListPrice+taxPerQty-discountPerQty;
					   double allowedqty =(allowedCreditval/dListPricePerQty);
					   double  allowedTotqty=(int) allowedqty;
					   if(allowedTotqty==0.0){
							m.put("msgflag","Goods issue cannot exceed Credit Limit!");
							throw new Exception("Goods issue cannot exceed " 
									   + (String) m.get(IConstants.CUSTOMER_CODE) 
									   + " Available Credit Limit "
									   + (String) m.get(IConstants.BASE_CURRENCY)
									   + (String) m.get(IConstants.CREDITLIMIT));
					   }
					   	m.put(IConstants.QTY, Double.toString(allowedTotqty));
						flag = processDodetForIssue(m);
						if (flag) {
							m.put("allowedpickqty", Double.toString(allowedTotqty));
							flag = processInvMstWithCreditLimit(m);
							if(flag){
								m.put("msgflag","Goods Issued  Partially Due to Credit Limit");	
							}
						}
					}else{	
						m.put("msgflag","Goods issue cannot exceed Credit Limit!");
						throw new Exception("Goods issue cannot exceed " 
								   + (String) m.get(IConstants.CUSTOMER_CODE) 
								   + " Available Credit Limit "
								   + (String) m.get(IConstants.BASE_CURRENCY)
								   + (String) m.get(IConstants.CREDITLIMIT));
					}
			   }
	        }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		boolean flagshiphis = false;
		String extCond = "";
		String sBatch = "";
		String sLoc = "";
		String sLocShipHis = "";
		String sIssueQty = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		DoDetDAO _DoDetDAO = new DoDetDAO();
		ShipHisDAO _ShipHisDAO = new ShipHisDAO();
		_InvMstDAO.setmLogger(mLogger);
		_DoDetDAO.setmLogger(mLogger);
		_ShipHisDAO.setmLogger(mLogger);
		String ExpDate="";
		String sIssuedate = "";
		try {
			// Get loc,batch from ShipHis
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			ht.put("dono", map.get(IConstants.DODET_DONUM));
			ht.put("dolnno", map.get(IConstants.DODET_DOLNNO));
			ht.put(IDBConstants.ITEM, map.get(IConstants.ITEM));

			List listQry = _DoDetDAO.getIssueShipHisDetailWMS(map.get(
			//List listQry = _DoDetDAO.getIssueShipHisDetailByWMS(map.get(
					IConstants.PLANT).toString(), map.get(
					IConstants.DODET_DONUM).toString(), map.get(
					IConstants.DODET_DOLNNO).toString(), map.get(
					IConstants.ITEM).toString());
			
			for (int i = 0; i < listQry.size(); i++) {
				
				Map m = (Map) listQry.get(i);
				sLocShipHis = (String) m.get("loc");
				
				sBatch = (String) m.get("batch");
				sIssueQty = (String) m.get("pickqty");
				sIssuedate= (String) m.get("ISSUEDATE");
				Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM));
		        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
		        if(!nonstocktype.equals("Y"))	
			    {
		        	double inqty = Double.valueOf(sIssueQty) * Double.valueOf((String)map.get("UOMQTY"));
				Hashtable htInvMst = new Hashtable();
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.USERFLD4, sBatch);
				htInvMst.put(IDBConstants.LOC, sLocShipHis);
				htInvMst.put(IDBConstants.CREATED_AT, sIssuedate.toString().replaceAll("/", "") + "000000");
				//htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.ISSUEDATE).toString().replaceAll("/", "") + "000000");
				StringBuffer sql = new StringBuffer(" SET ");
				sql.append("QTY" + " = QTY - " + inqty + " ");
				extCond = "AND QTY >='" + inqty + "' ";
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
				
				
			
				if (flag) {
					
					
					boolean bomexistsflag = false;
					boolean itemflag=false;
					
					boolean resultBomDelete=false;
					boolean movhisResult=false;
					String extcond="";
					
							
					ItemMstDAO _ItemMstDAO =new ItemMstDAO();
					BomDAO _BomDAO =new BomDAO() ;
					
					
					try {
					/*Hashtable htItemMst= new Hashtable();
					htItemMst.clear();
					
					htItemMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htItemMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					
					extcond=" userfld1='K'";
					
					itemflag = _ItemMstDAO .isExisit(htItemMst, extcond);
				
					if(itemflag)
					{*/
						Hashtable htBomMst = new Hashtable();
						htBomMst.clear();
						htBomMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
						htBomMst.put("PARENT_PRODUCT_LOC", sLocShipHis);
						htBomMst.put("PARENT_PRODUCT_BATCH",sBatch);

						bomexistsflag = _BomDAO.isExisit(htBomMst);
					
						if (bomexistsflag) {

							/*Hashtable hrBomDelete = new Hashtable();
							hrBomDelete.put("PLANT", map.get(IConstants.PLANT));
							hrBomDelete.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
							hrBomDelete.put("PARENT_PRODUCT_LOC",  sLocShipHis);
							hrBomDelete.put("PARENT_PRODUCT_BATCH", sBatch);
						
							Commanded on July071711 By Bruhan To disable remove item from Bom when Issuing Parent Item
						    resultBomDelete = _BomDAO.delete(hrBomDelete);*/
							
							
							 Hashtable htUpdateBOM = new Hashtable();
			    			 htUpdateBOM .put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			    			 htUpdateBOM .put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			    			 htUpdateBOM.put("PARENT_PRODUCT_LOC",  sLocShipHis);
			    			 htUpdateBOM .put("PARENT_PRODUCT_BATCH", sBatch);
			    			 
			    			 String loc = sLocShipHis.substring(sLocShipHis.indexOf("_")+1,sLocShipHis.length());
			 		        			    		
			    			 StringBuffer sql1 = new StringBuffer(" SET ");
			 				 sql1.append(" " +"STATUS" + " = 'C',PARENT_PRODUCT_LOC='"+loc+"',CHILD_PRODUCT_LOC='"+loc+"'");
		    				 sql1.append("," + IDBConstants.UPDATED_AT1 + " = '"
			 						+ dateUtils.getDateTime() + "'");
			 				 flag=_BomDAO.update(sql1.toString(), htUpdateBOM, " ");
							
						}
					// }
				
						
					} //end try
			        catch (Exception e) {

						this.mLogger.exception(this.printLog, "Kitting Outbound Issue", e);
						throw e;

					}
					
			      //update bom for outbound issue
				}
			    }else{
					flag=true;  // For NonStock Item Skip Inventory  and BOM Update
				}
				if(flag){
				String strStatus = "C";
				Hashtable htShipHis = new Hashtable();
				htShipHis.clear();
				htShipHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htShipHis.put("dono", map.get(IConstants.DODET_DONUM));
				htShipHis.put("dolno", map.get(IConstants.DODET_DOLNNO));
				htShipHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htShipHis.put("BATCH", sBatch);
				htShipHis.put(IDBConstants.LOC, sLocShipHis);
				htShipHis.put(IDBConstants.ISSUEDATE, sIssuedate);
				//htShipHis.put(IDBConstants.INVOICENO, map.get(IConstants.INVOICENO));
				StringBuffer sqlshiphis = new StringBuffer(" SET ");
				sqlshiphis.append("INVOICENO = '" + map.get(IConstants.INVOICENO) + "', STATUS" + " = '" + strStatus + "',USERFLD1=  '" + map.get("SHIPPINGNO") + "'");
				String extraCon = "  AND ISNULL(STATUS,'')='O'";
				flagshiphis = _DoDetDAO.updateShipHis(sqlshiphis.toString(), htShipHis, extraCon);
				}

				if (flag) {
					MovHisDAO movHisDao = new MovHisDAO();
					movHisDao.setmLogger(mLogger);
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htMovHis.put("DIRTYPE", TransactionConstants.ORD_ISSUE);
					htMovHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DODET_DONUM));
					htMovHis.put(IDBConstants.MOVHIS_ORDLNO, map.get(IConstants.DODET_DOLNNO));
					htMovHis.put("MOVTID", "OUT");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.LOC, sLocShipHis);
					htMovHis.put("BATNO", sBatch);
					htMovHis.put("QTY", sIssueQty);
					if((String)map.get(IConstants.CUSTOMER_CODE)!=null)
						htMovHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
					if((String)map.get(IConstants.UOM)!=null)
						htMovHis.put(IDBConstants.UOM, map.get(IConstants.UOM));
					htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
					htMovHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htMovHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htMovHis.put(IDBConstants.REMARKS,  su.InsertQuotes((String)map.get(IConstants.REMARKS)));

					flag = movHisDao.insertIntoMovHis(htMovHis);
				}

			}
			if(listQry.isEmpty())
			{
				
				throw new Exception(
				"Not Enough Inventory in Shipping Area To Issue");	
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception(
					"Not Enough Inventory in Shipping Area To Issue");
		}

		return flag;
	}

	public boolean processDodetForIssue(Map map) throws Exception {
		boolean flag = false;
		boolean dotransferdetflag=false;
		boolean dotransferhdrflag=false;
		try {

			// update Issue qty in dodet
			DoDetDAO _DoDetDAO = new DoDetDAO();
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
			DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
			_DoDetDAO.setmLogger(mLogger);
			_DoHdrDAO.setmLogger(mLogger);
			_DoTransferDetDAO.setmLogger(mLogger);
			_DoTransferHdrDAO.setmLogger(mLogger);
			
            Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");

			htCondiPoDet.put("PLANT", map.get(IConstants.PLANT));
			htCondiPoDet.put("item", map.get(IConstants.ITEM));
			htCondiPoDet.put("dono", map.get(IConstants.DODET_DONUM));
			htCondiPoDet.put("dolnno", map.get(IConstants.DODET_DOLNNO));
			
		
			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");
			query.append(",isnull(QtyIs,0) as QtyIs");

			Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);
			
			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double pickQty = Double.parseDouble((String) mQty.get("qtyPick"));
			double isQty = Double.parseDouble((String) mQty.get("QtyIs"));

			double tranQty = Double.parseDouble((String) map
					.get(IConstants.QTY));
			tranQty = StrUtils.RoundDB(tranQty, IConstants.DECIMALPTS);
			pickQty = StrUtils.RoundDB(pickQty, IConstants.DECIMALPTS);
			String queryPoDet = "";
			String queryPoHdr = "";
			String queryDoTransferDet="";
			String queryDoTransferHdr="";
			double sumqty = isQty + tranQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			System.out.println("orderqty"+ordQty+"issueqty"+sumqty+"pickqty"+pickQty);			
			if ((ordQty == pickQty) && pickQty == sumqty) {

				queryPoDet = "set qtyis= isNull(qtyis,0) + "
						+ map.get(IConstants.QTY) + " , LNSTAT='C' ";
		
			} else {
				queryPoDet = "set qtyis= isNull(qtyis,0) + "
						+ map.get(IConstants.QTY) + " , LNSTAT='O' ";
		
			}

			// Added By Samatha extracond to Controll the issue Qty Excced the
			// PickQty Aug 24 1010
			String extraCond = " AND  convert(decimal,qtyPick) >= convert(decimal,isNull(qtyis,0) + "
					+ map.get(IConstants.QTY)+")";

			flag = _DoDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			
			if (flag) {
	
				 //dotransferdetflag=_DoTransferDetDAO.update(queryPoDet, htCondiPoDet, "");
				//update Header Part
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
				htCondiPoHdr.put("dono", map.get(IConstants.DODET_DONUM));
				flag = _DoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");
          
				if (flag)
					queryPoHdr = "set STATUS='O' ";
				else
					queryPoHdr = "set STATUS='C' ";
					

				flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				
				//dotransferhdrflag=_DoTransferHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("Exception"+e.getMessage());			
			throw new Exception("Issue Qty Exceeded the Pick Qty to Issue");
		}
		
		return flag;
		
		
	}
	
	public boolean processInvMstWithCreditLimit(Map map) throws Exception {

		boolean flag = false;
		boolean flagshiphis = false;
		String extCond = "";
		String sBatch = "";
		String sLoc = "";
		String sLocShipHis = "";
		String sIssueQty = "",pickqty="";
		double leftpickqty=0.0;
		InvMstDAO _InvMstDAO = new InvMstDAO();
		DoDetDAO _DoDetDAO = new DoDetDAO();
		ShipHisDAO _ShipHisDAO = new ShipHisDAO();
		_InvMstDAO.setmLogger(mLogger);
		_DoDetDAO.setmLogger(mLogger);
		_ShipHisDAO.setmLogger(mLogger);
		String ExpDate="";
		try {
			// Get loc,batch from ShipHis
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			ht.put("dono", map.get(IConstants.DODET_DONUM));
			ht.put("dolnno", map.get(IConstants.DODET_DOLNNO));
			ht.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			String allowedpickqty=StrUtils.fString((String) map.get("allowedpickqty"));
			
			List listQry = _DoDetDAO.getIssueShipHisDetailByWMS(map.get(
					IConstants.PLANT).toString(), map.get(
					IConstants.DODET_DONUM).toString(), map.get(
					IConstants.DODET_DOLNNO).toString(), map.get(
					IConstants.ITEM).toString());
			
			for (int i = 0; i < listQry.size(); i++) {
				
				Map m = (Map) listQry.get(i);
				sLocShipHis = (String) m.get("loc");
				
				sBatch = (String) m.get("batch");
				sIssueQty = allowedpickqty;
				pickqty = (String) m.get("pickqty");
				leftpickqty =Double.parseDouble(pickqty)-Double.parseDouble(sIssueQty);
				pickqty=Double.toString(leftpickqty);
				Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM));
		        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
		        if(!nonstocktype.equals("Y"))	
			    {
				Hashtable htInvMst = new Hashtable();
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.USERFLD4, sBatch);
				htInvMst.put(IDBConstants.LOC, sLocShipHis);
				
				StringBuffer sql = new StringBuffer(" SET ");
				sql.append("QTY" + " = QTY - " + sIssueQty + " ");
				extCond = "AND QTY >='" + sIssueQty + "' ";
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
				
				
			
				if (flag) {
					
					
					boolean bomexistsflag = false;
					boolean itemflag=false;
					
					boolean resultBomDelete=false;
					boolean movhisResult=false;
					String extcond="";
					
							
					ItemMstDAO _ItemMstDAO =new ItemMstDAO();
					BomDAO _BomDAO =new BomDAO() ;
					
					
					try {
					/*Hashtable htItemMst= new Hashtable();
					htItemMst.clear();
					
					htItemMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htItemMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					
					extcond=" userfld1='K'";
					
					itemflag = _ItemMstDAO .isExisit(htItemMst, extcond);
				
					if(itemflag)
					{*/
						Hashtable htBomMst = new Hashtable();
						htBomMst.clear();
						htBomMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
						htBomMst.put("PARENT_PRODUCT_LOC", sLocShipHis);
						htBomMst.put("PARENT_PRODUCT_BATCH",sBatch);

						bomexistsflag = _BomDAO.isExisit(htBomMst);
					
						if (bomexistsflag) {

							/*Hashtable hrBomDelete = new Hashtable();
							hrBomDelete.put("PLANT", map.get(IConstants.PLANT));
							hrBomDelete.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
							hrBomDelete.put("PARENT_PRODUCT_LOC",  sLocShipHis);
							hrBomDelete.put("PARENT_PRODUCT_BATCH", sBatch);
						
							Commanded on July071711 By Deen To disable remove item from Bom when Issuing Parent Item
						    resultBomDelete = _BomDAO.delete(hrBomDelete);*/
							
							
							 Hashtable htUpdateBOM = new Hashtable();
			    			 htUpdateBOM .put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			    			 htUpdateBOM .put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			    			 htUpdateBOM.put("PARENT_PRODUCT_LOC",  sLocShipHis);
			    			 htUpdateBOM .put("PARENT_PRODUCT_BATCH", sBatch);
			    			 
			    			 String loc = sLocShipHis.substring(sLocShipHis.indexOf("_")+1,sLocShipHis.length());
			 		        			    		
			    			 StringBuffer sql1 = new StringBuffer(" SET ");
			 				 sql1.append(" " +"STATUS" + " = 'C',PARENT_PRODUCT_LOC='"+loc+"',CHILD_PRODUCT_LOC='"+loc+"'");
		    				 sql1.append("," + IDBConstants.UPDATED_AT1 + " = '"
			 						+ dateUtils.getDateTime() + "'");
			 				 flag=_BomDAO.update(sql1.toString(), htUpdateBOM, " ");
							
						}
					// }
				
						
					} //end try
			        catch (Exception e) {

						this.mLogger.exception(this.printLog, "Kitting Outbound Issue", e);
						throw e;

					}
					
			      //update bom for outbound issue
				}
			    }else{
					flag=true;  // For NonStock Item Skip Inventory  and BOM Update
				}
				if(flag){
				String strStatus = "O";
				Hashtable htShipHis = new Hashtable();
				htShipHis.clear();
				htShipHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htShipHis.put("dono", map.get(IConstants.DODET_DONUM));
				htShipHis.put("dolno", map.get(IConstants.DODET_DOLNNO));
				htShipHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htShipHis.put("BATCH", sBatch);
				htShipHis.put(IDBConstants.LOC, sLocShipHis);
				StringBuffer sqlshiphis = new StringBuffer(" SET ");
				//sqlshiphis.append("pickqty" + " = '" + pickqty + "',STATUS" + " = '" + strStatus + "',USERFLD1=  '" + map.get("SHIPPINGNO") + "'");
				sqlshiphis.append(" STATUS" + " = '" + strStatus + "',USERFLD1=  '" + map.get("SHIPPINGNO") + "'");
				String extraCon = "  AND ISNULL(STATUS,'')='O'";
				flagshiphis = _DoDetDAO.updateShipHis(sqlshiphis.toString(), htShipHis, extraCon);
				}

				if (flag) {
					MovHisDAO movHisDao = new MovHisDAO();
					movHisDao.setmLogger(mLogger);
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htMovHis.put("DIRTYPE", TransactionConstants.ORD_ISSUE);
					htMovHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DODET_DONUM));
					htMovHis.put(IDBConstants.MOVHIS_ORDLNO, map.get(IConstants.DODET_DOLNNO));
					htMovHis.put("MOVTID", "OUT");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.LOC, sLocShipHis);
					htMovHis.put("BATNO", sBatch);
					htMovHis.put("QTY", sIssueQty);
					if((String)map.get(IConstants.CUSTOMER_CODE)!=null)
						htMovHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
					if((String)map.get(IConstants.UOM)!=null)
						htMovHis.put(IDBConstants.UOM, map.get(IConstants.UOM));
					htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
					htMovHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htMovHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htMovHis.put(IDBConstants.REMARKS,  su.InsertQuotes((String)map.get(IConstants.REMARKS)));

					flag = movHisDao.insertIntoMovHis(htMovHis);
				}

			}
			if(listQry.isEmpty())
			{
				
				throw new Exception(
				"Not Enough Inventory in Shipping Area To Issue");	
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception(
					"Not Enough Inventory in Shipping Area To Issue");
		}

		return flag;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {

		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}
}