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

import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ProjectAttachDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.FinProjectDAO;
import com.track.db.object.FinProject;
import com.track.db.util.CustUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.TblControlUtil;

import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;

import com.track.service.ProjectService;
import com.track.serviceImplementation.ProjectServiceImpl;

import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/project/*")
public class ProjectServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private MLogger mLogger;
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		
		if(action.equalsIgnoreCase("new") || action.equalsIgnoreCase("edit")) {
			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			int Id;
			String id="", custno = "", project = "", ProjectName = "",ProjectDate = "", ExpiryDate = "",
				ProjectStatus = "", Reference = "", EstimateCost = "", EstimateTime = "", BillingOption = "",
				Isperhourwages = "",Perhourwagescost = "", Note = "", Crat = "", Crby = "", Upat = "",Upby = "";
			double dayhour = 0;
			Short isdayhour = 0;
			List<Hashtable<String,String>> projectAttachmentList = null;
			List<Hashtable<String,String>> projectAttachmentInfoList = null;
			
			projectAttachmentList = new ArrayList<Hashtable<String,String>>();
			projectAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
			ProjectAttachDAO projectAttachDAO = new ProjectAttachDAO();
			ProjectService projectService = new ProjectServiceImpl();
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				boolean isOrderExists = false;
				String taxby = new PlantMstDAO().getTaxBy(plant);
			
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					FinProject finProject = new FinProject();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
								custno = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PRNO")) {
								project = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PR_NAME")) {
								ProjectName = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DAYHOUR")) {
								dayhour = Double.parseDouble(fileItem.getString());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("MANDAYHOUR")) {
								isdayhour = Short.valueOf(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								ProjectDate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIRYDATE")) {
								ExpiryDate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PRESTCOST")) {
								EstimateCost = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("Notes")) {
								Note = StrUtils.fString(fileItem.getString()).trim();
							}
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/PROJECT" + "/"+ project;
								String filetempLocationATT = "C:/ATTACHMENTS/PROJECT" + "/temp" + "/"+ project;
								String fileName = StrUtils.fString(fileItem.getName()).trim();
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
								fileItem.write(uploadedFile);
								// delete temp uploaded file
								File tempPath = new File(filetempLocationATT);
								if (tempPath.exists()) {
									File tempUploadedfile = new File(tempPath + "/"+ fileName);
									if (tempUploadedfile.exists()) {
										tempUploadedfile.delete();
									}
								}
								Hashtable projectAttachment = new Hashtable<String, String>();
								projectAttachment.put("PLANT", plant);
								projectAttachment.put("FILETYPE", fileItem.getContentType());
								projectAttachment.put("FILENAME", fileName);
								projectAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								projectAttachment.put("FILEPATH", fileLocationATT);
								projectAttachment.put("CRAT",new DateUtils().getDateTime());
								projectAttachment.put("CRBY",username);
								projectAttachment.put("UPAT",new DateUtils().getDateTime());
								projectAttachmentList.add(projectAttachment);
							}
						}
					}
					finProject.setPLANT(plant);
					finProject.setCUSTNO(custno);
					finProject.setPROJECT(project);
					finProject.setPROJECT_NAME(ProjectName);
					finProject.setMANDAY_HOUR(dayhour);
					finProject.setISMANDAY_HOUR(isdayhour);
					finProject.setPROJECT_DATE(ProjectDate);
					finProject.setEXPIRY_DATE(ExpiryDate);
					finProject.setPROJECT_STATUS("OPEN");
					finProject.setREFERENCE(Reference);
					finProject.setESTIMATE_COST(EstimateCost);
					finProject.setESTIMATE_TIME(EstimateTime);
					finProject.setBILLING_OPTION(BillingOption);
					finProject.setNOTE(Note);
					finProject.setCRAT(new DateUtils().getDateTime());
					finProject.setCRBY(username);
					finProject.setUPAT(Upat);
					finProject.setUPBY(Upby);
					
					ut = DbBean.getUserTranaction();
					ut.begin();
					
				if(action.equalsIgnoreCase("new")) {
					Hashtable doht = new Hashtable();
					doht.put("project", finProject.getPROJECT());
					doht.put("PLANT", finProject.getPLANT());
					isOrderExists  = new FinProjectDAO().isExisit(doht);					
					if(!isOrderExists) {
				boolean insertFlag = projectService.addFinProject(finProject);
					if(insertFlag) {
						int attchSize = projectAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable projectAttachmentat = new Hashtable<String, String>();
								projectAttachmentat = projectAttachmentList.get(i);
								projectAttachmentat.put("PROJECTID", project);
								projectAttachmentInfoList.add(projectAttachmentat);
							}
							insertFlag = projectAttachDAO.addprojectAttachments(projectAttachmentInfoList, plant);
						}
					}
					if(insertFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_PROJECT);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custno);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, ProjectName);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, project);
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");	
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						insertFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
					}
					
					if(insertFlag) {
						new TblControlUtil().updateTblControlIESeqNo(plant, IConstants.PROJECT, "PR", project);
						DbBean.CommitTran(ut);
						resultJson.put("MESSAGE", "Project Created Successfully.");
						resultJson.put("ERROR_CODE", "100");	
					}else {
						DbBean.RollbackTran(ut);
						resultJson.put("MESSAGE",  "Failed to Project.");
				        resultJson.put("ERROR_CODE", "98");
					}
					}else {DbBean.RollbackTran(ut);
					resultJson.put("MESSAGE",  "Project Exists already. Try again.");
			        resultJson.put("ERROR_CODE", "98");
				}
				}else if(action.equalsIgnoreCase("edit")) {
					finProject.setUPAT(new DateUtils().getDateTime());
					finProject.setUPBY(username);
					boolean updateFlag = projectService.updateFinProject(finProject);
					if(updateFlag) {
						int attchSize = projectAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable projectAttachmentat = new Hashtable<String, String>();
								projectAttachmentat = projectAttachmentList.get(i);
								projectAttachmentat.put("PROJECTID", project);
								projectAttachmentInfoList.add(projectAttachmentat);
							}
							updateFlag = projectAttachDAO.addprojectAttachments(projectAttachmentInfoList, plant);
						}
					}
					if(updateFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_PROJECT);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custno);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, ProjectName);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, project);
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						updateFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
					}
					if(updateFlag) {
						DbBean.CommitTran(ut);
						resultJson.put("MESSAGE", "Project Updated Successfully.");
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
		}else if(action.equalsIgnoreCase("removeAttachmentById")){
			System.out.println("Remove Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("removeid"));
			ProjectAttachDAO projectAttachDAO = new ProjectAttachDAO();
			try {
				Hashtable ht1 = new Hashtable();
				projectAttachDAO.deleteprojectAttachByPrimId(plant, Integer.toString(ID));
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");  
		}else if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID"); 
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			ProjectAttachDAO projectAttachDAO = new ProjectAttachDAO();
			List paymentAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				paymentAttachment = projectAttachDAO.getprojectAttachById(plant, String.valueOf(ID));
				Map billAttach=(Map)paymentAttachment.get(0);
				String filePath=(String) billAttach.get("FilePath");
				String fileType=(String) billAttach.get("FileType");
				String fileName=(String) billAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}	  
		}else if (action.equalsIgnoreCase("Delete")) {
			String project= StrUtils.fString(request.getParameter("PRNO"));
			JSONObject resultJson = new JSONObject();
			ProjectService projectService = new ProjectServiceImpl();
			ProjectAttachDAO projectAttachDAO = new ProjectAttachDAO();
			try {
				boolean updateFlag = projectService.deleteFinProject(plant, project);
				int num = projectAttachDAO.deleteprojectAttachByNo(plant, project);
				if(updateFlag) {		
					MovHisDAO mdao = new MovHisDAO(plant);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE",TransactionConstants.DELETE_PROJECT);
					htm.put(IDBConstants.MOVHIS_ORDNUM, project);
					htm.put("UPBY", username);
					htm.put("CRBY", username);
					htm.put("CRAT", DateUtils.getDateTime());
					htm.put("REMARKS", "");
					htm.put("UPAT", DateUtils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					boolean inserted = mdao.insertIntoMovHis(htm);}
					resultJson.put("MESSAGE", "Project Deleted Successfully.");
					resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", "Project Not Deleted");
				resultJson.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
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
		JSONObject jsonObjectResult = new JSONObject();
		
		ProjectService projectService = new ProjectServiceImpl();
		FinProject finProject = new FinProject();
		if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION")) {			
			jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} 
	
		if(action.equalsIgnoreCase("new")) {
			try {
			ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
			ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
			ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
			ArrayList bankList =  new MasterUtil().getBankList("",plant);
			
			String deldate = new DateUtils().getDate();
			String collectionTime = new DateUtils().getTimeHHmm();
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			String msg = StrUtils.fString(request.getParameter("msg"));
			String gst = new selectBean().getGST("SALES",plant);
			String taxbylabel = new userBean().getTaxByLable(plant);
			
			request.setAttribute("TaxTreatmentList", taxTreatmentList);
			request.setAttribute("SalesLocations", slList);
			request.setAttribute("CountryList", ccList);
			request.setAttribute("BankList", bankList);
			
			request.setAttribute("DelDate", deldate);
			request.setAttribute("CollectionTime", collectionTime);
			request.setAttribute("NumberOfDecimal", numberOfDecimal);
			request.setAttribute("Currency", currency);
			request.setAttribute("GST", gst);
			request.setAttribute("Msg", msg);
			request.setAttribute("Taxbylabel", taxbylabel);
			request.setAttribute("Region", region);
			
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/createProject.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}if(action.equalsIgnoreCase("Auto-Generate")) {
			String prno = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {
				prno = _TblControlDAO.getNextOrder(plant,username,IConstants.PROJECT);
				json.put("PRNO", prno);
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
		}if(action.equalsIgnoreCase("summary")) {
			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if(ajax) {
				String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String cname = StrUtils.fString(request.getParameter("CNAME"));
				String projectno = StrUtils.fString(request.getParameter("PROJECTNO"));
				String reference = StrUtils.fString(request.getParameter("PROJECTNAME"));
				String status = StrUtils.fString(request.getParameter("STATUS"));
				String viewstatus = StrUtils.fString(request.getParameter("VIEWSTATUS"));
				String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				String fdate="",tdate=""; 
				ProjectService projectservice = new ProjectServiceImpl();
				CustMstDAO cusDAO=new CustMstDAO();
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();		
				try {
					if(StrUtils.fString(cname).length() > 0)	ht.put("CUSTNO", cname);
					if(StrUtils.fString(projectno).length() > 0)	ht.put("PROJECT", projectno);
					if(StrUtils.fString(reference).length() > 0)	ht.put("PROJECT_NAME", reference);
					if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(PROJECT_STATUS, 'Open')", status);
					if(StrUtils.fString(plant).length() > 0)	ht.put("PLANT", plant);
					
					if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			           String curDate =DateUtils.getDate();
						if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
							FROM_DATE=curDate;

			           if (FROM_DATE.length()>5)
			            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

			           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
			           if (TO_DATE.length()>5)
			           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

		            String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
					List<FinProject> doHeaders =  projectservice.getProject(ht, fdate, tdate,viewstatus);
				String project = StrUtils.fString(request.getParameter("PRNO"));
					
			
				
					if(doHeaders.size() > 0) {
						for(FinProject Project : doHeaders) {
							
							String unitCostValue = (String)Project.getESTIMATE_COST();
							float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
                            /* Author: Azees  Create date: July 4,2021  Description: Show Customer Name */
							String CName=(String) Project.getCUSTNO();
                            JSONObject cusJson=cusDAO.getCustomerName(plant, (String) Project.getCUSTNO());
                            if(!cusJson.isEmpty())
                            	CName=(String) cusJson.getString("CNAME");
                            
							JSONObject json = new JSONObject();
							json.put("CUST_NAME", CName);
							json.put("PRNO", Project.getPROJECT());
							json.put("PR_NAME", Project.getPROJECT_NAME());
							json.put("DELDATE", Project.getPROJECT_DATE());
							json.put("EXPIRYDATE", Project.getEXPIRY_DATE());
							json.put("ESTIMATE_COST", curency+unitCostValue);
							json.put("MANDAY_HOUR", Project.getMANDAY_HOUR());/* Author: Azees  Create date: July 4,2021  Description: Number of Man-day/Hour */
							json.put("STATUS", Project.getPROJECT_STATUS());							
							jsonArray.add(json);
						}
					}					
					resultJson.put("PROJECT", jsonArray);
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
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProjectSummary.jsp");
				rd.forward(request, response);
			}		
		}
		if(action.equalsIgnoreCase("edit")) {
			String project = StrUtils.fString(request.getParameter("PRNO"));
			String imagePath = "", numberOfDecimal = "";
		
			Map plntMap = new HashMap();
			Map doHdrDetails = new HashMap();
			CustUtil custUtil = new CustUtil();
			String msg= "";
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				finProject = projectService.getprojectByIdname(plant, project);
				
				String sCustName ="";
				ArrayList arrCust = custUtil.getCustomerDetails(finProject.getCUSTNO(), plant);
				sCustName = (String) arrCust.get(1);
				
				List attachmentList = new ProjectAttachDAO().getprojectAttachByPRNO(plant, project);
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editProject.jsp");
				request.setAttribute("FinProject", finProject);
				request.setAttribute("CustName", sCustName);
				request.setAttribute("Msg", msg);
				request.setAttribute("AttachmentList", attachmentList);
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}
		if(action.equalsIgnoreCase("detail")) {

			String project = StrUtils.fString(request.getParameter("PRNO"));
			String imagePath = "", numberOfDecimal = "";
		
			Map plntMap = new HashMap();
			Map doHdrDetails = new HashMap();
			CustUtil custUtil = new CustUtil();
			String msg= "";
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				finProject = projectService.getprojectByIdname(plant, project);
				
				String sCustName ="";
				ArrayList arrCust = custUtil.getCustomerDetails(finProject.getCUSTNO(), plant);
				sCustName = (String) arrCust.get(1);
								
				List attachmentList = new ProjectAttachDAO().getprojectAttachByPRNO(plant, project);
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProjectDetail.jsp");
				request.setAttribute("FinProject", finProject);
				request.setAttribute("CustName", sCustName);
				request.setAttribute("Msg", msg);
				request.setAttribute("AttachmentList", attachmentList);
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}

		}
		
	}
	
	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        FinProjectDAO finProject = new FinProjectDAO();
        StrUtils strUtils = new StrUtils();
