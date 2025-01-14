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

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.SemiFinishedProductDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ProductionBomUtil;
import com.track.db.util.TblControlUtil;
import com.track.tran.WmsSemiFinishedProduct;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SemiFinishedProductServlet
 */
@WebServlet("/SemiFinished/*")
public class SemiFinishedServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SemiFinishedServlet() {
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
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateSemiFinishedProduct.jsp");
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
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SemiFinishedProductDetail.jsp");
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
			SemiFinishedProductDAO processingReceiveDAO = new SemiFinishedProductDAO();
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String sCondition = "",extraCon="";
			sCondition=" and A.GRNO LIKE '%"+item+"%'";
			extraCon= "order by A.ID desc,CAST((SUBSTRING(A.ORDDATE, 7, 4) + SUBSTRING(A.ORDDATE, 4, 2) + SUBSTRING(A.ORDDATE, 1, 2)) AS date) desc ";
			String sql = "select GRNO from " + plant +"_SEMI_PROCESSHDR A WHERE A.PLANT='"+ plant+"'" + sCondition;
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
				SemiFinishedProductDAO processingReceiveDAO = new SemiFinishedProductDAO();
				try {
					if(StrUtils.fString(item).length() > 0)	ht.put("PARENT_PRODUCT", item);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("GRNO", orderno);					
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
					   sql = new StringBuffer("select GRNO,PARENT_PRODUCT,PARENT_PRODUCT_LOC,PARENT_PRODUCT_BATCH,PARENT_PRODUCT_QTY,PARENT_COST,ORDDATE");
					   sql.append(" from " + plant +"_SEMI_PROCESSHDR A WHERE A.PLANT='"+ plant+"'" + sCondition);
					   
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
		                            
		                            JSONObject resultJsonInt = new JSONObject();		                            
		                            resultJsonInt.put("ORDDATE",StrUtils.fString((String)lineArr.get("ORDDATE")));
		                            resultJsonInt.put("PARENT_PRODUCT_QTY",StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_QTY")));
		                            resultJsonInt.put("PARENT_PRODUCT_BATCH",StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_BATCH")));
		                            resultJsonInt.put("PARENT_PRODUCT_LOC",StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_LOC")));
		                            resultJsonInt.put("PARENT_PRODUCT",StrUtils.fString((String)lineArr.get("PARENT_PRODUCT")));
		                            resultJsonInt.put("GRNO",StrUtils.fString((String)lineArr.get("GRNO")));
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

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/SemiFinishedProductSummary.jsp");
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
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateSemiFinishedProduct.jsp");
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
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SemiFinishedProductSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	else if(action.equals("add")){
	String msg="";
	try {
		msg = addProcessing(request, response);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		jsonObjectResult.put("errorMsg", msg);
		jsonObjectResult.put("completedMes", "");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();
	
	 
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
    		String PLANT = "",PITEM = "",PBATCH="",CITEM  = "",CDESC="",CBATCH="",QTY="",KITTYPE="",PQTY="",PUOM="",CUOM="",kono="",ORDDATE="",PCOST="0";
    		ArrayList alResult = new ArrayList();
    		Map map = null;
    		
    		try {
                
    			HttpSession session = request.getSession();
    			InvMstDAO invMstDAO = new InvMstDAO();
    			ItemMstDAO itemMstDAO = new ItemMstDAO();
    			RecvDetDAO _RecvDetDAO = new RecvDetDAO();
    			SemiFinishedProductDAO processingReceiveDAO = new SemiFinishedProductDAO();
    			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));          
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
    			Double tranqty=0.0,curqty=0.0;
    			boolean invflag =true;
    			int HdrId = 0;
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
    			//check child invstock
    			ArrayList  chkQryList = _ProductionBomUtil.getProcessingProdBomList(PITEM,PLANT, "SEMIKITBOM");                
                if (chkQryList.size() > 0) {           	       
                for (int iCnt =0; iCnt<chkQryList.size(); iCnt++){
                	Map lineArr = (Map) chkQryList.get(iCnt);
                    CITEM = StrUtils.fString((String)lineArr.get("CITEM")) ;
                    QTY = StrUtils.fString((String)lineArr.get("QTY")) ;
                    CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                    String pitemprice = itemMstDAO.getItemCost(PLANT, PITEM);
                    Double CostPerqty=Double.parseDouble(PQTY)*Double.parseDouble(pitemprice);
                    CLoc = itemMstDAO.getItemDept(PLANT, CITEM);
                    
                    Double transprice = Double.valueOf(QTY) * CostPerqty;
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
			        Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,CITEM);
			        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
	    	        if(!childnonstocktype.equalsIgnoreCase("Y"))
	    			{
	    	        	Hashtable htcdn = new Hashtable();
	            		htcdn.put(IConstants.PLANT, PLANT);
	            		htcdn.put(IDBConstants.LOC, CLoc);
	            		boolean  result1= new LocMstDAO().isExisit(htcdn, "");
	            		if(!result1) {
//	        				throw new Exception("Child Product "+CITEM+", Location(Department) is Empty.");
	        				throw new Exception("Child Product "+CITEM+", Location/Department is Empty.");
	        			}
	            		
	    	        	/* Hashtable htinv = new Hashtable();
	    	        	htinv.put("PLANT", PLANT);    	        	
	    	        	htinv.put("ITEM", CITEM);
	            		htinv.put("LOC", CLoc);
	    	        	htinv.put("USERFLD4", CBATCH);
	    	        	invflag = invMstDAO.isExisit(htinv, "QTY>="+tranqty);
	    	        	if(!invflag){	
	    	        		throw new Exception(" Not Enough Inventory for Processing Product:"+CITEM+" with Batch:"+CBATCH);
	    	        	} */
	            		String stkqty = invMstDAO.getItemTotalQty(PLANT,CITEM, " USERFLD4='NOBATCH' AND LOC in ('"+CLoc+"','Kitchen')");
	            		if(Double.parseDouble(stkqty)<tranqty){	
	    	        		throw new Exception(" Not Enough Inventory for Processing Product:"+CITEM+" with Batch:"+CBATCH);
	    	        	}
	    			}
                }
                }
    			boolean itemFound = true;
    			String childitem="",scanitem="",childbomqty="";
    			
    			
    			String grno = new  TblControlDAO().getNextOrder(PLANT,UserId,"GRN");
    			String gino = new  TblControlDAO().getNextOrder(PLANT,UserId,"GINO");
    			Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails(PLANT,PITEM);
    	        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));
    	        if(!childnonstocktype.equalsIgnoreCase("Y"))
    			{
    	        	Hashtable htinv = new Hashtable();
    	        	htinv.put("PLANT", PLANT);    	        	
    	        	htinv.put("ITEM", PITEM);
            		htinv.put("LOC", Loc);
    	        	htinv.put("USERFLD4", PBATCH);
    	        	//No need to check now if batch is provided. Check only for NOBATCH.
    	    		if ("NOBATCH".equals(PBATCH)) {
    	    			invflag = invMstDAO.isExisit(htinv, "");				
    	    		} else 
    	        	htinv.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
    	        	curqty = Double.parseDouble(PQTY) * Double.valueOf(PUOMQTY);
    	        	invflag = invMstDAO.isExisit(htinv, "");
    	        	if(invflag){
    	        		StringBuffer sql1 = new StringBuffer(" SET ");
    	    		    sql1.append(IDBConstants.QTY + " =  QTY +'"
    	    		                    + curqty + "', ");
    	                                            
    	    			sql1.append(IDBConstants.EXPIREDATE + " = '', ");
    	    			sql1.append(IDBConstants.UPDATED_AT + " = '"
    	    					+ DateUtils.getDateTime() + "', ");
    	    			sql1.append(IDBConstants.UPDATED_BY + " = '"
    	    					+ UserId + "'");
    	    			invflag = invMstDAO.update(sql1.toString(), htinv, "");
    	        	}
    	        	else
    	        	{
    	        		htinv.put(IConstants.EXPIREDATE, "");
    	    			htinv.put(IDBConstants.QTY, String.valueOf(curqty));

    	    			htinv.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
    	    			htinv.put(IDBConstants.CREATED_BY, UserId);
    	    			invflag = invMstDAO.insertInvMst(htinv);
    	        	}						
						if(invflag) {
						Hashtable htCylinderMOH = new Hashtable();
						htCylinderMOH.clear();
						htCylinderMOH.put(IDBConstants.PLANT, PLANT);
						htCylinderMOH.put("DIRTYPE", "KITTING_IN");
						htCylinderMOH.put("QTY",curqty);
						htCylinderMOH.put("MOVTID", "IN");
						htCylinderMOH.put("RECID", "");
						htCylinderMOH.put("ORDNUM",grno);
						htCylinderMOH.put(IDBConstants.CREATED_BY, UserId);
						htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htCylinderMOH.put(IConstants.UOM, PUOM);
						htCylinderMOH.put(IConstants.ITEM, PITEM);
						htCylinderMOH.put("LOC", Loc);
						htCylinderMOH.put("BATNO", PBATCH);
					    htCylinderMOH.put("REMARKS","");
						
					    invflag = invflag && movHisDao.insertIntoMovHis(htCylinderMOH);
						}
    	        	
    			}
    	        
    	        boolean insertflag =true;
    	        if(invflag){
    	        	
    	        	//SEMIPRODUCT_PROCESSING_HDR
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
    				HdrId=processingReceiveDAO.addProcessingHdr(htkitbominsert,PLANT);
    	        	
    				Double pCostPerqty=Double.parseDouble(PCOST)/curqty;    				
    				
    				Hashtable htRecvDet = new Hashtable();
    				htRecvDet.put(IDBConstants.PLANT, PLANT);
    				htRecvDet.put("PONO","");
    				htRecvDet.put("GRNO",grno);
    				htRecvDet.put(IDBConstants.ITEM, PITEM);
    				htRecvDet.put(IDBConstants.LOC, Loc);
    				htRecvDet.put("BATCH", PBATCH);
    				htRecvDet.put("ORDQTY", String.valueOf(curqty));
    				htRecvDet.put("RECQTY", String.valueOf(curqty));
    				//htRecvDet.put("UNITCOST", PCOST);				
    				htRecvDet.put("CURRENCYID", currency);				
    				htRecvDet.put("UNITCOST", String.valueOf(pCostPerqty));				
    				htRecvDet.put("TRAN_TYPE", "KITTING");
    				htRecvDet.put(IDBConstants.RECVDATE, DateUtils.getDate());
    				htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
    				htRecvDet.put(IDBConstants.CREATED_BY, UserId);
    				invflag = _RecvDetDAO.insertRecvDet(htRecvDet);
    				
    	        ArrayList  movQryList = _ProductionBomUtil.getProcessingProdBomList(PITEM,PLANT, "SEMIKITBOM");                
               if (movQryList.size() > 0) {           	       
               for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               			int id=iCnt+1;
                           String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                           Map lineArr = (Map) movQryList.get(iCnt);
                           CITEM = StrUtils.fString((String)lineArr.get("CITEM")) ;
                           CDESC = StrUtils.fString((String)lineArr.get("CDESC")) ;
                           QTY = StrUtils.fString((String)lineArr.get("QTY")) ;
                           CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                           String CPURCHASEUOM = StrUtils.fString((String)lineArr.get("CPURCHASEUOM")) ;
                           //String pitemprice = itemMstDAO.getItemCost(PLANT, PITEM);
                           String citemprice = itemMstDAO.getItemCost(PLANT, CITEM);
                           //Double CostPerqty=Double.parseDouble(PQTY)*Double.parseDouble(pitemprice);
                           
                           CLoc = itemMstDAO.getItemDept(PLANT, CITEM);
                           //Double transprice = Double.valueOf(QTY) * CostPerqty;
                           
                        String CUOMQTY="1",CPURCHASEUOMQTY="1";
               			htTrand1 = new Hashtable<String, String>();
               			movHisDao1 = new MovHisDAO();
               			getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+CUOM+"'",htTrand1);
               			if(getuomqty.size()>0)
               			{
               			Map mapval = (Map) getuomqty.get(0);
               			CUOMQTY=(String)mapval.get("UOMQTY");
               			}
               			getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+CPURCHASEUOM+"'",htTrand1);
               			if(getuomqty.size()>0)
               			{
               			Map mapval = (Map) getuomqty.get(0);
               			CPURCHASEUOMQTY=(String)mapval.get("UOMQTY");
               			}
               	tranqty = Double.parseDouble(QTY) * Double.parseDouble(PQTY);           
    	        tranqty = tranqty * Double.valueOf(CUOMQTY);
    	        Double transprice = tranqty * Double.valueOf(citemprice);
    	        Double kitchenqty = Double.parseDouble(PQTY) * Double.valueOf(CPURCHASEUOMQTY);
    	        //Boolean ischildcal = itemMstDAO.getischildcal(PLANT, CITEM);
            	//if(!ischildcal)
            		//transprice=0.0;   			
    			Map linemap = new HashMap();
				linemap.put(IDBConstants.PLANT, PLANT);
				linemap.put("LOGIN_USER", UserId);
				linemap.put(IDBConstants.LOC, CLoc);
				linemap.put(IDBConstants.BATCH, CBATCH);
				linemap.put(IDBConstants.ITEM, CITEM);
				linemap.put(IDBConstants.ITEM_DESC, CDESC);
				linemap.put("CHILDUOM", CUOM);
				linemap.put(IConstants.QTY, StrUtils.addZeroes(tranqty, "3"));					
				linemap.put(IConstants.EXPIREDATE, "");
				linemap.put("AVERAGEUNITCOST", StrUtils.addZeroes(transprice, numberOfDecimal) );
				linemap.put("UNITCOST", citemprice);
				linemap.put(IConstants.GRNO, grno);
				linemap.put("GINO", gino);
				linemap.put(IConstants.OUT_DOLNNO, id);
				linemap.put("HDR_ID", HdrId);
				linemap.put("CURRENCY", currency);
				linemap.put("PQTY", StrUtils.addZeroes(kitchenqty, "3"));
				linemap.put("PUOMQTY", StrUtils.addZeroes(Double.valueOf(PUOMQTY), "3"));
				
				insertflag = process_Wms_CountSheet(linemap);
				
    		}
    		if(invflag && insertflag){
    			new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", grno);
    			new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", gino);
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

		WmsSemiFinishedProduct tran = new WmsSemiFinishedProduct();
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
               
               
   			ArrayList  movQryList = _ProductionBomUtil.getProcessingProdBomList(PITEM,PLANT, "SEMIKITBOM");
            
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
                            String invqty = StrUtils.fString((String)lineArr.get("INVQTY")) ;
                            String kitchenqty = StrUtils.fString((String)lineArr.get("KitchenQTY")) ;
                            String pitemprice = itemMstDAO.getItemCost(PLANT, PITEM);
                            Double CostPerqty=Double.parseDouble(PQTY)*Double.parseDouble(pitemprice);
                            bomqty = Double.parseDouble(qty);
                            
                            String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                            String citemprice = itemMstDAO.getItemCost(PLANT, childitem);
                            String cdept = itemMstDAO.getItemDept(PLANT, childitem);
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
                            	int childbomcount = ProdBomDao.getCount(htcond);
                            	
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
                            	
                            	
                            	cbatch="NOBATCH";
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
                            	//Boolean ischildcal = itemMstDAO.getischildcal(PLANT, childitem);
                            	//if(!ischildcal)
                            		//transprice=0.0;
                            	//Double transprice = Double.valueOf(qty) * CostPerqty;
                            	Double bomprice = Double.valueOf(qty) * Double.valueOf(citemprice);
                            	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
                            	if(scanitem.equalsIgnoreCase(childitem))
                            	{
                            		result += "<tr valign=\"middle\" >"  
                                		+ "<td align = left style=\"display:none\"><INPUT Type=\"Checkbox\" checked style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                		+ "<td align = left>"  + id + "</td>"
                                		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                		+ "<td align = left>"  + desc + "</td>"
                                		+ "<td id='cuom_"+id+"'align = left>"  + CUOM + "</td>"
                                		+ "<td id='cloc_"+id+"'align = left>"  + cdept + "</td>"
                                		+ "<td align = left>"  + cbatch + "</td>"
                                		+ "<td id='invqty_"+id+"' align = left>"  + invqty + "</td>"
                                		+ "<td id='kitchenqty_"+id+"' align = left>"  + kitchenqty + "</td>"
                                		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                		+ "<td id='tranqty_"+id+"' align = celeftnter>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                		+ "<td id='transprice_"+id+"' align = left>"  + StrUtils.addZeroes(transprice, numberOfDecimal) + "</td>"
                                   		+ "</tr>" ;
                            	}
                            	else if(scanitem.equalsIgnoreCase(eitem) && eitem.length()>0){
                            		result += "<tr valign=\"middle\">"  
                            				+ "<td align = left style=\"display:none\"><INPUT Type=\"Checkbox\" checked style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = left>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = left><strike>"  + childitem + "</strike></td>"
                                    		+ "<td align = left><strike>"  + desc + "</strike></td>"
                                    		+ "<td id='cuom_"+id+"'align = left>"  + CUOM + "</td>"
                                    		+ "<td id='cloc_"+id+"'align = left><strike>"  + cdept + "</strike></td>"
                                    		+ "<td align = left>"  + cbatch + "</td>"
                                    		+ "<td id='invqty_"+id+"' align = left>"  + invqty + "</td>"
                                    		+ "<td id='kitchenqty_"+id+"' align = left>"  + kitchenqty + "</td>"
                                    		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = left>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                            + "<td id='transprice_"+id+"' align = left>"  + StrUtils.addZeroes(transprice, numberOfDecimal) + "</td>"
                                       		+ "</tr>" ;
                            	}
                            	else{
                            		result += "<tr valign=\"middle\">"  
                            				+ "<td align = left style=\"display:none\"><INPUT Type=\"Checkbox\" checked style=\"border:0;background=#dddddd\"  name=\"chkitem\" id=\"chkitem\" value=" +chkString+ "></td>"
                                    		+ "<td align = left>"  + id + "</td>"
                                    		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                    		+ "<td align = left>"  + desc + "</td>"
                                    		+ "<td id='cuom_"+id+"' align = left>"  + CUOM + "</td>"
                                    		+ "<td id='cloc_"+id+"' align = left>"  + cdept + "</td>"
                                    		+ "<td align = left>"  + cbatch + "</td>"
                                    		+ "<td id='invqty_"+id+"' align = left>"  + invqty + "</td>"
                                    		+ "<td id='kitchenqty_"+id+"' align = left>"  + kitchenqty + "</td>"
                                    		+ "<td id='bomqty_"+id+"' align = left>"  + qty + "</td>"
                                    		+ "<td id='tranqty_"+id+"' align = left>"  + StrUtils.addZeroes(tranqty, "3") + "</td>"
                                            + "<td id='transprice_"+id+"' align = left>"  + StrUtils.addZeroes(transprice, numberOfDecimal) + "</td>"
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
        SemiFinishedProductDAO processingReceiveDAO = new SemiFinishedProductDAO();
        String completemsg="";
        
        try {
        	String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();                       
               String HDR_ID = StrUtils.fString(request.getParameter("HDR_ID"));
               ItemMstDAO itemMstDAO = new ItemMstDAO(); 
               PlantMstDAO plantMstDAO = new PlantMstDAO();
               request.getSession().setAttribute("RESULT","");
               boolean mesflag = false;
               
               Hashtable ht = new Hashtable();   
   			String sql = "select CHILD_PRODUCT,CHILD_PRODUCT_LOC,CHILD_PRODUCT_BATCH,QTY,CHILDUOM,CHILD_COST,ISNULL(GINO,'') GINO from " + PLANT +"_SEMI_PROCESSDET A WHERE A.PLANT='"+ PLANT+"' AND A.HDR_ID='"+HDR_ID+"'";
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
                            
                            String cdetdesc = itemMstDAO.getItemDetailDesc(PLANT, childitem);
                            String desc = itemMstDAO.getItemDesc(PLANT, childitem);
                            String citemprice = StrUtils.fString((String)lineArr.get("CHILD_COST")) ;
                            String cdept = StrUtils.fString((String)lineArr.get("CHILD_PRODUCT_LOC")) ;                            
                            String gino = StrUtils.fString((String)lineArr.get("GINO")) ;                            
                            
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
                                		+ "<td align = left>"  + gino + "</td>"
                                		+ "<td id='citem_"+id+"' align = left>"  + childitem + "</td>"
                                		+ "<td align = left>"  + desc + "</td>"
                                		+ "<td id='cuom_"+id+"'align = left>"  + CUOM + "</td>"
                                		+ "<td id='cloc_"+id+"'align = left>"  + cdept + "</td>"
                                		+ "<td align = left>"  + cbatch + "</td>"
                                		+ "<td id='tranqty_"+id+"' align = celeftnter>"  + StrUtils.addZeroes(bomqty, "3") + "</td>"
                                		+ "<td id='transprice_"+id+"' align = left>"  + StrUtils.addZeroes(bomprice, numberOfDecimal) + "</td>"
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
