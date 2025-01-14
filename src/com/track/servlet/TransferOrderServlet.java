package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
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
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderTypeUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TOUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.encryptBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*********************Modification History***********************************
 * Bruhan,Nov 10 2014,To update Add Products,Add product,Updatetodet and Delete to allow Transfer order to amend,add products once a order was processed
 * 
 */
public class TransferOrderServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.TransferOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TransferOrderServlet_PRINTPLANTMASTERINFO;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	String action = "";
	String xmlStr = "";
	TOUtil _TOUtil = null;
	StrUtils strUtils = new StrUtils();
	EmailMsgDAO emailDao =null;
	EmailMsgUtil mailUtil = null;
	TblControlDAO _TblControlDAO = null;
	ToHdrDAO _ToHdrDAO = null;
	PlantMstDAO _PlantMstDAO = null;
	LocUtil _locUtil = null;
	InvMstDAO _InvMstDAO = null;
	HTReportUtil htutil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_TOUtil = new TOUtil();
		emailDao =  new EmailMsgDAO();
		 mailUtil = new EmailMsgUtil();
		 _TblControlDAO = new TblControlDAO();
		 _ToHdrDAO = new ToHdrDAO();
		 _locUtil = new LocUtil();
		 _InvMstDAO = new InvMstDAO();
		 htutil = new HTReportUtil();
	}

	@SuppressWarnings("static-access")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			action = StrUtils.fString(request.getParameter("Submit")).trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
		
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			_TOUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			String tono = StrUtils.fString(request.getParameter("TONO")).trim();
			String rflag = StrUtils.fString(
					(String) request.getSession().getAttribute("RFLAG")).trim();
		

			String statusValue = StrUtils.fString(
					request.getParameter("statusValue")).trim();
			
			if (action.equalsIgnoreCase("GET_ORDER_NUM_FOR_AUTO_SUGGESTION")) {	
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderNumForAutoSuggestion(request);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} 
			
			if (action.equalsIgnoreCase("GET_ORDER_NUM_FOR_FORCE_CLOSE")) {	
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderNumForForceClose(request);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} 
			else if (action.equalsIgnoreCase("GET_GINO_NO_FOR_AUTO_SUGGESTION")) { 
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getgiForAutoSuggestion(request);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();				
			}
			
			if (action.equalsIgnoreCase("VIEW_CONSIGNMENT_DETAILS_PRINT")) {	
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getConsignmentDetailsToPrint(request);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} 
			
			if (action.equalsIgnoreCase("VIEW_CONSIGNMENT_DETAILS_PRINT_WITH_PRICE")) {	
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getConsignmentDetailsToPrintWithPrice(request);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} 
			
		
			if (action.equalsIgnoreCase("View")) {

				boolean flag = DisplayData(request, response);

				if (flag == true) {
					if (rflag.equals("1")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/CreateTransferOrder.jsp?TONO="
										+ tono + "&action=View");
					} else if (rflag.equals("3")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/CreateTransferOrder.jsp?TONO="
										+ tono + "&action=View");
					} else if (rflag.equals("4")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/maintTransferOrder.jsp?statusValue="
										+ statusValue
										+ "&TONO="
										+ tono
										+ "&action=View");
					} else if (rflag.equals("5")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/TransferOrderSummary.jsp?TONO="
										+ tono + "&action=View");
					} else if (rflag.equals("6")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/TransferOrderReceiving.jsp?TONO="
										+ tono + "&action=View");
					} 
                                            else if (rflag.equals("7")) {
                                                    tono = (String) request.getSession().getAttribute(
                                                                    "tono");
                                                    request.getSession().setAttribute("tono", "");
                                                    response
                                                                    .sendRedirect("jsp/TOSummaryForSingleStepPickIssue.jsp?TONO="
                                                                                    + tono + "&action=View");
                                             } else {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/CreateTransferOrder.jsp?TONO="
										+ tono + "&action=View");
					}
				}

			}
			else if (action.equalsIgnoreCase("MultipleView")) {

				boolean flag = DisplayDataForMultipleView(request, response);

				if (flag == true) {
					 if (rflag.equals("5")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/MultipleTransferOrderSummary.jsp?TONO="
										+ tono + "&action=MultipleView");
					} else if (rflag.equals("6")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/MultiTransferOrderReceiving.jsp?TONO="
										+ tono + "&action=MultipleView");
					}
					//Start added by Bruhan for Bulk receiving on 3 Aug 2012.	
					else if (rflag.equals("7")) {
							tono = (String) request.getSession().getAttribute(
									"tono");
							request.getSession().setAttribute("tono", "");
							response
									.sendRedirect("jsp/BulkTransferOrderReceiving.jsp?TONO="
											+ tono + "&action=MultipleView");
					}
					//End added by Bruhan for Bulk receiving on 3 Aug 2012.
					//Start added by Bruhan for Bulk issuing on 12 Mar 2013.	
					else if (rflag.equals("8")) {
							tono = (String) request.getSession().getAttribute(
									"tono");
							request.getSession().setAttribute("tono", "");
							response
									.sendRedirect("jsp/BulkTransferOrderPickIssue.jsp?TONO="
											+ tono + "&action=MultipleView");
					}
					//End added by Bruhan for Bulk issuing on 12 Mar 2013.
					
					//Start added by Bruhan for Transfer order reversal on 20 August 2013.	
					else if (rflag.equals("9")) {
							tono = (String) request.getSession().getAttribute(
									"tono");
							String gino = (String) request.getSession().getAttribute(
									"gino");
							request.getSession().setAttribute("tono", "");
							request.getSession().setAttribute("gino", "");
							response
									.sendRedirect("../consignment/orderreversal?TONO="
											+ tono + "&GINO= "+ gino +"&action=MultipleView");
					}
					//End added by Bruhan for Transfer order reversal on 20 August 2013.
					
					else if (rflag.equals("10")) {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("../consignment/orderpick?TONO="
										+ tono + "&action=MultipleView");
				      }
				
					else {
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						response
								.sendRedirect("jsp/CreateTransferOrder.jsp?TONO="
										+ tono + "&action=View");
					}
				}

			}
			
			//Start code by Bruhan for transferorder by product on 23 sep 2013
			else if (action.equalsIgnoreCase("ViewProductOrders")) {

				String item = request.getParameter("ITEM");
				String type = request.getParameter("TYPE");
                                ItemMstDAO itemdao = new ItemMstDAO();
                                HttpSession session = request.getSession();
                                plant = (String)session.getAttribute("PLANT");
                                String desc = itemdao.getItemDesc(plant,item);
                                desc=StrUtils.replaceCharacters2Send(desc);
                if(type.equalsIgnoreCase("pickrecvbyprd"))
                {
                	response.sendRedirect("jsp/TOPickReceiveByProduct.jsp?ITEM="
		                    + item + "&DESC="+desc+"&action=View");
                }
                else{
			    response.sendRedirect("jsp/TransferOrderByProduct.jsp?ITEM="
			                    + item + "&DESC="+desc+"&action=View");}
			} 
			
			else if (action.equalsIgnoreCase("BulkIssuebyProd")) {
				xmlStr = "";
				xmlStr = OutGoingIssueBulkDatabyProd(request, response);

			}
			//End code by Bruhan for transferorder by product on 23 sep 2013
			
			
			else if (action.equalsIgnoreCase("SAVE")) {
				String result = "";
				result = SaveToData(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");

			} else if (action.equalsIgnoreCase("print")) {
				String ordno = StrUtils.fString(request.getParameter("TONO"));
				String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";
				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();
				if (ordno.length() > 0) {
					htCond.put("PLANT", plant);
					htCond.put("TONO", ordno);
				    String query = "tono,custName,custCode,jobNum,custName,fromwarehouse,towarehouse,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip ,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as sTelno";
					al = _TOUtil.getOutGoingToHdrDetails(query, htCond);
					if (al.size() > 0) {
						m = (Map) al.get(0);
						fieldDesc = _TOUtil.listTODET(plant, tono, rflag);
					} else {
						fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";
					}

				}

				request.getSession().setAttribute("todetVal", m);
				request.getSession().setAttribute("tono", tono);
				request.getSession().setAttribute("RESULT1", fieldDesc);
				response.sendRedirect("jsp/CreateTransferOrder_Print.jsp?TONO="
						+ ordno + "&action=View");

			} else if (action.equalsIgnoreCase("REMOVE_TO")) {
				String User_id = StrUtils.fString(
						request.getParameter("LOGIN_USER")).trim();

				Hashtable httoHrd = new Hashtable();
				httoHrd.put(IDBConstants.PLANT, plant);
				httoHrd.put("TONO", tono);
				boolean isValidOrder = new ToHdrDAO().isExisit(httoHrd,"");
				boolean isOrderInProgress = new ToDetDAO().isExisit(httoHrd, " PICKSTATUS in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM ["+plant+"_ITEMMST] where NONSTKFLAG='Y')");
				if(isValidOrder){
					if (!isOrderInProgress) {
				
						Boolean value = this._TOUtil.removeRow(plant, tono,
								User_id);
						if (value) {
							response.sendRedirect("/track/TransferOrderServlet?statusValue=100&TONO=" + tono + "&Submit=View&rflag=4");
						} else {
							response.sendRedirect("/track/TransferOrderServlet?statusValue=97&TONO="+ tono + "&Submit=View&rflag=4");
						}
					} else {
						response.sendRedirect("/track/TransferOrderServlet?statusValue=98&TONO=" + tono + "&Submit=View&rflag=4");
					}
				}else{
					response.sendRedirect("/track/TransferOrderServlet?statusValue=99&TONO="+ tono + "&Submit=View&rflag=4");
				}
				
			}			
		//CREATED BY NAVAS FEB20
		else if (action.equals("ExportExcelConsignmentSummaryWithRemarks")) {

			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelConsignmentSummaryWithRemarks(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=ConsignmentSummary.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();

		}
		//END BY NAVAS
		
		//CREATED BY NAVAS FEB20
		else if (action.equals("ExportExcelConsignmentWOOrderSummary")) {

			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelConsignmentWOOrderSummary(request, response, wb);
			String type = strUtils.fString(request.getParameter("DIRTYPE"));				
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			
			if(type.equalsIgnoreCase("OB_SUMMARY_ISSUE")) {
				response.setHeader("Content-Disposition", "attachment; filename=ConsignmentSummaryDetails.xls");
			}else if(type.equalsIgnoreCase("OB_SUMMARY_ISS_WITH_PRICE")) {
				response.setHeader("Content-Disposition", "attachment; filename=ConsignmentSummaryByPrice.xls");
			}
			else if (type.equalsIgnoreCase("CONSIGNMENT"))
				response.setHeader("Content-Disposition", "attachment; filename=ConsignmentSummary.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();

		}
		//END BY NAVAS
		

	
			else if (action.equalsIgnoreCase("Delete")) {
				try {
					boolean flag = false;
					String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";
					String User_id = StrUtils.fString(
							request.getParameter("LOGIN_USER")).trim();
					String ordno = StrUtils.fString(
							request.getParameter("TONO")).trim();
					String ordlno = StrUtils.fString(
							request.getParameter("TOLNNO")).trim();
					String item = StrUtils
							.fString(request.getParameter("ITEM")).trim();
					Hashtable htdet = new Hashtable();
					htdet.put(IDBConstants.PLANT, plant);
					htdet.put(IDBConstants.TODET_TONUM, ordno);
					htdet.put(IDBConstants.TODET_TOLNNO, ordlno);
					htdet.put(IDBConstants.TODET_ITEM, item);
					htdet.put(IDBConstants.CREATED_BY, userName);
					flag = _TOUtil.deleteTransferDetLineDetails(htdet);
					if (flag) {
					//TOHDR Update
            		     String updateToHdr = "";
                  	     boolean updateHdrFlag  =false;
                		 boolean isExitFlag=false;
                		 ToHdrDAO _ToHdrDAO = new ToHdrDAO();
                		 ToDetDAO _ToDetDAO = new ToDetDAO();
                		 Hashtable htConditoHdr = new Hashtable();
     				     htConditoHdr.put("PLANT", plant);
     				     htConditoHdr.put("tono", ordno);
     				     
 	                    isExitFlag = _ToDetDAO.isExisit(htConditoHdr,	"pickStatus in ('C') and qtypick <> qtyrc   ");
 				    	if (isExitFlag)
 				    	{
 				    		updateToHdr = "set  Status='O',PickStaus='C' ";
 							updateHdrFlag=true;
 				    	}
 						else
 						{
 							isExitFlag = _ToDetDAO.isExisit(htConditoHdr,	"pickStatus in ('C') and qtypick = qtyrc ");
 							if (isExitFlag)
 	 				    	{
 								updateToHdr = "set  Status='C',PickStaus='C' ";
 								updateHdrFlag=true;
 	 				    	}
 				    	}
 				    	
 				    	if(updateHdrFlag==true){
 	        				 flag =_ToHdrDAO.update(updateToHdr, htConditoHdr, "");
 	           			}
 				    	//TOHDR Update
 				    	
						ArrayList al = new ArrayList();
						Hashtable htCond = new Hashtable();
						Map m = new HashMap();
						if (ordno.length() > 0) {
							htCond.put("PLANT", plant);
							htCond.put("TONO", ordno);
						    String query = "tono,custName,custCode,jobNum,custName,fromwarehouse,towarehouse,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip ,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as sTelno";
							al = _TOUtil.getOutGoingToHdrDetails(query, htCond);
							if (al.size() > 0) {
								m = (Map) al.get(0);
								fieldDesc = _TOUtil.listTODET(plant, tono,
										rflag);
							} else {

								fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";

							}

						}
						request.getSession().setAttribute("todetVal", m);
						request.getSession().setAttribute("tono", tono);
						request.getSession().setAttribute("RESULT1", fieldDesc);
						if (rflag.equals("1")) {
							response
									.sendRedirect("jsp/CreateTransferOrder.jsp?TONO="
											+ ordno + "&action=View");
						} else {
							response
									.sendRedirect("jsp/maintTransferOrder.jsp?TONO="
											+ ordno + "&action=View");
						}
					} else {
						throw new Exception(
								"Product ID Not Deleted Successfully");
					}
				} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);

					String result = "<font class = " + IConstants.FAILED_COLOR
							+ ">Error : " + ex.getMessage() + "</font>";
					result = result
							+ " <br><br><center> "
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "Consignment Order<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");
				}

			}
			else if(action.equalsIgnoreCase("Updatetodet")){ 

				try {
					String tolno = "", item = "", itemDesc = "";
				    tono = StrUtils.fString(request.getParameter("TONO")).trim();
					tolno = StrUtils.fString(request.getParameter("TOLNNO")).trim();
					item = StrUtils.fString(request.getParameter("ITEM")).trim();
					itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
					String qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")).trim());
					String productRemarks = StrUtils.fString(request.getParameter("PRDREMARKS")).trim();
					Hashtable htUpdatetodet = new Hashtable();
					htUpdatetodet.clear();
					htUpdatetodet.put(IDBConstants.PLANT, plant);
					htUpdatetodet.put(IDBConstants.TODET_TONUM, tono);
					htUpdatetodet.put("TOLNNO", tolno);
					htUpdatetodet.put(IDBConstants.ITEM, item);
					htUpdatetodet.put(IDBConstants.ITEM_DESC, strUtils.InsertQuotes(itemDesc));
					htUpdatetodet.put(IDBConstants.TODET_COMMENT1,strUtils.InsertQuotes(productRemarks));
					htUpdatetodet.put("ORDQTY", qty);
					
					boolean flag = _TOUtil.updateToDetDetails(htUpdatetodet);
					

					  MovHisDAO movhisDao = new MovHisDAO();
	                  Hashtable htmovHis = new Hashtable();
	                  movhisDao.setmLogger(mLogger);
	                  htmovHis.clear();
	                  htmovHis.put(IDBConstants.PLANT, plant);
	                  htmovHis.put("DIRTYPE", "TO_UPD_ITEM");
	                  htmovHis.put(IDBConstants.ITEM,item);
	                  htmovHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
	                  htmovHis.put(IDBConstants.MOVHIS_ORDLNO, tolno);
	                  htmovHis.put("QTY", qty);
	                  htmovHis.put("REMARKS", strUtils.InsertQuotes(productRemarks));
	                  htmovHis.put(IDBConstants.CREATED_BY,userName);
	                  htmovHis.put("MOVTID", "");
	                  htmovHis.put("RECID", "");
	                  htmovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
	                  htmovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());  
	                  flag = movhisDao.insertIntoMovHis(htmovHis);
					
					if (flag) {
						flag = DisplayData(request, response);
						tono = (String) request.getSession().getAttribute(
								"tono");
						request.getSession().setAttribute("tono", "");
						if (rflag.equals("1")) {
							response
									.sendRedirect("jsp/CreateTransferOrder.jsp?TONO="
											+ tono + "&action=View");
						} else {
							response
									.sendRedirect("jsp/maintTransferOrder.jsp?TONO="
											+ tono + "&action=View");
						}
					}
						

					 else {
						 throw new Exception(
									"Product ID Not updated Successfully");
					}
				} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);

					String result = "<font class = " + IConstants.FAILED_COLOR
							+ ">Error : " + ex.getMessage() + "</font>";
					result = result
							+ " <br><br><center> "
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "Consignment Order<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");
				}

			
			}

			else if (action.equalsIgnoreCase("Add Products")) {
				String result = "";
				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				tono = StrUtils.fString(request.getParameter("TONO")).trim();
				String RFLAG = StrUtils.fString(request.getParameter("RFLAG"))
						.trim();
				HttpSession session = request.getSession();
				htCond.put("PLANT", plant);
				htCond.put("TONO", tono);

				String query = "tono,custCode,custName,jobNum";
				al = _TOUtil.getToHdrDetails(query, htCond);
				if (al.size() < 1) {

					result = "<font color=\"red\"> Please Save the  Consignment order first before adding Product ID  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");

				} else {
					if(DbBean.TO_MODIFICATION.equals("Y"))
					{
						Map m = (Map) al.get(0);
						tono = (String) m.get("tono");
						String custName = (String) m.get("custName");
						String custCode = (String) m.get("custCode");
						response
								.sendRedirect("jsp/createTODET.jsp?Submit=add&TONO="
										+ tono
										+ "&CUST_CODE="
										+ custCode
										+ "&RFLAG="
										+ RFLAG
										+ "&CUST_NAME="
										+ StrUtils.replaceCharacters2Send(custName));
					}
					else
					{
						boolean flag = _TOUtil.isOpenTransferOrder(plant, tono);
						   if (!flag){
	                          throw new Exception(" Processed Consignment Order Cannot be modified");
					      }
						   
						   Map m = (Map) al.get(0);
							tono = (String) m.get("tono");
							String custName = (String) m.get("custName");
							String custCode = (String) m.get("custCode");
							response.sendRedirect("jsp/createTODET.jsp?Submit=add&TONO="
										+ tono
										+ "&CUST_CODE="
										+ custCode
										+ "&RFLAG="
										+ RFLAG
										+ "&CUST_NAME="
										+ StrUtils.replaceCharacters2Send(custName));
					
                    }
						
				}
			} else if (action.equalsIgnoreCase("Add Product")) {
				String custName = "", custCode = "", jobNum = "", tolno = "", item = "", itemDesc = "", result = "";
				MovHisDAO movHisDao = new MovHisDAO();
				movHisDao.setmLogger(mLogger);
				DateUtils dateUtils = new DateUtils();
				tono = StrUtils.fString(request.getParameter("TONO")).trim();
				tolno = StrUtils.fString(request.getParameter("TOLNNO")).trim();
				String user_id = StrUtils.fString(
						request.getParameter("LOGIN_USER")).trim();
				custCode = StrUtils.fString(request.getParameter("CUST_CODE"))
						.trim();
				custName = StrUtils.fString(request.getParameter("CUST_NAME"))
						.trim();
				item = StrUtils.fString(request.getParameter("ITEM")).trim();
				itemDesc = StrUtils.fString(request.getParameter("DESC"))
						.trim();
				String qty = StrUtils.fString(request.getParameter("QTY"))
						.trim();
				String uom = StrUtils.fString(request.getParameter("UOM"))
						.trim();
				String RFLAG = StrUtils.fString(request.getParameter("RFLAG"))
						.trim();
			       String productRemarks = StrUtils.fString(request.getParameter("PRDREMARKS")).trim();
				ItemMstUtil itemMstUtil = new ItemMstUtil();
				itemMstUtil.setmLogger(mLogger);
				String temItem = itemMstUtil.isValidAlternateItemInItemmst(
						plant, item);
				if (temItem != "") {
					item = temItem;
				} else {
					throw new Exception("Product not found!");
				}

				double quanty = Double.parseDouble(qty);
				quanty = StrUtils.RoundDB(quanty, IConstants.DECIMALPTS);
				qty = String.valueOf(quanty);
				
				// ADD ALL TO PODET

				Hashtable htTodet = new Hashtable();
				htTodet.put(IDBConstants.PLANT, plant);
				htTodet.put(IDBConstants.TODET_TONUM, tono);
				htTodet.put(IDBConstants.TODET_TOLNNO, tolno);
				htTodet.put(IDBConstants.TODET_ITEM, item);
				htTodet.put(IDBConstants.UNITMO, uom);
				htTodet.put("ITEMDESC",strUtils.InsertQuotes(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, item))));
		//		htTodet.put("UNITMO", strUtils.fString(new ItemMstDAO().getItemUOM(plant, item)));
				htTodet.put(IDBConstants.TODET_ITEM_DESC, strUtils.InsertQuotes(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, item))));
				htTodet.put(IDBConstants.TODET_LNSTATUS, "N");
				htTodet.put("PickStatus", "N");
				htTodet.put(IDBConstants.TODET_CUST_NAME, custName);
				htTodet.put(IDBConstants.TODET_QTYOR, qty);
				htTodet.put("QTYRC", "0");
				java.util.Date dt = new java.util.Date();
				SimpleDateFormat dfVisualDate = new SimpleDateFormat(
						"dd/MM/yyyy");
				String today = dfVisualDate.format(dt);
				htTodet.put(IDBConstants.TRAN_DATE, DateUtils
						.getDateinyyyy_mm_dd(today));
			        htTodet.put(IDBConstants.DODET_COMMENT1,strUtils.InsertQuotes(productRemarks));
			        htTodet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			        htTodet.put(IDBConstants.CREATED_BY, user_id);
				boolean flag = _TOUtil.saveToDetDetails(htTodet);

				if (flag) {
					//To check TOHDR status and update
				    Boolean hdrFlag=false;
					ToDetDAO _ToDetDAO = new ToDetDAO();
					ToHdrDAO _ToHdrDAO = new ToHdrDAO();
					String updateToHdr = "";
					Hashtable htConditoHdr = new Hashtable();
					htConditoHdr.put("PLANT", plant);
					htConditoHdr.put("tono", tono);
					hdrFlag = _ToHdrDAO.isExisit(htConditoHdr," isnull(pickstaus,'') in ('C') and isnull(STATUS,'') in ('C')");
    				if (hdrFlag){
						updateToHdr = "set  STATUS='O',PickStaus='O' ";
					   flag = _ToHdrDAO.update(updateToHdr, htConditoHdr, "");
    				}
				//To check TOHDR status and update end
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.TO_ADD_ITEM);
					htMovHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htMovHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htMovHis.put(IDBConstants.ITEM, item);
					htMovHis.put(IDBConstants.QTY, qty);
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
					htMovHis.put(IDBConstants.CREATED_BY, user_id);
					htMovHis.put("MOVTID", "");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.TRAN_DATE, dateUtils
							.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htMovHis.put(IDBConstants.CREATED_AT, dateUtils
							.getDateTime());
					flag = movHisDao.insertIntoMovHis(htMovHis);

				}

				if (flag)
					result = "<font color=\"green\">Product ID Added Successfully</font><br><br><center>"
							+ "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='/track/TransferOrderServlet?TONO="
							+ tono + "&Submit=View'\">";
				else
					result = "<font color=\"red\"> Error in adding Product ID   <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

				result = "Consignment Order<br><h3>" + result;

				response.sendRedirect("jsp/createTODET.jsp?Submit=add&TONO="
						+ tono + "&CUST_CODE=" + custCode + "&RFLAG=" + RFLAG
						+ "&CUST_NAME="
						+ StrUtils.replaceCharacters2Send(custName));

			} else if (action.equalsIgnoreCase("UPDATE")) {

				String result = updateToHdr(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			} else if (action.equalsIgnoreCase("Pick/Issue")) {
				xmlStr = "";
				xmlStr = TransferPickingDataByWMS(request, response,"PICKING");
			}
		       else if (action.equalsIgnoreCase("SingleStepPick/Issue")) {
                               xmlStr = "";
                               xmlStr = TransferPickingDataByWMS(request, response,"SINGLE_STEP");
                      }
			else if (action.equalsIgnoreCase("MultiplePick/Issue")) {
				xmlStr = "";
				xmlStr = TransferMultiPickingDataByWMS(request, response);
			}
			else if (action.equalsIgnoreCase("Pick/Issue Confirm")) {
				xmlStr = "";
				xmlStr = process_doTranferOrderPickingByWMS(request, response);
			}else if (action.equalsIgnoreCase("MultiPick/Issue Confirm")) {
				xmlStr = "";
				
				xmlStr = process_doTranferOrderMultiPickingByWMS(request, response);
			}
                        else if (action.equalsIgnoreCase("Single Step Pick/Issue Confirm")) {
                            xmlStr = process_doTranferOrderPickingForSingleStepMultiple(request, response);
                         }
			else if (action.equalsIgnoreCase("Receiving")) {
				xmlStr = "";

				xmlStr = TransferReceivingDataByWMS(request, response);
			}
	
			else if (action.equalsIgnoreCase("Print Transfer Order")) {
//				  viewTOReport(request, response);
					viewTOReportNew(request, response);
			}
			else if (action.equalsIgnoreCase("Print Transfer Order With Price")) {
				viewToReportWithPrice(request, response);
			}
			else if (action.equalsIgnoreCase("MultiReceiving")) {
				xmlStr = "";

				xmlStr = TransferMultiReceivingDataByWMS(request,response);
			}
			else if (action.equalsIgnoreCase("TO_ReceiveConfirm")) {
				xmlStr = "";

				xmlStr = process_orderToReceivingByWMS(request, response);
			}
			else if (action.equalsIgnoreCase("MultiTO_ReceiveConfirm")) {
				xmlStr = "";

				xmlStr = process_orderToMultiReceivingByWMS(request, response);
			}
			//Start added by Bruhan for Bulk receiving on 3 Aug 2012.
			else if (action.equalsIgnoreCase("Bulk_ReceiveConfirm")) {
				xmlStr = "";

				xmlStr = process_orderToBulkReceivingByWMS(request, response);
			}
                        else if (action.equalsIgnoreCase("TOConfirmMultipleRecipt")) {
                                xmlStr = "";
                                xmlStr = this.process_TOMultipleReciptConfirm(request, response);
                        }
			//End added by Bruhan for Bulk receiving on 3 Aug 2012.
			
			//Start added by Bruhan for Bulk picking on 14 mar 2013.
            			else if (action.equalsIgnoreCase("Bulk_PickConfirm")) {
            				xmlStr = "";

            				xmlStr = process_doTranferOrderBulkPickingByWMS(request, response);
            			}
			//End added by Bruhan for Bulk picking on 14 mar 2013.
			
			//Start added by Bruhan for TOReversal on 21 Aug 2013.
            			else if (action.equalsIgnoreCase("TOReverseConfirm")) {
            				xmlStr = "";

            				xmlStr = process_TranferOrderReversal(request, response);
            			}
			//End added by Bruhan for TOReversal on 21 Aug 2013.
		
            			else if (action.equalsIgnoreCase("Bulk_PickReceiveConfirm")) {
            				xmlStr = "";

            				xmlStr = process_TranferOrderBulkPickReceive(request, response);
            			}
            			else if (action.equalsIgnoreCase("BulkPickReceivebyProd")) {
            				xmlStr = "";

            				xmlStr = process_TransferOrderPickReceivebyProd(request, response);
            			}
			
			
			//start code by Bruhan for export to excel transfer order  details on 16 sep 2013
            else if(action.equalsIgnoreCase("Export To Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=Transferorder.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			//start code by Bruhan for export to excel transfer order  details on 16 sep 2013
            
			else {
				response
						.sendRedirect("jsp/CreateTransferOrder.jsp?action=Auto-Generate");
			}

		} catch (Exception ex) {

			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			result = result
					+ " <br><br><center> "
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
			result = "Consignment Order<br><h3>" + result;
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");

		}
	}

	

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

		private String SaveToData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		boolean flag = false,isAutoGenerate = false;
		Hashtable ht = new Hashtable();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		DateUtils dateUtils = new DateUtils();
		String result = "", tono = "",jobNum="", custName = "", custCode = "", personIncharge = "", user_id = "", contactNum = "", address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "", remark2 = "";
		String fromWareHouse = "", toWareHouse = "",shipCustname="",shipCname="",shipaddr1="",shipaddr2="",shipCity="",shipCountry="",shipZip="",shiptelno="",processno="";
		ToHdrDAO tohdrDao = new ToHdrDAO();
		HttpSession session = request.getSession();
		String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();

		try {
			tono = StrUtils.fString(request.getParameter("TONO")).trim();
			custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			user_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
			fromWareHouse = StrUtils.fString(request.getParameter("FROM_WAREHOUSE")).trim();
			toWareHouse = StrUtils.fString(request.getParameter("TO_WAREHOUSE")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			address = StrUtils.fString(request.getParameter("ADD1")).trim();
			address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
			collectionDate = StrUtils.fString(request.getParameter("DELDATE")).trim();
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			//Added for Shipping address details
			shipCustname = StrUtils.fString(request.getParameter("SCUST_NAME")).trim();
			shipCname = StrUtils.fString(request.getParameter("SCONTACT_NAME")).trim();
			shipaddr1 = StrUtils.fString(request.getParameter("SADDR1")).trim();
			shipaddr2 = StrUtils.fString(request.getParameter("SADDR2")).trim();
			shipCity = StrUtils.fString(request.getParameter("SCITY")).trim();
			shipCountry = StrUtils.fString(request.getParameter("SCOUNTRY")).trim();
			shipZip = StrUtils.fString(request.getParameter("SZIP")).trim();
			shiptelno = StrUtils.fString(request.getParameter("STELNO")).trim();
			 if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(plant, user_id, fromWareHouse);
			isAutoGenerate = Boolean.valueOf(StrUtils.fString(request.getParameter("ISAUTOGENERATE")).trim()); 
			if (isvalidlocforUser) {

				LocUtil locUtil = new LocUtil();
				locUtil.setmLogger(mLogger);

				isvalidlocforUser = locUtil.isValidLocInLocmst(plant,
						toWareHouse);
			}
			if (isvalidlocforUser) {

				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				boolean isvalidToAssingee = customerBeanDAO.isExistsToAssigneeName(custName, plant);
				if (isvalidToAssingee) {

					ht.put("PLANT", plant);
					ht.put("TONO", tono);
					boolean isOrderPresent = tohdrDao.isExisit(ht);
					ht.put(IDBConstants.TOHDR_CUST_CODE, custCode);
					ht.put(IDBConstants.TOHDR_CUST_NAME, custName);
					ht.put(IDBConstants.TOHDR_FROM_WAREHOUSE, fromWareHouse);
					ht.put(IDBConstants.TOHDR_TO_WAREHOUSE, toWareHouse);
					ht.put(IDBConstants.TOHDR_PERSON_INCHARGE, personIncharge);
					ht.put(IDBConstants.TOHDR_CONTACT_NUM, contactNum);
					ht.put(IDBConstants.TOHDR_ADDRESS, address);
					ht.put(IDBConstants.TOHDR_ADDRESS2, address2);
					ht.put(IDBConstants.TOHDR_ADDRESS3, address3);
					ht.put(IDBConstants.TOHDR_COL_DATE, collectionDate);
					ht.put(IDBConstants.TOHDR_COL_TIME, collectionTime);
					ht.put(IDBConstants.TOHDR_REMARK1, remark1);
					ht.put(IDBConstants.TOHDR_REMARK2, remark2);
					ht.put(IDBConstants.STATUS, "N");
					ht.put(IDBConstants.TOHDR_JOB_NUM, jobNum);
                    //Added for Shiiping address
                    
                    ht.put(IDBConstants.TOHDR_SCUST_NAME, shipCustname);
                    ht.put(IDBConstants.TOHDR_SCONTACT_NAME, shipCname);
                    ht.put(IDBConstants.TOHDR_SADDR1, strUtils.InsertQuotes(shipaddr1));
                    ht.put(IDBConstants.TOHDR_SADDR2, strUtils.InsertQuotes(shipaddr2));
                    ht.put(IDBConstants.TOHDR_SCITY, shipCity);
                    ht.put(IDBConstants.TOHDR_SCOUNTRY, shipCountry);
                    ht.put(IDBConstants.TOHDR_SZIP, shipZip);
                    ht.put(IDBConstants.TOHDR_STELNO, shiptelno);
                    ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
                    ht.put(IDBConstants.CREATED_BY, user_id);

					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, plant);

					if (!isOrderPresent) {
						flag = _TOUtil.saveWMSToHdrDetails(ht);
						//Start added by Bruhan for Email Notification on 11 July 2012.
						if(flag){
							String isAutoEmail = emailDao.getIsAutoEmailDetails(plant, IConstants.CONSIGNMENT_ORDER);
							if(isAutoEmail.equalsIgnoreCase("Y"))
							mailUtil.sendEmail(plant,tono,IConstants.CONSIGNMENT_ORDER);
						}
						//End added by Bruhan for Email Notification on 11 July 2012.

						htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_TO);
					//}
					if (flag) {
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
						htRecvHis.put(IDBConstants.CREATED_BY, user_id);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, custName + ","
									+ remark1);
						} else {
							htRecvHis.put(IDBConstants.REMARKS, custName);
						}
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
								.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils
								.getDateTime());
						flag = movHisDao.insertIntoMovHis(htRecvHis);

					}
					
						if (flag) {
							result = "<font color=\"green\"> Consignment Order Created Successfully.  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/TransferOrderServlet?TONO="
									+ tono + "&Submit=View'\"> ";
							result = "<br><h3>" + result + "</h3>";
							
							//UPDATE TBLCTRL
							//if(isAutoGenerate){
                                                       
                                                        new TblControlUtil().updateTblControlSeqNo(plant,IConstants.TRANSFER,"T",tono);
						            
							//}

						} else {
							result = "<font color=\"red\"> Error in Creating Consignment Order  - Please Check the Data  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
							result = "<br><h3>" + result + "</h3>";
						}
						
					
						/*flag = _TOUtil.updateToHdr(ht);
						htRecvHis
								.put("DIRTYPE", TransactionConstants.UPDATE_TO);*/
						
					//}
					
					
				} else {
					/*result = "<font color=\"red\"> Transfer Order Number already in use.Please generate new one!  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/TransferOrderServlet?TONO="
							+ tono + "&Submit=View'\"> ";
					result = "Transfer Order<br><h3>" + result + "</h3>";*/
					throw new Exception("Consignment order created already");
				}
				}else {
					result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result + "</h3>";
				}
			} else {
				result = "<font color=\"red\"> Enter Valid Location <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "<br><h3>" + result + "</h3>";
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			if(e.getMessage().equalsIgnoreCase("Consignment order created already")) 
			{
				processno=tono;
				tono = _TblControlDAO.getNextOrder(plant,user_id,IConstants.TRANSFER);
				ht.clear();
				ht.put("PLANT", plant);
				ht.put("TONO", tono);
				boolean isOrderPresent = tohdrDao.isExisit(ht);
				ht.put(IDBConstants.TOHDR_CUST_CODE, custCode);
				ht.put(IDBConstants.TOHDR_CUST_NAME, custName);
				ht.put(IDBConstants.TOHDR_FROM_WAREHOUSE, fromWareHouse);
				ht.put(IDBConstants.TOHDR_TO_WAREHOUSE, toWareHouse);
				ht.put(IDBConstants.TOHDR_PERSON_INCHARGE, personIncharge);
				ht.put(IDBConstants.TOHDR_CONTACT_NUM, contactNum);
				ht.put(IDBConstants.TOHDR_ADDRESS, address);
				ht.put(IDBConstants.TOHDR_ADDRESS2, address2);
				ht.put(IDBConstants.TOHDR_ADDRESS3, address3);
				ht.put(IDBConstants.TOHDR_COL_DATE, collectionDate);
				ht.put(IDBConstants.TOHDR_COL_TIME, collectionTime);
				ht.put(IDBConstants.TOHDR_REMARK1, remark1);
				ht.put(IDBConstants.TOHDR_REMARK2, remark2);
				ht.put(IDBConstants.STATUS, "N");
				ht.put(IDBConstants.TOHDR_JOB_NUM, jobNum);
                //Added for Shiiping address
                
                ht.put(IDBConstants.TOHDR_SCUST_NAME, shipCustname);
                ht.put(IDBConstants.TOHDR_SCONTACT_NAME, shipCname);
                ht.put(IDBConstants.TOHDR_SADDR1, strUtils.InsertQuotes(shipaddr1));
                ht.put(IDBConstants.TOHDR_SADDR2, strUtils.InsertQuotes(shipaddr2));
                ht.put(IDBConstants.TOHDR_SCITY, shipCity);
                ht.put(IDBConstants.TOHDR_SCOUNTRY, shipCountry);
                ht.put(IDBConstants.TOHDR_SZIP, shipZip);
                ht.put(IDBConstants.TOHDR_STELNO, shiptelno);

				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, plant);

				if (!isOrderPresent) {
					flag = _TOUtil.saveWMSToHdrDetails(ht);
					//Start added by Bruhan for Email Notification on 11 July 2012.
					if(flag){
						String isAutoEmail = emailDao.getIsAutoEmailDetails(plant, IConstants.CONSIGNMENT_ORDER);
						if(isAutoEmail.equalsIgnoreCase("Y"))
						mailUtil.sendEmail(plant,tono,IConstants.CONSIGNMENT_ORDER);
					}
					//End added by Bruhan for Email Notification on 11 July 2012.

					htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_TO);
				//}
				if (flag) {
					htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
					htRecvHis.put(IDBConstants.CREATED_BY, user_id);
					htRecvHis.put("MOVTID", "");
					htRecvHis.put("RECID", "");
					if (!remark1.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS, custName + ","
								+ remark1);
					} else {
						htRecvHis.put(IDBConstants.REMARKS, custName);
					}
					htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
							.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils
							.getDateTime());
					flag = movHisDao.insertIntoMovHis(htRecvHis);
					  new TblControlUtil().updateTblControlSeqNo(plant,IConstants.TRANSFER,"T",tono);

				 }
				}
				result = "<font color=\"green\"> Consignment order " + processno + " has already been used. System has auto created a new Consignment order " + tono + " for you.<br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/TransferOrderServlet?TONO="
						+ tono + "&Submit=View'\"> ";
				result = "<br><h3>" + result + "</h3>";
				processno="";
			}
			else
			{
				throw e;
			}
			
		}
			return result;
	}
		
