package com.track.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringUtils;

import com.oreilly.servlet.MultipartRequest;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EmployeeLeaveDetDAO;
import com.track.dao.HrEmpSalaryDAO;
import com.track.dao.HrEmpSalaryDetDAO;
import com.track.dao.HrHolidayMstDAO;
import com.track.dao.HrLeaveApplyHdrDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.BlockingDates;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.HrEmpSalaryDET;
import com.track.db.object.HrEmpSalaryMst;
import com.track.db.object.HrHolidayMst;
import com.track.db.object.HrLeaveApplyAttachment;
import com.track.db.object.HrLeaveApplyDet;
import com.track.db.object.HrLeaveApplyHdr;
import com.track.db.object.HrLeaveType;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmployeeUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.service.HrHolidayMstService;
import com.track.service.HrLeaveApplyDetService;
import com.track.service.HrLeaveApplyHdrService;
import com.track.service.JournalService;
import com.track.serviceImplementation.HrHolidayMstServiceImpl;
import com.track.serviceImplementation.HrLeaveApplyDetServiceImpl;
import com.track.serviceImplementation.HrLeaveApplyHdrServiceImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.tran.WmsTran;
import com.track.tran.WmsUploadCountSheet;
import com.track.tran.WmsUploadCustomerDiscountSheet;
import com.track.tran.WmsUploadCustomerSheet;
import com.track.tran.WmsUploadEmployeeSheet;
import com.track.tran.WmsUploadKittingSheet;
import com.track.tran.WmsUploadLoanAssigneeSheet;
import com.track.tran.WmsUploadStockTakeSheet;
import com.track.tran.WmsUploadSupplerSheet;
import com.track.tran.WmsUploadSupplierDiscountSheet;
import com.track.tran.WmsUploadTransferAssigneeSheet;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ImportServlet
 */
