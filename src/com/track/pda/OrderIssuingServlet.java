package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BomDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DNPLDetDAO;
import com.track.dao.DNPLHdrDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrAprrovalDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PayTermsDAO;
import com.track.dao.PaymentModeMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.PrdBrandDAO;
import com.track.dao.PrdClassDAO;
import com.track.dao.PrdDeptDAO;
import com.track.dao.PrdTypeDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.dao.TransportModeDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.DoHdrApproval;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.PoDet;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoHdr;
import com.track.db.object.ToDet;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.FieldCopier;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POSUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PrdBrandUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdDeptUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UomUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.service.Costofgoods;
import com.track.service.JournalService;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.servlet.DynamicFileServlet;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;
import com.track.util.XMLUtils;

import net.sf.json.JSONObject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class OrderIssuingServlet extends DynamicFileServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.OrderIssuingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.OrderIssuingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = -529519669783954856L;
	private DOUtil _DOUtil = null;
	private InvMstUtil _InvMstUtil = null;
//	private DateUtils dateUtils = null;
	

	private String PLANT = "";

	private String xmlStr = "";
	private String action = "";
	String autoinverr="";
//	StrUtils strUtils = new StrUtils();

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_DOUtil = new DOUtil();
		_InvMstUtil = new InvMstUtil();
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		try {

			action = request.getParameter("action").trim();
			String rflag = StrUtils.fString(request.getParameter("RFLAG"));
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_DOUtil.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("load_all_open_do")) {
				xmlStr = "";
				xmlStr = load_all_open_do(request, response);
			}
			if (action.equalsIgnoreCase("load_all_open_complete_do_for_print")) {
				xmlStr = "";
				xmlStr = load_all_open_complete_do_for_print(request, response);
			}
			if (action.equalsIgnoreCase("load_all_do_items")) {
				xmlStr = "";
				xmlStr = load_all_do_items(request, response);
			}
			if (action.equalsIgnoreCase("load_lotdetails")) {
				xmlStr = "";
				xmlStr = load_lotdetails(request, response);
			} else if (action.equalsIgnoreCase("View")) {
				if (rflag.equals("1")) {
//					boolean flag = 
					DisplayOutGoingIssueData(request, response);
					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					response.sendRedirect("../salestransaction/orderissue?DONO="
							+ dono + "&action=View");
				} else if (rflag.equals("2")) {
//					boolean flag = 
					DisplayOutGoingIssueData(request, response);
					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					request.getSession().setAttribute("dono", "");
					response.sendRedirect("jsp/OutBoundOrderSummary.jsp?DONO="
							+ dono + "&action=View");
				} else if (rflag.equals("3")) {
//					boolean flag = 
					DisplayOutGoingIssueData(request, response);
					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					request.getSession().setAttribute("dono", "");
					response.sendRedirect("../salestransaction/orderpickreturn?DONO="
							+ dono + "&action=View");
				} else if (rflag.equals("4")) {
//					boolean flag =	
					DisplayOutGoingIssueData(request, response);
					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					request.getSession().setAttribute("dono", "");
					response.sendRedirect("jsp/DOOutBoundsOrderIssueConfirm.jsp?DONO="
							+ dono + "&action=View");
				} 
				else if (rflag.equals("5")) {
//					boolean flag = 
					DisplayOutGoingIssueData(request, response);
					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					response.sendRedirect("../salestransaction/orderpickissue?DONO="
							+ dono + "&action=View");
				}
				else if (rflag.equals("6")) {
//					boolean flag = 
					DisplayOutGoingIssueData(request, response);
					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					response.sendRedirect("jsp/OutboundOrderIssueReversal.jsp?DONO="
							+ dono + "&action=View");
				}
				else if (rflag.equals("7")) {
					//boolean flag = dnpl(request, response);
//					HttpSession session = request.getSession();
					
					String dono = StrUtils.fString(request.getParameter("DONO"));
					/*if(dono.length()==0)
						dono = StrUtils.fString(
								(String) session.getAttribute("dono")).trim();*/
					String invoiceNo = StrUtils.fString(request.getParameter("INVOICENO"));
					String gino = StrUtils.fString(request.getParameter("GINO"));
					String CashCust = StrUtils.fString(request.getParameter("CashCust"));
					String custName = StrUtils.fString(request.getParameter("CUST_NAME"));
					if(custName.isEmpty())
						custName = StrUtils.fString(request.getParameter("CUSTOMER"));
					String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
					response.sendRedirect("../salestransaction/createpackinglist?DONO="
							+ dono + "&INVOICENO=" + invoiceNo + "&GINO=" + gino + "&custName=" + custName + "&custCode=" + custCode + "&action=View&CashCust="+CashCust);
				}
                    else {
//					boolean flag = 
					DisplayOutGoingIssueData(request, response);
					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					request.getSession().setAttribute("dono", "");
					response.sendRedirect("jsp/OutBoundOrderSummary.jsp?DONO="
							+ dono + "&action=View");
				}

			}
			 else if (action.equalsIgnoreCase("MultipleView")) {
					if (rflag.equals("1")) {
//						boolean flag = 
						DisplayOutGoingIssueData(request, response);
						String dono = "";
						dono = StrUtils.fString(request.getParameter("DONO"));

						response.sendRedirect("../salestransaction/orderissue?DONO="
								+ dono + "&action=View");
					} else if (rflag.equals("2")) {
						//boolean flag = 
						DisplayOutGoingIssueData(request, response);
						String dono = "";
						dono = StrUtils.fString(request.getParameter("DONO"));

						request.getSession().setAttribute("dono", "");
						response.sendRedirect("../salestransaction/orderpicksummary?DONO="
								+ dono + "&action=View");
					} else if (rflag.equals("3")) {
//						boolean flag = 
						DisplayOutGoingIssueData(request, response);
						String dono = "";
						dono = StrUtils.fString(request.getParameter("DONO"));

						request.getSession().setAttribute("dono", "");
						response.sendRedirect("../salestransaction/orderpickreturn?DONO="
								+ dono + "&action=View");
					
					
					}  else if (rflag.equals("4")) {
						//boolean flag = 
						DisplayOutGoingIssueData(request, response);
						String dono = "";
						dono = StrUtils.fString(request.getParameter("DONO"));

						request.getSession().setAttribute("dono", "");
						response.sendRedirect("jsp/DOSummaryForSingleStepPickIssue.jsp?DONO="
								+ dono + "&action=View");
					
					}  else if (rflag.equals("5")) {
                                                    //boolean flag = 
                                                    DisplayMobileOrderData(request, response);
                                                    String dono = "";
                                                    dono = StrUtils.fString(request.getParameter("DONO"));

                                                    request.getSession().setAttribute("dono", "");
                                                    response.sendRedirect("jsp/mobileShoppingFullfillment.jsp?DONO="
                                                                    + dono + "&action=View");
                                            
                                            } else if (rflag.equals("6")) {
                                                    //boolean flag = 
                                                    DisplayMobileOrderData(request, response);
                                                    String dono = "";
                                                    dono = StrUtils.fString(request.getParameter("DONO"));

                                                    request.getSession().setAttribute("dono", "");
                                                    response.sendRedirect("jsp/mobileShopWOInv.jsp?DONO="
                                                                    + dono + "&action=View");
                                            
                                            }else {
						//boolean flag = 
						DisplayOutGoingIssueData(request, response);
						String dono = "";
						dono = StrUtils.fString(request.getParameter("DONO"));

						request.getSession().setAttribute("dono", "");
						response.sendRedirect("../salestransaction/orderpicksummary?DONO="
								+ dono + "&action=View");
					}

				}
                                
		    else if (action.equalsIgnoreCase("MobileEnquiry")) {
		      
		     //boolean flag = 
		     DisplayMobileOrderData(request, response);
		     String dono = "";
		     dono = StrUtils.fString(request.getParameter("DONO"));
		     request.getSession().setAttribute("dono", "");
		     response.sendRedirect("jsp/mobileEnquiryFullfillment.jsp?DONO="+ dono + "&action=View&ORDERTYPE=MOBILE ENQUIRY");
		    }
		    
		        
		    
		        else if (action.equalsIgnoreCase("MobileEnquiryConfirmStatus")) {
		        xmlStr="";
		          xmlStr=  mobileEnquiryConfirm(request, response);
		        }

		    else if (action.equalsIgnoreCase("ViewByRange")) {
		                  
		                           //boolean flag = 
		                           DisplayOutGoingIssueData(request, response);
		                           String dono = "";
		                           dono = StrUtils.fString(request.getParameter("DONO"));

		                           request.getSession().setAttribute("dono", "");
		                           response.sendRedirect("../salestransaction/orderpickserial?DONO="
		                                           + dono + "&action=View");
		                

		           }

			else if (action.equalsIgnoreCase("Picking")) {
				//String xmlStr = 
				OutGoingPickingData(request, response,"PICKING");
			} 
			else if (action.equalsIgnoreCase("MultiPicking")) {
				//String xmlStr = 
				OutGoingPickingData(request, response,"MULTIPLE_PICK");
			}
			else if (action.equalsIgnoreCase("SingleStepPick/Issue")) {
				//String xmlStr = 
				OutGoingPickingData(request, response,"SINGLE_STEP");
			}
		    else if (action.equalsIgnoreCase("Mobile_Shopping")) {
		            //String xmlStr = 
		            OutGoingPickingData(request, response,"MOBILE_SHOPPING");
		    }
		    else if (action.equalsIgnoreCase("Mobile_Shopping_skip_invCheck")) {
		            //String xmlStr = 
		            OutGoingPickingData(request, response,"MOBILE_SHOPPING_WO_INVCHK");
		    }
            else if (action.equalsIgnoreCase("PickByRange")) {
                 //String xmlStr = 
                 OutGoingPickingData(request, response,"PICK_BY_RANGE");
            }else if (action.equalsIgnoreCase("Reverse")) {
				//String xmlStr = 
				process_OutGoingReverseDataByWMS(request, response);
			} else if (action.equalsIgnoreCase("ReverseConfirm")) {
				//String xmlStr = 
				process_OutBoundorderReverseByWMS(request, response);
			} else if (action.equalsIgnoreCase("Cancel")) {
				//boolean flag = 
				DisplayOutGoingIssueData(request, response);
				String dono = "";
				dono = StrUtils.fString(request.getParameter("DONO"));

				request.getSession().setAttribute("dono", "");
				response.sendRedirect("../salestransaction/orderissue?pono="
						+ dono + "&action=View");
			} else if (action.equalsIgnoreCase("Goods Issue")) {
				xmlStr = OutGoingIssueData(request, response);				
			} 
			else if (action.equalsIgnoreCase("BulkIssue")) {
				xmlStr = OutGoingIssueBulkData(request, response);
			}
			else if (action.equalsIgnoreCase("BulkIssueInvoice")) {
				xmlStr = OutGoingIssueBulkDataWithInvoice(request, response,StrUtils.fString(request.getParameter("INVOICENO")));
			}
			else if (action.equalsIgnoreCase("DNPL")) {
				xmlStr = processDNPL(request, response);

			}
			else if (action.equalsIgnoreCase("BulkIssuebyProd")) {
				xmlStr = OutGoingIssueBulkDatabyProd(request, response);

			}
			
			else if (action.equalsIgnoreCase("Confirm Issue")) {
				xmlStr = OutBoundsOrderIssueConfirm(request, response);

			}
		    else if (action.equalsIgnoreCase("Confirm_Issue_For_MobileShop")) {
		            xmlStr = MobileShopOBOrderIssueConfirm(request, response);

		    }
		    
		    else if (action.equalsIgnoreCase("Confirm_Issue_For_MobileShopWOInvcheck")) {
		            xmlStr = MobileShopIssueConfirmWOInvcheck(request, response);

		    }
		    		    
			else if (action.equalsIgnoreCase("Print")) {
//				String xmlStr = "";

				String plant = StrUtils.fString(
						(String) request.getSession().getAttribute("PLANT"))
						.trim();
				String dono = StrUtils.fString(request.getParameter("DONO"));
				String shipno = StrUtils.fString(request
						.getParameter("SHIPPINGNO"));
//				String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();
				DOUtil _doUtil = new DOUtil();
				if (dono.length() > 0) {

					htCond.put("PLANT", plant);
					htCond.put("DONO", dono);

					String query = "dono,custName,custCode,jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
					al = _doUtil.getOutGoingDoHdrDetails(query, htCond);
					if (al.size() > 0) {
						m = (Map) al.get(0);

					} else {

//						fieldDesc = "<tr><td>No Records Available</td></tr>";

					}

				}
				request.getSession().setAttribute("podetVal", m);
				response.sendRedirect("jsp/ShipConfirmPrint.jsp?DONO=" + dono
						+ "&action=View&SHIPPINGNO=" + shipno);

			}
			
			else if (action.equalsIgnoreCase("ViewProductOrders")) {

				String item = request.getParameter("ITEM");
                                ItemMstDAO itemdao = new ItemMstDAO();
                                HttpSession session = request.getSession();
                                String plant = (String)session.getAttribute("PLANT");
                                String desc = itemdao.getItemDesc(plant,item);
                                desc=StrUtils.replaceCharacters2Send(desc);
			    response.sendRedirect("../salestransaction/orderpickissuebyproduct?ITEM="
			                    + item + "&DESC="+desc+"&action=View");
			} 
			
			else if (action.equalsIgnoreCase("OBISSUEReverseConfirm")) {
				xmlStr = "";
				xmlStr = process_OutboundOrderIssueReversal(request, response);
			}
			else if (action.equalsIgnoreCase("BulkPickIssuebyOrders")) {
				xmlStr = "";
				xmlStr = OutGoingDataBulkbyOrders(request, response);

			}
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String load_all_open_do(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			str = _DOUtil.load_all_open_do_details_xml(PLANT);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_all_open_complete_do_for_print(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			str = _DOUtil.load_all_open_complete_do_for_print_details_xml(PLANT);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}

	private String load_all_do_items(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aDoNum = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aDoNum = StrUtils.fString(request.getParameter("DO_NUM"));

			str = _DOUtil.load_all_do_items_xml(aPlant, aDoNum);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return str;
	}

//	private String load_do_detais(HttpServletRequest request,
//			HttpServletResponse response) throws IOException, ServletException,
//			Exception {
//		String str = "", aPlant = "";
//		try {
//
//			PLANT = StrUtils.fString(request.getParameter("PLANT"));
//			str = _DOUtil.load_all_open_do_details_xml(PLANT);
//
//			if (str.equalsIgnoreCase("")) {
//				str = XMLUtils
//						.getXMLMessage(1, "No open outgoing order found ");
//			}
//		} catch (Exception e) {
//			this.mLogger.exception(this.printLog, "", e);
//			throw e;
//		}
//		return str;
//	}

	private String load_lotdetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "", aBatch = "", aLoc = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM"));
			aBatch = StrUtils.fString(request.getParameter("BATCH"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));

			str = _InvMstUtil.load_lotdetails_xml(aPlant, aItem, aBatch, aLoc,
					"");

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No open outgoing order found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return str;
	}

	private boolean dnpl(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		HttpSession session = request.getSession();
		String plant = StrUtils.fString(
				(String) session.getAttribute("PLANT")).trim();
		String dono = StrUtils.fString(request.getParameter("DONO"));
		String invoiceNo = StrUtils.fString(request.getParameter("INVOICENO"));
		String CashCust = StrUtils.fString(request.getParameter("TAXINVOICE"));
		String fieldDesc = "";

		if (invoiceNo.length() > 0) {
			Hashtable<String, String> htCondition = new Hashtable<>();
			  htCondition.put("PLANT", plant);		
			ShipHisDAO  _ShipHisDAO = new ShipHisDAO();
			ArrayList<Map<String, String>> arrList= new ArrayList<Map<String, String>>();
		String  extCond="";
		  if(invoiceNo.length()>0)
		  extCond="  and INVOICENO like '"+invoiceNo+"%' order by DONO desc";
		  else
			  extCond="  order by DONO desc";
		  if(CashCust.equalsIgnoreCase("1"))
			    arrList = _ShipHisDAO.selectShipHis("distinct DONO, INVOICENO,CNAME", htCondition, " INVOICENO IS NOT NULL AND INVOICENO <> '' AND 0 = (SELECT COUNT(*) FROM " + plant + "_dnplhdr where INVOICENO = S.INVOICENO) AND DONO like'T%'"+extCond);
			  else
				  arrList = _ShipHisDAO.selectShipHis("distinct DONO, INVOICENO,CNAME", htCondition, " INVOICENO IS NOT NULL AND INVOICENO <> '' AND 0 = (SELECT COUNT(*) FROM " + plant + "_dnplhdr where INVOICENO = S.INVOICENO) AND DONO like'S%'"+extCond);
		  for (int iCnt =0; iCnt<arrList.size(); iCnt++){
			  Map lineArr = (Map) arrList.get(iCnt);
			  if ( dono.length() == 0) 
				  dono = StrUtils.replaceCharacters2Send((String)lineArr.get("DONO"));
		  }
		}
		ArrayList al = new ArrayList();
		Hashtable htCond = new Hashtable();
		Map m = new HashMap();

		if (dono.length() > 0) {
						
			String INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			htCond.put("A.PLANT", plant);
			htCond.put("A.DONO", dono);
			htCond.put("INVOICENO", INVOICENO);
			String query ="";
if(dono.substring(0, 1).equalsIgnoreCase("T"))
{
	 POSUtil posUtil = new POSUtil();  
	query = "a.dono,(select isnull(CustCode,'') from ["+plant+"_POSHDR] where PLANT =a.PLANT and POSTRANID =a.DONO) as CustCode,(select isnull(JobNum,'') from ["+plant+"_DOHDR] where PLANT =a.PLANT and DONO =a.DONO) as JobNum,(select isnull(custName,'') from ["+plant+"_POSHDR] where PLANT =a.PLANT and POSTRANID =a.DONO) as custName,'' as dnplremarks";
	al = posUtil.getPosDetails(query, htCond,"");
}
else
{
			 query = "a.dono,(select isnull(CustCode,'') from ["+plant+"_DOHDR] where PLANT =a.PLANT and DONO =a.DONO) as CustCode,(select isnull(JobNum,'') from ["+plant+"_DOHDR] where PLANT =a.PLANT and DONO =a.DONO) as JobNum,(select isnull(custName,'') from ["+plant+"_DOHDR] where PLANT =a.PLANT and DONO =a.DONO) as custName,(select isnull(dnplremarks,'') from ["+plant+"_DOHDR] where PLANT =a.PLANT and DONO =a.DONO) as dnplremarks";
			/*String query = "dono,dolnno,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyPick,0) as qtyPick,loc as loc,userfld4 as batch,userfld3 as custname,"
			         +" (select itemdesc from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as Itemdesc,"
			         + "(select stkuom from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as uom,"
			         +"CASE when NETWEIGHT <>0 THEN NETWEIGHT ELSE (SELECT ISNULL(NETWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) END netweight,"
			         +"CASE when GROSSWEIGHT <>0 THEN GROSSWEIGHT ELSE (SELECT ISNULL(GROSSWEIGHT,0) FROM ["+plant+"_ITEMMST] WHERE ITEM=A.ITEM) END grossweight,"
			         +" (select hscode from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as hscode,"
			         +" (select coo from ["+plant+"_ITEMMST] where PLANT =a.PLANT and ITEM =a.ITEM) as coo,"
		             + "isnull(PACKING,'')packing,isnull(DIMENSION,'') dimension";*/
			
//			System.out.println("htCond");
			//System.out.println(htCond);
			
			al = _DOUtil.getdnplDetails(query, htCond);
		}
			if (al.size() > 0) {
				m = (Map) al.get(0);

			}

		}
		request.getSession().setAttribute("podetVal", m);
		request.getSession().setAttribute("dono", dono);
		request.getSession().setAttribute("RESULT", fieldDesc);

		return true;
	}
	
	
	private boolean DisplayOutGoingIssueData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String dono = StrUtils.fString(request.getParameter("DONO"));
		String fieldDesc = "";

		ArrayList al = new ArrayList();
		Hashtable htCond = new Hashtable();
		Map m = new HashMap();

		if (dono.length() > 0) {

			HttpSession session = request.getSession();
			String plant = StrUtils.fString(
					(String) session.getAttribute("PLANT")).trim();
			htCond.put("PLANT", plant);
			htCond.put("DONO", dono);

			String query = "dono,custName,custCode,isnull(jobNum,'') as jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,isnull(remark1,'') as remark1,isnull(remark2,'') as remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
			al = _DOUtil.getOutGoingDoHdrDetails(query, htCond);
			if (al.size() > 0) {
				m = (Map) al.get(0);

			}

		}
		request.getSession().setAttribute("podetVal", m);
		request.getSession().setAttribute("dono", dono);
		request.getSession().setAttribute("RESULT", fieldDesc);

		return true;
	}

	private String OutGoingIssueData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;
//		StrUtils StrUtils = new StrUtils();
		Map issMat_HM = null;

		Map mp = new HashMap();
		String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
		String LOGIN_USER = "";
		String SHIPPINGNO = "",creditLimit="",creditBy="";
		String PICKED_QTY = "", DO_BATCH = "", LOC = "", ISSUING_QTY = "", INVOICENO = "",UOMQTY="",priceval="",UOM="";
//		String ITEM_DESCRIPTION = "", ORDER_QTY = "", 
		double unitprice=0,totalprice=0,totalqty=0;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		JSONObject resultJson = new JSONObject();
		try {

			String sepratedtoken = "";
			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
			String REMARK = StrUtils.fString(request.getParameter("REMARK2"));
			INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			StringTokenizer parser = new StringTokenizer(totalString, "=");

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

				HttpSession session = request.getSession();
				PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				DO_NUM = StrUtils.fString((String) mp.get("data1"));
				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
//				ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));
//				ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
				PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
				ISSUING_QTY = StrUtils.fString((String) mp.get("data7"));
				LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
				LOC = StrUtils.fString((String) mp.get("data9"));
				
				DO_BATCH = StrUtils.fString((String) mp.get("data10"));
				UOMQTY= StrUtils.fString((String) mp.get("data11"));
				unitprice= Double.parseDouble(StrUtils.fString((String) mp.get("data12")));			
				UOM= StrUtils.fString((String) mp.get("data13"));			
				
				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				double issuingQty = Double.parseDouble(((String) ISSUING_QTY.trim().toString()));

				String issueQty = String.valueOf(pickedqty - issuingQty);
				System.out.println("Issue Quantity"+ issueQty);
				
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				totalprice=totalprice+(unitprice* Float.parseFloat(issueQty));
				priceval=String.valueOf(totalprice);
				double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
				priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
				totalqty=totalqty+Float.parseFloat(issueQty);
				
				if (SHIPPINGNO.length() == 0) {
					SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
				}
				 CustUtil custUtil = new CustUtil();
				  String curDate =DateUtils.getDate();
				  ShipHisDAO _ShipHisDAO =  new ShipHisDAO();
				  String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);				
				  String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
				  
				issMat_HM = new HashMap();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put(IConstants.ITEM, ITEM_NUM);
				issMat_HM.put(IConstants.QTY, issueQty);
				issMat_HM.put(IConstants.DODET_DONUM, DO_NUM);
				issMat_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
				issMat_HM.put(IConstants.LOC, LOC);
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DO_NUM));
				issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT, DO_NUM));
				issMat_HM.put(IConstants.BATCH, DO_BATCH);
				issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
				issMat_HM.put(IConstants.REMARKS, REMARK);
				issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
				issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
				issMat_HM.put(IConstants.INVOICENO, INVOICENO);
				issMat_HM.put(IConstants.UOM, UOM);
				issMat_HM.put("UOMQTY", UOMQTY);
				
				ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, DO_NUM),PLANT);
				creditLimit   = (String)arrCust.get(24);
				creditBy   = (String)arrCust.get(35);				
				
				issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
				issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
				issMat_HM.put(IConstants.BASE_CURRENCY, (String) session.getAttribute("BASE_CURRENCY"));
				
				xmlStr = "";
				boolean shipflag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_SHIPHIS where DONO='"+DO_NUM+"' and INVOICENO='"+INVOICENO+"'");
				flag = _DOUtil.process_IssueMaterial(issMat_HM);
				
				if (flag == true) {//Shopify Inventory Update
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
//   								String result="";
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
				
				if (flag) {
					
					DoDetDAO _DoDetDAO = new DoDetDAO();
//					DateUtils dateutils = new DateUtils();
					Hashtable htRecvDet = new Hashtable();
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DO_NUM));
					htRecvDet.put("GINO", INVOICENO);                    
					htRecvDet.put(IConstants.DODET_DONUM, DO_NUM);
					htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
					htRecvDet.put(IConstants.AMOUNT, priceval);
					htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
					htRecvDet.put("CRAT",DateUtils.getDateTime());
					htRecvDet.put("CRBY",LOGIN_USER);
					htRecvDet.put("UPAT",DateUtils.getDateTime());
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
        			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
        			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
        			htRecvHis.put(IDBConstants.REMARKS, DO_NUM);        					
        			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
        			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
        			flag = movHisDao.insertIntoMovHis(htRecvHis);

					
				if (!shipflag) {
					new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
					}
				}
				
			}
		} catch (Exception e) {
			String message = ThrowableUtil.getMessage(e);
			if (ajax) {
				resultJson.put("MESSAGE", message);
				resultJson.put("ERROR_CODE", "98");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				response.sendRedirect("../salestransaction/orderissue?action=View&PLANT="+ PLANT + "&DONO=" + DO_NUM + "&result=catchrerror");
				throw e;
			}
		}
		if (flag) {
			String msgflag=StrUtils.fString((String)	issMat_HM.get("msgflag"));
			if(msgflag.length()==0){
				msgflag = "Goods Issued successfully!";
			}
			request.getSession().setAttribute("RESULT",	msgflag	);
			if (ajax) {
				String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
				Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER_API);
				String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
				request.setAttribute("chkdDoNo", new String[] {request.getParameter("DONO")});
				if ("both".equals(sendAttachment) || "bothwithgino".equals(sendAttachment) || "do".equals(sendAttachment) || "dowithgino".equals(sendAttachment)) {
					if (sendAttachment.contains("withgino")) {
						request.setAttribute("printwithigino", "Y");
					}
					viewDOReport(request, response, "printDOWITHBATCH");
				}
				//	Print with GRNO option is not available for invoice
				if ("both".equals(sendAttachment) || "bothwithgino".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
					if (sendAttachment.contains("withgino")) {
						request.setAttribute("printwithigino", "Y");
					}
					viewInvoiceReport(request, response, "printInvoiceWITHBATCH");
				}
				resultJson.put("MESSAGE", msgflag);
				resultJson.put("ERROR_CODE", "100");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				/*
				 * Commanded By Bruhan on Sep 24 2010 To avoid Redirect to
				 * OutBoundShippingPrint page and add successfull Message in
				 * OutBoundsOrderIssue Page Itself
				 * 
				 * response.sendRedirect(
				 * "jsp/OutBoundShippingPrint.jsp?action=View&SHIPPINGNO=" +
				 * SHIPPINGNO + "&PLANT=" + PLANT + "&DONO=" + DO_NUM);
				 */
				response.sendRedirect("../salestransaction/orderissue?DONO=" + DO_NUM
					+ "&action=View");
			}
		}
		return xmlStr;
	}
	
	private String processDNPL(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
		Map issMat_HM = null;
		String PLANT = "", DONO = "", INVOICENO = "", GINO="",DNNO="",PLNO="", netdimension = "", netpacking = "", LOGIN_USER="",CUST_NAME="",dnplremarks="",dolno = "",netweight="",grossweight="",dimension="",Packing="",custCode="",HID="",EDIT="";
		Boolean allChecked = false,fullIssue = false;
		String status="",trandate="",transportid="",clearingagent="",contact_name="",trackingno="",freightforwarderId="",journey="",ShipDNPLID ="",CARRIER="";
		String curDate =DateUtils.getDate();
//		String curtime =DateUtils.getTimeHHmm();
		String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
		String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);	
		//UserTransaction ut = null;
