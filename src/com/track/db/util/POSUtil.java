
package com.track.db.util;

import com.track.constants.IDBConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.LocMstBeanDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.ShipHisDAO;
import com.track.tables.ITEMMST;
import com.track.util.MLogger;
import com.track.util.XMLUtils;
import com.track.util.StrUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class POSUtil {
	public POSUtil() {
	}

	private MLogger mLogger = new MLogger();
	private LocMstBeanDAO locMstBeanDAO = new LocMstBeanDAO();
	private boolean printLog = MLoggerConstant.PosUtil_PRINTPLANTMASTERLOG;
	
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	
	public String getItemList(String plant,String Item,String cond) {

		String xmlStr = "";
	        List listQry =null;
	        ItemUtil itemUtil = new ItemUtil();
		XMLUtils xmlUtils = new XMLUtils();
		itemUtil.setmLogger(mLogger);
		try {
			listQry = itemUtil.qryItemMstCond(Item,plant,cond);

			if (listQry.size() > 0) {
				xmlStr += xmlUtils.getXMLHeader();
				xmlStr += xmlUtils.getStartNode("products total='"+ String.valueOf(listQry.size()) + "'");
				for (int i = 0; i < listQry.size(); i++) {
				    Vector vecItem   = (Vector)listQry.get(i);
				     
					xmlStr += xmlUtils.getStartNode("record");
					xmlStr += xmlUtils.getXMLNode("item", (String)vecItem.get(0));
                     xmlStr += xmlUtils.getXMLNode("itemdesc", StrUtils.replaceCharacters2Send((String)vecItem.get(1)));
    					xmlStr += xmlUtils.getEndNode("record");
				}
				xmlStr += xmlUtils.getEndNode("products");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
    public ITEMMST getItemDetails(String aplant, String item,
                    int scanqty) throws Exception {
           
            ITEMMST items = new ITEMMST();
            String itemdesc = "", qty = "", price = "";
            float discount=0;String disccntstr = "";
            ItemSesBeanDAO itemDAO = new ItemSesBeanDAO();
        
            itemDAO.setmLogger(mLogger);
            List prdlist = itemDAO.queryProducts(aplant, " where item='" + item
                            + "'");
            for (int i = 0; i < 1; i++) {
                    Vector vecItem = (Vector) prdlist.get(0);
                    itemdesc = (String) vecItem.get(1);
                    qty = (String) vecItem.get(8);
                    price = (String) vecItem.get(12);
                    discount = Float.parseFloat((String) vecItem.get(13));
            }
            if(discount>0)
            {
                    items.setDISCOUNT(discount);
            disccntstr=" + Discount "+String.valueOf(discount)+"%";
            }
            items.setITEM(item);
            items.setITEMDESC(itemdesc+disccntstr);
            items.setDISCOUNT(discount);
            int minqty = 1;
            float pricef = Float.parseFloat(price);

            items.setStkqty(scanqty);
                    items.setUNITPRICE(pricef);
                    if (scanqty > 1)
                            items.setTotalPrice((pricef-pricef*discount/100) * scanqty);
                    else
                    items.setTotalPrice(pricef-pricef*discount/100);
            return items;
    }
    
    
    public Map getPosReceiptHdrDetails(String aplant) throws Exception {
           Map m =  new HashMap();
           POSHdrDAO  dao = new POSHdrDAO();
           m=dao.getPOSReciptHeaderDetails(aplant);
            return m;
    }

        
    public String getTransactionList(String plant,String aLoc) {

            String xmlStr = "";
            ArrayList arrList =null;
            POSDetDAO posdao = new POSDetDAO();
            XMLUtils xmlUtils = new XMLUtils();
            posdao.setmLogger(mLogger);
            try {
                    arrList = posdao.listTranIdForPos(plant,aLoc);

                    if (arrList.size() > 0) {
                            xmlStr += xmlUtils.getXMLHeader();
                            xmlStr += xmlUtils.getStartNode("TranList total='"+ String.valueOf(arrList.size()) + "'");
                            for (int i = 0; i < arrList.size(); i++) {
                                Map arrLocLine = (Map)arrList.get(i);
                                   String sTranId    = (String)arrLocLine.get("POSTRANID");
                                   String sNoOfItems   = strUtils.removeQuotes((String)arrLocLine.get("NOOFITEMS"));
                             
                                    xmlStr += xmlUtils.getStartNode("record");
                                    xmlStr += xmlUtils.getXMLNode("tranId", sTranId);
                                    xmlStr += xmlUtils.getXMLNode("count", sNoOfItems);
                                    xmlStr += xmlUtils.getEndNode("record");
                            }
                            xmlStr += xmlUtils.getEndNode("TranList");
                    }

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }

            return xmlStr;
    }

	
    public Map getPosReceiveReceiptHdrDetails(String aplant) throws Exception {
        Map m =  new HashMap();
        POSHdrDAO  dao = new POSHdrDAO();
        m=dao.getPOSReceiveReciptHeaderDetails(aplant);
         return m;
 }	
    public Map getPosShiftCloseReceiptHdrDetails(String aplant) throws Exception {
        Map m =  new HashMap();
        POSHdrDAO  dao = new POSHdrDAO();
        m=dao.getPOSShiftCloseReceiptHdrDetails(aplant);
         return m;
 }	
      
public Map getPosMoveHdrDetails(String aplant) throws Exception {
    Map m =  new HashMap();
    POSHdrDAO  dao = new POSHdrDAO();
    m=dao.getPOSMoveHeaderDetails(aplant);
     return m;
}
public boolean updatePosHdr(Hashtable ht) throws Exception {
	boolean flag = false;

	Hashtable htCond = new Hashtable();
	htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
	htCond.put(IDBConstants.POS_TRANID, (String) ht
			.get(IDBConstants.POS_TRANID));
	try {

		StringBuffer updateQyery = new StringBuffer("set ");

		updateQyery.append(IDBConstants.DOHDR_JOB_NUM + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_JOB_NUM) + "'");
		updateQyery.append("," + IDBConstants.DOHDR_CONTACT_NUM + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_CONTACT_NUM) + "'");		
		updateQyery.append("," + IDBConstants.DOHDR_REMARK1 + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_REMARK1) + "'");
		
		updateQyery.append("," + IDBConstants.DOHDR_REMARK3 + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_REMARK3) + "'");
		updateQyery.append("," + IDBConstants.DOHDR_CUST_NAME + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_CUST_NAME) + "'");
		updateQyery.append("," + IDBConstants.DOHDR_CUST_CODE + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_CUST_CODE) + "'");
		updateQyery
				.append(","
						+ IDBConstants.DOHDR_PERSON_INCHARGE
						+ " = '"
						+ (String) ht
								.get(IDBConstants.DOHDR_PERSON_INCHARGE)
						+ "'");
		updateQyery.append("," + IDBConstants.DOHDR_ADDRESS + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_ADDRESS) + "'");
		updateQyery.append("," + IDBConstants.DOHDR_ADDRESS2 + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_ADDRESS2) + "'");
		updateQyery.append("," + IDBConstants.DOHDR_ADDRESS3 + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_ADDRESS3) + "'");		
		updateQyery.append("," + IDBConstants.ORDERTYPE + " = '"
				+ (String) ht.get(IDBConstants.ORDERTYPE) + "'");
		updateQyery.append("," + IDBConstants.CURRENCYID + " = '"
				+ (String) ht.get(IDBConstants.CURRENCYID) + "'");
		updateQyery.append("," + IDBConstants.DELIVERYDATE + " = '"
				+ (String) ht.get(IDBConstants.DELIVERYDATE) + "'");
		
		updateQyery.append("," + IDBConstants.DOHDR_GST + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_GST) + "'");
		updateQyery.append("," + IDBConstants.DOHDR_EMPNO + " = '"
				+ (String) ht.get(IDBConstants.DOHDR_EMPNO) + "'");                            
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
		updateQyery.append("," + IDBConstants.CASHCUST + " = '"
		        + (String) ht.get(IDBConstants.CASHCUST) + "'");
