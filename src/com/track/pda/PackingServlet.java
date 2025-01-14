package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PackingDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.ESTUtil;
import com.track.db.util.PackingUtil;
import com.track.db.util.HTReportUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class PackingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.PackingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PackingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = -6013768540440713061L;
	private DOUtil _DOUtil = null;
	private PackingUtil packingUtil = null;
	private InvMstDAO _InvMstDAO = null;
	private String xmlStr = "";
	private String action = "";
	MovHisDAO mdao = null;
	DateUtils dateutils = new DateUtils();
	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_DOUtil = new DOUtil();
		_InvMstDAO = new InvMstDAO();
		packingUtil = new PackingUtil ();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		JSONObject jsonObjectResult = new JSONObject();
		
		
		

		try {
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
					.getParameter("PLANT")), StrUtils.fString(request
							.getParameter("LOGIN_USER")) + " PDA_USER"));
			_DOUtil.setmLogger(mLogger);
			_InvMstDAO.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("process_packingMaterial")) {
				String xmlStr = "";
				xmlStr = process_packingItem(request, response);
			}
			
			else if(action.equals("DELETE_PACKING")){
				xmlStr = "";
				xmlStr = DeletePacking(request, response);
				
		 }
		
			
			else if(action.equals("VIEW_PACKING_DETAILS")){
			    jsonObjectResult = this.getViewPackingDetails(request);	
				
				 
			 }
			
			 

			 else if (action.equals("VIEW_PACKING_SMRY")) {
		        jsonObjectResult = this.getPackingIssue(request);
		       
		    }
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String process_packingItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		String REF_NUM = "", ITEM_NUM = "";
		String LOGIN_USER = "";
		String ITEM_BATCH = "", ITEM_QTY = "", PALLET = "", ITEM_LOC = "";
		try {

			REF_NUM = StrUtils.fString(request.getParameter("REF_NUM"));

			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
			PALLET = StrUtils.fString(request.getParameter("PALLET"));

			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", "SIS");
			ht.put("REFNO", REF_NUM);
			ht.put("ITEM", ITEM_NUM);
			ht.put("USERFLD4", ITEM_BATCH);
			ht.put("LOC", ITEM_LOC);
			ht.put("QTY", ITEM_QTY);
			ht.put("PALLET", PALLET);
			ht.put("CRBY", LOGIN_USER);

			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", "SIS");
			htLocMst.put("item", ITEM_NUM);
			htLocMst.put("loc", ITEM_LOC);
			htLocMst.put("USERFLD4", ITEM_BATCH);

			boolean flag = _InvMstDAO.isExisit(htLocMst, "");
			// boolean flag = true;

			if (flag) {

				flag = _DOUtil.savePackingDetails(ht);

			} else {
				throw new Exception(" Not a Valid Product For Packing ");
			}
			if (flag) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product : " + ITEM_NUM
						+ " packed successfully!");
				// xmlStr =" Item Packed Successfully " ;
			} else {
				xmlStr = XMLUtils.getXMLMessage(0,
						"Error in packing Product : " + ITEM_NUM);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
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
	private String DeletePacking(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String PACKING = "", ID="",result="",fieldDescData="",rflag="";
		PackingDAO PackingDAO = new PackingDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					PACKING = itemArray[1];
					  System.out.println(" PACKING"+ PACKING);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("PACKINGID",ID);
					ht.put(IDBConstants.PACKING,PACKING);
					
					/*mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
		//			htm.put("DIRTYPE", TransactionConstants.DEL_PACKING);
		//			htm.put("ORDNUM","");
		//			htm.put("ITEM","");
					htm.put("PACKING",PACKING);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));*/
		            transactionHandler = PackingDAO.deletePacking(ht);
		/*							
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						*/
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","packing Deleted Successfully");
			response.sendRedirect("jsp/packingMasterSummary.jsp?action=result&PACKING="+PACKING);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting packing:"+PACKING);
			response.sendRedirect("jsp/packingMasterSummary.jsp?action=resulterror&PACKING="+PACKING);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("jsp/packingMasterSummary.jsp?action=catchrerror&PACKING="+PACKING);
			throw e;
		}

		return msg;
	}

	private JSONObject getViewPackingDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = packingUtil.getPackingDetails(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("PACKINGID")) ;
                            String packing = StrUtils.fString((String)lineArr.get("PACKING")) ;
                           	chkString =autoid+","+packing;
                           	
                           	
                              result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + packing + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("PACKINGMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("packingmaster", jsonArray);
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
                    resultJson.put("packingmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
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
           String  CUSTNAME = strUtils.fString(request.getParameter("CNAME"));
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
           //if(strUtils.fString(CUSTNAME).length() > 0)     ht.put("A.CUSTNAME",strUtils.InsertQuotes(CUSTNAME));
           if(strUtils.fString(PLNO).length() > 0)    ht.put("PLNO",PLNO);
           if(strUtils.fString(ISSUESTATUS).length() > 0)  ht.put("STATUS",ISSUESTATUS);
           if(strUtils.fString(PICKSTATUS).length() > 0)   ht.put("PickStaus",PICKSTATUS);
           if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("ITEMTYPE",PRD_TYPE_ID);
	        if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
	        if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("PRD_CLS_ID",PRD_CLS_ID);	        
	        if(strUtils.fString(CustName).length() > 0) ht.put("CustName",CustName);
	        if(strUtils.fString(INVOICENO).length() > 0) ht.put("INVOICENO",INVOICENO);
  
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
                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        JSONObject resultJsonInt = new JSONObject();
                                            
              			
              			 Index = Index + 1;
                       	
                        resultJsonInt.put("Index",Index);
                        resultJsonInt.put("INVOICENO", strUtils.fString((String)lineArr.get("INVOICENO")));
                        resultJsonInt.put("DONO", strUtils.fString((String)lineArr.get("DONO")));
                        resultJsonInt.put("plno",(plno));
                        resultJsonInt.put("dnno", strUtils.fString((String)lineArr.get("DNNO")));
                        resultJsonInt.put("TOTALNETWEIGHT", strUtils.fString((String)lineArr.get("TOTALNETWEIGHT")));
                        resultJsonInt.put("TOTALGROSSWEIGHT", strUtils.fString((String)lineArr.get("TOTALGROSSWEIGHT")));
                        resultJsonInt.put("NETPACKING", strUtils.fString((String)lineArr.get("NETPACKING")));
                        resultJsonInt.put("NETDIMENSION", strUtils.fString((String)lineArr.get("NETDIMENSION")));
                        resultJsonInt.put("remarks", strUtils.fString((String)lineArr.get("remarks")));
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
}
