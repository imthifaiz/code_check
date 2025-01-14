package com.track.db.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderPaymentDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.PrdClassDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.tran.WmsPayment;
import com.track.tran.WmsPickIssue;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class OrderPaymentUtil  {
	private MLogger mLogger = new MLogger();
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	 public ArrayList getOBOrderPaymentDetails(String plant,String ordNo,String custName,String refNo)
				throws Exception {
		 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
			String sql = "";
			ArrayList al = null;
			try {
				Hashtable htCond = new Hashtable();
				String searchCond ="";
				if(ordNo.length()>0){
					searchCond = " AND DONO ='"+ordNo+"'";
					}
		            if(refNo.length()>0){
		            	searchCond = searchCond + " AND jobNum ='"+refNo+"'";
					}
		           
		            if(custName.length()>0){
		            	searchCond =searchCond + " AND (   CUSTNAME ='"+custName+"' or CustCode = '"+custName+"') ";
		            }
		           String extraCond ="A where orderAmt>0 and isnull(PAYMENT_STATUS,'')<>'C'";
		          // String extraCond ="A where orderAmt>0 and (orderAmt*Tax)-amtReceived >0 and isnull(PAYMENT_STATUS,'')<>'C'";
   	             //htCond.put("PLANT", plant);
				sql = ""+"SELECT orderNo,refNo,ordDate,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt"
						+",amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt end-(amtReceived-returnamt) AS DueToPay FROM( "
						+"SELECT DONO AS orderNo,JOBNUM as refNo,COLLECTIONDATE as ordDate, "
						+"  ISNULL((SELECT SUM(QTYOR * UNITPRICE)  from ["+plant+"_DODET] where PLANT =["+plant+"_DOHDR].PLANT and dono =["+plant+"_DOHDR].dono ),0)   as orderAmt "
						+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_DOHDR].PLANT and PAYMENT_TYPE='CREDIT' and  ORDNO =["+plant+"_DOHDR].dono   ),0) as amtReceived "
						+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_DOHDR].PLANT and PAYMENT_TYPE='DEBIT' and  ORDNO =["+plant+"_DOHDR].dono   ),0) as returnamt "
						+ ", CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end AS TAX,PAYMENT_STATUS FROM ["+plant+"_DOHDR] WHERE PLANT='"+plant+"'" + searchCond +")" ;
				orderPayDao.setmLogger(mLogger);
				al = orderPayDao.selectForReport(sql, htCond, extraCond);
			} catch (Exception e) {
				this.mLogger.Exception(e.getMessage());
				throw e;
			}

			return al;
		}
	 public ArrayList getIBOrderPaymentDetails(String plant,String ordNo,String custName,String refNo)
				throws Exception {
		 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
			String sql = "";
			ArrayList al = null;
			try {
				Hashtable htCond = new Hashtable();
				String searchCond ="";
				if(ordNo.length()>0){
					searchCond = " AND PONO ='"+ordNo+"'";
					}
		            if(refNo.length()>0){
		            	searchCond = searchCond + " AND jobNum ='"+refNo+"'";
					}
		           
		            if(custName.length()>0){
		            	searchCond =searchCond + " AND (   CUSTNAME ='"+custName+"' or CustCode = '"+custName+"') ";
		            }
		           String extraCond ="A where orderAmt>0  and isnull(PAYMENT_STATUS,'')<>'C'";
		          // String extraCond ="A where orderAmt>0 and (orderAmt*Tax)-amtReceived >0 and isnull(PAYMENT_STATUS,'')<>'C'";
   	              //htCond.put("PLANT", plant);
				sql = ""+"SELECT orderNo,refNo,ordDate,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt"
						+",amtReceived,returnamt,case when TAX > 0 then orderAmt* TAX else orderAmt end-(amtReceived-returnamt) AS DueToPay FROM( "
						+"SELECT PONO AS orderNo,JOBNUM as refNo,COLLECTIONDATE as ordDate, "
						+"  ISNULL((SELECT SUM(QTYOR * UNITCOST)  from ["+plant+"_PODET] where PLANT =["+plant+"_POHDR].PLANT and  Pono =["+plant+"_POHDR].PONO ),0)   as orderAmt "
						+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_POHDR].PLANT and PAYMENT_TYPE='DEBIT' and ORDNO =["+plant+"_POHDR].PONO   ),0) as amtReceived "
						+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_POHDR].PLANT and PAYMENT_TYPE='CREDIT' and ORDNO =["+plant+"_POHDR].PONO   ),0) as returnamt "
						+ ", CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end AS TAX,PAYMENT_STATUS FROM ["+plant+"_POHDR] WHERE PLANT='"+plant+"'" + searchCond +")" ;
				orderPayDao.setmLogger(mLogger);
				al = orderPayDao.selectForReport(sql, htCond, extraCond);
			} catch (Exception e) {
				this.mLogger.Exception(e.getMessage());
				throw e;
			}

			return al;
		}
public boolean process_OrderPayment(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();

			ut.begin();
			flag = process_Wms_OrderPayment(obj);

			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			throw e;
		}

		return flag;
	}
	
public boolean  isExistsOrderPaymentDetails(String plant,String orderNo) throws Exception {
	boolean isExistsPaymentDetails=false;
	Hashtable<String,String> htValues = new Hashtable<String, String>();
	String tablename =plant+"_ORDER_PAYMENT_DET";
	boolean isTableexits = sqlBean.istableExists(tablename);
	if(isTableexits){
	htValues.put("ORDNO", orderNo);
	htValues.put("PLANT", plant);
	isExistsPaymentDetails  = new OrderPaymentDAO().isExisit(htValues," AND ISNULL(PAYMENT_TYPE,'')<>''");
	}
	return isExistsPaymentDetails;
}      

	private boolean process_Wms_OrderPayment(Map map) throws Exception {
		boolean flag = false;

		WmsTran tran = new WmsPayment();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}
	
	