//        finProject.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String prno = StrUtils.fString(request.getParameter("PRNO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    String projectname = StrUtils.fString(request.getParameter("PROJECT_NAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(prno.length()>0) extCond=" AND plant='"+plant+"' and PROJECT like '"+prno+"%' ";
		     if(projectname.length()>0) extCond=" AND PROJECT_NAME like '"+projectname+"%' ";
		     if(cname.length()>0) extCond=" AND CUSTNO = '"+cname+"' ";
		     //extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" ORDER BY CONVERT(date, PROJECT_DATE, 103) desc";
		     ArrayList listQry = finProject.selectFinProject("PROJECT,PROJECT_NAME,CUSTNO,PROJECT_STATUS,PROJECT_DATE",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  prno = (String)m.get("PROJECT");
				  String custno  = strUtils.replaceCharacters2Send((String)m.get("CUSTNO"));
				
				  String ProjectDate = (String)m.get("PROJECT_DATE");
				  String ProjectStatus  = (String)m.get("PROJECT_STATUS");
				  String ProjectName   = (String)m.get("PROJECT_NAME");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PRNO", prno);
				  resultJsonInt.put("CUSTNO", custno);
				  resultJsonInt.put("PROJECT_NAME", ProjectName);
				  resultJsonInt.put("DELDATE", ProjectDate);
				  resultJsonInt.put("project_STATUS", ProjectStatus);				  
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
                 resultJsonInt.put("ERROR_MESSAGE", "NO PROJECT RECORD FOUND!");
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
