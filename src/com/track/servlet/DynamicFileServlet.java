package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DNPLHdrDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.MultiPoEstHdrDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.dao.TransportModeDAO;
import com.track.dao.multiPoEstDetDAO;
import com.track.db.object.DoHdr;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.MultiPoEstDet;
import com.track.db.object.MultiPoEstHdr;
import com.track.db.object.PoHdr;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TOUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.json.JSONObject;

/*- ************Modification History*********************************
Dec 16 2014 Deen, Description:Modify Methods:viewDOReportWITHBATCH,viewDOReportWITHOUTBATCH,viewDOReportWITHBATCHANDCONTAINER,
                  viewInvoiceReportWITHBATCH,viewInvoiceReportWITHOUTBATCH,viewInvoiceReportWITHBATCHANDCONTAINER,
                  viewPOReportWITHBATCH,viewPOReportWITHOUTBATCH,viewPOInvoiceReportWITHBATCH,viewPOInvoiceReportWITHOUTBATCH - Include parameter RCBNO 
*/
@SuppressWarnings({"rawtypes", "unchecked"})
public class DynamicFileServlet extends HttpServlet implements IMLogger {
	
	private static final long serialVersionUID = 3945510584586969950L;
//	private boolean printLog = Boolean.valueOf(true);
//	private boolean printInfo = Boolean.valueOf(true);
	DateUtils dateUtils = null;
	StrUtils strUtils = new StrUtils();
	MasterUtil _masterUtil = null;
	PlantMstDAO _PlantMstDAO = null;
	DOUtil dOUtil = null;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dOUtil = new DOUtil();
		_PlantMstDAO = new PlantMstDAO();
		_masterUtil = new MasterUtil();
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

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException

	{

		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));

			String action = StrUtils.fString(request.getParameter("action"));
			
			if (action.equalsIgnoreCase("printDOWITHBATCH")) {
				viewDOReport(request, response, action);
			} else if (action.equalsIgnoreCase("printDOWITHBATCHANDCONTAINER")) {
				viewDOReport(request, response, action);
			} else if (action.equalsIgnoreCase("printDOWITHOUTBATCH")) {
				viewDOReport(request, response, action);
			} else if (action.equalsIgnoreCase("printInvoiceWITHBATCH")) {
					 viewInvoiceReport(request, response, action);
			} else if (action.equalsIgnoreCase("printInvoiceWITHBATCHANDCONTAINER")) {
				viewInvoiceReport(request, response, action);
			} else if (action.equalsIgnoreCase("printInvoiceWITHOUTBATCH")) {
				viewInvoiceReport(request, response, action);
			} else if (action.equalsIgnoreCase("printDnplWITHBATCH")) {
				viewDNPLReport(request, response, action);

			} else if (action.equalsIgnoreCase("printPOWITHBATCH")) {
				viewPOReport(request, response, "printPOWITHBATCH");
			} else if (action.equalsIgnoreCase("printPOWITHOUTBATCH")) {
				viewPOReport(request, response, "printPOWITHOUTBATCH");
			}  else if (action.equalsIgnoreCase("printPOInvoiceWITHBATCH")) {
					 viewPOInvoiceReport(request, response, "printPOInvoiceWithBatch");
			} else if (action.equalsIgnoreCase("printPOInvoiceWITHOUTBATCH")) {
				viewPOInvoiceReport(request, response, "printPOInvoiceWithOutBatch");
			} else if (action.equalsIgnoreCase("printLoanOrder")) {
				viewLoanOrderReport(request, response, "rptLoanOrder");
			} else if (action.equalsIgnoreCase("printLOANORDWITHBATCH")) {
				viewLoanOrderReport(request, response, "rptLOANORDWITHBATCH");
			} else if (action.equalsIgnoreCase("printLOANORDWITHOUTBATCH")) {
				viewLoanOrderReport(request, response, "rptLOANORDWITHOUTBATCH");

			} else if (action.equalsIgnoreCase("printRegister")) {
				viewRegister(request, response, "RegisterCustomer"); //

			}
			else if (action.equalsIgnoreCase("printLOANORDWITHOUTBATCHWITHPRICE")) {
				viewLoanOrderInvoiceReport(request, response, "rptLOANORDWITHOUTBATCHWITHPRICE");

			}
			else if (action.equalsIgnoreCase("printLOANORDWITHBATCHWITHPRICE")) {
					viewLoanOrderInvoiceReport(request, response, "rptLOANORDWITHBATCHWITHPRICE");
			}
			else if (action.equalsIgnoreCase("printTO")) {
				viewTOReport(request, response, "rptTransferOrder");
			} else if (action.equalsIgnoreCase("printTOWITHBATCH")) {
				viewTOReportNew(request, response, "rptTOWITHBATCH");
			} else if (action.equalsIgnoreCase("printTOWITHOUTBATCH")) {
				viewTOReportNew(request, response, "rptTOWITHOUTBATCH");
			} else if (action.equalsIgnoreCase("printTOWITHBATCHWITHPRICE")) {
				viewToReportWithPrice(request, response, "rptTOWITHBATCHWITHPRICE");
			} else if (action.equalsIgnoreCase("printTOWITHOUTBATCHWITHPRICE")) {
				viewToReportWithPrice(request, response, "rptTOWITHOUTBATCHWITHPRICE");
			} else if (action.equalsIgnoreCase("PREVIEW_LOGO_ON_PAGE")) {
				viewLogoPreview(request, response, "rpLogoPreview");
			} else if (action.equalsIgnoreCase("printBarcode")) {
				//viewReceiptBarcode(request, response, "printBarcodeReceipt");
				viewReceiptBarcodeNew(request, response, "printBarcodeReceipt");
			} else if (action.equalsIgnoreCase("printLocBarcode")) {
				//viewLocBarcode(request, response, "printLocBarcode");
				viewLocBarcodeNew(request, response, "printLocBarcode");
			} else if (action.equalsIgnoreCase("printManualBarcode")) {
				//viewManualBarcode(request, response, "printManualBarcode");
				viewManualBarcodeNew(request, response, "printManualBarcode");
			} else if (action.equalsIgnoreCase("printProductBarcode")) {
				viewProductBarcodeNew(request, response, "printProductBarcode");
			} else if (action.equalsIgnoreCase("printMultiProductBarcode")) {
				viewProductBarcodeNew(request, response, "printMultiProductBarcode");
			} else if (action.equalsIgnoreCase("printProductBarcodeModal")) {
				viewProductBarcodeNew(request, response, "printProductBarcodeModal");
			} else if (action.equalsIgnoreCase("printProductBarcodeModalSales")) {
				viewProductBarcodeNew(request, response, "printProductBarcodeModalSales");
			}else if (action.equalsIgnoreCase("printProductBarcodeModalINV")) {
				viewProductBarcodeNew(request, response, "printProductBarcodeModalINV");
			}else if (action.equalsIgnoreCase("SavemultiPO")) {
				SaveMultipo(request, response, "SaveMultipur");
			} else if (action.equalsIgnoreCase("printEmployeePayslip")) {
				String sendby = StrUtils.fString(request.getParameter("SendAs"));
				String[] chkdEmpNo = request.getParameterValues("chkdEmpNo");
				String from_month = StrUtils.fString(request.getParameter("from_month"));
				String from_year = StrUtils.fString(request.getParameter("from_year"));
				if(sendby.equalsIgnoreCase("Preview")){
					viewEmployeePayslip(request, response, "printEmployeePayslip","");
				} else {
					try {
					if(chkdEmpNo!=null)
					{
						if(from_month.equalsIgnoreCase("JAN"))
							from_month="January";
						else if(from_month.equalsIgnoreCase("FEB"))
							from_month="February";
						else if(from_month.equalsIgnoreCase("MAR"))
							from_month="March";
						else if(from_month.equalsIgnoreCase("APR"))
							from_month="April";
						else if(from_month.equalsIgnoreCase("MAY"))
							from_month="May";
						else if(from_month.equalsIgnoreCase("JUN"))
							from_month="June";
						else if(from_month.equalsIgnoreCase("JUL"))
							from_month="July";
						else if(from_month.equalsIgnoreCase("AUG"))
							from_month="August";
						else if(from_month.equalsIgnoreCase("SEP"))
							from_month="September";
						else if(from_month.equalsIgnoreCase("OCT"))
							from_month="October";
						else if(from_month.equalsIgnoreCase("NOV"))
							from_month="November";
						else if(from_month.equalsIgnoreCase("DEC"))
							from_month="December";
						List<String> attachmentLocations = new ArrayList<>();
						String attachmentLocation = null;
					 for (int j = 0; j < chkdEmpNo.length; j++) {
						 String EMPNO = chkdEmpNo[j];
						 ArrayList movQryList =  new EmployeeDAO().getEmployeeDetails(EMPNO,plant,"");
						 if (movQryList.size() > 0) {
						 Map arrCustLine = (Map)movQryList.get(0);
						 String EMPLOYEE_NAME =(String)arrCustLine.get("FNAME");
						 String EMAIL =(String)arrCustLine.get("EMAIL");
						 String REPORTING_EMPLOYEE =(String)arrCustLine.get("REPORTING_INCHARGENAME");
						 
						 if(!EMAIL.equalsIgnoreCase("")) {
						 EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PAYSLIP);
							// TODO: Send proper value in action parameter
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("payslip".equals(sendAttachment)) {
								viewEmployeePayslip(request, response, "printEmployeePayslip", EMPNO);
								attachmentLocations = new ArrayList<>();
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Payslip_" + EMPNO + ".pdf";
								if (attachmentLocation != null) {
									attachmentLocations.add(attachmentLocation);
								}
							}
							String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,IConstants.PAYSLIP);
							if (isAutoEmail.equalsIgnoreCase("Y")) {
								new EmailMsgUtil().sendPayslipEmail(plant, EMPLOYEE_NAME, IConstants.PAYSLIP,EMAIL,REPORTING_EMPLOYEE,attachmentLocations,from_month,from_year);
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);					
								htMovHis.put("DIRTYPE", TransactionConstants.BULK_PAYSLIP_PROCESSING_SEND_MAIL);
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								htMovHis.put(IDBConstants.ITEM, "");
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, EMPNO);
								htMovHis.put(IDBConstants.CREATED_BY, userName);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS",EMAIL);
								boolean isAdded =new MovHisDAO().insertIntoMovHis(htMovHis); // Insert MOVHIS
							}
						} /*
							 * else { // Delete files try { new File(DbBean.COMPANY_MAIL_ATTACHMENT_PATH +
							 * "/Payslip_" + chkdEmpNo[j] + ".pdf").delete(); }catch(Exception e) { //
							 * Ignore exception if it comes in attachment file deletion } }
							 */
					 }
					}
					 }

					response.sendRedirect("../payroll/payslipgenerate?result=Email Sent Successfully");
					} catch (Exception e) {
						response.sendRedirect("../payroll/payslipgenerate?result=Email Not Sent, "+e.toString());
					}
				}
			}
			
		} catch (Exception e) {

		}

	}
	private void viewManualBarcode(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		try {
			HttpSession session = request.getSession();
			String DESC = request.getParameter("ITEM_DESC");
			String BARCODE = request.getParameter("BARCODE");
			String QUANTITY = request.getParameter("QUANTITY");
			String PLANT = (String) session.getAttribute("PLANT");
			String username = (String) session.getAttribute("LOGIN_USER");
			String printype = StrUtils.fString(request.getParameter("Submit"));
			String asd = "";
			
			String txtfileName =PLANT+"_ManualBarcode"+printype+".prn";
			String filePath = DbBean.COMPANY_LOGO_PATH; 
			String filetowrite = filePath+"/"+txtfileName; // Point to your location
			File myObj = new File(filetowrite);
		      if (myObj.exists()) {
		    	  myObj.delete();
		      }
		    	  myObj.createNewFile();
			FileWriter sw = new FileWriter(filetowrite);
				
				int redty = Integer.valueOf(QUANTITY);
				for (int j = 0; j < redty; j++) {
				
					if(printype.equalsIgnoreCase("50X25")) {
						
						if(DESC.length()>34)
							DESC=DESC.substring(0, 33);
						
						asd = "SIZE 97.5 mm, 25 mm";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "DIRECTION 0,0";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "REFERENCE 0,0";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "OFFSET 0 mm";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET PEEL OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET CUTTER OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET PARTIAL_CUTTER OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET TEAR ON";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "CLS";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "BARCODE 762,127,\"128M\",61,0,180,2,4,\"!104" + BARCODE + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "CODEPAGE 1252";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 762,60,\"ROMAN.TTF\",180,1,10,\"" + BARCODE + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 762,165,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 682,165,\"ROMAN.TTF\",180,1,8,\"" + DESC + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        j = j + 1;
                        if (redty > j)
                        {
                        	
                        	//insert barcode
        	                Hashtable ht = new Hashtable();
        					ht.put(IDBConstants.PLANT,PLANT);
        					ht.put("BARCODE",BARCODE);
        					ht.put("DESCRIPTION",DESC);
        					ht.put("PRINTQTY","1");
        					ht.put("STATUS","N");
        					ht.put("LABELTYPE",printype);
        					ht.put(IDBConstants.LOGIN_USER,username);
        					ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//        					boolean insertflag = new MasterDAO().InsertManualBarcode(ht);
        					new MasterDAO().InsertManualBarcode(ht);
                        asd = "BARCODE 370,127,\"128M\",61,0,180,2,4,\"!104" + BARCODE + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 370,60,\"ROMAN.TTF\",180,1,10,\"" + BARCODE + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 370,166,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 295,165,\"ROMAN.TTF\",180,1,8,\"" + DESC + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        }
                        asd = "PRINT 1,1";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        //redty--;
						
					} else if(printype.equalsIgnoreCase("100X50")) {
					
					asd = "SIZE 97.5 mm, 50 mm";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "DIRECTION 0,0";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "REFERENCE 0,0";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "OFFSET 0 mm";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET PEEL OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET CUTTER OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET PARTIAL_CUTTER OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET TEAR ON";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "CLS";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "BARCODE 746,247,\"128M\",101,0,180,2,4,\"!104" + BARCODE + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "CODEPAGE 1252";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 746,140,\"ROMAN.TTF\",180,1,10,\"" + BARCODE + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 746,277,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 666,277,\"ROMAN.TTF\",180,1,8,\"" + DESC + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "PRINT 1,1";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
				}
					//insert barcode
	                Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("BARCODE",BARCODE);
					ht.put("DESCRIPTION",DESC);
					ht.put("PRINTQTY","1");
					ht.put("STATUS","N");
					ht.put("LABELTYPE",printype);
					ht.put(IDBConstants.LOGIN_USER,username);
					ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//					boolean insertflag = new MasterDAO().InsertManualBarcode(ht);
					new MasterDAO().InsertManualBarcode(ht);
				}
				
			sw.close();
			FileHandling fileHandling=new FileHandling();
			fileHandling.fileDownload(filePath, txtfileName, "text/html", response);
			myObj.delete();
			con = DbBean.getConnection();
		      
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}
	private void viewLocBarcode(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		try {
			HttpSession session = request.getSession();
			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
			String PLANT = (String) session.getAttribute("PLANT");
//			String username = (String) session.getAttribute("LOGIN_USER");
			String printype = StrUtils.fString(request.getParameter("Submit"));
			String asd = "";
			String loc="",locdesc="",recqty="";
			
			String txtfileName =PLANT+"_LocBarcode"+printype+".prn";
			String filePath = DbBean.COMPANY_LOGO_PATH; 
			String filetowrite = filePath+"/"+txtfileName; // Point to your location
			File myObj = new File(filetowrite);
		      if (myObj.exists()) {
		    	  myObj.delete();
		      }
		    	  myObj.createNewFile();
			FileWriter sw = new FileWriter(filetowrite);
			for (int i = 0; i < chkdDoNo.length; i++) {
				String	data = chkdDoNo[i];
				String[] chkdata = data.split(",");
				
				loc = chkdata[0];				
				locdesc = chkdata[1];
				recqty = StrUtils.fString(request.getParameter("PrintQty_"+loc));
				int redty = Integer.valueOf(recqty);
				for (int j = 0; j < redty; j++) {
				
					if(printype.equalsIgnoreCase("50X25")) {
						
						if(locdesc.length()>34)
							locdesc=locdesc.substring(0, 33);
						
						asd = "SIZE 97.5 mm, 25 mm";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "DIRECTION 0,0";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "REFERENCE 0,0";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "OFFSET 0 mm";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET PEEL OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET CUTTER OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET PARTIAL_CUTTER OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET TEAR ON";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "CLS";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "BARCODE 762,127,\"128M\",61,0,180,2,4,\"!104" + loc + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "CODEPAGE 1252";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 762,60,\"ROMAN.TTF\",180,1,10,\"" + loc + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 762,165,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 682,165,\"ROMAN.TTF\",180,1,8,\"" + locdesc + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        j = j + 1;
                        if (redty > j)
                        {
                        asd = "BARCODE 370,127,\"128M\",61,0,180,2,4,\"!104" + loc + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 370,60,\"ROMAN.TTF\",180,1,10,\"" + loc + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 370,166,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 295,165,\"ROMAN.TTF\",180,1,8,\"" + locdesc + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
					}
                        asd = "PRINT 1,1";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        //redty--;
						
					} else if(printype.equalsIgnoreCase("100X50")) {
					
					asd = "SIZE 97.5 mm, 50 mm";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "DIRECTION 0,0";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "REFERENCE 0,0";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "OFFSET 0 mm";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET PEEL OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET CUTTER OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET PARTIAL_CUTTER OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET TEAR ON";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "CLS";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "BARCODE 746,247,\"128M\",101,0,180,2,4,\"!104" + loc + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "CODEPAGE 1252";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 746,140,\"ROMAN.TTF\",180,1,10,\"" + loc + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 746,277,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 666,277,\"ROMAN.TTF\",180,1,8,\"" + locdesc + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "PRINT 1,1";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
				}	
				}
				
			}
			sw.close();
			FileHandling fileHandling=new FileHandling();
			fileHandling.fileDownload(filePath, txtfileName, "text/html", response);
			myObj.delete();
			con = DbBean.getConnection();
		      
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}
	private void viewReceiptBarcode(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		try {
			HttpSession session = request.getSession();
			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
			String PLANT = (String) session.getAttribute("PLANT");
			String username = (String) session.getAttribute("LOGIN_USER");
			String printype = StrUtils.fString(request.getParameter("Submit"));
			String grno="",asd = "",pono="";
			String barcode1="",description="",barcode="",item="",lnno="",loc="",batch="",recqty="",uom="";
			String printwithlot="",printwithbatch="",printwithpobill="";
			if (request.getParameter("printwithlot") != null) {
				printwithlot="1";
			}
			if (request.getParameter("printwithbatch") != null) {
				printwithbatch="1";
			}
			if (request.getParameter("printwithpobill") != null) {
				printwithpobill="1";	
			}
			String txtfileName =PLANT+"_ReceiptBarcode"+printype+".prn";
			String filePath = DbBean.COMPANY_LOGO_PATH; 
			String filetowrite = filePath+"/"+txtfileName; // Point to your location
			File myObj = new File(filetowrite);
		      if (myObj.exists()) {
		    	  myObj.delete();
		      }
		    	  myObj.createNewFile();
			FileWriter sw = new FileWriter(filetowrite);
			for (int i = 0; i < chkdDoNo.length; i++) {
				String	data = chkdDoNo[i];
				String[] chkdata = data.split(",");
				
				grno = chkdata[0];				
				item = chkdata[1];
				lnno = chkdata[2];
				loc = chkdata[3];
				batch = chkdata[4];
				recqty = chkdata[5];
				uom = chkdata[6];
				pono = chkdata[7];
				
				description = new ItemMstDAO().getItemDesc(PLANT,item);				
				TblControlDAO _TblControlDAO = new TblControlDAO();
			
				
			if(printwithlot.equalsIgnoreCase("")) {// check print with lot 
				
				int redty = Integer.valueOf(recqty);
				for (int j = 0; j < redty; j++) {
					barcode = _TblControlDAO.getNextOrder(PLANT,username,IConstants.BARCODE);
					barcode1=barcode;
					
					if(printype.equalsIgnoreCase("50X25")) {
						
						if(description.length()>34)
							description=description.substring(0, 33);
						
						if(printwithbatch.equalsIgnoreCase("")) { //50X25 WITHOUT BATCH
						
							asd = "SIZE 97.5 mm, 25 mm";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "DIRECTION 0,0";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "REFERENCE 0,0";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "OFFSET 0 mm";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET PEEL OFF";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET CUTTER OFF";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET PARTIAL_CUTTER OFF";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET TEAR ON";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "CLS";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "BARCODE 762,127,\"128M\",61,0,180,2,4,\"!104" + barcode + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "CODEPAGE 1252";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 762,60,\"ROMAN.TTF\",180,1,10,\"" + barcode + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 762,165,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 682,165,\"ROMAN.TTF\",180,1,8,\"" + description + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            j = j + 1;
                            if (redty > j)
                            {
                            	
                            	//insert barcode
                                Hashtable ht = new Hashtable();
                				ht.put(IDBConstants.PLANT,PLANT);
                				ht.put("BarcodeId",barcode);
                				ht.put("Item",item);
                				ht.put("LOC",loc);
                				ht.put("BATCH",batch);
                				ht.put("UNITMO",uom);
                				ht.put("PrintQty","1");
                				ht.put("PrintId","1");
                				ht.put("Status","N");
                				ht.put("LABELTYPE",printype);
                				ht.put("PRINTWITHBATCH",printwithbatch);
                				ht.put("PRINTWITHPOBILL",printwithpobill);
                				ht.put("PRINTWITHLOT",printwithlot);
                				ht.put(IDBConstants.LOGIN_USER,username);
                				ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//                				boolean insertflag = new MasterDAO().InsertBarcode(ht);
                				new MasterDAO().InsertBarcode(ht);
                                //update printqty
                				Hashtable htestCond = new Hashtable();
                				htestCond.put("PLANT", PLANT);
                				htestCond.put("GRNO", grno);
                				htestCond.put("LNNO", lnno);
                				htestCond.put("ITEM", item);
                				htestCond.put("BATCH", batch);
                	
                				String queryest = "set PRINTQTY= isNull(PRINTQTY,0) + 1";
//                				boolean flag = new RecvDetDAO().update(queryest, htestCond, "",PLANT);
                				new RecvDetDAO().update(queryest, htestCond, "",PLANT);
                				_TblControlDAO.updateSeqNo(IConstants.BARCODE,PLANT);
                				
                				barcode1 = _TblControlDAO.getNextOrder(PLANT,username,IConstants.BARCODE);
                				barcode=barcode1;
                            	
                            asd = "BARCODE 370,127,\"128M\",61,0,180,2,4,\"!104" + barcode1 + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 370,60,\"ROMAN.TTF\",180,1,10,\"" + barcode1 + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 370,166,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 295,165,\"ROMAN.TTF\",180,1,8,\"" + description + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            }
                            asd = "PRINT 1,1";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            //redty--;
                
				} else { //50X25 WITH BATCH
					
					if(description.length()>39)
						description=description.substring(0, 38);
					
					asd = "SIZE 97.5 mm, 25 mm";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "DIRECTION 0,0";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "REFERENCE 0,0";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "OFFSET 0 mm";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET PEEL OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET CUTTER OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET PARTIAL_CUTTER OFF";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "SET TEAR ON";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "CLS";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "BARCODE 762,151,\"128M\",37,0,180,2,4,\"!104" + barcode + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "CODEPAGE 1252";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 762,108,\"ROMAN.TTF\",180,1,8,\"" + barcode + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);  
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 691,173,\"ROMAN.TTF\",180,1,7,\"" + description + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 762,173,\"ROMAN.TTF\",180,1,7,\"DESC. : \"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "BARCODE 762,63,\"128M\",38,0,180,2,4,\"!104" + batch + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 762,20,\"ROMAN.TTF\",180,1,7,\"" + batch + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 762,85,\"ROMAN.TTF\",180,1,7,\"Batch\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    j = j + 1;
                    if (redty > j)
                    {
                    	
                    	//insert barcode
                        Hashtable ht = new Hashtable();
        				ht.put(IDBConstants.PLANT,PLANT);
        				ht.put("BarcodeId",barcode);
        				ht.put("Item",item);
        				ht.put("LOC",loc);
        				ht.put("BATCH",batch);
        				ht.put("UNITMO",uom);
        				ht.put("PrintQty","1");
        				ht.put("PrintId","1");
        				ht.put("Status","N");
        				ht.put("LABELTYPE",printype);
        				ht.put("PRINTWITHBATCH",printwithbatch);
        				ht.put("PRINTWITHPOBILL",printwithpobill);
        				ht.put("PRINTWITHLOT",printwithlot);
        				ht.put(IDBConstants.LOGIN_USER,username);
        				ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//        				boolean insertflag = new MasterDAO().InsertBarcode(ht);
        				new MasterDAO().InsertBarcode(ht);
                        //update printqty
        				Hashtable htestCond = new Hashtable();
        				htestCond.put("PLANT", PLANT);
        				htestCond.put("GRNO", grno);
        				htestCond.put("LNNO", lnno);
        				htestCond.put("ITEM", item);
        				htestCond.put("BATCH", batch);
        	
        				String queryest = "set PRINTQTY= isNull(PRINTQTY,0) + 1";
//        				boolean flag = new RecvDetDAO().update(queryest, htestCond, "",PLANT);
        				new RecvDetDAO().update(queryest, htestCond, "",PLANT);
        				_TblControlDAO.updateSeqNo(IConstants.BARCODE,PLANT);
        				
        				barcode1 = _TblControlDAO.getNextOrder(PLANT,username,IConstants.BARCODE);
        				barcode=barcode1;
                    	
                    asd = "BARCODE 370,63,\"128M\",38,0,180,2,4,\"!104" + batch + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 370,20,\"ROMAN.TTF\",180,1,7,\"" + batch + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 370,85,\"ROMAN.TTF\",180,1,7,\"Batch\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "BARCODE 370,151,\"128M\",37,0,180,2,4,\"!104" + barcode1 + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 370,108,\"ROMAN.TTF\",180,1,8,\"" + barcode1 + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 299,173,\"ROMAN.TTF\",180,1,7,\"" + description + "\"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    asd = "TEXT 370,173,\"ROMAN.TTF\",180,1,7,\"DESC. : \"";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    }
                    asd = "PRINT 1,1";
                    asd = asd.replace("", "");
                    sw.write(asd);
                    sw.write(System.getProperty("line.separator"));
                    //redty--;
					
				}							
						
					}					
					else if(printype.equalsIgnoreCase("100X50")) {						
					
						if(printwithpobill.equalsIgnoreCase("")) { //100X50 WITHOUT PO/BILL
						
						if(printwithbatch.equalsIgnoreCase("")) { //100X50 WITHOUT BATCH WITHOUT PO/BILL
							
							asd = "SIZE 97.5 mm, 50 mm";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "DIRECTION 0,0";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "REFERENCE 0,0";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "OFFSET 0 mm";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET PEEL OFF";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET CUTTER OFF";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET PARTIAL_CUTTER OFF";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "SET TEAR ON";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "CLS";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "BARCODE 746,247,\"128M\",101,0,180,2,4,\"!104" + barcode + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "CODEPAGE 1252";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 746,140,\"ROMAN.TTF\",180,1,10,\"" + barcode + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 746,277,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "TEXT 666,277,\"ROMAN.TTF\",180,1,8,\"" + description + "\"";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
                            asd = "PRINT 1,1";
                            asd = asd.replace("", "");
                            sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							
						} else { //100X50 WITH BATCH  WITHOUT PO/BILL
							
						asd = "SIZE 97.5 mm, 50 mm";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "DIRECTION 0,0";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "REFERENCE 0,0";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "OFFSET 0 mm";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET PEEL OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET CUTTER OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET PARTIAL_CUTTER OFF";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "SET TEAR ON";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "CLS";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        
                        asd = "CODEPAGE 1252";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 658,325,\"ROMAN.TTF\",180,1,8,\"" + description + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 738,325,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "BARCODE 738,295,\"128M\",77,0,180,2,4,\"!104" + barcode + "\"";                            
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 738,212,\"ROMAN.TTF\",180,1,10,\"" + barcode + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 738,158,\"ROMAN.TTF\",180,1,7,\"Batch\"";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        asd = "BARCODE 738,135,\"128M\",45,0,180,2,4,\"!104" + batch + "\"";
                        asd = asd.replace("", "");
                        sw.write(asd);  
                        sw.write(System.getProperty("line.separator"));
                        asd = "TEXT 738,84,\"ROMAN.TTF\",180,1,8,\"" + batch + "\"";                            
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));

                        asd = "PRINT 1,1";
                        asd = asd.replace("", "");
                        sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
                        
						}
						} else { //100X50 WITH PO/BILL
							
							if(printwithbatch.equalsIgnoreCase("")) { //100X50 WITHOUT BATCH WITH PO/BILL
							
								asd = "SIZE 97.5 mm, 50 mm";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "DIRECTION 0,0";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "REFERENCE 0,0";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "OFFSET 0 mm";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET PEEL OFF";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET CUTTER OFF";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET PARTIAL_CUTTER OFF";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET TEAR ON";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "CLS";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "CODEPAGE 1252";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 746,285,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 666,285,\"ROMAN.TTF\",180,1,8,\"" + description + "\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "BARCODE 746,247,\"128M\",101,0,180,3,6,\"!104" + barcode + "\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 746,140,\"ROMAN.TTF\",180,1,10,\"" + barcode + "\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 746,325,\"ROMAN.TTF\",180,1,8,\"PO/BillINo. : \"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 618,325,\"ROMAN.TTF\",180,1,8,\"" + pono + "\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "PRINT 1,1";	
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
		                        
							} else { //100X50 WITH BATCH WITH PO/BILL 
								
								asd = "SIZE 97.5 mm, 50 mm";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "DIRECTION 0,0";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "REFERENCE 0,0";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "OFFSET 0 mm";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET PEEL OFF";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET CUTTER OFF";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET PARTIAL_CUTTER OFF";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "SET TEAR ON";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "CLS";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "CODEPAGE 1252";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 658,325,\"ROMAN.TTF\",180,1,8,\"" + description +"\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 738,325,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "BARCODE 738,295,\"128M\",77,0,180,3,6,\"!104"+ barcode +"\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 738,212,\"ROMAN.TTF\",180,1,10,\""+ barcode +"\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 738,158,\"ROMAN.TTF\",180,1,7,\"Batch\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "BARCODE 738,135,\"128M\",45,0,180,2,4,\"!104"+ batch +"\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 738,84,\"ROMAN.TTF\",180,1,8,\""+ batch +"\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 738,357,\"ROMAN.TTF\",180,1,8,\"PO/Bill No. : \"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "TEXT 610,357,\"ROMAN.TTF\",180,1,8,\""+ pono +"\"";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
								asd = "PRINT 1,1";
								sw.write(asd);
		                        sw.write(System.getProperty("line.separator"));
							} 
						}
					}
                //insert barcode
                Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put("BarcodeId",barcode);
				ht.put("Item",item);
				ht.put("LOC",loc);
				ht.put("BATCH",batch);
				ht.put("UNITMO",uom);
				ht.put("PrintQty","1");
				ht.put("PrintId","1");
				ht.put("Status","N");
				ht.put("LABELTYPE",printype);
				ht.put("PRINTWITHBATCH",printwithbatch);
				ht.put("PRINTWITHPOBILL",printwithpobill);
				ht.put("PRINTWITHLOT",printwithlot);
				ht.put(IDBConstants.LOGIN_USER,username);
				ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//				boolean insertflag = new MasterDAO().InsertBarcode(ht);
				new MasterDAO().InsertBarcode(ht);                
                //update printqty
				Hashtable htestCond = new Hashtable();
				htestCond.put("PLANT", PLANT);
				htestCond.put("GRNO", grno);
				htestCond.put("LNNO", lnno);
				htestCond.put("ITEM", item);
				htestCond.put("BATCH", batch);
	
				String queryest = "set PRINTQTY= isNull(PRINTQTY,0) + 1";
//				boolean flag = new RecvDetDAO().update(queryest, htestCond, "",PLANT);
				new RecvDetDAO().update(queryest, htestCond, "",PLANT);
				_TblControlDAO.updateSeqNo(IConstants.BARCODE,PLANT);
				}
			} else {
				
				barcode = _TblControlDAO.getNextOrder(PLANT,username,IConstants.BARCODE);
				barcode1=barcode;
				
				if(printype.equalsIgnoreCase("100X50")) {						
				
					if(printwithpobill.equalsIgnoreCase("")) { //100X50 WITHOUT PO/BILL
					
					if(printwithbatch.equalsIgnoreCase("")) { //100X50 WITHOUT BATCH WITHOUT PO/BILL
						
						asd = "SIZE 97.5 mm, 50 mm";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "DIRECTION 0,0";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "REFERENCE 0,0";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "OFFSET 0 mm";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET PEEL OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET CUTTER OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET PARTIAL_CUTTER OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET TEAR ON";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "CLS";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "CODEPAGE 1252";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 746,277,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 666,277,\"ROMAN.TTF\",180,1,8,\""+ description +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "BARCODE 746,247,\"128M\",101,0,180,3,6,\"!104"+ barcode +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 746,140,\"ROMAN.TTF\",180,1,10,\""+ barcode +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 746,85,\"ROMAN.TTF\",180,1,8,\"QTY. : \"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 674,85,\"ROMAN.TTF\",180,1,8,\""+ recqty +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "PRINT 1,1";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						
					} else {
						
						asd = "SIZE 97.5 mm, 50 mm";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "DIRECTION 0,0";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "REFERENCE 0,0";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "OFFSET 0 mm";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET PEEL OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET CUTTER OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET PARTIAL_CUTTER OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET TEAR ON";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "CLS";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "CODEPAGE 1252";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 658,325,\"ROMAN.TTF\",180,1,8,\""+ description +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 738,325,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "BARCODE 738,295,\"128M\",77,0,180,3,6,\"!104"+ barcode +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 738,212,\"ROMAN.TTF\",180,1,10,\""+ barcode +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 738,157,\"ROMAN.TTF\",180,1,7,\"Batch\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "BARCODE 738,135,\"128M\",45,0,180,2,4,\"!104"+ batch +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 738,84,\"ROMAN.TTF\",180,1,8,\""+ batch +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 738,45,\"ROMAN.TTF\",180,1,8,\"QTY. : \"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 666,45,\"ROMAN.TTF\",180,1,8,\""+ recqty +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "PRINT 1,1";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
					
					}
					} else { //100X50 WITH PO/BILL
						
						if(printwithbatch.equalsIgnoreCase("")) { //100X50 WITHOUT BATCH WITH PO/BILL
						
						asd = "SIZE 97.5 mm, 50 mm";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "DIRECTION 0,0";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "REFERENCE 0,0";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "OFFSET 0 mm";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET PEEL OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET CUTTER OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET PARTIAL_CUTTER OFF";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "SET TEAR ON";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "CLS";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "CODEPAGE 1252";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 746,285,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 666,285,\"ROMAN.TTF\",180,1,8,\""+ description +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "BARCODE 746,247,\"128M\",101,0,180,3,6,\"!104"+ barcode +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 746,140,\"ROMAN.TTF\",180,1,10,\""+ barcode +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 746,325,\"ROMAN.TTF\",180,1,8,\"PO/Bill No. : \"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 618,325,\"ROMAN.TTF\",180,1,8,\""+ pono +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 746,85,\"ROMAN.TTF\",180,1,8,\"QTY. : \"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "TEXT 674,85,\"ROMAN.TTF\",180,1,8,\""+ recqty +"\"";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						asd = "PRINT 1,1";
						sw.write(asd);
                        sw.write(System.getProperty("line.separator"));
						
						} else {
							
							asd = "SIZE 97.5 mm, 50 mm";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "DIRECTION 0,0";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "REFERENCE 0,0";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "OFFSET 0 mm";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "SET PEEL OFF";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "SET CUTTER OFF";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "SET PARTIAL_CUTTER OFF";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "SET TEAR ON";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "CLS";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "CODEPAGE 1252";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 658,325,\"ROMAN.TTF\",180,1,8,\""+ description +"\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 738,325,\"ROMAN.TTF\",180,1,8,\"DESC. : \"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "BARCODE 738,295,\"128M\",77,0,180,3,6,\"!104"+ barcode +"\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 738,212,\"ROMAN.TTF\",180,1,10,\""+ barcode +"\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 738,157,\"ROMAN.TTF\",180,1,7,\"Batch\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "BARCODE 738,135,\"128M\",45,0,180,2,4,\"!104"+ batch +"\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 738,84,\"ROMAN.TTF\",180,1,8,\""+ batch +"\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 738,357,\"ROMAN.TTF\",180,1,8,\"PO/Bill No. : \"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 610,357,\"ROMAN.TTF\",180,1,8,\""+ pono +"\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 738,45,\"ROMAN.TTF\",180,1,8,\"QTY. : \"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "TEXT 666,45,\"ROMAN.TTF\",180,1,8,\""+ recqty +"\"";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							asd = "PRINT 1,1";
							sw.write(asd);
                            sw.write(System.getProperty("line.separator"));
							
						}
					}
				}
				
				//insert barcode
                Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put("BarcodeId",barcode);
				ht.put("Item",item);
				ht.put("LOC",loc);
				ht.put("BATCH",batch);
				ht.put("UNITMO",uom);
				ht.put("PrintQty",recqty);
				ht.put("PrintId","1");
				ht.put("Status","N");
				ht.put("LABELTYPE",printype);
				ht.put("PRINTWITHBATCH",printwithbatch);
				ht.put("PRINTWITHPOBILL",printwithpobill);
				ht.put("PRINTWITHLOT",printwithlot);
				ht.put(IDBConstants.LOGIN_USER,username);
				ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//				boolean insertflag = new MasterDAO().InsertBarcode(ht);
				new MasterDAO().InsertBarcode(ht);
                //update printqty
				Hashtable htestCond = new Hashtable();
				htestCond.put("PLANT", PLANT);
				htestCond.put("GRNO", grno);
				htestCond.put("LNNO", lnno);
				htestCond.put("ITEM", item);
				htestCond.put("BATCH", batch);
	
				String queryest = "set PRINTQTY= isNull(PRINTQTY,0) + "+recqty;
//				boolean flag = new RecvDetDAO().update(queryest, htestCond, "",PLANT);
				new RecvDetDAO().update(queryest, htestCond, "",PLANT);
                
				_TblControlDAO.updateSeqNo(IConstants.BARCODE,PLANT);
				
			}
			}
			sw.close();
			FileHandling fileHandling=new FileHandling();
			fileHandling.fileDownload(filePath, txtfileName, "text/html", response);
			myObj.delete();
			con = DbBean.getConnection();
		      
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	private void viewProductBarcodeNew(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		String errorval="",PAGE_TYPE="",printtype="";
		try {
			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String username = (String) session.getAttribute("LOGIN_USER");
			String loc="",locdesc="";
			String LabelType="",BarcodeWidth="",BarcodeHeight="",FontSize="",TextAlign="",TextPosition="",DisplayText="";
			String MODEL="",PRINT_WITH_MODEL="",PRINT_DATE="",PRINT_DATE_LABEL="",PRINT_WITH_PLANT="";
			LabelType=StrUtils.fString(request.getParameter("LabelType"));
			BarcodeWidth=StrUtils.fString(request.getParameter("BarcodeWidth"));
			BarcodeHeight=StrUtils.fString(request.getParameter("BarcodeHeight"));
			FontSize=StrUtils.fString(request.getParameter("FontSize"));
			TextAlign=StrUtils.fString(request.getParameter("TextAlign"));
			TextPosition=StrUtils.fString(request.getParameter("TextPosition"));
			DisplayText=StrUtils.fString(request.getParameter("DisplayText"));
			printtype=StrUtils.fString(request.getParameter("printtype"));
			PAGE_TYPE=StrUtils.fString(request.getParameter("PAGE_TYPE"));
			MODEL=StrUtils.fString(request.getParameter("MODEL"));
			PRINT_WITH_MODEL=StrUtils.fString(request.getParameter("PRINT_WITH_MODEL"));
			PRINT_DATE=StrUtils.fString(request.getParameter("PRINT_DATE"));
			PRINT_DATE_LABEL=StrUtils.fString(request.getParameter("PRINT_DATE_LABEL"));
			PRINT_WITH_PLANT=StrUtils.fString(request.getParameter("PRINT_WITH_PLANT"));
			
			int totrecqty =Integer.parseInt(StrUtils.fString(request.getParameter("totrecqty")));
			
			//Insert Barcode Print
			Hashtable ht1 = new Hashtable();
			ht1.put(IDBConstants.PLANT,PLANT);
			ht1.put("LABEL_TYPE",LabelType);
			if(printtype.equalsIgnoreCase("30X25")) {
				ht1.put("LABEL_WIDTH","30");
				ht1.put("LABEL_HEIGHT","25");
			} else if(printtype.equalsIgnoreCase("50X25")) {
				ht1.put("LABEL_WIDTH","50");
				ht1.put("LABEL_HEIGHT","25");
			} else {
				ht1.put("LABEL_WIDTH","100");
				ht1.put("LABEL_HEIGHT","50");	
			}
			ht1.put("BARCODE_WIDTH",BarcodeWidth);
			ht1.put("BARCODE_HEIGHT",BarcodeHeight);
			ht1.put("FONT_SIZE",FontSize);
			ht1.put("TEXT_ALIGN",TextAlign);
			ht1.put("TEXT_POSITION",TextPosition);
			ht1.put("DISPLAY_BARCODE_TEXT",DisplayText);
			ht1.put("PRINT_COUNT","1");
			ht1.put("PRICE_LABEL_TEXT","Price");
			ht1.put("PAGE_TYPE",PAGE_TYPE);
			ht1.put(IDBConstants.LOGIN_USER,username);
			ht1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			new MasterDAO().InsertBarcodePrint(ht1);
			
			for( int i = 0; i < totrecqty; i++ ) {
				
			String BARCODE = StrUtils.fString(request.getParameter("barcodevalue"+i));
			String DESC = StrUtils.fString(request.getParameter("locdescprint"+i));
			String PRICESYMBL = StrUtils.fString(request.getParameter("pricesyblvalue"+i));
			String PRICE = StrUtils.fString(request.getParameter("pricevalue"+i));
				
				//insert barcode
                Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put("BARCODE",BARCODE);
				ht.put("DESCRIPTION",DESC);
				ht.put("PRICESYMBOL",PRICESYMBL);
				ht.put("PRICE",PRICE);
				ht.put("PRINTQTY","1");
				ht.put("STATUS","N");
				ht.put("LABELTYPE",printtype);
				ht.put("PRINT_WITH_MODEL",PRINT_WITH_MODEL);
				ht.put("MODEL",MODEL);
				ht.put("PRINT_DATE_LABEL",PRINT_DATE_LABEL);
				ht.put("PRINT_DATE",PRINT_DATE);
				ht.put("PRINT_WITH_PLANT",PRINT_WITH_PLANT);
				ht.put(IDBConstants.LOGIN_USER,username);
				ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				new MasterDAO().InsertProductBarcode(ht);
			}
			if (fileName.equals("printProductBarcodeModal"))	
				response.sendRedirect("../product/summary?result=Label Generated Successfully");
			else if (fileName.equals("printProductBarcodeModalSales"))	
				response.sendRedirect("../salesorder/salessummary?result=Label Generated Successfully");
			else if (fileName.equals("printMultiProductBarcode"))	
			response.sendRedirect("../inhouse/genmultiproductbarcode?msg=Label Generated Successfully");
			else if (fileName.equals("printProductBarcodeModalINV"))	
				response.sendRedirect("../inventory/inventorysummary?result=Label Generated Successfully");
				else
		response.sendRedirect("../inhouse/genproductbarcode?msg=Label Generated Successfully");
		
	} catch (Exception e) {
		e.printStackTrace();
		errorval=e.toString();
	} finally {
		DbBean.closeConnection(con);
	}
	
	try {
	if(!errorval.equalsIgnoreCase(""))
		if (fileName.equals("printProductBarcodeModal"))	
			response.sendRedirect("../product/summary?msg="+errorval);
			else
		response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype+"&result="+errorval);
	} catch (Exception ex){}
}
	
	private void SaveMultipo (HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
		Connection con = null;
		String errorval="",PAGE_TYPE="",printtype="";
		TblControlDAO _TblControlDAO = new TblControlDAO();
		MultiPoEstHdr multiPoEstHdr = new MultiPoEstHdr();
		MultiPoEstDet multiPoEstDet = new MultiPoEstDet();
		MultiPoEstHdrDAO multiPoEstHdrDAO = new MultiPoEstHdrDAO();
		multiPoEstDetDAO MultiPoEstDetDAO = new multiPoEstDetDAO();
		HttpSession session = request.getSession();
		String PLANT = (String) session.getAttribute("PLANT");
		String username = (String) session.getAttribute("LOGIN_USER");
		String POMULTIESTNO = _TblControlDAO.getNextOrder(PLANT, username, IConstants.MULTIPOEST);
		boolean IsaddedHdr = false;
		boolean IsUpdate = false;
		String POITEM="",POITEMDESC="",POvendname="",POCURRENCY="",POCURRENCYUSEQT="",POUOM="",POQTY="",POPRICE="",POAMT="",POSUPID="";
		try {
			
			POITEM=StrUtils.fString(request.getParameter("ITEM_BARCODE"));
			POITEMDESC=StrUtils.fString(request.getParameter("ITEM_DESC_BARCODE"));
			POvendname=StrUtils.fString(request.getParameter("POvendname"));
			POCURRENCY=StrUtils.fString(request.getParameter("CURRENCY"));
			POCURRENCYUSEQT=StrUtils.fString(request.getParameter("CURRENCYUSEQT"));
			POUOM=StrUtils.fString(request.getParameter("UOM"));
			POQTY=StrUtils.fString(request.getParameter("POQTY"));
			POPRICE=StrUtils.fString(request.getParameter("POPRICE"));
			POAMT=StrUtils.fString(request.getParameter("POAMT"));
			POSUPID=StrUtils.fString(request.getParameter("POSUPID"));
			
			String collectionDate=DateUtils.getDate();
			String collectionTime=DateUtils.getTimeHHmm();
			String gst = new selectBean().getGST("PURCHASE",PLANT);
			 float gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
			 if(gstVatValue==0f){gst="0.000";}
			 else{gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");}
			
			
			int HdrId=0,lnno=0;
			MultiPoEstHdr poheader = multiPoEstHdrDAO.getPoHdrByDraft(PLANT, "","DRAFT");
			if(poheader.getPOMULTIESTNO()!=null){
				POMULTIESTNO = poheader.getPOMULTIESTNO();
				HdrId = poheader.getID();
				
				MultiPoEstDet podetail = MultiPoEstDetDAO.getPoDetByPonoItem(PLANT, POMULTIESTNO,POITEM);
				if(podetail.getITEM()!=null){
					lnno = podetail.getPOMULTIESTLNNO();
					HdrId = podetail.getID();
					
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", PLANT);
					htCondition.put("POMULTIESTNO", POMULTIESTNO);
					htCondition.put("ITEM", POITEM);
					htCondition.put("ID", String.valueOf(HdrId));
					double cost = (Double.valueOf((String)POPRICE)/Double.valueOf((String)POCURRENCYUSEQT));
					String updatepoestDet = "set  CustCode='"+POSUPID+"',CustName='"+POvendname+"',CURRENCYID='"+POCURRENCY+"',CURRENCYUSEQT='"+Double.valueOf(POCURRENCYUSEQT)+"',"
							+ "UNITMO='"+POUOM+"',QTYOR='"+Double.valueOf(POQTY)+"',UNITCOST='"+cost+"',"
							+ "UPAT='"+DateUtils.getDateTime()+"',  UPBY='"+username+"' ";
					boolean insertFlag = MultiPoEstDetDAO.updatepo(updatepoestDet, htCondition,"");
					IsUpdate = true;
				}else {
					Hashtable htData = new Hashtable();
					String detquery="SELECT TOP 1 POMULTIESTLNNO as lnno FROM "+PLANT+"_PO_MULTI_ESTDET WHERE POMULTIESTNO = '"+POMULTIESTNO+"' ORDER BY POMULTIESTLNNO DESC";
//					String detquery="SELECT Sum(POMULTIESTLNNO) as lnno FROM "+PLANT+"_PO_MULTI_ESTDET WHERE POMULTIESTNO='"+POMULTIESTNO+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(detquery,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String polnno = (String) mst.get("lnno");
		        	  lnno = Integer.valueOf(polnno);
		        	  lnno = lnno+1;
		        	  }
				MultiPoEstDet multiPoEstdet = new MultiPoEstDet();
				multiPoEstdet.setPLANT(PLANT);
				multiPoEstdet.setPOMULTIESTLNNO(lnno);
				multiPoEstdet.setPOMULTIESTNO(POMULTIESTNO);
				multiPoEstdet.setITEM(POITEM);
				multiPoEstdet.setTRANDATE(collectionDate);
				multiPoEstdet.setCustName(POvendname);
				multiPoEstdet.setCustCode(POSUPID);
				multiPoEstdet.setCURRENCYID(POCURRENCY);
				multiPoEstdet.setCURRENCYUSEQT(Double.valueOf(POCURRENCYUSEQT));
				multiPoEstdet.setTAXTREATMENT("GST Registered");
				multiPoEstdet.setUNITCOST((Double.valueOf((String)POPRICE)/Double.valueOf((String)POCURRENCYUSEQT)));
				multiPoEstdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)POQTY)));
				multiPoEstdet.setUNITMO((String)POUOM);
				multiPoEstdet.setEXPIREDATE("");
				multiPoEstdet.setDISCOUNT(Double.valueOf((String)"0"));
				multiPoEstdet.setUNITCOST_AOD(0.0);
				multiPoEstdet.setDISCOUNT_TYPE((String)POCURRENCY);
				multiPoEstdet.setACCOUNT_NAME((String)"Inventory Asset");
				multiPoEstdet.setTAX_TYPE("");
				multiPoEstdet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(PLANT, (String)POITEM)));
				multiPoEstdet.setUSERFLD2(multiPoEstHdr.getJobNum());
				multiPoEstdet.setUSERFLD3(multiPoEstdet.getCustName());
				multiPoEstdet.setCURRENCYUSEQT(Double.valueOf((String)POCURRENCYUSEQT));
				multiPoEstdet.setPRODGST(0);
				multiPoEstdet.setCOMMENT1((String) POSUPID+"-"+(String) POCURRENCY+"-"+(String) POCURRENCYUSEQT);
				multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
				multiPoEstdet.setLNSTAT("N");
				multiPoEstdet.setItemDesc(POITEMDESC);
				multiPoEstdet.setCRBY(username);
				multiPoEstdet.setCRAT(DateUtils.getDateTime());
				multiPoEstdet.setCOMMENT1("");
				MultiPoEstDetDAO.addMultiPoEstDet(multiPoEstdet);
				}
				
			}else {
				multiPoEstHdr.setPLANT(PLANT);
				multiPoEstHdr.setPOMULTIESTNO(POMULTIESTNO);
				multiPoEstHdr.setSTATUS("N");		
				multiPoEstHdr.setORDERTYPE("");		
				multiPoEstHdr.setDELDATE("");		
				multiPoEstHdr.setJobNum("");		
				multiPoEstHdr.setCollectionDate(collectionDate);
				multiPoEstHdr.setCollectionTime(collectionTime);
				multiPoEstHdr.setRemark1("");		
				multiPoEstHdr.setRemark2("");		
				multiPoEstHdr.setSHIPPINGID("");		
				multiPoEstHdr.setSHIPPINGCUSTOMER("");		
				multiPoEstHdr.setINBOUND_GST(Double.valueOf(gst));		
				multiPoEstHdr.setSTATUS_ID("NOT PAID");		
				multiPoEstHdr.setREMARK3("");		
				multiPoEstHdr.setINCOTERMS("");		
				multiPoEstHdr.setPAYMENTTYPE("");		
				multiPoEstHdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));		
				multiPoEstHdr.setPURCHASE_LOCATION("");		
				multiPoEstHdr.setORDER_STATUS("DRAFT");		
				multiPoEstHdr.setREVERSECHARGE(Short.valueOf("0"));		
				multiPoEstHdr.setGOODSIMPORT(Short.valueOf("0"));		
				multiPoEstHdr.setCRAT(DateUtils.getDateTime());		
				multiPoEstHdr.setCRBY(username);
				multiPoEstHdr.setLOCALEXPENSES(0.0);
				multiPoEstHdr.setISTAXINCLUSIVE(Short.valueOf("0"));		
				multiPoEstHdr.setTAXID(0);		
				multiPoEstHdr.setPROJECTID(0);		
				multiPoEstHdr.setTRANSPORTID(0);		
				multiPoEstHdr.setPAYMENT_TERMS("");		
				multiPoEstHdr.setEMPNO("");		
				multiPoEstHdr.setSAMEASEXPDATE(Short.valueOf("0"));		
				multiPoEstHdr.setISPRODUCTASSIGNEDSUPPLIER(Short.valueOf("0"));		
				IsaddedHdr = multiPoEstHdrDAO.addMultiPoEstHdr(multiPoEstHdr);
				
				if(IsaddedHdr) {
					new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(PLANT,IConstants.MULTIPOEST,"PM", multiPoEstHdr.getPOMULTIESTNO());
					MultiPoEstDet multiPoEstdet = new MultiPoEstDet();
					multiPoEstdet.setPLANT(PLANT);
					multiPoEstdet.setPOMULTIESTLNNO(1);
					multiPoEstdet.setPOMULTIESTNO(POMULTIESTNO);
					multiPoEstdet.setITEM(POITEM);
					multiPoEstdet.setTRANDATE(collectionDate);
					multiPoEstdet.setCustName(POvendname);
					multiPoEstdet.setCustCode(POSUPID);
					multiPoEstdet.setCURRENCYID(POCURRENCY);
					multiPoEstdet.setCURRENCYUSEQT(Double.valueOf(POCURRENCYUSEQT));
					multiPoEstdet.setTAXTREATMENT("GST Registered");
					multiPoEstdet.setUNITCOST((Double.valueOf((String)POPRICE)/Double.valueOf((String)POCURRENCYUSEQT)));
					multiPoEstdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)POQTY)));
					multiPoEstdet.setUNITMO((String)POUOM);
					multiPoEstdet.setEXPIREDATE("");
					multiPoEstdet.setDISCOUNT(Double.valueOf((String)"0"));
					multiPoEstdet.setUNITCOST_AOD(0.0);
					multiPoEstdet.setDISCOUNT_TYPE((String)POCURRENCY);
					multiPoEstdet.setACCOUNT_NAME((String)"Inventory Asset");
					multiPoEstdet.setTAX_TYPE("");
					multiPoEstdet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(PLANT, (String)POITEM)));
					multiPoEstdet.setUSERFLD2(multiPoEstHdr.getJobNum());
					multiPoEstdet.setUSERFLD3(multiPoEstdet.getCustName());
					multiPoEstdet.setCURRENCYUSEQT(Double.valueOf((String)POCURRENCYUSEQT));
					multiPoEstdet.setPRODGST(0);
					multiPoEstdet.setCOMMENT1((String) POSUPID+"-"+(String) POCURRENCY+"-"+(String) POCURRENCYUSEQT);
					multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
					multiPoEstdet.setLNSTAT("N");
					multiPoEstdet.setItemDesc(POITEMDESC);
					multiPoEstdet.setCRBY(username);
					multiPoEstdet.setCRAT(DateUtils.getDateTime());
					multiPoEstdet.setCOMMENT1("");
					MultiPoEstDetDAO.addMultiPoEstDet(multiPoEstdet);
				}
			}
			
