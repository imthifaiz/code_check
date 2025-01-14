package com.track.peppol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.HrClaimPojo;
import com.track.db.object.PoDet;
import com.track.db.object.PoDetApproval;
import com.track.db.object.PoHdr;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.FieldCopier;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.peppol.model.Invoiceevents;
import com.track.peppol.model.PeppolCreditors;
import com.track.peppol.model.PeppolInvoiceLine;
import com.track.peppol.model.PeppolSalesInvoice;
import com.track.peppol.model.PeppolSalesOrder;
import com.track.peppol.model.PeppolSalesOrderLineItem;
import com.track.peppol.model.PeppolSalesOrderOutput;
import com.track.peppol.model.Peppoldebitor;
import com.track.peppol.model.Peppolinvicesendresponse;
import com.track.peppol.model.PurchaseInvoice;
import com.track.peppol.model.PurchaseInvoiceLine;
import com.track.peppol.model.PurchaseInvoiceResponsemessage;
import com.track.peppol.model.PurchaseInvoicehdr;
import com.track.peppol.model.Purchaseinvoiceresponse;
import com.track.peppol.model.SalesOrderErrorResponse;
import com.track.peppol.model.peppolxml;
import com.track.service.DoDetService;
import com.track.service.DoHDRService;
import com.track.serviceImplementation.DoDetServiceImpl;
import com.track.serviceImplementation.DoHdrServiceImpl;
import com.track.tables.DOHDR;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.Numbers;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;

@WebServlet("/peppolapi/*")
public class PeppolAPIServlet  extends HttpServlet implements IMLogger {
	
	private static final long serialVersionUID = -8136069338637043579L;
	public static String peppolauthurl = "";
	public static String peppolbasicurl = "";
	public static String peppolclientkey= "";
	public static String peppolclientsecret = "";
	public static String peppolclientid = "";
	public static String PEPPOL_PROPS_FILE = MLoggerConstant.PROPS_FOLDER + "/track/config/peppol.properties";

	static {
		loadPeppolConstant();
	}

