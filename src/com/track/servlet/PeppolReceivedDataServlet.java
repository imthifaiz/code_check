package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.gates.DbBean;
import com.track.dao.MovHisDAO;
import com.track.dao.PeppolReceivedDataDAO;
import com.track.db.object.PEPPOL_RECEIVED_DATA;
import com.track.service.PeppolReceivedDataService;
import com.track.serviceImplementation.PeppolReceivedDataServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/peppolreceive")
public class PeppolReceivedDataServlet extends HttpServlet implements IMLogger  {
	
	private static final long serialVersionUID = 1L;
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		PeppolReceivedDataService peppolReceivedDataService = new PeppolReceivedDataServiceImpl();
		PeppolReceivedDataDAO peppolReceivedDataDAO = new PeppolReceivedDataDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			
			String Plant = request.getParameter("PLANT");
			String event = request.getParameter("EVENT");
			String docid = request.getParameter("DOC_ID");
			String receivedat = request.getParameter("RECEIVEDAT");
			String invoicefileurl = request.getParameter("INVOICEFILEURL");
			String evidencefileurl = request.getParameter("EVIDENCEFILEURL");
			String billno = request.getParameter("BILLNO");
			String expiresat = request.getParameter("EXPIRESAT");
			String bill_status = request.getParameter("BILL_STATUS");
			short billstatus = Short.parseShort(bill_status);
			
			UserTransaction ut = null;
			String result="";
			try {
				ut = DbBean.getUserTranaction();				
				ut.begin();
				 if (docid != null)    {
					 		PEPPOL_RECEIVED_DATA PeppolReceived = new PEPPOL_RECEIVED_DATA();
					 		PeppolReceived.setPLANT(Plant);
					 		PeppolReceived.setDOCID(docid);
					 		PeppolReceived.setEVENT(event);
					 		PeppolReceived.setRECEIVEDAT(receivedat);
					 		PeppolReceived.setINVOICEFILEURL(invoicefileurl);
					 		PeppolReceived.setEVIDENCEFILEURL(evidencefileurl);
					 		PeppolReceived.setBILLNO(billno);
					 		PeppolReceived.setEXPIRESAT(expiresat);
					 		PeppolReceived.setBILL_STATUS(billstatus);
					 		PeppolReceived.setCRAT(dateutils.getDate());
					 		PeppolReceived.setCRBY(username);
		 					   
		 	    			int ID = peppolReceivedDataService.addPeppolReceivedData(PeppolReceived);
		 	    			
		 	    			Hashtable htMovHis = new Hashtable();
		 					htMovHis.clear();
		 					htMovHis.put(IDBConstants.PLANT, plant);					
		 					htMovHis.put("DIRTYPE", "CREATE PEPPOL_RECEIVED_DATA");	
		 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
		 					htMovHis.put("RECID", "");
		 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ID);
		 					htMovHis.put(IDBConstants.CREATED_BY, username);		
		 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		 	     }
				 