//			Hashtable ht = new Hashtable();
//			ht.put(IDBConstants.PLANT, plant);
//			ht.put("POMULTIESTNO", orderno);
//			if (new MultiPoEstHdrDAO().isExisit(ht)) {
//			}
			
//				response.sendRedirect("../inhouse/genproductbarcode?msg=Label Generated Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			errorval=e.toString();
		} finally {
			DbBean.closeConnection(con);
		}
		
		try {
			String res = "";
			if(IsUpdate)
				res = "Multi Purchase Estimate "+POMULTIESTNO+" The Product ["+POITEM+"] is Updated Successfully";
			else
				res = "Multi Purchase Estimate "+POMULTIESTNO+" The Product ["+POITEM+"] is Created Successfully";
				
//			if(IsaddedHdr) 
//				res = "Multi Purchase Estimate ("+POMULTIESTNO+") Created Successfully For Product  "+POITEM+"";
//			else
//				res = "Multi Purchase Estimate ("+POMULTIESTNO+") Updated Successfully For Product  "+POITEM+"";
			
			if(errorval.equalsIgnoreCase(""))
				if (fileName.equals("SaveMultipur"))	
//					response.sendRedirect("../purchaseestimate/multipoestdetail?POMULTIESTNO="+POMULTIESTNO+" ");
					response.sendRedirect("../salesorder/reordersummary?result="+res+" ");
				else
					response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype+"&result="+errorval);
		} catch (Exception ex){}
	}

	private void viewManualBarcodeNew(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		String errorval="",PAGE_TYPE="",printtype="";
		try {
			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String username = (String) session.getAttribute("LOGIN_USER");
			String loc="",locdesc="";
			String LabelType="",BarcodeWidth="",BarcodeHeight="",FontSize="",TextAlign="",TextPosition="",DisplayText="";
			
			LabelType=StrUtils.fString(request.getParameter("LabelType"));
			BarcodeWidth=StrUtils.fString(request.getParameter("BarcodeWidth"));
			BarcodeHeight=StrUtils.fString(request.getParameter("BarcodeHeight"));
			FontSize=StrUtils.fString(request.getParameter("FontSize"));
			TextAlign=StrUtils.fString(request.getParameter("TextAlign"));
			TextPosition=StrUtils.fString(request.getParameter("TextPosition"));
			DisplayText=StrUtils.fString(request.getParameter("DisplayText"));
			printtype=StrUtils.fString(request.getParameter("printtype"));
			PAGE_TYPE=StrUtils.fString(request.getParameter("PAGE_TYPE"));
			
			int totrecqty =Integer.parseInt(StrUtils.fString(request.getParameter("totrecqty")));
			
			//Insert Barcode Print
			Hashtable ht1 = new Hashtable();
			ht1.put(IDBConstants.PLANT,PLANT);
			ht1.put("LABEL_TYPE",LabelType);
			if(printtype.equalsIgnoreCase("50X25")) {
				ht1.put("LABEL_WIDTH","50");
				ht1.put("LABEL_HEIGHT","25");
			} else {
				ht1.put("LABEL_WIDTH","100");
				ht1.put("LABEL_HEIGHT","50");	
			}
			ht1.put("BARCODE_WIDTH",BarcodeWidth);
			ht1.put("BARCODE_HEIGHT",BarcodeHeight);
			ht1.put("FONT_SIZE",FontSize);
			ht1.put("TEXT_ALIGN",TextAlign);
			ht1.put("TEXT_POSITION",TextPosition);
			ht1.put("DISPLAY_BARCODE_TEXT",DisplayText);
			ht1.put("PRINT_COUNT","1");
			ht1.put("PRICE_LABEL_TEXT","Price");
			ht1.put("PAGE_TYPE",PAGE_TYPE);
			ht1.put(IDBConstants.LOGIN_USER,username);
			ht1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			new MasterDAO().InsertBarcodePrint(ht1);
			
			for( int i = 0; i < totrecqty; i++ ) {
				
				String BARCODE = StrUtils.fString(request.getParameter("barcodevalue"+i));
				String DESC = StrUtils.fString(request.getParameter("locdescprint"+i));
				
				//insert barcode
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put("BARCODE",BARCODE);
				ht.put("DESCRIPTION",DESC);
				ht.put("PRINTQTY","1");
				ht.put("STATUS","N");
				ht.put("LABELTYPE",printtype);
				ht.put(IDBConstants.LOGIN_USER,username);
				ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				new MasterDAO().InsertManualBarcode(ht);
			}
			
			//response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype);
			response.sendRedirect("../inhouse/genmanualbarcode?msg=Label Generated Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			errorval=e.toString();
		} finally {
			DbBean.closeConnection(con);
		}
		
		try {
			if(!errorval.equalsIgnoreCase(""))
				response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype+"&result="+errorval);
		} catch (Exception ex){}
	}
	
	private void viewLocBarcodeNew(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		String errorval="",PAGE_TYPE="",printtype="";
		try {
			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String username = (String) session.getAttribute("LOGIN_USER");
			String loc="",locdesc="";
			String LabelType="",BarcodeWidth="",BarcodeHeight="",FontSize="",TextAlign="",TextPosition="",DisplayText="";
			
			LabelType=StrUtils.fString(request.getParameter("LabelType"));
			BarcodeWidth=StrUtils.fString(request.getParameter("BarcodeWidth"));
			BarcodeHeight=StrUtils.fString(request.getParameter("BarcodeHeight"));
			FontSize=StrUtils.fString(request.getParameter("FontSize"));
			TextAlign=StrUtils.fString(request.getParameter("TextAlign"));
			TextPosition=StrUtils.fString(request.getParameter("TextPosition"));
			DisplayText=StrUtils.fString(request.getParameter("DisplayText"));
			printtype=StrUtils.fString(request.getParameter("printtype"));
			PAGE_TYPE=StrUtils.fString(request.getParameter("PAGE_TYPE"));
			
			//Insert Barcode Print
			Hashtable ht1 = new Hashtable();
			ht1.put(IDBConstants.PLANT,PLANT);
			ht1.put("LABEL_TYPE",LabelType);
			if(printtype.equalsIgnoreCase("50X25")) {
				ht1.put("LABEL_WIDTH","50");
				ht1.put("LABEL_HEIGHT","25");
			} else {
				ht1.put("LABEL_WIDTH","100");
				ht1.put("LABEL_HEIGHT","50");	
			}
			ht1.put("BARCODE_WIDTH",BarcodeWidth);
			ht1.put("BARCODE_HEIGHT",BarcodeHeight);
			ht1.put("FONT_SIZE",FontSize);
			ht1.put("TEXT_ALIGN",TextAlign);
			ht1.put("TEXT_POSITION",TextPosition);
			ht1.put("DISPLAY_BARCODE_TEXT",DisplayText);
			ht1.put("PRINT_COUNT","1");
			ht1.put("PRICE_LABEL_TEXT","Price");
			ht1.put("PAGE_TYPE",PAGE_TYPE);
			ht1.put(IDBConstants.LOGIN_USER,username);
			ht1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			new MasterDAO().InsertBarcodePrint(ht1);
			
		//response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype);
			response.sendRedirect("../inhouse/genlocationbarcode?msg=Label Generated Successfully");
		
	} catch (Exception e) {
		e.printStackTrace();
		errorval=e.toString();
	} finally {
		DbBean.closeConnection(con);
	}
	
	try {
	if(!errorval.equalsIgnoreCase(""))
		response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype+"&result="+errorval);
	} catch (Exception ex){}
}
	
	private void viewReceiptBarcodeNew(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		String errorval="",PAGE_TYPE="",printtype="";
		try {
			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String username = (String) session.getAttribute("LOGIN_USER");
			String grno="",description="",barcode="",item="",lnno="",loc="",batch="",recqty="",uom="";
			String printwithlot="",printwithbatch="",printwithpobill="",printwithproduct="";
			String LabelType="",BarcodeWidth="",BarcodeHeight="",FontSize="",TextAlign="",TextPosition="",DisplayText="";
			int totrecqty =0,icount=0;
			
			printwithlot=StrUtils.fString(request.getParameter("printwithlot"));
			printwithbatch=StrUtils.fString(request.getParameter("printwithbatch"));
			printwithpobill=StrUtils.fString(request.getParameter("printwithpobill"));
			printwithproduct=StrUtils.fString(request.getParameter("printwithproduct"));
			
			if (!printwithlot.equalsIgnoreCase("")) {
				printwithlot="1";
			}
			if (!printwithbatch.equalsIgnoreCase("")) {
				printwithbatch="1";
			}			
			if (!printwithpobill.equalsIgnoreCase("")) {
				printwithpobill="1";	
			}
			if (!printwithproduct.equalsIgnoreCase("")) {
				printwithproduct="1";	
			}
			
			LabelType=StrUtils.fString(request.getParameter("LabelType"));
			BarcodeWidth=StrUtils.fString(request.getParameter("BarcodeWidth"));
			BarcodeHeight=StrUtils.fString(request.getParameter("BarcodeHeight"));
			FontSize=StrUtils.fString(request.getParameter("FontSize"));
			TextAlign=StrUtils.fString(request.getParameter("TextAlign"));
			TextPosition=StrUtils.fString(request.getParameter("TextPosition"));
			DisplayText=StrUtils.fString(request.getParameter("DisplayText"));
			printtype=StrUtils.fString(request.getParameter("printtype"));
			PAGE_TYPE=StrUtils.fString(request.getParameter("PAGE_TYPE"));
			
			totrecqty =Integer.parseInt(StrUtils.fString(request.getParameter("totrecqty")));
			icount=Integer.parseInt(StrUtils.fString(request.getParameter("totcount")));
			
			//Insert Barcode Print
			Hashtable ht1 = new Hashtable();
			ht1.put(IDBConstants.PLANT,PLANT);
			ht1.put("LABEL_TYPE",LabelType);
			if(printtype.equalsIgnoreCase("50X25")) {
				ht1.put("LABEL_WIDTH","50");
				ht1.put("LABEL_HEIGHT","25");
			} else {
				ht1.put("LABEL_WIDTH","100");
				ht1.put("LABEL_HEIGHT","50");	
			}
			ht1.put("BARCODE_WIDTH",BarcodeWidth);
			ht1.put("BARCODE_HEIGHT",BarcodeHeight);
			ht1.put("FONT_SIZE",FontSize);
			ht1.put("TEXT_ALIGN",TextAlign);
			ht1.put("TEXT_POSITION",TextPosition);
			ht1.put("DISPLAY_BARCODE_TEXT",DisplayText);
			ht1.put("PRINT_COUNT","1");
			ht1.put("PRICE_LABEL_TEXT","Price");
			ht1.put("PAGE_TYPE",PAGE_TYPE);
			ht1.put(IDBConstants.LOGIN_USER,username);
			ht1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			new MasterDAO().InsertBarcodePrint(ht1);
			
			if (printwithproduct.equalsIgnoreCase("")) {
			
			for( int j = 0; j < totrecqty; j++ ) {
				barcode = StrUtils.fString(request.getParameter("barcodevalue"+j));
				Hashtable htchk = new Hashtable();
				htchk.put(IDBConstants.PLANT,PLANT);
				htchk.put("BarcodeId",barcode);
				boolean chkbarcode = new MasterDAO().isExisitBarcode(htchk,"");
				if(chkbarcode)
				{
					response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype+"&result=Barcode already generated for other item");
				}
			}
			for( int i = 0; i < totrecqty; i++ ) {
				
				barcode = StrUtils.fString(request.getParameter("barcodevalue"+i));
				item = StrUtils.fString(request.getParameter("itemprint"+i));
				loc = StrUtils.fString(request.getParameter("locprint"+i));
				batch = StrUtils.fString(request.getParameter("batchprint"+i));
				grno = StrUtils.fString(request.getParameter("grnoprint"+i));
				lnno = StrUtils.fString(request.getParameter("lnno"+i));
				uom = StrUtils.fString(request.getParameter("uom"+i));
				if (!printwithlot.equalsIgnoreCase(""))
					recqty = StrUtils.fString(request.getParameter("recqtyprint"+i));
				else
					recqty="1";
				
//				brrecqty=recqty;
//				if(brrecqty.contains(".")) {
//				String[] arr=String.valueOf(brrecqty).split("\\.");
//			    int frval =Integer.parseInt(arr[0]); // 1
//			    int brval =Integer.parseInt(arr[1]); // 9
//			    if(brval>0)
//			    	frval=frval+1;
//			    brrecqty= String.valueOf(frval);
//				}
				
				//insert barcode
                Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put("BarcodeId",barcode);
				ht.put("Item",item);
				ht.put("LOC",loc);
				ht.put("BATCH",batch);
				ht.put("UNITMO",uom);
				ht.put("PrintQty","1");
				ht.put("RecvQty",recqty);
				ht.put("PrintId","1");
				ht.put("Status","N");
				ht.put("LABELTYPE",printtype);
				ht.put("PRINTWITHBATCH",printwithbatch);
				ht.put("PRINTWITHPOBILL",printwithpobill);
				ht.put("PRINTWITHLOT",printwithlot);
				ht.put(IDBConstants.LOGIN_USER,username);
				ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				new MasterDAO().InsertBarcode(ht);
				
				new TblControlDAO().updateSeqNo(IConstants.BARCODE,PLANT);
				
                //update printqty
				Hashtable htestCond = new Hashtable();
				htestCond.put("PLANT", PLANT);
				htestCond.put("GRNO", grno);
				htestCond.put("ISNULL(LNNO,'0')", lnno);
				htestCond.put("ITEM", item);
				htestCond.put("BATCH", batch);
	
				String queryest = "set PRINTQTY= isNull(PRINTQTY,0) + "+1;
				new RecvDetDAO().update(queryest, htestCond, "",PLANT);
                
				}
			}
			
			//response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype);
			response.sendRedirect("../inhouse/genreceiptbarcode?msg=Label Generated Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			errorval=e.toString();
		} finally {
			DbBean.closeConnection(con);
		}
		
		try {
		if(!errorval.equalsIgnoreCase(""))
			response.sendRedirect("../inhouse/printbarcode?PAGE_TYPE="+PAGE_TYPE+"&PRINT_TYPE="+printtype+"&result="+errorval);
		} catch (Exception ex){}
		
	}
	
	private void viewEmployeePayslip(HttpServletRequest request, HttpServletResponse response, String fileName, String MailEMPNO) {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String[] chkdEmpNo = request.getParameterValues("chkdEmpNo");
			String from_month = StrUtils.fString(request.getParameter("from_month"));
			String from_year = StrUtils.fString(request.getParameter("from_year"));
			String payment_mode = StrUtils.fString(request.getParameter("payment_mode"));
			String sendby = StrUtils.fString(request.getParameter("SendAs"));
			String pcountry = StrUtils.fString((String) session.getAttribute("COUNTRY"));
			PlantMstUtil pmUtil = new PlantMstUtil();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",COUNTRY_CODE="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno="", EMPNO = "",CWEBSITE="";
			List<JasperPrint> jasperPrintList = new ArrayList<>();
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			String imagePath2="";
			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}

			con = DbBean.getConnection();
			String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(PLANT);
			String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;
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
				CWEBSITE = (String) map.get("WEBSITE");
				companyregno = (String) map.get("companyregnumber");//imtiuen				
				SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
				COUNTRY_CODE=StrUtils.fString((String)map.get("COUNTRY_CODE"));
			}
			
			String signPath="";
			if(SIGNNAME.equalsIgnoreCase("")){
            	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
            }else {
               	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
               	if (!new File(signPath).exists()) {
    				signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
    			}
            }
			if(!sendby.equalsIgnoreCase("Preview")) {
				chkdEmpNo = new String[1];
				chkdEmpNo[0]=MailEMPNO;
			}
			
		 	if(chkdEmpNo!=null)
			{
			 for (int j = 0; j < chkdEmpNo.length; j++) {
				 EMPNO = chkdEmpNo[j];
			 ArrayList movQryList =  new EmployeeDAO().getEmployeeDetails(EMPNO,PLANT,"");
			 if (movQryList.size() > 0) {
			 for(int i =0; i<movQryList.size(); i++) {
			 Map arrCustLine = (Map)movQryList.get(i);
			 String FName =(String)arrCustLine.get("FNAME");
			 String DESIGNATION =(String)arrCustLine.get("DESGINATION");
			 String DATEOFJOINING =(String)arrCustLine.get("DATEOFJOINING");
			 String EMIRATESID =(String)arrCustLine.get("EMIRATESID");
			 int EMP_AGE =Integer.valueOf((String)arrCustLine.get("EMP_AGE"));
			 Map parameters = new HashMap();
			 
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
				String PRINTWITHCOMPANYSIG= "1";
				if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
					signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	                }
				parameters.put("signPath", signPath);
				parameters.put("EMPNO", EMPNO);
				parameters.put("company", PLANT);
				parameters.put("fromAddress_CompanyName", CNAME);
				if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
				{
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
				} else {
					String fromAddress_BlockAddress="";
					if(CADD2.length()>0)
						fromAddress_BlockAddress=CADD2 + ", ";
						parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
						if(CADD1.equals("")) {
							parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
						}else {
							parameters.put("fromAddress_RoadAddress", " "+CADD1 + ",");
						}
						if(CSTATE.equals("")) {
							parameters.put("fromAddress_Country",  CCOUNTRY);
						}else {
							parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
						}
				}
				
				parameters.put("fromAddress_ZIPCode", CZIP);
				parameters.put("fromAddress_TpNo", CTEL);
				parameters.put("fromAddress_FaxNo", CFAX);
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
				parameters.put("fromAddress_RCBNO", CRCBNO);
				parameters.put("fromAddress_UENNO", companyregno);//imtiuen
				parameters.put("fromAddress_Website", CWEBSITE);
				parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
				parameters.put("D_EMPLOYEENAME", FName);
				parameters.put("D_DESIGNATION", DESIGNATION);
				parameters.put("D_DOJ", DATEOFJOINING);
				String lastFourDigits=EMIRATESID;
				String fstDigits="";
				if (EMIRATESID.length() > 4) 
				{
				    lastFourDigits = EMIRATESID.substring(EMIRATESID.length() - 4);
				
				    for(int k = 0; k<(EMIRATESID.length()-4); k++) {
				      fstDigits=fstDigits+"X";
				    }
				}
				parameters.put("D_FINNO", fstDigits+lastFourDigits);
				parameters.put("D_PAYMENT", payment_mode);

				parameters.put("EMPLOYEENAME", "EMPLOYEE NAME");
				parameters.put("DESIGNATION", "DESIGNATION");
				parameters.put("DOJ", "D.O.J");
				parameters.put("FINNO", "NRIC / FIN NO");
				parameters.put("PAYMENT", "PAYMENT");

				parameters.put("EMPLOYEE", "EMPLOYEE");
				parameters.put("EMPLOYER", "EMPLOYER");
				parameters.put("TOTALCONTRIBUTION", "TOTAL");
				if(pcountry.equals("Singapore"))
				parameters.put("CPF_CONTRIBUTION", "CPF CONTRIBUTION");
				else
				parameters.put("CPF_CONTRIBUTION", "PF CONTRIBUTION");
				

				parameters.put("GROSS_SALARY", "GROSS SALARY");
				parameters.put("lblTOTAL", "NET SALARY");
				
				parameters.put("CompanyName", "Employee Signature");
				parameters.put("CompanySig", "General Manager Signature");
				
				parameters.put("Item", "Item");
				parameters.put("Amt", "Amount");
				parameters.put("RCBNO", "GST No.");
				parameters.put("UENNO", "UEN No.");
				parameters.put("PRINTUENNO",  "1");
				parameters.put("OrderHeader", "PAY SLIP - "+from_month+" - "+from_year);

				parameters.put("FItem", "Deductions");
				parameters.put("FAmt", "Amount");
				
				Double DEMP = new Double("0");
				Double DEMPER = new Double("0");
				Double TOT_DEMPE = new Double("0");
				String EMPLOYEE_AGE="";
				if(EMP_AGE > -1 && EMP_AGE < 56)
					EMPLOYEE_AGE="55 and below";
				else if(EMP_AGE > 55 && EMP_AGE < 61)
					EMPLOYEE_AGE="Above 55 to 60";
				else if(EMP_AGE > 60 && EMP_AGE < 66)
					EMPLOYEE_AGE="Above 60 to 65";
				else if(EMP_AGE > 65 && EMP_AGE < 71)
					EMPLOYEE_AGE="Above 65 to 70";
				else
					EMPLOYEE_AGE="Above 70";
					
				ArrayList cpfQryList =  new EmployeeDAO().getCPFCONTRIBUTIONDetails(COUNTRY_CODE,EMPLOYEE_AGE,"");
				 if (movQryList.size() > 0) {
					 for(int l =0; l<cpfQryList.size(); l++) {
					 Map arrCpfLine = (Map)cpfQryList.get(l);
					 DEMP = new Double((String)arrCpfLine.get("EMPLOYEE_WAGE"));
					 DEMPER = new Double((String)arrCpfLine.get("EMPLOYER_WAGE"));
					 TOT_DEMPE = new Double((String)arrCpfLine.get("TOTAL_WAGE"));
					 }
				 }
				
				Double totsal= new Double("0");				
				Double empsal= new Double("0");				
				ArrayList salQryList =  new EmployeeDAO().getEmployeeSalarydetails(EMPNO,PLANT,"");
				 if (salQryList.size() > 0) {
				 for(int n =0; n<salQryList.size(); n++) {
				 Map arrSalLine = (Map)salQryList.get(n);
				 totsal =totsal + Double.valueOf((String)arrSalLine.get("SALARY"));
				 if (arrSalLine.get("ISPAYROLL_BY_BASIC_SALARY").equals("1"))
					 empsal =empsal + Double.valueOf((String)arrSalLine.get("SALARY"));
				 }
				 }
				Double empval = (empsal*DEMP)/100;
				Double emperval = (empsal*DEMPER)/100;
				Double emptotval = (empsal*TOT_DEMPE)/100;
				Double grdtotsal = totsal-empval;
				
				parameters.put("D_EMPLOYEE", empval);
				parameters.put("D_EMPLOYER", emperval);
				parameters.put("D_TOTALCONTRIBUTION", emptotval);
				
				String phrase = StrUtils.addZeroes(grdtotsal,numberOfDecimal);
				Float num = new Float( phrase ) ;
				int dollars = (int)Math.floor( num ) ;
				String[] chkphrase = phrase.split("\\.");
				int cent =Integer.valueOf(chkphrase[1]);
				String TOTALINWORDS = convert( dollars ); 
				if(cent>0)
				TOTALINWORDS =TOTALINWORDS + " and "
				           + convert( cent ) ;
				parameters.put("D_TOTALINWORDS",  "NET SALARY (in words) " + TOTALINWORDS.toUpperCase() +" ONLY");
				
			 JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : " + start + "**********");
				jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
			 }
			 }
			 }
			}
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			exporter.exportReport();

			byte[] bytes = byteArrayOutputStream.toByteArray();
			if (!ajax) {
				if(sendby.equalsIgnoreCase("Preview")){
				response.addHeader("Content-disposition", "inline;filename=Payslip.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
				}else {
					try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Payslip_" + EMPNO + ".pdf")){
						fos.write(bytes);
					}
				}
			}else {
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Payslip_" + EMPNO + ".pdf")){
					fos.write(bytes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	private static final String[] tensNames = {
		    "",
		    " ten",
		    " twenty",
		    " thirty",
		    " forty",
		    " fifty",
		    " sixty",
		    " seventy",
		    " eighty",
		    " ninety"
		  };
	
	private static final String[] numNames = {
		    "",
		    " one",
		    " two",
		    " three",
		    " four",
		    " five",
		    " six",
		    " seven",
		    " eight",
		    " nine",
		    " ten",
		    " eleven",
		    " twelve",
		    " thirteen",
		    " fourteen",
		    " fifteen",
		    " sixteen",
		    " seventeen",
		    " eighteen",
		    " nineteen"
		  };
	
	private static String convertLessThanOneThousand(int number) {
	    String soFar;

	    if (number % 100 < 20){
	      soFar = numNames[number % 100];
	      number /= 100;
	    }
	    else {
	      soFar = numNames[number % 10];
	      number /= 10;

	      soFar = tensNames[number % 10] + soFar;
	      number /= 10;
	    }
	    if (number == 0) return soFar;
	    return numNames[number] + " hundred" + soFar;
	  }
	
	public static String convert(long number) {
	    // 0 to 999 999 999 999
	    if (number == 0) { return "zero"; }

	    String snumber = Long.toString(number);

	    // pad with "0"
	    String mask = "000000000000";
	    DecimalFormat df = new DecimalFormat(mask);
	    snumber = df.format(number);

	    // XXXnnnnnnnnn
	    int billions = Integer.parseInt(snumber.substring(0,3));
	    // nnnXXXnnnnnn
	    int millions  = Integer.parseInt(snumber.substring(3,6));
	    // nnnnnnXXXnnn
	    int hundredThousands = Integer.parseInt(snumber.substring(6,9));
	    // nnnnnnnnnXXX
	    int thousands = Integer.parseInt(snumber.substring(9,12));

	    String tradBillions;
	    switch (billions) {
	    case 0:
	      tradBillions = "";
	      break;
	    case 1 :
	      tradBillions = convertLessThanOneThousand(billions)
	      + " billion ";
	      break;
	    default :
	      tradBillions = convertLessThanOneThousand(billions)
	      + " billion ";
	    }
	    String result =  tradBillions;

	    String tradMillions;
	    switch (millions) {
	    case 0:
	      tradMillions = "";
	      break;
	    case 1 :
	      tradMillions = convertLessThanOneThousand(millions)
	         + " million ";
	      break;
	    default :
	      tradMillions = convertLessThanOneThousand(millions)
	         + " million ";
	    }
	    result =  result + tradMillions;

	    String tradHundredThousands;
	    switch (hundredThousands) {
	    case 0:
	      tradHundredThousands = "";
	      break;
	    case 1 :
	      tradHundredThousands = "one thousand ";
	      break;
	    default :
	      tradHundredThousands = convertLessThanOneThousand(hundredThousands)
	         + " thousand ";
	    }
	    result =  result + tradHundredThousands;

	    String tradThousand;
	    tradThousand = convertLessThanOneThousand(thousands);
	    result =  result + tradThousand;

	    // remove extra spaces!
	    return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	  }

	
	protected void viewPOReport(HttpServletRequest request, HttpServletResponse response, String fileName) {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			CurrencyUtil currUtil = new CurrencyUtil();
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			POUtil pOUtil = new POUtil();
			sb.setmLogger(mLogger);
			customerBeanDAO.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			pOUtil.setmLogger(mLogger);
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno="", PONO = "",CWEBSITE="";
			// PONO = StrUtils.fString(request.getParameter("PONO"));
			String PLANT = (String) session.getAttribute("PLANT");
			List<JasperPrint> jasperPrintList = new ArrayList<>();
			String[] chkdPoNo = request.getParameterValues("chkdPoNo");
			if (ajax) {
				chkdPoNo = (String[])request.getAttribute("chkdPoNo");
			}
			// ********To get from & to date receive date range*********************
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String F_GRNO = StrUtils.fString(request.getParameter("GRNO"));
			String dtCondStr = "  and ISNULL(a.recvdate,'')<>'' AND CAST((SUBSTRING(a.recvdate, 7, 4) + '-' + SUBSTRING(a.recvdate, 4, 2) + '-' + SUBSTRING(a.recvdate, 1, 2)) AS date)";
			String sCondition = "", fdate = "", tdate = "";
			;
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

			/*
			 * Hashtable htCond = new Hashtable(); htCond.put("PLANT", PLANT);
			 * htCond.put("PONO", PONO); String query =
			 * "jobNum,remark1,custCode,isnull(inbound_Gst,0) as inbound_Gst,isnull(collectionDate,'') as date"
			 * ; ArrayList arrayRemark1 = new ArrayList(); arrayRemark1 =
			 * pOUtil.getPoHdrDetails(query, htCond); Map dataMap = (Map)
			 * arrayRemark1.get(0);
			 */

//			//String SysDate = DateUtils.getDate();
//			Date sysTime1 = new Date(System.currentTimeMillis());
			// //Date cDt = new Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;
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
				CWEBSITE = (String) map.get("WEBSITE");
				companyregno = (String) map.get("companyregnumber");//imtiuen
			}
//			//java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
			for (int i = 0; i < chkdPoNo.length; i++) {
//				Get all GRNOs for this PO from RECVDET table added on 23.5.20 by azees
				String query = "DISTINCT isnull(grno, '') as GRNO, ISNULL(recvdate,'') as RECVDATE ";
				String jasperFileName = fileName;
				if (request.getParameter("printwithgrno") != null || request.getAttribute("printwithgrno") != null) {
					String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Check Company Industry
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
						jasperFileName = "printPOKitchenWITHGRN";
					else
					jasperFileName = "printPOWITHGRN";
				}
				PONO = chkdPoNo[i];
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("PONO", PONO);
				if(F_GRNO.length()>0)
					htCond.put("GRNO", F_GRNO);
				ArrayList alRecvDetail = new ArrayList();
				if (request.getParameter("printwithgrno") != null || request.getAttribute("printwithgrno") != null) {
					//alRecvDetail = pOUtil.getIBRecvList(query, htCond, " AND grno!=''");
					alRecvDetail = pOUtil.getIBRecvList(query, htCond, " and (grno!='' or  grno is not null) "+sCondition+" ORDER BY GRNO DESC ");
				}
				if(F_GRNO.length()>0) {
					htCond.remove("GRNO");
				}
				
				for(int j = 0; j < ((request.getParameter("printwithgrno") != null || request.getAttribute("printwithgrno") != null) ? alRecvDetail.size() : 1); j ++) {
					String GRNO = "", RECVDATE="";
					if (request.getParameter("printwithgrno") != null || request.getAttribute("printwithgrno") != null) {
						GRNO = ((Map)alRecvDetail.get(j)).get("GRNO").toString();
						RECVDATE = ((Map)alRecvDetail.get(j)).get("RECVDATE").toString();						
					}
				query = "isnull(jobNum,'') as jobNum,isnull(remark1,'') as remark1,isnull(EMPNO,'') as EMPNO,"
						//+ "custCode,isnull(inbound_Gst,0) as inbound_Gst,isnull(collectionDate + ' ' + LEFT(CollectionTime, 2)+':'+RIGHT(CollectionTime, 2),'') as date,"
						+ "custCode,isnull(inbound_Gst,0) as inbound_Gst,isnull(collectionDate,'') as date,"
						+ "isnull(deldate,'') as deldate," + "isnull(orderdiscount,0) orderdiscount,"
						+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_pohdr.PROJECTID),'') PROJECT_NAME,"
						+ "isnull(shippingcost,0) shippingcost,isnull(CRBY,'') as CRBY," + "isnull(incoterms,'') incoterms";
				ArrayList arrayRemark1 = new ArrayList();
				arrayRemark1 = pOUtil.getPoHdrDetails(query, htCond);
				Map dataMap = (Map) arrayRemark1.get(0);
				ArrayList arrCust = customerBeanDAO.getVendorDetailsForPO(PLANT, PONO);
				String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", 
						 SHPUNITNO = "", SHPBUILDING = "",  SHPCITY = "", SHPSTATE = "",sEmpno="",
						SHPCOUNTRY = "", SHPPOSTALCODE = "", DATAORDERDISCOUNT = "", DATASHIPPINGCOST = "",
						DATAINCOTERMS = "",SHPSTREET = "",PROJECT_NAME=""/*, SHPFAX = "", SHPEMAIL = ""*/;
				if (arrCust.size() > 0) {
//					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sState = (String) arrCust.get(22);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
					String sContactName = (String) arrCust.get(8);
//					String sDesgination = (String) arrCust.get(9);
					String sTelNo = (String) arrCust.get(10);
//					String sHpNo = (String) arrCust.get(11);
					String sEmail = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sRemarks = (String) arrCust.get(14);
					String sAddr4 = (String) arrCust.get(15);
//					String sIsActive = (String) arrCust.get(16);
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
					String orderRemarks3 = (String) arrCust.get(20);
					String SHIPPINGID = (String) arrCust.get(23);
					String sRcbno = (String) arrCust.get(24);
					String suenno = (String) arrCust.get(25);//imtiuen
					// get shipping details from shipping master table
					ArrayList arrShippingDetails = _masterUtil.getInboundShippingDetails(PONO, SHIPPINGID, PLANT);
					ArrayList arrShippingvendDetails = _masterUtil.getInboundShippingDetailsVendmst(PONO, SHIPPINGID, PLANT);
					Map parameters = new HashMap();
					if (arrShippingDetails.size() > 0) {
						parameters.put("shipToId", SHIPPINGID);
						SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
						SHPCONTACTNAME = (String) arrShippingDetails.get(2);
						SHPTELEPHONE = (String) arrShippingDetails.get(3);
						SHPHANDPHONE = (String) arrShippingDetails.get(4);
//						SHPFAX = (String) arrShippingDetails.get(5);
						//SHPEMAIL = (String) arrShippingDetails.get(6);
						SHPUNITNO = (String) arrShippingDetails.get(7);
						SHPBUILDING = (String) arrShippingDetails.get(8);
						SHPSTREET = (String) arrShippingDetails.get(9);
						SHPCITY = (String) arrShippingDetails.get(10);
						SHPSTATE = (String) arrShippingDetails.get(11);
						SHPCOUNTRY = (String) arrShippingDetails.get(12);
						SHPPOSTALCODE = (String) arrShippingDetails.get(13);
					} else {
//						parameters.put("shipToId", "");
						if (arrShippingvendDetails.size() > 0) {
							parameters.put("shipToId", SHIPPINGID);
							SHPCUSTOMERNAME = (String) arrShippingvendDetails.get(1);
							SHPCONTACTNAME = (String) arrShippingvendDetails.get(2);
							SHPTELEPHONE = (String) arrShippingvendDetails.get(3);
							SHPHANDPHONE = (String) arrShippingvendDetails.get(4);
//							SHPFAX = (String) arrShippingDetails.get(5);
//							SHPEMAIL = (String) arrShippingDetails.get(6);
							SHPUNITNO = (String) arrShippingvendDetails.get(7);
							SHPBUILDING = (String) arrShippingvendDetails.get(8);
							SHPSTREET = (String) arrShippingvendDetails.get(9);
							SHPCITY = (String) arrShippingvendDetails.get(10);
							SHPSTATE = (String) arrShippingvendDetails.get(11);
							SHPCOUNTRY = (String) arrShippingvendDetails.get(12);
							SHPPOSTALCODE = (String) arrShippingvendDetails.get(13);
						}else {
							parameters.put("shipToId", "");
						}
					}

					// if(orderRemarks.length()>0) orderRemarks = "Order Remarks : "+orderRemarks;
					String orderType = new PoHdrDAO().getOrderTypeForPO(PLANT, PONO);
					// Customer Details

					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("inboundOrderNo", PONO);
					// parameters.put("orderRemarks", orderRemarks);
					parameters.put("company", PLANT);
					parameters.put("taxInvoiceTo_CompanyName", sCustName);
					parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
					if (sState.equals("")) {
						parameters.put("taxInvoiceTo_Country",sCountry);
					}else {
						parameters.put("taxInvoiceTo_Country", sState + "\n" + sCountry);
					}
					parameters.put("taxInvoiceTo_ZIPCode", sZip);
					parameters.put("taxInvoiceTo_AttentionTo", sContactName);
					parameters.put("taxInvoiceTo_CCTO", "");
					parameters.put("taxInvoiceTo_Telno", sTelNo);
					parameters.put("taxInvoiceTo_Fax", sFax);
					// parameters.put("SupRemarks", sRemarks);
					parameters.put("taxInvoiceTo_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					PoHdrDAO poHdrDAO= new PoHdrDAO();
					PoHdr poheader = poHdrDAO.getPoHdrByPono(PLANT, PONO);
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(poheader.getTRANSPORTID() > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, poheader.getTRANSPORTID());
					}
					String paymentterms = "";
					paymentterms = poheader.getPAYMENT_TERMS();
					parameters.put("payterms", paymentterms);	
					parameters.put("trans", transportmode);
					//end

					parameters.put("STo_Addr1", SHPUNITNO);
					parameters.put("STo_Addr2", SHPBUILDING);
					parameters.put("STo_Addr3", SHPSTREET);
					parameters.put("STo_City", SHPCITY);
					if (SHPSTATE.equals("")) {
					parameters.put("STo_Country", SHPCOUNTRY);
					}else {
					parameters.put("STo_Country", SHPSTATE + "\n" + SHPCOUNTRY);	
					}
					parameters.put("STo_ZIP", SHPPOSTALCODE);
					parameters.put("STo_Telno", SHPTELEPHONE);
					parameters.put("SHPHANDPHONE", SHPHANDPHONE);
					if (SHPCONTACTNAME.length() > 0)
						parameters.put("STo_Telno", "Tel: " + SHPTELEPHONE);
					else
						parameters.put("STo_Telno", SHPTELEPHONE);
					if (SHPCUSTOMERNAME.length() > 0) {
						parameters.put("STo_AttentionTo", "Attn: " + SHPCONTACTNAME);
					} else {
						parameters.put("STo_AttentionTo", SHPCONTACTNAME);
					}

					/**/
					if (SHPHANDPHONE.length() > 0) {
						parameters.put("SHPHANDPHONE", "HP: " + SHPHANDPHONE);
					} else {
						parameters.put("SHPHANDPHONE", SHPHANDPHONE);
					}
					/**/
					
					// Company Details
					String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
//					parameters.put("fromAddress_CompanyName", CNAME);
//					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//					{
//					parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//					parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//					if(CSTATE.equals("")) {
//						parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//					}else {
//					parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//					}
//					} else {
//						String fromAddress_BlockAddress="";
//						if(CADD2.length()>0)
//							fromAddress_BlockAddress=CADD2 + ",";
//						parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//						parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//						parameters.put("fromAddress_Country", CCOUNTRY);
//					}
					
					parameters.put("fromAddress_CompanyName", CNAME);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
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
					} else {
						String fromAddress_BlockAddress="";
						if(CADD2.length()>0)
							fromAddress_BlockAddress=CADD2 + ", ";
							parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
							if(CADD1.equals("")) {
								parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
							}else {
								parameters.put("fromAddress_RoadAddress", " "+CADD1 + ",");
							}
							if(CSTATE.equals("")) {
								parameters.put("fromAddress_Country",  CCOUNTRY);
							}else {
								parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
							}
					}
					
					parameters.put("fromAddress_ZIPCode", CZIP);
					parameters.put("fromAddress_TpNo", CTEL);
					parameters.put("fromAddress_FaxNo", CFAX);
					parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
					parameters.put("fromAddress_ContactPersonMobile", CHPNO);
					parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
					parameters.put("fromAddress_RCBNO", CRCBNO);
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					parameters.put("fromAddress_Website", CWEBSITE);
					parameters.put("currentTime", (String) dataMap.get("date"));
					System.out.println("Referance No :" + (String) dataMap.get("jobNum"));
					parameters.put("referanceNO", (String) dataMap.get("jobNum"));
					parameters.put("InvoiceTerms", "");
					DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					DATAINCOTERMS = dataMap.get("incoterms").toString();
					PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
					sEmpno=dataMap.get("EMPNO").toString();
					Double gst = new Double((String) dataMap.get("inbound_Gst"));
					gst = gst / 100;
					System.out.println("GST : " + gst);
					parameters.put("taxPercentage", gst.doubleValue());
					parameters.put("orderType", orderType);
					// parameters.put("localCurrency", baseCurrency);
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";
					// Get Currency ID
					String currencyid = new PoHdrDAO().getCurrencyID(PLANT, PONO);
					// Get Currency Display
					Hashtable curHash = new Hashtable();
					curHash.put(IDBConstants.PLANT, PLANT);
					curHash.put(IDBConstants.CURRENCYID, currencyid);
					String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
					// get data's for inbound report data config
					if (jasperFileName.equals("printPOWITHBATCH") || jasperFileName.equals("printPOWITHOUTBATCH") || jasperFileName.equals("printPOWITHGRN") || jasperFileName.equals("printPOKitchenWITHGRN")) {
						POUtil poUtil = new POUtil();
						Map m = poUtil.getPOReceiptHdrDetails(PLANT);
//						Map k = poUtil.getPOReceiptInvoiceHdrDetails(PLANT); //imti
						Orientation = (String) m.get("PrintOrientation");
						if (orderType.equals("INBOUND ORDER")) {
							orderDesc = (String) m.get("HDR1");
						} else {
							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
						}
						if (m.get("DISPLAYBYORDERTYPE").equals("1"))
							parameters.put("OrderHeader", orderDesc);
						else
							parameters.put("OrderHeader", (String) m.get("HDR1"));
						// parameters.put("OrderHeader", (String) m.get("HDR1"));
						parameters.put("ToHeader", (String) m.get("HDR2"));
						parameters.put("FromHeader", (String) m.get("HDR3"));
						parameters.put("Date", (String) m.get("DATE"));
						parameters.put("OrderNo", (String) m.get("ORDERNO"));
						parameters.put("RefNo", (String) m.get("REFNO"));
						parameters.put("SoNo", (String) m.get("SONO"));
						parameters.put("Item", (String) m.get("ITEM"));
						parameters.put("Description", (String) m.get("DESCRIPTION"));

						parameters.put("OrderQty", (String) m.get("ORDERQTY"));
						parameters.put("PROJECT",  m.get("PROJECT"));
						parameters.put("ISPROJECT",  m.get("PRINTWITHPROJECT"));
						parameters.put("PRINTUENNO",  m.get("PRINTWITHUENNO"));//imthiuen
						parameters.put("PRINTSUPPLIERUENNO",  m.get("PRINTWITHSUPPLIERUENNO"));//imthiuen
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						parameters.put("trans", transportmode);
						parameters.put("payterms", paymentterms);
						parameters.put("PRINTWITHTRANSPORT_MODE", (String) m.get("PRINTWITHTRANSPORT_MODE"));
						parameters.put("TRANSPORT_MODE", (String) m.get("TRANSPORT_MODE"));
						parameters.put("TERMSDETAILS", (String) m.get("TERMSDETAILS"));
						//end
						
						parameters.put("UOM", (String) m.get("UOM"));
						parameters.put("Footer1", (String) m.get("F1"));
						parameters.put("Footer2", (String) m.get("F2"));
						parameters.put("Footer3", (String) m.get("F3"));
						parameters.put("Footer4", (String) m.get("F4"));
						parameters.put("Footer5", (String) m.get("F5"));
						parameters.put("Footer6", (String) m.get("F6"));
						parameters.put("Footer7", (String) m.get("F7"));
						parameters.put("Footer8", (String) m.get("F8"));
						parameters.put("Footer9", (String) m.get("F9"));
						parameters.put("lblINCOTERM", (String) m.get("INCOTERM"));
						parameters.put("PrdXtraDetails", (String) m.get("PRINTXTRADETAILS"));
						parameters.put("PRINTWITHBRAND", (String) m.get("PRINTWITHBRAND"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) m.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("RecvDateRange", sCondition);
						parameters.put("STo", (String) m.get("SHIPTO"));
						parameters.put("RCBNO", (String) m.get("RCBNO"));
						parameters.put("SUPPLIERRCBNO", (String) m.get("SUPPLIERRCBNO"));
						parameters.put("UENNO", (String) m.get("UENNO"));//imtiuen
						parameters.put("SUPPLIERUENNO", (String) m.get("SUPPLIERUENNO"));//imtiuen
						if (m.get("PSUPREMARKS").equals("1"))
							parameters.put("SupRemarks", sRemarks);
						else
							parameters.put("SupRemarks", "");
						parameters.put("DeliveryDate", (String) m.get("DELIVERYDATE"));
						if (m.get("PRINTEMPLOYEE").equals("1")) {// Author: Azees  Create date: July 10,2021  Description: Employee on Jasper
							parameters.put("Employee", (String) m.get("EMPLOYEE"));
							String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
							parameters.put("EmployeeName", empname);
						} else {
							parameters.put("Employee", "");
							parameters.put("EmployeeName", "");
						}
						if (orderRemarks.length() > 0)
							orderRemarks = (String) m.get("REMARK1") + " : " + orderRemarks;
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) m.get("REMARK2") + " : " + orderRemarks3;
						if (fileName.equals("printPOWITHBATCH"))
							parameters.put("printwithbatch", "yes");
						else
							parameters.put("printwithbatch", "no");
						
						parameters.put("GRNDATELabel", (String) m.get("GRNDATE"));						
						if (request.getParameter("printwithgrno") != null || request.getAttribute("printwithgrno") != null) {
							if (request.getParameter("printwithgrno") != null || request.getAttribute("printwithgrno") != null)
								parameters.put("printwithgrno", "yes");
							parameters.put("GRNOLabel", (String) m.get("GRNO"));
							parameters.put("GRNO", GRNO);
							if (GRNO.length() == 0)
							{
								parameters.put("GRNDATE", "");
							}else{
							parameters.put("GRNDATE", RECVDATE);
							}
							
						}else {
							parameters.put("printwithgrno", "no");
						}
						
						// ship to Address
						parameters.put("lblOrderDiscount",
								(String) m.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ")");
						parameters.put("lblShippingCost",
								(String) m.get("SHIPPINGCOST") + " " + "(" + curDisplay + ")");
						double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
						double doubledatashippingcost = new Double(DATASHIPPINGCOST);
						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
						parameters.put("lblINCOTERM", (String) m.get("INCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						parameters.put("PROJECTNAME", PROJECT_NAME);
						// ship to Address
						if (SHPCUSTOMERNAME.length() > 0) {
							// parameters.put("STo_CustName", "SHIP TO : "+ "\n"+sShipCustName);
							parameters.put("STo_CustName", SHPCUSTOMERNAME);

						} else {
							parameters.put("STo_CustName", SHPCUSTOMERNAME);
						}

						parameters.put("STo_CustName", SHPCUSTOMERNAME);
						parameters.put("STo", (String) m.get("SHIPTO"));
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
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) m.get("PRINTWITHCOMPANYSEAL");
						String PRINTWITHCOMPANYSIG= (String) m.get("PRINTWITHCOMPANYSIG");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						if (Orientation.equals("Portrait")) {
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
							if (jasperFileName.equals("printPOWITHBATCH") || jasperFileName.equals("printPOWITHOUTBATCH")) {
								jasperFileName = "printPOKitchenWITHBATCH";
							}
							jasperPath = DbBean.JASPER_INPUT + "/" + jasperFileName + "Portrait";
						}
						parameters.put("PRINTWITHDELIVERYDATE",  m.get("PRINTWITHDELIVERYDATE"));
						parameters.put("PRODUCTDELIVERYDATE",  m.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  m.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("CompanyDate", (String) m.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) m.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) m.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) m.get("COMPANYSIG"));
						parameters.put("PreBy", (String) m.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						parameters.put("PrintRecmLoc", (String) m.get("PRINTRECMLOC"));

						parameters.put("orderRemarks", orderRemarks);
						parameters.put("orderRemarks3", orderRemarks3);
						parameters.put("DeliveryDate", (String) m.get("DELIVERYDATE"));

						String deldate = StrUtils.fString((String) dataMap.get("deldate"));
						//if (deldate.length() > 0)
//							deldate = deldate;
						parameters.put("DeliveryDt", deldate);

						if (m.get("PSUPREMARKS").equals("1")) {
							parameters.put("SupRemarks", sRemarks);
						} else {
							parameters.put("SupRemarks", "");
						}
						ArrayList arrayposdao = new ArrayList();
						PoDetDAO poDetDAO = new PoDetDAO();	
						arrayposdao = poDetDAO.selectPoDet("ITEM,ItemDesc,UNITCOST ", htCond);
						if(arrayposdao.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						System.out.println("jasperPath : " + jasperPath);
						JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
						long start = System.currentTimeMillis();
						System.out.println("**************" + " Start Up Time : " + start + "**********");
						jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
					}
				}
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
			//response.addHeader("Content-disposition", "attachment;filename=MultiplePO.pdf");
			if (!ajax) {
				response.addHeader("Content-disposition", "inline;filename=MultiplePO.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}else {
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/PO_" + PONO + ".pdf")){
					fos.write(bytes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}

	protected void viewPOInvoiceReport(HttpServletRequest request, HttpServletResponse response,
			String fileName) {

		String jasperName = fileName;
		String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			// CustUtil cUtil = new CustUtil();
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			POUtil pOUtil = new POUtil();
			BillDAO billDAO = new BillDAO();
			PoHdrDAO poHdrDao = new PoHdrDAO();
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			customerBeanDAO.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			pOUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CRCBNO = "",companyregno = "", CEMAIL = "",CWEBSITE = "";
			String PONO = StrUtils.fString(request.getParameter("PONO"));
			String PLANT = (String) session.getAttribute("PLANT");
			String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
			List jasperPrintList = new ArrayList();
			String[] chkdPoNo = request.getParameterValues("chkdPoNo");
			if (ajax) {
				chkdPoNo = (String[])request.getAttribute("chkdPoNo");
			}
			// ********To get from & to date receive date range*********************
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			
			String dtCondStr = "  and ISNULL(a.recvdate,'')<>'' AND CAST((SUBSTRING(a.recvdate, 7, 4) + '-' + SUBSTRING(a.recvdate, 4, 2) + '-' + SUBSTRING(a.recvdate, 1, 2)) AS date)";
			String sCondition = "",sConditionbill = "", fdate = "", tdate = "";
			String dtCondStrbill = "  and ISNULL(BILL_DATE,'')<>'' AND CAST((SUBSTRING(BILL_DATE, 7, 4) + '-' + SUBSTRING(BILL_DATE, 4, 2) + '-' + SUBSTRING(BILL_DATE, 1, 2)) AS date)";
			
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
				sConditionbill=sConditionbill + dtCondStrbill + " >= '" + fdate + "'  ";
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" + tdate + "'  ";
					sConditionbill=sConditionbill + dtCondStrbill + " <get '" + tdate + "'  ";
				}
			} else {
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + "  <= '" + tdate + "'  ";
					sConditionbill=sConditionbill + dtCondStrbill + " <= '" + tdate + "'  ";
				}
			}
			// ********To get from & to date receive date range*********************

			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
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
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			String imagePath2, Orientation = "";
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);

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
//			Date sysTime1 = new Date(System.currentTimeMillis());
			//Date cDt = new Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
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
				CWEBSITE = (String) map.get("WEBSITE");//imtiuen

			}
			for (int i = 0; i < chkdPoNo.length; i++) {
				//	Get all GRNOs for this PO from RECVDET table
				String query = "DISTINCT isnull(grno, '') as GRNO, ISNULL(recvdate,'') as RECVDATE,ISNULL((Select top 1 B.BILL from "+PLANT+"_FINBILLHDR B WHERE B.PONO=A.PONO and B.GRNO=A.GRNO),'') as BILLNO,ISNULL((Select top 1 BILL_DATE from "+PLANT+"_FINBILLHDR B WHERE B.PONO=A.PONO and B.GRNO=A.GRNO),'') as BILLDATE ";
				//azees changes 12.2020
				String printwithbatch="1";
				if(jasperName.equalsIgnoreCase("printPOInvoiceWITHOUTBATCH"))
				{
					String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) { 
					fileName = "printPOKitchenInvoiceWITHBATCH";
					}else {
					fileName="printPOInvoiceWITHBATCH";
					}
					printwithbatch="0";
					
				}
				
				String jasperFileName = fileName;
				if (request.getParameter("printwithgrno") != null || request.getParameter("printwithbillno") != null) {
					String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
						jasperFileName = "printPOKitchenInvoiceWITHBATCHWithGrno";
					else
					jasperFileName += "WithGrno";
				}
				PONO = chkdPoNo[i];
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("PONO", PONO);
				ArrayList alRecvDetail = new ArrayList();
				if (request.getParameter("printwithgrno") != null || request.getParameter("printwithbillno") != null) {
					//alRecvDetail = pOUtil.getIBRecvList(query, htCond, " AND grno!=''");
					alRecvDetail = pOUtil.getIBRecvList(query, htCond, " and (grno!='' or  grno is not null) "+sCondition+" ORDER BY GRNO DESC ");
				}
				
				for(int j = 0; j < ((request.getParameter("printwithgrno") != null  || request.getParameter("printwithbillno") != null) ? alRecvDetail.size() : 1); j ++) {
					String GRNO = "", RECVDATE="",BILLNO="",BILLDATE="";
					if (request.getParameter("printwithgrno") != null || request.getParameter("printwithbillno") != null) {
						GRNO = ((Map)alRecvDetail.get(j)).get("GRNO").toString();
						RECVDATE = ((Map)alRecvDetail.get(j)).get("RECVDATE").toString();
						BILLNO = ((Map)alRecvDetail.get(j)).get("BILLNO").toString();
						BILLDATE = ((Map)alRecvDetail.get(j)).get("BILLDATE").toString();
						//RECVDATE = ((Map)alRecvDetail.get(j)).get("RECVDATE").toString();
						//htCond.put("GRNO", GRNO);
					}
					if (request.getParameter("printwithbillno") != null && BILLNO.isEmpty())
					{
						if(request.getParameter("printwithgrno") == null)
						continue;
					}
					ArrayList albill = new ArrayList();
					String ORDERDISCOUNTTYPE = "",AdjustmentAmount="",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",ISTAXINCLUSIVE="",DATAORDERDISCOUNT = "", DATASHIPPINGCOST = "",
							DISCOUNT="0",DISCOUNT_TYPE="",ISORDERDISCOUNTTAX="",PROJECT_NAME="",sales_location="",sEmpno = "";
							int TAXID=0;
					if (request.getParameter("printwithbillno") != null)
					{
						Hashtable htbill = new Hashtable();
						htbill.put("PLANT", PLANT);
						String querybill="Select isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
								+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_FINBILLHDR.TAXID),'') TAX_TYPE,isnull(EMPNO,'') as EMPNO,"
								+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_FINBILLHDR.PROJECTID),'') PROJECT_NAME,"
								+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_FINBILLHDR.PURCHASE_LOCATION),'') sales_location,"
								+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAXABLE,0) ISSHIPPINGTAX,ISNULL(TAXID,0) TAXID, "
								+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(ORDER_DISCOUNT,0) ELSE (isnull(ORDER_DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
								+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
								+ "ISNULL(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE "
								+ "FROM "+PLANT+"_FINBILLHDR WHERE BILL='"+BILLNO+"' ";
						albill=billDAO.selectForReport(querybill, htbill, "");
						if(albill.size()>0)
						{
							ORDERDISCOUNTTYPE = ((Map)albill.get(0)).get("ORDERDISCOUNTTYPE").toString();
							DISCOUNT_TYPE = ((Map)albill.get(0)).get("DISCOUNT_TYPE").toString();
							ISTAXINCLUSIVE = ((Map)albill.get(0)).get("ISTAXINCLUSIVE").toString();
							TAX_TYPE = ((Map)albill.get(0)).get("TAX_TYPE").toString();
							ISDISCOUNTTAX = ((Map)albill.get(0)).get("ISDISCOUNTTAX").toString();
							ISSHIPPINGTAX = ((Map)albill.get(0)).get("ISSHIPPINGTAX").toString();
							ISORDERDISCOUNTTAX= ((Map)albill.get(0)).get("ISORDERDISCOUNTTAX").toString();
							AdjustmentAmount = ((Map)albill.get(0)).get("ADJUSTMENT").toString();
							DATAORDERDISCOUNT = ((Map)albill.get(0)).get("orderdiscount").toString();
							DATASHIPPINGCOST = ((Map)albill.get(0)).get("shippingcost").toString();
							DISCOUNT = ((Map)albill.get(0)).get("DISCOUNT").toString();
							PROJECT_NAME=((Map)albill.get(0)).get("PROJECT_NAME").toString();
							sales_location=((Map)albill.get(0)).get("sales_location").toString();
							TAXID = Integer.valueOf(((Map)albill.get(0)).get("TAXID").toString());
							sEmpno=((Map)albill.get(0)).get("EMPNO").toString();
						}
					}
					query = "isnull(jobNum,'') as jobNum,isnull(remark1,'') as remark1,isnull(EMPNO,'') as EMPNO,"
							//+ "custCode,isnull(inbound_Gst,0) as inbound_Gst,isnull(collectionDate + ' ' + LEFT(CollectionTime, 2)+':'+RIGHT(CollectionTime, 2),'') as date,"
							+ "custCode,isnull(inbound_Gst,0) as inbound_Gst,isnull(collectionDate,'') as date,ISNULL(TAXID,0) TAXID,"
							+ "isnull(deldate,'') as deldate,CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount," 
							+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ISTAXINCLUSIVE,0) ISTAXINCLUSIVE,"
							+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_pohdr.TAXID),'') TAX_TYPE,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_pohdr.PROJECTID),'') PROJECT_NAME,"
							+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_pohdr.PURCHASE_LOCATION),'') sales_location,"
							+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,STATUS,"
							+ "isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') as PAYMENTTYPE," + "isnull(incoterms,'') incoterms";
					ArrayList arrayRemark1 = new ArrayList();
					arrayRemark1 = pOUtil.getPoHdrDetails(query, htCond);
					Map dataMap = (Map) arrayRemark1.get(0);
					if (request.getParameter("printwithbillno") == null)
					{
						ORDERDISCOUNTTYPE = (String) dataMap.get("ORDERDISCOUNTTYPE").toString();
						ISTAXINCLUSIVE = (String) dataMap.get("ISTAXINCLUSIVE").toString();
						TAX_TYPE = (String) dataMap.get("TAX_TYPE").toString();
						ISSHIPPINGTAX = (String) dataMap.get("ISSHIPPINGTAX").toString();
						ISORDERDISCOUNTTAX= (String) dataMap.get("ISDISCOUNTTAX").toString();
						AdjustmentAmount = (String) dataMap.get("ADJUSTMENT").toString();
						DATAORDERDISCOUNT = (String) dataMap.get("orderdiscount").toString();
						DATASHIPPINGCOST = (String) dataMap.get("shippingcost").toString();
						String POSTATUS = (String) dataMap.get("STATUS").toString();
						if(!POSTATUS.equalsIgnoreCase("C"))
						{
						DATASHIPPINGCOST = poHdrDao.getActualShippingCostForBill(PLANT, PONO);
						if(!ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
							DATAORDERDISCOUNT = poHdrDao.getActualDiscoutForBill(PLANT, PONO);
						}
						PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
						sales_location=dataMap.get("sales_location").toString();
						TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
						sEmpno = dataMap.get("EMPNO").toString();
					}
						PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
						
					ArrayList arrCust = customerBeanDAO.getVendorDetailsForPO(PLANT, PONO);
					String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", 
							SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
							SHPCOUNTRY = "", SHPPOSTALCODE = "", DATAINCOTERMS = "";//SHPFAX = "", SHPEMAIL = "", 
					if (arrCust.size() > 0) {
//						String sCustCode = (String) arrCust.get(0);
						String sCustName = (String) arrCust.get(1);
						String sAddr1 = (String) arrCust.get(2);
						String sAddr2 = (String) arrCust.get(3);
						String sAddr3 = (String) arrCust.get(4);
						String sCountry = (String) arrCust.get(5);
						String sState = (String) arrCust.get(22);
						String sZip = (String) arrCust.get(6);
//						String sCons = (String) arrCust.get(7);
						String sContactName = (String) arrCust.get(8);
//						String sDesgination = (String) arrCust.get(9);
						String sTelNo = (String) arrCust.get(10);
//						String sHpNo = (String) arrCust.get(11);
						String sEmail = (String) arrCust.get(12);
						String sFax = (String) arrCust.get(13);
						String sRemarks = (String) arrCust.get(14);
						String sAddr4 = (String) arrCust.get(15);
						String pays = (String) arrCust.get(38);
//						String sIsActive = (String) arrCust.get(16);
						String orderRemarks = (String) arrCust.get(18);
//						String sPayTerms = (String) arrCust.get(19);
						String orderRemarks3 = (String) arrCust.get(20);
						String SHIPPINGID = (String) arrCust.get(23);
						String sRcbno = (String) arrCust.get(24);
						String suenno = (String) arrCust.get(25);//imtiuen
						// get shipping details from shipping master table
						
						
						ArrayList arrShippingDetails = _masterUtil.getInboundShippingDetails(PONO, SHIPPINGID, PLANT);
						ArrayList arrShippingvendDetails = _masterUtil.getInboundShippingDetailsVendmst(PONO, SHIPPINGID, PLANT);
						Map parameters = new HashMap();
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", SHIPPINGID);
							SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
							SHPCONTACTNAME = (String) arrShippingDetails.get(2);
							SHPTELEPHONE = (String) arrShippingDetails.get(3);
							SHPHANDPHONE = (String) arrShippingDetails.get(4);
//							SHPFAX = (String) arrShippingDetails.get(5);
//							SHPEMAIL = (String) arrShippingDetails.get(6);
							SHPUNITNO = (String) arrShippingDetails.get(7);
							SHPBUILDING = (String) arrShippingDetails.get(8);
							SHPSTREET = (String) arrShippingDetails.get(9);
							SHPCITY = (String) arrShippingDetails.get(10);
							SHPSTATE = (String) arrShippingDetails.get(11);
							SHPCOUNTRY = (String) arrShippingDetails.get(12);
							SHPPOSTALCODE = (String) arrShippingDetails.get(13);
						} else {
//							parameters.put("shipToId", "");
							if (arrShippingvendDetails.size() > 0) {
								parameters.put("shipToId", SHIPPINGID);
								SHPCUSTOMERNAME = (String) arrShippingvendDetails.get(1);
								SHPCONTACTNAME = (String) arrShippingvendDetails.get(2);
								SHPTELEPHONE = (String) arrShippingvendDetails.get(3);
								SHPHANDPHONE = (String) arrShippingvendDetails.get(4);
//								SHPFAX = (String) arrShippingDetails.get(5);
//								SHPEMAIL = (String) arrShippingDetails.get(6);
								SHPUNITNO = (String) arrShippingvendDetails.get(7);
								SHPBUILDING = (String) arrShippingvendDetails.get(8);
								SHPSTREET = (String) arrShippingvendDetails.get(9);
								SHPCITY = (String) arrShippingvendDetails.get(10);
								SHPSTATE = (String) arrShippingvendDetails.get(11);
								SHPCOUNTRY = (String) arrShippingvendDetails.get(12);
								SHPPOSTALCODE = (String) arrShippingvendDetails.get(13);
							}else {
								parameters.put("shipToId", "");
							}
						}

						String orderType = new PoHdrDAO().getOrderTypeForPO(PLANT, PONO);
						// Customer Details

						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);

						parameters.put("inboundOrderNo", PONO);
						// parameters.put("orderRemarks", orderRemarks);
						parameters.put("company", PLANT);
						parameters.put("taxInvoiceTo_CompanyName", sCustName);
						parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
						parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
						if (sState.equals("")) {
							parameters.put("taxInvoiceTo_Country",sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country", sState + "\n" + sCountry);
						}
						parameters.put("taxInvoiceTo_ZIPCode", sZip);
						parameters.put("taxInvoiceTo_AttentionTo", sContactName);
						parameters.put("taxInvoiceTo_CCTO", "");
						parameters.put("taxInvoiceTo_Telno", sTelNo);
						parameters.put("taxInvoiceTo_Fax", sFax);
						// parameters.put("SupRemarks", sRemarks);
						parameters.put("taxInvoiceTo_Email", sEmail);

						parameters.put("STo_Addr1", SHPUNITNO);
						parameters.put("STo_Addr2", SHPBUILDING);
						parameters.put("STo_Addr3", SHPSTREET);
						parameters.put("STo_City", SHPCITY);
						if (SHPSTATE.equals("")) {
						parameters.put("STo_Country", SHPCOUNTRY);
						}else {
						parameters.put("STo_Country", SHPSTATE + "\n" + SHPCOUNTRY);	
						}
						parameters.put("STo_ZIP", SHPPOSTALCODE);
						parameters.put("STo_Telno", SHPTELEPHONE);
						parameters.put("sRCBNO", sRcbno);
						parameters.put("sUENNO", suenno);//imtiuen
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						PoHdrDAO poHdrDAO= new PoHdrDAO();
						PoHdr poheader = poHdrDAO.getPoHdrByPono(PLANT, PONO);
						TransportModeDAO transportmodedao = new TransportModeDAO();
						String transportmode = "";
						if(poheader.getTRANSPORTID() > 0){
							transportmode = transportmodedao.getTransportModeById(PLANT, poheader.getTRANSPORTID());
						}
						String paymentterms = "";
						paymentterms = poheader.getPAYMENT_TERMS();
						parameters.put("payterms", paymentterms);		
						parameters.put("trans", transportmode);
						//end
					
						parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						parameters.put("SHPCUSTOMERNAME", SHPCUSTOMERNAME);
						
						if (SHPTELEPHONE.length() > 0)
							parameters.put("STo_Telno", "Tel: " + SHPTELEPHONE);
						else
							parameters.put("STo_Telno", SHPTELEPHONE);
						if (SHPCONTACTNAME.length() > 0) {
							parameters.put("STo_AttentionTo", "Attn: " + SHPCONTACTNAME);
						} else {
							parameters.put("STo_AttentionTo", SHPCONTACTNAME);
						}
						if (SHPHANDPHONE.length() > 0) {
							parameters.put("SHPHANDPHONE", "HP: " + SHPHANDPHONE);
						} else {
							parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						}
						
						
						// Company Details
						String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
//						parameters.put("fromAddress_CompanyName", CNAME);
//						if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//						{
//						parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//						parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//						if(CSTATE.equals("")) {
//						parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//						}else {
//							parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//						}
//						} else {
//							String fromAddress_BlockAddress="";
//							if(CADD2.length()>0)
//								fromAddress_BlockAddress=CADD2 + ",";
//							parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//							parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//							parameters.put("fromAddress_Country", CCOUNTRY);
//						}
						
						parameters.put("fromAddress_CompanyName", CNAME);
						if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
						{
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
						} else {
							String fromAddress_BlockAddress="";
							if(CADD2.length()>0)
								fromAddress_BlockAddress=CADD2 + ", ";
								parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
								if(CADD1.equals("")) {
									parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
								}else {
									parameters.put("fromAddress_RoadAddress", " "+CADD1 + ",");
								}
								if(CSTATE.equals("")) {
									parameters.put("fromAddress_Country",  CCOUNTRY);
								}else {
									parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
								}
						}
						
						parameters.put("fromAddress_ZIPCode", CZIP);
						parameters.put("fromAddress_TpNo", CTEL);
						parameters.put("fromAddress_FaxNo", CFAX);
						parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
						parameters.put("fromAddress_ContactPersonMobile", CHPNO);
						parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
						parameters.put("fromAddress_RCBNO", CRCBNO);
						parameters.put("fromAddress_UENNO", companyregno);//imtiuen
						parameters.put("fromAddress_Website", CWEBSITE);
						parameters.put("currentTime", (String) dataMap.get("date"));
						System.out.println("Referance No :" + (String) dataMap.get("jobNum"));
						parameters.put("referanceNO", (String) dataMap.get("jobNum"));

						parameters.put("InvoiceTerms", "");
						/*if (request.getParameter("printwithbillno") == null)
						{
						DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
						DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
						}*/						
						DATAINCOTERMS = dataMap.get("incoterms").toString();
						
						String inbgst = (String) dataMap.get("inbound_Gst");
						Double gst = new Double(inbgst);
						/*gst = gst / 100;
						System.out.println("GST : " + gst);
						parameters.put("taxPercentage", gst.doubleValue());*/
						parameters.put("orderType", orderType);
						parameters.put("localCurrency", baseCurrency);

						// get data's for inbound report data config

						OrderTypeDAO ODAO = new OrderTypeDAO();
						String orderDesc = "";
						// Get Currency ID
						String currencyid = new PoHdrDAO().getCurrencyID(PLANT, PONO);
						// Get Currency Display
						Hashtable curHash = new Hashtable();
						curHash.put(IDBConstants.PLANT, PLANT);
						curHash.put(IDBConstants.CURRENCYID, currencyid);
						String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");

						POUtil poUtil = new POUtil();
						Map m = poUtil.getPOReceiptInvoiceHdrDetails(PLANT);
						Orientation = (String) m.get("PrintOrientation");
						System.out.println("PRINTWITHDISCOUNT : " + (String) m.get("PRINTWITHDISCOUNT"));
						if ("1".equals((String) m.get("PRINTWITHDISCOUNT"))) {
							
							boolean isPresent = jasperFileName.indexOf("WithDiscount") != -1 ? true : false;							
							if(isPresent)
							{
							
							}
							else
							{
								jasperFileName += "WithDiscount";
							}
							
						}
						System.out.println("jasperFileName : " + jasperFileName);
						if (SHPCUSTOMERNAME.length() > 0) {
							parameters.put("STo_CustName", (String) m.get("SHIPTO") + " : " + "\n" + SHPCUSTOMERNAME);
						} else {
							parameters.put("STo_CustName", SHPCUSTOMERNAME);
						}
						if (orderType.equals("INBOUND ORDER")) {
							orderDesc = (String) m.get("HDR1");
						} else {
							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
						}
						if (m.get("DISPLAYBYORDERTYPE").equals("1"))
							parameters.put("OrderHeader", orderDesc);
						else
							parameters.put("OrderHeader", (String) m.get("HDR1"));
						// parameters.put("OrderHeader", (String) m.get("HDR1"));
						parameters.put("ToHeader", (String) m.get("HDR2"));
						parameters.put("FromHeader", (String) m.get("HDR3"));
						parameters.put("Date", (String) m.get("DATE"));
						parameters.put("OrderNo", (String) m.get("ORDERNO"));
						parameters.put("RefNo", (String) m.get("REFNO"));
						parameters.put("Terms", (String) m.get("TERMS"));
						parameters.put("Discount", (String) m.get("DISCOUNT"));
						parameters.put("NetRate", (String) m.get("NETRATE"));
						parameters.put("OrderQty", (String) m.get("ORDERQTY"));
//						if (m.get("PRINTSUPTERMS").equals("1")) {
							//parameters.put("TermsDetails", sPayTerms);
							parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
//						} else {
//							parameters.put("TermsDetails", (String) m.get("TERMSDETAILS"));
//						}
						parameters.put("SoNo", (String) m.get("SONO"));
						parameters.put("Item", (String) m.get("ITEM"));
						parameters.put("Description", (String) m.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) m.get("ORDERQTY"));
						parameters.put("UOM", (String) m.get("UOM"));
						;
						parameters.put("Rate", (String) m.get("RATE"));
						parameters.put("TaxAmount", (String) m.get("TAXAMOUNT"));
						parameters.put("Amt", (String) m.get("AMT"));

						parameters.put("SubTotal", (String) m.get("SUBTOTAL") + " " + "(" + curDisplay + ")");
						String taxby = _PlantMstDAO.getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
						if (taxby.equalsIgnoreCase("BYORDER")) {
							parameters.put("TotalTax", (String) m.get("TOTALTAX") + " " + "(" + inbgst + "%" + ")");
						} else {
							parameters.put("TotalTax", (String) m.get("TOTALTAX"));
						}
						parameters.put("Total", (String) m.get("TOTAL") + " " + "(" + curDisplay + ")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) m.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", (String) m.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");

						parameters.put("Footer1", (String) m.get("F1"));
						parameters.put("Footer2", (String) m.get("F2"));
						parameters.put("Footer3", (String) m.get("F3"));
						parameters.put("Footer4", (String) m.get("F4"));
						parameters.put("Footer5", (String) m.get("F5"));
						parameters.put("Footer6", (String) m.get("F6"));
						parameters.put("Footer7", (String) m.get("F7"));
						parameters.put("Footer8", (String) m.get("F8"));
						parameters.put("Footer9", (String) m.get("F9"));
						parameters.put("lblINCOTERM", (String) m.get("INCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						parameters.put("PrdXtraDetails", (String) m.get("PRINTXTRADETAILS"));
						parameters.put("PRINTWITHBRAND", (String) m.get("PRINTWITHBRAND"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) m.get("PRINTWITHPRODUCTREMARKS"));
						if (request.getParameter("printwithbillno") != null)
						parameters.put("RecvDateRange", sConditionbill);
						else
						parameters.put("RecvDateRange", sCondition);
						parameters.put("CompanyDate", (String) m.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) m.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) m.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) m.get("COMPANYSIG"));
						parameters.put("PreBy", (String) m.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						parameters.put("AuthSign", (String) m.get("AUTHSIGNATURE"));
						parameters.put("RCBNO", (String) m.get("RCBNO"));
						parameters.put("SUPPLIERRCBNO", (String) m.get("SUPPLIERRCBNO"));
						parameters.put("UENNO", (String) m.get("UENNO"));//imtiuen
						parameters.put("SUPPLIERUENNO", (String) m.get("SUPPLIERUENNO"));//imtiuen
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						parameters.put("trans", transportmode);
						
						if (m.get("PRINTSUPTERMS").equals("1")) {
							parameters.put("payterms", pays);
							} else {
								parameters.put("payterms", paymentterms);
							}
//						parameters.put("payterms", paymentterms);
						parameters.put("PRINTWITHTRANSPORT_MODE", (String) m.get("PRINTWITHTRANSPORT_MODE"));
						parameters.put("TRANSPORT_MODE", (String) m.get("TRANSPORT_MODE"));
						parameters.put("TERMSDETAILS", (String) m.get("TERMSDETAILS"));
						//end
						
						parameters.put("TOTALAFTERDISCOUNT", (String) m.get("TOTALAFTERDISCOUNT"));
						parameters.put("GRNDATE", (String) m.get("GRNDATE"));
						parameters.put("BILLDATELabel", (String) m.get("BILLDATE"));
						//new changes - azees 12/2020 
						parameters.put("printwithbatch",printwithbatch);
						parameters.put("Adjustment",  m.get("ADJUSTMENT"));
						parameters.put("PROJECT",  m.get("PROJECT"));
						parameters.put("ISPROJECT",  m.get("PRINTWITHPROJECT"));
						parameters.put("PRINTUENNO",  m.get("PRINTWITHUENNO"));//imthiuen
						parameters.put("PRINTSUPPLIERUENNO",  m.get("PRINTWITHSUPPLIERUENNO"));//imthiuen
						parameters.put("PRODUCTRATESARE",  m.get("PRODUCTRATESARE"));
						parameters.put("ISTAXINCLUSIVE",  ISTAXINCLUSIVE);
						double doubledatabilldiscount = new Double(DISCOUNT);
						parameters.put("BILL_DISCOUNT", doubledatabilldiscount);
						parameters.put("BILL_DISCOUNT_TYPE", DISCOUNT_TYPE);
						parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
						parameters.put("AdjustmentAmount", AdjustmentAmount);
						parameters.put("ISDISCOUNTTAX", ISORDERDISCOUNTTAX);
						parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
						parameters.put("BILL_ISDISCOUNTTAX", ISDISCOUNTTAX);
						parameters.put("PROJECTNAME", PROJECT_NAME);
						if (m.get("PRINTEMPLOYEE").equals("1")) {// Author: Azees  Create date: July 10,2021  Description: Employee on Jasper
							parameters.put("Employee", (String) m.get("EMPLOYEE"));
							String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
							parameters.put("EmployeeName", empname);
						} else {
							parameters.put("Employee", "");
							parameters.put("EmployeeName", "");
						}
						if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
							ISSHIPPINGTAX="Tax Exclusive";
						else
							ISSHIPPINGTAX="Tax Inclusive";
						
						if(ISORDERDISCOUNTTAX.equalsIgnoreCase("0"))
							ISORDERDISCOUNTTAX="Tax Exclusive";
						else
							ISORDERDISCOUNTTAX="Tax Inclusive";
						
						if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
							ISDISCOUNTTAX="Tax Exclusive";
						else
							ISDISCOUNTTAX="Tax Inclusive";
						
						
						if(DISCOUNT_TYPE.equalsIgnoreCase("%"))
							parameters.put("lblOrderDiscountBill", "Discount ("+ DISCOUNT + "%" +")"+" ("+ISDISCOUNTTAX+")");
						else
							parameters.put("lblOrderDiscountBill", "Discount ("+DISCOUNT_TYPE+")"+" ("+ISDISCOUNTTAX+")");
						
						/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+inbgst+"%]").trim();*/
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
							inbgst="0.0";
						} else if(ptaxiszero == 0 && ptaxisshow == 0){
							TAX_TYPE = "";
							inbgst="0.0";
						} else if(ptaxiszero == 0 && ptaxisshow == 1){						
							if(sales_location.equalsIgnoreCase(""))
								TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+inbgst+"%]").trim();
							else
								TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+inbgst+"%]").trim();
						} else {							
							
							if(sales_location.equalsIgnoreCase(""))
								TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
							else
								TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
							inbgst="0.0";//Author: Azees  Create date: August 05,2021  Description: zero tax issue 
						}
		  				parameters.put("TAX_TYPE", TAX_TYPE);
		  				gst = new Double(inbgst);
		  				gst = gst / 100;
						System.out.println("GST : " + gst);
		  				parameters.put("taxPercentage", gst.doubleValue());
		  				
						if (request.getParameter("printwithgrno") != null || request.getParameter("printwithbillno") != null) {
							parameters.put("printwithbillno", "no");
							parameters.put("printwithgrno", "no");
							if (request.getParameter("printwithbillno") != null) {
								parameters.put("printwithbillno", "yes");
							//if (request.getParameter("printwithgrno") != null) //azees - 12/2020
								parameters.put("printwithgrno", "yes");
							}
							parameters.put("BILLNOLabel", (String) m.get("BILLNO"));
							parameters.put("BILLNO", BILLNO);
							parameters.put("GRNOLabel", (String) m.get("GRNO"));
							parameters.put("GRNO", GRNO);
							if (GRNO.length() == 0)
							{
								parameters.put("RECVDATE", "");
							}else{
							parameters.put("RECVDATE", RECVDATE);
							}
							if (BILLNO.length() == 0)
							{
							parameters.put("BILLDATE", "");
							}else{
							parameters.put("BILLDATE", BILLDATE);
							}
							//parameters.put("RECVDATE", RECVDATE);
						}else {
							parameters.put("printwithgrno", "no");
							parameters.put("printwithbillno", "no");
						}
						double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
						double doubledatashippingcost = new Double(DATASHIPPINGCOST);
						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
						if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
							parameters.put("lblOrderDiscount",
									(String) m.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISORDERDISCOUNTTAX + ")");
							else
								parameters.put("lblOrderDiscount",
										(String) m.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISORDERDISCOUNTTAX + ")");
						
						parameters.put("lblShippingCost", (String) m.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
						if (orderRemarks.length() > 0)
							orderRemarks = (String) m.get("REMARK1") + " : " + orderRemarks;
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) m.get("REMARK2") + " : " + orderRemarks3;

						parameters.put("orderRemarks", orderRemarks);
						parameters.put("orderRemarks3", orderRemarks3);
						parameters.put("DeliveryDate", (String) m.get("DELIVERYDATE"));

						String deldate = StrUtils.fString((String) dataMap.get("deldate"));
						//if (deldate.length() > 0)
