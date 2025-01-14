package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.SupplierCreditDAO;
import com.track.dao.TaxReturnDAO;
import com.track.dao.TaxReturnFilingDAO;
import com.track.dao.TaxSettingDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.FinTaxDetailedSummaryPojo;
import com.track.db.object.FinTaxLiabilityReportPojo;
import com.track.db.object.InvPaymentAttachment;
import com.track.db.object.TaxReturnFillAdjust;
import com.track.db.object.TaxReturnFillDet;
import com.track.db.object.TaxReturnFillHdr;
import com.track.db.object.TaxReturnPaymentDet;
import com.track.db.object.TaxReturnPaymentHdr;
import com.track.db.object.TaxReturnTransactionSummary;
import com.track.db.util.BillPaymentUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TaxSettingUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@WebServlet("/TaxReturnFiling")
public class TaxReturnFiling  extends HttpServlet implements IMLogger  {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.TaxSettingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TaxSettingServlet_PRINTPLANTMASTERINFO;
	String action = "";
	String reportingperiod="";
	private TaxSettingUtil taxSettingUtil=new TaxSettingUtil();
	DateUtils dateutils = new DateUtils();
	MovHisDAO movHisDao = new MovHisDAO();
	
	Double GlobalAmount=0.00;
	Double GlobalTaxAmount=0.00;
	Double GlobalFinalTaxAmount1=0.00;
	Double GlobalFinalTaxAmount2=0.00;
	Double GlobalFinalTaxAmount3=0.00;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		action = StrUtils.fString(request.getParameter("action")).trim();
		reportingperiod= StrUtils.fString(request.getParameter("reportingperiod")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		List<TaxReturnFillDet> taxReturnDetailList=new ArrayList<>();
		 Numbers numberUtil=new Numbers();
		int scale=2;
		RoundingMode rm=RoundingMode.HALF_UP;
		if(action.equalsIgnoreCase("generateTax")) {
			//System.out.println("Reportingperiod:"+reportingperiod);
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			LocalDate datetime=null;
			LocalDate plusMonth=null;
			try {
				
				List<TaxReturnFillHdr> taxReturnHdr =  taxReturnDAO.getAllTaxReturnHdr(plant);
				if(taxReturnHdr.size()>0)
				{
					int taxRetid=taxReturnDAO.getMaxId(plant);
					if(taxRetid>0)
					{
						TaxReturnFillHdr taxRtHdr=taxReturnDAO.getTaxReturnHdrById(plant, Integer.toString(taxRetid));
						if(taxRtHdr.getSTATUS().equalsIgnoreCase("Unfiled"))
						{
							throw new Exception("pending");
						}
						String lastToDate=taxReturnDAO.getTODate(plant, taxRetid);
						datetime=LocalDate.parse(lastToDate,datePattern).plusDays(1);
					}
				}
				else
				{
					Map<String,String> taxSettings =  taxReturnDAO.getTaxSetting(ht);
					String taxReturnFrom=taxSettings.get("RETURNFROM");
					datetime = LocalDate.parse(taxReturnFrom, datePattern);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage().equalsIgnoreCase("pending"))
				{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			        response.getWriter().write("pending");
			        response.flushBuffer();
					return;
				}	
				else
				{
					e.printStackTrace();
				}
					
			}
			if(reportingperiod.equalsIgnoreCase("Monthly")){
				plusMonth=datetime.plusMonths(1).minusDays(1);
			}else if(reportingperiod.equalsIgnoreCase("Quarterly")){
				plusMonth=datetime.plusMonths(3).minusDays(1);
			}else if(reportingperiod.equalsIgnoreCase("Half-Yearly")){
				plusMonth=datetime.plusMonths(6).minusDays(1);
			}else if(reportingperiod.equalsIgnoreCase("Yearly")){
				plusMonth=datetime.plusMonths(12).minusDays(1);
			}
			else if(reportingperiod.equalsIgnoreCase("Custom"))
			{
				String vatenddate= StrUtils.fString(request.getParameter("enddate")).trim();
				plusMonth = LocalDate.parse(vatenddate, datePattern);
			}
				try {
					Map plantMst=taxReturnDAO.getPlantMst(ht);
					//String taxReturnUntil=datePattern.format(plusMonth);
					ht.put("FROM", datetime.toString());
					ht.put("TO", plusMonth.toString());
					List<Map<String,String>> salesInvoiceList=taxReturnDAO.getSalesInvoice(ht);
					TaxReturnFillHdr taxRetHdr=new TaxReturnFillHdr();
					taxRetHdr.setPLANT(plant);
					taxRetHdr.setCOUNTRY_CODE("UAE");
					taxRetHdr.setTAX_BASIS(plantMst.get("REPROTSBASIS").toString());
					taxRetHdr.setFROM_DATE(datetime.format(datePattern));
					taxRetHdr.setTO_DATE(plusMonth.format(datePattern));
					taxRetHdr.setPAYMENTDUE_ON(plusMonth.plusMonths(1).format(datePattern));
					taxRetHdr.setREPORTING_PERIOD(reportingperiod);
					taxRetHdr.setSTATUS("Unfiled");
					taxRetHdr.setTAXPREVIOUSINCLUDED(false);
					int taxReturnHdrId=taxReturnDAO.addTaxReturnHdr(taxRetHdr);
					
					if(taxReturnHdrId>0)
					{
						for(Map<String,String> sales:salesInvoiceList)
						{
							TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
							taxRetDet.setPLANT(plant);
							taxRetDet.setTAXHDR_ID(taxReturnHdrId);
							taxRetDet.setCOUNTRY_CODE("UAE");
							System.out.println("Sales tax:"+sales.get("TAX_TYPE"));
							String taxtype=sales.get("TAX_TYPE");
							String filteredTaxType = null;
							if(!taxtype.isEmpty())
							{
								taxtype=taxtype.replace("[",":");
								System.out.println("TT:"+taxtype);
								String type[]=taxtype.split(":");
								
								if(type[0].contains("("))
								{
									filteredTaxType=type[0].substring(0, type[0].indexOf("("));
									System.out.println("TAx Type:"+filteredTaxType);
								}
								else
								{
									filteredTaxType=type[0];
								}
								
								
							
							if(filteredTaxType.equalsIgnoreCase("ZERO RATE"))
							{
								taxRetDet.setBOX("4");
							}
							else if(filteredTaxType.equalsIgnoreCase("EXEMPT"))
							{
								taxRetDet.setBOX("5");
							}
							else
							{
								if(sales.get("SALES_LOCATION")==null)
								{
									sales.put("SALES_LOCATION", "");
								}
								if(sales.get("SALES_LOCATION").equalsIgnoreCase("Abu Dhabi"))
									taxRetDet.setBOX("1a");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Dubai"))
									taxRetDet.setBOX("1b");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Sharjah"))
									taxRetDet.setBOX("1c");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Ajman"))
									taxRetDet.setBOX("1d");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Umm Al Quwain"))
									taxRetDet.setBOX("1e");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Ras Al Khaimah"))
									taxRetDet.setBOX("1f");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Fujairah"))
									taxRetDet.setBOX("1g");
								else
									taxRetDet.setBOX("unknown");
							}
							taxRetDet.setTRANSACTION_ID(sales.get("INVOICE"));
							Double salestaxableamount=Double.parseDouble(sales.get("AMOUNT"))+Double.parseDouble(sales.get("SHIPPINGCOST"));
							taxRetDet.setTAXABLE_AMOUNT(salestaxableamount);
							if(sales.get("TAXAMOUNT")==null)
							{
								sales.put("TAXAMOUNT", "0.00");
							}
							taxRetDet.setTAX_AMOUNT(Double.parseDouble(sales.get("TAXAMOUNT")));
							taxRetDet.setDATE(sales.get("INVOICE_DATE"));
							taxRetDet.setTRANSACTION_TYPE("Invoice");
							taxRetDet.setISTAXPREVIOUS(false);
						
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
						}
							
							
								
						}
					}
					String cond="TAXTREATMENT IN ('NON GCC','GCC VAT Registered','GCC NON VAT Registered')";
					String extracond="REVERSECHARGE='1'";
					List<Map<String,String>> billGCCNONGCCList=taxReturnDAO.getBillBYTaxTreamentType(ht, cond,extracond);
					billGCCNONGCCList.forEach(bill->{
						TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
						taxRetDet.setPLANT(plant);
						taxRetDet.setTAXHDR_ID(taxReturnHdrId);
						taxRetDet.setCOUNTRY_CODE("UAE");
						taxRetDet.setBOX("3");
						taxRetDet.setTRANSACTION_ID(bill.get("BILL"));
						Double billtaxableamount=Double.parseDouble(bill.get("SUB_TOTAL"))+Double.parseDouble(bill.get("SHIPPINGCOST"));
						taxRetDet.setTAXABLE_AMOUNT(billtaxableamount);
						if(bill.get("TAXAMOUNT")==null)
						{
							bill.put("TAXAMOUNT", "0.00");
						}
						taxRetDet.setTAX_AMOUNT(Double.parseDouble(bill.get("TAXAMOUNT")));
						taxRetDet.setDATE(bill.get("BILL_DATE"));
						taxRetDet.setTRANSACTION_TYPE("Bill");
						taxRetDet.setISTAXPREVIOUS(false);
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
					});
					List<Map<String,String>> expenseList=taxReturnDAO.getExpenseBYTaxDateFilter(ht);
					String[] taxtreatmentgccnongcc = new String[]{"NON GCC","GCC VAT Registered","GCC NON VAT Registered"};
					expenseList.forEach(expense->{
						boolean gccOrNonGcc=false;
							try {
								if(expense.get("TAXTREATMENT")==null)
								{
									gccOrNonGcc=false;
								}
								else
								{
									gccOrNonGcc = Arrays.stream(taxtreatmentgccnongcc).anyMatch(expense.get("TAXTREATMENT")::equals);
								}
								if(gccOrNonGcc)
								{
									int reversecharge = 0,goodsimport=0;
									if(!expense.get("REVERSECHARGE").isEmpty())
									{
										reversecharge=Integer.parseInt(expense.get("REVERSECHARGE"));
										
									}
									if(!expense.get("GOODSIMPORT").isEmpty())
									{
										goodsimport=Integer.parseInt(expense.get("GOODSIMPORT"));
										
									}
									if(reversecharge>0)
									{
										TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
										taxRetDet.setPLANT(plant);
										taxRetDet.setTAXHDR_ID(taxReturnHdrId);
										taxRetDet.setCOUNTRY_CODE("UAE");
										taxRetDet.setBOX("3");
										taxRetDet.setTRANSACTION_ID(expense.get("ID"));
										taxRetDet.setTAXABLE_AMOUNT(Double.parseDouble(expense.get("SUB_TOTAL")));
										if(expense.get("TAXAMOUNT")==null)
										{
											expense.put("TAXAMOUNT", "0.00");
										}
										taxRetDet.setTAX_AMOUNT(Double.parseDouble(expense.get("TAXAMOUNT")));
										taxRetDet.setDATE(expense.get("EXPENSES_DATE"));
										taxRetDet.setTRANSACTION_TYPE("Expense");
										taxRetDet.setISTAXPREVIOUS(false);
										try {
											taxReturnDAO.addTaxReturnDet(taxRetDet);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										taxReturnDetailList.add(taxRetDet);
									}
									else if(goodsimport>0)
									{
										TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
										taxRetDet.setPLANT(plant);
										taxRetDet.setTAXHDR_ID(taxReturnHdrId);
										taxRetDet.setCOUNTRY_CODE("UAE");
										taxRetDet.setBOX("6");
										taxRetDet.setTRANSACTION_ID(expense.get("ID"));
										taxRetDet.setTAXABLE_AMOUNT(Double.parseDouble(expense.get("SUB_TOTAL")));
										if(expense.get("TAXAMOUNT")==null)
										{
											expense.put("TAXAMOUNT", "0.00");
										}
										taxRetDet.setTAX_AMOUNT(Double.parseDouble(expense.get("TAXAMOUNT")));
										taxRetDet.setDATE(expense.get("EXPENSES_DATE"));
										taxRetDet.setTRANSACTION_TYPE("Expense");
										taxRetDet.setISTAXPREVIOUS(false);
										try {
											taxReturnDAO.addTaxReturnDet(taxRetDet);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										taxReturnDetailList.add(taxRetDet);
									}
								}
								else
								{
									TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
									taxRetDet.setPLANT(plant);
									taxRetDet.setTAXHDR_ID(taxReturnHdrId);
									taxRetDet.setCOUNTRY_CODE("UAE");
									taxRetDet.setBOX("9");
									taxRetDet.setTRANSACTION_ID(expense.get("ID"));
									taxRetDet.setTAXABLE_AMOUNT(Double.parseDouble(expense.get("SUB_TOTAL")));
									if(expense.get("TAXAMOUNT")==null)
									{
										expense.put("TAXAMOUNT", "0.00");
									}
									taxRetDet.setTAX_AMOUNT(Double.parseDouble(expense.get("TAXAMOUNT")));
									taxRetDet.setDATE(expense.get("EXPENSES_DATE"));
									taxRetDet.setTRANSACTION_TYPE("Expense");
									taxRetDet.setISTAXPREVIOUS(false);
									try {
										taxReturnDAO.addTaxReturnDet(taxRetDet);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									taxReturnDetailList.add(taxRetDet);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					});
					
					extracond="GOODSIMPORT='1'";
					List<Map<String,String>> billGCCNONGCCImportList=taxReturnDAO.getBillBYTaxTreamentType(ht, cond,extracond);
					billGCCNONGCCImportList.forEach(bill->{
						TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
						taxRetDet.setPLANT(plant);
						taxRetDet.setTAXHDR_ID(taxReturnHdrId);
						taxRetDet.setCOUNTRY_CODE("UAE");
						taxRetDet.setBOX("6");
						taxRetDet.setTRANSACTION_ID(bill.get("BILL"));
						Double billtaxableamount=Double.parseDouble(bill.get("SUB_TOTAL"))+Double.parseDouble(bill.get("SHIPPINGCOST"));
						taxRetDet.setTAXABLE_AMOUNT(billtaxableamount);
						if(bill.get("TAXAMOUNT")==null)
						{
							bill.put("TAXAMOUNT", "0.00");
						}
						taxRetDet.setTAX_AMOUNT(Double.parseDouble(bill.get("TAXAMOUNT")));
						taxRetDet.setDATE(bill.get("BILL_DATE"));
						taxRetDet.setTRANSACTION_TYPE("Bill");
						taxRetDet.setISTAXPREVIOUS(false);
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
					});
					String exclude3_6_cond="TAXTREATMENT NOT IN ('NON GCC','GCC VAT Registered','GCC NON VAT Registered')";
					List<Map<String,String>> billExclude3_6=taxReturnDAO.getBillBYTaxTreamentType(ht, exclude3_6_cond,"");
					billExclude3_6.forEach(bill->{
						TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
						taxRetDet.setPLANT(plant);
						taxRetDet.setTAXHDR_ID(taxReturnHdrId);
						taxRetDet.setCOUNTRY_CODE("UAE");
						taxRetDet.setBOX("9");
						taxRetDet.setTRANSACTION_ID(bill.get("BILL"));
						Double billtaxableamount=Double.parseDouble(bill.get("SUB_TOTAL"))+Double.parseDouble(bill.get("SHIPPINGCOST"));
						taxRetDet.setTAXABLE_AMOUNT(billtaxableamount);
						if(bill.get("TAXAMOUNT")==null)
						{
							bill.put("TAXAMOUNT", "0.00");
						}
						taxRetDet.setTAX_AMOUNT(Double.parseDouble(bill.get("TAXAMOUNT")));
						taxRetDet.setDATE(bill.get("BILL_DATE"));
						taxRetDet.setTRANSACTION_TYPE("Bill");
						taxRetDet.setISTAXPREVIOUS(false);
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
					});
					System.out.println("From:"+datetime.toString()+"    To:"+plusMonth.toString());
					Double TOTAL_TAXPAYABLE=0.00;
					Double totalSalesTax=0.00;
					Double totalExpenseTax=0.00;
					for(TaxReturnFillDet taxDet:taxReturnDetailList)
					{
						if(taxDet.getBOX().equalsIgnoreCase("9"))
						{
							totalExpenseTax+=taxDet.getTAX_AMOUNT();
						}
						else if(taxDet.getBOX().equalsIgnoreCase("3") || taxDet.getBOX().equalsIgnoreCase("6") || taxDet.getBOX().equalsIgnoreCase("7"))
						{
							totalSalesTax+=taxDet.getTAX_AMOUNT();
							totalExpenseTax+=taxDet.getTAX_AMOUNT();
						}
						else
						{
							totalSalesTax+=taxDet.getTAX_AMOUNT();
						}
						//TOTAL_TAXPAYABLE+=taxDet.getTAX_AMOUNT();
						
					}
					TOTAL_TAXPAYABLE=totalSalesTax-totalExpenseTax;
					String updateItems="TOTAL_TAXPAYABLE="+numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm);
					taxReturnDAO.updateTaxHeader(plant, String.valueOf(taxReturnHdrId), updateItems);
					taxReturnDAO.updateTaxBalanceDue(plant, String.valueOf(taxReturnHdrId), numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm));
					//update tax_status in Bill,Expense and Invoice Tables
					List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDetailList.stream().collect(Collectors.collectingAndThen(Collectors.toMap(TaxReturnFillDet::getTRANSACTION_ID, Function.identity(), (left, right) -> {
		                left.setTAXABLE_AMOUNT(left.getTAXABLE_AMOUNT()+right.getTAXABLE_AMOUNT());
		                return left;
		            }), m -> new ArrayList<>(m.values())));
					taxReturnDetailListMerged.forEach(taxMergeDet->{
						String id=taxMergeDet.getTRANSACTION_ID();
						String taxtype=taxMergeDet.getTRANSACTION_TYPE();
						String status="Tax Generated";
						try {
							taxReturnDAO.updateTaxItemStatus(plant, taxtype,id,status);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
					
					
					TaxReturnFillHdr taxReturnHdr=taxReturnDAO.getTaxReturnHdrById(plant, Integer.toString(taxReturnHdrId));
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.TAX_FILE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(taxReturnHdr.getTO_DATE()));														
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, taxReturnHdrId);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",taxReturnHdr.getCOUNTRY_CODE()+""+taxReturnHdr.getID());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		else if(action.equalsIgnoreCase("generateTaxForSG")) {

			//System.out.println("Reportingperiod:"+reportingperiod);
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			LocalDate datetime=null;
			LocalDate plusMonth=null;
			try {
				
				List<TaxReturnFillHdr> taxReturnHdr =  taxReturnDAO.getAllTaxReturnHdr(plant);
				if(taxReturnHdr.size()>0)
				{
					int taxRetid=taxReturnDAO.getMaxId(plant);
					if(taxRetid>0)
					{
						TaxReturnFillHdr taxRtHdr=taxReturnDAO.getTaxReturnHdrById(plant, Integer.toString(taxRetid));
						if(taxRtHdr.getSTATUS().equalsIgnoreCase("Unfiled"))
						{
							throw new Exception("pending");
						}
						String lastToDate=taxReturnDAO.getTODate(plant, taxRetid);
						datetime=LocalDate.parse(lastToDate,datePattern).plusDays(1);
					}
				}
				else
				{
					Map<String,String> taxSettings =  taxReturnDAO.getTaxSetting(ht);
					String taxReturnFrom=taxSettings.get("RETURNFROM");
					datetime = LocalDate.parse(taxReturnFrom, datePattern);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(e.getMessage().equalsIgnoreCase("pending"))
				{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			        response.getWriter().write("pending");
			        response.flushBuffer();
					return;
				}	
				else
				{
					e.printStackTrace();
				}
					
			}
			
			
			if(reportingperiod.equalsIgnoreCase("Monthly")){
				plusMonth=datetime.plusMonths(1).minusDays(1);
			}else if(reportingperiod.equalsIgnoreCase("Quarterly")){
				plusMonth=datetime.plusMonths(3).minusDays(1);
			}else if(reportingperiod.equalsIgnoreCase("Half-Yearly")){
				plusMonth=datetime.plusMonths(6).minusDays(1);
			}else if(reportingperiod.equalsIgnoreCase("Yearly")){
				plusMonth=datetime.plusMonths(12).minusDays(1);
			}
			else if(reportingperiod.equalsIgnoreCase("Custom"))
			{
				String vatenddate= StrUtils.fString(request.getParameter("enddate")).trim();
				plusMonth = LocalDate.parse(vatenddate, datePattern);
			}
			
			
			
			
			
				try {
					
					 String COUNTRYCODE="";
					 List viewlistQry = plantmstutil.getPlantMstDetails(plant);
					 for (int i = 0; i < viewlistQry.size(); i++) {
					     Map map = (Map) viewlistQry.get(i);
					     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
					 }
					
					Map plantMst=taxReturnDAO.getPlantMst(ht);
					ht.put("FROM", datetime.toString());
					ht.put("TO", plusMonth.toString());
					
					TaxReturnFillHdr taxRetHdr=new TaxReturnFillHdr();
					taxRetHdr.setPLANT(plant);
					taxRetHdr.setCOUNTRY_CODE(COUNTRYCODE);
					taxRetHdr.setTAX_BASIS(plantMst.get("REPROTSBASIS").toString());
					taxRetHdr.setFROM_DATE(datetime.format(datePattern));
					taxRetHdr.setTO_DATE(plusMonth.format(datePattern));
					taxRetHdr.setPAYMENTDUE_ON(plusMonth.plusMonths(1).format(datePattern));
					taxRetHdr.setREPORTING_PERIOD(reportingperiod);
					taxRetHdr.setSTATUS("Unfiled");
					taxRetHdr.setTAXPREVIOUSINCLUDED(false);
					int taxReturnHdrId=taxReturnDAO.addTaxReturnHdr(taxRetHdr);
					
					if(taxReturnHdrId>0)
					{
						
						List<Map<String,String>> salesInvoiceList=taxReturnDAO.getSalesInvoiceForTaxFill(ht);
						for(Map<String,String> sales:salesInvoiceList)
						{
							TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
							taxRetDet.setPLANT(plant);
							taxRetDet.setTAXHDR_ID(taxReturnHdrId);
							taxRetDet.setCOUNTRY_CODE(COUNTRYCODE);
							String taxtype=sales.get("TAXBOX");
							taxRetDet.setBOX(taxtype);
							taxRetDet.setTRANSACTION_ID(sales.get("INVOICE"));
							
							double orderdiscount = Double.valueOf(sales.get("ORDER_DISCOUNT"));
							String orderdiscounttype = sales.get("ORDERDISCOUNTTYPE");
							int orderdiscounttax = Integer.valueOf(sales.get("ISORDERDISCOUNTTAX"));
							
							double discount = Double.valueOf(sales.get("DISCOUNT"));
							String discounttype = sales.get("DISCOUNT_TYPE");
							int discounttax = Integer.valueOf(sales.get("ISDISCOUNTTAX"));
							
							double shippingcost = Double.valueOf(sales.get("SHIPPINGCOST"));
							int shippingtax = Integer.valueOf(sales.get("ISSHIPPINGTAX"));

							double salestaxableamount = Double.parseDouble(sales.get("SUB_TOTAL"));
							double subtotal = Double.parseDouble(sales.get("SUB_TOTAL")); 
							
							if(orderdiscounttax == 0){
								if(orderdiscounttype.equalsIgnoreCase("%")){
									salestaxableamount = salestaxableamount - ((subtotal/100)*orderdiscount);
								}else {
									salestaxableamount = salestaxableamount - orderdiscount;
								}
							}
							
							if(discounttax == 0){
								if(discounttype.equalsIgnoreCase("%")){
									salestaxableamount = salestaxableamount - ((subtotal/100)*discount);
								}else {
									salestaxableamount = salestaxableamount - discount;
								}
							}
							
							if(shippingtax == 0){
								salestaxableamount = salestaxableamount + shippingcost;
							}
							
							taxRetDet.setTAXABLE_AMOUNT(salestaxableamount);
							if(sales.get("TAXAMOUNT")==null)
							{
								sales.put("TAXAMOUNT", "0.00");
							}
							taxRetDet.setTAX_AMOUNT(Double.parseDouble(sales.get("TAXAMOUNT")));
							taxRetDet.setDATE(sales.get("INVOICE_DATE"));
							taxRetDet.setTRANSACTION_TYPE("Invoice");
							taxRetDet.setISTAXPREVIOUS(false);
						
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
						}
						
						List<Map<String,String>> purchaseBillList=taxReturnDAO.getPurchaseBillForTaxFill(ht);
						for(Map<String,String> bill:purchaseBillList)
						{
							TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
							taxRetDet.setPLANT(plant);
							taxRetDet.setTAXHDR_ID(taxReturnHdrId);
							taxRetDet.setCOUNTRY_CODE(COUNTRYCODE);
							String taxtype=bill.get("TAXBOX");
							taxRetDet.setBOX(taxtype);
							taxRetDet.setTRANSACTION_ID(bill.get("BILL"));
							
							double orderdiscount = Double.valueOf(bill.get("ORDER_DISCOUNT"));
							String orderdiscounttype = bill.get("ORDERDISCOUNTTYPE");
							int orderdiscounttax = Integer.valueOf(bill.get("ISORDERDISCOUNTTAX"));
							
							double discount = Double.valueOf(bill.get("DISCOUNT"));
							String discounttype = bill.get("DISCOUNT_TYPE");
							int discounttax = Integer.valueOf(bill.get("ISDISCOUNTTAX"));
							
							double shippingcost = Double.valueOf(bill.get("SHIPPINGCOST"));
							int shippingtax = Integer.valueOf(bill.get("ISSHIPPINGTAXABLE"));

							double purchasetaxableamount = Double.parseDouble(bill.get("SUB_TOTAL"));
							double subtotal = Double.parseDouble(bill.get("SUB_TOTAL")); 
							
							if(orderdiscounttax == 0){
								if(orderdiscounttype.equalsIgnoreCase("%")){
									purchasetaxableamount = purchasetaxableamount - ((subtotal/100)*orderdiscount);
								}else {
									purchasetaxableamount = purchasetaxableamount - orderdiscount;
								}
							}
							
							if(discounttax == 0){
								if(discounttype.equalsIgnoreCase("%")){
									purchasetaxableamount = purchasetaxableamount - ((subtotal/100)*discount);
								}else {
									purchasetaxableamount = purchasetaxableamount - discount;
								}
							}
							
							if(shippingtax == 0){
								purchasetaxableamount = purchasetaxableamount + shippingcost;
							}
							
							taxRetDet.setTAXABLE_AMOUNT(purchasetaxableamount);
							if(bill.get("TAXAMOUNT")==null)
							{
								bill.put("TAXAMOUNT", "0.00");
							}
							taxRetDet.setTAX_AMOUNT(Double.parseDouble(bill.get("TAXAMOUNT")));
							taxRetDet.setDATE(bill.get("BILL_DATE"));
							taxRetDet.setTRANSACTION_TYPE("Bill");
							taxRetDet.setISTAXPREVIOUS(false);
						
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
						}
						
						
						List<Map<String,String>> expenseList=taxReturnDAO.getExpenseForTaxFill(ht);
						for(Map<String,String> expense:expenseList)
						{
							TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
							taxRetDet.setPLANT(plant);
							taxRetDet.setTAXHDR_ID(taxReturnHdrId);
							taxRetDet.setCOUNTRY_CODE(COUNTRYCODE);
							String taxtype=expense.get("TAXBOX");
							taxRetDet.setBOX(taxtype);
							taxRetDet.setTRANSACTION_ID(expense.get("ID"));

							double expensetaxableamount = Double.parseDouble(expense.get("SUB_TOTAL"));
							
							taxRetDet.setTAXABLE_AMOUNT(expensetaxableamount);
							if(expense.get("TAXAMOUNT")==null)
							{
								expense.put("TAXAMOUNT", "0.00");
							}
							taxRetDet.setTAX_AMOUNT(Double.parseDouble(expense.get("TAXAMOUNT")));
							taxRetDet.setDATE(expense.get("EXPENSES_DATE"));
							taxRetDet.setTRANSACTION_TYPE("Expense");
							taxRetDet.setISTAXPREVIOUS(false);
						
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
						}				
				}
						
					
					System.out.println("From:"+datetime.toString()+"    To:"+plusMonth.toString());
					Double TOTAL_TAXPAYABLE=0.00;
					Double totalSalesTax=0.00;
					Double totalExpenseTax=0.00;
					for(TaxReturnFillDet taxDet:taxReturnDetailList)
					{
						if(taxDet.getBOX().equalsIgnoreCase("1"))
						{
							totalSalesTax+=taxDet.getTAX_AMOUNT();
						}
						else if(taxDet.getBOX().equalsIgnoreCase("5"))
						{
							totalExpenseTax+=taxDet.getTAX_AMOUNT();
						}
						else
						{
							totalSalesTax+=taxDet.getTAX_AMOUNT();
						}
						
					}
					TOTAL_TAXPAYABLE=totalSalesTax-totalExpenseTax;
					String updateItems="TOTAL_TAXPAYABLE="+numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm);
					taxReturnDAO.updateTaxHeader(plant, String.valueOf(taxReturnHdrId), updateItems);
					taxReturnDAO.updateTaxBalanceDue(plant, String.valueOf(taxReturnHdrId), numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm));
					//update tax_status in Bill,Expense and Invoice Tables
					List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDetailList.stream().collect(Collectors.collectingAndThen(Collectors.toMap(TaxReturnFillDet::getTRANSACTION_ID, Function.identity(), (left, right) -> {
		                left.setTAXABLE_AMOUNT(left.getTAXABLE_AMOUNT()+right.getTAXABLE_AMOUNT());
		                return left;
		            }), m -> new ArrayList<>(m.values())));
					taxReturnDetailListMerged.forEach(taxMergeDet->{
						String id=taxMergeDet.getTRANSACTION_ID();
						String taxtype=taxMergeDet.getTRANSACTION_TYPE();
						String status="Tax Generated";
						try {
							taxReturnDAO.updateTaxItemStatus(plant, taxtype,id,status);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
					
					
					TaxReturnFillHdr taxReturnHdr=taxReturnDAO.getTaxReturnHdrById(plant, Integer.toString(taxReturnHdrId));
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.TAX_FILE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(taxReturnHdr.getTO_DATE()));														
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, taxReturnHdrId);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",taxReturnHdr.getCOUNTRY_CODE()+""+taxReturnHdr.getID());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else if(action.equalsIgnoreCase("payTax")) {
			String paymentheaderid= StrUtils.fString(request.getParameter("modaltaxpayheaderid")).trim();
			String paidthr= StrUtils.fString(request.getParameter("paidthr")).trim();
			String amountpaid= StrUtils.fString(request.getParameter("amountpaid")).trim();
			String taxreturndate= StrUtils.fString(request.getParameter("modaltaxreturndate")).trim();
			String paymentdate= StrUtils.fString(request.getParameter("paymentdate")).trim();
			String referencenum= StrUtils.fString(request.getParameter("referencenum")).trim();
			String desc= StrUtils.fString(request.getParameter("desc")).trim();
			TaxReturnPaymentDet taxPaymentDet=new TaxReturnPaymentDet();
			taxPaymentDet.setPAYMENTHDR_ID(Integer.parseInt(paymentheaderid));
			taxPaymentDet.setPAID_THROUGH(paidthr);
			taxPaymentDet.setAMOUNT_PAID(Double.parseDouble(amountpaid));
			taxPaymentDet.setDATE(paymentdate);
			taxPaymentDet.setREFERENCE(referencenum);
			taxPaymentDet.setDESCRIPTION(desc);
			taxPaymentDet.setPLANT(plant);
			taxPaymentDet.setTAX_RETURN(taxreturndate);
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			int updateCount=0;
			try {
				updateCount=taxReturnDAO.addTaxReturnPaymentDet(taxPaymentDet);
				TaxReturnFillHdr taxRtHdr=taxReturnDAO.getTaxReturnHdrById(plant, paymentheaderid);
				Double balanceDue=taxRtHdr.getBALANCEDUE()-(Double.parseDouble(amountpaid));
				taxReturnDAO.updateTaxBalanceDue(plant, String.valueOf(paymentheaderid), String.valueOf(balanceDue));
				TaxReturnFillHdr taxReturnHdr=taxReturnDAO.getTaxReturnHdrById(plant, paymentheaderid);
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.CREATE_TAXRETURN_PAYMENT);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(taxReturnHdr.getTO_DATE()));														
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, paymentheaderid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",taxReturnHdr.getCOUNTRY_CODE()+""+taxReturnHdr.getID());
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject result=new JSONObject();
			result.put("STATUS", "SUCCESS");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if(action.equalsIgnoreCase("deleteTax")) {
			String paymentheaderid= StrUtils.fString(request.getParameter("paymentid")).trim();
			String taxheaderid= StrUtils.fString(request.getParameter("taxid")).trim();
			String amountpaid= StrUtils.fString(request.getParameter("amount")).trim();
			
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			int updateCount=0;
			try {
				updateCount=taxReturnDAO.deletePaymentById(plant, paymentheaderid);
				TaxReturnFillHdr taxRtHdr=taxReturnDAO.getTaxReturnHdrById(plant, taxheaderid);
				Double balanceDue=taxRtHdr.getBALANCEDUE()+(Double.parseDouble(amountpaid));
				taxReturnDAO.updateTaxBalanceDue(plant, taxheaderid, String.valueOf(balanceDue));
				TaxReturnFillHdr taxReturnHdr=taxReturnDAO.getTaxReturnHdrById(plant, paymentheaderid);
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.DELETE_TAXRETURN_PAYMENT);	
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(taxReturnHdr.getTO_DATE()));														
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, paymentheaderid);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",taxReturnHdr.getCOUNTRY_CODE()+""+taxReturnHdr.getID());
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject result=new JSONObject();
			result.put("STATUS", "SUCCESS");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if(action.equalsIgnoreCase("includePrevious")) {
			String taxheaderid= StrUtils.fString(request.getParameter("taxheaderid")).trim();
			String fromDate= StrUtils.fString(request.getParameter("from")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			try {
				Map plantMst=taxReturnDAO.getPlantMst(ht);
				Map<String,String> taxSettings =  taxReturnDAO.getTaxSetting(ht);
				String taxReturnFrom=taxSettings.get("RETURNFROM");
				if(!fromDate.equalsIgnoreCase(taxReturnFrom))
				{
					LocalDate datetime = LocalDate.parse(taxReturnFrom, datePattern);
					LocalDate toDate=LocalDate.parse(fromDate,datePattern).minusDays(1);
					//String taxReturnUntil=datePattern.format(plusMonth);
					ht.put("FROM", datetime.toString());
					ht.put("TO", toDate.toString());
					List<Map<String,String>> salesInvoiceList=taxReturnDAO.getSalesInvoiceNotTaxGen(ht);
					int taxReturnHdrId=Integer.parseInt(taxheaderid);
					if(taxReturnHdrId>0)
					{
						for(Map<String,String> sales:salesInvoiceList)
						{
							TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
							taxRetDet.setPLANT(plant);
							taxRetDet.setTAXHDR_ID(taxReturnHdrId);
							taxRetDet.setCOUNTRY_CODE("UAE");
							System.out.println("Sales tax:"+sales.get("TAX_TYPE"));
							String taxtype=sales.get("TAX_TYPE");
							String filteredTaxType = null;
							if(!taxtype.isEmpty())
							{
								taxtype=taxtype.replace("[",":");
								System.out.println("TT:"+taxtype);
								String type[]=taxtype.split(":");
								
								if(type[0].contains("("))
								{
									filteredTaxType=type[0].substring(0, type[0].indexOf("("));
									System.out.println("TAx Type:"+filteredTaxType);
								}
								else
								{
									filteredTaxType=type[0];
								}
								
								
							
							if(filteredTaxType.equalsIgnoreCase("ZERO RATE"))
							{
								taxRetDet.setBOX("4");
							}
							else if(filteredTaxType.equalsIgnoreCase("EXEMPT"))
							{
								taxRetDet.setBOX("5");
							}
							else
							{
								if(sales.get("SALES_LOCATION")==null)
								{
									sales.put("SALES_LOCATION", "");
								}
								if(sales.get("SALES_LOCATION").equalsIgnoreCase("Abu Dhabi"))
									taxRetDet.setBOX("1a");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Dubai"))
									taxRetDet.setBOX("1b");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Sharjah"))
									taxRetDet.setBOX("1c");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Ajman"))
									taxRetDet.setBOX("1d");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Umm Al Quwain"))
									taxRetDet.setBOX("1e");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Ras Al Khaimah"))
									taxRetDet.setBOX("1f");
								else if(sales.get("SALES_LOCATION").equalsIgnoreCase("Fujairah"))
									taxRetDet.setBOX("1g");
								else
									taxRetDet.setBOX("unknown");
							}
							taxRetDet.setTRANSACTION_ID(sales.get("INVOICE"));
							Double salestaxableamount=Double.parseDouble(sales.get("AMOUNT"))+Double.parseDouble(sales.get("SHIPPINGCOST"));
							taxRetDet.setTAXABLE_AMOUNT(salestaxableamount);
							if(sales.get("TAXAMOUNT")==null)
							{
								sales.put("TAXAMOUNT", "0.00");
							}
							taxRetDet.setTAX_AMOUNT(Double.parseDouble(sales.get("TAXAMOUNT")));
							taxRetDet.setDATE(sales.get("INVOICE_DATE"));
							taxRetDet.setTRANSACTION_TYPE("Invoice");
							taxRetDet.setISTAXPREVIOUS(true);
						
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
						}
							
							
								
						}
					}
					String cond="TAXTREATMENT IN ('NON GCC','GCC VAT Registered','GCC NON VAT Registered')";
					String extracond="REVERSECHARGE='1'";
					List<Map<String,String>> billGCCNONGCCList=taxReturnDAO.getBillBYTaxTreamentTypeNotTaxGen(ht, cond,extracond);
					billGCCNONGCCList.forEach(bill->{
						TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
						taxRetDet.setPLANT(plant);
						taxRetDet.setTAXHDR_ID(taxReturnHdrId);
						taxRetDet.setCOUNTRY_CODE("UAE");
						taxRetDet.setBOX("3");
						taxRetDet.setTRANSACTION_ID(bill.get("BILL"));
						Double billtaxableamount=Double.parseDouble(bill.get("SUB_TOTAL"))+Double.parseDouble(bill.get("SHIPPINGCOST"));
						taxRetDet.setTAXABLE_AMOUNT(billtaxableamount);
						if(bill.get("TAXAMOUNT")==null)
						{
							bill.put("TAXAMOUNT", "0.00");
						}
						taxRetDet.setTAX_AMOUNT(Double.parseDouble(bill.get("TAXAMOUNT")));
						taxRetDet.setDATE(bill.get("BILL_DATE"));
						taxRetDet.setTRANSACTION_TYPE("Bill");
						taxRetDet.setISTAXPREVIOUS(true);
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
					});
					List<Map<String,String>> expenseList=taxReturnDAO.getExpenseBYTaxDateFilterNotTaxGen(ht);
					String[] taxtreatmentgccnongcc = new String[]{"NON GCC","GCC VAT Registered","GCC NON VAT Registered"};
					expenseList.forEach(expense->{
						boolean gccOrNonGcc=false;
							try {
								if(expense.get("TAXTREATMENT")==null)
								{
									gccOrNonGcc=false;
								}
								else
								{
									gccOrNonGcc = Arrays.stream(taxtreatmentgccnongcc).anyMatch(expense.get("TAXTREATMENT")::equals);
								}
								if(gccOrNonGcc)
								{
									int reversecharge = 0,goodsimport=0;
									if(!expense.get("REVERSECHARGE").isEmpty())
									{
										reversecharge=Integer.parseInt(expense.get("REVERSECHARGE"));
										
									}
									if(!expense.get("GOODSIMPORT").isEmpty())
									{
										goodsimport=Integer.parseInt(expense.get("GOODSIMPORT"));
										
									}
									if(reversecharge>0)
									{
										TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
										taxRetDet.setPLANT(plant);
										taxRetDet.setTAXHDR_ID(taxReturnHdrId);
										taxRetDet.setCOUNTRY_CODE("UAE");
										taxRetDet.setBOX("3");
										taxRetDet.setTRANSACTION_ID(expense.get("ID"));
										taxRetDet.setTAXABLE_AMOUNT(Double.parseDouble(expense.get("SUB_TOTAL")));
										if(expense.get("TAXAMOUNT")==null)
										{
											expense.put("TAXAMOUNT", "0.00");
										}
										taxRetDet.setTAX_AMOUNT(Double.parseDouble(expense.get("TAXAMOUNT")));
										taxRetDet.setDATE(expense.get("EXPENSES_DATE"));
										taxRetDet.setTRANSACTION_TYPE("Expense");
										taxRetDet.setISTAXPREVIOUS(true);
										try {
											taxReturnDAO.addTaxReturnDet(taxRetDet);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										taxReturnDetailList.add(taxRetDet);
									}
									else if(goodsimport>0)
									{
										TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
										taxRetDet.setPLANT(plant);
										taxRetDet.setTAXHDR_ID(taxReturnHdrId);
										taxRetDet.setCOUNTRY_CODE("UAE");
										taxRetDet.setBOX("6");
										taxRetDet.setTRANSACTION_ID(expense.get("ID"));
										taxRetDet.setTAXABLE_AMOUNT(Double.parseDouble(expense.get("SUB_TOTAL")));
										if(expense.get("TAXAMOUNT")==null)
										{
											expense.put("TAXAMOUNT", "0.00");
										}
										taxRetDet.setTAX_AMOUNT(Double.parseDouble(expense.get("TAXAMOUNT")));
										taxRetDet.setDATE(expense.get("EXPENSES_DATE"));
										taxRetDet.setTRANSACTION_TYPE("Expense");
										taxRetDet.setISTAXPREVIOUS(true);
										try {
											taxReturnDAO.addTaxReturnDet(taxRetDet);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										taxReturnDetailList.add(taxRetDet);
									}
								}
								else
								{
									TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
									taxRetDet.setPLANT(plant);
									taxRetDet.setTAXHDR_ID(taxReturnHdrId);
									taxRetDet.setCOUNTRY_CODE("UAE");
									taxRetDet.setBOX("9");
									taxRetDet.setTRANSACTION_ID(expense.get("ID"));
									taxRetDet.setTAXABLE_AMOUNT(Double.parseDouble(expense.get("SUB_TOTAL")));
									if(expense.get("TAXAMOUNT")==null)
									{
										expense.put("TAXAMOUNT", "0.00");
									}
									taxRetDet.setTAX_AMOUNT(Double.parseDouble(expense.get("TAXAMOUNT")));
									taxRetDet.setDATE(expense.get("EXPENSES_DATE"));
									taxRetDet.setTRANSACTION_TYPE("Expense");
									taxRetDet.setISTAXPREVIOUS(true);
									try {
										taxReturnDAO.addTaxReturnDet(taxRetDet);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									taxReturnDetailList.add(taxRetDet);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					});
					
					extracond="GOODSIMPORT='1'";
					List<Map<String,String>> billGCCNONGCCImportList=taxReturnDAO.getBillBYTaxTreamentTypeNotTaxGen(ht, cond,extracond);
					billGCCNONGCCImportList.forEach(bill->{
						TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
						taxRetDet.setPLANT(plant);
						taxRetDet.setTAXHDR_ID(taxReturnHdrId);
						taxRetDet.setCOUNTRY_CODE("UAE");
						taxRetDet.setBOX("6");
						taxRetDet.setTRANSACTION_ID(bill.get("BILL"));
						Double billtaxableamount=Double.parseDouble(bill.get("SUB_TOTAL"))+Double.parseDouble(bill.get("SHIPPINGCOST"));
						taxRetDet.setTAXABLE_AMOUNT(billtaxableamount);
						if(bill.get("TAXAMOUNT")==null)
						{
							bill.put("TAXAMOUNT", "0.00");
						}
						taxRetDet.setTAX_AMOUNT(Double.parseDouble(bill.get("TAXAMOUNT")));
						taxRetDet.setDATE(bill.get("BILL_DATE"));
						taxRetDet.setTRANSACTION_TYPE("Bill");
						taxRetDet.setISTAXPREVIOUS(true);
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
					});
					String exclude3_6_cond="TAXTREATMENT NOT IN ('NON GCC','GCC VAT Registered','GCC NON VAT Registered')";
					List<Map<String,String>> billExclude3_6=taxReturnDAO.getBillBYTaxTreamentTypeNotTaxGen(ht, exclude3_6_cond,"");
					billExclude3_6.forEach(bill->{
						TaxReturnFillDet taxRetDet=new TaxReturnFillDet();
						taxRetDet.setPLANT(plant);
						taxRetDet.setTAXHDR_ID(taxReturnHdrId);
						taxRetDet.setCOUNTRY_CODE("UAE");
						taxRetDet.setBOX("9");
						taxRetDet.setTRANSACTION_ID(bill.get("BILL"));
						Double billtaxableamount=Double.parseDouble(bill.get("SUB_TOTAL"))+Double.parseDouble(bill.get("SHIPPINGCOST"));
						taxRetDet.setTAXABLE_AMOUNT(billtaxableamount);
						if(bill.get("TAXAMOUNT")==null)
						{
							bill.put("TAXAMOUNT", "0.00");
						}
						taxRetDet.setTAX_AMOUNT(Double.parseDouble(bill.get("TAXAMOUNT")));
						taxRetDet.setDATE(bill.get("BILL_DATE"));
						taxRetDet.setTRANSACTION_TYPE("Bill");
						taxRetDet.setISTAXPREVIOUS(true);
						try {
							taxReturnDAO.addTaxReturnDet(taxRetDet);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						taxReturnDetailList.add(taxRetDet);
					});
					//System.out.println("From:"+datetime.toString()+"    To:"+plusMonth.toString());
					Double TOTAL_TAXPAYABLE=0.00;
					Double totalSalesTax=0.00;
					Double totalExpenseTax=0.00;
					List<TaxReturnFillDet> taxreturndetListAll=taxReturnDAO.getAllTaxReturnDet(plant, taxheaderid);
					for(TaxReturnFillDet taxDet:taxreturndetListAll)
					{
						if(taxDet.getBOX().equalsIgnoreCase("9"))
						{
							totalExpenseTax+=taxDet.getTAX_AMOUNT();
						}
						else if(taxDet.getBOX().equalsIgnoreCase("3") || taxDet.getBOX().equalsIgnoreCase("6") || taxDet.getBOX().equalsIgnoreCase("7"))
						{
							totalSalesTax+=taxDet.getTAX_AMOUNT();
							totalExpenseTax+=taxDet.getTAX_AMOUNT();
						}
						else
						{
							totalSalesTax+=taxDet.getTAX_AMOUNT();
						}
						//TOTAL_TAXPAYABLE+=taxDet.getTAX_AMOUNT();
						
					}
					TOTAL_TAXPAYABLE=totalSalesTax-totalExpenseTax;
					String updateItems="TAXPREVIOUSINCLUDED=1,TOTAL_TAXPAYABLE="+numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm);
					taxReturnDAO.updateTaxHeader(plant, String.valueOf(taxReturnHdrId), updateItems);
					taxReturnDAO.updateTaxBalanceDue(plant, String.valueOf(taxReturnHdrId), numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm));
					//update tax_status in Bill,Expense and Invoice Tables
					List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDetailList.stream().collect(Collectors.collectingAndThen(Collectors.toMap(TaxReturnFillDet::getTRANSACTION_ID, Function.identity(), (left, right) -> {
		                left.setTAXABLE_AMOUNT(left.getTAXABLE_AMOUNT()+right.getTAXABLE_AMOUNT());
		                return left;
		            }), m -> new ArrayList<>(m.values())));
					taxReturnDetailListMerged.forEach(taxMergeDet->{
						String id=taxMergeDet.getTRANSACTION_ID();
						String taxtype=taxMergeDet.getTRANSACTION_TYPE();
						String status="Tax Generated";
						try {
							taxReturnDAO.updateTaxItemStatus(plant, taxtype,id,status);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
					
					
					TaxReturnFillHdr taxReturnHdr=taxReturnDAO.getTaxReturnHdrById(plant, Integer.toString(taxReturnHdrId));
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.TAX_FILE);	
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(taxReturnHdr.getTO_DATE()));														
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, taxReturnHdrId);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",taxReturnHdr.getCOUNTRY_CODE()+""+taxReturnHdr.getID());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					} 
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			JSONObject result=new JSONObject();
			result.put("STATUS", "SUCCESS");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if(action.equalsIgnoreCase("excludePrevious")) {
			String taxheaderid= StrUtils.fString(request.getParameter("taxheaderid")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			try {
				List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDAO.getAllPreviousTaxReturnDet(plant, taxheaderid);
				taxReturnDetailListMerged.forEach(taxMergeDet->{
					String id=taxMergeDet.getTRANSACTION_ID();
					String taxtype=taxMergeDet.getTRANSACTION_TYPE();
					String status=" ";
					try {
						taxReturnDAO.updateTaxItemStatus(plant, taxtype,id,status);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				taxReturnDAO.deletePreviousTaxDetById(plant, taxheaderid);
				String updateItems="TAXPREVIOUSINCLUDED=0";
				taxReturnDAO.updateTaxHeader(plant, taxheaderid, updateItems);
				calculateTaxHeaderAmounts(plant,taxheaderid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject result=new JSONObject();
			result.put("STATUS", "SUCCESS");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(result.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		 Double taxableamount1a = 0.00,taxableamount1b = 0.00,taxableamount1c=0.00,taxableamount1d=0.00,taxableamount1e=0.00,taxableamount1f=0.00,taxableamount1g=0.00,taxableamount3=0.00,taxableamount4=0.00,taxableamount5=0.00,taxableamount6=0.00,taxableamount7=0.00,taxableamount9 = 0.00,taxableamount10 = 0.00,taxableamount11=0.00;
		 Double taxamount1a = 0.00,taxamount1b = 0.00,taxamount1c=0.00,taxamount1d=0.00,taxamount1e=0.00,taxamount1f=0.00,taxamount1g=0.00,taxamount3=0.00,taxamount4=0.00,taxamount5=0.00,taxamount6=0.00,taxamount7=0.00,taxamount9=0.00,taxamount10=0.00;
		 Double totalTaxableAmount8=0.00,totalTaxAmount8=0.00;
		 Numbers numberUtil=new Numbers();
			int scale=2;
			RoundingMode rm=RoundingMode.HALF_UP;
		if(action.equalsIgnoreCase("getSalesTaxReturn")) {
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			System.out.println("Tax Header Id:::"+taxheaderid);
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			
			 try {
				taxreturndetList=taxReturnDAO.getAllTaxReturnDet(plant, taxheaderid);
				for(TaxReturnFillDet taxreturndet:taxreturndetList)
				{
					if(taxreturndet.getBOX().equalsIgnoreCase("1a"))
					{
						taxableamount1a+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount1a+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("1b"))
					{
						taxableamount1b+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount1b+=taxreturndet.getTAX_AMOUNT();
					}	
					else if(taxreturndet.getBOX().equalsIgnoreCase("1c"))
					{
						taxableamount1c+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount1c+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("1d"))
					{
						taxableamount1d+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount1d+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("1e"))
					{
						taxableamount1e+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount1e+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("1f"))
					{
						taxableamount1f+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount1f+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("1g"))
					{
						taxableamount1g+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount1g+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("3"))
					{
						taxableamount3+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount3+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("4"))
					{
						taxableamount4+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount4+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("5"))
					{
						taxableamount5+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount5+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("6"))
					{
						taxableamount6+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxableAmount8+=taxreturndet.getTAXABLE_AMOUNT();
						totalTaxAmount8+=taxreturndet.getTAX_AMOUNT();
						taxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					
				}
				Hashtable adjusmentTable = new Hashtable();
				adjusmentTable.put("PLANT", plant);
				adjusmentTable.put("TAXHDR_ID", taxheaderid);
				List<Map<String,String>> taxAdjustmentList=taxAdjusmentDAO.getTaxAdjusment_Box7(adjusmentTable);
				for(Map<String,String> taxadjustment:taxAdjustmentList)
				{
					taxableamount7+=Double.parseDouble(taxadjustment.get("AMOUNT"));
					taxamount7+=Double.parseDouble(taxadjustment.get("TAXAMOUNT"));
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			JSONArray jarray=new JSONArray();
			try {
				List<Map<String,String>> taxReturnFiling =  taxReturnDAO.getTaxSalesFiling(ht);
				JSONObject taxSettingJson=new JSONObject();
				
				for(Map<String,String> taxreturn:taxReturnFiling)
				{
					if(taxreturn.get("BOX").equalsIgnoreCase("1a"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount1a, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount1a, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("1b"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount1b, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount1b, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("1c"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount1c, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount1c, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("1d"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount1d, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount1d, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("1e"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount1e, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount1e, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("1f"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount1f, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount1f, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("1g"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount1g, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount1g, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("3"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount3, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount3, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("4"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount4, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount4, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("5"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount5, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount5, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("6"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount6, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount6, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("7"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount7, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount7, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("8"))
					{
						totalTaxableAmount8+=taxableamount7;
						totalTaxAmount8+=taxamount7;
						taxreturn.put("taxableamount", ""+numberUtil.Round(totalTaxableAmount8, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(totalTaxAmount8, scale, rm));
						GlobalFinalTaxAmount1=totalTaxAmount8;
					}
					else
					{
						taxreturn.put("taxableamount", "0.00");
						taxreturn.put("taxamount", "0.00");
					}
						
					GlobalAmount=taxableamount3+taxableamount6+taxableamount7;
					GlobalTaxAmount=taxamount3+taxamount6+taxamount7;
					taxSettingJson.putAll(taxreturn);
					jarray.add(taxSettingJson);
				}
				//taxSettingJson.putAll(taxReturnFiling);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("getSalesTaxReturnForSG")) {
			RoundingMode rmz=RoundingMode.HALF_DOWN;
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			System.out.println("Tax Header Id:::"+taxheaderid);
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			Double boxamount1 = 0.00,boxamount2 = 0.00,boxamount3=0.00,boxamount4=0.00,boxamount5=0.00,boxamount6=0.00,boxamount7=0.00,
					boxamount8=0.00,boxamount9=0.00,boxamount13=0.00,outofscope=0.00;
			 try {
				
				 
				taxreturndetList=taxReturnDAO.getAllTaxReturnDet(plant, taxheaderid);
				for(TaxReturnFillDet taxreturndet:taxreturndetList)
				{
					if(taxreturndet.getBOX().equalsIgnoreCase("1"))
					{
						boxamount1+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("2"))
					{
						boxamount2+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}	
					else if(taxreturndet.getBOX().equalsIgnoreCase("3"))
					{
						boxamount3+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("5"))
					{
						boxamount5+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount7+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("6"))
					{
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("7"))
					{
						boxamount7+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("13"))
					{
						boxamount13+=taxreturndet.getTAXABLE_AMOUNT();
					}
				}
				
				outofscope = boxamount13;
				boxamount4 = boxamount1 + boxamount2 + boxamount3;
				boxamount8 = boxamount6 - boxamount7;
				boxamount13 = boxamount13  + boxamount4;
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			 String COUNTRYCODE="";
			 List viewlistQry = plantmstutil.getPlantMstDetails(plant);
			 for (int i = 0; i < viewlistQry.size(); i++) {
			     Map map = (Map) viewlistQry.get(i);
			     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
			 }
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("COUNTRY_CODE", COUNTRYCODE);
			JSONArray jarray=new JSONArray();
			try {
				List<Map<String,String>> taxReturnFiling =  taxReturnDAO.getTaxFilingForSG(ht);
				JSONObject taxSettingJson=new JSONObject();
				
				for(Map<String,String> taxreturn:taxReturnFiling)
				{
					if(taxreturn.get("BOX").equalsIgnoreCase("1"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount1, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("2"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount2, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("3"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount3, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("4"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount4, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("5"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount5, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("6"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount6, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("7"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount7, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("8"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount8, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("9"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount9, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("13"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount13, scale, rmz));
					}
					else
					{
						taxreturn.put("tamount", ""+numberUtil.Round(outofscope, scale, rmz));
					}
						
					taxSettingJson.putAll(taxreturn);
					jarray.add(taxSettingJson);
				}
				//taxSettingJson.putAll(taxReturnFiling);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("getSalesTaxReturnSummary")) {

			RoundingMode rmz=RoundingMode.HALF_DOWN;
			String fromdate = StrUtils.fString(req.getParameter("FROMDATE")).trim();
			String todate = StrUtils.fString(req.getParameter("TODATE")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			Double boxamount1 = 0.00,boxamount2 = 0.00,boxamount3=0.00,boxamount4=0.00,boxamount5=0.00,boxamount6=0.00,boxamount7=0.00,
					boxamount8=0.00,boxamount9=0.00,boxamount13=0.00,outofscope=0.00;
			 try {
				taxreturndetList=taxReturnDAO.getAllTaxReturnDetSummary(plant, fromdate, todate);
				for(TaxReturnFillDet taxreturndet:taxreturndetList)
				{
					if(taxreturndet.getBOX().equalsIgnoreCase("1"))
					{
						boxamount1+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("2"))
					{
						boxamount2+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}	
					else if(taxreturndet.getBOX().equalsIgnoreCase("3"))
					{
						boxamount3+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("5"))
					{
						boxamount5+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount7+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("6"))
					{
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("7"))
					{
						boxamount7+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("13"))
					{
						boxamount13+=taxreturndet.getTAXABLE_AMOUNT();
					}
				}
				
				outofscope = boxamount13;
				boxamount4 = boxamount1 + boxamount2 + boxamount3;
				boxamount8 = boxamount6 - boxamount7;
				boxamount13 = boxamount13  + boxamount4;
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			 String COUNTRYCODE="";
			 List viewlistQry = plantmstutil.getPlantMstDetails(plant);
			 for (int i = 0; i < viewlistQry.size(); i++) {
			     Map map = (Map) viewlistQry.get(i);
			     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
			 }
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("COUNTRY_CODE", COUNTRYCODE);
			JSONArray jarray=new JSONArray();
			try {
				List<Map<String,String>> taxReturnFiling =  taxReturnDAO.getTaxFilingForSG(ht);
				JSONObject taxSettingJson=new JSONObject();
				
				for(Map<String,String> taxreturn:taxReturnFiling)
				{
					if(taxreturn.get("BOX").equalsIgnoreCase("1"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount1, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("2"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount2, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("3"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount3, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("4"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount4, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("5"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount5, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("6"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount6, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("7"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount7, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("8"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount8, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("9"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount9, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("13"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount13, scale, rmz));
					}
					else
					{
						taxreturn.put("tamount", ""+numberUtil.Round(outofscope, scale, rmz));
					}
						
					taxSettingJson.putAll(taxreturn);
					jarray.add(taxSettingJson);
				}
				//taxSettingJson.putAll(taxReturnFiling);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}else if(action.equalsIgnoreCase("getExpenseTaxReturn")) {
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			JSONArray jarray=new JSONArray();
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			System.out.println("Tax Header Id:::"+taxheaderid);
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			 try {
					taxreturndetList=taxReturnDAO.getAllTaxReturnDet(plant, taxheaderid);
					for(TaxReturnFillDet taxreturndet:taxreturndetList)
					{
						if(taxreturndet.getBOX().equalsIgnoreCase("9"))
						{
							taxableamount9+=taxreturndet.getTAXABLE_AMOUNT();
							taxamount9+=taxreturndet.getTAX_AMOUNT();
						}
						
					}
			 }
			 catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			try {
				List<Map<String,String>> taxReturnFiling =  taxReturnDAO.getTaxExpenseFiling(ht);
				JSONObject taxSettingJson=new JSONObject();
				for(Map<String,String> taxreturn:taxReturnFiling)
				{
					if(taxreturn.get("BOX").equalsIgnoreCase("9"))
					{
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount9, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount9, scale, rm));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("10"))
					{
						taxamount10=GlobalTaxAmount;
						taxableamount10=GlobalAmount;
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount10, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount10, scale, rm));
						GlobalAmount=0.00;
						GlobalTaxAmount=0.00;
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("11"))
					{
						Double taxamount11=taxamount9+taxamount10;
						taxableamount11=taxableamount9+taxableamount10;
						taxreturn.put("taxableamount", ""+numberUtil.Round(taxableamount11, scale, rm));
						taxreturn.put("taxamount", ""+numberUtil.Round(taxamount11, scale, rm));
						GlobalFinalTaxAmount2=taxamount11;
					}
					else
					{
						taxreturn.put("taxableamount", "0.0");
					}
					taxSettingJson.putAll(taxreturn);
					jarray.add(taxSettingJson);
				}
				//taxSettingJson.putAll(taxReturnFiling);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("getDueTaxReturn")) {
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			JSONArray jarray=new JSONArray();
			try {
				List<Map<String,String>> taxReturnFiling =  taxReturnDAO.getTaxDueFiling(ht);
				JSONObject taxSettingJson=new JSONObject();
				for(Map<String,String> taxreturn:taxReturnFiling)
				{
					if(taxreturn.get("BOX").equalsIgnoreCase("12"))
					{
						taxreturn.put("taxamount", ""+numberUtil.Round(GlobalFinalTaxAmount1, scale, rm));
						
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("13"))
					{
						taxreturn.put("taxamount", ""+numberUtil.Round(GlobalFinalTaxAmount2, scale, rm));
						
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("14"))
					{
						GlobalFinalTaxAmount3=GlobalFinalTaxAmount1-GlobalFinalTaxAmount2;
						taxreturn.put("taxamount", ""+numberUtil.Round(GlobalFinalTaxAmount3, scale, rm));
						GlobalFinalTaxAmount1=0.00;
						GlobalFinalTaxAmount2=0.00;
						GlobalFinalTaxAmount3=0.00;
					}
					else
					{
						taxreturn.put("taxamount", "0.00");
					}
					taxSettingJson.putAll(taxreturn);
					jarray.add(taxSettingJson);
				}
				//taxSettingJson.putAll(taxReturnFiling);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("getTaxSettings")) {
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			JSONArray jarray=new JSONArray();
			DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			try {
				Map<String,String> taxSettings =  taxReturnDAO.getTaxSetting(ht);
				JSONObject taxSettingJson=new JSONObject();
				taxSettingJson.putAll(taxSettings);
				int taxRetid=taxReturnDAO.getMaxId(plant);
				if(taxRetid>0)
				{
					String lastToDate=taxReturnDAO.getTODate(plant, taxRetid);
					LocalDate datetime=LocalDate.parse(lastToDate,datePattern).plusDays(1);
					taxSettingJson.put("fromDate", datetime.format(datePattern));
				}
				else
				{
					taxSettingJson.put("fromDate", taxSettings.get("RETURNFROM"));
				}
				//jarray.add(taxSettingJson);
				//taxSettingJson.putAll(taxReturnFiling);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(taxSettingJson.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("getTaxReturns")) {
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			JSONArray jarray=new JSONArray();
			try {
				List<TaxReturnFillHdr> taxReturnHdr =  taxReturnDAO.getAllTaxReturnHdr(plant);
				JSONObject taxSettingJson=new JSONObject();
				//taxSettingJson.putAll(taxReturnHdr.toArray());
				//jarray.add(taxSettingJson);
				//taxSettingJson.putAll(taxReturnFiling);
				jarray.addAll(taxReturnHdr);
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("getTaxReturnById")) {
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			JSONArray jarray=new JSONArray();
			try {
				TaxReturnFillHdr taxReturnHdr =  taxReturnDAO.getTaxReturnHdrById(plant,taxheaderid);
				JSONObject taxSettingJson=new JSONObject();
				taxSettingJson.put("taxheader",taxReturnHdr);
				//taxSettingJson.putAll(taxReturnHdr.toArray());
				//jarray.add(taxSettingJson);
				//taxSettingJson.putAll(taxReturnFiling);
				//jarray.addAll(taxReturnHdr);
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(taxSettingJson.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(action.equalsIgnoreCase("fillTax")) {
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			String filedon = StrUtils.fString(req.getParameter("fileddate")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			String status="Filed";
			try {
				//taxReturnDAO.updateTaxHeaderStatus(plant,taxheaderid,status);
				String updateItems="FILED_ON='"+filedon+"',STATUS='"+status+"'";
				taxReturnDAO.updateTaxHeader(plant, taxheaderid, updateItems);
				 resp.getWriter().write("Filed");
			     resp.flushBuffer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(action.equalsIgnoreCase("unfillTax")) {
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			String status="Unfiled";
			try {
				String updateItems="FILED_ON=' ',STATUS='"+status+"'";
				taxReturnDAO.updateTaxHeader(plant, taxheaderid, updateItems);
				//taxReturnDAO.updateTaxHeaderStatus(plant,taxheaderid,status);
				 resp.getWriter().write("Unfiled");
			     resp.flushBuffer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(action.equalsIgnoreCase("transactionSummary")) {
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			String box=StrUtils.fString(req.getParameter("box")).trim();
			List<TaxReturnTransactionSummary> tranSummaryList= new ArrayList<>();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			JSONArray jarray=new JSONArray();
			List<TaxReturnFillDet> taxReturnDetailList = new ArrayList<>();
			if(box.equalsIgnoreCase("10"))
			{
				try {
					String query="BOX IN ('3','6','7')";
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBoxes(plant,taxheaderid,query);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(box.equalsIgnoreCase("7"))
			{
				try {
					 List<TaxReturnFillAdjust> taxReturnDetAdjustList = taxReturnDAO.getAllTaxReturnDetAdjust(plant,taxheaderid);
					 for(TaxReturnFillAdjust taxadjust:taxReturnDetAdjustList)
					 {
						 TaxReturnFillDet taxDet=new TaxReturnFillDet();
						 taxDet.setDATE(taxadjust.getADJUSTMENTDATE());
						 taxDet.setTRANSACTION_ID(String.valueOf(taxadjust.getID()));
						 taxDet.setTRANSACTION_TYPE("Tax Adjustment");
						 taxDet.setTAXABLE_AMOUNT(taxadjust.getAMOUNT());
						 taxDet.setTAX_AMOUNT(taxadjust.getTAXAMOUNT());
						 taxReturnDetailList.add(taxDet);
					 }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBox(plant,taxheaderid,box);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDetailList.stream().collect(Collectors.collectingAndThen(Collectors.toMap(TaxReturnFillDet::getTRANSACTION_ID, Function.identity(), (left, right) -> {
                left.setTAXABLE_AMOUNT(left.getTAXABLE_AMOUNT()+right.getTAXABLE_AMOUNT());
                return left;
            }), m -> new ArrayList<>(m.values())));
			taxReturnDetailListMerged.forEach(transDet->{
				TaxReturnTransactionSummary tranSummary=new TaxReturnTransactionSummary();
				tranSummary.setDATE(transDet.getDATE());
				tranSummary.setENTRY(transDet.getTRANSACTION_ID());
				tranSummary.setTAXABLE_AMOUNT(transDet.getTAXABLE_AMOUNT());
				tranSummary.setTAX_AMOUNT(transDet.getTAX_AMOUNT());
				tranSummary.setTRANSACTION_TYPE(transDet.getTRANSACTION_TYPE());
				tranSummaryList.add(tranSummary);
			});
			jarray.addAll(tranSummaryList);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
			
		}else if(action.equalsIgnoreCase("transactionSummaryForSG")) {
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			String box=StrUtils.fString(req.getParameter("box")).trim();
			List<TaxReturnTransactionSummary> tranSummaryList= new ArrayList<>();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			JSONArray jarray=new JSONArray();
			List<TaxReturnFillDet> taxReturnDetailList = new ArrayList<>();
			if(box.equalsIgnoreCase("6"))
			{
				try {
					String query="BOX IN ('1','6')";
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBoxes(plant,taxheaderid,query);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(box.equalsIgnoreCase("7"))
			{
				try {
					String query="BOX IN ('5','7')";
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBoxes(plant,taxheaderid,query);
					taxReturnDetailList = taxReturnDetailList.stream().filter((r)->r.getTAX_AMOUNT()!=0).collect(Collectors.toList());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBox(plant,taxheaderid,box);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			/*List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDetailList.stream().collect(Collectors.collectingAndThen(Collectors.toMap(TaxReturnFillDet::getTRANSACTION_ID, Function.identity(), (left, right) -> {
                left.setTAXABLE_AMOUNT(left.getTAXABLE_AMOUNT()+right.getTAXABLE_AMOUNT());
                return left;
            }), m -> new ArrayList<>(m.values())));*/
			List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDetailList;
			taxReturnDetailListMerged.forEach(transDet->{
				TaxReturnTransactionSummary tranSummary=new TaxReturnTransactionSummary();
				tranSummary.setDATE(transDet.getDATE());
				tranSummary.setENTRY(transDet.getTRANSACTION_ID());
				tranSummary.setTAXABLE_AMOUNT(transDet.getTAXABLE_AMOUNT());
				tranSummary.setTAX_AMOUNT(transDet.getTAX_AMOUNT());
				tranSummary.setTRANSACTION_TYPE(transDet.getTRANSACTION_TYPE());
				tranSummaryList.add(tranSummary);
			});
			jarray.addAll(tranSummaryList);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
			
		}
		else if(action.equalsIgnoreCase("taxPaymentHdrSummary"))
		{
			List<TaxReturnFillHdr> taxRetPaymentList=new ArrayList<>();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			JSONArray jarray=new JSONArray();
			try {
				taxRetPaymentList=taxReturnDAO.getAllTaxReturnPaymentsHdr(plant);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jarray.addAll(taxRetPaymentList);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}
		else if(action.equalsIgnoreCase("taxPaymentDetSummary"))
		{
			List<TaxReturnPaymentDet> taxRetPaymentList=new ArrayList<>();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			JSONArray jarray=new JSONArray();
			try {
				taxRetPaymentList=taxReturnDAO.getAllTaxReturnPaymentsDet(plant);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jarray.addAll(taxRetPaymentList);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}else if(action.equalsIgnoreCase("getSalesTaxReturnSummaryExcel")) {

			RoundingMode rmz=RoundingMode.HALF_DOWN;
			String fromdate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String todate = StrUtils.fString(req.getParameter("toDate")).trim();
			String asofdate = StrUtils.fString(req.getParameter("asOfDate")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			HSSFWorkbook workbook = null;
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			Double boxamount1 = 0.00,boxamount2 = 0.00,boxamount3=0.00,boxamount4=0.00,boxamount5=0.00,boxamount6=0.00,boxamount7=0.00,
					boxamount8=0.00,boxamount9=0.00,boxamount13=0.00,outofscope=0.00;
			 try {
				taxreturndetList=taxReturnDAO.getAllTaxReturnDetSummary(plant, fromdate, todate);
				for(TaxReturnFillDet taxreturndet:taxreturndetList)
				{
					if(taxreturndet.getBOX().equalsIgnoreCase("1"))
					{
						boxamount1+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("2"))
					{
						boxamount2+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}	
					else if(taxreturndet.getBOX().equalsIgnoreCase("3"))
					{
						boxamount3+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("5"))
					{
						boxamount5+=taxreturndet.getTAXABLE_AMOUNT();
						boxamount7+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("6"))
					{
						boxamount6+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("7"))
					{
						boxamount7+=taxreturndet.getTAX_AMOUNT();
					}
					else if(taxreturndet.getBOX().equalsIgnoreCase("13"))
					{
						boxamount13+=taxreturndet.getTAXABLE_AMOUNT();
					}
				}
				
				outofscope = boxamount13;
				boxamount4 = boxamount1 + boxamount2 + boxamount3;
				boxamount8 = boxamount6 - boxamount7;
				boxamount13 = boxamount13  + boxamount4;
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			 String COUNTRYCODE="";
			 List viewlistQry = plantmstutil.getPlantMstDetails(plant);
			 for (int i = 0; i < viewlistQry.size(); i++) {
			     Map map = (Map) viewlistQry.get(i);
			     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
			 }
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("COUNTRY_CODE", COUNTRYCODE);
			JSONArray jarray=new JSONArray();
			try {
				List<Map<String,String>> taxReturnFiling =  taxReturnDAO.getTaxFilingForSG(ht);
				JSONObject taxSettingJson=new JSONObject();
				
				for(Map<String,String> taxreturn:taxReturnFiling)
				{
					if(taxreturn.get("BOX").equalsIgnoreCase("1"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount1, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("2"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount2, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("3"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount3, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("4"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount4, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("5"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount5, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("6"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount6, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("7"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount7, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("8"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount8, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("9"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount9, scale, rmz));
					}
					else if(taxreturn.get("BOX").equalsIgnoreCase("13"))
					{
						taxreturn.put("tamount", ""+numberUtil.Round(boxamount13, scale, rmz));
					}
					else
					{
						taxreturn.put("tamount", ""+numberUtil.Round(outofscope, scale, rmz));
					}
						
					//taxSettingJson.putAll(taxreturn);
					//jarray.add(taxSettingJson);
					
					
				}
				//taxSettingJson.putAll(taxReturnFiling);
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();
				ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
				Map plntMap = (Map) plntList.get(0);
				String PLNTDESC = (String) plntMap.get("PLNTDESC");
				workbook = populateExcelFprRec(taxReturnFiling, asofdate,PLNTDESC);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			resp.setContentType("application/ms-excel");
			resp.setContentLength(outArray.length);
			resp.setHeader("Expires:", "0"); // eliminates browser caching
			resp.setHeader("Content-Disposition", "attachment; filename=GSTSummaryReport.xls");
			OutputStream outStream = resp.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		
		}else if(action.equalsIgnoreCase("getTaxReturnDetailedSummary")) {

			RoundingMode rmz=RoundingMode.HALF_DOWN;
			String fromdate = StrUtils.fString(req.getParameter("FROMDATE")).trim();
			String todate = StrUtils.fString(req.getParameter("TODATE")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			List<FinTaxDetailedSummaryPojo> taxDetailedSummaryList = new ArrayList<FinTaxDetailedSummaryPojo>();
			JSONObject taxSettingJson=new JSONObject();
			JSONArray jarray=new JSONArray();
			 try {
				 selectBean sb = new selectBean();
				 List<Map<String,String>> taxReturnFiling =taxReturnDAO.getDetailTaxFilingReportForSG(plant, fromdate, todate);
				 String gstpurchase =	new selectBean().getGST("PURCHASE",plant);
				 String gstsales =  new selectBean().getGST("SALES",plant);
				 String b6name = "";
				 String b7name = "";
				 for(Map<String,String> taxreturn:taxReturnFiling){
					 int box = Integer.valueOf(taxreturn.get("BOX"));
					 if(box == 6) {
						 b6name = "BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING");
					 }else if(box == 7){
						 b7name = "BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING");
					 }
				 }
				 
				 for(Map<String,String> taxreturn:taxReturnFiling){
					 FinCountryTaxType taxtypes = finCountryTaxTypeDAO.getCountryTaxTypesByid(Integer.valueOf(taxreturn.get("TAXID")));
					 
					 int box = Integer.valueOf(taxreturn.get("BOX"));
					 if(box == 1) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 taxDetailedSummary.setTaxrate("");
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary1.setBoxname(b6name);
						 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary1.setName(taxreturn.get("NAME"));
						 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary1.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary1.setTaxrate(gstsales);
						 }
						 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary1.setBox(6);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 taxDetailedSummaryList.add(taxDetailedSummary1);
						 
					 }else if(box == 2) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 taxDetailedSummary.setTaxrate("");
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary1.setBoxname(b6name);
						 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary1.setName(taxreturn.get("NAME"));
						 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary1.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary1.setTaxrate(gstsales);
						 }
						 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary1.setBox(6);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 taxDetailedSummaryList.add(taxDetailedSummary1);
						 
						 
					 }else if(box == 3) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 taxDetailedSummary.setTaxrate("");
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary1.setBoxname(b6name);
						 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary1.setName(taxreturn.get("NAME"));
						 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary1.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary1.setTaxrate(gstsales);
						 }
						 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary1.setBox(6);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 taxDetailedSummaryList.add(taxDetailedSummary1);
						 
					 }else if(box == 5) {
						 if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Bill")) {
							 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
							 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary.setName(taxreturn.get("NAME"));
							 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
							 taxDetailedSummary.setTaxrate("");
							 taxDetailedSummary.setNetamount("");
							 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
							 taxDetailedSummary.setBox(box);
							 
							 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary1.setBoxname(b7name);
							 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary1.setName(taxreturn.get("NAME"));
							 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
							 if(taxtypes.getISZERO() == 1) {
								 taxDetailedSummary1.setTaxrate("0.0");
							 }else {
								 taxDetailedSummary1.setTaxrate(gstpurchase);
							 }
							 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
							 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
							 taxDetailedSummary1.setBox(7);
							 
							 taxDetailedSummaryList.add(taxDetailedSummary);
							 taxDetailedSummaryList.add(taxDetailedSummary1);
							 
						 }else {
							 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
							 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary.setName(taxreturn.get("NAME"));
							 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
							 taxDetailedSummary.setTaxrate("");
							 taxDetailedSummary.setNetamount("");
							 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT")); 
							 taxDetailedSummary.setBox(box);
							 
							 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary1.setBoxname(b7name);
							 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary1.setName(taxreturn.get("NAME"));
							 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
							 if(taxtypes.getISZERO() == 1) {
								 taxDetailedSummary1.setTaxrate("0.0");
							 }else {
								 taxDetailedSummary1.setTaxrate(gstsales);
							 }
							 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
							 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
							 taxDetailedSummary1.setBox(7);
							 
							 taxDetailedSummaryList.add(taxDetailedSummary);
							 taxDetailedSummaryList.add(taxDetailedSummary1);
							 
						 }
					 }else if(box == 6) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary.setTaxrate(gstsales);
						 }
						 taxDetailedSummary.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 
					 }else if(box == 7) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary.setTaxrate(gstpurchase);
						 }
						 taxDetailedSummary.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
					 }
				}
				 
				 
				List<FinTaxDetailedSummaryPojo> sortedList = taxDetailedSummaryList.stream()
				            .sorted(Comparator.comparingInt(FinTaxDetailedSummaryPojo::getBox))
				            .collect(Collectors.toList());
				
				double b1=0.0,b2=0.0,b3=0.0,b5=0.0,b6=0.0,b7=0.0;
				 
				 for (FinTaxDetailedSummaryPojo TaxDetsummary : sortedList) {
					 int box = TaxDetsummary.getBox();
					 if(box == 1) {
						 b1 = b1 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b1));
					 }else if(box == 2) {
						 b2 = b2 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b2));
					 }else if(box == 3) {
						 b3 = b3 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b3));
					 }else if(box == 5) {
						 b5 = b5 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b5));
					 }else if(box == 6) {
						 b6 = b6 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b6));
					 }else if(box == 7) {
						 b7 = b7 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b7));
					 }
				 }
				 
				 for (FinTaxDetailedSummaryPojo TaxDetsummary : sortedList) {
					 jarray.add(TaxDetsummary);
				 }
				 
				 
				 resp.setContentType("application/json");
				 resp.setCharacterEncoding("UTF-8");
				 resp.getWriter().write(jarray.toString());
				 resp.getWriter().flush();
				 resp.getWriter().close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("getTaxReturnDetailedSummaryExcel")) {

			RoundingMode rmz=RoundingMode.HALF_DOWN;
			String fromdate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String todate = StrUtils.fString(req.getParameter("toDate")).trim();
			String asOfDate = StrUtils.fString(req.getParameter("asOfDate")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			HSSFWorkbook workbook = null;
			FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			List<FinTaxDetailedSummaryPojo> taxDetailedSummaryList = new ArrayList<FinTaxDetailedSummaryPojo>();
			JSONObject taxSettingJson=new JSONObject();
			JSONArray jarray=new JSONArray();
			 try {
				 selectBean sb = new selectBean();
				 List<Map<String,String>> taxReturnFiling =taxReturnDAO.getDetailTaxFilingReportForSG(plant, fromdate, todate);
				 String gstpurchase =	new selectBean().getGST("PURCHASE",plant);
				 String gstsales =  new selectBean().getGST("SALES",plant);
				 String b6name = "";
				 String b7name = "";
				 for(Map<String,String> taxreturn:taxReturnFiling){
					 int box = Integer.valueOf(taxreturn.get("BOX"));
					 if(box == 6) {
						 b6name = "BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING");
					 }else if(box == 7){
						 b7name = "BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING");
					 }
				 }
				 
				 for(Map<String,String> taxreturn:taxReturnFiling){
					 FinCountryTaxType taxtypes = finCountryTaxTypeDAO.getCountryTaxTypesByid(Integer.valueOf(taxreturn.get("TAXID")));
					 
					 int box = Integer.valueOf(taxreturn.get("BOX"));
					 if(box == 1) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 taxDetailedSummary.setTaxrate("");
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary1.setBoxname(b6name);
						 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary1.setName(taxreturn.get("NAME"));
						 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary1.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary1.setTaxrate(gstsales);
						 }
						 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary1.setBox(6);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 taxDetailedSummaryList.add(taxDetailedSummary1);
						 
					 }else if(box == 2) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 taxDetailedSummary.setTaxrate("");
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary1.setBoxname(b6name);
						 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary1.setName(taxreturn.get("NAME"));
						 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary1.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary1.setTaxrate(gstsales);
						 }
						 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary1.setBox(6);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 taxDetailedSummaryList.add(taxDetailedSummary1);
						 
						 
					 }else if(box == 3) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 taxDetailedSummary.setTaxrate("");
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary1.setBoxname(b6name);
						 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary1.setName(taxreturn.get("NAME"));
						 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary1.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary1.setTaxrate(gstsales);
						 }
						 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
						 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary1.setBox(6);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 taxDetailedSummaryList.add(taxDetailedSummary1);
						 
					 }else if(box == 5) {
						 if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Bill")) {
							 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
							 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary.setName(taxreturn.get("NAME"));
							 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
							 taxDetailedSummary.setTaxrate("");
							 taxDetailedSummary.setNetamount("");
							 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT"));
							 taxDetailedSummary.setBox(box);
							 
							 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary1.setBoxname(b7name);
							 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary1.setName(taxreturn.get("NAME"));
							 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
							 if(taxtypes.getISZERO() == 1) {
								 taxDetailedSummary1.setTaxrate("0.0");
							 }else {
								 taxDetailedSummary1.setTaxrate(gstpurchase);
							 }
							 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
							 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
							 taxDetailedSummary1.setBox(7);
							 
							 taxDetailedSummaryList.add(taxDetailedSummary);
							 taxDetailedSummaryList.add(taxDetailedSummary1);
							 
						 }else {
							 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
							 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary.setName(taxreturn.get("NAME"));
							 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
							 taxDetailedSummary.setTaxrate("");
							 taxDetailedSummary.setNetamount("");
							 taxDetailedSummary.setAmount(taxreturn.get("TAXABLE_AMOUNT")); 
							 taxDetailedSummary.setBox(box);
							 
							 FinTaxDetailedSummaryPojo taxDetailedSummary1 = new FinTaxDetailedSummaryPojo();
							 taxDetailedSummary1.setBoxname(b7name);
							 taxDetailedSummary1.setTaxdate(taxreturn.get("DATE"));
							 taxDetailedSummary1.setTransactionid(taxreturn.get("TRANSACTION_ID"));
							 taxDetailedSummary1.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
							 taxDetailedSummary1.setName(taxreturn.get("NAME"));
							 taxDetailedSummary1.setTaxname(taxtypes.getTAXTYPE());
							 if(taxtypes.getISZERO() == 1) {
								 taxDetailedSummary1.setTaxrate("0.0");
							 }else {
								 taxDetailedSummary1.setTaxrate(gstsales);
							 }
							 taxDetailedSummary1.setNetamount(taxreturn.get("TAXABLE_AMOUNT"));
							 taxDetailedSummary1.setAmount(taxreturn.get("TAX_AMOUNT"));
							 taxDetailedSummary1.setBox(7);
							 
							 taxDetailedSummaryList.add(taxDetailedSummary);
							 taxDetailedSummaryList.add(taxDetailedSummary1);
							 
						 }
					 }else if(box == 6) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary.setTaxrate(gstsales);
						 }
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
						 
					 }else if(box == 7) {
						 FinTaxDetailedSummaryPojo taxDetailedSummary = new FinTaxDetailedSummaryPojo();
						 taxDetailedSummary.setBoxname("BOX "+taxreturn.get("BOX")+" "+taxreturn.get("DESC_HEADING"));
						 taxDetailedSummary.setTaxdate(taxreturn.get("DATE"));
						 taxDetailedSummary.setTransactionid(taxreturn.get("TRANSACTION_ID"));
						 taxDetailedSummary.setTransactiontype(taxreturn.get("TRANSACTION_TYPE"));
						 taxDetailedSummary.setName(taxreturn.get("NAME"));
						 taxDetailedSummary.setTaxname(taxtypes.getTAXTYPE());
						 if(taxtypes.getISZERO() == 1) {
							 taxDetailedSummary.setTaxrate("0.0");
						 }else {
							 taxDetailedSummary.setTaxrate(gstpurchase);
						 }
						 taxDetailedSummary.setNetamount("");
						 taxDetailedSummary.setAmount(taxreturn.get("TAX_AMOUNT"));
						 taxDetailedSummary.setBox(box);
						 
						 taxDetailedSummaryList.add(taxDetailedSummary);
					 }
				}
				 
				 
				List<FinTaxDetailedSummaryPojo> sortedList = taxDetailedSummaryList.stream()
				            .sorted(Comparator.comparingInt(FinTaxDetailedSummaryPojo::getBox))
				            .collect(Collectors.toList());
				
				double b1=0.0,b2=0.0,b3=0.0,b5=0.0,b6=0.0,b7=0.0;
				 
				 for (FinTaxDetailedSummaryPojo TaxDetsummary : sortedList) {
					 int box = TaxDetsummary.getBox();
					 if(box == 1) {
						 b1 = b1 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b1));
					 }else if(box == 2) {
						 b2 = b2 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b2));
					 }else if(box == 3) {
						 b3 = b3 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b3));
					 }else if(box == 5) {
						 b5 = b5 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b5));
					 }else if(box == 6) {
						 b6 = b6 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b6));
					 }else if(box == 7) {
						 b7 = b7 + Double.valueOf(TaxDetsummary.getAmount());
						 TaxDetsummary.setBalance(String.valueOf(b7));
					 }
				 }
				 
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();
				ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
				Map plntMap = (Map) plntList.get(0);
				String PLNTDESC = (String) plntMap.get("PLNTDESC");
				workbook = populateExcelFprRecForDetail(sortedList,asOfDate,PLNTDESC);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			resp.setContentType("application/ms-excel");
			resp.setContentLength(outArray.length);
			resp.setHeader("Expires:", "0"); // eliminates browser caching
			resp.setHeader("Content-Disposition", "attachment; filename=GSTDetailReport.xls");
			OutputStream outStream = resp.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if(action.equalsIgnoreCase("getTaxLiabilityReport")) {

			RoundingMode rmz=RoundingMode.HALF_DOWN;
			String fromdate = StrUtils.fString(req.getParameter("FROMDATE")).trim();
			String todate = StrUtils.fString(req.getParameter("TODATE")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			List<FinTaxLiabilityReportPojo> FinTaxLiabilityReportPojolist = new ArrayList<FinTaxLiabilityReportPojo>();
			JSONObject taxSettingJson=new JSONObject();
			JSONArray jarray=new JSONArray();
			 try {
				 selectBean sb = new selectBean();
				 List<Map<String,String>> taxReturnFiling =taxReturnDAO.getDetailTaxFilingReportForSG(plant, fromdate, todate);
				 String gstpurchase =	new selectBean().getGST("PURCHASE",plant);
				 String gstsales =  new selectBean().getGST("SALES",plant);
				
				 
				 for(Map<String,String> taxreturn:taxReturnFiling){
					FinCountryTaxType finCountryTaxType = finCountryTaxTypeDAO.getCountryTaxTypesByid(Integer.valueOf(taxreturn.get("TAXID")));
					String sGstPercentage = "0.0";
					String STATE_PREFIX = "";
					
					if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Invoice")) {
						sGstPercentage = gstsales;
					}else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Bill")) {
						sGstPercentage = gstpurchase;
					}else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Expense")) {
						sGstPercentage = gstsales;
					}

					String display = "";
					if (finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
						display = finCountryTaxType.getTAXTYPE();
					} else if (finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1
							&& finCountryTaxType.getISZERO() == 0) {
						display = finCountryTaxType.getTAXTYPE() + " [" + sGstPercentage + "%]";
					} else if (finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1
							&& finCountryTaxType.getISZERO() == 1) {
						display = finCountryTaxType.getTAXTYPE() + " [0.0%]";
					} else if (finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
						display = finCountryTaxType.getTAXTYPE() + STATE_PREFIX;
					} else if (finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1
							&& finCountryTaxType.getISZERO() == 1) {
						display = finCountryTaxType.getTAXTYPE() + STATE_PREFIX + " [0.0%]";
					} else {
						display = finCountryTaxType.getTAXTYPE() + STATE_PREFIX + " [" + sGstPercentage + "%]";
					}
					 
					
					 if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Invoice")) {
						 FinTaxLiabilityReportPojo FinTaxLiabilityReport = new FinTaxLiabilityReportPojo("Inland Revenue Authority of Singapore", "(INVOICE) "+display, Double.valueOf(taxreturn.get("TAXABLE_AMOUNT")), Double.valueOf(taxreturn.get("TAX_AMOUNT")));
						 FinTaxLiabilityReportPojolist.add(FinTaxLiabilityReport);
					 }else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Bill")) {
						 FinTaxLiabilityReportPojo FinTaxLiabilityReport = new FinTaxLiabilityReportPojo("Inland Revenue Authority of Singapore", "(BILL) "+display, Double.valueOf(taxreturn.get("TAXABLE_AMOUNT")), Double.valueOf(taxreturn.get("TAX_AMOUNT")));
						 FinTaxLiabilityReportPojolist.add(FinTaxLiabilityReport);
					 } else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Expense")) {
						 FinTaxLiabilityReportPojo FinTaxLiabilityReport = new FinTaxLiabilityReportPojo("Inland Revenue Authority of Singapore", "(EXPENSE) "+display, Double.valueOf(taxreturn.get("TAXABLE_AMOUNT")), Double.valueOf(taxreturn.get("TAX_AMOUNT")));
						 FinTaxLiabilityReportPojolist.add(FinTaxLiabilityReport);
					 }
				}
				 
				 
				/*List<FinTaxDetailedSummaryPojo> sortedList = taxDetailedSummaryList.stream()
				            .sorted(Comparator.comparingInt(FinTaxDetailedSummaryPojo::getBox))
				            .collect(Collectors.toList());*/
				 List<FinTaxLiabilityReportPojo> transform = FinTaxLiabilityReportPojolist.stream()
				            .collect(Collectors.groupingBy(foo -> foo.getTaxname()))
				            .entrySet().stream()
				            .map(e -> e.getValue().stream()
				                .reduce((f1,f2) -> new FinTaxLiabilityReportPojo(f1.getBoxname(),f1.getTaxname(),f1.getNetamount()+f2.getNetamount(),f1.getAmount()+f2.getAmount())))
				                .map(f -> f.get())
				                .collect(Collectors.toList());
				 System.out.println(transform);

				
				 
				 for (FinTaxLiabilityReportPojo FinTaxLiability : transform) {
					 jarray.add(FinTaxLiability);
				 }
				 
				 
				 resp.setContentType("application/json");
				 resp.setCharacterEncoding("UTF-8");
				 resp.getWriter().write(jarray.toString());
				 resp.getWriter().flush();
				 resp.getWriter().close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("getTaxLiabilityReportExcel")) {

			RoundingMode rmz=RoundingMode.HALF_DOWN;
			String fromdate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String todate = StrUtils.fString(req.getParameter("toDate")).trim();
			String asOfDate = StrUtils.fString(req.getParameter("asOfDate")).trim();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			TaxReturnDAO taxAdjusmentDAO=new TaxReturnDAO();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			
			HSSFWorkbook workbook = null;
			List<TaxReturnFillDet> taxreturndetList=new ArrayList<>();
			List<FinTaxLiabilityReportPojo> FinTaxLiabilityReportPojolist = new ArrayList<FinTaxLiabilityReportPojo>();
			JSONObject taxSettingJson=new JSONObject();
			JSONArray jarray=new JSONArray();
			 try {
				 selectBean sb = new selectBean();
				 List<Map<String,String>> taxReturnFiling =taxReturnDAO.getDetailTaxFilingReportForSG(plant, fromdate, todate);
				 String gstpurchase =	new selectBean().getGST("PURCHASE",plant);
				 String gstsales =  new selectBean().getGST("SALES",plant);
				
				 
				 for(Map<String,String> taxreturn:taxReturnFiling){
					FinCountryTaxType finCountryTaxType = finCountryTaxTypeDAO.getCountryTaxTypesByid(Integer.valueOf(taxreturn.get("TAXID")));
					String sGstPercentage = "0.0";
					String STATE_PREFIX = "";
					
					if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Invoice")) {
						sGstPercentage = gstsales;
					}else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Bill")) {
						sGstPercentage = gstpurchase;
					}else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Expense")) {
						sGstPercentage = gstsales;
					}

					String display = "";
					if (finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
						display = finCountryTaxType.getTAXTYPE();
					} else if (finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1
							&& finCountryTaxType.getISZERO() == 0) {
						display = finCountryTaxType.getTAXTYPE() + " [" + sGstPercentage + "%]";
					} else if (finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1
							&& finCountryTaxType.getISZERO() == 1) {
						display = finCountryTaxType.getTAXTYPE() + " [0.0%]";
					} else if (finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
						display = finCountryTaxType.getTAXTYPE() + STATE_PREFIX;
					} else if (finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1
							&& finCountryTaxType.getISZERO() == 1) {
						display = finCountryTaxType.getTAXTYPE() + STATE_PREFIX + " [0.0%]";
					} else {
						display = finCountryTaxType.getTAXTYPE() + STATE_PREFIX + " [" + sGstPercentage + "%]";
					}
					 
					
					 if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Invoice")) {
						 FinTaxLiabilityReportPojo FinTaxLiabilityReport = new FinTaxLiabilityReportPojo("Inland Revenue Authority of Singapore", "(INVOICE) "+display, Double.valueOf(taxreturn.get("TAXABLE_AMOUNT")), Double.valueOf(taxreturn.get("TAX_AMOUNT")));
						 FinTaxLiabilityReportPojolist.add(FinTaxLiabilityReport);
					 }else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Bill")) {
						 FinTaxLiabilityReportPojo FinTaxLiabilityReport = new FinTaxLiabilityReportPojo("Inland Revenue Authority of Singapore", "(BILL) "+display, Double.valueOf(taxreturn.get("TAXABLE_AMOUNT")), Double.valueOf(taxreturn.get("TAX_AMOUNT")));
						 FinTaxLiabilityReportPojolist.add(FinTaxLiabilityReport);
					 } else if(taxreturn.get("TRANSACTION_TYPE").equalsIgnoreCase("Expense")) {
						 FinTaxLiabilityReportPojo FinTaxLiabilityReport = new FinTaxLiabilityReportPojo("Inland Revenue Authority of Singapore", "(EXPENSE) "+display, Double.valueOf(taxreturn.get("TAXABLE_AMOUNT")), Double.valueOf(taxreturn.get("TAX_AMOUNT")));
						 FinTaxLiabilityReportPojolist.add(FinTaxLiabilityReport);
					 }
				}
				 
				 
				/*List<FinTaxDetailedSummaryPojo> sortedList = taxDetailedSummaryList.stream()
				            .sorted(Comparator.comparingInt(FinTaxDetailedSummaryPojo::getBox))
				            .collect(Collectors.toList());*/
				 List<FinTaxLiabilityReportPojo> transform = FinTaxLiabilityReportPojolist.stream()
				            .collect(Collectors.groupingBy(foo -> foo.getTaxname()))
				            .entrySet().stream()
				            .map(e -> e.getValue().stream()
				                .reduce((f1,f2) -> new FinTaxLiabilityReportPojo(f1.getBoxname(),f1.getTaxname(),f1.getNetamount()+f2.getNetamount(),f1.getAmount()+f2.getAmount())))
				                .map(f -> f.get())
				                .collect(Collectors.toList());
				 System.out.println(transform);
				 PlantMstDAO _PlantMstDAO = new PlantMstDAO();
				 ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
				 Map plntMap = (Map) plntList.get(0);
				 String PLNTDESC = (String) plntMap.get("PLNTDESC");
				
				 workbook = populateExcelFprRecForTaxLiability(transform,asOfDate,PLNTDESC);
				 
				 
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
					workbook.write(outByteStream);
					byte[] outArray = outByteStream.toByteArray();
					resp.setContentType("application/ms-excel");
					resp.setContentLength(outArray.length);
					resp.setHeader("Expires:", "0"); // eliminates browser caching
					resp.setHeader("Content-Disposition", "attachment; filename=TaxLiabilityReport.xls");
					OutputStream outStream = resp.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
					outStream.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("transactionSummaryForSGExcel")) {
			String taxheaderid = StrUtils.fString(req.getParameter("taxheaderid")).trim();
			String company = StrUtils.fString(req.getParameter("company")).trim();
			String heading = StrUtils.fString(req.getParameter("heading")).trim();
			String fromtodate = StrUtils.fString(req.getParameter("fromtodate")).trim();
			String box=StrUtils.fString(req.getParameter("box")).trim();
			HSSFWorkbook workbook = null;
			List<TaxReturnTransactionSummary> tranSummaryList= new ArrayList<>();
			TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
			JSONArray jarray=new JSONArray();
			List<TaxReturnFillDet> taxReturnDetailList = new ArrayList<>();
			if(box.equalsIgnoreCase("6"))
			{
				try {
					String query="BOX IN ('1','6')";
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBoxes(plant,taxheaderid,query);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(box.equalsIgnoreCase("7"))
			{
				try {
					String query="BOX IN ('5','7')";
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBoxes(plant,taxheaderid,query);
					taxReturnDetailList = taxReturnDetailList.stream().filter((r)->r.getTAX_AMOUNT()!=0).collect(Collectors.toList());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					taxReturnDetailList = taxReturnDAO.getAllTaxReturnDetByBox(plant,taxheaderid,box);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			List<TaxReturnFillDet> taxReturnDetailListMerged=taxReturnDetailList;
			taxReturnDetailListMerged.forEach(transDet->{
				TaxReturnTransactionSummary tranSummary=new TaxReturnTransactionSummary();
				tranSummary.setDATE(transDet.getDATE());
				tranSummary.setENTRY(transDet.getTRANSACTION_ID());
				tranSummary.setTAXABLE_AMOUNT(transDet.getTAXABLE_AMOUNT());
				tranSummary.setTAX_AMOUNT(transDet.getTAX_AMOUNT());
				tranSummary.setTRANSACTION_TYPE(transDet.getTRANSACTION_TYPE());
				tranSummaryList.add(tranSummary);
			});
			
			workbook = gettransactionSummaryexcel(tranSummaryList, company, heading, fromtodate);			 
			 
			 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				workbook.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				resp.setContentType("application/ms-excel");
				resp.setContentLength(outArray.length);
				resp.setHeader("Expires:", "0"); // eliminates browser caching
				resp.setHeader("Content-Disposition", "attachment; filename=Transactionsummary.xls");
				OutputStream outStream = resp.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
		}
	}
	
	public HSSFWorkbook gettransactionSummaryexcel(List<TaxReturnTransactionSummary> tranSummaryList,String company,String heading,String fromtodate)
			throws JSONException {
		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("Transactionsummary");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);
		
		CellStyle rightAligned = workbook.createCellStyle();
		rightAligned.setAlignment(CellStyle.ALIGN_RIGHT);
		
		CellStyle rightAlignedBOLD = workbook.createCellStyle();
		rightAlignedBOLD.setAlignment(CellStyle.ALIGN_RIGHT);
		rightAlignedBOLD.setFont(my_font);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(company);
		cell.setCellStyle(my_style);
		
		spreadsheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
		
		HSSFRow rowh = spreadsheet.createRow(2);
		HSSFCell cellh = rowh.createCell(0);
		cellh.setCellValue(heading);
		cellh.setCellStyle(my_style);
		
		spreadsheet.addMergedRegion(new CellRangeAddress(2,2,0,3));

		HSSFRow rowh1 = spreadsheet.createRow(4);
		HSSFCell cellh1 = rowh1.createCell(0);
		cellh1.setCellValue(fromtodate);
		cellh1.setCellStyle(my_style);
		
		spreadsheet.addMergedRegion(new CellRangeAddress(4,4,0,3));

		
		int dataRow = 6;
		
		HSSFRow row2 = spreadsheet.createRow(dataRow);

		HSSFCell hb1 = row2.createCell(0);
		hb1.setCellValue("DATE");
		hb1.setCellStyle(my_style);
		
		HSSFCell hb2 = row2.createCell(1);
		hb2.setCellValue("ENTRY#");
		hb2.setCellStyle(my_style);
		
		HSSFCell hb3 = row2.createCell(2);
		hb3.setCellValue("TRANSACTION TYPE");
		hb3.setCellStyle(my_style);
		
		HSSFCell hb4 = row2.createCell(3);
		hb4.setCellValue("TAXABLE AMOUNT");
		hb4.setCellStyle(rightAlignedBOLD);
		
		HSSFCell hb5 = row2.createCell(4);
		hb5.setCellValue("TAX AMOUNT");
		hb5.setCellStyle(rightAlignedBOLD);
		
		dataRow++;
		for (TaxReturnTransactionSummary taxReturnTransactionSummary : tranSummaryList) {
				 
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(taxReturnTransactionSummary.getDATE()));
					
					HSSFCell b2 = row3.createCell(1);
					b2.setCellValue(new HSSFRichTextString(taxReturnTransactionSummary.getENTRY()));
					
					HSSFCell b3 = row3.createCell(2);
					b3.setCellValue(new HSSFRichTextString(taxReturnTransactionSummary.getTRANSACTION_TYPE()));

					HSSFCell b4 = row3.createCell(3);
					b4.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(taxReturnTransactionSummary.getTAXABLE_AMOUNT(), "2")));
					b4.setCellStyle(rightAligned);
					
					HSSFCell b5 = row3.createCell(4);
					b5.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(taxReturnTransactionSummary.getTAX_AMOUNT(), "2")));
					b5.setCellStyle(rightAligned);
					
					dataRow++;
			
		}
		

		return workbook;
	}
	
	public HSSFWorkbook populateExcelFprRecForTaxLiability(List<FinTaxLiabilityReportPojo> gstTaxDetsummary,String asofdate,String pladesc)
			throws JSONException {
		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("TaxLiabilityReport");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);
		
		CellStyle rightAligned = workbook.createCellStyle();
		rightAligned.setAlignment(CellStyle.ALIGN_RIGHT);
		
		CellStyle rightAlignedBOLD = workbook.createCellStyle();
		rightAlignedBOLD.setAlignment(CellStyle.ALIGN_RIGHT);
		rightAlignedBOLD.setFont(my_font);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(pladesc);
		cell.setCellStyle(my_style);
		
		HSSFRow rowh = spreadsheet.createRow(2);
		HSSFCell cellh = rowh.createCell(0);
		cellh.setCellValue("GST Detail Report As of "+asofdate);
		cellh.setCellStyle(my_style);
		

		HSSFRow row2 = spreadsheet.createRow(4);

		
		HSSFCell hb2 = row2.createCell(1);
		hb2.setCellValue("NET AMOUNT");
		hb2.setCellStyle(my_style);
		
		HSSFCell hb3 = row2.createCell(2);
		hb3.setCellValue("GST AMOUNT");
		hb3.setCellStyle(my_style);
		
		int dataRow = 5;
		
		String boxname = "";
		boolean bstatus = false;
		
		for (FinTaxLiabilityReportPojo TaxDetsummary : gstTaxDetsummary) {
				 boxname = TaxDetsummary.getBoxname();
				 bstatus = true;
		 }
		
		if(bstatus) {
		
			HSSFRow rowbox1 = spreadsheet.createRow(dataRow);
			HSSFCell box10 = rowbox1.createCell(0);
			box10.setCellValue(new HSSFRichTextString(boxname));
			
			spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,0,2));
			
			dataRow++;
			for (FinTaxLiabilityReportPojo TaxDetsummary : gstTaxDetsummary) {
				 
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxname()));

					HSSFCell b7 = row3.createCell(1);
					b7.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(TaxDetsummary.getNetamount(), "2")));
					b7.setCellStyle(rightAligned);
					HSSFCell b8 = row3.createCell(2);
					b8.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(TaxDetsummary.getAmount(), "2")));
					b8.setCellStyle(rightAligned);
					
					dataRow++;
			
			 }
		
		}

		return workbook;
	}
	
	
	public HSSFWorkbook populateExcelFprRecForDetail(List<FinTaxDetailedSummaryPojo> gstTaxDetsummary,String asofdate,String pladesc)
			throws JSONException {
		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("GSTSummaryReport");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);
		
		CellStyle rightAligned = workbook.createCellStyle();
		rightAligned.setAlignment(CellStyle.ALIGN_RIGHT);
		
		CellStyle rightAlignedBOLD = workbook.createCellStyle();
		rightAlignedBOLD.setAlignment(CellStyle.ALIGN_RIGHT);
		rightAlignedBOLD.setFont(my_font);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(pladesc);
		cell.setCellStyle(my_style);
		
		HSSFRow rowh = spreadsheet.createRow(2);
		HSSFCell cellh = rowh.createCell(0);
		cellh.setCellValue("GST Detail Report As of "+asofdate);
		cellh.setCellStyle(my_style);
		
		/*HSSFRow rowb = spreadsheet.createRow(3);
		HSSFCell cellb = rowb.createCell(0);
		cellb.setCellValue("As of "+asofdate);
		cellb.setCellStyle(my_style);*/
		/*HSSFCell cellbn = rowb.createCell(1);
		cellbn.setCellValue(asofdate);*/


		HSSFRow row2 = spreadsheet.createRow(4);
		HSSFCell hb1 = row2.createCell(0);
		hb1.setCellValue("DATE");
		hb1.setCellStyle(my_style);
		
		HSSFCell hb2 = row2.createCell(1);
		hb2.setCellValue("TRANSACTION TYPE");
		hb2.setCellStyle(my_style);
		
		HSSFCell hb3 = row2.createCell(2);
		hb3.setCellValue("TRANSACTION ID");
		hb3.setCellStyle(my_style);
		
		HSSFCell hb4 = row2.createCell(3);
		hb4.setCellValue("NAME");
		hb4.setCellStyle(my_style);
		
		HSSFCell hb5 = row2.createCell(4);
		hb5.setCellValue("GST CODE");
		hb5.setCellStyle(my_style);
		
		HSSFCell hb6 = row2.createCell(5);
		hb6.setCellValue("GST RATE");
		hb6.setCellStyle(my_style);
		
		HSSFCell hb7 = row2.createCell(6);
		hb7.setCellValue("NET AMOUNT");
		hb7.setCellStyle(my_style);
		hb7.setCellStyle(rightAlignedBOLD);
		
		HSSFCell hb8 = row2.createCell(7);
		hb8.setCellValue("AMOUNT");
		hb8.setCellStyle(my_style);
		hb8.setCellStyle(rightAlignedBOLD);
		
		HSSFCell hb9 = row2.createCell(8);
		hb9.setCellValue("BALANCE");
		hb9.setCellStyle(my_style);
		hb9.setCellStyle(rightAlignedBOLD);
		
		int dataRow = 5;
		
		String boxname = "";
		double boxamount = 0.0;
		boolean bstatus = false;
		
		for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
			 int box = TaxDetsummary.getBox();
			 if(box == 1) {
				 boxname = TaxDetsummary.getBoxname();
				 boxamount = boxamount + Double.valueOf(TaxDetsummary.getAmount());
				 bstatus = true;
			 }
		 }
		
		if(bstatus) {
		
			HSSFRow rowbox1 = spreadsheet.createRow(dataRow);
			HSSFCell box10 = rowbox1.createCell(0);
			box10.setCellValue(new HSSFRichTextString(boxname));
			
			spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,0,7));
		
			HSSFCell box18 = rowbox1.createCell(8);
			box18.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(boxamount, "2")));
			box18.setCellStyle(rightAligned);
			
			dataRow++;
			for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
				 int box = TaxDetsummary.getBox();
				 if(box == 1) {
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxdate()));
					
					HSSFCell b2 = row3.createCell(1);
					b2.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactiontype()));
					
					HSSFCell b3 = row3.createCell(2);
					b3.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactionid()));
					
					HSSFCell b4 = row3.createCell(3);
					b4.setCellValue(new HSSFRichTextString(TaxDetsummary.getName()));
					
					HSSFCell b5 = row3.createCell(4);
					b5.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxname()));
					
					HSSFCell b6 = row3.createCell(5);
					b6.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxrate()));
					
					if(TaxDetsummary.getNetamount().equalsIgnoreCase("") || TaxDetsummary.getNetamount() == null) {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(TaxDetsummary.getNetamount()));
						b7.setCellStyle(rightAligned);
					}else {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getNetamount()), "2")));
						b7.setCellStyle(rightAligned);
					}
					
					HSSFCell b8 = row3.createCell(7);
					b8.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getAmount()), "2")));
					b8.setCellStyle(rightAligned);
					
					HSSFCell b9 = row3.createCell(8);
					b9.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getBalance()), "2")));
					b9.setCellStyle(rightAligned);
					
					dataRow++;
				 }
			 }
		
		}
		
		
		dataRow++;
		
		boxname = "";
		boxamount = 0.0;
		bstatus = false;
		
		for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
			 int box = TaxDetsummary.getBox();
			 if(box == 2) {
				 boxname = TaxDetsummary.getBoxname();
				 boxamount = boxamount + Double.valueOf(TaxDetsummary.getAmount());
				 bstatus = true;
			 }
		 }
		
		if(bstatus) {
		
			HSSFRow rowbox1 = spreadsheet.createRow(dataRow);
			HSSFCell box10 = rowbox1.createCell(0);
			box10.setCellValue(new HSSFRichTextString(boxname));
			
			spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,0,7));
		
			HSSFCell box18 = rowbox1.createCell(8);
			box18.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(boxamount, "2")));
			box18.setCellStyle(rightAligned);
			
			dataRow++;
			for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
				 int box = TaxDetsummary.getBox();
				 if(box == 2) {
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxdate()));
					
					HSSFCell b2 = row3.createCell(1);
					b2.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactiontype()));
					
					HSSFCell b3 = row3.createCell(2);
					b3.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactionid()));
					
					HSSFCell b4 = row3.createCell(3);
					b4.setCellValue(new HSSFRichTextString(TaxDetsummary.getName()));
					
					HSSFCell b5 = row3.createCell(4);
					b5.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxname()));
					
					HSSFCell b6 = row3.createCell(5);
					b6.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxrate()));
					 
					if(TaxDetsummary.getNetamount().equalsIgnoreCase("") || TaxDetsummary.getNetamount() == null) {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(TaxDetsummary.getNetamount()));
						b7.setCellStyle(rightAligned);
					}else {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getNetamount()), "2")));
						b7.setCellStyle(rightAligned);
					}
					
					HSSFCell b8 = row3.createCell(7);
					b8.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getAmount()), "2")));
					b8.setCellStyle(rightAligned);
					
					HSSFCell b9 = row3.createCell(8);
					b9.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getBalance()), "2")));
					b9.setCellStyle(rightAligned);
					
					dataRow++;
				 }
			 }
		
		}
		
		dataRow++;
		
		boxname = "";
		boxamount = 0.0;
		bstatus = false;
		
		for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
			 int box = TaxDetsummary.getBox();
			 if(box == 3) {
				 boxname = TaxDetsummary.getBoxname();
				 boxamount = boxamount + Double.valueOf(TaxDetsummary.getAmount());
				 bstatus = true;
			 }
		 }
		
		if(bstatus) {
		
			HSSFRow rowbox1 = spreadsheet.createRow(dataRow);
			HSSFCell box10 = rowbox1.createCell(0);
			box10.setCellValue(new HSSFRichTextString(boxname));
			
			spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,0,7));
		
			HSSFCell box18 = rowbox1.createCell(8);
			box18.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(boxamount, "2")));
			box18.setCellStyle(rightAligned);
			
			dataRow++;
			for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
				 int box = TaxDetsummary.getBox();
				 if(box == 3) {
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxdate()));
					
					HSSFCell b2 = row3.createCell(1);
					b2.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactiontype()));
					
					HSSFCell b3 = row3.createCell(2);
					b3.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactionid()));
					
					HSSFCell b4 = row3.createCell(3);
					b4.setCellValue(new HSSFRichTextString(TaxDetsummary.getName()));
					
					HSSFCell b5 = row3.createCell(4);
					b5.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxname()));
					
					HSSFCell b6 = row3.createCell(5);
					b6.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxrate()));
					 
					if(TaxDetsummary.getNetamount().equalsIgnoreCase("") || TaxDetsummary.getNetamount() == null) {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(TaxDetsummary.getNetamount()));
						b7.setCellStyle(rightAligned);
					}else {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getNetamount()), "2")));
						b7.setCellStyle(rightAligned);
					}
					
					HSSFCell b8 = row3.createCell(7);
					b8.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getAmount()), "2")));
					b8.setCellStyle(rightAligned);
					
					HSSFCell b9 = row3.createCell(8);
					b9.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getBalance()), "2")));
					b9.setCellStyle(rightAligned);
					
					dataRow++;
				 }
			 }
		
		}
		
		dataRow++;
		
		boxname = "";
		boxamount = 0.0;
		bstatus = false;
		
		for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
			 int box = TaxDetsummary.getBox();
			 if(box == 5) {
				 boxname = TaxDetsummary.getBoxname();
				 boxamount = boxamount + Double.valueOf(TaxDetsummary.getAmount());
				 bstatus = true;
			 }
		 }
		
		if(bstatus) {
		
			HSSFRow rowbox1 = spreadsheet.createRow(dataRow);
			HSSFCell box10 = rowbox1.createCell(0);
			box10.setCellValue(new HSSFRichTextString(boxname));
			
			spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,0,7));
		
			HSSFCell box18 = rowbox1.createCell(8);
			box18.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(boxamount, "2")));
			box18.setCellStyle(rightAligned);
			
			dataRow++;
			for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
				 int box = TaxDetsummary.getBox();
				 if(box == 5) {
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxdate()));
					
					HSSFCell b2 = row3.createCell(1);
					b2.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactiontype()));
					
					HSSFCell b3 = row3.createCell(2);
					b3.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactionid()));
					
					HSSFCell b4 = row3.createCell(3);
					b4.setCellValue(new HSSFRichTextString(TaxDetsummary.getName()));
					
					HSSFCell b5 = row3.createCell(4);
					b5.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxname()));
					
					HSSFCell b6 = row3.createCell(5);
					b6.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxrate()));
					 
					if(TaxDetsummary.getNetamount().equalsIgnoreCase("") || TaxDetsummary.getNetamount() == null) {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(TaxDetsummary.getNetamount()));
						b7.setCellStyle(rightAligned);
					}else {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getNetamount()), "2")));
						b7.setCellStyle(rightAligned);
					}
					
					HSSFCell b8 = row3.createCell(7);
					b8.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getAmount()), "2")));
					b8.setCellStyle(rightAligned);
					
					HSSFCell b9 = row3.createCell(8);
					b9.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getBalance()), "2")));
					b9.setCellStyle(rightAligned);
					
					dataRow++;
				 }
			 }
		
		}
		
		dataRow++;
		
		boxname = "";
		boxamount = 0.0;
		bstatus = false;
		
		for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
			 int box = TaxDetsummary.getBox();
			 if(box == 6) {
				 boxname = TaxDetsummary.getBoxname();
				 boxamount = boxamount + Double.valueOf(TaxDetsummary.getAmount());
				 bstatus = true;
			 }
		 }
		
		if(bstatus) {
		
			HSSFRow rowbox1 = spreadsheet.createRow(dataRow);
			HSSFCell box10 = rowbox1.createCell(0);
			box10.setCellValue(new HSSFRichTextString(boxname));
			
			spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,0,7));
		
			HSSFCell box18 = rowbox1.createCell(8);
			box18.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(boxamount, "2")));
			box18.setCellStyle(rightAligned);
			
			dataRow++;
			for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
				 int box = TaxDetsummary.getBox();
				 if(box == 6) {
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxdate()));
					
					HSSFCell b2 = row3.createCell(1);
					b2.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactiontype()));
					
					HSSFCell b3 = row3.createCell(2);
					b3.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactionid()));
					
					HSSFCell b4 = row3.createCell(3);
					b4.setCellValue(new HSSFRichTextString(TaxDetsummary.getName()));
					
					HSSFCell b5 = row3.createCell(4);
					b5.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxname()));
					
					HSSFCell b6 = row3.createCell(5);
					b6.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxrate()));

					if(TaxDetsummary.getNetamount().equalsIgnoreCase("") || TaxDetsummary.getNetamount() == null) {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(""));
						b7.setCellStyle(rightAligned);
					}else {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getNetamount()), "2")));
						b7.setCellStyle(rightAligned);
					}
					
					HSSFCell b8 = row3.createCell(7);
					b8.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getAmount()), "2")));
					b8.setCellStyle(rightAligned);
					
					HSSFCell b9 = row3.createCell(8);
					b9.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getBalance()), "2")));
					b9.setCellStyle(rightAligned);
					
					dataRow++;
				 }
			 }
		
		}
		
		dataRow++;
		
		boxname = "";
		boxamount = 0.0;
		bstatus = false;
		
		for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
			 int box = TaxDetsummary.getBox();
			 if(box == 7) {
				 boxname = TaxDetsummary.getBoxname();
				 boxamount = boxamount + Double.valueOf(TaxDetsummary.getAmount());
				 bstatus = true;
			 }
		 }
		
		if(bstatus) {
		
			HSSFRow rowbox1 = spreadsheet.createRow(dataRow);
			HSSFCell box10 = rowbox1.createCell(0);
			box10.setCellValue(new HSSFRichTextString(boxname));
			
			spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,0,7));
		
			HSSFCell box18 = rowbox1.createCell(8);
			box18.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(boxamount, "2")));
			box18.setCellStyle(rightAligned);
			
			dataRow++;
			for (FinTaxDetailedSummaryPojo TaxDetsummary : gstTaxDetsummary) {
				 int box = TaxDetsummary.getBox();
				 if(box == 7) {
					HSSFRow row3 = spreadsheet.createRow(dataRow);
					HSSFCell b1 = row3.createCell(0);
					b1.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxdate()));
					
					HSSFCell b2 = row3.createCell(1);
					b2.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactiontype()));
					
					HSSFCell b3 = row3.createCell(2);
					b3.setCellValue(new HSSFRichTextString(TaxDetsummary.getTransactionid()));
					
					HSSFCell b4 = row3.createCell(3);
					b4.setCellValue(new HSSFRichTextString(TaxDetsummary.getName()));
					
					HSSFCell b5 = row3.createCell(4);
					b5.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxname()));
					
					HSSFCell b6 = row3.createCell(5);
					b6.setCellValue(new HSSFRichTextString(TaxDetsummary.getTaxrate()));
					 
					if(TaxDetsummary.getNetamount().equalsIgnoreCase("") || TaxDetsummary.getNetamount() == null) {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(TaxDetsummary.getNetamount()));
						b7.setCellStyle(rightAligned);
					}else {
						HSSFCell b7 = row3.createCell(6);
						b7.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getNetamount()), "2")));
						b7.setCellStyle(rightAligned);
					}
					
					HSSFCell b8 = row3.createCell(7);
					b8.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getAmount()), "2")));
					b8.setCellStyle(rightAligned);
					
					HSSFCell b9 = row3.createCell(8);
					b9.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(TaxDetsummary.getBalance()), "2")));
					b9.setCellStyle(rightAligned);
					
					dataRow++;
				 }
			 }
		
		}

		return workbook;
	}

	public HSSFWorkbook populateExcelFprRec(List<Map<String,String>> taxReturnFiling,String asofdate,String pladesc)
			throws JSONException {
		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("GSTSummaryReport");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);
		
		CellStyle rightAligned = workbook.createCellStyle();
		rightAligned.setAlignment(CellStyle.ALIGN_RIGHT);
		
		CellStyle rightAlignedBOLD = workbook.createCellStyle();
		rightAlignedBOLD.setAlignment(CellStyle.ALIGN_RIGHT);
		rightAlignedBOLD.setFont(my_font);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(pladesc);
		cell.setCellStyle(my_style);
		
		HSSFRow rowh = spreadsheet.createRow(2);
		HSSFCell cellh = rowh.createCell(0);
		cellh.setCellValue("GST Summary Report");
		cellh.setCellStyle(my_style);
		
		HSSFRow rowb = spreadsheet.createRow(3);
		HSSFCell cellb = rowb.createCell(0);
		cellb.setCellValue("As of "+asofdate);
		cellb.setCellStyle(my_style);
		/*HSSFCell cellbn = rowb.createCell(1);
		cellbn.setCellValue(asofdate);*/


		HSSFRow row2 = spreadsheet.createRow(5);
		HSSFCell dateheader = row2.createCell(0);
		dateheader.setCellValue("");
		dateheader.setCellStyle(my_style);
		
		HSSFCell particularsHeader = row2.createCell(1);
		particularsHeader.setCellValue("TOTAL");
		particularsHeader.setCellStyle(my_style);
		particularsHeader.setCellStyle(rightAlignedBOLD);
		
		int dataRow = 6;
		

		
		for(Map<String,String> taxreturn:taxReturnFiling)
		{
			String tamot = taxreturn.get("tamount");
			String cur = taxreturn.get("CURRENCY");
			double tamount = Double.valueOf(taxreturn.get("tamount"));
			if(tamount >= 0) {
				tamot = cur + tamot;
			}else {
				tamot = "-"+cur + tamot.replace("-", "");
			}
			
			HSSFRow row3 = spreadsheet.createRow(dataRow);
			if(taxreturn.get("BOX").equalsIgnoreCase(""))
			{
				HSSFCell datedet = row3.createCell(0);
				datedet.setCellValue(new HSSFRichTextString(StrUtils.fString(taxreturn.get("DESC_HEADING"))));
			}else {
				HSSFCell datedet = row3.createCell(0);
				datedet.setCellValue(new HSSFRichTextString("BOX "+StrUtils.fString(taxreturn.get("BOX"))+" "+StrUtils.fString(taxreturn.get("DESC_HEADING"))));
			}
			HSSFCell particular = row3.createCell(1);
			particular.setCellValue(new HSSFRichTextString(tamot));
			particular.setCellStyle(rightAligned);
			dataRow++;
		}
		
		

		return workbook;
	}
	
	
  private boolean calculateTaxHeaderAmounts(String plant,String taxheaderid) throws Exception
  {
	  boolean success=false;
	  Numbers numberUtil=new Numbers();
		int scale=2;
		RoundingMode rm=RoundingMode.HALF_UP;
	  Double TOTAL_TAXPAYABLE=0.00;
		Double totalSalesTax=0.00;
		Double totalExpenseTax=0.00;
		TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
		List<TaxReturnFillDet> taxreturndetListAll=taxReturnDAO.getAllTaxReturnDet(plant, taxheaderid);
		for(TaxReturnFillDet taxDet:taxreturndetListAll)
		{
			if(taxDet.getBOX().equalsIgnoreCase("9"))
			{
				totalExpenseTax+=taxDet.getTAX_AMOUNT();
			}
			else if(taxDet.getBOX().equalsIgnoreCase("3") || taxDet.getBOX().equalsIgnoreCase("6") || taxDet.getBOX().equalsIgnoreCase("7"))
			{
				totalSalesTax+=taxDet.getTAX_AMOUNT();
				totalExpenseTax+=taxDet.getTAX_AMOUNT();
			}
			else
			{
				totalSalesTax+=taxDet.getTAX_AMOUNT();
			}
			//TOTAL_TAXPAYABLE+=taxDet.getTAX_AMOUNT();
			
		}
		TOTAL_TAXPAYABLE=totalSalesTax-totalExpenseTax;
		String updateItems="TOTAL_TAXPAYABLE="+numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm);
		taxReturnDAO.updateTaxHeader(plant, taxheaderid, updateItems);
		taxReturnDAO.updateTaxBalanceDue(plant, taxheaderid, numberUtil.Round(TOTAL_TAXPAYABLE, scale, rm));
		success=true;
		return success;
  }

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
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