	public static void loadPeppolConstant() {
		Properties pppr;
		InputStream ppip;
		try {
			ppip = new FileInputStream(new File(PEPPOL_PROPS_FILE));
			pppr = new Properties();
			pppr.load(ppip);
			//peppolukey = pppr.getProperty("PEPPOL_UPI_KEY");
			//inboundurl = pppr.getProperty("PEPPOL_INBOUND_URL");
			//outboundurl = pppr.getProperty("PEPPOL_OUTBOUND_URL");
			peppolauthurl = pppr.getProperty("PEPPOL_AUTH_URL");
			peppolbasicurl = pppr.getProperty("PEPPOL_BASIC_URL");
			peppolclientkey= pppr.getProperty("CLIENT_KEY");
			peppolclientsecret =  pppr.getProperty("CLIENT_SECRET");
			peppolclientid =  pppr.getProperty("CLIENT_ID");
		} catch (Exception e) {
			System.out.println("STATIC VALUES error");
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		List<String> attachmentLocations = new ArrayList<>();
		String attachmentLocation = null;
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		PlantMstUtil plantMstUtil = new PlantMstUtil();
		
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null) // Invalid Session
		{
			request.getSession().invalidate();
			System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}
		
		if (action.equalsIgnoreCase("peppolauth")) {
			String jsonString = "";
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			try {
				peppolAuthPojo = authenticate();
			}catch (Exception e) {
				e.printStackTrace();
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(peppolAuthPojo.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("salesordersummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PeppolSalesSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("invoicesummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Peppolinvoicesummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("purchaseinvoicesummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PurchaseInvoiceSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("salesdetail")) {
			String dono = StrUtils.fString(request.getParameter("dono"));
			String imagePath = "", numberOfDecimal = "";
			DoHDRService doHDRService = new DoHdrServiceImpl();
			DoDetService doDetService = new DoDetServiceImpl();
			DoHdr doHdr = new DoHdr();
			Map plntMap = new HashMap();
			Map doHdrDetails = new HashMap();
			ArrayList custDetails = new ArrayList();
			ArrayList shippingCustDetails = new ArrayList();
			List<DoDet> doDetList = new ArrayList<DoDet>();
			ArrayList itemList = new ArrayList();

			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				doHdr = doHDRService.getDoHdrById(plant, dono);
				doDetList = doDetService.getDoDetById(plant, dono);

				ArrayList plntList = new PlantMstDAO().getPlantMstDetails(plant);
				plntMap = (Map) plntList.get(0);
				custDetails = new CustUtil().getCustomerDetailsForDO(dono, plant);
				shippingCustDetails = new MasterUtil().getOutboundShippingDetails(dono, doHdr.getSHIPPINGCUSTOMER(),
						plant);

				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}

				doHdrDetails = new DOUtil().getDOReceiptInvoiceHdrDetails(plant, "Outbound Order");

				
				for (DoDet dodet : doDetList) {
					Map itemMap = new HashMap();
//					List listItem = new ItemUtil().queryItemMstDetails(dodet.getITEM(), plant);
//					Vector arrItem = (Vector) listItem.get(0);
					Map arrItem = new ItemMstUtil().GetProductForPurchase(dodet.getITEM(),plant);
					if (arrItem.size() > 0) {
						itemMap.put("brand",StrUtils.fString((String)arrItem.get("PRD_BRAND_ID")));
						itemMap.put("hscode",StrUtils.fString((String)arrItem.get("HSCODE")));
						itemMap.put("coo",StrUtils.fString((String)arrItem.get("COO")));
//						itemMap.put("brand", StrUtils.fString((String) arrItem.get(19)));		
//						itemMap.put("hscode", StrUtils.fString((String) arrItem.get(27)));
//						itemMap.put("coo", StrUtils.fString((String) arrItem.get(28)));
						itemList.add(itemMap);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}

			RequestDispatcher rd = request.getRequestDispatcher("/jsp/Peppolsalesorderdetail.jsp");
			request.setAttribute("DoHdr", doHdr);
			request.setAttribute("DoDetList", doDetList);
			request.setAttribute("PLNTMAP", plntMap);
			request.setAttribute("DOHDRDETAILS", doHdrDetails);
			request.setAttribute("CUSTDETAILS", custDetails);
			request.setAttribute("SHIPPINGCUSTDETAILS", shippingCustDetails);
			request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
			request.setAttribute("ItemList", itemList);

			request.setAttribute("IMAGEPATH", imagePath);

			rd.forward(request, response);
		}else if (action.equalsIgnoreCase("getsalesorder")) {
			String jsonString = "";
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			PeppolSalesOrderOutput peppolSalesOrderOutput = new PeppolSalesOrderOutput();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/sales-orders?client_id="+peppolclientid+"&status=DRAFT");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setDoOutput(true);
				con.setUseCaches(false);
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					peppolSalesOrderOutput = gson.fromJson(oresponse.toString(), PeppolSalesOrderOutput.class);
					System.out.println(peppolSalesOrderOutput);
					boolean isfinished = createsalesorder(plant, username, peppolSalesOrderOutput.getResults(), peppolAuthPojo.getAccess_token());
					if(isfinished) {
						jsonString = "created";
					}else {
						jsonString = "not created";
					}
					
				}else {
					System.out.println("NOT OK");
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			response.sendRedirect("../peppolapi/salesordersummary?msg=");
		}else if (action.equalsIgnoreCase("getsalesordersummary")) {
			net.sf.json.JSONObject resultJson = new net.sf.json.JSONObject();
			JSONArray jsonArray1 = new JSONArray();
			try {
				List<DoHdr> salesorderlist = new ArrayList<DoHdr>();
				salesorderlist = new DoHdrDAO().getDoHdrPeppol(plant);	
				
				int Index = 0;
				for (DoHdr sale : salesorderlist) {
					Index = Index + 1;
					net.sf.json.JSONObject resultJsonInt = new net.sf.json.JSONObject();
					resultJsonInt.put("INDEX", Index);
					resultJsonInt.put("SALESORDERUUID", StrUtils.fString((String)sale.getSALESORDERUUID()));
					resultJsonInt.put("DONO", StrUtils.fString((String)sale.getDONO()));
					resultJsonInt.put("CustName", StrUtils.fString((String)sale.getCustName()));
					resultJsonInt.put("ORDERSTATUS", StrUtils.fString((String)sale.getORDER_STATUS()));
					resultJsonInt.put("ISINVOICE",sale.getISINVOICED());
					resultJsonInt.put("IVOICENUMBER",sale.getINVOICEDID());
					resultJsonInt.put("SALESDATE",sale.getDELDATE());
					jsonArray1.add(resultJsonInt);
				}
				resultJson.put("SALESORDER", jsonArray1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			response.getWriter().write(resultJson.toString());
		}else if (action.equalsIgnoreCase("getinvoicesummary")) {
			net.sf.json.JSONObject resultJson = new net.sf.json.JSONObject();
			JSONArray jsonArray1 = new JSONArray();
			InvoiceUtil movHisUtil       = new InvoiceUtil();
			ArrayList movQryList  = new ArrayList();
			try {
				movQryList = movHisUtil.getpeppolinvoicesummary(plant);	
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
	            if (movQryList.size() > 0) {
	            int Index = 0;
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                         Map lineArr = (Map) movQryList.get(iCnt);                            
	                         net.sf.json.JSONObject resultJsonInt = new net.sf.json.JSONObject();
	                    	 Index = Index + 1;
	                    	 resultJsonInt.put("Index",(Index));
	                    	 
	                    	 resultJsonInt.put("INVOICE",StrUtils.fString((String)lineArr.get("INVOICE")));
	                    	 resultJsonInt.put("ID",StrUtils.fString((String)lineArr.get("ID")));
	                    	 resultJsonInt.put("INVOICE_DATE",StrUtils.fString((String)lineArr.get("INVOICE_DATE")));
	                    	 resultJsonInt.put("CUSTNO",StrUtils.fString((String)lineArr.get("CUSTNO")));
	                    	 resultJsonInt.put("SUB_TOTAL",Numbers.toMillionFormat((String)lineArr.get("SUB_TOTAL"),Integer.valueOf(numberOfDecimal)));
	                    	 resultJsonInt.put("TAXAMOUNT",Numbers.toMillionFormat((String)lineArr.get("TAXAMOUNT"),Integer.valueOf(numberOfDecimal)));
	                    	 resultJsonInt.put("TOTAL_AMOUNT",Numbers.toMillionFormat((String)lineArr.get("TOTAL_AMOUNT"),Integer.valueOf(numberOfDecimal)));
	                    	 resultJsonInt.put("peppolstatus",StrUtils.fString((String)lineArr.get("PEPPOL_STATUS")));
	                    	 resultJsonInt.put("peppoldocid",StrUtils.fString((String)lineArr.get("PEPPOL_DOC_ID")));
	                    	
	                         jsonArray1.add(resultJsonInt);                    	            	
	                }
	            }
				
				resultJson.put("INVOICE", jsonArray1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			response.getWriter().write(resultJson.toString());
		}else if (action.equalsIgnoreCase("getpurchaseinvoicesummary")) {
			net.sf.json.JSONObject resultJson = new net.sf.json.JSONObject();
			JSONArray jsonArray1 = new JSONArray();
			InvoiceUtil movHisUtil       = new InvoiceUtil();
			ArrayList movQryList  = new ArrayList();
			try {
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				Hashtable ht = new Hashtable();
				ht.put("PLANT", plant);
				List<PoHdr> poHeaders = new PoHdrDAO().getPoHdrSummaryByApproveforpeppol(ht, "", "", "");
	            if (poHeaders.size() > 0) {
	            int Index = 0;
	            for (PoHdr poHdr : poHeaders) {
					                         
	                         net.sf.json.JSONObject resultJsonInt = new net.sf.json.JSONObject();
	                    	 Index = Index + 1;
	                    	 resultJsonInt.put("Index",(Index));
	                    	 resultJsonInt.put("ID",poHdr.getID());
	                    	 resultJsonInt.put("INVDATE",poHdr.getCollectionDate());
	                    	 resultJsonInt.put("PONO",poHdr.getPONO());
	                    	 resultJsonInt.put("SUPPCODE",poHdr.getCustCode());
	                    	 resultJsonInt.put("SUPPNAME",poHdr.getCustName());
	                    	 resultJsonInt.put("ORDERSTATUS",poHdr.getORDER_STATUS());
	                    	 resultJsonInt.put("PID",poHdr.getPURCHASEPEPPOLID());
	                    	 resultJsonInt.put("UUID",poHdr.getPEPPOLINVOICEUUID());
	                    	 resultJsonInt.put("STATUS",poHdr.getPOSENDSTATUS());
	                    	 resultJsonInt.put("RESPONSECODE",poHdr.getRESPONCECODE());
	                    	 resultJsonInt.put("REPOSNSEMESSAGE",poHdr.getRESPONCEMESSAGE());
	                    	
	                    	
	                    	
	                         jsonArray1.add(resultJsonInt);                    	            	
	                }
	            }
				
				resultJson.put("INVOICE", jsonArray1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			response.getWriter().write(resultJson.toString());
		}else if (action.equalsIgnoreCase("showxml")) {
			String sid = StrUtils.fString(request.getParameter("SID"));
			JSONObject json = new JSONObject();
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			peppolxml peppolxmlfile = new peppolxml();
			try {
				
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/orders/"+sid+"/files");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setDoOutput(true);
				con.setUseCaches(false);
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					peppolxmlfile = gson.fromJson(oresponse.toString(), peppolxml.class);
					System.out.println(peppolxmlfile);
					json.put("URL", peppolxmlfile.getOrder_xml_url());
				}else {
					System.out.println("NOT OK");
				}
				
				
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
		}else if (action.equalsIgnoreCase("salesfliptoinvoice")) {
			String jsonString = "";
			JSONObject json = new JSONObject();
			String sid = StrUtils.fString(request.getParameter("SID"));
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			PeppolSalesInvoice peppolSalesInvoice = new PeppolSalesInvoice();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/sales-orders/"+sid+"/flip");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				//String urlParameters  = "";
				//byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
				//con.setDoOutput(true);
				//con.setUseCaches(false);
				//try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				  // wr.write( postData );
				//}
				String POST_PARAMS = "";
				con.setDoOutput(true);
				OutputStream os = con.getOutputStream();
				os.write(POST_PARAMS.getBytes());
				os.flush();
				os.close();
				
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200 || responseCode == 201) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					peppolSalesInvoice = gson.fromJson(oresponse.toString(), PeppolSalesInvoice.class);
					System.out.println(peppolSalesInvoice);
					boolean isfinished = createinvoice(plant, username, peppolSalesInvoice, peppolAuthPojo.getAccess_token(),sid);
					json.put("response", "Sales Order Converted Into Invoice");
				}else {
					System.out.println("NOT OK");
					json.put("response", "Unable To Convert Sales Order Into Invoice");
				}
				
			}catch (Exception e) {
				
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
			//response.sendRedirect("../peppolapi/salesordersummary");
		}else if (action.equalsIgnoreCase("sendinvoice")) {
			String jsonString = "";
			JSONObject json = new JSONObject();
			String sid = StrUtils.fString(request.getParameter("SID"));
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			Peppolinvicesendresponse peppolinvicesendresponse = new Peppolinvicesendresponse();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/sales-invoices/"+sid+"/action");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				//String urlParameters  = "";
				//byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
				//con.setDoOutput(true);
				//con.setUseCaches(false);
				//try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				  // wr.write( postData );
				//}
				String jsondata = "{\"type\":\"SEND\"}";
				con.setDoOutput(true);
				OutputStream os = con.getOutputStream();
				os.write(jsondata.getBytes());
				os.flush();
				os.close();
				
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200 || responseCode == 201) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					peppolinvicesendresponse = gson.fromJson(oresponse.toString(), Peppolinvicesendresponse.class);
					System.out.println(peppolinvicesendresponse);
					
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", plant);
					htCondition.put("PEPPOL_DOC_ID", sid);
					String updateHdr = "set PEPPOL_STATUS='1' ";
					new InvoiceUtil().updatepeppolstatus(updateHdr, htCondition, "");
					jsonString = "Invoice Send Successfully";
					json.put("response", jsonString);
				}else {
					System.out.println("NOT OK");
					jsonString="Invalid delivery_channel";
					json.put("response", jsonString);
				}
				
			}catch (Exception e) {
				try {
					json.put("response", "Error in Invoce Send");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
			//response.sendRedirect("../peppolapi/salesordersummary");
		}else if (action.equalsIgnoreCase("getinvoiceevents")) {
			String jsonString = "";
			JSONObject json = new JSONObject();
			String sid = StrUtils.fString(request.getParameter("SID"));
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			Invoiceevents invoiceevents = new Invoiceevents();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/sales-invoices/"+sid+"/events?sort=time,desc&key=up_delivery.peppol_response");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setDoOutput(true);
				con.setUseCaches(false);
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					invoiceevents = gson.fromJson(oresponse.toString(), Invoiceevents.class);
					System.out.println(invoiceevents);
					json.put("EVENTS",invoiceevents.getResults().toString());
					
				}else {
					System.out.println("NOT OK");
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}if(action.equalsIgnoreCase("invoicedetail")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PeppolInvoiceDeatil.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("purchaseinvoicefrompeppol")) {

			String jsonString = "";
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/purchase-invoices?is_invoice_lines_included=true");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setDoOutput(true);
				con.setUseCaches(false);
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					purchaseInvoice = gson.fromJson(oresponse.toString(), PurchaseInvoice.class);
					System.out.println(purchaseInvoice);
					boolean isfinished = createpurchaseinvoice(plant, username, purchaseInvoice.getResults(), peppolAuthPojo.getAccess_token());
					
					
				}else {
					System.out.println("NOT OK");
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			response.sendRedirect("../peppolapi/purchaseinvoicesummary");
		}else if (action.equalsIgnoreCase("acceptinvoice")) {
			String jsonString = "";
			JSONObject json = new JSONObject();
			String sid = StrUtils.fString(request.getParameter("SID"));
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			Purchaseinvoiceresponse purchaseinvoiceresponse = new Purchaseinvoiceresponse();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/purchase-invoices/"+sid+"/action");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				//String urlParameters  = "";
				//byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
				//con.setDoOutput(true);
				//con.setUseCaches(false);
				//try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				  // wr.write( postData );
				//}
				String jsondata = "{\"type\":\"ACCEPT\"}";
				con.setDoOutput(true);
				OutputStream os = con.getOutputStream();
				os.write(jsondata.getBytes());
				os.flush();
				os.close();
				
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200 || responseCode == 201) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					purchaseinvoiceresponse = gson.fromJson(oresponse.toString(), Purchaseinvoiceresponse.class);
					System.out.println(purchaseinvoiceresponse);
					
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", plant);
					htCondition.put("PURCHASEPEPPOLID", sid);
					String updateHdr = "set POSENDSTATUS='ACCEPTED',RESPONCECODE='"+purchaseinvoiceresponse.getPurchase_invoices_uuid()+"' ";
					new PoHdrDAO().updatePO(updateHdr, htCondition, "");

					json.put("response", "Purchase Invoice Accepted");
				}else {
					System.out.println("NOT OK");
					json.put("response", "Unable To Accept Purchase Invoice");
				}
				
			}catch (Exception e) {
				
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
			//response.sendRedirect("../peppolapi/salesordersummary");
		}else if (action.equalsIgnoreCase("rejectinvoice")) {
			String jsonString = "";
			JSONObject json = new JSONObject();
			String sid = StrUtils.fString(request.getParameter("SID"));
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			Purchaseinvoiceresponse purchaseinvoiceresponse = new Purchaseinvoiceresponse();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/purchase-invoices/"+sid+"/action");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				//String urlParameters  = "";
				//byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
				//con.setDoOutput(true);
				//con.setUseCaches(false);
				//try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				  // wr.write( postData );
				//}
				String jsondata = "{\"type\":\"REFUSE\"}";
				con.setDoOutput(true);
				OutputStream os = con.getOutputStream();
				os.write(jsondata.getBytes());
				os.flush();
				os.close();
				
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200 || responseCode == 201) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					purchaseinvoiceresponse = gson.fromJson(oresponse.toString(), Purchaseinvoiceresponse.class);
					System.out.println(purchaseinvoiceresponse);
					
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", plant);
					htCondition.put("PURCHASEPEPPOLID", sid);
					String updateHdr = "set POSENDSTATUS='REJECTED',RESPONCECODE='"+purchaseinvoiceresponse.purchase_invoices_uuid+"' ";
					new PoHdrDAO().updatePO(updateHdr, htCondition, "");
					json.put("response", "Purchase Invoice Rejected");
				}else {
					System.out.println("NOT OK");
					json.put("response", "Unable To Reject Purchase Invoice");
				}

			}catch (Exception e) {
				
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
			//response.sendRedirect("../peppolapi/salesordersummary");
		}else if (action.equalsIgnoreCase("getinvoiceresponse")) {
			String jsonString = "";
			JSONObject json = new JSONObject();
			String sid = StrUtils.fString(request.getParameter("SID"));
			String istatus = StrUtils.fString(request.getParameter("ISTATUS"));
			PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
			PurchaseInvoiceResponsemessage purchaseInvoiceResponsemessage = new PurchaseInvoiceResponsemessage();
			try {
				peppolAuthPojo = authenticate();
				
				ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
				URL obj = new URL(peppolbasicurl+"/v4/purchase-invoices/"+sid+"/invoice-responses");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization", "Bearer " + peppolAuthPojo.getAccess_token());
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				//String urlParameters  = "";
				//byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
				//con.setDoOutput(true);
				//con.setUseCaches(false);
				//try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				  // wr.write( postData );
				//}
				String jsondata="";
				if(istatus.equalsIgnoreCase("REJECTED")) {
					jsondata = "{ \"response_code\": \"RE\",\"rejection_reason\": \"DEMO\",\"status_reason_code\": \"OTH\"}";
				}else{
					jsondata = "{ \"response_code\": \"AP\",\"rejection_reason\": null,\"status_reason_code\": null}";
				}
				
				con.setDoOutput(true);
				OutputStream os = con.getOutputStream();
				os.write(jsondata.getBytes());
				os.flush();
				os.close();
				
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				
				String msg = "";
				if (responseCode == 200 || responseCode == 201 ||  responseCode == 202) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					purchaseInvoiceResponsemessage = gson.fromJson(oresponse.toString(), PurchaseInvoiceResponsemessage.class);
					System.out.println(purchaseInvoiceResponsemessage);
					
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", plant);
					htCondition.put("PURCHASEPEPPOLID", sid);
					String updateHdr = "set RESPONCEMESSAGE='"+purchaseInvoiceResponsemessage.getIntake_id()+"' ";
					new PoHdrDAO().updatePO(updateHdr, htCondition, "");
					json.put("response", "Purchase Invoice Response Send");
				}else {
					System.out.println("NOT OK");
					json.put("response", "Unable To Send Purchase Invoice Response");
				}

			}catch (Exception e) {
				
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
			//response.sendRedirect("../peppolapi/salesordersummary");
		}else if (action.equalsIgnoreCase("pidetail")) {
			try {
				String pono = StrUtils.fString(request.getParameter("pono"));
				request.setAttribute("PONO", pono);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/purchaseInvoiceDetail.jsp");
				rd.forward(request, response);
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
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		PlantMstUtil plantMstUtil = new PlantMstUtil();
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null)    //  Invalid Session
		{
			request.getSession().invalidate();
		    System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}
		
	}
	
	private  PeppolAuthPojo authenticate() {
		PeppolAuthPojo peppolAuthPojo = new PeppolAuthPojo();
		try {
			ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
			URL obj = new URL(peppolauthurl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			String clientid=peppolclientkey;
			String clientsecret=peppolclientsecret;
			String urlParameters  = "grant_type=client_credentials&client_id="+clientid+"&client_secret="+clientsecret+"";
			byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
			con.setDoOutput(true);
			con.setUseCaches(false);
			try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
			   wr.write( postData );
			}
			System.out.println(con.getResponseMessage());
			int responseCode = con.getResponseCode();
			
			String msg = "";
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer oresponse = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					oresponse.append(inputLine);
				}
				in.close();
				System.out.println(oresponse.toString());
				Gson gson = new Gson();
				peppolAuthPojo = gson.fromJson(oresponse.toString(), PeppolAuthPojo.class);
				System.out.println(peppolAuthPojo);
				
			}else {
				System.out.println("NOT OK");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return peppolAuthPojo;
		
	}
	
	

	public boolean createsalesorder(String plant, String username, List<PeppolSalesOrder> peppolSalesOrder,String authenticastion) {
		boolean result = false;
		DoHdr doHdr = new DoHdr();

		CustUtil custUtil = new CustUtil();
		ItemUtil itemUtil = new ItemUtil();
		Hashtable htData = new Hashtable();
		DoHDRService doHDRService = new DoHdrServiceImpl();
		DoDetService doDetService = new DoDetServiceImpl();
		boolean isOrderExists = false;
		CurrencyDAO _CurrencyDAO = new CurrencyDAO();
		int salesid = 0;
		String invoiceId = "", invoiceNumber = "", Reference = "", UpdatedDateUTC = "", CurrencyCode = "";
		String Name = "", EmailAddress = "", BankAccountDetails = "";
		String AddressType = "", City = "", Region = "", PostalCode = "", Country = "";
		String Phonetype = "", PhoneNumber = "", PhoneAreaCode = "", PhoneCountryCode = "";
		String UpdatedDateUTCcon = "", DefaultCurrency = "", DateString = "", Date = "", DueDateString = "",
				DueDate = "", BrandingThemeID = "", Status = "", LineAmountTypes = "";
		String ItemCode = "", Description = "", TaxType = "", AccountCode = "";
		String collectiondate = "", Duedate = "", FirstName = "", LastName = "";
		String CURRENCYUSEQT = "";
		String itemunitcost = "", Type = "", NameId = "";
		float sub = 0, tax = 0;
		double outbound = 0;
		double unitprice = 0, SubTotal = 0, TotalTax = 0, Total = 0, TaxAmount = 0, LineAmount = 0, DiscountAmount = 0;
		boolean IsSupplier, IsCustomer;
		double Quantity = 0.0, DiscountRate = 0;
		double UnitAmount = 0;
		boolean updateFlag = false;
		boolean editsave = false;
		boolean delete = false;

		try {

			String taxby = new PlantMstDAO().getTaxBy(plant);
			List<DoDet> DoDetList = new ArrayList();

			if (peppolSalesOrder.size() > 0) {
				for (PeppolSalesOrder fdata : peppolSalesOrder) {

					salesid = fdata.getId();
					invoiceNumber = fdata.getSales_order_number();
					invoiceNumber = skipSpecialCharacters(invoiceNumber);
					Reference = fdata.getPo_number();
					Total = fdata.getAmount();
					CurrencyCode = "SGD";

					Peppoldebitor peppoldebitor = getdebitor(fdata.getDebtor_id(), authenticastion);
					// contact start
					String CName = peppoldebitor.getDebtor_number();
					NameId = CName.replace("&", "And");
					NameId = NameId.replaceAll("[^a-zA-Z0-9 ]", "");
					NameId = NameId.replace(" ", "");
					NameId = NameId.substring(0, Math.min(NameId.length(), 50));

					Name = peppoldebitor.getName();
					Name = Name.replace("'", "");
					Name = Name.replace("[", "");
					Name = Name.replace("]", "");
					Name = Name.substring(0, Math.min(Name.length(), 100));

					FirstName = peppoldebitor.getName();
					if (FirstName == null)
						FirstName = "";
					FirstName = FirstName.substring(0, Math.min(FirstName.length(), 100));

					EmailAddress = peppoldebitor.getEmail();
					if (EmailAddress == null)
						EmailAddress = "";
					EmailAddress = EmailAddress.substring(0, Math.min(EmailAddress.length(), 50));

					Reference = peppoldebitor.getDebtor_reference();
					if (Reference == null)
						Reference = "";
					Reference = Reference.substring(0, Math.min(Reference.length(), 20));

					String add1 = "", add2 = "", add3 = "", add4 = "", astate = "", azip = "", acountry = "",
							sadd1 = "", sadd2 = "", sadd3 = "", sadd4 = "", sastate = "", sazip = "", sacountry = "";

					try {
						add1 = datavalid(peppoldebitor.getAddress(), 50);
						astate = datavalid(peppoldebitor.getCity(), 100);
						azip = datavalid(peppoldebitor.getZip_code(), 10);
						acountry = datavalid(peppoldebitor.getCity(), 100);

						sadd1 = datavalid(peppoldebitor.getAddress(), 50);
						sastate = datavalid(peppoldebitor.getCity(), 100);
						sazip = datavalid(peppoldebitor.getZip_code(), 10);
						sacountry = datavalid(peppoldebitor.getCity(), 100);

					} catch (Exception e) {

					}
					PhoneNumber = peppoldebitor.getPhone_number();
					if (PhoneNumber == null)
						PhoneNumber = "";
					Hashtable ht = new Hashtable();
					if (!custUtil.isExistCustomer(NameId, plant) && !custUtil.isExistCustomerName(Name, plant)) { // check
																													// if
																													// customer
																													// already
																													// exits

						ht.put(IConstants.PLANT, plant);
						ht.put(IConstants.CUSTOMER_CODE, NameId);
						ht.put("USER_ID", "");
						ht.put(IConstants.CUSTOMER_NAME, Name);
						ht.put(IConstants.CUSTOMER_LAST_NAME, LastName);
						ht.put(IConstants.companyregnumber, "");
						ht.put("CURRENCY_ID", DefaultCurrency);
						ht.put(IConstants.ADDRESS1, add1);
						ht.put(IConstants.ADDRESS2, add2);
						ht.put(IConstants.ADDRESS3, add3);
						ht.put(IConstants.ADDRESS4, add4);
						ht.put(IConstants.COUNTRY, acountry);
						ht.put(IConstants.STATE, astate);
						ht.put(IConstants.ZIP, azip);

						ht.put(IConstants.SHIP_ADDR1, sadd1);
						ht.put(IConstants.SHIP_ADDR2, sadd2);
						ht.put(IConstants.SHIP_ADDR3, sadd3);
						ht.put(IConstants.SHIP_ADDR4, sadd4);
						ht.put(IConstants.SHIP_COUNTRY_CODE, sacountry);
						ht.put(IConstants.SHIP_STATE, sastate);
						ht.put(IConstants.SHIP_ZIP, sazip);

						ht.put(IConstants.NAME, FirstName);
						ht.put(IConstants.DESGINATION, "");
						ht.put(IConstants.TELNO, PhoneNumber);
						ht.put(IConstants.HPNO, PhoneNumber);
						ht.put(IConstants.FAX, "");
						ht.put(IConstants.EMAIL, EmailAddress);
						ht.put(IConstants.TRANSPORTID, "");
						ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
						ht.put(IConstants.CREATED_BY, username);
						ht.put(IConstants.ISACTIVE, "Y");
						String crdlimtby = "NOLIMIT";
						ht.put("CREDIT_LIMIT_BY", crdlimtby);
						ht.put(IConstants.CUSTOMEREMAIL, EmailAddress);
						ht.put(IConstants.TAXTREATMENT, "Non GST Registered");
						boolean custInserted = custUtil.insertCustomer(ht);
					} else {
						String sNameId = new CustMstDAO().getcustomercodebyname(plant, Name);
						if (sNameId == null) {
						} else {
							NameId = sNameId;
						}

					}
					// contact end

					String outboundgst = fdata.getSales_order_lines().get(0).getService_vat();

					// save DOHDR
					doHdr.setPLANT(plant);
					doHdr.setDONO(invoiceNumber);
					doHdr.setORDERTYPE("PEPPOL");
					doHdr.setCollectionDate(dateformatercustom(fdata.getExpected_delivery_date()));
					doHdr.setCURRENCYID(CurrencyCode);
//			 			doHdr.setDELDATE(Duedate);
					doHdr.setDELDATE(dateformatercustom(fdata.getSales_order_date()));
					doHdr.setSTATUS("N");
					doHdr.setPickStaus("N");
					doHdr.setCRAT(new DateUtils().getDateTime());
					doHdr.setCRBY(username);
					doHdr.setJobNum(Reference);
					doHdr.setCustCode(NameId);
					doHdr.setCustName(Name);
					doHdr.setCollectionTime(new DateUtils().getTimeHHmm());
					doHdr.setPersonInCharge("");
					doHdr.setContactNum("");
					doHdr.setAddress("");
					doHdr.setAddress2("");
					doHdr.setAddress3("");
					doHdr.setSHIPPINGID(NameId);
					doHdr.setSHIPPINGCUSTOMER(Name);
					doHdr.setDELIVERYDATE(dateformatercustom(fdata.getExpected_delivery_date()));
					doHdr.setOUTBOUND_GST(Double.parseDouble(fdata.getSales_order_lines().get(0).getService_vat()));
					doHdr.setSTATUS_ID("");
					doHdr.setEMPNO("0");
					doHdr.setESTNO("");
					doHdr.setORDERDISCOUNT(Double.parseDouble("0"));
					doHdr.setSHIPPINGCOST(Double.parseDouble("0"));
					doHdr.setDELIVERYDATEFORMAT(Short.parseShort("1"));
					doHdr.setTAXTREATMENT("GST Registered");
					doHdr.setORDER_STATUS("Open");
					doHdr.setDISCOUNT(Double.parseDouble("0"));
					doHdr.setDISCOUNT_TYPE("%");
					doHdr.setADJUSTMENT(Double.parseDouble("0"));
					doHdr.setAPPROVESTATUS("");
					String itemrates = "";
					if (LineAmountTypes.equalsIgnoreCase("Inclusive")) {
						itemrates = "1";
					} else {
						itemrates = "0";
					}
					doHdr.setITEM_RATES(Short.parseShort(itemrates));
					doHdr.setSALES_LOCATION("");
					doHdr.setINCOTERMS("");
					doHdr.setPAYMENT_TERMS("");
					doHdr.setPAYMENTTYPE("");
					doHdr.setAPP_CUST_ORDER_STATUS("");
					doHdr.setRemark1("");
					doHdr.setRemark2("");
					doHdr.setRemark3("");
					List listQry = _CurrencyDAO.getCurrencyListWithcurrencySeq(plant, CurrencyCode);
					for (int i = 0; i < listQry.size(); i++) {
						Map m = (Map) listQry.get(i);
						CURRENCYUSEQT = (String) m.get("CURRENCYUSEQT");
					}
					doHdr.setCURRENCYUSEQT(Double.valueOf(CURRENCYUSEQT));
					doHdr.setORDERDISCOUNTTYPE(CurrencyCode);
					if (outboundgst.equalsIgnoreCase("")) {
						doHdr.setTAXID(Integer.valueOf("0"));
					} else if (outboundgst.equalsIgnoreCase("0")) {
						doHdr.setTAXID(Integer.valueOf("0"));
					} else {
						doHdr.setTAXID(Integer.valueOf("27"));
					}
					doHdr.setISDISCOUNTTAX(Short.parseShort("0"));
					doHdr.setISORDERDISCOUNTTAX(Short.parseShort("0"));
					doHdr.setISSHIPPINGTAX(Short.parseShort("0"));
					doHdr.setTRANSPORTID(Integer.valueOf("0"));
					doHdr.setISFROMPEPPOL((short)1);
					doHdr.setSALESORDERUUID(String.valueOf(fdata.getId()));
					doHdr.setISINVOICED((short)0);
					doHdr.setINVOICEDID("");

					Hashtable doht = new Hashtable();
					doht.put("dono", invoiceNumber);
					doht.put("PLANT", plant);
					isOrderExists = new DoHdrDAO().isExisit(doht);
					if (!isOrderExists) {
						int hdrid = new DoHdrDAO().addDoHdrReturnKey(doHdr);
					} else {
						new DoHdrDAO().updateDoHdr(doHdr);
						Hashtable dodetdelete = new Hashtable();
						dodetdelete.put("PLANT", plant);
						dodetdelete.put("DONO", invoiceNumber);
						new DoDetDAO().delete(dodetdelete);

					}
					int lnno = 0;

					for (PeppolSalesOrderLineItem Ldata : fdata.getSales_order_lines()) {
						Description = Ldata.getService_name();
						Description = Description.replace("'", "");
						Description = datavalid(Description, 100);

						String itemname = "";
						String prditem = "";
						boolean isnonstock = false;
						ItemCode = Ldata.getService_name();
						ItemCode = ItemCode.replace("&", "And");
						ItemCode = ItemCode.replaceAll("[^a-zA-Z0-9_-]", "");
						ItemCode = ItemCode.replace(" ", "");
						ItemCode = ItemCode.substring(0, Math.min(ItemCode.length(), 50));
						itemname = Description;
						UnitAmount = Ldata.getService_price();

						String uom = "PCS";
						ht.clear();
						if (!(itemUtil.isExistsItemMst(ItemCode, plant))) { // if the item exists already
							ht.put(IConstants.PLANT, plant);
							ht.put(IConstants.ITEM, ItemCode);
							ht.put(IConstants.ITEM_DESC, Description);
							ht.put("REMARK1", Ldata.getService_description());
							ht.put(IConstants.PRDBRANDID, "");
							ht.put(IConstants.PRDCLSID, "");
							ht.put("LOC_ID", "");
							ht.put(IConstants.PRDDEPTID, "");
							ht.put(IConstants.PRICE, String.valueOf(UnitAmount));
							ht.put(IConstants.ISACTIVE, "Y");
// 					          ht.put(IConstants.COST,UnitAmount);
							ht.put(IConstants.DISCOUNT, String.valueOf(DiscountRate));
							if (isnonstock) {
								ht.put(IConstants.NONSTKFLAG, "Y");
							} else {
								ht.put(IConstants.NONSTKFLAG, "N");
							}
							ht.put(IConstants.NONSTKTYPEID, "");
							ht.put("CRBY", username);
							ht.put("CRAT", new DateUtils().getDateTime());
							ht.put("STKUOM", "PCS");
							ht.put("PURCHASEUOM", "PCS");
							ht.put("SALESUOM", "PCS");
							ht.put("RENTALUOM", "PCS");
							ht.put("SERVICEUOM", "PCS");
							ht.put("INVENTORYUOM", "PCS");
							ht.put("ISBASICUOM", "1");
							ht.put("STKQTY", "0");
							ht.put("MAXSTKQTY", "0");

							boolean itemInserted = itemUtil.insertItem(ht);
							if (itemInserted) {
								String alternateItemName = ItemCode;
								List<String> alternateItemNameLists = new ArrayList<String>();
								alternateItemNameLists.add(alternateItemName);
								boolean insertAlternateItem = itemUtil.insertAlternateItemLists(plant, ItemCode,
										alternateItemNameLists);
							}
						} else {
							Hashtable htCondition = new Hashtable();
							htCondition.put(IConstants.ITEM, ItemCode);
							htCondition.put(IConstants.PLANT, plant);
							Hashtable htUpdate = new Hashtable();
							htUpdate.put(IConstants.PRICE, String.valueOf(UnitAmount));
							boolean itemUpdated = itemUtil.updateItem(htUpdate, htCondition);
							uom = new ItemMstDAO().getItemPurchaseUOM(plant, ItemCode);
						}

						lnno++;
						DoDet doDet = new DoDet();
						doDet.setPLANT(plant);
						doDet.setDONO(invoiceNumber);
						doDet.setDOLNNO(lnno);
						doDet.setPickStatus("N");
						doDet.setLNSTAT("N");
						doDet.setITEM(ItemCode);
						doDet.setItemDesc(itemname);
						doDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						String itemunitprice = String
								.valueOf((Ldata.getService_price()) / Double.parseDouble(CURRENCYUSEQT));
						String disctype = "%";
						double discountamount = Double.valueOf(DiscountRate);
						if (!disctype.equalsIgnoreCase("%")) {
							discountamount = discountamount / Double.parseDouble(CURRENCYUSEQT);
						}
						doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
						doDet.setQTYOR(BigDecimal.valueOf((Ldata.getService_quantity())));
						doDet.setQTYIS(BigDecimal.valueOf(0));
						doDet.setQtyPick(BigDecimal.valueOf(0));
						doDet.setUNITMO(uom);
						doDet.setCRAT(new DateUtils().getDateTime());
						doDet.setCRBY(username);
						doDet.setUSERFLD1(Description);
						doDet.setUSERFLD2(Reference);
						doDet.setUSERFLD3(Name);
						doDet.setCURRENCYUSEQT(Double.parseDouble((String) CURRENCYUSEQT));
						doDet.setPRODGST(Double.parseDouble("0"));
						doDet.setACCOUNT_NAME("Local sales - retail");
						doDet.setPRODUCTDELIVERYDATE("");
						doDet.setTAX_TYPE("STANDARD RATED [" + outboundgst + "%]");
						doDet.setDISCOUNT(discountamount);
						doDet.setDISCOUNT_TYPE("%");
						doDet.setADDONAMOUNT(Double.parseDouble("0"));
						doDet.setADDONTYPE(CurrencyCode);
						itemunitcost = String.valueOf((unitprice) / Double.parseDouble(CURRENCYUSEQT));
						doDet.setUNITCOST(Double.parseDouble(itemunitcost));
						doDet.setUPAT(new DateUtils().getDateTime());
						doDet.setUPBY(username);

						new DoDetDAO().addDoDet(doDet);
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.OB_ADD_ITEM);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, NameId);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, Reference);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, invoiceNumber);
						htRecvHis.put(IDBConstants.ITEM, ItemCode);
						htRecvHis.put(IDBConstants.QTY, Quantity);
						htRecvHis.put(IDBConstants.CREATED_BY, "PEPPOL");
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						updateFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);

					}
				}
			}

