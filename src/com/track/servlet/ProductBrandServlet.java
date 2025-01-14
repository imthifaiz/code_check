package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.MovHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.PrdBrandUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.dao.ItemMstDAO;

/**
 * Servlet implementation class ProductBrandServlet
 */
public class ProductBrandServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.PRODUCTBRANDServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PRODUCTBRANDServlet_PRINTPLANTMASTERINFO;
	String action = "";
	TblControlDAO _TblControlDAO = null;
	PrdBrandUtil prdbrandutil = null;
	DateUtils dateutils = null;
	ItemMstDAO itemmstdao = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 _TblControlDAO = new TblControlDAO();
		 prdbrandutil = new PrdBrandUtil();
		 dateutils = new DateUtils();
		 itemmstdao = new ItemMstDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			HttpSession session = request.getSession();
			action = request.getParameter("action").trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			String productBrandID = null, productBrandDesc = null;
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equals("Auto_ID")) {
				productBrandID = this.generateBrandID(plant, userName);
				response.sendRedirect("../productbrand/new?brandID="
						+ productBrandID);

			}
			if (action.equals("ADD")) {
				productBrandID = request.getParameter("ITEM_ID");
				productBrandDesc = request.getParameter("ITEM_DESC");
				String responseMsg = this.addBrand(plant, userName,
						productBrandID, productBrandDesc);
				response.sendRedirect("../productbrand/summary?response="
						+ responseMsg+"&brandID="+productBrandID+"&brandDesc="+productBrandDesc);

			}
			if(action.equals("UPDATE")){
				productBrandID = request.getParameter("PRD_BRAND_ID");
				productBrandDesc = request.getParameter("PRD_BRAND_DESC");
				 String isActive = request.getParameter("ACTIVE");
				String responseMsg = this.updateBrand(plant, userName,
						productBrandID, productBrandDesc,isActive);
				response.sendRedirect("../productbrand/summary?response="
						+ responseMsg+"&brandID="+productBrandID+"&brandDesc="+productBrandDesc+"&isActive="+isActive);
				
			}
			if(action.equals("DELETE")){
				productBrandID = request.getParameter("PRD_BRAND_ID");
				productBrandDesc = request.getParameter("PRD_BRAND_DESC");
				 String isActive = request.getParameter("ACTIVE");
				String responseMsg = this.deleteBrand(plant, userName,
						productBrandID, productBrandDesc,isActive);
				response.sendRedirect("../productbrand/summary?response="
						+ responseMsg+"&brandID="+productBrandID+"&brandDesc="+productBrandDesc+"&isActive="+isActive);
				
			}
			if(action.equals("Summary")){
				productBrandID = request.getParameter("PRD_BRAND_ID");
				String responseMsg = "";
				ArrayList<Map<String, String>> brandItemsList = this.getPrdBrandList(plant,productBrandID);
				if(brandItemsList.size()==0){
					responseMsg = "No data Found";
				}
				session.setAttribute("brandItemsList", brandItemsList);
				response.sendRedirect("jsp/prdBrandSummary.jsp?response="
						+ responseMsg+"&brandID="+productBrandID);
			}
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			request.getSession().setAttribute("res", result);
			response.sendRedirect("jsp/create_prdBrand.jsp");
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
	
	//Adds the brand to the Brand master table -Bruhan - //11/9/12
	private String addBrand(String plant, String userName,
			String ProductBrandId, String ProductBrandDesc)
			throws ServletException, IOException, Exception {
		String response = "",SAVE_RED="";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.PRDBRANDID, ProductBrandId);
		if (!(prdbrandutil.isExistsItemType(ht))) // if the Item exists already
		{
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.PRDBRANDID, ProductBrandId);
			ht.put(IDBConstants.PRDBRANDDESC, ProductBrandDesc);
			ht.put(IConstants.ISACTIVE, "Y");
			new DateUtils();
			ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, userName);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable<String, String> htm = new Hashtable<String, String>();
			htm.put(IDBConstants.PLANT, plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_PRDBRAND);
			htm.put("RECID", "");
			htm.put("ITEM",ProductBrandId);
			htm.put("REMARKS", ProductBrandDesc);
			htm.put("UPBY", userName);
			htm.put("CRBY", userName);
			htm.put("CRAT", DateUtils.getDateTime());
			htm.put("UPAT", DateUtils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, DateUtils
					.getDateinyyyy_mm_dd(DateUtils.getDate()));

			 htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
	          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	          
			  boolean updateFlag;
			if(ProductBrandId!="PB001")
	  		  {	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "PRDBRAND");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
	  		    updateFlag=_TblControlDAO.updateSeqNo("PRDBRAND",plant);
				else
				{
					boolean insertFlag = false;
					Map htInsert=null;
	            	Hashtable htTblCntInsert  = new Hashtable();           
	            	htTblCntInsert.put(IDBConstants.PLANT,plant);          
	            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDBRAND");
	            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"PB");
	             	htTblCntInsert.put("MINSEQ","0000");
	             	htTblCntInsert.put("MAXSEQ","9999");
	            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
	            	htTblCntInsert.put(IDBConstants.CREATED_BY, userName);
	            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
				}
			}
			
			boolean itemInserted = prdbrandutil.insertPrdBrandMst(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (itemInserted && inserted) {
				SAVE_RED="Product Brand Added Successfully";
				/*
				 * response = "<font class = " + IDBConstants.SUCCESS_COLOR +
				 * ">Product Brand Added Successfully</font>";
				 */

			} else {
				SAVE_RED="Failed to add New Product Brand";
				/*
				 * response = "<font class = " + IDBConstants.FAILED_COLOR +
				 * ">Failed to add New Product Brand </font>";
				 */

			}
		} else {
			SAVE_RED="Product Brand  Exists already. Try again";
			/*
			 * response = "<font class = " + IDBConstants.FAILED_COLOR +
			 * ">Product Brand  Exists already. Try again</font>";
			 */

		}
		return SAVE_RED;
	}
	
	
	//Updates the brand to the Brand master table -Bruhan - //11/9/12
	private String updateBrand(String plant, String userName,
			String ProductBrandId, String ProductBrandDesc,String isActive)
			throws ServletException, IOException, Exception {
		String response = "";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.PRDBRANDID, ProductBrandId);
		if ((prdbrandutil.isExistsItemType(ht))) // if the Item exists already
		{
			Hashtable<String, String> htUpdate = new Hashtable<String, String>();
			htUpdate.put(IDBConstants.PRDBRANDID, ProductBrandId);
			htUpdate.put(IDBConstants.PRDBRANDDESC, ProductBrandDesc);
			htUpdate.put(IConstants.ISACTIVE, isActive);
			new DateUtils();
			htUpdate.put(IDBConstants.UPDATED_AT, DateUtils
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, userName);

			Hashtable<String,String> htCondition = new Hashtable<String,String>();
			htCondition.put(IDBConstants.PRDBRANDID, ProductBrandId);
			htCondition.put(IDBConstants.PLANT, plant);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable<String, String> htm = new Hashtable<String, String>();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE",TransactionConstants.UPDATE_PRDBRAND);
			htm.put("RECID", "");
			htm.put("ITEM",ProductBrandId);
			htm.put("UPBY", userName);
			htm.put("CRBY", userName);
			htm.put("CRAT", DateUtils.getDateTime());
			htm.put("REMARKS", ProductBrandDesc);
			htm.put("UPAT", DateUtils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, DateUtils
					.getDateinyyyy_mm_dd(DateUtils.getDate()));

			boolean Updated = prdbrandutil.updatePrdBrand(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (Updated && inserted ) {
				response = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Product Brand  Updated Successfully</font>";
			} else {
				response = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Product Brand </font>";
			}
		} else {
			response = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Product Brand  doesn't not Exists. Try again</font>";

		}
		
		return response;
		
	}
	
	private String deleteBrand(String plant, String userName,
			String ProductBrandId, String ProductBrandDesc,String isActive)
			throws ServletException, IOException, Exception {
		String response = "",sSAVE_REDELETE = "";
		Hashtable htcondition = new Hashtable();
		htcondition.put(IDBConstants.PLANT, plant);
		htcondition.put(IDBConstants.PRDBRANDID, ProductBrandId);
		
		boolean itembrandflag  = itemmstdao.isExisit(htcondition,"");
		if (itembrandflag) {
			sSAVE_REDELETE = "Product Brand Exists In Products";
			/*
			 * response = "<font class = " + IDBConstants.FAILED_COLOR +
			 * " >Product Brand Exists In Products</font>";
			 */
		} 
		else{
			if(prdbrandutil.isExistsItemType(htcondition))
			{
				boolean flag = prdbrandutil.deletePrdBrandId(htcondition);
				
				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable<String, String> htm = new Hashtable<String, String>();
				htm.put("PLANT", plant);
				htm.put("DIRTYPE",TransactionConstants.DEL_PRDBRAND);
				htm.put("RECID", "");
				htm.put("ITEM",ProductBrandId);
				htm.put("UPBY", userName);
				htm.put("CRBY", userName);
				htm.put("CRAT", DateUtils.getDateTime());
				htm.put("REMARKS", ProductBrandDesc);
				htm.put("UPAT", DateUtils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, DateUtils
						.getDateinyyyy_mm_dd(DateUtils.getDate()));
				
				flag = mdao.insertIntoMovHis(htm);
				
				if(flag)
					{
					sSAVE_REDELETE = "Product Brand Deleted Successfully";
					
					/*
					 * response = "<font class = " + IDBConstants.SUCCESS_COLOR +
					 * " >Product Brand Deleted Successfully </font>";
					 */
					}
				else {
					sSAVE_REDELETE = "Failed to Delete Product Brand";
//					response = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Product Brand</font>";
                
      				}
			}else{
				sSAVE_REDELETE = "Product Brand doesn't  Exists. Try again";
//				response = "<font class = "+IConstants.FAILED_COLOR+">Product Brand doesn't  Exists. Try again</font>";
			}
		
		}

		
		
		return sSAVE_REDELETE;
		
	}
	
	//Get the list of brands from the brand Master table. -Bruhan //11/9/12
	private ArrayList<Map<String, String>> getPrdBrandList(String plant,String productBrandID){
		return prdbrandutil.getPrdBrandList(productBrandID,plant,"");
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
}
