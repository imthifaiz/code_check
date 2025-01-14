package com.track.tran;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TempDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.TempUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsPickIssue implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsPickIssue_PRINTPLANTMASTERLOG;
	

	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
        DOUtil doutil = new DOUtil();
        TempUtil _TempUtil = new  TempUtil();
        TempDAO _TempDAO = new TempDAO();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public WmsPickIssue() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			String creditLimitBy=StrUtils.fString((String) m.get(IConstants.CREDIT_LIMIT_BY));
			
			if(creditLimitBy.equalsIgnoreCase("NOLIMIT")) {
				flag = processDodet(m);			
				if (flag) {				
					String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
					if(!nonstocktype.equalsIgnoreCase("Y"))	
				    {
						flag = processInvMst(m);
				    }		
				}else{				
					flag=true;
			    }			
				if (flag) {
					flag = processMovHis_OUT(m);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						MLogger.log(0, "Exception locationTransfer"+ e.getMessage());
					}	
				}
			}else {
				DOUtil _DOUtil = new DOUtil();
				DoDetDAO _DoDetDAO = new DoDetDAO();	
				String PLANT = StrUtils.fString((String)	m.get(IConstants.PLANT));
				String DO_NUM = StrUtils.fString((String)	m.get(IConstants.DODET_DONUM));
				String DO_LN_NUM = StrUtils.fString((String)	m.get(IConstants.DODET_DOLNNO));
				String ITEM = StrUtils.fString((String)	m.get(IConstants.ITEM));
				String CustName = StrUtils.fString((String)	m.get(IConstants.CUSTOMER_CODE));
				String issuingQty = StrUtils.fString((String)	m.get(IConstants.QTY));
				
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
		    	
		        Calendar lastDayOfMonth = Calendar.getInstance();
		        lastDayOfMonth.set(Calendar.DATE, lastDayOfMonth.getActualMaximum(Calendar.DATE));
		        String endDate= currentdate.format(lastDayOfMonth.getTime()); 
		        
		        if(creditLimitBy.equalsIgnoreCase("DAILY")) {
		        	startDate = current_date;
		        	endDate = current_date;
		        }
		        
		        String creditLimit=StrUtils.fString((String) m.get(IConstants.CREDITLIMIT));
				String outstdamt = new InvoicePaymentUtil().getOutStdAmt(PLANT,(String) m.get(IConstants.CUSTOMER_CODE), startDate, endDate);
				
				if(Double.parseDouble(creditLimit)-((Double.parseDouble(outstdamt)+issuedamount))>=0){
					flag = processDodet(m);			
					if (flag) {				
						String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
						if(!nonstocktype.equalsIgnoreCase("Y"))	
					    {
							flag = processInvMst(m);
					    }		
					}else{				
						flag=true;
				    }			
					if (flag) {
						flag = processMovHis_OUT(m);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							MLogger.log(0, "Exception locationTransfer"+ e.getMessage());
						}	
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
						   throw new Exception("Goods issue cannot exceed " 
								   + (String) m.get(IConstants.CUSTOMER_CODE) 
								   + " Available Credit Limit "
								   + (String) m.get(IConstants.BASE_CURRENCY)
								   + (String) m.get(IConstants.CREDITLIMIT));	
					   }
					   m.put(IConstants.QTY, Double.toString(allowedTotqty));
					   String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
					   if(!nonstocktype.equalsIgnoreCase("Y")){
						   flag = processInvMst(m);
					   	}else{
					    	flag=true;
					    }
					   if (flag) {
						   flag = processDodet(m);
					   }
					   if (flag) {
						   flag = processMovHis_OUT(m);
						   if(flag){
							   m.put("msgflag","Goods Issued Partially Due to Credit Limit");
						   }
						   try {
							   Thread.sleep(1000);
						   } catch (Exception e) {
							   MLogger.log(0, "Exception locationTransfer" + e.getMessage());
						   }
						}
					}else{
						throw new Exception("Goods issue cannot exceed " 
								   + (String) m.get(IConstants.CUSTOMER_CODE) 
								   + " Available Credit Limit "
								   + (String) m.get(IConstants.BASE_CURRENCY)
								   + (String) m.get(IConstants.CREDITLIMIT));	
				   }
				}
			}
						
			String type = 	m.get("TYPE").toString();
			if(flag && type.equalsIgnoreCase("OBRANDOM"))
			{
				flag = processTempRemove(m);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	/* * ************Modification History*********************************
	   Sep 25 2014 Bruhan, Description: To include ISSUEDATE in SHIPHIS table
    */
	public boolean processDodet(Map map) throws Exception {
		//---Modified by Bruhan on March 04 2014, Description: Insert Container as 'NOCONTAINER'
        String ExpiryDate="";
		boolean flag = false;
		boolean flag1 = false;
		boolean dotransferdetflag=false;
		boolean dotransferhdrflag=false;
		try {
			DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			_DoHdrDAO.setmLogger(mLogger);
			/*
			 * DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO(); DoTransferHdrDAO
			 * _DoTransferHdrDAO = new DoTransferHdrDAO();
			 * _DoTransferDetDAO.setmLogger(mLogger); _DoTransferHdrDAO.setmLogger(mLogger);
			 */
			String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));  
			Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");
			// getQty from podet

			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("dono", map.get(IConstants.DODET_DONUM));
			htCondiPoDet.put("dolnno", map.get(IConstants.DODET_DOLNNO));
			htCondiPoDet.put(IDBConstants.ITEM,  map.get(IConstants.ITEM));

			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");

			Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);

			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double qtyPick = Double.parseDouble((String) mQty.get("qtyPick"));

			double tranQty = Double.parseDouble((String) map.get(IConstants.QTY));
			double sumqty = qtyPick + tranQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			String queryPoDet = "";
			String queryPoHdr = "";
			String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + "+ map.get(IConstants.QTY);
			if (ordQty == sumqty) {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + " + map.get(IConstants.QTY) + " ,qtyis= isNull(qtyis,0) + " + map.get(IConstants.QTY) + ", PickStatus='C',Lnstat='C' ";

			} else {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) +  " ,qtyis= isNull(qtyis,0) + " + map.get(IConstants.QTY) + " , PickStatus='O',Lnstat='O' ";

			}

			flag = _DoDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			
			if (flag) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("dono", map.get(IConstants.DODET_DONUM));

				flag = _DoDetDAO.isExisit(htCondiPoHdr,
						"pickStatus in ('O','N')");
				if (flag)
					queryPoHdr = "set  STATUS='O',PickStaus='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
				else
					queryPoHdr = "set PickStaus='C', STATUS='C',ORDER_STATUS='PROCESSED' "; //, STATUS='C'

				flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				
				if (flag) {
					
					 //dotransferdetflag=_DoTransferDetDAO.update(queryPoDet, htCondiPoDet, "");
					
					 htCondiPoHdr.clear();
					htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
					htCondiPoHdr.put("dono", map.get(IConstants.DODET_DONUM));
					flag1 = _DoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");
	          
					if (flag1)
						queryPoHdr = "set STATUS='O' ";
					else
						queryPoHdr = "set STATUS='C' ";
								
					//flag=_DoTransferHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				}
				
				if(!nonstocktype.equalsIgnoreCase("Y"))	
			    {
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
						
				ExpiryDate= _InvMstDAO.getInvExpireDate( (String)map.get(IConstants.PLANT), 
						(String)map.get(IConstants.ITEM),(String) map.get(IConstants.LOC),
						(String)map.get(IConstants.BATCH));
			    }
				 
				boolean pickdet = false;
				Hashtable htPickDet = new Hashtable();
				Hashtable htInvMst = new Hashtable();
				
				htPickDet.clear();
				htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htPickDet.put("DONO", map.get(IConstants.DODET_DONUM));
				htPickDet.put("DOLNO", map.get(IConstants.DODET_DOLNNO));
				htPickDet.put(IDBConstants.CUSTOMER_NAME, su.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htPickDet.put(IDBConstants.ITEM_DESC,su.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
				htPickDet.put("BATCH", map.get(IConstants.BATCH));
				htPickDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htPickDet.put("LOC1", map.get(IConstants.LOC));
				htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
				htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htPickDet.put("CONTAINER", "NOCONTAINER");
				htPickDet.put("REMARK", map.get(IConstants.INV_EXP_DATE));
				htPickDet.put("ExpiryDat", ExpiryDate);
			
				Hashtable htCurrency = new Hashtable();
				htCurrency.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htCurrency.put("DONO", map.get(IConstants.DODET_DONUM));
				String currencyid = doutil.getDohdrcol(htCurrency, "CURRENCYID");
				htCurrency.put("DOLNNO", map.get(IConstants.DODET_DOLNNO));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));				
					
				String unitprice = doutil.getDodetcol(htCurrency, "UNITPRICE");

			htPickDet.put(IDBConstants.CURRENCYID, currencyid);
			htPickDet.put("UNITPRICE", unitprice);
			htPickDet.put(IDBConstants.USERFLD1, map.get("SHIPPINGNO"));
			htPickDet.put("STATUS", "C");
			htPickDet.put(IDBConstants.ISSUEDATE,  map.get(IConstants.ISSUEDATE));
			htPickDet.put(IDBConstants.INVOICENO, map.get(IConstants.INVOICENO));
			if(nonstocktype.equals("Y"))	
		    {
	        	htPickDet.put("PICKQTY", String.valueOf(map.get(IConstants.QTY)));
	        	pickdet = _DoDetDAO.insertPickDet(htPickDet);
		    }
	        else
	        {
				
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				String extCond = "";
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
					extCond = "QTY >= " + map.get(IConstants.QTY);
				}else{
					extCond = "QTY > 0";
				}
				InvMstDAO _InvMstDAO = new InvMstDAO();
				ArrayList alStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);
				if (!alStock.isEmpty()) {
					double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
					Iterator iterStock = alStock.iterator();
					while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
						Map mapIterStock = (Map)iterStock.next();
						double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
						double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
						
						htPickDet.put("PICKQTY", String.valueOf(adjustedQuantity));
						htPickDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
						pickdet = _DoDetDAO.insertPickDet(htPickDet);	
						quantityToAdjust -= adjustedQuantity;
					}
				}
	        }

		}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Pick Qty Exceeded the Order Qty to Pick");
		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		 
		try {
			
				flag = processInvRemove(map);
	
				if (flag) {
					flag=processBOM(map);
					
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
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			//String cond = " QTY >= " + map.get(IConstants.QTY);
			String extCond = "";
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				double inqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				extCond = "QTY >= " + inqty;
			}else{
				extCond = "QTY > 0";
			}
			


			flag = _InvMstDAO.isExisit(htInvMst, extCond);

			if (flag) {
				//	Get details in ascending order of CRAT for that batch
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
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
						sql1.append(IDBConstants.QTY + " = QTY -'" + adjustedQuantity + "'");
						Hashtable htInvMstReduce = new Hashtable();
						htInvMstReduce.clear();
						htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
						htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
						htInvMstReduce.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
						htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
						htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
						flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");
						if (!flag) {
							throw new Exception("Could not update");
						}
						quantityToAdjust -= adjustedQuantity;
					}
				}
			} else {
				throw new Exception("Error in picking OutBound Order : Inventory not found for the product: " +map.get(IConstants.ITEM)+ " with batch: " +map.get(IConstants.BATCH)+ "  scanned at the location  "+map.get(IConstants.LOC));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}
	
	
	//start code by Bruhan to delete line in temp table on 06 August 2013
	private boolean processTempRemove(Map map) throws Exception {
		boolean flag = false;
		try {
			
			
			Hashtable htdeletetemp = new Hashtable();
			htdeletetemp.put("PLANT", map.get(IConstants.PLANT));
			htdeletetemp.put("ORDERNO", map.get(IConstants.DODET_DONUM));
			htdeletetemp.put("ITEM", map.get(IConstants.ITEM));
			htdeletetemp.put("LOC", map.get(IConstants.LOC));
			htdeletetemp.put("BATCH", map.get(IConstants.BATCH));
			
			double qty = Double.parseDouble((String)map.get(IConstants.PICKQTY));
			
			
			if(qty<0){
			flag = _TempUtil.deletetemp(htdeletetemp);}
			else{
				flag = true;
			}
			
			
			
			
			if(flag)
			{

			} else {
				throw new Exception("Error in deleting line in temp table");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}
	
	//End code by Bruhan to delete line in temp table on 06 August 2013

	
	
	
	private boolean processBOM(Map map) throws Exception {
		boolean isExists = false;
		boolean itemflag=false;
		boolean flag=true;
		String extcond="";
		
		ItemMstDAO _ItemMstDAO =new ItemMstDAO();
		BomDAO _BomDAO =new BomDAO() ;
		
		try
		{
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
			htBomMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
			htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

			isExists = _BomDAO.isExisit(htBomMst);
			
			if(isExists)
			{				
				 Hashtable htUpdateBOM = new Hashtable();
			    			 htUpdateBOM .put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			    			 htUpdateBOM .put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			    			 htUpdateBOM.put("PARENT_PRODUCT_LOC",   map.get(IConstants.LOC));
			    			 htUpdateBOM .put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));
			    		
			    			 StringBuffer sql1 = new StringBuffer(" SET ");
			 				 sql1.append(" " +"STATUS" + " = 'C' ");
		    				 sql1.append("," + IDBConstants.UPDATED_AT1 + " = '" + dateUtils.getDateTime() + "'");
			 				 flag=_BomDAO.update(sql1.toString(), htUpdateBOM, " ");
							
				
			    } 
			/*else {
				throw new Exception("Unable to process kitting outbound picking location transfer, Location not found");
			}
		}*/
	
		
		return flag;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

	}

	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htMovHis = new Hashtable();
			String isSales_Purchase = new PlantMstDAO().getIsSalesToPurchase((String)map.get(IConstants.PLANT));//imthi
			if(isSales_Purchase == null) isSales_Purchase = "0";
			String plnt = new PlantMstDAO().CheckPlantDesc((String)map.get(IConstants.CUSTOMER_NAME));//imthi
			if(plnt == null) plnt = "0";
			String currentplntdesc = new PlantMstDAO().getcmpyname((String)map.get(IConstants.PLANT));//imthi
			htMovHis.clear();
			htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovHis.put("DIRTYPE", TransactionConstants.ORD_PICK_ISSUE);
			htMovHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovHis.put("BATNO", map.get(IConstants.BATCH));
			htMovHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htMovHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DODET_DONUM));
			htMovHis.put("MOVTID", "OUT");
			htMovHis.put("RECID", "");
			htMovHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
//			htMovHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htMovHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			if((String)map.get(IConstants.UOM)!=null)
			htMovHis.put(IConstants.UOM,  map.get(IConstants.UOM));
			htMovHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			if(isSales_Purchase.equalsIgnoreCase("1")) {
				htMovHis.put(IDBConstants.REMARKS, map.get(IConstants.CUSTOMER_NAME) +"_"+ map.get("PONO"));
			}else {
				htMovHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS) +","+ su.InsertQuotes((String)map.get(IConstants.INV_EXP_DATE))+","+ map.get(IConstants.INVOICENO));
			}
			
			flag = movHisDao.insertIntoMovHis(htMovHis);

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