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

import com.track.constants.MLoggerConstant;
import com.track.db.util.DOUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.EmployeeDAO;
import com.track.db.util.DOUtil;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;


public class WmsPickingRandom implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsPickingRandom_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsPickingRandom_PRINTPLANTMASTERINFO;

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

	public WmsPickingRandom() {
		dateUtils = new DateUtils();
	}
	
	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			String creditLimitBy=StrUtils.fString((String) m.get(IConstants.CREDIT_LIMIT_BY));
			if(creditLimitBy.equalsIgnoreCase("NOLIMIT")) {
				flag = processDodetForPick(m);
				if (flag) {
					String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
			        if(!nonstocktype.equals("Y"))	
				    {
					flag = processInvMst(m);
				    }else{
				    	flag=true;				 
				    }
		        }
			
				if (flag) {
					flag = processMovHis_OUT(m);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						MLogger.log(0, "Exception locationTransfer"
								+ e.getMessage());
					}
				}
			}else {
				DOUtil _DOUtil = new DOUtil();
				DoDetDAO _DoDetDAO = new DoDetDAO();	
				String PLANT = StrUtils.fString((String)	m.get(IConstants.PLANT));
				String DO_NUM = StrUtils.fString((String)	m.get(IConstants.PODET_PONUM));
				String ITEM = StrUtils.fString((String)	m.get(IConstants.ITEM));
				String CustName = StrUtils.fString((String)	m.get(IConstants.CUSTOMER_NAME));
				String issuingQty = StrUtils.fString((String)	m.get(IConstants.QTY));
				
				StringBuffer query = new StringBuffer("");		
				Hashtable htCondiPoDet = new Hashtable();
				htCondiPoDet.put("PLANT", PLANT);
				htCondiPoDet.put("item", ITEM);
				htCondiPoDet.put("dono", DO_NUM);
				
				query.append("isnull(QtyOr,0) as QtyOr");
				query.append(",isnull(qtyPick,0) as qtyPick");
				query.append(",isnull(QtyIs,0) as QtyIs");

				Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);
				double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
				double pickQty = Double.parseDouble((String) mQty.get("qtyPick"));
				double isQty = Double.parseDouble((String) mQty.get("QtyIs"));
				double tranQty = Double.parseDouble(issuingQty);
						
				String ListPrice = new DOUtil().getUnitCostForProductWithoutlineno(PLANT,DO_NUM,ITEM);
				
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
					flag = processDodetForPick(m);
					if (flag) {
						String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
				        if(!nonstocktype.equals("Y"))	
					    {
						flag = processInvMst(m);
					    }else{
					    	flag=true;				 
					    }
				        }
				
					if (flag) {
						flag = processMovHis_OUT(m);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							MLogger.log(0, "Exception locationTransfer"
									+ e.getMessage());
						}
					}
				}else {
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
							   m.put("msgflag","Goods issue cannot exceed " 
									   + (String) m.get(IConstants.CUSTOMER_CODE) 
									   + " Available Credit Limit "
									   + (String) m.get(IConstants.BASE_CURRENCY)
									   + (String) m.get(IConstants.CREDITLIMIT));
							   throw new Exception("Goods issue cannot exceed " 
									   + (String) m.get(IConstants.CUSTOMER_CODE) 
									   + " Available Credit Limit "
									   + (String) m.get(IConstants.BASE_CURRENCY)
									   + (String) m.get(IConstants.CREDITLIMIT));	
						   }
						   m.put(IConstants.QTY, Double.toString(allowedTotqty));
						   m.put("TRAN_QTY", Double.toString(allowedTotqty));
						   flag = processDodetForPick(m);
							if (flag) {
								String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
						        if(!nonstocktype.equals("Y"))	
							    {
								flag = processInvMst(m);
							    }else{
							    	flag=true;				 
							    }
						        }
						
							if (flag) {
								flag = processMovHis_OUT(m);
								try {
									if(flag){
									   m.put("msgflag","Goods Issued Partially Due to Credit Limit");
								   }
									Thread.sleep(1000);
								} catch (Exception e) {
									MLogger.log(0, "Exception locationTransfer"+ e.getMessage());
								}
							}
					}else {
						m.put("msgflag","Goods issue cannot exceed " 
								   + (String) m.get(IConstants.CUSTOMER_CODE) 
								   + " Available Credit Limit "
								   + (String) m.get(IConstants.BASE_CURRENCY)
								   + (String) m.get(IConstants.CREDITLIMIT));
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

	public boolean processDodetForPick(Map map) throws Exception {
        String ExpiryDate="";
		boolean flag = false;
		DOUtil _DOUtil = new DOUtil();
		String DOLNO="";
		double ORDQTY=0;
		try {
			DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			_DoHdrDAO.setmLogger(mLogger);
			DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
			DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
               			
			String queryPoDet = "";
			String queryPoHdr = "";
						
			//process dodet qty  start
			boolean insertFlag=false;
			boolean dotransferdetflag=false;
			double totBal = 0;
        	double orderqty = 0;		 
		    double pickedqty = 0;
		    double balQty=0;
		    String PICKING_QTY = (String) map.get("QTY");
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
			pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			
			double sumqty =0;
			
			ArrayList alResult = new ArrayList();
			alResult = _DOUtil.getDODetDetailsRandomMultiUOM((String)map.get("PLANT"),(String) map.get("PONO"),(String) map.get("ITEM"),(String) map.get("UNITMO"));
			
			 if(alResult.size()>=1){
							 
				  for (int i = 0; i < alResult.size(); i++) {           		  	
	          		  Map mapLn = (Map) alResult.get(i);
		          		   totBal = Double.parseDouble((String)mapLn.get("totBal"));
		            	   orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
		 		           pickedqty = Double.parseDouble((String)mapLn.get("qtyPick"));
		 		          
	 		         		              
		 		           balQty = orderqty - pickedqty;
		 		           DOLNO =  StrUtils.fString((String)mapLn.get("dolnno"));
		 		           map.put(IConstants.PODET_POLNNO,DOLNO);
		            	   map.put(IConstants.ORD_QTY,Double.toString(orderqty));
		 		        		            	  
		            	  //mine
		            	  if ((pickingQty ==balQty) && (pickingQty >0)  ){   
		            	    	map.put(IConstants.QTY, Double.toString((pickingQty)));
		            	    	sumqty= pickedqty + pickingQty;
		            	    	pickingQty = 0;
		            	    
		            	    	
		 		          }else if((pickingQty < balQty) && (pickingQty >0)  ){   
			 		           	 map.put(IConstants.QTY, Double.toString((pickingQty)));
			 		             sumqty= pickedqty + pickingQty;
				 		         pickingQty = 0;
				 		      
				 		   }
		            	  else if((pickingQty > balQty) && (pickingQty >0)  ){   
		 		                  	
		 		        	  map.put(IConstants.QTY, Double.toString((balQty)));
		 		        	  pickingQty = pickingQty-balQty;
		 		        	  sumqty= pickedqty+balQty;
		 		           	  insertFlag=false;
		 		          }
		            		 		          	 		        
		     //process dodet qty  start end
		 		          
		 	Hashtable htCondiPoDet = new Hashtable();
			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("dono", map.get("PONO"));
			htCondiPoDet.put("dolnno", DOLNO);	  
			htCondiPoDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htCondiPoDet.put(IDBConstants.UNITMO, map.get(IConstants.UNITMO));
			String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + "+ map.get(IConstants.QTY);
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
					 
			if( insertFlag==false){
				
				
			if (orderqty == sumqty) {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " ,qtyis= isNull(qtyis,0) + " + map.get(IConstants.QTY) + ", PickStatus='C',Lnstat='C' ";
							
							
			} else {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " ,qtyis= isNull(qtyis,0) + " + map.get(IConstants.QTY) + ", PickStatus='O',Lnstat='O' ";
				
			}
			 
		    	flag = _DoDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			}
			
			if (flag ==true && insertFlag==false) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("dono", map.get("PONO"));
								
              /*  if((i+1)==alResult.size())
                {
					flag = _DoDetDAO.isExisit(htCondiPoHdr,
							"pickStatus in ('O','N')");
					if (flag)
						queryPoHdr = "set  STATUS='O',PickStaus='O' ";
					else
						queryPoHdr = "set PickStaus='C' , STATUS='C'"; //, STATUS='C'
	
					flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
                }*/
				
				/*if (flag) {
					
					 dotransferdetflag=_DoTransferDetDAO.update(queryPoDet, htCondiPoDet, "");
					
					htCondiPoHdr.clear();
					htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
					htCondiPoHdr.put("dono", map.get(IConstants.PODET_PONUM));
					flag = _DoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N') ");
	          
					if (flag)
						queryPoHdr = "set STATUS='O' ";
					else
						queryPoHdr = "set STATUS='C' ";
								
					flag=_DoTransferHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				}*/
				
				   String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));
			        if(!nonstocktype.equals("Y"))	
				    {
						InvMstDAO _InvMstDAO = new InvMstDAO();
						_InvMstDAO.setmLogger(mLogger);
						Hashtable htInvMstExpDate = new Hashtable();
						htInvMstExpDate.clear();
						htInvMstExpDate.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htInvMstExpDate.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
						htInvMstExpDate.put(IDBConstants.LOC, map.get(IConstants.LOC));
						htInvMstExpDate.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
						ExpiryDate= _InvMstDAO.getInvExpireDate( (String)map.get(IConstants.PLANT), 
								(String)map.get(IConstants.ITEM),(String) map.get(IConstants.LOC),
								(String)map.get(IConstants.BATCH));
				    }
				boolean pickdet = false;
				Hashtable htPickDet = new Hashtable();
				htPickDet.clear();
				htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htPickDet.put("DONO", map.get(IConstants.PODET_PONUM));
				htPickDet.put("DOLNO",  map.get(IConstants.PODET_POLNNO));
				htPickDet.put(IDBConstants.CUSTOMER_NAME, su.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htPickDet.put(IDBConstants.ITEM_DESC,su.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
				htPickDet.put("BATCH", map.get(IConstants.BATCH));
				htPickDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htPickDet.put("LOC1", map.get(IConstants.LOC));
				htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
				htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htPickDet.put("CONTAINER", map.get(IConstants.OUT_CONTAINER));
				htPickDet.put("REMARK", map.get(IConstants.OUT_REMARK1));
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
				htPickDet.put("STATUS", "C");
				htPickDet.put(IDBConstants.ISSUEDATE,  map.get(IConstants.ISSUEDATE));
				htPickDet.put(IDBConstants.INVOICENO,  map.get(IConstants.INVOICENO));
				htPickDet.put(IDBConstants.EMPNO,  map.get(IConstants.EMPNO));
				if(nonstocktype.equalsIgnoreCase("Y"))	
			    {
					htPickDet.put("PICKQTY", String.valueOf(map.get(IConstants.QTY)));
					pickdet = _DoDetDAO.insertPickDet(htPickDet);
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
					extCond = "QTY >= " + map.get(IConstants.QTY);
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
				
				htPickDet.put("PICKQTY",  String.valueOf(adjustedQuantity));				
				htPickDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
				pickdet = _DoDetDAO.insertPickDet(htPickDet);
				quantityToAdjust -= adjustedQuantity;
					}
				}
				if(pickingQty==0)
				{
					insertFlag=true;
				}
				}
			}// if flag end
		   }//alResult for loop end
					Hashtable htCondiPoHdr = new Hashtable();
					htCondiPoHdr.put("PLANT", map.get("PLANT"));
					htCondiPoHdr.put("dono", map.get("PONO"));
				  flag = _DoDetDAO.isExisit(htCondiPoHdr,
							"pickStatus in ('O','N')");
					if (flag)
						queryPoHdr = "set  STATUS='O',PickStaus='O' ";
					else
						queryPoHdr = "set PickStaus='C' , STATUS='C'"; //, STATUS='C'
	
					flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
		 }// alResut end
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Pick Qty Exceeded the Order Qty to Pick");
		}
		return flag;
	}
	public boolean processInvMst(Map map) throws Exception {
		boolean flag = false;
		boolean flag2=false;
		try {
			
			flag = processInvRemove(map);
			if (flag) {
				//flag = processInvAdd(map);
				flag2=processBOM(map);
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
			double UOMQTY=1;
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+map.get(IConstants.PLANT)+"_UOM where UOM='"+String.valueOf(map.get(IConstants.UNITMO))+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=Double.valueOf((String)mapval.get("UOMQTY"));
			}
			double invumqty = Double.valueOf((String)map.get(IConstants.QTY)) * UOMQTY;
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}                       
			String extCond = "";
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				extCond = "QTY >= " + invumqty;
			}else{
				extCond = "QTY > 0";
			}
			flag = _InvMstDAO.isExisit(htInvMst, extCond);
			if (flag) {
//				Get details in ascending order of CRAT for that batch
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				}
				ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, extCond);
				if (!alStock.isEmpty()) {
					double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
					quantityToAdjust =invumqty;
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
				htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
				
				flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");
				
				if (!flag) {
					throw new Exception("Could not update");
				}
				quantityToAdjust -= adjustedQuantity;
					}
				}
					
			} else {
				throw new Exception("Product not found in the location");
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
		boolean bomupdateflag=false;
		String extcond="";
		
		ItemMstDAO _ItemMstDAO =new ItemMstDAO();
		BomDAO _BomDAO =new BomDAO() ;
		
		try
		{
		/*
					
		if(itemflag)
		{*/
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
 				sql1.append(" " +"STATUS" + " = 'C' ");
				sql1.append("," + IDBConstants.UPDATED_AT1 + " = '"
 						+ dateUtils.getDateTime() + "'");
				
				 bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst,"");
			    } /*else {
				throw new Exception("Unable to process kitting outbound picking location transfer, Location not found");
			}
		}
		else
		{
			return bomupdateflag ;
		}
		*/
		return bomupdateflag;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

	}
  /* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
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
			htRecvHis.put("DIRTYPE", TransactionConstants.ORD_PICK_ISSUE);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get("TRAN_QTY"));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
			htRecvHis.put("LNNO",  map.get(IConstants.PODET_POLNNO));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE,map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			
			String empno=(String)map.get(IConstants.EMPNO);
			String empname=employeedao.getEmpname((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EMPNO),"");
			if(empno.length()>0) {
				//htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.OUT_REMARK1))+","+empno);
				htRecvHis.put(IDBConstants.REMARKS, "PDA:"+empname+","+su.InsertQuotes((String)map.get(IConstants.OUT_REMARK1)));
          	}
            else
     	     {
        	  //  htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.OUT_REMARK1)));
        	    htRecvHis.put(IDBConstants.REMARKS, "PDA:"+su.InsertQuotes((String)map.get(IConstants.OUT_REMARK1)));
     	     }
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
