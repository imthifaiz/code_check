package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoHdrDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.HrShiftMst;
import com.track.gates.DbBean;
import com.track.service.HrShiftService;
import com.track.serviceImplementation.HrShiftMstServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@WebServlet("/shift/*")
public class ShiftServlet extends HttpServlet implements IMLogger  {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ShiftServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ShiftServlet_PRINTPLANTMASTERINFO;
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		HrShiftService hrShiftService = new HrShiftMstServiceImpl();
		
		if(action.equalsIgnoreCase("summary")) {
			
			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if(ajax) {
				
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArrayErr = new JSONArray();		
				try {
					List<HrShiftMst> HrShiftMstList = hrShiftService.getAllShiftMst(plant);
					resultJson.put("SHIFTLIST", HrShiftMstList);   
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resultJson.put("SEARCH_DATA", "");
		            JSONObject resultJsonInt = new JSONObject();
		            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
		            resultJsonInt.put("ERROR_CODE", "98");
		            jsonArrayErr.add(resultJsonInt);
		            resultJson.put("ERROR", jsonArrayErr);
				}
				response.getWriter().write(resultJson.toString());
				
			}else {
				String msg = StrUtils.fString(request.getParameter("msg"));
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ShiftSummary.jsp");
				request.setAttribute("Msg", msg);
				rd.forward(request, response);
			}
			
		}
		
