package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OutletBeanDAO;
import com.track.dao.POSAmountByPayModeDAO;
import com.track.dao.POSBankInByPayModeDAO;
import com.track.dao.POSShiftAmountHdrDAO;
import com.track.dao.PaymentModeMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ReconciliationHdrDAO;
import com.track.dao.SalesDetailDAO;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.POSAmountByPayMode;
import com.track.db.object.POSBankInByPayMode;
import com.track.db.object.POSShiftAmountHdr;
import com.track.db.object.PaymentModeMst;
import com.track.db.object.ReconciliationHdr;
import com.track.db.util.CoaUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.POSUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.service.JournalService;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;


/**
 * Servlet implementation class PosReport
 */
public class PosReportServlet extends HttpServlet implements IMLogger   {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.PosServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PosServlet_PRINTPLANTMASTERINFO;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PosReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String action = request.getParameter("action");
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		
		 if(action.equalsIgnoreCase("Reprint")){
			 String tranid = request.getParameter("TRANID");
				String loc = request.getParameter("LOC");//added by Bruhan
		
				try {
					viewPOSReport(request, response, tranid, loc);//added by Bruhan
				}catch(Exception e)
				{
					System.out.print("Exception Message"+e.getMessage());	
				} 
		 }
		 if(action.equalsIgnoreCase("POS_Transaction_Excel")){
			 HSSFWorkbook wb = new HSSFWorkbook();
			 wb = this.posTransactionExcel(request,response,wb);
			 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			 wb.write(outByteStream);
			 byte [] outArray = outByteStream.toByteArray();
			 response.setContentType("application/ms-excel");
			 response.setContentLength(outArray.length);
			 response.setHeader("Expires:", "0"); // eliminates browser caching
			 
			 response.setHeader("Content-Disposition", "attachment; filename=posTransactionDetails.xls");
			
			 OutputStream outStream = response.getOutputStream();
			 outStream.write(outArray);
			 outStream.flush();
			 
		 }
		 if(action.equalsIgnoreCase("POS_SALES_RECIEVE_REPORT")){
			 JSONObject jsonObjectResult = new JSONObject();
			 jsonObjectResult = this.getsalesrecievedreport(request);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonObjectResult.toString());
			 response.getWriter().flush();
			 response.getWriter().close();
		 }
		 
		 if(action.equalsIgnoreCase("POS_SALES_RECIEVE_MONTH_REPORT")){
			 JSONObject jsonObjectResult = new JSONObject();
			 jsonObjectResult = this.getsalesrecievedmonthreport(request);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonObjectResult.toString());
			 response.getWriter().flush();
			 response.getWriter().close();
		 }
		 if (action.equalsIgnoreCase("receivepayment")) {
			 	JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = receivepayment(request, response);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		 if (action.equalsIgnoreCase("postreceivepayment")) {
			 	JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = postreceivepayment(request, response);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		 if (action.equalsIgnoreCase("posrecivedjournal")) {
			 	JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = posrecivedjournal(request, response);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		 if (action.equalsIgnoreCase("posbankinjournal")) {
			 	JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = posbankinjournal(request, response);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		 if(action.equalsIgnoreCase("POS_SALES_BANKIN_REPORT")){
			 JSONObject jsonObjectResult = new JSONObject();
			 jsonObjectResult = this.getsalesbankinreport(request);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonObjectResult.toString());
			 response.getWriter().flush();
			 response.getWriter().close();
		 }
		 
		 if (action.equalsIgnoreCase("bankinreceivepayment")) {
			 	JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = bankinreceivepayment(request, response);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		 
		 if (action.equalsIgnoreCase("BANKTEST")) {
			 	JSONObject resultJson = new JSONObject();
			 	POSBankInByPayModeDAO pOSBankInByPayModeDAO = new POSBankInByPayModeDAO();
				String FROM_DATE = StrUtils.fString(request.getParameter("fdate"));
				String TO_DATE = StrUtils.fString(request.getParameter("tdate"));
				String OUTLETCODE = StrUtils.fString(request.getParameter("outletcode"));
				String TERMINALCODE = StrUtils.fString(request.getParameter("terminalcode"));
				ArrayList bankincash = new ArrayList();
				try {
					bankincash = pOSBankInByPayModeDAO.selectbankincash(plant, OUTLETCODE, TERMINALCODE, FROM_DATE, TO_DATE);
					resultJson.put("cashlist", bankincash);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		 
		 if (action.equalsIgnoreCase("bankincashreceivepayment")) {
			 	JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = bankincashreceivepayment(request, response);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		 if (action.equalsIgnoreCase("RECONCILIATION_REPORT")) {
			 	JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = getreconciliationreport(request, response);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	
	public void viewPOSReport(HttpServletRequest request,
            HttpServletResponse response, String tranid, String loc) throws IOException,
            Exception {

    try {

            java.sql.Connection con = null;
            SalesDetailDAO saledao = new SalesDetailDAO();
            PlantMstUtil pmUtil = new PlantMstUtil();
            // added by Bruhan
            LocUtil lcUtil = new LocUtil();
            //end
            HttpSession session = request.getSession();
            String CFAX = "", CWEBSITE = "", CRCBNO = "", CGSTREGNO = "", PLANTDESC = "",CTEL="", CADD1 = "", CADD3 = "", CADD2 = "", CADD4 = "", CZIP = "", CCOUNTRY = "";
            String CONTACTNAME="",CHPNO="",CEMAIL="";
            String PLANT = (String) session.getAttribute("PLANT");
            String userid = (String) session.getAttribute("LOGIN_USER");
             String   paymentmode = "";
            con = DbBean.getConnection();
            String SysDate = DateUtils.getDate();
            String SysTime = DateUtils.Time();
            String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPOS";
            String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
        
            ArrayList listQry1 = saledao.getSalesByTranid(PLANT, tranid, "");
            Map salemap = (Map)listQry1.get(0);
            paymentmode = (String)salemap.get("paymentmode");
             String trantype = (String)salemap.get("trantype");
            Map parameters = new HashMap();
            ArrayList listQryPlantMst = pmUtil.getPlantMstDetails(PLANT);
       		for (int i = 0; i < listQryPlantMst.size(); i++) {
       			Map map = (Map) listQryPlantMst.get(i);

       			PLANTDESC  = (String) map.get("PLNTDESC");
       			CADD1 = (String) map.get("ADD1");
       			CADD2 = (String) map.get("ADD2");
       			CADD3 = (String) map.get("ADD3");
       			CADD4 = (String) map.get("ADD4");
       			CCOUNTRY = (String) map.get("COUNTY");
       			CZIP = (String) map.get("ZIP");
       			CTEL = (String) map.get("TELNO");
       			CFAX = (String) map.get("FAX");
       			CRCBNO = StrUtils.fString((String) map.get("RCBNO"));
       			CONTACTNAME = (String) map.get("NAME");
       			CHPNO = (String) map.get("HPNO");
       			CEMAIL = (String) map.get("EMAIL");
       		}
        
        POSUtil posUtil = new POSUtil();
        Map mhdr= posUtil.getPosReceiptHdrDetails(PLANT);
        

        String   PaymentMode = (String) mhdr.get("PAYMENT_MODE");
        String   SalesRep = (String) mhdr.get("SALES_REP");
        String   SerialNo = (String) mhdr.get("SERIAL_NO");
        String   ReceiptNo = (String) mhdr.get("RECEIPT_NO");
        String   Product = (String) mhdr.get("PRODUCT");
        String   Description = (String) mhdr.get("PROD_DESC");
        String   UnitPrice = (String) mhdr.get("UNIT_PRICE");
        String   Qty = (String) mhdr.get("QTY");
        String   Discount = (String) mhdr.get("DISCOUNT");
        String   Total = (String) mhdr.get("TOTAL");
        String   Subtotal = (String) mhdr.get("SUBTOTAL");
        String   Tax = (String) mhdr.get("TAX");
        String   TotalAmt = (String) mhdr.get("TOTAL_AMT");
        String   PaymentPaid = (String) mhdr.get("AMOUNT_PAID");
        String   ChangeRemaining = (String) mhdr.get("CHANGE");
        String   DisplayByLoc = (String) mhdr.get("ADDRBYLOC");
        
        String HEADER = (String) mhdr.get("HDR");
        String  GREET1 = (String) mhdr.get("G1");
        String GREET2 = (String) mhdr.get("G2");
        String FOOT1 = (String) mhdr.get("F1");
        String   FOOT2 = (String) mhdr.get("F2");
        String msgWithcompanyAdrr =  GREET1 +"##"+ GREET2 +"##"+ FOOT1 +"##"+ FOOT2 +"##";
        if(DisplayByLoc.equals("1"))
        {
        	
        	if(PLANTDESC.length()>0)
		        msgWithcompanyAdrr=msgWithcompanyAdrr+PLANTDESC+",";
		        if(CADD1.length()>0)
		         msgWithcompanyAdrr=msgWithcompanyAdrr+" Address:"+CADD1+"," ;
		        
		        if(CADD2.length()>0)
		            msgWithcompanyAdrr=msgWithcompanyAdrr+CADD2 +"," ;
		        if(CADD3.length()>0)
		            msgWithcompanyAdrr=msgWithcompanyAdrr+CADD3 +",";
		        if(CADD4.length()>0) 
		        msgWithcompanyAdrr= msgWithcompanyAdrr+ CADD4 +",";
		        if(CCOUNTRY.length()>0)
		        msgWithcompanyAdrr= msgWithcompanyAdrr+ CCOUNTRY ;
		        if(CZIP.length()>0)
		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"("+CZIP+")" ;
		        if(CTEL.length()>0)
		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"Tel:"+CTEL+"," ; 
		        if(CFAX.length()>0)
		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"Fax:"+CFAX+"##" ; 
		        if(CFAX=="null" || CFAX=="")
		       	msgWithcompanyAdrr= msgWithcompanyAdrr  +"##" ; 	
		        String strAdr2="";
		                
		        
		        if(CWEBSITE.length()>0)
		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"Website:"+CWEBSITE ; 
		        strAdr2=strAdr2+CWEBSITE ;
		        if(strAdr2.length()>0){strAdr2 = ",";}
		        
		        if(CRCBNO.length()>0)
		        msgWithcompanyAdrr= msgWithcompanyAdrr  +strAdr2+" Business Registration No:"+CRCBNO ; 
		        
		        if(CGSTREGNO.length()>0)
		        msgWithcompanyAdrr= msgWithcompanyAdrr  +","+" GST Registration No: "+CGSTREGNO ; 
		        
        	   ArrayList listQry = lcUtil.getLocListDetails(PLANT, loc);
    			for (int i = 0; i < listQry.size(); i++) {
					Map map1 = (Map) listQry.get(i);
							
					PLANTDESC  = (String) map1.get("COMNAME");
					CADD1 = (String) map1.get("ADD1");
					CADD2 = (String) map1.get("ADD2");
					CADD3 = (String) map1.get("ADD3");
					CADD4 = (String) map1.get("ADD4");
					CCOUNTRY = (String) map1.get("COUNTRY");
					CZIP = (String) map1.get("ZIP");
					CTEL = (String) map1.get("TELNO");
					CFAX = (String) map1.get("FAX");
					CRCBNO = StrUtils.fString((String) map1.get("RCBNO"));
					CONTACTNAME = "";
		   			CHPNO ="";
		   			CEMAIL = "";
					
			}
        }
        
          
        
        String[] receiptmsgarray = getReceiptMsg(msgWithcompanyAdrr);
        

        int index=1;
        // POS tran Details 
        for (int i = 0; i < receiptmsgarray.length; i++) {
                parameters.put("MSG"+index, receiptmsgarray[i]);
                index=index+1;
        }
       
	    parameters.put("imagePath", imagePath);
	    parameters.put("imagePath1", imagePath1);
        //below code is added by Bruhan for report headings
        parameters.put("lblPaymentMethods", PaymentMode);
        parameters.put("lblSalesRep", SalesRep);
        parameters.put("lblSerialNo", SerialNo);
        parameters.put("lblReceiptNo", ReceiptNo); 
        parameters.put("lblItem", Product);
        parameters.put("lblDescription", Description);
        parameters.put("lblUnitPrice", UnitPrice);
        parameters.put("lblQty", Qty);
        parameters.put("lblDiscount", Discount);
        parameters.put("lblTotal", Total);
        parameters.put("lblSubtotal", Subtotal);
        parameters.put("lblTax", Tax);
        parameters.put("lblTotalAmt", TotalAmt);
        parameters.put("lblPaymentPaid", PaymentPaid);
       // parameters.put("lblChange", ChangeRemaining);
        //end
            parameters.put("ReceiptNo", tranid);
            parameters.put("user", userid);
            parameters.put("company", PLANT);
            parameters.put("currentDate", SysDate);
            parameters.put("curTime", SysTime);
            parameters.put("paymentType", paymentmode);
            if(trantype.equalsIgnoreCase("POS_REFUND")){
             parameters.put("heading", " REFUND ");
            }else{
             parameters.put("heading", HEADER);
            }
            
            parameters.put("fromAddress_CompanyName", PLANTDESC);
    		parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
    		parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
    		parameters.put("fromAddress_Country", CCOUNTRY);
    		parameters.put("fromAddress_ZIPCode", CZIP);
    		parameters.put("fromAddress_TpNo", CTEL);
    		parameters.put("fromAddress_FaxNo", CFAX);
    		
    		 if(CONTACTNAME.length() > 1 ){
    			 parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
    		 }
    		 else
    		 {
    			 parameters.put("fromAddress_ContactPersonName","");  
    			
    		 }
    		 
    		 if(CHPNO.length() > 1 ){
    			 parameters.put("fromAddress_ContactPersonMobile", CHPNO);
    		 }
    		 else
    		 {
    			 parameters.put("fromAddress_ContactPersonMobile","");
    		 }
    			
    		 		 
    		 if(CEMAIL.length() > 1 ){
    			parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
    		 }
    		 else
    		 {
    			parameters.put("fromAddress_ContactPersonEmail", "");
    			
    		 }
    		 parameters.put("referanceNO",CRCBNO); 
        
        String gstValue = new selectBean().getGST("POS",PLANT);
        double gst=new Double(gstValue).doubleValue()/100;
        parameters.put("Gst",gst);
        
            long start = System.currentTimeMillis();
            System.out.println("**************" + " Start Up Time : " + start
                            + "**********");

            byte[] bytes = JasperRunManager.runReportToPdf(jasperPath
                            + ".jasper", parameters, con);

            response.addHeader("Content-disposition","attachment;filename=reporte.pdf");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.setContentType("application/pdf");

    } catch (IOException e) {

            e.printStackTrace();

    }
}
	
	

	private HSSFWorkbook posTransactionExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String PLANT = "", USERID ="";
		String PRD_CLS_ID = "", VOID = "" ,SALES_NO = "",FROM_PRICE = "",TO_PRICE = "" ;
		String extraCond ="";
		int SheetNo =1;	
		int maxRowsPerSheet = 65535;
		SalesDetailDAO salesdao = new SalesDetailDAO();
		StrUtils strUtils = new StrUtils();
		java.util.List sales_list = new java.util.ArrayList();
		Hashtable ht = new Hashtable();
		 DecimalFormat decformat = new DecimalFormat("#,##0.00");
		try{
			HttpSession session = request.getSession();
			PLANT = session.getAttribute("PLANT").toString();
			USERID = session.getAttribute("LOGIN_USER").toString();

			
			    String STARTDATE=strUtils.fString(request.getParameter("STARTDATE"));
			    String TODATE=strUtils.fString(request.getParameter("TODATE"));
			    String LOC =strUtils.fString(request.getParameter("LOC_0")).trim();
			    String ITEM = strUtils.fString(request.getParameter("ITEM")).trim();
		        String DESC =strUtils.fString(request.getParameter("DESC")).trim();
			    String recptno = strUtils.fString(request.getParameter("recptno")).trim();
			    String TRANTYPE = strUtils.fString(request.getParameter("TRANTYPE")).trim();
			    String PAYMENTMODE = strUtils.fString(request.getParameter("PAYMENTMODE")).trim();
			    String userid = strUtils.fString(request.getParameter("USERID")).trim();
			    String rptType= strUtils.fString(request.getParameter("REPORTTYPE")).trim();
 				String PRD_CLS_ID1 =  strUtils.fString(request.getParameter("PRD_CLS_ID"));
			    String PRD_TYPE_ID =  strUtils.fString(request.getParameter("PRD_TYPE_ID"));
			    String PRD_BRAND_ID =  strUtils.fString(request.getParameter("PRD_BRAND_ID"));
			    
			   
			if(rptType.equalsIgnoreCase("POS_SALES_MGMT")){
			    if(DESC.length()>0){
		            //extraCond = " AND REPLACE(ITEMDESC,' ','') LIKE '"+ strUtils.InsertQuotes(DESC.replaceAll(" ","")) + "%' group by tranid,trantype,purchasedate order by cast(purchasedate as date) ";
			    	 extraCond = " AND REPLACE(ITEMDESC,' ','') LIKE '"+ strUtils.InsertQuotes(DESC.replaceAll(" ","")) + "%' group by item,itemdesc,qty,trantype,tranid,purchasedate,remarks,rsncode,crby,batch order by cast(purchasedate as date) ";
		            }else{
		              extraCond = " group by item,itemdesc,qty,trantype,tranid,purchasedate,remarks,rsncode,crby,batch order by cast(purchasedate as date)";
		            }
			    sales_list = salesdao.getDistinctSalesForMgmt(PLANT,STARTDATE,TODATE,ITEM,"",recptno,userid,TRANTYPE,PRD_CLS_ID1,PRD_TYPE_ID,PRD_BRAND_ID,extraCond);
			    
			}else if(rptType.equalsIgnoreCase("POS_TRANSACTION")){
			      extraCond = " group by trantype,tranid,b.PAYMENTMODE,amount,purchasedate,remarks,rsncode,crby order by cast(purchasedate as date)";	
			      sales_list = salesdao.getDistinctSales(PLANT,STARTDATE,TODATE,LOC,recptno,userid,TRANTYPE,PAYMENTMODE,extraCond);	
			}
				
				 HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					dataStyle = createDataStyle(wb);
					 sheet = wb.createSheet("Sheet_"+SheetNo);
					 styleHeader = createStyleHeader(wb);
					 sheet = this.createWidth(sheet,rptType);
					 sheet = this.createHeader(sheet,styleHeader,rptType);
					 int index = 1;
				     if (sales_list.size() > 0) {
								
						for (int i = 0; i < sales_list.size(); i++) {
							java.util.Map map = (java.util.Map) sales_list.get(i);						
							int k = 0;
							
							
								    dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow( index);
								    
								    HSSFCell cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(new Integer(index).toString()));
									cell.setCellStyle(dataStyle);
									
								    cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("postranid"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("tranid"))));
									cell.setCellStyle(dataStyle);
									
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("CRBY"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("purchasedate"))));
									cell.setCellStyle(dataStyle);
									
									if(rptType.equalsIgnoreCase("POS_SALES_MGMT")){
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("item"))));
										cell.setCellStyle(dataStyle);
										

										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("itemdesc"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("batch"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(Double.parseDouble(StrUtils.fString((String)map.get("qty"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(Double.parseDouble(StrUtils.fString((String)map.get("price"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("remarks"))));
										cell.setCellStyle(dataStyle);
										
										
										
									
									}else if(rptType.equalsIgnoreCase("POS_TRANSACTION")){
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("paymentMode"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString((String)map.get("amount"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)map.get("remarks"))));
									cell.setCellStyle(dataStyle);
									}
								 index++;
									 if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 SheetNo++;
										 sheet = wb.createSheet("Sheet_"+SheetNo);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createWidth(sheet,rptType);
										 sheet = this.createHeader(sheet,styleHeader,rptType);
										 
									 }							   
								 

							  }
							
				 }
				 else if (sales_list.size() < 1) {		
					

						System.out.println("No Records Found To List");
					}
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;
	}
	
	
	private HSSFSheet createHeader(HSSFSheet sheet, HSSFCellStyle styleHeader,String rptType){
		int k = 0;
		try{
		
		
		HSSFRow rowhead = sheet.createRow( 0);
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("S/N"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Tran ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Receipt No"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("User ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Transaction Date"));
		cell.setCellStyle(styleHeader);
		if(rptType.equalsIgnoreCase("POS_SALES_MGMT")){
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Batch"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Qty"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Total Price"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Remarks"));
			cell.setCellStyle(styleHeader);
			
		}else if(rptType.equalsIgnoreCase("POS_TRANSACTION")){
			
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("PaymentMode"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Total Amount"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Remarks"));
		cell.setCellStyle(styleHeader);
		}
		
	
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFSheet createWidth(HSSFSheet sheet, String rptType ){
		
		try{
			
			sheet.setColumnWidth(0 ,1500);
			sheet.setColumnWidth(1 ,5500);
			sheet.setColumnWidth(2 ,4000);			
			sheet.setColumnWidth(3 ,3200);
			sheet.setColumnWidth(4 ,4000);
			
			if(rptType.equalsIgnoreCase("POS_SALES_MGMT")){
				sheet.setColumnWidth(5 ,4000);
				sheet.setColumnWidth(6 ,7000);
				sheet.setColumnWidth(7 ,4000);			
				sheet.setColumnWidth(8 ,3200);
				sheet.setColumnWidth(9 ,4000);
				sheet.setColumnWidth(10 ,7000);
			}else if(rptType.equalsIgnoreCase("POS_TRANSACTION")){
				sheet.setColumnWidth(5 ,3800);
				sheet.setColumnWidth(6 ,3200);
				sheet.setColumnWidth(7 ,7000);
			}
			
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
private HSSFCellStyle createStyleHeader(HSSFWorkbook wb){
		
		//Create style
		 HSSFCellStyle styleHeader = wb.createCellStyle();
		  HSSFFont fontHeader  = wb.createFont();
		  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		  fontHeader.setFontName("Arial");	
		  styleHeader.setFont(fontHeader);
		  styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		  styleHeader.setWrapText(true);
		  return styleHeader;
	}
	
    private HSSFCellStyle createDataStyle(HSSFWorkbook wb){
		
		//Create style
		  HSSFCellStyle dataStyle = wb.createCellStyle();
		  dataStyle.setWrapText(true);
		  return dataStyle;
	}
	
	public String[] getReceiptMsg(String msgkey)
	{
		String[] msgarray = null;
			msgarray = msgkey.split("##");
		
		return msgarray;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

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
	
	private JSONObject getsalesrecievedreport(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		HTReportUtil movHisUtil = new HTReportUtil();
		try {

			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
			String TERMINAL = StrUtils.fString(request.getParameter("TERMINALNAME"));
			String TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			String STATUS = StrUtils.fString(request.getParameter("STATUS"));
			String OUTLET = StrUtils.fString(request.getParameter("OUTLET_NAME"));
			String OUTLETCODE = StrUtils.fString(request.getParameter("OUTLET_CODE"));
			
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);

			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();

			POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
			POSAmountByPayModeDAO pOSAmountByPayModeDAO = new POSAmountByPayModeDAO();
			PaymentModeMstDAO paymentModeMstDAO = new PaymentModeMstDAO();
			OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
			
			List<PaymentModeMst> paymentModeMstList = paymentModeMstDAO.getAllPaymentModeMst(PLANT);
			List<POSShiftAmountHdr> pOSShiftAmountHdrList = pOSShiftAmountHdrDAO.getbyotds(PLANT, STATUS, OUTLETCODE,
					TERMINALCODE, FROM_DATE, TO_DATE);
			if (pOSShiftAmountHdrList.size() > 0) {
				for (POSShiftAmountHdr posShiftAmountHdr : pOSShiftAmountHdrList) {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("sid", posShiftAmountHdr.getID());
					resultJsonInt.put("shiftdate", posShiftAmountHdr.getShiftDate());
					resultJsonInt.put("shifttime", posShiftAmountHdr.getShiftTime());
					resultJsonInt.put("hdrstatus", posShiftAmountHdr.getPayReciveStatus());
					resultJsonInt.put("outlet", posShiftAmountHdr.getOutlet());
					String outletname = outletBeanDAO.getOutletname(PLANT, posShiftAmountHdr.getOutlet(), "");
					resultJsonInt.put("outletname", outletname);
					resultJsonInt.put("terminal", posShiftAmountHdr.getTerminal());
					String terminalname = outletBeanDAO.getOutletTerminalname(PLANT, posShiftAmountHdr.getOutlet(), posShiftAmountHdr.getTerminal());
					resultJsonInt.put("terminalname", terminalname);
					resultJsonInt.put("employee", posShiftAmountHdr.getEmployeeID());
					resultJsonInt.put("expenses", posShiftAmountHdr.getExpenses());
					resultJsonInt.put("shifttoatal", posShiftAmountHdr.getTotalSales());
					double expvalue=0.0;
					double diffvalue=0.0;
					for (PaymentModeMst paymentModeMst : paymentModeMstList) {
						List<POSAmountByPayMode> pOSAmountByPayModelist = pOSAmountByPayModeDAO
								.getbyhdridandpaymodelist(PLANT, posShiftAmountHdr.getID(),
										paymentModeMst.getPAYMENTMODE());
						if (pOSAmountByPayModelist.size() > 0) {
							POSAmountByPayMode pOSAmountByPayMode = pOSAmountByPayModeDAO.getbyhdridandpaymode(PLANT,
									posShiftAmountHdr.getID(), paymentModeMst.getPAYMENTMODE());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode(), pOSAmountByPayMode.getAmount());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"restatus", pOSAmountByPayMode.getRecivedStatus());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmid", pOSAmountByPayMode.getID());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmstatus", pOSAmountByPayMode.getRecivedStatus());
							
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmramount", pOSAmountByPayMode.getRecivedAmount());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmraccount", pOSAmountByPayMode.getReceivedAccount());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmeamount", pOSAmountByPayMode.getExpenses());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmeaccount", pOSAmountByPayMode.getExpensesAccount());
							
							POSAmountByPayMode pOSAmountByPayModeForCoa = pOSAmountByPayModeDAO.getbypaymodeandmaxid(PLANT,paymentModeMst.getPAYMENTMODE());
							if(pOSAmountByPayModeForCoa == null) {
								resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", "POS UNDEPOSITED");
								resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", "POS EXPENSES");
							}else {
								if(pOSAmountByPayModeForCoa.getReceivedAccount() == null) {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", "POS UNDEPOSITED");
								}else {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", pOSAmountByPayModeForCoa.getReceivedAccount());
								}
								if(pOSAmountByPayModeForCoa.getExpensesAccount() == null) {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", "POS EXPENSES");
								}else {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", pOSAmountByPayModeForCoa.getExpensesAccount());
								}
							}
							
							String accname = pOSAmountByPayMode.getPaymentMode().toUpperCase()+" IN TRANSIT";
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pdaccount", accname);
							if(pOSAmountByPayMode.getPosDiffAmount() == null) {
								resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pdamount", 0);
							}else {
								resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pdamount", pOSAmountByPayMode.getPosDiffAmount());
							}
							
							expvalue=expvalue+pOSAmountByPayMode.getExpenses();
							if(pOSAmountByPayMode.getPosDiffAmount() == null) {
								diffvalue=diffvalue;
							}else {
								diffvalue=diffvalue+pOSAmountByPayMode.getPosDiffAmount();
							}
							
							
						} else {
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE(), 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"restatus", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmid", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmstatus", 2);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", "");
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", "");
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmramount", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmraccount", "");
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmeamount", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmeaccount", "");
							
							String accname = paymentModeMst.getPAYMENTMODE().toUpperCase()+" IN TRANSIT";
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pdaccount", accname);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pdamount", 0);
						}
					}
					resultJsonInt.put("shiftstatus", posShiftAmountHdr.getPayReciveStatus());
					
					
					String expamt = movHisUtil.getexpdrawerampunt(PLANT, posShiftAmountHdr.getShiftId());
					String draamt = movHisUtil.getdrawerampunt(PLANT, posShiftAmountHdr.getShiftId());
					
					//double se = Double.valueOf(expamt) - Double.valueOf(draamt);
					double se = Double.valueOf(draamt) - Double.valueOf(expamt);
					String shortextra = StrUtils.addZeroes(se, numberOfDecimal);
					/*if(Double.valueOf(expamt) > Double.valueOf(draamt)){
						shortextra = "-"+shortextra;
					}*/
					
					resultJsonInt.put("shiftdiffval",shortextra);
					
					double adv = diffvalue-expvalue;
					if(expvalue == diffvalue) {
						resultJsonInt.put("actdiffval","0");
					}else if(expvalue > diffvalue){
						resultJsonInt.put("actdiffval",StrUtils.addZeroes(adv, numberOfDecimal));
					}else {
						resultJsonInt.put("actdiffval","+"+StrUtils.addZeroes(adv, numberOfDecimal));
					}
					
					
					jsonArray.add(resultJsonInt);

				}
				resultJson.put("POS", jsonArray);
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
				resultJson.put("POS", jsonArray);

				resultJson.put("errors", jsonArrayErr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonArray.add("");
			resultJson.put("POS", jsonArray);
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	
	public JSONObject receivepayment(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("mpayid"));
		String isedit = StrUtils.fString(request.getParameter("medit"));
		String rvamount = StrUtils.fString(request.getParameter("mamtrecived"));
		String rvaccount = StrUtils.fString(request.getParameter("mraccount"));
		String eamount = StrUtils.fString(request.getParameter("mvamount"));
		String eaccount = StrUtils.fString(request.getParameter("mvaccount"));
		String pdamount = StrUtils.fString(request.getParameter("pdamount"));
		String pdaccount = StrUtils.fString(request.getParameter("pdaccount"));
		
		

		UserTransaction ut = null;
		CoaUtil coaUtil = new CoaUtil();
		CoaDAO coaDAO = new CoaDAO();
		DateUtils dateutils = new DateUtils();
		POSAmountByPayModeDAO pOSAmountByPayModeDAO = new POSAmountByPayModeDAO();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			POSAmountByPayMode pOSAmountByPayMode = pOSAmountByPayModeDAO.getbyid(plant,Integer.valueOf(id));
			
			if(pOSAmountByPayMode.getRecivedStatus() == 1) {
				
				pOSAmountByPayMode.setRecivedAmount(Double.valueOf(rvamount));
				pOSAmountByPayMode.setReceivedAccount(rvaccount);
				pOSAmountByPayMode.setExpenses(Double.valueOf(eamount));
				pOSAmountByPayMode.setExpensesAccount(eaccount);
				pOSAmountByPayMode.setPosDiffAccount(pdaccount);
				pOSAmountByPayMode.setPosDiffAmount(Double.valueOf(pdamount));
				pOSAmountByPayMode.setRecivedStatus(1);
				
				pOSAmountByPayModeDAO.updatepaymode(pOSAmountByPayMode, username);
			}else {
				pOSAmountByPayMode.setRecivedAmount(Double.valueOf(rvamount));
				pOSAmountByPayMode.setReceivedAccount(rvaccount);
				pOSAmountByPayMode.setExpenses(Double.valueOf(eamount));
				pOSAmountByPayMode.setExpensesAccount(eaccount);
				pOSAmountByPayMode.setPosDiffAccount(pdaccount);
				pOSAmountByPayMode.setPosDiffAmount(Double.valueOf(pdamount));
				pOSAmountByPayMode.setRecivedStatus(1);
				
				pOSAmountByPayModeDAO.updatepaymode(pOSAmountByPayMode, username);
				
				List<POSAmountByPayMode>  pOSAmountByPayModeList = pOSAmountByPayModeDAO.getbyhdridandstatus(plant, pOSAmountByPayMode.getHdrId(), 0);
				if(pOSAmountByPayModeList.size() == 0) {
					POSShiftAmountHdr pOSShiftAmountHdr = pOSShiftAmountHdrDAO.getbyid(plant, pOSAmountByPayMode.getHdrId());
					pOSShiftAmountHdr.setPayReciveStatus(1);
					pOSShiftAmountHdrDAO.updatebyid(pOSShiftAmountHdr, username);
				}
			}
			
			/*if (accountCreated) {
				if (Double.parseDouble(accountBalance) != 0) {
					String mainid = coaDAO.GetMainAccountId(accountType, plant);
					int mid = Integer.valueOf(mainid);
					if (mid == 1 || mid == 2 || mid == 3) {
						// String curDate = dateutils.getDate();
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
								.trim();
						// Journal Entry
						JournalHeader journalHead = new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(accountBalanceDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("OPENINGBALANCE");
						journalHead.setTRANSACTION_ID(Integer.toString(0));
						journalHead.setSUB_TOTAL(Double.parseDouble(accountBalance));
						journalHead.setTOTAL_AMOUNT(Double.parseDouble(accountBalance));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);

						List<JournalDetail> journalDetails = new ArrayList<>();

						if (mid == 1) {
							JournalDetail journalDetail = new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson = coaDAO.getCOAByName(plant, accountName);
							System.out.println("Json" + coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(accountName);
							journalDetail.setDEBITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail);

							JournalDetail journalDetail1 = new JournalDetail();
							journalDetail1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, "Opening Balance Equity");
							System.out.println("Json" + coaJson1.toString());
							journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail1.setACCOUNT_NAME("Opening Balance Equity");
							journalDetail1.setCREDITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail1);
						} else {
							JournalDetail journalDetail = new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson = coaDAO.getCOAByName(plant, accountName);
							System.out.println("Json" + coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(accountName);
							journalDetail.setCREDITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail);

							JournalDetail journalDetail1 = new JournalDetail();
							journalDetail1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, "Opening Balance Equity");
							System.out.println("Json" + coaJson1.toString());
							journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail1.setACCOUNT_NAME("Opening Balance Equity");
							journalDetail1.setDEBITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail1);
						}

						Journal journal = new Journal();
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						JournalService journalService = new JournalEntry();
						journalService.addJournal(journal, username);
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}

				}
			}*/
			
			
			DbBean.CommitTran(ut);
			resultJson.put("MESSAGE", "Paymnet Received successfully");
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("MESSAGE", "Paymnet Not Received");
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	public JSONObject postreceivepayment(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("sid"));

		UserTransaction ut = null;
		CoaUtil coaUtil = new CoaUtil();
		CoaDAO coaDAO = new CoaDAO();
		DateUtils dateutils = new DateUtils();
		POSAmountByPayModeDAO pOSAmountByPayModeDAO = new POSAmountByPayModeDAO();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			List<POSAmountByPayMode>  pOSAmountByPayModeList = pOSAmountByPayModeDAO.getbyhdrilist(plant, Integer.valueOf(id));
			POSShiftAmountHdr pOSShiftAmountHdr = pOSShiftAmountHdrDAO.getbyid(plant, Integer.valueOf(id));
			pOSShiftAmountHdr.setPayReciveStatus(2);
			Double totalreceived = 0.0;
			for (POSAmountByPayMode posAmountByPayMode : pOSAmountByPayModeList) {
				totalreceived = totalreceived + posAmountByPayMode.getRecivedAmount();
			}
			pOSShiftAmountHdr.setTotalReceivedAmount(totalreceived);
			pOSShiftAmountHdrDAO.updatebyid(pOSShiftAmountHdr, username);
			
			
			
			
			for (POSAmountByPayMode posAmountByPayMode : pOSAmountByPayModeList) {
				
				String terminalname = outletBeanDAO.getOutletTerminalname(plant, pOSShiftAmountHdr.getOutlet(), pOSShiftAmountHdr.getTerminal());
				String jdesc = pOSShiftAmountHdr.getTerminal()+"-"+terminalname+"-"+new DateUtils().getDateFormatyyyyMMdd()+"-"+posAmountByPayMode.getPaymentMode();
				
				String curDate = dateutils.getDate();
				JournalHeader journalHead = new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(curDate);
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE(posAmountByPayMode.getPaymentMode());
				journalHead.setCURRENCYID(posAmountByPayMode.getCurrencyId());
				journalHead.setTRANSACTION_TYPE("POS_RECEIVE");
				journalHead.setTRANSACTION_ID(String.valueOf(posAmountByPayMode.getID()));
				journalHead.setSUB_TOTAL(posAmountByPayMode.getAmount());
				journalHead.setTOTAL_AMOUNT(posAmountByPayMode.getAmount());
				journalHead.setCRAT(dateutils.getDateTime());
				journalHead.setCRBY(username);
				
				List<JournalDetail> journalDetails = new ArrayList<>();
				
				JournalDetail journalDetail1 = new JournalDetail();
				journalDetail1.setPLANT(plant);
				String accname = posAmountByPayMode.getPaymentMode().toUpperCase()+" IN TRANSIT";
				JSONObject coaJson1 = coaDAO.getCOAByName(plant, accname);
				System.out.println("Json" + coaJson1.toString());
				journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
				journalDetail1.setACCOUNT_NAME(accname);
				if(posAmountByPayMode.getPosDiffAmount() > 0) {
					journalDetail1.setCREDITS(posAmountByPayMode.getAmount()+posAmountByPayMode.getPosDiffAmount());
					journalDetail1.setDEBITS(posAmountByPayMode.getPosDiffAmount());
				}else {
					journalDetail1.setCREDITS(posAmountByPayMode.getAmount());
				}
				journalDetail1.setDESCRIPTION(jdesc);
				journalDetail1.setBANKDATE(pOSShiftAmountHdr.getShiftDate());
				journalDetails.add(journalDetail1);
				
				if(posAmountByPayMode.getRecivedAmount() > 0) {
					JournalDetail journalDetail = new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson = coaDAO.getCOAByName(plant, posAmountByPayMode.getReceivedAccount());
					System.out.println("Json" + coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME(posAmountByPayMode.getReceivedAccount());
					journalDetail.setDEBITS(posAmountByPayMode.getRecivedAmount());
					journalDetail.setDESCRIPTION(jdesc);
					journalDetail.setBANKDATE(pOSShiftAmountHdr.getShiftDate());
					journalDetails.add(journalDetail);
				}
				
				if(posAmountByPayMode.getExpenses() > 0) {
					JournalDetail journalDetail = new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson = coaDAO.getCOAByName(plant, posAmountByPayMode.getExpensesAccount());
					System.out.println("Json" + coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME(posAmountByPayMode.getExpensesAccount());
					journalDetail.setDEBITS(posAmountByPayMode.getExpenses());
					journalDetail.setDESCRIPTION(jdesc);
					journalDetail.setBANKDATE(pOSShiftAmountHdr.getShiftDate());
					journalDetails.add(journalDetail);
				}
				
				if(posAmountByPayMode.getPosDiffAmount() > 0) {
					JournalDetail journalDetail = new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson = coaDAO.getCOAByName(plant, posAmountByPayMode.getExpensesAccount());
					System.out.println("Json" + coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME(posAmountByPayMode.getExpensesAccount());
					journalDetail.setCREDITS(posAmountByPayMode.getPosDiffAmount());
					journalDetail.setDESCRIPTION(jdesc);
					journalDetail.setBANKDATE(pOSShiftAmountHdr.getShiftDate());
					journalDetails.add(journalDetail);
				}
				
				Journal journal = new Journal();
				journal.setJournalHeader(journalHead);
				journal.setJournalDetails(journalDetails);
				JournalService journalService = new JournalEntry();
				journalService.addJournal(journal, username);
				MovHisDAO movHisDao = new MovHisDAO();
				Hashtable jhtMovHis = new Hashtable();
				jhtMovHis.put(IDBConstants.PLANT, plant);
				jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
				jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
				jhtMovHis.put(IDBConstants.ITEM, "");
				jhtMovHis.put(IDBConstants.QTY, "0.0");
				jhtMovHis.put("RECID", "");
				jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
				jhtMovHis.put(IDBConstants.CREATED_BY, username);		
				jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				jhtMovHis.put("REMARKS","");
				movHisDao.insertIntoMovHis(jhtMovHis);
			}

			
			DbBean.CommitTran(ut);
			resultJson.put("MESSAGE", "Post Received Paymnet Successfully");
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("MESSAGE", "Received Paymnet Not Posted");
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	public JSONObject posrecivedjournal(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("sid"));

		UserTransaction ut = null;
		DateUtils dateutils = new DateUtils();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			POSShiftAmountHdr pOSShiftAmountHdr = pOSShiftAmountHdrDAO.getbyid(plant, Integer.valueOf(id));
			String scname = employeeDAO.getEmpnameByempid(plant, pOSShiftAmountHdr.getCRBY(), "");
			pOSShiftAmountHdr.setCRBY(scname);
			
			JournalService journalService = new JournalEntry();
			List<JournalDetail> jdetail = journalService.getJournalDetailsBySalesRecipt(plant, Integer.valueOf(id));

			DbBean.CommitTran(ut);
			resultJson.put("JOURNAL", jdetail);
			resultJson.put("SHIFTHDR", pOSShiftAmountHdr);
			if(jdetail.size() > 0) {
				resultJson.put("DETSTATUS", "1");
				String detname = jdetail.get(0).getCRBY();
				String detdatetime = dateutils.parsecratDate(jdetail.get(0).getCRAT());
				resultJson.put("DETNAME",detname);
				resultJson.put("DETDATETIME", detdatetime);
			}else {
				resultJson.put("DETSTATUS", "0");
			}
			
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	public JSONObject posbankinjournal(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("sid"));

		UserTransaction ut = null;
		DateUtils dateutils = new DateUtils();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			POSShiftAmountHdr pOSShiftAmountHdr = pOSShiftAmountHdrDAO.getbyid(plant, Integer.valueOf(id));
			String scname = employeeDAO.getEmpnameByempid(plant, pOSShiftAmountHdr.getCRBY(), "");
			pOSShiftAmountHdr.setCRBY(scname);
			
			JournalService journalService = new JournalEntry();
			List<JournalDetail> jdetail = journalService.getJournalDetailsBySalesBankin(plant, Integer.valueOf(id));

			DbBean.CommitTran(ut);
			resultJson.put("JOURNAL", jdetail);
			resultJson.put("SHIFTHDR", pOSShiftAmountHdr);
			if(jdetail.size() > 0) {
				resultJson.put("DETSTATUS", "1");
				String detname = jdetail.get(0).getCRBY();
				String detdatetime = dateutils.parsecratDate(jdetail.get(0).getCRAT());
				resultJson.put("DETNAME",detname);
				resultJson.put("DETDATETIME", detdatetime);
			}else {
				resultJson.put("DETSTATUS", "0");
			}
			
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	
	private JSONObject getsalesbankinreport(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		HTReportUtil movHisUtil = new HTReportUtil();
		try {

			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
			String TERMINAL = StrUtils.fString(request.getParameter("TERMINALNAME"));
			String TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			String STATUS = StrUtils.fString(request.getParameter("STATUS"));
			String OUTLET = StrUtils.fString(request.getParameter("OUTLET_NAME"));
			String OUTLETCODE = StrUtils.fString(request.getParameter("OUTLET_CODE"));

			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();

			POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
			POSAmountByPayModeDAO pOSAmountByPayModeDAO = new POSAmountByPayModeDAO();
			PaymentModeMstDAO paymentModeMstDAO = new PaymentModeMstDAO();
			OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
			POSBankInByPayModeDAO pOSBankInByPayModeDAO = new POSBankInByPayModeDAO();
			
			List<PaymentModeMst> paymentModeMstList = paymentModeMstDAO.getAllPaymentModeMst(PLANT);
			List<POSShiftAmountHdr> pOSShiftAmountHdrList = pOSShiftAmountHdrDAO.getbyotds(PLANT, "2", OUTLETCODE,
					TERMINALCODE, FROM_DATE, TO_DATE);
			if (pOSShiftAmountHdrList.size() > 0) {
				for (POSShiftAmountHdr posShiftAmountHdr : pOSShiftAmountHdrList) {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("sid", posShiftAmountHdr.getID());
					resultJsonInt.put("shiftdate", posShiftAmountHdr.getShiftDate());
					resultJsonInt.put("shifttime", posShiftAmountHdr.getShiftTime());
					resultJsonInt.put("hdrstatus", posShiftAmountHdr.getPayReciveStatus());
					resultJsonInt.put("outlet", posShiftAmountHdr.getOutlet());
					String outletname = outletBeanDAO.getOutletname(PLANT, posShiftAmountHdr.getOutlet(), "");
					resultJsonInt.put("outletname", outletname);
					resultJsonInt.put("terminal", posShiftAmountHdr.getTerminal());
					String terminalname = outletBeanDAO.getOutletTerminalname(PLANT, posShiftAmountHdr.getOutlet(), posShiftAmountHdr.getTerminal());
					resultJsonInt.put("terminalname", terminalname);
					resultJsonInt.put("employee", posShiftAmountHdr.getEmployeeID());
					resultJsonInt.put("expenses", posShiftAmountHdr.getExpenses());
					resultJsonInt.put("shifttoatal", posShiftAmountHdr.getTotalReceivedAmount());
					for (PaymentModeMst paymentModeMst : paymentModeMstList) {
						List<POSAmountByPayMode> pOSAmountByPayModelist = pOSAmountByPayModeDAO
								.getbyhdridandpaymodelist(PLANT, posShiftAmountHdr.getID(),
										paymentModeMst.getPAYMENTMODE());
						if (pOSAmountByPayModelist.size() > 0) {
							POSAmountByPayMode pOSAmountByPayMode = pOSAmountByPayModeDAO.getbyhdridandpaymode(PLANT,
									posShiftAmountHdr.getID(), paymentModeMst.getPAYMENTMODE());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode(), pOSAmountByPayMode.getAmount());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"restatus", pOSAmountByPayMode.getRecivedStatus());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmid", pOSAmountByPayMode.getID());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmstatus", pOSAmountByPayMode.getRecivedStatus());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmramount", pOSAmountByPayMode.getRecivedAmount());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmraccount", pOSAmountByPayMode.getReceivedAccount());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmeamount", pOSAmountByPayMode.getExpenses());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmeaccount", pOSAmountByPayMode.getExpensesAccount());
							resultJsonInt.put(pOSAmountByPayMode.getPaymentMode()+"pmbankinstatus", pOSAmountByPayMode.getBankinStatus());
							
							
							//POSAmountByPayMode pOSAmountByPayModeForCoa = pOSAmountByPayModeDAO.getbypaymodeandmaxid(PLANT,paymentModeMst.getPAYMENTMODE());
							POSBankInByPayMode pOSBankInByPayMode = pOSBankInByPayModeDAO.getbypaymodeandmaxid(PLANT, paymentModeMst.getPAYMENTMODE());
							if(pOSBankInByPayMode == null) {
								resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", "");
								resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", "");
							}else {
								if(pOSBankInByPayMode.getBANKINACCOUNT() == null) {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", "");
								}else {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", pOSBankInByPayMode.getBANKINACCOUNT());
								}
								if(pOSBankInByPayMode.getCHARGESACCOUNT() == null) {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", "");
								}else {
									resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", pOSBankInByPayMode.getCHARGESACCOUNT());
								}
							}
							
						} else {
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE(), 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"restatus", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmid", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmstatus", 2);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"raccount", "");
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"eaccount", "");
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmramount", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmraccount", "");
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmeamount", 0);
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmeaccount", "");
							resultJsonInt.put(paymentModeMst.getPAYMENTMODE()+"pmbankinstatus", 0);
						}
					}

					jsonArray.add(resultJsonInt);

				}
				resultJson.put("POS", jsonArray);
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
				resultJson.put("POS", jsonArray);

				resultJson.put("errors", jsonArrayErr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonArray.add("");
			resultJson.put("POS", jsonArray);
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	
	public JSONObject bankinreceivepayment(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("bipayid"));
		String bdate = StrUtils.fString(request.getParameter("depositedate"));
		String paymode = StrUtils.fString(request.getParameter("bipaymode"));
		String bamount = StrUtils.fString(request.getParameter("biramount"));
		String baccount = StrUtils.fString(request.getParameter("biraccount"));
		String cpvalue = StrUtils.fString(request.getParameter("bicper"));
		String camount = StrUtils.fString(request.getParameter("bicamt"));
		String caccount= StrUtils.fString(request.getParameter("bicaccount"));
		

		UserTransaction ut = null;
		CoaUtil coaUtil = new CoaUtil();
		CoaDAO coaDAO = new CoaDAO();
		DateUtils dateutils = new DateUtils();
		POSAmountByPayModeDAO pOSAmountByPayModeDAO = new POSAmountByPayModeDAO();
		POSBankInByPayModeDAO pOSBankInByPayModeDAO = new POSBankInByPayModeDAO();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			POSAmountByPayMode pOSAmountByPayMode = pOSAmountByPayModeDAO.getbyid(plant,Integer.valueOf(id));
			POSShiftAmountHdr pOSShiftAmountHdr = pOSShiftAmountHdrDAO.getbyid(plant, pOSAmountByPayMode.getHdrId());
			boolean bankinstatus = pOSBankInByPayModeDAO.getbypaymodeandhdrid(plant, paymode, pOSShiftAmountHdr.getID());
			if(bankinstatus) {
			
				POSBankInByPayMode pOSBankInByPayMode = new POSBankInByPayMode();
				
				pOSBankInByPayMode.setPLANT(plant);
				pOSBankInByPayMode.setHDRID(pOSShiftAmountHdr.getID());
				pOSBankInByPayMode.setBANKINDATE(bdate);
				pOSBankInByPayMode.setPAYMENTMODE(paymode);
				pOSBankInByPayMode.setBANKINAMOUNT(Double.valueOf(bamount));
				pOSBankInByPayMode.setBANKINACCOUNT(baccount);
				pOSBankInByPayMode.setCHARGESPERCENTAGE(Double.valueOf(cpvalue));
				pOSBankInByPayMode.setCHARGESAMOUNT(Double.valueOf(camount));
				pOSBankInByPayMode.setCHARGESACCOUNT(caccount);
				pOSBankInByPayMode.setCRBY(username);
				
				int hid = pOSBankInByPayModeDAO.addBankInPaymode(pOSBankInByPayMode);
	
				
				String terminalname = outletBeanDAO.getOutletTerminalname(plant, pOSShiftAmountHdr.getOutlet(), pOSShiftAmountHdr.getTerminal());
				terminalname = terminalname.replaceAll("\\s", "");
				terminalname = terminalname.substring(0, Math.min(terminalname.length(), 6));
				String jdesc = pOSShiftAmountHdr.getTerminal()+"-"+terminalname+"-"+paymode;
	
				String curDate = dateutils.getDate();
				JournalHeader journalHead = new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(curDate);
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE(paymode);
				journalHead.setCURRENCYID(pOSAmountByPayMode.getCurrencyId());
				journalHead.setTRANSACTION_TYPE("POS_BANK_IN");
				journalHead.setTRANSACTION_ID(String.valueOf(hid));
				journalHead.setSUB_TOTAL(pOSAmountByPayMode.getRecivedAmount());
				journalHead.setTOTAL_AMOUNT(pOSAmountByPayMode.getRecivedAmount());
				journalHead.setCRAT(dateutils.getDateTime());
				journalHead.setCRBY(username);
				
				List<JournalDetail> journalDetails = new ArrayList<>();
				
				JournalDetail journalDetail1 = new JournalDetail();
				journalDetail1.setPLANT(plant);
				String accname = pOSAmountByPayMode.getReceivedAccount();
				JSONObject coaJson1 = coaDAO.getCOAByName(plant, accname);
				System.out.println("Json" + coaJson1.toString());
				journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
				journalDetail1.setACCOUNT_NAME(accname);
				journalDetail1.setCREDITS(pOSAmountByPayMode.getRecivedAmount());
				journalDetail1.setDESCRIPTION(jdesc);
				journalDetail1.setBANKDATE(bdate);
				journalDetails.add(journalDetail1);
				
				if(Double.valueOf(bamount) > 0) {
					JournalDetail journalDetail = new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson = coaDAO.getCOAByName(plant, baccount);
					System.out.println("Json" + coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME(baccount);
					journalDetail.setDEBITS(Double.valueOf(bamount));
					journalDetail.setDESCRIPTION(jdesc);
					journalDetail.setBANKDATE(bdate);
					journalDetails.add(journalDetail);
				}
				
				if(Double.valueOf(camount) > 0) {
					JournalDetail journalDetail = new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson = coaDAO.getCOAByName(plant, caccount);
					System.out.println("Json" + coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME(caccount);
					journalDetail.setDEBITS(Double.valueOf(camount));
					journalDetail.setDESCRIPTION(jdesc);
					journalDetail.setBANKDATE(bdate);
					journalDetails.add(journalDetail);
				}
				
				Journal journal = new Journal();
				journal.setJournalHeader(journalHead);
				journal.setJournalDetails(journalDetails);
				JournalService journalService = new JournalEntry();
				journalService.addJournal(journal, username);
				MovHisDAO movHisDao = new MovHisDAO();
				Hashtable jhtMovHis = new Hashtable();
				jhtMovHis.put(IDBConstants.PLANT, plant);
				jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
				jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
				jhtMovHis.put(IDBConstants.ITEM, "");
				jhtMovHis.put(IDBConstants.QTY, "0.0");
				jhtMovHis.put("RECID", "");
				jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
				jhtMovHis.put(IDBConstants.CREATED_BY, username);		
				jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				jhtMovHis.put("REMARKS","");
				movHisDao.insertIntoMovHis(jhtMovHis);
			
				
				pOSAmountByPayMode.setBankinId(hid);
				pOSAmountByPayMode.setBankinStatus(1);
				pOSAmountByPayModeDAO.updatepaymode(pOSAmountByPayMode, username);
				
				DbBean.CommitTran(ut);
				resultJson.put("MESSAGE", "Paymnet banked in successfully");
				resultJson.put("STATUS", "SUCCESS");
			}else {
				DbBean.CommitTran(ut);
				resultJson.put("MESSAGE", "Paymnet alredy banked deposited");
				resultJson.put("STATUS", "SUCCESS");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("MESSAGE", "Paymnet Not deposited");
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	public JSONObject bankincashreceivepayment(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("payidlist"));
		String bdate = StrUtils.fString(request.getParameter("depositedate"));
		String tamount = StrUtils.fString(request.getParameter("biamount"));
		String bamount = StrUtils.fString(request.getParameter("biramount"));
		String baccount = StrUtils.fString(request.getParameter("biraccount"));
		String cpvalue = StrUtils.fString(request.getParameter("bicper"));
		String camount = StrUtils.fString(request.getParameter("bicamt"));
		String caccount= StrUtils.fString(request.getParameter("bicaccount"));
		
		UserTransaction ut = null;
		CoaUtil coaUtil = new CoaUtil();
		CoaDAO coaDAO = new CoaDAO();
		DateUtils dateutils = new DateUtils();
		POSAmountByPayModeDAO pOSAmountByPayModeDAO = new POSAmountByPayModeDAO();
		POSBankInByPayModeDAO pOSBankInByPayModeDAO = new POSBankInByPayModeDAO();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
		POSAmountByPayMode pOSAmountByPayMode = new POSAmountByPayMode();
		String[] payids = id.split(",");
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			for (String s: payids)
			{
			    pOSAmountByPayMode = pOSAmountByPayModeDAO.getbyid(plant,Integer.valueOf(s));
			}
			
			POSBankInByPayMode pOSBankInByPayMode = new POSBankInByPayMode();
			
			pOSBankInByPayMode.setPLANT(plant);
			pOSBankInByPayMode.setBANKINDATE(bdate);
			pOSBankInByPayMode.setPAYMENTMODE(pOSAmountByPayMode.getPaymentMode());
			pOSBankInByPayMode.setBANKINAMOUNT(Double.valueOf(bamount));
			pOSBankInByPayMode.setBANKINACCOUNT(baccount);
			pOSBankInByPayMode.setCHARGESPERCENTAGE(Double.valueOf(cpvalue));
			pOSBankInByPayMode.setCHARGESAMOUNT(Double.valueOf(camount));
			pOSBankInByPayMode.setCHARGESACCOUNT(caccount);
			pOSBankInByPayMode.setCRBY(username);
			
			int hid = pOSBankInByPayModeDAO.addBankInPaymode(pOSBankInByPayMode);
			

			String curDate = dateutils.getDate();
			JournalHeader journalHead = new JournalHeader();
			journalHead.setPLANT(plant);
			journalHead.setJOURNAL_DATE(curDate);
			journalHead.setJOURNAL_STATUS("PUBLISHED");
			journalHead.setJOURNAL_TYPE(pOSAmountByPayMode.getPaymentMode());
			journalHead.setCURRENCYID(pOSAmountByPayMode.getCurrencyId());
			journalHead.setTRANSACTION_TYPE("POS_BANK_IN");
			journalHead.setTRANSACTION_ID(String.valueOf(hid));
			journalHead.setSUB_TOTAL(Double.valueOf(tamount));
			journalHead.setTOTAL_AMOUNT(Double.valueOf(tamount));
			journalHead.setCRAT(dateutils.getDateTime());
			journalHead.setCRBY(username);
			
			List<JournalDetail> journalDetails = new ArrayList<>();
			
			for (String s: payids)
			{
				POSAmountByPayMode pOSAmountByPayModejournal = pOSAmountByPayModeDAO.getbyid(plant,Integer.valueOf(s));
				POSShiftAmountHdr pOSShiftAmountHdr = pOSShiftAmountHdrDAO.getbyid(plant, pOSAmountByPayModejournal.getHdrId());
				String terminalname = outletBeanDAO.getOutletTerminalname(plant, pOSShiftAmountHdr.getOutlet(), pOSShiftAmountHdr.getTerminal());
				String jdesc1 = pOSShiftAmountHdr.getTerminal()+"-"+terminalname+"-"+new DateUtils().getDateFormatyyyyMMdd()+"-"+pOSAmountByPayModejournal.getPaymentMode();
				JournalDetail journalDetail1 = new JournalDetail();
				journalDetail1.setPLANT(plant);
				String accname = pOSAmountByPayModejournal.getReceivedAccount();
				JSONObject coaJson1 = coaDAO.getCOAByName(plant, accname);
				System.out.println("Json" + coaJson1.toString());
				journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
				journalDetail1.setACCOUNT_NAME(accname);
				journalDetail1.setCREDITS(pOSAmountByPayModejournal.getRecivedAmount());
				journalDetail1.setDESCRIPTION(new DateUtils().parseBankinDate(bdate)+"-CSDEP");
				journalDetail1.setBANKDATE(bdate);
				journalDetails.add(journalDetail1);
				pOSAmountByPayModejournal.setBankinId(1);
				pOSAmountByPayModejournal.setBankinId(hid);
				pOSAmountByPayModeDAO.updatepaymode(pOSAmountByPayModejournal, username);
			}
			
			if(Double.valueOf(bamount) > 0) {
				JournalDetail journalDetail = new JournalDetail();
				journalDetail.setPLANT(plant);
				JSONObject coaJson = coaDAO.getCOAByName(plant, baccount);
				System.out.println("Json" + coaJson.toString());
				journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
				journalDetail.setACCOUNT_NAME(baccount);
				journalDetail.setDEBITS(Double.valueOf(bamount));
				journalDetail.setDESCRIPTION(new DateUtils().parseBankinDate(bdate)+"-CSDEP");
				journalDetail.setBANKDATE(bdate);
				journalDetails.add(journalDetail);
			}
			
			if(Double.valueOf(camount) > 0) {
				JournalDetail journalDetail = new JournalDetail();
				journalDetail.setPLANT(plant);
				JSONObject coaJson = coaDAO.getCOAByName(plant, caccount);
				System.out.println("Json" + coaJson.toString());
				journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
				journalDetail.setACCOUNT_NAME(caccount);
				journalDetail.setDEBITS(Double.valueOf(camount));
				journalDetail.setDESCRIPTION(new DateUtils().parseBankinDate(bdate)+"-CSDEP");
				journalDetail.setBANKDATE(bdate);
				journalDetails.add(journalDetail);
			}
			
			Journal journal = new Journal();
			journal.setJournalHeader(journalHead);
			journal.setJournalDetails(journalDetails);
			JournalService journalService = new JournalEntry();
			journalService.addJournal(journal, username);
			MovHisDAO movHisDao = new MovHisDAO();
			Hashtable jhtMovHis = new Hashtable();
			jhtMovHis.put(IDBConstants.PLANT, plant);
			jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
			jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
			jhtMovHis.put(IDBConstants.ITEM, "");
			jhtMovHis.put(IDBConstants.QTY, "0.0");
			jhtMovHis.put("RECID", "");
			jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
			jhtMovHis.put(IDBConstants.CREATED_BY, username);		
			jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			jhtMovHis.put("REMARKS","");
			movHisDao.insertIntoMovHis(jhtMovHis);
		
			
			for (String s: payids)
			{
			    pOSAmountByPayMode = pOSAmountByPayModeDAO.getbyid(plant,Integer.valueOf(s));
			    pOSAmountByPayMode.setBankinId(hid);
			    pOSAmountByPayMode.setBankinStatus(1);
			    pOSAmountByPayModeDAO.updatepaymode(pOSAmountByPayMode, username);
			}
			
			DbBean.CommitTran(ut);
			resultJson.put("MESSAGE", "Paymnet banked in successfully");
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("MESSAGE", "Paymnet Not deposited");
			resultJson.put("STATUS", "FAIL");
		}
		
		return resultJson;
	}
	
	
	private JSONObject getreconciliationreport(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		HTReportUtil movHisUtil = new HTReportUtil();
		try {

			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
			String ACCOUNT = StrUtils.fString(request.getParameter("ACCOUNTNAME"));
			String USERNAME = StrUtils.fString(request.getParameter("LOGIN_USER"));
			
			if (FROM_DATE == null) {
				FROM_DATE = "";
			}else {
				FROM_DATE = FROM_DATE.trim();
			}
			if (TO_DATE == null) {
				TO_DATE = "";
			}else {
				TO_DATE = TO_DATE.trim();
			}

			ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();
			List<ReconciliationHdr> ReconciliationHdrlist = reconciliationHdrDAO.getallbyfilter(PLANT, ACCOUNT, FROM_DATE, TO_DATE);
			
			if (ReconciliationHdrlist.size() > 0) {
				resultJson.put("POS", ReconciliationHdrlist);
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
				resultJson.put("POS", jsonArray);

				resultJson.put("errors", jsonArrayErr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonArray.add("");
			resultJson.put("POS", jsonArray);
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	
	private JSONObject getsalesrecievedmonthreport(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		HTReportUtil movHisUtil = new HTReportUtil();
		try {
			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);

			POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
			POSAmountByPayModeDAO pOSAmountByPayModeDAO = new POSAmountByPayModeDAO();
			PaymentModeMstDAO paymentModeMstDAO = new PaymentModeMstDAO();
			OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
			
			List<PaymentModeMst> paymentModeMstList = paymentModeMstDAO.getAllPaymentModeMst(PLANT);
			List<POSShiftAmountHdr> pOSShiftAmountHdrList = pOSShiftAmountHdrDAO.getbyotdsmonth(PLANT);
			if (pOSShiftAmountHdrList.size() > 0) {
				for (POSShiftAmountHdr posShiftAmountHdr : pOSShiftAmountHdrList) {
					JSONObject resultJsonInt = new JSONObject();
					String[] monthyoear = posShiftAmountHdr.getShiftDate().split("-");
					resultJsonInt.put("smonth", monthyoear[1]);
					resultJsonInt.put("syear", monthyoear[0]);
					resultJsonInt.put("outlet", posShiftAmountHdr.getOutlet());
					String outletname = outletBeanDAO.getOutletname(PLANT, posShiftAmountHdr.getOutlet(), "");
					resultJsonInt.put("outletname", outletname);
					resultJsonInt.put("terminal", posShiftAmountHdr.getTerminal());
					String terminalname = outletBeanDAO.getOutletTerminalname(PLANT, posShiftAmountHdr.getOutlet(), posShiftAmountHdr.getTerminal());
					resultJsonInt.put("terminalname", terminalname);
					resultJsonInt.put("totalamount", StrUtils.addZeroes(posShiftAmountHdr.getTotalSales(), numberOfDecimal));

					for (PaymentModeMst paymentModeMst : paymentModeMstList) {
						/*Calendar cal = Calendar.getInstance();
					    cal.set(Integer.valueOf(monthyoear[1]), Integer.valueOf(monthyoear[0]), 1);
					    int res = cal.getActualMaximum(Calendar.DATE);
					    
					    String scmonth = monthyoear[0];
					    String scyear = monthyoear[1];
					    if(scmonth.length() == 1) {
					    	scmonth="0"+scmonth;
					    } 
					    String pmfdate = "01/"+scmonth+"/"+scyear;
					    String pmtdate = String.valueOf(res)+"/"+scmonth+"/"+scyear;*/

						String paymodeamount = pOSAmountByPayModeDAO.getbymonthpaymode(PLANT, posShiftAmountHdr.getShiftDate(),paymentModeMst.getPAYMENTMODE());
						resultJsonInt.put(paymentModeMst.getPAYMENTMODE(),StrUtils.addZeroes(Double.valueOf(paymodeamount), numberOfDecimal));
							
					}
					jsonArray.add(resultJsonInt);

				}
				resultJson.put("POS", jsonArray);
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
				resultJson.put("POS", jsonArray);

				resultJson.put("errors", jsonArrayErr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonArray.add("");
			resultJson.put("POS", jsonArray);
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
