package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
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

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ProcessingReceiveDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.UomDAO;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ProductionBomUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.tran.WmsProcessingReceive;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ProcessingReceiveServlet
 */
@WebServlet("/ProcessingReceive/*")
public class ProcessingReceiveServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessingReceiveServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		if(action.equalsIgnoreCase("new")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String actionvl   = StrUtils.fString(request.getParameter("action"));
				String pitem  = StrUtils.fString(request.getParameter("ITEM"));
				String pbatch  = StrUtils.fString(request.getParameter("BATCH_0"));
				request.setAttribute("Msg", msg);
				request.setAttribute("action", actionvl);
				request.setAttribute("ITEM", pitem);
				request.setAttribute("BATCH_0", pbatch);
				request.setAttribute("ISEDIT", "");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateProcessingReceive.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else if(action.equalsIgnoreCase("detail")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String GRNO  = StrUtils.fString(request.getParameter("GRNO"));
			request.setAttribute("Msg", msg);
			request.setAttribute("GRNO", GRNO);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProcessingReceiveDetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}		else if(action.equalsIgnoreCase("grno")) {

		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (ajax) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			ArrayList movQryList  = new ArrayList();
			Hashtable ht = new Hashtable();
			ProcessingReceiveDAO processingReceiveDAO = new ProcessingReceiveDAO();
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String sCondition = "",extraCon="";
			sCondition=" and A.GRNO LIKE '%"+item+"%'";
			extraCon= "order by A.ID desc,CAST((SUBSTRING(A.ORDDATE, 7, 4) + SUBSTRING(A.ORDDATE, 4, 2) + SUBSTRING(A.ORDDATE, 1, 2)) AS date) desc ";
			String sql = "select GRNO from " + plant +"_PROCESSING_RECEIVEHDR A WHERE A.PLANT='"+ plant+"'" + sCondition;
			try {
			movQryList = processingReceiveDAO.selectForReport(sql,ht,extraCon);
			if (movQryList.size() > 0) {
	            int Index = 0;
	                                  
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                            Map lineArr = (Map) movQryList.get(iCnt);  
	                            JSONObject resultJsonInt = new JSONObject();
	                            resultJsonInt.put("GRNO",StrUtils.fString((String)lineArr.get("GRNO")));
	                            jsonArray.add(resultJsonInt);
	                }
			}
			resultJson.put("items", jsonArray);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();	
		}
}		else if(action.equalsIgnoreCase("gino")) {
	
	boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	if (ajax) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		ArrayList movQryList  = new ArrayList();
		Hashtable ht = new Hashtable();
		ProcessingReceiveDAO processingReceiveDAO = new ProcessingReceiveDAO();
		String item = StrUtils.fString(request.getParameter("ITEM"));
		String sCondition = "",extraCon="";
		sCondition=" and A.GINO LIKE '%"+item+"%'";
		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.ORDDATE, 7, 4) + SUBSTRING(A.ORDDATE, 4, 2) + SUBSTRING(A.ORDDATE, 1, 2)) AS date) desc ";
		String sql = "select ISNULL(GINO,'')GINO from " + plant +"_PROCESSING_RECEIVEHDR A WHERE A.PLANT='"+ plant+"'" + sCondition;
		try {
			movQryList = processingReceiveDAO.selectForReport(sql,ht,extraCon);
			if (movQryList.size() > 0) {
				int Index = 0;
				
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					Map lineArr = (Map) movQryList.get(iCnt);  
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("GINO",StrUtils.fString((String)lineArr.get("GINO")));
					jsonArray.add(resultJsonInt);
				}
			}
			resultJson.put("items", jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();	
	}
		}		
		else if(action.equalsIgnoreCase("summary")) {

			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if (ajax) {
				String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String item = StrUtils.fString(request.getParameter("ITEM"));
				String orderno = StrUtils.fString(request.getParameter("ORDERNO"));
				String fdate = "", tdate = "";
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				ArrayList movQryList  = new ArrayList();
				StringBuffer sql;
				ProcessingReceiveDAO processingReceiveDAO = new ProcessingReceiveDAO();
				MovHisDAO movHisDao1 = new MovHisDAO();    			
				
				try {
					if(StrUtils.fString(item).length() > 0)	ht.put("PARENT_PRODUCT", item);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("GINO", orderno);					
					if(StrUtils.fString(plant).length() > 0)	ht.put("PLANT", plant);
					
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
					Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
					String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
					String sCondition = "",dtCondStr="",extraCon="";
					dtCondStr ="and ISNULL(A.ORDDATE,'')<>'' AND CAST((SUBSTRING(A.ORDDATE, 7, 4) + '-' + SUBSTRING(a.ORDDATE, 4, 2) + '-' + SUBSTRING(a.ORDDATE, 1, 2)) AS date)";
		    		extraCon= "order by A.ID desc,CAST((SUBSTRING(A.ORDDATE, 7, 4) + SUBSTRING(A.ORDDATE, 4, 2) + SUBSTRING(A.ORDDATE, 1, 2)) AS date) desc ";    			        
			       
					   if (fdate.length() > 0) {
		              	sCondition = sCondition + dtCondStr + "  >= '" 
		  						+ fdate
		  						+ "'  ";
		  				if (tdate.length() > 0) {
		  					sCondition = sCondition +dtCondStr+ " <= '" 
		  					+ tdate
		  					+ "'  ";
		  				}
		  			  } else {
		  				if (tdate.length() > 0) {
		  					sCondition = sCondition +dtCondStr+ " <= '" 
		  					+ tdate
		  					+ "'  ";
		  				}
		  		     	}
					   sql = new StringBuffer("select ISNULL(GINO,'') GINO,GRNO,PARENT_PRODUCT,PARENT_PRODUCT_LOC,PARENT_PRODUCT_BATCH,PARENT_PRODUCT_QTY,PARENT_COST,ORDDATE,PARENTUOM,ISNULL(WASTAGE,0) WASTAGE");
					   sql.append(" from " + plant +"_PROCESSING_RECEIVEHDR A WHERE A.PLANT='"+ plant+"'" + sCondition);
					   
					movQryList = processingReceiveDAO.selectForReport(sql.toString(),ht,extraCon);	
		            
		            if (movQryList.size() > 0) {
		            int Index = 0;
		                                  
		                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
		                            Map lineArr = (Map) movQryList.get(iCnt);                            
		                            
		                            String balCostValue = (String)lineArr.get("PARENT_COST");
		                            double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
		                            if(balCostVal==0f){
		                            	balCostValue="0.00000";
		                            }else{
		                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            }
		                            balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
		                            String PUOMQTY ="1";
		                            ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM where UOM='"+(String)lineArr.get("PARENTUOM")+"'",htTrand1);
		                			if(getuomqty.size()>0)
		                			{
		                			Map mapval = (Map) getuomqty.get(0);
		                			PUOMQTY=(String)mapval.get("UOMQTY");
		                			}
		                			Double curqty = Double.parseDouble((String)lineArr.get("PARENT_PRODUCT_QTY")) / Double.valueOf(PUOMQTY);
		                			
		                            JSONObject resultJsonInt = new JSONObject();		                            
		                            resultJsonInt.put("ORDDATE",StrUtils.fString((String)lineArr.get("ORDDATE")));
		                            resultJsonInt.put("PARENT_PRODUCT_QTY", StrUtils.addZeroes(curqty,"3"));
		                            resultJsonInt.put("PARENT_PRODUCT_BATCH",StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_BATCH")));
		                            resultJsonInt.put("PARENT_PRODUCT_LOC",StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_LOC")));
		                            resultJsonInt.put("PARENT_PRODUCT",StrUtils.fString((String)lineArr.get("PARENT_PRODUCT")));
		                            resultJsonInt.put("GRNO",StrUtils.fString((String)lineArr.get("GRNO")));
		                            resultJsonInt.put("GINO",StrUtils.fString((String)lineArr.get("GINO")));
		                            resultJsonInt.put("WASTAGE",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("WASTAGE")),"3"));
		                       	 	resultJsonInt.put("PARENT_COST",balCostValue);
		                            jsonArray.add(resultJsonInt);
		                }
		            }

					resultJson.put("items", jsonArray);
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(500);
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} else {
				String msg = StrUtils.fString(request.getParameter("msg"));

				request.setAttribute("Msg", msg);

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProcessingReceiveSummary.jsp");
				rd.forward(request, response);
			}

			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		JSONObject jsonObjectResult = new JSONObject();
if(action.equalsIgnoreCase("new")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String actionvl   = StrUtils.fString(request.getParameter("action"));
				String pitem  = StrUtils.fString(request.getParameter("ITEM"));
				String pbatch  = StrUtils.fString(request.getParameter("BATCH_0"));
				request.setAttribute("Msg", msg);
				request.setAttribute("action", actionvl);
				request.setAttribute("ITEM", pitem);
				request.setAttribute("BATCH_0", pbatch);
				request.setAttribute("ISEDIT", "");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateProcessingReceive.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	else if(action.equalsIgnoreCase("summary")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String pitem  = StrUtils.fString(request.getParameter("ITEM"));
			request.setAttribute("Msg", msg);
			request.setAttribute("ITEM", pitem);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProcessingReceiveSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	else if(action.equals("add")){
	String msg="";
	UserTransaction ut = null;
	List lnno = new ArrayList(), item = new ArrayList(), uom = new ArrayList(),
			qty = new ArrayList(), transqty = new ArrayList(),loc = new ArrayList(),batch = new ArrayList(), amount = new ArrayList(), unitpricerd = new ArrayList();
	String PITEM="",ISProcessing="0",PLOC="",PBATCH="",PCOST="",AVGCOST="",PARENTQTY="",PARENTUOM="",PARENTQPUOM="1",WASTAGE="0",WASTAGECOST="0";
	int itemCount  = 0, qtyCount  = 0, tranqtyCount  = 0, amountCount  = 0, lnnoCount  = 0, unitpricerdCount=0,
			locCount  = 0,batchCount  = 0,uomCount  = 0;
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
					if (fileItem.getFieldName().equalsIgnoreCase("ITEM")) {
						PITEM = StrUtils.fString(fileItem.getString()).trim();
					}
		
					if (fileItem.getFieldName().equalsIgnoreCase("Processing")) {
						ISProcessing = StrUtils.fString(fileItem.getString()).trim();
					}
				
					if (fileItem.getFieldName().equalsIgnoreCase("LOC_ID")) {
						PLOC = StrUtils.fString(fileItem.getString()).trim();
					}
				
					if (fileItem.getFieldName().equalsIgnoreCase("BATCH_0")) {
						PBATCH = StrUtils.fString(fileItem.getString()).trim();
					}
				
					if (fileItem.getFieldName().equalsIgnoreCase("COST")) {
						PCOST = StrUtils.fString(fileItem.getString()).trim();
					}

					if (fileItem.getFieldName().equalsIgnoreCase("CALCOST")) {
						AVGCOST = StrUtils.fString(fileItem.getString()).trim();
					}
				
					if (fileItem.getFieldName().equalsIgnoreCase("PARENTQTY")) {
						PARENTQTY = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("PARENTUOM")) {
						PARENTUOM = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("PARENTQPUOM")) {
						PARENTQPUOM = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("WASTAGE")) {
						WASTAGE = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("WASTAGECOST")) {
						WASTAGECOST = StrUtils.fString(fileItem.getString()).trim();
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
					if (fileItem.getFieldName().equalsIgnoreCase("loc")) {
						
						loc.add(locCount, StrUtils.fString(fileItem.getString()).trim());
						locCount++;
					}
				}
				
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("UOM")) {
						
						uom.add(uomCount, StrUtils.fString(fileItem.getString()).trim());
						uomCount++;
					}
				}
				
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("batch")) {
						
						batch.add(batchCount, StrUtils.fString(fileItem.getString()).trim());
						batchCount++;
					}
				}
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("QTY")) {
						qty.add(qtyCount, StrUtils.fString(fileItem.getString()).trim());
						qtyCount++;
					}
				}
				
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("TRANQTY")) {
						transqty.add(tranqtyCount,StrUtils.fString(fileItem.getString()).trim());
						tranqtyCount++;
					}
				}
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("amount")) {
						amount.add(amountCount, StrUtils.fString(fileItem.getString()).trim());
						amountCount++;
					}
				}				
				if (fileItem.isFormField()) {
					if (fileItem.getFieldName().equalsIgnoreCase("unitpricerd")) {
						unitpricerd.add(unitpricerdCount, StrUtils.fString(fileItem.getString()).trim());
						unitpricerdCount++;
					}
				}				
			}
		}
		Double tranqty=0.0,curqty=0.0;
		boolean invflag =true;
		int HdrId = 0;
		String grno = new  TblControlDAO().getNextOrder(PLANT,UserId,"GRN");
		String gino = new  TblControlDAO().getNextOrder(PLANT,UserId,"GINO");
		Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,PITEM);
        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
        String pitemDesc = StrUtils.fString((String) mPrddetchild.get("ITEMDESC"));
        if(!childnonstocktype.equalsIgnoreCase("Y"))
		{
        	Hashtable htinv = new Hashtable();
        	htinv.put("PLANT", PLANT);    	        	
        	htinv.put("ITEM", PITEM);
    		htinv.put("LOC", PLOC);
        	htinv.put("USERFLD4", PBATCH);
        	curqty = Double.parseDouble(PARENTQTY) * Double.valueOf(PARENTQPUOM);
        	invflag = invMstDAO.isExisit(htinv, "QTY>="+curqty);
        	if(!invflag){	
        		throw new Exception(" Not Enough Inventory for Processing Product:"+PITEM+" with Batch:"+PBATCH);
        	}
        	else
        	{
        		Hashtable htInsertInvParentBom = new Hashtable();
				htInsertInvParentBom.put(IConstants.PLANT, PLANT);
				htInsertInvParentBom.put("ITEM", PITEM);
				htInsertInvParentBom.put("LOC", PLOC);
				htInsertInvParentBom.put("USERFLD4", PBATCH);
				
				ArrayList alStock = invMstDAO.selectInvMst("ID, CRAT, QTY", htInsertInvParentBom, "");
				if (!alStock.isEmpty()) {
					double quantityToAdjust = curqty;
					Iterator iterStock = alStock.iterator();
					while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
						Map mapIterStock = (Map)iterStock.next();
						double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
						double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
						StringBuffer sql1 = new StringBuffer(" SET ");
						sql1.append(IDBConstants.QTY + " = QTY -'" + adjustedQuantity + "'");

						Hashtable htInvMstReduce = new Hashtable();
						htInvMstReduce.clear();
						htInvMstReduce.put(IDBConstants.PLANT, PLANT);
						htInvMstReduce.put(IDBConstants.ITEM, PITEM);
						htInvMstReduce.put(IDBConstants.LOC, PLOC);
						htInvMstReduce.put(IDBConstants.USERFLD4, PBATCH);
						htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
						htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));

						invflag = invMstDAO.update(sql1.toString(), htInvMstReduce, "");
						if (!invflag) {
							throw new Exception("Could not update");
						}
						quantityToAdjust -= adjustedQuantity;
					}
				}
				
				if(invflag) {
				Hashtable htCylinderMOH = new Hashtable();
				htCylinderMOH.clear();
				htCylinderMOH.put(IDBConstants.PLANT, PLANT);
				htCylinderMOH.put("DIRTYPE", "DE-KITTING_OUT");
				htCylinderMOH.put("QTY",StrUtils.addZeroes(Double.parseDouble(PARENTQTY), "3"));
				htCylinderMOH.put(IDBConstants.UOM, PARENTUOM);
				htCylinderMOH.put("MOVTID", "OUT");
				htCylinderMOH.put("RECID", "");
				htCylinderMOH.put("ORDNUM",gino);
				htCylinderMOH.put(IDBConstants.CREATED_BY, UserId);
				htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htCylinderMOH.put(IConstants.ITEM, PITEM);
				htCylinderMOH.put("LOC", PLOC);
				htCylinderMOH.put("BATNO", PBATCH);
			    htCylinderMOH.put("REMARKS","");
				
			    invflag = invflag && movHisDao.insertIntoMovHis(htCylinderMOH);
			    
			  //SHIP_HIS
				Hashtable htIssueDet = new Hashtable();
				htIssueDet.put(IDBConstants.PLANT,PLANT);
				htIssueDet.put(IDBConstants.DODET_DONUM, "");
				htIssueDet.put(IDBConstants.CUSTOMER_NAME, "");
				htIssueDet.put(IDBConstants.ITEM, PITEM);
				htIssueDet.put(IDBConstants.ITEM_DESC, pitemDesc);
				htIssueDet.put("BATCH", PBATCH);
				htIssueDet.put(IDBConstants.LOC, PLOC);
				htIssueDet.put("LOC1", PLOC);
				htIssueDet.put("DOLNO", "1");
				htIssueDet.put("ORDQTY", String.valueOf(PARENTQTY));
				htIssueDet.put("PICKQTY", String.valueOf(PARENTQTY));
				htIssueDet.put("REVERSEQTY", "0");
				htIssueDet.put("STATUS", "C");
				htIssueDet.put(IDBConstants.CREATED_BY, UserId);
				htIssueDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htIssueDet.put(IDBConstants.ISSUEDATE, DateUtils.getDate());
				htIssueDet.put(IDBConstants.EMPNO, "");
				htIssueDet.put(IDBConstants.RSNCODE, "");
				htIssueDet.put(IDBConstants.REFNO, "");
				htIssueDet.put("REMARK", "");
				htIssueDet.put("INVOICENO", gino);
				htIssueDet.put("TRAN_TYPE", "DE-KITTING");
				htIssueDet.put(IConstants.CURRENCYID, currency);
				htIssueDet.put(IDBConstants.PRICE, AVGCOST);
				//htIssueDet.put(IDBConstants.INVID, String.valueOf(mapIterStock.get(IDBConstants.INVID)));
				invflag =new ShipHisDAO().insertShipHis(htIssueDet);
			    
				}
        	}
		}
		
        boolean insertflag =true;
        if(invflag){
        	
        	//PROCESSING_RECEIVE_HDR
        	Hashtable htkitbominsert = new Hashtable();
			htkitbominsert.put(IDBConstants.PLANT,PLANT);
			htkitbominsert.put(IDBConstants.PARENT_PRODUCT,PITEM);
			htkitbominsert.put(IDBConstants.PARENT_LOC,PLOC);
			htkitbominsert.put(IDBConstants.PARENT_BATCH,PBATCH);
			htkitbominsert.put("PARENT_PRODUCT_QTY",String.valueOf(curqty));
			htkitbominsert.put("PARENTUOM",PARENTUOM);
			htkitbominsert.put("PARENT_COST",PCOST);
			htkitbominsert.put("GRNO",grno);
			htkitbominsert.put("GINO",gino);
			htkitbominsert.put("ISSAUTOPROCESSING",ISProcessing);
			htkitbominsert.put("WASTAGE",WASTAGE);
			htkitbominsert.put("WASTAGECOST",WASTAGECOST);
			htkitbominsert.put("ORDDATE",DateUtils.getDate());
			htkitbominsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
			htkitbominsert.put(IDBConstants.CREATED_BY,UserId);
			HdrId=processingReceiveDAO.addProcessingReceiveHdr(htkitbominsert,PLANT);
			
			//WASTAGE
			if(Double.valueOf(WASTAGE)>0) {
			Hashtable htWastageinsert = new Hashtable();
			htWastageinsert.put(IDBConstants.PLANT,PLANT);
			htWastageinsert.put("ITEM",PITEM);
			htWastageinsert.put("ITEMDESC",pitemDesc);
			htWastageinsert.put("WASTAGE_QTY",WASTAGE);
			htWastageinsert.put("WASTAGE_UOM",PARENTUOM);
			htWastageinsert.put("WASTAGE_COST",WASTAGECOST);
			htWastageinsert.put("TRANSACTION_ID",String.valueOf(HdrId));
			htWastageinsert.put("TRANSACTION_TYPE","PROCESSING");
			htWastageinsert.put("WASTAGE_DATE",DateUtils.getDate());
			htWastageinsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
			htWastageinsert.put(IDBConstants.CREATED_BY,UserId);
			Boolean isaddWastage =processingReceiveDAO.addWastage(htWastageinsert,PLANT);
			}
			
			String CITEM  = "",CBATCH="",QTY  = "",TQTY  = "",CUOM="",CLoc  = "";
			for (int i = 0; i < lnnoCount; i++) {
				int index = Integer.parseInt((String) lnno.get(i)) - 1;
                   CITEM = StrUtils.fString((String) item.get(index)) ;
                   CBATCH = StrUtils.fString((String) batch.get(index)) ;
                   QTY = StrUtils.fString((String) qty.get(index)) ;
                   TQTY = StrUtils.fString((String) transqty.get(index)) ;
                   CUOM = StrUtils.fString((String) uom.get(index)) ;
                   CLoc = StrUtils.fString((String) loc.get(index)) ;
                   //CLoc = itemMstDAO.getItemDept(PLANT, CITEM);
                   //Double transprice = Double.valueOf(StrUtils.fString((String) amount.get(index)));
                   Double transprice = Double.valueOf(StrUtils.fString((String) unitpricerd.get(index)));
                   //String citemprice = itemMstDAO.getItemCost(PLANT, CITEM);
                   String CUOMQTY="1";
                   Hashtable<String, String>htTrand1 = new Hashtable<String, String>();
       			movHisDao = new MovHisDAO();
       			ArrayList getuomqty = movHisDao.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+CUOM+"'",htTrand1);
       			if(getuomqty.size()>0)
       			{
       			Map mapval = (Map) getuomqty.get(0);
       			CUOMQTY=(String)mapval.get("UOMQTY");
       			}
       	tranqty = Double.parseDouble(TQTY);           
        tranqty = tranqty * Double.valueOf(CUOMQTY);

        Map linemap = new HashMap();
		linemap.put(IDBConstants.PLANT, PLANT);
		linemap.put("LOGIN_USER", UserId);
		linemap.put(IDBConstants.LOC, CLoc);
		linemap.put(IDBConstants.BATCH, CBATCH);
		linemap.put(IDBConstants.ITEM, CITEM);
		linemap.put("CHILDUOM", CUOM);
		linemap.put(IConstants.QTY, StrUtils.addZeroes(tranqty, "3"));					
		linemap.put("CQTY", StrUtils.addZeroes(Double.parseDouble(TQTY), "3"));					
		linemap.put(IConstants.EXPIREDATE, "");
		linemap.put("AVERAGEUNITCOST", String.valueOf(transprice*Double.parseDouble(CUOMQTY)) );
		linemap.put(IConstants.GRNO, grno);
		linemap.put("HDR_ID", HdrId);
		linemap.put("NumberOfDecimal", numberOfDecimal);
		linemap.put("CURRENCY", currency);
		
		insertflag = process_Wms_CountSheet(linemap);
		
	}
	if(invflag && insertflag){
		new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", grno);
		new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", gino);
		DbBean.CommitTran(ut);
		String message = PITEM+" Product Processed Successfully";
		if (ajax) {
			jsonObjectResult.put("MESSAGE", message);
			jsonObjectResult.put("ERROR_CODE", "100");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else {
			response.sendRedirect("../ProcessingReceive/summary?msg=" + message);
		}
		
		}
		else{			
			DbBean.RollbackTran(ut);
			String message = "Error in Processing Product "+PITEM;
			if (ajax) {
				jsonObjectResult.put("MESSAGE", message);
				jsonObjectResult.put("ERROR_CODE", "99");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				response.sendRedirect("../ProcessingReceive/new?msg=" + message);
			}
		}
        
    }else{
    	DbBean.RollbackTran(ut);
		String message = "Unable To Process Product "+PITEM;
		if (ajax) {
			jsonObjectResult.put("MESSAGE", message);
			jsonObjectResult.put("ERROR_CODE", "99");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else {
			response.sendRedirect("../ProcessingReceive/new?msg=" + message);
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
			response.sendRedirect("../ProcessingReceive/new?msg=" + ThrowableUtil.getMessage(e));
		}
	}
	/*
	 * jsonObjectResult.put("errorMsg", msg); jsonObjectResult.put("completedMes",
	 * ""); response.setContentType("application/json");
	 * response.setCharacterEncoding("UTF-8");
	 * response.getWriter().write(jsonObjectResult.toString());
	 * response.getWriter().flush(); response.getWriter().close();
	 */
	
	 
 }