public class ImportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

	String action = "";
	String PLANT = "";
	String login_user = "";
	String sys_date = "";
	String StrFileName = "";
	String orgFilePath = "";
	String StrSheetName = "";
	String result = "";
	String divertTo = "";
        String truncateval = "";

	public ImportServlet() {
		super();

	}

	private MLogger mlogger = new MLogger();

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mlogger.setLoggerConstans(dataForLogging);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    
    /* ************Modification History*********************************
		Nov 13 2014,Bruhan,Description: To include Download TransferAssignee,LoanAssigneeTemplate and Employee master
	*/
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		action = StrUtils.fString(request.getParameter("action")).trim();

		StrFileName = StrUtils.fString(request.getParameter("ImportFile"));
		StrSheetName = StrUtils.fString(request.getParameter("SheetName"));
	        truncateval = StrUtils.fString(request.getParameter("Truncate"));
		orgFilePath = StrFileName;
		
		System.out.println("Import File  *********:" + orgFilePath);
		System.out.println("Import File  *********:" + StrFileName);
		System.out.println("Import sheet *********:" + StrSheetName);

		PLANT = (String) request.getSession().getAttribute("PLANT");
		login_user = (String) request.getSession().getAttribute("LOGIN_USER");
		sys_date = DateUtils.getDate();
		try {
			if (action.equalsIgnoreCase("importLocCountSheet")) {
				onImportCountSheet(request, response,"LOCATION");
			}else if (action.equalsIgnoreCase("importSupplierCountSheet")) {
				onImportCountSheet(request, response,"SUPPLIER");
			} else if (action.equalsIgnoreCase("importCustomerCountSheet")) {
				onImportCountSheet(request, response,"CUSTOMER");
			} else if (action.equalsIgnoreCase("importTransferAssigneeCountSheet")) {
				onImportCountSheet(request, response,"TRANSFERASSIGNEE");
			} else if (action.equalsIgnoreCase("importLoanAssigneeCountSheet")) {
				onImportCountSheet(request, response,"LOANASSIGNEE");
			} else if (action.equalsIgnoreCase("importEmployeeCountSheet")) {
				onImportCountSheet(request, response,"EMPLOYEE");
			} else if (action.equalsIgnoreCase("importEmployeeLeaveApplyCountSheet")) {
				onImportCountSheet(request, response,"EMPLOYEELEAVEAPPLY");
			} else if (action.equalsIgnoreCase("importEmployeeLeaveDetCountSheet")) {
				onImportCountSheet(request, response,"EMPLOYEELEAVEDET");
			} else if (action.equalsIgnoreCase("importEmployeeSalaryDetCountSheet")) {
				onImportCountSheet(request, response,"EMPLOYEESALARYDET");	
			}else if (action.equalsIgnoreCase("importHolidayMstCountSheet")) {
				onImportCountSheet(request, response,"HOLIDAYMST");
			}else if (action.equalsIgnoreCase("importSalaryMstCountSheet")) {
				onImportCountSheet(request, response,"SALARYMST");
			}else if (action.equalsIgnoreCase("importKittingCountSheet")) {
				onImportCountSheet(request, response,"KITTING");
			} else if (action.equalsIgnoreCase("importSupplierDiscountCountSheet")) {
				  onImportCountSheet(request, response,"SUPPLIERDISCOUNT");
			} else if (action.equalsIgnoreCase("importCustomerDiscountCountSheet")) {
				  onImportCountSheet(request, response,"CUSTOMERDISCOUNT");
			}else if (action.equalsIgnoreCase("importStockTakeCountSheet")) {
				onImportCountSheet(request, response,"STOCKTAKE");
				
				
			}else if (action.equalsIgnoreCase("confirmLocCountSheet")) {
				onConfirmLocCountSheet(request, response);
			}else if (action.equalsIgnoreCase("confirmSupplierCountSheet")) {
                  onConfirmSupplierCountSheet(request, response);
            }else if (action.equalsIgnoreCase("confirmCustomerCountSheet")) {
                   onConfirmCustomerCountSheet(request, response);
 			}else if (action.equalsIgnoreCase("confirmTransferAssigneeCountSheet")) {
                onConfirmTransferAssigneeCountSheet(request, response);
            }else if (action.equalsIgnoreCase("confirmLoanAssigneeCountSheet")) {
                onConfirmLoanAssigneeCountSheet(request, response);
            }else if (action.equalsIgnoreCase("confirmEmployeeCountSheet")) {
                onConfirmEmployeeCountSheet(request, response);
            }else if (action.equalsIgnoreCase("confirmEmployeeLeaveDetCountSheet")) {
                onConfirmEmployeeLeaveDetCountSheet(request, response);
            }else if (action.equalsIgnoreCase("confirmEmployeeSalaryDetCountSheet")) {
            	onConfirmEmployeeSalaryDetCountSheet(request, response);    
            }else if (action.equalsIgnoreCase("confirmHolidayMstCountSheet")) {
                onConfirmHolidayMstCountSheet(request, response);
            }else if (action.equalsIgnoreCase("confirmSalaryMstCountSheet")) {
                onConfirmSalaryMstCountSheet(request, response);    
            }else if (action.equalsIgnoreCase("confirmKittingCountSheet")) {
				onConfirmKittingCountSheet(request, response); 
            }else if (action.equalsIgnoreCase("confirmStockTakeCountSheet")) {
				onConfirmStockTakeCountSheet(request, response);
		    }else if (action.equalsIgnoreCase("confirmCustomerDiscountCountSheet")) {
			  	 onConfirmCustomerDiscountCountSheet(request, response); 
	        } else if (action.equalsIgnoreCase("confirmSupplierDiscountCountSheet")) {
		  	     onConfirmSupplierDiscountCountSheet(request, response); 
            }else if (action.equalsIgnoreCase("confirmEmployeeLeaveApplyCountSheet")) {
                onConfirmEmployeeLeaveApplyCountSheet(request, response);
            }
            
			
            else if (action.equalsIgnoreCase("downloadLocTemplate")) {
				onDownloadTemplate(request, response,"LOCATION");
			}else if (action.equalsIgnoreCase("downloadSupplierTemplate")) {
				onDownloadTemplate(request, response,"SUPPLIER");
			} else if (action.equalsIgnoreCase("downloadCustomerTemplate")) {
                 onDownloadTemplate(request, response,"CUSTOMER");
            } else if (action.equalsIgnoreCase("downloadTransferAssigneeTemplate")) {
                onDownloadTemplate(request, response,"TRANSFERASSIGNEE");
            } else if (action.equalsIgnoreCase("downloadLoanAssigneeTemplate")) {
                onDownloadTemplate(request, response,"LOANASSIGNEE");
            } else if (action.equalsIgnoreCase("downloadEmployeeTemplate")) {
                onDownloadTemplate(request, response,"EMPLOYEE");
            } else if (action.equalsIgnoreCase("downloadEmployeeTemplateWithoutPayroll")) {
                onDownloadTemplate(request, response,"EMPLOYEEWITHOUTPAYROLL");
            } else if (action.equalsIgnoreCase("downloadEmployeeLeaveDetTemplate")) {
                onDownloadTemplate(request, response,"EMPLOYEELEAVEDET");
            } else if (action.equalsIgnoreCase("downloadEmployeeLeaveApplyTemplate")) {
                onDownloadTemplate(request, response,"EMPLOYEELEAVEAPPLY");
            } else if (action.equalsIgnoreCase("downloadEmployeeSalaryDetTemplate")) {
                onDownloadTemplate(request, response,"EMPLOYEESALARYDET");
            }else if (action.equalsIgnoreCase("downloadHolidayMstTemplate")) {
                onDownloadTemplate(request, response,"HOLIDAYMST");
            }else if (action.equalsIgnoreCase("downloadSalaryMstTemplate")) {
                onDownloadTemplate(request, response,"SALARYMST");    
            }else if (action.equalsIgnoreCase("downloadKittingTemplate")) {
				onDownloadTemplate(request, response,"KITTING");
			} else if (action.equalsIgnoreCase("downloadSupplierDiscountTemplate")) {
				onDownloadTemplate(request, response,"SUPPLIERDISCOUNT");
			}  else if (action.equalsIgnoreCase("downloadCustomerDiscountTemplate")) {
				onDownloadTemplate(request, response,"CUSTOMERDISCOUNT");
			}else if (action.equalsIgnoreCase("downloadStockTakeTemplate")) {
		    	onDownloadTemplate(request, response,"STOCKTAKE");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(CONTENT_TYPE);
		doGet(request, response);
	}

	/* ************Modification History*********************************
	  Nov 13 2014,Bruhan,Description: To include Download TransferAssignee,LoanAssigneeTemplate and Employee master
    */
	private void onDownloadTemplate(HttpServletRequest request,
			HttpServletResponse response,String masterType) throws ServletException, IOException,
			Exception {

		String path = "";

		response.setContentType("application/vnd.ms-excel");
                if (masterType.equalsIgnoreCase("LOCATION")){
                    path= DbBean.DownloadLocTemplate;
                        response.setHeader("Content-Disposition","attachment; filename=\"LocMstData.xls\"");
                }else if (masterType.equalsIgnoreCase("SUPPLIER")){
                    path= DbBean.DownloadSupTemplate;
                        response.setHeader("Content-Disposition", "attachment; filename=\"SupplierMasterTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("CUSTOMER")){
                    path= DbBean.DownloadCustTemplate;
                        response.setHeader("Content-Disposition", "attachment; filename=\"CustomerMasterTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("TRANSFERASSIGNEE")){
                    path= DbBean.DownloadTransferAssigneeTemplate;
                        response.setHeader("Content-Disposition", "attachment; filename=\"TransferOrderCustomerMasterTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("LOANASSIGNEE")){
                    path= DbBean.DownloadLoanAssigneeTemplate;
                        response.setHeader("Content-Disposition", "attachment; filename=\"RentalandServiceCustomerMasterTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("EMPLOYEE")){
                    path= DbBean.DownloadEmployeeWithPayollTemplate;
                        response.setHeader("Content-Disposition", "attachment; filename=\"EmployeeMasterWithPayrollTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("EMPLOYEEWITHOUTPAYROLL")){
                    path= DbBean.DownloadEmployeeWithoutPayollTemplate;
                    response.setHeader("Content-Disposition", "attachment; filename=\"EmployeeMasterWithoutPayollTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("EMPLOYEELEAVEDET")){
                    path= DbBean.DownloadEmployeeLeaveDetTemplate;
                    response.setHeader("Content-Disposition", "attachment; filename=\"EmployeeLeaveDetailsTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("EMPLOYEESALARYDET")){
                    path= DbBean.DownloadEmployeeSalaryDetTemplate;
                    response.setHeader("Content-Disposition", "attachment; filename=\"EmployeeSalaryDetailsTemplate.xls\"");    
                }else if (masterType.equalsIgnoreCase("HOLIDAYMST")){
                    path= DbBean.DownloadHolidayMstTemplate;
                    response.setHeader("Content-Disposition", "attachment; filename=\"HolidayMasterTemplate.xls\"");
                }else if (masterType.equalsIgnoreCase("SALARYMST")){
                    path= DbBean.DownloadSalaryMstTemplate;
                    response.setHeader("Content-Disposition", "attachment; filename=\"SalaryMasterTemplate.xls\"");    
                }else if (masterType.equalsIgnoreCase("KITTING")){
                    path= DbBean.DownloadKittingTemplate;
                    response.setHeader("Content-Disposition", "attachment; filename=\"KittingTemplate.xls\"");
               }else if (masterType.equalsIgnoreCase("SUPPLIERDISCOUNT")){
                   path= DbBean.DownloadSupplierDiscountTemplate;
                   response.setHeader("Content-Disposition", "attachment; filename=\"SupplierDiscount.xls\"");
               }else if (masterType.equalsIgnoreCase("CUSTOMERDISCOUNT")){
                 path= DbBean.DownloadCustomerDiscountTemplate;
                 response.setHeader("Content-Disposition", "attachment; filename=\"CustomerDiscount.xls\"");
               } else if (masterType.equalsIgnoreCase("STOCKTAKE")){
                   path= DbBean.DownloadStockTakeTemplate;
                   response.setHeader("Content-Disposition", "attachment; filename=\"StockTake.xls\"");
               } else if (masterType.equalsIgnoreCase("EMPLOYEELEAVEAPPLY")){
                     path= DbBean.DownloadApplyLeaveForEmployeeTemplate;
                     response.setHeader("Content-Disposition", "attachment; filename=\"ApplyLeaveForEmployeeTemplate.xls\"");
                }
                
                

		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		path = path.replace("\\", File.separator);
		System.out.println("Path before file object :" + path);
		//java.net.URL url = new java.net.URL("file://" + path);
		// File file = new File(url.getPath());
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.println("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(
				file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);

		int i;
		//byte[] b = new byte[10];
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}

		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();

	}

   /* ************Modification History*********************************
	  Nov 13 2014,Bruhan,Description: To include Download TransferAssignee,LoanAssigneeTemplate and Employee master
    */
	private void onImportCountSheet(HttpServletRequest request,
			HttpServletResponse response,String masterType) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		String REGION = (String) request.getSession().getAttribute("REGION");
		String NOOFSUPPLIER = (String) request.getSession().getAttribute("NOOFSUPPLIER");
		String NOOFCUSTOMER = (String) request.getSession().getAttribute("NOOFCUSTOMER");
		String NOOFEMPLOYEE = (String) request.getSession().getAttribute("NOOFEMPLOYEE");
		String NOOFLOCATION = (String) request.getSession().getAttribute("NOOFLOCATION"); //IMTI
		String baseCurrency = (String) request.getSession().getAttribute("BASE_CURRENCY"); //Author Name: Resvi , Date: 12/07/2021 
		
		System.out
				.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		// MLogger.info("CS Upload path : " + DbBean.CountSheetUploadPath);

		try {

		
			try {

				MultipartRequest mreq = new MultipartRequest(request,
						DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {

				throw eee;

			}

			request.getSession().setAttribute("IMP_RESULT", null);
			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;

			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;

			// StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"
					+ StrFileName);

			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();

		    if (masterType.equalsIgnoreCase("LOCATION")){    
			alRes = util.downloadCountSheetData(PLANT, StrFileName,StrSheetName,truncateval,NOOFLOCATION);//IMTI
                    }else if (masterType.equalsIgnoreCase("SUPPLIER")){                         
		       alRes = util.downloadSupplierSheetData(PLANT, StrFileName, StrSheetName,REGION,NOOFSUPPLIER,baseCurrency);
                    }else if (masterType.equalsIgnoreCase("CUSTOMER")){                         
		       alRes = util.downloadCustomerSheetData(PLANT, StrFileName, StrSheetName,REGION,NOOFCUSTOMER,baseCurrency);
                    }else if (masterType.equalsIgnoreCase("TRANSFERASSIGNEE")){                         
		       alRes = util.downloadTransferAssigneeSheetData(PLANT, StrFileName, StrSheetName);
                    }else if (masterType.equalsIgnoreCase("LOANASSIGNEE")){                         
		       alRes = util.downloadLoanAssigneeSheetData(PLANT, StrFileName, StrSheetName);
                    }else if (masterType.equalsIgnoreCase("EMPLOYEE")){                         
		       alRes = util.downloadEmployeeSheetData(PLANT, StrFileName, StrSheetName,REGION,NOOFEMPLOYEE);
               }else if (masterType.equalsIgnoreCase("EMPLOYEELEAVEDET")){                         
		       alRes = util.downloadEmployeeleavedetSheetData(PLANT, StrFileName, StrSheetName,REGION);
               }else if (masterType.equalsIgnoreCase("EMPLOYEESALARYDET")){                         
    		       alRes = util.downloadEmployeeSalarydetSheetData(PLANT, StrFileName, StrSheetName,REGION);
               }else if (masterType.equalsIgnoreCase("HOLIDAYMST")){                         
		       alRes = util.downloadHolidayMstSheetData(PLANT, StrFileName, StrSheetName,REGION);
               }else if (masterType.equalsIgnoreCase("SALARYMST")){                         
    		       alRes = util.downloadSalaryMstSheetData(PLANT, StrFileName, StrSheetName,REGION);
               }else if (masterType.equalsIgnoreCase("KITTING")){                         
         		       alRes = util.downloadKittingSheetData(PLANT, StrFileName, StrSheetName);
               }else if (masterType.equalsIgnoreCase("SUPPLIERDISCOUNT")){                         
     		       alRes = util.downloadSupplierDiscountSheetData(PLANT, StrFileName, StrSheetName);
               }else if (masterType.equalsIgnoreCase("CUSTOMERDISCOUNT")){                         
     		       alRes = util.downloadCustomerDiscountSheetData(PLANT, StrFileName, StrSheetName);
               } else if (masterType.equalsIgnoreCase("STOCKTAKE")){                         
     		       alRes = util.downloadStockTakeSheetData(PLANT, StrFileName, StrSheetName);
               }else if (masterType.equalsIgnoreCase("EMPLOYEELEAVEAPPLY")){                         
		       alRes = util.downloadEmployeeLeaveApplySheetData(PLANT, StrFileName, StrSheetName,REGION);
               }

			if (alRes.size() > 0) {
				String ValidNumber ="";
				for (int j = 0; j < alRes.size(); j++) {
				Map mValidNumber=(Map)alRes.get(j);
				ValidNumber = (String)mValidNumber.get("ValidNumber");
				}
				
				int index = alRes.size() - 1; 
				alRes.remove(index);
				
				if(ValidNumber.equalsIgnoreCase(""))
				{
					mlogger.info("Data Imported Successfully");
					result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
					if(ValidNumber.equalsIgnoreCase("SUPPLIER"))
					result = "<font color=\"red\">You have reached the limit of "+NOOFSUPPLIER+" suppliers you can create</font>";
					
					if(ValidNumber.equalsIgnoreCase("CUSTOMER"))
						result = "<font color=\"red\">You have reached the limit of "+NOOFCUSTOMER+" customers you can create</font>";
					
					if(ValidNumber.equalsIgnoreCase("EMPLOYEE"))
						result = "<font color=\"red\">You have reached the limit of "+NOOFEMPLOYEE+" employees you can create</font>";
				
					if(ValidNumber.equalsIgnoreCase("LOCATION"))//IMTI
						result = "<font color=\"red\">You have reached the limit of "+NOOFLOCATION+" location you can create</font>";

				}
			} else {
				throw new Exception("no data found  for given data in the excel");
			}

		} catch (Exception e) {
			// throw e;
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		
	    if (masterType.equalsIgnoreCase("LOCATION")){    
                request.getSession().setAttribute("RESULT", result);
                request.getSession().setAttribute("IMP_RESULT", alRes);
                response.sendRedirect("../location/import?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("SUPPLIER")){
                request.getSession().setAttribute("SUPRESULT", result);
                request.getSession().setAttribute("IMP_SUPRESULT", alRes);
                response.sendRedirect("../supplier/import?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("CUSTOMER")){
                request.getSession().setAttribute("CUSTRESULT", result);
                request.getSession().setAttribute("IMP_CUSTRESULT", alRes);
                response.sendRedirect("../customer/import?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("TRANSFERASSIGNEE")){
               request.getSession().setAttribute("TRANSFERASSIGNEERESULT", result);
               request.getSession().setAttribute("IMP_TRANSFERASSIGNEERESULT", alRes);
               response.sendRedirect("jsp/importTransferAssigneeExcelSheet.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("LOANASSIGNEE")){
              request.getSession().setAttribute("LOANASSIGNEERESULT", result);
              request.getSession().setAttribute("IMP_LOANASSIGNEERESULT", alRes);
              response.sendRedirect("jsp/importLoanAssigneeExcelSheet.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("EMPLOYEE")){
              request.getSession().setAttribute("EMPLOYEERESULT", result);
              request.getSession().setAttribute("IMP_EMPLOYEERESULT", alRes);
              response.sendRedirect("jsp/importEmployeeExcelSheet.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("EMPLOYEELEAVEDET")){
                request.getSession().setAttribute("EMPLOYEERESULT", result);
                request.getSession().setAttribute("IMP_EMPLOYEERESULT", alRes);
                response.sendRedirect("jsp/importEmployeeLeaveDetExcelSheet.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("EMPLOYEESALARYDET")){
                request.getSession().setAttribute("EMPLOYEERESULT", result);
                request.getSession().setAttribute("IMP_EMPLOYEERESULT", alRes);
                response.sendRedirect("jsp/importEmployeeSalaryDetExcelSheet.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("HOLIDAYMST")){
                request.getSession().setAttribute("HOLIDAYRESULT", result);
                request.getSession().setAttribute("IMP_HOLIDAYRESULT", alRes);
                response.sendRedirect("jsp/importHolidayMst.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("SALARYMST")){
                request.getSession().setAttribute("SALARYRESULT", result);
                request.getSession().setAttribute("IMP_SALARYRESULT", alRes);
                response.sendRedirect("jsp/importSalaryMst.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }else if (masterType.equalsIgnoreCase("KITTING")){
              request.getSession().setAttribute("KITTINGRESULT", result);
              request.getSession().setAttribute("IMP_KITTINGRESULT", alRes);
              response.sendRedirect("jsp/importKittingExcelSheet.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
          }else if (masterType.equalsIgnoreCase("SUPPLIERDISCOUNT")){
              request.getSession().setAttribute("SUPPLIERDISCOUNTRESULT", result);
              request.getSession().setAttribute("IMP_SUPPLIERDISCOUNTRESULT", alRes);
              response.sendRedirect("../supplier/discountimport?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
          }else if (masterType.equalsIgnoreCase("CUSTOMERDISCOUNT")){
              request.getSession().setAttribute("CUSTOMERDISCOUNTRESULT", result);
              request.getSession().setAttribute("IMP_CUSTOMERDISCOUNTRESULT", alRes);
              response.sendRedirect("../customer/importdiscount?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
          } else if (masterType.equalsIgnoreCase("STOCKTAKE")){
              request.getSession().setAttribute("STOCKTAKERESULT", result);
              request.getSession().setAttribute("IMPSTOCKTAKE_RESULT", alRes);
              response.sendRedirect("../import/stocktake?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
          }else if (masterType.equalsIgnoreCase("EMPLOYEELEAVEAPPLY")){
              request.getSession().setAttribute("EMPLOYEERESULT", result);
              request.getSession().setAttribute("IMP_EMPLOYEERESULT", alRes);
              response.sendRedirect("jsp/importEmployeeLeaveApply.jsp?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
            }


	}

	@SuppressWarnings("unchecked")
	private void onConfirmLocCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
		ArrayList alRes = new ArrayList();
		;
		UserTransaction ut = null;
		boolean flag = true;
		boolean errflg = false;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute(
					"LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute(
					"IMP_RESULT");

			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
					
					lineArr.put("LOGIN_USER", _login_user);
					linemap.put(IDBConstants.PLANT, PLANT);
					linemap.put("LOGIN_USER", _login_user);
					linemap.put(IDBConstants.LOC, (String) lineArr.get("LOC"));
					linemap.put(IDBConstants.LOCDESC, (String) lineArr.get("LOCDESC"));
					linemap.put(IDBConstants.REMARKS, (String) lineArr.get("REMARKS"));
					linemap.put(IConstants.ISACTIVE, (String) lineArr.get("ISACTIVE"));
					linemap.put(IDBConstants.ADD1, (String) lineArr.get("ADD1"));
					linemap.put(IDBConstants.ADD2, (String) lineArr.get("ADD2"));
					linemap.put(IDBConstants.ADD3, (String) lineArr.get("ADD3"));
					linemap.put(IConstants.ADD4, (String) lineArr.get("ADD4"));
					linemap.put(IDBConstants.STATE, (String) lineArr.get("STATE"));
					linemap.put(IDBConstants.COUNTRY, (String) lineArr.get("COUNTRY"));
					linemap.put(IDBConstants.ZIP, (String) lineArr.get("ZIP"));
					linemap.put(IDBConstants.TELNO, (String) lineArr.get("TELNO"));
					linemap.put(IConstants.FAX, (String) lineArr.get("FAX"));
					linemap.put("COMNAME", (String) lineArr.get("COMNAME"));
					linemap.put(IConstants.RCBNO, (String) lineArr.get("RCBNO"));
					linemap.put(IDBConstants.LOCTYPEID, (String) lineArr.get("LOC_TYPE_ID"));
					linemap.put(IDBConstants.LOCTYPEID2, (String) lineArr.get("LOC_TYPE_ID2"));
					linemap.put(IDBConstants.LOCTYPEID3, (String) lineArr.get("LOC_TYPE_ID3"));
					flag = process_Wms_CountSheet(linemap);

				}

			}
			//if (!flag) {
			//	throw new Exception("Error in Confirming Count Sheet");
			//}

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_RESULT");
				flag = true;
				result = "<font color=\"green\">Location's Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Location's </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULT", result);
			//throw e;
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
		}

		request.getSession().setAttribute("RESULT", result);
		response.sendRedirect("../location/import?");

	}
        
    @SuppressWarnings("unchecked")
    private void onConfirmSupplierCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";

            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute(
                                    "LOGIN_USER");

                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_SUPRESULT");

                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                                    
                                    linemap.put("LOGIN_USER", _login_user);
                                    linemap.put("PLANT", PLANT);
                                    linemap.put(IDBConstants.VENDOR_CODE, (String) lineArr.get(IDBConstants.VENDOR_CODE));
                                    linemap.put(IDBConstants.VENDOR_NAME, (String) lineArr.get(IDBConstants.VENDOR_NAME));
                                    linemap.put(IDBConstants.RCBNO, (String) lineArr.get(IDBConstants.RCBNO));
                                    linemap.put(IDBConstants.CURRENCYID, (String) lineArr.get("CURRENCY_ID")); //Resvi -12/07/2021
                                    linemap.put(IDBConstants.SUPPLIERTYPEID, (String) lineArr.get(IDBConstants.SUPPLIERTYPEID));
                                    linemap.put(IConstants.TRANSPORTID, (String) lineArr.get(IConstants.TRANSPORTID));
									linemap.put(IDBConstants.NAME, (String) lineArr.get(IDBConstants.NAME));
                                    linemap.put(IDBConstants.DESGINATION, (String) lineArr.get(IDBConstants.DESGINATION));
                                    linemap.put(IConstants.companyregnumber, lineArr.get(IConstants.companyregnumber));
                                    linemap.put(IDBConstants.TELNO, (String) lineArr.get(IDBConstants.TELNO));
                                    linemap.put(IDBConstants.HPNO, (String) lineArr.get(IDBConstants.HPNO));
                                    linemap.put(IDBConstants.FAX, (String) lineArr.get(IDBConstants.FAX));
                                    linemap.put(IDBConstants.EMAIL, (String) lineArr.get(IDBConstants.EMAIL));
                                    linemap.put(IDBConstants.ADDRESS1, (String) lineArr.get(IDBConstants.ADDRESS1));
                                    linemap.put(IConstants.ADDRESS2, (String) lineArr.get(IDBConstants.ADDRESS2));
                                    linemap.put(IDBConstants.ADDRESS3, (String) lineArr.get(IDBConstants.ADDRESS3));
                                    linemap.put(IDBConstants.ADDRESS4, (String) lineArr.get(IDBConstants.ADDRESS4));
                                    linemap.put(IDBConstants.STATE, (String) lineArr.get(IDBConstants.STATE));
                                    linemap.put(IDBConstants.COUNTRY, (String) lineArr.get(IDBConstants.COUNTRY));
                                    linemap.put(IConstants.ZIP, lineArr.get(IDBConstants.ZIP));
                                    linemap.put(IConstants.REMARKS, lineArr.get(IDBConstants.REMARKS));
                                    linemap.put(IConstants.ISACTIVE, lineArr.get(IDBConstants.ISACTIVE));
                                    linemap.put(IConstants.payment_terms, lineArr.get(IConstants.payment_terms));
                                    linemap.put(IConstants.PAYTERMS, lineArr.get(IConstants.PAYTERMS));
                                    linemap.put(IConstants.PAYINDAYS, lineArr.get(IConstants.PAYINDAYS));
                                    
                                    linemap.put(IConstants.CUSTOMEREMAIL, lineArr.get(IConstants.CUSTOMEREMAIL));
                                    linemap.put(IConstants.WORKPHONE, lineArr.get(IConstants.WORKPHONE));
                                    linemap.put(IConstants.FACEBOOK, lineArr.get(IConstants.FACEBOOK));
                                    linemap.put(IConstants.LINKEDIN, lineArr.get(IConstants.LINKEDIN));
                                    linemap.put(IConstants.TWITTER, lineArr.get(IConstants.TWITTER));
                                    linemap.put(IConstants.SKYPE, lineArr.get(IConstants.SKYPE));
                                    linemap.put(IConstants.WEBSITE, lineArr.get(IConstants.WEBSITE));
                                    linemap.put("ISPEPPOL", lineArr.get("ISPEPPOL"));
                                    linemap.put("PEPPOL_ID", lineArr.get("PEPPOL_ID"));
//                                    linemap.put(IConstants.OPENINGBALANCE, lineArr.get(IConstants.OPENINGBALANCE));
                                    linemap.put(IConstants.TAXTREATMENT, lineArr.get(IConstants.TAXTREATMENT));
                                    linemap.put(IDBConstants.CREATED_BY, _login_user);
                                    linemap.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
                                    linemap.put(IDBConstants.IBAN, (String) lineArr.get(IDBConstants.IBAN));
                                    linemap.put(IDBConstants.BANKNAME, (String) lineArr.get(IDBConstants.BANKNAME));
                                    linemap.put(IDBConstants.BANKROUTINGCODE, (String) lineArr.get(IDBConstants.BANKROUTINGCODE));
                                    
                                    flag = process_Wms_Supplier_CountSheet(linemap);
                            }
                            
                    }
            

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_SUPRESULT");
                            flag = true;
                            result = "<font color=\"green\">Supplier Data Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Supplier Data </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("SUPRESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("SUPRESULT", result);
            response.sendRedirect("../supplier/import?");

    }
    
    @SuppressWarnings("unchecked")
    private void onConfirmCustomerCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";

            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_CUSTRESULT");

                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                                    
                                    linemap.put("LOGIN_USER", _login_user);
                                    linemap.put("PLANT", PLANT);
                                    linemap.put(IDBConstants.CUSTOMER_CODE, (String) lineArr.get(IDBConstants.CUSTOMER_CODE));
                                    linemap.put(IDBConstants.CUSTOMER_NAME, (String) lineArr.get(IDBConstants.CUSTOMER_NAME));
                                    linemap.put(IConstants.ISSHOWBYPRODUCT, (String) lineArr.get(IConstants.ISSHOWBYPRODUCT));
                                    linemap.put(IConstants.ISSHOWBYCATEGORY, (String) lineArr.get(IConstants.ISSHOWBYCATEGORY));
                                    linemap.put(IDBConstants.CURRENCYID, (String) lineArr.get("CURRENCY_ID")); 
                                    linemap.put(IDBConstants.RCBNO, (String) lineArr.get(IDBConstants.RCBNO));
                                    linemap.put(IDBConstants.CUSTOMERTYPEID, (String) lineArr.get(IDBConstants.CUSTOMERTYPEID));
                                    linemap.put(IConstants.TRANSPORTID, (String) lineArr.get(IConstants.TRANSPORTID));
                                    linemap.put(IDBConstants.NAME, (String) lineArr.get(IDBConstants.NAME));
                                    linemap.put(IDBConstants.DESGINATION, (String) lineArr.get(IDBConstants.DESGINATION));
                                    linemap.put(IConstants.companyregnumber, lineArr.get(IConstants.companyregnumber));
                                    linemap.put(IDBConstants.TELNO, (String) lineArr.get(IDBConstants.TELNO));
                                    linemap.put(IDBConstants.HPNO, (String) lineArr.get(IDBConstants.HPNO));
                                    linemap.put(IDBConstants.FAX, (String) lineArr.get(IDBConstants.FAX));
                                    linemap.put(IDBConstants.EMAIL, (String) lineArr.get(IDBConstants.EMAIL));
                                    linemap.put(IDBConstants.ADDRESS1, (String) lineArr.get(IDBConstants.ADDRESS1));
                                    linemap.put(IConstants.ADDRESS2, (String) lineArr.get(IDBConstants.ADDRESS2));
                                    linemap.put(IDBConstants.ADDRESS3, (String) lineArr.get(IDBConstants.ADDRESS3));
                                    linemap.put(IDBConstants.ADDRESS4, (String) lineArr.get(IDBConstants.ADDRESS4));
                                    linemap.put(IDBConstants.STATE, (String) lineArr.get(IDBConstants.STATE));
                                    linemap.put(IDBConstants.COUNTRY, (String) lineArr.get(IDBConstants.COUNTRY));
                                    linemap.put(IConstants.ZIP, lineArr.get(IDBConstants.ZIP));

                                    
                					if (((String) lineArr.get(IConstants.SAME_AS_CONTACT_ADDRESS)).equals("Y")){
                						linemap.put(IConstants.SAME_AS_CONTACT_ADDRESS, ("1") );
                						  linemap.put(IConstants.SHIP_CONTACTNAME, lineArr.get(IDBConstants.NAME));
                                          linemap.put(IConstants.SHIP_DESGINATION, lineArr.get(IDBConstants.DESGINATION));
                                          linemap.put(IConstants.SHIP_WORKPHONE, lineArr.get(IDBConstants.WORKPHONE));
                                          linemap.put(IConstants.SHIP_HPNO, lineArr.get(IDBConstants.HPNO));
                                          linemap.put(IConstants.SHIP_EMAIL, lineArr.get(IDBConstants.EMAIL));
                                          linemap.put(IConstants.SHIP_COUNTRY_CODE, lineArr.get(IDBConstants.COUNTRY));
                                          linemap.put(IConstants.SHIP_ADDR1, lineArr.get(IDBConstants.ADDRESS1));	
                                          linemap.put(IConstants.SHIP_ADDR2, lineArr.get(IDBConstants.ADDRESS2));
                                          linemap.put(IConstants.SHIP_ADDR3, lineArr.get(IDBConstants.ADDRESS3));
                                          linemap.put(IConstants.SHIP_ADDR4, lineArr.get(IDBConstants.ADDRESS4));
                                          linemap.put(IConstants.SHIP_STATE, lineArr.get(IDBConstants.STATE));
                                          linemap.put(IConstants.SHIP_ZIP, lineArr.get(IDBConstants.ZIP));
                						}
                						else{
                						linemap.put(IConstants.SAME_AS_CONTACT_ADDRESS, ("0"));
                						  linemap.put(IConstants.SHIP_CONTACTNAME, lineArr.get(IConstants.SHIP_CONTACTNAME));
                                          linemap.put(IConstants.SHIP_DESGINATION, lineArr.get(IConstants.SHIP_DESGINATION));
                                          linemap.put(IConstants.SHIP_WORKPHONE, lineArr.get(IConstants.SHIP_WORKPHONE));
                                          linemap.put(IConstants.SHIP_HPNO, lineArr.get(IConstants.SHIP_HPNO));
                                          linemap.put(IConstants.SHIP_EMAIL, lineArr.get(IConstants.SHIP_EMAIL));
                                          linemap.put(IConstants.SHIP_COUNTRY_CODE, lineArr.get(IConstants.SHIP_COUNTRY_CODE));
                                          linemap.put(IConstants.SHIP_ADDR1, lineArr.get(IConstants.SHIP_ADDR1));	
                                          linemap.put(IConstants.SHIP_ADDR2, lineArr.get(IConstants.SHIP_ADDR2));
                                          linemap.put(IConstants.SHIP_ADDR3, lineArr.get(IConstants.SHIP_ADDR3));
                                          linemap.put(IConstants.SHIP_ADDR4, lineArr.get(IConstants.SHIP_ADDR4));
                                          linemap.put(IConstants.SHIP_STATE, lineArr.get(IConstants.SHIP_STATE));
                                          linemap.put(IConstants.SHIP_ZIP, lineArr.get(IConstants.SHIP_ZIP));
                						}
                                    
                                    
                                    linemap.put(IConstants.REMARKS, lineArr.get(IDBConstants.REMARKS));
                                    linemap.put(IConstants.ISACTIVE, lineArr.get(IDBConstants.ISACTIVE));
                                    linemap.put(IConstants.payment_terms, lineArr.get(IConstants.payment_terms));
                                    linemap.put(IConstants.PAYTERMS, lineArr.get(IConstants.PAYTERMS));
                                    linemap.put(IConstants.PAYINDAYS, lineArr.get(IConstants.PAYINDAYS));
                                    linemap.put(IConstants.CUSTOMERSTATUSID, lineArr.get(IConstants.CUSTOMERSTATUSID));
									linemap.put(IConstants.CREDITLIMIT, lineArr.get(IConstants.CREDITLIMIT));
                                    linemap.put(IConstants.CREDIT_LIMIT_BY, lineArr.get(IConstants.CREDIT_LIMIT_BY));
                                    
                                    linemap.put(IConstants.CUSTOMEREMAIL, lineArr.get(IConstants.CUSTOMEREMAIL));
                                    linemap.put(IConstants.WORKPHONE, lineArr.get(IConstants.WORKPHONE));
                                    linemap.put(IConstants.FACEBOOK, lineArr.get(IConstants.FACEBOOK));
                                    linemap.put(IConstants.LINKEDIN, lineArr.get(IConstants.LINKEDIN));
                                    linemap.put(IConstants.TWITTER, lineArr.get(IConstants.TWITTER));
                                    linemap.put(IConstants.SKYPE, lineArr.get(IConstants.SKYPE));
                                    linemap.put(IConstants.WEBSITE, lineArr.get(IConstants.WEBSITE));
                                    linemap.put("ISPEPPOL", lineArr.get("ISPEPPOL"));
                                    linemap.put("PEPPOL_ID", lineArr.get("PEPPOL_ID"));
//                                    linemap.put(IConstants.OPENINGBALANCE, lineArr.get(IConstants.OPENINGBALANCE));
                                    linemap.put(IConstants.TAXTREATMENT, lineArr.get(IConstants.TAXTREATMENT));
                                    linemap.put(IDBConstants.CREATED_BY, _login_user);
                                    linemap.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
                                    linemap.put(IDBConstants.IBAN, (String) lineArr.get(IDBConstants.IBAN));
                                    linemap.put(IDBConstants.BANKNAME, (String) lineArr.get(IDBConstants.BANKNAME));
                                    linemap.put(IDBConstants.BANKROUTINGCODE, (String) lineArr.get(IDBConstants.BANKROUTINGCODE));
                                    
                                    flag = process_Wms_Customer_CountSheet(linemap);
                            }
                            
                    }
            

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_CUSTRESULT");
                            flag = true;
                            result = "<font color=\"green\">Customer Data Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Customer Data </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("CUSTRESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("CUSTRESULT", result);
            response.sendRedirect("../customer/import?");

    }
 
    /***Created on Nov 13 2014,Bruhan,Description: To include Transfer Assignee master
    /* ************Modification History*********************************
	  
    */
    @SuppressWarnings("unchecked")
    private void onConfirmTransferAssigneeCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";

            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_TRANSFERASSIGNEERESULT");

                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                                    
                                    linemap.put("LOGIN_USER", _login_user);
                                    linemap.put("PLANT", PLANT);
                                    linemap.put(IDBConstants.ASSIGNENO, (String) lineArr.get(IDBConstants.ASSIGNENO));
                                    linemap.put(IDBConstants.ASSIGNENAME, (String) lineArr.get(IDBConstants.ASSIGNENAME));
                                    linemap.put(IDBConstants.NAME, (String) lineArr.get(IDBConstants.NAME));
                                    linemap.put(IDBConstants.DESGINATION, (String) lineArr.get(IDBConstants.DESGINATION));
                                    linemap.put(IDBConstants.TELNO, (String) lineArr.get(IDBConstants.TELNO));
                                    linemap.put(IDBConstants.HPNO, (String) lineArr.get(IDBConstants.HPNO));
                                    linemap.put(IDBConstants.FAX, (String) lineArr.get(IDBConstants.FAX));
                                    linemap.put(IDBConstants.EMAIL, (String) lineArr.get(IDBConstants.EMAIL));
                                    linemap.put(IDBConstants.ADDRESS1, (String) lineArr.get(IDBConstants.ADDRESS1));
                                    linemap.put(IConstants.ADDRESS2, (String) lineArr.get(IDBConstants.ADDRESS2));
                                    linemap.put(IDBConstants.ADDRESS3, (String) lineArr.get(IDBConstants.ADDRESS3));
                                    linemap.put(IDBConstants.ADDRESS4, (String) lineArr.get(IDBConstants.ADDRESS4));
                                    linemap.put(IDBConstants.STATE, (String) lineArr.get(IDBConstants.STATE));
                                    linemap.put(IDBConstants.COUNTRY, (String) lineArr.get(IDBConstants.COUNTRY));
                                    linemap.put(IConstants.ZIP, lineArr.get(IDBConstants.ZIP));
                                    linemap.put(IConstants.REMARKS, lineArr.get(IDBConstants.REMARKS));
                                    linemap.put(IConstants.ISACTIVE, lineArr.get(IDBConstants.ISACTIVE));
                                    flag = process_Wms_TransferAssignee_CountSheet(linemap);
                            }
                            
                    }
            

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_TRANSFERASSIGNEERESULT");
                            flag = true;
                            result = "<font color=\"green\">Transfer Order Customer Data Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Transfer Order Customer Data </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("TRANSFERASSIGNEERESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("TRANSFERASSIGNEERESULT", result);
            response.sendRedirect("jsp/importTransferAssigneeExcelSheet.jsp?");

    }
    
    
    /***Created on Nov 13 2014,Bruhan,Description: To include Loan Assignee master
    /* ************Modification History*********************************
	  
    */
    @SuppressWarnings("unchecked")
    private void onConfirmLoanAssigneeCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";

            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_LOANASSIGNEERESULT");

                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                                    
                                    linemap.put("LOGIN_USER", _login_user);
                                    linemap.put("PLANT", PLANT);
                                    linemap.put(IDBConstants.LASSIGNNO, (String) lineArr.get(IDBConstants.LASSIGNNO));
                                    linemap.put(IDBConstants.CUSTOMER_NAME, (String) lineArr.get(IDBConstants.CUSTOMER_NAME));
                                    linemap.put(IDBConstants.NAME, (String) lineArr.get(IDBConstants.NAME));
                                    linemap.put(IDBConstants.DESGINATION, (String) lineArr.get(IDBConstants.DESGINATION));
                                    linemap.put(IDBConstants.TELNO, (String) lineArr.get(IDBConstants.TELNO));
                                    linemap.put(IDBConstants.HPNO, (String) lineArr.get(IDBConstants.HPNO));
                                    linemap.put(IDBConstants.FAX, (String) lineArr.get(IDBConstants.FAX));
                                    linemap.put(IDBConstants.EMAIL, (String) lineArr.get(IDBConstants.EMAIL));
                                    linemap.put(IDBConstants.ADDRESS1, (String) lineArr.get(IDBConstants.ADDRESS1));
                                    linemap.put(IConstants.ADDRESS2, (String) lineArr.get(IDBConstants.ADDRESS2));
                                    linemap.put(IDBConstants.ADDRESS3, (String) lineArr.get(IDBConstants.ADDRESS3));
                                    linemap.put(IDBConstants.ADDRESS4, (String) lineArr.get(IDBConstants.ADDRESS4));
                                    linemap.put(IDBConstants.STATE, (String) lineArr.get(IDBConstants.STATE));
                                    linemap.put(IDBConstants.COUNTRY, (String) lineArr.get(IDBConstants.COUNTRY));
                                    linemap.put(IConstants.ZIP, lineArr.get(IDBConstants.ZIP));
                                    linemap.put(IConstants.REMARKS, lineArr.get(IDBConstants.REMARKS));
                                    linemap.put(IConstants.ISACTIVE, lineArr.get(IDBConstants.ISACTIVE));
                                    flag = process_Wms_LoanAssignee_CountSheet(linemap);
                            }
                            
                    }
            

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_LOANASSIGNEERESULT");
                            flag = true;
                            result = "<font color=\"green\">Rental and Service Customer Data Confirmed Successfully</font>";
                           
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Rental and Service Customer Data </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("LOANASSIGNEERESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("LOANASSIGNEERESULT", result);
            response.sendRedirect("jsp/importLoanAssigneeExcelSheet.jsp?");

    }

    
    /***Created on Nov 13 2014,Bruhan,Description: To include Employee master
    /* ************Modification History*********************************
	  
    */
    @SuppressWarnings("unchecked")
    
    private void onConfirmEmployeeCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";
            EmployeeUtil custUtil = new EmployeeUtil();
            EmployeeDAO employeeDAO = new EmployeeDAO();
            TblControlDAO _TblControlDAO =new TblControlDAO();
            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
            		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
            	    String ispay = _PlantMstDAO.getispayroll(PLANT);
                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_EMPLOYEERESULT");
                    int[] sqnum = new int[al.size()];
                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                                    
                                    linemap.put("LOGIN_USER", _login_user);
                                    linemap.put("PLANT", PLANT);
                                    linemap.put(IDBConstants.EMPNO, (String) lineArr.get(IDBConstants.EMPNO));
                                    String empcode = (String) lineArr.get(IDBConstants.EMPNO);
                                    sqnum[iCnt] = checkempcode(empcode);
                                    linemap.put(IDBConstants.FNAME, (String) lineArr.get(IDBConstants.FNAME)); 
                                    linemap.put(IDBConstants.EMPUSERID, (String) lineArr.get(IDBConstants.EMPUSERID));
                                    linemap.put(IDBConstants.PASSWORD_EMP, (String) lineArr.get(IDBConstants.PASSWORD_EMP));
                                    linemap.put(IDBConstants.EMPLOYEETYPEID, (String) lineArr.get(IDBConstants.EMPLOYEETYPEID));  
                                    linemap.put(IDBConstants.GENDER, (String) lineArr.get(IDBConstants.GENDER));
                                    linemap.put(IDBConstants.DOB, (String) lineArr.get(IDBConstants.DOB));
                                    linemap.put(IDBConstants.TELNO, (String) lineArr.get(IDBConstants.TELNO));
                                    linemap.put(IDBConstants.EMAIL, (String) lineArr.get(IDBConstants.EMAIL));
                                    linemap.put(IDBConstants.PASSPORTNUMBER, (String) lineArr.get(IDBConstants.PASSPORTNUMBER));
                                    linemap.put(IDBConstants.COUNTRYOFISSUE, (String) lineArr.get(IDBConstants.COUNTRYOFISSUE));
                                    linemap.put(IDBConstants.PASSPORTEXPIRYDATE, (String) lineArr.get(IDBConstants.PASSPORTEXPIRYDATE));
                                    linemap.put(IDBConstants.REPORTING_INCHARGE, (String) lineArr.get(IDBConstants.REPORTING_INCHARGE));
                                    linemap.put(IDBConstants.OUTLETS_CODE, (String) lineArr.get(IDBConstants.OUTLETS_CODE));
                                    linemap.put(IDBConstants.COUNTRY, (String) lineArr.get(IDBConstants.COUNTRY));
                                    linemap.put(IDBConstants.ADDRESS1, (String) lineArr.get(IDBConstants.ADDRESS1));
                                    linemap.put(IConstants.ADDRESS2, (String) lineArr.get(IDBConstants.ADDRESS2));
                                    linemap.put(IDBConstants.ADDRESS3, (String) lineArr.get(IDBConstants.ADDRESS3));
                                    linemap.put(IDBConstants.ADDRESS4, (String) lineArr.get(IDBConstants.ADDRESS4));
                                    linemap.put(IDBConstants.STATE, (String) lineArr.get(IDBConstants.STATE));                                    
                                    linemap.put(IConstants.ZIP, lineArr.get(IDBConstants.ZIP));                                    
                                    linemap.put(IDBConstants.FACEBOOK, (String) lineArr.get(IDBConstants.FACEBOOK));
                                    linemap.put(IDBConstants.TWITTER, (String) lineArr.get(IDBConstants.TWITTER));
                                    linemap.put(IDBConstants.LINKEDIN, (String) lineArr.get(IDBConstants.LINKEDIN));
                                    linemap.put(IDBConstants.SKYPE, (String) lineArr.get(IDBConstants.SKYPE));
                                    linemap.put(IDBConstants.EMIRATESID, (String) lineArr.get(IDBConstants.EMIRATESID));
                                    linemap.put(IDBConstants.EMIRATESIDEXPIRY, (String) lineArr.get(IDBConstants.EMIRATESIDEXPIRY));
                                    linemap.put(IDBConstants.VISANUMBER, (String) lineArr.get(IDBConstants.VISANUMBER));
                                    linemap.put(IDBConstants.VISAEXPIRYDATE, (String) lineArr.get(IDBConstants.VISAEXPIRYDATE));
                                    linemap.put(IDBConstants.DEPTARTMENT, (String) lineArr.get(IDBConstants.DEPTARTMENT));
                                    linemap.put(IDBConstants.DESGINATION, (String) lineArr.get(IDBConstants.DESGINATION));                                     
                                    linemap.put("ISCASHIER", (String) lineArr.get("ISCASHIER"));                                     
                                    linemap.put("ISSALESMAN", (String) lineArr.get("ISSALESMAN"));                                     
                                    linemap.put(IDBConstants.DATEOFJOINING, (String) lineArr.get(IDBConstants.DATEOFJOINING));
                                    linemap.put(IDBConstants.DATEOFLEAVING, (String) lineArr.get(IDBConstants.DATEOFLEAVING));                                    
                                    linemap.put(IDBConstants.LABOURCARDNUMBER, (String) lineArr.get(IDBConstants.LABOURCARDNUMBER));
                                    linemap.put(IDBConstants.WORKPERMITNUMBER, (String) lineArr.get(IDBConstants.WORKPERMITNUMBER));
                                    linemap.put(IDBConstants.CONTRACTSTARTDATE, (String) lineArr.get(IDBConstants.CONTRACTSTARTDATE));
                                    linemap.put(IDBConstants.CONTRACTENDDATE, (String) lineArr.get(IDBConstants.CONTRACTENDDATE));
                                    linemap.put(IDBConstants.IBAN, (String) lineArr.get(IDBConstants.IBAN));
                                    linemap.put(IDBConstants.BANKNAME, (String) lineArr.get(IDBConstants.BANKNAME));
                                    linemap.put(IDBConstants.BANKROUTINGCODE, (String) lineArr.get(IDBConstants.BANKROUTINGCODE)); 
                                    /*linemap.put(IDBConstants.BASICSALARY, (String) lineArr.get(IDBConstants.BASICSALARY)); 
                                    linemap.put(IDBConstants.HOUSERENTALLOWANCE, (String) lineArr.get(IDBConstants.HOUSERENTALLOWANCE));
                                    linemap.put(IDBConstants.TRANSPORTALLOWANCE, (String) lineArr.get(IDBConstants.TRANSPORTALLOWANCE));
                                    linemap.put(IDBConstants.COMMUNICATIONALLOWANCE, (String) lineArr.get(IDBConstants.COMMUNICATIONALLOWANCE));                                     
                                    linemap.put(IDBConstants.OTHERALLOWANCE, (String) lineArr.get(IDBConstants.OTHERALLOWANCE));
                                    linemap.put(IDBConstants.BONUS, (String) lineArr.get(IDBConstants.BONUS));
                                    linemap.put(IDBConstants.COMMISSION, (String) lineArr.get(IDBConstants.COMMISSION));*/
                                    linemap.put(IDBConstants.GRATUITY, (String) lineArr.get(IDBConstants.GRATUITY)); 
                                    linemap.put(IDBConstants.AIRTICKET, (String) lineArr.get(IDBConstants.AIRTICKET));
                                    linemap.put(IDBConstants.LEAVESALARY, (String) lineArr.get(IDBConstants.LEAVESALARY));
                                    linemap.put(IConstants.REMARKS, lineArr.get(IDBConstants.REMARKS));
                                    linemap.put(IConstants.ISACTIVE, lineArr.get(IDBConstants.ISACTIVE));
                                    flag = process_Wms_Employee_CountSheet(linemap);
                            }
            
                    if (flag == true) {
                    	if(ispay.equalsIgnoreCase("1")) {
	                    	Hashtable htTblloginInsert  = new Hashtable(); 
	                    	String emnoid = employeeDAO.getEmpid(PLANT, (String)linemap.get(IConstants.EMPNO), "");
	        			    htTblloginInsert.put(IDBConstants.PLANT,PLANT);          
	        			    htTblloginInsert.put("EMPNOID",emnoid);
	        			    htTblloginInsert.put("EMPUSERID",(String) linemap.get(IConstants.EMPUSERID));
	        			    htTblloginInsert.put("PASSWORD",(String) linemap.get(IConstants.PASSWORD_EMP));
	        			    htTblloginInsert.put("ISCASHIER", (String) linemap.get("ISCASHIER"));                                     
	        			    htTblloginInsert.put("ISSALESMAN", (String) linemap.get("ISSALESMAN"));
	        	           	htTblloginInsert.put(IDBConstants.CREATED_BY, _login_user);
	        	           	htTblloginInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	        	           	flag=custUtil.insertIntoEmployeeuseidMst(htTblloginInsert);
                    	}
                    } else {
                        DbBean.RollbackTran(ut);
                        flag = false;
                        result = "<font color=\"red\">Error in Confirming Employee Data </font>";
                    }
                    }
                    	
                    if (flag == true) {
                    		IntSummaryStatistics stat = Arrays.stream(sqnum).summaryStatistics();
                    		int maxcode = stat.getMax();
                    		if(maxcode != 0) {
	                    		boolean exitFlag = false;
	                			Hashtable htv = new Hashtable();				
	                			htv.put(IDBConstants.PLANT, PLANT);
	                			htv.put(IDBConstants.TBL_FUNCTION, "EMPLOYEE");
	                			exitFlag = _TblControlDAO.isExisit(htv, "", PLANT);
	                			if (exitFlag) 
	                    		 _TblControlDAO.updateMannualSeqNo("EMPLOYEE",PLANT,maxcode);
	                			else
	                			{
	                				boolean insertFlag = false;
	                				Map htInsert=null;
	                            	Hashtable htTblCntInsert  = new Hashtable();           
	                            	htTblCntInsert.put(IDBConstants.PLANT,PLANT);          
	                            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
	                            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"E");
	                             	htTblCntInsert.put("MINSEQ","0000");
	                             	htTblCntInsert.put("MAXSEQ","9999");
	                            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, String.valueOf(maxcode));
	                            	htTblCntInsert.put(IDBConstants.CREATED_BY, _login_user);
	                            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	                            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,PLANT);
	                			}
                    		}
             
	                } else {
	                        DbBean.RollbackTran(ut);
	                        flag = false;
	                        result = "<font color=\"red\">Error in Confirming Employee Data </font>";
	                }

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_EMPLOYEERESULT");
                            flag = true;
                            result = "<font color=\"green\">Employee Data Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Employee Data </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("EMPLOYEERESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("EMPLOYEERESULT", result);
            response.sendRedirect("jsp/importEmployeeExcelSheet.jsp?");

    }
    
 @SuppressWarnings("unchecked")
    
    private void onConfirmEmployeeLeaveDetCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";
            EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
            DateUtils dateutils = new DateUtils();
            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_EMPLOYEERESULT");

                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                            	boolean ischeck = employeeLeaveDetDAO.IsEmployeeLeavedet(PLANT, Integer.valueOf((String) lineArr.get("LEAVETYPEID")), Integer.valueOf((String) lineArr.get("EMPID")), (String) lineArr.get("LYEAR"));  
                            	if(!ischeck) {
	                            	 EmployeeLeaveDET employeeLeavedet = new EmployeeLeaveDET();
	            	        		 employeeLeavedet.setPLANT(PLANT);
	            	        		 employeeLeavedet.setEMPNOID(Integer.valueOf((String) lineArr.get("EMPID")));
	            	        		 employeeLeavedet.setLEAVETYPEID(Integer.valueOf((String) lineArr.get("LEAVETYPEID")));
	            	        		 employeeLeavedet.setTOTALENTITLEMENT(Double.valueOf((String) lineArr.get("TDAYS")));
	            	        		 employeeLeavedet.setLEAVEBALANCE(Double.valueOf((String) lineArr.get("TDAYS")));
	            	        		 employeeLeavedet.setLEAVEYEAR((String) lineArr.get("LYEAR"));
	            	        		 employeeLeavedet.setNOTE((String) lineArr.get("NOTES"));
	            	        		 employeeLeavedet.setCRAT(dateutils.getDate());
	            	        		 employeeLeavedet.setCRBY(_login_user);
	            	        		 flag = employeeLeaveDetDAO.addEmployeeLeavedetboolean(employeeLeavedet);
                            	}
                                   
                            }
                            
                    }
            

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_EMPLOYEERESULT");
                            flag = true;
                            result = "<font color=\"green\">Employee Leave Details Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Employee Leave Details </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("EMPLOYEERESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("EMPLOYEERESULT", result);
            response.sendRedirect("jsp/importEmployeeLeaveDetExcelSheet.jsp?");

    }
 
 @SuppressWarnings("unchecked")
 
 private void onConfirmEmployeeSalaryDetCountSheet(HttpServletRequest request,
                 HttpServletResponse response) throws ServletException, IOException,
                 Exception {
         String result = "";
         HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
         DateUtils dateutils = new DateUtils();
         UserTransaction ut = null;
         boolean flag = true;
         boolean errflg = false;
         try {
                 ut = com.track.gates.DbBean.getUserTranaction();
                 ut.begin();
                 String PLANT = (String) request.getSession().getAttribute("PLANT");
                 String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                 ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_EMPLOYEERESULT");

                 Map linemap = new HashMap();
                 for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                         Map lineArr = (Map) al.get(iCnt);
                         
                         if(flag){
                         	boolean ischeck = hrEmpSalaryDetDAO.IsEmpSalarydet(PLANT, (String) lineArr.get("SALARYTYPE"), Integer.valueOf((String) lineArr.get("EMPID")));  
                         	if(!ischeck) {
	                            	 HrEmpSalaryDET hrEmpSalaryDET = new HrEmpSalaryDET();
	                            	 hrEmpSalaryDET.setPLANT(PLANT);
	                            	 hrEmpSalaryDET.setEMPNOID(Integer.valueOf((String) lineArr.get("EMPID")));
	                            	 hrEmpSalaryDET.setSALARYTYPE((String) lineArr.get("SALARYTYPE"));
	                            	 hrEmpSalaryDET.setSALARY(Double.valueOf((String) lineArr.get("SALARY")));
	                            	 hrEmpSalaryDET.setCRAT(dateutils.getDate());
	                            	 hrEmpSalaryDET.setCRBY(_login_user);
	            	        		 flag = hrEmpSalaryDetDAO.addSalarydetboolean(hrEmpSalaryDET);
                         	}
                                
                         }
                         
                 }
         

                 if (flag == true) {
                         DbBean.CommitTran(ut);
                         request.getSession().removeAttribute("IMP_EMPLOYEERESULT");
                         flag = true;
                         result = "<font color=\"green\">Employee Salary Details Confirmed Successfully</font>";
                 } else {
                         DbBean.RollbackTran(ut);
                         flag = false;
                         result = "<font color=\"red\">Error in Confirming Employee Salary Details </font>";
                 }

         } catch (Exception e) {
                 flag = false;
                 DbBean.RollbackTran(ut);
                 request.getSession().setAttribute("EMPLOYEERESULT", result);
                 //throw e;
                 result = "<font color=\"red\">"+ e.getMessage() +"</font>";
         }

         request.getSession().setAttribute("EMPLOYEERESULT", result);
         response.sendRedirect("jsp/importEmployeeSalaryDetExcelSheet.jsp?");

 }

 @SuppressWarnings("unchecked")
 private void onConfirmHolidayMstCountSheet(HttpServletRequest request,
                 HttpServletResponse response) throws ServletException, IOException,
                 Exception {
         String result = "";
         HrHolidayMstDAO HrHolidayMstDAO =new HrHolidayMstDAO();
 		 MovHisDAO movHisDao = new MovHisDAO();
         DateUtils dateutils = new DateUtils();
         UserTransaction ut = null;
         boolean flag = true;
         boolean errflg = false;
         try {
                 ut = com.track.gates.DbBean.getUserTranaction();
                 ut.begin();
                 String PLANT = (String) request.getSession().getAttribute("PLANT");
                 String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                 ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_HOLIDAYRESULT");

                 Map linemap = new HashMap();
                 for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                         Map lineArr = (Map) al.get(iCnt);
                         
                         if(flag){
                        	 boolean ischeck = HrHolidayMstDAO.IsHoliday(PLANT, (String) lineArr.get("HOLIDAY_DATE"));
                         
                         	if(!ischeck) {
                         		HrHolidayMst hrHolidayMst = new HrHolidayMst();
    		 	    			hrHolidayMst.setPLANT(PLANT);
    		 	    			hrHolidayMst.setHOLIDAY_DATE((String) lineArr.get("HOLIDAY_DATE"));
    		 	    			hrHolidayMst.setHOLIDAY_DESC((String) lineArr.get("HOLIDAY_DESC"));
    		 	    			hrHolidayMst.setCRAT(dateutils.getDate());
    		 	    			hrHolidayMst.setCRBY(_login_user);
    			 	    		int hid = HrHolidayMstDAO.addHoliday(hrHolidayMst);
    			 	    		
    			 	    		Hashtable htMovHis = new Hashtable();
    							htMovHis.clear();
    							htMovHis.put(IDBConstants.PLANT, PLANT);					
    							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_HOLIDAY);	
    							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
    							htMovHis.put("RECID", "");
    							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hid);
    							htMovHis.put(IDBConstants.CREATED_BY, _login_user);		
    							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
    							htMovHis.put("REMARKS",(String) lineArr.get("HOLIDAY_DATE"));
    							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
    			 	    		
    			 	    		
	            	        	if(hid > 0) {
	            	        		flag =true;
	            	        	}else {
	            	        		flag =false;
	            	        	}
                         	}       
                         }    
                 }

                 if (flag == true) {
                         DbBean.CommitTran(ut);
                         request.getSession().removeAttribute("IMP_HOLIDAYRESULT");
                         flag = true;
                         result = "<font color=\"green\">Holiday Details Confirmed Successfully</font>";
                 } else {
                         DbBean.RollbackTran(ut);
                         flag = false;
                         result = "<font color=\"red\">Error in Confirming Holiday Details </font>";
                 }

         } catch (Exception e) {
                 flag = false;
                 DbBean.RollbackTran(ut);
                 request.getSession().setAttribute("HOLIDAYRESULT", result);
                 //throw e;
                 result = "<font color=\"red\">"+ e.getMessage() +"</font>";
         }

         request.getSession().setAttribute("HOLIDAYRESULT", result);
         response.sendRedirect("jsp/importHolidayMst.jsp?");

 }
 
 @SuppressWarnings("unchecked")
 private void onConfirmSalaryMstCountSheet(HttpServletRequest request,
                 HttpServletResponse response) throws ServletException, IOException,
                 Exception {
         String result = "";
         HrEmpSalaryDAO hrEmpSalaryDAO =new HrEmpSalaryDAO();
         MovHisDAO movHisDao = new MovHisDAO();
         DateUtils dateutils = new DateUtils();
         UserTransaction ut = null;
         boolean flag = true;
         boolean errflg = false;
         try {
                 ut = com.track.gates.DbBean.getUserTranaction();
                 ut.begin();
                 String PLANT = (String) request.getSession().getAttribute("PLANT");
                 String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                 ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_SALARYRESULT");

                 Map linemap = new HashMap();
                 for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                         Map lineArr = (Map) al.get(iCnt);
                         
                         if(flag){
                        	 boolean ischeck = hrEmpSalaryDAO.IsSalaryExists(PLANT, (String) lineArr.get("SALARYTYPE"));
                         
                         	if(!ischeck) {
                         		HrEmpSalaryMst hrEmpSalaryMst = new HrEmpSalaryMst();
                         		hrEmpSalaryMst.setPLANT(PLANT);
                         		hrEmpSalaryMst.setSALARYTYPE((String) lineArr.get("SALARYTYPE"));
                         		hrEmpSalaryMst.setCRAT(dateutils.getDate());
                         		hrEmpSalaryMst.setCRBY(_login_user);
                         		hrEmpSalaryMst.setIsActive("Y");
    			 	    		int hid = hrEmpSalaryDAO.addSalary(hrEmpSalaryMst);
	            	        	if(hid > 0) {
	            	        		flag =true;
	            	        		
	            	        		Hashtable htMovHis = new Hashtable();
	    							htMovHis.clear();
	    							htMovHis.put(IDBConstants.PLANT, PLANT);					
	    							htMovHis.put("DIRTYPE", TransactionConstants.IMPORT_SALARY_TYPE);	
	    							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
	    							htMovHis.put("RECID", "");
	    							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hid);
	    							htMovHis.put(IDBConstants.CREATED_BY, _login_user);		
	    							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
	    							htMovHis.put("REMARKS",(String) lineArr.get("SALARYTYPE"));
	    							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
	            	        		
	            	        	}else {
	            	        		flag =false;
	            	        	}
                         	}       
                         }    
                 }

                 if (flag == true) {
                         DbBean.CommitTran(ut);
                         request.getSession().removeAttribute("IMP_SALARYRESULT");
                         flag = true;
                         result = "<font color=\"green\">Salary Type Confirmed Successfully</font>";
                 } else {
                         DbBean.RollbackTran(ut);
                         flag = false;
                         result = "<font color=\"red\">Error in Confirming Salary Type </font>";
                 }

         } catch (Exception e) {
                 flag = false;
                 DbBean.RollbackTran(ut);
                 request.getSession().setAttribute("SALARYRESULT", result);
                 //throw e;
                 result = "<font color=\"red\">"+ e.getMessage() +"</font>";
         }

         request.getSession().setAttribute("SALARYRESULT", result);
         response.sendRedirect("jsp/importSalaryMst.jsp?");

 }

    @SuppressWarnings("unchecked")
    
       private void onConfirmKittingCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";

            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            String parentProduct="",parentLoc="",parentBatch="";
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
                    
                    //check parent item with location & batch already and delete
                    ArrayList alDelete = (ArrayList) request.getSession().getAttribute("IMP_KITTINGRESULT");
                    Map linemapDelete = new HashMap();
                    int arrsize= alDelete.size();
                    for (int iCntDelete = 0; iCntDelete < alDelete.size();iCntDelete++) {
                        Map lineArrDelete = (Map) alDelete.get(iCntDelete);
                            BomDAO bomDAO = new BomDAO();
                        	Hashtable htDelete = new Hashtable();
        					htDelete.put(IConstants.PLANT, PLANT);
        					htDelete.put(IConstants.PARENT_PRODUCT,(String) lineArrDelete.get(IDBConstants.PARENTITEM));
        					htDelete.put(IConstants.PARENT_LOC, (String) lineArrDelete.get(IDBConstants.LOC));
        					htDelete.put(IConstants.PARENT_BATCH,  (String) lineArrDelete.get(IDBConstants.PARENT_BATCH));
        					if(!parentProduct.equals((String) lineArrDelete.get(IDBConstants.PARENTITEM))|| !parentLoc.equals((String) lineArrDelete.get(IDBConstants.LOC))|| !parentBatch.equals((String) lineArrDelete.get(IDBConstants.PARENT_BATCH)))
        					{
        					 if (bomDAO.isExisit(htDelete)) {
        						flag =  bomDAO.deleteWMSDekitting(htDelete,(String) lineArrDelete.get(IDBConstants.PARENTITEM), (String) lineArrDelete.get(IDBConstants.LOC),(String) lineArrDelete.get(IDBConstants.PARENT_BATCH));
        					 }
        					}
                             parentProduct   =(String) lineArrDelete.get(IDBConstants.PARENTITEM);
                             parentLoc   =(String) lineArrDelete.get(IDBConstants.LOC);
                             parentBatch   =(String) lineArrDelete.get(IDBConstants.PARENT_BATCH);
                             if(iCntDelete== arrsize-1){
                            	 if (bomDAO.isExisit(htDelete)) {
             						flag =  bomDAO.deleteWMSDekitting(htDelete,(String) lineArrDelete.get(IDBConstants.PARENTITEM), (String) lineArrDelete.get(IDBConstants.LOC),(String) lineArrDelete.get(IDBConstants.PARENT_BATCH));
             					 }
                             }
                             
                        	
                        
                }
                    ////check parent item with location & batch already and delete end
                            
                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_KITTINGRESULT");
                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                                    
                                    linemap.put("LOGIN_USER", _login_user);
                                    linemap.put("PLANT", PLANT);
                                    linemap.put(IConstants.PARENT_PRODUCT, (String) lineArr.get(IDBConstants.PARENTITEM));
                                    linemap.put(IConstants.PARENT_LOC, (String) lineArr.get(IDBConstants.LOC));
                                    linemap.put(IConstants.PARENT_BATCH, (String) lineArr.get(IDBConstants.PARENT_BATCH));
                                    linemap.put(IConstants.CHILD_PRODUCT, (String) lineArr.get(IDBConstants.CHILDITEM));
                                    linemap.put(IConstants.CHILD_BATCH, (String) lineArr.get(IDBConstants.CHILD_BATCH));
                                    linemap.put(IConstants.QTY, (String) lineArr.get(IDBConstants.QTY));
                                    linemap.put(IConstants.REMARKS, (String) lineArr.get(IDBConstants.REMARKS));
                                    flag =  process_Wms_Kitting_CountSheet(linemap);
                            }
                            
                    }
            

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_KITTINGRESULT");
                            flag = true;
                            result = "<font color=\"green\">Kitting Data Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Kitting Data </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("KITTINGRESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("KITTINGRESULT", result);
            response.sendRedirect("jsp/importKittingExcelSheet.jsp?");

    }
    
    
    private void onConfirmSupplierDiscountCountSheet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            Exception {
    String result = "";

    UserTransaction ut = null;
    boolean flag = true;
    boolean errflg = false;
    try {
            ut = com.track.gates.DbBean.getUserTranaction();
            ut.begin();
            String PLANT = (String) request.getSession().getAttribute("PLANT");
            String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

            ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_SUPPLIERDISCOUNTRESULT");

            Map linemap = new HashMap();
            for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                    Map lineArr = (Map) al.get(iCnt);
                    
                    if(flag){
                            
                            linemap.put("PLANT", PLANT);
                            linemap.put(IDBConstants.ITEM, (String) lineArr.get(IDBConstants.ITEM));
                            linemap.put(IDBConstants.VENDORCODE, (String) lineArr.get(IDBConstants.VENDORCODE));
                            linemap.put(IDBConstants.IBDISCOUNT, (String) lineArr.get(IDBConstants.IBDISCOUNT));
                            linemap.put("LOGIN_USER", _login_user);
                            flag = process_Wms_SupplierDiscount_CountSheet(linemap);
                    }
                    
            }
                if (flag == true) {
                    DbBean.CommitTran(ut);
                    request.getSession().removeAttribute("IMP_SUPPLIERDISCOUNTRESULT");
                    flag = true;
                    result = "<font color=\"green\">Supplier Discount Purchase Order Data Confirmed Successfully</font>";
            } else {
                    DbBean.RollbackTran(ut);
                    flag = false;
                    result = "<font color=\"red\">Error in Confirming Supplier Discount Purchase Data </font>";
            }
                
                
                

    } catch (Exception e) {
            flag = false;
            DbBean.RollbackTran(ut);
            request.getSession().setAttribute("SUPPLIERDISCOUNTRESULT", result);
            //throw e;
            result = "<font color=\"red\">"+ e.getMessage() +"</font>";
    }

    request.getSession().setAttribute("SUPPLIERDISCOUNTRESULT", result);
    response.sendRedirect("../supplier/discountimport?");

}
    
    private void onConfirmCustomerDiscountCountSheet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            Exception {
    String result = "";

    UserTransaction ut = null;
    boolean flag = true;
    boolean errflg = false;
    try {
            ut = com.track.gates.DbBean.getUserTranaction();
            ut.begin();
            String PLANT = (String) request.getSession().getAttribute("PLANT");
            String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

            ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_CUSTOMERDISCOUNTRESULT");

            Map linemap = new HashMap();
            for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                    Map lineArr = (Map) al.get(iCnt);
                    
                    if(flag){
                          
                            linemap.put("PLANT", PLANT);
                            linemap.put(IDBConstants.ITEM, (String) lineArr.get(IDBConstants.ITEM));
                            linemap.put(IDBConstants.CUSTOMERTYPEID, (String) lineArr.get(IDBConstants.CUSTOMERTYPEID));
                            linemap.put(IDBConstants.OBDISCOUNT, (String) lineArr.get(IDBConstants.OBDISCOUNT));
                            linemap.put("LOGIN_USER", _login_user);
                             flag = process_Wms_CustomerDiscount_CountSheet(linemap);
                    }
                  }
                if (flag == true) {
                    DbBean.CommitTran(ut);
                    request.getSession().removeAttribute("IMP_CUSTOMERDISCOUNTRESULT");
                    flag = true;
                    result = "<font color=\"green\">Customer Discount Sales Order Data Confirmed Successfully</font>";
            } else {
                    DbBean.RollbackTran(ut);
                    flag = false;
                    result = "<font color=\"red\">Error in Confirming Customer Discount Sales Data </font>";
            }

    } catch (Exception e) {
            flag = false;
            DbBean.RollbackTran(ut);
            request.getSession().setAttribute("CUSTOMERDISCOUNTRESULT", result);
            //throw e;
            result = "<font color=\"red\">"+ e.getMessage() +"</font>";
    }

    request.getSession().setAttribute("CUSTOMERDISCOUNTRESULT", result);
    response.sendRedirect("../customer/importdiscount?");

}
    
    @SuppressWarnings("unchecked")
	private void onConfirmStockTakeCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
		ArrayList alRes = new ArrayList();
		UserTransaction ut = null;
		boolean flag = true;
		boolean errflg = false;
		String  QTY = "";
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute(
					"LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute(
					"IMPSTOCKTAKE_RESULT");
			
			String baseCurrency = "";
			PlantMstDAO plantDAO = new PlantMstDAO();
			ArrayList plantDetList = plantDAO.getPlantMstDetails(PLANT);
			for (int i = 0; i < plantDetList.size(); i++) {
				Map plntdet = (Map) plantDetList.get(i);
				baseCurrency = (String) plntdet.get("BASE_CURRENCY");
			}

			String grno = new TblControlDAO().getNextOrder(PLANT,login_user,"GRN");
			String gino = new TblControlDAO().getNextOrder(PLANT,login_user,"GINO");
			String stkno = new TblControlDAO().getNextOrder(PLANT,login_user,"STNO");
			
			String StockTakebyAvgCost = new PlantMstDAO().getStockTakebyAvgCost(PLANT);
			
			Double diffvalue = 0.0;
			int indxgrno =0;
			int indxgino =0;
			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
					
					lineArr.put("LOGIN_USER", _login_user);
					linemap.put(IDBConstants.PLANT, PLANT);
					linemap.put("LOGIN_USER", _login_user);
					linemap.put(IDBConstants.LOC, (String) lineArr.get("LOC"));
					linemap.put(IDBConstants.BATCH, (String) lineArr.get(IDBConstants.BATCH));
					linemap.put(IDBConstants.ITEM, (String) lineArr.get(IDBConstants.ITEM));
					linemap.put(IConstants.QTY, (String) lineArr.get(IConstants.QTY));
					QTY=(String) lineArr.get(IConstants.QTY);
					double qty = Double.parseDouble(QTY);
					qty = StrUtils.RoundDB(qty, IConstants.DECIMALPTS);
					QTY = String.valueOf(qty);
					linemap.put(IConstants.QTY, QTY);
					linemap.put(IDBConstants.REMARKS, "");
					linemap.put(IConstants.UOM, (String) lineArr.get(IConstants.UOM));
					linemap.put("DIFFQTY", (String) lineArr.get("DIFFQTY"));
					linemap.put(IConstants.INV_QTY, (String) lineArr.get(IConstants.INV_QTY));
					linemap.put("INVFLAG", (String) lineArr.get("INVFLAG"));
					linemap.put("GINO", gino);
					linemap.put("GRNO", grno);
					linemap.put("STNO", stkno);
					linemap.put("BASECURRENCY", baseCurrency);
					String Mitem=(String) lineArr.get(IDBConstants.ITEM);
					String MUOM=(String) lineArr.get(IDBConstants.UOM);
					String sDesc = new ItemMstDAO().getItemDesc(PLANT, Mitem);
	                  String itemcost = "0";
	                  String itemprice = "0";
						if(StockTakebyAvgCost.equalsIgnoreCase("1")) {
							itemcost = new DOUtil().getConvertedAverageUnitCostForProductByCurrency(PLANT,MUOM,Mitem);
						} else {
							itemcost = new ItemMstDAO().getItemCost(PLANT, Mitem);
							//itemprice = new ItemMstDAO().getItemPrice(PLANT, Mitem);
						}
						itemprice = itemcost;
						linemap.put(IDBConstants.ITEM_DESC, sDesc);	
						linemap.put("ITEMCOST", itemcost);
						linemap.put("ITEMPRICE", itemprice);
						
						String DIFFQTY= (String) lineArr.get("DIFFQTY");
						double diff = Double.parseDouble(DIFFQTY);
						Double mul = -1.0;
		                Double convalue = mul*Double.valueOf(DIFFQTY);						
						
					String uom= (String) lineArr.get(IConstants.UOM);
					if(!QTY.equalsIgnoreCase("0")&&!uom.equalsIgnoreCase("")) {
						
						if(diff>0)
							indxgino =indxgino +1;
						else
							indxgrno =indxgrno +1;
						linemap.put("indxgino", String.valueOf(indxgino));
						linemap.put("indxgrno", String.valueOf(indxgrno));
						diffvalue = diffvalue+(convalue*Double.valueOf(itemcost));
						flag = process_Wms_StockTakeCountSheet(linemap);
					}
				
				}
				

			}
			

			if (flag == true) {
				
				System.out.println("dvalue=================="+diffvalue);
				
				JournalService journalService = new JournalEntry();
				CoaDAO coaDAO = new CoaDAO();
				Journal journal = new Journal();
				List<JournalDetail> journalDetails = new ArrayList<>();
				
				boolean isAccountinEnabled = new PlantMstUtil().isAccountingModuleEnabled(PLANT);
				if(isAccountinEnabled) {
				if (diffvalue >= 0) {
					JournalHeader journalHead = new JournalHeader();
					journalHead.setPLANT(PLANT);
					journalHead.setJOURNAL_DATE(DateUtils.getDate());
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(baseCurrency);
					journalHead.setTRANSACTION_TYPE("STOCKTAKE");
					//journalHead.setTRANSACTION_ID(gino+"_"+grno);
					journalHead.setTRANSACTION_ID(stkno);
					journalHead.setSUB_TOTAL(diffvalue);
					journalHead.setCRAT(DateUtils.getDateTime());
					journalHead.setCRBY(login_user);

					JournalDetail journalDetail_InvAsset = new JournalDetail();
					journalDetail_InvAsset.setPLANT(PLANT);
					JSONObject coaJson7 = coaDAO.getCOAByName(PLANT, "Inventory Asset");
					System.out.println("Json" + coaJson7.toString());
					journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
					journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
					journalDetail_InvAsset.setCREDITS(diffvalue);
					journalDetails.add(journalDetail_InvAsset);

					JournalDetail journalDetail_COG = new JournalDetail();
					journalDetail_COG.setPLANT(PLANT);
					JSONObject coaJson8 = coaDAO.getCOAByName(PLANT, "Inventory WO");
					System.out.println("Json" + coaJson8.toString());
					journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
					journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
					journalDetail_COG.setDEBITS(diffvalue);
					journalDetails.add(journalDetail_COG);

					journalHead.setTOTAL_AMOUNT(diffvalue);
					journal.setJournalHeader(journalHead);
					journal.setJournalDetails(journalDetails);

					journalService.addJournal(journal, login_user);
					Hashtable jhtMovHis = new Hashtable();
					jhtMovHis.put(IDBConstants.PLANT, PLANT);
					jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
					jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					jhtMovHis.put(IDBConstants.ITEM, "");
					jhtMovHis.put(IDBConstants.QTY, "0.0");
					jhtMovHis.put("RECID", "");
					jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journal.getJournalHeader().getTRANSACTION_TYPE() + " "
							+ journal.getJournalHeader().getTRANSACTION_ID());
					jhtMovHis.put(IDBConstants.CREATED_BY, login_user);
					jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					jhtMovHis.put("REMARKS", "");
					new MovHisDAO().insertIntoMovHis(jhtMovHis);

				}else {
					diffvalue = -1.0 *diffvalue;
					JournalHeader journalHead = new JournalHeader();
					journalHead.setPLANT(PLANT);
					journalHead.setJOURNAL_DATE(DateUtils.getDate());
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(baseCurrency);
					journalHead.setTRANSACTION_TYPE("STOCKTAKE");
					//journalHead.setTRANSACTION_ID(gino+"_"+grno);
					journalHead.setTRANSACTION_ID(stkno);
					journalHead.setSUB_TOTAL(diffvalue);
					journalHead.setCRAT(DateUtils.getDateTime());
					journalHead.setCRBY(login_user);
					
					JournalDetail journalDetail_COG = new JournalDetail();
					journalDetail_COG.setPLANT(PLANT);
					JSONObject coaJson8 = coaDAO.getCOAByName(PLANT,"Inventory Asset");
					System.out.println("Json" + coaJson8.toString());
					journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
					journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
					journalDetail_COG.setDEBITS(diffvalue);
					journalDetails.add(journalDetail_COG);


					JournalDetail journalDetail_InvAsset = new JournalDetail();
					journalDetail_InvAsset.setPLANT(PLANT);
					JSONObject coaJson7 = coaDAO.getCOAByName(PLANT, "Inventory WO");
					System.out.println("Json" + coaJson7.toString());
					journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
					journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
					journalDetail_InvAsset.setCREDITS(diffvalue);
					journalDetails.add(journalDetail_InvAsset);

					journalHead.setTOTAL_AMOUNT(diffvalue);
					journal.setJournalHeader(journalHead);
					journal.setJournalDetails(journalDetails);
					

					journalService.addJournal(journal, login_user);
					Hashtable jhtMovHis = new Hashtable();
					jhtMovHis.put(IDBConstants.PLANT, PLANT);
					jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
					jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					jhtMovHis.put(IDBConstants.ITEM, "");
					jhtMovHis.put(IDBConstants.QTY, "0.0");
					jhtMovHis.put("RECID", "");
					jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journal.getJournalHeader().getTRANSACTION_TYPE() + " "
							+ journal.getJournalHeader().getTRANSACTION_ID());
					jhtMovHis.put(IDBConstants.CREATED_BY, login_user);
					jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					jhtMovHis.put("REMARKS", "");
					new MovHisDAO().insertIntoMovHis(jhtMovHis);
					
				}
			}
				
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", gino);
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRNO", "GR", grno);
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "STNO", "SK", stkno);
				
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMPINV_RESULT");
				flag = true;
				result = "<font color=\"green\">Stock Take Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Stock Take </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
			request.getSession().setAttribute("STOCKTAKERESULT", result);
			
		}

		request.getSession().setAttribute("STOCKTAKERESULT", result);
		response.sendRedirect("../import/stocktake?");

	}
    
