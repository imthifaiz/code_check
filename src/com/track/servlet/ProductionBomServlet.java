package com.track.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import org.omg.CosTransactions._TransactionalObjectStub;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ProcessingReceiveDAO;
import com.track.db.util.EmployeeUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ProductionBomUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;
import com.track.util.XMLUtils;

public class ProductionBomServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ProductionBomServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ProductionBomServlet_PRINTPLANTMASTERINFO;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	
	
	String action = "";
	String xmlStr = "";
	String completemsg="";
	ProductionBomUtil _ProductionBomUtil=null;
	ProductionBomDAO _ProductionBomDAO = null;
	//OperationSeqUtil operationsequtil = null;
	MovHisDAO mdao = null;
	DateUtils dateutils = new DateUtils();
	BomDAO bomdao = new BomDAO();
	StrUtils strUtils = new StrUtils();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		_ProductionBomUtil=new ProductionBomUtil();
		_ProductionBomDAO=new ProductionBomDAO();
		//operationsequtil = new OperationSeqUtil();
		mdao =new MovHisDAO();
	}

	//@SuppressWarnings("static-access")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject jsonObjectResult = new JSONObject();
		
				String chk23 = "";
		try{
			action = request.getParameter("action").trim();
			}
		catch(Exception ex){
			chk23="ok";
		}
		
		try {

			if(chk23=="ok")
				action = StrUtils.fString(request.getParameter("ACTION")).trim();
			else
				action = request.getParameter("action").trim();

			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			//_locUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			
			else if(action.equals("VIEW_PRODBOM_DETAILS")){
				jsonObjectResult = this.getProductionBOMDetails(request);	 
					// jsonObjectResult = this.getDODETDetails(request);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }

			else if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION")) {
				
				jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResult.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			}
			else if(action.equals("ADD_PRODBOM")){
				String msg   = addProductionBOM(request, response);
				jsonObjectResult = this.getProductionBOMDetails(request);	 
					// jsonObjectResult = this.getDODETDetails(request);
					 jsonObjectResult.put("errorMsg", msg);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("DELETE_PRODBOM")){
				xmlStr = "";
				xmlStr = DeleteProdutionBOM(request, response);
				
				
			 }
			else if(action.equals("VIEW_PRODBOM_SUMMARY")){
				jsonObjectResult = this.getProductionBOMSummaryDetails(request);	 
					// jsonObjectResult = this.getDODETDetails(request);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if (action.equals("VALIDATE_PARENT_PRODUCT")) {
				String item = StrUtils.fString(request.getParameter("ITEM")).trim();
				jsonObjectResult = this.validateParentProductBOM(plant, item);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();

			}
			
			else if (action.equals("VALIDATE_CHILD_PRODUCT")) {
				String pitem = StrUtils.fString(request.getParameter("PITEM")).trim();
				String citem = StrUtils.fString(request.getParameter("CITEM")).trim();
				jsonObjectResult = this.validateChildProductBOM(plant,pitem, citem);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();

			}
			else if (action.equals("VALIDATE_CHILDEQUI_PRODUCT")) {
				String pitem = StrUtils.fString(request.getParameter("PITEM")).trim();
				String citem = StrUtils.fString(request.getParameter("CITEM")).trim();
				jsonObjectResult = this.validateChildEquivalentProduct(plant,pitem, citem);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();

			}
			else if(action.equals("ADD_KITBOM")){
				String msg   = addkitBOM(request, response);
				jsonObjectResult = this.getKitBOMDetails(request);	 
					// jsonObjectResult = this.getDODETDetails(request);
					 jsonObjectResult.put("errorMsg", msg);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("ADD_COPY_KITBOM")){
				/*String msg   = addCopykitBOM(request, response);
				jsonObjectResult = this.getKitBOMDetails(request);	 
				jsonObjectResult.put("errorMsg", msg);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();*/
				

				String msg="";
				UserTransaction ut = null;
				List lnno = new ArrayList(), item = new ArrayList(), uom = new ArrayList(),
						qty = new ArrayList();
				String PITEM="",PARENTQTY="",PUOM="",PITEM_DESC="",EITEM="";
				String OGITEM="",OGPARENTUOM="",OGPITEM_DESC="";
				int itemCount  = 0, qtyCount  = 0, lnnoCount  = 0, uomCount  = 0;
				boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
				try {
					//msg = addProcessing(request, response);
					HttpSession session = request.getSession();
					InvMstDAO invMstDAO = new InvMstDAO();
					ItemMstDAO itemMstDAO = new ItemMstDAO();
					MovHisDAO movHisDao = new MovHisDAO();
					ProcessingReceiveDAO processingReceiveDAO = new ProcessingReceiveDAO();
					String PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
							.trim();
					String UserId = (String) session.getAttribute("LOGIN_USER");
					String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(PLANT);
					boolean isMultipart = ServletFileUpload.isMultipartContent(request);
					if(isMultipart) {
						FileItemFactory factory = new DiskFileItemFactory();
						ServletFileUpload upload = new ServletFileUpload(factory);				
						List items = upload.parseRequest(request);
						Iterator iterator = items.iterator();
						ut = DbBean.getUserTranaction();
						ut.begin();
						while (iterator.hasNext()) {
							FileItem fileItem = (FileItem) iterator.next();
							System.out.println(fileItem.getFieldName().toString());
							
							/* Hdr*/
							if (fileItem.isFormField()) {
								if (fileItem.getFieldName().equalsIgnoreCase("PITEM")) {
									PITEM = StrUtils.fString(fileItem.getString()).trim();
								}
								if (fileItem.getFieldName().equalsIgnoreCase("PITEM_DESC")) {
									PITEM_DESC = StrUtils.fString(fileItem.getString()).trim();
								}
								if (fileItem.getFieldName().equalsIgnoreCase("EITEM")) {
									EITEM = StrUtils.fString(fileItem.getString()).trim();
								}
																			
								if (fileItem.getFieldName().equalsIgnoreCase("QTY")) {
									PARENTQTY = StrUtils.fString(fileItem.getString()).trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("PARENTUOM")) {
									PUOM = StrUtils.fString(fileItem.getString()).trim();
								}
								if (fileItem.getFieldName().equalsIgnoreCase("OGITEM")) {
									OGITEM = StrUtils.fString(fileItem.getString()).trim();
								}
								if (fileItem.getFieldName().equalsIgnoreCase("OGPITEM_DESC")) {
									OGPITEM_DESC = StrUtils.fString(fileItem.getString()).trim();
								}
								if (fileItem.getFieldName().equalsIgnoreCase("OGPARENTUOM")) {
									OGPARENTUOM = StrUtils.fString(fileItem.getString()).trim();
								}
								
							}
							
							//DET
							if (fileItem.isFormField()) {
								if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
									lnno.add(lnnoCount, StrUtils.fString(fileItem.getString()).trim());
									lnnoCount++;
								}
							}
							if (fileItem.isFormField()) {
								if (fileItem.getFieldName().equalsIgnoreCase("citem")) {
									item.add(itemCount, StrUtils.fString(fileItem.getString()).trim());
									itemCount++;
								}
							}
							if (fileItem.isFormField()) {
								if (fileItem.getFieldName().equalsIgnoreCase("CUOM")) {
									
									uom.add(uomCount, StrUtils.fString(fileItem.getString()).trim());
									uomCount++;
								}
							}
							
							if (fileItem.isFormField()) {
								if (fileItem.getFieldName().equalsIgnoreCase("CQTY")) {
									qty.add(qtyCount, StrUtils.fString(fileItem.getString()).trim());
									qtyCount++;
								}
							}
							
						}
					}
					
					
			        	
					boolean insertflag =true;
						String CITEM  = "",QTY  = "",CUOM="";
						for (int i = 0; i < lnnoCount; i++) {
							
							int index = Integer.parseInt((String) lnno.get(i)) - 1;
							CITEM = StrUtils.fString((String) item.get(index)) ;
							QTY = StrUtils.fString((String) qty.get(index)) ;
							CUOM = StrUtils.fString((String) uom.get(index)) ;
							
							Hashtable htm = new Hashtable();
							htm.put("PLANT", PLANT);
							
							Hashtable htkitbominsert = new Hashtable();
							htkitbominsert.put(IDBConstants.PLANT,PLANT);
							htkitbominsert.put(IDBConstants.PARENTITEM,PITEM);
							htkitbominsert.put(IDBConstants.CHILDITEM,CITEM);
							htkitbominsert.put("BOMTYPE","KIT");
							boolean itemFound = _ProductionBomUtil.isExistsProdBom(htkitbominsert);
							if(!itemFound)
							{
							htkitbominsert.put("PARENTUOM",PUOM);
							htkitbominsert.put("CHILDUOM",CUOM);
							htkitbominsert.put(IDBConstants.QTY,QTY);						
							htkitbominsert.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
							htkitbominsert.put(IDBConstants.LOGIN_USER,UserId);
							
							Hashtable htequiinsert = new Hashtable();
							if(EITEM.length()>0)
							{
								htequiinsert.put(IDBConstants.PLANT,PLANT);
								htequiinsert.put(IDBConstants.ITEM,PITEM);
								htequiinsert.put("CHILDITEM",CITEM);
								htequiinsert.put("EQIITEM",EITEM);
							}
														
							htm.put("DIRTYPE", TransactionConstants.ADD_KITBOM);
							
							insertflag = _ProductionBomUtil.insertProdBomMst(htkitbominsert);
							if(EITEM.length()>0)
							{
								ProductionBomDAO ProdBomDao = new ProductionBomDAO();
								if(insertflag){
									boolean eqiflag = ProdBomDao.isExistsEquivalentitem(htequiinsert);
									if(!eqiflag){
										eqiflag = ProdBomDao.insertIntoEquivalentItem(htequiinsert);
									}
								}
							}
							
							} else {
								
								htm.put("DIRTYPE", TransactionConstants.UPDATE_KITBOM);
								String queryBom = "set "+IDBConstants.QTY+"="+QTY+", CHILDUOM = '"+CUOM+"'";
								insertflag = _ProductionBomDAO.updatePrdBOM(queryBom, htkitbominsert, "");
							}
							
							mdao.setmLogger(mLogger);
							htm.put("RECID", "");
							htm.put("ORDNUM",PITEM);
							htm.put("ITEM",CITEM);
							htm.put("REMARKS",EITEM);
							htm.put("UPBY", UserId);
							htm.put("CRBY", UserId);
							htm.put("CRAT", dateutils.getDateTime());
							htm.put("UPAT", dateutils.getDateTime());
							htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
							
							boolean inserted = mdao.insertIntoMovHis(htm);
							
					
				}
				if(insertflag){
					DbBean.CommitTran(ut);
					String message = PITEM+" Kit BOM Added Successfully";
					if (ajax) {
						jsonObjectResult.put("MESSAGE", message);
						jsonObjectResult.put("ERROR_CODE", "100");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
						response.sendRedirect("../billofmaterials/summary?RESULT=" + message);
					}
					
					}
					else{			
						DbBean.RollbackTran(ut);
						String message = "Error in Adding Kit BOM "+PITEM;
						if (ajax) {
							jsonObjectResult.put("MESSAGE", message);
							jsonObjectResult.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(jsonObjectResult.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../billofmaterials/copy?ITEM="+OGITEM+"&DESC="+OGPITEM_DESC+"&PUOM="+OGPARENTUOM+"&resulterror=" + message);
						}
					}
			        
			        
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DbBean.RollbackTran(ut);
					e.printStackTrace();
					if (ajax) {
						jsonObjectResult.put("MESSAGE", ThrowableUtil.getMessage(e));
						jsonObjectResult.put("ERROR_CODE", "98");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(jsonObjectResult.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
						response.sendRedirect("../billofmaterials/copy?ITEM="+PITEM+"&DESC="+PITEM_DESC+"&PUOM="+PUOM+"&resulterror=" + ThrowableUtil.getMessage(e));
					}
				}
			 
				
			}
			else if(action.equals("VIEW_KITBOM_DETAILS")){
				jsonObjectResult = this.getKitBOMDetails(request);	
				String PITEM = StrUtils.fString(request.getParameter("ITEM"));
				if(PITEM.length()>0)
				{	
					jsonObjectResult.put("completedMes", completemsg);
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }
			
			else if(action.equals("VIEW_KITBOM_DETAILS_FOR_KITTING")){
				jsonObjectResult = this.getKitBOMDetailsForKitting(request);	
				String PITEM = StrUtils.fString(request.getParameter("ITEM"));
				if(PITEM.length()>0)
				{	
					jsonObjectResult.put("completedMes", completemsg);
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }
			else if(action.equals("VIEW_KITBOM_DETAILS_FOR_DETAIL")){
				jsonObjectResult = this.getKitBOMDetailsFDET(request);	
				String PITEM = StrUtils.fString(request.getParameter("ITEM"));
				if(PITEM.length()>0)
				{	
					jsonObjectResult.put("completedMes", completemsg);
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }
			else if(action.equals("DELETE_KITBOM")){
				xmlStr = "";
				xmlStr = DeleteKitBOM(request, response);
				
				
			 }
			else if(action.equals("VIEW_KITBOM_SUMMARY_DETAILS")){
				jsonObjectResult = this.getKitBOMSummaryDetails(request);	 
				    response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("VIEW_PARENT_KITBOM_SUMMARY_DETAILS")){
				jsonObjectResult = this.getParentKitBOMSummaryDetails(request);	 
				    response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("VIEW_KIT_DEKIT_SUMMARY_DETAILS")){
				jsonObjectResult = this.getKitDeKitBOMSummaryDetails(request);	 
				    response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("ADD_KITTING")){
				String msg   = addkitting(request, response);
				jsonObjectResult = this.getKittingDetails(request);	 
					// jsonObjectResult = this.getDODETDetails(request);
					 jsonObjectResult.put("errorMsg", msg);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("VIEW_KITTING_DETAILS")){
				jsonObjectResult = this.getKittingDetails(request);	 
					// jsonObjectResult = this.getDODETDetails(request);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("DEKITTING")){
				xmlStr = "";
				xmlStr = DODekitting(request, response);
				
				
			 }
			else if(action.equals("ADD_KITTING_WITHBOM")){
				String msg   = addkitting(request, response);
				jsonObjectResult = this.getKitBOMDetails(request);	 
					jsonObjectResult.put("errorMsg", msg);
					jsonObjectResult.put("completedMes", completemsg);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if (action.equals("VALIDATE_KITCHECK_BATCH")) {
				String item = StrUtils.fString(request.getParameter("ITEM")).trim();
				String citem = StrUtils.fString(request.getParameter("CITEM")).trim();
				String batch = StrUtils.fString(request.getParameter("BATCH")).trim();
				jsonObjectResult = this.validateKitcheckBatch(plant, item,citem,batch);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();

			}
			else if (action.equals("VALIDATE_KITCHECK_PARENTITEM")) {
				String item = StrUtils.fString(request.getParameter("ITEM")).trim();
				jsonObjectResult = this.validateKitcheckparentitem(plant, item);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();

			}
			else if (action.equals("VALIDATE_KITCHECK_CHILDITEM")) {
				String item = StrUtils.fString(request.getParameter("ITEM")).trim();
				String citem = StrUtils.fString(request.getParameter("CITEM")).trim();
				jsonObjectResult = this.validateKitcheckchilditem(plant, item,citem);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();

			}
			else if(action.equals("ADD_KITCHECK")){
				String msg   = addkitcheck(request, response);
				jsonObjectResult = this.getKittingDetails(request);	 
					// jsonObjectResult = this.getDODETDetails(request);
					 jsonObjectResult.put("errorMsg", msg);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
				 
			 }
			else if(action.equals("DO_KITUNCHECK")){
				xmlStr = "";
				xmlStr = DOKitUncheck(request, response);
				
				 
			 }
			
			else if(action.equals("VIEW_KITBOM_DETAILS_BULK")){
				jsonObjectResult = this.getKitBOMDetailsForBulkKitting(request);	
				/*String PITEM = StrUtils.fString(request.getParameter("ITEM"));
				if(PITEM.length()>0)
				{	
					jsonObjectResult.put("completedMes", completemsg);
				}*/
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
				 
			 }
			else if(action.equals("BULK_KITTING_WITHBOM")){
				xmlStr = "";
				xmlStr = addkittingBulk(request, response);
				
				
			 }
			else if(action.equals("VIEW_LABEL_DETAILS")){
				jsonObjectResult = this.getlabelDetails(request);	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
		 
			 }
		 
			else if(action.equals("ADD_LABEL")){
				String msg   = addlabel(request, response);
				jsonObjectResult = this.getlabelDetails(request);	 
				jsonObjectResult.put("errorMsg", msg);
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


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	
	private String addProductionBOM(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String PLANT = "",PITEM = "",CITEM  = "",QTY="",OPSEQNUM="",REMARK1="",REMARK2="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			
			PITEM = StrUtils.fString(request.getParameter("ITEM"));
			CITEM = StrUtils.fString(request.getParameter("CITEM"));
			QTY = StrUtils.fString(request.getParameter("QTY"));
			OPSEQNUM = StrUtils.fString(request.getParameter("OPRSEQNUM"));
			REMARK1 = StrUtils.fString(request.getParameter("REMARK1"));
			REMARK2 = StrUtils.fString(request.getParameter("REMARK2"));
				
			boolean pitemFound = true;
			boolean citemFound = true;
			boolean seqFound = true;
			
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			try{
			itemMstUtil.isValidItemInItemmst(PLANT, PITEM);
			}
			catch (Exception e) {
				throw new Exception(" Scan/Enter a Valid Parent Product ID");
			}
			try{
			itemMstUtil.isValidItemInItemmst(PLANT, CITEM);
			}
			catch (Exception e) {
				throw new Exception(" Scan/Enter a Valid Child Product ID");
			}
			
			Hashtable htseqCondition = new Hashtable();
			htseqCondition.put(IDBConstants.OPRSEQNUM,OPSEQNUM);
			htseqCondition.put("PLANT", PLANT);
			
			
			/*seqFound = operationsequtil.isExistsOprSeq(htseqCondition);
			if (!seqFound) {
				throw new Exception(" Scan/Enter a Valid Operation Sequence Number");
			}*/
			
			
			Hashtable htprodbominsert = new Hashtable();
			htprodbominsert.put(IDBConstants.PLANT,PLANT);
			htprodbominsert.put(IDBConstants.PARENTITEM,PITEM);
			htprodbominsert.put(IDBConstants.CHILDITEM,CITEM);
			htprodbominsert.put(IDBConstants.QTY,QTY);
			htprodbominsert.put("SEQNUM",OPSEQNUM);
			htprodbominsert.put("BOMTYPE","PROD");
			htprodbominsert.put(IDBConstants.ITEMMST_REMARK1,REMARK1);
			htprodbominsert.put(IDBConstants.ITEMMST_REMARK2,REMARK2);
			htprodbominsert.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
			htprodbominsert.put(IDBConstants.LOGIN_USER,UserId);
			
			
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", PLANT);
			htm.put("DIRTYPE", TransactionConstants.ADD_PRODBOM);
			htm.put("RECID", "");
			htm.put("ORDNUM",PITEM);
			htm.put("ITEM",CITEM);
			htm.put("REMARKS",OPSEQNUM+","+REMARK1+","+REMARK2);
			htm.put("UPBY", UserId);
			htm.put("CRBY", UserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			
			
			
			
			boolean insertflag = _ProductionBomUtil.insertProdBomMst(htprodbominsert);
			
			boolean inserted = mdao.insertIntoMovHis(htm);
			
			
			if(insertflag&inserted){
			resultJson.put("status", "100");
			msg = "<font class = "+IDBConstants.SUCCESS_COLOR +">Production BOM Added Successfully</font>";
			}
			else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Production BOM</font>";
				
			}
			
						
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			
		}
		return msg;
	}
	
	private JSONObject getProductionBOMDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       
     
        try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
              // String CITEM = StrUtils.fString(request.getParameter("CITEM"));
                 
               
   			ArrayList  movQryList = _ProductionBomUtil.getProdBomList(PITEM,PLANT, " AND BOMTYPE='PROD'");
            
                 if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            
                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                            String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            String opseqnum = StrUtils.fString((String)lineArr.get("SEQNUM")) ;
                            String uom = StrUtils.fString((String)lineArr.get("CUOM")) ;
                            
                            chkString = parentitem+","+childitem;
                            
                            result += "<tr valign=\"middle\">"  
                            		+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td align = center>"  + id + "</td>"
                            		+ "<td align = center>"  + childitem + "</td>"
                            		+ "<td align = center>"  + desc + "</td>"
                            		+ "<td align = center>"  + qty + "</td>"
                            		+ "<td align = center>"  + opseqnum + "</td>"
                            		+ "<td align = center>"  + uom + "</td>"
                            		+ "</tr>" ;
                            	
                          resultJsonInt.put("PRODBOMDATA", result);
                         
                            jsonArray.add(resultJsonInt);

                }
                    resultJson.put("productionbom", jsonArray);
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
                    resultJson.put("productionbom", jsonArray);
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
	
	private String DeleteProdutionBOM(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String PITEM = "", CITEM = "",result="",fieldDescData="",rflag="";
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			rflag = StrUtils.fString(request.getParameter("RFLAG"));
			
			
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkditems  = request.getParameterValues("chkitem"); 
	   
	   process: 	
			if (chkditems != null)    {     
				for (int i = 0; i < chkditems.length; i++)       { 
					String item = chkditems[i];
					String itemArray[] = item.split(",");
					PITEM = itemArray[0];
					CITEM = itemArray[1];
							
					Hashtable htprodbomdelete = new Hashtable();
					htprodbomdelete.put(IDBConstants.PLANT,PLANT);
					htprodbomdelete.put(IDBConstants.PARENTITEM,PITEM);
					htprodbomdelete.put(IDBConstants.CHILDITEM,CITEM);
						
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_PRODBOM);
					htm.put("RECID", "");
					htm.put("ORDNUM",PITEM);
					htm.put("ITEM",CITEM);
					htm.put("REMARKS","");
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			transactionHandler = _ProductionBomUtil.deleteProdBom(htprodbomdelete);
    		boolean inserted = mdao.insertIntoMovHis(htm);
						
			if(!transactionHandler)
				break process;
			}
		}
 
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute("RESULT","Production BOM Deleted successfully for Parent Item:"+PITEM);
			if(rflag.equalsIgnoreCase("1")){
			response.sendRedirect("jsp/create_productionbom.jsp?action=result&ITEM="+PITEM);}
			else{response.sendRedirect("jsp/maint_productionbom.jsp?action=result&ITEM="+PITEM);}
		}
		else{
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("RESULTERROR","Error in Deleting Production BOM for Parent Item:"+PITEM);
			if(rflag.equalsIgnoreCase("1")){		
			response.sendRedirect("jsp/create_productionbom.jsp?action=resulterror&ITEM="+PITEM);}
			else{response.sendRedirect("jsp/maint_productionbom.jsp?action=resulterror&ITEM="+PITEM);}
			}
	

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			if(rflag.equalsIgnoreCase("1")){
			response.sendRedirect("jsp/create_productionbom.jsp?action=catchrerror&ITEM="+PITEM);}
			else{response.sendRedirect("jsp/maint_productionbom.jsp?action=catchrerror&ITEM="+PITEM);}
			throw e;
		}

		return msg;
	}
	
	private JSONObject getProductionBOMSummaryDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
               String CITEM = StrUtils.fString(request.getParameter("CITEM"));
               
               Hashtable ht = new Hashtable();
                if(StrUtils.fString(PITEM).length() > 0)   ht.put(IDBConstants.PARENTITEM,PITEM);
                if(StrUtils.fString(CITEM).length() > 0)   ht.put(IDBConstants.CHILDITEM,CITEM);
               
                    
               
   			ArrayList  movQryList = _ProductionBomUtil.getProdBomSummaryList(ht,PLANT," AND BOMTYPE='PROD'");
            
                 if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            
                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                            String pdesc = StrUtils.fString((String)lineArr.get("PDESC")) ;
                            String puom = StrUtils.fString((String)lineArr.get("PUOM")) ;
                            String cdesc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            String cuom = StrUtils.fString((String)lineArr.get("CUOM")) ;
                            String opseqnum = StrUtils.fString((String)lineArr.get("SEQNUM")) ;
                            String remark1 = StrUtils.fString((String)lineArr.get("REMARK1")) ;
                            String remark2 = StrUtils.fString((String)lineArr.get("REMARK2")) ;
                           
                            result += "<tr valign=\"middle\">" 
                            		+ "<td align = center>"  + id + "</td>"
                            		+ "<td align = center>"  + parentitem + "</td>"
                            		+ "<td align = center>"  + pdesc + "</td>"
                            		+ "<td align = center>"  + puom + "</td>"
                            		+ "<td align = center>"  + childitem + "</td>"
                            		+ "<td align = center>"  + cdesc + "</td>"
                            		+ "<td align = center>"  + cuom + "</td>"
                            		+ "<td align = center>"  + qty + "</td>"
                            		+ "<td align = center>"  + opseqnum + "</td>"
                            		+ "<td align = center>"  + remark1 + "</td>"
                            		+ "<td align = center>"  + remark2 + "</td>"
                            		+ "</tr>" ;
                            	
                          resultJsonInt.put("PRODBOMDATA", result);
                         
                            jsonArray.add(resultJsonInt);

                }
                    resultJson.put("productionbom", jsonArray);
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
                    resultJson.put("productionbom", jsonArray);
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
		
	private JSONObject validateParentProductBOM(String plant, String pitem) {
		JSONObject resultJson = new JSONObject();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		try {
			boolean itemFound = true;
			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.PARENTITEM,pitem);
			htCondition.put("PLANT", plant);
			itemFound = _ProductionBomUtil.isExistsProdBom(htCondition);
			if (itemFound) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", pitem);
				resultObjectJson.put("discription", itemMstDAO.getItemDesc(plant, pitem));
				resultObjectJson.put("detaildesc", itemMstDAO.getItemDetailDesc(plant, pitem));
				resultObjectJson.put("uom", itemMstDAO.getItemUOM(plant, pitem));
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
		
	private JSONObject validateChildProductBOM(String plant,String pitem, String citem) {
		JSONObject resultJson = new JSONObject();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		try {
			boolean itemFound = true;
			Hashtable htCondition = new Hashtable();
			if(pitem.length()>0){
				htCondition.put(IDBConstants.PARENTITEM,pitem);
			}
			htCondition.put(IDBConstants.CHILDITEM,citem);
			htCondition.put("PLANT", plant);
			itemFound = _ProductionBomUtil.isExistsProdBom(htCondition);
			if (itemFound) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", citem);
				resultObjectJson.put("discription", itemMstDAO.getItemDesc(plant, citem));
				resultObjectJson.put("detaildesc", itemMstDAO.getItemDetailDesc(plant, citem));
				resultObjectJson.put("uom", itemMstDAO.getItemUOM(plant, citem));
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject validateChildEquivalentProduct(String plant,String pitem, String citem) {
		JSONObject resultJson = new JSONObject();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		try {
			boolean itemFound = true;
			String childitem="";
			Hashtable htCondition = new Hashtable();
			if(pitem.length()>0){
				htCondition.put(IDBConstants.PARENTITEM,pitem);
			}
			htCondition.put(IDBConstants.CHILDITEM,citem);
			htCondition.put("BOMTYPE","KIT");
			htCondition.put("PLANT", plant);
			itemFound = _ProductionBomUtil.isExistsProdBom(htCondition);
			if(!itemFound)
			{
				childitem = _ProductionBomDAO.getchilditemfromequivalent(plant, pitem, citem);
				
			}
			
			if (itemFound) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", citem);
				resultObjectJson.put("discription", itemMstDAO.getItemDesc(plant, citem));
				resultObjectJson.put("detaildesc", itemMstDAO.getItemDetailDesc(plant, citem));
				//resultObjectJson.put("uom", itemMstDAO.getItemUOM(plant, citem));
				resultJson.put("result", resultObjectJson);
			} 
			else if(childitem.length()>0)
			{
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", citem);
				resultObjectJson.put("discription", itemMstDAO.getItemDesc(plant, citem));
				resultObjectJson.put("detaildesc", itemMstDAO.getItemDetailDesc(plant, citem));
				//resultObjectJson.put("uom", itemMstDAO.getItemUOM(plant, childitem));
				resultJson.put("result", resultObjectJson);
			}
			else
			{
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject validateKitcheckparentitem(String plant,String pitem) {
		JSONObject resultJson = new JSONObject();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		try {
			boolean pitemFound = true;
			Hashtable htCondition = new Hashtable();
			htCondition.put("PARENT_PRODUCT",pitem);
			htCondition.put("PLANT", plant);
			
			pitemFound = _ProductionBomDAO.isExistskitting(htCondition);
			if (pitemFound) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", pitem);
				resultObjectJson.put("discription", itemMstDAO.getItemDesc(plant, pitem));
				resultObjectJson.put("detaildesc", itemMstDAO.getItemDetailDesc(plant, pitem));
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject validateKitcheckchilditem(String plant,String pitem, String citem) {
		JSONObject resultJson = new JSONObject();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		try {
			boolean citemFound = true;
			Hashtable htCondition = new Hashtable();
			if(pitem.length()>0){
				htCondition.put("PARENT_PRODUCT",pitem);
			}
			htCondition.put("CHILD_PRODUCT",citem);
			htCondition.put("PLANT", plant);
			
			citemFound = _ProductionBomDAO.isExistskitting(htCondition);
			if (citemFound) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", citem);
				resultObjectJson.put("discription", itemMstDAO.getItemDesc(plant, citem));
				resultObjectJson.put("detaildesc", itemMstDAO.getItemDetailDesc(plant, citem));
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private JSONObject validateKitcheckBatch(String plant,String pitem, String citem,String batch) {
		JSONObject resultJson = new JSONObject();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		itemMstDAO.setmLogger(mLogger);
		try {
			boolean batchFound = true;
			Hashtable htCondition = new Hashtable();
			if(pitem.length()>0){
				htCondition.put("PARENT_PRODUCT",pitem);
			}
			htCondition.put("CHILD_PRODUCT",citem);
			htCondition.put("PLANT", plant);
			htCondition.put("CHILD_PRODUCT_BATCH", batch);
			batchFound = _ProductionBomDAO.isExistskitting(htCondition);
			if (batchFound) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("BATCH", batch);
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	private String addkitBOM(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String PLANT = "",PITEM = "",CITEM  = "",QTY="",EITEM="",PUOM="",CUOM="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			
			PITEM = StrUtils.fString(request.getParameter("ITEM"));
			CITEM = StrUtils.fString(request.getParameter("CITEM"));
			QTY = StrUtils.fString(request.getParameter("QTY"));
			EITEM = StrUtils.fString(request.getParameter("EITEM"));
			
			PUOM = StrUtils.fString(request.getParameter("PUOM"));
			CUOM = StrUtils.fString(request.getParameter("CUOM"));
			
			boolean pitemFound = true;
			boolean citemFound = true;
			boolean eitemFound = true;
			
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			try{
			itemMstUtil.isValidItemInItemmst(PLANT, PITEM);
			}
			catch (Exception e) {
				throw new Exception(" Scan/Enter a Valid Parent Product ID");
			}
			try{
			itemMstUtil.isValidItemInItemmst(PLANT, CITEM);
			}
			catch (Exception e) {
				throw new Exception(" Scan/Enter a Valid Child Product ID");
			}
			try{
				itemMstUtil.isValidItemInItemmst(PLANT, EITEM);
			}
			catch (Exception e) {
				throw new Exception(" Scan/Enter a Valid Equivalent Product ID");
			}
			
			
			Hashtable htkitbominsert = new Hashtable();
			htkitbominsert.put(IDBConstants.PLANT,PLANT);
			htkitbominsert.put(IDBConstants.PARENTITEM,PITEM);
			htkitbominsert.put("PARENTUOM",PUOM);
			htkitbominsert.put(IDBConstants.CHILDITEM,CITEM);
			htkitbominsert.put("CHILDUOM",CUOM);
			htkitbominsert.put(IDBConstants.QTY,QTY);
			htkitbominsert.put("BOMTYPE","KIT");
			htkitbominsert.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
			htkitbominsert.put(IDBConstants.LOGIN_USER,UserId);
			
			Hashtable htequiinsert = new Hashtable();
			if(EITEM.length()>0)
			{
				htequiinsert.put(IDBConstants.PLANT,PLANT);
				htequiinsert.put(IDBConstants.ITEM,PITEM);
				htequiinsert.put("CHILDITEM",CITEM);
				htequiinsert.put("EQIITEM",EITEM);
			}
			
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", PLANT);
			htm.put("DIRTYPE", TransactionConstants.ADD_KITBOM);
			htm.put("RECID", "");
			htm.put("ORDNUM",PITEM);
			htm.put("ITEM",CITEM);
			htm.put("REMARKS",EITEM);
			htm.put("UPBY", UserId);
			htm.put("CRBY", UserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));

			
			boolean insertflag = _ProductionBomUtil.insertProdBomMst(htkitbominsert);
			
			boolean inserted = mdao.insertIntoMovHis(htm);
			if(EITEM.length()>0)
			{
				ProductionBomDAO ProdBomDao = new ProductionBomDAO();
				if(insertflag&inserted){
					boolean eqiflag = ProdBomDao.isExistsEquivalentitem(htequiinsert);
					if(!eqiflag){
						eqiflag = ProdBomDao.insertIntoEquivalentItem(htequiinsert);
					}
				}
			}
			
			if(insertflag&inserted){
			resultJson.put("status", "100");
			msg = "Kit BOM Added Successfully";
			}
			else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Kit BOM</font>";
				
			}
			
						
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			String mes = e.getMessage();
			if(mes.contains("PRIMARY KEY constraint"))
			{
				msg = "<font class='mainred'>Child Product already exists for Parent Item</font>";
			}
			else
				{
					msg = "<font class='mainred'>"+e.getMessage()+"</font>";
				}
			
		}
		return msg;
	}
	private String addCopykitBOM(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String PLANT = "",PITEM = "",CITEM  = "",QTY="",EITEM="",PUOM="",CUOM="",OGITEM="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			OGITEM = StrUtils.fString(request.getParameter("OGITEM"));
			PITEM = StrUtils.fString(request.getParameter("ITEM"));
			PUOM = StrUtils.fString(request.getParameter("PUOM"));
			
			CITEM = StrUtils.fString(request.getParameter("CITEM"));
			QTY = StrUtils.fString(request.getParameter("QTY"));
			EITEM = StrUtils.fString(request.getParameter("EITEM"));
			CUOM = StrUtils.fString(request.getParameter("CUOM"));
			
			boolean pitemFound = true;
			boolean citemFound = true;
			boolean eitemFound = true;
			
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			try{
				itemMstUtil.isValidItemInItemmst(PLANT, PITEM);
			}
			catch (Exception e) {
				throw new Exception(" Scan/Enter a Valid Parent Product ID");
			}
			
			boolean insertflag = false,inserted=false;
			ArrayList  movQryList = new ProductionBomDAO().getCopyProdBomDetails(OGITEM,PLANT, " AND BOMTYPE='KIT'");
            
            if (movQryList.size() > 0) {
       	       
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	           			int id=iCnt+1;
	           			Map lineArr = (Map) movQryList.get(iCnt);
	                    JSONObject resultJsonInt = new JSONObject();
	                    String eitem="",edesc="",edetdesc="";
	                    double bomqty=0,kitqty=0,tranqty=0;
	                    String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
	                    String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
	                    String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
	                    String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
	                    CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
	                    
	                    Hashtable htkitbominsert = new Hashtable();
	        			htkitbominsert.put(IDBConstants.PLANT,PLANT);
	        			htkitbominsert.put(IDBConstants.PARENTITEM,PITEM);
	        			htkitbominsert.put("PARENTUOM",PUOM);
	        			htkitbominsert.put(IDBConstants.CHILDITEM,childitem);
	        			htkitbominsert.put("CHILDUOM",CUOM);
	        			htkitbominsert.put(IDBConstants.QTY,qty);
	        			htkitbominsert.put("BOMTYPE","KIT");
	        			htkitbominsert.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
	        			htkitbominsert.put(IDBConstants.LOGIN_USER,UserId);
	        			insertflag = _ProductionBomUtil.insertProdBomMst(htkitbominsert);
	        			
	        			mdao.setmLogger(mLogger);
	        			Hashtable htm = new Hashtable();
	        			htm.put("PLANT", PLANT);
	        			htm.put("DIRTYPE", TransactionConstants.ADD_KITBOM);
	        			htm.put("RECID", "");
	        			htm.put("ORDNUM",PITEM);
	        			htm.put("ITEM",childitem);
	        			htm.put("REMARKS",EITEM);
	        			htm.put("UPBY", UserId);
	        			htm.put("CRBY", UserId);
	        			htm.put("CRAT", dateutils.getDateTime());
	        			htm.put("UPAT", dateutils.getDateTime());
	        			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	        			inserted = mdao.insertIntoMovHis(htm);
	           			
	           }
            }
			
			
//			Hashtable htequiinsert = new Hashtable();
//			if(EITEM.length()>0)
//			{
//				htequiinsert.put(IDBConstants.PLANT,PLANT);
//				htequiinsert.put(IDBConstants.ITEM,PITEM);
//				htequiinsert.put("CHILDITEM",CITEM);
//				htequiinsert.put("EQIITEM",EITEM);
//			}
			
//			if(EITEM.length()>0)
//			{
//				ProductionBomDAO ProdBomDao = new ProductionBomDAO();
//				if(insertflag&inserted){
//					boolean eqiflag = ProdBomDao.isExistsEquivalentitem(htequiinsert);
//					if(!eqiflag){
//						eqiflag = ProdBomDao.insertIntoEquivalentItem(htequiinsert);
//					}
//				}
//			}
			
			if(insertflag&inserted){
				resultJson.put("status", "100");
				msg = "Kit BOM Added Successfully";
			}
			else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Kit BOM</font>";
				
			}
			
			
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			String mes = e.getMessage();
			if(mes.contains("PRIMARY KEY constraint"))
			{
				msg = "<font class='mainred'>Child Product already exists for Parent Item</font>";
			}
			else
			{
				msg = "<font class='mainred'>"+e.getMessage()+"</font>";
			}
			
		}
		return msg;
	}
	private JSONObject getKitBOMDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
        ProductionBomDAO ProdBomDao = new ProductionBomDAO();
     
        try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
               String PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
               String PQTY = StrUtils.fString(request.getParameter("PARENTQTY"));
               String TYPE = StrUtils.fString(request.getParameter("KITTYPE"));
               String KONO = StrUtils.fString(request.getParameter("KONO"));
               String OGITEM = StrUtils.fString(request.getParameter("OGITEM"));
               if(!OGITEM.equalsIgnoreCase(""))
            	   PITEM = OGITEM;
               ItemMstDAO itemMstDAO = new ItemMstDAO();  
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               
               
   			ArrayList  movQryList = _ProductionBomUtil.getProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");
            
                 if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String eitem="",edesc="",edetdesc="";
                            double bomqty=0,kitqty=0,tranqty=0;
                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                            String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            String PUOM = StrUtils.fString((String)lineArr.get("PUOM")) ;
                            String CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                            bomqty = Double.parseDouble(qty);
                            
                            String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                            eitem = ProdBomDao.getEquivalentitem(PLANT,parentitem,childitem);
                            if(eitem.length()>0)
                            {
                            	edesc = itemMstDAO.getItemDesc(PLANT, eitem);
                            	edetdesc = itemMstDAO.getItemDetailDesc(PLANT,eitem);
                            }
                            
                            if(TYPE.equalsIgnoreCase("KITDEKITWITHBOM")){
                            	String pbatch="",cloc="",cbatch="",cqty="",status="",scanitem="",pqty="";
                            	
                            	Hashtable htcond = new Hashtable();
                            	htcond.put("PLANT", PLANT);
                            	htcond.put("PITEM", PITEM);
                            	htcond.put("BOMTYPE", "KIT");
                            	
                            	Hashtable htkitcond = new Hashtable();
                            	htkitcond.put("PLANT", PLANT);
                            	htkitcond.put("PARENT_PRODUCT", PITEM);
                            	htkitcond.put("PARENT_PRODUCT_BATCH", PBATCH);
                            	htkitcond.put("KONO", KONO);
                            	
                            	ArrayList  KitList = bomdao.getDetails(" distinct CHILD_PRODUCT", htkitcond, "SCANSTATUS='C'");
                            	
                            	int kitcount = KitList.size();
                            	int childbomcount = _ProductionBomDAO.getCount(htcond);
                            	
                            	pbatch = ProdBomDao.getkittingparentbatchwithkono(PLANT,parentitem,childitem,KONO);
                            	pqty = ProdBomDao.getkittingparentqtywithkono(PLANT,parentitem,PBATCH,KONO);
                            	
                            	if(kitcount == childbomcount)
                            	{
                            		tranqty = bomqty * Double.parseDouble(pqty);
                            	}
                            	else if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
                            	{
                            		tranqty = bomqty * Double.parseDouble(PQTY);
                            	}
                            	else
                            	{
                            		tranqty = bomqty * Double.parseDouble(pqty);
                            	}
                            	
                            	//cbatch = ProdBomDao.getkittingchildbatch(PLANT,parentitem,childitem);
                            	ArrayList  movbatchList = ProdBomDao.getkitchildbatch(PLANT,parentitem,PBATCH,childitem, " AND KONO='"+KONO+"'");
                            	for (int i=0; i<movbatchList.size(); i++){
                        			Map lineArrbatch = (Map) movbatchList.get(i);
                        			if(movbatchList.size()>1){
                        				String childbatch = (String) lineArrbatch.get("CBATCH");
                        				if(cbatch.length()>0){
                        					cbatch = cbatch +","+childbatch;
                        				}
                        				else
                        				{
                        					cbatch = childbatch;
                        				}
                        				
                        			}
                        			else{
                        				cbatch = (String) lineArrbatch.get("CBATCH");
                        			}
                        		}
                            	cqty = ProdBomDao.getkittingchildqtywithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	cloc = ProdBomDao.getkittingchildlocwithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	scanitem = ProdBomDao.getkittingscanitemwithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	if(cqty.length()>0){
                            		kitqty = Double.parseDouble(cqty);
                            	}
                            	if(kitqty==0){status="N";mesflag = false;}
                            	else if(tranqty>kitqty){status="O";mesflag = false;}
                            	else{status = "C";mesflag = true;}
                            	chkString = parentitem+","+pbatch+","+childitem+","+cloc+","+cqty+","+scanitem+","+PUOM+","+CUOM+","+KONO;
                            	if(scanitem.equalsIgnoreCase(childitem))
                            	{
                            		result += "<tr valign=\"middle\" >"  
                                		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value='" +chkString+ "'></td>"
                                		+ "<td align = left>"  + id + "</td>"
                                		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                		+ "<td align = left>"  + desc + "</td>"
                                		+ "<td align = left>"  + cdetdesc + "</td>"
                                		+ "<td align = left>"  + CUOM + "</td>"
                                		/*+ "<td id='eitem_"+id+"' align = center><strike>"  + eitem + "</strike></td>"
                                		+ "<td align = center><strike>"  + edesc + "</strike></td>"
                                		+ "<td align = center><strike>"  + edetdesc + "</strike></td>"*/
                                		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                		+ "<td id='tranqty_"+id+"' align = celeftnter>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                		+ "<td align = left>"  + cbatch + "</td>"
                                		+ "<td id='kitqty_"+id+"' align = left>"  + cqty + "</td>"
                                		+ "<td align = left>"  + status + "</td>"
                                   		+ "</tr>" ;
                            	}
                            	else if(scanitem.equalsIgnoreCase(eitem) && eitem.length()>0){
                            		result += "<tr valign=\"middle\">"  
                                    		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = left>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = left><strike>"  + childitem + "</strike></td>"
                                    		+ "<td align = left><strike>"  + desc + "</strike></td>"
                                    		+ "<td align = left><strike>"  + cdetdesc + "</strike></td>"
                                    		+ "<td align = left>"  + CUOM + "</td>"
                                    		/*+ "<td id='eitem_"+id+"' align = center>"  + eitem + "</td>"
                                    		+ "<td align = center>"  + edesc + "</td>"
                                    		+ "<td align = center>"  + edetdesc + "</td>"*/
                                    		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = left>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                    		+ "<td align = left>"  + cbatch + "</td>"
                                    		+ "<td id='kitqty_"+id+"' align = left>"  + cqty + "</td>"
                                    		+ "<td align = left>"  + status + "</td>"
                                       		+ "</tr>" ;
                            	}
                            	else{
                            		result += "<tr valign=\"middle\">"  
                                    		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = left>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                    		+ "<td align = left>"  + desc + "</td>"
                                    		+ "<td align = left>"  + cdetdesc + "</td>"
                                    		+ "<td align = left>"  + CUOM + "</td>"
                                    		/*+ "<td id='eitem_"+id+"' align = center>"  + eitem + "</td>"
                                    		+ "<td align = center>"  + edesc + "</td>"
                                    		+ "<td align = center>"  + edetdesc + "</td>"*/
                                    		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = left>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                    		+ "<td align = left>"  + cbatch + "</td>"
                                    		+ "<td id='kitqty_"+id+"' align = left>"  + cqty + "</td>"
                                    		+ "<td align = left>"  + status + "</td>"
                                       		+ "</tr>" ;
                            	}
                            	
                            		
                            	if(kitcount == childbomcount)
                            	{
                            		completemsg = "<font class = "+IDBConstants.SUCCESS_COLOR +">It is Completed Transaction</font>";
                            		
                            	}
                            	else
                            	{
                            		completemsg="";
                            	}
                            	/*if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
                            	{
                            		completemsg="";
                            	}*/
                            	
                            }
                            else if(TYPE.equalsIgnoreCase("KITDEKITWITHBOMCOPY")){
                            	chkString = parentitem+","+childitem;
                            	/*result += "<tr valign=\"middle\">"  
                            			+ "<td align = left>"  + id + "</td>"
                            			+ "<td align = left>"  + childitem + "</td>"
                            			+ "<td align = left>"  + desc + "</td>"
                            			+ "<td align = left>"  + cdetdesc + "</td>"
                            			+ "<td align = left>"  + CUOM + "</td>"
                            			+ "<td align = left>"  + qty + "</td>"
                            			+ "</tr>" ;*/
                            	
                            	result += "<tr>";
                        		result += "<td class=\"item-img text-center\">";
                        		result += "<input type=\"text\" name=\"lnno\" class=\"form-control\" value=\""+id+"\" READONLY>";
                        		result += "</td>";
                        		result += "<td class=\"bill-item\">";
                        		result += "<input type=\"text\" name=\"citem\" class=\"form-control itemSearch\" style=\"width:87%\" placeholder=\"Type or click to select an item.\" value=\""+childitem+"\" placeholder=\"Type or click to select an item.\">";
                        		result += "<button type=\"button\" style=\"position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;\"  tabindex=\"-1\" onclick=\"changeitem(this)\"><i class=\"glyphicon glyphicon-menu-down\" style=\"font-size: 8px;\"></i></button>";
                        		result += "<input type=\"text\" name=\"citemdesc\" class=\"form-control\" style=\"width:87%\" value=\""+desc+"\" tabindex=\"-1\" READONLY>";
                        		result += "</td>";
                        		result += "<td class=\"bill-item\">";
                        		result += "<input type=\"text\" name=\"CUOM\" class=\"form-control cuomSearch\" placeholder=\"UOM\" value=\""+CUOM+"\" >";
                        		result += "</td>";
                            	result += "<td class=\"item-qty text-right grey-bg\" style=\"position: relative;\">";
                            	result += "<input type=\"text\" name=\"CQTY\" class=\"form-control text-right\" data-rl=\"0.000\" data-msq=\"0.000\" data-soh=\"0.000\" data-eq=\"0.000\" data-aq=\"0.000\" value=\""+StrUtils.addZeroes(Double.valueOf(qty), "3")+"\" >";
                            	result += "<span class=\"glyphicon glyphicon-remove-circle bill-action\" style=\"right: -12px;\" aria-hidden=\"true\"></span>";
                            	result += "</td>";                            	
                        		result += "</tr>";
                            	
                            }	
                            else{
                            	chkString = parentitem+","+childitem;
                               result += "<tr valign=\"middle\">"  
                            		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=\"" +chkString+ "\"></td>"
                            		+ "<td align = left>"  + id + "</td>"
                            		+ "<td align = left>"  + childitem + "</td>"
                            		+ "<td align = left>"  + desc + "</td>"
                            		+ "<td align = left>"  + cdetdesc + "</td>"
                                    + "<td align = left>"  + CUOM + "</td>"
                            		/*+ "<td align = center>"  + eitem + "</td>"
                            		+ "<td align = center>"  + edesc + "</td>"
                            		+ "<td align = center>"  + edetdesc + "</td>"*/
                            		+ "<td align = left>"  + qty + "</td>"
                               		+ "</tr>" ;
                            }	
                          resultJsonInt.put("KITBOMDATA", result);
                         
                            jsonArray.add(resultJsonInt);

                }
                    resultJson.put("kittingbom", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                    /*JSONObject resultJsonmes = new JSONObject();
                    resultJsonmes.put("MESSAGE", completemsg);
                    jsonArrayMes.add(resultJsonmes);
                    resultJson.put("messages", jsonArrayMes);*/
                    
                    
                    
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("kittingbom", jsonArray);
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

	private JSONObject getKitBOMDetailsForKitting(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
        ProductionBomDAO ProdBomDao = new ProductionBomDAO();
     
        try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
               String PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
               String PQTY = StrUtils.fString(request.getParameter("PARENTQTY"));
               String TYPE = StrUtils.fString(request.getParameter("KITTYPE"));
               String KONO = StrUtils.fString(request.getParameter("KONO"));
               ItemMstDAO itemMstDAO = new ItemMstDAO(); 
               PlantMstDAO plantMstDAO = new PlantMstDAO();
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               
               
   			ArrayList  movQryList = _ProductionBomUtil.getProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");
            
                 if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String eitem="",edesc="",edetdesc="";
                            double bomqty=0,kitqty=0,tranqty=0;
                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                            String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            String PUOM = StrUtils.fString((String)lineArr.get("PUOM")) ;
                            String CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                            bomqty = Double.parseDouble(qty);
                            
                            String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                            String citemprice = itemMstDAO.getItemPrice(PLANT, childitem);
                            eitem = ProdBomDao.getEquivalentitem(PLANT,parentitem,childitem);
                            if(eitem.length()>0)
                            {
                            	edesc = itemMstDAO.getItemDesc(PLANT, eitem);
                            	edetdesc = itemMstDAO.getItemDetailDesc(PLANT,eitem);
                            }
                            
                            if(TYPE.equalsIgnoreCase("KITDEKITWITHBOM")){
                            	String pbatch="",cloc="",cbatch="",cqty="",status="",scanitem="",pqty="";
                            	
                            	Hashtable htcond = new Hashtable();
                            	htcond.put("PLANT", PLANT);
                            	htcond.put("PITEM", PITEM);
                            	htcond.put("BOMTYPE", "KIT");
                            	
                            	Hashtable htkitcond = new Hashtable();
                            	htkitcond.put("PLANT", PLANT);
                            	htkitcond.put("PARENT_PRODUCT", PITEM);
                            	htkitcond.put("PARENT_PRODUCT_BATCH", PBATCH);
                            	htkitcond.put("KONO", KONO);
                            	
                            	ArrayList  KitList = bomdao.getDetails(" distinct CHILD_PRODUCT", htkitcond, "SCANSTATUS='C'");
                            	
                            	int kitcount = KitList.size();
                            	int childbomcount = _ProductionBomDAO.getCount(htcond);
                            	
                            	pbatch = ProdBomDao.getkittingparentbatchwithkono(PLANT,parentitem,childitem,KONO);
                            	pqty = ProdBomDao.getkittingparentqtywithkono(PLANT,parentitem,PBATCH,KONO);
                            	
                            	if(kitcount == childbomcount)
                            	{
                            		tranqty = bomqty * Double.parseDouble(pqty);
                            	}
                            	else if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
                            	{
                            		tranqty = bomqty * Double.parseDouble(PQTY);
                            	}
                            	else
                            	{
                            		tranqty = bomqty * Double.parseDouble(pqty);
                            	}
                            	
                            	//cbatch = ProdBomDao.getkittingchildbatch(PLANT,parentitem,childitem);
                            	ArrayList  movbatchList = ProdBomDao.getkitchildbatch(PLANT,parentitem,PBATCH,childitem, " AND KONO='"+KONO+"'");
                            	for (int i=0; i<movbatchList.size(); i++){
                        			Map lineArrbatch = (Map) movbatchList.get(i);
                        			if(movbatchList.size()>1){
                        				String childbatch = (String) lineArrbatch.get("CBATCH");
                        				if(cbatch.length()>0){
                        					cbatch = cbatch +","+childbatch;
                        				}
                        				else
                        				{
                        					cbatch = childbatch;
                        				}
                        				
                        			}
                        			else{
                        				cbatch = (String) lineArrbatch.get("CBATCH");
                        			}
                        		}
                            	cqty = ProdBomDao.getkittingchildqtywithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	cloc = ProdBomDao.getkittingchildlocwithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	scanitem = ProdBomDao.getkittingscanitemwithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	if(cqty.length()>0){
                            		kitqty = Double.parseDouble(cqty);
                            	}
                            	if(kitqty==0){status="N";mesflag = false;}
                            	else if(tranqty>kitqty){status="O";mesflag = false;}
                            	else{status = "C";mesflag = true;}
                            	chkString = parentitem+","+pbatch+","+childitem+","+cloc+","+cqty+","+scanitem+","+PUOM+","+CUOM+","+KONO;
                            	Double transprice = Double.valueOf(tranqty) * Double.valueOf(citemprice);
                            	Double bomprice = Double.valueOf(qty) * Double.valueOf(citemprice);
                            	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
                            	if(scanitem.equalsIgnoreCase(childitem))
                            	{
                            		result += "<tr valign=\"middle\" >"  
                                		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                		+ "<td align = left>"  + id + "</td>"
                                		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                		+ "<td align = left>"  + desc + "</td>"
                                		+ "<td align = left>"  + cdetdesc + "</td>"
                                		+ "<td align = left>"  + CUOM + "</td>"
                                		/*+ "<td id='eitem_"+id+"' align = center><strike>"  + eitem + "</strike></td>"
                                		+ "<td align = center><strike>"  + edesc + "</strike></td>"
                                		+ "<td align = center><strike>"  + edetdesc + "</strike></td>"*/
                                		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                		+ "<td id='tranqty_"+id+"' align = celeftnter>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                		+ "<td align = left>"  + StrUtils.addZeroes(Double.valueOf(citemprice), numberOfDecimal) + "</td>"
                                		+ "<td align = left>"  + StrUtils.addZeroes(bomprice, numberOfDecimal) + "</td>"
                                		+ "<td align = left>"  + StrUtils.addZeroes(transprice, numberOfDecimal) + "</td>"
                                		+ "<td align = left>"  + cbatch + "</td>"
                                		+ "<td id='kitqty_"+id+"' align = left>"  + cqty + "</td>"
                                		+ "<td align = left>"  + status + "</td>"
                                   		+ "</tr>" ;
                            	}
                            	else if(scanitem.equalsIgnoreCase(eitem) && eitem.length()>0){
                            		result += "<tr valign=\"middle\">"  
                                    		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = left>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = left><strike>"  + childitem + "</strike></td>"
                                    		+ "<td align = left><strike>"  + desc + "</strike></td>"
                                    		+ "<td align = left><strike>"  + cdetdesc + "</strike></td>"
                                    		+ "<td align = left>"  + CUOM + "</td>"
                                    		/*+ "<td id='eitem_"+id+"' align = center>"  + eitem + "</td>"
                                    		+ "<td align = center>"  + edesc + "</td>"
                                    		+ "<td align = center>"  + edetdesc + "</td>"*/
                                    		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = left>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                    		+ "<td align = left>"  + StrUtils.addZeroes(Double.valueOf(citemprice), numberOfDecimal) + "</td>"
                                            + "<td align = left>"  + StrUtils.addZeroes(bomprice, numberOfDecimal) + "</td>"
                                            + "<td align = left>"  + StrUtils.addZeroes(transprice, numberOfDecimal) + "</td>"
                                    		+ "<td align = left>"  + cbatch + "</td>"
                                    		+ "<td id='kitqty_"+id+"' align = left>"  + cqty + "</td>"
                                    		+ "<td align = left>"  + status + "</td>"
                                       		+ "</tr>" ;
                            	}
                            	else{
                            		result += "<tr valign=\"middle\">"  
                                    		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = left>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                    		+ "<td align = left>"  + desc + "</td>"
                                    		+ "<td align = left>"  + cdetdesc + "</td>"
                                    		+ "<td align = left>"  + CUOM + "</td>"
                                    		/*+ "<td id='eitem_"+id+"' align = center>"  + eitem + "</td>"
                                    		+ "<td align = center>"  + edesc + "</td>"
                                    		+ "<td align = center>"  + edetdesc + "</td>"*/
                                    		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = left>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                    		+ "<td align = left>"  + StrUtils.addZeroes(Double.valueOf(citemprice), numberOfDecimal) + "</td>"
                                            + "<td align = left>"  + StrUtils.addZeroes(bomprice, numberOfDecimal) + "</td>"
                                            + "<td align = left>"  + StrUtils.addZeroes(transprice, numberOfDecimal) + "</td>"
                                    		+ "<td align = left>"  + cbatch + "</td>"
                                    		+ "<td id='kitqty_"+id+"' align = left>"  + cqty + "</td>"
                                    		+ "<td align = left>"  + status + "</td>"
                                       		+ "</tr>" ;
                            	}
                            	
                            		
                            	if(kitcount == childbomcount)
                            	{
                            		completemsg = "<font class = "+IDBConstants.SUCCESS_COLOR +">It is Completed Transaction</font>";
                            		
                            	}
                            	else
                            	{
                            		completemsg="";
                            	}
                            	/*if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
                            	{
                            		completemsg="";
                            	}*/
                            	
                            }
                            else{
                            	chkString = parentitem+","+childitem;
                               result += "<tr valign=\"middle\">"  
                            		+ "<td align = left><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td align = left>"  + id + "</td>"
                            		+ "<td align = left>"  + childitem + "</td>"
                            		+ "<td align = left>"  + desc + "</td>"
                            		+ "<td align = left>"  + cdetdesc + "</td>"
                                    + "<td align = left>"  + CUOM + "</td>"
                            		/*+ "<td align = center>"  + eitem + "</td>"
                            		+ "<td align = center>"  + edesc + "</td>"
                            		+ "<td align = center>"  + edetdesc + "</td>"*/
                            		+ "<td align = left>"  + qty + "</td>"
                               		+ "</tr>" ;
                            }	
                          resultJsonInt.put("KITBOMDATA", result);
                         
                            jsonArray.add(resultJsonInt);

                }
                    resultJson.put("kittingbom", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                    /*JSONObject resultJsonmes = new JSONObject();
                    resultJsonmes.put("MESSAGE", completemsg);
                    jsonArrayMes.add(resultJsonmes);
                    resultJson.put("messages", jsonArrayMes);*/
                    
                    
                    
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("kittingbom", jsonArray);
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
	
	private JSONObject getKitBOMDetailsFDET(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
        ProductionBomDAO ProdBomDao = new ProductionBomDAO();
     
        try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
               String PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
               String PQTY = StrUtils.fString(request.getParameter("PARENTQTY"));
               String TYPE = StrUtils.fString(request.getParameter("KITTYPE"));
               String KONO = StrUtils.fString(request.getParameter("KONO"));
               ItemMstDAO itemMstDAO = new ItemMstDAO();  
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               
               
   			ArrayList  movQryList = _ProductionBomUtil.getProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");
            
                 if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String eitem="",edesc="",edetdesc="";
                            double bomqty=0,kitqty=0,tranqty=0;
                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                            String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            String PUOM = StrUtils.fString((String)lineArr.get("PUOM")) ;
                            String CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                            bomqty = Double.parseDouble(qty);
                            
                            String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                            eitem = ProdBomDao.getEquivalentitem(PLANT,parentitem,childitem);
                            if(eitem.length()>0)
                            {
                            	edesc = itemMstDAO.getItemDesc(PLANT, eitem);
                            	edetdesc = itemMstDAO.getItemDetailDesc(PLANT,eitem);
                            }
                            
                            if(TYPE.equalsIgnoreCase("KITDEKITWITHBOM")){
                            	String pbatch="",cloc="",cbatch="",cqty="",status="",scanitem="",pqty="";
                            	
                            	Hashtable htcond = new Hashtable();
                            	htcond.put("PLANT", PLANT);
                            	htcond.put("PITEM", PITEM);
                            	htcond.put("BOMTYPE", "KIT");
                            	
                            	Hashtable htkitcond = new Hashtable();
                            	htkitcond.put("PLANT", PLANT);
                            	htkitcond.put("PARENT_PRODUCT", PITEM);
                            	htkitcond.put("PARENT_PRODUCT_BATCH", PBATCH);
                            	htkitcond.put("KONO", KONO);
                            	
                            	ArrayList  KitList = bomdao.getDetails(" distinct CHILD_PRODUCT", htkitcond, "SCANSTATUS='C'");
                            	
                            	int kitcount = KitList.size();
                            	int childbomcount = _ProductionBomDAO.getCount(htcond);
                            	
                            	pbatch = ProdBomDao.getkittingparentbatchwithkono(PLANT,parentitem,childitem,KONO);
                            	pqty = ProdBomDao.getkittingparentqtywithkono(PLANT,parentitem,PBATCH,KONO);
                            	
                            	if(kitcount == childbomcount)
                            	{
                            		tranqty = bomqty * Double.parseDouble(pqty);
                            	}
                            	else if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
                            	{
                            		tranqty = bomqty * Double.parseDouble(PQTY);
                            	}
                            	else
                            	{
                            		tranqty = bomqty * Double.parseDouble(pqty);
                            	}
                            	
                            	//cbatch = ProdBomDao.getkittingchildbatch(PLANT,parentitem,childitem);
                            	ArrayList  movbatchList = ProdBomDao.getkitchildbatch(PLANT,parentitem,PBATCH,childitem, " AND KONO='"+KONO+"'");
                            	for (int i=0; i<movbatchList.size(); i++){
                        			Map lineArrbatch = (Map) movbatchList.get(i);
                        			if(movbatchList.size()>1){
                        				String childbatch = (String) lineArrbatch.get("CBATCH");
                        				if(cbatch.length()>0){
                        					cbatch = cbatch +","+childbatch;
                        				}
                        				else
                        				{
                        					cbatch = childbatch;
                        				}
                        				
                        			}
                        			else{
                        				cbatch = (String) lineArrbatch.get("CBATCH");
                        			}
                        		}
                            	cqty = ProdBomDao.getkittingchildqtywithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	cloc = ProdBomDao.getkittingchildlocwithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	scanitem = ProdBomDao.getkittingscanitemwithkono(PLANT,parentitem,PBATCH,childitem,KONO);
                            	if(cqty.length()>0){
                            		kitqty = Double.parseDouble(cqty);
                            	}
                            	if(kitqty==0){status="N";mesflag = false;}
                            	else if(tranqty>kitqty){status="O";mesflag = false;}
                            	else{status = "C";mesflag = true;}
                            	chkString = parentitem+","+pbatch+","+childitem+","+cloc+","+cqty+","+scanitem+","+PUOM+","+CUOM+","+KONO;
                            	if(scanitem.equalsIgnoreCase(childitem))
                            	{
                            		result += "<tr valign=\"middle\" >"  
                                		//+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                		+ "<td align = center>"  + id + "</td>"
                                		+ "<td id='citem_"+id+"' align = center>"  + childitem + "</td>"
                                		+ "<td align = center>"  + desc + "</td>"
                                		+ "<td align = center>"  + cdetdesc + "</td>"
                                		+ "<td align = center>"  + CUOM + "</td>"
                                		/*+ "<td id='eitem_"+id+"' align = center><strike>"  + eitem + "</strike></td>"
                                		+ "<td align = center><strike>"  + edesc + "</strike></td>"
                                		+ "<td align = center><strike>"  + edetdesc + "</strike></td>"*/
                                		+ "<td id='bomqty_"+id+"' align = center>"  + qty + "</td>"
                                		+ "<td id='tranqty_"+id+"' align = center>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                		+ "<td align = center>"  + cbatch + "</td>"
                                		+ "<td id='kitqty_"+id+"' align = center>"  + cqty + "</td>"
                                		+ "<td align = center>"  + status + "</td>"
                                   		+ "</tr>" ;
                            	}
                            	else if(scanitem.equalsIgnoreCase(eitem) && eitem.length()>0){
                            		result += "<tr valign=\"middle\">"  
                                    		//+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = center>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = center><strike>"  + childitem + "</strike></td>"
                                    		+ "<td align = center><strike>"  + desc + "</strike></td>"
                                    		+ "<td align = center><strike>"  + cdetdesc + "</strike></td>"
                                    		+ "<td align = center>"  + CUOM + "</td>"
                                    		/*+ "<td id='eitem_"+id+"' align = center>"  + eitem + "</td>"
                                    		+ "<td align = center>"  + edesc + "</td>"
                                    		+ "<td align = center>"  + edetdesc + "</td>"*/
                                    		+ "<td id='bomqty_"+id+"' align = center>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = center>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                    		+ "<td align = center>"  + cbatch + "</td>"
                                    		+ "<td id='kitqty_"+id+"' align = center>"  + cqty + "</td>"
                                    		+ "<td align = center>"  + status + "</td>"
                                       		+ "</tr>" ;
                            	}
                            	else{
                            		result += "<tr valign=\"middle\">"  
                                    		//+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = center>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = center>"  + childitem + "</td>"
                                    		+ "<td align = center>"  + desc + "</td>"
                                    		+ "<td align = center>"  + cdetdesc + "</td>"
                                    		+ "<td align = center>"  + CUOM + "</td>"
                                    		/*+ "<td id='eitem_"+id+"' align = center>"  + eitem + "</td>"
                                    		+ "<td align = center>"  + edesc + "</td>"
                                    		+ "<td align = center>"  + edetdesc + "</td>"*/
                                    		+ "<td id='bomqty_"+id+"' align = center>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = center>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                    		+ "<td align = center>"  + cbatch + "</td>"
                                    		+ "<td id='kitqty_"+id+"' align = center>"  + cqty + "</td>"
                                    		+ "<td align = center>"  + status + "</td>"
                                       		+ "</tr>" ;
                            	}
                            	
                            		
                            	if(kitcount == childbomcount)
                            	{
                            		completemsg = "<font class = "+IDBConstants.SUCCESS_COLOR +">It is Completed Transaction</font>";
                            		
                            	}
                            	else
                            	{
                            		completemsg="";
                            	}
                            	/*if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
                            	{
                            		completemsg="";
                            	}*/
                            	
                            }
                            else{
                            	chkString = parentitem+","+childitem;
                               result += "<tr valign=\"middle\">"  
                            		//+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td align = center>"  + id + "</td>"
                            		+ "<td align = center>"  + childitem + "</td>"
                            		+ "<td align = center>"  + desc + "</td>"
                            		+ "<td align = center>"  + cdetdesc + "</td>"
                                    + "<td align = center>"  + CUOM + "</td>"
                            		/*+ "<td align = center>"  + eitem + "</td>"
                            		+ "<td align = center>"  + edesc + "</td>"
                            		+ "<td align = center>"  + edetdesc + "</td>"*/
                            		+ "<td align = center>"  + StrUtils.addZeroes(Double.parseDouble(qty), "3") + "</td>"
                               		+ "</tr>" ;
                            }	
                          resultJsonInt.put("KITBOMDATA", result);
                         
                            jsonArray.add(resultJsonInt);

                }
                    resultJson.put("kittingbom", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
                    /*JSONObject resultJsonmes = new JSONObject();
                    resultJsonmes.put("MESSAGE", completemsg);
                    jsonArrayMes.add(resultJsonmes);
                    resultJson.put("messages", jsonArrayMes);*/
                    
                    
                    
              } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("kittingbom", jsonArray);
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
	private String DeleteKitBOM(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		Map receiveMaterial_HM = null;
		UserTransaction ut =null;
		String PLANT = "", 
				UserId = "";
		String PITEM = "", CITEM = "",result="",fieldDescData="",rflag="";
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		//Boolean allChecked = false;
			
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			rflag = StrUtils.fString(request.getParameter("RFLAG"));
			String PUOM = StrUtils.fString(request.getParameter("PARENTUOM"));
			
			
			Boolean transactionHandler = false;
	        ut = DbBean.getUserTranaction();
	        ut.begin();
	
	   String[] chkditems  = request.getParameterValues("chkitem"); 
	   
	   process: 	
			if (chkditems != null)    {     
				for (int i = 0; i < chkditems.length; i++)       { 
					String item = chkditems[i];
					String itemArray[] = item.split(",");
					PITEM = itemArray[0];
					CITEM = itemArray[1];
							
					Hashtable htkitbomdelete = new Hashtable();
					htkitbomdelete.put(IDBConstants.PLANT,PLANT);
					htkitbomdelete.put(IDBConstants.PARENTITEM,PITEM);
					htkitbomdelete.put(IDBConstants.CHILDITEM,CITEM);
					htkitbomdelete.put("BOMTYPE","KIT");
						
					mdao.setmLogger(mLogger);
					Hashtable htm = new Hashtable();
					htm.put("PLANT", PLANT);
					htm.put("DIRTYPE", TransactionConstants.DEL_KITBOM);
					htm.put("RECID", "");
					htm.put("ORDNUM",PITEM);
					htm.put("ITEM",CITEM);
					htm.put("REMARKS","");
					htm.put("UPBY", UserId);
					htm.put("CRBY", UserId);
					htm.put("CRAT", dateutils.getDateTime());
					htm.put("UPAT", dateutils.getDateTime());
					htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));

					Hashtable htequidelete = new Hashtable();
					htequidelete.put(IDBConstants.PLANT,PLANT);
					htequidelete.put("ITEM",PITEM);
					htequidelete.put("CHILDITEM",CITEM);
					
			
			
			transactionHandler = _ProductionBomUtil.deleteProdBom(htkitbomdelete);
			
			boolean inserted = mdao.insertIntoMovHis(htm);
			
			boolean eqiflag = ProdBomDao.isExistsEquivalentitem(htequidelete);
			{
				if(eqiflag){
					eqiflag = ProdBomDao.deleteEquivalentitem(htequidelete);
				}
			}
						
			if(!transactionHandler)
				break process;
			}
		}
	
	    
		if(transactionHandler==true)
		{
			DbBean.CommitTran(ut);
			request.getSession().setAttribute(
					"RESULT","Kit BOM Deleted successfully for Parent Item:"+PITEM);
			if(rflag.equalsIgnoreCase("1")){
			//response.sendRedirect("../billofmaterials/summary?RESULT=Kit BOM Deleted successfully");
				response.sendRedirect("../billofmaterials/new?action=result&ITEM="+PITEM+"&PUOM="+PUOM);
			}
			else{
				//response.sendRedirect("../billofmaterials/summary?RESULT=Kit BOM Deleted successfully");
				response.sendRedirect("../billofmaterials/new?action=result&ITEM="+PITEM+"&PUOM="+PUOM);
				}
		}
		else{
			DbBean.RollbackTran(ut);
			
			request.getSession().setAttribute(
					"RESULTERROR","Error in Deleting Kit BOM for Parent Item:"+PITEM);
			if(rflag.equalsIgnoreCase("1")){		
			response.sendRedirect("../billofmaterials/new?action=resulterror&ITEM="+PITEM);}
			else{response.sendRedirect("../billofmaterials/edit?action=resulterror&ITEM="+PITEM);}
			}
	

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
			if(rflag.equalsIgnoreCase("1")){
			response.sendRedirect("../billofmaterials/new?action=catchrerror&ITEM="+PITEM);}
			else{response.sendRedirect("../billofmaterials/edit?action=catchrerror&ITEM="+PITEM);}
			throw e;
		}

		return msg;
	}
	private JSONObject getKitBOMSummaryDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ProductionBomDAO ProdBomDao = new ProductionBomDAO();
     
        try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
               String PDESC = StrUtils.fString(request.getParameter("DESC"));
               String PDETAILDESC = StrUtils.fString(request.getParameter("DETDESC"));
               String CITEM = StrUtils.fString(request.getParameter("CITEM"));
               String CDESC = StrUtils.fString(request.getParameter("CDESC"));
               String CDETDESC = StrUtils.fString(request.getParameter("CDETDESC"));
               String EITEM = StrUtils.fString(request.getParameter("EITEM"));
               String EDESC = StrUtils.fString(request.getParameter("EDESC"));
               String EDETDESC = StrUtils.fString(request.getParameter("EDETDESC"));
               ItemMstDAO itemMstDAO = new ItemMstDAO();  
               
   		    	ArrayList  movQryList = _ProductionBomUtil.getProdBomSummaryList(PITEM,PLANT,PDESC,PDETAILDESC,CITEM,CDESC,CDETDESC,EITEM,EDESC,EDETDESC, " AND BOMTYPE='KIT'");
            
                if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String eitem="",edesc="",edetdesc="";
                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                            String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                                               
                            String pdesc = itemMstDAO.getItemDesc(PLANT, parentitem);
                           	String pdetdesc = itemMstDAO.getItemDetailDesc(PLANT,parentitem);
                           	
                           	String cdesc = itemMstDAO.getItemDesc(PLANT, childitem);
                           	String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                           
                            eitem = ProdBomDao.getEquivalentitem(PLANT,parentitem,childitem);
                            if(eitem.length()>0)
                            {
                            	edesc = itemMstDAO.getItemDesc(PLANT, eitem);
                            	edetdesc = itemMstDAO.getItemDetailDesc(PLANT,eitem);
                            }
                            chkString = parentitem+","+childitem;
                            

                             resultJsonInt.put("id", id);
                             resultJsonInt.put("parentitem", parentitem);
                             resultJsonInt.put("pdesc", pdesc);
                             resultJsonInt.put("pdetdesc", pdetdesc);
                             resultJsonInt.put("childitem", childitem);
                             resultJsonInt.put("cdesc", cdesc);
                             resultJsonInt.put("cdetdesc", cdetdesc);
                             resultJsonInt.put("eitem", eitem);
                             resultJsonInt.put("edesc", edesc);
                             resultJsonInt.put("edetdesc", edetdesc);
                             resultJsonInt.put("qty", qty);
                         	 jsonArray.add(resultJsonInt);
                            
                            
                           /* result += "<tr valign=\"middle\">"  
                            		+ "<td align = center>"  + id + "</td>"
                            		+ "<td align = center>"  + parentitem+ "</td>"
                            		+ "<td align = center>"  + pdesc + "</td>"
                            		+ "<td align = center>"  + pdetdesc + "</td>"
                            		+ "<td align = center>"  + childitem + "</td>"
                            		+ "<td align = center>"  + cdesc + "</td>"
                            		+ "<td align = center>"  + cdetdesc + "</td>"
                            		+ "<td align = center>"  + eitem + "</td>"
                            		+ "<td align = center>"  + edesc + "</td>"
                            		+ "<td align = center>"  + edetdesc + "</td>"
                            		+ "<td align = center>"  + qty + "</td>"
                            		+ "</tr>" ;
                            	
                          resultJsonInt.put("KITBOMDATA", result);
                         
                            jsonArray.add(resultJsonInt);*/

                }
                    resultJson.put("kittingbom", jsonArray);
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
                    resultJson.put("kittingbom", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        	
        	    jsonArray.add("");
    		    resultJson.put("items", jsonArray);
        	
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
	private JSONObject getParentKitBOMSummaryDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ProductionBomDAO ProdBomDao = new ProductionBomDAO();
     
        try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
               String PDESC = StrUtils.fString(request.getParameter("DESC"));
               String PDETAILDESC = StrUtils.fString(request.getParameter("DETDESC"));
               String CITEM = StrUtils.fString(request.getParameter("CITEM"));
               String CDESC = StrUtils.fString(request.getParameter("CDESC"));
               String CDETDESC = StrUtils.fString(request.getParameter("CDETDESC"));
               String EITEM = StrUtils.fString(request.getParameter("EITEM"));
               String EDESC = StrUtils.fString(request.getParameter("EDESC"));
               String EDETDESC = StrUtils.fString(request.getParameter("EDETDESC"));
               ItemMstDAO itemMstDAO = new ItemMstDAO();  
               
   		    	ArrayList  movQryList = _ProductionBomUtil.getParentBomSummaryList(PITEM,PLANT);
            
                if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();

                            resultJsonInt.put("id", id);
                            resultJsonInt.put("parentitem", StrUtils.fString((String)lineArr.get("PITEM")));
                            resultJsonInt.put("pdesc", StrUtils.fString((String)lineArr.get("ITEMDESC")));
                            resultJsonInt.put("pdetdesc", StrUtils.fString((String)lineArr.get("ITEMDETAILDESC")));
                            resultJsonInt.put("puom", StrUtils.fString((String)lineArr.get("PARENTUOM")));
                         	jsonArray.add(resultJsonInt);
                            

                }
                    resultJson.put("kittingbom", jsonArray);
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
                    resultJson.put("kittingbom", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        	
        	    jsonArray.add("");
    		    resultJson.put("items", jsonArray);
        	
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
	private JSONObject getKitDeKitBOMSummaryDetails(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ProductionBomDAO ProdBomDao = new ProductionBomDAO();
     
        try {
        
               String PLANT= StrUtils.fString(request.getParameter("PLANT"));
               String PITEM = StrUtils.fString(request.getParameter("ITEM"));
               String KONO = StrUtils.fString(request.getParameter("KONO"));
               String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String fdate = "", tdate = "";

               ItemMstDAO itemMstDAO = new ItemMstDAO();  
				if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
		           String curDate =DateUtils.getDate();
					if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
						FROM_DATE=curDate;

				if (FROM_DATE.length() > 5)
					fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-"
							+ FROM_DATE.substring(0, 2);

				if (TO_DATE == null)
					TO_DATE = "";
				else
					TO_DATE = TO_DATE.trim();
				if (TO_DATE.length() > 5)
					tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

   		    	ArrayList  movQryList = _ProductionBomDAO.getKittingDeKittinglist(PITEM,KONO,fdate,tdate,PLANT, " ");
            
                if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String eitem="",edesc="",edetdesc="";
                            String parentitem = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT")) ;
                            String kono = StrUtils.fString((String)lineArr.get("KONO")) ;
                            String UOM = StrUtils.fString((String)lineArr.get("UOM")) ;                            
                                               
                            String pdesc =  StrUtils.fString((String)lineArr.get("PDESC")) ;
                            String pdetdesc =  StrUtils.fString((String)lineArr.get("PDETDESC")) ;
                           	                            

                             resultJsonInt.put("id", id);
                             resultJsonInt.put("parentitem", parentitem);
                             resultJsonInt.put("pdesc", pdesc);
                             resultJsonInt.put("pdetdesc", pdetdesc);
                             resultJsonInt.put("LOC_ID", StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_LOC")));
                             resultJsonInt.put("BATCH_0", StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_BATCH")));
                             resultJsonInt.put("uom", UOM);
                             resultJsonInt.put("kono", kono);
                             jsonArray.add(resultJsonInt);
                                                        
                }
                    resultJson.put("kittingbom", jsonArray);
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
                    resultJson.put("kittingbom", jsonArray);
                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        	
        	    jsonArray.add("");
    		    resultJson.put("items", jsonArray);
        	
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
	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        ProductionBomDAO itemUtil = new ProductionBomDAO();
        StrUtils strUtils = new StrUtils();        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String kono = StrUtils.fString(request.getParameter("KONO")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(kono.length()>0) extCond=" AND plant='"+plant+"' and kono like '"+kono+"%' ";
		     
		     //extCond=extCond+"ORDER BY CONVERT(date, ORDDATE, 103) desc";
		     ArrayList listQry = itemUtil.selectBomHdr(" distinct kono",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  kono = (String)m.get("kono");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("KONO", kono);				  
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
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}	
private String addkitting(HttpServletRequest request,HttpServletResponse response) 
		throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String Empid="",Empname="",Emplastname="",Loc="",Locdesc="",Loctype="",Loctypedesc="",Remarks="",Reasoncode="";
		String PLANT = "",PITEM = "",PBATCH="",CITEM  = "",CBATCH="",QTY="",KITTYPE="",PQTY="",PUOM="",CUOM="",kono="",ORDDATE="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			
			Empid = StrUtils.fString(request.getParameter("EMP_ID"));
			Loc = StrUtils.fString(request.getParameter("LOC_ID"));
			Loctype = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
			Remarks = StrUtils.fString(request.getParameter("REMARKS"));
			Reasoncode = StrUtils.fString(request.getParameter("REASONCODE"));
			PITEM = StrUtils.fString(request.getParameter("ITEM"));
			PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
			PQTY = StrUtils.fString(request.getParameter("PARENTQTY"));
			CITEM = StrUtils.fString(request.getParameter("CITEM"));
			CBATCH = StrUtils.fString(request.getParameter("BATCH_1"));
			QTY = StrUtils.fString(request.getParameter("QTY"));
			String TYPE = StrUtils.fString(request.getParameter("KITTYPE"));
			PUOM = StrUtils.fString(request.getParameter("PUOM"));
			CUOM = StrUtils.fString(request.getParameter("CUOM"));
			kono = StrUtils.fString(request.getParameter("KONO"));
			ORDDATE = StrUtils.fString(request.getParameter("ORDDATE"));
			
			String PUOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+PUOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			PUOMQTY=(String)mapval.get("UOMQTY");
			}
			
			String CUOMQTY="1";
			htTrand1 = new Hashtable<String, String>();
			movHisDao1 = new MovHisDAO();
			getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+CUOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			CUOMQTY=(String)mapval.get("UOMQTY");
			}
			
			boolean EmpFound = false;
			boolean LocFound = true;
			boolean LoctypeFound = true;
			boolean RsncodeFound = true;
			boolean pitemFound = false;
			boolean citemFound = false;
			
			EmployeeUtil empUtil = new EmployeeUtil();
			Hashtable htEmp = new Hashtable();
			htEmp.put(IDBConstants.PLANT,PLANT);
			htEmp.put(IDBConstants.EMPNO,Empid);
			
				EmpFound = empUtil.isExistsEmployee(htEmp);
			
			if(!EmpFound) {
				throw new Exception(" Scan/Enter a Valid Employee ID");
			}
					
			
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			InvMstDAO invmstdao = new InvMstDAO();
			
			pitemFound = itemMstUtil.isValidItemInItemmst(PLANT, PITEM);
			if(!pitemFound) {
				throw new Exception(" Scan/Enter a Valid Parent Product ID");
			}
			
			citemFound = itemMstUtil.isValidItemInItemmst(PLANT, CITEM);
			if(!citemFound) {
				throw new Exception(" Scan/Enter a Valid Child Product ID");
			}
			Hashtable htparentcond = new Hashtable();
			htparentcond.put("PARENT_PRODUCT",PITEM);
			htparentcond.put("PARENT_PRODUCT_BATCH",PBATCH);
			htparentcond.put("PLANT", PLANT);
			
			LocFound = _ProductionBomDAO.isExistskitting(htparentcond);
			if(LocFound)
			{
				String kitloc = _ProductionBomDAO.getkittinglocation(PLANT,PITEM,PBATCH);
				if(kitloc.length()>0)
				{
					if(!kitloc.equalsIgnoreCase(Loc))
					{
						throw new Exception(" Scan Correct Location for all the child items. Correct Location is: " +kitloc);
					}
				}
							
			}
			
			boolean itemFound = true;
			String childitem="",scanitem="",childbomqty="";
			Double tranqty;
			if(TYPE.equalsIgnoreCase("KITDEKITWITHBOM")){
				Hashtable htCondition = new Hashtable();
				htCondition.put(IDBConstants.PARENTITEM,PITEM);
				htCondition.put(IDBConstants.CHILDITEM,CITEM);
				htCondition.put("BOMTYPE","KIT");
				htCondition.put("PLANT", PLANT);
				itemFound = _ProductionBomUtil.isExistsProdBom(htCondition);
				if(!itemFound)
				{
					childitem = _ProductionBomDAO.getchilditemfromequivalent(PLANT, PITEM, CITEM);
					//if(childitem.length()>0)CITEM=childitem;
				}
				if(!itemFound && childitem==""){
					throw new Exception(" Not a valid child  or equivalent product");
				}
				if(!itemFound)
				{
					scanitem = _ProductionBomDAO.getkittingscanitem(PLANT,PITEM,PBATCH,childitem);
					childbomqty = _ProductionBomDAO.getchildbomqty(PLANT, PITEM, childitem, "KIT");
				}
				else
				{
					scanitem = _ProductionBomDAO.getkittingscanitem(PLANT,PITEM,PBATCH,CITEM);
					childbomqty = _ProductionBomDAO.getchildbomqty(PLANT, PITEM, CITEM, "KIT");
				}
				
				if(scanitem.length()>0)
				{
					if(!scanitem.equalsIgnoreCase(CITEM))
					{
						throw new Exception(" Not a valid child  or equivalent product to scan");
					}
				}
				
				
				
			}
			
			String invitem="";
			Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,CITEM);
	        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
	        if(!childnonstocktype.equalsIgnoreCase("Y"))
			{
	        	Hashtable htinv = new Hashtable();
	        	htinv.put("PLANT", PLANT);
	        	
	        	htinv.put("ITEM", CITEM);
        		htinv.put("LOC", Loc);
	        	htinv.put("USERFLD4", CBATCH);
	        	double curqty = Double.parseDouble(QTY) * Double.valueOf(CUOMQTY);
	        	boolean invflag = invmstdao.isExisit(htinv, "QTY>="+curqty);
	        	if(!invflag){	
					//throw new Exception(" Not Enough Inventory for Child item:"+invitem+" with Batch:"+CBATCH);
	        		throw new Exception(" Not Enough Inventory for Child item:"+CITEM+" with Batch:"+CBATCH);
	        	}	
			}
	        
	        Hashtable htkitting = new Hashtable();
			htkitting.put(IDBConstants.PLANT,PLANT);
			htkitting.put(IDBConstants.EMPNO,Empid);
			htkitting.put(IDBConstants.LOC,Loc);
			htkitting.put(IDBConstants.LOCTYPEID,Loctype);
			htkitting.put(IDBConstants.REMARKS,Remarks);
			htkitting.put(IDBConstants.RSNCODE,Reasoncode);
			htkitting.put(IDBConstants.PARENTITEM,PITEM);
			htkitting.put("PARENTUOM",PUOM);
			htkitting.put("CHILDUOM",CUOM);
			htkitting.put("PBATCH",PBATCH);
			htkitting.put("PQTY",PQTY);
			htkitting.put("CBATCH",CBATCH);
			htkitting.put(IDBConstants.QTY,QTY);
			htkitting.put("CUOMQTY",CUOMQTY);
			htkitting.put("PUOMQTY",PUOMQTY);
			htkitting.put("KONO",kono); 
			htkitting.put("ORDDATE",ORDDATE); 
			if(TYPE.equalsIgnoreCase("KITDEKITWITHBOM")){
				htkitting.put("KITTYPE","WITHBOM");
				String pqty = _ProductionBomDAO.getkittingparentqtywithkono(PLANT,PITEM,PBATCH,kono);
				if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
				{
					tranqty = Double.parseDouble(PQTY)*Double.parseDouble(childbomqty);
				}
				else
				{
					tranqty = Double.parseDouble(pqty)*Double.parseDouble(childbomqty);
				}
				htkitting.put("TRANQTY",tranqty.toString());
				if(!itemFound){
					htkitting.put(IDBConstants.CHILDITEM,childitem);
					htkitting.put("INVITEM",CITEM);
					htkitting.put("EQUIITEM",CITEM);
					htkitting.put("SCANITEM",CITEM);
				}
				else{
					htkitting.put(IDBConstants.CHILDITEM,CITEM);
					htkitting.put("EQUIITEM","");
					htkitting.put("INVITEM",CITEM);
					htkitting.put("SCANITEM",CITEM);
				}
			}
			else{
				htkitting.put("KITTYPE","WITHOUTBOM");
				htkitting.put(IDBConstants.CHILDITEM,CITEM);
				htkitting.put("INVITEM",CITEM);
				htkitting.put("EQUIITEM","");
				htkitting.put("SCANITEM",CITEM);
				tranqty = Double.parseDouble(PQTY)*Double.parseDouble(QTY);
				htkitting.put("TRANQTY",tranqty.toString());
			}
			
			htkitting.put(IDBConstants.LOGIN_USER,UserId);
			
			boolean insertflag = _ProductionBomUtil.Dokitting(htkitting);
			
			
		if(insertflag){
			if(ORDDATE.equalsIgnoreCase(""))
				new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(PLANT,IConstants.KITTING,"KD",kono);
			resultJson.put("status", "100");
			request.getSession().setAttribute("RESULT","Kitting Added Successfully");
			msg = "<font class = "+IDBConstants.SUCCESS_COLOR +">Kitting Added Successfully</font>";
			}
			else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Kitting</font>";
				
			}
			
						
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
						
		}
		return msg;
	}
private JSONObject getKittingDetails(HttpServletRequest request) {
    JSONObject resultJson = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    JSONArray jsonArrayErr = new JSONArray();
    ProductionBomDAO ProdBomDao = new ProductionBomDAO();
 
    try {
    
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String PITEM = StrUtils.fString(request.getParameter("ITEM"));
           String Loc = StrUtils.fString(request.getParameter("LOC_ID"));
		   String PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
		   String TYPE = StrUtils.fString(request.getParameter("TYPE"));

           ItemMstDAO itemMstDAO = new ItemMstDAO();  
           
			ArrayList  movQryList = _ProductionBomUtil.getkittingList(PITEM,Loc,PBATCH,PLANT, " ");
        
             if (movQryList.size() > 0) {
        	       
            for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
            			int id=iCnt+1;
                        String result="",chkString="";
                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        Map lineArr = (Map) movQryList.get(iCnt);
                        JSONObject resultJsonInt = new JSONObject();
                        
                        String parentitem = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT")) ;
                        String pbatch = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_BATCH")) ;
                        String childitem = StrUtils.fString((String)lineArr.get("CHILD_PRODUCT")) ;
                        String cdesc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                        String cdetdesc = StrUtils.fString((String)lineArr.get("CDETDESC")) ;
                        String cloc = StrUtils.fString((String)lineArr.get("CHILD_PRODUCT_LOC")) ;
                        String cbatch = StrUtils.fString((String)lineArr.get("CHILD_PRODUCT_BATCH")) ;
                        String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                        String status = StrUtils.fString((String)lineArr.get("STATUS")) ;
                        String chkqty = StrUtils.fString((String)lineArr.get("CHKQTY")) ;
                        String chkbatch = StrUtils.fString((String)lineArr.get("CHKBATCH")) ;
                        String totqty = StrUtils.fString((String)lineArr.get("totalqty")) ;
                        String scanitem = StrUtils.fString((String)lineArr.get("SCANITEM")) ;
                        
                        chkString = parentitem+","+pbatch+","+childitem+","+cloc+","+qty+","+scanitem+","+cbatch;
                        
                        if(!scanitem.equalsIgnoreCase(childitem))
                        {
                        	childitem = scanitem;
                        	cdesc = new ItemMstDAO().getItemDesc(PLANT, scanitem);
                        	cdetdesc = new ItemMstDAO().getItemDetailDesc(PLANT, scanitem);
                        }
                        
                        if(TYPE.equalsIgnoreCase("KITCHECK"))
                        {
                        	result += "<tr valign=\"middle\">"  
                            		+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td align = center>"  + id + "</td>"
                            		+ "<td id='citem_"+id+"' align = center>"  + childitem + "</td>"
                            		+ "<td align = center>"  + cdesc + "</td>"
                            		+ "<td align = center>"  + cdetdesc + "</td>"
                            		+ "<td id='batch_"+id+"' align = center>"  + cbatch + "</td>"
                            		+ "<td id='qty_"+id+"' align = center>"  + qty + "</td>"
                            		+ "<td align = center>"  + chkbatch + "</td>"
                            		+ "<td id='chkqty_"+id+"' align = center>"  + chkqty + "</td>"
                            		+ "<td align = center>"  + status + "</td>"
                            		//+ "<td id='totqty_"+id+"' style='visibility:collapse;'>" + totqty + "</td>"
                            		+ "</tr>" ;
                        }
                        else
                        {
                        	result += "<tr valign=\"middle\">"  
                            		+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                            		+ "<td align = center>"  + id + "</td>"
                            		+ "<td align = center>"  + childitem + "</td>"
                            		+ "<td align = center>"  + cdesc + "</td>"
                            		+ "<td align = center>"  + cdetdesc + "</td>"
                            		+ "<td align = center>"  + cbatch + "</td>"
                            		+ "<td align = center>"  + qty + "</td>"
                            		+ "</tr>" ;
                        }
                        
                        	
                      resultJsonInt.put("KITTINGDATA", result);
                     
                        jsonArray.add(resultJsonInt);

            }
                resultJson.put("kitting", jsonArray);
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
                resultJson.put("kitting", jsonArray);
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

private String DODekitting(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException,
		Exception {
	JSONObject resultJson = new JSONObject();
	String msg = "";
	Map receiveMaterial_HM = null;
	UserTransaction ut =null;
	String PLANT = "", 
			UserId = "";
	String PITEM = "",PBATCH="", CITEM = "",CBATCH="",LOC="",QTY="",Empno="",Remarks="",result="",fieldDescData="",rflag="",PUOM="",CUOM="",KONO="",
			Reasoncode="",scanitem="",parentitem="",parentbatch="",LOC_TYPE_ID="",LOC_TYPE_ID2="";
	//Boolean allChecked = false;
		
	try {
		HttpSession session = request.getSession();
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
		UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
		rflag = StrUtils.fString(request.getParameter("RFLAG"));
		Remarks = StrUtils.fString(request.getParameter("REMARKS"));
		Empno = StrUtils.fString(request.getParameter("EMP_ID"));
		Reasoncode = StrUtils.fString(request.getParameter("REASONCODE"));
		parentitem = StrUtils.fString(request.getParameter("ITEM"));
        parentbatch = StrUtils.fString(request.getParameter("BATCH_0"));
        LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
        LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
		
		
		Boolean transactionHandler = false;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
        //ut = DbBean.getUserTranaction();
        //ut.begin();

   String[] chkditems  = request.getParameterValues("chkitem"); 
   
   process: 	
		if (chkditems != null)    {     
			for (int i = 0; i < chkditems.length; i++)       { 
				String item = chkditems[i];
				String itemArray[] = item.split(",");
				PITEM = itemArray[0];
				PBATCH = itemArray[1];
				CITEM = itemArray[2];
				LOC = itemArray[3];
				QTY = itemArray[4];
				PUOM = itemArray[6];
				CUOM = itemArray[7];
				KONO = itemArray[8];
				
				String PUOMQTY="1";
				Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
				MovHisDAO movHisDao1 = new MovHisDAO();
				ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+PUOM+"'",htTrand1);
				if(getuomqty.size()>0)
				{
				Map mapval = (Map) getuomqty.get(0);
				PUOMQTY=(String)mapval.get("UOMQTY");
				}
				
				String CUOMQTY="1";
				htTrand1 = new Hashtable<String, String>();
				movHisDao1 = new MovHisDAO();
				getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+CUOM+"'",htTrand1);
				if(getuomqty.size()>0)
				{
				Map mapval = (Map) getuomqty.get(0);
				CUOMQTY=(String)mapval.get("UOMQTY");
				}
					
				Hashtable htdekitting = new Hashtable();
				htdekitting.put(IDBConstants.PLANT,PLANT);
				htdekitting.put(IDBConstants.PARENTITEM,parentitem);
				htdekitting.put("PBATCH",parentbatch);
				htdekitting.put(IDBConstants.CHILDITEM,CITEM);
				htdekitting.put(IDBConstants.LOC,LOC);
				htdekitting.put("QTY",QTY);
				htdekitting.put(IDBConstants.LOGIN_USER,UserId);
				htdekitting.put(IDBConstants.REMARKS,Remarks);
				htdekitting.put(IDBConstants.RSNCODE,Reasoncode);
				htdekitting.put(IDBConstants.EMPNO,Empno);
				htdekitting.put("PUOMQTY",PUOMQTY);
				htdekitting.put("CUOMQTY",CUOMQTY);
				htdekitting.put("PUOM",PUOM);
				htdekitting.put("CUOM",CUOM);
				htdekitting.put("KONO",KONO);
				
				if(rflag.equalsIgnoreCase("1")){
					scanitem = itemArray[5];
					CBATCH = itemArray[6];
					htdekitting.put("CBATCH",CBATCH);
					htdekitting.put("INVITEM",scanitem);
					transactionHandler = _ProductionBomUtil.Dekitting(htdekitting);
				}
				else
				{
					scanitem = itemArray[5];
					ArrayList  movbatchList = ProdBomDao.getkitchildbatch(PLANT,parentitem,parentbatch,CITEM, " AND KONO='"+KONO+"'");
					String childbatch="",childqty="";
	            	for (int icnt=0; icnt<movbatchList.size(); icnt++)
	            	{
	        			Map lineArrbatch = (Map) movbatchList.get(icnt);
	        			String cbatch = (String) lineArrbatch.get("CBATCH");
	        			if(cbatch.contains("(")){
	        				childbatch = cbatch.substring(0,cbatch.indexOf("("));
	        				childqty = cbatch.substring(cbatch.indexOf("(")+1,cbatch.indexOf(")"));
	        			}
	        			htdekitting.put("INVITEM",scanitem);
	        			htdekitting.put("CBATCH",childbatch);
	        			htdekitting.put("QTY",childqty);
	        			transactionHandler = _ProductionBomUtil.Dekitting(htdekitting);
	        				
	        		}
					
					
				}
				
				
		if(!transactionHandler)
			break process;
		}
	}
   
   	Hashtable htinvQty = new Hashtable();
	htinvQty.put(IConstants.PLANT, PLANT);
	htinvQty.put("PARENT_PRODUCT", parentitem);
	htinvQty.put("PARENT_PRODUCT_LOC", LOC);
	htinvQty.put("KONO", KONO);	
	int bomcount = new BomDAO().getCount(htinvQty);
	
	if(transactionHandler==true)
	{
		request.getSession().setAttribute("RESULT","Dekitting successfully for Child Item");
		if(bomcount==0)
		{
			response.sendRedirect("../kittingdekitting/new?action=result&KONO="+KONO+"&ITEM="+parentitem+"&BATCH_0="+parentbatch+"&LOC_ID="+LOC+"&LOC_TYPE_ID="+LOC_TYPE_ID+"&LOC_TYPE_ID2="+LOC_TYPE_ID2+"&PUOM="+PUOM);
		}
		else if(rflag.equalsIgnoreCase("1")){
		response.sendRedirect("jsp/Kitting_Dekitting.jsp?action=result&ITEM="+parentitem+"&BATCH_0="+parentbatch);}
		else if(rflag.equalsIgnoreCase("2")){response.sendRedirect("../kittingdekitting/new?action=result&ITEM="+parentitem+"&BATCH_0="+parentbatch);}
		else if(rflag.equalsIgnoreCase("3")){response.sendRedirect("../kittingdekitting/edit?action=result&KONO="+KONO+"&ITEM="+parentitem);}
		else{response.sendRedirect("jsp/BulkKitting_DekittingwithBOM.jsp?action=result&ITEM="+parentitem+"&BATCH_0="+parentbatch+"&LOC_ID="+LOC);}
	}
	else{
		request.getSession().setAttribute("RESULTERROR","Error in Dekitting for  Child Item");
		if(rflag.equalsIgnoreCase("1")){		
		response.sendRedirect("jsp/Kitting_Dekitting.jsp?action=resulterror&ITEM="+parentitem+"&BATCH_0="+parentbatch);}
		else if(rflag.equalsIgnoreCase("2")){response.sendRedirect("../kittingdekitting/new?action=resulterror&ITEM="+parentitem+"&BATCH_0="+parentbatch);}
		else if(rflag.equalsIgnoreCase("3")){response.sendRedirect("../kittingdekitting/edit?action=resulterror&KONO="+KONO+"&ITEM="+parentitem);}
		else{response.sendRedirect("jsp/BulkKitting_DekittingwithBOM.jsp?action=resulterror&ITEM="+parentitem+"&BATCH_0="+parentbatch+"&LOC_ID="+LOC);}

		}


	} catch (Exception e) {
		
		this.mLogger.exception(this.printLog, "", e);
		request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
		if(rflag.equalsIgnoreCase("1")){
		response.sendRedirect("jsp/Kitting_Dekitting.jsp?action=catchrerror&ITEM="+parentitem);}
		else if(rflag.equalsIgnoreCase("2")){response.sendRedirect("../kittingdekitting/new?action=catchrerror&ITEM="+parentitem);}
		else if(rflag.equalsIgnoreCase("3")){response.sendRedirect("../kittingdekitting/edit?action=catchrerror&KONO="+KONO+"&ITEM="+parentitem);}
		else{response.sendRedirect("jsp/BulkKitting_DekittingwithBOM.jsp?action=catchrerror&ITEM="+parentitem+"&LOC_ID="+LOC);}
		throw e;
	}

	return msg;
}
private String addkitcheck(HttpServletRequest request,HttpServletResponse response) 
		throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String Empid="",Empname="",Emplastname="",Loc="",Locdesc="",Loctype="",Loctypedesc="",Remarks="",Reasoncode="";
		String PLANT = "",PITEM = "",PBATCH="",CITEM  = "",CBATCH="",QTY="",KITTYPE="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			
			Empid = StrUtils.fString(request.getParameter("EMP_ID"));
			Loc = StrUtils.fString(request.getParameter("LOC_ID"));
			Loctype = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
			Remarks = StrUtils.fString(request.getParameter("REMARKS"));
			Reasoncode = StrUtils.fString(request.getParameter("REASONCODE"));
			PITEM = StrUtils.fString(request.getParameter("ITEM"));
			PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
			CITEM = StrUtils.fString(request.getParameter("CITEM"));
			CBATCH = StrUtils.fString(request.getParameter("BATCH_1"));
			QTY = StrUtils.fString(request.getParameter("QTY"));
			String TYPE = StrUtils.fString(request.getParameter("TYPE"));	
			
			boolean EmpFound = true;
			boolean pitemFound = true;
			boolean citemFound = true;
			boolean flag = false;
			String childitem="";
			
			EmployeeUtil empUtil = new EmployeeUtil();
			Hashtable htEmp = new Hashtable();
			htEmp.put(IDBConstants.PLANT,PLANT);
			htEmp.put(IDBConstants.EMPNO,Empid);
			try{
				empUtil.isExistsEmployee(htEmp);
			}
			catch (Exception e) {
				throw new Exception(" Scan/Enter a Valid Employee ID");
			}
						
			Hashtable htpCondition = new Hashtable();
			htpCondition.put("PARENT_PRODUCT",PITEM);
			htpCondition.put("PLANT", PLANT);
			
			pitemFound = _ProductionBomDAO.isExistskitting(htpCondition);
			if(!pitemFound){
				throw new Exception(" Not a valid Parent product ");
			}
			
			Hashtable htCondition = new Hashtable();
			htCondition.put("PARENT_PRODUCT",PITEM);
			htCondition.put("CHILD_PRODUCT",CITEM);
			htCondition.put("PLANT", PLANT);
			
			citemFound = _ProductionBomDAO.isExistskitting(htCondition);
			if(!citemFound){
				childitem = _ProductionBomDAO.getchilditemfromequivalent(PLANT, PITEM, CITEM);
			}
			
			if(!citemFound && childitem==""){
				throw new Exception(" Not a valid child  or equivalent product");
			}
			
			boolean batchFound = true;
			Hashtable htbatch = new Hashtable();
			htbatch.put("PARENT_PRODUCT",PITEM);
			if(citemFound){
				htbatch.put("CHILD_PRODUCT",CITEM);
			}
			else{
				htbatch.put("CHILD_PRODUCT",childitem);
			}
			htbatch.put("PLANT", PLANT);
			htbatch.put("CHILD_PRODUCT_BATCH", CBATCH);
			batchFound = _ProductionBomDAO.isExistskitting(htbatch);
			if(!batchFound){
				throw new Exception(" Not a valid child product Batch");
			}
			
										
			Hashtable htkitting = new Hashtable();
			htkitting.put(IDBConstants.PLANT,PLANT);
			htkitting.put("PARENT_PRODUCT",PITEM);
			if(citemFound){
				htkitting.put("CHILD_PRODUCT",CITEM);
			}
			else{
				htkitting.put("CHILD_PRODUCT",childitem);
			}
			htkitting.put("PARENT_PRODUCT_BATCH",PBATCH);
			htkitting.put("CHILD_PRODUCT_BATCH",CBATCH);
			
			StringBuffer query = new StringBuffer("");
			query.append("isnull(CHKQTY,0) as chkqty");
			query.append(",isnull(QTY,0) as kitqty");
			
			Map mQty = _ProductionBomDAO.selectkitRow(query.toString(), htkitting,"");

			double chkqty = Double.parseDouble((String) mQty.get("chkqty"));
			double kitqty = Double.parseDouble((String) mQty.get("kitqty"));

			double tranQty = Double.parseDouble(QTY);
			double sumqty = chkqty + tranQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			String queryBom = "";
			
			//String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + "+ map.get(IConstants.QTY);
			if (kitqty == sumqty) {
				queryBom = "set CHKQTY= isNull(CHKQTY,0) + " + QTY + " ,CHKBATCH ='"+ CBATCH + "', CHKSTATUS='C' ";

			} else {
				queryBom = "set CHKQTY= isNull(CHKQTY,0) + " + QTY + " ,CHKBATCH ='"+ CBATCH + "', CHKSTATUS='O' ";
						
			}

			flag = _ProductionBomDAO.updateBOM(queryBom, htkitting, "");
			if(flag)
			{
				MovHisDAO movHisDao = new MovHisDAO();
				Boolean movhisResult = Boolean.TRUE;
				Hashtable htCylinderMOH = new Hashtable();
				htCylinderMOH.put(IDBConstants.PLANT, PLANT);
				htCylinderMOH.put("DIRTYPE", "KIT_CHECK");
				htCylinderMOH.put("QTY",QTY);
				htCylinderMOH.put("MOVTID", "");
				htCylinderMOH.put("RECID", "");
				htCylinderMOH.put(IDBConstants.CREATED_BY, UserId);
				htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htCylinderMOH.put(IConstants.ITEM, CITEM);
				htCylinderMOH.put("LOC", Loc);
				htCylinderMOH.put("BATNO", CBATCH);
			    htCylinderMOH.put("REMARKS",PITEM + " "+ Remarks + " "+ Reasoncode + " "+Empid);
				
				movhisResult = movHisDao.insertIntoMovHis(htCylinderMOH);

			}
				
			if(flag){
			resultJson.put("status", "100");
			msg = "<font class = "+IDBConstants.SUCCESS_COLOR +">Kit Check Added Successfully</font>";
			}
			else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Kit Check</font>";
				
			}
			
						
		}catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
						
		}
		return msg;
	}
