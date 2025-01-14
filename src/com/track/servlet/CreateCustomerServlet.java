package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.TransportModeDAO;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.ItemUtil;
import com.track.gates.encryptBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class CreateCustomerServlet
 */
@WebServlet("/CreateCustomerServlet")
public class CreateCustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateCustomerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
//			String sUserId = StrUtils.fString((String) request.getParameter("LOGIN_USER"));
			String sUserId = (String) request.getSession().getAttribute("LOGIN_USER");		
			String res = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			String sNewEnb = "enabled";
			String sDeleteEnb = "enabled";
			String sAddEnb = "enabled";
			String sUpdateEnb = "enabled";
			String sCustEnb = "enabled";
			String action = "";
			String redirecturl = "";
			String sCustCode = "", sCustName = "", sCustNameL = "",companyregnumber="",transport="", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "",
					sState = "", sCountry = "", sZip = "", sCons = "Y";

			String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "", sRemarks = "",PEPPOL_ID="",PEPPOL="",APP_SHOWBY_PRODUCT="",SameAsContactAddress="",
					sPayTerms = "",sPaymentTerm="", sPayInDays = "", customertypeid = "", desc = "", customerstatusid = "",
					statusdesc = "", sRcbno = "",currency="";
			String CREDITLIMIT = "", ISCREDITLIMIT = "",CREDIT_LIMIT_BY="";
			String CUSTOMEREMAIL = "", WEBSITE = "", FACEBOOK = "", TWITTER = "", LINKEDIN = "", SKYPE = "",
					OPENINGBALANCE = "", WORKPHONE = "",sTAXTREATMENT="";
			String DONO="",EMPNO="",TRANID="",cmd="";
			String sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="";
			StrUtils strUtils = new StrUtils();
			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			CustUtil custUtil = new CustUtil();
			JSONObject jsonObjectResult = new JSONObject();
			CurrencyDAO currencyDAO = new CurrencyDAO();
			action = strUtils.fString(request.getParameter("action"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			redirecturl = strUtils.fString(request.getParameter("reurl"));
			
			DONO = strUtils.fString(request.getParameter("DONO"));
			EMPNO = strUtils.fString(request.getParameter("EMPNO"));
			TRANID = strUtils.fString(request.getParameter("TRANID"));
			cmd = strUtils.fString(request.getParameter("cmd"));			

			String username = strUtils.fString((String) request.getParameter("LOGIN_USER"));
			sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));
//			if(sCustCode.equals("")) {
//			sCustCode = strUtils.fString(request.getParameter("CUST_CODE_C"));
//			}
			DateUtils dateutils = new DateUtils();
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
			if (sCustCode.length() <= 0)
				sCustCode = strUtils.fString(request.getParameter("CUST_CODE1"));
			sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
//			if(sCustName.equals("")) {
//			sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME_C")));	
//			}
			sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
			companyregnumber = strUtils.InsertQuotes(strUtils.fString(request.getParameter("cus_companyregnumber")));