//							deldate = deldate;
						parameters.put("DeliveryDt", deldate);

						if (m.get("PSUPREMARKS").equals("1")) {
							parameters.put("SupRemarks", sRemarks);
						} else {
							parameters.put("SupRemarks", "");
						}
						parameters.put("STo_CustName", SHPCUSTOMERNAME);
						parameters.put("STo", (String) m.get("SHIPTO"));

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
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) m.get("PRINTWITHCOMPANYSEAL");
						String PRINTWITHCOMPANYSIG= (String) m.get("PRINTWITHCOMPANYSIG");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
						parameters.put("PRINTWITHDELIVERYDATE",  m.get("PRINTWITHDELIVERYDATE"));
						parameters.put("PRODUCTDELIVERYDATE",  m.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  m.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("CALCULATETAXWITHSHIPPINGCOST",  m.get("CALCULATETAXWITHSHIPPINGCOST"));
						System.out.println("delivery date ::::::: "+m.get("PRINTWITHDELIVERYDATE"));
						if (Orientation.equals("Portrait")) {
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
								if (jasperFileName.equals("printPOInvoiceWithBatchWithDiscount")) {
									jasperFileName = "printPOKitchenInvoiceWithBatchWithDiscount";
								}
							jasperPath = DbBean.JASPER_INPUT + "/" + jasperFileName + "Portrait";

						} else {
							jasperPath = DbBean.JASPER_INPUT + "/" + jasperFileName;
						}
						
						ArrayList arrayposdao = new ArrayList();
						PoDetDAO poDetDAO = new PoDetDAO();	
						arrayposdao = poDetDAO.selectPoDet("ITEM,ItemDesc,UNITCOST ", htCond);
						if(arrayposdao.size() > 3)
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
			}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			exporter.exportReport();
			byte[] bytes = byteArrayOutputStream.toByteArray();
			//response.addHeader("Content-disposition", "attachment;filename=MultiplePO.pdf");
			if (!ajax) {
				response.addHeader("Content-disposition", "inline;filename=MultiplePO.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}else {
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + PONO + ".pdf")){
					fos.write(bytes);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}

	protected void viewDOReport(HttpServletRequest request, HttpServletResponse response, String action)
			throws IOException, Exception {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			DOUtil _DOUtil = new DOUtil();
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			if(Plant == null)
				Plant = (String) request.getAttribute("PLANT");//for mail - Azees 03_2022
			List listQry = pmUtil.getPlantMstDetails(Plant);//Azees Fix Query Load 09.22
			Map maps = (Map) listQry.get(0);
//			Map m = null;
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",companyregno = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",CWEBSITE = "",COMP_INDUSTRY="";
			
			String PDASIGN ="";

			String DONO = "";
			List jasperPrintList = new ArrayList();
			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
			if (ajax) {
				chkdDoNo = (String[])request.getAttribute("chkdDoNo");
			} else {
				String donoval = ((String)request.getAttribute("DONO"));
				if(donoval!=null) {
				chkdDoNo = new String[1];
				chkdDoNo[0]=donoval;
				}
			}
			//String PLANT = (String) session.getAttribute("PLANT");
			String PLANT = Plant;

			// ********To get from & to date issue date range*********************
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String GINOVal = StrUtils.fString(request.getParameter("INVOICENO"));
			
			if(GINOVal == null||GINOVal == "")
				GINOVal = (String) request.getAttribute("INVOICENO");//for mail - Azees 03_2022
				PDASIGN = (String) request.getAttribute("PDASIGN");//img for mail 
			if(GINOVal==null)
				GINOVal="";
			if(PDASIGN==null)
				PDASIGN="";
			String dtCondStr = "  and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
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
			//ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);//Azees Fix Query Load 09.22
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
				COMP_INDUSTRY = (String) map.get("COMP_INDUSTRY");

			}
			DOUtil poUtil = new DOUtil();
			Map ma = poUtil.getDOReceiptHdrDetailsDO(PLANT, "Outbound order");
			for (int i = 0; i < chkdDoNo.length; i++) {
//				Get all GINO for this DO from SHIPHIS table
				String query = "";
				String Quer = "DISTINCT isnull(SIGNATURENAME, '') as SIGNATURENAME,isnull(PDASIGNPATH, '') as PDASIGNPATH,isnull(invoiceno, '') as GINO,ISNULL(issuedate,'') as GINODATE,ISNULL((Select top 1 B.INVOICE from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as INVOICENO,ISNULL((Select top 1 INVOICE_DATE from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as ISSUEDATE,ISNULL((Select top 1 SHIPPINGCOST from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as DATASHIPPINGCOST,ISNULL((Select top 1 ORDER_DISCOUNT from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as DATAORDERDISCOUNT ";
//				String query = "DISTINCT isnull(invoiceno, '') as GINO,ISNULL(issuedate,'') as GINODATE ";
				DONO = chkdDoNo[i];
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("DONO", DONO);
				if(GINOVal.length()>0)
				htCond.put("INVOICENO", GINOVal);
				ArrayList alShipDetail = new ArrayList();
				if (request.getParameter("printwithigino") != null || request.getAttribute("printwithigino") != null) {										
					alShipDetail = dOUtil.getOBIssueList(Quer, htCond, " and (invoiceno!='' or  invoiceno is not null) "+sCondition+" ORDER BY GINO DESC");
				}
				if(GINOVal.length()>0)
				htCond.remove("INVOICENO");
				for(int j = 0; j < ((request.getParameter("printwithigino") != null || request.getAttribute("printwithigino") != null) ? alShipDetail.size() : 1); j ++) {
					String GINO="",GINODATE="",PDASIGNPATH="",SIGNATURENAME="";
					if (request.getParameter("printwithigino") != null || request.getAttribute("printwithigino") != null) {
						
						GINO = ((Map)alShipDetail.get(j)).get("GINO").toString();
						GINODATE = ((Map)alShipDetail.get(j)).get("GINODATE").toString();
						ArrayList signDetail = new ArrayList();
						signDetail = dOUtil.getOBIssueList(Quer, htCond, " and (invoiceno!='' or  invoiceno is not null) "+sCondition+" ORDER BY INVOICENO DESC");
						PDASIGNPATH = ((Map)signDetail.get(j)).get("PDASIGNPATH").toString();
						SIGNATURENAME = ((Map)signDetail.get(j)).get("SIGNATURENAME").toString();
						 if(PDASIGNPATH.equalsIgnoreCase("")){
							 PDASIGNPATH = PDASIGN;
				            }
						 if(PDASIGNPATH.equalsIgnoreCase("") && PDASIGN.equalsIgnoreCase("")){
							 PDASIGNPATH = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
						 }
					}
				if ("printDOWITHBATCH".equals(action)) {
					//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOKitchenWITHBATCH";
					else
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWITHBATCH";
				}else if ("printDOWITHBATCHANDCONTAINER".equals(action)){
					//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOKitchenWITHBATCHCONTAINER";
					else
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWITHBATCHCONTAINER";
				}else {
					//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOKitchenWITHBATCH";
					else
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWITHOUTBATCH";
				}
				
				if (request.getParameter("printwithigino") != null || request.getAttribute("printwithigino") != null) {
					if ("printDOWITHBATCHANDCONTAINER".equals(action)) {
						//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOKitchenWITHGINOCONTAINER";
					else
						jasperPath = DbBean.JASPER_INPUT + "/" +"rptDOWITHGINOCONTAINER";
					}else {
						//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOkitchenWITHGINO";
					else
						jasperPath = DbBean.JASPER_INPUT + "/" +"rptDOWITHGINO";
					}
				}
				
				signaturePath = DbBean.COMPANY_SIGN_PATH + "/" + PLANT.toLowerCase() + "/" + GINO + ".gif";
				File checkSignatureFile = new File(signaturePath);
				if (!checkSignatureFile.exists()) {
					signaturePath = DbBean.COMPANY_LOGO_PATH + "/" + "NoLogo.jpg";
				}
				ArrayList arrCust = cUtil.getCustomerDetailsForDO(DONO, PLANT);
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
					String sRemarks = (String) arrCust.get(15);
					String sAddr4 = (String) arrCust.get(16);
					String SHIPPINGID = (String) arrCust.get(25);
					String sRcbno = (String) arrCust.get(26);
					String suenno = (String) arrCust.get(27);//imtiuen
					
					ArrayList arrShippingDetails = _masterUtil.getOutboundShippingDetails(DONO, sCustCode, PLANT);
					Map parameters = new HashMap();
					if (request.getParameter("with_batch") != null
							&& "yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					String sShipCustName = "";					
					
					query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,isnull(orderdiscount,0) orderdiscount,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_dohdr.PROJECTID),'') PROJECT_NAME,"
							+ "isnull(CRBY,'') as CRBY,isnull(shippingcost,0) shippingcost," + "isnull(incoterms,'') incoterms";
					ArrayList arraydohdr = new ArrayList();
					arraydohdr = dOUtil.getDoHdrDetails(query, htCond);
					Map dataMap = (Map) arraydohdr.get(0);
//					String gstValue = dataMap.get("outbound_Gst").toString();
					String DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					String DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					String DATAINCOTERMS = dataMap.get("incoterms").toString();
					String PROJECT_NAME = dataMap.get("PROJECT_NAME").toString();
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
					String orderRemarks3 = (String) arrCust.get(21);
					String sDeliveryDate = (String) arrCust.get(22);
					String sEmpno = (String) arrCust.get(23);
					String sState = (String) arrCust.get(24);

					//String orderType = new DoHdrDAO().getOrderTypeForDO(PLANT, DONO);//Azees Fix Query Load 09.22

					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("signaturePath", signaturePath);
					// Customer Details
					parameters.put("OrderNo", DONO);
					parameters.put("company", PLANT);
					parameters.put("To_CompanyName", sCustName);
					parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
					if (sState.equals("")) {
						parameters.put("To_Country", sCountry);
					}else {
						parameters.put("To_Country", sState + "\n" + sCountry);
					}
					parameters.put("To_ZIPCode", sZip);
					parameters.put("To_AttentionTo", sContactName);
					parameters.put("To_CCTO", "");
					parameters.put("To_TelNo", sTelNo);
					parameters.put("To_Fax", sFax);
					parameters.put("To_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
//					parameters.put("usersignature", PDASIGNPATH);
//					parameters.put("usersignname", SIGNATURENAME);
					
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					DoHdrDAO doHdrDAO= new DoHdrDAO();
					DoHdr doheader = doHdrDAO.getDoHdrById(PLANT, DONO);
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(doheader.getTRANSPORTID() > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, doheader.getTRANSPORTID());
					}
					String paymentterms = "";
					paymentterms = doheader.getPAYMENT_TERMS();
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					//end
					
					// Company Details
					//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
//					parameters.put("fromAddress_CompanyName", CNAME);
//					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//					{
//					parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//					parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//					if(CSTATE.equals("")) {
//					parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//					}else {
//						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//					}
//					} else {
//						String fromAddress_BlockAddress="";
//						if(CADD2.length()>0)
//							fromAddress_BlockAddress=CADD2 + ",";
//						parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//						parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//						parameters.put("fromAddress_Country", CCOUNTRY);
//					}
					
					parameters.put("fromAddress_CompanyName", CNAME);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
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
					} else {
						String fromAddress_BlockAddress="";
						if(CADD2.length()>0)
							fromAddress_BlockAddress=CADD2 + ", ";
							parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
							if(CADD1.equals("")) {
								parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
							}else {
								parameters.put("fromAddress_RoadAddress", " "+CADD1 + ",");
							}
							if(CSTATE.equals("")) {
								parameters.put("fromAddress_Country",  CCOUNTRY);
							}else {
								parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
							}
					}
					
					parameters.put("fromAddress_ZIPCode", CZIP);
					parameters.put("fromAddress_TpNo", CTEL);
					parameters.put("fromAddress_FaxNo", CFAX);
					parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
					parameters.put("fromAddress_ContactPersonMobile", CHPNO);
					parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
					parameters.put("fromAddress_RCBNO", CRCBNO);
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					//parameters.put("currentTime", _DOUtil.getOrderDate(PLANT, DONO));
					parameters.put("currentTime", _DOUtil.getOrderDateOnly(PLANT, DONO));
					parameters.put("refNo", _DOUtil.getJobNum(PLANT, DONO));
					parameters.put("fromAddress_Website", CWEBSITE);
					String orderType = doheader.getORDERTYPE();//Azees Fix Query Load 09.22
					if(orderType.equals(""))
						orderType="OUTBOUND ORDER";
					parameters.put("orderType", orderType);
					parameters.put("InvoiceTerms", "");
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";

					// report template parameter added on June 22 2011 By Deen
					//DOUtil poUtil = new DOUtil();//Azees Fix Query Load 09.22
					//Map ma = poUtil.getDOReceiptHdrDetailsDO(PLANT, "Outbound order");
					Orientation = (String) ma.get("PrintOrientation");
					if (orderType.equals("OUTBOUND ORDER")) {
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
					parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));//azees
					parameters.put("PRINTWITHINCOTERM", (String) ma.get("PRINTINCOTERM"));//imti
					
					int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
					if(PRINTWITHSHIPINGADD == 1) {
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
						// ship to Address
						/*
						 * if(sShipCustName.length()>0) { parameters.put("STo_CustName", "SHIP TO : "+
						 * "\n"+ "\n"+ sShipCustName);
						 * 
						 * }else{ parameters.put("STo_CustName", sShipCustName); }
						 */
						parameters.put("STo_Addr1", sShipAddr1);
						parameters.put("STo_Addr2", sShipAddr2);
						parameters.put("STo_Addr3", sShipStreet);
						parameters.put("STo_City", sShipCity);
						if (sShipState.equals("")) {
							parameters.put("STo_Country",sShipCountry);
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
					} else {
						parameters.put("shipToId", "");
					}
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
					parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
					parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
					//end
					
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
					if (ma.get("PCUSREMARKS").equals("1")) {
						parameters.put("SupRemarks", sRemarks);
					} else {
						parameters.put("SupRemarks", "");
					}
					// report template parameter added on June 22 2011 end

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
					//imti start seal,sign condition in printoutconfig
					String CAPTURESIGNATURE = (String) ma.get("CAPTURESIGNATURE");
					String DISPLAYSIGNATURE= (String) ma.get("DISPLAYSIGNATURE");
					if(CAPTURESIGNATURE.equalsIgnoreCase("0")){
		                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					if(DISPLAYSIGNATURE.equalsIgnoreCase("0")){
						signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					parameters.put("sealPath", sealPath);
					parameters.put("signPath", signPath);
					
					parameters.put("imagePath1", imagePath1);
					parameters.put("imagePath2", imagePath2);
					
					if (Orientation.equals("Portrait")) {
						jasperPath += "Portrait";
					}
					if (action.equals("printDOWITHOUTBATCH")) {
						if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
							parameters.put("printwithbatch", "no");
						else 
							parameters.put("printwithbatch", "no");
					}else {
						parameters.put("printwithbatch", "yes");
					}
					parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
					parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
					parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
					parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
					parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
					parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
					parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
					parameters.put("buyer", (String) ma.get("BUYER"));
					parameters.put("buyersignature", (String) ma.get("BUYERSIGNATURE"));
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
					parameters.put("GINODATELabel", (String) ma.get("GINODATE"));
					
					
					if (request.getParameter("printwithigino") != null || request.getAttribute("printwithigino") != null) {
						
						if (request.getParameter("printwithigino") != null || request.getAttribute("printwithigino") != null)
							parameters.put("printwithigino", "yes");
						parameters.put("GINOLabel", (String) ma.get("GINO"));
						parameters.put("GINO", GINO);
						
						if (GINO.length() == 0)
						{
						parameters.put("GINODATE", "");
						}else{
						parameters.put("GINODATE", GINODATE);
						}
						
						//IMTI modified on 18-03-2022 : display signature based on printout config SIGNATURE radio button
						String SignConfig =  (String) ma.get("DISPLAYSIGNATURE");
							if (SignConfig.equals("0")) {
								signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
								parameters.put("usersignature", signPath);
								parameters.put("buyersignature", (String) ma.get("COMPANYSIG"));
								parameters.put("usersignname", "");
								parameters.put("buyer", (String) ma.get("COMPANYNAME"));
							}else if (SignConfig.equals("1")) {
								parameters.put("usersignature", signPath);
								parameters.put("buyersignature", (String) ma.get("COMPANYSIG"));
								parameters.put("buyer", (String) ma.get("COMPANYNAME"));
								parameters.put("usersignname", "");
							}else if (SignConfig.equals("2")) {
								parameters.put("usersignature", PDASIGNPATH);
								parameters.put("buyersignature", (String) ma.get("BUYERSIGNATURE"));
								parameters.put("buyer", (String) ma.get("BUYER"));
								parameters.put("usersignname", SIGNATURENAME);
							}
							
					}else {
						parameters.put("printwithigino", "no");						
						parameters.put("GINODATE", "");
						parameters.put("GINO", "");
					}
					String Querys = "";
					Querys = "item,itemdesc,unitprice";
					DoDetDAO dodetdao = new DoDetDAO();						
					ArrayList arraydodet = dodetdao.selectDoDet(Querys, htCond);
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
			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			// exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
			// "D:/jasper/MultipleDO.pdf");
			exporter.exportReport();
			byte[] bytes = byteArrayOutputStream.toByteArray();
			//response.addHeader("Content-disposition", "attachment;filename=MultipleDO.pdf");
			if (!ajax) {
				if ((String)request.getAttribute("ISAUTOMAIL") == null) {//for mail - Azees 03_2022
				response.addHeader("Content-disposition", "inline;filename=MultipleDO.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
				} else {
					try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/DO_" + DONO + ".pdf")){
						fos.write(bytes);
					}
				}
			}else {
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/DO_" + DONO + ".pdf")){
					fos.write(bytes);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}

	protected void viewInvoiceReport(HttpServletRequest request, HttpServletResponse response, String action)
			throws IOException, Exception {

		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			DOUtil _DOUtil = new DOUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			cUtil.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			InvoiceDAO invoicedao = new InvoiceDAO();
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List listQry = pmUtil.getPlantMstDetails(Plant);//Azees Fix Query Load 09.22
			Map maps = (Map) listQry.get(0);
//			Map m = null;
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "", companyregno = "",SEALNAME="",SIGNNAME="",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",CWEBSITE = "",COMP_INDUSTRY="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "";
			String DONO = "";
			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
			if (ajax) {
				chkdDoNo = (String[])request.getAttribute("chkdDoNo");
			}
			String PLANT = (String) session.getAttribute("PLANT");
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			// ********To get from & to date issue date range*********************
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String dtCondStr = "  and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
			String sCondition = "",sConditioninv = "", fdate = "", tdate = "", signaturePath = "";
			String dtCondStrinv = "  and ISNULL(INVOICE_DATE,'')<>'' AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date)";
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
				sConditioninv=sConditioninv + dtCondStrinv + " >= '" + fdate + "'  ";
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" + tdate + "'  ";
					sConditioninv=sConditioninv + dtCondStrinv + " <= '" + tdate + "'  ";
				}
			} else {
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + "  <= '" + tdate + "'  ";
					sConditioninv=sConditioninv + dtCondStrinv + " <= '" + tdate + "'  ";
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
			String imagePath2, Orientation = "", PRINTDELIVERYNOTE = "", PRINTPACKINGLIST = "";

			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}

			con = DbBean.getConnection();

//			//String SysDate = DateUtils.getDate();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceWithBatch";
//			Date sysTime1 = new Date(System.currentTimeMillis());
			//Date cDt = new Date(System.currentTimeMillis());
			//ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);//Azees Fix Query Load 09.22
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
				COMP_INDUSTRY = (String) map.get("COMP_INDUSTRY");

			}
			DOUtil poUtil1 = new DOUtil();
			Map ma1 = poUtil1.getDOReceiptInvoiceHdrDetailsDO(PLANT, "OUTBOUND ORDER");//Azees Fix Query Load 09.22
			for (int i = 0; i < chkdDoNo.length; i++) {
				//	Get all INVOICENOs for this DO from SHIPHIS table
				String query;
				String queryimg= "DISTINCT isnull(SIGNATURENAME, '') as SIGNATURENAME,isnull(PDASIGNPATH, '') as PDASIGNPATH,isnull(invoiceno, '') as GINO,ISNULL(issuedate,'') as GINODATE,ISNULL((Select top 1 B.INVOICE from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as INVOICENO,ISNULL((Select top 1 INVOICE_DATE from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as ISSUEDATE,ISNULL((Select top 1 SHIPPINGCOST from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as DATASHIPPINGCOST,ISNULL((Select top 1 ORDER_DISCOUNT from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as DATAORDERDISCOUNT ";
				DONO = chkdDoNo[i];
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("DONO", DONO);
				ArrayList alShipDetail = new ArrayList();
				if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null || request.getParameter("printwithinvoiceno") == null) {
					//alShipDetail = dOUtil.getOBIssueList(query, htCond, " AND invoiceno!=''");					
					alShipDetail = dOUtil.getOBIssueList(queryimg, htCond, " and (invoiceno!='' or  invoiceno is not null) "+sCondition+" ORDER BY INVOICENO DESC");
				}
				for(int j = 0; j < ((request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null || request.getParameter("printwithinvoiceno") == null ) ? alShipDetail.size() : 1); j ++) {
					String INVOICENO = "",ISSUEDATE="",GINO="",GINODATE="",DATAORDERDISCOUNT="0",DATASHIPPINGCOST="0",
							ORDERDISCOUNTTYPE = "",AdjustmentAmount="",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",
							ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",ISTAXINCLUSIVE="",PROJECT_NAME="",sales_location="",empno="",ID="",currencyuseqt="",PDASIGNPATH="",SIGNATURENAME="";
							int TAXID=0;
					if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null || request.getParameter("printwithinvoiceno") == null ) {
						INVOICENO = ((Map)alShipDetail.get(j)).get("INVOICENO").toString();
						ISSUEDATE = ((Map)alShipDetail.get(j)).get("ISSUEDATE").toString();
						GINO = ((Map)alShipDetail.get(j)).get("GINO").toString();
						GINODATE = ((Map)alShipDetail.get(j)).get("GINODATE").toString();
						DATAORDERDISCOUNT = ((Map)alShipDetail.get(j)).get("DATAORDERDISCOUNT").toString();
						DATASHIPPINGCOST = ((Map)alShipDetail.get(j)).get("DATASHIPPINGCOST").toString();
						//htCond.put("INVOICENO", INVOICENO);
					}
//Changes done by vicky on 05-10-21 PDA Signature included
					ArrayList signDetail = new ArrayList();
					signDetail = dOUtil.getOBIssueList(queryimg, htCond, " and (invoiceno!='' or  invoiceno is not null) "+sCondition+" ORDER BY INVOICENO DESC");
					PDASIGNPATH = ((Map)signDetail.get(j)).get("PDASIGNPATH").toString();
					SIGNATURENAME = ((Map)signDetail.get(j)).get("SIGNATURENAME").toString();
					 if(PDASIGNPATH.equalsIgnoreCase("")){
						 PDASIGNPATH = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			            }
					if (request.getParameter("printwithinvoiceno") != null && INVOICENO.isEmpty())
					{
						if(request.getParameter("printwithigino") == null)
						continue;
					}
					ArrayList alinvc = new ArrayList();
					if (request.getParameter("printwithinvoiceno") != null)
					{
						Hashtable htinv = new Hashtable();
						htinv.put("PLANT", PLANT);
						String querybill="Select isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
								+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_FININVOICEHDR.TAXID),'') TAX_TYPE,ISNULL(EMPNO,'') EMPNO,"
								+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_FININVOICEHDR.PROJECTID),'') PROJECT_NAME,"
								+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_FININVOICEHDR.sales_location),'') sales_location,"
								+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(TAXID,0) TAXID, "
								+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(ORDER_DISCOUNT,0) ELSE (isnull(ORDER_DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
								+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
								+ "ISNULL(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,ISNULL(ID,'') ID,ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT "
								+ "FROM "+PLANT+"_FININVOICEHDR WHERE INVOICE='"+INVOICENO+"' ";
						alinvc=invoicedao.selectForReport(querybill, htinv, "");
						if(alinvc.size()>0)
						{
							ORDERDISCOUNTTYPE = ((Map)alinvc.get(0)).get("ORDERDISCOUNTTYPE").toString();
							DISCOUNT_TYPE = ((Map)alinvc.get(0)).get("DISCOUNT_TYPE").toString();
							ISTAXINCLUSIVE = ((Map)alinvc.get(0)).get("ISTAXINCLUSIVE").toString();
							TAX_TYPE = ((Map)alinvc.get(0)).get("TAX_TYPE").toString();							
							ISDISCOUNTTAX = ((Map)alinvc.get(0)).get("ISDISCOUNTTAX").toString();
							ISSHIPPINGTAX = ((Map)alinvc.get(0)).get("ISSHIPPINGTAX").toString();
							ISORDERDISCOUNTTAX= ((Map)alinvc.get(0)).get("ISORDERDISCOUNTTAX").toString();
							AdjustmentAmount = ((Map)alinvc.get(0)).get("ADJUSTMENT").toString();
							DATAORDERDISCOUNT = ((Map)alinvc.get(0)).get("orderdiscount").toString();
							DATASHIPPINGCOST = ((Map)alinvc.get(0)).get("shippingcost").toString();
							DISCOUNT = ((Map)alinvc.get(0)).get("DISCOUNT").toString();
							TAXID = Integer.valueOf(((Map)alinvc.get(0)).get("TAXID").toString());
							PROJECT_NAME=((Map)alinvc.get(0)).get("PROJECT_NAME").toString();
							sales_location=((Map)alinvc.get(0)).get("sales_location").toString();
							empno=((Map)alinvc.get(0)).get("EMPNO").toString();
							ID=((Map)alinvc.get(0)).get("ID").toString();
							currencyuseqt=((Map)alinvc.get(0)).get("CURRENCYUSEQT").toString();
						}
					}
					String invoice = INVOICENO;
					
					
					//DOUtil poUtil1 = new DOUtil();//Azees Fix Query Load 09.22
					//Map ma1 = poUtil1.getDOReceiptInvoiceHdrDetailsDO(PLANT, "OUTBOUND ORDER");
					
					String printwithbatch="0"; //new changes - azees 12/2020
					if ("printInvoiceWITHBATCHANDCONTAINER".equals(action)){
						//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceWithBatchCONTAINER";
						if (ma1.get("PRINTWITHDISCOUNT").equals("1") && (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null)){
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchenWithBatchCONTAINERWithDiscountWithInvoice";
							else
							jasperPath += "WithDiscountWithInvoice";
						}else
						if (ma1.get("PRINTWITHDISCOUNT").equals("1")){
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchenWithBatchCONTAINERWithDiscount";
							else
							jasperPath += "WithDiscount";
						}else {
						if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null){
							jasperPath += "WithInvoice";
						}
						}
					}else {
						//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceWithBatch";
						if (ma1.get("PRINTWITHDISCOUNT").equals("1") && (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null)){
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchenWithBatchWithDiscountWithInvoice";
								else
							jasperPath += "WithDiscountWithInvoice";
						}else
						if (ma1.get("PRINTWITHDISCOUNT").equals("1")){
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) 
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchenWithBatchWithDiscount";
								else
							jasperPath += "WithDiscount";
						}else {
					if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null) {
						jasperPath += "WithInvoice";
					}
						}						
						
						if ("printInvoiceWITHBATCH".equals(action)) {
							printwithbatch="1";
						}
						
						/*jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceWithOutBatch";
						if (ma1.get("PRINTWITHDISCOUNT").equals("1") && (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null)){
							jasperPath += "WithDiscountWithInvoice";
						}else
						if (ma1.get("PRINTWITHDISCOUNT").equals("1")){
							jasperPath += "WithDiscount";
						}else {
						if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null){
						jasperPath += "WithInvoice";
						}
					}*/
					}
					
					signaturePath = DbBean.COMPANY_SIGN_PATH + "/" + PLANT.toLowerCase() + "/" + "sign" + DONO + ".bmp";
					File checkSignatureFile = new File(signaturePath);
					if (!checkSignatureFile.exists()) {
						signaturePath = DbBean.COMPANY_LOGO_PATH + "/" + "NoLogo.jpg";
					}
					ArrayList arrCust = cUtil.getCustomerDetailsForDO(DONO, PLANT);
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
						String sRemarks = (String) arrCust.get(15);
						String sAddr4 = (String) arrCust.get(16);
						String pays = (String) arrCust.get(28);

						String SHIPPINGID = (String) arrCust.get(25);
						String sRcbno = (String) arrCust.get(26);
						String suenno = (String) arrCust.get(27);//imtiuen
						ArrayList arrShippingDetails = _masterUtil.getOutboundShippingDetails(DONO, sCustCode, PLANT);
						Map parameters = new HashMap();
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", sCustCode);
							SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
							SHPCONTACTNAME = (String) arrShippingDetails.get(2);
							SHPTELEPHONE = (String) arrShippingDetails.get(3);
							SHPHANDPHONE = (String) arrShippingDetails.get(4);