				DbBean.CommitTran(ut);
				result = "Peppol Received added successfully.";
				response.sendRedirect("../ / ?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Peppol Received.";
				response.sendRedirect("../ / ?result="+ result);
			}
		}
		
		if(action.equalsIgnoreCase("Update")) {
		
			String Plant = request.getParameter("PLANT");
			String event = request.getParameter("EVENT");
			String docid = request.getParameter("DOC_ID");
			String receivedat = request.getParameter("RECEIVEDAT");
			String invoicefileurl = request.getParameter("INVOICEFILEURL");
			String evidencefileurl = request.getParameter("EVIDENCEFILEURL");
			String billno = request.getParameter("BILLNO");
			String expiresat = request.getParameter("EXPIRESAT");
			String bill_status = request.getParameter("BILL_STATUS");
			short billstatus = Short.parseShort(bill_status);
			String id = request.getParameter("ID");
			int Id = Integer.parseInt(id);
			
			UserTransaction ut = null;
			String result="";
			try {
				ut = DbBean.getUserTranaction();				
				ut.begin();
					
					PEPPOL_RECEIVED_DATA PeppolReceivedDataUpdate = peppolReceivedDataService.getPeppolReceivedDataById(Plant,Id);
					
					PeppolReceivedDataUpdate.setPLANT(Plant);
					PeppolReceivedDataUpdate.setDOCID(docid);
			 		PeppolReceivedDataUpdate.setEVENT(event);
			 		PeppolReceivedDataUpdate.setRECEIVEDAT(receivedat);
			 		PeppolReceivedDataUpdate.setINVOICEFILEURL(invoicefileurl);
			 		PeppolReceivedDataUpdate.setEVIDENCEFILEURL(evidencefileurl);
			 		PeppolReceivedDataUpdate.setBILLNO(billno);
			 		PeppolReceivedDataUpdate.setEXPIRESAT(expiresat);
			 		PeppolReceivedDataUpdate.setBILL_STATUS(billstatus);
					PeppolReceivedDataUpdate.setUPAT(dateutils.getDate());
					PeppolReceivedDataUpdate.setUPBY(username);
					
					peppolReceivedDataService.updatePeppolReceivedData(PeppolReceivedDataUpdate, username,Id);
					
					Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", "UPDATE PEPPOL_RECEIVED_DATA");	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", "");
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, docid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Peppol Received updated successfully.";
					response.sendRedirect("../ / ?result="+ result);
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Peppol Received.";
				response.sendRedirect("../ / ?ID="+Id+"&result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("DELETE"))
		{
			String Id = request.getParameter("DOCID");
			plant = request.getParameter("PLANT");
			JSONObject resultJson = new JSONObject();
			try {
				peppolReceivedDataDAO.DeletePeppolReceivedData(plant, Integer.valueOf(Id));
					resultJson.put("STATUS", "OK"); 
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			response.getWriter().write(resultJson.toString());
		}
		
		if(action.equalsIgnoreCase("GET_PPPOL_RECEIVED_DATA"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			JSONArray jsonArray = new JSONArray();
			
			try {
				plant = request.getParameter("PLANT");
				List<PEPPOL_RECEIVED_DATA> PeppolReceivedDataList = peppolReceivedDataService.getAllPeppolReceivedData(plant);
				if (PeppolReceivedDataList.size() > 0) {
					/*for(int i =0; i<PeppolReceivedDataList.size(); i++) {
				Map arrCustLine = (Map)PeppolReceivedDataList.get(i);
				JSONObject resultJsonInt = new JSONObject();
				
				resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
				resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
				resultJsonInt.put("EVENT", (String)arrCustLine.get("EVENT"));
				resultJsonInt.put("DOC_ID", (String)arrCustLine.get("DOC_ID"));
				resultJsonInt.put("RECEIVEDAT", (String)arrCustLine.get("RECEIVEDAT"));
				resultJsonInt.put("INVOICEFILEURL", (String)arrCustLine.get("INVOICEFILEURL"));
				resultJsonInt.put("EVIDENCEFILEURL", (String)arrCustLine.get("EVIDENCEFILEURL"));
				resultJsonInt.put("BILLNO", (String)arrCustLine.get("BILLNO"));
				resultJsonInt.put("EXPIRESAT", (String)arrCustLine.get("EXPIRESAT"));
				resultJsonInt.put("BILL_STATUS", (String)arrCustLine.get("BILL_STATUS"));
				jsonArray.add(resultJsonInt);
				}*/
				resultJson.put("PEPPOLRECEIVEDDATALIST", PeppolReceivedDataList);
				}else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("PEPPOLRECEIVEDDATALIST", jsonArray);
				}
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			response.getWriter().write(resultJson.toString());
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		PeppolReceivedDataService peppolReceivedDataService = new PeppolReceivedDataServiceImpl();
		
		if(action.equalsIgnoreCase("DELETE"))
		{
			String Id= StrUtils.fString(req.getParameter("DOCID"));
			JSONObject resultJson = new JSONObject();
			PeppolReceivedDataDAO peppolReceivedDataDAO = new PeppolReceivedDataDAO();
			try {
				peppolReceivedDataDAO.DeletePeppolReceivedData(plant, Integer.valueOf(Id));
					resultJson.put("STATUS", "OK"); 
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			resp.getWriter().write(resultJson.toString());
		}
		
		if(action.equalsIgnoreCase("GET_PPPOL_RECEIVED_DATA"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			JSONArray jsonArray = new JSONArray();
			
			try {
				plant = req.getParameter("PLANT");
				List<PEPPOL_RECEIVED_DATA> PeppolReceivedDataList = peppolReceivedDataService.getAllPeppolReceivedData(plant);
				if (PeppolReceivedDataList.size() > 0) {
				for(int i =0; i<PeppolReceivedDataList.size(); i++) {
				Map arrCustLine = (Map)PeppolReceivedDataList.get(i);
				JSONObject resultJsonInt = new JSONObject();
				
				resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
				resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
				resultJsonInt.put("EVENT", (String)arrCustLine.get("EVENT"));
				resultJsonInt.put("DOC_ID", (String)arrCustLine.get("DOC_ID"));
				resultJsonInt.put("RECEIVEDAT", (String)arrCustLine.get("RECEIVEDAT"));
				resultJsonInt.put("INVOICEFILEURL", (String)arrCustLine.get("INVOICEFILEURL"));
				resultJsonInt.put("EVIDENCEFILEURL", (String)arrCustLine.get("EVIDENCEFILEURL"));
				resultJsonInt.put("BILLNO", (String)arrCustLine.get("BILLNO"));
				resultJsonInt.put("EXPIRESAT", (String)arrCustLine.get("EXPIRESAT"));
				resultJsonInt.put("BILL_STATUS", (String)arrCustLine.get("BILL_STATUS"));
				jsonArray.add(resultJsonInt);
				}
				resultJson.put("PEPPOLRECEIVEDDATALIST", jsonArray);
				}else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("PEPPOLRECEIVEDDATALIST", jsonArray);
				}
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			resp.getWriter().write(resultJson.toString());
		}
		
		
		if(action.equalsIgnoreCase("GET_SUMMARY_BY_ID"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
	        
			try {
				plant = req.getParameter("PLANT");
				List<PEPPOL_RECEIVED_DATA> peppolReceivedDataList = peppolReceivedDataService.getAllPeppolReceivedData(plant);
				for (PEPPOL_RECEIVED_DATA peppolReceivedDataType : peppolReceivedDataList) {
					PEPPOL_RECEIVED_DATA PeppolReceived = peppolReceivedDataService.getPeppolReceivedDataById(plant,peppolReceivedDataType.getID());
					PEPPOL_RECEIVED_DATA peppolReceivedData = new PEPPOL_RECEIVED_DATA();
					
					peppolReceivedData.setPLANT(PeppolReceived.getPLANT());
					peppolReceivedData.setDOCID(PeppolReceived.getDOCID());
					peppolReceivedData.setEVENT(PeppolReceived.getEVENT());
					peppolReceivedData.setRECEIVEDAT(PeppolReceived.getRECEIVEDAT());
					peppolReceivedData.setINVOICEFILEURL(PeppolReceived.getINVOICEFILEURL());
					peppolReceivedData.setEVIDENCEFILEURL(PeppolReceived.getEVIDENCEFILEURL());
					peppolReceivedData.setBILLNO(PeppolReceived.getBILLNO());
					peppolReceivedData.setEXPIRESAT(PeppolReceived.getEXPIRESAT());
					peppolReceivedData.setBILL_STATUS(PeppolReceived.getBILL_STATUS());
//					peppolReceivedDataList.add(peppolReceivedData);
				}
				
				resultJson.put("PEPPOLRECEIVEDDATALIST", peppolReceivedDataList);   
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			resp.getWriter().write(resultJson.toString());
		}
		
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
