package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MiscIssuingUtil;
import com.track.db.util.MiscReceivingUtil;
import com.track.db.util.POUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.service.ShopifyService;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
import com.track.db.util.TblControlUtil;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



//public class OrderReceivingByPOServlet extends HttpServlet implements SingleThreadModel {
public class PdaReceivingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.PdaReceivingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PdaReceivingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 270114335994094842L;
	private POUtil _POUtil = null;
	private InvMstDAO _InvMstDAO = null;
	private DateUtils dateUtils = null;
	private String PLANT = "";
	private String xmlStr = "";
	JSONObject jsonObjectResult = new JSONObject();
	JSONArray jsonArrayErr = new JSONArray();
	private RecvDetDAO _RecvDetDAO = null;
	private PlantMstDAO _PlantMstDAO = null;
	private ShipHisDAO _ShipHisDAO = null;
	private DOUtil _DOUtil = null;
	private MiscIssuingUtil _MiscIssuingUtil = null;
	private MiscReceivingUtil _MiscReceivingUtil = null;
	private InvMstUtil _InvMstUtil = null;
	private LocMstDAO _LocMstDAO = null;
	
	private String action = "";
	
	private static final String CONTENT_TYPE = "text/xml";

	StrUtils strUtils = new StrUtils();

	public void init() throws ServletException {
		_POUtil = new POUtil();
		_InvMstDAO = new InvMstDAO();
		dateUtils = new DateUtils();
		_RecvDetDAO = new RecvDetDAO();
		_ShipHisDAO = new ShipHisDAO();
		_PlantMstDAO = new PlantMstDAO();
		_DOUtil = new DOUtil();
		_MiscReceivingUtil = new MiscReceivingUtil();
		_MiscIssuingUtil = new MiscIssuingUtil();
		_InvMstUtil = new InvMstUtil();
		 _LocMstDAO = new LocMstDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_POUtil.setmLogger(mLogger);
			_DOUtil.setmLogger(mLogger);
			_MiscReceivingUtil.setmLogger(mLogger);
			_MiscIssuingUtil.setmLogger(mLogger);
			_InvMstDAO.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			action = request.getParameter("action").trim();

			
		 if (action.equalsIgnoreCase("process_receiveMaterial_random")) {

			jsonObjectResult = process_orderReceiving_random(request, response);
			//System.out.print(jsonObjectResult.toString());
			}
		else if (action.equalsIgnoreCase("process_do_picking_random")) {
		
			jsonObjectResult = process_doPicking_random(request, response);
		}
		else if (action.equalsIgnoreCase("process_receiveMaterial")) {
			jsonObjectResult = process_orderMiscReceiving(request, response);
		}
		else if (action.equalsIgnoreCase("process_issueMaterial")) {
		
			jsonObjectResult = process_orderIssuing(request, response);
		} 
		else if (action.equalsIgnoreCase("process_locationTransfer")) {
			jsonObjectResult = process_locationTransfer(request, response);
			System.out.print(jsonObjectResult.toString());
		}  
		else if (action.equalsIgnoreCase("process_do_picking_random_bypda")) {
			jsonObjectResult = process_doPicking_random_bypda(request, response);
		}
		else if (action.equalsIgnoreCase("process_do_issue_random_bypda")) {
			jsonObjectResult = process_doissue_random_bypda(request, response);
			System.out.print(jsonObjectResult.toString());
		}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			/*xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());*/
			
			jsonObjectResult.put("message","Unable to process? "+e.getMessage());
			jsonObjectResult.put("status","0");
		}

		//out.write(jsonObjectResult.toString());
		
		//out.close();
		response.getWriter().write(jsonObjectResult.toString());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}


	private JSONObject process_orderReceiving_random(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException,
				Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
    //    String json_error = request.getParameter("error");
      //  out.print(json_str);
      
        JSONArray hostArray = JSONArray.fromObject(json_str);
        
       
   
       			Map receiveMaterial_HM = null;
			String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
			String ITEM_DESCRIPTION = "", LOGIN_USER = "",ISNONSTKFLG="";
			String ITEM_BATCH = "", ITEM_QTY = "", EXPIREDATE = "", ITEM_EXPDATE = "", ITEM_LOC = "",UOM="", 
			CUST_NAME = "", ITEM_REMARKS = "", REMARKS = "", ORD_QTY = "",GRNO="",EMPNO="";
			
			 for(int i = 0; i < hostArray.size(); i++)
		        {
		        JSONObject hostObject = hostArray.getJSONObject(i);
		         PLANT = hostObject.getString("companyCode");
		         LOGIN_USER = hostObject.getString("loginUser");
		         PO_NUM = hostObject.getString("pono");
		         ITEM_NUM = hostObject.getString("itemNum");
		         ITEM_DESCRIPTION = hostObject.getString("itemDesc");
		         ITEM_BATCH = hostObject.getString("itemBatch");
		         ITEM_QTY = hostObject.getString("itemQty");
		         ITEM_LOC = hostObject.getString("itemLoc");
		         UOM = hostObject.getString("uom");
		         EMPNO = hostObject.getString("employeeNo");
		         GRNO = hostObject.getString("grno");
		         EXPIREDATE = hostObject.getString("expiryDate");
		         
		        
		         
			try {
				UserLocUtil uslocUtil = new UserLocUtil();
				uslocUtil.setmLogger(mLogger);
				boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
						PLANT, LOGIN_USER, ITEM_LOC);
				if (!isvalidlocforUser) {
					throw new Exception(" Loc :" + ITEM_LOC
							+ " is not User Assigned Location");
				}
				DateUtils _dateUtils = new DateUtils();
				String curDate =_dateUtils.getDate();
				String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
				String strRecvDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
				
				ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);
				
			    double itemqty1 = Double.parseDouble(ITEM_QTY);
			    itemqty1 = StrUtils.RoundDB(itemqty1, IConstants.DECIMALPTS);
			    ITEM_QTY = String.valueOf(itemqty1);
			    ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
			 	receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
				//receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
						.getCustCode(PLANT, PO_NUM));
				receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
				receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
				//receiveMaterial_HM.put(IConstants.ORD_QTY, ORD_QTY);
				receiveMaterial_HM.put(IConstants.USERFLD1, ITEM_REMARKS);
				receiveMaterial_HM.put(IConstants.USERFLD2, "");
				receiveMaterial_HM.put(IConstants.INV_EXP_DATE, "");
				receiveMaterial_HM.put(IConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
				receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT,	PO_NUM));
				receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
				receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
				receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
				receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
				receiveMaterial_HM.put(IConstants.GRNO, GRNO);
				receiveMaterial_HM.put(IConstants.EMPNO, EMPNO);
				receiveMaterial_HM.put(IConstants.UNITMO, UOM);
				receiveMaterial_HM.put("LineNo",i);
				xmlStr = "";
				Hashtable htLocMst = new Hashtable();
				htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
				htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
				htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));
				boolean flag = true;
				if (flag) {
					if(!GRNO.isEmpty()){
					flag=_RecvDetDAO.isExisit("select count(*) from "+PLANT+"_RECVDET where PONO='"+PO_NUM+"' and GRNO='"+GRNO+"'");
					}
					else{
						 flag = false;
					}
					jsonObjectResult = _POUtil.process_ReceiveMaterial_Random_STD(receiveMaterial_HM);
					xmlStr = jsonObjectResult.toString();
					if(xmlStr.contains("received successfully")) //Shopify Inventory Update
					{
						Hashtable htCond = new Hashtable();
       					htCond.put(IConstants.PLANT, PLANT);
       					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
       						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
    						if(nonstkflag.equalsIgnoreCase("N")) {
       						String availqty ="0";
       						ArrayList invQryList = null;
       						htCond = new Hashtable();
       						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
       						if (invQryList.size() > 0) {					
       							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
       								String result="";
                                    Map lineArr = (Map) invQryList.get(iCnt);
                                    availqty = (String)lineArr.get("AVAILABLEQTY");
                                    System.out.println(availqty);
       							}
       							double availableqty = Double.parseDouble(availqty);
           						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
       						}	
    						}
       					}
					}
					
					if (!flag) {
						new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", GRNO);
						}
					//To GRN
					if(xmlStr.contains("received successfully"))
					{
						double unitprice=0,totalprice=0,totalqty=0;
						String priceval="";
						Hashtable htPoDet = new Hashtable();
						ArrayList PODetails = null;
						String queryPoDet = "(UNITCOST*CURRENCYUSEQT) as UNITCOST";
						htPoDet.put(IConstants.PODET_PONUM, PO_NUM);
						htPoDet.put(IConstants.PLANT, PLANT);
						htPoDet.put(IConstants.ITEM, ITEM_NUM);
						htPoDet.put(IConstants.UNITMO, UOM);
						PODetails = _POUtil.getPoDetDetails(queryPoDet, htPoDet);
							if(PODetails.size() > 0) {	
												Map map1 = (Map) PODetails.get(0);
													unitprice = Double.parseDouble((String) map1.get("UNITCOST"));
													PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
													StrUtils strUtils     = new StrUtils();
													String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
													totalprice=totalprice+(unitprice* Float.parseFloat(ITEM_QTY));
													priceval=String.valueOf(totalprice);
													double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
													priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
							}
							//insert GRNTOBILL
							RecvDetDAO _RecvDetDAO = new RecvDetDAO();
							DateUtils dateutils = new DateUtils();
							Hashtable htRecvDet = new Hashtable();
							htRecvDet.clear();
							htRecvDet.put(IConstants.PLANT, PLANT);
							htRecvDet.put(IConstants.VENDOR_CODE, _POUtil
									.getCustCode(PLANT, PO_NUM));
							htRecvDet.put(IConstants.GRNO, GRNO);                    
							htRecvDet.put(IConstants.PODET_PONUM, PO_NUM);
							htRecvDet.put(IConstants.STATUS, "NOT BILLED");
							htRecvDet.put(IConstants.AMOUNT, priceval);
							htRecvDet.put(IConstants.QTY, String.valueOf(ITEM_QTY));
							htRecvDet.put("CRAT",dateutils.getDateTime());
							htRecvDet.put("CRBY",LOGIN_USER);
							htRecvDet.put("UPAT",dateutils.getDateTime());
							
							//insert MovHis
		                    MovHisDAO movHisDao = new MovHisDAO();
		            		movHisDao.setmLogger(mLogger);
		        			Hashtable htRecvHis = new Hashtable();
		        			htRecvHis.clear();
		        			htRecvHis.put(IDBConstants.PLANT, PLANT);
		        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
		        			htRecvHis.put(IDBConstants.ITEM, "");
		        			htRecvHis.put("MOVTID", "");
		        			htRecvHis.put("RECID", "");
		        			htRecvHis.put(IConstants.QTY, String.valueOf(ITEM_QTY));
		        			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
		        			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
		        			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, GRNO);
		        			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							
							flag=_RecvDetDAO.isExisit("select count(*) from "+PLANT+"_FINGRNOTOBILL where PONO='"+PO_NUM+"' and GRNO='"+GRNO+"'");
							
							if (!flag) {
								flag = _RecvDetDAO.insertGRNtoBill(htRecvDet);
								flag = movHisDao.insertIntoMovHis(htRecvHis);
							} 
							else
							{								
								htRecvDet = new Hashtable();
								htRecvDet.clear();
								htRecvDet.put(IConstants.PLANT, PLANT);
								htRecvDet.put(IConstants.GRNO, GRNO);
								htRecvDet.put(IConstants.PODET_PONUM, PO_NUM);
								
								htRecvHis = new Hashtable();
			        			htRecvHis.clear();
			        			htRecvHis.put(IDBConstants.PLANT, PLANT);
			        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
			        			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
			        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, GRNO);
			        			
			        			flag=_RecvDetDAO.updateGRNtoBill(" set AMOUNT+="+priceval+",QTY+="+ITEM_QTY,htRecvDet,"",PLANT);
								flag=movHisDao.updateMovHis(" set QTY+="+ITEM_QTY,htRecvHis,"",PLANT);
							}
						
					}
					else{
						break;
					}
					
				} else {
					throw new Exception(" Product Received already ");
				}

			} catch (Exception e) {
				MLogger.log(0, "" + e.getMessage());
				throw e;
			}
	}
			return jsonObjectResult;
		}
	private JSONObject process_doPicking_random(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
     
      
        JSONArray hostArray = JSONArray.fromObject(json_str);
        
		Map issMat_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",CONTAINER="",REMARK;
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ORD_QTY = "", INV_QTY = "", 
				PICKING_QTY = "",ISNONSTKFLG="",INVID="",INVOICENO="",EMPNO="",UOM="",creditLimit="",creditBy="";
		for(int i = 0; i < hostArray.size(); i++)
        {
			JSONObject hostObject = hostArray.getJSONObject(i);
			PLANT = hostObject.getString("companyCode");
			PO_NUM = hostObject.getString("dono");
			ITEM_NUM = hostObject.getString("item");
			ITEM_DESCRIPTION = new ItemMstDAO().getItemDesc(PLANT,ITEM_NUM);
			LOGIN_USER = hostObject.getString("loginUser");
			CUST_NAME = hostObject.getString("customer");
			ITEM_BATCH = hostObject.getString("batchno");
			PICKING_QTY = hostObject.getString("qty");
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
		//	double pickingqty = Double.parseDouble(PICKING_QTY);
			/*pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);*/
			ITEM_LOC = hostObject.getString("loc");
			CONTAINER= hostObject.getString("container");
			CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
			INVOICENO=hostObject.getString("invoice");
			EMPNO=hostObject.getString("employeeno");
			UOM=hostObject.getString("uom");
			REMARK=hostObject.getString("remarks");
		
        
		try {
/*			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = new ItemMstDAO().getItemDesc(PLANT,ITEM_NUM);
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			CONTAINER= StrUtils.fString(request.getParameter("CONTAINER"));
			CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
			REMARK=StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			REMARK= StrUtils.replaceCharacters2Recv(REMARK);
			INVID=StrUtils.fString(request.getParameter("INVID"));
			INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));*/
			
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           /* UserLocUtil uslocUtil = new UserLocUtil();
            uslocUtil.setmLogger(mLogger);
            boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
            if (!isvalidlocforUser) {
                          throw new Exception(" Loc :"+ITEM_LOC+" is not a User Assigned Location/Valid Location to Pick");
             }*/
            
            DateUtils _dateUtils = new DateUtils();
            String curDate =_dateUtils.getDate();
            String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
            String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
            issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			issMat_HM.put(IConstants.PODET_PONUM, PO_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.QTY, PICKING_QTY);
			issMat_HM.put("TRAN_QTY", PICKING_QTY);
			issMat_HM.put(IConstants.INV_EXP_DATE, "");
			issMat_HM.put(IConstants.OUT_CONTAINER, CONTAINER);
			issMat_HM.put(IConstants.OUT_REMARK1, REMARK);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			//issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.INVID, INVID);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);
			issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put(IConstants.BATCH_ID, INVID);
			issMat_HM.put(IConstants.UNITMO, UOM);
			issMat_HM.put("LineNo",i);
			CustUtil custUtil = new CustUtil();
			ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, PO_NUM),PLANT);
			creditLimit   = (String)arrCust.get(24);
			creditBy   = (String)arrCust.get(35);
			
			issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
			issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
			issMat_HM.put(IConstants.BASE_CURRENCY, _PlantMstDAO.getBaseCurrency(PLANT));
			
			xmlStr = "";

			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, ITEM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, ITEM_BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			double invumqty = Double.valueOf(PICKING_QTY) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;
			if(!ISNONSTKFLG.equalsIgnoreCase("Y"))
			{
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					jsonObjectResult.put("message","Not Enough Inventory For Product : " + ITEM_NUM);
					jsonObjectResult.put("status","0");
					jsonObjectResult.put("LineNo",issMat_HM.get("LineNo").toString());
					
				}
			}
			if (flag) {
				flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_SHIPHIS where DONO='"+PO_NUM+"' and INVOICENO='"+INVOICENO+"'");
				jsonObjectResult = _DOUtil.process_DoPickingForPDA_Random_STD(issMat_HM);
				
				xmlStr = jsonObjectResult.toString();
				if(xmlStr.contains("picked successfully")) //Shopify Inventory Update
				{
					Hashtable htCond = new Hashtable();
   					htCond.put(IConstants.PLANT, PLANT);
   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
   						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
						if(nonstkflag.equalsIgnoreCase("N")) {
   						String availqty ="0";
   						ArrayList invQryList = null;
   						htCond = new Hashtable();
   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
   						if (invQryList.size() > 0) {					
   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
   								String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                availqty = (String)lineArr.get("AVAILABLEQTY");
                                System.out.println(availqty);
   							}
   							double availableqty = Double.parseDouble(availqty);
       						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
   						}	
						}
   					}
				}
				//To GI
				if(xmlStr.contains("picked successfully"))
				{
					if (!flag) {
						new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
						}
					DoDetDAO _DoDetDAO = new DoDetDAO();
					DateUtils dateutils = new DateUtils();
					Hashtable htRecvDet = new Hashtable();
					
					double unitprice=0,totalprice=0,totalqty=0;
					String priceval="";
					Hashtable htDoDet = new Hashtable();
					ArrayList DODetails = null;
					String queryDoDet = "(UNITPRICE*CURRENCYUSEQT) as UNITPRICE";
					htDoDet.put(IConstants.DODET_DONUM, PO_NUM);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.ITEM, ITEM_NUM);
					htDoDet.put(IConstants.UNITMO, UOM);
					DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
						if(DODetails.size() > 0) {	
											Map map1 = (Map) DODetails.get(0);
												unitprice = Double.parseDouble((String) map1.get("UNITPRICE"));
												PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
												StrUtils strUtils     = new StrUtils();
												totalprice=totalprice+(unitprice* Float.parseFloat(PICKING_QTY));
												priceval=String.valueOf(totalprice);
												double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
												priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
						}
						//Insert GINOTOINVOICE
						htRecvDet.clear();
						htRecvDet.put(IConstants.PLANT, PLANT);
						htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
						htRecvDet.put("GINO", INVOICENO);                    
						htRecvDet.put(IConstants.DODET_DONUM, PO_NUM);
						htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
						htRecvDet.put(IConstants.AMOUNT, priceval);
						htRecvDet.put(IConstants.QTY, String.valueOf(PICKING_QTY));
						htRecvDet.put("CRAT",dateutils.getDateTime());
						htRecvDet.put("CRBY",LOGIN_USER);
						htRecvDet.put("UPAT",dateutils.getDateTime());
						
		                //insert MovHis
		                MovHisDAO movHisDao = new MovHisDAO();
		        		movHisDao.setmLogger(mLogger);
		    			Hashtable htRecvHis = new Hashtable();
		    			htRecvHis.clear();
		    			htRecvHis.put(IDBConstants.PLANT, PLANT);
		    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
		    			htRecvHis.put(IDBConstants.ITEM, "");
		    			htRecvHis.put("MOVTID", "");
		    			htRecvHis.put("RECID", "");
		    			htRecvHis.put(IConstants.QTY, String.valueOf(PICKING_QTY));
		    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
		    			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
		    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
		    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						
						flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_FINGINOTOINVOICE where DONO='"+PO_NUM+"' and GINO='"+INVOICENO+"'");
						if (!flag) {
							flag = _DoDetDAO.insertGINOtoInvoice(htRecvDet);
							flag = movHisDao.insertIntoMovHis(htRecvHis);
						} 
						else{
							htRecvDet = new Hashtable();
							htRecvDet.clear();
							htRecvDet.put(IConstants.PLANT, PLANT);
							htRecvDet.put("GINO", INVOICENO);
							htRecvDet.put(IConstants.DODET_DONUM, PO_NUM);
							
							htRecvHis = new Hashtable();
							htRecvHis.clear();
		        			htRecvHis.put(IDBConstants.PLANT, PLANT);
		        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
		        			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
		        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
							
							flag=_DoDetDAO.updateGINOtoInvoice(" set AMOUNT+="+priceval+",QTY+="+PICKING_QTY,htRecvDet,"",PLANT);
							flag=movHisDao.updateMovHis(" set QTY+="+PICKING_QTY,htRecvHis,"",PLANT);
						}
						
				}
				else
				{
					break;
				}

			} else {
				throw new Exception(" Picking is not successfull ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
		return jsonObjectResult;
	}
	private JSONObject process_orderMiscReceiving(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
     
      
        JSONArray hostArray = JSONArray.fromObject(json_str);

		Map receiveMaterial_HM = null;
		RecvDetDAO _RecvDetDAO = new RecvDetDAO();
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String LOGIN_USER = "";
		String ITEM_BATCH = "", ITEM_QTY = "", EXPIREDATE = "", ITEM_EXPDATE = "", ITEM_LOC = "", RSNDESC = "",
				TRANSACTIONID = "", EMPNO = "", UOM = "",INVID="",REMARKS="";
		for(int i = 0; i < hostArray.size(); i++)
        {
			JSONObject hostObject = hostArray.getJSONObject(i);
			PLANT = hostObject.getString("companyCode");
			ITEM_NUM = hostObject.getString("itemNum");
			LOGIN_USER = hostObject.getString("loginUser");
			EXPIREDATE = hostObject.getString("expiryDate");
			ITEM_BATCH = hostObject.getString("itemBatch");
			ITEM_QTY = hostObject.getString("itemQty");
			REMARKS = hostObject.getString("remarks");
			ITEM_LOC =  hostObject.getString("itemLoc");
			RSNDESC =  hostObject.getString("reasoncode");
			TRANSACTIONID =  hostObject.getString("trxId");
			System.out.println("ordrenumber"+TRANSACTIONID);
			EMPNO =  hostObject.getString("employeeNo");
			UOM =  hostObject.getString("uom");
			RSNDESC = hostObject.getString("reasoncode");
			
		try {
			/*PLANT = StrUtils.fString(request.getParameter("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
			// ITEM_EXPDATE = StrUtils.fString(request.getParameter("ITEM_EXPDATE"));
			String REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			RSNDESC = StrUtils.fString(request.getParameter("RSNDESC"));

			TRANSACTIONID=StrUtils.fString(request.getParameter("ORDERNO"));
			System.out.println("ordrenumber"+TRANSACTIONID);
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));*/
			



			RSNDESC = StrUtils.replaceCharacters2Recv(RSNDESC);
			String nonstocktype = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			/*if (nonstocktype.equalsIgnoreCase("Y")) {
				throw new Exception(" " + ITEM_NUM + " is a non stock product, receiving is not allowed ");
			}*/

			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-" + ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}
			DateUtils _dateUtils = new DateUtils();
			String curDate = _dateUtils.getDate();
			String strTranDate = curDate.substring(6) + "-" + curDate.substring(3, 5) + "-" + curDate.substring(0, 2);
			String strRecvDate = curDate.substring(0, 2) + "/" + curDate.substring(3, 5) + "/" + curDate.substring(6);

			double itemqty = Double.parseDouble(ITEM_QTY);
			itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
			ITEM_QTY = String.valueOf(itemqty);
			ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.RSNDESC, RSNDESC);
			receiveMaterial_HM.put(IDBConstants.REMARKS, REMARKS);
			receiveMaterial_HM.put(IDBConstants.EXPIREDATE, EXPIREDATE);
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
			receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
			receiveMaterial_HM.put(IConstants.PONO, TRANSACTIONID);
			receiveMaterial_HM.put(IConstants.EMPNO, EMPNO);
			receiveMaterial_HM.put(IConstants.ISPDA, "ISPDA");
			receiveMaterial_HM.put(IConstants.UNITMO, UOM);
			receiveMaterial_HM.put(IConstants.NONSTKFLAG, nonstocktype);
			receiveMaterial_HM.put("LineNo",i);
			//Get Item Count
			
			POSDetDAO posdetDAO = new POSDetDAO();			
			posdetDAO.setmLogger(mLogger);
			List prdlist = posdetDAO.listProductsForPOSTranID(PLANT, TRANSACTIONID, " (TRANSTATUS <> '')");
			int gcount=prdlist.size()+1;
			receiveMaterial_HM.put(IConstants.DODET_DOLNNO, String.valueOf(gcount));

			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			receiveMaterial_HM.put("UOMQTY", UOMQTY);

		
			jsonObjectResult = _MiscReceivingUtil.process_MiscReceiveMaterial_STD(receiveMaterial_HM);
			
			xmlStr = jsonObjectResult.toString();
			if(xmlStr.contains("received successfully!")) //Shopify Inventory Update
			{
				Hashtable htCond = new Hashtable();
					htCond.put(IConstants.PLANT, PLANT);
					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
					if(nonstkflag.equalsIgnoreCase("N")) {
						String availqty ="0";
						ArrayList invQryList = null;
						htCond = new Hashtable();
						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
						if (invQryList.size() > 0) {					
							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
								String result="";
                            Map lineArr = (Map) invQryList.get(iCnt);
                            availqty = (String)lineArr.get("AVAILABLEQTY");
                            System.out.println(availqty);
							}
							double availableqty = Double.parseDouble(availqty);
   						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
						}	
					}
					}
			}
			if(xmlStr.contains("received successfully!"))
			{
				
				boolean flag = true;
				flag=_RecvDetDAO.isExisit("select count(*) from "+PLANT+"_FINGRNOTOBILL where  GRNO='"+TRANSACTIONID+"'");
				if (flag) {
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable htRecvDet = new Hashtable();
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.GRNO, TRANSACTIONID);
					
					
					flag=_RecvDetDAO.updateGRNtoBill(" set QTY+="+ITEM_QTY,htRecvDet,"",PLANT);
        			
					Hashtable htRecvHis = new Hashtable();
        			htRecvHis.clear();
        			htRecvHis.put(IDBConstants.PLANT, PLANT);
        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
        			        					
        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TRANSACTIONID);
					flag=movHisDao.updateMovHis(" set QTY+="+ITEM_QTY,htRecvHis,"",PLANT);
					
				}else{
			new TblControlUtil().updateTblControlIESeqNo(PLANT,"GRN","GN",TRANSACTIONID);
			DateUtils dateutils = new DateUtils();
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IConstants.PLANT, PLANT);
			htRecvDet.put(IConstants.VENDOR_CODE,"");
			htRecvDet.put(IConstants.GRNO, TRANSACTIONID);                    
			htRecvDet.put(IConstants.PODET_PONUM, "");
			htRecvDet.put(IConstants.STATUS, "NON BILLABLE");
			htRecvDet.put(IConstants.AMOUNT, "");
			htRecvDet.put(IConstants.QTY, ITEM_QTY);
			htRecvDet.put("CRAT",dateutils.getDateTime());
			htRecvDet.put("CRBY",LOGIN_USER);
			htRecvDet.put("UPAT",dateutils.getDateTime());
			 _RecvDetDAO.insertGRNtoBill(htRecvDet);
			   //insert MovHis
            MovHisDAO movHisDao = new MovHisDAO();
    		movHisDao.setmLogger(mLogger);
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, PLANT);
			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
			htRecvHis.put(IDBConstants.ITEM, "");
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IConstants.QTY, ITEM_QTY);
			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
			htRecvHis.put(IDBConstants.REMARKS, "");        					
			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TRANSACTIONID);
			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		    movHisDao.insertIntoMovHis(htRecvHis);
			}
			}
			else{
				break;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		//	xmlStr = XMLUtils.getXMLMessage(0, e.getMessage());
			jsonObjectResult.put("message",e.getMessage());
			jsonObjectResult.put("status",0);
			jsonObjectResult.put("LineNo",i);
		}
	}
		return jsonObjectResult;
	}
	private JSONObject process_orderIssuing(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
     
      
        JSONArray hostArray = JSONArray.fromObject(json_str);

		Map issMat_HM = null;
		String PLANT = "", DO_NUM = "", PO_LN_NUM = "", ITEM_NUM = "",ISNONSTKFLG="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",REMARKS="";
		String ITEM_QTY = "", ITEM_BATCH = "", ITEM_LOC = "", RSNDESC = "", USERID = "",TRANSACTIONID="",INVID="",EMPNO="", UOM = "";

		for(int i = 0; i < hostArray.size(); i++)
        {
			JSONObject hostObject = hostArray.getJSONObject(i);
			PLANT = hostObject.getString("companyCode");
			USERID  = hostObject.getString("loginUser");
			ITEM_NUM = hostObject.getString("itemNum");
			LOGIN_USER = hostObject.getString("loginUser");
			ITEM_QTY = hostObject.getString("issuedQty");
			ITEM_BATCH = hostObject.getString("itemBatch");
			ITEM_LOC =  hostObject.getString("itemLoc");
			RSNDESC = hostObject.getString("reasoncode");
			RSNDESC = StrUtils.replaceCharacters2Recv(RSNDESC);
			REMARKS = hostObject.getString("remarks");
			TRANSACTIONID =  hostObject.getString("trxId");
			INVID = hostObject.getString("invid");
			EMPNO =  hostObject.getString("employeeNo");
			UOM =  hostObject.getString("uom");
				
		try {
			/*PLANT = StrUtils.fString(request.getParameter("PLANT"));
			USERID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			RSNDESC = StrUtils.fString(request.getParameter("RSNDESC"));
			RSNDESC = StrUtils.replaceCharacters2Recv(RSNDESC);
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			TRANSACTIONID=StrUtils.fString(request.getParameter("ORDERNO"));
			INVID=StrUtils.fString(request.getParameter("INVID"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));*/
			
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
			double doqty = Double.parseDouble(ITEM_QTY);
			doqty =  StrUtils.RoundDB(doqty, IConstants.DECIMALPTS);
			ITEM_QTY = String.valueOf(doqty);
			ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);  
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
		    issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.INV_EXP_DATE, "");
			issMat_HM.put(IConstants.QTY, ITEM_QTY);
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.REMARKS, REMARKS);
			issMat_HM.put(IConstants.RSNDESC, RSNDESC);
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.DONO, TRANSACTIONID);
			issMat_HM.put(IConstants.INVID, INVID);
			issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put(IConstants.BATCH_ID, INVID);
			issMat_HM.put(IConstants.UNITMO, UOM);
			issMat_HM.put("LineNo", i);
			
			//Get Item Count
			POSDetDAO posdetDAO = new POSDetDAO();			
			posdetDAO.setmLogger(mLogger);
			List prdlist = posdetDAO.listProductsForPOSTranID(PLANT, TRANSACTIONID, " (TRANSTATUS <> '')");
			int gcount=prdlist.size()+1;
			issMat_HM.put(IConstants.DODET_DOLNNO, String.valueOf(gcount));
			
			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, ITEM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, ITEM_BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			issMat_HM.put("UOMQTY", UOMQTY);
			double invumqty = Double.valueOf(ITEM_QTY) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;
			if(!ISNONSTKFLG.equalsIgnoreCase("Y"))
			{
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					jsonObjectResult.put("message","Not Enough Inventory For Product : " + ITEM_NUM);
					jsonObjectResult.put("status","0");
					jsonObjectResult.put("LineNo",issMat_HM.get("LineNo").toString());
					throw new Exception(" Not Enough Inventory For Product ");
				}
			}
				if (flag) {
				
					jsonObjectResult = _MiscIssuingUtil.process_MiscIssueMaterial_STD(issMat_HM);
					xmlStr = jsonObjectResult.toString();
					if(xmlStr.contains("Issued successfully!")) //Shopify Inventory Update
					{
						Hashtable htCond = new Hashtable();
							htCond.put(IConstants.PLANT, PLANT);
							if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
								String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
							if(nonstkflag.equalsIgnoreCase("N")) {
								String availqty ="0";
								ArrayList invQryList = null;
								htCond = new Hashtable();
								invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
								if (invQryList.size() > 0) {					
									for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
										String result="";
		                            Map lineArr = (Map) invQryList.get(iCnt);
		                            availqty = (String)lineArr.get("AVAILABLEQTY");
		                            System.out.println(availqty);
									}
									double availableqty = Double.parseDouble(availqty);
		   						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
								}	
							}
							}
					}
				} else {
					throw new Exception(" Issuing is not successfull ");
				}
				if(xmlStr.contains("Issued successfully!"))
				{
					DoDetDAO _DoDetDAO = new DoDetDAO();
					DateUtils dateutils = new DateUtils();
					ShipHisDAO _ShipHisDAO = null;
					_ShipHisDAO = new ShipHisDAO();
					flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_FINGINOTOINVOICE where  GINO='"+TRANSACTIONID+"'");
					if (flag) {
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable htRecvDet = new Hashtable();
						htRecvDet.clear();
						htRecvDet.put(IConstants.PLANT, PLANT);
						htRecvDet.put("GINO", TRANSACTIONID);
						
						
						flag=_DoDetDAO.updateGINOtoInvoice(" set QTY+="+ITEM_QTY,htRecvDet,"",PLANT);
	        			
						Hashtable htRecvHis = new Hashtable();
	        			htRecvHis.clear();
	        			htRecvHis.put(IDBConstants.PLANT, PLANT);
	        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);       					
	        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TRANSACTIONID);
						flag=movHisDao.updateMovHis(" set QTY+="+ITEM_QTY,htRecvHis,"",PLANT);
						
					}else{
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI",TRANSACTIONID);
				  Hashtable	ht=new Hashtable();
				  ht.clear();
				  ht.put(IConstants.PLANT,PLANT);
	              ht.put("GINO",TRANSACTIONID);
	              ht.put(IConstants.CUSTOMER_CODE, "");
	              ht.put(IConstants.DODET_DONUM,"");
	              ht.put(IConstants.STATUS, "NON INVOICEABLE");
	              ht.put(IConstants.AMOUNT, "");
	              ht.put(IConstants.QTY, ITEM_QTY);
	              ht.put("CRAT",dateutils.getDateTime());
	              ht.put("CRBY",LOGIN_USER);
	              ht.put("UPAT",dateutils.getDateTime());
	               _DoDetDAO.insertGINOtoInvoice(ht);
	              
	              //insert MovHis
	                MovHisDAO movHisDao = new MovHisDAO();
	        		movHisDao.setmLogger(mLogger);
	    			Hashtable htRecvHis = new Hashtable();
	    			htRecvHis.clear();
	    			htRecvHis.put(IDBConstants.PLANT, PLANT);
	    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
	    			htRecvHis.put(IDBConstants.ITEM, "");
	    			htRecvHis.put("MOVTID", "");
	    			htRecvHis.put("RECID", "");
	    			htRecvHis.put(IConstants.QTY, ITEM_QTY);
	    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
	    			htRecvHis.put(IDBConstants.REMARKS, "");        					
	    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TRANSACTIONID);
	    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
	    			movHisDao.insertIntoMovHis(htRecvHis);
				}
				}
				else{
					break;
				}
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
        }
		return jsonObjectResult;
	}
	private JSONObject process_locationTransfer(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
     
      
        JSONArray hostArray = JSONArray.fromObject(json_str);
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Map<String, String> locTran_HM = null;
		String PLANT = "", FROM_LOC = "", ITEM_NUM = "", TO_LOC = "", TRAN_TYPE = "";
		String LOGIN_USER = "";
		String BATCH = "", Qty = "",INVQTY="",EXPIRYDATE="",REMARKS="",RSNDESC="",TRANSACTIONID="",EMPNO="",INVID="", UOM = "";
		int qty = 0;
		int invqty = 0;
		for(int i = 0; i < hostArray.size(); i++)
        {
			JSONObject hostObject = hostArray.getJSONObject(i);
			
			PLANT = hostObject.getString("companyCode");
			ITEM_NUM = hostObject.getString("itemNum");
			FROM_LOC =  hostObject.getString("itemLoc");
			TO_LOC =  hostObject.getString("toLoc");
			LOGIN_USER  = hostObject.getString("loginUser");
			TRAN_TYPE  = hostObject.getString("tranType");
			BATCH = hostObject.getString("itemBatch");
			Qty = hostObject.getString("itemQty");
			REMARKS = hostObject.getString("remarks");
			RSNDESC = hostObject.getString("reasoncode");
			RSNDESC = StrUtils.replaceCharacters2Recv(RSNDESC);
			TRANSACTIONID =  hostObject.getString("trxId");
			INVID = hostObject.getString("invid");
			EMPNO =  hostObject.getString("employeeNo");
			UOM =  hostObject.getString("uom");
			
		try {
/*			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TO_LOC"));
			TRAN_TYPE = StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			Qty = StrUtils.fString(request.getParameter("QTY"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"));
			TRANSACTIONID=StrUtils.fString(request.getParameter("ORDERNO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			INVID=StrUtils.fString(request.getParameter("INVID"));
			UOM=StrUtils.fString(request.getParameter("UOM"));*/
			
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			//String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strTranDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
			String LINE_NO = String.valueOf(i);
			// To check transaction already exists in POSHeader
			posServlet _posServlet = new posServlet();
			POSHdrDAO poshdr = new POSHdrDAO();
			Hashtable<String, String> htTrandId = new Hashtable<String, String>();
			htTrandId.put("POSTRANID", TRANSACTIONID);			
			boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
			double qty1 = Double.parseDouble(Qty);
			qty1 = StrUtils.RoundDB(qty1, IConstants.DECIMALPTS );
			Qty = String.valueOf(qty1);

			List listQry = _InvMstDAO.getLocationTransferBatch(PLANT,
					ITEM_NUM, FROM_LOC, BATCH);
			if (listQry.size() > 0) {
				Map m = (Map) listQry.get(0);
				String sQty = (String) m.get("qty");
								
				
				EXPIRYDATE =  (String) m.get("expiredate");
				INVQTY = sQty;
				}else{
                      throw new Exception("Please check the Batch and Quantity");
               }
			
			locTran_HM = new HashMap<String, String>();
			locTran_HM.put(IConstants.PLANT, PLANT);
			locTran_HM.put(IConstants.ITEM, ITEM_NUM);
			locTran_HM.put(IConstants.LOC, FROM_LOC);
			locTran_HM.put(IConstants.LOC2, TO_LOC);
			locTran_HM.put(IConstants.TRAN_TYPE, TRAN_TYPE);
			locTran_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			locTran_HM.put(IConstants.BATCH, BATCH);
			locTran_HM.put(IConstants.QTY, Qty);
			locTran_HM.put(IConstants.CUSTOMER_CODE, "");//invMstDAO.getCustCode(PLANT, ITEM_NUM) // Commented by Samatha as Userfld2 is always empty data
			locTran_HM.put(IConstants.EXPIREDATE, EXPIRYDATE);
			locTran_HM.put(IConstants.TRAN_DATE, strTranDate);
			locTran_HM.put("INV_QTY1", "1");
			locTran_HM.put(IConstants.REMARKS, REMARKS);
            locTran_HM.put(IConstants.RSNDESC, RSNDESC);
            locTran_HM.put(IConstants.PONO, TRANSACTIONID);
            locTran_HM.put(IConstants.INVID, INVID);
            locTran_HM.put(IConstants.EMPNO, EMPNO);
            locTran_HM.put(IConstants.ISPDA, "ISPDA");
            locTran_HM.put(IConstants.BATCH_ID, INVID);
			locTran_HM.put(IConstants.ISSUEDATE, strIssueDate);
			locTran_HM.put(IConstants.UNITMO, UOM);
			locTran_HM.put("LineNo",LINE_NO);
			locTran_HM.put("PDA","PDA:");
			xmlStr = "";
			Hashtable<String, String> htLocMst = new Hashtable<String, String>();
			htLocMst.put("PLANT", locTran_HM.get(IConstants.PLANT));
			htLocMst.put("loc", locTran_HM.get(IConstants.LOC2));
			_LocMstDAO.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			
			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, FROM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			locTran_HM.put("UOMQTY", UOMQTY);
			double invumqty = Double.valueOf(Qty) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;
			
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					
					jsonObjectResult.put("message","Not Enough Inventory For Product : " + ITEM_NUM);
					jsonObjectResult.put("status","0");
					jsonObjectResult.put("LineNo",locTran_HM.get("LineNo").toString());
					throw new Exception(" Not Enough Inventory For Product ");
				}
				if (flag) {
			
			jsonObjectResult = _InvMstUtil.process_LocationTransfer_STD(locTran_HM);
			
			POSHdrDAO posHdr = new POSHdrDAO();
			POSDetDAO posdet = new POSDetDAO();
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put(IDBConstants.PLANT, PLANT);
			ht.put(IDBConstants.POS_TRANID, TRANSACTIONID);
			String QryUpdate = " SET STATUS ='C',RECEIPTNO='" + TRANSACTIONID + "',UPAT ='" + DateUtils.getDate()
					+ "' ";
			boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
				} else {
					throw new Exception(" Transfer is not successfull ");
				}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in transfering Product : "
							//+ locTran_HM.get(IConstants.ITEM)
					+ ITEM_NUM
							+ " to location : "
							//+ locTran_HM.get(IConstants.LOC2) + " Error :: "
							+ TO_LOC + " Error :: "
							+ e.getMessage());
			
			jsonObjectResult.put("message","Error in transfering Product : "
			           + ITEM_NUM
					+ " to location : "
					+ TO_LOC + " Error :: "
					+ e.getMessage());
			jsonObjectResult.put("status","0");
			jsonObjectResult.put("LineNo",locTran_HM.get("LineNo").toString());
			
		}
	}
		return jsonObjectResult;
	}
	private JSONObject process_doPicking_random_bypda(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
     
      
        JSONArray hostArray = JSONArray.fromObject(json_str);
		Map issMat_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",CONTAINER="",REMARK;
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ORD_QTY = "", 
		INV_QTY = "", PICKING_QTY = "",ISNONSTKFLG="",INVID="",INVOICENO="",EMPNO="",UOM="";
		for(int i = 0; i < hostArray.size(); i++)
        {
		JSONObject hostObject = hostArray.getJSONObject(i);
		
		PLANT = hostObject.getString("companyCode");
		PO_NUM = hostObject.getString("dono");
		ITEM_NUM =  hostObject.getString("item");
		ITEM_DESCRIPTION =  hostObject.getString("itemdesc");
		LOGIN_USER  = hostObject.getString("loginUser");
		CUST_NAME  = hostObject.getString("customer");
		ITEM_BATCH = hostObject.getString("batchno");
		PICKING_QTY = hostObject.getString("qty");
		double pickingqty = Double.parseDouble(PICKING_QTY);
		ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
		pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
		PICKING_QTY = String.valueOf(pickingqty);
		PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);		
		ITEM_LOC = hostObject.getString("loc");
		CONTAINER= hostObject.getString("container");
		CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
		REMARK=hostObject.getString("remarks");
		REMARK= StrUtils.replaceCharacters2Recv(REMARK);
		INVID=hostObject.getString("invd");
		UOM =  hostObject.getString("uom");
		try {
			
	/*		PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			//ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEM_DESC"));
			ITEM_DESCRIPTION = new ItemMstDAO().getItemDesc(PLANT,ITEM_NUM);
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			CONTAINER= StrUtils.fString(request.getParameter("CONTAINER"));
			CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
			REMARK=StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			REMARK= StrUtils.replaceCharacters2Recv(REMARK);
			INVID=StrUtils.fString(request.getParameter("INVID"));
			INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));*/
			String LINE_NO = String.valueOf(i);
            DateUtils _dateUtils = new DateUtils();
            String curDate =_dateUtils.getDate();
            String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
            String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
            
			issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			issMat_HM.put(IConstants.PODET_PONUM, PO_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.QTY, PICKING_QTY);
			issMat_HM.put("TRAN_QTY", PICKING_QTY);
			issMat_HM.put(IConstants.INV_EXP_DATE, "");
			issMat_HM.put(IConstants.OUT_CONTAINER, CONTAINER);
			issMat_HM.put(IConstants.OUT_REMARK1, REMARK);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.INVID, INVID);
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);
			issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put(IConstants.BATCH_ID, INVID);
			issMat_HM.put(IConstants.UNITMO, UOM);
			issMat_HM.put("LineNo",LINE_NO);
			xmlStr = "";
			
			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, ITEM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, ITEM_BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			double invumqty = Double.valueOf(PICKING_QTY) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;			
			if(!ISNONSTKFLG.equalsIgnoreCase("Y"))
			{
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					jsonObjectResult.put("message","Not Enough Inventory For Product : " + ITEM_NUM);
					jsonObjectResult.put("status","0");
					jsonObjectResult.put("LineNo",issMat_HM.get("LineNo").toString());
					throw new Exception(" Not Enough Inventory For Product ");
				}
			}
			if (flag) {
				if(!INVOICENO.isEmpty()){
				flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_SHIPHIS where DONO='"+PO_NUM+"' and INVOICENO='"+INVOICENO+"'");
				}
				else{
					flag = false;
				}
				jsonObjectResult = _DOUtil.process_DoPickingForPDA_Random_ByPDAList(issMat_HM);
				xmlStr = jsonObjectResult.toString();
				if(xmlStr.contains("picked successfully")) //Shopify Inventory Update
				{
					Hashtable htCond = new Hashtable();
   					htCond.put(IConstants.PLANT, PLANT);
   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
   						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
						if(nonstkflag.equalsIgnoreCase("N")) {
   						String availqty ="0";
   						ArrayList invQryList = null;
   						htCond = new Hashtable();
   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
   						if (invQryList.size() > 0) {					
   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
   								String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                availqty = (String)lineArr.get("AVAILABLEQTY");
                                System.out.println(availqty);
   							}
   							double availableqty = Double.parseDouble(availqty);
       						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
   						}	
						}
   					}
				}
				if (!flag) {
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				}
			} else {
				throw new Exception(" Picking is not successfull ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
        }
		return jsonObjectResult;
	}
	private JSONObject process_doissue_random_bypda(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
     
      
        JSONArray hostArray = JSONArray.fromObject(json_str);
		Map issMat_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "",DO_LN_NUM="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",CONTAINER="",REMARK;
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ORD_QTY = "", INV_QTY = "", 
		PICKING_QTY = "",ISNONSTKFLG="",INVID="",INVOICENO="",EMPNO="",creditLimit ="", creditBy = "",ISSUEDATE="",UOM="",SHIPPINGNO="";
		
		for(int i = 0; i < hostArray.size(); i++)
        {
		JSONObject hostObject = hostArray.getJSONObject(i);
		
		PLANT = hostObject.getString("companyCode");
		PO_NUM = hostObject.getString("dono");
		ITEM_NUM =  hostObject.getString("item");
		ITEM_DESCRIPTION =  hostObject.getString("itemdesc");
		LOGIN_USER  = hostObject.getString("loginUser");
		CUST_NAME  = hostObject.getString("customer");
		ITEM_BATCH = hostObject.getString("batchno");
		PICKING_QTY = hostObject.getString("qty");
		double pickingqty = Double.parseDouble(PICKING_QTY);
		ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
		pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
		PICKING_QTY = String.valueOf(pickingqty);
		PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);		
		ITEM_LOC = hostObject.getString("loc");
		CONTAINER= hostObject.getString("container");
		CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
		REMARK=hostObject.getString("remarks");
		REMARK= StrUtils.replaceCharacters2Recv(REMARK);
		INVID=hostObject.getString("invd");
		UOM =  hostObject.getString("uom");
		INVOICENO= hostObject.getString("invoice");
		EMPNO= hostObject.getString("employeeno");
		ISSUEDATE = hostObject.getString("issuedate");
		DO_LN_NUM= hostObject.getString("dolnum");
		try {
/*			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEM_DESC"));
			
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ISSUEDATE =StrUtils.fString(request.getParameter("ISSUEDATE"));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			CONTAINER= StrUtils.fString(request.getParameter("CONTAINER"));
			REMARK=StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			REMARK= StrUtils.replaceCharacters2Recv(REMARK);
			INVID=StrUtils.fString(request.getParameter("INVID"));
			INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			DO_LN_NUM=StrUtils.fString(request.getParameter("DO_LN_NUM"));*/
			
			if (SHIPPINGNO.length() == 0) {
				SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
			}
			String LINE_NO = String.valueOf(i);
		    DateUtils _dateUtils = new DateUtils();
            String curDate =_dateUtils.getDate();
            String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
            String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
            issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			issMat_HM.put(IConstants.DODET_DONUM, PO_NUM);
			issMat_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.OUT_CONTAINER, CONTAINER);
			issMat_HM.put(IConstants.OUT_REMARK1, REMARK);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, ISSUEDATE);
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);
            issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
            issMat_HM.put(IConstants.UNITMO, UOM);
            issMat_HM.put("LineNo",LINE_NO);
            
            CustUtil custUtil = new CustUtil();
			ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, PO_NUM),PLANT);
			creditLimit   = (String)arrCust.get(24);
			creditBy   = (String)arrCust.get(35);
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
			issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
			issMat_HM.put(IConstants.BASE_CURRENCY, _PlantMstDAO.getBaseCurrency(PLANT));
			xmlStr = "";
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			issMat_HM.put(IConstants.QTY, String.valueOf(PICKING_QTY));
			issMat_HM.put("UOMQTY", UOMQTY);
			boolean flag = true;

			if (flag) {
				boolean shipflag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_SHIPHIS where DONO='"+PO_NUM+"' and INVOICENO='"+INVOICENO+"'");
				jsonObjectResult = _DOUtil.process_DoIssue_Random_ByPDAList(issMat_HM);
				xmlStr = jsonObjectResult.toString();
				if(xmlStr.contains("issued successfully")) //Shopify Inventory Update
				{
					Hashtable htCond = new Hashtable();
   					htCond.put(IConstants.PLANT, PLANT);
   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
   						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
						if(nonstkflag.equalsIgnoreCase("N")) {
   						String availqty ="0";
   						ArrayList invQryList = null;
   						htCond = new Hashtable();
   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
   						if (invQryList.size() > 0) {					
   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
   								String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                availqty = (String)lineArr.get("AVAILABLEQTY");
                                System.out.println(availqty);
   							}
   							double availableqty = Double.parseDouble(availqty);
       						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
   						}	
						}
   					}
				}
				//To GI
				if(xmlStr.contains("issued successfully"))
				{
					if (!shipflag) {
						new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
						}
					DoDetDAO _DoDetDAO = new DoDetDAO();
					DateUtils dateutils = new DateUtils();
					Hashtable htRecvDet = new Hashtable();
					
					double unitprice=0,totalprice=0,totalqty=0;
					String priceval="";
					Hashtable htDoDet = new Hashtable();
					ArrayList DODetails = null;
					String queryDoDet = "(UNITPRICE*CURRENCYUSEQT) as UNITPRICE";
					htDoDet.put(IConstants.DODET_DONUM, PO_NUM);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.ITEM, ITEM_NUM);
					htDoDet.put(IConstants.UNITMO, UOM);
					DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
						if(DODetails.size() > 0) {	
											Map map1 = (Map) DODetails.get(0);
												unitprice = Double.parseDouble((String) map1.get("UNITPRICE"));
												PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
												StrUtils strUtils     = new StrUtils();
												totalprice=totalprice+(unitprice* Float.parseFloat(PICKING_QTY));
												priceval=String.valueOf(totalprice);
												double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
												priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
						}
					
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
					htRecvDet.put("GINO", INVOICENO);                    
					htRecvDet.put(IConstants.DODET_DONUM, PO_NUM);
					htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
					htRecvDet.put(IConstants.AMOUNT, priceval);
					htRecvDet.put(IConstants.QTY, String.valueOf(PICKING_QTY));
					htRecvDet.put("CRAT",dateutils.getDateTime());
					htRecvDet.put("CRBY",LOGIN_USER);
					htRecvDet.put("UPAT",dateutils.getDateTime());
					flag = _DoDetDAO.insertGINOtoInvoice(htRecvDet);
	                
	                //insert MovHis
	                MovHisDAO movHisDao = new MovHisDAO();
	        		movHisDao.setmLogger(mLogger);
	    			Hashtable htRecvHis = new Hashtable();
	    			htRecvHis.clear();
	    			htRecvHis.put(IDBConstants.PLANT, PLANT);
	    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
	    			htRecvHis.put(IDBConstants.ITEM, "");
	    			htRecvHis.put("MOVTID", "");
	    			htRecvHis.put("RECID", "");
	    			htRecvHis.put(IConstants.QTY, String.valueOf(PICKING_QTY));
	    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
	    			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
	    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
	    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
	    			flag = movHisDao.insertIntoMovHis(htRecvHis);

				}

			/*	if (flag) {
				if (!shipflag) {
					new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
					}
				}*/
			} else {
				throw new Exception(" Issue is not successfull ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
        }
		return jsonObjectResult;
	}
	private String GenerateShippingNo(String plant, String loginuser) {

		String PLANT = "";
		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
		boolean resultflag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnShippNo = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht
					.put(IDBConstants.TBL_FUNCTION,
							IDBConstants.TBL_SHIPPING_CAPTION);

			boolean exitFlag = false;
			exitFlag = _TblControlDAO.isExisit(ht, extCond, plant);

			if (exitFlag == false) {

				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "Shipping");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) loginuser);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,
						plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "GENERATE_SHIPPING");
				htm.put("RECID", "");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception(
							"Generate Shipping Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, extCond);
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnShippNo = plant + updatedSeq;

				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "Shipping");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(),
						htTblCntUpdate, extCond, plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "UPDATE_SHIPPING");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("RECID", "");
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update Shippoing Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception(
							"Update Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return rtnShippNo;
	}
	public boolean CheckInvMstForGoddsIssue(Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean flag = true;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Boolean batchChecked = false;
		
		try {
			

			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			HttpSession session = request.getSession();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(map.get(IConstants.BATCH_ID)) && !"-1".equals(map.get(IConstants.BATCH_ID))) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			StringBuffer sql = new StringBuffer(" ");
			List invlist = _InvMstDAO.selectInvMstByCrat("item,loc,userfld4 as batch,qty,crat,id", htInvMst);
			String actualqty = "";
			double actqty = 0;
			double lnqty = 0, balancqty = 0;
			double totalqty = 0;
			actualqty = map.get(IConstants.QTY);			
			actqty = Double.valueOf(actualqty);

			if (request.getParameter("chkbatch") != null) {
				batchChecked = true;
			}

			// Calculate total qty in the loc
			for (int j = 0; j < invlist.size(); j++) {
				Map lineitem = (Map) invlist.get(j);
				String lineitemqty = (String) lineitem.get("qty");				

				totalqty = totalqty + Double.valueOf(lineitemqty);

			}

			if (actqty > totalqty) {
				flag = false;
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

			if (totalqty == 0) {
				flag = false;
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

		} catch (Exception e) {
			flag = false;
		}
		return flag;
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
}