//		Map checkedDOS = new HashMap();
		DNPLHdrDAO dNPLHdrDAO = new DNPLHdrDAO();
		DNPLDetDAO _DNPLDetDAO = new DNPLDetDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		InvoiceDAO invoicedao = new InvoiceDAO();
		ShipHisDAO _ShipHisDAO = new ShipHisDAO();
		String CashCust = StrUtils.fString(request.getParameter("CashCust"));
		try {

			HttpSession session = request.getSession();
            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			GINO = StrUtils.fString(request.getParameter("GINO"));
			DNNO = StrUtils.fString(request.getParameter("dnno"));
			netdimension = StrUtils.fString(request.getParameter("netdimension"));
			netpacking = StrUtils.fString(request.getParameter("netpacking"));
			PLNO = StrUtils.fString(request.getParameter("plno"));
			
			status = StrUtils.fString(request.getParameter("status"));
			trandate = StrUtils.fString(request.getParameter("TRANDATE"));
			transportid = StrUtils.fString(request.getParameter("TRANSPORTID"));
			clearingagent = StrUtils.fString(request.getParameter("clearingagent"));
			contact_name = StrUtils.fString(request.getParameter("CONTACT_NAME"));
			trackingno = StrUtils.fString(request.getParameter("TRACKINGNO"));
			freightforwarderId = StrUtils.fString(request.getParameter("FREIGHT_FORWARDERID"));
			journey = StrUtils.fString(request.getParameter("JOURNEY"));
			ShipDNPLID = StrUtils.fString(request.getParameter("SHIPDNPLID"));
			CARRIER = StrUtils.fString(request.getParameter("CARRIER"));
			
			
	String	TOTALGROSSWEIGHT = StrUtils.fString(request.getParameter("tgw"));
	String  TOTALNETWEIGHT = StrUtils.fString(request.getParameter("tnw"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			if(CUST_NAME.isEmpty())
				CUST_NAME = StrUtils.fString(request.getParameter("CUSTOMER"));	
			dnplremarks = StrUtils.fString(request.getParameter("dnplremarks"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
			custCode= StrUtils.fString(request.getParameter("CUST_CODE"));
//	String	jobNum = StrUtils.fString(request.getParameter("JOB_NUM"));
			EDIT = StrUtils.fString(request.getParameter("EDIT"));
	String DATACOUNT = StrUtils.fString(request.getParameter("DATACOUNT"));
			HID = StrUtils.fString(request.getParameter("HID"));
	String INVOICEHDRID = StrUtils.fString(request.getParameter("INVOICEHDRID"));
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			
		
			
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
			
			// ut = DbBean.getUserTranaction();
	        //    ut.begin();
//			ArrayList DODetails = null;

//    		Hashtable htDoDet = new Hashtable();
    		process: 	
			if (chkdDoNos != null)    {
				
				Map mDoHdr = new HashMap();
				
				mDoHdr.put("PLANT", PLANT);
				mDoHdr.put("DONO", DONO);
				mDoHdr.put("INVOICE",INVOICENO);
				mDoHdr.put("GINO", GINO);
				mDoHdr.put(IConstants.CUSTOMER_CODE, custCode);
				mDoHdr.put("DELIVERYNOTE", DNNO);
				mDoHdr.put("PACKINGLIST", PLNO);
				mDoHdr.put("DNPLDATE", strIssueDate);
				mDoHdr.put("TOTAlGROSSWEIGHT", TOTALGROSSWEIGHT);
				mDoHdr.put("TOTAlNETWEIGHT", TOTALNETWEIGHT);
				mDoHdr.put("TOTAlDIMENSION", netdimension);
				mDoHdr.put("TOTAlPACKING", netpacking);
				mDoHdr.put("STATUS", "P");
				mDoHdr.put("NOTE", dnplremarks);
				mDoHdr.put(IConstants.CREATED_BY, LOGIN_USER);
				mDoHdr.put(IConstants.CREATED_AT, DateUtils.getDateTime());
				mDoHdr.put(IConstants.UPDATED_AT, DateUtils.getDateTime());
				
				RecvDetDAO _RecvDetDAO = new RecvDetDAO();
				Hashtable htShipHdr = new Hashtable();
				htShipHdr.put(IConstants.PLANT, PLANT);
				htShipHdr.put("DIRTYPE", TransactionConstants.CREATEDNPL);
				htShipHdr.put(IConstants.ORDNUM, DONO);
				htShipHdr.put("CLEARING_AGENT_ID", clearingagent);                    
				htShipHdr.put("CLEARANCETYPE", "");
				if(transportid.equalsIgnoreCase(""))
					transportid="0";
                htShipHdr.put("TRANSPORTID", transportid);                    
                htShipHdr.put("CONTACTNAME", contact_name);                    
                htShipHdr.put("TRACKINGNO", trackingno);
				htShipHdr.put("RECEIPTNO", GINO);       
				
				if(status.equalsIgnoreCase("Shipped")) {
					htShipHdr.put("SHIPPING_STATUS", status);       
					htShipHdr.put("SHIPPING_DATE", trandate);       
				}else if(status.equalsIgnoreCase("Intransit")) {
					htShipHdr.put("INTRANSIT_STATUS", status);       
					htShipHdr.put("INTRANSIT_DATE", trandate);       
				}else if(status.equalsIgnoreCase("Delivered")) {
					htShipHdr.put("DELIVERY_STATUS", status);       
					htShipHdr.put("DELIVERY_DATE", trandate);       
				}
//				else {
//					htShipHdr.put("SHIPPING_STATUS", status);       
//					htShipHdr.put("SHIPPING_DATE", trandate);   
//				}
				htShipHdr.put("FREIGHT_FORWARDERID", freightforwarderId);       
				htShipHdr.put("DURATIONOFJOURNEY", journey);       
				htShipHdr.put("CARRIER", CARRIER);       
				htShipHdr.put("CRAT",DateUtils.getDateTime());
				htShipHdr.put("CRBY",LOGIN_USER);
				
				
				int dnplHdrId=0;
				if(EDIT!="")
				{
					dnplHdrId= Integer.parseInt(HID);
					mDoHdr.put("ID", dnplHdrId);
					mDoHdr.put(IConstants.UPDATED_BY, LOGIN_USER);
					flag = _DOUtil.updateDNPLHDR_GINO(mDoHdr,"DNPLHDR")&& true;
					
					if(Integer.valueOf(ShipDNPLID)>0) {
					Hashtable htSp = new Hashtable();
					htSp.put(IDBConstants.PLANT, PLANT);
					htSp.put("DIRTYPE", TransactionConstants.CREATEDNPL);
					htSp.put("DNPLID", ShipDNPLID);
//					htSp.put("DNPLID", Integer.toString(dnplHdrId));
					String sqlShip ="";
					if(status.equalsIgnoreCase("Shipped")) {
						sqlShip = " SET SHIPPING_STATUS='"+status+"',SHIPPING_DATE='"+trandate+"' ,FREIGHT_FORWARDERID='"+freightforwarderId+"',DURATIONOFJOURNEY='"+journey+"' ,"
								+ "TRACKINGNO='"+trackingno+"' ,CONTACTNAME='"+contact_name+"' ,TRANSPORTID='"+transportid+"' ,CARRIER='"+CARRIER+"' ,"
								+ "UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
					}else if(status.equalsIgnoreCase("Intransit")) {
						sqlShip = " SET INTRANSIT_STATUS='"+status+"',INTRANSIT_DATE='"+trandate+"' ,FREIGHT_FORWARDERID='"+freightforwarderId+"',DURATIONOFJOURNEY='"+journey+"' ,"
								+ "TRACKINGNO='"+trackingno+"' ,CONTACTNAME='"+contact_name+"' ,TRANSPORTID='"+transportid+"' ,CARRIER='"+CARRIER+"' ,"
								+ "UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
					}else if(status.equalsIgnoreCase("Delivered")) {
						sqlShip = " SET DELIVERY_STATUS='"+status+"',DELIVERY_DATE='"+trandate+"' ,FREIGHT_FORWARDERID='"+freightforwarderId+"',DURATIONOFJOURNEY='"+journey+"' ,"
								+ "TRACKINGNO='"+trackingno+"' ,CONTACTNAME='"+contact_name+"' ,TRANSPORTID='"+transportid+"' ,CARRIER='"+CARRIER+"' ,"
								+ "UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
					}
					_RecvDetDAO.updateShippingHDR(sqlShip, htSp, "");
					}else {
						htShipHdr.put("DNPLID", Integer.toString(dnplHdrId));     
						flag = _RecvDetDAO.insertShippingHdr(htShipHdr);
					}
					
					Hashtable<String, Object> updnpldet = new Hashtable<>();
					updnpldet.clear();
					updnpldet.put(IDBConstants.PLANT, PLANT);
					updnpldet.put("HDRID", Integer.toString(dnplHdrId));
					_DNPLDetDAO.delete(updnpldet);
					
					
					if(GINO.isEmpty()&&DONO.isEmpty())
					{
						Hashtable htgi = new Hashtable();
						htgi.put("PACKINGLIST", PLNO);
						String sqlgi = "UPDATE "+ PLANT+"_FININVOICEDET SET PACKINGLIST='',DELIVERYNOTE='' ,UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"' WHERE PLANT='"+ PLANT+"' AND INVOICEHDRID='"+INVOICEHDRID+"'";
						invoicedao.updategino(sqlgi, htgi, "");
					}
					else {
					Hashtable htgi = new Hashtable();
					htgi.put(IDBConstants.PLANT, PLANT);
					htgi.put("INVOICENO", GINO);
					htgi.put("DONO", DONO);
					htgi.put("INVOICENO", GINO);
					htgi.put("PACKINGLIST", PLNO);
					String sqlgi = " SET PACKINGLIST='',DELIVERYNOTE='' ,UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
					_ShipHisDAO.update(sqlgi, htgi, "");
					}
					
				}
				else {
					dnplHdrId = dNPLHdrDAO.addDNPLHDR(mDoHdr, PLANT);
					htShipHdr.put("DNPLID", Integer.toString(dnplHdrId));     
					flag = _RecvDetDAO.insertShippingHdr(htShipHdr);
				}
				
				if(dnplHdrId > 0) {
				for (int i = 0; i < chkdDoNos.length; i++)       { 
					dolno = chkdDoNos[i];
					netweight = StrUtils.fString(request.getParameter("netweight_"+dolno));
			    	grossweight = StrUtils.fString(request.getParameter("grossweight_"+dolno));
					dimension = StrUtils.fString(request.getParameter("dimension_"+dolno));
					Packing = StrUtils.fString(request.getParameter("packing"));
//					String srNo = StrUtils.fString(request.getParameter("SRNO_"+dolno));
					String item = StrUtils.fString(request.getParameter("ITEM_"+dolno));
					String hscode = StrUtils.fString(request.getParameter("HSCODE_"+dolno));
					String coo = StrUtils.fString(request.getParameter("COO_"+dolno));
					String uom = StrUtils.fString(request.getParameter("UOM_"+dolno));
					String qty = StrUtils.fString(request.getParameter("QTY_"+dolno));
					
					issMat_HM = new HashMap();
					issMat_HM.put(IConstants.PLANT, PLANT);
					issMat_HM.put("HDRID", Integer.toString(dnplHdrId));
					issMat_HM.put("LNNO", dolno);
					issMat_HM.put("ITEM", item);
					issMat_HM.put("HSCODE", hscode);
					issMat_HM.put("COO", coo);
					issMat_HM.put("UOM", uom);
					issMat_HM.put("QTY", qty);
					issMat_HM.put(IConstants.PACKING, Packing);
					issMat_HM.put(IConstants.DIMENSION, dimension);
					issMat_HM.put(IConstants.NETWEIGHT, netweight.replaceAll(",", ""));
					issMat_HM.put(IConstants.GROSSWEIGHT, grossweight.replaceAll(",", ""));
					issMat_HM.put(IConstants.CREATED_BY, LOGIN_USER);
					issMat_HM.put(IConstants.CREATED_AT, DateUtils.getDateTime());
					issMat_HM.put(IConstants.UPDATED_AT, DateUtils.getDateTime());
					
					
					/*issMat_HM.put(IConstants.DOHDR_CUST_NAME, CUST_NAME);
					issMat_HM.put(IConstants.DOHDR_JOB_NUM, jobNum);
					issMat_HM.put("SRNO", srNo);issMat_HM.put(IConstants.TRAN_DATE,strTranDate);
					issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
					issMat_HM.put(IConstants.DOHDR_COL_TIME, curtime);*/
				
				if(EDIT!=""){
					issMat_HM.put(IConstants.UPDATED_BY, LOGIN_USER);
					//flag = _DOUtil.updateDNPLHDR_GINO(issMat_HM,"DNPLDET")&& true;
					
				}
				//else{
					_DNPLDetDAO.insertDNPLDET(issMat_HM);
					//flag = _DOUtil.process_DNPL(issMat_HM)&& true;
					
				//}
					if(GINO.isEmpty()&&DONO.isEmpty())
					{
						Hashtable htgi = new Hashtable();
						htgi.put("LNNO", dolno);
						htgi.put("ITEM", item);
						String sqlgi = "UPDATE "+ PLANT+"_FININVOICEDET SET PACKINGLIST='"+ PLNO+"',DELIVERYNOTE='"+ DNNO+"' ,UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"' WHERE PLANT='"+ PLANT+"' AND INVOICEHDRID="+INVOICEHDRID;
						invoicedao.updategino(sqlgi, htgi, "");
					}
					else {
					Hashtable htgi = new Hashtable();
					htgi.put(IDBConstants.PLANT, PLANT);
					htgi.put("DOLNO", dolno);
					htgi.put("DONO", DONO);
					htgi.put("ITEM", item);
					htgi.put("INVOICENO", GINO);
					String sqlgi = " SET PACKINGLIST='"+ PLNO+"',DELIVERYNOTE='"+ DNNO+"' ,UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
					_ShipHisDAO.update(sqlgi, htgi, "");
					}
					Hashtable<String, Object> htMovHis = new Hashtable<>();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, PLANT);
					if(EDIT!="")
						htMovHis.put("DIRTYPE", TransactionConstants.EDITDNPL);
					else
						htMovHis.put("DIRTYPE", TransactionConstants.CREATEDNPL);
					htMovHis.put("BATNO", "");
					htMovHis.put(IDBConstants.QTY, qty);
					htMovHis.put(IDBConstants.POHDR_JOB_NUM, "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, DONO + "," + GINO+ "," + INVOICENO);
					htMovHis.put("ITEM", item);
					htMovHis.put("MOVTID", "");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.LOC, "");
					htMovHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
					htMovHis.put(IDBConstants.TRAN_DATE,  strTranDate);
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put(IDBConstants.REMARKS, "");
					
					flag = movHisDao.insertIntoMovHis(htMovHis);
				
				if(!flag)
					break process;
				}
				if(EDIT==""){
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "PAKING_LIST", "PL", PLNO);
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "DELIVERY_NOTE", "DN", DNNO);
				}
				//Update HDR STATUS
				int dcot = Integer.valueOf(DATACOUNT); 
				int chkdDoval = Integer.valueOf(chkdDoNos.length);
				if(chkdDoval==dcot)
				{
					Hashtable uphdr = new Hashtable();
					uphdr.put(IConstants.PLANT, PLANT);
					//uphdr.put("ID", dnplHdrId);
					if (DONO != null && DONO != "")
						uphdr.put("DONO", DONO);
					if (GINO != null && GINO != "")
						uphdr.put("GINO", GINO);
					if (INVOICENO != null && INVOICENO != "")
						uphdr.put("INVOICE", INVOICENO);
					
					flag = _DOUtil.updateDNPLHDR_STATUS(uphdr," STATUS='C' ");
				}
				else
				{
					if(EDIT!=""){
					Hashtable uphdr = new Hashtable();
					uphdr.put(IConstants.PLANT, PLANT);
					uphdr.put("ID", Integer.toString(dnplHdrId));
					flag = _DOUtil.updateDNPLHDR_STATUS(uphdr," STATUS='P' ");
					}
				}
				}
			}
	
			if (flag) {
				
				//DbBean.CommitTran(ut);				
				dnpl(request, response);
				if(EDIT!=""){
				request.getSession().setAttribute(
						"RESULT",
						"Delivery Note/Packing List updated successfully!");
				response.sendRedirect("../salestransaction/packinglistsummary?action=View&PLANT="
						+ PLANT 
						+"&dnplremarks="
						+dnplremarks+"&CashCust="+CashCust+"&EDIT="+EDIT);
				}
				else
				{
					request.getSession().setAttribute(
							"RESULT",
							"Delivery Note/Packing List created successfully!");
					response.sendRedirect("../salestransaction/packinglistsummary?action=View&PLANT="
							+ PLANT 
							+"&dnplremarks="
							+dnplremarks+"&CashCust="+CashCust);
				}
			} else {
				//DbBean.RollbackTran(ut);
				if(EDIT!=""){
				request.getSession()
						.setAttribute(
								"RESULTERROR",
								"Failed to Create Delivery Note/Packing List : "
										+ DONO+
										"&dnplremarks="
										+dnplremarks+"&CashCust="+CashCust+"&EDIT="+EDIT);
				response.sendRedirect("../salestransaction/createpackinglist?result=catchrerror");
				}
				else
				{
					request.getSession()
					.setAttribute(
							"RESULTERROR",
							"Failed to Create Delivery Note/Packing List : "
									+ DONO+
									"&dnplremarks="
									+dnplremarks+"&CashCust="+CashCust);
			response.sendRedirect("../salestransaction/createpackinglist?result=catchrerror");
				}
			}
		}catch (Exception e) {
			// DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR", e.getMessage());
			response
					.sendRedirect("../salestransaction/createpackinglist?action=View&PLANT="
							+ PLANT + "&DONO=" + DONO+ "&GINO=" + GINO 
							+ "&HID=" + HID+ "&INVOICENO=" + INVOICENO+ "&EDIT=" + EDIT
							+"&dnplremarks="
							+dnplremarks
							+ "&allChecked="
							+allChecked
							+"&fullReceive="
							+fullIssue
							+"&result=catchrerror&CashCust="+CashCust);
			throw e;
		}
		
		return xmlStr;
	}
	
	 /* ************Modification History*********************************
	   Sep 25 2014 Bruhan, Description: To include Issue date
	 */
	private String OutGoingIssueBulkData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
		Map issMat_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", DONO = "", LOC = "",dolno = "",item = "",QTYOR = "",issuingQty = "",UOMQTY="";//ITEM_DESCRIPTION = "",
		String ITEM_BATCH = "NOBATCH",BATCH_ID="",ITEM_QTY = "",PICKING_QTY = "",CUST_NAME = "",LOGIN_USER = "",SHIPPINGNO = "",ISNONSTKFLG="";//PICKED_QTY = "0",
		String REMARKS = "",ISSUEDATE="",strIssueDate="",strTranDate="",INVOICENO="",creditLimit="",creditBy="";
		double pickingQty = 0;
		Boolean allChecked = false,fullIssue = false;
		//UserTransaction ut = null;
		Map checkedDOS = new HashMap();
		try {

			HttpSession session = request.getSession();
            
            
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			DONO = StrUtils.fString(request.getParameter("DONO"));
			String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
			LOC = StrUtils.fString(request.getParameter("LOC_0"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			ISSUEDATE = StrUtils.fString(request.getParameter("ISSUEDATE_0"));
			INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			
			//check GINO ON SHIPHISH
			ShipHisDAO shiphstdao = new ShipHisDAO();
			Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
			htTrandId1.put(IConstants.INVOICENO, INVOICENO);
			htTrandId1.put(IConstants.DODET_DONUM+" !", DONO);
			htTrandId1.put(IConstants.PLANT, PLANT);
			boolean flagship = 	shiphstdao.isExisit(htTrandId1);
			if(flagship) {
				String NEW_INVOICENO = new TblControlDAO().getNextOrder(PLANT,LOGIN_USER,"GINO");
				if(NEW_INVOICENO.equalsIgnoreCase(INVOICENO)) {
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				INVOICENO = new TblControlDAO().getNextOrder(PLANT,LOGIN_USER,"GINO");
				} else
					INVOICENO=NEW_INVOICENO;
			}
			
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			
			if (ISSUEDATE.length()>5)
				strTranDate    = ISSUEDATE.substring(6)+"-"+ ISSUEDATE.substring(3,5)+"-"+ISSUEDATE.substring(0,2);
				strIssueDate    = ISSUEDATE.substring(0,2)+"/"+ ISSUEDATE.substring(3,5)+"/"+ISSUEDATE.substring(6);
			
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
			if (chkdDoNos != null)    {     
				for (int i = 0; i < chkdDoNos.length; i++)       { 
					dolno = chkdDoNos[i];
					issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+dolno));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+dolno));
					BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID_"+dolno));
					//LOC = StrUtils.fString(request.getParameter("LOC_"+dolno));//Revert Sales Pick Issue
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					checkedDOS.put(dolno, issuingQty+":"+ITEM_BATCH);
				}
				session.setAttribute("checkedDOS", checkedDOS);
            }
			// ut = DbBean.getUserTranaction();
	        //    ut.begin();
			ArrayList DODetails = null;

    		Hashtable htDoDet = new Hashtable();
    		String queryDoDet = "item,itemDesc,QTYOR,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+PLANT+"_UOM where UOM=UNITMO),1) UOMQTY";
    		process: 	
			if (chkdDoNos != null)    {     
				for (int i = 0; i < chkdDoNos.length; i++)       { 
					dolno = chkdDoNos[i];
					
					issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+dolno));	
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+dolno));
					BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID_"+dolno));
					//LOC = StrUtils.fString(request.getParameter("LOC_"+dolno));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					
					htDoDet.put(IConstants.DODET_DONUM, DONO);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.DODET_DOLNNO, dolno);
		    		DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
					if (DODetails.size() > 0) {	

							Map map1 = (Map) DODetails.get(0);
							item = (String) map1.get("item");
//							ITEM_DESCRIPTION = (String) map1.get("itemDesc");
							QTYOR = (String) map1.get("QTYOR");
							UOMQTY = (String) map1.get("UOMQTY");
							ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,item);
					}
					if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
					List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,item, LOC, ITEM_BATCH);
					double invqty = 0;
					if (listQry.size() > 0) {
						for (int j = 0; j < listQry.size(); j++) {
							Map m = (Map) listQry.get(j);
							ITEM_QTY = (String) m.get("qty");
							invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
						}
						double calinqty = pickingQty * Double.valueOf(UOMQTY);
						if(invqty < calinqty){
							throw new Exception(
									"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected");
							}						
					} 
					
					else {
						
						throw new Exception(
								"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected");
						
					}
					}
					// check for item in location
					
                UserLocUtil uslocUtil = new UserLocUtil();
                uslocUtil.setmLogger(mLogger);
                boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC);
                if(!isvalidlocforUser){
                    throw new Exception(" Loc : "+LOC+" is not User Assigned Location");
                }
	
//				double orderqty = Double.parseDouble(((String) QTYOR.trim()));
//				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
				pickingQty = StrUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
				PICKING_QTY = String.valueOf(pickingQty);
				PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
				SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
			
				issMat_HM = new HashMap();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put(IConstants.ITEM, item);
				//issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
				issMat_HM.put(IConstants.DODET_DONUM, DONO);
				issMat_HM.put(IConstants.DODET_DOLNNO, dolno);
				issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				issMat_HM.put(IConstants.LOC, LOC);
				issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
				issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
				issMat_HM.put(IConstants.BATCH_ID, BATCH_ID);
				issMat_HM.put(IConstants.QTY, PICKING_QTY);
				issMat_HM.put(IConstants.ORD_QTY, QTYOR);
				issMat_HM.put(IConstants.INV_EXP_DATE, "");
				issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
				issMat_HM.put("INV_QTY", "1");
				issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
				issMat_HM.put(IConstants.REMARKS, REMARKS);
				issMat_HM.put("TYPE", "OBBULK");
				issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
				issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
				issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
				issMat_HM.put(IConstants.INVOICENO, INVOICENO);
				issMat_HM.put("UOMQTY", UOMQTY);
				
				CustUtil custUtil = new CustUtil();
				ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, DONO),PLANT);
				creditLimit   = (String)arrCust.get(24);
				creditBy   = (String)arrCust.get(35);
				
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
				issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
				issMat_HM.put(IConstants.BASE_CURRENCY, (String) session.getAttribute("BASE_CURRENCY"));
				
				flag = _DOUtil.process_DoPickIssueForPC(issMat_HM)&& true;
				if(!flag)
					break process;
				}
			}
	
			if (flag) {
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				//DbBean.CommitTran(ut);	
				String msgflag=StrUtils.fString((String)	issMat_HM.get("msgflag"));
				if(msgflag.length()>0){
					request.getSession().setAttribute("RESULT",	msgflag	);
				}else {
					request.getSession().setAttribute("RESULT","Sales Order : " + DONO + "  Picked/Issued successfully!");
				}
				response.sendRedirect("../salestransaction/orderpickissue?action=View&PLANT="+ PLANT + "&DONO=" + DONO);
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession().setAttribute("RESULTERROR","Failed to Pick/Issue Sales Order : "+ DONO);
				response.sendRedirect("../salestransaction/orderpickissue?result=catchrerror");
			}
			
		}	
		 catch (Exception e) {
			// DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			response
					.sendRedirect("../salestransaction/orderpickissue?action=View&PLANT="
							+ PLANT + "&DONO=" + DONO 
							+"&LOC="
							+LOC
							+"&ISSUEDATE="
							+ISSUEDATE
							+"&REF="
							+REMARKS
							+ "&allChecked="
							+allChecked
							+"&fullReceive="
							+fullIssue
							+"&result=catchrerror"+"&INVOICENO="+INVOICENO);
			throw e;
		}
		/*if (flag) {
			request.getSession().setAttribute("RESULT",
					"Goods Issued successfully!");
			response.sendRedirect("jsp/OutBoundsOrderBulkIssue.jsp?action=View&PLANT="
					+ PLANT + "&DONO=" + DONO
					+"&LOC="
					+LOC
					+"&REF="
					+REMARKS);
		}*/
		return xmlStr;
	}
	private String OutGoingIssueBulkDataWithInvoice(HttpServletRequest request,
			HttpServletResponse response, String invnumber) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