//		resvi starts
		 
		  private JSONObject getConsignmentDetailsToPrint(HttpServletRequest request) {
		        JSONObject resultJson = new JSONObject();
		        JSONArray jsonArray = new JSONArray();
		        JSONArray jsonArrayErr = new JSONArray();
		        HTReportUtil movHisUtil       = new HTReportUtil();
		        DateUtils _dateUtils = new DateUtils();
		        ArrayList movQryList  = new ArrayList();
		        TOUtil toUtil = new TOUtil();
		        DecimalFormat decformat = new DecimalFormat("#,##0.00");
		       
		        StrUtils strUtils = new StrUtils();
		        String fdate="",tdate="";
		    
		        try {
		        
		        	 String PLANT= strUtils.fString(request.getParameter("PLANT"));
			           String  FROM_DATE   = strUtils.fString(request.getParameter("FDATE"));
			           String  TO_DATE = strUtils.fString(request.getParameter("TDATE"));
			           String   DIRTYPE       = StrUtils.fString(request.getParameter("DTYPE"));
			           String   JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
//			           String   CUSTOMER          = StrUtils.fString(request.getParameter("USER"));
			           String   TONO       = StrUtils.fString(request.getParameter("ORDERNO"));
			           String   USER      = StrUtils.fString(request.getParameter("CUSTOMER"));
			           String   CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
			           String   PICKSTATUS    = StrUtils.fString(request.getParameter("PICKSTATUS"));
			           
			           String issuests = PICKSTATUS; 
			           String ISSUESTATUS  = StrUtils.fString(request.getParameter("ISSUESTATUS"));
			           String ORDERTYPE  = StrUtils.fString(request.getParameter("ORDERTYPE"));
			           String  EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
			           String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
		           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
		           String curDate =_dateUtils.getDate();
		           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

		           if (FROM_DATE.length()>5)

		            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



		           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
		           if (TO_DATE.length()>5)
		           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

		              
		   
		           Hashtable ht = new Hashtable();
		            if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
		            if(strUtils.fString(TONO).length() > 0)      ht.put("B.PONO",TONO);
		            if(strUtils.fString(ORDERTYPE).length() > 0)    ht.put("A.ORDERTYPE",ORDERTYPE);
		            if(strUtils.fString(PICKSTATUS).length() > 0)   ht.put("A.STATUS",PICKSTATUS);
		            if(strUtils.fString(EMPNO).length() > 0) ht.put("A.EMPNO",EMPNO);
		            
		            movQryList =  toUtil.listTODetilstoPrintTo(ht,fdate,tdate,DIRTYPE,PLANT, USER,CUSTOMERTYPE);
		            if (movQryList.size() > 0) {
		            int iIndex = 0,Index = 0;
		             int irow = 0;
		             
		            double sumprdQty = 0;String lastProduct="";
		            
		            double totalOrdPrice=0,totaltax=0,totOrdPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
		            ;
		                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
		                      
		                        String result="";
		                        Map lineArr = (Map) movQryList.get(iCnt);
		                        String custcode =(String)lineArr.get("custcode");
		                        String dono = (String)lineArr.get("dono");
		                         
		                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
		                        JSONObject resultJsonInt = new JSONObject();
		                       
		                        String qtyOrValue =(String)lineArr.get("qtyor");
		               			String qtyPickValue =(String)lineArr.get("qtypick");
		               			String qtyValue =(String)lineArr.get("qty");
		               			
		                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
		                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
		                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
		                        
		                        if(qtyOrVal==0f){
		                        	qtyOrValue="0.000";
		                        }else{
		                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                        }if(qtyPickVal==0f){
		                        	qtyPickValue="0.000";
		                        }else{
		                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                        }if(qtyVal==0f){
		                        	qtyValue="0.000";
		                        }else{
		                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                        }
		                        
		                        qtyOrValue = StrUtils.addZeroes(qtyOrVal, "3");
		                        qtyPickValue = StrUtils.addZeroes(qtyPickVal, "3");
		                        qtyValue = StrUtils.addZeroes(qtyVal, "3");
		                        
		                        	Index = Index + 1;
		                        	//resultJsonInt.put("Index", Index);
		                       	resultJsonInt.put("tono", dono);
		                   		resultJsonInt.put("ordertype", strUtils.fString((String)lineArr.get("ordertype")));
		                      	resultJsonInt.put("jobnum", strUtils.fString((String)lineArr.get("jobnum")));
		                        resultJsonInt.put("custname", strUtils.fString((String)lineArr.get("custname")));
		                        resultJsonInt.put("qtyor", qtyOrValue);
		                      	resultJsonInt.put("qtypick",qtyPickValue );
		                      	resultJsonInt.put("empname", strUtils.fString((String)lineArr.get("empname")));
		                      	resultJsonInt.put("status", strUtils.fString((String)lineArr.get("status")));
//		                      	resultJsonInt.put("status_id", strUtils.fString((String)lineArr.get("status_id")));
//		                      	resultJsonInt.put("UOM", strUtils.fString((String)lineArr.get("UOM")));
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
	    
//	    resvi ends
	    
	    
//		resvi starts
		 
	    private JSONObject getConsignmentDetailsToPrintWithPrice(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        HTReportUtil movHisUtil       = new HTReportUtil();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList movQryList  = new ArrayList();
	        TOUtil toUtil = new TOUtil();
	        DecimalFormat decformat = new DecimalFormat("#,##0.00");
	       
	        StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="";
	    
	        try {
	        
	        	 String PLANT= strUtils.fString(request.getParameter("PLANT"));
		           String  FROM_DATE   = strUtils.fString(request.getParameter("FDATE"));
		           String  TO_DATE = strUtils.fString(request.getParameter("TDATE"));
		           String   DIRTYPE       = StrUtils.fString(request.getParameter("DTYPE"));
		           String   JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
//		           String   CUSTOMER          = StrUtils.fString(request.getParameter("USER"));
		           String   TONO       = StrUtils.fString(request.getParameter("ORDERNO"));
		           String   USER      = StrUtils.fString(request.getParameter("CUSTOMER"));
		           String   CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
		           String   PICKSTATUS    = StrUtils.fString(request.getParameter("PICKSTATUS"));
		           
		           String issuests = PICKSTATUS; 
		           String ISSUESTATUS  = StrUtils.fString(request.getParameter("ISSUESTATUS"));
		           String ORDERTYPE  = StrUtils.fString(request.getParameter("ORDERTYPE"));
		           String  EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
		           String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =_dateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)

	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	              
	   
	           Hashtable ht = new Hashtable();
	            if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
	            if(strUtils.fString(TONO).length() > 0)      ht.put("B.PONO",TONO);
	            if(strUtils.fString(ORDERTYPE).length() > 0)    ht.put("A.ORDERTYPE",ORDERTYPE);
	            if(strUtils.fString(PICKSTATUS).length() > 0)   ht.put("A.STATUS",PICKSTATUS);
	            if(strUtils.fString(EMPNO).length() > 0) ht.put("A.EMPNO",EMPNO);
	            
	            movQryList=  toUtil.listTODetilstoPrintTo(ht,fdate,tdate,DIRTYPE,PLANT, USER,CUSTOMERTYPE);
	            if (movQryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	             
	            double sumprdQty = 0;String lastProduct="";
	            
	            double totalOrdPrice=0,totaltax=0,totOrdPriceWTax=0,priceGrandTot=0,taxGrandTot=0,priceWTaxGrandTot=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
	            ;
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                      
	                        String result="";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        String custcode =(String)lineArr.get("custcode");
	                        String dono = (String)lineArr.get("dono");
	                         
	                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        JSONObject resultJsonInt = new JSONObject();
	                       
	                        String qtyOrValue =(String)lineArr.get("qtyor");
	               			String qtyPickValue =(String)lineArr.get("qtypick");
	               			String qtyValue =(String)lineArr.get("qty");
	               			
	                        float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
	                        float qtyPickVal ="".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
	                        float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
	                        
	                        if(qtyOrVal==0f){
	                        	qtyOrValue="0.000";
	                        }else{
	                        	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(qtyPickVal==0f){
	                        	qtyPickValue="0.000";
	                        }else{
	                        	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }if(qtyVal==0f){
	                        	qtyValue="0.000";
	                        }else{
	                        	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                        }
	                        
	                        qtyOrValue = StrUtils.addZeroes(qtyOrVal, "3");
	                        qtyPickValue = StrUtils.addZeroes(qtyPickVal, "3");
	                        qtyValue = StrUtils.addZeroes(qtyVal, "3");
	                        
	                        	Index = Index + 1;
	                        	//resultJsonInt.put("Index", Index);
	                       	resultJsonInt.put("tono", dono);
	                   		resultJsonInt.put("ordertype", strUtils.fString((String)lineArr.get("ordertype")));
	                      	resultJsonInt.put("jobnum", strUtils.fString((String)lineArr.get("jobnum")));
	                        resultJsonInt.put("custname", strUtils.fString((String)lineArr.get("custname")));
	                        resultJsonInt.put("qtyor", qtyOrValue);
	                      	resultJsonInt.put("qtypick",qtyPickValue );
	                      	resultJsonInt.put("empname", strUtils.fString((String)lineArr.get("empname")));
	                      	resultJsonInt.put("status", strUtils.fString((String)lineArr.get("status")));
//	                      	resultJsonInt.put("status_id", strUtils.fString((String)lineArr.get("status_id")));
//	                      	resultJsonInt.put("UOM", strUtils.fString((String)lineArr.get("UOM")));
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
	    
//	    resvi ends
		
		//imti from consignmentsummary.js for orderno start
		private JSONObject getOrderNumForAutoSuggestion(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        
	        TOUtil itemUtil = new TOUtil();
	        StrUtils strUtils = new StrUtils();
	        itemUtil.setmLogger(mLogger);
	        
	        try {
	        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			    String tono = StrUtils.fString(request.getParameter("TONO")).trim();
			    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
			    
			    Hashtable ht=new Hashtable();
			     String extCond="";
			     ht.put("PLANT",plant);
			     if(tono.length()>0) extCond=" AND plant='"+plant+"' and tono like '"+tono+"%' ";
			     if(cname.length()>0) extCond=" AND CustName = '"+cname+"' ";
			     extCond=extCond+" and TONO like 'C%'";
			     extCond=extCond+" ORDER BY CONVERT(date, CollectionDate, 103) desc";
			     ArrayList listQry = itemUtil.getToHdrDetails("tono,CustName,CustCode,jobNum,status,collectiondate",ht,extCond);
			     if (listQry.size() > 0) {
			    	 for(int i =0; i<listQry.size(); i++) {		   
					  Map m=(Map)listQry.get(i);
					  tono = (String)m.get("tono");
					  String custName = strUtils.replaceCharacters2Send((String)m.get("custName"));
					  String custcode = (String)m.get("custcode");
					  String orderdate = (String)m.get("collectiondate");
					  String jobNum = (String)m.get("jobNum");
					  String status = (String)m.get("status");
					  JSONObject resultJsonInt = new JSONObject();
					  resultJsonInt.put("TONO", tono);
					  resultJsonInt.put("CUSTNAME", custName);
					  resultJsonInt.put("CUSTCODE", custcode);
					  resultJsonInt.put("ORDERDATE", orderdate);
					  resultJsonInt.put("JOBNUM", jobNum);
					  resultJsonInt.put("STATUS", status);				  
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
	                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
	                 resultJsonInt.put("ERROR_CODE", "99");
	                 jsonArrayErr.add(resultJsonInt);
	                 jsonArray.add("");
	                 resultJson.put("items", jsonArray);
	                 resultJson.put("errors", jsonArrayErr);
			     }
	        }catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		//imti call frm consignmentsummary.js end
		
		
//		RESVI STARTS CONSIGNMENT CLOSE ORDER
		private JSONObject getOrderNumForForceClose(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        
	        TOUtil itemUtil = new TOUtil();
	        StrUtils strUtils = new StrUtils();
	        itemUtil.setmLogger(mLogger);
	        OrderTypeUtil orderUtil = new OrderTypeUtil();
	        orderUtil.setmLogger(mLogger);
	        try {
	        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			    String tono = StrUtils.fString(request.getParameter("TONO")).trim();
			    String status = StrUtils.fString(request.getParameter("STATUS")).trim();
			    String orderNo = StrUtils.fString(request.getParameter("orderNo")).trim();
			    
			    
			   
			    ArrayList listQry = orderUtil.getOrderHdrDetails(plant,"TRANSFER",tono,status);
			     if (listQry.size() > 0) {
			    	 for(int i =0; i<listQry.size(); i++) {		   
			    		  Map m=(Map)listQry.get(i);
			    	      orderNo     = (String)m.get("orderNo");
			    	      String custName    = (String)m.get("CustName");
			    	      String status1      =  (String)m.get("status");
					  JSONObject resultJsonInt = new JSONObject();
					  resultJsonInt.put("TONO", orderNo);
					  resultJsonInt.put("CUSTNAME", custName);
					  resultJsonInt.put("STATUS", status1);					  
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
	                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
	                 resultJsonInt.put("ERROR_CODE", "99");
	                 jsonArrayErr.add(resultJsonInt);
	                 jsonArray.add("");
	                 resultJson.put("items", jsonArray);
	                 resultJson.put("errors", jsonArrayErr);
			     }
	        }catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		//imti mar4 GINO
		private JSONObject getgiForAutoSuggestion(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        ToDetDAO itemUtil = new ToDetDAO();	        
	        ArrayList movQryList  = new ArrayList();	        
	        try {
	        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        	String tono = StrUtils.fString(request.getParameter("TONO"));

	        	int index=0;
	        	Hashtable ht = new Hashtable();
	        	
	        	 String extCond="";
			     ht.put("PLANT",plant);
			     if(tono.length()>0) extCond=" AND plant='"+plant+"' and tono='"+tono+"' ";
			     //extCond=extCond+" and STATUS <>'C'";
			     extCond=extCond+"  and GINO is not null";
			     ArrayList listQry = itemUtil.selectginotoinvoice("A.GINO",ht,extCond);
	        	
	        	for (int iCnt =0; iCnt<listQry.size(); iCnt++){
	           		Map lineArr = (Map) listQry.get(iCnt);
	           		JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("GINO",(String)lineArr.get("GINO"));
					
	                jsonArray.add(resultJsonInt);
	            }
	        	resultJson.put("gino", jsonArray);
	        }catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}

	private String updateToHdr(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		boolean flag = false;
		Hashtable ht = new Hashtable();
		Hashtable htRecvHis = new Hashtable();
		DateUtils dateUtils = new DateUtils();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		String result = "", tono = "",jobNum="",custCode = "", custName = "", personIncharge = "", user = "", contactNum = "", address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "", remark2 = "", fromwarehouse = "", towarehouse = "";
		try {
			tono = StrUtils.fString(request.getParameter("TONO")).trim();
			custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			fromwarehouse = StrUtils.fString(request.getParameter("FROM_WAREHOUSE")).trim();
			user = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			towarehouse = StrUtils.fString(request.getParameter("TO_WAREHOUSE")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			address = StrUtils.fString(request.getParameter("ADD1")).trim();
			address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
			collectionDate = StrUtils.fString(request.getParameter("DELDATE")).trim();
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
                        
		    //Added for Shipping address details
		        String shipCustname = StrUtils.fString(request.getParameter("SCUST_NAME")).trim();
		        String shipCname = StrUtils.fString(request.getParameter("SCONTACT_NAME")).trim();
		        String shipaddr1 = StrUtils.fString(request.getParameter("SADDR1")).trim();
		        String shipaddr2 = StrUtils.fString(request.getParameter("SADDR2")).trim();
		        String shipCity = StrUtils.fString(request.getParameter("SCITY")).trim();
		        String shipCountry = StrUtils.fString(request.getParameter("SCOUNTRY")).trim();
		        String shipZip = StrUtils.fString(request.getParameter("SZIP")).trim();
		        String shiptelno = StrUtils.fString(request.getParameter("STELNO")).trim();
		        if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}

			HttpSession session = request.getSession();
			String plant = StrUtils.fString(
					(String) session.getAttribute("PLANT")).trim();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TOHDR_TONUM, tono);
			ht.put(IDBConstants.TOHDR_CUST_NAME, custName);
			ht.put(IDBConstants.TOHDR_CUST_CODE, custCode);
			ht.put(IDBConstants.TOHDR_FROM_WAREHOUSE, fromwarehouse);
			ht.put(IDBConstants.TOHDR_TO_WAREHOUSE, towarehouse);
			ht.put(IDBConstants.TOHDR_PERSON_INCHARGE, personIncharge);
			ht.put(IDBConstants.TOHDR_CONTACT_NUM, contactNum);
			ht.put(IDBConstants.TOHDR_ADDRESS, address);
			ht.put(IDBConstants.TOHDR_ADDRESS2, address2);
			ht.put(IDBConstants.TOHDR_ADDRESS3, address3);
			ht.put(IDBConstants.TOHDR_COL_DATE, collectionDate);
			ht.put(IDBConstants.TOHDR_COL_TIME, collectionTime);
			ht.put(IDBConstants.TOHDR_REMARK1, remark1);
			ht.put(IDBConstants.TOHDR_REMARK2, remark2);
			ht.put(IDBConstants.STATUS, "N");
			ht.put(IDBConstants.TOHDR_JOB_NUM, jobNum);            
            //Added for Shiiping address
            
             ht.put(IDBConstants.TOHDR_SCUST_NAME, shipCustname);
             ht.put(IDBConstants.TOHDR_SCONTACT_NAME, shipCname);
             ht.put(IDBConstants.TOHDR_SADDR1, strUtils.InsertQuotes(shipaddr1));
             ht.put(IDBConstants.TOHDR_SADDR2, strUtils.InsertQuotes(shipaddr2));
             ht.put(IDBConstants.TOHDR_SCITY, shipCity);
             ht.put(IDBConstants.TOHDR_SCOUNTRY, shipCountry);
             ht.put(IDBConstants.TOHDR_SZIP, shipZip);
             ht.put(IDBConstants.TOHDR_STELNO, shiptelno);

			LocUtil locUtil = new LocUtil();
			locUtil.setmLogger(mLogger);
			boolean isValidloc = locUtil.isValidLocInLocmst(plant,
					fromwarehouse);
			if (isValidloc) {
				isValidloc = locUtil.isValidLocInLocmst(plant, towarehouse);
			}
			if (isValidloc) {
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				boolean isvalidToAssingee = customerBeanDAO
						.isExistsToAssigneeName(custName, plant);
				if (isvalidToAssingee) {

					flag = _TOUtil.updateToHdr(ht);
					if (flag) {
						htRecvHis
								.put("DIRTYPE", TransactionConstants.UPDATE_TO);
						htRecvHis.put("PLANT", plant);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, request
								.getParameter("CUST_CODE1"));
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
						htRecvHis.put(IDBConstants.CREATED_BY, request
								.getParameter("LOGIN_USER"));
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, custName + ","
									+ remark1);
						} else {
							htRecvHis.put(IDBConstants.REMARKS, custName);
						}
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
								.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils
								.getDateTime());
						flag = movHisDao.insertIntoMovHis(htRecvHis);

					}
					if (flag) {
						result = "<font color=\"green\"> Consignment Order Updated Successfully.  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "<br>" + result;

					} else {
						result = "<font color=\"red\"> Error in Updating Consignment Order  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "<br>" + result;

					}
				} else {
					result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br>" + result;
				}
			} else {
				result = "<font color=\"red\"> Enter Valid Location <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "<br>" + result;
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	private boolean DisplayData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String tono = StrUtils.fString(request.getParameter("TONO"));
		String fieldDesc = "<tr><td> Please Choose options from the list box shown above</td></tr>";
		ArrayList al = new ArrayList();
		Hashtable htCond = new Hashtable();
		Map m = new HashMap();
		if (tono.length() > 0) {
			HttpSession session = request.getSession();
			String plant = StrUtils.fString(
					(String) session.getAttribute("PLANT")).trim();
			String rfalg = StrUtils.fString(
					(String) session.getAttribute("RFLAG")).trim();
			htCond.put("PLANT", plant);
			htCond.put("TONO", tono);
			String query = "tono,custName,custCode,isnull(jobNum,'') as jobNum,custName,fromwarehouse,towarehouse,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip ,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as sTelno";
                        
			al = _TOUtil.getOutGoingToHdrDetails(query, htCond);
			if (al.size() > 0) {
				m = (Map) al.get(0);
				fieldDesc = _TOUtil.listTODET(plant, tono, rfalg);
			} else {

				fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";
			}

		}
		request.getSession().setAttribute("todetVal", m);
		request.getSession().setAttribute("tono", tono);
		request.getSession().setAttribute("RESULT1", fieldDesc);

		return true;
	}
	
	private boolean DisplayDataForMultipleView(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String tono = StrUtils.fString(request.getParameter("TONO"));
		String gino = StrUtils.fString(request.getParameter("gino"));
		String fieldDesc = "<tr><td> Please Choose options from the list box shown above</td></tr>";
		ArrayList al = new ArrayList();
		Hashtable htCond = new Hashtable();
		Map m = new HashMap();
//		if (tono.length() > 0) {
			HttpSession session = request.getSession();
			String plant = StrUtils.fString(
					(String) session.getAttribute("PLANT")).trim();  
			String rfalg = StrUtils.fString(
					(String) session.getAttribute("RFLAG")).trim();
			htCond.put("PLANT", plant);
			if (tono.length() > 0) {
			htCond.put("TONO", tono);
			}
			
		        String query = "tono,(SELECT top 1 ISNULL(GINO,'') FROM ["+ plant +"_TO_PICK] p WHERE a.tono=p.tono) gino, custName,custCode,jobNum,custName,fromwarehouse,towarehouse,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip ,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as sTelno";
		        
		        String extCond = "";
		        if (gino.length() > 0) {
		        extCond = "AND A.TONO IN (SELECT TONO from [" + plant +"_TO_PICK] WHERE GINO ='" + gino + "' AND TONO=A.TONO)";
		        }
			al = _TOUtil.getOutGoingToHdrDetailGino(query, htCond,extCond);
			if (al.size() > 0) {
				m = (Map) al.get(0);
				if (tono.length() > 0) {
//					tono=(String) m.get("tono");
				}
				else {
					tono=(String) m.get("tono");
				}
				/*if (gino.length() > 0) {
//					gino=(String) m.get("gino");
				}
				else {
					gino=(String) m.get("gino");
				}*/
				
				//fieldDesc = _TOUtil.listTODET(plant, tono, rfalg);
			} else {

				fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";
			}

//		}
		request.getSession().setAttribute("todetVal", m);
		request.getSession().setAttribute("tono", tono);
		request.getSession().setAttribute("gino", gino);
		//request.getSession().setAttribute("RESULT1", fieldDesc);

		return true;
	}

	private String TransferPickingDataByWMS(HttpServletRequest request,
			HttpServletResponse response,String processType) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;

		ToDetDAO _ToDetDAO = new ToDetDAO();
		_ToDetDAO.setmLogger(mLogger);

		Map receiveMaterial_HM = null;

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", TO_NUM = "", TO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "", FROM_WAREHOUSE = "", TO_WAREHOUSE = "";

		String PICKED_QTY = "", RECEIVED_QTY = "", TO_BATCH = "", LOC = "", ORDER_QTY = "";
	        String AvailQty="0";
		try {

			String sepratedtoken = "";

			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));

			StringTokenizer parser = new StringTokenizer(totalString, "=");

			while (parser.hasMoreTokens())

			{
				int count = 1;

				sepratedtoken = parser.nextToken();

				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
						",");

				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

				}

				HttpSession session = request.getSession();
				PLANT = StrUtils
						.fString((String) session.getAttribute("PLANT")).trim();
				TO_NUM = strUtils.fString((String) mp.get("data1"));
				TO_LN_NUM = strUtils.fString((String) mp.get("data2"));
				ITEM_NUM = strUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION = strUtils.fString((String) mp.get("data4"));
				ORDER_QTY = strUtils.fString((String) mp.get("data5"));
				PICKED_QTY = strUtils.fString((String) mp.get("data6"));
				RECEIVED_QTY = strUtils.fString((String) mp.get("data7"));
				LOGIN_USER = strUtils.fString((String) mp.get("data8"));
				LOC = strUtils.fString((String) mp.get("data9"));
				TO_BATCH = strUtils.fString((String) mp.get("data10"));
				CUST_NAME = strUtils.fString((String) mp.get("data11"));
				FROM_WAREHOUSE = strUtils.fString((String) mp.get("data12"));
				TO_WAREHOUSE = strUtils.fString((String) mp.get("data13"));
			    try{
			        AvailQty = new InvMstUtil().getAvailableInventoryQty(PLANT,ITEM_NUM,FROM_WAREHOUSE,"NOBATCH",LOGIN_USER);
			    }catch(Exception e){
			        this.mLogger.exception(this.printLog, "", e);
			    }
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.QTY, PICKED_QTY);
				receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
				receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil
						.getCustCode(PLANT, TO_NUM));

				receiveMaterial_HM.put(IConstants.BATCH, TO_BATCH);

				xmlStr = "";

			}
		}

		catch (Exception e) {

                    this.mLogger.exception(this.printLog, "", e);
                    request.getSession().setAttribute("CATCHERROR","Error in Picking Product!");
                                        
		    String redirectErrorPage = "TransferOderSummary.jsp";
		    if(processType.equalsIgnoreCase("PICKING"))
		     redirectErrorPage = "OutBoundOrderSummary.jsp";
		    if(processType.equalsIgnoreCase("SINGLE_STEP"))
		        redirectErrorPage="TOSummaryForSingleStepPickIssue.jsp";
		   
		    response.sendRedirect("jsp/"+redirectErrorPage+"?action=View&PLANT="
							+ PLANT + "&TONO=" + TO_NUM);
			throw e;
		}
		if (!flag) {
			List listQry = _ToDetDAO.getTransferReceivingDetailsByWMS(PLANT,TO_NUM);
			request.getSession().setAttribute("assigneelistqry1", listQry);
			request.getSession().setAttribute("RESULT","Product ID : " + ITEM_NUM + "  Picked successfully!");
                                        
                        String redirectPage = "TransferOrderPicking.jsp";
                     
                        if(processType.equalsIgnoreCase("PICKING"))
                            redirectPage = "TransferOrderPicking.jsp";
                        if(processType.equalsIgnoreCase("SINGLE_STEP"))
		           redirectPage="TOSummaryForSingleStepMultipleIssue.jsp";
                    
		      
                      response.sendRedirect("jsp/"+redirectPage+"?ORDERNO="
					+ TO_NUM + "&ORDERLNO=" + TO_LN_NUM + "&ITEMNO=" + ITEM_NUM
					+ "&ITEMDESC="
					+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
					+ "&FROMLOC=" + FROM_WAREHOUSE + "&TOLOC=" + TO_WAREHOUSE
					+ "&PICKEDQTY=" + PICKED_QTY + "&ORDERQTY=" + ORDER_QTY+"&BATCH=NOBATCH"+"&QTY="+AvailQty);
		}
		return xmlStr;
	}
	private String TransferMultiPickingDataByWMS(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;

		ToDetDAO _ToDetDAO = new ToDetDAO();
		_ToDetDAO.setmLogger(mLogger);

		Map receiveMaterial_HM = null;

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", TO_NUM = "", TO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "", FROM_WAREHOUSE = "", TO_WAREHOUSE = "";

		String PICKED_QTY = "", RECEIVED_QTY = "", TO_BATCH = "", LOC = "", ORDER_QTY = "";
	        String AvailQty="0";
		try {

			String sepratedtoken = "";

			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));

			StringTokenizer parser = new StringTokenizer(totalString, "=");

			while (parser.hasMoreTokens())

			{
				int count = 1;

				sepratedtoken = parser.nextToken();

				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
						",");

				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

				}

				HttpSession session = request.getSession();
				PLANT = StrUtils
						.fString((String) session.getAttribute("PLANT")).trim();
				TO_NUM = strUtils.fString((String) mp.get("data1"));
				TO_LN_NUM = strUtils.fString((String) mp.get("data2"));
				ITEM_NUM = strUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION = strUtils.fString((String) mp.get("data4"));
				ORDER_QTY = strUtils.fString((String) mp.get("data5"));
				PICKED_QTY = strUtils.fString((String) mp.get("data6"));
				RECEIVED_QTY = strUtils.fString((String) mp.get("data7"));
				LOGIN_USER = strUtils.fString((String) mp.get("data8"));
				LOC = strUtils.fString((String) mp.get("data9"));
				TO_BATCH = strUtils.fString((String) mp.get("data10"));
				CUST_NAME = strUtils.fString((String) mp.get("data11"));
				FROM_WAREHOUSE = strUtils.fString((String) mp.get("data12"));
				TO_WAREHOUSE = strUtils.fString((String) mp.get("data13"));
			    try{
			        AvailQty = new InvMstUtil().getAvailableInventoryQty(PLANT,ITEM_NUM,FROM_WAREHOUSE,"NOBATCH",LOGIN_USER);
			    }catch(Exception e){
			        this.mLogger.exception(this.printLog, "", e);
			    }
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.QTY, PICKED_QTY);
				receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
				receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil
						.getCustCode(PLANT, TO_NUM));

				receiveMaterial_HM.put(IConstants.BATCH, TO_BATCH);

				xmlStr = "";

			}
		}

		catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR",
					"Error in Picking Product!");
			response
					.sendRedirect("jsp/MultipleTransferOderSummary.jsp?action=MultipleView&PLANT="
							+ PLANT + "&TONO=" + TO_NUM);
			throw e;
		}
		if (!flag) {
			List listQry = _ToDetDAO.getTransferReceivingDetailsByWMS(PLANT,
					TO_NUM);
			request.getSession().setAttribute("assigneelistqry1", listQry);
			request.getSession().setAttribute("RESULT",
					"Product ID : " + ITEM_NUM + "  Picked successfully!");
			response.sendRedirect("jsp/MultiTransferOrderPicking.jsp?ORDERNO="
					+ TO_NUM + "&ORDERLNO=" + TO_LN_NUM + "&ITEMNO=" + ITEM_NUM
					+ "&ITEMDESC="
					+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
					+ "&FROMLOC=" + FROM_WAREHOUSE + "&TOLOC=" + TO_WAREHOUSE
					+ "&PICKEDQTY=" + PICKED_QTY + "&ORDERQTY=" + ORDER_QTY+"&BATCH=NOBATCH"+"&QTY="+AvailQty);
		}
		return xmlStr;
	}
	private String process_doTranferOrderPickingByWMS(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
		Map receiveMaterial_HM = null;
		String PLANT = "", TO_NUM = "", ITEM_NUM = "", TO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "";
		String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "";
		String REF = "", PICKEDQTY = "", REMARK = "";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			
			TO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));

			TO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			PICKEDQTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("PICKEDQTY")));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));

			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			ITEM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			REF = StrUtils.fString(request.getParameter("REF"));

			double orderqty = Double.parseDouble(((String) ORDER_QTY.trim()));
			double invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
					.toString()));
			pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			
			double pickedQty = Double.parseDouble(((String) PICKEDQTY.trim()
					.toString()));
			double sumqty = pickingQty + pickedQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			boolean qtyFlag = false;
			if (pickingQty > orderqty || pickingQty > invqty
					|| (sumqty) > orderqty) {

				qtyFlag = true;
				throw new Exception(
						"Error in picking item : Picking Qty Should less than Order Qty!");
			}

			PICKING_QTY = String.valueOf(pickingQty);
			
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.FROMLOC, FROM_LOC);
			receiveMaterial_HM.put(IConstants.TOLOC, TO_LOC);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_" + FROM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TO_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.REMARKS, REF);
			receiveMaterial_HM.put("INV_QTY", "1");

			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, LOGIN_USER, FROM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("To Loc :" + TO_LOC+ " is not User Assigned Location");
			}

			boolean flag = false;
			flag = _TOUtil.process_ToPickingForPC(receiveMaterial_HM);

			if (flag) {
				request.getSession().setAttribute("RESULTPICKING","Product  : " + ITEM_NUM + "  Picking successfully!");
				response.sendRedirect("jsp/TransferOrderSummary.jsp?action=View&PLANT="+ PLANT + "&TONO=" + TO_NUM);
			} else {
				request.getSession().setAttribute("RESULTERROR","Item Received Already : " + ITEM_NUM);
				response.sendRedirect("jsp/TransferOrderPicking.jsp?action=resulterror");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			ToDetDAO _ToDetDAO = new ToDetDAO();
			_ToDetDAO.setmLogger(mLogger);

			List listQry = _ToDetDAO.getTransferReceivingDetailsByWMS(PLANT,
					TO_NUM);
			request.getSession().setAttribute("assigneelistqry2", listQry);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/TransferOrderPicking.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&FROMLOC="
							+ ITEM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&ORDERNO="
							+ TO_NUM
							+ "&ORDERLNO="
							+ TO_LN_NUM

							+ "&BATCH="
							+ ITEM_BATCH
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&INVQTY="
							+ INV_QTY
							+ "&REF="
							+ REF
							+ "&PICKEDQTY="
							+ PICKEDQTY
							+ "&PICKINGQTY=" + PICKING_QTY);

		}

		return xmlStr;
	}

	/* ************Modification History*********************************
	   Oct 16 2014 Bruhan, Description: To include Transaction date
	*/
	private String process_doTranferOrderMultiPickingByWMS(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
		Map receiveMaterial_HM = null;
		String PLANT = "", TO_NUM = "", ITEM_NUM = "", TO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "";
		String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "";
		String REF = "", PICKEDQTY = "", REMARK = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			TO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
     		TO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
    		ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			PICKEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKEDQTY")));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
    		REF = StrUtils.fString(request.getParameter("REF"));
    		TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			String index = StrUtils.fString(request.getParameter("DYNAMIC_PICKING_SIZE"));
		    int count= Integer.parseInt(index);
			boolean flag = false;
			for (int i = 0; i < count; i++) {
				
				ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"+"_"+i));
				PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY"+"_"+i)));
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty =  StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
				PICKING_QTY = String.valueOf(pickingqty);
				PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
				ITEM_LOC = StrUtils.fString(request.getParameter("FROMLOC"+"_"+i));
				FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"+"_"+i));
				TO_LOC = StrUtils.fString(request.getParameter("TOLOC"+"_"+i));
			
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.FROMLOC, FROM_LOC);
			receiveMaterial_HM.put(IConstants.TOLOC, TO_LOC);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_" + FROM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TO_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.REMARKS, REF);
			receiveMaterial_HM.put("TYPE", "OLD");
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			receiveMaterial_HM.put(IConstants.ISSUEDATE, strTranDate);
			xmlStr = "";
			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, FROM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("To Loc :" + TO_LOC
						+ " is not User Assigned Location");
			}
			flag = _TOUtil.process_ToPickingForPC(receiveMaterial_HM);
			}
			if (flag) {
				request.getSession().setAttribute("RESULTPICKING",
						"Product  : " + ITEM_NUM + "  Picking successfully!");
				response
						.sendRedirect("jsp/MultipleTransferOrderSummary.jsp?action=MultipleView&PLANT="
								+ PLANT + "&TONO=" + TO_NUM);
			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Item Received Already : " + ITEM_NUM);
				response
						.sendRedirect("jsp/MultiTransferOrderPicking.jsp?action=resulterror");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			ToDetDAO _ToDetDAO = new ToDetDAO();
			_ToDetDAO.setmLogger(mLogger);

			List listQry = _ToDetDAO.getTransferReceivingDetailsByWMS(PLANT,
					TO_NUM);
			request.getSession().setAttribute("assigneelistqry2", listQry);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/MultiTransferOrderPicking.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&FROMLOC="
							+ ITEM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&ORDERNO="
							+ TO_NUM
							+ "&ORDERLNO="
							+ TO_LN_NUM

							+ "&BATCH="
							+ ITEM_BATCH
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&INVQTY="
							+ INV_QTY
							+ "&REF="
							+ REF
							+ "&PICKEDQTY="
							+ PICKEDQTY
							+ "&PICKINGQTY=" + PICKING_QTY);

		}

		return xmlStr;
	}
      
	/* Created By Bruhan on March 14 2014,Description: Bukl Tranfer Order Picking
	  *************Modification History*********************************
	   Oct 16 2014 Bruhan, Description: To include Transaction date
	*/
	private String process_doTranferOrderBulkPickingByWMS(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
		 boolean flag = false;
		Map receiveMaterial_HM = null;
		String PLANT = "", TONO = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "", TEMP_LOC = "", PICKEDQTY = "";
		String CUST_NAME = "",ITEM_LOC = "",REMARKS = "",CONTACTNUM="",TELNO="",EMAIL="";
		String COLLECTION_DATE = "",COLLECTION_TIME = "",REMARK1 = "",REMARK2 = "",PERSON_INCHARGE = "",pickingqty="";
		String orderLNo = "",QTYOR="",QTYPICK= "",item = "",value = null,batchNo = "NOBATCH";
		String ADD1="",ADD2="",ADD3="",ADD4="",COUNTRY="",ZIP="",ITEM_QTY="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		Map checkedTOS = new HashMap();
		//UserTransaction ut = null;
		Boolean allChecked = false,fullIssue = false;
		try {

			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			TONO = StrUtils.fString(request.getParameter("TONO"));			
			String[] chkdToNos  = request.getParameterValues("chkdToNo");
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));	
			PERSON_INCHARGE = StrUtils.fString(request.getParameter("PERSON_INCHARGE"));	
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC_0"));
			TO_LOC =   StrUtils.fString(request.getParameter("TO_WAREHOUSE"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_WAREHOUSE"));
			COLLECTION_DATE = StrUtils.fString(request.getParameter("COLLECTION_DATE"));
			COLLECTION_TIME = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
			REMARK1 = StrUtils.fString(request.getParameter("REMARK1"));
			REMARKS =  StrUtils.fString(request.getParameter("REF"));
			REMARK2=StrUtils.fString(request.getParameter("REMARK2"));
			CONTACTNUM=StrUtils.fString(request.getParameter("CONTACTNUM"));
			TELNO=StrUtils.fString(request.getParameter("TELNO"));
			EMAIL=StrUtils.fString(request.getParameter("EMAIL"));
			ADD1=StrUtils.fString(request.getParameter("ADD1"));
			ADD2=StrUtils.fString(request.getParameter("ADD2"));
			ADD3=StrUtils.fString(request.getParameter("ADD3"));
			ADD4=StrUtils.fString(request.getParameter("ADD4"));
			COUNTRY=StrUtils.fString(request.getParameter("COUNTRY")); 
			ZIP=StrUtils.fString(request.getParameter("ZIP"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
				strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, ITEM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("To Loc :" + ITEM_LOC
						+ " is not a User Assigned Location/Valid Location");
			}
			if (chkdToNos != null)    {     
				for (int i = 0; i < chkdToNos.length; i++)       { 
					orderLNo = chkdToNos[i];
					pickingqty = StrUtils.fString(request.getParameter("pickingQty_"+orderLNo));
					batchNo = StrUtils.fString(request.getParameter("batch_"+orderLNo));
					if (batchNo.length() == 0) {
						batchNo = "NOBATCH";
					}
					checkedTOS.put(orderLNo,pickingqty+":"+batchNo);
				}
				session.setAttribute("checkedTOS", checkedTOS);
            }
									
			Boolean transactionHandler = true;
			//ut = DbBean.getUserTranaction();
			//ut.begin();

			ArrayList DODetails = null;
			Hashtable htDoDet = new Hashtable();
			String queryDoDet = "item,isnull(qtyor,0) as qtyor,isnull(qtypick,0) as qtypick,ItemDesc";
					
			process: 	
				if (chkdToNos != null)    {     
					for (int i = 0; i < chkdToNos.length; i++)       { 
						orderLNo = chkdToNos[i];
				         /* if(value!=null)
				          {
				        	  orderLNo  = value.split(":")[0];
				        	  batchNo  = value.split(":")[1];
				        	 
				          }*/
				       
				        
				        pickingqty = StrUtils.fString(request.getParameter("pickingQty_"+orderLNo));
						
				        double pckingqty = Double.parseDouble(pickingqty); 
						pckingqty = StrUtils.RoundDB(pckingqty, IConstants.DECIMALPTS);
						pickingqty = String.valueOf(pckingqty);
						pickingqty = StrUtils.formatThreeDecimal(pickingqty);
						batchNo = StrUtils.fString(request.getParameter("batch_"+orderLNo));
						if (batchNo.length() == 0) {
							batchNo = "NOBATCH";
						}
						//String pickedQty = StrUtils.fString(request.getParameter("Qtypicked_"+orderLNo+"_"+batchNo));
						//String recvdQty = StrUtils.fString(request.getParameter("QtyReceived_"+orderLNo+"_"+batchNo));
						htDoDet.put(IConstants.TR_TONO, TONO);
			    		htDoDet.put(IConstants.PLANT, PLANT);
			    		htDoDet.put(IConstants.TR_TOLNNO, orderLNo);
						DODetails = _TOUtil.getToDetDetails(queryDoDet, htDoDet);
						if (DODetails.size() > 0) {	

							Map map1 = (Map) DODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("ItemDesc");
							QTYOR = (String) map1.get("qtyor");
							QTYPICK = (String) map1.get("qtypick");
						}
						
						// 1. INV against
						
						
						List listQry = _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,item, FROM_LOC, batchNo);
						double invqty = 0;
						if (listQry.size() > 0) {
							for (int j = 0; j < listQry.size(); j++) {
								Map m = (Map) listQry.get(j);
								ITEM_QTY = (String) m.get("qty");
								invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
								if(invqty < pckingqty){
									throw new Exception(
											"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +batchNo+ "  scanned at the location  "+FROM_LOC);
									}
							}
						} else {
							
							throw new Exception(
									"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +batchNo+ "  scanned at the location  "+FROM_LOC);
						}
						
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, item);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.TODET_TONUM, TONO);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, orderLNo);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.FROMLOC, FROM_LOC);
			receiveMaterial_HM.put(IConstants.TOLOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC, FROM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_" + FROM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
			receiveMaterial_HM.put(IConstants.BATCH, batchNo);
			receiveMaterial_HM.put(IConstants.QTY, pickingqty);
			receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, REMARKS);
			receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
			//start added by Bruhan for fine tuning process on 06 August 2013
			receiveMaterial_HM.put("TYPE", "TRBULK");
			//End added by Bruhan for fine tuning process on 06 August 2013
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			receiveMaterial_HM.put(IConstants.ISSUEDATE, strTranDate);
			xmlStr = "";
    		flag = _TOUtil.process_BulkToPickingForPC(receiveMaterial_HM);
			if(!flag)
				break process;
			}
				
		}
		if (flag) {
					
			//DbBean.CommitTran(ut);				
			request.getSession().setAttribute(
			"RESULTPICK",
			"Consignment Order : " + TONO
			+ "  Picked/Issued successfully!");
			response.sendRedirect("jsp/BulkTransferOrderPickIssue.jsp?action=MultipleView&PLANT="
			+ PLANT + "&TONO=" + TONO);
			} else {
					//DbBean.RollbackTran(ut);
					request.getSession()
							.setAttribute(
									"RESULTERROR",
									"Failed to Pick/Issue Consignment Order : "
											+ TONO);
					response.sendRedirect("jsp/BulkTransferOrderPickIssue.jsp?result=catchrerror");
				}
				
				
		}
		catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			response
					.sendRedirect("jsp/BulkTransferOrderPickIssue.jsp?action=MultipleView&PLANT="
							+ PLANT + "&TONO=" + TONO 
							+"&LOC="+ITEM_LOC
							+"&REF="
							+REMARKS
							+ "&allChecked="
							+allChecked
							+"&fullIssue="
							+fullIssue
							+"&result=catcherror");
			//throw e;
		}

		return xmlStr;
	}
	 //End code by Bruhan for bulktransfer order picking on 14/03/2013 
    /* ************Modification History*********************************
	   Oct 16 2014 Bruhan, Description: To include Transaction date
	*/  
    private String process_doTranferOrderPickingForSingleStepMultiple( HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException, Exception {
            UserTransaction ut = null;
            Map receiveMaterial_HM = null;
            String PLANT = "", TO_NUM = "", ITEM_NUM = "", TO_LN_NUM = "";
            String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "";
            String ITEM_BATCH = "", BATCH_ID="", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "";
            String REF = "", PICKEDQTY = "", REMARK = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
            try {
                 HttpSession session = request.getSession();
                PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
                TO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
                TO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
                CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
                ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
                ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
                LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
                ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
                INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
                ITEM_QTY =StrUtils.removeFormat( StrUtils.fString(request.getParameter("QTY")));
                PICKEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKEDQTY")));
                ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
                REF = StrUtils.fString(request.getParameter("REF"));
				TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
    			if (TRANSACTIONDATE.length()>5)
    				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
    				strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
                String index = StrUtils.fString(request.getParameter("DYNAMIC_PICKING_SIZE"));
                int count= Integer.parseInt(index);
                ut = com.track.gates.DbBean.getUserTranaction();
                ut.begin();
                boolean flag = false;
                for (int i = 0; i < count; i++) {
                        ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"+"_"+i));
                        BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID"+"_"+i));
                        PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY"+"_"+i)));
                        ITEM_LOC = StrUtils.fString(request.getParameter("FROMLOC"+"_"+i));
                        FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"+"_"+i));
                        TO_LOC = StrUtils.fString(request.getParameter("TOLOC"+"_"+i));
                double pickingqty = Double.parseDouble(PICKING_QTY);
                pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
                PICKING_QTY = String.valueOf(pickingqty);
                PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);      
                    receiveMaterial_HM = new HashMap();
                    receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                    receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
                    receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
                    receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
                    receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
                    receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                    receiveMaterial_HM.put(IConstants.FROMLOC, FROM_LOC);
                    receiveMaterial_HM.put(IConstants.TOLOC, TO_LOC);
                    receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
                    receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_" + FROM_LOC);
                    receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                    receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TO_NUM));
                    receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                    receiveMaterial_HM.put(IConstants.BATCH_ID, BATCH_ID);
                    receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
                    receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
                    receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
                    receiveMaterial_HM.put(IConstants.REMARKS, REF);
                    receiveMaterial_HM.put("TYPE", "SINGLESTEP");
					receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
                    receiveMaterial_HM.put(IConstants.ISSUEDATE, strTranDate);
                    xmlStr = "";

                    // check for item in location
                    Hashtable htLocMst = new Hashtable();
                    htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
                    htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
                    htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

                    UserLocUtil uslocUtil = new UserLocUtil();
                    uslocUtil.setmLogger(mLogger);
                    boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, LOGIN_USER, FROM_LOC);
                    if (!isvalidlocforUser) {
                            throw new Exception("To Loc :" + TO_LOC+ " is not User Assigned Location");
                    }
                   
                    flag = _TOUtil.process_Wms_ToPicking(receiveMaterial_HM);
                    if(!flag){
                        throw new Exception(" Error in processing the Order");
                    }
                        
                }
                
                if (flag) {
                        DbBean.CommitTran(ut);
                      
                        request.getSession().setAttribute("RESULTPICKING","Product  : " + ITEM_NUM + "  Picking successfully!");
                        response.sendRedirect("jsp/TOSummaryForSingleStepPickIssue.jsp?action=View&PLANT="+ PLANT + "&TONO=" + TO_NUM);
                } else {
                        DbBean.RollbackTran(ut);
                        request.getSession().setAttribute("RESULTERROR","Product Received Already : " + ITEM_NUM);
                        response.sendRedirect("jsp/TOSummaryForSingleStepMultipleIssue.jsp?action=resulterror");
                }

            }

            catch (Exception e) {
                    DbBean.RollbackTran(ut);
                    this.mLogger.exception(this.printLog, "", e);
                    ToDetDAO _ToDetDAO = new ToDetDAO();
                    _ToDetDAO.setmLogger(mLogger);

                    List listQry = _ToDetDAO.getTransferReceivingDetailsByWMS(PLANT, TO_NUM);
                    request.getSession().setAttribute("assigneelistqry2", listQry);
                    request.getSession().setAttribute("QTYERROR", e.getMessage());
                    response.sendRedirect("jsp/TOSummaryForSingleStepMultipleIssue.jsp?action=qtyerror&ITEMNO="
                                                    + ITEM_NUM
                                                    + "&ITEMDESC="
                                                    + strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
                                                    + "&FROMLOC="
                                                    + ITEM_LOC
                                                    + "&TOLOC="
                                                    + TO_LOC
                                                    + "&ORDERNO="
                                                    + TO_NUM
                                                    + "&ORDERLNO="
                                                    + TO_LN_NUM
                                                    + "&BATCH="
                                                    + ITEM_BATCH
                                                    + "&ORDERQTY="
                                                    + ORDER_QTY
                                                    + "&QTY="
                                                    + ITEM_QTY
                                                    + "&INVQTY="
                                                    + INV_QTY
                                                    + "&REF="
                                                    + REF
                                                    + "&PICKEDQTY="
                                                    + PICKEDQTY
                                                    + "&PICKINGQTY=" + PICKING_QTY);

            }

            return xmlStr;
    }

	private String TransferReceivingDataByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;
		StrUtils strUtils = new StrUtils();

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", TO_NUM = "", TO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "", ITEM_EXPDATE = "", FROM_WAREHOUSE = "", TO_WAREHOUSE = "";

		String PICKED_QTY = "", RECEIVED_QTY = "", TO_BATCH = "", LOC = "", TRAN_QTY = "", ISSUING_QTY = "", ORDER_QTY = "";
		try {
			String sepratedtoken = "";

			String totalString = StrUtils.fString(request
					.getParameter("TRAVELER"));

			StringTokenizer parser = new StringTokenizer(totalString, "=");

			while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
						",");
				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();
					mp.put("data" + count, sepratedtoken1);
					count++;

				}

			}
			HttpSession session = request.getSession();

			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			TO_NUM = strUtils.fString((String) mp.get("data1"));
			TO_LN_NUM = strUtils.fString((String) mp.get("data2"));
			ITEM_NUM = strUtils.fString((String) mp.get("data3"));
			ITEM_DESCRIPTION = strUtils.fString((String) mp.get("data4"));
			ORDER_QTY = strUtils.fString((String) mp.get("data5"));
			PICKED_QTY = strUtils.fString((String) mp.get("data6"));
			RECEIVED_QTY = strUtils.fString((String) mp.get("data7"));
            double availQty = Double.parseDouble(PICKED_QTY)-Double.parseDouble(RECEIVED_QTY);
			LOGIN_USER = strUtils.fString((String) mp.get("data8"));
			LOC = strUtils.fString((String) mp.get("data9"));
			TO_BATCH = strUtils.fString((String) mp.get("data10"));
			CUST_NAME = strUtils.fString((String) mp.get("data11"));
			FROM_WAREHOUSE = strUtils.fString((String) mp.get("data12"));
			TO_WAREHOUSE = strUtils.fString((String) mp.get("data13"));
			if (!flag) {
				response
						.sendRedirect("jsp/TransferOrderReceivingConfirm.jsp?ORDERNO="
								+ TO_NUM
								+ "&ORDERLNO="
								+ TO_LN_NUM
								+ "&CUSTNAME="
								+ strUtils.replaceCharacters2Send(CUST_NAME)
								+ "&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ strUtils
										.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&FROMLOC="
								+ FROM_WAREHOUSE
								+ "&TOLOC="
								+ TO_WAREHOUSE
								+ "&PICKEDQTY="
								+ PICKED_QTY
								+ "&ORDERQTY=" + ORDER_QTY+"&BATCH=NOBATCH"+"&AVAILABLEQTY="+StrUtils.displayDecimal(Double.toString(availQty)));
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	private String TransferMultiReceivingDataByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;
		StrUtils strUtils = new StrUtils();

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", TO_NUM = "", TO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "", ITEM_EXPDATE = "", FROM_WAREHOUSE = "", TO_WAREHOUSE = "";

		String PICKED_QTY = "", RECEIVED_QTY = "", TO_BATCH = "", LOC = "", TRAN_QTY = "", ISSUING_QTY = "", ORDER_QTY = "";
		try {
			String sepratedtoken = "";

			String totalString = StrUtils.fString(request
					.getParameter("TRAVELER"));

			StringTokenizer parser = new StringTokenizer(totalString, "=");

			while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
						",");
				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();
					mp.put("data" + count, sepratedtoken1);
					count++;

				}

			}
			HttpSession session = request.getSession();

			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			TO_NUM = strUtils.fString((String) mp.get("data1"));
			TO_LN_NUM = strUtils.fString((String) mp.get("data2"));
			ITEM_NUM = strUtils.fString((String) mp.get("data3"));
			ITEM_DESCRIPTION = strUtils.fString((String) mp.get("data4"));
			ORDER_QTY = strUtils.fString((String) mp.get("data5"));
			PICKED_QTY = strUtils.fString((String) mp.get("data6"));
			RECEIVED_QTY = strUtils.fString((String) mp.get("data7"));
                        double availQty = Double.parseDouble(PICKED_QTY)-Double.parseDouble(RECEIVED_QTY);
			LOGIN_USER = strUtils.fString((String) mp.get("data8"));
			LOC = strUtils.fString((String) mp.get("data9"));
			TO_BATCH = strUtils.fString((String) mp.get("data10"));
			CUST_NAME = strUtils.fString((String) mp.get("data11"));
			FROM_WAREHOUSE = strUtils.fString((String) mp.get("data12"));
			TO_WAREHOUSE = strUtils.fString((String) mp.get("data13"));
			if (!flag) {
				response
						.sendRedirect("jsp/MultiTransferOrderReceiveConfirm.jsp?ORDERNO="
								+ TO_NUM
								+ "&ORDERLNO="
								+ TO_LN_NUM
								+ "&CUSTNAME="
								+ strUtils.replaceCharacters2Send(CUST_NAME)
								+ "&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ strUtils
										.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&FROMLOC="
								+ FROM_WAREHOUSE+"&RECEIVED_QTY="+RECEIVED_QTY
								+ "&TOLOC="
								+ TO_WAREHOUSE
								+ "&PICKEDQTY="
								+ PICKED_QTY
								+ "&ORDERQTY=" + ORDER_QTY+"&BATCH=NOBATCH"+"&AVAILABLEQTY="+StrUtils.displayDecimal(Double.toString(availQty)));
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}

	private String process_orderToReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", TO_NUM = "", TONO = "", ITEM_NUM = "", TO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "", TEMP_LOC = "", PICKEDQTY = "", AVAILABLEQTY = "";
		String ITEM_BATCH = "", CUST_NAME = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", BALANCEQTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", REMARKS = "", FAX = "";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			TO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			TONO = StrUtils.fString(request.getParameter("ORDERNO"));
			TO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			RECEIVEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RECEIVEDQTY")));
			PICKEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKEDQTY")));
			double ipickedqty = Double.parseDouble(((String) PICKEDQTY.trim()
					.toString()));
			double OrdQty = Double.parseDouble(((String) QTY.trim().toString()));
			double receivedqty = Double.parseDouble(((String) RECEIVEDQTY.trim()));
			double balqty = OrdQty - receivedqty;
			balqty = StrUtils.RoundDB(balqty, IConstants.DECIMALPTS);
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RECEIVINGQTY")));
		double recivngqty = Double.parseDouble(ITEM_QTY); 
			recivngqty = StrUtils.RoundDB(recivngqty, IConstants.DECIMALPTS);
			
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			TEMP_LOC = StrUtils.fString(request.getParameter("TEMPLOC"));
			ITEM_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			BALANCEQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("BALANCEQTY")));
			AVAILABLEQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("AVAILABLEQTY")));

			REMARKS = StrUtils.fString(request.getParameter("REF"));
			FAX = StrUtils.fString(request.getParameter("FAX"));

			boolean qtyFlag = false;
			if (balqty < Double
					.parseDouble(((String) ITEM_QTY.trim().toString()))) {
				qtyFlag = true;
				throw new Exception(
						"Error in receiving item : Receiving Qty Should less than or equal to ordered Qty and Picked Qty");
			}

			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-"
						+ ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}
			ITEM_QTY = String.valueOf(recivngqty);
			ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC1, IConstants.TEMP_LOC + "_"
					+ FROM_LOC);

			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil
					.getCustCode(PLANT, TO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, QTY);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.USERFLD1, strUtils.InsertQuotes(REMARKS));
			receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
			receiveMaterial_HM.put(IConstants.CREATED_AT,
					(String) new DateUtils().getDateTime());
			receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.REMARKS, strUtils.InsertQuotes(REMARKS));
			receiveMaterial_HM.put("INV_QTY1", "1");


			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("PLANT", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, TO_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("To Loc :" + FROM_LOC
						+ " is not a User Assigned Location/Valid Location");
			}

			boolean flag = false;
			flag = _TOUtil.process_ToReceiveMaterialForPC(receiveMaterial_HM);
			if (flag) {
				request.getSession().setAttribute("RESULTRECIVE",
						"Product : " + ITEM_NUM + "  received successfully!");
				response
						.sendRedirect("jsp/TransferOrderReceiving.jsp?action=View&TONO="
								+ TONO);
			} else {
				request.getSession().setAttribute(
						"RESULTERROR",
						"Error in receiving Consignment order Product : "
								+ ITEM_NUM + " Order");
				response
						.sendRedirect("jsp/TransferOrderReceivingConfirm.jsp?action=resulterror");

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/TransferOrderReceivingConfirm.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&LOC="
							+ ITEM_LOC
							+ "&ORDERNO="
							+ TO_NUM
							+ "&ORDERLNO="
							+ TO_LN_NUM
							+ "&CUSTNAME="
							+ CUST_NAME
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ QTY
							+ "&RECEIVEQTY="
							+ ITEM_QTY
							+ "&FROMLOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&PICKEDQTY="
							+ PICKEDQTY
							+ "&AVAILABLEQTY="
							+ AVAILABLEQTY
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&RECEIVINGQTY="
							+ ITEM_QTY + "&RECEIVEDQTY=" + RECEIVEDQTY);

		}
		return xmlStr;
	}
	/* ************Modification History*********************************
	   Oct 14 2014 Bruhan, Description: To include Transaction date
	*/  
	private String process_orderToMultiReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", TO_NUM = "", TONO = "", ITEM_NUM = "", TO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "", TEMP_LOC = "", PICKEDQTY = "", AVAILABLEQTY = "";
		String ITEM_BATCH = "", CUST_NAME = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", BALANCEQTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", 
			REMARKS = "", FAX = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			TO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			TONO = StrUtils.fString(request.getParameter("ORDERNO"));
			TO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			
			QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			RECEIVEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RECEIVEDQTY")));
			PICKEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKEDQTY")));
			double ipickedqty = Double.parseDouble(((String) PICKEDQTY.trim()
					.toString()));
			double OrdQty = Double.parseDouble(((String) QTY.trim().toString()));
			double receivedqty = Double.parseDouble(((String) RECEIVEDQTY.trim()));
			double balqty = OrdQty - receivedqty;
			balqty = StrUtils.RoundDB(balqty, IConstants.DECIMALPTS);
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			
			TEMP_LOC = StrUtils.fString(request.getParameter("TEMPLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC_0"));
			BALANCEQTY = StrUtils.fString(request.getParameter("BALANCEQTY"));
			AVAILABLEQTY = StrUtils.removeFormat(StrUtils.fString(request
					.getParameter("AVAILABLEQTY")));

			REMARKS = StrUtils.fString(request.getParameter("REF"));
			FAX = StrUtils.fString(request.getParameter("FAX"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
            String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_RECEIVING_SIZE"));
    		int recvcnt = Integer.parseInt(DYNAMIC_RECEIVING_SIZE);
			boolean qtyFlag = false;
			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-"
						+ ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}
			boolean flag = false;
			for (int index = 0; index < recvcnt; index++) {
				ITEM_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("RECEIVINGQTY"+"_"+index)));
				ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"+"_"+index));
				ITEM_LOC = StrUtils.fString(request.getParameter("TOLOC"+"_"+index));
				double recivngqty = Double.parseDouble(ITEM_QTY); 
				recivngqty = StrUtils.RoundDB(recivngqty, IConstants.DECIMALPTS);
				ITEM_QTY = String.valueOf(recivngqty);
				ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC1, IConstants.TEMP_LOC + "_"+ FROM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, QTY);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.USERFLD1, strUtils.InsertQuotes(REMARKS));
			receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
			receiveMaterial_HM.put(IConstants.CREATED_AT,(String) new DateUtils().getDateTime());
			receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.REMARKS, strUtils.InsertQuotes(REMARKS));
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
 			receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
			xmlStr = "";
			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("PLANT", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, TO_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("To Loc :" + FROM_LOC
						+ " is not a User Assigned Location/Valid Location");
			}
		
			flag = _TOUtil.process_ToReceiveMaterialForPC(receiveMaterial_HM);
			}
			if (flag) {
				request.getSession().setAttribute("RESULTRECIVE",
						"Product : " + ITEM_NUM + "  received successfully!");
				response
						.sendRedirect("jsp/MultiTransferOrderReceiving.jsp?action=MultipleView&TONO="
								+ TONO);
			} else {
				request.getSession().setAttribute(
						"RESULTERROR",
						"Error in receiving Consignment order Product : "
								+ ITEM_NUM + " Order");
				response
						.sendRedirect("jsp/MultiTransferOrderReceiveConfirm.jsp?action=resulterror");

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/MultiTransferOrderReceiveConfirm.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&LOC="
							+ ITEM_LOC
							+ "&ORDERNO="
							+ TO_NUM
							+ "&ORDERLNO="
							+ TO_LN_NUM
							+ "&CUSTNAME="
							+ CUST_NAME
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ QTY
							+ "&RECEIVEQTY="
							+ ITEM_QTY
							+ "&FROMLOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&PICKEDQTY="
							+ PICKEDQTY
							+ "&AVAILABLEQTY="
							+ AVAILABLEQTY
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&RECEIVINGQTY="
							+ ITEM_QTY + "&RECEIVEDQTY=" + RECEIVEDQTY);

		}
		return xmlStr;
	}
	
	/**
	 * Author Bruhan. 3 Aug 2012
	 * method : process_orderToBulkReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) description : Process the information to recieve the checked ordered numbers.)
	 * 
	 * @param : HttpServletRequest request,
			HttpServletResponse response
	 * @return : String
	 * @throws Exception
	 */
	/* ************Modification History*********************************
	   Oct 14 2014 Bruhan, Description: To include Transaction date
	*/  	
	private String process_orderToBulkReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", TONO = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "", TEMP_LOC = "", PICKEDQTY = "";
		String CUST_NAME = "",ITEM_LOC = "",REMARKS = "";
		String collectionDate = "",collectionTime = "",remark = "",PERSON_INCHARGE = "";
		String orderLNo = "",receivingQty = "" ,QTYOR="",QTYRC= "",item = "",value = null,
		batchNo = "NOBATCH",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		Map checkedTOS = new HashMap();
		//UserTransaction ut = null;
		Boolean allChecked = false,fullReceive = false;
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			TONO = StrUtils.fString(request.getParameter("TONO"));			
			String[] chkdToNos  = request.getParameterValues("chkdToNo");
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));	
			PERSON_INCHARGE = StrUtils.fString(request.getParameter("PERSON_INCHARGE"));	
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC_0"));
			TO_LOC =   StrUtils.fString(request.getParameter("TO_WAREHOUSE"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_WAREHOUSE"));
			collectionDate = StrUtils.fString(request.getParameter("COLLECTION_DATE"));
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
			remark = StrUtils.fString(request.getParameter("REMARK1"));
			REMARKS =  StrUtils.fString(request.getParameter("REF"));
	
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullReceive")!=null){
				fullReceive = true;
			}
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, ITEM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("To Loc :" + ITEM_LOC
						+ " is not a User Assigned Location/Valid Location");
			}
			
			if (chkdToNos != null)    {     
				for (int i = 0; i < chkdToNos.length; i++)       { 
					value = chkdToNos[i];
					if (value != null) {
						orderLNo = value.split(":")[0];
						batchNo = value.split(":")[1];

					}
					receivingQty = StrUtils.fString(request.getParameter("receivingQty_"+orderLNo+"_"+batchNo));
					checkedTOS.put(orderLNo+":"+batchNo, receivingQty);
				}
				session.setAttribute("checkedTOS", checkedTOS);
            }
			
			Boolean transactionHandler = true;
			//ut = DbBean.getUserTranaction();
			//ut.begin();

			ArrayList DODetails = null;
			Hashtable htDoDet = new Hashtable();
			String queryDoDet = "item,isnull(qtyor,0) as qtyor,isnull(qtyrc,0) as qtyrc,ItemDesc";
					
			process: 	
				if (chkdToNos != null)    {     
					for (int i = 0; i < chkdToNos.length; i++)       { 
						value = chkdToNos[i];
				          if(value!=null)
				          {
				        	  orderLNo  = value.split(":")[0];
				        	  batchNo  = value.split(":")[1];
				        	 
				          }
				        boolean flag = false;
						receivingQty = StrUtils.fString(request.getParameter("receivingQty_"+orderLNo+"_"+batchNo));
						double recivngqty = Double.parseDouble(receivingQty); 
						recivngqty = StrUtils.RoundDB(recivngqty, IConstants.DECIMALPTS);
						receivingQty = String.valueOf(recivngqty);
						receivingQty = StrUtils.formatThreeDecimal(receivingQty);
						String pickedQty = StrUtils.fString(request.getParameter("Qtypicked_"+orderLNo+"_"+batchNo));
						String recvdQty = StrUtils.fString(request.getParameter("QtyReceived_"+orderLNo+"_"+batchNo));
						htDoDet.put(IConstants.TR_TONO, TONO);
			    		htDoDet.put(IConstants.PLANT, PLANT);
			    		htDoDet.put(IConstants.TR_TOLNNO, orderLNo);
						DODetails = _TOUtil.getToDetDetails(queryDoDet, htDoDet);
						if (DODetails.size() > 0) {	

							Map map1 = (Map) DODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("ItemDesc");
							QTYOR = (String) map1.get("qtyor");
							QTYRC = (String) map1.get("qtyrc");
						}
						
					receiveMaterial_HM = new HashMap();
					receiveMaterial_HM.put(IConstants.PLANT, PLANT);
					receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
					receiveMaterial_HM.put(IConstants.ITEM, item);
					receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
					receiveMaterial_HM.put(IConstants.TODET_TONUM, TONO);
					receiveMaterial_HM.put(IConstants.TODET_TOLNNO, orderLNo);
					receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
					receiveMaterial_HM.put(IConstants.LOC1, IConstants.TEMP_LOC+ "_" + FROM_LOC);
					receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
					receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
					receiveMaterial_HM.put(IConstants.INV_BATCH, batchNo);
					receiveMaterial_HM.put(IConstants.BATCH, batchNo);
					receiveMaterial_HM.put(IConstants.QTY, QTYOR);
					receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
					receiveMaterial_HM.put(IConstants.TR_RECVQTY, recvdQty);
					receiveMaterial_HM.put(IDBConstants.PICKQTY, pickedQty);
					receiveMaterial_HM.put(IConstants.TR_RECV_TYPE, "BULK");
					receiveMaterial_HM.put(IConstants.INV_QTY, receivingQty);
					receiveMaterial_HM.put(IConstants.RECV_QTY, receivingQty);
					receiveMaterial_HM.put(IConstants.INV_EXP_DATE, REMARKS);
					receiveMaterial_HM.put(IConstants.USERFLD1, strUtils.InsertQuotes(REMARKS));
					// receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
					receiveMaterial_HM.put(IConstants.CREATED_AT,(String) new DateUtils().getDateTime());
					receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
					receiveMaterial_HM.put(IConstants.REMARKS, strUtils.InsertQuotes(REMARKS));
					receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
	    			receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
					transactionHandler = _TOUtil
							.process_BulkReceiveMaterialForPC(receiveMaterial_HM) && true;
						
						if(!transactionHandler) break process;
						
					}
				}
		if (transactionHandler) {
				//DbBean.CommitTran(ut);
				request.getSession().setAttribute("RESULTRECIVE",
						"Products  received successfully!");
				response
						.sendRedirect("jsp/BulkTransferOrderReceiving.jsp?action=MultipleView&TONO="
								+ TONO);
			}else {
				//DbBean.RollbackTran(ut);
				throw new Exception("Unable to process..!");
			}

		} catch (Exception e) {
			//DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			response
					.sendRedirect("jsp/BulkTransferOrderReceiving.jsp?action=CATCHERROR&TONO="
							+ TONO
							+ "&FROMLOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&COLLECTION_DATE="
							+ collectionDate
							+ "&COLLECTION_TIME="
							+ collectionTime
							+ "&remark="
							+ REMARKS
							+ "&LOC="
							+ ITEM_LOC
							+ "&CUST_NAME="
							+ CUST_NAME
							+ "&PERSON_INCHARGE="
							+ PERSON_INCHARGE
							+ "&allChecked="
							+allChecked
							+"&fullReceive="
							+fullReceive
							);

		}
		return xmlStr;
	}
        
	/* ************Modification History*********************************
	   Oct 16 2014 Bruhan, Description: To include Transaction date
	*/ 
    private String process_TOMultipleReciptConfirm(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {
                 
             UserTransaction ut = null;
            Map receiveMaterial_HM = null;
            boolean flag = false;
            String PLANT = "", TO_NUM = "", TONO = "", ITEM_NUM = "", TO_LN_NUM = "";
            String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "";
            String ITEM_BATCH = "", CUST_NAME = "", QTY = "", ITEM_QTY = "",   ITEM_EXPDATE = "", ITEM_LOC = "", 
			REMARKS = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
            try {
                    HttpSession session = request.getSession();
                    userBean userBeanobj = new userBean();
                    encryptBean eb= new encryptBean();;
                    PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
                    TO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
                    TONO = StrUtils.fString(request.getParameter("ORDERNO"));
                    CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
                    LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
                    FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
                    ITEM_LOC = StrUtils.fString(request.getParameter("TOLOC"));
                    TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
     				TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
        			if (TRANSACTIONDATE.length()>5)
        				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
        				strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    UserLocUtil uslocUtil = new UserLocUtil();
                    uslocUtil.setmLogger(mLogger);
                    boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, LOGIN_USER, TO_LOC);
                    if (!isvalidlocforUser) {
                            throw new Exception("To Loc :" + FROM_LOC + " is not a User Assigned Location/Valid Location");
                    }
                
                    ArrayList dataList = new TOUtil().getToPickDetailsToConfirm(PLANT,TONO);
                    if (dataList.size()>0){
                        for (int index = 0; index < dataList.size(); index++) { 
                            Map lineArr = (Map) dataList.get(index);
                            TO_LN_NUM = StrUtils.fString((String)lineArr.get("TOLNO"));
                            ITEM_NUM = StrUtils.fString((String)lineArr.get("ITEM"));
                            ITEM_DESCRIPTION = StrUtils.fString( (String)lineArr.get("ITEMDESC"));
                            QTY = StrUtils.fString((String)lineArr.get("ORDQTY"));
                            ITEM_QTY = StrUtils.removeFormat(StrUtils.fString((String)lineArr.get("PICKQTY")));
                            ITEM_BATCH = StrUtils.fString((String)lineArr.get("BATCH"));
                            receiveMaterial_HM = new HashMap();
                            receiveMaterial_HM.clear();
                            receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                            receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                            receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
                            receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
                            receiveMaterial_HM.put(IConstants.TODET_TONUM, TO_NUM);
                            receiveMaterial_HM.put(IConstants.TODET_TOLNNO, TO_LN_NUM);
                            receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
                            receiveMaterial_HM.put(IConstants.LOC1, IConstants.TEMP_LOC + "_" + FROM_LOC);
                            receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                            receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TO_NUM));
                            receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
                            receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                            receiveMaterial_HM.put(IConstants.QTY, QTY);
                            receiveMaterial_HM.put(IConstants.ORD_QTY, QTY);
                            receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
                            receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
                            receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
                            receiveMaterial_HM.put(IConstants.USERFLD1, strUtils.InsertQuotes(REMARKS));
                            receiveMaterial_HM.put(IConstants.CREATED_AT, (String) new DateUtils().getDateTime());
                            receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
                            receiveMaterial_HM.put(IConstants.REMARKS, strUtils.InsertQuotes(REMARKS));
							receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
                            receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
                             flag = _TOUtil.process_Wms_ToReceiving(receiveMaterial_HM);
                            if(!flag){
                                 throw new Exception(" Error in processing the Order");
                             }
                       
                        }
                    }
                         if (flag) {
                                    DbBean.CommitTran(ut);
                                    request.getSession().setAttribute("RESULTPICKING","Products Received successfully!");
                                    response.sendRedirect("jsp/TOSummaryForSingleStepPickIssue.jsp?action=View&PLANT="+ PLANT + "&TONO=" + TO_NUM);
                            } else {
                                    DbBean.RollbackTran(ut);
                                    request.getSession().setAttribute("RESULTERROR","Error in Confirming Receiving for Order : " + TO_NUM);
                                    response.sendRedirect("jsp/transOrderReceiptToConfirm.jsp?action=resulterror");
                            }

              
                   
                  

            } catch (Exception e) {
                DbBean.RollbackTran(ut);
                    this.mLogger.exception(this.printLog, "", e);
                    request.getSession().setAttribute("QTYERROR", e.getMessage());
                    response
                                    .sendRedirect("jsp/transOrderReceiptToConfirm.jsp?action=qtyerror&"
                                                    + "&ORDERNO="
                                                    + TO_NUM
                                                    + "&CUSTNAME="
                                                    + CUST_NAME
                                                    + "&FROMLOC="
                                                    + FROM_LOC
                                                    + "&TOLOC="+ TO_LOC);

            }
            return xmlStr;
    }
    
    
    /* Created  by Bruhan for transfer order reversal on 21/Aug/2013    
       ************Modification History*********************************
 	   Oct 21 2014 Bruhan, Description: To include Transaction date
 	*/
	private String process_TranferOrderReversal(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
		 boolean flag = false;
		Map receiveMaterial_HM = null;
		String PLANT = "", TONO = "",tolno="",batch="",gino="",chkindex="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "", TEMP_LOC = "", PICKEDQTY = "",returnQty="";
		String CUST_NAME = "",ITEM_LOC = "",REMARKS = "",CONTACTNUM="",TELNO="",EMAIL="";
		String COLLECTION_DATE = "",COLLECTION_TIME = "",REMARK1 = "",REMARK2 = "",PERSON_INCHARGE = "",pickingqty="",reverseqty="";
		String orderLNo = "",QTYOR="",QTYPICK= "",item = "",value = null,batchNo = "NOBATCH",UOMQTY="1",UOM="";
		String ADD1="",ADD2="",ADD3="",ADD4="",COUNTRY="",ZIP="",ITEM_QTY = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";		
		Map checkedTOS = new HashMap();
		//UserTransaction ut = null;
		Boolean allChecked = false;
		try {

			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			TONO = StrUtils.fString(request.getParameter("TONO"));	
			gino = StrUtils.fString(request.getParameter("GINO"));	
			String[] chkdToNos  = request.getParameterValues("chkdToNo");
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			TO_LOC =   StrUtils.fString(request.getParameter("TO_WAREHOUSE"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_WAREHOUSE"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
 			if (TRANSACTIONDATE.length()>5)
 				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
 			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			 
			
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			
			
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, FROM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("From Loc :" + FROM_LOC
						+ " is not a User Assigned Location/Valid Location");
			}
			
									
			Boolean transactionHandler = true;
			//ut = DbBean.getUserTranaction();
			//ut.begin();
			
	
			ArrayList DODetails = null;
			Hashtable htDoDet = new Hashtable();
			String queryDoDet = "item,isnull(qtyor,0) as qtyor,isnull(qtypick,0) as qtypick,ItemDesc,ISNULL((select ISNULL(QPUOM,1) from "+PLANT+"_UOM where UOM=UNITMO),1) UOMQTY,ISNULL(UNITMO,'') UNITMO";
		
						
			process: 	
				if (chkdToNos != null)    {     
					for (int i = 0; i < chkdToNos.length; i++)       { 
						int iIndex = i + 1;
						
						String	data = chkdToNos[i];
						String[] chkdata = data.split(",");
		        		
					tolno = chkdata[0];
					batch = chkdata[1];
					item = chkdata[2];
					QTYOR = chkdata[3];
					QTYPICK = chkdata[4];
					TONO = chkdata[5];
					gino= chkdata[6];
					chkindex= chkdata[7];
								
					String lineindex = String.valueOf(chkindex);
			        orderLNo=lineindex+"_"+batch;
//					orderLNo = tolno+"_"+batch;
					
					//	orderLNo = chkdToNos[i];
				         /* if(value!=null)
				          {
				        	  orderLNo  = value.split(":")[0];
				        	  batchNo  = value.split(":")[1];
				        	 
				          }*/
						
						PICKEDQTY = StrUtils.fString(request.getParameter("Qtypicked_"+orderLNo));
						returnQty = StrUtils.fString(request.getParameter("QtyReverse_"+orderLNo));	
						batchNo = StrUtils.fString(request.getParameter("batch_"+orderLNo));
					
				        double reversqty = Double.parseDouble(returnQty); 
				        reversqty = StrUtils.RoundDB(reversqty, IConstants.DECIMALPTS);
				        reverseqty = String.valueOf(reversqty);
				       // reverseqty = StrUtils.formatThreeDecimal(reverseqty);
						batchNo = StrUtils.fString(request.getParameter("batch_"+orderLNo));
						
						htDoDet.put(IConstants.TR_TONO, TONO);
			    		htDoDet.put(IConstants.PLANT, PLANT);
			    		htDoDet.put(IConstants.TR_TOLNNO, tolno);
						DODetails = _TOUtil.getToDetDetails(queryDoDet, htDoDet);
						if (DODetails.size() > 0) {	

							Map map1 = (Map) DODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("ItemDesc");
							QTYOR = (String) map1.get("qtyor");
							QTYPICK = (String) map1.get("qtypick");
							UOMQTY = (String) map1.get("UOMQTY");
							UOM = (String) map1.get("UNITMO");
						}						
						
						
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, item);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
			receiveMaterial_HM.put(IConstants.TODET_TONUM, TONO);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, tolno);
			receiveMaterial_HM.put(IConstants.LOC, FROM_LOC);
			//receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_" + FROM_LOC);
			//receiveMaterial_HM.put(IConstants.LOC2, FROM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, TO_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.BATCH, batchNo);
			receiveMaterial_HM.put(IConstants.QTY, reverseqty);
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			receiveMaterial_HM.put("UOMQTY", UOMQTY);
			receiveMaterial_HM.put(IConstants.UOM, UOM);
			receiveMaterial_HM.put("GINO", gino);
			
			

			xmlStr = "";
					
			flag = _TOUtil.process_TOReversal(receiveMaterial_HM);
			if(!flag)
				break process;
			}
				
		}
		if (flag) {
					
			//DbBean.CommitTran(ut);				
			request.getSession().setAttribute(
			"RESULTPICK",
			"Consignment Order : " + TONO
			+ "  Reversed successfully!");
			response.sendRedirect("../consignment/orderreversal?action=MultipleView&PLANT="
			+ PLANT + "&TONO=" + TONO);
			} else {
					//DbBean.RollbackTran(ut);
					request.getSession()
							.setAttribute(
									"RESULTERROR",
									"Failed to Reverse Consignment Order : "
											+ TONO);
					response.sendRedirect("../consignment/orderreversal?result=catchrerror");
				}
				
				
		}
		catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			response
					.sendRedirect("../consignment/orderreversal?action=MultipleView&PLANT="
							+ PLANT + "&TONO=" + TONO 
							+ "&allChecked="
							+allChecked
							+"&result=catcherror");
			//throw e;
		}

		return xmlStr;
	}
	 //End code by Bruhan for transfer order reversal on 21/Aug/2013
    
    
    
    public void viewTOReport(HttpServletRequest request,
            HttpServletResponse response) throws IOException, Exception {
    Connection con = null;
    try {

            CustUtil cUtil = new CustUtil();
            PlantMstUtil pmUtil = new PlantMstUtil();
            StrUtils strUtils = new StrUtils();
            HttpSession session = request.getSession();
            String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "";
            ;

            String TONO = strUtils.fString(request.getParameter("TONO"));
            String PLANT = (String) session.getAttribute("PLANT");
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
	        
	        String imagePath2,Orientation="";	
            con = DbBean.getConnection();
            String SysDate = DateUtils.getDate();
            String jasperPath = DbBean.JASPER_INPUT + "/" + "rptTransferOrder";

            ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
            for (int i = 0; i < listQry.size(); i++) {
                    Map map = (Map) listQry.get(i);

                    CNAME = (String) map.get("PLNTDESC");
                    CADD1 = (String) map.get("ADD1");
                    CADD2 = (String) map.get("ADD2");
                    CADD3 = (String) map.get("ADD3");
                    CADD4 = (String) map.get("ADD4");
                    CCOUNTRY = (String) map.get("COUNTY");
                    CZIP = (String) map.get("ZIP");
                    CTEL = (String) map.get("TELNO");
                    CFAX = (String) map.get("FAX");
                    CONTACTNAME = (String) map.get("NAME");
                    CHPNO = (String) map.get("HPNO");
                    CEMAIL = (String) map.get("EMAIL");

            }
//            ArrayList arrCust = cUtil.getAssigneeDetailsForTO(TONO, PLANT);
            ArrayList arrCust = cUtil.getAssigneeDetailsForTONEW(TONO, PLANT);

            if (arrCust.size() > 0) {
                    String sCustCode = (String) arrCust.get(0);
                    String sCustName = (String) arrCust.get(1);
                    String sAddr1 = (String) arrCust.get(2);
                    String sAddr2 = (String) arrCust.get(3);
                    String sAddr3 = (String) arrCust.get(4);
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
                String sAddr4 = (String) arrCust.get(15);
                String sIsactive = (String) arrCust.get(16);
                
                String sShipCustName = (String) arrCust.get(17);
                String sShipContactName = (String) arrCust.get(18);
                String sShipAddr1 = (String) arrCust.get(19);
                String sShipAddr2 = (String) arrCust.get(20);
                String sShipCity = (String) arrCust.get(21);
                String sShipCountry = (String) arrCust.get(22);
                String sShipZip = (String) arrCust.get(23);
                String sshipTelno = (String) arrCust.get(24);
                String orderRemarks = (String) arrCust.get(25);
                if(orderRemarks.length()>0) orderRemarks = "Order Remarks : "+orderRemarks;

                    Map parameters = new HashMap();
                    parameters.put("imagePath", imagePath);
                    parameters.put("imagePath1", imagePath1);
        
                    // Customer Details
                    parameters.put("OrderNo", TONO);
                    parameters.put("orderRemarks", orderRemarks);
                    parameters.put("company", PLANT);
                    parameters.put("To_CompanyName", sCustName);
                    parameters.put("To_BlockAddress", sAddr1+"  " + sAddr2);
                    parameters.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
                    parameters.put("To_Country", sCountry);
                    parameters.put("To_ZIPCode", sZip);
                    parameters.put("To_AttentionTo", sContactName);
                    parameters.put("To_CCTO", "");
                    parameters.put("To_TelNo", sTelNo);
                    parameters.put("To_Email", sEmail);
                    parameters.put("To_Fax", sFax);
   				    parameters.put("SupRemarks", sRemarks);

                    parameters.put("STo_Addr1", sShipAddr1);
                    parameters.put("STo_Addr2", sShipAddr2);
                    parameters.put("STo_City", sShipCity);
                    parameters.put("STo_Country", sShipCountry);
                    parameters.put("STo_ZIP", sShipZip);
                    parameters.put("STo_Telno", sshipTelno);
                    if(sShipContactName.length()>0)  parameters.put("STo_Telno", "Tel : "+sshipTelno); else parameters.put("STo_Telno", sshipTelno);
                    if(sShipCustName.length()>0) {
                    parameters.put("STo_AttentionTo", "Attn : "+sShipContactName);
                    }else{
                    parameters.put("STo_AttentionTo", sShipContactName);
                    }
                    // Company Details
                    parameters.put("fromAddress_CompanyName", CNAME);
                    parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
                    parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
                    parameters.put("fromAddress_Country", CCOUNTRY);
                    parameters.put("fromAddress_ZIPCode", CZIP);
                    parameters.put("fromAddress_TpNo", CTEL);
                    parameters.put("fromAddress_FaxNo", CFAX);
                    parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
                    parameters.put("fromAddress_ContactPersonMobile", CHPNO);
                    parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
                    parameters.put("currentTime", _TOUtil.getOrderDate(PLANT,TONO));
                    parameters.put("refNo", new TOUtil().getJobNum(PLANT,TONO));
                    parameters.put("InvoiceTerms", "");
                    
                    
                	//report template parameter added on June 28 2011 By Bruhan
    				TOUtil toUtil = new TOUtil();
    		       	Map ma = toUtil.getTOReceiptHdrDetails(PLANT);
    		       	Orientation = (String) ma.get("PrintOrientation");
                    //ship to Address
                  if(sShipCustName.length()>0) {
                   parameters.put("STo_CustName", "SHIP TO : "+ "\n"+ "\n"+ sShipCustName);
                  
                  }else{
                     parameters.put("STo_CustName", sShipCustName);  
                  }
 		
    		        parameters.put("OrderHeader", (String) ma.get("HDR1"));
                        parameters.put("ToHeader", (String) ma.get("HDR2"));
                        parameters.put("FromHeader", (String) ma.get("HDR3"));
                        parameters.put("Date", (String) ma.get("DATE"));
                        parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
                        parameters.put("RefNo", (String) ma.get("REFNO"));
                        parameters.put("SoNo", (String) ma.get("SONO"));
                        parameters.put("Item", (String) ma.get("ITEM"));
                        parameters.put("Description", (String) ma.get("DESCRIPTION"));
                        
                        parameters.put("OrderQty", (String) ma.get("ORDERQTY"));;
                        parameters.put("UOM", (String) ma.get("UOM"));
                        parameters.put("Footer1", (String) ma.get("F1"));
                        parameters.put("Footer2", (String) ma.get("F2"));
                        parameters.put("Footer3", (String) ma.get("F3"));
                        parameters.put("Footer4", (String) ma.get("F4"));
                        parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
                        if(Orientation.equals("Portrait")){
                       	 parameters.put("STo_CustName",sShipCustName);
       						parameters.put("STo", "SHIPTO");
       						   
       				        if (!checkImageFile.exists()) {
       					           imagePath2 = imagePath1; 
       					           imagePath = "";
       					        }
       				        else if(!checkImageFile1.exists()){
       				        	imagePath2 = imagePath; 
       				        	imagePath1 = "";
       				        }
       				        else{
       				        	imagePath2 ="";	        	
       				        }
       						parameters.put("imagePath", imagePath);
       						parameters.put("imagePath1", imagePath1);
       						parameters.put("imagePath2", imagePath2);
       				        
       						jasperPath = DbBean.JASPER_INPUT + "/" + "rptTransferOrderPortrait";
       						
                        }

                    long start = System.currentTimeMillis();
                    System.out.println("**************" + " Start Up Time : "
                                    + start + "**********");
                    JasperCompileManager.compileReportToFile(
		        			jasperPath+".jrxml",jasperPath+".jasper");

                    byte[] bytes = JasperRunManager.runReportToPdf(jasperPath
                                    + ".jasper", parameters, con);

          
                    //response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
					response.addHeader("Content-disposition", "inline;filename=reporte.pdf");
                    response.setContentLength(bytes.length);
                    response.getOutputStream().write(bytes);
                    response.setContentType("application/pdf");
					response.getOutputStream().flush();
        			response.getOutputStream().close();
            }

    } catch (IOException e) {

            e.printStackTrace();

    } finally {
            DbBean.closeConnection(con);
    }
}
    //new changes
    