		if(action.equalsIgnoreCase("new")) {
			try {
				
				String msg = StrUtils.fString(request.getParameter("msg"));
				
				request.setAttribute("Msg", msg);
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateShift.jsp");
				request.setAttribute("Msg", msg);
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("edit")) {
			
			String lvtid = StrUtils.fString(request.getParameter("ID"));
			HrShiftMst hrShiftMst = new HrShiftMst();
			String msg= "";
			
			try {
			hrShiftMst = hrShiftService.getShiftMstById(plant, Integer.valueOf(lvtid));
			
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditShift.jsp");
			request.setAttribute("HrShiftMst", hrShiftMst);
			request.setAttribute("Msg", msg);
			rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}
		
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		HrShiftService hrShiftService = new HrShiftMstServiceImpl();
		
		if(action.equalsIgnoreCase("new") || action.equalsIgnoreCase("edit")) {
			
				
				UserTransaction ut = null;
				JSONObject resultJson = new JSONObject();
				String SHIFTID="0",SHIFTNAME = "", ISHOURBASED = "", ISTIMEBASED = "", ALLOCATEHOUR = "", INTIME = "", OUTTIME = "",IsActive="";
						try{
							boolean isMultipart = ServletFileUpload.isMultipartContent(request);
							boolean isExists = false;
							if(isMultipart) {
								FileItemFactory factory = new DiskFileItemFactory();
								ServletFileUpload upload = new ServletFileUpload(factory);				
								List items = upload.parseRequest(request);
								Iterator iterator = items.iterator();
								HrShiftMst hrShiftMst = new HrShiftMst();
								MovHisDAO movHisDao = new MovHisDAO();
								
								while (iterator.hasNext()) {
									FileItem fileItem = (FileItem) iterator.next();
									if (fileItem.isFormField()) {
										
										if (fileItem.getFieldName().equalsIgnoreCase("SHIFTID")) {
											SHIFTID = StrUtils.fString(fileItem.getString()).trim();
										}
										
										if (fileItem.getFieldName().equalsIgnoreCase("SHIFTNAME")) {
											SHIFTNAME = StrUtils.fString(fileItem.getString()).trim();
										}
										
										if (fileItem.getFieldName().equalsIgnoreCase("ISHOURBASED")) {
											ISHOURBASED = StrUtils.fString(fileItem.getString()).trim();
										}
										
										if (fileItem.getFieldName().equalsIgnoreCase("ISTIMEBASED")) {
											ISTIMEBASED = StrUtils.fString(fileItem.getString()).trim();
										}
										
										if (fileItem.getFieldName().equalsIgnoreCase("ALLOCATEHOUR")) {
											ALLOCATEHOUR = StrUtils.fString(fileItem.getString()).trim();
										}
										
										if (fileItem.getFieldName().equalsIgnoreCase("INTIME")) {
											INTIME = StrUtils.fString(fileItem.getString()).trim();
										}
										
										if (fileItem.getFieldName().equalsIgnoreCase("OUTTIME")) {
											OUTTIME = StrUtils.fString(fileItem.getString()).trim();
										}
										
										if (fileItem.getFieldName().equalsIgnoreCase("IsActive")) {
											IsActive = StrUtils.fString(fileItem.getString()).trim();
										}
									}
								}
								
								if(ISHOURBASED.equalsIgnoreCase(""))
									ISHOURBASED="0";
								
								if(ISTIMEBASED.equalsIgnoreCase(""))
									ISTIMEBASED="0";
								
								/*if(!ALLOCATEHOUR.equalsIgnoreCase(""))
								{
									if(ALLOCATEHOUR.length()>6)
									{}
									else
									ALLOCATEHOUR=ALLOCATEHOUR+":00";
								}
								
								if(!INTIME.equalsIgnoreCase(""))
								{
									if(INTIME.length()>6)
									{}
									else
									INTIME=INTIME+":00";
								}
								
								if(!OUTTIME.equalsIgnoreCase(""))
								{
									if(OUTTIME.length()>6)
									{}
									else
										OUTTIME=OUTTIME+":00";
								}*/
								
								hrShiftMst.setPLANT(plant);
								hrShiftMst.setSHIFTNAME(SHIFTNAME);
								hrShiftMst.setISHOURBASED(Short.parseShort(ISHOURBASED));
								hrShiftMst.setISTIMEBASED(Short.parseShort(ISTIMEBASED));
								hrShiftMst.setALLOCATEHOUR(ALLOCATEHOUR);
								hrShiftMst.setINTIME(INTIME);
								hrShiftMst.setOUTTIME(OUTTIME);
								hrShiftMst.setIsActive("Y");
								hrShiftMst.setCRAT(new DateUtils().getDateTime());
								hrShiftMst.setCRBY(username);
								
								ut = DbBean.getUserTranaction();
								hrShiftService = new HrShiftMstServiceImpl();
								ut.begin();
								if(action.equalsIgnoreCase("new")) {
									
									isExists  = hrShiftService.IsShiftMst(plant,SHIFTNAME);
										
								if(!isExists) {
								int ShiftId = hrShiftService.addShiftMst(hrShiftMst);
								if(ShiftId > 0) {
									
									Hashtable htMovHis = new Hashtable();
				 					htMovHis.clear();
				 					htMovHis.put(IDBConstants.PLANT, plant);					
				 					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_SHIFT);	
				 					htMovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));														
				 					htMovHis.put("RECID", "");
				 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(ShiftId));
				 					htMovHis.put(IDBConstants.CREATED_BY, username);		
				 					htMovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				 					htMovHis.put("REMARKS",SHIFTNAME);
				 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
									
				 					DbBean.CommitTran(ut);
									resultJson.put("MESSAGE", "Shift details created successfully");
									resultJson.put("ERROR_CODE", "100");
								} else {
									DbBean.RollbackTran(ut);
									resultJson.put("MESSAGE",  "Failed to create shift.");
							        resultJson.put("ERROR_CODE", "98");
								}
								}
								else {
									DbBean.RollbackTran(ut);
									resultJson.put("MESSAGE", "Shift name already exists");
									resultJson.put("ERROR_CODE", "98");									
								}
								}
								else if(action.equalsIgnoreCase("edit")){
									
									hrShiftMst.setUPAT(new DateUtils().getDateTime());
									hrShiftMst.setUPBY(username);
									hrShiftMst.setID(Integer.parseInt(SHIFTID));
									
									hrShiftService.updateShiftMst(hrShiftMst,username);
									
										
										Hashtable htMovHis = new Hashtable();
					 					htMovHis.clear();
					 					htMovHis.put(IDBConstants.PLANT, plant);					
					 					htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_SHIFT);	
					 					htMovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));														
					 					htMovHis.put("RECID", "");
					 					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, SHIFTID);
					 					htMovHis.put(IDBConstants.CREATED_BY, username);		
					 					htMovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					 					htMovHis.put("REMARKS",SHIFTNAME);
					 					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					 					
					 					
											DbBean.CommitTran(ut);
											resultJson.put("MESSAGE", "Shift details updated successfully");
											resultJson.put("ERROR_CODE", "100");
										
									
									
								}
				
			
		}
						} catch (Exception e) {
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
			String lvtid= StrUtils.fString(request.getParameter("id"));
			JSONObject resultJson = new JSONObject();
			try {
				
					hrShiftService.DeleteShiftMst(plant, Integer.valueOf(lvtid));
					resultJson.put("STATUS", "OK"); 
				
			} catch (Exception e) {
				resultJson.put("STATUS", "NOT OK");  
			}
			response.getWriter().write(resultJson.toString());
			
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