public boolean insertpaymentMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		try {
				orderPayDao.setmLogger(mLogger);
				inserted = orderPayDao.insertpaymentMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

public boolean isExistspaymentID(Hashtable ht) throws Exception {

		boolean isExists = false;
		OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		orderPayDao.setmLogger(mLogger);
		try {
			isExists = orderPayDao.isExistpaymentMst(ht);

		} catch (Exception e) {

			throw e;
		}

		return isExists;
	}

public boolean updatepaymentId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		try {
			
				orderPayDao.setmLogger(mLogger);
				update = orderPayDao.updatepaymentMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

public boolean deletepaymentId(Hashtable ht) throws Exception {
		boolean deleted = false;
		OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		try {
			
				orderPayDao.setmLogger(mLogger);
				deleted = orderPayDao.deletepaymentId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}	

public ArrayList getPaymentIdList(String paymentId, String plant, String Cond) {

	ArrayList al = null;
	OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	orderPayDao.setmLogger(mLogger);
	try {
		al = orderPayDao.getPaymentIdDetails(paymentId, plant, Cond);

	} catch (Exception e) {

	}

	return al;
}

public ArrayList getOrderPaymentDetails(String plant,String afrmDate,String atoDate,String order,
		String ordNo,String custName,String refNo)
		throws Exception {
 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	String sql = "",subquery="";
	ArrayList al = null;
	try { 
		Hashtable htCond = new Hashtable();
		String searchCond ="",dtCondStr="",extraCond="";
		
		
		dtCondStr =    " AND ISNULL(payment_dt,'')<>'' AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date)";
		//extraCon=" order by CAST((SUBSTRING(a.CollectionDate, 7, 4) + SUBSTRING(a.CollectionDate, 4, 2) + SUBSTRING(a.CollectionDate, 1, 2)) AS date) ";

		if (afrmDate.length() > 0) {
			searchCond = searchCond + dtCondStr + "  >= '" 
					+ afrmDate
					+ "'  ";
			if (atoDate.length() > 0) {
				searchCond = searchCond +dtCondStr+ " <= '" 
				+ atoDate
				+ "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				searchCond = searchCond +dtCondStr+ " <= '" 
				+ atoDate
				+ "'  ";
			}
		}
		
		if(ordNo.length()>0){
			searchCond = " AND ORDNO ='"+ordNo+"'";
			}
        if(refNo.length()>0){
        		if(order.equalsIgnoreCase("inbound")){
        			searchCond = searchCond + " AND ordno in(select pono from ["+plant+"_POHDR] where   jobNum ='"+refNo+"')";
            	}
        		if(order.equalsIgnoreCase("outbound")){
        			searchCond = searchCond + " AND ordno in(select dono from ["+plant+"_DOHDR] where   jobNum ='"+refNo+"')";
            	}
			}
           
         if(custName.length()>0){
        	 if(order.equalsIgnoreCase("inbound")){
     			searchCond = searchCond + " AND ordno in(select pono from ["+plant+"_POHDR] where   custName ='"+custName+"')";
         	}
     		if(order.equalsIgnoreCase("outbound")){
     			searchCond = searchCond + " AND ordno in(select dono from ["+plant+"_DOHDR] where   custName ='"+custName+"')";
         	}
            	
            }
         if(order.equalsIgnoreCase("inbound")){
        	 subquery = ",ISNULL((select jobnum from ["+plant+"_POHDR]B where  B.PONO =A.ordno),'') as refNo"
        			 	+",ISNULL((select COLLECTIONDATE from ["+plant+"_POHDR]B where  B.PONO =A.ordno),'') as ordDate"
         			 	+",ISNULL((SELECT CASE WHEN Inbound_Gst>0 then SUM(QTYOR * UNITCOST)*(1+(Inbound_Gst/100))" 
        			 	+"else SUM(QTYOR * UNITCOST) end as orderAmt "
        			 	+" from ["+plant+"_PODET]B,["+plant+"_POHDR]C where  B.PONO=C.PONO and " 
        			 	+"B.PONO =A.ordno group by Inbound_Gst),0)   as orderAmt, "
        			 	+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='DEBIT' and  ORDNO =A.ORDNO   ),0) as amtReceived ," 
     			 		+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='CREDIT' and ORDNO =A.ORDNO   ),0) as returnamt " ;

         }
         else if(order.equalsIgnoreCase("outbound")){
        	 subquery = ",ISNULL((select jobnum from ["+plant+"_DOHDR]B where  B.DONO =A.ordno),'') as refNo"
        			 	+",ISNULL((select COLLECTIONDATE from ["+plant+"_DOHDR]B where  B.DONO =A.ordno),'') as ordDate"
        			 	+",ISNULL((SELECT CASE WHEN Outbound_Gst>0 then SUM(QTYOR * UNITPRICE)*(1+(Outbound_Gst/100))" 
        			 	+"else SUM(QTYOR * UNITPRICE) end as orderAmt "
        			 	+" from ["+plant+"_DODET]B,["+plant+"_DOHDR]C where  B.DONO=C.DONO and " 
        			 	+"B.DONO =A.ordno group by Outbound_Gst),0)   as orderAmt, "
        			 	+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='CREDIT' and  ORDNO =A.ORDNO   ),0) as amtReceived ," 
     			 		+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='DEBIT' and ORDNO =A.ORDNO   ),0) as returnamt " ;

         }
         else
         {
        	 subquery=",'' as refNo,'' as ordDate, 0 as orderAmt,0 as amtReceived,0 as returnamt";
         }
        	 
         sql =   "select id,ordno,ordername,isnull(ordertype,'')ordertype,amount_paid,payment_dt,"
				+"isnull(payment_mode,'')payment_mode,isnull(payment_refno,'')payment_refno,"
				+"isnull(payment_type,'')payment_type,isnull(payment_id,'')payment_id,isnull(payment_remarks,'')payment_remarks" + subquery
     			+ "  from ["+plant+"_ORDER_PAYMENT_DET]A WHERE PLANT='"+plant+"' and ORDERNAME='"+order+"' AND  ISNULL(PAYMENT_TYPE,'')<>'' " + searchCond ;
		
		al = orderPayDao.selectForReport(sql, htCond, extraCond);
	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}

	return al;
}

