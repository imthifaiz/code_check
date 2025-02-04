package com.track.servlet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PrdBrandDAO;
import com.track.dao.PrdClassDAO;
import com.track.dao.PrdDeptDAO;
import com.track.dao.PrdTypeDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.UomDAO;
import com.track.db.object.ParentChildCmpDet;
import com.track.db.util.CatalogUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.ESTUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.POSUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PrdBrandUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdDeptUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.ProductionBomUtil;
import com.track.db.util.UomUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.dao.VendMstDAO;
import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.dao.BannerDAO;
import com.track.dao.CatalogDAO;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustMstDAO;
/**
 * Servlet implementation class ItemMstServlet
 */
public class ItemMstServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ItemMstServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ItemMstServlet_PRINTPLANTMASTERINFO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ItemMstServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	       JSONObject jsonObjectResult = new JSONObject();
	   
	       HttpSession session = request.getSession(false);
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
                
                if (action.equals("PRODUCT_LIST")) {
                   jsonObjectResult = this.getProductList(request);
                }
                if (action.equals("PRODUCT_LIST_BY_COND")) {
                   jsonObjectResult = this.getProductListByCond(request);
                }
                if (action.equals("INV_PRODUCT_LIST")) {
                   jsonObjectResult = this.getProductListFromInv(request);
                }
                if (action.equals("MISC_PRODUCT_LIST")) {
                   jsonObjectResult = this.getMiscProductList(request);
                }
                if (action.equals("GET_PRODUCT_DETAILS")) {
                    jsonObjectResult = this.GetProductDetails(request);
                }
                if (action.equals("GET_PRODUCT_IMG_DETAILS")) {
                	jsonObjectResult = this.GetProductImgDetails(request);
                }
                if (action.equals("GET_PRODUCT_IMG_DETAILS_FOR_BANNER")) {
                	jsonObjectResult = this.GetProductImgDetailsForBanner(request);
                }
                if (action.equals("GET_PRODUCT_AUTO_DETAILS")) { //imthi 1.09.22 FOR Sales
                	jsonObjectResult = this.GetProductAutoDetails(request);
                }
                if (action.equals("GET_AVERAGE_COST_PRODUCT_DETAILS")) {
                    jsonObjectResult = this.GetAverageCostProductDetails(request);
                }
                if (action.equals("GET_AVERAGE_COST_PRODUCT_AUTO_DETAILS")) { //imthi 1.09.22 FOR Sales
                	jsonObjectResult = this.GetAverageCostProductAutoDetails(request);
                }
                if (action.equals("GET_INVOICE_PRODUCT_DETAILS")) {
                    jsonObjectResult = this.GetInvoiceProductDetails(request);
                }
                if (action.equals("GET_INVOICE_PRODUCT_AUTO_DETAILS")) { //imthi 1.09.22 FOR Sales
                	jsonObjectResult = this.GetInvoiceAutoProductDetails(request);
                }
                if (action.equals("GET_PRODUCT_DETAILS_PURCHASE")) {
                    jsonObjectResult = this.GetProductDetailsforpurchase(request);
                }
                if (action.equals("GET_PURCHASE_PRODUCT_DETAILS")) {//By Azees-30.08.22 FOR Purchase
                	jsonObjectResult = this.GetPurchaseProductDetails(request);
                }
                
                if (action.equals("ALT_PRODUCT_DETAILS")) {
                    jsonObjectResult = this.GetAlternateProd(request);
                }
                if (action.equals("ALT_PRODUCT_DETAILS_DESC")) {
                    jsonObjectResult = this.GetAlternateProdByDesc(request);
                }
                if (action.equals("ALT_PRODUCT_DETAILS_ITEM")) {
                    jsonObjectResult = this.GetAlternateProdByItem(request,response);
                }
                
                if (action.equals("VIEW_PROD_SUMMARY")) {
                 String test=   (String)session.getAttribute("isItemDetailsPressed") ;
                System.out.println("test"+test);
                    session.setAttribute("isItemDetailsPressed","") ;
                    session.setAttribute("pItem","") ;
                    session.setAttribute("pItemDesc","") ;
                    session.setAttribute("pprdclsid","") ;
                    session.setAttribute("pItemType","") ;
                    jsonObjectResult = this.getProductsmryDetails(request);
                
                }
                
                if (action.equals("INV_PRODUCT_LIST")) {
                   jsonObjectResult = this.getInventoryProductList(request);
                }
               
	    
                if (action.equals("LOAD_ALTERNATE_ITEM_DETAILS")) {
                	String item = StrUtils.fString(request.getParameter("ITEM")).trim();
                	jsonObjectResult = this.loadAlternateItems(plant, item);
			
                }
		
		 if (action.equals("GET_CUSTOMER_TYPE_LIST")) {
             jsonObjectResult = this.getMutiPriceCutomerTypeList(request);
         }
		 
		 
		 if (action.equals("LOAD_CUSTOMER_DISCOUNT_DETAILS")) {
			 String item = StrUtils.fString(request.getParameter("ITEM")).trim();
             jsonObjectResult = this.loadCustomerDiscountOB(plant,item);
         }
		 
		 if (action.equals("GET_SUPPLIER_LIST")) {
             jsonObjectResult = this.getMutiCostSupplierList(request);
         }
		 
		 if (action.equals("LOAD_SUPPLIER_DISCOUNT_DETAILS")) {
			 String item = StrUtils.fString(request.getParameter("ITEM")).trim();
             jsonObjectResult = this.loadSupplierDiscountIB(plant,item);
         }
		 
		 if (action.equals("LOAD_ITEM_SUPPLIER_DETAILS")) {
			 String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			 jsonObjectResult = this.loadItemSupplier(plant,item);
		 }
		 
		 if (action.equals("LOAD_ADDITIONAL_DETAIL_DETAILS")) {
			 String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			 jsonObjectResult = this.loadAdditionDesc(plant,item);
		 }
		 
		 if (action.equals("LOAD_ADDITIONAL_PRD_DETAILS")) {
			 String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			 jsonObjectResult = this.loadAdditionPrd(plant,item);
		 }
		 
		 if (action.equals("GET_EMPLOYEE_DETAILS")) {
             jsonObjectResult = this.GetEmployeeDetails(request);
         }
		 if (action.equals("LOAD_ALTERNATE_EMPLOYEE_DETAILS")) {
         	String empid = StrUtils.fString(request.getParameter("EMPID")).trim();
         	jsonObjectResult = this.loadAlternateEmployee(plant, empid);
		
         }
		 
		 if (action.equals("PRODUCT_LIST_WITH_INVENTORY_QUANTITY")) {
			 jsonObjectResult = this.validateBatchForPicking(request);
		 }
		 
		 if (action.equals("PRODUCT_UNITPRICE")) {
			 jsonObjectResult = this.GetUNITPRICE(request);
		 }
		 if (action.equals("PRODUCT_UOM")) {
			 jsonObjectResult = this.GetUOM(request);
		 }
		 
		 if (action.equals("GET_LOC")) {
			 jsonObjectResult = this.GetLOC(request);
		 }
		 if (action.equals("PRODUCT_CHECK")) {
			 String item = StrUtils.fString(request.getParameter("ITEM"))
						.trim();
             jsonObjectResult = this.validateProduct(plant, userName,
 					item);
          }
		 if (action.equals("CHECK_NONACTIVE_PRODUCT")) {
			 String item = StrUtils.fString(request.getParameter("ITEM"))
					 .trim();
			 jsonObjectResult = this.ChecknonActiveProduct(plant, item);
		 }
		 if (action.equals("PRODUCT_LIST_WITH_INVENTORY_QUANTITY_MUTIUOM")) {
			 jsonObjectResult = this.validateBatchForPickingMultiUOM(request);
		 }
		 if (action.equals("ADD")) {
             addProduct(request, response);
          }
		 if (action.equals("GET_EQUIVALENT_PRODUCT")) {
			 jsonObjectResult = this.getEquivalentitem(request);
          }
		 if (action.equals("GET_PRODUCT_LIST_AUTO_SUGGESTION")) {//By Azees-29.08.22 FOR Purchase
			 jsonObjectResult = this.getProductListAutoSuggestion(request,"");
          }
		 if (action.equals("GET_MULTI_PRODUCT_LIST_AUTO_SUGGESTION")) {
			 jsonObjectResult = this.getMultiPoProductListAutoSuggestion(request,"");
		 }
		 if (action.equals("GET_MULTI_PRODUCT_QTY")) {
			 jsonObjectResult = this.getMultiQtyProductList(request,"GET_MULTI_PRODUCT_QTY");
		 }
		 if (action.equals("GET_SALESRETURN_QTY_DATA")) {
			 jsonObjectResult = this.getMultiQtyProductList(request,"GET_SALESRETURN_QTY_DATA");
		 }
		 if (action.equals("GET_POS_QTY_DATA")) {
			 jsonObjectResult = this.getMultiQtyProductList(request,"GET_POS_QTY_DATA");
		 }
		 if (action.equals("GET_PURCHASE_QTY_DATA")) {
			 jsonObjectResult = this.getMultiQtyProductList(request,"GET_PURCHASE_QTY_DATA");
		 }
		 if (action.equals("GET_PURCHASERETURN_QTY_DATA")) {
			 jsonObjectResult = this.getMultiQtyProductList(request,"GET_PURCHASERETURN_QTY_DATA");
		 }
		 if (action.equals("GET_SALESRETURN_QTY_DATA")) {
			 jsonObjectResult = this.getMultiQtyProductList(request,"GET_SALESRETURN_QTY_DATA");
		 }
		 if (action.equals("GET_PRODUCT_LIST_AUTO_SUGGESTION_REPORT")) {//By Azees-29.08.22 FOR Purchase
			 jsonObjectResult = this.getProductListAutoSuggestion(request,"REPORT");
		 }
		 if (action.equals("GET_PRODUCT_LIST_SUGGESTION")) {//imti  23-01-2023
			 jsonObjectResult = this.getProductListSuggestion(request);
		 }
		 if (action.equals("GET_MULTIPURCHASE_PRODUCT_LIST_AUTO_SUGGESTION")) {//By Azees-30.08.22 FOR Multi Purchase
			 jsonObjectResult = this.getMultiPurchaseProductListAutoSuggestion(request);
		 }
		 if (action.equals("GET_PRODUCT_LIST_AUTO_SUGGESTION_FOR_ITEM")) {//By Azees-29.08.22 FOR Purchase
			 jsonObjectResult = this.getProductListAutoSuggestionforitem(request);
          }
		 if (action.equals("GET_PRODUCT_LIST_AUTO_SUGGESTION_LOW")) {
			 jsonObjectResult = this.getProductListAutoSuggestionLow(request);
          }
		 if (action.equals("GET_PRODUCT_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getProductListForSuggestion(request);
		 }
		 if (action.equals("GET_BOM_PRODUCT_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getBomProductListForSuggestion(request);
          }
		 if (action.equals("GET_PROCESSING_BOM_PRODUCT_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getProcessingBomProductListForSuggestion(request);
          }
		 if (action.equals("GET_BOM_CHILD_PRODUCT_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getBomChildProductListForSuggestion(request);
          }
		 if (action.equals("GET_INVOICE_PRODUCT_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getInvoiceProductListForSuggestion(request);
          }
		 if (action.equals("GET_INVOICE_PRODUCT_LIST_AUTO_SUGGESTION")) { //By Imthi-31.08.22 FOR SALES
			 jsonObjectResult = this.getinvoiceProductListAutoSuggestion(request);
		 }
		 if (action.equals("GET_PRODUCTCLASS_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getProductClassListForSuggestion(request);
          }
		 if (action.equals("GET_PRODUCTTYPE_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getProductTypeListForSuggestion(request);
          }
		 if (action.equals("GET_PRODUCTBRAND_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getProductBrandListForSuggestion(request);
          }
		 if (action.equals("GET_GRNO_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getGRNOForSuggestion(request);
          }
		 if (action.equals("GET_TRANID_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getTRANIDForSuggestion(request);
		 }
		 if (action.equals("GET_TRANID_STOCK_MOVE_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getTRANIDStockeMoveForSuggestion(request);
		 }
		 if (action.equals("GET_PORECEIPT_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getPO_Receipt_pono_ForSuggestion(request);
		 }
		 if (action.equals("GET_LOC_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getLOCForSuggestion(request);
          }
		 if (action.equals("GET_LOCTYPE_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getLOCTYPEForSuggestion(request);
          }
		 if (action.equals("GET_LOCTYPETWO_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getLOCTYPETWOForSuggestion(request);
          }
		 if (action.equals("GET_LOCTYPETHREE_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getLOCTYPETHREEForSuggestion(request);
		 }
		 if (action.equals("GET_RSNCODE_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getRSNcodeForSuggestion(request);
          }
		 if (action.equals("GET_INVOICENO_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getInvoiceNoForSuggestion(request);
          }
		 if (action.equals("GET_CURRENCY_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getCURRENCYForSuggestion(request);
          }
		 if (action.equals("GET_SALES_PRODUCT_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getSalesProductListForSuggestion(request);
          }
		 if(action.equals("ADD_PRODUCT")) {
			 jsonObjectResult = this.addProduct(request);
		 }
		 if(action.equals("NEXT_SEQUENCE")) {
			 /* To get next running Product sequence */
			 jsonObjectResult = this.getNextSequence(request);
		 }
		 if(action.equals("ADD_ALT_PRODUCT")) {
			 jsonObjectResult = this.addAltProduct(request,response);
		 }
		 if(action.equals("UPDATE_ALT_PRODUCT")) {
			 jsonObjectResult = this.updateAltProduct(request,response);
		 }
		 if(action.equals("GET_ALT_PRODUCT_SUMMARY")) {
			 jsonObjectResult = this.summaryAltProduct(request,response);
		 }
		 else  if (action.equals("NOOFPRODUCT_CHECK")) {
       	  String NOOFINVENTORY = StrUtils.fString((String) request.getSession().getAttribute("NOOFINVENTORY"));
	             jsonObjectResult = this.validatenoofProduct(plant,NOOFINVENTORY);
	          }
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
               
	}
        
        
    private JSONObject getProductList(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
            try {
            
                String plant = StrUtils.fString(request.getParameter("PLANT"))
                               .trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM"))
                               .trim();
                String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC"))
                               .trim();
                String sExtraCond = StrUtils.fString(request.getParameter("COND"))
                               .trim();
                if(sItemDesc.length()>0){
                   sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
                   sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
                   }
                sItemDesc =sItemDesc+sExtraCond ;
              
                List listQry = new ItemUtil().qryItemMstCond(sItem,plant,sItemDesc);
                if (listQry.size() > 0) {
                    for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                                int iIndex = iCnt + 1;
                                Vector vecItem   = (Vector)listQry.get(iCnt);
                                JSONObject resultJsonInt = new JSONObject();
                                resultJsonInt.put("PRODUCT", (String)vecItem.get(0));
                                resultJsonInt.put("DESC", (String)vecItem.get(1));
                                resultJsonInt.put("ITEMTYPE", (String)vecItem.get(2));
                                resultJsonInt.put("UOM", (String)vecItem.get(3));
                                resultJsonInt.put("REMARK1", (String)vecItem.get(4));
                                resultJsonInt.put("REMARK2", (String)vecItem.get(5));
                                resultJsonInt.put("REMARK3",(String)vecItem.get(6));
                                resultJsonInt.put("REMARK4", (String)vecItem.get(7));
                                resultJsonInt.put("STKQTY", (String)vecItem.get(8));
                                resultJsonInt.put("ASSET", (String)vecItem.get(9));
                                resultJsonInt.put("PRD_CLS_ID", (String)vecItem.get(10));
                                resultJsonInt.put("ACTIVE", (String)vecItem.get(11));
                                resultJsonInt.put("PRICE", (String)vecItem.get(12));
                                resultJsonInt.put("COST", (String)vecItem.get(13));
                                resultJsonInt.put("DISCOUNT", (String)vecItem.get(14));
                                resultJsonInt.put("ISPARENT", (String)vecItem.get(15));
                                               
                                jsonArray.add(resultJsonInt);

                        }
                        resultJson.put("items", jsonArray);
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                        resultJsonInt.put("ERROR_CODE", "100");
                        jsonArrayErr.add(resultJsonInt);
                        resultJson.put("errors", jsonArrayErr);
                } else {
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                        resultJsonInt.put("ERROR_CODE", "99");
                        jsonArrayErr.add(resultJsonInt);
                        jsonArray.add("");
                        resultJson.put("items", jsonArray);
                        resultJson.put("errors", jsonArrayErr);
                }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getMiscProductList(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
            ItemMstDAO  _ItemMstDAO  = new  ItemMstDAO();
           _ItemMstDAO.setmLogger(mLogger);
            try {
            
    
                  String PLANT = StrUtils.fString(request.getParameter("PLANT")).trim();
                  String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
                  String DESC = StrUtils.fString(request.getParameter("ITEM_DESC"));
                   if(DESC.length()>0){
                   DESC = new StrUtils().InsertQuotes(DESC);
                   }
              
              
                List listQry = _ItemMstDAO.getMiscItemByWMS(PLANT,ITEMNO,DESC);
                if (listQry.size() > 0) {
                    for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                                int iIndex = iCnt + 1;
                                 Map m=(Map)listQry.get(iCnt);
                                JSONObject resultJsonInt = new JSONObject();
                                resultJsonInt.put("PRODUCT",(String)m.get("item"));
                                resultJsonInt.put("DESC", StrUtils.fString((String)m.get("itemdesc")));
                                resultJsonInt.put("UOM", StrUtils.fString((String)m.get("uom")));
                                jsonArray.add(resultJsonInt);

                        }
                        resultJson.put("items", jsonArray);
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                        resultJsonInt.put("ERROR_CODE", "100");
                        jsonArrayErr.add(resultJsonInt);
                        resultJson.put("errors", jsonArrayErr);
                } else {
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                        resultJsonInt.put("ERROR_CODE", "99");
                        jsonArrayErr.add(resultJsonInt);
                        jsonArray.add("");
                        resultJson.put("items", jsonArray);
                        resultJson.put("errors", jsonArrayErr);
                }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject GetProductAutoDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemMstUtil itemUtil = new ItemMstUtil();
            try {

                String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String doNO = StrUtils.fString(request.getParameter("DONO")).trim();
                String type = StrUtils.fString(request.getParameter("TYPE")).trim();
                
                String currencyID = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
                String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
                String replacePreviousSalesCost = StrUtils.fString(request.getParameter("REPLACEPREVIOUSSALESCOST")).trim();
                
                String convertedcost="";
                String itemprices="";
                String cost="";
                String AODtype="";
                String convertedcosts="";
                String convertedcostwtc="";
				String minSellingconvertedcost="";
                //start code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                ItemMstDAO itemMstDAO = new ItemMstDAO();
                itemMstDAO.setmLogger(mLogger);
                 
                 boolean itemFound = true;
                 String OBDiscount="", estQty = "", avlbQty = "";
                 String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, sItem);
                 if (scannedItemNo == null) {
                         itemFound = false;
                 }
                 else{
                	 sItem = scannedItemNo;
                 }
                 String incprice="",cppi="",purcost=""; //imthi fo SO
               //end code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                 if(type.equalsIgnoreCase("ESTIMATE")){
                	  	/*convertedcost = new ESTUtil().getConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"ESTIMATE");*/
                	  	convertedcost = new ESTUtil().getConvertedUnitCostForProductByCurrency(plant,currencyID,sItem); 
                	 	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
    					minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
                	 	OBDiscount=new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"ESTIMATE");
                	 	Hashtable ht = new Hashtable();
                	 	ht.put("item", sItem);
                	 	ht.put("plant", plant);
                	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
                	 	estQty = (String) m.get("ESTQTY");
                	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
                	 	avlbQty= (String) m.get("AVLBQTY");
                      }
					   else   if(type.equalsIgnoreCase("RENTAL")) {
              	 	convertedcost = new LoanUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
              	 	convertedcostwtc = new LoanUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
              	 	// OBDiscount=new LoanUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"RENTAL");     
               	}else if(type.equalsIgnoreCase("SO")){
               		convertedcost = new DOUtil().getConvertedUnitCostForProductByCurrency(plant,currencyID,sItem); 
            	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
            	 	OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"OUTBOUND");
         			Hashtable ht=new Hashtable<>();
               	 	String extCond="";
               	 	cost = new DoHdrDAO().getUnitCostsalesBasedOnCurIDSelectedForOrderByCurrency(plant,currencyID,sItem); //imthi added to display unitcost
                     if(replacePreviousSalesCost.equalsIgnoreCase("3")) {
                    	 String query="SELECT INCPRICE,CPPI,COST FROM "+plant+"_ITEMMST WHERE ITEM='"+sItem+"'";
                    	 ArrayList arrList = new ItemMstUtil().selectForReport(query,ht,"");
                    	 if (arrList.size() > 0) {
                    		 for (int i=0; i < arrList.size(); i++ ) {
                    			 Map m = (Map) arrList.get(i);
                    			  incprice = (String) m.get("INCPRICE");
                    			  cppi = (String) m.get("CPPI");
                    			  purcost = (String) m.get("COST");
                    		 	}
                    	 	}
                    	 		AODtype = currencyID;
	                    	 if(cppi.equalsIgnoreCase("BYPRICE")) {
	                    		 Double con;
	                    		 Double convl = Double.valueOf(purcost);
	                    		 Double conv2 = Double.valueOf(incprice);
	                    		 con = (convl+conv2);
	                    		 convertedcost = Double.toString(con);
	                    		 itemprices = convertedcost; 
	                    		 convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,convertedcost);
	                    		 incprice = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,incprice); 
	                    	 }else {
	                    		 Double con;
	                    		 Double convl = Double.valueOf(purcost);
	                    		 Double conv2 = Double.valueOf(incprice);
	                    		 Double Sum = ((convl*conv2)/100);
	                    		 con = Sum+convl;
	                    		 convertedcost = Double.toString(con);
	                    		 itemprices = convertedcost; 
	                    		 convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,convertedcost); 
	                    	 }
    	                  }
                     
            	 	
            	 	ht.put("item", sItem);
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	estQty = (String) m.get("ESTQTY");
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	avlbQty= (String) m.get("AVLBQTY");
               	}else{
                	 	convertedcost = new DOUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
                	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	 	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"OUTBOUND");     
                 	}
                 String discounttype="";
                 int plusIndex = OBDiscount.indexOf("%");
                 if (plusIndex != -1) {
                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
                	 	discounttype = "BYPERCENTAGE";
                     }
                            
                 Map arrItem = itemUtil.GetProductForPurchase(sItem,plant);
               	if(arrItem.size()>0){
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
                      	resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
                      	resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
          				resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						resultObjectJson.put("incprice",StrUtils.fString((String)arrItem.get("INCPRICE")));
						resultObjectJson.put("incpriceunit",StrUtils.fString((String)arrItem.get("INCPRICEUNIT")));
						resultObjectJson.put("prdesc",StrUtils.fString((String)arrItem.get("ITEMDESC")));
						resultObjectJson.put("ITEMDESC",StrUtils.fString((String)arrItem.get("ITEMDESC")));
						resultObjectJson.put("ConvertedUnitCost",convertedcost);
						resultObjectJson.put("itemprice",itemprices);
						resultObjectJson.put("ConvertedUnitCosts",cost);//imthi to display unitcost for the item
						resultObjectJson.put("ADDONCOST",incprice); //imthi to display incproce for the item
						resultObjectJson.put("AODTYPE",AODtype); //imthi to display currency what we choosed
                        resultObjectJson.put("outgoingOBDiscount",OBDiscount);
                        resultObjectJson.put("OBDiscountType",discounttype);
                        resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
						resultObjectJson.put("minSellingConvertedUnitCost",minSellingconvertedcost);
						resultObjectJson.put("EstQty",estQty);
						resultObjectJson.put("AvlbQty",avlbQty);
						resultJson.put("result", resultObjectJson);
                       } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    
    private JSONObject GetProductDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemUtil itemUtil = new ItemUtil();
            try {

                String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String doNO = StrUtils.fString(request.getParameter("DONO")).trim();
                String type = StrUtils.fString(request.getParameter("TYPE")).trim();
                
                String currencyID = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
                String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
                String replacePreviousSalesCost = StrUtils.fString(request.getParameter("REPLACEPREVIOUSSALESCOST")).trim();
                
                String convertedcost="";
                String cost="";
                String AODtype="";
                String convertedcosts="";
                String convertedcostwtc="";
				String minSellingconvertedcost="";
                //start code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                ItemMstDAO itemMstDAO = new ItemMstDAO();
                itemMstDAO.setmLogger(mLogger);
                 
                 boolean itemFound = true;
                 String OBDiscount="", estQty = "", avlbQty = "";
                 String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, sItem);
                 if (scannedItemNo == null) {
                         itemFound = false;
                 }
                 else{
                	 sItem = scannedItemNo;
                 }
                 String incprice="",cppi="",purcost=""; //imthi fo SO
               //end code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                 if(type.equalsIgnoreCase("ESTIMATE")){
                	  	/*convertedcost = new ESTUtil().getConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"ESTIMATE");*/
                	  	convertedcost = new ESTUtil().getConvertedUnitCostForProductByCurrency(plant,currencyID,sItem); 
                	 	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
    					minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
                	 	OBDiscount=new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"ESTIMATE");
                	 	Hashtable ht = new Hashtable();
                	 	ht.put("item", sItem);
                	 	ht.put("plant", plant);
                	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
                	 	estQty = (String) m.get("ESTQTY");
                	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
                	 	avlbQty= (String) m.get("AVLBQTY");
                      }
					   else   if(type.equalsIgnoreCase("RENTAL")) {
              	 	convertedcost = new LoanUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
              	 	convertedcostwtc = new LoanUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
              	 	// OBDiscount=new LoanUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"RENTAL");     
               	}else if(type.equalsIgnoreCase("SO")){
               		convertedcost = new DOUtil().getConvertedUnitCostForProductByCurrency(plant,currencyID,sItem); 
            	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
            	 	OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"OUTBOUND");
         			Hashtable ht=new Hashtable<>();
               	 	String extCond="";
                     if(replacePreviousSalesCost.equalsIgnoreCase("3")) {
                    	 cost = new DoHdrDAO().getUnitCostsalesBasedOnCurIDSelectedForOrderByCurrency(plant,currencyID,sItem); //imthi added to display unitcost
                    	 String query="SELECT INCPRICE,CPPI,COST FROM "+plant+"_ITEMMST WHERE ITEM='"+sItem+"'";
                    	 ArrayList arrList = new ItemMstUtil().selectForReport(query,ht,"");
                    	 if (arrList.size() > 0) {
                    		 for (int i=0; i < arrList.size(); i++ ) {
                    			 Map m = (Map) arrList.get(i);
                    			  incprice = (String) m.get("INCPRICE");
                    			  cppi = (String) m.get("CPPI");
                    			  purcost = (String) m.get("COST");
                    		 	}
                    	 	}
                    	 		AODtype = currencyID;
	                    	 if(cppi.equalsIgnoreCase("BYPRICE")) {
	                    		 Double con;
	                    		 Double convl = Double.valueOf(purcost);
	                    		 Double conv2 = Double.valueOf(incprice);
	                    		 con = (convl+conv2);
	                    		 convertedcost = Double.toString(con);
	                    		 convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,convertedcost);
	                    		 incprice = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,incprice); 
	                    	 }else {
	                    		 Double con;
	                    		 Double convl = Double.valueOf(purcost);
	                    		 Double conv2 = Double.valueOf(incprice);
	                    		 Double Sum = ((convl*conv2)/100);
	                    		 con = Sum+convl;
	                    		 convertedcost = Double.toString(con);
	                    		 convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,convertedcost); 
	                    	 }
    	                  }
            	 	ht.put("item", sItem);
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	estQty = (String) m.get("ESTQTY");
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	avlbQty= (String) m.get("AVLBQTY");
               	}else{
                	 	convertedcost = new DOUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
                	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	 	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"OUTBOUND");     
                 	}
                 String discounttype="";
                 int plusIndex = OBDiscount.indexOf("%");
                 if (plusIndex != -1) {
                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
                	 	discounttype = "BYPERCENTAGE";
                     }
                            
                 List listItem = itemUtil.queryItemMstDetails(sItem,plant);
                        Vector arrItem    = (Vector)listItem.get(0);
                        if(arrItem.size()>0){
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        resultObjectJson.put("sItem",(String)arrItem.get(0)); 
                        resultObjectJson.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        resultObjectJson.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
//                        resultObjectJson.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        String PRD_DEP_ID = StrUtils.fString((String)arrItem.get(46));
                        String PRD_CLS_ID = StrUtils.fString((String)arrItem.get(10));
                        String brand = StrUtils.fString((String)arrItem.get(19));
                        String sArtist = StrUtils.fString((String)arrItem.get(2));
						String PRD_DEPT_DESC = "",PRD_CLS_DESC="",PRD_BRAND_DESC="",PRD_TYPE_DESC="";
						
						if(PRD_DEP_ID.length()>0) 
						 { 
							JSONObject deptJson=new PrdDeptDAO().getDeptName(plant, (String) PRD_DEP_ID); 
							PRD_DEPT_DESC = deptJson.getString("PRD_DEPT_DESC"); 
						 }
						
						if(PRD_CLS_ID.length()>0) 
						{
//							JSONObject deptJson=new PrdClassDAO().getprdclsName(plant, (String) PRD_CLS_ID); 
							JSONObject deptJson=new PrdClassDAO().getprdclsNames(plant, (String) PRD_CLS_ID); 
							PRD_CLS_DESC = deptJson.getString("PRD_CLS_DESC"); 
						 }
						 
						if(brand.length()>0) 
						{ 
							JSONObject deptJson=new PrdBrandDAO().getprdbrandName(plant, (String) brand); 
							PRD_BRAND_DESC = deptJson.getString("PRD_BRAND_DESC"); 
						}
						if(sArtist.length()>0) 
						{ 
							JSONObject deptJson=new PrdTypeDAO().getprdtypeName(plant, (String) sArtist); 
							PRD_TYPE_DESC = deptJson.getString("PRD_TYPE_DESC"); 
						}
						
                        resultObjectJson.put("prd_dept_id",PRD_DEP_ID);
                        resultObjectJson.put("prd_dept_desc",PRD_DEPT_DESC);
                        resultObjectJson.put("prd_cls_id",PRD_CLS_ID);
                        resultObjectJson.put("prd_cls_desc",PRD_CLS_DESC);
                        resultObjectJson.put("brand",brand);
                        resultObjectJson.put("prd_brand_desc",PRD_BRAND_DESC);
                        resultObjectJson.put("sArtist",sArtist);
                        resultObjectJson.put("prd_type_desc",PRD_TYPE_DESC);
//                        resultObjectJson.put("DEPT_DISPLAY_ID",StrUtils.fString((String)arrItem.get(43)));
//                        resultObjectJson.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                    //  Start code added by Bruhan for product brand on 11/9/12 
//                        resultObjectJson.put("brand",StrUtils.fString((String)arrItem.get(19)));
                    //  End code added by Bruhan for product brand on 11/9/12 
                        resultObjectJson.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        resultObjectJson.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        resultObjectJson.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        resultObjectJson.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                       /* resultObjectJson.put("price", StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(12))));
                        resultObjectJson.put("cost",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(13))));
                        resultObjectJson.put("minsprice",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(14))));*/
                        resultObjectJson.put("price",StrUtils.fString((String)arrItem.get(12)));
                        resultObjectJson.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        resultObjectJson.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        resultObjectJson.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        resultObjectJson.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        resultObjectJson.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        resultObjectJson.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        resultObjectJson.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                      //Start code added by Bruhan for base currency on Aug 28th 2012.
                        resultObjectJson.put("ConvertedUnitCost",convertedcost);
                      //End code added by Bruhan for base currency on Aug 28th 2012.
                        resultObjectJson.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        resultObjectJson.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        resultObjectJson.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        resultObjectJson.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        resultObjectJson.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        resultObjectJson.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        resultObjectJson.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        resultObjectJson.put("model",StrUtils.fString((String)arrItem.get(30)));
						resultObjectJson.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        resultObjectJson.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        resultObjectJson.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        resultObjectJson.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        resultObjectJson.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        resultObjectJson.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        resultObjectJson.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        resultObjectJson.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						resultObjectJson.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						resultObjectJson.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        resultObjectJson.put("outgoingOBDiscount",OBDiscount);
                        resultObjectJson.put("OBDiscountType",discounttype);
                        resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
						resultObjectJson.put("minSellingConvertedUnitCost",minSellingconvertedcost);
						resultObjectJson.put("EstQty",estQty);
						resultObjectJson.put("AvlbQty",avlbQty);
						
						resultObjectJson.put("ConvertedUnitCosts",cost);//imthi to display unitcost for the item
						resultObjectJson.put("ADDONCOST",incprice); //imthi to display incproce for the item
						resultObjectJson.put("AODTYPE",AODtype); //imthi to display currency what we choosed
						
						resultObjectJson.put("iscompro",StrUtils.fString((String)arrItem.get(42)));
						resultObjectJson.put("cppi",StrUtils.fString((String)arrItem.get(43)));
						resultObjectJson.put("incprice",StrUtils.fString((String)arrItem.get(44)));
						resultObjectJson.put("incpriceunit",StrUtils.fString((String)arrItem.get(45)));
						resultObjectJson.put("ISCHILDCAL",StrUtils.fString((String)arrItem.get(48)));
						resultObjectJson.put("ISPOSDISCOUNT",StrUtils.fString((String)arrItem.get(49)));
						resultObjectJson.put("ISNEWARRIVAL",StrUtils.fString((String)arrItem.get(50)));
						resultObjectJson.put("ISTOPSELLING",StrUtils.fString((String)arrItem.get(51)));
						resultObjectJson.put("dimension",StrUtils.fString((String)arrItem.get(52)));
						resultObjectJson.put("LOC_ID",StrUtils.fString((String)arrItem.get(53)));
						String vendno = StrUtils.fString((String)arrItem.get(47));
						String vendname = "";
						if(vendno.length()>0) {
						JSONObject vendorJson=new VendMstDAO().getVendorName(plant, (String) vendno);
						vendname = vendorJson.getString("VNAME");
						}
						resultObjectJson.put("vendno",vendno);
						resultObjectJson.put("vendname",vendname);
						resultJson.put("result", resultObjectJson);
                       } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    
    private JSONObject GetProductImgDetails(HttpServletRequest request) {
    	JSONObject resultJson = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	JSONArray jsonArrayErr = new JSONArray();
    	try {
    		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
    		String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
    		List listItem =   new CatalogDAO().getAdditionalImg(plant,sItem);
    		int listsize = listItem.size();
    		JSONObject resultObjectJson = new JSONObject();
    		if (listItem.size() > 0) {
    			for (int iCnt =0; iCnt<listItem.size(); iCnt++){
    				Map m=(Map)listItem.get(iCnt);
    				if(((String)m.get("USERTIME1")).equalsIgnoreCase("2")) {
    					resultObjectJson.put("catalogpath1", (String)m.get("CATLOGPATH"));
    				}else if(((String)m.get("USERTIME1")).equalsIgnoreCase("3")) {
    					resultObjectJson.put("catalogpath2", (String)m.get("CATLOGPATH"));
    				}else if(((String)m.get("USERTIME1")).equalsIgnoreCase("4")) {
    					resultObjectJson.put("catalogpath3", (String)m.get("CATLOGPATH"));
    				}else if(((String)m.get("USERTIME1")).equalsIgnoreCase("5")) {
    					resultObjectJson.put("catalogpath4", (String)m.get("CATLOGPATH"));
    				}else if(((String)m.get("USERTIME1")).equalsIgnoreCase("6")) {
    				resultObjectJson.put("catalogpath5", (String)m.get("CATLOGPATH"));
    				}
    			}
    			resultJson.put("result", resultObjectJson);
    			resultJson.put("status", "100");
    		} else {
    			resultJson.put("status", "99");
    		}
    		return resultJson;
    	} catch (Exception daRE) {
    		resultJson.put("status", "99");
    		return resultJson;
    	}
    }
    
    //BannerMst
    private JSONObject GetProductImgDetailsForBanner(HttpServletRequest request) {
    	JSONObject resultJson = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	JSONArray jsonArrayErr = new JSONArray();
    	try {
    		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
    		List listItem =   new BannerDAO().getAdditionalImgForbanner(plant);
    		int listsize = listItem.size();
    		JSONObject resultObjectJson = new JSONObject();
    		if (listItem.size() > 0) {
    			for (int iCnt =0; iCnt<listItem.size(); iCnt++){
    				Map m=(Map)listItem.get(iCnt);
    				if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("1")) {
    					resultObjectJson.put("bannerpath1", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("2")) {
    					resultObjectJson.put("bannerpath2", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("3")) {
    					resultObjectJson.put("bannerpath3", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("4")) {
    					resultObjectJson.put("bannerpath4", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("5")) {
    					resultObjectJson.put("bannerpath5", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("6")) {
    					resultObjectJson.put("bannerpath6", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("7")) {
    					resultObjectJson.put("bannerpath7", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("8")) {
    					resultObjectJson.put("bannerpath8", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("9")) {
    					resultObjectJson.put("bannerpath9", (String)m.get("BANNERPATH"));
    				}else if(((String)m.get("BANNERLNNO")).equalsIgnoreCase("0")) {
    					resultObjectJson.put("bannerpath10", (String)m.get("BANNERPATH"));
    				}
    			}
    			resultJson.put("result", resultObjectJson);
    			resultJson.put("status", "100");
    		} else {
    			resultJson.put("status", "99");
    		}
    		return resultJson;
    	} catch (Exception daRE) {
    		resultJson.put("status", "99");
    		return resultJson;
    	}
    }
    // BannerMst
    
    private JSONObject GetAverageCostProductAutoDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemMstUtil itemUtil = new ItemMstUtil();
            try {

                String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String doNO = StrUtils.fString(request.getParameter("DONO")).trim();
                String type = StrUtils.fString(request.getParameter("TYPE")).trim();
                String uom = StrUtils.fString(request.getParameter("UOM")).trim();
                
                String currencyID = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
                String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
                
                String AODtype="";
                String convertedcost="";
                String convertedcosts="";
                String convertedcostwtc="";
				String minSellingconvertedcost="";
                //start code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                ItemMstDAO itemMstDAO = new ItemMstDAO();
                itemMstDAO.setmLogger(mLogger);
                 
                 boolean itemFound = true;
                 String OBDiscount="", estQty = "", avlbQty = "";
                 String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, sItem);
                 if (scannedItemNo == null) {
                         itemFound = false;
                 }
                 else{
                	 sItem = scannedItemNo;
                 }
                 String incprice="",cppi="";
               //end code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                 if(type.equalsIgnoreCase("ESTIMATE")){
                	  	/*convertedcost = new ESTUtil().getConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"ESTIMATE");*/
                	  	convertedcost = new ESTUtil().getConvertedUnitCostForProductByCurrency(plant,currencyID,sItem); 
                	 	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
    					minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
                	 	OBDiscount=new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"ESTIMATE");
                	 	Hashtable ht = new Hashtable();
                	 	ht.put("item", sItem);
                	 	ht.put("plant", plant);
                	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
                	 	estQty = (String) m.get("ESTQTY");
                	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
                	 	avlbQty= (String) m.get("AVLBQTY");
                      }
					   else   if(type.equalsIgnoreCase("RENTAL")) {
              	 	convertedcost = new LoanUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
              	 	convertedcostwtc = new LoanUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
              	 	// OBDiscount=new LoanUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"RENTAL");     
               	}else if(type.equalsIgnoreCase("SO")){
               		convertedcost = new DOUtil().getConvertedAverageUnitCostForProductByCurrency(plant,uom,sItem);
               		//if avg cost zero then set product cost
               		Double dcconvertedcost = Double.valueOf(StrUtils.fString(convertedcost));
               		if(dcconvertedcost==0)
               			convertedcost = new DoHdrDAO().getUnitCostsalesBasedOnCurIDSelectedForOrderByCurrency(plant,currencyID,sItem);
               		//end
               		convertedcosts = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,convertedcost); 
            	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
            	 	OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"OUTBOUND");
            	 	Hashtable ht = new Hashtable();
            	 	ht.put("item", sItem);
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	estQty = (String) m.get("ESTQTY");
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	avlbQty= (String) m.get("AVLBQTY");
               	}else{
                	 	convertedcost = new DOUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
                	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	 	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"OUTBOUND");     
                 	}
                 String discounttype="";
                 int plusIndex = OBDiscount.indexOf("%");
                 if (plusIndex != -1) {
                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
                	 	discounttype = "BYPERCENTAGE";
                     }
                 
                 Map arrItem = itemUtil.GetProductForPurchase(sItem,plant);
                 if(arrItem.size()>0){
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
                        resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
                        resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
            			resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						resultObjectJson.put("incprice",StrUtils.fString((String)arrItem.get("INCPRICE")));
						resultObjectJson.put("incpriceunit",StrUtils.fString((String)arrItem.get("INCPRICEUNIT")));
						resultObjectJson.put("ConvertedUnitCost",convertedcosts);
						resultObjectJson.put("ConvertedUnitCosts",convertedcost);
						incprice = StrUtils.fString((String)arrItem.get("INCPRICE"));
						cppi = StrUtils.fString((String)arrItem.get("CPPI"));
						AODtype = currencyID;
               	 		convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,convertedcost);
                    	 if(cppi.equalsIgnoreCase("BYPRICE")) {
                    		 incprice = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,incprice); 
                    	 }else {
                    		 AODtype = "%"; 
                    	 }
						resultObjectJson.put("ADDONCOST",incprice); //imthi to display incproce for the item
						resultObjectJson.put("AODTYPE",AODtype); //imthi to display currency what we choosed
//						resultObjectJson.put("ConvertedUnitCost",convertedcost);
                        resultObjectJson.put("outgoingOBDiscount",OBDiscount);
                        resultObjectJson.put("OBDiscountType",discounttype);
                        resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
						resultObjectJson.put("minSellingConvertedUnitCost",minSellingconvertedcost);
						resultObjectJson.put("EstQty",estQty);
						resultObjectJson.put("AvlbQty",avlbQty);
						resultObjectJson.put("prdesc",StrUtils.fString((String)arrItem.get("ITEMDESC")));
						resultJson.put("result", resultObjectJson);
                       } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    
    private JSONObject GetAverageCostProductDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemUtil itemUtil = new ItemUtil();
            try {

                String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String doNO = StrUtils.fString(request.getParameter("DONO")).trim();
                String type = StrUtils.fString(request.getParameter("TYPE")).trim();
                String uom = StrUtils.fString(request.getParameter("UOM")).trim();
                
                String currencyID = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
                String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
                
                
                String replacePreviousSalesCost = StrUtils.fString(request.getParameter("REPLACEPREVIOUSSALESCOST")).trim();
                
                String cost="";
                String AODtype="";
                String convertedcosts="";
                
                String convertedcost="";
                String convertedcostwtc="";
				String minSellingconvertedcost="";
                //start code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                ItemMstDAO itemMstDAO = new ItemMstDAO();
                itemMstDAO.setmLogger(mLogger);
                 
                 boolean itemFound = true;
                 String OBDiscount="", estQty = "", avlbQty = "";
                 String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, sItem);
                 if (scannedItemNo == null) {
                         itemFound = false;
                 }
                 else{
                	 sItem = scannedItemNo;
                 }
               //end code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                 String incprice="",cppi="",purcost=""; //imthi fo SO
                 if(type.equalsIgnoreCase("ESTIMATE")){
                	  	/*convertedcost = new ESTUtil().getConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	  	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"ESTIMATE");*/
                	  	convertedcost = new ESTUtil().getConvertedUnitCostForProductByCurrency(plant,currencyID,sItem); 
                	 	convertedcostwtc = new ESTUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
    					minSellingconvertedcost = new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
                	 	OBDiscount=new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"ESTIMATE");
                	 	Hashtable ht = new Hashtable();
                	 	ht.put("item", sItem);
                	 	ht.put("plant", plant);
                	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
                	 	estQty = (String) m.get("ESTQTY");
                	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
                	 	avlbQty= (String) m.get("AVLBQTY");
                      }
					   else   if(type.equalsIgnoreCase("RENTAL")) {
              	 	convertedcost = new LoanUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
              	 	convertedcostwtc = new LoanUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
              	 	// OBDiscount=new LoanUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"RENTAL");     
               	}else if(type.equalsIgnoreCase("SO")){
               		convertedcost = new DOUtil().getConvertedAverageUnitCostForProductByCurrency(plant,uom,sItem); 
            	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
            	 	OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"OUTBOUND");
            	 	Hashtable ht=new Hashtable<>();
               	 	String extCond="";
            	 	if(replacePreviousSalesCost.equalsIgnoreCase("0")) {
                   	 String query="SELECT INCPRICE,CPPI,COST FROM "+plant+"_ITEMMST WHERE ITEM='"+sItem+"'";
                   	 ArrayList arrList = new ItemMstUtil().selectForReport(query,ht,"");
                   	 if (arrList.size() > 0) {
                   		 for (int i=0; i < arrList.size(); i++ ) {
                   			 Map m = (Map) arrList.get(i);
                   			  incprice = (String) m.get("INCPRICE");
                   			  cppi = (String) m.get("CPPI");
                   			  purcost = (String) m.get("COST");
                   		 	}
                   	 	}
                   	 		AODtype = currencyID;
                   	 		convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,convertedcost);
	                    	 if(cppi.equalsIgnoreCase("BYPRICE")) {
	                    		 incprice = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,incprice); 
	                    	 }else {
	                    		 AODtype = "%"; 
	                    	 }
   	                  }
            	 	
            	 	
            	 	
            	 	ht.put("item", sItem);
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	estQty = (String) m.get("ESTQTY");
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	avlbQty= (String) m.get("AVLBQTY");
               	}else{
                	 	convertedcost = new DOUtil().getConvertedUnitCostForProduct(plant,doNO,sItem); 
                	 	convertedcostwtc = new DOUtil().getConvertedUnitCostForProductWTC(plant,doNO,sItem);
						minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProduct(plant,doNO,sItem);
                	 	 OBDiscount=new DOUtil().getOBDiscountSelectedItem(plant,doNO,sItem,"OUTBOUND");     
                 	}
                 String discounttype="";
                 int plusIndex = OBDiscount.indexOf("%");
                 if (plusIndex != -1) {
                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
                	 	discounttype = "BYPERCENTAGE";
                     }
                            
                 List listItem = itemUtil.queryItemMstDetails(sItem,plant);
                        Vector arrItem    = (Vector)listItem.get(0);
                        if(arrItem.size()>0){
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        resultObjectJson.put("sItem",(String)arrItem.get(0)); 
                        resultObjectJson.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        resultObjectJson.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        resultObjectJson.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        String PRD_DEP_ID = StrUtils.fString((String)arrItem.get(46));
						String PRD_DEPT_DESC = "";
						/*
						 * if(PRD_DEP_ID.length()>0) { JSONObject deptJson=new
						 * PrdDeptDAO().getDeptName(plant, (String) PRD_DEP_ID); PRD_DEPT_DESC =
						 * deptJson.getString("PRD_DEPT_DESC"); }
						 */
                        resultObjectJson.put("prd_dept_id",PRD_DEP_ID);
                        resultObjectJson.put("prd_dept_desc",PRD_DEPT_DESC);
//                        resultObjectJson.put("DEPT_DISPLAY_ID",StrUtils.fString((String)arrItem.get(43)));
                        resultObjectJson.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                    //  Start code added by Bruhan for product brand on 11/9/12 
                        resultObjectJson.put("brand",StrUtils.fString((String)arrItem.get(19)));
                    //  End code added by Bruhan for product brand on 11/9/12 
                        resultObjectJson.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        resultObjectJson.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        resultObjectJson.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        resultObjectJson.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                       /* resultObjectJson.put("price", StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(12))));
                        resultObjectJson.put("cost",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(13))));
                        resultObjectJson.put("minsprice",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(14))));*/
                        resultObjectJson.put("price",StrUtils.fString((String)arrItem.get(12)));
                        resultObjectJson.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        resultObjectJson.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        resultObjectJson.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        resultObjectJson.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        resultObjectJson.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        resultObjectJson.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        resultObjectJson.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                      //Start code added by Bruhan for base currency on Aug 28th 2012.
                        resultObjectJson.put("ConvertedUnitCost",convertedcost);
                      //End code added by Bruhan for base currency on Aug 28th 2012.
                        resultObjectJson.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        resultObjectJson.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        resultObjectJson.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        resultObjectJson.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        resultObjectJson.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        resultObjectJson.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        resultObjectJson.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        resultObjectJson.put("model",StrUtils.fString((String)arrItem.get(30)));
						resultObjectJson.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        resultObjectJson.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        resultObjectJson.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        resultObjectJson.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        resultObjectJson.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        resultObjectJson.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        resultObjectJson.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        resultObjectJson.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						resultObjectJson.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						resultObjectJson.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        resultObjectJson.put("outgoingOBDiscount",OBDiscount);
                        resultObjectJson.put("OBDiscountType",discounttype);
                        resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
						resultObjectJson.put("minSellingConvertedUnitCost",minSellingconvertedcost);
						resultObjectJson.put("EstQty",estQty);
						resultObjectJson.put("AvlbQty",avlbQty);
						resultObjectJson.put("iscompro",StrUtils.fString((String)arrItem.get(42)));
						resultObjectJson.put("cppi",StrUtils.fString((String)arrItem.get(43)));
						resultObjectJson.put("incprice",StrUtils.fString((String)arrItem.get(44)));
						resultObjectJson.put("incpriceunit",StrUtils.fString((String)arrItem.get(45)));
						String vendno = StrUtils.fString((String)arrItem.get(47));
						resultObjectJson.put("ISPOSDISCOUNT",StrUtils.fString((String)arrItem.get(49)));
						String vendname = "";
						if(vendno.length()>0) {
						JSONObject vendorJson=new VendMstDAO().getVendorName(plant, (String) vendno);
						vendname = vendorJson.getString("VNAME");
						}
						resultObjectJson.put("vendno",vendno);
						resultObjectJson.put("vendname",vendname);
						
						resultObjectJson.put("ADDONCOST",incprice); //imthi to display incproce for the item
						resultObjectJson.put("AODTYPE",AODtype); //imthi to display currency what we choosed
						
						resultJson.put("result", resultObjectJson);
                       } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    
    private JSONObject GetInvoiceAutoProductDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemMstUtil itemUtil = new ItemMstUtil();
            try {

                String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String doNO = StrUtils.fString(request.getParameter("DONO")).trim();
                String type = StrUtils.fString(request.getParameter("TYPE")).trim();
                String UNITPRICE = StrUtils.fString(request.getParameter("UNITPRICE")).trim();
                String uom = StrUtils.fString(request.getParameter("UOM")).trim();
                
                String currencyID = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
                String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
                

                String cost="";
                String convertedcost="";
                String convertedcostwtc="";
				String minSellingconvertedcost="";
                //start code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                ItemMstDAO itemMstDAO = new ItemMstDAO();
                itemMstDAO.setmLogger(mLogger);
                 
                 boolean itemFound = true;
                 String OBDiscount="", estQty = "", avlbQty = "";
                 String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, sItem);
                 if (scannedItemNo == null) {
                         itemFound = false;
                 }
                 else{
                	 sItem = scannedItemNo;
                 }
                 
     			Hashtable ht=new Hashtable<>();
           	 	String extCond="";
                 if(!custCode.equalsIgnoreCase("") && !sItem.equalsIgnoreCase("") ) {
	                    String query = "WITH TBL AS (" + 
	                    		"SELECT Custcode as CUSTNO,CollectionDate as ORD_DATE,A.CRAT,ITEM,UNITPRICE,E.UNITMO AS UOM FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] E ON A.DONO=E.DONO  WHERE A.PLANT='"+plant+"'" + 
	                    		"UNION " + 
	                    		"SELECT CUSTNO,INVOICE_DATE as ORD_DATE,A.CRAT,ITEM,UNITPRICE,ISNULL(E.UOM,'') UOM FROM ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] E ON A.ID=E.INVOICEHDRID  WHERE A.PLANT='"+plant+"' )" + 
	                    		"SELECT TOP 1 ISNULL(UNITPRICE,0)/ (case when TBL.UOM!='' then (ISNULL((select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM where UOM=TBL.UOM ),1)) else  (ISNULL((select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM U JOIN "+plant+"_ITEMMST I ON I.SALESUOM=U.UOM where I.ITEM=TBL.ITEM ),1)) end ) UNITPRICE FROM  TBL " + 
	                    		"where CUSTNO='"+custCode+"' and ITEM='"+ sItem +"' " + 
	                    		"order by CRAT desc,CAST((SUBSTRING(ORD_DATE, 7, 4) + SUBSTRING(ORD_DATE, 4, 2) + SUBSTRING(ORD_DATE, 1, 2)) AS date) desc";
	                    ArrayList al = itemUtil.selectForReport(query, ht, extCond);
	                    if (al.size() > 0) {
	                    	Map mp=(Map)al.get(0);
	                    	Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
				  			String PUOMQTY ="1";
                            ArrayList getuomqty = new MovHisDAO().selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM where UOM='"+ uom+"'",htTrand1);
                			if(getuomqty.size()>0)
                			{
                			Map mapval = (Map) getuomqty.get(0);
                			PUOMQTY=(String)mapval.get("UOMQTY");
                			}
	                    	UNITPRICE = (StrUtils.fString((String)mp.get("UNITPRICE")));
	                    	Double pcscost = Double.valueOf(UNITPRICE) * Double.valueOf(PUOMQTY);
	                    	UNITPRICE = String.valueOf(pcscost);
	                    } 
	                    }
                 
                 
               //end code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                  if(type.equalsIgnoreCase("SO")){
                	cost = new DoHdrDAO().getUnitCostsalesBasedOnCurIDSelectedForOrderByCurrency(plant,currencyID,sItem);
               		convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,UNITPRICE); 
            	 	convertedcostwtc = new DoHdrDAO().getInvoiceConvertedUnitCostForProductWTC(plant,doNO,sItem,UNITPRICE);
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
            	 	OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"OUTBOUND");
					//OBDiscount=convertedcost;--Commented by azees issue on discount cal. 10.12.22
            	 	ht.put("item", sItem);
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	estQty = (String) m.get("ESTQTY");
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	avlbQty= (String) m.get("AVLBQTY");
               	}
                 String discounttype="";
                 int plusIndex = OBDiscount.indexOf("%");
                 if (plusIndex != -1) {
                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
                	 	discounttype = "BYPERCENTAGE";
                     }

                 Map arrItem = itemUtil.GetProductForPurchase(sItem,plant);
                 if(arrItem.size()>0){
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
                        resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
                        resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
            			resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
            			resultObjectJson.put("ITEMDESC",StrUtils.fString((String)arrItem.get("ITEMDESC")));
            			resultObjectJson.put("ConvertedUnitCost",convertedcost);
                        resultObjectJson.put("outgoingOBDiscount",OBDiscount);
                        resultObjectJson.put("OBDiscountType",discounttype);
                        resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
						resultObjectJson.put("minSellingConvertedUnitCost",minSellingconvertedcost);
						resultObjectJson.put("ConvertedUnitCosts",cost);
						resultObjectJson.put("EstQty",estQty);
						resultObjectJson.put("AvlbQty",avlbQty);
						resultObjectJson.put("UNITPRICE",UNITPRICE);
						resultObjectJson.put("prdesc",StrUtils.fString((String)arrItem.get("ITEMDESC")));
                        resultJson.put("result", resultObjectJson);
                       } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    
    private JSONObject GetInvoiceProductDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemUtil itemUtil = new ItemUtil();
            try {

                String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String doNO = StrUtils.fString(request.getParameter("DONO")).trim();
                String type = StrUtils.fString(request.getParameter("TYPE")).trim();
                String UNITPRICE = StrUtils.fString(request.getParameter("UNITPRICE")).trim();
                
                String currencyID = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
                String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
                
                String convertedcost="";
                String convertedcostwtc="";
				String minSellingconvertedcost="";
                //start code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                ItemMstDAO itemMstDAO = new ItemMstDAO();
                itemMstDAO.setmLogger(mLogger);
                 
                 boolean itemFound = true;
                 String OBDiscount="", estQty = "", avlbQty = "";
                 String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, sItem);
                 if (scannedItemNo == null) {
                         itemFound = false;
                 }
                 else{
                	 sItem = scannedItemNo;
                 }
                 
      			Hashtable ht=new Hashtable<>();
           	 	String extCond="";
                 if(!custCode.equalsIgnoreCase("") && !sItem.equalsIgnoreCase("") ) {
	                    String query = "WITH TBL AS (" + 
	                    		"SELECT Custcode as CUSTNO,CollectionDate as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] E ON A.DONO=E.DONO  WHERE A.PLANT='"+plant+"'" + 
	                    		"UNION " + 
	                    		"SELECT CUSTNO,INVOICE_DATE as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] E ON A.ID=E.INVOICEHDRID  WHERE A.PLANT='"+plant+"' )" + 
	                    		"SELECT TOP 1 ISNULL(UNITPRICE,0) UNITPRICE FROM  TBL " + 
	                    		"where CUSTNO='"+custCode+"' and ITEM='"+ sItem +"' " + 
	                    		"order by CRAT desc,CAST((SUBSTRING(ORD_DATE, 7, 4) + SUBSTRING(ORD_DATE, 4, 2) + SUBSTRING(ORD_DATE, 1, 2)) AS date) desc";
	                    ArrayList al = new ItemMstUtil().selectForReport(query, ht, extCond);
	                    if (al.size() > 0) {
	                    	Map mp=(Map)al.get(0);
	                    	UNITPRICE = (StrUtils.fString((String)mp.get("UNITPRICE")));
	                    } 
	                    }
                 
               //end code by Bruhan to get item id if alternate item scanned in add products of outbound order on 19 dec 2013
                  if(type.equalsIgnoreCase("SO")){
               		convertedcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,currencyID,sItem,UNITPRICE); 
            	 	convertedcostwtc = new DoHdrDAO().getInvoiceConvertedUnitCostForProductWTC(plant,doNO,sItem,UNITPRICE);
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currencyID,sItem);
            	 	//OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,custCode,sItem,"OUTBOUND");
					OBDiscount=convertedcost;
            	 	ht.put("item", sItem);
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	estQty = (String) m.get("ESTQTY");
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	avlbQty= (String) m.get("AVLBQTY");
               	}
                 String discounttype="";
                 int plusIndex = OBDiscount.indexOf("%");
                 if (plusIndex != -1) {
                	 	OBDiscount = OBDiscount.substring(0, plusIndex);
                	 	discounttype = "BYPERCENTAGE";
                     }
                            
                 List listItem = itemUtil.queryItemMstDetails(sItem,plant);
                        Vector arrItem    = (Vector)listItem.get(0);
                        if(arrItem.size()>0){
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        resultObjectJson.put("sItem",(String)arrItem.get(0)); 
                        resultObjectJson.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        resultObjectJson.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        resultObjectJson.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        resultObjectJson.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                    //  Start code added by Bruhan for product brand on 11/9/12 
                        resultObjectJson.put("brand",StrUtils.fString((String)arrItem.get(19)));
                    //  End code added by Bruhan for product brand on 11/9/12 
                        resultObjectJson.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        resultObjectJson.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        resultObjectJson.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        resultObjectJson.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                       /* resultObjectJson.put("price", StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(12))));
                        resultObjectJson.put("cost",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(13))));
                        resultObjectJson.put("minsprice",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(14))));*/
                        resultObjectJson.put("price",StrUtils.fString((String)arrItem.get(12)));
                        resultObjectJson.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        resultObjectJson.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        resultObjectJson.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        resultObjectJson.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        resultObjectJson.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        resultObjectJson.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        resultObjectJson.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                      //Start code added by Bruhan for base currency on Aug 28th 2012.
                        resultObjectJson.put("ConvertedUnitCost",convertedcost);
                      //End code added by Bruhan for base currency on Aug 28th 2012.
                        resultObjectJson.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        resultObjectJson.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        resultObjectJson.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        resultObjectJson.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        resultObjectJson.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        resultObjectJson.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        resultObjectJson.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        resultObjectJson.put("model",StrUtils.fString((String)arrItem.get(30)));
						resultObjectJson.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        resultObjectJson.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        resultObjectJson.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        resultObjectJson.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        resultObjectJson.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        resultObjectJson.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        resultObjectJson.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        resultObjectJson.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						resultObjectJson.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						resultObjectJson.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
						resultObjectJson.put("ISPOSDISCOUNT",StrUtils.fString((String)arrItem.get(49)));
                        resultObjectJson.put("outgoingOBDiscount",OBDiscount);
                        resultObjectJson.put("OBDiscountType",discounttype);
                        resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
						resultObjectJson.put("minSellingConvertedUnitCost",minSellingconvertedcost);
						resultObjectJson.put("EstQty",estQty);
						resultObjectJson.put("AvlbQty",avlbQty);
						resultObjectJson.put("UNITPRICE",UNITPRICE);
                        resultJson.put("result", resultObjectJson);
                       } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }

    private JSONObject GetProductDetailsforpurchase(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemUtil itemUtil = new ItemUtil();
        ItemMstDAO itemMstDAO = new ItemMstDAO();
            try {

                String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String currency = StrUtils.fString(request.getParameter("CURRENCY")).trim();
                String vendno = StrUtils.fString(request.getParameter("VENDNO")).trim();
                String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant,sItem);
			
                String convertedcost= new POUtil().getConvertedUnitCostForProductsCurrency(plant,currency,scannedItemNo); 
                String convertedcostwtc=new POUtil().getConvertedUnitCostForProductWTCcurrency(plant,currency,scannedItemNo);
                String IBDiscount=new POUtil().getIBDiscountSelectedItemVNO(plant,vendno,sItem);
                String discounttype="";
                
                int plusIndex = IBDiscount.indexOf("%");
                if (plusIndex != -1) {
               	 	IBDiscount = IBDiscount.substring(0, plusIndex);
               	 	discounttype = "BYPERCENTAGE";
                    }
                   
                 List listItem = itemUtil.queryItemMstDetailsforpurchase(sItem,plant);
                        Vector arrItem    = (Vector)listItem.get(0);
                        if(arrItem.size()>0){
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        resultObjectJson.put("sItem",(String)arrItem.get(0)); 
                        resultObjectJson.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        resultObjectJson.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        resultObjectJson.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        resultObjectJson.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                    //  Start code added by Bruhan for product brand on 11/9/12 
                        resultObjectJson.put("brand",StrUtils.fString((String)arrItem.get(19)));
                    //  End code added by Bruhan for product brand on 11/9/12 
                        resultObjectJson.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        resultObjectJson.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        resultObjectJson.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        resultObjectJson.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                       /* resultObjectJson.put("price", StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(12))));
                        resultObjectJson.put("cost",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(13))));
                        resultObjectJson.put("minsprice",  StrUtils.currencyWtoutCommSymbol(StrUtils.fString((String)arrItem.get(14))));*/
                        resultObjectJson.put("price",StrUtils.fString((String)arrItem.get(12)));
                        resultObjectJson.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        resultObjectJson.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        resultObjectJson.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        resultObjectJson.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        resultObjectJson.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        resultObjectJson.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        resultObjectJson.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                      //Start code added by Bruhan for base currency on Aug 28th 2012.
                       // 
                      //End code added by Bruhan for base currency on Aug 28th 2012.
                        resultObjectJson.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        resultObjectJson.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        resultObjectJson.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        resultObjectJson.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        resultObjectJson.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        resultObjectJson.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        resultObjectJson.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        resultObjectJson.put("model",StrUtils.fString((String)arrItem.get(30)));
						resultObjectJson.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        resultObjectJson.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        resultObjectJson.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        resultObjectJson.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        resultObjectJson.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        resultObjectJson.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        resultObjectJson.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        resultObjectJson.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						resultObjectJson.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						resultObjectJson.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
						resultObjectJson.put("incommingQty",StrUtils.fString((String)arrItem.get(41)));
						resultObjectJson.put("ISPOSDISCOUNT",StrUtils.fString((String)arrItem.get(42)));
						resultObjectJson.put("ConvertedUnitCost",convertedcost);
						resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
						resultObjectJson.put("incomingIBDiscount",IBDiscount);
                        resultObjectJson.put("IBDiscountType",discounttype);
                        String estQty = "", avlbQty = "";
                        Hashtable ht = new Hashtable();
                	 	ht.put("item", sItem);
                	 	ht.put("plant", plant);
                	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
                	 	estQty = (String) m.get("ESTQTY");
                	 	//m = new InvMstDAO().getPOAvailableQtyByProduct(ht,StrUtils.fString((String)arrItem.get(41)));
                	 	m = new InvMstDAO().getPOAvailableQtyByProduct(ht,"0");
                	 	avlbQty= (String) m.get("AVLBQTY");
                	 	resultObjectJson.put("EstQty",estQty);
						resultObjectJson.put("AvlbQty",avlbQty);
						
                        resultJson.put("result", resultObjectJson);
                       } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    private JSONObject GetPurchaseProductDetails(HttpServletRequest request) {//By Azees-30.08.22 FOR Purchase
    	JSONObject resultJson = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	JSONArray jsonArrayErr = new JSONArray();
    	ItemMstUtil itemUtil = new ItemMstUtil();
    	ItemMstDAO itemMstDAO = new ItemMstDAO();
    	try {
    		
    		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
    		String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
    		String currency = StrUtils.fString(request.getParameter("CURRENCY")).trim();
    		String vendno = StrUtils.fString(request.getParameter("VENDNO")).trim();
    		String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant,sItem);
    		
    		String convertedcost= new POUtil().getConvertedUnitCostForProductsCurrency(plant,currency,scannedItemNo); 
    		String convertedcostwtc=new POUtil().getConvertedUnitCostForProductWTCcurrency(plant,currency,scannedItemNo);
    		String IBDiscount=new POUtil().getIBDiscountSelectedItemVNO(plant,vendno,sItem);
    		String discounttype="";
    		
    		int plusIndex = IBDiscount.indexOf("%");
    		if (plusIndex != -1) {
    			IBDiscount = IBDiscount.substring(0, plusIndex);
    			discounttype = "BYPERCENTAGE";
    		}
    		
    		Map arrItem = itemUtil.GetProductForPurchase(sItem,plant);
    		if(arrItem.size()>0){
    			resultJson.put("status", "100");
    			JSONObject resultObjectJson = new JSONObject();
    			resultObjectJson.put("cost",StrUtils.fString((String)arrItem.get("COST")));
    			resultObjectJson.put("stkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
    			resultObjectJson.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
    			resultObjectJson.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
    			resultObjectJson.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
    			resultObjectJson.put("incommingQty",StrUtils.fString((String)arrItem.get("INCOMINGQTY")));
    			resultObjectJson.put("prdesc",StrUtils.fString((String)arrItem.get("ITEMDESC")));
    			resultObjectJson.put("ConvertedUnitCost",convertedcost);
    			resultObjectJson.put("ConvertedUnitCostWTC",convertedcostwtc);
    			resultObjectJson.put("incomingIBDiscount",IBDiscount);
    			resultObjectJson.put("IBDiscountType",discounttype);
    			String estQty = "", avlbQty = "";
    			Hashtable ht = new Hashtable();
    			ht.put("item", sItem);
    			ht.put("plant", plant);
    			Map m = new EstDetDAO().getEstQtyByProduct(ht);
    			estQty = (String) m.get("ESTQTY");
    			m = new InvMstDAO().getPOAvailableQtyByProduct(ht,"0");
    			avlbQty= (String) m.get("AVLBQTY");
    			resultObjectJson.put("EstQty",estQty);
    			resultObjectJson.put("AvlbQty",avlbQty);
    			
    			resultJson.put("result", resultObjectJson);
    		} else {
    			resultJson.put("status", "99");
    		}
    		return resultJson;
    	} catch (Exception daRE) {
    		resultJson.put("status", "99");
    		return resultJson;
    	}
    }
    private JSONObject getProductsmryDetails(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
           StrUtils strUtils = new StrUtils();
            String PLANT="",ITEM="",ITEM_DESC="",PRD_CLS_ID="",PRD_TYPE_ID="",COND="";
            try {
            
            PLANT = StrUtils.fString(request.getParameter("PLANT")).trim();
            ITEM = StrUtils.fString(request.getParameter("ITEM")).trim();
            ITEM_DESC = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
            PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID")).trim();
            PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID")).trim();
            COND = StrUtils.fString(request.getParameter("COND")).trim();
              
              
                List locQryList= new ItemUtil().queryItemMstForSearchCriteria(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PLANT,COND);//"","","","",plant,"");
                if (locQryList.size() > 0) {
                        for (int iCnt =0; iCnt<locQryList.size(); iCnt++){
                                    int iIndex = iCnt + 1;
                                    Vector vecItem   = (Vector)locQryList.get(iCnt);
                                    JSONObject resultJsonInt = new JSONObject();
                                    resultJsonInt.put("PRODUCT", strUtils.fString((String)vecItem.get(0)));
                                    resultJsonInt.put("DESC", strUtils.fString((String)vecItem.get(1)));
                                    resultJsonInt.put("ITEMTYPE", strUtils.fString((String)vecItem.get(2)));
                                    resultJsonInt.put("UOM", strUtils.fString((String)vecItem.get(3)));
                                    resultJsonInt.put("REMARK1", strUtils.fString((String)vecItem.get(4)));
                                    resultJsonInt.put("REMARK2", strUtils.fString((String)vecItem.get(5)));
                                 //   resultJsonInt.put("REMARK3",strUtils.fString((String)vecItem.get(6)));
                                 //   resultJsonInt.put("REMARK4",strUtils.fString( (String)vecItem.get(7)));
                                  //  resultJsonInt.put("STKQTY", strUtils.fString((String)vecItem.get(8)));
                                  //  resultJsonInt.put("ASSET", strUtils.fString((String)vecItem.get(9)));
                                    resultJsonInt.put("PRD_CLS_ID",strUtils.fString( (String)vecItem.get(10)));
                                    resultJsonInt.put("ACTIVE", strUtils.fString((String)vecItem.get(11)));
                                    resultJsonInt.put("PRICE", StrUtils.currencyWtoutSymbol((String)vecItem.get(12)));
                                    resultJsonInt.put("COST", StrUtils.currencyWtoutSymbol((String)vecItem.get(13)));
                                   // resultJsonInt.put("DISCOUNT", strUtils.fString((String)vecItem.get(14)));
                                  //  resultJsonInt.put("ISPARENT", strUtils.fString((String)vecItem.get(15)));
                                                   
                                    jsonArray.add(resultJsonInt);

                            }
                        resultJson.put("items", jsonArray);
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                        resultJsonInt.put("ERROR_CODE", "100");
                        jsonArrayErr.add(resultJsonInt);
                        resultJson.put("errors", jsonArrayErr);
                } else {
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                        resultJsonInt.put("ERROR_CODE", "99");
                        jsonArrayErr.add(resultJsonInt);
                        jsonArray.add("");
                        resultJson.put("items", jsonArray);

                        resultJson.put("errors", jsonArrayErr);
                }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getProductListByCond(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
            ItemMstUtil itemUtil = new ItemMstUtil();
            StrUtils strUtils = new StrUtils();
            try {
            
                String plant = strUtils.fString(request.getParameter("PLANT")).trim();
                String sItem = strUtils.fString(request.getParameter("ITEM")).trim();
                String sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC")).trim();
                String sCond = strUtils.fString(request.getParameter("COND")).trim();
               
                ArrayList listQry = itemUtil.getItemList(plant,sItem,sItemDesc,sCond);
              
               
                if (listQry.size() > 0) {
                    for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                            int iIndex = iCnt + 1;
                            Map m=(Map)listQry.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            resultJsonInt.put("PRODUCT", strUtils.fString((String)m.get("item")));
                            resultJsonInt.put("DESC", strUtils.fString((String)m.get("itemdesc")));
                            resultJsonInt.put("UNITPRICE", strUtils.fString((String)m.get("UNITPRICE")));
                            resultJsonInt.put("UOM", strUtils.fString((String)m.get("UOM")));
                            resultJsonInt.put("DETAILDESC", strUtils.fString((String)m.get("REMARK1")));
                            resultJsonInt.put("MANUFACTURER",strUtils.fString( (String)m.get("MANUFACTURER")));
                            resultJsonInt.put("COND", "");
                            jsonArray.add(resultJsonInt);

                        }
                        resultJson.put("items", jsonArray);
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                        resultJsonInt.put("ERROR_CODE", "100");
                        jsonArrayErr.add(resultJsonInt);
                        resultJson.put("errors", jsonArrayErr);
                } else {
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                        resultJsonInt.put("ERROR_CODE", "99");
                        jsonArrayErr.add(resultJsonInt);
                        jsonArray.add("");
                        resultJson.put("items", jsonArray);

                        resultJson.put("errors", jsonArrayErr);
                }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    resultJson.put("TOTAL_QTY", 0);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getProductListFromInv(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
            InvMstDAO  _InvMstDAO  = new InvMstDAO();  
             _InvMstDAO.setmLogger(mLogger);
            try {
            
                String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
                String sLoc = StrUtils.fString(request.getParameter("LOC")).trim();
                String sCond = StrUtils.fString(request.getParameter("COND")).trim();
               
                List listQry  = _InvMstDAO.getLocationTransferItemWithOutLoc(plant,sItem,sItemDesc,sLoc);
              
               
                if (listQry.size() > 0) {
                    for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                            int iIndex = iCnt + 1;
                            Map m=(Map)listQry.get(iCnt);
                            String strItemDesc =(String)m.get("ItemDesc");
                            String[] splitformat= strItemDesc.split("||");
                            
                            JSONObject resultJsonInt = new JSONObject();
                            resultJsonInt.put("PRODUCT", (String)m.get("item"));
                            resultJsonInt.put("DESC",splitformat[0]);
                            resultJsonInt.put("UOM", splitformat[1]);
                        
                            jsonArray.add(resultJsonInt);

                        }
                        resultJson.put("items", jsonArray);
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                        resultJsonInt.put("ERROR_CODE", "100");
                        jsonArrayErr.add(resultJsonInt);
                        resultJson.put("errors", jsonArrayErr);
                } else {
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                        resultJsonInt.put("ERROR_CODE", "99");
                        jsonArrayErr.add(resultJsonInt);
                        jsonArray.add("");
                        resultJson.put("items", jsonArray);

                        resultJson.put("errors", jsonArrayErr);
                }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    resultJson.put("TOTAL_QTY", 0);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    
	private JSONObject loadAlternateItems(String plant, String item) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONArray resultJsonArray = new JSONArray();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			List<String> resultSet = itemMstDAO.getAllAssignedAlternateItems(
					plant, item);
			if (resultSet.size() > 0) {
				for (String resultValue : resultSet) {
					resultJsonArray.add(resultValue);
				}
				resultJson.put("result", resultJsonArray);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
	
        
    private JSONObject getInventoryProductList(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
            try {
            
                String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
                String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
                String sLoc = StrUtils.fString(request.getParameter("LOC")).trim();             
                String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
                
              
              
                List listQry =   new InvMstDAO().getInvItemByWMS(plant,sItem,sItemDesc,sLoc);
                if (listQry.size() > 0) {
                    for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                                 int iIndex = iCnt + 1;
                                Map m=(Map)listQry.get(iCnt);
                                JSONObject resultJsonInt = new JSONObject();
                                resultJsonInt.put("PRODUCT", (String)m.get("item"));
                                resultJsonInt.put("DESC", (String)m.get("itemDesc"));
                                resultJsonInt.put("UOM", (String)m.get("uom"));
                                                           
                               
                            jsonArray.add(resultJsonInt);

                        }
                        resultJson.put("items", jsonArray);
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                        resultJsonInt.put("ERROR_CODE", "100");
                        jsonArrayErr.add(resultJsonInt);
                        resultJson.put("errors", jsonArrayErr);
                } else {
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                        resultJsonInt.put("ERROR_CODE", "99");
                        jsonArrayErr.add(resultJsonInt);
                        jsonArray.add("");
                        resultJson.put("items", jsonArray);

                        resultJson.put("errors", jsonArrayErr);
                }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    resultJson.put("TOTAL_QTY", 0);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }

    @SuppressWarnings("unchecked")
	private JSONObject validateBatchForPicking(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			String location = StrUtils.fString(request.getParameter("LOC"));
			JSONObject resultJsonObject = new JSONObject();
			List resultList = invMstDAO.getTotalQuantityForOutBoundPickingBatchByWMS(plant,itemNo, location, batch);
			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				String sBatch = (String) m.get("batch");
				String qty = (String) m.get("qty");
				resultJsonObject.put("BATCH", sBatch);
				resultJsonObject.put("QTY", qty);
				JSONArray arr = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("INVENTORYQUANTITY", qty);
				arr.add(obj);
				resultJson.put("result", resultJsonObject);
				resultJson.put("items", arr);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
    
    private JSONObject getMutiPriceCutomerTypeList(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
            ItemMstDAO  _ItemMstDAO  = new  ItemMstDAO();
           _ItemMstDAO.setmLogger(mLogger);
            try {
            
    
                  String PLANT = StrUtils.fString(request.getParameter("PLANT")).trim();
                  String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
                  
                 
                  CustomerBeanDAO _CustomerBeanDAO = new CustomerBeanDAO(); 
                List listQry = _CustomerBeanDAO.getCustomerTypeDetails(CUSTOMERTYPE,PLANT,"");
                if (listQry.size() > 0) {
                    for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                                int iIndex = iCnt + 1;
                                 Map m=(Map)listQry.get(iCnt);
                                JSONObject resultJsonInt = new JSONObject();
                                resultJsonInt.put("CUSTOMERTYPE",(String)m.get("CUSTOMER_TYPE_ID"));
                                resultJsonInt.put("DESC", StrUtils.fString((String)m.get("CUSTOMER_TYPE_DESC")));
                                resultJsonInt.put("ISACTIVE", StrUtils.fString((String)m.get("ISACTIVE")));
                                jsonArray.add(resultJsonInt);

                        }
                        resultJson.put("items", jsonArray);
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                        resultJsonInt.put("ERROR_CODE", "100");
                        jsonArrayErr.add(resultJsonInt);
                        resultJson.put("errors", jsonArrayErr);
                } else {
                        JSONObject resultJsonInt = new JSONObject();
                        resultJsonInt.put("ERROR_MESSAGE", "NO CUSTOMER TYPE RECORD FOUND!");
                        resultJsonInt.put("ERROR_CODE", "99");
                        jsonArrayErr.add(resultJsonInt);
                        jsonArray.add("");
                        resultJson.put("items", jsonArray);
                        resultJson.put("errors", jsonArrayErr);
                }
            } catch (Exception e) {
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getMutiCostSupplierList(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ItemMstDAO  _ItemMstDAO  = new  ItemMstDAO();
       _ItemMstDAO.setmLogger(mLogger);
        try {
        

              String PLANT = StrUtils.fString(request.getParameter("PLANT")).trim();
              String SUPPLIER = StrUtils.fString(request.getParameter("SUPPLIER"));
              
             
              CustomerBeanDAO _CustomerBeanDAO = new CustomerBeanDAO(); 
            List listQry = _CustomerBeanDAO.getSupplierDetails(SUPPLIER,PLANT,"");
            if (listQry.size() > 0) {
                for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                            int iIndex = iCnt + 1;
                             Map m=(Map)listQry.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                           
                            resultJsonInt.put("SUPPLIERID", StrUtils.fString((String)m.get("VENDNO")));
                            resultJsonInt.put("SUPPLIERNAME",(String)m.get("VNAME"));
                            resultJsonInt.put("ISACTIVE", StrUtils.fString((String)m.get("ISACTIVE")));
                            jsonArray.add(resultJsonInt);

                    }
                    resultJson.put("items", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
            } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO SUPPLIER RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("unchecked")
	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("Class Name : " + this.getClass() + "\n");
			requestParams.append("Paramter Mapping : \n");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : "
						+ request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {

		}

	}
	/*private JSONObject loadAlternateItems(String plant, String item) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONArray resultJsonArray = new JSONArray();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			List<String> resultSet = itemMstDAO.getAllAssignedAlternateItems(
					plant, item);
			if (resultSet.size() > 0) {
				for (String resultValue : resultSet) {
					resultJsonArray.add(resultValue);
				}
				resultJson.put("result", resultJsonArray);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}*/
	private JSONObject loadCustomerDiscountOB(String plant, String item) {
		 JSONObject resultJson = new JSONObject();
        //JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
			JSONArray resultJsonArray = new JSONArray();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			String discounttype="",OBDiscount="";
			 List listQry =   itemMstDAO.getCustomerDiscountOB(plant,item);
            if (listQry.size() > 0) {
                for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                             int iIndex = iCnt + 1;
                            Map m=(Map)listQry.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            OBDiscount = (String)m.get("obdiscount");
                            int plusIndex = OBDiscount.indexOf("%");
                            if (plusIndex != -1) {
                            		OBDiscount = OBDiscount.substring(0, plusIndex);
                           	 	 	discounttype = "BYPERCENTAGE";
                                }
                            else{
                            	discounttype = "BYPRICE";
                            }
                            System.out.println("OBDISCOUNT"+OBDiscount);
                            resultJsonInt.put("OBDISCOUNT", OBDiscount);
                            resultJsonInt.put("CUSTOMERTYPE", (String)m.get("customertype"));
                            resultJsonInt.put("CNAME", (String)m.get("cname"));
                            resultJsonInt.put("DISCOUNTTYPE", discounttype);
                            resultJsonArray.add(resultJsonInt);

                    }
				 resultJson.put("items", resultJsonArray);
				resultJson.put("status", "99");
			} else {
                resultJson.put("OBDISCOUNT", "0.0");
				resultJson.put("status", "100");
			}
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}    
        return resultJson;
	}
	
	private JSONObject loadSupplierDiscountIB(String plant, String item) {
		 JSONObject resultJson = new JSONObject();
       //JSONArray jsonArray = new JSONArray();
       JSONArray jsonArrayErr = new JSONArray();
		try {
			JSONArray resultJsonArray = new JSONArray();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			String discounttype="",IBDiscount="";
			 List listQry =   itemMstDAO.getSupplierDiscountIB(plant,item);
           if (listQry.size() > 0) {
               for (int iCnt =0; iCnt<listQry.size(); iCnt++){
                            int iIndex = iCnt + 1;
                           Map m=(Map)listQry.get(iCnt);
                           JSONObject resultJsonInt = new JSONObject();
                           IBDiscount = (String)m.get("ibdiscount");
                           int plusIndex = IBDiscount.indexOf("%");
                           if (plusIndex != -1) {
                           		IBDiscount = IBDiscount.substring(0, plusIndex);
                          	 	 	discounttype = "BYPERCENTAGE";
                               }
                           else{
                           	discounttype = "BYCOST";
                           }
                           resultJsonInt.put("IBDISCOUNT", IBDiscount);
                           resultJsonInt.put("VENDNO", (String)m.get("vendno"));
                           resultJsonInt.put("VNAME", (String)m.get("vname"));
                           resultJsonInt.put("DISCOUNTTYPE", discounttype);
                           resultJsonArray.add(resultJsonInt);

                   }
               resultJson.put("items", resultJsonArray);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}    
       return resultJson;
	}
	
	private JSONObject loadItemSupplier(String plant, String item) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			JSONArray resultJsonArray = new JSONArray();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			List listQry =   itemMstDAO.getItemSupplier(plant,item);
			if (listQry.size() > 0) {
				for (int iCnt =0; iCnt<listQry.size(); iCnt++){
					int iIndex = iCnt + 1;
					Map m=(Map)listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
//					CustUtil custUtils = new CustUtil(); 
//    				ArrayList movQryList = custUtils.getVendorListsWithName((String)m.get("VENDNO"), plant,"");
//    				Map arrCustLine = (Map)movQryList.get(0);
//    				String VENDNO=(String)arrCustLine.get("VNAME");
//					resultJsonInt.put("VENDNO", VENDNO);
					resultJsonInt.put("VENDNO", (String)m.get("VENDNO"));
					resultJsonArray.add(resultJsonInt);
				}
				resultJson.put("items", resultJsonArray);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}    
		return resultJson;
	}
	
		public boolean additemSupp(String plant,String item,String sup,String username,Hashtable HM) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArrayErr = new JSONArray();
		Hashtable htData = new Hashtable();
		ItemMstDAO itemdao = new ItemMstDAO();
		ItemUtil itemUtil = new ItemUtil();
		boolean deleteItemSupplier=false;
		boolean Itemsup=false;
		try {
			String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);
			boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
		    if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int m=0; m < arrList.size(); m++ ) {
		        			  Map map = (Map) arrList.get(m);
		        			  String childplant = (String) map.get("CHILD_PLANT");
				        		   	HM.put(IConstants.PLANT, childplant);
				    			   Itemsup = itemUtil.insertItemSupplier(HM);
			        				ArrayList arrCust = new CustUtil().getVendorDetails(sup,plant);
			        				if (arrCust.size() > 0) {
			        				String sCustCode = (String) arrCust.get(0);
			        				String sCustName = (String) arrCust.get(1);
			        				if (!new CustUtil().isExistVendor(sup, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
			        				{
			        					String sAddr1 = (String) arrCust.get(2);
 			        					String sAddr2 = (String) arrCust.get(3);
 			        					String sAddr3 = (String) arrCust.get(4);
 			        					String sAddr4 = (String) arrCust.get(15);
 			        					String sCountry = (String) arrCust.get(5);
 			        					String sZip = (String) arrCust.get(6);
 			        					String sCons = (String) arrCust.get(7);
 			        					String sContactName = (String) arrCust.get(8);
 			        					String sDesgination = (String) arrCust.get(9);
 			        					String sTelNo = (String) arrCust.get(10);
 			        					String sHpNo = (String) arrCust.get(11);
 			        					String sEmail = (String) arrCust.get(12);
 			        					String sFax = (String) arrCust.get(13);
 			        					String sRemarks = (String) arrCust.get(14);
 			        					String ISActive = (String) arrCust.get(16);
 			        					String sPayTerms = (String) arrCust.get(17);
 			        					String sPayMentTerms = (String) arrCust.get(39);
 			        					String sPayInDays = (String) arrCust.get(18);
 			        					String sState = (String) arrCust.get(19);
 			        					String sRcbno = (String) arrCust.get(20);
 			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
 			        					String WEBSITE = (String) arrCust.get(23);
 			        					String FACEBOOK = (String) arrCust.get(24);
 			        					String TWITTER = (String) arrCust.get(25);
 			        					String LINKEDIN = (String) arrCust.get(26);
 			        					String SKYPE = (String) arrCust.get(27);
 			        					String OPENINGBALANCE = (String) arrCust.get(28);
 			        					String WORKPHONE = (String) arrCust.get(29);
 			        					String sTAXTREATMENT = (String) arrCust.get(30);
 			        					String sCountryCode = (String) arrCust.get(31);
 			        					String sBANKNAME = (String) arrCust.get(32);
 			        					String sBRANCH= (String) arrCust.get(33);
 			        					String sIBAN = (String) arrCust.get(34);
 			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
 			        					String companyregnum = (String) arrCust.get(36);
 			        					String PEPPOL = (String) arrCust.get(40);
 			        					String PEPPOL_ID = (String) arrCust.get(41);
 			        					String CURRENCY = (String) arrCust.get(37);
			        					Hashtable htsup = new Hashtable();
			        					htsup.put(IDBConstants.PLANT,childplant);
 			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
 			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
 			        					htsup.put(IConstants.companyregnumber,companyregnum);
 			        					htsup.put("ISPEPPOL", PEPPOL);
 			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
 			        					htsup.put("CURRENCY_ID", CURRENCY);
 			        					htsup.put(IConstants.NAME, sContactName);
 			        					htsup.put(IConstants.DESGINATION, sDesgination);
 			        					htsup.put(IConstants.TELNO, sTelNo);
 			        					htsup.put(IConstants.HPNO, sHpNo);
 			        					htsup.put(IConstants.FAX, sFax);
 			        					htsup.put(IConstants.EMAIL, sEmail);
 			        					htsup.put(IConstants.ADDRESS1, sAddr1);
 			        					htsup.put(IConstants.ADDRESS2, sAddr2);
 			        					htsup.put(IConstants.ADDRESS3, sAddr3);
 			        					htsup.put(IConstants.ADDRESS4, sAddr4);
 			        					if(sState.equalsIgnoreCase("Select State"))
 			        						sState="";
 			        					htsup.put(IConstants.STATE, (sState));
 			        					htsup.put(IConstants.COUNTRY, (sCountry));
 			        					htsup.put(IConstants.ZIP, sZip);
 			        					htsup.put(IConstants.USERFLG1, sCons);
 			        					htsup.put(IConstants.REMARKS, (sRemarks));
 			        					htsup.put(IConstants.PAYTERMS, "");
 					        			htsup.put(IConstants.payment_terms, "");
 					        			htsup.put(IConstants.PAYINDAYS, "");
 			        					htsup.put(IConstants.ISACTIVE, ISActive);
 			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
 			        					htsup.put(IConstants.TRANSPORTID, "0");
 			        					htsup.put(IConstants.RCBNO, sRcbno);
 			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
 			        					htsup.put(IConstants.WEBSITE,WEBSITE);
 			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
 			        					htsup.put(IConstants.TWITTER,TWITTER);
 			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
 			        					htsup.put(IConstants.SKYPE,SKYPE);
 			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
 			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
 			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
 			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
 			        			        	  sBANKNAME="";
 			        			          htsup.put(IDBConstants.BANKNAME,"");
 			        			          htsup.put(IDBConstants.IBAN,sIBAN);
 			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
 			        			          htsup.put("CRAT",new DateUtils().getDateTime());
 			        			          htsup.put("CRBY",username);
 			        			          htsup.put("Comment1", " 0 ");
			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			        				}
		 				        	 }
		        		  	}
		        	  	}
	        	  }
	        	  	}else if(PARENT_PLANT == null){
             		boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int j=0; j < arrLists.size(); j++ ) {
			        			  Map ms = (Map) arrLists.get(j);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
 				        		   	HM.put(IConstants.PLANT, parentplant);
				    			    Itemsup = itemUtil.insertItemSupplier(HM);
 			        				ArrayList arrCust = new CustUtil().getVendorDetails(sup,plant);
 			        				if (arrCust.size() > 0) {
 			        				String sCustCode = (String) arrCust.get(0);
 			        				String sCustName = (String) arrCust.get(1);
 			        				if (!new CustUtil().isExistVendor(sup, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
 			        				{
 			        					String sAddr1 = (String) arrCust.get(2);
 			        					String sAddr2 = (String) arrCust.get(3);
 			        					String sAddr3 = (String) arrCust.get(4);
 			        					String sAddr4 = (String) arrCust.get(15);
 			        					String sCountry = (String) arrCust.get(5);
 			        					String sZip = (String) arrCust.get(6);
 			        					String sCons = (String) arrCust.get(7);
 			        					String sContactName = (String) arrCust.get(8);
 			        					String sDesgination = (String) arrCust.get(9);
 			        					String sTelNo = (String) arrCust.get(10);
 			        					String sHpNo = (String) arrCust.get(11);
 			        					String sEmail = (String) arrCust.get(12);
 			        					String sFax = (String) arrCust.get(13);
 			        					String sRemarks = (String) arrCust.get(14);
 			        					String ISActive = (String) arrCust.get(16);
 			        					String sPayTerms = (String) arrCust.get(17);
 			        					String sPayMentTerms = (String) arrCust.get(39);
 			        					String sPayInDays = (String) arrCust.get(18);
 			        					String sState = (String) arrCust.get(19);
 			        					String sRcbno = (String) arrCust.get(20);
 			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
 			        					String WEBSITE = (String) arrCust.get(23);
 			        					String FACEBOOK = (String) arrCust.get(24);
 			        					String TWITTER = (String) arrCust.get(25);
 			        					String LINKEDIN = (String) arrCust.get(26);
 			        					String SKYPE = (String) arrCust.get(27);
 			        					String OPENINGBALANCE = (String) arrCust.get(28);
 			        					String WORKPHONE = (String) arrCust.get(29);
 			        					String sTAXTREATMENT = (String) arrCust.get(30);
 			        					String sCountryCode = (String) arrCust.get(31);
 			        					String sBANKNAME = (String) arrCust.get(32);
 			        					String sBRANCH= (String) arrCust.get(33);
 			        					String sIBAN = (String) arrCust.get(34);
 			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
 			        					String companyregnum = (String) arrCust.get(36);
 			        					String PEPPOL = (String) arrCust.get(40);
 			        					String PEPPOL_ID = (String) arrCust.get(41);
 			        					String CURRENCY = (String) arrCust.get(37);
 			        					Hashtable htsup = new Hashtable();
 			        					htsup.put(IDBConstants.PLANT,parentplant);
 			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
 			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
 			        					htsup.put(IConstants.companyregnumber,companyregnum);
 			        					htsup.put("ISPEPPOL", PEPPOL);
 			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
 			        					htsup.put("CURRENCY_ID", CURRENCY);
 			        					htsup.put(IConstants.NAME, sContactName);
 			        					htsup.put(IConstants.DESGINATION, sDesgination);
 			        					htsup.put(IConstants.TELNO, sTelNo);
 			        					htsup.put(IConstants.HPNO, sHpNo);
 			        					htsup.put(IConstants.FAX, sFax);
 			        					htsup.put(IConstants.EMAIL, sEmail);
 			        					htsup.put(IConstants.ADDRESS1, sAddr1);
 			        					htsup.put(IConstants.ADDRESS2, sAddr2);
 			        					htsup.put(IConstants.ADDRESS3, sAddr3);
 			        					htsup.put(IConstants.ADDRESS4, sAddr4);
 			        					if(sState.equalsIgnoreCase("Select State"))
 			        						sState="";
 			        					htsup.put(IConstants.STATE, (sState));
 			        					htsup.put(IConstants.COUNTRY, (sCountry));
 			        					htsup.put(IConstants.ZIP, sZip);
 			        					htsup.put(IConstants.USERFLG1, sCons);
 			        					htsup.put(IConstants.REMARKS, (sRemarks));
 			        					htsup.put(IConstants.PAYTERMS, "");
 					        			htsup.put(IConstants.payment_terms, "");
 					        			htsup.put(IConstants.PAYINDAYS, "");
 			        					htsup.put(IConstants.ISACTIVE, ISActive);
 			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
 			        					htsup.put(IConstants.TRANSPORTID, "0");
 			        					htsup.put(IConstants.RCBNO, sRcbno);
 			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
 			        					htsup.put(IConstants.WEBSITE,WEBSITE);
 			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
 			        					htsup.put(IConstants.TWITTER,TWITTER);
 			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
 			        					htsup.put(IConstants.SKYPE,SKYPE);
 			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
 			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
 			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
 			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
 			        			        	  sBANKNAME="";
 			        			          htsup.put(IDBConstants.BANKNAME,"");
 			        			          htsup.put(IDBConstants.IBAN,sIBAN);
 			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
 			        			          htsup.put("CRAT",new DateUtils().getDateTime());
 			        			          htsup.put("CRBY",username);
 			        			          htsup.put("Comment1", " 0 ");
 			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
 			        				}
 			        				}
			        		  }
			        	  }
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int k=0; k < arrList.size(); k++ ) {
			        			  m = (Map) arrList.get(k);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  if(childplant!=plant) {
 				           			String SupplierDesc;
 				        		   	HM.put(IConstants.PLANT, childplant);
				    			    Itemsup = itemUtil.insertItemSupplier(HM);
 			        				ArrayList arrCust = new CustUtil().getVendorDetails(sup,plant);
 			        				if (arrCust.size() > 0) {
 			        				String sCustCode = (String) arrCust.get(0);
 			        				String sCustName = (String) arrCust.get(1);
 			        				if (!new CustUtil().isExistVendor(sup, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
 			        				{
 			        					String sAddr1 = (String) arrCust.get(2);
 			        					String sAddr2 = (String) arrCust.get(3);
 			        					String sAddr3 = (String) arrCust.get(4);
 			        					String sAddr4 = (String) arrCust.get(15);
 			        					String sCountry = (String) arrCust.get(5);
 			        					String sZip = (String) arrCust.get(6);
 			        					String sCons = (String) arrCust.get(7);
 			        					String sContactName = (String) arrCust.get(8);
 			        					String sDesgination = (String) arrCust.get(9);
 			        					String sTelNo = (String) arrCust.get(10);
 			        					String sHpNo = (String) arrCust.get(11);
 			        					String sEmail = (String) arrCust.get(12);
 			        					String sFax = (String) arrCust.get(13);
 			        					String sRemarks = (String) arrCust.get(14);
 			        					String ISActive = (String) arrCust.get(16);
 			        					String sPayTerms = (String) arrCust.get(17);
 			        					String sPayMentTerms = (String) arrCust.get(39);
 			        					String sPayInDays = (String) arrCust.get(18);
 			        					String sState = (String) arrCust.get(19);
 			        					String sRcbno = (String) arrCust.get(20);
 			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
 			        					String WEBSITE = (String) arrCust.get(23);
 			        					String FACEBOOK = (String) arrCust.get(24);
 			        					String TWITTER = (String) arrCust.get(25);
 			        					String LINKEDIN = (String) arrCust.get(26);
 			        					String SKYPE = (String) arrCust.get(27);
 			        					String OPENINGBALANCE = (String) arrCust.get(28);
 			        					String WORKPHONE = (String) arrCust.get(29);
 			        					String sTAXTREATMENT = (String) arrCust.get(30);
 			        					String sCountryCode = (String) arrCust.get(31);
 			        					String sBANKNAME = (String) arrCust.get(32);
 			        					String sBRANCH= (String) arrCust.get(33);
 			        					String sIBAN = (String) arrCust.get(34);
 			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
 			        					String companyregnum = (String) arrCust.get(36);
 			        					String PEPPOL = (String) arrCust.get(40);
 			        					String PEPPOL_ID = (String) arrCust.get(41);
 			        					String CURRENCY = (String) arrCust.get(37);
 			        					Hashtable htsup = new Hashtable();
 			        					htsup.put(IDBConstants.PLANT,childplant);
 			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
 			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
 			        					htsup.put(IConstants.companyregnumber,companyregnum);
 			        					htsup.put("ISPEPPOL", PEPPOL);
 			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
 			        					htsup.put("CURRENCY_ID", CURRENCY);
 			        					htsup.put(IConstants.NAME, sContactName);
 			        					htsup.put(IConstants.DESGINATION, sDesgination);
 			        					htsup.put(IConstants.TELNO, sTelNo);
 			        					htsup.put(IConstants.HPNO, sHpNo);
 			        					htsup.put(IConstants.FAX, sFax);
 			        					htsup.put(IConstants.EMAIL, sEmail);
 			        					htsup.put(IConstants.ADDRESS1, sAddr1);
 			        					htsup.put(IConstants.ADDRESS2, sAddr2);
 			        					htsup.put(IConstants.ADDRESS3, sAddr3);
 			        					htsup.put(IConstants.ADDRESS4, sAddr4);
 			        					if(sState.equalsIgnoreCase("Select State"))
 			        						sState="";
 			        					htsup.put(IConstants.STATE, (sState));
 			        					htsup.put(IConstants.COUNTRY, (sCountry));
 			        					htsup.put(IConstants.ZIP, sZip);
 			        					htsup.put(IConstants.USERFLG1, sCons);
 			        					htsup.put(IConstants.REMARKS, (sRemarks));
 			        					htsup.put(IConstants.PAYTERMS, "");
 					        			htsup.put(IConstants.payment_terms, "");
 					        			htsup.put(IConstants.PAYINDAYS, "");
 			        					htsup.put(IConstants.ISACTIVE, ISActive);
 			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
 			        					htsup.put(IConstants.TRANSPORTID, "0");
 			        					htsup.put(IConstants.RCBNO, sRcbno);
 			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
 			        					htsup.put(IConstants.WEBSITE,WEBSITE);
 			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
 			        					htsup.put(IConstants.TWITTER,TWITTER);
 			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
 			        					htsup.put(IConstants.SKYPE,SKYPE);
 			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
 			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
 			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
 			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
 			        			        	  sBANKNAME="";
 			        			          htsup.put(IDBConstants.BANKNAME,"");
 			        			          htsup.put(IDBConstants.IBAN,sIBAN);
 			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
 			        			          htsup.put("CRAT",new DateUtils().getDateTime());
 			        			          htsup.put("CRBY",username);
 			        			          htsup.put("Comment1", " 0 ");
 			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
 			        				}
 			        				}
			        			  }
			        		  	}
			        	  	}
		        	  	}
             		}
             	  }
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return false;
		}    
		return true;
	}
		
		public boolean addSupDiscount(String plant,String item,String sup,String username,Hashtable HM) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			Hashtable htData = new Hashtable();
			ItemMstDAO itemdao = new ItemMstDAO();
			ItemUtil itemUtil = new ItemUtil();
			boolean deleteItemSupplier=false;
			boolean IBSupplierDiscount=false; 
			try {
				String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);
				boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
				  //IMTHI ADD for CHILD PARENT to insert customer discount
			    if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int m=0; m < arrList.size(); m++ ) {
			        			  Map map = (Map) arrList.get(m);
			        			  String childplant = (String) map.get("CHILD_PLANT");
			 				           			String SupplierDesc;
			 				        		   	HM.put(IConstants.PLANT, childplant);
			 				    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
			 				    			 //SUPPLIER START 
			 			        				ArrayList arrCust = new CustUtil().getVendorDetails(sup,plant);
			 			        				if (arrCust.size() > 0) {
			 			        				String sCustCode = (String) arrCust.get(0);
			 			        				String sCustName = (String) arrCust.get(1);
			 			        				if (!new CustUtil().isExistVendor(sup, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
			 			        				{
			 			        					String sAddr1 = (String) arrCust.get(2);
			 			        					String sAddr2 = (String) arrCust.get(3);
			 			        					String sAddr3 = (String) arrCust.get(4);
			 			        					String sAddr4 = (String) arrCust.get(15);
			 			        					String sCountry = (String) arrCust.get(5);
			 			        					String sZip = (String) arrCust.get(6);
			 			        					String sCons = (String) arrCust.get(7);
			 			        					String sContactName = (String) arrCust.get(8);
			 			        					String sDesgination = (String) arrCust.get(9);
			 			        					String sTelNo = (String) arrCust.get(10);
			 			        					String sHpNo = (String) arrCust.get(11);
			 			        					String sEmail = (String) arrCust.get(12);
			 			        					String sFax = (String) arrCust.get(13);
			 			        					String sRemarks = (String) arrCust.get(14);
			 			        					String ISActive = (String) arrCust.get(16);
			 			        					String sPayTerms = (String) arrCust.get(17);
			 			        					String sPayMentTerms = (String) arrCust.get(39);
			 			        					String sPayInDays = (String) arrCust.get(18);
			 			        					String sState = (String) arrCust.get(19);
			 			        					String sRcbno = (String) arrCust.get(20);
			 			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
			 			        					String WEBSITE = (String) arrCust.get(23);
			 			        					String FACEBOOK = (String) arrCust.get(24);
			 			        					String TWITTER = (String) arrCust.get(25);
			 			        					String LINKEDIN = (String) arrCust.get(26);
			 			        					String SKYPE = (String) arrCust.get(27);
			 			        					String OPENINGBALANCE = (String) arrCust.get(28);
			 			        					String WORKPHONE = (String) arrCust.get(29);
			 			        					String sTAXTREATMENT = (String) arrCust.get(30);
			 			        					String sCountryCode = (String) arrCust.get(31);
			 			        					String sBANKNAME = (String) arrCust.get(32);
			 			        					String sBRANCH= (String) arrCust.get(33);
			 			        					String sIBAN = (String) arrCust.get(34);
			 			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
			 			        					String companyregnum = (String) arrCust.get(36);
			 			        					String PEPPOL = (String) arrCust.get(40);
			 			        					String PEPPOL_ID = (String) arrCust.get(41);
			 			        					String CURRENCY = (String) arrCust.get(37);
			 			        					Hashtable htsup = new Hashtable();
			 			        					htsup.put(IDBConstants.PLANT,childplant);
			 			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
			 			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
			 			        					htsup.put(IConstants.companyregnumber,companyregnum);
			 			        					htsup.put("ISPEPPOL", PEPPOL);
			 			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
			 			        					htsup.put("CURRENCY_ID", CURRENCY);
			 			        					htsup.put(IConstants.NAME, sContactName);
			 			        					htsup.put(IConstants.DESGINATION, sDesgination);
			 			        					htsup.put(IConstants.TELNO, sTelNo);
			 			        					htsup.put(IConstants.HPNO, sHpNo);
			 			        					htsup.put(IConstants.FAX, sFax);
			 			        					htsup.put(IConstants.EMAIL, sEmail);
			 			        					htsup.put(IConstants.ADDRESS1, sAddr1);
			 			        					htsup.put(IConstants.ADDRESS2, sAddr2);
			 			        					htsup.put(IConstants.ADDRESS3, sAddr3);
			 			        					htsup.put(IConstants.ADDRESS4, sAddr4);
			 			        					if(sState.equalsIgnoreCase("Select State"))
			 			        						sState="";
			 			        					htsup.put(IConstants.STATE, (sState));
			 			        					htsup.put(IConstants.COUNTRY, (sCountry));
			 			        					htsup.put(IConstants.ZIP, sZip);
			 			        					htsup.put(IConstants.USERFLG1, sCons);
			 			        					htsup.put(IConstants.REMARKS, (sRemarks));
			 			        					htsup.put(IConstants.PAYTERMS, "");
			 					        			htsup.put(IConstants.payment_terms, "");
			 					        			htsup.put(IConstants.PAYINDAYS, "");
			 			        					htsup.put(IConstants.ISACTIVE, ISActive);
			 			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
			 			        					htsup.put(IConstants.TRANSPORTID, "0");
			 			        					htsup.put(IConstants.RCBNO, sRcbno);
			 			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			 			        					htsup.put(IConstants.WEBSITE,WEBSITE);
			 			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
			 			        					htsup.put(IConstants.TWITTER,TWITTER);
			 			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
			 			        					htsup.put(IConstants.SKYPE,SKYPE);
			 			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			 			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
			 			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			 			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			 			        			        	  sBANKNAME="";
			 			        			          htsup.put(IDBConstants.BANKNAME,"");
			 			        			          htsup.put(IDBConstants.IBAN,sIBAN);
			 			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			 			        			          htsup.put("CRAT",new DateUtils().getDateTime());
			 			        			          htsup.put("CRBY",username);
			 			        			          htsup.put("Comment1", " 0 ");
			 			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			 			        				}
			 			        				}//Supplier END
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int j=0; j < arrLists.size(); j++ ) {
				        			  Map ms = (Map) arrLists.get(j);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				 				           			String SupplierDesc;
				 				        		   	HM.put(IConstants.PLANT, parentplant);
				 				    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
				 				    			   //SUPPLIER START 
				 			        				ArrayList arrCust = new CustUtil().getVendorDetails(sup,plant);
				 			        				if (arrCust.size() > 0) {
				 			        				String sCustCode = (String) arrCust.get(0);
				 			        				String sCustName = (String) arrCust.get(1);
				 			        				if (!new CustUtil().isExistVendor(sup, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
				 			        				{
				 			        					String sAddr1 = (String) arrCust.get(2);
				 			        					String sAddr2 = (String) arrCust.get(3);
				 			        					String sAddr3 = (String) arrCust.get(4);
				 			        					String sAddr4 = (String) arrCust.get(15);
				 			        					String sCountry = (String) arrCust.get(5);
				 			        					String sZip = (String) arrCust.get(6);
				 			        					String sCons = (String) arrCust.get(7);
				 			        					String sContactName = (String) arrCust.get(8);
				 			        					String sDesgination = (String) arrCust.get(9);
				 			        					String sTelNo = (String) arrCust.get(10);
				 			        					String sHpNo = (String) arrCust.get(11);
				 			        					String sEmail = (String) arrCust.get(12);
				 			        					String sFax = (String) arrCust.get(13);
				 			        					String sRemarks = (String) arrCust.get(14);
				 			        					String ISActive = (String) arrCust.get(16);
				 			        					String sPayTerms = (String) arrCust.get(17);
				 			        					String sPayMentTerms = (String) arrCust.get(39);
				 			        					String sPayInDays = (String) arrCust.get(18);
				 			        					String sState = (String) arrCust.get(19);
				 			        					String sRcbno = (String) arrCust.get(20);
				 			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
				 			        					String WEBSITE = (String) arrCust.get(23);
				 			        					String FACEBOOK = (String) arrCust.get(24);
				 			        					String TWITTER = (String) arrCust.get(25);
				 			        					String LINKEDIN = (String) arrCust.get(26);
				 			        					String SKYPE = (String) arrCust.get(27);
				 			        					String OPENINGBALANCE = (String) arrCust.get(28);
				 			        					String WORKPHONE = (String) arrCust.get(29);
				 			        					String sTAXTREATMENT = (String) arrCust.get(30);
				 			        					String sCountryCode = (String) arrCust.get(31);
				 			        					String sBANKNAME = (String) arrCust.get(32);
				 			        					String sBRANCH= (String) arrCust.get(33);
				 			        					String sIBAN = (String) arrCust.get(34);
				 			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
				 			        					String companyregnum = (String) arrCust.get(36);
				 			        					String PEPPOL = (String) arrCust.get(40);
				 			        					String PEPPOL_ID = (String) arrCust.get(41);
				 			        					String CURRENCY = (String) arrCust.get(37);
				 			        					Hashtable htsup = new Hashtable();
				 			        					htsup.put(IDBConstants.PLANT,parentplant);
				 			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
				 			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
				 			        					htsup.put(IConstants.companyregnumber,companyregnum);
				 			        					htsup.put("ISPEPPOL", PEPPOL);
				 			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
				 			        					htsup.put("CURRENCY_ID", CURRENCY);
				 			        					htsup.put(IConstants.NAME, sContactName);
				 			        					htsup.put(IConstants.DESGINATION, sDesgination);
				 			        					htsup.put(IConstants.TELNO, sTelNo);
				 			        					htsup.put(IConstants.HPNO, sHpNo);
				 			        					htsup.put(IConstants.FAX, sFax);
				 			        					htsup.put(IConstants.EMAIL, sEmail);
				 			        					htsup.put(IConstants.ADDRESS1, sAddr1);
				 			        					htsup.put(IConstants.ADDRESS2, sAddr2);
				 			        					htsup.put(IConstants.ADDRESS3, sAddr3);
				 			        					htsup.put(IConstants.ADDRESS4, sAddr4);
				 			        					if(sState.equalsIgnoreCase("Select State"))
				 			        						sState="";
				 			        					htsup.put(IConstants.STATE, (sState));
				 			        					htsup.put(IConstants.COUNTRY, (sCountry));
				 			        					htsup.put(IConstants.ZIP, sZip);
				 			        					htsup.put(IConstants.USERFLG1, sCons);
				 			        					htsup.put(IConstants.REMARKS, (sRemarks));
				 			        					htsup.put(IConstants.PAYTERMS, "");
				 					        			htsup.put(IConstants.payment_terms, "");
				 					        			htsup.put(IConstants.PAYINDAYS, "");
				 			        					htsup.put(IConstants.ISACTIVE, ISActive);
				 			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
				 			        					htsup.put(IConstants.TRANSPORTID, "0");
				 			        					htsup.put(IConstants.RCBNO, sRcbno);
				 			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
				 			        					htsup.put(IConstants.WEBSITE,WEBSITE);
				 			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
				 			        					htsup.put(IConstants.TWITTER,TWITTER);
				 			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
				 			        					htsup.put(IConstants.SKYPE,SKYPE);
				 			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
				 			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
				 			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
				 			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
				 			        			        	  sBANKNAME="";
				 			        			          htsup.put(IDBConstants.BANKNAME,"");
				 			        			          htsup.put(IDBConstants.IBAN,sIBAN);
				 			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
				 			        			          htsup.put("CRAT",new DateUtils().getDateTime());
				 			        			          htsup.put("CRBY",username);
				 			        			          htsup.put("Comment1", " 0 ");
				 			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
				 			        				}
				 			        				}//Supplier END
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int k=0; k < arrList.size(); k++ ) {
				        			  m = (Map) arrList.get(k);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  if(childplant!=plant) {
					 				           			String SupplierDesc;
					 				        		   	HM.put(IConstants.PLANT, childplant);
					 				    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
					 				    			   //SUPPLIER START 
					 			        				ArrayList arrCust = new CustUtil().getVendorDetails(sup,plant);
					 			        				if (arrCust.size() > 0) {
					 			        				String sCustCode = (String) arrCust.get(0);
					 			        				String sCustName = (String) arrCust.get(1);
					 			        				if (!new CustUtil().isExistVendor(sup, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
					 			        				{
					 			        					String sAddr1 = (String) arrCust.get(2);
					 			        					String sAddr2 = (String) arrCust.get(3);
					 			        					String sAddr3 = (String) arrCust.get(4);
					 			        					String sAddr4 = (String) arrCust.get(15);
					 			        					String sCountry = (String) arrCust.get(5);
					 			        					String sZip = (String) arrCust.get(6);
					 			        					String sCons = (String) arrCust.get(7);
					 			        					String sContactName = (String) arrCust.get(8);
					 			        					String sDesgination = (String) arrCust.get(9);
					 			        					String sTelNo = (String) arrCust.get(10);
					 			        					String sHpNo = (String) arrCust.get(11);
					 			        					String sEmail = (String) arrCust.get(12);
					 			        					String sFax = (String) arrCust.get(13);
					 			        					String sRemarks = (String) arrCust.get(14);
					 			        					String ISActive = (String) arrCust.get(16);
					 			        					String sPayTerms = (String) arrCust.get(17);
					 			        					String sPayMentTerms = (String) arrCust.get(39);
					 			        					String sPayInDays = (String) arrCust.get(18);
					 			        					String sState = (String) arrCust.get(19);
					 			        					String sRcbno = (String) arrCust.get(20);
					 			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
					 			        					String WEBSITE = (String) arrCust.get(23);
					 			        					String FACEBOOK = (String) arrCust.get(24);
					 			        					String TWITTER = (String) arrCust.get(25);
					 			        					String LINKEDIN = (String) arrCust.get(26);
					 			        					String SKYPE = (String) arrCust.get(27);
					 			        					String OPENINGBALANCE = (String) arrCust.get(28);
					 			        					String WORKPHONE = (String) arrCust.get(29);
					 			        					String sTAXTREATMENT = (String) arrCust.get(30);
					 			        					String sCountryCode = (String) arrCust.get(31);
					 			        					String sBANKNAME = (String) arrCust.get(32);
					 			        					String sBRANCH= (String) arrCust.get(33);
					 			        					String sIBAN = (String) arrCust.get(34);
					 			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
					 			        					String companyregnum = (String) arrCust.get(36);
					 			        					String PEPPOL = (String) arrCust.get(40);
					 			        					String PEPPOL_ID = (String) arrCust.get(41);
					 			        					String CURRENCY = (String) arrCust.get(37);
					 			        					Hashtable htsup = new Hashtable();
					 			        					htsup.put(IDBConstants.PLANT,childplant);
					 			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
					 			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
					 			        					htsup.put(IConstants.companyregnumber,companyregnum);
					 			        					htsup.put("ISPEPPOL", PEPPOL);
					 			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
					 			        					htsup.put("CURRENCY_ID", CURRENCY);
					 			        					htsup.put(IConstants.NAME, sContactName);
					 			        					htsup.put(IConstants.DESGINATION, sDesgination);
					 			        					htsup.put(IConstants.TELNO, sTelNo);
					 			        					htsup.put(IConstants.HPNO, sHpNo);
					 			        					htsup.put(IConstants.FAX, sFax);
					 			        					htsup.put(IConstants.EMAIL, sEmail);
					 			        					htsup.put(IConstants.ADDRESS1, sAddr1);
					 			        					htsup.put(IConstants.ADDRESS2, sAddr2);
					 			        					htsup.put(IConstants.ADDRESS3, sAddr3);
					 			        					htsup.put(IConstants.ADDRESS4, sAddr4);
					 			        					if(sState.equalsIgnoreCase("Select State"))
					 			        						sState="";
					 			        					htsup.put(IConstants.STATE, (sState));
					 			        					htsup.put(IConstants.COUNTRY, (sCountry));
					 			        					htsup.put(IConstants.ZIP, sZip);
					 			        					htsup.put(IConstants.USERFLG1, sCons);
					 			        					htsup.put(IConstants.REMARKS, (sRemarks));
					 			        					htsup.put(IConstants.PAYTERMS, "");
					 					        			htsup.put(IConstants.payment_terms, "");
					 					        			htsup.put(IConstants.PAYINDAYS, "");
					 			        					htsup.put(IConstants.ISACTIVE, ISActive);
					 			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
					 			        					htsup.put(IConstants.TRANSPORTID, "0");
					 			        					htsup.put(IConstants.RCBNO, sRcbno);
					 			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
					 			        					htsup.put(IConstants.WEBSITE,WEBSITE);
					 			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
					 			        					htsup.put(IConstants.TWITTER,TWITTER);
					 			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
					 			        					htsup.put(IConstants.SKYPE,SKYPE);
					 			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
					 			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
					 			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
					 			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
					 			        			        	  sBANKNAME="";
					 			        			          htsup.put(IDBConstants.BANKNAME,"");
					 			        			          htsup.put(IDBConstants.IBAN,sIBAN);
					 			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
					 			        			          htsup.put("CRAT",new DateUtils().getDateTime());
					 			        			          htsup.put("CRBY",username);
					 			        			          htsup.put("Comment1", " 0 ");
					 			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
					 			        				}
					 			        				}//Supplier END
				        			  }
				        		  	}
				        	  	}
			        	  	}
	             		}
	             	  }//IMTHI END
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return false;
			}    
			return true;
		}
		
		public boolean addCustDiscount(String plant,String item,String cust,String username,Hashtable HM) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			Hashtable htData = new Hashtable();
			ItemMstDAO itemdao = new ItemMstDAO();
			ItemUtil itemUtil = new ItemUtil();
			MovHisDAO mdao = new MovHisDAO(plant);
			boolean OBCustomerDiscount=false; 
			try {
				String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);
				boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
				//IMTHI ADD for CHILD PARENT to insert customer discount
				if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int m=0; m < arrList.size(); m++ ) {
			        			  Map map = (Map) arrList.get(m);
			        			  String childplant = (String) map.get("CHILD_PLANT");
	 			        		   	HM.put(IConstants.PLANT, childplant);
	 			    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
	 			    			   //IMTHI ADD child company to insert in customer type
				        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(cust,plant);
				        			  if (arrCust.size() > 0) {
				        			  	String sCustTypeId = (String) arrCust.get(1);
				        			  	String sCustTypeDesc = (String) arrCust.get(2);
				        			  	String sIsactive = (String) arrCust.get(7);
					        			Hashtable htcust = new Hashtable();
					        			htcust.put(IDBConstants.PLANT,childplant);
					        			htcust.put(IDBConstants.CUSTOMERTYPEID,cust);
					        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
					        		    	Hashtable htcustAdd = new Hashtable();
						        		    	htcustAdd.put(IDBConstants.PLANT,childplant);
						        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
						        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
						        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
						        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						        				htcustAdd.put(IDBConstants.CREATED_BY, username);
						        		    Hashtable htms = new Hashtable();
						        		       htms.put("PLANT",childplant);
						        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
						        	           htms.put("RECID","");
						        	           htms.put("ITEM",sCustTypeId);
						        	           htms.put("REMARKS",sCustTypeDesc);
						        	           htms.put("UPBY",username);   
						        	           htms.put("CRBY",username);
						        	           htms.put("CRAT",new DateUtils().getDateTime());
						        	           htms.put("UPAT",new DateUtils().getDateTime());
						        	           htms.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));      
						        	           htms.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
						        	           htms.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
					        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
					        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
					        		    	}
					        		    }//IMTHI END CUSTOMER TYPE
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int j=0; j < arrLists.size(); j++ ) {
				        			  Map ms = (Map) arrLists.get(j);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
		 			        		   	HM.put(IConstants.PLANT, parentplant);
		 			    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
		 			    			 //IMTHI ADD child company to insert in customer type
					        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(cust,plant);
					        			  if (arrCust.size() > 0) {
					        			  	String sCustTypeId = (String) arrCust.get(1);
					        			  	String sCustTypeDesc = (String) arrCust.get(2);
					        			  	String sIsactive = (String) arrCust.get(7);
						        			Hashtable htcust = new Hashtable();
						        			htcust.put(IDBConstants.PLANT,parentplant);
						        			htcust.put(IDBConstants.CUSTOMERTYPEID,cust);
						        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
						        		    	Hashtable htcustAdd = new Hashtable();
							        		    	htcustAdd.put(IDBConstants.PLANT,parentplant);
							        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
							        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
							        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
							        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							        				htcustAdd.put(IDBConstants.CREATED_BY, username);
							        		    Hashtable htms = new Hashtable();
							        		       htms.put("PLANT",parentplant);
							        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
							        	           htms.put("RECID","");
							        	           htms.put("ITEM",sCustTypeId);
							        	           htms.put("REMARKS",sCustTypeDesc);
							        	           htms.put("UPBY",username);   
							        	           htms.put("CRBY",username);
							        	           htms.put("CRAT",new DateUtils().getDateTime());
							        	           htms.put("UPAT",new DateUtils().getDateTime());
							        	           htms.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));      
							        	           htms.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
							        	           htms.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
						        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
						        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
						        		    	}
						        		    }//IMTHI END CUSTOMER TYPE
				        		  	}
				        	  }
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int k=0; k < arrList.size(); k++ ) {
				        			  m = (Map) arrList.get(k);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  if(childplant!=plant) {
		 			        		   	HM.put(IConstants.PLANT, childplant);
		 			    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
		 			    			 //IMTHI ADD child company to insert in customer type
					        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(cust,plant);
					        			  if (arrCust.size() > 0) {
					        			  	String sCustTypeId = (String) arrCust.get(1);
					        			  	String sCustTypeDesc = (String) arrCust.get(2);
					        			  	String sIsactive = (String) arrCust.get(7);
						        			Hashtable htcust = new Hashtable();
						        			htcust.put(IDBConstants.PLANT,childplant);
						        			htcust.put(IDBConstants.CUSTOMERTYPEID,cust);
						        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
						        		    	Hashtable htcustAdd = new Hashtable();
							        		    	htcustAdd.put(IDBConstants.PLANT,childplant);
							        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
							        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
							        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
							        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							        				htcustAdd.put(IDBConstants.CREATED_BY, username);
							        		    Hashtable htms = new Hashtable();
							        		       htms.put("PLANT",childplant);
							        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
							        	           htms.put("RECID","");
							        	           htms.put("ITEM",sCustTypeId);
							        	           htms.put("REMARKS",sCustTypeDesc);
							        	           htms.put("UPBY",username);   
							        	           htms.put("CRBY",username);
							        	           htms.put("CRAT",new DateUtils().getDateTime());
							        	           htms.put("UPAT",new DateUtils().getDateTime());
							        	           htms.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));      
							        	           htms.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
							        	           htms.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
						        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
						        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
						        		    	}
						        		    }//IMTHI END CUSTOMER TYPE
				        			  	}
				        		  	}
				        	  	}
			        	  	}
	             		}
	             	  }//IMTHI END
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return false;
			}    
			return true;
		}
	
	private JSONObject loadAdditionDesc(String plant, String item) {
		JSONObject resultJson = new JSONObject();
		//JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		String addDes="";
		try {
			JSONArray resultJsonArray = new JSONArray();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			List listQry =   itemMstDAO.getAdditionalDesc(plant,item);
			if (listQry.size() > 0) {
				for (int iCnt =0; iCnt<listQry.size(); iCnt++){
					int iIndex = iCnt + 1;
					Map m=(Map)listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ITEMDETAIL", (String)m.get("ITEMDETAILDESC"));
					resultJsonInt.put("ITEMDETAILDESC", (String)m.get("ITEMDETAILDESC"));
					resultJsonArray.add(resultJsonInt);
					
				}
				resultJson.put("items", resultJsonArray);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}    
		return resultJson;
	}
	
	private JSONObject loadAdditionPrd(String plant, String item) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			JSONArray resultJsonArray = new JSONArray();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			List listQry =   itemMstDAO.getAdditionalPrd(plant,item);
			if (listQry.size() > 0) {
				for (int iCnt =0; iCnt<listQry.size(); iCnt++){
					int iIndex = iCnt + 1;
					Map m=(Map)listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ADDITIONAL", (String)m.get("ADDITIONALITEM"));
					resultJsonInt.put("ADDITIONALITEM", (String)m.get("ADDITIONALITEM"));
					resultJsonArray.add(resultJsonInt);
					
				}
				resultJson.put("items", resultJsonArray);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}    
		return resultJson;
	}
	
private JSONObject GetEmployeeDetails(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        EmployeeDAO EmployeeDAO = new EmployeeDAO();
	        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	            try {

	                String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
	                String empno = StrUtils.fString(request.getParameter("EMPNO")).trim();
	                String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);                           
	                 List listEmp = EmployeeDAO.queryEmpMstDetails(empno,plant,"");
	                        Vector arremp    = (Vector)listEmp.get(0);
	                        if(arremp.size()>0){
	                        	String DOB = StrUtils.fString((String)arremp.get(4));
	                        	/*if(DOB.length() > 0){
	                        		   DOB = DOB.substring(8,10)+"/"+DOB.substring(5, 7)+"/"+DOB.substring(0,4) ;
	                        		}*/
	                        	String BASICSALARY = StrUtils.fString((String)arremp.get(38));
	                        	String HOUSERENTALLOWANCE = StrUtils.fString((String)arremp.get(39));
	                        	String TRANSPORTALLOWANCE = StrUtils.fString((String)arremp.get(40));
	                        	String COMMUNICATIONALLOWANCE = StrUtils.fString((String)arremp.get(41));
	                        	String OTHERALLOWANCE = StrUtils.fString((String)arremp.get(42));
	                        	String BONUS = StrUtils.fString((String)arremp.get(43));
	                        	String COMMISSION = StrUtils.fString((String)arremp.get(44));
	                        	
	                        	String GRATUITY = StrUtils.fString((String)arremp.get(50));
	                        	String AIRTICKET = StrUtils.fString((String)arremp.get(51));
	                        	String LEAVESALARY = StrUtils.fString((String)arremp.get(52));
	                        	
		                        float BASICSALARYVal ="".equals(BASICSALARY) ? 0.0f :  Float.parseFloat(BASICSALARY);
	                            if(BASICSALARYVal==0f){
	                            	BASICSALARY="0.00000";
	                            }else{
	                            	BASICSALARY=BASICSALARY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            BASICSALARY = StrUtils.addZeroes(Double.parseDouble(BASICSALARY), numberOfDecimal);
	                            
	                            float HOUSERENTALLOWANCEVal ="".equals(HOUSERENTALLOWANCE) ? 0.0f :  Float.parseFloat(HOUSERENTALLOWANCE);
	                            if(HOUSERENTALLOWANCEVal==0f){
	                            	HOUSERENTALLOWANCE="0.00000";
	                            }else{
	                            	HOUSERENTALLOWANCE=HOUSERENTALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            HOUSERENTALLOWANCE = StrUtils.addZeroes(Double.parseDouble(HOUSERENTALLOWANCE), numberOfDecimal);
	                            
	                            float TRANSPORTALLOWANCEVal ="".equals(TRANSPORTALLOWANCE) ? 0.0f :  Float.parseFloat(TRANSPORTALLOWANCE);
	                            if(TRANSPORTALLOWANCEVal==0f){
	                            	TRANSPORTALLOWANCE="0.00000";
	                            }else{
	                            	TRANSPORTALLOWANCE=TRANSPORTALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            TRANSPORTALLOWANCE = StrUtils.addZeroes(Double.parseDouble(TRANSPORTALLOWANCE), numberOfDecimal);
	                            
	                            float COMMUNICATIONALLOWANCEVal ="".equals(COMMUNICATIONALLOWANCE) ? 0.0f :  Float.parseFloat(COMMUNICATIONALLOWANCE);
	                            if(COMMUNICATIONALLOWANCEVal==0f){
	                            	COMMUNICATIONALLOWANCE="0.00000";
	                            }else{
	                            	COMMUNICATIONALLOWANCE=COMMUNICATIONALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            COMMUNICATIONALLOWANCE = StrUtils.addZeroes(Double.parseDouble(COMMUNICATIONALLOWANCE), numberOfDecimal);
	                            
	                            float OTHERALLOWANCEVal ="".equals(OTHERALLOWANCE) ? 0.0f :  Float.parseFloat(OTHERALLOWANCE);
	                            if(OTHERALLOWANCEVal==0f){
	                            	OTHERALLOWANCE="0.00000";
	                            }else{
	                            	OTHERALLOWANCE=OTHERALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            OTHERALLOWANCE = StrUtils.addZeroes(Double.parseDouble(OTHERALLOWANCE), numberOfDecimal);
	                            
	                            float BONUSVal ="".equals(BONUS) ? 0.0f :  Float.parseFloat(BONUS);
	                            if(BONUSVal==0f){
	                            	BONUS="0.00000";
	                            }else{
	                            	BONUS=BONUS.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            BONUS = StrUtils.addZeroes(Double.parseDouble(BONUS), numberOfDecimal);
	                            
	                            float COMMISSIONVal ="".equals(COMMISSION) ? 0.0f :  Float.parseFloat(COMMISSION);
	                            if(COMMISSIONVal==0f){
	                            	COMMISSION="0.00000";
	                            }else{
	                            	COMMISSION=COMMISSION.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            COMMISSION = StrUtils.addZeroes(Double.parseDouble(COMMISSION), numberOfDecimal);
	                            
	                            float GRATUITYVal ="".equals(GRATUITY) ? 0.0f :  Float.parseFloat(GRATUITY);
	                            if(GRATUITYVal==0f){
	                            	GRATUITY="0.00000";
	                            }else{
	                            	GRATUITY=GRATUITY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            GRATUITY = StrUtils.addZeroes(Double.parseDouble(GRATUITY), numberOfDecimal);
	                            
	                            float AIRTICKETVal ="".equals(AIRTICKET) ? 0.0f :  Float.parseFloat(AIRTICKET);
	                            if(AIRTICKETVal==0f){
	                            	AIRTICKET="0.00000";
	                            }else{
	                            	AIRTICKET=AIRTICKET.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            AIRTICKET = StrUtils.addZeroes(Double.parseDouble(AIRTICKET), numberOfDecimal);
	                            
	                            float LEAVESALARYVal ="".equals(LEAVESALARY) ? 0.0f :  Float.parseFloat(LEAVESALARY);
	                            if(LEAVESALARYVal==0f){
	                            	LEAVESALARY="0.00000";
	                            }else{
	                            	LEAVESALARY=LEAVESALARY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }                            
	                            LEAVESALARY = StrUtils.addZeroes(Double.parseDouble(LEAVESALARY), numberOfDecimal);
	                            
	                        resultJson.put("status", "100");
	                        JSONObject resultObjectJson = new JSONObject();
	                        resultObjectJson.put("EMPNO",(String)arremp.get(0)); 
	                        resultObjectJson.put("FNAME",StrUtils.fString((String)arremp.get(1)));
	                        resultObjectJson.put("LNAME",StrUtils.fString((String)arremp.get(2)));
	                        resultObjectJson.put("GENDER",StrUtils.fString((String)arremp.get(3)));
	                        resultObjectJson.put("DOB",DOB);
	                        resultObjectJson.put("DEPT",StrUtils.fString((String)arremp.get(5)));
	                        resultObjectJson.put("DESGINATION",StrUtils.fString((String)arremp.get(6)));
	                        resultObjectJson.put("DATEOFJOINING",StrUtils.fString((String)arremp.get(7)));
	                        resultObjectJson.put("DATEOFLEAVING",StrUtils.fString((String)arremp.get(8)));
	                        resultObjectJson.put("NATIONALITY",StrUtils.fString((String)arremp.get(9)));
	                        resultObjectJson.put("TELNO",StrUtils.fString((String)arremp.get(10)));
	                        resultObjectJson.put("HPNO",StrUtils.fString((String)arremp.get(11)));
	                        //resultObjectJson.put("FAX",StrUtils.fString((String)arremp.get(10)));
	                        resultObjectJson.put("EMAIL", StrUtils.fString((String)arremp.get(12)));
	                        resultObjectJson.put("SKYPE", StrUtils.fString((String)arremp.get(13)));
	                        resultObjectJson.put("FACEBOOK", StrUtils.fString((String)arremp.get(14)));
	                        resultObjectJson.put("TWITTER", StrUtils.fString((String)arremp.get(15)));
	                        resultObjectJson.put("LINKEDIN", StrUtils.fString((String)arremp.get(16)));	                        
	                        resultObjectJson.put("PASSPORTNUMBER", StrUtils.fString((String)arremp.get(17)));
	                        resultObjectJson.put("COUNTRYOFISSUE", StrUtils.fString((String)arremp.get(18)));
	                        resultObjectJson.put("PASSPORTEXPIRYDATE", StrUtils.fString((String)arremp.get(19)));
	                        resultObjectJson.put("ADDR1",  StrUtils.fString((String)arremp.get(20)));
	                        resultObjectJson.put("ADDR2",  StrUtils.fString((String)arremp.get(21)));
	                        resultObjectJson.put("ADDR3",StrUtils.fString((String)arremp.get(22)));
	                        resultObjectJson.put("ADDR4",StrUtils.fString((String)arremp.get(23)));
	                        resultObjectJson.put("COUNTRY",StrUtils.fString((String)arremp.get(24)));
	                        resultObjectJson.put("ZIP",StrUtils.fString((String)arremp.get(25)));	                        
	                        resultObjectJson.put("EMIRATESID", StrUtils.fString((String)arremp.get(26)));
	                        resultObjectJson.put("EMIRATESIDEXPIRY", StrUtils.fString((String)arremp.get(27)));
	                        resultObjectJson.put("VISANUMBER", StrUtils.fString((String)arremp.get(28)));
	                        resultObjectJson.put("VISAEXPIRYDATE", StrUtils.fString((String)arremp.get(29)));
	                        resultObjectJson.put("LABOURCARDNUMBER", StrUtils.fString((String)arremp.get(30)));	                        
	                        resultObjectJson.put("WORKPERMITNUMBER", StrUtils.fString((String)arremp.get(31)));
	                        resultObjectJson.put("CONTRACTSTARTDATE", StrUtils.fString((String)arremp.get(32)));
	                        resultObjectJson.put("CONTRACTENDDATE", StrUtils.fString((String)arremp.get(33)));
	                        resultObjectJson.put("BANKNAME",  StrUtils.fString((String)arremp.get(34)));
	                        resultObjectJson.put("BRANCH",  StrUtils.fString((String)arremp.get(35)));
	                        resultObjectJson.put("IBAN",  StrUtils.fString((String)arremp.get(36)));
	                        resultObjectJson.put("BANKROUTINGCODE",StrUtils.fString((String)arremp.get(37)));	                        
	                        resultObjectJson.put("BASICSALARY",BASICSALARY);
	                        resultObjectJson.put("HOUSERENTALLOWANCE",HOUSERENTALLOWANCE);
	                        resultObjectJson.put("TRANSPORTALLOWANCE",TRANSPORTALLOWANCE);
	                        resultObjectJson.put("COMMUNICATIONALLOWANCE",COMMUNICATIONALLOWANCE);
	                        resultObjectJson.put("OTHERALLOWANCE",OTHERALLOWANCE);
	                        resultObjectJson.put("BONUS",BONUS);
	                        resultObjectJson.put("COMMISSION",COMMISSION);	                        
	                        resultObjectJson.put("REMARKS",StrUtils.fString((String)arremp.get(45)));
	                        resultObjectJson.put("IsActive",StrUtils.fString((String)arremp.get(46)));
	                        resultObjectJson.put("STATE",StrUtils.fString((String)arremp.get(47)));
	                        resultObjectJson.put("IMAGEPATH",StrUtils.fString((String)arremp.get(48)));
	                        resultObjectJson.put("COUNTRY_CODE",StrUtils.fString((String)arremp.get(49)));
	                        resultObjectJson.put("GRATUITY",GRATUITY);
	                        resultObjectJson.put("AIRTICKET",AIRTICKET);
	                        resultObjectJson.put("LEAVESALARY",LEAVESALARY);
	                        resultJson.put("result", resultObjectJson);
	                       } else {
	                            resultJson.put("status", "99");
	                    }
	                    return resultJson;
	            } catch (Exception daRE) {
	                    resultJson.put("status", "99");
	                    return resultJson;
	            }
	    } 
private JSONObject loadAlternateEmployee(String plant, String empid) {
	JSONObject resultJson = new JSONObject();
	try {
		JSONArray resultJsonArray = new JSONArray();
		EmployeeDAO empMstDAO = new EmployeeDAO();
		empMstDAO.setmLogger(mLogger);
		List<String> resultSet = empMstDAO.getAllAssignedAlternateEmployees(plant, empid);
		if (resultSet.size() > 0) {
			for (String resultValue : resultSet) {
				resultJsonArray.add(resultValue);
			}
			resultJson.put("result", resultJsonArray);
			resultJson.put("status", "100");
		} else {
			resultJson.put("status", "99");
		}
		return resultJson;
	} catch (Exception daRE) {
		resultJson.put("status", "99");
		return resultJson;
	}
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

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	private JSONObject GetUNITPRICE(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			 ItemMstUtil itemUtil = new ItemMstUtil();
			 itemUtil.setmLogger(mLogger);
			 POSUtil posUtil = new POSUtil();
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM"));
			String currencyid = StrUtils.fString(request.getParameter("CURRENCY"));
			String custcode = StrUtils.fString(request.getParameter("CUST_CODE"));
			
			JSONObject resultJsonObject = new JSONObject();
			List resultList = itemUtil.getMasterProductList(itemNo,plant,"");
			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				//String sUNITPRICE = (String) m.get("UNITPRICE");
				String sUNITPRICE =posUtil.getLocalCurrencyConvert(plant,currencyid,itemNo);
				String sUNITPRICERD =posUtil.getLocalCurrency(plant,currencyid,itemNo);
				String minSellingconvertedcost = posUtil.getminSellingLocalCurrencyConvert(plant,currencyid,itemNo);
				String OBDiscount=posUtil.getOBDiscountSelectedItem(plant,currencyid,itemNo,custcode);
				String discounttype="";
                int plusIndex = OBDiscount.indexOf("%");
                if (plusIndex != -1) {
               	 	OBDiscount = OBDiscount.substring(0, plusIndex);
               	 	discounttype = "BYPERCENTAGE";
                    }
					
				resultJsonObject.put("UNITPRICE", sUNITPRICE);
				resultJsonObject.put("UNITPRICERD", sUNITPRICERD);
				resultJsonObject.put("outgoingOBDiscount", OBDiscount);
				resultJsonObject.put("OBDiscountType", discounttype);
				resultJsonObject.put("minSellingConvertedUnitCost", minSellingconvertedcost);
				
				JSONArray arr = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("UNITPRICE", sUNITPRICE);
				obj.put("UNITPRICERD", sUNITPRICERD);
				obj.put("outgoingOBDiscount", OBDiscount);
				obj.put("OBDiscountType", discounttype);
				obj.put("minSellingConvertedUnitCost", minSellingconvertedcost);
				arr.add(obj);
				resultJson.put("result", resultJsonObject);
				resultJson.put("items", arr);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}	
		private JSONObject GetUOM(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			 ItemMstUtil itemUtil = new ItemMstUtil();
			 itemUtil.setmLogger(mLogger);

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM"));
			
			JSONObject resultJsonObject = new JSONObject();
			List resultList = itemUtil.getMasterProductList(itemNo,plant,"");
			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				String sUOM = (String) m.get("SALESUOM");
				String pUOM = (String) m.get("PURCHASEUOM");
				String inUOM = (String) m.get("INVENTORYUOM");
				resultJsonObject.put("SALESUOM", sUOM);
				resultJsonObject.put("PURCHASEUOM", pUOM);
				resultJsonObject.put("INVENTORYUOM", inUOM);
				JSONArray arr = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("SALESUOM", sUOM);
				obj.put("PURCHASEUOM", pUOM);
				obj.put("INVENTORYUOM", inUOM);
				arr.add(obj);
				resultJson.put("result", resultJsonObject);
				resultJson.put("items", arr);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}	
		private JSONObject GetAlternateProd(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			try {
				 ItemMstUtil itemUtil = new ItemMstUtil();
				 itemUtil.setmLogger(mLogger);

				String plant = StrUtils.fString(request.getParameter("PLANT"));
				String itemNo = StrUtils.fString(request.getParameter("ITEM"));
				String itemDesc = StrUtils.fString(request.getParameter("ITEMDESC"));
				String cond="";
				if(!itemDesc.isEmpty())
				{
					cond="AND ITEMDESC='"+itemDesc+"'";
				}
				
				List resultList = itemUtil.getMasterProductList(itemNo,plant,cond);
				JSONArray arr = new JSONArray();
				for(int i=0;i<resultList.size();i++)
				{
						Map m = (Map) resultList.get(i);
						String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
	                    String prodImg=((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath);
	                    //m.put("CATLOGPATH",prodImg);
	                    m.replace("CATLOGPATH",prodImg);
						JSONObject obj = new JSONObject();
						obj.putAll(m);
			
						arr.add(obj);
				}
				resultJson.put("items", arr);
				resultJson.put("status", "100");
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}
		}	
		private JSONObject GetAlternateProdByDesc(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			try {
				 ItemMstUtil itemUtil = new ItemMstUtil();
				 itemUtil.setmLogger(mLogger);

				String plant = StrUtils.fString(request.getParameter("PLANT"));
				String itemDesc = StrUtils.fString(request.getParameter("ITEM"));
				String cond="";
			
				List resultList = itemUtil.getMasterProductListByDesc(itemDesc,plant,cond);
				JSONArray arr = new JSONArray();
				for(int i=0;i<resultList.size();i++)
				{
						Map m = (Map) resultList.get(i);
						JSONObject obj = new JSONObject();
						obj.putAll(m);
			
						arr.add(obj);
				}
				resultJson.put("items", arr);
				resultJson.put("status", "100");
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}
		}
		private JSONObject GetAlternateProdByItem(HttpServletRequest request,HttpServletResponse response) {
			JSONObject resultJson = new JSONObject();
			try {
				 ItemMstUtil itemUtil = new ItemMstUtil();
				 itemUtil.setmLogger(mLogger);

				String plant = StrUtils.fString(request.getParameter("PLANT"));
				String itemNo = StrUtils.fString(request.getParameter("ITEM"));
				//String itemDesc = StrUtils.fString(request.getParameter("ITEMDESC"));
				
				List resultList = itemUtil.getMasterProductListByItem(itemNo,plant,"");
				JSONArray arr = new JSONArray();
				for(int i=0;i<resultList.size();i++)
				{
						Map m = (Map) resultList.get(i);
						JSONObject obj = new JSONObject();
						obj.putAll(m);
			
						arr.add(obj);
				}
				resultJson.put("items", arr);
				resultJson.put("status", "100");
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}
		}
	private JSONObject GetLOC(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String loc = StrUtils.fString(request.getParameter("LOC"));
			
			JSONObject resultJsonObject = new JSONObject();
			boolean isvalidlocforUser =  uslocUtil.isValidLocInLocmstForUser(plant, "", loc);
			if (isvalidlocforUser) {
				resultJsonObject.put("LOC", loc);
				JSONArray arr = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("LOC", loc);
				arr.add(obj);
				resultJson.put("result", resultJsonObject);
				resultJson.put("items", arr);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject validateProduct(String plant, String userId,
			String item) {
		JSONObject resultJson = new JSONObject();
		try {
			
			ItemUtil locMstDAO = new ItemUtil();
			locMstDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.ITEM, item);
			if (locMstDAO.isExistsItemMst(ht)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject ChecknonActiveProduct(String plant,
			String item) {
		JSONObject resultJson = new JSONObject();
		try {
			String result = "";
			//boolean itemDeleted = false;
			   Hashtable htinv = new Hashtable();
		        htinv.put(IConstants.ITEM,item);
		        htinv.put(IConstants.PLANT,plant);
		        //check in Invmst
		        InvMstDAO invdao = new InvMstDAO();
		       // itemDeleted = invdao.isExisit(htinv," qty>0");
			
			if (invdao.isExisit(htinv," qty>0")) {
				//if (locMstDAO.isExistsItemMst(ht)) {
				resultJson.put("status", "100");
				//result = "<font class = " + IConstants.FAILED_COLOR + ">Product Exists In Inventory or Non Active</font>";
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	@SuppressWarnings("unchecked")
	private JSONObject validateBatchForPickingMultiUOM(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			String location = StrUtils.fString(request.getParameter("LOC"));
			String uom = StrUtils.fString(request.getParameter("UOM"));
			JSONObject resultJsonObject = new JSONObject();
			List resultList = invMstDAO.getTotalQuantityForOutBoundPickingBatchByWMSMuliUOM(plant,itemNo, location, batch,uom);
			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				String sBatch = (String) m.get("batch");
				String qty = (String) m.get("qty");
				resultJsonObject.put("BATCH", sBatch);
				resultJsonObject.put("QTY", qty);
				JSONArray arr = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("INVENTORYQUANTITY", qty);
				arr.add(obj);
				resultJson.put("result", resultJsonObject);
				resultJson.put("items", arr);
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
	public void addProduct(HttpServletRequest request,
			HttpServletResponse response) {
		try {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		if(isMultipart) {
			int imgCount = 0;
			UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
			DateUtils dateutils = new DateUtils();
			HttpSession session = request.getSession();
			ItemUtil itemUtil = new ItemUtil();
			StrUtils strUtils = new StrUtils();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//			String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
//			if(CHECKplantCompany == null)
//				CHECKplantCompany = "0";
		  String result = "",strpath = "" , catlogpath = "",catlogpaths = "";
   		  ItemUtil _itemutil = new ItemUtil();
   		  CatalogUtil _catalogUtil = new CatalogUtil();
   		  TblControlDAO _TblControlDAO =new TblControlDAO();
   		  String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
   		  boolean prdflag = false , flag = false;
   		  boolean imageSizeflg = false;
 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ plant;
 			String filetempLocation = DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
			String ITEM = "" , ITEM1 = "" ,DESC = "", NONSTOCKFLAG = "",REMARKS = "",NONSTKTYPE = "",UOM = "",ISPOSDISCOUNT ="",ISNEWARRIVAL="",ISTOPSELLING="",ISBASICUOM ="",ISCHILDCAL="",
					PRD_CLS_ID = "",LOC_ID="",PRD_DEPT_ID="",DEPT_DISPLAY_ID="",HSCODE = "" ,ARTIST = "",COO = "",PRD_BRAND = "",VINNO = "" ,taxbylabelordermanagement ="",DIMENSION="",
					PRODGST ="",MODEL = "",ITEM_CONDITION = "", TITLE ="",IMAGE_UPLOAD = "",PURCHASEUOM = "",COST = "",
					DYNAMIC_SUPPLIERDISCOUNT_SIZE = "",IBDISCOUNT = "" ,DYNAMIC_SUPPLIER_DISCOUNT_0 = "",SUPPLIER_0 = "",
					DYNAMIC_SUPPLIER_DISCOUNT_SIZE = "" , SALESUOM = "" , NETWEIGHT = "" , PRICE = "",GROSSWEIGHT = "",MINSELLINGPRICE = "",
					DISCOUNT = "",DYNAMIC_CUSTOMERDISCOUNT_SIZE = "",OBDISCOUNT = "",DYNAMIC_CUSTOMER_DISCOUNT_0 = "",CUSTOMER_TYPE_ID_0 = "",
					DYNAMIC_CUSTOMER_DISCOUNT_SIZE = "", RENTALUOM = "",RENTALPRICE = "",SERVICEUOM = "",SERVICEPRICE = "",
					INVENTORYUOM = "",STKQTY = "",MAXSTKQTY = "",LOC_0="",sSAVE_RED="",iscompro="",cppi="",incprice="",incpriceunit="",vendno="",vendname="",DYNAMIC_ITEMSUPPLIER_SIZE="";		
			
			List DYNAMIC_CUSTOMER_DISCOUNT = new ArrayList(), CUSTOMER_TYPE_ID = new ArrayList(),SUPPLIER = new ArrayList(),DYNAMIC_SUPPLIER_DISCOUNT = new ArrayList(),ITEMSUPPLIER = new ArrayList();
					int DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT=0,DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT=0,DYNAMIC_CUSTOMER_DISCOUNTCount  = 0, CUSTOMER_TYPE_IDCount  = 0, SUPPLIERCount  = 0, DYNAMIC_SUPPLIER_DISCOUNTCount  = 0,DYNAMIC_ITEMSUPPLIER_SIZE_INT=0,ITEMSUPPLIERCount=0;
			List  ADITIONALPRD = new ArrayList(),DETAILDESC = new ArrayList();
					int PRODUCT_Count  = 0, DESC_Count  = 0;
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);//Check Parent Plant or child plant
			boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
			Hashtable htData = new Hashtable();	
			String field = "";
			System.out.println("product details list :::::: "+items.size());
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {

					if (item.getFieldName()
							.equalsIgnoreCase("ITEM1")) {
						ITEM1 = StrUtils.fString(item.getString());
					}
					if (item.getFieldName()
							.equalsIgnoreCase("ITEM")) {
						ITEM = StrUtils.fString(item.getString());
					}
					if (item.getFieldName()
							.equalsIgnoreCase("DESC")) {
						DESC = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("NONSTOCKFLAG")) {
						NONSTOCKFLAG = StrUtils.fString(item.getString());
						
					}
					if (item.getFieldName().equalsIgnoreCase("REMARKS")) {
						REMARKS = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("NONSTKTYPE")) {
						NONSTKTYPE = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("UOM")) {
						UOM = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ISBASICUOM")) {
						ISBASICUOM = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ISPOSDISCOUNT")) {
						ISPOSDISCOUNT = (StrUtils.fString(item.getString())!= null) ? "1": "0";
					}
					if (item.getFieldName().equalsIgnoreCase("ISNEWARRIVAL")) {
						ISNEWARRIVAL = (StrUtils.fString(item.getString())!= null) ? "1": "0";
					}
					if (item.getFieldName().equalsIgnoreCase("ISTOPSELLING")) {
						ISTOPSELLING = (StrUtils.fString(item.getString())!= null) ? "1": "0";
					}
					if (item.getFieldName().equalsIgnoreCase("ISCHILDCAL")) {
						ISCHILDCAL = (StrUtils.fString(item.getString())!= null) ? "1": "0";
					}
					if (item.getFieldName().equalsIgnoreCase("LOC_ID")) {
						LOC_ID = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("PRD_DEPT_ID")) {
						PRD_DEPT_ID = StrUtils.fString(item.getString());
					}//Resvi
					
					if (item.getFieldName().equalsIgnoreCase("PRD_CLS_ID")) {
						PRD_CLS_ID = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("HSCODE")) {
						HSCODE = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ARTIST")) {
						ARTIST = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("COO")) {
						COO = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("PRD_BRAND")) {
						PRD_BRAND =StrUtils.fString(item.getString()).trim();
					}
					
						/*
						 * if (item.getFieldName().equalsIgnoreCase("DEPT_DISPLAY_ID")) {
						 * DEPT_DISPLAY_ID =StrUtils.fString(item.getString()).trim(); }
						 */
					
					if (item.getFieldName().equalsIgnoreCase("VINNO")) {
						VINNO = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("taxbylabelordermanagement")) {
						taxbylabelordermanagement=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("PRODGST")) {
						PRODGST = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("MODEL")) {
						MODEL =StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("ITEM_CONDITION")) {
						ITEM_CONDITION = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("TITLE")) {
						TITLE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("IMAGE_UPLOAD")) {
						IMAGE_UPLOAD =StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("PURCHASEUOM")) {
						PURCHASEUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("COST")) {
						COST = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIERDISCOUNT_SIZE")) {
						DYNAMIC_SUPPLIERDISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_ITEMSUPPLIER_SIZE")) {
						DYNAMIC_ITEMSUPPLIER_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("IBDISCOUNT")) {
						IBDISCOUNT =StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIER_DISCOUNT_0")) {
						DYNAMIC_SUPPLIER_DISCOUNT_0 =StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("SUPPLIER_0")) {
						SUPPLIER_0 = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIER_DISCOUNT_SIZE")) {
						DYNAMIC_SUPPLIER_DISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("SALESUOM")) {
						SALESUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("NETWEIGHT")) {
						NETWEIGHT=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("PRICE")) {
						PRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("GROSSWEIGHT")) {
						GROSSWEIGHT=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DIMENSION")) {
						DIMENSION=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("MINSELLINGPRICE")) {
						MINSELLINGPRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DISCOUNT")) {
						DISCOUNT=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMERDISCOUNT_SIZE")) {
						DYNAMIC_CUSTOMERDISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("OBDISCOUNT")) {
						OBDISCOUNT=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMER_DISCOUNT_0")) {
						DYNAMIC_CUSTOMER_DISCOUNT_0=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("CUSTOMER_TYPE_ID_0")) {
						CUSTOMER_TYPE_ID_0=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMER_DISCOUNT_SIZE")) {
						DYNAMIC_CUSTOMER_DISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("RENTALUOM")) {
						RENTALUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("RENTALPRICE")) {
						RENTALPRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("SERVICEUOM")) {
						SERVICEUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("SERVICEPRICE")) {
						SERVICEPRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("INVENTORYUOM")) {
						INVENTORYUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("STKQTY")) {
						STKQTY=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("MAXSTKQTY")) {
						MAXSTKQTY=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("LOC_0")) {
						LOC_0=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("ISCOMPRO")) {
						iscompro=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("CPPI")) {
						cppi=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("INCPRICE")) {
						incprice=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("INCPRICEUNIT")) {
						incpriceunit=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName()
							.equalsIgnoreCase("vendno")) {
						vendno = StrUtils.fString(item.getString()).trim();
					}
					
					if(ITEM.length() <= 0)
						ITEM = ITEM1;
					
					//detail desc
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("DESCRIPTION")) {
							DETAILDESC.add(DESC_Count, StrUtils.fString(item.getString()).trim());
							DESC_Count++;
						}
					}
					
					//additional product
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("PRODUCT")) {
							ADITIONALPRD.add(PRODUCT_Count, StrUtils.fString(item.getString()).trim());
							PRODUCT_Count++;
						}
					}
				}
				else if (!item.isFormField()
						&& (item.getName().length() > 0)) {
					String fileName = item.getName();
					imgCount++;
					long size = item.getSize();
					
					String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
					System.out.println("Extensions:::::::" + extension);
					if (!imageFormatsList.contains(extension)) {
						result = "<font color=\"red\"> Image extension not valid </font>";
						imageSizeflg = true;
					}
					
					/*if(!(plant.equalsIgnoreCase("C2716640758S2T") || plant.equalsIgnoreCase("C1255800687S2T") || plant.equalsIgnoreCase("C4376460171S2T") 
							|| plant.equalsIgnoreCase("C5500747293S2T") || plant.equalsIgnoreCase("C697464484S2T") || plant.equalsIgnoreCase("C7743535839S2T") 
							|| plant.equalsIgnoreCase("C947002346S2T") || plant.equalsIgnoreCase("test"))){
//					size = size / 1024;
					// size = size / 1000;
					System.out.println("size of the Image imported :::"
							+ size);
					//checking image size for 2MB
//					if (size > 2040) // condtn checking Image size
					if (size > 250000) 
					{
						result = "<font color=\"red\">  Catalog Image size greater than 250 Pixel </font>";
//						result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";

						imageSizeflg = true;

					}
					}*/
					if(PARENT_PLANT == null){
						String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int i=0; i < arrLists.size(); i++ ) {
			        			  Map ms = (Map) arrLists.get(i);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
			        		  }
			        	  }
					}
					//File path = new File(fileLocation);
					File path = new File(filetempLocation);
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					fileName = fileName.substring(fileName
							.lastIndexOf("\\") + 1);
					File uploadedFile = new File("");
					if(imgCount==1) {
					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
					}else {
						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
					}

					// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
					// if (uploadedFile.exists()) {
					// uploadedFile.delete();
					// }
					strpath = path + "/" + fileName;
					if(imgCount==1) 
					//catlogpath = uploadedFile.getAbsolutePath();
					  filetempLocation = uploadedFile.getAbsolutePath();
						
					
					
					if (!imageSizeflg && !uploadedFile.exists())
						item.write(uploadedFile);
					
					//File sourceimage = new File(catlogpath);
					File sourceimage = new File(filetempLocation);
					BufferedImage originalImage = javax.imageio.ImageIO.read(sourceimage);
					path = new File(fileLocation);
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					//File outputFile = new File(path + "/Com/" + strUtils.RemoveSlash(ITEM)+".JPEG");
					File outputFile = new File(path +"/"+ strUtils.RemoveSlash(ITEM)+".JPEG");
					catlogpath = outputFile.getAbsolutePath();
					saveCompressedImage(originalImage, "JPEG", outputFile);
					if (uploadedFile.exists()) {
						 uploadedFile.delete();
					}
					
					//File sourceimage = new File(catlogpath);
					//BufferedImage image = javax.imageio.ImageIO.read(sourceimage);
					
					 //int newsize = 200;// size of the new image.
                     //take the file as inputstream.
                     //InputStream imageStream = item.getInputStream();
                     //read the image as a BufferedImage.
                     //BufferedImage image = javax.imageio.ImageIO.read(imageStream); 
                     //cal the sacleImage method.
                     //BufferedImage newImage = this.scaleImage(image, newsize);
                     //write file.
                     
                     
                     //File file = new File(path + "/Com/" + strUtils.RemoveSlash(ITEM)+".JPEG");
                     //javax.imageio.ImageIO.write(newImage, "JPG", file);
                     
					// delete temp uploaded file
					/*File tempPath = new File(filetempLocation);
					if (tempPath.exists()) {
						if(imgCount==1) {
						File tempUploadedfile = new File(tempPath + "/"
								+ strUtils.RemoveSlash(ITEM)+".JPEG");
						if (tempUploadedfile.exists()) {
							tempUploadedfile.delete();
						}
						}else {
							File tempUploadedfile = new File(tempPath + "/"
									+ strUtils.RemoveSlash(ITEM) +"_"+imgCount+ ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
					}*/
					Hashtable htCatalog = new Hashtable();
					if(imgCount>1) { 
					catlogpaths = uploadedFile.getAbsolutePath();
					
					int length = catlogpaths.length();
					int len = length-4;
					String new_word = catlogpaths.substring(len - 2);
					char firstChar = new_word.charAt(0);
					String usertime  = Character.toString(firstChar);
					
					htCatalog.put(IDBConstants.PRODUCTID, ITEM);
					htCatalog.put(IConstants.PLANT, plant);
					htCatalog.put(IConstants.CREATED_BY, userName);
					htCatalog.put(IConstants.ISACTIVE, "Y");
					htCatalog.put(IDBConstants.CATLOGPATH, catlogpaths);
					htCatalog.put("USERTIME1", usertime);
					flag = _catalogUtil.insertMst(htCatalog);
					}
					
					//IMAGE START
					  if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  //String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;--Azees Image to Update only Parent Company 23.09.24
				        			  String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ PARENT_PLANT;
				        			  path = new File(fileLocations);
					  					if (!path.exists()) {
					  						boolean status = path.mkdirs();
					  					}
					  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					  					
					  					if(imgCount==1) {
					  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
					  					}else {
					  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
					  					}
					  					
//					  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
					  					strpath = path + "/" + fileName;
					  					String childcatlogpath = uploadedFile.getAbsolutePath();
					  					/*File childpath = new File(childcatlogpath);
					  					File parentpath = new File("");
					  					if(imgCount==1) {
					  						parentpath = new File(catlogpath);
					  					}else {
					  						parentpath = new File(catlogpaths);
					  					}
					  					if (!imageSizeflg && !uploadedFile.exists())
					  					FileUtils.copyFile(parentpath, childpath);*/
					  					
					  					htCatalog.put(IConstants.PLANT, childplant);
					  					htCatalog.put(IDBConstants.CATLOGPATH, childcatlogpath);
										flag = _catalogUtil.insertMst(htCatalog);
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int i=0; i < arrLists.size(); i++ ) {
					        			  Map ms = (Map) arrLists.get(i);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
					        			  String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
					        			  path = new File(fileLocations);
						  					if (!path.exists()) {
						  						boolean status = path.mkdirs();
						  					}
						  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						  					
						  					if(imgCount==1) {
						  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
						  					}else {
						  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
						  					}
						  					
//						  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
						  					strpath = path + "/" + fileName;
						  					String childcatlogpath = uploadedFile.getAbsolutePath();
						  					/*File childpath = new File(childcatlogpath);
//						  					File parentpath = new File(catlogpath);
						  					File parentpath = new File("");
						  					if(imgCount==1) {
						  						parentpath = new File(catlogpath);
						  					}else {
						  						parentpath = new File(catlogpaths);
						  					}
						  					if (!imageSizeflg && !uploadedFile.exists())
						  					FileUtils.copyFile(parentpath, childpath);*/
						  					
						  					htCatalog.put(IConstants.PLANT, parentplant);
						  					htCatalog.put(IDBConstants.CATLOGPATH, childcatlogpath);
											flag = _catalogUtil.insertMst(htCatalog);
		
					        		  }
					        	  }
					        	  
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
						        			  //String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;--Azees Image to Update only Parent Company 23.09.24
						        			  String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
						        			  path = new File(fileLocations);
							  					if (!path.exists()) {
							  						boolean status = path.mkdirs();
							  					}
							  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							  					
							  					if(imgCount==1) {
							  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
							  					}else {
							  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
							  					}
							  					
//							  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
							  					strpath = path + "/" + fileName;
							  					String childcatlogpath = uploadedFile.getAbsolutePath();
							  					/*File childpath = new File(childcatlogpath);
//							  					File parentpath = new File(catlogpath);
							  					File parentpath = new File("");
							  					if(imgCount==1) {
							  						parentpath = new File(catlogpath);
							  					}else {
							  						parentpath = new File(catlogpaths);
							  					}
							  					if (!imageSizeflg && !uploadedFile.exists())
							  					FileUtils.copyFile(parentpath, childpath);*/
							  					
							  					htCatalog.put(IConstants.PLANT, childplant);
							  					htCatalog.put(IDBConstants.CATLOGPATH, childcatlogpath);
												flag = _catalogUtil.insertMst(htCatalog);
					        			  }
					        		  	}
					        	  	}
				        	  }	
		             	  }
		             	}
					  
					  //IMAGE END

				}
				
				//GET CUSTOMER DISCOUNT
				if(DYNAMIC_CUSTOMERDISCOUNT_SIZE!="")
				{
					DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_CUSTOMERDISCOUNT_SIZE)).intValue();
	        	  for(int nameCount = 0; nameCount<=DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT;nameCount++){
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount)) {
						DYNAMIC_CUSTOMER_DISCOUNT.add(DYNAMIC_CUSTOMER_DISCOUNTCount, StrUtils.fString(item.getString()).trim());
						DYNAMIC_CUSTOMER_DISCOUNTCount++;
					}
				}
				
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("CUSTOMER_TYPE_ID_"+nameCount)) {
						CUSTOMER_TYPE_ID.add(CUSTOMER_TYPE_IDCount, StrUtils.fString(item.getString()).trim());
						CUSTOMER_TYPE_IDCount++;
					}
				}
	        	  }
	        	}
				
				if(DYNAMIC_SUPPLIERDISCOUNT_SIZE!="")
				{
				DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_SUPPLIERDISCOUNT_SIZE)).intValue();
	        	  for(int nameCount = 0; nameCount<=DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT;nameCount++){
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount)) {
						DYNAMIC_SUPPLIER_DISCOUNT.add(DYNAMIC_SUPPLIER_DISCOUNTCount, StrUtils.fString(item.getString()).trim());
						DYNAMIC_SUPPLIER_DISCOUNTCount++;
					}
				}
				
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("SUPPLIER_"+nameCount)) {
						SUPPLIER.add(SUPPLIERCount, StrUtils.fString(item.getString()).trim());
						SUPPLIERCount++;
					}
				}
	        	  }
	        	}
				
				if(DYNAMIC_ITEMSUPPLIER_SIZE!="")
				{
					DYNAMIC_ITEMSUPPLIER_SIZE_INT = (new Integer(DYNAMIC_ITEMSUPPLIER_SIZE)).intValue();
					for(int nameCount = 0; nameCount<=DYNAMIC_ITEMSUPPLIER_SIZE_INT;nameCount++){
						if (item.isFormField()) {
							if (item.getFieldName().equalsIgnoreCase("ITEMSUPPLIER_"+nameCount)) {
								ITEMSUPPLIER.add(SUPPLIERCount, StrUtils.fString(item.getString()).trim());
								ITEMSUPPLIERCount++;
							}
						}
					}
				}
			}
				ut.begin();
				String loc = "";
				if (imageSizeflg) {
					 response
						.sendRedirect("../product/new?result="
								+ result + "&action=view");
				}else {
			    if(!(itemUtil.isExistsItemMst(ITEM,plant))) // if the item exists already
			    {
	                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
	                        if(ITEM.equals("")){
	                            throw new Exception("");
	                    }
//	                        if(specialcharsnotAllowed.length()>0){
//	                                throw new Exception("Product ID  value : '" + ITEM + "' has special characters "+specialcharsnotAllowed+" that are  not allowed ");
//	                        }
	                        
	                        if(loc.length()>0){
	                        boolean isExistLoc = false;
	    					LocUtil locUtil = new LocUtil();
	    					isExistLoc = locUtil.isValidLocInLocmst(plant, loc);
	    					if(!isExistLoc){
	    						throw new Exception("Location: " + loc + " is not valid location");
	    						}
	                        }
	                        DESC=strUtils.InsertQuotes(DESC);
			          Hashtable ht = new Hashtable();
			          ht.put(IConstants.PLANT,plant);
			          ht.put(IConstants.ITEM,ITEM);
			          ht.put(IConstants.ITEM_DESC,DESC);
			          ht.put(IConstants.ITEMMST_REMARK1,strUtils.InsertQuotes(REMARKS));
			          ht.put(IConstants.ITEMMST_ITEM_TYPE,ARTIST);//itemtype
			       // Start code added by Deen for product brand on 11/9/12
			          ht.put(IConstants.PRDBRANDID ,PRD_BRAND);
			       // End code added by Deen for product brand on 11/9/12
			          ht.put("STKUOM",UOM);
			          ht.put(IConstants.ITEMMST_REMARK4,strUtils.InsertQuotes(TITLE));//remark4
			          //ht.put(IConstants.ITEMMST_REMARK2,strUtils.InsertQuotes(sMedium)); // remark2
			          ht.put(IConstants.ITEMMST_REMARK3,strUtils.InsertQuotes(ITEM_CONDITION));//remark3
			          ht.put(IConstants.PRDCLSID,PRD_CLS_ID);//prd_cls_id
			          ht.put("LOC_ID",LOC_ID);
			          ht.put(IConstants.PRDDEPTID,PRD_DEPT_ID);//prd_dept_id
//			          ht.put(IConstants.DEPTDISPLAY,DEPT_DISPLAY_ID);//Dept Display
			          ht.put(IConstants.PRICE,PRICE);
			          ht.put(IConstants.ISACTIVE,"Y");
			          ht.put(IConstants.COST,COST);
			          ht.put(IConstants.MIN_S_PRICE,MINSELLINGPRICE); 
			          ht.put(IConstants.DISCOUNT, DISCOUNT);
			          ht.put(IConstants.PRODGST, PRODGST);
			          ht.put(IConstants.NONSTKFLAG, NONSTOCKFLAG);
			          ht.put(IConstants.NONSTKTYPEID, NONSTKTYPE);
			          ht.put(IConstants.DISCOUNT, DISCOUNT);
			          ht.put("ITEM_LOC", loc);
			          ht.put("NETWEIGHT",NETWEIGHT);
				      ht.put("GROSSWEIGHT",GROSSWEIGHT);
				      ht.put("DIMENSION",DIMENSION);
				      ht.put("HSCODE",HSCODE);
				      ht.put("COO",COO);
			          // Start code added by deen for createddate,createdby on 15/july/2013
			          ht.put("CRBY",username);
			          ht.put("CRAT",dateutils.getDateTime());
			          ht.put("VINNO",VINNO);
				      ht.put("MODEL",MODEL);
					  ht.put("RENTALPRICE",RENTALPRICE);
				      ht.put("SERVICEPRICE",SERVICEPRICE);
				      ht.put("PURCHASEUOM",PURCHASEUOM);
				      ht.put("SALESUOM",SALESUOM);
				      ht.put("RENTALUOM",RENTALUOM);
				      ht.put("SERVICEUOM",SERVICEUOM);
				      ht.put("INVENTORYUOM",INVENTORYUOM);
				      ht.put("ISBASICUOM",ISBASICUOM);
				      ht.put("ISPOSDISCOUNT",ISPOSDISCOUNT);
				      ht.put("ISNEWARRIVAL",ISNEWARRIVAL);
				      ht.put("ISTOPSELLING",ISTOPSELLING);
				      ht.put("ISCHILDCAL", ISCHILDCAL);
				      if(iscompro.equalsIgnoreCase("NONE"))
					  		iscompro="0";
					  	  else if(iscompro.equalsIgnoreCase("ISCOMPRO"))
					  		iscompro="1";
					      else
					    	 iscompro="2";
				      ht.put("ISCOMPRO",iscompro);
				      ht.put("CPPI",cppi);
				      ht.put("INCPRICE",incprice);
				      ht.put("INCPRICEUNIT",incpriceunit);
				      ht.put(IDBConstants.VENDOR_CODE,vendno);
			          ht.put("USERFLD1", "N");
			          if(STKQTY=="")
			        	  STKQTY ="0";
			          ht.put(IDBConstants.STKQTY, STKQTY);//stkqty
			          if(MAXSTKQTY=="")
			        	  MAXSTKQTY ="0";
			          ht.put(IDBConstants.MAXSTKQTY, MAXSTKQTY);
			          String remark = REMARKS+" "+ITEM_CONDITION+" "+TITLE;
			          MovHisDAO mdao = new MovHisDAO(plant);
			          mdao.setmLogger(mLogger);
			          Hashtable htm = new Hashtable();
			          htm.put("PLANT",plant);
			          htm.put("DIRTYPE",TransactionConstants.ADD_ITEM);
			          htm.put("RECID","");
			          htm.put("ITEM",ITEM);
			          htm.put("LOC",loc);
			          htm.put(IDBConstants.REMARKS,strUtils.InsertQuotes(remark));  
			          htm.put("CRBY",username);
			          htm.put("CRAT",dateutils.getDateTime());
			          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
			          boolean  inserted=false; 
			          boolean  insertAlternateItem = false;
			          catlogpath = catlogpath.replace('\\', '/');
						ht.put(IDBConstants.CATLOGPATH, catlogpath);
			          boolean itemInserted = itemUtil.insertItem(ht);
			          
			          String posquery="select PLANT,OUTLET from "+plant+"_POSOUTLETS WHERE PLANT ='"+plant+"'";
			          ArrayList posList = new ItemMstUtil().selectForReport(posquery,htData,"");
			          Map pos = new HashMap();
			          if (posList.size() > 0) {
			        	  for (int j=0; j < posList.size(); j++ ) {
			        		  pos = (Map) posList.get(j);
			        		  String outlet = (String) pos.get("OUTLET");
			        		  String posplant = (String) pos.get("PLANT");
			       	       	Hashtable htCondition = new Hashtable();
			       	       		htCondition.put(IConstants.ITEM,ITEM);
		      	        		htCondition.put(IConstants.PLANT,plant);
		      	        		htCondition.put("OUTLET",outlet);
			        	    Hashtable hPos = new Hashtable();	
			        		  hPos.put(IConstants.PLANT,plant);
			        		  hPos.put("OUTLET",outlet);
			        		  hPos.put(IConstants.ITEM,ITEM);
			        		  hPos.put("SALESUOM",SALESUOM);
			        		  hPos.put(IConstants.PRICE,PRICE);
			        		  hPos.put("CRBY",username);
			        		  hPos.put("CRAT",dateutils.getDateTime());
			        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,plant))) {
			        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
			        		  }else {
			        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
			        		  }
			        	  }
			          }
			          
			          pos = new HashMap();
			          if (posList.size() > 0) {
			        	  for (int j=0; j < posList.size(); j++ ) {
			        		  pos = (Map) posList.get(j);
			        		  String outlet = (String) pos.get("OUTLET");
			        		  String posplant = (String) pos.get("PLANT");
			       	       	Hashtable htCondition = new Hashtable();
			       	       		htCondition.put(IConstants.ITEM,ITEM);
		      	        		htCondition.put(IConstants.PLANT,plant);
		      	        		htCondition.put("OUTLET",outlet);
			        	    Hashtable hPoss = new Hashtable();	
			        		  hPoss.put(IConstants.PLANT,plant);
			        		  hPoss.put("OUTLET",outlet);
			        		  hPoss.put(IConstants.ITEM,ITEM);
			        		  hPoss.put("MINQTY",STKQTY);
			        		  hPoss.put("MAXQTY",MAXSTKQTY);
			        		  hPoss.put("CRBY",username);
			        		  hPoss.put("CRAT",dateutils.getDateTime());
			        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,plant))) {
			        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
			        		  }else {
			        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
			        		  }
			        	  }
			          }
			          
			          //IMTHI START ADD PRODUCT to Child based on plantmaster
			          
			          if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  String childplant="";
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  m = (Map) arrList.get(i);
			        			  childplant = (String) m.get("CHILD_PLANT");
			        			  ht.put(IConstants.PLANT,childplant);
			        			  if(!(itemUtil.isExistsItemMst(ITEM,childplant))) {
			      					  String catpath = catlogpath;
				        			  //catpath = catpath.replace(plant,childplant);
			      						ht.put(IDBConstants.CATLOGPATH, catpath);
			        				  boolean childitemInserted = itemUtil.insertItem(ht);
						          }
			        			  //PRD BRAND START
			        			  	Hashtable prdBrand = new Hashtable();
			        			  	Hashtable htBrandtype = new Hashtable();
			        				htBrandtype.clear();
			        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
			        				htBrandtype.put(IDBConstants.PRDBRANDID, PRD_BRAND);
			        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
			        				if(PRD_BRAND.length()>0) {
			        				if (flag == false) {
				        				ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(PRD_BRAND,plant);
				        				if (arrCust.size() > 0) {
				        				String brandId = (String) arrCust.get(0);
				        				String brandDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			        					prdBrand.clear();
			        					prdBrand.put(IDBConstants.PLANT, childplant);
			        					prdBrand.put(IDBConstants.PRDBRANDID, brandId);
			        					prdBrand.put(IDBConstants.PRDBRANDDESC, brandDesc);
			        					prdBrand.put(IConstants.ISACTIVE, isactive);
			        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        					prdBrand.put(IDBConstants.LOGIN_USER, username);
			        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
				        				}
			        				}}
			        			  //PRD BRAND END
			        				
			        			  //PRD CLASS START
			        				Hashtable htprdcls = new Hashtable();
			        			  	Hashtable htclass = new Hashtable();
			        			  	htprdcls.clear();
			        			  	htprdcls.put(IDBConstants.PLANT, childplant);
			        				htprdcls.put(IDBConstants.PRDCLSID,PRD_CLS_ID);
			      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
			      					if(PRD_CLS_ID.length()>0) {
			      					if (flag == false) {
			      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(PRD_CLS_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String classId = (String) arrCust.get(0);
				        				String classDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			      						htclass.put(IDBConstants.PLANT, childplant);
			      						htclass.put(IDBConstants.PRDCLSID, classId);
			      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
			      						htclass.put(IConstants.ISACTIVE, isactive);
			      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			      						htclass.put(IDBConstants.LOGIN_USER, username);
			      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
			      						}
			      					}
			      					}
			      				  //PRD CLASS END
			      					
			      				  //PRD TYPE START	
			      					Hashtable htprdtype = new Hashtable();
			        			  	Hashtable htprdtp = new Hashtable();
			        			  	htprdtype.clear();
			      					htprdtype.put(IDBConstants.PLANT, childplant);
			      					htprdtype.put("PRD_TYPE_ID",ARTIST);
								    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
								    if(ARTIST.length()>0) {
								    if (flag == false) {
								    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(ARTIST,plant);
				        				if (arrCust.size() > 0) {
				        				String typeId = (String) arrCust.get(0);
				        				String typedesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
				        				htprdtp.clear();
								    	htprdtp.put(IDBConstants.PLANT, childplant);
								    	htprdtp.put("PRD_TYPE_ID", typeId);
								    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedesc);
								    	htprdtp.put(IConstants.ISACTIVE, isactive);
								    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								    	htprdtp.put(IDBConstants.LOGIN_USER, username);
		               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
				        				}
		                               }
								    }
			      					//PRD TYPE END
								    
								    //PRD DEPT START
								    Hashtable htprddept = new Hashtable();
			        			  	Hashtable htdept = new Hashtable();
			        			  	htprddept.put(IDBConstants.PLANT, childplant);
			        			  	htprddept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
			        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
			        			  	if(PRD_DEPT_ID.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(PRD_DEPT_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String deptId = (String) arrCust.get(0);
				        				String deptdesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
										htdept.put(IDBConstants.PLANT, childplant);
										htdept.put(IDBConstants.PRDDEPTID, deptId);
										htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
										htdept.put(IConstants.ISACTIVE, isactive);
										htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										htdept.put(IDBConstants.LOGIN_USER, username);
										boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
				        				}
									}
			        			  	}
								   //PRD DEPT END
			        			  	
			        			  //check if Purchase UOM exists 
			        			  	Hashtable htInv = new Hashtable();
			        			  	Hashtable HtPurchaseuom = new Hashtable();
			        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
			        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
			        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
			        			  	if(PURCHASEUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END PURCHASE UOM
			        			  	
			        			  //check if Sales UOM exists 
			        			  	Hashtable HtSalesuom = new Hashtable();
			        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
			        			  	HtSalesuom.put("UOM", SALESUOM);
			        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
			        			  	if(SALESUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END SALES UOM
			        			  	
								   //check if Inventory UOM exists 
			        			  	Hashtable HtInvuom = new Hashtable();
			        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
			        			  	HtInvuom.put("UOM", INVENTORYUOM);
			        			  	flag = new UomUtil().isExistsUom(HtInvuom);
			        			  	if(INVENTORYUOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
			        			  		if (arrCust.size() > 0) {
			        			  			String uom = (String) arrCust.get(0);
			        			  			String uomdesc = (String) arrCust.get(1);
			        			  			String display = (String) arrCust.get(2);
			        			  			String qpuom = (String) arrCust.get(3);
			        			  			String isactive = (String) arrCust.get(4);	
								    	  htInv.clear();
								    	  htInv.put(IDBConstants.PLANT,childplant);
								    	  htInv.put("UOM", uom);
								    	  htInv.put("UOMDESC", uomdesc);
								    	  htInv.put("Display",display);
								    	  htInv.put("QPUOM", qpuom);
								    	  htInv.put(IConstants.ISACTIVE, isactive);
								    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								    	  htInv.put(IDBConstants.LOGIN_USER,username);
								    	  boolean uomInserted = new UomUtil().insertUom(htInv);
			        			  		}
			        			  	}
			        			  	}
			        			  	//END INV UOM
			        			  	
			        			  	//check if Stk UOM exists 
			        			  	Hashtable HtStkuom = new Hashtable();
			        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
			        			  	HtStkuom.put("UOM", UOM);
			        			  	flag = new UomUtil().isExistsUom(HtStkuom);
			        			  	if(UOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(UOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT,childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display",display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER,username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END STOCK UOM
			        			  	
			        			  //HSCODE
			        			  	if(HSCODE.length()>0) {
			        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, childplant)) 
									{						
						    			Hashtable htHS = new Hashtable();
						    			htHS.put(IDBConstants.PLANT,childplant);
						    			htHS.put(IDBConstants.HSCODE,HSCODE);
						    			htHS.put(IDBConstants.LOGIN_USER,username);
						    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddHSCODE(htHS);
										boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",HSCODE);
										htRecvHis.put(IDBConstants.CREATED_BY, username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        			  	//HSCODE END
			        			  	
			        			  	//COO 
			        			  	if(COO.length()>0) {
			        				if (!new MasterUtil().isExistCOO(COO, childplant)) 
									{						
						    			Hashtable htCoo = new Hashtable();
						    			htCoo.put(IDBConstants.PLANT,childplant);
						    			htCoo.put(IDBConstants.COO,COO);
						    			htCoo.put(IDBConstants.LOGIN_USER,username);
						    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddCOO(htCoo);
										boolean insertflag = new MasterDAO().InsertCOO(htCoo);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",COO);
										htRecvHis.put(IDBConstants.CREATED_BY,username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        				//COO END
			        				
			        				//SUPPLIER START 
			        			  	if(vendno.length()>0) {
			        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
			        				if (arrCust.size() > 0) {
			        				String sCustCode = (String) arrCust.get(0);
			        				String sCustName = (String) arrCust.get(1);
			        				if (!new CustUtil().isExistVendor(vendno, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
			        				{
			        					String sAddr1 = (String) arrCust.get(2);
			        					String sAddr2 = (String) arrCust.get(3);
			        					String sAddr3 = (String) arrCust.get(4);
			        					String sAddr4 = (String) arrCust.get(15);
			        					String sCountry = (String) arrCust.get(5);
			        					String sZip = (String) arrCust.get(6);
			        					String sCons = (String) arrCust.get(7);
			        					String sContactName = (String) arrCust.get(8);
			        					String sDesgination = (String) arrCust.get(9);
			        					String sTelNo = (String) arrCust.get(10);
			        					String sHpNo = (String) arrCust.get(11);
			        					String sEmail = (String) arrCust.get(12);
			        					String sFax = (String) arrCust.get(13);
			        					String sRemarks = (String) arrCust.get(14);
			        					String isActive = (String) arrCust.get(16);
			        					String sPayTerms = (String) arrCust.get(17);
			        					String sPayMentTerms = (String) arrCust.get(39);
			        					String sPayInDays = (String) arrCust.get(18);
			        					String sState = (String) arrCust.get(19);
			        					String sRcbno = (String) arrCust.get(20);
			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
			        					String WEBSITE = (String) arrCust.get(23);
			        					String FACEBOOK = (String) arrCust.get(24);
			        					String TWITTER = (String) arrCust.get(25);
			        					String LINKEDIN = (String) arrCust.get(26);
			        					String SKYPE = (String) arrCust.get(27);
			        					String OPENINGBALANCE = (String) arrCust.get(28);
			        					String WORKPHONE = (String) arrCust.get(29);
			        					String sTAXTREATMENT = (String) arrCust.get(30);
			        					String sCountryCode = (String) arrCust.get(31);
			        					String sBANKNAME = (String) arrCust.get(32);
			        					String sBRANCH= (String) arrCust.get(33);
			        					String sIBAN = (String) arrCust.get(34);
			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
			        					String companyregnumber = (String) arrCust.get(36);
			        					String PEPPOL = (String) arrCust.get(40);
			        					String PEPPOL_ID = (String) arrCust.get(41);
			        					String CURRENCY = (String) arrCust.get(37);
//			        					String transport = (String) arrCust.get(38);
//			        					String suppliertypeid = (String) arrCust.get(21);
			        					Hashtable htsup = new Hashtable();
			        					htsup.put(IDBConstants.PLANT,childplant);
			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
			        					htsup.put(IConstants.companyregnumber,companyregnumber);
			        					htsup.put("ISPEPPOL", PEPPOL);
			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
			        					htsup.put("CURRENCY_ID", CURRENCY);
			        					htsup.put(IConstants.NAME, sContactName);
			        					htsup.put(IConstants.DESGINATION, sDesgination);
			        					htsup.put(IConstants.TELNO, sTelNo);
			        					htsup.put(IConstants.HPNO, sHpNo);
			        					htsup.put(IConstants.FAX, sFax);
			        					htsup.put(IConstants.EMAIL, sEmail);
			        					htsup.put(IConstants.ADDRESS1, sAddr1);
			        					htsup.put(IConstants.ADDRESS2, sAddr2);
			        					htsup.put(IConstants.ADDRESS3, sAddr3);
			        					htsup.put(IConstants.ADDRESS4, sAddr4);
			        					if(sState.equalsIgnoreCase("Select State"))
			        						sState="";
			        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			        					htsup.put(IConstants.ZIP, sZip);
			        					htsup.put(IConstants.USERFLG1, sCons);
			        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//			        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//			        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//			        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
			        					htsup.put(IConstants.PAYTERMS, "");
			        					htsup.put(IConstants.payment_terms, "");
			        					htsup.put(IConstants.PAYINDAYS, "");
			        					htsup.put(IConstants.ISACTIVE, isActive);
//			        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//			        					htsup.put(IConstants.TRANSPORTID,transport);
			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
			        					htsup.put(IConstants.TRANSPORTID, "0");
			        					htsup.put(IConstants.RCBNO, sRcbno);
			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        					htsup.put(IConstants.WEBSITE,WEBSITE);
			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
			        					htsup.put(IConstants.TWITTER,TWITTER);
			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
			        					htsup.put(IConstants.SKYPE,SKYPE);
			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        			        	  sBANKNAME="";
//			        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
			        			          htsup.put(IDBConstants.BANKNAME,"");
			        			          htsup.put(IDBConstants.IBAN,sIBAN);
			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			        			          htsup.put("CRAT",new DateUtils().getDateTime());
			        			          htsup.put("CRBY",username);
			        			          htsup.put("Comment1", " 0 ");
			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			        				}
			        		  }
			        		  }
			        				//Supplier END
			      					
						          posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
						          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
						       	       		htCondition.put(IConstants.PLANT,childplant);
						       	       		htCondition.put("OUTLET",outlet);
						       	        Hashtable hPos = new Hashtable();	
						        		  hPos.put(IConstants.PLANT,childplant);
						        		  hPos.put("OUTLET",outlet);
						        		  hPos.put(IConstants.ITEM,ITEM);
						        		  hPos.put("SALESUOM",SALESUOM);
						        		  hPos.put(IConstants.PRICE,PRICE);
						        		  hPos.put("CRBY",username);
						        		  hPos.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
						        		  }
						        	  }
						          }
						          
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
					      	        		htCondition.put(IConstants.PLANT,childplant);
					      	        		htCondition.put("OUTLET",outlet);
						        	    Hashtable hPoss = new Hashtable();	
						        		  hPoss.put(IConstants.PLANT,childplant);
						        		  hPoss.put("OUTLET",outlet);
						        		  hPoss.put(IConstants.ITEM,ITEM);
						        		  hPoss.put("MINQTY",STKQTY);
						        		  hPoss.put("MAXQTY",MAXSTKQTY);
						        		  hPoss.put("CRBY",username);
						        		  hPoss.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
						        		  }
						        	  }
						          }
			        		  }
			        	  }
			        	}
			        	  
			          }else if(PARENT_PLANT == null){
			        	  boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = "";
			        	  parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        	  
			        	  
			        	  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String parentplant = "";
			        	  Map ms = new HashMap();
			        	  if (arrLists.size() > 0) {
			        		  for (int i=0; i < arrLists.size(); i++ ) {
			        			  ms = (Map) arrLists.get(i);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  ht.put(IConstants.PLANT,parentplant);
			        			  if(!(itemUtil.isExistsItemMst(ITEM,parentplant))) {
			      					  String catpath = catlogpath;
				        			  catpath = catpath.replace(plant,parentplant);
			      						ht.put(IDBConstants.CATLOGPATH, catpath);
			        				  boolean childitemInserted = itemUtil.insertItem(ht);
			        			  }
			        			  //PRD BRAND START
			        			  	Hashtable prdBrand = new Hashtable();
			        			  	Hashtable htBrandtype = new Hashtable();
			        				htBrandtype.clear();
			        			  	htBrandtype.put(IDBConstants.PLANT, parentplant);
			        				htBrandtype.put(IDBConstants.PRDBRANDID, PRD_BRAND);
			        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
			        				if(PRD_BRAND.length()>0) {
			        				if (flag == false) {
				        				ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(PRD_BRAND,plant);
				        				if (arrCust.size() > 0) {
				        				String brandId = (String) arrCust.get(0);
				        				String brandDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			        					prdBrand.clear();
			        					prdBrand.put(IDBConstants.PLANT, parentplant);
			        					prdBrand.put(IDBConstants.PRDBRANDID, brandId);
			        					prdBrand.put(IDBConstants.PRDBRANDDESC, brandDesc);
			        					prdBrand.put(IConstants.ISACTIVE, isactive);
			        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        					prdBrand.put(IDBConstants.LOGIN_USER, username);
			        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
				        				}
			        				}
			        				}
			        			   //PRD BRAND END
			        				
			        			  //PRD CLASS START
			        				Hashtable htprdcls = new Hashtable();
			        			  	Hashtable htclass = new Hashtable();
			        			  	htprdcls.clear();
			        			  	htprdcls.put(IDBConstants.PLANT, parentplant);
			        				htprdcls.put(IDBConstants.PRDCLSID,PRD_CLS_ID);
			      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
			      					if(PRD_CLS_ID.length()>0) {
			      					if (flag == false) {
			      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(PRD_CLS_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String classId = (String) arrCust.get(0);
				        				String classDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			      						htclass.put(IDBConstants.PLANT, parentplant);
			      						htclass.put(IDBConstants.PRDCLSID, classId);
			      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
			      						htclass.put(IConstants.ISACTIVE, isactive);
			      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			      						htclass.put(IDBConstants.LOGIN_USER, username);
			      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
				        				}
			      					}
			      					}
			      				  //PRD CLASS END
				      					
			      				//PRD TYPE START	
			      					Hashtable htprdtype = new Hashtable();
			        			  	Hashtable htprdtp = new Hashtable();
			        			  	htprdtype.clear();
			      					htprdtype.put(IDBConstants.PLANT, parentplant);
			      					htprdtype.put("PRD_TYPE_ID",ARTIST);
								    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
								    if(ARTIST.length()>0) {
								    if (flag == false) {
								    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(ARTIST,plant);
				        				if (arrCust.size() > 0) {
				        				String typeId = (String) arrCust.get(0);
				        				String typedesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
								    	htprdtp.clear();
								    	htprdtp.put(IDBConstants.PLANT, parentplant);
								    	htprdtp.put("PRD_TYPE_ID", typeId);
								    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedesc);
								    	htprdtp.put(IConstants.ISACTIVE, isactive);
								    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								    	htprdtp.put(IDBConstants.LOGIN_USER, username);
		               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
				        				}
		                               }
								    }
			      					//PRD TYPE END	
								    
								    //PRD DEPT START
								    Hashtable htprddept = new Hashtable();
			        			  	Hashtable htdept = new Hashtable();
			        			  	htprddept.put(IDBConstants.PLANT, parentplant);
			        			  	htprddept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
			        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
			        			  	if(PRD_DEPT_ID.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(PRD_DEPT_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String deptId = (String) arrCust.get(0);
				        				String deptdesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
										htdept.put(IDBConstants.PLANT, parentplant);
										htdept.put(IDBConstants.PRDDEPTID, deptId);
										htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
										htdept.put(IConstants.ISACTIVE, isactive);
										htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										htdept.put(IDBConstants.LOGIN_USER, username);
										boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
				        				}
									}
			        			  	}
								   //PRD DEPT END
			        			 	
			        			  //check if Purchase UOM exists 
			        			  	Hashtable htInv = new Hashtable();
			        			  	Hashtable HtPurchaseuom = new Hashtable();
			        			  	HtPurchaseuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
			        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
			        			  	if(PURCHASEUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, parentplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END PURCHASE UOM
			        			  	
			        			  //check if Sales UOM exists 
			        			  	Hashtable HtSalesuom = new Hashtable();
			        			  	HtSalesuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtSalesuom.put("UOM", SALESUOM);
			        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
			        			  	if(SALESUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, parentplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END SALES UOM
			        			  	
								   //check if Inventory UOM exists 
			        			  	Hashtable HtInvuom = new Hashtable();
			        			  	HtInvuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtInvuom.put("UOM", INVENTORYUOM);
			        			  	flag = new UomUtil().isExistsUom(HtInvuom);
			        			  	if(INVENTORYUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
							    	  htInv.clear();
							    	  htInv.put(IDBConstants.PLANT,parentplant);
							    	  htInv.put("UOM", uom);
							    	  htInv.put("UOMDESC", uomdesc);
							    	  htInv.put("Display",display);
							    	  htInv.put("QPUOM", qpuom);
							    	  htInv.put(IConstants.ISACTIVE, isactive);
							    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	  htInv.put(IDBConstants.LOGIN_USER,username);
							    	  boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END INV UOM
			        			  	
			        			  	//check if Stk UOM exists 
			        			  	Hashtable HtStkuom = new Hashtable();
			        			  	HtStkuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtStkuom.put("UOM", UOM);
			        			  	flag = new UomUtil().isExistsUom(HtStkuom);
			        			  	if(UOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(UOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT,parentplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display",display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER,username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END STOCK UOM
			        			  	
			        			  //HSCODE
			        			  	if(HSCODE.length()>0) {
			        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, parentplant)) 
									{						
						    			Hashtable htHS = new Hashtable();
						    			htHS.put(IDBConstants.PLANT,parentplant);
						    			htHS.put(IDBConstants.HSCODE,HSCODE);
						    			htHS.put(IDBConstants.LOGIN_USER,username);
						    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddHSCODE(htHS);
										boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, parentplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",HSCODE);
										htRecvHis.put(IDBConstants.CREATED_BY, username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        			  	//HSCODE END
			        			  	
			        			  	//COO 
			        			  	if(COO.length()>0) {
			        				if (!new MasterUtil().isExistCOO(COO, parentplant)) 
									{						
						    			Hashtable htCoo = new Hashtable();
						    			htCoo.put(IDBConstants.PLANT,parentplant);
						    			htCoo.put(IDBConstants.COO,COO);
						    			htCoo.put(IDBConstants.LOGIN_USER,username);
						    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddCOO(htCoo);
										boolean insertflag = new MasterDAO().InsertCOO(htCoo);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, parentplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",COO);
										htRecvHis.put(IDBConstants.CREATED_BY,username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        				//COO END

			        				//SUPPLIER START 
			        			  	if(vendno.length()>0) {
			        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
			        				if (arrCust.size() > 0) {
			        				String sCustCode = (String) arrCust.get(0);
			        				String sCustName = (String) arrCust.get(1);
			        				if (!new CustUtil().isExistVendor(vendno, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
			        				{
			        					String sAddr1 = (String) arrCust.get(2);
			        					String sAddr2 = (String) arrCust.get(3);
			        					String sAddr3 = (String) arrCust.get(4);
			        					String sAddr4 = (String) arrCust.get(15);
			        					String sCountry = (String) arrCust.get(5);
			        					String sZip = (String) arrCust.get(6);
			        					String sCons = (String) arrCust.get(7);
			        					String sContactName = (String) arrCust.get(8);
			        					String sDesgination = (String) arrCust.get(9);
			        					String sTelNo = (String) arrCust.get(10);
			        					String sHpNo = (String) arrCust.get(11);
			        					String sEmail = (String) arrCust.get(12);
			        					String sFax = (String) arrCust.get(13);
			        					String sRemarks = (String) arrCust.get(14);
			        					String isActive = (String) arrCust.get(16);
			        					String sPayTerms = (String) arrCust.get(17);
			        					String sPayMentTerms = (String) arrCust.get(39);
			        					String sPayInDays = (String) arrCust.get(18);
			        					String sState = (String) arrCust.get(19);
			        					String sRcbno = (String) arrCust.get(20);
			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
			        					String WEBSITE = (String) arrCust.get(23);
			        					String FACEBOOK = (String) arrCust.get(24);
			        					String TWITTER = (String) arrCust.get(25);
			        					String LINKEDIN = (String) arrCust.get(26);
			        					String SKYPE = (String) arrCust.get(27);
			        					String OPENINGBALANCE = (String) arrCust.get(28);
			        					String WORKPHONE = (String) arrCust.get(29);
			        					String sTAXTREATMENT = (String) arrCust.get(30);
			        					String sCountryCode = (String) arrCust.get(31);
			        					String sBANKNAME = (String) arrCust.get(32);
			        					String sBRANCH= (String) arrCust.get(33);
			        					String sIBAN = (String) arrCust.get(34);
			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
			        					String companyregnumber = (String) arrCust.get(36);
			        					String PEPPOL = (String) arrCust.get(40);
			        					String PEPPOL_ID = (String) arrCust.get(41);
			        					String CURRENCY = (String) arrCust.get(37);
//			        					String transport = (String) arrCust.get(38);
//			        					String suppliertypeid = (String) arrCust.get(21);
			        					Hashtable htsup = new Hashtable();
			        					htsup.put(IDBConstants.PLANT,parentplant);
			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
			        					htsup.put(IConstants.companyregnumber,companyregnumber);
			        					htsup.put("ISPEPPOL", PEPPOL);
			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
			        					htsup.put("CURRENCY_ID", CURRENCY);
			        					htsup.put(IConstants.NAME, sContactName);
			        					htsup.put(IConstants.DESGINATION, sDesgination);
			        					htsup.put(IConstants.TELNO, sTelNo);
			        					htsup.put(IConstants.HPNO, sHpNo);
			        					htsup.put(IConstants.FAX, sFax);
			        					htsup.put(IConstants.EMAIL, sEmail);
			        					htsup.put(IConstants.ADDRESS1, sAddr1);
			        					htsup.put(IConstants.ADDRESS2, sAddr2);
			        					htsup.put(IConstants.ADDRESS3, sAddr3);
			        					htsup.put(IConstants.ADDRESS4, sAddr4);
			        					if(sState.equalsIgnoreCase("Select State"))
			        						sState="";
			        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			        					htsup.put(IConstants.ZIP, sZip);
			        					htsup.put(IConstants.USERFLG1, sCons);
			        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//			        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//			        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//			        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
			        					htsup.put(IConstants.PAYTERMS, "");
			        					htsup.put(IConstants.payment_terms, "");
			        					htsup.put(IConstants.PAYINDAYS, "");
			        					htsup.put(IConstants.ISACTIVE, isActive);
//			        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//			        					htsup.put(IConstants.TRANSPORTID,transport);
			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
			        					htsup.put(IConstants.TRANSPORTID, "0");
			        					htsup.put(IConstants.RCBNO, sRcbno);
			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        					htsup.put(IConstants.WEBSITE,WEBSITE);
			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
			        					htsup.put(IConstants.TWITTER,TWITTER);
			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
			        					htsup.put(IConstants.SKYPE,SKYPE);
			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        			        	  sBANKNAME="";
//			        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
			        			          htsup.put(IDBConstants.BANKNAME,"");
			        			          htsup.put(IDBConstants.IBAN,sIBAN);
			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			        			          htsup.put("CRAT",new DateUtils().getDateTime());
			        			          htsup.put("CRBY",username);
			        			          htsup.put("Comment1", " 0 ");
			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			        				}
			        				}
			        			  	}
			        				//Supplier END
				      					
						          posquery="select PLANT,OUTLET from "+parentplant+"_POSOUTLETS WHERE PLANT ='"+parentplant+"'";
						          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						      	       	Hashtable htCondition = new Hashtable();
						      	        	htCondition.put(IConstants.ITEM,ITEM);
						      	        	htCondition.put(IConstants.PLANT,parentplant);
						      	        	htCondition.put("OUTLET",outlet);
						    	        Hashtable hPos = new Hashtable();	
						        		  hPos.put(IConstants.PLANT,parentplant);
						        		  hPos.put("OUTLET",outlet);
						        		  hPos.put(IConstants.ITEM,ITEM);
						        		  hPos.put("SALESUOM",SALESUOM);
						        		  hPos.put(IConstants.PRICE,PRICE);
						        		  hPos.put("CRBY",username);
						        		  hPos.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,parentplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
						        		  }
						        	  }
						          }
						          
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
					      	        		htCondition.put(IConstants.PLANT,parentplant);
					      	        		htCondition.put("OUTLET",outlet);
						        	    Hashtable hPoss = new Hashtable();	
						        		  hPoss.put(IConstants.PLANT,parentplant);
						        		  hPoss.put("OUTLET",outlet);
						        		  hPoss.put(IConstants.ITEM,ITEM);
						        		  hPoss.put("MINQTY",STKQTY);
						        		  hPoss.put("MAXQTY",MAXSTKQTY);
						        		  hPoss.put("CRBY",username);
						        		  hPoss.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,parentplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
						        		  }
						        	  }
						          }
			        		  }
			        	  }
			        	  
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  if(childplant!=plant) {
			        			  ht.put(IConstants.PLANT,childplant);
			        			  posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
			        			  if(!(itemUtil.isExistsItemMst(ITEM,childplant))) {
			        				  String catpath = catlogpath;
				        			  catpath = catpath.replace(plant,childplant);
			      						ht.put(IDBConstants.CATLOGPATH, catpath);
			        				  boolean childitemInserted = itemUtil.insertItem(ht);
			        			  }
			        			  //PRD BRAND START
			        			  	Hashtable prdBrand = new Hashtable();
			        			  	Hashtable htBrandtype = new Hashtable();
			        				htBrandtype.clear();
			        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
			        				htBrandtype.put(IDBConstants.PRDBRANDID, PRD_BRAND);
			        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
			        				if(PRD_BRAND.length()>0) {
			        				if (flag == false) {
				        				ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(PRD_BRAND,plant);
				        				if (arrCust.size() > 0) {
				        				String brandId = (String) arrCust.get(0);
				        				String brandDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			        					prdBrand.clear();
			        					prdBrand.put(IDBConstants.PLANT, childplant);
			        					prdBrand.put(IDBConstants.PRDBRANDID, brandId);
			        					prdBrand.put(IDBConstants.PRDBRANDDESC, brandDesc);
			        					prdBrand.put(IConstants.ISACTIVE, isactive);
			        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        					prdBrand.put(IDBConstants.LOGIN_USER, username);
			        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
				        				}
			        				}
			        				}
			        			  //PRD BRAND END
			        				
			        			  //PRD CLASS START
			        				Hashtable htprdcls = new Hashtable();
			        			  	Hashtable htclass = new Hashtable();
			        			  	htprdcls.clear();
			        			  	htprdcls.put(IDBConstants.PLANT, childplant);
			        				htprdcls.put(IDBConstants.PRDCLSID,PRD_CLS_ID);
			      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
			      					if(PRD_CLS_ID.length()>0) {
			      					if (flag == false) {
			      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(PRD_CLS_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String classId = (String) arrCust.get(0);
				        				String classDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			      						htclass.put(IDBConstants.PLANT, childplant);
			      						htclass.put(IDBConstants.PRDCLSID, classId);
			      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
			      						htclass.put(IConstants.ISACTIVE, isactive);
			      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			      						htclass.put(IDBConstants.LOGIN_USER, username);
			      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
				        				}
			      					}
			      					}
			      				  //PRD CLASS END
			      					
			      				  //PRD TYPE START	
			      					Hashtable htprdtype = new Hashtable();
			        			  	Hashtable htprdtp = new Hashtable();
			        			  	htprdtype.clear();
			      					htprdtype.put(IDBConstants.PLANT, childplant);
			      					htprdtype.put("PRD_TYPE_ID",ARTIST);
								    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
								    if(ARTIST.length()>0) {
								    if (flag == false) {
								    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(ARTIST,plant);
				        				if (arrCust.size() > 0) {
				        				String typeId = (String) arrCust.get(0);
				        				String typedesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
								    	htprdtp.clear();
								    	htprdtp.put(IDBConstants.PLANT, childplant);
								    	htprdtp.put("PRD_TYPE_ID", typeId);
								    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedesc);
								    	htprdtp.put(IConstants.ISACTIVE, isactive);
								    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								    	htprdtp.put(IDBConstants.LOGIN_USER, username);
		               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
				        				}
		                               }
								    }
			      					//PRD TYPE END
								    
								    //PRD DEPT START
								    Hashtable htprddept = new Hashtable();
			        			  	Hashtable htdept = new Hashtable();
			        			  	htprddept.put(IDBConstants.PLANT, childplant);
			        			  	htprddept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
			        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
			        			  	if(PRD_DEPT_ID.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(PRD_DEPT_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String deptId = (String) arrCust.get(0);
				        				String deptdesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
										htdept.put(IDBConstants.PLANT, childplant);
										htdept.put(IDBConstants.PRDDEPTID, deptId);
										htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
										htdept.put(IConstants.ISACTIVE, isactive);
										htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										htdept.put(IDBConstants.LOGIN_USER, username);
										boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
				        				}
									}
			        			  	}
								   //PRD DEPT END
			        			 	
			        			  //check if Purchase UOM exists 
			        			  	Hashtable htInv = new Hashtable();
			        			  	Hashtable HtPurchaseuom = new Hashtable();
			        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
			        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
			        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
			        			  	if(PURCHASEUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END PURCHASE UOM
			        			  	
			        			  //check if Sales UOM exists 
			        			  	Hashtable HtSalesuom = new Hashtable();
			        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
			        			  	HtSalesuom.put("UOM", SALESUOM);
			        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
			        			  	if(SALESUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END SALES UOM
			        			  	
								   //check if Inventory UOM exists 
			        			  	Hashtable HtInvuom = new Hashtable();
			        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
			        			  	HtInvuom.put("UOM", INVENTORYUOM);
			        			  	flag = new UomUtil().isExistsUom(HtInvuom);
			        			  	if(INVENTORYUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
							    	  htInv.clear();
							    	  htInv.put(IDBConstants.PLANT,childplant);
							    	  htInv.put("UOM", uom);
							    	  htInv.put("UOMDESC", uomdesc);
							    	  htInv.put("Display",display);
							    	  htInv.put("QPUOM", qpuom);
							    	  htInv.put(IConstants.ISACTIVE, isactive);
							    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	  htInv.put(IDBConstants.LOGIN_USER,username);
							    	  boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END INV UOM
			        			  	
			        			  	//check if Stk UOM exists 
			        			  	Hashtable HtStkuom = new Hashtable();
			        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
			        			  	HtStkuom.put("UOM", UOM);
			        			  	flag = new UomUtil().isExistsUom(HtStkuom);
			        			  	if(UOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(UOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT,childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display",display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER,username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END STOCK UOM
			        			  	
			        			  //HSCODE
			        			  	if(HSCODE.length()>0) {
			        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, childplant)) 
									{						
						    			Hashtable htHS = new Hashtable();
						    			htHS.put(IDBConstants.PLANT,childplant);
						    			htHS.put(IDBConstants.HSCODE,HSCODE);
						    			htHS.put(IDBConstants.LOGIN_USER,username);
						    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddHSCODE(htHS);
										boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",HSCODE);
										htRecvHis.put(IDBConstants.CREATED_BY, username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        			  	//HSCODE END
			        			  	
			        			  	//COO 
			        			  	if(COO.length()>0) {
			        				if (!new MasterUtil().isExistCOO(COO, childplant)) 
									{						
						    			Hashtable htCoo = new Hashtable();
						    			htCoo.put(IDBConstants.PLANT,childplant);
						    			htCoo.put(IDBConstants.COO,COO);
						    			htCoo.put(IDBConstants.LOGIN_USER,username);
						    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddCOO(htCoo);
										boolean insertflag = new MasterDAO().InsertCOO(htCoo);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",COO);
										htRecvHis.put(IDBConstants.CREATED_BY,username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        				//COO END

			        				//SUPPLIER START 
			        			  	if(vendno.length()>0) {
			        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
			        				if (arrCust.size() > 0) {
			        				String sCustCode = (String) arrCust.get(0);
			        				String sCustName = (String) arrCust.get(1);
			        				if (!new CustUtil().isExistVendor(vendno, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
			        				{
			        					String sAddr1 = (String) arrCust.get(2);
			        					String sAddr2 = (String) arrCust.get(3);
			        					String sAddr3 = (String) arrCust.get(4);
			        					String sAddr4 = (String) arrCust.get(15);
			        					String sCountry = (String) arrCust.get(5);
			        					String sZip = (String) arrCust.get(6);
			        					String sCons = (String) arrCust.get(7);
			        					String sContactName = (String) arrCust.get(8);
			        					String sDesgination = (String) arrCust.get(9);
			        					String sTelNo = (String) arrCust.get(10);
			        					String sHpNo = (String) arrCust.get(11);
			        					String sEmail = (String) arrCust.get(12);
			        					String sFax = (String) arrCust.get(13);
			        					String sRemarks = (String) arrCust.get(14);
			        					String isActive = (String) arrCust.get(16);
			        					String sPayTerms = (String) arrCust.get(17);
			        					String sPayMentTerms = (String) arrCust.get(39);
			        					String sPayInDays = (String) arrCust.get(18);
			        					String sState = (String) arrCust.get(19);
			        					String sRcbno = (String) arrCust.get(20);
			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
			        					String WEBSITE = (String) arrCust.get(23);
			        					String FACEBOOK = (String) arrCust.get(24);
			        					String TWITTER = (String) arrCust.get(25);
			        					String LINKEDIN = (String) arrCust.get(26);
			        					String SKYPE = (String) arrCust.get(27);
			        					String OPENINGBALANCE = (String) arrCust.get(28);
			        					String WORKPHONE = (String) arrCust.get(29);
			        					String sTAXTREATMENT = (String) arrCust.get(30);
			        					String sCountryCode = (String) arrCust.get(31);
			        					String sBANKNAME = (String) arrCust.get(32);
			        					String sBRANCH= (String) arrCust.get(33);
			        					String sIBAN = (String) arrCust.get(34);
			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
			        					String companyregnumber = (String) arrCust.get(36);
			        					String PEPPOL = (String) arrCust.get(40);
			        					String PEPPOL_ID = (String) arrCust.get(41);
			        					String CURRENCY = (String) arrCust.get(37);
//			        					String transport = (String) arrCust.get(38);
//			        					String suppliertypeid = (String) arrCust.get(21);
			        					Hashtable htsup = new Hashtable();
			        					htsup.put(IDBConstants.PLANT,childplant);
			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
			        					htsup.put(IConstants.companyregnumber,companyregnumber);
			        					htsup.put("ISPEPPOL", PEPPOL);
			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
			        					htsup.put("CURRENCY_ID", CURRENCY);
			        					htsup.put(IConstants.NAME, sContactName);
			        					htsup.put(IConstants.DESGINATION, sDesgination);
			        					htsup.put(IConstants.TELNO, sTelNo);
			        					htsup.put(IConstants.HPNO, sHpNo);
			        					htsup.put(IConstants.FAX, sFax);
			        					htsup.put(IConstants.EMAIL, sEmail);
			        					htsup.put(IConstants.ADDRESS1, sAddr1);
			        					htsup.put(IConstants.ADDRESS2, sAddr2);
			        					htsup.put(IConstants.ADDRESS3, sAddr3);
			        					htsup.put(IConstants.ADDRESS4, sAddr4);
			        					if(sState.equalsIgnoreCase("Select State"))
			        						sState="";
			        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			        					htsup.put(IConstants.ZIP, sZip);
			        					htsup.put(IConstants.USERFLG1, sCons);
			        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//			        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//			        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//			        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
			        					htsup.put(IConstants.PAYTERMS, "");
			        					htsup.put(IConstants.payment_terms, "");
			        					htsup.put(IConstants.PAYINDAYS, "");
			        					htsup.put(IConstants.ISACTIVE, isActive);
//			        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//			        					htsup.put(IConstants.TRANSPORTID,transport);
			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
			        					htsup.put(IConstants.TRANSPORTID, "0");
			        					htsup.put(IConstants.RCBNO, sRcbno);
			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        					htsup.put(IConstants.WEBSITE,WEBSITE);
			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
			        					htsup.put(IConstants.TWITTER,TWITTER);
			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
			        					htsup.put(IConstants.SKYPE,SKYPE);
			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        			        	  sBANKNAME="";
//			        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
			        			          htsup.put(IDBConstants.BANKNAME,"");
			        			          htsup.put(IDBConstants.IBAN,sIBAN);
			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			        			          htsup.put("CRAT",new DateUtils().getDateTime());
			        			          htsup.put("CRBY",username);
			        			          htsup.put("Comment1", " 0 ");
			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			        				}
			        				}
			        			  	}
			        				//Supplier END
									    
						          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						        	     Hashtable htCondition = new Hashtable();
						      	        	htCondition.put(IConstants.ITEM,ITEM);
						      	        	htCondition.put(IConstants.PLANT,childplant);
						      	        	htCondition.put("OUTLET",outlet);
						        		  Hashtable hPos = new Hashtable();	
						        		  hPos.put(IConstants.PLANT,childplant);
						        		  hPos.put("OUTLET",outlet);
						        		  hPos.put(IConstants.ITEM,ITEM);
						        		  hPos.put("SALESUOM",SALESUOM);
						        		  hPos.put(IConstants.PRICE,PRICE);
						        		  hPos.put("CRBY",username);
						        		  hPos.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
						        		  }
						        	  }
						          }
						          
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
					      	        		htCondition.put(IConstants.PLANT,childplant);
					      	        		htCondition.put("OUTLET",outlet);
						        	    Hashtable hPoss = new Hashtable();	
						        		  hPoss.put(IConstants.PLANT,childplant);
						        		  hPoss.put("OUTLET",outlet);
						        		  hPoss.put(IConstants.ITEM,ITEM);
						        		  hPoss.put("MINQTY",STKQTY);
						        		  hPoss.put("MAXQTY",MAXSTKQTY);
						        		  hPoss.put("CRBY",username);
						        		  hPoss.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
						        		  }
						        	  }
						          }
			        			  }
			        		  }
			        	  }
			        	  
			        	  }
			          	}
			          }
			          
					//added by imthi 29-06-2022 ->DESC: get item and insert with CUSTOMERID from custmst tbl
			          CustUtil custUtils = new CustUtil(); 
			          CustMstDAO custMstDAO = new CustMstDAO(); 
			          ArrayList movQryLists =custUtils.getCustomerListWithType("",plant,"");
			          if (movQryLists.size() > 0) {
			  			for(int i =0; i<movQryLists.size(); i++) {
			  				Map arrCustLine = (Map)movQryLists.get(i);
			  				String customerNo=(String)arrCustLine.get("CUSTNO");
			  					Hashtable apporderht = new Hashtable();
			  					apporderht.put(IConstants.PLANT,plant);
			  					apporderht.put("CUSTNO",customerNo);
			  					apporderht.put(IConstants.ITEM,ITEM);
			  					apporderht.put(IConstants.ITEM_DESC,DESC);
			  					apporderht.put("ORDER_QTY","0");
			  					apporderht.put("MAX_ORDER_QTY","0");
			  					apporderht.put("CRBY",username);
			  					apporderht.put("CRAT",dateutils.getDateTime());
			  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
			  			}
			          }
			          
			          //IMTHI START to insert for table APPORDERQTYCONFIG in child parent
			          if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
							          movQryLists =custUtils.getCustomerListWithType("",childplant,"");
							          if (movQryLists.size() > 0) {
							  			for(int t =0; t<movQryLists.size(); t++) {
							  				Map arrCustLine = (Map)movQryLists.get(t);
							  				String customerNo=(String)arrCustLine.get("CUSTNO");
							  					Hashtable apporderht = new Hashtable();
							  					apporderht.put(IConstants.PLANT,childplant);
							  					apporderht.put("CUSTNO",customerNo);
							  					apporderht.put(IConstants.ITEM,ITEM);
							  					apporderht.put(IConstants.ITEM_DESC,DESC);
							  					apporderht.put("ORDER_QTY","0");
							  					apporderht.put("MAX_ORDER_QTY","0");
							  					apporderht.put("CRBY",username);
							  					apporderht.put("CRAT",dateutils.getDateTime());
							  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
							  			}
							          }
				        		  	}
				        	  	}
			        	  	}
			          }else if(PARENT_PLANT == null){
			        	  boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int i=0; i < arrLists.size(); i++ ) {
					        			  Map ms = (Map) arrLists.get(i);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
								          movQryLists =custUtils.getCustomerListWithType("",parentplant,"");
								          if (movQryLists.size() > 0) {
								  			for(int t =0; t<movQryLists.size(); t++) {
								  				Map arrCustLine = (Map)movQryLists.get(t);
								  				String customerNo=(String)arrCustLine.get("CUSTNO");
								  					Hashtable apporderht = new Hashtable();
								  					apporderht.put(IConstants.PLANT,parentplant);
								  					apporderht.put("CUSTNO",customerNo);
								  					apporderht.put(IConstants.ITEM,ITEM);
								  					apporderht.put(IConstants.ITEM_DESC,DESC);
								  					apporderht.put("ORDER_QTY","0");
								  					apporderht.put("MAX_ORDER_QTY","0");
								  					apporderht.put("CRBY",username);
								  					apporderht.put("CRAT",dateutils.getDateTime());
								  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
								  			}
								          }
					        		  }
					        	  }
					        	  
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
									          movQryLists =custUtils.getCustomerListWithType("",childplant,"");
									          if (movQryLists.size() > 0) {
									  			for(int t =0; t<movQryLists.size(); t++) {
									  				Map arrCustLine = (Map)movQryLists.get(t);
									  				String customerNo=(String)arrCustLine.get("CUSTNO");
									  					Hashtable apporderht = new Hashtable();
									  					apporderht.put(IConstants.PLANT,childplant);
									  					apporderht.put("CUSTNO",customerNo);
									  					apporderht.put(IConstants.ITEM,ITEM);
									  					apporderht.put(IConstants.ITEM_DESC,DESC);
									  					apporderht.put("ORDER_QTY","0");
									  					apporderht.put("MAX_ORDER_QTY","0");
									  					apporderht.put("CRBY",username);
									  					apporderht.put("CRAT",dateutils.getDateTime());
									  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
									  			}
									          }
					        			  }
					        		  	}
					        	  	}
				        	  }	
		             		}
		             	  }//IMTHI END
			          
			          
			          {
			          if (itemInserted) {
			        	  String alternateItemName = ITEM; 
				           	 List<String> alternateItemNameLists = new ArrayList<String>();
				        	 alternateItemNameLists.add(alternateItemName);
				        	 insertAlternateItem = itemUtil.insertAlternateItemLists(plant, ITEM, alternateItemNameLists);
				        	 
				             if(PARENT_PLANT != null){
					        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
					        	  if(CHECKplantCompany == null)
					  				CHECKplantCompany = "0";
					        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  if (arrList.size() > 0) {
						        		  for (int i=0; i < arrList.size(); i++ ) {
						        			  Map m = (Map) arrList.get(i);
						        			  String childplant = (String) m.get("CHILD_PLANT");
						        			  insertAlternateItem = itemUtil.insertAlternateItemLists(childplant, ITEM, alternateItemNameLists);
						        		  	}
						        	  	}
					        	  	}
				             }else if(PARENT_PLANT == null){
				            	 boolean ischild = false;
						        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
						        	  if (arrLi.size() > 0) {
						        	  Map mst = (Map) arrLi.get(0);
						        	  String parent = (String) mst.get("PARENT_PLANT");
						         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
						        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
						        	  if(Ischildasparent){
						        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        			  ischild = true;
						        		  }
						        	  }else{
						        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        		  ischild = true;
						        	  }
						        	  }
						        	  if(ischild){
						        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
							        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
							        	  String  parentplant ="";
							        	  if (arrLists.size() > 0) {
							        		  for (int i=0; i < arrLists.size(); i++ ) {
							        			  Map ms = (Map) arrLists.get(i);
							        			  parentplant = (String) ms.get("PARENT_PLANT");
							        			  insertAlternateItem = itemUtil.insertAlternateItemLists(parentplant, ITEM, alternateItemNameLists);
							        		  }
							        	  }
							        	  
							        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
							        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
							        	  Map m = new HashMap();
							        	  if (arrList.size() > 0) {
							        		  for (int i=0; i < arrList.size(); i++ ) {
							        			  m = (Map) arrList.get(i);
							        			  String childplant = (String) m.get("CHILD_PLANT");
							        			  if(childplant!=plant) {
							        				  insertAlternateItem = itemUtil.insertAlternateItemLists(childplant, ITEM, alternateItemNameLists);
							        			  }
							        		  	}
							        	  	}
						        	  }	
				             	  }
				             	}
							}
			          if(itemInserted && insertAlternateItem) {
			        	  
			        	  //imti added ADDITIONAL detail desc AND additional product on 23-01-2023
			        	  //additional detail desc:
			        	  boolean DESCS=false;
			        	  for(int i =0 ; i < DETAILDESC.size() ; i++){
			        		  String DETAILDESC_VAL = (String) DETAILDESC.get(i);
			        		  if(DETAILDESC_VAL==""){
			        			  break;
			        		  }else{
			        			  Hashtable HM = new Hashtable();
			        			  HM.put(IConstants.PLANT, plant);
			        			  HM.put(IConstants.ITEM, ITEM);
			        			  HM.put("ITEMDETAILDESC", DETAILDESC_VAL);
			        			  HM.put("CRAT",dateutils.getDateTime());
			        			  HM.put("CRBY",username);
			        			  DESCS = itemUtil.insertDetailDesc(HM);
			        			  
			        			  //IMTHI ADD for CHILD PARENT to insert Additional Detail Description
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  DESCS = itemUtil.insertDetailDesc(HM);
								        		  	}
								        	  	}
							        	  	}
				    			    }else if(PARENT_PLANT == null){
				    			    	boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  DESCS = itemUtil.insertDetailDesc(HM);
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
									        				  DESCS = itemUtil.insertDetailDesc(HM);
									        			  }
									        		  	}
									        	  	}
								        	  	}
								        	  }
						             	  }//IMTHI END
			        		  }
			        	  }
			        	  
			        	  boolean ADDPRD=false;
			        	  //additional Product:
			        	  for(int i =0 ; i < ADITIONALPRD.size() ; i++){
			        		  String ADITIONALPRD_VAL = (String) ADITIONALPRD.get(i);
			        		  if(ADITIONALPRD_VAL==""){
			        			  break;
			        		  }else{
			        			  Hashtable HM = new Hashtable();
			        			  HM.put(IConstants.PLANT, plant);
			        			  HM.put(IConstants.ITEM, ITEM);
			        			  HM.put("ADDITIONALITEM", ADITIONALPRD_VAL);
			        			  HM.put("CRAT",dateutils.getDateTime());
			        			  HM.put("CRBY",username);
			        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
			        			  
			        			  //IMTHI ADD for CHILD PARENT to insert Additional Product
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
								        		  	}
								        	  	}
							        	  	}
				    			    }else if(PARENT_PLANT == null){
				    			    	boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
										        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
									        			  }
									        		  	}
									        	  	}
								        	  	}
								        	  }
						             	  }//IMTHI END
			        		  }
			        	  }
			        	  //imti end
			        	  
			        	  boolean OBCustomerDiscount=false;
			        	//insert customer discount outbound  
			        	  for(int i =0 ; i < DYNAMIC_CUSTOMER_DISCOUNT.size() ; i++){
			        		  String DYNAMIC_CUSTOMER_DISCOUNT_VAL = (String) DYNAMIC_CUSTOMER_DISCOUNT.get(i);
			        		  String CUSTOMER_TYPE_ID_VAL = (String) CUSTOMER_TYPE_ID.get(i);
			        		  if(DYNAMIC_CUSTOMER_DISCOUNT_VAL==""){
				        			break;
				        		}else{
				        			String CUSTOMER_TYPE_DESC;
			        		  Hashtable HM = new Hashtable();
			        		   	HM.put(IConstants.PLANT, plant);
			    				HM.put(IConstants.ITEM, ITEM);
			    				HM.put(IConstants.CUSTOMERTYPEID, CUSTOMER_TYPE_ID_VAL);
			    				CustMstDAO CustMstDAO = new CustMstDAO();
			    				CUSTOMER_TYPE_DESC= CustMstDAO.getCustomerNameByNo(HM);
			    					HM.put(IConstants.CUSTOMERTYPEDESC, CUSTOMER_TYPE_DESC);	
			    				if(OBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
			    					HM.put("OBDISCOUNT", DYNAMIC_CUSTOMER_DISCOUNT_VAL+"%");
			    				}
			    				else{
			    					HM.put("OBDISCOUNT", DYNAMIC_CUSTOMER_DISCOUNT_VAL);
			    				}
			    				HM.put("CRAT",dateutils.getDateTime());
			    			    HM.put("CRBY",username);
			    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
			    			    
			    			    //IMTHI ADD for CHILD PARENT to insert customer discount
			    			    if(PARENT_PLANT != null){
						        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
						        	  if(CHECKplantCompany == null)
						  				CHECKplantCompany = "0";
						        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
							        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
							        	  if (arrList.size() > 0) {
							        		  for (int m=0; m < arrList.size(); m++ ) {
							        			  Map map = (Map) arrList.get(m);
							        			  String childplant = (String) map.get("CHILD_PLANT");
							        			  HM.put(IConstants.PLANT, childplant);
							        			  OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
							        			  
							        			  //IMTHI ADD child company to insert in customer type
							        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(CUSTOMER_TYPE_ID_VAL,plant);
							        			  if (arrCust.size() > 0) {
							        			  	String sCustTypeId = (String) arrCust.get(1);
							        			  	String sCustTypeDesc = (String) arrCust.get(2);
							        			  	String sIsactive = (String) arrCust.get(7);
								        			Hashtable htcust = new Hashtable();
								        			htcust.put(IDBConstants.PLANT,childplant);
								        			htcust.put(IDBConstants.CUSTOMERTYPEID,CUSTOMER_TYPE_ID_VAL);
								        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
								        		    	Hashtable htcustAdd = new Hashtable();
									        		    	htcustAdd.put(IDBConstants.PLANT,childplant);
									        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
									        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
									        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
									        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
									        				htcustAdd.put(IDBConstants.CREATED_BY, username);
									        		    Hashtable htms = new Hashtable();
									        		       htms.put("PLANT",childplant);
									        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
									        	           htms.put("RECID","");
									        	           htms.put("ITEM",sCustTypeId);
									        	           htms.put("REMARKS",sCustTypeDesc);
									        	           htms.put("UPBY",username);   
									        	           htms.put("CRBY",username);
									        	           htms.put("CRAT",dateutils.getDateTime());
									        	           htms.put("UPAT",dateutils.getDateTime());
									        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
									        	           htms.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
									        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
								        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
								        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//								        				   boolean Updated = custutil.updateCustomerTypeId(htUpdate,htcust);
//								        				   boolean inserted = mdao.insertIntoMovHis(htm);
								        		    }
								        		    }//IMTHI END CUSTOMER TYPE
							        		  	}
							        	  	}
						        	  	}
			    			    }else if(PARENT_PLANT == null){
			    			    	boolean ischild = false;
							        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
							        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
							        	  if (arrLi.size() > 0) {
							        	  Map mst = (Map) arrLi.get(0);
							        	  String parent = (String) mst.get("PARENT_PLANT");
							         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
							        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
							        	  if(Ischildasparent){
							        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        			  ischild = true;
							        		  }
							        	  }else{
							        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  ischild = true;
							        	  }
							        	  }
							        	  if(ischild){
							        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
								        	  String  parentplant ="";
								        	  if (arrLists.size() > 0) {
								        		  for (int j=0; j < arrLists.size(); j++ ) {
								        			  Map ms = (Map) arrLists.get(j);
								        			  parentplant = (String) ms.get("PARENT_PLANT");
								        			  HM.put(IConstants.PLANT, parentplant);
								        			  OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
								        			  //IMTHI ADD child company to insert in customer type
								        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(CUSTOMER_TYPE_ID_VAL,plant);
								        			  	String sCustTypeId = (String) arrCust.get(1);
								        			  	String sCustTypeDesc = (String) arrCust.get(2);
								        			  	String sIsactive = (String) arrCust.get(7);
									        			Hashtable htcust = new Hashtable();
									        			htcust.put(IDBConstants.PLANT,parentplant);
									        			htcust.put(IDBConstants.CUSTOMERTYPEID,CUSTOMER_TYPE_ID_VAL);
									        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
									        		    	Hashtable htcustAdd = new Hashtable();
										        		    	htcustAdd.put(IDBConstants.PLANT,parentplant);
										        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
										        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
										        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
										        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										        				htcustAdd.put(IDBConstants.CREATED_BY, username);
										        		    Hashtable htms = new Hashtable();
										        		       htms.put("PLANT",parentplant);
										        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
										        	           htms.put("RECID","");
										        	           htms.put("ITEM",sCustTypeId);
										        	           htms.put("REMARKS",sCustTypeDesc);
										        	           htms.put("UPBY",username);   
										        	           htms.put("CRBY",username);
										        	           htms.put("CRAT",dateutils.getDateTime());
										        	           htms.put("UPAT",dateutils.getDateTime());
										        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
										        	           htms.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
										        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
									        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
									        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//									        				   boolean Updated = custutil.updateCustomerTypeId(htUpdate,htcust);
//									        				   boolean inserted = mdao.insertIntoMovHis(htm);
									        		    }//IMTHI END CUSTOMER TYPE
								        		  }
								        	  }
								        	  
								        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  Map m = new HashMap();
								        	  if (arrList.size() > 0) {
								        		  for (int k=0; k < arrList.size(); k++ ) {
								        			  m = (Map) arrList.get(k);
								        			  String childplant = (String) m.get("CHILD_PLANT");
								        			  if(childplant!=plant) {
								        				  HM.put(IConstants.PLANT, childplant);
								        				  OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
								        				  //IMTHI ADD child company to insert in customer type
									        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(CUSTOMER_TYPE_ID_VAL,plant);
									        			  if (arrCust.size() > 0) {
									        			  	String sCustTypeId = (String) arrCust.get(1);
									        			  	String sCustTypeDesc = (String) arrCust.get(2);
									        			  	String sIsactive = (String) arrCust.get(7);
										        			Hashtable htcust = new Hashtable();
										        			htcust.put(IDBConstants.PLANT,childplant);
										        			htcust.put(IDBConstants.CUSTOMERTYPEID,CUSTOMER_TYPE_ID_VAL);
										        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
										        		    	Hashtable htcustAdd = new Hashtable();
											        		    	htcustAdd.put(IDBConstants.PLANT,childplant);
											        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
											        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
											        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
											        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
											        				htcustAdd.put(IDBConstants.CREATED_BY, username);
											        		    Hashtable htms = new Hashtable();
											        		       htms.put("PLANT",childplant);
											        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
											        	           htms.put("RECID","");
											        	           htms.put("ITEM",sCustTypeId);
											        	           htms.put("REMARKS",sCustTypeDesc);
											        	           htms.put("UPBY",username);   
											        	           htms.put("CRBY",username);
											        	           htms.put("CRAT",dateutils.getDateTime());
											        	           htms.put("UPAT",dateutils.getDateTime());
											        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
											        	           htms.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
											        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
										        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
										        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//										        				   boolean Updated = custutil.updateCustomerTypeId(htUpdate,htcust);
//										        				   boolean inserted = mdao.insertIntoMovHis(htm);
										        		    }
										        		    }//IMTHI END CUSTOMER TYPE
								        			  }
								        		  	}
								        	  	}
							        	  	}
							        	  }
					             	  }//IMTHI END
			    			    
			        	  }
			        	  }
			        	  
			        	  /*int DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_CUSTOMERDISCOUNT_SIZE)).intValue();
			        	  for(int nameCount = 0; nameCount<=DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT;nameCount++){
				        		 System.out.println("nameCount"+nameCount);
				        		 System.out.println("nameCount"+strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID_"+nameCount)));
				        		if(StrUtils.fString(request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount))==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, ITEM);
				    				HM.put(IConstants.CUSTOMERTYPEID, strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID_"+nameCount)));
				    				if(OBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
				    					HM.put("OBDISCOUNT", request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount)+"%");
				    				}
				    				else{
				    					HM.put("OBDISCOUNT", request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount));
				    				}
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
				    								        		
				        		}
				        	 }*/
 			           //end insert customer discount sales
			        	  
			        //insert item supplier Purchase tab
			        	  boolean additemSupplier=false;
			        	  for(int i =0 ; i < ITEMSUPPLIER.size() ; i++){
			        		  String ITEMSUPPLIER_VAL = (String) ITEMSUPPLIER.get(i);
			        		  if(ITEMSUPPLIER_VAL==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, ITEM);
//				    				ArrayList movQryList = custUtils.getVendorListsWithName(ITEMSUPPLIER_VAL, plant,"");
//				    				Map arrCustLine = (Map)movQryList.get(0);
//				    				String VENDNO=(String)arrCustLine.get("VENDNO");
//				    				HM.put(IConstants.VENDNO, VENDNO);
				    				HM.put(IConstants.VENDNO, ITEMSUPPLIER_VAL);
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    additemSupplier = itemUtil.insertItemSupplier(HM);
				    			    
				    			    //IMTHI ADD for CHILD PARENT to insert supplier discount
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  additemSupplier = itemUtil.insertItemSupplier(HM);
								        			  
								        			//SUPPLIER START 
								        			  if(ITEMSUPPLIER_VAL.length()>0) {
								        				ArrayList arrCust = new CustUtil().getVendorDetails(ITEMSUPPLIER_VAL,plant);
								        				if (arrCust.size() > 0) {
								        				String sCustCode = (String) arrCust.get(0);
								        				String sCustName = (String) arrCust.get(1);
								        				if (!new CustUtil().isExistVendor(ITEMSUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
								        				{
								        					String sAddr1 = (String) arrCust.get(2);
								        					String sAddr2 = (String) arrCust.get(3);
								        					String sAddr3 = (String) arrCust.get(4);
								        					String sAddr4 = (String) arrCust.get(15);
								        					String sCountry = (String) arrCust.get(5);
								        					String sZip = (String) arrCust.get(6);
								        					String sCons = (String) arrCust.get(7);
								        					String sContactName = (String) arrCust.get(8);
								        					String sDesgination = (String) arrCust.get(9);
								        					String sTelNo = (String) arrCust.get(10);
								        					String sHpNo = (String) arrCust.get(11);
								        					String sEmail = (String) arrCust.get(12);
								        					String sFax = (String) arrCust.get(13);
								        					String sRemarks = (String) arrCust.get(14);
								        					String isActive = (String) arrCust.get(16);
								        					String sPayTerms = (String) arrCust.get(17);
								        					String sPayMentTerms = (String) arrCust.get(39);
								        					String sPayInDays = (String) arrCust.get(18);
								        					String sState = (String) arrCust.get(19);
								        					String sRcbno = (String) arrCust.get(20);
								        					String CUSTOMEREMAIL = (String) arrCust.get(22);
								        					String WEBSITE = (String) arrCust.get(23);
								        					String FACEBOOK = (String) arrCust.get(24);
								        					String TWITTER = (String) arrCust.get(25);
								        					String LINKEDIN = (String) arrCust.get(26);
								        					String SKYPE = (String) arrCust.get(27);
								        					String OPENINGBALANCE = (String) arrCust.get(28);
								        					String WORKPHONE = (String) arrCust.get(29);
								        					String sTAXTREATMENT = (String) arrCust.get(30);
								        					String sCountryCode = (String) arrCust.get(31);
								        					String sBANKNAME = (String) arrCust.get(32);
								        					String sBRANCH= (String) arrCust.get(33);
								        					String sIBAN = (String) arrCust.get(34);
								        					String sBANKROUTINGCODE = (String) arrCust.get(35);
								        					String companyregnumber = (String) arrCust.get(36);
								        					String PEPPOL = (String) arrCust.get(40);
								        					String PEPPOL_ID = (String) arrCust.get(41);
								        					String CURRENCY = (String) arrCust.get(37);
								        					Hashtable htsup = new Hashtable();
								        					htsup.put(IDBConstants.PLANT,childplant);
								        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
								        					htsup.put(IConstants.VENDOR_NAME, sCustName);
								        					htsup.put(IConstants.companyregnumber,companyregnumber);
								        					htsup.put("ISPEPPOL", PEPPOL);
								        					htsup.put("PEPPOL_ID", PEPPOL_ID);
								        					htsup.put("CURRENCY_ID", CURRENCY);
								        					htsup.put(IConstants.NAME, sContactName);
								        					htsup.put(IConstants.DESGINATION, sDesgination);
								        					htsup.put(IConstants.TELNO, sTelNo);
								        					htsup.put(IConstants.HPNO, sHpNo);
								        					htsup.put(IConstants.FAX, sFax);
								        					htsup.put(IConstants.EMAIL, sEmail);
								        					htsup.put(IConstants.ADDRESS1, sAddr1);
								        					htsup.put(IConstants.ADDRESS2, sAddr2);
								        					htsup.put(IConstants.ADDRESS3, sAddr3);
								        					htsup.put(IConstants.ADDRESS4, sAddr4);
								        					if(sState.equalsIgnoreCase("Select State"))
								        						sState="";
								        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
								        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
								        					htsup.put(IConstants.ZIP, sZip);
								        					htsup.put(IConstants.USERFLG1, sCons);
								        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
								        					htsup.put(IConstants.PAYTERMS, "");
								        					htsup.put(IConstants.payment_terms, "");
								        					htsup.put(IConstants.PAYINDAYS, "");
								        					htsup.put(IConstants.ISACTIVE, isActive);
								        					htsup.put(IConstants.SUPPLIERTYPEID,"");
								        					htsup.put(IConstants.TRANSPORTID, "0");
								        					htsup.put(IConstants.RCBNO, sRcbno);
								        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
								        					htsup.put(IConstants.WEBSITE,WEBSITE);
								        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
								        					htsup.put(IConstants.TWITTER,TWITTER);
								        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
								        					htsup.put(IConstants.SKYPE,SKYPE);
								        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
								        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
								        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
								        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
								        			        	  sBANKNAME="";
								        			          htsup.put(IDBConstants.BANKNAME,"");
								        			          htsup.put(IDBConstants.IBAN,sIBAN);
								        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
								        			          htsup.put("CRAT",new DateUtils().getDateTime());
								        			          htsup.put("CRBY",username);
								        			          htsup.put("Comment1", " 0 ");
								        			          boolean custInserted = new CustUtil().insertVendor(htsup);
								        				}
								        				}
								        			  }
								        				//Supplier END
								        		  	}
								        	  	}
							        	  	}
				    			    }else if(PARENT_PLANT == null){
				    			    	boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  additemSupplier = itemUtil.insertItemSupplier(HM);
									        			//SUPPLIER START 
									        			  if(ITEMSUPPLIER_VAL.length()>0) {
									        				ArrayList arrCust = new CustUtil().getVendorDetails(ITEMSUPPLIER_VAL,plant);
									        				if (arrCust.size() > 0) {
									        				String sCustCode = (String) arrCust.get(0);
									        				String sCustName = (String) arrCust.get(1);
									        				if (!new CustUtil().isExistVendor(ITEMSUPPLIER_VAL, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
									        				{
									        					String sAddr1 = (String) arrCust.get(2);
									        					String sAddr2 = (String) arrCust.get(3);
									        					String sAddr3 = (String) arrCust.get(4);
									        					String sAddr4 = (String) arrCust.get(15);
									        					String sCountry = (String) arrCust.get(5);
									        					String sZip = (String) arrCust.get(6);
									        					String sCons = (String) arrCust.get(7);
									        					String sContactName = (String) arrCust.get(8);
									        					String sDesgination = (String) arrCust.get(9);
									        					String sTelNo = (String) arrCust.get(10);
									        					String sHpNo = (String) arrCust.get(11);
									        					String sEmail = (String) arrCust.get(12);
									        					String sFax = (String) arrCust.get(13);
									        					String sRemarks = (String) arrCust.get(14);
									        					String isActive = (String) arrCust.get(16);
									        					String sPayTerms = (String) arrCust.get(17);
									        					String sPayMentTerms = (String) arrCust.get(39);
									        					String sPayInDays = (String) arrCust.get(18);
									        					String sState = (String) arrCust.get(19);
									        					String sRcbno = (String) arrCust.get(20);
									        					String CUSTOMEREMAIL = (String) arrCust.get(22);
									        					String WEBSITE = (String) arrCust.get(23);
									        					String FACEBOOK = (String) arrCust.get(24);
									        					String TWITTER = (String) arrCust.get(25);
									        					String LINKEDIN = (String) arrCust.get(26);
									        					String SKYPE = (String) arrCust.get(27);
									        					String OPENINGBALANCE = (String) arrCust.get(28);
									        					String WORKPHONE = (String) arrCust.get(29);
									        					String sTAXTREATMENT = (String) arrCust.get(30);
									        					String sCountryCode = (String) arrCust.get(31);
									        					String sBANKNAME = (String) arrCust.get(32);
									        					String sBRANCH= (String) arrCust.get(33);
									        					String sIBAN = (String) arrCust.get(34);
									        					String sBANKROUTINGCODE = (String) arrCust.get(35);
									        					String companyregnumber = (String) arrCust.get(36);
									        					String PEPPOL = (String) arrCust.get(40);
									        					String PEPPOL_ID = (String) arrCust.get(41);
									        					String CURRENCY = (String) arrCust.get(37);
									        					Hashtable htsup = new Hashtable();
									        					htsup.put(IDBConstants.PLANT,parentplant);
									        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
									        					htsup.put(IConstants.VENDOR_NAME, sCustName);
									        					htsup.put(IConstants.companyregnumber,companyregnumber);
									        					htsup.put("ISPEPPOL", PEPPOL);
									        					htsup.put("PEPPOL_ID", PEPPOL_ID);
									        					htsup.put("CURRENCY_ID", CURRENCY);
									        					htsup.put(IConstants.NAME, sContactName);
									        					htsup.put(IConstants.DESGINATION, sDesgination);
									        					htsup.put(IConstants.TELNO, sTelNo);
									        					htsup.put(IConstants.HPNO, sHpNo);
									        					htsup.put(IConstants.FAX, sFax);
									        					htsup.put(IConstants.EMAIL, sEmail);
									        					htsup.put(IConstants.ADDRESS1, sAddr1);
									        					htsup.put(IConstants.ADDRESS2, sAddr2);
									        					htsup.put(IConstants.ADDRESS3, sAddr3);
									        					htsup.put(IConstants.ADDRESS4, sAddr4);
									        					if(sState.equalsIgnoreCase("Select State"))
									        						sState="";
									        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
									        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
									        					htsup.put(IConstants.ZIP, sZip);
									        					htsup.put(IConstants.USERFLG1, sCons);
									        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
									        					htsup.put(IConstants.PAYTERMS, "");
									        					htsup.put(IConstants.payment_terms, "");
									        					htsup.put(IConstants.PAYINDAYS, "");
									        					htsup.put(IConstants.ISACTIVE, isActive);
									        					htsup.put(IConstants.SUPPLIERTYPEID,"");
									        					htsup.put(IConstants.TRANSPORTID, "0");
									        					htsup.put(IConstants.RCBNO, sRcbno);
									        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
									        					htsup.put(IConstants.WEBSITE,WEBSITE);
									        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
									        					htsup.put(IConstants.TWITTER,TWITTER);
									        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
									        					htsup.put(IConstants.SKYPE,SKYPE);
									        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
									        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
									        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
									        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
									        			        	  sBANKNAME="";
									        			          htsup.put(IDBConstants.BANKNAME,"");
									        			          htsup.put(IDBConstants.IBAN,sIBAN);
									        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
									        			          htsup.put("CRAT",new DateUtils().getDateTime());
									        			          htsup.put("CRBY",username);
									        			          htsup.put("Comment1", " 0 ");
									        			          boolean custInserted = new CustUtil().insertVendor(htsup);
									        				}
									        				}
									        			  }//Supplier END
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
									        				  additemSupplier = itemUtil.insertItemSupplier(HM);
									        				  
									        				//SUPPLIER START 
									        				  if(ITEMSUPPLIER_VAL.length()>0) {
										        				ArrayList arrCust = new CustUtil().getVendorDetails(ITEMSUPPLIER_VAL,plant);
										        				if (arrCust.size() > 0) {
										        				String sCustCode = (String) arrCust.get(0);
										        				String sCustName = (String) arrCust.get(1);
										        				if (!new CustUtil().isExistVendor(ITEMSUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
										        				{
										        					String sAddr1 = (String) arrCust.get(2);
										        					String sAddr2 = (String) arrCust.get(3);
										        					String sAddr3 = (String) arrCust.get(4);
										        					String sAddr4 = (String) arrCust.get(15);
										        					String sCountry = (String) arrCust.get(5);
										        					String sZip = (String) arrCust.get(6);
										        					String sCons = (String) arrCust.get(7);
										        					String sContactName = (String) arrCust.get(8);
										        					String sDesgination = (String) arrCust.get(9);
										        					String sTelNo = (String) arrCust.get(10);
										        					String sHpNo = (String) arrCust.get(11);
										        					String sEmail = (String) arrCust.get(12);
										        					String sFax = (String) arrCust.get(13);
										        					String sRemarks = (String) arrCust.get(14);
										        					String isActive = (String) arrCust.get(16);
										        					String sPayTerms = (String) arrCust.get(17);
										        					String sPayMentTerms = (String) arrCust.get(39);
										        					String sPayInDays = (String) arrCust.get(18);
										        					String sState = (String) arrCust.get(19);
										        					String sRcbno = (String) arrCust.get(20);
										        					String CUSTOMEREMAIL = (String) arrCust.get(22);
										        					String WEBSITE = (String) arrCust.get(23);
										        					String FACEBOOK = (String) arrCust.get(24);
										        					String TWITTER = (String) arrCust.get(25);
										        					String LINKEDIN = (String) arrCust.get(26);
										        					String SKYPE = (String) arrCust.get(27);
										        					String OPENINGBALANCE = (String) arrCust.get(28);
										        					String WORKPHONE = (String) arrCust.get(29);
										        					String sTAXTREATMENT = (String) arrCust.get(30);
										        					String sCountryCode = (String) arrCust.get(31);
										        					String sBANKNAME = (String) arrCust.get(32);
										        					String sBRANCH= (String) arrCust.get(33);
										        					String sIBAN = (String) arrCust.get(34);
										        					String sBANKROUTINGCODE = (String) arrCust.get(35);
										        					String companyregnumber = (String) arrCust.get(36);
										        					String PEPPOL = (String) arrCust.get(40);
										        					String PEPPOL_ID = (String) arrCust.get(41);
										        					String CURRENCY = (String) arrCust.get(37);
										        					Hashtable htsup = new Hashtable();
										        					htsup.put(IDBConstants.PLANT,childplant);
										        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
										        					htsup.put(IConstants.VENDOR_NAME, sCustName);
										        					htsup.put(IConstants.companyregnumber,companyregnumber);
										        					htsup.put("ISPEPPOL", PEPPOL);
										        					htsup.put("PEPPOL_ID", PEPPOL_ID);
										        					htsup.put("CURRENCY_ID", CURRENCY);
										        					htsup.put(IConstants.NAME, sContactName);
										        					htsup.put(IConstants.DESGINATION, sDesgination);
										        					htsup.put(IConstants.TELNO, sTelNo);
										        					htsup.put(IConstants.HPNO, sHpNo);
										        					htsup.put(IConstants.FAX, sFax);
										        					htsup.put(IConstants.EMAIL, sEmail);
										        					htsup.put(IConstants.ADDRESS1, sAddr1);
										        					htsup.put(IConstants.ADDRESS2, sAddr2);
										        					htsup.put(IConstants.ADDRESS3, sAddr3);
										        					htsup.put(IConstants.ADDRESS4, sAddr4);
										        					if(sState.equalsIgnoreCase("Select State"))
										        						sState="";
										        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
										        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
										        					htsup.put(IConstants.ZIP, sZip);
										        					htsup.put(IConstants.USERFLG1, sCons);
										        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
										        					htsup.put(IConstants.PAYTERMS, "");
										        					htsup.put(IConstants.payment_terms, "");
										        					htsup.put(IConstants.PAYINDAYS, "");
										        					htsup.put(IConstants.ISACTIVE, isActive);
										        					htsup.put(IConstants.SUPPLIERTYPEID,"");
										        					htsup.put(IConstants.TRANSPORTID, "0");
										        					htsup.put(IConstants.RCBNO, sRcbno);
										        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
										        					htsup.put(IConstants.WEBSITE,WEBSITE);
										        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
										        					htsup.put(IConstants.TWITTER,TWITTER);
										        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
										        					htsup.put(IConstants.SKYPE,SKYPE);
										        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
										        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
										        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
										        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
										        			        	  sBANKNAME="";
										        			          htsup.put(IDBConstants.BANKNAME,"");
										        			          htsup.put(IDBConstants.IBAN,sIBAN);
										        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
										        			          htsup.put("CRAT",new DateUtils().getDateTime());
										        			          htsup.put("CRBY",username);
										        			          htsup.put("Comment1", " 0 ");
										        			          boolean custInserted = new CustUtil().insertVendor(htsup);
										        				}
										        				}
									        				  }//Supplier END
									        			  }
									        		  	}
									        	  	}
								        	  	}	
								        	  }
						             	  }//IMTHI END
				        		}
			        		  }//imti end insert item supplier Purchase tab
 			           
			        	     //insert supplier discount Purchase
			        	  boolean IBSupplierDiscount=false;
			        	  for(int i =0 ; i < DYNAMIC_SUPPLIER_DISCOUNT.size() ; i++){
			        		  String DYNAMIC_SUPPLIER_DISCOUNT_VAL = (String) DYNAMIC_SUPPLIER_DISCOUNT.get(i);
			        		  String SUPPLIER_VAL = (String) SUPPLIER.get(i);
			        		  if(DYNAMIC_SUPPLIER_DISCOUNT_VAL==""){
				        			break;
				        		}else{
				        			String SupplierDesc;
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, ITEM);
				    				HM.put(IConstants.VENDNO, SUPPLIER_VAL);
				    				VendMstDAO vendMstDAO = new VendMstDAO();
				    				SupplierDesc=vendMstDAO.getVendorNameByNo(HM);
				    					HM.put("VNAME", SupplierDesc);				    				
				    				System.out.println("IBDISCOUNT"+IBDISCOUNT);
				    				if(IBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
				    					HM.put("IBDISCOUNT", DYNAMIC_SUPPLIER_DISCOUNT_VAL+"%");
				    									    				}
				    				else{
				    					HM.put("IBDISCOUNT", DYNAMIC_SUPPLIER_DISCOUNT_VAL);
				    									    				}
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
				    			    
				    			    //IMTHI ADD for CHILD PARENT to insert supplier discount
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
								        			  
								        			//SUPPLIER START 
								        			  if(SUPPLIER_VAL.length()>0) {
								        				ArrayList arrCust = new CustUtil().getVendorDetails(SUPPLIER_VAL,plant);
								        				if (arrCust.size() > 0) {
								        				String sCustCode = (String) arrCust.get(0);
								        				String sCustName = (String) arrCust.get(1);
								        				if (!new CustUtil().isExistVendor(SUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
								        				{
								        					String sAddr1 = (String) arrCust.get(2);
								        					String sAddr2 = (String) arrCust.get(3);
								        					String sAddr3 = (String) arrCust.get(4);
								        					String sAddr4 = (String) arrCust.get(15);
								        					String sCountry = (String) arrCust.get(5);
								        					String sZip = (String) arrCust.get(6);
								        					String sCons = (String) arrCust.get(7);
								        					String sContactName = (String) arrCust.get(8);
								        					String sDesgination = (String) arrCust.get(9);
								        					String sTelNo = (String) arrCust.get(10);
								        					String sHpNo = (String) arrCust.get(11);
								        					String sEmail = (String) arrCust.get(12);
								        					String sFax = (String) arrCust.get(13);
								        					String sRemarks = (String) arrCust.get(14);
								        					String isActive = (String) arrCust.get(16);
								        					String sPayTerms = (String) arrCust.get(17);
								        					String sPayMentTerms = (String) arrCust.get(39);
								        					String sPayInDays = (String) arrCust.get(18);
								        					String sState = (String) arrCust.get(19);
								        					String sRcbno = (String) arrCust.get(20);
								        					String CUSTOMEREMAIL = (String) arrCust.get(22);
								        					String WEBSITE = (String) arrCust.get(23);
								        					String FACEBOOK = (String) arrCust.get(24);
								        					String TWITTER = (String) arrCust.get(25);
								        					String LINKEDIN = (String) arrCust.get(26);
								        					String SKYPE = (String) arrCust.get(27);
								        					String OPENINGBALANCE = (String) arrCust.get(28);
								        					String WORKPHONE = (String) arrCust.get(29);
								        					String sTAXTREATMENT = (String) arrCust.get(30);
								        					String sCountryCode = (String) arrCust.get(31);
								        					String sBANKNAME = (String) arrCust.get(32);
								        					String sBRANCH= (String) arrCust.get(33);
								        					String sIBAN = (String) arrCust.get(34);
								        					String sBANKROUTINGCODE = (String) arrCust.get(35);
								        					String companyregnumber = (String) arrCust.get(36);
								        					String PEPPOL = (String) arrCust.get(40);
								        					String PEPPOL_ID = (String) arrCust.get(41);
								        					String CURRENCY = (String) arrCust.get(37);
//								        					String transport = (String) arrCust.get(38);
//								        					String suppliertypeid = (String) arrCust.get(21);
								        					Hashtable htsup = new Hashtable();
								        					htsup.put(IDBConstants.PLANT,childplant);
								        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
								        					htsup.put(IConstants.VENDOR_NAME, sCustName);
								        					htsup.put(IConstants.companyregnumber,companyregnumber);
								        					htsup.put("ISPEPPOL", PEPPOL);
								        					htsup.put("PEPPOL_ID", PEPPOL_ID);
								        					htsup.put("CURRENCY_ID", CURRENCY);
								        					htsup.put(IConstants.NAME, sContactName);
								        					htsup.put(IConstants.DESGINATION, sDesgination);
								        					htsup.put(IConstants.TELNO, sTelNo);
								        					htsup.put(IConstants.HPNO, sHpNo);
								        					htsup.put(IConstants.FAX, sFax);
								        					htsup.put(IConstants.EMAIL, sEmail);
								        					htsup.put(IConstants.ADDRESS1, sAddr1);
								        					htsup.put(IConstants.ADDRESS2, sAddr2);
								        					htsup.put(IConstants.ADDRESS3, sAddr3);
								        					htsup.put(IConstants.ADDRESS4, sAddr4);
								        					if(sState.equalsIgnoreCase("Select State"))
								        						sState="";
								        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
								        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
								        					htsup.put(IConstants.ZIP, sZip);
								        					htsup.put(IConstants.USERFLG1, sCons);
								        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//								        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//								        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//								        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
								        					htsup.put(IConstants.PAYTERMS, "");
								        					htsup.put(IConstants.payment_terms, "");
								        					htsup.put(IConstants.PAYINDAYS, "");
								        					htsup.put(IConstants.ISACTIVE, isActive);
//								        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//								        					htsup.put(IConstants.TRANSPORTID,transport);
								        					htsup.put(IConstants.SUPPLIERTYPEID,"");
								        					htsup.put(IConstants.TRANSPORTID, "0");
								        					htsup.put(IConstants.RCBNO, sRcbno);
								        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
								        					htsup.put(IConstants.WEBSITE,WEBSITE);
								        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
								        					htsup.put(IConstants.TWITTER,TWITTER);
								        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
								        					htsup.put(IConstants.SKYPE,SKYPE);
								        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
								        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
								        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
								        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
								        			        	  sBANKNAME="";
//								        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
								        			          htsup.put(IDBConstants.BANKNAME,"");
								        			          htsup.put(IDBConstants.IBAN,sIBAN);
								        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
								        			          htsup.put("CRAT",new DateUtils().getDateTime());
								        			          htsup.put("CRBY",username);
								        			          htsup.put("Comment1", " 0 ");
								        			          boolean custInserted = new CustUtil().insertVendor(htsup);
								        				}
								        				}
								        			  }
								        				//Supplier END
								        			  
//								        			//IMTHI ADD child company to insert in supplier type
//								        			  ArrayList arrCust = new CustomerBeanDAO().getSupplierTypeDetails(SUPPLIER_VAL,plant);
//								        			  	String sSupTypeId = (String) arrCust.get(1);
//								        			  	String sSupTypeDesc = (String) arrCust.get(2);
//								        			  	String sIsactive = (String) arrCust.get(7);
//									        			Hashtable htSup = new Hashtable();
//									        			htSup.put(IDBConstants.PLANT,childplant);
//									        			htSup.put(IDBConstants.SUPPLIERTYPEID,SUPPLIER_VAL);
//									        		    if ((!new CustUtil().isExistsSupplierType(htSup))) {
//									        		    	Hashtable htsupAdd = new Hashtable();
//									        		    	    htsupAdd.clear();
//										        		    	htsupAdd.put(IDBConstants.PLANT,childplant);
//										        		    	htsupAdd.put(IDBConstants.SUPPLIERTYPEID,sSupTypeId);
//										        		    	htsupAdd.put(IDBConstants.SUPPLIERTYPEDESC, sSupTypeDesc);
//										        				htsupAdd.put(IConstants.ISACTIVE, sIsactive);
//										        				htsupAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//										        				htsupAdd.put(IDBConstants.CREATED_BY, username);
//										        		    Hashtable htms = new Hashtable();
//										        		       htms.clear();
//										        		       htms.put("PLANT",childplant);
//										        	           htms.put("DIRTYPE",TransactionConstants.ADD_SUPPLIER_TYPE);
//										        	           htms.put("RECID","");
//										        	           htms.put("ITEM",sSupTypeId);
//										        	           htms.put("REMARKS",sSupTypeDesc);
//										        	           htms.put("UPBY",username);   
//										        	           htms.put("CRBY",username);
//										        	           htms.put("CRAT",dateutils.getDateTime());
//										        	           htms.put("UPAT",dateutils.getDateTime());
//										        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
//										        	          
//										        	           htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
//										        	           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
//									        		           boolean SupplierTypeInserted = new CustUtil().insertSupplierTypeMst(htsupAdd);
//									        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//									        		    }//IMTHI END SUPPLIER TYPE
								        		  	}
								        	  	}
							        	  	}
				    			    }else if(PARENT_PLANT == null){
				    			    	boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
									        			  
									        			  
									        			//SUPPLIER START 
									        			  if(SUPPLIER_VAL.length()>0) {
									        				ArrayList arrCust = new CustUtil().getVendorDetails(SUPPLIER_VAL,plant);
									        				if (arrCust.size() > 0) {
									        				String sCustCode = (String) arrCust.get(0);
									        				String sCustName = (String) arrCust.get(1);
									        				if (!new CustUtil().isExistVendor(SUPPLIER_VAL, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
									        				{
									        					String sAddr1 = (String) arrCust.get(2);
									        					String sAddr2 = (String) arrCust.get(3);
									        					String sAddr3 = (String) arrCust.get(4);
									        					String sAddr4 = (String) arrCust.get(15);
									        					String sCountry = (String) arrCust.get(5);
									        					String sZip = (String) arrCust.get(6);
									        					String sCons = (String) arrCust.get(7);
									        					String sContactName = (String) arrCust.get(8);
									        					String sDesgination = (String) arrCust.get(9);
									        					String sTelNo = (String) arrCust.get(10);
									        					String sHpNo = (String) arrCust.get(11);
									        					String sEmail = (String) arrCust.get(12);
									        					String sFax = (String) arrCust.get(13);
									        					String sRemarks = (String) arrCust.get(14);
									        					String isActive = (String) arrCust.get(16);
									        					String sPayTerms = (String) arrCust.get(17);
									        					String sPayMentTerms = (String) arrCust.get(39);
									        					String sPayInDays = (String) arrCust.get(18);
									        					String sState = (String) arrCust.get(19);
									        					String sRcbno = (String) arrCust.get(20);
									        					String CUSTOMEREMAIL = (String) arrCust.get(22);
									        					String WEBSITE = (String) arrCust.get(23);
									        					String FACEBOOK = (String) arrCust.get(24);
									        					String TWITTER = (String) arrCust.get(25);
									        					String LINKEDIN = (String) arrCust.get(26);
									        					String SKYPE = (String) arrCust.get(27);
									        					String OPENINGBALANCE = (String) arrCust.get(28);
									        					String WORKPHONE = (String) arrCust.get(29);
									        					String sTAXTREATMENT = (String) arrCust.get(30);
									        					String sCountryCode = (String) arrCust.get(31);
									        					String sBANKNAME = (String) arrCust.get(32);
									        					String sBRANCH= (String) arrCust.get(33);
									        					String sIBAN = (String) arrCust.get(34);
									        					String sBANKROUTINGCODE = (String) arrCust.get(35);
									        					String companyregnumber = (String) arrCust.get(36);
									        					String PEPPOL = (String) arrCust.get(40);
									        					String PEPPOL_ID = (String) arrCust.get(41);
									        					String CURRENCY = (String) arrCust.get(37);
//									        					String transport = (String) arrCust.get(38);
//									        					String suppliertypeid = (String) arrCust.get(21);
									        					Hashtable htsup = new Hashtable();
									        					htsup.put(IDBConstants.PLANT,parentplant);
									        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
									        					htsup.put(IConstants.VENDOR_NAME, sCustName);
									        					htsup.put(IConstants.companyregnumber,companyregnumber);
									        					htsup.put("ISPEPPOL", PEPPOL);
									        					htsup.put("PEPPOL_ID", PEPPOL_ID);
									        					htsup.put("CURRENCY_ID", CURRENCY);
									        					htsup.put(IConstants.NAME, sContactName);
									        					htsup.put(IConstants.DESGINATION, sDesgination);
									        					htsup.put(IConstants.TELNO, sTelNo);
									        					htsup.put(IConstants.HPNO, sHpNo);
									        					htsup.put(IConstants.FAX, sFax);
									        					htsup.put(IConstants.EMAIL, sEmail);
									        					htsup.put(IConstants.ADDRESS1, sAddr1);
									        					htsup.put(IConstants.ADDRESS2, sAddr2);
									        					htsup.put(IConstants.ADDRESS3, sAddr3);
									        					htsup.put(IConstants.ADDRESS4, sAddr4);
									        					if(sState.equalsIgnoreCase("Select State"))
									        						sState="";
									        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
									        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
									        					htsup.put(IConstants.ZIP, sZip);
									        					htsup.put(IConstants.USERFLG1, sCons);
									        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//									        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//									        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//									        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
									        					htsup.put(IConstants.PAYTERMS, "");
									        					htsup.put(IConstants.payment_terms, "");
									        					htsup.put(IConstants.PAYINDAYS, "");
									        					htsup.put(IConstants.ISACTIVE, isActive);
//									        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//									        					htsup.put(IConstants.TRANSPORTID,transport);
									        					htsup.put(IConstants.SUPPLIERTYPEID,"");
									        					htsup.put(IConstants.TRANSPORTID, "0");
									        					htsup.put(IConstants.RCBNO, sRcbno);
									        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
									        					htsup.put(IConstants.WEBSITE,WEBSITE);
									        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
									        					htsup.put(IConstants.TWITTER,TWITTER);
									        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
									        					htsup.put(IConstants.SKYPE,SKYPE);
									        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
									        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
									        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
									        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
									        			        	  sBANKNAME="";
//									        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
									        			          htsup.put(IDBConstants.BANKNAME,"");
									        			          htsup.put(IDBConstants.IBAN,sIBAN);
									        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
									        			          htsup.put("CRAT",new DateUtils().getDateTime());
									        			          htsup.put("CRBY",username);
									        			          htsup.put("Comment1", " 0 ");
									        			          boolean custInserted = new CustUtil().insertVendor(htsup);
									        				}
									        				}
									        			  }
									        				//Supplier END
									        				
//									        			//IMTHI ADD child company to insert in supplier type
//									        			  ArrayList arrCust = new CustomerBeanDAO().getSupplierTypeDetails(SUPPLIER_VAL,plant);
//									        			  	String sSupTypeId = (String) arrCust.get(1);
//									        			  	String sSupTypeDesc = (String) arrCust.get(2);
//									        			  	String sIsactive = (String) arrCust.get(7);
//										        			Hashtable htSup = new Hashtable();
//										        			htSup.put(IDBConstants.PLANT,parentplant);
//										        			htSup.put(IDBConstants.SUPPLIERTYPEID,SUPPLIER_VAL);
//										        		    if ((!new CustUtil().isExistsSupplierType(htSup))) {
//										        		    	Hashtable htsupAdd = new Hashtable();
//										        		    	    htsupAdd.clear();
//											        		    	htsupAdd.put(IDBConstants.PLANT,parentplant);
//											        		    	htsupAdd.put(IDBConstants.SUPPLIERTYPEID,sSupTypeId);
//											        		    	htsupAdd.put(IDBConstants.SUPPLIERTYPEDESC, sSupTypeDesc);
//											        				htsupAdd.put(IConstants.ISACTIVE, sIsactive);
//											        				htsupAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//											        				htsupAdd.put(IDBConstants.CREATED_BY, username);
//											        		    Hashtable htms = new Hashtable();
//											        		       htms.clear();
//											        		       htms.put("PLANT",parentplant);
//											        	           htms.put("DIRTYPE",TransactionConstants.ADD_SUPPLIER_TYPE);
//											        	           htms.put("RECID","");
//											        	           htms.put("ITEM",sSupTypeId);
//											        	           htms.put("REMARKS",sSupTypeDesc);
//											        	           htms.put("UPBY",username);   
//											        	           htms.put("CRBY",username);
//											        	           htms.put("CRAT",dateutils.getDateTime());
//											        	           htms.put("UPAT",dateutils.getDateTime());
//											        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
//											        	          
//											        	           htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
//											        	           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
//										        		           boolean SupplierTypeInserted = new CustUtil().insertSupplierTypeMst(htsupAdd);
//										        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//										        		    }//IMTHI END SUPPLIER TYPE
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
									        				  IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
									        				  
									        				//SUPPLIER START 
									        				  if(SUPPLIER_VAL.length()>0) {
										        				ArrayList arrCust = new CustUtil().getVendorDetails(SUPPLIER_VAL,plant);
										        				if (arrCust.size() > 0) {
										        				String sCustCode = (String) arrCust.get(0);
										        				String sCustName = (String) arrCust.get(1);
										        				if (!new CustUtil().isExistVendor(SUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
										        				{
										        					String sAddr1 = (String) arrCust.get(2);
										        					String sAddr2 = (String) arrCust.get(3);
										        					String sAddr3 = (String) arrCust.get(4);
										        					String sAddr4 = (String) arrCust.get(15);
										        					String sCountry = (String) arrCust.get(5);
										        					String sZip = (String) arrCust.get(6);
										        					String sCons = (String) arrCust.get(7);
										        					String sContactName = (String) arrCust.get(8);
										        					String sDesgination = (String) arrCust.get(9);
										        					String sTelNo = (String) arrCust.get(10);
										        					String sHpNo = (String) arrCust.get(11);
										        					String sEmail = (String) arrCust.get(12);
										        					String sFax = (String) arrCust.get(13);
										        					String sRemarks = (String) arrCust.get(14);
										        					String isActive = (String) arrCust.get(16);
										        					String sPayTerms = (String) arrCust.get(17);
										        					String sPayMentTerms = (String) arrCust.get(39);
										        					String sPayInDays = (String) arrCust.get(18);
										        					String sState = (String) arrCust.get(19);
										        					String sRcbno = (String) arrCust.get(20);
										        					String CUSTOMEREMAIL = (String) arrCust.get(22);
										        					String WEBSITE = (String) arrCust.get(23);
										        					String FACEBOOK = (String) arrCust.get(24);
										        					String TWITTER = (String) arrCust.get(25);
										        					String LINKEDIN = (String) arrCust.get(26);
										        					String SKYPE = (String) arrCust.get(27);
										        					String OPENINGBALANCE = (String) arrCust.get(28);
										        					String WORKPHONE = (String) arrCust.get(29);
										        					String sTAXTREATMENT = (String) arrCust.get(30);
										        					String sCountryCode = (String) arrCust.get(31);
										        					String sBANKNAME = (String) arrCust.get(32);
										        					String sBRANCH= (String) arrCust.get(33);
										        					String sIBAN = (String) arrCust.get(34);
										        					String sBANKROUTINGCODE = (String) arrCust.get(35);
										        					String companyregnumber = (String) arrCust.get(36);
										        					String PEPPOL = (String) arrCust.get(40);
										        					String PEPPOL_ID = (String) arrCust.get(41);
										        					String CURRENCY = (String) arrCust.get(37);
//										        					String transport = (String) arrCust.get(38);
//										        					String suppliertypeid = (String) arrCust.get(21);
										        					Hashtable htsup = new Hashtable();
										        					htsup.put(IDBConstants.PLANT,childplant);
										        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
										        					htsup.put(IConstants.VENDOR_NAME, sCustName);
										        					htsup.put(IConstants.companyregnumber,companyregnumber);
										        					htsup.put("ISPEPPOL", PEPPOL);
										        					htsup.put("PEPPOL_ID", PEPPOL_ID);
										        					htsup.put("CURRENCY_ID", CURRENCY);
										        					htsup.put(IConstants.NAME, sContactName);
										        					htsup.put(IConstants.DESGINATION, sDesgination);
										        					htsup.put(IConstants.TELNO, sTelNo);
										        					htsup.put(IConstants.HPNO, sHpNo);
										        					htsup.put(IConstants.FAX, sFax);
										        					htsup.put(IConstants.EMAIL, sEmail);
										        					htsup.put(IConstants.ADDRESS1, sAddr1);
										        					htsup.put(IConstants.ADDRESS2, sAddr2);
										        					htsup.put(IConstants.ADDRESS3, sAddr3);
										        					htsup.put(IConstants.ADDRESS4, sAddr4);
										        					if(sState.equalsIgnoreCase("Select State"))
										        						sState="";
										        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
										        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
										        					htsup.put(IConstants.ZIP, sZip);
										        					htsup.put(IConstants.USERFLG1, sCons);
										        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//										        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//										        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//										        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
										        					htsup.put(IConstants.PAYTERMS, "");
										        					htsup.put(IConstants.payment_terms, "");
										        					htsup.put(IConstants.PAYINDAYS, "");
										        					htsup.put(IConstants.ISACTIVE, isActive);
//										        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//										        					htsup.put(IConstants.TRANSPORTID,transport);
										        					htsup.put(IConstants.SUPPLIERTYPEID,"");
										        					htsup.put(IConstants.TRANSPORTID, "0");
										        					htsup.put(IConstants.RCBNO, sRcbno);
										        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
										        					htsup.put(IConstants.WEBSITE,WEBSITE);
										        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
										        					htsup.put(IConstants.TWITTER,TWITTER);
										        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
										        					htsup.put(IConstants.SKYPE,SKYPE);
										        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
										        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
										        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
										        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
										        			        	  sBANKNAME="";
//										        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
										        			          htsup.put(IDBConstants.BANKNAME,"");
										        			          htsup.put(IDBConstants.IBAN,sIBAN);
										        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
										        			          htsup.put("CRAT",new DateUtils().getDateTime());
										        			          htsup.put("CRBY",username);
										        			          htsup.put("Comment1", " 0 ");
										        			          boolean custInserted = new CustUtil().insertVendor(htsup);
										        				}
										        				}
									        				  }
										        				//Supplier END

//									        				//IMTHI ADD child company to insert in supplier type
//										        			  ArrayList arrCust = new CustomerBeanDAO().getSupplierTypeDetails(SUPPLIER_VAL,plant);
//										        			  	String sSupTypeId = (String) arrCust.get(1);
//										        			  	String sSupTypeDesc = (String) arrCust.get(2);
//										        			  	String sIsactive = (String) arrCust.get(7);
//											        			Hashtable htSup = new Hashtable();
//											        			htSup.put(IDBConstants.PLANT,childplant);
//											        			htSup.put(IDBConstants.SUPPLIERTYPEID,SUPPLIER_VAL);
//											        		    if ((!new CustUtil().isExistsSupplierType(htSup))) {
//											        		    	Hashtable htsupAdd = new Hashtable();
//											        		    	    htsupAdd.clear();
//												        		    	htsupAdd.put(IDBConstants.PLANT,childplant);
//												        		    	htsupAdd.put(IDBConstants.SUPPLIERTYPEID,sSupTypeId);
//												        		    	htsupAdd.put(IDBConstants.SUPPLIERTYPEDESC, sSupTypeDesc);
//												        				htsupAdd.put(IConstants.ISACTIVE, sIsactive);
//												        				htsupAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//												        				htsupAdd.put(IDBConstants.CREATED_BY, username);
//												        		    Hashtable htms = new Hashtable();
//												        		       htms.clear();
//												        		       htms.put("PLANT",childplant);
//												        	           htms.put("DIRTYPE",TransactionConstants.ADD_SUPPLIER_TYPE);
//												        	           htms.put("RECID","");
//												        	           htms.put("ITEM",sSupTypeId);
//												        	           htms.put("REMARKS",sSupTypeDesc);
//												        	           htms.put("UPBY",username);   
//												        	           htms.put("CRBY",username);
//												        	           htms.put("CRAT",dateutils.getDateTime());
//												        	           htms.put("UPAT",dateutils.getDateTime());
//												        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
//												        	          
//												        	           htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
//												        	           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
//											        		           boolean SupplierTypeInserted = new CustUtil().insertSupplierTypeMst(htsupAdd);
//											        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//											        		    }//IMTHI END SUPPLIER TYPE
									        			  }
									        		  	}
									        	  	}
								        	  	}	
								        	  }
						             	  }//IMTHI END
				    			    
			        	  }
			        	  }
			        	  /*DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_SUPPLIERDISCOUNT_SIZE)).intValue();
			        	  for(int nameCount = 0; nameCount<=DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT;nameCount++){
				        		 System.out.println("nameCount"+nameCount);
				        		 System.out.println("nameCount"+strUtils.fString(request.getParameter("SUPPLIER_"+nameCount)));
				        		if(StrUtils.fString(request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount))==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, ITEM);
				    				HM.put(IConstants.VENDNO, strUtils.fString(request.getParameter("SUPPLIER_"+nameCount)));
				    				System.out.println("IBDISCOUNT"+IBDISCOUNT);
				    				if(IBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
				    					HM.put("IBDISCOUNT", request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount)+"%");
				    									    				}
				    				else{
				    					HM.put("IBDISCOUNT", request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount));
				    									    				}
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
				    								        		
				        		}
				        	 }*/
			        		
			        	  
			        	  
			    		
 			           //end insert supplier discount Purchase
			            //if(OBCustomerDiscount){
			          	 inserted = mdao.insertIntoMovHis(htm);
			          	 
		    			//IMTHI ADD for CHILD PARENT to insert Activity log
			          	if(PARENT_PLANT != null){
				        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
				        	  if(CHECKplantCompany == null)
				  				CHECKplantCompany = "0";
				        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  if (arrList.size() > 0) {
					        		  for (int m=0; m < arrList.size(); m++ ) {
					        			  Map map = (Map) arrList.get(m);
					        			  String childplant = (String) map.get("CHILD_PLANT");
					        			  htm.put("PLANT",childplant);
					        			  inserted = mdao.insertIntoMovHis(htm);
					        		  	}
					        	  	}
				        	  	}
			          	}else if(PARENT_PLANT == null){
			          		boolean ischild = false;
					        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
					        	  if (arrLi.size() > 0) {
					        	  Map mst = (Map) arrLi.get(0);
					        	  String parent = (String) mst.get("PARENT_PLANT");
					         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
					        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
					        	  if(Ischildasparent){
					        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        			  ischild = true;
					        		  }
					        	  }else{
					        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  ischild = true;
					        	  }
					        	  }
					        	  if(ischild){
					        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
						        	  String  parentplant ="";
						        	  if (arrLists.size() > 0) {
						        		  for (int j=0; j < arrLists.size(); j++ ) {
						        			  Map ms = (Map) arrLists.get(j);
						        			  parentplant = (String) ms.get("PARENT_PLANT");
						        			  htm.put("PLANT",parentplant);
						        			  inserted = mdao.insertIntoMovHis(htm);
						        		  }
						        	  }
						        	  
						        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  Map m = new HashMap();
						        	  if (arrList.size() > 0) {
						        		  for (int k=0; k < arrList.size(); k++ ) {
						        			  m = (Map) arrList.get(k);
						        			  String childplant = (String) m.get("CHILD_PLANT");
						        			  if(childplant!=plant) {
						        				  htm.put("PLANT",childplant);
						        				  inserted = mdao.insertIntoMovHis(htm);
						        			  }
						        		  	}
						        	  	}
					        	  	}	
					        	  }
			             	  }//IMTHI END
			          // }
			          }
//			          htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
//		              htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		              
		    		  boolean updateFlag;
		    		if(ITEM!="P0001")
		      		  {
		    			if(ITEM.length()>0) {
		    			boolean exitFlag = false;
		    			Hashtable htv = new Hashtable();				
		    			htv.put(IDBConstants.PLANT, plant);
		    			htv.put(IDBConstants.TBL_FUNCTION, "PRODUCT");
		    			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
		    			if (exitFlag) 
		      		    updateFlag=_TblControlDAO.updateSeqNo("PRODUCT",plant);
		    			else
		    			{
		    				boolean insertFlag = false;
		    				Map htInsert=null;
		                	Hashtable htTblCntInsert  = new Hashtable();           
		                	htTblCntInsert.put(IDBConstants.PLANT,plant);          
		                	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
		                	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"P");
		                 	htTblCntInsert.put("MINSEQ","0000");
		                 	htTblCntInsert.put("MAXSEQ","9999");
		                	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
		                	htTblCntInsert.put(IDBConstants.CREATED_BY, username);
		                	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
		                	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
		    			}
		    			}
		    		}
			          
		    		DESC=strUtils.RemoveDoubleQuotesToSingle(DESC);
		          }
			          if(itemInserted && insertAlternateItem) {
			        	  DbBean.CommitTran(ut);

							//result = "<font color=\"green\"> Product created successfully</font>";
			        	  result = "<font class = "+IConstants.SUCCESS_COLOR+">Product Created Successfully</font>";
							response
									.sendRedirect("../product/summary?result="
											+ result + "&PGaction=view");

			          }
			          
			          else {
							DbBean.RollbackTran(ut);
							//result = "<font color=\"red\"> Failed to create product </font>";
							result = "<font class = "+IConstants.FAILED_COLOR+">Failed to create product</font>";
							response
									.sendRedirect("../product/new?result="
											+ result + "&action=view");
						}

					}
			    else{
                    DbBean.RollbackTran(ut);
                    result = "<font class = "+IConstants.FAILED_COLOR+">Product Exists already. Try again</font>";
                    response
					.sendRedirect("../product/new?result="
							+ result + "&action=view");
    }
			    
				}
			
		}
		
		}
		catch (FileUploadException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private JSONObject getProcessingBomProductListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String pitem = StrUtils.fString(request.getParameter("ITEM")).trim();
        	String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
        	String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
        	String type  = StrUtils.fString(request.getParameter("TYPE"));
        	if(sItemDesc.length()>0){
	        	sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
	        	sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
        	}
        	sItemDesc =sItemDesc+sExtraCond ;
        	List listQry = null;        	
        	if(type.equalsIgnoreCase("PRODBOM"))
       	 {
       		 listQry = _ProductionBomUtil.getProcessingProdBompitemList(pitem, plant, " AND BOMTYPE='PROD'");
       	 }
       	 else if(type.equalsIgnoreCase("KITBOM"))
       	 {
       		 listQry = _ProductionBomUtil.getProcessingProdBompitemList(pitem, plant, " AND BOMTYPE='KIT'");
       	 }
       	else if(type.equalsIgnoreCase("SEMIKITBOM"))
      	 {
      		 listQry = _ProductionBomUtil.getProcessingProdBompitemList(pitem, plant, "SEMIKITBOM");
      	 }
       	 else
       	 {
       		 listQry = _ProductionBomUtil.getProcessingProdBompitemList(pitem, plant, "");
       	 }
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("PITEM")));
                    resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("PDESC")));
                    resultJsonInt.put("ITEMDETDESC", StrUtils.fString((String)m.get("PDETDESC")));
                    resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("UOM")));
                    resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));                    
                    resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("CPPI", StrUtils.fString((String)m.get("CPPI")));
                    resultJsonInt.put("INCPRICE", StrUtils.fString((String)m.get("INCPRICE")));
                    resultJsonInt.put("PQPUOM", StrUtils.fString((String)m.get("PQPUOM")));
                    resultJsonInt.put("PRD_DEPT_ID", StrUtils.fString((String)m.get("PRD_DEPT_ID")));
                    Double dcqty = Double.valueOf(StrUtils.fString((String)m.get("INVQTY"))) / Double.valueOf(StrUtils.fString((String)m.get("PQPUOM")));
                    int STKUOMQTY=  dcqty.intValue();
                    resultJsonInt.put("STKUOMQTY", StrUtils.fString(String.valueOf(STKUOMQTY)));
                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("items", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                /*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
                resultJson.put("errors", jsonArrayErr);
        	}
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private JSONObject getBomProductListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String pitem = StrUtils.fString(request.getParameter("ITEM")).trim();
        	String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
        	String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
        	String type  = StrUtils.fString(request.getParameter("TYPE"));
        	if(sItemDesc.length()>0){
	        	sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
	        	sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
        	}
        	sItemDesc =sItemDesc+sExtraCond ;
        	List listQry = null;        	
        	if(type.equalsIgnoreCase("PRODBOM"))
       	 {
       		 listQry = _ProductionBomUtil.getProdBompitemList(pitem, plant, " AND BOMTYPE='PROD'");
       	 }
       	 else if(type.equalsIgnoreCase("KITBOM"))
       	 {
       		 listQry = _ProductionBomUtil.getProdBompitemList(pitem, plant, " AND BOMTYPE='KIT'");
       	 }
       	 else
       	 {
       		 listQry = _ProductionBomUtil.getProdBompitemList(pitem, plant, "");
       	 }
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("PITEM")));
                    resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("PDESC")));
                    resultJsonInt.put("ITEMDETDESC", StrUtils.fString((String)m.get("PDETDESC")));
                    resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("UOM")));
                    resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("items", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                /*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
                resultJson.put("errors", jsonArrayErr);
        	}
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private JSONObject getBomChildProductListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ProductionBomDAO productionBomDAO = new ProductionBomDAO();
        	ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String pitem = StrUtils.fString(request.getParameter("ITEM")).trim();
        	String citem = StrUtils.fString(request.getParameter("CITEM")).trim();
        	String pqty = StrUtils.fString(request.getParameter("PQTY")).trim();
        	String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
        	String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
        	String type  = StrUtils.fString(request.getParameter("TYPE"));
        	if(sItemDesc.length()>0){
	        	sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
	        	sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
        	}
        	sItemDesc =sItemDesc+sExtraCond ;
        	List listQry = null;        	
        	if(type.equalsIgnoreCase("KITWITHBOM"))
       	 {
       		 listQry = _ProductionBomUtil.getProdBomchilditemList(pitem,citem, plant, " AND BOMTYPE='KIT'");
       	 }
       	 else
       	 {
       		 listQry = _ProductionBomUtil.getProdBomchilditemList(pitem,citem, plant, " AND BOMTYPE='PROD'"); 
       	 }
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("CITEM")));
                    resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("CDESC")));
                    resultJsonInt.put("ITEMDETDESC", StrUtils.fString((String)m.get("CDETDESC")));
                    resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("UOM")));
                    resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("NONSTKFLAG", StrUtils.fString((String)m.get("NONSTKFLAG")));
                    if(StrUtils.fString((String)m.get("NONSTKFLAG")).equalsIgnoreCase("Y")) {
                    	String cqty = productionBomDAO.getchildbomqty(plant, pitem, (String)m.get("CITEM"), "KIT");
                    	Double dcqty = Double.valueOf(cqty) * Double.valueOf(pqty);
                    	 resultJsonInt.put("CQTY", StrUtils.addZeroes(dcqty, "3"));
                    }else {
                    	 resultJsonInt.put("CQTY", StrUtils.addZeroes(0, "3"));
                    }
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("items", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                /*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
                resultJson.put("errors", jsonArrayErr);
        	}
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private JSONObject getProductListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
        	String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
        	String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
        	String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
        	if(sItemDesc.length()>0){
	        	sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
	        	sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
        	}
        	sItemDesc =sItemDesc+sExtraCond ;
        	ArrayList listQry = itemUtil.getProductDetailsWithStockonHand(sItem,plant,sItemDesc);
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
                    resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
                    resultJsonInt.put("ITEMTYPE", StrUtils.fString((String)m.get("ITEMTYPE")));
                    resultJsonInt.put("BRAND", StrUtils.fString((String)m.get("PRD_BRAND_ID")));
                    resultJsonInt.put("VINNO", StrUtils.fString((String)m.get("VINNO")));
                    resultJsonInt.put("MODEL", StrUtils.fString((String)m.get("MODEL")));
                    resultJsonInt.put("UOM", StrUtils.fString((String)m.get("STKUOM")));
                    resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
                    resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
                    resultJsonInt.put("REMARK1", StrUtils.fString((String)m.get("REMARK1")));
                    resultJsonInt.put("REMARK2", StrUtils.fString((String)m.get("REMARK2")));
                    resultJsonInt.put("REMARK3",StrUtils.fString((String)m.get("REMARK3")));
                    resultJsonInt.put("REMARK4", StrUtils.fString((String)m.get("REMARK4")));
                    resultJsonInt.put("STKQTY", StrUtils.fString((String)m.get("STKQTY")));
                    resultJsonInt.put("ASSET", StrUtils.fString((String)m.get("ASSET")));
                    resultJsonInt.put("PRD_CLS_ID", StrUtils.fString((String)m.get("PRD_CLS_ID")));
                    resultJsonInt.put("ACTIVE", StrUtils.fString((String)m.get("ISACTIVE")));
                    resultJsonInt.put("PRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                    resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
                    resultJsonInt.put("DISCOUNT", StrUtils.fString((String)m.get("DISCOUNT")));
                    resultJsonInt.put("USERFLD1", StrUtils.fString((String)m.get("USERFLD1")));
                    resultJsonInt.put("NONSTKFLAG", StrUtils.fString((String)m.get("NONSTKFLAG")));
                    resultJsonInt.put("NONSTKTYPEID", StrUtils.fString((String)m.get("NONSTKTYPEID")));
                    resultJsonInt.put("PRD_DEPT_ID", StrUtils.fString((String)m.get("PRD_DEPT_ID")));
                    resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("ACCOUNT", "Inventory Asset");
                    resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                    Map invmap = null;
					String purchaseuomQty="",salesuomQty="";
					List uomQry = new UomDAO().getUomDetails(StrUtils.fString((String)m.get("PURCHASEUOM")), plant, "");
					if(uomQry.size()>0) {
						Map mp = (Map) uomQry.get(0);
						purchaseuomQty = (String)mp.get("QPUOM");
					}else {
						purchaseuomQty = "0";
					}
					double purchaseinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(purchaseuomQty);
					
					uomQry = new UomDAO().getUomDetails(StrUtils.fString((String)m.get("SALESUOM")), plant, "");
					if(uomQry.size()>0) {
						Map mp = (Map) uomQry.get(0);
						salesuomQty = (String)mp.get("QPUOM");
					}else {
						salesuomQty = "0";
					}
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(salesuomQty);
					
					resultJsonInt.put("PURCHASEINVQTY",StrUtils.addZeroes(purchaseinvqty, "3"));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
					resultJsonInt.put("PURCHASEUOMQTY",StrUtils.addZeroes(Double.parseDouble(purchaseuomQty), "3"));
                    String vendno = StrUtils.fString((String)m.get("VENDNO"));
					String vendname = "",TAXTREATMENT="",CURRENCY_ID="",CURRENCY="",Curramt="0";
					if(vendno.length()>0) {
					JSONObject vendorJson=new VendMstDAO().getVendorName(plant, (String) vendno);
					vendname = vendorJson.getString("VNAME");
					TAXTREATMENT = vendorJson.getString("TAXTREATMENT");
					CURRENCY_ID = vendorJson.getString("CURRENCY_ID");
					List curQryList=new ArrayList();
					curQryList = new CurrencyDAO().getCurrencyDetails(CURRENCY_ID,plant);
					for(int     j =0; j<curQryList.size(); j++) {
						ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
						CURRENCY = StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
				        Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
				        double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); 
				        Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
				   	    
				   			 
				        }
					}
					else
					{
						 List curQryList=new ArrayList();
							String Supcurrency=new PlantMstDAO().getBaseCurrency(plant);
							CURRENCY_ID=Supcurrency;
							List curQryList1=new ArrayList();
							curQryList1 = new CurrencyDAO().getCurrencyDetails(Supcurrency,plant);
							for(int     j =0; j<curQryList1.size(); j++) {
								ArrayList arrCurrLine = (ArrayList)curQryList1.get(j);
								CURRENCY = StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
						        Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
						        double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); 
						        Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
						   	    
						   			 
						        }
							
					}
					resultJsonInt.put("vendno",vendno);
					resultJsonInt.put("vendname",vendname);
					resultJsonInt.put("TAXTREATMENT",TAXTREATMENT);
					resultJsonInt.put("CURRENCY_ID",CURRENCY_ID);
					resultJsonInt.put("CURRENCY",CURRENCY);					
					resultJsonInt.put("CURRENCYUSEQT", Curramt);
                    String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("items", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                /*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
                resultJson.put("errors", jsonArrayErr);
        	}
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private JSONObject getProductListAutoSuggestion(HttpServletRequest request,String IsReport) {//By Azees-29.08.22 FOR Purchase
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			Hashtable ht=new Hashtable<>();
       	 	String extCond="";
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
			String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
			String CUSTNO = StrUtils.fString(request.getParameter("CUSTNO")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(sItemDesc.length()>0){
				sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
				sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
			}
			sItemDesc =sItemDesc+sExtraCond ;
			ArrayList listQry = new ArrayList();
			if(IsReport.length()>0)
			listQry = itemUtil.getProductDetailsAutoSuggestionReport(sItem,plant,sItemDesc);
			else
			listQry = itemUtil.getProductDetailsAutoSuggestion(sItem,plant,sItemDesc);
			if (listQry.size() > 0) {
				
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
					resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
					resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
					resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
					resultJsonInt.put("ACCOUNT", "Inventory Asset");
					resultJsonInt.put("MODEL",StrUtils.fString((String)m.get("MODEL")));
					//resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					resultJsonInt.put("prdesc",StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("nonStkFlag",StrUtils.fString((String)m.get("NONSTKFLAG")));
					
					double purchaseinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("PURCHASEUOMQTY")));
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("SALESUOMQTY")));
					
					resultJsonInt.put("PURCHASEINVQTY",StrUtils.addZeroes(purchaseinvqty, "3"));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
					resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
					
					String gtITEM= StrUtils.fString((String)m.get("ITEM"));
//					if(!CUSTNO.equalsIgnoreCase("") && !gtITEM.equalsIgnoreCase("") ) {
//	                    String query = "WITH TBL AS (" + 
//	                    		"SELECT Custcode as CUSTNO,CollectionDate as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] E ON A.DONO=E.DONO  WHERE A.PLANT='"+plant+"'" + 
//	                    		"UNION " + 
//	                    		"SELECT CUSTNO,INVOICE_DATE as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] E ON A.ID=E.INVOICEHDRID  WHERE A.PLANT='"+plant+"' )" + 
//	                    		"SELECT TOP 1 ISNULL(UNITPRICE,0) UNITPRICE FROM  TBL " + 
//	                    		"where CUSTNO='"+CUSTNO+"' and ITEM='"+ gtITEM +"' " + 
//	                    		"order by CRAT desc,CAST((SUBSTRING(ORD_DATE, 7, 4) + SUBSTRING(ORD_DATE, 4, 2) + SUBSTRING(ORD_DATE, 1, 2)) AS date) desc";
//	                    ArrayList al = itemUtil.selectForReport(query, ht, extCond);
//	                    if (al.size() > 0) {
//	                    	Map mp=(Map)al.get(0);
//	                    	resultJsonInt.put("UNITPRICE", StrUtils.fString((String)mp.get("UNITPRICE")));
//	                    } else {
//	                    resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
//	                    }
//	                    }
//	                    else {
	                        resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
	                        resultJsonInt.put("PRD_CLS_ID", StrUtils.fString((String)m.get("PRD_CLS_ID")));
//	                        }
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				/*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getMultiPoProductListAutoSuggestion(HttpServletRequest request,String IsReport) {//By Imti-05.12.24 FOR POmulti modal
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			Hashtable ht=new Hashtable<>();
			String extCond="";
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
			String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
			String CUSTNO = StrUtils.fString(request.getParameter("CUSTNO")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(sItemDesc.length()>0){
				sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
				sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
			}
			
			
//			ArrayList addcomp = new ArrayList();
//			Hashtable htData = new Hashtable();	
//			String childplant ="";
//			String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
//	      	ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
//	      	addcomp.add(plant);
//		      	if (arrList.size() > 0) {
//		      		for (int i=0; i < arrList.size(); i++ ) {
//		      			Map m = (Map) arrList.get(i);
//		      			childplant = (String) m.get("CHILD_PLANT");
//		      			addcomp.add(childplant);
//		      		  }
//		      	  }
//		      	StringBuilder queryExchangeBuilder = new StringBuilder();
//		      	StringBuilder querySalesPosBuilder = new StringBuilder();
//				StringBuilder querySalesBuilder = new StringBuilder();
//				StringBuilder querySalesReturnBuilder = new StringBuilder();
//				StringBuilder queryPurchaseBuilder = new StringBuilder();
//				StringBuilder queryPurchaseReturnBuilder = new StringBuilder();
//		      	
//				querySalesBuilder.append("WITH ProcessedOrders AS (");
//		      	for (int i = 0; i < addcomp.size(); i++) { plant = (String) addcomp.get(i);
//		      	    if (i > 0) {querySalesBuilder.append(" UNION ");}
//		      	    
//		      	  querySalesBuilder.append("SELECT B.PLANT, B.ITEM, SUM(B.QTYOR) AS QTYOR, ")
//	                .append("SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, ")
//	                .append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE, ")
//	                .append("SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ")
//	                .append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ")
//	                .append("A.ITEM_RATES, A.OUTBOUND_GST ")
//	                .append("FROM [").append(plant).append("_DOHDR] A ")
//	                .append("JOIN [").append(plant).append("_DODET] B ON A.DONO = B.DONO ")
//	                .append("JOIN [").append(plant).append("_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM = S.ITEM AND B.DOLNNO = S.DOLNO ")
//	                .append("WHERE S.STATUS='C' AND A.PLANT = '").append(plant).append("' AND S.LOC LIKE '%%' ")
//	                .append("AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2) >= '20241201' ")
//	                .append("GROUP BY B.PLANT, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST ");
//		      	}
//
//		      	querySalesBuilder.append("), ");
////		      	querySalesBuilder.append(") SELECT * FROM ProcessedOrders WHERE ITEM = '10004'");
//				String salesQuery = querySalesBuilder.toString();
//				
//				queryExchangeBuilder.append("ExchangeOrders AS (");
//				for (int i = 0; i < addcomp.size(); i++) {plant = (String) addcomp.get(i);
//					if (i > 0) {queryExchangeBuilder.append(" UNION ");}
//					
//					queryExchangeBuilder.append("SELECT PE.ITEM, SUM(ISNULL(I.COST, 0) * PE.QTY) AS EXCOST,SUM(ISNULL(PE.UNITPRICE, 0) * PE.QTY) AS EXPRICE  ")
//					.append("FROM [").append(plant).append("_POSEXCHANGEDET] PE ")
//					.append("JOIN [").append(plant).append("_ITEMMST] I ON I.ITEM = PE.ITEM ")
//					.append("JOIN [").append(plant).append("_DOHDR] A ON PE.EXDONO = A.DONO ")
//					.append("WHERE A.ORDER_STATUS = 'PROCESSED'  AND A.PLANT = '").append(plant).append("'")
//					.append("AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2) >= '20241201' ")
//					.append(" GROUP BY PE.ITEM ");
//				}
//				queryExchangeBuilder.append("), ");
////				queryExchangeBuilder.append(") SELECT * FROM ExchangeOrders WHERE ITEM = '10004'");
//				String ExchangeQuery = queryExchangeBuilder.toString();
//      	  
//				querySalesPosBuilder.append("SalesPOSOrders AS (");
//				for (int i = 0; i < addcomp.size(); i++) {
//					plant = (String) addcomp.get(i);
//					if (i > 0) {querySalesPosBuilder.append(" UNION ");}
//					
//					querySalesPosBuilder.append("SELECT A.DONO, B.ITEM, SUM(B.QTYOR) AS QTYOR FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO = B.DONO JOIN ["+plant+"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM AND ")
//					.append("B.DOLNNO=S.DOLNO WHERE S.STATUS='C'  AND A.PLANT = '"+plant+"' AND A.ORDERTYPE = 'POS'  AND S.LOC LIKE '%%' ")
//					.append("AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)  >= '20241101' ")
//					.append("GROUP BY A.DONO, B.ITEM ");
//				}
//				
//				querySalesPosBuilder.append("), ");
//				String SalesPosQuery = querySalesPosBuilder.toString();
//				
//				querySalesReturnBuilder.append("SoReturnorder AS (");
//				for (int i = 0; i < addcomp.size(); i++) {
//					plant = (String) addcomp.get(i);
//					if (i > 0) {
//						querySalesReturnBuilder.append(" UNION ");
//					}
//					
//					querySalesReturnBuilder.append("SELECT F.DONO, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR FROM ["+plant+"_FINSORETURN] F WHERE F.PLANT = '"+plant+"' ")
//					.append("AND SUBSTRING(F.RETURN_DATE, 7, 4) + SUBSTRING(F.RETURN_DATE, 4, 2) + SUBSTRING(F.RETURN_DATE, 1, 2)  >= '20241101' ")
//					.append("GROUP BY F.DONO, F.ITEM ");
//				}
//				
//				querySalesReturnBuilder.append("), ");
//				String SalesReturnQuery = querySalesReturnBuilder.toString();
//				
//				queryPurchaseBuilder.append("PurchaseOrders AS (");
//				for (int i = 0; i < addcomp.size(); i++) {plant = (String) addcomp.get(i);
//					if (i > 0) {queryPurchaseBuilder.append(" UNION ");}
//					
//					queryPurchaseBuilder.append("SELECT A.PONO, B.ITEM, SUM(B.QTYOR) AS QTYOR FROM ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO = B.PONO JOIN ["+plant+"_RECVDET] S ON B.PONO = S.PONO AND B.ITEM=S.ITEM ")
//					.append("AND B.POLNNO=S.LNNO WHERE A.STATUS='C'  AND A.PLANT = '"+plant+"' AND S.LOC LIKE '%%'  AND SUBSTRING(A.CollectionDate , 7, 4) + SUBSTRING(A.CollectionDate , 4, 2) +SUBSTRING(A.CollectionDate , 1, 2)  >= '20241101' ")
//					.append("GROUP BY A.PONO, B.ITEM ");
//				}
//				
//				queryPurchaseBuilder.append("), ");
//				String PurchaseQuery = queryPurchaseBuilder.toString();
//				
//				queryPurchaseReturnBuilder.append("PoReturnorder AS (");
//				for (int i = 0; i < addcomp.size(); i++) {plant = (String) addcomp.get(i);
//					if (i > 0) {queryPurchaseReturnBuilder.append(" UNION ");}
//					
//					queryPurchaseReturnBuilder.append("SELECT F.PONO, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR FROM ["+plant+"_FINPORETURN] F WHERE F.PLANT = '"+plant+"' ")
//					.append("AND SUBSTRING(F.RETURN_DATE, 7, 4) + SUBSTRING(F.RETURN_DATE, 4, 2) + SUBSTRING(F.RETURN_DATE, 1, 2)  >= '20241101' ")
//					.append("GROUP BY F.PONO, F.ITEM ");
//				}
//				
//				queryPurchaseReturnBuilder.append("), ");
//				String PurchaseReturnQuery = queryPurchaseReturnBuilder.toString();
//				
//				String finalquery = salesQuery+ExchangeQuery+SalesPosQuery+SalesReturnQuery+PurchaseQuery+PurchaseReturnQuery;
      	  
      	  
      	  
			sItemDesc =sItemDesc+sExtraCond ;
			ArrayList listQry = new ArrayList();
				listQry = itemUtil.getmultiPoProductDetailsAutoSuggestion(sItem,plant,sItemDesc);
			if (listQry.size() > 0) {
				
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
					resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("VNAME", StrUtils.fString((String)m.get("VNAME")));
					resultJsonInt.put("CURRENCY", StrUtils.fString((String)m.get("CURRENCY")));
					resultJsonInt.put("EQCUR", StrUtils.fString((String)m.get("EQCUR")));
					resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
					resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
					resultJsonInt.put("VENDNO", StrUtils.fString((String)m.get("VENDNO")));
					
					resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
					resultJsonInt.put("ACCOUNT", "Inventory Asset");
					double purchaseinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("PURCHASEUOMQTY")));
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("SALESUOMQTY")));
					resultJsonInt.put("PURCHASEINVQTY",StrUtils.addZeroes(purchaseinvqty, "3"));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
					
					String gtITEM= StrUtils.fString((String)m.get("ITEM"));
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				/*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getMultiQtyProductList(HttpServletRequest request,String IsReport) {//By Imti-05.12.24 FOR POmulti modal
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			Hashtable ht=new Hashtable<>();
			String extCond="";
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String fromdt = StrUtils.fString(request.getParameter("FDATE")).trim();
			String todt = StrUtils.fString(request.getParameter("TDATE")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			String fdate = "",tdate = "",Parentplant=plant;
			
			if(fromdt==null) fromdt=""; else fromdt = fromdt.trim();
			if (fromdt.length()>5)
				fdate    = fromdt.substring(6)+fromdt.substring(3,5)+fromdt.substring(0,2);
			
			if(todt==null) todt=""; else todt = todt.trim();
			if (todt.length()>5)
				tdate    = todt.substring(6)+todt.substring(3,5)+todt.substring(0,2);
			
			
			String bcondition = "";
			String bcondition1 = "";
			if (fdate.length() > 0) {
				bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)  >= '" + fdate
						+ "'  ";
				bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)  >= '" + fdate
						+ "'  ";
				if (tdate.length() > 0) {
					bcondition = bcondition + " AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2)   <= '" + tdate
							+ "'  ";
					bcondition1 = bcondition1 + " AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2)   <= '" + tdate
							+ "'  ";
				}
			} else {
				if (tdate.length() > 0) {
					bcondition =" AND SUBSTRING(issuedate, 7, 4) + SUBSTRING(issuedate, 4, 2) + SUBSTRING(issuedate, 1, 2) <= '" + tdate
							+ "'  ";
					bcondition1 =" AND SUBSTRING(A.DELDATE, 7, 4) + SUBSTRING(A.DELDATE, 4, 2) + SUBSTRING(A.DELDATE, 1, 2) <= '" + tdate
							+ "'  ";
				}
			}
			
			
			ArrayList addcomp = new ArrayList();
			Hashtable htData = new Hashtable();	
			String childplant ="";
			String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
			ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			addcomp.add(plant);
			if (arrList.size() > 0) {
				for (int i=0; i < arrList.size(); i++ ) {
					Map m = (Map) arrList.get(i);
					childplant = (String) m.get("CHILD_PLANT");
					addcomp.add(childplant);
				}
			}
			StringBuilder queryBuilder = new StringBuilder();
			String Query="",Datecond="";
			if(IsReport.equalsIgnoreCase("GET_MULTI_PRODUCT_QTY")) {
				
				queryBuilder.append("WITH Orders AS (");
				for (int i = 0; i < addcomp.size(); i++) { plant = (String) addcomp.get(i);
				if (i > 0) {queryBuilder.append(" UNION ");}
					queryBuilder.append("SELECT B.PLANT, B.ITEM, SUM(B.QTYOR) AS QTYOR, ")
					.append("SUM(B.UNITCOST * B.QTYOR) AS TOTAL_UNITCOST, ")
					.append("SUM(B.UNITPRICE * B.QTYOR) AS TOTAL_UNITPRICE, ")
					.append("SUM(CASE WHEN ISNULL(B.DISCOUNT_TYPE, '%') = '%' ")
					.append("THEN (B.UNITPRICE * B.QTYOR) * (B.DISCOUNT / 100) ELSE B.DISCOUNT END) AS TOTAL_DISCOUNT, ")
					.append("A.ITEM_RATES, A.OUTBOUND_GST ")
					.append("FROM [").append(plant).append("_DOHDR] A ")
					.append("JOIN [").append(plant).append("_DODET] B ON A.DONO = B.DONO ")
					.append("JOIN [").append(plant).append("_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM = S.ITEM AND B.DOLNNO = S.DOLNO ")
					.append("WHERE S.STATUS='C' AND A.PLANT = '").append(plant).append("' AND S.LOC LIKE '%%' ")
					.append(" "+bcondition1+" ")
					.append("GROUP BY B.PLANT, B.ITEM, A.ITEM_RATES, A.OUTBOUND_GST ");}
					
					queryBuilder.append(") SELECT * FROM Orders WHERE ITEM = '"+sItem+"'");
					Query = queryBuilder.toString();
				}else if(IsReport.equalsIgnoreCase("GET_SALESRETURN_QTY_DATA")) {
					Datecond= bcondition1.replace("A.DELDATE", "F.RETURN_DATE ");
					queryBuilder.append("WITH Orders AS (");
					for (int i = 0; i < addcomp.size(); i++) { plant = (String) addcomp.get(i);
					if (i > 0) {queryBuilder.append(" UNION ");}
					queryBuilder.append("SELECT F.PLANT, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR ")
					.append("FROM ["+plant+"_FINSORETURN] F WHERE F.PLANT = '"+plant+"'  ")
					.append(" "+Datecond+" ")
					.append("GROUP BY F.PLANT, F.ITEM ");}
					
					queryBuilder.append(") SELECT * FROM Orders WHERE ITEM = '"+sItem+"'");
					Query = queryBuilder.toString();
				}else if(IsReport.equalsIgnoreCase("GET_POS_QTY_DATA")) {
					queryBuilder.append("WITH Orders AS (");
					for (int i = 0; i < addcomp.size(); i++) { plant = (String) addcomp.get(i);
					if (i > 0) {queryBuilder.append(" UNION ");}
					queryBuilder.append("SELECT A.PLANT, B.ITEM, SUM(B.QTYOR) AS QTYOR FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO = B.DONO JOIN ["+plant+"_SHIPHIS] S ON B.DONO = S.DONO AND B.ITEM=S.ITEM ")
					.append("AND B.DOLNNO=S.DOLNO WHERE S.STATUS='C'  AND A.PLANT = '"+plant+"' AND A.ORDERTYPE = 'POS'  AND S.LOC LIKE '%%' ")
					.append(" "+bcondition1+" ")
					.append("GROUP BY A.PLANT, B.ITEM ");}
					
					queryBuilder.append(") SELECT * FROM Orders WHERE ITEM = '"+sItem+"'");
					Query = queryBuilder.toString();
				}else if(IsReport.equalsIgnoreCase("GET_PURCHASE_QTY_DATA")) {
					Datecond= bcondition1.replace("A.DELDATE", "A.CollectionDate ");
					queryBuilder.append("WITH Orders AS (");
					for (int i = 0; i < addcomp.size(); i++) { plant = (String) addcomp.get(i);
					if (i > 0) {queryBuilder.append(" UNION ");}
					queryBuilder.append("SELECT A.PLANT, B.ITEM, SUM(B.QTYOR) AS QTYOR FROM ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO = B.PONO JOIN ["+plant+"_RECVDET] S ON B.PONO = S.PONO AND B.ITEM=S.ITEM ")
					.append("AND B.POLNNO=S.LNNO WHERE A.STATUS='C'  AND A.PLANT = '"+plant+"' AND S.LOC LIKE '%%' ")
					.append(" "+Datecond+" ")
					.append("GROUP BY A.PLANT, B.ITEM ");}
					
					queryBuilder.append(") SELECT * FROM Orders WHERE ITEM = '"+sItem+"'");
					Query = queryBuilder.toString();
				}else if(IsReport.equalsIgnoreCase("GET_PURCHASERETURN_QTY_DATA")) {
					Datecond= bcondition1.replace("A.DELDATE", "F.RETURN_DATE ");
					queryBuilder.append("WITH Orders AS (");
					for (int i = 0; i < addcomp.size(); i++) { plant = (String) addcomp.get(i);
					if (i > 0) {queryBuilder.append(" UNION ");}
					queryBuilder.append("SELECT F.PLANT, F.ITEM, SUM(F.RETURN_QTY) AS QTYOR FROM ["+plant+"_FINPORETURN] F WHERE F.PLANT = '"+plant+"' ")
					.append(" "+Datecond+" ")
					.append("GROUP BY F.PLANT, F.ITEM ");}
					
					queryBuilder.append(") SELECT * FROM Orders WHERE ITEM = '"+sItem+"'");
					Query = queryBuilder.toString();
				}
			
			ArrayList salarrList = new ItemMstUtil().selectForReport(Query,htData,"");
			if (salarrList.size() > 0) {
				for (int i=0; i < salarrList.size(); i++ ) {
					Map m = (Map) salarrList.get(i);
					String PLANT = (String)m.get("PLANT");
					String item = (String)m.get("ITEM");
					String QTYOR = (String)m.get("QTYOR");
					JSONObject resultJsonInt = new JSONObject();
					ArrayList plntList1 = new PlantMstDAO().getPlantMstDetails(PLANT);
					Map plntMap1 = (Map) plntList1.get(0);
					String PLNTDESC = (String) plntMap1.get("PLNTDESC");
					if(PLANT.equalsIgnoreCase(Parentplant)) PLNTDESC = PLNTDESC+" -(Parent) ";
					else PLNTDESC = PLNTDESC+" -(Child)";
					resultJsonInt.put("PLANT", PLNTDESC);
					resultJsonInt.put("ITEM", item);
					resultJsonInt.put("QTY", QTYOR);			  
					jsonArray.add(resultJsonInt);
				}
				 resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
                resultJson.put("orders", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getProductListSuggestion(HttpServletRequest request) {//By Azees-29.08.22 FOR Purchase
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			Hashtable ht=new Hashtable<>();
       	 	String extCond="";
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
			String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
			String CUSTNO = StrUtils.fString(request.getParameter("CUSTNO")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(sItemDesc.length()>0){
				sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
				sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
			}
			sItemDesc =sItemDesc+sExtraCond ;
			ArrayList listQry = itemUtil.getProductDetailSuggestion(sItem,plant,sItemDesc);
			if (listQry.size() > 0) {
				
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
					resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
					resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
					resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
					resultJsonInt.put("PRD_BRAND_ID", StrUtils.fString((String)m.get("PRD_BRAND_ID")));
					resultJsonInt.put("PRD_CLS_ID", StrUtils.fString((String)m.get("PRD_CLS_ID")));
					resultJsonInt.put("ACCOUNT", "Inventory Asset");
					//resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					resultJsonInt.put("prdesc",StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("nonStkFlag",StrUtils.fString((String)m.get("NONSTKFLAG")));
					resultJsonInt.put("MODEL",StrUtils.fString((String)m.get("MODEL")));
					double purchaseinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("PURCHASEUOMQTY")));
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("SALESUOMQTY")));
					
					resultJsonInt.put("PURCHASEINVQTY",StrUtils.addZeroes(purchaseinvqty, "3"));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
					resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
					
					String gtITEM= StrUtils.fString((String)m.get("ITEM"));
	                        resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}

	private JSONObject getMultiPurchaseProductListAutoSuggestion(HttpServletRequest request) {//By Azees-30.08.22 FOR Multi Purchase
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
			String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(sItemDesc.length()>0){
				sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
				sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
			}
			sItemDesc =sItemDesc+sExtraCond ;
			ArrayList listQry = itemUtil.getMultiPurchaseProductDetailsAutoSuggestion(sItem,plant,sItemDesc);
			if (listQry.size() > 0) {
				
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
					resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
					resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
					resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
					resultJsonInt.put("ACCOUNT", "Inventory Asset");
					resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					
					double purchaseinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("PURCHASEUOMQTY")));
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("SALESUOMQTY")));
					
					resultJsonInt.put("PURCHASEINVQTY",StrUtils.addZeroes(purchaseinvqty, "3"));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
					resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
					
					resultJsonInt.put("vendno",StrUtils.fString((String)m.get("VENDNO")));
					resultJsonInt.put("vendname",StrUtils.fString((String)m.get("VNAME")));
					resultJsonInt.put("TAXTREATMENT",StrUtils.fString((String)m.get("TAXTREATMENT")));
					resultJsonInt.put("CURRENCY_ID",StrUtils.fString((String)m.get("CURRENCY_ID")));
					resultJsonInt.put("CURRENCY",StrUtils.fString((String)m.get("CURRENCY_DISPLAY")));					
					resultJsonInt.put("CURRENCYUSEQT",StrUtils.addZeroes( Double.parseDouble(StrUtils.fString((String)m.get("CURRENCYUSEQT"))) , numberOfDecimal));
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				/*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getProductListAutoSuggestionforitem(HttpServletRequest request) {//By Azees-29.08.22 FOR Purchase
		System.out.print("start time  "+ new DateUtils().getDateAtTime());
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		JSONObject resultJsonInt = new JSONObject();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			Hashtable ht=new Hashtable<>();
       	 	String extCond="";
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
			String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
			String CUSTNO = StrUtils.fString(request.getParameter("CUSTNO")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(sItemDesc.length()>0){
				sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
				sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
			}
			sItemDesc =sItemDesc+sExtraCond ;
			ArrayList listQry = itemUtil.getProductDetailsAutoSuggestion(sItem,plant,sItemDesc);
			if (listQry.size() > 0) {
				
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					
					resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
					resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
					resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
					resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
					resultJsonInt.put("LOC_ID", StrUtils.fString((String)m.get("LOC_ID")));
					resultJsonInt.put("HSCODE", StrUtils.fString((String)m.get("HSCODE")));
					resultJsonInt.put("ACCOUNT", "Inventory Asset");
					//resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					resultJsonInt.put("prdesc",StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("nonStkFlag",StrUtils.fString((String)m.get("NONSTKFLAG")));
					resultJsonInt.put("MODEL",StrUtils.fString((String)m.get("MODEL")));
					resultJsonInt.put("DIMENSION",StrUtils.fString((String)m.get("DIMENSION")));
					resultJsonInt.put("ISINVOICE",StrUtils.fString((String)m.get("ISINVOICE")));
       			  	
       			  	
       			  	String QPUOM= StrUtils.fString((String)m.get("PURCHASEUOMQTY"));
       			  	resultJsonInt.put("QPUOM",QPUOM);
       			 	
					double purchaseinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("PURCHASEUOMQTY")));
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("SALESUOMQTY")));
					
					resultJsonInt.put("PURCHASEINVQTY",StrUtils.addZeroes(purchaseinvqty, "3"));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
					resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
					
					String gtITEM= StrUtils.fString((String)m.get("ITEM"));
                    resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					resultJsonInt.put("PRD_CLS_ID", StrUtils.fString((String)m.get("PRD_CLS_ID")));
				}
				resultJson.put("items", resultJsonInt);
				JSONObject resultJsonIntE = new JSONObject();
				resultJsonIntE.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonIntE.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonIntE);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonIntE = new JSONObject();
				resultJsonIntE.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonIntE.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonIntE);
				/*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonIntE = new JSONObject();
			resultJsonIntE.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonIntE.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonIntE);
			resultJson.put("ERROR", jsonArrayErr);
		}
		
		System.out.print("end time  "+ new DateUtils().getDateAtTime());
		return resultJson;
	}
	
	private JSONObject getProductListAutoSuggestionLow(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			Hashtable ht=new Hashtable<>();
       	 	String extCond="";
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
			String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
			String CUSTNO = StrUtils.fString(request.getParameter("CUSTNO")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(sItemDesc.length()>0){
				sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
				sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
			}
			sItemDesc =sItemDesc+sExtraCond ;
			ArrayList listQry = itemUtil.getProductDetailsAutoSuggestionLow(sItem,plant,sItemDesc);
			if (listQry.size() > 0) {
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
					resultJsonInt.put("AITEM", StrUtils.fString((String)m.get("AITEM")));
					resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("SALESUOMQTY")));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
				
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				/*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getEquivalentitem(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
        	String sPitem = StrUtils.fString(request.getParameter("PITEM")).trim();
        	String eitem="",edesc="",edetdesc="";
        	ProductionBomDAO ProdBomDao = new ProductionBomDAO();
      	  ItemMstDAO itemMstDAO = new ItemMstDAO(); 
      	  eitem = ProdBomDao.getEquivalentitem(plant,sPitem,sItem);
          if(eitem.length()>0)
          {
          	 edesc = itemMstDAO.getItemDesc(plant, eitem);
          	edetdesc = itemMstDAO.getItemDetailDesc(plant,eitem);
          	
          	resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
          	resultJson.put("ERROR_CODE", "100");
          	resultJson.put("eitem", eitem);
          	resultJson.put("edesc", edesc);
          	resultJson.put("edetdesc", edetdesc);
                
                
        	} else {
                
                resultJson.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJson.put("ERROR_CODE", "99");
        	}
        } catch (Exception e) {
        	resultJson.put("ERROR_MESSAGE",  e.getMessage());
        	resultJson.put("ERROR_CODE", "98");
        }
	    return resultJson;
	}
	
	private JSONObject getinvoiceProductListAutoSuggestion(HttpServletRequest request) {//By imthi-31.08.22 FOR SALES
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			Hashtable ht=new Hashtable<>();
       	 	String extCond="";
			ItemMstUtil itemUtil = new ItemMstUtil();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
			String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
			String CUSTNO = StrUtils.fString(request.getParameter("CUSTNO")).trim();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(sItemDesc.length()>0){
				sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
				sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
			}
			sItemDesc =sItemDesc+sExtraCond ;
			ArrayList listQry = itemUtil.getProductDetailsAutoSuggestion(sItem,plant,sItemDesc);
			if (listQry.size() > 0) {
				
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					String gtITEM= StrUtils.fString((String)m.get("ITEM"));
					resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
					resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
					resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
					resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
					resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
					resultJsonInt.put("ACCOUNT", "Local sales - retail");
					
					
//                    if(!CUSTNO.equalsIgnoreCase("") && !gtITEM.equalsIgnoreCase("") ) {
//                    String query = "WITH TBL AS (" + 
//                    		"SELECT Custcode as CUSTNO,CollectionDate as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] E ON A.DONO=E.DONO  WHERE A.PLANT='"+plant+"'" + 
//                    		"UNION " + 
//                    		"SELECT CUSTNO,INVOICE_DATE as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] E ON A.ID=E.INVOICEHDRID  WHERE A.PLANT='"+plant+"' )" + 
//                    		"SELECT TOP 1 ISNULL(UNITPRICE,0) UNITPRICE FROM  TBL " + 
//                    		"where CUSTNO='"+CUSTNO+"' and ITEM='"+ gtITEM +"' " + 
//                    		"order by CRAT desc,CAST((SUBSTRING(ORD_DATE, 7, 4) + SUBSTRING(ORD_DATE, 4, 2) + SUBSTRING(ORD_DATE, 1, 2)) AS date) desc";
//                    ArrayList al = itemUtil.selectForReport(query, ht, extCond);
//                    if (al.size() > 0) {
//                    	Map mp=(Map)al.get(0);
//                    	resultJsonInt.put("PRICE", StrUtils.fString((String)mp.get("UNITPRICE")));   
//                    	resultJsonInt.put("UNITPRICE", StrUtils.fString((String)mp.get("UNITPRICE")));
//                    } else {
//                    resultJsonInt.put("PRICE", StrUtils.fString((String)m.get("UNITPRICE")));
//                    resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
//                    }
//                    }
//                    else {
                        resultJsonInt.put("PRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                        resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
//                        }
                    
//					resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
					
					double purchaseinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("PURCHASEUOMQTY")));
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(StrUtils.fString((String)m.get("SALESUOMQTY")));
					
					resultJsonInt.put("PURCHASEINVQTY",StrUtils.addZeroes(purchaseinvqty, "3"));
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));					
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
					resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				/*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getInvoiceProductListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
        	String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
        	String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
        	String CUSTNO = StrUtils.fString(request.getParameter("CUSTNO")).trim();
        	if(sItemDesc.length()>0){
	        	sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
	        	sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
        	}
        	sItemDesc =sItemDesc+sExtraCond ;
        	 Hashtable ht=new Hashtable<>();
        	 String extCond="";
        	ArrayList listQry = itemUtil.getProductDetailsWithStockonHand(sItem,plant,sItemDesc);
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			String gtITEM= StrUtils.fString((String)m.get("ITEM"));
        			resultJsonInt.put("ITEM", gtITEM);
                    resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
                    resultJsonInt.put("ITEMTYPE", StrUtils.fString((String)m.get("ITEMTYPE")));
                    resultJsonInt.put("BRAND", StrUtils.fString((String)m.get("PRD_BRAND_ID")));
                    resultJsonInt.put("VINNO", StrUtils.fString((String)m.get("VINNO")));
                    resultJsonInt.put("MODEL", StrUtils.fString((String)m.get("MODEL")));
                    resultJsonInt.put("UOM", StrUtils.fString((String)m.get("STKUOM")));
                    resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
                    resultJsonInt.put("PURCHASEUOM", StrUtils.fString((String)m.get("PURCHASEUOM")));
                    resultJsonInt.put("REMARK1", StrUtils.fString((String)m.get("REMARK1")));
                    resultJsonInt.put("REMARK2", StrUtils.fString((String)m.get("REMARK2")));
                    resultJsonInt.put("REMARK3",StrUtils.fString((String)m.get("REMARK3")));
                    resultJsonInt.put("REMARK4", StrUtils.fString((String)m.get("REMARK4")));
                    resultJsonInt.put("STKQTY", StrUtils.fString((String)m.get("STKQTY")));
                    resultJsonInt.put("ASSET", StrUtils.fString((String)m.get("ASSET")));
                    resultJsonInt.put("PRD_CLS_ID", StrUtils.fString((String)m.get("PRD_CLS_ID")));
                    resultJsonInt.put("ACTIVE", StrUtils.fString((String)m.get("ISACTIVE")));
                    Map invmap = null;
					String salesuomQty="";
					List uomQry = new UomDAO().getUomDetails(StrUtils.fString((String)m.get("SALESUOM")), plant, "");
					if(uomQry.size()>0) {
						Map mp = (Map) uomQry.get(0);
						salesuomQty = (String)mp.get("QPUOM");
					}else {
						salesuomQty = "0";
					}
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(salesuomQty);
					
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));
					
                    if(!CUSTNO.equalsIgnoreCase("") && !gtITEM.equalsIgnoreCase("") ) {
                    String query = "WITH TBL AS (" + 
                    		"SELECT Custcode as CUSTNO,CollectionDate as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] E ON A.DONO=E.DONO  WHERE A.PLANT='"+plant+"'" + 
                    		"UNION " + 
                    		"SELECT CUSTNO,INVOICE_DATE as ORD_DATE,A.CRAT,ITEM,UNITPRICE FROM ["+plant+"_FININVOICEHDR] A JOIN ["+plant+"_FININVOICEDET] E ON A.ID=E.INVOICEHDRID  WHERE A.PLANT='"+plant+"' )" + 
                    		"SELECT TOP 1 ISNULL(UNITPRICE,0) UNITPRICE FROM  TBL " + 
                    		"where CUSTNO='"+CUSTNO+"' and ITEM='"+ gtITEM +"' " + 
                    		"order by CRAT desc,CAST((SUBSTRING(ORD_DATE, 7, 4) + SUBSTRING(ORD_DATE, 4, 2) + SUBSTRING(ORD_DATE, 1, 2)) AS date) desc";
                    ArrayList al = itemUtil.selectForReport(query, ht, extCond);
                    if (al.size() > 0) {
                    	Map mp=(Map)al.get(0);
                    	resultJsonInt.put("PRICE", StrUtils.fString((String)mp.get("UNITPRICE")));   
                    	resultJsonInt.put("UNITPRICE", StrUtils.fString((String)mp.get("UNITPRICE")));
                    } else {
                    resultJsonInt.put("PRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                    resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                    }
                    }
                    else {
                        resultJsonInt.put("PRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                        resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                        }
                    
                    resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
                    resultJsonInt.put("DISCOUNT", StrUtils.fString((String)m.get("DISCOUNT")));
                    resultJsonInt.put("USERFLD1", StrUtils.fString((String)m.get("USERFLD1")));
                    resultJsonInt.put("NONSTKFLAG", StrUtils.fString((String)m.get("NONSTKFLAG")));
                    resultJsonInt.put("NONSTKTYPEID", StrUtils.fString((String)m.get("NONSTKTYPEID")));
                    //resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("ACCOUNT", "Inventory Asset");
                    
                    String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("items", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                /*jsonArray.add("");
                resultJson.put("items", jsonArray);*/
                resultJson.put("errors", jsonArrayErr);
        	}
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private JSONObject getProductClassListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getProductClassDetailsWithStockonHand(QUERY,plant," ORDER BY ITEMDESC asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("PRD_CLS_ID", StrUtils.fString((String)m.get("PRD_CLS_ID")));
                    resultJsonInt.put("PRD_CLS_DESC", StrUtils.fString((String)m.get("PRD_CLS_DESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("PRD_CLASS_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);            
        	}
        	 //resultJson.put("PRD_CLASS_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	private JSONObject getProductTypeListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getProductTypeDetailsWithStockonHand(QUERY,plant," ORDER BY PRD_TYPE_DESC asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("PRD_TYPE_ID", StrUtils.fString((String)m.get("PRD_TYPE_ID")));
                    resultJsonInt.put("PRD_TYPE_DESC", StrUtils.fString((String)m.get("PRD_TYPE_DESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("PRD_TYPE_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);            
        	}
        	 //resultJson.put("PRD_TYPE_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	private JSONObject getProductBrandListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getProductBrandDetailsWithStockonHand(QUERY,plant," ORDER BY PRD_BRAND_DESC asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("PRD_BRAND_ID", StrUtils.fString((String)m.get("PRD_BRAND_ID")));
                    resultJsonInt.put("PRD_BRAND_DESC", StrUtils.fString((String)m.get("PRD_BRAND_DESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("PRD_BRAND_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);            
        	}
        	 //resultJson.put("PRD_BRAND_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	private JSONObject getGRNOForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getGRNO(QUERY,plant," ORDER BY GRNO asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("GRNO", StrUtils.fString((String)m.get("GRNO")));
                    resultJsonInt.put("CNAME", StrUtils.fString((String)m.get("CNAME")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("GRNO_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);        
        	}
        	 resultJson.put("GRNO_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	private JSONObject getLOCForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getLOC(QUERY,plant," ORDER BY LOC asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("LOC", StrUtils.fString((String)m.get("LOC")));
                    resultJsonInt.put("LOCDESC", StrUtils.fString((String)m.get("LOCDESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("LOC_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);            
        	}
        	 //resultJson.put("LOC_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	private JSONObject getTRANIDForSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			POSDetDAO posdao = new POSDetDAO();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
			ArrayList listQry = posdao.getDistinctTranId(plant,"","MOVEWITHBATCH");
			if (listQry.size() > 0) {
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TRAN", StrUtils.fString((String)m.get("POSTRANID")));
					resultJsonInt.put("NOPRD", StrUtils.fString((String)m.get("NOOFITEMS")));                    
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("TRANS", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);            
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	private JSONObject getTRANIDStockeMoveForSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			POSDetDAO posdao = new POSDetDAO();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
			ArrayList listQry = posdao.getDistinctStokeMoveTranId(plant,QUERY,"MOVEWITHBATCH");
			if (listQry.size() > 0) {
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TRAN", StrUtils.fString((String)m.get("POSTRANID")));
					resultJsonInt.put("NOPRD", StrUtils.fString((String)m.get("NOOFITEMS")));                    
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("TRANS", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);            
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getPO_Receipt_pono_ForSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			POUtil itemUtil = new POUtil();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
			String pono = StrUtils.fString(request.getParameter("QUERY")).trim();
			String ProductOrder = StrUtils.fString(request.getParameter("OpenForReceipt")).trim();
				   
			     Hashtable ht=new Hashtable();
			     String extCond="";
			     ht.put("PLANT",plant);
			     if(pono.length()>0) extCond=" AND plant='"+plant+"' and pono like '"+pono+"%' ";
			     if("YES".equalsIgnoreCase(ProductOrder)) extCond += " and (ORDER_STATUS ='OPEN' OR ORDER_STATUS = 'PARTIALLY PROCESSED')";
			     extCond=extCond+" and status <> 'C' "; 
			     extCond=extCond+" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) desc";
			     ArrayList listQry = itemUtil.getPoHdrDetails("pono,custName,custcode,jobNum,status,collectiondate",ht,extCond);
			     if (listQry.size() > 0) {
			     for(int i =0; i<listQry.size(); i++) {
			      Map m=(Map)listQry.get(i);
			      pono     = (String)m.get("pono");
			      String custName    = StrUtils.replaceCharacters2Send((String)m.get("custName"));
			      String custcode    = (String)m.get("custcode");
			      String orderdate      =  (String)m.get("collectiondate");
			      String jobNum    = (String)m.get("jobNum");
			      String status      =  (String)m.get("status");
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PONO", StrUtils.fString((String)m.get("pono")));
					resultJsonInt.put("CUST", StrUtils.fString((String)m.get("custName")));                    
					resultJsonInt.put("DATE", StrUtils.fString((String)m.get("collectiondate")));                    
					jsonArray.add(resultJsonInt);
			      resultJson.put("PORECEIPT", jsonArray);
			     }
			     JSONObject resultJsonInt = new JSONObject();
			     resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			     resultJsonInt.put("ERROR_CODE", "100");
			     jsonArrayErr.add(resultJsonInt);
			     resultJson.put("errors", jsonArrayErr);
			
			
			} else {
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);            
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	private JSONObject getLOCTYPEForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getLOCTYPE(QUERY,plant," ORDER BY LOC_TYPE_ID asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("LOC_TYPE_ID", StrUtils.fString((String)m.get("LOC_TYPE_ID")));
                    resultJsonInt.put("LOC_TYPE_DESC", StrUtils.fString((String)m.get("LOC_TYPE_DESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("LOCTYPE_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);            
        	}
        	 //resultJson.put("LOCTYPE_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	//imti start loc2
	private JSONObject getLOCTYPETWOForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getLOCTYPETWO(QUERY,plant," ORDER BY LOC_TYPE_ID2 asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("LOC_TYPE_ID2", StrUtils.fString((String)m.get("LOC_TYPE_ID2")));
                    resultJsonInt.put("LOC_TYPE_DESC2", StrUtils.fString((String)m.get("LOC_TYPE_DESC2")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("LOCTYPE_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);            
        	}
        	 //resultJson.put("LOCTYPE_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	//imti start loc3
	private JSONObject getLOCTYPETHREEForSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
			
			
			
			
			ArrayList listQry = itemUtil.getLOCTYPETHREE(QUERY,plant," ORDER BY LOC_TYPE_ID3 asc");
			if (listQry.size() > 0) {
				
				for(int i =0; i<listQry.size(); i++) {
					Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("LOC_TYPE_ID3", StrUtils.fString((String)m.get("LOC_TYPE_ID3")));
					resultJsonInt.put("LOC_TYPE_DESC3", StrUtils.fString((String)m.get("LOC_TYPE_DESC3")));                    
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("LOCTYPE_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
			} else {
				
				JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);           
			}
			//resultJson.put("LOCTYPE_MST", jsonArray);
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	//imti end loc3
	//imti end loc2
	private JSONObject getRSNcodeForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getRSNCODE(QUERY,plant," ORDER BY RSNCODE asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("RSNCODE", StrUtils.fString((String)m.get("RSNCODE")));
                    resultJsonInt.put("RSNDESC", StrUtils.fString((String)m.get("RSNDESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("RSNCODE_MST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);    
        	}
//        	 resultJson.put("RSNCODE_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	private JSONObject getInvoiceNoForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getINVOICENO(QUERY,plant," ORDER BY INVOICENO asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("INVOICENO", StrUtils.fString((String)m.get("INVOICENO")));
                    resultJsonInt.put("CNAME", StrUtils.fString((String)m.get("CNAME")));                    
                    jsonArray.add(resultJsonInt);
        		}
	        		resultJson.put("INVOICENO_MST", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
        	} else {
        		 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 resultJson.put("errors", jsonArrayErr);              
        	}
//        	 resultJson.put("INVOICENO_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	private JSONObject getCURRENCYForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	
        	
        	
        
        	ArrayList listQry = itemUtil.getCURRENCY(QUERY,plant," ORDER BY CURRENCYID asc");
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("CURRENCYID", StrUtils.fString((String)m.get("CURRENCYID")));
                    resultJsonInt.put("DESCRIPTION", StrUtils.fString((String)m.get("DESCRIPTION")));                    
                    jsonArray.add(resultJsonInt);
        		}
        	} else {
        		
             	   JSONObject resultJsonInt = new JSONObject();
                    jsonArray.add("");
                    resultJson.put("footermaster", jsonArray);            
        	}
        	 resultJson.put("CURRENCY_MST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private JSONObject getSalesProductListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
        	String sItemDesc = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
        	String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
        	if(sItemDesc.length()>0){
	        	sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
	        	sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
        	}
        	sItemDesc =sItemDesc+sExtraCond ;
        	ArrayList listQry = itemUtil.getProductDetailsWithStockonHand(sItem,plant,sItemDesc);
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("ITEM", StrUtils.fString((String)m.get("ITEM")));
                    resultJsonInt.put("ITEMDESC", StrUtils.fString((String)m.get("ITEMDESC")));
                    resultJsonInt.put("ITEMTYPE", StrUtils.fString((String)m.get("ITEMTYPE")));
                    resultJsonInt.put("BRAND", StrUtils.fString((String)m.get("PRD_BRAND_ID")));
                    resultJsonInt.put("VINNO", StrUtils.fString((String)m.get("VINNO")));
                    resultJsonInt.put("MODEL", StrUtils.fString((String)m.get("MODEL")));
                    resultJsonInt.put("UOM", StrUtils.fString((String)m.get("STKUOM")));
                    resultJsonInt.put("REMARK1", StrUtils.fString((String)m.get("REMARK1")));
                    resultJsonInt.put("REMARK2", StrUtils.fString((String)m.get("REMARK2")));
                    resultJsonInt.put("REMARK3",StrUtils.fString((String)m.get("REMARK3")));
                    resultJsonInt.put("REMARK4", StrUtils.fString((String)m.get("REMARK4")));
                    resultJsonInt.put("STKQTY", StrUtils.fString((String)m.get("STKQTY")));
                    resultJsonInt.put("ASSET", StrUtils.fString((String)m.get("ASSET")));
                    resultJsonInt.put("PRD_CLS_ID", StrUtils.fString((String)m.get("PRD_CLS_ID")));
                    resultJsonInt.put("ACTIVE", StrUtils.fString((String)m.get("ISACTIVE")));
                    resultJsonInt.put("PRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                    resultJsonInt.put("COST", StrUtils.fString((String)m.get("COST")));
                    resultJsonInt.put("DISCOUNT", StrUtils.fString((String)m.get("DISCOUNT")));
                    resultJsonInt.put("USERFLD1", StrUtils.fString((String)m.get("USERFLD1")));
                    resultJsonInt.put("NONSTKFLAG", StrUtils.fString((String)m.get("NONSTKFLAG")));
                    resultJsonInt.put("NONSTKTYPEID", StrUtils.fString((String)m.get("NONSTKTYPEID")));
                    resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("ACCOUNT", "Sales");
                    resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
                    resultJsonInt.put("SALESUOM", StrUtils.fString((String)m.get("SALESUOM")));
                    
                    String salesuomQty="";
                   List uomQry = new UomDAO().getUomDetails(StrUtils.fString((String)m.get("SALESUOM")), plant, "");
					if(uomQry.size()>0) {
						Map mp = (Map) uomQry.get(0);
						salesuomQty = (String)mp.get("QPUOM");
					}else {
						salesuomQty = "0";
					}
					double salesinvqty=Double.parseDouble(StrUtils.fString((String)m.get("INVQTY")))/Double.parseDouble(salesuomQty);
					
					resultJsonInt.put("SALESINVQTY",StrUtils.addZeroes(salesinvqty, "3"));
					
                    String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("items", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
        	}
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	public JSONObject addProduct(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		if(isMultipart) {
			UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
			DateUtils dateutils = new DateUtils();
			HttpSession session = request.getSession();
			ItemUtil itemUtil = new ItemUtil();
			StrUtils strUtils = new StrUtils();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			int imgCount = 0;
			 String result = "",strpath = "" , catlogpath = "",catlogpaths = "";
   		  ItemUtil _itemutil = new ItemUtil();
   		  CatalogUtil _catalogUtil = new CatalogUtil();
   		  TblControlDAO _TblControlDAO =new TblControlDAO();
   		String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(plant);  
   		  String userName = StrUtils.fString(
   					(String) request.getSession().getAttribute("LOGIN_USER"))
   					.trim();
   		  boolean prdflag = false , flag = false;
   		  boolean imageSizeflg = false;
 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ plant;
 			String filetempLocation = DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
			String ITEM = "" , ITEM1 = "" ,DESC = "", NONSTOCKFLAG = "",REMARKS = "",NONSTKTYPE = "",UOM = "",ISPOSDISCOUNT="",ISNEWARRIVAL="",ISTOPSELLING="",ISBASICUOM ="",
					PRD_CLS_ID = "",LOC_ID="",PRD_DEPT_ID="",HSCODE = "" ,ARTIST = "",COO = "",PRD_BRAND = "",VINNO = "" ,taxbylabelordermanagement ="",
					PRODGST ="",MODEL = "",ITEM_CONDITION = "", TITLE ="",IMAGE_UPLOAD = "",PURCHASEUOM = "",COST = "",
					DYNAMIC_SUPPLIERDISCOUNT_SIZE = "",IBDISCOUNT = "" ,DYNAMIC_SUPPLIER_DISCOUNT_0 = "",SUPPLIER_0 = "",
					DYNAMIC_SUPPLIER_DISCOUNT_SIZE = "" , SALESUOM = "" , NETWEIGHT = "" , PRICE = "",GROSSWEIGHT = "",MINSELLINGPRICE = "",
					DISCOUNT = "",DYNAMIC_CUSTOMERDISCOUNT_SIZE = "",OBDISCOUNT = "",DYNAMIC_CUSTOMER_DISCOUNT_0 = "",CUSTOMER_TYPE_ID_0 = "",
					DYNAMIC_CUSTOMER_DISCOUNT_SIZE = "", RENTALUOM = "",RENTALPRICE = "",SERVICEUOM = "",SERVICEPRICE = "",
					INVENTORYUOM = "",STKQTY = "",MAXSTKQTY = "",LOC_0="",iscompro="",cppi="",incprice="",incpriceunit="",vendno="",vendname="",DIMENSION="",DYNAMIC_ITEMSUPPLIER_SIZE="";		
			
			List DYNAMIC_CUSTOMER_DISCOUNT = new ArrayList(), CUSTOMER_TYPE_ID = new ArrayList(),SUPPLIER = new ArrayList(),DYNAMIC_SUPPLIER_DISCOUNT = new ArrayList(),ITEMSUPPLIER = new ArrayList();
			int DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT=0,DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT=0,DYNAMIC_CUSTOMER_DISCOUNTCount  = 0, CUSTOMER_TYPE_IDCount  = 0, SUPPLIERCount  = 0, DYNAMIC_SUPPLIER_DISCOUNTCount  = 0,DYNAMIC_ITEMSUPPLIER_SIZE_INT=0,ITEMSUPPLIERCount=0;
			List  ADITIONALPRD = new ArrayList(),DETAILDESC = new ArrayList();
			int PRODUCT_Count  = 0, DESC_Count  = 0;
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);//Check Parent Plant or child plant
			boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
			Hashtable htData = new Hashtable();	
			
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			
			String field = "";
			System.out.println("product details list :::::: "+items.size());
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {

					if (item.getFieldName()
							.equalsIgnoreCase("ITEM1")) {
						ITEM1 = StrUtils.fString(item.getString());
					}
					if (item.getFieldName()
							.equalsIgnoreCase("ITEM")) {
						ITEM = StrUtils.fString(item.getString());
					}
					if (item.getFieldName()
							.equalsIgnoreCase("DESC")) {
						DESC = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("NONSTOCKFLAG")) {
						NONSTOCKFLAG = StrUtils.fString(item.getString());
						
					}
					if (item.getFieldName().equalsIgnoreCase("REMARKS")) {
						REMARKS = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("NONSTKTYPE")) {
						NONSTKTYPE = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("UOM")) {
						UOM = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ISBASICUOM")) {
						ISBASICUOM = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ISPOSDISCOUNT")) {
						ISPOSDISCOUNT =StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ISNEWARRIVAL")) {
						ISNEWARRIVAL = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ISTOPSELLING")) {
						ISTOPSELLING = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("PRD_CLS_ID")) {
						PRD_CLS_ID = StrUtils.fString(item.getString());
					}
					
					if (item.getFieldName().equalsIgnoreCase("LOC_ID")) {
						LOC_ID = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("PRD_DEPT_ID")) {
						PRD_DEPT_ID = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("HSCODE")) {
						HSCODE = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("ARTIST")) {
						ARTIST = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("COO")) {
						COO = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("PRD_BRAND")) {
						PRD_BRAND =StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("VINNO")) {
						VINNO = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("taxbylabelordermanagement")) {
						taxbylabelordermanagement=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("PRODGST")) {
						PRODGST = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("MODEL")) {
						MODEL =StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("ITEM_CONDITION")) {
						ITEM_CONDITION = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("TITLE")) {
						TITLE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("IMAGE_UPLOAD")) {
						IMAGE_UPLOAD =StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("PURCHASEUOM")) {
						PURCHASEUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("COST")) {
						COST = StrUtils.fString(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIERDISCOUNT_SIZE")) {
						DYNAMIC_SUPPLIERDISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_ITEMSUPPLIER_SIZE")) {
						DYNAMIC_ITEMSUPPLIER_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("IBDISCOUNT")) {
						IBDISCOUNT =StrUtils.fString(item.getString());
					}
//					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIER_DISCOUNT_0")) {
//						DYNAMIC_SUPPLIER_DISCOUNT_0 =StrUtils.fString(item.getString()).trim();
//					}
//					if (item.getFieldName().equalsIgnoreCase("SUPPLIER_0")) {
//						SUPPLIER_0 = StrUtils.fString(item.getString());
//					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIER_DISCOUNT_SIZE")) {
						DYNAMIC_SUPPLIER_DISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("SALESUOM")) {
						SALESUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("NETWEIGHT")) {
						NETWEIGHT=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("PRICE")) {
						PRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("GROSSWEIGHT")) {
						GROSSWEIGHT=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DIMENSION")) {
						DIMENSION=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("MINSELLINGPRICE")) {
						MINSELLINGPRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DISCOUNT")) {
						DISCOUNT=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMERDISCOUNT_SIZE")) {
						DYNAMIC_CUSTOMERDISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("OBDISCOUNT")) {
						OBDISCOUNT=StrUtils.fString(item.getString()).trim();
					}
//					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMER_DISCOUNT_0")) {
//						DYNAMIC_CUSTOMER_DISCOUNT_0=StrUtils.fString(item.getString()).trim();
//					}
//					if (item.getFieldName().equalsIgnoreCase("CUSTOMER_TYPE_ID_0")) {
//						CUSTOMER_TYPE_ID_0=StrUtils.fString(item.getString()).trim();
//					}
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMER_DISCOUNT_SIZE")) {
						DYNAMIC_CUSTOMER_DISCOUNT_SIZE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("RENTALUOM")) {
						RENTALUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("RENTALPRICE")) {
						RENTALPRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("SERVICEUOM")) {
						SERVICEUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("SERVICEPRICE")) {
						SERVICEPRICE=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("INVENTORYUOM")) {
						INVENTORYUOM=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("STKQTY")) {
						STKQTY=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("MAXSTKQTY")) {
						MAXSTKQTY=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("LOC_0")) {
						LOC_0=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("ISCOMPRO")) {
						iscompro=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("CPPI")) {
						cppi=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("INCPRICE")) {
						incprice=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName().equalsIgnoreCase("INCPRICEUNIT")) {
						incpriceunit=StrUtils.fString(item.getString()).trim();
					}
					if (item.getFieldName()
							.equalsIgnoreCase("vendno")) {
						vendno = StrUtils.fString(item.getString()).trim();
					}					
					
					if(ITEM.length() <= 0)
						ITEM = ITEM1;
					
					//detail desc
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("DESCRIPTION")) {
							DETAILDESC.add(DESC_Count, StrUtils.fString(item.getString()).trim());
							DESC_Count++;
						}
					}
					
					//additional product
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("PRODUCT")) {
							ADITIONALPRD.add(PRODUCT_Count, StrUtils.fString(item.getString()).trim());
							PRODUCT_Count++;
						}
					}
				}
				if (!item.isFormField()
						&& (item.getName().length() > 0)) {
					String fileName = item.getName();
					imgCount++;
					long size = item.getSize();
					
					String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
					System.out.println("Extensions:::::::" + extension);
					if (!imageFormatsList.contains(extension)) {
						result = "<font color=\"red\"> Image extension not valid </font>";
						imageSizeflg = true;
					}
					
//					size = size / 1024;
					// size = size / 1000;
					System.out.println("size of the Image imported :::"
							+ size);
					//checking image size for 2MB
//					if (size > 2040) // condtn checking Image size
					if (size > 250000) 
					{
						result = "<font color=\"red\">  Catalog Image size greater than 250 Pixel </font>";
//						result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";

						imageSizeflg = true;

					}
					File path = new File(fileLocation);
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					fileName = fileName.substring(fileName
							.lastIndexOf("\\") + 1);
					File uploadedFile = new File("");
					if(imgCount==1) {
					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
					}else {
						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
					}

					// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
					// if (uploadedFile.exists()) {
					// uploadedFile.delete();
					// }
					strpath = path + "/" + fileName;
					if(imgCount==1) 
					catlogpath = uploadedFile.getAbsolutePath();
					
					if (!imageSizeflg && !uploadedFile.exists())
						item.write(uploadedFile);

					// delete temp uploaded file
					File tempPath = new File(filetempLocation);
					if (tempPath.exists()) {
						if(imgCount==1) {
						File tempUploadedfile = new File(tempPath + "/"
								+ strUtils.RemoveSlash(ITEM)+".JPEG");
						if (tempUploadedfile.exists()) {
							tempUploadedfile.delete();
						}
						}else {
							File tempUploadedfile = new File(tempPath + "/"
									+ strUtils.RemoveSlash(ITEM) +"_"+imgCount+ ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
					}
					Hashtable htCatalog = new Hashtable();
					if(imgCount>1) { 
					catlogpaths = uploadedFile.getAbsolutePath();
					
					int length = catlogpaths.length();
					int len = length-4;
					String new_word = catlogpaths.substring(len - 2);
					char firstChar = new_word.charAt(0);
					String usertime  = Character.toString(firstChar);
					
					htCatalog.put(IDBConstants.PRODUCTID, ITEM);
					htCatalog.put(IConstants.PLANT, plant);
					htCatalog.put(IConstants.CREATED_BY, userName);
					htCatalog.put(IConstants.ISACTIVE, "Y");
					htCatalog.put(IDBConstants.CATLOGPATH, catlogpaths);
					htCatalog.put("USERTIME1", usertime);
					flag = _catalogUtil.insertMst(htCatalog);
					}
					
					//IMAGE START
					  if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
				        			  path = new File(fileLocations);
					  					if (!path.exists()) {
					  						boolean status = path.mkdirs();
					  					}
					  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					  					
					  					if(imgCount==1) {
					  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
					  					}else {
					  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
					  					}
					  					
//					  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
					  					strpath = path + "/" + fileName;
					  					String childcatlogpath = uploadedFile.getAbsolutePath();
					  					File childpath = new File(childcatlogpath);
					  					File parentpath = new File("");
					  					if(imgCount==1) {
					  						parentpath = new File(catlogpath);
					  					}else {
					  						parentpath = new File(catlogpaths);
					  					}
					  					if (!imageSizeflg && !uploadedFile.exists())
					  					FileUtils.copyFile(parentpath, childpath);
					  					
					  					htCatalog.put(IConstants.PLANT, childplant);
					  					htCatalog.put(IDBConstants.CATLOGPATH, childcatlogpath);
										flag = _catalogUtil.insertMst(htCatalog);
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int i=0; i < arrLists.size(); i++ ) {
					        			  Map ms = (Map) arrLists.get(i);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
					        			  String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
					        			  path = new File(fileLocations);
						  					if (!path.exists()) {
						  						boolean status = path.mkdirs();
						  					}
						  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						  					
						  					if(imgCount==1) {
						  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
						  					}else {
						  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
						  					}
						  					
//						  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
						  					strpath = path + "/" + fileName;
						  					String childcatlogpath = uploadedFile.getAbsolutePath();
						  					File childpath = new File(childcatlogpath);
//						  					File parentpath = new File(catlogpath);
						  					File parentpath = new File("");
						  					if(imgCount==1) {
						  						parentpath = new File(catlogpath);
						  					}else {
						  						parentpath = new File(catlogpaths);
						  					}
						  					if (!imageSizeflg && !uploadedFile.exists())
						  					FileUtils.copyFile(parentpath, childpath);
						  					
						  					htCatalog.put(IConstants.PLANT, parentplant);
						  					htCatalog.put(IDBConstants.CATLOGPATH, childcatlogpath);
											flag = _catalogUtil.insertMst(htCatalog);
		
					        		  }
					        	  }
					        	  
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
						        			  String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
						        			  path = new File(fileLocations);
							  					if (!path.exists()) {
							  						boolean status = path.mkdirs();
							  					}
							  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							  					
							  					if(imgCount==1) {
							  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+".JPEG");
							  					}else {
							  						uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+"_"+imgCount+ ".JPEG");
							  					}
							  					
//							  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
							  					strpath = path + "/" + fileName;
							  					String childcatlogpath = uploadedFile.getAbsolutePath();
							  					File childpath = new File(childcatlogpath);
//							  					File parentpath = new File(catlogpath);
							  					File parentpath = new File("");
							  					if(imgCount==1) {
							  						parentpath = new File(catlogpath);
							  					}else {
							  						parentpath = new File(catlogpaths);
							  					}
							  					if (!imageSizeflg && !uploadedFile.exists())
							  					FileUtils.copyFile(parentpath, childpath);
							  					
							  					htCatalog.put(IConstants.PLANT, childplant);
							  					htCatalog.put(IDBConstants.CATLOGPATH, childcatlogpath);
												flag = _catalogUtil.insertMst(htCatalog);
					        			  }
					        		  	}
					        	  	}
				        	  }	
		             	  }
		             	}
					  
					  //IMAGE END

				}
				
				//GET CUSTOMER DISCOUNT
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_CUSTOMER_DISCOUNT")) {
						DYNAMIC_CUSTOMER_DISCOUNT.add(DYNAMIC_CUSTOMER_DISCOUNTCount, StrUtils.fString(item.getString()).trim());
						DYNAMIC_CUSTOMER_DISCOUNTCount++;
					}
				}
				
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("CUSTOMER_TYPE_ID")) {
						CUSTOMER_TYPE_ID.add(CUSTOMER_TYPE_IDCount, StrUtils.fString(item.getString()).trim());
						CUSTOMER_TYPE_IDCount++;
					}
				}
				
				//GET SUPPLIER DISCOUNT
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("DYNAMIC_SUPPLIER_DISCOUNT")) {
						DYNAMIC_SUPPLIER_DISCOUNT.add(DYNAMIC_SUPPLIER_DISCOUNTCount, StrUtils.fString(item.getString()).trim());
						DYNAMIC_SUPPLIER_DISCOUNTCount++;
					}
				}
				
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("SUPPLIER")) {
						SUPPLIER.add(SUPPLIERCount, StrUtils.fString(item.getString()).trim());
						SUPPLIERCount++;
					}
				}
				
				if(DYNAMIC_ITEMSUPPLIER_SIZE!="")
				{
					DYNAMIC_ITEMSUPPLIER_SIZE_INT = (new Integer(DYNAMIC_ITEMSUPPLIER_SIZE)).intValue();
					for(int nameCount = 0; nameCount<=DYNAMIC_ITEMSUPPLIER_SIZE_INT;nameCount++){
						if (item.isFormField()) {
							if (item.getFieldName().equalsIgnoreCase("ITEMSUPPLIER_"+nameCount)) {
								ITEMSUPPLIER.add(SUPPLIERCount, StrUtils.fString(item.getString()).trim());
								ITEMSUPPLIERCount++;
							}
						}
					}
				}
				
			}
				
				String loc = "";
				if (imageSizeflg) {
					resultJson.put("STATUS", "FAIL");		        	  
				}else {
					ut.begin();
			    if(!(itemUtil.isExistsItemMst(ITEM,plant))) // if the item exists already
			    {
	                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
	                        if(ITEM.equals("")){
	                            throw new Exception("");
	                    }
//	                        if(specialcharsnotAllowed.length()>0){
//	                                throw new Exception("Product ID  value : '" + ITEM + "' has special characters "+specialcharsnotAllowed+" that are  not allowed ");
//	                        }
	                        
	                        if(loc.length()>0){
	                        boolean isExistLoc = false;
	    					LocUtil locUtil = new LocUtil();
	    					isExistLoc = locUtil.isValidLocInLocmst(plant, loc);
	    					if(!isExistLoc){
	    						throw new Exception("Location: " + loc + " is not valid location");
	    						}
	                        }
	                        DESC=strUtils.InsertQuotes(DESC);
			          Hashtable ht = new Hashtable();
			          ht.put(IConstants.PLANT,plant);
			          ht.put(IConstants.ITEM,ITEM);
			          ht.put(IConstants.ITEM_DESC,DESC);
			          ht.put(IConstants.ITEMMST_REMARK1,strUtils.InsertQuotes(REMARKS));
			          ht.put(IConstants.ITEMMST_ITEM_TYPE,ARTIST);//itemtype
			       // Start code added by Deen for product brand on 11/9/12
			          ht.put(IConstants.PRDBRANDID ,PRD_BRAND);
			       // End code added by Deen for product brand on 11/9/12
			          ht.put("STKUOM",UOM);
			          ht.put(IConstants.ITEMMST_REMARK4,strUtils.InsertQuotes(TITLE));//remark4
			          //ht.put(IConstants.ITEMMST_REMARK2,strUtils.InsertQuotes(sMedium)); // remark2
			          ht.put(IConstants.ITEMMST_REMARK3,strUtils.InsertQuotes(ITEM_CONDITION));//remark3
			          ht.put(IConstants.PRDCLSID,PRD_CLS_ID);//prd_cls_id
			          ht.put("LOC_ID",LOC_ID);
			          ht.put(IConstants.PRDDEPTID,PRD_DEPT_ID);//prd_cls_id
			          ht.put(IConstants.PRICE,PRICE);
			          ht.put(IConstants.ISACTIVE,"Y");
			          ht.put(IConstants.COST,COST);
			          ht.put(IConstants.MIN_S_PRICE,MINSELLINGPRICE); 
			          ht.put(IConstants.DISCOUNT, DISCOUNT);
			          ht.put(IConstants.PRODGST, PRODGST);
			          ht.put(IConstants.NONSTKFLAG, NONSTOCKFLAG);
			          ht.put(IConstants.NONSTKTYPEID, NONSTKTYPE);
			          ht.put(IConstants.DISCOUNT, DISCOUNT);
			          ht.put(IDBConstants.VENDOR_CODE,vendno);
			          ht.put("ITEM_LOC", loc);
			          ht.put("NETWEIGHT",NETWEIGHT);
				      ht.put("GROSSWEIGHT",GROSSWEIGHT);
				      ht.put("DIMENSION",DIMENSION);
				      ht.put("HSCODE",HSCODE);
				      ht.put("COO",COO);
			          // Start code added by deen for createddate,createdby on 15/july/2013
			          ht.put("CRBY",username);
			          ht.put("CRAT",dateutils.getDateTime());
			          ht.put("VINNO",VINNO);
				      ht.put("MODEL",MODEL);
					  ht.put("RENTALPRICE",RENTALPRICE);
				      ht.put("SERVICEPRICE",SERVICEPRICE);
				      ht.put("PURCHASEUOM",PURCHASEUOM);
				      ht.put("SALESUOM",SALESUOM);
				      ht.put("RENTALUOM",RENTALUOM);
				      ht.put("SERVICEUOM",SERVICEUOM);
				      ht.put("INVENTORYUOM",INVENTORYUOM);
				      ht.put("ISBASICUOM",ISBASICUOM);
				      ht.put("ISPOSDISCOUNT",ISPOSDISCOUNT);
				      ht.put("ISNEWARRIVAL",ISNEWARRIVAL);
				      ht.put("ISTOPSELLING",ISTOPSELLING);
				      if(iscompro.equalsIgnoreCase("NONE"))
				  		iscompro="0";
				  	  else if(iscompro.equalsIgnoreCase("ISCOMPRO"))
				  		iscompro="1";
				      else
				    	 iscompro="2";
				      ht.put("ISCOMPRO",iscompro);
				      ht.put("CPPI",cppi);
				      ht.put("INCPRICE",incprice);
				      ht.put("INCPRICEUNIT",incpriceunit);
			          
			         
			          ht.put("USERFLD1", "N");
			          if(STKQTY=="")
			        	  STKQTY ="0";
			          ht.put(IDBConstants.STKQTY, STKQTY);//stkqty
			          if(MAXSTKQTY=="")
			        	  MAXSTKQTY ="0";
			          ht.put(IDBConstants.MAXSTKQTY, MAXSTKQTY);
			          String remark = REMARKS+" "+ITEM_CONDITION+" "+TITLE;
			          MovHisDAO mdao = new MovHisDAO(plant);
			          mdao.setmLogger(mLogger);
			          Hashtable htm = new Hashtable();
			          htm.put("PLANT",plant);
			          htm.put("DIRTYPE",TransactionConstants.ADD_ITEM);
			          htm.put("RECID","");
			          htm.put("ITEM",ITEM);
			          htm.put("LOC",loc);
			          htm.put(IDBConstants.REMARKS,strUtils.InsertQuotes(remark));  
			          htm.put("CRBY",username);
			          htm.put("CRAT",dateutils.getDateTime());
			          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
			          boolean  inserted=false; 
			          boolean  insertAlternateItem = false;
			          catlogpath = catlogpath.replace('\\', '/');
						ht.put(IDBConstants.CATLOGPATH, catlogpath);
			          boolean itemInserted = itemUtil.insertItem(ht);
			          
			          
			          String posquery="select PLANT,OUTLET from "+plant+"_POSOUTLETS WHERE PLANT ='"+plant+"'";
			          ArrayList posList = new ItemMstUtil().selectForReport(posquery,htData,"");
			          Map pos = new HashMap();
			          if (posList.size() > 0) {
			        	  for (int j=0; j < posList.size(); j++ ) {
			        		  pos = (Map) posList.get(j);
			        		  String outlet = (String) pos.get("OUTLET");
			        		  String posplant = (String) pos.get("PLANT");
			       	       	Hashtable htCondition = new Hashtable();
			       	       		htCondition.put(IConstants.ITEM,ITEM);
		      	        		htCondition.put(IConstants.PLANT,plant);
		      	        		htCondition.put("OUTLET",outlet);
			        	    Hashtable hPos = new Hashtable();	
			        		  hPos.put(IConstants.PLANT,plant);
			        		  hPos.put("OUTLET",outlet);
			        		  hPos.put(IConstants.ITEM,ITEM);
			        		  hPos.put("SALESUOM",SALESUOM);
			        		  hPos.put(IConstants.PRICE,PRICE);
			        		  hPos.put("CRBY",username);
			        		  hPos.put("CRAT",dateutils.getDateTime());
			        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,plant))) {
			        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
			        		  }else {
			        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
			        		  }
			        	  }
			          }
			          
			          pos = new HashMap();
			          if (posList.size() > 0) {
			        	  for (int j=0; j < posList.size(); j++ ) {
			        		  pos = (Map) posList.get(j);
			        		  String outlet = (String) pos.get("OUTLET");
			        		  String posplant = (String) pos.get("PLANT");
			       	       	Hashtable htCondition = new Hashtable();
			       	       		htCondition.put(IConstants.ITEM,ITEM);
		      	        		htCondition.put(IConstants.PLANT,plant);
		      	        		htCondition.put("OUTLET",outlet);
			        	    Hashtable hPoss = new Hashtable();	
			        		  hPoss.put(IConstants.PLANT,plant);
			        		  hPoss.put("OUTLET",outlet);
			        		  hPoss.put(IConstants.ITEM,ITEM);
			        		  hPoss.put("MINQTY",STKQTY);
			        		  hPoss.put("MAXQTY",MAXSTKQTY);
			        		  hPoss.put("CRBY",username);
			        		  hPoss.put("CRAT",dateutils.getDateTime());
			        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,plant))) {
			        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
			        		  }else {
			        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
			        		  }
			        	  }
			          }
			          
			          //imthi start ADD PRODUCT to Child based on plantmaster
			          
			          if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  String childplant="";
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  m = (Map) arrList.get(i);
			        			  childplant = (String) m.get("CHILD_PLANT");
			        			  ht.put(IConstants.PLANT,childplant);
			        			  if(!(itemUtil.isExistsItemMst(ITEM,childplant))) {
			        				  String catpath = catlogpath;
				        			  catpath = catpath.replace(plant,childplant);
			      						ht.put(IDBConstants.CATLOGPATH, catpath);
			        				  boolean childitemInserted = itemUtil.insertItem(ht);
						          }
			        			  
			        			  //PRD BRAND START
			        			  	Hashtable prdBrand = new Hashtable();
			        			  	Hashtable htBrandtype = new Hashtable();
			        				htBrandtype.clear();
			        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
			        				htBrandtype.put(IDBConstants.PRDBRANDID, PRD_BRAND);
			        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
			        				if(PRD_BRAND.length()>0) {
			        				if (flag == false) {
			        					ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(PRD_BRAND,plant);
				        				if (arrCust.size() > 0) {
				        				String brandId = (String) arrCust.get(0);
				        				String brandDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			        					prdBrand.clear();
			        					prdBrand.put(IDBConstants.PLANT, childplant);
			        					prdBrand.put(IDBConstants.PRDBRANDID, brandId);
			        					prdBrand.put(IDBConstants.PRDBRANDDESC, brandDesc);
			        					prdBrand.put(IConstants.ISACTIVE, isactive);
			        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        					prdBrand.put(IDBConstants.LOGIN_USER, username);
			        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
				        				}
			        				}
			        				}
			        			  //PRD BRAND END
			        				
			        			  //PRD CLASS START
			        				Hashtable htprdcls = new Hashtable();
			        			  	Hashtable htclass = new Hashtable();
			        			  	htprdcls.clear();
			        			  	htprdcls.put(IDBConstants.PLANT, childplant);
			        				htprdcls.put(IDBConstants.PRDCLSID,PRD_CLS_ID);
			      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
			      					if(PRD_CLS_ID.length()>0) {
			      					if (flag == false) {
			      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(PRD_CLS_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String classId = (String) arrCust.get(0);
				        				String classDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			      						htclass.put(IDBConstants.PLANT, childplant);
			      						htclass.put(IDBConstants.PRDCLSID, classId);
			      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
			      						htclass.put(IConstants.ISACTIVE, isactive);
			      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			      						htclass.put(IDBConstants.LOGIN_USER, username);
			      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
				        				}
			      					}
			      					}
			      				  //PRD CLASS END
			      					
			      				  //PRD TYPE START	
			      					Hashtable htprdtype = new Hashtable();
			        			  	Hashtable htprdtp = new Hashtable();
			        			  	htprdtype.clear();
			      					htprdtype.put(IDBConstants.PLANT, childplant);
			      					htprdtype.put("PRD_TYPE_ID",ARTIST);
								    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
								    if(ARTIST.length()>0) {
								    if (flag == false) {
								    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(ARTIST,plant);
				        				if (arrCust.size() > 0) {
				        				String typeId = (String) arrCust.get(0);
				        				String typedesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
								    	htprdtp.clear();
								    	htprdtp.put(IDBConstants.PLANT, childplant);
								    	htprdtp.put("PRD_TYPE_ID", typeId);
								    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedesc);
								    	htprdtp.put(IConstants.ISACTIVE, isactive);
								    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								    	htprdtp.put(IDBConstants.LOGIN_USER, username);
		               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
				        				}
		                               }
								    }
			      					//PRD TYPE END
								    
								    //PRD DEPT START
								    Hashtable htprddept = new Hashtable();
			        			  	Hashtable htdept = new Hashtable();
			        			  	htprddept.put(IDBConstants.PLANT, childplant);
			        			  	htprddept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
			        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
			        			  	if(PRD_DEPT_ID.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(PRD_DEPT_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String deptId = (String) arrCust.get(0);
				        				String deptdesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);	
										htdept.put(IDBConstants.PLANT, childplant);
										htdept.put(IDBConstants.PRDDEPTID, deptId);
										htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
										htdept.put(IConstants.ISACTIVE, isactive);
										htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										htdept.put(IDBConstants.LOGIN_USER, username);
										boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
				        				}
									}
			        			  	}
								   //PRD DEPT END
			        			  	
			        			  //check if Purchase UOM exists 
			        			  	Hashtable htInv = new Hashtable();
			        			  	Hashtable HtPurchaseuom = new Hashtable();
			        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
			        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
			        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
			        			  	if(PURCHASEUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END PURCHASE UOM
			        			  	
			        			  //check if Sales UOM exists 
			        			  	Hashtable HtSalesuom = new Hashtable();
			        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
			        			  	HtSalesuom.put("UOM", SALESUOM);
			        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
			        			  	if(SALESUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END SALES UOM
			        			  	
								   //check if Inventory UOM exists 
			        			  	Hashtable HtInvuom = new Hashtable();
			        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
			        			  	HtInvuom.put("UOM", INVENTORYUOM);
			        			  	flag = new UomUtil().isExistsUom(HtInvuom);
			        			  	if(INVENTORYUOM.length()>0) {
			        			  	if (flag == false) {
			          			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
							    	  htInv.clear();
							    	  htInv.put(IDBConstants.PLANT,childplant);
							    	  htInv.put("UOM", uom);
							    	  htInv.put("UOMDESC", uomdesc);
							    	  htInv.put("Display",display);
							    	  htInv.put("QPUOM", qpuom);
							    	  htInv.put(IConstants.ISACTIVE, isactive);
							    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	  htInv.put(IDBConstants.LOGIN_USER,username);
							    	  boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END INV UOM
			        			  	
			        			  	//check if Stk UOM exists 
			        			  	Hashtable HtStkuom = new Hashtable();
			        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
			        			  	HtStkuom.put("UOM", UOM);
			        			  	flag = new UomUtil().isExistsUom(HtStkuom);
			        			  	if(UOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(UOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT,childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display",display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER,username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END STOCK UOM
			        			  	
			        			  //HSCODE
			        			  	if(HSCODE.length()>0) {
			        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, childplant)) 
									{						
						    			Hashtable htHS = new Hashtable();
						    			htHS.put(IDBConstants.PLANT,childplant);
						    			htHS.put(IDBConstants.HSCODE,HSCODE);
						    			htHS.put(IDBConstants.LOGIN_USER,username);
						    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddHSCODE(htHS);
										boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",HSCODE);
										htRecvHis.put(IDBConstants.CREATED_BY, username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        			  	//HSCODE END
			        			  	
			        			  	//COO 
			        			  	if(COO.length()>0) {
			        				if (!new MasterUtil().isExistCOO(COO, childplant)) 
									{						
						    			Hashtable htCoo = new Hashtable();
						    			htCoo.put(IDBConstants.PLANT,childplant);
						    			htCoo.put(IDBConstants.COO,COO);
						    			htCoo.put(IDBConstants.LOGIN_USER,username);
						    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddCOO(htCoo);
										boolean insertflag = new MasterDAO().InsertCOO(htCoo);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",COO);
										htRecvHis.put(IDBConstants.CREATED_BY,username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        				//COO END
			        				
			        				//SUPPLIER START 
			        			  	if(vendno.length()>0) {
			        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
			        				if (arrCust.size() > 0) {
			        				String sCustCode = (String) arrCust.get(0);
			        				String sCustName = (String) arrCust.get(1);
			        				if (!new CustUtil().isExistVendor(vendno, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
			        				{
			        					String sAddr1 = (String) arrCust.get(2);
			        					String sAddr2 = (String) arrCust.get(3);
			        					String sAddr3 = (String) arrCust.get(4);
			        					String sAddr4 = (String) arrCust.get(15);
			        					String sCountry = (String) arrCust.get(5);
			        					String sZip = (String) arrCust.get(6);
			        					String sCons = (String) arrCust.get(7);
			        					String sContactName = (String) arrCust.get(8);
			        					String sDesgination = (String) arrCust.get(9);
			        					String sTelNo = (String) arrCust.get(10);
			        					String sHpNo = (String) arrCust.get(11);
			        					String sEmail = (String) arrCust.get(12);
			        					String sFax = (String) arrCust.get(13);
			        					String sRemarks = (String) arrCust.get(14);
			        					String isActive = (String) arrCust.get(16);
			        					String sPayTerms = (String) arrCust.get(17);
			        					String sPayMentTerms = (String) arrCust.get(39);
			        					String sPayInDays = (String) arrCust.get(18);
			        					String sState = (String) arrCust.get(19);
			        					String sRcbno = (String) arrCust.get(20);
			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
			        					String WEBSITE = (String) arrCust.get(23);
			        					String FACEBOOK = (String) arrCust.get(24);
			        					String TWITTER = (String) arrCust.get(25);
			        					String LINKEDIN = (String) arrCust.get(26);
			        					String SKYPE = (String) arrCust.get(27);
			        					String OPENINGBALANCE = (String) arrCust.get(28);
			        					String WORKPHONE = (String) arrCust.get(29);
			        					String sTAXTREATMENT = (String) arrCust.get(30);
			        					String sCountryCode = (String) arrCust.get(31);
			        					String sBANKNAME = (String) arrCust.get(32);
			        					String sBRANCH= (String) arrCust.get(33);
			        					String sIBAN = (String) arrCust.get(34);
			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
			        					String companyregnumber = (String) arrCust.get(36);
			        					String PEPPOL = (String) arrCust.get(40);
			        					String PEPPOL_ID = (String) arrCust.get(41);
			        					String CURRENCY = (String) arrCust.get(37);
//			        					String transport = (String) arrCust.get(38);
//			        					String suppliertypeid = (String) arrCust.get(21);
			        					Hashtable htsup = new Hashtable();
			        					htsup.put(IDBConstants.PLANT,childplant);
			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
			        					htsup.put(IConstants.companyregnumber,companyregnumber);
			        					htsup.put("ISPEPPOL", PEPPOL);
			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
			        					htsup.put("CURRENCY_ID", CURRENCY);
			        					htsup.put(IConstants.NAME, sContactName);
			        					htsup.put(IConstants.DESGINATION, sDesgination);
			        					htsup.put(IConstants.TELNO, sTelNo);
			        					htsup.put(IConstants.HPNO, sHpNo);
			        					htsup.put(IConstants.FAX, sFax);
			        					htsup.put(IConstants.EMAIL, sEmail);
			        					htsup.put(IConstants.ADDRESS1, sAddr1);
			        					htsup.put(IConstants.ADDRESS2, sAddr2);
			        					htsup.put(IConstants.ADDRESS3, sAddr3);
			        					htsup.put(IConstants.ADDRESS4, sAddr4);
			        					if(sState.equalsIgnoreCase("Select State"))
			        						sState="";
			        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			        					htsup.put(IConstants.ZIP, sZip);
			        					htsup.put(IConstants.USERFLG1, sCons);
			        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//			        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//			        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//			        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
			        					htsup.put(IConstants.PAYTERMS, "");
			        					htsup.put(IConstants.payment_terms, "");
			        					htsup.put(IConstants.PAYINDAYS, "");
			        					htsup.put(IConstants.ISACTIVE, isActive);
//			        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//			        					htsup.put(IConstants.TRANSPORTID,transport);
			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
			        					htsup.put(IConstants.TRANSPORTID, "0");
			        					htsup.put(IConstants.RCBNO, sRcbno);
			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        					htsup.put(IConstants.WEBSITE,WEBSITE);
			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
			        					htsup.put(IConstants.TWITTER,TWITTER);
			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
			        					htsup.put(IConstants.SKYPE,SKYPE);
			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        			        	  sBANKNAME="";
//			        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
			        			          htsup.put(IDBConstants.BANKNAME,"");
			        			          htsup.put(IDBConstants.IBAN,sIBAN);
			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			        			          htsup.put("CRAT",new DateUtils().getDateTime());
			        			          htsup.put("CRBY",username);
			        			          htsup.put("Comment1", " 0 ");
			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			        				}
			        				}
			        			  	}
			        				//Supplier END
			        				
						          posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
						          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
						       	       		htCondition.put(IConstants.PLANT,childplant);
						       	       		htCondition.put("OUTLET",outlet);
						       	        Hashtable hPos = new Hashtable();	
						        		  hPos.put(IConstants.PLANT,childplant);
						        		  hPos.put("OUTLET",outlet);
						        		  hPos.put(IConstants.ITEM,ITEM);
						        		  hPos.put("SALESUOM",SALESUOM);
						        		  hPos.put(IConstants.PRICE,PRICE);
						        		  hPos.put("CRBY",username);
						        		  hPos.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
						        		  }
						        	  }
						          }
						          
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
					      	        		htCondition.put(IConstants.PLANT,childplant);
					      	        		htCondition.put("OUTLET",outlet);
						        	    Hashtable hPoss = new Hashtable();	
						        		  hPoss.put(IConstants.PLANT,childplant);
						        		  hPoss.put("OUTLET",outlet);
						        		  hPoss.put(IConstants.ITEM,ITEM);
						        		  hPoss.put("MINQTY",STKQTY);
						        		  hPoss.put("MAXQTY",MAXSTKQTY);
						        		  hPoss.put("CRBY",username);
						        		  hPoss.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
						        		  }
						        	  }
						          }
			        		  }
			        	  }
			        	}
			        	  
			          }else if(PARENT_PLANT == null){
			        	  boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = "";
			        	  parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        	  
			        	  
			        	  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String parentplant = "";
			        	  Map ms = new HashMap();
			        	  if (arrLists.size() > 0) {
			        		  for (int i=0; i < arrLists.size(); i++ ) {
			        			  ms = (Map) arrLists.get(i);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  ht.put(IConstants.PLANT,parentplant);
			        			  if(!(itemUtil.isExistsItemMst(ITEM,parentplant))) {
			        				  String catpath = catlogpath;
				        			  catpath = catpath.replace(plant,parentplant);
			      						ht.put(IDBConstants.CATLOGPATH, catpath);
			        				  boolean childitemInserted = itemUtil.insertItem(ht);
			        			  }
			        			  //PRD BRAND START
			        			  	Hashtable prdBrand = new Hashtable();
			        			  	Hashtable htBrandtype = new Hashtable();
			        				htBrandtype.clear();
			        			  	htBrandtype.put(IDBConstants.PLANT, parentplant);
			        				htBrandtype.put(IDBConstants.PRDBRANDID, PRD_BRAND);
			        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
			        				if(PRD_BRAND.length()>0) {
			        				if (flag == false) {
			        					ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(PRD_BRAND,plant);
				        				if (arrCust.size() > 0) {
				        				String brandId = (String) arrCust.get(0);
				        				String brandDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			        					prdBrand.clear();
			        					prdBrand.put(IDBConstants.PLANT, parentplant);
			        					prdBrand.put(IDBConstants.PRDBRANDID, brandId);
			        					prdBrand.put(IDBConstants.PRDBRANDDESC, brandDesc);
			        					prdBrand.put(IConstants.ISACTIVE, isactive);
			        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        					prdBrand.put(IDBConstants.LOGIN_USER, username);
			        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
				        				}
			        				}
			        				}
			        			  //PRD BRAND END
			        				
			        			  //PRD CLASS START
			        				Hashtable htprdcls = new Hashtable();
			        			  	Hashtable htclass = new Hashtable();
			        			  	htprdcls.clear();
			        			  	htprdcls.put(IDBConstants.PLANT, parentplant);
			        				htprdcls.put(IDBConstants.PRDCLSID,PRD_CLS_ID);
			      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
			      					if(PRD_CLS_ID.length()>0) {
			      					if (flag == false) {
			      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(PRD_CLS_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String classId = (String) arrCust.get(0);
				        				String classDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			      						htclass.put(IDBConstants.PLANT, parentplant);
			      						htclass.put(IDBConstants.PRDCLSID, classId);
			      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
			      						htclass.put(IConstants.ISACTIVE, isactive);
			      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			      						htclass.put(IDBConstants.LOGIN_USER, username);
			      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
				        				}
			      					}
			      					}
			      				  //PRD CLASS END
			      					
			      				  //PRD TYPE START	
			      					Hashtable htprdtype = new Hashtable();
			        			  	Hashtable htprdtp = new Hashtable();
			        			  	htprdtype.clear();
			      					htprdtype.put(IDBConstants.PLANT, parentplant);
			      					htprdtype.put("PRD_TYPE_ID",ARTIST);
								    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
								    if(ARTIST.length()>0) {
								    if (flag == false) {
								    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(ARTIST,plant);
				        				if (arrCust.size() > 0) {
				        				String typeId = (String) arrCust.get(0);
				        				String typedesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
								    	htprdtp.clear();
								    	htprdtp.put(IDBConstants.PLANT, parentplant);
								    	htprdtp.put("PRD_TYPE_ID", typeId);
								    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedesc);
								    	htprdtp.put(IConstants.ISACTIVE, isactive);
								    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								    	htprdtp.put(IDBConstants.LOGIN_USER, username);
		               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
				        				}
		                               }
								    }
			      					//PRD TYPE END
								    
								    //PRD DEPT START
								    Hashtable htprddept = new Hashtable();
			        			  	Hashtable htdept = new Hashtable();
			        			  	htprddept.put(IDBConstants.PLANT, parentplant);
			        			  	htprddept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
			        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
			        			  	if(PRD_DEPT_ID.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(PRD_DEPT_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String deptId = (String) arrCust.get(0);
				        				String deptdesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
										htdept.put(IDBConstants.PLANT, parentplant);
										htdept.put(IDBConstants.PRDDEPTID, deptId);
										htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
										htdept.put(IConstants.ISACTIVE, isactive);
										htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										htdept.put(IDBConstants.LOGIN_USER, username);
										boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
				        				}
									}
			        			  	}
								   //PRD DEPT END
			        			  	
			        			  //check if Purchase UOM exists 
			        			  	Hashtable htInv = new Hashtable();
			        			  	Hashtable HtPurchaseuom = new Hashtable();
			        			  	HtPurchaseuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
			        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
			        			  	if(PURCHASEUOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, parentplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END PURCHASE UOM
			        			  	
			        			  //check if Sales UOM exists 
			        			  	Hashtable HtSalesuom = new Hashtable();
			        			  	HtSalesuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtSalesuom.put("UOM", SALESUOM);
			        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
			        			  	if(SALESUOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, parentplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END SALES UOM
			        			  	
								   //check if Inventory UOM exists 
			        			  	Hashtable HtInvuom = new Hashtable();
			        			  	HtInvuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtInvuom.put("UOM", INVENTORYUOM);
			        			  	flag = new UomUtil().isExistsUom(HtInvuom);
			        			  	if(INVENTORYUOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
							    	  htInv.clear();
							    	  htInv.put(IDBConstants.PLANT,parentplant);
							    	  htInv.put("UOM", uom);
							    	  htInv.put("UOMDESC", uomdesc);
							    	  htInv.put("Display",display);
							    	  htInv.put("QPUOM", qpuom);
							    	  htInv.put(IConstants.ISACTIVE, isactive);
							    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	  htInv.put(IDBConstants.LOGIN_USER,username);
							    	  boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END INV UOM
			        			  	
			        			  	//check if Stk UOM exists 
			        			  	Hashtable HtStkuom = new Hashtable();
			        			  	HtStkuom.put(IDBConstants.PLANT, parentplant);
			        			  	HtStkuom.put("UOM", UOM);
			        			  	flag = new UomUtil().isExistsUom(HtStkuom);
			        			  	if(UOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(UOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT,parentplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display",display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER,username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END STOCK UOM
			        			  	
			        			  //HSCODE
			        			  	if(HSCODE.length()>0) {
			        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, parentplant)) 
									{						
						    			Hashtable htHS = new Hashtable();
						    			htHS.put(IDBConstants.PLANT,parentplant);
						    			htHS.put(IDBConstants.HSCODE,HSCODE);
						    			htHS.put(IDBConstants.LOGIN_USER,username);
						    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddHSCODE(htHS);
										boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, parentplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",HSCODE);
										htRecvHis.put(IDBConstants.CREATED_BY, username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        			  	//HSCODE END
			        			  	
			        			  	//COO 
			        			  	if(COO.length()>0) {
			        				if (!new MasterUtil().isExistCOO(COO, parentplant)) 
									{						
						    			Hashtable htCoo = new Hashtable();
						    			htCoo.put(IDBConstants.PLANT,parentplant);
						    			htCoo.put(IDBConstants.COO,COO);
						    			htCoo.put(IDBConstants.LOGIN_USER,username);
						    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddCOO(htCoo);
										boolean insertflag = new MasterDAO().InsertCOO(htCoo);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, parentplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",COO);
										htRecvHis.put(IDBConstants.CREATED_BY,username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        				//COO END
			        				
			        				//SUPPLIER START 
			        			  	if(vendno.length()>0) {
			        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
			        				if (arrCust.size() > 0) {
			        				String sCustCode = (String) arrCust.get(0);
			        				String sCustName = (String) arrCust.get(1);
			        				if (!new CustUtil().isExistVendor(vendno, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
			        				{
			        					String sAddr1 = (String) arrCust.get(2);
			        					String sAddr2 = (String) arrCust.get(3);
			        					String sAddr3 = (String) arrCust.get(4);
			        					String sAddr4 = (String) arrCust.get(15);
			        					String sCountry = (String) arrCust.get(5);
			        					String sZip = (String) arrCust.get(6);
			        					String sCons = (String) arrCust.get(7);
			        					String sContactName = (String) arrCust.get(8);
			        					String sDesgination = (String) arrCust.get(9);
			        					String sTelNo = (String) arrCust.get(10);
			        					String sHpNo = (String) arrCust.get(11);
			        					String sEmail = (String) arrCust.get(12);
			        					String sFax = (String) arrCust.get(13);
			        					String sRemarks = (String) arrCust.get(14);
			        					String isActive = (String) arrCust.get(16);
			        					String sPayTerms = (String) arrCust.get(17);
			        					String sPayMentTerms = (String) arrCust.get(39);
			        					String sPayInDays = (String) arrCust.get(18);
			        					String sState = (String) arrCust.get(19);
			        					String sRcbno = (String) arrCust.get(20);
			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
			        					String WEBSITE = (String) arrCust.get(23);
			        					String FACEBOOK = (String) arrCust.get(24);
			        					String TWITTER = (String) arrCust.get(25);
			        					String LINKEDIN = (String) arrCust.get(26);
			        					String SKYPE = (String) arrCust.get(27);
			        					String OPENINGBALANCE = (String) arrCust.get(28);
			        					String WORKPHONE = (String) arrCust.get(29);
			        					String sTAXTREATMENT = (String) arrCust.get(30);
			        					String sCountryCode = (String) arrCust.get(31);
			        					String sBANKNAME = (String) arrCust.get(32);
			        					String sBRANCH= (String) arrCust.get(33);
			        					String sIBAN = (String) arrCust.get(34);
			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
			        					String companyregnumber = (String) arrCust.get(36);
			        					String PEPPOL = (String) arrCust.get(40);
			        					String PEPPOL_ID = (String) arrCust.get(41);
			        					String CURRENCY = (String) arrCust.get(37);
//			        					String transport = (String) arrCust.get(38);
//			        					String suppliertypeid = (String) arrCust.get(21);
			        					Hashtable htsup = new Hashtable();
			        					htsup.put(IDBConstants.PLANT,parentplant);
			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
			        					htsup.put(IConstants.companyregnumber,companyregnumber);
			        					htsup.put("ISPEPPOL", PEPPOL);
			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
			        					htsup.put("CURRENCY_ID", CURRENCY);
			        					htsup.put(IConstants.NAME, sContactName);
			        					htsup.put(IConstants.DESGINATION, sDesgination);
			        					htsup.put(IConstants.TELNO, sTelNo);
			        					htsup.put(IConstants.HPNO, sHpNo);
			        					htsup.put(IConstants.FAX, sFax);
			        					htsup.put(IConstants.EMAIL, sEmail);
			        					htsup.put(IConstants.ADDRESS1, sAddr1);
			        					htsup.put(IConstants.ADDRESS2, sAddr2);
			        					htsup.put(IConstants.ADDRESS3, sAddr3);
			        					htsup.put(IConstants.ADDRESS4, sAddr4);
			        					if(sState.equalsIgnoreCase("Select State"))
			        						sState="";
			        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			        					htsup.put(IConstants.ZIP, sZip);
			        					htsup.put(IConstants.USERFLG1, sCons);
			        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//			        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//			        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//			        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
			        					htsup.put(IConstants.PAYTERMS, "");
			        					htsup.put(IConstants.payment_terms, "");
			        					htsup.put(IConstants.PAYINDAYS, "");
			        					htsup.put(IConstants.ISACTIVE, isActive);
//			        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//			        					htsup.put(IConstants.TRANSPORTID,transport);
			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
			        					htsup.put(IConstants.TRANSPORTID, "0");
			        					htsup.put(IConstants.RCBNO, sRcbno);
			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        					htsup.put(IConstants.WEBSITE,WEBSITE);
			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
			        					htsup.put(IConstants.TWITTER,TWITTER);
			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
			        					htsup.put(IConstants.SKYPE,SKYPE);
			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        			        	  sBANKNAME="";
//			        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
			        			          htsup.put(IDBConstants.BANKNAME,"");
			        			          htsup.put(IDBConstants.IBAN,sIBAN);
			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			        			          htsup.put("CRAT",new DateUtils().getDateTime());
			        			          htsup.put("CRBY",username);
			        			          htsup.put("Comment1", " 0 ");
			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			        				}
			        				}
			        				}
			        				//Supplier END
						          posquery="select PLANT,OUTLET from "+parentplant+"_POSOUTLETS WHERE PLANT ='"+parentplant+"'";
						          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						      	       	Hashtable htCondition = new Hashtable();
						      	        	htCondition.put(IConstants.ITEM,ITEM);
						      	        	htCondition.put(IConstants.PLANT,parentplant);
						      	        	htCondition.put("OUTLET",outlet);
						    	        Hashtable hPos = new Hashtable();	
						        		  hPos.put(IConstants.PLANT,parentplant);
						        		  hPos.put("OUTLET",outlet);
						        		  hPos.put(IConstants.ITEM,ITEM);
						        		  hPos.put("SALESUOM",SALESUOM);
						        		  hPos.put(IConstants.PRICE,PRICE);
						        		  hPos.put("CRBY",username);
						        		  hPos.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,parentplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
						        		  }
						        	  }
						          }
						          
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
					      	        		htCondition.put(IConstants.PLANT,parentplant);
					      	        		htCondition.put("OUTLET",outlet);
						        	    Hashtable hPoss = new Hashtable();	
						        		  hPoss.put(IConstants.PLANT,parentplant);
						        		  hPoss.put("OUTLET",outlet);
						        		  hPoss.put(IConstants.ITEM,ITEM);
						        		  hPoss.put("MINQTY",STKQTY);
						        		  hPoss.put("MAXQTY",MAXSTKQTY);
						        		  hPoss.put("CRBY",username);
						        		  hPoss.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,parentplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
						        		  }
						        	  }
						          }
			        		  }
			        	  }
			        	  
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  if(childplant!=plant) {
			        			  ht.put(IConstants.PLANT,childplant);
			        			  posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
			        			  if(!(itemUtil.isExistsItemMst(ITEM,childplant))) {
			        				  String catpath = catlogpath;
				        			  catpath = catpath.replace(plant,childplant);
			      						ht.put(IDBConstants.CATLOGPATH, catpath);
			        				  boolean childitemInserted = itemUtil.insertItem(ht);
			        			  }
			        			  
			        			  //PRD BRAND START
			        			  	Hashtable prdBrand = new Hashtable();
			        			  	Hashtable htBrandtype = new Hashtable();
			        				htBrandtype.clear();
			        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
			        				htBrandtype.put(IDBConstants.PRDBRANDID, PRD_BRAND);
			        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
			        				if(PRD_BRAND.length()>0) {
			        				if (flag == false) {
			        					ArrayList arrCust = new PrdBrandUtil().getPrdBrandDetails(PRD_BRAND,plant);
				        				if (arrCust.size() > 0) {
				        				String brandId = (String) arrCust.get(0);
				        				String brandDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			        					prdBrand.clear();
			        					prdBrand.put(IDBConstants.PLANT, childplant);
			        					prdBrand.put(IDBConstants.PRDBRANDID, brandId);
			        					prdBrand.put(IDBConstants.PRDBRANDDESC, brandDesc);
			        					prdBrand.put(IConstants.ISACTIVE, isactive);
			        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        					prdBrand.put(IDBConstants.LOGIN_USER, username);
			        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
				        				}
			        				}
			        				}
			        			  //PRD BRAND END
			        				
			        			  //PRD CLASS START
			        				Hashtable htprdcls = new Hashtable();
			        			  	Hashtable htclass = new Hashtable();
			        			  	htprdcls.clear();
			        			  	htprdcls.put(IDBConstants.PLANT, childplant);
			        				htprdcls.put(IDBConstants.PRDCLSID,PRD_CLS_ID);
			      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
			      					if(PRD_CLS_ID.length()>0) {
			      					if (flag == false) {
			      						ArrayList arrCust = new PrdClassUtil().getPrdClassDetails(PRD_CLS_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String classId = (String) arrCust.get(0);
				        				String classDesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
			      						htclass.put(IDBConstants.PLANT, childplant);
			      						htclass.put(IDBConstants.PRDCLSID, classId);
			      						htclass.put(IDBConstants.PRDCLSDESC, classDesc);
			      						htclass.put(IConstants.ISACTIVE, isactive);
			      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			      						htclass.put(IDBConstants.LOGIN_USER, username);
			      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
				        				}
			      					}
			      					}
			      				  //PRD CLASS END
			      					
			      				  //PRD TYPE START	
			      					Hashtable htprdtype = new Hashtable();
			        			  	Hashtable htprdtp = new Hashtable();
			        			  	htprdtype.clear();
			      					htprdtype.put(IDBConstants.PLANT, childplant);
			      					htprdtype.put("PRD_TYPE_ID",ARTIST);
								    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
								    if(ARTIST.length()>0) {
								    if (flag == false) {
								    	ArrayList arrCust = new PrdTypeUtil().getPrdTypeDetails(ARTIST,plant);
				        				if (arrCust.size() > 0) {
				        				String typeId = (String) arrCust.get(0);
				        				String typedesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
								    	htprdtp.clear();
								    	htprdtp.put(IDBConstants.PLANT, childplant);
								    	htprdtp.put("PRD_TYPE_ID", typeId);
								    	htprdtp.put(IDBConstants.PRDTYPEDESC, typedesc);
								    	htprdtp.put(IConstants.ISACTIVE, isactive);
								    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
								    	htprdtp.put(IDBConstants.LOGIN_USER, username);
		               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
				        				}
		                               }
								    }
			      					//PRD TYPE END
								    
								    //PRD DEPT START
								    Hashtable htprddept = new Hashtable();
			        			  	Hashtable htdept = new Hashtable();
			        			  	htprddept.put(IDBConstants.PLANT, childplant);
			        			  	htprddept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
			        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
			        			  	if(PRD_DEPT_ID.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new PrdDeptUtil().getPrdDeptDetails(PRD_DEPT_ID,plant);
				        				if (arrCust.size() > 0) {
				        				String deptId = (String) arrCust.get(0);
				        				String deptdesc = (String) arrCust.get(1);
				        				String isactive = (String) arrCust.get(2);
										htdept.put(IDBConstants.PLANT, childplant);
										htdept.put(IDBConstants.PRDDEPTID, deptId);
										htdept.put(IDBConstants.PRDDEPTDESC, deptdesc);
										htdept.put(IConstants.ISACTIVE, isactive);
										htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										htdept.put(IDBConstants.LOGIN_USER, username);
										boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
				        				}
									}
			        			  	}
								   //PRD DEPT END
			        			  	
			        			  //check if Purchase UOM exists 
			        			  	Hashtable htInv = new Hashtable();
			        			  	Hashtable HtPurchaseuom = new Hashtable();
			        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
			        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
			        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
			        			  	if(PURCHASEUOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(PURCHASEUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END PURCHASE UOM
			        			  	
			        			  //check if Sales UOM exists 
			        			  	Hashtable HtSalesuom = new Hashtable();
			        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
			        			  	HtSalesuom.put("UOM", SALESUOM);
			        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
			        			  	if(SALESUOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(SALESUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT, childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display", display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER, username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END SALES UOM
			        			  	
								   //check if Inventory UOM exists 
			        			  	Hashtable HtInvuom = new Hashtable();
			        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
			        			  	HtInvuom.put("UOM", INVENTORYUOM);
			        			  	flag = new UomUtil().isExistsUom(HtInvuom);
			        			  	if(INVENTORYUOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(INVENTORYUOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
							    	  htInv.clear();
							    	  htInv.put(IDBConstants.PLANT,childplant);
							    	  htInv.put("UOM", uom);
							    	  htInv.put("UOMDESC", uomdesc);
							    	  htInv.put("Display",display);
							    	  htInv.put("QPUOM", qpuom);
							    	  htInv.put(IConstants.ISACTIVE, isactive);
							    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	  htInv.put(IDBConstants.LOGIN_USER,username);
							    	  boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END INV UOM
			        			  	
			        			  	//check if Stk UOM exists 
			        			  	Hashtable HtStkuom = new Hashtable();
			        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
			        			  	HtStkuom.put("UOM", UOM);
			        			  	flag = new UomUtil().isExistsUom(HtStkuom);
			        			  	if(UOM.length()>0) {
			        			  	if (flag == false) {
			        			  		ArrayList arrCust = new UomUtil().getUomDetails(UOM,plant);
				        				if (arrCust.size() > 0) {
				        				String uom = (String) arrCust.get(0);
				        				String uomdesc = (String) arrCust.get(1);
				        				String display = (String) arrCust.get(2);
				        				String qpuom = (String) arrCust.get(3);
				        				String isactive = (String) arrCust.get(4);
			        			  		htInv.clear();
			        			  		htInv.put(IDBConstants.PLANT,childplant);
			        			  		htInv.put("UOM", uom);
			        			  		htInv.put("UOMDESC", uomdesc);
			        			  		htInv.put("Display",display);
			        			  		htInv.put("QPUOM", qpuom);
			        			  		htInv.put(IConstants.ISACTIVE, isactive);
			        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        			  		htInv.put(IDBConstants.LOGIN_USER,username);
			        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
				        				}
			        			  	}
			        			  	}
			        			  	//END STOCK UOM
			        			  	
			        			  //HSCODE
			        			  	if(HSCODE.length()>0) {
			        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, childplant)) 
									{						
						    			Hashtable htHS = new Hashtable();
						    			htHS.put(IDBConstants.PLANT,childplant);
						    			htHS.put(IDBConstants.HSCODE,HSCODE);
						    			htHS.put(IDBConstants.LOGIN_USER,username);
						    			htHS.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddHSCODE(htHS);
										boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",HSCODE);
										htRecvHis.put(IDBConstants.CREATED_BY, username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        			  	//HSCODE END
			        			  	
			        			  	//COO 
			        			  	if(COO.length()>0) {
			        				if (!new MasterUtil().isExistCOO(COO, childplant)) 
									{						
						    			Hashtable htCoo = new Hashtable();
						    			htCoo.put(IDBConstants.PLANT,childplant);
						    			htCoo.put(IDBConstants.COO,COO);
						    			htCoo.put(IDBConstants.LOGIN_USER,username);
						    			htCoo.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//										boolean insertflag = new MasterUtil().AddCOO(htCoo);
										boolean insertflag = new MasterDAO().InsertCOO(htCoo);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, childplant);
										htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
										htRecvHis.put("ORDNUM","");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS",COO);
										htRecvHis.put(IDBConstants.CREATED_BY,username);
										htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
									}
			        			  	}
			        				//COO END
			        				
			        				//SUPPLIER START 
			        			  	if(vendno.length()>0) {
			        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,plant);
			        				if (arrCust.size() > 0) {
			        				String sCustCode = (String) arrCust.get(0);
			        				String sCustName = (String) arrCust.get(1);
			        				if (!new CustUtil().isExistVendor(vendno, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
			        				{
			        					String sAddr1 = (String) arrCust.get(2);
			        					String sAddr2 = (String) arrCust.get(3);
			        					String sAddr3 = (String) arrCust.get(4);
			        					String sAddr4 = (String) arrCust.get(15);
			        					String sCountry = (String) arrCust.get(5);
			        					String sZip = (String) arrCust.get(6);
			        					String sCons = (String) arrCust.get(7);
			        					String sContactName = (String) arrCust.get(8);
			        					String sDesgination = (String) arrCust.get(9);
			        					String sTelNo = (String) arrCust.get(10);
			        					String sHpNo = (String) arrCust.get(11);
			        					String sEmail = (String) arrCust.get(12);
			        					String sFax = (String) arrCust.get(13);
			        					String sRemarks = (String) arrCust.get(14);
			        					String isActive = (String) arrCust.get(16);
			        					String sPayTerms = (String) arrCust.get(17);
			        					String sPayMentTerms = (String) arrCust.get(39);
			        					String sPayInDays = (String) arrCust.get(18);
			        					String sState = (String) arrCust.get(19);
			        					String sRcbno = (String) arrCust.get(20);
			        					String CUSTOMEREMAIL = (String) arrCust.get(22);
			        					String WEBSITE = (String) arrCust.get(23);
			        					String FACEBOOK = (String) arrCust.get(24);
			        					String TWITTER = (String) arrCust.get(25);
			        					String LINKEDIN = (String) arrCust.get(26);
			        					String SKYPE = (String) arrCust.get(27);
			        					String OPENINGBALANCE = (String) arrCust.get(28);
			        					String WORKPHONE = (String) arrCust.get(29);
			        					String sTAXTREATMENT = (String) arrCust.get(30);
			        					String sCountryCode = (String) arrCust.get(31);
			        					String sBANKNAME = (String) arrCust.get(32);
			        					String sBRANCH= (String) arrCust.get(33);
			        					String sIBAN = (String) arrCust.get(34);
			        					String sBANKROUTINGCODE = (String) arrCust.get(35);
			        					String companyregnumber = (String) arrCust.get(36);
			        					String PEPPOL = (String) arrCust.get(40);
			        					String PEPPOL_ID = (String) arrCust.get(41);
			        					String CURRENCY = (String) arrCust.get(37);
//			        					String transport = (String) arrCust.get(38);
//			        					String suppliertypeid = (String) arrCust.get(21);
			        					Hashtable htsup = new Hashtable();
			        					htsup.put(IDBConstants.PLANT,childplant);
			        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
			        					htsup.put(IConstants.VENDOR_NAME, sCustName);
			        					htsup.put(IConstants.companyregnumber,companyregnumber);
			        					htsup.put("ISPEPPOL", PEPPOL);
			        					htsup.put("PEPPOL_ID", PEPPOL_ID);
			        					htsup.put("CURRENCY_ID", CURRENCY);
			        					htsup.put(IConstants.NAME, sContactName);
			        					htsup.put(IConstants.DESGINATION, sDesgination);
			        					htsup.put(IConstants.TELNO, sTelNo);
			        					htsup.put(IConstants.HPNO, sHpNo);
			        					htsup.put(IConstants.FAX, sFax);
			        					htsup.put(IConstants.EMAIL, sEmail);
			        					htsup.put(IConstants.ADDRESS1, sAddr1);
			        					htsup.put(IConstants.ADDRESS2, sAddr2);
			        					htsup.put(IConstants.ADDRESS3, sAddr3);
			        					htsup.put(IConstants.ADDRESS4, sAddr4);
			        					if(sState.equalsIgnoreCase("Select State"))
			        						sState="";
			        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			        					htsup.put(IConstants.ZIP, sZip);
			        					htsup.put(IConstants.USERFLG1, sCons);
			        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//			        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//			        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//			        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
			        					htsup.put(IConstants.PAYTERMS, "");
			        					htsup.put(IConstants.payment_terms, "");
			        					htsup.put(IConstants.PAYINDAYS, "");
			        					htsup.put(IConstants.ISACTIVE, isActive);
//			        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//			        					htsup.put(IConstants.TRANSPORTID,transport);
			        					htsup.put(IConstants.SUPPLIERTYPEID,"");
			        					htsup.put(IConstants.TRANSPORTID, "0");
			        					htsup.put(IConstants.RCBNO, sRcbno);
			        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        					htsup.put(IConstants.WEBSITE,WEBSITE);
			        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
			        					htsup.put(IConstants.TWITTER,TWITTER);
			        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
			        					htsup.put(IConstants.SKYPE,SKYPE);
			        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
			        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        			        	  sBANKNAME="";
//			        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
			        			          htsup.put(IDBConstants.BANKNAME,"");
			        			          htsup.put(IDBConstants.IBAN,sIBAN);
			        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			        			          htsup.put("CRAT",new DateUtils().getDateTime());
			        			          htsup.put("CRBY",username);
			        			          htsup.put("Comment1", " 0 ");
			        			          boolean custInserted = new CustUtil().insertVendor(htsup);
			        				}
			        				}
			        			  	}
			        				//Supplier END
						          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						        	     Hashtable htCondition = new Hashtable();
						      	        	htCondition.put(IConstants.ITEM,ITEM);
						      	        	htCondition.put(IConstants.PLANT,childplant);
						      	        	htCondition.put("OUTLET",outlet);
						        		  Hashtable hPos = new Hashtable();	
						        		  hPos.put(IConstants.PLANT,childplant);
						        		  hPos.put("OUTLET",outlet);
						        		  hPos.put(IConstants.ITEM,ITEM);
						        		  hPos.put("SALESUOM",SALESUOM);
						        		  hPos.put(IConstants.PRICE,PRICE);
						        		  hPos.put("CRBY",username);
						        		  hPos.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletPrice(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
						        		  }
						        	  }
						          }
						          
						          pos = new HashMap();
						          if (posList.size() > 0) {
						        	  for (int j=0; j < posList.size(); j++ ) {
						        		  pos = (Map) posList.get(j);
						        		  String outlet = (String) pos.get("OUTLET");
						        		  String posplant = (String) pos.get("PLANT");
						       	       	Hashtable htCondition = new Hashtable();
						       	       		htCondition.put(IConstants.ITEM,ITEM);
					      	        		htCondition.put(IConstants.PLANT,childplant);
					      	        		htCondition.put("OUTLET",outlet);
						        	    Hashtable hPoss = new Hashtable();	
						        		  hPoss.put(IConstants.PLANT,childplant);
						        		  hPoss.put("OUTLET",outlet);
						        		  hPoss.put(IConstants.ITEM,ITEM);
						        		  hPoss.put("MINQTY",STKQTY);
						        		  hPoss.put("MAXQTY",MAXSTKQTY);
						        		  hPoss.put("CRBY",username);
						        		  hPoss.put("CRAT",dateutils.getDateTime());
						        		  if(!(itemUtil.isExistsPosOutletminmax(ITEM,outlet,childplant))) {
						        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletminmax(hPoss);
						        		  }else {
						        			  boolean posUpdated = new POSHdrDAO().updatePosOutletminmax(hPoss,htCondition);
						        		  }
						        	  }
						          }
			        			  }
			        		  }
			        	  }
			        	  
			        	  }
			        	 }
			          }
			          
			          
			          
						//added by imthi 29-06-2022 product popup save ->DESC: get item and insert with CUSTOMERID from custmst tbl
			          CustUtil custUtils = new CustUtil(); 
			          CustMstDAO custMstDAO = new CustMstDAO(); 
			          ArrayList movQryLists =custUtils.getCustomerListWithType("",plant,"");
			          if (movQryLists.size() > 0) {
			  			for(int i =0; i<movQryLists.size(); i++) {
			  				Map arrCustLine = (Map)movQryLists.get(i);
			  				String customerNo=(String)arrCustLine.get("CUSTNO");
			  					Hashtable apporderht = new Hashtable();
			  					apporderht.put(IConstants.PLANT,plant);
			  					apporderht.put("CUSTNO",customerNo);
			  					apporderht.put(IConstants.ITEM,ITEM);
			  					apporderht.put(IConstants.ITEM_DESC,DESC);
			  					apporderht.put("ORDER_QTY","0");
			  					apporderht.put("MAX_ORDER_QTY","0");
			  					apporderht.put("CRBY",username);
			  					apporderht.put("CRAT",dateutils.getDateTime());
			  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
			  			}
			          }
			          
			          //IMTHI START to insert for table APPORDERQTYCONFIG in child parent
			          if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
							          movQryLists =custUtils.getCustomerListWithType("",childplant,"");
							          if (movQryLists.size() > 0) {
							  			for(int t =0; t<movQryLists.size(); t++) {
							  				Map arrCustLine = (Map)movQryLists.get(t);
							  				String customerNo=(String)arrCustLine.get("CUSTNO");
							  					Hashtable apporderht = new Hashtable();
							  					apporderht.put(IConstants.PLANT,childplant);
							  					apporderht.put("CUSTNO",customerNo);
							  					apporderht.put(IConstants.ITEM,ITEM);
							  					apporderht.put(IConstants.ITEM_DESC,DESC);
							  					apporderht.put("ORDER_QTY","0");
							  					apporderht.put("MAX_ORDER_QTY","0");
							  					apporderht.put("CRBY",username);
							  					apporderht.put("CRAT",dateutils.getDateTime());
							  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
							  			}
							          }
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int i=0; i < arrLists.size(); i++ ) {
					        			  Map ms = (Map) arrLists.get(i);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
								          movQryLists =custUtils.getCustomerListWithType("",parentplant,"");
								          if (movQryLists.size() > 0) {
								  			for(int t =0; t<movQryLists.size(); t++) {
								  				Map arrCustLine = (Map)movQryLists.get(t);
								  				String customerNo=(String)arrCustLine.get("CUSTNO");
								  					Hashtable apporderht = new Hashtable();
								  					apporderht.put(IConstants.PLANT,parentplant);
								  					apporderht.put("CUSTNO",customerNo);
								  					apporderht.put(IConstants.ITEM,ITEM);
								  					apporderht.put(IConstants.ITEM_DESC,DESC);
								  					apporderht.put("ORDER_QTY","0");
								  					apporderht.put("MAX_ORDER_QTY","0");
								  					apporderht.put("CRBY",username);
								  					apporderht.put("CRAT",dateutils.getDateTime());
								  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
								  			}
								          }
					        		  }
					        	  }
					        	  
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  if(childplant!=plant) {
									          movQryLists =custUtils.getCustomerListWithType("",childplant,"");
									          if (movQryLists.size() > 0) {
									  			for(int t =0; t<movQryLists.size(); t++) {
									  				Map arrCustLine = (Map)movQryLists.get(t);
									  				String customerNo=(String)arrCustLine.get("CUSTNO");
									  					Hashtable apporderht = new Hashtable();
									  					apporderht.put(IConstants.PLANT,childplant);
									  					apporderht.put("CUSTNO",customerNo);
									  					apporderht.put(IConstants.ITEM,ITEM);
									  					apporderht.put(IConstants.ITEM_DESC,DESC);
									  					apporderht.put("ORDER_QTY","0");
									  					apporderht.put("MAX_ORDER_QTY","0");
									  					apporderht.put("CRBY",username);
									  					apporderht.put("CRAT",dateutils.getDateTime());
									  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
									  			}
									          }
					        			  }
					        		  	}
					        	  	}
				        	  }
		             		}
		             	  }//IMTHI END
			          
			          {
			          if (itemInserted) {
			        	  String alternateItemName = ITEM; 
				           	 List<String> alternateItemNameLists = new ArrayList<String>();
				        	 alternateItemNameLists.add(alternateItemName);
				        	 insertAlternateItem = itemUtil.insertAlternateItemLists(plant, ITEM, alternateItemNameLists);
				        	 
				        	  if(PARENT_PLANT != null){
					        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
					        	  if(CHECKplantCompany == null)
					  				CHECKplantCompany = "0";
					        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  if (arrList.size() > 0) {
						        		  for (int i=0; i < arrList.size(); i++ ) {
						        			  Map m = (Map) arrList.get(i);
						        			  String childplant = (String) m.get("CHILD_PLANT");
						        			  insertAlternateItem = itemUtil.insertAlternateItemLists(childplant, ITEM, alternateItemNameLists);
						        		  	}
						        	  	}
					        	  	}
				             	}else if(PARENT_PLANT == null){
				             		boolean ischild = false;
						        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
						        	  if (arrLi.size() > 0) {
						        	  Map mst = (Map) arrLi.get(0);
						        	  String parent = (String) mst.get("PARENT_PLANT");
						         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
						        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
						        	  if(Ischildasparent){
						        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        			  ischild = true;
						        		  }
						        	  }else{
						        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        		  ischild = true;
						        	  }
						        	  }
						        	  if(ischild){
						        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
							        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
							        	  String  parentplant ="";
							        	  if (arrLists.size() > 0) {
							        		  for (int i=0; i < arrLists.size(); i++ ) {
							        			  Map ms = (Map) arrLists.get(i);
							        			  parentplant = (String) ms.get("PARENT_PLANT");
							        			  insertAlternateItem = itemUtil.insertAlternateItemLists(parentplant, ITEM, alternateItemNameLists);
							        		  }
							        	  }
							        	  
							        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
							        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
							        	  Map m = new HashMap();
							        	  if (arrList.size() > 0) {
							        		  for (int i=0; i < arrList.size(); i++ ) {
							        			  m = (Map) arrList.get(i);
							        			  String childplant = (String) m.get("CHILD_PLANT");
							        			  if(childplant!=plant) {
							        				  insertAlternateItem = itemUtil.insertAlternateItemLists(childplant, ITEM, alternateItemNameLists);
							        			  }
							        		  	}
							        	  	}
						        	  }	
				             	  }
			          			}
							
						}
			          if(itemInserted && insertAlternateItem) {
			        	  
			        	  //imti added ADDITIONAL detail desc AND additional product on 23-01-2023
			        	  //additional detail desc:
			        	  boolean DESCS=false;
			        	  for(int i =0 ; i < DETAILDESC.size() ; i++){
			        		  String DETAILDESC_VAL = (String) DETAILDESC.get(i);
			        		  if(DETAILDESC_VAL==""){
			        			  break;
			        		  }else{
			        			  Hashtable HM = new Hashtable();
			        			  HM.put(IConstants.PLANT, plant);
			        			  HM.put(IConstants.ITEM, ITEM);
			        			  HM.put("ITEMDETAILDESC", DETAILDESC_VAL);
			        			  HM.put("CRAT",dateutils.getDateTime());
			        			  HM.put("CRBY",username);
			        			  DESCS = itemUtil.insertDetailDesc(HM);
			        			  
			        			  //IMTHI ADD for CHILD PARENT to insert Additional Detail Description
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  DESCS = itemUtil.insertDetailDesc(HM);
								        		  	}
								        	  	}
							        	  	}
				    			    }else if(PARENT_PLANT == null){
				    			    	boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  DESCS = itemUtil.insertDetailDesc(HM);
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
									        				  DESCS = itemUtil.insertDetailDesc(HM);
									        			  }
									        		  	}
									        	  	}
								        	  	}
								        	  }
						             	  }//IMTHI END
			        		  }
			        	  }
			        	  
			        	  boolean ADDPRD=false;
			        	  //additional Product:
			        	  for(int i =0 ; i < ADITIONALPRD.size() ; i++){
			        		  String ADITIONALPRD_VAL = (String) ADITIONALPRD.get(i);
			        		  if(ADITIONALPRD_VAL==""){
			        			  break;
			        		  }else{
			        			  Hashtable HM = new Hashtable();
			        			  HM.put(IConstants.PLANT, plant);
			        			  HM.put(IConstants.ITEM, ITEM);
			        			  HM.put("ADDITIONALITEM", ADITIONALPRD_VAL);
			        			  HM.put("CRAT",dateutils.getDateTime());
			        			  HM.put("CRBY",username);
			        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
			        			  
			        			  //IMTHI ADD for CHILD PARENT to insert Additional Product
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
								        		  	}
								        	  	}
							        	  	}
				    			    }else if(PARENT_PLANT == null){
				    			    	boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
										        			  ADDPRD = itemUtil.insertAdditionalPrd(HM);
									        			  }
									        		  	}
									        	  	}
								        	  	}
								        	  }
						             	  }//IMTHI END
			        		  }
			        	  }
			        	  //imti end
			        	  
			        	  boolean OBCustomerDiscount=false;  
			           //insert customer discount outbound
			        	  for(int i =0 ; i < DYNAMIC_CUSTOMER_DISCOUNT.size() ; i++){
			        		  String DYNAMIC_CUSTOMER_DISCOUNT_VAL = (String) DYNAMIC_CUSTOMER_DISCOUNT.get(i);
			        		  String CUSTOMER_TYPE_ID_VAL = (String) CUSTOMER_TYPE_ID.get(i);
			        		  if(DYNAMIC_CUSTOMER_DISCOUNT_VAL==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, ITEM);
				    				HM.put(IConstants.CUSTOMERTYPEID, CUSTOMER_TYPE_ID_VAL);
				    				if(OBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
				    					HM.put("OBDISCOUNT", DYNAMIC_CUSTOMER_DISCOUNT_VAL+"%");
				    				}
				    				else{
				    					HM.put("OBDISCOUNT", DYNAMIC_CUSTOMER_DISCOUNT_VAL);
				    				}
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
				    			    
				    			    //IMTHI ADD for CHILD PARENT to insert customer discount
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
								        			  
								        			  //IMTHI ADD child company to insert in customer type
								        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(CUSTOMER_TYPE_ID_VAL,plant);
								        			  if (arrCust.size() > 0) {
								        			  	String sCustTypeId = (String) arrCust.get(1);
								        			  	String sCustTypeDesc = (String) arrCust.get(2);
								        			  	String sIsactive = (String) arrCust.get(7);
									        			Hashtable htcust = new Hashtable();
									        			htcust.put(IDBConstants.PLANT,childplant);
									        			htcust.put(IDBConstants.CUSTOMERTYPEID,CUSTOMER_TYPE_ID_VAL);
									        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
									        		    	Hashtable htcustAdd = new Hashtable();
										        		    	htcustAdd.put(IDBConstants.PLANT,childplant);
										        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
										        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
										        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
										        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										        				htcustAdd.put(IDBConstants.CREATED_BY, username);
										        		    Hashtable htms = new Hashtable();
										        		       htms.put("PLANT",childplant);
										        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
										        	           htms.put("RECID","");
										        	           htms.put("ITEM",sCustTypeId);
										        	           htms.put("REMARKS",sCustTypeDesc);
										        	           htms.put("UPBY",username);   
										        	           htms.put("CRBY",username);
										        	           htms.put("CRAT",dateutils.getDateTime());
										        	           htms.put("UPAT",dateutils.getDateTime());
										        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
										        	           htms.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
										        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
									        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
									        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//									        				   boolean Updated = custutil.updateCustomerTypeId(htUpdate,htcust);
//									        				   boolean inserted = mdao.insertIntoMovHis(htm);
									        		    }
									        		    }//IMTHI END CUSTOMER TYPE
									        		    
								        		  	}
								        	  	}
							        	  	}
						             	}else if(PARENT_PLANT == null){
						             		boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
									        			  
									        			  //IMTHI ADD child company to insert in customer type
									        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(CUSTOMER_TYPE_ID_VAL,plant);
									        			  if (arrCust.size() > 0) {
									        			  	String sCustTypeId = (String) arrCust.get(1);
									        			  	String sCustTypeDesc = (String) arrCust.get(2);
									        			  	String sIsactive = (String) arrCust.get(7);
										        			Hashtable htcust = new Hashtable();
										        			htcust.put(IDBConstants.PLANT,parentplant);
										        			htcust.put(IDBConstants.CUSTOMERTYPEID,CUSTOMER_TYPE_ID_VAL);
										        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
										        		    	Hashtable htcustAdd = new Hashtable();
											        		    	htcustAdd.put(IDBConstants.PLANT,parentplant);
											        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
											        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
											        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
											        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
											        				htcustAdd.put(IDBConstants.CREATED_BY, username);
											        		    Hashtable htms = new Hashtable();
											        		       htms.put("PLANT",parentplant);
											        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
											        	           htms.put("RECID","");
											        	           htms.put("ITEM",sCustTypeId);
											        	           htms.put("REMARKS",sCustTypeDesc);
											        	           htms.put("UPBY",username);   
											        	           htms.put("CRBY",username);
											        	           htms.put("CRAT",dateutils.getDateTime());
											        	           htms.put("UPAT",dateutils.getDateTime());
											        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
											        	           htms.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
											        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
										        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
										        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//										        				   boolean Updated = custutil.updateCustomerTypeId(htUpdate,htcust);
//										        				   boolean inserted = mdao.insertIntoMovHis(htm);
										        		    }
										        		    }//IMTHI END CUSTOMER TYPE
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
									        				  OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
									        				  
									        				  //IMTHI ADD child company to insert in customer type
										        			  ArrayList arrCust = new CustomerBeanDAO().getCustTypeDetails(CUSTOMER_TYPE_ID_VAL,plant);
										        			  if (arrCust.size() > 0) {
										        			  	String sCustTypeId = (String) arrCust.get(1);
										        			  	String sCustTypeDesc = (String) arrCust.get(2);
										        			  	String sIsactive = (String) arrCust.get(7);
											        			Hashtable htcust = new Hashtable();
											        			htcust.put(IDBConstants.PLANT,childplant);
											        			htcust.put(IDBConstants.CUSTOMERTYPEID,CUSTOMER_TYPE_ID_VAL);
											        		    if ((!new CustUtil().isExistsCustomerType(htcust))) {
											        		    	Hashtable htcustAdd = new Hashtable();
												        		    	htcustAdd.put(IDBConstants.PLANT,childplant);
												        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEID,sCustTypeId);
												        		    	htcustAdd.put(IDBConstants.CUSTOMERTYPEDESC, sCustTypeDesc);
												        				htcustAdd.put(IConstants.ISACTIVE, sIsactive);
												        				htcustAdd.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
												        				htcustAdd.put(IDBConstants.CREATED_BY, username);
												        		    Hashtable htms = new Hashtable();
												        		       htms.put("PLANT",childplant);
												        		       htms.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
												        	           htms.put("RECID","");
												        	           htms.put("ITEM",sCustTypeId);
												        	           htms.put("REMARKS",sCustTypeDesc);
												        	           htms.put("UPBY",username);   
												        	           htms.put("CRBY",username);
												        	           htms.put("CRAT",dateutils.getDateTime());
												        	           htms.put("UPAT",dateutils.getDateTime());
												        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
												        	           htms.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
												        	           htms.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
											        		           boolean CustomerTypeInserted = new CustUtil().insertCustomerTypeMst(htcustAdd);
											        		           boolean  insertedcust = mdao.insertIntoMovHis(htms);
//											        				   boolean Updated = custutil.updateCustomerTypeId(htUpdate,htcust);
//											        				   boolean inserted = mdao.insertIntoMovHis(htm);
											        		    }
											        		    }//IMTHI END CUSTOMER TYPE
									        			  }
									        		  	}
									        	  	}
								        	  	}
								        	  }
						             	  }//IMTHI END
				    								        		
				        		}
				        	 }
 			           //end insert customer discount sales
 			           
			        	  
			        	  //insert item supplier Purchase tab
			        	  boolean additemSupplier=false;
			        	  for(int i =0 ; i < ITEMSUPPLIER.size() ; i++){
			        		  String ITEMSUPPLIER_VAL = (String) ITEMSUPPLIER.get(i);
			        		  if(ITEMSUPPLIER_VAL==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, ITEM);
//				    				ArrayList movQryList = custUtils.getVendorListsWithName(ITEMSUPPLIER_VAL, plant,"");
//				    				Map arrCustLine = (Map)movQryList.get(0);
//				    				String VENDNO=(String)arrCustLine.get("VENDNO");
//				    				HM.put(IConstants.VENDNO, VENDNO);
				    				HM.put(IConstants.VENDNO, ITEMSUPPLIER_VAL);
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    additemSupplier = itemUtil.insertItemSupplier(HM);
				    			    
				    			    //IMTHI ADD for CHILD PARENT to insert supplier discount
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  additemSupplier = itemUtil.insertItemSupplier(HM);
								        			  
								        			//SUPPLIER START 
								        			  if(ITEMSUPPLIER_VAL.length()>0) {
								        				ArrayList arrCust = new CustUtil().getVendorDetails(ITEMSUPPLIER_VAL,plant);
								        				if (arrCust.size() > 0) {
								        				String sCustCode = (String) arrCust.get(0);
								        				String sCustName = (String) arrCust.get(1);
								        				if (!new CustUtil().isExistVendor(ITEMSUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
								        				{
								        					String sAddr1 = (String) arrCust.get(2);
								        					String sAddr2 = (String) arrCust.get(3);
								        					String sAddr3 = (String) arrCust.get(4);
								        					String sAddr4 = (String) arrCust.get(15);
								        					String sCountry = (String) arrCust.get(5);
								        					String sZip = (String) arrCust.get(6);
								        					String sCons = (String) arrCust.get(7);
								        					String sContactName = (String) arrCust.get(8);
								        					String sDesgination = (String) arrCust.get(9);
								        					String sTelNo = (String) arrCust.get(10);
								        					String sHpNo = (String) arrCust.get(11);
								        					String sEmail = (String) arrCust.get(12);
								        					String sFax = (String) arrCust.get(13);
								        					String sRemarks = (String) arrCust.get(14);
								        					String isActive = (String) arrCust.get(16);
								        					String sPayTerms = (String) arrCust.get(17);
								        					String sPayMentTerms = (String) arrCust.get(39);
								        					String sPayInDays = (String) arrCust.get(18);
								        					String sState = (String) arrCust.get(19);
								        					String sRcbno = (String) arrCust.get(20);
								        					String CUSTOMEREMAIL = (String) arrCust.get(22);
								        					String WEBSITE = (String) arrCust.get(23);
								        					String FACEBOOK = (String) arrCust.get(24);
								        					String TWITTER = (String) arrCust.get(25);
								        					String LINKEDIN = (String) arrCust.get(26);
								        					String SKYPE = (String) arrCust.get(27);
								        					String OPENINGBALANCE = (String) arrCust.get(28);
								        					String WORKPHONE = (String) arrCust.get(29);
								        					String sTAXTREATMENT = (String) arrCust.get(30);
								        					String sCountryCode = (String) arrCust.get(31);
								        					String sBANKNAME = (String) arrCust.get(32);
								        					String sBRANCH= (String) arrCust.get(33);
								        					String sIBAN = (String) arrCust.get(34);
								        					String sBANKROUTINGCODE = (String) arrCust.get(35);
								        					String companyregnumber = (String) arrCust.get(36);
								        					String PEPPOL = (String) arrCust.get(40);
								        					String PEPPOL_ID = (String) arrCust.get(41);
								        					String CURRENCY = (String) arrCust.get(37);
								        					Hashtable htsup = new Hashtable();
								        					htsup.put(IDBConstants.PLANT,childplant);
								        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
								        					htsup.put(IConstants.VENDOR_NAME, sCustName);
								        					htsup.put(IConstants.companyregnumber,companyregnumber);
								        					htsup.put("ISPEPPOL", PEPPOL);
								        					htsup.put("PEPPOL_ID", PEPPOL_ID);
								        					htsup.put("CURRENCY_ID", CURRENCY);
								        					htsup.put(IConstants.NAME, sContactName);
								        					htsup.put(IConstants.DESGINATION, sDesgination);
								        					htsup.put(IConstants.TELNO, sTelNo);
								        					htsup.put(IConstants.HPNO, sHpNo);
								        					htsup.put(IConstants.FAX, sFax);
								        					htsup.put(IConstants.EMAIL, sEmail);
								        					htsup.put(IConstants.ADDRESS1, sAddr1);
								        					htsup.put(IConstants.ADDRESS2, sAddr2);
								        					htsup.put(IConstants.ADDRESS3, sAddr3);
								        					htsup.put(IConstants.ADDRESS4, sAddr4);
								        					if(sState.equalsIgnoreCase("Select State"))
								        						sState="";
								        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
								        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
								        					htsup.put(IConstants.ZIP, sZip);
								        					htsup.put(IConstants.USERFLG1, sCons);
								        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
								        					htsup.put(IConstants.PAYTERMS, "");
								        					htsup.put(IConstants.payment_terms, "");
								        					htsup.put(IConstants.PAYINDAYS, "");
								        					htsup.put(IConstants.ISACTIVE, isActive);
								        					htsup.put(IConstants.SUPPLIERTYPEID,"");
								        					htsup.put(IConstants.TRANSPORTID, "0");
								        					htsup.put(IConstants.RCBNO, sRcbno);
								        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
								        					htsup.put(IConstants.WEBSITE,WEBSITE);
								        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
								        					htsup.put(IConstants.TWITTER,TWITTER);
								        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
								        					htsup.put(IConstants.SKYPE,SKYPE);
								        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
								        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
								        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
								        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
								        			        	  sBANKNAME="";
								        			          htsup.put(IDBConstants.BANKNAME,"");
								        			          htsup.put(IDBConstants.IBAN,sIBAN);
								        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
								        			          htsup.put("CRAT",new DateUtils().getDateTime());
								        			          htsup.put("CRBY",username);
								        			          htsup.put("Comment1", " 0 ");
								        			          boolean custInserted = new CustUtil().insertVendor(htsup);
								        				}
								        				}
								        			  }
								        				//Supplier END
								        		  	}
								        	  	}
							        	  	}
				    			    }else if(PARENT_PLANT == null){
				    			    	boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  additemSupplier = itemUtil.insertItemSupplier(HM);
									        			//SUPPLIER START 
									        			  if(ITEMSUPPLIER_VAL.length()>0) {
									        				ArrayList arrCust = new CustUtil().getVendorDetails(ITEMSUPPLIER_VAL,plant);
									        				if (arrCust.size() > 0) {
									        				String sCustCode = (String) arrCust.get(0);
									        				String sCustName = (String) arrCust.get(1);
									        				if (!new CustUtil().isExistVendor(ITEMSUPPLIER_VAL, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
									        				{
									        					String sAddr1 = (String) arrCust.get(2);
									        					String sAddr2 = (String) arrCust.get(3);
									        					String sAddr3 = (String) arrCust.get(4);
									        					String sAddr4 = (String) arrCust.get(15);
									        					String sCountry = (String) arrCust.get(5);
									        					String sZip = (String) arrCust.get(6);
									        					String sCons = (String) arrCust.get(7);
									        					String sContactName = (String) arrCust.get(8);
									        					String sDesgination = (String) arrCust.get(9);
									        					String sTelNo = (String) arrCust.get(10);
									        					String sHpNo = (String) arrCust.get(11);
									        					String sEmail = (String) arrCust.get(12);
									        					String sFax = (String) arrCust.get(13);
									        					String sRemarks = (String) arrCust.get(14);
									        					String isActive = (String) arrCust.get(16);
									        					String sPayTerms = (String) arrCust.get(17);
									        					String sPayMentTerms = (String) arrCust.get(39);
									        					String sPayInDays = (String) arrCust.get(18);
									        					String sState = (String) arrCust.get(19);
									        					String sRcbno = (String) arrCust.get(20);
									        					String CUSTOMEREMAIL = (String) arrCust.get(22);
									        					String WEBSITE = (String) arrCust.get(23);
									        					String FACEBOOK = (String) arrCust.get(24);
									        					String TWITTER = (String) arrCust.get(25);
									        					String LINKEDIN = (String) arrCust.get(26);
									        					String SKYPE = (String) arrCust.get(27);
									        					String OPENINGBALANCE = (String) arrCust.get(28);
									        					String WORKPHONE = (String) arrCust.get(29);
									        					String sTAXTREATMENT = (String) arrCust.get(30);
									        					String sCountryCode = (String) arrCust.get(31);
									        					String sBANKNAME = (String) arrCust.get(32);
									        					String sBRANCH= (String) arrCust.get(33);
									        					String sIBAN = (String) arrCust.get(34);
									        					String sBANKROUTINGCODE = (String) arrCust.get(35);
									        					String companyregnumber = (String) arrCust.get(36);
									        					String PEPPOL = (String) arrCust.get(40);
									        					String PEPPOL_ID = (String) arrCust.get(41);
									        					String CURRENCY = (String) arrCust.get(37);
									        					Hashtable htsup = new Hashtable();
									        					htsup.put(IDBConstants.PLANT,parentplant);
									        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
									        					htsup.put(IConstants.VENDOR_NAME, sCustName);
									        					htsup.put(IConstants.companyregnumber,companyregnumber);
									        					htsup.put("ISPEPPOL", PEPPOL);
									        					htsup.put("PEPPOL_ID", PEPPOL_ID);
									        					htsup.put("CURRENCY_ID", CURRENCY);
									        					htsup.put(IConstants.NAME, sContactName);
									        					htsup.put(IConstants.DESGINATION, sDesgination);
									        					htsup.put(IConstants.TELNO, sTelNo);
									        					htsup.put(IConstants.HPNO, sHpNo);
									        					htsup.put(IConstants.FAX, sFax);
									        					htsup.put(IConstants.EMAIL, sEmail);
									        					htsup.put(IConstants.ADDRESS1, sAddr1);
									        					htsup.put(IConstants.ADDRESS2, sAddr2);
									        					htsup.put(IConstants.ADDRESS3, sAddr3);
									        					htsup.put(IConstants.ADDRESS4, sAddr4);
									        					if(sState.equalsIgnoreCase("Select State"))
									        						sState="";
									        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
									        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
									        					htsup.put(IConstants.ZIP, sZip);
									        					htsup.put(IConstants.USERFLG1, sCons);
									        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
									        					htsup.put(IConstants.PAYTERMS, "");
									        					htsup.put(IConstants.payment_terms, "");
									        					htsup.put(IConstants.PAYINDAYS, "");
									        					htsup.put(IConstants.ISACTIVE, isActive);
									        					htsup.put(IConstants.SUPPLIERTYPEID,"");
									        					htsup.put(IConstants.TRANSPORTID, "0");
									        					htsup.put(IConstants.RCBNO, sRcbno);
									        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
									        					htsup.put(IConstants.WEBSITE,WEBSITE);
									        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
									        					htsup.put(IConstants.TWITTER,TWITTER);
									        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
									        					htsup.put(IConstants.SKYPE,SKYPE);
									        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
									        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
									        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
									        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
									        			        	  sBANKNAME="";
									        			          htsup.put(IDBConstants.BANKNAME,"");
									        			          htsup.put(IDBConstants.IBAN,sIBAN);
									        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
									        			          htsup.put("CRAT",new DateUtils().getDateTime());
									        			          htsup.put("CRBY",username);
									        			          htsup.put("Comment1", " 0 ");
									        			          boolean custInserted = new CustUtil().insertVendor(htsup);
									        				}
									        				}
									        			  }//Supplier END
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
									        				  additemSupplier = itemUtil.insertItemSupplier(HM);
									        				  
									        				//SUPPLIER START 
									        				  if(ITEMSUPPLIER_VAL.length()>0) {
										        				ArrayList arrCust = new CustUtil().getVendorDetails(ITEMSUPPLIER_VAL,plant);
										        				if (arrCust.size() > 0) {
										        				String sCustCode = (String) arrCust.get(0);
										        				String sCustName = (String) arrCust.get(1);
										        				if (!new CustUtil().isExistVendor(ITEMSUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
										        				{
										        					String sAddr1 = (String) arrCust.get(2);
										        					String sAddr2 = (String) arrCust.get(3);
										        					String sAddr3 = (String) arrCust.get(4);
										        					String sAddr4 = (String) arrCust.get(15);
										        					String sCountry = (String) arrCust.get(5);
										        					String sZip = (String) arrCust.get(6);
										        					String sCons = (String) arrCust.get(7);
										        					String sContactName = (String) arrCust.get(8);
										        					String sDesgination = (String) arrCust.get(9);
										        					String sTelNo = (String) arrCust.get(10);
										        					String sHpNo = (String) arrCust.get(11);
										        					String sEmail = (String) arrCust.get(12);
										        					String sFax = (String) arrCust.get(13);
										        					String sRemarks = (String) arrCust.get(14);
										        					String isActive = (String) arrCust.get(16);
										        					String sPayTerms = (String) arrCust.get(17);
										        					String sPayMentTerms = (String) arrCust.get(39);
										        					String sPayInDays = (String) arrCust.get(18);
										        					String sState = (String) arrCust.get(19);
										        					String sRcbno = (String) arrCust.get(20);
										        					String CUSTOMEREMAIL = (String) arrCust.get(22);
										        					String WEBSITE = (String) arrCust.get(23);
										        					String FACEBOOK = (String) arrCust.get(24);
										        					String TWITTER = (String) arrCust.get(25);
										        					String LINKEDIN = (String) arrCust.get(26);
										        					String SKYPE = (String) arrCust.get(27);
										        					String OPENINGBALANCE = (String) arrCust.get(28);
										        					String WORKPHONE = (String) arrCust.get(29);
										        					String sTAXTREATMENT = (String) arrCust.get(30);
										        					String sCountryCode = (String) arrCust.get(31);
										        					String sBANKNAME = (String) arrCust.get(32);
										        					String sBRANCH= (String) arrCust.get(33);
										        					String sIBAN = (String) arrCust.get(34);
										        					String sBANKROUTINGCODE = (String) arrCust.get(35);
										        					String companyregnumber = (String) arrCust.get(36);
										        					String PEPPOL = (String) arrCust.get(40);
										        					String PEPPOL_ID = (String) arrCust.get(41);
										        					String CURRENCY = (String) arrCust.get(37);
										        					Hashtable htsup = new Hashtable();
										        					htsup.put(IDBConstants.PLANT,childplant);
										        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
										        					htsup.put(IConstants.VENDOR_NAME, sCustName);
										        					htsup.put(IConstants.companyregnumber,companyregnumber);
										        					htsup.put("ISPEPPOL", PEPPOL);
										        					htsup.put("PEPPOL_ID", PEPPOL_ID);
										        					htsup.put("CURRENCY_ID", CURRENCY);
										        					htsup.put(IConstants.NAME, sContactName);
										        					htsup.put(IConstants.DESGINATION, sDesgination);
										        					htsup.put(IConstants.TELNO, sTelNo);
										        					htsup.put(IConstants.HPNO, sHpNo);
										        					htsup.put(IConstants.FAX, sFax);
										        					htsup.put(IConstants.EMAIL, sEmail);
										        					htsup.put(IConstants.ADDRESS1, sAddr1);
										        					htsup.put(IConstants.ADDRESS2, sAddr2);
										        					htsup.put(IConstants.ADDRESS3, sAddr3);
										        					htsup.put(IConstants.ADDRESS4, sAddr4);
										        					if(sState.equalsIgnoreCase("Select State"))
										        						sState="";
										        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
										        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
										        					htsup.put(IConstants.ZIP, sZip);
										        					htsup.put(IConstants.USERFLG1, sCons);
										        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
										        					htsup.put(IConstants.PAYTERMS, "");
										        					htsup.put(IConstants.payment_terms, "");
										        					htsup.put(IConstants.PAYINDAYS, "");
										        					htsup.put(IConstants.ISACTIVE, isActive);
										        					htsup.put(IConstants.SUPPLIERTYPEID,"");
										        					htsup.put(IConstants.TRANSPORTID, "0");
										        					htsup.put(IConstants.RCBNO, sRcbno);
										        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
										        					htsup.put(IConstants.WEBSITE,WEBSITE);
										        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
										        					htsup.put(IConstants.TWITTER,TWITTER);
										        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
										        					htsup.put(IConstants.SKYPE,SKYPE);
										        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
										        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
										        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
										        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
										        			        	  sBANKNAME="";
										        			          htsup.put(IDBConstants.BANKNAME,"");
										        			          htsup.put(IDBConstants.IBAN,sIBAN);
										        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
										        			          htsup.put("CRAT",new DateUtils().getDateTime());
										        			          htsup.put("CRBY",username);
										        			          htsup.put("Comment1", " 0 ");
										        			          boolean custInserted = new CustUtil().insertVendor(htsup);
										        				}
										        				}
									        				  }//Supplier END
									        			  }
									        		  	}
									        	  	}
								        	  	}	
								        	  }
						             	  }//IMTHI END
				        		}
			        		  }//imti end insert item supplier Purchase tab
			        	  
//			        	     //insert supplier discount Purchase
//			        	  boolean IBSupplierDiscount=false;
//			        	  for(int i =0 ; i < DYNAMIC_SUPPLIER_DISCOUNT.size() ; i++){
//			        		  String DYNAMIC_SUPPLIER_DISCOUNT_VAL = (String) DYNAMIC_SUPPLIER_DISCOUNT.get(i);
//			        		  String SUPPLIER_VAL = (String) SUPPLIER.get(i);
//			        		  if(DYNAMIC_SUPPLIER_DISCOUNT_VAL==""){
//				        			break;
//				        		}else{
//				        			String SupplierDesc;
//				        			Hashtable HM = new Hashtable();
//				        		   	HM.put(IConstants.PLANT, plant);
//				    				HM.put(IConstants.ITEM, ITEM);
//				    				HM.put(IConstants.VENDNO, SUPPLIER_VAL);
//				    				VendMstDAO vendMstDAO = new VendMstDAO();
//				    				SupplierDesc=vendMstDAO.getVendorNameByNo(HM);
//				    					HM.put("VNAME", SupplierDesc);				    				
//				    				System.out.println("IBDISCOUNT"+IBDISCOUNT);
//				    				if(IBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
//				    					HM.put("IBDISCOUNT", DYNAMIC_SUPPLIER_DISCOUNT_VAL+"%");
//				    									    				}
//				    				else{
//				    					HM.put("IBDISCOUNT", DYNAMIC_SUPPLIER_DISCOUNT_VAL);
//				    									    				}
//				    				HM.put("CRAT",dateutils.getDateTime());
//				    			    HM.put("CRBY",username);
			        	     //insert supplier discount Purchase
			        	  boolean IBSupplierDiscount=false;
			        	  for(int i =0 ; i < DYNAMIC_SUPPLIER_DISCOUNT.size() ; i++){
			        		  String DYNAMIC_SUPPLIER_DISCOUNT_VAL = (String) DYNAMIC_SUPPLIER_DISCOUNT.get(i);
			        		  String SUPPLIER_VAL = (String) SUPPLIER.get(i);
			        		  if(DYNAMIC_SUPPLIER_DISCOUNT_VAL==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, ITEM);
				    				HM.put(IConstants.VENDNO, SUPPLIER_VAL);
				    				System.out.println("IBDISCOUNT"+IBDISCOUNT);
				    				if(IBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
				    					HM.put("IBDISCOUNT", DYNAMIC_SUPPLIER_DISCOUNT_VAL+"%");
				    									    				}
				    				else{
				    					HM.put("IBDISCOUNT", DYNAMIC_SUPPLIER_DISCOUNT_VAL);
				    									    				}
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
				    			    
				    			    //IMTHI ADD for CHILD PARENT to insert supplier discount
				    			    if(PARENT_PLANT != null){
							        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
							        	  if(CHECKplantCompany == null)
							  				CHECKplantCompany = "0";
							        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
								        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								        	  if (arrList.size() > 0) {
								        		  for (int m=0; m < arrList.size(); m++ ) {
								        			  Map map = (Map) arrList.get(m);
								        			  String childplant = (String) map.get("CHILD_PLANT");
								        			  HM.put(IConstants.PLANT, childplant);
								        			  IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
								        			  
								        			//SUPPLIER START 
								        			  if(SUPPLIER_VAL.length()>0) {
								        				ArrayList arrCust = new CustUtil().getVendorDetails(SUPPLIER_VAL,plant);
								        				if (arrCust.size() > 0) {
								        				String sCustCode = (String) arrCust.get(0);
								        				String sCustName = (String) arrCust.get(1);
								        				if (!new CustUtil().isExistVendor(SUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
								        				{
								        					String sAddr1 = (String) arrCust.get(2);
								        					String sAddr2 = (String) arrCust.get(3);
								        					String sAddr3 = (String) arrCust.get(4);
								        					String sAddr4 = (String) arrCust.get(15);
								        					String sCountry = (String) arrCust.get(5);
								        					String sZip = (String) arrCust.get(6);
								        					String sCons = (String) arrCust.get(7);
								        					String sContactName = (String) arrCust.get(8);
								        					String sDesgination = (String) arrCust.get(9);
								        					String sTelNo = (String) arrCust.get(10);
								        					String sHpNo = (String) arrCust.get(11);
								        					String sEmail = (String) arrCust.get(12);
								        					String sFax = (String) arrCust.get(13);
								        					String sRemarks = (String) arrCust.get(14);
								        					String isActive = (String) arrCust.get(16);
								        					String sPayTerms = (String) arrCust.get(17);
								        					String sPayMentTerms = (String) arrCust.get(39);
								        					String sPayInDays = (String) arrCust.get(18);
								        					String sState = (String) arrCust.get(19);
								        					String sRcbno = (String) arrCust.get(20);
								        					String CUSTOMEREMAIL = (String) arrCust.get(22);
								        					String WEBSITE = (String) arrCust.get(23);
								        					String FACEBOOK = (String) arrCust.get(24);
								        					String TWITTER = (String) arrCust.get(25);
								        					String LINKEDIN = (String) arrCust.get(26);
								        					String SKYPE = (String) arrCust.get(27);
								        					String OPENINGBALANCE = (String) arrCust.get(28);
								        					String WORKPHONE = (String) arrCust.get(29);
								        					String sTAXTREATMENT = (String) arrCust.get(30);
								        					String sCountryCode = (String) arrCust.get(31);
								        					String sBANKNAME = (String) arrCust.get(32);
								        					String sBRANCH= (String) arrCust.get(33);
								        					String sIBAN = (String) arrCust.get(34);
								        					String sBANKROUTINGCODE = (String) arrCust.get(35);
								        					String companyregnumber = (String) arrCust.get(36);
								        					String PEPPOL = (String) arrCust.get(40);
								        					String PEPPOL_ID = (String) arrCust.get(41);
								        					String CURRENCY = (String) arrCust.get(37);
//								        					String transport = (String) arrCust.get(38);
//								        					String suppliertypeid = (String) arrCust.get(21);
								        					Hashtable htsup = new Hashtable();
								        					htsup.put(IDBConstants.PLANT,childplant);
								        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
								        					htsup.put(IConstants.VENDOR_NAME, sCustName);
								        					htsup.put(IConstants.companyregnumber,companyregnumber);
								        					htsup.put("ISPEPPOL", PEPPOL);
								        					htsup.put("PEPPOL_ID", PEPPOL_ID);
								        					htsup.put("CURRENCY_ID", CURRENCY);
								        					htsup.put(IConstants.NAME, sContactName);
								        					htsup.put(IConstants.DESGINATION, sDesgination);
								        					htsup.put(IConstants.TELNO, sTelNo);
								        					htsup.put(IConstants.HPNO, sHpNo);
								        					htsup.put(IConstants.FAX, sFax);
								        					htsup.put(IConstants.EMAIL, sEmail);
								        					htsup.put(IConstants.ADDRESS1, sAddr1);
								        					htsup.put(IConstants.ADDRESS2, sAddr2);
								        					htsup.put(IConstants.ADDRESS3, sAddr3);
								        					htsup.put(IConstants.ADDRESS4, sAddr4);
								        					if(sState.equalsIgnoreCase("Select State"))
								        						sState="";
								        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
								        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
								        					htsup.put(IConstants.ZIP, sZip);
								        					htsup.put(IConstants.USERFLG1, sCons);
								        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//								        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//								        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//								        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
								        					htsup.put(IConstants.PAYTERMS, "");
								        					htsup.put(IConstants.payment_terms, "");
								        					htsup.put(IConstants.PAYINDAYS, "");
								        					htsup.put(IConstants.ISACTIVE, isActive);
//								        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//								        					htsup.put(IConstants.TRANSPORTID,transport);
								        					htsup.put(IConstants.SUPPLIERTYPEID,"");
								        					htsup.put(IConstants.TRANSPORTID, "0");
								        					htsup.put(IConstants.RCBNO, sRcbno);
								        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
								        					htsup.put(IConstants.WEBSITE,WEBSITE);
								        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
								        					htsup.put(IConstants.TWITTER,TWITTER);
								        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
								        					htsup.put(IConstants.SKYPE,SKYPE);
								        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
								        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
								        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
								        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
								        			        	  sBANKNAME="";
//								        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
								        			          htsup.put(IDBConstants.BANKNAME,"");
								        			          htsup.put(IDBConstants.IBAN,sIBAN);
								        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
								        			          htsup.put("CRAT",new DateUtils().getDateTime());
								        			          htsup.put("CRBY",username);
								        			          htsup.put("Comment1", " 0 ");
								        			          boolean custInserted = new CustUtil().insertVendor(htsup);
								        				}
								        				}
								        			  }
								        				//Supplier END
								        		  	}
								        	  	}
							        	  	}
						             	}else if(PARENT_PLANT == null){
						             		boolean ischild = false;
								        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
								        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
								        	  if (arrLi.size() > 0) {
								        	  Map mst = (Map) arrLi.get(0);
								        	  String parent = (String) mst.get("PARENT_PLANT");
								         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
								        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
								        	  if(Ischildasparent){
								        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        			  ischild = true;
								        		  }
								        	  }else{
								        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
								        		  ischild = true;
								        	  }
								        	  }
								        	  if(ischild){
								        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
									        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
									        	  String  parentplant ="";
									        	  if (arrLists.size() > 0) {
									        		  for (int j=0; j < arrLists.size(); j++ ) {
									        			  Map ms = (Map) arrLists.get(j);
									        			  parentplant = (String) ms.get("PARENT_PLANT");
									        			  HM.put(IConstants.PLANT, parentplant);
									        			  IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
									        			  
									        			//SUPPLIER START 
									        			  if(SUPPLIER_VAL.length()>0) {
									        				ArrayList arrCust = new CustUtil().getVendorDetails(SUPPLIER_VAL,plant);
									        				if (arrCust.size() > 0) {
									        				String sCustCode = (String) arrCust.get(0);
									        				String sCustName = (String) arrCust.get(1);
									        				if (!new CustUtil().isExistVendor(SUPPLIER_VAL, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
									        				{
									        					String sAddr1 = (String) arrCust.get(2);
									        					String sAddr2 = (String) arrCust.get(3);
									        					String sAddr3 = (String) arrCust.get(4);
									        					String sAddr4 = (String) arrCust.get(15);
									        					String sCountry = (String) arrCust.get(5);
									        					String sZip = (String) arrCust.get(6);
									        					String sCons = (String) arrCust.get(7);
									        					String sContactName = (String) arrCust.get(8);
									        					String sDesgination = (String) arrCust.get(9);
									        					String sTelNo = (String) arrCust.get(10);
									        					String sHpNo = (String) arrCust.get(11);
									        					String sEmail = (String) arrCust.get(12);
									        					String sFax = (String) arrCust.get(13);
									        					String sRemarks = (String) arrCust.get(14);
									        					String isActive = (String) arrCust.get(16);
									        					String sPayTerms = (String) arrCust.get(17);
									        					String sPayMentTerms = (String) arrCust.get(39);
									        					String sPayInDays = (String) arrCust.get(18);
									        					String sState = (String) arrCust.get(19);
									        					String sRcbno = (String) arrCust.get(20);
									        					String CUSTOMEREMAIL = (String) arrCust.get(22);
									        					String WEBSITE = (String) arrCust.get(23);
									        					String FACEBOOK = (String) arrCust.get(24);
									        					String TWITTER = (String) arrCust.get(25);
									        					String LINKEDIN = (String) arrCust.get(26);
									        					String SKYPE = (String) arrCust.get(27);
									        					String OPENINGBALANCE = (String) arrCust.get(28);
									        					String WORKPHONE = (String) arrCust.get(29);
									        					String sTAXTREATMENT = (String) arrCust.get(30);
									        					String sCountryCode = (String) arrCust.get(31);
									        					String sBANKNAME = (String) arrCust.get(32);
									        					String sBRANCH= (String) arrCust.get(33);
									        					String sIBAN = (String) arrCust.get(34);
									        					String sBANKROUTINGCODE = (String) arrCust.get(35);
									        					String companyregnumber = (String) arrCust.get(36);
									        					String PEPPOL = (String) arrCust.get(40);
									        					String PEPPOL_ID = (String) arrCust.get(41);
									        					String CURRENCY = (String) arrCust.get(37);
//									        					String transport = (String) arrCust.get(38);
//									        					String suppliertypeid = (String) arrCust.get(21);
									        					Hashtable htsup = new Hashtable();
									        					htsup.put(IDBConstants.PLANT,parentplant);
									        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
									        					htsup.put(IConstants.VENDOR_NAME, sCustName);
									        					htsup.put(IConstants.companyregnumber,companyregnumber);
									        					htsup.put("ISPEPPOL", PEPPOL);
									        					htsup.put("PEPPOL_ID", PEPPOL_ID);
									        					htsup.put("CURRENCY_ID", CURRENCY);
									        					htsup.put(IConstants.NAME, sContactName);
									        					htsup.put(IConstants.DESGINATION, sDesgination);
									        					htsup.put(IConstants.TELNO, sTelNo);
									        					htsup.put(IConstants.HPNO, sHpNo);
									        					htsup.put(IConstants.FAX, sFax);
									        					htsup.put(IConstants.EMAIL, sEmail);
									        					htsup.put(IConstants.ADDRESS1, sAddr1);
									        					htsup.put(IConstants.ADDRESS2, sAddr2);
									        					htsup.put(IConstants.ADDRESS3, sAddr3);
									        					htsup.put(IConstants.ADDRESS4, sAddr4);
									        					if(sState.equalsIgnoreCase("Select State"))
									        						sState="";
									        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
									        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
									        					htsup.put(IConstants.ZIP, sZip);
									        					htsup.put(IConstants.USERFLG1, sCons);
									        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//									        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//									        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//									        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
									        					htsup.put(IConstants.PAYTERMS, "");
									        					htsup.put(IConstants.payment_terms, "");
									        					htsup.put(IConstants.PAYINDAYS, "");
									        					htsup.put(IConstants.ISACTIVE, isActive);
//									        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//									        					htsup.put(IConstants.TRANSPORTID,transport);
									        					htsup.put(IConstants.SUPPLIERTYPEID,"");
									        					htsup.put(IConstants.TRANSPORTID, "0");
									        					htsup.put(IConstants.RCBNO, sRcbno);
									        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
									        					htsup.put(IConstants.WEBSITE,WEBSITE);
									        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
									        					htsup.put(IConstants.TWITTER,TWITTER);
									        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
									        					htsup.put(IConstants.SKYPE,SKYPE);
									        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
									        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
									        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
									        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
									        			        	  sBANKNAME="";
//									        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
									        			          htsup.put(IDBConstants.BANKNAME,"");
									        			          htsup.put(IDBConstants.IBAN,sIBAN);
									        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
									        			          htsup.put("CRAT",new DateUtils().getDateTime());
									        			          htsup.put("CRBY",username);
									        			          htsup.put("Comment1", " 0 ");
									        			          boolean custInserted = new CustUtil().insertVendor(htsup);
									        				}
									        				}
									        			  }
									        				//Supplier END
									        		  }
									        	  }
									        	  
									        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
									        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
									        	  Map m = new HashMap();
									        	  if (arrList.size() > 0) {
									        		  for (int k=0; k < arrList.size(); k++ ) {
									        			  m = (Map) arrList.get(k);
									        			  String childplant = (String) m.get("CHILD_PLANT");
									        			  if(childplant!=plant) {
									        				  HM.put(IConstants.PLANT, childplant);
									        				  IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
									        				  
									        				//SUPPLIER START 
									        				  if(SUPPLIER_VAL.length()>0) {
										        				ArrayList arrCust = new CustUtil().getVendorDetails(SUPPLIER_VAL,plant);
										        				if (arrCust.size() > 0) {
										        				String sCustCode = (String) arrCust.get(0);
										        				String sCustName = (String) arrCust.get(1);
										        				if (!new CustUtil().isExistVendor(SUPPLIER_VAL, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
										        				{
										        					String sAddr1 = (String) arrCust.get(2);
										        					String sAddr2 = (String) arrCust.get(3);
										        					String sAddr3 = (String) arrCust.get(4);
										        					String sAddr4 = (String) arrCust.get(15);
										        					String sCountry = (String) arrCust.get(5);
										        					String sZip = (String) arrCust.get(6);
										        					String sCons = (String) arrCust.get(7);
										        					String sContactName = (String) arrCust.get(8);
										        					String sDesgination = (String) arrCust.get(9);
										        					String sTelNo = (String) arrCust.get(10);
										        					String sHpNo = (String) arrCust.get(11);
										        					String sEmail = (String) arrCust.get(12);
										        					String sFax = (String) arrCust.get(13);
										        					String sRemarks = (String) arrCust.get(14);
										        					String isActive = (String) arrCust.get(16);
										        					String sPayTerms = (String) arrCust.get(17);
										        					String sPayMentTerms = (String) arrCust.get(39);
										        					String sPayInDays = (String) arrCust.get(18);
										        					String sState = (String) arrCust.get(19);
										        					String sRcbno = (String) arrCust.get(20);
										        					String CUSTOMEREMAIL = (String) arrCust.get(22);
										        					String WEBSITE = (String) arrCust.get(23);
										        					String FACEBOOK = (String) arrCust.get(24);
										        					String TWITTER = (String) arrCust.get(25);
										        					String LINKEDIN = (String) arrCust.get(26);
										        					String SKYPE = (String) arrCust.get(27);
										        					String OPENINGBALANCE = (String) arrCust.get(28);
										        					String WORKPHONE = (String) arrCust.get(29);
										        					String sTAXTREATMENT = (String) arrCust.get(30);
										        					String sCountryCode = (String) arrCust.get(31);
										        					String sBANKNAME = (String) arrCust.get(32);
										        					String sBRANCH= (String) arrCust.get(33);
										        					String sIBAN = (String) arrCust.get(34);
										        					String sBANKROUTINGCODE = (String) arrCust.get(35);
										        					String companyregnumber = (String) arrCust.get(36);
										        					String PEPPOL = (String) arrCust.get(40);
										        					String PEPPOL_ID = (String) arrCust.get(41);
										        					String CURRENCY = (String) arrCust.get(37);
//										        					String transport = (String) arrCust.get(38);
//										        					String suppliertypeid = (String) arrCust.get(21);
										        					Hashtable htsup = new Hashtable();
										        					htsup.put(IDBConstants.PLANT,childplant);
										        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
										        					htsup.put(IConstants.VENDOR_NAME, sCustName);
										        					htsup.put(IConstants.companyregnumber,companyregnumber);
										        					htsup.put("ISPEPPOL", PEPPOL);
										        					htsup.put("PEPPOL_ID", PEPPOL_ID);
										        					htsup.put("CURRENCY_ID", CURRENCY);
										        					htsup.put(IConstants.NAME, sContactName);
										        					htsup.put(IConstants.DESGINATION, sDesgination);
										        					htsup.put(IConstants.TELNO, sTelNo);
										        					htsup.put(IConstants.HPNO, sHpNo);
										        					htsup.put(IConstants.FAX, sFax);
										        					htsup.put(IConstants.EMAIL, sEmail);
										        					htsup.put(IConstants.ADDRESS1, sAddr1);
										        					htsup.put(IConstants.ADDRESS2, sAddr2);
										        					htsup.put(IConstants.ADDRESS3, sAddr3);
										        					htsup.put(IConstants.ADDRESS4, sAddr4);
										        					if(sState.equalsIgnoreCase("Select State"))
										        						sState="";
										        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
										        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
										        					htsup.put(IConstants.ZIP, sZip);
										        					htsup.put(IConstants.USERFLG1, sCons);
										        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
//										        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
//										        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
//										        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
										        					htsup.put(IConstants.PAYTERMS, "");
										        					htsup.put(IConstants.payment_terms, "");
										        					htsup.put(IConstants.PAYINDAYS, "");
										        					htsup.put(IConstants.ISACTIVE, isActive);
//										        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//										        					htsup.put(IConstants.TRANSPORTID,transport);
										        					htsup.put(IConstants.SUPPLIERTYPEID,"");
										        					htsup.put(IConstants.TRANSPORTID, "0");
										        					htsup.put(IConstants.RCBNO, sRcbno);
										        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
										        					htsup.put(IConstants.WEBSITE,WEBSITE);
										        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
										        					htsup.put(IConstants.TWITTER,TWITTER);
										        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
										        					htsup.put(IConstants.SKYPE,SKYPE);
										        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
										        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
										        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
										        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
										        			        	  sBANKNAME="";
//										        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
										        			          htsup.put(IDBConstants.BANKNAME,"");
										        			          htsup.put(IDBConstants.IBAN,sIBAN);
										        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
										        			          htsup.put("CRAT",new DateUtils().getDateTime());
										        			          htsup.put("CRBY",username);
										        			          htsup.put("Comment1", " 0 ");
										        			          boolean custInserted = new CustUtil().insertVendor(htsup);
										        				}
										        				}
										        				}
										        				//Supplier END
									        			  }
									        		  	}
									        	  	}
								        	  	}
								        	  }
						             	  }//IMTHI END
				    								        		
				        		}
				        	 }
			        		
			        	  
			        	  
			    		
 			           //end insert supplier discount Purchase
			            //if(OBCustomerDiscount){
			          	 inserted = mdao.insertIntoMovHis(htm);
			          	 
			          	//IMTHI ADD for CHILD PARENT to insert Activity log
				          	if(PARENT_PLANT != null){
					        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
					        	  if(CHECKplantCompany == null)
					  				CHECKplantCompany = "0";
					        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  if (arrList.size() > 0) {
						        		  for (int m=0; m < arrList.size(); m++ ) {
						        			  Map map = (Map) arrList.get(m);
						        			  String childplant = (String) map.get("CHILD_PLANT");
						        			  htm.put("PLANT",childplant);
						        			  inserted = mdao.insertIntoMovHis(htm);
						        		  	}
						        	  	}
					        	  	}
				             	}else if(PARENT_PLANT == null){
				             		boolean ischild = false;
						        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
						        	  if (arrLi.size() > 0) {
						        	  Map mst = (Map) arrLi.get(0);
						        	  String parent = (String) mst.get("PARENT_PLANT");
						         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
						        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
						        	  if(Ischildasparent){
						        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        			  ischild = true;
						        		  }
						        	  }else{
						        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
						        		  ischild = true;
						        	  }
						        	  }
						        	  if(ischild){
						        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
							        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
							        	  String  parentplant ="";
							        	  if (arrLists.size() > 0) {
							        		  for (int j=0; j < arrLists.size(); j++ ) {
							        			  Map ms = (Map) arrLists.get(j);
							        			  parentplant = (String) ms.get("PARENT_PLANT");
							        			  htm.put("PLANT",parentplant);
							        			  inserted = mdao.insertIntoMovHis(htm);
							        		  }
							        	  }
							        	  
							        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
							        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
							        	  Map m = new HashMap();
							        	  if (arrList.size() > 0) {
							        		  for (int k=0; k < arrList.size(); k++ ) {
							        			  m = (Map) arrList.get(k);
							        			  String childplant = (String) m.get("CHILD_PLANT");
							        			  if(childplant!=plant) {
							        				  htm.put("PLANT",childplant);
							        				  inserted = mdao.insertIntoMovHis(htm);
							        			  }
							        		  	}
							        	  	}
						        	  	}	
						        	  }
				             	  }//IMTHI END
			          // }
			          }
			          htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
		              htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		              
		    		  boolean updateFlag;
		    		if(ITEM!="P0001")
		      		  {	
		    			boolean exitFlag = false;
		    			Hashtable htv = new Hashtable();				
		    			htv.put(IDBConstants.PLANT, plant);
		    			htv.put(IDBConstants.TBL_FUNCTION, "PRODUCT");
		    			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
		    			if (exitFlag) 
		      		    updateFlag=_TblControlDAO.updateSeqNo("PRODUCT",plant);
		    			else
		    			{
		    				boolean insertFlag = false;
		    				Map htInsert=null;
		                	Hashtable htTblCntInsert  = new Hashtable();           
		                	htTblCntInsert.put(IDBConstants.PLANT,plant);          
		                	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
		                	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"P");
		                 	htTblCntInsert.put("MINSEQ","0000");
		                 	htTblCntInsert.put("MAXSEQ","9999");
		                	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
		                	htTblCntInsert.put(IDBConstants.CREATED_BY, username);
		                	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
		                	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
		    			}
		    		}			          
		    		DESC=strUtils.RemoveDoubleQuotesToSingle(DESC);
		          }
			          if(itemInserted && insertAlternateItem) {
			        	  DbBean.CommitTran(ut);
			        	  resultJson.put("STATUS", "SUCCESS");
			        	  resultJson.put("ITEM", ITEM);			        	  
			        	  resultJson.put("ITEM_DESC",DESC);			        	  
			        	  resultJson.put("NONSTOCKFLAG", NONSTOCKFLAG);			        	  
			        	  //resultJson.put("CATLOGPATH", catlogpath);
			        	  resultJson.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
			        	  resultJson.put("UNITPRICE", PRICE);
			        	  resultJson.put("UNITCOST", COST);
			        	  resultJson.put("SALESUOM",SALESUOM);
			        	  resultJson.put("PURCHASEUOM",PURCHASEUOM);
			        	  String TAXTREATMENT="",CURRENCY_ID="",CURRENCY="",Curramt="0";
							if(vendno.length()>0) {
							JSONObject vendorJson=new VendMstDAO().getVendorName(plant, (String) vendno);
							vendname = vendorJson.getString("VNAME");
							TAXTREATMENT = vendorJson.getString("TAXTREATMENT");
							CURRENCY_ID = vendorJson.getString("CURRENCY_ID");
							List curQryList=new ArrayList();
							curQryList = new CurrencyDAO().getCurrencyDetails(CURRENCY_ID,plant);
							for(int     j =0; j<curQryList.size(); j++) {
								ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
								CURRENCY = StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
						        Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
						        double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); 
						        Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
						   	    
						   			 
						        }
							}
							else
							{
								 List curQryList=new ArrayList();
									String Supcurrency=new PlantMstDAO().getBaseCurrency(plant);
									CURRENCY_ID=Supcurrency;
									List curQryList1=new ArrayList();
									curQryList1 = new CurrencyDAO().getCurrencyDetails(Supcurrency,plant);
									for(int     j =0; j<curQryList1.size(); j++) {
										ArrayList arrCurrLine = (ArrayList)curQryList1.get(j);
										CURRENCY = StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
								        Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
								        double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); 
								        Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
								   	    
								   			 
								        }
									
							}
							resultJson.put("vendno",vendno);
							resultJson.put("vendname",vendname);
							resultJson.put("TAXTREATMENT",TAXTREATMENT);
							resultJson.put("CURRENCY_ID",CURRENCY_ID);
							resultJson.put("CURRENCY",CURRENCY);					
							resultJson.put("CURRENCYUSEQT", Curramt);
							
							String convertedcost= new POUtil().getConvertedUnitCostForProductsCurrency(plant,CURRENCY_ID,ITEM); 
			                String convertedcostwtc=new POUtil().getConvertedUnitCostForProductWTCcurrency(plant,CURRENCY_ID,ITEM);
			                String IBDiscount=new POUtil().getIBDiscountSelectedItemVNO(plant,vendno,ITEM);
			                String discounttype="";
			                
			                int plusIndex = IBDiscount.indexOf("%");
			                if (plusIndex != -1) {
			               	 	IBDiscount = IBDiscount.substring(0, plusIndex);
			               	 	discounttype = "BYPERCENTAGE";
			                    }
							resultJson.put("ConvertedUnitCost",convertedcost);
							resultJson.put("ConvertedUnitCostWTC",convertedcostwtc);
							resultJson.put("incomingIBDiscount",IBDiscount);
							resultJson.put("IBDiscountType",discounttype);
	                        
			        	  resultJson.put("MESSAGE", "Product created successfully");
			          }			          
			          else {
			        	  DbBean.RollbackTran(ut);
			        	  resultJson.put("STATUS", "FAIL");
			        	  resultJson.put("MESSAGE", "Failed to create product");
			          }
					}
			    else{
                    DbBean.RollbackTran(ut);
                    resultJson.put("STATUS", "FAIL");
					resultJson.put("MESSAGE", "Product Exists already. Try again");
			    }			    
				}			
		}
		
		}
		catch (FileUploadException e) {
			e.printStackTrace();
			resultJson.put("STATUS", "FAIL");
			resultJson.put("MESSAGE", "Failed to create product");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			resultJson.put("STATUS", "FAIL");
			resultJson.put("MESSAGE", "Failed to create product");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultJson.put("STATUS", "FAIL");
			resultJson.put("MESSAGE", "Failed to create product");
		}
		return resultJson;
	}
	private JSONObject addAltProduct(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ItemMstUtil itemMstUtil = new ItemMstUtil();
		StrUtils strUtils = new StrUtils();
		DateUtils dateutils = new DateUtils();
		ItemUtil itemUtil = new ItemUtil();
		 String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		 String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		 String sItemId     = StrUtils.fString(request.getParameter("ITEM"));
		String sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
		String productBrandID    = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
		String sAlternateBrandItem    = StrUtils.fString(request.getParameter("ALTERNATEBRANDITEM"));
		String sAlternateBrandItemDesc      = StrUtils.fString(request.getParameter("ALTERNATEBRANDDESC"));
		String alternateProductBrandID    = StrUtils.fString(request.getParameter("ALTERNATE_PRD_BRAND_ID"));
		try{
			////////////////
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);				
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			List altPrdItemList=new ArrayList<>();
			List altPrdDescList=new ArrayList<>();
			List altPrdBrandList=new ArrayList<>();
			int altPrdItemCount=0,altPrdDescCount=0,altPrdBrandCount=0;
			while (iterator.hasNext()) {
				FileItem fileItem = (FileItem) iterator.next();
				/* BillHdr*/
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("ITEM")) {
						sItemId = StrUtils.fString(fileItem.getString()).trim();
					}
					if (fileItem.getFieldName().equalsIgnoreCase("DESC")) {
						sItemDesc = StrUtils.fString(fileItem.getString()).trim();
					}
					if (fileItem.getFieldName().equalsIgnoreCase("PRD_BRAND_ID")) {
						productBrandID = StrUtils.fString(fileItem.getString()).trim();
					}
					if (fileItem.getFieldName().equalsIgnoreCase("ALTERNATEBRANDITEM")) {
						altPrdItemList.add(altPrdItemCount, StrUtils.fString(fileItem.getString()).trim());
						altPrdItemCount++;
					}
					if (fileItem.getFieldName().equalsIgnoreCase("ALTERNATEBRANDDESC")) {
						altPrdDescList.add(altPrdDescCount, StrUtils.fString(fileItem.getString()).trim());
						altPrdDescCount++;
					}
					if (fileItem.getFieldName().equalsIgnoreCase("ALTERNATE_PRD_BRAND_ID")) {
						altPrdBrandList.add(altPrdBrandCount, StrUtils.fString(fileItem.getString()).trim());
						altPrdBrandCount++;
					}
				}
			}
			

			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.ITEM, sItemId);
			ht.put(IDBConstants.PRDBRANDID, productBrandID);
			for(int i =0 ; i < altPrdItemList.size() ; i++){
		     	ht.clear();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.ITEM, sItemId);
				ht.put(IDBConstants.PRDBRANDID, productBrandID);
				ht.put(IDBConstants.ALTERNATE_ITEM_NAME, (String) altPrdItemList.get(i));
				ht.put(IDBConstants.ALTERNATEPRDBRANDID, (String) altPrdBrandList.get(i));
				ht.put(IConstants.ISACTIVE, "Y");
				ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				ht.put(IDBConstants.LOGIN_USER, username);
	
				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", plant);
				htm.put("DIRTYPE", TransactionConstants.ADD_ALTERNATE_BRAND_PRODUCT);
				htm.put("RECID", "");
				htm.put("ITEM",altPrdItemList.get(i));
				htm.put("REMARKS",strUtils.InsertQuotes("Main Product:" +sItemId));
				htm.put("UPBY", username);
				htm.put("CRBY", username);
				htm.put("CRAT", dateutils.getDateTime());
				htm.put("UPAT", dateutils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				boolean itemInserted = itemUtil.insertAlternateBrandItem(ht);
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
			response.sendRedirect("../alternateproduct/summary");
			}
		}
		catch (Exception e) {
			 
			 e.printStackTrace();
			 //response.sendRedirect("jsp/createBill.jsp?action="+paction+"&PONO="+pono+"&GRNO="+grno+"&VEND_NAME="+vendname+"&VENDNO="+vendno+"&result="+e.toString());
		}
		 
		return resultJson;
	}
	private JSONObject updateAltProduct(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ItemMstUtil itemMstUtil = new ItemMstUtil();
		StrUtils strUtils = new StrUtils();
		DateUtils dateutils = new DateUtils();
		ItemUtil itemUtil = new ItemUtil();
		 String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		 String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		 String sItemId     = StrUtils.fString(request.getParameter("ITEM"));
		String sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
		String productBrandID    = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
		String sAlternateBrandItem    = StrUtils.fString(request.getParameter("ALTERNATEBRANDITEM"));
		String sAlternateBrandItemDesc      = StrUtils.fString(request.getParameter("ALTERNATEBRANDDESC"));
		String alternateProductBrandID    = StrUtils.fString(request.getParameter("ALTERNATE_PRD_BRAND_ID"));
		try{
			////////////////
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);				
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			List altPrdItemList=new ArrayList<>();
			List altPrdDescList=new ArrayList<>();
			List altPrdBrandList=new ArrayList<>();
			int altPrdItemCount=0,altPrdDescCount=0,altPrdBrandCount=0;
			while (iterator.hasNext()) {
				FileItem fileItem = (FileItem) iterator.next();
				/* BillHdr*/
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("ITEM")) {
						sItemId = StrUtils.fString(fileItem.getString()).trim();
					}
					if (fileItem.getFieldName().equalsIgnoreCase("DESC")) {
						sItemDesc = StrUtils.fString(fileItem.getString()).trim();
					}
					if (fileItem.getFieldName().equalsIgnoreCase("PRD_BRAND_ID")) {
						productBrandID = StrUtils.fString(fileItem.getString()).trim();
					}
					if (fileItem.getFieldName().equalsIgnoreCase("ALTERNATEBRANDITEM")) {
						altPrdItemList.add(altPrdItemCount, StrUtils.fString(fileItem.getString()).trim());
						altPrdItemCount++;
					}
					if (fileItem.getFieldName().equalsIgnoreCase("ALTERNATEBRANDDESC")) {
						altPrdDescList.add(altPrdDescCount, StrUtils.fString(fileItem.getString()).trim());
						altPrdDescCount++;
					}
					if (fileItem.getFieldName().equalsIgnoreCase("ALTERNATE_PRD_BRAND_ID")) {
						altPrdBrandList.add(altPrdBrandCount, StrUtils.fString(fileItem.getString()).trim());
						altPrdBrandCount++;
					}
				}
			}
			Hashtable dht = new Hashtable();
			dht.put(IDBConstants.PLANT, plant);
			dht.put(IDBConstants.ITEM, sItemId);
			itemUtil.deleteAlternateBrandItem(dht);
				
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.ITEM, sItemId);
			ht.put(IDBConstants.PRDBRANDID, productBrandID);
			for(int i =0 ; i < altPrdItemList.size() ; i++){
		     	ht.clear();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.ITEM, sItemId);
				ht.put(IDBConstants.PRDBRANDID, productBrandID);
				ht.put(IDBConstants.ALTERNATE_ITEM_NAME, (String) altPrdItemList.get(i));
				ht.put(IDBConstants.ALTERNATEPRDBRANDID, (String) altPrdBrandList.get(i));
				ht.put(IConstants.ISACTIVE, "Y");
				ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				ht.put(IDBConstants.LOGIN_USER, username);
	
				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", plant);
				htm.put("DIRTYPE", TransactionConstants.ADD_ALTERNATE_BRAND_PRODUCT);
				htm.put("RECID", "");
				htm.put("ITEM",altPrdItemList.get(i));
				htm.put("REMARKS",strUtils.InsertQuotes("Main Product:" +sItemId));
				htm.put("UPBY", username);
				htm.put("CRBY", username);
				htm.put("CRAT", dateutils.getDateTime());
				htm.put("UPAT", dateutils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				
				
				boolean itemInserted = itemUtil.insertAlternateBrandItem(ht);
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
			response.sendRedirect("../alternateproduct/summary?Result='OK'");
			}
		}
		catch (Exception e) {
			 
			 e.printStackTrace();
			 //response.sendRedirect("jsp/createBill.jsp?action="+paction+"&PONO="+pono+"&GRNO="+grno+"&VEND_NAME="+vendname+"&VENDNO="+vendno+"&result="+e.toString());
		}
		 
		return resultJson;
	}
	private JSONObject summaryAltProduct(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JSONObject resultJsonArr = new JSONObject();
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils = new StrUtils();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		 String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		 String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		 String sItemId     = StrUtils.fString(request.getParameter("ITEM"));
		String sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
		ArrayList result=itemUtil.getAltItemList(plant, sItemId, sItemDesc);
		if(result.size()>0)
		{
			result.forEach(m->{
				JSONObject resultJsonInt = new JSONObject();
				Map item=(Map) m; 
				resultJsonInt.put("ITEM", strUtils.fString((String) item.get("ITEM")));
				resultJsonInt.put("ITEMDESC", strUtils.fString((String) item.get("ITEMDESC")));
				resultJsonInt.put("BRAND", strUtils.fString((String) item.get("PRD_BRAND_ID")));
				resultJsonInt.put("VINNO", strUtils.fString((String) item.get("VINNO")));
				resultJsonInt.put("MODEL", strUtils.fString((String) item.get("MODEL")));
				jsonArray.add(resultJsonInt);
			});
			resultJson.put("items", jsonArray);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("errors", jsonArrayErr);
		}
		
	else {
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
		resultJsonInt.put("ERROR_CODE", "99");
		jsonArrayErr.add(resultJsonInt);
		jsonArray.add("");
		resultJson.put("items", jsonArray);
		resultJson.put("errors", jsonArrayErr);
		
	}
		return resultJson;
	}
	private JSONObject getNextSequence(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArrayErr = new JSONArray();
		
		boolean insertFlag=false;
	    String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
	    String sItem="", sBatchSeq="", sZero="";
	  	TblControlDAO _TblControlDAO =new TblControlDAO();
	  	_TblControlDAO.setmLogger(mLogger);
	    Hashtable  ht=new Hashtable();
	    String query=" isnull(NXTSEQ,'') as NXTSEQ";
	    ht.put(IDBConstants.PLANT,plant);
	    ht.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
		try {
		       boolean exitFlag=false; boolean resultflag=false;
		       exitFlag=_TblControlDAO.isExisit(ht,"",plant);		   
		     //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
		      if (exitFlag==false)
		      {         
		            Map htInsert=null;
		            Hashtable htTblCntInsert  = new Hashtable();
		            htTblCntInsert.put(IDBConstants.PLANT,plant);
		            htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
		            htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"P");
		            htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"0000");
		            htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"9999");
		            htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
		            htTblCntInsert.put(IDBConstants.CREATED_BY, username);
		            htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
		            insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
		            sItem="P"+"0001";
		      }
		      else
		      {
		           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
		           Map m= _TblControlDAO.selectRow(query, ht,"");
		           sBatchSeq=(String)m.get("NXTSEQ");
		           int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
		           String updatedSeq=Integer.toString(inxtSeq);
		            if(updatedSeq.length()==1)
		           {
		             sZero="000";
		           }
		           else if(updatedSeq.length()==2)
		           {
		             sZero="00";
		           }
		           else if(updatedSeq.length()==3)
		           {
		             sZero="0";
		           }
		            Map htUpdate = null;
		           Hashtable htTblCntUpdate = new Hashtable();
		           htTblCntUpdate.put(IDBConstants.PLANT,plant);
		           htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
		           htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"P");
		           StringBuffer updateQyery=new StringBuffer("set ");
		           updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
		        //   boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
		           sItem="P"+sZero+updatedSeq;
		        }
		      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
		      resultJson.put("ERROR_CODE", "100");
              resultJson.put("ITEM", sItem);
		} catch (Exception e) {
	        resultJson.put("ERROR_MESSAGE",  e.getMessage());
	        resultJson.put("ERROR_CODE", "98");
		}
		return resultJson;
	}
	
	private JSONObject validatenoofProduct(String plant, String NOOFINVENTORY) {
		JSONObject resultJson = new JSONObject();
		try {
			
			//Validate no.of Product -- Azees 19.11.2020
			ItemMstDAO itemMstDAO= new ItemMstDAO();
			
			int novalid =itemMstDAO.Itemcount(plant);
			if(!NOOFINVENTORY.equalsIgnoreCase("Unlimited"))
			{
				int convl = Integer.valueOf(NOOFINVENTORY);
				if(novalid>=convl)
				{
					resultJson.put("status", "100");
					resultJson.put("ValidNumber", NOOFINVENTORY);
				}
				else
				{
					resultJson.put("status", "99");
					
				}
			}
			else
				resultJson.put("status", "99");
			
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	 private BufferedImage scaleImage(BufferedImage bufferedImage, int size) {
         double boundSize = size;
            int origWidth = bufferedImage.getWidth();
            int origHeight = bufferedImage.getHeight();
            double scale;
            if (origHeight > origWidth)
                scale = boundSize / origHeight;
            else
                scale = boundSize / origWidth;
             //* Don't scale up small images.
            if (scale > 1.0)
                return (bufferedImage);
            int scaledWidth = (int) (scale * origWidth);
            int scaledHeight = (int) (scale * origHeight);
            Image scaledImage = bufferedImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            // new ImageIcon(image); // load image
            // scaledWidth = scaledImage.getWidth(null);
            // scaledHeight = scaledImage.getHeight(null);
            BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaledBI.createGraphics();
                 g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();
            return (scaledBI);
    }
	 
	 private void saveCompressedImage(BufferedImage image, String formatName, File outputFile) throws IOException {
	        // Create an ImageWriter for the specified format
	        Iterator<javax.imageio.ImageWriter> writers = javax.imageio.ImageIO.getImageWritersByFormatName(formatName);
	        if (!writers.hasNext()) {
	            throw new IllegalStateException("No writers found for format: " + formatName);
	        }
	        javax.imageio.ImageWriter writer = writers.next();

	        // Create ImageWriteParam to compress the image
	        javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
	        if (param.canWriteCompressed()) {
	            param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
	            param.setCompressionQuality(0.25f); // Set compression quality (0.0 - 1.0)
	        }

	        // Create the output stream and write the image
	        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
	        		javax.imageio.stream.ImageOutputStream ios = javax.imageio.ImageIO.createImageOutputStream(fos)) {
	            writer.setOutput(ios);
	            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
	        } finally {
	            writer.dispose();
	        }
	    }
}