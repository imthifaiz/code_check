package com.track.db.util;

import java.math.BigDecimal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.constants.TransactionConstants;


import com.track.dao.CustomerReturnDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.DoTransferDetDAO;

import com.track.dao.DoTransferHdrDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.ToHdrDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.tables.ESTHDR;
import com.track.tran.WmsIssueMaterial;
import com.track.tran.WmsIssueWOInvcheck;
import com.track.tran.WmsOBISSUEReversal;
import com.track.tran.WmsPickIssue;
import com.track.tran.WmsPickIssue;
import com.track.tran.WmsPicking;
import com.track.tran.WmsPickingRandom;
import com.track.tran.WmsPickingWOInvcheck;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
//Added By Bruhan,June 26 2014, To process mobile sales order
import com.track.tran.WmsMobileSales;

public class ESTUtil {

	EstHdrDAO _EstHdrDAO = null;
	EstDetDAO _EstDetDAO = null;
	DoTransferHdrDAO _DoTranHdrDAO = null;
	DoTransferDetDAO _DoTranDetDAO = null;
	StrUtils strUtils = new StrUtils();
	private boolean printLog = MLoggerConstant.ESTUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private POBeanDAO poBeanDAO = new POBeanDAO();

	public MLogger getmLogger() {
		return mLogger;
	}
	

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public ESTUtil() {
		_EstDetDAO = new EstDetDAO();
		_EstHdrDAO = new EstHdrDAO();

		_EstDetDAO.setmLogger(mLogger);
		_EstHdrDAO.setmLogger(mLogger);
		_DoTranHdrDAO = new DoTransferHdrDAO();
		_DoTranDetDAO = new DoTransferDetDAO();
		_DoTranHdrDAO.setmLogger(mLogger);
		_DoTranDetDAO.setmLogger(mLogger);
	}

	
	public boolean saveestHdrDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_EstHdrDAO.setmLogger(mLogger);
			flag = _EstHdrDAO.insertESTHDR(ht);
		} catch (Exception e) {
			MLogger.log(-1, "saveesthdrDetails() ::  Exception ############### "
					+ e.getMessage());
			//throw e;
        throw new Exception("estimate order created already");
		}
		return flag;
	}
	public ArrayList getOutGoingestHdrDetails(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_EstHdrDAO.setmLogger(mLogger);
			al = _EstHdrDAO.selectOutGoingESTHDR(query, ht);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}
	public ArrayList getestHdrDetails(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_EstHdrDAO.setmLogger(mLogger);
			al = _EstHdrDAO.selectESTHDR(query, ht);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}

	public ArrayList getestHdrDetails(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_EstHdrDAO.setmLogger(mLogger);
			al = _EstHdrDAO.selectESTHDR(query, ht, extCond);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}
	
	
	public boolean updateestHdr(Hashtable ht) throws Exception {
		boolean flag = false;

		Hashtable htCond = new Hashtable();
		htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put(IDBConstants.ESTHDR_ESTNUM, (String) ht
				.get(IDBConstants.ESTHDR_ESTNUM));
		try {

			StringBuffer updateQyery = new StringBuffer("set ");

			updateQyery.append(IDBConstants.ESTHDR_JOB_NUM + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_JOB_NUM) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_CONTACT_NUM + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_CONTACT_NUM) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_COL_DATE + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_COL_DATE) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_REMARK1 + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_REMARK1) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_REMARK2 + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_REMARK2) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_REMARK3 + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_REMARK3) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_CUST_NAME + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_CUST_NAME) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_CUST_CODE + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_CUST_CODE) + "'");
			updateQyery	.append(","	+ IDBConstants.ESTHDR_PERSON_INCHARGE + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_PERSON_INCHARGE) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_ADDRESS + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_ADDRESS) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_ADDRESS2 + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_ADDRESS2) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_ADDRESS3 + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_ADDRESS3) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_COL_TIME + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_COL_TIME) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_EMPNO + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_EMPNO) + "'");
			updateQyery.append("," + IDBConstants.CURRENCYID + " = '"
					+ (String) ht.get(IDBConstants.CURRENCYID) + "'");
			updateQyery.append("," + IDBConstants.EXPIREDATE + " = '"
					+ (String) ht.get(IDBConstants.EXPIREDATE) + "'");
			updateQyery.append("," + IDBConstants.ESTHDR_GST + " = '"
					+ (String) ht.get(IDBConstants.ESTHDR_GST) + "'");
			updateQyery.append("," + IDBConstants.DELIVERYDATE + " = '"
					+ (String) ht.get(IDBConstants.DELIVERYDATE) + "'");                            
			updateQyery.append("," + IDBConstants.SHIPPINGCUSTOMER + " = '"
			        + (String) ht.get(IDBConstants.SHIPPINGCUSTOMER) + "'");
			updateQyery.append("," + IDBConstants.SHIPPINGID+ " = '"
			        + (String) ht.get(IDBConstants.SHIPPINGID) + "'");
			updateQyery.append("," + IDBConstants.ORDERDISCOUNT + " = '"
			        + (String) ht.get(IDBConstants.ORDERDISCOUNT) + "'");
			updateQyery.append("," + IDBConstants.SHIPPINGCOST + " = '"
			        + (String) ht.get(IDBConstants.SHIPPINGCOST) + "'");
			updateQyery.append("," + IDBConstants.INCOTERMS + " = '"
			        + (String) ht.get(IDBConstants.INCOTERMS) + "'");
			updateQyery.append("," + IDBConstants.ORDERTYPE + " = '"
					+ (String) ht.get(IDBConstants.ORDERTYPE) + "'");
			updateQyery.append("," + IDBConstants.ORDSTATUSID + " = '"
			+ (String) ht.get(IDBConstants.ORDSTATUSID) + "'");
			updateQyery.append("," + IDBConstants.STATUS + " = '"
			+ (String) ht.get(IDBConstants.STATUS) + "'");
			updateQyery.append("," + IDBConstants.PAYMENTTYPE + " = '"
					+ (String) ht.get(IDBConstants.PAYMENTTYPE) + "'");
			updateQyery.append("," + IDBConstants.POHDR_DELIVERYDATEFORMAT + " = '"
					+ (String) ht.get(IDBConstants.POHDR_DELIVERYDATEFORMAT) + "'");
			updateQyery.append("," + IDBConstants.TAXTREATMENT + " = '"
					+ (String) ht.get(IDBConstants.TAXTREATMENT) + "'");

			_EstHdrDAO.setmLogger(mLogger);
			flag = _EstHdrDAO.update(updateQyery.toString(), htCond,"");//" AND STATUS ='N'" commented to allow the header details to be updated
			
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Processed Estimate Order Cannot be modified");
		}
		return flag;
	}
	
	
	 public Map getESTReceiptHdrDetails(String aplant) throws Exception {
	        Map m =  new HashMap();
	     
	        m=_EstHdrDAO.getESTReciptHeaderDetails(aplant);
	         return m;
	 }
	    
	    public Map getESTReceiptInvoiceHdrDetails(String aplant) throws Exception {
	        Map m =  new HashMap();
	       
	        m=_EstHdrDAO.getESTReciptInvoiceHeaderDetails(aplant);
	         return m;
	 }
	

	public String getCurrencyUseQT(String plant,String estNO) throws Exception{
    	String currencyUseQT = "";
    	try{
    		currencyUseQT = _EstHdrDAO.getCurrencyUseQT(plant,estNO);
    	}
    	catch(Exception e ){
    		this.mLogger.exception(this.printLog, "", e);
    	}
    	
    	return currencyUseQT;
    }
    
	public String getConvertedUnitCostToLocalCurrency(String aPlant, String estNO,String unitCost) throws Exception {
        String localCurrencyConvertedUnitCost="";
          try{
               localCurrencyConvertedUnitCost= _EstHdrDAO.getUnitCostCovertedTolocalCurrency(aPlant, estNO,unitCost);
          }catch(Exception e){
              throw e;
          }
        return localCurrencyConvertedUnitCost;
    }
    
	public String getConvertedUnitCostForProduct(String aPlant, String estNO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _EstHdrDAO.getUnitCostBasedOnCurIDSelectedForOrder(aPlant, estNO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
    
	public String getminSellingConvertedUnitCostForProduct(String aPlant, String estNO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _EstHdrDAO.getminSellingUnitCostBasedOnCurIDSelectedForOrder(aPlant, estNO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	public boolean saveestDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_EstHdrDAO.setmLogger(mLogger);
			flag = _EstDetDAO.insertESTDet(ht);
		} catch (Exception e) {

			throw e;
		}
		return flag;
	}
	
	public ArrayList getestDetDetails(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_EstHdrDAO.setmLogger(mLogger);
		//	al = _DoDetDAO.selectDoDet(query, ht);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}

	public ArrayList getestDetDetails(String query, Hashtable ht, String extCond)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_EstHdrDAO.setmLogger(mLogger);
		//	al = _DoDetDAO.selectDoDet(query, ht, extCond);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}
	
	public String listESTDET(String plant, String aESTNO, String rflag)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		String desc = "";
		String uom = "", nonstkflag = "";
		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("estno", aESTNO);
				q = "estno,estlnno,item,isnull(itemdesc,'') itemdesc,isnull(PRODUCTDELIVERYDATE,'') PRODUCTDELIVERYDATE ,isnull(status,'') status,isnull(unitmo,'') uom,isnull(unitprice,0)*isnull(currencyuseqt,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(comment1,'') as prdRemarks,isnull(prodgst,0) prodgst,CAST(isnull(unitprice,0)*isnull(currencyuseqt,0) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as Convcost ";
			 _EstHdrDAO.setmLogger(mLogger);
			 PlantMstDAO plantMstDAO = new PlantMstDAO();
	         String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

			ArrayList al = _EstDetDAO.selectESTDet(q, htCond,
					" plant <> ''  order by estlnno");

			if (al.size() > 0) {
				ItemMstDAO _ItemMstDAO = new ItemMstDAO();
				_ItemMstDAO.setmLogger(mLogger);
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				ItemUtil _itemutil = new ItemUtil();
				_itemutil.setmLogger(mLogger);
				for (int i = 0; i < al.size(); i++) {

					Map m = (Map) al.get(i);
					String estno = (String) m.get("estno");
					String estlnno = (String) m.get("estlnno");
					String item = (String) m.get("item");
					String itemdesc = (String) m.get("itemdesc");
					String prdRemarks = (String) m.get("prdRemarks");
					String itemuom = (String) m.get("uom");
					//String ConvertedUnitPrice = new EstHdrDAO().getUnitCostBasedOnCurIDSelected(plant, estno,estlnno, item);
					/*String ConvertedUnitPrice = (String) m.get("unitprice");
					String price = StrUtils	.currencyWtoutSymbol(ConvertedUnitPrice);
					String qtyor = StrUtils.formatNum((String) m.get("qtyor"));
					String qtyis = StrUtils.formatNum((String) m.get("qtyis"));*/
					
					String PRODUCTDELIVERYDATE = (String) m.get("PRODUCTDELIVERYDATE");
					String price = (String) m.get("unitprice");
					String qtyor =(String) m.get("qtyor");
					String qtyis =(String) m.get("qtyis");
					String convcost =	(String) m.get("Convcost");

					//float priceValue="".equals(price) ? 0.0f :  Float.parseFloat(price);
					float qtyOrVal="".equals(qtyor) ? 0.0f :  Float.parseFloat(qtyor);
					float qtyIssueVal="".equals(qtyis) ? 0.0f :  Float.parseFloat(qtyis);
					double convcostVal ="".equals(convcost) ? 0.0d :  Double.parseDouble(convcost);
					convcost = StrUtils.addZeroes(convcostVal, numberOfDecimal);
					/*convcost=convcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");*/
					//price=String.valueOf(priceValue);
					/*if(priceValue==0f){
						price="0.00000";
						convcost="0.00000";
					}else{
						price=price.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");						
					}*/if(qtyOrVal==0f){
						qtyor="0.000";
					}else{
						qtyor=qtyor.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}if(qtyIssueVal==0f){
						qtyis="0.000";
					}else{
						qtyis=qtyis.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					String ListPrice = new ESTUtil().getConvertedUnitCostForProduct(plant,estno,item);   
					String listprice = StrUtils.currencyWtoutSymbol(ListPrice);
					String prodgst=(String) m.get("prodgst");
					 String OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,estno,item,"ESTIMATE");     
	                 if (OBDiscount.equalsIgnoreCase("null")	|| OBDiscount == null || OBDiscount == ""){
							OBDiscount="0.00";
					 }
	                 String discounttype="";
	                 int plusIndex = OBDiscount.indexOf("%");
	                 if (plusIndex != -1) {
	                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	                	 	discounttype = "BYPERCENTAGE";
	                     }
	                 else
	                 {
	                	 discounttype="BYPRICE";
	                 }
	                 
					List itemList = _itemutil.qryItemMst(item, plant, " ");
					for (int j = 0; j < itemList.size(); j++) {
						Vector vecItem = (Vector) itemList.get(j);
						desc = (String) vecItem.get(1);
						uom = (String) vecItem.get(3);
						nonstkflag = (String) vecItem.get(16);
					}
					
					//to get stockonhand and outgoing quantity
			    	 String minstkqty="",maxstkqty="",stockonhand="",outgoingqty="";
			    	 ItemMstUtil itemutil= new ItemMstUtil();
	                    ArrayList listQry = itemutil.getDOItemList(plant,item," and  isnull(itemmst.userfld1,'N')='N' ");
	                    Map mQty=(Map)listQry.get(0);
	                    if(mQty.size()>0){
		                    stockonhand=StrUtils.fString((String) mQty.get("STOCKONHAND"));
		                    outgoingqty=StrUtils.fString((String) mQty.get("OUTGOINGQTY"));
		                    minstkqty=StrUtils.fString((String) mQty.get("STKQTY"));
		                    maxstkqty=StrUtils.fString((String) mQty.get("MAXSTKQTY"));
	                    }
                   //to get stockonhand and outgoing quantity end
	                    
	                   

					String status = (String) m.get("status");
					if (!rflag.equalsIgnoreCase("5")) {
							link = "<a href=\"" + "modiESTDET.jsp?ESTNO=" + estno
								+ "&STATUS=" + status + "&ESTLNNO=" + estlnno
								+ "&ITEM=" + item + "&DESC="
								+ StrUtils.replaceCharacters2Send(itemdesc)
								+ "&QTY=" + qtyor 
								+ "&QTYISSUE=" + qtyis
								+ "&PRICE=" + new java.math.BigDecimal(convcost).toPlainString() 
								+ "&UOM="+ itemuom
								+ "&RFLAG="
								+ rflag + "&NONSTOCKFLG="
								+ nonstkflag
								+"&PRDREMARKS="+ prdRemarks 
								+ "&MINSTKQTY=" + minstkqty 
								+"&MAXSTKQTY=" + maxstkqty 
                                +"&STOCKONHAND=" + stockonhand 
                                +"&OUTGOINGQTY=" + outgoingqty 
                                +"&LISTPRICE=" + listprice
                                +"&PRODUCTDELIVERYDATE=" + PRODUCTDELIVERYDATE
                                +"&CUSDIS=" + OBDiscount  +"&PRODGST=" + prodgst +"&DISTYPE=" + discounttype +"&COSTRD=" + new java.math.BigDecimal(price).toPlainString() +"\">";
							
					}

											
					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"10%\">" 
							+ estlnno + "</td>"
							+ "<td width=\"20%\" align = left>"
							+ link
							+ "<FONT COLOR=\"blue\" align = center >"
							+ sqlBean.formatHTML(item)+ "</a></font>"  + "</td>"
							+ "<td width=\"30%\" align = left>"
							+ sqlBean.formatHTML(desc) + "</td>"
							+ "<td width=\"10%\" align = center>"
							+ sqlBean.formatHTML(new java.math.BigDecimal(convcost).toPlainString()) + "</td>"
							+ "<td width=\"10%\" align = center>"
							+ sqlBean.formatHTML(qtyor) + "</td>"
							+ "<td width=\"10%\" align = center>"
							+ sqlBean.formatHTML(qtyis) + "</td>"
							+ "<td width=\"10%\" align = left>" + itemuom
							+ "</td>" + "</tr>";
					

				}
			} else {
				result += "<tr valign=\"middle\"><td colspan=\"7\" align=\"center\">"
						+ " Please add in Products " + "</td></tr>";
			}
		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	
   /***Created by Bruhan to add price editable on edit outbound order on 26/02/2013*****
     **********************  Modification History  ******************************
     *** Bruhan,Oct 27 2014,Description: To check update quantity less than picking quantity,add condition for update dodetail
     ***** Bruhan,March 12 2015,Description: To remove number format in qtyOr,qtyPick,qtyIs
	*/ 
	public boolean updateestDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;
		boolean updateHdrFlag = false;
		boolean isExitFlag = false;
		ShipHisDAO shipHisDAO = new ShipHisDAO();
		String price = ht.get("UNITPRICE").toString();
		String qty = ht.get("ORDQTY").toString();
		String PRODUCTDELIVERYDATE = ht.get("PRODUCTDELIVERYDATE").toString();
		String PrdRemarks = ht.get(IDBConstants.ESTDET_COMMENT1).toString();
		Hashtable htUpdateestdet = new Hashtable();
		htUpdateestdet.clear();
		htUpdateestdet.put(IDBConstants.PLANT, ht.get("PLANT"));
		htUpdateestdet.put(IDBConstants.ESTHDR_ESTNUM, ht.get("ESTNO"));
		htUpdateestdet.put("ESTLNNO", ht.get("ESTLNNO"));
		htUpdateestdet.put(IDBConstants.ITEM, ht.get("ITEM"));
		try {
			// check update quantity less than picking quantity
						String q = "";
						String qtyOr = "", status = "",qtyIs="";
						Hashtable htCond = new Hashtable();
						htCond.put("PLANT", ht.get("PLANT"));
						htCond.put("estno", ht.get("ESTNO"));
						htCond.put("estlnno", ht.get("ESTLNNO"));
						htCond.put("item", ht.get("ITEM"));
						q = " isnull(qtyor,0) qtyor,isnull(qtyis,0) qtyis,isnull(status,'') status ";
						_EstHdrDAO.setmLogger(mLogger);
						_EstDetDAO.setmLogger(mLogger);
						ArrayList al = _EstDetDAO.selectESTDet(q, htCond, " plant <> ''");
						for (int i = 0; i < al.size(); i++) {
							Map m = (Map) al.get(i);

							qtyOr = (String) m.get("qtyor");
							status = (String) m.get("status");
							qtyIs =(String) m.get("qtyis");

						}	
			 
			if(Double.parseDouble(qty) < Double.parseDouble(qtyIs))
			{
				throw new Exception(" Order Qty entered should not be less than already Issued Qty:"+qtyIs);
			}
			
			String query = "";
			EstDetDAO _EstDetDAO = new EstDetDAO();
			EstHdrDAO _EstHdrDAO = new EstHdrDAO();
			String updateEstHdr = "";
			Hashtable htConditoHdr = new Hashtable();
			htConditoHdr.put("PLANT", ht.get("PLANT"));
			htConditoHdr.put("estno", ht.get("ESTNO"));
				
		 if(status.equals("C") && Double.parseDouble(qtyOr)!=Double.parseDouble(qty))
	    {
	    	query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",COMMENT1='" +PrdRemarks+"',STATUS='O'";
	    	_EstDetDAO.setmLogger(mLogger);
			flag = _EstDetDAO.update(query, htUpdateestdet, "");
	    	isExitFlag =_EstHdrDAO.isExisit(htConditoHdr," isnull(STATUS,'') in ('Confirm') ");
	    	if (isExitFlag && Double.parseDouble(qtyOr)!=Double.parseDouble(qty)){
	    		updateEstHdr = "set  status='Pending' ";
				updateHdrFlag=true;
			}
	     }
	    else if(status.equals("O") && Double.parseDouble(qty)==Double.parseDouble(qtyIs))
	    {
	    	query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",COMMENT1='" +PrdRemarks+"',STATUS='C'" +", PRODUCTDELIVERYDATE='"+ PRODUCTDELIVERYDATE +"'";
	    	_EstDetDAO.setmLogger(mLogger);
			flag = _EstDetDAO.update(query, htUpdateestdet, "");
	    	isExitFlag = _EstDetDAO.isExisit(htConditoHdr,	"STATUS in ('O','N')");
	    	if (isExitFlag)
	    	{
	    		updateHdrFlag=false;
	    	}
			else
			{
				updateEstHdr = "set  Status='Confirm'";
				updateHdrFlag=true;
	    	}
	    }
	    else
	    {
	    	query = "set QTYOR=" + qty + ",UNITPRICE=" + price+ ",COMMENT1='" +PrdRemarks+"' , PRODUCTDELIVERYDATE='"+ PRODUCTDELIVERYDATE +"'";
	    	_EstDetDAO.setmLogger(mLogger);
			flag = _EstDetDAO.update(query, htUpdateestdet, "");
	    	updateHdrFlag=false;
	    }
		
	    //update DOHDR
		 if(flag){
			if(updateHdrFlag==true){
			 flag = _EstHdrDAO.update(updateEstHdr, htConditoHdr, "");
			}
		    		
		}
			
			

		} catch (Exception e) {

			throw e;
		}
		return flag;
	}


    public boolean isOpenEstimateOrder(String plant, String estno)
			throws Exception {

		boolean flag = false;
		try {

			EstHdrDAO _estHdrDAO = new EstHdrDAO();
			_estHdrDAO.setmLogger(mLogger);

			Hashtable htCondiesthdr = new Hashtable();
			htCondiesthdr.put("PLANT", plant);
			htCondiesthdr.put("estno", estno);

			flag = _estHdrDAO.isExisit(htCondiesthdr, " STATUS ='Confirm'");

			/*if (!flag) {
				throw new Exception(
						" Processed Outbound Order Cannot be modified");
			}*/

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
	
    public ArrayList getEstimateDetailsExportList(Hashtable ht, String dirType, String plant) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		MovHisDAO movHisDAO = new MovHisDAO();
		movHisDAO.setmLogger(mLogger);
		try {
			    
			    String aQuery = "select a.estno,estlnno,isnull(jobnum,'') as jobnum,isnull(collectiondate,'')collectiondate,isnull(collectiontime,'')collectiontime,isnull(remark1,'') as remarks,isnull(custcode,'')custcode,item,isnull(itemdesc,'')itemdesc,qtyor,unitmo,unitprice,isnull(currencyid,'')currencyid,isnull(EMPNO,'')empno,expiredate, "
						+ " isnull(a.orderdiscount,0) orderdiscount,isnull(a.shippingcost,0) shippingcost,isnull(a.incoterms,'') incoterms,isnull(a.shippingcustomer,'')shippingcustomer,isnull(a.PAYMENTTYPE,'')paymenttype,isnull(A.TAXTREATMENT,'') as TAXTREATMENT from "
			    		+ "["
						+ plant
						+ "_"
						+ "esthdr] a,"
						+ "["
						+ plant
						+ "_"
						+ "estdet] b where a.estno=b.estno and a.PLANT<>'' "; 

				
				arrList = movHisDAO.selectForReport(aQuery, ht);					
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"Exception :ESTUtil :: getEstimateDetailsExportList:", e);
		}
		return arrList;
	}
    
    public boolean deleteESTDetLineDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			EstDetDAO _estDetDAO = new EstDetDAO();
			Hashtable httoDet = new Hashtable();
			_estDetDAO.setmLogger(mLogger);

			httoDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
			httoDet.put(IDBConstants.ESTHDR_ESTNUM, ht.get(IDBConstants.ESTHDR_ESTNUM));
			httoDet.put(IDBConstants.ESTHDR_ESTLNNUM, ht.get(IDBConstants.ESTHDR_ESTLNNUM));

			flag = _estDetDAO.delete(httoDet);
			if (flag) {

				try{
					//delete dodet multi remarks
                    Hashtable htRemarksDel = new Hashtable();
                    htRemarksDel.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
                    httoDet.put(IDBConstants.ESTHDR_ESTNUM, ht.get(IDBConstants.ESTHDR_ESTNUM));
        			httoDet.put(IDBConstants.ESTHDR_ESTLNNUM, ht.get(IDBConstants.ESTHDR_ESTLNNUM));
                    htRemarksDel.put(IDBConstants.ESTDET_ITEM, ht.get(IDBConstants.ESTDET_ITEM));
                    flag = _estDetDAO.deleteEstimateMultiRemarks(htRemarksDel);
               
                   //delete dodet multi remarks end
                    
	    		    //To arrange the line no's
	    		        httoDet.remove(IDBConstants.ESTHDR_ESTLNNUM);
	    		        String  sql =  "SET ESTLNNO =ESTLNNO-1 ";
		                String extraCond=" AND ESTLNNO > '" + ht.get(IDBConstants.ESTHDR_ESTLNNUM) + "' ";
		                _estDetDAO.update(sql,httoDet,extraCond);
		         		                
	            }catch(Exception e){}
				MovHisDAO movhisDao = new MovHisDAO();
				Hashtable htmovHis = new Hashtable();
				movhisDao.setmLogger(mLogger);
				htmovHis.clear();
				htmovHis.put(IDBConstants.PLANT, (String) ht
						.get(IDBConstants.PLANT));
				htmovHis.put("DIRTYPE", "SALES_ESTIMATE_DELETE_PRODUCT");
				htmovHis.put(IDBConstants.ITEM, (String) ht
						.get(IDBConstants.ESTDET_ITEM));
				htmovHis.put(IDBConstants.MOVHIS_ORDNUM, (String) ht
						.get(IDBConstants.ESTHDR_ESTNUM));
				htmovHis.put(IDBConstants.MOVHIS_ORDLNO, (String) ht
						.get(IDBConstants.ESTHDR_ESTLNNUM));
				htmovHis.put(IDBConstants.CREATED_BY, (String) ht
						.get(IDBConstants.CREATED_BY));
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
    
    public Boolean removeRow(String plant, String estno, String userId) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			ut = DbBean.getUserTranaction();
			ut.begin();
			Boolean removeHeader = this._EstHdrDAO.removeOrder(plant, estno);
			Boolean removeDetails = this._EstDetDAO.removeOrderDetails(plant,estno);
			
		  	Hashtable htMovHis = createMoveHisHashtable(plant, estno, userId);
			Boolean movementHiss = movHisDao.insertIntoMovHis(htMovHis);
			if (removeHeader & removeDetails  & movementHiss) {
				DbBean.CommitTran(ut);
				flag = Boolean.valueOf(true);
			} else {
				DbBean.RollbackTran(ut);
				flag = Boolean.valueOf(false);
			}
		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = Boolean.valueOf(false);
		}
		return flag;

	}
    
    private Hashtable createMoveHisHashtable(String plant, String estno,
			String userId) {
		Hashtable<String, String> htMovHis = new Hashtable<String, String>();

		htMovHis.put(IDBConstants.PLANT, plant);
		htMovHis.put("DIRTYPE", "DELETE_SALES_ESTIMATE_ORDER");
		htMovHis.put(IDBConstants.ITEM, " ");
		htMovHis.put("BATNO", " ");
		htMovHis.put("ORDNUM", estno);

		htMovHis.put("MOVTID", " ");
		htMovHis.put("RECID", " ");
		htMovHis.put(IDBConstants.LOC, " ");

		htMovHis.put(IDBConstants.CREATED_BY, userId);
		htMovHis.put(IDBConstants.TRAN_DATE, DateUtils
				.getDateinyyyy_mm_dd(DateUtils.getDate()));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

		return htMovHis;
	}
    
    public String getOrderDate(String aPlant, String aESTNO) throws Exception {
		String OrderDate = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ESTNO", aESTNO);

		String query = " ISNULL( " + "collectionDate +' '+LEFT(CollectionTime, 2)+':'+RIGHT(CollectionTime, 2)" + ",'') as " + "date" + " ";
		_EstHdrDAO.setmLogger(mLogger);
		Map m = _EstHdrDAO.selectRow(query, ht);

		OrderDate = (String) m.get("date");
		return OrderDate;
	}
    public String getOrderDateOnly(String aPlant, String aESTNO) throws Exception {
    	String OrderDate = "";
    	
    	Hashtable ht = new Hashtable();
    	ht.put("PLANT", aPlant);
    	ht.put("ESTNO", aESTNO);
    	
    	String query = " ISNULL( " + "collectionDate" + ",'') as " + "date" + " ";
    	_EstHdrDAO.setmLogger(mLogger);
    	Map m = _EstHdrDAO.selectRow(query, ht);
    	
    	OrderDate = (String) m.get("date");
    	return OrderDate;
    }
    
    public String getJobNum(String aPlant, String aESTNO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ESTNO", aESTNO);

		String query = " ISNULL( " + "jobNum" + ",'') as " + "jobNum" + " ";
		_EstHdrDAO.setmLogger(mLogger);
		Map m = _EstHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("jobNum");
		return custCode;
	}

	public ArrayList listEstDetails(String plant, String aESTNO)
			throws Exception {
		String q = "";
		ArrayList al = null;

		try {

			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put("estno", aESTNO);

			q = "estno,estlnno,item,isnull(itemdesc,'') itemdesc,isnull(status,'') status,isnull(unitmo,'') uom,isnull(unitprice,0)*isnull(currencyuseqt,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(comment1,'') as prdRemarks,CAST(isnull(unitprice,0)*isnull(currencyuseqt,0) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as conunitprice ";
			al = _EstDetDAO.selectESTDet(q, htCond,
					" plant <> ''  order by estlnno");

		} catch (Exception e) {
			throw e;
		}
		return al;
	}

 public boolean saveEstimateoMultiRemarks(Hashtable ht) throws Exception {
			boolean flag = false;
			try {
				_EstDetDAO.setmLogger(mLogger);
				flag =  _EstDetDAO.insertEstimateMultiRemarks(ht);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}

			return flag;
		}

public ArrayList listEstimateMultiRemarks(String plant, String aESTNO,String aESTLNNO)
				throws Exception {
			String q = "";
			String link = "";
			String result = "";
			String where = "";
			ArrayList al = null;

			try {
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", plant);
				htCond.put("estno", aESTNO);
				htCond.put("estlnno", aESTLNNO);
				q = "isnull(remarks,'') remarks";
				_EstDetDAO.setmLogger(mLogger);
				al = _EstDetDAO.selectEstimateMultiRemarks(q,htCond," plant <> '' order by id_remarks");
			} catch (Exception e) {
				throw e;
			}
			return al;
		}
	    
public String getConvertedUnitCostForProductWTC(String aPlant, String estNO,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _EstHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTC(aPlant, estNO,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

public ArrayList getPendingSalesEstimate(String plant, String numberOfDecimal) throws Exception {
	ArrayList al = null;
	try {
		_EstHdrDAO.setmLogger(mLogger);
		al = _EstHdrDAO.getPendingSalesEstimate(plant,numberOfDecimal);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}
	return al;
}

	/* created by navas */
public boolean saveDoMultiRemarks(Hashtable ht) throws Exception {
	boolean flag = false;
	try {
		_EstDetDAO.setmLogger(mLogger);
		flag = _EstDetDAO.insertDoMultiRemarks(ht);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}

	return flag;
}

public String getOBDiscountSelectedItemByCustomer(String aPlant, String custcode,String aItem,String aType) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _EstHdrDAO.getOBDiscountSelectedItemByCustomer(aPlant, custcode,aItem,aType);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}



public String getminSellingConvertedUnitCostForProductByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _EstHdrDAO.getminSellingUnitCostBasedOnCurIDSelectedForOrderByCurrency(aPlant, currencyID,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}
public String getConvertedUnitCostForProductByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _EstHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderByCurrency(aPlant, currencyID,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

public String getConvertedUnitCostForProductWTCByCurrency(String aPlant, String currencyID,String aItem) throws Exception {
    String ConvertedUnitCost="";
      try{
           ConvertedUnitCost= _EstHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTCByCurrency(aPlant, currencyID,aItem);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}
}