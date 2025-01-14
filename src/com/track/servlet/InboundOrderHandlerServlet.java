package com.track.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BillDAO;
import com.track.dao.CurrencyDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.PoDetDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.util.HTReportUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class InboundOrderHandlerServlet
 * Change History
 * 
 * 18/6/2014 - included action Inbound Order Summary order with Price and Recv with Price.
 */
public class InboundOrderHandlerServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.InboundOrderHandlerServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.InboundOrderHandlerServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 1L;
	PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InboundOrderHandlerServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
		JSONObject jsonObjectResult = new JSONObject();
		if (action.equals("LOAD_INBOUND_ORDER_DETAILS")) {
			String inbounOrderNo = StrUtils.fString(
					request.getParameter("ORDER_NO")).trim();
			jsonObjectResult = this.loadInboundOrderDetails(plant,
					inbounOrderNo);

		}
		if (action.equals("VALIDATE_LOCATION")) {
			String locationId = StrUtils.fString(request.getParameter("LOC"))
					.trim();
			jsonObjectResult = this.validateLocation(plant, userName,
					locationId);
		}
		if (action.equals("GENERATE_BATCH")) {
			jsonObjectResult = this.generateBatch(request);
		}
		
		
	    if (action.equals("VIEW_IB_SUMMARY_ORD_WITH_COST")) {
	        jsonObjectResult = this.getinboundsmryDetailsOrdWithCost(request);
	       	       
	    }
	
	    if (action.equals("VIEW_IB_SUMMARY_RECV_WITH_COST")) {
	    	  
	        jsonObjectResult = this.getinboundsmryDetailsRecvWithCost(request);
	       	       
	    }
		if (action.equals("VIEW_IB_SUMMARY_RECV")) {
	        jsonObjectResult = this.getinboundsmryDetailsRecv(request);
	       	       
	    }
	    if (action.equals("VIEW_GOODS_RECIPT_SMRY")) {
	        jsonObjectResult = this.getGoodsReciptDetails(request);
	       	       
	    }
	    if (action.equals("VIEW_GOODS_RECIPT_SMRY_WITH_COST")) {
	        jsonObjectResult = this.getGoodsReciptDetailsWithCost(request);
	       	       
	    }
	    
		if (action.equals("VIEW_INBOUND_DETAILS_PRINT")) {
	        jsonObjectResult = this.getInboundDetailsToPrint(request);
	       	       
	    }
	    
		if (action.equals("GRN")) {
	        jsonObjectResult = this.generateGRN(plant, userName);	       	       
	    }	
		if (action.equals("VGRN")) {
			String grno = StrUtils.fString(
					request.getParameter("GRNO")).trim();
	        jsonObjectResult = this.checkGRN(plant, grno);	
	    }	
		if (action.equals("getRemarksDetails")) {
	        jsonObjectResult = this.getRemarksDetails(request);	       	       
	    }
		
		
	
		//this.mLogger.info(this.printInfo, "[JSON OUTPUT] " + jsonObjectResult);
		response.setContentType("application/json");
		//((ServletRequest) response).setCharacterEncoding("UTF-8");
		 response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	private JSONObject generateBatch(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			String batchCode = this.generateBatchCode(request);

			if (batchCode.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("batchCode", batchCode);
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

	@SuppressWarnings("unchecked")
	private String generateBatchCode(HttpServletRequest request)
			throws IOException, ServletException, Exception {
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();

		String BATCHDATE = "";

		String sBatchSeq = "";

		String rtnBatch = "";
		String sZero = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			BATCHDATE = _TblControlDAO.getBatchDate();
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_BATCH_CAPTION);
			ht.put(IDBConstants.TBL_PREFIX1, BATCHDATE.substring(0, 6));
			boolean exitFlag = _TblControlDAO.isExisit(ht, "", plant);
			if (exitFlag == false) {

				rtnBatch = BATCHDATE.substring(0, 6) + "-" + "0000"
						+ IDBConstants.TBL_FIRST_NEX_SEQ;

				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_BATCH_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) BATCHDATE
						.substring(0, 6));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) userName);
				new DateUtils();
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) DateUtils
						.getDateTime());
				_TblControlDAO.insertTblControl(htTblCntInsert, plant);

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");
				if (sBatchSeq.length() == 1) {
					sZero = "0000";
				} else if (sBatchSeq.length() == 2) {
					sZero = "000";
				} else if (sBatchSeq.length() == 3) {
					sZero = "00";
				} else if (sBatchSeq.length() == 4) {
					sZero = "0";
				}

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnBatch = BATCHDATE.substring(0, 6) + "-" + sZero + updatedSeq;
				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_BATCH_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, BATCHDATE
						.substring(0, 6));
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				_TblControlDAO.update(updateQyery.toString(), htTblCntUpdate,
						"", plant);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return rtnBatch;
	}
	
	 // Start code modified by Bruhan for inorderwithprice on 19/12/12 
	 private JSONObject getinboundsmryDetailsOrdWithCost(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        HTReportUtil movHisUtil       = new HTReportUtil();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList movQryList  = new ArrayList();
	        
	        DecimalFormat decformat = new DecimalFormat("#,##0.00");
	       
	        StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="",taxby="";
	  
	        try {
	        
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
	           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
	           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
	           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
	           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
	           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
	           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
	           String  CURRENCYID      = strUtils.fString(request.getParameter("CURRENCYID"));
	           String  CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
	           String  CURRENCYUSEQT = strUtils.fString(request.getParameter("currencyuseqt"));
	       	   String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	           //Start code added by 
	           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
	           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	           String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
	           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	           String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
	           String VIEWSTATUS=StrUtils.fString(request.getParameter("VIEWSTATUS"));
	           String SUPPLIERTYPE=StrUtils.fString(request.getParameter("SUPPLIERTYPE"));
	           String LOCALEXPENSES=StrUtils.fString(request.getParameter("LOCALEXPENSES"));
			   String DELDATE=StrUtils.fString(request.getParameter("DELDATE"));
			   String UOM=StrUtils.fString(request.getParameter("UOM"));
			  if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)

	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

               if(CURRENCYID.equals(""))
               {
                        CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
                        List listQry = _CurrencyDAO.getCurrencyListWithcurrencySeq(PLANT,CURRENCYDISPLAY);
                        for(int i =0; i<listQry.size(); i++) {
                            Map m=(Map)listQry.get(i);
                            CURRENCYID=(String)m.get("currencyid");
                            CURRENCYUSEQT=(String)m.get("CURRENCYUSEQT");
                        }
               }     
	   
	           Hashtable ht = new Hashtable();
	           
	            if(StrUtils.fString(JOBNO).length() > 0)         ht.put("A.JOBNUM",JOBNO);
		        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
		        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("A.PONO",ORDERNO);
		       // if(StrUtils.fString(CUSTOMER).length() > 0)      ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTOMER));
		        if(StrUtils.fString(STATUS).length() > 0)
		        {	
					if(STATUS.equalsIgnoreCase("DRAFT"))
		        	{
		        		ht.put("A.ORDER_STATUS","DRAFT");
		        		ht.put("B.LNSTAT","N");	
		        	}
		        	else
		        	{
		        		ht.put("B.LNSTAT", STATUS);
		        	   
		        	   if(STATUS.equalsIgnoreCase("N"))
		           		ht.put("A.ORDER_STATUS","OPEN");
		        	}
				}
		        	
		        if(StrUtils.fString(ORDERTYPE).length() > 0)     ht.put("A.ORDERTYPE",ORDERTYPE);
		       
		        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
		        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
		        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
		        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
		        if(StrUtils.fString(statusID).length() > 0) ht.put("A.STATUS_ID",statusID);
		        if(StrUtils.fString(SUPPLIERTYPE).length() > 0) ht.put("SUPPLIERTYPE", SUPPLIERTYPE);
			    if(StrUtils.fString(DELDATE).length() > 0) ht.put("A.DELDATE", DELDATE);
			    if(StrUtils.fString(UOM).length() > 0) ht.put("B.UNITMO", UOM);	
			   taxby=_PlantMstDAO.getTaxBy(PLANT);
		        if (LOCALEXPENSES.equalsIgnoreCase("0"))
		        {
			          if(taxby.equalsIgnoreCase("BYORDER"))
					{
			           movQryList = movHisUtil.getSupplierPOInvoiceSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER,VIEWSTATUS);
					}
			        else
			        {
			        	movQryList = movHisUtil.getSupplierPOInvoiceSummaryByProductGst(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER,VIEWSTATUS);
			        }
		        }
		        else
		        {
		        	 if(taxby.equalsIgnoreCase("BYORDER"))
						{
		        		 	if(((String)request.getSession().getAttribute("SYSTEMNOW")).equalsIgnoreCase("INVENTORY")) {
		        		 		movQryList = movHisUtil.getSupplierPOInvoiceLocalExpSummaryForInventory(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER,VIEWSTATUS);
		        		 	}else {
		        		 		movQryList = movHisUtil.getSupplierPOInvoiceLocalExpSummary(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER,VIEWSTATUS);
		        		 	}
				           
						}
				        else
				        {
				        	movQryList = movHisUtil.getSupplierPOInvoiceLocalExpSummaryByProductGst(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER,VIEWSTATUS);
				        }
		        }
	                         
	            if (movQryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	            double sumprdQty = 0;String lastProduct="";
	            double totalOrdCost=0,totaltax=0,totOrdCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
	           
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                      
	               	 		// Index = iCnt + 1;
	                             
	                            String result="";
	                            Map lineArr = (Map) movQryList.get(iCnt);
	                            String custcode =(String)lineArr.get("custcode");
	                            String pono = (String)lineArr.get("pono");
	                           
	              	          
	                            Float gstpercentage =  Float.parseFloat(((String) lineArr.get("Tax").toString())) ;
			            			
	                           
	              	             //indiviual subtotal price details
	              	             double orderCost = Double.parseDouble((String)lineArr.get("OrderCost"));
	              	           //orderCost = StrUtils.RoundDB(orderCost,2);
	              	           //double tax = Double.parseDouble((String)lineArr.get("taxval"));
	                           // double tax = (orderCost*gstpercentage)/100;
	              	           double tax =0.0;
	              	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
	              	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
	              	           if(taxid != 0){
	              	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
	              	        		   tax = (orderCost*gstpercentage)/100;
	              	        	   } else 
	              	        		 gstpercentage=Float.parseFloat("0.000");
	              	        	   
	              	           } else 
	              	        		 gstpercentage=Float.parseFloat("0.000");
	                            double ordCostwTax =orderCost+tax;
	                            
	                            //total price details
	                            totalOrdCost=totalOrdCost+Double.parseDouble(((String)lineArr.get("OrderCost").toString()));
	                            totaltax  =totaltax + tax;
	                            totOrdCostWTax = totOrdCostWTax+ordCostwTax;
	                            
	                            //Grand total details
	                            costGrandTot = costGrandTot+Double.parseDouble((String)lineArr.get("OrderCost"));;
	                            taxGrandTot = taxGrandTot+tax;
	                            costWTaxGrandTot = costWTaxGrandTot+ordCostwTax;
	                         
	                            
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                            
	                            String qtyOrValue =(String)lineArr.get("qtyor");
	                   			String qtyValue =(String)lineArr.get("qty");
	                   			String unitCost = (String)lineArr.get("unitcost");
	                   			String taxValue = String.valueOf(tax);
	                   			String ordCostwTaxValue=String.valueOf(ordCostwTax);
	                   			String orderCostValue=String.valueOf(orderCost);
	                   			String gstpercentageValue = String.valueOf(gstpercentage);
	                   			
	                            float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
	                            float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
	                            float unitCostVal ="".equals(unitCost) ? 0.0f :  Float.parseFloat(unitCost);
	                            float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
	                            float ordCostwTaxVal ="".equals(ordCostwTaxValue) ? 0.0f :  Float.parseFloat(ordCostwTaxValue);
	                            float orderCostVal ="".equals(orderCostValue) ? 0.0f :  Float.parseFloat(orderCostValue);
	                            float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
	                            
	                            if(gstpercentageVal==0f){
	                            	gstpercentageValue="0.000";
	                            }else{
	                            	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            if(qtyOrVal==0f){
	                            	qtyOrValue="0.000";
	                            }else{
	                            	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(qtyVal==0f){
	                            	qtyValue="0.000";
	                            }else{
	                            	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(unitCostVal==0f){
	                            	unitCost="0.00000";
	                            }else{
	                            	unitCost=unitCost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(taxVal==0f) {
	                            	taxValue="0.000";
	                            }else {
	                            	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(ordCostwTaxVal==0f) {
	                            	ordCostwTaxValue="0.00000";
	                            }else {
	                            	ordCostwTaxValue=ordCostwTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(orderCostVal==0f) {
	                            	orderCostValue="0.00000";
	                            }else {
	                            	orderCostValue=orderCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                       
	                        //DISPLAY COST BASED ON CURRENCY VALUE
	                        float CurUnitCost=Float.parseFloat(unitCost)*Float.parseFloat(CURRENCYUSEQT);
	                        float CurOrderCostValue=Float.parseFloat(orderCostValue)*Float.parseFloat(CURRENCYUSEQT);
	                        float CurordCostwTaxValue=Float.parseFloat(ordCostwTaxValue)*Float.parseFloat(CURRENCYUSEQT);
	                        float CurtaxValue=Float.parseFloat(taxValue)*Float.parseFloat(CURRENCYUSEQT);
	                        
	                        String currencyid = (String)lineArr.get("CURRENCYID");
                            double currencyseqt = Double.valueOf((String)lineArr.get("CURRENCYUSEQT"));
                            double rcvCostconv = 0,recvCostwTaxConv=0,taxValcon=0;
                            
                            rcvCostconv = orderCostVal * currencyseqt; 
                            String SrcvCostconv= String.valueOf(rcvCostconv);
                            SrcvCostconv = currencyid + String.format("%.2f", rcvCostconv);
                            
                            recvCostwTaxConv = ordCostwTaxVal * currencyseqt; 
                            String SrecvCostwTaxconv = currencyid + String.format("%.2f", recvCostwTaxConv);
                            String exchangerate = String.format("%.2f", currencyseqt);
                            
                            taxValcon = taxVal  * currencyseqt; 
                            String StaxValcon = currencyid + String.format("%.2f", taxValcon);
	                        
	                     //  if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
	                    	 Index = Index + 1;
	                    	 //resultJsonInt.put("Index", Index);
	                    	 resultJsonInt.put("pono",pono );
	                    	 resultJsonInt.put("ordertype",  StrUtils.fString((String)lineArr.get("ordertype")));
	                    	 resultJsonInt.put("custname",  StrUtils.fString((String)lineArr.get("custname")));
	                    	 resultJsonInt.put("item",  StrUtils.fString((String)lineArr.get("item")));
	                    	 resultJsonInt.put("itemdesc",  StrUtils.fString((String)lineArr.get("itemdesc")));
	                    	 resultJsonInt.put("DetailItemDesc",  StrUtils.fString((String)lineArr.get("DetailItemDesc")));
	                    	 resultJsonInt.put("CollectionDate",  StrUtils.fString((String)lineArr.get("CollectionDate")));
	                    	 resultJsonInt.put("qtyor", qtyOrValue);
	                    	 resultJsonInt.put("qty", qtyValue);
//	                    	 resultJsonInt.put("unitCost", unitCost);
	                    	 resultJsonInt.put("unitCost", CurUnitCost);
	                    	 resultJsonInt.put("gstpercentage", gstpercentageValue);
	                    	 resultJsonInt.put("orderCost", orderCostValue);
//	                    	 resultJsonInt.put("orderCost", CurOrderCostValue);
	                    	 resultJsonInt.put("tax", taxValue);
//	                    	 resultJsonInt.put("tax", CurtaxValue);
	                    	 resultJsonInt.put("ordCostwTax", ordCostwTaxValue);
//	                    	 resultJsonInt.put("ordCostwTax", CurordCostwTaxValue);
	                    	 resultJsonInt.put("status_id", StrUtils.fString((String)lineArr.get("status_id")));
	                    	 resultJsonInt.put("users",StrUtils.fString((String)lineArr.get("users")));
	                    	 resultJsonInt.put("remarks", StrUtils.fString((String)lineArr.get("remarks")));
	                    	 resultJsonInt.put("UOM", StrUtils.fString((String)lineArr.get("UOM")));
	                    	 resultJsonInt.put("remarks3", StrUtils.fString((String)lineArr.get("remarks3")));
	                    	 resultJsonInt.put("users",StrUtils.fString((String)lineArr.get("users")));
	                    	 resultJsonInt.put("polnno", (String)lineArr.get("polnno"));
	                    	 resultJsonInt.put("jobnum", (String)lineArr.get("jobnum"));
	                    	 
	                    	 
	                    	 resultJsonInt.put("recvConvCost", SrcvCostconv);
                             resultJsonInt.put("recvCostConvwTax", SrecvCostwTaxconv);
                             resultJsonInt.put("currencyid", currencyid);
                             resultJsonInt.put("exchangerate", exchangerate);
                             resultJsonInt.put("taxcon", StaxValcon);
	                    	
	                    	 //resultJsonInt.put("QTY", <a href='#' style='text-decoration: none;' onClick=\"javascript:popUpWin('inboundOrderPrdRemarksList.jsp?REMARKS1="+StrUtils.fString((String)lineArr.get("remarks"))+"&REMARKS2="+StrUtils.fString((String)lineArr.get("remarks3"))+"&ITEM=" + StrUtils.fString((String)lineArr.get("item"))+"&PONO="+pono+"&ITEM="+StrUtils.fString((String)lineArr.get("item"))+"&POLNNO="+(String)lineArr.get("polnno")+"\');\">"&#9432;</a>);
	                    	            	   
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
	        		//jsonArray.add("");
	        		//resultJson.put("items", jsonArray);
	        		resultJson.put("SEARCH_DATA", "");
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
	}


		 private JSONObject getinboundsmryDetailsRecvWithCost(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        HTReportUtil movHisUtil       = new HTReportUtil();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList movQryList  = new ArrayList();
	        String decimalZeros = "";
	        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
	            decimalZeros += "#";
	        }
	        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
	       
	        StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="",taxby="";
	         try {
	        
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
	           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
	           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
	           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
	           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
	           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
	           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
	           //CODE START BY IMTHI FOR CURRENCY SEARCH
	           String  CURRENCYID      = strUtils.fString(request.getParameter("CURRENCYID"));
	           String  CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
	           String  CURRENCYUSEQT = strUtils.fString(request.getParameter("currencyuseqt"));
	       	   String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	       	   
	           //Start code added by Bruhan for product brand,type on 2/sep/13
	           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
	           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	           String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
	           //End code added by Bruhan for product brand,type on 2/sep/13 
	           String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
	           String GRNO = StrUtils.fString(request.getParameter("GRNO"));
	           String SUPPLIERTYPE=StrUtils.fString(request.getParameter("SUPPLIERTYPE")); //Razeen
	           String LOCALEXPENSES=StrUtils.fString(request.getParameter("LOCALEXPENSES"));
	           String UOM=StrUtils.fString(request.getParameter("UOM"));
	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)

	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	           
               if(CURRENCYID.equals(""))
               {
                        CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
                        List listQry = _CurrencyDAO.getCurrencyListWithcurrencySeq(PLANT,CURRENCYDISPLAY);
                        for(int i =0; i<listQry.size(); i++) {
                            Map m=(Map)listQry.get(i);
                            CURRENCYID=(String)m.get("currencyid");
                            CURRENCYUSEQT=(String)m.get("CURRENCYUSEQT");
                        }
               }  
	              
	   
	           Hashtable ht = new Hashtable();
	           
	            if(StrUtils.fString(JOBNO).length() > 0)         ht.put("JOBNUM",JOBNO);
		        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("ITEM",ITEMNO);
		        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
		        if(StrUtils.fString(STATUS).length() > 0)        ht.put("STATUS",STATUS);
		        if(StrUtils.fString(ORDERTYPE).length() > 0)     ht.put("ORDERTYPE",ORDERTYPE);
		        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
		        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
		        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);
		        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
		        if(StrUtils.fString(statusID).length() > 0) ht.put("STATUS_ID",statusID);
		        if(StrUtils.fString(GRNO).length() > 0) ht.put("GRNO", GRNO);
		        if(StrUtils.fString(SUPPLIERTYPE).length() > 0) ht.put("SUPPLIERTYPE",SUPPLIERTYPE);

		        taxby=_PlantMstDAO.getTaxBy(PLANT);
				if(LOCALEXPENSES.equals("0")){
		        if(taxby.equalsIgnoreCase("BYORDER"))
				{     
		       	  movQryList = movHisUtil.getReportIBSummaryDetailsByCost(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER);
				}else
				{
				  movQryList = movHisUtil.getReportIBSummaryDetailsByCostByProductGst(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER);	
				}
                }else{
		        	 if(taxby.equalsIgnoreCase("BYORDER"))
						{     
				       	  movQryList = movHisUtil.getReportIBSummaryDetailsByCostlocalexpenses(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER);
						}else
						{
						  movQryList = movHisUtil.getReportIBSummaryDetailsByCostByProductGstlocalexpenses(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER);	
						}
		        	
		        } 
	            if (movQryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	            double sumprdQty = 0;String lastProduct="";
	            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
	           
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                            String result="";
	                            Map lineArr = (Map) movQryList.get(iCnt);
	                            String custcode =(String)lineArr.get("custname");
	                            String pono = (String)lineArr.get("pono");
	                            
	              	            Float gstpercentage =  Float.parseFloat(((String) lineArr.get("Tax").toString())) ;
			            		
								double recvCost = Double.parseDouble((String)lineArr.get("RecvCost"));
//								recvCost = StrUtils.RoundDB(recvCost,2);
								//double tax = (recvCost*gstpercentage)/100;
//								double tax = Double.parseDouble((String)lineArr.get("taxval"));
								
								   double tax =0.0;
		              	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
		              	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
		              	           if(taxid != 0){
		              	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
		              	        		   tax = (recvCost*gstpercentage)/100;
		              	        	   } else 
		              	        		 gstpercentage=Float.parseFloat("0.000");
		              	        	   
		              	           }else 
		              	        		 gstpercentage=Float.parseFloat("0.000");
		              	           
								double recvCostwTax =recvCost+tax;
	                            
	                            //total price details
								totalRecvCost=totalRecvCost+Double.parseDouble(((String)lineArr.get("RecvCost").toString()));
	                            totaltax  =totaltax + tax;
	                            totRecvCostWTax = totRecvCostWTax+recvCostwTax;
	                            
	                            //Grand total details
	                            costGrandTot = costGrandTot+Double.parseDouble((String)lineArr.get("RecvCost"));;
	                            taxGrandTot = taxGrandTot+tax;
	                            costWTaxGrandTot = costWTaxGrandTot+recvCostwTax;
	                         
	                            
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                            
	                            String qtyOrValue =(String)lineArr.get("qtyor");
	                   			String qtyValue =(String)lineArr.get("qty");
	                   			String unitCostValue = (String)lineArr.get("unitcost");
	                   			String recvCostValue = String.valueOf(recvCost);
	                   			String taxValue = String.valueOf(tax);
	                   			String recvCostwTaxValue = String.valueOf(recvCostwTax);
	                   			String gstpercentageValue = String.valueOf(gstpercentage);
	                   			
	                            float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
	                            float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
	                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
	                            float recvCostVal ="".equals(recvCostValue) ? 0.0f :  Float.parseFloat(recvCostValue);
	                            float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
	                            float recvCostwTaxVal ="".equals(recvCostwTaxValue) ? 0.0f :  Float.parseFloat(recvCostwTaxValue);
	                            float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);

	                            //DISPLAY COST BASED ON CURRENCY VALUE
		                        float CurunitCostValue=Float.parseFloat(unitCostValue)*Float.parseFloat(CURRENCYUSEQT);
		                        float CurrecvCostValue=Float.parseFloat(recvCostValue)*Float.parseFloat(CURRENCYUSEQT);
		                        float CurtaxValue=Float.parseFloat(taxValue)*Float.parseFloat(CURRENCYUSEQT);
		                        float CurrecvCostwTaxValue=Float.parseFloat(recvCostwTaxValue)*Float.parseFloat(CURRENCYUSEQT);
		                        
	                            if(qtyOrVal==0f){
	                            	qtyOrValue="0.000";
	                            }else{
	                            	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(qtyVal==0f){
	                            	qtyValue="0.000";
	                            }else{
	                            	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(unitCostVal==0f){
	                            	unitCostValue="0.00000";
	                            }else{
	                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(recvCostVal==0f){
	                            	recvCostValue="0.00000";
	                            }else{
	                            	recvCostValue=recvCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(taxVal==0f){
	                            	taxValue="0.000";
	                            }else{
	                            	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(recvCostwTaxVal==0f){
	                            	recvCostwTaxValue="0.00000";
	                            }else{
	                            	recvCostwTaxValue=recvCostwTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }if(gstpercentageVal==0f){
//	                            	gstpercentageValue="0.00000";
	                            	gstpercentageValue="0.000";
	                            	//gstpercentageValue=DbBean.NOOFDECIMALPTSFORWEIGHT;
	                            }else{
	                            	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            
	                            
	                            String currencyid = (String)lineArr.get("CURRENCYID");
	                            double currencyseqt = Double.valueOf((String)lineArr.get("CURRENCYUSEQT"));
	                            double rcvCostconv = 0,recvCostwTaxConv=0,taxValcon=0;
	                            
	                            double unitCostConv = Double.valueOf(CurunitCostValue)  * currencyseqt;
	                            String SnitCostConv = currencyid + String.format("%.2f", unitCostConv);
	                            
	                            rcvCostconv = recvCostVal * currencyseqt; 
	                            String SrcvCostconv= String.valueOf(rcvCostconv);
//	                            currencyid = "";
	                            SrcvCostconv = currencyid + String.format("%.2f", rcvCostconv);
	                            
	                            recvCostwTaxConv = recvCostwTaxVal * currencyseqt; 
	                            String SrecvCostwTaxconv = currencyid + String.format("%.2f", recvCostwTaxConv);
	                            String exchangerate = String.format("%.2f", currencyseqt);
	                            
	                            taxValcon = taxVal  * currencyseqt; 
	                            String StaxValcon = currencyid + String.format("%.2f", taxValcon);
	                            
	                      // if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
	                    	 Index = Index + 1;
	                    	 resultJsonInt.put("Index",(Index));
	                    	 resultJsonInt.put("pono",(pono));
	                    	 resultJsonInt.put("ordertype",StrUtils.fString((String)lineArr.get("ordertype")));
	                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("custname")));
	                    	 resultJsonInt.put("remarks",StrUtils.fString((String)lineArr.get("remarks")));
	                    	 resultJsonInt.put("remarks3",StrUtils.fString((String)lineArr.get("remarks3")));
	                    	 resultJsonInt.put("item",StrUtils.fString((String)lineArr.get("item")));
	                    	 resultJsonInt.put("itemdesc",StrUtils.fString((String)lineArr.get("itemdesc")));
	                    	 resultJsonInt.put("RECVDATE",StrUtils.fString((String)lineArr.get("RECVDATE")));
	                    	 resultJsonInt.put("GRNO",StrUtils.fString((String)lineArr.get("GRNO")));
	                    	 resultJsonInt.put("UOM",StrUtils.fString((String)lineArr.get("UOM")));
//	                    	 resultJsonInt.put("qtyor",qtyOrValue);
	                    	 resultJsonInt.put("qtyor",strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) qtyOrValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//	                    	 resultJsonInt.put("qty",qtyValue);
	                    	 resultJsonInt.put("qty",strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) qtyValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
//	                    	 resultJsonInt.put("unitCost",unitCostValue);
	                    	 resultJsonInt.put("unitCost",CurunitCostValue);
	                    	 resultJsonInt.put("unitCostConv", SnitCostConv);
//	                    	 resultJsonInt.put("gstpercentage", gstpercentageValue);
	                    	 resultJsonInt.put("gstpercentage", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) gstpercentageValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
	                    	 resultJsonInt.put("recvCost",recvCostValue);
//	                    	 resultJsonInt.put("recvCost",CurrecvCostValue);
	                    	 resultJsonInt.put("tax",taxValue);
//	                    	 resultJsonInt.put("tax",CurtaxValue);
	                    	 resultJsonInt.put("recvCostwTax",recvCostwTaxValue);
//	                    	 resultJsonInt.put("recvCostwTax",CurrecvCostwTaxValue);
	                    	 resultJsonInt.put("status_id",StrUtils.fString((String)lineArr.get("status_id")));
	                    	 resultJsonInt.put("users",StrUtils.fString((String)lineArr.get("users")));
	                    	 resultJsonInt.put("jobNum", (String)lineArr.get("jobNum"));
	                    	 
	                    	 
	                    	 resultJsonInt.put("recvConvCost", SrcvCostconv);
                             resultJsonInt.put("recvCostConvwTax", SrecvCostwTaxconv);
                             resultJsonInt.put("currencyid", currencyid);
                             resultJsonInt.put("exchangerate", exchangerate);
                             resultJsonInt.put("taxcon", StaxValcon);
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

		 /*************Modification History*********************************
	         Bruhan on Sep 26 2014, Description: To change transaction date as received date
	    */
		 private JSONObject getGoodsReciptDetails(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        HTReportUtil reptUtil       = new HTReportUtil();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList QryList  = new ArrayList();
	        
	        DecimalFormat decformat = new DecimalFormat("#,##0.00");
	       
	        StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="";
	        try {
	        	
	        	 String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
		           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
		           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
		           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
		           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
		           String  SUPPLIER = StrUtils.fString(request.getParameter("CNAME"));
		           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
		           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
		           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
		           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
		           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
		           String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
		           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
		           String	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
				   String  TO_ASSIGNEE = StrUtils.fString(request.getParameter("TOASSINEE"));
				   String  LOANASSIGNEE = StrUtils.fString(request.getParameter("LOANASSINEE"));
				   String	REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
				   String  BATCH = StrUtils.fString(request.getParameter("BATCH"));
				   String  LOC = StrUtils.fString(request.getParameter("LOC"));
				   String  FILTER = StrUtils.fString(request.getParameter("FILTER"));
//				   String  INVOICENUM = StrUtils.fString(request.getParameter("INVOICENUM"));
				   String	LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
				   String	LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
				   String GRNO = StrUtils.fString(request.getParameter("GRNO"));
		        	
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	        	   fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
	           
	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	        	   tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);

	           if (StrUtils.fString(ITEMNO).length() >0)
		         {
					 ItemMstUtil itemMstUtil = new ItemMstUtil();
					 itemMstUtil.setmLogger(mLogger);
					 String temItem = itemMstUtil.isValidAlternateItemInItemmst( PLANT, ITEMNO);
					 if (temItem != "") {
					 	ITEMNO = temItem;
					 } else {
					 	throw new Exception("Product Details not found!");
					 }
		         } 
	   
	           Hashtable ht = new Hashtable();
	        
		        if (StrUtils.fString(ITEMNO).length() > 0)      ht.put("a.ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)     ht.put("a.PONO", ORDERNO);
				if (StrUtils.fString(SUPPLIER).length() > 0)    ht.put("a.CNAME", SUPPLIER);
				if (StrUtils.fString(TO_ASSIGNEE).length() > 0) ht.put("a.CNAME_TO", TO_ASSIGNEE);
				if (StrUtils.fString(LOANASSIGNEE).length() > 0)ht.put("a.CNAME_LOAN", LOANASSIGNEE);
				if (StrUtils.fString(ORDERTYPE).length() > 0)   ht.put("ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(BATCH).length() > 0)       ht.put("a.BATCH", BATCH);
				if (StrUtils.fString(LOC).length() > 0)         ht.put("a.LOC", LOC);
				if(StrUtils.fString(LOC_TYPE_ID).length() > 0)  ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
				if(StrUtils.fString(PRD_TYPE_ID).length() > 0)  ht.put("ITEMTYPE",PRD_TYPE_ID);
		        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
		        if(StrUtils.fString(PRD_CLS_ID).length() > 0)   ht.put("PRD_CLS_ID",PRD_CLS_ID);
		        if(StrUtils.fString(PRD_DEPT_ID).length() > 0)   ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
		        if(StrUtils.fString(REASONCODE).length() > 0)   ht.put("a.REMARK",REASONCODE);
		        if(StrUtils.fString(FILTER).length() > 0)   ht.put("FILTER",FILTER);
		        if(StrUtils.fString(LOC_TYPE_ID2).length() > 0)  ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
		        if(StrUtils.fString(LOC_TYPE_ID3).length() > 0)  ht.put("LOC_TYPE_ID2",LOC_TYPE_ID3);
		        if(StrUtils.fString(GRNO).length() > 0)  ht.put("a.GRNO",GRNO);
			    // if (StrUtils.fString(INVOICENUM).length() > 0) ht.put("a.INVOICENO", INVOICENUM);
		       
		       QryList = reptUtil.getGoodsRecieveSummaryList_new(ht, fdate,tdate, DIRTYPE, PLANT,PRD_DESCRIP);     
			
	            if (QryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	            double totalRecvQty = 0;String lastProduct="";
	            double totalRecvCost1=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
	            String strDate="";
	                for (int iCnt =0; iCnt<QryList.size(); iCnt++){
	                            String result="";
	                            Map lineArr = (Map) QryList.get(iCnt);
	                            String itemCode =(String)lineArr.get("item");
	                            String pono = (String)lineArr.get("pono");
	                            //total RecvQty
								totalRecvQty=totalRecvQty+Double.parseDouble(((String)lineArr.get("recqty").toString()));
	                           
	                            
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                            
	                    strDate=(String)lineArr.get("transactiondate");   
	                      if(strDate.equals("1900-01-01")||strDate.equals("")||strDate==null)
		                  {
		              			strDate=(String)lineArr.get("transactioncratdate"); //transactioncratdate
		              	  }
	                    //  if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(itemCode))){
	                      
	                      
	                      		String recQtyValue=(String)lineArr.get("recqty");
	                      		String printQtyValue=(String)lineArr.get("PRINTQTY");
	                      		
	                      		
	                      		float recQtyVal= "".equals(recQtyValue) ? 0.0f :  Float.parseFloat(recQtyValue);
	                      		float printQtyVal= "".equals(printQtyValue) ? 0.0f :  Float.parseFloat(printQtyValue);
	                      		
	                      		if(recQtyVal==0f){
	                      			recQtyValue="0.000";
	                			}else{
	                				recQtyValue=recQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                			}
	                      		
	                      		if(printQtyVal==0f){
	                      			printQtyValue="0.000";
	                			}else{
	                				printQtyValue=printQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                			}
	                      		
	                      
                    	 
	                            Index = Index + 1;
	                            
	                            resultJsonInt.put("pono",StrUtils.fString((String)lineArr.get("pono")));
	                            resultJsonInt.put("grno",StrUtils.fString((String)lineArr.get("grno")));
	                            resultJsonInt.put("lnno",StrUtils.fString((String)lineArr.get("lnno")));
                             resultJsonInt.put("strDate", StrUtils.fString(strDate));
                             resultJsonInt.put("cname", StrUtils.fString((String)lineArr.get("cname")));
                             resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
                             resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
                             resultJsonInt.put("loc", StrUtils.fString((String)lineArr.get("loc")));
                             resultJsonInt.put("batch", StrUtils.fString((String)lineArr.get("batch")));
                             resultJsonInt.put("expiredate", StrUtils.fString((String)lineArr.get("expiredate")));
                             resultJsonInt.put("recqty", recQtyValue);
                             resultJsonInt.put("PRINTQTY", printQtyValue);
                             resultJsonInt.put("uom", StrUtils.fString((String)lineArr.get("uom")));
                             resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
                             resultJsonInt.put("remark", StrUtils.fString((String)lineArr.get("remark")));
                             jsonArray.add(resultJsonInt);
                             
                          /*   
	                    	result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
	                        + "<td>" + pono + "</td>"
 						//	+ "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("invoiceno")) + "</td>"
	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(strDate) + "</td>"
	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("cname")) + "</td>"
	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("loc")) + "</td>"
	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("batch")) + "</td>"
	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("expiredate")) + "</td>"
	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("recqty")) + "</td>"
	                       + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("uom")) + "</td>"
	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remark")) + "</td>"
	                        + "<tr>";
	                        if(iIndex+1 == QryList.size()){
	                                irow=irow+1;
	                                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  

	                                result +=  "<TR bgcolor ="+bgcolor+" >"
	                                        +"<TD colspan=7></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalRecvQty))+"</b></TD><TD></TD><TD></TD>  </TR>";
	                                   
	                         } }else{
	                          
	                        	 totalRecvQty=totalRecvQty-Double.parseDouble(((String)lineArr.get("recqty").toString()));
	                        	 totalRecvQty = StrUtils.RoundDB(totalRecvQty,2);
	                        	 result +=  "<TR bgcolor ="+bgcolor+" >"
	                                        +"<TD colspan=7></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalRecvQty))+"</b></TD><TD></TD><TD></TD>  </TR>";
	                           irow=irow+1;
	                           Index = 0;
	                           Index=Index+1;
	                           bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                            result += "<tr valign=\"middle\" bgcolor="+bgcolor+">" 
	         	                        + "<td>" + pono + "</td>"
 									//	+ "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("invoiceno")) + "</td>"
	         	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString(strDate) + "</td>"
	         	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("cname")) + "</td>"
	         	                        + "<td  align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("item")) + "</td>"
	         	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("itemdesc")) + "</td>"
	         	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("loc")) + "</td>"
	         	                        + "<td align = left>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("batch")) + "</td>"
	         	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("expiredate")) + "</td>"
	         	                        + "<td align = right>&nbsp;&nbsp;&nbsp;" + strUtils.formatNum((String)lineArr.get("recqty")) + "</td>"
	         	                       + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("uom")) + "</td>"
	         	                        + "<td align = center>&nbsp;&nbsp;&nbsp;" + StrUtils.fString((String)lineArr.get("remark")) + "</td>"
	                            + "<tr>";
      
	                            totalRecvQty=Double.parseDouble(((String)lineArr.get("recqty").toString()));
	                            totalRecvQty = StrUtils.RoundDB(totalRecvQty,2);
	                      	  
	                                 if(iIndex+1 == QryList.size()){ 
	                                     irow=irow+1;
	                                     bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                                     result +=  "<TR bgcolor ="+bgcolor+" >"
	 	                                        +"<TD colspan=7></TD> <TD align= \"right\"><b>Total:</b></td><TD align= \"right\"><b>"+StrUtils.formatNum(String.valueOf(totalRecvQty))+"</b></TD><TD></TD><TD></TD>  </TR>";
	                        }
	                        }
	                             irow=irow+1;
	                             iIndex=iIndex+1;
	                             lastProduct = itemCode;
	                             
	                             if(QryList.size()==iCnt+1){
	                            	 irow=irow+1;
	                                 bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
	                                 result +=  "<TR bgcolor ="+bgcolor+" >"
	                                         +"<TD colspan=11></TD> <TD align= \"right\"><b>Grand Total:</b></td><TD align= \"right\"><b>"+decformat.format(StrUtils.RoundDB(costGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+decformat.format(StrUtils.RoundDB(taxGrandTot,2))+"</b></TD><TD align= \"right\"><b>"+decformat.format(StrUtils.RoundDB(costWTaxGrandTot,2))+"</b></TD><TD></TD> </TR>";

	                             }
	                          
	                            resultJsonInt.put("RECVDETAILS", result);
	                         
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

	       
		 private JSONObject getGoodsReciptDetailsWithCost(HttpServletRequest request) {
			 JSONObject resultJson = new JSONObject();
		        JSONArray jsonArray = new JSONArray();
		        JSONArray jsonArrayErr = new JSONArray();
		        HTReportUtil reptUtil       = new HTReportUtil();
		        DateUtils _dateUtils = new DateUtils();
		        ArrayList QryList  = new ArrayList();
		        
		        DecimalFormat decformat = new DecimalFormat("#,##0.00");
		       
		        StrUtils strUtils = new StrUtils();
		        String fdate="",tdate="",taxby="";
		        try {
		        
		        	 String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
			           String GRNO = StrUtils.fString(request.getParameter("GRNO"));
			           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
			           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
			           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
			           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
			           String  SUPPLIER = StrUtils.fString(request.getParameter("CNAME"));
			           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
			           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
			           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			           String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
			           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			           String	LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
					   String  TO_ASSIGNEE = StrUtils.fString(request.getParameter("TO_ASSIGNEE"));
					   String  LOANASSIGNEE = StrUtils.fString(request.getParameter("LOANASSIGNEE"));
					   String	REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
					   String  BATCH = StrUtils.fString(request.getParameter("BATCH"));
					   String  LOC = StrUtils.fString(request.getParameter("LOC"));
					   String  FILTER = StrUtils.fString(request.getParameter("FILTER"));
					   String  LOCALEXPENSES = StrUtils.fString(request.getParameter("LOCALEXPENSES"));
					  // String  INVOICENUM = StrUtils.fString(request.getParameter("INVOICENUM"));
					   String	LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
							   
		           
					   if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			           String curDate =DateUtils.getDate();
			           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

			           if (FROM_DATE.length()>5)
			        	   fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
			           
			           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
			           if (TO_DATE.length()>5)
			        	   tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);

			           if (StrUtils.fString(ITEMNO).length() >0)
				         {
							 ItemMstUtil itemMstUtil = new ItemMstUtil();
							 itemMstUtil.setmLogger(mLogger);
							 String temItem = itemMstUtil.isValidAlternateItemInItemmst( PLANT, ITEMNO);
							 if (temItem != "") {
							 	ITEMNO = temItem;
							 } else {
							 	throw new Exception("Product Details not found!");
							 }
				         } 
			   
			           Hashtable ht = new Hashtable();
			        
			           if (StrUtils.fString(ITEMNO).length() > 0)      ht.put("a.ITEM", ITEMNO);
						if (StrUtils.fString(ORDERNO).length() > 0)     ht.put("a.PONO", ORDERNO);
						if(StrUtils.fString(GRNO).length() > 0)  ht.put("a.GRNO",GRNO);
						if (StrUtils.fString(SUPPLIER).length() > 0)    ht.put("a.CNAME", SUPPLIER);
						if (StrUtils.fString(TO_ASSIGNEE).length() > 0) ht.put("a.CNAME_TO", TO_ASSIGNEE);
						if (StrUtils.fString(LOANASSIGNEE).length() > 0)ht.put("a.CNAME_LOAN", LOANASSIGNEE);
						if (StrUtils.fString(ORDERTYPE).length() > 0)   ht.put("ORDERTYPE", ORDERTYPE);
						if (StrUtils.fString(BATCH).length() > 0)       ht.put("a.BATCH", BATCH);
						if (StrUtils.fString(LOC).length() > 0)         ht.put("a.LOC", LOC);
						if(StrUtils.fString(LOC_TYPE_ID).length() > 0)  ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
						if(StrUtils.fString(PRD_TYPE_ID).length() > 0)  ht.put("ITEMTYPE",PRD_TYPE_ID);
				        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
				        if(StrUtils.fString(PRD_CLS_ID).length() > 0)   ht.put("PRD_CLS_ID",PRD_CLS_ID);
				        if(StrUtils.fString(PRD_DEPT_ID).length() > 0)   ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
				        if(StrUtils.fString(REASONCODE).length() > 0)   ht.put("a.REMARK",REASONCODE);
				        if(StrUtils.fString(FILTER).length() > 0)   ht.put("FILTER",FILTER);
				        if(StrUtils.fString(LOC_TYPE_ID2).length() > 0)  ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
					    //if (StrUtils.fString(INVOICENUM).length() > 0) ht.put("a.INVOICENO", INVOICENUM);
				        taxby=_PlantMstDAO.getTaxBy(PLANT);
				         if(LOCALEXPENSES.equalsIgnoreCase("1"))
				        {
				        	
				        	 if(taxby.equalsIgnoreCase("BYORDER"))
								{
						       	    QryList = reptUtil.getGoodsRecieveSummaryListlocalexpenses_new(ht, fdate,tdate, DIRTYPE, PLANT,PRD_DESCRIP); 
								}else
								{
									QryList = reptUtil.getGoodsRecieveSummaryListByProductGstlocalexpenses(ht, fdate,tdate, DIRTYPE, PLANT,PRD_DESCRIP); 	
								}
				        }else
				        {
				        if(taxby.equalsIgnoreCase("BYORDER"))
						{
				       	    QryList = reptUtil.getGoodsRecieveSummaryList_new(ht, fdate,tdate, DIRTYPE, PLANT,PRD_DESCRIP); 
						}else
						{
							QryList = reptUtil.getGoodsRecieveSummaryListByProductGst(ht, fdate,tdate, DIRTYPE, PLANT,PRD_DESCRIP); 	
						}
				        }
					
				        
			            if (QryList.size() > 0) {
		            int iIndex = 0,Index = 0;
		             int irow = 0;
		             float gstpercentage=0;
		            double sumprdQty = 0;String lastProduct="";
		            double totalRecvCost=0,recvQty=0,totalRecvQty=0,costGrandTot=0,recvQtyGrandTot=0;
		           	double totaltax=0,totalRecvCostWTax=0,taxGrandTot=0,costWTaxGrandTot=0;
		           
		                for (int iCnt =0; iCnt<QryList.size(); iCnt++){
		                            String result="";
		                            Map lineArr = (Map) QryList.get(iCnt);
		                            String itemCode =(String)lineArr.get("item");
		                            String pono = (String)lineArr.get("pono");
		                            
		                            recvQty = Double.parseDouble((String)lineArr.get("recqty"));
		                            double recvCost = Double.parseDouble((String)lineArr.get("total"));
									//recvCost = StrUtils.RoundDB(recvCost,2);
									
									String gst = (String) lineArr.get("Tax");
		            	      		if(gst.length()>0)
		            	      		{
		            	      			gstpercentage =  Float.parseFloat(gst) ;
		            	      		}
//		            	      		 double tax = Double.parseDouble((String)lineArr.get("taxval"));
		            	      		 
									   double tax =0.0;
			              	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
			              	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
			              	           if(taxid != 0){
			              	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
			              	        		   tax = (recvCost*gstpercentage)/100;
			              	        	   } else 
			              	        		 gstpercentage=Float.parseFloat("0.000");
			              	        	   
			              	           }else 
			              	        		 gstpercentage=Float.parseFloat("0.000");
			              	           
			              	           
//			              	         double tax =0.0;
//									   String taxids = (String)lineArr.get("Tax");
//									   String[] parts = taxids.split(".0");
//									   String part1 = parts[0]; // 004
//									   int taxid=Integer.parseInt(part1);  
////			              	           int taxid = Integer.parseInt(((String)taxids.toString()));
//			              	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
//			              	           if(taxid != 0){
//			              	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
//			              	        		   tax = (recvCost*gstpercentage)/100;
//			              	        	   } else 
//			              	        		 gstpercentage=Float.parseFloat("0.000");
//			              	        	   
//			              	           }else 
//			              	        		 gstpercentage=Float.parseFloat("0.000");
			            	      	//double tax = (recvCost*gstpercentage)/100;
		            		        double recvCostwTax = recvCost+tax;
		            		     
									
		                            //total cost details
									totalRecvCost=totalRecvCost + Double.parseDouble(((String)lineArr.get("total").toString()));
									totaltax  =totaltax + tax;
									totalRecvCostWTax = totalRecvCostWTax+recvCostwTax;
		            		       	totalRecvQty = totalRecvQty + Double.parseDouble(((String)lineArr.get("recqty").toString()));
		                            									
									//Grand total details
		                            costGrandTot = costGrandTot+Double.parseDouble((String)lineArr.get("total"));;
		                            taxGrandTot = taxGrandTot+tax;
		                            costWTaxGrandTot = costWTaxGrandTot+recvCostwTax;
		                            recvQtyGrandTot= recvQtyGrandTot+Double.parseDouble((String)lineArr.get("recqty"));
		                            
		                                 
		                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
		                            JSONObject resultJsonInt = new JSONObject();
		                            
		                            String unitCost = (String)lineArr.get("unitcost");
		                            String ordqty=(String)lineArr.get("ordqty");
		                            String rcvQty=(String)lineArr.get("recqty");
		                            String rcvCost = String.valueOf(recvCost);
		                            String gstpercentageValue = String.valueOf(gstpercentage);
		                            String taxValue = String.valueOf(tax);
		                            String recvCostwTaxValue=String.valueOf(recvCostwTax);
		                            
		                            
		                            float unitCostVal ="".equals(unitCost) ? 0.0f :  Float.parseFloat(unitCost);
		                            float ordqtyVal ="".equals(ordqty) ? 0.0f :  Float.parseFloat(ordqty);
		                            float rcvQtyVal ="".equals(rcvQty) ? 0.0f :  Float.parseFloat(rcvQty);
		                            float rcvCostVal ="".equals(rcvCost) ? 0.0f :  Float.parseFloat(rcvCost);
		                            float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
		                            float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
		                            float recvCostwTaxVal ="".equals(recvCostwTaxValue) ? 0.0f :  Float.parseFloat(recvCostwTaxValue);
		                            
		                            if(unitCostVal==0f){
		                            	unitCost="0.00000";
		                            }else{
		                            	unitCost=unitCost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }if(ordqtyVal==0f){
		                            	ordqty="0.000";
		                            }else{
		                            	ordqty=ordqty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }if(rcvQtyVal==0f){
		                            	rcvQty="0.000";
		                            }else{
		                            	rcvQty=rcvQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }if(rcvCostVal==0f){
		                            	rcvCost="0.00000";
		                            }else{
		                            	rcvCost=rcvCost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }if(gstpercentageVal==0f){
		                            	gstpercentageValue="0.00000";
		                            }else{
		                            	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }if(taxVal==0f){
		                            	taxValue="0.00000";
		                            }else{
		                            	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }if(recvCostwTaxVal==0f){
		                            	recvCostwTaxValue="0.00000";
		                            }else{
		                            	recvCostwTaxValue=recvCostwTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }

		                            
		                            String currencyid = (String)lineArr.get("CURRENCYID");
		                            double currencyseqt = Double.valueOf((String)lineArr.get("CURRENCYUSEQT"));
		                            double rcvCostconv = 0,recvCostwTaxConv=0,taxValcon=0;
		                            
		                            rcvCostconv = rcvCostVal * currencyseqt; 
		                            String SrcvCostconv= String.valueOf(rcvCostconv);
//		                            currencyid = "";
		                            if(currencyid==null)currencyid="";
		                            SrcvCostconv = currencyid + String.format("%.2f", rcvCostconv);
		                            
		                            recvCostwTaxConv = recvCostwTaxVal * currencyseqt; 
		                            String SrecvCostwTaxconv = currencyid + String.format("%.2f", recvCostwTaxConv);
		                            String exchangerate = String.format("%.2f", currencyseqt);
		                            
		                            taxValcon = taxVal  * currencyseqt; 
		                            String StaxValcon = currencyid + String.format("%.2f", taxValcon);
		                            
		                            double unitCostConv = Double.valueOf(unitCost)  * currencyseqt;
		                            String SnitCostConv = currencyid + String.format("%.2f", unitCostConv);
		                            
		                       
		                  //   if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(itemCode))){
		                    	 Index = Index + 1;
		                    	
		                         
		                            resultJsonInt.put("pono",StrUtils.fString((String)lineArr.get("pono")));
		                            resultJsonInt.put("grno",StrUtils.fString((String)lineArr.get("grno")));
	                             resultJsonInt.put("transactiondate", StrUtils.fString((String)lineArr.get("transactiondate")));
	                             resultJsonInt.put("cname", StrUtils.fString((String)lineArr.get("cname")));
	                             resultJsonInt.put("item", StrUtils.fString((String)lineArr.get("item")));
	                             resultJsonInt.put("itemdesc", StrUtils.fString((String)lineArr.get("itemdesc")));
	                             resultJsonInt.put("loc", StrUtils.fString((String)lineArr.get("loc")));
	                             resultJsonInt.put("batch", StrUtils.fString((String)lineArr.get("batch")));
	                             resultJsonInt.put("expiredate", StrUtils.fString((String)lineArr.get("expiredate")));
	                             resultJsonInt.put("ordqty",ordqty);
	                             resultJsonInt.put("recqty", rcvQty);
	                             resultJsonInt.put("uom", StrUtils.fString((String)lineArr.get("uom")));
	                             resultJsonInt.put("unitCost", unitCost);
	                             resultJsonInt.put("unitCostConv", SnitCostConv);
//	                             resultJsonInt.put("gstpercentage", gstpercentageValue );
	                             resultJsonInt.put("gstpercentage", strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) gstpercentageValue)), DbBean.NOOFDECIMALPTSFORWEIGHT));
	                             resultJsonInt.put("recvCost", rcvCost);
	                             resultJsonInt.put("recvCostwTax", recvCostwTaxValue);
	                             resultJsonInt.put("tax", taxValue);
	                             resultJsonInt.put("users", StrUtils.fString((String)lineArr.get("users")));
	                             resultJsonInt.put("remark", StrUtils.fString((String)lineArr.get("remark")));
	                             
	                             resultJsonInt.put("recvConvCost", SrcvCostconv);
	                             resultJsonInt.put("recvCostConvwTax", SrecvCostwTaxconv);
	                             resultJsonInt.put("currencyid", currencyid);
	                             resultJsonInt.put("exchangerate", exchangerate);
	                             resultJsonInt.put("taxcon", StaxValcon);
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
	
     


	@SuppressWarnings("unchecked")
	private JSONObject validateLocation(String plant, String userId,
			String locationId) {
		JSONObject resultJson = new JSONObject();
		try {

			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
			String extraConforUserLoc = userLocUtil.getUserLocAssigned(locationId,
					plant, "LOC");
			LocMstDAO locMstDAO = new LocMstDAO();
			locMstDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.LOC, locationId);
			if (locMstDAO.isExisit(ht,
					"  ISACTIVE ='Y' AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%'"
							+ extraConforUserLoc)) {
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

	@SuppressWarnings("unchecked")
	private JSONObject loadInboundOrderDetails(String plant,
			String inbounOrderNo) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONObject resultJsonObject = new JSONObject();
			PoHdrDAO poHdrDAO = new PoHdrDAO();
			poHdrDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.PONO, inbounOrderNo);
			Map resultMap = poHdrDAO.selectRow("custName,jobNum", ht);
			if (resultMap.size() > 0) {

				resultJsonObject.put("CUSTNAME", resultMap.get("custName"));
				resultJsonObject.put("JOBNUM", resultMap.get("jobNum"));
				resultJson.put("result", resultJsonObject);
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

 private JSONObject getInboundDetailsToPrint(HttpServletRequest request) {
		        JSONObject resultJson = new JSONObject();
		        JSONArray jsonArray = new JSONArray();
		        JSONArray jsonArrayErr = new JSONArray();
		        HTReportUtil movHisUtil       = new HTReportUtil();
		        DateUtils _dateUtils = new DateUtils();
		        ArrayList movQryList  = new ArrayList();
		        DecimalFormat decformat = new DecimalFormat("#,##0.00");
		        StrUtils strUtils = new StrUtils();
		        String fdate="",tdate="";
		           try {
		            String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
		           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
		           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
		           String  SUPPLIERNAME = StrUtils.fString(request.getParameter("SUPPLIERNAME"));
		           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
		           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
		           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
		           String  RECEIVESTATUS = StrUtils.fString(request.getParameter("RECEIVESTATUS"));
		           String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
		           String TYPE = StrUtils.fString(request.getParameter("TYPE"));
		           String GRNO = StrUtils.fString(request.getParameter("GRNO"));
		           String INVOICE = StrUtils.fString(request.getParameter("INVOICE"));
		           String SUPPLIERTYPE=StrUtils.fString(request.getParameter("SUPPLIERTYPE"));
		           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
		           String curDate =DateUtils.getDate();
		           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
		           if (FROM_DATE.length()>5)
		            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
    	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
		           if (TO_DATE.length()>5)
		           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           
		           Hashtable ht = new Hashtable();
		           if(StrUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
		           if(StrUtils.fString(ORDERNO).length() > 0)      ht.put("PONO",ORDERNO);
		           if(StrUtils.fString(GRNO).length() > 0)		   ht.put("GRNO", GRNO);
		           if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
		           if(StrUtils.fString(RECEIVESTATUS).length() > 0)   ht.put("STATUS", RECEIVESTATUS);
		           if(StrUtils.fString(statusID).length() > 0) ht.put("STATUS_ID",statusID);
		           if(StrUtils.fString(SUPPLIERTYPE).length() > 0) ht.put("SUPPLIERTYPE",SUPPLIERTYPE);    		         
		           movQryList = movHisUtil.getPOSummaryToPrint(ht,fdate,tdate,DIRTYPE,PLANT, SUPPLIERNAME,TYPE);
		                         
		            if (movQryList.size() > 0) {
		            int iIndex = 0,Index = 0;
		             int irow = 0;
		            double sumprdQty = 0;String lastProduct="";
		            double totalOrdPrice=0,totaltax=0,totOrdPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
		            double unitprice=0;
		                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
		                      
		                        String result="";
		                        Map lineArr = (Map) movQryList.get(iCnt);
		                        String custcode =(String)lineArr.get("custcode");
		                        String pono = (String)lineArr.get("pono");
		                         
		                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
		                        JSONObject resultJsonInt = new JSONObject();
		                       
		                        String qtyOrValue =(String)lineArr.get("qtyor");
		               			String qtyValue =(String)lineArr.get("qty");
		               			
		                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
		                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
		                        
		                        
		                        if(qtyOrVal==0f){
		                        	qtyOrValue="0.000";
		                        }else{
		                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                        }if(qtyVal==0f){
		                        	qtyValue="0.000";
		                        }else{
		                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                        }
		                        
		                        	Index = Index + 1;
		                             irow=irow+1;
		                             iIndex=iIndex+1;
		                             //resultJsonInt.put("INBOUNDDETAILS", result);
		                             resultJsonInt.put("PONO", pono);
		                             resultJsonInt.put("INVOICE", INVOICE);
		                             resultJsonInt.put("FROMDATE", fdate);
		                             resultJsonInt.put("TODATE", tdate);
		                             resultJsonInt.put("ORDERTYPE", StrUtils.fString((String)lineArr.get("ordertype")));
		                             resultJsonInt.put("JOBNUM", StrUtils.fString((String)lineArr.get("jobnum")));
		                             resultJsonInt.put("CUSTNAME", StrUtils.fString((String)lineArr.get("custname")));
		                             resultJsonInt.put("QTYOR", qtyOrValue);
		                             resultJsonInt.put("QTY", qtyValue);
		                             resultJsonInt.put("STATUS", StrUtils.fString((String)lineArr.get("status")));
		                             resultJsonInt.put("STATUS_ID", StrUtils.fString((String)lineArr.get("status_id")));
		                             
		                             jsonArray.add(resultJsonInt);
		                 

		                }
		               
		                    resultJson.put("items", jsonArray);
		                    JSONObject resultJsonIntObj = new JSONObject();
		                    resultJsonIntObj.put("ERROR_MESSAGE", "NO ERRORS!");
		                    resultJsonIntObj.put("ERROR_CODE", "100");
		                    jsonArrayErr.add(resultJsonIntObj);
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

     private JSONObject getinboundsmryDetailsRecv(HttpServletRequest request) {
		        JSONObject resultJson = new JSONObject();
		        JSONArray jsonArray = new JSONArray();
		        JSONArray jsonArrayErr = new JSONArray();
		        HTReportUtil movHisUtil       = new HTReportUtil();
		        DateUtils _dateUtils = new DateUtils();
		        ArrayList movQryList  = new ArrayList();
		        
		        DecimalFormat decformat = new DecimalFormat("#,##0.00");
		       
		        StrUtils strUtils = new StrUtils();
		        String fdate="",tdate="";
		        try {
		        
		           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		           String ITEMNO    = StrUtils.fString(request.getParameter("ITEM"));
		           String  PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
		           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
		           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
		           String  DIRTYPE = StrUtils.fString(request.getParameter("DTYPE"));
		           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
		           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
		           String  JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
		           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
		           String  UOM = StrUtils.fString(request.getParameter("UOM"));
		           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
		           //Start code added by Bruhan for product brand,type on 2/sep/13
		           String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
		           String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
		           String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
		           String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
		           //End code added by Bruhan for product brand,type on 2/sep/13 
		           String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
		           String SUPPLIERTYPE=StrUtils.fString(request.getParameter("SUPPLIERTYPE")); //Razeen
		           //String VIEWSTATUS = StrUtils.fString(request.getParameter("VIEWSTATUS"));
		           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
		           String curDate =DateUtils.getDate();
		           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

		           if (FROM_DATE.length()>5)
		            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
	               if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
		           if (TO_DATE.length()>5)
		           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	               Hashtable ht = new Hashtable();
		            if(StrUtils.fString(JOBNO).length() > 0)         ht.put("JOBNUM",JOBNO);
			        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("ITEM",ITEMNO);
			        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
			        if(StrUtils.fString(STATUS).length() > 0)        ht.put("STATUS",STATUS);
			        if(StrUtils.fString(ORDERTYPE).length() > 0)     ht.put("ORDERTYPE",ORDERTYPE);
			        if(StrUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
			        if(StrUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
			        if(StrUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);
			        if(StrUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
			        if(StrUtils.fString(statusID).length() > 0) ht.put("STATUS_ID",statusID);
			        if(StrUtils.fString(SUPPLIERTYPE).length() > 0) ht.put("SUPPLIERTYPE",SUPPLIERTYPE);
			        movQryList = movHisUtil.getReportIBSummaryDetailsByCost(ht,fdate,tdate,DIRTYPE,PLANT, PRD_DESCRIP,CUSTOMER);
			       if (movQryList.size() > 0) {
		            int iIndex = 0,Index = 0;
		            int irow = 0;
		            double sumprdQty = 0;String lastProduct="";
		            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
		           
		                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
		                            String result="";
		                            Map lineArr = (Map) movQryList.get(iCnt);
		                            String custcode =(String)lineArr.get("custname");
		                            String pono = (String)lineArr.get("pono");
		                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
		                            JSONObject resultJsonInt = new JSONObject();
		                            
		                            String qtyOrValue =(String)lineArr.get("qtyor");
		                   			String qtyValue =(String)lineArr.get("qty");
		                   			
		                            float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
		                            float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
		                            
		                            if(qtyOrVal==0f){
		                            	qtyOrValue="0.000";
		                            }else{
		                            	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }if(qtyVal==0f){
		                            	qtyValue="0.000";
		                            }else{
		                            	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }
		                       
		                      // if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
		                    	 Index = Index + 1;
		                    	 resultJsonInt.put("Index",(Index));
		                    	 resultJsonInt.put("pono",(pono));
		                    	 resultJsonInt.put("ordertype",StrUtils.fString((String)lineArr.get("ordertype")));
		                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("custname")));
		                    	 resultJsonInt.put("remarks",StrUtils.fString((String)lineArr.get("remarks")));
		                    	 resultJsonInt.put("remarks3",StrUtils.fString((String)lineArr.get("remarks3")));
		                    	 resultJsonInt.put("item",StrUtils.fString((String)lineArr.get("item")));
		                    	 resultJsonInt.put("itemdesc",StrUtils.fString((String)lineArr.get("itemdesc")));
		                    	 resultJsonInt.put("RECVDATE",StrUtils.fString((String)lineArr.get("RECVDATE")));
		                    	 resultJsonInt.put("UOM",StrUtils.fString((String)lineArr.get("UOM")));
		                    	 resultJsonInt.put("qtyor",qtyOrValue);
		                    	 resultJsonInt.put("qty",qtyValue);
		                    	 resultJsonInt.put("status_id",StrUtils.fString((String)lineArr.get("status_id")));
		                    	 resultJsonInt.put("users",StrUtils.fString((String)lineArr.get("users")));
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
	
	private JSONObject generateGRN(String plant, String userId) {
		JSONObject resultJson = new JSONObject();
		try {
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			String grno = _TblControlDAO.getNextOrder(plant,userId,"GRN");

			if (grno.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("grno", grno);
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
	
	private JSONObject checkGRN(String plant, String grno) {
		JSONObject resultJson = new JSONObject();
		try {
			BillDAO billDAO = new BillDAO();
			boolean gcheck = billDAO.isGRNOexisit(plant, grno);
			if(gcheck) {
				/*boolean gcheckpaid = billDAO.isGRNOPaid(plant, grno);
				if(gcheckpaid) {
					resultJson.put("status", "100");
					JSONObject resultObjectJson = new JSONObject();
					resultObjectJson.put("grno", grno);
					resultJson.put("result", resultObjectJson);
				}else {
					resultJson.put("status", "99");
				}*/
				resultJson.put("status", "99");
			}else {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("grno", grno);
				resultJson.put("result", resultObjectJson);
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject getRemarksDetails(HttpServletRequest request) {
		StrUtils _StrUtils = null;
		String action="";
		JSONObject resultJson = new JSONObject();
		  JSONArray jsonArray = new JSONArray();
		try {

			action = _StrUtils.fString(request.getParameter("Submit")).trim();
			_StrUtils = new StrUtils();
			PoDetDAO _poDetDAO = new PoDetDAO();
			String plant = StrUtils.fString(
					   (String) request.getSession().getAttribute("PLANT")).trim();

				String remarks1 = _StrUtils.fString(request.getParameter("REMARKS1"));
				String remarks2 = _StrUtils.fString(request.getParameter("REMARKS2"));
				String pono = _StrUtils.fString(request.getParameter("PONO"));
				String item = _StrUtils.fString(request.getParameter("ITEM"));
				String polnno = _StrUtils.fString(request.getParameter("POLNNO"));
				List al = new ArrayList();
				List al1 = new ArrayList();

				if (pono.length() > 0) {

					String query = "REMARKS,POLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PODET_PONUM, pono);
					ht.put(IDBConstants.PODET_ITEM, item);
					ht.put(IDBConstants.PODET_POLNNO, polnno);
					ht.put(IDBConstants.PLANT, plant);
					al = _poDetDAO.selectRemarks(query, ht);	

					String query1 = "Remark1,REMARK3";
					Hashtable ht1 = new Hashtable();
					ht1.put(IDBConstants.PODET_PONUM, pono);
					ht1.put(IDBConstants.PLANT, plant);
					al1 =new PoHdrDAO().selectPoHdr(query1, ht1);
					if(al1.size()>0)
					{
						Map m = (Map) al1.get(0);
						remarks1=StrUtils.fString((String)m.get("Remark1"));
						remarks2=StrUtils.fString((String)m.get("REMARK3"));
					}
					
					if(al.size()>0) {
					for (int i = 0; i < al.size(); i++) {
						Map m = (Map) al.get(i);
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("REMARKS", StrUtils.fString((String)m.get("REMARKS")));                 
	        			resultJsonInt.put("REMARK1", remarks1);                 
	        			resultJsonInt.put("REMARK2", remarks2);                 
	                    jsonArray.add(resultJsonInt);
					}
					}
					else {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("REMARKS", "");                 
						resultJsonInt.put("REMARK1", remarks1);                 
						resultJsonInt.put("REMARK2", remarks2);                 
						jsonArray.add(resultJsonInt);
					}
					
   }	
				else {
	        		
	             	   JSONObject resultJsonInt = new JSONObject();
	                    jsonArray.add("");
	                    resultJson.put("footermaster", jsonArray);            
	        	}
	        	 resultJson.put("REMARKSMST", jsonArray);

   }
		catch (Exception he) {
			he.printStackTrace();
			System.out.println("Error in reterieving data");
		}
		return resultJson;
  }
   }