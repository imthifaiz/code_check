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

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.LocMstTwoDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.db.util.LocTypeUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.service.LoctaionTypeTwoService;
import com.track.serviceImplementation.LoctaionTypeTwoServiceImpl;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/locationtypetwo/*")
public class LoctaionTypeTwoServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;
	private LOC_TYPE_MST2 loc_TYPE_MST2;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		LoctaionTypeTwoService loctaionTypeTwoService = new LoctaionTypeTwoServiceImpl(); 
		if(action.equalsIgnoreCase("new") || action.equalsIgnoreCase("edit")) {
			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			TblControlDAO _TblControlDAO =new TblControlDAO();
			String lOC_TYPE_ID2 = "", lOC_TYPE_DESC2 = "", isActive = "";
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				boolean isOrderExists = false;
				String taxby = new PlantMstDAO().getTaxBy(plant);
				
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					LOC_TYPE_MST2 loc_TYPE_MST2 = new LOC_TYPE_MST2();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
						
							if (fileItem.getFieldName().equalsIgnoreCase("LOC_TYPE_ID2")) {
								lOC_TYPE_ID2 = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("LOC_TYPE_DESC2")) {
								lOC_TYPE_DESC2 = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("IsActive")) {
								isActive = StrUtils.fString(fileItem.getString()).trim();
							}
				}
					}
					loc_TYPE_MST2.setPLANT(plant);
					loc_TYPE_MST2.setLOC_TYPE_ID2(lOC_TYPE_ID2);
					loc_TYPE_MST2.setLOC_TYPE_DESC2(lOC_TYPE_DESC2);
					
					loc_TYPE_MST2.setCRAT(new DateUtils().getDateTime());
					loc_TYPE_MST2.setCRBY(username);
				
				ut = DbBean.getUserTranaction();
//				LoctaionTypeTwoService loctaionTypeTwoService = new LoctaionTypeTwoServiceImpl(); 
				ut.begin();
				if(action.equalsIgnoreCase("new")) {
					loc_TYPE_MST2.setIsActive("Y");
					Hashtable loc_TYP_MST2 = new Hashtable();
					loc_TYP_MST2.put("LOC_TYPE_ID2", loc_TYPE_MST2.getLOC_TYPE_ID2());
					loc_TYP_MST2.put("PLANT", loc_TYPE_MST2.getPLANT());
					
					isOrderExists  = new LocMstTwoDAO().isExisit(loc_TYP_MST2);
					
//					if(isOrderExists) {
//						 Hashtable  ht=new Hashtable();
//					      
//					       String query=" isnull(NXTSEQ,'') as NXTSEQ";
//					       ht.put(IDBConstants.PLANT,plant);
//					       ht.put(IDBConstants.TBL_FUNCTION,"LOCTYPETWO");
//						  String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
//						  //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
//				          
//			            Map m= _TblControlDAO.selectRow(query, ht,"");
//			           sBatchSeq=(String)m.get("NXTSEQ");
//			                     
//			            int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
//			           
//			            String updatedSeq=Integer.toString(inxtSeq);
//			             if(updatedSeq.length()==1)
//			            {
//			              sZero="00";
//			            }
//			            else if(updatedSeq.length()==2)
//			            {
//			              sZero="0";
//			            }
//			           
//			          
//			            Map htUpdate = null;
//			           
//			            Hashtable htTblCntUpdate = new Hashtable();
//			            htTblCntUpdate.put(IDBConstants.PLANT,plant);
//			            htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"LOCTYPETWO");
//			            htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"LTW");
//			            StringBuffer updateQyery=new StringBuffer("set ");
//			            updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
//			          
//			          
//			       //  boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
//			         
//			         
//			            lOC_TYPE_ID2="LTW"+sZero+updatedSeq;
////						lOC_TYPE_ID2 = loc_TYPE_MST2.getLOC_TYPE_ID2();
//				
//						loc_TYPE_MST2.setLOC_TYPE_ID2(lOC_TYPE_ID2);       
					//}
					
					
					if(!isOrderExists) {
					boolean insertFlag = loctaionTypeTwoService.addLOC_TYPE_MST2(loc_TYPE_MST2);
					if(insertFlag) {
						  MovHisDAO mdao = new MovHisDAO(plant);
						 Hashtable htm = new Hashtable();
				           htm.put("PLANT",plant);
				           htm.put("DIRTYPE",TransactionConstants.ADD_LOCTYPETWO);
				           htm.put("RECID","");
				           htm.put("ITEM",lOC_TYPE_ID2);
				           htm.put("REMARKS",lOC_TYPE_DESC2);
				           htm.put("UPBY",username);   htm.put("CRBY",username);
				            htm.put("CRAT",DateUtils.getDateTime());
				            htm.put("UPAT",DateUtils.getDateTime());
				            htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));      
				                  
				            htm.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
				            htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				            boolean updateFlag;
				      		if(lOC_TYPE_ID2!="LTW001")
				        		  {	
				      			boolean exitFlag = false;
				      			Hashtable htv = new Hashtable();				
				      			htv.put(IDBConstants.PLANT, plant);
				      			htv.put(IDBConstants.TBL_FUNCTION, "LOCTYPETWO");
				      			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				      			if (exitFlag) 
				        		    updateFlag=_TblControlDAO.updateSeqNo("LOCTYPETWO",plant);
				      			else
				      			{
				      				Map htInsert=null;
				                  	Hashtable htTblCntInsert  = new Hashtable();           
				                  	htTblCntInsert.put(IDBConstants.PLANT,plant);          
				                  	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCTYPETWO");
				                  	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"LTW");
				                   	htTblCntInsert.put("MINSEQ","0000");
				                   	htTblCntInsert.put("MAXSEQ","9999");
				                  	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
				                  	htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				                  	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
				                  	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
				      			}
				      		}
				                  boolean  inserted = mdao.insertIntoMovHis(htm);
				          		if(insertFlag) {
									DbBean.CommitTran(ut);
									resultJson.put("MESSAGE", "Location Type Two Created Successfully.");
									resultJson.put("ERROR_CODE", "100");	
									
									
									
								}else {
									DbBean.RollbackTran(ut);
									resultJson.put("MESSAGE",  "Failed to Create Location Type Two.");
							        resultJson.put("ERROR_CODE", "98");
								}
									}
							} else {DbBean.RollbackTran(ut);
							resultJson.put("MESSAGE",  "Location Type Two Exists already. Try again.");
					        resultJson.put("ERROR_CODE", "98");
						}
				
				}else if(action.equalsIgnoreCase("edit")) {
					loc_TYPE_MST2.setIsActive(isActive);
					loc_TYPE_MST2.setUPAT(new DateUtils().getDateTime());
					loc_TYPE_MST2.setUPBY(username);					
					boolean updateFlag = loctaionTypeTwoService.updateLOC_TYPE_MST2(loc_TYPE_MST2);
				
					if(updateFlag) {		
					MovHisDAO mdao = new MovHisDAO(plant);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE",TransactionConstants.UPDATE_LOCTYPETWO);
					htm.put("RECID", "");
					htm.put("ITEM",lOC_TYPE_ID2);
					htm.put("UPBY", username);
					htm.put("CRBY", username);
					htm.put("CRAT", DateUtils.getDateTime());
					htm.put("REMARKS", lOC_TYPE_DESC2);
					htm.put("UPAT", DateUtils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, DateUtils
							.getDateinyyyy_mm_dd(DateUtils.getDate()));

				
					boolean inserted = mdao.insertIntoMovHis(htm);}
					if(updateFlag) {
						DbBean.CommitTran(ut);
						resultJson.put("MESSAGE", "Location Type Two Updated Successfully.");
						resultJson.put("ERROR_CODE", "100");
					}else {
						DbBean.RollbackTran(ut);
					}
				}
				}
				}catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
			
		}
		if(action.equalsIgnoreCase("Delete"))
		{
			String lvtid= StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
			JSONObject resultJson = new JSONObject();

			try {
				
				
				boolean updateFlag = loctaionTypeTwoService.deleteLOC_TYPE_MST2(plant, lvtid);
				if(updateFlag) {		
					MovHisDAO mdao = new MovHisDAO(plant);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE",TransactionConstants.DEL_LOCTYPETWO);
					htm.put("RECID", "");
					htm.put("ITEM",lvtid);
					htm.put("UPBY", username);
					htm.put("CRBY", username);
					htm.put("CRAT", DateUtils.getDateTime());
					htm.put("REMARKS", "");
					htm.put("UPAT", DateUtils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, DateUtils
							.getDateinyyyy_mm_dd(DateUtils.getDate()));

				
					boolean inserted = mdao.insertIntoMovHis(htm);}
				
					resultJson.put("STATUS", "OK"); 
				
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			response.getWriter().write(resultJson.toString());
			
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
		LoctaionTypeTwoService loctaionTypeTwoService = new LoctaionTypeTwoServiceImpl(); 
		
		if(action.equalsIgnoreCase("new")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/createLocationTypeTwo.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("summary")) {
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if(ajax) {
			String LOCID = StrUtils.fString(request.getParameter("LOCATIONTYPE"));
			
		
			Hashtable ht = new Hashtable();
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();		
			try {
				List<LOC_TYPE_MST2> doHeaders =  loctaionTypeTwoService.getLOC_TYPE_MST2(plant, LOCID);
				if(doHeaders.size() > 0) {
					for(LOC_TYPE_MST2 lOC_TYPE_MST2 : doHeaders) {
						JSONObject json = new JSONObject();
				
					
						json.put("LOC_TYPE_ID2", lOC_TYPE_MST2.getLOC_TYPE_ID2());
						json.put("LOC_TYPE_DESC2", lOC_TYPE_MST2.getLOC_TYPE_DESC2());
						json.put("IsActive", lOC_TYPE_MST2.getIsActive());
						jsonArray.add(json);
					}
				}					
				resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}				
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else {
			String msg = StrUtils.fString(request.getParameter("msg"));
			
			request.setAttribute("Msg", msg);
			
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/locationTypeTwoSummary.jsp");
			rd.forward(request, response);
		}		
	}
		if(action.equalsIgnoreCase("detail")) {
			try {RequestDispatcher rd = request.getRequestDispatcher("/jsp/detailLocationTypeTwo.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//		if(action.equalsIgnoreCase("loctype")) {
//			try {RequestDispatcher rd = request.getRequestDispatcher("/jsp/LocTypeListTwo.jsp");
//			rd.forward(request, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

		if(action.equalsIgnoreCase("edit")) {
			String msg= "";
			try {RequestDispatcher rd = request.getRequestDispatcher("/jsp/editLocationTypeTwo.jsp");
			request.setAttribute("Msg", msg);
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		else if(action.equalsIgnoreCase("Auto-Generate")) {
		String locationTypeID = "" ;
		TblControlDAO _TblControlDAO = new TblControlDAO();
		JSONObject json = new JSONObject();
		try {

			  
			   String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
			  
			   
			       Hashtable  ht=new Hashtable();
			      
			       String query=" isnull(NXTSEQ,'') as NXTSEQ";
			       ht.put(IDBConstants.PLANT,plant);
			       ht.put(IDBConstants.TBL_FUNCTION,"LOCTYPETWO");
			        
			        boolean exitFlag=false; boolean resultflag=false;
			       exitFlag=_TblControlDAO.isExisit(ht,"",plant);
			      
			      //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
			      if (exitFlag==false)
			       { 
			                     
			             Map htInsert=null;
			             Hashtable htTblCntInsert  = new Hashtable();
			            
			             htTblCntInsert.put(IDBConstants.PLANT,plant);
			           
			             htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCTYPETWO");
			             htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"LTW");
			              htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"000");
			              htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"999");
			             htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
			             htTblCntInsert.put(IDBConstants.CREATED_BY, username);
			             htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
			             insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
			             
			             locationTypeID="LTW"+"001";
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
			              sZero="00";
			            }
			            else if(updatedSeq.length()==2)
			            {
			              sZero="0";
			            }
			           
			          
			            Map htUpdate = null;
			           
			            Hashtable htTblCntUpdate = new Hashtable();
			            htTblCntUpdate.put(IDBConstants.PLANT,plant);
			            htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"LOCTYPETWO");
			            htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"LTW");
			            StringBuffer updateQyery=new StringBuffer("set ");
			            updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
			          
			          
			       //  boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
			         
			         
			         locationTypeID="LTW"+sZero+updatedSeq;
			            }
			          
			json.put("LOCTYPETWO", locationTypeID);
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
	
		else if(action.equalsIgnoreCase("Auto-Gen")) {
			String locationTypeID = "" ;
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {

				  
				   String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
				  
				   
				       Hashtable  ht=new Hashtable();
				       String query=" isnull(NXTSEQ,'') as NXTSEQ";
				       ht.put(IDBConstants.PLANT,plant);
				       ht.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
				        
				        boolean exitFlag=false; boolean resultflag=false;
				       exitFlag=_TblControlDAO.isExisit(ht,"",plant);
				      
				      //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
				       if (exitFlag==false)
				       { 
				                     
				             Map htInsert=null;
				             Hashtable htTblCntInsert  = new Hashtable();
				            
				             htTblCntInsert.put(IDBConstants.PLANT,plant);
				           
				             htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
				             htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"LT");
				              htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"000");
				              htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"999");
				             htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
				             htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				             htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
				             insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
				             
				             locationTypeID="LT"+"001";
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
				              sZero="00";
				            }
				            else if(updatedSeq.length()==2)
				            {
				              sZero="0";
				            }
				           
				          
				             Map htUpdate = null;
				             
				             Hashtable htTblCntUpdate = new Hashtable();
				             htTblCntUpdate.put(IDBConstants.PLANT,plant);
				             htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
				             htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"LT");
				             StringBuffer updateQyery=new StringBuffer("set ");
				             updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
				           
				          
				       //  boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
				         
				         
				             locationTypeID="LT"+sZero+updatedSeq;
				            }
				          
				json.put("LOCTYPE", locationTypeID);
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

		