//    public void viewTOReport(HttpServletRequest request,
//            HttpServletResponse response) throws IOException, Exception {
//    Connection con = null;
//    MasterUtil _masterUtil = new MasterUtil();
//    try {
//
//            CustUtil cUtil = new CustUtil();
//            PlantMstUtil pmUtil = new PlantMstUtil();
//            StrUtils strUtils = new StrUtils();
//            HttpSession session = request.getSession();
//            String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "",CSTATE = "", CRCBNO = "",companyregno = "",CWEBSITE = "",SEALNAME="",SIGNNAME="";
//            String query ="";
//            String TONO = strUtils.fString(request.getParameter("TONO"));
//            String ACTIONVALUE = strUtils.fString(request.getParameter("Submit"));
//            String PLANT = (String) session.getAttribute("PLANT");
//            String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
//			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
//			List viewlistQry = pmUtil.getPlantMstDetails(PLANT);
//			Map maps = (Map) viewlistQry.get(0);
//			SEALNAME=StrUtils.fString((String)maps.get("SEALNAME"));
//            SIGNNAME=StrUtils.fString((String)maps.get("SIGNATURENAME"));
//            String sealPath = "",signPath="";
//            //imti get seal name from plantmst
//            if(SEALNAME.equalsIgnoreCase("")){
//            	sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
//            }else {
//            	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
//            }
//            if(SIGNNAME.equalsIgnoreCase("")){
//            	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
//            }else {
//               	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
//               	if (!new File(signPath).exists()) {
//    				signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
//    			}
//            }
//            //imti end
//			Hashtable htCond = new Hashtable();
//			htCond.put("PLANT", PLANT);
//			htCond.put("TONO", TONO);
//			
//			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
//			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
//			
//			String dtCondStr = "  and ISNULL(a.RECVDATE,'')<>'' AND CAST((SUBSTRING(a.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
//			String sCondition = "", fdate = "", tdate = "", signaturePath = "";
//			if (FROM_DATE.length() > 5)
//				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
//			if (TO_DATE == null)
//				TO_DATE = "";
//			else
//				TO_DATE = TO_DATE.trim();
//			if (TO_DATE.length() > 5)
//				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
//			if (fdate.length() > 0) {
//				sCondition = sCondition + dtCondStr + " >= '" + fdate + "'  ";
//				if (tdate.length() > 0) {
//					sCondition = sCondition + dtCondStr + " <= '" + tdate + "'  ";
//				}
//			} else {
//				if (tdate.length() > 0) {
//					sCondition = sCondition + dtCondStr + "  <= '" + tdate + "'  ";
//				}
//			}
//			
//			File checkImageFile = new File(imagePath);
//	        if (!checkImageFile.exists()) {
//	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
//	        }
//	        File checkImageFile1 = new File(imagePath1);
//	        if (!checkImageFile1.exists()) {
//	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
//	        }
//	        
//	        String imagePath2,Orientation="";	
//            con = DbBean.getConnection();
//            String SysDate = DateUtils.getDate();
//            String jasperPath = DbBean.JASPER_INPUT + "/" + "rptTransferOrder";
//
//            ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
//            for (int i = 0; i < listQry.size(); i++) {
//                    Map map = (Map) listQry.get(i);
//
//                    CNAME = (String) map.get("PLNTDESC");
//    				CADD1 = (String) map.get("ADD1");
//    				CADD2 = (String) map.get("ADD2");
//    				CADD3 = (String) map.get("ADD3");
//    				CADD4 = (String) map.get("ADD4");
//    				CCOUNTRY = (String) map.get("COUNTY");
//    				CZIP = (String) map.get("ZIP");
//    				CTEL = (String) map.get("TELNO");
//    				CFAX = (String) map.get("FAX");
//    				CONTACTNAME = (String) map.get("NAME");
//    				CHPNO = (String) map.get("HPNO");
//    				CEMAIL = (String) map.get("EMAIL");
//    				CRCBNO = (String) map.get("RCBNO");
//    				CSTATE = (String) map.get("STATE");
//    				companyregno = (String) map.get("companyregnumber");//imtiuen
//    				CWEBSITE = (String) map.get("WEBSITE");
//
//            }
//            
//            
////            ArrayList arrCust = cUtil.getAssigneeDetailsForTO(TONO, PLANT);
//			ArrayList arrCust = cUtil.getCustomerDetailsForTO(TONO, PLANT);
//			if (arrCust.size() > 0) {
//				String sCustCode = (String) arrCust.get(0);
//				String sCustName = (String) arrCust.get(1);
//				String sAddr1 = (String) arrCust.get(2);
//				String sAddr2 = (String) arrCust.get(3);
//				String sAddr3 = (String) arrCust.get(4);
//				String sCountry = (String) arrCust.get(5);
//				String sZip = (String) arrCust.get(6);
////				String sCons = (String) arrCust.get(7);
////				String sCustNameL = (String) arrCust.get(8);
//				String sContactName = (String) arrCust.get(9);
////				String sDesgination = (String) arrCust.get(10);
//				String sTelNo = (String) arrCust.get(11);
////				String sHpNo = (String) arrCust.get(12);
//				String sFax = (String) arrCust.get(13);
//				String sEmail = (String) arrCust.get(14);
//				String sRemarks = (String) arrCust.get(15);
//				String sAddr4 = (String) arrCust.get(16);
//				String SHIPPINGID = (String) arrCust.get(25);
//				String sRcbno = (String) arrCust.get(26);
//				String suenno = (String) arrCust.get(27);//imtiuen
//                
//				ArrayList arrShippingDetails = _masterUtil.getTransferShippingDetails(TONO, sCustCode, PLANT);
//				Map parameters = new HashMap();
//				if (request.getParameter("with_batch") != null
//						&& "yes".equals(request.getParameter("with_batch"))) {
//					parameters.put("PrintBatch", "yes");
//				}
//				String sShipCustName = "";
//				if (arrShippingDetails.size() > 0) {
//					parameters.put("shipToId", sCustCode);
//					sShipCustName = (String) arrShippingDetails.get(1);
//					String sShipContactName = (String) arrShippingDetails.get(2);
//					String sshipTelno = (String) arrShippingDetails.get(3);
//					String sShipPhone = (String) arrShippingDetails.get(4);
////					String sShipFax = (String) arrShippingDetails.get(5);
////					String sShipEmail = (String) arrShippingDetails.get(6);
//					String sShipAddr1 = (String) arrShippingDetails.get(7);
//					String sShipAddr2 = (String) arrShippingDetails.get(8);
//					String sShipStreet = (String) arrShippingDetails.get(9);
//					String sShipCity = (String) arrShippingDetails.get(10);
//					String sShipState = (String) arrShippingDetails.get(11);
//					String sShipCountry = (String) arrShippingDetails.get(12);
//					String sShipZip = (String) arrShippingDetails.get(13);
//
//					parameters.put("STo_Addr1", sShipAddr1);
//					parameters.put("STo_Addr2", sShipAddr2);
//					parameters.put("STo_Addr3", sShipStreet);
//					parameters.put("STo_City", sShipCity);
//					if (sShipState.equals("")) {
//						parameters.put("STo_Country", sShipCountry);
//					}else {
//						parameters.put("STo_Country", sShipState + "\n" + sShipCountry);
//					}
//					System.out.println("STo_Country : " + parameters.get("STo_Country"));
//					parameters.put("STo_ZIP", sShipZip);
//					if (sshipTelno.length() > 0) {
//						parameters.put("STo_Telno", "Tel: " + sshipTelno);
//					} else {
//						parameters.put("STo_Telno", sshipTelno);
//					}
//					if (sShipContactName.length() > 0) {
//						parameters.put("STo_AttentionTo", "Attn: " + sShipContactName);
//					} else {
//						parameters.put("STo_AttentionTo", sShipContactName);
//					}
//					if (sShipPhone.length() > 0) {
//						parameters.put("STo_Phone", "HP: " + sShipPhone);
//					} else {
//						parameters.put("STo_Phone", sShipPhone);
//					}
//				} else {
//					parameters.put("shipToId", "");
//				}
//                String orderRemarks = (String) arrCust.get(25);
//                if(orderRemarks.length()>0) orderRemarks = "Order Remarks : "+orderRemarks;
//                
//				query = "currencyid,isnull(consignment_Gst,0) as outbound_Gst,isnull(orderdiscount,0) orderdiscount,FROMWAREHOUSE,TOWAREHOUSE,"
//						+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_tohdr.PROJECTID),'') PROJECT_NAME,"
//						+ "isnull(CRBY,'') as CRBY,isnull(shippingcost,0) shippingcost," + "isnull(incoterms,'') incoterms";
//				ArrayList arraydohdr = new ArrayList();
//				arraydohdr = _TOUtil.getToHdrDetails(query, htCond);
//				Map dataMap = (Map) arraydohdr.get(0);
////				String gstValue = dataMap.get("outbound_Gst").toString();
//				String DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
//				String DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
//				String DATAINCOTERMS = dataMap.get("incoterms").toString();
//				String PROJECT_NAME = dataMap.get("PROJECT_NAME").toString();
//				String FROMWAREHOUSE = dataMap.get("FROMWAREHOUSE").toString();
//				String TOWAREHOUSE = dataMap.get("TOWAREHOUSE").toString();
//				 orderRemarks = (String) arrCust.get(18);
////				String sPayTerms = (String) arrCust.get(19);
//				String orderRemarks3 = (String) arrCust.get(21);
//				String sDeliveryDate = (String) arrCust.get(22);
//				String sEmpno = (String) arrCust.get(23);
//				String sState = (String) arrCust.get(24);
//				
//				String orderType = new ToHdrDAO().getOrderTypeForTO(PLANT, TONO);
//
////                    Map parameters = new HashMap();
//                    parameters.put("imagePath", imagePath);
//                    parameters.put("imagePath1", imagePath1);
//                    parameters.put("signaturePath", signaturePath);
//        
//                    // Customer Details
//                    parameters.put("OrderNo", TONO);
//                    parameters.put("orderRemarks", orderRemarks);
//                    parameters.put("company", PLANT);
//                    parameters.put("To_CompanyName", sCustName);
//                    parameters.put("To_BlockAddress", sAddr1+"  " + sAddr2);
//                    parameters.put("To_RoadAddress", sAddr3 +"  " + sAddr4);
//					if (sState.equals("")) {
//						parameters.put("To_Country", sCountry);
//					}else {
//						parameters.put("To_Country", sState + "\n" + sCountry);
//					}
//                    parameters.put("To_Country", sCountry);
//                    parameters.put("To_ZIPCode", sZip);
//                    parameters.put("To_AttentionTo", sContactName);
//                    parameters.put("To_CCTO", "");
//                    parameters.put("To_TelNo", sTelNo);
//                    parameters.put("To_Email", sEmail);
//                    parameters.put("To_Fax", sFax);
//   				    parameters.put("SupRemarks", sRemarks);
//
//					parameters.put("To_ZIPCode", sZip);
//					parameters.put("To_AttentionTo", sContactName);
//					parameters.put("To_CCTO", "");
//					parameters.put("To_TelNo", sTelNo);
//					parameters.put("To_Fax", sFax);
//					parameters.put("To_Email", sEmail);
//					parameters.put("sRCBNO", sRcbno);
//					parameters.put("sUENNO", suenno);//imtiuen
//					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
//                 // Company Details
//					parameters.put("fromAddress_CompanyName", CNAME);
////					parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
////					parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
////					if(CSTATE.equals("")) {
////					parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
////					}else {
////						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
////					}
//
//					if(CADD1.equals("")) {
//						parameters.put("fromAddress_BlockAddress", CADD2);
//					}else {
//						parameters.put("fromAddress_BlockAddress", CADD1 + ", " + CADD2);
//					}
//					if(CADD3.equals("")) {
//						parameters.put("fromAddress_RoadAddress", CADD4);
//					}else {
//						parameters.put("fromAddress_RoadAddress", CADD3 + "," + CADD4);
//					}
//					if(CSTATE.equals("")) {
//						parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//					}else {
//						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//					}
//					
//					parameters.put("fromAddress_ZIPCode", CZIP);
//					parameters.put("fromAddress_TpNo", CTEL);
//					parameters.put("fromAddress_FaxNo", CFAX);
//					parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
//					parameters.put("fromAddress_Website", CWEBSITE);
//					parameters.put("fromAddress_ContactPersonMobile", CHPNO);
//					parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
//					parameters.put("fromAddress_RCBNO", CRCBNO);
//					parameters.put("currentTime", _TOUtil.getOrderDateOnly(PLANT, TONO));
//					parameters.put("refNo", _TOUtil.getJobNum(PLANT, TONO));
//					parameters.put("orderType", orderType);
//					parameters.put("InvoiceTerms", "");
//
//                    
//                    
//                	//report template parameter added on June 28 2011 By Bruhan
//    				TOUtil toUtil = new TOUtil();
////    		       	Map ma = toUtil.getTOReceiptHdrDetails(PLANT);
//    				Map ma= toUtil.getReceiptHdrDetails(PLANT,"Upon Creation");
//    		       	Orientation = (String) ma.get("PrintOrientation");
//                    //ship to Address
//                  if(sShipCustName.length()>0) {
//                   parameters.put("STo_CustName", "SHIP TO : "+ "\n"+ "\n"+ sShipCustName);
//                  
//                  }else{
//                     parameters.put("STo_CustName", sShipCustName);  
//                  }
// 		
//    		        parameters.put("OrderHeader", (String) ma.get("HDR1"));
//                        parameters.put("ToHeader", (String) ma.get("HDR2"));
//    					parameters.put("FromHeader", (String) ma.get("HDR3"));
//    					parameters.put("Date", (String) ma.get("DATE"));
//    					parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
//    					parameters.put("RefNo", (String) ma.get("REFNO"));
//    					parameters.put("SoNo", (String) ma.get("SONO"));
//    					parameters.put("Item", (String) ma.get("ITEM"));
//    					parameters.put("Description", (String) ma.get("DESCRIPTION"));
//    					parameters.put("DISPLAYSIGNATURE", (String) ma.get("DISPLAYSIGNATURE"));    					
//    					parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
//    					parameters.put("UOM", (String) ma.get("UOM"));
//    					parameters.put("Footer1", (String) ma.get("F1"));
//    					parameters.put("Footer2", (String) ma.get("F2"));
//    					parameters.put("Footer3", (String) ma.get("F3"));
//    					parameters.put("Footer4", (String) ma.get("F4"));
//    					parameters.put("Footer5", (String) ma.get("F5"));
//    					parameters.put("Footer6", (String) ma.get("F6"));
//    					parameters.put("Footer7", (String) ma.get("F7"));
//    					parameters.put("Footer8", (String) ma.get("F8"));
//    					parameters.put("Footer9", (String) ma.get("F9"));
//    					parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
//    					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
//    					parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
//    					parameters.put("IssueDateRange", sCondition);
//    					parameters.put("RCBNO", (String) ma.get("RCBNO"));
//    					parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
//    					parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
//    					parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
//    					parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
//    					parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
//    					parameters.put("HSCODE", (String) ma.get("HSCODE"));
//    					parameters.put("COO", (String) ma.get("COO"));
//    					parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
//    					parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
//    					parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
//    					parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
//    					parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
//    					parameters.put("PreByUser", dataMap.get("CRBY").toString());
//    					parameters.put("PROJECTNAME", PROJECT_NAME);
//    					parameters.put("PROJECT",  ma.get("PROJECT"));
//    					parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
//    					parameters.put("FROMWAREHOUSE", FROMWAREHOUSE);
//    					parameters.put("TOWAREHOUSE", TOWAREHOUSE);
//    					if (orderRemarks.length() > 0)
//    						orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
//    					if (orderRemarks3.length() > 0)
//    						orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;
//    					parameters.put("orderRemarks", orderRemarks);
//    					parameters.put("orderRemarks3", orderRemarks3);
////    					if (sDeliveryDate.length() > 0)
////    						sDeliveryDate = sDeliveryDate;
//    					parameters.put("DeliveryDt", sDeliveryDate);
//    					parameters.put("DeliveryDate", (String) ma.get("DELIVERYDATE"));
//    					if (ma.get("PRINTEMPLOYEE").equals("1")) {
//    						parameters.put("Employee", (String) ma.get("EMPLOYEE"));
//    						String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
//    						parameters.put("EmployeeName", empname);
//    					} else {
//    						parameters.put("Employee", "");
//    						parameters.put("EmployeeName", "");
//    					}
//
//    					if (sShipCustName.length() > 0) {
//    						parameters.put("STo_CustName", (String) ma.get("SHIPTO") + " : " + "\n" + sShipCustName);
//    					} else {
//    						parameters.put("STo_CustName", sShipCustName);
//    					}
//
//    					parameters.put("STo_CustName", sShipCustName);
//    					parameters.put("STo", (String) ma.get("SHIPTO"));
//                        if(Orientation.equals("Portrait")){
//                       	 parameters.put("STo_CustName",sShipCustName);
//       						parameters.put("STo", "SHIPTO");
//       						   
//       				        if (!checkImageFile.exists()) {
//       					           imagePath2 = imagePath1; 
//       					           imagePath = "";
//       					        }
//       				        else if(!checkImageFile1.exists()){
//       				        	imagePath2 = imagePath; 
//       				        	imagePath1 = "";
//       				        }
//       				        else{
//       				        	imagePath2 ="";	        	
//       				        }
//       						parameters.put("imagePath", imagePath);
//       						parameters.put("imagePath1", imagePath1);
//       						parameters.put("imagePath2", imagePath2);
//       						//imti start seal,sign condition in printoutconfig
//       						String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
//       						String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
//       						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
//       			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
//       			                }
//       						if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
//       							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
//       			                }
//       						parameters.put("sealPath", sealPath);
//       						parameters.put("signPath", signPath);
//       						
//       						parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
//       						parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
//       						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
//       						parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
//       						parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
//       						parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
//       						parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
//       						parameters.put("lblOrderDiscount",
//       								(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ")");
//       						Hashtable curHash = new Hashtable();
//       						curHash.put(IDBConstants.PLANT, PLANT);
//       						curHash.put(IDBConstants.CURRENCYID, dataMap.get("currencyid"));
//       						CurrencyUtil currUtil = new CurrencyUtil();
//       						String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
//       						parameters.put("lblShippingCost",
//       								(String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ")");
//       						double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
//       						double doubledatashippingcost = new Double(DATASHIPPINGCOST);
//       						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
//       						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
//       						parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
//       						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
//       				        
//       						//jasperPath = DbBean.JASPER_INPUT + "/" + "rptKitchenTransferOrderPortrait";
//       						if (ACTIONVALUE.equalsIgnoreCase("Print Transfer Order")) {
//       							jasperPath = DbBean.JASPER_INPUT + "/" + "rptTOWITHBATCHPortraitkit";  
//       						}else {
//       						  jasperPath = DbBean.JASPER_INPUT + "/" + "printTOWITHBATCHWITHPRICEKit";  
//       					}
//                        }
//
//                    long start = System.currentTimeMillis();
//                    System.out.println("**************" + " Start Up Time : "
//                                    + start + "**********");
//                    JasperCompileManager.compileReportToFile(
//		        			jasperPath+".jrxml",jasperPath+".jasper");
//
//                    byte[] bytes = JasperRunManager.runReportToPdf(jasperPath
//                                    + ".jasper", parameters, con);
//
//          
//                    //response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
//					response.addHeader("Content-disposition", "inline;filename=reporte.pdf");
//                    response.setContentLength(bytes.length);
//                    response.getOutputStream().write(bytes);
//                    response.setContentType("application/pdf");
//					response.getOutputStream().flush();
//        			response.getOutputStream().close();
//            }
//
//    } catch (IOException e) {
//
//            e.printStackTrace();
//
//    } finally {
//            DbBean.closeConnection(con);
//    }
//}
    
    private void viewTOReportNew(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		Connection con = null;
		MasterUtil _masterUtil = new MasterUtil();
		try {
			TOUtil _TOUtil = new TOUtil();
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "",CWEBSITE = "";

			String TONO = strUtils.fString(request.getParameter("TONO"));
			List jasperPrintList = new ArrayList();
//			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
			String PLANT = (String) session.getAttribute("PLANT");

			// ********To get from & to date issue date range*********************
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			
			String dtCondStr = "  and ISNULL(a.RECVDATE,'')<>'' AND CAST((SUBSTRING(a.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
			String sCondition = "", fdate = "", tdate = "", signaturePath = "";
			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			if (fdate.length() > 0) {
				sCondition = sCondition + dtCondStr + " >= '" + fdate + "'  ";
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" + tdate + "'  ";
				}
			} else {
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + "  <= '" + tdate + "'  ";
				}
			}

			// ********To get from & to date receive date range*********************

			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			SEALNAME=StrUtils.fString((String)maps.get("SEALNAME"));
            SIGNNAME=StrUtils.fString((String)maps.get("SIGNATURENAME"));
            String sealPath = "",signPath="";
            //imti get seal name from plantmst
            if(SEALNAME.equalsIgnoreCase("")){
            	sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
            }else {
            	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
            }
            if(SIGNNAME.equalsIgnoreCase("")){
            	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
            }else {
               	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
               	if (!new File(signPath).exists()) {
    				signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    			}
            }
            //imti end
			String imagePath2, Orientation = "";

			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			con = DbBean.getConnection();

			//String SysDate = DateUtils.getDate();
			String jasperPath;

			//java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);

				CNAME = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
				CRCBNO = (String) map.get("RCBNO");
				CSTATE = (String) map.get("STATE");
				companyregno = (String) map.get("companyregnumber");//imtiuen
				CWEBSITE = (String) map.get("WEBSITE");
			}

				String query = " ";
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("TONO", TONO);
				