updateQyery.append("," + IDBConstants.PAYMENTTYPE + " = '"
		        + (String) ht.get(IDBConstants.PAYMENTTYPE) + "'");
updateQyery.append("," + IDBConstants.POSHDR_DELIVERYDATEFORMAT + " = '"
        + (String) ht.get(IDBConstants.POSHDR_DELIVERYDATEFORMAT) + "'");
				
		POSHdrDAO  dao = new POSHdrDAO();
		dao.setmLogger(mLogger);
		flag = dao.update(updateQyery.toString(), htCond,"");
		
		if(flag){
			htCond.remove(IDBConstants.POS_TRANID);
			htCond.put(IDBConstants.DOHDR_DONUM, (String) ht.get(IDBConstants.POS_TRANID));
		 updateQyery = new StringBuffer("set ");
		 updateQyery.append(" CNAME = '"+ (String) ht.get(IDBConstants.DOHDR_CUST_NAME) + "'");
		 boolean isExistsRecords = new ShipHisDAO().isExisit(htCond, "");
		 if(isExistsRecords){
		 flag = new ShipHisDAO().update(updateQyery.toString(), htCond,"");
		 }
		}

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw new Exception("Processed Goods Issue Cannot be modified");
	}
	return flag;
}
public ArrayList getPosHdrDetails(String query, Hashtable ht, String extCond)
		throws Exception {
	ArrayList al = new ArrayList();

	try {
		 POSHdrDAO  dao = new POSHdrDAO();
		 dao.setmLogger(mLogger);
		al = dao.selectPosHdr(query, ht, extCond);
	} catch (Exception e) {

		throw e;
	}
	return al;
}
public ArrayList dnpl(String plant, String aDONO, String invoiceNo)
	      throws Exception {
	     String q = "";
	     String link = "";
	     String result = "";
	     String where = "";
	     ArrayList al = null;
	     POSHdrDAO  dao = new POSHdrDAO();
	     try {
	      Hashtable htCond = new Hashtable();
	      htCond.put("A.PLANT", plant);
	      htCond.put("A.dono", aDONO);
	      htCond.put("INVOICENO", invoiceNo);
	      q = "A.dono,dolno as dolnno,Status as lnstat,a.item,isnull(ORDQTY,0) as qtyor,isnull(ORDQTY,0) as qtyis,isnull(PICKQTY,0) as qtyPick,a.loc as loc,a.userfld4 as batch,a.userfld3 as custname,"
	         +" (select itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as Itemdesc,"
	         + "(select stkuom from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as uom,"
	         +"(SELECT ISNULL(NETWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) AS netweight,"
	          +"(SELECT ISNULL(GROSSWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) AS grossweight,"
	         +" (select isnull(hscode,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as hscode,"
	         +" (select isnull(coo,'') from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as coo,"
	         + "'' as packing,'' as dimension";
	      dao.setmLogger(mLogger);
	      al = dao
	        .selectdnplPosDet(
	          q,
	          htCond,
	          " a.plant <> ''  and Status in ('O', 'C') order by dolno");

	     } catch (Exception e) {
	      throw e;
	     }
	     return al;
	  }