//		StrUtils StrUtils = new StrUtils();
		Map issMat_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", DONO = "", LOC = "",dolno = "",item = "",QTYOR = "",issuingQty = "",UOMQTY="",DOUOM="",DISCOUNT_TYPE="",ITEM_DESCRIPTION = "";
		String ITEM_BATCH = "NOBATCH",BATCH_ID="",ITEM_QTY = "",PICKING_QTY = "",CUST_NAME = "",LOGIN_USER = "",SHIPPINGNO = "",ISNONSTKFLG="";//PICKED_QTY = "0",
		String REMARKS = "",ISSUEDATE="",strIssueDate="",strTranDate="",INVOICENO="", creditLimit = "", creditBy = "",priceval="",CLEARAGENT="",CLEARANCETYPE="",TRANSPORTID="",CONTACT_NAME="",TRACKINGNO="";		
		String prdDelDate = "",PrdCurcencySeq="",tax_type="";		
		String alertitem="",MINSTKQTY="";
		double pickingQty = 0,unitprice=0,unitcost=0,totalprice=0,totalqty=0;
		Boolean allChecked = false,fullIssue = false;
		UserTransaction ut = null;
		Map checkedDOS = new HashMap();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		JSONObject resultJson = new JSONObject();
		try {

			HttpSession session = request.getSession();
            
            
			PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String ISINVENTORYMINQTY = new DoHdrDAO().getISINVENTORYMINQTY(PLANT);//Thanzi
			DONO = StrUtils.fString(request.getParameter("DONO"));
			String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
			LOC = StrUtils.fString(request.getParameter("LOC_0"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			ISSUEDATE = StrUtils.fString(request.getParameter("ISSUEDATE_0"));
			INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			CLEARAGENT = StrUtils.fString(request.getParameter("clearingagent"));
			CLEARANCETYPE = StrUtils.fString(request.getParameter("typeofclearance"));
			TRANSPORTID = StrUtils.fString(request.getParameter("TRANSPORTID"));
			CONTACT_NAME = StrUtils.fString(request.getParameter("CONTACT_NAME"));
			TRACKINGNO = StrUtils.fString(request.getParameter("TRACKINGNO"));
			
			//check GINO ON SHIPHISH
			ShipHisDAO shiphstdao = new ShipHisDAO();
			Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
			htTrandId1.put(IConstants.INVOICENO, INVOICENO);
			htTrandId1.put(IConstants.DODET_DONUM+" !", DONO);
			htTrandId1.put(IConstants.PLANT, PLANT);
			boolean flagship = 	shiphstdao.isExisit(htTrandId1);
			if(flagship) {
				String NEW_INVOICENO = new TblControlDAO().getNextOrder(PLANT,LOGIN_USER,"GINO");
				if(NEW_INVOICENO.equalsIgnoreCase(INVOICENO)) {
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				INVOICENO = new TblControlDAO().getNextOrder(PLANT,LOGIN_USER,"GINO");
				} else
					INVOICENO=NEW_INVOICENO;
			}
			
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			
			if (ISSUEDATE.length()>5)
				strTranDate    = ISSUEDATE.substring(6)+"-"+ ISSUEDATE.substring(3,5)+"-"+ISSUEDATE.substring(0,2);
				strIssueDate    = ISSUEDATE.substring(0,2)+"/"+ ISSUEDATE.substring(3,5)+"/"+ISSUEDATE.substring(6);
				
				DateUtils dateUtils = new DateUtils();
				String fromdates = dateUtils.parseDateddmmyyyy(strIssueDate);
				String time = DateUtils.getTimeHHmm();
				String orderdate = fromdates+time+"12";
				LocalDate date = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
				String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String tran = formattedDate;
			
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
			String SHOWSALESBYPURCHASECOST="0";
			CustUtil custUtils = new CustUtil();
            ArrayList arrCust1 = custUtils.getCustomerListStartsWithName(CUST_NAME,PLANT);
            if (arrCust1.size() > 0) {
            for(int i =0; i<arrCust1.size(); i++) {
                Map arrCustLine = (Map)arrCust1.get(i);
                SHOWSALESBYPURCHASECOST = (String)arrCustLine.get("SHOWSALESBYPURCHASECOST");
            }
            }
			
			if (chkdDoNos != null)    {     
				for (int i = 0; i < chkdDoNos.length; i++)       { 
					dolno = chkdDoNos[i];
					issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+dolno));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+dolno));
					BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID_"+dolno));
					//LOC = StrUtils.fString(request.getParameter("LOC_"+dolno));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					checkedDOS.put(dolno, issuingQty+":"+ITEM_BATCH);
				}
				session.setAttribute("checkedDOS", checkedDOS);
            }
			 ut = DbBean.getUserTranaction();
	            ut.begin();
			ArrayList DODetails = null;
			
			String isSales_Purchase = new PlantMstDAO().getIsSalesToPurchase(PLANT);//imthi
			if(isSales_Purchase == null) isSales_Purchase = "0";
			String plnt = new PlantMstDAO().CheckPlantDesc(CUST_NAME);//imthi
			if(plnt == null) plnt = "0";
			String currentplntdesc = new PlantMstDAO().getcmpyname(PLANT);//imthi
			String isShopify = new PlantMstDAO().getshopify(PLANT);//azees
			
			
			String pono="",sCustCode="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sTelNo="";
			String gst = new selectBean().getGST("PURCHASE",plnt);
			String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE(PLANT);
			if(pltCountry.equalsIgnoreCase(""))
				pltCountry="Asia/Singapore";
			PoHdr pohdr = new PoHdr();
			if(isSales_Purchase.equalsIgnoreCase("1") && !plnt.equalsIgnoreCase("0")) {
				pono = new TblControlDAO().getNextOrder(plnt, LOGIN_USER, IConstants.INBOUND);
				new TblControlUtil().updateTblControlSeqNo(plnt, IConstants.INBOUND, "P", pono);
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plnt);
				ht.put(IDBConstants.PONO, pono);
				if (!new PoHdrDAO().isExisit(ht)) {
					
				PoHdrDAO poHdrDAO= new PoHdrDAO();
				DoHdr olddohdr =new DoHdrDAO().getDoHdrById(PLANT, DONO);
				new FieldCopier().copyFields(olddohdr, pohdr);
				pohdr.setPONO(pono);
				pohdr.setCollectionDate(ISSUEDATE);
				ArrayList arrCust = new CustUtil().getVendorDetailsbyName(currentplntdesc,plnt);
				if (arrCust.size() > 0) {
					sCustCode = (String) arrCust.get(0);
    				sCustName = (String) arrCust.get(1);
    				sAddr1 = (String) arrCust.get(2);
					sAddr2 = (String) arrCust.get(3);
					sAddr3 = (String) arrCust.get(4);
					sTelNo = (String) arrCust.get(10);
				}else {
					ArrayList plntList = new PlantMstDAO().getPlantMstDetails(plnt);
					Map plntMap  = (Map) plntList.get(0);
					String COUNTRY = (String) plntMap.get("COUNTY");
					sAddr1 = (String) plntMap.get("ADD1");
					sAddr2 = (String) plntMap.get("ADD2");
					sAddr3 = (String) plntMap.get("ADD3");
					sCustCode = currentplntdesc;
    				sCustName = currentplntdesc;
					String Supcurrency=new PlantMstDAO().getBaseCurrency(plnt);
					Hashtable htsup = new Hashtable();
					htsup.put(IDBConstants.PLANT,plnt);
					htsup.put(IConstants.VENDOR_CODE, currentplntdesc);
					htsup.put(IConstants.VENDOR_NAME, currentplntdesc);
					htsup.put("CURRENCY_ID", Supcurrency);
					htsup.put(IConstants.COUNTRY, COUNTRY);
					htsup.put(IConstants.ISACTIVE, "Y");
					htsup.put(IConstants.TAXTREATMENT, "Non GST Registered");
					htsup.put("CRAT",new DateUtils().getDateTime());
			        htsup.put("CRBY",LOGIN_USER);
			        htsup.put("Comment1", " 0 ");
					boolean custInserted = new CustUtil().insertVendor(htsup);
				}
				pohdr.setCustCode(sCustCode);
				pohdr.setPLANT(plnt);
				pohdr.setCustName(sCustName);
				 float gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
				 if(gstVatValue==0f){
						gst="0.000";
					}else{
						gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
				 
//				pohdr.setINBOUND_GST(olddohdr.getOUTBOUND_GST());
				pohdr.setINBOUND_GST(Double.valueOf(gst));
				pohdr.setISDISCOUNTTAX(olddohdr.getISDISCOUNTTAX());
				pohdr.setISSHIPPINGTAX(olddohdr.getISSHIPPINGTAX());
				pohdr.setSTATUS("N");
				pohdr.setORDER_STATUS("Open");
				pohdr.setSTATUS_ID("NOT PAID");
				pohdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
				pohdr.setISTAXINCLUSIVE(Short.valueOf("0"));
				pohdr.setREVERSECHARGE(Short.valueOf("0"));
				pohdr.setGOODSIMPORT(Short.valueOf("0"));
				pohdr.setSHIPPINGID("");
				pohdr.setSHIPPINGCUSTOMER("");
				pohdr.setAddress(sAddr1);
				pohdr.setAddress2(sAddr2);
				pohdr.setAddress3(sAddr3);
				pohdr.setContactNum(sTelNo);
				pohdr.setORDERDISCOUNT(0);
				pohdr.setSHIPPINGCOST(0);
				pohdr.setADJUSTMENT(0);
				pohdr.setREMARK3(olddohdr.getRemark3());
				pohdr.setREMARK3(olddohdr.getRemark3());
				pohdr.setCRAT(DateUtils.getDateTime());
				
				int transportid = olddohdr.getTRANSPORTID(); 
				if(transportid > 0){
				String transportmode = new TransportModeDAO().getTransportModeById(PLANT, transportid);
				if (new TransportModeDAO().isExistTransport(transportmode, plnt)) 
				{	
					String transport = new TransportModeDAO().getTransportModeByName(plnt, transportmode);
					int tranId = Integer.valueOf(transport);
					pohdr.setTRANSPORTID(tranId);
				}else {
					pohdr.setTRANSPORTID(0);
				}
				}else {
					pohdr.setTRANSPORTID(0);
				}
				
				String Paymentmode = olddohdr.getPAYMENTTYPE();
				if(!Paymentmode.equalsIgnoreCase("")){
				if (new PaymentModeMstDAO().IsPaymentModeMstExists(plnt,Paymentmode)) {
					pohdr.setPAYMENTTYPE(Paymentmode);
				}else {
					pohdr.setPAYMENTTYPE("");
				}
				}else {
					pohdr.setPAYMENTTYPE("");
				}
				
				String Paymentterms = olddohdr.getPAYMENT_TERMS();
				if(!Paymentterms.equalsIgnoreCase("")){
					if (new PayTermsDAO().isExistPaytype(Paymentterms, plnt)) {
						pohdr.setPAYMENT_TERMS(Paymentterms);
					}else {
						pohdr.setPAYMENT_TERMS("");
					}
				}else {
					pohdr.setPAYMENT_TERMS("");
				}
				
				String IncoTerms = olddohdr.getINCOTERMS();
				if(!IncoTerms.equalsIgnoreCase("")){
					if (new MasterUtil().isExistINCOTERM(IncoTerms, plnt)) {
						pohdr.setINCOTERMS(IncoTerms);
					}else {
						pohdr.setINCOTERMS("");
					}
				}else {
					pohdr.setINCOTERMS("");
				}
				
				pohdr.setORDERTYPE("");
				pohdr.setEMPNO("");
				
				
				int DOtaxid = olddohdr.getTAXID();
				FinCountryTaxType fintax = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(Integer.valueOf(DOtaxid));
				String cCode="",cConKey="",cTaxtype="";
				int POtaxid = 0;
				if (fintax!= null ||fintax.equals("")) {
					cCode= fintax.getCOUNTRY_CODE();
					cConKey= fintax.getCONFIGKEY();
					cTaxtype= fintax.getTAXTYPE();
				}
				List<FinCountryTaxType> taxtypes = new FinCountryTaxTypeDAO().getCountryTaxTypes("INBOUND", cCode, cTaxtype);
				if (taxtypes.size() > 0) {
					for (FinCountryTaxType finCountryTaxType : taxtypes) {
						POtaxid = Integer.valueOf(finCountryTaxType.getID());	  
					}
				}
//				if(DOtaxid==25) {POtaxid=2;}else if(DOtaxid==26) {POtaxid=3;}else if(DOtaxid==27) {POtaxid=8;}else if(DOtaxid==29) {POtaxid=9;}else {POtaxid=0;}
				pohdr.setTAXID(POtaxid);
				
//				boolean poadded = poHdrDAO.addPoHdr(pohdr);
//				if(poadded==true) {
//					MovHisDAO movHisDao = new MovHisDAO();
//					Hashtable<String, String> htPoMovhis = new Hashtable();
//					htPoMovhis.put(IDBConstants.PLANT, plnt);
//					htPoMovhis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
//					htPoMovhis.put(IDBConstants.CUSTOMER_CODE, sCustCode);
//					htPoMovhis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
//					htPoMovhis.put(IDBConstants.CREATED_BY, LOGIN_USER);
//					htPoMovhis.put("MOVTID", "");
//					htPoMovhis.put("RECID", "");
//					htPoMovhis.put(IDBConstants.REMARKS, currentplntdesc+","+DONO+","+INVOICENO);
//					//htPoMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getCountryDate(pltCountry)));
//					htPoMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
//					htPoMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//					movHisDao.insertIntoMovHis(htPoMovhis);
//					
//					new TblControlUtil().updateTblControlSeqNo(plnt, IConstants.INBOUND, "P", pono);
//				}
				}
			}
			
    		Hashtable htDoDet = new Hashtable();
    		String queryDoDet = "item,itemDesc,CURRENCYUSEQT,TAX_TYPE,PRODUCTDELIVERYDATE,QTYOR,UNITMO,DISCOUNT_TYPE,ISNULL((select ISNULL(QPUOM,1) from "+PLANT+"_UOM where UOM=UNITMO),1) UOMQTY,UNITPRICE,UNITCOST";
    		int polno = 0;
    		process: 	
			if (chkdDoNos != null)    {     
				for (int i = 0; i < chkdDoNos.length; i++)       { 
					dolno = chkdDoNos[i];
					polno = i + 1;
					issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+dolno));	
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+dolno));
					BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID_"+dolno));
					//LOC = StrUtils.fString(request.getParameter("LOC_"+dolno));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					
					htDoDet.put(IConstants.DODET_DONUM, DONO);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.DODET_DOLNNO, dolno);
		    		DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
					if (DODetails.size() > 0) {	

							Map map1 = (Map) DODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("itemDesc");
							QTYOR = (String) map1.get("QTYOR");
							UOMQTY = (String) map1.get("UOMQTY");
							DOUOM = (String) map1.get("UNITMO");
							DISCOUNT_TYPE = (String) map1.get("DISCOUNT_TYPE");
							ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,item);
							unitprice= Double.parseDouble(StrUtils.fString((String) map1.get("UNITPRICE")));
//							StrUtils strUtils     = new StrUtils();
							String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
							
							priceval=String.valueOf(totalprice);
							double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
							priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
							
							unitcost= Double.parseDouble(StrUtils.fString((String) map1.get("UNITCOST")));
							prdDelDate = (String) map1.get("PRODUCTDELIVERYDATE");
							PrdCurcencySeq = (String) map1.get("CURRENCYUSEQT");
							tax_type = (String) map1.get("TAX_TYPE");
							if(tax_type.contains("STANDARD RATED")) {
								tax_type = "STANDARD RATED "+"["+gst+".0%] ";
							}
							
							
							
							
					}
					if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
					List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,item, LOC, ITEM_BATCH);
					double invqty = 0;
					double Detuctqty = 0;
					double STKQTY = 0;
					if (listQry.size() > 0) {
						for (int j = 0; j < listQry.size(); j++) {
							Map m = (Map) listQry.get(j);
							ITEM_QTY = (String) m.get("qty");
							MINSTKQTY = (String) m.get("MINSTKQTY");
							invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
							STKQTY = Double.parseDouble(((String) MINSTKQTY.trim()));
						}
						if(STKQTY!=0) {
						Detuctqty = invqty-pickingQty;
						if(STKQTY>Detuctqty) {
							if(alertitem.equalsIgnoreCase("")) {
								alertitem =item;
							}else {
								alertitem = alertitem+" , "+item;
							}
							
						}
						}
						
						if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
							alertitem = alertitem;
						else 
							alertitem="";
						
						double calinqty = pickingQty * Double.valueOf(UOMQTY);
						if(invqty < calinqty){
				    		Hashtable ht = new Hashtable();
				    		boolean podetex = false;
				    		if(isSales_Purchase.equalsIgnoreCase("1") && !plnt.equalsIgnoreCase("0")) {
				    		ht.put(IDBConstants.PLANT, plnt);
							ht.put("PONO",pono);
							podetex = new PoDetDAO().isExisit(ht);
				    		}
							if(podetex==true) {
								boolean poadded = new PoHdrDAO().addPoHdr(pohdr);
								MovHisDAO movHisDao = new MovHisDAO();
								Hashtable<String, String> htPoMovhis = new Hashtable();
								htPoMovhis.put(IDBConstants.PLANT, plnt);
								htPoMovhis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
								htPoMovhis.put(IDBConstants.CUSTOMER_CODE, sCustCode);
								htPoMovhis.put(IDBConstants.MOVHIS_ORDNUM, pono);
								htPoMovhis.put(IDBConstants.CREATED_BY, LOGIN_USER);
								htPoMovhis.put("MOVTID", "");
								htPoMovhis.put("RECID", "");
								htPoMovhis.put(IDBConstants.REMARKS, currentplntdesc+","+DONO+","+INVOICENO);
								htPoMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htPoMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								movHisDao.insertIntoMovHis(htPoMovhis);
								
								//new TblControlUtil().updateTblControlSeqNo(plnt, IConstants.INBOUND, "P", pono);
							}
							throw new Exception(
									"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected");
							}						
					} 
					
					else {
			    		Hashtable ht = new Hashtable();
			    		boolean podetex = false;
			    		if(isSales_Purchase.equalsIgnoreCase("1") && !plnt.equalsIgnoreCase("0")) {
			    		ht.put(IDBConstants.PLANT, plnt);
						ht.put("PONO",pono);
						podetex = new PoDetDAO().isExisit(ht);
			    		}
						if(podetex==true) {
							boolean poadded = new PoHdrDAO().addPoHdr(pohdr);
							MovHisDAO movHisDao = new MovHisDAO();
							Hashtable<String, String> htPoMovhis = new Hashtable();
							htPoMovhis.put(IDBConstants.PLANT, plnt);
							htPoMovhis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
							htPoMovhis.put(IDBConstants.CUSTOMER_CODE, sCustCode);
							htPoMovhis.put(IDBConstants.MOVHIS_ORDNUM, pono);
							htPoMovhis.put(IDBConstants.CREATED_BY, LOGIN_USER);
							htPoMovhis.put("MOVTID", "");
							htPoMovhis.put("RECID", "");
							htPoMovhis.put(IDBConstants.REMARKS, currentplntdesc+","+DONO+","+INVOICENO);
							htPoMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htPoMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							movHisDao.insertIntoMovHis(htPoMovhis);
							
							//new TblControlUtil().updateTblControlSeqNo(plnt, IConstants.INBOUND, "P", pono);
						}
						throw new Exception(
								"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected");
						
					}
					}
					// check for item in location
					
                UserLocUtil uslocUtil = new UserLocUtil();
                uslocUtil.setmLogger(mLogger);
                boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC);
                if(!isvalidlocforUser){
                    throw new Exception(" Loc : "+LOC+" is not User Assigned Location");
                }
	
//				double orderqty = Double.parseDouble(((String) QTYOR.trim()));
//				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
				pickingQty = StrUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
				PICKING_QTY = String.valueOf(pickingQty);
				PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
				SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
			
				issMat_HM = new HashMap();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put(IConstants.ITEM, item);
				//issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
				issMat_HM.put(IConstants.DODET_DONUM, DONO);
				issMat_HM.put(IConstants.DODET_DOLNNO, dolno);
				issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				issMat_HM.put(IConstants.LOC, LOC);
				issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
				issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
				issMat_HM.put(IConstants.BATCH_ID, BATCH_ID);
				issMat_HM.put(IConstants.QTY, PICKING_QTY);
				issMat_HM.put(IConstants.ORD_QTY, QTYOR);
				issMat_HM.put(IConstants.INV_EXP_DATE, "");
				issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
				issMat_HM.put("INV_QTY", "1");
				issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
				issMat_HM.put(IConstants.REMARKS, REMARKS);
				issMat_HM.put("TYPE", "OBBULK");
				issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
				issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
				issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
				issMat_HM.put(IConstants.INVOICENO, INVOICENO);
				issMat_HM.put(IConstants.UOM, DOUOM);
				issMat_HM.put("UOMQTY", UOMQTY);
				issMat_HM.put("PONO", pono);
				
				CustUtil custUtil = new CustUtil();
				ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, DONO),PLANT);
				creditLimit   = (String)arrCust.get(24);
				creditBy   = (String)arrCust.get(35);
				
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
				issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
				issMat_HM.put(IConstants.BASE_CURRENCY, (String) session.getAttribute("BASE_CURRENCY"));
				
				flag = _DOUtil.process_DoPickIssueForPC_ut(issMat_HM)&& true;
				if (flag) {
				totalprice=totalprice+(unitprice* pickingQty);
				totalqty=totalqty+pickingQty;
				}
				if(flag) {
					if(isSales_Purchase.equalsIgnoreCase("1") && !plnt.equalsIgnoreCase("0")) {
						PoDet poDet = new PoDet();
						new FieldCopier().copyFields(DODetails, poDet);
						PoDetDAO poDetDAO= new PoDetDAO();
						poDet.setPLANT(plnt);
						poDet.setPONO(pono);
						poDet.setITEM(item);
						poDet.setItemDesc(ITEM_DESCRIPTION);
						poDet.setTRANDATE(ISSUEDATE);
						/*poDet.setUNITCOST(unitcost);*/
						poDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)issuingQty)));
						poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0")));
						poDet.setUNITMO(DOUOM);
						poDet.setUSERFLD1(ITEM_DESCRIPTION);
						poDet.setACCOUNT_NAME("Inventory Asset");
						poDet.setTAX_TYPE(tax_type);
						poDet.setUSERFLD3(sCustName);
//						int polno = i+1;
//						poDet.setPOLNNO(polno);
						poDet.setLNSTAT("N");
						poDet.setPRODUCTDELIVERYDATE(prdDelDate);
						poDet.setDISCOUNT_TYPE(DISCOUNT_TYPE);
						poDet.setUNITCOST_AOD(unitcost);
						poDet.setCRBY(LOGIN_USER);
						poDet.setCURRENCYUSEQT(Double.parseDouble(PrdCurcencySeq));
						poDet.setCRAT(DateUtils.getDateTime());
						boolean poadetdded = false;
						if((new ItemUtil().isExistsItemMst(item,plnt))) {
							List listItem = new ItemUtil().queryItemMstDetails(item,plnt);
	                        Vector arrItem    = (Vector)listItem.get(0);
	                        if(arrItem.size()>0){
	                        	unitcost= Double.parseDouble(StrUtils.fString((String) arrItem.get(13)));
	                        }
	                        //polno =getNextCountValue();
							poDet.setPOLNNO(polno);
							if(SHOWSALESBYPURCHASECOST.equalsIgnoreCase("1"))
	                        poDet.setUNITCOST(unitprice);
							else
							poDet.setUNITCOST(unitcost);
						 poadetdded = poDetDAO.addPoDet(poDet);
						}
						if(poadetdded==true) {
							Hashtable<String, String> ht = new Hashtable<>();
							ht.put(IDBConstants.PLANT, PLANT);
							ht.put(IDBConstants.DODET_DONUM, DONO);
							ht.put(IDBConstants.DODET_DOLNNO, dolno);
							ht.put(IDBConstants.ITEM, item);
							String rem="";
							List al = new DoDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);	
							if (al.size() > 0) {
									Map m = (Map) al.get(0);
									rem = (String) m.get("REMARKS");
							}
							
							PoDetRemarks poDetRemarks = new PoDetRemarks();
							poDetRemarks.setREMARKS(rem);
							poDetRemarks.setITEM(item);
							poDetRemarks.setPONO(pono);
							poDetRemarks.setPOLNNO(Integer.valueOf(polno));
							poDetRemarks.setCRBY(LOGIN_USER);
							poDetRemarks.setPLANT(plnt);
							poDetRemarks.setCRAT(DateUtils.getDateTime());
							boolean insertFlag = poDetDAO.addPoDetRemarks(poDetRemarks);
							
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plnt);
						htRecvHis.put("DIRTYPE", TransactionConstants.IB_ADD_ITEM);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, sCustCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, "");
						htRecvHis.put(IDBConstants.ITEM,item);
						htRecvHis.put(IDBConstants.QTY, issuingQty);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
						htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						//htRecvHis.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getCountryDate(pltCountry)));
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						flag = new MovHisDAO().insertIntoMovHis(htRecvHis);
						}
						
