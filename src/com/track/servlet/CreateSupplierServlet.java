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
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.TransportModeDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.ExpensesUtil;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class CreateSupplerServlet
 */
@WebServlet("/CreateSupplierServlet")
public class CreateSupplierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateSupplierServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			StrUtils strUtils = new StrUtils();
			String sUserId = strUtils.fString(request.getParameter("LOGIN_USER"));
			String res = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			String sNewEnb = "enabled";
			String sDeleteEnb = "enabled";
			String sAddEnb = "enabled";
			String sUpdateEnb = "enabled";
			String sCustEnb = "enabled";
			String action = "";
			String redirecturl="";
			String sCustCode = "", sCustName = "", sCustNameL = "",companyregnumber="",transport="", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sCountry = "", sZip = "", sCons = "Y";
			String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "",sRcbno="",currency="", 
					sRemarks = "",sPayTerms="",sPaymentTerm="",sPayInDays="",suppliertypeid="",desc="",sState="",sTAXTREATMENT="",nTAXTREATMENT="",BILL_HDR="",sREVERSECHARGE="",sGOODSIMPORT="";
			String CUSTOMEREMAIL="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",OPENINGBALANCE="",WORKPHONE="",PONO="",GRNO="",isgrn="";
			String sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="",PEPPOL="",PEPPOL_ID="";
			DateUtils dateutils = new DateUtils();
			JSONObject jsonObjectResult = new JSONObject();
			
			CustUtil custUtil = new CustUtil();
			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			
			action = strUtils.fString(request.getParameter("action"));
			String plant = strUtils.fString(request.getParameter("PLANT"));
			String username = strUtils.fString(request.getParameter("LOGIN_USER"));
			sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));
			redirecturl = strUtils.fString(request.getParameter("reurl"));
			userBean ub = new userBean();
			String taxbylabel= ub.getTaxByLable(plant);
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
			if (sCustCode.length() <= 0)
			sCustCode = strUtils.fString(request.getParameter("CUST_CODE1"));
			sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
			sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
			companyregnumber=StrUtils.fString(request.getParameter("companyregnumber"));
			transport = strUtils.fString(request.getParameter("TRANSPORTSID"));//imti
			currency = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CURRENCYID_S")));
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
			sRemarks = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
			sPayTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PAYTERMS")));
			if(sPayTerms.equals("0")) {
				sPayTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("SPAYTERMS")));
			}else if(sPayTerms.equals("")) {
				sPayTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("SPAYTERMS")));
			}else {
				sPayTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PAYTERMS")));
			}
			sPaymentTerm = strUtils.InsertQuotes(strUtils.fString(request.getParameter("sup_payment_terms")));
			sPayInDays=strUtils.InsertQuotes(strUtils.fString(request.getParameter("SUP_PMENT_DAYS")));
			suppliertypeid=strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
			
			CUSTOMEREMAIL=strUtils.fString(request.getParameter("CUSTOMEREMAIL"));
			PEPPOL=StrUtils.fString((request.getParameter("PEPPOL") != null) ? "1": "0").trim();
			WEBSITE=strUtils.fString(request.getParameter("WEBSITE"));
			PEPPOL_ID=strUtils.fString(request.getParameter("PEPPOL_ID"));
			FACEBOOK=strUtils.fString(request.getParameter("FACEBOOK"));
			TWITTER=strUtils.fString(request.getParameter("TWITTER"));
			LINKEDIN=strUtils.fString(request.getParameter("LINKEDIN"));
			SKYPE=strUtils.fString(request.getParameter("SKYPE"));
			OPENINGBALANCE=strUtils.fString(request.getParameter("OPENINGBALANCE"));
			WORKPHONE=strUtils.fString(request.getParameter("WORKPHONE"));
			PONO=strUtils.fString(request.getParameter("PONO"));
			GRNO=strUtils.fString(request.getParameter("GRNO"));
			sTAXTREATMENT=strUtils.fString(request.getParameter("TAXTREATMENT"));
			nTAXTREATMENT=strUtils.fString(request.getParameter("nTAXTREATMENT"));
			BILL_HDR=strUtils.fString(request.getParameter("BILL_HDR"));
			sREVERSECHARGE=StrUtils.fString(request.getParameter("REVERSECHARGE"));
			sGOODSIMPORT=StrUtils.fString(request.getParameter("GOODSIMPORT"));
			isgrn=StrUtils.fString(request.getParameter("isgrn"));
			sBANKNAME   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BANKNAME")));
			sIBAN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("IBAN")));
			sBANKROUTINGCODE       = strUtils.fString(request.getParameter("BANKROUTINGCODE"));
			float OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
			List suppliertypelist=custUtil.getVendorTypeList("",plant," AND ISACTIVE ='Y'");
			MovHisDAO mdao = new MovHisDAO(plant);
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			//1. >> New
			if (action.equalsIgnoreCase("Clear")) {

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
				sContactName = "";
				sDesgination = "";
				sTelNo = "";
				sHpNo = "";
				sFax = "";
				sEmail = "";
				sRemarks = "";
				sPayTerms="";
				sPaymentTerm="";
				sPayInDays="";
				sAddEnb = "";
				sCustEnb = "";
				sState="";
				sRcbno="";
				suppliertypeid="";
				PEPPOL="";
				PEPPOL_ID="";
				CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";OPENINGBALANCE="";WORKPHONE="";
				sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";
			} else if (action.equalsIgnoreCase("Auto-ID")) {
				String minseq = "";
				String sBatchSeq = "";
				boolean insertFlag = false;
				String sZero = "";
				//TblControlDAO _TblControlDAO = new TblControlDAO();
				//_TblControlDAO.setmLogger(mLogger);
				Hashtable ht = new Hashtable();

				String query = " isnull(NXTSEQ,'') as NXTSEQ";
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.TBL_FUNCTION, "SUPPLIER");
				try {
					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "S");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sCustCode = "S" + "0001";
					} else {
						//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

						Map m = _TblControlDAO.selectRow(query, ht, "");
						sBatchSeq = (String) m.get("NXTSEQ");
						System.out.println("length" + sBatchSeq.length());

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
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "S");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

						//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
						sCustCode = "S" + sZero + updatedSeq;
						if(redirecturl.contains("createBill.jsp?action=View"))
							response.sendRedirect("jsp/"+redirecturl+"&PONO="+PONO+"&GRNO="+GRNO+"&sCustCode="+sCustCode+"&TAXTREATMENT="+nTAXTREATMENT+"&REVERSECHARGE="+sREVERSECHARGE+"&GOODSIMPORT="+sGOODSIMPORT+"&isgrn="+isgrn);
						else if(redirecturl.contains("editSupplierCredit.jsp"))
							response.sendRedirect("jsp/"+redirecturl+"&BILL_HDR="+BILL_HDR+"&sCustCode="+sCustCode+"&TAXTREATMENT="+nTAXTREATMENT+"&REVERSECHARGE="+sREVERSECHARGE+"&GOODSIMPORT="+sGOODSIMPORT);
						else if(redirecturl.contains("CopySupplierCreditNotes.jsp"))
							response.sendRedirect("jsp/"+redirecturl+"&BILL_HDR="+BILL_HDR+"&sCustCode="+sCustCode+"&TAXTREATMENT="+nTAXTREATMENT+"&REVERSECHARGE="+sREVERSECHARGE+"&GOODSIMPORT="+sGOODSIMPORT);
						else
							response.sendRedirect("jsp/"+redirecturl+".jsp?sCustCode="+sCustCode);
					}
				} catch (Exception e) {
					//mLogger.exception(true,"ERROR IN JSP PAGE - vender_view.jsp ", e);
				}
			}
			//2. >> Add
			else if (action.equalsIgnoreCase("ADD")) {
				if (!custUtil.isExistVendor(sCustCode, plant) && !custUtil.isExistVendorName(sCustName, plant)) // if the Customer exists already
				{
					
					OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
					OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
					  
					Hashtable ht = new Hashtable();
					ht.put(IConstants.PLANT, plant);
					ht.put(IConstants.VENDOR_CODE, sCustCode);
					ht.put(IConstants.VENDOR_NAME, sCustName);
				    ht.put(IConstants.companyregnumber,companyregnumber);
				    ht.put(IConstants.TRANSPORTID, transport);
					ht.put("CURRENCY_ID", currency);
					ht.put(IConstants.NAME, sContactName);
					ht.put(IConstants.DESGINATION, sDesgination);
					ht.put(IConstants.TELNO, sTelNo);
					ht.put(IConstants.HPNO, sHpNo);
					ht.put(IConstants.FAX, sFax);
					ht.put(IConstants.EMAIL, sEmail);
					ht.put(IConstants.ADDRESS1, sAddr1);
					ht.put(IConstants.ADDRESS2, sAddr2);
					ht.put(IConstants.ADDRESS3, sAddr3);
					ht.put(IConstants.ADDRESS4, sAddr4);
					ht.put(IConstants.COUNTRY, sCountry);
					ht.put(IConstants.ZIP, sZip);
					ht.put(IConstants.USERFLG1, sCons);
					ht.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
					ht.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
					ht.put("PAYMENT_TERMS", strUtils.InsertQuotes(sPaymentTerm));
					ht.put(IConstants.PAYINDAYS, sPayInDays);
					ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
					ht.put(IConstants.CREATED_BY, sUserId);
					ht.put(IConstants.ISACTIVE, "Y");
					
					ht.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        ht.put(IConstants.WEBSITE,WEBSITE);
			        ht.put("ISPEPPOL",PEPPOL);
					ht.put("PEPPOL_ID",PEPPOL_ID);
			        ht.put(IConstants.FACEBOOK,FACEBOOK);
			        ht.put(IConstants.TWITTER,TWITTER);
			        ht.put(IConstants.LINKEDIN,LINKEDIN);
			        ht.put(IConstants.SKYPE,SKYPE);
			        ht.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        ht.put(IConstants.WORKPHONE,WORKPHONE);
					
					ht.put("Comment1", " 0 ");
					ht.put(IConstants.STATE, sState);
					ht.put(IConstants.RCBNO, sRcbno);
					ht.put(IConstants.SUPPLIERTYPEID, suppliertypeid);
					ht.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        	  sBANKNAME="";
			          ht.put(IDBConstants.BANKNAME,sBANKNAME);
			          ht.put(IDBConstants.IBAN,sIBAN);
			          ht.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
					String sysTime = DateUtils.Time();
					String sysDate = DateUtils.getDate();
					sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

					//mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE", TransactionConstants.ADD_SUP);
					htm.put("RECID", "");
					htm.put("ITEM",sCustCode);
					if(!sRemarks.equals(""))
					{
						htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
					}
					else
					{
						htm.put(IDBConstants.REMARKS, sCustName);
					}
					
					htm.put(IDBConstants.CREATED_BY, username);
					htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				
				boolean updateFlag;
					if(sCustCode!="S0001")
			  		  {	
						boolean exitFlag = false;
						Hashtable htv = new Hashtable();				
						htv.put(IDBConstants.PLANT, plant);
						htv.put(IDBConstants.TBL_FUNCTION, "SUPPLIER");
						exitFlag = _TblControlDAO.isExisit(htv, "", plant);
						if (exitFlag) 
							updateFlag=_TblControlDAO.updateSeqNo("SUPPLIER",plant);				
					else
					{
						boolean insertFlag = false;
						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "S");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
					}
					}
				
					boolean custInserted = custUtil.insertVendor(ht);
					boolean inserted = mdao.insertIntoMovHis(htm);
					if (custInserted) {

						res = "<font class = " + IConstants.SUCCESS_COLOR+ ">Supplier Added Successfully</font>";
						//                    sAddEnb  = "disabled";
						sCustEnb = "disabled";
					} else {
						res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to add New Supplier</font>";
						//                    sAddEnb  = "enabled";
						sCustEnb = "enabled";
					}
				} else {
					res = "<font class = " + IConstants.FAILED_COLOR
							+ ">Supplier ID Or Name Exists already. Try again with diffrent Supplier ID Or Name.</font>";
					//           sAddEnb = "enabled";
					sCustEnb = "enabled";
				}
				if(redirecturl.contains("createBill.jsp?action=View"))
					response.sendRedirect("jsp/"+redirecturl+"&PONO="+PONO+"&GRNO="+GRNO+"&VEND_NAME="+sCustName+"&VENDNO="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT+"&REVERSECHARGE="+sREVERSECHARGE+"&GOODSIMPORT="+sGOODSIMPORT+"&isgrn="+isgrn);				
				else if(redirecturl.contains("editSupplierCredit.jsp"))
					response.sendRedirect("jsp/"+redirecturl+"&BILL_HDR="+BILL_HDR+"&VEND_NAME="+sCustName+"&VENDNO="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT+"&REVERSECHARGE="+sREVERSECHARGE+"&GOODSIMPORT="+sGOODSIMPORT);
				else if(redirecturl.contains("CopySupplierCreditNotes.jsp"))
					response.sendRedirect("jsp/"+redirecturl+"&BILL_HDR="+BILL_HDR+"&VEND_NAME="+sCustName+"&VENDNO="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT+"&REVERSECHARGE="+sREVERSECHARGE+"&GOODSIMPORT="+sGOODSIMPORT);
				else
					response.sendRedirect("jsp/"+redirecturl+".jsp?VEND_NAME="+sCustName+"&VENDNO="+sCustCode+"&TAXTREATMENT="+sTAXTREATMENT);
			}

			//3. >> Update
			else if (action.equalsIgnoreCase("UPDATE")) {
				sCustEnb = "disabled";
				//    sAddEnb  = "disabled";
				if (custUtil.isExistVendor(sCustCode, plant)) {
					Hashtable htUpdate = new Hashtable();
					htUpdate.put(IConstants.PLANT, plant);
					htUpdate.put(IConstants.VENDOR_CODE, sCustCode);
					htUpdate.put(IConstants.VENDOR_NAME, sCustName);
					htUpdate.put(IConstants.companyregnumber,companyregnumber);
					htUpdate.put(IConstants.TRANSPORTID, transport);
					//          htUpdate.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
					htUpdate.put("CURRENCY_ID", currency);
					htUpdate.put(IConstants.NAME, sContactName);
					htUpdate.put(IConstants.DESGINATION, sDesgination);
					htUpdate.put(IConstants.TELNO, sTelNo);
					htUpdate.put(IConstants.HPNO, sHpNo);
					htUpdate.put(IConstants.FAX, sFax);
					htUpdate.put(IConstants.EMAIL, sEmail);
					htUpdate.put(IConstants.ADDRESS1, sAddr1);
					htUpdate.put(IConstants.ADDRESS2, sAddr2);
					htUpdate.put(IConstants.ADDRESS3, sAddr3);
					htUpdate.put(IConstants.ADDRESS4, sAddr4);
					htUpdate.put(IConstants.STATE, sState);
					htUpdate.put(IConstants.COUNTRY, sCountry);
					htUpdate.put(IConstants.ZIP, sZip);
					htUpdate.put(IConstants.USERFLG1, sCons);
					htUpdate.put(IConstants.REMARKS, sRemarks);
					htUpdate.put(IConstants.PAYTERMS, sPayTerms);
					htUpdate.put("PAYMENT_TERMS", sPaymentTerm);
					htUpdate.put(IConstants.PAYINDAYS, sPayInDays);
					htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
					htUpdate.put(IConstants.UPDATED_BY, sUserId);
					//htUpdate.put(IConstants.RCBNO, sRcbno);
					htUpdate.put(IConstants.SUPPLIERTYPEID, suppliertypeid);
					htUpdate.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
					Hashtable htCondition = new Hashtable();
					htCondition.put("VENDNO", sCustCode);
					htCondition.put(IConstants.PLANT, plant);

					String sysTime = DateUtils.Time();
					String sysDate = DateUtils.getDate();
					sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

					
					//mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE", "UPD_SUP");
					htm.put("RECID", "");
					htm.put("UPBY", username);
					htm.put("CRBY", username);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
					boolean custUpdated = custUtil.updateVendor(htUpdate,htCondition);
					boolean inserted = mdao.insertIntoMovHis(htm);
					if (custUpdated) {
						res = "<font class = " + IConstants.SUCCESS_COLOR+ ">Supplier Updated Successfully</font>";
					} else {
						res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to Update Supplier</font>";
					}
				} else {
					res = "<font class = " + IConstants.FAILED_COLOR+ ">Supplier doesn't not Exists. Try again</font>";
					

				}
			}

			//4. >> Delete
			else if (action.equalsIgnoreCase("DELETE")) {
				sCustEnb = "disabled";
				
					if (custUtil.isExistVendor(sCustCode, plant)) {
					boolean custDeleted = custUtil.deleteVendor(sCustCode,
							plant);
					if (custDeleted) {
						res = "<font class = " + IConstants.SUCCESS_COLOR
								+ ">Supplier Deleted Successfully</font>";
						//                    sAddEnb    = "disabled";
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
						sContactName = "";
						sDesgination = "";
						sTelNo = "";
						sHpNo = "";
						sFax = "";
						sEmail = "";
						sRemarks = "";
						sPayTerms="";
						sPaymentTerm="";
						sPayInDays="";
						sCons = "Y";
						sRcbno="";
						PEPPOL="";
						PEPPOL_ID="";
						CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";OPENINGBALANCE="";WORKPHONE="";

					} else {
						res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to Delete Supplier</font>";
						sAddEnb = "enabled";
					}
				} else {
					res = "<font class = " + IConstants.FAILED_COLOR+ ">Supplier doesn't not Exists. Try again</font>";
				}
			
			}

			//4. >> View
			else if (action.equalsIgnoreCase("VIEW")) {
				try {
					ArrayList arrCust = custUtil.getVendorDetails(sCustCode,
							plant);
					sCustCode = (String) arrCust.get(0);
					sCustName = (String) arrCust.get(1);
					//sCustName   = (String)arrCust.get(2);
					sAddr1 = (String) arrCust.get(2);
					sAddr2 = (String) arrCust.get(3);
					sAddr3 = (String) arrCust.get(4);
					sAddr4 = (String) arrCust.get(15);
					sCountry = (String) arrCust.get(5);
					sZip = (String) arrCust.get(6);
					sCons = (String) arrCust.get(7);
					sContactName = (String) arrCust.get(8);
					sDesgination = (String) arrCust.get(9);
					sTelNo = (String) arrCust.get(10);
					sHpNo = (String) arrCust.get(11);
					sEmail = (String) arrCust.get(12);
					sFax = (String) arrCust.get(13);
					sRemarks = (String) arrCust.get(14);
					sPayTerms = (String) arrCust.get(17);
					sPaymentTerm = (String) arrCust.get(39);
					sPayInDays = (String) arrCust.get(18);
					suppliertypeid = (String) arrCust.get(19);
					currency = (String) arrCust.get(16);
					transport = (String) arrCust.get(38);
					PEPPOL = (String) arrCust.get(40);
					PEPPOL_ID = (String) arrCust.get(41);
					

				} catch (Exception e) {
					res = "no details found for Vendor id : " + sCustCode;
				}

			} else if (action.equalsIgnoreCase("JAuto-ID")) {
				

				String minseq = "";
				String sBatchSeq = "";
				boolean insertFlag = false;
				String sZero = "";
				//TblControlDAO _TblControlDAO = new TblControlDAO();
				//_TblControlDAO.setmLogger(mLogger);
				Hashtable ht = new Hashtable();

				String query = " isnull(NXTSEQ,'') as NXTSEQ";
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.TBL_FUNCTION, "SUPPLIER");
				try {
					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "S");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sCustCode = "S" + "0001";
					} else {
						//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

						Map m = _TblControlDAO.selectRow(query, ht, "");
						sBatchSeq = (String) m.get("NXTSEQ");
						System.out.println("length" + sBatchSeq.length());

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
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "S");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

						//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
						sCustCode = "S" + sZero + updatedSeq;
						
					}
				} catch (Exception e) {
					//mLogger.exception(true,"ERROR IN JSP PAGE - vender_view.jsp ", e);
				}
			
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getautiincremtid(request,sCustCode);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();	
			} else if (action.equalsIgnoreCase("JADD")) {
				

				if (!custUtil.isExistVendor(sCustCode, plant) && !custUtil.isExistVendorName(sCustName, plant)) // if the Customer exists already
				{
					
					OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
					OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
					  
					Hashtable ht = new Hashtable();
					ht.put(IConstants.PLANT, plant);
					ht.put(IConstants.VENDOR_CODE, sCustCode);
					ht.put(IConstants.VENDOR_NAME, sCustName);
				    ht.put(IConstants.companyregnumber,companyregnumber);
				    ht.put(IConstants.TRANSPORTID, transport);
					ht.put("CURRENCY_ID", currency);
					ht.put(IConstants.NAME, sContactName);
					ht.put(IConstants.DESGINATION, sDesgination);
					ht.put(IConstants.TELNO, sTelNo);
					ht.put(IConstants.HPNO, sHpNo);
					ht.put(IConstants.FAX, sFax);
					ht.put(IConstants.EMAIL, sEmail);
					ht.put(IConstants.ADDRESS1, sAddr1);
					ht.put(IConstants.ADDRESS2, sAddr2);
					ht.put(IConstants.ADDRESS3, sAddr3);
					ht.put(IConstants.ADDRESS4, sAddr4);
					ht.put(IConstants.COUNTRY, sCountry);
					ht.put(IConstants.ZIP, sZip);
					ht.put(IConstants.USERFLG1, sCons);
					ht.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
					ht.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
					ht.put("PAYMENT_TERMS", strUtils.InsertQuotes(sPaymentTerm));
					ht.put(IConstants.PAYINDAYS, sPayInDays);
					ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
					ht.put(IConstants.CREATED_BY, sUserId);
					ht.put(IConstants.ISACTIVE, "Y");
					
					ht.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			        ht.put(IConstants.WEBSITE,WEBSITE);
			        ht.put(IConstants.FACEBOOK,FACEBOOK);
			        ht.put(IConstants.TWITTER,TWITTER);
			        ht.put(IConstants.LINKEDIN,LINKEDIN);
			        ht.put(IConstants.SKYPE,SKYPE);
			        ht.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			        ht.put(IConstants.WORKPHONE,WORKPHONE);
					
			        ht.put("PEPPOL_ID",PEPPOL_ID);
			        ht.put("ISPEPPOL",PEPPOL);
					ht.put("Comment1", " 0 ");
					ht.put(IConstants.STATE, sState);
					ht.put(IConstants.RCBNO, sRcbno);
					ht.put(IConstants.SUPPLIERTYPEID, suppliertypeid);
					ht.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
			        	  sBANKNAME="";
			          ht.put(IDBConstants.BANKNAME,sBANKNAME);
			          ht.put(IDBConstants.IBAN,sIBAN);
			          ht.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			          
					String sysTime = DateUtils.Time();
					String sysDate = DateUtils.getDate();
					sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

					//mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE", TransactionConstants.ADD_SUP);
					htm.put("RECID", "");
					htm.put("ITEM",sCustCode);
					if(!sRemarks.equals(""))
					{
						htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
					}
					else
					{
						htm.put(IDBConstants.REMARKS, sCustName);
					}
					
					htm.put(IDBConstants.CREATED_BY, username);
					htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				
				boolean updateFlag;
					if(sCustCode!="S0001")
			  		  {	
						boolean exitFlag = false;
						Hashtable htv = new Hashtable();				
						htv.put(IDBConstants.PLANT, plant);
						htv.put(IDBConstants.TBL_FUNCTION, "SUPPLIER");
						exitFlag = _TblControlDAO.isExisit(htv, "", plant);
						if (exitFlag) 
							updateFlag=_TblControlDAO.updateSeqNo("SUPPLIER",plant);				
					else
					{
						boolean insertFlag = false;
						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "S");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
					}
					}
				
					boolean custInserted = custUtil.insertVendor(ht);
					boolean inserted = mdao.insertIntoMovHis(htm);
					if (custInserted) {

						res = "<font class = " + IConstants.SUCCESS_COLOR+ ">Supplier Added Successfully</font>";
						//                    sAddEnb  = "disabled";
						sCustEnb = "disabled";
					} else {
						res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to add New Supplier</font>";
						//                    sAddEnb  = "enabled";
						sCustEnb = "enabled";
					}
				} else {
					res = "<font class = " + IConstants.FAILED_COLOR
							+ ">Supplier ID Or Name Exists already. Try again with diffrent Supplier ID Or Name.</font>";
					//           sAddEnb = "enabled";
					sCustEnb = "enabled";
				}
				
				
				jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getsavedsuplier(request,sCustCode,sCustName,sTAXTREATMENT,currency,transport,sPaymentTerm,sPayTerms,sContactName,sAddr1,sAddr2,sAddr3,sAddr4,sState,sCountry,sZip,sHpNo,sEmail);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();	
			}
				
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				
					resultJsonInt.put("SID", sCustCode);
										
					jsonArray.add(resultJsonInt);
		    
		    	resultJson.put("supplier", jsonArray);
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
	
	private JSONObject getsavedsuplier(HttpServletRequest request,String sCustCode, String sCustname,String sTAXTREATMENT,String currency,String transport,String sPaymentTerm,String sPayTerms,String sContactName,String sAddr1,String sAddr2,String sAddr3,String sAddr4,String sState,String sCountry,String sZip,String sHpNo,String sEmail) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expUtil = new ExpensesUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		CurrencyDAO currencyDAO = new CurrencyDAO();
		TransportModeDAO transportmodedao = new TransportModeDAO();
		String transportmode = "";
		String QUERY= StrUtils.fString(request.getParameter("QUERY"));
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();

		try {
	                String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
					JSONObject resultJsonInt = new JSONObject();					
				
					CustUtil custUtil = new CustUtil();
					ArrayList arrCust = custUtil.getVendorDetails(sCustCode,
							plant);
					transport = (String) arrCust.get(38);
		    		int trans = Integer.valueOf(transport);
		    		if(trans > 0){
		    			transportmode = transportmodedao.getTransportModeById(plant,trans);
		    		}
		    	else{
		    		transportmode = "";
		    	}
		        		
					resultJsonInt.put("SID", sCustCode);
					resultJsonInt.put("SName", sCustname);
					resultJsonInt.put("sTAXTREATMENT", sTAXTREATMENT);
					resultJsonInt.put("sCURRENCY_ID", currency); 	//Author Name:Resviya ,Date:19/07/21
					resultJsonInt.put("TRANSPORTID", (String)arrCust.get(38));
					resultJsonInt.put("TRANSPORTNAME", transportmode);
					
					resultJsonInt.put("NAME", sContactName);
					resultJsonInt.put("ADDR1", sAddr1);
					resultJsonInt.put("ADDR2", sAddr2);
					resultJsonInt.put("ADDR3", sAddr3);
					resultJsonInt.put("ADDR4", sAddr4);
					resultJsonInt.put("STATE", sState);
					resultJsonInt.put("COUNTRY", sCountry);
					resultJsonInt.put("ZIP", sZip);
					resultJsonInt.put("HPNO", sHpNo);
					resultJsonInt.put("EMAIL", sEmail);
					resultJsonInt.put("PAY_TERMS", sPaymentTerm);
					resultJsonInt.put("PAYMENT_TERMS", sPayTerms);
					

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
		    
		    	resultJson.put("supplier", jsonArray);
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
