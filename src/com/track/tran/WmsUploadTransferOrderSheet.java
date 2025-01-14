package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.TOUtil;
import com.track.db.util.TblControlUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsUploadTransferOrderSheet implements WmsTran, IMLogger {

	

	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	PrdClassUtil prdclsutil = null;
	PrdTypeUtil prdtypeutil = null;
	ItemSesBeanDAO itemsesdao = null;
            TOUtil toUtil = null; 

	public WmsUploadTransferOrderSheet() {
		itemdao = new ItemUtil();
		dateUtils = new DateUtils();
		prdclsutil = new PrdClassUtil();
		prdtypeutil = new PrdTypeUtil();
	         toUtil =  new TOUtil();
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
		MLogger.log(0, "2.insertitemsheet - Stage : 1");
		flag = processCountSheet(m);
		

		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			prdclsutil.setmLogger(mLogger);
			prdtypeutil.setmLogger(mLogger);
		        toUtil.setmLogger(mLogger);
		
			// check TONO that already exist on TOHDR table
			boolean isExistToNo = false;
			TOUtil toUtil = new TOUtil();
			isExistToNo = toUtil.isExistTONO((String)map.get(IConstants.TR_TONO), (String)map.get(IConstants.PLANT));
                        if(isExistToNo){
                                //Update TOHDR 
                                flag=this.processUpdateTohdr(map);
                        }else{
                          
                            flag=this.processToHdr(map);
                            new TblControlUtil().updateTblControlSeqNo((String)map.get(IConstants.PLANT),IConstants.TRANSFER,"T",(String)map.get(IConstants.TR_TONO));
                           
                        }
                        
                        if(flag){
                            flag=this.processMovHisTohdr(map);
                        }
                        
                        if(flag){
                            flag=this.processDeleteTodet(map);
                                 
                        }       
                        if(flag){
                            flag=this.processTodet(map);
                        }
                          if(flag){
                              flag=this.processMovHisTodet(map);
                          } 

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processMovHisTohdr(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		try {
			movHisDao.setmLogger(mLogger);
			Hashtable htmovhishdr = new Hashtable();
                        htmovhishdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                        htmovhishdr.put(IConstants.DIRTYPE, TransactionConstants.CREATE_TO);
                        htmovhishdr.put(IConstants.LNNO, (String)map.get(IConstants.TR_TOLNNO));
                        htmovhishdr.put(IConstants.ORDNUM, (String)map.get(IConstants.TR_TONO));
                        htmovhishdr.put(IConstants.ITEM, (String)map.get(IConstants.TR_ITEM));
                        htmovhishdr.put("CRBY", (String)map.get("LOGIN_USER"));
                        htmovhishdr.put("TRANDATE", dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
                        htmovhishdr.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
                        htmovhishdr.put("MOVTID", "");
                        htmovhishdr.put("RECID", "");
		    
		        flag = movHisDao.insertIntoMovHis(htmovhishdr);

		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
    public boolean processMovHisTodet(Map map) throws Exception {
            boolean flag = false;
            MovHisDAO movHisDao = new MovHisDAO();
            try {
                    movHisDao.setmLogger(mLogger);
                    Hashtable htmovhisdet = new Hashtable();
                    htmovhisdet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                    htmovhisdet.put(IConstants.DIRTYPE, TransactionConstants.TO_ADD_ITEM);
                    htmovhisdet.put(IConstants.LNNO,(String)map.get(IConstants.TR_TOLNNO));
                    htmovhisdet.put(IConstants.ORDNUM, (String)map.get(IConstants.TR_TONO));
                    htmovhisdet.put(IConstants.ITEM, (String)map.get(IConstants.TR_ITEM));
                    htmovhisdet.put(IConstants.QTY, (String)map.get(IConstants.TR_QTY));
                    htmovhisdet.put("CRBY", (String)map.get("LOGIN_USER"));
                    htmovhisdet.put("TRANDATE", dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
                    htmovhisdet.put("CRAT", DateUtils.getDateTime());
                    htmovhisdet.put("MOVTID", "");
                    htmovhisdet.put("RECID", "");
                
                    flag = movHisDao.insertIntoMovHis(htmovhisdet);
            

            } catch (Exception e) {
                    throw e;
            }
            return flag;
    }
    
    
    public boolean processToHdr(Map map) throws Exception {
            boolean flag = false;
           
            try {
                    
                Hashtable httohdr = new Hashtable();
                // data for TOHDR table
//                httohdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
//                httohdr.put(IConstants.TR_TONO, (String)map.get(IConstants.TR_TONO));
//                httohdr.put(IConstants.TR_FROM_LOC, (String)map.get(IConstants.TR_FROM_LOC));
//                httohdr.put(IConstants.TR_TO_LOC, (String)map.get(IConstants.TR_TO_LOC));
//                httohdr.put(IConstants.TR_REF_NO, (String)map.get(IConstants.TR_REF_NO));
//                httohdr.put(IConstants.TR_STATUS, "N");
//                httohdr.put(IConstants.TR_ASSIGNEE, (String)map.get(IConstants.TR_ASSIGNEE));
//                httohdr.put(IConstants.TR_ASSIGNEE_NAME, (String)map.get(IConstants.TR_ASSIGNEE_NAME));
//                httohdr.put(IConstants.TR_ASSIGNEE_PERSONINCHARGE, (String)map.get(IConstants.TR_ASSIGNEE_PERSONINCHARGE));
//                httohdr.put(IConstants.TR_ASSIGNEE_ADD1, (String)map.get(IConstants.TR_ASSIGNEE_ADD1));
//                httohdr.put(IConstants.TR_ASSIGNEE_ADD2, (String)map.get(IConstants.TR_ASSIGNEE_ADD2));
//                httohdr.put(IConstants.TR_ASSIGNEE_ADD3, (String)map.get(IConstants.TR_ASSIGNEE_ADD3));
//                httohdr.put(IConstants.TR_ORDER_DATE, (String)map.get(IConstants.TR_ORDER_DATE));
//                httohdr.put(IConstants.TR_ORDER_TIME, (String)map.get(IConstants.TR_ORDER_TIME));
//                httohdr.put(IConstants.TR_REMARK1, (String)map.get(IConstants.TR_REMARK1));
//                httohdr.put(IConstants.TR_REMARK2, (String)map.get(IConstants.TR_REMARK2));
              httohdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
              httohdr.put(IConstants.TR_TONO, (String)map.get(IConstants.TR_TONO));
              httohdr.put(IConstants.TR_STATUS, "N");
              httohdr.put(IConstants.TR_FROM_LOC, (String)map.get(IConstants.TR_FROM_LOC));
              httohdr.put(IConstants.TR_TO_LOC, (String)map.get(IConstants.TR_TO_LOC));
              httohdr.put(IConstants.TR_REF_NO, (String)map.get(IConstants.TR_REF_NO));
              httohdr.put(IConstants.TR_ORDERTYPE, (String)map.get(IConstants.TR_ORDERTYPE));
              httohdr.put(IConstants.TR_ORDER_DATE, (String)map.get(IConstants.TR_ORDER_DATE));
              httohdr.put(IConstants.TR_ORDER_TIME, (String)map.get(IConstants.TR_ORDER_TIME));
              
              httohdr.put(IConstants.LOANHDR_ADDRESS, (String)map.get(IConstants.LOANHDR_ADDRESS));
              httohdr.put(IConstants.LOANHDR_ADDRESS2, (String)map.get(IConstants.LOANHDR_ADDRESS2));
              httohdr.put(IConstants.LOANHDR_ADDRESS3, (String)map.get(IConstants.LOANHDR_ADDRESS3));
              httohdr.put(IConstants.LOANHDR_CONTACT_NUM, (String)map.get(IConstants.LOANHDR_CONTACT_NUM));
              
              httohdr.put(IConstants.TR_ASSIGNEE, (String)map.get(IConstants.TR_ASSIGNEE));
              httohdr.put(IConstants.TR_ASSIGNEE_NAME, (String)map.get(IConstants.TR_ASSIGNEE_NAME));
              httohdr.put(IConstants.TR_ASSIGNEE_PERSONINCHARGE, (String)map.get(IConstants.TR_ASSIGNEE_PERSONINCHARGE));
              httohdr.put(IConstants.TR_REMARK1, (String)map.get(IConstants.TR_REMARK1));
              httohdr.put(IConstants.TR_REMARK2, (String)map.get(IConstants.TR_REMARK2));
              httohdr.put(IConstants.TR_REMARK3, (String)map.get(IConstants.TR_REMARK3));
              httohdr.put(IConstants.TR_CONSIGNMENT_GST, (String)map.get(IConstants.TR_CONSIGNMENT_GST));
              httohdr.put(IConstants.TOHDR_EMPNO, (String)map.get(IConstants.TOHDR_EMPNO));
              httohdr.put(IConstants.SALES_LOCATION, (String)map.get(IConstants.SALES_LOCATION));
              httohdr.put(IConstants.TR_CURRENCYID, (String)map.get(IConstants.TR_CURRENCYID));
              httohdr.put(IConstants.SHIPPINGCUSTOMER, (String)map.get(IConstants.SHIPPINGCUSTOMER));
//              httohdr.put(IConstants.ORDERDISCOUNT, (String)map.get(IConstants.ORDERDISCOUNT));
              httohdr.put(IConstants.DELIVERYDATE, (String)map.get(IConstants.DELIVERYDATE));
              httohdr.put(IConstants.SHIPPINGCOST, (String)map.get(IConstants.SHIPPINGCOST));
  			  httohdr.put(IConstants.PAYMENTTYPE, (String)map.get(IConstants.PAYMENTTYPE));
  			  httohdr.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String)map.get(IConstants.POHDR_DELIVERYDATEFORMAT));
  			  httohdr.put(IConstants.TAXTREATMENT, (String)map.get(IConstants.TAXTREATMENT));
  			  httohdr.put(IConstants.SALES_LOCATION, (String)map.get(IConstants.SALES_LOCATION));
              httohdr.put(IConstants.INCOTERMS, (String)map.get(IConstants.INCOTERMS));
                
        	  httohdr.put("DELDATE", (String)map.get(IConstants.TR_ORDER_DATE));
			  httohdr.put("PickStaus", "N");
			  httohdr.put("SHIPPINGID", (String)map.get("SHIPPINGID"));
			  httohdr.put("ORDER_STATUS", "Open");
			  
			  httohdr.put(IConstants.SHIP_CONTACTNAME, (String)map.get(IConstants.SHIP_CONTACTNAME));
	          httohdr.put(IConstants.SHIP_DESGINATION, (String)map.get(IConstants.SHIP_DESGINATION));
	          httohdr.put(IConstants.SHIP_WORKPHONE, (String)map.get(IConstants.SHIP_WORKPHONE));
	          httohdr.put(IConstants.SHIP_HPNO, (String)map.get(IConstants.SHIP_HPNO));
	          httohdr.put(IConstants.SHIP_EMAIL, (String)map.get(IConstants.SHIP_EMAIL));
	          httohdr.put(IConstants.SHIP_COUNTRY, (String)map.get(IConstants.SHIP_COUNTRY));
	          httohdr.put(IConstants.SHIP_ADDR1, (String)map.get(IConstants.SHIP_ADDR1));
	          httohdr.put(IConstants.SHIP_ADDR2, (String)map.get(IConstants.SHIP_ADDR2));
	          httohdr.put(IConstants.SHIP_ADDR3, (String)map.get(IConstants.SHIP_ADDR3));
	          httohdr.put(IConstants.SHIP_ADDR4, (String)map.get(IConstants.SHIP_ADDR4));
	          httohdr.put(IConstants.SHIP_STATE, (String)map.get(IConstants.SHIP_STATE));
	          httohdr.put(IConstants.SHIP_ZIP, (String)map.get(IConstants.SHIP_ZIP));

			  httohdr.put("ADJUSTMENT", (String)map.get("ADJUSTMENT"));
			  httohdr.put("ITEM_RATES", (String)map.get("ISTAXINCLUSIVE"));
			  httohdr.put("PROJECTID", (String)map.get("PROJECTID"));
			  httohdr.put("CURRENCYUSEQT", (String)map.get("EQUIVALENTCURRENCY"));
			  httohdr.put("ORDERDISCOUNTTYPE", (String)map.get("ORDERDISCOUNTTYPE"));
			  httohdr.put("TAXID", (String)map.get("TAXID"));
//			  httohdr.put("ISDISCOUNTTAX", (String)map.get("ISDISCOUNTTAX"));
			  httohdr.put("ISORDERDISCOUNTTAX", (String)map.get("ISORDERDISCOUNTTAX"));
			  httohdr.put("ORDERDISCOUNT", (String)map.get("ORDERDISCOUNT"));
			  httohdr.put("ISSHIPPINGTAX", (String)map.get("ISSHIPPINGTAX"));
			  httohdr.put("CRAT", dateUtils.getDate());
			  httohdr.put("CRBY", (String)map.get("LOGIN_USER"));
                
                // data insert into DOHDR table
                flag = toUtil.saveToHdrDetails(httohdr);
                httohdr.remove(IDBConstants.ORDERTYPE);
                httohdr.remove(IDBConstants.ORDERDISCOUNT);
                httohdr.remove(IDBConstants.SHIPPINGCOST);
                httohdr.remove("ISSHIPPINGTAX");
                httohdr.remove("ISORDERDISCOUNTTAX");
                httohdr.remove("ISDISCOUNTTAX");
                httohdr.remove("CURRENCYUSEQT");
                httohdr.remove("ITEM_RATES");
                httohdr.remove("ORDERDISCOUNTTYPE");
                httohdr.remove("TAXID");
               
            

            } catch (Exception e) {
                    throw e;
            }
            return flag;
    }
    
    public boolean processTodet(Map map) throws Exception {
            boolean flag = false;
           
            try {
            	String nonstocktpid="";//added by Bruhan  
                Hashtable httodet = new Hashtable();
                httodet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                httodet.put(IConstants.TR_TONO, (String)map.get(IConstants.TR_TONO));
                httodet.put(IConstants.TR_TOLNNO, (String)map.get(IConstants.TR_TOLNNO));
                //added by Bruhan
                nonstocktpid = (String)map.get(IConstants.TR_NONSTKTYPEID);
                Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.OUT_ITEM));
                String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
                String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
              
                if(nonstocktype.equals("Y"))	
    		    {
                	  if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")){ 
                    	httodet.put(IConstants.TR_UNITCOST, "-"+(String)map.get(IConstants.TR_UNITCOST));
                    }else{
                    	httodet.put(IConstants.TR_UNITCOST, (String)map.get(IConstants.TR_UNITCOST));
                    }
    		    }
                else
                {
                	httodet.put(IConstants.TR_UNITCOST, (String)map.get(IConstants.TR_UNITCOST));

                }
                //end
                httodet.put(IConstants.TR_LNSTAT, "N");
                httodet.put(IConstants.TR_PICK_STAT, "N");
                httodet.put(IConstants.TR_ITEM, (String)map.get(IConstants.TR_ITEM));
                httodet.put(IConstants.TR_ITEM_DESC, (String)map.get(IConstants.TR_ITEM_DESC));
                httodet.put(IConstants.TR_TRANDATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
                httodet.put(IConstants.TR_QTY, (String)map.get(IConstants.TR_QTY));
                httodet.put(IConstants.TR_RECVQTY, "0");
                httodet.put(IConstants.TR_ITEM_UOM, (String)map.get(IConstants.TR_ITEM_UOM));
                httodet.put(IConstants.TR_ITEM_DET_DESC, (String)map.get(IConstants.TR_ITEM_DESC));
                httodet.put(IConstants.TR_ASSIGNEE_DESC, (String)map.get(IConstants.TR_ASSIGNEE_NAME));
                httodet.put(IConstants.PRODGST, (String)map.get(IConstants.PRODGST));
                
                String CURRENCYUSEQT = new TOUtil().getCurrencyUseQT((String)map.get(IConstants.PLANT), (String)map.get(IConstants.TR_TONO));
                httodet.put("QTYRC", "0.0");
                httodet.put("QtyPick", "0.0");
                httodet.put("PRODUCTDELIVERYDATE", (String)map.get("PRODUCTDELIVERYDATE"));
                httodet.put("PRODGST", (String)map.get("PRODGST"));
                httodet.put("ACCOUNT_NAME", (String)map.get("ACCOUNT"));
                httodet.put("TAX_TYPE", (String)map.get("TAX"));
                httodet.put("DISCOUNT", (String)map.get("PRODUCTDISCOUNT"));
                httodet.put("DISCOUNT_TYPE", (String)map.get("PRODUCTDISCOUNT_TYPE"));
                httodet.put("CRAT", dateUtils.getDate());
                httodet.put("CRBY", (String)map.get("LOGIN_USER"));
                httodet.put(IDBConstants.CURRENCYUSEQT, (String)map.get("EQUIVALENTCURRENCY"));
               flag = toUtil.saveToDetDetails(httodet);
               
            

            } catch (Exception e) {
                    throw e;
            }
            return flag;
    }
    
    
    public boolean processUpdateTohdr(Map map) throws Exception {
        boolean flag = false;
       
        try {
             
            ToHdrDAO _toHdrDAO =new ToHdrDAO();
            _toHdrDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.TR_TONO, (String)map.get(IConstants.TR_TONO));
            
          
            StringBuffer updateQyery = new StringBuffer("set ");
//            updateQyery.append(IConstants.TR_FROM_LOC + " = '" + (String)map.get(IConstants.TR_FROM_LOC) + "'");
//            updateQyery.append("," + IConstants.TR_TO_LOC + " = '" +  (String)map.get(IConstants.TR_TO_LOC) + "'");
//            updateQyery.append("," + IConstants.TR_REF_NO + " = '" +  (String)map.get(IConstants.TR_REF_NO) + "'");
//            updateQyery.append("," + IConstants.TR_ORDER_DATE + " = '"+ (String)map.get(IConstants.TR_ORDER_DATE) + "'");
//            updateQyery.append("," + IConstants.TR_ORDER_TIME + " = '"+ (String)map.get(IConstants.TR_ORDER_TIME) + "'");
//            updateQyery.append("," + IConstants.TR_REMARK1 + " = '"+ (String)map.get(IConstants.TR_REMARK1) + "'");
//            updateQyery.append("," + IConstants.TR_REMARK2 + " = '"+ (String)map.get(IConstants.TR_REMARK2) + "'");
//            updateQyery.append(","+ IConstants.TR_ASSIGNEE + " = '" + (String)map.get(IConstants.TR_ASSIGNEE) + "'");
//            updateQyery.append("," +IConstants.TR_ASSIGNEE_NAME + " = '" + (String)map.get(IConstants.TR_ASSIGNEE_NAME) + "'");
//            updateQyery.append("," + IConstants.TR_ASSIGNEE_ADD1 + " = '"+ (String)map.get(IConstants.TR_ASSIGNEE_ADD1) + "'");
//            updateQyery.append(","+ IConstants.TR_ASSIGNEE_ADD2 + " = '" + (String)map.get(IConstants.TR_ASSIGNEE_ADD2) + "'");
//            updateQyery.append("," +IConstants.TR_ASSIGNEE_ADD3 + " = '" + (String)map.get(IConstants.TR_ASSIGNEE_ADD3) + "'");
//            updateQyery.append("," + IConstants.TR_ASSIGNEE_PERSONINCHARGE + " = '"+ (String)map.get(IConstants.TR_ASSIGNEE_PERSONINCHARGE) + "'");
            
            updateQyery.append(IConstants.TR_FROM_LOC + " = '" + (String)map.get(IConstants.TR_FROM_LOC) + "'");
            updateQyery.append("," + IConstants.TR_TO_LOC + " = '" +  (String)map.get(IConstants.TR_TO_LOC) + "'");
            updateQyery.append("," + IConstants.TR_ORDER_DATE + " = '"+ (String)map.get(IConstants.TR_ORDER_DATE) + "'");
            updateQyery.append("," + IConstants.TR_ORDER_TIME + " = '"+ (String)map.get(IConstants.TR_ORDER_TIME) + "'");
            updateQyery.append("," + IConstants.TR_REMARK1 + " = '"+ (String)map.get(IConstants.TR_REMARK1) + "'");
            updateQyery.append("," + IConstants.TR_REMARK2 + " = '"+ (String)map.get(IConstants.TR_REMARK2) + "'");
            updateQyery.append("," + IConstants.TR_CONSIGNMENT_GST + " = '"+ (String)map.get(IConstants.TR_CONSIGNMENT_GST) + "'");
            updateQyery.append(","+ IConstants.TR_ASSIGNEE + " = '" + (String)map.get(IConstants.TR_ASSIGNEE) + "'");
            updateQyery.append("," +IConstants.TR_ASSIGNEE_NAME + " = '" + (String)map.get(IConstants.IN_CUST_NAME) + "'");
            updateQyery.append("," + IConstants.TR_ASSIGNEE_PERSONINCHARGE + " = '"+ (String)map.get(IConstants.TR_ASSIGNEE_PERSONINCHARGE) + "'");
            updateQyery.append("," + IConstants.DOHDR_EMPNO + " = '"+ (String)map.get(IConstants.DOHDR_EMPNO) + "'");
            updateQyery.append("," + IConstants.DELIVERYDATE + " = '"+ (String)map.get(IConstants.DELIVERYDATE) + "'");
            updateQyery.append("," +IConstants.ORDERDISCOUNT + " = '" + (String)map.get(IConstants.ORDERDISCOUNT) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCOST + " = '"+ (String)map.get(IConstants.SHIPPINGCOST) + "'");
            updateQyery.append("," + IConstants.INCOTERMS + " = '"+ (String)map.get(IConstants.INCOTERMS) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCUSTOMER + " = '"+ (String)map.get(IConstants.SHIPPINGCUSTOMER) + "'");
            updateQyery.append("," + IConstants.PAYMENTTYPE + " = '"+ (String)map.get(IConstants.PAYMENTTYPE) + "'");
			updateQyery.append("," + IConstants.POHDR_DELIVERYDATEFORMAT + " = '"+ (String)map.get(IConstants.POHDR_DELIVERYDATEFORMAT) + "'");
			updateQyery.append("," + IConstants.TAXTREATMENT + " = '"+ (String)map.get(IConstants.TAXTREATMENT) + "'");
			updateQyery.append("," + IConstants.SALES_LOCATION + " = '"+ (String)map.get(IConstants.SALES_LOCATION) + "'");
			
			updateQyery.append(",SHIPPINGID = '"+ (String)map.get("SHIPPINGID")+ "'");
			updateQyery.append(",DISCOUNT = '"+ (String)map.get("DISCOUNT")+ "'");
			updateQyery.append(",DISCOUNT_TYPE = '"+ (String)map.get("DISCOUNTTYPE")+ "'");
			updateQyery.append(",ADJUSTMENT = '"+ (String)map.get("ADJUSTMENT")+ "'");
			updateQyery.append(",PROJECTID = '"+ (String)map.get("PROJECTID")+ "'");
          

            
            flag = _toHdrDAO.update(updateQyery.toString(), htCond, " AND STATUS ='N' ");
            if(!flag){
	            	updateQyery.append("," + IConstants.TR_CURRENCYID + " = '"+ (String)map.get(IConstants.TR_CURRENCYID) + "'");
	                updateQyery.append("," + IConstants.TR_ORDERTYPE + " = '" +  (String)map.get(IConstants.TR_ORDERTYPE) + "'");
	                updateQyery.append(",TAXID = '"+ (String)map.get("TAXID")+ "'");
	      			updateQyery.append(",ISDISCOUNTTAX = '"+ (String)map.get("ISDISCOUNTTAX")+ "'");
	      			updateQyery.append(",ISORDERDISCOUNTTAX = '"+ (String)map.get("ISORDERDISCOUNTTAX")+ "'");
	      			updateQyery.append(",ISSHIPPINGTAX = '"+ (String)map.get("ISSHIPPINGTAX")+ "'");
	      			updateQyery.append(",CURRENCYUSEQT = '"+ (String)map.get("EQUIVALENTCURRENCY")+ "'");
	      			updateQyery.append(",ORDERDISCOUNTTYPE = '"+ (String)map.get("ORDERDISCOUNTTYPE")+ "'");
	      			updateQyery.append(",ITEM_RATES = '"+ (String)map.get("ISTAXINCLUSIVE")+ "'");
	      			updateQyery.append(",DELDATE = '"+ (String)map.get(IConstants.TR_ORDER_DATE)+ "'");
                  flag = _toHdrDAO.update(updateQyery.toString(), htCond, "  ");
               
            }
            
            if(!flag){
            	 throw new Exception("Consignment Order "+(String)map.get(IConstants.TR_TONO)+" is processed,Cannot Modify");
            }
            
         
        } catch (Exception e) {
                throw e;
        }
        return flag;
    }
  

    public boolean processDeleteTodet(Map map) throws Exception {
        boolean flag = false;
       
        try {
            Hashtable httodet = new Hashtable();
            httodet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            httodet.put(IConstants.TR_TONO, (String)map.get(IConstants.TR_TONO));
            httodet.put(IConstants.TR_TOLNNO, (String)map.get(IConstants.TR_TOLNNO));
            
            boolean isLineExist = toUtil.isExitsToLine(httodet);
            if(isLineExist){
                ToDetDAO _todetDAO = new ToDetDAO();
               _todetDAO.setmLogger(mLogger);
               flag  =_todetDAO.delete(httodet);
            }else{
                flag=true; 
            }
            
         
        } catch (Exception e) {
                throw e;
        }
        return flag;
    }

}