//						if(!(new ItemUtil().isExistsItemMst(item,plnt))) {
//							List listItem = new ItemUtil().queryItemMstDetails(item,PLANT);
//	                        Vector arrItem    = (Vector)listItem.get(0);
//	                        if(arrItem.size()>0){
//	                        	  Hashtable ht = new Hashtable();
//	    	                        ht.put(IConstants.PLANT,plnt);
//	    	                        ht.put(IConstants.ITEM,(String)arrItem.get(0));
//	    	                        ht.put(IConstants.ITEM_DESC,StrUtils.fString((String)arrItem.get(1)));
//	    	                        ht.put(IConstants.ITEMMST_REMARK1,StrUtils.fString((String)arrItem.get(4)));
//	    	                        ht.put(IConstants.ITEMMST_ITEM_TYPE,StrUtils.fString((String)arrItem.get(2)));
//	    	                        ht.put(IConstants.PRDBRANDID ,StrUtils.fString((String)arrItem.get(19)));
//	    	                        ht.put("STKUOM",StrUtils.fString((String)arrItem.get(3)));
//	    	                        ht.put(IConstants.ITEMMST_REMARK4,StrUtils.fString((String)arrItem.get(7)));
//	    	                        ht.put(IConstants.ITEMMST_REMARK3,StrUtils.fString((String)arrItem.get(6)));
//	    	                        ht.put(IConstants.PRDCLSID,StrUtils.fString((String)arrItem.get(10)));
//	    	                        ht.put(IConstants.PRDDEPTID,StrUtils.fString((String)arrItem.get(46)));
//	    	                        ht.put(IConstants.PRICE,StrUtils.fString((String)arrItem.get(12)));
//	    	                        ht.put(IConstants.ISACTIVE,StrUtils.fString((String)arrItem.get(11)));
//	    	                        ht.put(IConstants.COST,StrUtils.fString((String)arrItem.get(13)));
//	    	                        ht.put(IConstants.MIN_S_PRICE,StrUtils.fString((String)arrItem.get(14)));
//	    	                        ht.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
//	    	                        ht.put("NONSTKFLAG",StrUtils.fString((String)arrItem.get(17)));
//	    	                        ht.put("NONSTKTYPEID",StrUtils.fString((String)arrItem.get(18)));
//	    	                        ht.put("DISCOUNT",StrUtils.fString((String)arrItem.get(15)));
//	    	                        ht.put("ITEM_LOC",StrUtils.fString((String)arrItem.get(20)));
//	    	                        ht.put("NETWEIGHT",StrUtils.fString((String)arrItem.get(25)));
//	    	                        ht.put("GROSSWEIGHT",StrUtils.fString((String)arrItem.get(26)));
//	    	                        ht.put("HSCODE",StrUtils.fString((String)arrItem.get(27)));
//	    	                        ht.put("COO",StrUtils.fString((String)arrItem.get(28)));
//	    	                        ht.put("VINNO",StrUtils.fString((String)arrItem.get(29)));
//	    	                        ht.put("MODEL",StrUtils.fString((String)arrItem.get(30)));
//	    							ht.put("RENTALPRICE",StrUtils.fString((String)arrItem.get(31)));
//	    	                        ht.put("SERVICEPRICE",StrUtils.fString((String)arrItem.get(32)));
//	    	                        ht.put("PURCHASEUOM",StrUtils.fString((String)arrItem.get(33)));
//	    	                        ht.put("SALESUOM",StrUtils.fString((String)arrItem.get(34)));
//	    	                        ht.put("RENTALUOM",StrUtils.fString((String)arrItem.get(35)));
//	    	                        ht.put("SERVICEUOM",StrUtils.fString((String)arrItem.get(36)));
//	    	                        ht.put("INVENTORYUOM",StrUtils.fString((String)arrItem.get(37)));
//	    	                        ht.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
//	    							ht.put("CATLOGPATH",StrUtils.fString((String)arrItem.get(40)));
//	    							ht.put("ISCOMPRO",StrUtils.fString((String)arrItem.get(42)));
//	    							ht.put("CPPI",StrUtils.fString((String)arrItem.get(43)));
//	    							ht.put("INCPRICE",StrUtils.fString((String)arrItem.get(44)));
//	    							ht.put("INCPRICEUNIT",StrUtils.fString((String)arrItem.get(45)));
//	    							ht.put("ISCHILDCAL",StrUtils.fString((String)arrItem.get(48)));
//	    							ht.put("ISPOSDISCOUNT",StrUtils.fString((String)arrItem.get(49)));
////	    							ht.put("LOC_ID",StrUtils.fString((String)arrItem.get(50)));
//	    							String vendno = StrUtils.fString((String)arrItem.get(47));
//	    							ht.put(IDBConstants.VENDOR_CODE,vendno);
//	    							ht.put("USERFLD1", "N");
//	    							String STKQTY = StrUtils.fString((String)arrItem.get(8));
//	    					         if(STKQTY=="")
//	    					        	  STKQTY ="0";
//	    					          ht.put(IDBConstants.STKQTY, STKQTY);//stkqty
//	    					          String MAXSTKQTY = StrUtils.fString((String)arrItem.get(21));
//	    					          if(MAXSTKQTY=="")
//	    					        	  MAXSTKQTY ="0";
//	    					          ht.put(IDBConstants.MAXSTKQTY, MAXSTKQTY);
//	    					          boolean itemInserted = new ItemUtil().insertItem(ht);
//	    					          
//	    					          String PRD_BRAND = StrUtils.fString((String)arrItem.get(19));
//	    					          String PRD_CLS_ID = StrUtils.fString((String)arrItem.get(10));
//	    					          String PRD_DEPT_ID = StrUtils.fString((String)arrItem.get(46));
//	    					          String ARTIST = StrUtils.fString((String)arrItem.get(2));
//	    					          String HSCODE = StrUtils.fString((String)arrItem.get(2));
//	    					          String COO = StrUtils.fString((String)arrItem.get(28));
//	    					          String PURCHASEUOM = StrUtils.fString((String)arrItem.get(33));
//	    					          String SALESUOM = StrUtils.fString((String)arrItem.get(34));
//	    					          String INVENTORYUOM = StrUtils.fString((String)arrItem.get(3));
//	    					          String UOM = StrUtils.fString((String)arrItem.get(28));
//	    					          
//	    					        //PRD BRAND START
//				        			  	Hashtable prdBrand = new Hashtable();
//				        			  	Hashtable htBrandtype = new Hashtable();
//				        				htBrandtype.clear();
//				        			  	htBrandtype.put(IDBConstants.PLANT, plnt);
//				        				htBrandtype.put(IDBConstants.PRDBRANDID, PRD_BRAND);
//				        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
//				        				if (flag == false) {
//				        					prdBrand.clear();
//				        					prdBrand.put(IDBConstants.PLANT, plnt);
//				        					prdBrand.put(IDBConstants.PRDBRANDID, PRD_BRAND);
//				        					prdBrand.put(IDBConstants.PRDBRANDDESC, PRD_BRAND);
//				        					prdBrand.put(IConstants.ISACTIVE, "Y");
//				        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//				        					prdBrand.put(IDBConstants.LOGIN_USER, LOGIN_USER);
//				        					boolean PrdBrandInserted = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
//				        				}
//				        			  //PRD BRAND END
//				        				
//				        			  //PRD CLASS START
//				        				Hashtable htprdcls = new Hashtable();
//				        			  	Hashtable htclass = new Hashtable();
//				        			  	htprdcls.clear();
//				        			  	htprdcls.put(IDBConstants.PLANT, plnt);
//				        				htprdcls.put(IDBConstants.PRDCLSID,PRD_CLS_ID);
//				      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
//				      					if (flag == false) {
//				      						htclass.put(IDBConstants.PLANT, plnt);
//				      						htclass.put(IDBConstants.PRDCLSID, PRD_CLS_ID);
//				      						htclass.put(IDBConstants.PRDCLSDESC, PRD_CLS_ID);
//				      						htclass.put(IConstants.ISACTIVE, "Y");
//				      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//				      						htclass.put(IDBConstants.LOGIN_USER, LOGIN_USER);
//				      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
//				      					}
//				      				  //PRD CLASS END
//				      					
//				      				  //PRD TYPE START	
//				      					Hashtable htprdtype = new Hashtable();
//				        			  	Hashtable htprdtp = new Hashtable();
//				        			  	htprdtype.clear();
//				      					htprdtype.put(IDBConstants.PLANT, plnt);
//				      					htprdtype.put("PRD_TYPE_ID",ARTIST);
//									    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
//									    if (flag == false) {
//									    	htprdtp.clear();
//									    	htprdtp.put(IDBConstants.PLANT, plnt);
//									    	htprdtp.put("PRD_TYPE_ID", ARTIST);
//									    	htprdtp.put(IDBConstants.PRDTYPEDESC, ARTIST);
//									    	htprdtp.put(IConstants.ISACTIVE, "Y");
//									    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//									    	htprdtp.put(IDBConstants.LOGIN_USER, LOGIN_USER);
//			               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
//			                               }
//				      					//PRD TYPE END
//									    
//									    //PRD DEPT START
//									    Hashtable htprddept = new Hashtable();
//				        			  	Hashtable htdept = new Hashtable();
//				        			  	htprddept.put(IDBConstants.PLANT, plnt);
//				        			  	htprddept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
//				        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
//				        			  	if (flag == false) {
//											htdept.put(IDBConstants.PLANT, plnt);
//											htdept.put(IDBConstants.PRDDEPTID, PRD_DEPT_ID);
//											htdept.put(IDBConstants.PRDDEPTDESC, PRD_DEPT_ID);
//											htdept.put(IConstants.ISACTIVE, "Y");
//											htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//											htdept.put(IDBConstants.LOGIN_USER, LOGIN_USER);
//											boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
//										}
//									   //PRD DEPT END
//				        			  	
//				        			  //check if Purchase UOM exists 
//				        			  	Hashtable htInv = new Hashtable();
//				        			  	Hashtable HtPurchaseuom = new Hashtable();
//				        			  	HtPurchaseuom.put(IDBConstants.PLANT, plnt);
//				        			  	HtPurchaseuom.put("UOM", PURCHASEUOM);
//				        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
//				        			  	if (flag == false) {
//				        			  		htInv.clear();
//				        			  		htInv.put(IDBConstants.PLANT, plnt);
//				        			  		htInv.put("UOM", PURCHASEUOM);
//				        			  		htInv.put("UOMDESC", PURCHASEUOM);
//				        			  		htInv.put("Display", PURCHASEUOM);
//				        			  		htInv.put("QPUOM", "1");
//				        			  		htInv.put(IConstants.ISACTIVE, "Y");
//				        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//				        			  		htInv.put(IDBConstants.LOGIN_USER, LOGIN_USER);
//				        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
//				        			  	}
//				        			  	//END PURCHASE UOM
//				        			  	
//				        			  //check if Sales UOM exists 
//				        			  	Hashtable HtSalesuom = new Hashtable();
//				        			  	HtSalesuom.put(IDBConstants.PLANT, plnt);
//				        			  	HtSalesuom.put("UOM", SALESUOM);
//				        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
//				        			  	if (flag == false) {
//				        			  		htInv.clear();
//				        			  		htInv.put(IDBConstants.PLANT, plnt);
//				        			  		htInv.put("UOM", SALESUOM);
//				        			  		htInv.put("UOMDESC", SALESUOM);
//				        			  		htInv.put("Display", SALESUOM);
//				        			  		htInv.put("QPUOM", "1");
//				        			  		htInv.put(IConstants.ISACTIVE, "Y");
//				        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//				        			  		htInv.put(IDBConstants.LOGIN_USER, LOGIN_USER);
//				        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
//				        			  	}
//				        			  	//END SALES UOM
//				        			  	
//									   //check if Inventory UOM exists 
//				        			  	Hashtable HtInvuom = new Hashtable();
//				        			  	HtInvuom.put(IDBConstants.PLANT, plnt);
//				        			  	HtInvuom.put("UOM", INVENTORYUOM);
//				        			  	flag = new UomUtil().isExistsUom(HtInvuom);
//				        			  	if (flag == false) {
//								    	  htInv.clear();
//								    	  htInv.put(IDBConstants.PLANT,plnt);
//								    	  htInv.put("UOM", INVENTORYUOM);
//								    	  htInv.put("UOMDESC", INVENTORYUOM);
//								    	  htInv.put("Display",INVENTORYUOM);
//								    	  htInv.put("QPUOM", "1");
//								    	  htInv.put(IConstants.ISACTIVE, "Y");
//								    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//								    	  htInv.put(IDBConstants.LOGIN_USER,LOGIN_USER);
//								    	  boolean uomInserted = new UomUtil().insertUom(htInv);
//				        			  	}
//				        			  	//END INV UOM
//				        			  	
//				        			  	//check if Stk UOM exists 
//				        			  	Hashtable HtStkuom = new Hashtable();
//				        			  	HtStkuom.put(IDBConstants.PLANT, plnt);
//				        			  	HtStkuom.put("UOM", UOM);
//				        			  	flag = new UomUtil().isExistsUom(HtStkuom);
//				        			  	if (flag == false) {
//				        			  		htInv.clear();
//				        			  		htInv.put(IDBConstants.PLANT,plnt);
//				        			  		htInv.put("UOM", UOM);
//				        			  		htInv.put("UOMDESC", UOM);
//				        			  		htInv.put("Display",UOM);
//				        			  		htInv.put("QPUOM", "1");
//				        			  		htInv.put(IConstants.ISACTIVE, "Y");
//				        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
//				        			  		htInv.put(IDBConstants.LOGIN_USER,LOGIN_USER);
//				        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
//				        			  	}
//				        			  	//END STOCK UOM
//				        			  	
//				        			  //HSCODE
//				        			  	if (!new MasterUtil().isExistHSCODE(HSCODE, plnt)) 
//										{						
//							    			Hashtable htHS = new Hashtable();
//							    			htHS.put(IDBConstants.PLANT,plnt);
//							    			htHS.put(IDBConstants.HSCODE,HSCODE);
//							    			htHS.put(IDBConstants.LOGIN_USER,LOGIN_USER);
//							    			htHS.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
////											boolean insertflag = new MasterUtil().AddHSCODE(htHS);
//											boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
//											Hashtable htRecvHis = new Hashtable();
//											htRecvHis.clear();
//											htRecvHis.put(IDBConstants.PLANT, plnt);
//											htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
//											htRecvHis.put("ORDNUM","");
//											htRecvHis.put(IDBConstants.ITEM, "");
//											htRecvHis.put("BATNO", "");
//											htRecvHis.put(IDBConstants.LOC, "");
//											htRecvHis.put("REMARKS",HSCODE);
//											htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
//											htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
//											htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//											boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
//										}
//				        			  	//HSCODE END
//				        			  	
//				        			  	//COO 
//				        				if (!new MasterUtil().isExistCOO(COO, plnt)) 
//										{						
//							    			Hashtable htCoo = new Hashtable();
//							    			htCoo.put(IDBConstants.PLANT,plnt);
//							    			htCoo.put(IDBConstants.COO,COO);
//							    			htCoo.put(IDBConstants.LOGIN_USER,LOGIN_USER);
//							    			htCoo.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
////											boolean insertflag = new MasterUtil().AddCOO(htCoo);
//											boolean insertflag = new MasterDAO().InsertCOO(htCoo);
//											Hashtable htRecvHis = new Hashtable();
//											htRecvHis.clear();
//											htRecvHis.put(IDBConstants.PLANT, plnt);
//											htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
//											htRecvHis.put("ORDNUM","");
//											htRecvHis.put(IDBConstants.ITEM, "");
//											htRecvHis.put("BATNO", "");
//											htRecvHis.put(IDBConstants.LOC, "");
//											htRecvHis.put("REMARKS",COO);
//											htRecvHis.put(IDBConstants.CREATED_BY,LOGIN_USER);
//											htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
//											htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//											boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
//										}
//				        				//COO END
//				        				
//				        				//SUPPLIER START 
//				        				ArrayList arrCust = new CustUtil().getVendorDetails(vendno,PLANT);
//				        				if (arrCust.size() > 0) {
//				        				String CustCode = (String) arrCust.get(0);
//				        				String CustName = (String) arrCust.get(1);
//				        				if (!new CustUtil().isExistVendor(vendno, plnt) && !new CustUtil().isExistVendorName(CustName, plnt)) // if the Customer exists already 
//				        				{
//				        					String Addr1 = (String) arrCust.get(2);
//				        					String Addr2 = (String) arrCust.get(3);
//				        					String Addr3 = (String) arrCust.get(4);
//				        					String sAddr4 = (String) arrCust.get(15);
//				        					String sCountry = (String) arrCust.get(5);
//				        					String sZip = (String) arrCust.get(6);
//				        					String sCons = (String) arrCust.get(7);
//				        					String sContactName = (String) arrCust.get(8);
//				        					String sDesgination = (String) arrCust.get(9);
//				        					String TelNo = (String) arrCust.get(10);
//				        					String sHpNo = (String) arrCust.get(11);
//				        					String sEmail = (String) arrCust.get(12);
//				        					String sFax = (String) arrCust.get(13);
//				        					String sRemarks = (String) arrCust.get(14);
//				        					String isActive = (String) arrCust.get(16);
//				        					String sPayTerms = (String) arrCust.get(17);
//				        					String sPayMentTerms = (String) arrCust.get(39);
//				        					String sPayInDays = (String) arrCust.get(18);
//				        					String sState = (String) arrCust.get(19);
//				        					String sRcbno = (String) arrCust.get(20);
//				        					String CUSTOMEREMAIL = (String) arrCust.get(22);
//				        					String WEBSITE = (String) arrCust.get(23);
//				        					String FACEBOOK = (String) arrCust.get(24);
//				        					String TWITTER = (String) arrCust.get(25);
//				        					String LINKEDIN = (String) arrCust.get(26);
//				        					String SKYPE = (String) arrCust.get(27);
//				        					String OPENINGBALANCE = (String) arrCust.get(28);
//				        					String WORKPHONE = (String) arrCust.get(29);
//				        					String sTAXTREATMENT = (String) arrCust.get(30);
//				        					String sCountryCode = (String) arrCust.get(31);
//				        					String sBANKNAME = (String) arrCust.get(32);
//				        					String sBRANCH= (String) arrCust.get(33);
//				        					String sIBAN = (String) arrCust.get(34);
//				        					String sBANKROUTINGCODE = (String) arrCust.get(35);
//				        					String companyregnumber = (String) arrCust.get(36);
//				        					String PEPPOL = (String) arrCust.get(40);
//				        					String PEPPOL_ID = (String) arrCust.get(41);
//				        					String CURRENCY = (String) arrCust.get(37);
//				        					String transport = (String) arrCust.get(38);
//				        					String suppliertypeid = (String) arrCust.get(21);
//				        					Hashtable htsup = new Hashtable();
//				        					htsup.put(IDBConstants.PLANT,plnt);
//				        					htsup.put(IConstants.VENDOR_CODE, CustCode);
//				        					htsup.put(IConstants.VENDOR_NAME, CustName);
//				        					htsup.put(IConstants.companyregnumber,companyregnumber);
//				        					htsup.put("ISPEPPOL", PEPPOL);
//				        					htsup.put("PEPPOL_ID", PEPPOL_ID);
//				        					htsup.put("CURRENCY_ID", CURRENCY);
//				        					htsup.put(IConstants.NAME, sContactName);
//				        					htsup.put(IConstants.DESGINATION, sDesgination);
//				        					htsup.put(IConstants.TELNO, TelNo);
//				        					htsup.put(IConstants.HPNO, sHpNo);
//				        					htsup.put(IConstants.FAX, sFax);
//				        					htsup.put(IConstants.EMAIL, sEmail);
//				        					htsup.put(IConstants.ADDRESS1, Addr1);
//				        					htsup.put(IConstants.ADDRESS2, Addr2);
//				        					htsup.put(IConstants.ADDRESS3, Addr3);
//				        					htsup.put(IConstants.ADDRESS4, sAddr4);
//				        					if(sState.equalsIgnoreCase("Select State"))
//				        						sState="";
//				        					htsup.put(IConstants.STATE, StrUtils.InsertQuotes(sState));
//				        					htsup.put(IConstants.COUNTRY, StrUtils.InsertQuotes(sCountry));
//				        					htsup.put(IConstants.ZIP, sZip);
//				        					htsup.put(IConstants.USERFLG1, sCons);
//				        					htsup.put(IConstants.REMARKS, StrUtils.InsertQuotes(sRemarks));
//				        					htsup.put(IConstants.PAYTERMS, StrUtils.InsertQuotes(sPayTerms));
//				        					htsup.put(IConstants.payment_terms, StrUtils.InsertQuotes(sPayMentTerms));
//				        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
//				        					htsup.put(IConstants.ISACTIVE, isActive);
//				        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
//				        					htsup.put(IConstants.TRANSPORTID,transport);
//				        					htsup.put(IConstants.RCBNO, sRcbno);
//				        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
//				        					htsup.put(IConstants.WEBSITE,WEBSITE);
//				        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
//				        					htsup.put(IConstants.TWITTER,TWITTER);
//				        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
//				        					htsup.put(IConstants.SKYPE,SKYPE);
//				        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
//				        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
//				        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
//				        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
//				        			        	  sBANKNAME="";
//				        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
//				        			          htsup.put(IDBConstants.IBAN,sIBAN);
//				        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
//				        			          htsup.put("CRAT",new DateUtils().getDateTime());
//				        			          htsup.put("CRBY",LOGIN_USER);
//				        			          htsup.put("Comment1", " 0 ");
//				        			          boolean custInserted = new CustUtil().insertVendor(htsup);
//				        				}
//				        		  }//Supplier END
//		        				if (itemInserted) {
//		        					String alternateItemName = item; 
//		        					List<String> alternateItemNameLists = new ArrayList<String>();
//		        					alternateItemNameLists.add(alternateItemName);
//		        					boolean insertAlternateItem = new ItemUtil().insertAlternateItemLists(plnt, item, alternateItemNameLists);
//		        				}
//	                        }
//						}
							
						}
				}
				if (flag == true) {//Shopify Inventory Update
					if(isShopify.equalsIgnoreCase("1")){//13.01.23 Non Shopify Company data slow issue fix - added by Azees
   					Hashtable htCond = new Hashtable();
   					htCond.put(IConstants.PLANT, PLANT);
   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
   						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,item);						
						if(nonstkflag.equalsIgnoreCase("N")) {
   						String availqty ="0";
   						ArrayList invQryList = null;
   						htCond = new Hashtable();
   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,item, new ItemMstDAO().getItemDesc(PLANT, item),htCond);						
   						if (invQryList.size() > 0) {					
   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
//   								String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                availqty = (String)lineArr.get("AVAILABLEQTY");
                                System.out.println(availqty);
   							}
   							double availableqty = Double.parseDouble(availqty);
       						new ShopifyService().UpdateShopifyInventoryItem(PLANT, item, availableqty);
   						}	
						}
   					}
					}
   				}
				if(!flag) {
				if(isSales_Purchase.equalsIgnoreCase("1") && !plnt.equalsIgnoreCase("0")) {
	    		Hashtable ht = new Hashtable();
	    		ht.put(IDBConstants.PLANT, plnt);
				ht.put("PONO",pono);
	    		boolean podetex = new PoDetDAO().isExisit(ht);
				if(podetex==true) {
					boolean poadded = new PoHdrDAO().addPoHdr(pohdr);
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable<String, String> htPoMovhis = new Hashtable();
					htPoMovhis.put(IDBConstants.PLANT, plnt);
					htPoMovhis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
					htPoMovhis.put(IDBConstants.CUSTOMER_CODE, sCustCode);
					htPoMovhis.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htPoMovhis.put(IDBConstants.CREATED_BY, LOGIN_USER);
					htPoMovhis.put("MOVTID", "");
					htPoMovhis.put("RECID", "");
					htPoMovhis.put(IDBConstants.REMARKS, currentplntdesc+","+DONO+","+INVOICENO);
					htPoMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htPoMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					movHisDao.insertIntoMovHis(htPoMovhis);
					
					//new TblControlUtil().updateTblControlSeqNo(plnt, IConstants.INBOUND, "P", pono);
				}
					break process;
				}
				}
				}
			}
    		
    		if(isSales_Purchase.equalsIgnoreCase("1") && !plnt.equalsIgnoreCase("0")) {
    		Hashtable ht = new Hashtable();
    		ht.put(IDBConstants.PLANT, plnt);
			ht.put("PONO",pono);
    		boolean podetex = new PoDetDAO().isExisit(ht);
			if(podetex==true) {
				boolean poadded = new PoHdrDAO().addPoHdr(pohdr);
				MovHisDAO movHisDao = new MovHisDAO();
				Hashtable<String, String> htPoMovhis = new Hashtable();
				htPoMovhis.put(IDBConstants.PLANT, plnt);
				htPoMovhis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
				htPoMovhis.put(IDBConstants.CUSTOMER_CODE, sCustCode);
				htPoMovhis.put(IDBConstants.MOVHIS_ORDNUM, pono);
				htPoMovhis.put(IDBConstants.CREATED_BY, LOGIN_USER);
				htPoMovhis.put("MOVTID", "");
				htPoMovhis.put("RECID", "");
				htPoMovhis.put(IDBConstants.REMARKS, currentplntdesc+","+DONO+","+INVOICENO);
				htPoMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htPoMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				movHisDao.insertIntoMovHis(htPoMovhis);
				
				//new TblControlUtil().updateTblControlSeqNo(plnt, IConstants.INBOUND, "P", pono);
			}
    		}
	
			if (totalqty>0) {
				
				DoDetDAO _DoDetDAO = new DoDetDAO();
//				DateUtils dateutils = new DateUtils();
				Hashtable htRecvDet = new Hashtable();
				htRecvDet.clear();
				htRecvDet.put(IConstants.PLANT, PLANT);
				htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
				htRecvDet.put("GINO", INVOICENO);                    
				htRecvDet.put(IConstants.DODET_DONUM, DONO);
				htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
				htRecvDet.put(IConstants.AMOUNT, priceval);
				htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
//				htRecvDet.put("CRAT",DateUtils.getDateTime());
				htRecvDet.put("CRAT",orderdate);
				htRecvDet.put("CRBY",LOGIN_USER);
				htRecvDet.put("UPAT",DateUtils.getDateTime());
				flag = _DoDetDAO.insertGINOtoInvoice(htRecvDet);
				
				RecvDetDAO _RecvDetDAO = new RecvDetDAO();
				Hashtable htShipHdr = new Hashtable();
				htShipHdr.put(IConstants.PLANT, PLANT);
				htShipHdr.put("DIRTYPE", TransactionConstants.ORD_PICK_ISSUE);
				htShipHdr.put(IConstants.ORDNUM, DONO);
				htShipHdr.put("CLEARING_AGENT_ID", CLEARAGENT);                    
				htShipHdr.put("CLEARANCETYPE", CLEARANCETYPE);
				if(TRANSPORTID.equalsIgnoreCase(""))
                	TRANSPORTID="0";
                htShipHdr.put("TRANSPORTID", TRANSPORTID);                    
                htShipHdr.put("CONTACTNAME", CONTACT_NAME);                    
                htShipHdr.put("TRACKINGNO", TRACKINGNO);
				htShipHdr.put("RECEIPTNO", INVOICENO);                    
				htShipHdr.put("CRAT",DateUtils.getDateTime());
				htShipHdr.put("CRBY",LOGIN_USER);
//				htShipHdr.put("UPAT",DateUtils.getDateTime());
//				htShipHdr.put("UPBY",LOGIN_USER);
//				flag = _RecvDetDAO.insertShippingHdr(htShipHdr);
                
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
    			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
    			htRecvHis.put(IDBConstants.REMARKS, DONO);        					
    			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
    			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
    			flag = movHisDao.insertIntoMovHis(htRecvHis);

				String msg="";
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				//resetCounter();
				DbBean.CommitTran(ut);	
				String msgflag=StrUtils.fString((String)	issMat_HM.get("msgflag"));
				if(msgflag.length()>0){
					request.getSession().setAttribute("RESULT",	msgflag	);
				}else {
					if(alertitem.equalsIgnoreCase("")) {
						//request.getSession().setAttribute("RESULT","Sales Order : " + DONO + "  Picked/Issued successfully!");
					msg = " Sales Order : " + DONO + " Picked/Issued successfully!";
					}else {
						//request.getSession().setAttribute("RESULT","Sales Order : " + DONO + "  Picked/Issued successfully! <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>");
						msg = "Sales Order : " + DONO + "  Picked/Issued successfully! <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>";
					}
//					request.getSession().setAttribute("RESULT","Sales Order : " + DONO + "  Picked/Issued successfully!");
				}
				/*----------------- automatic invoice-----------------------*/
				String AutoConvStatus = new PlantMstDAO().getIsSalesPickandIssueToInvoice(PLANT);
				if(AutoConvStatus.equalsIgnoreCase("1")) {
					String invstatus = createInvoice(request, response, issMat_HM, INVOICENO, PLANT, LOGIN_USER);
					if(invstatus.equalsIgnoreCase("ok")) {
						flag = true;
						msg = "Picked/Issued! & Invoice Created Successfully" ;
					}else {
						flag = false;
						msg = "Picked/Issued successfully! Invoice not created";
					}
				}
				request.getSession().setAttribute("RESULT",msg);
				request.getSession().setAttribute("rdata",request);				
				if (ajax) {
					String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
					Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER_API);
					String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
					request.setAttribute("chkdDoNo", new String[] {request.getParameter("DONO")});
					if ("both".equals(sendAttachment) || "bothwithgino".equals(sendAttachment) || "do".equals(sendAttachment) || "dowithgino".equals(sendAttachment)) {
						if (sendAttachment.contains("withgino")) {
							request.setAttribute("printwithigino", "Y");
						}
						viewDOReport(request, response, "printDOWITHBATCH");
					}
					//	Print with GRNO option is not available for invoice
					if ("both".equals(sendAttachment) || "bothwithgino".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
						viewInvoiceReport(request, response, "printInvoiceWITHBATCH");
					}
					resultJson.put("MESSAGE", msgflag);
					resultJson.put("ERROR_CODE", "100");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}else {
				response.sendRedirect("../salestransaction/orderpickissue?action=View&PLANT="+ PLANT + "&DONO=" + DONO);			
				}
				/*response.sendRedirect("jsp/IssueingGoodsInvoice.jsp?DONO="
						+DONO+"&invcusnum="+CUST_NAME
						+"&invcuscode="+StrUtils.fString(request.getParameter("CUST_CODE"))
						+"&invnum="+invnumber
						+"&CUST_CODE="+_DOUtil.getCustCode(PLANT, DONO)
						+"&result="+msgflag
						+"&cmd=IssueingGoodsInvoice");*/
			} else {
				DbBean.RollbackTran(ut);
				String message = "Failed to Pick/Issue Sales Order : "
						+ DONO;
				request.getSession()
						.setAttribute(
								"RESULTERROR",
								message);
				if (ajax) {
					resultJson.put("MESSAGE", message);
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}else {
					response.sendRedirect("../salestransaction/orderpickissue?result=catchrerror");
				}
			}
			
			}
			
		 catch (Exception e) {
			 
			 
			 if (totalqty>0) {
					
					DoDetDAO _DoDetDAO = new DoDetDAO();
//					DateUtils dateutils = new DateUtils();
					Hashtable htRecvDet = new Hashtable();
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
					htRecvDet.put("GINO", INVOICENO);                    
					htRecvDet.put(IConstants.DODET_DONUM, DONO);
					htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
					htRecvDet.put(IConstants.AMOUNT, priceval);
					htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
					htRecvDet.put("CRAT",DateUtils.getDateTime());
					htRecvDet.put("CRBY",LOGIN_USER);
					htRecvDet.put("UPAT",DateUtils.getDateTime());
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
	    			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
	    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
	    			htRecvHis.put(IDBConstants.REMARKS, DONO);        					
	    			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
	    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
	    			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	    			flag = movHisDao.insertIntoMovHis(htRecvHis);

					new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
					INVOICENO="";
				}
			 
			// DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			String message = ThrowableUtil.getMessage(e);
			request.getSession().setAttribute("CATCHERROR", message);
			if (ajax) {
				resultJson.put("MESSAGE", message);
				resultJson.put("ERROR_CODE", "98");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				response
				.sendRedirect("../salestransaction/orderpickissue?action=View&PLANT="
						+ PLANT + "&DONO=" + DONO 
						+"&LOC="
						+LOC
						+"&ISSUEDATE="
						+ISSUEDATE
						+"&REF="
						+REMARKS
						+ "&allChecked="
						+allChecked
						+"&fullReceive="
						+fullIssue
						+"&result=catchrerror"+"&INVOICENO="+INVOICENO);
				throw e;
			}
		}
		/*if (flag) {
			request.getSession().setAttribute("RESULT",
					"Goods Issued successfully!");
			response.sendRedirect("jsp/OutBoundsOrderBulkIssue.jsp?action=View&PLANT="
					+ PLANT + "&DONO=" + DONO
					+"&LOC="
					+LOC
					+"&REF="
					+REMARKS);
		}*/
		return xmlStr;
	}
	
	public String createInvoice(HttpServletRequest request,
			HttpServletResponse response,Map map,String gino, String plant, String username) {
		//HttpServletResponse response,Map map,List<DoDet> dodetlist,String gino, String plant, String username) {

		/* InvoiceHdr*/
		String CustCode = "", invoiceNo = "", dono = "", invoiceDate = "", dueDate = "", payTerms = "",cmd = "",TranId = "",salesloc="",orderdiscount="0",
		itemRates = "", discount = "0", discountType = "", discountAccount = "", shippingCost = "",isexpense = "0",taxamount = "0",
		adjustment = "", subTotal = "", totalAmount = "", invoiceStatus = "", note = "",empno="",terms="",custName="",custName1="",empName="",taxtreatment="",
		shipId = "", shipCust = "", incoterm = "", origin = "", deductInv = "",currencyid="",currencyuseqt="0",projectid="",transportid="",orderdiscstatus = "0",discstatus = "0",
		shipstatus = "0",taxid = "",orderdisctype = "%",gst="0",jobNum="";//shippingcost="",
		/*InvoiceDet*/
		List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),notesexp = new ArrayList(),
				cost = new ArrayList(), detDiscount = new ArrayList(),detDiscounttype = new ArrayList(), taxType = new ArrayList(),DETID= new ArrayList(),
				amount = new ArrayList(),edit_item=new ArrayList<>(),edit_qty=new ArrayList<>(),tlnno= new ArrayList(),dolnno= new ArrayList();
		List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),index = new ArrayList(),addcost = new ArrayList(),addtype = new ArrayList(),Ucost = new ArrayList(),
				 ordQty = new ArrayList(), convcost= new ArrayList(), is_cogs_set = new ArrayList();
		List<Hashtable<String,String>> invoiceDetInfoList = null;
		List<Hashtable<String,String>> invoiceAttachmentList = null;
		List<Hashtable<String,String>> invoiceAttachmentInfoList = null;
		Hashtable<String,String> invoiceDetInfo = null;
		Hashtable<String,String> invoiceAttachment = null;
		UserTransaction ut = null;
		InvoiceUtil invoiceUtil = new InvoiceUtil();
		InvoiceDAO invoiceDAO = new InvoiceDAO();
		ShipHisDAO shipHisDAO = new ShipHisDAO();
