package com.track.pda;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

//import sun.misc.BASE64Decoder;
//import sun.nio.cs.UTF_32;

import com.oreilly.servlet.Base64Decoder;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TOUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.service.ShopifyService;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
@MultipartConfig
    public class DoPickingServlet extends HttpServlet implements IMLogger {
/*	public class DoPickingServlet extends HttpServlet implements SingleThreadModel {*/
	private boolean printLog = MLoggerConstant.DoPickingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.DoPickingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 1619653645107940257L;

	private POUtil _POUtil = null;
	private DOUtil _DOUtil = null;
	private DoDetDAO _DoDetDAO = null;
	private TOUtil _TOUtil = null;
	private InvMstDAO _InvMstDAO = null;
	private ShipHisDAO _ShipHisDAO = null;
	private PlantMstDAO _PlantMstDAO = null;
	private String PLANT = "";

	private String xmlStr = "";
	private String action = "";

	StrUtils strUtils = new StrUtils();

	private static final String CONTENT_TYPE = "text/xml";
	static final int baseX = 25;
	  static final int baseY = 25;
	  private static final boolean DEBUG = true;
	  String sProjIDInd = "";
	   String isWr       ="";
	  String TaskNo     ="";
	  String  PartID    = "";
	  String PartTypeID = "";
	  String PartDesc   = "";
	  String  TransactQty = "";
	  String ChrgFlg      = "";
	  String AppQty       = "";
	  String TransID      = "";

	public void init() throws ServletException {
		_POUtil = new POUtil();
		_DOUtil = new DOUtil();
		_DoDetDAO = new DoDetDAO();
		_TOUtil = new TOUtil();
		_InvMstDAO = new InvMstDAO();
		_ShipHisDAO = new ShipHisDAO();
		_PlantMstDAO = new PlantMstDAO();
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
			_DOUtil.setmLogger(mLogger);
			_TOUtil.setmLogger(mLogger);
			_InvMstDAO.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_item_details(request, response);
			}

			else if (action.equalsIgnoreCase("load_item_location")) {
				xmlStr = "";
				xmlStr = load_item_location(request, response);
			}

			else if (action.equalsIgnoreCase("load_batch_qty")) {
				xmlStr = "";
				xmlStr = load_batch_qty(request, response);
			}

			else if (action.equalsIgnoreCase("load_do_details")) {
				xmlStr = "";
				xmlStr = load_do_Details(request, response);
			}

			// By Samatha
			else if (action.equalsIgnoreCase("load_outBoundOrders")) {
				xmlStr = "";
				xmlStr = load_outBoundOrders(request, response);
			}

			else if (action.equalsIgnoreCase("load_outBoundOrder_item_details")) {
				xmlStr = "";
				xmlStr = load_outBoundOrder_item_details(request, response);
			}

			else if (action.equalsIgnoreCase("load_lotdetails")) {
				xmlStr = "";
				xmlStr = load_details_for_lot(request, response);
			}

			else if (action.equalsIgnoreCase("load_outBoundOrder_sup_details")) {
				xmlStr = "";
				xmlStr = load_outBoundOrder_sup_details(request, response);
			}

			else if (action.equalsIgnoreCase("process_do_picking")) {
				xmlStr = "";
				xmlStr = process_doPicking(request, response);
			}
			else if (action.equalsIgnoreCase("process_do_picking_random_bypda")) {
				xmlStr = "";
				xmlStr = process_doPicking_random_bypda(request, response);
			}
			else if (action.equalsIgnoreCase("process_do_issue_random_bypda")) {
				xmlStr = "";
				xmlStr = process_doissue_random_bypda(request, response);
			}
				
			else if (action.equalsIgnoreCase("process_do_picking_random")) {
				xmlStr = "";
				xmlStr = process_doPicking_random(request, response);
			}

			else if (action.equalsIgnoreCase("process_SignaturePda")) {
				xmlStr = "";
				xmlStr = process_SignaturePda(request, response);
			}
			
			else if (action.equalsIgnoreCase("Pick Confirm")) {
				xmlStr = "";
				xmlStr = process_doPickiingByWMS(request, response);
			}
			else if (action.equalsIgnoreCase("MultiplePick Confirm")) {
				xmlStr = process_doMultiplePickiingByWMS(request, response);
			}
			else if (action.equalsIgnoreCase("Pick/Issue Confirm")) {
				xmlStr = "";
				xmlStr = process_doPickIssueConfirmByWMS(request, response);
			}
		    else if (action.equalsIgnoreCase("Pick/IssueForMobileShop")) {
		            xmlStr = "";
		            xmlStr = process_doPickIssueForMobileShopping(request, response);
		    }
		    
		    else if (action.equalsIgnoreCase("Pick/IssueForMobileShop_WOInvcheck")) {
		            xmlStr = "";
		            xmlStr = process_doPickIssueForMobileShoppingWOInvcheck(request, response);
		    }
		    else if (action.equalsIgnoreCase("Pick/IssueConfirmByRange")) {
		            xmlStr = process_doPickByRange(request, response);
		    }
			// By Shayanthan
			else if (action.equalsIgnoreCase("GET_BATCH_CODES")) {
				xmlStr = "";
				xmlStr = getBatchCode(request, response);
			}
			else if (action.equalsIgnoreCase("GET_BATCH_CODES_RANDOM")) {
				xmlStr = "";
				xmlStr = getBatchCodeRandom(request, response);
			}
			else if (action.equalsIgnoreCase("GET_BATCH_CODES_RANDOM_WITHOUTLOC")) {
				xmlStr = "";
				xmlStr = getBatchCodeRandomWithOutLoc(request, response);
			}
			else if (action.equalsIgnoreCase("GET_PICKING_ITEM")) {
				xmlStr = "";
				xmlStr = getPickingItem(request, response);
			}
			else if (action.equalsIgnoreCase("load_random_outBoundOrder_order_details")) {
				xmlStr = "";
				xmlStr = load_random_outBoundOrder_order_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_random_outBoundOrder_item_details")) {
				xmlStr = "";
				xmlStr = load_random_outBoundOrder_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_pick_item_details")) {
				xmlStr = "";
				xmlStr = load_pick_item_details(request, response);
			}
			
			else if (action.equalsIgnoreCase("load_random_outBoundOrder_product_details_scanby_product")) {
				xmlStr = "";
				xmlStr = load_random_outBoundOrder_product_details_scanby_product(request, response);
			}
			else if (action.equalsIgnoreCase("GET_BATCH_CODES_RANDOM_BYBATCH")) {
				xmlStr = "";
				xmlStr  = getBatchCodeByBatchRandom(request, response);
			}
			else if (action.equalsIgnoreCase("load_locations_with_batch")) {
				xmlStr = "";
				xmlStr = load_locations_with_batch(request, response);
			}
			else if (action.equalsIgnoreCase("check_locations_with_batch")) {
				xmlStr = "";
				xmlStr = check_locations_with_batch(request, response);
			}
				else if (action.equalsIgnoreCase("process_signature")) {
				xmlStr = "";
				xmlStr = process_signature(request, response);
			}
			else if (action.equalsIgnoreCase("get_signature_order_status")) {
				xmlStr = "";
				xmlStr = get_signature_order_status(request, response);
			}
			else if (action.equalsIgnoreCase("check_plant")) {
				xmlStr = "";
				xmlStr = check_plant(request, response);
			}	
			else if (action.equalsIgnoreCase("getSignatureCount")) {
					xmlStr = "";
					xmlStr = getSignatureCount(request, response);
				}	
			else if (action.equalsIgnoreCase("LoadLocDetailsByCreatedDate")) {
				xmlStr = "";
				xmlStr =load_ob_location_details_by_createddate(request, response);
			}	
			else if (action.equalsIgnoreCase("LoadLocDetailsByExpiryDate")) {
				xmlStr = "";
				xmlStr =load_ob_location_details_by_expirydate(request, response);
			}	
			else if (action.equalsIgnoreCase("LoadBatchDetailsByCreatedDate")) {
				xmlStr = "";
				xmlStr =load_ob_batch_details_by_createddate(request, response);
			}	
			else if (action.equalsIgnoreCase("LoadBatchDetailsByExpiryDate")) {
				xmlStr = "";
				xmlStr =load_ob_batch_details_by_expirydate(request, response);
			}	
			else if (action.equalsIgnoreCase("Load_OutboundOrdersByItem")) {
				xmlStr = "";
				xmlStr =load_OutboundOrdersByItem(request, response);
			}	
			
			else if (action.equalsIgnoreCase("load_outBoundPickOrders")) {
				xmlStr = "";
				xmlStr =load_outBoundPickOrders(request, response);
			}
			else if (action.equalsIgnoreCase("load_outBoundOrder_item_orderline_details")) {
				xmlStr = "";
				xmlStr =load_outBoundOrder_item_orderline_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_outBoundOrder_item_orderline_detailsnext")) {
				xmlStr = "";
				xmlStr =load_outBoundOrder_item_orderline_detailsnext(request, response);
			}
			else if (action.equalsIgnoreCase("load_outBoundOrder_item_ByItem")) {
				xmlStr = "";
				xmlStr = load_outBoundOrder_item_ByItem(request, response);
			}
			else if (action.equalsIgnoreCase("load_do_uom")) {
				xmlStr = "";
				xmlStr = load_do_uom(request, response);
			}
			else if (action.equals("load_do_invoice")) {
				xmlStr = "";
				xmlStr = generateINVOICE(request, response);	       	       
		    }
			else if (action.equals("load_do_gino")) {
				xmlStr = "";
				xmlStr = generateGINO(request, response);	       	       
		    }
			else if (action.equals("load_outBoundOrdersPDApopup")) {
				xmlStr = "";
				xmlStr = load_outBoundOrdersPDApopup(request, response);	       	       
		    }
			else if (action.equals("load_OutBoundOrdersPDAAutoSuggestion")) {
				xmlStr = "";
				xmlStr = load_OutBoundOrdersPDAAutoSuggestion(request, response);	       	       
		    }
			else if (action.equals("load_outBoundOrdersIssuePDApopup")) {
				xmlStr = "";
				xmlStr = load_outBoundOrdersIssuePDApopup(request, response);	       	       
		    }
			else if (action.equals("load_outBoundOrder_item_details_ForPDA_UOMPopup")) {
				xmlStr = "";
				xmlStr = load_outBoundOrder_item_details_ForPDA_UOMPopup(request, response);	       	       
		    }
			else if (action.equals("check_sales_order_details")) {
				xmlStr = "";
				xmlStr = check_sales_order_details(request, response);	       	       
		    }
			else if (action.equals("load_salesCheckOrdersPDApopup")) {
				xmlStr = "";
				xmlStr = load_salesCheckOrdersPDApopup(request, response);	       	       
		    }
			else if (action.equalsIgnoreCase("load_random_salescheckOrder_item_details")) {
				xmlStr = "";
				xmlStr = load_random_salescheckOrder_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_SalesCheckOrder_item_ByItem")) {
				xmlStr = "";
				xmlStr = load_SalesCheckOrder_item_ByItem(request, response);
			}
			else if (action.equalsIgnoreCase("load_SalesCheckOrder_item_validation")) {
				xmlStr = "";
				xmlStr = load_SalesCheckOrder_item_validation(request, response);
			}
			
			
			
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

	private String load_do_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("plant", PLANT);
			ht.put("item", aItemNum);

			str = _DOUtil.getDoDetails_picking(PLANT);

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
		String str = "", aPlant = "", aItem = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			String itemDesc = new ItemMstDAO().getItemDesc(aPlant, aItem);

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ItemDetails");

			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("item", aItem);
			xmlStr += XMLUtils.getXMLNode("itemDesc", itemDesc);

			xmlStr += XMLUtils.getEndNode("ItemDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	private String load_item_location(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ItemDetails");

			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("item", aItem);

			String extCond = " LOC NOT LIKE  'SHIPPINGAREA%'AND LOC NOT LIKE  'HOLDINGAREA%' ";

			xmlStr += XMLUtils.getXMLNode("itemLoc", _InvMstDAO.getItemLoc(
					"SIS", aItem, extCond));

			xmlStr += XMLUtils.getEndNode("ItemDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product Not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}

	private String load_batch_qty(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "", aBatch = "", aLoc = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aBatch = StrUtils.fString(request.getParameter("BATCH"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ItemDetails");

			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("item", aItem);

			String extCond = " qty > 0 ";
			xmlStr += XMLUtils.getXMLNode("qty", _InvMstDAO.getBatchQty(aPlant,
					aItem, aBatch, aLoc, extCond));

			xmlStr += XMLUtils.getEndNode("ItemDetails");
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product Not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	private String process_doPicking(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ORD_QTY = "", INV_QTY = "", PICKING_QTY = "",ISNONSTKFLG="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("DO_LN_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEM_DESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ORD_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORD_QTY")));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			 ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			double ordqty = Double.parseDouble(ORD_QTY);
			ordqty = StrUtils.RoundDB(ordqty, IConstants.DECIMALPTS);
			
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			
			PICKING_QTY = String.valueOf(pickingqty);
			ORD_QTY = String.valueOf(ordqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			ORD_QTY = StrUtils.formatThreeDecimal(ORD_QTY);
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));

			ITEM_DESCRIPTION = StrUtils.replaceCharacters2Recv(ITEM_DESCRIPTION);
			CUST_NAME = StrUtils.replaceCharacters2Recv(CUST_NAME);
                        
            UserLocUtil uslocUtil = new UserLocUtil();
            uslocUtil.setmLogger(mLogger);
           /* boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
            if (!isvalidlocforUser) {
                throw new Exception(" Loc :"+ITEM_LOC+" is not a User Assigned Location/Valid Location to Pick");
            }*/
            
            DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
			
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORD_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, "");
			receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			receiveMaterial_HM.put("INV_QTY", "1");
			receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
			receiveMaterial_HM.put(IConstants.ISSUEDATE, strIssueDate);
			receiveMaterial_HM.put("TYPE", "");
			
			xmlStr = "";

			boolean flag = true;

			if (flag) {
				xmlStr = _DOUtil.process_DoPickingForPDA(receiveMaterial_HM);
			} else {
				throw new Exception(" Picking is not successfull ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return xmlStr;
	}

	 /**************   Modification History *****************************
	 * Bruhan on July 23 2014, Description:To Include ISNONSTKFLG
	 * 
	 * 
	 */ 
	private String process_doPickiingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "";
		String CONTACT_NAME = "", TELNO = "", EMAIL = "", ADD1 = "", ADD2 = "", ADD3 = "", PICKED_QTY = "",ISNONSTKFLG="";
		try {
                    
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));

			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));

			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));

			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			PICKED_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("PICKEDQTY")));
			 ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("QTY")));
			
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));

			
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);

			List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,
					ITEM_NUM, ITEM_LOC, ITEM_BATCH);
			double invqty = 0;
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					ITEM_QTY = (String) m.get("qty");
					invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
				}
			} else {
				throw new Exception(
						"Error in picking OutBound Order : Invalid Batch Detail");
			}
			double orderqty = Double.parseDouble(((String) ORDER_QTY.trim()));			 
			double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
					.toString()));
			pickingQty = strUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			pickedqty = strUtils.RoundDB(pickedqty, IConstants.DECIMALPTS);
			orderqty = strUtils.RoundDB(orderqty, IConstants.DECIMALPTS);
			invqty = strUtils.RoundDB(invqty, IConstants.DECIMALPTS);
			double sumqty = pickingQty + pickedqty;
			sumqty = strUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			boolean qtyFlag = false;
			if (pickingQty > orderqty || pickingQty > invqty
					|| (sumqty) > orderqty) {
				qtyFlag = true;
				throw new Exception(
						"Error in picking OutBound Order : Picking Qty Should less than Order Qty!");
			}
			PICKING_QTY = String.valueOf(pickingQty);
		 PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"
					+ ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
					.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put("INV_QTY", "1");

			// GET JOBNUM
			receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,
					PO_NUM));

			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			boolean flag = false;
                        UserLocUtil uslocUtil = new UserLocUtil();
                        uslocUtil.setmLogger(mLogger);
                        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
                        if(!isvalidlocforUser){
                            throw new Exception(" Loc :"+ITEM_LOC+" is not User Assigned Location");
                        }
                       
			flag = _DOUtil.process_DoPickingForPC(receiveMaterial_HM);
			if (flag) {
				request.getSession().setAttribute("RESULT",
						"OutBound Order : " + PO_NUM + "  Picked successfully!");
				response
						.sendRedirect("jsp/OutBoundOrderSummary.jsp?action=View&PLANT="
								+ PLANT + "&DONO=" + PO_NUM);
			} else {

				request.getSession().setAttribute("RESULTERROR",
						"Failed to Pick OutBound Order : " + PO_NUM);
				response
						.sendRedirect("jsp/OutBoundOrderPicking.jsp?action=resulterror");
			}
                  

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,PO_NUM);
			request.getSession().setAttribute("customerlistqry2", listQry);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/OutBoundOrderPicking.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&ORDERNO="
							+ PO_NUM
							+ "&ORDERLNO="
							+ PO_LN_NUM
							+ "&CUSTNAME="
							+ strUtils.replaceCharacters2Send(CUST_NAME)
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&INVQTY="
							+ INV_QTY
							+ "&PICKEDQTY="
							+ PICKED_QTY
							+ "&PICKINGQTY=" + PICKING_QTY);
			// throw e;
		}
		// MLogger.log(0, "process_doPickiingByWMS() Ends");

		return xmlStr;
	}
	/* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To include Transaction date
	*/
	private String process_doMultiplePickiingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map issMat_HM = null;
		ItemMstDAO _ItemMstDAO=new ItemMstDAO();
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",INVOICENO="";
		String ITEM_BATCH = "", BATCH_ID = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "",ISNONSTKFLG="";
		String CONTACT_NAME = "", TELNO = "", EMAIL = "", ADD1 = "", ADD2 = "", ADD3 = "", PICKED_QTY = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",UOM="",UOMQTY="";
		String alertitem="",MINSTKQTY="";
		try {
                    
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String ISINVENTORYMINQTY = new DoHdrDAO().getISINVENTORYMINQTY(PLANT);//Thanzi
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			ISNONSTKFLG = StrUtils.fString(request.getParameter("ISNONSTKPRD"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			BATCH_ID = StrUtils.fString(request.getParameter("BATCH_ID"));
			INVOICENO = StrUtils.fString("" + session.getAttribute("INVOICENO"));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));

			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));

			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			PICKED_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("PICKEDQTY")));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			UOMQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOMQTY")));
			UOM = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOM")));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));
				TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			 String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_PICKING_SIZE"));
			int DYNAMIC_RECEIVING_SIZE_INT = Integer.parseInt(DYNAMIC_RECEIVING_SIZE);
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			issMat_HM = new HashMap();
			boolean flag = false;
			for (int indexFlow = 0; indexFlow < DYNAMIC_RECEIVING_SIZE_INT; indexFlow++) {
				ITEM_LOC=StrUtils.fString(request.getParameter("LOC"+"_"+indexFlow));
				ITEM_BATCH=StrUtils.fString(request.getParameter("BATCH"+"_"+indexFlow));
				BATCH_ID =StrUtils.fString(request.getParameter("BATCH_ID"+"_"+indexFlow));
                PICKING_QTY=StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY"+"_"+indexFlow)));
				ITEM_EXPDATE=StrUtils.fString(request.getParameter("REMARKS"+"_"+indexFlow));
				if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
					List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM_NUM, ITEM_LOC, ITEM_BATCH);
					double invqty = 0;
					double Detuctqty = 0;
					double pickingQty = 0;
					double STKQTY = 0;
					if (listQry.size() > 0) {
						for (int i = 0; i < listQry.size(); i++) {
							Map m = (Map) listQry.get(i);
							ITEM_QTY = (String) m.get("qty");
							MINSTKQTY = (String) m.get("MINSTKQTY");
							invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
							STKQTY = Double.parseDouble(((String) MINSTKQTY.trim()));
						}
						pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
						Detuctqty = invqty-pickingQty;
						if(STKQTY!=0) {
						if(STKQTY>Detuctqty) {
							if(alertitem.equalsIgnoreCase("")) {
								alertitem =ITEM_NUM;
							}else {
								alertitem = alertitem+" , "+ITEM_NUM;
							}
						}
						}
						
						if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
							alertitem = alertitem;
						else 
							alertitem="";
						
						double curqty = Double.parseDouble(PICKING_QTY) * Double.valueOf(UOMQTY);
						if(invqty < curqty){
							throw new Exception(
									"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+PO_LN_NUM+" in the location selected");
							}
					} else {
						throw new Exception(
								"Error in picking Sales Order : Invalid Batch Detail");
					}
				}
				double orderqty = Double.parseDouble(((String) ORDER_QTY.trim()));			 
				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
						.toString()));
				pickingQty = strUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
				boolean qtyFlag = false;
			PICKING_QTY = String.valueOf(pickingQty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM_NUM));
			issMat_HM.put(IConstants.PODET_PONUM, PO_NUM);
			issMat_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.BATCH_ID, BATCH_ID);
			issMat_HM.put(IConstants.QTY, PICKING_QTY);
			issMat_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			issMat_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
	    	issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
			issMat_HM.put("TYPE", "");
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);
			issMat_HM.put(IConstants.UOM, UOM);
			issMat_HM.put("UOMQTY", UOMQTY);
			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", issMat_HM.get(IConstants.PLANT));
			htLocMst.put("item", issMat_HM.get(IConstants.ITEM));
			htLocMst.put("loc", issMat_HM.get(IConstants.LOC));

			
                        UserLocUtil uslocUtil = new UserLocUtil();
                        uslocUtil.setmLogger(mLogger);
                        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
                        if(!isvalidlocforUser){
                            throw new Exception(" Loc :"+ITEM_LOC+" is not User Assigned Location");
                        }
                       
			flag = _DOUtil.process_DoPickingForPC(issMat_HM);
			
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
								String result="";
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
			}
			if (flag) {
				if(alertitem.equalsIgnoreCase("")) {
				request.getSession().setAttribute("RESULT",
						"Sales Order : " +PO_NUM + "  Picked successfully!");
				}else {
					request.getSession().setAttribute("RESULT",
							"Sales Order : " +PO_NUM + "  Picked successfully! <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>");
				}
				response
						.sendRedirect("../salestransaction/orderpicksummary?action=View&PLANT="
								+ PLANT + "&DONO=" + PO_NUM);
			} else {

				request.getSession().setAttribute("RESULTERROR",
						"Failed to Pick Sales Order : " + PO_NUM);
				response
						.sendRedirect("../salestransaction/orderpick?action=resulterror");
			}
                  

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,PO_NUM);
			request.getSession().setAttribute("customerlistqry2", listQry);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("../salestransaction/orderpick?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&ORDERNO="
							+ PO_NUM
							+ "&ORDERLNO="
							+ PO_LN_NUM
							+ "&CUSTNAME="
							+ strUtils.replaceCharacters2Send(CUST_NAME)
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&INVQTY="
							+ INV_QTY
							+ "&PICKEDQTY="
							+ PICKED_QTY
							+ "&PICKINGQTY=" + PICKING_QTY+ "&UOM=" + UOM+ "&UOMQTY=" + UOMQTY);
			// throw e;
		}
		// MLogger.log(0, "process_doPickiingByWMS() Ends");

		return xmlStr;
	}
	
	/* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To include Transaction date
	*/
	private String process_doPickIssueConfirmByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map issMat_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "",ISNONSTKFLG="";
		String CONTACT_NAME = "", TELNO = "", EMAIL = "", ADD1 = "", ADD2 = "", ADD3 = "", PICKED_QTY = "", TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";;
		
		try {
             
			HttpSession session = request.getSession();
            PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			ISNONSTKFLG = StrUtils.fString(request.getParameter("ISNONSTKPRD"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			PICKED_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("PICKEDQTY")));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));
				TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
				strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_PICKING_SIZE"));
			int DYNAMIC_RECEIVING_SIZE_INT = Integer.parseInt(DYNAMIC_RECEIVING_SIZE);
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			issMat_HM = new HashMap();
			boolean flag = false;
			for (int indexFlow = 0; indexFlow < DYNAMIC_RECEIVING_SIZE_INT; indexFlow++) {
				ITEM_LOC=StrUtils.fString(request.getParameter("LOC"+"_"+indexFlow));
				ITEM_BATCH=StrUtils.fString(request.getParameter("BATCH"+"_"+indexFlow));
                PICKING_QTY=StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY"+"_"+indexFlow)));
				ITEM_EXPDATE=StrUtils.fString(request.getParameter("REMARKS"+"_"+indexFlow));
				if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
				List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM_NUM, ITEM_LOC, ITEM_BATCH);
				double invqty = 0;
				if (listQry.size() > 0) {
					for (int i = 0; i < listQry.size(); i++) {
						Map m = (Map) listQry.get(i);
						ITEM_QTY = (String) m.get("qty");
						invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
					}
				} else {
					throw new Exception("Error in picking OutBound Order : Invalid Batch Detail");
				}
				}
				double orderqty = Double.parseDouble(((String) ORDER_QTY.trim()));			 
				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
						.toString()));
				pickingQty = strUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
				boolean qtyFlag = false;
				PICKING_QTY = String.valueOf(pickingQty);
				PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  

			 											
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			//issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM_NUM));
			issMat_HM.put(IConstants.PODET_PONUM, PO_NUM);
			issMat_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.QTY, PICKING_QTY);
			issMat_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			issMat_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
			issMat_HM.put("TYPE", "");

			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", issMat_HM.get(IConstants.PLANT));
			htLocMst.put("item", issMat_HM.get(IConstants.ITEM));
			htLocMst.put("loc", issMat_HM.get(IConstants.LOC));

			
                        UserLocUtil uslocUtil = new UserLocUtil();
                        uslocUtil.setmLogger(mLogger);
                        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
                        if(!isvalidlocforUser){
                            throw new Exception(" Loc :"+ITEM_LOC+" is not User Assigned Location");
                        }
                       
			flag = _DOUtil.process_DoPickingForPC(issMat_HM);
			}
			if (flag) {
				request.getSession().setAttribute("RESULT",
						"OutBound Order : " +PO_NUM + "  Picked successfully!");
				response
						.sendRedirect("jsp/DOSummaryForSingleStepPickIssue.jsp?action=View&PLANT="
								+ PLANT + "&DONO=" + PO_NUM);
			} else {

				request.getSession().setAttribute("RESULTERROR",
						"Failed to Pick OutBound Order : " + PO_NUM);
				response
						.sendRedirect("jsp/DOSummaryForSingleStepPickIssue.jsp?action=resulterror");
			}
                  

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,PO_NUM);
			request.getSession().setAttribute("customerlistqry2", listQry);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/DOSummaryForSingleStepPickIssue.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&ORDERNO="
							+ PO_NUM
							+ "&ORDERLNO="
							+ PO_LN_NUM
							+ "&CUSTNAME="
							+ strUtils.replaceCharacters2Send(CUST_NAME)
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&INVQTY="
							+ INV_QTY
							+ "&PICKEDQTY="
							+ PICKED_QTY
							+ "&PICKINGQTY=" + PICKING_QTY);
			// throw e;
		}
		// MLogger.log(0, "process_doPickiingByWMS() Ends");

		return xmlStr;
	}
        
    private String process_doPickIssueForMobileShopping(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {

            Map receiveMaterial_HM = null;
            String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
            String ITEM_DESCRIPTION = "", LOGIN_USER = "";
            String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "";
            String CONTACT_NAME = "", TELNO = "", EMAIL = "", ADD1 = "", ADD2 = "", ADD3 = "", PICKED_QTY = "";
            try {
                
                    HttpSession session = request.getSession();
                    
                 
                    PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
                                    .trim();
                    PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
                    PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
                    CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
                    ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
                    ITEM_DESCRIPTION = StrUtils.fString(request
                                    .getParameter("ITEMDESC"));
                    LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
                    ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));

                    ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));

                    ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));

                    ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
                    PICKED_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("PICKEDQTY")));
                    INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
                    ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
                    
                    PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));
                    String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_PICKING_SIZE"));
                    int DYNAMIC_RECEIVING_SIZE_INT = Integer.parseInt(DYNAMIC_RECEIVING_SIZE);
                    
                    InvMstDAO invMstDAO = new InvMstDAO();
                    invMstDAO.setmLogger(mLogger);


                    receiveMaterial_HM = new HashMap();
                    boolean flag = false;
                    for (int indexFlow = 0; indexFlow < DYNAMIC_RECEIVING_SIZE_INT; indexFlow++) {
                            ITEM_LOC=StrUtils.fString(request.getParameter("LOC"+"_"+indexFlow));
                            ITEM_BATCH=StrUtils.fString(request.getParameter("BATCH"+"_"+indexFlow));
                            PICKING_QTY=StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY"+"_"+indexFlow)));
                            ITEM_EXPDATE=StrUtils.fString(request.getParameter("REMARKS"+"_"+indexFlow));
                    
                            List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEM_NUM, ITEM_LOC, ITEM_BATCH);
                            double invqty = 0;
                            if (listQry.size() > 0) {
                                    for (int i = 0; i < listQry.size(); i++) {
                                            Map m = (Map) listQry.get(i);
                                            ITEM_QTY = (String) m.get("qty");
                                            invqty = Double.parseDouble(((String) ITEM_QTY.trim()));
                                    }
                            } else {
                                    throw new Exception(
                                                    "Error in picking OutBound Order : Invalid Batch Detail");
                            }
                            double orderqty = Double.parseDouble(((String) ORDER_QTY.trim()));                       
                            double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
                            double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
                                            .toString()));
                            pickingQty = strUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
                            boolean qtyFlag = false;
                    PICKING_QTY = String.valueOf(pickingQty);
                    PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
                    receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                    receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
                    receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
                    receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
                    receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
                    receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                    receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
    
                    receiveMaterial_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"
                                    + ITEM_LOC);
                    receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                    receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
                                    .getCustCode(PLANT, PO_NUM));
                    receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                    receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
                    receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
                    receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);

                    // GET JOBNUM
                    receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,
                                    PO_NUM));
                    receiveMaterial_HM.put("INV_QTY", "1");

                    xmlStr = "";

                    // check for item in location
                    Hashtable htLocMst = new Hashtable();
                    htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
                    htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
                    htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

                    
                    UserLocUtil uslocUtil = new UserLocUtil();
                    uslocUtil.setmLogger(mLogger);
                    boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
                    if(!isvalidlocforUser){
                        throw new Exception(" Loc :"+ITEM_LOC+" is not User Assigned Location");
                    }
                   
                    flag = _DOUtil.process_DoPickingForPC(receiveMaterial_HM);
                    }
                    if (flag) {
                            request.getSession().setAttribute("RESULT",
                                            "Mobile Order : " +PO_NUM + "  Picked successfully!");
                            response
                                            .sendRedirect("jsp/mobileShoppingFullfillment.jsp?action=View&PLANT="
                                                            + PLANT + "&DONO=" + PO_NUM);
                    } else {

                            request.getSession().setAttribute("RESULTERROR",
                                            "Failed to Pick Mobile Order : " + PO_NUM);
                            response
                                            .sendRedirect("jsp/mobileShoppingFullfillment.jsp?action=resulterror");
                    }
              

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    DoDetDAO _DoDetDAO = new DoDetDAO();
                    _DoDetDAO.setmLogger(mLogger);
                    List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,PO_NUM);
                    request.getSession().setAttribute("customerlistqry2", listQry);
                    request.getSession().setAttribute("QTYERROR", e.getMessage());
                    response.sendRedirect("jsp/mobileShoppingFullfillment.jsp?action=qtyerror&ITEMNO="
                                                    + ITEM_NUM
                                                    + "&ITEMDESC="
                                                    + strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
                                                    + "&LOC="
                                                    + ITEM_LOC
                                                    + "&ORDERNO="
                                                    + PO_NUM
                                                    + "&ORDERLNO="
                                                    + PO_LN_NUM
                                                    + "&CUSTNAME="
                                                    + strUtils.replaceCharacters2Send(CUST_NAME)
                                                    + "&BATCH="
                                                    + ITEM_BATCH
                                                    + "&REF="
                                                    + ITEM_EXPDATE
                                                    + "&ORDERQTY="
                                                    + ORDER_QTY
                                                    + "&QTY="
                                                    + ITEM_QTY
                                                    + "&INVQTY="
                                                    + INV_QTY
                                                    + "&PICKEDQTY="
                                                    + PICKED_QTY
                                                    + "&PICKINGQTY=" + PICKING_QTY);
                    // throw e;
            }
            // MLogger.log(0, "process_doPickiingByWMS() Ends");

            return xmlStr;
    }
        
     
     private String process_doPickIssueForMobileShoppingWOInvcheck(HttpServletRequest request,
                     HttpServletResponse response) throws IOException, ServletException,
                     Exception {

             Map receiveMaterial_HM = null;
             String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
             String ITEM_DESCRIPTION = "", LOGIN_USER = "";
             String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "";
             String PICKED_QTY = "";
             try {
                 
                     HttpSession session = request.getSession();
                     
                  
                     PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
                                     .trim();
                     PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
                     PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
                     CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
                     ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
                     ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
                     LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
                     ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));

                     ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));

                     ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));

                     ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
                     PICKED_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("PICKEDQTY")));
                     INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
                     ITEM_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("QTY")));
                     
                     PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));

                     
                 
                     double orderqty = Double.parseDouble(((String) ORDER_QTY.trim()));                       
                     double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
                     double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim()
                                     .toString()));
                     pickingQty = strUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
                     pickedqty = strUtils.RoundDB(pickedqty, IConstants.DECIMALPTS);
                     orderqty = strUtils.RoundDB(orderqty, IConstants.DECIMALPTS);
                  
                     double sumqty = pickingQty + pickedqty;
                     sumqty = strUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
                     boolean qtyFlag = false;
                     if (pickingQty > orderqty || (sumqty) > orderqty) {
                             qtyFlag = true;
                             throw new Exception(
                                             "Error in picking Mobile Order : Picking Qty Should less than Order Qty!");
                     }
                     PICKING_QTY = String.valueOf(pickingQty);
                     PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
                     receiveMaterial_HM = new HashMap();
                     receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                     receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
                     receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
                     receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
                     receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
                     receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                     receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
                     receiveMaterial_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"
                                     + ITEM_LOC);
                     receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                     receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
                                     .getCustCode(PLANT, PO_NUM));
                     receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                     receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
                     receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
                     receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
                     receiveMaterial_HM.put("INV_QTY", "1");

                     // GET JOBNUM
                     receiveMaterial_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,
                                     PO_NUM));

                     xmlStr = "";

       

                     boolean flag = false;
                     
                     flag = _DOUtil.process_DoPickingWOInvCheck(receiveMaterial_HM);
                  
                     if (flag) {
                             request.getSession().setAttribute("RESULT",
                                             "Mobile Shopping Order : " +PO_NUM + "  Picked successfully!");
                             response
                                             .sendRedirect("jsp/mobileShopWOInv.jsp?action=View&PLANT="
                                                             + PLANT + "&DONO=" + PO_NUM);
                     } else {
     
                             request.getSession().setAttribute("RESULTERROR",
                                             "Failed to Pick Mobile Shopping Order : " + PO_NUM);
                             response
                                             .sendRedirect("jsp/mobileShopWOInv.jsp?action=resulterror");
                     }
               

             } catch (Exception e) {
                     this.mLogger.exception(this.printLog, "", e);
                     DoDetDAO _DoDetDAO = new DoDetDAO();
                     _DoDetDAO.setmLogger(mLogger);
                     List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,PO_NUM);
                     request.getSession().setAttribute("customerlistqry2", listQry);
                     request.getSession().setAttribute("QTYERROR", e.getMessage());
                     response.sendRedirect("jsp/mobileSingleStepWOInvcheck.jsp?action=qtyerror&ITEMNO="
                                                     + ITEM_NUM
                                                     + "&ITEMDESC="
                                                     + strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
                                                     + "&LOC="
                                                     + ITEM_LOC
                                                     + "&ORDERNO="
                                                     + PO_NUM
                                                     + "&ORDERLNO="
                                                     + PO_LN_NUM
                                                     + "&CUSTNAME="
                                                     + strUtils.replaceCharacters2Send(CUST_NAME)
                                                     + "&BATCH="
                                                     + ITEM_BATCH
                                                     + "&REF="
                                                     + ITEM_EXPDATE
                                                     + "&ORDERQTY="
                                                     + ORDER_QTY
                                                     + "&QTY="
                                                     + ITEM_QTY
                                                     + "&INVQTY="
                                                     + INV_QTY
                                                     + "&PICKEDQTY="
                                                     + PICKED_QTY
                                                     + "&PICKINGQTY=" + PICKING_QTY);
                     // throw e;
             }
             // MLogger.log(0, "process_doPickiingByWMS() Ends");

             return xmlStr;
     }
        
   
 /* ************Modification History*********************************
	   Oct 14 2014 Bruhan, Description: To include Transaction date
	 */
    private String process_doPickByRange(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {
            UserLocUtil uslocUtil = new UserLocUtil();
            uslocUtil.setmLogger(mLogger);
            Map issMat_HM = null;
            ItemMstDAO _ItemMstDAO = new ItemMstDAO();
            String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "",INVOICENO="";
            String ITEM_DESCRIPTION = "", LOGIN_USER = "";
            String ITEM_BATCH = "", BATCH_ID = "-1", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "",ISNONSTKFLG="";
            String  PICKED_QTY = "",SRANGE="",ERANGE="",SUFFIX="",DTFRMT="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",UOM="",UOMQTY="";
            String alertitem="",MINSTKQTY="";
            //UserTransaction ut = null;
            try {
                
                    HttpSession session = request.getSession();
                    PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
                    String ISINVENTORYMINQTY = new DoHdrDAO().getISINVENTORYMINQTY(PLANT);//Thanzi
                    INVOICENO=StrUtils.fString((String) session.getAttribute("INVOICENO"));
                    PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
                    PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
                    CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
                    ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
                    ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
                    LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
                    ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
                    ITEM_EXPDATE = StrUtils.fString(request.getParameter("REMARKS"));
                    ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
                    ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
        			PICKED_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("PICKEDQTY")));
                    INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
                    ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
                    PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("SERIALQTY")));
                    SRANGE = StrUtils.fString(request.getParameter("SRANGE"));
                    ERANGE = StrUtils.fString(request.getParameter("ERANGE"));
                    SUFFIX = StrUtils.fString(request.getParameter("SUFFIX"));
                    DTFRMT = StrUtils.fString(request.getParameter("DTFRMT"));
					TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
        			if (TRANSACTIONDATE.length()>5)
        				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
        			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
                    long rangeSize = Long.parseLong(ERANGE)-Long.parseLong(SRANGE);
                    rangeSize = rangeSize+1;
                    ITEM_QTY = StrUtils.fString(request.getParameter("SERIALQTY"));
                    UOMQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOMQTY")));
        			UOM = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOM")));
                     ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
                    InvMstDAO invMstDAO = new InvMstDAO();
                    invMstDAO.setmLogger(mLogger);
                    double pickingqty = Double.parseDouble(PICKING_QTY);
                    pickingqty = strUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
                    PICKING_QTY = String.valueOf(pickingqty);
                    PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
                    boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
                    if(!isvalidlocforUser){
                        throw new Exception(" Picking Loc :"+ITEM_LOC+" is not User Assigned Location");
                    }
                    boolean flag = false;
                    //Boolean transactionHandler = true;
                    //ut = DbBean.getUserTranaction();
                    //ut.begin();
                
                        long SerialNo = 0;
                        for(int cnt=0;cnt<rangeSize;cnt++){
                        SerialNo= Long.parseLong(SRANGE)+cnt;
                        ITEM_BATCH =SUFFIX+DTFRMT+Long.toString(SerialNo);
                
                        issMat_HM = new HashMap();
                       
            
                    	if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
                            List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,
                                            ITEM_NUM, ITEM_LOC, ITEM_BATCH);
                        double invqty = 0;
                        double Detuctqty = 0;
    					double STKQTY = 0;
    					double pickingQty = 0;
                            if (listQry.size() > 0) {
                                    for (int i = 0; i < listQry.size(); i++) {
                                            Map m = (Map) listQry.get(i);
                                            ITEM_QTY = (String) m.get("qty");
                                            MINSTKQTY = (String) m.get("MINSTKQTY");
                                            invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
                                            STKQTY = Double.parseDouble(((String) MINSTKQTY.trim()));
                                    }
            						pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
            						Detuctqty = invqty-pickingQty;
            						if(STKQTY!=0) {
            						if(STKQTY>Detuctqty) {
            							if(alertitem.equalsIgnoreCase("")) {
            								alertitem =ITEM_NUM;
            							}else {
            								alertitem = alertitem+" , "+ITEM_NUM;
            							}
            						}
            						}
            						
            						if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
    									alertitem = alertitem;
    								else 
    									alertitem="";
            						
        							if(invqty < Double.parseDouble(PICKING_QTY)){
        								throw new Exception(
        										"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+PO_LN_NUM+" in the location selected");
        								}
                            } else {
                                    throw new Exception( "Error in picking Sales Order : Invalid range Batch Details ");
                            }
                    	}         
                    issMat_HM.put(IConstants.PLANT, PLANT);
                    issMat_HM.put(IConstants.ITEM, ITEM_NUM);
                    issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM_NUM));
                    issMat_HM.put(IConstants.PODET_PONUM, PO_NUM);
                    issMat_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
                    issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                    issMat_HM.put(IConstants.LOC, ITEM_LOC);
                    issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_" + ITEM_LOC);
                    issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                    issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
                    issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
                    issMat_HM.put(IConstants.INVID, BATCH_ID);
                    issMat_HM.put(IConstants.QTY, PICKING_QTY);
                    issMat_HM.put(IConstants.ORD_QTY, ORDER_QTY);
                    issMat_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
                    issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
                    issMat_HM.put("INV_QTY", "1");
                    issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
                    issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
                    issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
					issMat_HM.put("TYPE", "");
					issMat_HM.put(IConstants.INVOICENO, INVOICENO);
					issMat_HM.put(IConstants.UOM, UOM);
					issMat_HM.put("UOMQTY", UOMQTY);
                    xmlStr = "";
                    flag = _DOUtil.process_DoPickingByRange(issMat_HM);
                    
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
    								String result="";
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
                    }
                   if (flag) {
                            //DbBean.CommitTran(ut);
                	   if(alertitem.equalsIgnoreCase("")) {
                		   request.getSession().setAttribute("RESULT", "Sales Order : " + PO_NUM+ "  Picked successfully!");
   					}else {
   						request.getSession().setAttribute("RESULT", "Sales Order : " + PO_NUM+ "  Picked successfully! <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>");
   					}
                    response.sendRedirect("../salestransaction/orderpickserial?action=View&PLANT=" + PLANT + "&DONO=" + PO_NUM);
                    } else {
                            request.getSession().setAttribute("RESULTERROR","Failed to Pick Sales Order : " + PO_NUM);
                            response.sendRedirect("../salestransaction/orderpickrange?action=resulterror");
                    }
              

            } catch (Exception e) {
                    //DbBean.RollbackTran(ut);
                    this.mLogger.exception(this.printLog, "", e);
                    DoDetDAO _DoDetDAO = new DoDetDAO();
                    _DoDetDAO.setmLogger(mLogger);
                    List listQry = _DoDetDAO.getOutBoundOrderIssueDetailsByWMS(PLANT,PO_NUM);
                    request.getSession().setAttribute("customerlistqry2", listQry);
                    request.getSession().setAttribute("QTYERROR", e.getMessage());
                    response
                                    .sendRedirect("../salestransaction/orderpickrange?action=qtyerror&ITEMNO="
                                                    + ITEM_NUM
                                                    + "&ITEMDESC="
                                                    + strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
                                                    + "&LOC="
                                                    + ITEM_LOC
                                                    + "&ORDERNO="
                                                    + PO_NUM
                                                    + "&ORDERLNO="
                                                    + PO_LN_NUM
                                                    + "&CUSTNAME="
                                                    + strUtils.replaceCharacters2Send(CUST_NAME)
                                                    + "&BATCH="
                                                    + ITEM_BATCH
                                                    + "&REMARKS="
                                                    + ITEM_EXPDATE
                                                    + "&ORDERQTY="
                                                    + ORDER_QTY
                                                    + "&QTY="
                                                    + ITEM_QTY
                                                    + "&INVQTY="
                                                    + INV_QTY
                                                    + "&PICKEDQTY="
                                                    + PICKED_QTY
                                                    + "&SERIALQTY=" + PICKING_QTY+ "&SRANGE=" + SRANGE+ "&ERANGE=" + ERANGE+ "&UOM=" + UOM+ "&UOMQTY=" + UOMQTY);
                   
            }
          

            return xmlStr;
    }
	private String load_outBoundOrders(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrders() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);

			str = _DOUtil.getOpenOutBoundOrder(PLANT);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		this.mLogger.info(this.printInfo, str);

		return str;
	}

	private String load_outBoundPickOrders(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrders() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);

			str = _DOUtil.getOpenOutPickBoundOrder(PLANT);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		this.mLogger.info(this.printInfo, str);

		return str;
	}


	private String load_outBoundOrder_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = _DOUtil.getOutBoundOrderItemDetails(PLANT, aOrdNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		
		return str;
	}

	private String load_details_for_lot(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "", aBatch = "";
		try {
			InvMstUtil _InvMstUtil = new InvMstUtil();
			_InvMstUtil.setmLogger(mLogger);
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM"));
			aBatch = StrUtils.fString(request.getParameter("BATCH"));

			str = _InvMstUtil.load_inv_details_for_Lot_xml(aPlant, aItem,
					aBatch);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Not a valid batch ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		
		return str;
	}

	private String load_outBoundOrder_sup_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("DO_NUM"));

			str = _DOUtil.getOpenoutBoundOrderSupDetails(PLANT, aOrdNo);
			if (str.equals("")) {
				str = XMLUtils.getXMLMessage(1, "No Orders Found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String getPickingItem(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String dono = StrUtils.fString(request.getParameter("DONO"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
		    String plant = StrUtils.fString(request.getParameter("PLANT"));
		    String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			InvMstUtil itM = new InvMstUtil();
			xmlStr = _DOUtil.getPickingItemDetails(plant, dono,itemNo,userID);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String load_random_outBoundOrder_order_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = _DOUtil.getRandomOutBoundOrderOrderDetails(PLANT, aOrdNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found.");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

		return str;
	}
	
	private String load_random_outBoundOrder_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = _DOUtil.getRandomOutBoundOrderItemDetails(PLANT, aOrdNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found.");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

		return str;
	}
	
	private String load_pick_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = _DOUtil.getRandomOutBoundOrderIssueItemDetails(PLANT, aOrdNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found.");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

		return str;
	}

	
	private String load_random_outBoundOrder_product_details_scanby_product(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNo = StrUtils.fString(request.getParameter("ITEMNO"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("ITEM", aItemNo);

			str = _DOUtil.getRandomOutBoundOrderProductctDetailsScanByProduct(PLANT, aItemNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found.");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

		return str;
	}
	
	private String load_outBoundOrder_item_orderline_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "", aItem="",uom="";
		try {
			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
			uom = StrUtils.fString(request.getParameter("UOM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);
			ht.put("ITEM", aItem);
			ht.put("UNITMO", uom);

			str = _DOUtil.getoutBoundOrder_item_orderline_details(PLANT, aOrdNo,aItem,uom,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found.");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

		return str;
	}
	private String load_outBoundOrder_item_orderline_detailsnext(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "", aItem="",start="",end="";
		try {
		
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);
			ht.put("ITEM", aItem);
			ht.put("START", start);
			ht.put("END", end);

			str = _DOUtil.getoutBoundOrder_item_orderline_detailsnext(PLANT, aOrdNo,aItem,start,end,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found.");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	private String load_outBoundOrder_item_ByItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "", aItem="",start="",end="";
		try {
		
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);
			ht.put("ITEM", aItem);
			ht.put("START", start);
			ht.put("END", end);

			str = _DOUtil.getoutBoundOrder_item_Byitem(PLANT, aOrdNo,aItem,start,end,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found.");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}

	private String getBatchCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		    String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			InvMstUtil itM = new InvMstUtil();
			itM.setmLogger(mLogger);
			xmlStr = itM.getBatchDetailWithLocation(plant, itemNo,userID);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String getBatchCodeRandom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		    String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			InvMstUtil itM = new InvMstUtil();
			itM.setmLogger(mLogger);
			xmlStr = itM.getBatchDetailWithLocationRandom(plant, itemNo,batch,userID);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	
	private String getBatchCodeRandomWithOutLoc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		    String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			InvMstUtil itM = new InvMstUtil();
			itM.setmLogger(mLogger);
			xmlStr = itM.getBatchDetailWithOutLocation(plant, itemNo,batch,userID);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	/*******   Modification History  *************************************
	 * Bruhan,July 23, To include non stocks
	 *
	 * 
	 */ 
	private String process_doPicking_random(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
        out.print(json_str);
      
        JSONArray hostArray = JSONArray.fromObject(json_str);
        
		Map issMat_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",CONTAINER="",REMARK;
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ORD_QTY = "", INV_QTY = "", 
				PICKING_QTY = "",ISNONSTKFLG="",INVID="",INVOICENO="",EMPNO="",UOM="",creditLimit="",creditBy="";
		for(int i = 0; i < hostArray.size(); i++)
        {
			JSONObject hostObject = hostArray.getJSONObject(i);
			PLANT = hostObject.getString("companyCode");
			PO_NUM = hostObject.getString("dono");
			ITEM_NUM = hostObject.getString("item");
			ITEM_DESCRIPTION = new ItemMstDAO().getItemDesc(PLANT,ITEM_NUM);
			LOGIN_USER = hostObject.getString("loginUser");
			CUST_NAME = hostObject.getString("customer");
			ITEM_BATCH = hostObject.getString("batchno");
			PICKING_QTY = hostObject.getString("qty");
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
		//	double pickingqty = Double.parseDouble(PICKING_QTY);
			/*pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);*/
			ITEM_LOC = hostObject.getString("loc");
			CONTAINER= hostObject.getString("container");
			CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
			INVOICENO=hostObject.getString("invoice");
			EMPNO=hostObject.getString("employeeno");
			UOM=hostObject.getString("uom");
			REMARK=hostObject.getString("remarks");
        
		try {
/*			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = new ItemMstDAO().getItemDesc(PLANT,ITEM_NUM);
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			CONTAINER= StrUtils.fString(request.getParameter("CONTAINER"));
			CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
			REMARK=StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			REMARK= StrUtils.replaceCharacters2Recv(REMARK);
			INVID=StrUtils.fString(request.getParameter("INVID"));
			INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));*/
			
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           /* UserLocUtil uslocUtil = new UserLocUtil();
            uslocUtil.setmLogger(mLogger);
            boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
            if (!isvalidlocforUser) {
                          throw new Exception(" Loc :"+ITEM_LOC+" is not a User Assigned Location/Valid Location to Pick");
             }*/
            
            DateUtils _dateUtils = new DateUtils();
            String curDate =_dateUtils.getDate();
            String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
            String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
            issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			issMat_HM.put(IConstants.PODET_PONUM, PO_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.QTY, PICKING_QTY);
			issMat_HM.put("TRAN_QTY", PICKING_QTY);
			issMat_HM.put(IConstants.INV_EXP_DATE, "");
			issMat_HM.put(IConstants.OUT_CONTAINER, CONTAINER);
			issMat_HM.put(IConstants.OUT_REMARK1, REMARK);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			//issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.INVID, INVID);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);
			issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put(IConstants.BATCH_ID, INVID);
			issMat_HM.put(IConstants.UNITMO, UOM);
			
			CustUtil custUtil = new CustUtil();
			ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, PO_NUM),PLANT);
			creditLimit   = (String)arrCust.get(24);
			creditBy   = (String)arrCust.get(35);
			
			issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
			issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
			issMat_HM.put(IConstants.BASE_CURRENCY, _PlantMstDAO.getBaseCurrency(PLANT));
			
			xmlStr = "";

			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, ITEM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, ITEM_BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			double invumqty = Double.valueOf(PICKING_QTY) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;
			if(!ISNONSTKFLG.equalsIgnoreCase("Y"))
			{
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					throw new Exception(" Not Enough Inventory For Product ");
				}
			}
			if (flag) {
				flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_SHIPHIS where DONO='"+PO_NUM+"' and INVOICENO='"+INVOICENO+"'");
				xmlStr = _DOUtil.process_DoPickingForPDA_Random(issMat_HM);
				//To GI
				if(xmlStr.contains("picked successfully"))
				{
					if (!flag) {
						new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
						}
					DoDetDAO _DoDetDAO = new DoDetDAO();
					DateUtils dateutils = new DateUtils();
					Hashtable htRecvDet = new Hashtable();
					
					double unitprice=0,totalprice=0,totalqty=0;
					String priceval="";
					Hashtable htDoDet = new Hashtable();
					ArrayList DODetails = null;
					String queryDoDet = "(UNITPRICE*CURRENCYUSEQT) as UNITPRICE";
					htDoDet.put(IConstants.DODET_DONUM, PO_NUM);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.ITEM, ITEM_NUM);
					htDoDet.put(IConstants.UNITMO, UOM);
					DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
						if(DODetails.size() > 0) {	
											Map map1 = (Map) DODetails.get(0);
												unitprice = Double.parseDouble((String) map1.get("UNITPRICE"));
												PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
												StrUtils strUtils     = new StrUtils();
												totalprice=totalprice+(unitprice* Float.parseFloat(PICKING_QTY));
												priceval=String.valueOf(totalprice);
												double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
												priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
						}
						//Insert GINOTOINVOICE
						htRecvDet.clear();
						htRecvDet.put(IConstants.PLANT, PLANT);
						htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
						htRecvDet.put("GINO", INVOICENO);                    
						htRecvDet.put(IConstants.DODET_DONUM, PO_NUM);
						htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
						htRecvDet.put(IConstants.AMOUNT, priceval);
						htRecvDet.put(IConstants.QTY, String.valueOf(PICKING_QTY));
						htRecvDet.put("CRAT",dateutils.getDateTime());
						htRecvDet.put("CRBY",LOGIN_USER);
						htRecvDet.put("UPAT",dateutils.getDateTime());
						
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
		    			htRecvHis.put(IConstants.QTY, String.valueOf(PICKING_QTY));
		    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
		    			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
		    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
		    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						
						flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_FINGINOTOINVOICE where DONO='"+PO_NUM+"' and GINO='"+INVOICENO+"'");
						if (!flag) {
							flag = _DoDetDAO.insertGINOtoInvoice(htRecvDet);
							flag = movHisDao.insertIntoMovHis(htRecvHis);
						} 
						else{
							htRecvDet = new Hashtable();
							htRecvDet.clear();
							htRecvDet.put(IConstants.PLANT, PLANT);
							htRecvDet.put("GINO", INVOICENO);
							htRecvDet.put(IConstants.DODET_DONUM, PO_NUM);
							
							htRecvHis = new Hashtable();
							htRecvHis.clear();
		        			htRecvHis.put(IDBConstants.PLANT, PLANT);
		        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
		        			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
		        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
							
							flag=_DoDetDAO.updateGINOtoInvoice(" set AMOUNT+="+priceval+",QTY+="+PICKING_QTY,htRecvDet,"",PLANT);
							flag=movHisDao.updateMovHis(" set QTY+="+PICKING_QTY,htRecvHis,"",PLANT);
						}
						
				}
				

			} else {
				throw new Exception(" Picking is not successfull ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
		return xmlStr;
	}
	
	private String process_doPicking_random_bypda(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map issMat_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",CONTAINER="",REMARK;
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ORD_QTY = "", 
		INV_QTY = "", PICKING_QTY = "",ISNONSTKFLG="",INVID="",INVOICENO="",EMPNO="",UOM="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			//ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEM_DESC"));
			ITEM_DESCRIPTION = new ItemMstDAO().getItemDesc(PLANT,ITEM_NUM);
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			CONTAINER= StrUtils.fString(request.getParameter("CONTAINER"));
			CONTAINER= StrUtils.replaceCharacters2Recv(CONTAINER);
			REMARK=StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			REMARK= StrUtils.replaceCharacters2Recv(REMARK);
			INVID=StrUtils.fString(request.getParameter("INVID"));
			INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
        
            DateUtils _dateUtils = new DateUtils();
            String curDate =_dateUtils.getDate();
            String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
            String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
            
			issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			issMat_HM.put(IConstants.PODET_PONUM, PO_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.QTY, PICKING_QTY);
			issMat_HM.put("TRAN_QTY", PICKING_QTY);
			issMat_HM.put(IConstants.INV_EXP_DATE, "");
			issMat_HM.put(IConstants.OUT_CONTAINER, CONTAINER);
			issMat_HM.put(IConstants.OUT_REMARK1, REMARK);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.INVID, INVID);
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);
			issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put(IConstants.BATCH_ID, INVID);
			issMat_HM.put(IConstants.UNITMO, UOM);
			xmlStr = "";
			
			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, ITEM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, ITEM_BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			double invumqty = Double.valueOf(PICKING_QTY) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;			
			if(!ISNONSTKFLG.equalsIgnoreCase("Y"))
			{
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					throw new Exception(" Not Enough Inventory For Product ");
				}
			}
			if (flag) {
				if(!INVOICENO.isEmpty()){
				flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_SHIPHIS where DONO='"+PO_NUM+"' and INVOICENO='"+INVOICENO+"'");
				}
				else{
					flag = false;
				}
				xmlStr = _DOUtil.process_DoPickingForPDA_Random_ByPDA(issMat_HM);
				if (!flag) {
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				}
			} else {
				throw new Exception(" Picking is not successfull ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return xmlStr;
	}
	
	private String process_doissue_random_bypda(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map issMat_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "",DO_LN_NUM="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",CONTAINER="",REMARK;
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ORD_QTY = "", INV_QTY = "", 
		PICKING_QTY = "",ISNONSTKFLG="",INVID="",INVOICENO="",EMPNO="",creditLimit ="", creditBy = "",ISSUEDATE="",UOM="",SHIPPINGNO="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEM_DESC"));
			
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ISSUEDATE =StrUtils.fString(request.getParameter("ISSUEDATE"));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);	
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty = StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			CONTAINER= StrUtils.fString(request.getParameter("CONTAINER"));
			
			REMARK=StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			REMARK= StrUtils.replaceCharacters2Recv(REMARK);
			INVID=StrUtils.fString(request.getParameter("INVID"));
			INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			DO_LN_NUM=StrUtils.fString(request.getParameter("DO_LN_NUM"));
			
			if (SHIPPINGNO.length() == 0) {
				SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
			}
			
		    DateUtils _dateUtils = new DateUtils();
            String curDate =_dateUtils.getDate();
            String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
            String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
            issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			issMat_HM.put(IConstants.DODET_DONUM, PO_NUM);
			issMat_HM.put(IConstants.DODET_DOLNNO, DO_LN_NUM);
			issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			issMat_HM.put(IConstants.LOC, ITEM_LOC);
			
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
	//		issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
	//		issMat_HM.put(IConstants.QTY, PICKING_QTY);
	//		issMat_HM.put("TRAN_QTY", PICKING_QTY);
	//		issMat_HM.put(IConstants.INV_EXP_DATE, "");
			issMat_HM.put(IConstants.OUT_CONTAINER, CONTAINER);
			issMat_HM.put(IConstants.OUT_REMARK1, REMARK);
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,PO_NUM));
			issMat_HM.put("INV_QTY", "1");
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, ISSUEDATE);
	//		issMat_HM.put(IConstants.INVID, INVID);
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);
            issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
            issMat_HM.put(IConstants.UNITMO, UOM);
            
            CustUtil custUtil = new CustUtil();
			ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, PO_NUM),PLANT);
			creditLimit   = (String)arrCust.get(24);
			creditBy   = (String)arrCust.get(35);
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
			issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
			issMat_HM.put(IConstants.BASE_CURRENCY, _PlantMstDAO.getBaseCurrency(PLANT));
			xmlStr = "";
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
		//	double invumqty = Double.valueOf(PICKING_QTY) * Double.valueOf(UOMQTY);			
			issMat_HM.put(IConstants.QTY, String.valueOf(PICKING_QTY));
			issMat_HM.put("UOMQTY", UOMQTY);
			boolean flag = true;

			if (flag) {
				boolean shipflag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_SHIPHIS where DONO='"+PO_NUM+"' and INVOICENO='"+INVOICENO+"'");
				xmlStr = _DOUtil.process_DoIssue_Random_ByPDA(issMat_HM);
				//To GI
				if(xmlStr.contains("issued successfully"))
				{
					DoDetDAO _DoDetDAO = new DoDetDAO();
					DateUtils dateutils = new DateUtils();
					Hashtable htRecvDet = new Hashtable();
					
					double unitprice=0,totalprice=0,totalqty=0;
					String priceval="";
					Hashtable htDoDet = new Hashtable();
					ArrayList DODetails = null;
					String queryDoDet = "(UNITPRICE*CURRENCYUSEQT) as UNITPRICE";
					htDoDet.put(IConstants.DODET_DONUM, PO_NUM);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.ITEM, ITEM_NUM);
					htDoDet.put(IConstants.UNITMO, UOM);
					DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);
						if(DODetails.size() > 0) {	
											Map map1 = (Map) DODetails.get(0);
												unitprice = Double.parseDouble((String) map1.get("UNITPRICE"));
												PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
												StrUtils strUtils     = new StrUtils();
												totalprice=totalprice+(unitprice* Float.parseFloat(PICKING_QTY));
												priceval=String.valueOf(totalprice);
												double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
												priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
						}
					
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
					htRecvDet.put("GINO", INVOICENO);                    
					htRecvDet.put(IConstants.DODET_DONUM, PO_NUM);
					htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
					htRecvDet.put(IConstants.AMOUNT, priceval);
					htRecvDet.put(IConstants.QTY, String.valueOf(PICKING_QTY));
					htRecvDet.put("CRAT",dateutils.getDateTime());
					htRecvDet.put("CRBY",LOGIN_USER);
					htRecvDet.put("UPAT",dateutils.getDateTime());
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
	    			htRecvHis.put(IConstants.QTY, String.valueOf(PICKING_QTY));
	    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
	    			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
	    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
	    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
	    			flag = movHisDao.insertIntoMovHis(htRecvHis);

				}

				if (flag) {
				if (!shipflag) {
					new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
					}
				}
			} else {
				throw new Exception(" Issue is not successfull ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return xmlStr;
	}

	
	
	private String getBatchCodeByBatchRandom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		    String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
		    String batch = StrUtils.fString(request.getParameter("BATCH"));
                      
                    
			InvMstUtil itM = new InvMstUtil();
			itM.setmLogger(mLogger);
			xmlStr = itM.getBatchDetailByBatch(plant, itemNo, batch,userID);
			if (xmlStr.length() == 0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	
	private String load_locations_with_batch(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",item="",batch="",loc="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM_NUM"));
			batch = StrUtils.fString(request.getParameter("BATCH"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			
			InvMstUtil _InvMstUtil = new InvMstUtil();
			_InvMstUtil .setmLogger(mLogger);
			str = _InvMstUtil.getLocDetailWithBatch(PLANT, userID,item,batch,loc);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String check_locations_with_batch(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",item="",batch="",loc="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM_NUM"));
			batch = StrUtils.fString(request.getParameter("BATCH"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			
			InvMstUtil _InvMstUtil = new InvMstUtil();
			_InvMstUtil .setmLogger(mLogger);
			str = _InvMstUtil.checkLocDetailWithBatch(PLANT, userID,item,batch,loc);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_ob_location_details_by_createddate(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",item="",loc="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			ItemMstDAO _itemMstDAO = new ItemMstDAO();      
			InvMstUtil _InvMstUtil = new InvMstUtil();
			
			_InvMstUtil .setmLogger(mLogger);
			
			String Nonstock = _itemMstDAO.getNonStockFlag(PLANT,item);
			if(Nonstock.equals("Y")){
				str = _InvMstUtil.loadLocationsXmlPDA(PLANT, loc,userID);
			}
			else
			{
			str = _InvMstUtil.getOBLocDetailsByCreatedDate(PLANT, userID,item,loc);
			}
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String load_ob_location_details_by_expirydate(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",item="",loc="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			
			InvMstUtil _InvMstUtil = new InvMstUtil();
			_InvMstUtil .setmLogger(mLogger);
			str = _InvMstUtil.getOBLocDetailsByExpiryDate(PLANT, userID,item,loc);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String load_ob_batch_details_by_createddate(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",item="",loc="",batch="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			batch = StrUtils.fString(request.getParameter("BATCH"));
			
			InvMstUtil _InvMstUtil = new InvMstUtil();
			_InvMstUtil .setmLogger(mLogger);
			str = _InvMstUtil.getOBBatchDetailsByCreatedDate(PLANT, userID,item,loc,batch);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String load_ob_batch_details_by_expirydate(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",item="",loc="",batch="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			batch = StrUtils.fString(request.getParameter("BATCH"));
			
			InvMstUtil _InvMstUtil = new InvMstUtil();
			_InvMstUtil .setmLogger(mLogger);
			str = _InvMstUtil.getOBBatchDetailsByExpiryDate(PLANT, userID,item,loc,batch);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
private static Vector readFile(String sSigndata) throws Exception {
		    Vector v = new Vector();
		    StrUtils strUtils = new StrUtils();
		    StringReader sr = new StringReader(sSigndata);
		    BufferedReader br = new BufferedReader(sr);
		    String record = null;
		    // System.out.println(br.readLine());

		    while (strUtils.fString( (record = br.readLine())).trim().length() > 0) {
		      String[] points = new String[4];
		      points = record.split(" ");
		      v.add(points);
		    }
		    return v;
		  }

		  private static Hashtable getMinMax(Vector v) {
		    System.out.println("**********getMinMax*********");
		    Hashtable ht = new Hashtable();
		    int minX = 9999;
		    int maxX = 0;
		    int minY = 9999;
		    int maxY = 0;
		    for (int j = 0; j < v.size(); j++) {
		      String[] points = (String[]) v.get(j);
		      for (int i = 0; i < 4; i++) {
		        minX = (Integer.parseInt(points[0]) < minX) ? Integer.parseInt(points[0]) :
		            minX;
		        minX = (Integer.parseInt(points[2]) < minX) ? Integer.parseInt(points[2]) :
		            minX;
		        maxX = (Integer.parseInt(points[0]) > maxX) ? Integer.parseInt(points[0]) :
		            maxX;
		        maxX = (Integer.parseInt(points[2]) > maxX) ? Integer.parseInt(points[2]) :
		            maxX;

		        minY = (Integer.parseInt(points[1]) < minY) ? Integer.parseInt(points[1]) :
		            minY;
		        minY = (Integer.parseInt(points[3]) < minY) ? Integer.parseInt(points[3]) :
		            minY;
		        maxY = (Integer.parseInt(points[1]) > maxY) ? Integer.parseInt(points[1]) :
		            maxY;
		        maxY = (Integer.parseInt(points[3]) > maxY) ? Integer.parseInt(points[3]) :
		            maxY;

		      }
		    }
		    ht.put("minX", new Integer(minX));
		    ht.put("maxX", new Integer(maxX));
		    ht.put("minY", new Integer(minY));
		    ht.put("maxY", new Integer(maxY));
		    return ht;
		  }

		  private static void drawImage(Graphics g2d, Vector v, int minX, int minY) throws
		      Exception {
		    System.out.println("**********drawImage*********************");
		    g2d.setColor(Color.black);
		    for (int j = 0; j < v.size(); j++) {
		      String[] points = (String[]) v.get(j);
		      int x1 = 0;
		      int y1 = 0;
		      int x2 = 0;
		      int y2 = 0;
		      for (int i = 0; i < 4; i++) {
		        x1 = Integer.parseInt(points[0]) - minX + (baseX / 2);
		        x2 = Integer.parseInt(points[2]) - minX + (baseX / 2);
		        y1 = Integer.parseInt(points[1]) - minY + (baseY / 2);
		        y2 = Integer.parseInt(points[3]) - minY + (baseY / 2);
		      }
		      g2d.drawLine(x1, y1, x2, y2);
		    }
		  }
 /* public static void deleteFilesOlderThanNdays(int daysBack, String dirWay, org.apache.commons.logging.Log log) {
			  
			    File directory = new File(dirWay);
			    if(directory.exists()){
			 
			        File[] listFiles = directory.listFiles();            
			        long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);
			        for(File listFile : listFiles) {
			            if(listFile.lastModified() < purgeTime) {
			                if(!listFile.delete()) {
			                    System.err.println("Unable to delete file: " + listFile);
			                }
			            }
			        }
			    } else {
			        log.warn("Files were not deleted, directory " + dirWay + " does'nt exist!");
			    }
			}*/
		  
		  public static void deleteFiles(long days, String fileExtension,String plant) {
			   //String dirPath = "c:\\hello";
			  String dirPath=DbBean.COMPANY_SIGN_PATH + "/"+ plant;
		        File folder = new File(dirPath);
		 
		        if (folder.exists()) {
		 
		            File[] listFiles = folder.listFiles();
		 
		            long eligibleForDeletion = System.currentTimeMillis() -
		                (days * 24 * 60 * 60 * 1000 );
		 
		            for (File listFile: listFiles) {
		 
		                if (listFile.getName().endsWith(fileExtension) &&
		                    listFile.lastModified() < eligibleForDeletion) {
		 
		                    if (!listFile.delete()) {
		 
		                        System.out.println("Sorry Unable to Delete Files..");
		 
		                    }
		                }
		            }
		        }
		    }

 	private String process_signature(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException,
					Exception {
				Map htSign = null;
				String PLANT = "", USERID ="",DONO = "", SIGNERNAME="",Signdata="",fileLocation="",filetempLocation="",fileName="",signpath="";
				PLANT = StrUtils.fString(request.getParameter("PLANT"));
				USERID = StrUtils.fString(request.getParameter("LOGIN_USER"));
				DONO = StrUtils.fString(request.getParameter("DONUM"));
				Signdata = StrUtils.fString(request.getParameter("SIGNDATA"));
				SIGNERNAME= StrUtils.fString(request.getParameter("SIGNERNAME"));
				fileLocation = DbBean.COMPANY_SIGN_PATH + "/"+ PLANT;
				filetempLocation = DbBean.COMPANY_SIGN_PATH + "/temp" + "/" + PLANT;
				
			    
			    //------------------ Supervisor Sign
		        try {
		          Vector v = readFile(Signdata);
		          Hashtable ht = getMinMax(v);
		          int minX = ( (Integer) ht.get("minX")).intValue();
		          int maxX = ( (Integer) ht.get("maxX")).intValue();
		          int minY = ( (Integer) ht.get("minY")).intValue();
		          int maxY = ( (Integer) ht.get("maxY")).intValue();

		          int width = maxX - minX + baseX;
		          int height = maxY - minY + baseY;

		          // Create a buffered image in which to draw
		          BufferedImage bufferedImage = new BufferedImage(width, height,
		              BufferedImage.TYPE_INT_RGB);
		          // Create a graphics contents on the buffered image
		   	      	
		          Graphics g2d = bufferedImage.createGraphics();
		          g2d.setColor(Color.white);
		          // Draw graphics
		          g2d.fillRect(0, 0, width, height);
		          drawImage(g2d, v, minX, minY);

		          // Graphics context no longer needed so dispose it
		          g2d.dispose();
		          //File imageFile = new File("D:\\images\\signdata.bmp");
		          
		        /*Hashtable ht = new Hashtable();
		  		ht.put(IConstants.PLANT, plant);
		  		noofrecords = _catalogUtil.NoofRecords(ht, "");
		  		catalogspercompany = sb.getNOfCatalogs(plant);
		  		// System.out.println("catalogsize"+catalogspercompany);
		  		int noofcatlogs = Integer.parseInt(catalogspercompany);
		  		// Check not to exceed 50 records
		  		if (noofrecords > noofcatlogs) {
		  			result = "<font color=\"red\"> Catalogs exceeded more than "
		  					+ noofcatlogs + " </font>";
		  			response.sendRedirect("jsp/createCatalog.jsp?result=" + result);
		  		} else {  */
		         boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				/*if (!isMultipart) {
					System.out.println("File Not Uploaded");
				} else {*/
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					File path = new File(fileLocation);
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					/*if(PLANT.equals("test"))
					{
						deleteFiles(4, ".bmp",PLANT);
					}*/
					fileName = "sign"+DONO;
					File uploadedFile = new File(path + "/" + fileName+ ".bmp");
					System.out.println("uploadfile......"+uploadedFile);
					signpath = uploadedFile.getAbsolutePath();
					ImageIO.write(bufferedImage, "bmp",uploadedFile);
			          if (bufferedImage == null)
			          {
			            throw new Exception("Customer's sign is not available");
			          }
			          else
			          {
			        	  			        	  
			        	htSign = new HashMap();
			  			htSign.put(IConstants.PLANT, PLANT);
			  			htSign.put(IConstants.DONO, DONO);
			  			signpath = signpath.replace('\\', '/');
			  			htSign.put("SIGNPATH", signpath);
			  			htSign.put("SIGNERNAME", SIGNERNAME);
			  			htSign.put(IConstants.LOGIN_USER, USERID);
			  			xmlStr = "";
			  			boolean flag = true;

						if (flag) {
							xmlStr = _DOUtil.process_Signature(htSign);
						} else {
							throw new Exception("Customer's signature capture is not successful ");
						}
						
			          }
			          
				//}
		        }
		        catch (Exception e) {
		        	this.mLogger.exception(this.printLog, "", e);
					throw e;
		        }
		        
				
				return xmlStr;
			}
		   
		   private String get_signature_order_status(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", plant = "", dono = "",userid="";
				try {

					plant = StrUtils.fString(request.getParameter("PLANT"));
					dono = StrUtils.fString(request.getParameter("DONUM"));
					userid= StrUtils.fString(request.getParameter("LOGIN_USER"));

					str = _DOUtil.getSignatureOrderStatus(plant, dono);
					/*if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "Order details not found!");
					}*/

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}
				return str;
			}
		   private String check_plant(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException,
					Exception {
				String str = "";

				String PLANT = "", userID = "",item="",batch="",loc="";
				try {
					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
					
					
					PlantMstUtil _PlantMstUtil = new PlantMstUtil();
					_PlantMstUtil .setmLogger(mLogger);
					str = _PlantMstUtil.checkPlant(PLANT);
					if (str.equalsIgnoreCase("")) {

						str = XMLUtils.getXMLMessage(1, "Details not found");
					}
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				}
				return str;
			}	
		
		private String GenerateShippingNo(String plant, String loginuser) {

				String PLANT = "";
				boolean flag = false;
				boolean updateFlag = false;
				boolean insertFlag = false;
				boolean resultflag = false;
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

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();

						htTblCntInsert.put(IDBConstants.PLANT, (String) plant);

						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
								(String) IDBConstants.TBL_SHIPPING_CAPTION);
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "Shipping");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
								(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, (String) loginuser);
						htTblCntInsert.put(IDBConstants.CREATED_AT,
								(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,
								plant);

						MovHisDAO mdao = new MovHisDAO(plant);
						mdao.setmLogger(mLogger);
						Hashtable htm = new Hashtable();
						htm.put("PLANT", (String) plant);
						htm.put("DIRTYPE", "GENERATE_SHIPPING");
						htm.put("RECID", "");
						htm.put("CRBY", (String) loginuser);
						htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htm.put("CRAT", (String) new DateUtils().getDateTime());
						boolean inserted = mdao.insertIntoMovHis(htm);
						if (insertFlag) {
							resultflag = true;
						} else if (!insertFlag) {

							throw new Exception(
									"Generate Shipping Failed, Error In Inserting Table:"
											+ " " + PLANT + "_ " + "TBLCONTROL");
						} else if (inserted) {
							resultflag = true;
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

						Map htUpdate = null;

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
						htm.put("CRAT", (String) new DateUtils().getDateTime());
						boolean inserted = mdao.insertIntoMovHis(htm);

						if (updateFlag) {
							resultflag = true;
						} else if (!updateFlag) {

							throw new Exception(
									"Update Shippoing Failed, Error In Updating Table:"
											+ " " + PLANT + "_ " + "TBLCONTROL");
						} else if (inserted) {
							resultflag = true;
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

 private String getSignatureCount(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", plant = "", dono = "",userid="";
				try {

					plant = StrUtils.fString(request.getParameter("PLANT"));
					userid= StrUtils.fString(request.getParameter("LOGIN_USER"));
					xmlStr = XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("ItemDetails");

					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					
					int noofrecords = 0;  
					String signpercompany="";
					PlantMstUtil _PlantMstUtil=new PlantMstUtil();
					Hashtable ht1 = new Hashtable();
					selectBean sb = new selectBean();
			  		ht1.put(IConstants.PLANT, plant);
			  		noofrecords = _PlantMstUtil.getSignatureCount(ht1, "");
			  		signpercompany = sb.getNOfSignatures(plant);

			  		xmlStr += XMLUtils.getXMLNode("noofrecords", Integer.toString(noofrecords));
			  		xmlStr += XMLUtils.getXMLNode("signpercompany", signpercompany);

					xmlStr += XMLUtils.getEndNode("ItemDetails");

					if (xmlStr.equalsIgnoreCase("")) {
						xmlStr = XMLUtils.getXMLMessage(1, "Details Not found");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}
				return xmlStr;
			}


 private String validate_location(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", loc = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			loc = StrUtils.fString(request.getParameter("LOC"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("LOC", loc);
			
			LocMstDAO itM = new LocMstDAO();
			itM.setmLogger(mLogger);
			boolean locFound = itM.isExisit(ht, " ISACTIVE='Y'");

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
			
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
			
			

			if (locFound) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Location found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Location not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("locationDetails");
			
			

			if (xmlStr.equalsIgnoreCase("")) {
				throw new Exception("Location not found");
			}
		} catch (Exception e) {
			throw e;
		}
		// MLogger.log(0, "validate_location() Ends");
		return xmlStr;
	}
		
 private String load_OutboundOrdersByItem(HttpServletRequest request,
         HttpServletResponse response) throws IOException, ServletException {
 String str = "", aPlant = "", aOrdNo = "";
 try {

         PLANT = StrUtils.fString(request.getParameter("PLANT"));
         String ITEM = StrUtils.fString(request.getParameter("ITEMNO"));

         str = _DOUtil.getOutboundOrderByItem(PLANT,ITEM);

         if (str.equalsIgnoreCase("")) {
                 str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found for the Item : "+ITEM);
         }

 } catch (Exception e) {
         this.mLogger.exception(this.printLog, "", e);
         str = XMLUtils.getXMLMessage(1, e.getMessage());
 }

 return str;
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
	public boolean CheckInvMstForGoddsIssue(Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean flag = true;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Boolean batchChecked = false;
		
		try {
			

			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			HttpSession session = request.getSession();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(map.get(IConstants.BATCH_ID)) && !"-1".equals(map.get(IConstants.BATCH_ID))) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			StringBuffer sql = new StringBuffer(" ");
			List invlist = _InvMstDAO.selectInvMstByCrat("item,loc,userfld4 as batch,qty,crat,id", htInvMst);
			String actualqty = "";
			double actqty = 0;
			double lnqty = 0, balancqty = 0;
			double totalqty = 0;
			actualqty = map.get(IConstants.QTY);			
			actqty = Double.valueOf(actualqty);

			if (request.getParameter("chkbatch") != null) {
				batchChecked = true;
			}

			// Calculate total qty in the loc
			for (int j = 0; j < invlist.size(); j++) {
				Map lineitem = (Map) invlist.get(j);
				String lineitemqty = (String) lineitem.get("qty");				

				totalqty = totalqty + Double.valueOf(lineitemqty);

			}

			if (actqty > totalqty) {
				flag = false;
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

			if (totalqty == 0) {
				flag = false;
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

		} catch (Exception e) {
			flag = false;
		}
		return flag;
		}
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
	private String load_do_uom(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",dono = "",item = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			dono = StrUtils.fString(request.getParameter("DO_NUM"));
			item = StrUtils.fString(request.getParameter("ITEM_NUM"));
			DOUtil doUtil = new DOUtil();
			doUtil.setmLogger(mLogger);
			str = doUtil.getDoUomWithQty(PLANT, userID,dono,item);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String generateINVOICE(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			str = _TblControlDAO.getNextOrder(PLANT,userID,"INVOICE");
			if (str!="") {
				    xmlStr += XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("INVOICENO", str);
					xmlStr += XMLUtils.getEndNode("record");
					System.out.println(xmlStr);
				    str=xmlStr;
			}
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String generateGINO(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			str = _TblControlDAO.getNextOrder(PLANT,userID,"GINO");
			if (str!="") {
				    xmlStr += XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("INVOICENO", str);
					xmlStr += XMLUtils.getEndNode("record");
					System.out.println(xmlStr);
				    str=xmlStr;
			}
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	
	private String process_SignaturePda(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		 response.setContentType("text/html;charset=UTF-8");
		
		 Hashtable issMat_HM = new Hashtable();
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String DO_NUM="";
		String ITEM_DESCRIPTION = "",INVOICENO="", LOGIN_USER = "",SIGNATURE="",SIGNATURENAME="",EMAIL="";
        
		try {
//			Part pl = request.getPart ("PLANT");
//			Part Don =	request.getPart("DO_NUM");
//			Part logn = request.getPart("LOGIN_USER");
//			Part INVOICEN =request.getPart("INVOICENO");
//			Part SIGN = request.getPart("SIGNATURE");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
			SIGNATURE=StrUtils.fString(request.getParameter("SIGNATURE"));
			SIGNATURENAME=StrUtils.fString(request.getParameter("SIGNATURENAME"));
			EMAIL=StrUtils.fString(request.getParameter("EMAIL"));
			issMat_HM.put(IConstants.PLANT, PLANT);
		    
		
			
			//BASE64Decoder decoder = new BASE64Decoder();
			//byte[] bytearr = decoder.decodeBuffer(SIGNATURE);
			Base64.Decoder decoder = Base64.getDecoder();
	        byte[] bytearr = decoder.decode(SIGNATURE);
		
			BufferedImage img ;
			img =ImageIO.read(new ByteArrayInputStream(bytearr));
			ByteArrayInputStream bis = new ByteArrayInputStream(bytearr);
			BufferedImage bimages2 = ImageIO.read(bis);
			String fileLocation = DbBean.COMPANY_SIGN_PATH + "/" + PLANT;
	
			File path = new File(fileLocation);
			if (!path.exists()) {
				boolean status = path.mkdirs();
			}
			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(INVOICENO)
					+ ".GIF");
			if (!uploadedFile.exists())
				
			ImageIO.write(img,"GIF", uploadedFile);
			String catlogpath = uploadedFile.getAbsolutePath();
			catlogpath = catlogpath.replace('\\', '/');
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.DODET_DONUM, PO_NUM);
			issMat_HM.put(IConstants.INVOICENO, INVOICENO);			
	
			
			xmlStr = "";

			boolean flag = false;		
			flag = _DoDetDAO.updateShipHis(" set PDASIGNPATH = '"+ catlogpath+"', SIGNATUREEMAIL = '"+EMAIL+"',SIGNATURENAME = '"+SIGNATURENAME+"'", issMat_HM, "");
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return xmlStr;
	}

	//created by vicky Used for PDA Sales order popup
	private String load_outBoundOrdersPDApopup(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",START,END,DONO;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			START = StrUtils.fString(request.getParameter("START"));
			END = StrUtils.fString(request.getParameter("END"));
			DONO = StrUtils.fString(request.getParameter("DONO"));

			Hashtable ht = new Hashtable();
			

			str = _DOUtil.getPDApopupOutBoundOrder(PLANT,START,END,DONO);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	//created by vicky Used for PDA Sales order AutoSuggestion
			private String load_OutBoundOrdersPDAAutoSuggestion(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "",DONO;
				try {

					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					DONO = StrUtils.fString(request.getParameter("DONO"));

					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);

					str = _DOUtil.getPDAOutBoundOrderAutoSuggestion(PLANT,DONO);

					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "No Inbound Orders Found");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
			}
   //created by vicky Used for PDA Sales Issue order popup
			private String load_outBoundOrdersIssuePDApopup(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "",START,END,DONO;
				try {

					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					START = StrUtils.fString(request.getParameter("START"));
					END = StrUtils.fString(request.getParameter("END"));
					DONO = StrUtils.fString(request.getParameter("DONO"));

					Hashtable ht = new Hashtable();
					

					str = _DOUtil.getPDApopupOutBoundOrderIssue(PLANT,START,END,DONO);
					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
			}
	//created by vicky Used for PDA Sales Issue order AutoSuggestion
			private String load_OutBoundOrdersIssuePDAAutoSuggestion(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "",DONO;
				try {

					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					DONO = StrUtils.fString(request.getParameter("DONO"));

					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);

					str = _DOUtil.getPDAOutBoundOrderIssueAutoSuggestion(PLANT,DONO);

					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "No Inbound Orders Found");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
			}
			//Created by Vicky Desc:Used for getting Sales Order item For UOM popup Screen in PDA
			private String load_outBoundOrder_item_details_ForPDA_UOMPopup(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "", aItem="",uom="";
				try {
					
					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
					aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
					
					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);
					ht.put("DONO", aOrdNo);
					ht.put("ITEM", aItem);
					
					str = _DOUtil.getoutBoundOrder_item_order_ForUOMPopup(PLANT, aOrdNo,aItem,uom,
							Boolean.FALSE);

					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "Product details not found.");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
			}
			//created by vicky Used for PDA Checking Sales order Details
			private String check_sales_order_details(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "",DONO;
				try {

					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					DONO = StrUtils.fString(request.getParameter("DONO"));

					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);

					str = _DOUtil.getPDACheckingSalesOrder(PLANT,DONO);

					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "No Inbound Orders Found");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
			}
			//created by vicky Used for PDA Sales Check order popup
			private String load_salesCheckOrdersPDApopup(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "",START,END,DONO;
				try {

					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					START = StrUtils.fString(request.getParameter("START"));
					END = StrUtils.fString(request.getParameter("END"));
					DONO = StrUtils.fString(request.getParameter("DONO"));

					Hashtable ht = new Hashtable();
					

					str = _DOUtil.getPDApopupSalesOrderCheck(PLANT,START,END,DONO);
					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
			}
			//created by vicky Used for PDA getting Items Sales Check order
			private String load_random_salescheckOrder_item_details(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "";
				try {
					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);
					ht.put("DONO", aOrdNo);

					str = _DOUtil.getRandomSalesCheckOrderItemDetails(PLANT, aOrdNo,
							Boolean.FALSE);

					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "Product details not found.");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}
				return str;
			}
			//created by vicky Used for PDA Sales Check order item popup
			private String load_SalesCheckOrder_item_ByItem(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "", aItem="",start="",end="";
				try {
				
					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
					aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
					start = StrUtils.fString(request.getParameter("START"));
					end = StrUtils.fString(request.getParameter("END"));

					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);
					ht.put("DONO", aOrdNo);
					ht.put("ITEM", aItem);
					ht.put("START", start);
					ht.put("END", end);

					str = _DOUtil.getSalesOrder_item_Byitem(PLANT, aOrdNo,aItem,start,end,
							Boolean.FALSE);

					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "Product details not found.");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
			}
			//created by vicky Used for PDA Sales Check order item validation
			private String load_SalesCheckOrder_item_validation(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException {
				String str = "", aPlant = "", aOrdNo = "", aItem="",start="",end="";
				try {
				
					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
					aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
				

					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);
					ht.put("DONO", aOrdNo);
					ht.put("ITEM", aItem);
					

					str = _DOUtil.getSalesOrder_item_validation(PLANT, aOrdNo,aItem,start,end,
							Boolean.FALSE);

					if (str.equalsIgnoreCase("")) {
						str = XMLUtils.getXMLMessage(1, "Product details not found.");
					}

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					str = XMLUtils.getXMLMessage(1, e.getMessage());
				}

				return str;
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