//							SHPFAX = (String) arrShippingDetails.get(5);
							//SHPEMAIL = (String) arrShippingDetails.get(6);
							SHPUNITNO = (String) arrShippingDetails.get(7);
							SHPBUILDING = (String) arrShippingDetails.get(8);
							SHPSTREET = (String) arrShippingDetails.get(9);
							SHPCITY = (String) arrShippingDetails.get(10);
							SHPSTATE = (String) arrShippingDetails.get(11);
							SHPCOUNTRY = (String) arrShippingDetails.get(12);
							SHPPOSTALCODE = (String) arrShippingDetails.get(13);
						} else {
							parameters.put("shipToId", "");
						}
						if (request.getParameter("with_batch") != null
								&& "yes".equals(request.getParameter("with_batch"))) {
							parameters.put("PrintBatch", "yes");
						}
						String sShipCustName = "";
						
						query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,"
								+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
								+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
								+ "isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') as PAYMENTTYPE,isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost," + "isnull(incoterms,'') incoterms,"
								+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(TAXID,0) TAXID,ISNULL(EMPNO,'') EMPNO,"
								+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_dohdr.TAXID),'') TAX_TYPE,"
								+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_dohdr.PROJECTID),'') PROJECT_NAME,"
								+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_dohdr.sales_location),'') sales_location,"
								+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
								+ "ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,STATUS";
						ArrayList arraydohdr = new ArrayList();
						arraydohdr = dOUtil.getDoHdrDetails(query, htCond);
						Map dataMap = (Map) arraydohdr.get(0);
						String gstValue = dataMap.get("outbound_Gst").toString();
						
						if (request.getParameter("printwithinvoiceno") == null)
						//if (request.getParameter("printwithinvoiceno") == null || request.getParameter("printwithigino") == null)
						{
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
							String POSTATUS = (String) dataMap.get("STATUS").toString();
							if(!POSTATUS.equalsIgnoreCase("C"))
							{
							DATASHIPPINGCOST = invoicedao.getActualShippingCostForinvoice(PLANT, DONO);
							if(!ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
								DATAORDERDISCOUNT = invoicedao.getActualOrderDiscountCostForinvoice(PLANT, DONO);
							}
							PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
							sales_location=dataMap.get("sales_location").toString();
							TAX_TYPE = dataMap.get("TAX_TYPE").toString();
							TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
							empno=dataMap.get("EMPNO").toString();
						}
						//new changes - azees 12/2020
						parameters.put("fromAddress_Website", " "+CWEBSITE);
						parameters.put("printwithbatch",printwithbatch);
						parameters.put("ISTAXINCLUSIVE", ISTAXINCLUSIVE);
						double doubledatabilldiscount = new Double(DISCOUNT);
						parameters.put("DO_DISCOUNT", doubledatabilldiscount);
						parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
						parameters.put("DO_DISCOUNT_TYPE", DISCOUNT_TYPE);
						parameters.put("AdjustmentAmount", AdjustmentAmount);
						parameters.put("ISDISCOUNTTAX", ISDISCOUNTTAX);
						parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
						parameters.put("ISORDERDISCOUNTTAX", ISORDERDISCOUNTTAX);
						parameters.put("PROJECTNAME", PROJECT_NAME);
						parameters.put("invoice", invoice);
						
						parameters.put("STo", (String) ma1.get("SHIPTO"));
						parameters.put("STo_Addr1", SHPUNITNO);
						parameters.put("STo_Addr2", SHPBUILDING);
						parameters.put("STo_Addr3", SHPSTREET);
						parameters.put("STo_City", SHPCITY);
						parameters.put("STo_Country", SHPSTATE + ", " + SHPCOUNTRY);
						parameters.put("STo_ZIP", SHPPOSTALCODE);
						parameters.put("STo_Telno", SHPTELEPHONE);
						parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						if (SHPCONTACTNAME.length() > 0)
							parameters.put("STo_Telno", "Tel: " + SHPTELEPHONE);
						else
							parameters.put("STo_Telno", SHPTELEPHONE);
						if (SHPCUSTOMERNAME.length() > 0) {
							parameters.put("STo_AttentionTo", "Attn: " + SHPCONTACTNAME);
						} else {
							parameters.put("STo_AttentionTo", SHPCONTACTNAME);
						}

						/**/
						if (SHPHANDPHONE.length() > 0) {
							parameters.put("SHPHANDPHONE", "HP: " + SHPHANDPHONE);
						} else {
							parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						}
						
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
						//TAX_TYPE = dataMap.get("TAX_TYPE").toString();
						/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();*/
						//TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
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
						
						//String DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
						//String DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
						String DATAINCOTERMS = dataMap.get("incoterms").toString();
						String orderRemarks = (String) arrCust.get(18);
//						String sPayTerms = (String) arrCust.get(19);
						String orderRemarks3 = (String) arrCust.get(21);
						String sDeliveryDate = (String) arrCust.get(22);
						String sEmpno = (String) arrCust.get(23);
						String sState = (String) arrCust.get(24);
						//String orderType = new DoHdrDAO().getOrderTypeForDO(PLANT, DONO);//Azees Fix Query Load 09.22
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);

						parameters.put("signaturePath", signaturePath);
						// Customer Details
						//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Azees Fix Query Load 09.22
