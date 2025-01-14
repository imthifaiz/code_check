package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.LocUtil;
import com.track.gates.sqlBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.dao.LocMstDAO;
import com.track.db.util.LocTypeUtil;

public class LocMstServlet extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.LocMstServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LocMstServlet_PRINTPLANTMASTERINFO;
	String action = "";
	DateUtils dateutils = new DateUtils();
	LocUtil _locUtil = null;
	InvMstDAO _invmstDAO = null;
	//WipTranDAO _WipTranDAO = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_locUtil = new LocUtil();
		_invmstDAO = new InvMstDAO();
		//_WipTranDAO = new WipTranDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			action = request.getParameter("action").trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			_locUtil.setmLogger(mLogger);
			JSONObject jsonObjectResult = new JSONObject();
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("New")) {
				response.sendRedirect("jsp/LocMst_View.jsp?action=NEW");
			}

			else if (action.equalsIgnoreCase("ADD")) {
				String result = addLocation(request, response);
				if(result=="Added"){
					String result2="<font class = " + IConstants.SUCCESS_COLOR
							+ "> Location Added Successfully</font>";
				response.sendRedirect("../location/summary?action=SHOW_RESULT&result="+result2);
				}
				else{
					response.sendRedirect("../location/summary?action=SHOW_RESULT&result="+ result);	
				}

			} else if (action.equalsIgnoreCase("UPDATE")) {

				String result = updateLocation(request, response);
				if(result=="Update"){
				String result2="<font class = " + IConstants.SUCCESS_COLOR
							+ "> Location Updated Successfully</font>";
				response.sendRedirect("../location/summary?action=SHOW_RESULT&result="+ result2);
				}
				else{
				response.sendRedirect("../location/summary?action=SHOW_RESULT&result="+ result);
				}
			}

			else if (action.equalsIgnoreCase("DELETE")) {

				String result = DeleteLocation(request, response);
				String result2="<font class = " + IConstants.SUCCESS_COLOR
						+ "> Location Deleted Successfully</font>";
				response.sendRedirect("../location/summary?action=SHOW_RESULT&result="+ result2);
			}
			else  if (action.equals("getLoc")) {
				String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
	            jsonObjectResult = this.getLoc(request);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
	          }
			else  if (action.equals("getLocType")) {
				String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
	            jsonObjectResult = this.getLocType(request);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
	          }
			else if(action.equals("Loc_Export_Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToLocExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=LocationMasterList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
		       String result = "<font class = " + IConstants.FAILED_COLOR + ">Exception : " + ex.getMessage() + "</font>";
		       request.getSession().setAttribute("errResult", result);
			response.sendRedirect("jsp/LocMst_View.jsp?action=SHOW_RESULT_VALUE");
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String addLocation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "", sAddr1  = "",
	       sAddr2  = "", scomname = "", srcbno ="",
	       sAddr3  = "", sAddr4  = "",
	       sCountry   = "", sState="",
	       sZip       = "", schkstatus = "",
	       sTelNo = "", sFax = "" ;
		TblControlDAO _TblControlDAO =new TblControlDAO();
		try {
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");

			String locId = request.getParameter("LOC_ID").trim();
			String locDesc = request.getParameter("LOC_DESC").trim();
			String loctypeid = request.getParameter("LOC_TYPE_ID").trim();
			String loctypeid2 = request.getParameter("LOC_TYPE_ID2").trim();
			String loctypeid3 = request.getParameter("LOC_TYPE_ID3").trim();
			
			String remarks = request.getParameter("REMARKS").trim();
			scomname =  request.getParameter("COMNAME").trim();
			srcbno =  request.getParameter("RCBNO").trim();
			sAddr1 =  request.getParameter("ADDR1").trim();
			sAddr2 = request.getParameter("ADDR2").trim();
			sAddr3 =  request.getParameter("ADDR3").trim();
			sAddr4 = request.getParameter("ADDR4").trim();
			sState = request.getParameter("STATE").trim();
			sCountry = request.getParameter("COUNTRY").trim();
			sZip = request.getParameter("ZIP").trim();
			sTelNo = request.getParameter("TELNO").trim();
			sFax = request.getParameter("FAX").trim();
			schkstatus = StrUtils.fString(request.getParameter("CHK_ADDRESS"));
				if(locId.length()>0){
                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(locId);
                        if(specialcharsnotAllowed.length()>0){
                        throw new Exception("Loc ID  value : '" + locId + "' has special characters "+specialcharsnotAllowed+" that are  not allowed .");
                        }
                        }

			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("WHID", "");
			ht.put("LOC", locId);
			ht.put("LOCDESC", locDesc);
			ht.put("LOC_TYPE_ID", loctypeid);
			ht.put("LOC_TYPE_ID2", loctypeid2);
			ht.put("LOC_TYPE_ID3", loctypeid3);
			ht.put("ISACTIVE", "Y");
			ht.put("USERFLD1", remarks);
			ht.put("COMNAME", scomname);
			ht.put("RCBNO", srcbno);
			ht.put("ADD1", sAddr1);
			ht.put("ADD2", sAddr2);
			ht.put("ADD3", sAddr3);
			ht.put("ADD4", sAddr4);
			ht.put("STATE", sState);
			ht.put("COUNTRY", sCountry);
			ht.put("ZIP", sZip);
			ht.put("TELNO", sTelNo);
			ht.put("FAX", sFax);
			if(schkstatus.equalsIgnoreCase("Y")){
			ht.put("CHKSTATUS", schkstatus);}
			else{
			ht.put("CHKSTATUS", "N");}	

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_LOC);
			htm.put("RECID", "");
			htm.put("ITEM",locId);
			htm.put("CRBY", user);
			htm.put("REMARKS", remarks);htm.put("LOC", locId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));

			  htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
	          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	          boolean updateFlag;
			if(locId!="L0001")
	  		  {	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "LOCATION");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
	  		    updateFlag=_TblControlDAO.updateSeqNo("LOCATION",plant);
				else
				{
					boolean insertFlag = false;
					Map htInsert=null;
	            	Hashtable htTblCntInsert  = new Hashtable();           
	            	htTblCntInsert.put(IDBConstants.PLANT,plant);          
	            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCATION");
	            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"L");
	             	htTblCntInsert.put("MINSEQ","0000");
	             	htTblCntInsert.put("MAXSEQ","9999");
	            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
	            	htTblCntInsert.put(IDBConstants.CREATED_BY, user);
	            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
				}
			}
			
			boolean flag = _locUtil.addLocation(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {
				request.getSession().setAttribute("locMstData", ht);
				result = "Added";				

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> Location Exists Already</font>";
				
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	private String updateLocation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", locId = "", locDesc = "", remarks = "", isActive = "";
		String sAddr1  = "", scomname = "", srcbno ="",
	       sAddr2  = "",
	       sAddr3  = "", sAddr4  = "",
	       sCountry   = "", schkstatus = "",
	       sZip       = "",sState="",
	       sTelNo = "", sFax = "",sloctypeid="",sloctypeid2="",sloctypeid3="" ;
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		String user = (String) session.getAttribute("LOGIN_USER");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();
		

		try {

			locId = request.getParameter("LOC_ID").trim();
			locDesc = request.getParameter("LOC_DESC").trim();
			remarks = request.getParameter("REMARKS").trim();
			isActive = request.getParameter("ACTIVE").trim();
			scomname =  request.getParameter("COMNAME").trim();
			srcbno =  request.getParameter("RCBNO").trim();
			sAddr1 =  request.getParameter("ADDR1").trim();
			sAddr2 = request.getParameter("ADDR2").trim();
			sAddr3 =  request.getParameter("ADDR3").trim();
			sAddr4 = request.getParameter("ADDR4").trim();
			sState = request.getParameter("STATE").trim();
			sCountry = request.getParameter("COUNTRY").trim();
			sZip = request.getParameter("ZIP").trim();
			sTelNo = request.getParameter("TELNO").trim();
			sFax = request.getParameter("FAX").trim();
			schkstatus = StrUtils.fString(request.getParameter("CHK_ADDRESS"));
			sloctypeid = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
			sloctypeid2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
			sloctypeid3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("WHID", "");
			ht.put("LOC", locId);
			ht.put("LOCDESC", locDesc);
			ht.put("ISACTIVE", isActive);
			ht.put("USERFLD1", remarks);
			ht.put("COMNAME", scomname);
			ht.put("RCBNO", srcbno);
			ht.put("ADD1", sAddr1);
			ht.put("ADD2", sAddr2);
			ht.put("ADD3", sAddr3);
			ht.put("ADD4", sAddr4);
			ht.put("STATE", sState);
			ht.put("COUNTRY", sCountry);
			ht.put("ZIP", sZip);
			ht.put("TELNO", sTelNo);
			ht.put("FAX", sFax);
			ht.put("LOC_TYPE_ID", sloctypeid);
			ht.put("LOC_TYPE_ID2", sloctypeid2);
			ht.put("LOC_TYPE_ID3", sloctypeid3);
			if(schkstatus.equalsIgnoreCase("Y")){
				ht.put("CHKSTATUS", schkstatus);}
			else{
				ht.put("CHKSTATUS", "N");}	


			if (locDesc.length() > 0 || !locDesc.equalsIgnoreCase(null)) {
				htValues.put("LOCDESC", locDesc);
			}
			if (sloctypeid.length() > 0 || !sloctypeid.equalsIgnoreCase(null)) {
				htValues.put("LOC_TYPE_ID", sloctypeid);
				
			}
			if (sloctypeid2.length() > 0 || !sloctypeid2.equalsIgnoreCase(null)) {
				htValues.put("LOC_TYPE_ID2", sloctypeid2);
				
			}
			if (sloctypeid3.length() > 0 || !sloctypeid3.equalsIgnoreCase(null)) {
				htValues.put("LOC_TYPE_ID3", sloctypeid3);
				
			}
			if (remarks.length() > 0 || !remarks.equalsIgnoreCase(null)) {
				htValues.put("USERFLD1", remarks);
			}
			if (isActive.length() > 0 || !isActive.equalsIgnoreCase(null)) {
				htValues.put("ISACTIVE", isActive);
			}
			if (scomname.length() > 0 || !scomname.equalsIgnoreCase(null)) {
				htValues.put("COMNAME", scomname);
			}
			if (srcbno.length() > 0 || !srcbno.equalsIgnoreCase(null)) {
				htValues.put("RCBNO", srcbno);
			}
			if (sAddr1.length() > 0 || !sAddr1.equalsIgnoreCase(null)) {
				htValues.put("ADD1", sAddr1);
			}
			if (sAddr2.length() > 0 || !sAddr2.equalsIgnoreCase(null)) {
				htValues.put("ADD2", sAddr2);
			}
			if (sAddr3.length() > 0 || !sAddr3.equalsIgnoreCase(null)) {
				htValues.put("ADD3", sAddr3);
			}
			if (sAddr4.length() > 0 || !sAddr4.equalsIgnoreCase(null)) {
				htValues.put("ADD4", sAddr4);
			}
			if (sState.length() > 0 || !sState.equalsIgnoreCase(null)) {
				htValues.put("STATE", sState);
			}
			if (sCountry.length() > 0 || !sCountry.equalsIgnoreCase(null)) {
				htValues.put("COUNTRY", sCountry);
			}
			if (sZip.length() > 0 || !sZip.equalsIgnoreCase(null)) {
				htValues.put("ZIP", sZip);
			}
			if (sTelNo.length() > 0 || !sTelNo.equalsIgnoreCase(null)) {
				htValues.put("TELNO", sTelNo);
			}
			if (sFax.length() > 0 || !sFax.equalsIgnoreCase(null)) {
				htValues.put("FAX", sFax);
			}
			if (schkstatus.length() > 0 || !schkstatus.equalsIgnoreCase(null)) {
				htValues.put("CHKSTATUS", schkstatus);
			}

			htCondition.put("LOC", ht.get("LOC"));
			htCondition.put("PLANT", ht.get("PLANT"));

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.UPD_LOC);
			htm.put("RECID", "");
			htm.put("ITEM",locId);
			htm.put("UPBY", user);
			htm.put("REMARKS", remarks);htm.put("LOC", locId);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			if(_locUtil.isValidLoc(plant, locId)){

			boolean flag = _locUtil.updateLocation(htValues, htCondition, " ");
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {

				request.getSession().setAttribute("locMstData", ht);
				result="Update";
				/*result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Location:" + locId + " updated Successfully</font>";*/

			} else {
				throw new Exception("Unable to update location ");
			}
			}
			else{
				request.getSession().setAttribute("locMstData", ht);
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">location ID:" + locId + " doesn't not Exists. Try again</font>";
				
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	private String DeleteLocation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", locId = "", locDesc = "", remarks = "";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		String user = (String) session.getAttribute("LOGIN_USER");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();
		sqlBean sqlbn= new sqlBean();
		boolean wiplocflag=false;
		MovHisDAO mdao = new MovHisDAO(plant);

		try {

			locId = request.getParameter("LOC_ID").trim();
			
			boolean movementhistoryExist=false;
			Hashtable htmh = new Hashtable();
			htmh.put("LOC", locId);
			htmh.put("PLANT", plant);
			movementhistoryExist = mdao.isExisit(htmh," DIRTYPE NOT IN('CRATE_LOCATION','EDIT_LOCATION','ADD_LOC','UPD_LOC','CNT_LOC_UPLOAD')");


			Hashtable ht = new Hashtable();
			htCondition.put("LOC", locId);
			htCondition.put("PLANT", plant);
			
			boolean locflag  = _invmstDAO.isExisit(htCondition,"QTY>0");
				
			/*String tablename =plant+"_WIP_TRAN";
	        boolean isTableexits = sqlbn.istableExists(tablename);
	        if(isTableexits){
			htValues.put("LOC", "WIP_"+locId);
			htValues.put("PLANT", plant);
			
			wiplocflag  = _WipTranDAO.isExisit(htValues,"QTY>0");
	        }*/
	        
	        if (locflag || movementhistoryExist) {
				result = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Location Exists in either Inventory or Transactions!</font>";
			}
	        
	        /*else if (wiplocflag) {
				result = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Location Exists In WIP Location </font>";
			}*/
			else{

			boolean flag = _locUtil.deleteLocation(locId, plant);
			
			
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.DEL_LOC);
			htm.put("RECID", "");
			htm.put("ITEM",locId);
			htm.put("UPBY", user);
			htm.put("REMARKS", remarks);htm.put("LOC", locId);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			flag = mdao.insertIntoMovHis(htm);

			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("WHID", "");
				ht.put("LOC", "");
				ht.put("LOCDESC", "");
				ht.put("USERFLD1", "");ht.put("ISACTIVE", "Y");
				ht.put("COMNAME", "");
				ht.put("RCBNO", "");
				ht.put("ADD1", "");
				ht.put("ADD2", "");
				ht.put("ADD3", "");
				ht.put("ADD4", "");
				ht.put("STATE", "");
				ht.put("COUNTRY", "");
				ht.put("ZIP", "");
				ht.put("TELNO", "");
				ht.put("FAX", "");

				request.getSession().setAttribute("locMstData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Location Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete location</font>";
				
			}
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}
	
    private HSSFWorkbook writeToLocExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "";
		int maxRowsPerSheet = 65535;
	
		try{
			List listQry = new ArrayList();
			StrUtils strUtils = new StrUtils();
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			int SheetNo =1;	
			Hashtable ht = new Hashtable();
			 String LOC_ID     = strUtils.fString(request.getParameter("LOC_ID")).trim();
			 String  LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOC_TP_ID"));
			 
		      if(strUtils.fString(plant).length() > 0)       ht.put("PLANT",plant);
		      ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
		      LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOC_TP_ID"));
			
		    	 listQry =  new LocUtil().getLocDetails(LOC_ID,plant," ",ht);
		    
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						dataStyle = createDataStyle(wb);
						 sheet = wb.createSheet("Sheet"+SheetNo);
						 styleHeader = createStyleHeader(wb);
						 sheet = this.createLocWidth(sheet);
						 sheet = this.createLocHeader(sheet,styleHeader);
						 int index = 1;
						 if (listQry.size() > 0) {
						 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
							
								   Map lineArr = (Map) listQry.get(iCnt);	
								    int k = 0;
								    
								    HSSFRow row = sheet.createRow(index);
								  
								    HSSFCell cell = row.createCell( k++);
								    
							
								    
								    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
									cell.setCellStyle(dataStyle);
									
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOCDESC"))));
									cell.setCellStyle(dataStyle);
								    
									 cell = row.createCell( k++);
									 cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC_TYPE_ID"))));
									cell.setCellStyle(dataStyle);

								    
								    cell = row.createCell( k++);
								    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("USERFLD1"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COMNAME"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("RCBNO"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADD1"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADD2"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADD3"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADD4"))));
									cell.setCellStyle(dataStyle);
									
																		
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COUNTRY"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ZIP"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TELNO"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("FAX"))));
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ISACTIVE"))));
									cell.setCellStyle(dataStyle);
										
									 index++;
									 if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 SheetNo++;
										 sheet = wb.createSheet("Sheet_"+SheetNo);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createLocWidth(sheet);
										 sheet = this.createLocHeader(sheet,styleHeader);
										 
									 }
									

							  }
						
				 }
				 else if (listQry.size() < 1) {		
					

						System.out.println("No Records Found To List");
					}
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;
	}
 
 private HSSFSheet createLocHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
		int k = 0;
		try{
		
		
		HSSFRow rowhead = sheet.createRow( 0);
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("LocationID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Description"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Location Type"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Remarks"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Company Name"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Business Registration No"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Unit No"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Building"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Street"));
		cell.setCellStyle(styleHeader);
		
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("City"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Country"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Postal Code"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Tel No"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Fax"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Is Active"));
		cell.setCellStyle(styleHeader);
		
	
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFSheet createLocWidth(HSSFSheet sheet){
		
		try{
			sheet.setColumnWidth(0 ,3500);
			sheet.setColumnWidth(1 ,6000);
			sheet.setColumnWidth(2 ,4000);
			sheet.setColumnWidth(3 ,3500);
			sheet.setColumnWidth(4 ,3500);
			sheet.setColumnWidth(5 ,3000);
			sheet.setColumnWidth(6 ,3500);
			sheet.setColumnWidth(7 ,4000);
			sheet.setColumnWidth(8 ,4000);
			sheet.setColumnWidth(9 ,4000);
			sheet.setColumnWidth(10 ,3500);
			sheet.setColumnWidth(11 ,3500);
			sheet.setColumnWidth(12 ,3500);
			sheet.setColumnWidth(13 ,3500);
			sheet.setColumnWidth(14 ,3500);
			
			
			
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
	private JSONObject getLoc(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ///////////////////////
               CustUtil custUtils = new CustUtil();
               LocMstDAO  _LocMstDAO  = new LocMstDAO(); 
               ArrayList arrCust = _LocMstDAO.getLocByWMS(QUERY,PLANT,"ORDER BY LOCDESC asc");
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("LOC", (String)arrCustLine.get("LOC"));
                   resultJsonInt.put("LOCDESC", (String)arrCustLine.get("LOCDESC"));
                   jsonArray.add(resultJsonInt);
               }
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   jsonArray.add("");
                   resultJson.put("footermaster", jsonArray);
               }
               resultJson.put("LOC_MST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	private JSONObject getLocType(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ///////////////////////
               LocTypeUtil loctypeutil = new LocTypeUtil();
               LocMstDAO  _LocMstDAO  = new LocMstDAO(); 
               ArrayList arrCust = loctypeutil.getLocTypeList(QUERY,PLANT,"ORDER BY LOC_TYPE_DESC asc");
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
                   resultJsonInt.put("LOC_TYPE_DESC", (String)arrCustLine.get("LOC_TYPE_DESC"));
                   jsonArray.add(resultJsonInt);
               }
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   jsonArray.add("");
                   resultJson.put("footermaster", jsonArray);
               }
               resultJson.put("LOCTYPE_MST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
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