else if(action.equals("VIEW_PROCESSING_DETAILS_TO_RECEIVE")){
	jsonObjectResult = this.getProcessingBOMDetailsToReceive(request);	
	String PITEM = StrUtils.fString(request.getParameter("ITEM"));
	if(PITEM.length()>0)
	{	
		jsonObjectResult.put("completedMes", "");
	}
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	response.getWriter().write(jsonObjectResult.toString());
	response.getWriter().flush();
	response.getWriter().close();
	 
 }
else if(action.equals("VIEW_PROCESSED_DETAIL")){
	jsonObjectResult = this.getProcessingReceive(request);	
	String HDR_ID = StrUtils.fString(request.getParameter("HDR_ID"));
	if(HDR_ID.equalsIgnoreCase("0"))
	{	
		jsonObjectResult.put("completedMes", "");
	}
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	response.getWriter().write(jsonObjectResult.toString());
	response.getWriter().flush();
	response.getWriter().close();
	 
 }
	}
	
    private String addProcessing(HttpServletRequest request,HttpServletResponse response) 
    		throws IOException, ServletException,Exception {
    		JSONObject resultJson = new JSONObject();
    		String msg = "";
    		String Empid="",Empname="",Emplastname="",Loc="",Locdesc="",Loctype="",Loctypedesc="",Remarks="",Reasoncode="",CCOST="0",CLoc="";
    		String PLANT = "",PITEM = "",PBATCH="",CITEM  = "",CBATCH="",QTY="",KITTYPE="",PQTY="",PUOM="",CUOM="",kono="",ORDDATE="",PCOST="0";
    		ArrayList alResult = new ArrayList();
    		Map map = null;
    		
    		try {
                
    			HttpSession session = request.getSession();
    			InvMstDAO invMstDAO = new InvMstDAO();
    			ItemMstDAO itemMstDAO = new ItemMstDAO();
    			ProcessingReceiveDAO processingReceiveDAO = new ProcessingReceiveDAO();
                         
    			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
    					.trim();
    			String UserId = (String) session.getAttribute("LOGIN_USER");
    			
    			Loc = StrUtils.fString(request.getParameter("LOC_ID"));
    			Loctype = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
    			PITEM = StrUtils.fString(request.getParameter("ITEM"));
    			PBATCH = StrUtils.fString(request.getParameter("BATCH_0"));
    			PQTY = StrUtils.fString(request.getParameter("PARENTQTY"));
    			PUOM = StrUtils.fString(request.getParameter("PUOM"));
    			PCOST = StrUtils.fString(request.getParameter("PCOST"));
    			ORDDATE = StrUtils.fString(request.getParameter("ORDDATE"));
    			CBATCH="NOBATCH";
    			String PUOMQTY="1";
    			String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(PLANT);
    			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
    			MovHisDAO movHisDao1 = new MovHisDAO();
    			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+PUOM+"'",htTrand1);
    			if(getuomqty.size()>0)
    			{
    			Map mapval = (Map) getuomqty.get(0);
    			PUOMQTY=(String)mapval.get("UOMQTY");
    			}
    			    			
    			boolean pitemFound = false;
    					
    			ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
    			ItemMstUtil itemMstUtil = new ItemMstUtil();
    			MovHisDAO movHisDao = new MovHisDAO();
    			
    			pitemFound = itemMstUtil.isValidItemInItemmst(PLANT, PITEM);
    			if(!pitemFound) {
    				throw new Exception(" Scan/Enter a Valid Parent Product ID");
    			}
    			String pitemprice = itemMstDAO.getItemCost(PLANT, PITEM);
				Double CostPerqty=Double.parseDouble(PQTY)*Double.parseDouble(pitemprice);
    			ArrayList  chkQryList = _ProductionBomUtil.getProcessingProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");                
                if (chkQryList.size() > 0) {           	       
                for (int iCnt =0; iCnt<chkQryList.size(); iCnt++){
                	Map lineArr = (Map) chkQryList.get(iCnt);
                    CITEM = StrUtils.fString((String)lineArr.get("CITEM")) ;
                    CLoc = itemMstDAO.getItemDept(PLANT, CITEM);
                    QTY = StrUtils.fString((String)lineArr.get("QTY")) ;
                    String citemprice = itemMstDAO.getItemCost(PLANT, CITEM);
                    Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,CITEM);
			        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
	    	        if(!childnonstocktype.equalsIgnoreCase("Y"))
	    			{
                    Hashtable htcdn = new Hashtable();
            		htcdn.put(IConstants.PLANT, PLANT);
            		htcdn.put(IDBConstants.LOC, CLoc);
            		boolean  result1= new LocMstDAO().isExisit(htcdn, "");
            		if(!result1) {
        				throw new Exception("Child Product "+CITEM+", Location(Department) is Empty.");
        			}
            		
            		Boolean ischildcal = itemMstDAO.getischildcal(PLANT, CITEM);
                	if(ischildcal)
                	{
                		String CUOMQTY="1";
                		getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+CUOM+"'",htTrand1);
               			if(getuomqty.size()>0)
               			{
               			Map mapval = (Map) getuomqty.get(0);
               			CUOMQTY=(String)mapval.get("UOMQTY");
               			}
            		Hashtable htinv = new Hashtable();
    	        	htinv.put("PLANT", PLANT);    	        	
    	        	htinv.put("ITEM", CITEM);
            		htinv.put("LOC", CLoc);
    	        	htinv.put("USERFLD4", CBATCH);
    	        	Double curqty = Double.parseDouble(QTY) * Double.valueOf(CUOMQTY);
    	        	Boolean invflag = invMstDAO.isExisit(htinv, "QTY>="+curqty);
    	        	if(!invflag){	
    	        		throw new Exception(" Not Enough Inventory for Processing Child Product:"+CITEM+" with Batch:"+CBATCH);
    	        	}
                	}
            		
	    			} else {
	    				Double nocprice = Double.valueOf(QTY) * Double.valueOf(citemprice);
  	    	        	CostPerqty=CostPerqty+nocprice;
	    			}
                }
                }
    			boolean itemFound = true;
    			String childitem="",scanitem="",childbomqty="";
    			Double tranqty=0.0,curqty=0.0;
    			boolean invflag =true;
    			int HdrId = 0;
    			String grno = new  TblControlDAO().getNextOrder(PLANT,UserId,"GRN");
    			Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,PITEM);
    	        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
    	        if(!childnonstocktype.equalsIgnoreCase("Y"))
    			{
    	        	Hashtable htinv = new Hashtable();
    	        	htinv.put("PLANT", PLANT);    	        	
    	        	htinv.put("ITEM", PITEM);
            		htinv.put("LOC", Loc);
    	        	htinv.put("USERFLD4", PBATCH);
    	        	curqty = Double.parseDouble(PQTY) * Double.valueOf(PUOMQTY);
    	        	invflag = invMstDAO.isExisit(htinv, "QTY>="+curqty);
    	        	if(!invflag){	
    	        		throw new Exception(" Not Enough Inventory for Processing Product:"+PITEM+" with Batch:"+PBATCH);
    	        	}
    	        	else
    	        	{
    	        		Hashtable htInsertInvParentBom = new Hashtable();
						htInsertInvParentBom.put(IConstants.PLANT, PLANT);
						htInsertInvParentBom.put("ITEM", PITEM);
						htInsertInvParentBom.put("LOC", Loc);
						htInsertInvParentBom.put("USERFLD4", PBATCH);
						
						ArrayList alStock = invMstDAO.selectInvMst("ID, CRAT, QTY", htInsertInvParentBom, "");
						if (!alStock.isEmpty()) {
							double quantityToAdjust = curqty;
							Iterator iterStock = alStock.iterator();
							while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
								Map mapIterStock = (Map)iterStock.next();
								double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
								double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
								StringBuffer sql1 = new StringBuffer(" SET ");
								sql1.append(IDBConstants.QTY + " = QTY -'" + adjustedQuantity + "'");

								Hashtable htInvMstReduce = new Hashtable();
								htInvMstReduce.clear();
								htInvMstReduce.put(IDBConstants.PLANT, PLANT);
								htInvMstReduce.put(IDBConstants.ITEM, PITEM);
								htInvMstReduce.put(IDBConstants.LOC, Loc);
								htInvMstReduce.put(IDBConstants.USERFLD4, PBATCH);
								htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
								htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));

								invflag = invMstDAO.update(sql1.toString(), htInvMstReduce, "");
								if (!invflag) {
									throw new Exception("Could not update");
								}
								quantityToAdjust -= adjustedQuantity;
							}
						}
						
						if(invflag) {
						Hashtable htCylinderMOH = new Hashtable();
						htCylinderMOH.clear();
						htCylinderMOH.put(IDBConstants.PLANT, PLANT);
						htCylinderMOH.put("DIRTYPE", "DE-KITTING_OUT");
						htCylinderMOH.put("QTY",StrUtils.addZeroes(Double.parseDouble(PQTY), "3"));
						htCylinderMOH.put("MOVTID", "OUT");
						htCylinderMOH.put("RECID", "");
						htCylinderMOH.put("ORDNUM",grno);
						htCylinderMOH.put(IDBConstants.CREATED_BY, UserId);
						htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htCylinderMOH.put(IConstants.ITEM, PITEM);
						htCylinderMOH.put("LOC", Loc);
						htCylinderMOH.put("BATNO", PBATCH);
					    htCylinderMOH.put("REMARKS","");
						
					    invflag = invflag && movHisDao.insertIntoMovHis(htCylinderMOH);
						}
    	        	}
    			}
    	        
    	        boolean insertflag =true;
    	        if(invflag){
    	        	
    	        	//PROCESSING_RECEIVE_HDR
    	        	Hashtable htkitbominsert = new Hashtable();
    				htkitbominsert.put(IDBConstants.PLANT,PLANT);
    				htkitbominsert.put(IDBConstants.PARENT_PRODUCT,PITEM);
    				htkitbominsert.put(IDBConstants.PARENT_LOC,Loc);
    				htkitbominsert.put(IDBConstants.PARENT_BATCH,PBATCH);
    				htkitbominsert.put("PARENT_PRODUCT_QTY",String.valueOf(curqty));
    				htkitbominsert.put("PARENTUOM",PUOM);
    				htkitbominsert.put("PARENT_COST",PCOST);
    				htkitbominsert.put("GRNO",grno);
    				htkitbominsert.put("ORDDATE",DateUtils.getDate());
    				htkitbominsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
    				htkitbominsert.put(IDBConstants.CREATED_BY,UserId);
    				HdrId=processingReceiveDAO.addProcessingReceiveHdr(htkitbominsert,PLANT);    				
    					
    	        ArrayList  movQryList = _ProductionBomUtil.getProcessingProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");                
               if (movQryList.size() > 0) {            	               	   
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               			int id=iCnt+1;
                           String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                           Map lineArr = (Map) movQryList.get(iCnt);
                           CITEM = StrUtils.fString((String)lineArr.get("CITEM")) ;
                           QTY = StrUtils.fString((String)lineArr.get("QTY")) ;
                           CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                           CLoc = itemMstDAO.getItemDept(PLANT, CITEM);
                           Double transprice = Double.valueOf(QTY) * CostPerqty;
                           String citemprice = itemMstDAO.getItemCost(PLANT, CITEM);
                           String CUOMQTY="1";
               			htTrand1 = new Hashtable<String, String>();
               			movHisDao1 = new MovHisDAO();
               			getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+CUOM+"'",htTrand1);
               			if(getuomqty.size()>0)
               			{
               			Map mapval = (Map) getuomqty.get(0);
               			CUOMQTY=(String)mapval.get("UOMQTY");
               			}
               	tranqty = Double.parseDouble(QTY) * Double.parseDouble(PQTY);           
    	        tranqty = tranqty * Double.valueOf(CUOMQTY);
    	        Double bomprice = Double.valueOf(QTY) * Double.valueOf(citemprice);
    	        Boolean ischildcal = itemMstDAO.getischildcal(PLANT, CITEM);
            	if(ischildcal)
            		transprice=bomprice;   			    			
    			Map linemap = new HashMap();
				linemap.put(IDBConstants.PLANT, PLANT);
				linemap.put("LOGIN_USER", UserId);
				linemap.put(IDBConstants.LOC, CLoc);
				linemap.put(IDBConstants.BATCH, CBATCH);
				linemap.put(IDBConstants.ITEM, CITEM);
				linemap.put("CHILDUOM", CUOM);
				linemap.put(IConstants.QTY, StrUtils.addZeroes(tranqty, "3"));					
				linemap.put(IConstants.EXPIREDATE, "");
				linemap.put("AVERAGEUNITCOST", StrUtils.addZeroes(transprice, numberOfDecimal) );
				linemap.put(IConstants.GRNO, grno);
				linemap.put("HDR_ID", HdrId);
				
				insertflag = process_Wms_CountSheet(linemap);
				
    		}
    		if(invflag && insertflag){
    			new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", grno);
    			resultJson.put("status", "100");
    			request.getSession().setAttribute("RESULT",PITEM+" Product Processed Successfully");
    			msg = "<font class = "+IDBConstants.SUCCESS_COLOR +">"+PITEM+" Product Processed Successfully</font>";
    			}
    			else{
    				resultJson.put("status", "99");
    				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Processing Product "+PITEM+"</font>";
    				
    			}
                
            }
    	    }else{
				resultJson.put("status", "99");
				msg = "<font class = "+IDBConstants.FAILED_COLOR +">Error in Processing Product "+PITEM+"</font>";
				
			}
    						
    		}catch (Exception e) {
    			msg = "<font class='mainred'>"+e.getMessage()+"</font>";
    						
    		}
    		return msg;
    	}
    
    private boolean process_Wms_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_Receive()");
		boolean flag = false;

		WmsProcessingReceive tran = new WmsProcessingReceive();
		flag = tran.processWmsReceive(map);
		MLogger.log(-1, this.getClass() + " process_Wms_Receive()");
		return flag;
	}
    
    private JSONObject getProcessingBOMDetailsToReceive(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        //JSONArray jsonArrayMes = new JSONArray();
        ProductionBomDAO ProdBomDao = new ProductionBomDAO();
        ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
        BomDAO bomdao = new BomDAO();
        String completemsg="";
        
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
               String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);//Check Company Industry
               String pitemprice = itemMstDAO.getItemCost(PLANT, PITEM);
               Double CostPerqty=Double.parseDouble(PQTY)*Double.parseDouble(pitemprice);
   				ArrayList  movQryList = _ProductionBomUtil.getProcessingProdBomList(PITEM,PLANT, " AND BOMTYPE='KIT'");
   				Double sumchildqty=0.0,sumuomchildqty=0.0;
                if (movQryList.size() > 0) {
					
					  for (int iCnt =0; iCnt<movQryList.size(); iCnt++) { 
						  Map lineArr = (Map) movQryList.get(iCnt); 
					  String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ; 
					  String citemprice = itemMstDAO.getItemCost(PLANT, childitem); 
					  String qty = StrUtils.fString((String)lineArr.get("QTY")) ; 
					  String CQPUOM = StrUtils.fString((String)lineArr.get("CQPUOM")) ;
					  Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,childitem); 
					  String  childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
					  if(childnonstocktype.equalsIgnoreCase("Y")) { 
						  Double nocprice = Double.valueOf(qty) * Double.valueOf(citemprice);
						  CostPerqty=CostPerqty+(Double.parseDouble(PQTY)*nocprice);
					  
					  	}
					  else {
						  sumchildqty=sumchildqty+Double.parseDouble(qty);
					  	  sumuomchildqty=sumuomchildqty+(Double.parseDouble(qty)*Double.parseDouble(CQPUOM));
					  }
					  }
					 
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
                            String PQPUOM = StrUtils.fString((String)lineArr.get("PQPUOM")) ;
                            String CQPUOM = StrUtils.fString((String)lineArr.get("CQPUOM")) ;
                            
                            bomqty = Double.parseDouble(qty);
                            
                            String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                            String citemprice = itemMstDAO.getItemCost(PLANT, childitem);
                            String cdept = itemMstDAO.getItemDept(PLANT, childitem);
                            Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,childitem);
        			        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));        	    	                                   
                            
                            if(TYPE.equalsIgnoreCase("KITDEKITWITHBOM")){
                            	String cbatch="";
                            	cbatch="NOBATCH";
                            	Double nocprice =0.0;
                            	Double UOMPerqty=CostPerqty / Double.valueOf(sumuomchildqty);
                            	//Double transprice = Double.valueOf(qty) * UOMPerqty;
                            	Double transprice = UOMPerqty;
                            	tranqty=Double.valueOf(qty) * Double.valueOf(PQTY);
                            	if(childnonstocktype.equalsIgnoreCase("Y"))
                            	{
                            		nocprice = Double.valueOf(citemprice);
                            		transprice=0.0;
                            	}
                            	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
								
									 result += "<tr>";
	                            		result += "<td class=\"item-img text-center\">";
	                            		result += "<img alt=\"\" src=\"../jsp/dist/img/NO_IMG.png\" style=\"width: 100%;\"><input type=\"hidden\" name=\"basecost\" value=\""+ StrUtils.addZeroes(nocprice, numberOfDecimal) +"\">";
	                            		result += "<input type=\"hidden\" name=\"lnno\" value=\""+id+"\">";
	                            		result += "<input type=\"hidden\" name=\"itemdesc\" value=\""+ desc +"\">";
	                            		result += "<input type=\"hidden\" name=\"uomqty\" value=\""+ CQPUOM +"\">";
	                            		result += "<input type=\"hidden\" name=\"isnonstock\" value=\""+ childnonstocktype +"\">";
	                            		result += "<input type=\"hidden\" name=\"unitpricerd\" value=\""+ transprice+"\">";
	                            		result += "</td>";
	                            		result += "<td class=\"bill-item\">";
	                            		result += "<input type=\"text\" name=\"citem\" class=\"form-control itemSearch\" style=\"width:87%\" placeholder=\"Type or click to select an item.\" value=\""+childitem+"\" READONLY>";
	                            		//result += "<button type=\"button\" style=\"position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;\" onclick=\"changeitem(this)\"><i class=\"glyphicon glyphicon-menu-down\" style=\"font-size: 8px;\"></i></button>";
	                            		result += "</td>";
	                            		result += "<td class=\"bill-item\">";
	                            		result += "<input type=\"text\" name=\"UOM\" class=\"form-control uomSearch\" placeholder=\"UOM\" value=\""+CUOM+"\" READONLY>";
	                            		result += "</td>";
	                            		result += "<td class=\"bill-item\">";
	                            		result += "<input type=\"text\" name=\"loc\" class=\"form-control locSearch\" placeholder=\"Location\" value=\""+cdept+"\" READONLY>";
	                            		result += "</td>";
	                            		if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
	                            			result += "<td class=\"bill-item\">";
		                            		result += "<input type=\"text\" name=\"batch\" class=\"form-control batchSearch\" placeholder=\"Batch\" value=\""+cbatch+"\" READONLY>";
		                            		result += "</td>";
	                            		} else {
	                            		result += "<td class=\"bill-item\"><div class=\"input-group\">";
	                            		result += "<input type=\"text\" name=\"batch\" value=\""+cbatch+"\" class=\"form-control batchSearch\" placeholder=\"Batch\">";
	                            		result += "<span class=\"input-group-addon\" onclick=\"javascript:generateBatch(this);return false;\" id=\"actionBatch\" name=\"actionBatch\">";
	                            		result += "<a href=\"#\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Generate\">";
	                            		result += "<i class=\"glyphicon glyphicon-edit\"></i></a></span>";
	                            		result += "</div></td>";
                            			}
	                            		result += "<td class=\"item-qty text-right\"><input type=\"text\" name=\"QTY\" class=\"form-control text-right\" data-rl=\"0.000\" data-msq=\"0.000\" data-soh=\"0.000\" data-eq=\"0.000\" data-aq=\"0.000\" value=\""+qty+"\" onchange=\"calculateAmount(this)\" READONLY></td>";
	                            		result += "<td>";
	                            		result += "<input type=\"text\" name=\"TRANQTY\" class=\"form-control text-right\" value=\""+StrUtils.addZeroes(tranqty,"3")+"\" READONLY>";
	                            		result += "</td>";
	                            		result += "<td class=\"item-amount text-right\">";
	                            		//result += "<span class=\"glyphicon glyphicon-remove-circle bill-action\" aria-hidden=\"true\" onclick=\"javascript:removeRow(this);return false;\"></span>";
	                            		result += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value=\""+StrUtils.addZeroes(transprice*Double.parseDouble(CQPUOM), numberOfDecimal)+"\" readonly=\"readonly\" style=\"display:inline-block;\">";
	                            		result += "</td>";
	                            		result += "<td class=\"item-amount text-right grey-bg\" style=\"position:relative;\">";
	                            		result += "<input name=\"totamount\" type=\"text\" class=\"form-control text-right\" value=\""+StrUtils.addZeroes(((transprice*Double.parseDouble(CQPUOM))*tranqty), numberOfDecimal)+"\" readonly=\"readonly\" style=\"display:inline-block;\">";
	                            		result += "</td>";
	                            		result += "</tr>";                            	
                            	
                            		completemsg = "<font class = "+IDBConstants.SUCCESS_COLOR +">It is Completed Transaction</font>";
                            	
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
    
    private JSONObject getProcessingReceive(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ProcessingReceiveDAO processingReceiveDAO = new ProcessingReceiveDAO();
        String completemsg="";
        
        try {
        	String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();                       
               String HDR_ID = StrUtils.fString(request.getParameter("HDR_ID"));
               ItemMstDAO itemMstDAO = new ItemMstDAO(); 
               PlantMstDAO plantMstDAO = new PlantMstDAO();
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               
               Hashtable ht = new Hashtable();   
   			String sql = "select CHILD_PRODUCT,CHILD_PRODUCT_LOC,CHILD_PRODUCT_BATCH,QTY,CHILDUOM,CHILD_COST,ISNULL((SELECT TOP 1 GRNO FROM " + PLANT +"_PROCESSING_RECEIVEHDR H WHERE H.ID=A.HDR_ID),'') GRNO from " + PLANT +"_PROCESSING_RECEIVEDET A WHERE A.PLANT='"+ PLANT+"' AND A.HDR_ID='"+HDR_ID+"'";
   			ArrayList  movQryList = processingReceiveDAO.selectForReport(sql,ht,"");
            
                 if (movQryList.size() > 0) {
            	       
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                			int id=iCnt+1;
                            String result="",chkString="";
                            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            Map lineArr = (Map) movQryList.get(iCnt);
                            JSONObject resultJsonInt = new JSONObject();
                            String eitem="",edesc="",edetdesc="";
                            double bomqty=0,kitqty=0,tranqty=0;
                            String childitem = StrUtils.fString((String)lineArr.get("CHILD_PRODUCT")) ;
                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            String CUOM = StrUtils.fString((String)lineArr.get("CHILDUOM")) ;
                            bomqty = Double.parseDouble(qty);
                            
                            String uomQty="";
        					List uomQry = new UomDAO().getUomDetails(StrUtils.fString((String)lineArr.get("CHILDUOM")), PLANT, "");
        					if(uomQry.size()>0) {
        						Map mp = (Map) uomQry.get(0);
        						uomQty = (String)mp.get("QPUOM");
        					}else {
        						uomQty = "0";
        					}
                            
                            String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                            String desc = itemMstDAO.getItemDesc(PLANT, childitem);
                            String citemprice = StrUtils.fString((String)lineArr.get("CHILD_COST")) ;
                            String cdept = StrUtils.fString((String)lineArr.get("CHILD_PRODUCT_LOC")) ;                            
                            String grno = StrUtils.fString((String)lineArr.get("GRNO")) ;                            
                            
                            	String pbatch="",cloc="",cbatch="",cqty="",status="",scanitem="",pqty="";                            	
                            	
                            	cbatch= StrUtils.fString((String)lineArr.get("CHILD_PRODUCT_BATCH")) ;                            	
                            	if(cqty.length()>0){
                            		kitqty = Double.parseDouble(cqty);
                            	}
                            	if(kitqty==0){status="N";mesflag = false;}
                            	else if(tranqty>kitqty){status="O";mesflag = false;}
                            	else{status = "C";mesflag = true;}
                            	Double bomprice = Double.valueOf(citemprice);
                            	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
                            	
                            		result += "<tr valign=\"middle\" >"  
                                		+ "<td align = left>"  + id + "</td>"
                                		+ "<td align = left>"  + grno + "</td>"
                                		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                		+ "<td align = left>"  + desc + "</td>"
                                		+ "<td id='cloc_"+id+"'align = left>"  + cdept + "</td>"
                                		+ "<td id='cuom_"+id+"'align = left>"  + CUOM + "</td>"
                                		+ "<td align = left>"  + cbatch + "</td>"
                                		+ "<td id='tranqty_"+id+"' align = celeftnter>"  + StrUtils.addZeroes(bomqty/Double.parseDouble(uomQty), "3") + "</td>"
                                		+ "<td id='transprice_"+id+"' align = left>"  + StrUtils.addZeroes((bomprice/Double.parseDouble(uomQty)), numberOfDecimal) + "</td>"
                                		+ "<td id='tottransprice_"+id+"' align = left>"  + StrUtils.addZeroes(((bomprice/Double.parseDouble(uomQty))*bomqty), numberOfDecimal) + "</td>"
                                   		+ "</tr>" ;
                            	
                            		completemsg="";
                            	
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
