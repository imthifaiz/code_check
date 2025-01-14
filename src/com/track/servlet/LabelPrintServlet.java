package com.track.servlet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.File;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//import net.sf.jasperreports.engine.export.*;

 //import net.sf.jasperreports.engine.*;


//import com.lowagie.text.pdf.codec.Base64.InputStream;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;

import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;


import com.track.db.util.ItemMstUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
import com.track.db.util.HTReportUtil;
import com.track.db.util.TblControlUtil;
import java.util.Random;
import com.track.dao.LabelPrintDAO;
import com.track.db.util.LabelPrintUtil;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JRDesignReportFont;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.gates.DbBean;
import com.track.gates.selectBean;;
/**
 * Servlet implementation class LabelPrintServlet
 */
public class LabelPrintServlet extends HttpServlet implements IMLogger{
	private static final long serialVersionUID = 1L;
	//private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.LabelPrintServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LabelPrintServlet_PRINTPLANTMASTERINFO;
  	String action = "";
	String xmlStr = "";
	String statusValue="";
	LabelPrintUtil labelPrintUtil=null;
	LabelPrintDAO _LabelPrintDAO=null;
	StrUtils strUtils = null;
	 DateUtils dateUtils = null;
	 MovHisDAO movHisDao = new MovHisDAO();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		strUtils = new StrUtils();
		labelPrintUtil = new LabelPrintUtil();
		_LabelPrintDAO = new LabelPrintDAO();
				
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.setContentType(CONTENT_TYPE);
		//PrintWriter out = response.getOutputStream().w
		
