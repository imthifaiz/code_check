package com.track.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.MovHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.BillUtil;
import com.track.db.util.CoaUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PrdBrandUtil;
import com.track.db.util.BankUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.dao.CoaDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.JournalDAO;
import com.track.gates.DbBean;

/**
 * Servlet implementation class BankServlet
 */
public class BankServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.BANKServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.BANKServlet_PRINTPLANTMASTERINFO;
	String action = "";
	TblControlDAO _TblControlDAO = null;
	BankUtil bankutil = null;
	DateUtils dateutils = null;
	ItemMstDAO itemmstdao = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 _TblControlDAO = new TblControlDAO();
		 //prdbrandutil = new PrdBrandUtil();
		 bankutil = new BankUtil();
		 dateutils = new DateUtils();
		 itemmstdao = new ItemMstDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			HttpSession session = request.getSession();
			
			action = StrUtils.fString(request.getParameter("action")).trim();			
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			//String view= request.getParameter("view").trim();
					
			//String action1 = request.getParameter("view").trim();
			String BANK_NAME = null, BANK_BRANCH = null,BANK_BRANCH_CODE = null,productBrandID= null,productBrandDesc=null;
			String IFSC =null,SWIFT_CODE=null,TELNO=null,FAX=null,EMAIL=null,WEBSITE=null;
			String FACEBOOKID=null,TWITTERID=null,LINKEDINID=null,UNITNO=null,BUILDING=null,STREET=null,CITY=null,STATE=null,COUNTRY=null;
			String ZIP=null,NOTE=null,CONTACT_PERSON=null,HPNO=null;
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equals("Auto_ID")) {
				BANK_NAME = this.generateBrandID(plant, userName);
				response.sendRedirect("jsp/create_prdBrand.jsp?brandID="
						+ BANK_NAME);

			}
			if (action.equals("ADD")) {
				BANK_NAME = request.getParameter("BANK_NAME");
				BANK_BRANCH = request.getParameter("BANK_BRANCH");
				BANK_BRANCH_CODE = request.getParameter("BANK_BRANCH_CODE");
				IFSC = request.getParameter("IFSC");
				SWIFT_CODE = request.getParameter("SWIFT_CODE");
				TELNO = request.getParameter("TELNO");
				FAX = request.getParameter("FAX");
				EMAIL = request.getParameter("EMAIL");
				WEBSITE = request.getParameter("WEBSITE");
				
				UNITNO = request.getParameter("UNITNO");
				BUILDING = request.getParameter("BUILDING");
				STREET = request.getParameter("STREET");
				STATE = request.getParameter("STATE");
				COUNTRY = request.getParameter("COUNTRY");
				ZIP = request.getParameter("ZIP");
				CITY = request.getParameter("CITY");
				FACEBOOKID = request.getParameter("FACEBOOKID");
				TWITTERID = request.getParameter("TWITTERID");
				LINKEDINID = request.getParameter("LINKEDINID");
				NOTE = request.getParameter("NOTE");
				CONTACT_PERSON = request.getParameter("CONTACT_PERSON");
				HPNO = request.getParameter("HPNO");
				String modal = StrUtils.fString(request.getParameter("MODAL"));
				String responseMsg = this.addBrand(plant, userName,BANK_NAME, BANK_BRANCH,BANK_BRANCH_CODE,IFSC,SWIFT_CODE,TELNO,FAX,EMAIL,WEBSITE,UNITNO,BUILDING,STATE,STREET,COUNTRY,CITY,ZIP,FACEBOOKID,TWITTERID,LINKEDINID,NOTE,CONTACT_PERSON,HPNO);
				//String responseMsg = this.addBank(plant, userName,BANK_NAME, BANK_BRANCH);
				if(responseMsg.equals("<font class = maingreen>Bank Details Added Successfully</font>")) {
					CoaUtil coaUtil = new CoaUtil();
					CoaDAO coaDAO = new CoaDAO();
					String accname = BANK_NAME+"-"+BANK_BRANCH_CODE;
									
					Hashtable<String, String> accountHt = new Hashtable<>();
					accountHt.put("PLANT", plant);
					accountHt.put("ACCOUNTTYPE", "3");
					accountHt.put("ACCOUNTDETAILTYPE", "8");
					accountHt.put("ACCOUNT_NAME", accname);
					accountHt.put("DESCRIPTION", "");
					accountHt.put("ISSUBACCOUNT", "0");
					accountHt.put("SUBACCOUNTNAME", "");
					accountHt.put("OPENINGBALANCE", "");
					accountHt.put("OPENINGBALANCEDATE", "");
					accountHt.put("CRAT", dateutils.getDateTime());
					accountHt.put("CRBY", userName);
					accountHt.put("UPAT", dateutils.getDateTime());

					String gcode = coaDAO.GetGCodeById("3", plant);
					String dcode = coaDAO.GetDCodeById("8", plant);
					List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, "3", "8");
					String maxid = "";
					String atcode = "";
					if(listQry.size() > 0) {
						for (int i = 0; i < listQry.size(); i++) {
							Map<String, String> m = listQry.get(i);
							maxid = m.get("CODE");
						}
					
						int count = Integer.valueOf(maxid);
						atcode = String.valueOf(count+1);
						if(atcode.length() == 1) {
							atcode = "0"+atcode;
						}
					}else {
						atcode = "01";
					}
					String accountCode = gcode+"-"+dcode+atcode;
					accountHt.put("ACCOUNT_CODE", accountCode);
					accountHt.put("CODE", atcode);
					coaUtil.addAccount(accountHt, plant);
				}
				if(modal.equalsIgnoreCase("Y")) {
					JSONObject resultJson = new JSONObject();
					if(responseMsg.equals("<font class = maingreen>Bank Details Added Successfully</font>")) {
						resultJson.put("MESSAGE", "Bank Details Added Successfully");
						resultJson.put("BANK_NAME", BANK_NAME);
						resultJson.put("BANK_BRANCH", BANK_BRANCH);
						resultJson.put("BANK_BRANCH_CODE", BANK_BRANCH_CODE);		 
						resultJson.put("STATUS", "SUCCESS");
					}else {
						resultJson.put("MESSAGE", "Failed to add Bank Details");		 
						resultJson.put("STATUS", "FAIL");
					}			
					
					response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(resultJson.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
				}else {
				if(responseMsg.equals("<font class = maingreen>Bank Details Added Successfully</font>"))
					/*response.sendRedirect("jsp/BankSummary.jsp?response="+""+"&BANK_BRANCH_CODE="+BANK_BRANCH_CODE);*/
					response.sendRedirect("../banking/summary?response="+responseMsg);
				else
				response.sendRedirect("../banking/new?response="
						+ responseMsg+"&BANK_NAME="+BANK_NAME+"&BANK_BRANCH="+BANK_BRANCH+"&BANK_BRANCH_CODE="+BANK_BRANCH_CODE+"&WEBSITE="+WEBSITE
						+"&IFSC="+IFSC+"&SWIFT_CODE="+SWIFT_CODE+"&TELNO="+TELNO+"&FAX="+FAX+"&EMAIL="+EMAIL+"&UNITNO="+UNITNO+"&BUILDING="+BUILDING
						+"&UNITNO="+UNITNO+"&BUILDING="+BUILDING+"&STREET="+STREET+"&STATE="+STATE+"&COUNTRY="+COUNTRY+"&ZIP="+ZIP+"&CITY="+CITY
						+"&FACEBOOKID="+FACEBOOKID+"&TWITTERID="+TWITTERID+"&LINKEDINID="+LINKEDINID+"&NOTE="+NOTE+"&CONTACT_PERSON="+CONTACT_PERSON+"&HPNO="+HPNO);
				}
			}
			if(action.equals("UPDATE")){
				BANK_NAME = request.getParameter("BANK_NAME");
				BANK_BRANCH = request.getParameter("BANK_BRANCH");
				BANK_BRANCH_CODE = request.getParameter("BANK_BRANCH_CODE");
				IFSC = request.getParameter("IFSC");
				SWIFT_CODE = request.getParameter("SWIFT_CODE");
				TELNO = request.getParameter("TELNO");
				FAX = request.getParameter("FAX");
				EMAIL = request.getParameter("EMAIL");
				WEBSITE = request.getParameter("WEBSITE");
				UNITNO = request.getParameter("UNITNO");
				BUILDING = request.getParameter("BUILDING");
				STREET = request.getParameter("STREET");
				STATE = request.getParameter("STATE");
				COUNTRY = request.getParameter("COUNTRY");
				ZIP = request.getParameter("ZIP");
				CITY = request.getParameter("CITY");
				FACEBOOKID = request.getParameter("FACEBOOKID");
				TWITTERID = request.getParameter("TWITTERID");
				LINKEDINID = request.getParameter("LINKEDINID");
				NOTE = request.getParameter("NOTE");
				CONTACT_PERSON = request.getParameter("CONTACT_PERSON");
				HPNO = request.getParameter("HPNO");
				// String isActive = request.getParameter("ISACTIVE");
				String responseMsg = this.updateBank(plant, userName,BANK_NAME, BANK_BRANCH,BANK_BRANCH_CODE,IFSC,SWIFT_CODE,TELNO,FAX,EMAIL,WEBSITE,UNITNO,BUILDING,STATE,STREET,COUNTRY,CITY,ZIP,FACEBOOKID,TWITTERID,LINKEDINID,NOTE,CONTACT_PERSON,HPNO);
				if(responseMsg.equals("<font class = maingreen >Bank Details Updated Successfully</font>"))
					response.sendRedirect("../banking/summary?response="+responseMsg);
				else
				response.sendRedirect("../banking/edit?response="
						+ responseMsg+"&BANK_NAME="+BANK_NAME+"&BANK_BRANCH="+BANK_BRANCH+"&BANK_BRANCH_CODE="+BANK_BRANCH_CODE+"&WEBSITE="+WEBSITE
						+"&IFSC="+IFSC+"&SWIFT_CODE="+SWIFT_CODE+"&TELNO="+TELNO+"&FAX="+FAX+"&EMAIL="+EMAIL+"&UNITNO="+UNITNO+"&BUILDING="+BUILDING
						+"&UNITNO="+UNITNO+"&BUILDING="+BUILDING+"&STREET="+STREET+"&STATE="+STATE+"&COUNTRY="+COUNTRY+"&ZIP="+ZIP+"&CITY="+CITY
						+"&FACEBOOKID="+FACEBOOKID+"&TWITTERID="+TWITTERID+"&LINKEDINID="+LINKEDINID+"&NOTE="+NOTE+"&CONTACT_PERSON="+CONTACT_PERSON+"&HPNO="+HPNO);
				
			}
			if(action.equals("DELETE")){
				BANK_NAME = request.getParameter("BANK_NAME");
				BANK_BRANCH = request.getParameter("BANK_BRANCH");
				BANK_BRANCH_CODE = request.getParameter("BANK_BRANCH_CODE");
				IFSC = request.getParameter("IFSC");
				SWIFT_CODE = request.getParameter("SWIFT_CODE");
				TELNO = request.getParameter("TELNO");
				FAX = request.getParameter("FAX");
				EMAIL = request.getParameter("EMAIL");
				WEBSITE = request.getParameter("WEBSITE");				
				UNITNO = request.getParameter("UNITNO");
				BUILDING = request.getParameter("BUILDING");
				STREET = request.getParameter("STREET");
				STATE = request.getParameter("STATE");
				COUNTRY = request.getParameter("COUNTRY");
				ZIP = request.getParameter("ZIP");
				CITY = request.getParameter("CITY");
				FACEBOOKID = request.getParameter("FACEBOOKID");
				TWITTERID = request.getParameter("TWITTERID");
				LINKEDINID = request.getParameter("LINKEDINID");
				NOTE = request.getParameter("NOTE");
				CONTACT_PERSON = request.getParameter("CONTACT_PERSON");
				HPNO = request.getParameter("HPNO");
				JournalDAO journalDAO = new JournalDAO();
				String accname = BANK_NAME+"-"+BANK_BRANCH_CODE;
				boolean accstatus = journalDAO.isAccountExisit(plant, accname);
				String responseMsg = "";
				if(accstatus) {
					response.sendRedirect("../banking/edit?response=<font class = mainred >Unable to delete Bank</font>&BANK_NAME=");
				}else {
					responseMsg = this.deleteBank(plant, userName,
							BANK_BRANCH_CODE);
					
					if(responseMsg.equals("<font class = maingreen >Bank details Deleted Successfully</font>"))
						response.sendRedirect("../banking/summary?response="+responseMsg);
					else
					response.sendRedirect("../banking/edit?response="
							+ responseMsg+"&BANK_NAME=");
				}
				

				
				
			}
			
			
			if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			}
			
			if (action.equalsIgnoreCase("GET_BRANCH_NAME_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getBranchNameForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			}
			
			if (action.equalsIgnoreCase("GET_BRANCH_CODE_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getBranchCodeForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			}
			
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			request.getSession().setAttribute("res", result);
			response.sendRedirect("jsp/displayResult2User.jsp");
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	
	//Generate the next sequence Brand ID for the product -Bruhan.//11/9/12
	private String generateBrandID(String plant, String userName)
			throws ServletException, IOException, Exception {
		String productBrandID = "", sBatchSeq = "", sZero = "";
		boolean exitFlag = false;
		try {
			Hashtable<String, String> ht = new Hashtable<String, String>();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "PRDBRAND");
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);
			if (exitFlag == false) {
				Hashtable<String, String> htTblCntInsert = new Hashtable<String, String>();

				htTblCntInsert.put(IDBConstants.PLANT, plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "PRDBRAND");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PB");
				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "000");
				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, userName);
				htTblCntInsert.put(IDBConstants.CREATED_AT, DateUtils
						.getDateTime());
				_TblControlDAO.insertTblControl(htTblCntInsert,
						plant);

				productBrandID = "PB" + "001";
			} else {
				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "00";
				} else if (updatedSeq.length() == 2) {
					sZero = "0";
				}


				Hashtable<String, String> htTblCntUpdate = new Hashtable<String, String>();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "PRDBRAND");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PB");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				/* _TblControlDAO.update(updateQyery
						.toString(), htTblCntUpdate, "", plant);*/

				productBrandID = "PB" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return productBrandID;

	}
	
		private String addBrand(String plant, String userName,
			String BANK_NAME, String BANK_BRANCH,String BANK_BRANCH_CODE,String IFSC,String SWIFT_CODE,String TELNO,String FAX,String EMAIL,String WEBSITE,
			String UNITNO,String BUILDING,String STATE,String STREET,String COUNTRY,String CITY,String ZIP,String FACEBOOKID,String TWITTERID,String LINKEDINID,String NOTE,String CONTACT_PERSON,String HPNO)
			throws ServletException, IOException, Exception {
		String response = "";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.BANK_BRANCH_CODE, BANK_BRANCH_CODE);
		if (!(bankutil.isExistsItemType(ht))) // if the BANK exists already
		{
			ht.put(IDBConstants.PLANT, plant);
			//ht.put("ID", "1");
			ht.put(IDBConstants.BANK_NAME, BANK_NAME);
			ht.put(IDBConstants.BANK_BRANCH, BANK_BRANCH);
			ht.put(IDBConstants.BANK_BRANCH_CODE, BANK_BRANCH_CODE);
			
			ht.put(IDBConstants.BANK_IFSC, IFSC);
			ht.put(IDBConstants.BANK_SWIFT_CODE, SWIFT_CODE);
			ht.put(IDBConstants.BANK_TELNO, TELNO);
			ht.put(IDBConstants.BANK_FAX, FAX);
			ht.put(IDBConstants.BANK_EMAIL, EMAIL);
			ht.put(IDBConstants.BANK_WEBSITE, WEBSITE);
			
			ht.put(IDBConstants.BANK_UNITNO, UNITNO);
			ht.put(IDBConstants.BANK_BUILDING, BUILDING);
			ht.put(IDBConstants.BANK_STREET, STREET);
			ht.put(IDBConstants.BANK_STATE, STATE);
			ht.put(IDBConstants.BANK_CITY, CITY);
			ht.put(IDBConstants.BANK_COUNTRY, COUNTRY);
			ht.put(IDBConstants.BANK_POSTALCODE, ZIP);
			ht.put(IDBConstants.BANK_FB, FACEBOOKID);
			ht.put(IDBConstants.BANK_TWITTER, TWITTERID);
			ht.put(IDBConstants.BANK_LINKEDIN, LINKEDINID);
			ht.put(IDBConstants.BANK_CONTACTPERSON, CONTACT_PERSON);
			ht.put(IDBConstants.BANK_HPNO, HPNO);
			ht.put(IDBConstants.BANK_NOTE, NOTE);
			ht.put(IConstants.ISACTIVE, "Y");
			new DateUtils();
			ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, userName);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable<String, String> htm = new Hashtable<String, String>();
			htm.put(IDBConstants.PLANT, plant);
			htm.put("DIRTYPE", "CREATE BANK");
			htm.put("RECID", "");
			htm.put("ITEM",BANK_BRANCH_CODE);
			htm.put("REMARKS", BANK_NAME);
			htm.put("UPBY", userName);
			htm.put("CRBY", userName);
			htm.put("CRAT", DateUtils.getDateTime());
			htm.put("UPAT", DateUtils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, DateUtils
					.getDateinyyyy_mm_dd(DateUtils.getDate()));

			 htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
	          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	          
			  boolean updateFlag;
			
			boolean itemInserted = bankutil.insertBankMst(ht);
		//	boolean inserted = mdao.insertIntoMovHis(htm);
			if (itemInserted) {
				response = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">Bank Details Added Successfully</font>";

			} else {
				response = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New Bank Details </font>";

			}
		} else {
			response = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Bank Details Exists already. Try again</font>";

		}
		return response;
	}
	
	
	//Updates the brand to the Brand master table -Bruhan - //11/9/12
	private String updateBank(String plant, String userName,
			String BANK_NAME, String BANK_BRANCH,String BANK_BRANCH_CODE,String IFSC,String SWIFT_CODE,String TELNO,String FAX,String EMAIL,String WEBSITE,
			String UNITNO,String BUILDING,String STATE,String STREET,String COUNTRY,String CITY,String ZIP,String FACEBOOKID,String TWITTERID,String LINKEDINID,String NOTE,String CONTACT_PERSON,String HPNO)
			throws ServletException, IOException, Exception {
		String response = "";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		if(IFSC == null)
			IFSC = "";
		if(SWIFT_CODE == null)
			SWIFT_CODE = "";
		if(TELNO == null)
			TELNO = "";
		if(FAX == null)
			FAX = "";
		if(EMAIL == null)
			EMAIL = "";
		if(WEBSITE == null)
			WEBSITE = "";
		if(UNITNO == null)
			UNITNO = "";
		if(BUILDING == null)
			BUILDING = "";
		if(STREET == null)
			STREET = "";
		if(CITY == null)
			CITY = "";
		if(STATE == null)
			STATE = "";
		if(COUNTRY == null)
			COUNTRY = "";
		if(ZIP == null)
			ZIP = "";
		if(FACEBOOKID == null)
			FACEBOOKID = "";
		if(TWITTERID == null)
			TWITTERID = "";
		if(LINKEDINID == null)
			LINKEDINID = "";
		if(CONTACT_PERSON == null)
			CONTACT_PERSON = "";
		if(HPNO == null)
			HPNO = "";
		if(NOTE == null)
			NOTE = "";
		
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.BANK_BRANCH_CODE, BANK_BRANCH_CODE);
		if ((bankutil.isExistsItemType(ht))) // if the Item exists already
		{
			Hashtable<String, String> htUpdate = new Hashtable<String, String>();
			htUpdate.put(IDBConstants.BANK_NAME, BANK_NAME);
			htUpdate.put(IDBConstants.BANK_BRANCH, BANK_BRANCH);
			htUpdate.put(IDBConstants.BANK_BRANCH_CODE, BANK_BRANCH_CODE);
			
			htUpdate.put(IDBConstants.BANK_IFSC, IFSC);
			htUpdate.put(IDBConstants.BANK_SWIFT_CODE, SWIFT_CODE);
			htUpdate.put(IDBConstants.BANK_TELNO, TELNO);
			htUpdate.put(IDBConstants.BANK_FAX, FAX);
			htUpdate.put(IDBConstants.BANK_EMAIL, EMAIL);
			htUpdate.put(IDBConstants.BANK_WEBSITE, WEBSITE);
			htUpdate.put(IDBConstants.BANK_UNITNO, UNITNO);
			htUpdate.put(IDBConstants.BANK_BUILDING, BUILDING);
			htUpdate.put(IDBConstants.BANK_STREET, STREET);
			htUpdate.put(IDBConstants.BANK_STATE, STATE);
			htUpdate.put(IDBConstants.BANK_CITY, CITY);
			htUpdate.put(IDBConstants.BANK_COUNTRY, COUNTRY);
			htUpdate.put(IDBConstants.BANK_POSTALCODE, ZIP);
			htUpdate.put(IDBConstants.BANK_FB, FACEBOOKID);
			htUpdate.put(IDBConstants.BANK_TWITTER, TWITTERID);
			htUpdate.put(IDBConstants.BANK_LINKEDIN, LINKEDINID);
			htUpdate.put(IDBConstants.BANK_CONTACTPERSON, CONTACT_PERSON);
			htUpdate.put(IDBConstants.BANK_HPNO, HPNO);
			htUpdate.put(IDBConstants.BANK_NOTE, NOTE);
			
			new DateUtils();
			htUpdate.put(IDBConstants.UPDATED_AT, DateUtils
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, userName);

			Hashtable<String,String> htCondition = new Hashtable<String,String>();
			htCondition.put(IDBConstants.BANK_BRANCH_CODE, BANK_BRANCH_CODE);
			htCondition.put(IDBConstants.PLANT, plant);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable<String, String> htm = new Hashtable<String, String>();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE","BANK UPDATE");
			htm.put("RECID", "");
			htm.put("ITEM",BANK_BRANCH_CODE);
			htm.put("UPBY", userName);
			htm.put("CRBY", userName);
			htm.put("CRAT", DateUtils.getDateTime());
			htm.put("REMARKS", BANK_NAME);
			htm.put("UPAT", DateUtils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, DateUtils
					.getDateinyyyy_mm_dd(DateUtils.getDate()));

			boolean Updated = bankutil.updateBank(htUpdate,
					htCondition);
		//	boolean inserted = mdao.insertIntoMovHis(htm);
			if (Updated) {
				response = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Bank Details Updated Successfully</font>";
			} else {
				response = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Bank Details </font>";
			}
		} else {
			response = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Bank Deatils doesn't not Exists. Try again</font>";

		}
		
		return response;
		
	}
	
	private String deleteBank(String plant, String userName,
			String BANK_BRANCH_CODE)
			throws ServletException, IOException, Exception {
		String response = "";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.BANK_BRANCH_CODE, BANK_BRANCH_CODE);
		
		//boolean itembankflag  = bankutil.isExistsItemType(ht);
		//if ((bankutil.isExistsItemType(ht))) // if the Item exists already
		//if (itembankflag) {
			//response = "<font class = " + IDBConstants.FAILED_COLOR
				//	+ " >Brank Exists In Transactions</font>";
		//} 
		//else{
			if(bankutil.isExistsItemType(ht))
			{
				boolean flag = bankutil.deleteBank(ht);
				
				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable<String, String> htm = new Hashtable<String, String>();
				htm.put("PLANT", plant);
				//htm.put("DIRTYPE",TransactionConstants.DEL_PRDBRAND);
				htm.put("RECID", "");
				//htm.put("ITEM",ProductBrandId);
				htm.put("UPBY", userName);
				htm.put("CRBY", userName);
				htm.put("CRAT", DateUtils.getDateTime());
			//	htm.put("REMARKS", ProductBrandDesc);
				htm.put("UPAT", DateUtils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, DateUtils
						.getDateinyyyy_mm_dd(DateUtils.getDate()));
				
				//flag = mdao.insertIntoMovHis(htm);
				
				if(flag)
					{response = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Bank details Deleted Successfully</font>";}
				else {
					response = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Product Brand</font>";
                
      				}
			}else{
				response = "<font class = "+IConstants.FAILED_COLOR+">Bank details doesn't  Exists. Try again</font>";
			}
		
		//}

		
		
		return response;
		
	}
	
	//Get the list of brands from the brand Master table. -Bruhan //11/9/12
	private ArrayList<Map<String, String>> getBankList(String plant,String BANK_BRANCH_CODE,String BANK_NAME,String BANK_BRANCH){
		return 	bankutil.getBankList(BANK_BRANCH_CODE,plant,"",BANK_NAME,BANK_BRANCH);
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

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

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
	
	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        BankUtil bankUtil = new BankUtil();
        StrUtils strUtils = new StrUtils();
        bankUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String name = StrUtils.fString(request.getParameter("NAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(name.length()>0) extCond=" AND plant='"+plant+"' and NAME like '"+name+"%' ";
		   //  extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" order by BRANCH";
		     ArrayList listQry = bankUtil.getPoHdrDetails("NAME,BRANCH,BRANCH_NAME",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  name = (String)m.get("NAME");
				  //String custName = strUtils.replaceCharacters2Send((String)m.get("custName"));
				  String branchcode = (String)m.get("BRANCH");
				  String branchname = (String)m.get("BRANCH_NAME");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("NAME", name);
				  resultJsonInt.put("CUSTNAME", branchcode);
				  resultJsonInt.put("BRANCH_NAME", branchname);		  
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
	
	private JSONObject getBranchNameForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        BankUtil bankUtil = new BankUtil();
        StrUtils strUtils = new StrUtils();
        bankUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String branchname = StrUtils.fString(request.getParameter("BRANCH_NAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(branchname.length()>0) extCond=" AND plant='"+plant+"' and BRANCH_NAME like '"+branchname+"%' ";
		   //  extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" order by BRANCH";
		     ArrayList listQry = bankUtil.getPoHdrDetails("NAME,BRANCH,BRANCH_NAME",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String name = (String)m.get("NAME");
				  //String custName = strUtils.replaceCharacters2Send((String)m.get("custName"));
				  String branchcode = (String)m.get("BRANCH");
				 branchname=(String)m.get("BRANCH_NAME");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("NAME", name);
				  resultJsonInt.put("BRANCH_CODE", branchcode);
				  resultJsonInt.put("BRANCH_NAME", branchname);
				 			  
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
	
	private JSONObject getBranchCodeForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        BankUtil bankUtil = new BankUtil();
        StrUtils strUtils = new StrUtils();
        bankUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String branchcode = StrUtils.fString(request.getParameter("BRANCH")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(branchcode.length()>0) extCond=" AND plant='"+plant+"' and BRANCH like '"+branchcode+"%' ";
		   //  extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" order by BRANCH";
		     ArrayList listQry = bankUtil.getPoHdrDetails("NAME,BRANCH,BRANCH_NAME",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String name = (String)m.get("NAME");
				  //String custName = strUtils.replaceCharacters2Send((String)m.get("custName"));
				  branchcode = (String)m.get("BRANCH");
				  String branchname=(String)m.get("BRANCH_NAME");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("NAME", name);
				  resultJsonInt.put("BRANCH", branchcode);
				  resultJsonInt.put("BRANCH_NAME", branchname);
				 			  
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
}
