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
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerAttachDAO;
import com.track.dao.ItemSesBeanDAO;
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
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.encryptBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/customer/*")
public class CustomerServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.CUSTOMERBEANDAO_PRINTPLANTMASTERLOG;
	DateUtils dateutils = new DateUtils();
	CustUtil _custUtil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_custUtil = new CustUtil();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		JSONObject objectResult = new JSONObject();
		
		if(action.equalsIgnoreCase("Save")) {
			objectResult = customerUpdate(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(objectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
if(action.equalsIgnoreCase("summary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String res = StrUtils.fString(request.getParameter("result"));
				String pgact = StrUtils.fString(request.getParameter("PGaction"));
				request.setAttribute("Msg", msg);
				request.setAttribute("result", res);
				request.setAttribute("PGaction", pgact);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/custmerSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
if(action.equalsIgnoreCase("new")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/customer_view.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if (action.equalsIgnoreCase("AssignCus")) {
	try {
			String result = assignUserCustomer(request, response);
			response.sendRedirect("../customer/assignuser?action=SHOW_RESULT&result="+ result);
	} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR+ ">Exception : " + ex.getMessage() + "</font>";
			response.sendRedirect("../customer/assignuser?action=SHOW_RESULT_VALUE&result="+ result);
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
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/custmerSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if (action.equalsIgnoreCase("AssignCus")) {
			try {
					String result = assignUserCustomer(request, response);
					response.sendRedirect("../customer/assignuser?action=SHOW_RESULT&result="+ result);
			} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);
					String result = "<font class = " + IConstants.FAILED_COLOR+ ">Exception : " + ex.getMessage() + "</font>";
					response.sendRedirect("../customer/assignuser?action=SHOW_RESULT_VALUE&result="+ result);
			}
		}
	if(action.equalsIgnoreCase("new")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/customer_view.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("assignuser")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/UserCustomer.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	else if(action.equalsIgnoreCase("Auto-ID"))
	{
			String minseq="";  String sBatchSeq="",sCustCode="",sUserId="";
			boolean insertFlag=false; String sZero="";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
	   //TblControlDAO _TblControlDAO =new TblControlDAO();
	      Hashtable  ht=new Hashtable();
	     
	      String query=" isnull(NXTSEQ,'') as NXTSEQ";
	      ht.put(IDBConstants.PLANT,plant);
	      ht.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
	      try{
	      		boolean exitFlag=false; boolean resultflag=false;
	      		exitFlag=_TblControlDAO.isExisit(ht,"",plant);
	     
	     
	    	 	if (exitFlag==false)
	      		{ 
	                    
	            	Map htInsert=null;
	            	Hashtable htTblCntInsert  = new Hashtable();
	           
	            	htTblCntInsert.put(IDBConstants.PLANT,plant);
	          
	            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
	            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"C");
	             	htTblCntInsert.put("MINSEQ","0000");
	             	htTblCntInsert.put("MAXSEQ","9999");
	            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
	            	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
	            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
	            
	        		sCustCode="C"+"0001";
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
	           		htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
	           		htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"C");
	           		StringBuffer updateQyery=new StringBuffer("set ");
	           		updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
	         

	        		//boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
	              	sCustCode="C"+sZero+updatedSeq;
	      		}
				json.put("CUSTOMER", sCustCode);
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
		
	if(action.equalsIgnoreCase("edit")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_customer.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	
	if(action.equalsIgnoreCase("import")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importCustomerExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
if(action.equalsIgnoreCase("discount")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/customerdiscountsummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

if(action.equalsIgnoreCase("importdiscount")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/importCustomerDiscountExcelSheet.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
		
}
	private String assignUserCustomer(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException,Exception {
		String result = "";
		String[] chkdDoNo = request.getParameterValues("chkdDoNo");
	
		try {
	        userBean _userBean      = new userBean();
		    _userBean.setmLogger(mLogger);
		    LocUtil locUtil = new LocUtil();
	        locUtil.setmLogger(mLogger);
                        
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");
			boolean Flag ; 
	        String AssignedUser = StrUtils.fString(request.getParameter("UI_PKEY")).trim();
	        if(AssignedUser.length() >0) {
	        boolean flag = _userBean.isValidUserUIPKEYID(AssignedUser,plant); 
			Flag = _custUtil.deleteUserCus(AssignedUser,plant);
			if(flag){
			for (int i = 1; i < chkdDoNo.length; i++) {
            String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
            AssignedLoc = chkdDoNo[i];
                        
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("USERID", AssignedUser);
			ht.put("CUSTNO", AssignedLoc);
			ht.put("CRAT", dateutils.getDateTime());
			ht.put("CRBY", user);
		    request.getSession().setAttribute("userAssignedLoc", ht);
			
			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", "USERCUSTOMER");
			htm.put("RECID", "");
			htm.put("CUSTNO", AssignedLoc);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
            
			flag = _custUtil.addUserCust(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">User Assigned to Customer Successfully</font>";
			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> User Assigned to Customer failed</font>";
			}
            } 
		    System.out.println("User flag ::"+flag);
			}else{
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> Enter/Select Valid User </font>"; 
			}
	        }
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	public JSONObject customerUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		CustUtil custUtil = new CustUtil();
		TblControlDAO _TblControlDAO = new TblControlDAO();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		StrUtils strUtils = new StrUtils();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
				String result = "", strpath = "", catlogpath = "";
				List<Hashtable<String,String>> customerAttachmentList = null;
				List<Hashtable<String,String>> customerAttachmentInfoList = null;
				DateUtils dateutils = new DateUtils();
				CustomerAttachDAO customerAttachDAO = new CustomerAttachDAO();
				customerAttachmentList = new ArrayList<Hashtable<String,String>>();
				customerAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				String COMP_INDUSTRY = _PlantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
				boolean imageSizeflg = false;
				List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List customeruserid = new ArrayList(), customerusername = new ArrayList(),customeruserhpno = new ArrayList(),customeruseremail = new ArrayList(), customerpassword = new ArrayList(), customerappaccess = new ArrayList();
				int customeruseridCount  = 0, customerusernameCount  = 0,customeruserhpnoCount  = 0,customeruseremailCount  = 0, customerpasswordCount=0, customerappaccessCount=0;
				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);

				String res = "";			
				String sCustCode  = "",sCustName  = "",APP_SHOWBY_PRODUCT  = "",APP_SHOWBY_CATEGORY  = "",SHOWSALESBYPURCHASECOST = "",PEPPOL="",PEPPOL_ID="",DOB="",NATIONALITY="",companyregnumber="",
					       sCustNameL  = "",sAddr1     = "",sAddr2     = "",
					       sAddr3     = "", sAddr4     = "",sState   = "",
					       sCountry   = "",sZip       = "",sCons      = "Y",ADDONCOSTTYPE="";
					       
					String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",sPayTerms="",sPayMentTerms="",sPayInDays="",customertypeid="",desc="",customerstatusid="",statusdesc="",sRcbno="";
					String CREDITLIMIT="",ISCREDITLIMIT="",CREDIT_LIMIT_BY="",CUST_ADDONCOST="";
					String CUSTOMEREMAIL="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",OPENINGBALANCE="",WORKPHONE="",sTAXTREATMENT="";
					String sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",CURRENCY="",transport="",sBRANCH="";

					String shipContactName="",shipDesgination="",shipWORKPHONE="",shipHpNo="",shipEmail="",shipCountry="",shipAddr1="",shipAddr2="",shipAddr3="",shipAddr4="",shipState="",shipZip="",SameAsContactAddress="";

				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();					
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("CUST_CODE")) {
							sCustCode = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CUST_NAME")) {
							sCustName = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							CURRENCY = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("companyregnumber")) {
							companyregnumber = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DOB")) {
							DOB = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("NATIONALITY")) {
							NATIONALITY = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
							transport = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("L_CUST_NAME")) {
							sCustNameL = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("ADDR1")) {
							sAddr1 = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("ADDR2")) {
							sAddr2 = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("ADDR3")) {
							sAddr3 = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("ADDR4")) {
							sAddr4 = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("STATE")) {
							sState = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("COUNTRY")) {
							sCountry = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("ZIP")) {
							sZip = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CONSIGNMENT")) {
							sCons = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_ZIP")) {
							shipZip = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_STATE")) {
							shipState = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_ADDR4")) {
							shipAddr4 = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_ADDR3")) {
							shipAddr3 = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_ADDR2")) {
							shipAddr2 = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_ADDR1")) {
							shipAddr1 = StrUtils.fString(item.getString());
						}						
						if (item.getFieldName().equalsIgnoreCase("SHIP_COUNTRY")) {
							shipCountry = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_EMAIL")) {
							shipEmail = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_HPNO")) {
							shipHpNo = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_WORKPHONE")) {
							shipWORKPHONE = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("SHIP_DESGINATION")) {
							shipDesgination = StrUtils.fString(item.getString());
						}						
						if (item.getFieldName().equalsIgnoreCase("SHIP_CONTACTNAME")) {
							shipContactName = StrUtils.fString(item.getString());
						}						
						if (item.getFieldName().equalsIgnoreCase("CONTACTNAME")) {
							sContactName = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DESGINATION")) {
							sDesgination = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("TELNO")) {
							sTelNo = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("HPNO")) {
							sHpNo = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("FAX")) {
							sFax = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("EMAIL")) {
							sEmail = StrUtils.fString(item.getString());
						}						
						if (item.getFieldName().equalsIgnoreCase("RCBNO")) {
							sRcbno = StrUtils.fString(item.getString());
						}						
						if (item.getFieldName().equalsIgnoreCase("REMARKS")) {
							sRemarks = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("PAYTERMS")) {
							sPayTerms = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("PAYMENT_TERMS")) {
							sPayMentTerms = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("PMENT_DAYS")) {
							sPayInDays = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CUSTOMER_TYPE_ID")) {
							customertypeid = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CREDITLIMIT")) {
							CREDITLIMIT = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("ISCREDITLIMIT")) {
							ISCREDITLIMIT = ((StrUtils.fString(item.getString()) != null) ? "1": "0").trim();
						}						
						if (item.getFieldName().equalsIgnoreCase("CUST_ADDONCOST")) {
							CUST_ADDONCOST = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("APP_SHOWBY_PRODUCT")) {
							APP_SHOWBY_PRODUCT = ((StrUtils.fString(item.getString()) != null) ? "1": "0").trim();
						}
						if (item.getFieldName().equalsIgnoreCase("SameAsContactAddress")) {
							SameAsContactAddress = ((StrUtils.fString(item.getString()) != null) ? "1": "0").trim();
						}
						if (item.getFieldName().equalsIgnoreCase("APP_SHOWBY_CATEGORY")) {
							APP_SHOWBY_CATEGORY = ((StrUtils.fString(item.getString()) != null) ? "1": "0").trim();
						}
						
						if (item.getFieldName().equalsIgnoreCase("SHOWSALESBYPURCHASECOST")) {
//							SHOWSALESBYPURCHASECOST = ((StrUtils.fString(item.getString()) != null) ? "1": "0").trim();
							SHOWSALESBYPURCHASECOST = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("ADDONCOSTTYPE")) {
							ADDONCOSTTYPE = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("PEPPOL")) {
							PEPPOL = ((StrUtils.fString(item.getString()) != null) ? "1": "0").trim();
						}
						if (item.getFieldName().equalsIgnoreCase("CREDIT_LIMIT_BY")) {
							CREDIT_LIMIT_BY = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CUSTOMEREMAIL")) {
							CUSTOMEREMAIL = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("WEBSITE")) {
							WEBSITE = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("PEPPOL_ID")) {
							PEPPOL_ID = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("FACEBOOK")) {
							FACEBOOK = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("TWITTER")) {
							TWITTER = StrUtils.fString(item.getString());
						}						
						if (item.getFieldName().equalsIgnoreCase("LINKEDIN")) {
							LINKEDIN = StrUtils.fString(item.getString());
						}						
						if (item.getFieldName().equalsIgnoreCase("SKYPE")) {
							SKYPE = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("OPENINGBALANCE")) {
							OPENINGBALANCE = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("WORKPHONE")) {
							WORKPHONE = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("TAXTREATMENT")) {
							sTAXTREATMENT = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CUSTOMER_STATUS_ID")) {
							customerstatusid = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("BANKNAME")) {
							sBANKNAME = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("BRANCH")) {
							sBRANCH = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("IBAN")) {
							sIBAN = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("BANKROUTINGCODE")) {
							sBANKROUTINGCODE = StrUtils.fString(item.getString());
						}
						
						
						
						if (item.getFieldName().equalsIgnoreCase("USER_ID")) {
							customeruserid.add(customeruseridCount, StrUtils.fString(item.getString()).trim());
							customeruseridCount++;
						}
						if (item.getFieldName().equalsIgnoreCase("USER_NAME")) {
							customerusername.add(customerusernameCount,StrUtils.fString(item.getString()).trim());
							customerusernameCount++;
						}
						if (item.getFieldName().equalsIgnoreCase("USER_HPNO")) {
							customeruserhpno.add(customeruserhpnoCount,StrUtils.fString(item.getString()).trim());
							customeruserhpnoCount++;
						}
						if (item.getFieldName().equalsIgnoreCase("USER_EMAIL")) {
							customeruseremail.add(customeruseremailCount,StrUtils.fString(item.getString()).trim());
							customeruseremailCount++;
						}
						if (item.getFieldName().equalsIgnoreCase("PASSWORD")) {
							customerpassword.add(customerpasswordCount, StrUtils.fString(item.getString()).trim());
							customerpasswordCount++;
						}						
						if (item.getFieldName().equalsIgnoreCase("MANAGER_APP_VAL")) {
							customerappaccess.add(customerappaccessCount, StrUtils.fString(item.getString()).trim());
							customerappaccessCount++;
						}
						
					} else if (!item.isFormField() && (item.getName().length() > 0)) {
						if(item.getFieldName().equalsIgnoreCase("file")){
							String fileLocationATT = "C:/ATTACHMENTS/CUSTOMER" + "/"+ sCustCode;
							String filetempLocationATT = "C:/ATTACHMENTS/CUSTOMER" + "/temp" + "/"+ sCustCode;
							String fileName = StrUtils.fString(item.getName()).trim();
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							
							File path = new File(fileLocationATT);
							if (!path.exists()) {
								path.mkdirs();
							}
							
							//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							File uploadedFile = new File(path + "/" +fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}
							
							item.write(uploadedFile);
							
							// delete temp uploaded file
							File tempPath = new File(filetempLocationATT);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/"+ fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							Hashtable customerAttachment = new Hashtable<String, String>();
							customerAttachment.put("PLANT", plant);
							customerAttachment.put("FILETYPE", item.getContentType());
							customerAttachment.put("FILENAME", fileName);
							customerAttachment.put("FILESIZE", String.valueOf(item.getSize()));
							customerAttachment.put("FILEPATH", fileLocationATT);
							customerAttachment.put("CRAT",dateutils.getDateTime());
							customerAttachment.put("CRBY",username);
							customerAttachment.put("UPAT",dateutils.getDateTime());
							customerAttachmentList.add(customerAttachment);
						}
					}

				}
				ut.begin();
				
				

				if (sCustCode.equals("")) {
					throw new Exception("");
				}
				
				float CREDITLIMITVALUE ="".equals(CREDITLIMIT) ? 0.0f :  Float.parseFloat(CREDITLIMIT);
				float OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
				float CUST_ADDONCOSTVALUE ="".equals(CUST_ADDONCOST) ? 0.0f :  Float.parseFloat(CUST_ADDONCOST);
				
				if (!custUtil.isExistCustomer(sCustCode, plant) && !custUtil.isExistCustomerName(sCustName, plant)) // if
					// the
					// Customer
					// exists
					// already
					{
					boolean chkuserid=false;
					String chkcustomeruserid="";
					if(customeruserid.size() >0) {
					for(int i =0 ; i < customeruserid.size() ; i++){
					chkcustomeruserid =(String)customeruserid.get(i);
					if(!chkcustomeruserid.equalsIgnoreCase("")) {
					if(new CustMstDAO().isExistsCustomerUser(chkcustomeruserid,plant))
					{
						chkuserid=true;
						break;
					
					}
					}
					}					
					}
					if(!chkuserid) {
					if (ISCREDITLIMIT.equals("1")) {
					ISCREDITLIMIT = "Y";
					} else if (ISCREDITLIMIT.equals("0")) {
					ISCREDITLIMIT = "N";
					}
					CREDITLIMITVALUE = "".equals(CREDITLIMIT) ? 0.0f : Float.parseFloat(CREDITLIMIT);
					CREDITLIMIT = StrUtils.addZeroes(CREDITLIMITVALUE, numberOfDecimal);
					
					CUST_ADDONCOSTVALUE = "".equals(CUST_ADDONCOST) ? 0.0f : Float.parseFloat(CUST_ADDONCOST);
					OPENINGBALANCEVALUE = "".equals(OPENINGBALANCE) ? 0.0f : Float.parseFloat(OPENINGBALANCE);
					OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
					Hashtable ht = new Hashtable();
					ht.put(IConstants.PLANT, plant);
					ht.put(IConstants.CUSTOMER_CODE, sCustCode);
					ht.put("USER_ID", sCustCode);
					ht.put(IConstants.CUSTOMER_NAME, sCustName);
					ht.put(IConstants.CUSTOMER_LAST_NAME, sCustNameL);
					ht.put(IConstants.companyregnumber,companyregnumber);
					ht.put("DATEOFBIRTH",DOB);
					ht.put("NATIONALITY",NATIONALITY);
					ht.put("CURRENCY_ID", CURRENCY);
					ht.put(IConstants.ADDRESS1, sAddr1);
					ht.put(IConstants.ADDRESS2, sAddr2);
					ht.put(IConstants.ADDRESS3, sAddr3);
					ht.put(IConstants.ADDRESS4, sAddr4);
					ht.put(IConstants.COUNTRY, sCountry);
					if(sState.equalsIgnoreCase("Select State"))
						sState="";
					ht.put(IConstants.STATE, sState);
					ht.put(IConstants.ZIP, sZip);
					ht.put(IConstants.USERFLG1, sCons);
					ht.put(IConstants.NAME, sContactName);
					ht.put(IConstants.DESGINATION, sDesgination);
					ht.put(IConstants.TELNO, sTelNo);
					ht.put(IConstants.HPNO, sHpNo);
					ht.put(IConstants.FAX, sFax);
					ht.put(IConstants.EMAIL, sEmail);
					ht.put(IConstants.TRANSPORTID, transport);
					
					ht.put(IConstants.SHIP_CONTACTNAME,shipContactName);
			          ht.put(IConstants.SHIP_DESGINATION,shipDesgination);
			          ht.put(IConstants.SHIP_WORKPHONE,shipWORKPHONE);
			          ht.put(IConstants.SHIP_HPNO,shipHpNo);
			          ht.put(IConstants.SHIP_EMAIL,shipEmail);
			          ht.put(IConstants.SHIP_COUNTRY_CODE,shipCountry);
			          ht.put(IConstants.SHIP_ADDR1,shipAddr1);
			          ht.put(IConstants.SHIP_ADDR2,shipAddr2);
			          ht.put(IConstants.SHIP_ADDR3,shipAddr3);
			          ht.put(IConstants.SHIP_ADDR4,shipAddr4);
			          if(shipState.equalsIgnoreCase("Select State"))
			        	  shipState="";
			          ht.put(IConstants.SHIP_STATE,shipState);
			          ht.put(IConstants.SHIP_ZIP,shipZip);
					
					ht.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
					ht.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
					ht.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
					ht.put(IConstants.PAYINDAYS, sPayInDays);
					ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
					ht.put(IConstants.CREATED_BY, username);
					ht.put(IConstants.ISACTIVE, "Y");
					ht.put(IConstants.CUSTOMERTYPEID, customertypeid);
					ht.put(IConstants.CUSTOMERSTATUSID, customerstatusid);
					ht.put(IConstants.RCBNO, sRcbno);
					ht.put("CREDITLIMIT", CREDITLIMIT);
					/*ht.put("ISCREDITLIMIT", ISCREDITLIMIT);*/
				//	if(COMP_INDUSTRY.equals("Education")){
					if(CREDIT_LIMIT_BY.equalsIgnoreCase("")) {
						String crdlimtby = "NOLIMIT";
						ht.put("CREDIT_LIMIT_BY",crdlimtby);
					}else {
					ht.put("CREDIT_LIMIT_BY",CREDIT_LIMIT_BY);
					}
			//		}
					ht.put("CUST_ADDONCOST", CUST_ADDONCOST);
					ht.put("ISSHOWBYPRODUCT",APP_SHOWBY_PRODUCT);
					ht.put("ISSAMEASCONTACTADD",SameAsContactAddress);
					ht.put("ISSHOWAPPCATEGORYIMAGE",APP_SHOWBY_CATEGORY);
					ht.put("SHOWSALESBYPURCHASECOST", SHOWSALESBYPURCHASECOST);
					ht.put("ADDONCOSTTYPE", ADDONCOSTTYPE);
					ht.put("ISPEPPOL",PEPPOL);
					ht.put("PEPPOL_ID",PEPPOL_ID);
					ht.put(IConstants.CUSTOMEREMAIL, CUSTOMEREMAIL);
					ht.put(IConstants.WEBSITE, WEBSITE);
					ht.put(IConstants.FACEBOOK, FACEBOOK);
					ht.put(IConstants.TWITTER, TWITTER);
					ht.put(IConstants.LINKEDIN, LINKEDIN);
					ht.put(IConstants.SKYPE, SKYPE);
					ht.put(IConstants.OPENINGBALANCE, OPENINGBALANCE);
					ht.put(IConstants.WORKPHONE, WORKPHONE);
					ht.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
					sBANKNAME="";
					ht.put(IDBConstants.BANKNAME,sBANKNAME);
					ht.put(IDBConstants.IBAN,sIBAN);
					ht.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
					
					MovHisDAO mdao = new MovHisDAO(plant);
					// mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put(IDBConstants.PLANT, plant);
					htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_CUST);
					htm.put("RECID", "");
					htm.put("ITEM", sCustCode);
					htm.put(IDBConstants.CREATED_BY, username);
					
					if (!sRemarks.equals("")) {
					htm.put(IDBConstants.REMARKS, sCustName + "," + sRemarks);
					} else {
					htm.put(IDBConstants.REMARKS, sCustName);
					}
					
					htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
					
					boolean updateFlag;
					if (sCustCode != "C0001") {
					boolean exitFlag = false;
					Hashtable htv = new Hashtable();
					htv.put(IDBConstants.PLANT, plant);
					htv.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
					exitFlag = _TblControlDAO.isExisit(htv, "", plant);
					if (exitFlag)
					updateFlag = _TblControlDAO.updateSeqNo("CUSTOMER", plant);
					else {
					boolean insertFlag = false;
					Map htInsert = null;
					Hashtable htTblCntInsert = new Hashtable();
					htTblCntInsert.put(IDBConstants.PLANT, plant);
					htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
					htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "C");
					htTblCntInsert.put("MINSEQ", "0000");
					htTblCntInsert.put("MAXSEQ", "9999");
					htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
					htTblCntInsert.put(IDBConstants.CREATED_BY, username);
					htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
					insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
					}
					}
					
					boolean custInserted = custUtil.insertCustomer(ht);
					boolean inserted = mdao.insertIntoMovHis(htm);
					
					//added by imthi 29-06-2022 ->DESC: get customer code and insert with item from itemmst tbl
			          CustUtil custUtils = new CustUtil(); 
			          CustMstDAO custMstDAO = new CustMstDAO(); 
			          ItemSesBeanDAO ItemSesBeanDAO = new ItemSesBeanDAO();
			          List movQryLists =ItemSesBeanDAO.getitemWithCust("",plant,"");
			          if (movQryLists.size() > 0) {
			  			for(int i =0; i<movQryLists.size(); i++) {
			  					Map arrCustLine = (Map)movQryLists.get(i);
			  					String item=(String)arrCustLine.get("ITEM");
			  					String itemdesc=(String)arrCustLine.get("ITEMDESC");
			  						Hashtable apporderht = new Hashtable();
			  						apporderht.put(IConstants.PLANT,plant);
			  						apporderht.put("CUSTNO",sCustCode);
			  						apporderht.put(IConstants.ITEM,item);
			  						apporderht.put(IConstants.ITEM_DESC,itemdesc);
			  						apporderht.put("ORDER_QTY","0");
			  						apporderht.put("MAX_ORDER_QTY","0");
			  						apporderht.put("CRBY",username);
			  						apporderht.put("CRAT",dateutils.getDateTime());
			  						boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
			  			}
			          }
			          
					if (custInserted && inserted) {

				for(int i =0 ; i < customeruserid.size() ; i++){
					Hashtable customeruser = new Hashtable<String, String>();

					String sPassword= (String)customerpassword.get(i);
					if(sPassword != ""){
						String enpPwd   = new encryptBean().encrypt(sPassword);
						sPassword=enpPwd;
			          }
					
					customeruser.put("PLANT", plant);
					customeruser.put("CUSTNO", sCustCode);
					customeruser.put("CUSTDESC", sCustName);
					customeruser.put("USER_ID", (String)customeruserid.get(i));
					customeruser.put("PASSWORD", sPassword);
					customeruser.put("USER_NAME", (String)customerusername.get(i));
					customeruser.put("USER_HPNO", (String)customeruserhpno.get(i));
					customeruser.put("USER_EMAIL", (String)customeruseremail.get(i));
					customeruser.put("ISMANAGERAPPACCESS", (String)customerappaccess.get(i));
					customeruser.put("CRAT",dateutils.getDateTime());
					customeruser.put("CRBY",username);
					customeruser.put("UPAT",dateutils.getDateTime());
					boolean itemInserted =new CustMstDAO().addcustomerUser(customeruser, plant);
				}
				
				int attchSize = customerAttachmentList.size();
				if(attchSize>0) {
				  for(int i =0 ; i < attchSize ; i++){
						Hashtable customerAttachmentat = new Hashtable<String, String>();
						customerAttachmentat = customerAttachmentList.get(i);
						customerAttachmentat.put("CUSTNO", sCustCode);
						customerAttachmentInfoList.add(customerAttachmentat);
				  }
				  boolean itemInserted = customerAttachDAO.addcustomerAttachments(customerAttachmentInfoList, plant);
				}
					DbBean.CommitTran(ut);
					result = "Customer Added successfully";
					resultObj.put("customer", sCustCode);
					resultObj.put("message", result);
					resultObj.put("ERROR_CODE", 100);
					//response.sendRedirect("../customer/summary?PGaction=View&result=Customer Added Successfully");
				} else {
					DbBean.RollbackTran(ut);
					result = "Failed to add New Customer";
					resultObj.put("customer", sCustCode);
					resultObj.put("message", result);
					resultObj.put("ERROR_CODE", 99);
				}
				
					} else {
						DbBean.RollbackTran(ut);
						result = "User ID "+chkcustomeruserid+" Exists already. Try again with diffrent User ID.";
						resultObj.put("customer", sCustCode);
						resultObj.put("message", result);
						resultObj.put("ERROR_CODE", 98);
						}
					
					} else {
						DbBean.RollbackTran(ut);
						result = "Customer ID Or Name Exists already. Try again with diffrent Customer ID Or Name.";
						resultObj.put("customer", sCustCode);
						resultObj.put("message", result);
						resultObj.put("ERROR_CODE", 98);
						}

			} catch (Exception e) {

			}
			//resultJSON.put("result", resultObj);
		}
		return resultObj;
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

		