public boolean updatepayment(Hashtable ht) throws Exception {
	boolean flag = false;
	OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	Hashtable htCond = new Hashtable();
	htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
	htCond.put("ID", (String) ht.get("ID"));
	htCond.put("ORDNO", (String) ht.get("ORDNO"));
	try {

		StringBuffer updateQyery = new StringBuffer("set ");

		updateQyery.append("PAYMENT_DT = '"
				+ (String) ht.get("PAYMENT_DT") + "'");
		updateQyery.append(",PAYMENT_MODE = '"
				+ (String) ht.get("PAYMENT_MODE") + "'");
		updateQyery.append(",PAYMENT_TYPE = '"
				+ (String) ht.get("PAYMENT_TYPE") + "'");
		updateQyery.append(",ORDERTYPE = '"
				+ (String) ht.get("ORDERTYPE") + "'");
		updateQyery.append(",PAYMENT_ID = '"
				+ (String) ht.get("PAYMENT_ID") + "'");
		updateQyery.append(",PAYMENT_REFNO = '"
				+ (String) ht.get("PAYMENT_REFNO") + "'");
		updateQyery.append(",PAYMENT_REMARKS = '"
				+ (String) ht.get("PAYMENT_REMARKS") + "'");
		updateQyery	.append(",AMOUNT_PAID = '"
				+ (String) ht.get("AMOUNT_PAID") + "'");
		

		orderPayDao.setmLogger(mLogger);
		flag = orderPayDao.updatePaymentDetails(updateQyery.toString(), htCond,"");
		
		if(flag)
		{
			String ordername = (String)ht.get("ORDERNAME");
			String queryHdr="";
			if(ordername.equalsIgnoreCase("outbound")){
				DoHdrDAO _DoHdrDAO = new DoHdrDAO();
				_DoHdrDAO.setmLogger(mLogger);
				Hashtable<String,String> htCondiDoHdr = new Hashtable<String,String>();
				htCondiDoHdr.put("PLANT", (String)ht.get(IConstants.PLANT));
				htCondiDoHdr.put("Dono", (String)ht.get("ORDNO"));

				flag = _DoHdrDAO.isExisit(htCondiDoHdr,"");

				if (flag)
					queryHdr = "set  PAYMENT_STATUS = '"+(String)ht.get("PAYMENT_STATUS")+"' ";
				else
					throw new Exception ("Order No. "+ht.get("ORDNO")+" not found to process the paymnet");

				flag = _DoHdrDAO.update(queryHdr, htCondiDoHdr, "");
			}
			else if(ordername.equalsIgnoreCase("inbound")){
			
				PoHdrDAO _PoHdrDAO = new PoHdrDAO();
				_PoHdrDAO.setmLogger(mLogger);
				Hashtable<String,String> htCondiPoHdr = new Hashtable<String,String>();
				htCondiPoHdr.put("PLANT", (String)ht.get(IConstants.PLANT));
				htCondiPoHdr.put("pono", (String)ht.get("ORDNO"));

				flag = _PoHdrDAO.isExisit(htCondiPoHdr,"");

				if (flag)
					queryHdr = "set PAYMENT_STATUS = '"+(String)ht.get("PAYMENT_STATUS")+"' ";
				else
					throw new Exception ("Order No. "+ht.get("ORDNO")+" not found to process the paymnet");

				flag = _PoHdrDAO.updatePO(queryHdr, htCondiPoHdr, "");
			}
			else{
				flag = true;
			}
				
		}
		


	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}
	return flag;
}

public boolean deletepaymentLineDetails(Hashtable ht) throws Exception {
	boolean flag = false;

	try {
		OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
		Hashtable htpmt = new Hashtable();
		orderPayDao.setmLogger(mLogger);

		htpmt.put("PLANT", ht.get(IConstants.PLANT));
		htpmt.put("ID", ht.get("ID"));
		htpmt.put("ORDERNAME", ht.get("ORDERNAME"));
		htpmt.put("ORDNO", ht.get("ORDNO"));

		flag = orderPayDao.deletepaymentdetails(htpmt);
		
		if(flag)
		{
			String ordername = (String)ht.get("ORDERNAME");
			String queryHdr="";
			if(ordername.equalsIgnoreCase("outbound")){
				DoHdrDAO _DoHdrDAO = new DoHdrDAO();
				_DoHdrDAO.setmLogger(mLogger);
				Hashtable<String,String> htCondiDoHdr = new Hashtable<String,String>();
				htCondiDoHdr.put("PLANT", (String)ht.get(IConstants.PLANT));
				htCondiDoHdr.put("Dono", (String)ht.get("ORDNO"));

				flag = _DoHdrDAO.isExisit(htCondiDoHdr,"");

				if (flag)
					queryHdr = "set  PAYMENT_STATUS = 'O' ";
				else
					throw new Exception ("Order No. "+ht.get("ORDNO")+" not found to process the paymnet");

				flag = _DoHdrDAO.update(queryHdr, htCondiDoHdr, "");
			}
			else if(ordername.equalsIgnoreCase("inbound")){
			
				PoHdrDAO _PoHdrDAO = new PoHdrDAO();
				_PoHdrDAO.setmLogger(mLogger);
				Hashtable<String,String> htCondiPoHdr = new Hashtable<String,String>();
				htCondiPoHdr.put("PLANT", (String)ht.get(IConstants.PLANT));
				htCondiPoHdr.put("pono", (String)ht.get("ORDNO"));

				flag = _PoHdrDAO.isExisit(htCondiPoHdr,"");

				if (flag)
					queryHdr = "set PAYMENT_STATUS = 'O' ";
				else
					throw new Exception ("Order No. "+ht.get("ORDNO")+" not found to process the paymnet");

				flag = _PoHdrDAO.updatePO(queryHdr, htCondiPoHdr, "");
			}
			else{
				flag = true;
			}
		}
			
		if (flag) {
			
			MovHisDAO movhisDao = new MovHisDAO();
			Hashtable htmovHis = new Hashtable();
			String ORDER = (String)ht.get("ORDERNAME");
			movhisDao.setmLogger(mLogger);
			htmovHis.clear();
			htmovHis.put(IDBConstants.PLANT, (String) ht
					.get(IDBConstants.PLANT));
			htmovHis.put("DIRTYPE", TransactionConstants.DELETE_PAYMENT);
			htmovHis.put(IDBConstants.ITEM, "");
			if(ORDER.equalsIgnoreCase("OTHERS"))
			{
				htmovHis.put(IDBConstants.MOVHIS_ORDNUM, "OTHERS");
			}
			else
			{	
				htmovHis.put(IDBConstants.MOVHIS_ORDNUM, ht.get("ORDNO"));
			}
			
			htmovHis.put(IDBConstants.CREATED_BY, (String) ht.get(IDBConstants.CREATED_BY));
			htmovHis.put("MOVTID", "");
			htmovHis.put("RECID", "");
			htmovHis.put(IDBConstants.TRAN_DATE, new DateUtils()
					.getDateinyyyy_mm_dd(new DateUtils().getDate()));
			htmovHis.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());

			flag = movhisDao.insertIntoMovHis(htmovHis);
		}

	} catch (Exception e) {
		throw e;
	}

	return flag;
}