			result = true;
		} catch (Exception e) {
			System.out.print(e);
		}
		return result;
	}
	
	
	public boolean createinvoice(String plant, String username, PeppolSalesInvoice peppolSalesInvoice,String authenticastion,String sid) {
		boolean result = false;
		List<Hashtable<String,String>> invoiceDetInfoList = null;
		List<Hashtable<String,String>> invoiceAttachmentList = null;
		List<Hashtable<String,String>> invoiceAttachmentInfoList = null;
		Hashtable<String,String> invoiceDetInfo = null;
		Hashtable<String,String> invoiceAttachment = null;
		UserTransaction ut = null;
		InvoiceUtil invoiceUtil = new InvoiceUtil();
		InvoiceDAO invoiceDAO = new InvoiceDAO();

		try {
			
			String CName = peppolSalesInvoice.getDebtor_number();
			String NameId = CName.replace("&", "And");
			NameId = NameId.replaceAll("[^a-zA-Z0-9 ]", "");
			NameId = NameId.replace(" ", "");
			NameId = NameId.substring(0, Math.min(NameId.length(), 50));
			
			invoiceDetInfoList = new ArrayList<Hashtable<String,String>>();
			Hashtable invoiceHdr =new Hashtable(); 
			invoiceHdr.put("PLANT", plant);
			invoiceHdr.put("CUSTNO", NameId);
			invoiceHdr.put("INVOICE", peppolSalesInvoice.getSales_invoice_number());
			invoiceHdr.put("GINO","");
			invoiceHdr.put("DONO", peppolSalesInvoice.getSales_invoice_number());
			invoiceHdr.put("INVOICE_DATE", dateformatercustom(peppolSalesInvoice.getSales_invoice_date()));
			invoiceHdr.put("DUE_DATE", dateformatercustom(peppolSalesInvoice.getSales_invoice_due_date()));
			invoiceHdr.put("PAYMENT_TERMS", peppolSalesInvoice.getPayment_term());
			invoiceHdr.put("EMPNO", "");
			invoiceHdr.put("ORDERTYPE", "");
			invoiceHdr.put("ITEM_RATES", "0");
			invoiceHdr.put("DISCOUNT", "0");
			invoiceHdr.put("ORDER_DISCOUNT", String.valueOf(peppolSalesInvoice.getTotal_discounts()));
			invoiceHdr.put("DISCOUNT_TYPE", "SGD");
			invoiceHdr.put("DISCOUNT_ACCOUNT", "");
			invoiceHdr.put("SHIPPINGCOST", "0");
			invoiceHdr.put("ADJUSTMENT", "0");
			invoiceHdr.put("SUB_TOTAL", String.valueOf(peppolSalesInvoice.getExclusive_amount()));
			invoiceHdr.put("TOTAL_AMOUNT", String.valueOf(peppolSalesInvoice.getAmount()));
			invoiceHdr.put("BILL_STATUS", "OPEN");
			invoiceHdr.put("NOTE", "");
			invoiceHdr.put("TERMSCONDITIONS", "");
			invoiceHdr.put("CRAT",DateUtils.getDateTime());
			invoiceHdr.put("CRBY",username);
			invoiceHdr.put("UPAT",DateUtils.getDateTime());
			invoiceHdr.put("SALES_LOCATION", "");
			invoiceHdr.put("ISEXPENSE","0");
			invoiceHdr.put("TAXTREATMENT","GST Registered");
			invoiceHdr.put("TAXAMOUNT",String.valueOf(peppolSalesInvoice.getVat_amount()));
			invoiceHdr.put("ORIGIN","PEPPOL");
			invoiceHdr.put("DEDUCT_INV","0");
			invoiceHdr.put("CURRENCYUSEQT","1");
			invoiceHdr.put("ORDERDISCOUNTTYPE","SGD");
			invoiceHdr.put("TAXID","27");
			invoiceHdr.put("ISDISCOUNTTAX","1");
			invoiceHdr.put("ISORDERDISCOUNTTAX","1");
			invoiceHdr.put("ISSHIPPINGTAX","1");
			invoiceHdr.put("PROJECTID","");
			invoiceHdr.put("TRANSPORTID","");
			invoiceHdr.put("OUTBOUD_GST",String.valueOf(peppolSalesInvoice.getInvoice_lines().get(0).getService_vat()));
			invoiceHdr.put(IDBConstants.CURRENCYID,peppolSalesInvoice.getCurrency_code());
			invoiceHdr.put("JobNum","");
			invoiceHdr.put("SHIPCONTACTNAME","");
			invoiceHdr.put("SHIPDESGINATION","");
			invoiceHdr.put("SHIPWORKPHONE","");
			invoiceHdr.put("SHIPHPNO","");
			invoiceHdr.put("SHIPEMAIL","");
			invoiceHdr.put("SHIPCOUNTRY","");
			invoiceHdr.put("SHIPADDR1","");
			invoiceHdr.put("SHIPADDR2","");
			invoiceHdr.put("SHIPADDR3","");
			invoiceHdr.put("SHIPADDR4","");
			invoiceHdr.put("SHIPSTATE","");
			invoiceHdr.put("SHIPZIP","");
			invoiceHdr.put("PEPPOL_DOC_ID",String.valueOf(peppolSalesInvoice.getId()));
			/*Get Transaction object*/
			ut = DbBean.getUserTranaction();				
			/*Begin Transaction*/
			ut.begin();
			int invoiceHdrId=0;
			
			invoiceHdr.put("CREDITNOTESSTATUS", "0");
			invoiceHdr.put("TOTAL_PAYING","0");
			invoiceHdrId = invoiceUtil.addInvoiceHdr(invoiceHdr, plant);
			
			if (invoiceHdrId > 0) {
				int i=0;
				for (PeppolInvoiceLine invoiceline:peppolSalesInvoice.getInvoice_lines()) {
					int lnno = i + 1;
					
					String Description = invoiceline.getService_name();
					Description = Description.replace("'", "");
					Description = datavalid(Description, 100);

					String itemname = "";
					String prditem = "";
					boolean isnonstock = false;
					String ItemCode = invoiceline.getService_name();
					ItemCode = ItemCode.replace("&", "And");
					ItemCode = ItemCode.replaceAll("[^a-zA-Z0-9_-]", "");
					ItemCode = ItemCode.replace(" ", "");
					ItemCode = ItemCode.substring(0, Math.min(ItemCode.length(), 50));
					itemname = Description;

					invoiceDetInfo = new Hashtable<String, String>();
					invoiceDetInfo.put("PLANT", plant);
					invoiceDetInfo.put("LNNO", Integer.toString(lnno));
					invoiceDetInfo.put("INVOICEHDRID", Integer.toString(invoiceHdrId));
					invoiceDetInfo.put("ITEM", invoiceline.getService_name());
					invoiceDetInfo.put("ACCOUNT_NAME", "Local sales - retail");
					invoiceDetInfo.put("QTY", String.valueOf(invoiceline.getService_quantity()));
					invoiceDetInfo.put("UNITPRICE", String.valueOf(invoiceline.getService_price()));
					invoiceDetInfo.put("TAX_TYPE", "STANDARD RATED ["+invoiceline.getService_vat()+".0%]");
					invoiceDetInfo.put("AMOUNT", String.valueOf(invoiceline.getService_subtotal()));
					invoiceDetInfo.put("CRAT", DateUtils.getDateTime());
					invoiceDetInfo.put("CRBY", username);
					invoiceDetInfo.put("UPAT", DateUtils.getDateTime());
					invoiceDetInfo.put("DISCOUNT", String.valueOf(invoiceline.getService_discount_perc()));
					invoiceDetInfo.put("DISCOUNT_TYPE", "%");
					invoiceDetInfo.put(IDBConstants.CURRENCYUSEQT, "1");
					invoiceDetInfo.put("UOM", invoiceline.getService_unit());
					invoiceDetInfo.put("NOTE", "");
					invoiceDetInfoList.add(invoiceDetInfo);
				}
				invoiceUtil.addMultipleInvoiceDet(invoiceDetInfoList, plant);
				
				Hashtable htCondition = new Hashtable();
				htCondition.put("PLANT", plant);
				htCondition.put("DONO", peppolSalesInvoice.getSales_invoice_number());
				String updateHdr = "set STATUS='C',ORDER_STATUS='INVOICED',ISINVOICED=1,INVOICEDID='"+peppolSalesInvoice.getId()+"'";
				new DoHdrDAO().update(updateHdr, htCondition, "");
				
			}

			DbBean.CommitTran(ut);
			result = true;
		} catch (Exception e) {
			System.out.print(e);
			DbBean.RollbackTran(ut);
		}
		return result;
	}
	
	
	
