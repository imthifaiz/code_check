package com.track.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.track.db.object.ClearingAgentTypeDET;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.HrEmpType;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.db.object.LOC_TYPE_MST3;
import com.track.db.util.ClearanceUtil;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.EmployeeUtil;
import com.track.db.util.GstTypeUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.LocTypeUtil;
import com.track.db.util.LocUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.*;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderTypeUtil;
import com.track.db.util.OutletUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PayTermsUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.PrdBrandUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdDeptUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.RsnMstUtil;
import com.track.db.util.ShipperUtil;
import com.track.db.util.TransportModeUtil;
import com.track.db.util.UomUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.service.LoctaionTypeThreeService;
import com.track.service.LoctaionTypeTwoService;
import com.track.serviceImplementation.LoctaionTypeThreeServiceImpl;
import com.track.serviceImplementation.LoctaionTypeTwoServiceImpl;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;
import com.track.util.XMLUtils;

public class MasterServlet extends HttpServlet implements IMLogger{
	
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ProductionBomServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ProductionBomServlet_PRINTPLANTMASTERINFO;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private static final String CLEARANCETYPEID = null;
	String action = "";
	String xmlStr = "";
	String completemsg="";
	MasterUtil _MasterUtil=null;
	MovHisDAO mdao = null;
	DateUtils dateutils = new DateUtils();
	
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 _MasterUtil=new  MasterUtil();
		mdao =new MovHisDAO();
		
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String actionnew = StrUtils.fString(request.getParameter("ACTION")).trim();
		if(actionnew!="")
			action =actionnew;
		else
		action = request.getParameter("action").trim();
		
	    if (action.equals("downloadCompAttachmentById")) {
  		
  		System.out.println("Attachments by ID");
		int ID=Integer.parseInt(request.getParameter("attachid"));
		FileHandling fileHandling=new FileHandling(); 
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		List compAttachment = null;
		try {
			
			String aplant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			Hashtable ht1 = new Hashtable();
			ht1.put("ID", String.valueOf(ID));
			ht1.put("PLANT", aplant);
			compAttachment = _PlantMstDAO.getCompAttachByHrdId(aplant,String.valueOf(ID));
			Map compAttach=(Map)compAttachment.get(0);
			String filePath=(String) compAttach.get("FilePath");
			String fileType=(String) compAttach.get("FileType");
			String fileName=(String) compAttach.get("FileName");
			fileHandling.fileDownload(filePath, fileName, fileType, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
		PrintWriter out = response.getWriter();
		JSONObject jsonObjectResult = new JSONObject();
		
		try {
			
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			
		    if(action.equals("ADD_FOOTER")){
				String msg   = addfooter(request, response);
				jsonObjectResult = this.getFooterDetails(request);	 
				    jsonObjectResult.put("errorMsg", msg);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }else if(action.equals("VIEW_FOOTER_DETAILS")){
				    jsonObjectResult = this.getViewFooterDetails(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					 
			 }else  if (action.equals("CREATE_CURRENCY")) {
	        	  	jsonObjectResult = this.CreateCurrencyModal(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
			 }else  if (action.equals("GET_CURRENCY_NAME_DATA")) {
		            jsonObjectResult = this.getCurrencyNameData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
			 }
		    //imtiziaf start1 customertype
			 else if(action.equals("GET_CUSTOMERTYPE_FOR_SUMMARY")){
			    jsonObjectResult = this.getViewCustomerTypeDetails(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }//imtiziaf end customertype
		    
		    //vicky start clearancertype
			 else if(action.equals("GET_CLEARANCETYPE_FOR_SUMMARY")){
				    jsonObjectResult = this.getViewClearanceTypeDetails(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					 
				 }//vicky end clearancertype
		    
		    //NAVAS start GST
			    else if(action.equals("GET_GST_FOR_SUMMARY")){ 
				 jsonObjectResult = this.queryGstType(request);
				 response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				 response.getWriter().write(jsonObjectResult.toString());
				 response.getWriter().flush(); response.getWriter().close();
				 } //NAVAS end GST  end
		    
			/* Navas START reasonsummary */
		    else if(action.equals("GET_RSN_SUMMARY")){
			    jsonObjectResult = this.getReasonMstDetails(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			 }//Navas reasonsummary end
		    
		    
			  //Navas START order type
			    else if(action.equals("GET_ORDERTYPE_FOR_SUMMARY")){
				    jsonObjectResult = this.getOrderTypeListDetails(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }//Navas order type end
		    
		  //NAVAS start SUPPLIERDISCOUNT
			    else if(action.equals("GET_SUPPLIERDISCOUNT_FOR_SUMMARY")){ 
				 jsonObjectResult = this.getSupplierReport(request);
				 response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				 response.getWriter().write(jsonObjectResult.toString());
				 response.getWriter().flush(); response.getWriter().close();
				 } //NAVAS SUPPLIERDISCOUNT   end
		    
		    //NAVAS start CUSTOMERDISCOUNT
			    else if(action.equals("GET_CUSTOMERERDISCOUNT_FOR_SUMMARY")){ 
				 jsonObjectResult = this.getCustomerForReportPgn(request);
				 response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				 response.getWriter().write(jsonObjectResult.toString());
				 response.getWriter().flush(); response.getWriter().close();
				 } //NAVAS CUSTOMERDISCOUNT   end
		    
		  //Navas START CURRENCY type
			    else if(action.equals("GET_CURRENCYTYPE_SUMMARY")){
				    jsonObjectResult = this.getCurrencyDetails(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }//Navas CURRENCY type end
		    
		  //Navas START location type
		    else if(action.equals("GET_LOCATIONTYPE_SUMMARY")){
			    jsonObjectResult = this.getLocTypeList(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			 }//Navas LOCATION type end
		    
			  //Navas START location typetwo
			    else if(action.equals("GET_LOCATIONTYPETWO_SUMMARY")){
				    jsonObjectResult = this.getLocTypeListTwo(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }//Navas LOCATION typetwo end
		 
		 // RESVI STARTSPRDBRANDSUMMARY

		    else if(action.equals("GET_PRODUCTBRANDID_FOR_SUMMARY")){
		    jsonObjectResult =this.getPrdBrandList(request);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(jsonObjectResult.toString());
		    response.getWriter().flush();
		    response.getWriter().close();


		    }
		    
		    else if(action.equals("GET_TRANSPORTMODEID_FOR_SUMMARY")){
			    jsonObjectResult =this.getTranModeList(request);
			    response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(jsonObjectResult.toString());
			    response.getWriter().flush();
			    response.getWriter().close();


			    }


		    // ENDS
		    
//		    RESVI STARTS ALTERNATE SUMMARY
			 else if(action.equals("GET_ALTERNATEPRODUCT_FOR_SUMMARY")){
				    jsonObjectResult = this.getAlternateProductSummary(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }
//		    ENDS
		    
//		    RESVI STARTS PRDCLASS SUMMARY
			 else if(action.equals("GET_PRODUCTCLASSID_FOR_SUMMARY")){ 
			 jsonObjectResult = this.getPrdclsList(request);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonObjectResult.toString());
			 response.getWriter().flush(); response.getWriter().close();
			 } 
//		    ENDS
		    
		    
//		    RESVI STARTS PRDDEPT SUMMARY
			 else if(action.equals("GET_PRODUCTDEPARTMENTID_FOR_SUMMARY")){ 
			 jsonObjectResult = this.getPrdDepList(request);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonObjectResult.toString());
			 response.getWriter().flush(); response.getWriter().close();
			 } 
//		    ENDS
		    

//			Thanzith Starts Agent Summary
			 else if(action.equals("GET_CLEAR_AGENT_FOR_SUMMARY")){ 
				 jsonObjectResult = this.getClearAgentList(request);
				 response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				 response.getWriter().write(jsonObjectResult.toString());
				 response.getWriter().flush(); response.getWriter().close();
			 } 
//		    ENDS
			 else if(action.equals("GET_TRANSPORT_CLEARING_AGENT")){ 
				 jsonObjectResult = this.getTransportClearAgentList(request);
				 response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				 response.getWriter().write(jsonObjectResult.toString());
				 response.getWriter().flush(); response.getWriter().close();
			 } 

		    
		  //resvi START PRODUCTTYPEE
			    else if(action.equals("GET_PRD_FOR_SUMMARY")){
				    jsonObjectResult = this.getPrdTypeList(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }// PRODUCTTYPE end
		    
		    //resvi START UOMTYPE
			    else if(action.equals("GET_UOM_FOR_SUMMARY")){
				    jsonObjectResult = this.getUomDetails(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }//resvi UOM END
		    
		    //imti START internalcrm
			    else if(action.equals("GET_USER_DETAILS_SUMMARY")){
			    	jsonObjectResult = this.getusersummaryDetails(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }//imti internalcrm END
			   
		    //imti START Contact
			    else if(action.equals("GET_CONTACT_DETAILS_SUMMARY")){
			    	jsonObjectResult = this.getcontactsummaryDetails(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }//imti Contact END
		    
		    //imti START salesdelivery
			    else if(action.equals("GET_SALES_DELIVERY_SUMMARY")){
			    	jsonObjectResult = this.getsalesorderdeliverysummary(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }//imti salesdelivery END
		    
				 else if(action.equals("VIEW_CONTACT_DETAILS_EDIT")){
					 jsonObjectResult = this.getViewContactDetailsEdit(request);	
					 response.setContentType("application/json");
					 response.setCharacterEncoding("UTF-8");
					 response.getWriter().write(jsonObjectResult.toString());
					 response.getWriter().flush();
					 response.getWriter().close();
					 
				 }
		    
		    
		    //resvi START LOCT
			    else if(action.equals("GET_LOC_FOR_SUMMARY")){
				    jsonObjectResult = this.getLocDetails(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }//resvi LOCT end

		    //imthi USERASSIGNLOCATION LOCT
			    else if(action.equals("GET_USERASSIGNLOC_FOR_SUMMARY")){
			    	jsonObjectResult = this.getUserAssignLocDetails(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }//imthi USERASSIGNLOCATION end
		    
		    //Thanzi USERASSIGNCUSTOMER 
			    else if(action.equals("GET_USERASSIGNCUS_FOR_SUMMARY")){
			    	jsonObjectResult = this.getUserAssignCusDetails(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }//thanzi  end
		    //Thanzi USERASSIGNPRODUCT 
			    else if(action.equals("GET_USERASSIGNITEM_FOR_SUMMARY")){
			    	jsonObjectResult = this.getUserAssignitemDetails(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }//thanzi  end

		    //Azees POSCLOSINGSTOCKCATEGORY
			    else if(action.equals("GET_POSCATEGORY_FOR_SUMMARY")){
			    	jsonObjectResult = this.getPOSCategoryDetails(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }
		    
		    //imthi ASSIGNINVLOCATION LOCT
			    else if(action.equals("GET_ASSIGNINVLOC_FOR_SUMMARY")){
			    	jsonObjectResult = this.getAssignInvLocDetails(request);	
			    	response.setContentType("application/json");
			    	response.setCharacterEncoding("UTF-8");
			    	response.getWriter().write(jsonObjectResult.toString());
			    	response.getWriter().flush();
			    	response.getWriter().close();
			    }//imthi USERASSIGNLOCATION end
		    
		    
//		    RESVI STARTS USER SUMMARY
			 else if(action.equals("GET_USERADMIN_FOR_USERDETAILS")){
			    jsonObjectResult = this.getUserListforCompany(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			 }
//		    ENDS
		    
			 else if(action.equals("GET_ALL_PLANTS")){
				    jsonObjectResult = this.getPlantList(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }
		    
		    
//		    user acces for inv,acc,pay

			 else if(action.equals("GET_USERADMIN_FOR_USERACCESS")){
				 jsonObjectResult = this.getUserAccessRightsGroup(request);
				 response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				 response.getWriter().write(jsonObjectResult.toString());
				 response.getWriter().flush();
				 response.getWriter().close();
				 }
//		    end
		    
		  //imtiziaf start1 customer
		    else if(action.equals("GET_CUSTOMER_FOR_SUMMARY")){
			    jsonObjectResult = this.getViewCustomerDetails(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }//imtiziaf end customer

		    else if(action.equals("GET_PEPPOL_CUSTOMER_FOR_SUMMARY")){
		    	jsonObjectResult = this.getViewPeppolCustomerDetails(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    	
		    }
		    
		    //imtiziaf start1 activitys
		    else if(action.equals("GET_ACTIVITY_FOR_SUMMARY")){
		    	jsonObjectResult = this.getViewActivityDetails(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    	
		    }//imtiziaf end activitys
		    
		    else if (action.equals("GET_CUSTOMER_BY_CODE")) {
		    	jsonObjectResult = this.getCustomerDetails(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		    	
		    }
		    
			  //imtiziaf start1 supplier
		    else if(action.equals("GET_SUPPLIER_FOR_SUMMARY")){
			    jsonObjectResult = this.getVendorListStartsWithName(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }//imtiziaf end suppler
		    //thanz
		    else if(action.equals("GET_FREIGHT_FORWARDER_FOR_SUMMARY")){
		    	jsonObjectResult = this.getShipperListStartsWithName(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    	
		    }//thanz end suppler
		    else if(action.equals("GET_PEPPOL_SUPPLIER_FOR_SUMMARY")){
		    	jsonObjectResult = this.getPeppolVendorListStartsWithName(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    	
		    }
		    else if(action.equals("GET_OUTLET_FOR_SUMMARY")){
			    jsonObjectResult = this.getOutletListStartsWithName(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }
		    
		    else if(action.equals("GET_PRODUCT_PROMOTION_FOR_SUMMARY")){
		    	jsonObjectResult = this.getProductPromotion(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    }
		    else if(action.equals("GET_CATEGORY_PROMOTION_FOR_SUMMARY")){
		    	jsonObjectResult = this.getCategoryPromotion(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    }
		    else if(action.equals("GET_BRAND_PROMOTION_FOR_SUMMARY")){
		    	jsonObjectResult = this.getBrandPromotion(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    }
			else  if (action.equals("GET_PROMOTION_DATA")) {
	            jsonObjectResult = this.getPromotionData(request);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
	          }
			else  if (action.equals("GET_CATEGORY_PROMOTION_DATA")) {
				jsonObjectResult = this.getCategoryPromotionData(request);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}
			else  if (action.equals("GET_BRAND_PROMOTION_DATA")) {
				jsonObjectResult = this.getBrandPromotionData(request);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}
//		    CREATED BY imthi
//		    CREATED ON 30-03-2022
		    else if(action.equals("GET_OUTLETTERMINAL_FOR_SUMMARY")){
		    	jsonObjectResult = this.getOutletTerminalListStartsWithName(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    }
		    
		    else  if (action.equals("GET_TERMINAL_DATA")) {
				String aCustCode = StrUtils.fString(request.getParameter("TERMINALNAME")).trim();
				jsonObjectResult = this.getOutletTerminalData(request);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}
		    //END
		
		    else if (action.equals("GET_SUPPLIER_BY_CODE")) {
		    	jsonObjectResult = this.getVendorDetails(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		    	
		    }
		    
		    else if (action.equals("GET_STOCKMOVE_TRANS_ID")) {
		    	jsonObjectResult = this.getStockTransId(request);	
		    	response.setContentType("application/json");
		    	response.setCharacterEncoding("UTF-8");
		    	response.getWriter().write(jsonObjectResult.toString());
		    	response.getWriter().flush();
		    	response.getWriter().close();
		    }
		    
		    //IMTIZIAF START SUPPLIER type
		    else if(action.equals("GET_SUPPLIERTYPE_FOR_SUMMARY")){
			    jsonObjectResult = this.getSupplierTypeSummary(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				
				 
			 }//imtiziaf supplier type end
		    
		    //imtiziaf start product

		    else if(action.equals("GET_PRODUCT_FOR_SUMMARY")){ 
			 jsonObjectResult = this.queryItemMstForSearchCriteriaNew(request);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonObjectResult.toString());
			 response.getWriter().flush(); response.getWriter().close();
			 
			 } //imtiziaf end product  end
		    
		    
//		    //imtiziaf start employee
			
			 else if(action.equals("GET_EMPLOYEE_FOR_SUMMARY")){ 
			 jsonObjectResult = this.getViewEmployeeNameDetails(request);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().write(jsonObjectResult.toString());
			 response.getWriter().flush(); response.getWriter().close();
			 
			 } //imtiziaf end employee
			 	    
			 else if(action.equals("VIEW_FOOTER_DETAILS_EDIT")){
				    jsonObjectResult = this.getViewFooterDetailsEdit(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					 
				 }
		    
			 else if(action.equals("DELETE_FOOTER")){
					xmlStr = "";
					xmlStr = DeleteFooter(request, response);
								
				 }
		    
		    
			 else if(action.equals("ADD_REMARKS")){
				String msg   = addremarks(request, response);
				jsonObjectResult = this.getRemarksDetails(request);	 
				    jsonObjectResult.put("errorMsg", msg);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					
					
				
				 
			 }else if(action.equals("VIEW_REMARKS_DETAILS")){
				    jsonObjectResult = this.getViewRemarksDetails(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					 
				 }
		    
		    
			 else if(action.equals("VIEW_REMARKS_DETAILS_EDIT")){
				    jsonObjectResult = this.getViewRemarksDetailsEdit(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					 
				 }
		    
			 else if(action.equals("DELETE_REMARKS")){
					xmlStr = "";
					xmlStr = DeleteRemarks(request, response);
					
				 }
		    
//			RESVI STARTS TRANSPORTMODE

			 else if(action.equals("VIEW_TRANSPORT_MODE_EDIT")){
				    jsonObjectResult = this.getViewTransportModeEdit(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					 
				 }
		    
			 else if(action.equals("VIEW_TRANSPORT_MODE")){
				    jsonObjectResult = this.getViewTransportMode(request);	
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
					 
				 }
		    
		    
		    
		    
		    
			 else if(action.equals("DELETE_TRANSPORT_MODE")){
					xmlStr = "";
					xmlStr = DeleteTransportMode(request, response);
					
				 }
		    
		    

			 else if(action.equals("ADD_TRANSPORT_MODE")){
					String msg   = addtransportmode(request, response);
					jsonObjectResult = this.getTransportModeDetails(request);	 
					    jsonObjectResult.put("errorMsg", msg);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
			 }
	
		    
		    
			 else if(action.equals("ADD_INCOTERMS")){
					String msg   = addincoterms(request, response);
					String isModal = StrUtils.fString(request.getParameter("ISMODAL"));
					if(isModal.equalsIgnoreCase("")) {
						jsonObjectResult = this.getINCOTERMSDetails(request);
					}else {
						String IncoTerms = StrUtils.fString(request.getParameter("INCOTERMS"));
						jsonObjectResult.put("MESSAGE", "Supplier Added Successfully");
						jsonObjectResult.put("INCOTERM", IncoTerms);		 
						jsonObjectResult.put("STATUS", "SUCCESS");
					}
					    jsonObjectResult.put("errorMsg", msg);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
					
					 
				 }else if(action.equals("VIEW_INCOTERMS_DETAILS")){
					    jsonObjectResult = this.getViewINCOTERMSDetails(request);	
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
						 
					 }
		    
				 else if(action.equals("VIEW_INCOTERMS_DETAILS_EDIT")){
					    jsonObjectResult = this.getViewINCOTERMSDetailsEdit(request);	
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
						 
					 }
		    
				 else if(action.equals("DELETE_INCOTERMS")){
						xmlStr = "";
						xmlStr = DeleteINCOTERMS(request, response);
						
					 }
		    
				 else if(action.equals("ADD_SHIPPING_DETAILS")){
						String msg   = addshippingdetails(request, response);
						jsonObjectResult = this.getshippingdetails(request);	 
						    jsonObjectResult.put("errorMsg", msg);
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(jsonObjectResult.toString());
							response.getWriter().flush();
							response.getWriter().close();
						
						 
					 }else if(action.equals("VIEW_SHIPPING_DETAILS")){
						    jsonObjectResult = this.getviewshippingdetails(request);	
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(jsonObjectResult.toString());
							response.getWriter().flush();
							response.getWriter().close();
							 
						 }
					 else if(action.equals("DELETE_SHIPPING_DETAILS")){
							xmlStr = "";
							xmlStr = DeleteShippingDetails(request, response);
							
						 } else if(action.equals("ADD_PAYMENTTYPE")){//***************************PAYMENTTYPE****************************************//	
								String msg   = addpaymenttype(request, response);
								jsonObjectResult = this.getPaymentTypeDetails(request);	 
								    jsonObjectResult.put("errorMsg", msg);
									response.setContentType("application/json");
									response.setCharacterEncoding("UTF-8");
									response.getWriter().write(jsonObjectResult.toString());
									response.getWriter().flush();
									response.getWriter().close();
									
									
								 
							 }else if(action.equals("VIEW_PAYMENTTYPE_DETAILS")){
								    jsonObjectResult = this.getViewPaymentTypeDetails(request);	
									response.setContentType("application/json");
									response.setCharacterEncoding("UTF-8");
									response.getWriter().write(jsonObjectResult.toString());
									response.getWriter().flush();
									response.getWriter().close();
									 
								 } else if(action.equals("VIEW_PAYMENTTYPE_DETAILS_EDIT")){
									    jsonObjectResult = this.getViewPaymentTypeDetailsEdit(request);	
										response.setContentType("application/json");
										response.setCharacterEncoding("UTF-8");
										response.getWriter().write(jsonObjectResult.toString());
										response.getWriter().flush();
										response.getWriter().close();
										 
									 } else if(action.equals("DELETE_PAYMENTTYPE")){
											xmlStr = "";
											xmlStr = DeletePaymentType(request, response);
										
									 }else if(action.equals("ADD_PAYMENTMODE")){//***************************PAYMENTMODE****************************************//	
									String msg   = addpaymentmode(request, response);
									jsonObjectResult = this.getPaymentModeDetails(request);	 
									    jsonObjectResult.put("errorMsg", msg);
										response.setContentType("application/json");
										response.setCharacterEncoding("UTF-8");
										response.getWriter().write(jsonObjectResult.toString());
										response.getWriter().flush();
										response.getWriter().close();
									 
								 }else if(action.equals("VIEW_PAYMENTMODE_DETAILS")){
									    jsonObjectResult = this.getViewPaymentModeDetails(request);	
										response.setContentType("application/json");
										response.setCharacterEncoding("UTF-8");
										response.getWriter().write(jsonObjectResult.toString());
										response.getWriter().flush();
										response.getWriter().close();
										 
									 }
						    
							 else if(action.equals("VIEW_PAYMENTMODE_DETAILS_EDIT")){
								 jsonObjectResult = this.getViewPaymentModeDetailsEdit(request);	
								 response.setContentType("application/json");
								 response.setCharacterEncoding("UTF-8");
								 response.getWriter().write(jsonObjectResult.toString());
								 response.getWriter().flush();
								 response.getWriter().close();
								 
							 } else if(action.equals("DELETE_PAYMENTMODE")){
								 xmlStr = "";
								 xmlStr = DeletePaymentMode(request, response);
								 
							 }else if(action.equals("ADD_PAYMENTTERM")){//***************************PAYMENTMODE****************************************//	
									String msg   = addpaymentterm(request, response);
									jsonObjectResult = this.getPaymentTermDetails(request);	 
									    jsonObjectResult.put("errorMsg", msg);
										response.setContentType("application/json");
										response.setCharacterEncoding("UTF-8");
										response.getWriter().write(jsonObjectResult.toString());
										response.getWriter().flush();
										response.getWriter().close();
								 }
		    				
							 else if(action.equals("VIEW_PAYMENTTERM_DETAILS")){
								    jsonObjectResult = this.getViewPaymentTermDetails(request);	
									response.setContentType("application/json");
									response.setCharacterEncoding("UTF-8");
									response.getWriter().write(jsonObjectResult.toString());
									response.getWriter().flush();
									response.getWriter().close();
									 
								 }
							 else if(action.equals("VIEW_PAYMENTTERM_DETAILS_EDIT")){
								 jsonObjectResult = this.getViewPaymentTermDetailsEdit(request);	
								 response.setContentType("application/json");
								 response.setCharacterEncoding("UTF-8");
								 response.getWriter().write(jsonObjectResult.toString());
								 response.getWriter().flush();
								 response.getWriter().close();
								 
							 }
							
							
							 else if(action.equals("DELETE_PAYMENTTERM")){
								 xmlStr = "";
								 xmlStr = DeletePaymentTerm(request, response);
								 
							 }else if(action.equals("ADD_HSCODE")){//****************************HSCODE****************************************//	
										String msg   = addHSCODE(request, response);
										jsonObjectResult = this.getHSCODEDetails(request);	 
										    jsonObjectResult.put("errorMsg", msg);
											response.setContentType("application/json");
											response.setCharacterEncoding("UTF-8");
											response.getWriter().write(jsonObjectResult.toString());
											response.getWriter().flush();
											response.getWriter().close();
											
											
										
										 
									 }else if(action.equals("VIEW_HSCODE_DETAILS")){
										    jsonObjectResult = this.getViewHSCODEDetails(request);	
											response.setContentType("application/json");
											response.setCharacterEncoding("UTF-8");
											response.getWriter().write(jsonObjectResult.toString());
											response.getWriter().flush();
											response.getWriter().close();
										 }else if(action.equals("ADD_POS_DEPT")){	
												String msg   = addPOSDEPT(request, response);
												jsonObjectResult = this.getPOSDEPT(request);	 
												    jsonObjectResult.put("errorMsg", msg);
													response.setContentType("application/json");
													response.setCharacterEncoding("UTF-8");
													response.getWriter().write(jsonObjectResult.toString());
													response.getWriter().flush();
													response.getWriter().close();
										 }else if(action.equals("POS_DEPT_DELETE")){	
											 String msg   = delPOSDept(request, response);
											 jsonObjectResult = this.getPOSDEPT(request);
											 jsonObjectResult.put("errorMsg", msg);
											 response.setContentType("application/json");
											 response.setCharacterEncoding("UTF-8");
											 response.getWriter().write(jsonObjectResult.toString());
											 response.getWriter().flush();
											 response.getWriter().close();
										 }else if(action.equals("VIEW_POS_DEPT_DETAILS")){
											    jsonObjectResult = this.getViewPosDeptDetails(request);	
												response.setContentType("application/json");
												response.setCharacterEncoding("UTF-8");
												response.getWriter().write(jsonObjectResult.toString());
												response.getWriter().flush();
												response.getWriter().close();
										 }
		//Thanzith 								 
										 else if(action.equals("ADD_POS_CLS")){	
												String msg   = addPOSCLS(request, response);
												jsonObjectResult = this.getPOSCLS(request);	 
												    jsonObjectResult.put("errorMsg", msg);
													response.setContentType("application/json");
													response.setCharacterEncoding("UTF-8");
													response.getWriter().write(jsonObjectResult.toString());
													response.getWriter().flush();
													response.getWriter().close();
										 }else if(action.equals("POS_CLS_DELETE")){	
											 String msg   = delPOSCls(request, response);
											 jsonObjectResult = this.getPOSCLS(request);
											 jsonObjectResult.put("errorMsg", msg);
											 response.setContentType("application/json");
											 response.setCharacterEncoding("UTF-8");
											 response.getWriter().write(jsonObjectResult.toString());
											 response.getWriter().flush();
											 response.getWriter().close();
										 }else if(action.equals("VIEW_POS_CLS_DETAILS")){
											    jsonObjectResult = this.getViewPosClsDetails(request);	
												response.setContentType("application/json");
												response.setCharacterEncoding("UTF-8");
												response.getWriter().write(jsonObjectResult.toString());
												response.getWriter().flush();
												response.getWriter().close();
										 }
	//	    End
										 else if(action.equals("ADD_POS_PRD")){	
												String msg   = addPOSPRD(request, response);
												jsonObjectResult = this.getPOSPRD(request);	 
												jsonObjectResult.put("errorMsg", msg);
												response.setContentType("application/json");
												response.setCharacterEncoding("UTF-8");
												response.getWriter().write(jsonObjectResult.toString());
												response.getWriter().flush();
												response.getWriter().close();
											}else if(action.equals("POS_PRODUCT_DELETE")){	
												String msg   = delPOSPrd(request, response);
												jsonObjectResult = this.getPOSPRD(request);
												    jsonObjectResult.put("errorMsg", msg);
													response.setContentType("application/json");
													response.setCharacterEncoding("UTF-8");
													response.getWriter().write(jsonObjectResult.toString());
													response.getWriter().flush();
													response.getWriter().close();
											}else if(action.equals("VIEW_POS_PRD_DETAILS")){
											    jsonObjectResult = this.getViewPosPrdDetails(request);	
												response.setContentType("application/json");
												response.setCharacterEncoding("UTF-8");
												response.getWriter().write(jsonObjectResult.toString());
												response.getWriter().flush();
												response.getWriter().close();
										 }
								    
								    
									 else if(action.equals("VIEW_HSCODE_DETAILS_EDIT")){
										    jsonObjectResult = this.getViewHSCODEDetailsEdit(request);	
											response.setContentType("application/json");
											response.setCharacterEncoding("UTF-8");
											response.getWriter().write(jsonObjectResult.toString());
											response.getWriter().flush();
											response.getWriter().close();
											 
										 }
								    
									 else if(action.equals("DELETE_HSCODE")){
											xmlStr = "";
											xmlStr = DeleteHSCODE(request, response);
											
									}else if(action.equals("ADD_COO")){//****************************COO****************************************//	
										String msg   = addCOO(request, response);
										jsonObjectResult = this.getCOODetails(request);	 
										    jsonObjectResult.put("errorMsg", msg);
											response.setContentType("application/json");
											response.setCharacterEncoding("UTF-8");
											response.getWriter().write(jsonObjectResult.toString());
											response.getWriter().flush();
											response.getWriter().close();
											
											
										
										 
									 }else if(action.equals("VIEW_COO_DETAILS")){
										    jsonObjectResult = this.getViewCOODetails(request);	
											response.setContentType("application/json");
											response.setCharacterEncoding("UTF-8");
											response.getWriter().write(jsonObjectResult.toString());
											response.getWriter().flush();
											response.getWriter().close();
											 
										 }
								    
								    
									 else if(action.equals("VIEW_COO_DETAILS_EDIT")){
										    jsonObjectResult = this.getViewCOODetailsEdit(request);	
											response.setContentType("application/json");
											response.setCharacterEncoding("UTF-8");
											response.getWriter().write(jsonObjectResult.toString());
											response.getWriter().flush();
											response.getWriter().close();
											 
										 }
								    
									 else if(action.equals("DELETE_COO")){
											xmlStr = "";
											xmlStr = DeleteCOO(request, response);
											
										 }
		    
				else  if (action.equals("SUPPLIER_CHECK")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
					jsonObjectResult = this.validateSupplier(plant, userName, aCustCode);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
	          }		 
				else  if (action.equals("FREIGHT_FORWARDER_CHECK")) {
					String aCustCode = StrUtils.fString(request.getParameter("SHIPPER_CODE")).trim();
					jsonObjectResult = this.validateShipper(plant, userName, aCustCode);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}		 
				else  if (action.equals("OUTLET_CHECK")) {
					String aOutletCode = StrUtils.fString(request.getParameter("OUTLETS_CODE")).trim();
					jsonObjectResult = this.validateOutlet(plant, userName, aOutletCode);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
	          }		 
		    
		    
				else  if (action.equals("PROJECT_CHECK")) {
					String project = StrUtils.fString(request.getParameter("PROJECT_NAME")).trim();
					jsonObjectResult = this.validateProject(plant, userName, project);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}		 
				else  if (action.equals("ORDER_CHECK")) {
					String status = StrUtils.fString(request.getParameter("STATUS")).trim();
					String OrderType = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
					jsonObjectResult = this.validateOrderStatus(plant, userName, status, OrderType);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}		 
			
				else  if (action.equals("CUSTOMER_CHECK")) {
						String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		             	jsonObjectResult = this.validateCustomer(plant, userName, aCustCode);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
		          }
		    
				else  if (action.equals("TRANSPORT_CHECK")) {
					String transport = StrUtils.fString(request.getParameter("TRANSPORT_MODE")).trim();
					jsonObjectResult = this.validateTransport(plant, userName, transport);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("PAYTYPE_CHECK")) {
					String paymenttype = StrUtils.fString(request.getParameter("PAYMENTMODE")).trim();
					jsonObjectResult = this.validatePayType(plant, userName, paymenttype);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("PAYTERMS_CHECK")) {
					String paymentterms = StrUtils.fString(request.getParameter("PAYMENT_TERMS")).trim();
					jsonObjectResult = this.validatePayTerms(plant, userName, paymentterms);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("USER_CHECK")) {
					String user = StrUtils.fString(request.getParameter("USER")).trim();
					jsonObjectResult = this.validateUser(plant, userName, user);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("BANK_CHECK")) {
					String bankname = StrUtils.fString(request.getParameter("BANKNAME")).trim();
					jsonObjectResult = this.validateBank(plant, userName, bankname);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("ORDERTYPE_CHECK")) {
					String ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
					jsonObjectResult = this.validateOrderType(plant, userName, ordertype);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("EMPLOYEE_CHECKS")) {
					String employee = StrUtils.fString(request.getParameter("FNAME")).trim();
					jsonObjectResult = this.validateEmployees(plant, userName, employee);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("INCOTERMS_CHECK")) {
					String incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
					jsonObjectResult = this.validateIncoterms(plant, userName, incoterms);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				 else  if (action.equals("CLEARING_AGENT_CHECK")) {
						String clearingagent = StrUtils.fString(request.getParameter("CLEARINGAGENT")).trim();
						jsonObjectResult = this.validateClearingAgent(plant, userName, clearingagent);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}
					else  if (action.equals("TYPE_OF_CLEARANCE_CHECK")) {
						String typeofclearance = StrUtils.fString(request.getParameter("TYPEOFCLEARANCE")).trim();
						jsonObjectResult = this.validateTypeOfClearance(plant, userName, typeofclearance);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}
		    
				else  if (action.equals("CURRENCY_CHECK")) {
					String currency = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
					jsonObjectResult = this.validateCurrency(plant, userName, currency);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("TAX_CHECK")) {
					String tax = StrUtils.fString(request.getParameter("TAXTYPE")).trim();
					jsonObjectResult = this.validateTax(plant, userName, tax);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("ACCOUNT_CHECK")) {//Account Validation --08.22 Azees
					String account = StrUtils.fString(request.getParameter("ACCOUNT")).trim();
					jsonObjectResult = this.validateaccount(plant, userName, account);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}

				else  if (action.equals("UOM_CHECK")) {
					String uom = StrUtils.fString(request.getParameter("UOM")).trim();
					jsonObjectResult = this.validateUom(plant, userName, uom);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("LOC_CHECK")) {
					String loc = StrUtils.fString(request.getParameter("LOC")).trim();
					jsonObjectResult = this.validateLoc(plant, userName, loc);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("HSCODE_CHECK")) {
					String hscode = StrUtils.fString(request.getParameter("HSCODE")).trim();
					jsonObjectResult = this.validateHscode(plant, userName, hscode);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("COO_CHECK")) {
					String coo = StrUtils.fString(request.getParameter("COO")).trim();
					jsonObjectResult = this.validateCoo(plant, userName, coo);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("DEPT_CHECK")) {
					String dept = StrUtils.fString(request.getParameter("PRD_DEPT_ID")).trim();
					jsonObjectResult = this.validateDept(plant, userName, dept);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("CAT_CHECK")) {
					String cat = StrUtils.fString(request.getParameter("PRD_CLS_ID")).trim();
					jsonObjectResult = this.validateCat(plant, userName, cat);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("TYPE_CHECK")) {
					String type = StrUtils.fString(request.getParameter("PRD_TYPE_ID")).trim();
					jsonObjectResult = this.validateType(plant, userName, type);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("BRAND_CHECK")) {
					String brand = StrUtils.fString(request.getParameter("PRD_BRAND_ID")).trim();
					jsonObjectResult = this.validateBrand(plant, userName, brand);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
		    
				else  if (action.equals("CUSTOMER_CHECKUSER")) {
					 String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		             jsonObjectResult = this.validateCustomerUser(plant, userName, aCustCode);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
		          }
				else  if (action.equals("GET_SUPPLIER_DATA")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getSupplierData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }else  if (action.equals("GET_SUPPLIER_DATA_PEPPOL")) {
						String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getSupplierDataPeppol(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
		          }
					else  if (action.equals("GET_SUPPLIER_DATA_ACTIVE")) {
						String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getSupplierDataActive(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
				else  if (action.equals("GET_FREIGHT_FORWARDER_DATA")) {
					String aCustCode = StrUtils.fString(request.getParameter("SHIPPER_CODE")).trim();
					jsonObjectResult = this.getShipperData(request);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}
				else  if (action.equals("GET_OUTLET_DATA")) {
					String aCustCode = StrUtils.fString(request.getParameter("OUTLET")).trim();
		            jsonObjectResult = this.getOutletData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_SUPPLIERTYPE_DATA")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getSupplierTypeData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("getCustomerListData")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getCustomerListData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("getCustomerListTypeData")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getCustomerListTypeData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("getEmployeeListStartsWithName")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getEmployeeListStartsWithName(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_GST_TYPE_DATA")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getGstTypeData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_GST_TYPE_DATA_PO")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getGstTypeDataPO(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
				}else if(action.equals("ADD_PEPPOL")){//imthi added 24-12-2022	
					 String msg   = addPEPPOL(request, response);
					 jsonObjectResult.put("message", msg);
					 response.setContentType("application/json");
					 response.setCharacterEncoding("UTF-8");
					 response.getWriter().write(jsonObjectResult.toString());
					 response.getWriter().flush();
					 response.getWriter().close();
				}else  if (action.equals("GET_TAX_TYPE_DATA_ADJ")) {
		            jsonObjectResult = this.getTaxTypeDataADJ(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_TAX_TYPE_DATA_PO")) {
		            jsonObjectResult = this.getTaxTypeDataPO(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_GST_TYPE_DATA_SALES")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getGstTypeDataSales(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_GST_TYPE_DATA_SALES_SO")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getGstTypeDataSalesSO(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_GST_TYPE_DATA_EXPENSE")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getGstTypeDataExpense(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("CREATE_SUPPLIER")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.createSupplier(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }
				else  if (action.equals("GET_CUSTOMER_DATA")) {
					String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		            jsonObjectResult = this.getCustomerData(request);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonObjectResult.toString());
	                response.getWriter().flush();
	                response.getWriter().close();
		          }else  if (action.equals("GET_CUSTOMER_DATA_PEPPOL")) {
						String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getCustomerDataPeppol(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }else  if (action.equals("GET_CUSTOMER_DATA_ACTIVE")) {
						String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getCustomerDataActive(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }else  if (action.equals("GET_SUPPLIER_SEQUENCE")) {
						String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getNextSupplierSequence(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          
		             }else  if (action.equals("GET_PRODUCT_DEPARTMENT_SEQUENCE")) {
			           String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
                       jsonObjectResult = this.getNextPrdDep(request);
                       response.setContentType("application/json");
                       response.setCharacterEncoding("UTF-8");
                       response.getWriter().write(jsonObjectResult.toString());
                       response.getWriter().flush();
                        response.getWriter().close();
		             }else  if (action.equals("GET_SUPPLIER_TYPE_SEQUENCE")) {
				           String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
	                       jsonObjectResult = this.getNextSupType(request);
	                       response.setContentType("application/json");
	                       response.setCharacterEncoding("UTF-8");
	                       response.getWriter().write(jsonObjectResult.toString());
	                       response.getWriter().flush();
	                        response.getWriter().close();
			             }else  if (action.equals("GET_CUSTOMER_TYPE_SEQUENCE")) {
					           String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		                       jsonObjectResult = this.getNextCustType(request);
		                       response.setContentType("application/json");
		                       response.setCharacterEncoding("UTF-8");
		                       response.getWriter().write(jsonObjectResult.toString());
		                       response.getWriter().flush();
		                        response.getWriter().close();
				             }else  if (action.equals("GET_PRODUCT_CATEGORY_SEQUENCE")) {
				           String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
	                       jsonObjectResult = this.getNextPrdCat(request);
	                       response.setContentType("application/json");
	                       response.setCharacterEncoding("UTF-8");
	                       response.getWriter().write(jsonObjectResult.toString());
	                       response.getWriter().flush();
	                        response.getWriter().close(); 
	                        
				             }else  if (action.equals("GET_PRODUCT_LOCATION_SEQUENCE")) {
				            	 String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
				            	 jsonObjectResult = this.getNextLocCat(request);
				            	 response.setContentType("application/json");
				            	 response.setCharacterEncoding("UTF-8");
				            	 response.getWriter().write(jsonObjectResult.toString());
				            	 response.getWriter().flush();
				            	 response.getWriter().close(); 
	                        
		             }else  if (action.equals("GET_PRODUCT_SUB_CATEGORY_SEQUENCE")) {
				           String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
	                       jsonObjectResult = this.getNextPrdSubCat(request);
	                       response.setContentType("application/json");
	                       response.setCharacterEncoding("UTF-8");
	                       response.getWriter().write(jsonObjectResult.toString());
	                       response.getWriter().flush();
	                        response.getWriter().close();       
                       
		             }else  if (action.equals("GET_PRODUCT_BRAND_SEQUENCE")) {
				           String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
	                       jsonObjectResult = this.getNextPrdBrand(request);
	                       response.setContentType("application/json");
	                       response.setCharacterEncoding("UTF-8");
	                       response.getWriter().write(jsonObjectResult.toString());
	                       response.getWriter().flush();
	                        response.getWriter().close(); 
                      
		             }else  if (action.equals("GET_EMPLOYEE_DATA")) {
		        	  String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getEmployeeData(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
		          else  if (action.equals("GET_EMPLOYEE_DATA_PAYROLL")) {
		        	  String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getEmployeeDatapayroll(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
		          else  if (action.equals("GET_EMPLOYEE_DATA_EMPID")) {
		        	  String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.getEmployeeDatawithoutid(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
		          else  if (action.equals("CREATE_CUSTOMER")) {
							String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
				            jsonObjectResult = this.createCustomer(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				          }
		          else  if (action.equals("GET_CUSTOMER_SEQUENCE")) {
								String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
					            jsonObjectResult = this.getNextCustomerSequence(request);
				                response.setContentType("application/json");
				                response.setCharacterEncoding("UTF-8");
				                response.getWriter().write(jsonObjectResult.toString());
				                response.getWriter().flush();
				                response.getWriter().close();
					          }
		          else  if (action.equals("GET_EMPLOYEE_SEQUENCE")) {
									String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
						            jsonObjectResult = this.getNextEmployeeSequence(request);
					                response.setContentType("application/json");
					                response.setCharacterEncoding("UTF-8");
					                response.getWriter().write(jsonObjectResult.toString());
					                response.getWriter().flush();
					                response.getWriter().close();
						          }
		          else  if (action.equals("NOOFEMPLOYEE_CHECK")) {
		        	  String NOOFEMPLOYEE = StrUtils.fString((String) request.getSession().getAttribute("NOOFEMPLOYEE"));
			             jsonObjectResult = this.validatenoofEmployee(plant,NOOFEMPLOYEE);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }
		          else  if (action.equals("EMPLOYEE_CHECK")) {
						 String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			             jsonObjectResult = this.validateEmployee(plant, userName, aCustCode);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }
		          else  if (action.equals("CREATE_EMPLOYEE")) {
						String aCustCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			            jsonObjectResult = this.createEmployee(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
		          else  if (action.equals("VIEW_SHIPMENT_SUMMARY_VIEW")) {
			            jsonObjectResult = this.getshipmentview(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
		          else  if (action.equals("GET_SHIPMENT_SEQUENCE")) {
				            jsonObjectResult = this.getNextShipmentSequence(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				          }
		          else  if (action.equals("SAVE_SHIPMENT")) {
		        	  jsonObjectResult = this.saveShipment(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
		          }
		          else  if (action.equals("GET_DEPARTMENT_DATA")) {
			            jsonObjectResult = this.getDepartmentData(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
		          else  if (action.equals("GET_BANKNAME_DATA")) {
			            jsonObjectResult = this.getBankNameData(request);
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			          }
		          else  if (action.equals("ADD_SHIPMENT")) {
					String username = (String) request.getSession().getAttribute("LOGIN_USER");
					String shipmentCode = "", pono = "",cmd = "", sCons = "Y";
					UserTransaction ut = null;
					DateUtils dateutils = new DateUtils();
					boolean isAdded = false;
					String result="";
					TblControlDAO _TblControlDAO = new TblControlDAO();
					try{
						////////////////
						boolean isMultipart = ServletFileUpload.isMultipartContent(request);
						if(isMultipart) {
						FileItemFactory factory = new DiskFileItemFactory();
						ServletFileUpload upload = new ServletFileUpload(factory);				
						List items = upload.parseRequest(request);
						Iterator iterator = items.iterator();
						StrUtils strUtils = new StrUtils();
						
						while (iterator.hasNext()) {
							FileItem fileItem = (FileItem) iterator.next();
							/* ShipmentHdr*/
							if (fileItem.isFormField()) {
								if (fileItem.getFieldName().equalsIgnoreCase("shipmentCode")) {
									shipmentCode = StrUtils.fString(fileItem.getString()).trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
									pono = StrUtils.fString(fileItem.getString()).trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("cmd")) {
									cmd = StrUtils.fString(fileItem.getString()).trim();
								}
							}
						}
						}
								Hashtable ShipmentHdr =new Hashtable(); 
								ShipmentHdr.put("PLANT", plant);
								ShipmentHdr.put("PONO", pono);
								ShipmentHdr.put("SHIPMENT_CODE", shipmentCode);
								ShipmentHdr.put("STATUS", sCons);
								ShipmentHdr.put("CRAT",dateutils.getDateTime());
								ShipmentHdr.put("CRBY",username);
								ShipmentHdr.put("UPAT",dateutils.getDateTime());
								/*Get Transaction object*/
								ut = DbBean.getUserTranaction();				
								/*Begin Transaction*/
								ut.begin();
								if(cmd.equalsIgnoreCase("Edit"))
								{
									if(!shipmentCode.isEmpty())
									{										
										ShipmentHdr.put("UPBY",username);
										isAdded = _MasterUtil.updateShipment(ShipmentHdr);
									}
								}
								else
								{
									isAdded = _MasterUtil.addShipment(ShipmentHdr,plant);
									if(isAdded)
										isAdded = _TblControlDAO.updateSeqNo("SHIPMENT",plant);
								}
							if(isAdded) {
								DbBean.CommitTran(ut);
								if(cmd.equalsIgnoreCase("Edit"))
									result = "Shipment updated successfully";
								else
									result = "Shipment created successfully";
							}
							else {
								DbBean.RollbackTran(ut);
								result = "Shipment not created";
							}
							response.sendRedirect("jsp/createShipment.jsp?result="+ result);/* Redirect to Shipment Summary page */
						}catch (Exception e) {
							 DbBean.RollbackTran(ut);
						}
		          	  }else  if (action.equals("ADD_PLANT")) {
		          		 
		          		String PLANT="",COMPANY="",PLNTDESC="",companyregnumber="",STARTDATE="",EXPIRYDATE="",ACTUALEXPIRYDATE="",SDATE="",EDATE="",RCBNO="",
		          				NAME="",DESGINATION="",TELNO="",HPNO="",EMAIL="",ADD1="",ADD2="",ADD3="",ADD4="",FAX="",REMARKS="",COUNTY="",ZIP="",
		          			    SALESPERCENT="",SDOLLARFRATE="",SCENTSFRATE="",SALES="",NOOFCATALOGS="",EDOLLARFRATE="",STATE="",SEALNAME="",APPPATH="",SIGNATURENAME="",
		          			    ECENTSFRATE="",currencyCode = "",NOOFSIGNATURES="",TAXBY="", FLATRATE="",PERCENTAGE="", ENABLEINVENTORY = "0", ENABLEACCOUNTING = "0",OWNERAPP="0",CUSTOMERAPP="0",MANAGERAPP="0",STOREAPP = "0",RIDEAPP="0",TAXBYLABEL="",DECIMALPRECISION="0",ISTAXREG="0",COMP_INDUSTRY="",REPROTSBASIS="",
		          			    FISCALYEAR="",PAYROLLYEAR="",REGION="",TAXBYLABELORDERMANAGEMENT="",strtaxbylabe1order="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",ENABLEPAYROLL="",ENABLEPOS="",FYEAR="",PYEAR="",EMPLOYEEWORKINGMANDAYSBY="",
		          			    		NOOFSUPPLIER="",NOOFCUSTOMER="",NOOFEMPLOYEE="",NOOFUSER="",NOOFINVENTORY="",NOOFLOCATION="",NOOFEXPBILLINV="",NOOFORDER="" ,PRODUCT_SHOWBY_CATAGERY="",ISASSIGN_USERLOC="",SHOW_STOCKQTY_ONAPP="",ALLOWPOSTRAN_LESSTHAN_AVBQTY="",ISSHOWPOSPRICEBYOUTLET="",ISPOSRETURN_TRAN="",ISPOSVOID_TRAN="",SHOW_POS_SUMMARY="",ISMANAGEWORKFLOW="",ALLOWCATCH_ADVANCE_SEARCH="",SETCURRENTDATE_ADVANCE_SEARCH="",ISPRODUCTMAXQTY="",ISSALESAPP_TAXINCLUSIVE="",ISPOSTAXINCLUSIVE="",ISAUTO_CONVERT_ESTPO="",ISAUTO_CONVERT_RECEIPTBILL="",SETCURRENTDATE_GOODSRECEIPT="",SETCURRENTDATE_PICKANDISSUE="",ISAUTO_CONVERT_ISSUETOINVOICE="",SHOWITEM_AVGCOST="",ISAUTO_MINMAX_MULTIESTPO="",ISAUTO_CONVERT_SOINVOICE="", NOOFPAYMENT="", NOOFJOURNAL="", NOOFCONTRA="",SHOPIFY="",LAZADA="",SHOPEE="",AMAZON="",ISREFERENCEINVOICE="",ISSUPPLIERMANDATORY="",ISPRICE_UPDATEONLY_IN_OWNOUTLET=""; 
		          		String sUserId = (String) request.getSession().getAttribute("LOGIN_USER");
		          		String Plant = (String) request.getSession().getAttribute("PLANT");
		          		String sPlant  = "",sLogo="";
		          		String res="";
		          		String existingplnt="";
		          		long randomCompany=0;
		          		boolean flag=false;
		          		PlantMstUtil plantmstutil = new PlantMstUtil();
		          		MasterUtil _MasterUtil=new  MasterUtil();
		          		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		          		plantmstutil.setmLogger(mLogger);
		          		_PlantMstDAO.setmLogger(mLogger);
		          		DateUtils dateutils = new DateUtils();
		          		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		        		List<Hashtable<String,String>> AttachmentList = null;
		        		List<Hashtable<String,String>> AttachmentInfoList = null;
		        		Hashtable<String,String> Attachment = null;
		        		//random+(Math.floor(Math.random() * (max - min + 1)) + min);
						 randomCompany=(long)(Math.random()*10000000000L);
						 //String stringCompany="C"+dateUtils.getDateTime()+Long.toString(randomCompany)+"S2T" ;
						String stringCompany="C"+Long.toString(randomCompany)+"S2T" ;						
					    PLANT=stringCompany;
					    String result = "",strpath = "" , catlogpath = "";
					    
					    try {
		        		if(isMultipart)
		        		{
		        			boolean imageSizeflg = false;
		         			String fileLocation = "";
		         			String filetempLocation = "";		         			
		         			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		        			FileItemFactory factory = new DiskFileItemFactory();
		        			ServletFileUpload upload = new ServletFileUpload(factory);
		        			AttachmentList = new ArrayList<Hashtable<String,String>>();
		        			AttachmentInfoList = new ArrayList<Hashtable<String,String>>();
		        			
		        			List items = upload.parseRequest(request);
		        			Iterator iterator = items.iterator();
		        			
		        			while (iterator.hasNext()) {
		        				FileItem item = (FileItem) iterator.next();
		        				if (item.isFormField()) {

		        					if (item.getFieldName()
		        							.equalsIgnoreCase("PLANT")) {
		        						sPlant = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("PLNTDESC")) {
		        						PLNTDESC = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("STARTDATE")) {
		        						STARTDATE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("EXPIRYDATE")) {
		        						EXPIRYDATE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ACTUALEXPIRYDATE")) {
		        						ACTUALEXPIRYDATE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("NOOFCATALOGS")) {
		        						NOOFCATALOGS = StrUtils.fString(item.getString());
		        					}					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("NOOFSIGNATURES")) {
		        						NOOFSIGNATURES = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("BaseCurrency")) {
		        						currencyCode = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("decimal_precision")) {
		        						DECIMALPRECISION = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ENABLE_INVENTORY")) {
		        						ENABLEINVENTORY = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ENABLE_ACCOUNTING")) {
		        						ENABLEACCOUNTING = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("OWNER_APP")) {
		        						OWNERAPP = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("CUSTOMER_APP")) {
		        						CUSTOMERAPP = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("MANAGER_APP")) {
		        						MANAGERAPP = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("STORE_APP")) {
		        						STOREAPP = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("RIDE_APP")) {
		        						RIDEAPP = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("PRODUCT_SHOWBY_CATAGERY")) {
		        						PRODUCT_SHOWBY_CATAGERY = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISASSIGN_USERLOC")) {
		        						ISASSIGN_USERLOC = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("SHOW_STOCKQTY_ONAPP")) {
		        						SHOW_STOCKQTY_ONAPP = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISSHOWPOSPRICEBYOUTLET")) {
		        						ISSHOWPOSPRICEBYOUTLET = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ALLOWPOSTRAN_LESSTHAN_AVBQTY")) {
		        						ALLOWPOSTRAN_LESSTHAN_AVBQTY = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISPOSRETURN_TRAN")) {
		        						ISPOSRETURN_TRAN = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISPOSVOID_TRAN")) {
		        						ISPOSVOID_TRAN = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("SHOW_POS_SUMMARY")) {
		        						SHOW_POS_SUMMARY = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISMANAGEWORKFLOW")) {
		        						ISMANAGEWORKFLOW = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ALLOWCATCH_ADVANCE_SEARCH")) {
		        						ALLOWCATCH_ADVANCE_SEARCH = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("SETCURRENTDATE_ADVANCE_SEARCH")) {
		        						SETCURRENTDATE_ADVANCE_SEARCH = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISAUTO_CONVERT_ESTPO")) {
		        						ISAUTO_CONVERT_ESTPO = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISPRODUCTMAXQTY")) {
		        						ISPRODUCTMAXQTY = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISAUTO_CONVERT_RECEIPTBILL")) {
		        						ISAUTO_CONVERT_RECEIPTBILL = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("SETCURRENTDATE_GOODSRECEIPT")) {
		        						SETCURRENTDATE_GOODSRECEIPT = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("SETCURRENTDATE_PICKANDISSUE")) {
		        						SETCURRENTDATE_PICKANDISSUE = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISAUTO_CONVERT_ISSUETOINVOICE")) {
		        						ISAUTO_CONVERT_ISSUETOINVOICE = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("SHOWITEM_AVGCOST")) {
		        						SHOWITEM_AVGCOST = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISAUTO_MINMAX_MULTIESTPO")) {
		        						ISAUTO_MINMAX_MULTIESTPO = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISAUTO_CONVERT_SOINVOICE")) {
		        						ISAUTO_CONVERT_SOINVOICE = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					} //resvi-29/10/21
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ENABLE_PAYROLL")) {
		        						ENABLEPAYROLL = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISREFERENCEINVOICE")) {
		        						ISREFERENCEINVOICE = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					} 
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISSUPPLIERMANDATORY")) {
		        						ISSUPPLIERMANDATORY = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					} 
									else if (item.getFieldName()
		        							.equalsIgnoreCase("ISPRICE_UPDATEONLY_IN_OWNOUTLET")) {
		        						ISPRICE_UPDATEONLY_IN_OWNOUTLET = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					} 
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ENABLE_POS")) {
		        						ENABLEPOS = StrUtils.fString((item.getString() != null) ? "1": "0").trim();
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISPOSTAXINCLUSIVE")) {
		        						ISPOSTAXINCLUSIVE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISSALESAPP_TAXINCLUSIVE")) {
		        						ISSALESAPP_TAXINCLUSIVE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ISTAXREGISTRED")) {
		        						ISTAXREG = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("RCBNO")) {
		        						RCBNO = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("TAXBY")) {
		        						TAXBY = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("TAXBYLABEL")) {
		        						TAXBYLABEL = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("TAXBYLABELORDERMANAGEMENT")) {
		        						TAXBYLABELORDERMANAGEMENT = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("REPROTSBASIS")) {
		        						REPROTSBASIS = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("FISCALYEAR")) {
		        						FISCALYEAR = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("PAYROLLYEAR")) {
		        						PAYROLLYEAR = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("NAME")) {
		        						NAME = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("DESGINATION")) {
		        						DESGINATION = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("TELNO")) {
		        						TELNO = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("HPNO")) {
		        						HPNO = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("FAX")) {
		        						FAX = StrUtils.fString(item.getString());
		        					}	
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("EMAIL")) {
		        						EMAIL = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("WEBSITE")) {
		        						WEBSITE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("FACEBOOK")) {
		        						FACEBOOK = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("TWITTER")) {
		        						TWITTER = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("LINKEDIN")) {
		        						LINKEDIN = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("SKYPE")) {
		        						SKYPE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("REGION")) {
		        						REGION = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("COUNTY")) {
		        						COUNTY = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ADD1")) {
		        						ADD1 = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ADD2")) {
		        						ADD2 = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ADD3")) {
		        						ADD3 = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ADD4")) {
		        						ADD4 = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("STATE")) {
		        						STATE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("ZIP")) {
		        						ZIP = StrUtils.fString(item.getString());
		        					}
		        					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("CompanyIndustry")) {
		        						COMP_INDUSTRY = StrUtils.fString(item.getString());
		        					}
		        					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("REMARKS")) {
		        						REMARKS = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("EMPLOYEEWORKINGMANDAYSBY")) {
		        						EMPLOYEEWORKINGMANDAYSBY = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Supplier")) {
		        						NOOFSUPPLIER = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Customer")) {
		        						NOOFCUSTOMER = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Employee")) {
		        						NOOFEMPLOYEE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_User")) {
		        						NOOFUSER = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Inventory")) {
		        						NOOFINVENTORY = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Location")) {
		        						NOOFLOCATION = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("companyregnumber")) {
		        						companyregnumber = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Order")) {
		        						NOOFORDER = StrUtils.fString(item.getString());
		        					}
		        					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Exp_Bill_Inv")) {
		        						NOOFEXPBILLINV = StrUtils.fString(item.getString());
		        					}
		        					
		        					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Payment")) {
		        						NOOFPAYMENT = StrUtils.fString(item.getString());
		        					}
		        					
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Journal")) {
		        						NOOFJOURNAL = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("No_of_Contra")) {
		        						NOOFCONTRA = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("shopify")) {
		        						SHOPIFY = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("lazada")) {
		        						LAZADA = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("shopee")) {
		        						SHOPEE = StrUtils.fString(item.getString());
		        					}
		        					else if (item.getFieldName()
		        							.equalsIgnoreCase("amazon")) {
		        						AMAZON = StrUtils.fString(item.getString());
		        					}
		        					
		        				}
		        				else if (!item.isFormField()
		        						&& (item.getName().length() > 0)) {
		        					
		        					
		        					if(item.getFieldName().equalsIgnoreCase("seal")) {
		        						String fileName = StrUtils.fString(item.getName()).trim();
		        						
		        						long size = item.getSize();
		        						String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        						if (!imageFormatsList.contains(extension)) {
			        						result = "Seal extension not valid";
			        						imageSizeflg = true;
			        					}
									/*
									 * if (size > 2040) // condtn checking Image size { result =
									 * "Seal Image size greater than 1 MB";
									 * 
									 * imageSizeflg = true;
									 * 
									 * }
									 */
		        						
		        						if(!sPlant.equalsIgnoreCase(""))
			        						PLANT=sPlant;
		        						SEALNAME = PLANT.toLowerCase()+fileName;
			        					fileLocation = DbBean.COMPANY_SEAL_PATH + "/"+ PLANT.toLowerCase() + fileName;
					         			filetempLocation = DbBean.COMPANY_SEAL_PATH + "/temp" + "/" + PLANT.toLowerCase() + fileName;
					         			
			        				 java.io.File path = new File(fileLocation);
			        					if (!path.exists()) {
			        						boolean status = path.mkdirs();
			        					}
			        					fileName = fileName.substring(fileName
			        							.lastIndexOf("\\") + 1);
			        					
			    						File uploadedFile = new File(fileLocation);
			    						if (uploadedFile.exists()) {
			    							 uploadedFile.delete();
			    							 }
			        					strpath = path + "/" + fileName;
			        					catlogpath = uploadedFile.getAbsolutePath();
			        					if (!imageSizeflg && !uploadedFile.exists())
			        						item.write(uploadedFile);

			        					// delete temp uploaded file
			        					File tempPath = new File(filetempLocation);
			        					if (tempPath.exists()) {
			        						File tempUploadedfile = new File(filetempLocation);
			        						
			        						if (tempUploadedfile.exists()) {
			        							tempUploadedfile.delete();
			        						}
			        					}
		        					}
		        					if(item.getFieldName().equalsIgnoreCase("APP_IMAGE_UPLOAD")) {
		        						String fileName = StrUtils.fString(item.getName()).trim();
		        						
		        						long size = item.getSize();
		        						String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        						if (!imageFormatsList.contains(extension)) {
			        						result = "Order App Image extension not valid";
			        						imageSizeflg = true;
			        					}
								
		        						
		        						if(!sPlant.equalsIgnoreCase(""))
			        						PLANT=sPlant;
		        						APPPATH = PLANT.toLowerCase()+fileName;
			        					fileLocation = DbBean.COMPANY_ORDER_APP_BACKGROUND_PATH + "/"+ PLANT.toLowerCase() + fileName;
					         			filetempLocation = DbBean.COMPANY_ORDER_APP_BACKGROUND_PATH + "/temp" + "/" + PLANT.toLowerCase() + fileName;
					         			
			        				 java.io.File path = new File(fileLocation);
			        					if (!path.exists()) {
			        						boolean status = path.mkdirs();
			        					}
			        					fileName = fileName.substring(fileName
			        							.lastIndexOf("\\") + 1);
			        					
			    						File uploadedFile = new File(fileLocation);
			    						if (uploadedFile.exists()) {
			    							 uploadedFile.delete();
			    							 }
			        					strpath = path + "/" + fileName;
			        					catlogpath = uploadedFile.getAbsolutePath();
			        					if (!imageSizeflg && !uploadedFile.exists())
			        						item.write(uploadedFile);

			        					// delete temp uploaded file
			        					File tempPath = new File(filetempLocation);
			        					if (tempPath.exists()) {
			        						File tempUploadedfile = new File(filetempLocation);
			        						
			        						if (tempUploadedfile.exists()) {
			        							tempUploadedfile.delete();
			        						}
			        					}
		        					}
		        					
		        					else if(item.getFieldName().equalsIgnoreCase("dsignature")) {

		        						String fileName = StrUtils.fString(item.getName()).trim();
		        						
		        						long size = item.getSize();
		        						String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        						if (!imageFormatsList.contains(extension)) {
			        						result = "Signature extension not valid";
			        						imageSizeflg = true;
			        					}
									/*
									 * if (size > 2040) // condtn checking Image size { result =
									 * "Signature Image size greater than 1 MB";
									 * 
									 * imageSizeflg = true;
									 * 
									 * }
									 */
		        						
		        						if(!sPlant.equalsIgnoreCase(""))
			        						PLANT=sPlant;
		        						SIGNATURENAME = PLANT.toLowerCase()+fileName;
			        					fileLocation = DbBean.COMPANY_SIGNATURE_PATH + "/"+ PLANT.toLowerCase() + fileName;
					         			filetempLocation = DbBean.COMPANY_SIGNATURE_PATH + "/temp" + "/" + PLANT.toLowerCase() + fileName;
					         			
			        				 java.io.File path = new File(fileLocation);
			        					if (!path.exists()) {
			        						boolean status = path.mkdirs();
			        					}
			        					fileName = fileName.substring(fileName
			        							.lastIndexOf("\\") + 1);
			        					
			    						File uploadedFile = new File(fileLocation);
			    						if (uploadedFile.exists()) {
			    							 uploadedFile.delete();
			    							 }
			        					strpath = path + "/" + fileName;
			        					catlogpath = uploadedFile.getAbsolutePath();
			        					if (!imageSizeflg && !uploadedFile.exists())
			        						item.write(uploadedFile);

			        					// delete temp uploaded file
			        					File tempPath = new File(filetempLocation);
			        					if (tempPath.exists()) {
			        						File tempUploadedfile = new File(filetempLocation);
			        						
			        						if (tempUploadedfile.exists()) {
			        							tempUploadedfile.delete();
			        						}
			        					}
		        					
		        					}else {
		        					
			        					if(sLogo.equalsIgnoreCase(""))
			        					{
			        					String fileName = StrUtils.fString(item.getName()).trim();
			        					sLogo=fileName;
			        					long size = item.getSize();
	
			        					size = size / 1024;
			        					// size = size / 1000;
			        					System.out.println("size of the Image imported :::"
			        							+ size);
			        					
			        					String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			        					System.out.println("Extensions:::::::" + extension);
			        					if (!imageFormatsList.contains(extension)) {
			        						result = "Image extension not valid";
			        						imageSizeflg = true;
			        					}
			        					
			        					//checking image size for 2MB
			        					if (size > 2040) // condtn checking Image size
			        					{
			        						result = "Catalog Image size greater than 1 MB";
	
			        						imageSizeflg = true;
	
			        					}
			        					if(!sPlant.equalsIgnoreCase(""))
			        						PLANT=sPlant;
			        					
			        					fileLocation = DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
					         			filetempLocation = DbBean.COMPANY_LOGO_PATH + "/temp" + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
					         			
			        				 java.io.File path = new File(fileLocation);
			        					if (!path.exists()) {
			        						boolean status = path.mkdirs();
			        					}
			        					fileName = fileName.substring(fileName
			        							.lastIndexOf("\\") + 1);
			        					
			    						File uploadedFile = new File(fileLocation);
			    						if (uploadedFile.exists()) {
			    							 uploadedFile.delete();
			    							 }
			        					strpath = path + "/" + fileName;
			        					catlogpath = uploadedFile.getAbsolutePath();
			        					if (!imageSizeflg && !uploadedFile.exists())
			        						item.write(uploadedFile);
	
			        					// delete temp uploaded file
			        					File tempPath = new File(filetempLocation);
			        					if (tempPath.exists()) {
			        						File tempUploadedfile = new File(filetempLocation);
			        						
			        						if (tempUploadedfile.exists()) {
			        							tempUploadedfile.delete();
			        						}
			        					}
			        					}
			        				
			        					String fileName = StrUtils.fString(item.getName()).trim();
			        					if(!fileName.equalsIgnoreCase(sLogo))
			        					{
			        					String fileLocationAtt = "C:/ATTACHMENTS/Company" + "/"+ PLANT;
			        					String filetempLocationAtt = "C:/ATTACHMENTS/Company" + "/temp" + "/"+ PLANT;
			        					
			        					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			        					
			        					File path = new File(fileLocationAtt);
			        					if (!path.exists()) {
			        						path.mkdirs();
			        					}
			        					
			        					//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			        					File uploadedFile = new File(path + "/" +fileName);
			        					if (uploadedFile.exists()) {
			        						uploadedFile.delete();
			        					}
			        					
			        					item.write(uploadedFile);
			        					
			        					// delete temp uploaded file
			        					File tempPath = new File(filetempLocationAtt);
			        					if (tempPath.exists()) {
			        						File tempUploadedfile = new File(tempPath + "/"+ fileName);
			        						if (tempUploadedfile.exists()) {
			        							tempUploadedfile.delete();
			        						}
			        					}
			        					Attachment = new Hashtable<String, String>();
			        					Attachment.put("PLANT", PLANT);
			        					Attachment.put("FILETYPE", item.getContentType());
			        					Attachment.put("FILENAME", fileName);
			        					Attachment.put("FILESIZE", String.valueOf(item.getSize()));
			        					Attachment.put("FILEPATH", fileLocationAtt);
			        					Attachment.put("CRAT",dateutils.getDateTime());
			        					Attachment.put("CRBY",sUserId);
			        					Attachment.put("UPAT",dateutils.getDateTime());
			        					AttachmentList.add(Attachment);
			        					}
		        					}
		        				}
		        			}
		        			if (imageSizeflg) {
		        				//res  = result;
		        				response.sendRedirect("jsp/PlantMst.jsp?result="+ result);
		        			}
		        			else{
		        				Hashtable htCond=new Hashtable();
		        				 htCond.put("PLANT",(String)PLANT);
		        				 List listQry = plantmstutil.getPlantMstDetails(PLANT);
		        				 for (int i =0; i<listQry.size(); i++){
		        				    Map map = (Map) listQry.get(i);
		        				    existingplnt  = (String) map.get("PLANT");
		        				    if(existingplnt.equalsIgnoreCase(sPlant))
		        				    {
		        				      flag=true;
		        				      //res="<font class = "+"mainred"+">Company"+ " "+ PLANT +" "+"Exists Already</font>";
		        				       result="Exists Already";	        					       
		        				    }
		        				}
		        				 Hashtable ht = new Hashtable();
		        				 if(flag==false)
		        				  {
		        					 if(!STARTDATE.equals("") || STARTDATE.equals(null))
		        						{
		        						    SDATE    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);
		        						}
		        						if(!EXPIRYDATE.equals("") || EXPIRYDATE.equals(null))
		        						{
		        						   EDATE    = EXPIRYDATE.substring(6)+"-"+ EXPIRYDATE.substring(3,5)+"-"+EXPIRYDATE.substring(0,2);
		        						}
		        						if(!FISCALYEAR.equals("") || FISCALYEAR.equals(null))
		        						{
		        							FYEAR    = FISCALYEAR.substring(6)+"-"+ FISCALYEAR.substring(3,5)+"-"+FISCALYEAR.substring(0,2);
		        						}
		        						if(!PAYROLLYEAR.equals("") || PAYROLLYEAR.equals(null))
		        						{
		        							PYEAR    = PAYROLLYEAR.substring(6)+"-"+ PAYROLLYEAR.substring(3,5)+"-"+PAYROLLYEAR.substring(0,2);
		        						}
		        						
		        					    ht.put("PLANT",PLANT);
		        					    ht.put("PLNTDESC",PLNTDESC);
		        					    ht.put("REPROTSBASIS",REPROTSBASIS);
		        					    ht.put(IDBConstants.ISTAXREG,ISTAXREG);
		        					    ht.put(IDBConstants.COMP_INDUSTRY,COMP_INDUSTRY);
		        					    ht.put("STARTDATE",SDATE );
		        					    ht.put("EXPIRYDATE",EDATE );
		        					    ht.put("ACTEXPIRYDATE",ACTUALEXPIRYDATE);
		        					    ht.put("NAME",NAME);
		        					    ht.put("DESGINATION",DESGINATION);
		        					    ht.put("TELNO",TELNO);
		        					    ht.put("HPNO",HPNO);
		        					    ht.put("EMAIL",EMAIL);
		        					    ht.put("ADD1",ADD1);
		        					    ht.put("ADD2",ADD2);
		        					    ht.put("ADD3",ADD3);
		        					    ht.put("ADD4",ADD4);
		        					    ht.put(IDBConstants.RCBNO,RCBNO);
		        					    if(COUNTY.equalsIgnoreCase("Select Country"))
		        					    	COUNTY="";
		        					    ht.put(IDBConstants.COUNTY,COUNTY);
		        					    ht.put(IDBConstants.ZIP,ZIP);  
		        					    ht.put("USERFLD1",REMARKS);
		        					    ht.put("USERFLD2",FAX);ht.put("AUTHSTAT","0");
		        					    ht.put("CRBY",sUserId);
		        					    ht.put("CRAT", new DateUtils().getDateTime());
		        					    ht.put("SALES_CHARGE_BY",SALES);
		        				        ht.put("SALES_PERCENT",SALESPERCENT);
		        				        ht.put("SALES_FR_DOLLARS",SDOLLARFRATE);
		        				        ht.put("SALES_FR_CENTS",SCENTSFRATE);
		        				        ht.put("ENQUIRY_FR_DOLLARS",EDOLLARFRATE);
		        				        ht.put("ENQUIRY_FR_CENTS",ECENTSFRATE);
		        				        ht.put("NUMBER_OF_CATALOGS",NOOFCATALOGS);
		        				        ht.put("NUMBER_OF_SIGNATURES",NOOFSIGNATURES);   
		        				        ht.put(IDBConstants.BASE_CURRENCY,currencyCode);
		        				        ht.put(IDBConstants.TAXBY,TAXBY);
		        				        ht.put(IDBConstants.TAXBYLABEL,TAXBYLABEL);
		        				        if(STATE.equalsIgnoreCase("Select State"))
		        				        	STATE="";
		        				        ht.put(IDBConstants.STATE,STATE);
		        				        ht.put("ENABLE_INVENTORY", ENABLEINVENTORY);
		        				        ht.put("ENABLE_ACCOUNTING", ENABLEACCOUNTING);
		        				        ht.put(IDBConstants.OWNER_APP, OWNERAPP);
		        				        ht.put(IDBConstants.CUSTOMER_APP, CUSTOMERAPP);
		        				        ht.put(IDBConstants.MANAGER_APP, MANAGERAPP);
		        				        ht.put(IDBConstants.STORE_APP, STOREAPP);
		        				        ht.put(IDBConstants.RIDE_APP, RIDEAPP);
		        				        ht.put("NUMBEROFDECIMAL", DECIMALPRECISION);
		        				        ht.put("ENABLE_PAYROLL", ENABLEPAYROLL);
		        				        ht.put("ENABLE_POS", ENABLEPOS);
		        				        ht.put("FISCAL_YEAR", FYEAR);
		        				        ht.put("PAYROLL_YEAR", PYEAR);
		        				        if(REGION.equalsIgnoreCase("Select Region"))
		        				        	REGION="";
		        				        ht.put("REGION", REGION);
		        				        ht.put("TAXBYLABELORDERMANAGEMENT", TAXBYLABELORDERMANAGEMENT);
		        				        ht.put(IConstants.WEBSITE,WEBSITE);
		        				        ht.put(IConstants.FACEBOOK,FACEBOOK);
		        				        ht.put(IConstants.TWITTER,TWITTER);
		        				        ht.put(IConstants.LINKEDIN,LINKEDIN);
		        				        ht.put(IConstants.SKYPE,SKYPE);
		        				        ht.put("EMPLOYEEWORKINGMANDAYSBY",EMPLOYEEWORKINGMANDAYSBY);
		        				        ht.put("SEALNAME", SEALNAME);
		        				        ht.put("APPPATH", APPPATH);
		        				        ht.put("SIGNATURENAME", SIGNATURENAME);
		        				        
		        				        ht.put("NUMBER_OF_SUPPLIER", NOOFSUPPLIER);
		        				        ht.put("NUMBER_OF_CUSTOMER", NOOFCUSTOMER);
		        				        ht.put("NUMBER_OF_EMPLOYEE", NOOFEMPLOYEE);
		        				        ht.put("NUMBER_OF_USER", NOOFUSER);
		        				        ht.put("NUMBER_OF_INVENTORY", NOOFINVENTORY);
		        				        ht.put("NUMBER_OF_LOCATION", NOOFLOCATION);
		        				        ht.put("PRODUCT_SHOWBY_CATAGERY", PRODUCT_SHOWBY_CATAGERY);
		        				        ht.put("ISASSIGN_USERLOC", ISASSIGN_USERLOC);
		        				        ht.put("SHOW_STOCKQTY_ONAPP", SHOW_STOCKQTY_ONAPP);
		        				        ht.put("ISSHOWPOSPRICEBYOUTLET", ISSHOWPOSPRICEBYOUTLET);
		        				        ht.put("ALLOWPOSTRAN_LESSTHAN_AVBQTY", ALLOWPOSTRAN_LESSTHAN_AVBQTY);
		        				        ht.put("ISPOSRETURN_TRAN", ISPOSRETURN_TRAN);
		        				        ht.put("ISPOSVOID_TRAN", ISPOSVOID_TRAN);
		        				        ht.put("SHOW_POS_SUMMARY", SHOW_POS_SUMMARY);
		        				        ht.put("ISMANAGEWORKFLOW", ISMANAGEWORKFLOW);
		        				        ht.put("ALLOWCATCH_ADVANCE_SEARCH", ALLOWCATCH_ADVANCE_SEARCH);
		        				        ht.put("SETCURRENTDATE_ADVANCE_SEARCH", SETCURRENTDATE_ADVANCE_SEARCH);
		        				        ht.put("ISAUTO_CONVERT_ESTPO", ISAUTO_CONVERT_ESTPO);
		        				        ht.put("ISPRODUCTMAXQTY", ISPRODUCTMAXQTY);
		        				        ht.put("ISAUTO_CONVERT_RECEIPTBILL", ISAUTO_CONVERT_RECEIPTBILL);
		        				        ht.put("SETCURRENTDATE_GOODSRECEIPT", SETCURRENTDATE_GOODSRECEIPT);
		        				        ht.put("SETCURRENTDATE_PICKANDISSUE", SETCURRENTDATE_PICKANDISSUE);
		        				        ht.put("ISAUTO_CONVERT_ISSUETOINVOICE", ISAUTO_CONVERT_ISSUETOINVOICE);
		        				        ht.put("SHOWITEM_AVGCOST", SHOWITEM_AVGCOST);
		        				        ht.put("ISAUTO_MINMAX_MULTIESTPO", ISAUTO_MINMAX_MULTIESTPO);
		        				        ht.put("ISAUTO_CONVERT_SOINVOICE", ISAUTO_CONVERT_SOINVOICE);//Resvi
		        				        ht.put("ISPOSTAXINCLUSIVE", ISPOSTAXINCLUSIVE);
		        				        ht.put("ISSALESAPP_TAXINCLUSIVE", ISSALESAPP_TAXINCLUSIVE);
		        				        ht.put("companyregnumber", companyregnumber);
		        				        ht.put("NUMBER_OF_ORDER", NOOFORDER);
		        				        ht.put("NUMBER_OF_EBIQI", NOOFEXPBILLINV);
		        				        ht.put("NUMBER_OF_PAYMENT", NOOFPAYMENT);
		        				        ht.put("NUMBER_OF_JOURNAL", NOOFJOURNAL);
		        				        ht.put("NUMBER_OF_CONTRA", NOOFCONTRA);
		        				        ht.put("ISREFERENCEINVOICE", ISREFERENCEINVOICE);
		        				        ht.put("ISSUPPLIERMANDATORY", ISSUPPLIERMANDATORY);
										ht.put("ISPRICE_UPDATEONLY_IN_OWNOUTLET", ISPRICE_UPDATEONLY_IN_OWNOUTLET);
		        				        ht.put("shopify", String.valueOf("on".equals(SHOPIFY) ? 1 : 0));
		        				        ht.put("lazada", String.valueOf("on".equals(LAZADA) ? 1 : 0));
		        				        ht.put("shopee", String.valueOf("on".equals(SHOPEE) ? 1 : 0));
		        				        ht.put("amazon", String.valueOf("on".equals(AMAZON) ? 1 : 0));

		        					    boolean itemInserted = plantmstutil.insertPlantMst(ht,sUserId);
		        					    if(itemInserted) 
		        					    {

		        					    	int plantId= plantmstutil.getPlantMstMaxId();
		        					    	
		        					    	int attchSize = AttachmentList.size();
		        							for(int i =0 ; i < attchSize ; i++){
		        								Attachment = new Hashtable<String, String>();
		        								Attachment = AttachmentList.get(i);
		        								Attachment.put("PLANTID", Integer.toString(plantId));
		        								AttachmentInfoList.add(Attachment);
		        							}
		        							if(AttachmentInfoList.size() > 0)
		        								itemInserted = plantmstutil.addAttachments(AttachmentInfoList, PLANT);
		        							
		        							//res = "<font class = "+"maingreen"+">Company  "+ " "+ PLANT +" "+"Created Successfully</font>";
		        							result="Created Successfully";		        								
		        					    }
		        					    else 
		        					     {
		        					         //res = "<font class = "+"black"+">Failed to add Company </font>";
		        					    	result="Failed to add Company";
		        					                        
		        					     }
		        					 
		        				  }				 
		        			}
		        		}
		        		if(result.equalsIgnoreCase("Created Successfully"))
		        			response.sendRedirect("jsp/PlantMst.jsp?result="+ result+"&SPLANT="+PLANT+"&SPLNTDESC="+PLNTDESC);
		        		else
		        			response.sendRedirect("jsp/PlantMst.jsp?result="+ result);	
					    }catch (Exception e) {
					    		result="Failed to add Company";
							 response.sendRedirect("jsp/PlantMst.jsp?result="+result+" - "+ e.toString());	
						}
		          	}else  if (action.equals("downloadCompAttachmentById")) {
		          		
		          		System.out.println("Attachments by ID");
		    			int ID=Integer.parseInt(request.getParameter("attachid"));
		    			FileHandling fileHandling=new FileHandling(); 
		    			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		    			List billAttachment = null;
		    			try {
		    				Hashtable ht1 = new Hashtable();
		    				ht1.put("ID", String.valueOf(ID));
		    				ht1.put("PLANT", plant);
		    				billAttachment = _PlantMstDAO.getCompAttachByHrdId(plant,String.valueOf(ID));
		    				Map billAttach=(Map)billAttachment.get(0);
		    				String filePath=(String) billAttach.get("FilePath");
		    				String fileType=(String) billAttach.get("FileType");
		    				String fileName=(String) billAttach.get("FileName");
		    				fileHandling.fileDownload(filePath, fileName, fileType, response);
		    			} catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    			
		          	}else  if (action.equals("removeCompAttachmentById")) {
		          		
		          		System.out.println("Remove Attachments by ID");
		    			int ID=Integer.parseInt(request.getParameter("removeid"));
		    			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		    			try {
		    				Hashtable ht1 = new Hashtable();
		    				ht1.put("ID", String.valueOf(ID));
		    				ht1.put("PLANT", plant);
		    			 boolean flag= _PlantMstDAO.deleteCompAttachByPrimId(ht1);
		    				
		    			} catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    			response.getWriter().write("Deleted");
		          	}else  if (action.equals("comp_attach_edit")) {
		          		
		          		PlantMstUtil plantmstutil = new PlantMstUtil();
		          		JSONObject resultObj = new JSONObject();
		        		JSONObject resultJSON = new JSONObject();
		        		String type = StrUtils.fString(request.getParameter("Type"));
		          		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		          		List<Hashtable<String,String>> AttachmentList = null;
		        		List<Hashtable<String,String>> AttachmentInfoList = null;
		          		Hashtable<String,String> Attachment = null;
		          		boolean itemInserted=false;
		        		if (isMultipart) {
		        			try {
		        				String result = "";
		        			AttachmentList = new ArrayList<Hashtable<String,String>>();
		        			AttachmentInfoList = new ArrayList<Hashtable<String,String>>();
		        			
		    				FileItemFactory factory = new DiskFileItemFactory();
		    				ServletFileUpload upload = new ServletFileUpload(factory);

		    				List items = upload.parseRequest(request);
		    				Iterator iterator = items.iterator();
		    				
		    				String PLANTID = "";
		    				while (iterator.hasNext()) {
		    					FileItem item = (FileItem) iterator.next();
		    					if (item.isFormField()) {

		    						if (item.getFieldName().equalsIgnoreCase("ID")) {
		    							PLANTID = StrUtils.fString(item.getString());
		    						}
		    					} else if (!item.isFormField() && (item.getName().length() > 0)) {
		    						String fileName = StrUtils.fString(item.getName()).trim();
		        					/*if(!fileName.equalsIgnoreCase(sLogo))
		        					{*/
		        					String fileLocationAtt = "C:/ATTACHMENTS/Company" + "/"+ plant;
		        					String filetempLocationAtt = "C:/ATTACHMENTS/Company" + "/temp" + "/"+ plant;
		        					
		        					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        					
		        					File path = new File(fileLocationAtt);
		        					if (!path.exists()) {
		        						path.mkdirs();
		        					}
		        					
		        					//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        					File uploadedFile = new File(path + "/" +fileName);
		        					if (uploadedFile.exists()) {
		        						uploadedFile.delete();
		        					}
		        					
		        					item.write(uploadedFile);
		        					
		        					// delete temp uploaded file
		        					File tempPath = new File(filetempLocationAtt);
		        					if (tempPath.exists()) {
		        						File tempUploadedfile = new File(tempPath + "/"+ fileName);
		        						if (tempUploadedfile.exists()) {
		        							tempUploadedfile.delete();
		        						}
		        					}
		        					Attachment = new Hashtable<String, String>();
		        					Attachment.put("PLANT", plant);
		        					Attachment.put("FILETYPE", item.getContentType());
		        					Attachment.put("FILENAME", fileName);
		        					Attachment.put("FILESIZE", String.valueOf(item.getSize()));
		        					Attachment.put("FILEPATH", fileLocationAtt);
		        					Attachment.put("CRAT",dateutils.getDateTime());
		        					Attachment.put("CRBY",userName);
		        					Attachment.put("UPAT",dateutils.getDateTime());
		        					AttachmentList.add(Attachment);
		        					//}
		    					}
		        		}
		    				
		    				
					    	int attchSize = AttachmentList.size();
							for(int i =0 ; i < attchSize ; i++){
								Attachment = new Hashtable<String, String>();
								Attachment = AttachmentList.get(i);
								Attachment.put("PLANTID", PLANTID);
								AttachmentInfoList.add(Attachment);
							}
							if(AttachmentInfoList.size() > 0)
								itemInserted = plantmstutil.addAttachments(AttachmentInfoList, plant);
							
							if (itemInserted) {

								result = "<font color=\"green\"> Attachment Updated successfully</font>";
								resultObj.put("product", PLANTID);
								resultObj.put("message", result);
							} else {
								result = "<font color=\"red\"> Failed to Update Attachment </font>";
								resultObj.put("product", PLANTID);
								resultObj.put("message", result);
							}
							
		        			} catch (Exception e) {

		        			}
		        			resultJSON.put("result", resultObj);
		        			
		        		}
		        		
		        		jsonObjectResult = resultJSON;
		                response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
		                response.getWriter().write(jsonObjectResult.toString());
		                response.getWriter().flush();
		                response.getWriter().close();
			        }else  if (action.equals("GET_PAYMENT_TYPE_LIST")) {
			        	  	jsonObjectResult = this.getPaymentTypeMstList(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_TRANSPORT_LIST")) {
		          		jsonObjectResult = this.getTransportList(request);
		          		response.setContentType("application/json");
		          		response.setCharacterEncoding("UTF-8");
		          		response.getWriter().write(jsonObjectResult.toString());
		          		response.getWriter().flush();
		          		response.getWriter().close();
		          	}else  if (action.equals("GET_SUPPLIER_LIST")) {
		          		jsonObjectResult = this.getSupplierList(request);
		          		response.setContentType("application/json");
		          		response.setCharacterEncoding("UTF-8");
		          		response.getWriter().write(jsonObjectResult.toString());
		          		response.getWriter().flush();
		          		response.getWriter().close();
		          	}
		          	else  if (action.equals("CREATE_PAYMENT_TYPE")) {
			        	  	jsonObjectResult = this.createpaymenttype(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			         //RESVI ORDER TYPE			          
			          }else  if (action.equals("CREATE_TRANSPORTMODE_TYPE_MODAL")) {
			        	  	jsonObjectResult = this.CreateTransportModeModal(request, response);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			         //RESVI PRODUCT DEPARTMENT
			          }else  if (action.equals("CREATE_CLEARANCE_TYPE_MODAL")) {
				        	jsonObjectResult = this.CreateClearanceTypetModeModal(request, response);
				            response.setContentType("application/json");
				            response.setCharacterEncoding("UTF-8");
				            response.getWriter().write(jsonObjectResult.toString());
		                    response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_CLEAR_AGENT_MODAL")) {
				        	jsonObjectResult = this.CreateClearingAgentModeModal(request, response);
				            response.setContentType("application/json");
				            response.setCharacterEncoding("UTF-8");
				            response.getWriter().write(jsonObjectResult.toString());
		                    response.getWriter().flush();
			                response.getWriter().close();         
			          }else  if (action.equals("CREATE_SUPPLIER_TYPE_MODAL")) {
			        	  	jsonObjectResult = this.CreateSuppliertypeModal(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_CUSTOMER_TYPE")) {
			        	  	jsonObjectResult = this.CreateCustomertypeModal(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
		          }else  if (action.equals("CREATE_PRODUCT_DEPARTMENT")) {
				        	  	jsonObjectResult = this.createproductdepartment(request);
				                response.setContentType("application/json");
				                response.setCharacterEncoding("UTF-8");
				                response.getWriter().write(jsonObjectResult.toString());
				                response.getWriter().flush();
				                response.getWriter().close();
			          }else  if (action.equals("CREATE_PRODUCT_CATEGORY")) {
			        	  	jsonObjectResult = this.createproductcategory(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_PRODUCT_LOCATION")) {
			        	  jsonObjectResult = this.createproductlocation(request);
			        	  response.setContentType("application/json");
			        	  response.setCharacterEncoding("UTF-8");
			        	  response.getWriter().write(jsonObjectResult.toString());
			        	  response.getWriter().flush();
			        	  response.getWriter().close();
			          }else  if (action.equals("CREATE_PRODUCT_SUB_CATEGORY")) {
			        	  	jsonObjectResult = this.createproductsubcategory(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_PRODUCT_BRAND")) {
			        	  	jsonObjectResult = this.createproductbrand(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_PRODUCT_HSCODE")) {
			        	  	jsonObjectResult = this.createproducthscode(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_PRODUCT_COO")) {
			        	  	jsonObjectResult = this.createproductcoo(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_PRODUCT_UOM")) {
			        	  	jsonObjectResult = this.createproductuom(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_ORDER_TYPE")) {
			        	  	jsonObjectResult = this.createordertype(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
//			                //RESVI LOCATION TYPE			          
			          }else  if (action.equals("CREATE_LOCATION_TYPE")) {
			        	  	jsonObjectResult = this.createlocationtype(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_LOCATION_TYPE_TWO")) {//RESVI LOCATION TYPE	2
			        	  	jsonObjectResult = this.createlocationtype2(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("CREATE_LOCATION_TYPE_THREE")) {
			        	  	jsonObjectResult = this.createlocationtype3(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          } else  if (action.equals("GET_SHIPMENT_REF_FOR_BILL")) {
			        	  	jsonObjectResult = this.getShipmentRefForBill(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_EXPENSE_DETAIL_FOR_BILL")) {
			        	  	jsonObjectResult = this.getExpenseDetailForBill(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_EXPENSE_DETAIL_FOR_INVENTORY_BILL")) {
			        	  	jsonObjectResult = this.getExpenseDetailForInventoryBill(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_CREDITNOTE_SEQUENCE")) {
				            jsonObjectResult = this.getNextCreditNoteSequence(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_BILLNO_SEQUENCE")) {
				            jsonObjectResult = this.getNextBillSequence(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_CUSTOMER_CREDITNOTE_SEQUENCE")) {
				            jsonObjectResult = this.getNextCustomerCreditNoteSequence(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_SALES_LOCATION_DATA")) {
				            jsonObjectResult = this.getSalesLocationData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }
			          else  if (action.equals("GET_LOCATION_BY_COUNTRY")) {
				            jsonObjectResult = this.getLocationByCountry(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_TAXTREATMENT_DATA")) {
					            jsonObjectResult = this.getTaxTreatmentData(request);
				                response.setContentType("application/json");
				                response.setCharacterEncoding("UTF-8");
				                response.getWriter().write(jsonObjectResult.toString());
				                response.getWriter().flush();
				                response.getWriter().close();
				      }else  if (action.equals("GET_STATE_DATA")) {
				            jsonObjectResult = this.getStateData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_STATE_DATA_POPUP")) {
				            jsonObjectResult = this.getStateDataPopup(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			             }else  if (action.equals("GET_COO_CURRENCY_CODE")) {
				    	  jsonObjectResult = this.getcooCurrency(request);
				    	  response.setContentType("application/json");
				    	  response.setCharacterEncoding("UTF-8");
				    	  response.getWriter().write(jsonObjectResult.toString());
				    	  response.getWriter().flush();
				    	  response.getWriter().close();
			             }else  if (action.equals("GET_PRODUCT_ASSIGNED_SUPPLIER")) {
			            	 jsonObjectResult = this.getProductAssignedSupplier(request);
			            	 response.setContentType("application/json");
			            	 response.setCharacterEncoding("UTF-8");
			            	 response.getWriter().write(jsonObjectResult.toString());
			            	 response.getWriter().flush();
			            	 response.getWriter().close();
				      }else  if (action.equals("GET_LOCATION_DATA")) {
				            jsonObjectResult = this.getLocationData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_LOCATION_TYPE_DATA")) {
				            jsonObjectResult = this.getLocationTypeData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_UOM_DATA")) {
				            jsonObjectResult = this.getUomData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_CURRENCY_DATA")) {
				            jsonObjectResult = this.getCurrencyData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();      
				      }else  if (action.equals("GET_FOOTER_DATA")) {
				            jsonObjectResult = this.getFooterData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();      
				      }else  if (action.equals("GET_PRD_CLS_DATA")) {
				            jsonObjectResult = this.getPRD_CLS_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();			                
				      }else  if (action.equals("GET_PRD_DEPT_DATA")) { //Resvi
				            jsonObjectResult = this.getPRD_DEPT_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();			                
				      }else  if (action.equals("GET_SUPPLIER_TYPE_DATA")) { 
				            jsonObjectResult = this.getSUPPLIERTYPE_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();			                
				      }else  if (action.equals("GET_CUSTOMER_TYPE_DATA")) { 
				            jsonObjectResult = this.getCUSTOMER_TYPE_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();			                
				      }else  if (action.equals("GET_DEPT_DISPLAY_DATA")) { //Resvi
				            jsonObjectResult = this.getDEPT_DISPLAY_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();			                
				      }
				      else  if (action.equals("GET_PRD_TYPE_DATA")) {
				            jsonObjectResult = this.getPRD_TYPE_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_PRD_BRAND_DATA")) {
				            jsonObjectResult = this.getPRD_BRAND_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_HSCODE_DATA")) {
				            jsonObjectResult = this.getHSCODE_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_COO_DATA")) {
				            jsonObjectResult = this.getCOO_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_CUSTOMERTYPE_DATA")) {
				            jsonObjectResult = this.getCUSTOMERTYPE_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();      
				      }else  if (action.equals("GET_BANK_DATA")) {
				            jsonObjectResult = this.getBankData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_BANK_DATA_ALL")) {
				            jsonObjectResult = this.getBankData_All(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
				      }else  if (action.equals("GET_COUNTRY_DATA")) {
				            jsonObjectResult = this.getCountryData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_BATCH_DATA")) {
				            jsonObjectResult = this.getBatchData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_BATCH_DATA_STK")) {
			        	  jsonObjectResult = this.getBatchDataStockTake(request);
			        	  response.setContentType("application/json");
			        	  response.setCharacterEncoding("UTF-8");
			        	  response.getWriter().write(jsonObjectResult.toString());
			        	  response.getWriter().flush();
			        	  response.getWriter().close();
			          }else  if (action.equals("GET_STOCK_BATCH_DATA")) { /* Author: Azees  Create date: July 3,2021  Description: Stock Validation */
			        	  jsonObjectResult = this.getLocBatchStock(request);
			        	  response.setContentType("application/json");
			        	  response.setCharacterEncoding("UTF-8");
			        	  response.getWriter().write(jsonObjectResult.toString());
			        	  response.getWriter().flush();
			        	  response.getWriter().close();
			          }else  if (action.equals("GET_KITING_BATCH_DATA")) {
				            jsonObjectResult = this.getKittingParentBatch(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_KITING_CHILD_BATCHDETAILS_DATA")) {
				            jsonObjectResult = this.getKittingChildDetailsBatch(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();      
			          }else  if (action.equals("GET_KITING_CHILD_BATCH_DATA")) {
				            jsonObjectResult = this.getKittingChildBatch(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();      
			          }
			          else  if (action.equals("GET_INCOTERM_DATA")) {
				            jsonObjectResult = this.getIncotermData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }	
			          else  if (action.equals("GET_ITEM_MODEL_DATA")) {
				            jsonObjectResult = this.getItemModelData(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_ORDERTYPE_DATA")) {
				            jsonObjectResult = this.getORDERTYPE_Data(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }else  if (action.equals("GET_SHIPPING_CODE_SEQUENCE")) {
				            jsonObjectResult = this.getShipRefSequence(request);
			                response.setContentType("application/json");
			                response.setCharacterEncoding("UTF-8");
			                response.getWriter().write(jsonObjectResult.toString());
			                response.getWriter().flush();
			                response.getWriter().close();
			          }
			
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ ex.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}
	
	private JSONObject getCustomerDetails(HttpServletRequest request) {
		String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		String CUSTOMERCODE= StrUtils.fString(request.getParameter("CUSTOMERCODE"));
		JSONObject resultJson = new JSONObject();
		try {
			
			CustUtil custUtil = new CustUtil();
			ArrayList arrCust = custUtil.getCustomerDetails(CUSTOMERCODE,
					PLANT);
			resultJson.put("ERROR_CODE", "100");
			resultJson.put("CUSTOMER_EMAIL", arrCust.get(14));
		}catch(Exception e) {
			resultJson.put("ERROR_MESSAGE", e.getMessage());
			resultJson.put("ERROR_CODE", "98");
		}
		return resultJson;
	}
	private String addfooter(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String  PLANT="",Footer="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				Footer = StrUtils.fString(request.getParameter("FOOTER"));
				if (!_MasterUtil.isExistsFOOTER(Footer, PLANT)) // if the Footer exists already 24.10.18 by Azees
				{
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.FOOTER,Footer);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddFooter(ht);
											
			if(insertflag){
				resultJson.put("status", "100");
				msg = "<font class='maingreen'>Footer Added Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Footer</font>";
					
				}
				}
				else
				{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Footer Exists already</font>";
				}
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}
	
	
	private JSONObject getViewFooterDetailsEdit(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getFooterList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String footer = StrUtils.fString((String)lineArr.get("FOOTER")) ;
                           	chkString =autoid+","+footer;
                               result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +autoid+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + footer + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("FOOTERMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("footermaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("footermaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	
	private JSONObject getFooterDetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
	    //MasterDAO _MasterDAO = new MasterDAO();
      try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getFooterList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        String footer = StrUtils.fString((String)lineArr.get("FOOTER")) ;
	                        chkString =autoid+","+footer;
	                       	result += "<tr valign=\"middle\">"  
	                            		//+ "<td width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
	                            		+ "<td width='5%' align = center>"  + id + "</td>"
	                            		+ "<td width='75%' align = left>"  + footer + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("FOOTERMASTERDATA", result);
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("footermaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("footermaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	
	private JSONObject getViewFooterDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getFooterList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String footer = StrUtils.fString((String)lineArr.get("FOOTER")) ;
                           	chkString =autoid+","+footer;
                               result += "<tr valign=\"middle\">"  
                            		//+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + footer + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("FOOTERMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("footermaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("footermaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	private String DeleteFooter(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String FOOTER = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					String item = chkd[i];
					//String itemArray[] = item.split(",");
					//ID = itemArray[0];
					//FOOTER = itemArray[1];
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",item);
					//ht.put(IDBConstants.FOOTER,FOOTER);
				
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_FOOTER);
					//htm.put("RECID", "");
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",FOOTER);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deleteFooter(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	    
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Footer Deleted Successfully");
			response.sendRedirect("../footer/edit?action=result&FOOTER="+FOOTER);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Footer:"+FOOTER);
			response.sendRedirect("../footer/edit?action=resulterror&FOOTER="+FOOTER);
		}
	

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../footer/edit?action=catchrerror&FOOTER="+FOOTER);
			throw e;
		}

		return msg;
	}
	//imtiziaf start customer type
	private JSONObject getViewCustomerTypeDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CustUtil _CustUtil = new CustUtil();
		try {
		String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		String CUSTOMERTYPEID= StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
		request.getSession().setAttribute("RESULT","");
		boolean mesflag = false;
		ArrayList movQryList = _CustUtil.getCustTypeList(CUSTOMERTYPEID,PLANT,"");

		if (movQryList.size() > 0) {
		for(int i =0; i<movQryList.size(); i++) {
		Map arrCustLine = (Map)movQryList.get(i);
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("CUSTOMER_TYPE_ID", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
		resultJsonInt.put("CUSTOMER_TYPE_DESC", (String)arrCustLine.get("CUSTOMER_TYPE_DESC"));
		resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
		
		jsonArray.add(resultJsonInt);
		}
		resultJson.put("CUSTOMERTYPELIST", jsonArray);
        JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);
		}else {
			JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
            resultJsonInt.put("ERROR_CODE", "99");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("errors", jsonArrayErr);  
		}

		} catch (Exception e) {
		resultJson.put("SEARCH_DATA", "");
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
		resultJsonInt.put("ERROR_CODE", "98");
		jsonArrayErr.add(resultJsonInt);
		resultJson.put("ERROR", jsonArrayErr);
		jsonArray.add("");
		resultJson.put("CUSTOMERTYPELIST", jsonArray);
		}
		return resultJson;
		}
	//imtiziaf end customer type
	
	//VICKY start clearance type
		private JSONObject getViewClearanceTypeDetails(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			ClearanceUtil _ClearanceUtil = new ClearanceUtil();
			try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String CLEARANCETYPEID= StrUtils.fString(request.getParameter("CLEARANCETYPE"));
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList movQryList = _ClearanceUtil.getClearanceTypeList(CLEARANCETYPEID,PLANT,"");

			if (movQryList.size() > 0) {
			for(int i =0; i<movQryList.size(); i++) {
			Map arrCustLine = (Map)movQryList.get(i);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("CLEARANCETYPE", (String)arrCustLine.get("CLEARANCETYPE"));
			resultJsonInt.put("TYPE", (String)arrCustLine.get("TYPE"));
			jsonArray.add(resultJsonInt);
			}
			resultJson.put("CLEARANCETYPELIST", jsonArray);
	        JSONObject resultJsonInt = new JSONObject();
	        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	        resultJsonInt.put("ERROR_CODE", "100");
	        jsonArrayErr.add(resultJsonInt);
	        resultJson.put("errors", jsonArrayErr);
			}else {
				JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	            resultJsonInt.put("ERROR_CODE", "99");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);  
			}

			} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("CLEARANCETYPELIST", jsonArray);
			}
			return resultJson;
			}
		//vicky end clearance type
	
	//Navas start GST
			private JSONObject queryGstType(HttpServletRequest request) {
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONArray jsonArrayErr = new JSONArray();
				CustUtil custUtils = new CustUtil(); 
				GstTypeDAO gstTypeDAO = new GstTypeDAO();
				StrUtils strUtils = new StrUtils();
				try {
				String GST = strUtils.fString(request.getParameter("GST"));
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));			
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				List  locQryList = gstTypeDAO.queryGstType(GST,PLANT,"");
				if (locQryList.size() > 0) {
				for(int i =0; i<locQryList.size(); i++) {
				Vector arrCustLine = (Vector) locQryList.get(i);
				JSONObject resultJsonInt = new JSONObject();			
				resultJsonInt.put("GST", (String)arrCustLine.get(0));
				resultJsonInt.put("GSTDESC", (String)arrCustLine.get(1));
				resultJsonInt.put("GSTPERCENTAGE", (String)arrCustLine.get(2));
				resultJsonInt.put("REMARKS", (String)arrCustLine.get(3));
				jsonArray.add(resultJsonInt);
				}
				resultJson.put("CUSTOMERTYPELIST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
				}else {
					JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);  
				}

				} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}
				return resultJson;
				}
			//navas end GST
			
			//Navas start reasoncode
				private JSONObject getReasonMstDetails(HttpServletRequest request) {
					JSONObject resultJson = new JSONObject();
					JSONArray jsonArray = new JSONArray();
					JSONArray jsonArrayErr = new JSONArray();
					RsnMstUtil _rsnUtil = new RsnMstUtil();
					StrUtils strUtils = new StrUtils();
					Hashtable ht = new Hashtable();
					try {
					String PLANT= StrUtils.fString(request.getParameter("PLANT"));
					String RSN= StrUtils.fString(request.getParameter("REASONCODE"));
//					String RSN = strUtils.fString(request.getParameter("REASONCODE"));
					request.getSession().setAttribute("RESULT","");
					if (strUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
					boolean mesflag = false;					
					ArrayList 	locQryList = _rsnUtil.getReasonMstDetails("", PLANT, RSN);					
					if (locQryList.size() > 0) {						
					for(int i =0; i<locQryList.size(); i++) {						
					Map arrCustLine = (Map)locQryList.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("rsncode", (String)arrCustLine.get("rsncode"));
					resultJsonInt.put("rsndesc", (String)arrCustLine.get("rsndesc"));
					resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
					jsonArray.add(resultJsonInt);
					}
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
					}else {
						JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
		                resultJsonInt.put("ERROR_CODE", "99");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);  
					}

					} catch (Exception e) {
					resultJson.put("SEARCH_DATA", "");
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
					}
					return resultJson;
					}
				//Navas end reasoncode
				
				//Navas start ordertype
					private JSONObject getOrderTypeListDetails(HttpServletRequest request) {
						JSONObject resultJson = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						JSONArray jsonArrayErr = new JSONArray();
						CustUtil custUtils = new CustUtil();
						OrderTypeUtil _OrderTypeUtil = new OrderTypeUtil();
						try {
						String PLANT= StrUtils.fString(request.getParameter("PLANT"));
						String ORDTYP= StrUtils.fString(request.getParameter("ORDTYP"));
						request.getSession().setAttribute("RESULT","");
						boolean mesflag = false;
						ArrayList OrderTypeQryList=      _OrderTypeUtil.getOrderTypeListDetails(PLANT,ORDTYP);
						if (OrderTypeQryList.size() > 0) {
						for(int i =0; i<OrderTypeQryList.size(); i++) {
						Map arrCustLine = (Map)OrderTypeQryList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ORDERTYPE", (String)arrCustLine.get("ORDERTYPE"));
						resultJsonInt.put("ORDERDESC", (String)arrCustLine.get("ORDERDESC"));
						resultJsonInt.put("TYPE", (String)arrCustLine.get("TYPE"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
						resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
						jsonArray.add(resultJsonInt);
						}
						resultJson.put("CUSTOMERTYPELIST", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
						}else {
							JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
			                resultJsonInt.put("ERROR_CODE", "99");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);  
						}
						} catch (Exception e) {
						resultJson.put("SEARCH_DATA", "");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("CUSTOMERTYPELIST", jsonArray);
						}
						return resultJson;
						}
					//Navas end ordertype
					
					//NAVAS SUPPLIERDISCOUNT 
					private JSONObject getSupplierReport(HttpServletRequest request) {
						JSONObject resultJson = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						JSONArray jsonArrayErr = new JSONArray();
						EmployeeUtil custUtil = new EmployeeUtil();
						 Hashtable ht = new Hashtable();
						 VendMstDAO vendmstdao = new VendMstDAO();
						 StrUtils strUtils     = new StrUtils();
						try {
						String PLANT= StrUtils.fString(request.getParameter("PLANT"));
						String LOC     = strUtils.fString(request.getParameter("LOC"));
						String ITEM    = strUtils.fString(request.getParameter("ITEM"));
						String CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMER"));
						String sSupplierTypeId  = strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
						String PRD_DESCRIP= StrUtils.fString(request.getParameter("PRD_DESCRIP"));
						request.getSession().setAttribute("RESULT","");
						boolean mesflag = false;
						  ht.put("C.PLANT",PLANT);
					      if(strUtils.fString(PLANT).length() > 0)        
					      if(strUtils.fString(ITEM).length() > 0)      
					      {
					        ht.put("I.ITEM",ITEM);
					      } 
					      if(strUtils.fString(CUSTOMER).length() > 0)      
					      {
					        ht.put("V.VNAME",new StrUtils().InsertQuotes(CUSTOMER));
					      }
						ArrayList invQryList =   vendmstdao.getSupplierReport(ht,PRD_DESCRIP,0,100);
						if (invQryList.size() > 0) {
						for(int i =0; i<invQryList.size(); i++) {
						Map arrCustLine = (Map)invQryList.get(i);
						String discountType="";
			            String discount=(String)arrCustLine.get("DISCOUNT");
			            double cost =Double.parseDouble((String)arrCustLine.get("COST"));
			            double discountedcost=0.00d;
			            double converteddiscount=0.00d;
			            double discount1=0.00d;
			            double discountedper=0.00d;
			            int plusIndex = discount.indexOf("%");
			            if (plusIndex != -1) {
			            	discount = discount.substring(0, plusIndex);
			            	converteddiscount=Double.parseDouble(discount);
			            	discountType = "BYPERCENTAGE";
			            	discountedcost=(converteddiscount/100)*cost;
			            	discountedper=cost-discountedcost;
			               }
			            else{
			            	 discount1 =Double.parseDouble(discount);
			            }
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ITEM", (String)arrCustLine.get("ITEM"));
						resultJsonInt.put("ITEMDESCRIPTION", (String)arrCustLine.get("ITEMDESCRIPTION"));
						resultJsonInt.put("SUPPLIERID", (String)arrCustLine.get("SUPPLIERID"));
						resultJsonInt.put("SUPPLIERNAME", (String)arrCustLine.get("SUPPLIERNAME"));
						resultJsonInt.put("COST", StrUtils.formatNum(String.format("%.2f",cost)));
						resultJsonInt.put("DISCOUNT", (String)arrCustLine.get("DISCOUNT"));    
								 if(discountType.equalsIgnoreCase("BYPERCENTAGE")){
								 resultJsonInt.put("discountedper",StrUtils.formatNum(String.format("%.2f",discountedper)));
								 }else{
								 resultJsonInt.put("discountedper",StrUtils.formatNum(String.format("%.2f",discount1)));
								 }
						jsonArray.add(resultJsonInt);
						}
						resultJson.put("SUPPLIERTYPELIST", jsonArray);
						}else {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("SUPPLIERTYPELIST", jsonArray);
						}
						} catch (Exception e) {
						resultJson.put("SEARCH_DATA", "");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("SUPPLIERTYPELIST", jsonArray);
						}
						return resultJson;
						}
					// NAVAS SUPPLIERDISCOUNT end
					
					//NAVAS  CUSTOMERDISCOUNT START 
					private JSONObject getCustomerForReportPgn(HttpServletRequest request) {
						JSONObject resultJson = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						JSONArray jsonArrayErr = new JSONArray();
						 Hashtable ht = new Hashtable();
						 CustMstDAO custdao = new CustMstDAO();
						 StrUtils strUtils     = new StrUtils();
						 PlantMstDAO _PlantMstDAO = new PlantMstDAO();
						try {
//							ITEM    = strUtils.fString(request.getParameter("ITEM"));
//							CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMER"));
//							sCustomerTypeId  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
//							PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
						String PLANT= StrUtils.fString(request.getParameter("PLANT"));
						String ITEMDESC = strUtils.fString(request.getParameter("PRDDESCRIP"));
						String ITEM    = strUtils.fString(request.getParameter("ITEM"));
						String CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMERS"));
						String sCustomerTypeId  = strUtils.fString(request.getParameter("CUSTOMERTYPE_ID"));
						String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
						request.getSession().setAttribute("RESULT","");
						boolean mesflag = false;
					    ht.put("M.PLANT",PLANT);
						   if(strUtils.fString(ITEM).length() > 0)      
						    {
						      ht.put("I.ITEM",ITEM);
						    } 
						    if(strUtils.fString(CUSTOMER).length() > 0)      
						    {
						      ht.put("C.CNAME",new StrUtils().InsertQuotes(CUSTOMER));
						    }
						    if(strUtils.fString(sCustomerTypeId).length() > 0)      
						    {
						      ht.put("C.CUSTOMER_TYPE_ID",sCustomerTypeId);
						    }
						ArrayList invQryList = custdao.getCustomerForReportPgn(ht,ITEMDESC,0,100);
						if (invQryList.size() > 0) {
						for(int i =0; i<invQryList.size(); i++) {
						Map arrCustLine = (Map) invQryList.get(i);
						 String discountType="";
			                String discount=(String)arrCustLine.get("OBDISCOUNT");
			                double UnitPrice =Double.parseDouble((String)arrCustLine.get("UnitPrice"));
			                double discountedcost=0.00d;
			                double converteddiscount=0.00d;
			                double discount1=0.00d;
			                double discountedper=0.00d;
			                int plusIndex = discount.indexOf("%");
			                if (plusIndex != -1) {
			                	discount = discount.substring(0, plusIndex);
			                	converteddiscount=Double.parseDouble(discount);
			                	discountType = "BYPERCENTAGE";
			                	discountedcost=(converteddiscount/100)*UnitPrice;
			                	discountedper=UnitPrice-discountedcost;
			                   }
			                else{
			                	 discount1 =Double.parseDouble(discount);
			                }
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("CUSTID", (String)arrCustLine.get("CUSTID"));
						resultJsonInt.put("CUSTTYPE", (String)arrCustLine.get("CUSTTYPE"));
						resultJsonInt.put("CUSTOMERNAME", (String)arrCustLine.get("CUSTOMERNAME"));
						resultJsonInt.put("ITEM", (String)arrCustLine.get("ITEM"));
						resultJsonInt.put("ITEMDESC", (String)arrCustLine.get("ITEMDESC"));
						resultJsonInt.put("OBDISCOUNT", (String)arrCustLine.get("OBDISCOUNT")); 
						resultJsonInt.put("UnitPrice",  StrUtils.addZeroes(UnitPrice,numberOfDecimal));    
						if(discountType.equalsIgnoreCase("BYPERCENTAGE")){
						resultJsonInt.put("discountedper", StrUtils.addZeroes(discountedper,numberOfDecimal));
						}else{
					    resultJsonInt.put("discountedper", StrUtils.addZeroes(discount1,numberOfDecimal));
						}
						jsonArray.add(resultJsonInt);
						}
						resultJson.put("SUPPLIERTYPELIST", jsonArray);
						}else {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("SUPPLIERTYPELIST", jsonArray);
						}
						} catch (Exception e) {
						resultJson.put("SEARCH_DATA", "");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("SUPPLIERTYPELIST", jsonArray);
						}
						return resultJson;
						}
					// NAVAS CUSTOMERDISCOUNT end
					
					//Navas start CURRENCY
					private JSONObject getCurrencyDetails(HttpServletRequest request) {
						JSONObject resultJson = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						JSONArray jsonArrayErr = new JSONArray();
						CurrencyUtil curUtil = new CurrencyUtil();
						LocTypeUtil loctypeutil = new LocTypeUtil();
						StrUtils strUtils     = new StrUtils();
						try {
						String PLANT= StrUtils.fString(request.getParameter("PLANT"));
						String CURRENCYID     = strUtils.fString(request.getParameter("CURRENCY"));
						request.getSession().setAttribute("RESULT","");
						boolean mesflag = false;
						ArrayList   currencyList =  curUtil.getCurrencyDetails(CURRENCYID,PLANT);			
						if (currencyList.size() > 0) {						
						for(int i =0; i<currencyList.size(); i++) {	
						ArrayList arrCustLine = (ArrayList)currencyList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("CURRENCYID", (String)arrCustLine.get(0));
						resultJsonInt.put("DESCRIPTION", (String)arrCustLine.get(1));
						resultJsonInt.put("DISPLAY", (String)arrCustLine.get(2));
						resultJsonInt.put("CURREQT", (String)arrCustLine.get(3));
						resultJsonInt.put("REMARK", (String)arrCustLine.get(5));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get(6));
						jsonArray.add(resultJsonInt);
						}
						resultJson.put("CUSTOMERTYPELIST", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
						}else {
							JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
			                resultJsonInt.put("ERROR_CODE", "99");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);  
						}
						} catch (Exception e) {
						resultJson.put("SEARCH_DATA", "");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("CUSTOMERTYPELIST", jsonArray);
						}
						return resultJson;
						}
					//Navas end CURRENCYTYPE
					
					//Navas start locationType
						private JSONObject getLocTypeList(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							CustUtil custUtils = new CustUtil();
							LocTypeUtil loctypeutil = new LocTypeUtil();
							StrUtils strUtils     = new StrUtils();
							 Hashtable ht = new Hashtable();
							try {
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOCATIONTYPE"));
							request.getSession().setAttribute("RESULT","");
							if(strUtils.fString(PLANT).length() > 0)  
							ht.put("PLANT",PLANT);
							boolean mesflag = false;
							ArrayList   locQryList = loctypeutil.getLocTypeList(LOC_TYPE_ID,PLANT,"");
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
							Map arrCustLine = (Map)locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
							resultJsonInt.put("LOC_TYPE_DESC", (String)arrCustLine.get("LOC_TYPE_DESC"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
							}else {
								JSONObject resultJsonInt = new JSONObject();
				                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				                resultJsonInt.put("ERROR_CODE", "99");
				                jsonArrayErr.add(resultJsonInt);
				                resultJson.put("errors", jsonArrayErr);  
							}
							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//Navas end locationTYpe
						
						//Navas start locationTypetwo
						private JSONObject getLocTypeListTwo(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							CustUtil custUtils = new CustUtil();
							LocTypeUtil loctypeutil = new LocTypeUtil();
							StrUtils strUtils     = new StrUtils();
							 Hashtable ht = new Hashtable();
							try {
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOCATIONTYPE"));
							request.getSession().setAttribute("RESULT","");
							if(strUtils.fString(PLANT).length() > 0)  
							ht.put("PLANT",PLANT);
							boolean mesflag = false;
							ArrayList   locQryList = loctypeutil.getLocTypeList(LOC_TYPE_ID,PLANT,"");
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
							Map arrCustLine = (Map)locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
							resultJsonInt.put("LOC_TYPE_DESC", (String)arrCustLine.get("LOC_TYPE_DESC"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
							}else {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
							}
							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//Navas end locationTYpetwo
						
						// RESVI STARTSPRDBRANDSUMMARY
						private JSONObject getPrdBrandList(HttpServletRequest request){
						JSONObject resultJson = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						JSONArray jsonArrayErr = new JSONArray();
						
						//List brandItemsList = new ArrayList();
						PlantMstDAO _PlantMstDAO = new PlantMstDAO();
						PrdBrandDAO prdBrandDAO = new PrdBrandDAO();

						try {
						String plant= StrUtils.fString(request.getParameter("PLANT"));
						String productBrandID= StrUtils.fString(request.getParameter("PRODUCTBRANDID"));


						request.getSession().setAttribute("RESULT","");
						boolean mesflag = false;
						ArrayList brandItemsList = prdBrandDAO.getPrdBrandDetails(productBrandID,plant,"");
						if (brandItemsList.size() > 0) {
						for(int i =0; i<brandItemsList.size(); i++) {
						Map arrCustLine = (Map)brandItemsList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("PRD_BRAND_ID", (String)arrCustLine.get("PRD_BRAND_ID"));
						resultJsonInt.put("PRD_BRAND_DESC", (String)arrCustLine.get("PRD_BRAND_DESC"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
						jsonArray.add(resultJsonInt);
						}
						resultJson.put("PRODUCTBRAND", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
						}else {
							JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
			                resultJsonInt.put("ERROR_CODE", "99");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);  
						}

						} catch (Exception e) {
						resultJson.put("SEARCH_DATA", "");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("PRODUCTBRAND", jsonArray);
						}
						return resultJson;
						}
						//RESVI end PRDBRANDSUMMARY
						
						// RESVI STARTSTRANMODESUMMARY
						private JSONObject getTranModeList(HttpServletRequest request){
						JSONObject resultJson = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						JSONArray jsonArrayErr = new JSONArray();
						
						TransportModeDAO transportModeDAO = new TransportModeDAO();

						try {
						String plant= StrUtils.fString(request.getParameter("PLANT"));
						String ID= StrUtils.fString(request.getParameter("ID"));


						request.getSession().setAttribute("RESULT","");
						boolean mesflag = false;
						ArrayList tranModeList = transportModeDAO.getTranModelDetails(ID,plant,"");
						if (tranModeList.size() > 0) {
						for(int i =0; i<tranModeList.size(); i++) {
						Map arrCustLine = (Map)tranModeList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						resultJsonInt.put("TRANSPORT_MODE", (String)arrCustLine.get("TRANSPORT_MODE"));
						//resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));


						jsonArray.add(resultJsonInt);
						System.out.println("JSONARRAY "+ resultJsonInt);
						}
						resultJson.put("TRANSPORTMODE", jsonArray);
						}else {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("TRANSACTIONMODE", jsonArray);
						}

						} catch (Exception e) {
						resultJson.put("SEARCH_DATA", "");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("TRANSACTIONMODE", jsonArray);
						}
						return resultJson;
						}
						//RESVI end TRANMODESUMMARY
						
//						RESVI STARTS ALTERNATE
						private JSONObject getAlternateProductSummary(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							ItemUtil itemUtil = new ItemUtil();
							try {
							List locQryList  = new ArrayList();
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String ITEM= StrUtils.fString(request.getParameter("ITEM"));
							String ITEM_DESC= StrUtils.fString(request.getParameter("ITEM_DESC"));
							String PRD_CLS_ID= StrUtils.fString(request.getParameter("PRD_CLS_ID"));
							String PRD_TYPE_ID= StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
							String PRD_DEPT_ID= StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
							String PRD_BRAND_ID= StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							locQryList= itemUtil.queryAlternetItemMstForSearchCriteriaNew(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,PLANT,"",0,100);
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
								Vector arrCustLine = (Vector)locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ID", (String)arrCustLine.get(3));
							resultJsonInt.put("0", (String)arrCustLine.get(0));
							resultJsonInt.put("1", (String)arrCustLine.get(1));
							resultJsonInt.put("2", (String)arrCustLine.get(2));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}else {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
							}
//						RESVI ENDS ALTERNATE
						
						//resvi START LOCT
						private JSONObject getLocDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							LocUtil _LocUtil = new LocUtil();
							Hashtable ht = new Hashtable();
							
							try {
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String LOC_ID     = strUtils.fString(request.getParameter("LOCT"));
							String LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOCTYPE1"));
							String LOC_TYPE_ID2     = strUtils.fString(request.getParameter("LOCTYPE2"));
							String LOC_TYPE_ID3     = strUtils.fString(request.getParameter("LOCTYPE3"));
							if(strUtils.fString(PLANT).length() > 0)      
							ht.put("PLANT",PLANT);
							ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
						    ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
						    ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
							String	RSN     = strUtils.fString(request.getParameter("PRODUCTTYPEID"));
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							ArrayList locQryList =    _LocUtil.getLocDetails(LOC_ID,PLANT," ",ht);
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
								Map arrCustLine = (Map) locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("LOC", (String)arrCustLine.get("LOC"));
							resultJsonInt.put("LOCDESC", (String)arrCustLine.get("LOCDESC"));
							resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
							resultJsonInt.put("LOC_TYPE_ID2", (String)arrCustLine.get("LOC_TYPE_ID2"));
							resultJsonInt.put("LOC_TYPE_ID3", (String)arrCustLine.get("LOC_TYPE_ID3"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
							resultJsonInt.put("USERFLD1", (String)arrCustLine.get("USERFLD1"));	
							resultJsonInt.put("COMNAME", (String)arrCustLine.get("COMNAME"));
							resultJsonInt.put("RCBNO", (String)arrCustLine.get("RCBNO"));
							resultJsonInt.put("ADD1", (String)arrCustLine.get("ADD1"));
							resultJsonInt.put("ADD2", (String)arrCustLine.get("ADD2"));
							resultJsonInt.put("ADD3", (String)arrCustLine.get("ADD3"));
							resultJsonInt.put("ADD4", (String)arrCustLine.get("ADD4"));
							resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
							resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
							resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
							resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
							resultJsonInt.put("FAX", (String)arrCustLine.get("TELNO"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}else {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}

							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//resvi end LOCT
						
						//imti start assignloc 
						private JSONObject getUserAssignLocDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							LocUtil _LocUtil = new LocUtil();
							Hashtable ht = new Hashtable();
							UserLocUtil _userLocUtil = null;
							_userLocUtil = new UserLocUtil();
							HttpSession session = request.getSession();
							try {
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String LOC_ID     = strUtils.fString(request.getParameter("LOCT"));
							String LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOCTYPE1"));
							String LOC_TYPE_ID2     = strUtils.fString(request.getParameter("LOCTYPE2"));
							String LOC_TYPE_ID3     = strUtils.fString(request.getParameter("LOCTYPE3"));
							String AssignedUser = StrUtils.fString(request.getParameter("UI_PKEY")).trim();
		                    String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
		                    String plant = (String) session.getAttribute("PLANT");
							String USER     = strUtils.fString(request.getParameter("USER")); //
							if(strUtils.fString(PLANT).length() > 0)      
							ht.put("PLANT",PLANT);
							ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
						    ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
						    ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
							String	RSN     = strUtils.fString(request.getParameter("PRODUCTTYPEID"));
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							ArrayList locQryList =    _LocUtil.getLocDetails(LOC_ID,PLANT," ",ht);
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
								Map arrCustLine = (Map) locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							if (AssignedUser.length() > 0) {
							boolean flag = _userLocUtil.isValidUserAssignedLoc(plant,AssignedUser,(String)arrCustLine.get("LOC"));
							if (flag)
							resultJsonInt.put("CHK","yes");
							else
							resultJsonInt.put("CHK","no");
							}else {
							resultJsonInt.put("CHK","no");
							}
							resultJsonInt.put("LOC", (String)arrCustLine.get("LOC"));
							resultJsonInt.put("LOCDESC", (String)arrCustLine.get("LOCDESC"));
							resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
							resultJsonInt.put("LOC_TYPE_ID2", (String)arrCustLine.get("LOC_TYPE_ID2"));
							resultJsonInt.put("LOC_TYPE_ID3", (String)arrCustLine.get("LOC_TYPE_ID3"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
							resultJsonInt.put("USERFLD1", (String)arrCustLine.get("USERFLD1"));	
							resultJsonInt.put("COMNAME", (String)arrCustLine.get("COMNAME"));
							resultJsonInt.put("RCBNO", (String)arrCustLine.get("RCBNO"));
							resultJsonInt.put("ADD1", (String)arrCustLine.get("ADD1"));
							resultJsonInt.put("ADD2", (String)arrCustLine.get("ADD2"));
							resultJsonInt.put("ADD3", (String)arrCustLine.get("ADD3"));
							resultJsonInt.put("ADD4", (String)arrCustLine.get("ADD4"));
							resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
							resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
							resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
							resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
							resultJsonInt.put("FAX", (String)arrCustLine.get("TELNO"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}else {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}

							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//imthi end assignloc
						
						//Thanzi start assigncustomer 
						private JSONObject getUserAssignCusDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							CustomerBeanDAO _LocUtil = new CustomerBeanDAO();
							Hashtable ht = new Hashtable();
							UserLocUtil _userLocUtil = null;
							CustUtil CustUtil = null;
							CustUtil = new CustUtil();
							_userLocUtil = new UserLocUtil();
							HttpSession session = request.getSession();
							try {
								String PLANT= StrUtils.fString(request.getParameter("PLANT"));
								String CUSTNO     = strUtils.fString(request.getParameter("CUSTNO"));
								String CNAME     = strUtils.fString(request.getParameter("CNAME"));
								String CUSTOMER_TYPE     = strUtils.fString(request.getParameter("CUSTOMER_TYPE"));
								String EMAIL     = strUtils.fString(request.getParameter("EMAIL"));
								String AssignedUser = StrUtils.fString(request.getParameter("UI_PKEY")).trim();
								String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
								String plant = (String) session.getAttribute("PLANT");
								String USER     = strUtils.fString(request.getParameter("USER")); //
								
								  if(strUtils.fString(PLANT).length() > 0) ht.put("PLANT",PLANT);
								  ht.put("CUSTNO",CUSTNO); ht.put("CNAME",CNAME);
								  ht.put("CUSTOMER_TYPE",CUSTOMER_TYPE);
								 
								String	RSN     = strUtils.fString(request.getParameter("PRODUCTTYPEID"));
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList locQryList =    CustUtil.getUserCustDetails(CUSTNO,ht,PLANT);
								if (locQryList.size() > 0) {
									for(int i =0; i<locQryList.size(); i++) {
										Map arrCustLine = (Map) locQryList.get(i);
										JSONObject resultJsonInt = new JSONObject();
										if (AssignedUser.length() > 0) {
											boolean flag = CustUtil.isValidUserAssignedCus(plant,AssignedUser,(String)arrCustLine.get("CUSTNO"));
											if (flag)
												resultJsonInt.put("CHK","yes");
											else
												resultJsonInt.put("CHK","no");
										}else {
											resultJsonInt.put("CHK","no");
										}
										resultJsonInt.put("CUSTNO", (String)arrCustLine.get("CUSTNO"));
										resultJsonInt.put("CUSTDESC", (String)arrCustLine.get("CNAME"));
										resultJsonInt.put("CUST_TYPE", (String)arrCustLine.get("CUSTOMER_TYPE"));
										resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
										resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
										
										jsonArray.add(resultJsonInt);
									}
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "98");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("ERROR", jsonArrayErr);
									jsonArray.add("");
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}
								
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
						}
						//Thanzi end assignCus
						
						//Thanzi start assignProduct 
						private JSONObject getUserAssignitemDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							CustomerBeanDAO _LocUtil = new CustomerBeanDAO();
							Hashtable ht = new Hashtable();
							ItemUtil itemUtil = null;
							itemUtil = new ItemUtil();
							HttpSession session = request.getSession();
							try {
								String PLANT= StrUtils.fString(request.getParameter("PLANT"));
								String ITEM     = strUtils.fString(request.getParameter("ITEM"));
								String ITEMDESC     = strUtils.fString(request.getParameter("ITEMDESC"));
								String PRD_DEPT_ID     = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
								String PRD_CLS_ID     = strUtils.fString(request.getParameter("PRD_CLS_ID"));
								String PRD_TYPE_ID     = strUtils.fString(request.getParameter("ITEMTYPE"));
								String PRD_BRAND_ID     = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
								String AssignedUser = StrUtils.fString(request.getParameter("CUSTNO")).trim();
								String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
								String plant = (String) session.getAttribute("PLANT");
								String USER     = strUtils.fString(request.getParameter("USER")); //
								
								if(strUtils.fString(PLANT).length() > 0) ht.put("PLANT",PLANT);
								ht.put("ITEM",ITEM); 
								ht.put("ITEMDESC",ITEMDESC);
								ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
								
								String	RSN     = strUtils.fString(request.getParameter("PRODUCTTYPEID"));
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList locQryList =    itemUtil.getUserItemDetails(ITEM,ht,PLANT);
								if (locQryList.size() > 0) {
									for(int i =0; i<locQryList.size(); i++) {
										Map arrCustLine = (Map) locQryList.get(i);
										JSONObject resultJsonInt = new JSONObject();
										if (AssignedUser.length() > 0) {
											boolean flag = itemUtil.isValidCustAssignedItem(plant,AssignedUser,(String)arrCustLine.get("ITEM"));
											if (flag)
												resultJsonInt.put("CHK","yes");
											else
												resultJsonInt.put("CHK","no");
										}else {
											resultJsonInt.put("CHK","no");
										}
										resultJsonInt.put("ITEM", (String)arrCustLine.get("ITEM"));
										resultJsonInt.put("ITEMDESC", (String)arrCustLine.get("ITEMDESC"));
										resultJsonInt.put("PRD_DEPT_ID", (String)arrCustLine.get("PRD_DEPT_ID"));
										resultJsonInt.put("PRD_CLS_ID", (String)arrCustLine.get("PRD_CLS_ID"));
										resultJsonInt.put("PRD_TYPE_ID", (String)arrCustLine.get("ITEMTYPE"));
										resultJsonInt.put("PRD_BRAND_ID", (String)arrCustLine.get("PRD_BRAND_ID"));
										resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
										
										jsonArray.add(resultJsonInt);
									}
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "98");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("ERROR", jsonArrayErr);
									jsonArray.add("");
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}
								
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
						}
						//Thanzi end assignCus
						private JSONObject getPOSCategoryDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							Hashtable ht = new Hashtable();
							UserLocUtil _userLocUtil = null;
							_userLocUtil = new UserLocUtil();
							HttpSession session = request.getSession();
							try {
								String PLANT= StrUtils.fString(request.getParameter("PLANT"));
								String AssignedLoc = StrUtils.fString(request.getParameter("chkdDoNo")).trim();
								String plant = (String) session.getAttribute("PLANT");
								String USER     = strUtils.fString(request.getParameter("USER")); //
								String AssignedUser = (request.getParameter("PRINTWITHCATEGORY") != null) ? "1": "0";
								if(strUtils.fString(PLANT).length() > 0)      
									ht.put("PLANT",PLANT);
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList locQryList = new PrdClassDAO().getPrdClsDetails("",PLANT," ");
								if (locQryList.size() > 0) {
									for(int i =0; i<locQryList.size(); i++) {
										Map arrCustLine = (Map) locQryList.get(i);
										JSONObject resultJsonInt = new JSONObject();
										if (AssignedUser.length() > 0) {
											boolean flag = new PrdClassDAO().isAlreadyPOSCategoryExists((String)arrCustLine.get("prd_cls_id"),plant);
											if (flag)
												resultJsonInt.put("CHK","yes");
											else
												resultJsonInt.put("CHK","no");
										}else {
											resultJsonInt.put("CHK","no");
										}
										resultJsonInt.put("PRD_CLS_ID", (String)arrCustLine.get("prd_cls_id"));
										resultJsonInt.put("PRD_CLS_DESC", (String)arrCustLine.get("prd_cls_desc"));
										jsonArray.add(resultJsonInt);
									}
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "98");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("ERROR", jsonArrayErr);
									jsonArray.add("");
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}
								
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
						}
						//azees end POS Category Details
						
						//imti start assigninvloc 
						private JSONObject getAssignInvLocDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							LocUtil _LocUtil = new LocUtil();
							Hashtable ht = new Hashtable();
							UserLocUtil _userLocUtil = null;
							_userLocUtil = new UserLocUtil();
							 UserAssignedLocDAO userlocDAO = new UserAssignedLocDAO();
							HttpSession session = request.getSession();
							try {
								String PLANT= StrUtils.fString(request.getParameter("PLANT"));
								String LOC_ID     = strUtils.fString(request.getParameter("LOCT"));
								String LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOCTYPE1"));
								String LOC_TYPE_ID2     = strUtils.fString(request.getParameter("LOCTYPE2"));
								String LOC_TYPE_ID3     = strUtils.fString(request.getParameter("LOCTYPE3"));
				     			
								String plant = (String) session.getAttribute("PLANT");
								String USER     = strUtils.fString(request.getParameter("USER")); //
								if(strUtils.fString(PLANT).length() > 0)      
									ht.put("PLANT",PLANT);
								ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
								ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
								ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
								String	RSN     = strUtils.fString(request.getParameter("PRODUCTTYPEID"));
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList locQryList =    _LocUtil.getLocDetails(LOC_ID,PLANT," ",ht);
								if (locQryList.size() > 0) {
									for(int i =0; i<locQryList.size(); i++) {
										Map arrCustLine = (Map) locQryList.get(i);
										JSONObject resultJsonInt = new JSONObject();
											boolean flag = _userLocUtil.isAlreadyInvLocExist(plant,(String)arrCustLine.get("LOC"));
											if (flag)
												resultJsonInt.put("CHK","yes");
											else
												resultJsonInt.put("CHK","no");
										resultJsonInt.put("LOC", (String)arrCustLine.get("LOC"));
										resultJsonInt.put("LOCDESC", (String)arrCustLine.get("LOCDESC"));
										resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
										resultJsonInt.put("LOC_TYPE_ID2", (String)arrCustLine.get("LOC_TYPE_ID2"));
										resultJsonInt.put("LOC_TYPE_ID3", (String)arrCustLine.get("LOC_TYPE_ID3"));
										resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
										resultJsonInt.put("USERFLD1", (String)arrCustLine.get("USERFLD1"));	
										resultJsonInt.put("COMNAME", (String)arrCustLine.get("COMNAME"));
										resultJsonInt.put("RCBNO", (String)arrCustLine.get("RCBNO"));
										resultJsonInt.put("ADD1", (String)arrCustLine.get("ADD1"));
										resultJsonInt.put("ADD2", (String)arrCustLine.get("ADD2"));
										resultJsonInt.put("ADD3", (String)arrCustLine.get("ADD3"));
										resultJsonInt.put("ADD4", (String)arrCustLine.get("ADD4"));
										resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
										resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
										resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
										resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
										resultJsonInt.put("FAX", (String)arrCustLine.get("TELNO"));
										jsonArray.add(resultJsonInt);
									}
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "98");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("ERROR", jsonArrayErr);
									jsonArray.add("");
									resultJson.put("SUPPLIERTYPELIST", jsonArray);
								}
								
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
						}
						//imthi end assignloc
						
						//RESVI start PRDCLASSSUMMARY
						private JSONObject getPrdclsList(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							CustUtil custUtils = new CustUtil(); 
							PrdClassUtil prdclsutil = new PrdClassUtil();
							StrUtils strUtils     = new StrUtils();
							try {
							String	RSN     = strUtils.fString(request.getParameter("PRODUCTCLASSID"));			
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String PRODUCTCLASSID= StrUtils.fString(request.getParameter("PRODUCTCLASSID"));
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							ArrayList locQryList =  prdclsutil.getPrdTypeList(RSN,PLANT,"");
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
							Map arrCustLine = (Map)locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("prd_cls_id", (String)arrCustLine.get("prd_cls_id"));
							resultJsonInt.put("prd_cls_desc", (String)arrCustLine.get("prd_cls_desc"));
							resultJsonInt.put("CATALOG", (String)arrCustLine.get("CATALOG"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
							}else {
								JSONObject resultJsonInt = new JSONObject();
				                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				                resultJsonInt.put("ERROR_CODE", "99");
				                jsonArrayErr.add(resultJsonInt);
				                resultJson.put("errors", jsonArrayErr);  
							}
							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//RESVI end PRDCLASSSUMMARY	
						
						
						//RESVI start PRDDEPTUMMARY
						private JSONObject getPrdDepList(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							CustUtil custUtils = new CustUtil(); 
							PrdDeptUtil prddeputil = new PrdDeptUtil();
							StrUtils strUtils     = new StrUtils();
							try {
							String	RSN     = strUtils.fString(request.getParameter("PRODUCTDEPARTMENTID"));			
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String PRODUCTDEPARTMENTID= StrUtils.fString(request.getParameter("PRODUCTDEPARTMENTID"));
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							ArrayList locQryList =  prddeputil.getPrdTypeList(RSN,PLANT,"");
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
							Map arrCustLine = (Map)locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("prd_dep_id", (String)arrCustLine.get("prd_dept_id"));
							resultJsonInt.put("prd_dep_desc", (String)arrCustLine.get("prd_dept_desc"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
							}else {
								JSONObject resultJsonInt = new JSONObject();
				                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				                resultJsonInt.put("ERROR_CODE", "99");
				                jsonArrayErr.add(resultJsonInt);
				                resultJson.put("errors", jsonArrayErr);  
							}
							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("CUSTOMERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//RESVI end PRDDEPTUMMARY	
						
//						Thanzith Start Clearing Agent
						private JSONObject getClearAgentList(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							CustUtil custUtils = new CustUtil(); 
							PrdDeptUtil prddeputil = new PrdDeptUtil();
							StrUtils strUtils     = new StrUtils();
							try {
//								String	RSN     = strUtils.fString(request.getParameter("DESIGNATIONID"));			
								String plant= StrUtils.fString(request.getParameter("PLANT"));
								String designationname= StrUtils.fString(request.getParameter("CLEARAGENTID"));
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList locQryList =  new ClearAgentDAO().getClearingAgentDetails(designationname,plant,"");
								if (locQryList.size() > 0) {
									for(int i =0; i<locQryList.size(); i++) {
										Map arrCustLine = (Map)locQryList.get(i);
										JSONObject resultJsonInt = new JSONObject();
										resultJsonInt.put("clearing_agent_id", (String)arrCustLine.get("CLEARING_AGENT_ID"));
										resultJsonInt.put("clearing_agent_name", (String)arrCustLine.get("CLEARING_AGENT_NAME"));
										resultJsonInt.put("isactive", (String)arrCustLine.get("ISACTIVE"));
										jsonArray.add(resultJsonInt);
									}
									resultJson.put("DESIGNATIONLIST", jsonArray);
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
									resultJsonInt.put("ERROR_CODE", "100");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("errors", jsonArrayErr);
								}else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO DESIGNATION RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "99");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("errors", jsonArrayErr);  
								}
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("DESIGNATIONLIST", jsonArray);
							}
							return resultJson;
						}
//					End		
						private JSONObject getTransportClearAgentList(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							CustUtil custUtils = new CustUtil(); 
							PrdDeptUtil prddeputil = new PrdDeptUtil();
							StrUtils strUtils     = new StrUtils();
							try {
//								String	RSN     = strUtils.fString(request.getParameter("DESIGNATIONID"));			
								String plant= StrUtils.fString(request.getParameter("PLANT"));
								String designationname= StrUtils.fString(request.getParameter("CLEARAGENTID"));
								String transport= StrUtils.fString(request.getParameter("TRANSPORTID"));
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList locQryList =  new ClearAgentDAO().getClearingAgentDetailsByTrasnport(transport,designationname,plant,"");
								if (locQryList.size() > 0) {
									for(int i =0; i<locQryList.size(); i++) {
										Map arrCustLine = (Map)locQryList.get(i);
										JSONObject resultJsonInt = new JSONObject();
										resultJsonInt.put("clearing_agent_id", (String)arrCustLine.get("CLEARING_AGENT_ID"));
										resultJsonInt.put("clearing_agent_name", (String)arrCustLine.get("CLEARING_AGENT_NAME"));
										resultJsonInt.put("CONTACTNAME", (String)arrCustLine.get("CONTACTNAME"));
										resultJsonInt.put("TRANSPORT", (String)arrCustLine.get("TRANSPORT"));
										resultJsonInt.put("isactive", (String)arrCustLine.get("ISACTIVE"));
										jsonArray.add(resultJsonInt);
									}
									resultJson.put("DESIGNATIONLIST", jsonArray);
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
									resultJsonInt.put("ERROR_CODE", "100");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("errors", jsonArrayErr);
								}else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO DESIGNATION RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "99");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("errors", jsonArrayErr);  
								}
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("DESIGNATIONLIST", jsonArray);
							}
							return resultJson;
						}
						
						//resvi START PRODUCTTYPEE
						private JSONObject getPrdTypeList(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							PrdTypeUtil prdutil = new PrdTypeUtil();
							try {
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String	RSN     = strUtils.fString(request.getParameter("PRODUCTTYPEID"));
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							ArrayList locQryList =    prdutil.getPrdTypeList(RSN,PLANT,"");
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
								Map arrCustLine = (Map) locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("prd_type_id", (String)arrCustLine.get("prd_type_id"));
							resultJsonInt.put("prd_type_desc", (String)arrCustLine.get("prd_type_desc"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));					
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
							}else {
								JSONObject resultJsonInt = new JSONObject();
				                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				                resultJsonInt.put("ERROR_CODE", "99");
				                jsonArrayErr.add(resultJsonInt);
				                resultJson.put("errors", jsonArrayErr);  
							}

							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//resvi end PRODUCTTYPEE
						
						//resvi START UOMTYPE
						private JSONObject getUomDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							UomUtil uomUtil = new UomUtil();
							try {
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
							String UOM = strUtils.fString(request.getParameter("UNITMEASURE"));			
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							ArrayList locQryList =   uomUtil.getUomDetails(UOM, PLANT, "");
							if (locQryList.size() > 0) {
							for(int i =0; i<locQryList.size(); i++) {
								Map arrCustLine = (Map) locQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("uom", (String)arrCustLine.get("uom"));
							resultJsonInt.put("uomdesc", (String)arrCustLine.get("uomdesc"));
							resultJsonInt.put("Display", (String)arrCustLine.get("Display"));
							resultJsonInt.put("QPUOM", (String)arrCustLine.get("QPUOM"));
							resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
							resultJsonInt.put("ISCONVERTIBLE", (String)arrCustLine.get("ISCONVERTIBLE"));
							resultJsonInt.put("CUOM", (String)arrCustLine.get("CUOM"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
							}else {
								JSONObject resultJsonInt = new JSONObject();
				                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				                resultJsonInt.put("ERROR_CODE", "99");
				                jsonArrayErr.add(resultJsonInt);
				                resultJson.put("errors", jsonArrayErr);  
							}

							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("SUPPLIERTYPELIST", jsonArray);
							}
							return resultJson;
							}
						//resvi end uom
						
						//imti internalcrm start
						private JSONObject getusersummaryDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							PlantMstUtil plantmstutil = new PlantMstUtil();
							try {
							String PLANT= StrUtils.fString(request.getParameter("plants"));
							//String UOM = strUtils.fString(request.getParameter("UNITMEASURE"));			
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							ArrayList userOrylist =   plantmstutil.getPlantMstDetailForCrm(PLANT);
							if (userOrylist.size() > 0) {
							for(int i =0; i<userOrylist.size(); i++) {
								Map arrCustLine = (Map) userOrylist.get(i);
							JSONObject resultJsonInt = new JSONObject();
							
							String inventory = (String)arrCustLine.get("ENABLE_INVENTORY");
							String accounting = (String)arrCustLine.get("ENABLE_ACCOUNTING");
							String payroll = (String)arrCustLine.get("ENABLE_PAYROLL");
							String pos = (String)arrCustLine.get("ENABLE_POS");
							
							if(inventory.equals("1")) {
								inventory = "Order Management";
							}else {
								inventory = "";
							}
							if(accounting.equals("1")) {
								accounting = ",Accounting";
							}else {
								accounting = "";
							}
							if(payroll.equals("1")) {
								payroll = ",Payroll";
							}else {
								payroll = "";
							}
							if(pos.equals("1")) {
								pos = ",POS";
							}else {
								pos = "";
							}
							
							resultJsonInt.put("USER", (String)arrCustLine.get("USERNAME"));
							resultJsonInt.put("COMPANYCODE", (String)arrCustLine.get("PLANT"));
							resultJsonInt.put("COMPANYNAME", (String)arrCustLine.get("PLNTDESC"));
							resultJsonInt.put("PERSON", (String)arrCustLine.get("NAME"));
							resultJsonInt.put("CONTACTDETAIL", (String)arrCustLine.get("TELNO")+", "+(String)arrCustLine.get("EMAIL"));
							resultJsonInt.put("SYSTEMTYPE", inventory+accounting+payroll+pos+"");
							resultJsonInt.put("PARTNER", (String)arrCustLine.get("PARTNER"));
							resultJsonInt.put("REGION", (String)arrCustLine.get("REGION"));
							resultJsonInt.put("STATUS", (String)arrCustLine.get("STATUS"));
							resultJsonInt.put("SDATE", (String)arrCustLine.get("STARTDATE"));
							resultJsonInt.put("EDATE", (String)arrCustLine.get("EXPIRYDATE"));
							resultJsonInt.put("AEDATE", (String)arrCustLine.get("ACTEXPIRYDATE"));
							resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("USERLIST", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
							}else {
								JSONObject resultJsonInt = new JSONObject();
				                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				                resultJsonInt.put("ERROR_CODE", "99");
				                jsonArrayErr.add(resultJsonInt);
				                resultJson.put("errors", jsonArrayErr);  
							}

							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("USERLIST", jsonArray);
							}
							return resultJson;
							}
						//imti internalcrm end
						
						//imti Contact start
						private JSONObject getcontactsummaryDetails(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							PlantMstUtil plantmstutil = new PlantMstUtil();
							try {
								String PLANT= StrUtils.fString(request.getParameter("PLANT"));
//								int ID = StrUtils.fString(request.getParameter("PLANT"));
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList userOrylist =   plantmstutil.getContactHdr(PLANT);
								if (userOrylist.size() > 0) {
									for(int i =0; i<userOrylist.size(); i++) {
										Map arrCustLine = (Map) userOrylist.get(i);
										JSONObject resultJsonInt = new JSONObject();
										resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
										resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
										resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
										resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
										resultJsonInt.put("COMPANY", (String)arrCustLine.get("COMPANYCONTACT"));
										resultJsonInt.put("JOB", (String)arrCustLine.get("JOB"));
										resultJsonInt.put("PHNO", (String)arrCustLine.get("PHONENUMBER"));
										resultJsonInt.put("LEAD", (String)arrCustLine.get("LEADSTATUS"));
										resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
										resultJsonInt.put("INDUSTRY", (String)arrCustLine.get("INDUSTRY"));
										resultJsonInt.put("MOBILENO", (String)arrCustLine.get("MOBILENO"));
										resultJsonInt.put("SALESPROBABILITY", (String)arrCustLine.get("SALESPROBABILITY"));
										resultJsonInt.put("LIFECYCLESTAGE", (String)arrCustLine.get("LIFECYCLESTAGE"));
										resultJsonInt.put("NOTE", (String)arrCustLine.get("NOTE"));
										jsonArray.add(resultJsonInt);
									}
									resultJson.put("CONTACTLIST", jsonArray);
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
									resultJsonInt.put("ERROR_CODE", "100");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("errors", jsonArrayErr);
								}else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO CONTACTS RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "99");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("errors", jsonArrayErr);  
								}
								
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("CONTACTLIST", jsonArray);
							}
							return resultJson;
						}
						//imti Contact end
						
						//imti salesdelivery start
						private JSONObject getsalesorderdeliverysummary(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							StrUtils strUtils = new StrUtils();
							  String fdate="",tdate="";
							String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
							String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
							
					         if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					           String curDate =DateUtils.getDate();
								if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
									FROM_DATE=curDate;

					           if (FROM_DATE.length()>5)
					            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

					           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
					           if (TO_DATE.length()>5)
					           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
					           
							String dono = StrUtils.fString(request.getParameter("DONO"));
							String invoice = StrUtils.fString(request.getParameter("INVOICE"));
							String item = StrUtils.fString(request.getParameter("PRODUCT"));
							String status = StrUtils.fString(request.getParameter("STATUS"));
							String shipped = StrUtils.fString(request.getParameter("SHIPPEDSTATUS"));
							String deliverystatus = StrUtils.fString(request.getParameter("DELIVERYSTATUS"));
							String intransit = StrUtils.fString(request.getParameter("INTRANSITSTATUS"));
							
								if(status.equalsIgnoreCase("PROCESSED")) {
									status = "C";
								}else if (status.equalsIgnoreCase("PARTIALLY PROCESSED")) {
									status =	"O";
								}else if (status.equalsIgnoreCase("OPEN")) {
									status =	"N";
								}
							
									if(shipped.equalsIgnoreCase("SHIPPED")) {
										shipped = "C";
									}else if (shipped.equalsIgnoreCase("PARTIALLY SHIPPED")) {
										shipped =	"N";
									}
								
										if(deliverystatus.equalsIgnoreCase("DELIVERED")) {
											deliverystatus = "DELIVERED";
										}
							
							PlantMstUtil plantmstutil = new PlantMstUtil();
							try {
								String PLANT= StrUtils.fString(request.getParameter("PLANT"));
								String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList userOrylist = new ArrayList(); 
								if(COMP_INDUSTRY.equals("Centralised Kitchen")) {
									 userOrylist =   plantmstutil.getsalesorderdeliverysummary(PLANT,fdate,tdate,dono,invoice,item,status,shipped,deliverystatus);
									 if (userOrylist.size() > 0) {
										 for(int i =0; i<userOrylist.size(); i++) {
											 Map arrCustLine = (Map) userOrylist.get(i);
											 JSONObject resultJsonInt = new JSONObject();
											 
											 //String sts = (String)arrCustLine.get("STATUS");
											 String sts = (String)arrCustLine.get("ORDER_STATUS");
											 String picstatus = (String)arrCustLine.get("PickStaus");
											 String delstatus = (String)arrCustLine.get("BILL_STATUS");
											 String Rrevperson = (String)arrCustLine.get("REV_PERSON");
											 
											 /*if(sts.equalsIgnoreCase("C")) {
											sts = "Processed";
										}else if(sts.equalsIgnoreCase("O")) {
											sts =	"Partialy Processed";
										}else if(sts.equalsIgnoreCase("N")) {
											sts =	"Open";
										}*/
											 
											 /*if(picstatus.equalsIgnoreCase("C")) {
												picstatus = "Shipped";
											}else {
												picstatus =	"Partialy Shipped";
											}*/
											 
											 if(sts.equalsIgnoreCase("Partialy Open"))
												 picstatus = "Partialy Shipped";
											 else
												 picstatus = "";
											 
											 if(delstatus.equalsIgnoreCase("DELIVERED")) {
												 delstatus = "Delivered";
												 picstatus = "Shipped";
												 if(Rrevperson.equalsIgnoreCase(""))
													 Rrevperson = (String)arrCustLine.get("CustName");
											 }else {
												 if(delstatus.equalsIgnoreCase("SHIPPED")) { 
													 delstatus = "";	
													 picstatus = "Shipped";	
												 } else {
													 delstatus = "";
												 }
											 }
											 
											 resultJsonInt.put("DONO", (String)arrCustLine.get("DONO"));
											 resultJsonInt.put("INVOICENO", (String)arrCustLine.get("INVOICE"));
											 resultJsonInt.put("ITEM", (String)arrCustLine.get("ITEM"));
											 resultJsonInt.put("QTYPICK", (String)arrCustLine.get("Qty"));
											 resultJsonInt.put("STATUS", sts.toUpperCase());
											 resultJsonInt.put("PICKSTATUS", picstatus.toUpperCase());
											 resultJsonInt.put("BILL_STATUS", delstatus.toUpperCase());
											 resultJsonInt.put("DELIVERYPERSON", (String)arrCustLine.get("DELIVERY_PERSON"));
											 resultJsonInt.put("DELIVERYDATE", (String)arrCustLine.get("DELIVERY_DATE"));
											 resultJsonInt.put("RECEIVEDPERSON", Rrevperson.toUpperCase());
											 //resultJsonInt.put("DELIVERYDATE", "");
											 //resultJsonInt.put("DELIVERYPERSON", "");
											 jsonArray.add(resultJsonInt);
										 }
										 resultJson.put("DELIVERYLIST", jsonArray);
										 JSONObject resultJsonInt = new JSONObject();
										 resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
										 resultJsonInt.put("ERROR_CODE", "100");
										 jsonArrayErr.add(resultJsonInt);
										 resultJson.put("errors", jsonArrayErr);
									 }else {
										 JSONObject resultJsonInt = new JSONObject();
										 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
										 resultJsonInt.put("ERROR_CODE", "99");
										 jsonArrayErr.add(resultJsonInt);
										 resultJson.put("errors", jsonArrayErr);  
									 }
									
								}else {
									 userOrylist =   plantmstutil.getsalesorderdeliveryShippingsummary(PLANT,fdate,tdate,dono,invoice,item,status,shipped,intransit,deliverystatus);
									 if (userOrylist.size() > 0) {
										 for(int i =0; i<userOrylist.size(); i++) {
											 Map arrCustLine = (Map) userOrylist.get(i);
											 JSONObject resultJsonInt = new JSONObject();
											 
											 String SHIPSTATUS="",SHIPDATE="";
											 String sts = (String)arrCustLine.get("ORDER_STATUS");
											 String picstatus = (String)arrCustLine.get("PickStaus");
											 String delstatus = (String)arrCustLine.get("BILL_STATUS");
											 String Rrevperson = (String)arrCustLine.get("REV_PERSON");
											 String shippingsts = (String)arrCustLine.get("SHIPPING_STATUS");
											 String intransitsts = (String)arrCustLine.get("INTRANSIT_STATUS");
											 String deliverysts = (String)arrCustLine.get("DELIVERY_STATUS");
											 if(shippingsts.equalsIgnoreCase("")) {
												 SHIPSTATUS = "Shipped";
												 SHIPDATE= (String)arrCustLine.get("ISSUEDATE");
											 }else {
												 SHIPSTATUS = (String)arrCustLine.get("SHIPPING_STATUS");
												 SHIPDATE= (String)arrCustLine.get("SHIPPING_DATE");
											 }
											 
											 resultJsonInt.put("DONO", (String)arrCustLine.get("DONO"));
											 resultJsonInt.put("INVOICENO", (String)arrCustLine.get("INVOICE"));
											 resultJsonInt.put("ITEM", (String)arrCustLine.get("ITEM"));
											 resultJsonInt.put("QTYPICK", (String)arrCustLine.get("Qty"));
											 resultJsonInt.put("STATUS", sts.toUpperCase());
											 resultJsonInt.put("SHIPPING_STATUS", SHIPSTATUS.toUpperCase());
											 resultJsonInt.put("SHIPPING_DATE", SHIPDATE.toUpperCase());
											 resultJsonInt.put("INTRANSIT_STATUS", intransitsts.toUpperCase());
											 resultJsonInt.put("INTRANSIT_DATE", (String)arrCustLine.get("INTRANSIT_DATE"));
											 resultJsonInt.put("DELIVERY_STATUS", deliverysts.toUpperCase());
											 resultJsonInt.put("DELIVERY_DATE", (String)arrCustLine.get("DELIVERY_DATE"));
//											 resultJsonInt.put("DELIVERYDATE", (String)arrCustLine.get("DELIVERY_DATE"));
											 resultJsonInt.put("DELIVERYPERSON", (String)arrCustLine.get("DELIVERY_PERSON"));
											 resultJsonInt.put("RECEIVEDPERSON", Rrevperson.toUpperCase());
											 jsonArray.add(resultJsonInt);
										 }
										 resultJson.put("DELIVERYLIST", jsonArray);
										 JSONObject resultJsonInt = new JSONObject();
										 resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
										 resultJsonInt.put("ERROR_CODE", "100");
										 jsonArrayErr.add(resultJsonInt);
										 resultJson.put("errors", jsonArrayErr);
									 }else {
										 JSONObject resultJsonInt = new JSONObject();
										 resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
										 resultJsonInt.put("ERROR_CODE", "99");
										 jsonArrayErr.add(resultJsonInt);
										 resultJson.put("errors", jsonArrayErr);  
									 }
								}
								
								
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr);
								jsonArray.add("");
								resultJson.put("DELIVERYLIST", jsonArray);
							}
							return resultJson;
						}
						//imti salesdelivery end
						

						private JSONObject getViewContactDetailsEdit(HttpServletRequest request) {
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();
							PlantMstUtil plantmstutil = new PlantMstUtil();
							
							try {
								String PLANT= StrUtils.fString(request.getParameter("PLANT"));
								String conid= StrUtils.fString(request.getParameter("ID"));
								request.getSession().setAttribute("RESULT","");
								boolean mesflag = false;
								ArrayList  movQryList = plantmstutil.getContactDet(PLANT,conid);
								if (movQryList.size() > 0) {
									for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
										int sno=iCnt+1;
										String result="";
										String dummy="";
										String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
										Map lineArr = (Map) movQryList.get(iCnt);
										JSONObject resultJsonInt = new JSONObject();
										String leaddate= StrUtils.fString((String)lineArr.get("LEADDATE")) ;
										String lifecycle = StrUtils.fString((String)lineArr.get("LIFECYCLESTAGE")) ;
										String leadstatus = StrUtils.fString((String)lineArr.get("LEADSTATUS")) ;
										String note = StrUtils.fString((String)lineArr.get("NOTE")) ;
										
										result += "<tr valign=\"middle\">"  
												+ "<td  width='10%' align = center>"+ dummy +"</td>"
												+ "<td  width='10%' align = left>"  + sno + "</td>"
												+ "<td  width='20%' align = left>"  + leaddate + "</td>"
												+ "<td  width='20%' align = left>"  + lifecycle + "</td>"
												+ "<td  width='20%' align = left>"  + leadstatus + "</td>"
												+ "<td  width='20%' align = left>"  + note + "</td>"
												+ "</tr>" ;
										resultJsonInt.put("CONTACTMASTERDATA", result);
										jsonArray.add(resultJsonInt);
										
									}
									resultJson.put("contactmaster", jsonArray);
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
									resultJsonInt.put("ERROR_CODE", "100");
									jsonArrayErr.add(resultJsonInt);
									resultJson.put("errors", jsonArrayErr);
									
								} else {
									JSONObject resultJsonInt = new JSONObject();
									resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
									resultJsonInt.put("ERROR_CODE", "99");
									jsonArrayErr.add(resultJsonInt);
									jsonArray.add("");
									resultJson.put("contactmaster", jsonArray);
									resultJson.put("errors", jsonArrayErr);
								}
							} catch (Exception e) {
								resultJson.put("SEARCH_DATA", "");
								resultJson.put("TOTAL_QTY", 0);
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
								resultJsonInt.put("ERROR_CODE", "98");
								jsonArrayErr.add(resultJsonInt);
								resultJson.put("ERROR", jsonArrayErr); 
							}
							return resultJson;
						}
						
						//RESVI start USER SUMMARY
						private JSONObject getUserListforCompany(HttpServletRequest request) throws Exception{
							JSONObject resultJson = new JSONObject();
							JSONArray jsonArray = new JSONArray();
							JSONArray jsonArrayErr = new JSONArray();			
							userBean _userBean      = new userBean();
							StrUtils strUtils = new StrUtils();
							Hashtable ht = new Hashtable();
							List invQryList = new ArrayList();
							try {
							String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			/*
			 * String COMPANY= StrUtils.fString(request.getParameter("COMPANY")); String
			 * GROUP= StrUtils.fString(request.getParameter("GROUP")); String USER=
			 * StrUtils.fString(request.getParameter("USER"));
			 */
							String COMPANY = strUtils.fString(request.getParameter("COMPANY"));
							String GROUP = strUtils.fString(request.getParameter("GROUP"));
							String USER = strUtils.fString(request.getParameter("USER"));
							HttpSession session = request.getSession();
							String systatus = session.getAttribute("SYSTEMNOW").toString();
							request.getSession().setAttribute("RESULT","");
							boolean mesflag = false;
							if(systatus.equalsIgnoreCase("INVENTORY")) {
								invQryList = _userBean.getUserListforCompany(ht,PLANT,COMPANY,USER,GROUP);
								}else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
									invQryList = _userBean.getUserListforCompanyAcc(ht,PLANT,COMPANY,USER,GROUP);
								}else if(systatus.equalsIgnoreCase("PAYROLL")) {
									invQryList = _userBean.getUserListforCompanyPay(ht,PLANT,COMPANY,USER,GROUP);
								}
//							ArrayList invQryList = _userBean.getUserListforCompany(ht,PLANT,COMPANY,USER,GROUP);
							if (invQryList.size() > 0) {
							for(int i =0; i<invQryList.size(); i++) {
							Map arrCustLine = (Map)invQryList.get(i);
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("DEPT", (String)arrCustLine.get("DEPT"));
							resultJsonInt.put("USER_ID", (String)arrCustLine.get("USER_ID"));
							resultJsonInt.put("USER_NAME", (String)arrCustLine.get("USER_NAME"));
							resultJsonInt.put("UI_PKEY", (String)arrCustLine.get("UI_PKEY"));
							resultJsonInt.put("USER_LEVEL_PAYROLL", (String)arrCustLine.get("USER_LEVEL_PAYROLL"));
							//resvi
							resultJsonInt.put("USER_LEVEL_ACCOUNTING", (String)arrCustLine.get("USER_LEVEL_ACCOUNTING"));
							resultJsonInt.put("USER_LEVEL", (String)arrCustLine.get("USER_LEVEL"));
                            //ends
							jsonArray.add(resultJsonInt);
							}
							resultJson.put("USERLEVEL", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
							}else {
								JSONObject resultJsonInt = new JSONObject();
				                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				                resultJsonInt.put("ERROR_CODE", "99");
				                jsonArrayErr.add(resultJsonInt);
				                resultJson.put("errors", jsonArrayErr);  
							}

							} catch (Exception e) {
							resultJson.put("SEARCH_DATA", "");
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
							resultJsonInt.put("ERROR_CODE", "98");
							jsonArrayErr.add(resultJsonInt);
							resultJson.put("ERROR", jsonArrayErr);
							jsonArray.add("");
							resultJson.put("USERLEVEL", jsonArray);
							}
							return resultJson;
							}
						//RESVI end USER
						
						
				
						
						
						//RESVI start USER ACCESS RIGHTS SEARCH
						private JSONObject getUserAccessRightsGroup(HttpServletRequest request) throws Exception{
						JSONObject resultJson = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						JSONArray jsonArrayErr = new JSONArray();
						userBean _userBean = new userBean();
						StrUtils strUtils = new StrUtils();
						Hashtable ht = new Hashtable();
						List invQryList = new ArrayList();
						try {
						String PLANT= StrUtils.fString(request.getParameter("PLANT"));
						String COMPANY = strUtils.fString(request.getParameter("COMPANY"));
						String GROUP = strUtils.fString(request.getParameter("GROUP"));
						String USER = strUtils.fString(request.getParameter("USER"));
						HttpSession session = request.getSession();
						String systatus = session.getAttribute("SYSTEMNOW").toString();

						if (strUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
						if (strUtils.fString(GROUP).length() > 0)
						ht.put("LEVEL_NAME", GROUP);

						request.getSession().setAttribute("RESULT","");
						boolean mesflag = false;
						if(systatus.equalsIgnoreCase("INVENTORY")) {
						invQryList = _userBean.getGroupListSummary(ht, PLANT, GROUP);
						}else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
						invQryList = _userBean.getGroupListSummaryAccounting(ht, PLANT, GROUP);
						}else if(systatus.equalsIgnoreCase("PAYROLL")) {
						invQryList = _userBean.getGroupListSummarypayroll(ht, PLANT, GROUP);
						}
						if (invQryList.size() > 0) {
						for(int i =0; i<invQryList.size(); i++) {
						Map arrCustLine = (Map)invQryList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("DEPT", (String)arrCustLine.get("DEPT"));
						resultJsonInt.put("USER_NAME", (String)arrCustLine.get("USER_NAME"));
						resultJsonInt.put("USER_LEVEL_PAYROLL", (String)arrCustLine.get("USER_LEVEL_PAYROLL"));
						resultJsonInt.put("USER_LEVEL_ACCOUNTING", (String)arrCustLine.get("USER_LEVEL_ACCOUNTING"));
						resultJsonInt.put("LEVEL_NAME", (String)arrCustLine.get("LEVEL_NAME"));
						jsonArray.add(resultJsonInt);
						}
						resultJson.put("USERLEVEL", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
						}else {
							JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
			                resultJsonInt.put("ERROR_CODE", "99");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);  
						}
						} catch (Exception e) {
						resultJson.put("SEARCH_DATA", "");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
						resultJsonInt.put("ERROR_CODE", "98");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("ERROR", jsonArrayErr);
						jsonArray.add("");
						resultJson.put("USERLEVEL", jsonArray);
						}
						return resultJson;
						}
						//RESVI end USER ACCESS RIGHTS SEARCH
						
	
						
	//imtiziaf start customer
		private JSONObject getViewCustomerDetails(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			CustUtil custUtils = new CustUtil(); 
			try {
			String CUSTOMERTYPE= StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String CUSTOMERNAME= StrUtils.fString(request.getParameter("CUSTOMERNAME"));
			
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList movQryList =custUtils.getCustomerListWithType(CUSTOMERNAME,PLANT,CUSTOMERTYPE);

			if (movQryList.size() > 0) {
			for(int i =0; i<movQryList.size(); i++) {
			Map arrCustLine = (Map)movQryList.get(i);
			String customerType=(String)arrCustLine.get("CUSTOMER_TYPE_ID");
			if(customerType.equals(""))
			{
				customerType="NOCUSTOMERTYPE";
			}			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("CUSTNO", (String)arrCustLine.get("CUSTNO"));
			resultJsonInt.put("CNAME", (String)arrCustLine.get("CNAME"));
			resultJsonInt.put("CUSTOMERTYPE",customerType);
			resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
			resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
			resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
		
			
			jsonArray.add(resultJsonInt);
			}
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}else {
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}

			} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}
			return resultJson;
			}
		//imtiziaf end customer
		
		private JSONObject getViewPeppolCustomerDetails(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			CustUtil custUtils = new CustUtil(); 
			try {
				String CUSTOMERTYPE= StrUtils.fString(request.getParameter("CUSTOMERTYPE"));
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String CUSTOMERNAME= StrUtils.fString(request.getParameter("CUSTOMERNAME"));
				
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				ArrayList movQryList =custUtils.getPeppolCustomerListWithType(CUSTOMERNAME,PLANT,CUSTOMERTYPE);
				
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						String customerType=(String)arrCustLine.get("CUSTOMER_TYPE_ID");
						if(customerType.equals(""))
						{
							customerType="NOCUSTOMERTYPE";
						}			
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("CUSTNO", (String)arrCustLine.get("CUSTNO"));
						resultJsonInt.put("CNAME", (String)arrCustLine.get("CNAME"));
						resultJsonInt.put("CUSTOMERTYPE",customerType);
						resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
						resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
						resultJsonInt.put("PEPPOL_ID", (String)arrCustLine.get("PEPPOL_ID"));
						
						
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}
			return resultJson;
		}
		
		//imtiziaf start activitys
		private JSONObject getViewActivityDetails(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			Hashtable ht = new Hashtable();
			HTReportUtil movHisUtil       = new HTReportUtil();
			try {
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
				String TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
				String DIRTYPE = StrUtils.fString(request.getParameter("DIRTYPE"));
				String JOBNO   = StrUtils.fString(request.getParameter("JOBNO"));
				String USER    = StrUtils.fString(request.getParameter("USER"));
				String ITEMNO  = StrUtils.fString(request.getParameter("ITEM"));
				String sItemDesc  = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("DESC")));
				String ORDERNO   =  StrUtils.fString(request.getParameter("ORDERNO"));
				String CUSTOMER= StrUtils.fString(request.getParameter("CUSTOMER"));
				String BATCH   = StrUtils.fString(request.getParameter("BATCH"));
				String LOC     = StrUtils.fString(request.getParameter("LOC"));
				String REASONCODE     = StrUtils.fString(request.getParameter("REASONCODE"));
				String constkey 	 = StrUtils.fString(request.getParameter("DIRTYPE"));
				String LOC_TYPE_ID =  StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
				String LOC_TYPE_ID2 =  StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
				String LOC_TYPE_ID3 =  StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
				String TYPE =  StrUtils.fString(request.getParameter("TYPE"));
				String PRD_CLS_ID =  StrUtils.fString(request.getParameter("PRD_CLS_ID"));
				String PRD_TYPE_ID =  StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_BRAND_ID =  StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
				String PRD_DEPT_ID =  StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
				String REMARKS =  StrUtils.fString(request.getParameter("REMARKS"));
				
				if(ITEMNO.length() > 0) {
			         ItemMstUtil itemMstUtil = new ItemMstUtil();
			         itemMstUtil.setmLogger(mLogger);
			         try{
			         String temItem = itemMstUtil.isValidAlternateItemInItemmst(PLANT, ITEMNO);
			         ITEMNO = temItem;
			         }catch(Exception e){
			                 
			         }
			         }
			  
			        if(StrUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
			        if(StrUtils.fString(USER).length() > 0)          ht.put("CRBY",USER);
			        if(StrUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
			        if(StrUtils.fString(CUSTOMER).length() > 0)        ht.put("CUSTNO",CUSTOMER);
			        if(StrUtils.fString(ORDERNO).length() > 0)         ht.put("ORDNUM",ORDERNO);
			        if(StrUtils.fString(BATCH).length() > 0)           ht.put("BATNO",BATCH);
				
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;

				ArrayList movQryList =movHisUtil.getMovHisListWithRemarks(ht,PLANT,FROM_DATE,TO_DATE,USER,sItemDesc,REASONCODE,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,DIRTYPE,TYPE,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,REMARKS);
				
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						 Map lineArr = (Map) movQryList.get(i);
						 
						 String trDate= "",TRANDATE="";
	                     trDate=(String)lineArr.get("CRAT");
	                     TRANDATE=(String)lineArr.get("TRANDATE");
	 
	                     if (trDate.length()>8) {
	                    	 trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
	                     }

	                     if(TRANDATE.contains("-"))
	                     {
	                    	 TRANDATE = TRANDATE.substring(8,10)+"/"+ TRANDATE.substring(5,7)+"/"+TRANDATE.substring(0,4);
	                     }
	  
	                   int iIndex = i + 1;
	                   
						JSONObject resultJsonInt = new JSONObject();
//						resultJsonInt.put("SNO", iIndex);
						resultJsonInt.put("DATE", TRANDATE);
						resultJsonInt.put("TIME",trDate);
						resultJsonInt.put("LOGTYPE", (String)arrCustLine.get("DIRTYPE"));
						resultJsonInt.put("ORDERNO", (String)arrCustLine.get("ORDNUM"));
						resultJsonInt.put("LOC", (String)arrCustLine.get("LOC"));
						resultJsonInt.put("PRDID", (String)arrCustLine.get("ITEM"));
						resultJsonInt.put("DESC", (String)arrCustLine.get("ITEMDESC"));
						resultJsonInt.put("BATCH", (String)arrCustLine.get("BATNO"));
						resultJsonInt.put("QTY", StrUtils.formatNum((String)arrCustLine.get("QTY")));
						resultJsonInt.put("USER", (String)arrCustLine.get("CRBY"));
						resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
						
						
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}
			return resultJson;
		}
		//imtiziaf end activitys
		
//		//imtiziaf start product
		private JSONObject queryItemMstForSearchCriteriaNew(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			ItemUtil itemUtil = new ItemUtil();
			CustUtil custUtils = new CustUtil(); 
			
			try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String PRODUCT= StrUtils.fString(request.getParameter("PRODUCT"));
			String PRODUCTDESC= StrUtils.fString(request.getParameter("PRODUCTDESC"));
			String PRODUCTCLS= StrUtils.fString(request.getParameter("PRODUCTCLS"));
			String PRODUCTTYPE= StrUtils.fString(request.getParameter("PRODUCTTYPE"));
			String PRODUCTBRAND= StrUtils.fString(request.getParameter("PRODUCTBRAND"));
			String PRODUCTDEPT= StrUtils.fString(request.getParameter("PRODUCTDEPT"));
			String STOCKTYPE= StrUtils.fString(request.getParameter("STOCKTYPE"));
			int start= Integer.valueOf(StrUtils.fString(request.getParameter("start")));
			int end = Integer.valueOf(StrUtils.fString(request.getParameter("end")));
			
			
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			List movQryList = itemUtil.queryItemMstForSearchCriteriaNew(PRODUCT,PRODUCTDESC,PRODUCTCLS,PRODUCTTYPE,PRODUCTBRAND,PRODUCTDEPT,STOCKTYPE,PLANT,"",start,end);
			if (movQryList.size() > 0) {
			for(int i =0; i<movQryList.size(); i++) {
			Vector arrCustLine = (Vector)movQryList.get(i);
						
			
			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("17", (String)arrCustLine.get(17));
			resultJsonInt.put("ITEM", (String)arrCustLine.get(0));
			resultJsonInt.put("ITEMDESC", (String)arrCustLine.get(1));
			resultJsonInt.put("CATALOG", (String)arrCustLine.get(39));
			resultJsonInt.put("3", (String)arrCustLine.get(3));
			resultJsonInt.put("10", (String)arrCustLine.get(10));
			resultJsonInt.put("2", (String)arrCustLine.get(2));
			resultJsonInt.put("19", (String)arrCustLine.get(19));
			resultJsonInt.put("13", (String)arrCustLine.get(13));
			resultJsonInt.put("COST", (String)arrCustLine.get(13));
			resultJsonInt.put("12", (String)arrCustLine.get(12));
			resultJsonInt.put("PRICE", (String)arrCustLine.get(12));
			resultJsonInt.put("21", (String)arrCustLine.get(21));
			resultJsonInt.put("8", (String)arrCustLine.get(8));
			resultJsonInt.put("11", (String)arrCustLine.get(11));
			resultJsonInt.put("40", (String)arrCustLine.get(40));
			resultJsonInt.put("30", (String)arrCustLine.get(30));
		
			
			jsonArray.add(resultJsonInt);
			}
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}else {
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}

			} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}
			return resultJson;
			}
		//imtiziaf end product
		
		
		//imtiziaf start supplier
		private JSONObject getVendorListStartsWithName(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			CustUtil custUtils = new CustUtil(); 
			try {
			String SUPPLIERTYPE= StrUtils.fString(request.getParameter("SUPPLIERTYPE"));
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String SUPPLIERNAME= StrUtils.fString(request.getParameter("SUPPLIERNAME"));
			if(SUPPLIERTYPE.equalsIgnoreCase("NOCUSTOMERTYPE"))
					SUPPLIERTYPE="";
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList movQryList = custUtils.getVendorListsWithName(SUPPLIERNAME, PLANT," AND ISNULL(SUPPLIER_TYPE_ID,'') LIKE  '%"+SUPPLIERTYPE+"%'");

			if (movQryList.size() > 0) {
			for(int i =0; i<movQryList.size(); i++) {
			Map arrCustLine = (Map)movQryList.get(i);
						
			
			String supplierType=(String)arrCustLine.get("SUPPLIER_TYPE_ID");
			if(supplierType.equals(""))
			{
				supplierType="NOCUSTOMERTYPE";
			}			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("VENDNO", (String)arrCustLine.get("VENDNO"));
			resultJsonInt.put("VNAME", (String)arrCustLine.get("VNAME"));
			resultJsonInt.put("SUPPLIERTYPE",supplierType);
			resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
			resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
			resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
		
			
			jsonArray.add(resultJsonInt);
			}
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}else {
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}

			} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}
			return resultJson;
			}
		//imtiziaf end supplier
		//thanz
		private JSONObject getShipperListStartsWithName(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			ShipperUtil custUtils = new ShipperUtil(); 
			try {
			//	String SUPPLIERTYPE= StrUtils.fString(request.getParameter("SUPPLIERTYPE"));
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String SHIPPERNAME= StrUtils.fString(request.getParameter("SHIPPERNAME"));
				/*
				 * if(SUPPLIERTYPE.equalsIgnoreCase("NOCUSTOMERTYPE")) SUPPLIERTYPE="";
				 */
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				ArrayList movQryList = custUtils.getShipperListsWithName(SHIPPERNAME, PLANT," AND FREIGHT_FORWARDERNAME LIKE  '%"+ "" +"%'");
				
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						
						
						/*
						 * String supplierType=(String)arrCustLine.get("SUPPLIER_TYPE_ID");
						 * if(supplierType.equals("")) { supplierType="NOCUSTOMERTYPE"; }
						 */		
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("FREIGHT_FORWARDERNO", (String)arrCustLine.get("FREIGHT_FORWARDERNO"));
						resultJsonInt.put("FREIGHT_FORWARDERNAME", (String)arrCustLine.get("FREIGHT_FORWARDERNAME"));
						resultJsonInt.put("LICENCENO", (String)arrCustLine.get("LICENCENO"));
					//	resultJsonInt.put("LICENCENO",supplierType);
						resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
						resultJsonInt.put("WORKPHONE", (String)arrCustLine.get("WORKPHONE"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("IsActive"));
						
						
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}
			return resultJson;
		}
		//thanz end supplier

		private JSONObject getPeppolVendorListStartsWithName(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			CustUtil custUtils = new CustUtil(); 
			try {
				String SUPPLIERTYPE= StrUtils.fString(request.getParameter("SUPPLIERTYPE"));
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String SUPPLIERNAME= StrUtils.fString(request.getParameter("SUPPLIERNAME"));
				if(SUPPLIERTYPE.equalsIgnoreCase("NOCUSTOMERTYPE"))
					SUPPLIERTYPE="";
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				String con ="";
				if(SUPPLIERTYPE.length() > 0) {
					con = "AND SUPPLIER_TYPE_ID LIKE '%" + SUPPLIERTYPE + "%'";
				}
				ArrayList movQryList = custUtils.getPeppolVendorListsWithName(SUPPLIERNAME, PLANT,SUPPLIERTYPE,con);
				
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						
						
						String supplierType=(String)arrCustLine.get("SUPPLIER_TYPE_ID");
						if(supplierType.equals(""))
						{
							supplierType="NOCUSTOMERTYPE";
						}			
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("VENDNO", (String)arrCustLine.get("VENDNO"));
						resultJsonInt.put("VNAME", (String)arrCustLine.get("VNAME"));
						resultJsonInt.put("SUPPLIERTYPE",supplierType);
						resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
						resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
						resultJsonInt.put("PEPPOL_ID", (String)arrCustLine.get("PEPPOL_ID"));
						
						
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("CUSTOMERTYPELIST", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("CUSTOMERTYPELIST", jsonArray);
			}
			return resultJson;
		}
		
		
		private JSONObject getOutletListStartsWithName(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			OutletUtil outletUtils = new OutletUtil(); 
			try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String OUTLETNAME= StrUtils.fString(request.getParameter("OUTLETNAME"));
			boolean mesflag = false;
			ArrayList movQryList = outletUtils.getOutletListsWithName(OUTLETNAME, PLANT,"");

			if (movQryList.size() > 0) {
			for(int i =0; i<movQryList.size(); i++) {
			Map arrCustLine = (Map)movQryList.get(i);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("OUTLET", (String)arrCustLine.get("OUTLET"));
			resultJsonInt.put("OUTLET_NAME", (String)arrCustLine.get("OUTLET_NAME"));
			resultJsonInt.put("CONTACT_PERSON", (String)arrCustLine.get("CONTACT_PERSON"));
			resultJsonInt.put("DESGINATION", (String)arrCustLine.get("DESGINATION"));
			resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
		
			
			jsonArray.add(resultJsonInt);
			}
			resultJson.put("OUTLETLIST", jsonArray);
			}else {
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("OUTLETLIST", jsonArray);
			}

			} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("OUTLETLIST", jsonArray);
			}
			return resultJson;
			}
		
		//		CREATED BY imthi
		//		CREATED ON 14-04-2022
		private JSONObject getProductPromotion(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			PosItemPromotionHdrDAO posItemPromotionHdrDAO = new PosItemPromotionHdrDAO();
			try {
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String prm= StrUtils.fString(request.getParameter("PRM"));
				String prmdesc= StrUtils.fString(request.getParameter("PRMDESC"));
				String OUTLET_CODE= StrUtils.fString(request.getParameter("OUTLET_CODE"));
				boolean mesflag = false;
//				ArrayList movQryList = posItemPromotionHdrDAO.getProductPromotionsWithName(OUTLET_CODE,prm,prmdesc,PLANT," ORDER BY PROMOTION_NAME asc");				
				ArrayList movQryList = posItemPromotionHdrDAO.getProductPromotionsWithName(OUTLET_CODE,prm,prmdesc,PLANT," GROUP BY PROMOTION_NAME,PROMOTION_DESC,CUSTOMER_TYPE_ID,START_DATE,START_TIME,END_DATE,END_TIME,IsActive;");				

				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("OUTLET_NAME", (String)arrCustLine.get("OUTLET_NAME"));
						resultJsonInt.put("PROMOTION", (String)arrCustLine.get("PROMOTION_NAME"));
						resultJsonInt.put("PROMOTIONDESC", (String)arrCustLine.get("PROMOTION_DESC"));
						resultJsonInt.put("CUSTYPE", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
						resultJsonInt.put("START", (String)arrCustLine.get("START_DATE")+" - "+(String)arrCustLine.get("START_TIME"));
						resultJsonInt.put("END", (String)arrCustLine.get("END_DATE")+" - "+(String)arrCustLine.get("END_TIME"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("IsActive"));
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("PRDPROMOTION", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("PRDPROMOTION", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("PRDPROMOTION", jsonArray);
			}
			return resultJson;
		}
		private JSONObject getCategoryPromotion(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			CategoryPromotionHdrDAO categoryPromotionHdrDAO = new CategoryPromotionHdrDAO();
			try {
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String prm= StrUtils.fString(request.getParameter("PRM"));
				String prmdesc= StrUtils.fString(request.getParameter("PRMDESC"));
				String OUTLET_CODE= StrUtils.fString(request.getParameter("OUTLET_CODE"));
				boolean mesflag = false;
				ArrayList movQryList = categoryPromotionHdrDAO.getProductPromotionsWithName(OUTLET_CODE,prm,prmdesc,PLANT," ORDER BY PROMOTION_NAME asc");
				
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("OUTLET_NAME", (String)arrCustLine.get("OUTLET_NAME"));
						resultJsonInt.put("PROMOTION", (String)arrCustLine.get("PROMOTION_NAME"));
						resultJsonInt.put("PROMOTIONDESC", (String)arrCustLine.get("PROMOTION_DESC"));
						resultJsonInt.put("CUSTYPE", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
						resultJsonInt.put("START", (String)arrCustLine.get("START_DATE")+" - "+(String)arrCustLine.get("START_TIME"));
						resultJsonInt.put("END", (String)arrCustLine.get("END_DATE")+" - "+(String)arrCustLine.get("END_TIME"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("IsActive"));
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("PRDPROMOTION", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("PRDPROMOTION", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("PRDPROMOTION", jsonArray);
			}
			return resultJson;
		}
		private JSONObject getBrandPromotion(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			BrandPromotionHdrDAO brandPromotionHdrDAO = new BrandPromotionHdrDAO();
			try {
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String prm= StrUtils.fString(request.getParameter("PRM"));
				String prmdesc= StrUtils.fString(request.getParameter("PRMDESC"));
				String OUTLET_CODE= StrUtils.fString(request.getParameter("OUTLET_CODE"));
				boolean mesflag = false;
				ArrayList movQryList = brandPromotionHdrDAO.getProductPromotionsWithName(OUTLET_CODE,prm,prmdesc,PLANT," ORDER BY PROMOTION_NAME asc");
				
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("OUTLET_NAME", (String)arrCustLine.get("OUTLET_NAME"));
						resultJsonInt.put("PROMOTION", (String)arrCustLine.get("PROMOTION_NAME"));
						resultJsonInt.put("PROMOTIONDESC", (String)arrCustLine.get("PROMOTION_DESC"));
						resultJsonInt.put("CUSTYPE", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
						resultJsonInt.put("START", (String)arrCustLine.get("START_DATE")+" - "+(String)arrCustLine.get("START_TIME"));
						resultJsonInt.put("END", (String)arrCustLine.get("END_DATE")+" - "+(String)arrCustLine.get("END_TIME"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("IsActive"));
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("PRDPROMOTION", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("PRDPROMOTION", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("PRDPROMOTION", jsonArray);
			}
			return resultJson;
		}
		
		private JSONObject getPromotionData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
	               boolean mesflag = false;
	               PosItemPromotionHdrDAO posItemPromotionHdrDAO = new PosItemPromotionHdrDAO();
	               ArrayList arrCust = posItemPromotionHdrDAO.getProductPromotionsWithName(QUERY,PLANT," ORDER BY PROMOTION_NAME asc");
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   	JSONObject resultJsonInt = new JSONObject();
		               	resultJsonInt.put("PROMOTION", (String)arrCustLine.get("PROMOTION_NAME"));
						resultJsonInt.put("PROMOTIONDESC", (String)arrCustLine.get("PROMOTION_DESC"));
						resultJsonInt.put("CUSTYPE", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
						resultJsonInt.put("START", (String)arrCustLine.get("START_DATE")+" - "+(String)arrCustLine.get("START_TIME"));
						resultJsonInt.put("END", (String)arrCustLine.get("END_DATE")+" - "+(String)arrCustLine.get("END_TIME"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("IsActive"));
	                   jsonArray.add(resultJsonInt);
	               }
	               		resultJson.put("PRDPROMOTION", jsonArray);
			            JSONObject resultJsonInt = new JSONObject();
			            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			            resultJsonInt.put("ERROR_CODE", "100");
			            jsonArrayErr.add(resultJsonInt);
			            resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
	        	} catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		//END
		
		private JSONObject getCategoryPromotionData(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();			
			
			try {
				String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String QUERY= StrUtils.fString(request.getParameter("QUERY"));
				request.getSession().setAttribute("RESULT","");
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				boolean mesflag = false;
				///////////////////////
				CategoryPromotionHdrDAO categoryPromotionHdrDAO = new CategoryPromotionHdrDAO();
				ArrayList arrCust = categoryPromotionHdrDAO.getProductPromotionsWithName(QUERY,PLANT," ORDER BY PROMOTION_NAME asc");
				if (arrCust.size() > 0) {
					for(int i =0; i<arrCust.size(); i++) {
						Map arrCustLine = (Map)arrCust.get(i);
						
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("PROMOTION", (String)arrCustLine.get("PROMOTION_NAME"));
						resultJsonInt.put("PROMOTIONDESC", (String)arrCustLine.get("PROMOTION_DESC"));
						resultJsonInt.put("CUSTYPE", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
						resultJsonInt.put("START", (String)arrCustLine.get("START_DATE")+" - "+(String)arrCustLine.get("START_TIME"));
						resultJsonInt.put("END", (String)arrCustLine.get("END_DATE")+" - "+(String)arrCustLine.get("END_TIME"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("IsActive"));
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("PRDPROMOTION", jsonArray);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "99");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);  
				}
			} catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
		
		private JSONObject getBrandPromotionData(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();			
			
			try {
				String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String QUERY= StrUtils.fString(request.getParameter("QUERY"));
				request.getSession().setAttribute("RESULT","");
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				boolean mesflag = false;
				///////////////////////
				BrandPromotionHdrDAO brandPromotionHdrDAO = new BrandPromotionHdrDAO();
				ArrayList arrCust = brandPromotionHdrDAO.getProductPromotionsWithName(QUERY,PLANT," ORDER BY PROMOTION_NAME asc");
				if (arrCust.size() > 0) {
					for(int i =0; i<arrCust.size(); i++) {
						Map arrCustLine = (Map)arrCust.get(i);
						
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("PROMOTION", (String)arrCustLine.get("PROMOTION_NAME"));
						resultJsonInt.put("PROMOTIONDESC", (String)arrCustLine.get("PROMOTION_DESC"));
						resultJsonInt.put("CUSTYPE", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
						resultJsonInt.put("START", (String)arrCustLine.get("START_DATE")+" - "+(String)arrCustLine.get("START_TIME"));
						resultJsonInt.put("END", (String)arrCustLine.get("END_DATE")+" - "+(String)arrCustLine.get("END_TIME"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("IsActive"));
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("PRDPROMOTION", jsonArray);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "99");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);  
				}
			} catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
	
		//		CREATED BY imthi
		//		CREATED ON 30-03-2022
		private JSONObject getOutletTerminalListStartsWithName(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			OutletUtil outletUtils = new OutletUtil(); 
			try {
				String PLANT= StrUtils.fString(request.getParameter("PLANT"));
				String OUTLETNAME= StrUtils.fString(request.getParameter("OUTLETNAME"));
				String TERMINALNAME= StrUtils.fString(request.getParameter("TERMINALNAME"));
				boolean mesflag = false;
				ArrayList movQryList = outletUtils.getOutletTerminalListsWithName(OUTLETNAME,TERMINALNAME, PLANT,"");
				
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("TERMINAL", (String)arrCustLine.get("TERMINAL"));
						resultJsonInt.put("TERMINAL_NAME", (String)arrCustLine.get("TERMINAL_NAME"));
						resultJsonInt.put("OUTLET", (String)arrCustLine.get("OUTLET"));
						resultJsonInt.put("OUTLET_NAME", (String)arrCustLine.get("OUTLET_NAME"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("OUTLETLIST", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
					jsonArray.add("");
					resultJson.put("OUTLETLIST", jsonArray);
				}
				
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
				jsonArray.add("");
				resultJson.put("OUTLETLIST", jsonArray);
			}
			return resultJson;
		}
		
		
		private JSONObject getOutletTerminalData(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			
			try {
				String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String QUERY= StrUtils.fString(request.getParameter("QUERY"));
				String ONAME= StrUtils.fString(request.getParameter("ONAME"));
				request.getSession().setAttribute("RESULT","");
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				boolean mesflag = false;
				OutletUtil outletUtils = new OutletUtil();
				ArrayList arrCust = outletUtils.getOutletTerminalListsWithName(QUERY,PLANT," ORDER BY TERMINAL_NAME asc");
				if(ONAME.equals("")) {
					arrCust = outletUtils.getOutletTerminalListsWithName(QUERY,PLANT," ORDER BY TERMINAL_NAME asc");	
				}else {
					arrCust = outletUtils.getOutletTerminalListsWithOutlet(ONAME,PLANT," ORDER BY TERMINAL_NAME asc");
				}
				if (arrCust.size() > 0) {
					for(int i =0; i<arrCust.size(); i++) {
						Map arrCustLine = (Map)arrCust.get(i);
						
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("TERMINAL", (String)arrCustLine.get("TERMINAL"));
						resultJsonInt.put("TERMINAL_NAME", (String)arrCustLine.get("TERMINAL_NAME"));
						resultJsonInt.put("OUTLET", (String)arrCustLine.get("OUTLET"));
						resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("POSOUTLETS", jsonArray);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "99");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);  
				}
			} catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
		//END
		
				
	private JSONObject getVendorDetails(HttpServletRequest request) {
		String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		String SUPPLIERCODE= StrUtils.fString(request.getParameter("SUPPLIERCODE"));
		JSONObject resultJson = new JSONObject();
     	TransportModeDAO transportmodedao = new TransportModeDAO();
		String transportmode = "",transport = "";
		try {
			
			CustUtil custUtil = new CustUtil();
			ArrayList arrCust = custUtil.getVendorDetails(SUPPLIERCODE,
					PLANT);
			transport = (String) arrCust.get(38);
    		int trans = Integer.valueOf(transport);
    		if(trans > 0){
    			transportmode = transportmodedao.getTransportModeById(PLANT,trans);
    		}
    	else{
    		transportmode = "";
    	}
			resultJson.put("ERROR_CODE", "100");
			resultJson.put("SUPPLIER_EMAIL", arrCust.get(12));
			resultJson.put("TRANSPORTID", (String)arrCust.get(38));
		    resultJson.put("TRANSPORTNAME", transportmode);
		    resultJson.put("PAYMENT_TERMS", arrCust.get(17));
		    resultJson.put("PAY_TERMS", arrCust.get(39));
		}catch(Exception e) {
			resultJson.put("ERROR_MESSAGE", e.getMessage());
			resultJson.put("ERROR_CODE", "98");
		}
		return resultJson;
	}
	
	private JSONObject getStockTransId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		JSONObject resultJson = new JSONObject();
		TblControlDAO _TblControlDAO = new TblControlDAO();
		String PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
		String UserId = (String) session.getAttribute("LOGIN_USER");
		String cmdAction = StrUtils.fString(request.getParameter("URL"));
		String tran = StrUtils.fString(request.getParameter("TRAN"));
//		String url = request.getRequestURL().toString();
		String stockId = "";
			try {
				if(!cmdAction.contains("?")) {
					stockId = _TblControlDAO.getNextOrder(PLANT, UserId, "STOCKMOVE");
				}else {
					stockId = tran;
				}
				resultJson.put("ERROR_CODE", "100");
				resultJson.put("TRANID", stockId);
			}catch(Exception e) {
				resultJson.put("ERROR_MESSAGE", e.getMessage());
				resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
	
	//IMTI START SUPPLIERType
	private JSONObject getSupplierTypeSummary(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CustUtil _CustUtil = new CustUtil();
		try {
		String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		String SUPPLIERTYPEID= StrUtils.fString(request.getParameter("SUPPLIERTYPE"));
		request.getSession().setAttribute("RESULT","");
		boolean mesflag = false;
		ArrayList movQryList =  _CustUtil.getSupplierTypeSummary(SUPPLIERTYPEID,PLANT,"");

		if (movQryList.size() > 0) {
		for(int i =0; i<movQryList.size(); i++) {
		Map arrCustLine = (Map)movQryList.get(i);
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("SUPPLIER_TYPE_ID", (String)arrCustLine.get("SUPPLIER_TYPE_ID"));
		resultJsonInt.put("SUPPLIER_TYPE_DESC", (String)arrCustLine.get("SUPPLIER_TYPE_DESC"));
		resultJsonInt.put("ISACTIVE", (String)arrCustLine.get("ISACTIVE"));
		
		jsonArray.add(resultJsonInt);
		}
		resultJson.put("SUPPLIERTYPELIST", jsonArray);
        JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);
		}else {
			JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
            resultJsonInt.put("ERROR_CODE", "99");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("errors", jsonArrayErr);  
		}

		} catch (Exception e) {
		resultJson.put("SEARCH_DATA", "");
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
		resultJsonInt.put("ERROR_CODE", "98");
		jsonArrayErr.add(resultJsonInt);
		resultJson.put("ERROR", jsonArrayErr);
		jsonArray.add("");
		resultJson.put("SUPPLIERTYPELIST", jsonArray);
		}
		return resultJson;
		}
	//imtiziaf end SUPPLIERType
	
	//imtiziaf employee
	
	private JSONObject getViewEmployeeNameDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		EmployeeUtil custUtil = new EmployeeUtil();
		try {
		String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		String CUSTOMERNAME= StrUtils.fString(request.getParameter("CUSTOMERNAME"));
		request.getSession().setAttribute("RESULT","");
		boolean mesflag = false;
		ArrayList movQryList =  custUtil.getEmployeeListStartsWithName(CUSTOMERNAME,PLANT);

		if (movQryList.size() > 0) {
		for(int i =0; i<movQryList.size(); i++) {
		Map arrCustLine = (Map)movQryList.get(i);
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("EMPNO", (String)arrCustLine.get("EMPNO"));
		resultJsonInt.put("FNAME", (String)arrCustLine.get("FNAME"));
		resultJsonInt.put("DESGINATION", (String)arrCustLine.get("DESGINATION"));
		resultJsonInt.put("GENDER", (String)arrCustLine.get("GENDER"));
		resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));       
		resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
		resultJsonInt.put("EMIRATESID", (String)arrCustLine.get("EMIRATESID"));
		resultJsonInt.put("DATEOFJOINING", (String)arrCustLine.get("DATEOFJOINING"));       
		resultJsonInt.put("DEPT", (String)arrCustLine.get("DEPT"));
		jsonArray.add(resultJsonInt);
		}
		resultJson.put("CUSTOMERTYPELIST", jsonArray);
		}else {
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
		resultJsonInt.put("ERROR_CODE", "98");
		jsonArrayErr.add(resultJsonInt);
		resultJson.put("ERROR", jsonArrayErr);
		jsonArray.add("");
		resultJson.put("CUSTOMERTYPELIST", jsonArray);
		}

		} catch (Exception e) {
		resultJson.put("SEARCH_DATA", "");
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
		resultJsonInt.put("ERROR_CODE", "98");
		jsonArrayErr.add(resultJsonInt);
		resultJson.put("ERROR", jsonArrayErr);
		jsonArray.add("");
		resultJson.put("CUSTOMERTYPELIST", jsonArray);
		}
		return resultJson;
		}
	 
	// imtiziaf employee end
	
	
	/**************************REMARKS***********************************************************************************************************/
	private String addremarks(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String  PLANT="",Remarks="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				Remarks = StrUtils.fString(request.getParameter("REMARKS"));
				
				if (!_MasterUtil.isExistREMARKS(Remarks, PLANT)) // if the REMARKS exists already 17.10.18 by Azees
				{
					
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.REMARKS,Remarks);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddRemarks(ht);
											
			if(insertflag){
				resultJson.put("status", "100");
				msg = "<font class='maingreen'>Remarks Added Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Remarks</font>";
					
				}
				}
				else
				{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Remarks Exists already</font>";
				}
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}

	private JSONObject getViewRemarksDetailsEdit(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getRemarksList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String remarks = StrUtils.fString((String)lineArr.get("REMARKS")) ;
                           	chkString =autoid+","+remarks;
                           	
                           	
                              result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + remarks + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("REMARKSMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("remarksmaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("remarksmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr); 
        }
        return resultJson;
}
	
	
	private JSONObject getRemarksDetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
	    //MasterDAO _MasterDAO = new MasterDAO();
      try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getRemarksList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        String remarks = StrUtils.fString((String)lineArr.get("REMARKS")) ;
	                        chkString =autoid+","+remarks;
	                       	result += "<tr valign=\"middle\" >"  
	                            		//+ "<td width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
	                            		+ "<td width='5%' align = center>"  + id + "</td>"
	                            		+ "<td width='75%' align = left>"  + remarks + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("REMARKSMASTERDATA", result);
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("remarksmaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("remarksmaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	
		
	private JSONObject getViewRemarksDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getRemarksList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String remarks = StrUtils.fString((String)lineArr.get("REMARKS")) ;
                           	chkString =autoid+","+remarks;
                               result += "<tr valign=\"middle\">"  
                            		//+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + remarks + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("REMARKSMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("remarksmaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("remarksmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	private String DeleteRemarks(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String REMARKS = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					REMARKS = itemArray[1];
					  System.out.println(" REMARKS"+ REMARKS);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					//ht.put(IDBConstants.REMARKS,REMARKS);
					
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_REMARKS);
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",REMARKS);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deleteRemarks(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Remarks Deleted Successfully");
			response.sendRedirect("../remarks/edit?action=result&REMARKS="+REMARKS);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Remarks:"+REMARKS);
			response.sendRedirect("../remarks/edit?action=resulterror&REMARKS="+REMARKS);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../remarks/edit?action=catchrerror&REMARKS="+REMARKS);
			throw e;
		}

		return msg;
	}
	
	/**************************TRANSPORT MODE****************************************************************************************/
	
//	Resvi Starts getTransportModeDetails
	private JSONObject getTransportModeDetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
	    //MasterDAO _MasterDAO = new MasterDAO();
      try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getTransportModeList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        String transportmode = StrUtils.fString((String)lineArr.get("TRANSPORT_MODE")) ;
	                        chkString =autoid+","+transportmode;
	                       	result += "<tr valign=\"middle\" >"  
	                            		//+ "<td width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
	                            		+ "<td width='5%' align = center>"  + id + "</td>"
	                            		+ "<td width='75%' align = left>"  + transportmode + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("TRANSPORTMASTERDATA", result);
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("transportmaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("transportmaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	
//	ends
	
	
	private JSONObject getViewTransportMode(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getTransportModeList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String transportmode = StrUtils.fString((String)lineArr.get("TRANSPORT_MODE")) ;
                           	chkString =autoid+","+transportmode;
                               result += "<tr valign=\"middle\">"  
//                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + transportmode + "</td>"
                            	+ "</tr>" ;
                               resultJsonInt.put("TRANSPORTMASTERDATA", result);
                               jsonArray.add(resultJsonInt);

                     }
                         resultJson.put("transportmaster", jsonArray);
                         JSONObject resultJsonInt = new JSONObject();
                         resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                         resultJsonInt.put("ERROR_CODE", "100");
                         jsonArrayErr.add(resultJsonInt);
                         resultJson.put("errors", jsonArrayErr);
                                          
                   } else {
                         JSONObject resultJsonInt = new JSONObject();
                         resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                         resultJsonInt.put("ERROR_CODE", "99");
                         jsonArrayErr.add(resultJsonInt);
                         jsonArray.add("");
                         resultJson.put("transportmaster", jsonArray);
                         resultJson.put("errors", jsonArrayErr);
                 }
             } catch (Exception e) {
                     resultJson.put("SEARCH_DATA", "");
                     resultJson.put("TOTAL_QTY", 0);
                     JSONObject resultJsonInt = new JSONObject();
                     resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                     resultJsonInt.put("ERROR_CODE", "98");
                     jsonArrayErr.add(resultJsonInt);
                     resultJson.put("ERROR", jsonArrayErr); 
             }
             return resultJson;
     }
	
	
//	Resvi Starts GetViewTransportMode
	
	private JSONObject getViewTransportModeEdit(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getTransportModeList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String transportmode = StrUtils.fString((String)lineArr.get("TRANSPORT_MODE")) ;
                           	chkString =autoid+","+transportmode;
                           	
                           	
                              result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + transportmode + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("TRANSPORTMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("transportmaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("transportmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr); 
        }
        return resultJson;
}

//Ends
	
	
//	Resvi Starts ADDTransportMode
	
	private String addtransportmode(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			TransportModeUtil transportModeUtil = new TransportModeUtil();
			String msg = "",tranId="",plant="",TranDesc="",userName="";
			String  PLANT="",TRANSPORT_MODE="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				String transportmode = StrUtils.fString(request.getParameter("TRANSPORT_MODE"));
				
				if (!_MasterUtil.isExistTRANSPORTMODE(transportmode, PLANT)) // if the REMARKS exists already 17.10.18 by Azees
//				if (!(transportModeUtil.isExistsItemType(ht)))
				{
					
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.TRANSPORT_MODE,transportmode);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				
				
				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable<String, String> htm = new Hashtable<String, String>();
				htm.put(IDBConstants.PLANT, PLANT);
				htm.put("DIRTYPE", TransactionConstants.ADD_TRANSPORT_MODE);
				htm.put("REMARKS", transportmode);
				htm.put("RECID", "");
				htm.put("ORDNUM","");
				htm.put("ITEM","");
				htm.put("UPBY", UserId);
				htm.put("CRBY", UserId);
				htm.put("CRAT", DateUtils.getDateTime());
				htm.put("UPAT", DateUtils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				
//				boolean insertflag = _MasterUtil.AddTransportMode(ht);
				boolean itemInserted = transportModeUtil.insertTransportModeMst(ht);
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (itemInserted && inserted) {
				resultJson.put("status", "100");
				msg = "<font class='maingreen'>Transport Mode Added Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Transport Mode</font>";
					
				}
				}
				else
				{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Transport Mode Exists already</font>";
				}
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}
	
//	ENDS
	
//	Resvi Starts DeleteTransportMode
	private String DeleteTransportMode(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String TRANSPORT_MODE = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					TRANSPORT_MODE = itemArray[1];
					  System.out.println(" TRANSPORT_MODE"+ TRANSPORT_MODE);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					//ht.put(IDBConstants.REMARKS,REMARKS);
					
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_TRANSPORT_MODE);
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deleteTransportMode(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Transport Mode Deleted Successfully");
			response.sendRedirect("../transportmode/summary?action=result&TRANSPORT_MODE="+TRANSPORT_MODE);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Transport Mode:"+TRANSPORT_MODE);
			response.sendRedirect("../transportmode/summary?action=resulterror&TRANSPORT_MODE="+TRANSPORT_MODE);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../transportmode/summary?action=catchrerror&TRANSPORT_MODE="+TRANSPORT_MODE);
			throw e;
		}

		return msg;
	}
	
//	ends
	

	
	/**************************INCOTERMS***********************************************************************************************************/
	private String addincoterms(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String  PLANT="",IncoTerms="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				IncoTerms = StrUtils.fString(request.getParameter("INCOTERMS"));
				if (!_MasterUtil.isExistINCOTERM(IncoTerms, PLANT)) // if the INCOTERM exists already 17.10.18 by Azees
				{
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.INCOTERMS,IncoTerms);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddINCOTERMS(ht);
											
			if(insertflag){
				resultJson.put("status", "100");
				msg = "<font class='maingreen'>INCOTERM Added Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding INCOTERM</font>";
					
				}
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">INCOTERM Exists already</font>";					
				}
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}
	
	
	private JSONObject getViewINCOTERMSDetailsEdit(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getINCOTERMSList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String incoterms = StrUtils.fString((String)lineArr.get("INCOTERMS")) ;
                           	chkString =autoid+","+incoterms;
                               result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\" style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + incoterms + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("INCOTERMSMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("incotermsmaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("incotermsmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	private JSONObject getINCOTERMSDetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
      try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getINCOTERMSList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        String incoterms = StrUtils.fString((String)lineArr.get("INCOTERMS")) ;
	                        chkString =autoid+","+incoterms;
	                       	result += "<tr valign=\"middle\">"  
	                            		//+ "<td width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
	                            		+ "<td width='5%' align = center>"  + id + "</td>"
	                            		+ "<td width='75%' align = left>"  + incoterms + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("INCOTERMSMASTERDATA", result);
	                      
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("incotermsmaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("incotermsmaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	

	private JSONObject getViewINCOTERMSDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getINCOTERMSList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String incoterms = StrUtils.fString((String)lineArr.get("INCOTERMS")) ;
                           	chkString =autoid+","+incoterms;
                               result += "<tr valign=\"middle\">"  
                            		//+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\" style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + incoterms + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("INCOTERMSMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("incotermsmaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("incotermsmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	private String DeleteINCOTERMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String INCOTERMS = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					String item = chkd[i];
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					INCOTERMS = itemArray[1];
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					//ht.put(IDBConstants.INCOTERMS,INCOTERMS);
					
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_INCO_TERMS);
					//htm.put("RECID", "");
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",INCOTERMS);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
					
		            transactionHandler = _MasterDAO.deleteINCOTERMS(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	    
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","INCOTERM Deleted Successfully");
			response.sendRedirect("../incoterms/edit?action=result&INCOTERMS="+INCOTERMS);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting INCOTERM:"+INCOTERMS);
			response.sendRedirect("../incoterms/edit?action=resulterror&INCOTERMS="+INCOTERMS);
		}
	

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../incoterms/edit?action=catchrerror&INCOTERMS="+INCOTERMS);
			throw e;
		}

		return msg;
	}
	
	/**************************ShippingDetails***********************************************************************************************************/
	private String addshippingdetails(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String PLANT="",customername="",contactname="",telephone="",handphone="",fax="",email="",unitno="",building="",street="",city="",state="",country="",postalcode="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				customername  = StrUtils.fString(request.getParameter("CUSTOMERNAME"));
				contactname  = StrUtils.fString(request.getParameter("CONTACTNAME"));
				telephone  = StrUtils.fString(request.getParameter("TELEPHONE"));
				handphone = StrUtils.fString(request.getParameter("HANDPHONE"));
				fax  = StrUtils.fString(request.getParameter("FAX"));
				email  = StrUtils.fString(request.getParameter("EMAIL"));
				unitno  = StrUtils.fString(request.getParameter("UNITNO"));
				building  = StrUtils.fString(request.getParameter("BUILDING"));
				street = StrUtils.fString(request.getParameter("STREET"));
				city = StrUtils.fString(request.getParameter("CITY"));
				state = StrUtils.fString(request.getParameter("STATE"));
				country = StrUtils.fString(request.getParameter("COUNTRY"));
				postalcode  = StrUtils.fString(request.getParameter("POSTALCODE"));
									
				
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.CUSTOMERNAME,customername);
				ht.put(IDBConstants.CONTACTNAME, contactname);
				ht.put(IDBConstants.TELNO, telephone);
				ht.put(IDBConstants.HPNO,handphone);
				ht.put(IDBConstants.FAX,fax);
				ht.put(IDBConstants.EMAIL,email);
				ht.put(IDBConstants.UNITNO, unitno);
				ht.put(IDBConstants.BUILDING, building);
				ht.put(IDBConstants.STREET, street);
				ht.put(IDBConstants.CITY,city);
				ht.put(IDBConstants.STATE,state);
				ht.put(IDBConstants.COUNTRY, country);
				ht.put(IDBConstants.POSTALCODE, postalcode);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddShippingDetails(ht);
											
			if(insertflag){
				resultJson.put("status", "100");
				msg = "<font class = "+IDBConstants.SUCCESS_COLOR +">Shipping Details Added Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding ShippingDetails</font>";
					
				}
				
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}
	
	private JSONObject getshippingdetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
      try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getShippingDetailsList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String customername="",contactname="",telephone="",handphone="",fax="",email="",unitno="",building="",street="",city="",state="",country="",postalcode="";
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        customername= StrUtils.fString((String)lineArr.get("CUSTOMERNAME")) ;
	                      	contactname  = StrUtils.fString((String)lineArr.get("CONTACTNAME"));
	        				telephone  = StrUtils.fString((String)lineArr.get("TELEPHONE"));
	        				handphone = StrUtils.fString((String)lineArr.get("HANDPHONE"));
	        				fax  = StrUtils.fString((String)lineArr.get("FAX"));
	        				email  = StrUtils.fString((String)lineArr.get("EMAIL"));
	        				unitno  = StrUtils.fString((String)lineArr.get("UNITNO"));
	        				building  = StrUtils.fString((String)lineArr.get("BUILDING"));
	        				street = StrUtils.fString((String)lineArr.get("STREET"));
	        				city = StrUtils.fString((String)lineArr.get("CITY"));
	        				state =StrUtils.fString((String)lineArr.get("STATE"));
	        				country = StrUtils.fString((String)lineArr.get("COUNTRY"));
	        				postalcode  = StrUtils.fString((String)lineArr.get("POSTALCODE"));
	                        chkString =autoid+","+customername+","+contactname+","+telephone+","+handphone+","+fax+","+email+","+unitno+
	                        		   ","+building+","+street+","+city+","+state+","+country+","+postalcode;
	                       	result += "<tr valign=\"middle\">"  
	                            		+ "<td width='4%' align = left><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
	                            		+ "<td width='4%' align = left>"  + id + "</td>"
	                            		+ "<td width='10%' align = left>"  + customername + "</td>"
	                            		+ "<td width='10%' align = left>"  + contactname + "</td>"
	                            		+ "<td width='8%' align = left>"  + telephone + "</td>"
	                            		+ "<td width='8%' align = left>"  + handphone + "</td>"
	                            		+ "<td width='8%' align = left>"  + fax + "</td>"
	                            		+ "<td width='8%' align = left>"  + email + "</td>"
	                            		+ "<td width='8%' align = left>"  + unitno + "</td>"
	                            		+ "<td width='8%' align = left>"  + building + "</td>"
	                            		+ "<td width='10%' align = left>"  + street + "</td>"
	                            		+ "<td width='8%' align = left>"  + city + "</td>"
	                            		+ "<td width='8%' align = left>"  + state + "</td>"
	                            		+ "<td width='7%' align = left>"  + country + "</td>"
	                            		+ "<td width='6%' align = left>"  + postalcode + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("SHIPPINGDETAILSMASTERDATA", result);
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("shippingdetailsmaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("shippingdetailsmaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	
	
	private JSONObject getviewshippingdetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getShippingDetailsList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String customername="",contactname="",telephone="",handphone="",fax="",email="",unitno="",building="",street="",city="",state="",country="",postalcode="";
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        customername= StrUtils.fString((String)lineArr.get("CUSTOMERNAME")) ;
	                      	contactname  = StrUtils.fString((String)lineArr.get("CONTACTNAME"));
	        				telephone  = StrUtils.fString((String)lineArr.get("TELEPHONE"));
	        				handphone = StrUtils.fString((String)lineArr.get("HANDPHONE"));
	        				fax  = StrUtils.fString((String)lineArr.get("FAX"));
	        				email  = StrUtils.fString((String)lineArr.get("EMAIL"));
	        				unitno  = StrUtils.fString((String)lineArr.get("UNITNO"));
	        				building  = StrUtils.fString((String)lineArr.get("BUILDING"));
	        				street = StrUtils.fString((String)lineArr.get("STREET"));
	        				city = StrUtils.fString((String)lineArr.get("CITY"));
	        				state =StrUtils.fString((String)lineArr.get("STATE"));
	        				country = StrUtils.fString((String)lineArr.get("COUNTRY"));
	        				postalcode  = StrUtils.fString((String)lineArr.get("POSTALCODE"));
	                        chkString =autoid+","+customername+","+contactname+","+telephone+","+handphone+","+fax+","+email+","+unitno+
	                        		   ","+building+","+street+","+city+","+state+","+country+","+postalcode;
	                        result += "<tr valign=\"middle\">"  
                            		+ "<td  width='4%' align = left><INPUT Type=\"Checkbox\"  class=\"form-check-input\" style=\"border:0;  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='4%' align = left>"  + id + "</td>"
                            		+ "<td width='10%' align = left>"  + customername + "</td>"
                            		+ "<td width='10%' align = left>"  + contactname + "</td>"
                            		+ "<td width='8%' align = left>"  + telephone + "</td>"
                            		+ "<td width='8%' align = left>"  + handphone + "</td>"
                            		+ "<td width='8%' align = left>"  + fax + "</td>"
                            		+ "<td width='8%' align = left>"  + email + "</td>"
                            		+ "<td width='8%' align = left>"  + unitno + "</td>"
                            		+ "<td width='8%' align = left>"  + building + "</td>"
                            		+ "<td width='10%' align = left>"  + street + "</td>"
                            		+ "<td width='8%' align = left>"  + city + "</td>"
                            		+ "<td width='8%' align = left>"  + state + "</td>"
                            		+ "<td width='7%' align = left>"  + country + "</td>"
                            		+ "<td width='6%' align = left>"  + postalcode + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("SHIPPINGDETAILSMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("shippingdetailsmaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("shippingdetailsmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	

	/**************************PAYMENTTYPE***********************************************************************************************************/
	private String addpaymenttype(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String  PLANT="",PaymenType="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			MasterDAO _MasterDAO = new MasterDAO();
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				PaymenType = StrUtils.fString(request.getParameter("PAYMENTTYPE"));
				
				if (!_MasterUtil.isExistPaymentType(PaymenType, PLANT)) // if the PaymenType exists already 17.10.18 by Azees
				{
					
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.PAYMENTTYPE,PaymenType);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddPaymentType(ht);
				
				
											
			if(insertflag){
				resultJson.put("status", "100");
				msg="<font class='maingreen'>Payment Type Added Successfully</font>";
//				msg = "Payment Type Added Successfully";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Payment Type</font>";
					
				}
				}
				else
				{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Payment Type Exists already</font>";
				}
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}
	private String DeletePaymentType(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String PAYMENTTYPE = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					PAYMENTTYPE = itemArray[1];
					  System.out.println(" PAYMENTTYPE"+ PAYMENTTYPE);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					//ht.put(IDBConstants.REMARKS,REMARKS);
					
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_PAYMENTTYPE);
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",PAYMENTTYPE);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deletePaymentType(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Payment Type Deleted Successfully");
			response.sendRedirect("../paymenttype/edit?action=result&PAYMENTTYPE="+PAYMENTTYPE);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Payment Type:"+PAYMENTTYPE);
			response.sendRedirect("../paymenttype/edit?action=resulterror&PAYMENTTYPE="+PAYMENTTYPE);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../paymenttype/edit?action=catchrerror&PAYMENTTYPE="+PAYMENTTYPE);
			throw e;
		}

		return msg;
	}
	
	private JSONObject getPaymentTypeDetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
	    //MasterDAO _MasterDAO = new MasterDAO();
      try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getPaymentTypeList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        String paymenttytpe = StrUtils.fString((String)lineArr.get("PAYMENTTYPE")) ;
	                        chkString =autoid+","+paymenttytpe;
	                       	result += "<tr valign=\"middle\" >"  
	                            		//+ "<td width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
	                            		+ "<td width='5%' align = center>"  + id + "</td>"
	                            		+ "<td width='75%' align = left>"  + paymenttytpe + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("PAYMENTTYPEMASTERDATA", result);
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("paymenttypemaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("paymenttypemaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	
		
	private JSONObject getViewPaymentTypeDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getPaymentTypeList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String paymenttype = StrUtils.fString((String)lineArr.get("PAYMENTTYPE")) ;
                           	chkString =autoid+","+paymenttype;
                               result += "<tr valign=\"middle\">"  
                            		//+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + paymenttype + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("PAYMENTTYPEMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("paymenttypemaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("paymenttypemaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	
	
	private JSONObject getViewPaymentTypeDetailsEdit(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getPaymentTypeList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String paymenttype = StrUtils.fString((String)lineArr.get("PAYMENTTYPE")) ;
                           	chkString =autoid+","+paymenttype;
                           	
                           	
                              result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + paymenttype + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("PAYMENTTYPEMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("paymenttypemaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("paymenttypemaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr); 
        }
        return resultJson;
}

	
	
	/**************************PAYMENTMODE***********************************************************************************************************/
	private String addpaymentmode(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String  PLANT="",PaymenType="",paymentmodeserial="0";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			PaymenType = StrUtils.fString(request.getParameter("PAYMENTMODE"));
			paymentmodeserial = StrUtils.fString(request.getParameter("PAYMENTMODESERIAL"));
			
			if (!_MasterUtil.isExistPaymentMode(PaymenType, PLANT)) // if the PaymenType exists already 17.10.18 by Azees
			{
				
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.PAYMENTMODE,PaymenType);
				ht.put("PAYMENTMODESERIAL",paymentmodeserial);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddPaymentMode(ht);
				
				
				
				if(insertflag){
					resultJson.put("status", "100");
					msg="<font class='maingreen'>Payment Mode Added Successfully</font>";
//				msg = "Payment Type Added Successfully";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Payment Mode</font>";
					
				}
			}
			else
			{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Payment Mode Exists already</font>";
			}
			
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
	private String DeletePaymentMode(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String PAYMENTTYPE = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					PAYMENTTYPE = itemArray[1];
					  System.out.println(" PAYMENTMODE"+ PAYMENTTYPE);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					//ht.put(IDBConstants.REMARKS,REMARKS);
					
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_PAYMENTMODE);
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",PAYMENTTYPE);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deletePaymentMode(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Payment Mode Deleted Successfully");
			response.sendRedirect("../PaymentMode/edit?action=result&PAYMENTMODE="+PAYMENTTYPE);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Payment Mode:"+PAYMENTTYPE);
			response.sendRedirect("../PaymentMode/edit?action=resulterror&PAYMENTMODE="+PAYMENTTYPE);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../PaymentMode/edit?action=catchrerror&PAYMENTMODE="+PAYMENTTYPE);
			throw e;
		}

		return msg;
	}
	
	private JSONObject getPaymentModeDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		// String PaymenMode="";
	//	PaymentModeMstDAO paymentModeMstDAO = new PaymentModeMstDAO();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
		//	PaymenMode = StrUtils.fString(request.getParameter("PAYMENTMODE"));
			 ArrayList  movQryList = _MasterUtil.getPaymentModeList(PLANT,"");
		//	ArrayList  movQryList = PaymentModeDAO.IsPaymentModeMstExists(PLANT,"");
			// if (!paymentModeMstDAO.IsPaymentModeMstExists(PLANT,PaymenMode))
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="",chkString="";
					String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String paymenttytpe = StrUtils.fString((String)lineArr.get("PAYMENTMODE")) ;
					chkString =autoid+","+paymenttytpe;
					result += "<tr valign=\"middle\" >"  
							//+ "<td width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
							+ "<td width='5%' align = center>"  + id + "</td>"
							+ "<td width='75%' align = left>"  + paymenttytpe + "</td>"
							+ "</tr>" ;
					resultJsonInt.put("PAYMENTMODEMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("paymentmodemaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("paymentmodemaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getViewPaymentModeDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList  movQryList = _MasterUtil.getPaymentModeList(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="",chkString="";
					String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String paymentmodeserial= StrUtils.fString((String)lineArr.get("PAYMENTMODESERIAL")) ;
					String paymenttype = StrUtils.fString((String)lineArr.get("PAYMENTMODE")) ;
					chkString =autoid+","+paymenttype;
					result += "<tr valign=\"middle\">"  
							//+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
							+ "<td  width='5%' align = center>"  + paymentmodeserial + "</td>"
							+ "<td  width='75%' align = left>"  + paymenttype + "</td>"
							+ "</tr>" ;
					resultJsonInt.put("PAYMENTMODEMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("paymentmodemaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
				
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("paymentmodemaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getViewPaymentModeDetailsEdit(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList  movQryList = _MasterUtil.getPaymentModeList(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="",chkString="";
					String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String paymentmodeserial= StrUtils.fString((String)lineArr.get("PAYMENTMODESERIAL")) ;
					String paymenttype = StrUtils.fString((String)lineArr.get("PAYMENTMODE")) ;
					chkString =autoid+","+paymenttype;
					
					
					result += "<tr valign=\"middle\">"  
							+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
							+ "<td  width='5%' align = center>"  + paymentmodeserial + "</td>"
							+ "<td  width='75%' align = left>"  + paymenttype + "</td>"
							+ "</tr>" ;
					resultJsonInt.put("PAYMENTMODEMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("paymentmodemaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
				
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("paymentmodemaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr); 
		}
		return resultJson;
	}
	
	
	/**************************PAYMENTTERM***********************************************************************************************************/
	
	private String addpaymentterm(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String  PLANT="",PaymenType="",nodays="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			PaymenType = StrUtils.fString(request.getParameter("PAYMENT_TERMS"));
			nodays = StrUtils.fString(request.getParameter("NO_DAYS"));
			
			if (!_MasterUtil.isExistPaymentTerm(PaymenType, PLANT)) 
			{
				
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.PAYMENT_TERMS,PaymenType);
				ht.put("NO_DAYS", nodays);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddPaymentTerm(ht);
				
				
				
				if(insertflag){
					resultJson.put("status", "100");
					msg="<font class='maingreen'>Payment Term Added Successfully</font>";
//				msg = "Payment Type Added Successfully";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Payment Term</font>";
					
				}
			}
			else
			{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Payment Term Exists already</font>";
			}
			
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
	private JSONObject getPaymentTermDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			 ArrayList  movQryList = _MasterUtil.getPaymentTermList(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="",chkString="";
					String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String paymenttytpe = StrUtils.fString((String)lineArr.get("PAYMENT_TERMS")) ;
					String nodays = StrUtils.fString((String)lineArr.get("NO_DAYS")) ;
					chkString =autoid+","+paymenttytpe+","+nodays;
					result += "<tr valign=\"middle\" >"  
							//+ "<td width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
							+ "<td width='5%' align = center>"  + id + "</td>"
							+ "<td width='75%' align = left>"  + paymenttytpe + "</td>"
							+ "<td width='75%' align = left>"  + nodays + "</td>"
							+ "</tr>" ;
					resultJsonInt.put("PAYMENTTERMMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("paymenttermmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("paymenttermmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	
	private JSONObject getViewPaymentTermDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList  movQryList = _MasterUtil.getPaymentTermList(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="",chkString="";
					String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String paymenttype = StrUtils.fString((String)lineArr.get("PAYMENT_TERMS")) ;
					String nodays = StrUtils.fString((String)lineArr.get("NO_DAYS")) ;
					chkString =autoid+","+paymenttype+","+nodays;
					result += "<tr valign=\"middle\">"  
							//+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
							+ "<td  width='5%' align = center>"  + id + "</td>"
							+ "<td  width='75%' align = left>"  + paymenttype + "</td>"
							+ "<td  width='75%' align = left>"  + nodays + "</td>"
							+ "</tr>" ;
					resultJsonInt.put("PAYMENTTERMMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("paymenttermmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
				
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("paymenttermmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getViewPaymentTermDetailsEdit(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList  movQryList = _MasterUtil.getPaymentTermList(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="",chkString="";
					String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String paymenttype = StrUtils.fString((String)lineArr.get("PAYMENT_TERMS")) ;
					String nodays = StrUtils.fString((String)lineArr.get("NO_DAYS")) ;
					chkString =autoid+","+paymenttype+","+nodays;
					
					
					result += "<tr valign=\"middle\">"  
							+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
							+ "<td  width='5%' align = center>"  + id + "</td>"
							+ "<td  width='75%' align = left>"  + paymenttype + "</td>"
							+ "<td  width='75%' align = left>"  + nodays + "</td>"
							+ "</tr>" ;
					resultJsonInt.put("PAYMENTTERMMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("paymenttermmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
				
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("paymenttermmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr); 
		}
		return resultJson;
	}
	
	
	private String DeletePaymentTerm(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String PAYMENTTYPE = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					PAYMENTTYPE = itemArray[1];
					  System.out.println(" PAYMENT_TERMS"+ PAYMENTTYPE);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					//ht.put(IDBConstants.REMARKS,REMARKS);
					
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_PAYMENTTERMS);
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",PAYMENTTYPE);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deletePaymentTerm(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Payment Term Deleted Successfully");
			response.sendRedirect("../Payterm/edit?action=result&PAYMENT_TERMS="+PAYMENTTYPE);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Payment Term:"+PAYMENTTYPE);
			response.sendRedirect("../Payterm/edit?action=resulterror&PAYMENT_TERMS="+PAYMENTTYPE);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../Payterm/edit?action=catchrerror&PAYMENT_TERMS="+PAYMENTTYPE);
			throw e;
		}

		return msg;
	}
	
	
	/**************************HSCODE***********************************************************************************************************/
	private String addHSCODE(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String  PLANT="",hscode="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			MasterDAO _MasterDAO = new MasterDAO();
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				hscode = StrUtils.fString(request.getParameter("HSCODE"));
				
				if (!_MasterUtil.isExistHSCODE(hscode, PLANT)) // if the PaymenType exists already 17.10.18 by Azees
				{
					
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.HSCODE,hscode);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddHSCODE(ht);
				
				
											
			if(insertflag){
				resultJson.put("status", "100");
				msg = "<font class='maingreen'>HSCODE Added Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding HSCODE</font>";
					
				}
				}
				else
				{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">HSCODE Exists already</font>";
				}
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}
	
	private String addPOSDEPT(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String  PLANT="",hscode="",out="",dept="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			hscode = StrUtils.fString(request.getParameter("HSCODE"));
			out = StrUtils.fString(request.getParameter("OUTLET"));
			dept = StrUtils.fString(request.getParameter("DEPT"));
			
			if (!_OutletBeanDAO.isExistsdeptOutlet(out,dept,PLANT)) // if the PaymenType exists already 17.10.18 by Azees
			{
				
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
//				ht.put("ID",0);
				ht.put("OUTLET",out);
				ht.put("PRD_DEPT_ID",dept);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = false; 
				insertflag = _OutletBeanDAO.insertPosDept(ht);
				
				
				
				if(insertflag){
					resultJson.put("status", "100");
					msg = "<font class='maingreen' style='font-size: 18px;'>Department Configuration Saved Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Error in Saving Department Configuration</font>";
					
				}
			}
			else
			{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Department Configuration Already Exists</font>";
			}
			
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
	//Thanzith
	private String addPOSCLS(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String  PLANT="",hscode="",out="",dept="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			hscode = StrUtils.fString(request.getParameter("HSCODE"));
			out = StrUtils.fString(request.getParameter("OUTLET"));
			dept = StrUtils.fString(request.getParameter("CAT"));
			
			if (!_OutletBeanDAO.isExistsclsOutlet(out,dept,PLANT)) // if the PaymenType exists already 17.10.18 by Azees
			{
				
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
//				ht.put("ID",0);
				ht.put("OUTLET",out);
				ht.put("PRD_CLS_ID",dept);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = false; 
				insertflag = _OutletBeanDAO.insertPosCls(ht);
				
				
				
				if(insertflag){
					resultJson.put("status", "100");
					msg = "<font class='maingreen' style='font-size: 18px;'>Category Configuration Saved Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Error in Saving Category Configuration</font>";
					
				}
			}
			else
			{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Category Configuration Already Exists</font>";
			}
			
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
//End	
	
	private String delPOSPrd(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		String lvtid= StrUtils.fString(request.getParameter("ID"));
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			HttpSession session = request.getSession();
			String  PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			
				boolean deleteflag = false; 
				deleteflag = _OutletBeanDAO.DeletePrdtype(PLANT, Integer.valueOf(lvtid));
				if(deleteflag){
					resultJson.put("status", "100");
					msg = "<font class='mainred' style='font-size: 18px;'>Product Configuration Deleted Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Error in deleting Product Configuration</font>";
					
				}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
	private String delPOSDept(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		String lvtid= StrUtils.fString(request.getParameter("ID"));
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			HttpSession session = request.getSession();
			String  PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			
			boolean deleteflag = false; 
			deleteflag = _OutletBeanDAO.DeleteDepttype(PLANT, Integer.valueOf(lvtid));
			if(deleteflag){
				resultJson.put("status", "100");
				msg = "<font class='mainred' style='font-size: 18px;'>Department Configuration Deleted Successfully</font>";
			}
			else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Error in deleting Department Configuration</font>";
				
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
//Thanzith	
	private String delPOSCls(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		String lvtid= StrUtils.fString(request.getParameter("ID"));
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			HttpSession session = request.getSession();
			String  PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			
			boolean deleteflag = false; 
			deleteflag = _OutletBeanDAO.DeleteClstype(PLANT, Integer.valueOf(lvtid));
			if(deleteflag){
				resultJson.put("status", "100");
				msg = "<font class='mainred' style='font-size: 18px;'>Category Configuration Deleted Successfully</font>";
			}
			else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Error in deleting Category Configuration</font>";
				
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	//End
	
	private String addPOSPRD(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String  PLANT="",out="",prd="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		MasterDAO _MasterDAO = new MasterDAO();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			out = StrUtils.fString(request.getParameter("OUTLET"));
			prd = StrUtils.fString(request.getParameter("PRODUCT"));
			
			if (!_OutletBeanDAO.isExistsPrdOutlet(out,prd,PLANT)) // if the PaymenType exists already 17.10.18 by Azees
			{
				
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
//				ht.put("ID",0);
				ht.put("OUTLET",out);
				ht.put("ITEM",prd);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = false; 
				insertflag = _OutletBeanDAO.insertPosPrd(ht);
				
				
				
				if(insertflag){
					resultJson.put("status", "100");
					msg = "<font class='maingreen' style='font-size: 18px;'>Product Configuration Saved Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Error in Saving Product Configuration</font>";
					
				}
			}
			else
			{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +" style='font-size: 18px;'>Product Configuration Already Exists</font>";
			}
			
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
	private String DeleteHSCODE(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String HSCODE = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					HSCODE = itemArray[1];
					  System.out.println(" HSCODE"+ HSCODE);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					//ht.put(IDBConstants.REMARKS,REMARKS);
					
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_HSCODE);
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",HSCODE);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deleteHSCODE(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","HSCODE Deleted Successfully");
			response.sendRedirect("../hscode/edit?action=result&HSCODE="+HSCODE);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting HSCODE:"+HSCODE);
			response.sendRedirect("../hscode/edit?action=resulterror&HSCODE="+HSCODE);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../hscode/edit?action=catchrerror&HSCODE="+HSCODE);
			throw e;
		}

		return msg;
	}
	
	private JSONObject getHSCODEDetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
	    try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getHSCODEList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        String hscode = StrUtils.fString((String)lineArr.get("HSCODE")) ;
	                        chkString =autoid+","+hscode;
	                       	result += "<tr valign=\"middle\" >"  
	                            		+ "<td width='5%' align = center>"  + id + "</td>"
	                            		+ "<td width='75%' align = left>"  + hscode + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("HSCODEMASTERDATA", result);
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("hscodemaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("hscodemaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	
	private JSONObject getPOSDEPT(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String outlet= StrUtils.fString(request.getParameter("OUTLET"));
		 	   HttpSession session = request.getSession();
			   String systatus = session.getAttribute("SYSTEMNOW").toString();
			   String USERID= session.getAttribute("LOGIN_USER").toString();
			   boolean displaySummaryDelete = false;
			   userBean ub = new userBean();
			   if(systatus.equalsIgnoreCase("INVENTORY")) {
				   displaySummaryDelete = ub.isCheckValinv("postouchscreenconfigdelete", PLANT,USERID);   
			   }else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
				   displaySummaryDelete = ub.isCheckValAcc("postouchscreenconfigdelete", PLANT,USERID);   
			   }
//			ArrayList  movQryList = _OutletBeanDAO.getPosDept(PLANT,outlet);
			ArrayList  movQryList = _OutletBeanDAO.getPosDept(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String out = StrUtils.fString((String)lineArr.get("OUTLET")) ;
					String dept = StrUtils.fString((String)lineArr.get("PRD_DEPT_ID")) ;
					String outname = StrUtils.fString((String)lineArr.get("OUTNAME")) ;
					String deptname = StrUtils.fString((String)lineArr.get("PRDNAME")) ;
//					boolean dlt =  _OutletBeanDAO.DeleteDepttype(PLANT, id);
					if(displaySummaryDelete){
					result += "<tr valign=\"middle\" >"  
							+ "<td width='5%' align = left>"  + id + "</td>"
							+ "<td width='5%' align = left>"  + outname + "</td>"
							+ "<td width='5%' align = left>"  + dept + "</td>"
							+ "<td width='5%' align = left>"  + deptname + "</td>"
							+ "<td  width='5%' align = center>"  + "<a style=\" color: red;\" href=\"javascript:deletedeptpos("+autoid+")\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
							+ "</tr>" ;
					}else {
						result += "<tr valign=\"middle\" >"  
								+ "<td width='5%' align = left>"  + id + "</td>"
								+ "<td width='5%' align = left>"  + outname + "</td>"
								+ "<td width='5%' align = left>"  + dept + "</td>"
								+ "<td width='5%' align = left>"  + deptname + "</td>"
								+ "<td  width='5%' align = center>"  + "<a style=\"pointer-events: none; cursor: default; color: darkgrey;\" href=\"#\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
								+ "</tr>" ;
					}
					resultJsonInt.put("DEPTMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("deptmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("deptmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	
//Than	
	private JSONObject getPOSCLS(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String outlet= StrUtils.fString(request.getParameter("OUTLET"));
			HttpSession session = request.getSession();
			String systatus = session.getAttribute("SYSTEMNOW").toString();
			String USERID= session.getAttribute("LOGIN_USER").toString();
			boolean displaySummaryDelete = false;
			userBean ub = new userBean();
			if(systatus.equalsIgnoreCase("INVENTORY")) {
				displaySummaryDelete = ub.isCheckValinv("postouchscreenconfigdelete", PLANT,USERID);   
			}else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
				displaySummaryDelete = ub.isCheckValAcc("postouchscreenconfigdelete", PLANT,USERID);   
			}
//			ArrayList  movQryList = _OutletBeanDAO.getPosDept(PLANT,outlet);
			ArrayList  movQryList = _OutletBeanDAO.getPoscls(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String out = StrUtils.fString((String)lineArr.get("OUTLET")) ;
					String dept = StrUtils.fString((String)lineArr.get("PRD_CLS_ID")) ;
					String outname = StrUtils.fString((String)lineArr.get("OUTNAME")) ;
					String deptname = StrUtils.fString((String)lineArr.get("PRDNAME")) ;
//					boolean dlt =  _OutletBeanDAO.DeleteDepttype(PLANT, id);
					if(displaySummaryDelete){
						result += "<tr valign=\"middle\" >"  
								+ "<td width='5%' align = left>"  + id + "</td>"
								+ "<td width='5%' align = left>"  + outname + "</td>"
								+ "<td width='5%' align = left>"  + dept + "</td>"
								+ "<td width='5%' align = left>"  + deptname + "</td>"
								+ "<td  width='5%' align = center>"  + "<a style=\" color: red;\" href=\"javascript:deletedeptpos("+autoid+")\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
								+ "</tr>" ;
					}else {
						result += "<tr valign=\"middle\" >"  
								+ "<td width='5%' align = left>"  + id + "</td>"
								+ "<td width='5%' align = left>"  + outname + "</td>"
								+ "<td width='5%' align = left>"  + dept + "</td>"
								+ "<td width='5%' align = left>"  + deptname + "</td>"
								+ "<td  width='5%' align = center>"  + "<a style=\"pointer-events: none; cursor: default; color: darkgrey;\" href=\"#\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
								+ "</tr>" ;
					}
					resultJsonInt.put("CLASSMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("clsmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("clsmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getViewPosDeptDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String outlet= StrUtils.fString(request.getParameter("OUTLET"));
               request.getSession().setAttribute("RESULT","");
           	   HttpSession session = request.getSession();
			   String systatus = session.getAttribute("SYSTEMNOW").toString();
			   String USERID= session.getAttribute("LOGIN_USER").toString();
			   boolean displaySummaryDelete = false;
			   userBean ub = new userBean();
			   if(systatus.equalsIgnoreCase("INVENTORY")) {
				   displaySummaryDelete = ub.isCheckValinv("postouchscreenconfigdelete", PLANT,USERID);   
			   }else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
				   displaySummaryDelete = ub.isCheckValAcc("postouchscreenconfigdelete", PLANT,USERID);   
			   }
               boolean mesflag = false;
               ArrayList  movQryList = _OutletBeanDAO.getPosDept(PLANT,outlet);
//               ArrayList  movQryList = _OutletBeanDAO.getPosDept(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                    		String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
        					String out = StrUtils.fString((String)lineArr.get("OUTLET")) ;
        					String dept = StrUtils.fString((String)lineArr.get("PRD_DEPT_ID")) ;
        					String outname = StrUtils.fString((String)lineArr.get("OUTNAME")) ;
        					String deptname = StrUtils.fString((String)lineArr.get("PRDNAME")) ;
//        					boolean imt =  _OutletBeanDAO.DeleteDepttype(PLANT, id);
        					if(displaySummaryDelete){
                               result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = left>"  + id + "</td>"
                            		+ "<td  width='5%' align = left>"  + outname + "</td>"
                            		+ "<td  width='5%' align = left>"  + dept + "</td>"
                            		+ "<td  width='5%' align = left>"  + deptname + "</td>"
                            		+ "<td  width='5%' align = center>"  + "<a style=\" color: red;\" href=\"javascript:deletedeptpos("+autoid+")\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
                            	+ "</tr>" ;
        					}else {
        						result += "<tr valign=\"middle\">"  
                                		+ "<td  width='5%' align = left>"  + id + "</td>"
                                		+ "<td  width='5%' align = left>"  + outname + "</td>"
                                		+ "<td  width='5%' align = left>"  + dept + "</td>"
                                		+ "<td  width='5%' align = left>"  + deptname + "</td>"
                                		+ "<td  width='5%' align = center>"  + "<a style=\"pointer-events: none; cursor: default; color: darkgrey;\" href=\"#\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
                                	+ "</tr>" ;
        					}
                          resultJsonInt.put("DEPTMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("deptmaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("deptmaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
//Thanzith	
	private JSONObject getViewPosClsDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String outlet= StrUtils.fString(request.getParameter("OUTLET"));
			request.getSession().setAttribute("RESULT","");
			HttpSession session = request.getSession();
			String systatus = session.getAttribute("SYSTEMNOW").toString();
			String USERID= session.getAttribute("LOGIN_USER").toString();
			boolean displaySummaryDelete = false;
			userBean ub = new userBean();
			if(systatus.equalsIgnoreCase("INVENTORY")) {
				displaySummaryDelete = ub.isCheckValinv("postouchscreenconfigdelete", PLANT,USERID);   
			}else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
				displaySummaryDelete = ub.isCheckValAcc("postouchscreenconfigdelete", PLANT,USERID);   
			}
			boolean mesflag = false;
			ArrayList  movQryList = _OutletBeanDAO.getPosDept(PLANT,outlet);
//               ArrayList  movQryList = _OutletBeanDAO.getPosDept(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String out = StrUtils.fString((String)lineArr.get("OUTLET")) ;
					String dept = StrUtils.fString((String)lineArr.get("PRD_CLS_ID")) ;
					String outname = StrUtils.fString((String)lineArr.get("OUTNAME")) ;
					String deptname = StrUtils.fString((String)lineArr.get("PRDNAME")) ;
//        					boolean imt =  _OutletBeanDAO.DeleteDepttype(PLANT, id);
					if(displaySummaryDelete){
						result += "<tr valign=\"middle\">"  
								+ "<td  width='5%' align = left>"  + id + "</td>"
								+ "<td  width='5%' align = left>"  + outname + "</td>"
								+ "<td  width='5%' align = left>"  + dept + "</td>"
								+ "<td  width='5%' align = left>"  + deptname + "</td>"
								+ "<td  width='5%' align = center>"  + "<a style=\" color: red;\" href=\"javascript:deletedeptpos("+autoid+")\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
								+ "</tr>" ;
					}else {
						result += "<tr valign=\"middle\">"  
								+ "<td  width='5%' align = left>"  + id + "</td>"
								+ "<td  width='5%' align = left>"  + outname + "</td>"
								+ "<td  width='5%' align = left>"  + dept + "</td>"
								+ "<td  width='5%' align = left>"  + deptname + "</td>"
								+ "<td  width='5%' align = center>"  + "<a style=\"pointer-events: none; cursor: default; color: darkgrey;\" href=\"#\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
								+ "</tr>" ;
					}
					resultJsonInt.put("CLASSMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("clsmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
				
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("clsmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
//end	
	private JSONObject getPOSPRD(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String outlet= StrUtils.fString(request.getParameter("OUTLET"));
		 	   HttpSession session = request.getSession();
			   String systatus = session.getAttribute("SYSTEMNOW").toString();
			   String USERID= session.getAttribute("LOGIN_USER").toString();
			   boolean displaySummaryDelete = false;
			   userBean ub = new userBean();
			   if(systatus.equalsIgnoreCase("INVENTORY")) {
				   displaySummaryDelete = ub.isCheckValinv("postouchscreenconfigdelete", PLANT,USERID);   
			   }else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
				   displaySummaryDelete = ub.isCheckValAcc("postouchscreenconfigdelete", PLANT,USERID);   
			   }
//			ArrayList  movQryList = _OutletBeanDAO.getPosPrd(PLANT,outlet);
			ArrayList  movQryList = _OutletBeanDAO.getPosPrd(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String prd = StrUtils.fString((String)lineArr.get("ITEM")) ;
					String out = StrUtils.fString((String)lineArr.get("OUTLET")) ;
					String outname = StrUtils.fString((String)lineArr.get("OUTNAME")) ;
					String itemdesc = StrUtils.fString((String)lineArr.get("ITEMDESC")) ;
					String dept = StrUtils.fString((String)lineArr.get("PRDDEPT")) ;
					String cls = StrUtils.fString((String)lineArr.get("PRDCLS")) ;
					String type = StrUtils.fString((String)lineArr.get("PRDTYPE")) ;
					String typedesc = StrUtils.fString((String)lineArr.get("TYPEDESC")) ;
					if(displaySummaryDelete){
					result += "<tr valign=\"middle\" >"  
							+ "<td width='5%' align = left>"  + id + "</td>"
							+ "<td width='5%' align = left>"  + outname + "</td>"
							+ "<td width='5%' align = left>"  + prd + "</td>"
							+ "<td width='5%' align = left>"  + itemdesc + "</td>"
							+ "<td width='5%' align = left>"  + dept + "</td>"
							+ "<td width='5%' align = left>"  + cls + "</td>"
							+ "<td width='5%' align = left>"  + typedesc + "</td>"
							+ "<td  width='5%' align = center>"  + "<a style=\" color: red;\" href=\"javascript:deleteprdpos("+autoid+")\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
							+ "</tr>" ;
					}else {
						result += "<tr valign=\"middle\" >"  
								+ "<td width='5%' align = left>"  + id + "</td>"
								+ "<td width='5%' align = left>"  + outname + "</td>"
								+ "<td width='5%' align = left>"  + prd + "</td>"
								+ "<td width='5%' align = left>"  + itemdesc + "</td>"
								+ "<td width='5%' align = left>"  + dept + "</td>"
								+ "<td width='5%' align = left>"  + cls + "</td>"
								+ "<td width='5%' align = left>"  + typedesc + "</td>"
								+ "<td  width='5%' align = center>"  + "<a style=\"pointer-events: none; cursor: default; color: darkgrey;\" href=\"#\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
								+ "</tr>" ;
					}
					resultJsonInt.put("PRDMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("prdmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("prdmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	

	private JSONObject getViewPosPrdDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		OutletBeanDAO _OutletBeanDAO = new OutletBeanDAO();
		try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String outlet= StrUtils.fString(request.getParameter("OUTLET"));
			/*
			 * String item= StrUtils.fString(request.getParameter("ITEM")); String combine =
			 * outlet+","+item; if (combine.equals("")) { String[] OutItem =
			 * combine.split(","); String Out = OutItem [0]; String Item = OutItem [1]; if
			 * (Out.equals("")) { Out = item; }else { Out = outlet; } }
			 */
			
			request.getSession().setAttribute("RESULT","");
			boolean mesflag = false;
			ArrayList  movQryList = _OutletBeanDAO.getPosPrd(PLANT,outlet);
		 	   HttpSession session = request.getSession();
			   String systatus = session.getAttribute("SYSTEMNOW").toString();
			   String USERID= session.getAttribute("LOGIN_USER").toString();
			   boolean displaySummaryDelete = false;
			   userBean ub = new userBean();
			   if(systatus.equalsIgnoreCase("INVENTORY")) {
				   displaySummaryDelete = ub.isCheckValinv("postouchscreenconfigdelete", PLANT,USERID);   
			   }else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
				   displaySummaryDelete = ub.isCheckValAcc("postouchscreenconfigdelete", PLANT,USERID);   
			   }
//			ArrayList  movQryList = _OutletBeanDAO.getPosPrd(PLANT,"");
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					int id=iCnt+1;
					String result="";
					Map lineArr = (Map) movQryList.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
					String prd = StrUtils.fString((String)lineArr.get("ITEM")) ;
					String out = StrUtils.fString((String)lineArr.get("OUTLET")) ;
					String outname = StrUtils.fString((String)lineArr.get("OUTNAME")) ;
					String itemdesc = StrUtils.fString((String)lineArr.get("ITEMDESC")) ;
					String dept = StrUtils.fString((String)lineArr.get("PRDDEPT")) ;
					String cls = StrUtils.fString((String)lineArr.get("PRDCLS")) ;
					String type = StrUtils.fString((String)lineArr.get("PRDTYPE")) ;
					String typedesc = StrUtils.fString((String)lineArr.get("TYPEDESC")) ;
					if(displaySummaryDelete){
					result += "<tr valign=\"middle\" >"  
							+ "<td width='5%' align = left>"  + id + "</td>"
							+ "<td width='5%' align = left>"  + outname + "</td>"
							+ "<td width='5%' align = left>"  + prd + "</td>"
							+ "<td width='5%' align = left>"  + itemdesc + "</td>"
							+ "<td width='5%' align = left>"  + dept + "</td>"
							+ "<td width='5%' align = left>"  + cls + "</td>"
							+ "<td width='5%' align = left>"  + typedesc + "</td>"
							+ "<td  width='5%' align = center>"  + "<a style=\" color: red;\" href=\"javascript:deleteprdpos("+autoid+")\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
							+ "</tr>" ;
					}else {
						result += "<tr valign=\"middle\" >"  
								+ "<td width='5%' align = left>"  + id + "</td>"
								+ "<td width='5%' align = left>"  + outname + "</td>"
								+ "<td width='5%' align = left>"  + prd + "</td>"
								+ "<td width='5%' align = left>"  + itemdesc + "</td>"
								+ "<td width='5%' align = left>"  + dept + "</td>"
								+ "<td width='5%' align = left>"  + cls + "</td>"
								+ "<td width='5%' align = left>"  + typedesc + "</td>"
								+ "<td  width='5%' align = center>"  + "<a style=\"pointer-events: none; cursor: default; color: darkgrey;\" href=\"#\"><i class=\"fa fa-trash\"></i></a>" + "</td>"
								+ "</tr>" ;
					}
					resultJsonInt.put("PRDMASTERDATA", result);
					jsonArray.add(resultJsonInt);
					
				}
				resultJson.put("prdmaster", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
				
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("prdmaster", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			resultJson.put("TOTAL_QTY", 0);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
		
	private JSONObject getViewHSCODEDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getHSCODEList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String hscode = StrUtils.fString((String)lineArr.get("HSCODE")) ;
                           	chkString =autoid+","+hscode;
                               result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + hscode + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("HSCODEMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("hscodemaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("hscodemaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	
	
	private JSONObject getViewHSCODEDetailsEdit(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getHSCODEList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String hscode = StrUtils.fString((String)lineArr.get("HSCODE")) ;
                           	chkString =autoid+","+hscode;
                           	
                           	
                              result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + hscode + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("HSCODEMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("hscodemaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("hscodemaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr); 
        }
        return resultJson;
}
	
	
	/**************************COO***********************************************************************************************************/
	private String addCOO(HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException,Exception {
			JSONObject resultJson = new JSONObject();
			String msg = "";
			String  PLANT="",coo="",result="";
			ArrayList alResult = new ArrayList();
			Map map = null;
			MasterDAO _MasterDAO = new MasterDAO();
			try {
	            HttpSession session = request.getSession();
     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
     			result = StrUtils.fString((String) request.getAttribute("result")).trim();
				String UserId = (String) session.getAttribute("LOGIN_USER");
				coo = StrUtils.fString(request.getParameter("COO"));
				
				if (!_MasterUtil.isExistCOO(coo, PLANT)) 
				{
					result="";
    			Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,PLANT);
				ht.put(IDBConstants.COO,coo);
				ht.put(IDBConstants.LOGIN_USER,UserId);
				ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				boolean insertflag = _MasterUtil.AddCOO(ht);
				
				
											
			if(insertflag){
				resultJson.put("status", "100");
				msg = "<font class='maingreen'>COO Added Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding COO</font>";
					
				}
				}
				else
				{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">COO Exists already</font>";
				}
				
				
				if(!result.equalsIgnoreCase("")) {
					
					msg = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">"+result+"</font>";
					}
							
			}catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
							
			}
			return msg;
		}
	private String DeleteCOO(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String COO = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	 
	   
	   
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					
					String item = chkd[i];
					  System.out.println(" chkd chkd"+ item);
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					COO = itemArray[1];
					  System.out.println(" COO"+ COO);
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
										
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_COO);
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",COO);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		            transactionHandler = _MasterDAO.deleteCOO(ht);
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	  
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","COO Deleted Successfully");
			response.sendRedirect("../coo/edit?action=result&COO="+COO);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting COO:"+COO);
			response.sendRedirect("../coo/edit?action=resulterror&COO="+COO);
		}
	

		}

		
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("../coo/edit?action=catchrerror&COO="+COO);
			throw e;
		}

		return msg;
	}
	
	private JSONObject getCOODetails(HttpServletRequest request) {
	    JSONObject resultJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    JSONArray jsonArrayErr = new JSONArray();
	    try {
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           ArrayList  movQryList = _MasterUtil.getCOOList(PLANT,"");
	           if (movQryList.size() > 0) {
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	            			int id=iCnt+1;
	                        String result="",chkString="";
	                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                        Map lineArr = (Map) movQryList.get(iCnt);
	                        JSONObject resultJsonInt = new JSONObject();
	                        String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
	                        String coo = StrUtils.fString((String)lineArr.get("COO")) ;
	                        chkString =autoid+","+coo;
	                       	result += "<tr valign=\"middle\" >"  
	                            		+ "<td width='5%' align = center>"  + id + "</td>"
	                            		+ "<td width='75%' align = left>"  + coo + "</td>"
	                            		+ "</tr>" ;
	                      resultJsonInt.put("COOMASTERDATA", result);
	                      jsonArray.add(resultJsonInt);

	            }
	                resultJson.put("coomaster", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	          } else {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                resultJsonInt.put("ERROR_CODE", "99");
	                jsonArrayErr.add(resultJsonInt);
	                jsonArray.add("");
	                resultJson.put("coomaster", jsonArray);
	                resultJson.put("errors", jsonArrayErr);
	        }
	    } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            resultJson.put("TOTAL_QTY", 0);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	    }
	    return resultJson;
	}
	
		
	private JSONObject getViewCOODetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getCOOList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String coo = StrUtils.fString((String)lineArr.get("COO")) ;
                           	chkString =autoid+","+coo;
                               result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + coo + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("COOMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("coomaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("coomaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	private String addPEPPOL(HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String  PLANT="",peppol_id="",peppol_ukey="",plntcode="";
		Map map = null;
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		PlantMstUtil plantmstutil = new PlantMstUtil();
		boolean flag = false;
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			peppol_id = StrUtils.fString(request.getParameter("PEPPOL_ID"));
			peppol_ukey = StrUtils.fString(request.getParameter("PEPPOL_UKEY"));
			
			Hashtable htCond=new Hashtable();
	 		htCond.put("PLANT",PLANT);  
			List listQry = plantmstutil.getPlantMstDetails(PLANT);
				for (int i =0; i<listQry.size(); i++){
					map = (Map) listQry.get(i);
					plntcode = (String) map.get("PLANT");
	 				}
	     	if(plntcode.equalsIgnoreCase(PLANT)){ 
	     		StringBuffer updateQyery=new StringBuffer("set ");
	    	 	updateQyery.append("PEPPOL_ID= '"+ (String)peppol_id+ "'");
//	 	        updateQyery.append(",PEPPOL_UKEY= '"+ (String)sPEPPOL_UKEY+ "'");
				if(!peppol_id.equalsIgnoreCase(""))
					updateQyery.append(",ISPEPPOL= '"+ 1 + "'");
				else
					updateQyery.append(",ISPEPPOL= '"+ 0 + "'");					
		    	    
				flag=  _PlantMstDAO.update(updateQyery.toString(),htCond,""); 
		    	    
				if(flag){
					resultJson.put("status", "100");
					msg = "<font class='maingreen'>Peppol ID Saved Successfully</font>";
				}
				else{
					resultJson.put("status", "99");
					msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Saving Peppol ID</font>";
				}
			}
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
		}
		return msg;
	}
	
	
	private JSONObject getViewCOODetailsEdit(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
         try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ArrayList  movQryList = _MasterUtil.getCOOList(PLANT,"");
              if (movQryList.size() > 0) {
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String autoid= StrUtils.fString((String)lineArr.get("ID")) ;
                            String coo = StrUtils.fString((String)lineArr.get("COO")) ;
                           	chkString =autoid+","+coo;
                           	
                           	
                              result += "<tr valign=\"middle\">"  
                            		+ "<td  width='5%' align = center><INPUT Type=\"Checkbox\" class=\"form-check-input\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td  width='5%' align = center>"  + id + "</td>"
                            		+ "<td  width='75%' align = left>"  + coo + "</td>"
                            	+ "</tr>" ;
                          resultJsonInt.put("COOMASTERDATA", result);
                          jsonArray.add(resultJsonInt);

                }
                    resultJson.put("coomaster", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                                     
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("coomaster", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
                resultJson.put("SEARCH_DATA", "");
                resultJson.put("TOTAL_QTY", 0);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr); 
        }
        return resultJson;
}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	@SuppressWarnings("unchecked")
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
	private String DeleteShippingDetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String CUSTOMERNAME = "", ID="",result="",fieldDescData="",rflag="";
		MasterDAO _MasterDAO = new MasterDAO();
		//Boolean allChecked = false;
			
		try {
			   
			   	
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkd  = request.getParameterValues("chkitem"); 
	   process: 	
			if (chkd != null)    {     
				for (int i = 0; i < chkd.length; i++)       { 
					String item = chkd[i];
					String itemArray[] = item.split(",");
					ID = itemArray[0];
					CUSTOMERNAME = itemArray[1];
				     Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put("ID",ID);
					ht.put(IDBConstants.CUSTOMERNAME,CUSTOMERNAME);
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_SHIPPING_DETAILS);
					//htm.put("RECID", "");
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("REMARKS",CUSTOMERNAME);
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
					
					 PoHdrDAO podoa = new PoHdrDAO();
					    DoHdrDAO dodao= new DoHdrDAO();
					    EstHdrDAO estdao = new EstHdrDAO();
					    
					    boolean podoaisExist=false;
					    boolean dodaoisExist=false;
					    boolean estdaoisExist=false;
					    
					   	Hashtable htmh = new Hashtable();
					   	htmh.put("SHIPPINGCUSTOMER",CUSTOMERNAME);
					   	htmh.put(IConstants.PLANT,PLANT );
					   	
					   	podoaisExist = podoa.isExisit(htmh,"");
					   	dodaoisExist= dodao.isExisit(htmh,"");
					   	estdaoisExist= estdao.isExisit(htmh,"");
					   	if(podoaisExist || dodaoisExist||estdaoisExist)
					   	{	
					   		DbBean.RollbackTran(ut);
							request.getSession().setAttribute("RESULTERROR","Shipping Details Exists In Transactions:"+CUSTOMERNAME);
							response.sendRedirect("jsp/createshippingdetails.jsp?action=resulterro&CUSTOMERNAME="+CUSTOMERNAME);
					   		
					   	}
					   	else
					   	{
					   	    transactionHandler = _MasterDAO.deleteShippingDetails(ht);
					   	}
					
		           
									
			if(transactionHandler)
			{
				boolean inserted = mdao.insertIntoMovHis(htm);
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	    
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Shipping Details Deleted Successfully");
			response.sendRedirect("jsp/createshippingdetails.jsp?action=result&CUSTOMERNAME="+CUSTOMERNAME);
			
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Shipping Details:"+CUSTOMERNAME);
			response.sendRedirect("jsp/createshippingdetails.jsp?action=result&CUSTOMERNAME="+CUSTOMERNAME);
		}
	

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			response.sendRedirect("jsp/createshippingdetails.jsp?action=catchrerror&CUSTOMERNAME="+CUSTOMERNAME);
			throw e;
		}

		return msg;
	}
	
	
	
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
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
		private JSONObject validateSupplier(String plant, String userId, String aCustCode) {
		JSONObject resultJson = new JSONObject();
		try {
			
			CustUtil custUtil = new CustUtil();
			custUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.VENDNO, aCustCode);
			if (custUtil.isExistVendor(aCustCode,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
		}
		
		private JSONObject validateShipper(String plant, String userId, String aCustCode) {
			JSONObject resultJson = new JSONObject();
			try {
				
				ShipperUtil shipUtil = new ShipperUtil();
				shipUtil.setmLogger(mLogger);
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, plant);
				ht.put(IConstants.FREIGHTFORWARDERNO, aCustCode);
				if (shipUtil.isExistShipper(aCustCode,plant)) {
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}	
	}
		private JSONObject validateOutlet(String plant, String userId, String aOutletCode) {
			JSONObject resultJson = new JSONObject();
			try {
				
				CustUtil custUtil = new CustUtil();
				custUtil.setmLogger(mLogger);
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, plant);
				ht.put(IConstants.OUTLETNO, aOutletCode);
				if (custUtil.isExistVendor(aOutletCode,plant)) {
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}	
		}
		
		private JSONObject validateProject(String plant, String userId, String project) {
			JSONObject resultJson = new JSONObject();
			try {
				
				FinProjectDAO finProjectDAO = new FinProjectDAO();
				finProjectDAO.setmLogger(mLogger);
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, plant);
				ht.put(IConstants.PROJECTNAME, project);
				if (finProjectDAO.isExistProject(project,plant)) {
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}	
		}
		
		private JSONObject validateOrderStatus(String plant, String userId, String status, String OrderType) {
			JSONObject resultJson = new JSONObject();
			try {
				
				DoHdrDAO doHdrDAO = new DoHdrDAO();
				doHdrDAO.setmLogger(mLogger);
				if (doHdrDAO.isExistPrintOrder(status,userId,plant,OrderType)) {
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}	
		}
		
	private JSONObject validateCustomer(String plant, String userId, String aCustCode) {
		JSONObject resultJson = new JSONObject();
		try {
			
			CustUtil custUtil = new CustUtil();
			custUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.CUSTOMER_CODE, aCustCode);
			if (custUtil.isExistCustomer(aCustCode,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateTransport(String plant, String userId, String trans) {
		JSONObject resultJson = new JSONObject();
		try {
			
			TransportModeUtil transUtil = new TransportModeUtil();
			transUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.TRANSPORT_MODE, trans);
			if (transUtil.isExistTransport(trans,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validatePayType(String plant, String userId,String paytype) {
		JSONObject resultJson = new JSONObject();
		try {
			
			PayTermsUtil paytermsUtil = new PayTermsUtil();
			paytermsUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.PAYMENTMODE, paytype);
			if (paytermsUtil.isExistPaytype(paytype,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validatePayTerms(String plant, String userId,String payterms) {
		JSONObject resultJson = new JSONObject();
		try {
			
			PayTermsUtil paytermsUtil = new PayTermsUtil();
			paytermsUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.PAYMENT_TERMS, payterms);
			if (paytermsUtil.isExistPayterms(payterms,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateUser(String plant, String userId,String user) {
		JSONObject resultJson = new JSONObject();
		try {
			
			int companyCount = new PlantMstDAO().getCompanyCount(user);
			if (companyCount == 1) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateBank(String plant, String userId, String Bank) {
		JSONObject resultJson = new JSONObject();
		try {
			
			BankDAO bankDAO = new BankDAO();
			bankDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.BANK, Bank);
			if (bankDAO.isExistBank(Bank,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	
	private JSONObject validateOrderType(String plant, String userId, String ordertype) {
		JSONObject resultJson = new JSONObject();
		try {
			
			OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
			ordertypeUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.ORDERTYPE, ordertype);
			if (ordertypeUtil.isExistOrdertype(ordertype,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateEmployees(String plant, String userId, String employee) {
		JSONObject resultJson = new JSONObject();
		try {
			
			EmployeeUtil employeeUtil = new EmployeeUtil();
			employeeUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.FNAME, employee);
			if (employeeUtil.isExistEmployee(employee,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateIncoterms(String plant, String userId, String incoterms) {
		JSONObject resultJson = new JSONObject();
		try {
			
			EmployeeUtil employeeUtil = new EmployeeUtil();
			employeeUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.INCOTERMS, incoterms);
			if (employeeUtil.isExistIncoterms(incoterms,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateClearingAgent(String plant, String userId, String clearingagent) {
		JSONObject resultJson = new JSONObject();
		try {
			
			ClearAgentDAO clearAgentDAO = new ClearAgentDAO();
			clearAgentDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put("CLEARING_AGENT_ID", clearingagent);
			if (clearAgentDAO.isExistsClearagent(ht)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateTypeOfClearance(String plant, String userId, String typeofclearance) {
		JSONObject resultJson = new JSONObject();
		try {
			
			ClearanceUtil clearanceutil = new ClearanceUtil();
			clearanceutil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IDBConstants.CLEARANCETYPEID,typeofclearance);
			if (clearanceutil.isExistsClearanceType(ht)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateCurrency(String plant, String userId, String currency) {
		JSONObject resultJson = new JSONObject();
		try {
			
			CurrencyUtil currencyUtil = new CurrencyUtil();
			currencyUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.CURRENCYID, currency);
			if (currencyUtil.isExistCurrency(currency,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateTax(String plant, String userId, String tax) {
		JSONObject resultJson = new JSONObject();
		try {
			
			CurrencyUtil currencyUtil = new CurrencyUtil();
			currencyUtil.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.TAXTYPE, tax);
			if (currencyUtil.isExistTax(tax,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateaccount(String plant, String userId, String account) {
		JSONObject resultJson = new JSONObject();
		try {
			
			CoaDAO coaDAO = new CoaDAO();
			coaDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.UOM, account);
			if (coaDAO.isExistAccount(account,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}

	private JSONObject validateUom(String plant, String userId, String uom) {
		JSONObject resultJson = new JSONObject();
		try {
			
			UomDAO uomDao = new UomDAO();
			uomDao.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.UOM, uom);
			if (uomDao.isExistUom(uom,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateLoc(String plant, String userId, String loc) {
		JSONObject resultJson = new JSONObject();
		try {
			
			LocationsDAO locationsDAO = new LocationsDAO();
			locationsDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.LOC, loc);
			if (locationsDAO.isExistLoc(loc,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateHscode(String plant, String userId, String hscode) {
		JSONObject resultJson = new JSONObject();
		try {
			
			UomDAO uomDao = new UomDAO();
			uomDao.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.HSCODE, hscode);
			if (uomDao.isExistHscode(hscode,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateCoo(String plant, String userId, String coo) {
		JSONObject resultJson = new JSONObject();
		try {
			
			UomDAO uomDao = new UomDAO();
			uomDao.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.COO, coo);
			if (uomDao.isExistCoo(coo,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateDept(String plant, String userId, String dept) {
		JSONObject resultJson = new JSONObject();
		try {
			
			PrdDeptDAO prdDeptDAO = new PrdDeptDAO();
			prdDeptDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.PRDDEPTID, dept);
			if (prdDeptDAO.isExistDept(dept,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateCat(String plant, String userId, String cat) {
		JSONObject resultJson = new JSONObject();
		try {
			
			PrdClassDAO prdClassDAO = new PrdClassDAO();
			prdClassDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.PRDCLSID, cat);
			if (prdClassDAO.isExistCat(cat,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateType(String plant, String userId, String type) {
		JSONObject resultJson = new JSONObject();
		try {
			
			PrdTypeDAO prdTypeDAO = new PrdTypeDAO();
			prdTypeDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.PRDTYPEID, type);
			if (prdTypeDAO.isExistType(type,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateBrand(String plant, String userId, String brand) {
		JSONObject resultJson = new JSONObject();
		try {
			
			PrdBrandDAO prdBrandDAO = new PrdBrandDAO();
			prdBrandDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.PRDBRANDID, brand);
			if (prdBrandDAO.isExistBrand(brand,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
	private JSONObject validateCustomerUser(String plant, String userId,
			 String aCustCode) {
		JSONObject resultJson = new JSONObject();
		try {
			
			CustMstDAO custUtil = new CustMstDAO();
			custUtil.setmLogger(mLogger);
			if (custUtil.isExistsCustomerUser(aCustCode,plant)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}	
	}
	
/**/
	private JSONObject getSupplierData(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        CurrencyDAO currencyDAO = new CurrencyDAO();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               //String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
               String currency=new PlantMstDAO().getBaseCurrency(PLANT);
            	TransportModeDAO transportmodedao = new TransportModeDAO();
        		String transportmode = "",transport = "";
               boolean mesflag = false;
               ///////////////////////
               CustUtil custUtils = new CustUtil();
               ArrayList arrCust = custUtils.getVendorListsWithName(QUERY,PLANT," ORDER BY VNAME asc");
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
              
        		transport = (String) arrCustLine.get("TRANSPORTID");
        		int trans = Integer.valueOf(transport);
        		if(trans > 0){
        			transportmode = transportmodedao.getTransportModeById(PLANT,trans);
        		}
        	else{
        		transportmode = "";
        	}
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("VENDO", (String)arrCustLine.get("VENDNO"));
                   resultJsonInt.put("VNAME", (String)arrCustLine.get("VNAME"));
                   resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
                   resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
                   resultJsonInt.put("TRANSPORTID", (String)arrCustLine.get("TRANSPORTID"));
                   resultJsonInt.put("TRANSPORTNAME", transportmode);
                   resultJsonInt.put("BANKNAME", (String)arrCustLine.get("BANKNAME"));
                   resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
                   resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
                   resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
                   resultJsonInt.put("ADDR1", (String)arrCustLine.get("ADDR1"));
                   resultJsonInt.put("ADDR2", (String)arrCustLine.get("ADDR2"));
                   resultJsonInt.put("ADDR3", (String)arrCustLine.get("ADDR3"));
                   resultJsonInt.put("ADDR4", (String)arrCustLine.get("ADDR4"));
                   resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
                   resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
                   resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
                   resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
                   resultJsonInt.put("PAYMENTTYPE", (String)arrCustLine.get("PAYMENTTYPE"));
                   resultJsonInt.put("PAY_TERMS", (String)arrCustLine.get("PAYMENT_TERMS"));
                   resultJsonInt.put("PAYMENT_TERMS", (String)arrCustLine.get("PAYMENTTERMS"));
                   resultJsonInt.put("SUPPLIERTYPE", (String)arrCustLine.get("SUPPLIER_TYPE_ID"));
                   String CURRENCY = (String)arrCustLine.get("CURRENCY_ID");//Author: Azees  Create date: July 13,2021  Description: Show Supplier Based Currency
                   if(CURRENCY.equalsIgnoreCase(""))
                	   CURRENCY=currency;
                   resultJsonInt.put("CURRENCYID", CURRENCY);
                   List curQryList=new ArrayList();
					curQryList = currencyDAO.getCurrencyDetails(CURRENCY,PLANT);
					for(int j =0; j<curQryList.size(); j++) {
						ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
						resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
				        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
				        //double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt);--azees no decimal convert 
				        //Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
				   	    resultJsonInt.put("CURRENCYUSEQT", Curramt);
				   			 
				        }
                   /*ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, PLANT, "");
	               if (listQry.size() > 0) {
	            	   
		                   Map arrCustcur = (Map)listQry.get(0);
		                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
		                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
		                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
		                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
		                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
		                   
		            }*/
                   /*String sCustCode     = (String)arrCustLine.get("VENDNO");
                   String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
                   String isactive    = strUtils.removeQuotes((String)arrCustLine.get("ISACTIVE")); */
                   jsonArray.add(resultJsonInt);
               }
               		resultJson.put("VENDMST", jsonArray);
		            JSONObject resultJsonInt = new JSONObject();
		            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		            resultJsonInt.put("ERROR_CODE", "100");
		            jsonArrayErr.add(resultJsonInt);
		            resultJson.put("errors", jsonArrayErr);
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                   resultJsonInt.put("ERROR_CODE", "99");
                   jsonArrayErr.add(resultJsonInt);
                   resultJson.put("errors", jsonArrayErr);  
               }
//               resultJson.put("VENDMST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getSupplierDataPeppol(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        CurrencyDAO currencyDAO = new CurrencyDAO();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               //String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
               String currency=new PlantMstDAO().getBaseCurrency(PLANT);
            	TransportModeDAO transportmodedao = new TransportModeDAO();
        		String transportmode = "",transport = "";
               boolean mesflag = false;
               ///////////////////////
               CustUtil custUtils = new CustUtil();
               ArrayList arrCust = custUtils.getVendorListsWithNamePeppol(QUERY,PLANT," ORDER BY VNAME asc");
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
              
        		transport = (String) arrCustLine.get("TRANSPORTID");
        		int trans = Integer.valueOf(transport);
        		if(trans > 0){
        			transportmode = transportmodedao.getTransportModeById(PLANT,trans);
        		}
        	else{
        		transportmode = "";
        	}
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("VENDO", (String)arrCustLine.get("VENDNO"));
                   resultJsonInt.put("VNAME", (String)arrCustLine.get("VNAME"));
                   resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
                   resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
                   resultJsonInt.put("TRANSPORTID", (String)arrCustLine.get("TRANSPORTID"));
                   resultJsonInt.put("TRANSPORTNAME", transportmode);
                   resultJsonInt.put("BANKNAME", (String)arrCustLine.get("BANKNAME"));
                   resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
                   resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
                   resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
                   resultJsonInt.put("ADDR1", (String)arrCustLine.get("ADDR1"));
                   resultJsonInt.put("ADDR2", (String)arrCustLine.get("ADDR2"));
                   resultJsonInt.put("ADDR3", (String)arrCustLine.get("ADDR3"));
                   resultJsonInt.put("ADDR4", (String)arrCustLine.get("ADDR4"));
                   resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
                   resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
                   resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
                   resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
                   resultJsonInt.put("PAYMENTTYPE", (String)arrCustLine.get("PAYMENTTYPE"));
                   resultJsonInt.put("PAY_TERMS", (String)arrCustLine.get("PAYMENT_TERMS"));
                   resultJsonInt.put("PAYMENT_TERMS", (String)arrCustLine.get("PAYMENTTERMS"));
                   resultJsonInt.put("SUPPLIERTYPE", (String)arrCustLine.get("SUPPLIER_TYPE_ID"));
                   String CURRENCY = (String)arrCustLine.get("CURRENCY_ID");//Author: Azees  Create date: July 13,2021  Description: Show Supplier Based Currency
                   if(CURRENCY.equalsIgnoreCase(""))
                	   CURRENCY=currency;
                   resultJsonInt.put("CURRENCYID", CURRENCY);
                   List curQryList=new ArrayList();
					curQryList = currencyDAO.getCurrencyDetails(CURRENCY,PLANT);
					for(int j =0; j<curQryList.size(); j++) {
						ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
						resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
				        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
				        //double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); 
				        //Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
				   	    resultJsonInt.put("CURRENCYUSEQT", Curramt);
				   			 
				        }
                   /*ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, PLANT, "");
	               if (listQry.size() > 0) {
	            	   
		                   Map arrCustcur = (Map)listQry.get(0);
		                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
		                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
		                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
		                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
		                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
		                   
		            }*/
                   /*String sCustCode     = (String)arrCustLine.get("VENDNO");
                   String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
                   String isactive    = strUtils.removeQuotes((String)arrCustLine.get("ISACTIVE")); */
                   jsonArray.add(resultJsonInt);
               }
               		resultJson.put("VENDMST", jsonArray);
		            JSONObject resultJsonInt = new JSONObject();
		            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		            resultJsonInt.put("ERROR_CODE", "100");
		            jsonArrayErr.add(resultJsonInt);
		            resultJson.put("errors", jsonArrayErr);
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                   resultJsonInt.put("ERROR_CODE", "99");
                   jsonArrayErr.add(resultJsonInt);
                   resultJson.put("errors", jsonArrayErr);  
               }
//               resultJson.put("VENDMST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	private JSONObject getSupplierDataActive(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        CurrencyDAO currencyDAO = new CurrencyDAO();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               //String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
               String currency=new PlantMstDAO().getBaseCurrency(PLANT);
            	TransportModeDAO transportmodedao = new TransportModeDAO();
        		String transportmode = "",transport = "";
               boolean mesflag = false;
               ///////////////////////
               CustUtil custUtils = new CustUtil();
               ArrayList arrCust = custUtils.getVendorListsWithNameActive(QUERY,PLANT," ORDER BY VNAME asc");
               if (arrCust.size() > 0) {
                   for(int i =0; i<arrCust.size(); i++) {
                       Map arrCustLine = (Map)arrCust.get(i);
                  
            		transport = (String) arrCustLine.get("TRANSPORTID");
            		int trans = Integer.valueOf(transport);
            		if(trans > 0){
            			transportmode = transportmodedao.getTransportModeById(PLANT,trans);
            		}
            	else{
            		transportmode = "";
            	}
                       JSONObject resultJsonInt = new JSONObject();
                       resultJsonInt.put("VENDO", (String)arrCustLine.get("VENDNO"));
                       resultJsonInt.put("VNAME", (String)arrCustLine.get("VNAME"));
                       resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
                       resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
                       resultJsonInt.put("TRANSPORTID", (String)arrCustLine.get("TRANSPORTID"));
                       resultJsonInt.put("TRANSPORTNAME", transportmode);
                       resultJsonInt.put("BANKNAME", (String)arrCustLine.get("BANKNAME"));
                       resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
                       resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
                       resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
                       resultJsonInt.put("ADDR1", (String)arrCustLine.get("ADDR1"));
                       resultJsonInt.put("ADDR2", (String)arrCustLine.get("ADDR2"));
                       resultJsonInt.put("ADDR3", (String)arrCustLine.get("ADDR3"));
                       resultJsonInt.put("ADDR4", (String)arrCustLine.get("ADDR4"));
                       resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
                       resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
                       resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
                       resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
                       resultJsonInt.put("PAYMENTTYPE", (String)arrCustLine.get("PAYMENTTYPE"));
                       resultJsonInt.put("PAY_TERMS", (String)arrCustLine.get("PAYMENT_TERMS"));
                       resultJsonInt.put("PAYMENT_TERMS", (String)arrCustLine.get("PAYMENTTERMS"));
                       resultJsonInt.put("SUPPLIERTYPE", (String)arrCustLine.get("SUPPLIER_TYPE_ID"));
                       String CURRENCY = (String)arrCustLine.get("CURRENCY_ID");//Author: Azees  Create date: July 13,2021  Description: Show Supplier Based Currency
                       if(CURRENCY.equalsIgnoreCase(""))
                    	   CURRENCY=currency;
                       resultJsonInt.put("CURRENCYID", CURRENCY);
                       List curQryList=new ArrayList();
    					curQryList = currencyDAO.getCurrencyDetails(CURRENCY,PLANT);
    					for(int j =0; j<curQryList.size(); j++) {
    						ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
    						resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
    				        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
    				        //double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt);--azees no decimal convert 
    				        //Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
    				   	    resultJsonInt.put("CURRENCYUSEQT", Curramt);
    				   			 
    				        }
                       /*ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, PLANT, "");
    	               if (listQry.size() > 0) {
    	            	   
    		                   Map arrCustcur = (Map)listQry.get(0);
    		                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
    		                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
    		                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
    		                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
    		                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
    		                   
    		            }*/
                       /*String sCustCode     = (String)arrCustLine.get("VENDNO");
                       String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
                       String isactive    = strUtils.removeQuotes((String)arrCustLine.get("ISACTIVE")); */
                       jsonArray.add(resultJsonInt);
                   }
                   		resultJson.put("VENDMST", jsonArray);
    		            JSONObject resultJsonInt = new JSONObject();
    		            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
    		            resultJsonInt.put("ERROR_CODE", "100");
    		            jsonArrayErr.add(resultJsonInt);
    		            resultJson.put("errors", jsonArrayErr);
                   }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                   resultJsonInt.put("ERROR_CODE", "99");
                   jsonArrayErr.add(resultJsonInt);
                   resultJson.put("errors", jsonArrayErr);  
               }
//               resultJson.put("VENDMST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	private JSONObject getShipperData(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CurrencyDAO currencyDAO = new CurrencyDAO();
		//JSONArray jsonArrayMes = new JSONArray();
		//MasterDAO _MasterDAO = new MasterDAO();
		
		try {
			//String PLANT= StrUtils.fString(request.getParameter("PLANT"));
			String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String QUERY= StrUtils.fString(request.getParameter("QUERY"));
			request.getSession().setAttribute("RESULT","");
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
			String currency=new PlantMstDAO().getBaseCurrency(PLANT);
			TransportModeDAO transportmodedao = new TransportModeDAO();
			String transportmode = "",transport = "";
			boolean mesflag = false;
			///////////////////////
			ShipperUtil shipUtils = new ShipperUtil();
			ArrayList arrCust = shipUtils.getShipperListsWithName(QUERY,PLANT," ORDER BY FREIGHT_FORWARDERNAME asc");
			if (arrCust.size() > 0) {
				for(int i =0; i<arrCust.size(); i++) {
					Map arrCustLine = (Map)arrCust.get(i);
					
					transport = (String) arrCustLine.get("TRANSPORTID");
					int trans = Integer.valueOf(transport);
					if(trans > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT,trans);
					}
					else{
						transportmode = "";
					}
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("FREIGHT_FORWARDERNO", (String)arrCustLine.get("FREIGHT_FORWARDERNO"));
					resultJsonInt.put("FREIGHT_FORWARDERNAME", (String)arrCustLine.get("FREIGHT_FORWARDERNAME"));
					resultJsonInt.put("TRANSPORTID", (String)arrCustLine.get("TRANSPORTID"));
					resultJsonInt.put("TRANSPORTNAME", transportmode);
				//	resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
					resultJsonInt.put("LICENCENO", (String)arrCustLine.get("LICENCENO"));
					resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
					resultJsonInt.put("DESGINATION", (String)arrCustLine.get("DESGINATION"));
					resultJsonInt.put("ADDR1", (String)arrCustLine.get("ADDR1"));
					resultJsonInt.put("ADDR2", (String)arrCustLine.get("ADDR2"));
					resultJsonInt.put("ADDR3", (String)arrCustLine.get("ADDR3"));
					resultJsonInt.put("ADDR4", (String)arrCustLine.get("ADDR4"));
					resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
					resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
					resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
					resultJsonInt.put("WORKPHONE", (String)arrCustLine.get("WORKPHONE"));
					resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
					resultJsonInt.put("CUSTOMEREMAIL", (String)arrCustLine.get("CUSTOMEREMAIL"));
					resultJsonInt.put("WEBSITE", (String)arrCustLine.get("WEBSITE"));
					resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
					
				//	resultJsonInt.put("PAYMENTTYPE", (String)arrCustLine.get("PAYMENTTYPE"));
				//	resultJsonInt.put("PAY_TERMS", (String)arrCustLine.get("PAYMENT_TERMS"));
				//	resultJsonInt.put("PAYMENT_TERMS", (String)arrCustLine.get("PAYMENTTERMS"));
				//	resultJsonInt.put("SUPPLIERTYPE", (String)arrCustLine.get("SUPPLIER_TYPE_ID"));
				/*
				 * String CURRENCY = (String)arrCustLine.get("CURRENCY_ID");//Author: Azees
				 * Create date: July 13,2021 Description: Show Supplier Based Currency
				 * if(CURRENCY.equalsIgnoreCase("")) CURRENCY=currency;
				 * resultJsonInt.put("CURRENCYID", CURRENCY); List curQryList=new ArrayList();
				 * curQryList = currencyDAO.getCurrencyDetails(CURRENCY,PLANT); for(int j =0;
				 * j<curQryList.size(); j++) { ArrayList arrCurrLine =
				 * (ArrayList)curQryList.get(j);
				 * resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)
				 * arrCurrLine.get(2)))); String Curramt
				 * =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3))); double
				 * CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt);
				 * Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
				 * resultJsonInt.put("CURRENCYUSEQT", Curramt);
				 * 
				 * }
				 */
					/*ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, PLANT, "");
	               if (listQry.size() > 0) {
	            	   
		                   Map arrCustcur = (Map)listQry.get(0);
		                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
		                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
		                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
		                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
		                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
		                   
		            }*/
					/*String sCustCode     = (String)arrCustLine.get("VENDNO");
                   String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
                   String isactive    = strUtils.removeQuotes((String)arrCustLine.get("ISACTIVE")); */
					jsonArray.add(resultJsonInt);
				}
				resultJson.put("FREIGHT_FORWARDERMST", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			}else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);  
			}
//               resultJson.put("VENDMST", jsonArray);
			///////////////////////              
		} catch (Exception e) {
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}

	//OUTLET
	private JSONObject getOutletData(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
     
        try {
               //String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
               boolean mesflag = false;
               ///////////////////////
               OutletUtil outletUtils = new OutletUtil();
               ArrayList arrCust = outletUtils.getOutletListsWithName(QUERY,PLANT," ORDER BY OUTLET_NAME asc");
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
        		
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("OUTLET", (String)arrCustLine.get("OUTLET"));
                   resultJsonInt.put("OUTLET_NAME", (String)arrCustLine.get("OUTLET_NAME"));
                   resultJsonInt.put("CONTACT_PERSON", (String)arrCustLine.get("CONTACT_PERSON"));
                   resultJsonInt.put("DESGINATION", (String)arrCustLine.get("DESGINATION"));
                   resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
                   resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
                   resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
                   resultJsonInt.put("ADD1", (String)arrCustLine.get("ADD1"));
                   resultJsonInt.put("ADD2", (String)arrCustLine.get("ADD2"));
                   resultJsonInt.put("ADD3", (String)arrCustLine.get("ADD3"));
                   resultJsonInt.put("ADD4", (String)arrCustLine.get("ADD4"));
                   resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
                   resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
                   resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
                   jsonArray.add(resultJsonInt);
               }
               		resultJson.put("POSOUTLETS", jsonArray);
		            JSONObject resultJsonInt = new JSONObject();
		            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		            resultJsonInt.put("ERROR_CODE", "100");
		            jsonArrayErr.add(resultJsonInt);
		            resultJson.put("errors", jsonArrayErr);
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                   resultJsonInt.put("ERROR_CODE", "99");
                   jsonArrayErr.add(resultJsonInt);
                   resultJson.put("errors", jsonArrayErr);  
               }
        	} catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getSupplierTypeData(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
      //MasterDAO _MasterDAO = new MasterDAO();
     
        try {
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ///////////////////////
               CustUtil custUtils = new CustUtil();
               ArrayList arrCust = custUtils.getVendorTypeListStartsWithName(QUERY,PLANT," ORDER BY SUPPLIER_TYPE_DESC asc");
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("SUPPLIER_TYPE_ID", (String)arrCustLine.get("SUPPLIER_TYPE_ID"));
                   resultJsonInt.put("SUPPLIER_TYPE_DESC", (String)arrCustLine.get("SUPPLIER_TYPE_DESC"));
                   jsonArray.add(resultJsonInt);
               }
               resultJson.put("SUPPLIER_TYPE_MST", jsonArray);
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
            resultJsonInt.put("ERROR_CODE", "100");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("errors", jsonArrayErr);
               }else {
            	   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                   resultJsonInt.put("ERROR_CODE", "99");
                   jsonArrayErr.add(resultJsonInt);
                   resultJson.put("errors", jsonArrayErr);  
               }
//               resultJson.put("SUPPLIER_TYPE_MST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	private JSONObject getGstTypeData(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String GSTTYPE = StrUtils.fString(request.getParameter("GSTTYPE"));
        	String STATE_PREFIX = StrUtils.fString(request.getParameter("SALESLOC"));
        	if(STATE_PREFIX.length()>0)
         		STATE_PREFIX="("+STATE_PREFIX+")";
		    GstTypeUtil _GstTypeUtil = new GstTypeUtil();
	    	_GstTypeUtil.setmLogger(mLogger);	    	
		     List listQry= _GstTypeUtil.qryGstType(GSTTYPE ,plant," AND GSTTYPE <> 'POS' AND configkey IN ('EXEMPT','OUT OF SCOPE','INBOUND', 'ZERO RATE') ");
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {
		         	Vector vecItem = (Vector)listQry.get(i);
		         	String sGstTypes = (String)vecItem .get(0);
		         	String sGstDesc = (String)vecItem .get(1);
		         	String sGstPercentage = (String)vecItem .get(2);
		         	String sRemarks = (String)vecItem.get(3);		 
		         	String display="";
		         	if(sGstTypes.equalsIgnoreCase("EXEMPT") || sGstTypes.equalsIgnoreCase("OUT OF SCOPE"))
		         	{
		         		display = sGstTypes;
		         	}
		         	else
		         	{
		         		display = sGstTypes+STATE_PREFIX+" ["+sGstPercentage+"%]";
		         	}
		         	
		         	JSONObject resultJsonInt = new JSONObject();
		         	resultJsonInt.put("SGSTTYPES",(String)vecItem.get(0)+STATE_PREFIX);
					resultJsonInt.put("SGSTDESC", (String)vecItem.get(1));
					resultJsonInt.put("SGSTPERCENTAGE", (String)vecItem .get(2));
					resultJsonInt.put("SREMARKS", (String)vecItem.get(3));
					resultJsonInt.put("DISPLAY", display);
					jsonArray.add(resultJsonInt);
		    	 }
			     resultJson.put("records", jsonArray);
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
	
	private JSONObject getGstTypeDataPO(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String GSTTYPE = StrUtils.fString(request.getParameter("GSTTYPE"));
        	String STATE_PREFIX = StrUtils.fString(request.getParameter("SALESLOC"));
        	String sGstPercentage = StrUtils.fString(request.getParameter("GST_PERCENTAGE"));
        	sGstPercentage = StrUtils.addZeroes(Float.parseFloat(sGstPercentage), "1");
        	if(STATE_PREFIX.length()>0)
         		STATE_PREFIX="("+STATE_PREFIX+")";
		    GstTypeUtil _GstTypeUtil = new GstTypeUtil();
	    	_GstTypeUtil.setmLogger(mLogger);	    	
		     List listQry= _GstTypeUtil.qryGstType(GSTTYPE ,plant," AND GSTTYPE <> 'POS' AND configkey IN ('EXEMPT','OUT OF SCOPE','INBOUND', 'ZERO RATE') ");
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {
		         	Vector vecItem = (Vector)listQry.get(i);
		         	String sGstTypes = (String)vecItem .get(0);
		         	String sGstDesc = (String)vecItem .get(1);
		         	//String sGstPercentage = (String)vecItem .get(2);
		         	String sRemarks = (String)vecItem.get(3);		 
		         	String display="";
		         	if(sGstTypes.equalsIgnoreCase("EXEMPT") || sGstTypes.equalsIgnoreCase("OUT OF SCOPE"))
		         	{
		         		display = sGstTypes;
		         	}
		         	else if(sGstTypes.equalsIgnoreCase("ZERO RATE")) {
		         		display = sGstTypes+STATE_PREFIX+" [0.0%]";
		         	}
		         	else
		         	{
		         		display = sGstTypes+STATE_PREFIX+" ["+sGstPercentage+"%]";
		         	}
		         	
		         	JSONObject resultJsonInt = new JSONObject();
		         	resultJsonInt.put("SGSTTYPES",(String)vecItem.get(0)+STATE_PREFIX);
					resultJsonInt.put("SGSTDESC", (String)vecItem.get(1));
					if(sGstTypes.equalsIgnoreCase("ZERO RATE") || sGstTypes.equalsIgnoreCase("EXEMPT") || sGstTypes.equalsIgnoreCase("OUT OF SCOPE")) {
						resultJsonInt.put("SGSTPERCENTAGE", "0.0");
					}else {
						resultJsonInt.put("SGSTPERCENTAGE", sGstPercentage);
					}
					resultJsonInt.put("SREMARKS", (String)vecItem.get(3));
					resultJsonInt.put("DISPLAY", display);
					jsonArray.add(resultJsonInt);
		    	 }
			     resultJson.put("records", jsonArray);
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
	
	private JSONObject getTaxTypeDataPO(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
        PlantMstUtil plantmstutil = new PlantMstUtil();
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String COUNTRYCODE = "";
        	String GSTTYPE = StrUtils.fString(request.getParameter("GSTTYPE"));
        	String TAXKEY = StrUtils.fString(request.getParameter("TAXKEY"));
        	String STATE_PREFIX = StrUtils.fString(request.getParameter("SALESLOC"));
        	String sGstPercentage = StrUtils.fString(request.getParameter("GST_PERCENTAGE"));
        	sGstPercentage = StrUtils.addZeroes(Float.parseFloat(sGstPercentage), "1");
        	List viewlistQry = plantmstutil.getPlantMstDetails(plant);
            for (int i = 0; i < viewlistQry.size(); i++) {
                Map map = (Map) viewlistQry.get(i);
                COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
            }
        	
        	boolean state = false;
        	if(STATE_PREFIX.length()>0) {
         		STATE_PREFIX="("+STATE_PREFIX+")";
         		state = true;
        	}
        	
        	if(state) {
        		
        		List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes(TAXKEY, COUNTRYCODE, GSTTYPE);
        		
        		if(taxtypes.size() > 0) {
	        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("SGSTTYPES",finCountryTaxType.getTAXTYPE()+STATE_PREFIX);
	        			resultJsonInt.put("SGSTDESC",finCountryTaxType.getTAXDESC());
	        			if(finCountryTaxType.getISZERO() == 1) {
	        				resultJsonInt.put("SGSTPERCENTAGE", "0.0");
	        			}else {
	        				resultJsonInt.put("SGSTPERCENTAGE", sGstPercentage);
	        			}
	        			
	        			String display="";
			         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
			         	{
			         		display = finCountryTaxType.getTAXTYPE();
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
			         		display = finCountryTaxType.getTAXTYPE()+" ["+sGstPercentage+"%]";
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
			         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
			         		display = finCountryTaxType.getTAXTYPE()+STATE_PREFIX;
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
			         		display = finCountryTaxType.getTAXTYPE()+STATE_PREFIX+" [0.0%]";
			         	}
			         	else
			         	{
			         		display = finCountryTaxType.getTAXTYPE()+STATE_PREFIX+" ["+sGstPercentage+"%]";
			         	}
			         	
			         	resultJsonInt.put("DISPLAY", display);
			         	resultJsonInt.put("ISZERO", finCountryTaxType.getISZERO());
			         	resultJsonInt.put("ISSHOW", finCountryTaxType.getSHOWTAX());
			         	resultJsonInt.put("ID", finCountryTaxType.getID());
						jsonArray.add(resultJsonInt);
					}				
	        		
	        		 resultJson.put("records", jsonArray);
		             JSONObject resultJsonInt = new JSONObject();
		             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		             resultJsonInt.put("ERROR_CODE", "100");
		             jsonArrayErr.add(resultJsonInt);
		             resultJson.put("errors", jsonArrayErr);
        		}else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
   		        }
        	}else {
        		
        		List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes(TAXKEY, COUNTRYCODE, GSTTYPE);
        		
        		if(taxtypes.size() > 0) {
	        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
	        			JSONObject resultJsonInt = new JSONObject();
	        			resultJsonInt.put("SGSTTYPES",finCountryTaxType.getTAXTYPE()+STATE_PREFIX);
	        			resultJsonInt.put("SGSTDESC",finCountryTaxType.getTAXDESC());
	        			if(finCountryTaxType.getISZERO() == 1) {
	        				resultJsonInt.put("SGSTPERCENTAGE", "0.0");
	        			}else {
	        				resultJsonInt.put("SGSTPERCENTAGE", sGstPercentage);
	        			}
	        			
	        			String display="";
			         	if(finCountryTaxType.getSHOWPERCENTAGE() == 0 && finCountryTaxType.getISZERO() == 0)
			         	{
			         		display = finCountryTaxType.getTAXTYPE();
			         	}else if(finCountryTaxType.getSHOWPERCENTAGE() == 0 && finCountryTaxType.getISZERO() == 1) {
			         		display = finCountryTaxType.getTAXTYPE();
			         	}else if(finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
			         		display = finCountryTaxType.getTAXTYPE()+" ["+sGstPercentage+"%]";
			         	}else {
			         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
			         	}
	
			         	resultJsonInt.put("DISPLAY", display);
			         	resultJsonInt.put("ISZERO", finCountryTaxType.getISZERO());
			         	resultJsonInt.put("ISSHOW", finCountryTaxType.getSHOWTAX());
			         	resultJsonInt.put("ID", finCountryTaxType.getID());
						jsonArray.add(resultJsonInt);
					}				
	        		
	        		 resultJson.put("records", jsonArray);
		             JSONObject resultJsonInt = new JSONObject();
		             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		             resultJsonInt.put("ERROR_CODE", "100");
		             jsonArrayErr.add(resultJsonInt);
		             resultJson.put("errors", jsonArrayErr);
        		}else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
   		     }
        		
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
	
	private JSONObject getGstTypeDataSales(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String GSTTYPE = StrUtils.fString(request.getParameter("GSTTYPE"));
        	String STATE_PREFIX = StrUtils.fString(request.getParameter("SALESLOC"));
        	if(STATE_PREFIX.length()>0)
         		STATE_PREFIX="("+STATE_PREFIX+")";
		    GstTypeUtil _GstTypeUtil = new GstTypeUtil();
	    	_GstTypeUtil.setmLogger(mLogger);	    	
		     List listQry= _GstTypeUtil.qryGstType(GSTTYPE ,plant," AND GSTTYPE <> 'POS' AND configkey IN ('EXEMPT','OUT OF SCOPE','OUTBOUND', 'ZERO RATE') ");
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {
		         	Vector vecItem = (Vector)listQry.get(i);
		         	String sGstTypes = (String)vecItem .get(0);
		         	String sGstDesc = (String)vecItem .get(1);
		         	String sGstPercentage = (String)vecItem .get(2);
		         	String sRemarks = (String)vecItem.get(3);		         	
		         	String display="";
		         	if(sGstTypes.equalsIgnoreCase("EXEMPT") || sGstTypes.equalsIgnoreCase("OUT OF SCOPE"))
		         	{
		         		display = sGstTypes;
		         	}
		         	else
		         	{
		         		display = sGstTypes+STATE_PREFIX+" ["+sGstPercentage+"%]";
		         	}
		         	JSONObject resultJsonInt = new JSONObject();
		         	resultJsonInt.put("SGSTTYPES", (String)vecItem.get(0));
					resultJsonInt.put("SGSTDESC", (String)vecItem.get(1));
					resultJsonInt.put("SGSTPERCENTAGE", (String)vecItem .get(2));
					resultJsonInt.put("SREMARKS", (String)vecItem.get(3));
					resultJsonInt.put("DISPLAY", display);
					jsonArray.add(resultJsonInt);
		    	 }
			     resultJson.put("records", jsonArray);
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
	
	private JSONObject getGstTypeDataSalesSO(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String GSTTYPE = StrUtils.fString(request.getParameter("GSTTYPE"));
        	String STATE_PREFIX = StrUtils.fString(request.getParameter("SALESLOC"));
        	String GST = StrUtils.fString(request.getParameter("GST"));
        	if(STATE_PREFIX.length()>0)
         		STATE_PREFIX="("+STATE_PREFIX+")";
		    GstTypeUtil _GstTypeUtil = new GstTypeUtil();
	    	_GstTypeUtil.setmLogger(mLogger);	    	
		     List listQry= _GstTypeUtil.qryGstType(GSTTYPE ,plant," AND GSTTYPE <> 'POS' AND configkey IN ('EXEMPT','OUT OF SCOPE','OUTBOUND', 'ZERO RATE') ");
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {
		         	Vector vecItem = (Vector)listQry.get(i);
		         	String sGstTypes = (String)vecItem .get(0);
		         	String sGstDesc = (String)vecItem .get(1);
		         	String sGstPercentage = StrUtils.addZeroes(Float.parseFloat(GST), "1");
		         	String sRemarks = (String)vecItem.get(3);		         	
		         	String display="";
		         	if(sGstTypes.equalsIgnoreCase("EXEMPT") || sGstTypes.equalsIgnoreCase("OUT OF SCOPE"))
		         	{
		         		display = sGstTypes;
		         	}
		         	else if(sGstTypes.equalsIgnoreCase("ZERO RATE")) {
		         		display = sGstTypes+STATE_PREFIX+" [0.0%]";
		         	}
		         	else
		         	{
		         		display = sGstTypes+STATE_PREFIX+" ["+sGstPercentage+"%]";
		         	}
		         	JSONObject resultJsonInt = new JSONObject();
		         	resultJsonInt.put("SGSTTYPES", (String)vecItem.get(0));
					resultJsonInt.put("SGSTDESC", (String)vecItem.get(1));
					if(sGstTypes.equalsIgnoreCase("EXEMPT") || sGstTypes.equalsIgnoreCase("OUT OF SCOPE") || sGstTypes.equalsIgnoreCase("ZERO RATE"))
		         	{
						resultJsonInt.put("SGSTPERCENTAGE", "0");
		         	}else{
		         		resultJsonInt.put("SGSTPERCENTAGE", GST);
		         	}
					resultJsonInt.put("SREMARKS", (String)vecItem.get(3));
					resultJsonInt.put("DISPLAY", display);
					jsonArray.add(resultJsonInt);
		    	 }
			     resultJson.put("records", jsonArray);
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
	private JSONObject getGstTypeDataExpense(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String GSTTYPE = StrUtils.fString(request.getParameter("GSTTYPE"));
		    GstTypeUtil _GstTypeUtil = new GstTypeUtil();
	    	_GstTypeUtil.setmLogger(mLogger);	    	
		     List listQry= _GstTypeUtil.qryGstType(GSTTYPE ,plant," AND GSTTYPE <> 'POS' AND configkey IN ('EXEMPT','OUT OF SCOPE','STANDARD RATE', 'ZERO RATE') ");
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {
		         	Vector vecItem = (Vector)listQry.get(i);
		         	String sGstTypes = (String)vecItem .get(0);
		         	String sGstDesc = (String)vecItem .get(1);
		         	String sGstPercentage = (String)vecItem .get(2);
		         	String sRemarks = (String)vecItem.get(3);		         	
		         	String display="";
		         	if(sGstTypes.equalsIgnoreCase("EXEMPT") || sGstTypes.equalsIgnoreCase("OUT OF SCOPE"))
		         	{
		         		display = sGstTypes;
		         	}
		         	else
		         	{
		         		display = sGstTypes+"["+sGstPercentage+"%]";
		         	}
		         	JSONObject resultJsonInt = new JSONObject();
		         	resultJsonInt.put("SGSTTYPES", (String)vecItem.get(0));
					resultJsonInt.put("SGSTDESC", (String)vecItem.get(1));
					resultJsonInt.put("SGSTPERCENTAGE", (String)vecItem .get(2));
					resultJsonInt.put("SREMARKS", (String)vecItem.get(3));
					resultJsonInt.put("DISPLAY", display);
					jsonArray.add(resultJsonInt);
		    	 }
			     resultJson.put("records", jsonArray);
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
	/**/
	private JSONObject createSupplier(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		String sUserId = (String) request.getSession().getAttribute("LOGIN_USER");
		String sNewEnb = "enabled";
		String sDeleteEnb = "enabled";
		String sAddEnb = "enabled";
		String sUpdateEnb = "enabled";
		String sCustEnb = "enabled";
		String sCustCode = "", sCustName = "", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sCountry = "", sZip = "", sCons = "Y";
		String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "",sRcbno="", 
				sRemarks = "",sPayTerms="",sPayInDays="",suppliertypeid="",desc="",sState="";

		DateUtils dateutils = new DateUtils();
		StrUtils strUtils = new StrUtils();
		CustUtil custUtil = new CustUtil();
		custUtil.setmLogger(mLogger);
		TblControlDAO _TblControlDAO = new TblControlDAO();
		String plant = strUtils.fString((String) request.getSession().getAttribute("PLANT"));
		String username = strUtils.fString((String) request.getSession().getAttribute("LOGIN_USER"));
		sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));
		try {
		userBean ub = new userBean(); 
		String taxbylabel= ub.getTaxByLable(plant);

		if (sCustCode.length() <= 0)
		sCustCode = strUtils.fString(request.getParameter("CUST_CODE1"));
		sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
		sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
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
		sPayInDays=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PMENT_DAYS")));
		suppliertypeid=strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
		List suppliertypelist=custUtil.getVendorTypeList("",plant," AND ISACTIVE ='Y'");
		MovHisDAO mdao = new MovHisDAO(plant);
		
		if (!custUtil.isExistVendor(sCustCode, plant) && !custUtil.isExistVendorName(sCustName, plant)) // if the Supplier exists already
		{
		Hashtable ht = new Hashtable();
		ht.put(IConstants.PLANT, plant);
		ht.put(IConstants.VENDOR_CODE, sCustCode);
		ht.put(IConstants.VENDOR_NAME, sCustName);
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
		ht.put(IConstants.PAYINDAYS, sPayInDays);
		ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
		ht.put(IConstants.CREATED_BY, sUserId);
		ht.put(IConstants.ISACTIVE, "Y");
		
		ht.put("Comment1", " 0 ");
		ht.put(IConstants.STATE, sState);
		ht.put(IConstants.RCBNO, sRcbno);
		ht.put(IConstants.SUPPLIERTYPEID, suppliertypeid);
		
		String sysTime = DateUtils.Time();
		String sysDate = DateUtils.getDate();
		sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

		mdao.setmLogger(mLogger);
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
			resultJson.put("MESSAGE", "Supplier Added Successfully");
			resultJson.put("VENDOR_NAME", sCustName);		 
			resultJson.put("STATUS", "SUCCESS");
		} else {
			resultJson.put("MESSAGE", "Failed to add New Supplier");
			resultJson.put("STATUS", "FAIL");
		}
	} else {
		resultJson.put("MESSAGE", "Supplier ID Or Name Exists already. Try again with diffrent Supplier ID Or Name.");
		resultJson.put("STATUS", "FAIL");
	}
		}catch (Exception e) {
			System.out.println(e.getMessage());
			resultJson.put("MESSAGE", "Failed to add New Supplier");
			resultJson.put("STATUS", "FAIL");
		}
	
		return resultJson;
	}
		private JSONObject getCustomerData(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        CurrencyDAO currencyDAO = new CurrencyDAO();
     
        try {
               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
               String currency=new PlantMstDAO().getBaseCurrency(PLANT);
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               ///////////////////////
               CustUtil custUtils = new CustUtil();
               ArrayList arrCust = custUtils.getCustomerListStartsWithName(QUERY,PLANT);
               if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
                   JSONObject resultJsonInt = new JSONObject();
                   resultJsonInt.put("CUSTNO", (String)arrCustLine.get("CUSTNO"));
                   resultJsonInt.put("CNAME", (String)arrCustLine.get("CNAME"));
                   resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
                   resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
                   resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
                   resultJsonInt.put("ADDR1", (String)arrCustLine.get("ADDR1"));
                   resultJsonInt.put("ADDR2", (String)arrCustLine.get("ADDR2"));
                   resultJsonInt.put("ADDR3", (String)arrCustLine.get("ADDR3"));
                   resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
                   resultJsonInt.put("ADDR4", (String)arrCustLine.get("ADDR4"));
                   resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
                   resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
                   resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
                   resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
                   resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
                   resultJsonInt.put("PAYMENTTYPE", (String)arrCustLine.get("PAY_TERMS"));
                   resultJsonInt.put("BANKNAME", (String)arrCustLine.get("BANKNAME"));
                   resultJsonInt.put("CUSTOMER_TYPE_ID", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
                   resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
                   resultJsonInt.put("SHIPCONTACTNAME", (String)arrCustLine.get("SHIPCONTACTNAME"));
                   resultJsonInt.put("SHIPDESGINATION", (String)arrCustLine.get("SHIPDESGINATION"));
                   resultJsonInt.put("SHIPADDR1", (String)arrCustLine.get("SHIPADDR1"));
                   resultJsonInt.put("SHIPADDR2", (String)arrCustLine.get("SHIPADDR2"));
                   resultJsonInt.put("SHIPADDR3", (String)arrCustLine.get("SHIPADDR3"));
                   resultJsonInt.put("SHIPADDR4", (String)arrCustLine.get("SHIPADDR4"));
                   resultJsonInt.put("SHIPSTATE", (String)arrCustLine.get("SHIPSTATE"));
                   resultJsonInt.put("SHIPZIP", (String)arrCustLine.get("SHIPZIP"));
                   resultJsonInt.put("SHIPWORKPHONE", (String)arrCustLine.get("SHIPWORKPHONE"));
                   resultJsonInt.put("TRANSPORTID", (String)arrCustLine.get("TRANSPORTID"));
                   resultJsonInt.put("TRANSPORT_MODE", (String)arrCustLine.get("TRANSPORT_MODE"));
                   resultJsonInt.put("TRANSPORTNAME", (String)arrCustLine.get("TRANSPORT_MODE"));
                   resultJsonInt.put("PAY_TERMS", (String)arrCustLine.get("PAY_TERMS"));
                   resultJsonInt.put("SHIPHPNO", (String)arrCustLine.get("SHIPHPNO"));
                   resultJsonInt.put("SHIPEMAIL", (String)arrCustLine.get("SHIPEMAIL"));
                   resultJsonInt.put("SHIPCOUNTRY", (String)arrCustLine.get("SHIPCOUNTRY"));
                   resultJsonInt.put("PAYMENT_TERMS", (String)arrCustLine.get("PAYMENT_TERMS"));
                   resultJsonInt.put("DOB", (String)arrCustLine.get("DATEOFBIRTH"));
                   resultJsonInt.put("NATIONALITY", (String)arrCustLine.get("NATIONALITY"));
                   resultJsonInt.put("ISPEPPOL", (String)arrCustLine.get("ISPEPPOL"));
                   resultJsonInt.put("SHOWSALESBYPURCHASECOST", (String)arrCustLine.get("SHOWSALESBYPURCHASECOST"));
                   
                   String CURRENCY = (String)arrCustLine.get("CURRENCY_ID");//Author: Azees  Create date: July 14,2021  Description: Show Customer Based Currency
                   if(CURRENCY.equalsIgnoreCase(""))
                	   CURRENCY=currency;
                   resultJsonInt.put("CURRENCYID", CURRENCY);
                   List curQryList=new ArrayList();
					curQryList = currencyDAO.getCurrencyDetails(CURRENCY,PLANT);
					for(int j =0; j<curQryList.size(); j++) {
						ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
						resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
				        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
				        //double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); --azees no decimal convert
				        //Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
				   	    resultJsonInt.put("CURRENCYUSEQT", Curramt);
				   			 
				        }
                   /*ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, PLANT, "");
	               if (listQry.size() > 0) {
	            	   
		                   Map arrCustcur = (Map)listQry.get(0);
		                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
		                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
		                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
		                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
		                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
		                   
		            }*/
                   jsonArray.add(resultJsonInt);
               }
               }/*else {
            	   JSONObject resultJsonInt = new JSONObject();
                   jsonArray.add("");
                   resultJson.put("footermaster", jsonArray);
               }*/
               resultJson.put("CUSTMST", jsonArray);
               ///////////////////////              
        } catch (Exception e) {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
		}
		
		private JSONObject getCustomerDataActive(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        CurrencyDAO currencyDAO = new CurrencyDAO();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
	               String currency=new PlantMstDAO().getBaseCurrency(PLANT);
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               CustUtil custUtils = new CustUtil();
	               ArrayList arrCust = custUtils.getCustomerListStartsWithNameActive(QUERY,PLANT);
	               if (arrCust.size() > 0) {
	                   for(int i =0; i<arrCust.size(); i++) {
	                       Map arrCustLine = (Map)arrCust.get(i);
	                       JSONObject resultJsonInt = new JSONObject();
	                       resultJsonInt.put("CUSTNO", (String)arrCustLine.get("CUSTNO"));
	                       resultJsonInt.put("CNAME", (String)arrCustLine.get("CNAME"));
	                       resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
	                       resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
	                       resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
	                       resultJsonInt.put("ADDR1", (String)arrCustLine.get("ADDR1"));
	                       resultJsonInt.put("ADDR2", (String)arrCustLine.get("ADDR2"));
	                       resultJsonInt.put("ADDR3", (String)arrCustLine.get("ADDR3"));
	                       resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
	                       resultJsonInt.put("ADDR4", (String)arrCustLine.get("ADDR4"));
	                       resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
	                       resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
	                       resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
	                       resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
	                       resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
	                       resultJsonInt.put("PAYMENTTYPE", (String)arrCustLine.get("PAY_TERMS"));
	                       resultJsonInt.put("BANKNAME", (String)arrCustLine.get("BANKNAME"));
	                       resultJsonInt.put("CUSTOMER_TYPE_ID", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
	                       resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
	                       resultJsonInt.put("SHIPCONTACTNAME", (String)arrCustLine.get("SHIPCONTACTNAME"));
	                       resultJsonInt.put("SHIPDESGINATION", (String)arrCustLine.get("SHIPDESGINATION"));
	                       resultJsonInt.put("SHIPADDR1", (String)arrCustLine.get("SHIPADDR1"));
	                       resultJsonInt.put("SHIPADDR2", (String)arrCustLine.get("SHIPADDR2"));
	                       resultJsonInt.put("SHIPADDR3", (String)arrCustLine.get("SHIPADDR3"));
	                       resultJsonInt.put("SHIPADDR4", (String)arrCustLine.get("SHIPADDR4"));
	                       resultJsonInt.put("SHIPSTATE", (String)arrCustLine.get("SHIPSTATE"));
	                       resultJsonInt.put("SHIPZIP", (String)arrCustLine.get("SHIPZIP"));
	                       resultJsonInt.put("SHIPWORKPHONE", (String)arrCustLine.get("SHIPWORKPHONE"));
	                       resultJsonInt.put("TRANSPORTID", (String)arrCustLine.get("TRANSPORTID"));
	                       resultJsonInt.put("TRANSPORT_MODE", (String)arrCustLine.get("TRANSPORT_MODE"));
	                       resultJsonInt.put("TRANSPORTNAME", (String)arrCustLine.get("TRANSPORT_MODE"));
	                       resultJsonInt.put("PAY_TERMS", (String)arrCustLine.get("PAY_TERMS"));
	                       resultJsonInt.put("SHIPHPNO", (String)arrCustLine.get("SHIPHPNO"));
	                       resultJsonInt.put("SHIPEMAIL", (String)arrCustLine.get("SHIPEMAIL"));
	                       resultJsonInt.put("SHIPCOUNTRY", (String)arrCustLine.get("SHIPCOUNTRY"));
	                       resultJsonInt.put("PAYMENT_TERMS", (String)arrCustLine.get("PAYMENT_TERMS"));
	                       resultJsonInt.put("DOB", (String)arrCustLine.get("DATEOFBIRTH"));
	                       resultJsonInt.put("NATIONALITY", (String)arrCustLine.get("NATIONALITY"));
	                       resultJsonInt.put("ISPEPPOL", (String)arrCustLine.get("ISPEPPOL"));
	                       resultJsonInt.put("SHOWSALESBYPURCHASECOST", (String)arrCustLine.get("SHOWSALESBYPURCHASECOST"));
	                       resultJsonInt.put("CUST_ADDONCOST", (String)arrCustLine.get("CUST_ADDONCOST"));
	                       resultJsonInt.put("CUST_ADDONCOSTTYPE", (String)arrCustLine.get("ADDONCOSTTYPE"));
	                       
	                       String CURRENCY = (String)arrCustLine.get("CURRENCY_ID");//Author: Azees  Create date: July 14,2021  Description: Show Customer Based Currency
	                       if(CURRENCY.equalsIgnoreCase(""))
	                    	   CURRENCY=currency;
	                       resultJsonInt.put("CURRENCYID", CURRENCY);
	                       List curQryList=new ArrayList();
	    					curQryList = currencyDAO.getCurrencyDetails(CURRENCY,PLANT);
	    					for(int j =0; j<curQryList.size(); j++) {
	    						ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
	    						resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
	    				        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
	    				        //double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); --azees no decimal convert
	    				        //Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
	    				   	    resultJsonInt.put("CURRENCYUSEQT", Curramt);
	    				   			 
	    				        }
	                       /*ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, PLANT, "");
	    	               if (listQry.size() > 0) {
	    	            	   
	    		                   Map arrCustcur = (Map)listQry.get(0);
	    		                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
	    		                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
	    		                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
	    		                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
	    		                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
	    		                   
	    		            }*/
	                       jsonArray.add(resultJsonInt);
	                   }
	                   }/*else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }*/
	               resultJson.put("CUSTMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		private JSONObject getCustomerDataPeppol(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        CurrencyDAO currencyDAO = new CurrencyDAO();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
	               String currency=new PlantMstDAO().getBaseCurrency(PLANT);
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               CustUtil custUtils = new CustUtil();
	               ArrayList arrCust = custUtils.getCustomerListStartsWithNamePeppol(QUERY,PLANT);
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("CUSTNO", (String)arrCustLine.get("CUSTNO"));
	                   resultJsonInt.put("CNAME", (String)arrCustLine.get("CNAME"));
	                   resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
	                   resultJsonInt.put("TELNO", (String)arrCustLine.get("TELNO"));
	                   resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
	                   resultJsonInt.put("ADDR1", (String)arrCustLine.get("ADDR1"));
	                   resultJsonInt.put("ADDR2", (String)arrCustLine.get("ADDR2"));
	                   resultJsonInt.put("ADDR3", (String)arrCustLine.get("ADDR3"));
	                   resultJsonInt.put("REMARKS", (String)arrCustLine.get("REMARKS"));
	                   resultJsonInt.put("ADDR4", (String)arrCustLine.get("ADDR4"));
	                   resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
	                   resultJsonInt.put("COUNTRY", (String)arrCustLine.get("COUNTRY"));
	                   resultJsonInt.put("ZIP", (String)arrCustLine.get("ZIP"));
	                   resultJsonInt.put("HPNO", (String)arrCustLine.get("HPNO"));
	                   resultJsonInt.put("EMAIL", (String)arrCustLine.get("EMAIL"));
	                   resultJsonInt.put("PAYMENTTYPE", (String)arrCustLine.get("PAY_TERMS"));
	                   resultJsonInt.put("BANKNAME", (String)arrCustLine.get("BANKNAME"));
	                   resultJsonInt.put("CUSTOMER_TYPE_ID", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
	                   resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
	                   resultJsonInt.put("SHIPCONTACTNAME", (String)arrCustLine.get("SHIPCONTACTNAME"));
	                   resultJsonInt.put("SHIPDESGINATION", (String)arrCustLine.get("SHIPDESGINATION"));
	                   resultJsonInt.put("SHIPADDR1", (String)arrCustLine.get("SHIPADDR1"));
	                   resultJsonInt.put("SHIPADDR2", (String)arrCustLine.get("SHIPADDR2"));
	                   resultJsonInt.put("SHIPADDR3", (String)arrCustLine.get("SHIPADDR3"));
	                   resultJsonInt.put("SHIPADDR4", (String)arrCustLine.get("SHIPADDR4"));
	                   resultJsonInt.put("SHIPSTATE", (String)arrCustLine.get("SHIPSTATE"));
	                   resultJsonInt.put("SHIPZIP", (String)arrCustLine.get("SHIPZIP"));
	                   resultJsonInt.put("SHIPWORKPHONE", (String)arrCustLine.get("SHIPWORKPHONE"));
	                   resultJsonInt.put("TRANSPORTID", (String)arrCustLine.get("TRANSPORTID"));
	                   resultJsonInt.put("TRANSPORT_MODE", (String)arrCustLine.get("TRANSPORT_MODE"));
	                   resultJsonInt.put("TRANSPORTNAME", (String)arrCustLine.get("TRANSPORT_MODE"));
	                   resultJsonInt.put("PAY_TERMS", (String)arrCustLine.get("PAY_TERMS"));
	                   resultJsonInt.put("SHIPHPNO", (String)arrCustLine.get("SHIPHPNO"));
	                   resultJsonInt.put("SHIPEMAIL", (String)arrCustLine.get("SHIPEMAIL"));
	                   resultJsonInt.put("SHIPCOUNTRY", (String)arrCustLine.get("SHIPCOUNTRY"));
	                   resultJsonInt.put("PAYMENT_TERMS", (String)arrCustLine.get("PAYMENT_TERMS"));
	                   resultJsonInt.put("DOB", (String)arrCustLine.get("DATEOFBIRTH"));
	                   resultJsonInt.put("NATIONALITY", (String)arrCustLine.get("NATIONALITY"));
	                   resultJsonInt.put("ISPEPPOL", (String)arrCustLine.get("ISPEPPOL"));
	                   
	                   String CURRENCY = (String)arrCustLine.get("CURRENCY_ID");//Author: Azees  Create date: July 14,2021  Description: Show Customer Based Currency
	                   if(CURRENCY.equalsIgnoreCase(""))
	                	   CURRENCY=currency;
	                   resultJsonInt.put("CURRENCYID", CURRENCY);
	                   List curQryList=new ArrayList();
						curQryList = currencyDAO.getCurrencyDetails(CURRENCY,PLANT);
						for(int j =0; j<curQryList.size(); j++) {
							ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
							resultJsonInt.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
					        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
					        double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt); 
					        Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
					   	    resultJsonInt.put("CURRENCYUSEQT", Curramt);
					   			 
					        }
	                   /*ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, PLANT, "");
		               if (listQry.size() > 0) {
		            	   
			                   Map arrCustcur = (Map)listQry.get(0);
			                   resultJsonInt.put("CURRENCY", (String)arrCustcur.get("DISPLAY"));		                   		                   
			                 String  Curramt = (String)arrCustcur.get("CURRENCYUSEQT");
			                 double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);        					
			                 Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);                         
			                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
			                   
			            }*/
	                   jsonArray.add(resultJsonInt);
	               }
	               }/*else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }*/
	               resultJson.put("CUSTMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		private JSONObject getNextSupplierSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			String minseq = "";
			String sBatchSeq = "";
			boolean insertFlag = false;
			String sZero = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "SUPPLIER");			
			String sCustCode="";
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
			      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("CUSTCODE", sCustCode);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		
		
		//Resvi Starts Code For Product Department Modal
		private JSONObject getNextPrdDep(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			String minseq = "";
			String sBatchSeq = "";
			boolean insertFlag = false;
			String sZero = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "PRDDEPT");			
			String sItemId="";
			try {

					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDDEPT");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PD");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sItemId = "PD" + "0001";
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
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRDDEPT");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PD");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

						//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
						sItemId = "PD" + sZero + updatedSeq;
					}
			      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("PRDDEPT", sItemId);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		//Ends
		
		private JSONObject getNextSupType(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			String minseq = "";
			String sBatchSeq = "";
			boolean insertFlag = false;
			String sZero = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "SUPPLIERTYPE");			
			String sItemId="";
			try {

					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SUPPLIERTYPE");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "ST");
						htTblCntInsert.put("MINSEQ", "000");
						htTblCntInsert.put("MAXSEQ", "999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sItemId = "ST" + "001";
					} else {
						//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

						Map m = _TblControlDAO.selectRow(query, ht, "");
						sBatchSeq = (String) m.get("NXTSEQ");
						System.out.println("length" + sBatchSeq.length());

						int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

						String updatedSeq = Integer.toString(inxtSeq);
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
						htTblCntUpdate.put(IDBConstants.PLANT, plant);
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"SUPPLIERTYPE");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "ST");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

						//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
						sItemId = "ST" + sZero + updatedSeq;
					}
			      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("SUPPLIERTYPE", sItemId);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		
		private JSONObject getNextCustType(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			String minseq = "";
			String sBatchSeq = "";
			boolean insertFlag = false;
			String sZero = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "CUSTOMERTYPE");			
			String sItemId="";
			try {

					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "CT");
						htTblCntInsert.put("MINSEQ", "000");
						htTblCntInsert.put("MAXSEQ", "999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sItemId = "CT" + "001";
					} else {
						//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

						Map m = _TblControlDAO.selectRow(query, ht, "");
						sBatchSeq = (String) m.get("NXTSEQ");
						System.out.println("length" + sBatchSeq.length());

						int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

						String updatedSeq = Integer.toString(inxtSeq);
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
						htTblCntUpdate.put(IDBConstants.PLANT, plant);
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "CT");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

						//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
						sItemId = "CT" + sZero + updatedSeq;
					}
			      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("CUSTOMERTYPE", sItemId);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		
		//Thanzith Starts Code For Product Category Modal
				private JSONObject getNextPrdCat(HttpServletRequest request) {
					JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
					String minseq = "";
					String sBatchSeq = "";
					boolean insertFlag = false;
					String sZero = "";
					TblControlDAO _TblControlDAO = new TblControlDAO();
					_TblControlDAO.setmLogger(mLogger);
					Hashtable ht = new Hashtable();
					String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
					String query = " isnull(NXTSEQ,'') as NXTSEQ";
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.TBL_FUNCTION, "PRDCLASS");			
					String sItemId="";
					try {

							boolean exitFlag = false;
							boolean resultflag = false;
							exitFlag = _TblControlDAO.isExisit(ht, "", plant);

							//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
							if (exitFlag == false) {

								Map htInsert = null;
								Hashtable htTblCntInsert = new Hashtable();
								htTblCntInsert.put(IDBConstants.PLANT, plant);
								htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDCLASS");
								htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PC");
								htTblCntInsert.put("MINSEQ", "000");
								htTblCntInsert.put("MAXSEQ", "999");
								htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
								htTblCntInsert.put(IDBConstants.CREATED_BY, username);
								htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
								insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

								sItemId = "PC" + "001";
							} else {
								//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

								Map m = _TblControlDAO.selectRow(query, ht, "");
								sBatchSeq = (String) m.get("NXTSEQ");
								System.out.println("length" + sBatchSeq.length());

								int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

								String updatedSeq = Integer.toString(inxtSeq);
								if (updatedSeq.length() == 1) {
									sZero = "00";
								} else if (updatedSeq.length() == 2) {
									sZero = "0";
								} 
								/*
								 * else if (updatedSeq.length() == 3) { sZero = "0"; }
								 */

								Map htUpdate = null;

								Hashtable htTblCntUpdate = new Hashtable();
								htTblCntUpdate.put(IDBConstants.PLANT, plant);
								htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRDCLASS");
								htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PC");
								StringBuffer updateQyery = new StringBuffer("set ");
								updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

								//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
								sItemId = "PC" + sZero + updatedSeq;
							}
					      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
					      resultJson.put("ERROR_CODE", "100");
			              resultJson.put("PRDCLASS", sItemId);
					} catch (Exception e) {
				        resultJson.put("ERROR_MESSAGE",  e.getMessage());
				        resultJson.put("ERROR_CODE", "98");
					}
					return resultJson;
				}
				//END 
				//Thanzith Starts Code For Product Location Modal
				private JSONObject getNextLocCat(HttpServletRequest request) {
					JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
					String minseq = "";
					String sBatchSeq = "";
					boolean insertFlag = false;
					String sZero = "";
					TblControlDAO _TblControlDAO = new TblControlDAO();
					_TblControlDAO.setmLogger(mLogger);
					Hashtable ht = new Hashtable();
					String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
					String query = " isnull(NXTSEQ,'') as NXTSEQ";
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.TBL_FUNCTION, "LOCATION");			
					String sItemId="";
					try {
						
						boolean exitFlag = false;
						boolean resultflag = false;
						exitFlag = _TblControlDAO.isExisit(ht, "", plant);
						
						//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
						if (exitFlag == false) {
							
							Map htInsert = null;
							Hashtable htTblCntInsert = new Hashtable();
							htTblCntInsert.put(IDBConstants.PLANT, plant);
							htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCATION");
							htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "L");
							htTblCntInsert.put("MINSEQ", "0000");
							htTblCntInsert.put("MAXSEQ", "9999");
							htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
							htTblCntInsert.put(IDBConstants.CREATED_BY, username);
							htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
							insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
							
							sItemId = "L" + "0001";
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
							}  else if (updatedSeq.length() == 3) {
								sZero = "0";
							}
							/*
							 * else if (updatedSeq.length() == 3) { sZero = "0"; }
							 */
							
							Map htUpdate = null;
							
							Hashtable htTblCntUpdate = new Hashtable();
							htTblCntUpdate.put(IDBConstants.PLANT, plant);
							htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"LOCATION");
							htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "L");
							StringBuffer updateQyery = new StringBuffer("set ");
							updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");
							
							//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
							sItemId = "L" + sZero + updatedSeq;
						}
						resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
						resultJson.put("ERROR_CODE", "100");
						resultJson.put("LOCATION", sItemId);
					} catch (Exception e) {
						resultJson.put("ERROR_MESSAGE",  e.getMessage());
						resultJson.put("ERROR_CODE", "98");
					}
					return resultJson;
				}
				//END 
				
				//Thanzith Starts Code For Product sub Category Modal
				private JSONObject getNextPrdSubCat(HttpServletRequest request) {
					JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
					String minseq = "";
					String sBatchSeq = "";
					boolean insertFlag = false;
					String sZero = "";
					TblControlDAO _TblControlDAO = new TblControlDAO();
					_TblControlDAO.setmLogger(mLogger);
					Hashtable ht = new Hashtable();
					String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
					String query = " isnull(NXTSEQ,'') as NXTSEQ";
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.TBL_FUNCTION, "PRDTYPE");			
					String sItemId="";
					try {

							boolean exitFlag = false;
							boolean resultflag = false;
							exitFlag = _TblControlDAO.isExisit(ht, "", plant);

							//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
							if (exitFlag == false) {

								Map htInsert = null;
								Hashtable htTblCntInsert = new Hashtable();
								htTblCntInsert.put(IDBConstants.PLANT, plant);
								htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDTYPE");
								htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PT");
								htTblCntInsert.put("MINSEQ", "000");
								htTblCntInsert.put("MAXSEQ", "999");
								htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
								htTblCntInsert.put(IDBConstants.CREATED_BY, username);
								htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
								insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

								sItemId = "PT" + "001";
							} else {
								//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

								Map m = _TblControlDAO.selectRow(query, ht, "");
								sBatchSeq = (String) m.get("NXTSEQ");
								System.out.println("length" + sBatchSeq.length());

								int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

								String updatedSeq = Integer.toString(inxtSeq);
								if (updatedSeq.length() == 1) {
									sZero = "00";
								} else if (updatedSeq.length() == 2) {
									sZero = "0";
								} 
								/*
								 * else if (updatedSeq.length() == 3) { sZero = "0"; }
								 */

								Map htUpdate = null;

								Hashtable htTblCntUpdate = new Hashtable();
								htTblCntUpdate.put(IDBConstants.PLANT, plant);
								htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRDTYPE");
								htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PT");
								StringBuffer updateQyery = new StringBuffer("set ");
								updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

								//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
								sItemId = "PT" + sZero + updatedSeq;
							}
					      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
					      resultJson.put("ERROR_CODE", "100");
			              resultJson.put("PRDTYPE", sItemId);
					} catch (Exception e) {
				        resultJson.put("ERROR_MESSAGE",  e.getMessage());
				        resultJson.put("ERROR_CODE", "98");
					}
					return resultJson;
				}
				//END 
				
				//Thanzith Starts Code For Product Brand Modal
				private JSONObject getNextPrdBrand(HttpServletRequest request) {
					JSONObject resultJson = new JSONObject();
					JSONArray jsonArrayErr = new JSONArray();
					String minseq = "";
					String sBatchSeq = "";
					boolean insertFlag = false;
					String sZero = "";
					TblControlDAO _TblControlDAO = new TblControlDAO();
					_TblControlDAO.setmLogger(mLogger);
					Hashtable ht = new Hashtable();
					String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
					String query = " isnull(NXTSEQ,'') as NXTSEQ";
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.TBL_FUNCTION, "PRDBRAND");			
					String sItemId="";
					try {

							boolean exitFlag = false;
							boolean resultflag = false;
							exitFlag = _TblControlDAO.isExisit(ht, "", plant);

							//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
							if (exitFlag == false) {

								Map htInsert = null;
								Hashtable htTblCntInsert = new Hashtable();
								htTblCntInsert.put(IDBConstants.PLANT, plant);
								htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDBRAND");
								htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PB");
								htTblCntInsert.put("MINSEQ", "000");
								htTblCntInsert.put("MAXSEQ", "999");
								htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
								htTblCntInsert.put(IDBConstants.CREATED_BY, username);
								htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
								insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

								sItemId = "PB" + "001";
							} else {
								//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

								Map m = _TblControlDAO.selectRow(query, ht, "");
								sBatchSeq = (String) m.get("NXTSEQ");
								System.out.println("length" + sBatchSeq.length());

								int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

								String updatedSeq = Integer.toString(inxtSeq);
								if (updatedSeq.length() == 1) {
									sZero = "00";
								} else if (updatedSeq.length() == 2) {
									sZero = "0";
								} 
								/*
								 * else if (updatedSeq.length() == 3) { sZero = "0"; }
								 */

								Map htUpdate = null;

								Hashtable htTblCntUpdate = new Hashtable();
								htTblCntUpdate.put(IDBConstants.PLANT, plant);
								htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRDBRAND");
								htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PB");
								StringBuffer updateQyery = new StringBuffer("set ");
								updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

								//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
								sItemId = "PB" + sZero + updatedSeq;
							}
					      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
					      resultJson.put("ERROR_CODE", "100");
			              resultJson.put("PRDBRAND", sItemId);
					} catch (Exception e) {
				        resultJson.put("ERROR_MESSAGE",  e.getMessage());
				        resultJson.put("ERROR_CODE", "98");
					}
					return resultJson;
				}
				//END 
				
		private JSONObject getSalesLocationData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               
	               ArrayList arrCust = _MasterUtil.getSalesLocationList(QUERY,PLANT,"");
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
	                   resultJsonInt.put("STATE_PREFIX", (String)arrCustLine.get("PREFIX"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("SLOCMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		
		private JSONObject getLocationByCountry(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String COUNTRYCODE= StrUtils.fString(request.getParameter("COUNTRYCODE"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               
	               ArrayList arrCust = _MasterUtil.getSalesLocationListByCode(QUERY,PLANT,COUNTRYCODE);
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("STATE", (String)arrCustLine.get("STATE"));
	                   resultJsonInt.put("STATE_PREFIX", (String)arrCustLine.get("PREFIX"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("SLOCMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		
		private JSONObject getEmployeeData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               EmployeeUtil custUtils = new EmployeeUtil();
	               HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO();
	               ArrayList arrCust = custUtils.getEmployeeListStartsWithName(QUERY,PLANT);
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	            	   Map arrCustLine = (Map)arrCust.get(i);
	            	   String emptype="";
	            	   int emptypeid = Integer.valueOf((String)arrCustLine.get("EMPLOYEETYPEID"));
	            	   if(emptypeid > 0) {
	            		  HrEmpType hrEmpType = hrEmpTypeDAO.getEmployeetypeById(PLANT, emptypeid);
	            		  emptype = hrEmpType.getEMPLOYEETYPE();
	            	   }
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("EMPNO", (String)arrCustLine.get("EMPNO"));
	                   resultJsonInt.put("FNAME", (String)arrCustLine.get("FNAME"));
	                   resultJsonInt.put("EMPTYPE", emptype);
	                   resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
	                   resultJsonInt.put("REPORTING_INCHARGE", (String)arrCustLine.get("REPORTING_INCHARGE"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
	               resultJson.put("EMPMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		private JSONObject getEmployeeDatawithoutid(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String empid= StrUtils.fString(request.getParameter("empid"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               EmployeeUtil custUtils = new EmployeeUtil();
	               ArrayList arrCust = custUtils.getEmployeeListStartsWithNameempid(QUERY,PLANT,empid);
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("EMPNO", (String)arrCustLine.get("EMPNO"));
	                   resultJsonInt.put("FNAME", (String)arrCustLine.get("FNAME"));
	                   resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("EMPMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		private JSONObject createCustomer(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			String sUserId = (String) request.getSession().getAttribute("LOGIN_USER");
			String sNewEnb = "enabled";
			String sDeleteEnb = "enabled";
			String sAddEnb = "enabled";
			String sUpdateEnb = "enabled";
			String sCustEnb = "enabled";
			String sCustCode = "", sCustName = "", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sCountry = "", sZip = "", sCons = "Y";
			String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "",sRcbno="", 
					sRemarks = "",sPayTerms="",sPayInDays="",customertypeid="",desc="",sState="";

			DateUtils dateutils = new DateUtils();
			StrUtils strUtils = new StrUtils();
			CustUtil custUtil = new CustUtil();
			custUtil.setmLogger(mLogger);
			TblControlDAO _TblControlDAO = new TblControlDAO();
			String plant = strUtils.fString((String) request.getSession().getAttribute("PLANT"));
			String username = strUtils.fString((String) request.getSession().getAttribute("LOGIN_USER"));
			sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));
			try {
			userBean ub = new userBean(); 
			String taxbylabel= ub.getTaxByLable(plant);
						
			sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
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
			sPayInDays=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PMENT_DAYS")));
			customertypeid=strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
			List customertypelist=custUtil.getCustTypeList("",plant," AND ISACTIVE ='Y'");
			MovHisDAO mdao = new MovHisDAO(plant);
			
			if (!custUtil.isExistCustomer(sCustCode, plant) && !custUtil.isExistCustomerName(sCustName, plant)) // if the Customer exists already
			{
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.CUSTOMER_CODE, sCustCode);
			ht.put(IConstants.CUSTOMER_NAME, sCustName);
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
			ht.put(IConstants.PAYINDAYS, sPayInDays);
			ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			ht.put(IConstants.CREATED_BY, sUserId);
			ht.put(IConstants.ISACTIVE, "Y");
			
			ht.put("Comment1", " 0 ");
			ht.put(IConstants.STATE, sState);
			ht.put(IConstants.RCBNO, sRcbno);
			ht.put(IConstants.CUSTOMERTYPEID, customertypeid);
			
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_CUST);
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
			if(sCustCode!="C0001")
	  		  {	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
					updateFlag=_TblControlDAO.updateSeqNo("CUSTOMER",plant);				
			else
			{
				boolean insertFlag = false;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "C");
				htTblCntInsert.put("MINSEQ", "0000");
				htTblCntInsert.put("MAXSEQ", "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
			}
			}
		
			boolean custInserted = custUtil.insertCustomer(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custInserted) {
				resultJson.put("MESSAGE", "Customer Added Successfully");
				resultJson.put("CUSTOMER", sCustName);		 
				resultJson.put("STATUS", "SUCCESS");
			} else {
				resultJson.put("MESSAGE", "Failed to add New Customer");
				resultJson.put("STATUS", "FAIL");
			}
		} else {
			resultJson.put("MESSAGE", "Customer ID Or Name Exists already. Try again with diffrent Customer ID Or Name.");
			resultJson.put("STATUS", "FAIL");
		}
			}catch (Exception e) {
				System.out.println(e.getMessage());
				resultJson.put("MESSAGE", "Failed to add New Customer");
				resultJson.put("STATUS", "FAIL");
			}
		
			return resultJson;
		}
		private JSONObject getNextCustomerSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			String minseq = "";
			String sBatchSeq = "";
			boolean insertFlag = false;
			String sZero = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");			
			String sCustCode="";
			try {

					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "C");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sCustCode = "C" + "0001";
					} else {

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
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "C");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");
						sCustCode = "C" + sZero + updatedSeq;
					}
			      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("CUSTCODE", sCustCode);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		private JSONObject validatenoofEmployee(String plant, String NOOFEMPLOYEE) {
			JSONObject resultJson = new JSONObject();
			try {
				
				//Validate no.of Employee -- Azees 19.11.2020
				EmployeeDAO employeeDao = new EmployeeDAO();
				int novalid =employeeDao.Employeecount(plant);
				if(!NOOFEMPLOYEE.equalsIgnoreCase("Unlimited"))
				{
					int convl = Integer.valueOf(NOOFEMPLOYEE);
					if(novalid>=convl)
					{
						resultJson.put("status", "100");
						resultJson.put("ValidNumber", NOOFEMPLOYEE);
					}
					else
					{
						resultJson.put("status", "99");
						
					}
				}
				else
					resultJson.put("status", "99");
				
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}	
		}
		private JSONObject validateEmployee(String plant, String userId,
				 String aCustCode) {
			JSONObject resultJson = new JSONObject();
			try {
				
				EmployeeUtil custUtil = new EmployeeUtil();
				custUtil.setmLogger(mLogger);
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, plant);
				ht.put(IDBConstants.EMPNO, aCustCode);
				if (custUtil.isExistsEmployee(ht)) {
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
				return resultJson;
			} catch (Exception daRE) {
				resultJson.put("status", "99");
				return resultJson;
			}	
		}
		private JSONObject getNextEmployeeSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			String minseq = "";
			String sBatchSeq = "";
			boolean insertFlag = false;
			String sZero = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "EMPLOYEE");			
			String sCustCode="";
			try {

					boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

					//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "E");
						htTblCntInsert.put("MINSEQ", "0000");
						htTblCntInsert.put("MAXSEQ", "9999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sCustCode = "E" + "0001";
					} else {

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
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "E");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");
						sCustCode = "E" + sZero + updatedSeq;
					}
			      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("CUSTCODE", sCustCode);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		private JSONObject createEmployee(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			String sUserId = (String) request.getSession().getAttribute("LOGIN_USER");
			String sNewEnb = "enabled";
			String sDeleteEnb = "enabled";
			String sAddEnb = "enabled";
			String sUpdateEnb = "enabled";
			String sCustEnb = "enabled";
			String sCustCode = "", sCustName = "", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sCountry = "", sZip = "", sCons = "Y";
			String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "",sNationality="", 
					sRemarks = "",sDept="",sDOB="",sGender="",ISPOSCUSTOMER="",ISEDITPOSPRODUCTPRICE="",ISCASHIER="",ISSALESMAN="",desc="",sState="",DOB="",sOutCode="";

			DateUtils dateutils = new DateUtils();
			StrUtils strUtils = new StrUtils();
			EmployeeUtil custUtil = new EmployeeUtil();
			custUtil.setmLogger(mLogger);
			TblControlDAO _TblControlDAO = new TblControlDAO();
			String plant = strUtils.fString((String) request.getSession().getAttribute("PLANT"));
			String username = strUtils.fString((String) request.getSession().getAttribute("LOGIN_USER"));
			sCustCode = strUtils.fString(request.getParameter("CUST_CODE_EMP"));
			try {
			userBean ub = new userBean(); 
			String taxbylabel= ub.getTaxByLable(plant);
						
			sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
			sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
			sAddr1 = strUtils.fString(request.getParameter("ADDR1"));
			sAddr2 = strUtils.fString(request.getParameter("ADDR2"));
			sAddr3 = strUtils.fString(request.getParameter("ADDR3"));
			sAddr4 = strUtils.fString(request.getParameter("ADDR4"));

			sState = strUtils.InsertQuotes(strUtils.fString(request.getParameter("STATE_EMP")));
			sCountry = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));
			sZip = strUtils.fString(request.getParameter("ZIP"));
			sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
			sTelNo = strUtils.fString(request.getParameter("TELNO"));
			sHpNo = strUtils.fString(request.getParameter("HPNO"));
			sFax = strUtils.fString(request.getParameter("FAX"));
			sEmail = strUtils.fString(request.getParameter("EMAIL"));
			sGender = strUtils.fString(request.getParameter("GENDER"));
			ISPOSCUSTOMER = strUtils.fString(request.getParameter("ISPOSCUSTOMER"));
			ISEDITPOSPRODUCTPRICE = strUtils.fString(request.getParameter("ISEDITPOSPRODUCTPRICE"));
			ISCASHIER = strUtils.fString(request.getParameter("ISCASHIER"));
			ISSALESMAN = strUtils.fString(request.getParameter("ISSALESMAN"));
			sOutCode = strUtils.fString(request.getParameter("OUTCODE"));
			sRemarks = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
			sDOB=strUtils.InsertQuotes(strUtils.fString(request.getParameter("DOB")));
			sNationality=strUtils.InsertQuotes(strUtils.fString(request.getParameter("NATIONALITY")));
			sDept=strUtils.fString(request.getParameter("DEPT"));
			if(sDOB.length() > 5){
				DOB = sDOB.substring(6) +"-"+ sDOB.substring(3, 5) +"-"+  sDOB.substring(0, 2);
			}
			String sPASSPORTNUMBER="",sCOUNTRYOFISSUE="",sPASSPORTEXPIRYDATE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",sEMIRATESID="",sEMIRATESIDEXPIRY="",sVISANUMBER="",sVISAEXPIRYDATE="",
					sDATEOFJOINING="",sDATEOFLEAVING="",sLABOURCARDNUMBER="",sWORKPERMITNUMBER="",sCONTRACTSTARTDATE="",sCONTRACTENDDATE="",sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="",
					BASICSALARY="",HOUSERENTALLOWANCE="",TRANSPORTALLOWANCE="",COMMUNICATIONALLOWANCE="",OTHERALLOWANCE="",BONUS="",COMMISSION="",sSAVE_RED="",sCountryCode="";
			sDATEOFJOINING     = strUtils.fString(request.getParameter("DATEOFJOINING"));
			sDATEOFLEAVING   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("DATEOFLEAVING")));
			SKYPE   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("SKYPE")));
			FACEBOOK   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("FACEBOOK")));
			TWITTER   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TWITTER")));
			LINKEDIN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("LINKEDIN")));
			sPASSPORTNUMBER       = strUtils.fString(request.getParameter("PASSPORTNUMBER"));
			sCOUNTRYOFISSUE      = strUtils.fString(request.getParameter("COUNTRYOFISSUE"));
			sPASSPORTEXPIRYDATE      = strUtils.fString(request.getParameter("PASSPORTEXPIRYDATE"));
			sEMIRATESID  = strUtils.fString(request.getParameter("EMIRATESID"));
			sEMIRATESIDEXPIRY = strUtils.fString(request.getParameter("EMIRATESIDEXPIRY"));
			sVISANUMBER = strUtils.fString(request.getParameter("VISANUMBER"));
			sVISAEXPIRYDATE  = strUtils.fString(request.getParameter("VISAEXPIRYDATE"));
			sLABOURCARDNUMBER  = strUtils.fString(request.getParameter("LABOURCARDNUMBER"));
			sWORKPERMITNUMBER  = strUtils.fString(request.getParameter("WORKPERMITNUMBER"));
			sCONTRACTSTARTDATE= strUtils.fString(request.getParameter("CONTRACTSTARTDATE"));
			sCONTRACTENDDATE     = strUtils.fString(request.getParameter("CONTRACTENDDATE"));
			sBANKNAME   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BANKNAME")));
			sIBAN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("IBAN")));
			sBANKROUTINGCODE       = strUtils.fString(request.getParameter("BANKROUTINGCODE"));
			BASICSALARY      = strUtils.fString(request.getParameter("BASICSALARY"));
			HOUSERENTALLOWANCE      = strUtils.fString(request.getParameter("HOUSERENTALLOWANCE"));
			TRANSPORTALLOWANCE  = strUtils.fString(request.getParameter("TRANSPORTALLOWANCE"));
			COMMUNICATIONALLOWANCE = strUtils.fString(request.getParameter("COMMUNICATIONALLOWANCE"));
			OTHERALLOWANCE = strUtils.fString(request.getParameter("OTHERALLOWANCE"));
			BONUS  = strUtils.fString(request.getParameter("BONUS"));
			COMMISSION  = strUtils.fString(request.getParameter("COMMISSION"));			
			
			//List employeetypelist=custUtil.getCustTypeList("",plant," AND ISACTIVE ='Y'");
			MovHisDAO mdao = new MovHisDAO(plant);
			
			Hashtable htchk = new Hashtable();
			htchk.put(IConstants.PLANT, plant);
			htchk.put(IConstants.EMPNO, sCustCode);
			
			Hashtable htchkn = new Hashtable();
			htchkn.put(IConstants.PLANT, plant);
			htchkn.put(IDBConstants.FNAME, sCustName);
			
			if (!custUtil.isExistsEmployee(htchk) && !custUtil.isExistsEmployee(htchk)) // if the Employee exists already
			{
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT,plant);
	          ht.put(IDBConstants.EMPNO,sCustCode);
	          ht.put(IDBConstants.FNAME,sCustName);
	          ht.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
	          ht.put(IDBConstants.GENDER,sGender);
	          ht.put("ISPOSCUSTOMER",ISPOSCUSTOMER);
	          ht.put("ISEDITPOSPRODUCTPRICE",ISEDITPOSPRODUCTPRICE);
	          ht.put("ISCASHIER",ISCASHIER);
	          ht.put("ISSALESMAN",ISSALESMAN);
	          ht.put(IDBConstants.DOB,sDOB);
	          ht.put(IDBConstants.DEPTARTMENT,sDept);
	          ht.put(IConstants.DESGINATION,sDesgination);
	          ht.put(IDBConstants.DATEOFJOINING,sDATEOFJOINING);
	          ht.put(IDBConstants.DATEOFLEAVING,sDATEOFLEAVING);
	          ht.put(IDBConstants.NATIONALITY,sNationality);
	          ht.put(IConstants.TELNO,sTelNo);
	          ht.put(IConstants.HPNO,sHpNo);
	          //ht.put(IConstants.FAX,sFax);
	          ht.put(IConstants.EMAIL,sEmail);
		      ht.put(IConstants.FACEBOOK,FACEBOOK);
		      ht.put(IConstants.TWITTER,TWITTER);
		      ht.put(IConstants.LINKEDIN,LINKEDIN);
		      ht.put(IConstants.SKYPE,SKYPE);
		      ht.put("OUTLET",sOutCode);
		      ht.put(IDBConstants.PASSPORTNUMBER,sPASSPORTNUMBER);
		      if(sCOUNTRYOFISSUE.equalsIgnoreCase("Select Country"))
		    	  sCOUNTRYOFISSUE="";
		      ht.put(IDBConstants.COUNTRYOFISSUE,sCOUNTRYOFISSUE);
		      ht.put(IDBConstants.PASSPORTEXPIRYDATE,sPASSPORTEXPIRYDATE);
	          ht.put(IConstants.UNITNO,sAddr1);
	          ht.put(IConstants.BUILDING,sAddr2);
	          ht.put(IConstants.STREET,sAddr3);
	          ht.put(IConstants.CITY,sAddr4);
	          if(sState.equalsIgnoreCase("Select State"))
					sState="";
	          ht.put(IConstants.STATE,sState);
	          if(sCountry.equalsIgnoreCase("Select Country"))
	        	  sCountry="";
	          ht.put(IConstants.COUNTRY,sCountry);
	          ht.put(IConstants.ZIP,sZip);
	          ht.put(IDBConstants.EMIRATESID,sEMIRATESID);
	          ht.put(IDBConstants.EMIRATESIDEXPIRY,sEMIRATESIDEXPIRY);
	          ht.put(IDBConstants.VISANUMBER,sVISANUMBER);
	          ht.put(IDBConstants.VISAEXPIRYDATE,sVISAEXPIRYDATE);
	          ht.put(IDBConstants.LABOURCARDNUMBER,sLABOURCARDNUMBER);
	          ht.put(IDBConstants.WORKPERMITNUMBER,sWORKPERMITNUMBER);
	          ht.put(IDBConstants.CONTRACTSTARTDATE,sCONTRACTSTARTDATE);
	          ht.put(IDBConstants.CONTRACTENDDATE,sCONTRACTENDDATE);
	          if(sBANKNAME.equalsIgnoreCase("Select Bank"))
	        	  sBANKNAME="";
	          ht.put(IDBConstants.BANKNAME,sBANKNAME);
	          ht.put(IDBConstants.IBAN,sIBAN);
	          ht.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
	          ht.put(IDBConstants.BASICSALARY,BASICSALARY);
	          ht.put(IDBConstants.HOUSERENTALLOWANCE,HOUSERENTALLOWANCE);
	          ht.put(IDBConstants.TRANSPORTALLOWANCE,TRANSPORTALLOWANCE);
	          ht.put(IDBConstants.COMMUNICATIONALLOWANCE,COMMUNICATIONALLOWANCE);
	          ht.put(IDBConstants.OTHERALLOWANCE,OTHERALLOWANCE);
	          ht.put(IDBConstants.BONUS,BONUS);
	          ht.put(IDBConstants.COMMISSION,COMMISSION);
	          ht.put(IConstants.REMARKS,sRemarks);
	          ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
	          ht.put(IConstants.CREATED_BY,sUserId);
	          ht.put(IConstants.ISACTIVE,"Y");
	          
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_EMPLOYEE);
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
			if(sCustCode!="E0001")
	  		  {	
				boolean exitFlag = false;
				Hashtable htv = new Hashtable();				
				htv.put(IDBConstants.PLANT, plant);
				htv.put(IDBConstants.TBL_FUNCTION, "EMPLOYEE");
				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
				if (exitFlag) 
					updateFlag=_TblControlDAO.updateSeqNo("EMPLOYEE",plant);				
			else
			{
				boolean insertFlag = false;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "E");
				htTblCntInsert.put("MINSEQ", "0000");
				htTblCntInsert.put("MAXSEQ", "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
			}
			}
		
			boolean custInserted = custUtil.insertEmployeeMst(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custInserted) {
				resultJson.put("MESSAGE", "Employee Added Successfully");
				resultJson.put("EMP_NAME", sCustName);		 
				resultJson.put("STATUS", "SUCCESS");
			} else {
				resultJson.put("MESSAGE", "Failed to add New Employee");
				resultJson.put("STATUS", "FAIL");
			}
		} else {
			resultJson.put("MESSAGE", "Employee ID Or Name Exists already. Try again with diffrent Employee ID Or Name.");
			resultJson.put("STATUS", "FAIL");
		}
			}catch (Exception e) {
				System.out.println(e.getMessage());
				resultJson.put("MESSAGE", "Failed to add New Employee");
				resultJson.put("STATUS", "FAIL");
			}
		
			return resultJson;
		}
		private JSONObject getNextShipmentSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			 ArrayList movQryList  = new ArrayList();
			/*String minseq = "";
			String sBatchSeq = "";
			boolean insertFlag = false;
			String sZero = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/*String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "SHIPMENT");			
			String sCustCode="";*/
		    String shipmentcode="";
			try {

					/*boolean exitFlag = false;
					boolean resultflag = false;
					exitFlag = _TblControlDAO.isExisit(ht, "", plant);

				
					if (exitFlag == false) {

						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SHIPMENT");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "SN");
						htTblCntInsert.put("MINSEQ", "00000");
						htTblCntInsert.put("MAXSEQ", "99999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

						sCustCode = "SN" + "00001";
					} else {

						Map m = _TblControlDAO.selectRow(query, ht, "");
						sBatchSeq = (String) m.get("NXTSEQ");
						System.out.println("length" + sBatchSeq.length());

						int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

						String updatedSeq = Integer.toString(inxtSeq);
						if (updatedSeq.length() == 1) {
							sZero = "0000";
						} else if (updatedSeq.length() == 2) {
							sZero = "000";
						} else if (updatedSeq.length() == 3) {
							sZero = "00";
						} else if (updatedSeq.length() == 4) {
							sZero = "0";
						}

						Map htUpdate = null;

						Hashtable htTblCntUpdate = new Hashtable();
						htTblCntUpdate.put(IDBConstants.PLANT, plant);
						htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"SHIPMENT");
						htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "SN");
						StringBuffer updateQyery = new StringBuffer("set ");
						updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");
						sCustCode = "SN" + sZero + updatedSeq;
					}*/
				
				  String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
				
				  Hashtable ht = new Hashtable();
		           
			      ht.put("PONO",ORDERNO);
			       
			      movQryList = _MasterUtil.getShipmentSummaryView(ht,plant,ORDERNO);
			      
			      if (movQryList.size() > 0) {
			    	  		int a[]=new int[movQryList.size()];
			                                  
			                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
			                        String result="";
			                        Map lineArr = (Map) movQryList.get(iCnt);                            
			               
			                    	String str = (String) lineArr.get("SHIPMENTCODE");
			                    	String strNew = str.replaceAll("SN", "");
			                    	            	
			                    	a[iCnt] = Integer.parseInt(strNew);
			                }
			               
			                int b = getMax(a);
			                shipmentcode = "SN";
			                String str = String.format("%05d", (b+1));
			                shipmentcode += str;
			                
			      } else {
			           shipmentcode = "SN00001"; 
			      }
				
				
				
			      resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("SHIPMENTCODE", shipmentcode);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		
		public static int getMax(int[] inputArray){ 
		    int maxValue = inputArray[0]; 
		    for(int i=1;i < inputArray.length;i++){ 
		      if(inputArray[i] > maxValue){ 
		         maxValue = inputArray[i]; 
		      } 
		    } 
		    return maxValue; 
		  }
		
		private JSONObject getshipmentview(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList movQryList  = new ArrayList();
	        
	        StrUtils strUtils = new StrUtils();
	        
	         try {
	        
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	           String  SHIPMENT_CODE = StrUtils.fString(request.getParameter("SHIPMENT_CODE"));
	           
	              
	   
	           Hashtable ht = new Hashtable();
	           
		        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
		        if(StrUtils.fString(SHIPMENT_CODE).length() > 0)       ht.put("SHIPMENT_CODE",SHIPMENT_CODE);

		        
					movQryList = _MasterUtil.getShipmentSummaryView(ht,PLANT,ORDERNO);	
				
	            
	            if (movQryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	                                  
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                            String result="";
	                            Map lineArr = (Map) movQryList.get(iCnt);                            
	                               
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                   			
	                      
	                    	 Index = Index + 1;
	                    	 resultJsonInt.put("Index",(Index));
	                    	 resultJsonInt.put("PONO",StrUtils.fString((String)lineArr.get("PONO")));
	                    	 resultJsonInt.put("SHIPMENTCODE",StrUtils.fString((String)lineArr.get("SHIPMENTCODE")));
	                         jsonArray.add(resultJsonInt);
	                    	            	

	                }
	               
	                    resultJson.put("items", jsonArray);
	                    JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                    resultJsonInt.put("ERROR_CODE", "100");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr);
	            } else {
	                    JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    //jsonArray.add("");
	                    resultJson.put("items", jsonArray);

	                    resultJson.put("errors", jsonArrayErr);
	            }
	        } catch (Exception e) {
	        		//jsonArray.add("");
	        		resultJson.put("items", jsonArray);
	                resultJson.put("SEARCH_DATA", "");
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
	}
		private JSONObject saveShipment(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			DateUtils dateutils = new DateUtils();
			
			try {
				HttpSession session = request.getSession();
				String plant = (String) session.getAttribute("PLANT");
				String username = (String) session.getAttribute("LOGIN_USER");
				String pono = request.getParameter("shipmentpono").trim();
				String shipmentCode = request.getParameter("shipmentCode").trim();
				boolean isAdded = false;
				TblControlDAO _TblControlDAO = new TblControlDAO();
				
				Hashtable ShipmentHdr =new Hashtable(); 
				ShipmentHdr.put("PLANT", plant);
				ShipmentHdr.put("PONO", pono);
				ShipmentHdr.put("SHIPMENT_CODE", shipmentCode);
				ShipmentHdr.put("STATUS", "Y");
				ShipmentHdr.put("CRAT",dateutils.getDateTime());
				ShipmentHdr.put("CRBY",username);
				ShipmentHdr.put("UPAT",dateutils.getDateTime());
				
				Hashtable shcheck = new Hashtable();
				shcheck.put("PLANT", plant);
				shcheck.put("PONO", pono);
				shcheck.put("SHIPMENT_CODE", shipmentCode);
				
			 boolean ischeck = _MasterUtil.isExisitShipment(shcheck, plant); 
				
				if(ischeck) {
					resultJson.put("MESSAGE", "Shipment Already Present");
					resultJson.put("STATUS", "FAIL");
				}else {
					isAdded = _MasterUtil.addShipment(ShipmentHdr, plant);
					if(isAdded)
						//isAdded = _TblControlDAO.updateSeqNo("SHIPMENT",plant);
					if(isAdded) {
						resultJson.put("MESSAGE", "Shipment creted successfully");
						resultJson.put("PONO", pono);
						resultJson.put("SHIPMENT_CODE", shipmentCode);
						resultJson.put("STATUS", "SUCCESS");
					}else {
						resultJson.put("MESSAGE", "Failed to add Shipment");
						resultJson.put("STATUS", "FAIL");
					}	
			 } 
				
				
				
				
				
			}catch (Exception e) {
				System.out.println(e.getMessage());
				resultJson.put("MESSAGE", "Failed to add Shipment");
				resultJson.put("STATUS", "FAIL");
			}
			return resultJson;
		}
		
		private JSONObject getPaymentTypeMstList(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	  		       String extCond="";
	  		       if(QUERY.length() > 0)
	  		    	 extCond = "PAYMENTTYPE LIKE '"+QUERY+"%'";
	               ///////////////////////
	               ArrayList  movQryList = _MasterUtil.getPaymentTypeList(PLANT,extCond);
	               if (movQryList.size() > 0) {
		               for(int i =0; i<movQryList.size(); i++) {
		            	   Map arrCustLine = (Map)movQryList.get(i);
		                   String paymenttype  = (String)arrCustLine.get("PAYMENTTYPE");
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
		                   resultJsonInt.put("PAYMENTTYPE", paymenttype);
		                   resultJsonInt.put("CRAT", (String)arrCustLine.get("CRAT"));
		                   resultJsonInt.put("CRBY", (String)arrCustLine.get("CRBY"));
		                   jsonArray.add(resultJsonInt);
		               }
		               	resultJson.put("payTypes", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
//	                   jsonArray.add("");
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("payTypes", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getTransportList(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			TransportModeUtil transportModeUtil = new TransportModeUtil();
			
			try {
				String transport_mode_id = StrUtils.fString(request.getParameter("TRANSPORT_MODE_ID"));
				String extraCond = StrUtils.fString(request.getParameter("Cond"));
				String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String QUERY= StrUtils.fString(request.getParameter("QUERY"));
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				String extCond="";

				///////////////////////
				ArrayList  movQryList = transportModeUtil.getTranModeList(QUERY,plant,"");
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						String transportmode  = (String)arrCustLine.get("TRANSPORT_MODE");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						resultJsonInt.put("TRANSPORT_MODE", transportmode);
						resultJsonInt.put("CRAT", (String)arrCustLine.get("CRAT"));
						resultJsonInt.put("CRBY", (String)arrCustLine.get("CRBY"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("TRANSPORTMODE", jsonArray);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
				}else {
//	                   jsonArray.add("");
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "99");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);  
				}
//	               resultJson.put("payTypes", jsonArray);
				///////////////////////              
			} catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
		
		private JSONObject getSupplierList(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			CustUtil SupplierUtil = new CustUtil();
			
			try {
				String transport_mode_id = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
				String extraCond = StrUtils.fString(request.getParameter("Cond"));
				String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String QUERY= StrUtils.fString(request.getParameter("QUERY"));
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				String extCond="";

				///////////////////////
				ArrayList  movQryList = SupplierUtil.getVendorTypeList(QUERY,plant,"");
				if (movQryList.size() > 0) {
					for(int i =0; i<movQryList.size(); i++) {
						Map arrCustLine = (Map)movQryList.get(i);
						String transportmode  = (String)arrCustLine.get("SUPPLIER_TYPE_DESC");
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
						resultJsonInt.put("SUPPLIER_TYPE_DESC", transportmode);
						resultJsonInt.put("CRAT", (String)arrCustLine.get("CRAT"));
						resultJsonInt.put("CRBY", (String)arrCustLine.get("CRBY"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("SUPPLIERTYPE", jsonArray);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
				}else {
//	                   jsonArray.add("");
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "99");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);  
				}
//	               resultJson.put("payTypes", jsonArray);
				///////////////////////              
			} catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
		
		private JSONObject createpaymenttype(HttpServletRequest request) 
				throws IOException, ServletException,Exception {
				JSONObject resultJson = new JSONObject();
				String msg = "";
				String  PLANT="",PaymenType="";
				ArrayList alResult = new ArrayList();
				Map map = null;
				MasterDAO _MasterDAO = new MasterDAO();
				try {
		            HttpSession session = request.getSession();
	     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String UserId = (String) session.getAttribute("LOGIN_USER");
					PaymenType = StrUtils.fString(request.getParameter("PAYMENTTYPE"));
					
					if (!_MasterUtil.isExistPaymentType(PaymenType, PLANT)) // if the PaymenType exists already 17.10.18 by Azees
					{						
		    			Hashtable ht = new Hashtable();
						ht.put(IDBConstants.PLANT,PLANT);
						ht.put(IDBConstants.PAYMENTTYPE,PaymenType);
						ht.put(IDBConstants.LOGIN_USER,UserId);
						ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						boolean insertflag = _MasterUtil.AddPaymentType(ht);
													
						if(insertflag){
							resultJson.put("STATUS", "100");
							resultJson.put("PAYMENTTYPE", PaymenType);
							resultJson.put("MESSAGE", "Payment Mode Added Successfully");
						}
						else{
							resultJson.put("STATUS", "99");
							resultJson.put("MESSAGE", "Error in Adding Payment Mode");							
						}
					}
					else
					{
						resultJson.put("STATUS", "99");
						resultJson.put("MESSAGE", "Payment Mode Exists already");
					}								
				}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", e.getMessage());								
				}
				return resultJson;
			}
		
		
		//transport modal imthi start
		
//		Resvi Starts ADDTransportMode
		
		private JSONObject CreateTransportModeModal(HttpServletRequest request,HttpServletResponse response) 
				throws IOException, ServletException,Exception {
				JSONObject resultJson = new JSONObject();
				TransportModeUtil transportModeUtil = new TransportModeUtil();
				String msg = "";
				String  PLANT="",TRANSPORT_MODE="",plant="",userName="",tranId="", TranDesc="";
				ArrayList alResult = new ArrayList();
				Map map = null;
				try {
		            HttpSession session = request.getSession();
	     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
					String UserId = (String) session.getAttribute("LOGIN_USER");
					String transportmode = StrUtils.fString(request.getParameter("transport"));
				
					if (!_MasterUtil.isExistTRANSPORTMODE(transportmode, PLANT)) // if the REMARKS exists already 17.10.18 by Azees
					{
						
	    			Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT,PLANT);
					ht.put(IDBConstants.TRANSPORT_MODE,transportmode);
					ht.put(IDBConstants.LOGIN_USER,UserId);
					ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
//					boolean insertflag = transportModeUtil.insertTransportModeMst(ht);
					
					MovHisDAO mdao = new MovHisDAO(PLANT);
					mdao.setmLogger(mLogger);
					Hashtable<String, String> htm = new Hashtable<String, String>();
					htm.put(IDBConstants.PLANT, PLANT);
					htm.put("DIRTYPE", TransactionConstants.ADD_TRANSPORT_MODE);
					htm.put(IDBConstants.MOVHIS_ORDNUM, transportmode);
					htm.put("REMARKS", transportmode);
					htm.put("RECID", "");
					htm.put("ORDNUM","");
					htm.put("ITEM","");
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", DateUtils.getDateTime());
					htm.put("UPAT", DateUtils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					
					boolean inserted = mdao.insertIntoMovHis(htm);
					int id = transportModeUtil.insertTransportModeModalMst(ht);
												
					if(id > 0 && inserted){
//				if(insertflag){
					resultJson.put("STATUS", "100");
					resultJson.put("transport", transportmode);
					resultJson.put("TRANSPORTID", id);
					msg = "<font class='maingreen'>Transport Mode Added Successfully</font>";
					}
					else{
						resultJson.put("STATUS", "99");
						resultJson.put("MESSAGE","Error in Adding Transport Mode");
						msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Transport Mode</font>";
						
					}
					}
					else
					{
						resultJson.put("STATUS", "99");
						resultJson.put("MESSAGE","Transport Mode Exists already");
						msg = "<font class = "+IDBConstants.FAILED_COLOR +">Transport Mode Exists already</font>";
					}
								
				}catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					msg = "<font class='mainred'>"+e.getMessage()+"</font>";
								
				}
				return resultJson;
			}
		
		//Create by vicky
				private JSONObject CreateClearanceTypetModeModal(HttpServletRequest request,HttpServletResponse response) 
						throws IOException, ServletException,Exception {
						JSONObject resultJson = new JSONObject();
						ClearanceUtil clearanceutil = new ClearanceUtil();
						String msg = "";
						String  PLANT="",plant="",userName="",tranId="", TranDesc="";
						ArrayList alResult = new ArrayList();
						Map map = null;
						try {
				            HttpSession session = request.getSession();
			     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
							String UserId = (String) session.getAttribute("LOGIN_USER");
							String CLEARANCE_TYPE_ID = StrUtils.fString(request.getParameter("CLEARANCE_TYPE_ID"));
							String TYPE = StrUtils.fString(request.getParameter("TYPE"));
					
						 	 Hashtable ht = new Hashtable();
						     ht.put(IDBConstants.PLANT,PLANT);
						     ht.put(IDBConstants.CLEARANCETYPEID,CLEARANCE_TYPE_ID);
							if (!clearanceutil.isExistsClearanceType(ht)) 
							{
								
							ht.put(IDBConstants.PLANT,PLANT);
					        ht.put(IDBConstants.CLEARANCETYPEID,CLEARANCE_TYPE_ID);
					        ht.put(IDBConstants.CLEARANCETYPE,TYPE); 
							ht.put(IDBConstants.LOGIN_USER,UserId);
							ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());

							MovHisDAO mdao = new MovHisDAO(PLANT);
							mdao.setmLogger(mLogger);
							Hashtable<String, String> htm = new Hashtable<String, String>();
							htm.put(IDBConstants.PLANT, PLANT);
							htm.put("DIRTYPE", TransactionConstants.ADD_CLEARANCE_TYPE);
							htm.put("ITEM", CLEARANCE_TYPE_ID);
							htm.put("REMARKS", CLEARANCE_TYPE_ID);
							htm.put("RECID", "");
							htm.put("ORDNUM","");
							htm.put("UPBY", UserId);
							htm.put("CRBY", UserId);
							htm.put("CRAT", DateUtils.getDateTime());
							htm.put("UPAT", DateUtils.getDateTime());
							htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							
							boolean inserted = mdao.insertIntoMovHis(htm);
							boolean id = clearanceutil.insertClearanceTypeMst(ht);
														
							if(id  && inserted){
							resultJson.put("STATUS", "100");
							resultJson.put("CLEARANCE_TYPE_ID", CLEARANCE_TYPE_ID);
							msg = "<font class='maingreen'>Clearance Type Added Successfully</font>";
							}
							else{
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE","Error in Adding Clearance Type");
								msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Clearance Type</font>";
								
							}
							}
							else
							{
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE","Clearance Type Exists already");
								msg = "<font class = "+IDBConstants.FAILED_COLOR +">Clearance Exists already</font>";
							}
										
						}catch (Exception e) {
							this.mLogger.exception(this.printLog, "", e);
							msg = "<font class='mainred'>"+e.getMessage()+"</font>";
										
						}
						return resultJson;
					}
				//Create by vicky
				private JSONObject CreateClearingAgentModeModal(HttpServletRequest request,HttpServletResponse response) 
						throws IOException, ServletException,Exception {
						JSONObject resultJson = new JSONObject();
						List transport = new ArrayList();
						TblControlDAO _TblControlDAO =new TblControlDAO();
						ClearingAgentTypeDAO clearingAgentTypeDAO = new ClearingAgentTypeDAO();
						String msg = "";
						String  PLANT="",userName="",tranId="", TranDesc="";
						int transportCount  = 0, contactnameCount  = 0, telnoCount  = 0, emailCount  = 0;
						List CONTACTNAME=new ArrayList(),TELNO=new ArrayList(),EMAIL=new ArrayList();
						ArrayList alResult = new ArrayList();
						Map map = null;
						try {
				            HttpSession session = request.getSession();
			     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
							String UserId = (String) session.getAttribute("LOGIN_USER");
							String CLEARING_AGENT_ID = StrUtils.fString(request.getParameter("CLEARING_AGENT_ID"));
							String CLEARING_AGENT_NAME = StrUtils.fString(request.getParameter("CLEARING_AGENT_NAME"));		
							boolean isMultipart = ServletFileUpload.isMultipartContent(request);
							if (isMultipart) {
								try {
									FileItemFactory factory = new DiskFileItemFactory();
									ServletFileUpload upload = new ServletFileUpload(factory);

									List items = upload.parseRequest(request);
									Iterator iterator = items.iterator();
									
									String ITEM = "",sLogo="";;
									while (iterator.hasNext()) {
										FileItem item = (FileItem) iterator.next();
										if (item.isFormField()) {
											if (item.getFieldName().equalsIgnoreCase("CLEARING_AGENT_ID")) {
												CLEARING_AGENT_ID = StrUtils.fString(item.getString());
											}
											if (item.getFieldName().equalsIgnoreCase("CLEARING_AGENT_NAME")) {
												CLEARING_AGENT_NAME = StrUtils.fString(item.getString());
											}
											if (item.isFormField()) {
												if (item.getFieldName().equalsIgnoreCase("newtransport")) {
													transport.add(transportCount, StrUtils.fString(item.getString()).trim());
													transportCount++;
												}
											}
											if (item.isFormField()) {
												if (item.getFieldName().equalsIgnoreCase("CONTACTNAME")) {
													CONTACTNAME.add(contactnameCount, StrUtils.fString(item.getString()).trim());
													contactnameCount++;
												}
											}
											if (item.isFormField()) {
												if (item.getFieldName().equalsIgnoreCase("TELNO")) {
													TELNO.add(telnoCount, StrUtils.fString(item.getString()).trim());
													telnoCount++;
												}
											}
											if (item.isFormField()) {
												if (item.getFieldName().equalsIgnoreCase("EMAIL")) {
													EMAIL.add(emailCount, StrUtils.fString(item.getString()).trim());
													emailCount++;
												}
											}
											
										}

									}
										} catch (Exception e) {

										}
							}
							
						 	 Hashtable ht = new Hashtable();
						     ht.put(IDBConstants.PLANT,PLANT);
						     ht.put("CLEARING_AGENT_ID",CLEARING_AGENT_ID);
							if (!new ClearAgentDAO().isExistsClearagent(ht)) 
							{										
							ht.put(IDBConstants.PLANT, PLANT);
							ht.put("CLEARING_AGENT_ID", CLEARING_AGENT_ID);
							ht.put("CLEARING_AGENT_NAME", CLEARING_AGENT_NAME);
							ht.put(IConstants.ISACTIVE, "Y");
							ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							ht.put(IDBConstants.LOGIN_USER, UserId);

							MovHisDAO mdao = new MovHisDAO(PLANT);
							mdao.setmLogger(mLogger);
							Hashtable<String, String> htm = new Hashtable<String, String>();
							htm.put("PLANT", PLANT);
							htm.put("DIRTYPE", "CREATE CLEARING AGENT");
							htm.put("RECID", "");
							htm.put("ITEM",CLEARING_AGENT_ID);
							htm.put("REMARKS",CLEARING_AGENT_NAME);
							htm.put("UPBY", UserId);
							htm.put("CRBY", UserId);
							htm.put("CRAT", dateutils.getDateTime());
							htm.put("UPAT", dateutils.getDateTime());
							htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
							htm.put(IDBConstants.TRAN_DATE, dateutils
									.getDateinyyyy_mm_dd(dateutils.getDate()));

				   		  	boolean updateFlag;
				   			if(CLEARING_AGENT_ID!="CA001"){	
				   			boolean exitFlag = false;
				   			Hashtable htv = new Hashtable();				
				   			htv.put(IDBConstants.PLANT, PLANT);
				   			htv.put(IDBConstants.TBL_FUNCTION, "CLEARING_AGENT");
				   			exitFlag = _TblControlDAO.isExisit(htv, "", PLANT);
				   			if (exitFlag) 
				     		    updateFlag=_TblControlDAO.updateSeqNo("CLEARING_AGENT",PLANT);
				   			else
				   			{
				   				boolean insertFlag = false;
				   				Map htInsert=null;
				               	Hashtable htTblCntInsert  = new Hashtable();           
				               	htTblCntInsert.put(IDBConstants.PLANT,PLANT);          
				               	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CLEARING_AGENT");
				               	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"CA");
				                htTblCntInsert.put("MINSEQ","0000");
				                htTblCntInsert.put("MAXSEQ","9999");
				               	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
				               	htTblCntInsert.put(IDBConstants.CREATED_BY, UserId);
				               	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
				               	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,PLANT);
				   			}
				   		}
				   			
							boolean id = new ClearAgentDAO().insertClearagentMst(ht);
							boolean inserted = mdao.insertIntoMovHis(htm);
							if(id){
								  for(int i =0 ; i < transport.size() ; i++){
										TransportModeDAO transportmodedao = new TransportModeDAO();
										String transportmode = "";
										
											transportmode = transportmodedao.getTransportModeByName(PLANT,(String)transport.get(i));
										
					        			 ClearingAgentTypeDET clearingAgentTypeDET = new ClearingAgentTypeDET();
					        			 clearingAgentTypeDET.setPLANT(PLANT);
					        			 clearingAgentTypeDET.setCLEARING_AGENT_ID(CLEARING_AGENT_ID);
					        			 clearingAgentTypeDET.setTRANSPORTID(Integer.valueOf(transportmode));
					        			 clearingAgentTypeDET.setCONTACTNAME((String)CONTACTNAME.get(i));
					        			 clearingAgentTypeDET.setTELNO((String)TELNO.get(i));
					        			 clearingAgentTypeDET.setEMAIL((String)EMAIL.get(i));
					        			 clearingAgentTypeDET.setCRAT(dateutils.getDate());
					        			 clearingAgentTypeDET.setCRBY(UserId);
					        			 clearingAgentTypeDAO.addClearAgentTypedet(clearingAgentTypeDET);
					        	  	 }
					        	  }
							if(id && inserted){
							resultJson.put("STATUS", "100");
							resultJson.put("CLEARING_AGENT_ID", CLEARING_AGENT_ID);
							resultJson.put("CLEARINGAGENTID", id);
							msg = "<font class='maingreen'>Clearing Agent Added Successfully</font>";
							}
							else{
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE","Error in Adding Clearing Agent ");
								msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Clearing Agent </font>";
								
							}
							}
							else
							{
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE","Clearing Agent Exists already");
								msg = "<font class = "+IDBConstants.FAILED_COLOR +">Clearing Agent Exists already</font>";
							}
										
						}catch (Exception e) {
							this.mLogger.exception(this.printLog, "", e);
							msg = "<font class='mainred'>"+e.getMessage()+"</font>";
										
						}
						return resultJson;
					}
// thanzi Start SupplierTypeModal		
		private JSONObject CreateSuppliertypeModal(HttpServletRequest request) 
				throws IOException, ServletException,Exception {
				JSONObject resultJson = new JSONObject();
				String msg = "";
				ArrayList alResult = new ArrayList();
				Map map = null;
				MasterDAO _MasterDAO = new MasterDAO();
				TblControlDAO _TblControlDAO = new TblControlDAO();
				CustUtil CustUtil = new CustUtil();

				try {
					HttpSession session = request.getSession();
					String plant = (String) session.getAttribute("PLANT");
					String user = (String) session.getAttribute("LOGIN_USER");

					String supplier_type_id = request.getParameter("SUPPLIER_TYPE_ID1").trim();
					String supplier_type_desc = request.getParameter("SUPPLIER_TYPE_DESC1").trim();
					String sItemId="";
					Hashtable ht = new Hashtable();
					if (!CustUtil.isExistSupplierType(supplier_type_id,supplier_type_desc, plant)) //Resvi
					{						
					ht.put("PLANT", plant);
					ht.put("SUPPLIER_TYPE_ID", supplier_type_id);
					ht.put("SUPPLIER_TYPE_DESC", supplier_type_desc);
					ht.put("CRBY", user);
					ht.put("CRAT", dateutils.getDateTime());
					
					ht.put("ISACTIVE", "Y");
					

					MovHisDAO mdao = new MovHisDAO(plant);
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE", TransactionConstants.ADD_SUPPLIER_TYPE);
					htm.put("RECID", "");
					htm.put("ITEM",supplier_type_id);
					htm.put("REMARKS", supplier_type_desc);
					htm.put("CRBY", user);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPBY", user);
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
					htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
		            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		             
					
					boolean updateFlag;
					if(sItemId!="ST001")
						  {	
						boolean exitFlag = false;
						Hashtable htv = new Hashtable();				
						htv.put(IDBConstants.PLANT, plant);
						htv.put(IDBConstants.TBL_FUNCTION, "SUPPLIERTYPE");
						exitFlag = _TblControlDAO.isExisit(htv, "", plant);
						if (exitFlag) 
							updateFlag=_TblControlDAO.updateSeqNo("SUPPLIERTYPE",plant);				
					else
					{
						boolean insertFlag = false;
						Map htInsert = null;
						Hashtable htTblCntInsert = new Hashtable();
						htTblCntInsert.put(IDBConstants.PLANT, plant);
						htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SUPPLIERTYPE");
						htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "ST");
						htTblCntInsert.put("MINSEQ", "000");
						htTblCntInsert.put("MAXSEQ", "999");
						htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
						htTblCntInsert.put(IDBConstants.CREATED_BY, user);
						htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
						insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
					}
					}
					boolean flag = CustUtil.insertSupplierTypeMst(ht);
					
					
					boolean inserted = mdao.insertIntoMovHis(htm);
					if (flag && inserted) {
						resultJson.put("STATUS", "100");
						resultJson.put("SUPPLIER_TYPE_ID", supplier_type_id);
						resultJson.put("SUPPLIER_TYPE_DESC", supplier_type_desc);
						resultJson.put("MESSAGE", "Supplier Group Added Successfully");

					} else {
						resultJson.put("STATUS", "99");
						resultJson.put("MESSAGE", "Error in Adding Supplier Group");		
					}
					}
				
				else
				{
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", "Supplier Group  Exists already");
				}
				

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", e.getMessage());		
					throw e;
				}
			return resultJson;
		}
// end
		
		// thanzi Start CustomerTypeModal		
				private JSONObject CreateCustomertypeModal(HttpServletRequest request) 
						throws IOException, ServletException,Exception {
						JSONObject resultJson = new JSONObject();
						String msg = "";
						ArrayList alResult = new ArrayList();
						Map map = null;
						MasterDAO _MasterDAO = new MasterDAO();
						TblControlDAO _TblControlDAO = new TblControlDAO();
						CustUtil CustUtil = new CustUtil();

						try {
							HttpSession session = request.getSession();
							String plant = (String) session.getAttribute("PLANT");
							String user = (String) session.getAttribute("LOGIN_USER");

							String customer_type_id = request.getParameter("CUSTOMER_TYPE_ID1").trim();
							String customer_type_desc = request.getParameter("CUSTOMER_TYPE_DESC1").trim();
							String sItemId="";
							Hashtable ht = new Hashtable();
							if (!CustUtil.isExistCustomerType(customer_type_id,customer_type_desc, plant)) //Resvi
							{						
							ht.put("PLANT", plant);
							ht.put("CUSTOMER_TYPE_ID", customer_type_id);
							ht.put("CUSTOMER_TYPE_DESC", customer_type_desc);
							ht.put("CRBY", user);
							ht.put("CRAT", dateutils.getDateTime());
							
							ht.put("ISACTIVE", "Y");
							

							MovHisDAO mdao = new MovHisDAO(plant);
							mdao.setmLogger(mLogger);
							Hashtable htm = new Hashtable();
							htm.put("PLANT", plant);
							htm.put("DIRTYPE", TransactionConstants.ADD_CUSTOMER_TYPE);
							htm.put("RECID", "");
							htm.put("ITEM",customer_type_id);
							htm.put("REMARKS", customer_type_desc);
							htm.put("CRBY", user);
							htm.put("CRAT", dateutils.getDateTime());
							htm.put("UPBY", user);
							htm.put("UPAT", dateutils.getDateTime());
							htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
							htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
				            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				             
							
							boolean updateFlag;
							if(sItemId!="CT001")
								  {	
								boolean exitFlag = false;
								Hashtable htv = new Hashtable();				
								htv.put(IDBConstants.PLANT, plant);
								htv.put(IDBConstants.TBL_FUNCTION, "CUSTOMERTYPE");
								exitFlag = _TblControlDAO.isExisit(htv, "", plant);
								if (exitFlag) 
									updateFlag=_TblControlDAO.updateSeqNo("CUSTOMERTYPE",plant);				
							else
							{
								boolean insertFlag = false;
								Map htInsert = null;
								Hashtable htTblCntInsert = new Hashtable();
								htTblCntInsert.put(IDBConstants.PLANT, plant);
								htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
								htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "CT");
								htTblCntInsert.put("MINSEQ", "000");
								htTblCntInsert.put("MAXSEQ", "999");
								htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
								htTblCntInsert.put(IDBConstants.CREATED_BY, user);
								htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
								insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
							}
							}
							boolean flag = CustUtil.insertCustomerTypeMst(ht);
							
							
							boolean inserted = mdao.insertIntoMovHis(htm);
							if (flag && inserted) {
								resultJson.put("STATUS", "100");
								resultJson.put("CUSTOMER_TYPE_ID", customer_type_id);
								resultJson.put("CUSTOMER_TYPE_DESC", customer_type_desc);
								resultJson.put("MESSAGE", "Customer Group Added Successfully");

							} else {
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE", "Error in Adding Customer Group");		
							}
							}
						
						else
						{
							resultJson.put("STATUS", "99");
							resultJson.put("MESSAGE", "Customer Group  Exists already");
						}
						

						} catch (Exception e) {
							this.mLogger.exception(this.printLog, "", e);
							resultJson.put("STATUS", "99");
							resultJson.put("MESSAGE", e.getMessage());		
							throw e;
						}
					return resultJson;
				}
		// end
		
		
//		Resvi Starts ADDPRODUCTDEPARTMENT
		
				private JSONObject createproductdepartment(HttpServletRequest request) 
						throws IOException, ServletException,Exception {
						JSONObject resultJson = new JSONObject();
						String msg = "";
						ArrayList alResult = new ArrayList();
						Map map = null;
						MasterDAO _MasterDAO = new MasterDAO();
						TblControlDAO _TblControlDAO = new TblControlDAO();
						PrdDeptUtil PrdDeptUtil = new PrdDeptUtil();

						try {
							HttpSession session = request.getSession();
							String plant = (String) session.getAttribute("PLANT");
							String user = (String) session.getAttribute("LOGIN_USER");

							String prd_dept_id = request.getParameter("PRD_DEPT_ID1").trim();
							String prd_dept_desc = request.getParameter("PRD_DEPT_DESC1").trim();
							String sItemId="";
							
							if (!PrdDeptUtil.isExistPRDDEPT(prd_dept_id,prd_dept_desc, plant)) //Resvi
							{						
							Hashtable ht = new Hashtable();
							ht.put("PLANT", plant);
							ht.put("PRD_DEPT_ID", prd_dept_id);
							ht.put("PRD_DEPT_DESC", prd_dept_desc);
							ht.put("CRBY", user);
							ht.put("CRAT", dateutils.getDateTime());
							
							ht.put("ISACTIVE", "Y");
							

							MovHisDAO mdao = new MovHisDAO(plant);
							mdao.setmLogger(mLogger);
							Hashtable htm = new Hashtable();
							htm.put("PLANT", plant);
							htm.put("DIRTYPE", TransactionConstants.ADD_PRDDEP);
							htm.put("RECID", "");
							htm.put("ITEM",prd_dept_id);
							htm.put("REMARKS", prd_dept_desc);
							htm.put("CRBY", user);
							htm.put("CRAT", dateutils.getDateTime());
							htm.put("UPBY", user);
							htm.put("UPAT", dateutils.getDateTime());
							htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
							htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
				            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				             
							
							boolean updateFlag;
							if(sItemId!="PD001")
								  {	
								boolean exitFlag = false;
								Hashtable htv = new Hashtable();				
								htv.put(IDBConstants.PLANT, plant);
								htv.put(IDBConstants.TBL_FUNCTION, "PRDDEPT");
								exitFlag = _TblControlDAO.isExisit(htv, "", plant);
								if (exitFlag) 
									updateFlag=_TblControlDAO.updateSeqNo("PRDDEPT",plant);				
							else
							{
								boolean insertFlag = false;
								Map htInsert = null;
								Hashtable htTblCntInsert = new Hashtable();
								htTblCntInsert.put(IDBConstants.PLANT, plant);
								htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDDEPT");
								htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PD");
								htTblCntInsert.put("MINSEQ", "0000");
								htTblCntInsert.put("MAXSEQ", "9999");
								htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
								htTblCntInsert.put(IDBConstants.CREATED_BY, user);
								htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
								insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
							}
							}
							boolean flag = PrdDeptUtil.insertPrdDepMst(ht);
							
							
							boolean inserted = mdao.insertIntoMovHis(htm);
							if (flag && inserted) {
								resultJson.put("STATUS", "100");
								resultJson.put("PRD_DEPT_ID", prd_dept_id);
								resultJson.put("PRD_DEPT_DESC", prd_dept_desc);
								resultJson.put("MESSAGE", "Product Department Added Successfully");

							} else {
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE", "Error in Adding Product Department");		
							}
							}
						
						else
						{
							resultJson.put("STATUS", "99");
							resultJson.put("MESSAGE", "Product Department  Exists already");
						}
						

						} catch (Exception e) {
							this.mLogger.exception(this.printLog, "", e);
							resultJson.put("STATUS", "99");
							resultJson.put("MESSAGE", e.getMessage());		
							throw e;
						}
					return resultJson;
				}

//				ENDS
				
				
//				Thanzith Starts ADDPRODUCTCATEGORY
				
						private JSONObject createproductcategory(HttpServletRequest request) 
								throws IOException, ServletException,Exception {
								JSONObject resultJson = new JSONObject();
								String msg = "";
								ArrayList alResult = new ArrayList();
								Map map = null;
								MasterDAO _MasterDAO = new MasterDAO();
								TblControlDAO _TblControlDAO = new TblControlDAO();
								PrdClassUtil PrdClasstUtil = new PrdClassUtil();

								try {
									HttpSession session = request.getSession();
									String plant = (String) session.getAttribute("PLANT");
									String user = (String) session.getAttribute("LOGIN_USER");

									String prd_cls_id = request.getParameter("PRD_CLS_ID1").trim();
									String prd_cls_desc = request.getParameter("PRD_CLS_DESC1").trim();
									String sItemId="";
									
									if (!PrdClasstUtil.isExistPRDCAT(prd_cls_id,prd_cls_desc, plant)) //Thanzith
									{						
									Hashtable ht = new Hashtable();
									ht.put("PLANT", plant);
									ht.put("PRD_CLS_ID", prd_cls_id);
									ht.put("PRD_CLS_DESC", prd_cls_desc);
									ht.put("CRBY", user);
									ht.put("CRAT", dateutils.getDateTime());
									
									ht.put("ISACTIVE", "Y");
									

									MovHisDAO mdao = new MovHisDAO(plant);
									mdao.setmLogger(mLogger);
									Hashtable htm = new Hashtable();
									htm.put("PLANT", plant);
									htm.put("DIRTYPE", TransactionConstants.ADD_PRDCLS);
									htm.put("RECID", "");
									htm.put("ITEM",prd_cls_id);
									htm.put("REMARKS", prd_cls_desc);
									htm.put("CRBY", user);
									htm.put("CRAT", dateutils.getDateTime());
									htm.put("UPBY", user);
									htm.put("UPAT", dateutils.getDateTime());
									htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
									htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
						            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
						             
									
									boolean updateFlag;
									if(sItemId!="PC001")
										  {	
										boolean exitFlag = false;
										Hashtable htv = new Hashtable();				
										htv.put(IDBConstants.PLANT, plant);
										htv.put(IDBConstants.TBL_FUNCTION, "PRDCLASS");
										exitFlag = _TblControlDAO.isExisit(htv, "", plant);
										if (exitFlag) 
											updateFlag=_TblControlDAO.updateSeqNo("PRDCLASS",plant);				
									else
									{
										boolean insertFlag = false;
										Map htInsert = null;
										Hashtable htTblCntInsert = new Hashtable();
										htTblCntInsert.put(IDBConstants.PLANT, plant);
										htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDCLASS");
										htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PC");
										htTblCntInsert.put("MINSEQ", "000");
										htTblCntInsert.put("MAXSEQ", "999");
										htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
										htTblCntInsert.put(IDBConstants.CREATED_BY, user);
										htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
										insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
									}
									}
									boolean flag = PrdClasstUtil.insertPrdClsMst(ht);
									
									
									boolean inserted = mdao.insertIntoMovHis(htm);
									if (flag && inserted) {
										resultJson.put("STATUS", "100");
										resultJson.put("PRD_CLS_ID", prd_cls_id);
										resultJson.put("PRD_CLS_DESC", prd_cls_desc);
										resultJson.put("MESSAGE", "Product Category Added Successfully");

									} else {
										resultJson.put("STATUS", "99");
										resultJson.put("MESSAGE", "Error in Adding Product category");		
									}
									}
								
								else
								{
									resultJson.put("STATUS", "99");
									resultJson.put("MESSAGE", "Product Category  Exists already");
								}
								

								} catch (Exception e) {
									this.mLogger.exception(this.printLog, "", e);
									resultJson.put("STATUS", "99");
									resultJson.put("MESSAGE", e.getMessage());		
									throw e;
								}
							return resultJson;
						}

//						ENDS
//				Thanzith Starts ADDPRODUCTLOCATION
						
						private JSONObject createproductlocation(HttpServletRequest request) 
								throws IOException, ServletException,Exception {
							JSONObject resultJson = new JSONObject();
							String msg = "";
							ArrayList alResult = new ArrayList();
							Map map = null;
							MasterDAO _MasterDAO = new MasterDAO();
							TblControlDAO _TblControlDAO = new TblControlDAO();
							LocUtil _locUtil = new LocUtil();
							
							try {
								HttpSession session = request.getSession();
								String plant = (String) session.getAttribute("PLANT");
								String user = (String) session.getAttribute("LOGIN_USER");
								
								String prd_loc_id = request.getParameter("LOC_ID1").trim();
								String prd_loc_desc = request.getParameter("LOC_DESC1").trim();
								String sItemId="";
								
								if (!_locUtil.isExistPRDLoc(prd_loc_id,prd_loc_desc, plant)) //Thanzith
								{						
									Hashtable ht = new Hashtable();
									ht.put("PLANT", plant);
									ht.put("LOC", prd_loc_id);
									ht.put("LOCDESC", prd_loc_desc);
									ht.put("WHID", "");
									ht.put("LOC_TYPE_ID", "");
									ht.put("LOC_TYPE_ID2", "");
									ht.put("LOC_TYPE_ID3", "");
									ht.put("ISACTIVE", "");
									ht.put("USERFLD1", "");
									ht.put("COMNAME", "");
									ht.put("RCBNO", "");
									ht.put("ADD1", "");
									ht.put("ADD2", "");
									ht.put("ADD3", "");
									ht.put("ADD4", "");
									ht.put("STATE", "");
									ht.put("COUNTRY", "");
									ht.put("ZIP", "");
									ht.put("TELNO", "");
									ht.put("FAX", "");
									ht.put("CRBY", user);
									ht.put("CHKSTATUS", "");
									ht.put("CRAT", dateutils.getDateTime());
									
									ht.put("ISACTIVE", "Y");
									
									
									MovHisDAO mdao = new MovHisDAO(plant);
									mdao.setmLogger(mLogger);
									Hashtable htm = new Hashtable();
									htm.put("PLANT", plant);
									htm.put("DIRTYPE", TransactionConstants.ADD_LOC);
									htm.put("RECID", "");
									htm.put("ITEM",prd_loc_id);
									htm.put("REMARKS", prd_loc_desc);
									htm.put("CRBY", user);
									htm.put("CRAT", dateutils.getDateTime());
									htm.put("UPBY", user);
									htm.put("UPAT", dateutils.getDateTime());
									htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
									htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
									htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
									
									
									boolean updateFlag;
									if(sItemId!="L0001")
									{	
										boolean exitFlag = false;
										Hashtable htv = new Hashtable();				
										htv.put(IDBConstants.PLANT, plant);
										htv.put(IDBConstants.TBL_FUNCTION, "LOCATION");
										exitFlag = _TblControlDAO.isExisit(htv, "", plant);
										if (exitFlag) 
											updateFlag=_TblControlDAO.updateSeqNo("LOCATION",plant);				
										else
										{
											boolean insertFlag = false;
											Map htInsert = null;
											Hashtable htTblCntInsert = new Hashtable();
											htTblCntInsert.put(IDBConstants.PLANT, plant);
											htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCATION");
											htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "L");
											htTblCntInsert.put("MINSEQ", "0000");
											htTblCntInsert.put("MAXSEQ", "9999");
											htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
											htTblCntInsert.put(IDBConstants.CREATED_BY, user);
											htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
											insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
										}
									}
									boolean flag = _locUtil.insertPrdLocMst(ht);
									
									
									boolean inserted = mdao.insertIntoMovHis(htm);
									if (flag && inserted) {
										resultJson.put("STATUS", "100");
										resultJson.put("LOC", prd_loc_id);
										resultJson.put("LOCDESC", prd_loc_desc);
										resultJson.put("MESSAGE", "Product Location Added Successfully");
										
									} else {
										resultJson.put("STATUS", "99");
										resultJson.put("MESSAGE", "Error in Adding Product Location");		
									}
								}
								
								else
								{
									resultJson.put("STATUS", "99");
									resultJson.put("MESSAGE", "Product Location  Exists already");
								}
								
								
							} catch (Exception e) {
								this.mLogger.exception(this.printLog, "", e);
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE", e.getMessage());		
								throw e;
							}
							return resultJson;
						}
						
//						ENDS
						
						
//						Thanzith Starts ADDPRODUCTSUBCATEGORY
						
								private JSONObject createproductsubcategory(HttpServletRequest request) 
										throws IOException, ServletException,Exception {
										JSONObject resultJson = new JSONObject();
										String msg = "";
										ArrayList alResult = new ArrayList();
										Map map = null;
										MasterDAO _MasterDAO = new MasterDAO();
										TblControlDAO _TblControlDAO = new TblControlDAO();
										PrdTypeUtil PrdTypeUtil = new PrdTypeUtil();

										try {
											HttpSession session = request.getSession();
											String plant = (String) session.getAttribute("PLANT");
											String user = (String) session.getAttribute("LOGIN_USER");

											String prd_type_id = request.getParameter("ITEM_ID1").trim();
											String prd_type_desc = request.getParameter("ITEM_DESC1").trim();
											String sItemId="";
											
											if (!PrdTypeUtil.isExistPrdSubCat(prd_type_id,prd_type_desc, plant)) //Thanzith
											{						
											Hashtable ht = new Hashtable();
											ht.put("PLANT", plant);
											ht.put("PRD_TYPE_ID", prd_type_id);
											ht.put("PRD_TYPE_DESC", prd_type_desc);
											ht.put("CRBY", user);
											ht.put("CRAT", dateutils.getDateTime());
											
											ht.put("ISACTIVE", "Y");
											

											MovHisDAO mdao = new MovHisDAO(plant);
											mdao.setmLogger(mLogger);
											Hashtable htm = new Hashtable();
											htm.put("PLANT", plant);
											htm.put("DIRTYPE", TransactionConstants.ADD_PRDTYPE);
											htm.put("RECID", "");
											htm.put("ITEM",prd_type_id);
											htm.put("REMARKS", prd_type_desc);
											htm.put("CRBY", user);
											htm.put("CRAT", dateutils.getDateTime());
											htm.put("UPBY", user);
											htm.put("UPAT", dateutils.getDateTime());
											htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
											htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
								            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
								             
											
											boolean updateFlag;
											if(sItemId!="PT001")
												  {	
												boolean exitFlag = false;
												Hashtable htv = new Hashtable();				
												htv.put(IDBConstants.PLANT, plant);
												htv.put(IDBConstants.TBL_FUNCTION, "PRDTYPE");
												exitFlag = _TblControlDAO.isExisit(htv, "", plant);
												if (exitFlag) 
													updateFlag=_TblControlDAO.updateSeqNo("PRDTYPE",plant);				
											else
											{
												boolean insertFlag = false;
												Map htInsert = null;
												Hashtable htTblCntInsert = new Hashtable();
												htTblCntInsert.put(IDBConstants.PLANT, plant);
												htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDTYPE");
												htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PT");
												htTblCntInsert.put("MINSEQ", "000");
												htTblCntInsert.put("MAXSEQ", "999");
												htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
												htTblCntInsert.put(IDBConstants.CREATED_BY, user);
												htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
												insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
											}
											}
											boolean flag = PrdTypeUtil.insertPrdTypeMst(ht);
											
											
											boolean inserted = mdao.insertIntoMovHis(htm);
											if (flag && inserted) {
												resultJson.put("STATUS", "100");
												resultJson.put("PRD_TYPE_ID", prd_type_id);
												resultJson.put("PRD_TYPE_DESC", prd_type_desc);
												resultJson.put("MESSAGE", "Product Sub Category Added Successfully");

											} else {
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", "Error in Adding Product Sub category");		
											}
											}
										
										else
										{
											resultJson.put("STATUS", "99");
											resultJson.put("MESSAGE", "Product Sub Category  Exists already");
										}
										

										} catch (Exception e) {
											this.mLogger.exception(this.printLog, "", e);
											resultJson.put("STATUS", "99");
											resultJson.put("MESSAGE", e.getMessage());		
											throw e;
										}
									return resultJson;
								}

//								ENDS
						
				
						//		Thanzith Starts ADDPRODUCTBRAND
								
								private JSONObject createproductbrand(HttpServletRequest request) 
										throws IOException, ServletException,Exception {
										JSONObject resultJson = new JSONObject();
										String msg = "";
										ArrayList alResult = new ArrayList();
										Map map = null;
										MasterDAO _MasterDAO = new MasterDAO();
										TblControlDAO _TblControlDAO = new TblControlDAO();
										PrdBrandUtil PrdBrandUtil = new PrdBrandUtil();

										try {
											HttpSession session = request.getSession();
											String plant = (String) session.getAttribute("PLANT");
											String user = (String) session.getAttribute("LOGIN_USER");

											String prd_brand_id = request.getParameter("ITEM_ID1").trim();
											String prd_brand_desc = request.getParameter("ITEM_DESC1").trim();
											String sItemId="";
											
											if (!PrdBrandUtil.isExistPrdBrand(prd_brand_id,prd_brand_desc, plant)) //Thanzith
											{						
											Hashtable ht = new Hashtable();
											ht.put("PLANT", plant);
											ht.put("PRD_BRAND_ID", prd_brand_id);
											ht.put("PRD_BRAND_DESC", prd_brand_desc);
											ht.put("CRBY", user);
											ht.put("CRAT", dateutils.getDateTime());
											
											ht.put("ISACTIVE", "Y");
											

											MovHisDAO mdao = new MovHisDAO(plant);
											mdao.setmLogger(mLogger);
											Hashtable htm = new Hashtable();
											htm.put("PLANT", plant);
											htm.put("DIRTYPE", TransactionConstants.ADD_PRDBRAND);
											htm.put("RECID", "");
											htm.put("ITEM",prd_brand_id);
											htm.put("REMARKS", prd_brand_desc);
											htm.put("CRBY", user);
											htm.put("CRAT", dateutils.getDateTime());
											htm.put("UPBY", user);
											htm.put("UPAT", dateutils.getDateTime());
											htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
											htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
								            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
								             
											
											boolean updateFlag;
											if(sItemId!="PB001")
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
												Map htInsert = null;
												Hashtable htTblCntInsert = new Hashtable();
												htTblCntInsert.put(IDBConstants.PLANT, plant);
												htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDBRAND");
												htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PB");
												htTblCntInsert.put("MINSEQ", "000");
												htTblCntInsert.put("MAXSEQ", "999");
												htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
												htTblCntInsert.put(IDBConstants.CREATED_BY, user);
												htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
												insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
											}
											}
											boolean flag = PrdBrandUtil.insertPrdBrandMst(ht);
											
											
											boolean inserted = mdao.insertIntoMovHis(htm);
											if (flag && inserted) {
												resultJson.put("STATUS", "100");
												resultJson.put("PRD_BRAND_ID", prd_brand_id);
												resultJson.put("PRD_BRAND_DESC", prd_brand_desc);
												resultJson.put("MESSAGE", "Product Brand Added Successfully");

											} else {
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", "Error in Adding Product Brand");		
											}
											}
										
										else
										{
											resultJson.put("STATUS", "99");
											resultJson.put("MESSAGE", "Product Brand Exists already");
										}
										

										} catch (Exception e) {
											this.mLogger.exception(this.printLog, "", e);
											resultJson.put("STATUS", "99");
											resultJson.put("MESSAGE", e.getMessage());		
											throw e;
										}
									return resultJson;
								}

//								ENDS
								
								
//								Thanzith Starts ADDPRODUCTHSCODE	
								private JSONObject createproducthscode(HttpServletRequest request) 
										throws IOException, ServletException,Exception {
										JSONObject resultJson = new JSONObject();
										String msg = "";
										String  PLANT="",Hscode="";
										ArrayList alResult = new ArrayList();
										Map map = null;
										MasterDAO _MasterDAO = new MasterDAO();
										try {
								            HttpSession session = request.getSession();
							     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
											String UserId = (String) session.getAttribute("LOGIN_USER");
											Hscode = StrUtils.fString(request.getParameter("ITEM_ID1"));
											
											if (!_MasterUtil.isExistHSCODE(Hscode, PLANT)) 
											{						
								    			Hashtable ht = new Hashtable();
												ht.put(IDBConstants.PLANT,PLANT);
												ht.put(IDBConstants.HSCODE,Hscode);
												ht.put(IDBConstants.LOGIN_USER,UserId);
												ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
												boolean insertflag = _MasterUtil.AddHSCODE(ht);
												
												
												if(insertflag){
													resultJson.put("STATUS", "100");
													resultJson.put("HSCODE", Hscode);
													resultJson.put("MESSAGE", "HS Code Added Successfully");
												}
												else{
													resultJson.put("STATUS", "99");
													resultJson.put("MESSAGE", "Failed to add New HS Code");							
												}
											}
											else
											{
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", "HS Code Exists already. Try again");
											}								
										}catch (Exception e) {
											this.mLogger.exception(this.printLog, "", e);
											resultJson.put("STATUS", "99");
											resultJson.put("MESSAGE", e.getMessage());								
										}
										return resultJson;
									}
								
//								Thanzith Starts ADDPRODUCTCOO
								private JSONObject createproductcoo(HttpServletRequest request) 
										throws IOException, ServletException,Exception {
										JSONObject resultJson = new JSONObject();
										String msg = "";
										String  PLANT="",Coo="";
										ArrayList alResult = new ArrayList();
										Map map = null;
										MasterDAO _MasterDAO = new MasterDAO();
										try {
								            HttpSession session = request.getSession();
							     			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
											String UserId = (String) session.getAttribute("LOGIN_USER");
											Coo = StrUtils.fString(request.getParameter("ITEM_ID1"));
											
											if (!_MasterUtil.isExistCOO(Coo, PLANT)) 
											{						
								    			Hashtable ht = new Hashtable();
												ht.put(IDBConstants.PLANT,PLANT);
												ht.put(IDBConstants.COO,Coo);
												ht.put(IDBConstants.LOGIN_USER,UserId);
												ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
												boolean insertflag = _MasterUtil.AddCOO(ht);
												
												
												if(insertflag){
													resultJson.put("STATUS", "100");
													resultJson.put("COO", Coo);
													resultJson.put("MESSAGE", "COO Added Successfully");
												}
												else{
													resultJson.put("STATUS", "99");
													resultJson.put("MESSAGE", "Failed to add New COO");							
												}
											}
											else
											{
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", "COO Exists already. Try again");
											}								
										}catch (Exception e) {
											this.mLogger.exception(this.printLog, "", e);
											resultJson.put("STATUS", "99");
											resultJson.put("MESSAGE", e.getMessage());								
										}
										return resultJson;
									}
								
//								Thanzith Starts ADDPRODUCTUOM
								
								private JSONObject createproductuom(HttpServletRequest request) 
										throws IOException, ServletException,Exception {
										JSONObject resultJson = new JSONObject();
										String msg = "";
										String  plant="",uom="",  UomDesc ="",Display = "",QtyPerUom = "";
										ArrayList alResult = new ArrayList();
										Map map = null;
										UomDAO UomDAO = new UomDAO();
										UomUtil uomUtil = new UomUtil();
										try {
								            HttpSession session = request.getSession();
							     			plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
											String UserId = (String) session.getAttribute("LOGIN_USER");
											uom = StrUtils.fString(request.getParameter("ITEM_ID1"));
											UomDesc = StrUtils.fString(request.getParameter("ITEM_DESC1"));
											if(UomDesc.equalsIgnoreCase(""))
												UomDesc = StrUtils.fString(request.getParameter("ITEM_ID_DESC1"));
											Display = StrUtils.fString(request.getParameter("Display"));
											QtyPerUom = StrUtils.fString(request.getParameter("QPUOM"));
										
																
								    			Hashtable ht = new Hashtable();
								    			ht.put(IDBConstants.PLANT, plant);
								    			ht.put(IDBConstants.UOMCODE, uom);
								    			if (!(uomUtil.isExistsUom(ht)))
												{	
												ht.put(IDBConstants.PLANT,plant);
												ht.put(IDBConstants.UOMCODE, uom);
												ht.put(IDBConstants.UOMDESC, UomDesc);
												ht.put(IConstants.ISACTIVE, "Y");
												ht.put("Display",Display);
												ht.put("QPUOM",QtyPerUom);
												ht.put(IDBConstants.LOGIN_USER,UserId);
												ht.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
												
												MovHisDAO mdao = new MovHisDAO(plant);
												mdao.setmLogger(mLogger);
												Hashtable htm = new Hashtable();
												htm.put(IDBConstants.PLANT, plant);
												htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_UOM);
												htm.put("RECID", "");
												htm.put("ITEM",uom);
												htm.put(IDBConstants.UPBY, UserId);
												htm.put(IDBConstants.REMARKS, UomDesc);
												htm.put(IDBConstants.CREATED_BY, UserId);
												htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
												htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
												htm.put(IDBConstants.TRAN_DATE, dateutils
														.getDateinyyyy_mm_dd(dateutils.getDate()));

												boolean insertflag = uomUtil.insertUom(ht);
												boolean inserted = mdao.insertIntoMovHis(htm);
												
												
												
												if(insertflag){
													resultJson.put("STATUS", "100");
													resultJson.put("UOMCODE", uom);
													resultJson.put("UOMDESC", UomDesc);
													resultJson.put("Display", Display);
													resultJson.put("QPUOM", QtyPerUom);
													resultJson.put("MESSAGE", "UOM Added Successfully");
												}
												else{
													resultJson.put("STATUS", "99");
													resultJson.put("MESSAGE", "Failed to add New UOM");							
												}
											}
											else
											{
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", "UOM Exists already. Try again");
											}								
										}catch (Exception e) {
											this.mLogger.exception(this.printLog, "", e);
											resultJson.put("STATUS", "99");
											resultJson.put("MESSAGE", e.getMessage());								
										}
										return resultJson;
									}
		
		
//RESVI STARTS
		private JSONObject createordertype(HttpServletRequest request) 
				throws IOException, ServletException,Exception {
				JSONObject resultJson = new JSONObject();
				String msg = "";
				ArrayList alResult = new ArrayList();
				Map map = null;
				MasterDAO _MasterDAO = new MasterDAO();
				OrderTypeUtil _OrderTypeUtil = new OrderTypeUtil();

				try {
					HttpSession session = request.getSession();
					String plant = (String) session.getAttribute("PLANT");
					String user = (String) session.getAttribute("LOGIN_USER");

					String ordertype = request.getParameter("ORDERTYPE").trim();
					String orderdesc = request.getParameter("DESCRIPTION").trim();
					String type = request.getParameter("TYPE").trim();
					String remarks = request.getParameter("REMARKS").trim();
					
					if (!new OrderTypeBeanDAO().isExists(ordertype, type,plant)) // if the PaymenType exists already 17.10.18 by Azees
					{						
					Hashtable ht = new Hashtable();
					ht.put("PLANT", plant);
					ht.put("ORDERTYPE", ordertype);
					ht.put("TYPE", type);
					ht.put("ORDERDESC", orderdesc);
					ht.put("REMARKS", remarks);
					ht.put("ISACTIVE", "Y");
					

					MovHisDAO mdao = new MovHisDAO(plant);
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE", TransactionConstants.ADD_ORDERTYPE);
					htm.put("RECID", "");
					htm.put("CRBY", user);
					htm.put("REMARKS", remarks);
					//htm.put("LOC", locId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils
							.getDateinyyyy_mm_dd(dateutils.getDate()));

					boolean flag = _OrderTypeUtil.addOrderType(ht);
					
					
					boolean inserted = mdao.insertIntoMovHis(htm);
					if (flag && inserted) {
						request.getSession().setAttribute("orderTypeData", ht);
						resultJson.put("STATUS", "100");
						resultJson.put("ORDERTYPE", ordertype);
						resultJson.put("MESSAGE", "Order Type Added Successfully");

					} else {
						resultJson.put("STATUS", "99");
						resultJson.put("MESSAGE", "Error in Adding Order Type");		
					}
					}
				
				else
				{
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", "Order Type  Exists already");
				}
				

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", e.getMessage());		
					throw e;
				}
			return resultJson;
		}

//		ENDS
		
         //RESVI STARTS location type 1
		
				private JSONObject createlocationtype(HttpServletRequest request) 
						throws IOException, ServletException,Exception {
						JSONObject resultJson = new JSONObject();
						String msg = "";
						
						ArrayList alResult = new ArrayList();
						Map map = null;
						MasterDAO _MasterDAO = new MasterDAO();
						LocTypeUtil loctypeutil = new LocTypeUtil();
			
						
						
						TblControlDAO _TblControlDAO =new TblControlDAO();
						
						try {
							HttpSession session = request.getSession();
							String sPlant = (String) session.getAttribute("PLANT");
							
							String plant = (String) session.getAttribute("PLANT");
							String user = (String) session.getAttribute("LOGIN_USER");
							String locDesc = request.getParameter("LOC_DESC").trim();
							String loctypeid = request.getParameter("LOC_TYPE_ID1").trim();

						    Hashtable ht = new Hashtable();
						    ht.put(IDBConstants.PLANT,sPlant);
						    ht.put(IDBConstants.LOCTYPEID,loctypeid);
						    
						     if(!(loctypeutil.isExistsLocType(ht))) // if the Item  exists already
						     {
						           ht.put(IDBConstants.PLANT,sPlant);
						           ht.put(IDBConstants.LOCTYPEID,loctypeid);
						           ht.put(IDBConstants.LOCTYPEDESC,locDesc); 
						           ht.put(IConstants.ISACTIVE,"Y");
						           ht.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
						           ht.put(IDBConstants.LOGIN_USER,user);
						     
						           MovHisDAO mdao = new MovHisDAO(sPlant);
						           mdao.setmLogger(mLogger);
						           Hashtable htm = new Hashtable();
						           htm.put("PLANT",sPlant);
						           htm.put("DIRTYPE",TransactionConstants.ADD_LOCTYPE);
						           htm.put("RECID","");
						           htm.put("ITEM",loctypeid);
						           htm.put("REMARKS",locDesc);
						           htm.put("UPBY",user);   htm.put("CRBY",user);
						            htm.put("CRAT",dateutils.getDateTime());
						            htm.put("UPAT",dateutils.getDateTime());
						            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
						                  
						            htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
						            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
						            
						  		  boolean updateFlag;
						  		 if(loctypeid!="LT001")
						    		  {	
						  			boolean exitFlag = false;
						  			Hashtable htv = new Hashtable();				
						  			htv.put(IDBConstants.PLANT, plant);
						  			htv.put(IDBConstants.TBL_FUNCTION, "LOCTYPE");
						  			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
						  			if (exitFlag) 
						    		    updateFlag=_TblControlDAO.updateSeqNo("LOCTYPE",plant);
						  			else
						  			{
						  				boolean insertFlag = false;
						  				Map htInsert=null;
						              	Hashtable htTblCntInsert  = new Hashtable();           
						              	htTblCntInsert.put(IDBConstants.PLANT,plant);          
						              	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
						              	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"LT");
						               	htTblCntInsert.put("MINSEQ","0000");
						               	htTblCntInsert.put("MAXSEQ","9999");
						              	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
						              	htTblCntInsert.put(IDBConstants.CREATED_BY, user);
						              	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
						              	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
						  			}
						  		}
						    boolean itemInserted = loctypeutil.insertLocTypeMst(ht);
						
							
							
							boolean inserted = mdao.insertIntoMovHis(htm);
							
							if (inserted) {
								request.getSession().setAttribute("locationTypeOneData", ht);
								resultJson.put("STATUS", "100");
								resultJson.put("LOC_TYPE_ID1", loctypeid);
								resultJson.put("MESSAGE", " Location Type One Added Successfully");

							} else {
								resultJson.put("STATUS", "99");
								resultJson.put("MESSAGE", "Error in Adding Location Type One");		
							}
						     }
							
								     						
							else
						{
							resultJson.put("STATUS", "99");
							resultJson.put("MESSAGE", " Location Type One Exists already");
						}
						

						} catch (Exception e) {
							this.mLogger.exception(this.printLog, "", e);
							resultJson.put("STATUS", "99");
							resultJson.put("MESSAGE", e.getMessage());		
							throw e;
						}
					return resultJson;
				}
						
							
						    
				//RESVI STARTS location type 2
				
				private JSONObject createlocationtype2(HttpServletRequest request) 
						throws IOException, ServletException,Exception {
						JSONObject resultJson = new JSONObject();
						String msg = "";
						
						ArrayList alResult = new ArrayList();
						Map map = null;
						MasterDAO _MasterDAO = new MasterDAO();
						LocTypeUtil loctypeutil = new LocTypeUtil();
						LoctaionTypeTwoService loctaionTypeTwoService = new LoctaionTypeTwoServiceImpl(); 
						Hashtable ht= new Hashtable();
						boolean isOrderExists = false;
						
						
						TblControlDAO _TblControlDAO =new TblControlDAO();
						
						try {
							HttpSession session = request.getSession();
							String sPlant = (String) session.getAttribute("PLANT");
							
							String plant = (String) session.getAttribute("PLANT");
							String user = (String) session.getAttribute("LOGIN_USER");
							String locDesc = request.getParameter("LOC_DESC").trim();
							String loctypeid2 = request.getParameter("LOC_TYPE_ID_2").trim();
							
							LOC_TYPE_MST2 loc_TYPE_MST2 = new LOC_TYPE_MST2();
							loc_TYPE_MST2.setPLANT(plant);
							loc_TYPE_MST2.setLOC_TYPE_ID2(loctypeid2);
							loc_TYPE_MST2.setLOC_TYPE_DESC2(locDesc);
							
							loc_TYPE_MST2.setCRAT(new DateUtils().getDateTime());
							loc_TYPE_MST2.setCRBY(user);
							loc_TYPE_MST2.setIsActive("Y");
							Hashtable loc_TYP_MST2 = new Hashtable();
							loc_TYP_MST2.put("LOC_TYPE_ID2", loc_TYPE_MST2.getLOC_TYPE_ID2());
							loc_TYP_MST2.put("PLANT", loc_TYPE_MST2.getPLANT());
							
							isOrderExists  = new LocMstTwoDAO().isExisit(loc_TYP_MST2);
							
							if(!isOrderExists) {
								boolean insertFlag = loctaionTypeTwoService.addLOC_TYPE_MST2(loc_TYPE_MST2);
								if(insertFlag) {
									  MovHisDAO mdao = new MovHisDAO(plant);
									 Hashtable htm = new Hashtable();
							           htm.put("PLANT",plant);
							           htm.put("DIRTYPE",TransactionConstants.ADD_LOCTYPETWO);
							           htm.put("RECID","");
							           htm.put("ITEM",loctypeid2);
							           htm.put("REMARKS",locDesc);
							           htm.put("UPBY",user);   htm.put("CRBY",user);
							            htm.put("CRAT",DateUtils.getDateTime());
							            htm.put("UPAT",DateUtils.getDateTime());
							            htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));      
							                  
							            htm.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
							            htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							            boolean updateFlag;
							      		if(loctypeid2!="LTW001")
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
							                  	htTblCntInsert.put(IDBConstants.CREATED_BY, user);
							                  	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
							                  	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
							      			}
							      		}
							                  boolean  inserted = mdao.insertIntoMovHis(htm);
							
							                  if (inserted) {
													request.getSession().setAttribute("locationTypeTwoData", ht);
													resultJson.put("STATUS", "100");
													resultJson.put("LOC_TYPE_ID2", loctypeid2);
													resultJson.put("MESSAGE", " Location Type Two Added Successfully");

												} else {
													resultJson.put("STATUS", "99");
													resultJson.put("MESSAGE", "Error in Adding Location Type Two");		
												}
											     }
							}
													     						
												else
											{
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", " Location Type Two Exists already");
											}
						                     	
							

											} catch (Exception e) {
												this.mLogger.exception(this.printLog, "", e);
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", e.getMessage());		
												throw e;
											}
						
											
										return resultJson;
									}

											
							
//						ENDS
				
				//imthi start for loctaion3
				
				private JSONObject createlocationtype3(HttpServletRequest request) 
						throws IOException, ServletException,Exception {
						JSONObject resultJson = new JSONObject();
						String msg = "";
						
						ArrayList alResult = new ArrayList();
						Map map = null;
						MasterDAO _MasterDAO = new MasterDAO();
						LocTypeUtil loctypeutil = new LocTypeUtil();
						LoctaionTypeThreeService loctaionTypeThreeService = new LoctaionTypeThreeServiceImpl(); 
						Hashtable ht= new Hashtable();
						boolean isOrderExists = false;
						
						
						TblControlDAO _TblControlDAO =new TblControlDAO();
						
						try {
							HttpSession session = request.getSession();
							String sPlant = (String) session.getAttribute("PLANT");
							
							String plant = (String) session.getAttribute("PLANT");
							String user = (String) session.getAttribute("LOGIN_USER");
							String locDesc = request.getParameter("LOC_DESC").trim();
							String loctypeid3 = request.getParameter("LOC_TYPE_ID_3").trim();
							
							LOC_TYPE_MST3 loc_TYPE_MST3 = new LOC_TYPE_MST3();
							loc_TYPE_MST3.setPLANT(plant);
							loc_TYPE_MST3.setLOC_TYPE_ID3(loctypeid3);
							loc_TYPE_MST3.setLOC_TYPE_DESC3(locDesc);
							
							loc_TYPE_MST3.setCRAT(new DateUtils().getDateTime());
							loc_TYPE_MST3.setCRBY(user);
							loc_TYPE_MST3.setIsActive("Y");
							Hashtable loc_TYP_MST3 = new Hashtable();
							loc_TYP_MST3.put("LOC_TYPE_ID3", loc_TYPE_MST3.getLOC_TYPE_ID3());
							loc_TYP_MST3.put("PLANT", loc_TYPE_MST3.getPLANT());
							
							isOrderExists  = new LocMstThreeDAO().isExisit(loc_TYP_MST3);
							
							if(!isOrderExists) {
								boolean insertFlag = loctaionTypeThreeService.addLOC_TYPE_MST3(loc_TYPE_MST3);
								if(insertFlag) {
									  MovHisDAO mdao = new MovHisDAO(plant);
									 Hashtable htm = new Hashtable();
							           htm.put("PLANT",plant);
							           htm.put("DIRTYPE",TransactionConstants.ADD_LOCTYPETHREE);
							           htm.put("RECID","");
							           htm.put("ITEM",loctypeid3);
							           htm.put("REMARKS",locDesc);
							           htm.put("UPBY",user);   htm.put("CRBY",user);
							            htm.put("CRAT",DateUtils.getDateTime());
							            htm.put("UPAT",DateUtils.getDateTime());
							            htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));      
							                  
							            htm.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
							            htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							            boolean updateFlag;
							      		if(loctypeid3!="LTH001")
							        		  {	
							      			boolean exitFlag = false;
							      			Hashtable htv = new Hashtable();				
							      			htv.put(IDBConstants.PLANT, plant);
							      			htv.put(IDBConstants.TBL_FUNCTION, "LOCTYPETHREE");
							      			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
							      			if (exitFlag) 
							        		    updateFlag=_TblControlDAO.updateSeqNo("LOCTYPETHREE",plant);
							      			else
							      			{
							      				Map htInsert=null;
							                  	Hashtable htTblCntInsert  = new Hashtable();           
							                  	htTblCntInsert.put(IDBConstants.PLANT,plant);          
							                  	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCTYPETHREE");
							                  	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"LTH");
							                   	htTblCntInsert.put("MINSEQ","0000");
							                   	htTblCntInsert.put("MAXSEQ","9999");
							                  	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
							                  	htTblCntInsert.put(IDBConstants.CREATED_BY, user);
							                  	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
							                  	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
							      			}
							      		}
							                  boolean  inserted = mdao.insertIntoMovHis(htm);
							
							                  if (inserted) {
													request.getSession().setAttribute("locationTypeThreeData", ht);
													resultJson.put("STATUS", "100");
													resultJson.put("LOC_TYPE_ID3", loctypeid3);
													resultJson.put("MESSAGE", " Location Type Three Added Successfully");

												} else {
													resultJson.put("STATUS", "99");
													resultJson.put("MESSAGE", "Error in Adding Location Type Three");		
												}
											     }
							}
													     						
												else
											{
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", " Location Type Three Exists already");
											}
						                     	
							

											} catch (Exception e) {
												this.mLogger.exception(this.printLog, "", e);
												resultJson.put("STATUS", "99");
												resultJson.put("MESSAGE", e.getMessage());		
												throw e;
											}
						
											
										return resultJson;
									}
				
				//imti ends
		
		private JSONObject getShipmentRefForBill(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("SHIPMENT_CODE"));
	               String PONO= StrUtils.fString(request.getParameter("ORDERNO"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	  		       String extCond="";
	  		       if(QUERY.length() > 0)
	  		    	 extCond = "SHIPMENT_CODE LIKE '"+QUERY+"%'";
	               ///////////////////////
	               ArrayList  movQryList = _MasterUtil.getShipmentRefForBill(PLANT, PONO, extCond);
	               if (movQryList.size() > 0) {
		               for(int i =0; i<movQryList.size(); i++) {
		            	   Map arrCustLine = (Map)movQryList.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
		                   resultJsonInt.put("SHIPMENTCODE", (String)arrCustLine.get("SHIPMENT_CODE"));
		                   jsonArray.add(resultJsonInt);
		               }
	               }else {
	                   //jsonArray.add("");
	               }
	               resultJson.put("shipments", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getExpenseDetailForBill(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        BillDAO bildao = new BillDAO();
	        String hdrid="0";
	     
	        try {
	               String PLANT = StrUtils.fString(request.getParameter("PLANT"));
	               //String EXPENSESHDRID = StrUtils.fString(request.getParameter("ID"));
	               
	               String pono = StrUtils.fString(request.getParameter("ORDERNO"));
	               String shipmentcode = StrUtils.fString(request.getParameter("SHIPMENT_CODE"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               Hashtable Shipment =new Hashtable(); 
	               Shipment.put("PLANT", PLANT);
	               Shipment.put("PONO", pono);
	               Shipment.put("SHIPMENT_CODE", shipmentcode);
	               mesflag = bildao.isShipmentcode(Shipment, "");
	              
	               //ArrayList  movQryList = _MasterUtil.getExpenseDetailForBill(PLANT, EXPENSESHDRID);
	               ArrayList  movQryList = _MasterUtil.getExpenseDetailusingponoanddnol(PLANT, pono, shipmentcode);
	               if (movQryList.size() > 0) {
		               for(int i =0; i<movQryList.size(); i++) {
		            	   Map arrCustLine = (Map)movQryList.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("EXPENSESACCOUNT", (String)arrCustLine.get("EXPENSES_ACCOUNT"));
		                   resultJsonInt.put("TAXTYPE", (String)arrCustLine.get("TAX_TYPE"));
		                   resultJsonInt.put("AMOUNT", (String)arrCustLine.get("AMOUNT"));
		                   resultJsonInt.put("TOTALAMOUNT", (String)arrCustLine.get("TOTAL_AMOUNT"));
		                   resultJsonInt.put("CURRENCYTOBASE", (String)arrCustLine.get("CURRENCYTOBASE"));
		                   resultJsonInt.put("BASETOORDERCURRENCY", (String)arrCustLine.get("BASETOORDERCURRENCY"));
		                   resultJsonInt.put("STATUS", (String)arrCustLine.get("STATUS"));
		                   resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
		                   if(mesflag) {
		                	   resultJsonInt.put("SCODESTATUS","YES");
		                   }else {
		                	   resultJsonInt.put("SCODESTATUS","NO");
		                   }
		                   jsonArray.add(resultJsonInt);
		                   hdrid = (String)arrCustLine.get("ID");
		               }
	               }else {
	                   jsonArray.add("");
	               }
	               resultJson.put("expenses", jsonArray);
	               jsonArray = new JSONArray();
	               
	              /* for(int j =0; j<movQryList.size(); j++) {
	            	   Map arrCustLinemap = (Map)movQryList.get(j);
	            	   hdrid = (String)arrCustLinemap.get("ID");
	               
		               ArrayList  shipmentTaxList = _MasterUtil.getExpenseTaxDetailForBill(PLANT, hdrid);
		               if (shipmentTaxList.size() > 0) {
			               for(int i =0; i<shipmentTaxList.size(); i++) {
			            	   Map arrCustLine = (Map)shipmentTaxList.get(i);
			                   JSONObject resultJsonInt = new JSONObject();
			                   resultJsonInt.put("TAXTYPE", (String)arrCustLine.get("TAX_TYPE"));
			                   resultJsonInt.put("TAXTOTAL", (String)arrCustLine.get("TAX_TOTAL"));
			                   jsonArray.add(resultJsonInt);
			               }
		               }else {
		                   jsonArray.add("");
		               }
		               
	               }*/
	              
	               for(int j =0; j<movQryList.size(); j++) {
	            	   int hdridfortax = 0;
	            	   Map arrCustLinemap = (Map)movQryList.get(j);
	            	   hdridfortax = Integer.valueOf((String)arrCustLinemap.get("ID"));
	            	   if(hdridfortax > 0) {
		            	   ArrayList  shipmentTaxList = _MasterUtil.getExpenseTaxDetailForBill(PLANT, String.valueOf(hdridfortax));
			               if (shipmentTaxList.size() > 0) {
				               for(int i =0; i<shipmentTaxList.size(); i++) {
				            	   Map arrCustLine = (Map)shipmentTaxList.get(i);
				                   JSONObject resultJsonInt = new JSONObject();
				                   resultJsonInt.put("TAXTYPE", (String)arrCustLine.get("TAX_TYPE"));
				                   resultJsonInt.put("TAXTOTAL", (String)arrCustLine.get("TAX_TOTAL"));
				                   jsonArray.add(resultJsonInt);
				               }
			               }else {
			                   jsonArray.add("");
			               }
		               }
	               }
	               
	               
	               
	               resultJson.put("expenseTax", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getNextCreditNoteSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		    String sCustCode="";
			try {
			 com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger); 
				sCustCode = _TblControlDAO.getNextOrder(plant,username,"SUPPLIER_CREDIT_NOTE");
				resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("CUSTCODE", sCustCode);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
			}
		private JSONObject getNextBillSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		    String sCustCode="";
			try {
			 com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger); 
				sCustCode = _TblControlDAO.getNextOrder(plant,username,"BILL");
				resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("CUSTCODE", sCustCode);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		private JSONObject getNextCustomerCreditNoteSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		    String sCustCode="";
			try {
			 com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger); 
				sCustCode = _TblControlDAO.getNextOrder(plant,username,"CUSTOMER_CREDIT_NOTE");
				resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
			      resultJson.put("ERROR_CODE", "100");
	              resultJson.put("CUSTCODE", sCustCode);
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		private JSONObject getTaxTreatmentData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               
	               ArrayList arrCust = _MasterUtil.getTaxTreatmentList("",PLANT,"");
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("text", (String)arrCustLine.get("TAXTREATMENT"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("TAXTREATMENTMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		private JSONObject getStateData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	        	   String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String COUNTRY= StrUtils.fString(request.getParameter("COUNTRY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               
	               ArrayList arrCust = _MasterUtil.getStateList("",PLANT,COUNTRY);
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   String result = (String)arrCustLine.get("STATE");
	                   result = result.replaceAll("\\s+$", "");

	                   resultJsonInt.put("text", result);
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("STATEMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		private JSONObject getStateDataPopup(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	        	   String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String COUNTRY= StrUtils.fString(request.getParameter("COUNTRY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               
	               ArrayList arrCust1 = _MasterUtil.getCountryList(COUNTRY,PLANT,"");
	               if (arrCust1.size() > 0) {
		               for(int i =0; i<arrCust1.size(); i++) {
		            	   Map arrCustLine1 = (Map)arrCust1.get(i);
		            	   COUNTRY = (String)arrCustLine1.get("COUNTRY_CODE");
		               }
	               }
	               
	               ArrayList arrCust = _MasterUtil.getStateList("",PLANT,COUNTRY);
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   String result = (String)arrCustLine.get("STATE");
	                   result = result.replaceAll("\\s+$", "");

	                   resultJsonInt.put("text", result);
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("STATEMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		private JSONObject getcooCurrency(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			
			try {
				String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
				String country= StrUtils.fString(request.getParameter("COUNTRY"));
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				
				ArrayList arrCust = _MasterUtil.getcooCountryCode(country);
				if (arrCust.size() > 0) {
					for(int i =0; i<arrCust.size(); i++) {
						Map arrCustLine = (Map)arrCust.get(i);
						String currency_code = (String)arrCustLine.get("CURRENCY_CODE");
						
						ArrayList arrCustcur = _MasterUtil.getCountryCurrency(PLANT,currency_code); 
						Map arrCustLine1 = (Map)arrCustcur.get(0);
						String currencyuseqt = (String)arrCustLine1.get("CURRENCYUSEQT");
						
						resultJson.put("CURRENCY", currencyuseqt);
					}
				}else {
					resultJson.put("CURRENCY", "0");
					JSONObject resultJsonInt = new JSONObject();
				}
			} catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("CURRENCY", "0");
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		
		private JSONObject getProductAssignedSupplier(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			CurrencyDAO currencyDAO = new CurrencyDAO();
			
			try {
				String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String currency=new PlantMstDAO().getBaseCurrency(plant);
				String item= StrUtils.fString(request.getParameter("ITEM"));
				request.getSession().setAttribute("RESULT","");
				boolean mesflag = false;
				
				ArrayList arrCust = _MasterUtil.getProductAssignedSupplier(plant,item);
				if (arrCust.size() > 0) {
					for(int i =0; i<arrCust.size(); i++) {
						Map arrCustLine = (Map)arrCust.get(i);
						String vendno = (String)arrCustLine.get("VENDNO");
		               CustUtil custUtils = new CustUtil();
		               ArrayList arrCusts = custUtils.getVendorListsWithName(vendno,plant," ORDER BY VNAME asc");
		                   Map arrCustLines = (Map)arrCusts.get(0);
//		                   resultJson.put("VENDO", (String)arrCustLinea.get("VENDNO"));
//		                   resultJson.put("VNAME", (String)arrCustLinea.get("VNAME"));
//		                   resultJson.put("NAME", (String)arrCustLinea.get("NAME"));
		                   
		                   
		                   resultJson.put("VENDO", (String)arrCustLines.get("VENDNO"));
		                   resultJson.put("VNAME", (String)arrCustLines.get("VNAME"));
		                   resultJson.put("NAME", (String)arrCustLines.get("NAME"));
		                   resultJson.put("TAXTREATMENT", (String)arrCustLines.get("TAXTREATMENT"));
		                   resultJson.put("TRANSPORTID", (String)arrCustLines.get("TRANSPORTID"));
		                   resultJson.put("BANKNAME", (String)arrCustLines.get("BANKNAME"));
		                   resultJson.put("TELNO", (String)arrCustLines.get("TELNO"));
		                   resultJson.put("EMAIL", (String)arrCustLines.get("EMAIL"));
		                   resultJson.put("HPNO", (String)arrCustLines.get("HPNO"));
		                   resultJson.put("ADDR1", (String)arrCustLines.get("ADDR1"));
		                   resultJson.put("ADDR2", (String)arrCustLines.get("ADDR2"));
		                   resultJson.put("ADDR3", (String)arrCustLines.get("ADDR3"));
		                   resultJson.put("ADDR4", (String)arrCustLines.get("ADDR4"));
		                   resultJson.put("REMARKS", (String)arrCustLines.get("REMARKS"));
		                   resultJson.put("STATE", (String)arrCustLines.get("STATE"));
		                   resultJson.put("COUNTRY", (String)arrCustLines.get("COUNTRY"));
		                   resultJson.put("ZIP", (String)arrCustLines.get("ZIP"));
		                   resultJson.put("PAYMENTTYPE", (String)arrCustLines.get("PAYMENTTYPE"));
		                   resultJson.put("PAY_TERMS", (String)arrCustLines.get("PAYMENT_TERMS"));
		                   resultJson.put("PAYMENT_TERMS", (String)arrCustLines.get("PAYMENTTERMS"));
		                   resultJson.put("SUPPLIERTYPE", (String)arrCustLines.get("SUPPLIER_TYPE_ID"));
		                   String CURRENCY = (String)arrCustLines.get("CURRENCY_ID");//Author: Azees  Create date: July 13,2021  Description: Show Supplier Based Currency
		                   if(CURRENCY.equalsIgnoreCase(""))
		                	   CURRENCY=currency;
		                   resultJson.put("CURRENCYID", CURRENCY);
		                   List curQryList=new ArrayList();
							curQryList = currencyDAO.getCurrencyDetails(CURRENCY,plant);
							for(int j =0; j<curQryList.size(); j++) {
								ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
								resultJson.put("CURRENCY",StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2))));
						        String Curramt =StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
						        //double CurramtValue ="".equals(Curramt) ? 0.0d : Double.parseDouble(Curramt);--azees no decimal convert 
						        //Curramt =StrUtils.addZeroes(CurramtValue, numberOfDecimal);
						   	    resultJson.put("CURRENCYUSEQT", Curramt);
						   			 
						        }
		                   
		                   
						jsonArray.add(resultJson);
					}
					resultJson.put("VENDMST", jsonArray);
				}else {
					JSONObject resultJsonInt = new JSONObject();
				}
			} catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJson.put("VENDMST", "");
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		private JSONObject getLocationData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String LOCATION= StrUtils.fString(request.getParameter("LOCATION"));	               
	               LocMstDAO  _LocMstDAO  = new LocMstDAO();
	               ArrayList listQry = _LocMstDAO.getLocByWMS(PLANT,"",LOCATION); 
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("LOC", (String)arrCustLine.get("loc"));
		                   resultJsonInt.put("LOCDESC", (String)arrCustLine.get("locdesc"));
		                   resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
		                   resultJsonInt.put("LOC_TYPE_ID2", (String)arrCustLine.get("LOC_TYPE_ID2"));
		                   jsonArray.add(resultJsonInt);
		               }
		               		resultJson.put("LOCMST", jsonArray);
			                JSONObject resultJsonInt = new JSONObject();
			                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			                resultJsonInt.put("ERROR_CODE", "100");
			                jsonArrayErr.add(resultJsonInt);
			                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("LOCMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getLocationTypeData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String location_type= StrUtils.fString(request.getParameter("LOCATION_TYPE"));	               
	               LocTypeUtil loctypeutil = new LocTypeUtil();
	               ArrayList listQry = loctypeutil.getLocTypeList(location_type,plant,"");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("LOC_TYPE_ID", (String)arrCustLine.get("LOC_TYPE_ID"));
		                   resultJsonInt.put("LOC_TYPE_DESC", (String)arrCustLine.get("LOC_TYPE_DESC"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("LOCTYPEMST", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("LOCTYPEMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getCurrencyData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               String CURRENCY = StrUtils.fString(request.getParameter("CURRENCY"));	
	               CurrencyDAO currencyDAO = new CurrencyDAO();
	               PlantMstDAO plantMstDAO = new PlantMstDAO();
	               String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	               ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, plant, "");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("CURRENCY", (String)arrCustLine.get("DISPLAY"));
		                   resultJsonInt.put("CURRENCYID", (String)arrCustLine.get("CURRENCYID"));
		                   
		                 String  Curramt = (String)arrCustLine.get("CURRENCYUSEQT");
		                 //double CurramtValue ="".equals(Curramt) ? 0.0d :  Double.parseDouble(Curramt);--azees no decimal convert        					
		                 //Curramt = StrUtils.addZeroes(CurramtValue, numberOfDecimal);
                         
		                   resultJsonInt.put("CURRENCYUSEQT", Curramt);
		                   jsonArray.add(resultJsonInt);
		               }
		               	resultJson.put("CURRENCYMST", jsonArray);   
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("CURRENCYMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getUomData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               String UOM = StrUtils.fString(request.getParameter("UOM"));	
	               UomDAO uomDAO = new UomDAO();
	               ArrayList listQry = uomDAO.getUomDetails(UOM, plant, "");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("UOM", (String)arrCustLine.get("uom"));
		                   resultJsonInt.put("UOMDESC", (String)arrCustLine.get("uomdesc"));
		                   jsonArray.add(resultJsonInt);
		               }
		               	resultJson.put("UOMMST", jsonArray);      
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("UOMMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getFooterData(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               MasterDAO masterDAO = new MasterDAO();
	               ArrayList listQry = masterDAO.getFooterDetails(plant, " FOOTER like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("FOOTER", (String)arrCustLine.get("FOOTER"));
		                   jsonArray.add(resultJsonInt);
		               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("FOOTERMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getBankData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	        	   String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               String NAME = StrUtils.fString(request.getParameter("NAME"));	
	               MasterDAO masterDAO = new MasterDAO();
	               ArrayList listQry = masterDAO.getBankList(NAME, plant);
	               if (listQry.size() > 0) {
		               
		                   Map arrCustLine = (Map)listQry.get(0);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("BRANCH", (String)arrCustLine.get("BRANCH_NAME"));
		                   jsonArray.add(resultJsonInt);
		               
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("BANKMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getBankData_All(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	        	   String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               String NAME = StrUtils.fString(request.getParameter("NAME"));	
	               MasterDAO masterDAO = new MasterDAO();
	               ArrayList listQry = masterDAO.getBankList(NAME, plant);
	               if (listQry.size() > 0) {
	            	   for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("text", (String)arrCustLine.get("NAME"));
		                   jsonArray.add(resultJsonInt);
	            	   }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("BANKMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getCountryData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               String NAME = StrUtils.fString(request.getParameter("NAME"));
	               String region="";
	               if(QUERY.equalsIgnoreCase("CREATEPLANT"))
	            	   region = StrUtils.fString(request.getParameter("REGION"));
	               else
	            	   region =StrUtils.fString((String) request.getSession().getAttribute("REGION"));
	               MasterDAO masterDAO = new MasterDAO();
	               ArrayList listQry = masterDAO.getCountryList(NAME, plant, region);
	               if (listQry.size() > 0) {		               
	            	   for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("COUNTRY_CODE", (String)arrCustLine.get("COUNTRY_CODE"));
		                   resultJsonInt.put("COUNTRYNAME", (String)arrCustLine.get("COUNTRYNAME"));
		                   jsonArray.add(resultJsonInt);
	            	   }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("COUNTRYMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getBatchData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String BATCH= StrUtils.fString(request.getParameter("QUERY"));	
	               String ITEMNO = StrUtils.fString(request.getParameter("ITEMNO"));
	               String LOC = StrUtils.fString(request.getParameter("LOC"));
	               String UOM = StrUtils.fString(request.getParameter("UOM"));
	               
	               InvMstDAO  _InvMstDAO  = new InvMstDAO();
	               ArrayList listQry = _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOM(plant,ITEMNO,LOC,BATCH,UOM);
	               if (listQry.size() > 0) {	               
	            	   for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("BATCH", (String)arrCustLine.get("batch"));
		                   resultJsonInt.put("PCSUOM", (String)arrCustLine.get("pcsuom"));
		                   resultJsonInt.put("PCSQTY", StrUtils.formatNum((String)arrCustLine.get("pcsqty")));
		                   resultJsonInt.put("UOM", UOM);
		                   resultJsonInt.put("QTY", (String)arrCustLine.get("qty"));
		                   resultJsonInt.put("CRAT", (String)arrCustLine.get("crat"));
		                   resultJsonInt.put("EXPIRYDATE", (String)arrCustLine.get("expirydate"));
		                   jsonArray.add(resultJsonInt);
	            	   }
	            	   resultJson.put("batches", jsonArray);
	              }else {
	            	    JSONObject resultJsonInt = new JSONObject();
		   	            resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
		   	            resultJsonInt.put("ERROR_CODE", "98");
		   	            jsonArrayErr.add(resultJsonInt);
		   	            resultJson.put("ERROR", jsonArrayErr);
	              }   
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getBatchDataStockTake(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();	     
			try {
				String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
				String BATCH= StrUtils.fString(request.getParameter("QUERY"));	
				String ITEMNO = StrUtils.fString(request.getParameter("ITEMNO"));
				String LOC = StrUtils.fString(request.getParameter("LOC"));
				String UOM = StrUtils.fString(request.getParameter("UOM"));
				
				InvMstDAO  _InvMstDAO  = new InvMstDAO();
				ArrayList listQry = _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOM(plant,ITEMNO,LOC,BATCH,UOM);
				if (listQry.size() > 0) {	               
					for(int i =0; i<listQry.size(); i++) {
						Map arrCustLine = (Map)listQry.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("BATCH", (String)arrCustLine.get("batch"));
						resultJsonInt.put("PCSUOM", (String)arrCustLine.get("pcsuom"));
						resultJsonInt.put("PCSQTY", StrUtils.formatNum((String)arrCustLine.get("pcsqty")));
						resultJsonInt.put("UOM", UOM);
						resultJsonInt.put("QTY", (String)arrCustLine.get("qty"));
						resultJsonInt.put("CRAT", (String)arrCustLine.get("crat"));
						resultJsonInt.put("EXPIRYDATE", (String)arrCustLine.get("expirydate"));
						jsonArray.add(resultJsonInt);
					}
					resultJson.put("batches", jsonArray);
				}else {
					
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BATCH", "NOBATCH");
					resultJsonInt.put("PCSUOM", "PCS");
					resultJsonInt.put("PCSQTY", "0");
					resultJsonInt.put("UOM", UOM);
					resultJsonInt.put("QTY", "0");
					resultJsonInt.put("CRAT", "");
					resultJsonInt.put("EXPIRYDATE", "");
					jsonArray.add(resultJsonInt);
					
					resultJson.put("batches", jsonArray);
					/*JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);*/
				}   
			}catch (Exception e) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			return resultJson;
		}
		/* Author: Azees  Create date: July 3,2021  Description: Stock Validation */
		private JSONObject getLocBatchStock(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String BATCH= StrUtils.fString(request.getParameter("QUERY"));	
	               String ITEMNO = StrUtils.fString(request.getParameter("ITEMNO"));
	               String LOC = StrUtils.fString(request.getParameter("LOC"));
	               String UOM = StrUtils.fString(request.getParameter("UOM"));
	               String QTY = StrUtils.fString(request.getParameter("QTY"));
	               
	               InvMstDAO  _InvMstDAO  = new InvMstDAO();
	               ArrayList listQry = _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOM(plant,ITEMNO,LOC,BATCH,UOM);
	               if (listQry.size() > 0) {	               
	            	   for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   float stqty = Float.valueOf((String)arrCustLine.get("qty"));
		                   String SQTY  = StrUtils.addZeroes(stqty, "3");
							if(Float.valueOf(SQTY) >= Float.valueOf(QTY))
							{
								resultJsonInt.put("ERROR_CODE", "100");
							}		                   
							else {
								resultJsonInt.put("ERROR_CODE", "98");
							}		                   
							jsonArray.add(resultJsonInt);
	            	   }
	            	   resultJson.put("batches", jsonArray);
	              }else {
	            	    JSONObject resultJsonInt = new JSONObject();
		   	            resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
		   	            resultJsonInt.put("ERROR_CODE", "98");
		   	            jsonArrayErr.add(resultJsonInt);
		   	            resultJson.put("batches", jsonArrayErr);
	              }   
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("batches", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getKittingParentBatch(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        BomDAO  bomDAO  = new  BomDAO();
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String PARENTBATCH= StrUtils.fString(request.getParameter("QUERY"));	
	               String ITEM = StrUtils.fString(request.getParameter("ITEM"));
	               
	               InvMstDAO  _InvMstDAO  = new InvMstDAO();
	               List listQry = bomDAO.getKittingParentBatchByWMS(plant,ITEM,"","",PARENTBATCH);
	               if (listQry.size() > 0) {	               
	            	   for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("BATCH", (String)arrCustLine.get("Batch"));		                   
		                   resultJsonInt.put("QTY", (String)arrCustLine.get("Qty"));		                   
		                   jsonArray.add(resultJsonInt);
	            	   }
	              }
            	   resultJson.put("batches", jsonArray);   
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getKittingChildDetailsBatch(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        BomDAO  bomDAO  = new  BomDAO();
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String CHILDBATCH= StrUtils.fString(request.getParameter("QUERY"));	
	               String ITEM = StrUtils.fString(request.getParameter("ITEM"));
	               
	               InvMstDAO  _InvMstDAO  = new InvMstDAO();
	               List listQry = bomDAO.getKittingChildDetailsBatchByWMS(plant,ITEM,"","",CHILDBATCH);
	               if (listQry.size() > 0) {	               
	            	   for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("BATCH", (String)arrCustLine.get("Batch"));		                   
		                   resultJsonInt.put("QTY", (String)arrCustLine.get("Qty"));		                   
		                   jsonArray.add(resultJsonInt);
	            	   }
	              }
            	   resultJson.put("batches", jsonArray);   
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getKittingChildBatch(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        BomDAO  bomDAO  = new  BomDAO();
	        ProductionBomDAO productionBomDAO = new ProductionBomDAO();
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
	               String BATCH= StrUtils.fString(request.getParameter("QUERY"));
	               String KONO = StrUtils.fString(request.getParameter("KONO"));
	               String ITEM = StrUtils.fString(request.getParameter("ITEM"));
	               String FROM_LOC = StrUtils.fString(request.getParameter("LOC"));
	               String PITEM = StrUtils.fString(request.getParameter("PITEM"));
	               String PITEMQTY = StrUtils.fString(request.getParameter("PITEMQTY"));
	               
	               InvMstDAO  _InvMstDAO  = new InvMstDAO();
	               List listQry =  _InvMstDAO.getLocationTransferBatch(plant,ITEM,FROM_LOC,BATCH);
	               if (listQry.size() > 0) {	               
	            	   for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("BATCH", (String)arrCustLine.get("batch"));		                   
		                   resultJsonInt.put("QTY", (String)arrCustLine.get("qty"));
		                   Double qty = Double.valueOf((String)arrCustLine.get("qty"));
		                   String bomqty = productionBomDAO.getchildbomqty(plant, PITEM, ITEM, "KIT");
		                   String kitbomqty = bomDAO.getChildDeKittingItemqtybyKONO(plant, KONO, ITEM);
		                   if(kitbomqty == null) {
		                	   kitbomqty="0";
		                   }
		                   Double dbomqty = Double.valueOf(bomqty) * Double.valueOf(PITEMQTY);
		                   dbomqty = Double.valueOf(dbomqty) - Double.valueOf(kitbomqty);
		                   
		                   if(qty >= dbomqty) {
		                	   resultJsonInt.put("BOMQTY", dbomqty);
		                   }else {
		                	   resultJsonInt.put("BOMQTY", qty);
		                   }
		                   jsonArray.add(resultJsonInt);
	            	   }
	              }
            	   resultJson.put("batches", jsonArray);   
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getIncotermData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               String cond= "";
	               boolean mesflag = false;
	               if(QUERY.length() > 0) {
	            	   cond = " INCOTERMS like '"+QUERY+"%' ";
	               }
	               ArrayList  movQryList = new MasterUtil().getINCOTERMSList(PLANT,cond);
	               if (movQryList.size() > 0) {
	               for(int i =0; i<movQryList.size(); i++) {
	                   Map arrCustLine = (Map)movQryList.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("INCOTERM", (String)arrCustLine.get("INCOTERMS"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }
	               resultJson.put("INCOTERMS", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		private JSONObject getItemModelData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               String cond= "";
	               boolean mesflag = false;
	               if(QUERY.length() > 0) {
	            	   cond = " MODEL like '"+QUERY+"%' ";
	               }
	               ArrayList  movQryList = new MasterUtil().getItemMasterList(PLANT,cond);
	               if (movQryList.size() > 0) {
	               for(int i =0; i<movQryList.size(); i++) {
	                   Map arrCustLine = (Map)movQryList.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("MODEL", (String)arrCustLine.get("MODEL"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }
	               resultJson.put("MODELS", jsonArray);        
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		private JSONObject getPRD_CLS_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               PrdClassDAO PRD_CLSDAO = new PrdClassDAO();
	               ArrayList listQry = PRD_CLSDAO.getPrdClsDetails("", plant, " AND prd_cls_desc like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("PRD_CLS_ID", (String)arrCustLine.get("prd_cls_id"));
		                   resultJsonInt.put("PRD_CLS_DESC", (String)arrCustLine.get("prd_cls_desc"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("PRD_CLSMST", jsonArray);    
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            		JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr); 
//	            	   JSONObject resultJsonInt = new JSONObject();
//	                   jsonArray.add("");
//	                   resultJson.put("footermaster", jsonArray);
	               }
//	               resultJson.put("PRD_CLSMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		
		//Product Department
		private JSONObject getDEPT_DISPLAY_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               MasterDAO _MasterDAO = new MasterDAO();
	               ArrayList listQry = _MasterDAO.getDepDispDetails("", plant, " AND DEPT_DISPLAY_DESC like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("DEPT_DISPLAY_ID", (String)arrCustLine.get("DEPT_DISPLAY_ID"));
		                   resultJsonInt.put("DEPT_DISPLAY_DESC", (String)arrCustLine.get("DEPT_DISPLAY_DESC"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("DEPTMST", jsonArray);    
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            		JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr); 

	               }
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		//Ends
		
		private JSONObject getPRD_DEPT_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               PrdDeptDAO PRD_DEPTDAO = new PrdDeptDAO();
	               ArrayList listQry = PRD_DEPTDAO.getPrdDepDetails("", plant, " AND prd_dept_desc like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("PRD_DEPT_ID", (String)arrCustLine.get("prd_dept_id"));
		                   resultJsonInt.put("PRD_DEPT_DESC", (String)arrCustLine.get("prd_dept_desc"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("PRD_DEPTMST", jsonArray);    
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            		JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr); 

	               }
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getSUPPLIERTYPE_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               CustomerBeanDAO CustBeanDAO = new CustomerBeanDAO();
	               ArrayList listQry = CustBeanDAO.getSupplierTypeDetails("", plant, " AND SUPPLIER_TYPE_DESC like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("SUPPLIER_TYPE_ID", (String)arrCustLine.get("SUPPLIER_TYPE_ID"));
		                   resultJsonInt.put("SUPPLIER_TYPE_DESC", (String)arrCustLine.get("SUPPLIER_TYPE_DESC"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("SUPPLIER_TYPE_MST", jsonArray);    
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            		JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr); 

	               }
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getCUSTOMER_TYPE_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               CustomerBeanDAO CustBeanDAO = new CustomerBeanDAO();
	               ArrayList listQry = CustBeanDAO.getCustomerTypeDetails("", plant, " AND CUSTOMER_TYPE_DESC like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("CUSTOMER_TYPE_ID", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
		                   resultJsonInt.put("CUSTOMER_TYPE_DESC", (String)arrCustLine.get("CUSTOMER_TYPE_DESC"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("CUSTOMER_TYPE_MST", jsonArray);    
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            		JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr); 

	               }
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getPRD_TYPE_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               PrdTypeDAO prdTypeDAO = new PrdTypeDAO();
	               ArrayList listQry = prdTypeDAO.getPrdTypeDetails("", plant, " AND prd_type_desc like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("PRD_TYPE_ID", (String)arrCustLine.get("prd_type_id"));
		                   resultJsonInt.put("PRD_TYPE_DESC", (String)arrCustLine.get("prd_type_desc"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("PRD_TYPEMST", jsonArray); 
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);   
	               }
//	               resultJson.put("PRD_TYPEMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getPRD_BRAND_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               PrdBrandDAO prdBrandDAO = new PrdBrandDAO();
	               ArrayList listQry = prdBrandDAO.getPrdBrandDetails("", plant, " AND PRD_BRAND_DESC like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("PRD_BRAND_ID", (String)arrCustLine.get("PRD_BRAND_ID"));
		                   resultJsonInt.put("PRD_BRAND_DESC", (String)arrCustLine.get("PRD_BRAND_DESC"));
		                   jsonArray.add(resultJsonInt);
		               }
		               resultJson.put("PRD_BRANDMST", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	              		JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("PRD_BRANDMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getHSCODE_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               MasterDAO masterDAO = new MasterDAO();
	               ArrayList listQry = masterDAO.getHSCODEDetails(plant, " HSCODE like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("HSCODE", (String)arrCustLine.get("HSCODE"));
		                   jsonArray.add(resultJsonInt);
		               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("HSCODEMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}

		private JSONObject getCOO_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               MasterDAO masterDAO = new MasterDAO();
	               ArrayList listQry = masterDAO.getCOODetails(plant, " COO like '%"+QUERY+"%' ");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("COO", (String)arrCustLine.get("COO"));
		                   jsonArray.add(resultJsonInt);
		               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("COOMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getCUSTOMERTYPE_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	               ArrayList listQry = customerBeanDAO.getCustomerTypeDetails(QUERY,plant, "");
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("CUSTOMER_TYPE_ID", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
		                   resultJsonInt.put("CUSTOMER_TYPE_DESC", (String)arrCustLine.get("CUSTOMER_TYPE_DESC"));
		                   jsonArray.add(resultJsonInt);
		               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("CUSTYPEMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getORDERTYPE_Data(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               String type= StrUtils.fString(request.getParameter("Type"));	
	               CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	               ArrayList listQry = new OrderTypeUtil().getOrderTypeDetails(QUERY,type,plant);
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("ORDERTYPE", (String)arrCustLine.get("ORDERTYPE"));
		                   resultJsonInt.put("ORDERDESC", (String)arrCustLine.get("ORDERDESC"));
		                   resultJsonInt.put("TYPE", (String)arrCustLine.get("TYPE"));
		                   jsonArray.add(resultJsonInt);
		               }
		            resultJson.put("ORDERTYPES", jsonArray);   
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("ORDERTYPES", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getEmployeeDatapayroll(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               EmployeeUtil custUtils = new EmployeeUtil();
	               HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO();
	               ArrayList arrCust = custUtils.getEmployeeListStartsWithName(QUERY,PLANT);
	               if (arrCust.size() > 0) {
	            	   
	            	   JSONObject resultJsonInt1 = new JSONObject();
	                   resultJsonInt1.put("EMPNO", "");
	                   resultJsonInt1.put("FNAME", "ALL EMPLOYEES");
	                   resultJsonInt1.put("EMPTYPE", "");
	                   resultJsonInt1.put("ID", "0");
	                   resultJsonInt1.put("REPORTING_INCHARGE", "");
	                   jsonArray.add(resultJsonInt1);
	            	   
	               for(int i =0; i<arrCust.size(); i++) {
	            	   Map arrCustLine = (Map)arrCust.get(i);
	            	   String emptype="";
	            	   int emptypeid = Integer.valueOf((String)arrCustLine.get("EMPLOYEETYPEID"));
	            	   if(emptypeid > 0) {
	            		  HrEmpType hrEmpType = hrEmpTypeDAO.getEmployeetypeById(PLANT, emptypeid);
	            		  emptype = hrEmpType.getEMPLOYEETYPE();
	            	   }
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("EMPNO", (String)arrCustLine.get("EMPNO"));
	                   resultJsonInt.put("FNAME", (String)arrCustLine.get("FNAME"));
	                   resultJsonInt.put("EMPTYPE", emptype);
	                   resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
	                   resultJsonInt.put("REPORTING_INCHARGE", (String)arrCustLine.get("REPORTING_INCHARGE"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
//	            	   JSONObject resultJsonInt = new JSONObject();
//	                   jsonArray.add("");
//	                   resultJson.put("footermaster", jsonArray);
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
	               resultJson.put("EMPMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		private JSONObject getCustomerListData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        //JSONArray jsonArrayMes = new JSONArray();
	      //MasterDAO _MasterDAO = new MasterDAO();
	     
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               CustUtil custUtils = new CustUtil();
	               ArrayList arrCust = custUtils.getCustomerListStartsWithName(QUERY,PLANT," ORDER BY CNAME asc");
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("CUSTNO", (String)arrCustLine.get("CUSTNO"));
	                   resultJsonInt.put("CNAME", (String)arrCustLine.get("CNAME"));
	             /*      resultJsonInt.put("TAXTREATMENT", (String)arrCustLine.get("TAXTREATMENT"));
	                   resultJsonInt.put("BANKNAME", (String)arrCustLine.get("BANKNAME"));*/
	                   /*String sCustCode     = (String)arrCustLine.get("VENDNO");
	                   String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
	                   String isactive    = strUtils.removeQuotes((String)arrCustLine.get("ISACTIVE")); */
	                   jsonArray.add(resultJsonInt);
	               }
			            resultJson.put("CUSTMST", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("CUSTMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		private JSONObject getCustomerListTypeData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        //JSONArray jsonArrayMes = new JSONArray();
	      //MasterDAO _MasterDAO = new MasterDAO();
	     
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               CustUtil custUtils = new CustUtil();
	               ArrayList arrCust = custUtils.getCustomerTypeListStartsWithName(QUERY,PLANT," ORDER BY CUSTOMER_TYPE_DESC asc");
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("CUSTOMER_TYPE_ID", (String)arrCustLine.get("CUSTOMER_TYPE_ID"));
	                   resultJsonInt.put("CUSTOMER_TYPE_DESC", (String)arrCustLine.get("CUSTOMER_TYPE_DESC"));
	                   jsonArray.add(resultJsonInt);
	               }
	               	resultJson.put("CUST_TYPE_MST", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("CUST_TYPE_MST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getEmployeeListStartsWithName(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        //JSONArray jsonArrayMes = new JSONArray();
	      //MasterDAO _MasterDAO = new MasterDAO();
	     
	        try {
	               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               String TYPE= StrUtils.fString(request.getParameter("TYPE"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
	               CustUtil custUtils = new CustUtil();
	               ArrayList arrCust = custUtils.getEmployeeListStartsWithName(QUERY,PLANT," ORDER BY FNAME asc");
	               if(TYPE.equals("CASHIER")){
	                arrCust = custUtils.getEmployeeListNameForPOSCashier(QUERY,PLANT," ORDER BY FNAME asc");
	               }else if(TYPE.equals("SALES")){
	                arrCust = custUtils.getEmployeeListNameForPOSsales(QUERY,PLANT," ORDER BY FNAME asc");
	               }
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("EMPNO", (String)arrCustLine.get("EMPNO"));
	                   resultJsonInt.put("FNAME", (String)arrCustLine.get("FNAME"));
	                   jsonArray.add(resultJsonInt);
	               }
	               		resultJson.put("EMP_MST", jsonArray);
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("EMP_MST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		private JSONObject getDepartmentData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
/*	               MasterUtil MasterUtil=new  MasterUtil();
	    		   ArrayList cList =  MasterUtil.getDepartmentList("",plant);*/
	               MasterUtil MasterUtil = new MasterUtil();
	               ArrayList arrCust = MasterUtil.getDepartmentList(QUERY,PLANT," ORDER BY DEPARTMENT asc");
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("DEPARTMENT", (String)arrCustLine.get("DEPARTMENT"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("DEPARTMENT_MST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		private JSONObject getBankNameData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	     
	        try {
	               String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               ///////////////////////
/*	               MasterUtil MasterUtil=new  MasterUtil();
	    		   ArrayList cList =  MasterUtil.getDepartmentList("",plant);*/
	               MasterUtil MasterUtil = new MasterUtil();
	               ArrayList arrCust = MasterUtil.getBankNameList(QUERY,PLANT," ORDER BY NAME asc");
	               if (arrCust.size() > 0) {
	               for(int i =0; i<arrCust.size(); i++) {
	                   Map arrCustLine = (Map)arrCust.get(i);
	                   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("NAME", (String)arrCustLine.get("NAME"));
	                   resultJsonInt.put("BRANCH_NAME", (String)arrCustLine.get("BRANCH_NAME"));
	                   jsonArray.add(resultJsonInt);
	               }
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   jsonArray.add("");
	                   resultJson.put("footermaster", jsonArray);
	               }
	               resultJson.put("BANKMST", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
			}
		
		
		private JSONObject getShipRefSequence(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		    
			try {
				String bill = request.getParameter("BILL").trim();
				String pono = "";
				String shipmentCode = "SN00001";
				boolean isAdded = false;
				
				Hashtable ShipmentHdr =new Hashtable(); 
				ShipmentHdr.put("PLANT", plant);
				ShipmentHdr.put("PONO", pono);
				ShipmentHdr.put("BILL", bill);
				ShipmentHdr.put("SHIPMENT_CODE", shipmentCode);
				ShipmentHdr.put("STATUS", "Y");
				ShipmentHdr.put("CRAT",dateutils.getDateTime());
				ShipmentHdr.put("CRBY",username);
				ShipmentHdr.put("UPAT",dateutils.getDateTime());
				
				Hashtable shcheck = new Hashtable();
				shcheck.put("PLANT", plant);
				shcheck.put("BILL", bill);
				shcheck.put("SHIPMENT_CODE", shipmentCode);
				
				boolean ischeck = _MasterUtil.isExisitShipment(shcheck, plant); 
				
				if(!ischeck) {
					isAdded = _MasterUtil.addShipment(ShipmentHdr, plant);
					if(isAdded) {
						resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
					    resultJson.put("ERROR_CODE", "100");
			            resultJson.put("SHIPREF", "SN00001");
					}else {
						 resultJson.put("ERROR_MESSAGE",  "Shipping Code Not Created");
					     resultJson.put("ERROR_CODE", "98");
					}	
				}else {
					resultJson.put("ERROR_MESSAGE", "NO ERRORS!");
				    resultJson.put("ERROR_CODE", "100");
		            resultJson.put("SHIPREF", "SN00001");
				}
				
				
			} catch (Exception e) {
		        resultJson.put("ERROR_MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			return resultJson;
		}
		
		private JSONObject getExpenseDetailForInventoryBill(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        BillDAO bildao = new BillDAO();
	        String hdrid="0";
	     
	        try {
	               String PLANT = StrUtils.fString(request.getParameter("PLANT"));
	               //String EXPENSESHDRID = StrUtils.fString(request.getParameter("ID"));
	               
	               String bill = StrUtils.fString(request.getParameter("BILL"));
	               String shipmentcode = StrUtils.fString(request.getParameter("SHIPMENT_CODE"));
	               request.getSession().setAttribute("RESULT","");
	               boolean mesflag = false;
	               Hashtable Shipment =new Hashtable(); 
	               Shipment.put("PLANT", PLANT);
	               Shipment.put("BILL", bill);
	               Shipment.put("SHIPMENT_CODE", shipmentcode);
	               mesflag = bildao.isShipmentcode(Shipment, "");
	              
	               //ArrayList  movQryList = _MasterUtil.getExpenseDetailForBill(PLANT, EXPENSESHDRID);
	               ArrayList  movQryList = _MasterUtil.getExpenseDetailusingbillanddnol(PLANT, bill, shipmentcode);
	               if (movQryList.size() > 0) {
		               for(int i =0; i<movQryList.size(); i++) {
		            	   Map arrCustLine = (Map)movQryList.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("EXPENSESACCOUNT", (String)arrCustLine.get("EXPENSES_ACCOUNT"));
		                   resultJsonInt.put("TAXTYPE", (String)arrCustLine.get("TAX_TYPE"));
		                   resultJsonInt.put("AMOUNT", (String)arrCustLine.get("AMOUNT"));
		                   resultJsonInt.put("TOTALAMOUNT", (String)arrCustLine.get("TOTAL_AMOUNT"));
		                   resultJsonInt.put("CURRENCYTOBASE", (String)arrCustLine.get("CURRENCYTOBASE"));
		                   resultJsonInt.put("BASETOORDERCURRENCY", (String)arrCustLine.get("BASETOORDERCURRENCY"));
		                   resultJsonInt.put("STATUS", (String)arrCustLine.get("STATUS"));
		                   resultJsonInt.put("ID", (String)arrCustLine.get("ID"));
		                   if(mesflag) {
		                	   resultJsonInt.put("SCODESTATUS","YES");
		                   }else {
		                	   resultJsonInt.put("SCODESTATUS","NO");
		                   }
		                   jsonArray.add(resultJsonInt);
		                   hdrid = (String)arrCustLine.get("ID");
		               }
	               }/*else {
	                   jsonArray.add("");
	               }*/
	               resultJson.put("expenses", jsonArray);
	               jsonArray = new JSONArray();
	               
	              /* for(int j =0; j<movQryList.size(); j++) {
	            	   Map arrCustLinemap = (Map)movQryList.get(j);
	            	   hdrid = (String)arrCustLinemap.get("ID");
	               
		               ArrayList  shipmentTaxList = _MasterUtil.getExpenseTaxDetailForBill(PLANT, hdrid);
		               if (shipmentTaxList.size() > 0) {
			               for(int i =0; i<shipmentTaxList.size(); i++) {
			            	   Map arrCustLine = (Map)shipmentTaxList.get(i);
			                   JSONObject resultJsonInt = new JSONObject();
			                   resultJsonInt.put("TAXTYPE", (String)arrCustLine.get("TAX_TYPE"));
			                   resultJsonInt.put("TAXTOTAL", (String)arrCustLine.get("TAX_TOTAL"));
			                   jsonArray.add(resultJsonInt);
			               }
		               }else {
		                   jsonArray.add("");
		               }
		               
	               }*/
	              
	               for(int j =0; j<movQryList.size(); j++) {
	            	   int hdridfortax = 0;
	            	   Map arrCustLinemap = (Map)movQryList.get(j);
	            	   hdridfortax = Integer.valueOf((String)arrCustLinemap.get("ID"));
	            	   if(hdridfortax > 0) {
		            	   ArrayList  shipmentTaxList = _MasterUtil.getExpenseTaxDetailForBill(PLANT, String.valueOf(hdridfortax));
			               if (shipmentTaxList.size() > 0) {
				               for(int i =0; i<shipmentTaxList.size(); i++) {
				            	   Map arrCustLine = (Map)shipmentTaxList.get(i);
				                   JSONObject resultJsonInt = new JSONObject();
				                   resultJsonInt.put("TAXTYPE", (String)arrCustLine.get("TAX_TYPE"));
				                   resultJsonInt.put("TAXTOTAL", (String)arrCustLine.get("TAX_TOTAL"));
				                   jsonArray.add(resultJsonInt);
				               }
			               }else {
			                   jsonArray.add("");
			               }
		               }
	               }
	               
	               
	               
	               resultJson.put("expenseTax", jsonArray);
	               ///////////////////////              
	        } catch (Exception e) {
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	                resultJsonInt.put("ERROR_CODE", "98");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		
		
		
		//getTaxTypeDataPO
		private JSONObject getTaxTypeDataADJ(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	        PlantMstUtil plantmstutil = new PlantMstUtil();
	        try {
	        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
	        	String COUNTRYCODE = "";
	        	String GSTTYPE = StrUtils.fString(request.getParameter("GSTTYPE"));
	        	String TAXKEY = StrUtils.fString(request.getParameter("TAXKEY"));
	        	String STATE_PREFIX = "";
	        	String sGstPercentage = "";
	        	
	        	List viewlistQry = plantmstutil.getPlantMstDetails(plant);
	            for (int i = 0; i < viewlistQry.size(); i++) {
	                Map map = (Map) viewlistQry.get(i);
	                COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
	            }
	        	
	        	boolean state = false;
	        	if(STATE_PREFIX.length()>0) {
	         		STATE_PREFIX="("+STATE_PREFIX+")";
	         		state = true;
	        	}
	        	if(TAXKEY.equalsIgnoreCase("INBOUND")) {
	        		sGstPercentage = new selectBean().getGST("PURCHASE",plant);
	        		sGstPercentage = StrUtils.addZeroes(Float.parseFloat(sGstPercentage), "1");
	        	}
	        	if(TAXKEY.equalsIgnoreCase("OUTBOUND")) {
	        		sGstPercentage = new selectBean().getGST("SALES",plant);
	        		sGstPercentage = StrUtils.addZeroes(Float.parseFloat(sGstPercentage), "1");
	        	}
	        	
	        	if(state) {
	        		
	        		List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes(TAXKEY, COUNTRYCODE, GSTTYPE);
	        		
	        		if(taxtypes.size() > 0) {
		        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
		        			if(finCountryTaxType.getTAXBOX() != 13 && finCountryTaxType.getTAXBOX() != 0) {
			        			JSONObject resultJsonInt = new JSONObject();
			        			resultJsonInt.put("SGSTTYPES",finCountryTaxType.getTAXTYPE()+STATE_PREFIX);
			        			resultJsonInt.put("SGSTDESC",finCountryTaxType.getTAXDESC());
			        			if(finCountryTaxType.getISZERO() == 1) {
			        				resultJsonInt.put("SGSTPERCENTAGE", "0.0");
			        			}else {
			        				resultJsonInt.put("SGSTPERCENTAGE", sGstPercentage);
			        			}
			        			
			        			String display="";
					         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
					         	{
					         		display = finCountryTaxType.getTAXTYPE();
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
					         		display = finCountryTaxType.getTAXTYPE()+" ["+sGstPercentage+"%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
					         		display = finCountryTaxType.getTAXTYPE()+STATE_PREFIX;
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		display = finCountryTaxType.getTAXTYPE()+STATE_PREFIX+" [0.0%]";
					         	}
					         	else
					         	{
					         		display = finCountryTaxType.getTAXTYPE()+STATE_PREFIX+" ["+sGstPercentage+"%]";
					         	}
					         	
					         	resultJsonInt.put("DISPLAY", display);
					         	resultJsonInt.put("ISZERO", finCountryTaxType.getISZERO());
					         	resultJsonInt.put("ISSHOW", finCountryTaxType.getSHOWTAX());
					         	resultJsonInt.put("ID", finCountryTaxType.getID());
								jsonArray.add(resultJsonInt);
		        			}
						}				
		        		
		        		 resultJson.put("records", jsonArray);
			             JSONObject resultJsonInt = new JSONObject();
			             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			             resultJsonInt.put("ERROR_CODE", "100");
			             jsonArrayErr.add(resultJsonInt);
			             resultJson.put("errors", jsonArrayErr);
	        		}else {
	                    JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    jsonArray.add("");
	                    resultJson.put("items", jsonArray);
	                    resultJson.put("errors", jsonArrayErr);
	   		        }
	        	}else {
	        		
	        		List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes(TAXKEY, COUNTRYCODE, GSTTYPE);
	        		
	        		if(taxtypes.size() > 0) {
		        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
		        			JSONObject resultJsonInt = new JSONObject();
		        			resultJsonInt.put("SGSTTYPES",finCountryTaxType.getTAXTYPE()+STATE_PREFIX);
		        			resultJsonInt.put("SGSTDESC",finCountryTaxType.getTAXDESC());
		        			if(finCountryTaxType.getISZERO() == 1) {
		        				resultJsonInt.put("SGSTPERCENTAGE", "0.0");
		        			}else {
		        				resultJsonInt.put("SGSTPERCENTAGE", sGstPercentage);
		        			}
		        			
		        			String display="";
				         	if(finCountryTaxType.getSHOWPERCENTAGE() == 0 && finCountryTaxType.getISZERO() == 0)
				         	{
				         		display = finCountryTaxType.getTAXTYPE();
				         	}else if(finCountryTaxType.getSHOWPERCENTAGE() == 0 && finCountryTaxType.getISZERO() == 1) {
				         		display = finCountryTaxType.getTAXTYPE();
				         	}else if(finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
				         		display = finCountryTaxType.getTAXTYPE()+" ["+sGstPercentage+"%]";
				         	}else {
				         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
				         	}
		
				         	resultJsonInt.put("DISPLAY", display);
				         	resultJsonInt.put("ISZERO", finCountryTaxType.getISZERO());
				         	resultJsonInt.put("ISSHOW", finCountryTaxType.getSHOWTAX());
				         	resultJsonInt.put("ID", finCountryTaxType.getID());
							jsonArray.add(resultJsonInt);
						}				
		        		
		        		 resultJson.put("records", jsonArray);
			             JSONObject resultJsonInt = new JSONObject();
			             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			             resultJsonInt.put("ERROR_CODE", "100");
			             jsonArrayErr.add(resultJsonInt);
			             resultJson.put("errors", jsonArrayErr);
	        		}else {
	                    JSONObject resultJsonInt = new JSONObject();
	                    resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
	                    resultJsonInt.put("ERROR_CODE", "99");
	                    jsonArrayErr.add(resultJsonInt);
	                    jsonArray.add("");
	                    resultJson.put("items", jsonArray);
	                    resultJson.put("errors", jsonArrayErr);
	   		     }
	        		
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
		
		

		private JSONObject getPlantList(HttpServletRequest request) throws Exception{
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();			
			PlantMstDAO plantMstDAO =new PlantMstDAO();
			StrUtils strUtils = new StrUtils();
			Hashtable ht = new Hashtable();
			List invQryList = new ArrayList();
			try {
			String PLANT= StrUtils.fString(request.getParameter("PLANT"));

			
			boolean mesflag = false;
			
				invQryList = plantMstDAO.getPlantMstlistdropdownAll(PLANT);
			
			if (invQryList.size() > 0) {
			for(int i =0; i<invQryList.size(); i++) {
			Map arrCustLine = (Map)invQryList.get(i);
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("PLANT", (String)arrCustLine.get("PLANT"));
			resultJsonInt.put("PLNTDESC", (String)arrCustLine.get("PLNTDESC"));
			jsonArray.add(resultJsonInt);
			}
			resultJson.put("PLANTLIST", jsonArray);
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
            resultJsonInt.put("ERROR_CODE", "100");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("errors", jsonArrayErr);
			}else {
				JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);  
			}

			} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
			jsonArray.add("");
			resultJson.put("USERLEVEL", jsonArray);
			}
			return resultJson;
			}
		
		private JSONObject CreateCurrencyModal(HttpServletRequest request) 
				throws IOException, ServletException,Exception {
				JSONObject resultJson = new JSONObject();
				String msg = "";
				ArrayList alResult = new ArrayList();
				Map map = null;
				MasterDAO _MasterDAO = new MasterDAO();
				TblControlDAO _TblControlDAO = new TblControlDAO();
				CurrencyUtil currUtil = new CurrencyUtil();

				try {
					HttpSession session = request.getSession();
					String plant = (String) session.getAttribute("PLANT");
					String user = (String) session.getAttribute("LOGIN_USER");

					String currency_id = request.getParameter("CURRENCY_ID").trim();
					String desc = request.getParameter("DESC").trim();
					String display = request.getParameter("DISPLAY").trim();
					String basecurrency = request.getParameter("BASECURRENCY").trim();
					String equ_currency = request.getParameter("CURREQT").trim();
					String Remarks = request.getParameter("REMARK").trim();
					String sItemId="";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.CURRENCYID, currency_id);
					if (!currUtil.isExistCurrency(ht,""))
					{						
					ht.put("PLANT", plant);
					ht.put("CURRENCYID", currency_id);
					ht.put("DESCRIPTION", desc);
					ht.put("DISPLAY", display);
					ht.put("CURRENCYUSEQT", equ_currency);
					ht.put("REMARK", Remarks);
					ht.put("CRBY", user);
					ht.put("CRAT", dateutils.getDateTime());
					
					ht.put("ISACTIVE", "Y");
					

					MovHisDAO mdao = new MovHisDAO(plant);
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", plant);
					htm.put("DIRTYPE", TransactionConstants.ADD_CURNCY);
					htm.put("RECID", "");
					htm.put("ITEM",currency_id);
					htm.put("REMARKS", desc);
					htm.put("CRBY", user);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPBY", user);
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
					htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
		            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				
					boolean flag = currUtil.insertCurrency(ht);
					
					
					boolean inserted = mdao.insertIntoMovHis(htm);
					if (flag && inserted) {
						resultJson.put("STATUS", "100");
						resultJson.put("CURRENCYID", currency_id);
						resultJson.put("DESCRIPTION", desc);
						resultJson.put("DISPLAY", display);
						resultJson.put("CURRENCYUSEQT", equ_currency);
						resultJson.put("MESSAGE", "Currency Added Successfully");

					} else {
						resultJson.put("STATUS", "99");
						resultJson.put("MESSAGE", "Error in Adding Currency");		
					}
					}
				
				else
				{
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", "Currency already exists ");
				}
				

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					resultJson.put("STATUS", "99");
					resultJson.put("MESSAGE", e.getMessage());		
					throw e;
				}
			return resultJson;
		}
// end
		private JSONObject getCurrencyNameData(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();	     
	        try {
	               String plant= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	               String QUERY= StrUtils.fString(request.getParameter("QUERY"));	
	               String CURRENCY = StrUtils.fString(request.getParameter("CURRENCY"));	
	               CurrencyDAO currencyDAO = new CurrencyDAO();
	               PlantMstDAO plantMstDAO = new PlantMstDAO();
	               Hashtable ht = new Hashtable();
	               String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	          //     ArrayList listQry = currencyDAO.getcurrencydetails(CURRENCY, plant, "");
	               ArrayList listQry = new CountryNCurrencyDAO().getCurrencyList(ht);
	               if (listQry.size() > 0) {
		               for(int i =0; i<listQry.size(); i++) {
		                   Map arrCustLine = (Map)listQry.get(i);
		                   JSONObject resultJsonInt = new JSONObject();
		                   resultJsonInt.put("CURRENCY", (String)arrCustLine.get("COUNTRY_NAME")+"("+(String)arrCustLine.get("CURRENCY_CODE")+")");
		                   resultJsonInt.put("CURRENCYID", (String)arrCustLine.get("CURRENCY_CODE"));
		                   jsonArray.add(resultJsonInt);
		               }
		               	resultJson.put("CURRENCYMST", jsonArray);   
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                resultJsonInt.put("ERROR_CODE", "100");
		                jsonArrayErr.add(resultJsonInt);
		                resultJson.put("errors", jsonArrayErr);
	               }else {
	            	   JSONObject resultJsonInt = new JSONObject();
	                   resultJsonInt.put("ERROR_MESSAGE", "NO CURRENCY RECORD FOUND!");
	                   resultJsonInt.put("ERROR_CODE", "99");
	                   jsonArrayErr.add(resultJsonInt);
	                   resultJson.put("errors", jsonArrayErr);  
	               }
//	               resultJson.put("CURRENCYMST", jsonArray);           
	        }catch (Exception e) {
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
	        }
	        return resultJson;
		}
		

}
						