//		DateUtils dateutils = new DateUtils();
		MovHisDAO movHisDao = new MovHisDAO();
		boolean isAdded = false;
		boolean isAmntExceed = false;
		boolean Isconvcost=false;
		String result="", amntExceedMsg ="";
		int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0, detDiscounttypeCount  = 0,addcostCount  = 0,addtypeCount  = 0,UcostCount  = 0,
				taxTypeCount  = 0,DETIDCount  = 0, amountCount  = 0, notesexpcount =0,editItemCount=0,editQtyCount=0,dolnnoCount = 0;
		int locCount  = 0,batchCount  = 0,uomCount  = 0, idCount = 0, ordQtyCount = 0;//,convcostCount = 0,is_cogs_setCount=0,tlnnoCount = 0,;
		/*others*/
		//String personIncharge="",ctype="",fitem="",floc="",floc_type_id="",floc_type_id2="",fmodel="",fuom="";
		try{
			
			
	    	
			//invoiceNo = 
			//subTotal =
			//totalAmount = 
			//taxamoun 
			//shipId=
			//CustCode = doHdr.getCustCode();
			//custName = doHdr.getCustName();
			String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(plant);
			CustCode = (String)map.get(IConstants.CUSTOMER_CODE);
			custName = (String)map.get(IConstants.CUSTOMER_NAME);
			dono = (String)map.get(IConstants.DODET_DONUM);
			invoiceDate = (String)map.get(IConstants.ISSUEDATE);
			 DoHdr doHdr =new DoHdrDAO().getDoHdrById(plant, dono);
			 custName1 = doHdr.getCustName();
			//dono = doHdr.getDONO();
			dueDate ="";
			payTerms ="";
			empno ="";
			empName = "";
			itemRates ="0";
			discount ="0";
			discountType = doHdr.getCURRENCYID();
			orderdiscount ="0";
			shippingCost = String.valueOf(doHdr.getSHIPPINGCOST()/doHdr.getCURRENCYUSEQT());
			adjustment = "0";
			invoiceStatus="Open";
			terms ="";
			note="";
			salesloc = doHdr.getSALES_LOCATION();
			isexpense ="0";
			taxtreatment = doHdr.getTAXTREATMENT();
			shipCust=doHdr.getCustCode();
			origin="sales";
			deductInv="0";
			incoterm="";
			currencyid= doHdr.getCURRENCYID();
			currencyuseqt = String.valueOf(doHdr.getCURRENCYUSEQT());
			orderdiscstatus="1";
			discstatus="1";
			shipstatus="0";
			taxid = String.valueOf(doHdr.getTAXID());
			transportid = String.valueOf(doHdr.getTRANSPORTID());
			gst = String.valueOf(doHdr.getOUTBOUND_GST());
			jobNum = doHdr.getJobNum();
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			invoiceNo = _TblControlDAO.getNextOrder(plant,username,"INVOICE");
			
			//subTotal =
			//totalAmount = 
			//taxamount =
			//shipId=
			
			 String querystrings1 = "A.ISSUEDATE,ISNULL(A.UNITPRICE,0)* ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) UNITPRICE,"
 					+"ISNULL((SELECT TOP 1 ISNULL(TAX_TYPE,'') TAX_TYPE FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) TAX_TYPE,"
 					+"ISNULL((SELECT TOP 1 ISNULL(UNITMO,'') UOM FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) UOM,"
 					+"ISNULL((SELECT TOP 1 ISNULL(UNITCOST,'') DOCOST FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) DOCOST,"
 					+"ISNULL((SELECT TOP 1 ISNULL(ADDONAMOUNT,'') DOAOD FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) DOAOD,"
 					+"ISNULL((SELECT TOP 1 ISNULL(ADDONTYPE,'') DOAODTYPE FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) DOAODTYPE,"
 					+ "A.ITEM,C.ITEMDESC,C.CATLOGPATH,A.PICKQTY,ISNULL((SELECT B.OUTBOUND_GST FROM [" + plant + "_DOHDR] B WHERE A.DONO = B.DONO),0) OUTBOUND_GST,ISNULL(D.TAXTREATMENT,'')  TAXTREATMENT,"
 					+"ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = D.CURRENCYID),'') DISPLAY,D.CURRENCYID,ISNULL(D.ORDERDISCOUNTTYPE,'') ORDERDISCOUNTTYPE,ISNULL(D.EMPNO,'') EMPNAME,"
 					+"ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) CURRENCYUSEQT,"
 					+"ISNULL((SELECT COST FROM "+ plant +"_ITEMMST E WHERE C.ITEM = E.ITEM),'') as UNITCOST,"
 					+"ISNULL((SELECT INCPRICE FROM "+ plant +"_ITEMMST E WHERE C.ITEM = E.ITEM),'') as INCPRICE,"
 					+"ISNULL((SELECT CPPI FROM "+ plant +"_ITEMMST E WHERE C.ITEM = E.ITEM),'') as CPPI,"
 					+"ISNULL((SELECT P.ORDERDISCOUNT FROM ["+ plant +"_DOHDR] P WHERE A.DONO = P.DONO),0) ORDERDISCOUNT,ISNULL(A.UNITPRICE,0) BASECOST,"
 					+"CAST(ISNULL(A.UNITPRICE,0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_DODET] T WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) AS DECIMAL(25,5)) AS CONVCOST,"
 					+ " ISNULL(D.SALES_LOCATION,'') SALES_LOCATION, ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE D.SALES_LOCATION = C.STATE),'') as STATE_PREFIX,a.dolno as LNNO";
					 String querystrings2 = "[" + plant + "_SHIPHIS] A JOIN [" + plant + "_DOHDR] D ON A.DONO = D.DONO JOIN [" + plant + "_ITEMMST] C ON A.ITEM = C.ITEM ";
					
					 String cgroup =" order by a.dolno";
					        
					 Hashtable ht=new Hashtable();
					 ht.put("INVOICENO", gino);
					 ht.put("PLANT", plant);
			
			ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings1,querystrings2, ht,cgroup);
			//List<DoDet> dodetail = new DoDetDAO().getDoDetById(plant, dono);

			//Double subtotaldet = 0.0;
			double dsubTotal =0;
		    double dtotalAmount =0;
		    double dtaxTotal =0;
			//for (DoDet doDet : dodetail) {
			if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    	Map m=(Map)listQry.get(i);
			    //item.add(itemCount, doDet.getITEM());
			    item.add(itemCount, (String)m.get("ITEM"));
			    itemCount++;

			    dolnno.add(dolnnoCount, (String)m.get("LNNO"));
			    
			    accountName.add(accountNameCount, "Local sales - retail");
			    accountNameCount++;

			    //qty.add(qtyCount, String.valueOf(doDet.getQTYOR()));
			    qty.add(qtyCount, (String)m.get("PICKQTY"));
			    qtyCount++;

			    //cost.add(costCount, String.valueOf(doDet.getUNITPRICE()));
			    cost.add(costCount, (String)m.get("UNITPRICE"));
			    costCount++;

			    detDiscount.add(detDiscountCount, "0");
			    detDiscountCount++;

			    detDiscounttype.add(detDiscounttypeCount, doHdr.getCURRENCYID());
			    detDiscounttypeCount++;

			    taxType.add(taxTypeCount, (String)m.get("TAX_TYPE"));
			    taxTypeCount++;
			    
			    double dCost = Double.parseDouble((String)m.get("UNITPRICE"));
			    double dQty = Double.parseDouble((String)m.get("PICKQTY"));
				double dAmount = dCost * dQty;

				//amount.add(amountCount, String.valueOf((doDet.getUNITPRICE() * doDet.getQTYOR().doubleValue())));
			    amount.add(amountCount, String.valueOf(dAmount));
			    amountCount++;

			    //uom.add(uomCount, doDet.getUNITMO());
			    uom.add(uomCount, StrUtils.fString((String)m.get("UOM")));
			    uomCount++;

			    //ordQty.add(ordQtyCount, String.valueOf(doDet.getQTYOR()));
			    ordQty.add(ordQtyCount, (String)m.get("PICKQTY"));
			    ordQtyCount++;
			    
			    addcost.add(addcostCount, (String)m.get("DOCOST"));
				addcostCount++;
				
				addtype.add(addtypeCount, (String)m.get("DOAODTYPE"));
				addtypeCount++;
				
				Ucost.add(UcostCount, (String)m.get("DOAOD"));
				UcostCount++;

			    dsubTotal += dQty * (dCost / doHdr.getCURRENCYUSEQT());
			}
			}
	    	
			subTotal = String.valueOf(dsubTotal);			
			/*FinCountryTaxType  taxtypecounty = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(doHdr.getTAXID());
			double taxamountdouble = 0.0;
			if(doHdr.getTAXID() > 0){
			if(taxtypecounty.getISZERO() == 0) {
				taxamountdouble =  ((subtotaldet/100)*doHdr.getOUTBOUND_GST()); 
			}
			} else
				taxamountdouble =  ((subtotaldet/100)*doHdr.getOUTBOUND_GST());//taxid zero issue.. fix by Azees
			*/
			
			FinCountryTaxType fintaxtype= new FinCountryTaxType();
		    String ptaxtype="";
		    String ptaxiszero="1";
		    String ptaxisshow ="0";
		    if(doHdr.getTAXID() != 0) {
            	fintaxtype =new FinCountryTaxTypeDAO().getCountryTaxTypesByid(doHdr.getTAXID());
            	ptaxtype=String.valueOf(fintaxtype.getTAXTYPE());
            	ptaxiszero=String.valueOf(fintaxtype.getISZERO());
            	ptaxisshow=String.valueOf(fintaxtype.getSHOWTAX());
            }
		    
		    String isshtax=String.valueOf(doHdr.getISSHIPPINGTAX());
		    String isorderdiscounttax=String.valueOf(doHdr.getISDISCOUNTTAX());
		    orderdisctype=doHdr.getORDERDISCOUNTTYPE();
			double oddiscount = doHdr.getORDERDISCOUNT();
		    if(!doHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
		    	 String actualDiscountCost =new DoHdrDAO().getActualDiscoutForInvoice(plant, dono);
		    	 oddiscount = Double.valueOf(actualDiscountCost) * doHdr.getCURRENCYUSEQT();
		    }
		    
			if(orderdisctype.toString().equalsIgnoreCase("%"))
		    	orderdiscount = String.valueOf(( dsubTotal* (oddiscount /100)));
		    else
		    	orderdiscount = String.valueOf(((oddiscount) / (Double.parseDouble(currencyuseqt))));
		    
		    if(!itemRates.equalsIgnoreCase("0"))
		    {
		    	if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow .equalsIgnoreCase("1")){					
					double taxsubtotal =(100*dsubTotal) / (100+doHdr.getOUTBOUND_GST());
					dtotalAmount = taxsubtotal;
					dsubTotal = taxsubtotal;
				}
		    }
		    
		    if(itemRates.equalsIgnoreCase("0")){
				if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow.equalsIgnoreCase("1")){
					dtaxTotal = dtaxTotal + ((dsubTotal/100)*doHdr.getOUTBOUND_GST());
					
					if(isshtax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) + ((Double.parseDouble(shippingCost)/100)*doHdr.getOUTBOUND_GST());
					}
					
					if(isorderdiscounttax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) - ((Double.parseDouble(orderdiscount)/100)*doHdr.getOUTBOUND_GST());
					}
				}
		 }else{
				if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow.equalsIgnoreCase("1")){
					
					dtaxTotal = (dtaxTotal) + ((dsubTotal/100)*doHdr.getOUTBOUND_GST());
					
					if(isshtax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) + ((Double.parseDouble(shippingCost)/100)*doHdr.getOUTBOUND_GST());
					}
					
					if(isorderdiscounttax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) - ((Double.parseDouble(orderdiscount)/100)*doHdr.getOUTBOUND_GST());
					}
					
				}
		}
		    
		    if(ptaxisshow.equalsIgnoreCase("0"))
		    	dtaxTotal=0;
		    
		    dtotalAmount = ((dsubTotal)- Double.parseDouble(orderdiscount) + (dtaxTotal)
					 + Double.parseDouble(shippingCost) + Double.parseDouble(adjustment));
		    //orderdiscount = StrUtils.addZeroes((Double.parseDouble(orderdiscount) / (Double.parseDouble(currencyuseqt))), numberOfDecimal);
		    orderdiscount = StrUtils.addZeroes(Double.parseDouble(orderdiscount), numberOfDecimal);
		    subTotal = String.valueOf(dsubTotal);
		    totalAmount = StrUtils.addZeroes(dtotalAmount, numberOfDecimal);
		    taxamount =String.valueOf(dtaxTotal);
		    
		    //subTotal = String.valueOf(dsubTotal / (Double.parseDouble(currencyuseqt)));
		    //totalAmount = StrUtils.addZeroes((dtotalAmount / (Double.parseDouble(currencyuseqt))), numberOfDecimal);
		    //taxamount =String.valueOf(dtaxTotal / (Double.parseDouble(currencyuseqt)));
			
			//double totaldouble = subtotaldet + (doHdr.getSHIPPINGCOST()/doHdr.getCURRENCYUSEQT())+taxamountdouble;
			//taxamount = String.valueOf(taxamountdouble);
			//totalAmount = String.valueOf(totaldouble);
			
			totalAmount = StrUtils.addZeroes(dtotalAmount, numberOfDecimal);
		    taxamount =String.valueOf(dtaxTotal);
			
			String CURRENCYID = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			if(currencyid.isEmpty())
				if(!CURRENCYID.isEmpty())
					currencyid=CURRENCYID;
		
			
			//////////////////////
			invoiceDetInfoList = new ArrayList<Hashtable<String,String>>();
			Hashtable invoiceHdr =new Hashtable(); 
			invoiceHdr.put("PLANT", plant);
			invoiceHdr.put("CUSTNO", CustCode);
			invoiceHdr.put("INVOICE", invoiceNo);
			invoiceHdr.put("GINO",gino);
			invoiceHdr.put("DONO", dono);
			invoiceHdr.put("INVOICE_DATE", invoiceDate);
			invoiceHdr.put("DUE_DATE", dueDate);
			invoiceHdr.put("PAYMENT_TERMS", payTerms);
			invoiceHdr.put("EMPNO", empno);
			invoiceHdr.put("ITEM_RATES", itemRates);
			invoiceHdr.put("DISCOUNT", discount);
			invoiceHdr.put("ORDER_DISCOUNT", orderdiscount);
			invoiceHdr.put("DISCOUNT_TYPE", discountType);
			invoiceHdr.put("DISCOUNT_ACCOUNT", discountAccount);
			invoiceHdr.put("SHIPPINGCOST", shippingCost);
			invoiceHdr.put("ADJUSTMENT", adjustment);
			invoiceHdr.put("SUB_TOTAL", subTotal);
			invoiceHdr.put("TOTAL_AMOUNT", totalAmount);
			invoiceHdr.put("BILL_STATUS", invoiceStatus);
			invoiceHdr.put("NOTE", note);
			invoiceHdr.put("TERMSCONDITIONS", terms);
			invoiceHdr.put("CRAT",DateUtils.getDateTime());
			invoiceHdr.put("CRBY",username);
			invoiceHdr.put("UPAT",DateUtils.getDateTime());
			invoiceHdr.put("SALES_LOCATION", salesloc);
			invoiceHdr.put("ISEXPENSE",isexpense);
			invoiceHdr.put("TAXTREATMENT",taxtreatment);
			invoiceHdr.put("TAXAMOUNT",taxamount);
			invoiceHdr.put("SHIPPINGID",shipId);
			invoiceHdr.put("SHIPPINGCUSTOMER",shipCust);
			invoiceHdr.put("INCOTERMS",incoterm);
			invoiceHdr.put("ORIGIN",origin);
			invoiceHdr.put("DEDUCT_INV",deductInv);
			invoiceHdr.put("CURRENCYUSEQT",currencyuseqt);
			invoiceHdr.put("ORDERDISCOUNTTYPE",orderdisctype);
			invoiceHdr.put("TAXID",taxid);
			invoiceHdr.put("ISDISCOUNTTAX",discstatus);
			invoiceHdr.put("ISORDERDISCOUNTTAX",orderdiscstatus);
			invoiceHdr.put("ISSHIPPINGTAX",shipstatus);
			invoiceHdr.put("PROJECTID",projectid);
			invoiceHdr.put("TRANSPORTID",transportid);
			invoiceHdr.put("OUTBOUD_GST",gst);
			invoiceHdr.put(IDBConstants.CURRENCYID, currencyid);
			invoiceHdr.put("JobNum",jobNum);

			int invoiceHdrId=0;
			BillDAO itemCogsDao=new BillDAO();
			Costofgoods costofGoods=new CostofgoodsImpl();
	
				Map resultMap = getOutstandingAmountForCustomer(CustCode, Double.parseDouble(totalAmount), plant);
				isAmntExceed = (boolean) resultMap.get("STATUS");
				amntExceedMsg = (String) resultMap.get("MSG");
				if(!isAmntExceed) {
					invoiceHdr.put("CREDITNOTESSTATUS","0");
					invoiceHdr.put("TOTAL_PAYING","0");
					invoiceHdrId = invoiceUtil.addInvoiceHdr(invoiceHdr, plant);
				}

			
			if(invoiceHdrId > 0) {
				for(int i =0 ; i < item.size() ; i++){
					int lnno = i+1;
					String convDiscount=""; 
					String convCost = String.valueOf((Double.parseDouble((String) cost.get(i)) / Double.parseDouble(currencyuseqt)));
					if(Isconvcost)
						convCost = String.valueOf((Double.parseDouble((String) convcost.get(i)) / Double.parseDouble(currencyuseqt)));
					if(!detDiscounttype.get(i).toString().contains("%"))
					{
						convDiscount = String.valueOf((Double.parseDouble((String) detDiscount.get(i)) / Double.parseDouble(currencyuseqt)));
					}
					else
						convDiscount = (String) detDiscount.get(i);
					String convAmount = String.valueOf((Double.parseDouble((String) amount.get(i)) / Double.parseDouble(currencyuseqt)));
					
					invoiceDetInfo = new Hashtable<String, String>();
					invoiceDetInfo.put("PLANT", plant);
					//invoiceDetInfo.put("LNNO", Integer.toString(lnno));
					invoiceDetInfo.put("LNNO", (String) dolnno.get(i));
					if(cmd.equalsIgnoreCase("Edit"))
						invoiceDetInfo.put("INVOICEHDRID", TranId);						
					else
						invoiceDetInfo.put("INVOICEHDRID", Integer.toString(invoiceHdrId));
					//invoiceDetInfo.put("INVOICEHDRID", TranId);							
					invoiceDetInfo.put("ITEM", (String) item.get(i));
					invoiceDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
					invoiceDetInfo.put("QTY", (String) qty.get(i));
					invoiceDetInfo.put("UNITPRICE", convCost);
					invoiceDetInfo.put("TAX_TYPE", (String) taxType.get(i));
					invoiceDetInfo.put("AMOUNT", convAmount);
					invoiceDetInfo.put("CRAT",DateUtils.getDateTime());
					invoiceDetInfo.put("CRBY",username);
					invoiceDetInfo.put("UPAT",DateUtils.getDateTime());
					invoiceDetInfo.put("DISCOUNT", convDiscount);
					invoiceDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
					invoiceDetInfo.put("UOM", (String) uom.get(i));
					invoiceDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
					invoiceDetInfo.put("ADDONAMOUNT", (String)addcost.get(i));
					invoiceDetInfo.put("ADDONTYPE", (String)addtype.get(i));
					double uconv = ((Double.valueOf((String)Ucost.get(i)))/(Double.valueOf(currencyuseqt)));
					invoiceDetInfo.put("UNITCOST", (String.valueOf(uconv)));

					if(loc.size() > 0) {
						invoiceDetInfo.put("LOC", (String) loc.get(i));
						invoiceDetInfo.put("BATCH", (String) batch.get(i));
					}
					
					invoiceDetInfoList.add(invoiceDetInfo);
					
					int cogsCnt=itemCogsDao.addItemCogs(costofGoods.soldProductDetails((String)qty.get(i), (String)item.get(i), plant, dueDate),plant);
					System.out.println("Insert ItemCogs Status :"+ cogsCnt);

					
					
				}
				
				isAdded = invoiceUtil.addMultipleInvoiceDet(invoiceDetInfoList, plant);

				if(isAdded) {

					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					System.out.println("invoice Status"+invoiceStatus);
					if(!invoiceStatus.equalsIgnoreCase("Draft"))
					{
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(invoiceDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("INVOICE");
						journalHead.setTRANSACTION_ID(invoiceNo);
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(DateUtils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						CustMstDAO cusDAO=new CustMstDAO();
						ItemMstDAO itemMstDAO=new ItemMstDAO();
						Double totalItemNetWeight=0.00;
						Double totalCostofGoodSold=0.00;
						
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) CustCode);
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject CusJson=cusDAO.getCustomerName(plant, (String) CustCode);
							if(!CusJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant, CusJson.getString("CNAME"));
								if(coaJson1.isEmpty() || coaJson1.isNullObject())
								{
									coaJson1=coaDAO.getCOAByName(plant, CusJson.getString("CUSTNO")+"-"+CusJson.getString("CNAME"));
								}
							}
						}
						if(coaJson1.isEmpty() || coaJson1.isNullObject())
						{
							
						}
						else
						{
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							//journalDetail_1.setACCOUNT_NAME((String) CustCode);
							if(coaJson1.getString("account_name")!=null) {
								journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
							}
							journalDetail_1.setDEBITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_1);
						}
						
						if(discount.isEmpty())
						{
							discount="0.00";
						}
						Double discountFrom = Double.parseDouble(discount);
						Double orderDiscountFrom=0.00;
						if(!orderdiscount.isEmpty())
						{
							orderDiscountFrom=Double.parseDouble(orderdiscount);
							
							if(orderdisctype.toString().equalsIgnoreCase("%"))
								orderdiscount=Double.toString((Double.parseDouble(subTotal)*orderDiscountFrom)/100);
						}
						if(discountFrom>0 || orderDiscountFrom>0)
						{
							if(!discountType.isEmpty())
							{
								if(discountType.equalsIgnoreCase("%"))
								{
									Double subTotalAfterOrderDiscount=Double.parseDouble(subTotal);
									discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
								}
							}
							//discountFrom=discountFrom+orderDiscountFrom;
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discounts given - COS");
							journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
							journalDetail_3.setACCOUNT_NAME("Discounts given - COS");
							journalDetail_3.setDEBITS(discountFrom);
							journalDetails.add(journalDetail_3);
						}
						
						for(Map invDet:invoiceDetInfoList)
						{
							Double quantity=Double.parseDouble(invDet.get("QTY").toString());
							String netWeight=itemMstDAO.getItemNetWeight(plant, invDet.get("ITEM").toString());
							if(netWeight!=null && !"".equals(netWeight))
							{
								Double Netweight=quantity*Double.parseDouble(netWeight);
								totalItemNetWeight+=Netweight;
							}
							
							
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) invDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) invDet.get("ACCOUNT_NAME"));
							//double discpercal = (Double.parseDouble(invDet.get("AMOUNT").toString()) * 100)/Double.parseDouble(subTotal);
							//double deductamt = (Double.parseDouble(orderdiscount.toString())/100)*discpercal;
							//journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())- deductamt);
							
							if(!orderdisctype.toString().equalsIgnoreCase("%")) {
								journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())-Double.parseDouble(orderDiscountFrom.toString())/invoiceDetInfoList.size());
							}else {
								Double jodamt = (Double.parseDouble(invDet.get("AMOUNT").toString())/100)*Double.parseDouble(orderDiscountFrom.toString());
								journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString()) -jodamt);
							}
							
							/*
							 * String avg=new InvUtil().getCostOfGoods((String)invDet.get("ITEM"),plant);
							 * if(avg!=null && !"".equals(avg)) {
							 * totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }else { avg=new
							 * InvUtil().getAvgCostofItem((String)invDet.get("ITEM"),plant); if(avg!=null &&
							 * !"".equals(avg)) { totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }
							 * 
							 * }
							 */
							/**/
							int invoicesCount = new InvoiceDAO().invoiceWoCOGSCount((String)invDet.get("ITEM"), plant);
							if(invoicesCount == 1) {
								Map invDetail = new InvMstDAO().getInvDataByProduct((String)invDet.get("ITEM"), plant);
								double bill_qty = 0, invoiced_qty = 0;//inv_qty=0, unbill_qty = 0, 
//								inv_qty = Double.parseDouble((String)invDetail.get("INV_QTY"));
								bill_qty = Double.parseDouble((String)invDetail.get("BILL_QTY"));
//								unbill_qty = Double.parseDouble((String)invDetail.get("UNBILL_QTY"));
								invoiced_qty = Double.parseDouble((String)invDetail.get("INVOICE_QTY"));
								
								bill_qty = bill_qty - invoiced_qty;
								
								if(bill_qty >= quantity) {
							/**/
								ArrayList invQryList;
								Hashtable ht_cog = new Hashtable();
								ht_cog.put("a.PLANT",plant);
								ht_cog.put("a.ITEM",(String)invDet.get("ITEM"));
								invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOMLandedCost(ht_cog,"","",plant,(String)invDet.get("ITEM"),"",curency,curency,"","","","","");
								if(invQryList.isEmpty())
								{
									invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOM(ht_cog,"","",plant,(String)invDet.get("ITEM"),"",curency,curency,"","","","","");
								}
								if(invQryList!=null)
								{
									if(!invQryList.isEmpty())
									{
										Map lineArr = (Map) invQryList.get(0);
										String avg= StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("AVERAGE_COST")),"2");
										totalCostofGoodSold+=Double.parseDouble(avg)*(quantity);
									}
								}
								new InvoiceDAO().update_is_cogs_set((String)invDet.get("INVOICEHDRID"), (String)invDet.get("LNNO"), (String)invDet.get("ITEM"), plant);
								}
							}
							//totalCostofGoodSold+=Double.parseDouble(invDet.get("AMOUNT").toString());
							boolean isLoop=false;
							if(journalDetails.size()>0)
							{
								int i=0;
								for(JournalDetail journal:journalDetails) {
									int accountId=journal.getACCOUNT_ID();
									if(accountId==journalDetail.getACCOUNT_ID()) {
										isLoop=true;
										Double sumDetit=journal.getCREDITS()+journalDetail.getCREDITS();
										journalDetail.setCREDITS(sumDetit);
										journalDetails.set(i, journalDetail);
										break;
									}
									i++;
									
								}
								if(isLoop==false) {
									journalDetails.add(journalDetail);
								}
							
							}
							else
							{
								journalDetails.add(journalDetail);
							}
						}
						
						if(!shippingCost.isEmpty())
						{
							Double shippingCostFrom=Double.parseDouble(shippingCost);
							if(shippingCostFrom>0)
							{
								JournalDetail journalDetail_4=new JournalDetail();
								journalDetail_4.setPLANT(plant);
								JSONObject coaJson4=coaDAO.getCOAByName(plant, "Outward freight & shipping");
								journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
								journalDetail_4.setACCOUNT_NAME("Outward freight & shipping");
								journalDetail_4.setCREDITS(shippingCostFrom);
								journalDetails.add(journalDetail_4);
							}
						}
						
						if(taxamount.isEmpty())
						{
							taxamount="0.00";
						}
						Double taxAmountFrom=Double.parseDouble(taxamount);
						if(taxAmountFrom>0)
						{
							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
							journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail_2.setACCOUNT_NAME("VAT Output");*/
							
							MasterDAO masterDAO = new MasterDAO();
							String planttaxtype = masterDAO.GetTaxType(plant);
							
							if(planttaxtype.equalsIgnoreCase("TAX")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Output");
							}else if(planttaxtype.equalsIgnoreCase("GST")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Payable");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("GST Payable");
							}else if(planttaxtype.equalsIgnoreCase("VAT")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Output");
							}else {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Output");
							}
							
							journalDetail_2.setCREDITS(taxAmountFrom);
							journalDetails.add(journalDetail_2);
						}
						
						if(!adjustment.isEmpty())
						{
							Double adjustFrom=Double.parseDouble(adjustment);
							if(adjustFrom>0)
							{
								JournalDetail journalDetail_7=new JournalDetail();
								journalDetail_7.setPLANT(plant);
								JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
								journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
								journalDetail_7.setACCOUNT_NAME("Adjustment");
								journalDetail_7.setCREDITS(adjustFrom);
								journalDetails.add(journalDetail_7);
							}
							else if(adjustFrom<0)
							{
								JournalDetail journalDetail_7=new JournalDetail();
								journalDetail_7.setPLANT(plant);
								JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
								journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
								journalDetail_7.setACCOUNT_NAME("Adjustment");
								adjustFrom=Math.abs(adjustFrom);
								journalDetail_7.setDEBITS(adjustFrom);
								journalDetails.add(journalDetail_7);
							}
						}
						Journal journal=new Journal();
						Double totalDebitAmount=0.00;
						for(JournalDetail jourDet:journalDetails)
						{
							 totalDebitAmount=totalDebitAmount+jourDet.getDEBITS();
						}
						journalHead.setTOTAL_AMOUNT(totalDebitAmount);
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						JournalService journalService=new JournalEntry();
						Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
						if(journalFrom.getJournalHeader()!=null)
						{
							if(journalFrom.getJournalHeader().getID()>0)
							{
								journalHead.setID(journalFrom.getJournalHeader().getID());
								journalService.updateJournal(journal, username);
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								htMovHis.put(IDBConstants.ITEM, "");
								htMovHis.put(IDBConstants.QTY, "0.0");
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS","");
								isAdded = movHisDao.insertIntoMovHis(htMovHis);
							}
							else
							{
								journalService.addJournal(journal, username);
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								htMovHis.put(IDBConstants.ITEM, "");
								htMovHis.put(IDBConstants.QTY, "0.0");
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS","");
								isAdded = movHisDao.insertIntoMovHis(htMovHis);
							}
							
							//Cost of goods sold
							if(totalCostofGoodSold>0)
							{
								journalDetails.clear();
								journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
								JournalDetail journalDetail_InvAsset=new JournalDetail();
								journalDetail_InvAsset.setPLANT(plant);
								JSONObject coaJson7=coaDAO.getCOAByName(plant, "Inventory Asset");
								System.out.println("Json"+coaJson7.toString());
								journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
								journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
								journalDetail_InvAsset.setCREDITS(totalCostofGoodSold);
								journalDetails.add(journalDetail_InvAsset);
								
								JournalDetail journalDetail_COG=new JournalDetail();
								journalDetail_COG.setPLANT(plant);
								JSONObject coaJson8=coaDAO.getCOAByName(plant, "Cost of goods sold");
								System.out.println("Json"+coaJson8.toString());
								journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
								journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
								journalDetail_COG.setDEBITS(totalCostofGoodSold);
								journalDetails.add(journalDetail_COG);
								
								journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
								journal.setJournalHeader(journalHead);
								journal.setJournalDetails(journalDetails);
								
								Journal journalCOG=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
								if(journalCOG.getJournalHeader()!=null)
								{
									if(journalCOG.getJournalHeader().getID()>0)
									{
										journalHead.setID(journalCOG.getJournalHeader().getID());
										journalService.updateJournal(journal, username);
										Hashtable htMovHis = new Hashtable();
										htMovHis.put(IDBConstants.PLANT, plant);
										htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
										htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
										htMovHis.put(IDBConstants.ITEM, "");
										htMovHis.put(IDBConstants.QTY, "0.0");
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
										htMovHis.put(IDBConstants.CREATED_BY, username);		
										htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htMovHis.put("REMARKS","");
										isAdded = movHisDao.insertIntoMovHis(htMovHis);
									}
									else
									{
										journalService.addJournal(journal, username);
										Hashtable htMovHis = new Hashtable();
										htMovHis.put(IDBConstants.PLANT, plant);
										htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
										htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
										htMovHis.put(IDBConstants.ITEM, "");
										htMovHis.put(IDBConstants.QTY, "0.0");
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
										htMovHis.put(IDBConstants.CREATED_BY, username);		
										htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htMovHis.put("REMARKS","");
										isAdded = movHisDao.insertIntoMovHis(htMovHis);
									}
								}
							}
						}
						
					}
				}
				
				

				if(isAdded) {
					if(!deductInv.equalsIgnoreCase("1") && !isexpense.equals("1") && !gino.equalsIgnoreCase("")) {
						Hashtable htgi = new Hashtable();	
						String sqlgi = "UPDATE "+ plant+"_FINGINOTOINVOICE SET STATUS='INVOICED' WHERE PLANT='"+ plant+"' AND GINO='"+gino+"'";
						invoiceDAO.updategino(sqlgi, htgi, "");
					}
				}
				
				if(isAdded) {
					for(int i =0 ; i < item.size() ; i++){
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					if(cmd.equalsIgnoreCase("Edit"))
						htMovHis.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);	
					else
					{
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_INVOICE);
						if(isexpense.equalsIgnoreCase("1"))
							htMovHis.put("DIRTYPE", TransactionConstants.EXPENSES_TO_INVOICE);
						if(cmd.equalsIgnoreCase("IssueingGoodsInvoice"))
							htMovHis.put("DIRTYPE", TransactionConstants.CONVERT_TO_INVOICE);
					}
					
						
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(invoiceDate));														
					htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
					String billqty = String.valueOf((String) qty.get(i));
					htMovHis.put(IDBConstants.QTY, billqty);
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					if(!jobNum.isEmpty())
						htMovHis.put("REMARKS",jobNum);
					else
						htMovHis.put("REMARKS",dono);
					
					Hashtable htMovChk = new Hashtable();
					htMovChk.clear();
					htMovChk.put(IDBConstants.PLANT, plant);
					htMovChk.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);
					htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
					htMovChk.put(IDBConstants.QTY, billqty);
					htMovChk.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
					isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%INVOICE%' ");
					if(!isAdded)	
					isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
				}
			}
			
			
			
			//azees consignmentToInvoice Status & Qty Update
			if(!cmd.equalsIgnoreCase("Edit"))
			{
			if(jobNum.contains("C")) {
			for(int i=0;i<item.size();i++) {
				
//				int lnv=i+1;
			ToDetDAO _ToDetDAO = new ToDetDAO();
			ToHdrDAO _ToHdrDAO = new ToHdrDAO();
			String updateEstHdr = "",updateEstDet="";
			Hashtable htCondition = new Hashtable();
			htCondition.put("PLANT", plant);
			htCondition.put("TONO", jobNum);
			htCondition.put("TOLNNO", String.valueOf(tlnno.get(i)));
			int tllno=Integer.parseInt(((String)tlnno.get(i)));
			String issuingQty = (String)qty.get(i);
			ToDet toDet = new ToDet();
			toDet = _ToDetDAO.getToDetById(plant, jobNum, tllno, (String)item.get(i));
			
			//String issuedqty = (String)qtyIs.get(index);
			BigDecimal Ordqty = toDet.getQTYOR();
			BigDecimal tranQty = BigDecimal.valueOf(Double.parseDouble(issuingQty));
			BigDecimal issqty = BigDecimal.valueOf(Double.parseDouble("0"));
					if (toDet.getQTYAC() != null) {
						issqty =toDet.getQTYAC();
					}
			BigDecimal sumqty = issqty.add(tranQty);
			//sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			
			String extraCond = " AND  QtyOr >= isNull(QTYAC,0) + "+ issuingQty;
			if (Ordqty.compareTo(sumqty) == 0) {
				updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty  + ", RECSTAT='C' ";

			} else {
				updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty  + ", RECSTAT='O' ";
			
			}
		
		boolean	insertFlag = _ToDetDAO.update(updateEstDet, htCondition, extraCond);
				
			if (insertFlag) {
				htCondition.remove("TOLNNO");
				
				insertFlag = _ToDetDAO.isExisit(htCondition,"RECSTAT in ('O','N')");
				if (!insertFlag){
					updateEstHdr = "set  RECSTAT='C',ORDER_STATUS='INVOICED' ";
					insertFlag = _ToHdrDAO.update(updateEstHdr, htCondition, "");
				}					
					
			
			}
			}
			}
			}
			
			if(isAdded) {
				DbBean.CommitTran(ut);

					ShipHisDAO shiphstdao = new ShipHisDAO();
					Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
					htTrandId1.put(IConstants.INVOICENO, invoiceNo);
					htTrandId1.put(IConstants.PLANT, plant);
//					boolean flag = 
							shiphstdao.isExisit(htTrandId1);	// Check SHIPHIS	 						
					/*if(flag)
					{*/
					new TblControlUtil().updateTblControlIESeqNo(plant, "INVOICE", "IN", invoiceNo);
					/*}*/
					if(deductInv.equalsIgnoreCase("1")) {
						new TblControlUtil().updateTblControlIESeqNo(plant, "GINO", "GI", gino);
					}
					result = "ok";
					autoinverr="";
			}
			else {
				result = "not ok";
			}
			
		}catch (Exception e) {
			 e.printStackTrace();
			 autoinverr=e.toString();
			 result = "not ok";
		}			
	
		return result;
	
	}
	
	
	private Map getOutstandingAmountForCustomer(String custCode, double orderAmount, String plant) throws Exception{
		String outstdamt = "", creditLimitBy ="", creditLimit = "";//, creditBy = "";
		Map resultMap = new HashMap();
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
		
		CustUtil custUtil = new CustUtil();
    	ArrayList arrCust = custUtil.getCustomerDetails(custCode,plant);
		creditLimit   = (String)arrCust.get(24);
		creditLimitBy   = (String)arrCust.get(35);
		
		if(creditLimitBy.equalsIgnoreCase("NOLIMIT")) {
			resultMap.put("STATUS", false);
			resultMap.put("MSG", "");
		}else {
			DateFormat currentdate = new SimpleDateFormat("dd/MM/yyyy");
	    	Date date = new Date();
	    	String current_date=currentdate.format(date); 
	    	
	    	Calendar firstDayOfMonth = Calendar.getInstance();   
	    	firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
	    	String startDate = currentdate.format(firstDayOfMonth.getTime()); 
	    	System.out.println(firstDayOfMonth.getTime()+"  "+startDate);
	    	
	        Calendar lastDayOfMonth = Calendar.getInstance();
	        lastDayOfMonth.set(Calendar.DATE, lastDayOfMonth.getActualMaximum(Calendar.DATE));
	        String endDate= currentdate.format(lastDayOfMonth.getTime());
	        System.out.println(firstDayOfMonth.getTime()+"  "+endDate);
	        
	        if(creditLimitBy.equalsIgnoreCase("DAILY")) {
	        	startDate = current_date;
	        	endDate = current_date;
	        }
	        try {
				outstdamt = new InvoicePaymentUtil().getOutStdAmt(plant,custCode, startDate, endDate);
				double allowedCreditval = Double.parseDouble(creditLimit)-((Double.parseDouble(outstdamt)+orderAmount)); 
				creditLimit = StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal);
				if(allowedCreditval >= 0){
					resultMap.put("STATUS", false);
					resultMap.put("MSG", "");
				} else {
					resultMap.put("STATUS", true);
					resultMap.put("MSG", "Invoice amount cannot exceed "+ custCode +" Available Credit Limit " 
					+ _PlantMstDAO.getBaseCurrency(plant) + " " + creditLimit);
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return resultMap;
	}

	/* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To include Transaction date
	*/
	private String OutGoingIssueBulkDatabyProd(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
//		StrUtils StrUtils = new StrUtils();
		Map issMat_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", DONO = "", LOC = "",dolno = "",item = "",QTYOR = "",issuingQty = "";//ITEM_DESCRIPTION = "",
		String ITEM_BATCH = "NOBATCH",BATCH_ID="-1",ITEM_QTY = "",PICKING_QTY = "",CUST_NAME = "",LOGIN_USER = "",SHIPPINGNO = "",ISNONSTKFLG="";//PICKED_QTY = "0",
		String REMARKS = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="", INVOICENO = "",UOMQTY="",UOM="";//ISSUEDQTY="",
		String creditLimit = "", creditBy = "";
		String alertitem="",MINSTKQTY="";
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
			PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String ISINVENTORYMINQTY = new DoHdrDAO().getISINVENTORYMINQTY(PLANT);//Thanzi
	        item = StrUtils.fString(request.getParameter("ITEM"));
//	        ITEM_DESCRIPTION =  StrUtils.fString(request.getParameter("DESC"));
	        String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
			LOC = StrUtils.fString(request.getParameter("LOC_0"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, item);
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
	         if (chkdDoNos != null)    {     
    				for (int i = 0; i < chkdDoNos.length; i++)       { 
    				String	data = chkdDoNos[i];
    				String[] chkdata = data.split(",");
    				String dno=chkdata[0]+"_"+chkdata[1];
    			issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+dno));
    			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+dno));
    			BATCH_ID  = StrUtils.fString(request.getParameter("BATCH_ID_"+dno));
    			if (ITEM_BATCH.length() == 0) {
    					ITEM_BATCH = "NOBATCH";
    				}
    			
    					checkedDOS.put(dno, issuingQty+":"+ITEM_BATCH);
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

			DONO = StrUtils.fString((String) mp.get("data1"));
			dolno = StrUtils.fString((String) mp.get("data2"));
			QTYOR = StrUtils.fString((String) mp.get("data3"));
//			ISSUEDQTY = StrUtils.fString((String) mp.get("data4"));
			CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data5")));			
			UOMQTY = StrUtils.replaceCharacters2Recv(StrUtils
					.fString((String) mp.get("data6")));
			UOM = StrUtils.fString((String) mp.get("data7"));
			
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
		
			 
			ArrayList DODetails = null;

    		Hashtable htDoDet = new Hashtable();
    		String queryDoDet = "item,itemDesc,QTYOR";
    				
					issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+DONO+"_"+dolno));	
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+DONO+"_"+dolno));
					BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID_"+DONO+"_"+dolno));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
			    	htDoDet.put(IConstants.DODET_DONUM, DONO);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.DODET_DOLNNO, dolno);
		    		DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
					if (DODetails.size() > 0) {	
						Map map1 = (Map) DODetails.get(0);
						item = (String) map1.get("item");
//						ITEM_DESCRIPTION = (String) map1.get("itemDesc");
						QTYOR = (String) map1.get("QTYOR");
					}
					if(!ISNONSTKFLG.equalsIgnoreCase("Y")){ //If Non Stock Item then omit Inventory Validation
					List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,item, LOC, ITEM_BATCH);
					double invqty = 0;
                    double Detuctqty = 0;
					double STKQTY = 0;
					if (listQry.size() > 0) {
						for (int j = 0; j < listQry.size(); j++) {
							Map m = (Map) listQry.get(j);
							ITEM_QTY = (String) m.get("qty");
			                MINSTKQTY = (String) m.get("MINSTKQTY");
							invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
							STKQTY = Double.parseDouble(((String) MINSTKQTY.trim()));
						}
						if(STKQTY!=0) {
						Detuctqty = invqty-pickingQty;
						if(STKQTY>Detuctqty) {
							if(alertitem.equalsIgnoreCase("")) {
								alertitem =item;
							}else {
								alertitem = alertitem+" , "+item;
							}
						}
							
						}
						
						if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
							alertitem = alertitem;
						else 
							alertitem="";
						
						double curqty = pickingQty * Double.valueOf(UOMQTY);
						if(invqty < curqty){
							throw new Exception(
									"Error in picking sales Order : Inventory not found for the product/batch " +item+ " scanned at the location "+LOC + " for OrderNo "+DONO+ " and LineNo "+dolno);							
						}
					} 
					
					else {
						
						throw new Exception(
								"Error in picking sales Order : Inventory not found for the product/batch " +item+ " scanned at the location "+LOC + "for OrderNo "+DONO+ " and LineNo "+dolno);
						
					}
					}
					
					// check for item in location
					
                UserLocUtil uslocUtil = new UserLocUtil();
                uslocUtil.setmLogger(mLogger);
                boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC);
                if(!isvalidlocforUser){
                    throw new Exception(" Loc : "+LOC+" is not User Assigned Location");
                }
					