private String DOKitUncheck(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException,
		Exception {
	JSONObject resultJson = new JSONObject();
	String msg = "";
	Map receiveMaterial_HM = null;
	UserTransaction ut =null;
	String PLANT = "", 
			UserId = "";
	String PITEM = "",PBATCH="", CITEM = "",CBATCH="",LOC="",QTY="",Empno="",Remarks="",result="",fieldDescData="",rflag="",Reasoncode="";
	String scanitem="";
		
	try {
		HttpSession session = request.getSession();
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
		UserId = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
		rflag = StrUtils.fString(request.getParameter("RFLAG"));
		Remarks = StrUtils.fString(request.getParameter("REMARKS"));
		Empno = StrUtils.fString(request.getParameter("EMP_ID"));
		Reasoncode = StrUtils.fString(request.getParameter("REASONCODE"));
		
		
		boolean flag = false;
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
        
       String[] chkditems  = request.getParameterValues("chkitem"); 
       
   
   process: 	
		if (chkditems != null)    {     
			for (int i = 0; i < chkditems.length; i++)       { 
				String item = chkditems[i];
				String itemArray[] = item.split(",");
				PITEM = itemArray[0];
				PBATCH = itemArray[1];
				CITEM = itemArray[2];
				LOC = itemArray[3];
				QTY = itemArray[4];
				scanitem = itemArray[5];
				CBATCH = itemArray[6];
				
				Hashtable htkitting = new Hashtable();
				htkitting.put(IDBConstants.PLANT,PLANT);
				htkitting.put("PARENT_PRODUCT",PITEM);
				htkitting.put("CHILD_PRODUCT",CITEM);
				htkitting.put("PARENT_PRODUCT_BATCH",PBATCH);
				htkitting.put("CHILD_PRODUCT_BATCH",CBATCH);
				
				String queryBom = "";
		
				queryBom = "set CHKQTY= 0 ,CHKBATCH ='', CHKSTATUS='N' ";

				flag = _ProductionBomDAO.updateBOM(queryBom, htkitting, "");
				
				if(flag)
				{
					MovHisDAO movHisDao = new MovHisDAO();
					Boolean movhisResult = Boolean.TRUE;
					Hashtable htCylinderMOH = new Hashtable();
					htCylinderMOH.put(IDBConstants.PLANT, PLANT);
					htCylinderMOH.put("DIRTYPE", "KIT_UNCHECK");
					htCylinderMOH.put("QTY",QTY);
					htCylinderMOH.put("MOVTID", "");
					htCylinderMOH.put("RECID", "");
					htCylinderMOH.put(IDBConstants.CREATED_BY, UserId);
					htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htCylinderMOH.put(IConstants.ITEM, CITEM);
					htCylinderMOH.put("LOC", LOC);
					htCylinderMOH.put("BATNO", CBATCH);
				    htCylinderMOH.put("REMARKS",PITEM + " "+ Remarks + " "+ Reasoncode + " "+Empno);
					
					movhisResult = movHisDao.insertIntoMovHis(htCylinderMOH);

				}
				if(!flag)
				{
					break process;
				}
			}
		}
       if(flag==true)
   		{
   			request.getSession().setAttribute("RESULT","Kit Uncheck successfully for Child Item");
   			response.sendRedirect("jsp/Kitcheck.jsp?action=result&ITEM="+PITEM);
   		}
   	   else
   	   {
   		   request.getSession().setAttribute("RESULTERROR","Error in kit uncheck for  Child Item");
   		   response.sendRedirect("jsp/Kitcheck.jsp?action=resulterror&ITEM="+PITEM);
   	   }
   	} catch (Exception e) {
   		
   		this.mLogger.exception(this.printLog, "", e);
   		request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
   		response.sendRedirect("jsp/Kitcheck.jsp?action=catchrerror&ITEM="+PITEM);
   		throw e;
   	}

   	return msg;
   }

private JSONObject getKitBOMDetailsForBulkKitting(HttpServletRequest request) {
    JSONObject resultJson = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    JSONArray jsonArrayErr = new JSONArray();
    //JSONArray jsonArrayMes = new JSONArray();
    ProductionBomDAO ProdBomDao = new ProductionBomDAO();
    InvMstDAO InvMstDAO = new InvMstDAO();
 
    try {
    
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String PITEM = StrUtils.fString(request.getParameter("ITEM"));
           String PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
           String PQTY = StrUtils.fString(request.getParameter("PARENTQTY"));
           String Loc = StrUtils.fString(request.getParameter("LOC"));
           ItemMstDAO itemMstDAO = new ItemMstDAO();  
           //request.getSession().setAttribute("RESULT","");
           boolean mesflag = false;
           
           
			ArrayList  movQryList = _ProductionBomUtil.getProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");
        
             if (movQryList.size() > 0) {
        	       
            for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
            			int id=iCnt+1;
                        String result="",chkString="";
                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        Map lineArr = (Map) movQryList.get(iCnt);
                        JSONObject resultJsonInt = new JSONObject();
                        String eitem="",edesc="",edetdesc="",eiteminv="",citeminv="";
                        double bomqty=0,kitqty=0,tranqty=0;
                        String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
                        String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                        String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                        String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                        bomqty = Double.parseDouble(qty);
                        citeminv = InvMstDAO.getItemTotalInvQty(PLANT, childitem, Loc);
                        
                        String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                        eitem = ProdBomDao.getEquivalentitem(PLANT,parentitem,childitem);
                        if(eitem.length()>0)
                        {
                        	edesc = itemMstDAO.getItemDesc(PLANT, eitem);
                        	edetdesc = itemMstDAO.getItemDetailDesc(PLANT,eitem);
                        	eiteminv = InvMstDAO.getItemTotalInvQty(PLANT, eitem, Loc);
                        }
                        
                       	String pbatch="",cloc="",cbatch="",cqty="",status="",scanitem="",pqty="";
                        	
                        	Hashtable htcond = new Hashtable();
                        	htcond.put("PLANT", PLANT);
                        	htcond.put("PITEM", PITEM);
                        	htcond.put("BOMTYPE", "KIT");
                        	
                        	Hashtable htkitcond = new Hashtable();
                        	htkitcond.put("PLANT", PLANT);
                        	htkitcond.put("PARENT_PRODUCT", PITEM);
                        	htkitcond.put("PARENT_PRODUCT_BATCH", PBATCH);
                        	
                        	ArrayList  KitList = bomdao.getDetails(" distinct CHILD_PRODUCT", htkitcond, "SCANSTATUS='C'");
                        	
                        	int kitcount = KitList.size();
                        	int childbomcount = _ProductionBomDAO.getCount(htcond);
                        	
                        	pbatch = ProdBomDao.getkittingparentbatch(PLANT,parentitem,childitem);
                        	pqty = ProdBomDao.getkittingparentqty(PLANT,parentitem,PBATCH);
                        	
                        	if(kitcount == childbomcount)
                        	{
                        		tranqty = bomqty * Double.parseDouble(pqty);
                        	}
                        	else if(Double.parseDouble(PQTY)>Double.parseDouble(pqty))
                        	{
                        		tranqty = bomqty * Double.parseDouble(PQTY);
                        	}
                        	else
                        	{
                        		tranqty = bomqty * Double.parseDouble(pqty);
                        	}
                        	
                        	
                        	//cbatch = ProdBomDao.getkittingchildbatch(PLANT,parentitem,childitem);
                        	ArrayList  movbatchList = ProdBomDao.getkitchildbatch(PLANT,parentitem,PBATCH,childitem, "");
                        	for (int i=0; i<movbatchList.size(); i++){
                    			Map lineArrbatch = (Map) movbatchList.get(i);
                    			if(movbatchList.size()>1){
                    				String childbatch = (String) lineArrbatch.get("CBATCH");
                    				if(cbatch.length()>0){
                    					cbatch = cbatch +","+childbatch;
                    				}
                    				else
                    				{
                    					cbatch = childbatch;
                    				}
                    				
                    			}
                    			else{
                    				cbatch = (String) lineArrbatch.get("CBATCH");
                    			}
                    		}
                        	cqty = ProdBomDao.getkittingchildqty(PLANT,parentitem,PBATCH,childitem);
                        	cloc = ProdBomDao.getkittingchildloc(PLANT,parentitem,PBATCH,childitem);
                        	scanitem = ProdBomDao.getkittingscanitem(PLANT,parentitem,PBATCH,childitem);
                        	if(cqty.length()>0){
                        		kitqty = Double.parseDouble(cqty);
                        	}
                        	if(kitqty==0){status="N";mesflag = false;}
                        	else if(tranqty>kitqty){status="O";mesflag = false;}
                        	else{status = "C";mesflag = true;}
                        	chkString = parentitem+","+pbatch+","+childitem+","+cloc+","+cqty+","+scanitem;
                        	String colorcode="";
                        	if(Double.parseDouble(citeminv) < tranqty)
                        	{
                        		colorcode = "#FF0000";
                        	}
                        	else
                        	{
                        		colorcode = "#000000";
                        	}
                        	if(eitem.length()>0)
                        	{
                        		if(Double.parseDouble(eiteminv) < tranqty)colorcode = "#FF0000";
                        		result += "<tr valign=\"middle\">"  
                                		+ "<td align = center style=\"display:none;\"><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + id + "</font></td>"
                                		+"<td align = center><font color="+colorcode+"><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkscanitem\" id=\"chkscanitem\" value=" +eitem+ "></font></td>"
                                		+"<td id='citem_"+id+"' align = center><font color="+colorcode+">"  + childitem + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + desc + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + cdetdesc + "</font></td>"
                                		+ "<td id='eitem_"+id+"' align = center><font color="+colorcode+">"  + eitem + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + edesc + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + edetdesc + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + tranqty + "</font></td>"
                                		+ "<td id='cinvqty_"+id+"' align = center><font color="+colorcode+">"  + citeminv + "</font></td>"
                                		+ "<td id='einvqty_"+id+"' align = center><font color="+colorcode+">"  + eiteminv + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + cbatch + "</font></td>"
                                		+ "<td id='kitqty_"+id+"' align = center><font color="+colorcode+">"  + cqty + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + status + "</font></td>"
                                   		+ "</tr>" ;
                        	
                        	}
                        	else
                        	{
                        		result += "<tr valign=\"middle\">"  
                                		+ "<td align = center style=\"display:none;\"><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + id + "</font></td>"
                                		+"<td align = center><font color="+colorcode+"><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkscanitem\" id=\"chkscanitem\"  disabled=\"disabled\" value=" +eitem+ " ></font></td>"
                                		+"<td id='citem_"+id+"' align = center><font color="+colorcode+">"  + childitem + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + desc + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + cdetdesc + "</font></td>"
                                		+ "<td id='eitem_"+id+"' align = center><font color="+colorcode+">"  + eitem + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + edesc + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + edetdesc + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + tranqty + "</font></td>"
                                		+ "<td id='cinvqty_"+id+"' align = center><font color="+colorcode+">"  + citeminv + "</font></td>"
                                		+ "<td id='einvqty_"+id+"' align = center><font color="+colorcode+">"  + eiteminv + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + cbatch + "</font></td>"
                                		+ "<td id='kitqty_"+id+"' align = center><font color="+colorcode+">"  + cqty + "</font></td>"
                                		+ "<td align = center><font color="+colorcode+">"  + status + "</font></td>"
                                   		+ "</tr>" ;
                        	}
                        		
                        /*	if(kitcount == childbomcount)
                        	{
                        		completemsg = "<font class = "+IDBConstants.SUCCESS_COLOR +">It is Completed Transaction</font>";
                        		
                        	}
                        	else
                        	{
                        		completemsg="";
                        	}
                        	*/
                        
                      resultJsonInt.put("KITBOMDATA", result);
                     
                        jsonArray.add(resultJsonInt);

            }
                resultJson.put("kittingbom", jsonArray);
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
                resultJson.put("kittingbom", jsonArray);
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

private String addkittingBulk(HttpServletRequest request,HttpServletResponse response) 
		throws IOException, ServletException,Exception {
		JSONObject resultJson = new JSONObject();
		String msg = "";
		String Empid="",Empname="",Emplastname="",Loc="",Locdesc="",Loctype="",Loctypedesc="",Remarks="",Reasoncode="";
		String PLANT = "",PITEM = "",PBATCH="",CITEM  = "",CBATCH="",QTY="",KITTYPE="",PQTY="";
		ArrayList alResult = new ArrayList();
		Map map = null;
		UserTransaction ut = null;
		
		try {
            
			HttpSession session = request.getSession();
                        
                     
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			String UserId = (String) session.getAttribute("LOGIN_USER");
			
			Empid = StrUtils.fString(request.getParameter("EMP_ID"));
			Loc = StrUtils.fString(request.getParameter("LOC_ID"));
			Loctype = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
			Remarks = StrUtils.fString(request.getParameter("REMARKS"));
			Reasoncode = StrUtils.fString(request.getParameter("REASONCODE"));
			PITEM = StrUtils.fString(request.getParameter("ITEM"));
			PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
			PQTY = StrUtils.fString(request.getParameter("PARENTQTY"));
						
			boolean EmpFound = false;
			boolean LocFound = true;
			boolean LoctypeFound = true;
			boolean RsncodeFound = true;
			boolean pitemFound = false;
						
			EmployeeUtil empUtil = new EmployeeUtil();
			Hashtable htEmp = new Hashtable();
			htEmp.put(IDBConstants.PLANT,PLANT);
			htEmp.put(IDBConstants.EMPNO,Empid);
			
				EmpFound =	empUtil.isExistsEmployee(htEmp);
			
			if(!EmpFound)
			{
				throw new Exception(" Scan/Enter a Valid Employee ID");
			}
					
			
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			InvMstDAO invmstdao = new InvMstDAO();
			
			pitemFound = itemMstUtil.isValidItemInItemmst(PLANT, PITEM);
			if(!pitemFound)
			{
				throw new Exception(" Scan/Enter a Valid Parent Product ID");
			}
			
			Hashtable htparent = new Hashtable();
			htparent.put(IDBConstants.PLANT,PLANT);
			htparent.put("PARENT_PRODUCT",PITEM);
			htparent.put("PARENT_PRODUCT_BATCH",PBATCH);
			htparent.put("KITTYPE","BULKWITHBOM");
			boolean isparentexist = _ProductionBomDAO.isExistskitting(htparent);
			if(isparentexist)
			{
				throw new Exception(" Already kitted parent item:"+PITEM+" with batch:"+PBATCH);
			}
						
			boolean itemFound = true;
			boolean insertflag = false;
			String childitem="",scanitem="",childbomqty="",eitem="";
			Double tranqty;
			ProductionBomDAO ProdBomDao = new ProductionBomDAO();
			
			String[] checkscancitem = request.getParameterValues("chkscanitem");
						
			ArrayList  movQryList = _ProductionBomUtil.getProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");
			 ut = DbBean.getUserTranaction();
	         ut.begin();
            if (movQryList.size() > 0) {
       	       
           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
        	   
        	   Map lineArr = (Map) movQryList.get(iCnt);
        	   
        	   childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
        	   childbomqty = StrUtils.fString((String)lineArr.get("QTY")) ;
        	   eitem = ProdBomDao.getEquivalentitem(PLANT,PITEM,childitem);
        	   tranqty = Double.parseDouble(PQTY)*Double.parseDouble(childbomqty);
        	   String chkscanitem="";
        	   if (checkscancitem != null)    { 
        	   for (int i = 0; i < checkscancitem.length; i++)       
        	   { 
   					chkscanitem = checkscancitem[i];
        	   
   					if(chkscanitem.equalsIgnoreCase(eitem))
   					{
   						scanitem = eitem;
        		   
   					}
   					else
   					{
   						scanitem = childitem;
   					}
        	   }
        	   }
        	   else
        	   {
        		   scanitem = childitem;
        	   }
               
				
			String invitem="",invqty="";
			double childinvqty=0;
			Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,scanitem);
	        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
	        if(!childnonstocktype.equalsIgnoreCase("Y"))
			{
	        	invqty = invmstdao.getItemTotalInvQty(PLANT, scanitem, Loc);
	        	
	        	childinvqty = Double.parseDouble(invqty);
	        	
	        	if(childinvqty < tranqty)
	        	{	
					throw new Exception(" Not Enough Inventory for Child or Equivalent item:"+scanitem);
	        	}	
	        	
	        	
			}
	        
	        Hashtable htkitting = new Hashtable();
			htkitting.put(IDBConstants.PLANT,PLANT);
			htkitting.put(IDBConstants.EMPNO,Empid);
			htkitting.put(IDBConstants.LOC,Loc);
			htkitting.put(IDBConstants.LOCTYPEID,Loctype);
			htkitting.put(IDBConstants.REMARKS,Remarks);
			htkitting.put(IDBConstants.RSNCODE,Reasoncode);
			htkitting.put(IDBConstants.PARENTITEM,PITEM);
			htkitting.put("PBATCH",PBATCH);
			htkitting.put("PQTY",PQTY);
			htkitting.put("KITTYPE","BULKWITHBOM");
			htkitting.put("TRANQTY",tranqty.toString());
			htkitting.put(IDBConstants.CHILDITEM,childitem);
			htkitting.put(IDBConstants.LOGIN_USER,UserId);
			if (checkscancitem != null)    {
				if(chkscanitem.equalsIgnoreCase(eitem)){
					htkitting.put("INVITEM",eitem);
					htkitting.put("EQUIITEM",eitem);
					htkitting.put("SCANITEM",eitem);
		    	
				}
				else{
					htkitting.put("INVITEM",childitem);
					htkitting.put("EQUIITEM","");
					htkitting.put("SCANITEM",childitem);
				}
			}
			else{
				htkitting.put("INVITEM",childitem);
				htkitting.put("EQUIITEM","");
				htkitting.put("SCANITEM",childitem);
			}
			// Need to do calculation based on available inventory for batches
			if(!childnonstocktype.equalsIgnoreCase("Y"))
			{
				ArrayList  invbatchList = invmstdao.getinvbatchqty(PLANT, scanitem, Loc);
				for (int batchCnt =0; batchCnt<invbatchList.size(); batchCnt++){
	        	   
	        	   Map batchArr = (Map) invbatchList.get(batchCnt);
	        	   String batch = StrUtils.fString((String)batchArr.get("batch")) ;
	        	   String childqty = StrUtils.fString((String)batchArr.get("qty")) ;
	        	   double qty = Double.parseDouble(childqty);
	        	   if(qty>=tranqty)
	        	   {
	        		   htkitting.put("CBATCH",batch);
		        	   htkitting.put(IDBConstants.QTY,tranqty.toString());
		        	   tranqty = 0.0;
	        	   }
	        	   else 
	        	   {
	        		   htkitting.put("CBATCH",batch);
		        	   htkitting.put(IDBConstants.QTY,childqty);
		        	   tranqty = tranqty-qty;
	        	   }
	        	   insertflag = _ProductionBomUtil.DokittingBulk(htkitting);
	        	   if(tranqty == 0)
	        	   {
	        		   break;  
	        	   }
				}
			}
			else{
					htkitting.put("CBATCH","NOBATCH");
	        	    htkitting.put(IDBConstants.QTY,tranqty.toString());
				insertflag = _ProductionBomUtil.DokittingBulk(htkitting);
			}
           }
       }
        
            if(insertflag)
        	{
            	DbBean.CommitTran(ut);
        		request.getSession().setAttribute("RESULT","Kitting Added Successfully");
        		
        		response.sendRedirect("jsp/BulkKitting_DekittingwithBOM.jsp?action=result&ITEM="+PITEM+"&BATCH_0="+PBATCH+"&EMP_ID="+Empid+"&LOC_ID="+Loc+"&PARENTQTY="+PQTY);
        		
        	}
        	else{
        		DbBean.RollbackTran(ut);
        		request.getSession().setAttribute("RESULTERROR","Error in Adding Kitting");
        			
        		response.sendRedirect("jsp/BulkKitting_DekittingwithBOM.jsp?action=resulterror&ITEM="+PITEM+"&BATCH_0="+PBATCH+"&EMP_ID="+Empid+"&LOC_ID="+Loc+"&PARENTQTY="+PQTY);
        		

        	} 
		}catch (Exception e) {
			    DbBean.RollbackTran(ut);
        		
        		this.mLogger.exception(this.printLog, "", e);
        		request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
        		response.sendRedirect("jsp/BulkKitting_DekittingwithBOM.jsp?action=catchrerror&ITEM="+PITEM+"&BATCH_0="+PBATCH
        				+"&PARENTQTY="+PQTY+"&EMP_ID="+Empid+"&LOC_ID="+Loc);
        		throw e;
        	}

        	return msg;
	}

private JSONObject getlabelDetails(HttpServletRequest request) {
    JSONObject resultJson = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    JSONArray jsonArrayErr = new JSONArray();
     
    try {
    
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String ITEM = StrUtils.fString(request.getParameter("ITEM"));
           //String DESC = StrUtils.fString(request.getParameter("BATCH_0"));
           //String BATCH = StrUtils.fString(request.getParameter("PARENTQTY"));
           
           ItemMstDAO itemMstDAO = new ItemMstDAO();  
         
			ArrayList  movQryList = itemMstDAO.getLabelprintDetails(PLANT, "");
        
             if (movQryList.size() > 0) {
        	       
            for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
            			int id=iCnt+1;
                        String result="",chkString="";
                        String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        Map lineArr = (Map) movQryList.get(iCnt);
                        JSONObject resultJsonInt = new JSONObject();
                        String ID = StrUtils.fString((String)lineArr.get("ID")) ;
                        String item = StrUtils.fString((String)lineArr.get("ITEM")) ;
                        String desc = StrUtils.fString((String)lineArr.get("ITEMDESC")) ;
                        String batch = StrUtils.fString((String)lineArr.get("BATCH")) ;
                        //chkString = item+","+StrUtils.replaceCharacters2Send(desc)+","+batch;
                        chkString = ID;
                        		result += "<tr valign=\"middle\">"  
                                		+ "<td align = center><INPUT Type=\"Checkbox\"  style=\"border:0;background=#dddddd\"  name=\"chkdLnNo\" id=\"chkdLnNo\" value=" +chkString+ "></td>"
                                		+ "<td align = center>"  + item + "</td>"
                                		+ "<td align = center>"  + desc + "</td>"
                                		+ "<td align = center>"  + batch + "</td>"
                                		+ "</tr>" ;
                        	
                        	
                      resultJsonInt.put("LBLDATA", result);
                     
                        jsonArray.add(resultJsonInt);

            }
                resultJson.put("labeldetails", jsonArray);
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
                resultJson.put("labeldetails", jsonArray);
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
private String addlabel(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException,
		Exception {
	JSONObject resultJson = new JSONObject();
	String msg = "";
	String PLANT = "",ITEM = "",DESC  = "",BATCH="";
	ArrayList alResult = new ArrayList();
	Map map = null;
	DateUtils dateutils = new DateUtils();
	ItemMstDAO itemMstDAO = new ItemMstDAO();  
	try {
        
		HttpSession session = request.getSession();
                    
                 
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
				.trim();
		String UserId = (String) session.getAttribute("LOGIN_USER");
		
		ITEM = StrUtils.fString(request.getParameter("ITEM"));
		DESC = StrUtils.fString(request.getParameter("DESC"));
		BATCH = StrUtils.fString(request.getParameter("BATCH"));
		
		
		Hashtable htlabel = new Hashtable();
		htlabel.put(IDBConstants.PLANT,PLANT);
		htlabel.put("ITEM",ITEM);
		htlabel.put("ITEMDESC",DESC);
		htlabel.put("BATCH",BATCH);
		htlabel.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
		htlabel.put(IDBConstants.LOGIN_USER,UserId);
		
		boolean insertflag = itemMstDAO.insertLabelDetails(htlabel);
				
		if(insertflag){
		resultJson.put("status", "100");
		msg = "<font class = "+IDBConstants.SUCCESS_COLOR +">Label Added Successfully</font>";
		}
		else{
			resultJson.put("status", "99");
			msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Adding Label</font>";
			
		}
		
					
	}catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
				
	}
	return msg;
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
	
	
	
}