//				ArrayList alShipDetail = new ArrayList();
				
				
				for(int j = 0; j < 1 ; j ++) {
					
				
					jasperPath = DbBean.JASPER_INPUT + "/" + "printTONOWITHOUTPRICE";
				
				
				
				signaturePath = DbBean.COMPANY_SIGN_PATH + "/" + PLANT.toLowerCase() + "/" + TONO + ".gif";
				File checkSignatureFile = new File(signaturePath);
				if (!checkSignatureFile.exists()) {
					signaturePath = DbBean.COMPANY_LOGO_PATH + "/" + "NoLogo.jpg";
				}
				ArrayList arrCust = cUtil.getCustomerDetailsForTO(TONO, PLANT);
				if (arrCust.size() > 0) {
					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
//					String sCustNameL = (String) arrCust.get(8);
					String sContactName = (String) arrCust.get(9);
//					String sDesgination = (String) arrCust.get(10);
					String sTelNo = (String) arrCust.get(11);
//					String sHpNo = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sEmail = (String) arrCust.get(14);
//					String sRemarks = (String) arrCust.get(15);
					String sAddr4 = (String) arrCust.get(16);
					String SHIPPINGID = (String) arrCust.get(25);
					String sRcbno = (String) arrCust.get(26);
					String suenno = (String) arrCust.get(27);//imtiuen
					ArrayList arrShippingDetails = _masterUtil.getTransferShippingDetails(TONO, sCustCode, PLANT);
					Map parameters = new HashMap();
					if (request.getParameter("with_batch") != null
							&& "yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					String sShipCustName = "";
					if (arrShippingDetails.size() > 0) {
						parameters.put("shipToId", sCustCode);
						sShipCustName = (String) arrShippingDetails.get(1);
						String sShipContactName = (String) arrShippingDetails.get(2);
						String sshipTelno = (String) arrShippingDetails.get(3);
						String sShipPhone = (String) arrShippingDetails.get(4);
//						String sShipFax = (String) arrShippingDetails.get(5);
//						String sShipEmail = (String) arrShippingDetails.get(6);
						String sShipAddr1 = (String) arrShippingDetails.get(7);
						String sShipAddr2 = (String) arrShippingDetails.get(8);
						String sShipStreet = (String) arrShippingDetails.get(9);
						String sShipCity = (String) arrShippingDetails.get(10);
						String sShipState = (String) arrShippingDetails.get(11);
						String sShipCountry = (String) arrShippingDetails.get(12);
						String sShipZip = (String) arrShippingDetails.get(13);
 
						parameters.put("STo_Addr1", sShipAddr1);
						parameters.put("STo_Addr2", sShipAddr2);
						parameters.put("STo_Addr3", sShipStreet);
						parameters.put("STo_City", sShipCity);
						if (sShipState.equals("")) {
							parameters.put("STo_Country", sShipCountry);
						}else {
							parameters.put("STo_Country", sShipState + "\n" + sShipCountry);
						}
						System.out.println("STo_Country : " + parameters.get("STo_Country"));
						parameters.put("STo_ZIP", sShipZip);
						if (sshipTelno.length() > 0) {
							parameters.put("STo_Telno", "Tel: " + sshipTelno);
						} else {
							parameters.put("STo_Telno", sshipTelno);
						}
						if (sShipContactName.length() > 0) {
							parameters.put("STo_AttentionTo", "Attn: " + sShipContactName);
						} else {
							parameters.put("STo_AttentionTo", sShipContactName);
						}
						if (sShipPhone.length() > 0) {
							parameters.put("STo_Phone", "HP: " + sShipPhone);
						} else {
							parameters.put("STo_Phone", sShipPhone);
						}
					} else {
						parameters.put("shipToId", "");
					}
					
					query = "currencyid,isnull(consignment_Gst,0) as outbound_Gst,isnull(orderdiscount,0) orderdiscount,FROMWAREHOUSE,TOWAREHOUSE,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_tohdr.PROJECTID),'') PROJECT_NAME,"
							+ "isnull(CRBY,'') as CRBY,isnull(shippingcost,0) shippingcost," + "isnull(incoterms,'') incoterms";
					ArrayList arraydohdr = new ArrayList();
					arraydohdr = _TOUtil.getToHdrDetails(query, htCond);
					Map dataMap = (Map) arraydohdr.get(0);
//					String gstValue = dataMap.get("outbound_Gst").toString();
					String DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					String DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					String DATAINCOTERMS = dataMap.get("incoterms").toString();
					String PROJECT_NAME = dataMap.get("PROJECT_NAME").toString();
					String FROMWAREHOUSE = dataMap.get("FROMWAREHOUSE").toString();
					String TOWAREHOUSE = dataMap.get("TOWAREHOUSE").toString();
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
					String orderRemarks3 = (String) arrCust.get(21);
					String sDeliveryDate = (String) arrCust.get(22);
					String sEmpno = (String) arrCust.get(23);
					String sState = (String) arrCust.get(24);

					String orderType = new ToHdrDAO().getOrderTypeForTO(PLANT, TONO);

					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("signaturePath", signaturePath);
					// Customer Details
					parameters.put("OrderNo", TONO);
					parameters.put("company", PLANT);
					parameters.put("To_CompanyName", sCustName);
					parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
					if (sState.equals("")) {
						parameters.put("To_Country", sCountry);
					}else {
						parameters.put("To_Country", sState + "\n" + sCountry);
					}
//					parameters.put("To_Country", sState + ", " + sCountry);
					parameters.put("To_ZIPCode", sZip);
					parameters.put("To_AttentionTo", sContactName);
					parameters.put("To_CCTO", "");
					parameters.put("To_TelNo", sTelNo);
					parameters.put("To_Fax", sFax);
					parameters.put("To_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					// Company Details
					parameters.put("fromAddress_CompanyName", CNAME);
//					parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//					parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//					if(CSTATE.equals("")) {
//					parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//					}else {
//						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//					}

					if(CADD1.equals("")) {
						parameters.put("fromAddress_BlockAddress", CADD2);
					}else {
						parameters.put("fromAddress_BlockAddress", CADD1 + ", " + CADD2);
					}
					if(CADD3.equals("")) {
						parameters.put("fromAddress_RoadAddress", CADD4);
					}else {
						parameters.put("fromAddress_RoadAddress", CADD3 + "," + CADD4);
					}
					if(CSTATE.equals("")) {
						parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
					}else {
						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
					}
					
					parameters.put("fromAddress_ZIPCode", CZIP);
					parameters.put("fromAddress_TpNo", CTEL);
					parameters.put("fromAddress_FaxNo", CFAX);
					parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
					parameters.put("fromAddress_Website", CWEBSITE);
					parameters.put("fromAddress_ContactPersonMobile", CHPNO);
					parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
					parameters.put("fromAddress_RCBNO", CRCBNO);
					parameters.put("currentTime", _TOUtil.getOrderDateOnly(PLANT, TONO));
					parameters.put("refNo", _TOUtil.getJobNum(PLANT, TONO));
					parameters.put("orderType", orderType);
					parameters.put("InvoiceTerms", "");
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";

					// report template parameter added on June 22 2011 By Deen
					
					Map ma = _TOUtil.getReceiptHdrDetails(PLANT,"Upon Creation");
					Orientation = (String) ma.get("PrintOrientation");
					if (orderType.equals("CONSIGNMENT ORDER")) {
						orderDesc = (String) ma.get("HDR1");
					} else {
						orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
					}
					if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
						parameters.put("OrderHeader", orderDesc);
					else
						parameters.put("OrderHeader", (String) ma.get("HDR1"));
					// parameters.put("OrderHeader", (String) ma.get("HDR1"));
					parameters.put("ToHeader", (String) ma.get("HDR2"));
					parameters.put("FromHeader", (String) ma.get("HDR3"));
					parameters.put("Date", (String) ma.get("DATE"));
					parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
					parameters.put("RefNo", (String) ma.get("REFNO"));
					parameters.put("SoNo", (String) ma.get("SONO"));
					parameters.put("Item", (String) ma.get("ITEM"));
					parameters.put("Description", (String) ma.get("DESCRIPTION"));
					parameters.put("DISPLAYSIGNATURE", (String) ma.get("DISPLAYSIGNATURE"));
					
					parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
					parameters.put("UOM", (String) ma.get("UOM"));
					parameters.put("Footer1", (String) ma.get("F1"));
					parameters.put("Footer2", (String) ma.get("F2"));
					parameters.put("Footer3", (String) ma.get("F3"));
					parameters.put("Footer4", (String) ma.get("F4"));
					parameters.put("Footer5", (String) ma.get("F5"));
					parameters.put("Footer6", (String) ma.get("F6"));
					parameters.put("Footer7", (String) ma.get("F7"));
					parameters.put("Footer8", (String) ma.get("F8"));
					parameters.put("Footer9", (String) ma.get("F9"));
					parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
					parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
					parameters.put("IssueDateRange", sCondition);
					parameters.put("RCBNO", (String) ma.get("RCBNO"));
					parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
					parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
					parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
					parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
					parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
					parameters.put("HSCODE", (String) ma.get("HSCODE"));
					parameters.put("COO", (String) ma.get("COO"));
					parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
					parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
					parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
					parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
					parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
					parameters.put("PreByUser", dataMap.get("CRBY").toString());
					parameters.put("PROJECTNAME", PROJECT_NAME);
					parameters.put("PROJECT",  ma.get("PROJECT"));
					parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
					parameters.put("FROMWAREHOUSE", FROMWAREHOUSE);
					parameters.put("TOWAREHOUSE", TOWAREHOUSE);
					/*if (ma.get("PCUSREMARKS").equals("1")) {
						parameters.put("SupRemarks", sRemarks);
					} else {*/
						parameters.put("SupRemarks", "");
					//}					

					if (orderRemarks.length() > 0)
						orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
					if (orderRemarks3.length() > 0)
						orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("orderRemarks3", orderRemarks3);
//					if (sDeliveryDate.length() > 0)
//						sDeliveryDate = sDeliveryDate;
					parameters.put("DeliveryDt", sDeliveryDate);
					parameters.put("DeliveryDate", (String) ma.get("DELIVERYDATE"));
					if (ma.get("PRINTEMPLOYEE").equals("1")) {
						parameters.put("Employee", (String) ma.get("EMPLOYEE"));
						String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
						parameters.put("EmployeeName", empname);
					} else {
						parameters.put("Employee", "");
						parameters.put("EmployeeName", "");
					}

					if (sShipCustName.length() > 0) {
						parameters.put("STo_CustName", (String) ma.get("SHIPTO") + " : " + "\n" + sShipCustName);
					} else {
						parameters.put("STo_CustName", sShipCustName);
					}

					parameters.put("STo_CustName", sShipCustName);
					parameters.put("STo", (String) ma.get("SHIPTO"));
					if (!checkImageFile.exists()) {
						imagePath2 = imagePath1;
						imagePath = "";
					} else if (!checkImageFile1.exists()) {
						imagePath2 = imagePath;
						imagePath1 = "";
					} else {
						imagePath2 = "";
					}
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					
					//imti start seal,sign condition in printoutconfig
					String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
					String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
					if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
		                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
						signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					parameters.put("sealPath", sealPath);
					parameters.put("signPath", signPath);
					
					parameters.put("imagePath2", imagePath2);
//					if (Orientation.equals("Portrait")) {
//						jasperPath += "Portrait";
//					}
					if (action.equalsIgnoreCase("rptTOWITHOUTBATCH"))
						parameters.put("printwithbatch", "no");
					else
						parameters.put("printwithbatch", "yes");
					parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
					parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
					parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
					parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
					parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
					parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
					parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
					parameters.put("lblOrderDiscount",
							(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ")");
					Hashtable curHash = new Hashtable();
					curHash.put(IDBConstants.PLANT, PLANT);
					curHash.put(IDBConstants.CURRENCYID, dataMap.get("currencyid"));
					CurrencyUtil currUtil = new CurrencyUtil();
					String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
					parameters.put("lblShippingCost",
							(String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ")");
					double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
					double doubledatashippingcost = new Double(DATASHIPPINGCOST);
					parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
					parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
					parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
					
					query = "item,itemdesc,unitprice";
					ToDetDAO todetdao = new ToDetDAO();						
					ArrayList arraydodet = todetdao.selectToDet(query, htCond);
					if(arraydodet.size() > 3)
					{ }
					else
						parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
					long start = System.currentTimeMillis();
					System.out.println("**************" + " Start Up Time : " + start + "**********");
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");

					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
				}
			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			// exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
			// "D:/jasper/MultipleDO.pdf");
			exporter.exportReport();
			byte[] bytes = byteArrayOutputStream.toByteArray();
			
			response.addHeader("Content-disposition", "inline;filename=MultipleTO.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	private void viewToReportWithPrice(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		Connection con = null;
		try {

			TOUtil _TOUtil = new TOUtil();
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			cUtil.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "",SEALNAME="",SIGNNAME="",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "",CWEBSITE = "";
			  String TONO = strUtils.fString(request.getParameter("TONO"));
			String PLANT = (String) session.getAttribute("PLANT");
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			// ********To get from & to date issue date range*********************
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String dtCondStr = "  and ISNULL(a.RECVDATE,'')<>'' AND CAST((SUBSTRING(a.RECVDATE, 7, 4) + '-' + SUBSTRING(a.RECVDATE, 4, 2) + '-' + SUBSTRING(a.RECVDATE, 1, 2)) AS date)";
			String sCondition = "",fdate = "", tdate = "", signaturePath = "";//sConditioninv = "", 
			
			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			if (fdate.length() > 0) {
				sCondition = sCondition + dtCondStr + " >= '" + fdate + "'  ";
				
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" + tdate + "'  ";
					
				}
			} else {
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + "  <= '" + tdate + "'  ";
					
				}
			}

			// ********To get from & to date receive date range*********************

			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			SEALNAME=StrUtils.fString((String)maps.get("SEALNAME"));
            SIGNNAME=StrUtils.fString((String)maps.get("SIGNATURENAME"));
            String sealPath = "",signPath="";
            //imti get seal name from plantmst
            if(SEALNAME.equalsIgnoreCase("")){
            	sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
            }else {
            	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
            }
            if(SIGNNAME.equalsIgnoreCase("")){
            	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
            }else {
               	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
               	if (!new File(signPath).exists()) {
    				signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    			}
            }
            //imti end
			String imagePath2;//, Orientation = "", PRINTDELIVERYNOTE = "", PRINTPACKINGLIST = "";

			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}

			con = DbBean.getConnection();

			String jasperPath = DbBean.JASPER_INPUT + "/" + "printTONOWITHPRICE";
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			List jasperPrintList = new ArrayList();
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);

				CNAME = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CSTATE = (String) map.get("STATE");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
				CRCBNO = (String) map.get("RCBNO");
				companyregno = (String) map.get("companyregnumber");//imtiuen
				CWEBSITE = (String) map.get("WEBSITE");

			}
			
				String query = " ";
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("TONO", TONO);
				
				for(int j = 0; j < 1; j ++) {
					String DATAORDERDISCOUNT="0",DATASHIPPINGCOST="0",
							ORDERDISCOUNTTYPE = "",AdjustmentAmount="",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",
							ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",ISTAXINCLUSIVE="",PROJECT_NAME="",sales_location="",empno="";
							int TAXID=0;
					
					signaturePath = DbBean.COMPANY_SIGN_PATH + "/" + PLANT.toLowerCase() + "/" + "sign" + TONO + ".bmp";
					File checkSignatureFile = new File(signaturePath);
					if (!checkSignatureFile.exists()) {
						signaturePath = DbBean.COMPANY_LOGO_PATH + "/" + "NoLogo.jpg";
					}
					MasterUtil _masterUtil = new MasterUtil();
					ArrayList arrCust = cUtil.getCustomerDetailsForTO(TONO, PLANT);
					if (arrCust.size() > 0) {
						String sCustCode = (String) arrCust.get(0);
						String sCustName = (String) arrCust.get(1);
						String sAddr1 = (String) arrCust.get(2);
						String sAddr2 = (String) arrCust.get(3);
						String sAddr3 = (String) arrCust.get(4);
						String sCountry = (String) arrCust.get(5);
						String sZip = (String) arrCust.get(6);
//						String sCons = (String) arrCust.get(7);
//						String sCustNameL = (String) arrCust.get(8);
						String sContactName = (String) arrCust.get(9);
//						String sDesgination = (String) arrCust.get(10);
						String sTelNo = (String) arrCust.get(11);
//						String sHpNo = (String) arrCust.get(12);
						String sFax = (String) arrCust.get(13);
						String sEmail = (String) arrCust.get(14);
//						String sRemarks = (String) arrCust.get(15);
						String sAddr4 = (String) arrCust.get(16);

						String SHIPPINGID = (String) arrCust.get(25);
						String sRcbno = (String) arrCust.get(26);
						String suenno = (String) arrCust.get(27);//imtiuen
						ArrayList arrShippingDetails = _masterUtil.getTransferShippingDetails(TONO, sCustCode, PLANT);
						Map parameters = new HashMap();
						if (request.getParameter("with_batch") != null
								&& "yes".equals(request.getParameter("with_batch"))) {
							parameters.put("PrintBatch", "yes");
						}
						String sShipCustName = "";
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", sCustCode);
							sShipCustName = (String) arrShippingDetails.get(1);
							String sShipContactName = (String) arrShippingDetails.get(2);
							String sshipTelno = (String) arrShippingDetails.get(3);
							String sShipPhone = (String) arrShippingDetails.get(4);
//							String sShipFax = (String) arrShippingDetails.get(5);
//							String sShipEmail = (String) arrShippingDetails.get(6);
							String sShipAddr1 = (String) arrShippingDetails.get(7);
							String sShipAddr2 = (String) arrShippingDetails.get(8);
							String sShipStreet = (String) arrShippingDetails.get(9);
							String sShipCity = (String) arrShippingDetails.get(10);
							String sShipState = (String) arrShippingDetails.get(11);
							String sShipCountry = (String) arrShippingDetails.get(12);
							String sShipZip = (String) arrShippingDetails.get(13);
							
							parameters.put("STo_Addr1", sShipAddr1);
							parameters.put("STo_Addr2", sShipAddr2);
							parameters.put("STo_Addr3", sShipStreet);
							parameters.put("STo_City", sShipCity);
							if (sShipState.equals("")) {
								parameters.put("STo_Country", sShipCountry);
							}else {
								parameters.put("STo_Country", sShipState + "\n" + sShipCountry);
							}
//							parameters.put("STo_Country", sShipState + "," + sShipCountry);
							System.out.println("STo_Country : " + parameters.get("STo_Country"));
							parameters.put("STo_ZIP", sShipZip);
							if (sshipTelno.length() > 0) {
								parameters.put("STo_Telno", "Tel: " + sshipTelno);
							} else {
								parameters.put("STo_Telno", sshipTelno);
							}
							if (sShipContactName.length() > 0) {
								parameters.put("STo_AttentionTo", "Attn: " + sShipContactName);
							} else {
								parameters.put("STo_AttentionTo", sShipContactName);
							}
							if (sShipPhone.length() > 0) {
								parameters.put("STo_Phone", "HP: " + sShipPhone);
							} else {
								parameters.put("STo_Phone", sShipPhone);
							}
						} else {
							parameters.put("shipToId", "");
						}
						query = "currencyid,isnull(consignment_Gst,0) as outbound_Gst,FROMWAREHOUSE,TOWAREHOUSE,"
								+ "isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') as PAYMENTTYPE,isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost," + "isnull(incoterms,'') incoterms,"
								+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(TAXID,0) TAXID,ISNULL(EMPNO,'') EMPNO,"
								+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_tohdr.TAXID),'') TAX_TYPE,"
								+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_tohdr.PROJECTID),'') PROJECT_NAME,"
								+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_tohdr.sales_location),'') sales_location,"
								+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
								+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
								+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
								+ "ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,STATUS";
						ArrayList arraydohdr = new ArrayList();
						arraydohdr = _TOUtil.getToHdrDetails(query, htCond);
						Map dataMap = (Map) arraydohdr.get(0);
						String gstValue = dataMap.get("outbound_Gst").toString();						
						
							DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
							DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
							//new changes - azees 12/2020
							ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
							DISCOUNT_TYPE=dataMap.get("DISCOUNT_TYPE").toString();
							ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
							ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
							ISORDERDISCOUNTTAX=dataMap.get("ISORDERDISCOUNTTAX").toString();
							DISCOUNT=dataMap.get("DISCOUNT").toString();
							AdjustmentAmount=dataMap.get("ADJUSTMENT").toString();
							ISTAXINCLUSIVE=dataMap.get("ISTAXINCLUSIVE").toString();