@SuppressWarnings("unchecked")
    
    private void onConfirmEmployeeLeaveApplyCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";

            UserTransaction ut = null;
            boolean flag = true;
            boolean errflg = false;
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String plant = (String) request.getSession().getAttribute("PLANT");
                    String username = (String) request.getSession().getAttribute("LOGIN_USER");
            		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
            		HrLeaveApplyHdrService hrLeaveApplyHdrService = new HrLeaveApplyHdrServiceImpl();
            		HrLeaveApplyDetService hrLeaveApplyDetService = new HrLeaveApplyDetServiceImpl();
            		HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
            		EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
            		HrLeaveTypeDAO  hrLeaveTypeDAO = new HrLeaveTypeDAO();
            		MovHisDAO movHisDao = new MovHisDAO();
            		DateUtils dateutils = new DateUtils();
                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_EMPLOYEERESULT");
                    int[] sqnum = new int[al.size()];
                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                            	
                    			HrLeaveApplyHdr hrLeaveApplyHdr = new HrLeaveApplyHdr();
                    			hrLeaveApplyHdr.setPLANT(plant);
                    			HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO = new HrLeaveApplyHdrDAO();
                    			List<HrLeaveApplyAttachment> hrLeaveApplyAttachmentList = new ArrayList<HrLeaveApplyAttachment>();
                            	
                            	String empid = (String) lineArr.get("EMPID");
                            	String repid = (String) lineArr.get("REPID"); 
                            	String lvtid = (String) lineArr.get("EMPDETID"); 
                            	String nodays = (String) lineArr.get("NUMBEROFDAYS");
                            	String lvduration = (String) lineArr.get("LEAVEDURATION");
                            	String leaveduration = "";
                            	if(lvduration.equalsIgnoreCase("Multiple days")) {
                            		leaveduration = "2";
                            	}else {
                            		leaveduration = "1";
                            	}
                            	String sdate = (String) lineArr.get("FROMDATE"); 
                            	String sdaystatus = (String) lineArr.get("FDAYSTATUS"); 
                            	String edate = (String) lineArr.get("TODATE");
                            	String edaystatus = (String) lineArr.get("TDAYSTATUS");
                            	
                            	hrLeaveApplyHdr.setEMPNOID(Integer.valueOf(empid));
            					hrLeaveApplyHdr.setLEAVETYPEID(Integer.valueOf(lvtid));
            					hrLeaveApplyHdr.setREPORT_INCHARGE_ID(Integer.valueOf(repid));
            					hrLeaveApplyHdr.setNUMBEROFDAYS(Double.valueOf(nodays));
            					if(leaveduration.equalsIgnoreCase("1")) {
            						hrLeaveApplyHdr.setFROM_DATE(sdate);
            						hrLeaveApplyHdr.setTO_DATE(sdate);
            						edate = sdate;
            					}else {
            						hrLeaveApplyHdr.setFROM_DATE(sdate);
            						hrLeaveApplyHdr.setTO_DATE(edate);
            					}
            					hrLeaveApplyHdr.setSTATUS("Pending");
            					hrLeaveApplyHdr.setCRBY(username);
            					hrLeaveApplyHdr.setCRAT(dateutils.getDateTime());
            					
            					int hdrid = hrLeaveApplyHdrService.addHrLeaveApplyHdr(hrLeaveApplyHdr);
            					
            					if(hdrid > 0) {
            						List<BlockingDates> BlockingDateslist = new ArrayList<BlockingDates>();	
            						
            						List<HrHolidayMst> holidayList = hrHolidayMstService.getAllHoliday(plant);
            						List<HrLeaveApplyDet> hrLeaveApplyDetList = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidfullday(plant,Integer.valueOf(empid));
            						List<BlockingDates> Blockingleavedays = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidTwohalfdays(plant, Integer.valueOf(empid));
            						int arrsize = holidayList.size() + hrLeaveApplyDetList.size()+Blockingleavedays.size();
            						ArrayList<String> datelist = new ArrayList<String>(arrsize);
            						for (HrHolidayMst HrHolidayMst : holidayList) {
            							datelist.add(HrHolidayMst.getHOLIDAY_DATE());
            						}	
            						for (HrLeaveApplyDet HrLeaveApplyDet : hrLeaveApplyDetList) {
            							datelist.add(HrLeaveApplyDet.getLEAVE_DATE());
            						}
            						
            						for (BlockingDates Blockingleave : Blockingleavedays) {
            							datelist.add(Blockingleave.getBLOCKING_DATE());
            						}
            						
            						BlockingDateslist = getdatebetween(sdate, edate);
            						
            						int datesize = BlockingDateslist.size();
            						int di = 0;
            						for (BlockingDates blockingDates : BlockingDateslist) {
            							if(!datelist.contains(blockingDates.getBLOCKING_DATE())) {
            								List<HrLeaveApplyDet> hrLeaveApplyDetListdate = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpiddate(plant, Integer.valueOf(empid), blockingDates.getBLOCKING_DATE());
            								if(hrLeaveApplyDetListdate.size() > 0) {
            									if(hrLeaveApplyDetListdate.size() == 1) {
            										HrLeaveApplyDet hrLeaveApplyDethalfday = hrLeaveApplyDetListdate.get(0);
            										if(hrLeaveApplyDethalfday.getPREPOSTLUNCH().equalsIgnoreCase("Morning")) {
            											HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
            											hrLeaveApplyDet.setPLANT(plant);
            											hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
            											hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
            											hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
            										    hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
            											hrLeaveApplyDet.setSTATUS("Pending");
            											hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
            											hrLeaveApplyDet.setCRBY(username);
            											hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
            										}
            										
            										if(hrLeaveApplyDethalfday.getPREPOSTLUNCH().equalsIgnoreCase("Afternoon")) {
            											HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
            											hrLeaveApplyDet.setPLANT(plant);
            											hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
            											hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
            											hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
            										    hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
            											hrLeaveApplyDet.setSTATUS("Pending");
            											hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
            											hrLeaveApplyDet.setCRBY(username);
            											hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
            										}
            										
            									}
            								}else {
            									
            									if(di == 0) {
            										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
            										hrLeaveApplyDet.setPLANT(plant);
            										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
            										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
            										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
            										if(sdaystatus.equalsIgnoreCase("Fullday")) {
            											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
            										}
            										if(sdaystatus.equalsIgnoreCase("Morning")) {
            											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
            										}
            										if(sdaystatus.equalsIgnoreCase("Afternoon")) {
            											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
            										}
            										hrLeaveApplyDet.setSTATUS("Pending");
            										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
            										hrLeaveApplyDet.setCRBY(username);
            										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
            									}else if(di == (datesize - 1)) {
            										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
            										hrLeaveApplyDet.setPLANT(plant);
            										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
            										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
            										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
            										if(edaystatus.equalsIgnoreCase("Fullday")) {
            											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
            										}
            										if(edaystatus.equalsIgnoreCase("Morning")) {
            											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Morning");
            										}
            										if(edaystatus.equalsIgnoreCase("Afternoon")) {
            											hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Halfday");
            											hrLeaveApplyDet.setPREPOSTLUNCH("Afternoon");
            										}
            										hrLeaveApplyDet.setSTATUS("Pending");
            										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
            										hrLeaveApplyDet.setCRBY(username);
            										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
            									}else {
            										HrLeaveApplyDet hrLeaveApplyDet = new HrLeaveApplyDet();
            										hrLeaveApplyDet.setPLANT(plant);
            										hrLeaveApplyDet.setEMPNOID(Integer.valueOf(empid));
            										hrLeaveApplyDet.setLEAVEHDRID(Integer.valueOf(hdrid));
            										hrLeaveApplyDet.setLEAVE_DATE(blockingDates.getBLOCKING_DATE());
            										hrLeaveApplyDet.setPREPOSTLUNCHTYPE("Fullday");
            										hrLeaveApplyDet.setPREPOSTLUNCH("Fullday");
            										hrLeaveApplyDet.setSTATUS("Pending");
            										hrLeaveApplyDet.setCRAT(dateutils.getDateTime());
            										hrLeaveApplyDet.setCRBY(username);
            										hrLeaveApplyDetService.addHrLeaveApplyDet(hrLeaveApplyDet);
            									}
            								}
            							}
            							
            							di++;
            						}
            						
            						
            						for(HrLeaveApplyAttachment leaveApplyAttachment:hrLeaveApplyAttachmentList) {
            							leaveApplyAttachment.setLEAVEAPPLYHDRID(hdrid);
            							hrLeaveApplyHdrDAO.addHrLeaveApplyAttachment(leaveApplyAttachment);
            						}
            						
            						EmployeeLeaveDET employeelavedet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, Integer.valueOf(lvtid));
            						HrLeaveType Hrleavetype = hrLeaveTypeDAO.getLeavetypeById(plant, employeelavedet.getLEAVETYPEID());
            						if(Hrleavetype.getISNOPAYLEAVE() == 0) {
            							double leavebal = employeelavedet.getLEAVEBALANCE() - Double.valueOf(nodays);
            							employeelavedet.setLEAVEBALANCE(leavebal);
            							employeeLeaveDetDAO.updateEmployeeLeavedet(employeelavedet, username);
            						}
            						
            						Hashtable htMovHis = new Hashtable();
            	 					htMovHis.clear();
            	 					htMovHis.put(IDBConstants.PLANT, plant);					
            	 					htMovHis.put("DIRTYPE", TransactionConstants.APPLY_LEAVE);	
            	 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
            	 					htMovHis.put("RECID", hdrid);
            	 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, hdrid);
            	 					htMovHis.put(IDBConstants.CREATED_BY, username);		
            	 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
            	 					htMovHis.put("REMARKS",hrLeaveApplyHdr.getEMPNOID()+","+hrLeaveApplyHdr.getLEAVETYPEID()+","+hrLeaveApplyHdr.getFROM_DATE()+","+hrLeaveApplyHdr.getTO_DATE()+","+hrLeaveApplyHdr.getNUMBEROFDAYS());
            	 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
                                    
            	 					flag = true;
            					} 
                            }
                            
                    }
            
                    

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_EMPLOYEERESULT");
                            flag = true;
                            result = "<font color=\"green\">Employee Leave Apply Data Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Employee Leave Apply Data </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("EMPLOYEERESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("EMPLOYEERESULT", result);
            response.sendRedirect("jsp/importEmployeeLeaveApply.jsp?");

    }

        