//				double orderqty = Double.parseDouble(((String) QTYOR.trim()));
//				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
				pickingQty = StrUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
				PICKING_QTY = String.valueOf(pickingQty);
				PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
				SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
				issMat_HM = new HashMap();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put(IConstants.ITEM, item);
				//issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
				issMat_HM.put(IConstants.DODET_DONUM, DONO);
				issMat_HM.put(IConstants.DODET_DOLNNO, dolno);
				issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				issMat_HM.put(IConstants.LOC, LOC);
				issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
				issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
				issMat_HM.put(IConstants.BATCH_ID, BATCH_ID);
				issMat_HM.put(IConstants.QTY, PICKING_QTY);
				issMat_HM.put(IConstants.ORD_QTY, QTYOR);
				issMat_HM.put(IConstants.INV_EXP_DATE, "");
				issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
				issMat_HM.put("INV_QTY", "1");
				issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
				issMat_HM.put(IConstants.REMARKS, REMARKS);
				issMat_HM.put("TYPE", "OBPROD");
				issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
				issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
				issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
				issMat_HM.put(IConstants.INVOICENO, INVOICENO);
				issMat_HM.put(IConstants.UOM, UOM);
				issMat_HM.put("UOMQTY", UOMQTY);
				CustUtil custUtil = new CustUtil();
				ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, DONO),PLANT);
				creditLimit   = (String)arrCust.get(24);
				creditBy   = (String)arrCust.get(35);
				
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
				issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
				issMat_HM.put(IConstants.BASE_CURRENCY, (String) session.getAttribute("BASE_CURRENCY"));
				flag = _DOUtil.process_DoPickIssueForPC(issMat_HM)&& true;
     			
     			if (flag == true) {//Shopify Inventory Update
   					Hashtable htCond = new Hashtable();
   					htCond.put(IConstants.PLANT, PLANT);
   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
   						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,item);						
						if(nonstkflag.equalsIgnoreCase("N")) {
   						String availqty ="0";
   						ArrayList invQryList = null;
   						htCond = new Hashtable();
   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,item, new ItemMstDAO().getItemDesc(PLANT, item),htCond);						
   						if (invQryList.size() > 0) {					
   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
//   								String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                availqty = (String)lineArr.get("AVAILABLEQTY");
                                System.out.println(availqty);
   							}
   							double availableqty = Double.parseDouble(availqty);
       						new ShopifyService().UpdateShopifyInventoryItem(PLANT, item, availableqty);
   						}	
						}
   					}
   				}
			}
			if (flag) {
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				//DbBean.CommitTran(ut);	
				String msgflag=StrUtils.fString((String)	issMat_HM.get("msgflag"));
				if(msgflag.length()>0){
					request.getSession().setAttribute("RESULT",	msgflag	);
				}else{
					if(alertitem.equalsIgnoreCase("")) {
						request.getSession().setAttribute("RESULT","Product ID : " + item+ "  Picked/Issued successfully!");
					}else {
						request.getSession().setAttribute("RESULT","Product ID : " + item+ "  Picked/Issued successfully! <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>");
					}
				}
				
				response.sendRedirect("../salestransaction/orderpickissuebyproduct?action=View&PLANT="
						+ PLANT + "&ITEM=" + item +"&result=sucess");
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession()
						.setAttribute(
								"RESULTERROR",
								"Failed to Pick/Issue Item : "
										+ item);
				response.sendRedirect("../salestransaction/orderpickissuebyproduct?result=error");
			}
			
			}
			
		 catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
			response
					.sendRedirect("../salestransaction/orderpickissuebyproduct?action=View&PLANT="
							+ PLANT + "&ITEM=" + item 
							+"&LOC="
							+LOC
							+"&REF="
							+REMARKS
							+ "&allChecked="
							+allChecked
							+"&fullReceive="
							+fullIssue
							+"&result=catchrerror"+"&INVOICENO="+INVOICENO);
			throw e;
		}
		
		return xmlStr;
	}

	
	private String OutBoundsOrderIssueConfirm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;
//		StrUtils StrUtils = new StrUtils();
		Map receiveMaterial_HM = null;

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
		String LOGIN_USER = "";//ITEM_DESCRIPTION = "", 
		String SHIPPINGNO = "";
		String PICKED_QTY = "", DO_BATCH = "", LOC = "", ISSUING_QTY = "";//, ORDER_QTY = "";

		try {

			String sepratedtoken = "";
			String totalString = StrUtils.fString(request
					.getParameter("TRAVELER"));
			String REMARK = StrUtils.fString(request.getParameter("REMARK2"));

			StringTokenizer parser = new StringTokenizer(totalString, "=");

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

				HttpSession session = request.getSession();
				PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				DO_NUM = StrUtils.fString((String) mp.get("data1"));
				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
//				ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));
//				ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
				PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
				ISSUING_QTY = StrUtils.fString((String) mp.get("data7"));
				LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
				LOC = StrUtils.fString((String) mp.get("data9"));
				DO_BATCH = StrUtils.fString((String) mp.get("data10"));

				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				double issuingQty = Double.parseDouble(((String) ISSUING_QTY.trim()
						.toString()));

				String issueQty = String.valueOf(pickedqty - issuingQty);
				System.out.println("Issue Quantity"+ issueQty);
				if (SHIPPINGNO.length() == 0) {
					SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
				}
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.QTY, issueQty);
				receiveMaterial_HM.put(IConstants.DODET_DONUM, DO_NUM);
				receiveMaterial_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
						.getCustCode(PLANT, DO_NUM));
				receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(
						PLANT, DO_NUM));
				receiveMaterial_HM.put(IConstants.BATCH, DO_BATCH);
				receiveMaterial_HM.put("SHIPPINGNO", SHIPPINGNO);
				receiveMaterial_HM.put(IConstants.REMARKS, REMARK);
				xmlStr = "";
				flag = _DOUtil.process_IssueMaterial(receiveMaterial_HM);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			response
					.sendRedirect("jsp/DOOutBoundsOrderIssueConfirm.jsp?action=View&PLANT="
							+ PLANT + "&DONO=" + DO_NUM + "&result=catchrerror");
			throw e;
		}
		if (flag) {
			request.getSession().setAttribute("RESULT",
					"Goods Issued successfully!");

		
			response.sendRedirect("jsp/DOSummaryForSingleStepPickIssue.jsp?DONO=" + DO_NUM
					+ "&action=View");
		}
		return xmlStr;
	}
	
	
//	private String OutGoingPickingData(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException,
//			Exception {
//
//		String sepratedtoken1 = "";
//		boolean flag = false;
//
//		DoDetDAO _DoDetDAO = new DoDetDAO();
//		_DoDetDAO.setmLogger(mLogger);
////		StrUtils StrUtils = new StrUtils();
//		Map receiveMaterial_HM = null;
//
//		Map mp = null;
//		mp = new HashMap();
//		String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
//		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "", ITEM_EXPDATE = "";
//
//		String DO_QTY = "", PICKED_QTY = "", ISSUED_QTY = "", DO_BATCH = "", LOC = "", TRAN_QTY = "", ISSUING_QTY = "", ORDER_QTY = "";
//
//		try {
//
//			String sepratedtoken = "";
//
//			String totalString = StrUtils.fString(request
//					.getParameter("TRAVELER"));
//
//			StringTokenizer parser = new StringTokenizer(totalString, "=");
//
//			while (parser.hasMoreTokens())
//
//			{
//				int count = 1;
//				sepratedtoken = parser.nextToken();
//				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
//						",");
//
//				while (parser1.hasMoreTokens())
//
//				{
//					sepratedtoken1 = parser1.nextToken();
//
//					mp.put("data" + count, sepratedtoken1);
//
//					count++;
//				}
//
//				HttpSession session = request.getSession();
//				PLANT = StrUtils
//						.fString((String) session.getAttribute("PLANT")).trim();
//				DO_NUM = StrUtils.fString((String) mp.get("data1"));
//
//				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
//
//				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
//				ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));
//
//				ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
//				PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
//				ISSUING_QTY = StrUtils.fString((String) mp.get("data7"));
//				LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
//				LOC = StrUtils.fString((String) mp.get("data9"));
//				DO_BATCH = StrUtils.fString((String) mp.get("data10"));
//
//				CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils
//						.fString((String) mp.get("data11")));
//
//				List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(
//						PLANT, DO_NUM);
//				session.setAttribute("customerlistqry1", listQry);
//
//				receiveMaterial_HM = new HashMap();
//				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
//				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
//				receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
//				receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
//				receiveMaterial_HM.put(IConstants.QTY, PICKED_QTY);
//				receiveMaterial_HM.put(IConstants.DODET_DONUM, DO_NUM);
//				receiveMaterial_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
//				receiveMaterial_HM.put(IConstants.LOC, LOC);
//				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
//				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
//						.getCustCode(PLANT, DO_NUM));
//
//				receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(
//						PLANT, DO_NUM));
//
//				receiveMaterial_HM.put(IConstants.BATCH, DO_BATCH);
//				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
//
//				xmlStr = "";
//
//			}
//		}
//
//		catch (Exception e) {
//			this.mLogger.exception(this.printLog, "", e);
//			request.getSession().setAttribute("CATCHERROR",
//					"Error in Issuing Product!");
//			response
//					.sendRedirect("jsp/OutBoundOrderSummary.jsp?action=View&PLANT="
//							+ PLANT + "&DONO=" + DO_NUM);
//			throw e;
//		}
//		if (!flag) {
//
//			List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,
//					DO_NUM);
//			request.getSession().setAttribute("customerlistqry4", listQry);
//			response.sendRedirect("jsp/OutBoundOrderPicking.jsp?ORDERNO="
//					+ DO_NUM + "&ORDERLNO=" + DO_LN_NUM + "&ITEMNO=" + ITEM_NUM
//					+ "&ITEMDESC="
//					+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
//					+ "&PICKEDQTY=" + PICKED_QTY + "&ORDERQTY=" + ORDER_QTY+"&BATCH=NOBATCH");
//		}
//		return xmlStr;
//	}
	private String OutGoingPickingData(HttpServletRequest request,
			HttpServletResponse response,String pickType) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;

		DoDetDAO _DoDetDAO = new DoDetDAO();
		_DoDetDAO.setmLogger(mLogger);
