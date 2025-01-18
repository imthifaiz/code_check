package com.track.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.track.constants.MLoggerConstant;
import com.track.dao.CurrencyDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.ReportSesBeanDAO;
import com.track.dao.UomDAO;
import com.track.db.object.ItemQtyBigDecimalPojo;
import com.track.db.object.ItemQtyPojo;
import com.track.db.object.OpenCloseReportPojo;
import com.track.db.util.DOUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.PackingUtil;
import com.track.db.util.StockTakeUtil;
import com.track.db.util.UserLocUtil;
//import com.track.db.util.WipUtil;
import com.track.gates.sqlBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;


/**
 * Servlet implementation class InvMstServlet
 */
public class InvMstServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.InvMstServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.InvMstServlet_PRINTPLANTMASTERINFO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InvMstServlet() {
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
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String baseCurrency = StrUtils.fString(
				(String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
                System.out.println("action ::"+action);
                
	    if (action.equals("VIEW_INV_SUMMARY_WO_PRICE")) {
	        jsonObjectResult = this.getInventorysmryDetailsWoPrice(request);
	            
	    }
	    if (action.equals("VIEW_INV_SUMMARY_WO_PRICE_MULTIUOM")) {
	        jsonObjectResult = this.getInventorysmryDetailsWoPriceMultiUOM(request);
	       //jsonObjectResult = this.getInventorysmryDetailsWoPriceTest(request);
	       
	    }
	    if (action.equals("VIEW_MANUALSTK_SUMMARY")) {
	    	jsonObjectResult = this.getManualStksmryDetails(request);
	    	
	    }
	    if (action.equals("VIEW_INV_SUMMARY_AVG_COST")) {
	        jsonObjectResult = this.getInventorysmryDetailsWithAverageCost(request,baseCurrency);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_AVG_COST_COG")) {
	        //jsonObjectResult = this.getInventorysmryDetailsWithAverageCostCog(request,baseCurrency);
	    	jsonObjectResult = this.getInventorysmryDetailsWithAverageCost(request,baseCurrency);
	    }
	    if (action.equals("VIEW_PRODUCT_SUMMARY_COST_PRICE_INVENTORY")) {
	    	jsonObjectResult = this.getProductSummaryCostPriceInventory(request,baseCurrency);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_OPEN_CLOSE_REPORT")) {
	    	jsonObjectResult = this.getInventorysmryopenclose(request,baseCurrency);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_OPEN_CLOSE_AVG_COST_REPORT")) {
	    	jsonObjectResult = this.getInventorysmryopencloseavgcost(request,baseCurrency);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_RECEIVED_ISUUED_REPORT")) {
	    	jsonObjectResult = this.getInventorysmryincomingoutgoingqty(request,baseCurrency);
	    }
            
	    if (action.equals("VIEW_INV_SUMMARY_MIN_QTY")) {
	        jsonObjectResult = this.getInventorysmryDetailsWMinQty(request);
	    }
	    
	    if (action.equals("VIEW_INV_SUMMARY_GROUPBY_PRD")) {
	        jsonObjectResult = this.getInventorysmryGroupByProd(request);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_GROUPBY_PRD_MULTIUOM")) {
	        jsonObjectResult = this.getInventorysmryGroupByProdMultiUOM(request);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_GROUPBY_PRD_MULTIUOM_NEW")) {
	    	jsonObjectResult = this.getInventorysmryGroupByProdMultiUOMNEW(request);
	    }
	    if (action.equals("VIEW_INV_VALUATION_SUMMARY")) {
	    	jsonObjectResult = this.getInventoryValuationsmry(request);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_OPEN_CLOSE_BAL")) {
	        jsonObjectResult = this.getInventorysmryForOpenCloseBal(request);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_OPEN_CLOSE_BAL_LOC")) {
	        jsonObjectResult = this.getInventorysmryForOpenCloseBalByLoc(request);
	    }
	    if (action.equals("VIEW_OPEN_CLOSE_BAL_INV_WAVGCOST")) {
	        jsonObjectResult = this.getInventorysmryForOpenCloseBalWithAvgCost(request);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_MOVEMENT_REPORT")) {
	    	jsonObjectResult = this.getInventorysmryMovHis(request);
	    }
	    if (action.equals("VIEW_INV_SUMMARY_GROUPBY_PRD_MOVEMENT")) {
	    	jsonObjectResult = this.getInventoryByPrdsmryMovHis(request);
	    }
	    
	    
	    if (action.equals("VIEW_INV_SUMMARY_WITH_EXPIRYDT")) {
	        jsonObjectResult = this.getInventorysmryDetailsWithExpiry(request);
	    }
	    if (action.equals("VIEW_CONTAINER_SUMMARY")) {
	        jsonObjectResult = this.getContainerSummary(request);
	    }
	    if (action.equals("VIEW_STOCK_TAKE_SUMMARY")) {
	        jsonObjectResult = this.getStockTakeSummary(request);
	     	       
	    }
	    
	    if (action.equals("VIEW_STOCK_TAKE_SUMMARY_WITHCOST")) {
	        jsonObjectResult = this.getStockTakeSummaryWithCost(request);
	     	       
	    }
	    
	    
	    if (action.equals("VIEW_INV_SUMMARY_LOC_ZEROQTY")) {
	        jsonObjectResult = this.getInventorysmryLocZeroQty(request);
	    }
	    
	    if (action.equals("VIEW_INV_SUMMARY_AGING_DAYS")) {
	        jsonObjectResult = this.getInventorysmryAgingDetailsByDays(request);
	    }
	    if (action.equals("VIEW_INV_VS_OUTGOING")) {
	        jsonObjectResult = this.getInventoryVsOutgoing(request);
	    }
	    
	    if (action.equals("VIEW_INV_SUMMARY_ALTERNATE_BRAND")) {
	        jsonObjectResult = this.getInventorySummaryAlternateBrand(request);
	    }
	    
	       if (action.equals("VIEW_PACKING_SMRY")) {
	        jsonObjectResult = this.getPackingIssue(request);
	       
	    }
	     
	    if (action.equals("VIEW_SUMMARY_ALTERNATE_BRAND")) {
	        jsonObjectResult = this.getSummaryAlternateBrand(request);
	    }
	    if (action.equals("GET_LOCATION_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getLocationListForSuggestion(request);
        }
	    if (action.equals("GET_BATCH_LIST_FOR_SUGGESTION")) {
			 jsonObjectResult = this.getInvBatchListForSuggestion(request);
	    }
	    if (action.equals("ADD_ESTIMATE_LIST")) {
			 jsonObjectResult = this.addToEstimateList(request);
	    }
	    if (action.equals("DELETE_ESTIMATE_LIST")) {
			 jsonObjectResult = this.deleteEstimateList(request);
	    }
	    if (action.equals("GET_AVAILABLE_QTY")) {
			 jsonObjectResult = this.getAvailableQty(request);
	    }
	    if (action.equals("GET_INV_LIST_SUMMARY_WITH_AVLQTY_MULTI_UOM")) {
			 jsonObjectResult = this.getInvListSummaryWithAvlQtyMultiUom(request);
	    }	    
	    
			response.setContentType("application/json");
			//((ServletRequest) response).setCharacterEncoding("UTF-8");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	

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
    
    
    private JSONObject getInventorysmryDetailsWoPriceTest(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
           
            StrUtils strUtils = new StrUtils();
         
            try {
            
               String PLANT= strUtils.fString(request.getParameter("PLANT"));
               String LOC     = strUtils.fString(request.getParameter("LOC"));
               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
               String BATCH   = strUtils.fString(request.getParameter("BATCH"));
               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
               String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
               String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
               String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
               String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
       
               Hashtable ht = new Hashtable();

                if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
                //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
                if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
                if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
                if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
             
                ArrayList invQryList= new InvUtil().getInvListSummaryWithOutPrice(ht,PLANT,ITEM,PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID3,"",LOC);//"","","","",plant,"");
                if (invQryList.size() > 0) {
                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                          
                                 
                           
                                Map lineArr = (Map) invQryList.get(iCnt);
                                JSONObject resultJsonInt = new JSONObject();
                                resultJsonInt.put("PRODUCT", strUtils.fString((String)lineArr.get("ITEM")));
                                 resultJsonInt.put("LOC", strUtils.fString((String)lineArr.get("LOC")));
                                 resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
                                 resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
                                 resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String)lineArr.get("ITEMDESC"))));
                                 resultJsonInt.put("UOM", strUtils.fString((String)lineArr.get("STKUOM")));
                                 resultJsonInt.put("BATCH",strUtils.fString((String)lineArr.get("BATCH")));
                                 resultJsonInt.put("QTY",strUtils.formatNum((String)lineArr.get("QTY")));

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
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
        
    private JSONObject getInventorysmryDetailsWoPrice(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
           
            StrUtils strUtils = new StrUtils();
          //  String PLANT="",ITEM="",ITEM_DESC="",PRD_CLS_ID="",PRD_TYPE_ID="",COND="",LOC="",BATCH=""   ;
            try {
            
               String PLANT= strUtils.fString(request.getParameter("PLANT"));
               String LOC     = strUtils.fString(request.getParameter("LOC"));
               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
               String BATCH   = strUtils.fString(request.getParameter("BATCH"));
               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
               String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
               String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
               String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
               String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
               String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
               String  LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
               String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
               String  MODEL = strUtils.fString(request.getParameter("MODEL"));
               Hashtable ht = new Hashtable();

                if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
                if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
                if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
                if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
                if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
                if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
                if(strUtils.fString(MODEL).length() > 0)        ht.put("b.MODEL",MODEL) ; 
                ArrayList invQryList= new InvUtil().getInvListSummaryWithOutPrice(ht,PLANT,ITEM,PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC);//"","","","",plant,"");
                if (invQryList.size() > 0) {
                int iIndex = 0;
                 int irow = 0;
                double sumprdQty = 0;String lastProduct="";
                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                          
                                 
                                String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
                                String item =(String)lineArr.get("ITEM");
                                String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                                JSONObject resultJsonInt = new JSONObject();
                           
                      //     if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(item))){
                        	   resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM")));
                        	   resultJsonInt.put("LOC", strUtils.fString((String)lineArr.get("LOC")));
                        	   resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
                        	   resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String)lineArr.get("PRD_DEPT_ID")));
                        	   resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
                        	   resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("PRD_BRAND_ID")));
                        	   resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String)lineArr.get("ITEMDESC"))));
                        	   resultJsonInt.put("STKUOM", strUtils.fString((String)lineArr.get("STKUOM")));
                        	   resultJsonInt.put("BATCH", strUtils.fString((String)lineArr.get("BATCH")));
                        	  // resultJsonInt.put("QTY", strUtils.formatNum((String)lineArr.get("QTY")));
								resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));                           	  
							  jsonArray.add(resultJsonInt);
                    }                    
                                          // End code modified by Bruhan for product brand on 11/9/12 
                 
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
            		jsonArray.add("");
            		resultJson.put("items", jsonArray);
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getInventorysmryDetailsWMinQty(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
           
            StrUtils strUtils = new StrUtils();
          //  String PLANT="",ITEM="",ITEM_DESC="",PRD_CLS_ID="",PRD_TYPE_ID="",COND="",LOC="",BATCH=""   ;
            try {
            
               String PLANT= strUtils.fString(request.getParameter("PLANT"));
               String LOC     = strUtils.fString(request.getParameter("LOC"));
               String LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
               String LOC_TYPE_ID2     = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
               String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
               String SUPPLIER = strUtils.fString(request.getParameter("CUSTOMER"));
               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
               String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
               String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
               String  PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
               String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
               String VIEWSTATUS = strUtils.fString(request.getParameter("VIEWSTATUS"));
               String TOPUPBY = strUtils.fString(request.getParameter("TOPUPBY"));
               String UOM = strUtils.fString(request.getParameter("UOM"));
               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
               
               String PARENT_PLANT1 ="";
               String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENTBYCHILD(PLANT);
               if(PARENT_PLANT==null)
            		PARENT_PLANT="";
               else {
            	   List CompanyCompanyByparent = new ParentChildCmpDetDAO().getChildCompanyByparent(PARENT_PLANT);
            	   for(int i =0; i<CompanyCompanyByparent.size(); i++) {
            	 		Map Comp = (Map)CompanyCompanyByparent.get(i);
            	 		String childComp = (String)Comp.get("CHILD_PLANT");
            	 		
            		if(childComp.equals("C4376460171S2T"))
            			PARENT_PLANT1=childComp;
            	   }
               }
              
               Hashtable ht = new Hashtable();

                if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
                if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
                if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("ITEMTYPE",PRD_TYPE_ID) ;
                if(strUtils.fString(PRD_BRAND_ID).length() > 0)         ht.put("PRD_BRAND_ID",PRD_BRAND_ID) ;  
                if(strUtils.fString(PRD_DEPT_ID).length() > 0)         ht.put("PRD_DEPT_ID",PRD_DEPT_ID) ;  
				/*
				 * if(strUtils.fString(SUPPLIER).length() > 0) ht.put("VENDNO",SUPPLIER) ;
				 * if(SUPPLIER.length()>0){ SUPPLIER = new StrUtils().InsertQuotes(sItemDesc);
				 * SUPPLIER= " AND VNAME like '%"+SUPPLIER+"%'"; } Ar
				 */ArrayList invQryList= new ArrayList();
                if(PARENT_PLANT.equalsIgnoreCase(""))
                	invQryList= new InvUtil().getInvListSummaryWithMinStock(ht,PLANT,ITEM,PRD_DESCRIP,false,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,VIEWSTATUS,UOM,SUPPLIER);//"","","","",plant,"");
                else	
                	invQryList= new InvUtil().getInvListSummaryWithMinStock(ht,PLANT,ITEM,PRD_DESCRIP,false,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,VIEWSTATUS,UOM,PARENT_PLANT,SUPPLIER,PARENT_PLANT1);//"","","","",plant,"");
                
                if (invQryList.size() > 0) {
                 int iIndex = 0;
                 int irow = 0;
                 double sumprdQty = 0,stkqty=0;String lastProduct="";double lastProdStkQty=0;
                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                                String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                String item =(String)lineArr.get("ITEM");
                                String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                                JSONObject resultJsonInt = new JSONObject();
                                resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM")));
                                resultJsonInt.put("LOC", strUtils.fString((String)lineArr.get("LOC")));
                                resultJsonInt.put("LOCDESC", strUtils.fString((String)lineArr.get("LOCDESC")));
                                resultJsonInt.put("LOCTYPE", strUtils.fString((String)lineArr.get("LOC_TYPE_ID")));
                                resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
                                resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String)lineArr.get("PRD_DEPT_ID")));
                                resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
                                resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("PRD_BRAND_ID")));
                                resultJsonInt.put("ITEMDESC", strUtils.fString((String)lineArr.get("ITEMDESC")));
                                resultJsonInt.put("STKUOM", strUtils.fString((String)lineArr.get("STKUOM")));
                                resultJsonInt.put("VNAME", strUtils.fString((String)lineArr.get("VNAME")));
                                //resultJsonInt.put("MINQTY", strUtils.fString((String)lineArr.get("MINQTY")));
                                //resultJsonInt.put("MAXQTY", strUtils.fString((String)lineArr.get("MAXQTY")));
                                resultJsonInt.put("QTY", strUtils.fString((String)lineArr.get("QTY")));
                                resultJsonInt.put("INVENTORYUOM", strUtils.fString((String)lineArr.get("INVENTORYUOM")));
//                                resultJsonInt.put("INVMINQTY", strUtils.fString((String)lineArr.get("INVMINQTY")));
//                                resultJsonInt.put("INVMAXQTY", strUtils.fString((String)lineArr.get("INVMAXQTY")));
//                                resultJsonInt.put("INVQTY", strUtils.fString((String)lineArr.get("INVQTY")));
                                
                                double MAXQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("MAXQTY")));
                                double MINQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("MINQTY")));
                                double INVMAXQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVMAXQTY")));
                                double INVMINQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVMINQTY")));
                                double INVQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVQTY")));
                                double PARENTINVQTY = 0.0;
                                if(!PARENT_PLANT.equalsIgnoreCase(""))
                                	PARENTINVQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("PARENT_INVQTY")));
                                double PARENTINVQTY1 = 0.0;
                                if(!PARENT_PLANT1.equalsIgnoreCase(""))
                                	PARENTINVQTY1 = Double.valueOf(StrUtils.fString((String)lineArr.get("PARENT_INVQTY1")));
                                double cost = Double.valueOf(StrUtils.fString((String)lineArr.get("TOTCOST")));
                               // double TOPUP_QTY = MINQTY-INVQTY;
                                double TOPUP_QTY = 0;
                                if(TOPUPBY.equals("ByMaxQty"))
                                	TOPUP_QTY = MAXQTY-INVQTY;
                                else if(TOPUPBY.equals("ByMinQty"))
                                	TOPUP_QTY = MINQTY-INVQTY;
                                /*if (TOPUP_QTY < 0) {
                                	TOPUP_QTY = 0;
                                }*/
                                double PARENT_TOPUP_QTY = 0;
                                    if(TOPUPBY.equals("ByMaxQty"))
                                    	//PARENT_TOPUP_QTY = MAXQTY-(INVQTY+PARENTINVQTY);
										PARENT_TOPUP_QTY = (MAXQTY-INVQTY);
                                    else if(TOPUPBY.equals("ByMinQty"))
                                    	//PARENT_TOPUP_QTY = MINQTY-(INVQTY+PARENTINVQTY);
										PARENT_TOPUP_QTY = (MINQTY-INVQTY);
                                /*if (PARENT_TOPUP_QTY < 0) {
                                	PARENT_TOPUP_QTY = 0;
                                }*/
                                