private boolean process_Wms_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;

		WmsTran tran = new WmsUploadCountSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
        
    private boolean process_Wms_Supplier_CountSheet(Map map) throws Exception {
            MLogger.log(1, this.getClass() + " process_Wms_Supplier_CountSheet()");
            boolean flag = false;

            WmsTran tran = new WmsUploadSupplerSheet();
            flag = tran.processWmsTran(map);
            MLogger.log(-1, this.getClass() + " process_Wms_Supplier_CountSheet()");
            return flag;
    }
    
    private boolean process_Wms_Customer_CountSheet(Map map) throws Exception {
            MLogger.log(1, this.getClass() + " process_Wms_Customer_CountSheet()");
            boolean flag = false;

            WmsTran tran = new WmsUploadCustomerSheet();
            flag = tran.processWmsTran(map);
            MLogger.log(-1, this.getClass() + " process_Wms_Customer_CountSheet()");
            return flag;
    }
  
    /***Created on Nov 13 2014,Bruhan,Description: To include Transfer Assignee master
    /* ************Modification History*********************************
	  
    */
    private boolean process_Wms_TransferAssignee_CountSheet(Map map) throws Exception {
        MLogger.log(1, this.getClass() + " process_Wms_TransferAssignee_CountSheet()");
        boolean flag = false;

        WmsTran tran = new WmsUploadTransferAssigneeSheet();
        flag = tran.processWmsTran(map);
        MLogger.log(-1, this.getClass() + " process_Wms_TransferAssignee_CountSheet()");
        return flag;
  }
    
    /***Created on Nov 13 2014,Bruhan,Description: To include Loan Assignee master
    /* ************Modification History*********************************
	  
    */
    private boolean process_Wms_LoanAssignee_CountSheet(Map map) throws Exception {
        MLogger.log(1, this.getClass() + " process_Wms_LoanAssignee_CountSheet()");
        boolean flag = false;

        WmsTran tran = new WmsUploadLoanAssigneeSheet();
        flag = tran.processWmsTran(map);
        MLogger.log(-1, this.getClass() + " process_Wms_LoanAssignee_CountSheet()");
        return flag;
  }
    
    /***Created on Nov 13 2014,Bruhan,Description: To include Employee master
    /* ************Modification History*********************************
	  
    */
    private boolean process_Wms_Employee_CountSheet(Map map) throws Exception {
        MLogger.log(1, this.getClass() + " process_Wms_Employee_CountSheet()");
        boolean flag = false;

        WmsTran tran = new WmsUploadEmployeeSheet();////////////
        flag = tran.processWmsTran(map); 
        MLogger.log(-1, this.getClass() + " process_Wms_Employee_CountSheet()");
        return flag;
  }
    
    private boolean process_Wms_Kitting_CountSheet(Map map) throws Exception {
        MLogger.log(1, this.getClass() + " process_Wms_Kitting_CountSheet()");
        boolean flag = false;

        WmsTran tran = new WmsUploadKittingSheet();////////////  
        flag = tran.processWmsTran(map); 
        MLogger.log(-1, this.getClass() + " process_Wms_Kitting_CountSheet()");
        return flag;
  }
    
    private boolean process_Wms_SupplierDiscount_CountSheet(Map map) throws Exception {
        MLogger.log(1, this.getClass() + " process_Wms_SupplierDiscount_CountSheet()");
        boolean flag = false;

        WmsTran tran = new WmsUploadSupplierDiscountSheet();////////////
        flag = tran.processWmsTran(map); 
        MLogger.log(-1, this.getClass() + " process_Wms_SupplierDiscount_CountSheet()");
        return flag;
  }
    
    private boolean process_Wms_CustomerDiscount_CountSheet(Map map) throws Exception {
        MLogger.log(1, this.getClass() + " process_Wms_CustomerDiscount_CountSheet()");
        boolean flag = false;

        WmsTran tran = new WmsUploadCustomerDiscountSheet();////////////
        flag = tran.processWmsTran(map); 
        MLogger.log(-1, this.getClass() + " process_Wms_CustomerDiscount_CountSheet()");
        return flag;
  }
    
    private boolean process_Wms_StockTakeCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_StockTakeSheet()");
		boolean flag = false;
		WmsTran tran = new WmsUploadStockTakeSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " process_Wms_StockTakeSheet()");
		return flag;
	}

    
    public int checkempcode(String empcode){
    	char ch1 = empcode.charAt(0);
    	if(ch1 == 'E') {
    		String empvalue = empcode.substring(1);
    		if(StringUtils.isNumeric(empvalue)) {
    			return Integer.valueOf(empvalue);
    		}else {
    			return 0;
    		}
    	}else {
    		return 0;
    	}
    }
    
	public List<BlockingDates> getdatebetween(String str_date,String end_date) throws ParseException{
		List<Date> dates = new ArrayList<Date>();
		List<BlockingDates> BetweenDateslist = new ArrayList<BlockingDates>();	
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date  startDate = (Date)formatter.parse(str_date); 
		Date  endDate = (Date)formatter.parse(end_date);
		long interval = 24*1000 * 60 * 60; // 1 hour in millis
		long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
		long curTime = startDate.getTime();
		while (curTime <= endTime) {
		    dates.add(new Date(curTime));
		    curTime += interval;
		}
		for(int i=0;i<dates.size();i++){
		    Date lDate =(Date)dates.get(i);
		    String ds = formatter.format(lDate);    
		    BlockingDates blockingDates = new BlockingDates();
		    blockingDates.setBLOCKING_DATE(ds);
		    BetweenDateslist.add(blockingDates);
		}
		return BetweenDateslist;
	}
}
