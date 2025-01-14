package com.track.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.IntegrationsDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.IntegrationsUtil;
import com.track.service.ShopeeService;
import com.track.service.ShopifyService;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

@WebServlet("/integrations/*")
public class IntegrationsServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8136069338637043579L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String plant = StrUtils.fString((String)request.getSession().getAttribute("PLANT")).trim();
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null)    //  Invalid Session
		{
			request.getSession().invalidate();
		    System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}
		
		if(action.equalsIgnoreCase("shopping-cart")) {
			try {
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/shoppingCartIntegration.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(action.equalsIgnoreCase("peppolintegration")) {
			try {
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/peppolIntegration.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("ecommerce")) {
			try {
				String shop_id = StrUtils.fString(request.getParameter("shop_id")).trim();
				boolean isShopeeAuth = false;
				String msg = "";
				if(shop_id.length()>0) {
					isShopeeAuth = UpdateShopId(shop_id, plant, (String)request.getSession().getAttribute("LOGIN_USER"));
					if(isShopeeAuth)
						msg = "Shopee Authenticated Successfully";
					else
						msg = "Failed to Authenticated Shopee";
				}
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ecommerceIntegration.jsp?Msg="+msg);
				request.setAttribute("Msg", msg);
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("ShopeeAuthorization")) {
			String partnerId = "", key = "", environment = "", redirectUrl = "", requestUrl = "";
			String domain = request.getServerName();			
			try {
				IntegrationsUtil integrationsUtil = new IntegrationsUtil();
				Map<String, String> mapShopeeConfig = integrationsUtil.getShopeeConfigDetail(plant);
				partnerId = StrUtils.fString(mapShopeeConfig.get(IConstants.SHOPEE_PARTNER_ID));
				key = StrUtils.fString(mapShopeeConfig.get(IConstants.SHOPEE_API_KEY));
				environment = StrUtils.fString(mapShopeeConfig.get(IConstants.SHOPEE_ENVIRONMENT));
				if(domain.equalsIgnoreCase("localhost"))
					domain = domain+":8091";				
				redirectUrl="http"+"://"+domain+"/track/integrations/ecommerce";				
				String token= key+redirectUrl;
				String hashCode = org.apache.commons.codec.digest.DigestUtils.sha256Hex(token);				
				if(environment.equalsIgnoreCase("production"))
					requestUrl = "https://partner.shopeemobile.com/api/v1/shop/auth_partner?id="+partnerId+"&token="+hashCode+"&redirect="+redirectUrl;
				else
					requestUrl = "https://partner.test-stable.shopeemobile.com/api/v1/shop/auth_partner?id="+partnerId+"&token="+hashCode+"&redirect="+redirectUrl;
				response.sendRedirect(requestUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("shopeeResponse")) {
			
			
		}else if (action.equalsIgnoreCase("getShopeeItemsList")) {
			String pageOffset = StrUtils.fString(request.getParameter("pageOffset"));
			org.json.JSONObject json = new org.json.JSONObject();
			try {
				pageOffset = (pageOffset.equals("")) ? "0" : pageOffset;
				json = new ShopeeService().getShopeeProductsList(pageOffset,plant);
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(json.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(json.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			}
		}else if (action.equalsIgnoreCase("addShopeeProducts")) {
			String[] chkdItem= request.getParameterValues("chkdItem");
			boolean isAdded = false;
			try {
			for (int i = 0; i < chkdItem.length; i++){	
				isAdded = new ShopeeService().addShopeeProducts(chkdItem[i], plant);
        	}
	        JSONObject resultJson = new JSONObject();
	        if (isAdded) {
				resultJson.put("MESSAGE", "Products imported Successfully.");
				resultJson.put("ERROR_CODE", "100");
			}else {
				resultJson.put("MESSAGE", "Failed to import products. Please try again.");
				resultJson.put("ERROR_CODE", "98");
			}
	        response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String plant = StrUtils.fString((String)request.getSession().getAttribute("PLANT")).trim();
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null)    //  Invalid Session
		{
			request.getSession().invalidate();
		    System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}
		
		
		
		if (action.equalsIgnoreCase("addShopifyParameters")) {
			try {
				//	TODO : Transaction management if movement history is included
				Hashtable htCond=new Hashtable();
				htCond.put("PLANT",plant);
				boolean isUpdated = false;
				
				IntegrationsUtil integrationsUtil = new IntegrationsUtil();
				IntegrationsDAO IntegrationsDAO = new IntegrationsDAO();
				
				if(!(IntegrationsDAO.getShopifyConfigCount(htCond,""))){				
					htCond.put("API_KEY",request.getParameter(IDBConstants.SHOPIFY_API_KEY));
					htCond.put("API_PASSWORD",request.getParameter(IDBConstants.SHOPIFY_API_PASSWORD));
					htCond.put("DOMAIN_NAME",request.getParameter(IDBConstants.SHOPIFY_API_STORE_DOMAIN_NAME));
					htCond.put("LOCATION",request.getParameter(IDBConstants.SHOPIFY_API_LOC_ID));
					htCond.put("WEBHOOK_KEY",request.getParameter(IDBConstants.SHOPIFY_WEBHOOK_KEY));
					htCond.put("CRAT",new DateUtils().getDateTime());
					htCond.put("CRBY",request.getSession().getAttribute("LOGIN_USER"));
					isUpdated = integrationsUtil.insertShopifyConfig(htCond);
					if(isUpdated) {
					Hashtable htMovHis = new Hashtable();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.ADD_SHOPIFYCONFIG);
					htMovHis.put(IDBConstants.ITEM, "");
					htMovHis.put("MOVTID", "");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.LOC, "");
					htMovHis.put(IDBConstants.CREATED_BY, request.getSession().getAttribute("LOGIN_USER"));
					htMovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()) );
					htMovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					htMovHis.put(IDBConstants.REMARKS, "");
					
					isUpdated = new MovHisDAO().insertIntoMovHis(htMovHis);
					}
				}else {
					StringBuffer updateQuery=new StringBuffer("set ");
					updateQuery.append(IDBConstants.SHOPIFY_API_KEY +" = '"+ request.getParameter(IDBConstants.SHOPIFY_API_KEY) + "'");
					updateQuery.append(","+IDBConstants.SHOPIFY_API_PASSWORD +" = '"+ request.getParameter(IDBConstants.SHOPIFY_API_PASSWORD) + "'");
					updateQuery.append(","+IDBConstants.SHOPIFY_API_STORE_DOMAIN_NAME +" = '"+ request.getParameter(IDBConstants.SHOPIFY_API_STORE_DOMAIN_NAME) + "'");
					updateQuery.append(","+IDBConstants.SHOPIFY_API_LOC_ID +" = '"+ request.getParameter(IDBConstants.SHOPIFY_API_LOC_ID) + "'");
					updateQuery.append(","+IDBConstants.SHOPIFY_WEBHOOK_KEY +" = '"+ request.getParameter(IDBConstants.SHOPIFY_WEBHOOK_KEY) + "'");
					isUpdated = IntegrationsDAO.updateShopifyConfig(updateQuery.toString(), htCond, "");
					if(isUpdated) {
						Hashtable htMovHis = new Hashtable();
						htMovHis.put(IDBConstants.PLANT, plant);
						htMovHis.put("DIRTYPE", TransactionConstants.UPD_SHOPIFYCONFIG);
						htMovHis.put(IDBConstants.ITEM, "");
						htMovHis.put("MOVTID", "");
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.LOC, "");
						htMovHis.put(IDBConstants.CREATED_BY, request.getSession().getAttribute("LOGIN_USER"));
						htMovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()) );
						htMovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						htMovHis.put(IDBConstants.REMARKS, "");
						
						isUpdated = new MovHisDAO().insertIntoMovHis(htMovHis);
					}
				}
				
				//	TODO : What to be added in movement history
				JSONObject resultJson = new JSONObject();
				if (isUpdated) {
					resultJson.put("MESSAGE", "Shopify Parameters Stored Successfully.");
					resultJson.put("ERROR_CODE", "100");
				}else {
					resultJson.put("MESSAGE", "Could not store Shopify Parameters. Please try again.");
					resultJson.put("ERROR_CODE", "98");
				}
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(resultJson.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("addShopifyProducts")) {
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			try {
			ServletInputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	        System.out.println(stringBuilder.toString());
	        org.json.JSONObject jsonData = new org.json.JSONObject(stringBuilder.toString());
	        boolean isAdded = new ShopifyService().addShopifyProducts(jsonData);
	        JSONObject resultJson = new JSONObject();
	        if (isAdded) {
				resultJson.put("MESSAGE", "Products imported Successfully.");
				resultJson.put("ERROR_CODE", "100");
			}else {
				resultJson.put("MESSAGE", "Failed to import products. Please try again.");
				resultJson.put("ERROR_CODE", "98");
			}
	        response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (action.equalsIgnoreCase("addShopeeParameters")) {
			try {
				//	TODO : Transaction management if movement history is included
				Hashtable htCond=new Hashtable();
				htCond.put("PLANT",plant);
				boolean isUpdated = false;
				
				IntegrationsUtil integrationsUtil = new IntegrationsUtil();
				IntegrationsDAO IntegrationsDAO = new IntegrationsDAO();
				
				if(!(IntegrationsDAO.getConfigCount(htCond, IDBConstants.SHOPEE_CONFIG_TABLE, ""))){				
					htCond.put("API_KEY",request.getParameter(IDBConstants.SHOPEE_API_KEY));
					htCond.put("PARTNER_ID",request.getParameter(IDBConstants.SHOPEE_PARTNER_ID));
					htCond.put("ENVIRONMENT",request.getParameter(IDBConstants.SHOPEE_ENVIRONMENT));
					htCond.put("CRAT",new DateUtils().getDateTime());
					htCond.put("CRBY",request.getSession().getAttribute("LOGIN_USER"));
					isUpdated = integrationsUtil.insertApiConfig(htCond, IDBConstants.SHOPEE_CONFIG_TABLE);
					if(isUpdated) {
					Hashtable htMovHis = new Hashtable();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.ADD_SHOPEECONFIG);
					htMovHis.put(IDBConstants.ITEM, "");
					htMovHis.put("MOVTID", "");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.LOC, "");
					htMovHis.put(IDBConstants.CREATED_BY, request.getSession().getAttribute("LOGIN_USER"));
					htMovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()) );
					htMovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					htMovHis.put(IDBConstants.REMARKS, "");
					
					isUpdated = new MovHisDAO().insertIntoMovHis(htMovHis);
					}
				}else {
					StringBuffer updateQuery=new StringBuffer("set ");
					updateQuery.append(IDBConstants.SHOPEE_API_KEY +" = '"+ request.getParameter(IDBConstants.SHOPEE_API_KEY) + "'");
					updateQuery.append(","+IDBConstants.SHOPEE_PARTNER_ID +" = '"+ request.getParameter(IDBConstants.SHOPEE_PARTNER_ID) + "'");
					updateQuery.append(","+IDBConstants.SHOPEE_ENVIRONMENT +" = '"+ request.getParameter(IDBConstants.SHOPEE_ENVIRONMENT) + "'");
					isUpdated = IntegrationsDAO.updateConfig(updateQuery.toString(), IDBConstants.SHOPEE_CONFIG_TABLE, htCond, "");
					if(isUpdated) {
						Hashtable htMovHis = new Hashtable();
						htMovHis.put(IDBConstants.PLANT, plant);
						htMovHis.put("DIRTYPE", TransactionConstants.UPD_SHOPEECONFIG);
						htMovHis.put(IDBConstants.ITEM, "");
						htMovHis.put("MOVTID", "");
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.LOC, "");
						htMovHis.put(IDBConstants.CREATED_BY, request.getSession().getAttribute("LOGIN_USER"));
						htMovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()) );
						htMovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						htMovHis.put(IDBConstants.REMARKS, "");
						
						isUpdated = new MovHisDAO().insertIntoMovHis(htMovHis);
					}
				}
				
				//	TODO : What to be added in movement history
				JSONObject resultJson = new JSONObject();
				if (isUpdated) {
					resultJson.put("MESSAGE", "Shopee Parameters Stored Successfully.");
					resultJson.put("ERROR_CODE", "100");
				}else {
					resultJson.put("MESSAGE", "Could not store Shopee Parameters. Please try again.");
					resultJson.put("ERROR_CODE", "98");
				}
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(resultJson.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean UpdateShopId(String shop_id, String plant, String loginUser) {
		IntegrationsDAO IntegrationsDAO = new IntegrationsDAO();
		Hashtable htCond=new Hashtable();
		htCond.put("PLANT",plant);
		StringBuffer updateQuery=new StringBuffer("set ");
		updateQuery.append(IDBConstants.SHOPEE_SHOP_ID +" = '"+ shop_id + "'");
		boolean isUpdated = false;
		try {
			isUpdated = IntegrationsDAO.updateConfig(updateQuery.toString(), IDBConstants.SHOPEE_CONFIG_TABLE, htCond, "");
			if(isUpdated) {
				Hashtable htMovHis = new Hashtable();
				htMovHis.put(IDBConstants.PLANT, plant);
				htMovHis.put("DIRTYPE", TransactionConstants.UPD_SHOPEECONFIG);
				htMovHis.put(IDBConstants.ITEM, "");
				htMovHis.put("MOVTID", "");
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.LOC, "");
				htMovHis.put(IDBConstants.CREATED_BY, loginUser);
				htMovHis.put(IDBConstants.TRAN_DATE, new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()) );
				htMovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				htMovHis.put(IDBConstants.REMARKS, "");
				
				isUpdated = new MovHisDAO().insertIntoMovHis(htMovHis);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isUpdated;
	}
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		
	}
}