//                                PARENT_TOPUP_QTY = Math.abs(PARENT_TOPUP_QTY);
                                    
                                double invcost = cost*INVQTY;
                                double mincost= cost*INVMINQTY;
        			            resultJsonInt.put("MINQTY",StrUtils.addZeroes(MINQTY, "3"));
        			            resultJsonInt.put("MAXQTY",StrUtils.addZeroes(MAXQTY, "3"));
        			            resultJsonInt.put("INVMINQTY",StrUtils.addZeroes(INVMINQTY, "3"));
        			            resultJsonInt.put("INVMAXQTY",StrUtils.addZeroes(INVMAXQTY, "3"));
        			            resultJsonInt.put("INVQTY",StrUtils.addZeroes(INVQTY, "3"));
        			            resultJsonInt.put("PARENTINVQTY",StrUtils.addZeroes(PARENTINVQTY, "3"));
        			            resultJsonInt.put("PARENTINVQTY1",StrUtils.addZeroes(PARENTINVQTY1, "3"));
        			            resultJsonInt.put("TOTALCOST",StrUtils.addZeroes(invcost, numberOfDecimal));
        			            resultJsonInt.put("INVCOST",StrUtils.addZeroes(mincost, numberOfDecimal));
        			            resultJsonInt.put("REVENUECOST",StrUtils.addZeroes((invcost-mincost), numberOfDecimal));
        			            resultJsonInt.put("TOPUP_QTY",StrUtils.addZeroes(TOPUP_QTY, "3"));
        			            
        			         /*  if(INVMINQTY==INVQTY) {
        			            	PARENT_TOPUP_QTY = MAXQTY-INVQTY;
        			            }else if(INVMINQTY>INVQTY) {
        			            	PARENT_TOPUP_QTY = MAXQTY-INVQTY;
        			            }else if(INVMAXQTY<INVQTY) {
        			            	PARENT_TOPUP_QTY = MAXQTY-INVQTY;
//        			            	PARENT_TOPUP_QTY = Math.abs(PARENT_TOPUP_QTY);
        			            	INVMINQTY = INVQTY+INVQTY;
        			            }
        			            resultJsonInt.put("PARENT_TOPUP_QTY",StrUtils.addZeroes(PARENT_TOPUP_QTY, "3"));
//        			            if (PARENT_TOPUP_QTY>0) {
        			            	if(INVMINQTY>INVQTY) {
        			            if(!PARENT_PLANT.equalsIgnoreCase("")) {                                
                                if(PARENT_TOPUP_QTY!=0)
                                	if((PARENTINVQTY+PARENTINVQTY1)!=0)                                	
                            	   jsonArray.add(resultJsonInt);
        			            } else
                                jsonArray.add(resultJsonInt);
        			            	}
//        			            } */
        			            
        			            String status="";
        			            if(INVMINQTY==INVQTY) {
        			            	PARENT_TOPUP_QTY = MAXQTY-INVQTY;
        			            }else if(INVMAXQTY<INVQTY && INVMINQTY<INVQTY) {
        			            	PARENT_TOPUP_QTY = MAXQTY-INVQTY;
        			            	status = "show";
        			            }else if(INVMINQTY<INVQTY) {
        			            	status = "hide";
        			            }else if(INVMINQTY>INVQTY) {
        			            	PARENT_TOPUP_QTY = MAXQTY-INVQTY;
        			            }
        			            
        			            resultJsonInt.put("PARENT_TOPUP_QTY",StrUtils.addZeroes(PARENT_TOPUP_QTY, "3"));
        			            if(!status.equalsIgnoreCase("hide")) {
        			            	
        			            if(!PARENT_PLANT.equalsIgnoreCase("")) {                                
                                if(PARENT_TOPUP_QTY!=0)
                                	if((PARENTINVQTY+PARENTINVQTY1)!=0)                                	
                            	   jsonArray.add(resultJsonInt);
        			            } else
                                jsonArray.add(resultJsonInt);
        			            }
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
            	jsonArray.add("");
        		resultJson.put("items", jsonArray);
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getInventorysmryDetailsWithExpiry(HttpServletRequest request) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
           
            StrUtils strUtils = new StrUtils();
       
            try {
            
               String PLANT= strUtils.fString(request.getParameter("PLANT"));
               String USERID= strUtils.fString(request.getParameter("LOGIN_USER"));
               String LOC     = strUtils.fString(request.getParameter("LOC"));
               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
               String BATCH   = strUtils.fString(request.getParameter("BATCH"));
               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
               String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
               String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
               // Start code modified by Bruhan for product brand on 11/9/12 
               String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
               String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
               String EXPIREDATE = strUtils.fString(request.getParameter("EXPIREDATE"));
               String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
               String  LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
               String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
               String  VIEWSTATUS = strUtils.fString(request.getParameter("VIEWSTATUS"));
               String  UOM = strUtils.fString(request.getParameter("UOM"));
               
               Hashtable ht = new Hashtable();

                if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
                if(strUtils.fString(ITEM).length() > 0)              ht.put("a.ITEM",ITEM);
                if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
                if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
                if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;
                if(strUtils.fString(PRD_BRAND_ID).length() > 0)         ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ;
                if(strUtils.fString(PRD_DEPT_ID).length() > 0)         ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ;
                                   
                ArrayList invQryList= new InvUtil().getInvListSummaryWithExpireDate(ht,PLANT,ITEM,PRD_DESCRIP,EXPIREDATE,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,VIEWSTATUS,UOM);
                if (invQryList.size() > 0) {
                int iIndex = 0;
                 int irow = 0;
                double sumprdQty = 0;String lastProduct="";
                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                                String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
                                String item =(String)lineArr.get("ITEM");
                         
                                String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                                JSONObject resultJsonInt = new JSONObject();
                                resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM")));
                                resultJsonInt.put("LOC", strUtils.fString((String)lineArr.get("LOC")));
                                resultJsonInt.put("LOCDESC", strUtils.fString((String)lineArr.get("LOCDESC")));
                                resultJsonInt.put("LOCTYPE", strUtils.fString((String)lineArr.get("LOC_TYPE_ID")));
                                resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
                                resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
                                resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("PRD_BRAND_ID")));
                                resultJsonInt.put("ITEMDESC", strUtils.fString((String)lineArr.get("ITEMDESC")));
                                resultJsonInt.put("STKUOM", strUtils.fString((String)lineArr.get("STKUOM")));
                                resultJsonInt.put("BATCH", strUtils.fString((String)lineArr.get("BATCH")));
                                resultJsonInt.put("EXPIREDATE", strUtils.fString((String)lineArr.get("EXPIREDATE")));
                                resultJsonInt.put("QTY", strUtils.fString((String)lineArr.get("QTY")));
                                resultJsonInt.put("INVENTORYUOM", strUtils.fString((String)lineArr.get("INVENTORYUOM")));
                                resultJsonInt.put("INVQTY", strUtils.fString((String)lineArr.get("INVUOMQTY")));
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
            	jsonArray.add("");
        		resultJson.put("items", jsonArray);
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    
    private JSONObject getInventorysmryDetailsWithAverageCost(HttpServletRequest request,String baseCurrency) {
            JSONObject resultJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayErr = new JSONArray();
           
            StrUtils strUtils = new StrUtils();
            try {
                String  fdate="",tdate="";
               String PLANT= strUtils.fString(request.getParameter("PLANT"));
               String USERID= strUtils.fString(request.getParameter("LOGIN_USER"));
               String LOC     = strUtils.fString(request.getParameter("LOC"));
               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
               String BATCH   = strUtils.fString(request.getParameter("BATCH"));
               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
               String  PRD_TYPE_ID     = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
               String  PRD_DEPT_ID     = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
               // Start code modified by Bruhan for product brand on 11/9/12 
               String  PRD_BRAND_ID     = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
               String  PRD_DESCRIP     = strUtils.fString(request.getParameter("PRD_DESCRIP"));
               String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
               String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
               String  CURRENCYID      = strUtils.fString(request.getParameter("CURRENCYID"));
               String  CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
               String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
               String  LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
               String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
               String UOM = strUtils.fString(request.getParameter("UOM"));
               String LOCALEXPENSES=StrUtils.fString(request.getParameter("LOCALEXPENSES"));
               
                if (FROM_DATE.length()>5)
                       fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
                       
                if (TO_DATE.length()>5)
                        tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
                                      
                if(CURRENCYID.equals(""))
                 {
                          CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
                          List listQry = _CurrencyDAO.getCurrencyList(PLANT,CURRENCYDISPLAY);
                          for(int i =0; i<listQry.size(); i++) {
                              Map m=(Map)listQry.get(i);
                              CURRENCYID=(String)m.get("currencyid");
                          }
                 }        
                Hashtable ht = new Hashtable();
                if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
                if(strUtils.fString(ITEM).length() > 0)                ht.put("a.ITEM",ITEM);
                //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
                if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
                if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
                if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;
                if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ;
                if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ;
                System.out.println((String)request.getSession().getAttribute("SYSTEMNOW"));
                ArrayList invQryList= new ArrayList();
                if(LOCALEXPENSES.equals("0")){
                	invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOM(ht,fdate,tdate,PLANT,ITEM,PRD_DESCRIP,CURRENCYID,baseCurrency,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);
                }else {
                	if(((String)request.getSession().getAttribute("SYSTEMNOW")).equalsIgnoreCase("INVENTORY")) {
                		invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOMLandedCostForInventory(ht,fdate,tdate,PLANT,ITEM,PRD_DESCRIP,CURRENCYID,baseCurrency,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);
                	}else {
                		invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOMLandedCost(ht,fdate,tdate,PLANT,ITEM,PRD_DESCRIP,CURRENCYID,baseCurrency,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);
                	}
                	
                }
                if (invQryList.size() > 0) {
              
                int iIndex = 0;
                 int irow = 0;
                double sumprdQty = 0 ,sumOfTotalCost = 0,sumOfTotalPrice=0, billQty=0,unbillQty=0,invQty=0;String lastProduct="";
                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                      
                                String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                
                                String billStatus=strUtils.fString((String) lineArr.get("BILL_STATUS"));
                                String billedqty,unbilledqty;
                                		
                                /*if(billStatus.equalsIgnoreCase("BILLED"))
                                {
                                	billedqty=strUtils.fString((String) lineArr.get("INVUOMQTY"));
                                	unbilledqty="";
                                }
                                else
                                {
                                	billedqty="";
                                	unbilledqty=strUtils.fString((String) lineArr.get("INVUOMQTY"));
                                }*/
                                billQty = Double.parseDouble(strUtils.fString((String) lineArr.get("BILLED_QTY")));
                                invQty = Double.parseDouble(strUtils.fString((String) lineArr.get("INVUOMQTY")));
                                unbillQty = Double.parseDouble(strUtils.fString((String) lineArr.get("UNBILLED_QTY")));
                                billedqty=StrUtils.addZeroes(billQty, "3");
                                unbilledqty=StrUtils.addZeroes((unbillQty), "3");
                                if(LOCALEXPENSES.equals("0")){
                                	billedqty=StrUtils.addZeroes(0d, "3");
                                    unbilledqty=StrUtils.addZeroes((unbillQty), "3");
                                }
                                if(((String)request.getSession().getAttribute("SYSTEMNOW")).equalsIgnoreCase("INVENTORY")) {
                                	billedqty=unbilledqty="0";
                                }
                                
                                String avgcostcheck = "0.0";
                                if((String)lineArr.get("AVERAGE_COST") == null) {
                                }else {
                                	avgcostcheck = (String)lineArr.get("AVERAGE_COST");
                                }
                                
                                String costpccheck = "0.0";
                                if((String)lineArr.get("COST_PC") == null) {
                                }else {
                                	costpccheck = (String)lineArr.get("COST_PC");
                                }
                               
                                sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
                                double avgCost =Double.parseDouble(avgcostcheck);
                                double cost_pc =Double.parseDouble(costpccheck);
                                double price =Double.parseDouble((String)lineArr.get("LIST_PRICE"));
                                double TotalCost=0d;
                                if(((String)request.getSession().getAttribute("SYSTEMNOW")).equalsIgnoreCase("ACCOUNTING")) {
                                	if(LOCALEXPENSES.equals("0")){
                                		TotalCost=avgCost* Double.parseDouble((String)lineArr.get("INVUOMQTY"));
                                	}else {
                                		if(billQty==0) {
                                			avgCost=0;
                                			cost_pc=0;
                                		}
                                		TotalCost=avgCost* billQty;
                                	}
                                	
                                }else {
                                	TotalCost=avgCost* Double.parseDouble((String)lineArr.get("INVUOMQTY"));
                                    
                                }
                                double TotalPrice=price* Double.parseDouble((String)lineArr.get("QTY"));
                                sumOfTotalCost = sumOfTotalCost + TotalCost;
                                sumOfTotalPrice = sumOfTotalPrice + TotalPrice;
                                String item =(String)lineArr.get("ITEM");
                                String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                                JSONObject resultJsonInt = new JSONObject();
                                
                               
    							
                                resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM")));
                                resultJsonInt.put("LOC", strUtils.fString((String)lineArr.get("LOC")));
                                resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
                                resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
                                resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("PRD_BRAND_ID")));
                                resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String)lineArr.get("PRD_DEPT_ID")));
                                resultJsonInt.put("ITEMDESC", strUtils.fString((String)lineArr.get("ITEMDESC")));
                                resultJsonInt.put("STKUOM", strUtils.fString((String)lineArr.get("STKUOM")));
                                resultJsonInt.put("STKQTY", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("STKQTY"))), "3"));
    		                    resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArr.get("INVENTORYUOM")));
    		                    resultJsonInt.put("INVUOMQTY", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("INVUOMQTY"))), "3"));
    		                    resultJsonInt.put("BILLEDQTY", billedqty);
    		                    resultJsonInt.put("UNBILLEDQTY", unbilledqty);
                                resultJsonInt.put("BATCH", strUtils.fString((String)lineArr.get("BATCH")));
                                resultJsonInt.put("AVERAGE_COST", StrUtils.addZeroes(avgCost, "3"));
                                resultJsonInt.put("COST_PER_PC", StrUtils.addZeroes(cost_pc, "3"));
                                resultJsonInt.put("LIST_PRICE", (String)lineArr.get("LIST_PRICE"));
                                resultJsonInt.put("QTY", strUtils.formatNum((String)lineArr.get("QTY")));
                                resultJsonInt.put("TotalCost", (TotalCost));
                                resultJsonInt.put("TotalPrice", (TotalPrice));
                               
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
            	jsonArray.add("");
        		resultJson.put("items", jsonArray);
                    resultJson.put("SEARCH_DATA", "");
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                    resultJsonInt.put("ERROR_CODE", "98");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("ERROR", jsonArrayErr);
            }
            return resultJson;
    }
    private JSONObject getInventorysmryDetailsWithAverageCostCog(HttpServletRequest request,String baseCurrency) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
        StrUtils strUtils = new StrUtils();
        try {
            String  fdate="",tdate="";
           String PLANT= strUtils.fString(request.getParameter("PLANT"));
           String USERID= strUtils.fString(request.getParameter("LOGIN_USER"));
           String LOC     = strUtils.fString(request.getParameter("LOC"));
           String ITEM    = strUtils.fString(request.getParameter("ITEM"));
           String BATCH   = strUtils.fString(request.getParameter("BATCH"));
           String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
           String  PRD_TYPE_ID     = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
           // Start code modified by Bruhan for product brand on 11/9/12 
           String  PRD_BRAND_ID     = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
           String  PRD_DESCRIP     = strUtils.fString(request.getParameter("PRD_DESCRIP"));
           String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
           String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
           String  CURRENCYID      = strUtils.fString(request.getParameter("CURRENCYID"));
           String  CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
           String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
           String  LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
           String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
           String UOM = strUtils.fString(request.getParameter("UOM"));
           String LOCALEXPENSES=StrUtils.fString(request.getParameter("LOCALEXPENSES"));
           
            if (FROM_DATE.length()>5)
                   fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
                   
            if (TO_DATE.length()>5)
                    tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
                                  
            if(CURRENCYID.equals(""))
             {
                      CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
                      List listQry = _CurrencyDAO.getCurrencyList(PLANT,CURRENCYDISPLAY);
                      for(int i =0; i<listQry.size(); i++) {
                          Map m=(Map)listQry.get(i);
                          CURRENCYID=(String)m.get("currencyid");
                      }
             }        
            Hashtable ht = new Hashtable();
            if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
            if(strUtils.fString(ITEM).length() > 0)                ht.put("a.ITEM",ITEM);
            //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;
            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ;
            
            ArrayList invQryList= new ArrayList();
            if(LOCALEXPENSES.equals("0")){
            	invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOM(ht,fdate,tdate,PLANT,ITEM,PRD_DESCRIP,CURRENCYID,baseCurrency,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);
            }else {
            	invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOMLandedCost(ht,fdate,tdate,PLANT,ITEM,PRD_DESCRIP,CURRENCYID,baseCurrency,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);
            }
            if (invQryList.size() > 0) {
          
            int iIndex = 0;
             int irow = 0;
            double sumprdQty = 0 ,sumOfTotalCost = 0,sumOfTotalPrice=0;String lastProduct="";
                for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                  
                            String result="";
                            Map lineArr = (Map) invQryList.get(iCnt);
                            
                            String CostAvg=new InvUtil().getCostOfGoods((String)lineArr.get("ITEM"),PLANT);
                            String AVERAGE_COST=null;
                            if(CostAvg!=null) {
                            	AVERAGE_COST=CostAvg;
                            }else {
                            	  CostAvg=new InvUtil().getAvgCostofItem((String)lineArr.get("ITEM"), PLANT);
	                            	  if(CostAvg!=null) {
	                            		  AVERAGE_COST=CostAvg;
	                            	  }else {
	                            		  AVERAGE_COST=(String)lineArr.get("AVERAGE_COST");
	                            	  }
                            }
                            String billStatus=strUtils.fString((String) lineArr.get("BILL_STATUS"));
                            String billedqty,unbilledqty;
                            		
                            if(billStatus.equalsIgnoreCase("BILLED"))
                            {
                            	billedqty=strUtils.fString((String) lineArr.get("INVUOMQTY"));
                            	unbilledqty="";
                            }
                            else
                            {
                            	billedqty="";
                            	unbilledqty=strUtils.fString((String) lineArr.get("INVUOMQTY"));
                            }
                            sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
                            double avgCost =Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
                            double price =Double.parseDouble((String)lineArr.get("LIST_PRICE"));
                            double TotalCost=Double.parseDouble(AVERAGE_COST)* Double.parseDouble((String)lineArr.get("QTY"));
                            double TotalPrice=price* Double.parseDouble((String)lineArr.get("QTY"));
                            sumOfTotalCost = sumOfTotalCost + TotalCost;
                            sumOfTotalPrice = sumOfTotalPrice + TotalPrice;
                            String item =(String)lineArr.get("ITEM");
                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                            resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM")));
                            resultJsonInt.put("LOC", strUtils.fString((String)lineArr.get("LOC")));
                            resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
                            resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
                            resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("PRD_BRAND_ID")));
                            resultJsonInt.put("ITEMDESC", strUtils.fString((String)lineArr.get("ITEMDESC")));
                            resultJsonInt.put("STKUOM", strUtils.fString((String)lineArr.get("STKUOM")));
                            resultJsonInt.put("STKQTY", strUtils.fString((String) lineArr.get("STKQTY")));
		                    resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArr.get("INVENTORYUOM")));
		                    resultJsonInt.put("BILLEDQTY", billedqty);
		                    resultJsonInt.put("UNBILLEDQTY", unbilledqty);
                            resultJsonInt.put("BATCH", strUtils.fString((String)lineArr.get("BATCH")));
                            resultJsonInt.put("AVERAGE_COST", AVERAGE_COST);
                            resultJsonInt.put("COST_PER_PC", AVERAGE_COST);
                            resultJsonInt.put("LIST_PRICE", strUtils.currencyWtoutSymbol((String)lineArr.get("LIST_PRICE")));
                            resultJsonInt.put("QTY", strUtils.formatNum((String)lineArr.get("QTY")));
                            resultJsonInt.put("TotalCost", (TotalCost));
                            resultJsonInt.put("TotalPrice", (TotalPrice));
                           
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
        	jsonArray.add("");
    		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
    
	private JSONObject getInventorysmryGroupByProd(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();

		StrUtils strUtils = new StrUtils();

		try {
			String PLANT = strUtils.fString(request.getParameter("PLANT"));
			String LOC = strUtils.fString(request.getParameter("LOC"));
			String ITEM = strUtils.fString(request.getParameter("ITEM"));
			String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
			String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
			String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
			String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
			String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
			String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
			String MODEL = strUtils.fString(request.getParameter("MODEL"));
			Hashtable ht = new Hashtable();
			if (strUtils.fString(PRD_CLS_ID).length() > 0)
				ht.put("b.PRD_CLS_ID", PRD_CLS_ID);
			if (strUtils.fString(PRD_TYPE_ID).length() > 0)
				ht.put("b.itemtype", PRD_TYPE_ID);
			if (strUtils.fString(PRD_BRAND_ID).length() > 0)
				ht.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
			if (strUtils.fString(PRD_DEPT_ID).length() > 0)
				ht.put("b.PRD_DEPT_ID", PRD_DEPT_ID);
			// if(strUtils.fString(LOC_TYPE_ID).length() > 0)
			// ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
			if (strUtils.fString(MODEL).length() > 0)
				ht.put("b.MODEL", MODEL);
			ArrayList invQryList = new InvUtil().getInvListSummaryGroupByProd(PLANT, ITEM, PRD_DESCRIP, ht, LOC,
					LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3);// "","","","",plant,"")
			if (invQryList.size() > 0) {
				int iIndex = 0;
				int irow = 0;
				double sumprdQty = 0;
				String lastProduct = "";
				for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
					String result = "";
					Map lineArr = (Map) invQryList.get(iCnt);
					sumprdQty = sumprdQty + Double.parseDouble((String) lineArr.get("QTY"));
					String item = (String) lineArr.get("ITEM");
					String loc = (String) lineArr.get("LOC");
					String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
					JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
                    resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
                    resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
                    resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
                    resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
                    resultJsonInt.put("PRDBRANDID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
                    resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(StrUtils.fString((String) lineArr.get("ITEMDESC"))));
                    resultJsonInt.put("STKUOM", strUtils.fString((String) lineArr.get("STKUOM")));
                    resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
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
			jsonArray.add("");
			resultJson.put("items", jsonArray);
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
    
    
    private JSONObject getInventorysmryForOpenCloseBal(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       String link ="";
        StrUtils strUtils = new StrUtils();
     
        try {
				   String PLANT= strUtils.fString(request.getParameter("PLANT"));
				   String ITEM    = strUtils.fString(request.getParameter("ITEM"));
				   String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				   String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				   String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
				   String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				   String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
	               String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
	               String fdate ="",tdate="";
	                if (FROM_DATE.length()>5)
	                       fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	                       
	                if (TO_DATE.length()>5){
	                        tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
	                }else{
	                	tdate = new DateUtils().getDateFormatyyyyMMdd();
	                }
	                
			        Hashtable ht = new Hashtable();
			        if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
					if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("itemtype",PRD_TYPE_ID); 
					if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID);  
                      
        
            ArrayList invQryList= new InvUtil().getInvWithOpenCloseBal(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate);
            
             if (invQryList.size() > 0) {
             int iIndex = 0;
             
                          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                              String result="";
                             Map lineArr = (Map) invQryList.get(iCnt);
                            String item =(String)lineArr.get("ITEM");

    				       link = "<a href=\"" + "prdRIDetails.jsp?ITEM=" + item +"&FROM_DATE=" + fdate + "&TO_DATE=" + tdate+"\">";

                             String bgcolor = ((iIndex == 0) || (iIndex % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                             JSONObject resultJsonInt = new JSONObject();
                        
                             resultJsonInt.put(  "ITEM",  strUtils.fString((String) lineArr.get("ITEM")));
                             resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
                             resultJsonInt.put("PRD_CLS_ID", strUtils.fString((String) lineArr.get("PRD_CLS_ID")));
                             resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
                             resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
                             resultJsonInt.put("UOM", strUtils.fString((String) lineArr.get("STKUOM")));
                             resultJsonInt.put("OPENING", strUtils.fString((String) lineArr.get("OPENING")));
                             resultJsonInt.put("TOTRECV", strUtils.fString((String) lineArr.get("TOTRECV")));
                             resultJsonInt.put("TOTAL_ISS", strUtils.fString((String) lineArr.get("TOTAL_ISS")));
                             resultJsonInt.put("TOTISSREV", strUtils.fString((String) lineArr.get("TOTISSREV")));
                             resultJsonInt.put("CLOSING", strUtils.fString((String) lineArr.get("CLOSING")));
                             resultJsonInt.put("RECVED_AFTER", strUtils.fString((String) lineArr.get("RECVED_AFTER")));
                             resultJsonInt.put("ISSUED_AFTER", strUtils.fString((String) lineArr.get("ISSUED_AFTER")));
                             resultJsonInt.put("STOCKONHAND", strUtils.formatNum(StrUtils.fString((String)lineArr.get("STOCKONHAND"))));
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
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}

  private JSONObject getInventorysmryForOpenCloseBalByLoc(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       String link ="",link1="";
        StrUtils strUtils = new StrUtils();
     
        try {
				   String PLANT= strUtils.fString(request.getParameter("PLANT"));
				   String ITEM    = strUtils.fString(request.getParameter("ITEM"));
				   String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				   String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				   String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
				   String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				   String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
	               String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
	               String LOC     = strUtils.fString(request.getParameter("LOC"));
                   String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
                   String  rptType = strUtils.fString(request.getParameter("INV_REP_TYPE"));
	               String fdate ="",tdate="";
	                if (FROM_DATE.length()>5)
	                       fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	                       
	                if (TO_DATE.length()>5){
	                        tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
	                }else{
	                	tdate = new DateUtils().getDateFormatyyyyMMdd();
	                }
	                
			        Hashtable ht = new Hashtable();
			        if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
			        if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("itemtype",PRD_TYPE_ID); 
					if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID);  
                      
        
            ArrayList invQryList =  new InvUtil().getInvWithOpenCloseBalByLoc(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate,LOC,LOC_TYPE_ID,"");
           
             if (invQryList.size() > 0) {
             int iIndex = 0;
             
                          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                              String result="";
                             Map lineArr = (Map) invQryList.get(iCnt);
                            String item =(String)lineArr.get("ITEM");
                            String loc =strUtils.fString((String)lineArr.get("LOC"));

    				      //  link = "<a href=\"" + "prdRIDetails.jsp?TYPE=OCLOC&ITEM=" + item +"&FROM_DATE=" + fdate + "&TO_DATE=" + tdate+ "&LOC=" + loc+ "\">";
    				       // link1 = "<a href=\"" + "prdRIDetails.jsp?TYPE=OCLOC&ITEM=" + item +"&FROM_DATE=" + fdate + "&TO_DATE=" + tdate+ "\">";

                             String bgcolor = ((iIndex == 0) || (iIndex % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                             JSONObject resultJsonInt = new JSONObject();
                        
                             resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
                             resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
                             resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
                             resultJsonInt.put("PRD_CLS_ID", strUtils.fString((String) lineArr.get("PRD_CLS_ID")));
                             resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
                             resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
                             resultJsonInt.put("UOM", strUtils.fString((String) lineArr.get("STKUOM")));
                             resultJsonInt.put("OPENING", strUtils.fString((String) lineArr.get("OPENING")));
                             resultJsonInt.put("TOTRECV", strUtils.fString((String) lineArr.get("TOTRECV")));
                             resultJsonInt.put("TOTLTRECV", strUtils.fString((String) lineArr.get("TOTLTRECV")));
                             resultJsonInt.put("TOTLTISS", strUtils.fString((String) lineArr.get("TOTLTISS")));
                             resultJsonInt.put("TOTAL_ISS", strUtils.fString((String) lineArr.get("TOTAL_ISS")));
                             resultJsonInt.put("CLOSING", strUtils.fString((String) lineArr.get("CLOSING")));
                             resultJsonInt.put("RECVED_AFTER", strUtils.fString((String) lineArr.get("RECVED_AFTER")));
                             resultJsonInt.put("CTOTLTRECV", strUtils.fString((String) lineArr.get("CTOTLTRECV")));
                             resultJsonInt.put("CTOTLTISS", strUtils.fString((String) lineArr.get("CTOTLTISS")));
                             resultJsonInt.put("ISSUED_AFTER", strUtils.fString((String) lineArr.get("ISSUED_AFTER")));
                             resultJsonInt.put("STOCKONHAND", strUtils.fString((String) lineArr.get("STOCKONHAND")));
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
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
    

    private JSONObject getInventorysmryForOpenCloseBalWithAvgCost(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       String link ="";
        StrUtils strUtils = new StrUtils();
     
        try {
				   String PLANT= strUtils.fString(request.getParameter("PLANT"));
				   String ITEM    = strUtils.fString(request.getParameter("ITEM"));
				   String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				   String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				   String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
				   String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				   String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
	               String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
	               String LOC     = strUtils.fString(request.getParameter("LOC"));
                   String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
                   String  rptType = strUtils.fString(request.getParameter("INV_REP_TYPE"));
	               String fdate ="",tdate="";
	                if (FROM_DATE.length()>5)
	                       fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	                       
	                if (TO_DATE.length()>5){
	                        tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
	                }else{
	                	tdate = new DateUtils().getDateFormatyyyyMMdd();
	                }
	                
			        Hashtable ht = new Hashtable();
			        if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
					if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("itemtype",PRD_TYPE_ID); 
					if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID);  
                      
        
            ArrayList invQryList=  new InvUtil().getInvWithOpenCloseBalByLoc(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate,LOC,LOC_TYPE_ID,"WITH_AVGCOST");	
            
             if (invQryList.size() > 0) {
             int iIndex = 0;
             double sumOfTotalCost = 0,sumOfTotalPrice=0;
                          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                              String result="";
                             Map lineArr = (Map) invQryList.get(iCnt);
                            String item =(String)lineArr.get("ITEM");
                            double avgCost =Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
                            double price =Double.parseDouble((String)lineArr.get("LIST_PRICE"));
                            double TotalCost=avgCost* Double.parseDouble((String)lineArr.get("CLOSING"));
                            double TotalPrice=price* Double.parseDouble((String)lineArr.get("CLOSING"));
                            sumOfTotalCost = sumOfTotalCost + TotalCost;
                            sumOfTotalPrice = sumOfTotalPrice + TotalPrice;
                            
                            String openingQty = (String) lineArr.get("OPENING");
                            String totRcvQty = (String) lineArr.get("TOTRECV");
                            String totLTRcvQty = (String) lineArr.get("TOTLTRECV");
                            String totLTISS = (String) lineArr.get("TOTLTISS");
                            String total_ISS = (String) lineArr.get("TOTAL_ISS");
                            String closing = (String) lineArr.get("CLOSING");
                            String averageCost = (String) lineArr.get("AVERAGE_COST");
                            String listPrice = (String) lineArr.get("LIST_PRICE");
                            String totalCost = (String.valueOf(TotalCost));
                            String totalPrice = (String.valueOf(TotalPrice));
                            String receivedAfter = (String) lineArr.get("RECVED_AFTER");
                            String ctotltRecv = (String) lineArr.get("CTOTLTRECV");
                            String ctotltISS = (String) lineArr.get("CTOTLTISS");
                            String issuedAfter = (String) lineArr.get("ISSUED_AFTER");
                            String stockOnHand = (String) lineArr.get("STOCKONHAND");
                           
                            
                            float openingQtyVal = "".equals(openingQty) ? 0.0f :  Float.parseFloat(openingQty);
                            float totRcvQtyVal = "".equals(totRcvQty) ? 0.0f :  Float.parseFloat(totRcvQty);
                            float totLTRcvQtyVal = "".equals(totLTRcvQty) ? 0.0f :  Float.parseFloat(totLTRcvQty);
                            float totLTISSVal = "".equals(totLTISS) ? 0.0f :  Float.parseFloat(totLTISS);
                            float total_ISSVal = "".equals(total_ISS) ? 0.0f :  Float.parseFloat(total_ISS);
                            float closingVal = "".equals(closing) ? 0.0f :  Float.parseFloat(closing);
                            float averageCostVal = "".equals(averageCost) ? 0.0f :  Float.parseFloat(averageCost);
                            float listPriceVal = "".equals(listPrice) ? 0.0f :  Float.parseFloat(listPrice);
                            float totalCostVal = "".equals(totalCost) ? 0.0f :  Float.parseFloat(totalCost);
                            float totalPriceVal = "".equals(totalPrice) ? 0.0f :  Float.parseFloat(totalPrice);
                            float receivedAfterVal = "".equals(receivedAfter) ? 0.0f :  Float.parseFloat(receivedAfter);
                            float ctotltrecvVal = "".equals(ctotltRecv) ? 0.0f :  Float.parseFloat(ctotltRecv);
                            float ctotltISSVal = "".equals(ctotltISS) ? 0.0f :  Float.parseFloat(ctotltISS);
                            float issuedAfterVal = "".equals(issuedAfter) ? 0.0f :  Float.parseFloat(issuedAfter);
                            float stockOnHandVal = "".equals(stockOnHand) ? 0.0f :  Float.parseFloat(stockOnHand);
                           
                            
                            if(openingQtyVal==0f){
                            	openingQty="0.000";
                         	}else{
                         		openingQty=openingQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(totRcvQtyVal==0f){
                         		totRcvQty="0.000";
                         	}else{
                         		totRcvQty=totRcvQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(totLTRcvQtyVal==0f){
                         		totLTRcvQty="0.000";
                         	}else{
                         		totLTRcvQty=totLTRcvQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(totLTISSVal==0f){
                         		totLTISS="0.000";
                         	}else{
                         		totLTISS=totLTISS.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(total_ISSVal==0f){
                         		total_ISS="0.000";
                         	}else{
                         		total_ISS=total_ISS.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(closingVal==0f){
                         		closing="0.000";
                         	}else{
                         		closing=closing.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(averageCostVal==0f){
                         		averageCost="0.00000";
                         	}else{
                         		averageCost=averageCost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(listPriceVal==0f){
                         		listPrice="0.00000";
                         	}else{
                         		listPrice=listPrice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(totalCostVal==0f){
                         		totalCost="0.00000";
                         	}else{
                         		totalCost=totalCost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(totalPriceVal==0f){
                         		totalPrice="0.00000";
                         	}else{
                         		totalPrice=totalPrice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(receivedAfterVal==0f){
                         		receivedAfter="0.000";
                         	}else{
                         		receivedAfter=receivedAfter.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(ctotltrecvVal==0f){
                         		ctotltRecv="0.000";
                         	}else{
                         		ctotltRecv=ctotltRecv.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(ctotltISSVal==0f){
                         		ctotltISS="0.000";
                         	}else{
                         		ctotltISS=ctotltISS.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(issuedAfterVal==0f){
                         		issuedAfter="0.000";
                         	}else{
                         		issuedAfter=issuedAfter.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}if(stockOnHandVal==0f){
                         		stockOnHand="0.000";
                         	}else{
                         		stockOnHand=stockOnHand.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                         	}

    				      // link = "<a href=\"" + "prdRIDetails.jsp?ITEM=" + item +"&FROM_DATE=" + fdate + "&TO_DATE=" + tdate+"\">";

                             String bgcolor = ((iIndex == 0) || (iIndex % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                             JSONObject resultJsonInt = new JSONObject();
                             resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
                             resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
                             resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
                             resultJsonInt.put("PRD_CLS_ID", strUtils.fString((String) lineArr.get("PRD_CLS_ID")));
                             resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
                             resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
                             resultJsonInt.put("UOM", strUtils.fString((String) lineArr.get("STKUOM")));
                             resultJsonInt.put("OPENING", openingQty);
                             resultJsonInt.put("TOTRECV", totRcvQty);
                             resultJsonInt.put("TOTLTRECV", totLTRcvQty);
                             resultJsonInt.put("TOTLTISS",totLTISS );
                             resultJsonInt.put("TOTAL_ISS", total_ISS);
                             resultJsonInt.put("CLOSING", closing);
                             resultJsonInt.put("AVERAGE_COST",averageCost);
                             resultJsonInt.put("LIST_PRICE", listPrice);
                             resultJsonInt.put("TotalCost", totalCost);
                             resultJsonInt.put("TotalPrice", totalPrice);
                             resultJsonInt.put("RECVED_AFTER", receivedAfter);
                             resultJsonInt.put("CTOTLTRECV", ctotltRecv);
                             resultJsonInt.put("CTOTLTISS", ctotltISS);
                             resultJsonInt.put("ISSUED_AFTER", issuedAfter);
                             resultJsonInt.put("STOCKONHAND", stockOnHand);
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
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
    
    private JSONObject getInventorysmryMovHis(HttpServletRequest request) {
    	JSONObject resultJson = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	JSONArray jsonArrayErr = new JSONArray();
    	JSONArray jsonArrayinv = new JSONArray();
    	String link ="";
    	StrUtils strUtils = new StrUtils();
    	
    	try {
    		String PLANT= strUtils.fString(request.getParameter("PLANT"));
    		String ITEM    = strUtils.fString(request.getParameter("ITEM"));
    		String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
    		String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
    		String LOC     = strUtils.fString(request.getParameter("LOC"));
    		String fdate ="",tdate="";
    		if (FROM_DATE.length()>5)
    			fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
    		
    		if (TO_DATE.length()>5){
    			tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
    		}else{
    			tdate = new DateUtils().getDateFormatyyyyMMdd();
    		}
    		
    		String INV_UOM="";
    		String INV_UOM_QTY="1";
    		if(!ITEM.equals(""))
    		{
    			INV_UOM = new ItemMstDAO().getInvUOM(PLANT,ITEM);
    			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
    			MovHisDAO movHisDao1 = new MovHisDAO();
    			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+INV_UOM+"'",htTrand1);
    			if(getuomqty.size()>0)
    			{
    			Map mapval = (Map) getuomqty.get(0);
    			INV_UOM_QTY=(String)mapval.get("UOMQTY");
    			}
    		}
    		
    		Hashtable ht = new Hashtable();
    		
    		ArrayList invQryList=  new InvUtil().getInvMovHisByLoc(PLANT,ITEM,ht,fdate,tdate,LOC,"");	
    		
    		if (invQryList.size() > 0) {
    			int iIndex = 0;
    			float balqty = 0;
    			float balinvqty = 0;
    			String oldtype="";
    			double sumOfTotalCost = 0,sumOfTotalPrice=0;
    			for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
    				String result="";
    				Map lineArr = (Map) invQryList.get(iCnt);
    				String type =(String)lineArr.get("DIRTYPE");
    				String qty = (String) lineArr.get("QTY");
    				if(!type.equals("Opening"))
    					qty=qty.replace("-","");
    				float qtyVal = "".equals(qty) ? 0.0f :  Float.parseFloat(qty);
    				
    				if(qtyVal==0f){
    					qty="0.000";
    				}else{
    					qty=qty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
    				}
    				
    				
    				String bgcolor = ((iIndex == 0) || (iIndex % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
    				JSONObject resultJsonInt = new JSONObject();
    				resultJsonInt.put("TRANDATE", strUtils.fString((String) lineArr.get("TRANDATE")));
    				resultJsonInt.put("TRAN_NUMBER", strUtils.fString((String) lineArr.get("ORDNUM")));
    				resultJsonInt.put("TYPE", strUtils.fString((String) lineArr.get("DIRTYPE")));
    				resultJsonInt.put("REFERENCE", strUtils.fString((String) lineArr.get("CUSTOMER")));
    				String PUOMQTY="1";
        			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
        			MovHisDAO movHisDao1 = new MovHisDAO();
        			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+strUtils.fString((String) lineArr.get("UOM"))+"'",htTrand1);
        			if(getuomqty.size()>0)
        			{
        			Map mapval = (Map) getuomqty.get(0);
        			PUOMQTY=(String)mapval.get("UOMQTY");
        			}
    				if(type.equals("Opening"))
                    {
    					float qtyValnew = qtyVal*Float.parseFloat(INV_UOM_QTY);
    					resultJsonInt.put("IN_UOM", "");
    					resultJsonInt.put("IN_QTY", "");
    					resultJsonInt.put("OUT_UOM", "");
    					resultJsonInt.put("OUT_QTY", "");
    					resultJsonInt.put("BAL_UOM", "");
    					resultJsonInt.put("BAL_QTY", "");
    					resultJsonInt.put("BAL_INV_UOM", INV_UOM);
    					resultJsonInt.put("BAL_INV_QTY", strUtils.addZeroes(qtyValnew, "3"));
    					balqty=qtyVal;
    					balinvqty=qtyValnew;
                    } else if(type.equals("ORDER_RECV") || type.equals("GOODSRECEIPT") || type.equals("CONSIGNMENT_ORDER_RECV") || type.equals("KITTING_IN") || type.equals("STOCK_MOVE_IN") || type.equals("DE-KITTING_IN") || type.equals("CNT_STOCK_TAKE_IMPORT_DATA") || type.equals("SALES_RETURN") || type.equals("CNT_INVENTORY_IMPORT_DATA") || type.equals("SALES_ORDER_PICK_TRAN_IN") || type.equals("SALES_ORDER_REVERSE") || type.equals("POS_VOID_SALES_ORDER") || type.equals("POS_SALES_RETURN") || type.equals("POS_EXCHANGE") || type.equals("CONSIGNMENT_REVERSE_IN") || type.equals("STOCK_TAKE")) {
                    	float qtyValnew = qtyVal*Float.parseFloat(PUOMQTY);
                    	if(type.equals("CNT_INVENTORY_IMPORT_DATA"))
                    	{
                    		balqty = 0;
                			balinvqty = 0;
                    	}
                    	balinvqty=balinvqty+(qtyValnew*Float.parseFloat(INV_UOM_QTY));
                    	balqty=(balinvqty / Float.parseFloat(PUOMQTY));
                    	
                    	resultJsonInt.put("IN_UOM", strUtils.fString((String) lineArr.get("UOM")));
						resultJsonInt.put("IN_QTY", strUtils.addZeroes(Double.parseDouble(qty), "3"));
						resultJsonInt.put("OUT_UOM", "");
						resultJsonInt.put("OUT_QTY", "");
						resultJsonInt.put("BAL_UOM", strUtils.fString((String) lineArr.get("UOM")));
						resultJsonInt.put("BAL_QTY", strUtils.addZeroes((balqty), "3"));
						resultJsonInt.put("BAL_INV_UOM", INV_UOM);
    					resultJsonInt.put("BAL_INV_QTY", strUtils.addZeroes(balinvqty, "3"));
    					if(type.equals("CNT_INVENTORY_IMPORT_DATA")) {
    						if(oldtype.equals(""))
    							oldtype="CNT_INVENTORY_IMPORT_DATA";
    					}
						
                    } else if(type.equals("ORD_PICK_ISSUE") || type.equals("GOODSISSUE") || type.equals("PICK_CONSIGNMENT_ORDER_OUT") || type.equals("KITTING_OUT") || type.equals("STOCK_MOVE_OUT") || type.equals("DE-KITTING_OUT") || type.equals("PURCHASE_RETURN") || type.equals("SALES_ORDER_PICK_TRAN_OUT") || type.equals("ORDER_ISSUE") || type.equals("OB_REVERSE_OUT") || type.equals("CONSIGNMENT_REVERSE_OUT") || type.equals("STOCK_TAKE_OUT")) {
                    	float qtyValnew = qtyVal*Float.parseFloat(PUOMQTY);
                    	balinvqty=balinvqty-(qtyValnew*Float.parseFloat(INV_UOM_QTY));
                    	balqty=(balinvqty / Float.parseFloat(PUOMQTY));
                    	resultJsonInt.put("IN_UOM", "");
                    	resultJsonInt.put("IN_QTY", "");
                    	resultJsonInt.put("OUT_UOM", strUtils.fString((String) lineArr.get("UOM")));
                    	resultJsonInt.put("OUT_QTY", strUtils.addZeroes(Double.parseDouble(qty), "3"));
                    	resultJsonInt.put("BAL_UOM", strUtils.fString((String) lineArr.get("UOM")));
                    	resultJsonInt.put("BAL_QTY", strUtils.addZeroes((balqty), "3"));
                    	resultJsonInt.put("BAL_INV_UOM", INV_UOM);
    					resultJsonInt.put("BAL_INV_QTY", strUtils.addZeroes(balinvqty, "3"));
    				
                    }
    				jsonArray.add(resultJsonInt);
    				
    			}
    			
    			resultJson.put("items", jsonArray);
    			JSONObject resultJsonInt = new JSONObject();
    			resultJsonInt.put("CLOSINGINV", strUtils.addZeroes(balinvqty, "3"));
    			jsonArrayinv.add(resultJsonInt);
    			resultJson.put("closinginv", jsonArrayinv);
    			resultJsonInt = new JSONObject();
    			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
    			resultJsonInt.put("ERROR_CODE", "100");
    			jsonArrayErr.add(resultJsonInt);
    			resultJson.put("errors", jsonArrayErr);
    		} else {
    			JSONObject resultJsonInt = new JSONObject();
    			resultJsonInt.put("CLOSINGINV", strUtils.addZeroes(0, "3"));
    			jsonArrayinv.add(resultJsonInt);
    			resultJson.put("closinginv", jsonArrayinv);
    			resultJsonInt = new JSONObject();
    			resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
    			resultJsonInt.put("ERROR_CODE", "99");
    			jsonArrayErr.add(resultJsonInt);
    			jsonArray.add("");
    			resultJson.put("items", jsonArray);
    			
    			resultJson.put("errors", jsonArrayErr);
    		}
    	} catch (Exception e) {
    		jsonArray.add("");
    		resultJson.put("items", jsonArray);
    		resultJson.put("SEARCH_DATA", "");
    		resultJson.put("TOTAL_QTY", 0);
    		JSONObject resultJsonInt = new JSONObject();
    		resultJsonInt.put("CLOSINGINV", strUtils.addZeroes(0, "3"));
			jsonArrayinv.add(resultJsonInt);
			resultJson.put("closinginv", jsonArrayinv);
			resultJsonInt = new JSONObject();
    		resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
    		resultJsonInt.put("ERROR_CODE", "98");
    		jsonArrayErr.add(resultJsonInt);
    		resultJson.put("ERROR", jsonArrayErr);
    	}
    	return resultJson;
    }

    private JSONObject getInventoryByPrdsmryMovHis(HttpServletRequest request) {
    	JSONObject resultJson = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	JSONArray jsonArrayErr = new JSONArray();
    	JSONArray jsonArrayinv = new JSONArray();
    	String link ="";
    	StrUtils strUtils = new StrUtils();
    	
    	try {
    		String PLANT= strUtils.fString(request.getParameter("PLANT"));
    		String ITEM    = strUtils.fString(request.getParameter("ITEM"));
    		String FROM_DATE        = "";
    		String  TO_DATE         = "";
    		String LOC     = strUtils.fString(request.getParameter("LOC"));
    		String fdate ="",tdate="";
    		if (FROM_DATE.length()>5)
    			fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
    		
    		if (TO_DATE.length()>5){
    			tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
    		}else{
    			tdate = new DateUtils().getDateFormatyyyyMMdd();
    		}
    		
    		String INV_UOM="";
    		String INV_UOM_QTY="1";
    		if(!ITEM.equals(""))
    		{
    			INV_UOM = new ItemMstDAO().getInvUOM(PLANT,ITEM);
    			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
    			MovHisDAO movHisDao1 = new MovHisDAO();
    			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+INV_UOM+"'",htTrand1);
    			if(getuomqty.size()>0)
    			{
    				Map mapval = (Map) getuomqty.get(0);
    				INV_UOM_QTY=(String)mapval.get("UOMQTY");
    			}
    		}
    		
    		Hashtable ht = new Hashtable();
    		
    		ArrayList invQryList=  new InvUtil().getInvMovHisByPrd(PLANT,ITEM,ht,fdate,tdate,LOC,"");	
    		
    		if (invQryList.size() > 0) {
    			int iIndex = 0;
    			float balqty = 0;
    			float balinvqty = 0;
    			String oldtype="";
    			double sumOfTotalCost = 0,sumOfTotalPrice=0;
    			for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
    				String result="";
    				Map lineArr = (Map) invQryList.get(iCnt);
    				String type =(String)lineArr.get("DIRTYPE");
    				String qty = (String) lineArr.get("QTY");
    				qty=qty.replace("-","");
    				float qtyVal = "".equals(qty) ? 0.0f :  Float.parseFloat(qty);
    				
    				if(qtyVal==0f){
    					qty="0.000";
    				}else{
    					qty=qty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
    				}
    				
    				
    				String bgcolor = ((iIndex == 0) || (iIndex % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
    				JSONObject resultJsonInt = new JSONObject();
    				resultJsonInt.put("TYPE", strUtils.fString((String) lineArr.get("DIRTYPE")));
    				String PUOMQTY="1";
    				PUOMQTY=(String)lineArr.get("UOMQTY");
    				if(type.equals("ORDER_RECV") || type.equals("GOODSRECEIPT") || type.equals("CONSIGNMENT_ORDER_RECV") || type.equals("KITTING_IN") || type.equals("STOCK_MOVE_IN") || type.equals("DE-KITTING_IN") || type.equals("CNT_STOCK_TAKE_IMPORT_DATA") || type.equals("SALES_RETURN") || type.equals("CNT_INVENTORY_IMPORT_DATA") || type.equals("SALES_ORDER_PICK_TRAN_IN") || type.equals("SALES_ORDER_REVERSE") || type.equals("POS_VOID_SALES_ORDER") || type.equals("POS_SALES_RETURN") || type.equals("POS_EXCHANGE") || type.equals("CONSIGNMENT_REVERSE_IN") || type.equals("STOCK_TAKE")) {
    					float qtyValnew = qtyVal*Float.parseFloat(PUOMQTY);
    					if(type.equals("CNT_INVENTORY_IMPORT_DATA"))
    					{
    						balqty = 0;
    						balinvqty = 0;
    					}
    					balinvqty=balinvqty+(qtyValnew*Float.parseFloat(INV_UOM_QTY));
    					balqty=(balinvqty / Float.parseFloat(PUOMQTY));
    					
    					resultJsonInt.put("IN_UOM", strUtils.fString((String) lineArr.get("UOM")));
    					resultJsonInt.put("IN_QTY", strUtils.addZeroes(Double.parseDouble(qty), "3"));
    					resultJsonInt.put("OUT_QTY", "");
    					resultJsonInt.put("BAL_INV_QTY", strUtils.addZeroes(balinvqty, "3"));
    					
    					if(type.equals("CNT_INVENTORY_IMPORT_DATA")) {
    						if(oldtype.equals(""))
    							oldtype="CNT_INVENTORY_IMPORT_DATA";
    					}
    					
    				} else if(type.equals("ORD_PICK_ISSUE") || type.equals("GOODSISSUE") || type.equals("PICK_CONSIGNMENT_ORDER_OUT") || type.equals("KITTING_OUT") || type.equals("STOCK_MOVE_OUT") || type.equals("DE-KITTING_OUT") || type.equals("PURCHASE_RETURN") || type.equals("SALES_ORDER_PICK_TRAN_OUT") || type.equals("ORDER_ISSUE") || type.equals("OB_REVERSE_OUT") || type.equals("CONSIGNMENT_REVERSE_OUT") || type.equals("STOCK_TAKE_OUT")) {
    					float qtyValnew = qtyVal*Float.parseFloat(PUOMQTY);
    					balinvqty=balinvqty-(qtyValnew*Float.parseFloat(INV_UOM_QTY));
    					balqty=(balinvqty / Float.parseFloat(PUOMQTY));
    					resultJsonInt.put("IN_UOM", strUtils.fString((String) lineArr.get("UOM")));
    					resultJsonInt.put("IN_QTY", "");
    					resultJsonInt.put("OUT_QTY", strUtils.addZeroes(Double.parseDouble(qty), "3"));
    					resultJsonInt.put("BAL_INV_QTY", strUtils.addZeroes(balinvqty, "3"));
    					
    				}
    				jsonArray.add(resultJsonInt);
    				
    			}
    			
    			resultJson.put("items", jsonArray);
    			JSONObject resultJsonInt = new JSONObject();
    			resultJsonInt.put("CLOSINGINV", strUtils.addZeroes(balinvqty, "3"));
    			jsonArrayinv.add(resultJsonInt);
    			resultJson.put("closinginv", jsonArrayinv);
    			resultJsonInt = new JSONObject();
    			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
    			resultJsonInt.put("ERROR_CODE", "100");
    			jsonArrayErr.add(resultJsonInt);
    			resultJson.put("errors", jsonArrayErr);
    		} else {
    			JSONObject resultJsonInt = new JSONObject();
    			resultJsonInt.put("CLOSINGINV", strUtils.addZeroes(0, "3"));
    			jsonArrayinv.add(resultJsonInt);
    			resultJson.put("closinginv", jsonArrayinv);
    			resultJsonInt = new JSONObject();
    			resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
    			resultJsonInt.put("ERROR_CODE", "99");
    			jsonArrayErr.add(resultJsonInt);
    			jsonArray.add("");
    			resultJson.put("items", jsonArray);
    			
    			resultJson.put("errors", jsonArrayErr);
    		}
    	} catch (Exception e) {
    		jsonArray.add("");
    		resultJson.put("items", jsonArray);
    		resultJson.put("SEARCH_DATA", "");
    		resultJson.put("TOTAL_QTY", 0);
    		JSONObject resultJsonInt = new JSONObject();
    		resultJsonInt.put("CLOSINGINV", strUtils.addZeroes(0, "3"));
    		jsonArrayinv.add(resultJsonInt);
    		resultJson.put("closinginv", jsonArrayinv);
    		resultJsonInt = new JSONObject();
    		resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
    		resultJsonInt.put("ERROR_CODE", "98");
    		jsonArrayErr.add(resultJsonInt);
    		resultJson.put("ERROR", jsonArrayErr);
    	}
    	return resultJson;
    }
        
    private JSONObject getContainerSummary(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
        StrUtils strUtils = new StrUtils();
      
        try {
        
           String PLANT= strUtils.fString(request.getParameter("PLANT"));
           String CUSTOMERNAME     = strUtils.fString(request.getParameter("CUSTOMERNAME"));
           String CONTAINER    = strUtils.fString(request.getParameter("CONTAINER"));
           String ORDERNO  = strUtils.fString(request.getParameter("ORDERNO"));
           String ITEM = strUtils.fString(request.getParameter("ITEM"));
           String BATCH   = strUtils.fString(request.getParameter("BATCH"));
           String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
           //-----Added by Bruhan on Feb 7 2014, Description: Include From and To date in container summary search
           String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
           String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
           String CUSTOMERTYPE = strUtils.fString(request.getParameter("CUSTOMERTYPE"));
           //-----Added by Bruhan on Feb 7 2014 end
            
            Hashtable ht = new Hashtable();

            if(strUtils.fString(PLANT).length() > 0)               ht.put("PLANT",PLANT);
            if(strUtils.fString(CUSTOMERNAME).length() > 0)        ht.put("CNAME",CUSTOMERNAME);
            if(strUtils.fString(CONTAINER).length() > 0)           ht.put("CONTAINER",CONTAINER);
            if(strUtils.fString(ORDERNO).length() > 0)             ht.put("DONO",ORDERNO) ; 
            if(strUtils.fString(ITEM).length() > 0)                ht.put("ITEM",ITEM);
            if(strUtils.fString(PRD_DESCRIP).length() > 0)         ht.put("ITEMDESC",PRD_DESCRIP) ;  
            if(strUtils.fString(BATCH).length() > 0)               ht.put("BATCH",BATCH);
            if(strUtils.fString(CUSTOMERTYPE).length() > 0)        ht.put("CUSTTYPE",CUSTOMERTYPE);
            /*-- Modification History
                 1. Bruhan on Feb 07 2014, Description:Include From and To date in search
            */
            ArrayList conQryList= new DOUtil().getContainerSummary(ht,FROM_DATE,TO_DATE,PLANT);
            if (conQryList.size() > 0) {
            int iIndex = 0;
             int irow = 0;
             String strContainer;
            double sumprdQty = 0;String lastProduct="",lastContainer="";
                for (int iCnt =0; iCnt<conQryList.size(); iCnt++){
                      
                             
                            String result="";
                            strContainer="";
                            Map lineArr = (Map) conQryList.get(iCnt);
                            sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("qty"));
                            String item =(String)lineArr.get("item");
                            String Container =(String)lineArr.get("container");
                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                            
                          //-----Modified by Bruhan on Feb 27 2014, Description: To display NOCONTAINER data's as ''
                            if(lineArr.get("container").equals("NOCONTAINER"))
                            {
                            	strContainer="";
                            }
                            else
                            {
                            	strContainer=(String)lineArr.get("container");
                            }
                            	
                            //-----Modified by Bruhan on Feb 26 2014, Description: Include NOCONTAINER data's in container Summary
                       //  if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(item))&& (lastContainer.equalsIgnoreCase("") || lastContainer.equalsIgnoreCase(Container))){
                        	 resultJsonInt.put("cname", strUtils.fString((String)lineArr.get("cname")));
                        	 resultJsonInt.put("strContainer",  strUtils.fString(strContainer));
                        	 resultJsonInt.put("transactiondate", strUtils.fString((String)lineArr.get("transactiondate")));
                        	 resultJsonInt.put("dono", strUtils.fString((String)lineArr.get("dono")));
                        	 resultJsonInt.put("item", strUtils.fString((String)lineArr.get("item")));
                        	 resultJsonInt.put("itemdesc", strUtils.fString((String)lineArr.get("itemdesc")));
                        	 resultJsonInt.put("batch", strUtils.fString((String)lineArr.get("batch")));
                        	 resultJsonInt.put("qty", strUtils.formatNum((String)lineArr.get("qty")));
                        	 
                             jsonArray.add(resultJsonInt);	
                       
                           /*  result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                     
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("cname")) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(strContainer) + "</td>"
                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("transactiondate")) + "</td>"
                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("dono")) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("item")) + "</td>"
                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + sqlBean.formatHTML(strUtils.fString((String)lineArr.get("itemdesc"))) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;"+ strUtils.fString((String)lineArr.get("batch")) + "</td>"
                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                        + "</tr>" ;
                          if(iIndex+1 == conQryList.size()){ 
                        	   irow=irow+1;
                                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

                              result +=  "<TR bgcolor ="+bgcolor+" >"
                              + "<td>  </td>"
                              + "<td>  </td>"
                              + "<td>  </td>"
                              +"<td>  </td>"
                              + "<td>  </td>"
                              +"<td>  </td>"
                              + "<td align=right><b>Total:</b></td>"
                              + "<td align=right><b> " + strUtils.formatNum((new Double(sumprdQty).toString())) + "</b></td>"
                              + "</tr>" ;   
                                   
                         } }else
                          {
                        	sumprdQty = sumprdQty - Double.parseDouble((String)lineArr.get("qty"));
                                                 
                          result +=  "<TR bgcolor ="+bgcolor+" >"
                           + "<td>  </td>"
                           + "<td>  </td>"
                           + "<td>  </td>"
                           +"<td>  </td>"
                           + "<td>  </td>"
                           +"<td>  </td>"
                           + "<td align=right><b>Total:</b></td>"
                           + "<td align=right><b> " + strUtils.formatNum((new Double(sumprdQty).toString())) + "</b></td>"
                           + "</tr>" ;   
                           irow=irow+1;
                           bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                            result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                            + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("cname")) + "</td>"
                            + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(strContainer) + "</td>"
                             + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("transactiondate")) + "</td>"
                            + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("dono")) + "</td>"
                            + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("item")) + "</td>"
                            + "<td align = left>&nbsp;&nbsp;&nbsp;" + sqlBean.formatHTML(strUtils.fString((String)lineArr.get("itemdesc"))) + "</td>"
                            + "<td  align = left>&nbsp;&nbsp;&nbsp;"+ strUtils.fString((String)lineArr.get("batch")) + "</td>"
                            + "<td align = right>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("qty")) + "</td>"
                            + "</tr>" ;
                                              
                        sumprdQty =  Double.parseDouble((String)lineArr.get("qty"));
                                 if(iIndex+1 == conQryList.size()){ 
                                     irow=irow+1;
                                     bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                                     result +=  "<TR bgcolor ="+bgcolor+" >"
                                     + "<td>  </td>"
                                     + "<td>  </td>"
                                     + "<td>  </td>"
                                     +"<td>  </td>"
                                     + "<td>  </td>"
                                     +"<td>  </td>"
                                     + "<td align=right><b>Total:</b></td>"
                                     + "<td align=right><b> " + strUtils.formatNum((new Double(sumprdQty).toString())) + "</b></td>"
                                     + "</tr>" ;   
                                                                              
                          }
                        }
                             irow=irow+1;
                             iIndex=iIndex+1;
                             lastProduct = item;
                             lastContainer=Container;
                         
                           // resultJsonInt.put("INVDETAILS", result);
                             resultJsonInt.put("CONTAINERSUMMARY", result);
                         
                            jsonArray.add(resultJsonInt);*/

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
        	jsonArray.add("");
    		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
    
    /* ************Modification History*********************************
	   Sep 23 2014, Description: To include USER
	    April 29 2015, Description: To include REMARKS
	*/
 private JSONObject getStockTakeSummary(HttpServletRequest request) {
     JSONObject resultJson = new JSONObject();
     JSONArray jsonArray = new JSONArray();
     JSONArray jsonArrayErr = new JSONArray();
    
     StrUtils strUtils = new StrUtils();
  
     try {
     
        String PLANT= strUtils.fString(request.getParameter("PLANT"));
        String LOC     = strUtils.fString(request.getParameter("LOC"));
        String ITEM    = strUtils.fString(request.getParameter("ITEM"));
        String BATCH   = strUtils.fString(request.getParameter("BATCH"));
        String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
        String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
        String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
        String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
        String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
		   String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
		   String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
		   String  USER = strUtils.fString(request.getParameter("USER"));
		   Hashtable ht = new Hashtable();

         if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
         if(strUtils.fString(BATCH).length() > 0)               ht.put("a.BATCH",BATCH);
         //if(strUtils.fString(LOC_TYPE_ID).length() > 0)               ht.put("b.LOC_TYPE_ID",LOC_TYPE_ID);
        
         ArrayList invQryList= new InvUtil().getStockTakeDetails(ht,PLANT,ITEM,PRD_DESCRIP,PRD_CLS_ID,PRD_BRAND_ID,PRD_TYPE_ID,FROM_DATE,TO_DATE,LOC_TYPE_ID,USER);//"","","","",plant,"");
         if (invQryList.size() > 0) {
         int iIndex = 0;
          int irow = 0;
         double invqty,pcsqty = 0;
         double stockqty,diffqty=0;
         
         String strDiffQty,strpcsDiffQty=""; 
         
             for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
             	     
                         String result="";
                         Map lineArr = (Map) invQryList.get(iCnt);
                        
                         String item =(String)lineArr.get("ITEM");
                         String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                         JSONObject resultJsonInt = new JSONObject();
                         
                         invqty  =  Double.parseDouble((String)lineArr.get("QTY"));
                         pcsqty  =  Double.parseDouble((String)lineArr.get("PCSQTY"));
                         stockqty  =  Double.parseDouble((String)lineArr.get("STOCKQTY"));
                         diffqty  =  Double.parseDouble((String)lineArr.get("STOCKDIFFQTY"));
                         strDiffQty=Double.toString(stockqty+diffqty);
                         
                         /*strDiffQty=Double.toString(stockqty-invqty);
                         if(stockqty==0 && invqty!=0)
                         {
                         if(pcsqty>0)
                        	 strpcsDiffQty=Double.toString(stockqty-pcsqty);
                         else
                        	 strpcsDiffQty="0";	 
                         }
                         else
                         {
                         	if(pcsqty!=0)
                        	 		strpcsDiffQty="-"+Double.toString(pcsqty);
                         	else
                         		strpcsDiffQty=Double.toString(pcsqty);	
                         }*/	
                        	 
                         /*if(invqty >0 && stockqty<=0 && invqty != stockqty)
                         {
                         	strDiffQty=Double.toString(-invqty);
                         }
                         else if(invqty == stockqty)
                         {
                         	strDiffQty="0";
                         }
                         else if(stockqty > invqty || stockqty < invqty)
                         {
                         	strDiffQty=Double.toString(stockqty-invqty);
                         }
                         
                         else	
                         {
                         	strDiffQty=(String)lineArr.get("STOCKQTY");
                         }*/
                                                                       
                      /*if(invqty==0 &&  stockqty==0&& pcsqty==0){
                      }
                      else
                      {*/
                    	  resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM").toString().toUpperCase()));
                          resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
                          resultJsonInt.put("TRANDATE", strUtils.fString((String) lineArr.get("TRANDATE")));
                          resultJsonInt.put("TRANNO", strUtils.fString((String) lineArr.get("STKNO")));
                          resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
                          resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
                          resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
                          resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String)lineArr.get("ITEMDESC"))));
                          resultJsonInt.put("STKUOM", strUtils.fString((String) lineArr.get("STKUOM")));
                          resultJsonInt.put("BATCH", strUtils.fString((String) lineArr.get("BATCH")));
                          resultJsonInt.put("STOCKQTY", strUtils.formatNum((String) lineArr.get("STOCKQTY")));
                          //resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
                          resultJsonInt.put("QTY", strUtils.formatNum(strDiffQty));
                          resultJsonInt.put("PCSQTY", strUtils.formatNum((String) lineArr.get("PCSQTY")));
                          resultJsonInt.put("strDiffQty", strUtils.formatNum((String) lineArr.get("STOCKDIFFQTY")));
                          resultJsonInt.put("PCSDiffQty", strUtils.formatNum((String) lineArr.get("STOCKDIFFQTY")));
                          //resultJsonInt.put("strDiffQty", strUtils.formatNum(strDiffQty));
                          //resultJsonInt.put("PCSDiffQty", strUtils.formatNum(strpcsDiffQty));
                          resultJsonInt.put("USERID", strUtils.fString((String) lineArr.get("USERID")));
                          resultJsonInt.put("REMARKS", strUtils.fString((String) lineArr.get("REMARKS")));
                          jsonArray.add(resultJsonInt);
                     
                      //}
             }
             // End code modified by Bruhan for product brand on 11/9/12 
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
    	 	 jsonArray.add("");
    	 	 resultJson.put("items", jsonArray);    
    	 	 resultJson.put("SEARCH_DATA", "");
             JSONObject resultJsonInt = new JSONObject();
             resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
             resultJsonInt.put("ERROR_CODE", "98");
             jsonArrayErr.add(resultJsonInt);
             resultJson.put("ERROR", jsonArrayErr);
     }
    
     return resultJson;
}
 
 /* ************Modification History*********************************
	   Sep 23 2014, Description: To include USER 
	   April 29 2015, Description: To include REMARKS
	*/
 
 private JSONObject getStockTakeSummaryWithCost(HttpServletRequest request) {
     JSONObject resultJson = new JSONObject();
     JSONArray jsonArray = new JSONArray();
     JSONArray jsonArrayErr = new JSONArray();
    
     StrUtils strUtils = new StrUtils();
  
     try {
     
        String PLANT= strUtils.fString(request.getParameter("PLANT"));
        String LOC     = strUtils.fString(request.getParameter("LOC"));
        String ITEM    = strUtils.fString(request.getParameter("ITEM"));
        String BATCH   = strUtils.fString(request.getParameter("BATCH"));
        String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
        String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
        String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
        // Start code modified by Bruhan for product brand on 11/9/12 
        String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
        String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
        String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
        String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
		   String  USER = strUtils.fString(request.getParameter("USER"));
        Hashtable ht = new Hashtable();

         if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
         if(strUtils.fString(BATCH).length() > 0)               ht.put("a.BATCH",BATCH);
         //if(strUtils.fString(LOC_TYPE_ID).length() > 0)         ht.put("b.LOC_TYPE_ID",LOC_TYPE_ID);
         
         ArrayList invQryList= new InvUtil().getStockTakeDetails(ht,PLANT,ITEM,PRD_DESCRIP,PRD_CLS_ID,PRD_BRAND_ID,PRD_TYPE_ID,FROM_DATE,TO_DATE,LOC_TYPE_ID,USER);//"","","","",plant,"");
         if (invQryList.size() > 0) {
         int iIndex = 0;
          int irow = 0;
         double invqty = 0;
         double stockqty,pcsqty=0;
         
         String strDiffQty,strpcsDiffQty=""; 
         
             for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
             	     
                         String result="";
                         Map lineArr = (Map) invQryList.get(iCnt);
                        
                         String item =(String)lineArr.get("ITEM");
                         String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                         JSONObject resultJsonInt = new JSONObject();
                         
                         invqty  =  Double.parseDouble((String)lineArr.get("QTY"));
                         pcsqty  =  Double.parseDouble((String)lineArr.get("PCSQTY"));
                         stockqty  =  Double.parseDouble((String)lineArr.get("STOCKQTY"));
                        
                         strDiffQty=Double.toString(stockqty-invqty);
                         if(stockqty==0 && invqty!=0)
                         {
                         if(pcsqty>0)
                        	 strpcsDiffQty=Double.toString(stockqty-pcsqty);
                         else
                        	 strpcsDiffQty="0";	 
                         }
                         else
                         {
                         	if(pcsqty!=0)
                        	 		strpcsDiffQty="-"+Double.toString(pcsqty);
                         	else
                         		strpcsDiffQty=Double.toString(pcsqty);	
                         }
                         
                         /*if(invqty >0 && stockqty<=0 && invqty != stockqty)
                         {
                         	strDiffQty=Double.toString(-invqty);
                         }
                         else if(invqty == stockqty)
                         {
                         	strDiffQty="0";
                         }
                         else if(stockqty > invqty || stockqty < invqty)
                         {
                         	strDiffQty=Double.toString(stockqty-invqty);
                         }
                         else	
                         {
                         	strDiffQty=(String)lineArr.get("STOCKQTY");
                         }*/
                         double unitCost =Double.parseDouble((String)lineArr.get("UNITCOST"));
                        Double totalAvalableCost = unitCost*(new Double((String) lineArr.get("STOCKQTY")));
             			Double totalInventeryCost = unitCost*(new Double((String) lineArr.get("QTY")));
             			Double totalPcsCost = unitCost*(new Double((String) lineArr.get("PCSQTY")));
                        Double totalDiffentetCost = unitCost*(new Double(((String) strDiffQty)));
                        Double totalpcsDiffentetCost = unitCost*(new Double((String) strpcsDiffQty));
             			java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
                                                                       
                      if(invqty==0 &&  stockqty==0&& pcsqty==0){
                      }
                      else
                      {
                    	  
                    	  String stockQtyValue =(String)lineArr.get("STOCKQTY");
                    	  
                    	  float stockQtyVal="".equals(stockQtyValue) ? 0.0f :  Float.parseFloat(stockQtyValue);
                    	  
                    	  if(stockQtyVal==0f) {
                    		  stockQtyValue="0.000";
                    	  }else {
                    		  stockQtyValue=stockQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    	  }
                    	  
                    	  resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM").toString().toUpperCase()));
                          resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
                          resultJsonInt.put("TRANDATE", strUtils.fString((String) lineArr.get("TRANDATE")));
                          resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
                          resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
                          resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
                          resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String)lineArr.get("ITEMDESC"))));
                          resultJsonInt.put("STKUOM", strUtils.fString((String) lineArr.get("STKUOM")));
                          resultJsonInt.put("BATCH", strUtils.fString((String) lineArr.get("BATCH")));
                          resultJsonInt.put("UNITCOST", unitCost);
                          resultJsonInt.put("STOCKQTY", stockQtyValue);
                          resultJsonInt.put("totalAvalableCost", totalAvalableCost);
                          resultJsonInt.put("QTY", strUtils.formatNum((String)lineArr.get("QTY")));
                          resultJsonInt.put("totalInventeryCost", totalInventeryCost);
                          resultJsonInt.put("PCSQTY", strUtils.formatNum((String)lineArr.get("PCSQTY")));
                          resultJsonInt.put("totalPcsCost", totalPcsCost);
                          resultJsonInt.put("strDiffQty",  strUtils.formatNum(strDiffQty));
                          resultJsonInt.put("totalDiffentetCost",totalDiffentetCost);
                          resultJsonInt.put("strpcsDiffQty",  strUtils.formatNum(strpcsDiffQty));
                          resultJsonInt.put("totalpcsDiffentetCost",totalpcsDiffentetCost);
                          resultJsonInt.put("USERID", strUtils.fString((String) lineArr.get("USERID")));
                          resultJsonInt.put("REMARKS", strUtils.fString((String) lineArr.get("REMARKS")));
                          jsonArray.add(resultJsonInt);	  
                    	  
                    /* result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                     + "<td  align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("ITEM").toString().toUpperCase()) + "</td>"
                     + "<td  align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("LOC")) + "</td>"
                     + "<td  align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("TRANDATE")) + "</td>"
                     + "<td  align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("PRDCLSID")) + "</td>"
                     + "<td align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("ITEMTYPE")) + "</td>"
                     + "<td align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("PRD_BRAND_ID")) + "</td>"
                     + "<td align = left >&nbsp;&nbsp;&nbsp;" + sqlBean.formatHTML(strUtils.fString((String)lineArr.get("ITEMDESC"))) + "</td>"
                     + "<td  align = left >&nbsp;&nbsp;&nbsp;"+ strUtils.fString((String)lineArr.get("STKUOM")) + "</td>"
                     + "<td align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("BATCH")) + "</td>"
                     + "<td align = right >&nbsp;&nbsp;&nbsp;" + strUtils.currencyWtoutSymbol((String)lineArr.get("UNITCOST")) + "</td>"
                     + "<td align = right >&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("STOCKQTY")) + "</td>"
                     + "<td align = right >&nbsp;&nbsp;&nbsp;" + StrUtils.currencyWtoutSymbol(df.format(totalAvalableCost)) + "</td>"
                     + "<td align = right >&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("QTY")) + "</td>"
                     + "<td align = right >&nbsp;&nbsp;&nbsp;" + StrUtils.currencyWtoutSymbol(df.format(totalInventeryCost)) + "</td>"
                     + "<td align = right >&nbsp;&nbsp;&nbsp;" + strUtils.formatNum(strDiffQty) + "</td>"
                     + "<td align = right >&nbsp;&nbsp;&nbsp;" + StrUtils.currencyWtoutSymbol(df.format(totalDiffentetCost)) + "</td>"
					 + "<td align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("USERID")) + "</td>"
					  + "<td align = left >&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("REMARKS")) + "</td>"
                      + "</tr>" ;
                      }
                     irow=irow+1;
                     iIndex=iIndex+1;
                     
                     resultJsonInt.put("STOCKDETAILS", result);
                     jsonArray.add(resultJsonInt);*/
                      }
             }
             // End code modified by Bruhan for product brand on 11/9/12 
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
    	 	 jsonArray.add("");
    	 	 resultJson.put("items", jsonArray);  
             resultJson.put("SEARCH_DATA", "");
             JSONObject resultJsonInt = new JSONObject();
             resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
             resultJsonInt.put("ERROR_CODE", "98");
             jsonArrayErr.add(resultJsonInt);
             resultJson.put("ERROR", jsonArrayErr);
     }
    
     return resultJson;
}
	/*************************  Modification History  ******************************
     *** Bruhan,Oct 29 2014,Description: To include child item,work order number,Operation Sequence and remove Product class,type,brand
	*/ 
	/*private JSONObject getWipInventorysmry(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
        StrUtils strUtils = new StrUtils();
        try {
        
           String PLANT= strUtils.fString(request.getParameter("PLANT"));
           String LOC     = strUtils.fString(request.getParameter("LOC"));
           String ITEM    = strUtils.fString(request.getParameter("ITEM"));
           String BATCH   = strUtils.fString(request.getParameter("BATCH"));
           String WONO    = strUtils.fString(request.getParameter("WONO"));
           String CITEM = strUtils.fString(request.getParameter("CITEM"));
           String OPSEQ = strUtils.fString(request.getParameter("OPSEQ"));
           String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
           String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
           
            Hashtable ht = new Hashtable();

            if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.BATCH",BATCH);
            if(strUtils.fString(WONO).length() > 0)          ht.put("a.WONO",WONO);
            if(strUtils.fString(OPSEQ).length() > 0)         ht.put("a.OPSEQ",OPSEQ) ;  
            if(strUtils.fString(ITEM).length() > 0)        ht.put("a.PITEM",ITEM) ; 
            if(strUtils.fString(CITEM).length() > 0)        ht.put("a.CITEM",CITEM) ; 
            
            ArrayList invQryList= new WipUtil().getWipInventory(ht,PLANT,PRD_DESCRIP,LOC_TYPE_ID,LOC);
            if (invQryList.size() > 0) {
            int iIndex = 0;
             int irow = 0;
            double sumprdQty = 0;String lastProduct="";
                for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                      
                            String result="";
                            Map lineArr = (Map) invQryList.get(iCnt);
                            sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
                            String item =(String)lineArr.get("CITEM");
                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                       
                       if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(item))){
                        result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("WONO")) + "</td>"
                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("OPSEQ")) + "</td>"
                         + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("LOC")) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("CITEM")) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + sqlBean.formatHTML(strUtils.fString((String)lineArr.get("citemdesc"))) + "</td>"
                        + "<td  align = left>&nbsp;&nbsp;&nbsp;"+ strUtils.fString((String)lineArr.get("STKUOM")) + "</td>"
                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("BATCH")) + "</td>"
                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("QTY")) + "</td>"
                        + "</tr>" ;
                        if(iIndex+1 == invQryList.size()){
                                irow=irow+1;
                                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

                              result +=  "<TR bgcolor ="+bgcolor+" >"
                                 +"<TD colspan=6></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+strUtils.formatNum((new Double(sumprdQty).toString()))+"</b></TD> </TR>";
                                   
                         } }else{
                           sumprdQty = sumprdQty - Double.parseDouble((String)lineArr.get("QTY"));
                           result +=  "<TR bgcolor ="+bgcolor+" >"
                            +"<TD colspan=6></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+strUtils.formatNum((new Double(sumprdQty).toString()))+"</b></TD> </TR>";
                           irow=irow+1;
                           bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                            result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                            		 + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("WONO")) + "</td>"
                                     + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("OPSEQ")) + "</td>"
                                      + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("LOC")) + "</td>"
                                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("CITEM")) + "</td>"
                                     + "<td  align = left>&nbsp;&nbsp;&nbsp;" + sqlBean.formatHTML(strUtils.fString((String)lineArr.get("citemdesc"))) + "</td>"
                                     + "<td  align = left>&nbsp;&nbsp;&nbsp;"+ strUtils.fString((String)lineArr.get("STKUOM")) + "</td>"
                                     + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString((String)lineArr.get("BATCH")) + "</td>"
                                     + "<td align = right>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("QTY")) + "</td>"
                            + "</tr>" ;
                      
                        
                        sumprdQty =  Double.parseDouble((String)lineArr.get("QTY"));
                                 if(iIndex+1 == invQryList.size()){ 
                                     irow=irow+1;
                                     bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                        result +=  "<TR bgcolor ="+bgcolor+" >"
                            +"<TD colspan=6></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+strUtils.formatNum((new Double(sumprdQty).toString()))+"</b></TD> </TR>";
                        }
                        }
                             irow=irow+1;
                             iIndex=iIndex+1;
                             lastProduct = item;
                         
                            resultJsonInt.put("INVDETAILS", result);
                         
                            jsonArray.add(resultJsonInt);

                }
                // End code modified by Bruhan for product brand on 11/9/12 
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
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
 }*/
	private JSONObject getInventorysmryLocZeroQty(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
        StrUtils strUtils = new StrUtils();
     
        try {
               String PLANT= strUtils.fString(request.getParameter("PLANT"));
               String LOC     = strUtils.fString(request.getParameter("LOC"));
               String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
         
                Hashtable ht = new Hashtable();
                //if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
                if(strUtils.fString(LOC).length() > 0)          ht.put("LOC",LOC);
                if(strUtils.fString(LOC_TYPE_ID).length() > 0)  ht.put("LOC_TYPE_ID",LOC_TYPE_ID); 
               
            ArrayList invQryList= new InvUtil().getInvListSummaryLocZeroQty(PLANT,ht);
            
             if (invQryList.size() > 0) {
             int iIndex = 0;
              int irow = 0;
             double sumprdQty = 0;String lastProduct="";
                 for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                              String result="";
                             Map lineArr = (Map) invQryList.get(iCnt);
                            
                            // String loc =(String)lineArr.get("loc");
                             String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                             JSONObject resultJsonInt = new JSONObject();
                             
                             resultJsonInt.put("loc", strUtils.fString((String) lineArr.get("loc")));
                             resultJsonInt.put("locdesc", strUtils.fString((String) lineArr.get("locdesc")));
                             resultJsonInt.put("loc_type_id", strUtils.fString((String) lineArr.get("loc_type_id")));
                             resultJsonInt.put("loc_type_desc", strUtils.fString((String) lineArr.get("loc_type_desc")));
                             resultJsonInt.put("UOM", strUtils.fString((String) lineArr.get("STKUOM")));
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
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
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
	
	private JSONObject getInventorysmryAgingDetailsByDays(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        DateUtils _dateUtils =  new DateUtils();
       
        StrUtils strUtils = new StrUtils();
         try {
        
           String PLANT= strUtils.fString(request.getParameter("PLANT"));
           String LOC     = strUtils.fString(request.getParameter("LOC"));
           String ITEM    = strUtils.fString(request.getParameter("ITEM"));
           String BATCH   = strUtils.fString(request.getParameter("BATCH"));
           String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
           String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
           String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
           String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
           String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
           String curDate =_dateUtils.getDate();
           //String frmDate = curDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
           
           Hashtable ht = new Hashtable();

            if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
            //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
            //if(strUtils.fString(LOC_TYPE_ID).length() > 0)         ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
            ArrayList invQryList= new InvUtil().getInvListAgingSummaryByCost( PLANT,ITEM,PRD_DESCRIP,ht,LOC,LOC_TYPE_ID,curDate);//"","","","",plant,"");
            if (invQryList.size() > 0) {
            int iIndex = 0;
             int irow = 0;
            double sumprdQty = 0;
            double recQty = 0;
            double lastSumPrdQty = 0;
            String lastProduct="",lastDescription="",lastLocation="",lastBatch="",lastuom="";
            String item="",loc="",batch="",uom="";
            double days30=0;double days60=0;double days90=0;double days120=0;
            double prevdays30=0;double prevdays60=0;double prevdays90=0;double prevdays120=0;
            curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
                for (int iCnt =0; iCnt<invQryList.size(); iCnt++){                     
                             
                            String result="";
                            Map lineArr = (Map) invQryList.get(iCnt);
                            sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
                            
                            JSONObject resultJsonInt = new JSONObject();
                            
                            String recdate = strUtils.fString((String)lineArr.get("RECVDATE"));
                            item = (String)lineArr.get("ITEM");
                            loc= (String)lineArr.get("LOC");
                            batch = (String)lineArr.get("BATCH");
                            sumprdQty = Double.parseDouble((String)lineArr.get("QTY"));
                            recQty = Double.parseDouble((String)lineArr.get("RECQTY"));
                            uom=strUtils.fString((String)lineArr.get("STKUOM"));
                        	recdate = recdate.substring(0,2) + recdate.substring(3,5) + recdate.substring(6);
                        	Calendar cal1 = new GregorianCalendar();
     		               Calendar cal2 = new GregorianCalendar();
     		               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
     		               Date d1 = sdf.parse(recdate);
     		               cal1.setTime(d1);
     		               Date d2 = sdf.parse(curDate);
     		               cal2.setTime(d2);         		               
     		               Long diff = d2.getTime()-d1.getTime();         		               
     		               int days =  (int) (diff/(1000 * 60 * 60 * 24));
     		              if(days>0 && days<=30)
   		            	   {
   		            		   days30 = days30 + recQty;
   		            	   }
   		            	   if(days>30 && days<=60)
   		            	   {
   		            		   days60 = days60 + recQty;
   		            	   }
   		            	   if(days>60 && days<=90)
   		            	   {
   		            		   days90 = days90 + recQty;
   		            	   }
   		            	   if(days>90)
   		            	   {
   		            		   days120 = days120 + recQty;
   		            	   }
   		            	   
                           if(!(lastProduct.equalsIgnoreCase(item)&&lastLocation.equalsIgnoreCase(loc)&&lastBatch.equalsIgnoreCase(batch)&&lastSumPrdQty == sumprdQty)){
                        	   String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        	   
                        	   if(!lastProduct.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")){
                        		   irow=irow+1;
                        		   if(prevdays30>lastSumPrdQty){
                        			   prevdays30 = lastSumPrdQty;
                        		   }
                        		   if((prevdays30+prevdays60)>lastSumPrdQty){
                        			   prevdays60 = lastSumPrdQty-prevdays30;
                        		   }
                        		   if((prevdays30+prevdays60+prevdays90)>lastSumPrdQty){
                        			   prevdays90 = lastSumPrdQty-prevdays30-prevdays60;
                        		   }
                        		   if((prevdays30+prevdays60+prevdays90+prevdays120)>lastSumPrdQty){
                        			   prevdays120 = lastSumPrdQty-prevdays30-prevdays60-prevdays90;
                        		   }
                        		   /*result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                                           + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag(lastProduct)) + "</td>"
                                           + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag(lastDescription)) + "</td>"
                                           + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag(lastLocation)) + "</td>"
                                           + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag(lastBatch)) + "</td>"
                                           + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(Double.toString(lastSumPrdQty))) + "</td>"
                                           + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(Double.toString(prevdays30))) + "</td>"
                                           + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(Double.toString(prevdays60))) + "</td>"
                                           + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(Double.toString(prevdays90))) + "</td>"
                                           + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(Double.toString(prevdays120))) + "</td>"
                                           + "</tr>" ;*/
                        		   resultJsonInt.put("item", strUtils.fString(StrUtils.forHTMLTag(lastProduct)));
                        		   resultJsonInt.put("ITEMDESC", strUtils.fString(StrUtils.forHTMLTag(lastDescription)));
                        		   resultJsonInt.put("loc", strUtils.fString(StrUtils.forHTMLTag(lastLocation)));
                        		   resultJsonInt.put("batch", strUtils.fString(StrUtils.forHTMLTag(lastBatch)));
                        		   resultJsonInt.put("uom", strUtils.fString(StrUtils.forHTMLTag(lastuom)));
                        		   resultJsonInt.put("sumprdQty", strUtils.fString(StrUtils.formatNum(String.valueOf(lastSumPrdQty))));
                        		   resultJsonInt.put("days30", strUtils.fString(StrUtils.formatNum(String.valueOf(prevdays30))));
                        		   resultJsonInt.put("days60", strUtils.fString(StrUtils.formatNum(String.valueOf(prevdays60))));
                        		   resultJsonInt.put("days90", strUtils.fString(StrUtils.formatNum(String.valueOf(prevdays90))));
                        		   resultJsonInt.put("days120", strUtils.fString(StrUtils.formatNum(String.valueOf(prevdays120))));
                        		   days30=0;days60=0;days90=0;days120=0;
                        		   //prevdays30=0;prevdays60=0;prevdays90=0;prevdays120=0;
                        		   if(days>0 && days<=30)
           		            	   {
           		            		   days30 = days30 + recQty;
           		            	   }
           		            	   if(days>30 && days<=60)
           		            	   {
           		            		   days60 = days60 + recQty;
           		            	   }
           		            	   if(days>60 && days<=90)
           		            	   {
           		            		   days90 = days90 + recQty;
           		            	   }
           		            	   if(days>90)
           		            	   {
           		            		   days120 = days120 + recQty;
           		            	   }
                        	   }
                           }   
                           if(iIndex+1 == invQryList.size()){
                    		   irow=irow+1;
       		            	  String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       		            	   if(days30>sumprdQty){
                 			   days30 = sumprdQty;
                     		   }
       		            	   if((days30+days60)>sumprdQty){
                     			   days60 = sumprdQty-days30;
                     		   }
       		            	   if((days30+days60+days90)>sumprdQty){
                     			   days90 = sumprdQty-days30-days60;
                     		   }
       		            	   if((days30+days60+days90+days120)>sumprdQty){
                     			   days120 = sumprdQty-days30-days60-days90;
                     		   }
                    		   /*result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
                                       + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag(item)) + "</td>"
                                       + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC"))) + "</td>"
                                       + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag(loc)) + "</td>"
                                       + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.forHTMLTag(batch)) + "</td>"
                                       + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(sumprdQty)))) + "</td>"
                                       + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days30)))) + "</td>"
                                       + "<td  align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days60)))) + "</td>"
                                       + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days90)))) + "</td>"
                                       + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days120)))) + "</td>"
                                       + "</tr>" ;*/
       		            	resultJsonInt.put("item", strUtils.fString(StrUtils.forHTMLTag(item)));
       		            	resultJsonInt.put("ITEMDESC", strUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC"))));
       		            	resultJsonInt.put("loc", strUtils.fString(StrUtils.forHTMLTag(loc)));
       		            	resultJsonInt.put("batch", strUtils.fString(StrUtils.forHTMLTag(batch)));
       		            	resultJsonInt.put("uom", strUtils.fString(StrUtils.forHTMLTag(uom)));
       		            	resultJsonInt.put("sumprdQty", strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(sumprdQty)))));
       		            	resultJsonInt.put("days30", strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days30)))));
       		            	resultJsonInt.put("days60", strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days60)))));
       		            	resultJsonInt.put("days90", strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days90)))));
       		            	resultJsonInt.put("days120", strUtils.fString(StrUtils.formatNum(StrUtils.formattwodecNum(String.valueOf(days120)))));
                    	   }
                         
                         iIndex=iIndex+1;
                         lastProduct = item;
                         lastLocation= loc;
                         lastBatch = batch;
                         lastuom = uom;
                         lastSumPrdQty = sumprdQty;
                         lastDescription = (String)lineArr.get("ITEMDESC");
                         prevdays30 = days30;
                         prevdays60 = days60;
                         prevdays90 = days90;
                         prevdays120 = days120;
                     
                        //resultJsonInt.put("INVDETAILS", result);
                        if (resultJsonInt.size() > 0){
                        	jsonArray.add(resultJsonInt);
                        }

                }
                // End code modified by Lakshmi for product brand on 11/9/12 
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
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	 private JSONObject getInventoryVsOutgoing(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	       String link ="";
	        StrUtils strUtils = new StrUtils();
	     
	        try {
					   String PLANT= strUtils.fString(request.getParameter("PLANT"));
					   String ITEM    = strUtils.fString(request.getParameter("ITEM"));
					   String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
					   String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
					   String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
					   String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
					   String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
		               String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
		               String fdate ="",tdate="";
		                if (FROM_DATE.length()>5)
		                       fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
		                       
		                if (TO_DATE.length()>5){
		                        tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
		                }else{
		                	tdate = new DateUtils().getDateFormatyyyyMMdd();
		                }
		                
				        Hashtable ht = new Hashtable();
				        if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
						if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("itemtype",PRD_TYPE_ID); 
						if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID);  
	                      
	        
	            ArrayList invQryList= new InvUtil().getInvVsOutgoing(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate);
	            
	             if (invQryList.size() > 0) {
	             int iIndex = 0;
	             
	                          for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	                              String result="";
	                             Map lineArr = (Map) invQryList.get(iCnt);
	                            String item =(String)lineArr.get("ITEM");

	    				       // link = "<a href=\"" + "prdRIDetails.jsp?ITEM=" + strUtils.replaceCharacters2Str(item) +"&FROM_DATE=" + fdate + "&TO_DATE=" + tdate+"\">";

	                             String bgcolor = ((iIndex == 0) || (iIndex % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                             JSONObject resultJsonInt = new JSONObject();
	                             resultJsonInt.put("ITEM",strUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("ITEM"))));
	                             resultJsonInt.put("ITEMDESC",sqlBean.formatHTML(StrUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC")))));
	                             resultJsonInt.put("UOM",strUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("STKUOM"))));
	                             resultJsonInt.put("TOTRECV",strUtils.formatNum((String)lineArr.get("TOTRECV")));
	                             resultJsonInt.put("TOTAL_ISS",strUtils.formatNum((String)lineArr.get("TOTAL_ISS")));
	                             resultJsonInt.put("STOCKONHAND",strUtils.formatNum(StrUtils.fString((String)lineArr.get("STOCKONHAND"))));
	                             jsonArray.add(resultJsonInt);
								  /*result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
								  + "<td  align = left>&nbsp;&nbsp;&nbsp;" + link+  strUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("ITEM"))) + "</a></td>"
								    + "<td  align = left>&nbsp;&nbsp;&nbsp;" + sqlBean.formatHTML(StrUtils.fString(StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC")))) + "</td>"
								+ "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("TOTRECV")) + "</td>"
								  + "<td align = left>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("TOTAL_ISS")) + "</td>"
								  + "<td  align = right>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum(StrUtils.fString((String)lineArr.get("STOCKONHAND"))) + "</td>"
								  + "</tr>" ;
								 
	                        
	                              
	                              iIndex=iIndex+1;
	                             
	                            
	                             resultJsonInt.put("PRODUCT", result);
	                          
	                             jsonArray.add(resultJsonInt);*/

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
	        		jsonArray.add("");
	        		resultJson.put("items", jsonArray);
	                resultJson.put("SEARCH_DATA", "");
	                resultJson.put("TOTAL_QTY", 0);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
	}
	 
	 /*private JSONObject getInventorySummaryAlternateBrand(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();

			StrUtils strUtils = new StrUtils();

			try {
				String PLANT = strUtils.fString(request.getParameter("PLANT"));
				String LOC = strUtils.fString(request.getParameter("LOC"));
				String ITEM = strUtils.fString(request.getParameter("ITEM"));
				String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
				String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
				String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
				String VINNO = strUtils.fString(request.getParameter("VINNO"));
				String MODEL = strUtils.fString(request.getParameter("MODEL"));
				String VIEWALTERNATE = strUtils.fString(request.getParameter("VIEWALTERNATE"));
				String CNAME = strUtils.fString(request.getParameter("CUSTOMER"));
				String UOM = strUtils.fString(request.getParameter("UOM"));
				Hashtable ht = new Hashtable();
				if (strUtils.fString(ITEM).length() > 0)
				{									
					//Check Item in ALTERNATEBRAND
					ArrayList itemList = new InvUtil().getItemListFromAlterItem(PLANT,ITEM);
					if (itemList.size() > 0) {
						Map lineArrIm = (Map) itemList.get(0);
						ITEM=strUtils.fString((String) lineArrIm.get("ITEM"));						
					}
					ht.put("A.ITEM", ITEM);
				}
				
				int itemcount=0;
				String NewItem="";
				if(VIEWALTERNATE.equalsIgnoreCase("1"))
					{
//				ArrayList itemList = new InvUtil().getItemList(PLANT,ht,PRD_DESCRIP,CNAME);getItemListUom
					ArrayList itemList = new InvUtil().getItemListUom(PLANT,ht,PRD_DESCRIP,CNAME,UOM);
				if (itemList.size() > 0) {
					
					for (int iCnt1 = 0; iCnt1 < itemList.size(); iCnt1++) {						
						Map lineArrIm = (Map) itemList.get(iCnt1);
						ITEM=strUtils.fString((String) lineArrIm.get("ITEM"));
						if(NewItem!=ITEM)
						{
							NewItem=ITEM;
						Hashtable htft = new Hashtable();
						if (strUtils.fString(ITEM).length() > 0)
							htft.put("A.ITEM", ITEM);
						ArrayList ftritemList = new InvUtil().getItemList(PLANT,htft,PRD_DESCRIP,CNAME);
						if(itemcount==0)
							itemcount=ftritemList.size();
						}
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("Id", "0");
						resultJsonInt.put("ITEM", ITEM);
	                    resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArrIm.get("ITEMDESC")));
	                    resultJsonInt.put("CATALOG", "<img  width='130px' src='" +strUtils.fString((String) lineArrIm.get("CATALOG"))+"'>");
	                    resultJsonInt.put("BRAND", strUtils.fString((String) lineArrIm.get("BRAND")));
	                    resultJsonInt.put("LOC", strUtils.fString((String) lineArrIm.get("LOC")));
	                    resultJsonInt.put("QTY", strUtils.fString((String) lineArrIm.get("QTY")));
	                    resultJsonInt.put("STKUOM", strUtils.fString((String)lineArrIm.get("STKUOM")));
                        resultJsonInt.put("STKQTY", strUtils.fString((String) lineArrIm.get("STKQTY")));
	                    resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArrIm.get("INVENTORYUOM")));
	                    resultJsonInt.put("INVUOMQTY", strUtils.fString((String) lineArrIm.get("INVUOMQTY")));
	                    resultJsonInt.put("VINNO", strUtils.fString((String) lineArrIm.get("VINNO")));
	                    resultJsonInt.put("MODEL", strUtils.fString((String) lineArrIm.get("MODEL")));
	                    
	                    
	                    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	                    String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	                    
	                    double prval = 0;
	                    prval = Double.valueOf((String) lineArrIm.get("UNITPRICE"));
	                    String OBDiscount=strUtils.fString((String) lineArrIm.get("DISCOUNTBY"));
	                    int plusIndex = OBDiscount.indexOf("%");
	                    if (plusIndex != -1) {
	                   	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	                   	 prval=prval-((prval*Double.valueOf(OBDiscount))/100);
	                        }
	                    else
	                    {
	                    	if(OBDiscount!="")
	                    	{
	                    		prval=Double.valueOf(OBDiscount);
	                    	}
	                    }
	     			   BigDecimal bd = new BigDecimal(prval);	     			 
	     			 DecimalFormat format = new DecimalFormat("#.#####");		
	     			 format.setRoundingMode(RoundingMode.FLOOR);
	     			String priceval = format.format(bd);
	     			
	     			priceval = strUtils.addZeroes(Double.parseDouble(priceval), numberOfDecimal);
	                    resultJsonInt.put("UNITPRICE", priceval);
	                    
	                    if (strUtils.fString(ITEM).length() > 0)	                    
	    					ht.remove("A.ITEM");
	                    if (strUtils.fString(PRD_DESCRIP).length() > 0)	                    
	    					ht.remove("A.ITEMDESC");
	                    VINNO = strUtils.fString(request.getParameter("VINNO"));
	    				MODEL = strUtils.fString(request.getParameter("MODEL"));
	    				String VINNO1 = strUtils.fString(request.getParameter("VINNO"));
	    				String MODEL1 = strUtils.fString(request.getParameter("MODEL"));
	    				if(VINNO=="" && MODEL=="")
	    				{
	    					jsonArray.add(resultJsonInt);	    				
	    					
	    				}
	    				
	    				if(VINNO!="")
	    				{
	                    if (VINNO.contentEquals(strUtils.fString((String) lineArrIm.get("VINNO"))))
	                    {
	                    	VINNO="";
	                    	
	                    	if(MODEL!="")
		    				{
	                    	if (MODEL.contentEquals(strUtils.fString((String) lineArrIm.get("MODEL"))))
		                    	MODEL="";
		    				}
	                    	if(MODEL!="")
	                    		MODEL="";
	                    }
	    				}
	    				if(MODEL!="")
	    				{
	                    if (MODEL.contentEquals(strUtils.fString((String) lineArrIm.get("MODEL"))))
	                    	MODEL="";
	    				}
	                    ht.put("A.ITEM", ITEM);
	                    itemcount=itemcount-1;
	                    
				ArrayList invQryList = new InvUtil().getInvListSummaryAlternateBrand(PLANT, ITEM, PRD_DESCRIP, ht, LOC,
						LOC_TYPE_ID,VINNO,MODEL,CNAME,LOC_TYPE_ID2);// "","","","",plant,"")
				if (invQryList.size() > 0) {					
					if(VINNO1!="" && MODEL1!="")    				
    					jsonArray.add(resultJsonInt);	    				
					else if(VINNO1!="" && MODEL1=="")    				
    					jsonArray.add(resultJsonInt);
					else if(VINNO1=="" && MODEL1!="")    				
    					jsonArray.add(resultJsonInt);                    
					int iIndex = 0;
					int irow = 0;
					double sumprdQty = 0;					
					String lastProduct = "";
					if(itemcount==0)
                    {
					for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
						String result = "";
						Map lineArr = (Map) invQryList.get(iCnt);
						sumprdQty = sumprdQty + Double.parseDouble((String) lineArr.get("QTY"));
						String item = (String) lineArr.get("ALTERNATEITEM");
						String loc = (String) lineArr.get("LOC");
						String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
						prval=Double.parseDouble((String) lineArr.get("UNITPRICE"));
						OBDiscount=strUtils.fString((String) lineArr.get("DISCOUNTBY"));
	                    plusIndex = OBDiscount.indexOf("%");
	                    if (plusIndex != -1) {
	                   	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	                   	 prval=prval-((prval*Double.valueOf(OBDiscount))/100);
	                        }
	                    else
	                    {
	                    	if(OBDiscount!="")
	                    	{
	                    		prval=Double.valueOf(OBDiscount);
	                    	}
	                    }
						 bd = new BigDecimal(prval);
						format = new DecimalFormat("#.#####");		
						 format.setRoundingMode(RoundingMode.FLOOR);
						 priceval = format.format(bd);
						resultJsonInt.put("Id", "1");
	                    resultJsonInt.put("ITEM", item);
	                    resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ALTERNATEITEMDESC")));
	                    resultJsonInt.put("CATALOG", "<img width='130px' src='" +strUtils.fString((String) lineArr.get("CATALOG"))+"' >  ");
	                    resultJsonInt.put("BRAND", strUtils.fString((String) lineArr.get("ALTERNATEBRAND")));
	                    resultJsonInt.put("ALTERNATEITEM", strUtils.fString((String) lineArr.get("ALTERNATEITEM")));
	                    resultJsonInt.put("ALTERNATEITEMDESC", strUtils.fString((String) lineArr.get("ALTERNATEITEMDESC")));
	                    resultJsonInt.put("ALTERNATEBRAND", sqlBean.formatHTML(StrUtils.fString((String) lineArr.get("ALTERNATEBRAND"))));
	                    resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
	                    resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
	                    resultJsonInt.put("VINNO", strUtils.fString((String) lineArr.get("VINNO")));
	                    resultJsonInt.put("MODEL", strUtils.fString((String) lineArr.get("MODEL")));
	                    resultJsonInt.put("UNITPRICE", priceval);
						jsonArray.add(resultJsonInt);
						itemcount=0;
						NewItem="";
					}
                    }
					resultJson.put("items", jsonArray);
					resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
				}
				else {
					if(VINNO=="" && MODEL=="")
					{
					resultJson.put("items", jsonArray);
					resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
					}
				
					}
					}
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
					}
				else
				{
					ArrayList invQryList = new InvUtil().getInvListSummaryDetail(PLANT, ITEM, PRD_DESCRIP, ht, LOC,
							LOC_TYPE_ID,VINNO,MODEL,CNAME);
					if (invQryList.size() > 0) {						
						int iIndex = 0;
						int irow = 0;
						double sumprdQty = 0;
						double prval = 0;
						String lastProduct = "";
						JSONObject resultJsonInt = new JSONObject();
						for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
							String result = "";
							Map lineArr = (Map) invQryList.get(iCnt);
							sumprdQty = sumprdQty + Double.parseDouble((String) lineArr.get("QTY"));
							String item = (String) lineArr.get("ITEM");
							String loc = (String) lineArr.get("LOC");
							String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
							prval=Double.parseDouble((String) lineArr.get("UNITPRICE"));
							String OBDiscount=strUtils.fString((String) lineArr.get("DISCOUNTBY"));
		                    int plusIndex = OBDiscount.indexOf("%");
		                    if (plusIndex != -1) {
		                   	 	OBDiscount = OBDiscount.substring(0, plusIndex);
		                   	 prval=prval-((prval*Double.valueOf(OBDiscount))/100);
		                        }
		                    else
		                    {
		                    	if(OBDiscount!="")
		                    	{
		                    		prval=Double.valueOf(OBDiscount);
		                    	}
		                    }
							BigDecimal bd = new BigDecimal(prval);
							DecimalFormat format = new DecimalFormat("#.#####");		
							 format.setRoundingMode(RoundingMode.FLOOR);
							String priceval = format.format(bd);
							resultJsonInt.put("Id", "1");
		                    resultJsonInt.put("ITEM", item);
		                    resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
		                    resultJsonInt.put("CATALOG", "<img width='130px' src='" +strUtils.fString((String) lineArr.get("CATALOG"))+"' >  ");
		                    resultJsonInt.put("BRAND", strUtils.fString((String) lineArr.get("BRAND")));
		                    resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
		                    resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
		                    resultJsonInt.put("VINNO", strUtils.fString((String) lineArr.get("VINNO")));
		                    resultJsonInt.put("MODEL", strUtils.fString((String) lineArr.get("MODEL")));
		                    resultJsonInt.put("UNITPRICE", priceval);
							jsonArray.add(resultJsonInt);
						}
						resultJson.put("items", jsonArray);
						resultJsonInt = new JSONObject();
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
				}
				
			} catch (Exception e) {
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("SEARCH_DATA", "");
				resultJson.put("TOTAL_QTY", 0);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}*/
	 
	 private JSONObject getInventorySummaryAlternateBrand(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		
		StrUtils strUtils = new StrUtils();
		try {
			String PLANT = strUtils.fString(request.getParameter("PLANT"));
			String LOC = strUtils.fString(request.getParameter("LOC"));
			String ITEM = strUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
			String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
			String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
			String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
			String MODEL = strUtils.fString(request.getParameter("MODEL"));
			String VIEWALTERNATE = strUtils.fString(request.getParameter("VIEWALTERNATE"));
			String CNAME = strUtils.fString(request.getParameter("CUSTOMER"));
			String UOM = strUtils.fString(request.getParameter("UOM"));
			
			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
            String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            
			Hashtable ht = new Hashtable();
			ht.put("ITEM", ITEM);
			ht.put("ITEM_DESC", PRD_DESCRIP);
			ht.put("LOC", LOC);
			ht.put("LOC_TYPE_ID", LOC_TYPE_ID);
			ht.put("LOC_TYPE_ID2", LOC_TYPE_ID2);
			ht.put("LOC_TYPE_ID3", LOC_TYPE_ID3);
			ht.put("CNAME", CNAME);
			ht.put("MODEL", MODEL);
			ht.put("UOM", UOM);
			
			ArrayList otherItemList = new InvUtil().getOtherProduct(PLANT,ht);
			ArrayList itemList = new InvUtil().getParentChildProduct(PLANT,ht);
			
			if (otherItemList.size() > 0) {
				for (int iCnt1 = 0; iCnt1 < otherItemList.size(); iCnt1++) {	
					Map lineArrIm = (Map) otherItemList.get(iCnt1);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("GROUP", strUtils.fString((String) lineArrIm.get("P_GROUP")));
					resultJsonInt.put("ITEM", strUtils.fString((String) lineArrIm.get("ITEM")));
                    resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArrIm.get("ITEMDESC")));
                    resultJsonInt.put("CATALOG", "<img style='width: 100%;' src='" +strUtils.fString((String) lineArrIm.get("CATALOG"))+"'>");
                    resultJsonInt.put("BRAND", strUtils.fString((String) lineArrIm.get("PRD_BRAND_ID")));
                    resultJsonInt.put("MODEL", strUtils.fString((String) lineArrIm.get("MODEL")));
                    resultJsonInt.put("LOC", strUtils.fString((String) lineArrIm.get("LOC")));
                    resultJsonInt.put("QTY", strUtils.fString((String) lineArrIm.get("QTY")));
                    resultJsonInt.put("STKUOM", strUtils.fString((String)lineArrIm.get("STKUOM")));
                    resultJsonInt.put("STKQTY", strUtils.fString((String) lineArrIm.get("STKQTY")));
                    resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArrIm.get("INVENTORYUOM")));
                    resultJsonInt.put("AVAILABLE_QUANTITY", strUtils.fString((String) lineArrIm.get("AVAILABLE_QUANTITY")));
                    resultJsonInt.put("ESTQTY", strUtils.fString((String) lineArrIm.get("ESTQTY")));
                    resultJsonInt.put("INVUOMQTY", strUtils.fString((String) lineArrIm.get("INVUOMQTY")));
                    resultJsonInt.put("SALESUOM", strUtils.fString((String) lineArrIm.get("SALESUOM")));
                    resultJsonInt.put("NONSTKFLAG", strUtils.fString((String) lineArrIm.get("NONSTKFLAG")));
                    
                    double prval = 0;
                    prval = Double.valueOf((String) lineArrIm.get("UnitPrice"));
                    String OBDiscount=strUtils.fString((String) lineArrIm.get("DISCOUNTBY"));
                    int plusIndex = OBDiscount.indexOf("%");
                    if (plusIndex != -1) {
                   	 	OBDiscount = OBDiscount.substring(0, plusIndex);
                   	 	prval=prval-((prval*Double.valueOf(OBDiscount))/100);
                     }else{
                    	if(OBDiscount!="")
                    	{
                    		prval=Double.valueOf(OBDiscount);
                    	}
                     }
     			   	BigDecimal bd = new BigDecimal(prval);	     			 
     			   	DecimalFormat format = new DecimalFormat("#.#####");		
     			   	format.setRoundingMode(RoundingMode.FLOOR);
     			   	String priceval = format.format(bd);
     			
     			   	priceval = strUtils.addZeroes(Double.parseDouble(priceval), numberOfDecimal);
     			   	
                    resultJsonInt.put("UNITPRICE", priceval);
                    jsonArray.add(resultJsonInt);
				}
				if (itemList.size() > 0) {
					for (int iCnt1 = 0; iCnt1 < itemList.size(); iCnt1++) {	
						Map lineArrIm = (Map) itemList.get(iCnt1);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("GROUP", strUtils.fString((String) lineArrIm.get("P_GROUP")));
						resultJsonInt.put("ITEM", strUtils.fString((String) lineArrIm.get("ITEM")));
	                    resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArrIm.get("ITEMDESC")));
	                    resultJsonInt.put("CATALOG", "<img style='width: 100%;' src='" +strUtils.fString((String) lineArrIm.get("CATALOG"))+"'>");
	                    resultJsonInt.put("BRAND", strUtils.fString((String) lineArrIm.get("PRD_BRAND_ID")));
	                    resultJsonInt.put("MODEL", strUtils.fString((String) lineArrIm.get("MODEL")));
	                    resultJsonInt.put("LOC", strUtils.fString((String) lineArrIm.get("LOC")));
	                    resultJsonInt.put("QTY", strUtils.fString((String) lineArrIm.get("QTY")));
	                    resultJsonInt.put("STKUOM", strUtils.fString((String)lineArrIm.get("STKUOM")));
	                    resultJsonInt.put("STKQTY", strUtils.fString((String) lineArrIm.get("STKQTY")));
	                    resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArrIm.get("INVENTORYUOM")));
	                    resultJsonInt.put("AVAILABLE_QUANTITY", strUtils.fString((String) lineArrIm.get("AVAILABLE_QUANTITY")));
	                    resultJsonInt.put("ESTQTY", strUtils.fString((String) lineArrIm.get("ESTQTY")));
	                    resultJsonInt.put("INVUOMQTY", strUtils.fString((String) lineArrIm.get("INVUOMQTY")));
	                    resultJsonInt.put("SALESUOM", strUtils.fString((String) lineArrIm.get("SALESUOM")));
	                    resultJsonInt.put("NONSTKFLAG", strUtils.fString((String) lineArrIm.get("NONSTKFLAG")));
	                    
	                    double prval = 0;
	                    prval = Double.valueOf((String) lineArrIm.get("UnitPrice"));
	                    String OBDiscount=strUtils.fString((String) lineArrIm.get("DISCOUNTBY"));
	                    int plusIndex = OBDiscount.indexOf("%");
	                    if (plusIndex != -1) {
	                   	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	                   	 	prval=prval-((prval*Double.valueOf(OBDiscount))/100);
	                     }else{
	                    	if(OBDiscount!="")
	                    	{
	                    		prval=Double.valueOf(OBDiscount);
	                    	}
	                     }
	     			   	BigDecimal bd = new BigDecimal(prval);	     			 
	     			   	DecimalFormat format = new DecimalFormat("#.#####");		
	     			   	format.setRoundingMode(RoundingMode.FLOOR);
	     			   	String priceval = format.format(bd);
	     			
	     			   	priceval = strUtils.addZeroes(Double.parseDouble(priceval), numberOfDecimal);
	     			   	
	                    resultJsonInt.put("UNITPRICE", priceval);
	                    jsonArray.add(resultJsonInt);
					}
				}
				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);   
			}else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		}catch(Exception e) {
			jsonArray.add("");
			resultJson.put("items", jsonArray);
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}		
		return resultJson;
	 }
	 
		private JSONObject getPackingIssue(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        HTReportUtil movHisUtil       = new HTReportUtil();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList movQryList  = new ArrayList();
	        
	        PackingUtil packingUtil = new PackingUtil();
	        
	        
	        DecimalFormat decformat = new DecimalFormat("#,##0.00");
	       
	        StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="";

	        try {
	        	
	           String PLANT= strUtils.fString(request.getParameter("PLANT"));
	           String ORDERNO    = strUtils.fString(request.getParameter("ORDERNO"));
	           String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	           String FROM_DATE   = strUtils.fString(request.getParameter("FDATE"));
	           String TO_DATE = strUtils.fString(request.getParameter("TDATE"));
	           String  DIRTYPE = strUtils.fString(request.getParameter("DTYPE"));
	           String  CUSTNO = strUtils.fString(request.getParameter("CNAME"));
	           String  PLNO = strUtils.fString(request.getParameter("PLNO"));
	           String  JOBNO = strUtils.fString(request.getParameter("JOBNO"));
	           String  PICKSTATUS = strUtils.fString(request.getParameter("PICKSTATUS"));
	           String  ISSUESTATUS = strUtils.fString(request.getParameter("ISSUESTATUS"));
	           String  DNNO = strUtils.fString(request.getParameter("DNNO"));
	           String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	           String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	           String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));           
	           String SORT = strUtils.fString(request.getParameter("SORT"));
	           String CustName = strUtils.fString(request.getParameter("CustName"));
	           String INVOICENO = strUtils.fString(request.getParameter("INVOICENO"));
	           String GINO = strUtils.fString(request.getParameter("GINO"));
	           String  CUSTNAME ="";
	           
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =_dateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	           	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	              
	   
	           Hashtable ht = new Hashtable();
	           if(strUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
	           if(strUtils.fString(ORDERNO).length() > 0)       ht.put("DONO",ORDERNO);
	           if(strUtils.fString(DNNO).length() > 0)      ht.put("DNNO",DNNO);
	           if(strUtils.fString(CUSTNO).length() > 0)     ht.put("CustName",strUtils.InsertQuotes(CUSTNO));
	           if(strUtils.fString(PLNO).length() > 0)    ht.put("PLNO",PLNO);
	           if(strUtils.fString(ISSUESTATUS).length() > 0)  ht.put("STATUS",ISSUESTATUS);
	           if(strUtils.fString(PICKSTATUS).length() > 0)   ht.put("PickStaus",PICKSTATUS);
	           if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
		        if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
		        if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);	        
		        if(strUtils.fString(CustName).length() > 0) ht.put("CustName",CustName);
		        if(strUtils.fString(INVOICENO).length() > 0) ht.put("INVOICENO",INVOICENO);
		        if(strUtils.fString(GINO).length() > 0) ht.put("GINO",GINO);
	  
		       	movQryList = packingUtil.getPackingIssueSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTNAME,SORT);
		       
	           
	            if (movQryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	             String  customerstatusid="",customerstatusdesc="",customertypeid="",customertypedesc="";;
	            double sumprdQty = 0;String lastProduct="";
	            
	            double totalIssPrice=0,totaltax=0,totIssPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0,TotalIssPrice=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
	            double issPriceGrandTot=0;
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                      
	                        String result="",custcode="";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        
	            //           	 custcode=(String)lineArr.get("custcode");
	                      
	                        String plno = (String)lineArr.get("PLNO");
	                        String custname = (String)lineArr.get("CustName");
	                        String JOBNUM = (String)lineArr.get("JobNum");
	                 //       Float gstpercentage =  Float.parseFloat(lineArr.get("Tax") == null ? "0.0" : ((String) lineArr.get("Tax").toString())) ;
	            			            			
	                       
	          	                                    
	                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        JSONObject resultJsonInt = new JSONObject();
	                        
	                      /*  customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT,(String)lineArr.get("custname"));
	                        if(customerstatusid == null || customerstatusid.equals(""))
	            			   {
	            			   customerstatusdesc="";
	            			   }
	            			   else
	            			   {
	            			    customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT,customerstatusid);
	            			   }
	                        
	                        customertypeid = customerBeanDAO.getCustomerTypeId(PLANT,(String)lineArr.get("custname"));
	              			 if(customertypeid == null || customertypeid.equals(""))
	              			 {
	              				customertypedesc="";
	              			 }
	              			 else
	              			 {
	              				customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT,customertypeid);
	              			 }
	              			 */
	              			/*String shippingcost =(String)lineArr.get("shippingcost");
	               			String qtyPickValue =(String)lineArr.get("qty");
	               			String qtyValue =(String)lineArr.get("qty");
	               			String unitprice = (String)lineArr.get("rate");

	               			
	                        float shippingcostVal ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost);
	                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
	                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
	                        float unitPriceVal="".equals(unitprice) ? 0.0f :  Float.parseFloat(unitprice);
	                        
	                        if(shippingcostVal==0f){
	                        	shippingcost="0.00000";
	                        }else{
	                        	shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(qtyPickVal==0f){
	                        	qtyPickValue="0.000";
	                        }else{
	                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(qtyVal==0f){
	                        	qtyValue="0.000";
	                        }else{
	                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(unitPriceVal==0f){
	                        	unitprice="0.00000";
	                        }else{
	                        	unitprice=unitprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }*/
	              			 
	              			 
	                      
	              			
	              			 Index = Index + 1;
	                       	
	                        resultJsonInt.put("Index",Index);
	                        resultJsonInt.put("HID", strUtils.fString((String)lineArr.get("HID")));
	                        resultJsonInt.put("INVOICENO", strUtils.fString((String)lineArr.get("INVOICENO")));
	                        resultJsonInt.put("DONO", strUtils.fString((String)lineArr.get("DONO")));
	                        resultJsonInt.put("GINO", strUtils.fString((String)lineArr.get("GINO")));
	                        resultJsonInt.put("CUSTNAME", custname);
	                        resultJsonInt.put("DATE", strUtils.fString((String)lineArr.get("CollectionDate")));
	                        resultJsonInt.put("plno",(plno));
	                        resultJsonInt.put("dnno", strUtils.fString((String)lineArr.get("DNNO")));
	                        resultJsonInt.put("TOTALNETWEIGHT", strUtils.fString((String)lineArr.get("TOTALNETWEIGHT")));
	                        resultJsonInt.put("TOTALGROSSWEIGHT", strUtils.fString((String)lineArr.get("TOTALGROSSWEIGHT")));
	                        resultJsonInt.put("NETPACKING", strUtils.fString((String)lineArr.get("NETPACKING")));
	                        resultJsonInt.put("NETDIMENSION", strUtils.fString((String)lineArr.get("NETDIMENSION")));
	                        resultJsonInt.put("custname", (custname));
	                        resultJsonInt.put("JOBNUM", (JOBNUM));
	                        resultJsonInt.put("remarks", strUtils.fString((String)lineArr.get("remarks")));
	                        resultJsonInt.put("STATUS", strUtils.fString((String)lineArr.get("DELSTS")));
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
	        	e.printStackTrace();
	        		jsonArray.add("");
	        		resultJson.put("items", jsonArray);
	                resultJson.put("SEARCH_DATA", "");
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
	}
		
		 private JSONObject getSummaryAlternateBrand(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();

			StrUtils strUtils = new StrUtils();

			try {
				String PLANT = strUtils.fString(request.getParameter("PLANT"));
				String ITEM = strUtils.fString(request.getParameter("ITEM"));
				String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
				String MODEL = strUtils.fString(request.getParameter("MODEL"));
				String ALTRNATEMODEL = strUtils.fString(request.getParameter("ALTRNATEMODEL"));
				String VINNO = strUtils.fString(request.getParameter("VINNO"));
				String ALTERNATEITEMVINNO = strUtils.fString(request.getParameter("ALTERNATEITEMVINNO"));

				Hashtable ht = new Hashtable();
				if (strUtils.fString(ITEM).length() > 0)
					ht.put("A.ITEM", ITEM);

				ArrayList invQryList = new InvUtil().getSummaryAlternateBrand(PLANT, ITEM, PRD_DESCRIP, ht);// "","","","",plant,"")
				if (invQryList.size() > 0) {
					int iIndex = 0;
					int irow = 0;
					double sumprdQty = 0;
					String lastProduct = "";
					for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
						String result = "";
						Map lineArr = (Map) invQryList.get(iCnt);

						String item = (String) lineArr.get("ITEM");

						String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
						JSONObject resultJsonInt = new JSONObject();
	                  
	                    resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
	                    resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
	                    resultJsonInt.put("BRAND", strUtils.fString((String) lineArr.get("BRAND")));
	                    resultJsonInt.put("VINNO", strUtils.fString((String) lineArr.get("VINNO")));
	                    resultJsonInt.put("MODEL", strUtils.fString((String) lineArr.get("MODEL")));
	                    resultJsonInt.put("ALTERNATEITEM", strUtils.fString((String) lineArr.get("ALTERNATEITEM")));
	                    resultJsonInt.put("ALTERNATEITEMDESC", strUtils.fString((String) lineArr.get("ALTERNATEITEMDESC")));
	                    resultJsonInt.put("ALTERNATEBRAND", sqlBean.formatHTML(StrUtils.fString((String) lineArr.get("ALTERNATEBRAND"))));
	                    resultJsonInt.put("ALTERNATEITEMVINNO", sqlBean.formatHTML(StrUtils.fString((String) lineArr.get("ALTERNATEITEMVINNO"))));
	                    resultJsonInt.put("ALTERNATEMODEL", strUtils.fString((String) lineArr.get("ALTERNATEMODEL")));

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
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("SEARCH_DATA", "");
				resultJson.put("TOTAL_QTY", 0);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
		 private JSONObject getInventorysmryGroupByProdMultiUOM(HttpServletRequest request) {
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONArray jsonArrayErr = new JSONArray();

				StrUtils strUtils = new StrUtils();

				try {
					String PLANT = strUtils.fString(request.getParameter("PLANT"));
					String LOC = strUtils.fString(request.getParameter("LOC"));
					String ITEM = strUtils.fString(request.getParameter("ITEM"));
					String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
					String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
					String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
					String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
					// Start code modified by Bruhan for product brand on 11/9/12
					String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
					String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
					String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
					String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
					String MODEL = strUtils.fString(request.getParameter("MODEL"));
					String UOM = strUtils.fString(request.getParameter("UOM"));
					String SORT = strUtils.fString(request.getParameter("SORT"));
					Hashtable ht = new Hashtable();
					if (strUtils.fString(PRD_CLS_ID).length() > 0)
						ht.put("PRDCLSID", PRD_CLS_ID);
					
					if (strUtils.fString(PRD_DEPT_ID).length() > 0)
						ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
					
					if (strUtils.fString(PRD_TYPE_ID).length() > 0)
						ht.put("b.itemtype", PRD_TYPE_ID);
					if (strUtils.fString(PRD_BRAND_ID).length() > 0)
						ht.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
					// if(strUtils.fString(LOC_TYPE_ID).length() > 0)
					// ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
					if (strUtils.fString(MODEL).length() > 0)
						ht.put("b.MODEL", MODEL);
					ArrayList invQryList = new InvUtil().getInvListSummaryGroupByProdMultiUom(PLANT, ITEM, PRD_DESCRIP, ht, LOC,
							LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,UOM,SORT);// "","","","",plant,"")
					if (invQryList.size() > 0) {
						int iIndex = 0;
						int irow = 0;
						double sumprdQty = 0;
						String lastProduct = "";
						for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
							String result = "";
							Map lineArr = (Map) invQryList.get(iCnt);
							sumprdQty = sumprdQty + Double.parseDouble((String) lineArr.get("QTY"));
							String item = (String) lineArr.get("ITEM");
							String loc = (String) lineArr.get("LOC");
							String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
							JSONObject resultJsonInt = new JSONObject();
							
		                    resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
		                    resultJsonInt.put("LOCDESC", strUtils.fString((String) lineArr.get("LOCDESC")));
		                    resultJsonInt.put("LOCTYPE", strUtils.fString((String) lineArr.get("LOC_TYPE_ID")));
		                    resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
		                    resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
		                    resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
		                    resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
		                    resultJsonInt.put("PRDBRANDID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
		                    resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(StrUtils.fString((String) lineArr.get("ITEMDESC"))));
		                    resultJsonInt.put("STKUOM", strUtils.fString((String) lineArr.get("STKUOM")));
		                    resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
		                    resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArr.get("INVENTORYUOM")));
		                    resultJsonInt.put("INVUOMQTY",strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("INVUOMQTY"))), "3"));
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
					jsonArray.add("");
					resultJson.put("items", jsonArray);
					resultJson.put("SEARCH_DATA", "");
					resultJson.put("TOTAL_QTY", 0);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
				}
				return resultJson;
			}
		 private JSONObject getInventorysmryGroupByProdMultiUOMNEW(HttpServletRequest request) {
			 JSONObject resultJson = new JSONObject();
			 JSONArray jsonArray = new JSONArray();
			 JSONArray jsonArrayErr = new JSONArray();
			 
			 StrUtils strUtils = new StrUtils();
			 
			 try {
				 String PLANT = strUtils.fString(request.getParameter("PLANT"));
				 String LOC = strUtils.fString(request.getParameter("LOC"));
				 String ITEM = strUtils.fString(request.getParameter("ITEM"));
				 String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
				 String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				 String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				 String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
				 // Start code modified by Bruhan for product brand on 11/9/12
				 String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				 String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
				 String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
				 String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
				 String MODEL = strUtils.fString(request.getParameter("MODEL"));
				 String UOM = strUtils.fString(request.getParameter("UOM"));
				 String SORT = strUtils.fString(request.getParameter("SORT"));
				 Hashtable ht = new Hashtable();
				 if (strUtils.fString(PRD_CLS_ID).length() > 0)
					 ht.put("PRDCLSID", PRD_CLS_ID);
				 
				 if (strUtils.fString(PRD_DEPT_ID).length() > 0)
					 ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
				 
				 if (strUtils.fString(PRD_TYPE_ID).length() > 0)
					 ht.put("b.itemtype", PRD_TYPE_ID);
				 if (strUtils.fString(PRD_BRAND_ID).length() > 0)
					 ht.put("b.PRD_BRAND_ID", PRD_BRAND_ID);
				 // if(strUtils.fString(LOC_TYPE_ID).length() > 0)
				 // ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
				 if (strUtils.fString(MODEL).length() > 0)
					 ht.put("b.MODEL", MODEL);
				 ArrayList invQryList = new InvUtil().getInvListSummaryGroupByProdMultiUomNew(PLANT, ITEM, PRD_DESCRIP, ht, LOC,
						 LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,UOM,SORT);// "","","","",plant,"")
				 if (invQryList.size() > 0) {
					 int iIndex = 0;
					 int irow = 0;
					 double sumprdQty = 0;
					 String lastProduct = "";
					 for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
						 String result = "";
						 Map lineArr = (Map) invQryList.get(iCnt);
						 sumprdQty = sumprdQty + Double.parseDouble((String) lineArr.get("QTY"));
						 String item = (String) lineArr.get("ITEM");
						 String loc = (String) lineArr.get("LOC");
						 String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
						 JSONObject resultJsonInt = new JSONObject();
						 
						 resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
						 resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
						 resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
						 resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
						 resultJsonInt.put("PRDBRANDID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
						 resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(StrUtils.fString((String) lineArr.get("ITEMDESC"))));
						 resultJsonInt.put("CATALOGPATH", strUtils.fString((String) lineArr.get("CATALOGPATH")));
						 resultJsonInt.put("STKUOM", strUtils.fString((String) lineArr.get("STKUOM")));
						 resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
						 resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArr.get("INVENTORYUOM")));
						 resultJsonInt.put("INVUOMQTY",strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("INVUOMQTY"))), "3"));
						 resultJsonInt.put("UnitPrice", strUtils.fString((String) lineArr.get("UnitPrice")));
						 resultJsonInt.put("COST", strUtils.fString((String) lineArr.get("COST")));
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
				 jsonArray.add("");
				 resultJson.put("items", jsonArray);
				 resultJson.put("SEARCH_DATA", "");
				 resultJson.put("TOTAL_QTY", 0);
				 JSONObject resultJsonInt = new JSONObject();
				 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				 resultJsonInt.put("ERROR_CODE", "98");
				 jsonArrayErr.add(resultJsonInt);
				 resultJson.put("ERROR", jsonArrayErr);
			 }
			 return resultJson;
		 }
		 private JSONObject getInventoryValuationsmry(HttpServletRequest request) {
			 JSONObject resultJson = new JSONObject();
			 JSONArray jsonArray = new JSONArray();
			 JSONArray jsonArrayErr = new JSONArray();
			 
			 StrUtils strUtils = new StrUtils();
			 
			 try {
				 String PLANT = strUtils.fString(request.getParameter("PLANT"));
				 String LOC = strUtils.fString(request.getParameter("LOC"));
				 String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
				 String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				 String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				 String ALLOWPRDNAME = strUtils.fString(request.getParameter("ALLOWPRDNAME"));
				
				 ArrayList invQryList = new InvUtil().getInvListValuationSummary(PLANT,PRD_DEPT_ID,PRD_CLS_ID,PRD_BRAND_ID,LOC,ALLOWPRDNAME);// "","","","",plant,"")
				 if (invQryList.size() > 0) {
					 int iIndex = 0;
					 int irow = 0;
					 double sumprdQty = 0;
					 String lastProduct = "";
					 for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
						 String result = "";
						 Map lineArr = (Map) invQryList.get(iCnt);
						 String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
						 JSONObject resultJsonInt = new JSONObject();
						 
						 resultJsonInt.put("Name", strUtils.fString((String) lineArr.get("NAME")));
						 resultJsonInt.put("Location", strUtils.fString((String) lineArr.get("TOTALQTY")));
						 resultJsonInt.put("Total", strUtils.fString((String) lineArr.get("TOTALQTY")));
						
						 
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
				 jsonArray.add("");
				 resultJson.put("items", jsonArray);
				 resultJson.put("SEARCH_DATA", "");
				 resultJson.put("TOTAL_QTY", 0);
				 JSONObject resultJsonInt = new JSONObject();
				 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				 resultJsonInt.put("ERROR_CODE", "98");
				 jsonArrayErr.add(resultJsonInt);
				 resultJson.put("ERROR", jsonArrayErr);
			 }
			 return resultJson;
		 }
		 private JSONObject getInventorysmryDetailsWoPriceMultiUOM(HttpServletRequest request) {
	            JSONObject resultJson = new JSONObject();
	            JSONArray jsonArray = new JSONArray();
	            JSONArray jsonArrayErr = new JSONArray();
	           
	            StrUtils strUtils = new StrUtils();
	          //  String PLANT="",ITEM="",ITEM_DESC="",PRD_CLS_ID="",PRD_TYPE_ID="",COND="",LOC="",BATCH=""   ;
	            try {
	            
	               String PLANT= strUtils.fString(request.getParameter("PLANT"));
	               String LOC     = strUtils.fString(request.getParameter("LOC"));
	               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
	               String BATCH   = strUtils.fString(request.getParameter("BATCH"));
	               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	               String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	               String  PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
	               String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	               // Start code modified by Bruhan for product brand on 11/9/12 
	               String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	               String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	               String  LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	               String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	               String  MODEL = strUtils.fString(request.getParameter("MODEL"));
	               String  UOM = strUtils.fString(request.getParameter("UOM"));
	               String  TYPE = strUtils.fString(request.getParameter("TYPE"));
	               PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	               String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	               Hashtable ht = new Hashtable();

	                //if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
	                //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
	                if(strUtils.fString(BATCH).length() > 0)               ht.put("BATCH",BATCH);
	                if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
	                if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
	                if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
	                if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
	                //if(strUtils.fString(LOC_TYPE_ID).length() > 0)         ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
	                 if(strUtils.fString(MODEL).length() > 0)        ht.put("b.MODEL",MODEL) ; 
	                ArrayList invQryList= new InvUtil().getInvListSummaryWithOutPriceMultiUom(ht,PLANT,ITEM,PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);//"","","","",plant,"");
	                if(TYPE.equalsIgnoreCase("MANUAL"))
	                	 invQryList= new InvUtil().getInvListManualSummaryWithOutPriceMultiUom(ht,PLANT,ITEM,PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);//"","","","",plant,"");
	                if (invQryList.size() > 0) {
	                int iIndex = 0;
	                 int irow = 0;
	                double sumprdQty = 0;String lastProduct="";
	                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	                          
	                                 
	                                String result="";
	                                Map lineArr = (Map) invQryList.get(iCnt);
	                                sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
	                                String item =(String)lineArr.get("ITEM");
	                                String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                                JSONObject resultJsonInt = new JSONObject();
	                           
	                      //     if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(item))){
	                        	   resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM")));
	                        	   resultJsonInt.put("LOC", strUtils.fString((String)lineArr.get("LOC")));
	                        	   resultJsonInt.put("LOCDESC", strUtils.fString((String)lineArr.get("LOCDESC")));
	                        	   resultJsonInt.put("LOCTYPE", strUtils.fString((String)lineArr.get("LOC_TYPE_ID")));
	                        	   resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
	                        	   resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String)lineArr.get("PRD_DEPT_ID")));
	                        	   resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
	                        	   resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("PRD_BRAND_ID")));
	                        	   resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String)lineArr.get("ITEMDESC"))));
	                        	   resultJsonInt.put("BATCH", strUtils.fString((String)lineArr.get("BATCH")));
	                        	   resultJsonInt.put("STKUOM", strUtils.fString((String)lineArr.get("STKUOM")));	                        	   
	                        	  // resultJsonInt.put("QTY", strUtils.formatNum((String)lineArr.get("QTY")));
									resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY"))); 
									resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArr.get("INVENTORYUOM")));
				                    resultJsonInt.put("INVUOMQTY", strUtils.fString((String) lineArr.get("INVUOMQTY")));
				                    resultJsonInt.put("DETDESC", strUtils.fString((String) lineArr.get("REMARK1")));
				                    if(TYPE.equalsIgnoreCase("MANUAL")) {
					                    resultJsonInt.put("CRDATE", strUtils.fString((String) lineArr.get("CRDATE")));
					                    resultJsonInt.put("STKTAKEQTY", strUtils.fString((String) lineArr.get("STKTAKEQTY")));
					                    resultJsonInt.put("CRBY", strUtils.fString((String) lineArr.get("CRBY")));
					                    resultJsonInt.put("REMARKS", strUtils.fString((String) lineArr.get("REMARKS")));
					                    
					                    double stk = Double.parseDouble((String) lineArr.get("STKTAKEQTY"));
					                    double invqty = Double.parseDouble((String) lineArr.get("QTY"));
					                    double diff = (invqty-stk);
					                    String priceval = strUtils.addZeroes((diff), numberOfDecimal);
					                    
					                    resultJsonInt.put("QTYDIFF", priceval);
				                    }
								  jsonArray.add(resultJsonInt);
	                    }                    
	                                          // End code modified by Bruhan for product brand on 11/9/12 
	                 
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
	            		jsonArray.add("");
	            		resultJson.put("items", jsonArray);
	                    resultJson.put("SEARCH_DATA", "");
	                    JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                    resultJsonInt.put("ERROR_CODE", "98");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("ERROR", jsonArrayErr);
	            }
	            return resultJson;
    }
		 
		 private JSONObject getManualStksmryDetails(HttpServletRequest request) {
	            JSONObject resultJson = new JSONObject();
	            JSONArray jsonArray = new JSONArray();
	            JSONArray jsonArrayErr = new JSONArray();
	           
	            StrUtils strUtils = new StrUtils();
	            try {
	            
	               String PLANT= strUtils.fString(request.getParameter("PLANT"));
	               String LOC     = strUtils.fString(request.getParameter("LOC"));
	               if(LOC.equalsIgnoreCase(""))
	            	   LOC     = strUtils.fString(request.getParameter("LOCS"));
	               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
	               String BATCH   = strUtils.fString(request.getParameter("BATCH"));
	               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	               String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	               String  PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
	               String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	               String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	               String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	               String  LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	               String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	               String  MODEL = strUtils.fString(request.getParameter("MODEL"));
	               String  UOM = strUtils.fString(request.getParameter("UOM"));
	               String  TYPE = strUtils.fString(request.getParameter("TYPE"));
	               String  RADIOTYPE = strUtils.fString(request.getParameter("RADIO"));
	               PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	               String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	               Hashtable ht = new Hashtable();

	                if(strUtils.fString(BATCH).length() > 0)               ht.put("BATCH",BATCH);
	                if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
	                if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
	                if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
	                if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
	                 if(strUtils.fString(MODEL).length() > 0)        ht.put("b.MODEL",MODEL) ; 
	                	ArrayList invQryList= new StockTakeUtil().getInvListManualSummaryWithOutPriceMultiUom(ht,PLANT,ITEM,PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);//"","","","",plant,"");
	                	//ArrayList invQryList= new InvUtil().getInvListManualSummaryWithOutPriceMultiUom(ht,PLANT,ITEM,PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);//"","","","",plant,"");
	                if (invQryList.size() > 0) {
	                int iIndex = 0;
	                 int irow = 0;
	                double sumprdQty = 0;String lastProduct="";
	                int count = 0;
	                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	                          
	                                String result="";
	                                Map lineArr = (Map) invQryList.get(iCnt);
	                                sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
	                                String item =(String)lineArr.get("ITEM");
	                                String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                                JSONObject resultJsonInt = new JSONObject();
	                                String stkqty = strUtils.fString((String) lineArr.get("STKTAKEQTY"));
	                                String diffqty = strUtils.fString((String) lineArr.get("DIFFQTY"));
	                                String INVFLAG = strUtils.fString((String) lineArr.get("INVFLAG"));
	                                String CRDATE = strUtils.fString((String) lineArr.get("CRDATE"));
	                                double invqty = Double.parseDouble((String) lineArr.get("QTY"));
	                                double stk = Double.parseDouble(stkqty);	                                
	                                double olddiffqty = Double.parseDouble(diffqty);	                                
	                                double diff = 0;
	                                
	                                if(!CRDATE.equalsIgnoreCase("")){
	                                if(INVFLAG.equalsIgnoreCase("1")){
	                                  	if(stk != invqty){
	                                  		stk = stk-(stk-invqty);
	                                  	}
	                                  	
	                                  }else{
	                                  	if((stk+olddiffqty) != invqty){
	                                  		stk=stk-((stk+olddiffqty)-invqty);
	                                  	}
	                                  }
	                                 diff = (invqty - stk);
	                                }
	                                        
	                                	/*if(RADIOTYPE.equalsIgnoreCase("1")) {
											resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
											resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
											resultJsonInt.put("PRDCLSID",strUtils.fString((String) lineArr.get("PRDCLSID")));
											resultJsonInt.put("PRD_DEPT_ID",strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
											resultJsonInt.put("ITEMTYPE",strUtils.fString((String) lineArr.get("ITEMTYPE")));
											resultJsonInt.put("PRD_BRAND_ID",strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
											resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String) lineArr.get("ITEMDESC"))));
											resultJsonInt.put("BATCH", strUtils.fString((String) lineArr.get("BATCH")));
											resultJsonInt.put("STKUOM",strUtils.fString((String) lineArr.get("STKUOM")));
											resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
											resultJsonInt.put("INVENTORYUOM",strUtils.fString((String) lineArr.get("INVENTORYUOM")));
											resultJsonInt.put("INVUOMQTY",strUtils.fString((String) lineArr.get("INVUOMQTY")));
											resultJsonInt.put("DETDESC",strUtils.fString((String) lineArr.get("REMARK1")));
											resultJsonInt.put("CRDATE",strUtils.fString((String) lineArr.get("CRDATE")));
											//resultJsonInt.put("STKTAKEQTY",strUtils.fString((String) lineArr.get("STKTAKEQTY")));
											resultJsonInt.put("CRBY", strUtils.fString((String) lineArr.get("CRBY")));
											resultJsonInt.put("REMARKS",strUtils.fString((String) lineArr.get("REMARKS")));
											//double stk = Double.parseDouble((String) lineArr.get("STKTAKEQTY"));
											//double invqty = Double.parseDouble((String) lineArr.get("QTY"));
											//double diff = (invqty - stk);
											String priceval = strUtils.addZeroes((diff), "3");
											resultJsonInt.put("STKTAKEQTY",strUtils.addZeroes((stk), "3"));
											resultJsonInt.put("QTYDIFF", priceval);
											jsonArray.add(resultJsonInt);
	                                	}else if(RADIOTYPE.equalsIgnoreCase("2") && stk!=0) {
											resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
											resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
											resultJsonInt.put("PRDCLSID",strUtils.fString((String) lineArr.get("PRDCLSID")));
											resultJsonInt.put("PRD_DEPT_ID",strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
											resultJsonInt.put("ITEMTYPE",strUtils.fString((String) lineArr.get("ITEMTYPE")));
											resultJsonInt.put("PRD_BRAND_ID",strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
											resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String) lineArr.get("ITEMDESC"))));
											resultJsonInt.put("BATCH", strUtils.fString((String) lineArr.get("BATCH")));
											resultJsonInt.put("STKUOM",strUtils.fString((String) lineArr.get("STKUOM")));
											resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
											resultJsonInt.put("INVENTORYUOM",strUtils.fString((String) lineArr.get("INVENTORYUOM")));
											resultJsonInt.put("INVUOMQTY",strUtils.fString((String) lineArr.get("INVUOMQTY")));
											resultJsonInt.put("DETDESC",strUtils.fString((String) lineArr.get("REMARK1")));
											resultJsonInt.put("CRDATE",strUtils.fString((String) lineArr.get("CRDATE")));
											//resultJsonInt.put("STKTAKEQTY",strUtils.fString((String) lineArr.get("STKTAKEQTY")));
											resultJsonInt.put("CRBY", strUtils.fString((String) lineArr.get("CRBY")));
											resultJsonInt.put("REMARKS",strUtils.fString((String) lineArr.get("REMARKS")));
											//double stk = Double.parseDouble((String) lineArr.get("STKTAKEQTY"));
											//double invqty = Double.parseDouble((String) lineArr.get("QTY"));
											//double diff = (invqty - stk);
											String priceval = strUtils.addZeroes((diff), "3");
											count = 1;
											resultJsonInt.put("STKTAKEQTY",strUtils.addZeroes((stk), "3"));
											resultJsonInt.put("QTYDIFF", priceval);
											jsonArray.add(resultJsonInt);
	                                	}else if(RADIOTYPE.equalsIgnoreCase("3") && stk==0) {
											resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
											resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
											resultJsonInt.put("PRDCLSID",strUtils.fString((String) lineArr.get("PRDCLSID")));
											resultJsonInt.put("PRD_DEPT_ID",strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
											resultJsonInt.put("ITEMTYPE",strUtils.fString((String) lineArr.get("ITEMTYPE")));
											resultJsonInt.put("PRD_BRAND_ID",strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
											resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String) lineArr.get("ITEMDESC"))));
											resultJsonInt.put("BATCH", strUtils.fString((String) lineArr.get("BATCH")));
											resultJsonInt.put("STKUOM",strUtils.fString((String) lineArr.get("STKUOM")));
											resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
											resultJsonInt.put("INVENTORYUOM",strUtils.fString((String) lineArr.get("INVENTORYUOM")));
											resultJsonInt.put("INVUOMQTY",strUtils.fString((String) lineArr.get("INVUOMQTY")));
											resultJsonInt.put("DETDESC",strUtils.fString((String) lineArr.get("REMARK1")));
											resultJsonInt.put("CRDATE",strUtils.fString((String) lineArr.get("CRDATE")));
											//resultJsonInt.put("STKTAKEQTY",strUtils.fString((String) lineArr.get("STKTAKEQTY")));
											resultJsonInt.put("CRBY", strUtils.fString((String) lineArr.get("CRBY")));
											resultJsonInt.put("REMARKS",strUtils.fString((String) lineArr.get("REMARKS")));
											//double stk = Double.parseDouble((String) lineArr.get("STKTAKEQTY"));
											//double invqty = Double.parseDouble((String) lineArr.get("QTY"));
											//double diff = (invqty - stk);
											String priceval = strUtils.addZeroes((diff), "3");
											resultJsonInt.put("STKTAKEQTY",strUtils.addZeroes((stk), "3"));
											resultJsonInt.put("QTYDIFF", priceval);
											jsonArray.add(resultJsonInt);
	                                	}else if(RADIOTYPE.equalsIgnoreCase("2") && stk==0 &&count==0) {
	                                		//jsonArray.add("");
	                                	}*/                    
	                 
	                	
	                	resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
						resultJsonInt.put("LOC", strUtils.fString((String) lineArr.get("LOC")));
						resultJsonInt.put("PRDCLSID",strUtils.fString((String) lineArr.get("PRDCLSID")));
						resultJsonInt.put("PRD_DEPT_ID",strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
						resultJsonInt.put("ITEMTYPE",strUtils.fString((String) lineArr.get("ITEMTYPE")));
						resultJsonInt.put("PRD_BRAND_ID",strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
						resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String) lineArr.get("ITEMDESC"))));
						resultJsonInt.put("BATCH", strUtils.fString((String) lineArr.get("BATCH")));
						resultJsonInt.put("STKUOM",strUtils.fString((String) lineArr.get("STKUOM")));
						resultJsonInt.put("QTY", strUtils.fString((String) lineArr.get("QTY")));
						resultJsonInt.put("INVENTORYUOM",strUtils.fString((String) lineArr.get("INVENTORYUOM")));
						resultJsonInt.put("INVUOMQTY",strUtils.fString((String) lineArr.get("INVUOMQTY")));
						resultJsonInt.put("DETDESC",strUtils.fString((String) lineArr.get("REMARK1")));
						resultJsonInt.put("CRDATE",strUtils.fString((String) lineArr.get("CRDATE")));
						//resultJsonInt.put("STKTAKEQTY",strUtils.fString((String) lineArr.get("STKTAKEQTY")));
						resultJsonInt.put("CRBY", strUtils.fString((String) lineArr.get("CRBY")));
						resultJsonInt.put("REMARKS",strUtils.fString((String) lineArr.get("REMARKS")));
						resultJsonInt.put("STKID",strUtils.fString((String) lineArr.get("ID")));
						//double stk = Double.parseDouble((String) lineArr.get("STKTAKEQTY"));
						//double invqty = Double.parseDouble((String) lineArr.get("QTY"));
						//double diff = (invqty - stk);
						String priceval = strUtils.addZeroes((diff), "3");
						resultJsonInt.put("STKTAKEQTY",strUtils.addZeroes((stk), "3"));
						resultJsonInt.put("QTYDIFF", priceval);
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
	            		jsonArray.add("");
	            		resultJson.put("items", jsonArray);
	                    resultJson.put("SEARCH_DATA", "");
	                    JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                    resultJsonInt.put("ERROR_CODE", "98");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("ERROR", jsonArrayErr);
	            }
	            return resultJson;
 }
		 
	 private JSONObject getLocationListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	StrUtils strUtils = new StrUtils();
        	LocMstDAO  _LocMstDAO  = new LocMstDAO();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String loc = StrUtils.fString(request.getParameter("LOC"));
        	ArrayList listQry = _LocMstDAO.getLocByWMS(plant,"",loc);  
        	if (listQry.size() > 0) {    		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("LOC", StrUtils.fString((String)m.get("loc")));
                    resultJsonInt.put("LOCDESC", StrUtils.fString((String)m.get("locdesc")));
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("locations", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO LOCATION RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
//                resultJson.put("locations", jsonArray);
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
	 
	 private JSONObject getInvBatchListForSuggestion(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        try {
	        	StrUtils strUtils = new StrUtils();
	        	List listQry =new ArrayList();
	        	InvMstDAO  _InvMstDAO  = new InvMstDAO();  
	        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
	        	String batch = StrUtils.fString(request.getParameter("BATCH"));
	        	listQry =  _InvMstDAO.getBatchByWMS(plant,batch);
	        	if (listQry.size() > 0) {	
	        		for(int i =0; i<listQry.size(); i++) {
	        			Map m=(Map)listQry.get(i);
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("BATCH", StrUtils.fString((String)m.get("batch")));
	                    resultJsonInt.put("QTY", StrUtils.fString((String)m.get("qty")));
	                    resultJsonInt.put("UOM", StrUtils.fString((String)m.get("uom")));
	                    jsonArray.add(resultJsonInt);
	        		}
	        		resultJson.put("batchList", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	        	} else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO LOCATION RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
//	                resultJson.put("batchList", jsonArray);
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
	 
	 private JSONObject addToEstimateList(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        try {

	        	String[] product = request.getParameterValues("PRODUCTID[]");
	        	String[] productdesc = request.getParameterValues("PRODUCTDESC[]");
	        	String[] catalog = request.getParameterValues("CATALOG[]");
	        	String[] brand = request.getParameterValues("BRAND[]");
	        	String[] model = request.getParameterValues("MODEL[]");
	        	String[] location = request.getParameterValues("LOCATION[]");
	        	String[] unitprice = request.getParameterValues("UNITPRICE[]");
	        	String[] invuom = request.getParameterValues("INVUOM[]");
	        	String[] invqty = request.getParameterValues("INVQTY[]");
	        	String[] avlqty = request.getParameterValues("AVLQTY[]");
	        	String[] orduom = request.getParameterValues("ORDUOM[]");
	        	String[] nonStkFlag = request.getParameterValues("NONSTKFLAG[]");
	        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        	
	        	List listQry = new ArrayList();
	        	if(request.getSession().getAttribute("EST_LIST") != null){
	        		listQry = (List) request.getSession().getAttribute("EST_LIST");
        		}
	        	if(product != null) {
		        	for(int i =0; i<product.length; i++) {
		        		Map m = new HashMap();
		        		m.put("PRODUCT", product[i]);
		        		m.put("PRODUCTDESC", productdesc[i]);
		        		m.put("CATALOG", catalog[i]);
		        		m.put("BRAND", brand[i]);
		        		m.put("MODEL", model[i]);
		        		m.put("LOCATION", location[i]);
		        		m.put("UNITPRICE", unitprice[i]);
		        		m.put("INVUOM", invuom[i]);
		        		m.put("INVQTY", invqty[i]);
		        		m.put("AVLQTY", avlqty[i]);
		        		m.put("ORDUOM", orduom[i]);
		        		m.put("NONSTKFLAG", nonStkFlag[i]);
		        		listQry.add(m);
		        	}
	        	}
	        	if (listQry.size() > 0) {
	        		for(int i =0; i<listQry.size(); i++) {
	        			Map m=(Map)listQry.get(i);
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("PRODUCT", StrUtils.fString((String)m.get("PRODUCT")));
	        			resultJsonInt.put("PRODUCTDESC", StrUtils.fString((String)m.get("PRODUCTDESC")));
	        			resultJsonInt.put("CATALOG", StrUtils.fString((String)m.get("CATALOG")));
	        			resultJsonInt.put("BRAND", StrUtils.fString((String)m.get("BRAND")));
	        			resultJsonInt.put("MODEL", StrUtils.fString((String)m.get("MODEL")));
	        			resultJsonInt.put("LOCATION", StrUtils.fString((String)m.get("LOCATION")));
	        			resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
	        			resultJsonInt.put("INVUOM", StrUtils.fString((String)m.get("INVUOM")));
	        			resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
	        			resultJsonInt.put("AVLQTY", StrUtils.fString((String)m.get("AVLQTY")));
	        			resultJsonInt.put("ORDUOM", StrUtils.fString((String)m.get("ORDUOM")));
	        			resultJsonInt.put("NONSTKFLAG", StrUtils.fString((String)m.get("NONSTKFLAG")));
	                    jsonArray.add(resultJsonInt);
	        		}
	        		resultJson.put("estList", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	                
	                request.getSession().setAttribute("EST_LIST", listQry);
	                
	                UomDAO uomDAO = new UomDAO();
		            listQry = uomDAO.getUomDetails("", plant, "");
		            if (listQry.size() > 0) {
		            	jsonArray = new JSONArray();
		            	for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   resultJsonInt = new JSONObject();
		                   resultJsonInt.put("UOM", (String)arrCustLine.get("uom"));
		                   jsonArray.add(resultJsonInt);
		            	}
		            }
		            resultJson.put("uomList", jsonArray);
	        	} else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO LOCATION RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("batchList", jsonArray);
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
	 
	 private JSONObject deleteEstimateList(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        try {
	        	String id = StrUtils.fString(request.getParameter("ID"));
	        	int index = (id.equalsIgnoreCase("") ? 0 : Integer.parseInt(id));
	        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        	
	        	List listQry = new ArrayList();
	        	if(request.getSession().getAttribute("EST_LIST") != null){
	        		listQry = (List) request.getSession().getAttribute("EST_LIST");
	        		listQry.remove(index);
	        	}
	        	if (listQry.size() > 0) {
	        		for(int i =0; i<listQry.size(); i++) {
	        			Map m=(Map)listQry.get(i);
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("PRODUCT", StrUtils.fString((String)m.get("PRODUCT")));
	        			resultJsonInt.put("PRODUCTDESC", StrUtils.fString((String)m.get("PRODUCTDESC")));
	        			resultJsonInt.put("CATALOG", StrUtils.fString((String)m.get("CATALOG")));
	        			resultJsonInt.put("BRAND", StrUtils.fString((String)m.get("BRAND")));
	        			resultJsonInt.put("MODEL", StrUtils.fString((String)m.get("MODEL")));
	        			resultJsonInt.put("LOCATION", StrUtils.fString((String)m.get("LOCATION")));
	        			resultJsonInt.put("UNITPRICE", StrUtils.fString((String)m.get("UNITPRICE")));
	        			resultJsonInt.put("INVUOM", StrUtils.fString((String)m.get("INVUOM")));
	        			resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
	        			resultJsonInt.put("AVLQTY", StrUtils.fString((String)m.get("AVLQTY")));
	        			resultJsonInt.put("ORDUOM", StrUtils.fString((String)m.get("ORDUOM")));
	        			resultJsonInt.put("NONSTKFLAG", StrUtils.fString((String)m.get("NONSTKFLAG")));
	                    jsonArray.add(resultJsonInt);
	        		}
	        		resultJson.put("estList", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	                
	                request.getSession().setAttribute("EST_LIST", listQry);
	                
	                UomDAO uomDAO = new UomDAO();
		            listQry = uomDAO.getUomDetails("", plant, "");
		            if (listQry.size() > 0) {
		            	jsonArray = new JSONArray();
		            	for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   resultJsonInt = new JSONObject();
		                   resultJsonInt.put("UOM", (String)arrCustLine.get("uom"));
		                   jsonArray.add(resultJsonInt);
		            	}
		            }
		            resultJson.put("uomList", jsonArray);
	        	} else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO LOCATION RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("batchList", jsonArray);
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
	 
	 private JSONObject getAvailableQty(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        try {
	        	String item = StrUtils.fString(request.getParameter("ITEM"));
	        	String loc = StrUtils.fString(request.getParameter("LOC"));
	        	String uom = StrUtils.fString(request.getParameter("UOM"));
	        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        	
	        	Map invMap = new InvUtil().getAvailableQty(item, uom, loc, plant);
	        	resultJson.put("status", "100");
                JSONObject resultObjectJson = new JSONObject();
                resultObjectJson.put("AVAILABLEQTY",(String)invMap.get("AVAILABLEQTY"));
                resultJson.put("result", resultObjectJson);
	        } catch (Exception e) {
	        	resultJson.put("status", "99");
	        }
		    return resultJson;
	}
	 
	 private JSONObject getInvListSummaryWithAvlQtyMultiUom(HttpServletRequest request) {
         JSONObject resultJson = new JSONObject();
         JSONArray jsonArray = new JSONArray();
         JSONArray jsonArrayErr = new JSONArray();
        
         StrUtils strUtils = new StrUtils();
         try {
         
            String PLANT= strUtils.fString(request.getParameter("PLANT"));
            String ITEM    = strUtils.fString(request.getParameter("ITEM"));
            String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
            String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
            String  PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
            String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
            String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
            String  UOM = strUtils.fString(request.getParameter("UOM"));
            Hashtable ht = new Hashtable();

             if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
             if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("ITEMTYPE",PRD_TYPE_ID) ;  
             if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID) ; 
             if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("PRD_DEPT_ID",PRD_DEPT_ID) ; 
             ArrayList invQryList= new InvUtil().getInvListSummaryWithAvlQtyMultiUom(ht,PLANT,ITEM,PRD_DESCRIP,UOM);
             if (invQryList.size() > 0) {
             int iIndex = 0;
              int irow = 0;
             double sumprdQty = 0;String lastProduct="";
                 for (int iCnt =0; iCnt<invQryList.size(); iCnt++){                       
                              
	                 String result="";
	                 Map lineArr = (Map) invQryList.get(iCnt);
	                 sumprdQty = sumprdQty + Double.parseDouble((String)lineArr.get("QTY"));
	                 String item =(String)lineArr.get("ITEM");
	                 String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                 JSONObject resultJsonInt = new JSONObject();
                        
					   resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("ITEM")));
					   resultJsonInt.put("ITEMDESC", sqlBean.formatHTML(strUtils.fString((String)lineArr.get("ITEMDESC"))));
					   resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("PRDCLSID")));
					   resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("PRD_BRAND_ID")));
					   resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("ITEMTYPE")));
					   resultJsonInt.put("STKUOM", strUtils.fString((String)lineArr.get("STKUOM")));
					   resultJsonInt.put("INVENTORYUOM", strUtils.fString((String)lineArr.get("INVENTORYUOM")));
					   resultJsonInt.put("QTY", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("QTY"))), "3") ); 
					   resultJsonInt.put("ESTIMATEQTY", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("ESTIMATEQTY"))), "3"));
					   resultJsonInt.put("OUTBOUNDQTY", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("OUTBOUNDQTY"))), "3"));
					   resultJsonInt.put("AVAILABLEQTY", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("AVAILABLEQTY"))), "3"));
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
         		jsonArray.add("");
         		resultJson.put("items", jsonArray);
                 resultJson.put("SEARCH_DATA", "");
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                 resultJsonInt.put("ERROR_CODE", "98");
                 jsonArrayErr.add(resultJsonInt);
                 resultJson.put("ERROR", jsonArrayErr);
         }
         return resultJson;
	 }
	 
	 private JSONObject getInventorysmryincomingoutgoingqty(HttpServletRequest request, String baseCurrency) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();

			StrUtils strUtils = new StrUtils();
			try {
				String fdate = "", tdate = "", tndate = "";
				String PLANT = strUtils.fString(request.getParameter("PLANT"));
				String USERID = strUtils.fString(request.getParameter("LOGIN_USER"));
				String ITEM = strUtils.fString(request.getParameter("ITEM"));
				String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
				String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
				String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
				
				String dt = TO_DATE;  // Start date
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(dt));
				c.add(Calendar.DATE, 1);  // number of days to add
				dt = sdf.format(c.getTime());  // dt is now the new date

				if (FROM_DATE.length() > 5)
					fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) + FROM_DATE.substring(0, 2);

				if (TO_DATE.length() > 5)
					tdate = TO_DATE.substring(6) + TO_DATE.substring(3, 5) + TO_DATE.substring(0, 2);
				
				if (dt.length() > 5)
					tndate = dt.substring(6) + dt.substring(3, 5) + dt.substring(0, 2);

				PlantMstDAO plantMstDAO = new PlantMstDAO();
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
				ArrayList invQryList = new ArrayList();
				ReportSesBeanDAO reportdoa = new ReportSesBeanDAO();
				invQryList = reportdoa.IncomingoutgoingqtyReport(PLANT, ITEM, PRD_DEPT_ID, PRD_BRAND_ID, PRD_CLS_ID, PRD_TYPE_ID, "",FROM_DATE,TO_DATE,tndate);
				List<OpenCloseReportPojo> OpenCloseReportList =  new ArrayList<OpenCloseReportPojo>();
				
				if (invQryList.size() > 0) {
  
					int iIndex = 0;
					int irow = 0;
					double sumprdQty = 0, sumOfTotalCost = 0, sumOfTotalPrice = 0, billQty = 0, unbillQty = 0,
							invQty = 0;
					String lastProduct = "";
					for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
						String result = "";
						Map lineArr = (Map) invQryList.get(iCnt);

						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("PLANT", PLANT);
						resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
						resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
						resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
						resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
						resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
						resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
						resultJsonInt.put("TOTALRECEIVEDQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALRECEIVEDQTY")), "3"));
			            resultJsonInt.put("TOTALISSUEDQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALISSUEDQTY")), "3"));
			            resultJsonInt.put("STOCKONHAND",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("STOCKONHAND")), "3"));
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
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}

	 private JSONObject getInventorysmryopenclose(HttpServletRequest request, String baseCurrency) {
		 JSONObject resultJson = new JSONObject();
		 JSONArray jsonArray = new JSONArray();
		 JSONArray jsonArrayErr = new JSONArray();
		 
		 StrUtils strUtils = new StrUtils();
		 try {
			 String fdate = "", tdate = "", tndate = "";
			 String PLANT = strUtils.fString(request.getParameter("PLANT"));
			 String USERID = strUtils.fString(request.getParameter("LOGIN_USER"));
			 String ITEM = strUtils.fString(request.getParameter("ITEM"));
			 String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
			 String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
			 String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
			 String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
			 String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
			 String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
			 
			 String dt = TO_DATE;  // Start date
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			 Calendar c = Calendar.getInstance();
			 c.setTime(sdf.parse(dt));
			 c.add(Calendar.DATE, 1);  // number of days to add
			 dt = sdf.format(c.getTime());  // dt is now the new date
			 
			 if (FROM_DATE.length() > 5)
				 fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) + FROM_DATE.substring(0, 2);
			 
			 if (TO_DATE.length() > 5)
				 tdate = TO_DATE.substring(6) + TO_DATE.substring(3, 5) + TO_DATE.substring(0, 2);
			 
			 if (dt.length() > 5)
				 tndate = dt.substring(6) + dt.substring(3, 5) + dt.substring(0, 2);
			 
			 PlantMstDAO plantMstDAO = new PlantMstDAO();
			 String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			 ArrayList invQryList = new ArrayList();
			 ReportSesBeanDAO reportdoa = new ReportSesBeanDAO();
			 invQryList = reportdoa.OpenAnclosingReport(PLANT, ITEM, PRD_DEPT_ID, PRD_BRAND_ID, PRD_CLS_ID, PRD_TYPE_ID, "",FROM_DATE,TO_DATE,tndate);
			 List<OpenCloseReportPojo> OpenCloseReportList =  new ArrayList<OpenCloseReportPojo>();
			 
			 if (invQryList.size() > 0) {
				 
				 int iIndex = 0;
				 int irow = 0;
				 double sumprdQty = 0, sumOfTotalCost = 0, sumOfTotalPrice = 0, billQty = 0, unbillQty = 0,
						 invQty = 0;
				 String lastProduct = "";
				 for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
					 String result = "";
					 Map lineArr = (Map) invQryList.get(iCnt);
					 
					 JSONObject resultJsonInt = new JSONObject();
					 resultJsonInt.put("PLANT", PLANT);
					 resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
					 resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
					 resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
					 resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
					 resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
					 resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
					 resultJsonInt.put("TOTALRECEIVEDQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALRECEIVEDQTY")), "3"));
					 resultJsonInt.put("TOTALISSUEDQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALISSUEDQTY")), "3"));
					 resultJsonInt.put("OPENINGSTOCKQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("OPENINGSTOCKQTY")), "3"));
					 resultJsonInt.put("CLOSINGSTOCKQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("CLOSINGSTOCKQTY")), "3"));
					 resultJsonInt.put("LASTRECEIVEDQTY",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("LASTRECEIVEDQTY")), "3"));
					 resultJsonInt.put("LASTISSUEDQTY",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("LASTISSUEDQTY")), "3"));
					 resultJsonInt.put("STOCKONHAND",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("STOCKONHAND")), "3"));
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
			 jsonArray.add("");
			 resultJson.put("items", jsonArray);
			 resultJson.put("SEARCH_DATA", "");
			 JSONObject resultJsonInt = new JSONObject();
			 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			 resultJsonInt.put("ERROR_CODE", "98");
			 jsonArrayErr.add(resultJsonInt);
			 resultJson.put("ERROR", jsonArrayErr);
		 }
		 return resultJson;
	 }
	 
	 private JSONObject getInventorysmryopencloseavgcost(HttpServletRequest request, String baseCurrency) {
		 JSONObject resultJson = new JSONObject();
		 JSONArray jsonArray = new JSONArray();
		 JSONArray jsonArrayErr = new JSONArray();
		 
		 StrUtils strUtils = new StrUtils();
		 try {
			 String fdate = "", tdate = "", tndate = "";
			 String PLANT = strUtils.fString(request.getParameter("PLANT"));
			 String USERID = strUtils.fString(request.getParameter("LOGIN_USER"));
			 String ITEM = strUtils.fString(request.getParameter("ITEM"));
			 String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
			 String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
			 String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
			 String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
			 String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
			 String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
			 
			 String dt = TO_DATE;  // Start date
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			 Calendar c = Calendar.getInstance();
			 c.setTime(sdf.parse(dt));
			 c.add(Calendar.DATE, 1);  // number of days to add
			 dt = sdf.format(c.getTime());  // dt is now the new date
			 
			 if (FROM_DATE.length() > 5)
				 fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) + FROM_DATE.substring(0, 2);
			 
			 if (TO_DATE.length() > 5)
				 tdate = TO_DATE.substring(6) + TO_DATE.substring(3, 5) + TO_DATE.substring(0, 2);
			 
			 if (dt.length() > 5)
				 tndate = dt.substring(6) + dt.substring(3, 5) + dt.substring(0, 2);
			 
			 PlantMstDAO plantMstDAO = new PlantMstDAO();
			 String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			 ArrayList invQryList = new ArrayList();
			 ReportSesBeanDAO reportdoa = new ReportSesBeanDAO();
			 invQryList = reportdoa.OpenAnclosingAvgCostReport(PLANT, ITEM, PRD_DEPT_ID, PRD_BRAND_ID, PRD_CLS_ID, PRD_TYPE_ID, "",FROM_DATE,TO_DATE,tndate);
			 List<OpenCloseReportPojo> OpenCloseReportList =  new ArrayList<OpenCloseReportPojo>();
			 
			 if (invQryList.size() > 0) {
				 
				 int iIndex = 0;
				 int irow = 0;
				 double sumprdQty = 0, sumOfTotalCost = 0, sumOfTotalPrice = 0, billQty = 0, unbillQty = 0,
						 invQty = 0;
				 String lastProduct = "";
				 for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
					 String result = "";
					 Map lineArr = (Map) invQryList.get(iCnt);
					 
					 JSONObject resultJsonInt = new JSONObject();
					 resultJsonInt.put("PLANT", PLANT);
					 resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
					 resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
					 resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
					 resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
					 resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
					 resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
					 resultJsonInt.put("TOTALRECEIVEDQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALRECEIVEDQTY")), "3"));
					 resultJsonInt.put("TOTALISSUEDQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALISSUEDQTY")), "3"));
					 resultJsonInt.put("OPENINGSTOCKQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("OPENINGSTOCKQTY")), "3"));
					 resultJsonInt.put("CLOSINGSTOCKQTY", StrUtils.addZeroes(Double.valueOf((String) lineArr.get("CLOSINGSTOCKQTY")), "3"));
					 resultJsonInt.put("AVERAGECOST",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("AVERAGECOST")), numberOfDecimal));
					 resultJsonInt.put("PRICE",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("PRICE")), numberOfDecimal));
					 resultJsonInt.put("TOTALCOST",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALCOST")), numberOfDecimal));
					 resultJsonInt.put("TOTALPRICE",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("TOTALPRICE")), numberOfDecimal));
					 resultJsonInt.put("LASTRECEIVEDQTY",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("LASTRECEIVEDQTY")), "3"));
					 resultJsonInt.put("LASTISSUEDQTY",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("LASTISSUEDQTY")), "3"));
					 resultJsonInt.put("STOCKONHAND",StrUtils.addZeroes(Double.valueOf((String) lineArr.get("STOCKONHAND")), "3"));
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
			 jsonArray.add("");
			 resultJson.put("items", jsonArray);
			 resultJson.put("SEARCH_DATA", "");
			 JSONObject resultJsonInt = new JSONObject();
			 resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			 resultJsonInt.put("ERROR_CODE", "98");
			 jsonArrayErr.add(resultJsonInt);
			 resultJson.put("ERROR", jsonArrayErr);
		 }
		 return resultJson;
	 }
	 
		private JSONObject getInventorysmryopenclosebak(HttpServletRequest request, String baseCurrency) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();

			StrUtils strUtils = new StrUtils();
			try {
				String fdate = "", tdate = "", tndate = "";
				String PLANT = strUtils.fString(request.getParameter("PLANT"));
				String USERID = strUtils.fString(request.getParameter("LOGIN_USER"));
				String ITEM = strUtils.fString(request.getParameter("ITEM"));
				String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
				String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
				String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
				
				String dt = TO_DATE;  // Start date
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(dt));
				c.add(Calendar.DATE, 1);  // number of days to add
				dt = sdf.format(c.getTime());  // dt is now the new date

				if (FROM_DATE.length() > 5)
					fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) + FROM_DATE.substring(0, 2);

				if (TO_DATE.length() > 5)
					tdate = TO_DATE.substring(6) + TO_DATE.substring(3, 5) + TO_DATE.substring(0, 2);
				
				if (dt.length() > 5)
					tndate = dt.substring(6) + dt.substring(3, 5) + dt.substring(0, 2);

				PlantMstDAO plantMstDAO = new PlantMstDAO();
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
				
				ArrayList invQryList = new ArrayList();
				ReportSesBeanDAO reportdoa = new ReportSesBeanDAO();
				invQryList = new ItemMstDAO().getallitem(PLANT, ITEM, PRD_DEPT_ID, PRD_BRAND_ID, PRD_CLS_ID, PRD_TYPE_ID, "");
				
		        /*try {
		        	List<ItemQtyBigDecimalPojo> receivedQtyList = reportdoa.findByItemQtyR(PLANT,FROM_DATE,TO_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}
		        
		        try {
		        	 List<ItemQtyBigDecimalPojo> issuedQtyList = reportdoa.findByItemQtyS(PLANT,FROM_DATE,TO_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}
		        
		        try {
		        	List<ItemQtyPojo> receivedQtyFromdateList = reportdoa.findByItemQtyFromDateR(PLANT,TO_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}
		        try {
		        	List<ItemQtyPojo> receivedQtyToDateList = reportdoa.findByItemQtyToDateR(PLANT,FROM_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}
		        try {
		        	List<ItemQtyPojo> issuedQtyFromDateList = reportdoa.findByItemQtyFromDateS(PLANT,TO_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}
		        try {
		        	List<ItemQtyBigDecimalPojo> issuedQtyToDateList = reportdoa.findByItemQtyToDateS(PLANT,FROM_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}
		        try {
		        	 List<ItemQtyPojo> receivedQtyToDateList2 = reportdoa.findByItemQtyToDateR(PLANT,TO_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}
		        try {
		        	 List<ItemQtyBigDecimalPojo> issuedQtyToDateList2 = reportdoa.findByItemQtyToDateS(PLANT,TO_DATE);
				} catch (Exception e) {
					// TODO: handle exception
				}*/
				
		        List<ItemQtyBigDecimalPojo> receivedQtyList = reportdoa.findByItemQtyR(PLANT,FROM_DATE,TO_DATE);
		        List<ItemQtyBigDecimalPojo> issuedQtyList = reportdoa.findByItemQtyS(PLANT,FROM_DATE,TO_DATE);
		        List<ItemQtyBigDecimalPojo> receivedQtyFromdateList = reportdoa.findByItemQtyFromDateR(PLANT,TO_DATE);
		        List<ItemQtyBigDecimalPojo> receivedQtyToDateList = reportdoa.findByItemQtyToDateR(PLANT,FROM_DATE);
		        List<ItemQtyBigDecimalPojo> issuedQtyFromDateList = reportdoa.findByItemQtyFromDateS(PLANT,TO_DATE);
		        List<ItemQtyBigDecimalPojo> issuedQtyToDateList = reportdoa.findByItemQtyToDateS(PLANT,FROM_DATE);
				/*
				 * List<ItemQtyBigDecimalPojo> receivedQtyToDateList2 =
				 * reportdoa.findByItemQtyToDateR(PLANT,TO_DATE); List<ItemQtyBigDecimalPojo>
				 * issuedQtyToDateList2 = reportdoa.findByItemQtyToDateS(PLANT,TO_DATE);
				 */
		        
		        List<ItemQtyBigDecimalPojo> receivedQtyToDateList2 = reportdoa.findByItemQtyToDateR(PLANT,tndate);
		        List<ItemQtyBigDecimalPojo> issuedQtyToDateList2 = reportdoa.findByItemQtyToDateS(PLANT,tndate);

				if (invQryList.size() > 0) {

					int iIndex = 0;
					int irow = 0;
					double sumprdQty = 0, sumOfTotalCost = 0, sumOfTotalPrice = 0, billQty = 0, unbillQty = 0,
							invQty = 0;
					String lastProduct = "";
					for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {

						String result = "";
						Map lineArr = (Map) invQryList.get(iCnt);

						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("PLANT", PLANT);
						resultJsonInt.put("ITEM", strUtils.fString((String) lineArr.get("ITEM")));
						resultJsonInt.put("ITEMDESC", strUtils.fString((String) lineArr.get("ITEMDESC")));
						resultJsonInt.put("PRDCLSID", strUtils.fString((String) lineArr.get("PRDCLSID")));
						resultJsonInt.put("ITEMTYPE", strUtils.fString((String) lineArr.get("ITEMTYPE")));
						resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String) lineArr.get("PRD_BRAND_ID")));
						resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String) lineArr.get("PRD_DEPT_ID")));
						
						float recqty = 0.0f;
			            float issqty = 0.0f;
			            float orecqty = 0.0f;
			            float oissqty = 0.0f;
			            float crecqty = 0.0f;
			            float cissqty = 0.0f;
			            String item = strUtils.fString((String) lineArr.get("ITEM"));
			            List<ItemQtyBigDecimalPojo> rqty = receivedQtyList.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(rqty.size() > 0){
			                resultJsonInt.put("TOTALRECEIVEDQTY", StrUtils.addZeroes(rqty.get(0).getQty().floatValue(), "3"));
			                recqty = rqty.get(0).getQty().floatValue();
			            }else{
			            	 resultJsonInt.put("TOTALRECEIVEDQTY", "0.000");
			            }

			            List<ItemQtyBigDecimalPojo> iqty = issuedQtyList.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(iqty.size() > 0){
			                resultJsonInt.put("TOTALISSUEDQTY", StrUtils.addZeroes(iqty.get(0).getQty().floatValue(), "3"));
			                issqty  = (float) iqty.get(0).getQty().floatValue();
			            }else{
			            	resultJsonInt.put("TOTALISSUEDQTY", "0.000");
			            }

			            List<ItemQtyBigDecimalPojo> orqty = receivedQtyToDateList.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(orqty.size() > 0){
			                orecqty  = (float) orqty.get(0).getQty().floatValue();
			            }
			            List<ItemQtyBigDecimalPojo> oiqty = issuedQtyToDateList.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(oiqty.size() > 0){
			                oissqty  = (float) oiqty.get(0).getQty().floatValue();
			            }

			            resultJsonInt.put("OPENINGSTOCKQTY", StrUtils.addZeroes(orecqty-oissqty, "3"));

			            List<ItemQtyBigDecimalPojo> crqty = receivedQtyToDateList2.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(crqty.size() > 0){
			                crecqty  = (float) crqty.get(0).getQty().floatValue();
			            }
			            List<ItemQtyBigDecimalPojo> ciqty = issuedQtyToDateList2.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(ciqty.size() > 0){
			                cissqty  = (float) ciqty.get(0).getQty().floatValue();
			            }
			            
			            resultJsonInt.put("CLOSINGSTOCKQTY", StrUtils.addZeroes(crecqty-cissqty, "3"));
			           
			            
			            String iscompro = strUtils.fString((String) lineArr.get("ISCOMPRO"));
			            String incpriceunit = strUtils.fString((String) lineArr.get("INCPRICEUNIT"));
			            String incprice = strUtils.fString((String) lineArr.get("INCPRICE"));
			            String avgcost ="0";
			            float price = 0.0f;
			            if(iscompro.equalsIgnoreCase("1")) {
			            	float itemprice = 0;
			            	ProductionBomDAO bomDAO = new ProductionBomDAO();
			            	ArrayList bomList = new ArrayList();
			            	if(bomList.size() > 0) {
			            		for (int bont = 0; bont < bomList.size(); bont++) {
									Map boArr = (Map) bomList.get(bont);
									 String cavgcost = reportdoa.getavgcost(PLANT,strUtils.fString((String) boArr.get("CITEM")),strUtils.fString((String) boArr.get("CHILDUOM")));
			                         itemprice = itemprice + (Float.valueOf(cavgcost) * Float.valueOf(strUtils.fString((String) boArr.get("QTY"))));
			            		}
			            	}
		                   
		                    avgcost = String.valueOf(itemprice);
		                    if (incpriceunit.equalsIgnoreCase("%")) {
		                        price = Float.valueOf(itemprice) + ((Float.valueOf(itemprice) / 100) * Float.valueOf(incprice));
		                    } else {
		                        price = Float.valueOf(itemprice) + Float.valueOf(incprice);
		                    }
		                }else{
		                    avgcost = reportdoa.getavgcost(PLANT,item,strUtils.fString((String) lineArr.get("INVENTORYUOM")));	
		                    if(avgcost == null) {
		                    	avgcost ="0";
		                    }
		                    if (incpriceunit.equalsIgnoreCase("%")) {
		                        price = Float.valueOf(avgcost) + ((Float.valueOf(avgcost) / 100) * Float.valueOf(incprice));
		                    } else {
		                        price = Float.valueOf(avgcost) + Float.valueOf(incprice);
		                    }
		                }
			            
			            resultJsonInt.put("AVERAGECOST",StrUtils.addZeroes(Float.parseFloat(avgcost), numberOfDecimal));
			            resultJsonInt.put("PRICE",StrUtils.addZeroes(price, numberOfDecimal));
			            resultJsonInt.put("TOTALCOST",StrUtils.addZeroes(Float.valueOf(avgcost)*recqty, numberOfDecimal));
			            resultJsonInt.put("TOTALPRICE",StrUtils.addZeroes(price*issqty, numberOfDecimal));
			            
			            List<ItemQtyBigDecimalPojo> lastrqty = receivedQtyFromdateList.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(lastrqty.size() > 0){
			                resultJsonInt.put("LASTRECEIVEDQTY",StrUtils.addZeroes(lastrqty.get(0).getQty().floatValue(), "3"));
			            }else{
			                resultJsonInt.put("LASTRECEIVEDQTY","0.000");
			            }

			            List<ItemQtyBigDecimalPojo> lastiqty = issuedQtyFromDateList.stream().filter(a->a.getItem().equalsIgnoreCase(item)).collect(Collectors.toList());
			            if(lastiqty.size() > 0){
			                resultJsonInt.put("LASTISSUEDQTY",StrUtils.addZeroes(lastiqty.get(0).getQty().floatValue(), "3"));
			            }else{
			            	resultJsonInt.put("LASTISSUEDQTY","0.000");
			            }
			            String soh = new InvUtil().getStockOnHandByProduct(item, PLANT);
			            resultJsonInt.put("STOCKONHAND",StrUtils.addZeroes(Float.valueOf(soh), "3"));
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
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
		
		
		 private JSONObject getProductSummaryCostPriceInventory(HttpServletRequest request,String baseCurrency) {
	            JSONObject resultJson = new JSONObject();
	            JSONArray jsonArray = new JSONArray();
	            JSONArray jsonArrayErr = new JSONArray();
	           
	            StrUtils strUtils = new StrUtils();
	            try {

	               String PLANT= strUtils.fString(request.getParameter("PLANT"));
	               String USERID= strUtils.fString(request.getParameter("LOGIN_USER"));
	               String LOC     = strUtils.fString(request.getParameter("LOC"));
	               String ITEM    = strUtils.fString(request.getParameter("ITEM"));
	               String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	               String  PRD_TYPE_ID     = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	               String  PRD_DEPT_ID     = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
	               String  PRD_BRAND_ID     = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	               String  PRD_DESCRIP     = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	               String  CURRENCYID      = strUtils.fString(request.getParameter("CURRENCYID"));
	               String  CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
	               
	                                      
	                if(CURRENCYID.equals(""))
	                 {
	                          CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
	                          List listQry = _CurrencyDAO.getCurrencyList(PLANT,CURRENCYDISPLAY);
	                          for(int i =0; i<listQry.size(); i++) {
	                              Map m=(Map)listQry.get(i);
	                              CURRENCYID=(String)m.get("currencyid");
	                          }
	                 }        
	                Hashtable ht = new Hashtable();
	                
	                ArrayList invQryList= new ArrayList();
	                invQryList= new InvUtil().getInvListSummaryWithcostpriceinventory(ht,PLANT,ITEM,PRD_DESCRIP,CURRENCYID,baseCurrency,LOC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID);

	                if (invQryList.size() > 0) {
	              
	                double sumprdQty = 0 ,sumOfTotalCost = 0,sumOfTotalPrice=0, billQty=0,unbillQty=0,invQty=0;String lastProduct="";
	                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	                      
	                                String result="";
	                                Map lineArr = (Map) invQryList.get(iCnt);
	                                
	                              JSONObject resultJsonInt = new JSONObject();
	                                resultJsonInt.put("ITEM", strUtils.fString((String)lineArr.get("item")));
	                                resultJsonInt.put("ITEMDESC", strUtils.fString((String)lineArr.get("itemDesc")));
	                                resultJsonInt.put("PRDCLSID", strUtils.fString((String)lineArr.get("category")));
	                                resultJsonInt.put("ITEMTYPE", strUtils.fString((String)lineArr.get("subCategory")));
	                                resultJsonInt.put("PRD_BRAND_ID", strUtils.fString((String)lineArr.get("brand")));
	                                resultJsonInt.put("PRD_DEPT_ID", strUtils.fString((String)lineArr.get("department")));
	    		                    resultJsonInt.put("INVENTORYUOM", strUtils.fString((String) lineArr.get("inventoryUom")));
	    		                    resultJsonInt.put("INVUOMQTY", strUtils.addZeroes(Double.parseDouble(String.valueOf(lineArr.get("stockQty"))), "3"));
	    		                    resultJsonInt.put("PURCHASEUOM", strUtils.fString((String) lineArr.get("purchaseUom")));
	    		                    resultJsonInt.put("PURCHASECOST", strUtils.fString((String) lineArr.get("unitCost")));
	    		                    resultJsonInt.put("SALESUOM", strUtils.fString((String) lineArr.get("salesUom")));
	    		                    resultJsonInt.put("SALESPRICE", strUtils.fString((String) lineArr.get("unitPrice")));
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
	            	jsonArray.add("");
	        		resultJson.put("items", jsonArray);
	                    resultJson.put("SEARCH_DATA", "");
	                    JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                    resultJsonInt.put("ERROR_CODE", "98");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("ERROR", jsonArrayErr);
	            }
	            return resultJson;
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
}