public Peppoldebitor getdebitor(int debitid, String authentication) {
	Peppoldebitor peppoldebitor = new Peppoldebitor();
	try {
		ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
		URL obj = new URL(peppolbasicurl+"/v4/debtors/" + debitid);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + authentication);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		con.setUseCaches(false);
		System.out.println(con.getResponseMessage());
		int responseCode = con.getResponseCode();

		String msg = "";
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer oresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				oresponse.append(inputLine);
			}
			in.close();
			System.out.println(oresponse.toString());
			Gson gson = new Gson();
			peppoldebitor = gson.fromJson(oresponse.toString(), Peppoldebitor.class);
			System.out.println(peppoldebitor);

		} else {
			System.out.println("NOT OK");
		}
	} catch (Exception e) {
		System.out.print(e.getMessage());
	}
	
	return peppoldebitor;
}


public PeppolCreditors getcreditors(int creditid, String authentication) {
	PeppolCreditors peppolCreditors = new PeppolCreditors();
	try {
		ResultSetToObjectMap resultSetToObjectMap = new ResultSetToObjectMap();
		URL obj = new URL(peppolbasicurl+"/v4/creditors/" + creditid);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + authentication);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		con.setUseCaches(false);
		System.out.println(con.getResponseMessage());
		int responseCode = con.getResponseCode();

		String msg = "";
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer oresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				oresponse.append(inputLine);
			}
			in.close();
			System.out.println(oresponse.toString());
			Gson gson = new Gson();
			peppolCreditors = gson.fromJson(oresponse.toString(), PeppolCreditors.class);

		} else {
			System.out.println("NOT OK");
		}
	} catch (Exception e) {
		System.out.print(e.getMessage());
	}
	
	return peppolCreditors;
}