public ArrayList getPaymentSummaryDetails(Hashtable ht,String afrmDate,String atoDate,String plant,String order,String custName)
		throws Exception {
 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	String sql = "",subquery="";
	ArrayList al = null;
	try { 
		Hashtable htCond = new Hashtable();
		String searchCond ="",dtCondStr="",extraCond="";
		
		
		dtCondStr =    " AND ISNULL(payment_dt,'')<>'' AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date)";
		extraCond=" order by ORDNO,CAST((SUBSTRING(payment_dt, 7, 4) + SUBSTRING(payment_dt, 4, 2) + SUBSTRING(payment_dt, 1, 2)) AS date) ";

		if (afrmDate.length() > 0) {
			searchCond = searchCond + dtCondStr + "  >= '" 
					+ afrmDate
					+ "'  ";
			if (atoDate.length() > 0) {
				searchCond = searchCond +dtCondStr+ " <= '" 
				+ atoDate
				+ "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				searchCond = searchCond +dtCondStr+ " <= '" 
				+ atoDate
				+ "'  ";
			}
		}
		 
		if(custName.length()>0){
	     	 if(order.equalsIgnoreCase("inbound")){
		        		 searchCond = searchCond + " AND ordno in(select pono from ["+plant+"_POHDR] where   custName ='"+custName+"')";
		   	 }
	     	 if(order.equalsIgnoreCase("outbound")){
		        		 searchCond = searchCond + " AND ordno in(select dono from ["+plant+"_DOHDR] where   custName ='"+custName+"')";
	     	 }
            	
		}
	    if(order.equalsIgnoreCase("inbound")){
        	 subquery = ",ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='DEBIT' and  ORDNO =A.ORDNO   ),0) as amtReceived ," 
     			 		+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='CREDIT' and ORDNO =A.ORDNO   ),0) as returnamt ," 
        			 	+"(select CustName from ["+plant+"_pohdr]B where B.PONO =A.ordno) as custname,ISNULL((SELECT CASE WHEN Inbound_Gst>0 then SUM(QTYOR * UNITCOST)*(1+(Inbound_Gst/100))" 
        			 	+"else SUM(QTYOR * UNITCOST) end as orderAmt "
        			 	+" from ["+plant+"_PODET]B,["+plant+"_POHDR]C where  B.PONO=C.PONO and " 
        			 	+"B.PONO =A.ordno group by Inbound_Gst),0)   as orderAmt ";
         }
         else if(order.equalsIgnoreCase("outbound")){
        	 subquery = ",ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='CREDIT' and  ORDNO =A.ORDNO   ),0) as amtReceived ," 
        			 	+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where PAYMENT_TYPE='DEBIT' and ORDNO =A.ORDNO   ),0) as returnamt ," 
        			 	+"(select CustName from ["+plant+"_dohdr]B where B.DONO =A.ordno) as custname,ISNULL((SELECT CASE WHEN Outbound_Gst>0 then SUM(QTYOR * UNITPRICE)*(1+(Outbound_Gst/100))" 
        			 	+"else SUM(QTYOR * UNITPRICE) end as orderAmt "
        			 	+" from ["+plant+"_DODET]B,["+plant+"_DOHDR]C where  B.DONO=C.DONO and " 
        			 	+"B.DONO =A.ordno group by Outbound_Gst),0)   as orderAmt ";
         }
         else
         {
        	 subquery=",0 as amtReceived,0 as returnamt,'' as custname, 0 as orderAmt";
         }
        	 
         sql =   "select id,ordno,ordername,isnull(ordertype,'')ordertype,amount_paid,payment_dt,"
				+"isnull(payment_mode,'')payment_mode,isnull(payment_refno,'')payment_refno,"
				+"isnull(payment_type,'')payment_type,isnull(payment_id,'')payment_id,isnull(payment_remarks,'')payment_remarks" + subquery
     			+ "  from ["+plant+"_ORDER_PAYMENT_DET]A WHERE PLANT='"+plant+"' AND  ISNULL(payment_type,'')<>'' " + searchCond ;
		
		al = orderPayDao.selectForReport(sql, ht, extraCond);
	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}

	return al;
}

public Map getPaymentReceiptHdrDetails(String aplant) throws Exception {
    Map m =  new HashMap();
    OrderPaymentDAO  dao = new OrderPaymentDAO();
    m=dao.getPaymentReciptHeaderDetails(aplant);
     return m;
}