//							String POSTATUS = (String) dataMap.get("STATUS").toString();
							
							PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
							sales_location=dataMap.get("sales_location").toString();
							TAX_TYPE = dataMap.get("TAX_TYPE").toString();
							TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
							empno=dataMap.get("EMPNO").toString();
							String FROMWAREHOUSE = dataMap.get("FROMWAREHOUSE").toString();
							String TOWAREHOUSE = dataMap.get("TOWAREHOUSE").toString();
							
							if (action.equalsIgnoreCase("rptTOWITHOUTBATCHWITHPRICE"))
								parameters.put("printwithbatch", "0");
							else
								parameters.put("printwithbatch", "1");
						
						double doubledatabilldiscount = new Double(DISCOUNT);
						parameters.put("DO_DISCOUNT", doubledatabilldiscount);
						parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
						parameters.put("DO_DISCOUNT_TYPE", DISCOUNT_TYPE);
						parameters.put("fromAddress_Website", CWEBSITE);
						parameters.put("AdjustmentAmount", AdjustmentAmount);
						parameters.put("ISDISCOUNTTAX", ISDISCOUNTTAX);
						parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
						parameters.put("ISORDERDISCOUNTTAX", ISORDERDISCOUNTTAX);
						parameters.put("PROJECTNAME", PROJECT_NAME);
						parameters.put("FROMWAREHOUSE", FROMWAREHOUSE);
						parameters.put("TOWAREHOUSE", TOWAREHOUSE);
						if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
							ISSHIPPINGTAX="Tax Exclusive";
						else
							ISSHIPPINGTAX="Tax Inclusive";
						if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
							ISDISCOUNTTAX="Tax Exclusive";
						else
							ISDISCOUNTTAX="Tax Inclusive";
						if(ISORDERDISCOUNTTAX.equalsIgnoreCase("0"))
							ISORDERDISCOUNTTAX="Tax Exclusive";
						else
							ISORDERDISCOUNTTAX="Tax Inclusive";
						if(DISCOUNT_TYPE.equalsIgnoreCase("%"))
							parameters.put("lblDiscountDO", "Discount ("+ DISCOUNT + "%" +")"+" ("+ISDISCOUNTTAX+")");
						else
							parameters.put("lblDiscountDO", "Discount ("+DISCOUNT_TYPE+")"+" ("+ISDISCOUNTTAX+")");
						 
						FinCountryTaxType fintaxtype = new FinCountryTaxType();
						Short ptaxiszero=0,ptaxisshow=0;
						
						if(TAXID > 0){
							FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
							fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(TAXID);
							ptaxiszero=fintaxtype.getISZERO();
							ptaxisshow=fintaxtype.getSHOWTAX();
						}
						if(ptaxiszero == 1 && ptaxisshow == 0){							
							TAX_TYPE = "";
							gstValue="0.0";
						} else if(ptaxiszero == 0 && ptaxisshow == 0){
							TAX_TYPE = "";
							gstValue="0.0";
						} else if(ptaxiszero == 0 && ptaxisshow == 1){						
							if(sales_location.equalsIgnoreCase(""))
								TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();
							else
								TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+gstValue+"%]").trim();
						} else {							
							
							if(sales_location.equalsIgnoreCase(""))
								TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
							else
								TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
							gstValue="0.0";//Author: Azees  Create date: August 05,2021  Description: zero tax issue
						}
		  				parameters.put("TAX_TYPE", TAX_TYPE);
						
						
						String DATAINCOTERMS = dataMap.get("incoterms").toString();
						String orderRemarks = (String) arrCust.get(18);
