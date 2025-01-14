package com.track.db.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BaseDAO;
import com.track.dao.ItemMstDAO;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
/*- ************Modification History*********************************
Feb 13, 2018,Ravindra, Description:Fix:Issue in retrieving expiring products
*/
public class ItemMstUtil {
	ItemMstDAO _ItemMstDAO = null;
	private boolean printLog = MLoggerConstant.ItemMstUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public ItemMstUtil() {
		_ItemMstDAO = new ItemMstDAO();

	}

	public ArrayList getItemList(String plant, String item, String itemDesc,String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
		
	   
                String sCondition="";
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	    itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "select distinct(itemmst.item) item ,itemmst.itemdesc , itemmst.unitprice UNITPRICE ,itemmst.STKUOM UOM,itemmst.PURCHASEUOM PURCHASEUOM,itemmst.SALESUOM SALESUOM,itemmst.SERVICEUOM SERVICEUOM,itemmst.REMARK1 REMARK1,itemmst.REMARK2 MANUFACTURER,isnull(cast(itemmst.STKQTY as decimal(18,3)),0) as MINSTKQTY,isnull(cast(itemmst.MAXSTKQTY as decimal(18,3)),0) MAXSTKQTY, "
					+ "  ISNULL((select ISNULL(SUM(QTY),0) from [" + plant + "_INVMST] where ITEM=itemmst.ITEM  group by item),0) STOCKONHAND,"
					+ "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYRC),0) from [" + plant + "_PODET] where ITEM=itemmst.ITEM   and LNSTAT <>'C' group by item),0) INCOMINGQTY,ISNULL(PRODGST,0)PRODGST "
					+ "from [" 
					+ plant
					+ "_"
					+ "itemmst"
					+ "] itemmst,["
					+ plant
					+ "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
					+ plant
					+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
					+ item
					+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
					+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	
	public String isValidItem(Hashtable ht, String extra) throws Exception {

		ItemMstDAO itemdao = new ItemMstDAO();
		itemdao.setmLogger(mLogger);
		String desc = "";
		String xmlStr = "";
		Map map = null;
		boolean isValid = false;
		try {
			isValid = itemdao.isExisit(ht, extra);
			if (isValid) {
				xmlStr = XMLUtils.getXMLMessage(0, "Valid Item");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Item");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}

	public boolean isValidItemInItemmst(String aPlant, String aItem)
			throws Exception {
		boolean itemFound = true;
		try {

			if (aItem.length() > 0) {

				
				_ItemMstDAO.setmLogger(mLogger);
				String scannedItemNo = _ItemMstDAO.getItemIdFromAlternate(
						aPlant, aItem);
				if (scannedItemNo == null) {
					itemFound = false;
				}
					

				if (!itemFound) {
					throw new Exception(" Scan/Enter a Valid Product ID");
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return itemFound;
	}
	
	
		public boolean isValidItemInItemmstPDAMiscReceive(String aPlant, String aItem)
		throws Exception {
	boolean itemFound = true;
	try {
	
		if (aItem.length() > 0) {
	
			
			_ItemMstDAO.setmLogger(mLogger);
			String scannedItemNo = _ItemMstDAO.getItemIdFromAlternatePDAMiscReceive(aPlant, aItem);
			
			if (scannedItemNo == null || scannedItemNo.equals("")) {
				itemFound = false;
			}
				
	
			if (!itemFound) {
				throw new Exception(" Scan/Enter a Valid Product ID");
			}
		}
	
	} catch (Exception e) {
		throw e;
	}
	return itemFound;
	}

	public String isValidAlternateItemInItemmst(String aPlant, String aItem)
			throws Exception {
		String scannedItemNo = "";
		try {
			if (aItem.length() > 0) {

				boolean itemFound = true;
				_ItemMstDAO.setmLogger(mLogger);
				scannedItemNo = _ItemMstDAO.getItemIdFromAlternate(aPlant,
						aItem);
				
				if (scannedItemNo == null) {
					itemFound = false;
				}
				if (!itemFound) {
					throw new Exception(" Scan/Enter a Valid Product ID");
				}

			}

		} catch (Exception e) {
			throw e;
		}
		return scannedItemNo;
	}

	public String ItemInItemmst(String aPlant, String aItem)
			throws Exception {
		String scannedItemNo = "";
		try {
			if (aItem.length() > 0) {

				boolean itemFound = true;
				_ItemMstDAO.setmLogger(mLogger);
				scannedItemNo = _ItemMstDAO.getItemDesc(aPlant,
						aItem);
				
				if (scannedItemNo == null) {
					itemFound = false;
				}
				if (!itemFound) {
					throw new Exception(" Scan/Enter a Valid Product ID");
				}

			}

		} catch (Exception e) {
			throw e;
		}
		return scannedItemNo;
	}


	public String isValidInvAlternateItemInItemmst(String aPlant, String aItem)
	throws Exception {
	String scannedItemNo = "";
	try {
	if (aItem.length() > 0) {
	
		boolean itemFound = true;
		_ItemMstDAO.setmLogger(mLogger);
		scannedItemNo = _ItemMstDAO.getInvItemIdFromAlternate(aPlant,
				aItem);
		
		if (scannedItemNo == null) {
			itemFound = false;
		}
		if (!itemFound) {
			throw new Exception(" Scan/Enter a Valid Product ID");
		}
	
	}
	
	} catch (Exception e) {
	throw e;
	}
	return scannedItemNo;
	}
	
	 /* getMobileSalesProductList() 
     * Created By Bruhan,June 23 2014, To get Mobile Order product details
     *  ************Modification History*********************************
     *  Bruhan, Aug 20, To include special character handling 
     */
	public String getMobileSales_ItemList(String plant, String item, String itemDesc,String custname,String currency,String cond)
		throws Exception {
		String xmlStr = "";
		ArrayList al = new ArrayList();
	    StrUtils strUtils = new StrUtils();
	    String sCondition="";
		 if (itemDesc.length() > 0 ) {
	        if (itemDesc.indexOf("%") != -1) {
	        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
	        }
	        sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	    }
	itemDesc  =sCondition +cond;
	
	Connection con = null;
	try {
		con = DbBean.getConnection();
		_ItemMstDAO.setmLogger(mLogger);
		String sQry = "select distinct(itemmst.item) item ,itemmst.itemdesc , itemmst.unitprice UNITPRICE ,itemmst.STKUOM UOM,isnull(itemmst.REMARK1,'') REMARK1,itemmst.REMARK2 MANUFACTURER from ["
				+ plant
				+ "_"
				+ "itemmst"
				+ "] itemmst,["
				+ plant
				+ "_"
				+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
				+ plant
				+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
				+ item
				+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
				+ " ORDER BY itemmst.item, itemmst.itemdesc ";
		this.mLogger.query(true, sQry);
		String OBDiscount="",convertedcost="",calAmount="";
		float price=0,discount=0;
		
		al = _ItemMstDAO.selectData(con, sQry);
		if (al.size() > 0) {
		    String trantype="";
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ItemDetails total='"+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
				convertedcost = new DOUtil().getMobileSalesConvertedUnitCostForProduct(plant,"",(String) map.get("item"),currency);  
				OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,custname,(String) map.get("item"),"PDAOB");     
				xmlStr += XMLUtils.getXMLNode("itemdesc",   StrUtils.replaceCharacters2SendPDA((String)map.get("itemdesc")));
				
				xmlStr += XMLUtils.getXMLNode("uom",  (String) map.get("UOM"));
				xmlStr += XMLUtils.getXMLNode("remark1",  StrUtils.replaceCharacters2SendPDA((String) map.get("REMARK1")));
				xmlStr += XMLUtils.getXMLNode("manufacturer",  StrUtils.replaceCharacters2SendPDA((String) map.get("MANUFACTURER")));
				//calculate outbound order discount
				
				if(OBDiscount=="" || OBDiscount =="0" || OBDiscount=="0.00")
				{
					xmlStr += XMLUtils.getXMLNode("unitprice",  convertedcost);
										
				}
				else
				{
					int plusIndex = OBDiscount.indexOf("%");
	                 if (plusIndex != -1) {
	                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	                	 	String uprice=convertedcost;
	    					discount = (Float.parseFloat(uprice) *  Float.parseFloat(OBDiscount)/100);
	    					price = (Float.parseFloat(uprice)-discount);
	    					calAmount = String.format("%.2f", price);
	    				
	                     }
	                 else{
	                	 	price = Float.parseFloat(OBDiscount);
	                	 	calAmount = String.format("%.2f", price);
	                 }
						xmlStr += XMLUtils.getXMLNode("unitprice",  calAmount);
						//String lprice=(String) map.get("UNITPRICE");
						
				}
				//calculate outbound order discount end
				float flistprice=Float.parseFloat(convertedcost);
				xmlStr += XMLUtils.getXMLNode("listprice",  String.format("%.2f",flistprice));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("ItemDetails");
		}	
			
	} catch (Exception e) {
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return xmlStr;
	}
	public String getMobileSales_ItemList_storentrack(String plant, String item, String itemDesc,String custname,String currency,String cond)
			throws Exception {
			String xmlStr = "";
			ArrayList al = new ArrayList();
		    StrUtils strUtils = new StrUtils();
		    String sCondition="";
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		    }
		itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "select distinct(itemmst.item) item ,itemmst.itemdesc , itemmst.unitprice UNITPRICE ,itemmst.STKUOM UOM,itemmst.REMARK1 REMARK1,itemmst.REMARK2 MANUFACTURER from ["
					+ plant
					+ "_"
					+ "itemmst"
					+ "] itemmst,["
					+ plant
					+ "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
					+ plant
					+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
					+ item
					+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
					+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			this.mLogger.query(true, sQry);
			String OBDiscount="",convertedcost="",calAmount="";
			float price=0,discount=0;
			
			al = _ItemMstDAO.selectData(con, sQry);
			if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemdesc",   StrUtils.replaceCharacters2SendPDA((String)map.get("itemdesc")));
							
					//calculate outbound order discount
					//xmlStr += XMLUtils.getXMLNode("listprice",  (String) map.get("UNITPRICE"));
					convertedcost = new DOUtil().getMobileSalesConvertedUnitCostForProduct(plant,"",(String) map.get("item"),currency);  
					OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,custname,(String) map.get("item"),"PDAOB");     
					if(OBDiscount=="" || OBDiscount =="0" || OBDiscount=="0.00")
					{
						xmlStr += XMLUtils.getXMLNode("unitprice",  convertedcost);
						
						
					}
					else
					{
						String uprice=convertedcost;
						discount = (Float.parseFloat(uprice) *  Float.parseFloat(OBDiscount)/100);
						price = (Float.parseFloat(uprice)-discount);
						calAmount = String.format("%.2f", price);
						xmlStr += XMLUtils.getXMLNode("unitprice",  calAmount);
											
					}
					//calculate outbound order discount end
					
					xmlStr += XMLUtils.getXMLNode("uom",  (String) map.get("UOM"));
					xmlStr += XMLUtils.getXMLNode("remark1",  StrUtils.replaceCharacters2SendPDA((String) map.get("REMARK1")));
					xmlStr += XMLUtils.getXMLNode("manufacturer",  StrUtils.replaceCharacters2SendPDA((String) map.get("MANUFACTURER")));
			    	xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}	
				
		} catch (Exception e) {
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return xmlStr;
		}
	public ArrayList getDOItemList(String plant, String aItem, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
        StrUtils strUtils = new StrUtils();
		Connection con = null;
		try {
			con = DbBean.getConnection();
			    _ItemMstDAO.setmLogger(mLogger);
		      String sCondition = " WHERE IM.ITEM  = '"+ aItem.toUpperCase() + "'";
		        String sQry = "SELECT  IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,isnull(cast(STKQTY as decimal(18,3)),0) STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST,isnull(MINSPRICE,'0') MINSPRICE, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1, isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID ,PRD_BRAND_ID,isnull(ITEM_LOC,'')ITEM_LOC, "
		                                    +" ISNULL(cast(MAXSTKQTY as decimal(18,3)),0) MAXSTKQTY,"
		                                    + "  ISNULL((select ISNULL(SUM(QTY),0) from " + plant + "_INVMST where ITEM=IM.ITEM  group by item),0) STOCKONHAND, "
		                                    + "  ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYPICK),0) from [" + plant + "_DODET] where ITEM=IM.ITEM   and PICKSTATUS <>'C' group by item),0) OUTGOINGQTY FROM"
		        		                    + "["
		                                    + plant
		                                    + "_"
		                                    + "ITEMMST "
		                                    + "] as IM "
		                                    + sCondition;
		this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public String isPDAAddProductValidAlternateItemInItemmst(String aPlant, String aItem)
			throws Exception {
		String scannedItemNo = "";
		//try {
			if (aItem.length() > 0) {

				boolean itemFound = true;
				_ItemMstDAO.setmLogger(mLogger);
				scannedItemNo = _ItemMstDAO.getItemIdFromAlternate(aPlant,
						aItem);
				
				if (scannedItemNo == null) {
					itemFound = false;
				}
				/*if (!itemFound) {
					throw new Exception(" Scan/Enter a Valid Product ID");
				}*/

			}

		/*} catch (Exception e) {
			throw e;
		}*/
		return scannedItemNo;
	}
	
	public String load_maxplus_printing_item_details(String plant, String item, String itemDesc,String cond)
			throws Exception {
			String xmlStr = "";
			ArrayList al = new ArrayList();
		    StrUtils strUtils = new StrUtils();
		    String sCondition="";
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		    }
		itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "select distinct(itemmst.item) item ,itemmst.itemdesc,itemmst.stkuom as uom from ["
					+ plant
					+ "_"
					+ "itemmst"
					+ "] itemmst,["
					+ plant
					+ "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
					+ plant
					+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
					+ item
					+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
					+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			this.mLogger.query(true, sQry);
			String OBDiscount="",convertedcost="",calAmount="";
			float price=0,discount=0;
			
			al = _ItemMstDAO.selectData(con, sQry);
			if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map.get("item"));
					xmlStr += XMLUtils.getXMLNode("itemdesc",   StrUtils.replaceCharacters2SendPDA((String)map.get("itemdesc")));
					xmlStr += XMLUtils.getXMLNode("uom",  (String) map.get("uom"));
		
			    	xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}	
				
		} catch (Exception e) {
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return xmlStr;
		}
	
	public String load_maxplus_printing_batch_details(String plant, String item, String itemDesc,String batch, String cond)
			throws Exception {
			String xmlStr = "";
			ArrayList al = new ArrayList();
		    StrUtils strUtils = new StrUtils();
		    String sCondition="";
			 if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
		        sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
		    }
		itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "select _inv.item as ITEM,_inv.userfld4 as BATCH ,_item.itemdesc as ITEMDESC from  ["
					+ plant
					+ "_"
					+ "invmst"
					+ "] _inv,["
					+ plant
					+ "_"
					+ "itemmst]  _item  where _inv.item=_item.item and _inv.qty > 0" 
					+ " and _inv.item like '"
					+ item
					+ "%' and _inv.userfld4 like '"
					+ batch
					+ "%'";
			this.mLogger.query(true, sQry);
			String OBDiscount="",convertedcost="",calAmount="";
			float price=0,discount=0;
			
			al = _ItemMstDAO.selectData(con, sQry);
			if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ITEM", (String) map.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("BATCH",  (String) map.get("BATCH"));
					xmlStr += XMLUtils.getXMLNode("ITEMDESC",   StrUtils.replaceCharacters2SendPDA((String)map.get("ITEMDESC")));
					
		
			    	xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}	
				
		} catch (Exception e) {
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return xmlStr;
		}

	/**
	 * Provides list of products that are getting expired in the given period
	 * 
	 * @param plant
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList getExpiringProducts(String plant, String fromDate, String toDate) throws Exception {
		ArrayList al = null;
		try {
			String aQuery = "select I.ITEM AS PRODUCT, ISNULL(I.ITEM_LOC,'') AS LOCATION, B.USERFLD4 AS BATCH, B.EXPIREDATE AS EXPIRY_DATE, B.QTY AS QUANTITY from ["
					+ plant + "_ITEMMST] AS I INNER JOIN [" + plant
					+ "_INVMST] AS B ON I.ITEM = B.ITEM where CONVERT(DATETIME, B.EXPIREDATE, 103) between '" + fromDate + "' and '" + toDate + "'";

			al = _ItemMstDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return al;
	}
	public ArrayList getMasterProductList(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE AIM.ALTERNATE_ITEM_NAME LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT ";
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,CATLOGPATH,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,"
					 + " isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1, "
					 + " isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID,ISNULL(PRD_BRAND_ID,'') BRAND,ISNULL(IM.VINNO,'') VINNO,ISNULL(IM.MODEL,'') MODEL,ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(INVENTORYUOM,'') as INVENTORYUOM FROM "
                     + "["
                     + plant
                     + "_"
                     + "ITEMMST "
                     + "] as IM, "
                     + "["
                     + plant
                     + "_ALTERNATE_ITEM_MAPPING] as AIM "
                     + sCondition
                     + cond
                     + " ORDER BY IM.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getMasterProductListByDesc(String itemDesc, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.ITEMDESC LIKE '"
                        + itemDesc
                        + "%' AND AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT ";
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,ISACTIVE,"
					 + " isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(USERFLD1,'N')USERFLD1, "
					 + " isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID,ISNULL(PRD_BRAND_ID,'') BRAND,ISNULL(IM.VINNO,'') VINNO,ISNULL(IM.MODEL,'') MODEL,ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(INVENTORYUOM,'') as INVENTORYUOM FROM "
                     + "["
                     + plant
                     + "_"
                     + "ITEMMST "
                     + "] as IM, "
                     + "["
                     + plant
                     + "_ALTERNATE_ITEM_MAPPING] as AIM "
                     + sCondition
                     + cond
                     + " ORDER BY IM.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getMasterProductListByItem(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "select abim.ITEM,abim.PRD_BRAND_ID,im.ITEMDESC,im.VINNO,im.MODEL,abim.ALTERNATE_ITEM_NAME from ["+plant+"_ALTERNATE_BRAND_ITEM_MAPPING] abim join ["+plant+"_ITEMMST] im on abim.ITEM=im.ITEM where im.ITEM=RTRIM('"+aItem+"')"
                     + cond;
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getAltProductListByItem(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "select abim.ITEM,abim.ALTERNATE_ITEM_BRAND_ID as PRD_BRAND_ID,im.ITEMDESC,im.VINNO,im.MODEL,abim.ALTERNATE_ITEM_NAME,im.CATLOGPATH from ["+plant+"_ALTERNATE_BRAND_ITEM_MAPPING] abim join ["+plant+"_ITEMMST] im on abim.ALTERNATE_ITEM_NAME=im.ITEM where im.ITEM='"+aItem+"'"
                     + cond;
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public boolean isValidAlternateBrand(String aPlant, String aItem)
			throws Exception {
		boolean itemFound = true;
		

			if (aItem.length() > 0) {

				
				_ItemMstDAO.setmLogger(mLogger);
				String scannedItemNo = _ItemMstDAO.getItemIdFromAlternate(aPlant, aItem);
				if (scannedItemNo == null) {
					itemFound = false;
				}
					

			
			}

		
		return itemFound;
	}
	
	public ArrayList getAlternateBrandProductList(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.ITEM LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.ITEM ITEM ,ISNULL(IM.PRD_BRAND_ID,'') PRD_BRAND_ID, "
					 + " ISNULL(ALTERNATE_ITEM_NAME,'') ALTERNATE_ITEM_NAME, ISNULL(ALTERNATE_ITEM_BRAND_ID,'') ALTERNATE_ITEM_BRAND_ID FROM "
                     + "["
                     + plant
                     + "_"
                     + "ALTERNATE_BRAND_ITEM_MAPPING "
                     + "] as IM "
                     + sCondition
                     + cond
                     + " ORDER BY IM.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getItemUOMCOSTList(String plant,String aPono, String item, String itemDesc,String uom,String type,String disc,String minprs,String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
		
	   
                String sCondition="";
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	    itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry="";
			if(type.equalsIgnoreCase("Purchase"))
			{
			sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
					+ " THEN (case when PURCHASEUOM='"+uom+"' then COST else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end) "
					+ " ELSE (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
					+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
					+ " THEN (case when PURCHASEUOM='"+uom+"' then COST else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO ='"+aPono+"')) "
					+ " ELSE ((COST*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
					+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
					+ " THEN (case when PURCHASEUOM='"+uom+"' then COST else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO ='"+aPono+"'))  "
					+ " ELSE ((COST*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
					+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "  
					+ " THEN (case when PURCHASEUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO ='"+aPono+"'))  " 
					+ " ELSE (("+disc+"*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, 1 as MinPriceWTC "
					+ "from [" 
					+ plant
					+ "_"
					+ "itemmst"
					+ "] itemmst,["
					+ plant
					+ "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
					+ plant
					+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
					+ item
					+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
					+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			else if(type.equalsIgnoreCase("Sales"))
			{
				/*sQry = "select  ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice as UNITPRICE, "
						+ " CAST(( ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ "  (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))  as ConvertedUnitCostWTC "*/
				sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) "
						+ " ELSE (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"')) "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))  "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
						+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))  "
						+ " ELSE (("+disc+"*Cast((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"')) as decimal(18,6))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, "
						+ " CASE WHEN MINSPRICE > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then MINSPRICE else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (MINSPRICE / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))  "
						+ " ELSE ((MINSPRICE*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else MINSPRICE end as MinPriceWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			else if(type.equalsIgnoreCase("SalesEstimate"))
			{
				/*sQry = "select  ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice as UNITPRICE, "
						+ " CAST(( ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ "  (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))  as ConvertedUnitCostWTC "*/
				sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) "
						+ " ELSE (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"')) "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"'))  "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
						+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"'))  "
						+ " ELSE (("+disc+"*cast((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"')) as decimal(18,6))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, "
						+ " CASE WHEN MINSPRICE > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then MINSPRICE else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (MINSPRICE / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"'))  "
						+ " ELSE ((MINSPRICE*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_ESTHDR WHERE ESTNO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else MINSPRICE end as MinPriceWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			else if(type.equalsIgnoreCase("Rental"))
			{
			sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when RENTALUOM='"+uom+"' then RentalPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (RentalPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1))) end) "
						+ " ELSE (RentalPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when RENTALUOM='"+uom+"' then RentalPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (RentalPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_LOANHDR WHERE ORDNO ='"+aPono+"')) "
						+ " ELSE ((RentalPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_LOANHDR WHERE ORDNO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when RENTALUOM='"+uom+"' then RentalPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (RentalPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_LOANHDR WHERE ORDNO ='"+aPono+"'))  "
						+ " ELSE ((RentalPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_LOANHDR WHERE ORDNO ='"+aPono+"'))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=RENTALUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			else if(type.equalsIgnoreCase("DirectTax"))
			{
				sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) "
						+ " ELSE (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"') "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')  "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')  "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UOMQTY, "
						+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')  "
						+ " ELSE (("+disc+"*cast((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')as decimal(18,6))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, "
						+ " CASE WHEN MINSPRICE > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then MINSPRICE else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (MINSPRICE / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')  "
						+ " ELSE ((MINSPRICE*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+aPono+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else MINSPRICE end as MinPriceWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getItemUOMCOSTListcurrency(String plant,String currency, String item, String itemDesc,String uom,String type,String disc,String minprs,String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
		
	   
                String sCondition="";
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	    itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry="";
			if(type.equalsIgnoreCase("Purchase"))
			{
			sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
					+ " THEN (case when PURCHASEUOM='"+uom+"' then COST else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end) "
					+ " ELSE (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
					+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
					+ " THEN (case when PURCHASEUOM='"+uom+"' then COST else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"') "
					+ " ELSE ((COST*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
					+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
					+ " THEN (case when PURCHASEUOM='"+uom+"' then COST else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (COST / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')  "
					+ " ELSE ((COST*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
					+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "  
					+ " THEN (case when PURCHASEUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')  " 
					+ " ELSE (("+disc+"*(SELECT CAST(CURRENCYUSEQT as float )as CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=PURCHASEUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, 1 as MinPriceWTC "
					+ "from [" 
					+ plant
					+ "_"
					+ "itemmst"
					+ "] itemmst,["
					+ plant
					+ "_"
					+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
					+ plant
					+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
					+ item
					+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
					+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			else if(type.equalsIgnoreCase("Sales"))
			{
				/*sQry = "select  ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice as UNITPRICE, "
						+ " CAST(( ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ "  (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))  as ConvertedUnitCostWTC "*/
				sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) "
						+ " ELSE (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"') "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID="+currency+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')  "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
						+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')  "
						+ " ELSE (("+disc+"*Cast((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"') as decimal(18,6))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, "
						+ " CASE WHEN MINSPRICE > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then MINSPRICE else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (MINSPRICE / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')  "
						+ " ELSE ((MINSPRICE*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currency+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else MINSPRICE end as MinPriceWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getProductDetailsWithStockonHand(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE AIM.ALTERNATE_ITEM_NAME LIKE '"
                        + aItem.toUpperCase()
                        + "%' OR ITEMDESC LIKE '" +aItem.toUpperCase()
                		+ "%' AND AIM.PLANT = IM.PLANT ";
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.ITEM ITEM ,ITEMDESC,ITEMTYPE,PRD_BRAND_ID,VINNO,MODEL,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,PRD_CLS_ID,IM.ISACTIVE,"
					 + " isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST, isnull(DISCOUNT,'0') DISCOUNT,ISNULL(IM.USERFLD1,'N')USERFLD1, "
					 + " isnull(NONSTKFLAG,'N') NONSTKFLAG, isnull(NONSTKTYPEID,'') NONSTKTYPEID,ISNULL(PRD_BRAND_ID,'') BRAND,ISNULL(IM.VINNO,'') VINNO,ISNULL(IM.MODEL,'') MODEL,ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(INVENTORYUOM,'') as INVENTORYUOM,SUM(ISNULL(INVM.QTY,0)) AS INVQTY,ISNULL(CATLOGPATH,'') AS CATLOGPATH,ISNULL(VENDNO,'') VENDNO,ISNULL(PRD_DEPT_ID,'') PRD_DEPT_ID FROM "
                     + "["
                     + plant
                     + "_"
                     + "ITEMMST "
                     + "] as IM JOIN "
                     + "["
                     + plant
                     + "_ALTERNATE_ITEM_MAPPING] as AIM ON AIM.ITEM = IM.ITEM "
                     +" LEFT JOIN ["+ plant + "_INVMST] as INVM " + "ON IM.ITEM = INVM.ITEM "
                     + sCondition
                     + cond
                     +" GROUP BY IM.ITEM ,ITEMDESC,ITEMTYPE,STKUOM,REMARK1,REMARK2,REMARK3,REMARK4,STKQTY,ASSET,"
                     + "PRD_CLS_ID,IM.ISACTIVE,UNITPRICE,COST,DISCOUNT,IM.USERFLD1,NONSTKFLAG,NONSTKTYPEID,PRD_BRAND_ID,"
                     + "IM.VINNO,IM.MODEL,SALESUOM,PURCHASEUOM,INVENTORYUOM,CATLOGPATH,VENDNO,PRD_DEPT_ID"
                     + " ORDER BY IM.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getProductDetailsAutoSuggestion(String aItem, String plant, String cond)//By Azees-29.08.22 FOR Purchase
			throws Exception {
		ArrayList listItem = new ArrayList();
		StrUtils strUtils = new StrUtils();
		String sCondition1 = "WHERE IM.ALTERNATE_ITEM_NAME LIKE '%"
				+ aItem.toUpperCase()
				+ "%' OR IM.ITEMDESC LIKE '%" +aItem.toUpperCase()+"%' ";
				//+ "%' AND AIM.PLANT = IM.PLANT AND IM.ISACTIVE='Y' ";
		String sCondition = "WHERE AIM.PLANT = IM.PLANT AND IM.ISACTIVE='Y' ";
		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			
			//Optimize Query fix slow --Azees_21_8_24
			String sQry = "WITH A AS ( SELECT AIM.ALTERNATE_ITEM_NAME, IM.ITEM, IM.ITEMDESC,ISNULL(IM.UNITPRICE, '0') AS UNITPRICE, ISNULL(IM.COST, '0') AS COST,"
			  + "IM.NONSTKFLAG, ISNULL(IM.LOC_ID, '') AS LOC_ID, IM.PRD_CLS_ID,ISNULL(IM.SALESUOM, '') AS SALESUOM, ISNULL(IM.PURCHASEUOM, '') AS PURCHASEUOM," 
			  + "ISNULL(IM.CATLOGPATH, '') AS CATLOGPATH, ISNULL(IM.HSCODE, 0) AS HSCODE, IM.MODEL "
			  + "FROM ["+ plant+"_ITEMMST] AS IM INNER JOIN ["+ plant+"_ALTERNATE_ITEM_MAPPING] AS AIM ON AIM.ITEM = IM.ITEM AND "
			  + "AIM.PLANT = IM.PLANT WHERE IM.ISACTIVE = 'Y' "
			  + cond
			  + ") SELECT IM.*, ISNULL(SUM(INVM.QTY), 0) AS INVQTY, ISNULL(UOM1.QPUOM, 0) AS PURCHASEUOMQTY,"
			  + "ISNULL(UOM2.QPUOM, 0) AS SALESUOMQTY FROM A AS IM LEFT JOIN ["+ plant+"_INVMST] AS INVM ON IM.ITEM = INVM.ITEM LEFT JOIN "
			  + "["+ plant+"_UOM] AS UOM1 ON IM.PURCHASEUOM = UOM1.UOM LEFT JOIN ["+ plant+"_UOM] AS UOM2 ON IM.SALESUOM = UOM2.UOM "
			  + sCondition1
			  + "GROUP BY IM.ALTERNATE_ITEM_NAME, IM.ITEM, IM.ITEMDESC, IM.UNITPRICE, IM.COST,IM.NONSTKFLAG, IM.LOC_ID, IM.PRD_CLS_ID, IM.SALESUOM, "
			  + "IM.PURCHASEUOM,IM.CATLOGPATH, IM.HSCODE, IM.MODEL, UOM1.QPUOM, UOM2.QPUOM ORDER BY IM.ITEM";
			 
			
			/*String sQry = "WITH A AS ( "
					+ "SELECT DISTINCT AIM.ALTERNATE_ITEM_NAME,IM.ITEM,ITEMDESC,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST,NONSTKFLAG,ISNULL(LOC_ID,'') LOC_ID,PRD_CLS_ID,"
					//+ "ISNULL((SELECT SUM(ISNULL(INVM.QTY,0)) FROM ["+ plant+"_INVMST] as INVM WHERE IM.ITEM = INVM.ITEM ),0) AS INVQTY,MODEL,"
					//+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.PURCHASEUOM = INVM.UOM ),0) AS PURCHASEUOMQTY,"
					//+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.SALESUOM = INVM.UOM ),0) AS SALESUOMQTY,"
					+ "ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(CATLOGPATH,'') AS CATLOGPATH,ISNULL(IM.HSCODE,0) AS HSCODE,MODEL FROM "
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST "
					+ "] as IM JOIN "
					+ "["
					+ plant
					+ "_ALTERNATE_ITEM_MAPPING] as AIM ON AIM.ITEM = IM.ITEM "
					+ sCondition
					+ cond
					+" ) SELECT *,"
//					+ "ISNULL((SELECT SUM(ISNULL(INVM.QTY,0)) FROM ["+ plant+"_INVMST] as INVM WHERE IM.ITEM = INVM.ITEM ),0) AS INVQTY,MODEL,"
					+ "ISNULL((SELECT SUM(ISNULL(INVM.QTY,0)) FROM ["+ plant+"_INVMST] as INVM WHERE IM.ITEM = INVM.ITEM ),0) AS INVQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.PURCHASEUOM = INVM.UOM ),0) AS PURCHASEUOMQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.SALESUOM = INVM.UOM ),0) AS SALESUOMQTY"
					+ " FROM A AS IM "
					+ sCondition1
					+ " ORDER BY IM.ITEM ";*/
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getmultiPoProductDetailsAutoSuggestion(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
		StrUtils strUtils = new StrUtils();
		String sCondition1 = "WHERE IM.ITEM LIKE '%" +aItem.toUpperCase()+"%' ";
		String sCondition = "WHERE AIM.PLANT = IM.PLANT AND IM.ISACTIVE='Y' ";
		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			
			String sQry = "WITH A AS ( SELECT IM.ITEM, IM.ITEMDESC,IM.VENDNO,(select VNAME from "+ plant+"_vendmst V WHERE V.VENDNO=IM.VENDNO) as VNAME,(select CURRENCY_ID from "+ plant+"_vendmst V WHERE V.VENDNO=IM.VENDNO)  as CURRENCY, "
					+ "(select CURRENCYUSEQT from "+ plant+"_CURRENCYMST C join "+ plant+"_vendmst V on C.CURRENCYID=V.CURRENCY_ID where IM.VENDNO=V.VENDNO) as EQCUR,ISNULL(IM.UNITPRICE, '0') AS UNITPRICE, ISNULL(IM.COST, '0') AS COST, " 
					+ "ISNULL(IM.SALESUOM, '') AS SALESUOM, ISNULL(IM.PURCHASEUOM, '') AS PURCHASEUOM "
					+ "FROM ["+ plant+"_ITEMMST] AS IM INNER JOIN ["+ plant+"_ALTERNATE_ITEM_MAPPING] AS AIM ON AIM.ITEM = IM.ITEM AND AIM.PLANT = IM.PLANT WHERE IM.ISACTIVE = 'Y' "
					+ cond
					+ ") SELECT IM.*, ISNULL(SUM(INVM.QTY), 0) AS INVQTY, ISNULL(UOM1.QPUOM, 0) AS PURCHASEUOMQTY,"
					+ "ISNULL(UOM2.QPUOM, 0) AS SALESUOMQTY FROM A AS IM LEFT JOIN ["+ plant+"_INVMST] AS INVM ON IM.ITEM = INVM.ITEM LEFT JOIN "
					+ "["+ plant+"_UOM] AS UOM1 ON IM.PURCHASEUOM = UOM1.UOM LEFT JOIN ["+ plant+"_UOM] AS UOM2 ON IM.SALESUOM = UOM2.UOM "
					+ sCondition1
					+ " GROUP BY  IM.ITEM, IM.ITEMDESC,IM.VENDNO, IM.UNITPRICE, IM.COST,IM.SALESUOM, IM.PURCHASEUOM,VNAME, CURRENCY,EQCUR,UOM1.QPUOM, UOM2.QPUOM ORDER BY IM.ITEM";
			
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}

	public ArrayList getProductDetailsAutoSuggestionReport(String aItem, String plant, String cond)//By Azees-03.06.24 FOR Report
			throws Exception {
		ArrayList listItem = new ArrayList();
		StrUtils strUtils = new StrUtils();
		String sCondition1 = "WHERE IM.ALTERNATE_ITEM_NAME LIKE '%"
				+ aItem.toUpperCase()
				+ "%' OR IM.ITEMDESC LIKE '%" +aItem.toUpperCase()+"%' ";
		String sCondition = "WHERE AIM.PLANT = IM.PLANT ";
		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "WITH A AS ( "
					+ "SELECT DISTINCT AIM.ALTERNATE_ITEM_NAME,IM.ITEM,ITEMDESC,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST,NONSTKFLAG,PRD_CLS_ID,"
					+ "ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(CATLOGPATH,'') AS CATLOGPATH,MODEL FROM "
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST "
					+ "] as IM JOIN "
					+ "["
					+ plant
					+ "_ALTERNATE_ITEM_MAPPING] as AIM ON AIM.ITEM = IM.ITEM "
					+ sCondition
					+ cond
					+" ) SELECT *,"
+ "ISNULL((SELECT SUM(ISNULL(INVM.QTY,0)) FROM ["+ plant+"_INVMST] as INVM WHERE IM.ITEM = INVM.ITEM ),0) AS INVQTY,"
+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.PURCHASEUOM = INVM.UOM ),0) AS PURCHASEUOMQTY,"
+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.SALESUOM = INVM.UOM ),0) AS SALESUOMQTY"
+ " FROM A AS IM "
+ sCondition1
+ " ORDER BY IM.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	

	public ArrayList getProductDetailSuggestion(String aItem, String plant, String cond)//By Azees-29.08.22 FOR Purchase
			throws Exception {
		ArrayList listItem = new ArrayList();
		StrUtils strUtils = new StrUtils();
		String sCondition = "WHERE AIM.ALTERNATE_ITEM_NAME LIKE '%"
				+ aItem.toUpperCase()
				+ "%' OR ITEMDESC LIKE '%" +aItem.toUpperCase()
				+ "%' OR PRD_BRAND_ID LIKE '%" +aItem.toUpperCase()
				+ "%' OR PRD_CLS_ID LIKE '%" +aItem.toUpperCase()
				+ "%' AND AIM.PLANT = IM.PLANT AND IM.ISACTIVE='Y' ";
		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "WITH A AS ( "
					+ "SELECT IM.ITEM,ITEMDESC,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST,NONSTKFLAG,MODEL,"
					+ "ISNULL((SELECT SUM(ISNULL(INVM.QTY,0)) FROM ["+ plant+"_INVMST] as INVM WHERE IM.ITEM = INVM.ITEM ),0) AS INVQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.PURCHASEUOM = INVM.UOM ),0) AS PURCHASEUOMQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.SALESUOM = INVM.UOM ),0) AS SALESUOMQTY,"
					+ "ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(PRD_BRAND_ID,'') as PRD_BRAND_ID,ISNULL(PRD_CLS_ID,'') as PRD_CLS_ID,ISNULL(CATLOGPATH,'') AS CATLOGPATH FROM "
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST "
					+ "] as IM JOIN "
					+ "["
					+ plant
					+ "_ALTERNATE_ITEM_MAPPING] as AIM ON AIM.ITEM = IM.ITEM "
					+ sCondition
					+ cond
					+" ) SELECT DISTINCT * FROM A "
					+ " ORDER BY A.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getProductDetailsAutoSuggestionLow(String aItem, String plant, String cond)//By Azees-29.08.22 FOR Purchase
			throws Exception {
		ArrayList listItem = new ArrayList();
		StrUtils strUtils = new StrUtils();
		String sCondition1 = "WHERE AL.ALTERNATE_ITEM_NAME LIKE '%"
				+ aItem.toUpperCase()
				+ "%' OR A.ITEMDESC LIKE '%" +aItem.toUpperCase()+"%' ";
		String sCondition = "WHERE A.ISACTIVE='Y' ";
		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "SELECT A.ITEM,AL.ALTERNATE_ITEM_NAME AS AITEM,A.ITEMDESC,A.SALESUOM,ISNULL(SUM(B.QTY),0) AS INVQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE A.SALESUOM = INVM.UOM ),0) AS SALESUOMQTY from "+ plant+"_ALTERNATE_ITEM_MAPPING AS AL " + 
					"LEFT JOIN "+ plant+"_ITEMMST AS A ON AL.ITEM = A.ITEM LEFT JOIN "+ plant+"_INVMST AS B ON A.ITEM = B.ITEM "
					+ sCondition1
					+ " GROUP BY AL.ID,AL.ALTERNATE_ITEM_NAME,A.ITEM,A.ITEMDESC,B.ITEM,A.SALESUOM ORDER BY A.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getMultiPurchaseProductDetailsAutoSuggestion(String aItem, String plant, String cond)//By Azees-30.08.22 FOR Multi Purchase
			throws Exception {
		ArrayList listItem = new ArrayList();
		StrUtils strUtils = new StrUtils();
		String sCondition = "WHERE (AIM.ALTERNATE_ITEM_NAME LIKE '%"
				+ aItem.toUpperCase()
				+ "%' OR ITEMDESC LIKE '%" +aItem.toUpperCase()
				+ "%') AND AIM.PLANT = IM.PLANT AND IM.ISACTIVE='Y' ";
		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "WITH A AS ( "
					+ "SELECT IM.ITEM,ITEMDESC,isnull(UNITPRICE,'0') UNITPRICE,isnull(COST,'0') COST,ISNULL(IM.VENDNO,'') VENDNO,"
					+ "ISNULL((SELECT SUM(ISNULL(INVM.QTY,0)) FROM ["+ plant+"_INVMST] as INVM WHERE IM.ITEM = INVM.ITEM ),0) AS INVQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.PURCHASEUOM = INVM.UOM ),0) AS PURCHASEUOMQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.QPUOM,0)) FROM ["+ plant+"_UOM] as INVM WHERE IM.SALESUOM = INVM.UOM ),0) AS SALESUOMQTY,"
					+ "ISNULL((SELECT (ISNULL(INVM.VNAME,'')) FROM ["+ plant+"_VENDMST] as INVM WHERE IM.VENDNO = INVM.VENDNO ),'') AS VNAME,"
					+ "ISNULL((SELECT (ISNULL(INVM.TAXTREATMENT,'')) FROM ["+ plant+"_VENDMST] as INVM WHERE IM.VENDNO = INVM.VENDNO ),'') AS TAXTREATMENT,"
					+ "CASE WHEN ISNULL((SELECT (ISNULL(INVM.CURRENCY_ID,'')) FROM ["+ plant+"_VENDMST] as INVM WHERE IM.VENDNO = INVM.VENDNO ),'')='' THEN ISNULL((SELECT (ISNULL(INVM.BASE_CURRENCY,'')) FROM [PLNTMST] as INVM WHERE IM.PLANT = INVM.PLANT ),'') ELSE ISNULL((SELECT (ISNULL(INVM.CURRENCY_ID,'')) FROM ["+ plant+"_VENDMST] as INVM WHERE IM.VENDNO = INVM.VENDNO ),'') END  CURRENCY_ID,"
					+ "ISNULL(SALESUOM,'') as SALESUOM,ISNULL(PURCHASEUOM,'') as PURCHASEUOM,ISNULL(CATLOGPATH,'') AS CATLOGPATH FROM "
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST "
					+ "] as IM JOIN "
					+ "["
					+ plant
					+ "_ALTERNATE_ITEM_MAPPING] as AIM ON AIM.ITEM = IM.ITEM "
					+ sCondition
					+ cond
					+ " ) SELECT DISTINCT A.*,ISNULL(INVM.DISPLAY,'') CURRENCY_DISPLAY,ISNULL(INVM.CURRENCYUSEQT,1) CURRENCYUSEQT"
					+ " FROM A JOIN ["+ plant+"_CURRENCYMST] as INVM ON A.CURRENCY_ID = INVM.CURRENCYID"
					+ " ORDER BY A.ITEM ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public Map GetProductForPurchase(String sItem,String plant) throws Exception {//By Azees-30.08.22 FOR Multi Purchase
        MLogger.log(1, this.getClass() + " GetProductForPurchase()");
        Map m = new HashMap();
        java.sql.Connection con = null;
        try {
                con = DbBean.getConnection();
                StringBuffer sql = new StringBuffer(" SELECT ITEM ,ITEMDESC,isnull(COST,'0') COST,isnull(SALESUOM,'') SALESUOM,isnull(cast(STKQTY as decimal(18,3)),0) STKQTY,isnull(INCPRICE,'0') INCPRICE,ISNULL(CPPI,'') CPPI,isnull(UNITPRICE,'0') UNITPRICE,isnull(MINSPRICE,'0') MINSPRICE,isnull(INCPRICEUNIT,'%') INCPRICEUNIT,CATLOGPATH,PRD_BRAND_ID,isnull(HSCODE,'') HSCODE,isnull(COO,'') COO,");
                sql.append("ISNULL(cast(MAXSTKQTY as decimal(18,3)),0) MAXSTKQTY,ISNULL((select ISNULL(SUM(QTY),0) from " + plant + "_INVMST where ITEM=IM.ITEM  group by item),0) STOCKONHAND,");
                sql.append("ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYPICK),0) from [" + plant + "_DODET] D JOIN [" + plant + "_DOHDR] H ON D.DONO=H.DONO where ITEM=IM.ITEM and H.ORDER_STATUS!='Draft' and PICKSTATUS <>'C' group by item),0) OUTGOINGQTY,");
                sql.append("ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(QTYRC),0) from [" + plant + "_PODET] D JOIN [" + plant + "_POHDR] H ON D.PONO=H.PONO where ITEM=IM.ITEM and H.ORDER_STATUS!='Draft' and LNSTAT <>'C' group by item),0) INCOMINGQTY");
                sql.append(" FROM " + "[" + plant + "_"+ "ITEMMST] IM where plant='"+plant+"' AND ITEM='"+sItem+"'");
                
                this.mLogger.query(true, sql.toString());
                m =new BaseDAO().getRowOfData(con, sql.toString());

        } catch (Exception e) {

                this.mLogger.exception(this.printLog, "", e);
                throw e;
        } finally {
                if (con != null) {
                        DbBean.closeConnection(con);
                }
        }
        return m;

}
	
	public ArrayList getItemUOMCOSTListByCurrency(String plant,String currencyID, String item, String itemDesc,String uom,String type,String disc,String minprs,String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
		
	   
                String sCondition="";
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	    itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry="";
			if(type.equalsIgnoreCase("Sales"))
			{
				/*sQry = "select  ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice as UNITPRICE, "
						+ " CAST(( ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"')) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ "  (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * UnitPrice) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_DOHDR WHERE DONO ='"+aPono+"'))  as ConvertedUnitCostWTC "*/
				sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) "
						+ " ELSE (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then UnitPrice else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (UnitPrice / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE ((UnitPrice*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
						+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE (("+disc+"*Cast((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') as decimal(18,6))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, "
						+ " CASE WHEN MINSPRICE > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then MINSPRICE else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (MINSPRICE / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE ((MINSPRICE*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else MINSPRICE end as MinPriceWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getItemUOMCOSTListByCurrencyItemPrice(String plant,String currencyID, String item, String itemDesc,String uom,String type,String disc,String minprs,String cond,String itemprice)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
		
	   
                String sCondition="";
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	    itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry="";
			if(type.equalsIgnoreCase("Sales"))
			{

				sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then CAST('"+itemprice+"' AS float) else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) "
						+ " ELSE (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when SALESUOM='"+uom+"' then CAST('"+itemprice+"' AS float) else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') "
						+ " ELSE ((CAST('"+itemprice+"' AS float)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then CAST('"+itemprice+"' AS float) else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE ((CAST('"+itemprice+"' AS float)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
						+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE (("+disc+"*Cast((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') as decimal(18,6))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, "
						+ " CASE WHEN MINSPRICE > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then MINSPRICE else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (MINSPRICE / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE ((MINSPRICE*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else MINSPRICE end as MinPriceWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	
	public ArrayList getItemUOMCOSTListByCurrencyItemCost(String plant,String currencyID, String item, String itemDesc,String uom,String type,String disc,String minprs,String cond,String itemprice)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
		
	   
                String sCondition="";
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
	    itemDesc  =sCondition +cond;
		
		Connection con = null;
		try {
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry="";
			if(type.equalsIgnoreCase("Sales"))
			{

				sQry = "select  CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then CAST('"+itemprice+"' AS float) else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) "
						+ " ELSE (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as UNITCOST,"
						+ " CAST(CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1)"
						+ " THEN (case when SALESUOM='"+uom+"' then CAST('"+itemprice+"' AS float) else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') "
						+ " ELSE ((CAST('"+itemprice+"' AS float)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as ConvertedUnitCost,"
						+ " CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then CAST('"+itemprice+"' AS float) else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (CAST('"+itemprice+"' AS float) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE ((CAST('"+itemprice+"' AS float)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end as ConvertedUnitCostWTC, "
						+ " CASE WHEN "+disc+" > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then "+disc+" else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * ("+disc+" / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE (("+disc+"*Cast((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"') as decimal(18,6))) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else "+disc+" end as ConvertedDiscWTC, "
						+ " CASE WHEN MINSPRICE > 0 then (CASE WHEN ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)<ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) "
						+ " THEN (case when SALESUOM='"+uom+"' then MINSPRICE else ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) * (MINSPRICE / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1))) end) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')  "
						+ " ELSE ((MINSPRICE*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST]  WHERE  CURRENCYID='"+currencyID+"')) / (ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM=SALESUOM),1)))*ISNULL((select ISNULL(QPUOM,1) from "+ plant+"_UOM where UOM='"+uom+"'),1) end) else MINSPRICE end as MinPriceWTC "
						+ "from [" 
						+ plant
						+ "_"
						+ "itemmst"
						+ "] itemmst,["
						+ plant
						+ "_"
						+ "ALTERNATE_ITEM_MAPPING] altitem  where  itemmst.plant='"
						+ plant
						+ "' and  altitem.ALTERNATE_ITEM_NAME like '"
						+ item
						+ "%' and altitem.ITEM = itemmst.ITEM and itemmst.ISACTIVE= 'Y'" +itemDesc
						+ " ORDER BY itemmst.item, itemmst.itemdesc ";
			}
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
public ArrayList getProductClassDetailsWithStockonHand(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.PRD_CLS_ID LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.PRD_CLS_ID PRD_CLS_ID ,ISNULL(IM.PRD_CLS_DESC,'') PRD_CLS_DESC FROM "
                     + "["
                     + plant
                     + "_"
                     + "PRD_CLASS_MST "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.PRD_CLS_ID ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getProductTypeDetailsWithStockonHand(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.PRD_TYPE_ID LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.PRD_TYPE_ID PRD_TYPE_ID ,ISNULL(IM.PRD_TYPE_DESC,'') PRD_TYPE_DESC FROM "
                     + "["
                     + plant
                     + "_"
                     + "PRD_TYPE_MST "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.PRD_TYPE_ID ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList getProductBrandDetailsWithStockonHand(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.PRD_BRAND_ID LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
	                 		
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.PRD_BRAND_ID PRD_BRAND_ID ,ISNULL(IM.PRD_BRAND_DESC,'') PRD_BRAND_DESC FROM "
                     + "["
                     + plant
                     + "_"
                     + "PRD_BRAND_MST "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.PRD_BRAND_ID ";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}

	public ArrayList getGRNO(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.GRNO LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.GRNO ,ISNULL(IM.CNAME,'') CNAME FROM "
                     + "["
                     + plant
                     + "_"
                     + "RECVDET "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.GRNO desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getLOC(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.LOC LIKE '"
                        + aItem.toUpperCase()
                        + "%' OR IM.LOCDESC LIKE '%" +aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.LOC ,ISNULL(IM.LOCDESC,'') LOCDESC FROM "
                     + "["
                     + plant
                     + "_"
                     + "LOCMST "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.LOC desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getLOCTYPE(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.LOC_TYPE_ID LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.LOC_TYPE_ID ,ISNULL(IM.LOC_TYPE_DESC,'') LOC_TYPE_DESC FROM "
                     + "["
                     + plant
                     + "_"
                     + "LOC_TYPE_MST "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.LOC_TYPE_ID desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getLOCTYPETWO(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.LOC_TYPE_ID2 LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.LOC_TYPE_ID2 ,ISNULL(IM.LOC_TYPE_DESC2,'') LOC_TYPE_DESC2 FROM "
                     + "["
                     + plant
                     + "_"
                     + "LOC_TYPE_MST2 "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.LOC_TYPE_ID2 desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getLOCTYPETHREE(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
		StrUtils strUtils = new StrUtils();
		String sCondition = "WHERE IM.LOC_TYPE_ID3 LIKE '"
				+ aItem.toUpperCase()
				+ "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			String sQry = "SELECT DISTINCT IM.LOC_TYPE_ID3 ,ISNULL(IM.LOC_TYPE_DESC3,'') LOC_TYPE_DESC3 FROM "
					+ "["
					+ plant
					+ "_"
					+ "LOC_TYPE_MST3 "
					+ "] as IM "
					+ sCondition
					+ " ORDER BY IM.LOC_TYPE_ID3 desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getRSNCODE(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.RSNCODE LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.RSNCODE ,ISNULL(IM.RSNDESC,'') RSNDESC FROM "
                     + "["
                     + plant
                     + "_"
                     + "RSNMST "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.RSNCODE desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getINVOICENO(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.INVOICENO LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.INVOICENO ,ISNULL(IM.CNAME,'') CNAME FROM "
                     + "["
                     + plant
                     + "_"
                     + "SHIPHIS "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.INVOICENO desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	public ArrayList getCURRENCY(String aItem, String plant, String cond)
			throws Exception {
		ArrayList listItem = new ArrayList();
                StrUtils strUtils = new StrUtils();
                String sCondition = "WHERE IM.CURRENCYID LIKE '"
                        + aItem.toUpperCase()
                        + "%' AND IM.PLANT = IM.PLANT ";
		Connection con = null;
		try {
			
			con = DbBean.getConnection();
			_ItemMstDAO.setmLogger(mLogger);
			 String sQry = "SELECT DISTINCT IM.CURRENCYID ,ISNULL(IM.DESCRIPTION,'') DESCRIPTION FROM "
                     + "["
                     + plant
                     + "_"
                     + "CURRENCYMST "
                     + "] as IM "
                     + sCondition
                     + " ORDER BY IM.CURRENCYID desc";
			this.mLogger.query(true, sQry);
			listItem = _ItemMstDAO.selectData(con, sQry);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return listItem;
	}
	
	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {
//		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				sql.append(" AND ");
				conditon = _ItemMstDAO.formCondition(ht);
				sql.append(" " + conditon);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}

			this.mLogger.query(true, sql.toString());
			al = _ItemMstDAO.selectData(con, sql.toString());
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	
}