public ArrayList getPaymentStatementDetails(String plant,String afrmDate,String atoDate, String order,String custcode)
		throws Exception {
 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	String sql = "";
	ArrayList al = null;
	try {
		Hashtable htCond = new Hashtable();
		String searchCond ="",dtCondStr="",extraCon="";
		               
            if(custcode.length()>0){
            	searchCond =searchCond + " AND (   CustCode = '"+custcode+"') ";
            }
            
            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
       	   if (afrmDate.length() > 0) {
				 searchCond = searchCond + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
			
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			 } else {
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			 }   
       	 // extraCon = "ORDER BY CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
       	  extraCon = "ORDER BY rowid ";
  
           /* 
            dtCondStr = "  AND CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2)) AS date) ";
            dtCondpmt = "  AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date)";
            
            //extraCon=" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) ";
			 if (afrmDate.length() > 0) {
				 searchCondord = searchCond + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
				 searchCondpmt = searchCondpmt + dtCondpmt + "  >= '" 
							+ afrmDate
							+ "'  ";
				if (atoDate.length() > 0) {
					searchCondord = searchCondord +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
							+ atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					searchCondord = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
							+ atoDate
							+ "'  ";
				}
			}   
           //String extraCond ="A where orderAmt>0 ";
			 String extraCond="";*/
           
        if(order.equalsIgnoreCase("OUTBOUND"))
        {	
        	/*sql = "" +"SELECT orderNo,ordDate,custname,orderAmt,amtReceived,returnamt,DueToPay from("	
        				+"SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt,"
        				+"case when TAX > 0 then orderAmt* Tax else orderAmt end AS amtReceived,0 as returnamt, case when TAX > 0 then orderAmt* Tax else orderAmt end AS DueToPay FROM( "
   						+"SELECT DONO AS orderNo,COLLECTIONDATE as ordDate, "
   						+"  ISNULL((SELECT SUM(QTYOR * UNITPRICE)  from ["+plant+"_DODET] where PLANT =["+plant+"_DOHDR].PLANT and  dono =["+plant+"_DOHDR].dono ),0)   as orderAmt,custname, "
   						+ " CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_DOHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A" 
   						+" UNION  "
   						+" SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt, "
   						+" -amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt end-(amtReceived-returnamt) AS DueToPay "
   						+" FROM( "
   						+" SELECT (ORDNO+' - '+PAYMENT_REFNO) AS orderNo,PAYMENT_DT as ordDate, "
   						+" ISNULL((SELECT SUM(QTYOR * UNITPRICE)  from ["+plant+"_DODET] where PLANT =A.PLANT and dono =A.ORDNO),0)   as orderAmt, "
   						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and PAYMENT_TYPE='CREDIT' AND ORDNO = B.DONO   ),0)as amtReceived  , "
   						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT = A.PLANT  and PAYMENT_TYPE='DEBIT' and  ORDNO = B.DONO),0) as returnamt ,custname, "
   						+" CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end  AS TAX "
   						+" FROM ["+plant+"_ORDER_PAYMENT_DET]A,["+plant+"_DOHDR]B WHERE A.PLANT='"+plant+"' and PAYMENT_TYPE='CREDIT' AND"
   						+" a.ORDNO = b.DONO " +searchCond1+ searchCondpmt + ")A )PMT where orderAmt>0 order by custname,CAST((SUBSTRING(ordDate, 7, 4) + SUBSTRING(ordDate, 4, 2) + SUBSTRING(ordDate, 1, 2)) AS date)  ";*/
        	sql = "" +"SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDNO else ORDNO+'Pmtrefno'+PAYMENT_REFNO end[ORDNO],"
        			 +"PAYMENT_DT as TRANDATE,CustCode,"  
        			 +"case when ORDERNAME='OUTBOUND' and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " 
        			 +"end[AMOUNT], "
        			 +"BALANCE =  COALESCE("
        			 +"("
        			 +"SELECT SUM(AMOUNT_PAID)"
        			 +"FROM ["+plant+"_TEMP_ORDER_PAYMENT_DET]  AS I "
        			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
        			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
        			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +" 
        			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERNAME='OUTBOUND' " 
        			 +"and ISNULL(PAYMENT_MODE,'')=''), 0" 
        			 +" )"
        			 +"-"
        			 +" COALESCE("
        			 +"("
        			 +"SELECT SUM(AMOUNT_PAID)"
        			 +"FROM ["+plant+"_TEMP_ORDER_PAYMENT_DET]  AS I "
        			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
        			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
        			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + "
        			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERNAME='OUTBOUND' " 
        			 +"and ISNULL(PAYMENT_TYPE,'')='CREDIT' and "
        			 +"ISNULL(PAYMENT_MODE,'')<>''), 0"
        			 +")"
        			 +"FROM ["+plant+"_TEMP_ORDER_PAYMENT_DET]  AS PMT "
        			 +"where PLANT = '"+plant+"' AND "
        			 +"ORDERNAME='OUTBOUND'" +searchCond+extraCon;
        }else{
        	/*sql = "" +"SELECT orderNo,ordDate,custname,orderAmt,amtReceived,returnamt,DueToPay from("	
    				+"SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt,"
    				+"case when TAX > 0 then orderAmt* Tax else orderAmt end AS amtReceived,0 as returnamt, case when TAX > 0 then orderAmt* Tax else orderAmt end AS DueToPay FROM( "
						+"SELECT PONO AS orderNo,COLLECTIONDATE as ordDate, "
						+"  ISNULL((SELECT SUM(QTYOR * UNITCOST)  from ["+plant+"_PODET] where PLANT =["+plant+"_POHDR].PLANT and  pono =["+plant+"_POHDR].pono ),0)   as orderAmt,custname, "
						+ " CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_POHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A" 
						+" UNION  "
						+" SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt, "
						+" -amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt end-(amtReceived-returnamt) AS DueToPay "
						+" FROM( "
						+" SELECT (ORDNO+' - '+PAYMENT_REFNO) AS orderNo,PAYMENT_DT as ordDate, "
						+" ISNULL((SELECT SUM(QTYOR * UNITCOST)  from ["+plant+"_PODET] where PLANT =A.PLANT and pono =A.ORDNO),0)   as orderAmt, "
						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and PAYMENT_TYPE='DEBIT' AND ORDNO = B.PONO   ),0)as amtReceived  , "
						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT = A.PLANT  and PAYMENT_TYPE='CREDIT' and  ORDNO = B.PONO),0) as returnamt ,custname, "
						+" CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end  AS TAX "
						+" FROM ["+plant+"_ORDER_PAYMENT_DET]A,["+plant+"_POHDR]B WHERE A.PLANT='"+plant+"' and PAYMENT_TYPE='DEBIT' AND"
						+" a.ORDNO = b.PONO " +searchCond1+ searchCondpmt + ")A )PMT where orderAmt>0 order by custname,CAST((SUBSTRING(ordDate, 7, 4) + SUBSTRING(ordDate, 4, 2) + SUBSTRING(ordDate, 1, 2)) AS date) ";*/
        	sql = "" +"SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDNO else ORDNO+'Pmtrefno'+PAYMENT_REFNO end[ORDNO],"
       			 +"PAYMENT_DT as TRANDATE,CustCode,"  
       			 +"case when ORDERNAME='INBOUND' and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " 
       			 +"end[AMOUNT], "
       			 +"BALANCE =  COALESCE("
       			 +"("
       			 +"SELECT SUM(AMOUNT_PAID)"
       			 +"FROM ["+plant+"_TEMP_ORDER_PAYMENT_DET]  AS I "
       			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
       			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
       			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +" 
       			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERNAME='INBOUND' " 
       			 +"and ISNULL(PAYMENT_MODE,'')=''), 0" 
       			 +" )"
       			 +"-"
       			 +" COALESCE("
       			 +"("
       			 +"SELECT SUM(AMOUNT_PAID)"
       			 +"FROM ["+plant+"_TEMP_ORDER_PAYMENT_DET]  AS I "
       			 +"WHERE I.ROWID <= PMT.ROWID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
       			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
       			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + "
       			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) "+searchCond+" and PMT.CustCode=I.CustCode and ORDERNAME='INBOUND' " 
       			 +"and ISNULL(PAYMENT_TYPE,'')='DEBIT'   and  "
       			 +"ISNULL(PAYMENT_MODE,'')<>''), 0"
       			 +")"
       			 +"FROM ["+plant+"_TEMP_ORDER_PAYMENT_DET]  AS PMT "
       			 +"where PLANT = '"+plant+"' AND "
       			 +"ORDERNAME='INBOUND'" +searchCond+extraCon;
        }
		orderPayDao.setmLogger(mLogger);
		al = orderPayDao.selectForReport(sql, htCond, "");
	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}

	return al;
}