//			companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
			transport = strUtils.fString(request.getParameter("TRANSPORTID"));//imti
			if(transport.equals("0")) {
				transport = strUtils.fString(request.getParameter("TRANSPORTSID"));//imti
			}else if(transport.equals("")) {
				transport = strUtils.fString(request.getParameter("TRANSPORTSID"));//imti
				 if(transport.equals("")) 
					 transport = strUtils.fString(request.getParameter("TRANSPORTIDC"));//imti
			}else {
				transport = strUtils.fString(request.getParameter("TRANSPORTID"));//imti
			}
			currency = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CURRENCYID_C")));
			sAddr1 = strUtils.fString(request.getParameter("ADDR1"));
			sAddr2 = strUtils.fString(request.getParameter("ADDR2"));
			sAddr3 = strUtils.fString(request.getParameter("ADDR3"));
			sAddr4 = strUtils.fString(request.getParameter("ADDR4"));
			sState = strUtils.InsertQuotes(strUtils.fString(request.getParameter("STATE")));
			sCountry = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));
			sZip = strUtils.fString(request.getParameter("ZIP"));
			sCons = strUtils.fString(request.getParameter("CONSIGNMENT"));
			sContactName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
			sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
			
			sTelNo = strUtils.fString(request.getParameter("TELNO"));
			sHpNo = strUtils.fString(request.getParameter("HPNO"));
			sFax = strUtils.fString(request.getParameter("FAX"));
			sEmail = strUtils.fString(request.getParameter("EMAIL"));
			sRcbno = strUtils.fString(request.getParameter("RCBNO"));
			String shipZip = strUtils.fString(request.getParameter("SHIP_ZIP"));
			if(shipZip.equals("")) {
				shipZip  = strUtils.fString(request.getParameter("CUST_SHIP_ZIP"));
			}
			String	shipState = strUtils.fString(request.getParameter("SHIP_STATE"));
			if(shipState.equals("")) {
				shipState  = strUtils.fString(request.getParameter("CUST_SHIP_STATE"));
			}
			String shipAddr4 = strUtils.fString(request.getParameter("SHIP_ADDR4"));
			if(shipAddr4.equals("")) {
				shipAddr4  = strUtils.fString(request.getParameter("CUST_SHIP_ADDR4"));
			}
			String shipAddr3 = strUtils.fString(request.getParameter("SHIP_ADDR3"));
			if(shipAddr3.equals("")) {
				shipAddr3  = strUtils.fString(request.getParameter("CUST_SHIP_ADDR3"));
			}
			String shipAddr2 = strUtils.fString(request.getParameter("SHIP_ADDR2"));
			if(shipAddr2.equals("")) {
				shipAddr2  = strUtils.fString(request.getParameter("CUST_SHIP_ADDR2"));
			}
			String shipAddr1 = strUtils.fString(request.getParameter("SHIP_ADDR1"));
			if(shipAddr1.equals("")) {
				shipAddr1  = strUtils.fString(request.getParameter("CUST_SHIP_ADDR1"));
			}
			String shipCountry = strUtils.fString(request.getParameter("SHIP_COUNTRY"));
			if(shipCountry.equals("")) {
				shipCountry  = strUtils.fString(request.getParameter("CUST_SHIP_COUNTRY"));
			}
			String shipEmail = strUtils.fString(request.getParameter("SHIP_EMAIL"));
			if(shipEmail.equals("")) {
				shipEmail  = strUtils.fString(request.getParameter("CUST_SHIP_EMAIL"));
			}
			String shipHpNo = strUtils.fString(request.getParameter("SHIP_HPNO"));
			if(shipHpNo.equals("")) {
				shipHpNo  = strUtils.fString(request.getParameter("CUST_SHIP_HPNO"));
			}
			String shipWORKPHONE = strUtils.fString(request.getParameter("SHIP_WORKPHONE"));
			if(shipWORKPHONE.equals("")) {
				shipWORKPHONE  = strUtils.fString(request.getParameter("CUST_SHIP_WORKPHONE"));
			}
			String shipDesgination = strUtils.fString(request.getParameter("SHIP_DESGINATION"));
			if(shipDesgination.equals("")) {
				shipDesgination  = strUtils.fString(request.getParameter("CUST_SHIP_DESGINATION"));
			}
			String shipContactName  = strUtils.fString(request.getParameter("SHIP_CONTACTNAME"));
			if(shipContactName.equals("")) {
				shipContactName  = strUtils.fString(request.getParameter("CUST_SHIP_CONTACTNAME"));
			}
			SameAsContactAddress=StrUtils.fString((request.getParameter("SameAsContactAddress") != null) ? "1": "0").trim();
			
			String dob  = strUtils.fString(request.getParameter("DOB"));
			String nationality  = strUtils.fString(request.getParameter("NATIONALITY"));
		
			sRemarks = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
			sPayTerms = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_PAYTERMS")));
			//sPaymentTerm = strUtils.InsertQuotes(strUtils.fString(request.getParameter("payment_term")));
			sPaymentTerm = strUtils.InsertQuotes(strUtils.fString(request.getParameter("cpayment_terms")));
			sPayInDays = strUtils.InsertQuotes(strUtils.fString(request.getParameter("PMENT_DAYS")));
			customertypeid = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
			CREDITLIMIT = strUtils.fString(request.getParameter("CREDITLIMIT"));
			ISCREDITLIMIT = StrUtils.fString((request.getParameter("ISCREDITLIMIT") != null) ? "1" : "0").trim();
			APP_SHOWBY_PRODUCT=StrUtils.fString((request.getParameter("APP_SHOWBY_PRODUCT") != null) ? "1": "0").trim();
			PEPPOL=StrUtils.fString((request.getParameter("PEPPOL_C") != null) ? "1": "0").trim();
			String APP_SHOWBY_CATEGORY=StrUtils.fString((request.getParameter("APP_SHOWBY_CATEGORY") != null) ? "1": "0").trim();
			CREDIT_LIMIT_BY=strUtils.fString(request.getParameter("CREDIT_LIMIT_BY"));
			CUSTOMEREMAIL = strUtils.fString(request.getParameter("CUSTOMEREMAIL"));
			WEBSITE = strUtils.fString(request.getParameter("WEBSITE"));
			PEPPOL_ID = strUtils.fString(request.getParameter("PEPPOL_IDC"));
			FACEBOOK = strUtils.fString(request.getParameter("FACEBOOK"));
			TWITTER = strUtils.fString(request.getParameter("TWITTER"));
			LINKEDIN = strUtils.fString(request.getParameter("LINKEDIN"));
			SKYPE = strUtils.fString(request.getParameter("SKYPE"));
			OPENINGBALANCE = strUtils.fString(request.getParameter("OPENINGBALANCE"));
			WORKPHONE = strUtils.fString(request.getParameter("WORKPHONE"));
			sTAXTREATMENT = strUtils.fString(request.getParameter("TAXTREATMENT"));
			sBANKNAME   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BANKNAME")));
			sIBAN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("IBAN")));
			sBANKROUTINGCODE       = strUtils.fString(request.getParameter("BANKROUTINGCODE"));
			String[] USER_NAME = request.getParameterValues("USER_NAME");
			String[] USER_HPNO = request.getParameterValues("USER_HPNO");
			String[] USER_EMAIL = request.getParameterValues("USER_EMAIL");
			String[] customeruserid = request.getParameterValues("USER_ID");
			String[] PASSWORD= request.getParameterValues("PASSWORD");
			String[] manageapp= request.getParameterValues("MANAGER_APP_VAL");
			float CREDITLIMITVALUE = "".equals(CREDITLIMIT) ? 0.0f : Float.parseFloat(CREDITLIMIT);
			float OPENINGBALANCEVALUE = "".equals(OPENINGBALANCE) ? 0.0f : Float.parseFloat(OPENINGBALANCE);
			customerstatusid = strUtils.fString(request.getParameter("CUSTOMER_STATUS_ID"));
			List customerstatuslist = custUtil.getCustStatusList("", plant, " AND ISACTIVE ='Y'");
			
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			if(shipState.equalsIgnoreCase("Select State"))
				shipState="";
			if (action.equalsIgnoreCase("NEW")) {

				sCustCode = "";
				sCustName = "";
				sCustNameL = "";
				companyregnumber="";
				transport="";
				currency="";
				sAddr1 = "";
				sAddr2 = "";
				sAddr3 = "";
				sAddr4 = "";
				sState = "";
				sCountry = "";
				sZip = "";
				sCons = "Y";
				sContactName = "";
				sDesgination = "";
				PEPPOL_ID ="";
				sTelNo = "";
				sHpNo = "";
				sFax = "";
				sEmail = "";
				sRemarks = "";
				sPayTerms = "";
				sPaymentTerm = "";
				sPayInDays = "";
				sRcbno = "";
				customertypeid = "";
				CREDITLIMIT = "";
				CREDIT_LIMIT_BY="";
				CUSTOMEREMAIL = "";
				WEBSITE = "";
				FACEBOOK = "";
				TWITTER = "";
				LINKEDIN = "";
				SKYPE = "";
				OPENINGBALANCE = "";
				WORKPHONE = "";
				APP_SHOWBY_CATEGORY="";
				shipZip="";
				shipState="";
				shipAddr4="";
				shipAddr3="";
				shipAddr2="";
				shipAddr1="";
				shipCountry="";
				shipEmail="";
				shipHpNo="";shipWORKPHONE="";shipDesgination="";shipContactName="";dob="";nationality="";SameAsContactAddress="";
				
				sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";APP_SHOWBY_PRODUCT="";PEPPOL="";
				
			} else if (action.equalsIgnoreCase("Auto-ID")) {

				String minseq = "";
				String sBatchSeq = "";
				boolean insertFlag = false;
				String sZero = "";
//TblControlDAO _TblControlDAO =new TblControlDAO();

				Hashtable ht = new Hashtable();

				String query = " isnull(NXTSEQ,'') as NXTSEQ";
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
				try {
					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();

						htTblCntInsert.put(IDBConstants.PLANT, plant);

						htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "C");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
						htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sCustCode = "C" + "0001";
					} else {
						// --if exitflag is not false than we updated nxtseq batch number based on
						// plant,currentmonth

						Map m = _TblControlDAO.selectRow(query, ht, "");
						sBatchSeq = (String) m.get("NXTSEQ");

						int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

						String updatedSeq = Integer.toString(inxtSeq);
						if (updatedSeq.length() == 1) {
							sZero = "000";
						} else if (updatedSeq.length() == 2) {
							sZero = "00";
						} else if (updatedSeq.length() == 3) {
							sZero = "0";
						}

						Map htUpdate = null;

						Hashtable htTblCntUpdate = new Hashtable();
						htTblCntUpdate.put(IDBConstants.PLANT, plant);
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "C");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + (String) updatedSeq.toString() + "'");

						// boolean
						// updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
						sCustCode = "C" + sZero + updatedSeq;
						if(redirecturl.contains("CustomerCreditNoteEdit"))
						{
								response.sendRedirect("jsp/"+redirecturl+"&sCustCode="+sCustCode);
						}
						else if(redirecturl.contains("expensetoInvoice"))
						{
							response.sendRedirect("jsp/"+redirecturl+"&cmd="+cmd+"&sCustCode="+sCustCode);
						}
						else if(redirecturl.contains("IssueingGoodsInvoice"))
						{
							response.sendRedirect("jsp/"+redirecturl+"&DONO="+DONO+"&EMPNO="+EMPNO+"&TRANID="+TRANID+"&cmd=Edit&sCustCode="+sCustCode);
						}
						else
						{
								response.sendRedirect("jsp/"+redirecturl+".jsp?sCustCode="+sCustCode);
						}
					}
				} catch (Exception e) {
					// mLogger.exception(true, "ERROR IN JSP PAGE - customer_view.jsp ", e);
				}

			}