public boolean createpurchaseinvoice(String plant, String username, List<PurchaseInvoicehdr> purchaseInvoicehdrList,String authenticastion) {
	boolean result = false;

	try {
		
		for (PurchaseInvoicehdr purchaseInvoicehdr : purchaseInvoicehdrList) {

			Hashtable htPoHrd = new Hashtable();
			htPoHrd.put(IDBConstants.PLANT, plant);
			htPoHrd.put(IDBConstants.PODET_PONUM, purchaseInvoicehdr.getPurchase_invoice_number());
//			sqlBean sqlbn = new sqlBean();
			boolean isValidOrder = new PoHdrDAO().isExisit(htPoHrd, "");

			if (!isValidOrder) {
				CustUtil custUtil = new CustUtil();
				PoHdr pohdr = new PoHdr();
				pohdr.setPLANT(plant);
				pohdr.setPONO(purchaseInvoicehdr.getPurchase_invoice_number());

				PeppolCreditors peppolCreditors = getcreditors(purchaseInvoicehdr.getCreditor_id(), authenticastion);
				
				
				if (!custUtil.isExistVendor(peppolCreditors.getCreditor_number(), plant)) // if the Customer exists already
				{
					
					  
					Hashtable ht = new Hashtable();
					ht.put(IConstants.PLANT, plant);
					ht.put(IConstants.VENDOR_CODE, String.valueOf(peppolCreditors.getCreditor_number()));
					ht.put(IConstants.VENDOR_NAME, String.valueOf(peppolCreditors.getName()));
				    ht.put(IConstants.companyregnumber,String.valueOf(peppolCreditors.getLegal_entity_trn()));
				    ht.put(IConstants.TRANSPORTID, "");
					ht.put("CURRENCY_ID", String.valueOf(peppolCreditors.getCurrency_code()));
					ht.put(IConstants.NAME, "");
					ht.put(IConstants.DESGINATION, "");
					ht.put(IConstants.TELNO, String.valueOf(peppolCreditors.getPhone()));
					ht.put(IConstants.HPNO, "");
					ht.put(IConstants.FAX, "");
					ht.put(IConstants.EMAIL, String.valueOf(peppolCreditors.getEmail()));
					ht.put(IConstants.ADDRESS1, String.valueOf(peppolCreditors.getAddress()));
					ht.put(IConstants.ADDRESS2, String.valueOf(peppolCreditors.getCity()));
					ht.put(IConstants.ADDRESS3, "");
					ht.put(IConstants.ADDRESS4, "");
					ht.put(IConstants.COUNTRY, String.valueOf(peppolCreditors.getCity()));
					ht.put(IConstants.ZIP, String.valueOf(peppolCreditors.getZip_code()));
					ht.put(IConstants.USERFLG1, "");
					ht.put(IConstants.REMARKS, "");
					ht.put(IConstants.PAYTERMS, "");
					ht.put("PAYMENT_TERMS", "");
					ht.put(IConstants.PAYINDAYS, String.valueOf(""));
					ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
					ht.put(IConstants.CREATED_BY, username);
					ht.put(IConstants.ISACTIVE, "Y");
					
					ht.put(IConstants.CUSTOMEREMAIL,String.valueOf(peppolCreditors.getEmail()));
			        ht.put(IConstants.WEBSITE,"");
			        ht.put("ISPEPPOL","");
					ht.put("PEPPOL_ID","");
			        ht.put(IConstants.FACEBOOK,"");
			        ht.put(IConstants.TWITTER,"");
			        ht.put(IConstants.LINKEDIN,"");
			        ht.put(IConstants.SKYPE,"");
			        ht.put(IConstants.OPENINGBALANCE,"0");
			        ht.put(IConstants.WORKPHONE,"");
					
					ht.put("Comment1", " 0 ");
					if(peppolCreditors.getState() == null) {
						ht.put(IConstants.STATE, String.valueOf(""));
					}else {
						ht.put(IConstants.STATE, String.valueOf(peppolCreditors.getState()));
					}
					ht.put(IConstants.RCBNO, "");
					ht.put(IConstants.SUPPLIERTYPEID, "0");
					ht.put(IConstants.TAXTREATMENT, "GST Registered");
			        ht.put(IDBConstants.BANKNAME,"");
			        ht.put(IDBConstants.IBAN,"");
			        ht.put(IDBConstants.BANKROUTINGCODE,"");

					//mdao.setmLogger(mLogger);
					
				
					boolean custInserted = new CustUtil().insertVendor(ht);


			}
				

				pohdr.setCustCode(peppolCreditors.getCreditor_number());
				pohdr.setCustName(peppolCreditors.getName());
				pohdr.setJobNum("");
				pohdr.setPersonInCharge("");
				pohdr.setContactNum(peppolCreditors.getPhone());
				pohdr.setAddress(peppolCreditors.getAddress());
				pohdr.setAddress2(peppolCreditors.getCity());
				if(peppolCreditors.getState() == null) {
					pohdr.setAddress3("");
				}else {
					pohdr.setAddress3(peppolCreditors.getState());
				}
				pohdr.setCollectionTime("");
				if(purchaseInvoicehdr.getPurchase_invoice_date() == null) {
					pohdr.setCollectionDate("");
				}else {
					pohdr.setCollectionDate(dateformatercustom(purchaseInvoicehdr.getPurchase_invoice_date()));
				}
				pohdr.setRemark1("");
				pohdr.setRemark2("");
				pohdr.setREMARK3("");
				pohdr.setORDERTYPE("");
				pohdr.setINBOUND_GST(purchaseInvoicehdr.getInvoice_lines().get(0).getService_vat());
				pohdr.setCURRENCYID(purchaseInvoicehdr.getCurrency_code());
				pohdr.setDELDATE(dateformatercustom(purchaseInvoicehdr.getSupply_date()));
				pohdr.setSTATUS_ID("NOT PAID");
				pohdr.setSHIPPINGID("");
				pohdr.setSHIPPINGCUSTOMER("");
				pohdr.setINCOTERMS("");
				pohdr.setORDERDISCOUNT(Double.valueOf("0.0"));
				pohdr.setSHIPPINGCOST(Double.valueOf("0.0"));
				pohdr.setPAYMENTTYPE("");
				pohdr.setPAYMENT_TERMS(purchaseInvoicehdr.getPayment_term());
				pohdr.setDELIVERYDATEFORMAT(Short.valueOf(StrUtils.fString("0").trim()));
				pohdr.setTAXTREATMENT("GST Registered");
				pohdr.setPURCHASE_LOCATION("");
				pohdr.setORDER_STATUS("Open");
				pohdr.setADJUSTMENT(Double.valueOf(StrUtils.fString("0.0").trim()));
				pohdr.setISDISCOUNTTAX(Short.valueOf(StrUtils.fString("0").trim()));
				pohdr.setISSHIPPINGTAX(Short.valueOf(StrUtils.fString("0").trim()));
				pohdr.setTAXID(Integer.valueOf(StrUtils.fString("8").trim()));
				pohdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString("0").trim()));
				pohdr.setORDERDISCOUNTTYPE(StrUtils.fString("SGD").trim());
				pohdr.setCURRENCYUSEQT(Double.valueOf("1"));
				pohdr.setEMPNO("");
				pohdr.setPOESTNO("");
				pohdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
				pohdr.setORDERDISCOUNT(0.0);
				pohdr.setSHIPPINGCOST(0.0);
				pohdr.setADJUSTMENT(0.0);
				pohdr.setSTATUS("N");
				pohdr.setLOCALEXPENSES(0.0);
				pohdr.setCRBY(username);
				pohdr.setCRAT(DateUtils.getDateTime());
				pohdr.setSHIPCONTACTNAME("");
				pohdr.setSHIPDESGINATION("");
				pohdr.setSHIPWORKPHONE("");
				pohdr.setSHIPHPNO("");
				pohdr.setSHIPEMAIL("");
				pohdr.setSHIPCOUNTRY("");
				pohdr.setSHIPADDR1("");
				pohdr.setSHIPADDR2("");
				pohdr.setSHIPADDR3("");
				pohdr.setSHIPADDR4("");
				pohdr.setSHIPSTATE("");
				pohdr.setSHIPZIP("");
				pohdr.setAPPROVAL_STATUS("");
				pohdr.setUPAT("");
				pohdr.setUPBY("");
				pohdr.setREVERSECHARGE((short) 0);
				pohdr.setGOODSIMPORT((short) 0);
				pohdr.setPURCHASEPEPPOLID(purchaseInvoicehdr.getId());
				pohdr.setPEPPOLINVOICEUUID(purchaseInvoicehdr.getPurchase_invoice_uuid());
				pohdr.setPOSENDSTATUS("PENDING");

				int hdrid = new PoHdrDAO().addPoHdrReturnKey(pohdr);
				
				if(hdrid > 0) {
					List<PoDet> podetlist = new ArrayList<PoDet>();
					int i = 0;
					for (PurchaseInvoiceLine purchaseInvoiceLine : purchaseInvoicehdr.getInvoice_lines()) {

						i++;
						
						PoDet poDet = new PoDet();
						poDet.setPLANT(plant);
						poDet.setPONO(pohdr.getPONO());
						poDet.setTRANDATE(pohdr.getCollectionDate());
						poDet.setPOLNNO(i);
						poDet.setPOESTNO("");
						poDet.setPOESTLNNO(0);
						poDet.setITEM(purchaseInvoiceLine.getService_name());
						poDet.setUNITCOST(purchaseInvoiceLine.getService_price());
						poDet.setQTYOR(BigDecimal.valueOf(purchaseInvoiceLine.getService_quantity()));
						poDet.setUNITMO("PCS");
						poDet.setPRODUCTDELIVERYDATE(dateformatercustom(purchaseInvoicehdr.getSupply_date()));
						poDet.setDISCOUNT(Double.valueOf("0.0"));
						poDet.setUNITCOST_AOD(0.0);
						poDet.setDISCOUNT_TYPE("SGD");
						poDet.setACCOUNT_NAME("Inventory Asset");
						poDet.setTAX_TYPE("STANDARD RATED ["+purchaseInvoiceLine.getService_vat()+".0%]");
						poDet.setUSERFLD1(purchaseInvoiceLine.getService_description());
						poDet.setUSERFLD2("");
						poDet.setUSERFLD3(peppolCreditors.getName());
						poDet.setCURRENCYUSEQT(pohdr.getCURRENCYUSEQT());
						poDet.setPRODGST(0);
						poDet.setCOMMENT1("");
						poDet.setLNSTAT("N");
						poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
						poDet.setItemDesc(purchaseInvoiceLine.getService_description());
						poDet.setCRBY(username);
						poDet.setCRAT(DateUtils.getDateTime());
						
						new PoDetDAO().addPoDet(poDet);
					
						
						
					}
				}

			}
		}
		
		result = true;
	} catch (Exception e) {
		System.out.print(e);
		
	}
	return result;
}
	
	
	public static String skipSpecialCharacters(String input) {
	    // Replace all special characters except "-" and "_"
	    String result = input.replaceAll("[^a-zA-Z0-9-_]", "");
	    return result;
	}
	
	public String datavalid(String xvalue,int len) {
		xvalue = xvalue.replace("&", "And");
		xvalue = xvalue.replaceAll("[^a-zA-Z0-9 ]", "");
		xvalue = xvalue.substring(0, Math.min(xvalue.length(), len));
		return xvalue;
	}
	
	public String dateformatercustom(String DateString) {
		String outdate="";
		if(DateString == null) {
			outdate="";
		}else {
	        String date = DateString.substring(0, DateString.indexOf("T"));
	        String[] col = date.split("-");
	        String day = col[2];     
	        String month = col[1];   
	        String year = col[0]; 
	        outdate = day+"/"+month+"/"+year;
		}
        
        return outdate;
	}
	
	public String getprodutid(String plant) {

		TblControlDAO _TblControlDAO = new TblControlDAO();
		JSONObject json = new JSONObject();
		String sItemDesc = "";
		String sItem = "";
		String sArtist = "";
		String prd_cls_id = "";
		String prdBrand = "";
		String sTitle = "";
		String sRemark = "";
		String sItemCondition = "";
		String stkqty = "";
		String price = "0.00";
		String cost = "0.00";
		String maxstkqty = "";
		String minsprice = "";
		String discount = "";
		String sUOM = "";
		String NETWEIGHT = "0.00";
		String GROSSWEIGHT = "0.00";
		String HSCODE = "";
		String COO = "";
		String VINNO = "";
		String MODEL = "";
		String RENTALPRICE = "0.00";
		String SERVICEPRICE = "0.00";
		String PURCHASEUOM = "";
		String SALESUOM = "";
		String RENTALUOM = "";
		String SERVICEUOM = "";
		String INVENTORYUOM = "";
		String ISBASICUOM = "";

		String sAddEnb = "enabled";
		String sItemEnb = "enabled";
		String minseq = "", sBatchSeq = "", sZero = "";

		boolean insertFlag = false;
		Hashtable ht = new Hashtable();
		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "PRODUCT");
		try {
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

			// --if exitflag is false than we insert batch number on first time based on
			// plant,currentmonth
			if (exitFlag == false) {
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "PRODUCT");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "P");
				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "0000");
				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, "PEPPOL");
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);
				sItem = "P" + "0001";
			} else {
				// --if exitflag is not false than we updated nxtseq batch number based on
				// plant,currentmonth
				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");
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
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "PRODUCT");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "P");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + (String) updatedSeq.toString() + "'");
				// boolean
				// updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
				sItem = "P" + sZero + updatedSeq;
			}
			
			 _TblControlDAO.updateSeqNo("PRODUCT",plant);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sItem;
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
