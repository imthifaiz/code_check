package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
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
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.LocMstTwoDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.db.util.CustUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.LocTypeUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/product/*")
public class ProductServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ITEMSESBEANDAO_PRINTPLANTMASTERLOG;
	DateUtils dateutils = new DateUtils();
	ItemUtil _custUtil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_custUtil = new ItemUtil();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
	if(action.equalsIgnoreCase("summary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String res = StrUtils.fString(request.getParameter("result"));
				String pgact = StrUtils.fString(request.getParameter("PGaction"));
				request.setAttribute("Msg", msg);
				request.setAttribute("result", res);
				request.setAttribute("PGaction", pgact);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/itemSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if (action.equalsIgnoreCase("AssignPrd")) {
		try {
				String result = assignCustPrd(request, response);
				response.sendRedirect("../product/Custproduct?action=SHOW_RESULT&result="+ result);
		} catch (Exception ex) {
				this.mLogger.exception(this.printLog, "", ex);
				String result = "<font class = " + IConstants.FAILED_COLOR+ ">Exception : " + ex.getMessage() + "</font>";
				response.sendRedirect("../product/Custproduct?action=SHOW_RESULT_VALUE&result="+ result);
		}
	}
if(action.equalsIgnoreCase("new")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String result = StrUtils.fString(request.getParameter("result"));
			request.setAttribute("Msg", msg);
			request.setAttribute("result", result);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/item_view.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

if(action.equalsIgnoreCase("edit")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_item.jsp?action=UPDATE");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("delete")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_item.jsp?action=DELETE");
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
		
	if(action.equalsIgnoreCase("summary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String res = StrUtils.fString(request.getParameter("result"));
				String pgact = StrUtils.fString(request.getParameter("PGaction"));
				request.setAttribute("Msg", msg);
				request.setAttribute("result", res);
				request.setAttribute("PGaction", pgact);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/itemSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if (action.equalsIgnoreCase("AssignPrd")) {
		try {
				String result = assignCustPrd(request, response);
				response.sendRedirect("../product/Custproduct?action=SHOW_RESULT&result="+ result);
		} catch (Exception ex) {
				this.mLogger.exception(this.printLog, "", ex);
				String result = "<font class = " + IConstants.FAILED_COLOR+ ">Exception : " + ex.getMessage() + "</font>";
				response.sendRedirect("../product/Custproduct?action=SHOW_RESULT_VALUE&result="+ result);
		}
	}
	
	
	
	if(action.equalsIgnoreCase("new")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String result = StrUtils.fString(request.getParameter("result"));
			request.setAttribute("Msg", msg);
			request.setAttribute("result", result);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/item_view.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	else if(action.equalsIgnoreCase("auto-id")){
		TblControlDAO _TblControlDAO = new TblControlDAO();
		JSONObject json = new JSONObject();
	  String sItemDesc  = "";
	  String sItem  = "";
	  String  sArtist   = "";
	  String prd_cls_id="";
	  String prdBrand = "";
	  String sTitle  = "";
	  String  sRemark="";
	  String sItemCondition=""; 
	  String stkqty=""; 
	  String price="0.00";
	  String cost="0.00";
	  String maxstkqty="";
	  String minsprice="";
	  String discount="";
	  String sUOM="";
	  String NETWEIGHT="0.00";
	  String GROSSWEIGHT="0.00";
	  String  HSCODE="";
	  String COO="";
	  String VINNO="";
	  String MODEL="";
	  String RENTALPRICE="0.00"; 
	  String SERVICEPRICE="0.00";
	  String PURCHASEUOM="";
	  String SALESUOM="";
	  String RENTALUOM="";
	  String SERVICEUOM="";
	  String INVENTORYUOM="";
	  String ISBASICUOM="";
		
	  String sAddEnb    = "enabled";
	  String sItemEnb   = "enabled";
	    String minseq="", sBatchSeq="",sZero="";
	   
	    boolean insertFlag=false;
	 // 	TblControlDAO _TblControlDAO =new TblControlDAO();
   //	_TblControlDAO.setmLogger(mLogger);
//	 String prdclassid = request.getParameter("PRD_CLS_ID");
	    Hashtable  ht=new Hashtable();
	    String query=" isnull(NXTSEQ,'') as NXTSEQ";
	    ht.put(IDBConstants.PLANT,plant);
	    ht.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
	    try{
	       boolean exitFlag=false; boolean resultflag=false;
	       exitFlag=_TblControlDAO.isExisit(ht,"",plant);
	   
	     //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
	      if (exitFlag==false)
	      {         
	            Map htInsert=null;
	            Hashtable htTblCntInsert  = new Hashtable();
	            htTblCntInsert.put(IDBConstants.PLANT,plant);
	            htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
	            htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"P");
	            htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"0000");
	            htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"9999");
	            htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
	            htTblCntInsert.put(IDBConstants.CREATED_BY, username);
	            htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	            insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
	             sItem="P"+"0001";
	      }
	      else
	      {
	           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
	           Map m= _TblControlDAO.selectRow(query, ht,"");
	           sBatchSeq=(String)m.get("NXTSEQ");
	           int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
	           String updatedSeq=Integer.toString(inxtSeq);
	            if(updatedSeq.length()==1)
	           {
	             sZero="000";
	           }
	           else if(updatedSeq.length()==2)
	           {
	             sZero="00";
	           }
	           else if(updatedSeq.length()==3)
	           {
	             sZero="0";
	           }
	            Map htUpdate = null;
	           Hashtable htTblCntUpdate = new Hashtable();
	           htTblCntUpdate.put(IDBConstants.PLANT,plant);
	           htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
	           htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"P");
	           StringBuffer updateQyery=new StringBuffer("set ");
	           updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
	        //   boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
	           sItem="P"+sZero+updatedSeq;
	        }
	      json.put("PRODUCT", sItem);
			response.setStatus(200);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	if(action.equalsIgnoreCase("detail")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/itemDetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("edit")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_item.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("delete")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_item.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	if(action.equalsIgnoreCase("u-cloproduct")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importItemExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	if(action.equalsIgnoreCase("importitemsupplier")) {	
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importItemSupplierExcelSheet.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	if(action.equalsIgnoreCase("importminmax")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importPrdMinMaxQtyExcel.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("importoutletminmax")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importPrdOutletMinMaxQtyExcel.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//imti added for addtional IMPORT[desc,img,product]
	if(action.equalsIgnoreCase("additional")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importAddtionalExcelSheet.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("importshopee")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ImportShopee.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

if(action.equalsIgnoreCase("shopifyproduct")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importshopifyproduct.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	if(action.equalsIgnoreCase("alternateprodsummary")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/AlternetItemSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("importalterprd")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importAltrPrdExcel.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("Custproduct")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/UserProduct.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

		
}
	
	private String assignCustPrd(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String result = "";
		String[] chkdDoNo = request.getParameterValues("chkdDoNo");
	
		try {
	        
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");
			boolean Flag ; 
	        String customer = StrUtils.fString(request.getParameter("CUSTNO")).trim();
	        boolean flag = new CustomerBeanDAO().isValidCustno(customer,plant); 
			Flag = _custUtil.deleteCustPrd(customer,plant);
			if(flag){
			for (int i = 1; i < chkdDoNo.length; i++) {
            String Assignedprd = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
            Assignedprd = chkdDoNo[i];
                        
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("CUSTNO", customer);
			ht.put("ITEM", Assignedprd);
			ht.put("CRAT", dateutils.getDateTime());
			ht.put("CRBY", user);
		    request.getSession().setAttribute("userAssignedLoc", ht);
			
			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", "CUSTOMERPRODUCT");
			htm.put("RECID", "");
			htm.put("ITEM", Assignedprd);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
            
			flag = _custUtil.addCustPrd(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Customer Assignn to Product Successfully</font>";
			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> Customer Assignn to Product failed</font>";
			}
            } 
		    System.out.println("User flag ::"+flag);
			}else{
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> Enter/Select Valid Customer </font>"; 
			}
		} catch (Exception e) {
		}
		return result;
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
	private MLogger mLogger = new MLogger();

}

		