//		StrUtils StrUtils = new StrUtils();
		Map receiveMaterial_HM = null;

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "", INVOICENO="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "";//, ITEM_EXPDATE = "";

		String PICKED_QTY = "", DO_BATCH = "", LOC = "", ORDER_QTY = "",UOM="",UOMQTY="";//DO_QTY = "", ISSUED_QTY = "", TRAN_QTY = "", ISSUING_QTY = "", 

		try {

			String sepratedtoken = "";

			String totalString = StrUtils.fString(request
					.getParameter("TRAVELER"));
			INVOICENO = StrUtils.fString(request
					.getParameter("INVOICENO"));
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
				DO_NUM = StrUtils.fString((String) mp.get("data1"));

				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));

				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));

				ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
				PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
//				ISSUING_QTY = StrUtils.fString((String) mp.get("data7"));
				LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
				LOC = StrUtils.fString((String) mp.get("data9"));
				DO_BATCH = StrUtils.fString((String) mp.get("data10"));

				CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils
						.fString((String) mp.get("data11")));
				UOM = StrUtils.replaceCharacters2Recv(StrUtils
						.fString((String) mp.get("data12")));
				UOMQTY = StrUtils.replaceCharacters2Recv(StrUtils
						.fString((String) mp.get("data13")));
				List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(
						PLANT, DO_NUM);
				session.setAttribute("customerlistqry1", listQry);
				session.setAttribute("INVOICENO", INVOICENO);
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
				receiveMaterial_HM.put(IConstants.QTY, PICKED_QTY);
				receiveMaterial_HM.put(IConstants.DODET_DONUM, DO_NUM);
				receiveMaterial_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
						.getCustCode(PLANT, DO_NUM));

				receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(
						PLANT, DO_NUM));

				receiveMaterial_HM.put(IConstants.BATCH, DO_BATCH);
				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				receiveMaterial_HM.put(IConstants.INVOICENO, INVOICENO);
				xmlStr = "";

			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR",
					"Error in Issuing Product!");
		        String redirectErrorPage = "OutBoundOrderSummary.jsp";
		        if(pickType.equalsIgnoreCase("PICKING"))
			 redirectErrorPage = "OutBoundOrderSummary.jsp";
			if(pickType.equalsIgnoreCase("MULTIPLE_PICK"))
				redirectErrorPage="../salestransaction/orderpicksummary";
		    if(pickType.equalsIgnoreCase("PICK_BY_RANGE"))
		        redirectErrorPage="../salestransaction/orderpickserial";
		    if(pickType.equalsIgnoreCase("SINGLE_STEP"))
		        redirectErrorPage="DOSummaryForSingleStepMultipleIssue.jsp";
		    if(pickType.equalsIgnoreCase("MOBILE_SHOPPING"))
		        redirectErrorPage="mobileShopSingleStepPickIssue.jsp";
		    if(pickType.equalsIgnoreCase("MOBILE_SHOPPING_WO_INVCHK"))
		        redirectErrorPage="mobileSingleStepWOInvcheck.jsp";
                            
			response.sendRedirect("jsp/"+redirectErrorPage+"?action=View&PLANT="
							+ PLANT + "&DONO=" + DO_NUM);
			throw e;
		}
		if (!flag) {

			List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,
					DO_NUM);
			String redirectPage = "OutBoundOrderPicking.jsp";
                        
			request.getSession().setAttribute("customerlistqry4", listQry);
			    if(pickType.equalsIgnoreCase("PICKING"))
			       redirectPage = "OutBoundOrderPicking.jsp";
			    if(pickType.equalsIgnoreCase("MULTIPLE_PICK"))
			        redirectPage="../salestransaction/orderpick";       
			    if(pickType.equalsIgnoreCase("PICK_BY_RANGE"))
			    	redirectPage="../salestransaction/orderpickrange";
			    if(pickType.equalsIgnoreCase("SINGLE_STEP"))
			    	redirectPage="DOSummaryForSingleStepMultipleIssue.jsp";
                            if(pickType.equalsIgnoreCase("MOBILE_SHOPPING"))
                                redirectPage="mobileShopSingleStepPickIssue.jsp";
                            if(pickType.equalsIgnoreCase("MOBILE_SHOPPING_WO_INVCHK"))
                                redirectPage="mobileSingleStepWOInvcheck.jsp";
			response.sendRedirect("jsp/"+redirectPage+"?ORDERNO="
					+ DO_NUM + "&ORDERLNO=" + DO_LN_NUM + "&ITEMNO=" + ITEM_NUM
					+ "&ITEMDESC="
					+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
					+ "&PICKEDQTY=" + PICKED_QTY + "&ORDERQTY=" + ORDER_QTY+"&UOM=" +UOM+"&UOMQTY=" +UOMQTY);
		}
		return xmlStr;
	}
	private String process_OutGoingReverseDataByWMS(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;

		DoDetDAO _DoDetDAO = new DoDetDAO();
		_DoDetDAO.setmLogger(mLogger);
//		StrUtils StrUtils = new StrUtils();
		Map receiveMaterial_HM = null;

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "";//, ITEM_EXPDATE = "";

		String PICKED_QTY = "", BALANCE_QTY="",DO_BATCH = "", LOC = "", ISSUING_QTY = "", ORDER_QTY = "", UOM = "", UOMQTY = "";//DO_QTY = "", ISSUED_QTY = "", TRAN_QTY = "", 

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

				HttpSession session = request.getSession();
				PLANT = StrUtils
						.fString((String) session.getAttribute("PLANT")).trim();
				DO_NUM = StrUtils.fString((String) mp.get("data1"));

				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));

				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));

				ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
				PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
				ISSUING_QTY = StrUtils.fString((String) mp.get("data7"));
				LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
				LOC = StrUtils.fString((String) mp.get("data9"));
				DO_BATCH = StrUtils.fString((String) mp.get("data10"));
				CUST_NAME = StrUtils.fString((String) mp.get("data11"));
				BALANCE_QTY = StrUtils.fString((String) mp.get("data12"));
				UOM = StrUtils.fString((String) mp.get("data13"));
				UOMQTY = StrUtils.fString((String) mp.get("data14"));
			

				List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(
						PLANT, DO_NUM);
				session.setAttribute("customerlistqry1", listQry);

				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.QTY, PICKED_QTY);
				receiveMaterial_HM.put(IConstants.DODET_DONUM, DO_NUM);
				receiveMaterial_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
						.getCustCode(PLANT, DO_NUM));

				receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(
						PLANT, DO_NUM));
				receiveMaterial_HM.put(IConstants.BATCH, DO_BATCH);

				xmlStr = "";

			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR",
					"Error in Issuing Product!");
			response
					.sendRedirect("../salestransaction/orderpickreturn?action=View&PLANT="
							+ PLANT + "&DONO=" + DO_NUM);
			throw e;
		}
		if (!flag) {

			response.sendRedirect("jsp/OutBoundOrderReverseConfirm.jsp?DONO="
					+ DO_NUM + "&DOLNNO=" + DO_LN_NUM + "&CUSTNAME="
					+ StrUtils.replaceCharacters2Send(CUST_NAME) + "&ITEM="
					+ ITEM_NUM + "&ITEMDESC="
					+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
					+ "&LOC=" + LOC + "&BATCH=" + DO_BATCH + "&PICKEDQTY="
					+ PICKED_QTY + "&ORDERQTY=" + ORDER_QTY + "&ISSUEDQTY=" + ISSUING_QTY+ "&BALANCEQTY=" + BALANCE_QTY+ "&UOM=" + UOM+ "&UOMQTY=" + UOMQTY);
		}
		return xmlStr;
	}

	private String GenerateShippingNo(String plant, String loginuser) {

		String PLANT = "";
//		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
//		boolean resultflag = false;
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

//				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "Shipping");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) loginuser);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) DateUtils.getDateTime());
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
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
//					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception(
							"Generate Shipping Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
//					resultflag = true;
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

//				Map htUpdate = null;

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
				htm.put("RECID", "");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
//					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update Shippoing Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
//					resultflag = true;
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
	/* ************Modification History*********************************
	   Oct 21 2014 Bruhan, Description: To include Transaction date
	*/
	private String process_OutBoundorderReverseByWMS(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
//		Map receiveMaterial_HM = null;
		String PLANT = "", DO_NUM = "", CUST_NAME = "", ITEM_NUM = "", DO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", REMARKS = "";
		String ITEM_BATCH = "", QTY = "",UOM="",CUST_NO="",
		PICKED_QTY = "",REVERSE_QTY = "", INV_QTY="",ITEM_EXPDATE = "", ITEM_LOC = "",TRANSACTIONDATE = "",strMovHisTranDate="",UOMQTY="";//strTranDate="",//ISSUE_QTY="", //, ORDER_QTY = "",//ITEM_QTY = "", 

		HttpSession session = request.getSession();
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));

		DoDetDAO _DoDetDAO = new DoDetDAO(PLANT);
		DoHdrDAO _DoHdrDAO = new DoHdrDAO(PLANT);
		InvMstDAO _InvMstDAO = new InvMstDAO(PLANT);
		ShipHisDAO _ShipHisDAO=new ShipHisDAO(PLANT);
//		DOUtil doutil = new DOUtil();
		boolean flag = false;
		boolean flagrecvinsert = false;
		boolean flagdodet = false;
		boolean flaginvdet = false;
		try {

			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			DO_NUM = StrUtils.fString(request.getParameter("DONO"));
			DO_LN_NUM = StrUtils.fString(request.getParameter("DOLNNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
//			ORDER_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("ORDERQTY")));
			PICKED_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKEDQTY")));
//			ISSUE_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTYISSUE")));
			REVERSE_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("REVERSEQTY")));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
//			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			LOGIN_USER = (String) session.getAttribute("LOGIN_USER");
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			UOMQTY = StrUtils.fString(request.getParameter("UOMQTY"));
			UOM = StrUtils.fString(request.getParameter("UOM"));
			INV_QTY="1";
			
			double reverseqty = Double.parseDouble(REVERSE_QTY);
			reverseqty = StrUtils.RoundDB(reverseqty, IConstants.DECIMALPTS);
			REVERSE_QTY = String.valueOf(reverseqty); 
			REVERSE_QTY = StrUtils.formatThreeDecimal(REVERSE_QTY);
			CUST_NO = _DOUtil.getCustCode(PLANT, DO_NUM);
			Hashtable htShipHis = new Hashtable();
			htShipHis.clear();
			htShipHis.put(IDBConstants.PLANT, PLANT);
			htShipHis.put("DONO", DO_NUM);
			htShipHis.put("DOLNO", DO_LN_NUM);
			htShipHis.put("CNAME", StrUtils.InsertQuotes(CUST_NAME));
			htShipHis.put(IDBConstants.ITEM, ITEM_NUM);
			//htShipHis.put(IDBConstants.ITEM_DESC,StrUtils.InsertQuotes(ITEM_DESCRIPTION));
			//htShipHis.put("LOC1", ITEM_LOC);
			htShipHis.put("LOC", "SHIPPINGAREA" + "_" + ITEM_LOC);
			htShipHis.put("BATCH", ITEM_BATCH);
			//htShipHis.put("REMARK", ITEM_EXPDATE);
			//htShipHis.put("ORDQTY", QTY);
			//htShipHis.put("PICKQTY", "-" + REVERSE_QTY);
			//htShipHis.put("REVERSEQTY", REVERSE_QTY);
			htShipHis.put("STATUS", "O");
			Hashtable htCurrency = new Hashtable();
			htCurrency.put(IDBConstants.PLANT, PLANT);
			htCurrency.put("DONO", DO_NUM);
			
//			String currencyid = doutil.getDohdrcol(htCurrency, "CURRENCYID");
			htCurrency.put("DOLNNO", DO_LN_NUM);	
//			String unitprice = doutil.getDodetcol(htCurrency, "UNITPRICE");
			//ArrayList shipHistoryDetails = _ShipHisDAO.getShipHisDetail(PLANT, DO_NUM);
			ArrayList shipHistoryDetails = _ShipHisDAO.getShipHisNewDetail(PLANT, DO_NUM);
			//htShipHis.put(IDBConstants.CURRENCYID, currencyid);
			//htShipHis.put("UNITPRICE", unitprice);
			//htShipHis.put(IDBConstants.CREATED_AT, (String) DateUtils.getDateTime());
			//htShipHis.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);

			double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
			double revQty = Double.parseDouble(((String) REVERSE_QTY.trim().toString()));
//		    boolean qtyFlag = false;
			if (pickedqty < revQty) {
//				qtyFlag = true;
				throw new Exception(
						"Error in reversing Product : Reversing Qty Should less than or equal to Picked Qty");
			}
			Iterator iterShipHistory = shipHistoryDetails.iterator();
		//modified by samatha to delete in sync with all reversal
//			flagrecvinsert = new ShipHisDAO().deleteSHIPHIS(htShipHis);
//
//			if (!flagrecvinsert) {
//				flag = false;
//				throw new Exception("Sales Product Reversed  Failed, Error In Insert SHIPHIS :"+ " " + ITEM_NUM);
//			} else {
//				flag = true;
//			}
//
//			String extCont = "";
//			String sLnstat = "O";
//			Hashtable htPoDet = new Hashtable();
//			htPoDet.put("PLANT", (String) PLANT);
//			htPoDet.put("DONO", DO_NUM);
//			htPoDet.put(IDBConstants.ITEM, ITEM_NUM);
//			htPoDet.put("DOLNNO", DO_LN_NUM);
//			String updateDoDet = "";
//
//			updateDoDet = "set qtypick= isNull(qtypick,0) - " + REVERSE_QTY+ " , LNSTAT='O',PickStatus='O'  ";
//
//			flagdodet = _DoDetDAO.update(updateDoDet, htPoDet, "");
			//NON Stock Reverse - Azees 13/2/20
			ArrayList alShipHis = _ShipHisDAO.selectShipHis("DONO,ISNULL(ID,0) ID,PICKQTY,CRAT", htShipHis);
			if (alShipHis.size() > 0) {
				for (int i = 0; i < alShipHis.size(); i++) {
					Map m = (Map) alShipHis.get(i);
					
					if(reverseqty == 0) break;
					
//					String dono = (String)m.get("DONO");
					double recqty = Double.parseDouble((String)m.get("PICKQTY"));
			          String crat = (String)m.get("CRAT");
			          String ID = (String)m.get("ID").toString();
					
			          String updaterecdet = "";
			          String updateDoDet = "";
			          if(reverseqty >= recqty)
			          {
			        	  updaterecdet = "set PICKQTY = PICKQTY-" +recqty +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+(String) LOGIN_USER+"'";
			        	  updateDoDet = "set qtypick= isNull(qtypick,0) - " + recqty+ " , LNSTAT='O',PickStatus='O'  ";
			          }
			          else
			          {
			        	  updaterecdet = "set PICKQTY = PICKQTY-" +reverseqty +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+(String) LOGIN_USER+"'";
			        	  updateDoDet = "set qtypick= isNull(qtypick,0) - " + reverseqty+ " , LNSTAT='O',PickStatus='O'  ";
			        	  recqty=reverseqty;
			          }
			        	  
			          htShipHis.put("CRAT", crat);
			          if(!ID.equalsIgnoreCase("0"))
			        	  htShipHis.put("ID", ID);
			          if(reverseqty>0)
			          {
			        	  boolean isexists = _ShipHisDAO.isExisit(htShipHis, "");
							 if(isexists)
							 {
								 flagrecvinsert = _ShipHisDAO.update(updaterecdet, htShipHis, "");
						          reverseqty=reverseqty-recqty;
							 }
							 
							 Hashtable htPoDet = new Hashtable();
								htPoDet.put("PLANT", (String) PLANT);
								htPoDet.put("DONO", DO_NUM);
								htPoDet.put(IDBConstants.ITEM, ITEM_NUM);
								htPoDet.put("DOLNNO", DO_LN_NUM);
								flagdodet = _DoDetDAO.update(updateDoDet, htPoDet, "");
								if (!flagdodet) {
									flag = false;
									throw new Exception("Sales Product Reversed  Failed, Error In update  DODET :" + " " + ITEM_NUM);
								}
							 
			          }							
				}
			
		
				htShipHis.remove("CRAT");
				htShipHis.remove("ID");
			}
			else
			{
				flag = false;
				throw new Exception("Sales Product Reversed  Failed, Error In Ships :" + " " + ITEM_NUM);
			}
			htShipHis.put("PICKQTY", "0.000");
			 
			 boolean isexists = _ShipHisDAO.isExisit(htShipHis, "");
			 if(isexists)
			 {
				 flagrecvinsert = _ShipHisDAO.deleteSHIPHIS(htShipHis);
			 }
				
			 if (!flagrecvinsert) {
					flag = false;
					throw new Exception("Sales Product Reversed  Failed, Error In Insert SHIPHIS :"+ " " + ITEM_NUM);
				} else {
					flag = true;
				}
					
/*			if (!alShipHis.isEmpty()) {
			Iterator iterShipHis = alShipHis.iterator();
			while(iterShipHis.hasNext()) {
				//String ID = ((Map)iterShipHis.next()).get("ID").toString();
				Map mapIterStock = (Map)iterShipHis.next();
				String ID = mapIterStock.get("ID").toString();
				String RPICKQTY = mapIterStock.get("PICKQTY").toString();				
				htShipHis.put("ID", ID);
				flagrecvinsert = _ShipHisDAO.deleteSHIPHIS(htShipHis);

				

				String extCont = "";
				String sLnstat = "O";
				Hashtable htPoDet = new Hashtable();
				htPoDet.put("PLANT", (String) PLANT);
				htPoDet.put("DONO", DO_NUM);
				htPoDet.put(IDBConstants.ITEM, ITEM_NUM);
				htPoDet.put("DOLNNO", DO_LN_NUM);
				//String updateDoDet = "set qtypick= isNull(qtypick,0) - " + REVERSE_QTY+ " , LNSTAT='O',PickStatus='O'  ";
				String updateDoDet = "set qtypick= isNull(qtypick,0) - " + RPICKQTY+ " , LNSTAT='O',PickStatus='O'  ";

				flagdodet = _DoDetDAO.update(updateDoDet, htPoDet, "");
				if (!flagdodet) {
					flag = false;
					throw new Exception("Sales Product Reversed  Failed, Error In update  DODET :" + " " + ITEM_NUM);
				}
			}
			}*/
			
			if (!flagdodet) {
				flag = false;
				throw new Exception("Sales Product Reversed  Failed, Error In update  DODET :" + " " + ITEM_NUM);
			} else {
				flag = true;

				 Hashtable htDoDet1 = new Hashtable();
				 htDoDet1.put("PLANT", (String) PLANT);
				 htDoDet1.put("DONO", DO_NUM);
				 htDoDet1.put("DOLNNO", DO_LN_NUM);
				 htDoDet1.put(IDBConstants.ITEM, ITEM_NUM);
				 boolean isExists = _DoDetDAO.isExisit(htDoDet1,"  QtyPick =CAST('0' AS DECIMAL(18,3)) ");
				 if(isExists){
				     String updateDoDet1 = "";

				     updateDoDet1 = "set LNSTAT='N',PickStatus='N' ";
				     flag = _DoDetDAO.update(updateDoDet1, htDoDet1, "");    
				 }
				// //Check recv when its = O then update status='N' else
				// status='O' end
			}

			String updatedoHdr = "";
			Hashtable htCondidoHdr = new Hashtable();
			htCondidoHdr.put("PLANT", PLANT);
			htCondidoHdr.put("DONO", DO_NUM);
			flag = _DoDetDAO
					.isExisit(htCondidoHdr,
							" (isnull(pickStatus,'') in ('O','C') or isnull(LNSTAT,'') in ('O','C'))");
			if (!flag)
				updatedoHdr = "set STATUS='N',PickStaus='N' ";
			else
				updatedoHdr = "set STATUS='O', PickStaus='O' ";

			flagdodet = _DoHdrDAO.update(updatedoHdr, htCondidoHdr, "");

			if (!flagdodet) {
				flag = false;
				throw new Exception("Sales Product Reversed  Failed, Error In update  DOHDR :"+ " " + ITEM_NUM);
			} else {
				flag = true;
			}
			
			if(flag){
			Map mPrddet = new ItemMstDAO().getProductNonStockDetails(PLANT,ITEM_NUM);
	        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
	        if(!nonstocktype.equals("Y"))	
		    {
				while(iterShipHistory.hasNext()) {
					Map m = (Map)iterShipHistory.next();
					if (Float.parseFloat("" + m.get("PICKQTY")) > 0) {
						Hashtable htInvMst = new Hashtable();
						htInvMst.put("PLANT", (String) PLANT);
						htInvMst.put(IDBConstants.ITEM, ITEM_NUM);
						htInvMst.put(IDBConstants.LOC, "SHIPPINGAREA" + "_" + ITEM_LOC);
						htInvMst.put(IDBConstants.USERFLD4, ITEM_BATCH);
						htInvMst.put(IDBConstants.INVID, "" + m.get(IDBConstants.INVSHID));
						String updateInvDet = "";						
						//updateInvDet = "set qty= isNull(qty,0) - " + Float.parseFloat("" + m.get("PICKQTY")) + "";
						double calinqty = Double.valueOf((String) m.get("PICKQTY"))* Double.valueOf(UOMQTY);
						updateInvDet = "set qty= isNull(qty,0) - " + calinqty + "";
						flaginvdet = _InvMstDAO.update(updateInvDet, htInvMst, "");
						if (!flaginvdet) {
							flag = false;
							throw new Exception(
									"Sales Product Reversed  Failed, Error In update  InvMst ShippingArea Loc :"
											+ " " + ITEM_NUM);
						} else {
							flag = true;
						}
						Hashtable htInvMst1 = new Hashtable();
						htInvMst1.put("PLANT", (String) PLANT);
						htInvMst1.put(IDBConstants.ITEM, ITEM_NUM);
						htInvMst1.put(IDBConstants.LOC, ITEM_LOC);
						htInvMst1.put(IDBConstants.USERFLD4, ITEM_BATCH);
						htInvMst1.put(IDBConstants.INVID, "" + m.get(IDBConstants.INVID));
						String updateInvDet1 = "";
						//updateInvDet1 = "set qty= isNull(qty,0) + " + Float.parseFloat("" + m.get("PICKQTY")) + "";
						updateInvDet1 = "set qty= isNull(qty,0) + " + calinqty + "";
						flaginvdet = _InvMstDAO.update(updateInvDet1, htInvMst1, "");
						if (!flaginvdet) {
							flag = false;
							throw new Exception(
									"Sales Product Reversed  Failed, Error In update Orginal  InvMst Loc :"
											+ " " + ITEM_NUM);
						}
						else {
							flag = true;
						}
					}
				}

			if (flaginvdet) {
				flag = true;
				//bom updattion
				boolean bomexistsflag = false;
//				boolean itemflag=false;
				boolean bomupdateflag=false;
//				String extcond="";
				
//				ItemMstDAO _ItemMstDAO =new ItemMstDAO();
				BomDAO _BomDAO =new BomDAO() ;
				
				try
				{
				/*Hashtable htItemMst= new Hashtable();
				htItemMst.clear();
				
				htItemMst.put(IDBConstants.PLANT, (String) PLANT);
				htItemMst.put(IDBConstants.ITEM, ITEM_NUM);
				
				extcond=" userfld1='K'";
				
				itemflag = _ItemMstDAO .isExisit(htItemMst, extcond);
				
				if(itemflag)
				{*/
				
					Hashtable htBomMst = new Hashtable();
					htBomMst.clear();
					htBomMst.put(IDBConstants.PLANT,(String) PLANT);
					htBomMst.put("PARENT_PRODUCT", ITEM_NUM);
					htBomMst.put("PARENT_PRODUCT_LOC",  "SHIPPINGAREA" + "_" + ITEM_LOC);
					htBomMst.put("PARENT_PRODUCT_BATCH", ITEM_BATCH);

					bomexistsflag = _BomDAO.isExisit(htBomMst);
					
					if(bomexistsflag)
					{
						Hashtable htBomUpdateMst = new Hashtable();
						htBomUpdateMst.put(IDBConstants.PLANT, (String) PLANT);
						htBomUpdateMst.put("PARENT_PRODUCT", ITEM_NUM);
						htBomUpdateMst.put("PARENT_PRODUCT_LOC",  "SHIPPINGAREA" + "_" + ITEM_LOC);
						htBomUpdateMst.put("PARENT_PRODUCT_BATCH",  ITEM_BATCH);
						
						StringBuffer sql1 = new StringBuffer(" SET ");
						sql1.append(IDBConstants.PARENT_LOC + " ='"
								+ ITEM_LOC + "'");
						sql1.append(" , "+IDBConstants.CHILD_LOC+"='"
								+ ITEM_LOC + "'");
						sql1.append("," +"STATUS" + " = 'A' ");
						
						bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst,"");
						if(bomupdateflag)
						{   
							boolean invResult=false;
//							boolean queryUpdateInv=false;
							
							
							InvMstDAO invMstDAO = new InvMstDAO();
																		
							
							Hashtable htInvParentBom = new Hashtable();
							htInvParentBom.put(IConstants.PLANT, (String) PLANT);
						    htInvParentBom.put("ITEM",  ITEM_NUM);
							htInvParentBom.put("LOC", ITEM_LOC);
							htInvParentBom.put("USERFLD4", ITEM_BATCH);
						
								if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
									
									Hashtable htInsertInvParentBom = new Hashtable();
									htInsertInvParentBom.put(IConstants.PLANT, (String) PLANT);
									htInsertInvParentBom.put("ITEM", ITEM_NUM);
									htInsertInvParentBom.put("LOC",  ITEM_LOC);
									htInsertInvParentBom.put("USERFLD4", ITEM_BATCH);
									htInsertInvParentBom.put("QTY",  INV_QTY);
												
									invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
									
									if (!invResult) {
										throw new Exception(
												"Unable to process kitting Sales reverse location transfer ,Save inventory parent product failed");
									} 
								
								}
								
						}
						
					
					}
				/*}
				else
				{
					
				}
				*/
				
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;

				}
		
				
				//bom updation end
	
			}
			if (flaginvdet == true) {//Shopify Inventory Update
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
//							String result="";
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
		}else{
			flag =true;
		}
	}
	        MovHisDAO mdao = new MovHisDAO(PLANT);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", (String) PLANT);
			htm.put("DIRTYPE", "OB_REVERSE_OUT");
			htm.put("ORDNUM", DO_NUM);
			htm.put("MOVTID", "OUT");
			htm.put("RECID", "");
			htm.put("ITEM", ITEM_NUM);
			htm.put("LOC", "SHIPPINGAREA" + "_" + ITEM_LOC);
			htm.put("Batno", ITEM_BATCH);
			htm.put("qty", "-"+REVERSE_QTY);
			htm.put(IDBConstants.CUSTOMER_CODE, CUST_NO);
			htm.put(IDBConstants.UOM, UOM);
			htm.put(IDBConstants.TRAN_DATE, strMovHisTranDate);
			htm.put("CRBY", (String) LOGIN_USER);
			htm.put("CRAT", (String) DateUtils.getDateTime());
			htm.put("REMARKS", REMARKS);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if(inserted){
				htm.clear();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", TransactionConstants.OB_REVERSE);
				htm.put("ORDNUM", DO_NUM);
				htm.put("MOVTID", "IN");
				htm.put("RECID", "");
				htm.put("ITEM", ITEM_NUM);
				htm.put("LOC", ITEM_LOC);
				htm.put("Batno", ITEM_BATCH);
				htm.put("qty", REVERSE_QTY);
				htm.put(IDBConstants.CUSTOMER_CODE, CUST_NO);
				htm.put(IDBConstants.UOM, UOM);
				htm.put(IDBConstants.TRAN_DATE, strMovHisTranDate);
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				htm.put("REMARKS", REMARKS);
				inserted = mdao.insertIntoMovHis(htm);
			}
			
			if (!inserted) {
				flag = false;
				throw new Exception( "Product Reversed  Failed, Error In update  MOVHIS :" + " " + ITEM_NUM);
			} else {
				flag = true;
			}

			if (flag) {
				request.getSession().setAttribute("RESULT",
						"Product : " + ITEM_NUM + "  reversed successfully!");
				response
						.sendRedirect("../salestransaction/orderpickreturn?action=View&PLANT="
								+ PLANT + "&DONO=" + DO_NUM);

			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Error in reversing Product : " + ITEM_NUM + " Order");

				response
						.sendRedirect("jsp/OutBoundOrderReverseConfirm.jsp?action=resulterror&DONO="
								+ DO_NUM
								+ "&DOLNNO="
								+ DO_LN_NUM
								+ "&ITEM="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ StrUtils
										.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&LOC="
								+ ITEM_LOC
								+ "&BATCH="
								+ ITEM_BATCH
								+ "&REF="
								+ ITEM_EXPDATE
								+ "&ORDERQTY="
								+ QTY
								+ "&PICKEDQTY="
								+ PICKED_QTY
								+ "&REVERSEDQTY="
								+ REVERSE_QTY);

			}

		} catch (Exception e) {
			MLogger
					.log(
							0,
							"######################### Exception ::process_OutBoundorderReversingByWMS() () : ######################### \n");
			MLogger.log(0, "" + e.getMessage());
			MLogger
					.log(
							0,
							"######################### Exception :: process_InboundorderReversingByWMS() : ######################### \n");

			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			response
					.sendRedirect("jsp/OutBoundOrderReverseConfirm.jsp?action=catchrerror&DONO="
							+ DO_NUM
							+ "&DOLNNO="
							+ DO_LN_NUM
							+ "&ITEM="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ QTY
							+ "&PICKEDQTY="
							+ PICKED_QTY
							+ "&REVERSEDQTY="
							+ REVERSE_QTY);

			throw e;
		}
		return xmlStr;
	}

    private String mobileEnquiryConfirm(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {

         
            boolean flag = false;
//            StrUtils StrUtils = new StrUtils();
            HttpSession session = request.getSession();
//            Map mp = new HashMap();
            String PLANT = "",DONO = "",STATUS="";//LOGIN_USER = "", 

            try {
             
                PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
                DONO = StrUtils.fString(request.getParameter("DONO"));
                STATUS = StrUtils.fString(request.getParameter("Status"));
//                LOGIN_USER = (String) session.getAttribute("LOGIN_USER");

                
                            
                            Hashtable htCondiupdate = new Hashtable();
                            htCondiupdate.put("PLANT",PLANT);
                            htCondiupdate.put("dono", DONO);
                            
                            DoDetDAO _DoDetDAO = new DoDetDAO();
                            _DoDetDAO.setmLogger(mLogger);
                            DoHdrDAO _DoHdrDAO = new DoHdrDAO();
                            _DoHdrDAO.setmLogger(mLogger);
                            DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
                            _DoTransferHdrDAO.setmLogger(mLogger);
                            DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
                            _DoTransferDetDAO.setmLogger(mLogger);
                            
                            if (STATUS.equalsIgnoreCase("NEW")){
                                
                                String  querydoDet = "set qtyPick=0,qtyis= 0, PickStatus='N',Lnstat='N' ";
                                  flag = _DoDetDAO.update(querydoDet, htCondiupdate, "");
                                  if(flag){
                                      flag=_DoTransferDetDAO.update(querydoDet, htCondiupdate, "");
                                      if(flag){
                                      String querydoHdr = "set  STATUS='N',PickStaus='N' ";
                                      flag = _DoHdrDAO.update(querydoHdr, htCondiupdate, "");
                                      if(flag){
                                          flag=_DoTransferHdrDAO.update(querydoHdr, htCondiupdate, "");    
                                      }
                                      }
                                  }
                            } else if (STATUS.equalsIgnoreCase("OPEN")){
                                
                              
                              String  querydoDet = "set qtyPick=0,qtyis= 0, PickStatus='O',Lnstat='O' ";
                                flag = _DoDetDAO.update(querydoDet, htCondiupdate, "");
                                if(flag){
                                    flag=_DoTransferDetDAO.update(querydoDet, htCondiupdate, "");
                                    if(flag){
                                    String querydoHdr = "set  STATUS='O',PickStaus='O' ";
                                    flag = _DoHdrDAO.update(querydoHdr, htCondiupdate, "");
                                    if(flag){
                                        flag=_DoTransferHdrDAO.update(querydoHdr, htCondiupdate, "");    
                                    }
                                    }
                                }
                            }else if (STATUS.equalsIgnoreCase("CLOSE")){
                                
                                String  querydoDet = "set qtyPick=1,qtyis= 1, PickStatus='C',Lnstat='C' ";
                                  flag = _DoDetDAO.update(querydoDet, htCondiupdate, "");
                                  if(flag){
                                      flag=_DoTransferDetDAO.update(querydoDet, htCondiupdate, "");
                                      if(flag){
                                      String querydoHdr = "set  STATUS='C',PickStaus='C' ";
                                      flag = _DoHdrDAO.update(querydoHdr, htCondiupdate, "");
                                      if(flag){
                                          flag=_DoTransferHdrDAO.update(querydoHdr, htCondiupdate, "");    
                                      }
                                      }
                                  }                                
                            }
                            
                         
                        
                 
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    request.getSession().setAttribute("CATCHERROR", e.getMessage());
                    response.sendRedirect("jsp/mobileEnquiryFullfillment.jsp?action=View&PLANT="
                                                    + PLANT + "&DONO=" + DONO + "&result=catchrerror");
                    throw e;
            }
            if (flag) {
                request.getSession().setAttribute("RESULT",
                                "Mobile Enquiry Order status updated successfully!");

                
                response.sendRedirect("jsp/mobileEnquiryFullfillment.jsp?DONO=" + DONO
                                + "&action=View&ORDERTYPE=MOBILE ENQUIRY");
            }
            return xmlStr;
    }
    private boolean DisplayMobileOrderData(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {

            String dono = StrUtils.fString(request.getParameter("DONO"));
            String orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
            String fieldDesc = "";

            ArrayList al = new ArrayList();
            Hashtable htCond = new Hashtable();
            Map m = new HashMap();

            if (dono.length() > 0) {

                    HttpSession session = request.getSession();
                    String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
                    htCond.put("PLANT", plant);
                    htCond.put("DONO", dono);
                    htCond.put("ORDERTYPE", orderType);
                   

                    String query = "dono,custName,custCode,jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
                    al = _DOUtil.getMobileEnQuiryOrderDetails(query, htCond);
                    if (al.size() > 0) {
                            m = (Map) al.get(0);

                    }

            }
            request.getSession().setAttribute("podetVal", m);
            request.getSession().setAttribute("dono", dono);
            request.getSession().setAttribute("RESULT", fieldDesc);

            return true;
    }

    private String MobileShopOBOrderIssueConfirm(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {

            String sepratedtoken1 = "";
            boolean flag = false;
//            StrUtils StrUtils = new StrUtils();
            Map receiveMaterial_HM = null;

            Map mp = null;
            mp = new HashMap();
            String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
            String LOGIN_USER = "";//ITEM_DESCRIPTION = "", 
            String SHIPPINGNO = "";
            String PICKED_QTY = "", DO_BATCH = "", LOC = "", ISSUING_QTY = "";//, ORDER_QTY = "";

            try {

                    String sepratedtoken = "";
                    String totalString = StrUtils.fString(request
                                    .getParameter("TRAVELER"));
                    String REMARK = StrUtils.fString(request.getParameter("REMARK2"));

                    StringTokenizer parser = new StringTokenizer(totalString, "=");

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

                            HttpSession session = request.getSession();
                            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
                            DO_NUM = StrUtils.fString((String) mp.get("data1"));
                            DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
                            ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
//                            ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));
//                            ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
                            PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
                            ISSUING_QTY = StrUtils.fString((String) mp.get("data7"));
                            LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
                            LOC = StrUtils.fString((String) mp.get("data9"));
                            DO_BATCH = StrUtils.fString((String) mp.get("data10"));

                            double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
                            double issuingQty = Double.parseDouble(((String) ISSUING_QTY.trim()
                                            .toString()));

                            String issueQty = String.valueOf(pickedqty - issuingQty);
                            System.out.println("Issue Quantity"+ issueQty);
                            if (SHIPPINGNO.length() == 0) {
                                    SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
                            }
                            receiveMaterial_HM = new HashMap();
                            receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                            receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
                            receiveMaterial_HM.put(IConstants.QTY, issueQty);
                            receiveMaterial_HM.put(IConstants.DODET_DONUM, DO_NUM);
                            receiveMaterial_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
                            receiveMaterial_HM.put(IConstants.LOC, LOC);
                            receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                            receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
                                            .getCustCode(PLANT, DO_NUM));
                            receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(
                                            PLANT, DO_NUM));
                            receiveMaterial_HM.put(IConstants.BATCH, DO_BATCH);
                            receiveMaterial_HM.put("SHIPPINGNO", SHIPPINGNO);
                            receiveMaterial_HM.put(IConstants.REMARKS, REMARK);
                            xmlStr = "";
                            flag = _DOUtil.process_IssueMaterial(receiveMaterial_HM);
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    request.getSession().setAttribute("CATCHERROR", e.getMessage());
                    response
                                    .sendRedirect("jsp/mobileShoppingConfirmWInv.jsp?action=View&PLANT="
                                                    + PLANT + "&DONO=" + DO_NUM + "&result=catchrerror");
                    throw e;
            }
            if (flag) {
                    request.getSession().setAttribute("RESULT",
                                    "Goods Issued successfully!");

            
                    response.sendRedirect("jsp/mobileShoppingFullfillment.jsp?DONO=" + DO_NUM
                                    + "&action=View");
            }
            return xmlStr;
    }
    
    
    
    private String MobileShopIssueConfirmWOInvcheck(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {

            String sepratedtoken1 = "";
            boolean flag = false;
//            StrUtils StrUtils = new StrUtils();
            Map receiveMaterial_HM = null;

            Map mp = null;
            mp = new HashMap();
            String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
            String LOGIN_USER = "";//ITEM_DESCRIPTION = "", 
            String SHIPPINGNO = "";
            String PICKED_QTY = "", DO_BATCH = "", LOC = "", ISSUING_QTY = "";//, ORDER_QTY = "";

            try {

                    String sepratedtoken = "";
                    String totalString = StrUtils.fString(request
                                    .getParameter("TRAVELER"));
                    String REMARK = StrUtils.fString(request.getParameter("REMARK2"));

                    StringTokenizer parser = new StringTokenizer(totalString, "=");

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

                            HttpSession session = request.getSession();
                            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
                            DO_NUM = StrUtils.fString((String) mp.get("data1"));
                            DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
                            ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
//                            ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));
//                            ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
                            PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
                            ISSUING_QTY = StrUtils.fString((String) mp.get("data7"));
                            LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
                            LOC = StrUtils.fString((String) mp.get("data9"));
                            DO_BATCH = StrUtils.fString((String) mp.get("data10"));

                            double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
                            double issuingQty = Double.parseDouble(((String) ISSUING_QTY.trim()
                                            .toString()));

                            String issueQty = String.valueOf(pickedqty - issuingQty);
                            System.out.println("Issue Quantity"+ issueQty);
                            if (SHIPPINGNO.length() == 0) {
                                    SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
                            }
                            receiveMaterial_HM = new HashMap();
                            receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                            receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
                            receiveMaterial_HM.put(IConstants.QTY, issueQty);
                            receiveMaterial_HM.put(IConstants.DODET_DONUM, DO_NUM);
                            receiveMaterial_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
                            receiveMaterial_HM.put(IConstants.LOC, LOC);
                            receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                            receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
                                            .getCustCode(PLANT, DO_NUM));
                            receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(
                                            PLANT, DO_NUM));
                            receiveMaterial_HM.put(IConstants.BATCH, DO_BATCH);
                            receiveMaterial_HM.put("SHIPPINGNO", SHIPPINGNO);
                            receiveMaterial_HM.put(IConstants.REMARKS, REMARK);
                            xmlStr = "";
                            flag = _DOUtil.process_IssueMaterialWOInvcheck(receiveMaterial_HM);
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    request.getSession().setAttribute("CATCHERROR", e.getMessage());
                    response.sendRedirect("jsp/mobileShoppingWOInvcheck.jsp?action=View&PLANT="
                                                    + PLANT + "&DONO=" + DO_NUM + "&result=catchrerror");
                    throw e;
            }
            if (flag) {
                    request.getSession().setAttribute("RESULT", "Goods Issued successfully!");

            
                    response.sendRedirect("jsp/mobileShopWOInv.jsp?DONO=" + DO_NUM
                                    + "&action=View");
                                    
            }
            return xmlStr;
    }
    /* Created by Bruhan for ouboundorder issue reversal on 01 Oct 2013
     ************Modification History*********************************
	   Oct 21 2014 Bruhan, Description: To include Transaction date
	*/
    private String process_OutboundOrderIssueReversal(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
//		StrUtils StrUtils = new StrUtils();
		Map receiveMaterial_HM = null;

		String PLANT = "", DONO = "", LOC = "",dolno = "",item = "",QTYOR = "",reverseQTY = "",returnQty="",UOMQTY="";//ITEM_DESCRIPTION = "",
		String CUST_NAME="",LOGIN_USER="",ITEM_BATCH="",batch="",issuedQty="",TRANSACTIONDATE = "",strMovHisTranDate="";//,strTranDate="";		
		Boolean allChecked = false;
		double reversQty=0;
		//UserTransaction ut = null;
//		Map checkedDOS = new HashMap();
		String REASONCODE="", REMARKS="";//sepratedtoken1 = "",
//		Map mp = new HashMap();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
	        String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
	        DONO = StrUtils.fString(request.getParameter("DONO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
		    REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
		    TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
//			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
		      
			
       
			
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			
		process: 	
			if (chkdDoNos != null)    {  
        	for (int i = 0; i < chkdDoNos.length; i++){
        		String	data = chkdDoNos[i];
				String[] chkdata = data.split(",");
        		
			dolno = chkdata[0];
			LOC = chkdata[1];
			batch = chkdata[2];
	    	item = chkdata[3];
			QTYOR = chkdata[4];
			issuedQty = chkdata[5];
			DONO = chkdata[6];
			UOMQTY = chkdata[7];
			String lineno = dolno+"_"+LOC+"_"+batch;
		  				
					ITEM_BATCH = StrUtils.fString(request.getParameter("batch_"+lineno));
					issuedQty = StrUtils.fString(request.getParameter("Qtyissued_"+lineno));
					returnQty = StrUtils.fString(request.getParameter("QtyReverse_"+lineno));	
					reversQty = Double.parseDouble(returnQty);
					reversQty = StrUtils.RoundDB(reversQty, IConstants.DECIMALPTS);
					reverseQTY = String.valueOf(reversQty);
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, item);
				receiveMaterial_HM.put(IConstants.ITEM_DESC, itemMstDAO.getItemDesc(PLANT, item));
				receiveMaterial_HM.put(IConstants.DODET_DONUM, DONO);
				receiveMaterial_HM.put(IConstants.DODET_DOLNNO, dolno);
				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
		
				receiveMaterial_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"
						+ LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
						.getCustCode(PLANT, DONO));
				receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.QTY, reverseQTY);
				receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
				receiveMaterial_HM.put("ISSUEDQTY", issuedQty);
				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
				receiveMaterial_HM.put(IConstants.RSNDESC, REASONCODE);
				receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				receiveMaterial_HM.put("UOMQTY", UOMQTY);
				
				flag = _DOUtil.process_OBISSUEReversal(receiveMaterial_HM)&& true;
				
				if(!flag)
					break process;
			}
		}
			if (flag) {
				
				//DbBean.CommitTran(ut);				
				request.getSession().setAttribute(
						"RESULT",
						"Sales Order : " + DONO
								+ "  Reversed successfully!");
				response.sendRedirect("jsp/OutboundOrderIssueReversal.jsp?action=View&PLANT="
						+ PLANT +"&DONO="+DONO+"&result=sucess");
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession()
						.setAttribute(
								"RESULTERROR",
								"Failed to Reverse Sales Order : "
										+ DONO);
				response.sendRedirect("jsp/OutboundOrderIssueReversal.jsp?result=error");
			}
			
			}
			
		 catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
			response
					.sendRedirect("jsp/OutboundOrderIssueReversal.jsp?action=View&PLANT="
							+ PLANT 
							+"&DONO="+DONO
							+ "&allChecked="
							+allChecked
							+"&result=catchrerror");
			throw e;
		}
		
		return xmlStr;
	}

    private String OutGoingDataBulkbyOrders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