public ArrayList getAgeingDetails(String plant,String afrmDate,String atoDate, String order,String custName,String orderno)
		throws Exception {
 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	String sql = "";
	ArrayList al = null;
	try {
		Hashtable htCond = new Hashtable();
		String searchCond ="",searchCond1="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="";
		              
            if(custName.length()>0){
            	searchCond =searchCond + " AND (   CUSTNAME ='"+custName+"' or CustCode = '"+custName+"') ";
            }
            if(custName.length()>0){
            	
            	searchCond1 =searchCond1 + " AND (   B.CUSTNAME ='"+custName+"' or B.CustCode = '"+custName+"') ";
            }
            
            dtCondStr = "  AND CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2)) AS date) ";
            dtCondpmt = "  AND CAST((SUBSTRING(payment_dt, 7, 4) + '-' + SUBSTRING(payment_dt, 4, 2) + '-' + SUBSTRING(payment_dt, 1, 2)) AS date)";
            
           // extraCon=" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) ";
            if (afrmDate.length() > 0) {
				 searchCondord = searchCond + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
				 searchCondpmt = searchCondpmt + dtCondpmt + "  >= '" 
							+ afrmDate
							+ "'  ";
				if (atoDate.length() > 0) {
					searchCondord = searchCondord +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
							+ atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					searchCondord = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					searchCondpmt = searchCondpmt +dtCondpmt+ " <= '" 
							+ atoDate
							+ "'  ";
				}
			}    
                        
			if(orderno.length()>0) 
			{
				if(order.equalsIgnoreCase("OUTBOUND")){
					searchCondord = searchCondord+" AND DONO='"+orderno+"' ";
					searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
				}
				else if(order.equalsIgnoreCase("INBOUND")){
					searchCondord = searchCondord+" AND PONO='"+orderno+"' ";
					searchCondpmt = searchCondpmt+" AND ORDNO='"+orderno+"' ";
				}
			}
			 
           String extraCond =" where orderAmt>0 ";
			 
        if(order.equalsIgnoreCase("OUTBOUND"))
        {	
        	sql = ""+"SELECT orderNo,ordDate,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt"
					+",amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt end-(amtReceived-returnamt) AS DueToPay FROM( "
					+"SELECT DONO AS orderNo,COLLECTIONDATE as ordDate, "
					+"  ISNULL((SELECT SUM(QTYOR * UNITPRICE)  from ["+plant+"_DODET] where PLANT =["+plant+"_DOHDR].PLANT and dono =["+plant+"_DOHDR].dono ),0)   as orderAmt "
					+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_DOHDR].PLANT and PAYMENT_TYPE='CREDIT' and  ORDNO =["+plant+"_DOHDR].dono "+searchCondpmt+"  ),0) as amtReceived "
					+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_DOHDR].PLANT and PAYMENT_TYPE='DEBIT' and  ORDNO =["+plant+"_DOHDR].dono " +searchCondpmt+"  ),0) as returnamt "
					+ ", CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_DOHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A" 
					+" UNION"
					+" SELECT orderNo,ordDate,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt,"
					+"amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt "
					+"end-(amtReceived-returnamt) AS DueToPay  FROM( "
					+"SELECT ORDNO AS orderNo, COLLECTIONDATE as ordDate,"
					+"ISNULL((SELECT SUM(QTYOR * UNITPRICE)  from ["+plant+"_DODET]"
					+" where PLANT =A.PLANT and dono =A.ORDNO),0)   as orderAmt,"
					+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and "
					+"PAYMENT_TYPE='CREDIT' AND ORDNO = B.DONO   ),0)as amtReceived  ,"
					+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT = A.PLANT  and "
					+"PAYMENT_TYPE='DEBIT' and  ORDNO = B.DONO),0) as returnamt ,"
					+"CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end  AS TAX  FROM "
					+"["+plant+"_ORDER_PAYMENT_DET]A,["+plant+"_DOHDR]B WHERE A.PLANT='"+plant+"' and "
					+"a.ORDNO = b.DONO " +searchCond1 + searchCondpmt +")A";
	        }else{
	        	sql = ""+"SELECT orderNo,ordDate,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt"
						+",amtReceived,returnamt,case when TAX > 0 then orderAmt* TAX else orderAmt end-(amtReceived-returnamt) AS DueToPay FROM( "
						+"SELECT PONO AS orderNo,COLLECTIONDATE as ordDate, "
						+"  ISNULL((SELECT SUM(QTYOR * UNITCOST)  from ["+plant+"_PODET] where PLANT =["+plant+"_POHDR].PLANT and  Pono =["+plant+"_POHDR].PONO ),0)   as orderAmt "
						+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_POHDR].PLANT and PAYMENT_TYPE='DEBIT' and ORDNO =["+plant+"_POHDR].PONO "+searchCondpmt+" ),0) as amtReceived "
						+" ,ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =["+plant+"_POHDR].PLANT and PAYMENT_TYPE='CREDIT' and ORDNO =["+plant+"_POHDR].PONO "+searchCondpmt+" ),0) as returnamt "
						+ ", CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_POHDR] WHERE PLANT='"+plant+"'" + searchCondord +") A"
						+" UNION"
						+" SELECT orderNo,ordDate,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt,"
						+"amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt "
						+"end-(amtReceived-returnamt) AS DueToPay  FROM( "
						+"SELECT ORDNO AS orderNo, COLLECTIONDATE as ordDate,"
						+"ISNULL((SELECT SUM(QTYOR * UNITCOST)  from ["+plant+"_PODET]"
						+" where PLANT =A.PLANT and pono =A.ORDNO),0)   as orderAmt,"
						+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and "
						+"PAYMENT_TYPE='DEBIT' AND ORDNO = B.PONO   ),0)as amtReceived  ,"
						+"ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT = A.PLANT  and "
						+"PAYMENT_TYPE='CREDIT' and  ORDNO = B.PONO),0) as returnamt ,"
						+"CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end  AS TAX  FROM "
						+"["+plant+"_ORDER_PAYMENT_DET]A,["+plant+"_POHDR]B WHERE A.PLANT='"+plant+"' and "
						+"a.ORDNO = b.PONO " + searchCond1 + searchCondpmt +")A";
	        }
		orderPayDao.setmLogger(mLogger);
		al = orderPayDao.selectForReport(sql, htCond, extraCond);
	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}

	return al;
}
public ArrayList getPaymentOrderStatementDetails(String plant,String afrmDate,String atoDate,String order,String orderno)
		throws Exception {
 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	String sql = "";
	ArrayList al = null;
	try {
		Hashtable htCond = new Hashtable();
		String extraCon="",dtCondStr="",searchCond="";
           
      /*  if(order.equalsIgnoreCase("OUTBOUND"))
        {	
        	sql = "" +"SELECT orderNo,ordDate,custname,orderAmt,amtReceived,returnamt,DueToPay from("	
        				+"SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt,"
        				+"case when TAX > 0 then orderAmt* Tax else orderAmt end as amtReceived,0 as returnamt, case when TAX > 0 then orderAmt* Tax else orderAmt end AS DueToPay FROM( "
   						+"SELECT DONO AS orderNo,COLLECTIONDATE as ordDate,custname, "
   						+"  ISNULL((SELECT SUM(QTYOR * UNITPRICE)  from ["+plant+"_DODET] where PLANT =["+plant+"_DOHDR].PLANT and  dono =["+plant+"_DOHDR].dono ),0)   as orderAmt, "
   						+ " CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_DOHDR] WHERE PLANT='"+plant+"' AND DONO='" + orderno +"' ) A" 
   						+" UNION  "
   						+" SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt, "
   						+" -amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt end-(amtReceived-returnamt) AS DueToPay "
   						+" FROM( "
   						+" SELECT (ORDNO+' - '+PAYMENT_REFNO) AS orderNo,PAYMENT_DT as ordDate, custname,"
   						+" ISNULL((SELECT SUM(QTYOR * UNITPRICE)  from ["+plant+"_DODET] where PLANT =A.PLANT and dono =A.ORDNO),0)   as orderAmt, "
   						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and PAYMENT_TYPE='CREDIT' AND ORDNO = B.DONO   ),0)as amtReceived  , "
   						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT = A.PLANT  and PAYMENT_TYPE='DEBIT' and  ORDNO = B.DONO),0) as returnamt , "
   						+" CASE WHEN Outbound_Gst>0 then 1+(Outbound_Gst/100) else 0 end  AS TAX "
   						+" FROM ["+plant+"_ORDER_PAYMENT_DET]A,["+plant+"_DOHDR]B WHERE A.PLANT='"+plant+"' and PAYMENT_TYPE='CREDIT' AND"
   						+" a.ORDNO = b.DONO AND  ORDNO='" + orderno + "' )A )PMT where orderAmt>0 order by CAST((SUBSTRING(ordDate, 7, 4) + SUBSTRING(ordDate, 4, 2) + SUBSTRING(ordDate, 1, 2)) AS date)  ";
        }else if(order.equalsIgnoreCase("INBOUND")){
        	sql = "" +"SELECT orderNo,ordDate,custname,orderAmt,amtReceived,returnamt,DueToPay from("	
    				+"SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt,"
    				+"case when TAX > 0 then orderAmt* Tax else orderAmt end as amtReceived,0 as returnamt, case when TAX > 0 then orderAmt* Tax else orderAmt end AS DueToPay FROM( "
						+"SELECT PONO AS orderNo,COLLECTIONDATE as ordDate,custname, "
						+"  ISNULL((SELECT SUM(QTYOR * UNITCOST)  from ["+plant+"_PODET] where PLANT =["+plant+"_POHDR].PLANT and  pono =["+plant+"_POHDR].pono ),0)   as orderAmt, "
						+ " CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end AS TAX FROM ["+plant+"_POHDR] WHERE PLANT='"+plant+"' AND PONO='" + orderno +"' ) A" 
						+" UNION  "
						+" SELECT orderNo,ordDate,custname,Tax,case when TAX > 0 then orderAmt* Tax else orderAmt end as orderAmt, "
						+" -amtReceived,returnamt,case when TAX > 0 then orderAmt* Tax else orderAmt end-(amtReceived-returnamt) AS DueToPay "
						+" FROM( "
						+" SELECT (ORDNO+' - '+PAYMENT_REFNO) AS orderNo,PAYMENT_DT as ordDate,custname, "
						+" ISNULL((SELECT SUM(QTYOR * UNITCOST)  from ["+plant+"_PODET] where PLANT =A.PLANT and pono =A.ORDNO),0)   as orderAmt, "
						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT =A.PLANT and PAYMENT_TYPE='DEBIT' AND ORDNO = B.PONO   ),0)as amtReceived  , "
						+" ISNULL((SELECT SUM(AMOUNT_PAID)  from ["+plant+"_ORDER_PAYMENT_DET] where  PLANT = A.PLANT  and PAYMENT_TYPE='CREDIT' and  ORDNO = B.PONO),0) as returnamt , "
						+" CASE WHEN Inbound_Gst>0 then 1+(Inbound_Gst/100) else 0 end  AS TAX "
						+" FROM ["+plant+"_ORDER_PAYMENT_DET]A,["+plant+"_POHDR]B WHERE A.PLANT='"+plant+"' and PAYMENT_TYPE='DEBIT' AND"
						+" a.ORDNO = b.PONO AND  ORDNO='" + orderno + "' )A )PMT where orderAmt>0 order by CAST((SUBSTRING(ordDate, 7, 4) + SUBSTRING(ordDate, 4, 2) + SUBSTRING(ordDate, 1, 2)) AS date) ";
        }*/
		
		dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
    	   if (afrmDate.length() > 0) {
				 searchCond = searchCond + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
			
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			 } else {
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			 }   
		
		
		extraCon = "ORDER BY CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
        
              
     if(order.equalsIgnoreCase("OUTBOUND"))
     {	
     	     	sql = "" +"SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDNO else ORDNO+'-'+PAYMENT_REFNO end[ORDNO],"
     			 +"PAYMENT_DT as TRANDATE,CustCode,"  
     			 +"case when ORDERNAME='OUTBOUND' and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " 
     			 +"end[AMOUNT], "
     			 +"BALANCE =  COALESCE("
     			 +"("
     			 +"SELECT SUM(AMOUNT_PAID)"
     			 +"FROM ["+plant+"_ORDER_PAYMENT_DET]  AS I "
     			 +"WHERE I.ID <= PMT.ID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
     			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
     			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +" 
     			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) and PMT.CustCode=I.CustCode and ORDERNAME='OUTBOUND' " 
     			 +"and ISNULL(PAYMENT_MODE,'')='' and ORDNO='"+orderno+"'), 0" 
     			 +" )"
     			 +"-"
     			 +" COALESCE("
     			 +"("
     			 +"SELECT SUM(AMOUNT_PAID)"
     			 +"FROM ["+plant+"_ORDER_PAYMENT_DET]  AS I "
     			 +"WHERE I.ID <= PMT.ID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
     			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
     			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + "
     			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) and PMT.CustCode=I.CustCode and ORDERNAME='OUTBOUND' " 
     			 +"and ISNULL(PAYMENT_TYPE,'')='CREDIT' and "
     			 +"ISNULL(PAYMENT_MODE,'')<>'' and ORDNO='"+orderno+"'), 0"
     			 +")"
     			 +"FROM ["+plant+"_ORDER_PAYMENT_DET]  AS PMT "
     			 +"where PLANT = '"+plant+"' AND "
     			 +"ORDERNAME='OUTBOUND' and ORDNO='"+orderno+"'" +searchCond+extraCon;
     }else{
     		sql = "" +"SELECT case when ISNULL(PAYMENT_REFNO,'')='' then ORDNO else ORDNO+'-'+PAYMENT_REFNO end[ORDNO],"
    			 +"PAYMENT_DT as TRANDATE,CustCode,"  
    			 +"case when ORDERNAME='INBOUND' and ISNULL(PAYMENT_MODE,'')='' then AMOUNT_PAID else -AMOUNT_PAID " 
    			 +"end[AMOUNT], "
    			 +"BALANCE =  COALESCE("
    			 +"("
    			 +"SELECT SUM(AMOUNT_PAID)"
    			 +"FROM ["+plant+"_ORDER_PAYMENT_DET]  AS I "
    			 +"WHERE I.ID <= PMT.ID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
    			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
    			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' +" 
    			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) and PMT.CustCode=I.CustCode and ORDERNAME='INBOUND' " 
    			 +"and ISNULL(PAYMENT_MODE,'')='' and ORDNO='"+orderno+"'), 0" 
    			 +" )"
    			 +"-"
    			 +" COALESCE("
    			 +"("
    			 +"SELECT SUM(AMOUNT_PAID)"
    			 +"FROM ["+plant+"_ORDER_PAYMENT_DET]  AS I "
    			 +"WHERE I.ID <= PMT.ID AND CAST((SUBSTRING(I.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(I.PAYMENT_DT, 4, 2) + '-' + "
    			 +"SUBSTRING(I.PAYMENT_DT, 1, 2)) AS date)   <= " 
    			 +"CAST((SUBSTRING(PMT.PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PMT.PAYMENT_DT, 4, 2) + '-' + "
    			 +"SUBSTRING(PMT.PAYMENT_DT, 1, 2)) AS date) and PMT.CustCode=I.CustCode and ORDERNAME='INBOUND' " 
    			 +"and ISNULL(PAYMENT_TYPE,'')='DEBIT'   and  "
    			 +"ISNULL(PAYMENT_MODE,'')<>'' and ORDNO='"+orderno+"'), 0"
    			 +")"
    			 +"FROM ["+plant+"_ORDER_PAYMENT_DET]  AS PMT "
    			 +"where PLANT = '"+plant+"' AND "
    			 +"ORDERNAME='INBOUND' and ORDNO='"+orderno+"'" +searchCond+extraCon;
     }
		
		
		orderPayDao.setmLogger(mLogger);
		al = orderPayDao.selectForReport(sql, htCond, "");
	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}

	return al;
}