public ArrayList getPosDetails(String query, Hashtable ht, String extCond)
		throws Exception {
	ArrayList al = new ArrayList();

	try {
		 POSHdrDAO  dao = new POSHdrDAO();
		 dao.setmLogger(mLogger);
		al = dao.selectdnplPosDet(query, ht, extCond);
	} catch (Exception e) {

		throw e;
	}
	return al;
}
public String getConvertedUnitCostToLocalCurrency(String aPlant, String poNO,String unitCost) throws Exception {
    String localCurrencyConvertedUnitCost="";
      try{
    	  POSHdrDAO  dao = new POSHdrDAO();
           localCurrencyConvertedUnitCost= dao.getUnitCostCovertedTolocalCurrency(aPlant, poNO,unitCost);
      }catch(Exception e){
          throw e;
      }
    return localCurrencyConvertedUnitCost;
}
public String getLocalCurrency(String aPlant, String CURRENCY,String unitCost) throws Exception {
    String localCurrencyConvertedUnitCost="";
      try{
    	  POSHdrDAO  dao = new POSHdrDAO();
           localCurrencyConvertedUnitCost= dao.getLocalCurrency(aPlant, CURRENCY,unitCost);
      }catch(Exception e){
          throw e;
      }
    return localCurrencyConvertedUnitCost;
}
public String getConvertedLocalCurrencyToUnitCost(String aPlant, String poNO,String unitCost) throws Exception {
    String localCurrencyConvertedUnitCost="";
      try{
    	  POSHdrDAO  dao = new POSHdrDAO();
           localCurrencyConvertedUnitCost= dao.getlocalCurrencyCovertedToUnitCost(aPlant, poNO,unitCost);
      }catch(Exception e){
          throw e;
      }
    return localCurrencyConvertedUnitCost;
}
public String getLocalCurrencyConvert(String aPlant, String CURRENCY,String unitCost) throws Exception {
    String localCurrencyConvertedUnitCost="";
      try{
    	  POSHdrDAO  dao = new POSHdrDAO();
           localCurrencyConvertedUnitCost= dao.getLocalCurrencyConvert(aPlant, CURRENCY,unitCost);
      }catch(Exception e){
          throw e;
      }
    return localCurrencyConvertedUnitCost;
}
public String getminSellingLocalCurrencyConvert(String aPlant, String CURRENCY,String unitCost) throws Exception {
    String localCurrencyConvertedUnitCost="";
      try{
    	  POSHdrDAO  dao = new POSHdrDAO();
           localCurrencyConvertedUnitCost= dao.getminSellingLocalCurrencyConvert(aPlant, CURRENCY,unitCost);
      }catch(Exception e){
          throw e;
      }
    return localCurrencyConvertedUnitCost;
}
public String getCurrencyUseQT(String plant,String posNO) throws Exception{
	String currencyUseQT = "";
	try{
		POSHdrDAO  dao = new POSHdrDAO();
		currencyUseQT = dao.getCurrencyUseQT(plant,posNO);
	}
	catch(Exception e ){
		this.mLogger.exception(this.printLog, "", e);
	}
	
	return currencyUseQT;
}
public String getOBDiscountSelectedItem(String plant, String currencyid,String itemNo,String custcode) throws Exception {
    String ConvertedUnitCost="";
      try{
    	  POSHdrDAO  dao = new POSHdrDAO();
           ConvertedUnitCost= dao.getOBDiscountSelectedItem(plant, currencyid,itemNo,custcode);
      }catch(Exception e){
          throw e;
      }
    return ConvertedUnitCost;
}

}