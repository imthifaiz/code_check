package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.LocMstTwoDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.db.util.GstTypeUtil;
import com.track.db.util.LocTypeUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POSUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.pda.posServlet;
import com.track.tables.ITEMMST;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/inhouse/*")
public class InHouseServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		HttpSession session = request.getSession(false);
		
		if (action.equalsIgnoreCase("stockmove")) {
		    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		    if (ajax) {
		        String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
		        String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
		        String TRANID = StrUtils.fString(request.getParameter("TRANID"));
		        String FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
		        String TOLOC = StrUtils.fString(request.getParameter("TOLOC"));

		        String fdate = "", tdate = "";
		       
		        
		        POSHdrDAO pOSHdrDAO = new POSHdrDAO();
		        Hashtable<String, String> ht = new Hashtable<>();
		        JSONObject resultJson = new JSONObject();
		        JSONArray jsonArray = new JSONArray();

		        try {
		            if (TRANID.length() > 0) ht.put("TRANID", TRANID);
		            if (FROM_LOC.length() > 0) ht.put("FROM_LOC", FROM_LOC);
		            if (TOLOC.length() > 0) ht.put("TOLOC", TOLOC);
		            if (plant.length() > 0) ht.put("PLANT", plant);

		            FROM_DATE = FROM_DATE == null ? "" : FROM_DATE.trim();
		            String curDate = DateUtils.getDate();
		            if (FROM_DATE.isEmpty()) FROM_DATE = curDate;

		            if (FROM_DATE.length() > 5) {
		                fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
		            }

		            TO_DATE = TO_DATE == null ? "" : TO_DATE.trim();
		            if (TO_DATE.length() > 5) {
		                tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
		            }

		            ArrayList<Map<String, String>> listQry = pOSHdrDAO.getsalesdetailsforsummary(plant, ht, fdate, tdate);
		            for (Map<String, String> m : listQry) {
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("PURCHASEDATE", m.get("PURCHASEDATE"));
		                resultJsonInt.put("TRANID", m.get("TRANID"));
		                resultJsonInt.put("FROM_LOC", m.get("LOC"));
		                resultJsonInt.put("TOLOC", m.get("TOLOC"));
		                String status = m.get("STATUS");
		                if(status.equalsIgnoreCase("C")) {
		                	status = "PROCESSED";
		                }else {
		                	status = "HOLD";
		                }
		                resultJsonInt.put("STATUS",status);
		                jsonArray.add(resultJsonInt);
		            }
		            resultJson.put("ORDERS", jsonArray);
		        } catch (Exception e) {
		            e.printStackTrace();
		            response.setStatus(500);
		        }

		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write(resultJson.toString());
		        response.getWriter().flush();
		        response.getWriter().close();
		    } else {
		        String msg = StrUtils.fString(request.getParameter("msg"));
		        request.setAttribute("Msg", msg);
		        RequestDispatcher rd = request.getRequestDispatcher("/jsp/stockMoveSummary.jsp");
		        rd.forward(request, response);
		    }
		}
		if(action.equalsIgnoreCase("new")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
//					RequestDispatcher rd = request.getRequestDispatcher("/jsp/MultiLocationTransferWithBatch.jsp");
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateStockMove.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("stocktake")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/StockTake.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		if(action.equalsIgnoreCase("delete")) {
			JSONObject resultJson = new JSONObject();
			try {
				posServlet _posServlet = new posServlet();
				POSDetDAO posdet = new POSDetDAO();
				String msg = StrUtils.fString(request.getParameter("msg"));
				String tranID = StrUtils.fString(request.getParameter("TRANID"));
				Hashtable<String, String> htTrandcnd = new Hashtable<String, String>();
				htTrandcnd.put("POSTRANID", tranID);
				boolean istranidExists = _posServlet.isExisit(plant, htTrandcnd);
				if(istranidExists) {
					htTrandcnd.clear();
					htTrandcnd.put(IDBConstants.PLANT, plant);
					htTrandcnd.put(IDBConstants.POS_TRANID, tranID);
					POSHdrDAO poshdrdao = new POSHdrDAO();
					boolean flag = poshdrdao.deletePosHdr(htTrandcnd);
					posdet.deletePosTranId(plant, tranID);	
					if(flag) {
						msg ="Deleted Successfully"	;
						resultJson.put("MESSAGE", "Stock Move '"+tranID+"' Deleted Successfully.");
						resultJson.put("ERROR_CODE", "100");
					}
				}
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
			}
		}
		
		if(action.equalsIgnoreCase("manualstocktake")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ManualStockTake.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("stocktakewithprice")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/StockTakeWithPrice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("stocktakereset")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/StockTakeReset.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
if(action.equalsIgnoreCase("printbarcode")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("PAGE_TYPE"));
			String PRINT_TYPE = StrUtils.fString(request.getParameter("PRINT_TYPE"));
			request.setAttribute("PAGE_TYPE", msg);
			request.setAttribute("printtype", PRINT_TYPE);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcode.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}		

	if(action.equalsIgnoreCase("genbarcode")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/generateBarcode.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	if(action.equalsIgnoreCase("genmultiproductbarcode")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintMultipleProductcode.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

if(action.equalsIgnoreCase("genreceiptbarcode")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcodeRecvSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

if(action.equalsIgnoreCase("genlocationbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcodeLocSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("genmanualbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcodeManual.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("genproductbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintProductcode.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}


if(action.equalsIgnoreCase("locdetail")) {


try {
	String msg = StrUtils.fString(request.getParameter("msg"));
	request.setAttribute("Msg", msg);
	RequestDispatcher rd = request.getRequestDispatcher("/jsp/locDetail.jsp");
rd.forward(request, response);
} catch (Exception e) {
e.printStackTrace();
}
}


			
}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		HttpSession session = request.getSession(false);
	
		if (action.equalsIgnoreCase("stockmove")) {
		    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		    if (ajax) {
		        String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
		        String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
		        String TRANID = StrUtils.fString(request.getParameter("TRANID"));
		        String FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
		        String TOLOC = StrUtils.fString(request.getParameter("TOLOC"));

		        String fdate = "", tdate = "";
		       
		        
		        POSHdrDAO pOSHdrDAO = new POSHdrDAO();
		        Hashtable<String, String> ht = new Hashtable<>();
		        JSONObject resultJson = new JSONObject();
		        JSONArray jsonArray = new JSONArray();

		        try {
		            if (TRANID.length() > 0) ht.put("TRANID", TRANID);
		            if (FROM_LOC.length() > 0) ht.put("FROM_LOC", FROM_LOC);
		            if (TOLOC.length() > 0) ht.put("TOLOC", TOLOC);
		            if (plant.length() > 0) ht.put("PLANT", plant);

		            FROM_DATE = FROM_DATE == null ? "" : FROM_DATE.trim();
		            String curDate = DateUtils.getDate();
		            if (FROM_DATE.isEmpty()) FROM_DATE = curDate;

		            if (FROM_DATE.length() > 5) {
		                fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
		            }

		            TO_DATE = TO_DATE == null ? "" : TO_DATE.trim();
		            if (TO_DATE.length() > 5) {
		                tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
		            }

		            ArrayList<Map<String, String>> listQry = pOSHdrDAO.getsalesdetailsforsummary(plant, ht, fdate, tdate);
		            for (Map<String, String> m : listQry) {
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("PURCHASEDATE", m.get("PURCHASEDATE"));
		                resultJsonInt.put("TRANID", m.get("TRANID"));
		                resultJsonInt.put("FROM_LOC", m.get("LOC"));
		                resultJsonInt.put("TOLOC", m.get("TOLOC"));
		                String status = m.get("STATUS");
		                if(status.equalsIgnoreCase("C")) {
		                	status = "PROCESSED";
		                }else {
		                	status = "HOLD";
		                }
		                resultJsonInt.put("STATUS",status);
		                jsonArray.add(resultJsonInt);
		            }
		            resultJson.put("ORDERS", jsonArray);
		        } catch (Exception e) {
		            e.printStackTrace();
		            response.setStatus(500);
		        }

		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write(resultJson.toString());
		        response.getWriter().flush();
		        response.getWriter().close();
		    } else {
		        String msg = StrUtils.fString(request.getParameter("msg"));
		        request.setAttribute("Msg", msg);
		        RequestDispatcher rd = request.getRequestDispatcher("/jsp/stockMoveSummary.jsp");
		        rd.forward(request, response);
		    }
		}
	if(action.equalsIgnoreCase("new")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
//				RequestDispatcher rd = request.getRequestDispatcher("/jsp/MultiLocationTransferWithBatch.jsp");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateStockMove.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("detail")) {
		
		
		try {
			POSHdrDAO posHdr = new POSHdrDAO();
			String msg = StrUtils.fString(request.getParameter("msg"));
			String TRANID = StrUtils.fString(request.getParameter("TRANID"));
			Vector<ITEMMST> buylist = (Vector<ITEMMST>) session.getAttribute("poslist");
			Vector<ITEMMST> veclist = this.getItemDetailsForPOSTranId(request, TRANID, buylist);
			session.setAttribute("poslist", veclist);
			String tranTime="",trandate="",status="",crby="",emp_name="";
			if(veclist.size() > 0)
			{
			Hashtable htv1 = new Hashtable();
			htv1.put(IDBConstants.PLANT, plant);
			htv1.put(IDBConstants.POS_TRANID, TRANID);
			ArrayList prdlist = posHdr.selectPosHdr("POSTRANID as TRANID,TRANDT,TRANTM,LOC,TOLOC,CRBY,STATUS,PersonInCharge as PERSON_INCHARGE,contactNum as CONTACT_NUM,REMARK1,REMARKS as REMARK2,REMARK3,CustCode as CUST_CODE,CustName as CUST_NAME,RSNCODE as REASONCODE,JobNum as REFERENCENO,Address as ADD1,Address2 as ADD2,Address3 as ADD3,'' as ADD4,EMPNO as EMP_NAME,ORDERTYPE,DELIVERYDATE,SHIPPINGID,SHIPPINGCUSTOMER,ORDERDISCOUNT,SHIPPINGCOST,INCOTERMS,OUTBOUND_GST as GST,'' as EDIT,ISNULL(CashCust,'') as CashCust,ISNULL((select CUSTOMER_TYPE_ID from "+plant+"_CUSTMST where CustCode=CUSTNO),'') as CUSTOMERTYPEDESC,ISNULL(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,ISNULL(PAYMENTTYPE,'') as PAYMENTTYPE,CURRENCYID as currencyid",htv1);
			session.setAttribute("prdlist", prdlist);
			for (Object item : prdlist) {
			    Map<String, Object> row = (Map<String, Object>) item;
			     tranTime = (String) row.get("TRANTM");
			     trandate = (String) row.get("TRANDT");
			     status = (String) row.get("STATUS");
			     crby = (String) row.get("CRBY");
			     emp_name = (String) row.get("EMP_NAME");
			}
			}
			request.setAttribute("Msg", msg);
			request.setAttribute("tranTime", tranTime);
			request.setAttribute("trandate", trandate);
			request.setAttribute("status", status);
			request.setAttribute("crby", crby);
			request.setAttribute("emp_name", emp_name);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/StockMoveDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("stocktake")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/StockTake.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("manualstocktake")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ManualStockTake.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("stocktakewithprice")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/StockTakeWithPrice.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
if(action.equalsIgnoreCase("stocktakereset")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/StockTakeReset.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
if(action.equalsIgnoreCase("printbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("PAGE_TYPE"));
		String PRINT_TYPE = StrUtils.fString(request.getParameter("PRINT_TYPE"));
		request.setAttribute("PAGE_TYPE", msg);
		request.setAttribute("printtype", PRINT_TYPE);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcode.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("genbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/generateBarcode.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("genmultiproductbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintMultipleProductcode.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

if(action.equalsIgnoreCase("genreceiptbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcodeRecvSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("genlocationbarcode")) {


try {
	String msg = StrUtils.fString(request.getParameter("msg"));
	request.setAttribute("Msg", msg);
	RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcodeLocSummary.jsp");
rd.forward(request, response);
} catch (Exception e) {
e.printStackTrace();
}
}
if(action.equalsIgnoreCase("genmanualbarcode")) {


try {
	String msg = StrUtils.fString(request.getParameter("msg"));
	request.setAttribute("Msg", msg);
	RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintBarcodeManual.jsp");
rd.forward(request, response);
} catch (Exception e) {
e.printStackTrace();
}
}

if(action.equalsIgnoreCase("genproductbarcode")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintProductcode.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

if(action.equalsIgnoreCase("locdetail")) {


try {
	String msg = StrUtils.fString(request.getParameter("msg"));
	request.setAttribute("Msg", msg);
	RequestDispatcher rd = request.getRequestDispatcher("/jsp/locDetail.jsp");
rd.forward(request, response);
} catch (Exception e) {
e.printStackTrace();
}
}
		
}
	
	private Vector<ITEMMST> getItemDetailsForPOSTranId(HttpServletRequest req, String TranId, Vector<ITEMMST> list)
			throws Exception {
		HttpSession session1 = req.getSession();
		GstTypeUtil _GstTypeUtil = new GstTypeUtil();
		Vector<ITEMMST> vecRec = new Vector<ITEMMST>();
		String item = "", itemdesc = "", batch = "", qty = "", price = "", TotalPrice = "", discnt = "",uom="",
				expirydate = ""; // minsprice = "";
//		float discount = 0;
//		String disccntstr = "";
		POSDetDAO posdetDAO = new POSDetDAO();
		String PLANT = session1.getAttribute("PLANT").toString();
		List prdlist = posdetDAO.listProductsForPOSTranID(PLANT, TranId, " (TRANSTATUS='C' or TRANSTATUS='N')");
		float fgsttax = 0;
		List gsttax = _GstTypeUtil.qryGstType("POS", PLANT, " ");
		for (int i = 0; i < 1; i++) {
			Vector vecgsttax = (Vector) gsttax.get(0);
			fgsttax = Float.parseFloat((String) vecgsttax.get(2));
		}
		for (int i = 0; i < prdlist.size(); i++) {
			ITEMMST items = new ITEMMST();
			Vector vecItem = (Vector) prdlist.get(i);
			item = (String) vecItem.get(0);
			itemdesc = (String) vecItem.get(1);
			qty = (String) vecItem.get(2);
			//qty = StrUtils.TrunkateDecimalForImportData(qty);
			price = (String) vecItem.get(3);
			if(Float.parseFloat(price)>0)
			price = new POSUtil().getConvertedLocalCurrencyToUnitCost(PLANT, TranId, price);
			items.setUSERFLD4(price);
			TotalPrice = (String) vecItem.get(4);
			if(Float.parseFloat(TotalPrice)>0)
			TotalPrice = new POSUtil().getConvertedLocalCurrencyToUnitCost(PLANT, TranId, TotalPrice);
			discnt = (String) vecItem.get(5);
			batch = (String) vecItem.get(6);
			expirydate = (String) vecItem.get(7);
			uom = (String) vecItem.get(18);
			items.setEXPIREDATE(expirydate);
			items.setITEM(item);
			items.setITEMDESC(itemdesc);
			items.setBATCH(batch);
			float qtyf = Float.parseFloat(qty);
			items.setStkqty(qtyf);
			float pricef = Float.parseFloat(price);
			items.setUNITPRICE( Float.parseFloat(price));
			float dicountf = Float.parseFloat(discnt);
			items.setDISCOUNT(dicountf);
			float totalPrice = Float.parseFloat(TotalPrice);
			items.setTotalPrice(totalPrice);
			double prval = Double.valueOf((String)vecItem.get(3)); 
			   BigDecimal bd = new BigDecimal((String)vecItem.get(3));
			 System.out.println(bd);
			 DecimalFormat format = new DecimalFormat("#.#####");		
			 format.setRoundingMode(RoundingMode.FLOOR);
			 items.setUSERFLD6(format.format(bd));
			//items.setUSERFLD6((String) vecItem.get(3));
			items.setEmpNo((String) vecItem.get(8));
			items.setRefNo((String) vecItem.get(9));
			items.setTranDate((String) vecItem.get(10));
			items.setReasonCode((String) vecItem.get(11));
			items.setRemarks((String) vecItem.get(12));
			items.setEmpName((String) vecItem.get(13));
			items.setLoc((String) vecItem.get(14));
			items.setFromLoc((String) vecItem.get(14));
			items.setToLoc((String) vecItem.get(15));
			items.setLocDesc((String) vecItem.get(16));
			items.setToLocDesc((String) vecItem.get(17));
			items.setSTKUOM((String) vecItem.get(18));
			if (fgsttax > 0) {
				items.setGSTTAX(fgsttax);
			}
			vecRec.addElement(items);

		}
		return vecRec;
	}
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}

}

		