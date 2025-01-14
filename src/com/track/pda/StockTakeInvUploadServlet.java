package com.track.pda;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.ItemMstDAO;
import com.track.db.util.InvUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.StockTakeUtil;
import com.track.gates.sqlBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;


/**
 * Servlet implementation class StockTakeInvUploadServlet
 */
public class StockTakeInvUploadServlet extends HttpServlet implements IMLogger{
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.StockTakeServletInvUpload_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.StockTakeServletInvUpload_PRINTPLANTMASTERINFO;
	
	//private POUtil _POUtil = null;
	//private StockTakeUtil _StockTakeUtil = null;

	private String PLANT = "";
	private String xmlStr = "";
	private String action = "";
	
	private static final String CONTENT_TYPE = "text/xml";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StockTakeInvUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
		//_POUtil = new POUtil();
		//_StockTakeUtil = new StockTakeUtil();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
				.getParameter("LOGIN_USER"))
				+ " PDA_USER"));
		//_POUtil.setmLogger(mLogger);
		//_StockTakeUtil.setmLogger(mLogger);
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		try {
			action = request.getParameter("action").trim();

		
			if(action.equals("Export_ExcelInventoryUpload")){  
				 HSSFWorkbook wbInv = new HSSFWorkbook();
				 wbInv = this.writeToInventoryExcel(request,response,wbInv);
				 ByteArrayOutputStream outByteStreamInv = new ByteArrayOutputStream();
				 wbInv.write(outByteStreamInv);
				 byte [] outArray = outByteStreamInv.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=InvMstData.xls");
				 OutputStream outStreamInv = response.getOutputStream();
		         outStreamInv.write(outArray);
				 outStreamInv.flush();
				
				 
			 }
			else if(action.equals("Export_ExcelStockTake")){  
				 HSSFWorkbook wbInv = new HSSFWorkbook();
				 wbInv = this.writeToStockTakeExcel(request,response,wbInv);
				 ByteArrayOutputStream outByteStreamInv = new ByteArrayOutputStream();
				 wbInv.write(outByteStreamInv);
				 byte [] outArray = outByteStreamInv.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=StockTakeTemplateWithData.xls");
				 OutputStream outStreamInv = response.getOutputStream();
		         outStreamInv.write(outArray);
				 outStreamInv.flush();
				
				 
			 }

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			if(!action.equals("Export_Excel")){
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
			}
		}

		if(!action.equals("Export_ExcelInventoryUpload")&&!action.equals("Export_ExcelStockTake")){
			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.write(xmlStr);
			out.close();
		}
	}
	
	 /* ************Modification History*********************************
	   Sep 23 2014, Description: To include USER
	*/
	private HSSFWorkbook writeToInventoryExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",BATCH="";
		int maxRowsPerSheet = 65535;
		
		ArrayList stockTakeList = new ArrayList();
		int SheetId =1;
		try{
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			String itemNo = StrUtils.fString(request.getParameter("ITEM")).trim();		         
		    String itemDesc = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("PRD_DESCRIP"))).trim();
			String location = StrUtils.fString(request.getParameter("LOC")).trim();
			BATCH=StrUtils.fString(request.getParameter("BATCH")).trim();
			//Start code added by Bruhan for product brand,type on 19/sep/13
			PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			//End code added by Bruhan for product brand,type on 19/sep/13 
			 String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			 String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			 String LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
			 String USER = StrUtils.fString(request.getParameter("USER"));
		
			
			Boolean isWithPrice = Boolean.valueOf(StrUtils.fString(request.getParameter("isWithPrice")).trim());
			
			Hashtable ht = new Hashtable();

		      StrUtils strUtils = new StrUtils();
				// if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
	            if(strUtils.fString(location).length() > 0)             ht.put("a.LOC",location);
	            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.BATCH",BATCH);
	            //if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
	           // if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("ITEMTYPE",PRD_TYPE_ID) ;  
	           // if(strUtils.fString(PRD_BRAND_ID).length() > 0)         ht.put("PRD_BRAND_ID",PRD_BRAND_ID) ; 
			
			    stockTakeList = new InvUtil().getStockTakeDetails(ht,plant,itemNo,itemDesc,PRD_CLS_ID,PRD_BRAND_ID,PRD_TYPE_ID,FROM_DATE,TO_DATE,LOC_TYPE_ID,USER);
				 Boolean workSheetCreated = true;
				 if (stockTakeList.size() > 0) {
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						
		
						dataStyle = createDataStyle(wb);	
						 sheet = wb.createSheet("Sheet"+SheetId);
						 styleHeader = createStyleHeader(wb);
						
						 sheet = this.createInventoryWidth(sheet);
						 sheet = this.createInventoryHeader(sheet,styleHeader);
						 
						 int index = 1;
				         double invqty,pcsqty = 0;
				         double stockqty=0;
				       
				            
						 for (int iCnt =0; iCnt<stockTakeList.size(); iCnt++){	
								   Map lineArr = (Map) stockTakeList.get(iCnt);	
								   int k = 0;
								   
								   invqty  =  Double.parseDouble((String)lineArr.get("QTY"));
								   pcsqty  =  Double.parseDouble((String)lineArr.get("PCSQTY"));
		                           stockqty  =  Double.parseDouble((String)lineArr.get("STOCKQTY"));
								   		                                                                          
		                         if(invqty==0 &&  stockqty==0&& pcsqty==0){
		                         }
		                         else
		                         {
								    HSSFRow row = sheet.createRow(index);
								    
								    HSSFCell cell = row.createCell((short) k++);
								    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
									cell.setCellStyle(dataStyle);
																											
								   									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATCH"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
									cell.setCellStyle(dataStyle);																	
									
									cell = row.createCell((short) k++);
									cell.setCellValue(0.0);
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString((String)lineArr.get("STOCKQTY"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue("");
									cell.setCellStyle(dataStyle);
									
									
									index++;
									 if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 SheetId++;
										 sheet = wb.createSheet("Sheet"+SheetId);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createInventoryWidth(sheet);
										 sheet = this.createInventoryHeader(sheet,styleHeader);
										
										 
									 }

						 }
						 }
							
				 }
				 else if (stockTakeList.size() < 1) {		
					

						System.out.println("No Records Found To List");
					}
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;
	}
	
	 private HSSFSheet createInventoryHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
						
			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("LOC"));
			cell.setCellStyle(styleHeader);
					
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("BATCH"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Avg Unit Cost"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Qty"));
			cell.setCellStyle(styleHeader);
								
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Expire Date(mm/dd/yyyy)"));
			cell.setCellStyle(styleHeader);
							
					
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
	 
	 private HSSFSheet createInventoryWidth(HSSFSheet sheet){
			int i = 0;
			try{
				sheet.setColumnWidth((short)i++ ,(short)6000);
				sheet.setColumnWidth((short)i++ ,(short)5000);
				sheet.setColumnWidth((short)i++ ,(short)5000);
				sheet.setColumnWidth((short)i++ ,(short)5000);
				sheet.setColumnWidth((short)i++ ,(short)5000);
							
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}


	 private HSSFWorkbook writeToStockTakeExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "",BATCH="";
			int maxRowsPerSheet = 65535;
			
			ArrayList stockTakeList = new ArrayList();
			try{
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				String itemNo = StrUtils.fString(request.getParameter("ITEM")).trim();
				String location = StrUtils.fString(request.getParameter("LOC")).trim();
				BATCH=StrUtils.fString(request.getParameter("BATCH")).trim();
				
				Hashtable ht = new Hashtable();

			      StrUtils strUtils = new StrUtils();
			      if(strUtils.fString(location).length() > 0)             ht.put("LOC",location);
		            if(strUtils.fString(BATCH).length() > 0)               ht.put("S.USERFLD4",BATCH);
		            
		            stockTakeList = new InvUtil().getStockTakeDataDetails(ht,plant,itemNo);
					 Boolean workSheetCreated = true;
					 
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							
			
							dataStyle = createDataStyle(wb);	
							 sheet = wb.createSheet();
							 styleHeader = createStockTakeStyleHeader(wb);
							 wb.setSheetName(0, "Sheet1");
							 sheet = this.createInventoryWidth(sheet);
							 sheet = this.createStockTakeHeader(wb,sheet,styleHeader);
							 if (stockTakeList.size() > 0) {		 
							 int index = 1;
							 
							 for (int iCnt =0; iCnt<stockTakeList.size(); iCnt++){	
								   Map lineArr = (Map) stockTakeList.get(iCnt);	
								   int k = 0;
								   HSSFRow row = sheet.createRow(index);
								    
								    HSSFCell cell = row.createCell((short) k++);
								    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
									cell.setCellStyle(dataStyle);
																											
								   									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATCH"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(""));
									cell.setCellStyle(dataStyle);																	
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UOM"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue("");
									cell.setCellStyle(dataStyle);
									
									
									index++;
									 if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 sheet = wb.createSheet();
										 styleHeader = createStockTakeStyleHeader(wb);
										 sheet = this.createInventoryWidth(sheet);
										 sheet = this.createStockTakeHeader(wb,sheet,styleHeader);
										
										 
									 }

						 }						 
							
				 }
				 else if (stockTakeList.size() < 1) {
						System.out.println("No Records Found To List");
					}
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;							 
	 }
	 
	 private HSSFSheet createStockTakeHeader(HSSFWorkbook wb, HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
				 
				HSSFCellStyle styleHeaderred = wb.createCellStyle();
				  HSSFFont fontHeader  = wb.createFont();
				  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				  fontHeader.setFontName("Arial");
				  fontHeader.setColor(HSSFColor.RED.index);
				  styleHeaderred.setFont(fontHeader);
				  HSSFPalette palette = ((HSSFWorkbook)wb).getCustomPalette();
				  palette.setColorAtIndex((short)17, (byte)169, (byte)208, (byte)141);
				  styleHeaderred.setFillForegroundColor(palette.getColor(17).getIndex());
				  styleHeaderred.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				  styleHeaderred.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				  styleHeaderred.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
				  styleHeaderred.setWrapText(true);
				
			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product ID (Max 13 Chars for 50mm x 25mm Labels)"));
			cell.setCellStyle(styleHeaderred);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Location ID (Max 20 Chars)"));
			cell.setCellStyle(styleHeaderred);
			
					
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Batch /Serial Number (Max 40 Chars)"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Stock Take Quantity"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("UOM (Max 10 Char)"));
			cell.setCellStyle(styleHeaderred);
							
					
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
				
	 
		 private HSSFCellStyle createDataStyle(HSSFWorkbook wb){
				
				//Create style
				  HSSFCellStyle dataStyle = wb.createCellStyle();
				  dataStyle.setWrapText(true);
				  return dataStyle;
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


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void destroy() {
	}
	
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
	
	private HSSFSheet createHeaderCompanyReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PLANT){
		int k = 0;
		try{
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",COL1="",COL2="" ;
			PlantMstUtil pmUtil = new PlantMstUtil();
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				CNAME = (String) map.get("PLNTDESC");
				/*CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				COL1=CADD1+" "+CADD2;
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				if((CADD3+CADD4).length()>1)
					COL1=COL1+", "+(CADD3+" "+CADD4);*/
				
				CCOUNTRY = (String) map.get("COUNTY");
				CSTATE = (String) map.get("STATE");
				CZIP = (String) map.get("ZIP");
				if((CSTATE).length()>1)
					CNAME=CNAME+", "+CSTATE;
				
				if((CCOUNTRY).length()>1)
					CNAME=CNAME+", "+CCOUNTRY;
				
				if((CZIP).length()>1)
					CNAME=CNAME+"-"+CZIP;
					
					
			}
			
		HSSFRow rowhead = sheet.createRow(0);
		HSSFCell cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString(CNAME));	
		cell.setCellStyle(styleHeader);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 4);
		sheet.addMergedRegion(cellRangeAddress);
											
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFCellStyle createCompStyleHeader(HSSFWorkbook wb){
		
		//Create style
		 HSSFCellStyle styleHeader = wb.createCellStyle();
		  HSSFFont fontHeader  = wb.createFont();
		  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		  fontHeader.setFontName("Arial");	
		  styleHeader.setFont(fontHeader);
		  styleHeader.setFillForegroundColor(HSSFColor.WHITE.index);
		  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		  styleHeader.setWrapText(true);
		  return styleHeader;
	}
	
private HSSFCellStyle createStockTakeStyleHeader(HSSFWorkbook wb){
		
		//Create style
		 HSSFCellStyle styleHeader = wb.createCellStyle();
		  HSSFFont fontHeader  = wb.createFont();
		  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		  fontHeader.setFontName("Arial");	
		  styleHeader.setFont(fontHeader);
		  HSSFPalette palette = ((HSSFWorkbook)wb).getCustomPalette();
		  palette.setColorAtIndex((short)17, (byte)169, (byte)208, (byte)141);
		  styleHeader.setFillForegroundColor(palette.getColor(17).getIndex());
		  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		  styleHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		  styleHeader.setWrapText(true);
		  return styleHeader;
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