public ArrayList getcustomerorsuppliername(String plant,String afrmDate,String atoDate, String order,String custname)
		throws Exception {
 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
	String sql = "";
	ArrayList al = null;
	try {
		Hashtable htCond = new Hashtable();
		String searchCond ="",dtCondStr="",extraCon="",dtCondpmt="",searchCondord="",searchCondpmt="";
		   
            dtCondStr = "  AND CAST((SUBSTRING(CollectionDate,7,4) + '-' + SUBSTRING(CollectionDate,4,2) + '-' + SUBSTRING(CollectionDate,1,2)) AS date) ";
           
            if(custname.length()>0){
            	searchCond =searchCond + " AND (   CUSTNAME ='"+custname+"' or CustCode = '"+custname+"') ";
            }
            
            if (afrmDate.length() > 0) {
				 searchCondord = searchCond + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
				 
				if (atoDate.length() > 0) {
					searchCondord = searchCondord +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			} else {
				if (atoDate.length() > 0) {
					searchCondord = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			}    
                        
			
			 
        if(order.equalsIgnoreCase("OUTBOUND"))
        {	
        	sql = ""+"SELECT distinct custname,custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
					+"["+plant+"_DOHDR]A,["+plant+"_CUSTMST]B WHERE A.PLANT='"+plant+"' AND A.CustCode=B.CUSTNO"+searchCondord;
					
	        }else{
	         sql = ""+"SELECT distinct custname,custcode,ISNULL(PAY_IN_DAYS,'')pmtdays from "
						+"["+plant+"_POHDR]A,["+plant+"_VENDMST]B WHERE A.PLANT='"+plant+"' AND A.CustCode=B.VENDNO"+searchCondord;
	        }
		orderPayDao.setmLogger(mLogger);
		al = orderPayDao.selectForReport(sql, htCond, "");
	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}

	return al;
}
public boolean  InsertTempOrderPayment(String plant,String afrmDate,String atoDate, String order,String custname)
		throws Exception {
 OrderPaymentDAO orderPayDao = new OrderPaymentDAO();
 boolean insertflag  = false;
 boolean isExistsPaymentDetails = false;
 boolean isExiststemp = false;
	String sql = "";
	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	ArrayList arrCust = new ArrayList();
	
	try {
		Hashtable htCond = new Hashtable();
		String searchCond ="",dtCondStr="",extraCon="",sCustCode="";
		   
            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT,7,4) + '-' + SUBSTRING(PAYMENT_DT,4,2) + '-' + SUBSTRING(PAYMENT_DT,1,2)) AS date) ";
           
            if(custname.length()>0){
            	if(order.equalsIgnoreCase("OUTBOUND")){
            		sCustCode = customerBeanDAO.getCustomerCode(plant, custname);
            	}
            	else if(order.equalsIgnoreCase("INBOUND")){
            		sCustCode = customerBeanDAO.getVendorCode(plant, custname);
            	}
            	
            	searchCond =searchCond + " AND (   CustCode = '"+sCustCode+"') ";
            }
            
            if (afrmDate.length() > 0) {
            	searchCond = searchCond + dtCondStr + "  >= '" 
						+ afrmDate
						+ "'  ";
				 
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			} else {
				if (atoDate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ atoDate
					+ "'  ";
					
				}
			} 
            Hashtable htValues = new Hashtable();
            htValues.put("ORDERNAME", order);
        	htValues.put("PLANT", plant);    
        isExistsPaymentDetails  = new OrderPaymentDAO().isExisit(htValues,searchCond);
        if(isExistsPaymentDetails)
        {
        	htValues.remove("ORDERNAME");
        	isExiststemp = new OrderPaymentDAO().isExisittemp(htValues, "");
        	if(isExiststemp){
        		boolean deleteflag = new OrderPaymentDAO().deletetemporderpayment(htValues);
        	}
        	htValues.put("ORDERNAME", order);
        	insertflag = new OrderPaymentDAO().inserttemporderpayment(htValues, searchCond);
        	
        }
        else
        {
        	insertflag = true;
        }
                        
		
	} catch (Exception e) {
		this.mLogger.Exception(e.getMessage());
		throw e;
	}

	return insertflag;
}
}
