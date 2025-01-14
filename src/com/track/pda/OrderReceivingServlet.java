package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.ItemUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.POUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

/*- ************Modification History*********************************
  Dec 5 2014 Bruhan, Description:Modify Method:load_item_details- To include item location
*/
@SuppressWarnings({"rawtypes", "unchecked"})
public class OrderReceivingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.OrderReceivingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.OrderReceivingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 8800687029132079L;
	private POUtil _POUtil = null;

	private String PLANT = "";
	private String xmlStr = "";
	private String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	StrUtils strUtils = new StrUtils();
	POUtil _poUtil = new POUtil();

	public void init() throws ServletException {
		_POUtil = new POUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		
		try {
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_POUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_item_details(request, response);
			}
			
			else if (action.equalsIgnoreCase("load_item_details_Kitting")) {
				xmlStr = "";
				xmlStr = load_item_details_Kitting(request, response);
			}
			else if (action.equalsIgnoreCase("load_Kitting_Item")) {
				xmlStr = "";
				xmlStr = load_Kitting_Item(request, response);
			}
			else if (action.equalsIgnoreCase("load_Kitting_Childitem_details")) {
				xmlStr = "";
				xmlStr = load_Kitting_Childitem_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_kitting_parent_products")) {
				xmlStr = "";
				xmlStr = load_kitting_parent_products(request, response);
	
		    }
			else if (action.equalsIgnoreCase("load_kitting_parent_locations")) {
					xmlStr = "";
					xmlStr = load_kitting_parent_locations(request, response);
			}
			else if (action.equalsIgnoreCase("load_kitting_check_parent_batch")) {
				xmlStr = "";
				xmlStr = load_kitting_check_parent_batch(request, response);
		    }
			else if (action.equalsIgnoreCase("load_kitting_child_products")) {
				xmlStr = "";
				xmlStr = load_kitting_child_products(request, response);
				
			}else if (action.equalsIgnoreCase("load_kitting_child_batch")) {
						xmlStr = "";
						xmlStr = load_kitting_child_batch(request, response);
			
			}else if (action.equalsIgnoreCase("load_DeKitting_Item")) {
				xmlStr = "";
				xmlStr = load_DeKitting_Item(request, response); 
			}else if (action.equalsIgnoreCase("load_DeKitting_Childitem_details")) {
				xmlStr = "";
				
				xmlStr = load_DeKitting_Childitem_details(request, response);  
			}else if (action.equalsIgnoreCase("load_dekitting_parent_batch")) {
				xmlStr = "";
				
				xmlStr = load_dekitting_parent_batch(request, response);  
				
			}else if (action.equalsIgnoreCase("load_dekitting_child_products")) {
				xmlStr = "";
				
				xmlStr = load_dekitting_child_products(request, response);  
			
			}else if (action.equalsIgnoreCase("load_dekitting_child_batch")) {
				xmlStr = "";
				
				xmlStr = load_dekitting_child_batch(request, response);  
			   	
		   }else if (action.equalsIgnoreCase("load_po_details")) {
				xmlStr = "";
				xmlStr = load_po_Details(request, response);
			} else if (action.equalsIgnoreCase("MultyReceive")) {
				xmlStr = "";
				xmlStr = getGoodsReceiptDetailsByWMS(request, response, "MULTIPLE_RECEVING");
			} else if (action.equalsIgnoreCase("Receive")) {
				xmlStr = "";
				xmlStr = getGoodsReceiptDetailsByWMS(request, response, "RECEVING");
			} else if (action.equalsIgnoreCase("ReceiveByRange")) {
				xmlStr = "";
				xmlStr = getGoodsReceiptDetailsByWMS(request, response, "RECV_BY_RANGE");
			} else if (action.equalsIgnoreCase("Reverse")) {
				xmlStr = "";
				xmlStr = getGoodsReverseDetailsByWMS(request, response);
			}

			else if (action.equalsIgnoreCase("process_receiveMaterial")) {
				xmlStr = "";
				xmlStr = process_orderReceiving(request, response);
			}
		   //---Added by Bruhan on March 28 2014, Description:To InboundOrderMultipleReversal
			else if (action.equalsIgnoreCase("IBMultipleReverseConfirm")) {
				xmlStr = "";
				xmlStr =  process_InboundOrderMultipleReversal(request, response);
			}
		    //---Added by Bruhan on March 28 2014, Description:To InboundOrderMultipleReversal
			
			else if (action.equalsIgnoreCase("View")) {

				String rflag = StrUtils.fString(request.getParameter("RFLAG"));
				
				String pono = StrUtils
						.fString(request.getParameter("PONO"));
				HttpSession session = request.getSession();
				String plant = StrUtils.fString((String) session.getAttribute("PLANT"));

//				String vend = "", deldate = "", jobNum = "", custName = "", personIncharge = "", contactNum = "";
//				String remark1 = "", remark2 = "", address = "", collectionDate = "", collectionTime = "";
				String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();
				
//				PODET _podet = new PODET();
				if (rflag.equals("1")) {
					if (pono.length() > 0) {

						htCond.put("PLANT", plant);
						htCond.put("PONO", pono);

						String query = " a.pono,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
						al = _poUtil.getSupplierHdrDetails(query, htCond, "");

						if (al.size() > 0) {
							m = (Map) al.get(0);

						} else {

							fieldDesc = "<tr><td>No Records Available</td></tr>";

						}
			
						request.getSession().setAttribute("isummaryvalue", m);
						request.getSession().setAttribute("ISUMMARYRESULT",	fieldDesc);

						response.sendRedirect("jsp/InboundOrderSummary.jsp?PONO="+ pono + "&action=View");
					}
				
			   }else if (rflag.equals("2")) {
					if (pono.length() > 0) {

						htCond.put("PLANT", plant);
						htCond.put("PONO", pono);

						String query = " a.pono,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
						al = _poUtil.getSupplierHdrDetails(query, htCond, "");

						if (al.size() > 0) {
							m = (Map) al.get(0);

						} else {

							fieldDesc = "<tr><td>No Records Available</td></tr>";

						}

					}
					request.getSession().setAttribute("isummaryvalue", m);
					request.getSession().setAttribute("ISUMMARYRESULT",	fieldDesc);
					response.sendRedirect("jsp/InboundsOrderReverse.jsp?PONO="	+ pono + "&action=View");
				//--Added by Bruhan on March 28 2014, Description: To View InboundMultipleReversal Data's
				}else if (rflag.equals("6")) {
//					boolean flag = 
							DisplayInboundData(request, response);
					pono = StrUtils.fString(request.getParameter("PONO"));
					//--Added by Bruhan on March 08 2014, Description: To View InboundMultipleReversal Data's
					request.getSession().setAttribute("RESULT","");
					//--End Added by Bruhan on March 08 2014, Description: To View InboundMultipleReversal Data's
					response.sendRedirect("jsp/InboundOrderMultipleReversal.jsp?PONO="+ pono + "&action=View");
				}
				//--End  Bruhan on March 28 2014, Description: To View InboundMultipleReversal Data's
			}else if (action.equalsIgnoreCase("ViewProductOrders")) {

				String item = request.getParameter("ITEM");
                                ItemMstDAO itemdao = new ItemMstDAO();
                                HttpSession session = request.getSession();
                                String plant = (String)session.getAttribute("PLANT");
                                String desc = itemdao.getItemDesc(plant,item);
                                desc=StrUtils.replaceCharacters2Send(desc);
			    response.sendRedirect("../purchasetransaction/receiptsummarybyproduct?ITEM="
			                    + item + "&DESC="+desc+"&action=View");
			} 
		    else if (action.equalsIgnoreCase("ViewByRange")) {

		           
		          
		                    String pono = StrUtils.fString(request.getParameter("PONO"));
		                    HttpSession session = request.getSession();
		                    String plant = StrUtils.fString((String) session
		                                    .getAttribute("PLANT"));

//		                    String vend = "", deldate = "", jobNum = "", custName = "", personIncharge = "", contactNum = "";
//		                    String remark1 = "", remark2 = "", address = "", collectionDate = "", collectionTime = "";
		                    String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

		                    ArrayList al = new ArrayList();
		                    Hashtable htCond = new Hashtable();
		                    Map m = new HashMap();
//		                    POUtil _poUtil = new POUtil();
		                    PoHdrDAO _poHdrDAO = new PoHdrDAO();
//		                    PODET _podet = new PODET();
		                    if (pono.length() > 0) {

		                            htCond.put("PLANT", plant);
		                            htCond.put("PONO", pono);

		                            String query = " a.pono,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
		                            //al = _poUtil.getSupplierHdrDetails(query, htCond, "");
		                            al = _poHdrDAO.selectSupplierHdrRecivingWithext(query, htCond, " and ORDER_STATUS <>'Draft' ");  //not by Draft - azees
		                            if (al.size() > 0) {
		                                    m = (Map) al.get(0);

		                            } else {

		                                    fieldDesc = "<tr><td>No Records Available</td></tr>";

		                            }

		                    }
		                    request.getSession().setAttribute("isummaryvalue", m);
		                    request.getSession().setAttribute("ISUMMARYRESULT",
		                                    fieldDesc);

		                    response.sendRedirect("../purchasetransaction/receiptsummarybyserial?PONO="
		                                    + pono + "&action=View");

		            } 
                else if (action.equalsIgnoreCase("ViewMultiple")) {

				String rflag = StrUtils.fString(request.getParameter("RFLAG"));
				if (rflag.equals("1")) {
					String pono = StrUtils
							.fString(request.getParameter("PONO"));
					HttpSession session = request.getSession();
					String plant = StrUtils.fString((String) session
							.getAttribute("PLANT"));
					 //Start added by Bruhan for Bulk receiving on 30 July 2012.
					String type =  StrUtils.fString(request.getParameter("type"));
					 //End added by Bruhan for Bulk receiving on 30 July 2012.

//					String vend = "", deldate = "", jobNum = "", custName = "", personIncharge = "", contactNum = "";
//					String remark1 = "", remark2 = "", address = "", collectionDate = "", collectionTime = "";
					String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

					ArrayList al = new ArrayList();
					Hashtable htCond = new Hashtable();
					Map m = new HashMap();
//					POUtil _poUtil = new POUtil();
					PoHdrDAO _poHdrDAO = new PoHdrDAO();
//					PODET _podet = new PODET();
					if (pono.length() > 0) {

						htCond.put("PLANT", plant);
						htCond.put("PONO", pono);

						String query = " a.pono,a.CustCode,a.custName,isnull(a.jobNum,'') jobNum,a.custName,isnull(a.personInCharge,'') personInCharge,isnull(a.contactNum,'') contactNum,isnull(a.address,'') address,isnull(a.collectionDate,'')collectionDate,isnull(a.collectionTime,'')collectionTime, isnull(a.remark1,'') remark1,isnull(a.remark2,'') remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
						//al = _poUtil.getSupplierHdrDetails(query, htCond, " ");
						al = _poHdrDAO.selectSupplierHdrRecivingWithext(query, htCond, " and ORDER_STATUS <>'Draft' ");  //not by Draft - azees
						if (al.size() > 0) {
							m = (Map) al.get(0);

						} else {

							fieldDesc = "<tr><td>No Records Available</td></tr>";

						}

					}
					request.getSession().setAttribute("isummaryvalue", m);
					request.getSession().setAttribute("ISUMMARYRESULT",
							fieldDesc);
					 //Start added by Bruhan for Bulk receiving on 30 July 2012.
					if(type.equalsIgnoreCase("BulkRecieve")){
						response.sendRedirect("../purchasetransaction/receiptsummary?PONO="
								+ pono + "&action=View");
					}
					 //End added by Bruhan for Bulk receiving on 30 July 2012.
					else{
					response.sendRedirect("jsp/InboundOrderMultipleReceiptSummary.jsp?PONO="
							+ pono + "&action=View");
					}

				}else if (rflag.equals("prdreceive")) {
					String icrno = StrUtils.fString(request.getParameter("ICRNO"));
					String icrnohdr = StrUtils.fString(request.getParameter("ICRNOHDR"));
					HttpSession session = request.getSession();
					String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
					String type =  StrUtils.fString(request.getParameter("type"));
					String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

					ArrayList al = new ArrayList();
					Hashtable htCond = new Hashtable();
					Map m = new HashMap();
					ItemMstDAO itemMstDAO = new ItemMstDAO();
					if (icrno.length() > 0) {
						htCond.put("PLANT", plant);
						htCond.put("ICRNO", icrno);
						String query = "ICRNO as dono,CustName,CustCode,isnull(jobNum,'') as jobNum,custName,'' as personInCharge,''contactNum,''address,''address2,''address3,RECEIVE_DATE as collectionDate,'' as collectionTime,isnull(NOTE,'') as remark1,isnull('','') as remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
						al = itemMstDAO.selectReceivingProduct(query, htCond);
						
						if (al.size() > 0) {
							m = (Map) al.get(0);
						} else {
							fieldDesc = "<tr><td>No Records Available</td></tr>";
						}
					}
					request.getSession().setAttribute("isummaryvalue", m);
					request.getSession().setAttribute("ISUMMARYRESULT",fieldDesc);
					response.sendRedirect("../productreceive/receiptsummary?ICRNO="+ icrno + "&HDRID="+icrnohdr+"&action=View");
					
					

				} else {
					String pono = StrUtils
							.fString(request.getParameter("PONO"));
					HttpSession session = request.getSession();
					String plant = StrUtils.fString((String) session
							.getAttribute("PLANT"));

//					String vend = "", deldate = "", jobNum = "", custName = "", personIncharge = "", contactNum = "";
//					String remark1 = "", remark2 = "", address = "", collectionDate = "", collectionTime = "";
					String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

					ArrayList al = new ArrayList();
					Hashtable htCond = new Hashtable();
					Map m = new HashMap();
					POUtil _poUtil = new POUtil();
//					PODET _podet = new PODET();
					_poUtil.setmLogger(mLogger);

					if (pono.length() > 0) {

						htCond.put("PLANT", plant);
						htCond.put("PONO", pono);

						String query = " a.pono,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
						al = _poUtil.getSupplierHdrDetails(query, htCond, "");

						if (al.size() > 0) {
							m = (Map) al.get(0);

						} else {

							fieldDesc = "<tr><td>No Records Available</td></tr>";

						}

					}
					request.getSession().setAttribute("isummaryvalue", m);
					request.getSession().setAttribute("ISUMMARYRESULT",
							fieldDesc);

					response.sendRedirect("jsp/InboundsOrderReverse.jsp?PONO="
							+ pono + "&action=View");
				}

			}
//process_InboundOrderMultipleReversal
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
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

	private String load_po_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aItemNum = "";//aPlant = "", 
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			Hashtable ht = new Hashtable();
			ht.put("plant", PLANT);
			ht.put("item", aItemNum);

			str = _POUtil.getItemDetails(PLANT, aItemNum);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}

	private String load_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String aPlant = "", aItem = "";//str = "", 
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			aItem = itemMstDAO.getItemIdFromAlternate(aPlant, aItem);
			boolean isexits = true;
			if (aItem == null) {
				isexits = false;
			}

			if (isexits) {
				Hashtable itemht = new Hashtable();
				itemht.put("PLANT", aPlant);
				itemht.put("ITEM", aItem);
				String itemDesc = StrUtils.fString(itemMstDAO.getItemDesc(aPlant, aItem));
				String uom = StrUtils.fString(itemMstDAO.getItemUOM(aPlant,	aItem));
				String purchaseuom = StrUtils.fString(itemMstDAO.getItemPurchaseUOM(aPlant,	aItem));
				String salesuom = StrUtils.fString(itemMstDAO.getItemSalesUOM(aPlant,	aItem));
				String inventoryuom = StrUtils.fString(itemMstDAO.getInvUOM(aPlant,	aItem));
				String nonStock = StrUtils.fString(itemMstDAO.getNonStockFlag(aPlant,	aItem));
				String itemloc = StrUtils.fString(itemMstDAO.getItemLoc(aPlant,	aItem));
				
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", aItem);
				xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
						.replaceCharacters2SendPDA(itemDesc).toString());
				xmlStr += XMLUtils.getXMLNode("uom", uom);
				xmlStr += XMLUtils.getXMLNode("purchaseuom", purchaseuom);
				xmlStr += XMLUtils.getXMLNode("salesuom", salesuom);
				xmlStr += XMLUtils.getXMLNode("inventoryuom", inventoryuom);
				xmlStr += XMLUtils.getXMLNode("nonStock", nonStock);
				xmlStr += XMLUtils.getXMLNode("itemloc", itemloc);
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}
			// /

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}

	private String load_item_details_Kitting(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String aPlant = "", aItem = "";//str = "", 
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			aItem = itemMstDAO.getKittingItem(aPlant, aItem);
			boolean isexits = true;
			if (aItem == null) {
				isexits = false;
			}

			if (isexits) {
				Hashtable itemht = new Hashtable();
				itemht.put("PLANT", aPlant);
				itemht.put("ITEM", aItem);
				String itemDesc = StrUtils.fString(itemMstDAO.getItemDesc(
						aPlant, aItem));
				String uom = StrUtils.fString(itemMstDAO.getItemUOM(aPlant,
						aItem));
				// /
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");

				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", aItem);
				xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils
						.replaceCharacters2SendPDA(itemDesc).toString());
				xmlStr += XMLUtils.getXMLNode("uom", uom);

				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				//xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Parent Product");
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}
			// /

			if (xmlStr.equalsIgnoreCase("")) {
				//xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Parent Product");
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	
	
	private String load_Kitting_Item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String aPlant = "", aItem = "";//str = "", 
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			aItem = invMstDAO.getChildKittingItem(aPlant, aItem);
			boolean isexits = true;
			if (aItem == null) {
				isexits = false;
			}

			if (isexits) {
				Hashtable itemht = new Hashtable();
				itemht.put("PLANT", aPlant);
				itemht.put("ITEM", aItem);
			
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");

				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", aItem);
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}
	

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
		
	private String load_DeKitting_Item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String aPlant = "", aParentItem = "",aChildItem="";//str = "", 
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aParentItem = StrUtils.fString(request.getParameter("PARENT_PRODUCT"));
			aChildItem = StrUtils.fString(request.getParameter("CHILD_PRODUCT"));

			BomDAO  bomDAO = new BomDAO();
			bomDAO.setmLogger(mLogger);
			aChildItem = bomDAO.getChildDeKittingItem(aPlant, aParentItem,aChildItem);
			boolean isexits = true;
			if (aChildItem == null) {
				isexits = false;
			}

			if (isexits) {
				Hashtable itemht = new Hashtable();
				itemht.put("PLANT", aPlant);
				itemht.put("CHILD_PRODUCT", aChildItem);
			
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");

				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", aChildItem);
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Child Product");
			}
	

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	
	private String load_Kitting_Childitem_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String aPlant = "", aItem = "",aLoc="",aBatch="";//str = "", 
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			aBatch = StrUtils.fString(request.getParameter("BATCH"));

			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			aItem = invMstDAO.getChildKittingItem(aPlant, aItem);
			boolean isexits = true;
			if (aItem == null) {
				isexits = false;
			}

			if (isexits) {
				Hashtable itemht = new Hashtable();
				itemht.put("PLANT", aPlant);
				itemht.put("ITEM", aItem);
				
				
				String batch = StrUtils.fString(invMstDAO.getChildKittingInvBatch(
						aPlant, aItem,aLoc,aBatch));
				String qty = StrUtils.fString(invMstDAO.getChildKittingInvQty(aPlant,
						aItem,aLoc,batch));
		
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", aItem);
				xmlStr += XMLUtils.getXMLNode("batch", batch);
				xmlStr += XMLUtils.getXMLNode("qty", qty);

				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Batch");
			}
		

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Batch");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	
	private String load_kitting_parent_products(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "";//,parent_product="",parent_location="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ItemUtil itemUtil = new ItemUtil();
			
			str = itemUtil.getkittingparentproduct(PLANT, userID);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_kitting_child_products(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",parent_location="";//,parent_product="",;//,parent_batch;
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
//			parent_location= StrUtils.fString(request.getParameter("PARENT_LOC"));
			ItemUtil itemUtil = new ItemUtil();
			
			str = itemUtil.getkittingchildproduct(PLANT, userID,parent_location);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_kitting_child_batch(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",parent_location="",child_product;//parent_product="",
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			parent_location= StrUtils.fString(request.getParameter("PARENT_LOC"));
			child_product= StrUtils.fString(request.getParameter("CHILD_PRODUCT"));
			ItemUtil itemUtil = new ItemUtil();
			
			str = itemUtil.getkittingchildbatch(PLANT, userID,parent_location,child_product);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	
	private String load_kitting_check_parent_batch(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",parent_product="",parent_location="",parent_batch="";
		try {
			
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			parent_location= StrUtils.fString(request.getParameter("PARENT_LOC"));
			parent_product= StrUtils.fString(request.getParameter("PARENT_PRODUCT"));
			parent_batch= StrUtils.fString(request.getParameter("PARENT_BATCH"));
			ItemUtil itemUtil = new ItemUtil();
		
			str = itemUtil.getkittingcheckbatch(PLANT, userID,parent_location,parent_product,parent_batch);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String load_kitting_parent_locations(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "";//,parent_product="",parent_location="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			LocUtil locUtil = new LocUtil();
			locUtil.setmLogger(mLogger);
			str = locUtil.getlocationsWithDesc(PLANT, userID);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
//	private String load_Dekitting_parent_locations(HttpServletRequest request,
//			HttpServletResponse response) throws IOException, ServletException,
//			Exception {
//		String str = "";
//
//		String PLANT = "", userID = "",parent_product="",parent_location="";
//		try {
//			PLANT = StrUtils.fString(request.getParameter("PLANT"));
//			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
//			LocUtil locUtil = new LocUtil();
//			locUtil.setmLogger(mLogger);
//			str = locUtil.getlocationsWithDesc(PLANT, userID);
//			if (str.equalsIgnoreCase("")) {
//
//				str = XMLUtils.getXMLMessage(1, "Details not found");
//			}
//		} catch (Exception e) {
//			this.mLogger.exception(this.printLog, "", e);
//			throw e;
//		}
//		return str;
//	}
	
	private String load_dekitting_parent_batch(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",parent_product="",parent_location="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			
			parent_location= StrUtils.fString(request.getParameter("PARENT_LOC"));
			parent_product= StrUtils.fString(request.getParameter("PARENT_PRODUCT"));
			ItemUtil itemUtil = new ItemUtil();
			
			str = itemUtil.getDekittingparentbatch(PLANT, userID,parent_location,parent_product);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_dekitting_child_products(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",parent_product="",parent_location="",parent_batch;//child_product,
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			parent_product= StrUtils.fString(request.getParameter("PARENT_PRODUCT"));
			parent_location= StrUtils.fString(request.getParameter("PARENT_LOC"));
			parent_batch= StrUtils.fString(request.getParameter("PARENT_BATCH"));
			//child_product= StrUtils.fString(request.getParameter("CHILD_PRODUCT"));
			ItemUtil itemUtil = new ItemUtil();
			
			str = itemUtil.getDekittingchildproduct(PLANT, userID,parent_product,parent_location,parent_batch);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_dekitting_child_batch(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",parent_product="",parent_location="",child_product="",parent_batch;
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			parent_product= StrUtils.fString(request.getParameter("PARENT_PRODUCT"));
			parent_location= StrUtils.fString(request.getParameter("PARENT_LOC"));
			parent_batch= StrUtils.fString(request.getParameter("PARENT_BATCH"));
			child_product= StrUtils.fString(request.getParameter("CHILD_PRODUCT"));
			
			ItemUtil itemUtil = new ItemUtil();
			
			str = itemUtil.getDekittingchildtbatch(PLANT, userID,parent_location,parent_product,child_product,parent_batch);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_DeKitting_Childitem_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String aPlant = "", aParentItem = "",aParentBatch="",aChildItem="",aLoc="",aBatch="";//str = "", 
		
		
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aParentItem = StrUtils.fString(request.getParameter("PARENT_PRODUCT"));
			aParentBatch= StrUtils.fString(request.getParameter("PARENT_BATCH"));
			aChildItem = StrUtils.fString(request.getParameter("CHILD_PRODUCT"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			aBatch = StrUtils.fString(request.getParameter("BATCH"));
			
			BomDAO  bomDAO = new BomDAO();
			bomDAO.setmLogger(mLogger);
					
			aChildItem = bomDAO.getChildDeKittingItem(aPlant, aParentItem,aChildItem);
			boolean isexits = true;
			if (aChildItem == null) {
				isexits = false;
			}
            
			if (isexits) {
				
				Hashtable itemht = new Hashtable();
				itemht.put("PLANT", aPlant);
				itemht.put("PARENT_PRODUCT", aParentItem);
				itemht.put("CHILD_PRODUCT", aChildItem);
			
				String batch = StrUtils.fString(bomDAO.getChildDeKittingBatch(
						aPlant, aParentItem,aChildItem,aLoc,aBatch,aParentBatch));
				
				String qty = StrUtils.fString(bomDAO.getChildDeKittingQty(aPlant, aParentItem,aChildItem,aLoc,batch,aParentBatch));
		
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", aChildItem);
				xmlStr += XMLUtils.getXMLNode("batch", batch);
				xmlStr += XMLUtils.getXMLNode("qty", qty);

				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Batch");
			}
		

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Batch");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
	private String process_orderReceiving(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String LOGIN_USER = "";//ITEM_DESCRIPTION = "", 
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("PO_NUM"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("PO_LN_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
			ITEM_EXPDATE = StrUtils.fString(request
					.getParameter("ITEM_EXPDATE"));

			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-"
						+ ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}

			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);

			receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT,	PO_NUM));

			xmlStr = "";

			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			boolean flag = true;

			if (flag) {
				xmlStr = _POUtil.process_ReceiveMaterial(receiveMaterial_HM);
			} else {
				throw new Exception(" Product Received already ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}
        
        

	private String getGoodsReceiptDetailsByWMS(HttpServletRequest request,
			HttpServletResponse response, String recvType)
			throws ServletException, IOException, Exception {

		String sepratedtoken1 = "";

//		StrUtils StrUtils = new StrUtils();

		Map mp = new HashMap();
		String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "";
		String CUST_NAME = "",RECFLG="";

		String DO_BATCH = "", UOM="",UOMQTY="";//DO_QTY = "", PICKED_QTY = "", ISSUED_QTY = "", TRAN_QTY = "", ISSUING_QTY = "", ORDER_QTY = "",LOC = "", 

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
			    String userId = StrUtils
			                    .fString((String) session.getAttribute("LOGIN_USER")).trim();
				DO_NUM = StrUtils.fString((String) mp.get("data1"));

				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data4")));
				CUST_NAME = StrUtils.fString((String) mp.get("data5"));
                                RECFLG = request.getParameter("RECFLAG");
				// LOC = "HOLDINGAREA";
				DO_BATCH = "NOBATCH";
				UOM = StrUtils.fString((String) mp.get("data6"));
				UOMQTY = StrUtils.fString((String) mp.get("data7"));
			        String sNextSeq ="";
				String redirectPage = "../purchasetransaction/inboundorderreceiving";
				 if (recvType.equalsIgnoreCase("RECEVING")) {
					redirectPage = "../purchasetransaction/inboundorderreceiving";
				  
				}
                                if (recvType.equalsIgnoreCase("MULTIPLE_RECEVING")) {
                                        redirectPage = "InboundOrderMultipleReceiving.jsp";
                                  
                                }
				else  if (recvType.equalsIgnoreCase("RECV_BY_RANGE")) {
                                          Hashtable htTblCnt = new Hashtable();
				           TblControlDAO _TblControlDAO =new TblControlDAO();
				          _TblControlDAO.setmLogger(mLogger);
				          
                                           htTblCnt.put(IDBConstants.PLANT,PLANT);
                                           htTblCnt.put(IDBConstants.TBL_FUNCTION,"RECV_BY_RANGE");
                                           htTblCnt.put(IDBConstants.TBL_PREFIX1,"");
                                           htTblCnt.put(IDBConstants.TBL_MIN_SEQ,"0000");
                                           htTblCnt.put(IDBConstants.TBL_MAX_SEQ,"9999");
                                           htTblCnt.put(IDBConstants.TBL_NEXT_SEQ, IDBConstants.TBL_FIRST_NEX_SEQ);
                                           htTblCnt.put(IDBConstants.CREATED_BY,userId );
                                           htTblCnt.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
                                           sNextSeq= _TblControlDAO.getTableControlDetails(htTblCnt);
                                       
                                        
				           redirectPage = "../purchasetransaction/receiptrange";
				    }
				response.sendRedirect("jsp/" + redirectPage + "?View&PLANT="
						+ PLANT + "&ORDERNO=" + DO_NUM + "&ORDERLNO="
						+ DO_LN_NUM + "&CUSTNAME="
						+ StrUtils.replaceCharacters2Send(CUST_NAME)
						+ "&ITEMNO=" + ITEM_NUM + "&ITEMDESC="
						+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
						+ "&BATCH=" + DO_BATCH+ "&RECFLAG=" + RECFLG+ "&SRANGE=" + sNextSeq+ "&UOM=" + UOM+ "&UOMQTY=" + UOMQTY);

			}
		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			request.getSession().setAttribute("CATCHERROR",
					"Error in Get Receiving  item details!");
			String redirectErrorPage = "InboundOrderSummary.jsp";
			    if (recvType.equalsIgnoreCase("RECEVING")) {
			           redirectErrorPage = "InboundOrderSummary.jsp";
			     
			    }
			    if (recvType.equalsIgnoreCase("MULTIPLE_RECEVING")) {
			           redirectErrorPage = "InboundOrderMultipleReceiptSummary.jsp";
			     
			    }
			    else  if (recvType.equalsIgnoreCase("RECV_BY_RANGE")) {
			            redirectErrorPage = "../purchasetransaction/receiptsummarybyserial";
			       }
			response.sendRedirect("jsp/" + redirectErrorPage
					+ "?action=catcherro&PLANT=" + PLANT + "&DONO=" + DO_NUM);
			throw e;
		}

		return xmlStr;
	}
            private String getGoodsReverseDetailsByWMS(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException,
                            Exception {

                    String sepratedtoken1 = "";
//                    StrUtils StrUtils = new StrUtils();
                    Map mp = new HashMap();
                    String PLANT = "", PO_NUM = "", PO_LN_NUM = "", ITEM_NUM = "";
                    String ITEM_DESCRIPTION = "", QTYOR = "", QTYRC = "";
                    String CUST_NAME = "";
                    String DO_BATCH = "", LOC = "", UOM = "", UOMQTY = "";//DO_QTY = "", PICKED_QTY = "", ISSUED_QTY = "", TRAN_QTY = "", ISSUING_QTY = "", ORDER_QTY = "", 
                    try {

                            String sepratedtoken = "";
                            String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
                            StringTokenizer parser = new StringTokenizer(totalString, "=");
                            while (parser.hasMoreTokens())

                            {
                                    int count = 1;
                                    sepratedtoken = parser.nextToken();
                                    StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");
                                    while (parser1.hasMoreTokens())

                                    {
                                            sepratedtoken1 = parser1.nextToken();
                                            mp.put("data" + count, sepratedtoken1);
                                            count++;

                                    }

                                    HttpSession session = request.getSession();
                                    PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
                                    PO_NUM = StrUtils.fString((String) mp.get("data1"));
                                    PO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
                                    ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
                                    ITEM_DESCRIPTION = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data4")));
                                    CUST_NAME = StrUtils.fString((String) mp.get("data5"));
                                    QTYOR = StrUtils.fString((String) mp.get("data6"));
                                    QTYRC = StrUtils.fString((String) mp.get("data7"));
                                    LOC = StrUtils.fString((String) mp.get("data8"));
                                    DO_BATCH = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data9")));
                                    UOM = StrUtils.fString((String) mp.get("data10"));
                                    UOMQTY = StrUtils.fString((String) mp.get("data11"));
                                    response.sendRedirect("jsp/InboundOrderReverseConfirm.jsp?View&PLANT="
                                                                    + PLANT
                                                                    + "&ORDERNO="
                                                                    + PO_NUM
                                                                    + "&ORDERLNO="
                                                                    + PO_LN_NUM
                                                                    + "&CUSTNAME="
                                                                    + StrUtils.replaceCharacters2Send((String) CUST_NAME)
                                                                    + "&ITEMNO="
                                                                    + ITEM_NUM
                                                                    + "&ITEMDESC="
                                                                    + StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
                                                                    + "&ORDERQTY="
                                                                    + QTYOR
                                                                    + "&RECEIVEQTY="
                                                                    + QTYRC
                                                                    + "&LOC=" + LOC + "&BATCH=" + DO_BATCH+ "&UOM=" + UOM+ "&UOMQTY=" + UOMQTY);

                            }
                    } catch (Exception e) {
                            this.mLogger.exception(this.printLog, "", e);
                            request.getSession().setAttribute("CATCHERROR",
                                            "Error in Get Receiving  item details!");
                            response
                                            .sendRedirect("jsp/InboundsOrderReverse.jsp?action=catcherro&PLANT="
                                                            + PLANT + "&PONO=" + PO_NUM);
                            throw e;
                    }
                    return xmlStr;
            }


          //--Added by Bruhan on March 28 2014, Description: To View InboundMultipleReversal Data's
            private boolean DisplayInboundData(HttpServletRequest request,
        			HttpServletResponse response) throws ServletException, IOException,
        			Exception {

        		String pono = StrUtils.fString(request.getParameter("PONO"));
        		String fieldDesc = "";

        		ArrayList al = new ArrayList();
        		Hashtable htCond = new Hashtable();
        		Map m = new HashMap();

        		if (pono.length() > 0) {

        			HttpSession session = request.getSession();
        			String plant = StrUtils.fString(
        					(String) session.getAttribute("PLANT")).trim();
        			htCond.put("PLANT", plant);
        			htCond.put("PONO", pono);

        			String query = " a.pono,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
					
        			al = _poUtil.getSupplierHdrDetails(query, htCond, "");

					if (al.size() > 0) {
						m = (Map) al.get(0);
					}

        		}
        		request.getSession().setAttribute("podetVal", m);
        		request.getSession().setAttribute("pono", pono);
        		request.getSession().setAttribute("RESULT", fieldDesc);
        		 //---Added by Bruhan on pril 07 2014, Description:To InboundOrderMultipleReversal
        		request.getSession().setAttribute("RESULTERROR","");
        		request.getSession().setAttribute("CATCHERROR","");
        		 //---End Added by Bruhan ENd on April 07 2014, Description:To InboundOrderMultipleReversal

        		return true;
        	
       }
            
         /*---Created by Bruhan on March 28 2014, Description:To PurchaseOrderMultipleReversal
         ************Modification History*********************************
     	   Oct 21 2014 Bruhan, Description: To include Transaction date
     	*/
        private String process_InboundOrderMultipleReversal(HttpServletRequest request,
        			HttpServletResponse response) throws ServletException, IOException,
        			Exception {
        	boolean flag = false;
//        	StrUtils StrUtils = new StrUtils();
        	Map receiveMaterial_HM = null;
        	String PLANT = "", PONO = "", LOC = "",polno = "",item = "",QTYOR = "",reverseQTY = "",returnQty="",UOMQTY="";
        		String CUST_NAME="",LOGIN_USER="",ITEM_BATCH="",batch="",receivedQty="",TRANSACTIONDATE = "",strMovHisTranDate="";//,ITEM_DESCRIPTION = "",recievedQTY="",strTranDate="";		
        		Boolean allChecked = false;
        		double reversQty=0;
//        		Map checkedDOS = new HashMap();
        		String REASONCODE="", REMARKS="";//sepratedtoken1 = "",
//        		Map mp = new HashMap();
        		ItemMstDAO itemMstDAO = new ItemMstDAO();
        		itemMstDAO.setmLogger(mLogger);
        		try {
        			HttpSession session = request.getSession();
        			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
        	        String[] chkdPoNos  = request.getParameterValues("chkdPoNo");
        	        PONO = StrUtils.fString(request.getParameter("PONO"));
        			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
        			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
        			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
        		    REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
        		    TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
        			if (TRANSACTIONDATE.length()>5)
        				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
//        			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
        		  if( request.getParameter("select")!=null){
        				allChecked = true;
        			}
        			process: 	
        				if (chkdPoNos != null)    {  
        					for (int i = 0; i < chkdPoNos.length; i++){
        						String	data = chkdPoNos[i];
        						String[] chkdata = data.split(",");
        						polno = chkdata[0];
        						LOC = chkdata[1];
        						batch = chkdata[2];
        						item = chkdata[3];
        						QTYOR = chkdata[4];
        						receivedQty = chkdata[5];
        						PONO = chkdata[6];
        						UOMQTY = chkdata[7];
        						String lineno = polno+"_"+LOC+"_"+batch;
            					ITEM_BATCH = StrUtils.fString(request.getParameter("batch_"+lineno));
//            					recievedQTY = StrUtils.fString(request.getParameter("QtyReceived_"+lineno));	
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
        						receiveMaterial_HM.put(IConstants.PODET_PONUM, PONO);
        						receiveMaterial_HM.put(IConstants.PODET_POLNNO, polno);
        						receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
        						receiveMaterial_HM.put(IConstants.LOC, LOC);
        	       				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
        	       				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PONO));
        	       				receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
        	       				receiveMaterial_HM.put(IConstants.QTY, reverseQTY);
        	       				receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
        	       				receiveMaterial_HM.put(IConstants.RECV_QTY, receivedQty);
        	       				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
        	       				receiveMaterial_HM.put(IConstants.RSNDESC, REASONCODE);
        	       				receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
        	       				receiveMaterial_HM.put("UOMQTY", UOMQTY);
        	        			flag = _POUtil.process_IBRECEIVEReversal(receiveMaterial_HM)&& true;
        				
        				if(!flag)
        					break process;
        			}
        		}
        			
        			if (flag) {
        				request.getSession().setAttribute("RESULT",	"Purchase Order : " + PONO+ "  Reversed successfully!");
        				response.sendRedirect("jsp/InboundOrderMultipleReversal.jsp?action=View&PLANT="
        						+ PLANT +"&PONO="+PONO+"&result=sucess");
        			} else {
        				request.getSession().setAttribute("RESULTERROR","Failed to Reverse Purchase Order : "+ PONO);
        				response.sendRedirect("jsp/InboundOrderMultipleReversal.jsp?action=View&PLANT="
    							+ PLANT 
    							+"&PONO="+PONO
    							+ "&allChecked="
    							+allChecked
    							+"&result=resulterror");
        			}
        			
        			}
        			
        		 catch (Exception e) {
        			this.mLogger.exception(this.printLog, "", e);
        			request.getSession().setAttribute("CATCHERROR", e.getMessage());
        			System.out.print((String)request.getSession().getAttribute("CATCHERROR"));
        			response.sendRedirect("jsp/InboundOrderMultipleReversal.jsp?action=View&PLANT="
        							+ PLANT 
        							+"&PONO="+PONO
        							+"&allChecked="+allChecked
        							
        							+"&result=catchrerror");
        			throw e;
        		}
        		
        		return xmlStr;
       }
     //---End Added by Bruhan on March 28 2014, Description:To InboundOrderMultipleReversal

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
