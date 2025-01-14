package com.track.pda;

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
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.StockTakeDAO;
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

public class StockTakeServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.StockTakeServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.StockTakeServlet_PRINTPLANTMASTERINFO;

	private static final long serialVersionUID = -1216082456798213009L;
	private POUtil _POUtil = null;
	private StockTakeUtil _StockTakeUtil = null;

	private String PLANT = "";
	private String xmlStr = "";
	private String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_POUtil = new POUtil();
		_StockTakeUtil = new StockTakeUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
				.getParameter("LOGIN_USER"))
				+ " PDA_USER"));
		_POUtil.setmLogger(mLogger);
		_StockTakeUtil.setmLogger(mLogger);
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		try {
			action = request.getParameter("action").trim();

			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = loadItemDetails(request, response);
			}
			if (action.equalsIgnoreCase("process_stockTake")) {
				xmlStr = "";
				xmlStr = processStockTake(request, response);
			}
			if (action.equalsIgnoreCase("GET_ALL_STOCK_DETAILS")) {
				xmlStr = "";
				xmlStr = getAllStockTakeDetails(request, response);
			}
			if (action.equalsIgnoreCase("REMOVE_STOCK_TAKE")) {
				xmlStr = "";
				xmlStr = removeStockTakeDetail(request, response);
			}
			if (action.equalsIgnoreCase("VALIDATE_PRODUCT")) {
				xmlStr = "";
				xmlStr = validate_item(request, response);
			}
			if (action.equalsIgnoreCase("LOAD_PRODUCT_DETAILS")) {
				xmlStr = "";
				xmlStr = loadProductDetails(request, response);
			}
			if(action.equals("Export_Excel")){
				 String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=StockTake.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
			 }
			
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			if(!action.equals("Export_Excel")){
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
			}
		}

		if(!action.equals("Export_Excel")){
			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.write(xmlStr);
			out.close();
		}
		
	}

	private String validate_item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", item = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			item = StrUtils.fString(request.getParameter("ITEM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("ITEM", item);
			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			boolean itemFound = true;
			String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
			if (scannedItemNo == null) {
				itemFound = false;
			}

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

			if (itemFound) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Product found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Product not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				throw new Exception("Product not found");
			}
		} catch (Exception e) {
			throw e;
		}
		// MLogger.log(0, "validate_item() Ends");
		return xmlStr;
	}

	private String removeStockTakeDetail(HttpServletRequest request,
			HttpServletResponse response) {
		String returnXmlString = "";
		try {
			String plant = "", loginUser = "", loc = "", batch = "", qty = "", item = "";

			plant = StrUtils.fString(request.getParameter("PLANT"));
			loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			batch = StrUtils.fString(request.getParameter("BATCH"));
			qty = StrUtils.fString(request.getParameter("QTY"));
			item = StrUtils.fString(request.getParameter("ITEM"));

			HashMap<String, String> inputHashMap = new HashMap<String, String>();
			inputHashMap.put("PLANT", plant);
			inputHashMap.put("LOC", loc);
			inputHashMap.put("ITEM", item);
			inputHashMap.put("BATCH", batch);
			inputHashMap.put("QTY", qty);
			inputHashMap.put("LOGIN_USER", loginUser);
			StockTakeUtil stm = new StockTakeUtil();
			returnXmlString = stm.removeStockTakeDetail(inputHashMap);

			if (returnXmlString.equalsIgnoreCase("")) {
				returnXmlString = XMLUtils.getXMLMessage(1,
						"Unable to remove the Item!");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			returnXmlString = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return returnXmlString;
	}

	private String getAllStockTakeDetails(HttpServletRequest request,
			HttpServletResponse response) {
		String returnXmlString = "";
		try {
			String plant = "", loginUser = "";

			plant = StrUtils.fString(request.getParameter("PLANT"));
			loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));

			HashMap<String, String> inputHashMap = new HashMap<String, String>();
			inputHashMap.put("PLANT", plant);
			inputHashMap.put("LOGIN_USER", loginUser);
			StockTakeUtil stm = new StockTakeUtil();
			stm.setmLogger(mLogger);
			returnXmlString = stm.getAllStockTakeDetails(inputHashMap);

			if (returnXmlString.equalsIgnoreCase("")) {
				returnXmlString = XMLUtils.getXMLMessage(1,
						"Item details not found!");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			returnXmlString = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return returnXmlString;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String loadItemDetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aItemNum = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));

			str = _POUtil.getItem_Details_xml(PLANT, aItemNum);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	 /*
     ************Modification History*********************************
 	    April 29 2015 Bruhan, Description: To include REMARKS
 	*/
	private String processStockTake(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map<String, String> receiveMaterial_HM = null;
		String PLANT = "", ITEM = "", LOC = "", BATCH = "", QTY = "", LOGIN_USER = "", UOM = "",REMARKS="";

		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			ITEM = StrUtils.fString(request.getParameter("ITEM"));
			LOC = StrUtils.fString(request.getParameter("LOC"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			QTY = StrUtils.fString(request.getParameter("QTY"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			UOM = StrUtils.fString(request.getParameter("UOM"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));

			double qty = Double.parseDouble(QTY);
			qty = StrUtils.RoundDB(qty, IConstants.DECIMALPTS);
			QTY = String.valueOf(qty);
			
			//check for existence of item
			boolean result=new ItemUtil().isExistsItemMst(ITEM,PLANT);
			
			
			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, PLANT);
			htInvMst.put(IDBConstants.ITEM, ITEM);
			htInvMst.put("BATCH", BATCH);
			htInvMst.put(IDBConstants.LOC, LOC);
			htInvMst.put("STATUS", "N");
			
			String STKQTY="0";
            ArrayList StklistQry = new StockTakeDAO().selectInvMstDetails(" isnull(qty,0) as qty ",htInvMst,"");
            if (StklistQry.size() > 0) {	               
          	   for(int i =0; i<StklistQry.size(); i++) {
                    Map arrCustLine = (Map)StklistQry.get(i);
                    STKQTY = (String)arrCustLine.get("qty");
          	   }
             }
			
			String INVQTY="0",INVFLAG="0";
			InvMstDAO  _InvMstDAO  = new InvMstDAO();
            ArrayList listQry = _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOMWithNegQty(PLANT,ITEM,LOC,BATCH,UOM);
            if (listQry.size() > 0) {	               
         	   for(int i =0; i<listQry.size(); i++) {
                   Map arrCustLine = (Map)listQry.get(i);
                   INVQTY = (String)arrCustLine.get("qty");
         	   }
            }
            
            double stk = Double.parseDouble(QTY)+Double.parseDouble(STKQTY);
			double invqty = Double.parseDouble(INVQTY);
//			double diff = (invqty - qty);
			double diff = (invqty - stk);
			diff = StrUtils.RoundDB(diff, IConstants.DECIMALPTS);
			String diffval = String.valueOf(diff);
			
			if(qty==invqty)
				INVFLAG="1";
				
			receiveMaterial_HM = new HashMap<String, String>();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM);
			receiveMaterial_HM.put(IConstants.LOC, LOC);
			receiveMaterial_HM.put(IConstants.BATCH, BATCH);
			receiveMaterial_HM.put(IConstants.QTY, QTY);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put("UOM", UOM);
			receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
			
			receiveMaterial_HM.put("INVFLAG", INVFLAG);
			receiveMaterial_HM.put("DIFFQTY", diffval);

			xmlStr = "";
			xmlStr = _StockTakeUtil.process_StockTake(receiveMaterial_HM);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	private String loadProductDetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "";
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			Hashtable itemht = new Hashtable();
			itemht.put("PLANT", aPlant);
			itemht.put("ITEM", aItem);
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			boolean isexits = true;
			String scannedItemNo = itemMstDAO.getItemIdFromAlternate(aPlant,
					aItem);
			if (scannedItemNo == null) {
				isexits = false;
			}
			if (isexits) {
				String itemDesc = StrUtils.fString(itemMstDAO.getItemDesc(
						aPlant, scannedItemNo));
				String uom = StrUtils.fString(itemMstDAO.getItemMutiUOM(aPlant,scannedItemNo,"INVENTORYUOM"));
				// /
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");

				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", scannedItemNo);
				xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
						.replaceCharacters2SendPDA(itemDesc).toString());
				xmlStr += XMLUtils.getXMLNode("uom", uom);

				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Product Not found");
			}
			// /

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product Not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	

	 /* ************Modification History*********************************
	   Sep 23 2014, Description: To include USER
	   April 29 2015 Bruhan, Description: To include REMARKS
	*/
	private HSSFWorkbook writeToExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",BATCH="",FROM_DATE="",TO_DATE="",LOC_TYPE_ID="";
		int maxRowsPerSheet = 65535;
		int SheetId =1;
		ArrayList stockTakeList = new ArrayList();
		try{
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			String itemNo = StrUtils.fString(request.getParameter("ITEM")).trim();		         
		    String itemDesc = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("PRD_DESCRIP"))).trim();
			String location = StrUtils.fString(request.getParameter("LOC")).trim();
			String slocation = StrUtils.fString(request.getParameter("LOC_ID")).trim();
			BATCH=StrUtils.fString(request.getParameter("BATCH")).trim();
			//Start code added by Bruhan for product brand,type on 19/sep/13
			PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			//End code added by Bruhan for product brand,type on 19/sep/13 
			FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
			String USER= StrUtils.fString(request.getParameter("USER"));
			String reportType ="";
			
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	         
			


			Boolean isWithCost = Boolean.valueOf(StrUtils.fString(request.getParameter("isWithCost")).trim());
			
			Hashtable ht = new Hashtable();

		      StrUtils strUtils = new StrUtils();
			  if(strUtils.fString(location).length() > 0)             ht.put("a.LOC",location);
			  if(strUtils.fString(slocation).length() > 0)             ht.put("a.LOC",slocation); // For Stock reset parameter as LOC_ID
	          if(strUtils.fString(BATCH).length() > 0)               ht.put("a.BATCH",BATCH);
	          //if(strUtils.fString(LOC_TYPE_ID).length() > 0)         ht.put("b.LOC_TYPE_ID",LOC_TYPE_ID);
	       	 
	          stockTakeList = new InvUtil().getStockTakeDetails(ht,plant,itemNo,itemDesc,PRD_CLS_ID,PRD_BRAND_ID,PRD_TYPE_ID,FROM_DATE,TO_DATE,LOC_TYPE_ID,USER);
				 Boolean workSheetCreated = true;
				 if (stockTakeList.size() > 0) {
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						HSSFCellStyle CompHeader = null;
						sheet = wb.createSheet("Sheet"+SheetId);
						dataStyle = createDataStyle(wb);	
						 //sheet = wb.createSheet();
						 styleHeader = createStyleHeader(wb);
						 CompHeader = createCompStyleHeader(wb);
						 sheet = this.createWidth(sheet,isWithCost);
						 sheet = this.createHeader(sheet,styleHeader,isWithCost);
						 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
						 int index = 2;
				         double invqty,pcsqty = 0;
				         double stockqty=0;
				          
				            String strDiffQty,strpcsDiffQty=""; 
				            
						 for (int iCnt =0; iCnt<stockTakeList.size(); iCnt++){	
								   Map lineArr = (Map) stockTakeList.get(iCnt);	
								   int k = 0;
								    
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
		                            Double totalPcsCost = unitCost*(new Double((String) lineArr.get("PCSQTY")));
		                			Double totalInventeryCost = unitCost*(new Double((String) lineArr.get("QTY")));
		                			Double totalDiffentetCost = unitCost*(new Double((String) strDiffQty));
		                			Double totalpcsDiffentetCost = unitCost*(new Double((String) strpcsDiffQty));
		                			//java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		                                                                          
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
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TRANDATE"))));
									cell.setCellStyle(dataStyle);
															   
																										
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRDCLSID"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMTYPE"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_BRAND_ID"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATCH"))));
									cell.setCellStyle(dataStyle);
									
									
									if(isWithCost){
										
										String unitCostValue = (String)lineArr.get("UNITCOST");
										/*float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
							    		if(unitCostVal==0f){
							    			unitCostValue="0.00000";
							    		}else{
							    			unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}*/
										
										double unitCostVal = "".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
										unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(unitCostValue);
										cell.setCellStyle(dataStyle);
									}
									String stockQtyValue = (String)lineArr.get("STOCKQTY");
									float stockQtyVal ="".equals(stockQtyValue) ? 0.0f :  Float.parseFloat(stockQtyValue);
								    
						    		if(stockQtyVal==0f){
						    			stockQtyValue="0.000";
						    		}else{
						    			stockQtyValue=stockQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}
									cell = row.createCell((short) k++);
									cell.setCellValue(stockQtyValue);
									cell.setCellStyle(dataStyle);
									
									if(isWithCost){
										String totalAvalableCostValue = String.valueOf(totalAvalableCost);
										/*float totalAvalableCostVal ="".equals(totalAvalableCostValue) ? 0.0f :  Float.parseFloat(totalAvalableCostValue);
									    
							    		if(totalAvalableCostVal==0f){
							    			totalAvalableCostValue="0.00000";
							    		}else{
							    			totalAvalableCostValue=totalAvalableCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}*/
										
										double totalAvalableCostVal = "".equals(totalAvalableCostValue) ? 0.0d :  Double.parseDouble(totalAvalableCostValue);
										totalAvalableCostValue = StrUtils.addZeroes(totalAvalableCostVal, numberOfDecimal);
										cell = row.createCell((short) k++);
										cell.setCellValue(totalAvalableCostValue);
										cell.setCellStyle(dataStyle);
									}
									
									String qtyValue = (String)lineArr.get("QTY");
									float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
								    
						    		if(qtyVal==0f){
						    			qtyValue="0.000";
						    		}else{
						    			qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}
									
									cell = row.createCell((short) k++);
									cell.setCellValue(qtyValue);
									cell.setCellStyle(dataStyle);
									
									if(isWithCost){
										String totalInventeryCostValue = String.valueOf(totalInventeryCost);
										/*float totalInventeryCostVal ="".equals(totalInventeryCostValue) ? 0.0f :  Float.parseFloat(totalInventeryCostValue);
									    
							    		if(totalInventeryCostVal==0f){
							    			totalInventeryCostValue="0.00000";
							    		}else{
							    			totalInventeryCostValue=totalInventeryCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}*/
										
										double totalInventeryCostVal = "".equals(totalInventeryCostValue) ? 0.0d :  Double.parseDouble(totalInventeryCostValue);
										totalInventeryCostValue = StrUtils.addZeroes(totalInventeryCostVal, numberOfDecimal);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(totalInventeryCostValue);
										cell.setCellStyle(dataStyle);
									}
									String pcsQtyValue = (String)lineArr.get("PCSQTY");
									float pcsQtyVal ="".equals(pcsQtyValue) ? 0.0f :  Float.parseFloat(pcsQtyValue);
								    
						    		if(pcsQtyVal==0f){
						    			pcsQtyValue="0.000";
						    		}else{
						    			pcsQtyValue=pcsQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}
									cell = row.createCell((short) k++);
									cell.setCellValue(pcsQtyValue);
									cell.setCellStyle(dataStyle);
									
									if(isWithCost){
										String totalPcsCostValue = String.valueOf(totalPcsCost);
										/*float totalPcsCostVal ="".equals(totalPcsCostValue) ? 0.0f :  Float.parseFloat(totalPcsCostValue);
									    
							    		if(totalPcsCostVal==0f){
							    			totalPcsCostValue="0.00000";
							    		}else{
							    			totalPcsCostValue=totalPcsCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}*/
										double totalPcsCostVal = "".equals(totalPcsCostValue) ? 0.0d :  Double.parseDouble(totalPcsCostValue);
										totalPcsCostValue = StrUtils.addZeroes(totalPcsCostVal, numberOfDecimal);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(totalPcsCostValue);
										cell.setCellStyle(dataStyle);
									}
									String strDiffQtyValue = String.valueOf(strDiffQty);
									float strDiffQtyVal ="".equals(strDiffQtyValue) ? 0.0f :  Float.parseFloat(strDiffQtyValue);
								    
						    		if(strDiffQtyVal==0f){
						    			strDiffQtyValue="0.000";
						    		}else{
						    			strDiffQtyValue=strDiffQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}
									cell = row.createCell((short) k++);
									cell.setCellValue(strDiffQtyValue);
									cell.setCellStyle(dataStyle);
									
									if(isWithCost){
										String totalpcsDiffentetCostValue = String.valueOf(totalDiffentetCost);
										/*float totalpcsDiffentetCostVal ="".equals(totalpcsDiffentetCostValue) ? 0.0f :  Float.parseFloat(totalpcsDiffentetCostValue);
									    
							    		if(totalpcsDiffentetCostVal==0f){
							    			totalpcsDiffentetCostValue="0.00000";
							    		}else{
							    			totalpcsDiffentetCostValue=totalpcsDiffentetCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}*/
										
										double totalpcsDiffentetCostVal = "".equals(totalpcsDiffentetCostValue) ? 0.0d :  Double.parseDouble(totalpcsDiffentetCostValue);
										totalpcsDiffentetCostValue = StrUtils.addZeroes(totalpcsDiffentetCostVal, numberOfDecimal);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(totalpcsDiffentetCostValue);
										cell.setCellStyle(dataStyle);
									}
									String strpcsDiffQtyValue = String.valueOf(strpcsDiffQty);
									/*float strpcsDiffQtyVal ="".equals(strpcsDiffQtyValue) ? 0.0f :  Float.parseFloat(strpcsDiffQtyValue);
						    		if(strpcsDiffQtyVal==0f){
						    			strpcsDiffQtyValue="0.000";
						    		}else{
						    			strpcsDiffQtyValue=strpcsDiffQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}*/
									double strpcsDiffQtyVal = "".equals(strpcsDiffQtyValue) ? 0.0d :  Double.parseDouble(strpcsDiffQtyValue);
									strpcsDiffQtyValue = StrUtils.addZeroes(strpcsDiffQtyVal, numberOfDecimal);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(strpcsDiffQtyValue);
									cell.setCellStyle(dataStyle);
									
									if(isWithCost){
										String totalDiffentetCostValue = String.valueOf(totalpcsDiffentetCost);
										/*float totalDiffentetCostVal ="".equals(totalDiffentetCostValue) ? 0.0f :  Float.parseFloat(totalDiffentetCostValue);
							    		if(totalDiffentetCostVal==0f){
							    			totalDiffentetCostValue="0.00000";
							    		}else{
							    			totalDiffentetCostValue=totalDiffentetCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}*/
										double totalDiffentetCostVal = "".equals(totalDiffentetCostValue) ? 0.0d :  Double.parseDouble(totalDiffentetCostValue);
										totalDiffentetCostValue = StrUtils.addZeroes(totalDiffentetCostVal, numberOfDecimal);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(totalDiffentetCostValue);
										cell.setCellStyle(dataStyle);
									}
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("USERID"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARKS"))));
									cell.setCellStyle(dataStyle);
									index++;
									if((index-2)%maxRowsPerSheet==0){
										 index = 2;
										 SheetId++;
										 sheet = wb.createSheet("Sheet"+SheetId);
										 styleHeader = createStyleHeader(wb);
										 CompHeader = createCompStyleHeader(wb);
										 sheet = this.createWidth(sheet,isWithCost);
										 sheet = this.createHeader(sheet,styleHeader,isWithCost);
										 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
										 
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
	
	
	/* ************Modification History*********************************
	   Sep 23 2014, Description: To include USER
	   April 29 2015, Description: To include REMARKS
	*/
	 private HSSFSheet createHeader(HSSFSheet sheet, HSSFCellStyle styleHeader,Boolean isWithCost){
			int k = 0;
			try{
			
			
			HSSFRow rowhead = sheet.createRow((short) 1);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("LOC"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("TRANDATE"));
			cell.setCellStyle(styleHeader);
					
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT CLASS"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT TYPE"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT BRAND"));
			cell.setCellStyle(styleHeader);
			
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("BATCH"));
			cell.setCellStyle(styleHeader);
			
			if(isWithCost){
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("UNIT COST"));
				cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("STOCKTAKE QUANTITY"));
			cell.setCellStyle(styleHeader);
			
			if(isWithCost){
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("STOCKTAKE COST"));
			cell.setCellStyle(styleHeader);
			}		
			
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("INVENTORY QUANTITY"));
			cell.setCellStyle(styleHeader);
			
			if(isWithCost){
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("INVENTORY COST"));
			cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PC/PCS/EA QUANTITY"));
			cell.setCellStyle(styleHeader);
			
			if(isWithCost){
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PC/PCS/EA COST"));
			cell.setCellStyle(styleHeader);
			}			
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("INVENTORY QUANTITY DIFF"));
			cell.setCellStyle(styleHeader);
			
			if(isWithCost){
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("INVENTORY COST DIFF"));
			cell.setCellStyle(styleHeader);
			}
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PC/PCS/EA QUANTITY DIFF"));
			cell.setCellStyle(styleHeader);
			
			if(isWithCost){
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("PC/PCS/EA COST DIFF"));
			cell.setCellStyle(styleHeader);
			}	
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("USER"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS"));
			cell.setCellStyle(styleHeader);
					
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
	 
	 /* ************Modification History*********************************
	   Sep 23 2014, Description: To include USER width
	    April 29 2015, Description: To include REMARKS width
	*/
	 
	private HSSFSheet createWidth(HSSFSheet sheet,Boolean isWithCost){
		int i = 0;
		try{
			sheet.setColumnWidth((short)i++ ,(short)4500);
			sheet.setColumnWidth((short)i++ ,(short)3500);
			sheet.setColumnWidth((short)i++ ,(short)4200);
			sheet.setColumnWidth((short)i++ ,(short)4200);
			sheet.setColumnWidth((short)i++ ,(short)5200);
			sheet.setColumnWidth((short)i++ ,(short)4200);
			sheet.setColumnWidth((short)i++ ,(short)6500);
			
			if(isWithCost){
				sheet.setColumnWidth((short)i++ ,(short)3500);
			}
			sheet.setColumnWidth((short)i++ ,(short)3500);
			if(isWithCost){
				sheet.setColumnWidth((short)i++ ,(short)3500);
			}
			sheet.setColumnWidth((short)i++ ,(short)2500);
			if(isWithCost){
				sheet.setColumnWidth((short)i++ ,(short)3500);
			}
			sheet.setColumnWidth((short)i++ ,(short)2500);
			if(isWithCost){
				sheet.setColumnWidth((short)i++ ,(short)3500);
			}
			sheet.setColumnWidth((short)i++ ,(short)2500);
			if(isWithCost){
				sheet.setColumnWidth((short)i++ ,(short)3500);
			}
			
			sheet.setColumnWidth((short)i++ ,(short)3500);
			sheet.setColumnWidth((short)i++ ,(short)5000);
			
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
