package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.track.constants.MLoggerConstant;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.SupplierCreditDAO;
import com.track.dao.TaxSettingDAO;
import com.track.db.object.InvPaymentAttachment;
import com.track.db.util.BillPaymentUtil;
import com.track.db.util.TaxSettingUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

@WebServlet("/TaxSettingServlet")
public class TaxSettingServlet  extends HttpServlet implements IMLogger  {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.TaxSettingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TaxSettingServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private TaxSettingUtil taxSettingUtil=new TaxSettingUtil();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION")).trim();
		String TaxCode="",TaxRegNo="",isEnableTrade="",vatregdate="",generatefirsttax="",reportingperiod="",TaxId="";
		TaxId = StrUtils.fString(request.getParameter("taxid"));
		TaxCode = StrUtils.fString(request.getParameter("TaxCode"));
		TaxRegNo = StrUtils.fString(request.getParameter("TaxRegNo"));
		isEnableTrade = StrUtils.fString(request.getParameter("ENABLE_TRADE"));
		System.out.println("Tax id---"+TaxId);
		vatregdate = StrUtils.fString(request.getParameter("vatregdate"));
		generatefirsttax = StrUtils.fString(request.getParameter("generatefirsttax"));
		reportingperiod = StrUtils.fString(request.getParameter("reportingperiod"));
		boolean isEdit=false;
		if(action.equalsIgnoreCase("Save")) {
			//System.out.println("HI Tax settings");
			
			  if (isEnableTrade.equalsIgnoreCase("on")) 
				  isEnableTrade = "1";
			  else
				  isEnableTrade = "0";
			 
			  Hashtable htData = new Hashtable();
			  if(TaxId.isEmpty())
			  {
				  isEdit=false;
			  }
			  else
			  {
				  isEdit=true;
			  }
			
			htData.put("PLANT", plant);
			htData.put("TAX", TaxRegNo);
			htData.put("TAXBYLABEL", TaxCode);
			htData.put("ISINTERNATIONALTRADE", isEnableTrade);
			htData.put("VATREGISTEREDON", vatregdate);
			htData.put("RETURNFROM", generatefirsttax);
			htData.put("REPORTINGPERIOD", reportingperiod);
			taxSettingUtil.addTaxSettings(htData, plant,isEdit,TaxId);
			if(region.equalsIgnoreCase("GCC"))
			response.sendRedirect("../tax/gstsetting");
			else if(region.equalsIgnoreCase("ASIA PACIFIC"))
				response.sendRedirect("../tax/sg-gstsetting");
		}
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		if(action.equalsIgnoreCase("getTaxSetting")) {
			TaxSettingDAO taxSettingDAO=new TaxSettingDAO();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			try {
				Map<String,String> taxSetting =  taxSettingDAO.getTaxSetting(ht);
				Map<String,String> isTaxReg =  taxSettingDAO.IsTaxRegistered(ht);
				JSONObject taxSettingJson=new JSONObject();
				
				if(taxSetting.isEmpty())
				{
					Map<String,String> taxSettingNew =  taxSettingDAO.getTaxSettingFromPlanMast(ht);
					taxSettingJson.putAll(taxSettingNew);
				}
				else
				{
					taxSettingJson.putAll(taxSetting);
				}
				taxSettingJson.put("isTaxReg", isTaxReg.get("ISTAXREGISTRED"));
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