//						String sPayTerms = (String) arrCust.get(19);
						String orderRemarks3 = (String) arrCust.get(21);
						String sDeliveryDate = (String) arrCust.get(22);
//						String sEmpno = (String) arrCust.get(23);
						String sState = (String) arrCust.get(24);
						String orderType = new ToHdrDAO().getOrderTypeForTO(PLANT, TONO);
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);

						parameters.put("signaturePath", signaturePath);
						// Customer Details
						parameters.put("OrderNo", TONO);
						parameters.put("company", PLANT);
						parameters.put("taxInvoiceTo_CompanyName", sCustName);
						parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
						parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
						if (sState.equals("")) {
							parameters.put("taxInvoiceTo_Country", sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country", sState + "\n" + sCountry);
						}
//						parameters.put("taxInvoiceTo_Country", sState + ", " + sCountry);
						parameters.put("taxInvoiceTo_ZIPCode", sZip);
						parameters.put("taxInvoiceTo_AttentionTo", sContactName);
						parameters.put("taxInvoiceTo_CCTO", "");
						parameters.put("To_TelNo", sTelNo);
						parameters.put("To_Fax", sFax);
						parameters.put("To_Email", sEmail);
						parameters.put("sRCBNO", sRcbno);
						parameters.put("sUENNO", suenno);//imtiuen
						parameters.put("fromAddress_UENNO", companyregno);//imtiuen
						// Company Details
						parameters.put("fromAddress_CompanyName", CNAME);
						if(CADD1.equals("")) {
							parameters.put("fromAddress_BlockAddress", CADD2);
						}else {
							parameters.put("fromAddress_BlockAddress", CADD1 + ", " + CADD2);
						}
						if(CADD3.equals("")) {
							parameters.put("fromAddress_RoadAddress", CADD4);
						}else {
							parameters.put("fromAddress_RoadAddress", CADD3 + "," + CADD4);
						}
						if(CSTATE.equals("")) {
							parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
						}else {
							parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
						}
						parameters.put("fromAddress_ZIPCode", CZIP);
						parameters.put("fromAddress_TpNo", CTEL);
						parameters.put("fromAddress_FaxNo", CFAX);
						parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
						parameters.put("fromAddress_ContactPersonMobile", CHPNO);
						parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
						parameters.put("fromAddress_RCBNO", CRCBNO);
						
						parameters.put("currentTime", _TOUtil.getOrderDateOnly(PLANT, TONO));
						parameters.put("taxInvoiceNo", "");
						parameters.put("InvoiceTerms", "");
						parameters.put("refNo", _TOUtil.getJobNum(PLANT, TONO));
						arraydohdr = _TOUtil.getToHdrDetails (query, htCond);
						Hashtable curHash = new Hashtable();
						curHash.put(IDBConstants.PLANT, PLANT);
						curHash.put(IDBConstants.CURRENCYID, dataMap.get("currencyid"));
						String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
						OrderTypeDAO ODAO = new OrderTypeDAO();
						String orderDesc = "";

						Map ma = _TOUtil.getTOReceiptHdrDetails(PLANT);
						if (orderType.equals("CONSIGNMENT ORDER")) {
							orderDesc = (String) ma.get("HDR1");
						} else {
							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
						}
						if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
							parameters.put("OrderHeader", orderDesc);
						else
							parameters.put("OrderHeader", (String) ma.get("HDR1"));
						
						parameters.put("ToHeader", (String) ma.get("HDR2"));
						parameters.put("FromHeader", (String) ma.get("HDR3"));
						parameters.put("Date", (String) ma.get("DATE"));
						parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
						parameters.put("RefNo", (String) ma.get("REFNO"));
						parameters.put("Terms", (String) ma.get("TERMS"));
						parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						if (ma.get("PRINTCUSTERMS").equals("1")) {
							
							parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
						} else {
							parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
						}
						parameters.put("SoNo", (String) ma.get("SONO"));
						parameters.put("Item", (String) ma.get("ITEM"));
						parameters.put("Description", (String) ma.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
						parameters.put("UOM", (String) ma.get("UOM"));
						;
						parameters.put("Rate", (String) ma.get("RATE"));
						parameters.put("TaxAmount", (String) ma.get("TAXAMOUNT"));
						parameters.put("Amt", (String) ma.get("AMT"));
						parameters.put("SubTotal", "Sub Total" + " " + "(" + curDisplay + ")");
						String taxby =new PlantMstDAO().getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
//						if (taxby.equalsIgnoreCase("BYORDER")) {
//							parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
//						} else {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
//						}
						parameters.put("Total", (String) ma.get("TOTAL") + " " + "(" + curDisplay + ")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", "Roundoff Total With Tax" + " " + "(" + curDisplay + ")");
						
						parameters.put("Footer1", (String) ma.get("F1"));
						parameters.put("Footer2", (String) ma.get("F2"));
						parameters.put("Footer3", (String) ma.get("F3"));
						parameters.put("Footer4", (String) ma.get("F4"));
						parameters.put("Footer5", (String) ma.get("F5"));
						parameters.put("Footer6", (String) ma.get("F6"));
						parameters.put("Footer7", (String) ma.get("F7"));
						parameters.put("Footer8", (String) ma.get("F8"));
						parameters.put("Footer9", (String) ma.get("F9"));
						parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
						parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
						parameters.put("RCBNO", (String) ma.get("RCBNO"));
						parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
						parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
						parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
						parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
						parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
						parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
						
						parameters.put("HSCODE", (String) ma.get("HSCODE"));
						parameters.put("COO", (String) ma.get("COO"));
						parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
						parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));						
							parameters.put("IssueDateRange", sCondition);
						
							parameters.put("SupRemarks", "");
						
						parameters.put("curDisplay", curDisplay);

						double gst = new Double(gstValue).doubleValue() / 100;
						parameters.put("Gst", gst);
						parameters.put("orderType", orderType);

						if (orderRemarks.length() > 0)
							orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;

						parameters.put("orderRemarks", orderRemarks);
						parameters.put("orderRemarks3", orderRemarks3);