//2. >> Add
			else if (action.equalsIgnoreCase("ADD")) {

				if (!custUtil.isExistCustomer(sCustCode, plant) && !custUtil.isExistCustomerName(sCustName, plant)) // if
																													// the
																													// Customer
																													// exists
																													// already
				{
					
						
						boolean chkuserid=false;
						String chkcustomeruserid="";
						if(customeruserid !=null) {
						for(int i =0 ; i < customeruserid.length ; i++){
						chkcustomeruserid =(String)customeruserid[i];
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

					OPENINGBALANCEVALUE = "".equals(OPENINGBALANCE) ? 0.0f : Float.parseFloat(OPENINGBALANCE);
					OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
					Hashtable ht = new Hashtable();
					ht.put(IConstants.PLANT, plant);
					ht.put(IConstants.CUSTOMER_CODE, sCustCode);
					ht.put("USER_ID", sCustCode);
					ht.put(IConstants.CUSTOMER_NAME, sCustName);
					ht.put(IConstants.CUSTOMER_LAST_NAME, sCustNameL);
			        ht.put(IConstants.companyregnumber,companyregnumber);
			        ht.put(IConstants.TRANSPORTID, transport);
					ht.put("CURRENCY_ID", currency);
					ht.put(IConstants.ADDRESS1, sAddr1);
					ht.put(IConstants.ADDRESS2, sAddr2);
					ht.put(IConstants.ADDRESS3, sAddr3);
					ht.put(IConstants.ADDRESS4, sAddr4);
					ht.put(IConstants.COUNTRY, sCountry);
					ht.put(IConstants.STATE, sState);
					ht.put(IConstants.ZIP, sZip);
					ht.put(IConstants.USERFLG1, sCons);
					ht.put(IConstants.NAME, sContactName);
					ht.put(IConstants.DESGINATION, sDesgination);
					ht.put(IConstants.TELNO, sTelNo);
					ht.put(IConstants.HPNO, sHpNo);
					ht.put(IConstants.FAX, sFax);
					ht.put(IConstants.EMAIL, sEmail);
					ht.put(IConstants.SHIP_ZIP, shipZip);
					ht.put(IConstants.SHIP_CONTACTNAME, shipContactName);
					ht.put("DATEOFBIRTH",dob);
					ht.put("NATIONALITY",nationality);
					ht.put(IConstants.SHIP_DESGINATION, shipDesgination);
					ht.put(IConstants.SHIP_WORKPHONE, shipWORKPHONE);
					ht.put(IConstants.SHIP_HPNO, shipHpNo);
					ht.put(IConstants.SHIP_EMAIL, shipEmail);
					ht.put(IConstants.SHIP_COUNTRY_CODE, shipCountry);
					ht.put(IConstants.SHIP_ADDR1, shipAddr1);
					ht.put(IConstants.SHIP_ADDR2, shipAddr2);
					ht.put(IConstants.SHIP_ADDR3, shipAddr3);
					ht.put(IConstants.SHIP_ADDR4, shipAddr4);
					ht.put(IConstants.SHIP_STATE, shipState);
					ht.put("ISSHOWAPPCATEGORYIMAGE", APP_SHOWBY_CATEGORY);
					ht.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
					ht.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
					ht.put("PAYMENT_TERMS", sPaymentTerm);
					ht.put(IConstants.PAYINDAYS, sPayInDays);
					ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
					ht.put(IConstants.CREATED_BY, sUserId);
					ht.put(IConstants.ISACTIVE, "Y");
					ht.put(IConstants.CUSTOMERTYPEID, customertypeid);
					ht.put(IConstants.CUSTOMERSTATUSID, customerstatusid);
					ht.put(IConstants.RCBNO, sRcbno);
					ht.put("CREDITLIMIT", CREDITLIMIT);
					/*ht.put("ISCREDITLIMIT", ISCREDITLIMIT);*/
					ht.put("CREDIT_LIMIT_BY",CREDIT_LIMIT_BY);
					ht.put("PEPPOL_ID",PEPPOL_ID);
					ht.put("ISPEPPOL",PEPPOL);
					ht.put("ISSHOWBYPRODUCT",APP_SHOWBY_PRODUCT);
					ht.put("ISSAMEASCONTACTADD",SameAsContactAddress);
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
					htm.put(IDBConstants.CREATED_BY, sUserId);

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
							htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
							htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
							insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
						}
					}

					if(customeruserid !=null) {
						for(int i =0 ; i < customeruserid.length ; i++){
							Hashtable customeruser = new Hashtable<String, String>();
							String sPassword =(String)PASSWORD[i];
							if(sPassword != ""){
					        	String  empPwd   = new encryptBean().encrypt(sPassword);
					        	  sPassword = empPwd;
					          }
							
							customeruser.put("PLANT", plant);
							customeruser.put("CUSTNO", sCustCode);
							customeruser.put("CUSTDESC", sCustName);
							customeruser.put("USER_ID", (String)customeruserid[i]);
							customeruser.put("PASSWORD", sPassword);
							customeruser.put("ISMANAGERAPPACCESS", (String)manageapp[i]);
							customeruser.put("USER_NAME", (String)USER_NAME[i]);
							customeruser.put("USER_HPNO", (String)USER_HPNO[i]);
							customeruser.put("USER_EMAIL", (String)USER_EMAIL[i]);
							customeruser.put("CRAT",dateutils.getDateTime());
							customeruser.put("CRBY",sUserId);
							customeruser.put("UPAT",dateutils.getDateTime());
							boolean itemInserted =new CustMstDAO().addcustomerUser(customeruser, plant);
						}
				 }
					boolean custInserted = custUtil.insertCustomer(ht);
					boolean inserted = mdao.insertIntoMovHis(htm);
					jsonObjectResult = new JSONObject();
					
					if (custInserted && inserted) {
						res = "<font class = " + IConstants.SUCCESS_COLOR + ">Customer Added Successfully</font>";res = "Failed to add New Customer";
						jsonObjectResult = this.getsavedsuplierwithtax(request,sCustCode,sCustName,sTAXTREATMENT,currency,transport, sContactName, sDesgination, sTelNo, sHpNo, sEmail, sAddr1, sAddr2, sAddr3, sAddr4, sCountry, sState,
								 sZip, shipZip, shipContactName,dob,nationality, shipDesgination, shipWORKPHONE, shipHpNo, shipEmail, shipCountry, shipAddr1, shipAddr2, shipAddr3, shipAddr4, shipState);

					} else {
//						res = "<font class = " + IConstants.FAILED_COLOR + ">Failed to add New Customer</font>";
						res = "Failed to add New Customer";
						JSONObject resultJsonInt = new JSONObject();	
						JSONArray jsonArray = new JSONArray();
						resultJsonInt.put("STATUS", "FAILED");	
						resultJsonInt.put("MESSAGE", res);
						jsonArray.add(resultJsonInt);
						jsonObjectResult.put("customer", jsonArray);

					}
				}else{
//			           res = "<font class = "+IConstants.FAILED_COLOR+">User ID "+chkcustomeruserid+" Exists already. Try again with diffrent User ID.</font>";
					    res = "User ID "+chkcustomeruserid+" Exists already. Try again with diffrent User ID.";
			            JSONObject resultJsonInt = new JSONObject();	
			            JSONArray jsonArray = new JSONArray();
						resultJsonInt.put("STATUS", "FAILED");	
						resultJsonInt.put("MESSAGE", res);
						jsonArray.add(resultJsonInt);
						jsonObjectResult.put("customer", jsonArray);
			    	}}else {
//					res = "<font class = " + IConstants.FAILED_COLOR	+ ">Customer ID Or Name Exists already. Try again with diffrent Customer ID Or Name.</font>";
			    		    res = "Customer ID Or Name Exists already. Try again with diffrent Customer ID Or Name";
						    JSONObject resultJsonInt = new JSONObject();	
				            JSONArray jsonArray = new JSONArray();
							resultJsonInt.put("STATUS", "FAILED");	
							resultJsonInt.put("MESSAGE", res);
							jsonArray.add(resultJsonInt);
							jsonObjectResult.put("customer", jsonArray);
				}
				
				if(redirecturl.contains("CustomerCreditNoteEdit"))
				{
					response.sendRedirect("jsp/"+redirecturl+"&CUST_NAME="+sCustName+"&CUST_CODE="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT);
				}
				else if(redirecturl.contains("expensetoInvoice"))
				{
					response.sendRedirect("jsp/"+redirecturl+"&cmd="+cmd+"&CUST_NAME="+sCustName+"&CUST_CODE="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT);
				}				
				else if(redirecturl.contains("IssueingGoodsInvoice"))
				{
					response.sendRedirect("jsp/"+redirecturl+"&DONO="+DONO+"&EMPNO="+EMPNO+"&TRANID="+TRANID+"&cmd=Edit&CUST_NAME="+sCustName+"&CUST_CODE="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT);
				}
				else
				{
				response.sendRedirect("jsp/"+redirecturl+".jsp?CUST_NAME="+sCustName+"&CUST_CODE="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT);
				}
			}