		try {
			action = request.getParameter("action").trim();
			String rflag = StrUtils.fString(request.getParameter("RFLAG"));
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))+ " PDA_USER"));
			labelPrintUtil.setmLogger(mLogger);
						
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("Print")) {
				xmlStr = LabelPrint(request, response);
			}
			
		   if (action.equalsIgnoreCase("ViewReport")) {
				ViewPrintReport(request, response);
								
		    }
						
			if (action.equalsIgnoreCase("PrintProduct")) {
				xmlStr = LabelPrintProduct(request, response);
				
				response.setContentType("text/html");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(xmlStr);
				response.getWriter().flush();
				response.getWriter().close();

			}
			
					
			if (action.equalsIgnoreCase("PrintProductWithBatch")) {
				xmlStr = LabelPrintProductWithBatch(request, response);
				
				response.setContentType("text/html");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(xmlStr);
				response.getWriter().flush();
				response.getWriter().close();
			}
			
			if (action.equalsIgnoreCase("PrintManual")) {
				xmlStr = LabelPrintManual(request, response);
				
				response.setContentType("text/html");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(xmlStr);
				response.getWriter().flush();
				response.getWriter().close();
			}
			
	
			if (action.equalsIgnoreCase("PrintLocation")) {
				xmlStr = LabelPrintLocation(request, response);
				
				response.setContentType("text/html");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(xmlStr);
				response.getWriter().flush();
				response.getWriter().close();
			}
			if (action.equalsIgnoreCase("PrintEmployee")) {
				xmlStr = LabelPrintEmployee(request, response);
				
				response.setContentType("text/html");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(xmlStr);
				response.getWriter().flush();
				response.getWriter().close();
			}
			if (action.equalsIgnoreCase("PrintCatalog")) {
				xmlStr = LabelPrintCatalog(request, response);
				
				response.setContentType("text/html");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(xmlStr);
				response.getWriter().flush();
				response.getWriter().close();
			}
			if (action.equalsIgnoreCase("ChangeLabelSetting")) {
				ChangeLabelSetting(request, response);
				
		    }
			
		 if (action.equalsIgnoreCase("GETLABELPRINTDETAILS")) {
				JSONObject jsonObjectResult = getLabelPrintDetails(request);
				
				response.setContentType("application/json");
				//((ServletRequest) response).setCharacterEncoding("UTF-8");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
								
		    }
			
			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
			 request.getSession().setAttribute("RESULTERROR",e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("[[Class Name : " + this.getClass() + "]]\t");
			requestParams.append("[[Paramter Mapping]]\t");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : "
						+ request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {

		}
	}
  private String LabelPrint(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		boolean flag = false;
		StrUtils StrUtils = new StrUtils();
		Map receiveMaterial_HM = null;
		String  OrderNo = "", Loc = "",Batch="",Item= "",ItemDesc= "",Qty = "",Uom = "",OrderStatus="",Remarks="";
		String LOGIN_USER="",Cname="",LnNO="";
		double pickingQty = 0;
		Boolean allChecked = false,fullPrint = false;
		String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
		String FROM_DATE       = StrUtils.fString(request.getParameter("FROM_DATE"));
		String TO_DATE       = StrUtils.fString(request.getParameter("TO_DATE"));
		String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
		String JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
		String USER          = StrUtils.fString(request.getParameter("USER"));
		String ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
		String BATCH         = StrUtils.fString(request.getParameter("BATCH"));
		String ORDERNO       = StrUtils.fString(request.getParameter("ORDERNO"));
		String CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
		String CUSTOMER_TO   = StrUtils.fString(request.getParameter("CUSTOMER_TO"));
		String CUSTOMER_LO   = StrUtils.fString(request.getParameter("CUSTOMER_LO"));
		String ITEMDESC      = StrUtils.fString(request.getParameter("DESC"));
		String ORDERTYPE	 = StrUtils.fString(request.getParameter("ORDERTYPE"));
		String PRD_TYPE_ID 	 = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
		String PRD_BRAND_ID  = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
		String PRD_CLS_ID 	 = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
		String REASONCODE 	 = StrUtils.fString(request.getParameter("REASONCODE"));
		String LOC 			 = StrUtils.fString(request.getParameter("LOC"));
		String LOC_TYPE_ID   = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
		String MODULENAME	 = StrUtils.fString(request.getParameter("MODULENAME"));
		String PRINTSTATUS	= StrUtils.fString(request.getParameter("PRINTSTATUS"));
		
		request.getSession().setAttribute("RESULT","");
		request.getSession().setAttribute("RESULTERROR","");
	   	request.getSession().setAttribute("FROM_DATE","");
	    //request.getSession().setAttribute("refNo","");
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		Map checkedDOS = new HashMap();
		long randomRefno=0;
		randomRefno=RefNumber();
		String url = "jsp/LabelPrintGoodsIssue.jsp?PGaction=View&PLANT="
                + PLANT
                + "&FROM_DATE="
                + FROM_DATE
                + "&TO_DATE="
                + TO_DATE
                + "&DIRTYPE="
                + DIRTYPE
                + "&MODULENAME="
                + MODULENAME
                + "&REFNO="
                + Long.toString(randomRefno)
                + "&JOBNO="
                + JOBNO
                + "&ITEM="
                + ITEMNO 
                + "&BATCH="
                + BATCH 
                + "&ORDERNO="
                + ORDERNO 
                + "&CUSTOMER="
                + CUSTOMER 
                + "& CUSTOMER_TO="
                +  CUSTOMER_TO 
                + "&CUSTOMER_LO="
                +  CUSTOMER_LO 
                + "&DESC="
                +  ITEMDESC 
                + "&ORDERTYPE="
                + ORDERTYPE 
                + "&PRD_TYPE_ID="
                + PRD_TYPE_ID
                + "&PRD_BRAND_ID="
                + PRD_BRAND_ID
                + "&PRD_CLS_ID="
                + PRD_CLS_ID
                + "&REASONCODE="
                + REASONCODE
                + "&LOC="
                + LOC
                + "&LOC_TYPE_ID=" 
                + LOC_TYPE_ID
				+ "&PRINTSTATUS=" 
				+ PRINTSTATUS;
		try {
			HttpSession session = request.getSession();
            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String[] chkdLnNo= request.getParameterValues("chkdLnNo");
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			process: 	
			if (chkdLnNo != null)    {     
				for (int i = 0; i < chkdLnNo.length; i++)    
				{ 
					String	data = chkdLnNo[i];
				    String[] chkdata = data.split(",,,");
					OrderNo = chkdata[0];
					Cname = strUtils.replaceCharacters2Recv(chkdata[1]);
					Item = chkdata[2];
					ItemDesc = strUtils.replaceCharacters2Recv(chkdata[3]);
					Loc = chkdata[4];
					Batch = chkdata[5];
					Qty = chkdata[6];
					Uom = strUtils.replaceCharacters2Recv(chkdata[7]);
					OrderStatus = chkdata[8];
					Remarks = "";
					LnNO = chkdLnNo[i];
					if (Batch.length() == 0) {
						Batch = "NOBATCH";
					}
					if(OrderStatus.equals("Empty"))
					{
						OrderStatus="";
					}
					receiveMaterial_HM = new HashMap();
					receiveMaterial_HM.put(IConstants.PLANT, PLANT);
					receiveMaterial_HM.put(IConstants.ORDERNO, OrderNo);
					receiveMaterial_HM.put(IConstants.REFNO,Long.toString(randomRefno));
					receiveMaterial_HM.put(IConstants.ITEM, Item);
					receiveMaterial_HM.put(IConstants.ITEM_DESC, itemMstDAO.getItemDesc(PLANT, Item));
					receiveMaterial_HM.put(IConstants.LOC, Loc);
					receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
					receiveMaterial_HM.put(IConstants.BATCH, Batch);
					receiveMaterial_HM.put(IConstants.QTY, Qty);
					receiveMaterial_HM.put(IConstants.REMARKS, Remarks);
					receiveMaterial_HM.put(IConstants.UOMCODE, Uom);
					receiveMaterial_HM.put(IConstants.LABEL_PRINT_TYPE, "GI");
					receiveMaterial_HM.put("ORDERSTATUS", OrderStatus);
					receiveMaterial_HM.put("CNAME", Cname);
					flag = labelPrintUtil.process_LabelPrint(receiveMaterial_HM)&& true;
					//flag=true;
					
					if(!flag)
						break process;
					}
			}
					
			if (flag) {
				
				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put("BATNO", "");
				htRecvHis.put(IDBConstants.PLANT, PLANT);
				htRecvHis.put("DIRTYPE",  "LABEL_PRINT_GI");
				htRecvHis.put(IDBConstants.ITEM, "");
				htRecvHis.put("MOVTID", "");
				htRecvHis.put("RECID", "");
				htRecvHis.put(IDBConstants.LOC, "");
				//htRecvHis.put(IDBConstants.QTY, 0);
				htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
				htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
				flag = movHisDao.insertIntoMovHis(htRecvHis);
				if(!flag)
				{
					throw new Exception();
				}
				request.getSession().setAttribute("refNo",Long.toString(randomRefno));
				request.getSession().setAttribute("ISPOPUP","OPENED");
				response.sendRedirect(url);
				//RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
				//rd.forward(request, response);
			} else {
				
				request.getSession().setAttribute("RESULTERROR","Error in printing details!");
				response.sendRedirect(url);
					
		    	}
			
			}
			
		 catch (Exception e) {
			 this.mLogger.exception(this.printLog, "", e);
			 request.getSession().setAttribute("RESULTERROR",e.getMessage());
			 response.sendRedirect(url);
			 
		}
			
			return xmlStr;
	}
  
			  
		 @SuppressWarnings("deprecation")
		public void  ViewPrintReport(HttpServletRequest request,
		            HttpServletResponse response) throws IOException, Exception {
		    	Connection con = null;
		    	String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();	
		    	String  REFNO=(String)request.getSession().getAttribute("refNo");
		    	 try {
		    	 	 
		    	 	 	
		    		 	String LABELTYPE 			= strUtils.fString(request.getParameter("LABELTYPE"));
		    		 	String BARCODEFONTSIZE    = strUtils.fString(request.getParameter("BARCODEFONTSIZE"));
		    		 	String TEXTFONTSIZE       = strUtils.fString(request.getParameter("TEXTFONTSIZE"));
		    		 	String PRINTTYPE       = strUtils.fString(request.getParameter("PRINTTYPE"));
		    		 	
		    			int iBarCodeFontSize=Integer.parseInt(BARCODEFONTSIZE);
		    		 	int iTextFontSize=Integer.parseInt(TEXTFONTSIZE);
					 
		    		 	//  ----------------------Goods Issue-----------------------------/
		    		    // Type 100X50mm With Batch
		    		 	if (PRINTTYPE.equals("GOODS ISSUE") && LABELTYPE .equals("100mm X 50mm With Batch"))
		    		 	{
		    		 	 	con = DbBean.getConnection(); 
			    		 	String fileName="rptGIWithBatch100X50mm";
			    	 	 	String jasperPath = DbBean.JASPER_INPUT + "/GenerateLabel/" + "rptGIWithBatch100X50mm";
			    	 		Map parameters = new HashMap();
			    	 	 	parameters.put("company", PLANT);
			    	 	 	parameters.put("refNo", REFNO);
			    	 	 	parameters.put("barcodeFontSize",iBarCodeFontSize);
			    	 	 	parameters.put("textFontSize",iTextFontSize);
			    	 		byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
							System.out.println("Byte Value : " + bytes.length);
							response.addHeader("Content-disposition","attachment;filename=" + fileName + ".pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							UpdatePrintStatus(PLANT,REFNO);
							GetLabelDetail(PLANT,REFNO);
		    		 	}
		    		  // Type 100X50mm No Batch
		    		 	if (PRINTTYPE.equals("GOODS ISSUE") && LABELTYPE .equals("100mm X 50mm No Batch"))
		    		 	{
		    		 	 	con = DbBean.getConnection(); 
			    		 	String fileName="rptGINoBatch100X50mm";
			    	 	 	String jasperPath = DbBean.JASPER_INPUT + "/GenerateLabel/" + "rptGINoBatch100X50mm";
			    	 		Map parameters = new HashMap();
			    	 	 	parameters.put("company", PLANT);
			    	 	 	parameters.put("refNo", REFNO);
			    	 	 	parameters.put("barcodeFontSize",iBarCodeFontSize);
			    	 	 	parameters.put("textFontSize",iTextFontSize);
			    	 	 	byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
							System.out.println("Byte Value : " + bytes.length);
							response.addHeader("Content-disposition","attachment;filename=" + fileName + ".pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							UpdatePrintStatus(PLANT,REFNO);
							GetLabelDetail(PLANT,REFNO);
		    		 	}
		    		 	  // Type 100X50mm With Batch
		    		 	if (PRINTTYPE.equals("GOODS ISSUE") && LABELTYPE .equals("100mm X 35mm With Batch"))
		    		 	{
		    		 	 	con = DbBean.getConnection(); 
			    		 	String fileName="rptGIWithBatch100X35mm";
			    	 	 	String jasperPath = DbBean.JASPER_INPUT + "/GenerateLabel/" + "rptGIWithBatch100X35mm";
			    	 		Map parameters = new HashMap();
			    	 	 	parameters.put("company", PLANT);
			    	 	 	parameters.put("refNo", REFNO);
			    	 	 	parameters.put("barcodeFontSize",iBarCodeFontSize);
			    	 	 	parameters.put("textFontSize",iTextFontSize);
			    	 		byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
							System.out.println("Byte Value : " + bytes.length);
							response.addHeader("Content-disposition","attachment;filename=" + fileName + ".pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							UpdatePrintStatus(PLANT,REFNO);
							GetLabelDetail(PLANT,REFNO);
		    		 	}
		    		 	 // Type 100X35mm No Batch
		    		 	if (PRINTTYPE.equals("GOODS ISSUE") && LABELTYPE .equals("100mm X 35mm No Batch"))
		    		 	{
		    		 	 	con = DbBean.getConnection(); 
			    		 	String fileName="rptGINoBatch100X35mm";
			    	 	 	String jasperPath = DbBean.JASPER_INPUT + "/GenerateLabel/" + "rptGINoBatch100X35mm";
			    	 		Map parameters = new HashMap();
			    	 	 	parameters.put("company", PLANT);
			    	 	 	parameters.put("refNo", REFNO);
			    	 	 	parameters.put("barcodeFontSize",iBarCodeFontSize);
			    	 	 	parameters.put("textFontSize",iTextFontSize);
			    	 	 	byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
							System.out.println("Byte Value : " + bytes.length);
							response.addHeader("Content-disposition","attachment;filename=" + fileName + ".pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							UpdatePrintStatus(PLANT,REFNO);
							GetLabelDetail(PLANT,REFNO);
		    		 	}
		    		 	 // Type 50X25mm With Batch
		    		 	if (PRINTTYPE.equals("GOODS ISSUE") && LABELTYPE .equals("50mm X 25mm With Batch"))
		    		 	{
		    		 	 	con = DbBean.getConnection(); 
			    		 	String fileName="rptGIWithBatch50X25mm";
			    	 	 	String jasperPath = DbBean.JASPER_INPUT + "/GenerateLabel/" + "rptGIWithBatch50X25mm";
			    	 		Map parameters = new HashMap();
			    	 	 	parameters.put("company", PLANT);
			    	 	 	parameters.put("refNo", REFNO);
			    	 	 	parameters.put("barcodeFontSize",iBarCodeFontSize);
			    	 	 	parameters.put("textFontSize",iTextFontSize);
			    	 		byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
							System.out.println("Byte Value : " + bytes.length);
							response.addHeader("Content-disposition","attachment;filename=" + fileName + ".pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							UpdatePrintStatus(PLANT,REFNO);
							GetLabelDetail(PLANT,REFNO);
		    		 	}
		    		 // Type 50X25mm No Batch
		    		 	if (PRINTTYPE.equals("GOODS ISSUE") && LABELTYPE .equals("50mm X 25mm No Batch"))
		    		 	{
		    		 	 	con = DbBean.getConnection(); 
			    		 	String fileName="rptGINoBatch50X25mm";
			    	 	 	String jasperPath = DbBean.JASPER_INPUT + "/GenerateLabel/" + "rptGINoBatch50X25mm";
			    	 		Map parameters = new HashMap();
			    	 	 	parameters.put("company", PLANT);
			    	 	 	parameters.put("refNo", REFNO);
			    	 	 	parameters.put("barcodeFontSize",iBarCodeFontSize);
			    	 	 	parameters.put("textFontSize",iTextFontSize);
			    	 	 	byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
							System.out.println("Byte Value : " + bytes.length);
							response.addHeader("Content-disposition","attachment;filename=" + fileName + ".pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							UpdatePrintStatus(PLANT,REFNO);
							GetLabelDetail(PLANT,REFNO);
		    		 	}
		    		 	 // Type 35X15mm No Batch
		    		 	if (PRINTTYPE.equals("GOODS ISSUE") && LABELTYPE .equals("35mm X 15mm No Batch"))
		    		 	{
		    		 	 	con = DbBean.getConnection(); 
			    		 	String fileName="rptGINoBatch35X15mm";
			    	 	 	String jasperPath = DbBean.JASPER_INPUT + "/GenerateLabel/" + "rptGINoBatch35X15mm";
			    	 		Map parameters = new HashMap();
			    	 	 	parameters.put("company", PLANT);
			    	 	 	parameters.put("refNo", REFNO);
			    	 	 	parameters.put("barcodeFontSize",iBarCodeFontSize);
			    	 	 	parameters.put("textFontSize",iTextFontSize);
			    	 	 	byte[] bytes = JasperRunManager.runReportToPdf(jasperPath+ ".jasper", parameters, con);
							System.out.println("Byte Value : " + bytes.length);
							response.addHeader("Content-disposition","attachment;filename=" + fileName + ".pdf");
							response.setContentLength(bytes.length);
							response.getOutputStream().write(bytes);
							response.setContentType("application/pdf");
							UpdatePrintStatus(PLANT,REFNO);
							GetLabelDetail(PLANT,REFNO);
		    		 	}
		    		 	
		    		 	
		    	} catch (IOException e) {

		             e.printStackTrace();

		     } finally {
		    	     
		    	 	 request.getSession().setAttribute("refNo","");
		    	 	 DbBean.closeConnection(con);
		     }
		    	 
		}
		 
		 private JSONObject getLabelPrintDetails(HttpServletRequest request) {
		      JSONObject resultJson = new JSONObject();
		      JSONArray jsonArray = new JSONArray();
		      
			      try {
			    	  
			    	  	String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();	
				    	String  REFNO=(String)request.getSession().getAttribute("refNo");
				    	
					 	LabelPrintUtil _LabelPrintUtil = new LabelPrintUtil();
					 	Hashtable ht=new Hashtable();
						//String extCondData=" and status='C'";
					 	String extCondData="";
					 	
						ht.put("PLANT",PLANT);
						ht.put("REFNO",REFNO);
				 		 ArrayList listQry = _LabelPrintUtil.getLabelDetail(" ordnum,item,itemdesc,loc,batch,isnull(cname,'') cname,orderstatus as status ",ht,extCondData);
					     for(int i =0; i<listQry.size(); i++) {
					         Map m=(Map)listQry.get(i);
					         //UpdatePickIssuePrintStatus(Plant,REFNO,(String)m.get("ordnum"),(String)m.get("item"),(String)m.get("itemdesc"),(String)m.get("loc"),(String)m.get("batch"),(String)m.get("cname"),(String)m.get("status"));
					        
					         
					         JSONObject jsonProd = new JSONObject();
					         
					         jsonProd.put("ITEM", (String)m.get("item"));
					         jsonProd.put("ITEMDESC", (String)m.get("itemdesc"));
					         jsonProd.put("BATCH",(String)m.get("batch"));
					         
					         jsonArray.add(jsonProd);
					         
					         
					     }
					     
					     resultJson.put("items", jsonArray);
					     
					     
					     UpdatePrintStatus(PLANT,REFNO);
						
			      }
					catch (Exception e) {
						
						String s = "";
					
						 //throw e;
					
					}
		      
		      
		      return resultJson;
		  }
		 
		 public void  ChangeLabelSetting(HttpServletRequest request,
		            HttpServletResponse response) throws IOException, Exception {
		 		 
			 boolean flag = false;
			 String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			 String LOGIN_USER=  StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			 String LABELTYPE 			= strUtils.fString(request.getParameter("LABELTYPE"));
			 String BARCODEFONTSIZE    = strUtils.fString(request.getParameter("BARCODEFONTSIZE"));
			 String TEXTFONTSIZE       = strUtils.fString(request.getParameter("TEXTFONTSIZE"));
			 String PRINTTYPE       = strUtils.fString(request.getParameter("PRINTTYPE"));
			 String url = "jsp/LabelSettings.jsp?LABELTYPE="
		                + LABELTYPE
		                + "&BARCODEFONTSIZE="
		                + BARCODEFONTSIZE
		                + "&TEXTFONTSIZE=" +TEXTFONTSIZE
		                + "&PRINTTYPE=" +PRINTTYPE;
			 	try {
					 	StringBuffer sql1 = new StringBuffer(" SET ");
					 	sql1.append("LABELTYPE  = '" + LABELTYPE + "', ");
					 	sql1.append("BARCODEFONTSIZE  = '" + BARCODEFONTSIZE + "', ");
					 	sql1.append("TEXTFONTSIZE  = '" + TEXTFONTSIZE + "'");
					 	Hashtable htUpdate= new Hashtable();
					 	htUpdate.clear();
					 	htUpdate.put(IDBConstants.PLANT,PLANT); 
					 	htUpdate.put("PRINTTYPE", PRINTTYPE);
					 	flag = _LabelPrintDAO.updateLabelSetting(sql1.toString(), htUpdate, "");
					 	if(flag)
					 	{
					 		Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put("BATNO", "");
							htRecvHis.put(IDBConstants.PLANT, PLANT);
							htRecvHis.put("DIRTYPE", "UPD_LABEL_SETTING_GI");
							htRecvHis.put(IDBConstants.ITEM, "");
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.LOC, "");
						//	htRecvHis.put(IDBConstants.QTY, 0);
							htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
							flag = movHisDao.insertIntoMovHis(htRecvHis);
							
							if(!flag)
							{
								throw new Exception();
							}
							
					 		request.getSession().setAttribute("RESULT","Change setting successfully!");
					 		response.sendRedirect(url);
					 	}
					 	else
					 	{
					 		request.getSession().setAttribute("RESULTERROR","Error in change setting!");
					 		response.sendRedirect(url);
					 	}
				
			 } catch (Exception e) {

				 request.getSession().setAttribute("RESULTERROR","Error in change setting:" +e.getMessage());
				 response.sendRedirect(url);

		    }
				
		 }
		 
		 public void  UpdatePrintStatus(String Plant,String REFNO) throws IOException, Exception {
			 boolean flag = false;
			 	try {
			 		    String strStatus="C";
					 	StringBuffer sql1 = new StringBuffer(" SET ");
					 	sql1.append("STATUS  = '" + strStatus + "' ");
					 	Hashtable htUpdate= new Hashtable();
					 	htUpdate.clear();
					 	htUpdate.put(IDBConstants.PLANT,Plant); 
					 	htUpdate.put("REFNO", REFNO);
					 	flag = _LabelPrintDAO.updateLabelType(sql1.toString(), htUpdate, "");
					 	if(!flag)
						{
							throw new Exception();
						}
					 
			 	}
			 catch (Exception e) {

				 throw e;

		    }
				
		 }
		 
		 public void  GetLabelDetail(String Plant,String REFNO) throws IOException, Exception {
			 boolean flag = false;
			 	try {
			 		 	LabelPrintUtil _LabelPrintUtil = new LabelPrintUtil();
			 		 	Hashtable ht=new Hashtable();
			 			String extCondData=" and status='C'";
			 			ht.put("PLANT",Plant);
			 			ht.put("REFNO",REFNO);
				 		 ArrayList listQry = _LabelPrintUtil.getLabelDetail(" ordnum,item,itemdesc,loc,batch,isnull(cname,'') cname,orderstatus as status ",ht,extCondData);
					     for(int i =0; i<listQry.size(); i++) {
					         Map m=(Map)listQry.get(i);
					         UpdatePickIssuePrintStatus(Plant,REFNO,(String)m.get("ordnum"),(String)m.get("item"),(String)m.get("itemdesc"),(String)m.get("loc"),(String)m.get("batch"),(String)m.get("cname"),(String)m.get("status"));
					        
					     }
						
			 	}
			 catch (Exception e) {

				 throw e;

		    }
				
		 }
			//sql_Transfer.append(" group by tono,item,itemdesc,loc,batch,cname,status"); 
		 public void UpdatePickIssuePrintStatus(String Plant,String REFNO,String ordnum,String item,String itemdesc,String loc,String batch,String cname,String status) throws IOException, Exception {
			 boolean flag = false;
			 boolean exitFlag=false; 
			 	try {
			 		 
			 			String tablename="";
			 		    String strStatus="C";
					 	StringBuffer sql1 = new StringBuffer(" SET ");
					 	sql1.append("PRINTSTATUS  = '" + strStatus + "' ");
					 	Hashtable ht= new Hashtable();
					 	ht.clear();
					 	ht.put("ITEM", item);
					 	ht.put("ITEMDESC", itemdesc);
					 	ht.put("LOC", loc);
					 	ht.put("BATCH", batch);
					 	ht.put("CNAME", cname);
					 	if(status.length() > 0) {ht.put("STATUS", status);}
					 	
					    // to transfer order
					    exitFlag=_LabelPrintDAO.isExisit(ht,Plant,"TO_PICK"," TONO='" + ordnum + "'");
					    if(exitFlag==true)
					    {
					    	ht.clear();
					    	ht.put("PLANT", Plant);
					    	ht.put("TONO", ordnum);
						 	ht.put("ITEM", item);
						 	ht.put("ITEMDESC", itemdesc);
						 	ht.put("LOC", loc);
						 	ht.put("BATCH", batch);
						 	ht.put("CNAME", cname);
						 	if(status.length() > 0) {ht.put("STATUS", status);}
					    	flag = _LabelPrintDAO.updatePickIssuePrintStatus(sql1.toString(), ht, "","TO_PICK");
						 	if(!flag)
							{
								throw new Exception();
							}
					    }
					    else
					    {
					     // to loan order
						    exitFlag=_LabelPrintDAO.isExisit(ht,Plant,"LOAN_PICK"," ORDNO='" + ordnum + "'");
						    if(exitFlag==true)
						    {
						    	ht.clear();
						    	ht.put("PLANT", Plant);
						    	ht.put("ORDNO", ordnum);
							 	ht.put("ITEM", item);
							 	ht.put("ITEMDESC", itemdesc);
							 	ht.put("LOC", loc);
							 	ht.put("BATCH", batch);
							 	ht.put("CNAME", cname);
							 	if(status.length() > 0) {ht.put("STATUS", status);}
							 	
						    	flag = _LabelPrintDAO.updatePickIssuePrintStatus(sql1.toString(), ht, "","LOAN_PICK");
							 	if(!flag)
								{
									throw new Exception();
								}
						    }
						    else   // to outbound or misc issue
						    {
						    	ht.clear();
						    	ht.put("PLANT", Plant);
						    	ht.put("DONO", ordnum);
							 	ht.put("ITEM", item);
							 	ht.put("ITEMDESC", itemdesc);
							 	ht.put("LOC", loc);
							 	ht.put("BATCH", batch);
							 	ht.put("CNAME", cname);
							 	if(status.length() > 0) {ht.put("STATUS", status);}
						    	flag = _LabelPrintDAO.updatePickIssuePrintStatus(sql1.toString(), ht, "","SHIPHIS");
							 	if(!flag)
								{
									throw new Exception();
								}
						    }
					    
					   }		 	
					 	
					 
			 	}
			 catch (Exception e) {

				 throw e;

		    }
				
		 }
		 
		 private String LabelPrintProduct(HttpServletRequest request,
					HttpServletResponse response) throws ServletException, IOException,
					Exception {
				boolean flag = false;
				StrUtils StrUtils = new StrUtils();
				Map receiveMaterial_HM = null;
				String  OrderNo = "", Loc = "",Batch="",Item= "",ItemDesc= "",Qty = "",Uom = "",OrderStatus="",Remarks="";
				String LOGIN_USER="",Cname="",LnNO="";
				double pickingQty = 0;
				Boolean allChecked = false,fullPrint = false;
				String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
				String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				String USER          = StrUtils.fString(request.getParameter("USER"));
				String ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
				String ITEMDESC      = StrUtils.fString(request.getParameter("DESC"));
				String PRD_TYPE_ID 	 = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_BRAND_ID  = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
				String PRD_CLS_ID 	 = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
				String UOM 	 = StrUtils.fString(request.getParameter("UOM"));
				String PRINTSTATUS	= StrUtils.fString(request.getParameter("PRINTSTATUS"));
				
				request.getSession().setAttribute("RESULT","");
				request.getSession().setAttribute("RESULTERROR","");
			   	request.getSession().setAttribute("FROM_DATE","");
			    //request.getSession().setAttribute("refNo","");
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				Map checkedDOS = new HashMap();
				long randomRefno=0;
				randomRefno=RefNumber();
				String url = "jsp/LabelPrintProduct.jsp?PGaction=View&PLANT="
		                + PLANT
		                 + "&DIRTYPE="
		                + DIRTYPE
		                + "&REFNO="
		                + Long.toString(randomRefno)
		                 + "&ITEM="
		                + ITEMNO 
		                + "&DESC="
		                +  ITEMDESC 
		                + "&PRD_TYPE_ID="
		                + PRD_TYPE_ID
		                + "&PRD_BRAND_ID="
		                + PRD_BRAND_ID
		                + "&PRD_CLS_ID="
		                + PRD_CLS_ID
		                + "&UOM="
		                + UOM
		               
						+ "&PRINTSTATUS=" 
						+ PRINTSTATUS;
				try {
					HttpSession session = request.getSession();
		            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String[] chkdLnNo= request.getParameterValues("chkdLnNo");
					LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
					InvMstDAO invMstDAO = new InvMstDAO();
					invMstDAO.setmLogger(mLogger);
					if( request.getParameter("select")!=null){
						allChecked = true;
					}
					process: 	
					if (chkdLnNo != null)    {     
						for (int i = 0; i < chkdLnNo.length; i++)    
						{ 
							String	data = chkdLnNo[i];
						    String[] chkdata = data.split(",,,");
							Item = chkdata[0];
							ItemDesc = strUtils.replaceCharacters2Recv(chkdata[1]);
							Uom = strUtils.replaceCharacters2Recv(chkdata[2]);
							Remarks = "";
							LnNO = chkdLnNo[i];
							if (Batch.length() == 0) {
								Batch = "NOBATCH";
							}
							if(OrderStatus.equals("Empty"))
							{
								OrderStatus="";
							}
							receiveMaterial_HM = new HashMap();
							receiveMaterial_HM.put(IConstants.PLANT, PLANT);
							receiveMaterial_HM.put(IConstants.ORDERNO, "");
							receiveMaterial_HM.put(IConstants.REFNO,Long.toString(randomRefno));
							receiveMaterial_HM.put(IConstants.ITEM, Item);
							receiveMaterial_HM.put(IConstants.ITEM_DESC, itemMstDAO.getItemDesc(PLANT, Item));
							receiveMaterial_HM.put(IConstants.LOC, "");
							receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
							receiveMaterial_HM.put(IConstants.BATCH, Batch);
							receiveMaterial_HM.put(IConstants.QTY, "0");
							receiveMaterial_HM.put(IConstants.REMARKS, "Product ID & Description");
							receiveMaterial_HM.put(IConstants.UOMCODE, Uom);
							receiveMaterial_HM.put(IConstants.LABEL_PRINT_TYPE, "Label1");
							receiveMaterial_HM.put("ORDERSTATUS", "");
							receiveMaterial_HM.put("CNAME", "");
							flag = labelPrintUtil.process_LabelPrint(receiveMaterial_HM)&& true;
							//flag=true;
							
							if(!flag)
								break process;
							}
					}
							
					if (flag) {
						
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put("BATNO", "");
						htRecvHis.put(IDBConstants.PLANT, PLANT);
						htRecvHis.put("DIRTYPE",  "Label Print");
						htRecvHis.put(IDBConstants.ITEM, Item);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.LOC, "");
						htRecvHis.put(IDBConstants.QTY, "0");
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
						htRecvHis.put(IConstants.REMARKS, "Product ID & Description");
						flag = movHisDao.insertIntoMovHis(htRecvHis);
						if(!flag)
						{
							throw new Exception();
						}
						request.getSession().setAttribute("refNo",Long.toString(randomRefno));
						request.getSession().setAttribute("ISPOPUP","OPENED");
						//response.sendRedirect(url);
						xmlStr = "<script type=\"text/javascript\">window.open('LabelSettings.jsp?PRINTTYPE=1','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')</script>";

						
					} else {
						
						request.getSession().setAttribute("RESULTERROR","Error in printing product and description details!");
						//response.sendRedirect(url);
						xmlStr = "Error in printing details!";

							
				    	}
					
					}
					
				 catch (Exception e) {
					 this.mLogger.exception(this.printLog, "", e);
					 request.getSession().setAttribute("RESULTERROR",e.getMessage());
					 response.sendRedirect(url);
					 
				}
					
					return xmlStr;
			}
		 
		 
		 private String LabelPrintProductWithBatch(HttpServletRequest request,
					HttpServletResponse response) throws ServletException, IOException,
					Exception {
				boolean flag = false;
				StrUtils StrUtils = new StrUtils();
				Map receiveMaterial_HM = null;
				String  OrderNo = "", Loc = "",Batch="",Item= "",ItemDesc= "",Qty = "",Uom = "",OrderStatus="",Remarks="";
				String LOGIN_USER="",Cname="",LnNO="";
				double pickingQty = 0;
				Boolean allChecked = false,fullPrint = false;
				String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
				String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				String USER          = StrUtils.fString(request.getParameter("USER"));
				String ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
				String ITEMDESC      = StrUtils.fString(request.getParameter("DESC"));
				String PRD_TYPE_ID 	 = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_BRAND_ID  = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
				String PRD_CLS_ID 	 = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
				String UOM 	 = StrUtils.fString(request.getParameter("UOM"));
				String PRINTSTATUS	= StrUtils.fString(request.getParameter("PRINTSTATUS"));
				String BATCH         = StrUtils.fString(request.getParameter("BATCH"));
				String LOC 			 = StrUtils.fString(request.getParameter("LOC"));
				String LOC_TYPE_ID   = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
				
				request.getSession().setAttribute("RESULT","");
				request.getSession().setAttribute("RESULTERROR","");
			   	request.getSession().setAttribute("FROM_DATE","");
			    //request.getSession().setAttribute("refNo","");
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				Map checkedDOS = new HashMap();
				long randomRefno=0;
				randomRefno=RefNumber();
				String url = "jsp/LabelPrintProductWithBatch.jsp?PGaction=View&PLANT="
		                + PLANT
		                 + "&DIRTYPE="
		                + DIRTYPE
		                + "&REFNO="
		                + Long.toString(randomRefno)
		                 + "&ITEM="
		                + ITEMNO 
		                + "&DESC="
		                +  ITEMDESC 
		                + "&BATCH="
		                + BATCH 
		                + "&PRD_TYPE_ID="
		                + PRD_TYPE_ID
		                + "&PRD_BRAND_ID="
		                + PRD_BRAND_ID
		                + "&PRD_CLS_ID="
		                + PRD_CLS_ID
		                + "&LOC="
		                + LOC
		                + "&LOC_TYPE_ID=" 
		                + LOC_TYPE_ID
						+ "&PRINTSTATUS=" 
						+ PRINTSTATUS;
				try {
					HttpSession session = request.getSession();
		            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String[] chkdLnNo= request.getParameterValues("chkdLnNo");
					LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
					InvMstDAO invMstDAO = new InvMstDAO();
					invMstDAO.setmLogger(mLogger);
					if( request.getParameter("select")!=null){
						allChecked = true;
					}
					process: 	
					if (chkdLnNo != null)    {     
						for (int i = 0; i < chkdLnNo.length; i++)    
						{ 
							String	data = chkdLnNo[i];
						    String[] chkdata = data.split(",,,");
							Item = chkdata[0];
							ItemDesc = strUtils.replaceCharacters2Recv(chkdata[1]);
							Loc = chkdata[2];
							Batch = chkdata[3];
							Uom = strUtils.replaceCharacters2Recv(chkdata[4]);
							Remarks = "";
							LnNO = chkdLnNo[i];
							if (Batch.length() == 0) {
								Batch = "NOBATCH";
							}
							if(OrderStatus.equals("Empty"))
							{
								OrderStatus="";
							}
							receiveMaterial_HM = new HashMap();
							receiveMaterial_HM.put(IConstants.PLANT, PLANT);
							receiveMaterial_HM.put(IConstants.ORDERNO, "Label2");
							receiveMaterial_HM.put(IConstants.REFNO,Long.toString(randomRefno));
							receiveMaterial_HM.put(IConstants.ITEM, Item);
							receiveMaterial_HM.put(IConstants.ITEM_DESC, itemMstDAO.getItemDesc(PLANT, Item));
							receiveMaterial_HM.put(IConstants.LOC, Loc);
							receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
							receiveMaterial_HM.put(IConstants.BATCH, Batch);
							receiveMaterial_HM.put(IConstants.QTY, "0");
								receiveMaterial_HM.put(IConstants.REMARKS, "Product ID & Description + Batch No");
							receiveMaterial_HM.put(IConstants.UOMCODE, Uom);
							receiveMaterial_HM.put(IConstants.LABEL_PRINT_TYPE, "Label2");
							receiveMaterial_HM.put("ORDERSTATUS", "");
							receiveMaterial_HM.put("CNAME", "");
							flag = labelPrintUtil.process_LabelPrint(receiveMaterial_HM)&& true;
							//flag=true;
							
							if(!flag)
								break process;
							}
					}
							
					if (flag) {
						
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put("BATNO", "");
						htRecvHis.put(IDBConstants.PLANT, PLANT);
						htRecvHis.put("DIRTYPE",  "Label Print");
						htRecvHis.put(IDBConstants.ITEM, Item);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.LOC, Loc);
						htRecvHis.put(IDBConstants.QTY, "0");
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IConstants.REMARKS, "Product ID & Description + Batch No");
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
						flag = movHisDao.insertIntoMovHis(htRecvHis);
						if(!flag)
						{
							throw new Exception();
						}
						request.getSession().setAttribute("refNo",Long.toString(randomRefno));
						request.getSession().setAttribute("ISPOPUP","OPENED");
						xmlStr = "<script type=\"text/javascript\">window.open('LabelSettings.jsp?PRINTTYPE=2','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')</script>";
						//response.sendRedirect(url);
						
					} else {
						
						request.getSession().setAttribute("RESULTERROR","Error in printing product and description details!");
						//response.sendRedirect(url);
						xmlStr = "Error in printing details!";
							
				    	}
					
					}
					
				 catch (Exception e) {
					 this.mLogger.exception(this.printLog, "", e);
					 request.getSession().setAttribute("RESULTERROR",e.getMessage());
					 response.sendRedirect(url);
					 
				}
					
					return xmlStr;
			}
		 
		 private String LabelPrintManual(HttpServletRequest request,
					HttpServletResponse response) throws ServletException, IOException,
					Exception {
				boolean flag = false;
				StrUtils StrUtils = new StrUtils();
				Map receiveMaterial_HM = null;
				String  OrderNo = "", Loc = "",Batch="",ID="",Item= "",ItemDesc= "",Qty = "",Uom = "",OrderStatus="",Remarks="";
				String LOGIN_USER="",Cname="",LnNO="";
				double pickingQty = 0;
				Boolean allChecked = false,fullPrint = false;
				String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
				String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				String USER          = StrUtils.fString(request.getParameter("USER"));
				String ITEMNO        = StrUtils.fString(request.getParameter("ITEM"));
				String ITEMDESC      = StrUtils.fString(request.getParameter("DESC"));
				String UOM 	 = StrUtils.fString(request.getParameter("UOM"));
				String PRINTSTATUS	= StrUtils.fString(request.getParameter("PRINTSTATUS"));
				String BATCH         = StrUtils.fString(request.getParameter("BATCH"));
								
				request.getSession().setAttribute("RESULT","");
				request.getSession().setAttribute("RESULTERROR","");
			    ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				Map checkedDOS = new HashMap();
				long randomRefno=0;
				randomRefno=RefNumber();
				String url = "jsp/LabelPrintManual.jsp?PGaction=View&PLANT="
		                + PLANT
		                 + "&DIRTYPE="
		                + DIRTYPE
		                + "&REFNO="
		                + Long.toString(randomRefno)
		                 + "&ITEM="
		                + ITEMNO 
		                + "&DESC="
		                +  ITEMDESC 
		                + "&BATCH="
		                + BATCH 
		               	+ "&PRINTSTATUS=" 
						+ PRINTSTATUS;
				try {
					HttpSession session = request.getSession();
		            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String[] chkdLnNo= request.getParameterValues("chkdLnNo");
					LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
					InvMstDAO invMstDAO = new InvMstDAO();
					invMstDAO.setmLogger(mLogger);
					if( request.getParameter("select")!=null){
						allChecked = true;
					}
					process: 	
					if (chkdLnNo != null)    {     
						for (int i = 0; i < chkdLnNo.length; i++)    
						{ 
							ID = chkdLnNo[i];
							ArrayList  movQryList = itemMstDAO.getLabelprintDetails(PLANT, " AND  ID="+ID);
						    
							 if (movQryList.size() > 0) {
				        	       
						            for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
						            			Map lineArr = (Map) movQryList.get(iCnt);
						            			Item = StrUtils.fString((String)lineArr.get("ITEM")) ;
						            			ItemDesc = StrUtils.fString((String)lineArr.get("ITEMDESC")) ;
						            			Batch = StrUtils.fString((String)lineArr.get("BATCH")) ;
						            }
							 }
							/*LnNO = chkdLnNo[i];
							if (Batch.length() == 0) {
								Batch = "NOBATCH";
							}*/
							
							receiveMaterial_HM = new HashMap();
							receiveMaterial_HM.put(IConstants.PLANT, PLANT);
							receiveMaterial_HM.put(IConstants.ORDERNO, "Label8");
							receiveMaterial_HM.put(IConstants.REFNO,Long.toString(randomRefno));
							receiveMaterial_HM.put(IConstants.ITEM, Item);
							receiveMaterial_HM.put(IConstants.ITEM_DESC, ItemDesc);
							receiveMaterial_HM.put(IConstants.LOC, "");
							receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
							receiveMaterial_HM.put(IConstants.BATCH, Batch);
							receiveMaterial_HM.put(IConstants.QTY, "0");
							receiveMaterial_HM.put(IConstants.REMARKS, "Product ID & Description + Batch No (manual)");
							receiveMaterial_HM.put(IConstants.UOMCODE, "");
							receiveMaterial_HM.put(IConstants.LABEL_PRINT_TYPE, "Label8");
							receiveMaterial_HM.put("ORDERSTATUS", "");
							receiveMaterial_HM.put("CNAME", "");
							flag = labelPrintUtil.process_LabelPrint(receiveMaterial_HM)&& true;
							//flag=true;
							if(flag)
							{
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put("BATNO", Batch);
								htRecvHis.put(IDBConstants.PLANT, PLANT);
								htRecvHis.put("DIRTYPE",  "Label Print");
								htRecvHis.put(IDBConstants.ITEM, Item);
								htRecvHis.put("MOVTID", "");
								htRecvHis.put("RECID", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put(IDBConstants.QTY, "0");
								htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
								htRecvHis.put(IConstants.REMARKS, "Product ID & Description + Batch No (manual)");
								htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
								htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
								flag = movHisDao.insertIntoMovHis(htRecvHis);	
							}
							if(!flag)
								break process;
							}
					}
							
					if (flag) {
					
						request.getSession().setAttribute("refNo",Long.toString(randomRefno));
						request.getSession().setAttribute("ISPOPUP","OPENED");
						
						xmlStr = "<script type=\"text/javascript\">window.open('LabelSettings.jsp?PRINTTYPE=8','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')</script>";
						//response.sendRedirect(url);
						
					} else {
						
						request.getSession().setAttribute("RESULTERROR","Error in printing product and description details!");
						//response.sendRedirect(url);
						xmlStr = "Error in printing details!";
							
				    	}
					
					}
					
				 catch (Exception e) {
					 this.mLogger.exception(this.printLog, "", e);
					 request.getSession().setAttribute("RESULTERROR",e.getMessage());
					 response.sendRedirect(url);
					 
				}
					
					return xmlStr;
			}
		 
		 
		 
 private String LabelPrintLocation(HttpServletRequest request,
					HttpServletResponse response) throws ServletException, IOException,
					Exception {
				boolean flag = false;
				StrUtils StrUtils = new StrUtils();
				Map receiveMaterial_HM = null;
				String  OrderNo = "", loc = "",locdesc="",loctype="",Remarks="";
				String LOGIN_USER="",Cname="",LnNO="";
				double pickingQty = 0;
				Boolean allChecked = false,fullPrint = false;
				String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
				String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				String USER          = StrUtils.fString(request.getParameter("USER"));
				String LOC        = StrUtils.fString(request.getParameter("LOC"));
				String LOC_TYPE_ID	 = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
				
				request.getSession().setAttribute("RESULT","");
				request.getSession().setAttribute("RESULTERROR","");
			   	request.getSession().setAttribute("FROM_DATE","");
			    //request.getSession().setAttribute("refNo","");
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				Map checkedDOS = new HashMap();
				long randomRefno=0;
				randomRefno=RefNumber();
				String url = "jsp/LabelPrintLoc.jsp?PGaction=View&PLANT="
		                + PLANT
		                 + "&DIRTYPE="
		                + DIRTYPE
		                + "&REFNO="
		                + Long.toString(randomRefno)
		                + "&LOC="
		                + LOC
		                + "&LOC_TYPE_ID=" 
		                + LOC_TYPE_ID;
						
				try {
					HttpSession session = request.getSession();
		            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String[] chkdLnNo= request.getParameterValues("chkdLnNo");
					LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
					if( request.getParameter("select")!=null){
						allChecked = true;
					}
					process: 	
					if (chkdLnNo != null)    {     
						for (int i = 0; i < chkdLnNo.length; i++)    
						{ 
							String	data = chkdLnNo[i];
						    String[] chkdata = data.split(",,,");
							loc = chkdata[0];
							locdesc = strUtils.replaceCharacters2Recv(chkdata[1]);
							loctype = chkdata[2];
							LnNO = chkdLnNo[i];
							receiveMaterial_HM = new HashMap();
							receiveMaterial_HM.put(IConstants.PLANT, PLANT);
							receiveMaterial_HM.put(IConstants.ORDERNO, "");
							receiveMaterial_HM.put(IConstants.REFNO,Long.toString(randomRefno));
							receiveMaterial_HM.put(IConstants.ITEM, "");
							receiveMaterial_HM.put(IConstants.ITEM_DESC, "");
							receiveMaterial_HM.put(IConstants.LOC, loc);
							receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
							receiveMaterial_HM.put(IConstants.BATCH, "");
							receiveMaterial_HM.put(IConstants.QTY, "0");
								receiveMaterial_HM.put(IConstants.REMARKS, "Location");
							receiveMaterial_HM.put(IConstants.UOMCODE, "");
							receiveMaterial_HM.put(IConstants.LABEL_PRINT_TYPE, "Label4");
							receiveMaterial_HM.put("ORDERSTATUS", "");
							receiveMaterial_HM.put("CNAME", "");
							flag = labelPrintUtil.process_LabelPrint(receiveMaterial_HM)&& true;
							//flag=true;
							
							if(!flag)
								break process;
							}
					}
							
					if (flag) {
						
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put("BATNO", "");
						htRecvHis.put(IDBConstants.PLANT, PLANT);
						htRecvHis.put("DIRTYPE",  "LABEL PRINT");
						htRecvHis.put(IDBConstants.ITEM, "");
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.LOC, loc);
						htRecvHis.put(IDBConstants.QTY, "0");
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IConstants.REMARKS, "Location");
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
						flag = movHisDao.insertIntoMovHis(htRecvHis);
						if(!flag)
						{
							throw new Exception();
						}
						request.getSession().setAttribute("refNo",Long.toString(randomRefno));
						request.getSession().setAttribute("ISPOPUP","OPENED");
						xmlStr = "<script type=\"text/javascript\">window.open('LabelSettings.jsp?PRINTTYPE=4','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')</script>";
						//response.sendRedirect(url);
						
					} else {
						
						request.getSession().setAttribute("RESULTERROR","Error in printing location!");
						//response.sendRedirect(url);
						xmlStr = "Error in printing details!";
							
				    	}
					
					}
					
				 catch (Exception e) {
					 this.mLogger.exception(this.printLog, "", e);
					 request.getSession().setAttribute("RESULTERROR",e.getMessage());
					 response.sendRedirect(url);
					 
				}
					
					return xmlStr;
			}
		 
		 
		 
		 private String LabelPrintEmployee(HttpServletRequest request,
					HttpServletResponse response) throws ServletException, IOException,
					Exception {
				boolean flag = false;
				StrUtils StrUtils = new StrUtils();
				Map receiveMaterial_HM = null;
				String  OrderNo = "", empno = "",firstname="",lastname="",Remarks="";
				String LOGIN_USER="",Cname="",LnNO="";
				double pickingQty = 0;
				Boolean allChecked = false,fullPrint = false;
				String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
				String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				String USER          = StrUtils.fString(request.getParameter("USER"));
				String CUST_NAME      = StrUtils.fString(request.getParameter("CUST_NAME"));
				
				request.getSession().setAttribute("RESULT","");
				request.getSession().setAttribute("RESULTERROR","");
			   	request.getSession().setAttribute("FROM_DATE","");
			    //request.getSession().setAttribute("refNo","");
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				Map checkedDOS = new HashMap();
				long randomRefno=0;
				randomRefno=RefNumber();
				String url = "jsp/LabelPrintEmployee.jsp?PGaction=View&PLANT="
		                + PLANT
		                 + "&DIRTYPE="
		                + DIRTYPE
		                + "&REFNO="
		                + Long.toString(randomRefno)
		                + "&CUST_NAME="
		                + CUST_NAME;
		                
						
				try {
					HttpSession session = request.getSession();
		            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String[] chkdLnNo= request.getParameterValues("chkdLnNo");
					LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
					
					if( request.getParameter("select")!=null){
						allChecked = true;
					}
					process: 	
					if (chkdLnNo != null)    {     
						for (int i = 0; i < chkdLnNo.length; i++)    
						{ 
							String	data = chkdLnNo[i];
						    String[] chkdata = data.split(",,,");
						    empno = chkdata[0];
						    firstname = strUtils.replaceCharacters2Recv(chkdata[1]);
						   // lastname =  strUtils.replaceCharacters2Recv(chkdata[2]);
							LnNO = chkdLnNo[i];
							receiveMaterial_HM = new HashMap();
							receiveMaterial_HM.put(IConstants.PLANT, PLANT);
							receiveMaterial_HM.put(IConstants.ORDERNO, "");
							receiveMaterial_HM.put(IConstants.REFNO,Long.toString(randomRefno));
							receiveMaterial_HM.put(IConstants.ITEM, empno + " "+firstname);
							receiveMaterial_HM.put(IConstants.ITEM_DESC, "");
							receiveMaterial_HM.put(IConstants.LOC, "");
							receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
							receiveMaterial_HM.put(IConstants.BATCH, "");
							receiveMaterial_HM.put(IConstants.QTY, "0");
							receiveMaterial_HM.put(IConstants.REMARKS, "Employee");
							receiveMaterial_HM.put(IConstants.UOMCODE, "");
							receiveMaterial_HM.put(IConstants.LABEL_PRINT_TYPE, "Label5");
							receiveMaterial_HM.put("ORDERSTATUS", "");
							receiveMaterial_HM.put("CNAME", "");
							flag = labelPrintUtil.process_LabelPrint(receiveMaterial_HM)&& true;
							//flag=true;
							
							if(!flag)
								break process;
							}
					}
							
					if (flag) {
						
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put("BATNO", "");
						htRecvHis.put(IDBConstants.PLANT, PLANT);
						htRecvHis.put("DIRTYPE",  "LABEL PRINT");
						htRecvHis.put(IDBConstants.ITEM, empno + " "+firstname);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.LOC, "");
						htRecvHis.put(IDBConstants.QTY, "0");
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IConstants.REMARKS, "Employee");
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
						flag = movHisDao.insertIntoMovHis(htRecvHis);
						if(!flag)
						{
							throw new Exception();
						}
						request.getSession().setAttribute("refNo",Long.toString(randomRefno));
						request.getSession().setAttribute("ISPOPUP","OPENED");
						xmlStr = "<script type=\"text/javascript\">window.open('LabelSettings.jsp?PRINTTYPE=5','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')</script>";
						//response.sendRedirect(url);
						
					} else {
						
						request.getSession().setAttribute("RESULTERROR","Error in printing location!");
						//response.sendRedirect(url);
						xmlStr = "Error in printing details!";
							
				    	}
					
					}
					
				 catch (Exception e) {
					 this.mLogger.exception(this.printLog, "", e);
					 request.getSession().setAttribute("RESULTERROR",e.getMessage());
					 response.sendRedirect(url);
					 
				}
					
					return xmlStr;
			}
		 
		 private String LabelPrintCatalog(HttpServletRequest request,
					HttpServletResponse response) throws ServletException, IOException,
					Exception {
				boolean flag = false;
				StrUtils StrUtils = new StrUtils();
				Map receiveMaterial_HM = null;
				String  OrderNo = "", imagepath="",item = "",itemdesc="",uom="",Remarks="";
				String LOGIN_USER="",Cname="",LnNO="";
				double pickingQty = 0;
				Boolean allChecked = false,fullPrint = false;
				String PLANT =     StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
				String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
				String USER          = StrUtils.fString(request.getParameter("USER"));
				String PRODUCTID = strUtils.fString(request.getParameter("PRODUCTID"));
				String DESCR = strUtils.fString(request.getParameter("DESCR"));
				String PRD_CLS_ID= strUtils.fString(request.getParameter("PRD_CLS_ID"));
				String PRD_TYPE_ID= strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_BRAND_ID= strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				
				request.getSession().setAttribute("RESULT","");
				request.getSession().setAttribute("RESULTERROR","");
			   	request.getSession().setAttribute("FROM_DATE","");
			    //request.getSession().setAttribute("refNo","");
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				itemMstDAO.setmLogger(mLogger);
				Map checkedDOS = new HashMap();
				long randomRefno=0;
				randomRefno=RefNumber();
				String url = "jsp/LabelPrintCatalog.jsp?PGaction=View&PLANT="
		                + PLANT
		                 + "&DIRTYPE="
		                + DIRTYPE
		                + "&REFNO="
		                + Long.toString(randomRefno)
		                + "&PRODUCTID="
		                + PRODUCTID
		                + "&DESCR="
		                + DESCR
		                + "&PRD_CLS_ID="
		                + PRD_CLS_ID
		                + "&PRD_TYPE_ID="
		                + PRD_TYPE_ID
		                + "&PRD_BRAND_ID="
		                + PRD_BRAND_ID;
		                
						
				try {
					HttpSession session = request.getSession();
		            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String[] chkdLnNo= request.getParameterValues("chkdLnNo");
					LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
					
					if( request.getParameter("select")!=null){
						allChecked = true;
					}
					process: 	
					if (chkdLnNo != null)    {     
						for (int i = 0; i < chkdLnNo.length; i++)    
						{ //chkString  =imageFile+",,,"+prodid+",,,"+ItemDesc+",,,"+Uom;
							String	data = chkdLnNo[i];
						    String[] chkdata = data.split(",,,");
						    imagepath= chkdata[0];
						    item = chkdata[1];
						    itemdesc= strUtils.replaceCharacters2Recv(chkdata[2]);
						    uom = chkdata[3];
							LnNO = chkdLnNo[i];
							File QRCodeFile = QRCode.from(item).to(ImageType.PNG).withSize(150, 150).file(); 
			    		    String QRCodeImagePath =QRCodeFile.getAbsolutePath();
			    		    
							receiveMaterial_HM = new HashMap();
							receiveMaterial_HM.put(IConstants.PLANT, PLANT);
							receiveMaterial_HM.put(IConstants.ORDERNO, "");
							receiveMaterial_HM.put(IConstants.REFNO,Long.toString(randomRefno));
							receiveMaterial_HM.put(IConstants.ITEM, item);
							receiveMaterial_HM.put(IConstants.ITEM_DESC, itemdesc);
							receiveMaterial_HM.put(IConstants.LOC, "");
							receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
							receiveMaterial_HM.put(IConstants.BATCH, "");
							receiveMaterial_HM.put(IConstants.QTY, "0");
							receiveMaterial_HM.put(IConstants.REMARKS, "Catalog");
							receiveMaterial_HM.put(IConstants.UOMCODE, uom);
							receiveMaterial_HM.put(IConstants.LABEL_PRINT_TYPE, "Label6");
							receiveMaterial_HM.put("ORDERSTATUS", "");
							receiveMaterial_HM.put("CNAME", "");
							receiveMaterial_HM.put(IConstants.CatalogImagePath, QRCodeImagePath);
							flag = labelPrintUtil.process_LabelPrint(receiveMaterial_HM)&& true;
							//flag=true;
							
							if(!flag)
								break process;
							}
					}
							
					if (flag) {
						
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put("BATNO", "");
						htRecvHis.put(IDBConstants.PLANT, PLANT);
						htRecvHis.put("DIRTYPE",  "LABEL PRINT");
						htRecvHis.put(IDBConstants.ITEM, item);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.LOC, "");
						htRecvHis.put(IDBConstants.QTY, "0");
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IConstants.REMARKS, "Catalog");
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
						flag = movHisDao.insertIntoMovHis(htRecvHis);
						if(!flag)
						{
							throw new Exception();
						}
						request.getSession().setAttribute("refNo",Long.toString(randomRefno));
						request.getSession().setAttribute("ISPOPUP","OPENED");
						xmlStr = "<script type=\"text/javascript\">window.open('LabelSettings.jsp?PRINTTYPE=6','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')</script>";
						//response.sendRedirect(url);
						
					} else {
						
						request.getSession().setAttribute("RESULTERROR","Error in printing location!");
						//response.sendRedirect(url);
						xmlStr = "Error in printing details!";
							
				    	}
					
					}
					
				 catch (Exception e) {
					 this.mLogger.exception(this.printLog, "", e);
					 request.getSession().setAttribute("RESULTERROR",e.getMessage());
					 response.sendRedirect(url);
					 
				}
					
					return xmlStr;
			}
		 
		 
		 
  	private long RefNumber()  
  	{  
  		return (long)(Math.random()*10000000000L);  
  		 		
  	}
  		
  
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
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

}