//						if (sDeliveryDate.length() > 0)
//							sDeliveryDate = sDeliveryDate;
						parameters.put("DeliveryDt", sDeliveryDate);
						parameters.put("DeliveryDate", (String) ma.get("DELIVERYDATE"));
						if (ma.get("PRINTEMPLOYEE").equals("1")) {
							parameters.put("Employee", (String) ma.get("EMPLOYEE"));
							String empname = new EmployeeDAO().getEmpname(PLANT, empno, "");
							parameters.put("EmployeeName", empname);
						} else {
							parameters.put("Employee", "");
							parameters.put("EmployeeName", "");
						}

						

						if (sShipCustName.length() > 0) {
							parameters.put("STo_CustName", (String) ma.get("SHIPTO") + " : " + "\n" + sShipCustName);

						} else {
							parameters.put("STo_CustName", sShipCustName);
						}
						parameters.put("STo_CustName", sShipCustName);
						parameters.put("STo", (String) ma.get("SHIPTO"));

						if (!checkImageFile.exists()) {
							imagePath2 = imagePath1;
							imagePath = "";
						} else if (!checkImageFile1.exists()) {
							imagePath2 = imagePath;
							imagePath1 = "";
						} else {
							imagePath2 = "";
						}
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
						String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						
						parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
						parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("CALCULATETAXWITHSHIPPINGCOST",  ma.get("CALCULATETAXWITHSHIPPINGCOST"));
						parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
						

						parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
						parameters.put("Seller", (String) ma.get("SELLER"));
						parameters.put("SellerSign", (String) ma.get("SELLERSIGNATURE"));
						parameters.put("Buyer", (String) ma.get("BUYER"));
						parameters.put("BuyerSign", (String) ma.get("BUYERSIGNATURE"));
						//new changes - azees 12/2020
						parameters.put("Adjustment",  ma.get("ADJUSTMENT"));	
						
						String productrates = ((String)ma.get("PRODUCTRATESARE"));
						if(productrates.equals("")) {
							
						}else {
							parameters.put("ISTAXINCLUSIVE", ISTAXINCLUSIVE);
						}
						parameters.put("PRODUCTRATESARE",  ma.get("PRODUCTRATESARE"));
						
						parameters.put("PROJECT",  ma.get("PROJECT"));
						parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
						if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
							parameters.put("lblOrderDiscount",
									(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISORDERDISCOUNTTAX + ")");
							else
								parameters.put("lblOrderDiscount",
										(String) ma.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISORDERDISCOUNTTAX + ")");
						parameters.put("lblShippingCost", (String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
						double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
						double doubledatashippingcost = new Double(DATASHIPPINGCOST);
						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
						parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						parameters.put("Discount", (String) ma.get("DISCOUNT"));
						parameters.put("NetRate", (String) ma.get("NETRATE"));
						
						query = "item,itemdesc,unitprice";
						ToDetDAO todetdao = new ToDetDAO();						
						ArrayList arraydodet = todetdao.selectToDet(query, htCond);
						if(arraydodet.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						
						
						long start = System.currentTimeMillis();
						System.out.println("**************" + " Start Up Time : " + start + "**********");
						System.out.println("jasperPath : " + jasperPath);
						JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");

						jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
						

					}
				}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			// exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
			// "D:/jasper/MultipleDO.pdf");
			exporter.exportReport();

			byte[] bytes = byteArrayOutputStream.toByteArray();

			//response.addHeader("Content-disposition", "attachment;filename=MultipleInvoice.pdf");
			response.addHeader("Content-disposition", "inline;filename=MultipleToWithPrice.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	//created by navas
    private HSSFWorkbook writeToExcelConsignmentWOOrderSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		StrUtils strUtils = new StrUtils();
		HTReportUtil movHisUtil = new HTReportUtil();
		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		try {

			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMNO = strUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = strUtils.fString(request.getParameter("DESC"));
			String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
			String CUSTNAME = strUtils.fString(request.getParameter("CUSTOMER"));
			String CUSTOMERID = strUtils.fString(request.getParameter("CUSTOMERID"));
			String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
			String FROMWAREHOUSE = strUtils.fString(request.getParameter("FROMWAREHOUSE"));
			String TOWAREHOUSE = strUtils.fString(request.getParameter("TOWAREHOUSE"));
			String JOBNO = strUtils.fString(request.getParameter("JOBNO"));
			String PICKSTATUS = strUtils.fString(request.getParameter("PICKSTATUS"));
			String ISSUESTATUS = strUtils.fString(request.getParameter("ISSUESTATUS"));
			String ORDERTYPE = strUtils.fString(request.getParameter("ORDERTYPE"));
			String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
			String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
			String statusID = strUtils.fString(request.getParameter("STATUS_ID"));
			String type = strUtils.fString(request.getParameter("DIRTYPE"));
			String SORT = strUtils.fString(request.getParameter("SORT"));
			String EMPNO = strUtils.fString(request.getParameter("EMP_NAME"));
			String CUSTOMERTYPE = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
			String CURRENCYID = strUtils.fString(request.getParameter("CURRENCYID"));
			String CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
			String INVOICENO = strUtils.fString(request.getParameter("INVOICENO"));
			String UOM = strUtils.fString(request.getParameter("UOM"));
			//imti start
			if(PICKSTATUS.equalsIgnoreCase("OPEN"))
				PICKSTATUS="N";
			else if(PICKSTATUS.equalsIgnoreCase("PARTIALLY PICKED"))
				PICKSTATUS="O";
			else if(PICKSTATUS.equalsIgnoreCase("PICKED"))
				PICKSTATUS="C";
			//imti end
			//imti start
			if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
				ISSUESTATUS="N";
			else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
				ISSUESTATUS="O";
			else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
				ISSUESTATUS="C";
			//imti end
			String reportType ="";
			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;
			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			Hashtable ht = new Hashtable();
			/* taxby = _PlantMstDAO.getTaxBy(PLANT); */
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			if (type.equals("CONSIGNMENT")) {

				if (strUtils.fString(JOBNO).length() > 0)
					ht.put("A.JOBNUM", JOBNO);
				if (strUtils.fString(ITEMNO).length() > 0)
					ht.put("B.ITEM", ITEMNO);
				if (strUtils.fString(ORDERNO).length() > 0)
					ht.put("B.TONO", ORDERNO);
				//created by navas
				if (strUtils.fString(FROMWAREHOUSE).length() > 0)
					ht.put("A.FROMWAREHOUSE", FROMWAREHOUSE);
				if (strUtils.fString(TOWAREHOUSE).length() > 0)
					ht.put("A.TOWAREHOUSE", TOWAREHOUSE);
				//end by navas
				if (strUtils.fString(CUSTOMERID).length() > 0)
					ht.put("A.CUSTCODE", CUSTOMERID);
				if (strUtils.fString(ORDERTYPE).length() > 0)
					ht.put("A.ORDERTYPE", ORDERTYPE);
				if (strUtils.fString(ISSUESTATUS).length() > 0) {
					if(ISSUESTATUS.equalsIgnoreCase("DRAFT"))
		        	{
		        	ht.put("ORDER_STATUS","Draft");
		        	ht.put("B.LNSTAT","N");
		        	}
		        	else
		        	{
		        		ht.put("B.LNSTAT",ISSUESTATUS);

		        	if(ISSUESTATUS.equalsIgnoreCase("N"))
		        	ht.put("ORDER_STATUS","OPEN");
		        	}
		        	}   
				if (strUtils.fString(PICKSTATUS).length() > 0){
					if(PICKSTATUS.equalsIgnoreCase("DRAFT"))
				{
		        	ht.put("ORDER_STATUS","Draft");
		        	ht.put("B.PICKSTATUS","N");
		        	}
		        	else
		        	{
		        	ht.put("B.PICKSTATUS",PICKSTATUS);

		        	if(PICKSTATUS.equalsIgnoreCase("N"))
		        	ht.put("ORDER_STATUS","OPEN");
		        	}
		        	}
				if (strUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("C.ITEMTYPE", PRD_TYPE_ID);
				if (strUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
				if (strUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
				if (strUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("C.PRD_DEPT_ID", PRD_DEPT_ID);
				if (strUtils.fString(statusID).length() > 0)
					ht.put("A.STATUS_ID", statusID);
				if (strUtils.fString(EMPNO).length() > 0)
					ht.put("A.EMPNO", EMPNO);
				if (strUtils.fString(CUSTOMERTYPE).length() > 0)
					ht.put("CUSTTYPE", CUSTOMERTYPE);
				if (strUtils.fString(UOM).length() > 0)
					ht.put("SKTUOM", UOM);

				movQryList = movHisUtil.getConsignmentWorkOrderSummaryList(ht, fdate, tdate, type, PLANT, PRD_DESCRIP, CUSTNAME,UOM,
						"");

			} 

			Boolean workSheetCreated = true;
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthOutboundSummary(sheet, type);
				sheet = this.createHeaderOutboundSummary(sheet, styleHeader, type);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
				double orderPriceSubTot = 0, issPriceSubTot = 0, unitprice = 0, Price = 0, tax = 0, PricewTax = 0;
				float gstpercentage = 0;
				String strDiffQty = "", deliverydateandtime = "";
				DecimalFormat decformat = new DecimalFormat("#,##0.00");
				DecimalFormat decimalFormat = new DecimalFormat("#.#####");
				decimalFormat.setRoundingMode(RoundingMode.FLOOR);
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				String customerstatusid = "", fromwarehouse = "", towarehouse = "", customertypeid = "", customertypedesc = "";

				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;
					customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT, (String) lineArr.get("custname"));
					
					 
					  customertypeid = customerBeanDAO.getCustomerTypeId(PLANT, (String)
					  lineArr.get("custname")); if (customertypeid == null ||
					  customertypeid.equals("")) { customertypedesc = ""; 
					  }
					  else { customertypedesc= customerBeanDAO.getCustomerTypeDesc(PLANT, customertypeid); }
					 

					
					deliverydateandtime = (String) lineArr.get("deliverydate");
					//+ " "	+ (String) lineArr.get("deliverytime");
					
					String PricewTaxValue =  decimalFormat.format(PricewTax);
					String taxValue =  decimalFormat.format(tax);
					String PriceValue =  decimalFormat.format(Price);
					String unitPriceValue =  decimalFormat.format(unitprice);
					String gstpercentageValue = String.valueOf(gstpercentage);
					//String unitPriceValue = String.valueOf(unitprice);
					String qtyValue= (String) lineArr.get("qty");
					String qtyPickValue= (String) lineArr.get("qtypick");
					String qtyOrValue= (String) lineArr.get("qtyor");
					String qtyInvValue= (String) lineArr.get("qtyac");
					
					/*float PricewTaxVal ="".equals(PricewTaxValue) ? 0.0f :  Float.parseFloat(PricewTaxValue);*/
					/*float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);*/
					/*float PriceVal ="".equals(PriceValue) ? 0.0f :  Float.parseFloat(PriceValue);*/
					float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
					/*float unitPriceVal ="".equals(unitPriceValue) ? 0.0f :  Float.parseFloat(unitPriceValue);*/
					float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
					float qtyPickVal =qtyPickValue == null || "".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
					float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
					float qtyInvVal ="".equals(qtyInvValue) ? 0.0f :  Float.parseFloat(qtyInvValue);
					//imti start		 
		 			String lnstat= StrUtils.fString((String) lineArr.get("lnstat"));
		 			String ordstat= (String)lineArr.get("ORDER_STATUS");
					if(lnstat.equalsIgnoreCase("N"))
					{
						lnstat="OPEN";
						if(ordstat.equalsIgnoreCase("Draft"))
							lnstat="DRAFT";				
					}
		 		       else if(lnstat.equalsIgnoreCase("O"))
		 		    	   lnstat="PARTIALLY ISSUED";
		 		       else if(lnstat.equalsIgnoreCase("C"))
		 		    	   lnstat="ISSUED";
		 				//imti end
		 
		   	   		//imti start		 
		  			String pickstatus= StrUtils.fString((String) lineArr.get("pickstatus"));
		  			String ordstats= (String)lineArr.get("ORDER_STATUS");
					if(pickstatus.equalsIgnoreCase("N"))
					{
						pickstatus="OPEN";
						if(ordstats.equalsIgnoreCase("Draft"))
							pickstatus="DRAFT";				
					}
		  		       else if(pickstatus.equalsIgnoreCase("O"))
		  		    	 pickstatus="PARTIALLY PICKED";
		  		       else if(pickstatus.equalsIgnoreCase("C"))
		  		    	 pickstatus="PICKED";
		  //imti end
		   			 
					double unitPriceVal ="".equals(unitPriceValue) ? 0.0d :  Double.parseDouble(unitPriceValue);
					double PriceVal ="".equals(PriceValue) ? 0.0d :  Double.parseDouble(PriceValue);
					double taxVal ="".equals(taxValue) ? 0.0d :  Double.parseDouble(taxValue);
					double PricewTaxVal ="".equals(PricewTaxValue) ? 0.0d :  Double.parseDouble(PricewTaxValue);
					
		    		
					/*if(PricewTaxVal==0f){
		    			PricewTaxValue="0.000";
		    		}else{
		    			PricewTaxValue=PricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(taxVal==0f){
		    			taxValue="0.000";
		    		}else{
		    			taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(PriceVal==0f){
		    			PriceValue="0.00000";
		    		}else{
		    			PriceValue=PriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/if(gstpercentageVal==0f){
		    			gstpercentageValue="0.000";
		    		}else{
		    			gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}/*if(unitPriceVal==0f){
		    			unitPriceValue="0.00000";
		    		}else{
		    			unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/if(qtyVal==0f){
		    			qtyValue="0.000";
		    		}else{
		    			qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyPickVal==0f){
		    			qtyPickValue="0.000";
		    		}else{
		    			qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyOrVal==0f){
		    			qtyOrValue="0.000";
		    		}else{
		    			qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyInvVal==0f){
		    			qtyInvValue="0.000";
		    		}else{
		    			qtyInvValue=qtyInvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}
		    		
		    		unitPriceValue = StrUtils.addZeroes(unitPriceVal, numberOfDecimal);
		    		PriceValue = StrUtils.addZeroes(PriceVal, numberOfDecimal);
		    		taxValue = StrUtils.addZeroes(taxVal, numberOfDecimal);
		    		PricewTaxValue = StrUtils.addZeroes(PricewTaxVal, numberOfDecimal);
					
		    		qtyInvValue = StrUtils.addZeroes(qtyInvVal, "3");
		    		qtyOrValue = StrUtils.addZeroes(qtyOrVal, "3");
		    		qtyPickValue = StrUtils.addZeroes(qtyPickVal, "3");
		    		qtyValue = StrUtils.addZeroes(qtyVal, "3");
		    		
					HSSFRow row = sheet.createRow(index);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("tono"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ordertype"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("jobNum"))));
					cell.setCellStyle(dataStyle);
					

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("customertypedesc"))));
					cell.setCellStyle(dataStyle);
					//CREATED BY NAVAS
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("fromwarehouse"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("towarehouse"))));
					cell.setCellStyle(dataStyle);
					//END BY NAVAS
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks2"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
					cell.setCellStyle(dataStyle);

					
						cell = row.createCell(k++);
						cell.setCellValue(
								new HSSFRichTextString(StrUtils.fString((String) lineArr.get("DetailItemDesc"))));
						cell.setCellStyle(dataStyle);
				
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PRD_DEPT_ID"))));
						cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("prd_cls_id"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemtype"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("prd_brand_id"))));
					cell.setCellStyle(dataStyle);

					SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");
					

						cell = row.createCell(k++);
						Calendar c = Calendar.getInstance();
						c.setTime(_shortDateformatter.parse(StrUtils.fString((String) lineArr.get("CollectionDate"))));
						dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
						cell.setCellValue(c.getTime());
						cell.setCellStyle(dataStyleSpecial);

				

					
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) deliverydateandtime)));
						cell.setCellStyle(dataStyle);
				
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("UOM"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(qtyOrValue);
					cell.setCellStyle(dataStyle);

					
						cell = row.createCell(k++);
						cell.setCellValue(qtyPickValue);
						cell.setCellStyle(dataStyle);

						

					cell = row.createCell(k++);
					cell.setCellValue(qtyValue);
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(qtyInvValue);
					cell.setCellStyle(dataStyle);
				

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("empname"))));
					cell.setCellStyle(dataStyle);

					
					
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(pickstatus));
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(lnstat));
						cell.setCellStyle(dataStyle);

				
					
					cell = row.createCell(k++);
				    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("SALES_LOCATION"))));
				    cell.setCellStyle(dataStyle);
				    
				    cell = row.createCell(k++);
				    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("TAXTREATMENT"))));
				    cell.setCellStyle(dataStyle);
					
					    cell = row.createCell(k++);
					    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("users"))));
					    cell.setCellStyle(dataStyle);
					

					index++;
					if ((index - 2) % maxRowsPerSheet == 0) {
						index = 2;
						SheetId++;
						 sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthOutboundSummary(sheet, type);
						sheet = this.createHeaderOutboundSummary(sheet, styleHeader, type);
						sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);

					}

				}

			} else if (movQryList.size() < 1) {
				System.out.println("No Records Found To List");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return wb;
	}
	//end by navas
	
	//CREATED BY NAVAS FEB20
	
		private HSSFWorkbook writeToExcelConsignmentSummaryWithRemarks(HttpServletRequest request,
				HttpServletResponse response, HSSFWorkbook wb) {
			StrUtils strUtils = new StrUtils();
			HTReportUtil movHisUtil = new HTReportUtil();
			DateUtils _dateUtils = new DateUtils();
			ArrayList movQryList = new ArrayList();
			int maxRowsPerSheet = 65535;
			String fdate = "", tdate = "";
			int SheetId =1;
			try {

				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				String ITEMNO = strUtils.fString(request.getParameter("ITEM"));
				String PRD_DESCRIP = strUtils.fString(request.getParameter("DESC"));
				String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
				String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
				String CUSTNAME = strUtils.fString(request.getParameter("CUSTOMER"));
				String CUSTOMERID = strUtils.fString(request.getParameter("CUSTOMERID"));
				String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
				String JOBNO = strUtils.fString(request.getParameter("JOBNO"));
				String PICKSTATUS = strUtils.fString(request.getParameter("PICKSTATUS"));
				String ISSUESTATUS = strUtils.fString(request.getParameter("ISSUESTATUS"));
				String ORDERTYPE = strUtils.fString(request.getParameter("ORDERTYPE"));
				String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				String statusID = strUtils.fString(request.getParameter("STATUS_ID"));
				String type = strUtils.fString(request.getParameter("DIRTYPE"));
				String SORT = strUtils.fString(request.getParameter("SORT"));
				String EMPNO = strUtils.fString(request.getParameter("EMP_NAME"));
				String CUSTOMERTYPE = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
				String CURRENCYID = strUtils.fString(request.getParameter("CURRENCYID"));
				String CURRENCYDISPLAY = strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
				String UOM = strUtils.fString(request.getParameter("UOM"));
				String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
				String reportType ="";
				if (FROM_DATE == null)
					FROM_DATE = "";
				else
					FROM_DATE = FROM_DATE.trim();
				String curDate = DateUtils.getDate();
				if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
					FROM_DATE = curDate;
				if (FROM_DATE.length() > 5)
					fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
				if (TO_DATE == null)
					TO_DATE = "";
				else
					TO_DATE = TO_DATE.trim();
				if (TO_DATE.length() > 5)
					tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
				Hashtable ht = new Hashtable();

				if (strUtils.fString(JOBNO).length() > 0)
					ht.put("A.JOBNUM", JOBNO);
				if (strUtils.fString(ITEMNO).length() > 0)
					ht.put("B.ITEM", ITEMNO);
				if (strUtils.fString(ORDERNO).length() > 0)
					ht.put("B.TONO", ORDERNO);
				if (strUtils.fString(CUSTOMERID).length() > 0)
					ht.put("A.CUSTCODE", CUSTOMERID);
				if (strUtils.fString(ORDERTYPE).length() > 0)
					ht.put("A.ORDERTYPE", ORDERTYPE);
				if (strUtils.fString(ISSUESTATUS).length() > 0)
					ht.put("B.LNSTAT", ISSUESTATUS);
				if (strUtils.fString(PICKSTATUS).length() > 0)
					ht.put("B.PICKSTATUS", PICKSTATUS);
				if (strUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("C.ITEMTYPE", PRD_TYPE_ID);
				if (strUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
				if (strUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
				if (strUtils.fString(statusID).length() > 0)
					ht.put("A.STATUS_ID", statusID);
				if (strUtils.fString(EMPNO).length() > 0)
					ht.put("A.EMPNO", EMPNO);
				if (strUtils.fString(CUSTOMERTYPE).length() > 0)
					ht.put("CUSTTYPE", CUSTOMERTYPE);

				movQryList = movHisUtil.getWorkOrderSummaryList(ht, fdate, tdate, "CONSIGNMENTPRODUCTREMARKS", PLANT,
						PRD_DESCRIP, CUSTNAME, "",UOM,POSSEARCH);

				Boolean workSheetCreated = true;
				if (movQryList.size() > 0) {
					HSSFSheet sheet = null;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					HSSFCellStyle CompHeader = null;
					HSSFCellStyle dataStyleSpecial = null;
					dataStyle = createDataStyle(wb);
					dataStyleSpecial = createDataStyle(wb);
					CreationHelper createHelper = wb.getCreationHelper();
					 sheet = wb.createSheet("Sheet"+SheetId);
					styleHeader = createStyleHeader(wb);
					CompHeader = createCompStyleHeader(wb);
					sheet = this.createWidthOutboundSummaryWithRemarks(sheet, type);
					sheet = this.createHeaderOutboundSummaryWithRemarks(sheet, styleHeader, type);
					sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
					int index = 2;
					CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
					String customerstatusid = "", customerstatusdesc = "", customertypeid = "", customertypedesc = "";

					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;

						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("tono"))));
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("prdremarks"))));
						cell.setCellStyle(dataStyle);

						index++;
						if((index-2)%maxRowsPerSheet==0){
							 index = 2;
							 SheetId++;
							 sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthOutboundSummary(sheet, type);
							sheet = this.createHeaderOutboundSummary(sheet, styleHeader, type);
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);

						}

					}

				} else if (movQryList.size() < 1) {
					System.out.println("No Records Found To List");
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}

			return wb;
		}
		//END BY NAVAS

  //Start code by Bruhan for transfer order  export to excel on 16 sep 2013
	 private HSSFWorkbook writeToExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			ArrayList listQry = new ArrayList();
			try{
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				String TONO =  StrUtils.fString(request.getParameter("TONO"))
	                     .trim();
				//String CUSTNAME = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
				String DIRTYPE = StrUtils.fString(request.getParameter("DIRTYPE"));
				Hashtable htCond = new Hashtable();
				htCond.put("A.PLANT", plant);
				htCond.put("A.TONO", TONO);

				//String query = "dono,dolnno,item,isnull(itemdesc,'') itemdesc,isnull(unitmo,'') uom,isnull(unitprice,0) unitprice,isnull(qtyor,0) as qtyor";
				
				 //listQry = dOUtil.getDoDetDetails(query, htCond);
				 listQry = htutil.getOrderDetailsExportList(htCond,DIRTYPE,plant);
					
					 if (listQry.size() > 0) {
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							 sheet = wb.createSheet("Sheet1");
							 styleHeader = createStyleHeader(wb);
							 sheet = this.createWidth(sheet);
							 sheet = this.createHeader(sheet,styleHeader);
											
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								 Map lineArr = (Map) listQry.get(iCnt);	
								 int k = 0;
								 String collectiondate = StrUtils.fString((String)lineArr.get("collectiondate"));
								 if(collectiondate.length()>0){
								 collectiondate = collectiondate.substring(3,5)+"/"+collectiondate.substring(0,2)+"/"+collectiondate.substring(6) ;
								// DateFormat df = new SimpleDateFormat("MM/dd/YYYY");
								// collectiondate = df.format(collectiondate);
								 }
			
									    dataStyle = createDataStyle(wb);
									    HSSFRow row = sheet.createRow((short) iCnt+1);
									    
									    HSSFCell cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("tono"))));
										cell.setCellStyle(dataStyle);
										
									    cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("tolnno"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custcode"))));
										cell.setCellStyle(dataStyle);
											
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("fromloc"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("toloc"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobnum"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(collectiondate));
										cell.setCellStyle(dataStyle);
												
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("collectiontime"))));
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("remarks"))));
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										//cell.setCellType(cell.CELL_TYPE_NUMERIC);
										cell.setCellValue(Numbers.toMillionFormat(Double.parseDouble(StrUtils.fString((String)lineArr.get("qtyor"))),"2"));
										cell.setCellStyle(dataStyle);
																				
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("unitmo"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
										cell.setCellStyle(dataStyle);
										
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
	 
	 private HSSFSheet createHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
			
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Consignment Order No"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Consignment Order Line No"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Customer ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("From Loc"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("To Loc"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Ref No"));
			cell.setCellStyle(styleHeader);
				
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Order Date"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Time"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Remarks"));
			cell.setCellStyle(styleHeader);
			
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Quantity"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Uom"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Description"));
			cell.setCellStyle(styleHeader);
											
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		private HSSFSheet createWidth(HSSFSheet sheet){
			
			try{
				sheet.setColumnWidth((short)0 ,(short)5000);
				sheet.setColumnWidth((short)1 ,(short)5000);
				sheet.setColumnWidth((short)2 ,(short)4000);
				sheet.setColumnWidth((short)3 ,(short)4000);
				sheet.setColumnWidth((short)4 ,(short)4000);
				sheet.setColumnWidth((short)5 ,(short)5000);
				sheet.setColumnWidth((short)6 ,(short)4000);
				sheet.setColumnWidth((short)7 ,(short)3000);
				sheet.setColumnWidth((short)8 ,(short)10000);
				sheet.setColumnWidth((short)9 ,(short)10000);
				sheet.setColumnWidth((short)10 ,(short)3000);
				sheet.setColumnWidth((short)11 ,(short)3000);
				sheet.setColumnWidth((short)12 ,(short)12000);
				
				
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
	    
	//End code by Bruhan for transfer order  export to excel on 16 sep 2013    
  
	/*  Created By Bruhan On Sep 23 2013,Description:transfer order pick/issue by prod
	    ************Modification History*********************************
		   Oct 15 2014 Bruhan, Description: To include Transaction date
	*/	private String OutGoingIssueBulkDatabyProd(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException,
				Exception {
			
			boolean flag = false;
			StrUtils StrUtils = new StrUtils();
			Map receiveMaterial_HM = null;

			String PLANT = "", TONO = "", FROMLOC = "",TOLOC="",tolno = "",item = "",ITEM_DESCRIPTION = "",QTYOR = "";
			String ITEM_BATCH = "NOBATCH",PICKED_QTY = "0",PICKING_QTY = "",CUST_NAME = "",LOGIN_USER = "";
			String REMARKS = "",SHIPNO="",issuingQty="",ITEM_QTY="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
			double pickingQty = 0;
			Boolean allChecked = false,fullIssue = false;
			//UserTransaction ut = null;
			Map checkedDOS = new HashMap();
			String sepratedtoken1 = "";
			Map mp = null;
			mp = new HashMap();
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			try {
				String sepratedtoken = "";
				String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
				StringTokenizer parser = new StringTokenizer(totalString, "=");
				HttpSession session = request.getSession();
				PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
		        item = StrUtils.fString(request.getParameter("ITEM"));
		        ITEM_DESCRIPTION =  StrUtils.fString(request.getParameter("DESC"));
		        String[] chkdToNo  = request.getParameterValues("chkdToNo");
				//FROMLOC = StrUtils.fString(request.getParameter("LOC_0"));
				CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
				LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
				REMARKS = StrUtils.fString(request.getParameter("REF"));
				TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
				if (TRANSACTIONDATE.length()>5)
					  strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
				      strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
				       
	        	if (chkdToNo != null)    {     
	    				for (int i = 0; i < chkdToNo.length; i++)       { 
	    				String	data = chkdToNo[i];
	    				String[] chkdata = data.split(",");
	    				String tno=chkdata[0]+"_"+chkdata[1];
	    			issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+tno));
	    			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+tno));
	    			if (ITEM_BATCH.length() == 0) {
	    					ITEM_BATCH = "NOBATCH";
	    				}
	    			
	    					checkedDOS.put(tno, issuingQty+":"+ITEM_BATCH);
	    				}
	    				session.setAttribute("checkedDOS", checkedDOS);
	                }
	    			
	            
				while (parser.hasMoreTokens())

				{
					int count = 1;
					sepratedtoken = parser.nextToken();

					System.out.println("sepratedtoken ::" + sepratedtoken);
					StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
							",");

					while (parser1.hasMoreTokens())
					{
						sepratedtoken1 = parser1.nextToken();

						mp.put("data" + count, sepratedtoken1);

						count++;

			}

				TONO = StrUtils.fString((String) mp.get("data1"));
				tolno = StrUtils.fString((String) mp.get("data2"));
				QTYOR = StrUtils.fString((String) mp.get("data3"));
				PICKED_QTY = StrUtils.fString((String) mp.get("data4"));
				CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data5")));
				FROMLOC = StrUtils.fString((String) mp.get("data6"));
				TOLOC = StrUtils.fString((String) mp.get("data7"));
				if( request.getParameter("select")!=null){
					allChecked = true;
				}
				if(request.getParameter("fullIssue")!=null){
					fullIssue = true;
				}
			
				 
				ArrayList TODetails = null;

	    		Hashtable htToDet = new Hashtable();
	    		String queryToDet = "item,itemDesc,QTYOR";
	    				
						issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+TONO+"_"+tolno));	
						pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
						ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+TONO+"_"+tolno));
						//start code added by Bruhan for outbound balance issue on 25 july 2013
						SHIPNO = StrUtils.fString(request.getParameter("SHIPNO_"+TONO+"_"+tolno));
						//end code added by Bruhan for outbound balance issue on 25 july 2013
						if (ITEM_BATCH.length() == 0) {
	    					ITEM_BATCH = "NOBATCH";
	    				}
												
						htToDet.put(IConstants.TODET_TONUM, TONO);
						htToDet.put(IConstants.PLANT, PLANT);
						htToDet.put(IConstants.TODET_TOLNNO, tolno);
						TODetails = _TOUtil.getToDetDetails(queryToDet, htToDet);
						if (TODetails.size() > 0) {	

								Map map1 = (Map) TODetails.get(0);
								item = (String) map1.get("item");
								ITEM_DESCRIPTION = (String) map1.get("itemDesc");
								QTYOR = (String) map1.get("QTYOR");
						}
						
						double orderqty = Double.parseDouble(((String) QTYOR.trim()));
						double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
						pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
						pickingQty = strUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
						PICKING_QTY = String.valueOf(pickingQty);
						PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
						//SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
			
						// check for item in location
                                     UserLocUtil uslocUtil = new UserLocUtil();
			                        uslocUtil.setmLogger(mLogger);
			                        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,FROMLOC);
			                        if(!isvalidlocforUser){
			                            throw new Exception(" Loc : "+FROMLOC+" is not User Assigned Location");
			                        }
			                        
			                        
					
				
					// 1. INV against
				List listQry = _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,item, FROMLOC, ITEM_BATCH);
					double invqty = 0;
					if (listQry.size() > 0) {
						for (int j = 0; j < listQry.size(); j++) {
							Map m = (Map) listQry.get(j);
							ITEM_QTY = (String) m.get("qty");
							invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
							if(invqty < pickingQty){
								throw new Exception(
										"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +ITEM_BATCH+ "  scanned at the location  "+FROMLOC);
								}
						}
					} else {
						
						throw new Exception(
								"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +ITEM_BATCH+ "  scanned at the location  "+FROMLOC);
					}
					
		receiveMaterial_HM = new HashMap();
		receiveMaterial_HM.put(IConstants.PLANT, PLANT);
		receiveMaterial_HM.put(IConstants.ITEM, item);
		receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
		receiveMaterial_HM.put(IConstants.TODET_TONUM, TONO);
		receiveMaterial_HM.put(IConstants.TODET_TOLNNO, tolno);
		receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
		receiveMaterial_HM.put(IConstants.FROMLOC, FROMLOC);
		receiveMaterial_HM.put(IConstants.TOLOC, TOLOC);
		receiveMaterial_HM.put(IConstants.LOC, FROMLOC);
		receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_" + FROMLOC);
		receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
		receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
		receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
		receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
		receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
		receiveMaterial_HM.put(IConstants.INV_EXP_DATE, REMARKS);
		receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
		//start added by Bruhan for fine tuning process on 06 August 2013
		receiveMaterial_HM.put("TYPE", "TRBYPROD");
		//End added by Bruhan for fine tuning process on 06 August 2013
		receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
		receiveMaterial_HM.put(IConstants.ISSUEDATE, strTranDate);
		xmlStr = "";
		flag = _TOUtil.process_BulkToPickingForPC(receiveMaterial_HM);
	  }
				
		if (flag) {
					
					//DbBean.CommitTran(ut);				
					request.getSession().setAttribute(
							"RESULT",
							"Product ID : " + item
									+ "  Picked/Issued successfully!");
					response.sendRedirect("jsp/TransferOrderByProduct.jsp?action=View&PLANT="
							+ PLANT + "&ITEM=" + item +"&result=sucess");
				} else {
					//DbBean.RollbackTran(ut);
					request.getSession()
							.setAttribute(
									"RESULTERROR",
									"Failed to Pick/Issue Item : "
											+ item);
					response.sendRedirect("jsp/TransferOrderByProduct.jsp?result=error");
				}
				
			} catch (Exception e) {
				 //DbBean.RollbackTran(ut);
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
				response
						.sendRedirect("jsp/TransferOrderByProduct.jsp?action=View&PLANT="
								+ PLANT + "&ITEM=" + item 
								+"&REF="
								+REMARKS
								+ "&allChecked="
								+allChecked
								+"&fullReceive="
								+fullIssue
								+"&result=catchrerror");
				//throw e;
			}
			
			return xmlStr;
		}
	//create by navas
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