//3. >> Update
			else if (action.equalsIgnoreCase("UPDATE")) {

				if (custUtil.isExistCustomer(sCustCode, plant)) {
					Hashtable htUpdate = new Hashtable();
					htUpdate.put(IConstants.PLANT, plant);
					htUpdate.put(IConstants.CUSTOMER_CODE, sCustCode);
					htUpdate.put(IConstants.CUSTOMER_NAME, sCustName);
					htUpdate.put(IConstants.CUSTOMER_LAST_NAME, sCustNameL);
					htUpdate.put(IConstants.companyregnumber,companyregnumber);
					htUpdate.put(IConstants.TRANSPORTID, transport);
					htUpdate.put("CURRENCY_ID", currency);
					htUpdate.put(IConstants.ADDRESS1, sAddr1);
					htUpdate.put(IConstants.ADDRESS2, sAddr2);
					htUpdate.put(IConstants.ADDRESS3, sAddr3);
					htUpdate.put(IConstants.ADDRESS4, sAddr4);
					htUpdate.put(IConstants.COUNTRY, sCountry);
					htUpdate.put(IConstants.ZIP, sZip);
					htUpdate.put(IConstants.USERFLG1, sCons);
					htUpdate.put(IConstants.NAME, sContactName);
					htUpdate.put(IConstants.DESGINATION, sDesgination);
					htUpdate.put(IConstants.TELNO, sTelNo);
					htUpdate.put(IConstants.HPNO, sHpNo);
					htUpdate.put(IConstants.FAX, sFax);
					htUpdate.put(IConstants.EMAIL, sEmail);
					htUpdate.put(IConstants.SHIP_ZIP, shipZip);
					htUpdate.put(IConstants.SHIP_CONTACTNAME, shipContactName);
					htUpdate.put("DATEOFBIRTH",dob);
					htUpdate.put("NATIONALITY",nationality);
					htUpdate.put(IConstants.SHIP_DESGINATION, shipDesgination);
					htUpdate.put(IConstants.SHIP_WORKPHONE, shipWORKPHONE);
					htUpdate.put(IConstants.SHIP_HPNO, shipHpNo);
					htUpdate.put(IConstants.SHIP_EMAIL, shipEmail);
					htUpdate.put(IConstants.SHIP_COUNTRY_CODE, shipCountry);
					htUpdate.put(IConstants.SHIP_ADDR1, shipAddr1);
					htUpdate.put(IConstants.SHIP_ADDR2, shipAddr2);
					htUpdate.put(IConstants.SHIP_ADDR3, shipAddr3);
					htUpdate.put(IConstants.SHIP_ADDR4, shipAddr4);
					htUpdate.put(IConstants.SHIP_STATE, shipState);
					htUpdate.put("ISSHOWAPPCATEGORYIMAGE", APP_SHOWBY_CATEGORY);
					htUpdate.put(IConstants.REMARKS, sRemarks);
					htUpdate.put(IConstants.PAYTERMS, sPayTerms);
					htUpdate.put("PAYMENT_TERMS", sPaymentTerm);
					htUpdate.put(IConstants.PAYINDAYS, sPayInDays);
					htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
					htUpdate.put(IConstants.UPDATED_BY, sUserId);
					htUpdate.put(IConstants.ISACTIVE, "Y");

					Hashtable htCondition = new Hashtable();
					htCondition.put("CUSTNO", sCustCode);
					htCondition.put(IConstants.PLANT, plant);

					MovHisDAO mdao = new MovHisDAO(plant);
					// mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE", "UPD_CUST");
					htm.put("RECID", "");
					htm.put("UPBY", sUserId);
					htm.put("CRBY", sUserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
					boolean custUpdated = custUtil.updateCustomer(htUpdate, htCondition);
					boolean inserted = mdao.insertIntoMovHis(htm);
					if (custUpdated && inserted) {
						res = "<font class = " + IConstants.SUCCESS_COLOR + ">Customer Updated Successfully</font>";
					} else {
						res = "<font class = " + IConstants.FAILED_COLOR + ">Failed to Update Customer</font>";
					}
				} else {
					res = "<font class = " + IConstants.FAILED_COLOR + ">Customer doesn't not Exists. Try again</font>";

				}
			}

//4. >> Delete
			else if (action.equalsIgnoreCase("DELETE")) {

				if (custUtil.isExistCustomer(sCustCode, plant)) {
					boolean custDeleted = custUtil.deleteCustomer(sCustCode, plant);
					if (custDeleted) {
						res = "<font class = " + IConstants.SUCCESS_COLOR + ">Customer Deleted Successfully</font>";
						sCustCode = "";
						sCustName = "";
						sCustNameL = "";
						companyregnumber="";
						transport="";
						currency="";
						sAddr1 = "";
						sAddr2 = "";
						sAddr3 = "";
						sAddr4 = "";
						sCountry = "";
						sZip = "";
						sCons = "Y";
						sDesgination = "";
						sTelNo = "";
						sHpNo = "";
						sFax = "";
						sEmail = "";
						sRemarks = "";
						sPayTerms = "";
						sPaymentTerm = "";
						sPayInDays = "";
						sContactName = "";
						customertypeid = "";
						CREDITLIMIT = "";
						CUSTOMEREMAIL = "";
						WEBSITE = "";
						PEPPOL_ID = "";
						FACEBOOK = "";
						TWITTER = "";
						LINKEDIN = "";
						SKYPE = "";
						OPENINGBALANCE = "";
						WORKPHONE = "";APP_SHOWBY_PRODUCT="";PEPPOL="";
						APP_SHOWBY_CATEGORY="";
						shipZip="";
						shipState="";
						shipAddr4="";
						shipAddr3="";
						shipAddr2="";
						shipAddr1="";
						shipCountry="";
						shipEmail="";
						shipHpNo="";shipWORKPHONE="";shipDesgination="";shipContactName="";dob="";nationality="";SameAsContactAddress="";

					} else {
						res = "<font class = " + IConstants.FAILED_COLOR + ">Failed to Delete Customer</font>";
						sAddEnb = "enabled";
					}
				} else {
					res = "<font class = " + IConstants.FAILED_COLOR + ">Customer doesn't not Exists. Try again</font>";
				}
			}

//4. >> View
			else if (action.equalsIgnoreCase("VIEW")) {
				try {

					ArrayList arrCust = custUtil.getCustomerDetails(sCustCode, plant);
					sCustCode = (String) arrCust.get(0);
					sCustName = (String) arrCust.get(1);
					sAddr1 = (String) arrCust.get(2);
					sAddr2 = (String) arrCust.get(3);
					sAddr3 = (String) arrCust.get(4);
					sCountry = (String) arrCust.get(5);
					sZip = (String) arrCust.get(6);
					sCons = (String) arrCust.get(7);
					sCustNameL = (String) arrCust.get(8);
					sContactName = (String) arrCust.get(9);
					sDesgination = (String) arrCust.get(10);
					sTelNo = (String) arrCust.get(11);
					sHpNo = (String) arrCust.get(12);
					sFax = (String) arrCust.get(13);
					sEmail = (String) arrCust.get(14);
					sRemarks = (String) arrCust.get(15);
					currency = (String) arrCust.get(16);
					transport = (String) arrCust.get(57);
					
					sAddr4 = (String) arrCust.get(17);
					sPayTerms = (String) arrCust.get(18);
					sPaymentTerm = (String) arrCust.get(59);
					sPayInDays = (String) arrCust.get(19);
					sRcbno = (String) arrCust.get(20);
					customertypeid = (String) arrCust.get(21);
					CREDITLIMIT = (String) arrCust.get(22);
					
					shipContactName = (String) arrCust.get(45);
					shipDesgination = (String) arrCust.get(46);
					shipWORKPHONE = (String) arrCust.get(47);
					shipHpNo = (String) arrCust.get(48);
					shipEmail = (String) arrCust.get(49);
					shipCountry = (String) arrCust.get(50);
					shipAddr1 = (String) arrCust.get(51);
					shipAddr2 = (String) arrCust.get(52);
					shipAddr3 = (String) arrCust.get(53);
					shipAddr4 = (String) arrCust.get(54);
					shipState = (String) arrCust.get(55);
					shipZip = (String) arrCust.get(56);
					dob = (String) arrCust.get(61);
					nationality = (String) arrCust.get(62);
					APP_SHOWBY_CATEGORY = (String) arrCust.get(57);
					CREDITLIMITVALUE = "".equals(CREDITLIMIT) ? 0.0f : Float.parseFloat(CREDITLIMIT);
				} catch (Exception e) {
					res = "No details found for customer ID : " + sCustCode;
				}

			}else if (action.equalsIgnoreCase("JAuto-ID")) {
				


				String minseq = "";
				String sBatchSeq = "";
				boolean insertFlag = false;
				String sZero = "";
//TblControlDAO _TblControlDAO =new TblControlDAO();

				Hashtable ht = new Hashtable();

				String query = " isnull(NXTSEQ,'') as NXTSEQ";
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
				try {
					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();

						htTblCntInsert.put(IDBConstants.PLANT, plant);

						htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "C");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
						htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sCustCode = "C" + "0001";
					} else {
						// --if exitflag is not false than we updated nxtseq batch number based on
						// plant,currentmonth

						Map m = _TblControlDAO.selectRow(query, ht, "");
						sBatchSeq = (String) m.get("NXTSEQ");

						int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

						String updatedSeq = Integer.toString(inxtSeq);
						if (updatedSeq.length() == 1) {
							sZero = "000";
						} else if (updatedSeq.length() == 2) {
							sZero = "00";
						} else if (updatedSeq.length() == 3) {
							sZero = "0";
						}

						Map htUpdate = null;

						Hashtable htTblCntUpdate = new Hashtable();
						htTblCntUpdate.put(IDBConstants.PLANT, plant);
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "C");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + (String) updatedSeq.toString() + "'");

						// boolean
						// updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
						sCustCode = "C" + sZero + updatedSeq;
						
						
					}
				} catch (Exception e) {
					// mLogger.exception(true, "ERROR IN JSP PAGE - customer_view.jsp ", e);
				}

			

			
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getautiincremtid(request,sCustCode);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();	
			} else if (action.equalsIgnoreCase("JADD")) {
				sCustCode = strUtils.fString(request.getParameter("CUST_CODE_C"));
				
				if (sCustCode.length() <= 0)
					sCustCode = strUtils.fString(request.getParameter("CUST_CODE1_C"));
				sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME_C")));
				


				if (!custUtil.isExistCustomer(sCustCode, plant) && !custUtil.isExistCustomerName(sCustName, plant)) // if
																													// the
																													// Customer
																													// exists
																													// already
				{
					
					
						
						boolean chkuserid=false;
						String chkcustomeruserid="";
						if(customeruserid !=null) {
						for(int i =0 ; i < customeruserid.length ; i++){
						chkcustomeruserid =(String)customeruserid[i];
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

					OPENINGBALANCEVALUE = "".equals(OPENINGBALANCE) ? 0.0f : Float.parseFloat(OPENINGBALANCE);
					OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
					Hashtable ht = new Hashtable();
					ht.put(IConstants.PLANT, plant);
					ht.put(IConstants.CUSTOMER_CODE, sCustCode);
					ht.put("USER_ID", sCustCode);
					ht.put(IConstants.CUSTOMER_NAME, sCustName);
					ht.put(IConstants.CUSTOMER_LAST_NAME, sCustNameL);
			        ht.put(IConstants.companyregnumber,companyregnumber);
			        ht.put(IConstants.TRANSPORTID, transport);
					ht.put("CURRENCY_ID", currency);
					ht.put(IConstants.ADDRESS1, sAddr1);
					ht.put(IConstants.ADDRESS2, sAddr2);
					ht.put(IConstants.ADDRESS3, sAddr3);
					ht.put(IConstants.ADDRESS4, sAddr4);
					ht.put(IConstants.COUNTRY, sCountry);
					ht.put(IConstants.STATE, sState);
					ht.put(IConstants.ZIP, sZip);
					ht.put(IConstants.USERFLG1, sCons);
					ht.put(IConstants.NAME, sContactName);
					ht.put(IConstants.DESGINATION, sDesgination);
					ht.put(IConstants.TELNO, sTelNo);
					ht.put(IConstants.HPNO, sHpNo);
					ht.put(IConstants.FAX, sFax);
					ht.put(IConstants.EMAIL, sEmail);
					ht.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
					ht.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
					ht.put("PAYMENT_TERMS",strUtils.InsertQuotes(sPaymentTerm));
					ht.put(IConstants.PAYINDAYS, sPayInDays);
					ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
					ht.put(IConstants.CREATED_BY, sUserId);
					ht.put(IConstants.ISACTIVE, "Y");
					ht.put(IConstants.CUSTOMERTYPEID, customertypeid);
					ht.put(IConstants.CUSTOMERSTATUSID, customerstatusid);
					ht.put(IConstants.RCBNO, sRcbno);
					ht.put(IConstants.SHIP_ZIP, shipZip);
					ht.put(IConstants.SHIP_CONTACTNAME, shipContactName);
					ht.put("DATEOFBIRTH",dob);
					ht.put("NATIONALITY",nationality);
					ht.put(IConstants.SHIP_DESGINATION, shipDesgination);
					ht.put(IConstants.SHIP_WORKPHONE, shipWORKPHONE);
					ht.put(IConstants.SHIP_HPNO, shipHpNo);
					ht.put(IConstants.SHIP_EMAIL, shipEmail);
					ht.put(IConstants.SHIP_COUNTRY_CODE, shipCountry);
					ht.put(IConstants.SHIP_ADDR1, shipAddr1);
					ht.put(IConstants.SHIP_ADDR2, shipAddr2);
					ht.put(IConstants.SHIP_ADDR3, shipAddr3);
					ht.put(IConstants.SHIP_ADDR4, shipAddr4);
					ht.put(IConstants.SHIP_STATE, shipState);
					ht.put("ISSHOWAPPCATEGORYIMAGE", APP_SHOWBY_CATEGORY);
					ht.put("ISPEPPOL", PEPPOL);
					ht.put("PEPPOL_ID", PEPPOL_ID);
					ht.put("CREDITLIMIT", CREDITLIMIT);
					/*ht.put("ISCREDITLIMIT", ISCREDITLIMIT);*/
					ht.put("CREDIT_LIMIT_BY",CREDIT_LIMIT_BY);
				//	ht.put("ISPEPPOL",PEPPOL);
					ht.put("ISSHOWBYPRODUCT",APP_SHOWBY_PRODUCT);
					ht.put("ISSAMEASCONTACTADD",SameAsContactAddress);
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
					htm.put(IDBConstants.CREATED_BY, sUserId);

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
							htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
							htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
							insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
						}
					}
					
					if(customeruserid !=null) {
						for(int i =0 ; i < customeruserid.length ; i++){
							Hashtable customeruser = new Hashtable<String, String>();
							String sPassword =(String)PASSWORD[i];
							if(sPassword != ""){
					        	String  empPwd   = new encryptBean().encrypt(sPassword);
					        	  sPassword = empPwd;
					          }
							
							customeruser.put("PLANT", plant);
							customeruser.put("CUSTNO", sCustCode);
							customeruser.put("CUSTDESC", sCustName);
							customeruser.put("USER_ID", (String)customeruserid[i]);
							customeruser.put("PASSWORD", sPassword);
							customeruser.put("ISMANAGERAPPACCESS", (String)manageapp[i]);
							customeruser.put("USER_NAME", (String)USER_NAME[i]);
							customeruser.put("USER_HPNO", (String)USER_HPNO[i]);
							customeruser.put("USER_EMAIL", (String)USER_EMAIL[i]);
							customeruser.put("CRAT",dateutils.getDateTime());
							customeruser.put("CRBY",sUserId);
							customeruser.put("UPAT",dateutils.getDateTime());
							boolean itemInserted =new CustMstDAO().addcustomerUser(customeruser, plant);
						}
				 }

					boolean custInserted = custUtil.insertCustomer(ht);
					boolean inserted = mdao.insertIntoMovHis(htm);
					
					//added by imthi 29-06-2022 for customer popup save ->DESC: get customer code and insert with item from itemmst tbl
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
			          
					jsonObjectResult = new JSONObject();

					if (custInserted && inserted) {
						res = "<font class = " + IConstants.SUCCESS_COLOR + ">Customer Added Successfully</font>";
						jsonObjectResult = this.getsavedsuplierwithtax(request,sCustCode,sCustName,sTAXTREATMENT,currency,transport, sContactName, sDesgination, sTelNo, sHpNo, sEmail, sAddr1, sAddr2, sAddr3, sAddr4, sCountry, sState,
								 sZip, shipZip, shipContactName,dob,nationality, shipDesgination, shipWORKPHONE, shipHpNo, shipEmail, shipCountry, shipAddr1, shipAddr2, shipAddr3, shipAddr4, shipState);

					} else {
//						res = "<font class = " + IConstants.FAILED_COLOR + ">Failed to add New Customer</font>";
						res = "Failed to add New Customer";
						JSONObject resultJsonInt = new JSONObject();	
						JSONArray jsonArray = new JSONArray();
						resultJsonInt.put("STATUS", "FAILED");	
						resultJsonInt.put("MESSAGE", res);
						jsonArray.add(resultJsonInt);
						jsonObjectResult.put("customer", jsonArray);
					}
				}else{
			    	 
//			           res = "<font class = "+IConstants.FAILED_COLOR+">User ID "+chkcustomeruserid+" Exists already. Try again with diffrent User ID.</font>";
			           res = "User ID "+chkcustomeruserid+" Exists already. Try again with diffrent User ID.";
			           JSONObject resultJsonInt = new JSONObject();	
			           JSONArray jsonArray = new JSONArray();
						resultJsonInt.put("STATUS", "FAILED");	
						resultJsonInt.put("MESSAGE", res);
						jsonArray.add(resultJsonInt);
						jsonObjectResult.put("customer", jsonArray);
			
			    } }else {
//					res = "<font class = " + IConstants.FAILED_COLOR	+ ">Customer ID Or Name Exists already. Try again with diffrent Customer ID Or Name.</font>";
					res = "Customer ID Or Name Exists already. Try again with diffrent Customer ID Or Name";
					 JSONObject resultJsonInt = new JSONObject();	
			           JSONArray jsonArray = new JSONArray();
						resultJsonInt.put("STATUS", "FAILED");	
						resultJsonInt.put("MESSAGE", res);
						jsonArray.add(resultJsonInt);
						jsonObjectResult.put("customer", jsonArray);
				}
				
			
				
				
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();	
			}
			
			
			
			CREDITLIMIT = StrUtils.addZeroes(CREDITLIMITVALUE, numberOfDecimal);
			OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private JSONObject getautiincremtid(HttpServletRequest request,String sCustCode) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expUtil = new ExpensesUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			
					JSONObject resultJsonInt = new JSONObject();					
				
					resultJsonInt.put("CID", sCustCode);
										
					jsonArray.add(resultJsonInt);
		    
		    	resultJson.put("customer", jsonArray);
	            JSONObject resultJsonInti = new JSONObject();
	            resultJsonInti.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInti.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInti);
	            resultJson.put("errors", jsonArrayErr);
		
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
	
	private JSONObject getsavedsuplier(HttpServletRequest request,String sCustCode, String sCustname) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expUtil = new ExpensesUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			
					JSONObject resultJsonInt = new JSONObject();					
				
					resultJsonInt.put("CID", sCustCode);
					resultJsonInt.put("CName", sCustname);
										
					jsonArray.add(resultJsonInt);
		    
		    	resultJson.put("customer", jsonArray);
	            JSONObject resultJsonInti = new JSONObject();
	            resultJsonInti.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInti.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInti);
	            resultJson.put("errors", jsonArrayErr);
		
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
	private JSONObject getsavedsuplierwithtax(HttpServletRequest request,String sCustCode, String sCustname, String sTAXTREATMENT,String currency,String transport,String sContactName,String sDesgination,String sTelNo,String sHpNo,String sEmail,String sAddr1,String sAddr2,String sAddr3,String sAddr4,String sCountry,String sState,
			String sZip,String shipZip,String shipContactName,String dob,String nationality,String shipDesgination,String shipWORKPHONE,String shipHpNo,String shipEmail,String shipCountry,String shipAddr1,String shipAddr2,String shipAddr3,String shipAddr4,String shipState) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expUtil = new ExpensesUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		CurrencyDAO currencyDAO = new CurrencyDAO();
		TransportModeDAO transportmodedao = new TransportModeDAO();
		String transportmode = "";
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
		try {
			
			        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
					JSONObject resultJsonInt = new JSONObject();	

					CustUtil custUtil = new CustUtil();
					ArrayList arrCust = custUtil.getCustomerDetails(sCustCode,
							plant);
					transport = (String) arrCust.get(57);
		    		int trans = Integer.valueOf(transport);
		    		if(trans > 0){
		    			transportmode = transportmodedao.getTransportModeById(plant,trans);
		    		}
		    	else{
		    		transportmode = "";
		    	}					
					resultJsonInt.put("STATUS", "SUCCESS");
					resultJsonInt.put("CID", sCustCode);
					resultJsonInt.put("CName", sCustname);
					resultJsonInt.put("CTAXTREATMENT", sTAXTREATMENT);
					resultJsonInt.put("CURRENCY_ID", currency); 	//Author Name:Resviya ,Date:14/07/21
					resultJsonInt.put("TRANSPORTID", (String)arrCust.get(57));
					resultJsonInt.put("TRANSPORTNAME", transportmode);
					resultJsonInt.put("CNAME", (String)arrCust.get(1));
					resultJsonInt.put("ADDR1", (String)arrCust.get(2));
					resultJsonInt.put("ADDR2", (String)arrCust.get(3));
					resultJsonInt.put("ADDR3", (String)arrCust.get(4));
					resultJsonInt.put("ADDR4", (String)arrCust.get(16));
					resultJsonInt.put("COUNTRY", (String)arrCust.get(5));
					resultJsonInt.put("STATE", (String)arrCust.get(22));
					resultJsonInt.put("ZIP", (String)arrCust.get(6));
					resultJsonInt.put("HPNO", (String)arrCust.get(12));
					resultJsonInt.put("EMAIL", (String)arrCust.get(14));
					resultJsonInt.put("SHIPCONTACTNAME", (String)arrCust.get(44));
					resultJsonInt.put("SHIPDESGINATION", (String)arrCust.get(45));
					resultJsonInt.put("SHIPADDR1", (String)arrCust.get(50));
					resultJsonInt.put("SHIPADDR2", (String)arrCust.get(51));
					resultJsonInt.put("SHIPADDR3", (String)arrCust.get(52));
					resultJsonInt.put("SHIPADDR4", (String)arrCust.get(53));
					resultJsonInt.put("SHIPSTATE", (String)arrCust.get(54));
					resultJsonInt.put("SHIPCOUNTRY", (String)arrCust.get(49));
					resultJsonInt.put("SHIPZIP", (String)arrCust.get(55));
					resultJsonInt.put("SHIPHPNO", (String)arrCust.get(47));
					resultJsonInt.put("SHIPWORKPHONE", (String)arrCust.get(46));
					resultJsonInt.put("SHIPEMAIL", (String)arrCust.get(48));
					resultJsonInt.put("PAY_TERMS", (String)arrCust.get(18));
					resultJsonInt.put("PAYMENT_TERMS", (String)arrCust.get(59));
					resultJsonInt.put("DOB", (String)arrCust.get(61));
					resultJsonInt.put("NATIONALITY", (String)arrCust.get(62));
					List curQryList=new ArrayList();
					curQryList = currencyDAO.getCurrencyDetails(currency,plant);
					for(int i =0; i<curQryList.size(); i++) {
						ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
						resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
				        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
				        double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); 
				        Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
				   	    resultJsonInt.put("CURRENCYUSEQT", Curramt);
				   			 
				        }
					/*ArrayList listQry = currencyDAO.getcurrencydetails(currency, plant, "");
		               if (listQry.size() > 0) {
		            	   
			                   Map arrCustcur = (Map)listQry.get(0);
			                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
			                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
			                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
			                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
			                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
			                   
			            }*/
					
					jsonArray.add(resultJsonInt);
		    
		    	resultJson.put("customer", jsonArray);
	            JSONObject resultJsonInti = new JSONObject();
	            resultJsonInti.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInti.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInti);
	            resultJson.put("errors", jsonArrayErr);
		
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