//		StrUtils StrUtils = new StrUtils();
		Map issMat_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", DONO = "", LOC = "",dolno = "",item = "",QTYOR = "",issuingQty = "";//,qtypick="";//ITEM_DESCRIPTION = "",
		String ITEM_BATCH = "NOBATCH",BATCH_ID="-1",ITEM_QTY = "",PICKED_QTY = "",PICKING_QTY = "",CUST_NAME = "",LOGIN_USER = "",SHIPPINGNO = "",ISNONSTKFLG="";
		String REMARKS = "",ISSUEDQTY="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",PICKISSUETYPE="",FROMDATE="",TODATE="",INVOICENO="",UOMQTY="",UOM="";
		String creditLimit ="", creditBy = "";
		String alertitem="",MINSTKQTY="";
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
			PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String ISINVENTORYMINQTY = new DoHdrDAO().getISINVENTORYMINQTY(PLANT);//Thanzi
	        String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
			LOC = StrUtils.fString(request.getParameter("LOC_0"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			FROMDATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			TODATE = StrUtils.fString(request.getParameter("TO_DATE"));
			PICKISSUETYPE = StrUtils.fString(request.getParameter("PICKISSUE"));
			//ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, item);
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
	         if (chkdDoNos != null)    {     
    				for (int i = 0; i < chkdDoNos.length; i++)       { 
    				String	data = chkdDoNos[i];
    				String[] chkdata = data.split(",");
    				String dno=chkdata[0]+"_"+chkdata[1];
    			issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+dno));
    			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+dno));
    			BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID_"+dno));
    			if (ITEM_BATCH.length() == 0) {
    					ITEM_BATCH = "NOBATCH";
    				}
    			
    					checkedDOS.put(dno, issuingQty+":"+ITEM_BATCH);
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

			DONO = StrUtils.fString((String) mp.get("data1"));
			dolno = StrUtils.fString((String) mp.get("data2"));
			QTYOR = StrUtils.fString((String) mp.get("data3"));
			PICKED_QTY = StrUtils.fString((String) mp.get("data4"));
			ISSUEDQTY = StrUtils.fString((String) mp.get("data5"));
			CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data6")));
			UOMQTY= StrUtils.fString((String) mp.get("data7"));
			UOM= StrUtils.fString((String) mp.get("data8"));
			
			
			
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullIssue")!=null){
				fullIssue = true;
			}
		
			 
			ArrayList DODetails = null;

    		Hashtable htDoDet = new Hashtable();
    		String queryDoDet = "item,itemDesc,QTYOR,isnull(QtyPick,0)QtyPick";
    				
    		if(!PICKISSUETYPE.equalsIgnoreCase("Issue"))
    		{
    				issuingQty = StrUtils.fString(request.getParameter("issuingQty_"+DONO+"_"+dolno));	
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+DONO+"_"+dolno));
					BATCH_ID =  StrUtils.fString(request.getParameter("BATCH_ID_"+DONO+"_"+dolno));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
			}
			    	htDoDet.put(IConstants.DODET_DONUM, DONO);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.DODET_DOLNNO, dolno);
		    		DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
					if (DODetails.size() > 0) {	
						Map map1 = (Map) DODetails.get(0);
						item = (String) map1.get("item");
//						ITEM_DESCRIPTION = (String) map1.get("itemDesc");
						QTYOR = (String) map1.get("QTYOR");
//						qtypick = (String) map1.get("QtyPick");
					}
					ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, item);
					if(!ISNONSTKFLG.equalsIgnoreCase("Y") && !PICKISSUETYPE.equalsIgnoreCase("Issue")){ //If Non Stock Item then omit Inventory Validation
					List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,item, LOC, ITEM_BATCH);
					double invqty = 0;
                    double Detuctqty = 0;
					double STKQTY = 0;
					if (listQry.size() > 0) {
						for (int j = 0; j < listQry.size(); j++) {
							Map m = (Map) listQry.get(j);
							ITEM_QTY = (String) m.get("qty");
			                MINSTKQTY = (String) m.get("MINSTKQTY");
							invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
							STKQTY = Double.parseDouble(((String) MINSTKQTY.trim()));
						}
						if(STKQTY!=0) {
						Detuctqty = invqty-pickingQty;
						if(STKQTY>Detuctqty) {
							if(alertitem.equalsIgnoreCase("")) {
								alertitem =item;
							}else {
								alertitem = alertitem+" , "+item;
							}
						}
						}
						
						if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
							alertitem = alertitem;
						else 
							alertitem="";
						
						double curqty = pickingQty * Double.valueOf(UOMQTY);
						if(invqty < curqty){
							throw new Exception(
									"Error in picking Sales Order : Inventory not found for the product/batch " +item+ " scanned at the location "+LOC + " for OrderNo "+DONO+ " and LineNo "+dolno);							
						}
					} 
					
					else {
						
						throw new Exception(
								"Error in picking Sales Order : Inventory not found for the product/batch " +item+ " scanned at the location "+LOC + "for OrderNo "+DONO+ " and LineNo "+dolno);
						
					}
					}
					
					// check for item in location
					
                UserLocUtil uslocUtil = new UserLocUtil();
                uslocUtil.setmLogger(mLogger);
                boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC);
                if(!isvalidlocforUser){
                    throw new Exception(" Loc : "+LOC+" is not User Assigned Location");
                }
					
//				double orderqty = Double.parseDouble(((String) QTYOR.trim()));
//				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				if(PICKISSUETYPE.equalsIgnoreCase("Issue"))
	    		{
					pickingQty = Double.parseDouble(PICKED_QTY)-Double.parseDouble(ISSUEDQTY);
					pickingQty = StrUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
					PICKING_QTY = String.valueOf(pickingQty);
					PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
	    		}
				else
				{
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					pickingQty = StrUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
					PICKING_QTY = String.valueOf(pickingQty);
					PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
				}
				SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
				issMat_HM = new HashMap();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put(IConstants.ITEM, item);
				//issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
				issMat_HM.put(IConstants.DODET_DONUM, DONO);
				issMat_HM.put(IConstants.DODET_DOLNNO, dolno);
				issMat_HM.put(IConstants.PODET_PONUM, DONO);
				issMat_HM.put(IConstants.PODET_POLNNO, dolno);
				issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				issMat_HM.put(IConstants.LOC, LOC);
				issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
				issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
				issMat_HM.put(IConstants.BATCH_ID, BATCH_ID);
				issMat_HM.put(IConstants.ORD_QTY, QTYOR);
				issMat_HM.put(IConstants.QTY, PICKING_QTY);
				issMat_HM.put(IConstants.INV_EXP_DATE, "");
				issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
				issMat_HM.put("INV_QTY", "1");
				issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
				issMat_HM.put(IConstants.REMARKS, REMARKS);
				issMat_HM.put("TYPE", "BULKOB");
				issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
				issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
				issMat_HM.put(IConstants.INVOICENO, INVOICENO);
				issMat_HM.put(IConstants.UOM, UOM);
				issMat_HM.put("UOMQTY", UOMQTY);
				CustUtil custUtil = new CustUtil();
				ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, DONO),PLANT);
				creditLimit   = (String)arrCust.get(24);
				creditBy   = (String)arrCust.get(35);
				
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
				issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
				issMat_HM.put(IConstants.BASE_CURRENCY, (String) session.getAttribute("BASE_CURRENCY"));
				
				if(PICKISSUETYPE.equalsIgnoreCase("Pick"))
				{
					//issMat_HM.put(IConstants.QTY, PICKING_QTY);
					flag = _DOUtil.process_DoPickingForPC(issMat_HM);
				}
				else if(PICKISSUETYPE.equalsIgnoreCase("Pick&Issue"))
				{
					//issMat_HM.put(IConstants.QTY, PICKING_QTY);
					flag = _DOUtil.process_DoPickIssueForPC(issMat_HM)&& true;
				}
				else
				{
					//issMat_HM.put(IConstants.QTY, qtypick);
					flag = _DOUtil.process_IssueMaterial(issMat_HM);
				}
				
				if(!PICKISSUETYPE.equalsIgnoreCase("Pick"))
				{
				if (flag == true) {//Shopify Inventory Update
   					Hashtable htCond = new Hashtable();
   					htCond.put(IConstants.PLANT, PLANT);
   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
   						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,item);						
						if(nonstkflag.equalsIgnoreCase("N")) {
   						String availqty ="0";
   						ArrayList invQryList = null;
   						htCond = new Hashtable();
   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,item, new ItemMstDAO().getItemDesc(PLANT, item),htCond);						
   						if (invQryList.size() > 0) {					
   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
//   								String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                availqty = (String)lineArr.get("AVAILABLEQTY");
                                System.out.println(availqty);
   							}
   							double availableqty = Double.parseDouble(availqty);
       						new ShopifyService().UpdateShopifyInventoryItem(PLANT, item, availableqty);
   						}	
						}
   					}
   				}
				}
				
			}
			if (flag) {
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				//DbBean.CommitTran(ut);		
				String msgflag=StrUtils.fString((String)	issMat_HM.get("msgflag"));
				if(msgflag.length()>0){
					request.getSession().setAttribute("RESULT",	msgflag	);
				}else{
					if(alertitem.equalsIgnoreCase("")) {
						request.getSession().setAttribute("RESULT", "Orders Picked/Issued successfully!");
					}else {
						request.getSession().setAttribute("RESULT", "Orders Picked/Issued successfully! <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>");
					}
				}
				response.sendRedirect("../salestransaction/orderpickissuebymultiple?action=View&PLANT="
						+ PLANT +"&PICKISSUE="+PICKISSUETYPE+"&LOC="+LOC+"&FROM_DATE="
						+FROMDATE
						+"&TO_DATE="
						+TODATE
						+"&result=sucess");
			} else {
				//DbBean.RollbackTran(ut);
				request.getSession()
						.setAttribute(
								"RESULTERROR",
								"Failed to Pick/Issue  ");
										
				response.sendRedirect("../salestransaction/orderpickissuebymultiple?result=error");
			}
			
			}
			
		 catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR", e.getMessage());
			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
			response
					.sendRedirect("../salestransaction/orderpickissuebymultiple?action=View&PLANT="
							+ PLANT 
							+"&FROM_DATE="
							+FROMDATE
							+"&TO_DATE="
							+TODATE
							+"&LOC="
							+LOC
							+"&PICKISSUE="
							+PICKISSUETYPE
							+"&REF="
							+REMARKS
							+ "&allChecked="
							+allChecked
							+"&fullReceive="
							+fullIssue
							+"&result=catchrerror");
			throw e;
		}
		
		return xmlStr;
	}


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
	
	 private static AtomicInteger counts = new AtomicInteger(0);
	
	 public int getNextCountValue() {
	        return counts.incrementAndGet();
	    }
	 
	    public static void resetCounter() {
	        counts = new AtomicInteger(0);
	    }
}