private HSSFSheet createWidthOutboundSummaryWithRemarks(HSSFSheet sheet, String type) {
	int i = 0;
	try {
		sheet.setColumnWidth(i++, 1000);
		sheet.setColumnWidth(i++, 4000);
		sheet.setColumnWidth(i++, 4000);
		sheet.setColumnWidth(i++, 8000);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

private HSSFSheet createHeaderOutboundSummaryWithRemarks(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
	int k = 0;
	try {

		HSSFRow rowhead = sheet.createRow(1);

		HSSFCell cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("S/N"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Order No"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Product ID"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Remarks"));
		cell.setCellStyle(styleHeader);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
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
private HSSFSheet createHeaderOutboundSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
	int k = 0;
	try {

		HSSFRow rowhead = sheet.createRow(1);

		HSSFCell cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("S/N"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Order No"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Order Type"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Ref No"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Customer Name"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Customer Type"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("From Location"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("To Location"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Remarks1"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Remarks2"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Product ID"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Description"));
		cell.setCellStyle(styleHeader);

		
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Detail Description"));
			cell.setCellStyle(styleHeader);
	
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Department"));
			cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Category"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Sub Category"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Brand"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Date"));
		cell.setCellStyle(styleHeader);

		
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Delivery Date&Time"));
			cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("UOM"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Order Qty"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Pick Qty"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Issue Qty"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Invoiced Qty"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Employee"));
		cell.setCellStyle(styleHeader);

		
		
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Pick Status"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Issue Status"));
			cell.setCellStyle(styleHeader);
	
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Sales Location"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
		cell.setCellStyle(styleHeader);
		
		   cell = rowhead.createCell(k++);
		   cell.setCellValue(new HSSFRichTextString("User"));
		   cell.setCellStyle(styleHeader);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}
	
	private HSSFSheet createWidthOutboundSummary(HSSFSheet sheet, String type) {
		int i = 0;
		try {
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 6500);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			
				sheet.setColumnWidth(i++, 3500);
			
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 3200);
			sheet.setColumnWidth(i++, 3200);
			sheet.setColumnWidth(i++, 3200);
			sheet.setColumnWidth(i++, 4000);

			
			
				sheet.setColumnWidth(i++, 3200);
				sheet.setColumnWidth(i++, 3200);
			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
//ENd code by Bruhan for transfer order pick/issue by prod on 23 sep 2013  
	
	private String process_TranferOrderBulkPickReceive(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
		 boolean flag = false;
		Map receiveMaterial_HM = null;
		String PLANT = "", TONO = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", FROM_LOC = "", TO_LOC = "", TEMP_LOC = "", PICKEDQTY = "";
		String CUST_NAME = "",ITEM_LOC = "",REMARKS = "",CONTACTNUM="",TELNO="",EMAIL="",GINO="";
		String COLLECTION_DATE = "",COLLECTION_TIME = "",REMARK1 = "",REMARK2 = "",PERSON_INCHARGE = "",pickingqty="";
		String orderLNo = "",QTYOR="",QTYPICK= "",item = "",value = null,batchNo = "NOBATCH",BATCHID="-1",UOMQTY="1",UOM="";
		String ADD1="",ADD2="",ADD3="",ADD4="",COUNTRY="",ZIP="",ITEM_QTY="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",priceval="";
		double pickingQty = 0,unitprice=0,totalprice=0,totalqty=0;
		Map checkedTOS = new HashMap();
		//UserTransaction ut = null;
		Boolean allChecked = false,fullIssue = false;
		try {

			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			TONO = StrUtils.fString(request.getParameter("TONO"));			
			String[] chkdToNos  = request.getParameterValues("chkdToNo");
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));	
			PERSON_INCHARGE = StrUtils.fString(request.getParameter("PERSON_INCHARGE"));	
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC_0"));
			TO_LOC =   StrUtils.fString(request.getParameter("TO_WAREHOUSE"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_WAREHOUSE"));
			COLLECTION_DATE = StrUtils.fString(request.getParameter("COLLECTION_DATE"));
			COLLECTION_TIME = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
			REMARK1 = StrUtils.fString(request.getParameter("REMARK1"));
			REMARKS =  StrUtils.fString(request.getParameter("REF"));
			REMARK2=StrUtils.fString(request.getParameter("REMARK2"));
			CONTACTNUM=StrUtils.fString(request.getParameter("CONTACTNUM"));
			TELNO=StrUtils.fString(request.getParameter("TELNO"));
			EMAIL=StrUtils.fString(request.getParameter("EMAIL"));
			ADD1=StrUtils.fString(request.getParameter("ADD1"));
			ADD2=StrUtils.fString(request.getParameter("ADD2"));
			ADD3=StrUtils.fString(request.getParameter("ADD3"));
			ADD4=StrUtils.fString(request.getParameter("ADD4"));
			COUNTRY=StrUtils.fString(request.getParameter("COUNTRY")); 
			ZIP=StrUtils.fString(request.getParameter("ZIP"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			GINO = StrUtils.fString(request.getParameter("GINO"));
			
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
				strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, ITEM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception("To Loc :" + ITEM_LOC
						+ " is not a User Assigned Location/Valid Location");
			}
			if (chkdToNos != null)    {     
				for (int i = 0; i < chkdToNos.length; i++)       { 
					orderLNo = chkdToNos[i];
					pickingqty = StrUtils.fString(request.getParameter("pickingQty_"+orderLNo));
					batchNo = StrUtils.fString(request.getParameter("BATCH_"+orderLNo));
					if (batchNo.length() == 0) {
						batchNo = "NOBATCH";
					}
					checkedTOS.put(orderLNo,pickingqty+":"+batchNo);
				}
				session.setAttribute("checkedTOS", checkedTOS);
            }
									
			Boolean transactionHandler = true;
			//ut = DbBean.getUserTranaction();
			//ut.begin();
			//by Bruhan to avoid wrong loc entry if two orders have same item and user changed only order no in textbox without click view and do pickissue
			Hashtable htloc = new Hashtable();
			htloc.put(IConstants.PLANT, PLANT);
			htloc.put(IConstants.TODET_TONUM, TONO);
		
			String locquery = " isnull(fromwarehouse,'') as fromloc,isnull(towarehouse,'') as toloc ";
			List loclist = _ToHdrDAO.selectToHdr(locquery,htloc);
			if (loclist.size() > 0) {
			Map mloc = (Map) loclist.get(0);
			FROM_LOC = (String) mloc.get("fromloc");
			TO_LOC = (String) mloc.get("toloc");
			}
			// end by Bruhan
			ArrayList DODetails = null;
			Hashtable htDoDet = new Hashtable();
			String queryDoDet = "item,isnull(qtyor,0) as qtyor,isnull(qtypick,0) as qtypick,UNITMO,UNITPRICE,ItemDesc,ISNULL((select ISNULL(QPUOM,1) from "+PLANT+"_UOM where UOM=UNITMO),1) UOMQTY";
					
			process: 	
				if (chkdToNos != null)    {     
					for (int i = 0; i < chkdToNos.length; i++)       { 
						orderLNo = chkdToNos[i];
				         /* if(value!=null)
				          {
				        	  orderLNo  = value.split(":")[0];
				        	  batchNo  = value.split(":")[1];
				        	 
				          }*/
				       
				        
				        pickingqty = StrUtils.fString(request.getParameter("pickingQty_"+orderLNo));
						
				        double pckingqty = Double.parseDouble(pickingqty); 
						pckingqty = StrUtils.RoundDB(pckingqty, IConstants.DECIMALPTS);
						pickingqty = String.valueOf(pckingqty);
						pickingqty = StrUtils.formatThreeDecimal(pickingqty);
						batchNo = StrUtils.fString(request.getParameter("BATCH_"+orderLNo));
						if (batchNo.length() == 0) {
							batchNo = "NOBATCH";
						}
						BATCHID = StrUtils.fString(request.getParameter("BATCH_ID_"+orderLNo));
						//String pickedQty = StrUtils.fString(request.getParameter("Qtypicked_"+orderLNo+"_"+batchNo));
						//String recvdQty = StrUtils.fString(request.getParameter("QtyReceived_"+orderLNo+"_"+batchNo));
						htDoDet.put(IConstants.TR_TONO, TONO);
			    		htDoDet.put(IConstants.PLANT, PLANT);
			    		htDoDet.put(IConstants.TR_TOLNNO, orderLNo);
						DODetails = _TOUtil.getToDetDetails(queryDoDet, htDoDet);
						if (DODetails.size() > 0) {	

							Map map1 = (Map) DODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("ItemDesc");
							QTYOR = (String) map1.get("qtyor");
							QTYPICK = (String) map1.get("qtypick");
							UOMQTY = (String) map1.get("UOMQTY");
							UOM = (String) map1.get("UNITMO");
							
							unitprice= Double.parseDouble(StrUtils.fString((String) map1.get("UNITPRICE")));
							StrUtils strUtils = new StrUtils();
							String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
							priceval=String.valueOf(unitprice);
							double priceValue ="".equals(priceval) ? 0.0d : Double.parseDouble(priceval);
							priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
						}
                        double pickuomQty = pckingqty*Double.valueOf(UOMQTY);

						// 1. INV against
						
						
						List listQry = _InvMstDAO.getOutBoundPickingBatchIdByWMS(PLANT,item, FROM_LOC, batchNo, BATCHID);
						double invqty = 0;
						double invqtytot = 0;
						if (listQry.size() > 0) {
							for (int j = 0; j < listQry.size(); j++) {
								Map m = (Map) listQry.get(j);
								ITEM_QTY = (String) m.get("qty");
								invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
								if(!BATCHID.equals("-1"))
								{
								if(invqty < pickuomQty){
									throw new Exception(
											"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +batchNo+ "  scanned at the location  "+FROM_LOC);
									}
								}
								else
								{
									List ClistQty = _InvMstDAO.getTotalQuantityForOutBoundPickingBatchByWMS(PLANT,item, FROM_LOC,batchNo);
									if (ClistQty.size() > 0) {
										for (int k = 0; k < ClistQty.size(); k++) {
											Map mp = (Map) ClistQty.get(k);											
											Double invstkqty = Double.parseDouble((String) mp.get("qty"));
											if(invstkqty < pickuomQty){
												throw new Exception(
														"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +batchNo+ "  scanned at the location  "+FROM_LOC);
												}
											}
										}
									
								}
								invqtytot=invqtytot+invqty;
							}
						} else {
							
							throw new Exception(
									"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +batchNo+ "  scanned at the location  "+FROM_LOC);
						}
						if(invqtytot<0)
						{
							throw new Exception(
									"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +batchNo+ "  scanned at the location  "+FROM_LOC);
							
						}
						
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, item);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.TODET_TONUM, TONO);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, orderLNo);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.FROMLOC, FROM_LOC);
			receiveMaterial_HM.put(IConstants.TOLOC, TO_LOC);	
			receiveMaterial_HM.put(IConstants.LOC, TO_LOC);
			//receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_" + FROM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
			receiveMaterial_HM.put(IConstants.BATCH, batchNo);
			receiveMaterial_HM.put(IConstants.UOM, UOM);
			receiveMaterial_HM.put("UNITPRICE", priceval);
			if (BATCHID != "-1") {
				receiveMaterial_HM.put(IConstants.BATCH_ID, String.valueOf(BATCHID));
			}
			receiveMaterial_HM.put(IConstants.QTY, pickingqty);
			receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, REMARKS);
			receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
			//start added by Bruhan for fine tuning process on 06 August 2013
			receiveMaterial_HM.put("TYPE", "TRBULK");
			//End added by Bruhan for fine tuning process on 06 August 2013
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			receiveMaterial_HM.put(IConstants.ISSUEDATE, strTranDate);
			receiveMaterial_HM.put("UOMQTY", UOMQTY);
			receiveMaterial_HM.put(IConstants.GRNO, GINO);
			xmlStr = "";
    		flag = _TOUtil.process_ToPickReceiveForPC(receiveMaterial_HM);
			if(!flag)
				break process;
			}
				
		}
		if (flag) {
			new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", GINO);
			//DbBean.CommitTran(ut);				
			request.getSession().setAttribute(
			"RESULTPICK",
			"Consignment Order : " + TONO
			+ "  Picked/Received successfully!");
			response.sendRedirect("../consignment/orderpick?action=MultipleView&PLANT="
			+ PLANT + "&TONO=" + TONO);
			} else {
					//DbBean.RollbackTran(ut);
					request.getSession()
							.setAttribute(
									"RESULTERROR",
									"Failed to Pick/Issue Consignment Order : "
											+ TONO);
					response.sendRedirect("../consignment/orderpick?result=catchrerror");
				}
				
				
		}
		catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			response
					.sendRedirect("../consignment/orderpick?action=MultipleView&PLANT="
							+ PLANT + "&TONO=" + TONO 
							+"&LOC="+ITEM_LOC
							+"&REF="
							+REMARKS
							+ "&allChecked="
							+allChecked
							+"&fullIssue="
							+fullIssue
							+"&result=catcherror");
			//throw e;
		}

		return xmlStr;
	}
	
	private String process_TransferOrderPickReceivebyProd(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
		StrUtils StrUtils = new StrUtils();
		Map receiveMaterial_HM = null;

		String PLANT = "", TONO = "", FROMLOC = "",TOLOC="",tolno = "",item = "",ITEM_DESCRIPTION = "",QTYOR = "";
		String ITEM_BATCH = "NOBATCH",PICKED_QTY = "0",PICKING_QTY = "",CUST_NAME = "",LOGIN_USER = "";
		String REMARKS = "",SHIPNO="",issuingQty="",ITEM_QTY="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		double pickingQty = 0;
		Boolean allChecked = false,fullIssue = false;
		//UserTransaction ut = null;
		Map checkedDOS = new HashMap();
		String sepratedtoken1 = "";
		Map mp = null;
		mp = new HashMap();
		InvMstDAO invMstDAO = new InvMstDAO();
		invMstDAO.setmLogger(mLogger);
		try {
			String sepratedtoken = "";
			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
			StringTokenizer parser = new StringTokenizer(totalString, "=");
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
	        item = StrUtils.fString(request.getParameter("ITEM"));
	        ITEM_DESCRIPTION =  StrUtils.fString(request.getParameter("DESC"));
	        String[] chkdToNo  = request.getParameterValues("chkdToNo");
			//FROMLOC = StrUtils.fString(request.getParameter("LOC_0"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				  strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			      strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			       
        	if (chkdToNo != null)    {     
    				for (int i = 0; i < chkdToNo.length; i++)       { 
    				String	data = chkdToNo[i];
    				String[] chkdata = data.split(",");
    				String tno=chkdata[0]+"_"+chkdata[1];
    			issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+tno));
    			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+tno));
    			if (ITEM_BATCH.length() == 0) {
    					ITEM_BATCH = "NOBATCH";
    				}
    			
    					checkedDOS.put(tno, issuingQty+":"+ITEM_BATCH);
    				}
    				session.setAttribute("checkedDOS", checkedDOS);
                }
    			
            
			while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();

				System.out.println("sepratedtoken ::" + sepratedtoken);
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
						",");

				while (parser1.hasMoreTokens())
				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

		}

			TONO = StrUtils.fString((String) mp.get("data1"));
			tolno = StrUtils.fString((String) mp.get("data2"));
			QTYOR = StrUtils.fString((String) mp.get("data3"));
			PICKED_QTY = StrUtils.fString((String) mp.get("data4"));
			CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data5")));
			FROMLOC = StrUtils.fString((String) mp.get("data6"));
			TOLOC = StrUtils.fString((String) mp.get("data7"));
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
		
			 
			ArrayList TODetails = null;

    		Hashtable htToDet = new Hashtable();
    		String queryToDet = "item,itemDesc,QTYOR";
    				
					issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+TONO+"_"+tolno));	
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+TONO+"_"+tolno));
					//start code added by Bruhan for outbound balance issue on 25 july 2013
					SHIPNO = StrUtils.fString(request.getParameter("SHIPNO_"+TONO+"_"+tolno));
					//end code added by Bruhan for outbound balance issue on 25 july 2013
					if (ITEM_BATCH.length() == 0) {
    					ITEM_BATCH = "NOBATCH";
    				}
											
					htToDet.put(IConstants.TODET_TONUM, TONO);
					htToDet.put(IConstants.PLANT, PLANT);
					htToDet.put(IConstants.TODET_TOLNNO, tolno);
					TODetails = _TOUtil.getToDetDetails(queryToDet, htToDet);
					if (TODetails.size() > 0) {	

							Map map1 = (Map) TODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("itemDesc");
							QTYOR = (String) map1.get("QTYOR");
					}
					
					double orderqty = Double.parseDouble(((String) QTYOR.trim()));
					double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					pickingQty = strUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
					PICKING_QTY = String.valueOf(pickingQty);
					PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
					//SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
		
					// check for item in location
                                 UserLocUtil uslocUtil = new UserLocUtil();
		                        uslocUtil.setmLogger(mLogger);
		                        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,FROMLOC);
		                        if(!isvalidlocforUser){
		                            throw new Exception(" Loc : "+FROMLOC+" is not User Assigned Location");
		                        }
		                        
		                        
				
			
				// 1. INV against
			List listQry = _InvMstDAO.getTotalQuantityForOutBoundPickingBatchByWMS(PLANT,item, FROMLOC, ITEM_BATCH);
				double invqty = 0;
				if (listQry.size() > 0) {
					for (int j = 0; j < listQry.size(); j++) {
						Map m = (Map) listQry.get(j);
						ITEM_QTY = (String) m.get("qty");
						invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
						if(invqty < pickingQty){
							throw new Exception(
									"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +ITEM_BATCH+ "  scanned at the location  "+FROMLOC);
							}
					}
				} else {
					
					throw new Exception(
							"Error in picking Consignment Order : Inventory not found for the product: " +item+ " with batch: " +ITEM_BATCH+ "  scanned at the location  "+FROMLOC);
				}
				
	receiveMaterial_HM = new HashMap();
	receiveMaterial_HM.put(IConstants.PLANT, PLANT);
	receiveMaterial_HM.put(IConstants.ITEM, item);
	receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
	receiveMaterial_HM.put(IConstants.TODET_TONUM, TONO);
	receiveMaterial_HM.put(IConstants.TODET_TOLNNO, tolno);
	receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
	receiveMaterial_HM.put(IConstants.FROMLOC, FROMLOC);
	receiveMaterial_HM.put(IConstants.TOLOC, TOLOC);
	receiveMaterial_HM.put(IConstants.LOC, TOLOC);
	receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
	receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _TOUtil.getCustCode(PLANT, TONO));
	receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
	receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
	receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
	receiveMaterial_HM.put(IConstants.INV_EXP_DATE, REMARKS);
	receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
	//start added by Bruhan for fine tuning process on 06 August 2013
	receiveMaterial_HM.put("TYPE", "TRBYPROD");
	//End added by Bruhan for fine tuning process on 06 August 2013
	receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
	receiveMaterial_HM.put(IConstants.ISSUEDATE, strTranDate);
	xmlStr = "";
	flag = _TOUtil.process_ToPickReceiveForPC(receiveMaterial_HM);
  }
			
	if (flag) {
				
				//DbBean.CommitTran(ut);				
				request.getSession().setAttribute(
						"RESULT",
						"Product ID : " + item
								+ "  Picked/Issued successfully!");
				response.sendRedirect("jsp/TOPickReceiveByProduct.jsp?action=View&PLANT="
						+ PLANT + "&ITEM=" + item +"&result=sucess");
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession()
						.setAttribute(
								"RESULTERROR",
								"Failed to Pick/Issue Item : "
										+ item);
				response.sendRedirect("jsp/TOPickReceiveByProduct.jsp?result=error");
			}
			
		} catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
			response
					.sendRedirect("jsp/TOPickReceiveByProduct.jsp?action=View&PLANT="
							+ PLANT + "&ITEM=" + item 
							+"&REF="
							+REMARKS
							+ "&allChecked="
							+allChecked
							+"&fullReceive="
							+fullIssue
							+"&result=catchrerror");
			//throw e;
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
