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
import com.track.dao.PeppolDocIdsDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.PEPPOL_DOC_IDS;
import com.track.gates.DbBean;
import com.track.service.PeppolDocIdsService;
import com.track.serviceImplementation.PeppolDocIdsServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/peppoldoc")
public class PeppolDocIdsServlet extends HttpServlet implements IMLogger  {
	
	private static final long serialVersionUID = 1L;
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		PeppolDocIdsService peppolDocIdsService = new PeppolDocIdsServiceImpl();
		PeppolDocIdsDAO peppolDocIdsDAO = new PeppolDocIdsDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		
		if(action.equalsIgnoreCase("SAVE")) {
			
			String Plant = request.getParameter("PLANT");
			String docid = request.getParameter("DOC_ID");
			
			UserTransaction ut = null;
			String result="";
			try {
				ut = DbBean.getUserTranaction();				
				ut.begin();
				 if (docid != null)    {
					 		PEPPOL_DOC_IDS PeppolDocIds = new PEPPOL_DOC_IDS();
					 		PeppolDocIds.setPLANT(Plant);
					 		PeppolDocIds.setDOC_ID(docid);
					 		PeppolDocIds.setCRAT(dateutils.getDate());
					 		PeppolDocIds.setCRBY(username);
		 					   
		 	    			int ID = peppolDocIdsService.addPeppolDoc(PeppolDocIds);
		 	    			
		 	    			Hashtable htMovHis = new Hashtable();
		 					htMovHis.clear();
		 					htMovHis.put(IDBConstants.PLANT, plant);					
		 					htMovHis.put("DIRTYPE", "CREATE PEPPOL DOC");	
		 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
		 					htMovHis.put("RECID", "");
		 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ID);
		 					htMovHis.put(IDBConstants.CREATED_BY, username);		
		 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		 	     }
				 
				DbBean.CommitTran(ut);
				result = "Peppol Doc added successfully.";
				response.sendRedirect("../ / ?result="+ result);
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Peppol Doc.";
				response.sendRedirect("../ / ?result="+ result);
			}
		}
		
		if(action.equalsIgnoreCase("Update")) {
		
			String Plant = request.getParameter("PLANT");
			String docid = request.getParameter("DOC_ID");
			String id = request.getParameter("ID");
			int Id = Integer.parseInt(id);
			int DocId = Integer.parseInt(docid);
			
			UserTransaction ut = null;
			String result="";
			try {
				ut = DbBean.getUserTranaction();				
				ut.begin();
					
					PEPPOL_DOC_IDS PeppolDocIdsUpdate = peppolDocIdsService.getPeppolDocById(DocId);
					
					PeppolDocIdsUpdate.setPLANT(Plant);
					PeppolDocIdsUpdate.setDOC_ID(docid);
					PeppolDocIdsUpdate.setUPAT(dateutils.getDate());
					PeppolDocIdsUpdate.setUPBY(username);
					
					peppolDocIdsService.updatePeppolDoc(PeppolDocIdsUpdate, username,DocId);
					
					Hashtable htMovHis = new Hashtable();
 					htMovHis.clear();
 					htMovHis.put(IDBConstants.PLANT, plant);					
 					htMovHis.put("DIRTYPE", "UPDATE PEPPOL DOC");	
 					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
 					htMovHis.put("RECID", "");
 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, docid);
 					htMovHis.put(IDBConstants.CREATED_BY, username);		
 					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					DbBean.CommitTran(ut);
					result = "Peppol Doc updated successfully.";
					response.sendRedirect("../ / ?result="+ result);
				
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't add Peppol Doc.";
				response.sendRedirect("../ / ?ID="+DocId+"&result="+ result);
			}
	 	    	  
		}
		
		if(action.equalsIgnoreCase("DELETE"))
		{
			String Id = request.getParameter("DOC_ID");
			JSONObject resultJson = new JSONObject();
			try {
				peppolDocIdsDAO.DeletePeppolDoc(plant, Integer.valueOf(Id));
					resultJson.put("STATUS", "OK"); 
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			response.getWriter().write(resultJson.toString());
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		PeppolDocIdsService peppolDocIdsService = new PeppolDocIdsServiceImpl();
		
		if(action.equalsIgnoreCase("DELETE"))
		{
			String Id= StrUtils.fString(req.getParameter("DOC_ID"));
			JSONObject resultJson = new JSONObject();
			PeppolDocIdsDAO peppolDocIdsDAO = new PeppolDocIdsDAO();
			try {
				peppolDocIdsDAO.DeletePeppolDoc(plant, Integer.valueOf(Id));
					resultJson.put("STATUS", "OK"); 
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			resp.getWriter().write(resultJson.toString());
		}
		
		if(action.equalsIgnoreCase("GET_PPPOL_DOC"))
		{
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			JSONArray jsonArray = new JSONArray();
			
			try {
				List<PEPPOL_DOC_IDS> PeppolDocIdsList = peppolDocIdsService.getAllPeppolDoc();
				if (PeppolDocIdsList.size() > 0) {
				for(int i =0; i<PeppolDocIdsList.size(); i++) {
				Map arrCustLine = (Map)PeppolDocIdsList.get(i);
				JSONObject resultJsonInt = new JSONObject();
				
				resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
				resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
				resultJsonInt.put("DOC_ID", (String)arrCustLine.get("DOC_ID"));
				jsonArray.add(resultJsonInt);
				}
				resultJson.put("PEPPOLDOCIDSLIST", jsonArray);
				}else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("PEPPOLDOCIDSLIST", jsonArray);
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
				List<PEPPOL_DOC_IDS> PeppolDocIdsList = peppolDocIdsService.getAllPeppolDoc();
				for (PEPPOL_DOC_IDS PeppolDocIdsType : PeppolDocIdsList) {
					PEPPOL_DOC_IDS peppolDoc = peppolDocIdsService.getPeppolDocById(PeppolDocIdsType.getID());
					PEPPOL_DOC_IDS PeppolDocIds = new PEPPOL_DOC_IDS();
					
					PeppolDocIds.setPLANT(peppolDoc.getPLANT());
					PeppolDocIds.setDOC_ID(peppolDoc.getDOC_ID());
					PeppolDocIdsList.add(PeppolDocIds);
				}
				
				resultJson.put("PEPPOLDOCIDSLIST", PeppolDocIdsList);   
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