//						if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
//							if(DONO.length()>0)
//								DONO = INVOICENO;
//							parameters.put("OrderNo", INVOICENO);
//							}else {
//								parameters.put("OrderNo", DONO);
//							}
					
//						if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")){ 
//						parameters.put("OrderNo", INVOICENO);
//						}else{
//						parameters.put("OrderNo", DONO);
//						}
						parameters.put("OrderNo", DONO);
						parameters.put("company", PLANT);
						parameters.put("taxInvoiceTo_CompanyName", sCustName);
						parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
						parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
						if (sState.equals("")) {
							parameters.put("taxInvoiceTo_Country",sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country", sState + "\n" + sCountry);	
						}
//						parameters.put("taxInvoiceTo_Country", sState + "\n" + sCountry);
						parameters.put("taxInvoiceTo_ZIPCode", sZip);
						parameters.put("taxInvoiceTo_AttentionTo", sContactName);
						parameters.put("taxInvoiceTo_CCTO", "");
						parameters.put("To_TelNo", sTelNo);
						parameters.put("To_Fax", sFax);
						parameters.put("To_Email", sEmail);
						parameters.put("sRCBNO", sRcbno);
						parameters.put("sUENNO", suenno);//imtiuen
						parameters.put("usersignature", PDASIGNPATH);
						parameters.put("usersignname", SIGNATURENAME);
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						DoHdrDAO doHdrDAO= new DoHdrDAO();
						DoHdr doheader = doHdrDAO.getDoHdrById(PLANT, DONO);
						TransportModeDAO transportmodedao = new TransportModeDAO();
						String transportmode = "";
						if(doheader.getTRANSPORTID() > 0){
							transportmode = transportmodedao.getTransportModeById(PLANT, doheader.getTRANSPORTID());
						}
						String paymentterms = "";
						paymentterms = doheader.getPAYMENT_TERMS();
						parameters.put("trans", transportmode);
						parameters.put("payterms", paymentterms);
						//end
						
						// Company Details
//						parameters.put("fromAddress_CompanyName", CNAME);
//						if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//						{
//						parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//						parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//						if(CSTATE.equals("")) {
//							parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//						}else {
//							parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//						}
//						} else {
//							String fromAddress_BlockAddress="";
//							if(CADD2.length()>0)
//								fromAddress_BlockAddress=CADD2 + ",";
//							parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//							parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//							parameters.put("fromAddress_Country", CCOUNTRY);
//						}
						
						parameters.put("fromAddress_CompanyName", CNAME);
						if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
						{
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
						} else {
							String fromAddress_BlockAddress="";
							if(CADD2.length()>0)
								fromAddress_BlockAddress=CADD2 + ", ";
								parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
								if(CADD1.equals("")) {
									parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
								}else {
									parameters.put("fromAddress_RoadAddress", " "+CADD1 + ",");
								}
								if(CSTATE.equals("")) {
									parameters.put("fromAddress_Country",  CCOUNTRY);
								}else {
									parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
								}
						}
						
						parameters.put("fromAddress_ZIPCode", CZIP);
						parameters.put("fromAddress_TpNo", CTEL);
						parameters.put("fromAddress_FaxNo", CFAX);
						parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
						parameters.put("fromAddress_ContactPersonMobile", CHPNO);
						parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
						parameters.put("fromAddress_RCBNO", CRCBNO);
						parameters.put("fromAddress_UENNO", companyregno);//imtiuen
						//parameters.put("currentTime", _DOUtil.getOrderDate(PLANT, DONO));
						parameters.put("currentTime", _DOUtil.getOrderDateOnly(PLANT, DONO));
						parameters.put("taxInvoiceNo", "");
						parameters.put("InvoiceTerms", "");
//						parameters.put("refNo", _DOUtil.getJobNum(PLANT, DONO));
//						if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
//						String rfno = "";
//						if(DONO.length()>0)
//							rfno ="SO: "+DONO;
//						if(GINO.length()>0)
//							rfno =rfno+",DO: "+GINO;
//						parameters.put("refNo", rfno);
//						}else {
							parameters.put("refNo", _DOUtil.getJobNum(PLANT, DONO));
//						}
						arraydohdr = _DOUtil.getDoHdrDetails(query, htCond);
						Hashtable curHash = new Hashtable();
						curHash.put(IDBConstants.PLANT, PLANT);
						curHash.put(IDBConstants.CURRENCYID, dataMap.get("currencyid"));
						String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
						OrderTypeDAO ODAO = new OrderTypeDAO();
						String orderDesc = "";

						DOUtil poUtil = new DOUtil();
						Map ma = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT, "OUTBOUND ORDER");
						Orientation = (String) ma.get("PrintOrientation");
						PRINTDELIVERYNOTE = (String)ma.get("PRINTDELIVERYNOTE");
						PRINTPACKINGLIST = (String)ma.get("PRINTPACKINGLIST"); 
						String orderType = doheader.getORDERTYPE();
						if(orderType.equals(""))
							orderType="OUTBOUND ORDER";
						if (orderType.equals("OUTBOUND ORDER")) {
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
						parameters.put("InvoiceNoHdr", (String) ma.get("INVOICENO"));
						parameters.put("InvoiceDate", (String) ma.get("INVOICEDATE"));
						parameters.put("RefNo", (String) ma.get("REFNO"));
						parameters.put("Terms", (String) ma.get("TERMS"));
						parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
//						if (ma.get("PRINTCUSTERMS").equals("1")) {
							//parameters.put("TermsDetails", sPayTerms);
							parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
//						} else {
//							parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
//						}
						parameters.put("SoNo", (String) ma.get("SONO"));
						parameters.put("Item", (String) ma.get("ITEM"));
						parameters.put("Description", (String) ma.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
						parameters.put("UOM", (String) ma.get("UOM"));
						;
						parameters.put("Rate", (String) ma.get("RATE"));
						parameters.put("TaxAmount", (String) ma.get("TAXAMOUNT"));
						parameters.put("Amt", (String) ma.get("AMT"));
						parameters.put("SubTotal", (String) ma.get("SUBTOTAL") + " " + "(" + curDisplay + ")");
						String taxby = _PlantMstDAO.getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
						if (taxby.equalsIgnoreCase("BYORDER")) {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
						} else {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
						}
						parameters.put("Total", (String) ma.get("TOTAL") + " " + "(" + curDisplay + ")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", (String) ma.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
						
						//changes start : IMTHI
						//payment made displays from sales order number (dono)
						if (request.getParameter("printwithinvoiceno") == null ) {
						InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
						double orderadvanceamt = invoicePaymentDAO.getcreditamoutusingorderno(PLANT, DONO);
						double paidamountfororder = invoicePaymentDAO.getpaidamoutusingorderno(PLANT, DONO);	
						double pdcamount = 0.0;
						InvPaymentDetail InvPaymentDetail = new InvPaymentDetail();
						List<InvPaymentDetail> invdetlist = invoicePaymentDAO.getInvoicePaymentDetailsbydono(DONO, PLANT, dataMap.get("CRBY").toString());
						for(InvPaymentDetail invdet:invdetlist){
							Hashtable ht = new Hashtable();	
							ht.put("PAYMENTID",String.valueOf(invdet.getRECEIVEHDRID()));
							ht.put("PLANT",PLANT);
							List pdcDetailListpc = invoicePaymentDAO.getpdcbipayid(ht);
							for(int k =0; k < pdcDetailListpc.size(); k++) {
								Map pdcdet=(Map)pdcDetailListpc.get(k);
								String status = (String)pdcdet.get("STATUS");
								if(status.equalsIgnoreCase("NOT PROCESSED")) {
									pdcamount = pdcamount+(Double.parseDouble((String)pdcdet.get("CHEQUE_AMOUNT")) * Double.parseDouble((String)pdcdet.get("CURRENCYUSEQT")));
								}
							}
						}
						String invoiceAmount=StrUtils.addZeroes(((orderadvanceamt + paidamountfororder + pdcamount)), numberOfDecimal);
						parameters.put("PaymentMade", invoiceAmount);
						parameters.put("BalanceDue", invoiceAmount);
						}else {
						//payment made displays from invoice number if reports in print with invoice
						InvoicePaymentDAO InvPaymentdao = new InvoicePaymentDAO();
			        	double paymentmade=InvPaymentdao.getbalacedue(PLANT, ID);
			        	double paymentMadeloc =paymentmade;
			        	paymentmade=(paymentmade*Float.parseFloat(currencyuseqt));
			        	String Spaymentmade = Numbers.toMillionFormat(paymentmade, numberOfDecimal);
			        	parameters.put("PaymentMade",Spaymentmade);
			        	parameters.put("BalanceDue", Spaymentmade);
						}
						parameters.put("PRINTPAYMENTMADE", (String) ma.get("PRINTPAYMENTMADE"));
						parameters.put("PRINTADJUSTMENT", (String) ma.get("PRINTADJUSTMENT"));
						parameters.put("PRINTSHIPPINGCOST", (String) ma.get("PRINTSHIPPINGCOST"));
						parameters.put("PRINTORDERDISCOUNT", (String) ma.get("PRINTORDERDISCOUNT"));
						parameters.put("PRINTBALANCEDUE", (String) ma.get("PRINTBALANCEDUE"));
						parameters.put("BalancedueHdr", (String) ma.get("BALANCEDUE"));
						parameters.put("PaymentMadeHdr", (String) ma.get("PAYMENTMADE"));
						//changes end : imthi
						
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
						parameters.put("PRINTWITHINCOTERM", (String) ma.get("PRINTINCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
						parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
						String both =(String) ma.get("PRINTWITHUENNO");//by imthi to disply both uen and gst
						if(both.equalsIgnoreCase("2")) {
							parameters.put("RCBNO", ", "+(String) ma.get("RCBNO"));
						}else {
							parameters.put("RCBNO", (String) ma.get("RCBNO"));
						}
						parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
						parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
						parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
						parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
						parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
						parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));//azees
						
						int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
						if(PRINTWITHSHIPINGADD == 1) {
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
							// ship to Address
							/*
							 * if(sShipCustName.length()>0) { parameters.put("STo_CustName", "SHIP TO : "+
							 * "\n"+ "\n"+ sShipCustName);
							 * 
							 * }else{ parameters.put("STo_CustName", sShipCustName); }
							 */
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
					} else {
						parameters.put("shipToId", "");
					}
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						parameters.put("trans", transportmode);
						
						if (ma.get("PRINTCUSTERMS").equals("1")) {
							parameters.put("payterms", pays);
							} else {
								parameters.put("payterms", paymentterms);
							}
//						parameters.put("payterms", paymentterms);
						parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
						parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
						parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
						//end
						
						parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
						parameters.put("INVOICEDATE", (String) ma.get("INVOICEDATE"));
						parameters.put("GINODATELabel", (String) ma.get("GINODATE"));
						if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null) {
							parameters.put("printwithigino", "no");
							parameters.put("printwithinvoiceno", "no");
							if (request.getParameter("printwithinvoiceno") != null)
							{
								parameters.put("printwithinvoiceno", "yes");
							//if (request.getParameter("printwithigino") != null)
								parameters.put("printwithigino", "yes");
							}
							parameters.put("GINOLabel", (String) ma.get("GINO"));
							parameters.put("GINO", GINO);
							parameters.put("INVOICENOLabel", (String) ma.get("INVOICENO"));
							parameters.put("INVOICENO", INVOICENO);
							if (INVOICENO.length() == 0)
							{
							parameters.put("ISSUEDATE", "");
							}else{
							parameters.put("ISSUEDATE", ISSUEDATE);
							}
							if (GINO.length() == 0)
							{
							parameters.put("GINODATE", "");
							}else{
							parameters.put("GINODATE", GINODATE);
							}
							//parameters.put("ISSUEDATE", ISSUEDATE);
						}else {
							if (request.getParameter("printwithinvoiceno") == null)
							{
								parameters.put("printwithigino", "yes");
							}
//							parameters.put("printwithigino", "no");
							parameters.put("printwithinvoiceno", "no");
							parameters.put("ISSUEDATE", "");
							parameters.put("INVOICENO", "");
							parameters.put("GINOLabel", (String) ma.get("GINO"));
							parameters.put("GINO", GINO);
							parameters.put("GINODATE", GINODATE);
						}
						parameters.put("HSCODE", (String) ma.get("HSCODE"));
						parameters.put("COO", (String) ma.get("COO"));
						parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
						parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
						if (request.getParameter("printwithinvoiceno") != null) //new changes - azees 12/2020
							parameters.put("IssueDateRange", sConditioninv);
						else
							parameters.put("IssueDateRange", sCondition);
						if (ma.get("PCUSREMARKS").equals("1")) {
							parameters.put("SupRemarks", sRemarks);
						} else {
							parameters.put("SupRemarks", "");
						}
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
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
						String DISPLAYSIGNATURE= (String) ma.get("DISPLAYSIGNATURE");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(DISPLAYSIGNATURE.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
						parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("CALCULATETAXWITHSHIPPINGCOST",  ma.get("CALCULATETAXWITHSHIPPINGCOST"));
						parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
						if (Orientation.equals("Portrait")) {

							jasperPath += "Portrait";

						}

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
						String querylist;
						querylist = "item,itemdesc,unitprice";
						DoDetDAO dodetdao = new DoDetDAO();						
						ArrayList arraydodet = dodetdao.selectDoDet(querylist, htCond);
						if(arraydodet.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						
						
						long start = System.currentTimeMillis();
						System.out.println("**************" + " Start Up Time : " + start + "**********");
						System.out.println("jasperPath : " + jasperPath);
						JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");

						jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
						//In Landscape, Check if delivery note is enabled -- Blocked by Azees 28.8.20 - Block Removed on 28.7.22
						if ("1".equals(PRINTDELIVERYNOTE) && (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null)) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDNWITHBATCH";
							parameters.put("IssueDateRange", sCondition);
							parameters.put("OrderHeader", "Delivery Note");
							parameters.put("DeliveryNoteNumber", "Delivery Note Number");
							parameters.put("PartNo", "Part No");
							parameters.put("HSCode", "HS Code");
							parameters.put("COO", "COO");
							parameters.put("NetWeight", "Net Weight");
							parameters.put("GrossWeight", "Gross Weight");
							parameters.put("Dimension", "Dimension");
							parameters.put("Packing", "Packing");
							DNPLHdrDAO _DnPLHdrDAO = new DNPLHdrDAO();
							Hashtable htDnPlCondition = new Hashtable();
							htDnPlCondition.put("PLANT", PLANT);
							htDnPlCondition.put("DONO", DONO);
							htDnPlCondition.put("INVOICENO", GINO);
							ArrayList aldnpl = _DnPLHdrDAO.selectdnplDNPLHDR("*", htDnPlCondition);
							Iterator iterdnpl = aldnpl.iterator();
							while(iterdnpl.hasNext()) {
								Map mapdnpl = (Map)iterdnpl.next();
								//parameters.put("DeliveryNoteNo", mapdnpl.get("DNNO"));
								//parameters.put("TotalDimension", mapdnpl.get("NETDIMENSION"));
								//parameters.put("TotalPacking", mapdnpl.get("NETPACKING"));
								//parameters.put("TotalNetWeight", mapdnpl.get("TOTALNETWEIGHT"));
								//parameters.put("TotalGrossWeight", mapdnpl.get("TOTALGROSSWEIGHT"));
								//parameters.put("INVOICENO", mapdnpl.get("INVOICENO"));
								parameters.put("DeliveryNoteNo", mapdnpl.get("DELIVERYNOTE"));
								parameters.put("TotalDimension", mapdnpl.get("TOTAlDIMENSION"));
								parameters.put("TotalPacking", mapdnpl.get("TOTAlPACKING"));
								parameters.put("TotalNetWeight", mapdnpl.get("TOTAlNETWEIGHT"));
								parameters.put("TotalGrossWeight", mapdnpl.get("TOTAlGROSSWEIGHT"));
								parameters.put("INVOICENO", mapdnpl.get("GINO"));
								parameters.put("InvoiceNumber", "GINO ");
								parameters.put("InvoiceDate", "GINODATE ");
								parameters.put("InvoiceDt", GINODATE);
								
								String sSTATUS="",STATUSDATE="",DateLable="";
								parameters.put("DNNOTE", " "+mapdnpl.get("NOTE"));
								parameters.put("DNNOTELABLE", "Notes :");
								String SHIPSTATUS =(String)mapdnpl.get("SHIPPING_STATUS");
								String SHIPDATE =(String)mapdnpl.get("SHIPPING_DATE");
								String INTRANSITSTATUS =(String)mapdnpl.get("INTRANSIT_STATUS");
								String INTRANSITDATE =(String)mapdnpl.get("INTRANSIT_DATE");
								String DELIVERYSTATUS =(String)mapdnpl.get("DELIVERY_STATUS");
								String DELIVERYDATE =(String)mapdnpl.get("DELIVERY_DATE");
								String TRANSPORT =(String)mapdnpl.get("TRANSPORTID");
								
								String CARRIER =(String)mapdnpl.get("CARRIER");
								String FREIGHT_FORWARDERID =(String)mapdnpl.get("FREIGHT_FORWARDERID");
								String DURATIONOFJOURNEY =(String)mapdnpl.get("DURATIONOFJOURNEY");
								String TRACKINGNO =(String)mapdnpl.get("TRACKINGNO");
								
								int trans = Integer.valueOf(TRANSPORT);

								if(trans > 0){
									transportmode = transportmodedao.getTransportModeById(PLANT, Integer.valueOf(TRANSPORT));
								}else{
									transportmode = "";
								}
								
								if(SHIPSTATUS==null){SHIPSTATUS="";}if(SHIPDATE==null){SHIPDATE="";}
								if(INTRANSITSTATUS==null){INTRANSITSTATUS="";}if(INTRANSITDATE==null){INTRANSITDATE="";}
								if(DELIVERYSTATUS==null){DELIVERYSTATUS="";}if(DELIVERYDATE==null){DELIVERYDATE="";}
								if(CARRIER==null){CARRIER="";}if(FREIGHT_FORWARDERID==null){FREIGHT_FORWARDERID="";}
								if(DURATIONOFJOURNEY==null){DURATIONOFJOURNEY="";}if(TRACKINGNO==null){TRACKINGNO="";}
								
									
							if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
								sSTATUS = SHIPSTATUS;
								STATUSDATE = SHIPDATE;
								DateLable = sSTATUS+" Date";
							}else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
								sSTATUS = DELIVERYSTATUS;
								STATUSDATE = DELIVERYDATE;
								DateLable = sSTATUS+" Date";
				           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = INTRANSITSTATUS;
				        	   STATUSDATE = INTRANSITDATE;
				        	   DateLable = sSTATUS+" Date";
				           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = INTRANSITSTATUS;
				        	   STATUSDATE = INTRANSITDATE;
				        	   DateLable = sSTATUS+" Date";
				           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = DELIVERYSTATUS;
				        	   STATUSDATE = DELIVERYDATE;
				        	   DateLable = sSTATUS+" Date";
				           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = DELIVERYSTATUS;
				        	   STATUSDATE = DELIVERYDATE;
				        	   DateLable = sSTATUS+" Date";
				           }
									
								parameters.put("CARRIER", CARRIER);
								parameters.put("CARRIERLABLE", "Carrier");
								parameters.put("FREIGHT_FORWARDER", FREIGHT_FORWARDERID);
								parameters.put("FREIGHTLABLE", "Freight Forwarder");
								parameters.put("DURATIONOFJOURNEY", DURATIONOFJOURNEY);
								parameters.put("JOURNEYLABLE", "Duration of Journey");
								parameters.put("TRACKINGNO", TRACKINGNO);
								parameters.put("TRACKINGLABLE", "Tracking No");
								parameters.put("TRANSPORT", transportmode);
								parameters.put("TRANSPORTLABLE", "Transport");
								parameters.put("STATUS", sSTATUS);
								parameters.put("STATUSLABLE", "Status");
								parameters.put("STATUSDATE", STATUSDATE);
								parameters.put("STATUSDATELABLE", DateLable);
							
								JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
								jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
							}
						}
						//In Landscape, Check if packing list is enabled
						  if ("1".equals(PRINTPACKINGLIST) &&  request.getParameter("printwithinvoiceno") != null) {
							  jasperPath = DbBean.JASPER_INPUT + "/" + "rptPLWITHBATCH";
							  	parameters.put("IssueDateRange", sCondition);
								parameters.put("OrderHeader", "Packing List");
								parameters.put("PackingListNumber", "Packing List Number");
								parameters.put("PartNo", "Part No");
								parameters.put("HSCode", "HS Code");
								parameters.put("COO", "COO");
								parameters.put("NetWeight", "Net Weight");
								parameters.put("GrossWeight", "Gross Weight");
								parameters.put("Dimension", "Dimension");
								parameters.put("Packing", "Packing");
								DNPLHdrDAO _DnPLHdrDAO = new DNPLHdrDAO();
								Hashtable htDnPlCondition = new Hashtable();
								htDnPlCondition.put("PLANT", PLANT);
								htDnPlCondition.put("DONO", DONO);
								htDnPlCondition.put("INVOICENO", GINO);
								ArrayList aldnpl = _DnPLHdrDAO.selectdnplDNPLHDR("*", htDnPlCondition);
								Iterator iterdnpl = aldnpl.iterator();
								while(iterdnpl.hasNext()) {
									Map mapdnpl = (Map)iterdnpl.next();
									parameters.put("PackingListNo", mapdnpl.get("PACKINGLIST"));
									parameters.put("TotalDimension", mapdnpl.get("TOTAlDIMENSION"));
									parameters.put("TotalPacking", mapdnpl.get("TOTAlPACKING"));
									parameters.put("TotalNetWeight", mapdnpl.get("TOTAlNETWEIGHT"));
									parameters.put("TotalGrossWeight", mapdnpl.get("TOTAlGROSSWEIGHT"));
									parameters.put("INVOICENO", mapdnpl.get("GINO"));
									
									String sSTATUS="",STATUSDATE="",DateLable="";
									parameters.put("DNNOTE", " "+mapdnpl.get("NOTE"));
									parameters.put("DNNOTELABLE", "Notes :");
									String SHIPSTATUS =(String)mapdnpl.get("SHIPPING_STATUS");
									String SHIPDATE =(String)mapdnpl.get("SHIPPING_DATE");
									String INTRANSITSTATUS =(String)mapdnpl.get("INTRANSIT_STATUS");
									String INTRANSITDATE =(String)mapdnpl.get("INTRANSIT_DATE");
									String DELIVERYSTATUS =(String)mapdnpl.get("DELIVERY_STATUS");
									String DELIVERYDATE =(String)mapdnpl.get("DELIVERY_DATE");
									String TRANSPORT =(String)mapdnpl.get("TRANSPORTID");
									
									String CARRIER =(String)mapdnpl.get("CARRIER");
									String FREIGHT_FORWARDERID =(String)mapdnpl.get("FREIGHT_FORWARDERID");
									String DURATIONOFJOURNEY =(String)mapdnpl.get("DURATIONOFJOURNEY");
									String TRACKINGNO =(String)mapdnpl.get("TRACKINGNO");
									
									int trans = Integer.valueOf(TRANSPORT);

									if(trans > 0){
										transportmode = transportmodedao.getTransportModeById(PLANT, Integer.valueOf(TRANSPORT));
									}else{
										transportmode = "";
									}
									
									if(SHIPSTATUS==null){SHIPSTATUS="";}if(SHIPDATE==null){SHIPDATE="";}
									if(INTRANSITSTATUS==null){INTRANSITSTATUS="";}if(INTRANSITDATE==null){INTRANSITDATE="";}
									if(DELIVERYSTATUS==null){DELIVERYSTATUS="";}if(DELIVERYDATE==null){DELIVERYDATE="";}
									if(CARRIER==null){CARRIER="";}if(FREIGHT_FORWARDERID==null){FREIGHT_FORWARDERID="";}
									if(DURATIONOFJOURNEY==null){DURATIONOFJOURNEY="";}if(TRACKINGNO==null){TRACKINGNO="";}
									
										
								if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
									sSTATUS = SHIPSTATUS;
									STATUSDATE = SHIPDATE;
									DateLable = sSTATUS+" Date";
								}else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
									sSTATUS = DELIVERYSTATUS;
									STATUSDATE = DELIVERYDATE;
									DateLable = sSTATUS+" Date";
					           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = INTRANSITSTATUS;
					        	   STATUSDATE = INTRANSITDATE;
					        	   DateLable = sSTATUS+" Date";
					           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = INTRANSITSTATUS;
					        	   STATUSDATE = INTRANSITDATE;
					        	   DateLable = sSTATUS+" Date";
					           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = DELIVERYSTATUS;
					        	   STATUSDATE = DELIVERYDATE;
					        	   DateLable = sSTATUS+" Date";
					           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = DELIVERYSTATUS;
					        	   STATUSDATE = DELIVERYDATE;
					        	   DateLable = sSTATUS+" Date";
					           }
										
									parameters.put("CARRIER", CARRIER);
									parameters.put("CARRIERLABLE", "Carrier");
									parameters.put("FREIGHT_FORWARDER", FREIGHT_FORWARDERID);
									parameters.put("FREIGHTLABLE", "Freight Forwarder");
									parameters.put("DURATIONOFJOURNEY", DURATIONOFJOURNEY);
									parameters.put("JOURNEYLABLE", "Duration of Journey");
									parameters.put("TRACKINGNO", TRACKINGNO);
									parameters.put("TRACKINGLABLE", "Tracking No");
									parameters.put("TRANSPORT", transportmode);
									parameters.put("TRANSPORTLABLE", "Transport");
									parameters.put("STATUS", sSTATUS);
									parameters.put("STATUSLABLE", "Status");
									parameters.put("STATUSDATE", STATUSDATE);
									parameters.put("STATUSDATELABLE", DateLable);
									
									//parameters.put("PackingListNo", mapdnpl.get("PLNO"));
									//parameters.put("TotalDimension", mapdnpl.get("NETDIMENSION"));
									//parameters.put("TotalPacking", mapdnpl.get("NETPACKING"));
									//parameters.put("TotalNetWeight", mapdnpl.get("TOTALNETWEIGHT"));
									//parameters.put("TotalGrossWeight", mapdnpl.get("TOTALGROSSWEIGHT"));
									//parameters.put("INVOICENO", mapdnpl.get("INVOICENO"));
//									parameters.put("InvoiceNumber", "Invoice No. ");
//									parameters.put("InvoiceDate", "Invoice Dt. ");
									parameters.put("InvoiceNumber", "GINO ");
									parameters.put("InvoiceDate", "GINODATE ");
									parameters.put("InvoiceDt", ISSUEDATE);
									JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
									jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
						  }
						  }

					}
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
			if (!ajax) {
				response.addHeader("Content-disposition", "inline;filename=MultipleInvoice.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}else {
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + DONO + ".pdf")){
					fos.write(bytes);
				}
			}
			
			

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	
	}

	protected void viewDNPLReport(HttpServletRequest request, HttpServletResponse response, String action)
			throws IOException, Exception {

		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			DOUtil _DOUtil = new DOUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			cUtil.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			InvoiceDAO invoicedao = new InvoiceDAO();
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List listQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) listQry.get(0);
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "", companyregno = "",SEALNAME="",SIGNNAME="",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",CWEBSITE = "",COMP_INDUSTRY="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "";
			String DONO = "";
			String chkdDoNo = request.getParameter("DONO");
			if (ajax) {
				chkdDoNo = (String)request.getAttribute("chkdDoNo");
			}
			String PLANT = (String) session.getAttribute("PLANT");
			String jasperPath = "";
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			// ********To get from & to date issue date range*********************
			String dtCondStr = "  and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
			String sCondition = "",sConditioninv = "", fdate = "", tdate = "", signaturePath = "";
			String dtCondStrinv = "  and ISNULL(INVOICE_DATE,'')<>'' AND CAST((SUBSTRING(INVOICE_DATE, 7, 4) + '-' + SUBSTRING(INVOICE_DATE, 4, 2) + '-' + SUBSTRING(INVOICE_DATE, 1, 2)) AS date)";
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
			String imagePath2, Orientation = "", PRINTDELIVERYNOTE = "", PRINTPACKINGLIST = "";

			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}

			con = DbBean.getConnection();

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
				COMP_INDUSTRY = (String) map.get("COMP_INDUSTRY");

			}
			DOUtil poUtil1 = new DOUtil();
			Map ma1 = poUtil1.getDOReceiptInvoiceHdrDetailsDO(PLANT, "OUTBOUND ORDER");//Azees Fix Query Load 09.22
				//	Get all INVOICENOs for this DO from SHIPHIS table
				String query;
				String queryimg= "DISTINCT isnull(SIGNATURENAME, '') as SIGNATURENAME,isnull(PDASIGNPATH, '') as PDASIGNPATH,isnull(invoiceno, '') as GINO,ISNULL(issuedate,'') as GINODATE,ISNULL((Select top 1 B.INVOICE from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as INVOICENO,ISNULL((Select top 1 INVOICE_DATE from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as ISSUEDATE,ISNULL((Select top 1 SHIPPINGCOST from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as DATASHIPPINGCOST,ISNULL((Select top 1 ORDER_DISCOUNT from "+PLANT+"_FININVOICEHDR B WHERE B.DONO=A.DONO and B.GINO=A.INVOICENO),'') as DATAORDERDISCOUNT ";
				DONO = chkdDoNo;
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("DONO", DONO);
				ArrayList alShipDetail = new ArrayList();
				if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null || request.getParameter("printwithinvoiceno") == null) {
					//alShipDetail = dOUtil.getOBIssueList(query, htCond, " AND invoiceno!=''");					
					alShipDetail = dOUtil.getOBIssueList(queryimg, htCond, " and (invoiceno!='' or  invoiceno is not null) "+sCondition+" ORDER BY INVOICENO DESC");
				}
				for(int j = 0; j < ((request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null || request.getParameter("printwithinvoiceno") == null ) ? alShipDetail.size() : 1); j ++) {
					String INVOICENO = "",ISSUEDATE="",GINO="",GINODATE="",DATAORDERDISCOUNT="0",DATASHIPPINGCOST="0",
							ORDERDISCOUNTTYPE = "",AdjustmentAmount="",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",
							ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",ISTAXINCLUSIVE="",PROJECT_NAME="",sales_location="",empno="",ID="",currencyuseqt="",PDASIGNPATH="",SIGNATURENAME="";
							int TAXID=0;
					if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null || request.getParameter("printwithinvoiceno") == null ) {
						INVOICENO = ((Map)alShipDetail.get(j)).get("INVOICENO").toString();
						ISSUEDATE = ((Map)alShipDetail.get(j)).get("ISSUEDATE").toString();
						GINO = ((Map)alShipDetail.get(j)).get("GINO").toString();
						GINODATE = ((Map)alShipDetail.get(j)).get("GINODATE").toString();
						DATAORDERDISCOUNT = ((Map)alShipDetail.get(j)).get("DATAORDERDISCOUNT").toString();
						DATASHIPPINGCOST = ((Map)alShipDetail.get(j)).get("DATASHIPPINGCOST").toString();
					}
					ArrayList signDetail = new ArrayList();
					signDetail = dOUtil.getOBIssueList(queryimg, htCond, " and (invoiceno!='' or  invoiceno is not null) "+sCondition+" ORDER BY INVOICENO DESC");
					PDASIGNPATH = ((Map)signDetail.get(j)).get("PDASIGNPATH").toString();
					SIGNATURENAME = ((Map)signDetail.get(j)).get("SIGNATURENAME").toString();
					 if(PDASIGNPATH.equalsIgnoreCase("")){
						 PDASIGNPATH = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			            }
					if (request.getParameter("printwithinvoiceno") != null && INVOICENO.isEmpty())
					{
						if(request.getParameter("printwithigino") == null)
						continue;
					}
					ArrayList alinvc = new ArrayList();
					if (request.getParameter("printwithinvoiceno") != null)
					{
						Hashtable htinv = new Hashtable();
						htinv.put("PLANT", PLANT);
						String querybill="Select isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
								+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_FININVOICEHDR.TAXID),'') TAX_TYPE,ISNULL(EMPNO,'') EMPNO,"
								+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_FININVOICEHDR.PROJECTID),'') PROJECT_NAME,"
								+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_FININVOICEHDR.sales_location),'') sales_location,"
								+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(TAXID,0) TAXID, "
								+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(ORDER_DISCOUNT,0) ELSE (isnull(ORDER_DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
								+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
								+ "ISNULL(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,ISNULL(ID,'') ID,ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT "
								+ "FROM "+PLANT+"_FININVOICEHDR WHERE INVOICE='"+INVOICENO+"' ";
						alinvc=invoicedao.selectForReport(querybill, htinv, "");
						if(alinvc.size()>0)
						{
							ORDERDISCOUNTTYPE = ((Map)alinvc.get(0)).get("ORDERDISCOUNTTYPE").toString();
							DISCOUNT_TYPE = ((Map)alinvc.get(0)).get("DISCOUNT_TYPE").toString();
							ISTAXINCLUSIVE = ((Map)alinvc.get(0)).get("ISTAXINCLUSIVE").toString();
							TAX_TYPE = ((Map)alinvc.get(0)).get("TAX_TYPE").toString();							
							ISDISCOUNTTAX = ((Map)alinvc.get(0)).get("ISDISCOUNTTAX").toString();
							ISSHIPPINGTAX = ((Map)alinvc.get(0)).get("ISSHIPPINGTAX").toString();
							ISORDERDISCOUNTTAX= ((Map)alinvc.get(0)).get("ISORDERDISCOUNTTAX").toString();
							AdjustmentAmount = ((Map)alinvc.get(0)).get("ADJUSTMENT").toString();
							DATAORDERDISCOUNT = ((Map)alinvc.get(0)).get("orderdiscount").toString();
							DATASHIPPINGCOST = ((Map)alinvc.get(0)).get("shippingcost").toString();
							DISCOUNT = ((Map)alinvc.get(0)).get("DISCOUNT").toString();
							TAXID = Integer.valueOf(((Map)alinvc.get(0)).get("TAXID").toString());
							PROJECT_NAME=((Map)alinvc.get(0)).get("PROJECT_NAME").toString();
							sales_location=((Map)alinvc.get(0)).get("sales_location").toString();
							empno=((Map)alinvc.get(0)).get("EMPNO").toString();
							ID=((Map)alinvc.get(0)).get("ID").toString();
							currencyuseqt=((Map)alinvc.get(0)).get("CURRENCYUSEQT").toString();
						}
					}
					String invoice = INVOICENO;
					String printwithbatch="0"; //new changes - azees 12/2020
					signaturePath = DbBean.COMPANY_SIGN_PATH + "/" + PLANT.toLowerCase() + "/" + "sign" + DONO + ".bmp";
					File checkSignatureFile = new File(signaturePath);
					if (!checkSignatureFile.exists()) {
						signaturePath = DbBean.COMPANY_LOGO_PATH + "/" + "NoLogo.jpg";
					}
					ArrayList arrCust = cUtil.getCustomerDetailsForDO(DONO, PLANT);
					if (arrCust.size() > 0) {
						String sCustCode = (String) arrCust.get(0);
						String sCustName = (String) arrCust.get(1);
						String sAddr1 = (String) arrCust.get(2);
						String sAddr2 = (String) arrCust.get(3);
						String sAddr3 = (String) arrCust.get(4);
						String sCountry = (String) arrCust.get(5);
						String sZip = (String) arrCust.get(6);
						String sContactName = (String) arrCust.get(9);
						String sTelNo = (String) arrCust.get(11);
						String sFax = (String) arrCust.get(13);
						String sEmail = (String) arrCust.get(14);
						String sRemarks = (String) arrCust.get(15);
						String sAddr4 = (String) arrCust.get(16);
						String pays = (String) arrCust.get(28);

						String SHIPPINGID = (String) arrCust.get(25);
						String sRcbno = (String) arrCust.get(26);
						String suenno = (String) arrCust.get(27);//imtiuen
						ArrayList arrShippingDetails = _masterUtil.getOutboundShippingDetails(DONO, sCustCode, PLANT);
						Map parameters = new HashMap();
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", sCustCode);
							SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
							SHPCONTACTNAME = (String) arrShippingDetails.get(2);
							SHPTELEPHONE = (String) arrShippingDetails.get(3);
							SHPHANDPHONE = (String) arrShippingDetails.get(4);
							SHPUNITNO = (String) arrShippingDetails.get(7);
							SHPBUILDING = (String) arrShippingDetails.get(8);
							SHPSTREET = (String) arrShippingDetails.get(9);
							SHPCITY = (String) arrShippingDetails.get(10);
							SHPSTATE = (String) arrShippingDetails.get(11);
							SHPCOUNTRY = (String) arrShippingDetails.get(12);
							SHPPOSTALCODE = (String) arrShippingDetails.get(13);
						} else {
							parameters.put("shipToId", "");
						}
						if (request.getParameter("with_batch") != null
								&& "yes".equals(request.getParameter("with_batch"))) {
							parameters.put("PrintBatch", "yes");
						}
						String sShipCustName = "";
						
						query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,"
								+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
								+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
								+ "isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') as PAYMENTTYPE,isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost," + "isnull(incoterms,'') incoterms,"
								+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(TAXID,0) TAXID,ISNULL(EMPNO,'') EMPNO,"
								+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_dohdr.TAXID),'') TAX_TYPE,"
								+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_dohdr.PROJECTID),'') PROJECT_NAME,"
								+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_dohdr.sales_location),'') sales_location,"
								+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
								+ "ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,STATUS";
						ArrayList arraydohdr = new ArrayList();
						arraydohdr = dOUtil.getDoHdrDetails(query, htCond);
						Map dataMap = (Map) arraydohdr.get(0);
						String gstValue = dataMap.get("outbound_Gst").toString();
						
						if (request.getParameter("printwithinvoiceno") == null)
						{
							DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
							DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
							ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
							DISCOUNT_TYPE=dataMap.get("DISCOUNT_TYPE").toString();
							ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
							ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
							ISORDERDISCOUNTTAX=dataMap.get("ISORDERDISCOUNTTAX").toString();
							DISCOUNT=dataMap.get("DISCOUNT").toString();
							AdjustmentAmount=dataMap.get("ADJUSTMENT").toString();
							ISTAXINCLUSIVE=dataMap.get("ISTAXINCLUSIVE").toString();
							String POSTATUS = (String) dataMap.get("STATUS").toString();
							if(!POSTATUS.equalsIgnoreCase("C"))
							{
							DATASHIPPINGCOST = invoicedao.getActualShippingCostForinvoice(PLANT, DONO);
							if(!ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
								DATAORDERDISCOUNT = invoicedao.getActualOrderDiscountCostForinvoice(PLANT, DONO);
							}
							PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
							sales_location=dataMap.get("sales_location").toString();
							TAX_TYPE = dataMap.get("TAX_TYPE").toString();
							TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
							empno=dataMap.get("EMPNO").toString();
						}
						//new changes - azees 12/2020
						parameters.put("fromAddress_Website", " "+CWEBSITE);
						parameters.put("printwithbatch",printwithbatch);
						parameters.put("ISTAXINCLUSIVE", ISTAXINCLUSIVE);
						double doubledatabilldiscount = new Double(DISCOUNT);
						parameters.put("DO_DISCOUNT", doubledatabilldiscount);
						parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
						parameters.put("DO_DISCOUNT_TYPE", DISCOUNT_TYPE);
						parameters.put("AdjustmentAmount", AdjustmentAmount);
						parameters.put("ISDISCOUNTTAX", ISDISCOUNTTAX);
						parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
						parameters.put("ISORDERDISCOUNTTAX", ISORDERDISCOUNTTAX);
						parameters.put("PROJECTNAME", PROJECT_NAME);
						parameters.put("invoice", invoice);
						
						parameters.put("STo", (String) ma1.get("SHIPTO"));
						parameters.put("STo_Addr1", SHPUNITNO);
						parameters.put("STo_Addr2", SHPBUILDING);
						parameters.put("STo_Addr3", SHPSTREET);
						parameters.put("STo_City", SHPCITY);
						parameters.put("STo_Country", SHPSTATE + ", " + SHPCOUNTRY);
						parameters.put("STo_ZIP", SHPPOSTALCODE);
						parameters.put("STo_Telno", SHPTELEPHONE);
						parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						if (SHPCONTACTNAME.length() > 0)
							parameters.put("STo_Telno", "Tel: " + SHPTELEPHONE);
						else
							parameters.put("STo_Telno", SHPTELEPHONE);
						if (SHPCUSTOMERNAME.length() > 0) {
							parameters.put("STo_AttentionTo", "Attn: " + SHPCONTACTNAME);
						} else {
							parameters.put("STo_AttentionTo", SHPCONTACTNAME);
						}

						/**/
						if (SHPHANDPHONE.length() > 0) {
							parameters.put("SHPHANDPHONE", "HP: " + SHPHANDPHONE);
						} else {
							parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						}
						
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
							gstValue="0.0";
						}
		  				parameters.put("TAX_TYPE", TAX_TYPE);
						
						String DATAINCOTERMS = dataMap.get("incoterms").toString();
						String orderRemarks = (String) arrCust.get(18);
						String orderRemarks3 = (String) arrCust.get(21);
						String sDeliveryDate = (String) arrCust.get(22);
						String sEmpno = (String) arrCust.get(23);
						String sState = (String) arrCust.get(24);
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);

						parameters.put("signaturePath", signaturePath);
						parameters.put("OrderNo", DONO);
						parameters.put("company", PLANT);
						parameters.put("taxInvoiceTo_CompanyName", sCustName);
						parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
						parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
						if (sState.equals("")) {
							parameters.put("taxInvoiceTo_Country",sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country", sState + "\n" + sCountry);	
						}
						parameters.put("taxInvoiceTo_ZIPCode", sZip);
						parameters.put("taxInvoiceTo_AttentionTo", sContactName);
						parameters.put("taxInvoiceTo_CCTO", "");
						parameters.put("To_TelNo", sTelNo);
						parameters.put("To_Fax", sFax);
						parameters.put("To_Email", sEmail);
						parameters.put("sRCBNO", sRcbno);
						parameters.put("sUENNO", suenno);//imtiuen
						parameters.put("usersignature", PDASIGNPATH);
						parameters.put("usersignname", SIGNATURENAME);
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						DoHdrDAO doHdrDAO= new DoHdrDAO();
						DoHdr doheader = doHdrDAO.getDoHdrById(PLANT, DONO);
						TransportModeDAO transportmodedao = new TransportModeDAO();
						String transportmode = "";
						if(doheader.getTRANSPORTID() > 0){
							transportmode = transportmodedao.getTransportModeById(PLANT, doheader.getTRANSPORTID());
						}
						String paymentterms = "";
						paymentterms = doheader.getPAYMENT_TERMS();
						parameters.put("trans", transportmode);
						parameters.put("payterms", paymentterms);
						//end
						
						parameters.put("fromAddress_CompanyName", CNAME);
						if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
						{
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
						} else {
							String fromAddress_BlockAddress="";
							if(CADD2.length()>0)
								fromAddress_BlockAddress=CADD2 + ", ";
								parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
								if(CADD1.equals("")) {
									parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
								}else {
									parameters.put("fromAddress_RoadAddress", " "+CADD1 + ",");
								}
								if(CSTATE.equals("")) {
									parameters.put("fromAddress_Country",  CCOUNTRY);
								}else {
									parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
								}
						}
						
						parameters.put("fromAddress_ZIPCode", CZIP);
						parameters.put("fromAddress_TpNo", CTEL);
						parameters.put("fromAddress_FaxNo", CFAX);
						parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
						parameters.put("fromAddress_ContactPersonMobile", CHPNO);
						parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
						parameters.put("fromAddress_RCBNO", CRCBNO);
						parameters.put("fromAddress_UENNO", companyregno);//imtiuen
						parameters.put("currentTime", _DOUtil.getOrderDateOnly(PLANT, DONO));
						parameters.put("taxInvoiceNo", "");
						parameters.put("InvoiceTerms", "");
							parameters.put("refNo", _DOUtil.getJobNum(PLANT, DONO));
						arraydohdr = _DOUtil.getDoHdrDetails(query, htCond);
						Hashtable curHash = new Hashtable();
						curHash.put(IDBConstants.PLANT, PLANT);
						curHash.put(IDBConstants.CURRENCYID, dataMap.get("currencyid"));
						String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
						OrderTypeDAO ODAO = new OrderTypeDAO();
						String orderDesc = "";

						DOUtil poUtil = new DOUtil();
						Map ma = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT, "OUTBOUND ORDER");
						Orientation = (String) ma.get("PrintOrientation");
						String orderType = doheader.getORDERTYPE();
						if(orderType.equals(""))
							orderType="OUTBOUND ORDER";
						if (orderType.equals("OUTBOUND ORDER")) {
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
						parameters.put("InvoiceNoHdr", (String) ma.get("INVOICENO"));
						parameters.put("InvoiceDate", (String) ma.get("INVOICEDATE"));
						parameters.put("RefNo", (String) ma.get("REFNO"));
						parameters.put("Terms", (String) ma.get("TERMS"));
						parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
							parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
						parameters.put("SoNo", (String) ma.get("SONO"));
						parameters.put("Item", (String) ma.get("ITEM"));
						parameters.put("Description", (String) ma.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
						parameters.put("UOM", (String) ma.get("UOM"));
						;
						parameters.put("Rate", (String) ma.get("RATE"));
						parameters.put("TaxAmount", (String) ma.get("TAXAMOUNT"));
						parameters.put("Amt", (String) ma.get("AMT"));
						parameters.put("SubTotal", (String) ma.get("SUBTOTAL") + " " + "(" + curDisplay + ")");
						String taxby = _PlantMstDAO.getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
						if (taxby.equalsIgnoreCase("BYORDER")) {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
						} else {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
						}
						parameters.put("Total", (String) ma.get("TOTAL") + " " + "(" + curDisplay + ")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", (String) ma.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
						
						//changes start : IMTHI
						//payment made displays from sales order number (dono)
						if (request.getParameter("printwithinvoiceno") == null ) {
						InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
						double orderadvanceamt = invoicePaymentDAO.getcreditamoutusingorderno(PLANT, DONO);
						double paidamountfororder = invoicePaymentDAO.getpaidamoutusingorderno(PLANT, DONO);	
						double pdcamount = 0.0;
						InvPaymentDetail InvPaymentDetail = new InvPaymentDetail();
						List<InvPaymentDetail> invdetlist = invoicePaymentDAO.getInvoicePaymentDetailsbydono(DONO, PLANT, dataMap.get("CRBY").toString());
						for(InvPaymentDetail invdet:invdetlist){
							Hashtable ht = new Hashtable();	
							ht.put("PAYMENTID",String.valueOf(invdet.getRECEIVEHDRID()));
							ht.put("PLANT",PLANT);
							List pdcDetailListpc = invoicePaymentDAO.getpdcbipayid(ht);
							for(int k =0; k < pdcDetailListpc.size(); k++) {
								Map pdcdet=(Map)pdcDetailListpc.get(k);
								String status = (String)pdcdet.get("STATUS");
								if(status.equalsIgnoreCase("NOT PROCESSED")) {
									pdcamount = pdcamount+(Double.parseDouble((String)pdcdet.get("CHEQUE_AMOUNT")) * Double.parseDouble((String)pdcdet.get("CURRENCYUSEQT")));
								}
							}
						}
						String invoiceAmount=StrUtils.addZeroes(((orderadvanceamt + paidamountfororder + pdcamount)), numberOfDecimal);
						parameters.put("PaymentMade", invoiceAmount);
						parameters.put("BalanceDue", invoiceAmount);
						}else {
						//payment made displays from invoice number if reports in print with invoice
						InvoicePaymentDAO InvPaymentdao = new InvoicePaymentDAO();
			        	double paymentmade=InvPaymentdao.getbalacedue(PLANT, ID);
			        	double paymentMadeloc =paymentmade;
			        	paymentmade=(paymentmade*Float.parseFloat(currencyuseqt));
			        	String Spaymentmade = Numbers.toMillionFormat(paymentmade, numberOfDecimal);
			        	parameters.put("PaymentMade",Spaymentmade);
			        	parameters.put("BalanceDue", Spaymentmade);
						}
						parameters.put("PRINTPAYMENTMADE", (String) ma.get("PRINTPAYMENTMADE"));
						parameters.put("PRINTADJUSTMENT", (String) ma.get("PRINTADJUSTMENT"));
						parameters.put("PRINTSHIPPINGCOST", (String) ma.get("PRINTSHIPPINGCOST"));
						parameters.put("PRINTORDERDISCOUNT", (String) ma.get("PRINTORDERDISCOUNT"));
						parameters.put("PRINTBALANCEDUE", (String) ma.get("PRINTBALANCEDUE"));
						parameters.put("BalancedueHdr", (String) ma.get("BALANCEDUE"));
						parameters.put("PaymentMadeHdr", (String) ma.get("PAYMENTMADE"));
						//changes end : imthi
						
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
						parameters.put("PRINTWITHINCOTERM", (String) ma.get("PRINTINCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
						parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
						String both =(String) ma.get("PRINTWITHUENNO");//by imthi to disply both uen and gst
						if(both.equalsIgnoreCase("2")) {
							parameters.put("RCBNO", ", "+(String) ma.get("RCBNO"));
						}else {
							parameters.put("RCBNO", (String) ma.get("RCBNO"));
						}
						parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
						parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
						parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
						parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
						parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
						parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));//azees
						
						int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
						if(PRINTWITHSHIPINGADD == 1) {
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", sCustCode);
							sShipCustName = (String) arrShippingDetails.get(1);
							String sShipContactName = (String) arrShippingDetails.get(2);
							String sshipTelno = (String) arrShippingDetails.get(3);
							String sShipPhone = (String) arrShippingDetails.get(4);
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
					} else {
						parameters.put("shipToId", "");
					}
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						parameters.put("trans", transportmode);
						
						if (ma.get("PRINTCUSTERMS").equals("1")) {
							parameters.put("payterms", pays);
							} else {
								parameters.put("payterms", paymentterms);
							}
//						parameters.put("payterms", paymentterms);
						parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
						parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
						parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
						//end
						
						parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
						parameters.put("INVOICEDATE", (String) ma.get("INVOICEDATE"));
						parameters.put("GINODATELabel", (String) ma.get("GINODATE"));
						if (request.getParameter("printwithinvoiceno") != null || request.getParameter("printwithigino") != null) {
							parameters.put("printwithigino", "no");
							parameters.put("printwithinvoiceno", "no");
							if (request.getParameter("printwithinvoiceno") != null)
							{
								parameters.put("printwithinvoiceno", "yes");
							//if (request.getParameter("printwithigino") != null)
								parameters.put("printwithigino", "yes");
							}
							parameters.put("GINOLabel", (String) ma.get("GINO"));
							parameters.put("GINO", GINO);
							parameters.put("INVOICENOLabel", (String) ma.get("INVOICENO"));
							parameters.put("INVOICENO", INVOICENO);
							if (INVOICENO.length() == 0)
							{
							parameters.put("ISSUEDATE", "");
							}else{
							parameters.put("ISSUEDATE", ISSUEDATE);
							}
							if (GINO.length() == 0)
							{
							parameters.put("GINODATE", "");
							}else{
							parameters.put("GINODATE", GINODATE);
							}
						}else {
							if (request.getParameter("printwithinvoiceno") == null)
							{
								parameters.put("printwithigino", "yes");
							}
							parameters.put("printwithinvoiceno", "no");
							parameters.put("ISSUEDATE", "");
							parameters.put("INVOICENO", "");
							parameters.put("GINOLabel", (String) ma.get("GINO"));
							parameters.put("GINO", GINO);
							parameters.put("GINODATE", GINODATE);
						}
						parameters.put("HSCODE", (String) ma.get("HSCODE"));
						parameters.put("COO", (String) ma.get("COO"));
						parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
						parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
						if (request.getParameter("printwithinvoiceno") != null) //new changes - azees 12/2020
							parameters.put("IssueDateRange", sConditioninv);
						else
							parameters.put("IssueDateRange", sCondition);
						if (ma.get("PCUSREMARKS").equals("1")) {
							parameters.put("SupRemarks", sRemarks);
						} else {
							parameters.put("SupRemarks", "");
						}
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
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
						String DISPLAYSIGNATURE= (String) ma.get("DISPLAYSIGNATURE");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(DISPLAYSIGNATURE.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
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
						String querylist;
						querylist = "item,itemdesc,unitprice";
						DoDetDAO dodetdao = new DoDetDAO();						
						ArrayList arraydodet = dodetdao.selectDoDet(querylist, htCond);
						if(arraydodet.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						
						Map DoDetails= new DOUtil().getDOReceiptHdrDetailsDO(PLANT,"Outbound order");
				        if(!DoDetails.isEmpty()){
				            PRINTPACKINGLIST = (String)DoDetails.get("PRINTPACKINGLIST");
				            PRINTDELIVERYNOTE = (String)DoDetails.get("PRINTDELIVERYNOTE");
				    	}
						//In Landscape, Check if delivery note is enabled -- Blocked by Azees 28.8.20 - Block Removed on 28.7.22
						if ("1".equals(PRINTDELIVERYNOTE)) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDNWITHBATCH";
							parameters.put("IssueDateRange", sCondition);
							parameters.put("OrderHeader", "Delivery Note");
							parameters.put("DeliveryNoteNumber", "Delivery Note Number");
							parameters.put("PartNo", "Part No");
							parameters.put("HSCode", "HS Code");
							parameters.put("COO", "COO");
							parameters.put("NetWeight", "Net Weight");
							parameters.put("GrossWeight", "Gross Weight");
							parameters.put("Dimension", "Dimension");
							parameters.put("Packing", "Packing");
							DNPLHdrDAO _DnPLHdrDAO = new DNPLHdrDAO();
							Hashtable htDnPlCondition = new Hashtable();
							htDnPlCondition.put("PLANT", PLANT);
							htDnPlCondition.put("DONO", DONO);
							htDnPlCondition.put("INVOICENO", GINO);
							ArrayList aldnpl = _DnPLHdrDAO.selectdnplDNPLHDR("*", htDnPlCondition);
							Iterator iterdnpl = aldnpl.iterator();
							while(iterdnpl.hasNext()) {
								Map mapdnpl = (Map)iterdnpl.next();
								parameters.put("DeliveryNoteNo", mapdnpl.get("DELIVERYNOTE"));
								parameters.put("TotalDimension", mapdnpl.get("TOTAlDIMENSION"));
								parameters.put("TotalPacking", mapdnpl.get("TOTAlPACKING"));
								parameters.put("TotalNetWeight", mapdnpl.get("TOTAlNETWEIGHT"));
								parameters.put("TotalGrossWeight", mapdnpl.get("TOTAlGROSSWEIGHT"));
								parameters.put("INVOICENO", mapdnpl.get("GINO"));
								parameters.put("InvoiceNumber", "GINO ");
								parameters.put("InvoiceDate", "GINODATE ");
								parameters.put("InvoiceDt", GINODATE);
								
								String sSTATUS="",STATUSDATE="",DateLable="";
								parameters.put("DNNOTE", " "+mapdnpl.get("NOTE"));
								parameters.put("DNNOTELABLE", "Notes :");
								String SHIPSTATUS =(String)mapdnpl.get("SHIPPING_STATUS");
								String SHIPDATE =(String)mapdnpl.get("SHIPPING_DATE");
								String INTRANSITSTATUS =(String)mapdnpl.get("INTRANSIT_STATUS");
								String INTRANSITDATE =(String)mapdnpl.get("INTRANSIT_DATE");
								String DELIVERYSTATUS =(String)mapdnpl.get("DELIVERY_STATUS");
								String DELIVERYDATE =(String)mapdnpl.get("DELIVERY_DATE");
								String TRANSPORT =(String)mapdnpl.get("TRANSPORTID");
								
								String CARRIER =(String)mapdnpl.get("CARRIER");
								String FREIGHT_FORWARDERID =(String)mapdnpl.get("FREIGHT_FORWARDERID");
								String DURATIONOFJOURNEY =(String)mapdnpl.get("DURATIONOFJOURNEY");
								String TRACKINGNO =(String)mapdnpl.get("TRACKINGNO");
								
								int trans = Integer.valueOf(TRANSPORT);

								if(trans > 0){
									transportmode = transportmodedao.getTransportModeById(PLANT, Integer.valueOf(TRANSPORT));
								}else{
									transportmode = "";
								}
								
								if(SHIPSTATUS==null){SHIPSTATUS="";}if(SHIPDATE==null){SHIPDATE="";}
								if(INTRANSITSTATUS==null){INTRANSITSTATUS="";}if(INTRANSITDATE==null){INTRANSITDATE="";}
								if(DELIVERYSTATUS==null){DELIVERYSTATUS="";}if(DELIVERYDATE==null){DELIVERYDATE="";}
								if(CARRIER==null){CARRIER="";}if(FREIGHT_FORWARDERID==null){FREIGHT_FORWARDERID="";}
								if(DURATIONOFJOURNEY==null){DURATIONOFJOURNEY="";}if(TRACKINGNO==null){TRACKINGNO="";}
								
									
							if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
								sSTATUS = SHIPSTATUS;
								STATUSDATE = SHIPDATE;
								DateLable = sSTATUS+" Date";
							}else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
								sSTATUS = DELIVERYSTATUS;
								STATUSDATE = DELIVERYDATE;
								DateLable = sSTATUS+" Date";
				           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = INTRANSITSTATUS;
				        	   STATUSDATE = INTRANSITDATE;
				        	   DateLable = sSTATUS+" Date";
				           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = INTRANSITSTATUS;
				        	   STATUSDATE = INTRANSITDATE;
				        	   DateLable = sSTATUS+" Date";
				           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = DELIVERYSTATUS;
				        	   STATUSDATE = DELIVERYDATE;
				        	   DateLable = sSTATUS+" Date";
				           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
				        	   sSTATUS = DELIVERYSTATUS;
				        	   STATUSDATE = DELIVERYDATE;
				        	   DateLable = sSTATUS+" Date";
				           }
									
								parameters.put("CARRIER", CARRIER);
								parameters.put("CARRIERLABLE", "Carrier");
								parameters.put("FREIGHT_FORWARDER", FREIGHT_FORWARDERID);
								parameters.put("FREIGHTLABLE", "Freight Forwarder");
								parameters.put("DURATIONOFJOURNEY", DURATIONOFJOURNEY);
								parameters.put("JOURNEYLABLE", "Duration of Journey");
								parameters.put("TRACKINGNO", TRACKINGNO);
								parameters.put("TRACKINGLABLE", "Tracking No");
								parameters.put("TRANSPORT", transportmode);
								parameters.put("TRANSPORTLABLE", "Transport");
								parameters.put("STATUS", sSTATUS);
								parameters.put("STATUSLABLE", "Status");
								parameters.put("STATUSDATE", STATUSDATE);
								parameters.put("STATUSDATELABLE", DateLable);
							
								if(PRINTDELIVERYNOTE.equals("1")){
									JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
									jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
								}
							}
						}
						//In Landscape, Check if packing list is enabled
						  if ("1".equals(PRINTPACKINGLIST)) {
							  jasperPath = DbBean.JASPER_INPUT + "/" + "rptPLWITHBATCH";
							  	parameters.put("IssueDateRange", sCondition);
								parameters.put("OrderHeader", "Packing List");
								parameters.put("PackingListNumber", "Packing List Number");
								parameters.put("PartNo", "Part No");
								parameters.put("HSCode", "HS Code");
								parameters.put("COO", "COO");
								parameters.put("NetWeight", "Net Weight");
								parameters.put("GrossWeight", "Gross Weight");
								parameters.put("Dimension", "Dimension");
								parameters.put("Packing", "Packing");
								DNPLHdrDAO _DnPLHdrDAO = new DNPLHdrDAO();
								Hashtable htDnPlCondition = new Hashtable();
								htDnPlCondition.put("PLANT", PLANT);
								htDnPlCondition.put("DONO", DONO);
								htDnPlCondition.put("INVOICENO", GINO);
								ArrayList aldnpl = _DnPLHdrDAO.selectdnplDNPLHDR("*", htDnPlCondition);
								Iterator iterdnpl = aldnpl.iterator();
								while(iterdnpl.hasNext()) {
									Map mapdnpl = (Map)iterdnpl.next();
									parameters.put("PackingListNo", mapdnpl.get("PACKINGLIST"));
									parameters.put("TotalDimension", mapdnpl.get("TOTAlDIMENSION"));
									parameters.put("TotalPacking", mapdnpl.get("TOTAlPACKING"));
									parameters.put("TotalNetWeight", mapdnpl.get("TOTAlNETWEIGHT"));
									parameters.put("TotalGrossWeight", mapdnpl.get("TOTAlGROSSWEIGHT"));
									parameters.put("INVOICENO", mapdnpl.get("GINO"));
									
									String sSTATUS="",STATUSDATE="",DateLable="";
									parameters.put("DNNOTE", " "+mapdnpl.get("NOTE"));
									parameters.put("DNNOTELABLE", "Notes :");
									String SHIPSTATUS =(String)mapdnpl.get("SHIPPING_STATUS");
									String SHIPDATE =(String)mapdnpl.get("SHIPPING_DATE");
									String INTRANSITSTATUS =(String)mapdnpl.get("INTRANSIT_STATUS");
									String INTRANSITDATE =(String)mapdnpl.get("INTRANSIT_DATE");
									String DELIVERYSTATUS =(String)mapdnpl.get("DELIVERY_STATUS");
									String DELIVERYDATE =(String)mapdnpl.get("DELIVERY_DATE");
									String TRANSPORT =(String)mapdnpl.get("TRANSPORTID");
									
									String CARRIER =(String)mapdnpl.get("CARRIER");
									String FREIGHT_FORWARDERID =(String)mapdnpl.get("FREIGHT_FORWARDERID");
									String DURATIONOFJOURNEY =(String)mapdnpl.get("DURATIONOFJOURNEY");
									String TRACKINGNO =(String)mapdnpl.get("TRACKINGNO");
									
									int trans = Integer.valueOf(TRANSPORT);

									if(trans > 0){
										transportmode = transportmodedao.getTransportModeById(PLANT, Integer.valueOf(TRANSPORT));
									}else{
										transportmode = "";
									}
									
									if(SHIPSTATUS==null){SHIPSTATUS="";}if(SHIPDATE==null){SHIPDATE="";}
									if(INTRANSITSTATUS==null){INTRANSITSTATUS="";}if(INTRANSITDATE==null){INTRANSITDATE="";}
									if(DELIVERYSTATUS==null){DELIVERYSTATUS="";}if(DELIVERYDATE==null){DELIVERYDATE="";}
									if(CARRIER==null){CARRIER="";}if(FREIGHT_FORWARDERID==null){FREIGHT_FORWARDERID="";}
									if(DURATIONOFJOURNEY==null){DURATIONOFJOURNEY="";}if(TRACKINGNO==null){TRACKINGNO="";}
									
										
								if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
									sSTATUS = SHIPSTATUS;
									STATUSDATE = SHIPDATE;
									DateLable = sSTATUS+" Date";
								}else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
									sSTATUS = DELIVERYSTATUS;
									STATUSDATE = DELIVERYDATE;
									DateLable = sSTATUS+" Date";
					           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = INTRANSITSTATUS;
					        	   STATUSDATE = INTRANSITDATE;
					        	   DateLable = sSTATUS+" Date";
					           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&DELIVERYSTATUS.equalsIgnoreCase("")&&DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = INTRANSITSTATUS;
					        	   STATUSDATE = INTRANSITDATE;
					        	   DateLable = sSTATUS+" Date";
					           }else if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = DELIVERYSTATUS;
					        	   STATUSDATE = DELIVERYDATE;
					        	   DateLable = sSTATUS+" Date";
					           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")&&!DELIVERYSTATUS.equalsIgnoreCase("")&&!DELIVERYDATE.equalsIgnoreCase("")){
					        	   sSTATUS = DELIVERYSTATUS;
					        	   STATUSDATE = DELIVERYDATE;
					        	   DateLable = sSTATUS+" Date";
					           }
										
									parameters.put("CARRIER", CARRIER);
									parameters.put("CARRIERLABLE", "Carrier");
									parameters.put("FREIGHT_FORWARDER", FREIGHT_FORWARDERID);
									parameters.put("FREIGHTLABLE", "Freight Forwarder");
									parameters.put("DURATIONOFJOURNEY", DURATIONOFJOURNEY);
									parameters.put("JOURNEYLABLE", "Duration of Journey");
									parameters.put("TRACKINGNO", TRACKINGNO);
									parameters.put("TRACKINGLABLE", "Tracking No");
									parameters.put("TRANSPORT", transportmode);
									parameters.put("TRANSPORTLABLE", "Transport");
									parameters.put("STATUS", sSTATUS);
									parameters.put("STATUSLABLE", "Status");
									parameters.put("STATUSDATE", STATUSDATE);
									parameters.put("STATUSDATELABLE", DateLable);
									parameters.put("InvoiceNumber", "GINO ");
									parameters.put("InvoiceDate", "GINODATE ");
									parameters.put("InvoiceDt", ISSUEDATE);
									if(PRINTPACKINGLIST.equals("1")){
										JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
										jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
									}
						  }
						  }

					}
				}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			exporter.exportReport();

			byte[] bytes = byteArrayOutputStream.toByteArray();
			if (!ajax) {
				response.addHeader("Content-disposition", "inline;filename=MultipleInvoice.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}else {
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + DONO + ".pdf")){
					fos.write(bytes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	
	}
	
	

	// Transfer..........................................................................

	public void viewTOReport(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws IOException, Exception {
		Connection con = null;
		try {
			TOUtil _TOUtil = new TOUtil();
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
//			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "";
			;

			String TONO = StrUtils.fString(request.getParameter("TONO"));
			String PLANT = (String) session.getAttribute("PLANT");
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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
			String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;

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

			}
			ArrayList arrCust = cUtil.getAssigneeDetailsForTO(TONO, PLANT);
			if (arrCust.size() > 0) {
//				String sCustCode = (String) arrCust.get(0);
				String sCustName = (String) arrCust.get(1);
				String sAddr1 = (String) arrCust.get(2);
				String sAddr2 = (String) arrCust.get(3);
				String sAddr3 = (String) arrCust.get(4);
				String sCountry = (String) arrCust.get(5);
				String sZip = (String) arrCust.get(6);
//				String sCons = (String) arrCust.get(7);
				String sContactName = (String) arrCust.get(8);
//				String sDesgination = (String) arrCust.get(9);
				String sTelNo = (String) arrCust.get(10);
//				String sHpNo = (String) arrCust.get(11);
				String sEmail = (String) arrCust.get(12);
				String sFax = (String) arrCust.get(13);
				String sRemarks = (String) arrCust.get(14);
				String sAddr4 = (String) arrCust.get(15);
//				String sIsactive = (String) arrCust.get(16);

				String sShipCustName = (String) arrCust.get(17);
				String sShipContactName = (String) arrCust.get(18);
				String sShipAddr1 = (String) arrCust.get(19);
				String sShipAddr2 = (String) arrCust.get(20);
				String sShipCity = (String) arrCust.get(21);
				String sShipCountry = (String) arrCust.get(22);
				String sShipZip = (String) arrCust.get(23);
				String sshipTelno = (String) arrCust.get(24);
				String orderRemarks = (String) arrCust.get(25);
				if (orderRemarks.length() > 0)
					orderRemarks = "Order Remarks : " + orderRemarks;

				Map parameters = new HashMap();
				parameters.put("imagePath", imagePath);
				parameters.put("imagePath1", imagePath1);

				// Customer Details
				parameters.put("OrderNo", TONO);
				parameters.put("orderRemarks", orderRemarks);
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_AttentionTo", sContactName);
				parameters.put("To_CCTO", "");
				parameters.put("To_TelNo", sTelNo);
				parameters.put("To_Email", sEmail);
				parameters.put("To_Fax", sFax);
				parameters.put("SupRemarks", sRemarks);
				// ship to Address

				parameters.put("STo_Addr1", sShipAddr1);
				parameters.put("STo_Addr2", sShipAddr2);
				parameters.put("STo_City", sShipCity);
				parameters.put("STo_Country", sShipCountry);
				parameters.put("STo_ZIP", sShipZip);
				parameters.put("STo_Telno", sshipTelno);
				if (sShipContactName.length() > 0)
					parameters.put("STo_Telno", "Tel : " + sshipTelno);
				else
					parameters.put("STo_Telno", sshipTelno);
				if (sShipCustName.length() > 0) {
					parameters.put("STo_AttentionTo", "Attn : " + sShipContactName);
				} else {
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
				parameters.put("currentTime", _TOUtil.getOrderDate(PLANT, TONO));
				parameters.put("refNo", new TOUtil().getJobNum(PLANT, TONO));
				parameters.put("InvoiceTerms", "");

				// report template parameter added on June 28 2011 By Deen
				TOUtil toUtil = new TOUtil();
				Map ma = toUtil.getReceiptHdrDetails(PLANT,"Pick and Issue");
				Orientation = (String) ma.get("PrintOrientation");
				if (sShipCustName.length() > 0) {
					parameters.put("STo_CustName", "SHIP TO : " + "\n" + "\n" + sShipCustName);

				} else {
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

				parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
				;
				parameters.put("UOM", (String) ma.get("UOM"));
				parameters.put("Footer1", (String) ma.get("F1"));
				parameters.put("Footer2", (String) ma.get("F2"));
				parameters.put("Footer3", (String) ma.get("F3"));
				parameters.put("Footer4", (String) ma.get("F4"));
				parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
				parameters.put("STo_CustName", sShipCustName);
				parameters.put("STo", "SHIPTO");

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
				if (Orientation.equals("Portrait")) {

					jasperPath = DbBean.JASPER_INPUT + "/" + fileName + "Portrait";

				}

				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : " + start + "**********");
				System.out.println("jasperPath : " + jasperPath);
				JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
				byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);

				//response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
				response.addHeader("Content-disposition", "inline;filename=reporte.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	
	private void viewTOReportNew(HttpServletRequest request, HttpServletResponse response, String action)
			throws IOException, Exception {
		Connection con = null;
		try {

			TOUtil _TOUtil = new TOUtil();
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
//			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "",CWEBSITE = "";

			String TONO = "";
			List jasperPrintList = new ArrayList();
			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
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
			for (int i = 0; i < chkdDoNo.length; i++) {

				String query = " ";
				TONO = chkdDoNo[i];
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("TONO", TONO);
				
//				ArrayList alShipDetail = new ArrayList();
				
				
				for(int j = 0; j < 1 ; j ++) {
					
				
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptTOWITHBATCH";
				
				
				
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
					
					Map ma = _TOUtil.getReceiptHdrDetails(PLANT,"Pick and Issue");
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
					if (Orientation.equals("Portrait")) {
						jasperPath += "Portrait";
					}
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

	
	private void viewToReportWithPrice(HttpServletRequest request, HttpServletResponse response, String action)
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
			
//			Map m = null;
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "",SEALNAME="",SIGNNAME="",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "",CWEBSITE = "";
			String TONO = "";
			String[] chkdDoNo = request.getParameterValues("chkdDoNo");
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

//			//String SysDate = DateUtils.getDate();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "printTOWITHBATCHWITHPRICE";
//			Date sysTime1 = new Date(System.currentTimeMillis());
			//Date cDt = new Date(System.currentTimeMillis());
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
			for (int i = 0; i < chkdDoNo.length; i++) {
			
				String query = " ";
				TONO = chkdDoNo[i];
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("TONO", TONO);
//				ArrayList alShipDetail = new ArrayList();
				
				for(int j = 0; j < 1; j ++) {
					String DATAORDERDISCOUNT="0",DATASHIPPINGCOST="0",
							ORDERDISCOUNTTYPE = "",AdjustmentAmount="",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",
							ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",ISTAXINCLUSIVE="",PROJECT_NAME="",sales_location="",empno="";
							int TAXID=0;
//					String INVOICENO = "",ISSUEDATE="",GINO="",GINODATE="",
					
					
					
					
//					DOUtil poUtil1 = new DOUtil();
//					Map ma1 = _TOUtil.getTOReceiptHdrDetails(PLANT);
					
//					String printwithbatch="0";					
					
					signaturePath = DbBean.COMPANY_SIGN_PATH + "/" + PLANT.toLowerCase() + "/" + "sign" + TONO + ".bmp";
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
						
//						parameters.put("ISTAXINCLUSIVE", ISTAXINCLUSIVE);
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
//						parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//						parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//						if(CSTATE.equals("")) {
//						parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//						}else {
//							parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//						}
						
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

//						DOUtil poUtil = new DOUtil();
						Map ma = _TOUtil.getTOReceiptHdrDetails(PLANT);
//						Orientation = (String) ma.get("PrintOrientation");
//						PRINTDELIVERYNOTE = (String)ma.get("PRINTDELIVERYNOTE");
//						PRINTPACKINGLIST = (String)ma.get("PRINTPACKINGLIST"); 
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
						String taxby = _PlantMstDAO.getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
						if (taxby.equalsIgnoreCase("BYORDER")) {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
						} else {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
						}
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

	public void viewLoanOrderReport(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
//			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "",RCBNO="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "";
//			String SHPFAX = "", SHPEMAIL = "", ;
			
			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String PLANT = (String) session.getAttribute("PLANT");
			String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER"));
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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
			String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;

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
				RCBNO = (String) map.get("RCBNO");
				
			}
			ArrayList arrCust = cUtil.getAssigneeDetailsForLoan(ORDERNO, PLANT);
			if (arrCust.size() > 0) {
//				String sCustCode = (String) arrCust.get(0);
				String sCustName = (String) arrCust.get(1);
				String sAddr1 = (String) arrCust.get(2);
				String sAddr2 = (String) arrCust.get(3);
				String sAddr3 = (String) arrCust.get(4);
				String sCountry = (String) arrCust.get(5);
				String sZip = (String) arrCust.get(6);
//				String sCons = (String) arrCust.get(7);
				String sContactName = (String) arrCust.get(8);
//				String sDesgination = (String) arrCust.get(9);
				String sTelNo = (String) arrCust.get(10);
//				String sHpNo = (String) arrCust.get(11);
				String sEmail = (String) arrCust.get(12);
				String sFax = (String) arrCust.get(13);
				String sRemarks = (String) arrCust.get(14);
				String sAddr4 = (String) arrCust.get(15);
//				String sIsactive = (String) arrCust.get(16);
				String orderRemarks = (String) arrCust.get(17);
				String ORDTYPE = (String) arrCust.get(18);
				String CRBY = (String) arrCust.get(19);
				
				ArrayList arrShippingDetails = _masterUtil.getRentalShippingDetails(ORDERNO, SHIPPINGID, PLANT);
				Map parameters = new HashMap();
				if (arrShippingDetails.size() > 0) {
				//	parameters.put("shipToId", SHIPPINGID);
					SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
					SHPCONTACTNAME = (String) arrShippingDetails.get(2);
					SHPTELEPHONE = (String) arrShippingDetails.get(3);
					SHPHANDPHONE = (String) arrShippingDetails.get(4);
//					SHPFAX = (String) arrShippingDetails.get(5);
//					SHPEMAIL = (String) arrShippingDetails.get(6);
					SHPUNITNO = (String) arrShippingDetails.get(7);
					SHPBUILDING = (String) arrShippingDetails.get(8);
					SHPSTREET = (String) arrShippingDetails.get(9);
					SHPCITY = (String) arrShippingDetails.get(10);
					SHPSTATE = (String) arrShippingDetails.get(11);
					SHPCOUNTRY = (String) arrShippingDetails.get(12);
					SHPPOSTALCODE = (String) arrShippingDetails.get(13);
				}else {
					parameters.put("shipToId", "");
				}

				//Map parameters = new HashMap();
				parameters.put("imagePath", imagePath);
				parameters.put("imagePath1", imagePath1);

				// Customer Details
				parameters.put("OrderNo", ORDERNO);
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_AttentionTo", sContactName);
				parameters.put("To_CCTO", "");
				parameters.put("To_TelNo", sTelNo);
				parameters.put("To_Email", sEmail);
				parameters.put("To_Fax", sFax);
				parameters.put("SupRemarks", sRemarks);

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
				parameters.put("fromAddress_RCBNO", RCBNO);
				parameters.put("currentTime", DateUtils.getDate());
				parameters.put("refNo", new LoanUtil().getJobNum(PLANT, ORDERNO));
				parameters.put("InvoiceTerms", "");
				parameters.put("prepareduser", CRBY);
				
				LoanUtil loUtil = new LoanUtil();
				Map ma = loUtil.getLOReceiptHdrDetails(PLANT);
				Orientation = (String) ma.get("PrintOrientation");
				if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
					parameters.put("OrderHeader", ORDTYPE);
				else
				parameters.put("OrderHeader", (String) ma.get("HDR1"));
				parameters.put("ToHeader", (String) ma.get("HDR2"));
				parameters.put("FromHeader", (String) ma.get("HDR3"));
				parameters.put("Date", (String) ma.get("DATE"));
				parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
				parameters.put("RefNo", (String) ma.get("REFNO"));
				parameters.put("SoNo", (String) ma.get("SONO"));
				parameters.put("Item", (String) ma.get("ITEM"));
				parameters.put("Description", (String) ma.get("DESCRIPTION"));
				
				parameters.put("DELDATE", (String) ma.get("DELDATE"));
				parameters.put("EMPNAME", (String) ma.get("EMPNAME"));
				parameters.put("ORDDISCOUNT", (String) ma.get("ORDDISCOUNT"));
				parameters.put("PREPAREDBY", (String) ma.get("PREPAREDBY"));
				parameters.put("SELLER", (String) ma.get("SELLER"));
				parameters.put("SELLERAUTHORIZED", (String) ma.get("SELLERAUTHORIZED"));
				parameters.put("BUYER", (String) ma.get("BUYER"));
				parameters.put("BUYERAUTHORIZED", (String) ma.get("BUYERAUTHORIZED"));
				parameters.put("COMPANYNAME", (String) ma.get("COMPANYNAME"));
				parameters.put("COMPANYSTAMP", (String) ma.get("COMPANYSTAMP"));
				parameters.put("SIGNATURE", (String) ma.get("SIGNATURE"));
				parameters.put("VAT", (String) ma.get("VAT"));
				parameters.put("CUSTOMERVAT", (String) ma.get("CUSTOMERVAT"));
				parameters.put("ORDDATE", (String) ma.get("ORDDATE"));
				parameters.put("SHIPTO", (String) ma.get("SHIPTO"));
				parameters.put("PAYMENTTYPE", (String) ma.get("PAYMENTTYPE"));
				
				parameters.put("PRINTDETAILDESC", (String) ma.get("PRINTDETAILDESC"));
				parameters.put("PRINTPRDREMARKS", (String) ma.get("PRINTPRDREMARKS"));
				parameters.put("PRINTWITHPRDID", (String) ma.get("PRINTWITHPRDID"));
				parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
				parameters.put("PRINTWITHEMPLOYEE", (String) ma.get("PRINTWITHEMPLOYEE"));
				parameters.put("PREPAREDBY", (String) ma.get("PREPAREDBY"));
				
				parameters.put("SHPCUSTOMERNAME", SHPCUSTOMERNAME);
				if (SHPCONTACTNAME.length() > 0) {
					parameters.put("SHPCONTACTNAME", "Attn : " + SHPCONTACTNAME);
				} else {
					parameters.put("SHPCONTACTNAME", SHPCONTACTNAME);
				}
				if (SHPTELEPHONE.length() > 0) {
					parameters.put("SHPTELEPHONE", "Tel : " + SHPTELEPHONE);
				} else {
					parameters.put("SHPTELEPHONE", SHPTELEPHONE);
				}
				if (SHPHANDPHONE.length() > 0) {
					parameters.put("SHPHANDPHONE", "HP : " + SHPHANDPHONE);
				} else {
					parameters.put("SHPHANDPHONE", SHPHANDPHONE);
				}
				parameters.put("SHPUNITNO", SHPUNITNO);
				parameters.put("SHPBUILDING", SHPBUILDING);
				parameters.put("SHPSTREET", SHPSTREET);
				parameters.put("SHPCITY", SHPCITY);
				parameters.put("SHPSTATE", SHPSTATE);
				parameters.put("SHPCOUNTRY", SHPCOUNTRY);
				parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);

				parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
				parameters.put("UOM", (String) ma.get("UOM"));
				if (orderRemarks.length() > 0)
					orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
				parameters.put("OrderRemarks", orderRemarks);
				parameters.put("Footer1", (String) ma.get("F1"));
				parameters.put("Footer2", (String) ma.get("F2"));
				parameters.put("Footer3", (String) ma.get("F3"));
				parameters.put("Footer4", (String) ma.get("F4"));
				parameters.put("Footer5", (String) ma.get("F5"));
				parameters.put("Footer6", (String) ma.get("F6"));
				parameters.put("Footer7", (String) ma.get("F7"));
				parameters.put("Footer8", (String) ma.get("F8"));
				parameters.put("Footer9", (String) ma.get("F9"));
				parameters.put("expireDate", new LoanUtil().getExpireDate(PLANT, ORDERNO));

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
				if (Orientation.equals("Portrait")) {

					jasperPath = DbBean.JASPER_INPUT + "/" + fileName + "Portrait";

				}

				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : " + start + "**********");
				System.out.println("jasperPath : " + jasperPath);
				JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");

				byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);

				//response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
				response.addHeader("Content-disposition", "inline;filename=reporte.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	public void viewLoanOrderInvoiceReport(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
//			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "",RCBNO="";
			
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "",
					SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "";
//			String SHPEMAIL = "", 
			;
//			String SHPFAX = "";

			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER"));
			String PLANT = (String) session.getAttribute("PLANT");
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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
			String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;

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
				RCBNO = (String) map.get("RCBNO");
				
			}
			ArrayList arrCust = cUtil.getAssigneeDetailsForLoan(ORDERNO, PLANT);
			if (arrCust.size() > 0) {
//				String sCustCode = (String) arrCust.get(0);
				String sCustName = (String) arrCust.get(1);
				String sAddr1 = (String) arrCust.get(2);
				String sAddr2 = (String) arrCust.get(3);
				String sAddr3 = (String) arrCust.get(4);
				String sCountry = (String) arrCust.get(5);
				String sZip = (String) arrCust.get(6);
//				String sCons = (String) arrCust.get(7);
				String sContactName = (String) arrCust.get(8);
//				String sDesgination = (String) arrCust.get(9);
				String sTelNo = (String) arrCust.get(10);
//				String sHpNo = (String) arrCust.get(11);
				String sEmail = (String) arrCust.get(12);
				String sFax = (String) arrCust.get(13);
				String sRemarks = (String) arrCust.get(14);
				String sAddr4 = (String) arrCust.get(15);
//				String sIsactive = (String) arrCust.get(16);
				String orderRemarks = (String) arrCust.get(17);
				String ORDTYPE = (String) arrCust.get(18);
				String CRBY = (String) arrCust.get(19);
				String Payment_type = (String) arrCust.get(20);
				 
				ArrayList arrShippingDetails = _masterUtil.getRentalShippingDetails(ORDERNO, SHIPPINGID, PLANT);
				Map parameters = new HashMap();
				if (arrShippingDetails.size() > 0) {
				//	parameters.put("shipToId", SHIPPINGID);
					SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
					SHPCONTACTNAME = (String) arrShippingDetails.get(2);
					SHPTELEPHONE = (String) arrShippingDetails.get(3);
					SHPHANDPHONE = (String) arrShippingDetails.get(4);
//					SHPFAX = (String) arrShippingDetails.get(5);
//					SHPEMAIL = (String) arrShippingDetails.get(6);
					SHPUNITNO = (String) arrShippingDetails.get(7);
					SHPBUILDING = (String) arrShippingDetails.get(8);
					SHPSTREET = (String) arrShippingDetails.get(9);
					SHPCITY = (String) arrShippingDetails.get(10);
					SHPSTATE = (String) arrShippingDetails.get(11);
					SHPCOUNTRY = (String) arrShippingDetails.get(12);
					SHPPOSTALCODE = (String) arrShippingDetails.get(13);
				}else {
					parameters.put("shipToId", "");
				}

			//	Map parameters = new HashMap();
				parameters.put("imagePath", imagePath);
				parameters.put("imagePath1", imagePath1);

				// Customer Details
				parameters.put("OrderNo", ORDERNO);
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_AttentionTo", sContactName);
				parameters.put("To_CCTO", "");
				parameters.put("To_TelNo", sTelNo);
				parameters.put("To_Email", sEmail);
				parameters.put("To_Fax", sFax);
				parameters.put("SupRemarks", sRemarks);
				parameters.put("TermsDetails", Payment_type);

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
				parameters.put("currentTime", DateUtils.getDate());
				parameters.put("refNo", new LoanUtil().getJobNum(PLANT, ORDERNO));
				parameters.put("InvoiceTerms", "");
				parameters.put("fromAddress_RCBNO", RCBNO);
				parameters.put("prepareduser", CRBY);
				
				LoanUtil loUtil = new LoanUtil();
				Map ma = loUtil.getLOReceiptHdrDetails(PLANT);
				Orientation = (String) ma.get("PrintOrientation");
				if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
					parameters.put("OrderHeader", ORDTYPE);
				else
				parameters.put("OrderHeader", (String) ma.get("HDR1"));
				parameters.put("ToHeader", (String) ma.get("HDR2"));
				parameters.put("FromHeader", (String) ma.get("HDR3"));
				parameters.put("Date", (String) ma.get("DATE"));
				parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
				parameters.put("RefNo", (String) ma.get("REFNO"));
				parameters.put("SoNo", (String) ma.get("SONO"));
				parameters.put("Item", (String) ma.get("ITEM"));
				parameters.put("Description", (String) ma.get("DESCRIPTION"));
				
				parameters.put("DELDATE", (String) ma.get("DELDATE"));
				parameters.put("EMPNAME", (String) ma.get("EMPNAME"));
				parameters.put("ORDDISCOUNT", (String) ma.get("ORDDISCOUNT"));
				parameters.put("PREPAREDBY", (String) ma.get("PREPAREDBY"));
				parameters.put("SELLER", (String) ma.get("SELLER"));
				parameters.put("SELLERAUTHORIZED", (String) ma.get("SELLERAUTHORIZED"));
				parameters.put("BUYER", (String) ma.get("BUYER"));
				parameters.put("BUYERAUTHORIZED", (String) ma.get("BUYERAUTHORIZED"));
				parameters.put("COMPANYNAME", (String) ma.get("COMPANYNAME"));
				parameters.put("COMPANYSTAMP", (String) ma.get("COMPANYSTAMP"));
				parameters.put("SIGNATURE", (String) ma.get("SIGNATURE"));
				parameters.put("SHIPPINGCOST", (String) ma.get("SHIPPINGCOST"));
				parameters.put("VAT", (String) ma.get("VAT"));
				parameters.put("CUSTOMERVAT", (String) ma.get("CUSTOMERVAT"));
				parameters.put("ORDDATE", (String) ma.get("ORDDATE"));
				parameters.put("SHIPTO", (String) ma.get("SHIPTO"));
				parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
				parameters.put("NETAMT", (String) ma.get("NETAMT"));
				parameters.put("TAXAMT", (String) ma.get("TAXAMT"));
				parameters.put("TOTALAMT", (String) ma.get("TOTALAMT"));
				parameters.put("RATE", (String) ma.get("RATE"));
				parameters.put("PAYMENTTYPE", (String) ma.get("PAYMENTTYPE"));
				parameters.put("AMOUNT", (String) ma.get("AMOUNT"));
				parameters.put("TOTALTAX", (String) ma.get("TOTALTAX"));
				parameters.put("TOTALWITHTAX", (String) ma.get("TOTALWITHTAX"));
				parameters.put("SHPCUSTOMERNAME", SHPCUSTOMERNAME);
				
				parameters.put("PRINTDETAILDESC", (String) ma.get("PRINTDETAILDESC"));
				parameters.put("PRINTPRDREMARKS", (String) ma.get("PRINTPRDREMARKS"));
				parameters.put("PRINTWITHPRDID", (String) ma.get("PRINTWITHPRDID"));
				parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
				parameters.put("PRINTWITHEMPLOYEE", (String) ma.get("PRINTWITHEMPLOYEE"));
				parameters.put("ROUNDOFF", (String) ma.get("ROUNDOFF"));
				parameters.put("TOTALROUNDOFF", (String) ma.get("TOTALROUNDOFF"));
				
				if (SHPCONTACTNAME.length() > 0) {
					parameters.put("SHPCONTACTNAME", "Attn : " + SHPCONTACTNAME);
				} else {
					parameters.put("SHPCONTACTNAME", SHPCONTACTNAME);
				}
				if (SHPTELEPHONE.length() > 0) {
					parameters.put("SHPTELEPHONE", "Tel : " + SHPTELEPHONE);
				} else {
					parameters.put("SHPTELEPHONE", SHPTELEPHONE);
				}
				if (SHPHANDPHONE.length() > 0) {
					parameters.put("SHPHANDPHONE", "HP : " + SHPHANDPHONE);
				} else {
					parameters.put("SHPHANDPHONE", SHPHANDPHONE);
				}
				parameters.put("SHPUNITNO", SHPUNITNO);
				parameters.put("SHPBUILDING", SHPBUILDING);
				parameters.put("SHPSTREET", SHPSTREET);
				parameters.put("SHPCITY", SHPCITY);
				parameters.put("SHPSTATE", SHPSTATE);
				parameters.put("SHPCOUNTRY", SHPCOUNTRY);
				parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);

				parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
				parameters.put("UOM", (String) ma.get("UOM"));
				if (orderRemarks.length() > 0)
					orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
				parameters.put("OrderRemarks", orderRemarks);
				parameters.put("Footer1", (String) ma.get("F1"));
				parameters.put("Footer2", (String) ma.get("F2"));
				parameters.put("Footer3", (String) ma.get("F3"));
				parameters.put("Footer4", (String) ma.get("F4"));
				parameters.put("Footer5", (String) ma.get("F5"));
				parameters.put("Footer6", (String) ma.get("F6"));
				parameters.put("Footer7", (String) ma.get("F7"));
				parameters.put("Footer8", (String) ma.get("F8"));
				parameters.put("Footer9", (String) ma.get("F9"));
				parameters.put("expireDate", new LoanUtil().getExpireDate(PLANT, ORDERNO));

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
				if (Orientation.equals("Portrait")) {

					jasperPath = DbBean.JASPER_INPUT + "/" + fileName + "Portrait";

				}

				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : " + start + "**********");
				System.out.println("jasperPath : " + jasperPath);
				JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");

				byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);

				//response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
				response.addHeader("Content-disposition", "inline;filename=reporte.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}

	public void viewRegister(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
//			PlantMstUtil pmUtil = new PlantMstUtil();
//			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			// String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "",
			// CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL =
			// "";
			;

//			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String PLANT = (String) session.getAttribute("PLANT");
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
//			String imagePath2, Orientation = "";

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
			String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;

			String CUST_CODE = StrUtils.fString(request.getParameter("CUST_CODE"));
			//java.util.Date cDt = new java.util.Date(System.currentTimeMillis());

			ArrayList arrCust = cUtil.getCustomerDetailsForRegister(CUST_CODE, PLANT);
			if (arrCust.size() > 0) {

				String sCustCode = (String) arrCust.get(0);
				String sCustName = (String) arrCust.get(1);
				// String sAddr1 = (String) arrCust.get(2);
				// String sAddr2 = (String) arrCust.get(3);
				// String sAddr3 = (String) arrCust.get(4);
				// String sCountry = (String) arrCust.get(5);
				// String sZip = (String) arrCust.get(6);
				// String sCons = (String) arrCust.get(7);
				// String sCustNameL = (String) arrCust.get(8);
				String sContactName = (String) arrCust.get(9);
				// String sDesgination = (String) arrCust.get(10);
				// String sTelNo = (String) arrCust.get(11);
				// String sHpNo = (String) arrCust.get(12);
				// String sFax = (String) arrCust.get(13);
				//// String sEmail = (String) arrCust.get(14);
//				String sRemarks = (String) arrCust.get(15);
				// String sAddr4 = (String) arrCust.get(16);

				Map parameter = new HashMap();
				parameter.put("company", PLANT);
				parameter.put("CUSTOMERID", sCustCode);
				parameter.put("CUSTOMERNAME", sCustName);
				parameter.put("COMPANYNAME", sContactName);
				// Customer Details

				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : " + start + "**********");

				byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameter, con);

				response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}

	}

	public void viewLogoPreview(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws IOException, Exception {
		Connection con = null;
		try {

//			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
//			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "";

			String PLANT = (String) session.getAttribute("PLANT");
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
//			String imagePath2, Orientation = "";

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
			String jasperPath = DbBean.JASPER_INPUT + "/" + fileName;

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

			}

			Map parameters = new HashMap();
			parameters.put("imagePath", imagePath);
			parameters.put("imagePath1", imagePath1);
			// Customer Details

			parameters.put("company", PLANT);

			// Company Details
			parameters.put("OrderNo", "ORD12345");
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
			parameters.put("currentTime", DateUtils.getDate());

			long start = System.currentTimeMillis();
			System.out.println("**************" + " Start Up Time : " + start + "**********");

			byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);

			response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
