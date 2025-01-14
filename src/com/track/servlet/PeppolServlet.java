package com.track.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.BorderFactory;
import javax.transaction.UserTransaction;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.SAXReader;
import org.jboss.dmr.JSONParser;
import org.json.JSONArray;
import org.json.XML;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.itextpdf.io.codec.Base64;
import com.itextpdf.kernel.geom.PageSize;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CountryNCurrencyDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.ExpensesDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PeppolDocIdsDAO;
import com.track.dao.PeppolReceivedDataDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PrdDeptDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.UomDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.InvoiceDet;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.PEPPOL_DOC_IDS;
import com.track.db.object.PEPPOL_RECEIVED_DATA;
import com.track.db.object.PeppolInboundData;
import com.track.db.object.PeppolInboundDataList;
import com.track.db.object.PeppolInvoiceResult;
import com.track.db.util.BillUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UomUtil;
import com.track.gates.DbBean;
import com.track.gates.userBean;
import com.track.service.Costofgoods;
import com.track.service.DoDetService;
import com.track.service.DoHDRService;
import com.track.service.JournalService;
import com.track.service.PeppolReceivedDataService;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
import com.track.serviceImplementation.DoDetServiceImpl;
import com.track.serviceImplementation.DoHdrServiceImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.serviceImplementation.PeppolReceivedDataServiceImpl;
import com.track.tranaction.SendEmail;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.ImageUtil;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;
import com.track.util.http.HttpUtils;
import com.track.util.pdf.PdfUtil;

import net.sf.json.JSONObject;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@WebServlet("/peppol/*")
public class PeppolServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8136069338637043579L;
	public static final String xmlFilePath = "D:\\Invoice_Outbound.xml";
	//public static String peppolukey = "";
	public static String inboundurl = "";
	public static String outboundurl = "";
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
			inboundurl = pppr.getProperty("PEPPOL_INBOUND_URL");
			outboundurl = pppr.getProperty("PEPPOL_OUTBOUND_URL");
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

		if (action.equalsIgnoreCase("peppolpurchase")) {
			try {
				String msg = request.getParameter("msg");
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/peppolPurchase.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("peppolsales")) {
			try {
				String msg = request.getParameter("msg");
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/peppolSales.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("peppolcustomer")) {
			try {
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/peppolCustomer.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("peppolsupplier")) {
			try {
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/peppolSupplier.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equalsIgnoreCase("peppolregister")) {
			try {
				String res = StrUtils.fString(request.getParameter("result"));
				String pgact = StrUtils.fString(request.getParameter("PGaction"));
				request.setAttribute("result", res);
				request.setAttribute("PGaction", pgact);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/peppolRegister.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("getpeppolbill")) {
			String jsonString = "";
			try {

				URL obj = new URL("https://api.ap-connect.dev.einvoice.sg/v1/invoice/inbound?includeDownloaded=1");
				String peppolukey = plantMstUtil.getPeppolUkey(plant);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				// con.setRequestProperty("content-type", "text/xml");
				con.setRequestProperty("api_key", peppolukey);
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				System.out.println("POST Response Code :: " + responseCode);

				String msg = "";
				if (responseCode == 200) { // success
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();

					// print result

					JSONObject peppolin = new JSONObject();
					/*
					 * JSONArray arrivalMoFr = new JSONArray();
					 * arrivalMoFr.put(oresponse.toString());
					 */
					peppolin.put("peppollist", oresponse.toString());
					System.out.println(peppolin);
					Gson gson = new Gson();
					PeppolInboundDataList peppolresult = new Gson().fromJson(peppolin.toString(),
							PeppolInboundDataList.class);
					System.out.println(peppolresult);

					for (PeppolInboundData inboundsata : peppolresult.getPeppollist()) {
						URL obj1 = new URL(inboundsata.getInvoiceFileUrl());
						HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
						con1.setRequestMethod("GET");
						con1.setRequestProperty("api_key", peppolukey);
						System.out.println(con1.getResponseMessage());
						int responseCode1 = con1.getResponseCode();
						System.out.println("POST Response Code :: " + responseCode1);

						String msg1 = "";
						if (responseCode1 == 200) { // success
							BufferedReader in1 = new BufferedReader(new InputStreamReader(con1.getInputStream()));
							String inputLine1;
							StringBuffer oresponse1 = new StringBuffer();

							while ((inputLine1 = in1.readLine()) != null) {
								oresponse1.append(inputLine1);
							}
							in.close();
							// print result
							System.out.println("-------------xml--------------");
							System.out.println(oresponse1.toString());
							System.out.println("-------------xml--------------");

							org.json.JSONObject json = XML.toJSONObject(oresponse1.toString());
							jsonString = json.toString();
							org.json.JSONObject jstrings = (org.json.JSONObject) json.get("StandardBusinessDocument");
							org.json.JSONObject jstring = (org.json.JSONObject) jstrings.get("Invoice");
							System.out.println("-------------json--------------");
							System.out.println(jstring.toString());
							System.out.println("-------------json--------------");
						}

					}

					msg = "Invoice Data processed";
				} else {
					System.out.println("POST request not worked");
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();

					// print result
					System.out.println(response.toString());
					msg = "Unable To Process";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);
			response.getWriter().flush();
			response.getWriter().close();
		}

		if (action.equalsIgnoreCase("getpeppolreciveptdata")) {
			String jsonString = "";
			try {
				PeppolReceivedDataService peppolReceivedDataService = new PeppolReceivedDataServiceImpl();
				PeppolReceivedDataDAO peppolReceivedDataDAO = new PeppolReceivedDataDAO();
				MovHisDAO movHisDao = new MovHisDAO();
				DateUtils dateutils = new DateUtils();

				//URL obj = new URL("https://api.ap-connect.dev.einvoice.sg/v1/invoice/inbound?includeDownloaded=1");
				//URL obj = new URL(inboundurl);
				URI uri = new URI(inboundurl);
				URL obj = uri.toURL();
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				// con.setRequestProperty("content-type", "text/xml");
				String peppolukey = plantMstUtil.getPeppolUkey(plant);
				con.setRequestProperty("api_key", peppolukey);
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				System.out.println("POST Response Code :: " + responseCode);

				String msg = "";
				if (responseCode == 200) { // success
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();

					// print result

					JSONObject peppolin = new JSONObject();
					/*
					 * JSONArray arrivalMoFr = new JSONArray();
					 * arrivalMoFr.put(oresponse.toString());
					 */
					peppolin.put("peppollist", oresponse.toString());
					System.out.println(peppolin);
					Gson gson = new Gson();
					PeppolInboundDataList peppolresult = new Gson().fromJson(peppolin.toString(),
							PeppolInboundDataList.class);
					System.out.println(peppolresult);

					for (PeppolInboundData inboundsata : peppolresult.getPeppollist()) {

						boolean status = new PeppolReceivedDataDAO().IsPeppolReceivedDataByDocid(plant,
								inboundsata.getDocId());
						if (!status) {
							PEPPOL_RECEIVED_DATA PeppolReceived = new PEPPOL_RECEIVED_DATA();
							PeppolReceived.setPLANT(plant);
							PeppolReceived.setDOCID(inboundsata.getDocId());
							PeppolReceived.setEVENT("BILL");
							PeppolReceived.setRECEIVEDAT(inboundsata.getReceivedAt());
							PeppolReceived.setINVOICEFILEURL(inboundsata.getInvoiceFileUrl());
							PeppolReceived.setEVIDENCEFILEURL(inboundsata.getEvidenceFileUrl());
							PeppolReceived.setBILLNO("");
							PeppolReceived.setEXPIRESAT(inboundsata.getExpiresAt());
							PeppolReceived.setBILL_STATUS((short) 0);
							PeppolReceived.setCRAT(dateutils.getDate());
							PeppolReceived.setCRBY(username);

							int ID = peppolReceivedDataService.addPeppolReceivedData(PeppolReceived);

							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", "CREATE PEPPOL_RECEIVED_DATA");
							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, ID);
							htMovHis.put(IDBConstants.CREATED_BY, username);
							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							
						
							String result = "";
							try {

								PeppolReceived.setID(ID);
								PEPPOL_RECEIVED_DATA peppolrecvdata = PeppolReceived;

								URL obj1 = new URL(peppolrecvdata.getINVOICEFILEURL());
								HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
								con1.setRequestMethod("GET");
								con1.setRequestProperty("api_key", peppolukey);
								System.out.println(con1.getResponseMessage());
								int responseCode1 = con1.getResponseCode();
								System.out.println("POST Response Code :: " + responseCode1);

								String msg1 = "";
								if (responseCode1 == 200) { // success
									BufferedReader in1 = new BufferedReader(new InputStreamReader(con1.getInputStream()));
									String inputLine1;
									StringBuffer oresponse1 = new StringBuffer();

									while ((inputLine1 = in1.readLine()) != null) {
										oresponse1.append(inputLine1);
									}
									in1.close();
									// print result
									System.out.println("-------------xml--------------");
									System.out.println(oresponse1.toString());
									System.out.println("-------------xml--------------");
									

									org.json.JSONObject json = XML.toJSONObject(oresponse1.toString());
									jsonString = json.toString();
									
									
									org.json.JSONObject invoice = null;
									org.json.JSONObject jstrings = null;
									Iterator keys1 = json.keys();
									String key1 ="";
									 while(keys1.hasNext()) {
									   key1 = (String)keys1.next();
									   System.out.println(key1);
									 }
									 jstrings = (org.json.JSONObject) json.get(key1);
									 Iterator keys2 = jstrings.keys();
										String key2 ="";
										 while(keys2.hasNext()) {
										   String keycheck2 = (String)keys2.next();
										   System.out.println(keycheck2);
										   if(keycheck2.contains("Invoice")) {
											   key2 = keycheck2;
										   }
										 }
								     invoice = (org.json.JSONObject) jstrings.get(key2);
									 
									
								/*	boolean getsname = true;
									org.json.JSONObject jstrings = null;
									if(getsname) {
										try {
										jstrings = (org.json.JSONObject) json.get("StandardBusinessDocument");
										getsname = false;
										}catch (Exception e) {
											getsname = true;
											System.out.println("StandardBusinessDocument not available");
										}
									}
									
									if(getsname) {
										try {
											jstrings = (org.json.JSONObject) json.get("sh:StandardBusinessDocument");
											getsname = false;
											}catch (Exception e) {
												getsname = true;
												System.out.println("sh:StandardBusinessDocument not available");
											}
									}
									
									boolean getiname = true;
									org.json.JSONObject invoice = null;
									if(getiname) {
										try {
											invoice = (org.json.JSONObject) jstrings.get("Invoice");
											getiname = false;
										}catch (Exception e) {
											getiname = true;
											System.out.println("Invoice not available");
										}
									}
									if(getiname) {
										try {
											invoice = (org.json.JSONObject) jstrings.get("ns4:Invoice");
											getiname = false;
										}catch (Exception e) {
											getiname = true;
											System.out.println("ns4:Invoice not available");
										}
									}
									if(getiname) {
										try {
											invoice = (org.json.JSONObject) jstrings.get("ns:Invoice");
											getiname = false;
										}catch (Exception e) {
											getiname = true;
											System.out.println("ns:Invoice not available");
										}
									}*/
									
									//org.json.JSONObject jstrings = (org.json.JSONObject) json.get("StandardBusinessDocument");
									//org.json.JSONObject jstring = (org.json.JSONObject) jstrings.get("Invoice");
									System.out.println("-------------json--------------");
									System.out.println(invoice.toString());
									System.out.println("-------------json--------------");

									
									/*try {
										org.json.JSONObject AdditionalDocumentReference = (org.json.JSONObject) invoice.get("cac:AdditionalDocumentReference");
										org.json.JSONObject Attachment = (org.json.JSONObject) AdditionalDocumentReference.get("cac:Attachment");
										org.json.JSONObject EmbeddedDocument = (org.json.JSONObject) Attachment.get("cbc:EmbeddedDocumentBinaryObject");
										String crntImage = EmbeddedDocument.getString("content");
										String dname = EmbeddedDocument.getString("filename");
										savedocument(crntImage, dname);
										}catch (Exception e) {
											System.out.println(e.getMessage());
										}*/									
									
										try {
											org.json.JSONObject AdditionalDocumentReference = (org.json.JSONObject) json.get("cac:AdditionalDocumentReference");
											System.out.println("test");
										}catch (Exception e) {
											
										}
									
									//org.json.JSONObject invoice = (org.json.JSONObject) jstrings.get("Invoice");
									org.json.JSONObject AccountingSupplierParty = (org.json.JSONObject) invoice.get("cac:AccountingSupplierParty");
									org.json.JSONObject Party = (org.json.JSONObject) AccountingSupplierParty.get("cac:Party");
									
									
									org.json.JSONObject PartyLegalEntity = (org.json.JSONObject) Party.get("cac:PartyLegalEntity");

									org.json.JSONObject endpoint = (org.json.JSONObject) Party.get("cbc:EndpointID");
									
									org.json.JSONObject LegalMonetaryTotal = (org.json.JSONObject) invoice
											.get("cac:LegalMonetaryTotal");
									org.json.JSONObject PayableAmount = (org.json.JSONObject) LegalMonetaryTotal
											.get("cbc:PayableAmount");
									org.json.JSONObject LineExtensionAmount = (org.json.JSONObject) LegalMonetaryTotal
											.get("cbc:LineExtensionAmount");

									

									org.json.JSONObject TaxTotal = (org.json.JSONObject) invoice.get("cac:TaxTotal");
									org.json.JSONObject TaxAmount = (org.json.JSONObject) TaxTotal.get("cbc:TaxAmount");

									org.json.JSONObject PostalAddress = (org.json.JSONObject) Party.get("cac:PostalAddress");
									org.json.JSONObject Country = (org.json.JSONObject) PostalAddress.get("cac:Country");
									
									
									String supplieremail = "";
									try {
										org.json.JSONObject suppliercontact = (org.json.JSONObject) Party.get("cac:Contact");
										supplieremail = suppliercontact.getString("cbc:ElectronicMail");
									}catch (Exception e) {
										System.out.println("no supplier email");
									}

									org.json.JSONObject TaxSubtotal = (org.json.JSONObject) TaxTotal.get("cac:TaxSubtotal");
									org.json.JSONObject TaxCategory = (org.json.JSONObject) TaxSubtotal.get("cac:TaxCategory");
									
									String payterms = "";
									try {
									org.json.JSONObject PaymentTerms = (org.json.JSONObject) invoice.get("cac:PaymentTerms");
									payterms = PaymentTerms.getString("cbc:Note");
									}catch (Exception e) {
										System.out.println("payment terms not available");
									}

									String vendname = PartyLegalEntity.getString("cbc:RegistrationName");
									String vendpeppolid = endpoint.getString("content");
									String currency = PayableAmount.getString("currencyID");
									String issuedate = invoice.getString("cbc:IssueDate");
									String countrycode = Country.getString("cbc:IdentificationCode");
									String companyregno = "";
									try{
										org.json.JSONObject PartyTaxScheme = (org.json.JSONObject) Party.get("cac:PartyTaxScheme");
										try {
											org.json.JSONObject compcontent = (org.json.JSONObject) PartyTaxScheme.get("cbc:CompanyID");
											companyregno = compcontent.getString("content");
										} catch (Exception e) {
											companyregno = PartyTaxScheme.getString("cbc:CompanyID");
										}
										
									} catch (Exception e) {
										try {
											org.json.JSONObject compcontent = (org.json.JSONObject) PartyLegalEntity.get("cbc:CompanyID");
											companyregno = compcontent.getString("content");
										} catch (Exception r) {
											companyregno = PartyLegalEntity.getString("cbc:CompanyID");
										}
										
									}
									String add1 = "";
									String add3 = "";
									String state = "";
									String zip = "";
									try {
										add1 = PostalAddress.getString("cbc:StreetName");
									}catch (Exception e) {
										System.out.println("StreetName not available");
									}
									try {
										add3 = PostalAddress.getString("cbc:AdditionalStreetName");
									}catch (Exception e) {
										System.out.println("AdditionalStreetName not available");
									}
									try {
										state = PostalAddress.getString("cbc:CityName");
									}catch (Exception e) {
										System.out.println("CityName not available");
									}
									try {
										zip = PostalAddress.getString("cbc:PostalZone");
									}catch (Exception e) {
										System.out.println("PostalZone not available");
									}
									//String state = PostalAddress.getString("cbc:CityName");
									//String zip = PostalAddress.getString("cbc:PostalZone");
									String otaxcode = TaxCategory.getString("cbc:ID");
									String countryname = new PlantMstDAO().getCOUNTRYNAME(countrycode);
									if(countryname == null) {
										org.json.JSONObject identification = (org.json.JSONObject) Country.get("cbc:IdentificationCode");
										countrycode = identification.getString("content");
										countryname = new PlantMstDAO().getCOUNTRYNAME(countrycode);
									}
									
									String dueDate = "";
									try {
										dueDate = invoice.getString("cbc:DueDate");
										dueDate = new DateUtils().getDateyyyymmdd(dueDate);
									}catch (Exception e) {
										// TODO: handle exception
									}
									
									org.json.JSONArray InvoiceLine = new JSONArray();
									try {
									InvoiceLine = (org.json.JSONArray) invoice.get("cac:InvoiceLine");
									}catch (Exception e) {
										org.json.JSONObject InvoiceLineObject = (org.json.JSONObject) invoice.get("cac:InvoiceLine");
										InvoiceLine.put(InvoiceLineObject);
									}

									/*
									 * for (int i = 0; i < InvoiceLine.length(); i++) { org.json.JSONObject
									 * explrObject = InvoiceLine.getJSONObject(i);
									 * 
									 * org.json.JSONObject Price = (org.json.JSONObject)
									 * explrObject.get("cac:Price"); org.json.JSONObject Item =
									 * (org.json.JSONObject) explrObject.get("cac:Item"); org.json.JSONObject
									 * InvoicedQuantity = (org.json.JSONObject)
									 * explrObject.get("cbc:InvoicedQuantity");
									 * 
									 * org.json.JSONObject PriceAmount = (org.json.JSONObject)
									 * Price.get("cbc:PriceAmount"); org.json.JSONObject ClassifiedTaxCategory =
									 * (org.json.JSONObject) Item.get("cac:ClassifiedTaxCategory");
									 * 
									 * String lnno =explrObject.getString("cbc:ID");; String unitcost
									 * =PriceAmount.getString("content"); String taxcode
									 * =ClassifiedTaxCategory.getString("cbc:ID"); String taxpercentage
									 * =ClassifiedTaxCategory.getString("cbc:Percent"); String item
									 * =Item.getString("cbc:Name"); String uom
									 * =InvoicedQuantity.getString("unitCode"); String qty
									 * =InvoicedQuantity.getString("content");
									 * 
									 * 
									 * 
									 * }
									 */

									List<Hashtable<String, String>> billDetInfoList = null;
									Hashtable<String, String> billDetInfo = null;
									UserTransaction ut = null;
									BillUtil billUtil = new BillUtil();
									RecvDetDAO recvDao = new RecvDetDAO();
									CustUtil custUtil = new CustUtil();
									ItemUtil itemUtil = new ItemUtil();
									boolean isAdded = false;

									try {

										
										boolean vedboolean = custUtil.isExistVendorName(vendname, plant);
										String vendno = "";
										if(vedboolean) {
											Hashtable vht = new Hashtable();
											vht.put("PLANT", plant);
											vht.put("VNAME", vendname);
											vendno = new VendMstDAO().getVendorNoByName(vht);
										}else {
											vendno = createsupplier(plant, username, vendname,vendpeppolid,companyregno,add1,add3,state,zip,countryname,supplieremail);
										}
										

										String billNo = invoice.getString("cbc:ID");
										String pono = "";
										String grno = "";
										String billDate = new DateUtils().getDateyyyymmdd(issuedate);
										//String dueDate = "";
										String payTerms = payterms;
										String empno = "";
										String itemRates = "0";
										String discountType = currency;
										String discountAccount = "";
										String adjustmentLabel = "Adjustment";
										String adjustment = "0.0";
										String totalAmount = PayableAmount.getString("content");
										String subTotal = LineExtensionAmount.getString("content");
										String billStatus = "Open";
										String shipRef = "";
										String refNum = "";
										String note = "";
										String taxamount = TaxAmount.getString("content");
										String isshtax = "0";
										String purchaseloc = "";
										String taxtreatment = "GST Registered";
										String sREVERSECHARGE = "";
										String sGOODSIMPORT = "";
										String currencyid = currency;
										String currencyuseqt = "1.0";
										String orddiscounttype = "%";
										String taxid = "";
										FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();

										if (otaxcode.equalsIgnoreCase("SR")) {
											List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
													.getCountryTaxTypes("INBOUND", countrycode, "STANDARD RATED");
											taxid = String.valueOf(finCountryTaxType.get(0).getID());
										} else if (otaxcode.equalsIgnoreCase("ZR")) {
											List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
													.getCountryTaxTypes("INBOUND", countrycode, "ZERO RATE");
											taxid = String.valueOf(finCountryTaxType.get(0).getID());
										} else if (otaxcode.equalsIgnoreCase("OS")) {
											List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
													.getCountryTaxTypes("INBOUND", countrycode, "OUT OF SCOPE");
											taxid = String.valueOf(finCountryTaxType.get(0).getID());
										} else if (otaxcode.equalsIgnoreCase("ES33")) {
											List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
													.getCountryTaxTypes("INBOUND", countrycode, "EXEMPT");
											taxid = String.valueOf(finCountryTaxType.get(0).getID());
										} else {
											List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
													.getCountryTaxTypes("INBOUND", countrycode, "EXEMPT");
											taxid = String.valueOf(finCountryTaxType.get(0).getID());
										}
										String isdiscounttax = "1";
										String isorderdiscounttax = "1";
										String discount = "0.0";
										String shippingCost = "0.0";
										String orderdiscount = "0.000";
										String gst = TaxCategory.getString("cbc:Percent");
										String projectid = "";
										String transportid = "0";
										String deductInv = "0";
										String billtype = "NON INVENTORY";

										billDetInfoList = new ArrayList<Hashtable<String, String>>();
										Hashtable billHdr = new Hashtable();
										billHdr.put("PLANT", plant);
										billHdr.put("VENDNO", vendno);
										billHdr.put("BILL", billNo);
										billHdr.put("PONO", pono);
										billHdr.put("GRNO", grno);
										billHdr.put("BILL_DATE", billDate);
										billHdr.put("DUE_DATE", dueDate);
										billHdr.put("PAYMENT_TERMS", payTerms);
										billHdr.put("EMPNO", empno);
										billHdr.put("ITEM_RATES", itemRates);
										billHdr.put("DISCOUNT_TYPE", discountType);
										billHdr.put("DISCOUNT_ACCOUNT", discountAccount);
										billHdr.put("ADJUSTMENT_LABEL", adjustmentLabel);
										billHdr.put("ADJUSTMENT", adjustment);
										billHdr.put("SUB_TOTAL", subTotal);
										billHdr.put("TOTAL_AMOUNT", totalAmount);
										billHdr.put("BILL_STATUS", billStatus);
										billHdr.put("SHIPMENT_CODE", shipRef);
										billHdr.put("REFERENCE_NUMBER", refNum);
										billHdr.put("NOTE", note);
										billHdr.put("CRAT", DateUtils.getDateTime());
										billHdr.put("CRBY", username);
										billHdr.put("UPAT", DateUtils.getDateTime());
										billHdr.put("CREDITNOTESSTATUS", "0");
										billHdr.put("TAXAMOUNT", taxamount);
										billHdr.put("ISSHIPPINGTAXABLE", isshtax);
										billHdr.put("PURCHASE_LOCATION", purchaseloc);
										billHdr.put("TAXTREATMENT", taxtreatment);
										billHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
										billHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
										billHdr.put(IDBConstants.CURRENCYID, currencyid);
										billHdr.put("CURRENCYUSEQT", currencyuseqt);
										billHdr.put("ORDERDISCOUNTTYPE", orddiscounttype);
										billHdr.put("TAXID", taxid);
										billHdr.put("ISDISCOUNTTAX", isdiscounttax);
										billHdr.put("ISORDERDISCOUNTTAX", isorderdiscounttax);
										billHdr.put("DISCOUNT", discount);
										billHdr.put("SHIPPINGCOST", shippingCost);
										billHdr.put("ORDER_DISCOUNT", orderdiscount);
										billHdr.put("INBOUND_GST", gst);
										billHdr.put("PROJECTID", projectid);
										billHdr.put("TRANSPORTID", transportid);
										billHdr.put("SHIPCONTACTNAME", "");
										billHdr.put("SHIPDESGINATION", "");
										billHdr.put("SHIPWORKPHONE", "");
										billHdr.put("SHIPHPNO", "");
										billHdr.put("SHIPEMAIL", "");
										billHdr.put("SHIPCOUNTRY", "");
										billHdr.put("SHIPADDR1", "");
										billHdr.put("SHIPADDR2", "");
										billHdr.put("SHIPADDR3", "");
										billHdr.put("SHIPADDR4", "");
										billHdr.put("SHIPSTATE", "");
										billHdr.put("SHIPZIP", "");
										billHdr.put("SHIPPINGID", "");
										billHdr.put("SHIPPINGCUSTOMER", "");
										billHdr.put("ORIGIN", "PEPPOL");
										billHdr.put("DEDUCT_INV", deductInv);
										billHdr.put("BILL_TYPE", billtype);

										ExpensesDAO expenseDao = new ExpensesDAO();
										CoaDAO coaDao = new CoaDAO();
										BillDAO itemCogsDao = new BillDAO();
										List<CostofgoodsLanded> landedCostLst = new ArrayList<>();
//														List<ItemCogs> lstCogs=new ArrayList<>();
										ItemMstDAO itemmstDao = new ItemMstDAO();
										Costofgoods costofGoods = new CostofgoodsImpl();
										String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
												.trim();
										double expenseAmt = 0.0;
										// Get Transaction object
										ut = DbBean.getUserTranaction();
										// Begin Transaction
										ut.begin();
										int billHdrId = billUtil.addBillHdr(billHdr, plant);
										if (billHdrId > 0) {
											for (int i = 0; i < InvoiceLine.length(); i++) {
												org.json.JSONObject explrObject = InvoiceLine.getJSONObject(i);

												org.json.JSONObject Price = (org.json.JSONObject) explrObject.get("cac:Price");
												org.json.JSONObject Item = (org.json.JSONObject) explrObject.get("cac:Item");
												org.json.JSONObject InvoicedQuantity = (org.json.JSONObject) explrObject
														.get("cbc:InvoicedQuantity");

												org.json.JSONObject PriceAmount = (org.json.JSONObject) Price.get("cbc:PriceAmount");
												org.json.JSONObject ClassifiedTaxCategory = (org.json.JSONObject) Item
														.get("cac:ClassifiedTaxCategory");

												String lnno = explrObject.getString("cbc:ID");
												;
												String unitcost = PriceAmount.getString("content");
												String taxcode = ClassifiedTaxCategory.getString("cbc:ID");
												String taxpercentage = ClassifiedTaxCategory.getString("cbc:Percent");
												String item = Item.getString("cbc:Name");
												String uom = InvoicedQuantity.getString("unitCode");
												String qty = InvoicedQuantity.getString("content");

												double lamount = Double.valueOf(unitcost) * Double.valueOf(qty);
												
												Hashtable uht = new Hashtable();
												uht.put(IDBConstants.PLANT, plant);
												uht.put(IDBConstants.UOMCODE, uom);
												if (!(new UomUtil().isExistsUom(uht))) // if the Item  exists already
												{
													createuom(plant, username, uom);
												}
												
												 if(!(itemUtil.isExistsItemMst(item,plant))) // if the item exists already
												    {
													 createproduct(plant, username, item, uom, unitcost);
												    }

												billDetInfo = new Hashtable<String, String>();
												billDetInfo.put("PLANT", plant);
												billDetInfo.put("LNNO", lnno);
												billDetInfo.put("BILLHDRID", Integer.toString(billHdrId));
												billDetInfo.put("ITEM", item);
												billDetInfo.put("ACCOUNT_NAME", "Inventory Asset");
												billDetInfo.put("QTY", qty);
												billDetInfo.put("COST", unitcost);
												billDetInfo.put("DISCOUNT", "0.0");
												billDetInfo.put("DISCOUNT_TYPE", currency);

												String taxdettype = "";
												double taxper = Double.valueOf(taxpercentage);
												if (taxcode.equalsIgnoreCase("SR")) {
													taxdettype = "STANDARD RATED [" + taxper + "%]";
												} else if (taxcode.equalsIgnoreCase("ZR")) {
													taxdettype = "ZERO RATE [0.0%]";
												} else if (taxcode.equalsIgnoreCase("OS")) {
													taxdettype = "OUT OF SCOPE";
												} else if (taxcode.equalsIgnoreCase("ES33")) {
													taxdettype = "EXEMPT";
												} else {
													taxdettype = "ZERO RATE [0.0%]";
												}

												billDetInfo.put("Tax_Type", taxdettype);
												billDetInfo.put("Amount", String.valueOf(lamount));
												billDetInfo.put("LANDED_COST", "0");
												billDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
												billDetInfo.put("LOC", "");
												billDetInfo.put("BATCH", "NOBATCH");
												billDetInfo.put("UOM", uom);
												billDetInfo.put("CRAT", DateUtils.getDateTime());
												billDetInfo.put("CRBY", username);
												billDetInfo.put("UPAT", DateUtils.getDateTime());
												billDetInfoList.add(billDetInfo);

											}
											isAdded = billUtil.addMultipleBillDet(billDetInfoList, plant);
											
											if(isAdded) {
												try {
													org.json.JSONObject AdditionalDocumentReference = (org.json.JSONObject) invoice.get("cac:AdditionalDocumentReference");
													org.json.JSONObject Attachment = (org.json.JSONObject) AdditionalDocumentReference.get("cac:Attachment");
													org.json.JSONObject EmbeddedDocument = (org.json.JSONObject) Attachment.get("cbc:EmbeddedDocumentBinaryObject");
													String crntImage = EmbeddedDocument.getString("content");
													String dname = EmbeddedDocument.getString("filename");
													String dtype = EmbeddedDocument.getString("mimeCode");
													savedocument(plant,crntImage, dname,vendno,invoice.getString("cbc:ID"),dtype,username,billHdrId);
													}catch (Exception e) {
														System.out.println(e.getMessage());
													}		
											}
											
											if (isAdded) {

												System.out.println("billStatus" + billStatus);
												if (!billStatus.equalsIgnoreCase("Draft")) {
													// Journal Entry
													JournalHeader journalHead = new JournalHeader();
													journalHead.setPLANT(plant);
													journalHead.setJOURNAL_DATE(billDate);
													journalHead.setJOURNAL_STATUS("PUBLISHED");
													journalHead.setJOURNAL_TYPE("Cash");
													journalHead.setCURRENCYID(curency);
													journalHead.setTRANSACTION_TYPE("BILL");
													journalHead.setTRANSACTION_ID(billNo);
													journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
													// journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
													journalHead.setCRAT(DateUtils.getDateTime());
													journalHead.setCRBY(username);

													List<JournalDetail> journalDetails = new ArrayList<>();
													List<JournalDetail> journalReversalList = new ArrayList<>();
													CoaDAO coaDAO = new CoaDAO();
													VendMstDAO vendorDAO = new VendMstDAO();
													ItemMstDAO itemMstDAO = new ItemMstDAO();
													Double totalItemNetWeight = 0.00;
													Double totalline = 0.00;
													for (Map billDet : billDetInfoList) {
														Double quantity = Double.parseDouble(billDet.get("QTY").toString());
														totalline++;
														String netWeight = itemMstDAO.getItemNetWeight(plant,
																billDet.get("ITEM").toString());
//																		TODO : Ravindra - Get verified
														if (netWeight == null || "".equals(netWeight)) {
															netWeight = "0";
														}
														Double Netweight = quantity * Double.parseDouble(netWeight);
														totalItemNetWeight += Netweight;
														System.out.println("TotalNetWeight:" + totalItemNetWeight);

														JournalDetail journalDetail = new JournalDetail();
														journalDetail.setPLANT(plant);
														JSONObject coaJson = coaDAO.getCOAByName(plant,
																(String) billDet.get("ACCOUNT_NAME"));
														System.out.println("Json" + coaJson.toString());
														journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
														journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
														if (!orddiscounttype.toString().equalsIgnoreCase("%")) {
															journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString())
																	- Double.parseDouble(orderdiscount.toString())
																			/ billDetInfoList.size());
														} else {
															Double jodamt = (Double.parseDouble(billDet.get("Amount").toString()) / 100)
																	* Double.parseDouble(orderdiscount.toString());
															journalDetail.setDEBITS(
																	Double.parseDouble(billDet.get("Amount").toString()) - jodamt);
														}

														boolean isLoop = false;
														if (journalDetails.size() > 0) {
															int i = 0;
															for (JournalDetail journal : journalDetails) {
																int accountId = journal.getACCOUNT_ID();
																if (accountId == journalDetail.getACCOUNT_ID()) {
																	isLoop = true;
																	Double sumDetit = journal.getDEBITS() + journalDetail.getDEBITS();
																	journalDetail.setDEBITS(sumDetit);
																	journalDetails.set(i, journalDetail);
																	break;
																}
																i++;

															}
															if (isLoop == false) {
																journalDetails.add(journalDetail);
															}
														} else {
															journalDetails.add(journalDetail);
														}

													}

													JournalDetail journalDetail_1 = new JournalDetail();
													journalDetail_1.setPLANT(plant);
													JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
													if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
														JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
														if (!vendorJson.isEmpty()) {
															coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VNAME"));
															if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
																coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")
																		+ "-" + vendorJson.getString("VNAME"));
															}

														}
													}
													if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

													} else {
														journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
														if (coaJson1.getString("account_name") != null) {
															journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
														}
														// journalDetail_1.setACCOUNT_NAME((String) vendno);
														journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
														journalDetails.add(journalDetail_1);
													}

													Double taxAmountFrom = Double.parseDouble(taxamount);
													if (taxAmountFrom > 0) {
														JournalDetail journalDetail_2 = new JournalDetail();
														journalDetail_2.setPLANT(plant);
														// JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
														// journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
														// journalDetail_2.setACCOUNT_NAME("VAT Input");

														MasterDAO masterDAO = new MasterDAO();
														String planttaxtype = masterDAO.GetTaxType(plant);

														if (planttaxtype.equalsIgnoreCase("TAX")) {
															JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
															journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
															journalDetail_2.setACCOUNT_NAME("VAT Input");
														} else if (planttaxtype.equalsIgnoreCase("GST")) {
															JSONObject coaJson2 = coaDAO.getCOAByName(plant, "GST Receivable");
															journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
															journalDetail_2.setACCOUNT_NAME("GST Receivable");
														} else if (planttaxtype.equalsIgnoreCase("VAT")) {
															JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
															journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
															journalDetail_2.setACCOUNT_NAME("VAT Input");
														} else {
															JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
															journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
															journalDetail_2.setACCOUNT_NAME("VAT Input");
														}

														journalDetail_2.setDEBITS(taxAmountFrom);
														journalDetails.add(journalDetail_2);
													}

													Double discountFrom = Double.parseDouble(discount);
													Double orderDiscountFrom = 0.00;
													if (!orderdiscount.isEmpty()) {
														orderDiscountFrom = Double.parseDouble(orderdiscount);
														orderDiscountFrom = (Double.parseDouble(subTotal) * orderDiscountFrom) / 100;
													}

													if (discountFrom > 0 || orderDiscountFrom > 0) {
														if (!discountType.isEmpty()) {
															if (discountType.equalsIgnoreCase("%")) {
																Double subTotalAfterOrderDiscount = Double.parseDouble(subTotal)
																		- orderDiscountFrom;
																discountFrom = (subTotalAfterOrderDiscount * discountFrom) / 100;
															}
														}
														discountFrom = discountFrom + orderDiscountFrom;
														JournalDetail journalDetail_3 = new JournalDetail();
														journalDetail_3.setPLANT(plant);
														JSONObject coaJson3 = coaDAO.getCOAByName(plant, "Discount Received");
														journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
														journalDetail_3.setACCOUNT_NAME("Discount Received");
														journalDetail_3.setCREDITS(discountFrom);
														journalDetails.add(journalDetail_3);
													}

													if (!shippingCost.isEmpty()) {
														Double shippingCostFrom = Double.parseDouble(shippingCost);
														if (shippingCostFrom > 0) {
															JournalDetail journalDetail_4 = new JournalDetail();
															journalDetail_4.setPLANT(plant);
															JSONObject coaJson4 = coaDAO.getCOAByName(plant,
																	"Inward freight & shipping");
															journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
															journalDetail_4.setACCOUNT_NAME("Inward freight & shipping");
															journalDetail_4.setDEBITS(shippingCostFrom);
															journalDetails.add(journalDetail_4);

															for (Map billDet : billDetInfoList) {
																Double quantity = Double.parseDouble(billDet.get("QTY").toString());
																String netWeight = itemMstDAO.getItemNetWeight(plant,
																		billDet.get("ITEM").toString());
																Double Netweight = quantity * Double.parseDouble(netWeight);
																Double calculatedShippingCost = 0.0;
																if (totalItemNetWeight > 0) {
																	if (Netweight > 0) {
																		calculatedShippingCost = (shippingCostFrom * Netweight)
																				/ totalItemNetWeight;
																	} else {
																		calculatedShippingCost = 0.00;
																	}
																} else {
																	calculatedShippingCost = shippingCostFrom / totalline;
																}
																System.out.println("calculatedShippingCost:" + calculatedShippingCost);

																JournalDetail journalDetail_5 = new JournalDetail();
																journalDetail_5.setPLANT(plant);
																JSONObject coaJson5 = coaDAO.getCOAByName(plant,
																		(String) billDet.get("ACCOUNT_NAME"));
																journalDetail_5
																		.setACCOUNT_ID(Integer.parseInt(coaJson5.getString("id")));
																journalDetail_5.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
																journalDetail_5.setDEBITS(calculatedShippingCost);
																boolean isLoop = false;
																if (journalReversalList.size() > 0) {
																	int i = 0;
																	for (JournalDetail journal : journalReversalList) {
																		int accountId = journal.getACCOUNT_ID();
																		if (accountId == journalDetail_5.getACCOUNT_ID()) {
																			isLoop = true;
																			Double sumDetit = journal.getDEBITS()
																					+ journalDetail_5.getDEBITS();
																			journalDetail_5.setDEBITS(sumDetit);
																			journalReversalList.set(i, journalDetail_5);
																			break;
																		}
																		i++;

																	}
																	if (isLoop == false) {
																		journalReversalList.add(journalDetail_5);
																	}
																} else {
																	journalReversalList.add(journalDetail_5);
																}
															}

															JournalDetail journalDetail_6 = new JournalDetail();
															journalDetail_6.setPLANT(plant);
															JSONObject coaJson6 = coaDAO.getCOAByName(plant,
																	"Inward freight & shipping");
															journalDetail_6.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
															journalDetail_6.setACCOUNT_NAME("Inward freight & shipping");
															journalDetail_6.setCREDITS(shippingCostFrom);
															journalReversalList.add(journalDetail_6);
														}

													}
													if (!adjustment.isEmpty()) {
														Double adjustFrom = Double.parseDouble(adjustment);
														if (adjustFrom > 0) {
															JournalDetail journalDetail_7 = new JournalDetail();
															journalDetail_7.setPLANT(plant);
															JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
															journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
															journalDetail_7.setACCOUNT_NAME("Adjustment");
															journalDetail_7.setDEBITS(adjustFrom);
															journalDetails.add(journalDetail_7);
														} else if (adjustFrom < 0) {
															JournalDetail journalDetail_7 = new JournalDetail();
															journalDetail_7.setPLANT(plant);
															JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
															journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
															journalDetail_7.setACCOUNT_NAME("Adjustment");
															adjustFrom = Math.abs(adjustFrom);
															journalDetail_7.setCREDITS(adjustFrom);
															journalDetails.add(journalDetail_7);
														}
													}

													Journal journal = new Journal();
													Double totalDebitAmount = 0.00;
													for (JournalDetail jourDet : journalDetails) {
														totalDebitAmount = totalDebitAmount + jourDet.getDEBITS();
													}
													journalHead.setTOTAL_AMOUNT(totalDebitAmount);
													journal.setJournalHeader(journalHead);
													journal.setJournalDetails(journalDetails);
													JournalService journalService = new JournalEntry();
													journalService.addJournal(journal, username);
													Hashtable jhtMovHis = new Hashtable();
													jhtMovHis.put(IDBConstants.PLANT, plant);
													jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
													jhtMovHis.put(IDBConstants.TRAN_DATE,
															DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
													jhtMovHis.put(IDBConstants.ITEM, "");
													jhtMovHis.put(IDBConstants.QTY, "0.0");
													jhtMovHis.put("RECID", "");
													jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
															journal.getJournalHeader().getTRANSACTION_TYPE() + " "
																	+ journal.getJournalHeader().getTRANSACTION_ID());
													jhtMovHis.put(IDBConstants.CREATED_BY, username);
													jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
													jhtMovHis.put("REMARKS", "");
													movHisDao.insertIntoMovHis(jhtMovHis);

													List<JournalDetail> expensepo_journaldetails = new ArrayList<>();

													if (journalReversalList.size() > 0) {
														JournalHeader journalReversalHead = journalHead;
														Double totalDebitReversal = 0.00;
														for (JournalDetail journDet : journalReversalList) {
															totalDebitReversal = totalDebitReversal + journDet.getDEBITS();

														}
														journalReversalHead.setTOTAL_AMOUNT(totalDebitReversal);
														Journal journal_1 = new Journal();
														journalReversalHead.setTRANSACTION_TYPE("BILL_REVERSAL");
														journal_1.setJournalHeader(journalReversalHead);
														journal_1.setJournalDetails(journalReversalList);
														journalService.addJournal(journal_1, username);
														Hashtable jhtMovHis1 = new Hashtable();
														jhtMovHis1.put(IDBConstants.PLANT, plant);
														jhtMovHis1.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
														jhtMovHis1.put(IDBConstants.TRAN_DATE,
																DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
														jhtMovHis1.put(IDBConstants.ITEM, "");
														jhtMovHis1.put(IDBConstants.QTY, "0.0");
														jhtMovHis1.put("RECID", "");
														jhtMovHis1.put(IDBConstants.MOVHIS_ORDNUM,
																journal_1.getJournalHeader().getTRANSACTION_TYPE() + " "
																		+ journal_1.getJournalHeader().getTRANSACTION_ID());
														jhtMovHis1.put(IDBConstants.CREATED_BY, username);
														jhtMovHis1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
														jhtMovHis1.put("REMARKS", "");
														movHisDao.insertIntoMovHis(jhtMovHis1);
													}
												}
											}

										}

										if (isAdded) {
											for (int i = 0; i < InvoiceLine.length(); i++) {
												org.json.JSONObject explrObject = InvoiceLine.getJSONObject(i);

												org.json.JSONObject Price = (org.json.JSONObject) explrObject.get("cac:Price");
												org.json.JSONObject Item = (org.json.JSONObject) explrObject.get("cac:Item");
												org.json.JSONObject InvoicedQuantity = (org.json.JSONObject) explrObject
														.get("cbc:InvoicedQuantity");

												org.json.JSONObject PriceAmount = (org.json.JSONObject) Price.get("cbc:PriceAmount");
												org.json.JSONObject ClassifiedTaxCategory = (org.json.JSONObject) Item
														.get("cac:ClassifiedTaxCategory");

												String item = Item.getString("cbc:Name");
												String qty = InvoicedQuantity.getString("content");

												Hashtable htMovHisq = new Hashtable();
												htMovHisq.clear();
												htMovHisq.put(IDBConstants.PLANT, plant);
												htMovHisq.put("DIRTYPE", TransactionConstants.CREATE_BILL);
												htMovHisq.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billDate));
												htMovHisq.put(IDBConstants.ITEM, item);
												String billqty = qty;
												htMovHisq.put(IDBConstants.QTY, billqty);
												htMovHisq.put("RECID", "");
												htMovHisq.put(IDBConstants.MOVHIS_ORDNUM, billNo);
												htMovHisq.put(IDBConstants.CREATED_BY, username);
												htMovHisq.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												htMovHisq.put("REMARKS", pono + "," + grno + "," + refNum);
												isAdded = movHisDao.insertIntoMovHis(htMovHisq); // Insert MOVHIS
											}
										}

										// Added by Abhilash to handle COGS
										for (Map billDet : billDetInfoList) {

											List pendingCogsInvoiceDetails = new InvoiceDAO()
													.invoiceWoCOGS((String) billDet.get("ITEM"), plant);
											if (pendingCogsInvoiceDetails.size() > 0) {
												for (int i = 0; i < pendingCogsInvoiceDetails.size(); i++) {
													Double totalCostofGoodSold = 0.00;
													CoaDAO coaDAO = new CoaDAO();
													Journal journal = new Journal();
													List<JournalDetail> journalDetails = new ArrayList<>();
													Map pendingCogsInvoice = (Map) pendingCogsInvoiceDetails.get(i);

													Map invDetail = new InvMstDAO()
															.getInvDataByProduct((String) pendingCogsInvoice.get("ITEM"), plant);
													double inv_qty = 0, bill_qty = 0, unbill_qty = 0, net_bill_qty = 0,
															invoiced_qty = 0, invoice_quantity = 0, quantity = 0;
													inv_qty = Double.parseDouble((String) invDetail.get("INV_QTY"));
													bill_qty = Double.parseDouble((String) invDetail.get("BILL_QTY"));
													unbill_qty = Double.parseDouble((String) invDetail.get("UNBILL_QTY"));
													invoiced_qty = Double.parseDouble((String) invDetail.get("INVOICE_QTY"));
													invoice_quantity = Double.parseDouble((String) pendingCogsInvoice.get("QTY"));
													quantity = Double.parseDouble(pendingCogsInvoice.get("QTY").toString());

													bill_qty = bill_qty - invoiced_qty;
													net_bill_qty = bill_qty + unbill_qty;

													if ((net_bill_qty != inv_qty) && (bill_qty >= invoice_quantity)) {
														ArrayList invQryList;
														Hashtable ht_cog = new Hashtable();
														ht_cog.put("a.PLANT", plant);
														ht_cog.put("a.ITEM", (String) billDet.get("ITEM"));
														invQryList = new InvUtil().getAverageCost(ht_cog, plant,
																(String) billDet.get("ITEM"), curency, curency);
														if (invQryList != null) {
															if (!invQryList.isEmpty()) {
																Map lineArr = (Map) invQryList.get(0);
																String avg = StrUtils.addZeroes(
																		Double.parseDouble((String) lineArr.get("AVERAGE_COST")), "2");
																totalCostofGoodSold += Double.parseDouble(avg) * (quantity);
															}
														}

														JournalService journalService = new JournalEntry();
														Journal journalCOG = journalService.getJournalByTransactionId(plant,
																(String) pendingCogsInvoice.get("INVOICE"), "COSTOFGOODSOLD");

														if (journalCOG.getJournalHeader() != null) {
															if (journalCOG.getJournalHeader().getID() > 0) {
																totalCostofGoodSold += journalCOG.getJournalHeader().getTOTAL_AMOUNT();
															}
														}

														JournalHeader journalHead = new JournalHeader();
														journalHead.setPLANT(plant);
														journalHead.setJOURNAL_DATE(DateUtils.getDate());
														journalHead.setJOURNAL_STATUS("PUBLISHED");
														journalHead.setJOURNAL_TYPE("Cash");
														journalHead.setCURRENCYID(curency);
														journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
														journalHead.setTRANSACTION_ID((String) pendingCogsInvoice.get("INVOICE"));
														journalHead.setSUB_TOTAL(totalCostofGoodSold);
														journalHead.setCRAT(DateUtils.getDateTime());
														journalHead.setCRBY(username);

														JournalDetail journalDetail_InvAsset = new JournalDetail();
														journalDetail_InvAsset.setPLANT(plant);
														JSONObject coaJson7 = coaDAO.getCOAByName(plant, "Inventory Asset");
														System.out.println("Json" + coaJson7.toString());
														journalDetail_InvAsset
																.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
														journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
														journalDetail_InvAsset.setCREDITS(totalCostofGoodSold);
														journalDetails.add(journalDetail_InvAsset);

														JournalDetail journalDetail_COG = new JournalDetail();
														journalDetail_COG.setPLANT(plant);
														JSONObject coaJson8 = coaDAO.getCOAByName(plant, "Cost of goods sold");
														System.out.println("Json" + coaJson8.toString());
														journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
														journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
														journalDetail_COG.setDEBITS(totalCostofGoodSold);
														journalDetails.add(journalDetail_COG);

														journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
														journal.setJournalHeader(journalHead);
														journal.setJournalDetails(journalDetails);

														if (journalCOG.getJournalHeader() != null) {
															if (journalCOG.getJournalHeader().getID() > 0) {
																journalHead.setID(journalCOG.getJournalHeader().getID());
																journalService.updateJournal(journal, username);
																Hashtable jhtMovHis = new Hashtable();
																jhtMovHis.put(IDBConstants.PLANT, plant);
																jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);
																jhtMovHis.put(IDBConstants.TRAN_DATE,
																		DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
																jhtMovHis.put(IDBConstants.ITEM, "");
																jhtMovHis.put(IDBConstants.QTY, "0.0");
																jhtMovHis.put("RECID", "");
																jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
																		journal.getJournalHeader().getTRANSACTION_TYPE() + " "
																				+ journal.getJournalHeader().getTRANSACTION_ID());
																jhtMovHis.put(IDBConstants.CREATED_BY, username);
																jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
																jhtMovHis.put("REMARKS", "");
																movHisDao.insertIntoMovHis(jhtMovHis);
															} else {
																journalService.addJournal(journal, username);
																Hashtable jhtMovHis = new Hashtable();
																jhtMovHis.put(IDBConstants.PLANT, plant);
																jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
																jhtMovHis.put(IDBConstants.TRAN_DATE,
																		DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
																jhtMovHis.put(IDBConstants.ITEM, "");
																jhtMovHis.put(IDBConstants.QTY, "0.0");
																jhtMovHis.put("RECID", "");
																jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
																		journal.getJournalHeader().getTRANSACTION_TYPE() + " "
																				+ journal.getJournalHeader().getTRANSACTION_ID());
																jhtMovHis.put(IDBConstants.CREATED_BY, username);
																jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
																jhtMovHis.put("REMARKS", "");
																movHisDao.insertIntoMovHis(jhtMovHis);
															}
														}
														new InvoiceDAO().update_is_cogs_set(
																(String) pendingCogsInvoice.get("INVOICEHDRID"),
																(String) pendingCogsInvoice.get("LNNO"),
																(String) pendingCogsInvoice.get("ITEM"), plant);
													}
												}
											}
										}
										
										if(isAdded) {
											peppolrecvdata.setBILL_STATUS((short) 1);
											peppolrecvdata.setBILLNO(billNo);
											new PeppolReceivedDataDAO().updatePeppolReceivedData(peppolrecvdata, username, peppolrecvdata.getID());
										}

										if (isAdded) {
											DbBean.CommitTran(ut);
											
											result = "Bill created successfully!";
										} else {
											DbBean.RollbackTran(ut);
											result = "Bill not created";
										}

									} catch (Exception e) {
										DbBean.RollbackTran(ut);
										e.printStackTrace();

									}

								} else {
									result = "Bill not created";
								}

							} catch (Exception e) {
								e.printStackTrace();
								result = "Bill not created";
							}
							

						
						}

					}

					msg = "Invoice Data processed";
				} else {
					System.out.println("POST request not worked");
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();

					// print result
					System.out.println(response.toString());
					msg = "Unable To Process";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			response.sendRedirect("../peppol/peppolpurchase");
		}

		if (action.equalsIgnoreCase("cxml")) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.newDocument();

				PlantMstUtil plantmstutil = new PlantMstUtil();
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
				String countrycode = "", taxtype = "", companyGstNo = "";
				String qty = "", unitprice = "", item = "", lnno = "", uom = "";
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String invoiceId = StrUtils.fString(request.getParameter("ID"));

				Hashtable ht = new Hashtable();
				ht.put("ID", invoiceId);
				ht.put("PLANT", plant);
				List invoiceHdrList = new InvoiceUtil().getInvoiceHdrById(ht);
				Map invoiceHdrmap = (Map) invoiceHdrList.get(0);
				String invoice_no = String.valueOf(invoiceHdrmap.get("INVOICE").toString());
				String invoice_date = String.valueOf(invoiceHdrmap.get("INVOICE_DATE").toString());
				String invoice_due_date = String.valueOf(invoiceHdrmap.get("DUE_DATE").toString());
				String currency_id = String.valueOf(invoiceHdrmap.get("CURRENCYID").toString());
				String shipping_customer = String.valueOf(invoiceHdrmap.get("SHIPPINGCUSTOMER").toString());
				String payment_terms = String.valueOf(invoiceHdrmap.get("PAYMENT_TERMS").toString());
				String outbound_gst = String.valueOf(invoiceHdrmap.get("OUTBOUD_GST").toString());
				String sub_total = String.valueOf(invoiceHdrmap.get("SUB_TOTAL").toString());
				String tax_amount = String.valueOf(invoiceHdrmap.get("TAXAMOUNT").toString());
				String discount = String.valueOf(invoiceHdrmap.get("DISCOUNT").toString());
				String shippingcost = String.valueOf(invoiceHdrmap.get("SHIPPINGCOST").toString());
				String adjustment = String.valueOf(invoiceHdrmap.get("ADJUSTMENT").toString());
				String total_amount = String.valueOf(invoiceHdrmap.get("TOTAL_AMOUNT").toString());
				String item_rates = String.valueOf(invoiceHdrmap.get("ITEM_RATES").toString());
				String invtaxtype = "0";
				String suppliername = new PlantMstDAO().getcmpyname(plant);
				String supplieremail = new PlantMstDAO().getcmpyEmail(plant);
				String customername = new CustMstDAO().getCustName(plant,String.valueOf(invoiceHdrmap.get("CUSTNO").toString()));
				String customeremail = new CustMstDAO().getCustEmail(plant,String.valueOf(invoiceHdrmap.get("CUSTNO").toString()));
				String referenceno = String.valueOf(invoiceHdrmap.get("JobNum").toString());
				if(referenceno.length() == 0) {
					referenceno= new DateUtils().getUniqueId();
				}
				if(payment_terms.length() == 0) {
					payment_terms = "30 DAYS";
				}
				
				if (Integer.valueOf(invoiceHdrmap.get("TAXID").toString()) == 0) {
					invtaxtype = "0";
				} else {
					FinCountryTaxType fintax = finCountryTaxTypeDAO
							.getCountryTaxTypesByid(Integer.valueOf(invoiceHdrmap.get("TAXID").toString()));
					invtaxtype = fintax.getTAXTYPE();
				}

				String senderid = plantmstutil.getPeppolId(plant);
				String reciverid = customerBeanDAO.getPeppolId(String.valueOf(invoiceHdrmap.get("CUSTNO").toString()),
						plant);

				Double exclusiveAmount = 0.0, inclusiveAmount = 0.0;
				Double Exc_IncSubtotalAmount = (Double.parseDouble(sub_total) * 100)
						/ (Double.parseDouble(outbound_gst) + 100);
				Double Exc_IncTaxamount = (Exc_IncSubtotalAmount - Double.parseDouble(discount))
						* (Double.parseDouble(outbound_gst) / (100));
				Double Exc_IncTotalamount = (Exc_IncSubtotalAmount - Double.parseDouble(discount) + Exc_IncTaxamount
						+ Double.parseDouble(shippingcost) + Double.parseDouble(adjustment));
				Double Exc_IncAmount = Exc_IncSubtotalAmount - Double.parseDouble(discount) + Exc_IncTaxamount
						+ Double.parseDouble(shippingcost) + Double.parseDouble(adjustment);
				exclusiveAmount = Double.parseDouble(total_amount);
				inclusiveAmount = Exc_IncAmount;

				if (Integer.valueOf(item_rates) == 1) {
					Exc_IncSubtotalAmount = (Double.parseDouble(sub_total) * Double.parseDouble(outbound_gst) / (100)
							+ Double.parseDouble(sub_total));
					Exc_IncTaxamount = (Exc_IncSubtotalAmount - Double.parseDouble(discount))
							* (Double.parseDouble(outbound_gst) / (100));
					Exc_IncTotalamount = (Exc_IncSubtotalAmount - Double.parseDouble(discount) + Exc_IncTaxamount
							+ Double.parseDouble(shippingcost) + Double.parseDouble(adjustment));
					Exc_IncAmount = Exc_IncSubtotalAmount - Double.parseDouble(discount) + Exc_IncTaxamount
							+ Double.parseDouble(shippingcost) + Double.parseDouble(adjustment);
					exclusiveAmount = Exc_IncAmount;
					inclusiveAmount = Double.parseDouble(total_amount);
					;
				}

				String inclusiveAmt = StrUtils.addZeroes(inclusiveAmount, numberOfDecimal);
				String exclusiveAmt = StrUtils.addZeroes(exclusiveAmount, numberOfDecimal);
				total_amount = StrUtils.addZeroes(Double.parseDouble(total_amount), numberOfDecimal);
				tax_amount = StrUtils.addZeroes(Double.parseDouble(tax_amount), numberOfDecimal);
				// String subtotalamt =
				// StrUtils.addZeroes(String.valueOf((Integer.valueOf(total_amount)-Integer.valueOf(tax_amount))),
				// numberOfDecimal);

				Hashtable htDet = new Hashtable();
				htDet.put("INVOICEHDRID", invoiceId);
				htDet.put("PLANT", plant);
				List invoicedet = new InvoiceDAO().getInvoiceDetByHdrId(htDet);
				int i = 0;
				String a1 ="";
				String a3 = "";
				String STATE = "";
				String ZIP = "";
				List viewlistQry = plantmstutil.getPlantMstDetails(plant);
				for (int k = 0; k < viewlistQry.size(); k++) {
					Map map = (Map) viewlistQry.get(k);
					companyGstNo = StrUtils.fString((String) map.get("companyregnumber"));
					taxtype = StrUtils.fString((String) map.get("TAXBYLABEL"));
					countrycode = StrUtils.fString((String) map.get("COUNTRY_CODE"));
					
					String ADD1 = (String) map.get("ADD1");
					String ADD2 = (String) map.get("ADD2");
					String ADD3 = (String) map.get("ADD3");
					String ADD4 = (String) map.get("ADD4");
					
					STATE = (String) map.get("STATE");
					ZIP = (String) map.get("ZIP");
					a1 = ADD1 + " " + ADD2;
					a3 = ADD3 + " " + ADD4;
				}
				
				/*String country = "";
				ArrayList currencycode = new CountryNCurrencyDAO().getCountryCodeByCountry(plant, country);
				for (int j = 0; j < currencycode.size(); j++) {
					Map arrCustLine = (Map) currencycode.get(j);
					countrycode = (String) arrCustLine.get("COUNTRY_CODE");
				}*/


				// root element
				Element StandardBusinessDocument = doc.createElement("StandardBusinessDocument");
				Attr attrType = doc.createAttribute("xmlns:xs");
				attrType.setValue("http://www.w3.org/2001/XMLSchema");
				StandardBusinessDocument.setAttributeNode(attrType);
				attrType = doc.createAttribute("xmlns");
				attrType.setValue("http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader");
				StandardBusinessDocument.setAttributeNode(attrType);

				doc.appendChild(StandardBusinessDocument);

				Element StandardBusinessDocumentHeader = doc.createElement("StandardBusinessDocumentHeader");

				Element HeaderVersion = doc.createElement("HeaderVersion");
				HeaderVersion.appendChild(doc.createTextNode("1.0"));
				StandardBusinessDocumentHeader.appendChild(HeaderVersion);

				Element Sender = doc.createElement("Sender");

				Element Identifier = doc.createElement("Identifier");
				attrType = doc.createAttribute("Authority");
				attrType.setValue("iso6523-actorid-upis");
				Identifier.setAttributeNode(attrType);
				// Identifier.appendChild(doc.createTextNode("0195:SGUEN1234519H"));
				Identifier.appendChild(doc.createTextNode(senderid));
				Sender.appendChild(Identifier);

				StandardBusinessDocumentHeader.appendChild(Sender);

				Element Receiver = doc.createElement("Receiver");

				Element rIdentifier = doc.createElement("Identifier");
				attrType = doc.createAttribute("Authority");
				attrType.setValue("iso6523-actorid-upis");
				rIdentifier.setAttributeNode(attrType);
				// rIdentifier.appendChild(doc.createTextNode("0195:SGUEN9876543M"));
				rIdentifier.appendChild(doc.createTextNode(reciverid));
				Receiver.appendChild(rIdentifier);

				StandardBusinessDocumentHeader.appendChild(Receiver);

				Element DocumentIdentification = doc.createElement("DocumentIdentification");
				Element Standard = doc.createElement("Standard");
				Standard.appendChild(doc.createTextNode("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2"));
				DocumentIdentification.appendChild(Standard);

				Element TypeVersion = doc.createElement("TypeVersion");
				TypeVersion.appendChild(doc.createTextNode("2.1"));
				DocumentIdentification.appendChild(TypeVersion);

				Element InstanceIdentifier = doc.createElement("InstanceIdentifier");
				InstanceIdentifier.appendChild(doc.createTextNode("b0f952ba-df85-450a-b543-8fddc362c277"));
				DocumentIdentification.appendChild(InstanceIdentifier);

				Element Type = doc.createElement("Type");
				Type.appendChild(doc.createTextNode("Invoice"));
				DocumentIdentification.appendChild(Type);

				Element CreationDateAndTime = doc.createElement("CreationDateAndTime");
				CreationDateAndTime.appendChild(doc.createTextNode("2022-03-16T12:00:09+08:00"));
				DocumentIdentification.appendChild(CreationDateAndTime);

				StandardBusinessDocumentHeader.appendChild(DocumentIdentification);

				Element BusinessScope = doc.createElement("BusinessScope");

				Element Scope1 = doc.createElement("Scope");
				Element Type1 = doc.createElement("Type");
				Type1.appendChild(doc.createTextNode("DOCUMENTID"));
				Scope1.appendChild(Type1);
				Element InstanceIdentifier1 = doc.createElement("InstanceIdentifier");
				InstanceIdentifier1.appendChild(doc.createTextNode(
						"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#conformant#urn:fdc:peppol.eu:2017:poacc:billing:international:sg:3.0::2.1"));
				Scope1.appendChild(InstanceIdentifier1);
				BusinessScope.appendChild(Scope1);

				Element Scope2 = doc.createElement("Scope");
				Element Type2 = doc.createElement("Type");
				Type2.appendChild(doc.createTextNode("PROCESSID"));
				Scope2.appendChild(Type2);
				Element InstanceIdentifier2 = doc.createElement("InstanceIdentifier");
				InstanceIdentifier2.appendChild(doc.createTextNode("urn:fdc:peppol.eu:2017:poacc:billing:01:1.0"));
				Scope2.appendChild(InstanceIdentifier2);
				BusinessScope.appendChild(Scope2);

				StandardBusinessDocumentHeader.appendChild(BusinessScope);

				StandardBusinessDocument.appendChild(StandardBusinessDocumentHeader);

				Element rootElement = doc.createElement("Invoice");

				attrType = doc.createAttribute("xmlns:xsd");
				attrType.setValue("http://www.w3.org/2001/XMLSchema");
				rootElement.setAttributeNode(attrType);
				attrType = doc.createAttribute("xmlns:xsi");
				attrType.setValue("http://www.w3.org/2001/XMLSchema-instance");
				rootElement.setAttributeNode(attrType);
				attrType = doc.createAttribute("xmlns:cac");
				attrType.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
				rootElement.setAttributeNode(attrType);
				attrType = doc.createAttribute("xmlns:cbc");
				attrType.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
				rootElement.setAttributeNode(attrType);
				attrType = doc.createAttribute("xmlns");
				attrType.setValue("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
				rootElement.setAttributeNode(attrType);

				Element CustomizationID = doc.createElement("cbc:CustomizationID");
				CustomizationID.appendChild(doc.createTextNode(
						"urn:cen.eu:en16931:2017#conformant#urn:fdc:peppol.eu:2017:poacc:billing:international:sg:3.0"));
				rootElement.appendChild(CustomizationID);

				Element ProfileID = doc.createElement("cbc:ProfileID");
				ProfileID.appendChild(doc.createTextNode("urn:fdc:peppol.eu:2017:poacc:billing:01:1.0"));
				rootElement.appendChild(ProfileID);

				Element ID = doc.createElement("cbc:ID");
				ID.appendChild(doc.createTextNode(invoice_no)); // INVOICE
//				ID.appendChild(doc.createTextNode("SO000012345")); // DONO
				rootElement.appendChild(ID);

				String[] invdate = invoice_date.split("/");
				String invoicedate = invdate[2] + "-" + invdate[1] + "-" + invdate[0];

				Element IssueDate = doc.createElement("cbc:IssueDate");
				IssueDate.appendChild(doc.createTextNode(invoicedate)); // DATE yyyy-mm-dd
//				IssueDate.appendChild(doc.createTextNode("2022-08-18")); // DATE yyyy-mm-dd
				rootElement.appendChild(IssueDate);
				
				String[] invduedate = invoice_due_date.split("/");
				String invoiceduedate = invduedate[2] + "-" + invduedate[1] + "-" + invduedate[0];

				Element dueDate = doc.createElement("cbc:DueDate");
				dueDate.appendChild(doc.createTextNode(invoiceduedate)); // due DATE yyyy-mm-dd
//				IssueDate.appendChild(doc.createTextNode("2022-08-18")); //due DATE yyyy-mm-dd
				rootElement.appendChild(dueDate);
				
				Element InvoiceTypeCode = doc.createElement("cbc:InvoiceTypeCode");
				InvoiceTypeCode.appendChild(doc.createTextNode("380"));
				rootElement.appendChild(InvoiceTypeCode);

				Element DocumentCurrencyCode = doc.createElement("cbc:DocumentCurrencyCode");
				DocumentCurrencyCode.appendChild(doc.createTextNode(currency_id)); // CURRENCY CODE
//				DocumentCurrencyCode.appendChild(doc.createTextNode("SGD")); // CURRENCY CODE
				rootElement.appendChild(DocumentCurrencyCode);

				Element BuyerReference = doc.createElement("cbc:BuyerReference");
				BuyerReference.appendChild(doc.createTextNode(referenceno)); // Buyer Reference
				rootElement.appendChild(BuyerReference);

				Element AccountingSupplierParty = doc.createElement("cac:AccountingSupplierParty");
				rootElement.appendChild(AccountingSupplierParty);

				Element ASParty = doc.createElement("cac:Party");
				AccountingSupplierParty.appendChild(ASParty);
				
				String[] attsendid = senderid.split(":");

				Element ASEndpointID = doc.createElement("cbc:EndpointID");
				attrType = doc.createAttribute("schemeID");
				attrType.setValue(attsendid[0]);
				ASEndpointID.setAttributeNode(attrType);
				//ASEndpointID.appendChild(doc.createTextNode("SGTST201904946H")); // SUPPLIER PEPPOL ID
				ASEndpointID.appendChild(doc.createTextNode(attsendid[1])); // SUPPLIER PEPPOL ID
				ASParty.appendChild(ASEndpointID);
				
				
				Element ASPartyIdentification = doc.createElement("cac:PartyIdentification");
				ASParty.appendChild(ASPartyIdentification);
				
				Element ASPIID = doc.createElement("cbc:ID");
				attrType = doc.createAttribute("schemeID");
				attrType.setValue(attsendid[0]);
				ASPIID.setAttributeNode(attrType);
				//ASEndpointID.appendChild(doc.createTextNode("SGTST201904946H")); // SUPPLIER PEPPOL ID
				ASPIID.appendChild(doc.createTextNode(attsendid[1])); // SUPPLIER PEPPOL ID
				ASPartyIdentification.appendChild(ASPIID);
				
				
				Element ASPartyName = doc.createElement("cac:PartyName");
				ASParty.appendChild(ASPartyName);

				Element ASPNName = doc.createElement("cbc:Name");
				ASPNName.appendChild(doc.createTextNode(suppliername)); // SUPPLIER NAME
//				ASRegistrationName.appendChild(doc.createTextNode("SMART SUITE")); // SUPPLIER NAME
				ASPartyName.appendChild(ASPNName);
				

				Element ASPostalAddress = doc.createElement("cac:PostalAddress");
				ASParty.appendChild(ASPostalAddress);
				
				Element StreetName = doc.createElement("cbc:StreetName");
				StreetName.appendChild(doc.createTextNode(a1)); // a1+a2
				ASPostalAddress.appendChild(StreetName);
				
				Element AdditionalStreetName = doc.createElement("cbc:AdditionalStreetName");
				AdditionalStreetName.appendChild(doc.createTextNode(a3)); // 
				ASPostalAddress.appendChild(AdditionalStreetName);
				
				Element CityName = doc.createElement("cbc:CityName");
				CityName.appendChild(doc.createTextNode(STATE)); // 
				ASPostalAddress.appendChild(CityName);
				
				Element PostalZone = doc.createElement("cbc:PostalZone");
				PostalZone.appendChild(doc.createTextNode(ZIP)); // 
				ASPostalAddress.appendChild(PostalZone);
				
				Element ASCountry = doc.createElement("cac:Country");
				ASPostalAddress.appendChild(ASCountry);

				Element ASIdentificationCode = doc.createElement("cbc:IdentificationCode");
				ASIdentificationCode.appendChild(doc.createTextNode(countrycode)); // COUNTRY CODE
//				ASIdentificationCode.appendChild(doc.createTextNode("SG")); // COUNTRY CODE
				ASCountry.appendChild(ASIdentificationCode);

				Element ASPartyTaxScheme = doc.createElement("cac:PartyTaxScheme");
				ASParty.appendChild(ASPartyTaxScheme);

				Element ASCompanyID = doc.createElement("cbc:CompanyID");
				ASCompanyID.appendChild(doc.createTextNode(companyGstNo)); // COMPANY GST ID
//				  ASCompanyID.appendChild(doc.createTextNode("AK12333")); // COMPANY GST ID
				ASPartyTaxScheme.appendChild(ASCompanyID);

				Element ASTaxScheme = doc.createElement("cac:TaxScheme");
				ASPartyTaxScheme.appendChild(ASTaxScheme);

				Element ASID = doc.createElement("cbc:ID");
				ASID.appendChild(doc.createTextNode(taxtype)); // TAX TYPE (GST)
//					  ASID.appendChild(doc.createTextNode("GST")); // TAX TYPE (GST)
				ASTaxScheme.appendChild(ASID);

				Element ASPartyLegalEntity = doc.createElement("cac:PartyLegalEntity");
				ASParty.appendChild(ASPartyLegalEntity);

				Element ASRegistrationName = doc.createElement("cbc:RegistrationName");
				ASRegistrationName.appendChild(doc.createTextNode(suppliername)); // SUPPLIER NAME
//				ASRegistrationName.appendChild(doc.createTextNode("SMART SUITE")); // SUPPLIER NAME
				ASPartyLegalEntity.appendChild(ASRegistrationName);
				
				
				  Element ASContact = doc.createElement("cac:Contact");
				  ASParty.appendChild(ASContact);
				  
				  Element ASElectronicMail = doc.createElement("cbc:ElectronicMail");
				  ASElectronicMail.appendChild(doc.createTextNode(supplieremail)); // SUPPLIER EMAIL 
				  ASContact.appendChild(ASElectronicMail);
				 

				Element AccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
				rootElement.appendChild(AccountingCustomerParty);

				Element ACParty = doc.createElement("cac:Party");
				AccountingCustomerParty.appendChild(ACParty);
				
				String[] attreciveid = reciverid.split(":");

				Element ACEndpointID = doc.createElement("cbc:EndpointID");
				attrType = doc.createAttribute("schemeID");
				attrType.setValue(attreciveid[0]);
				ACEndpointID.setAttributeNode(attrType);
				//ACEndpointID.appendChild(doc.createTextNode("SGUEN201723984K")); // CUSTOMER PEPPOL ID
				ACEndpointID.appendChild(doc.createTextNode(attreciveid[1])); // CUSTOMER PEPPOL ID
				ACParty.appendChild(ACEndpointID);

				Element ACPostalAddress = doc.createElement("cac:PostalAddress");
				ACParty.appendChild(ACPostalAddress);

				Element ACCountry = doc.createElement("cac:Country");
				ACPostalAddress.appendChild(ACCountry);

				Element ACIdentificationCode = doc.createElement("cbc:IdentificationCode");
				ACIdentificationCode.appendChild(doc.createTextNode(countrycode)); // COUNTRY CODE
//				ACIdentificationCode.appendChild(doc.createTextNode("SG")); // COUNTRY CODE
				ACCountry.appendChild(ACIdentificationCode);

				Element ACPartyLegalEntity = doc.createElement("cac:PartyLegalEntity");
				ACParty.appendChild(ACPartyLegalEntity);

				Element ACRegistrationName = doc.createElement("cbc:RegistrationName");
				ACRegistrationName.appendChild(doc.createTextNode(customername)); // CUSTOMER NAME
//				ACRegistrationName.appendChild(doc.createTextNode("SMART SUITE")); // CUSTOMER NAME
				ACPartyLegalEntity.appendChild(ACRegistrationName);
				
				
				
				  Element ACContact = doc.createElement("cac:Contact");
				  ACParty.appendChild(ACContact);
				  
				  Element ACElectronicMail = doc.createElement("cbc:ElectronicMail");
				  ACElectronicMail.appendChild(doc.createTextNode(customeremail)); // CUSTOMER EMAIL 
				  ACContact.appendChild(ACElectronicMail);
				 

				Element PaymentTerms = doc.createElement("cac:PaymentTerms");
				rootElement.appendChild(PaymentTerms);

				Element PayTermNote = doc.createElement("cbc:Note");
				PayTermNote.appendChild(doc.createTextNode(payment_terms)); // PAYMENT TERMS
//				PayTermNote.appendChild(doc.createTextNode("PAYMENT TERMS")); // PAYMENT TERMS
				PaymentTerms.appendChild(PayTermNote);

				Element TaxTotal = doc.createElement("cac:TaxTotal");
				rootElement.appendChild(TaxTotal);

				Element TaxAmount = doc.createElement("cbc:TaxAmount");
				attrType = doc.createAttribute("currencyID");
				attrType.setValue("SGD");
				TaxAmount.setAttributeNode(attrType);
				TaxAmount.appendChild(doc.createTextNode(tax_amount)); // TOTAL TAX AMOUNT
//				TaxAmount.appendChild(doc.createTextNode("21.00")); // TOTAL TAX AMOUNT
				TaxTotal.appendChild(TaxAmount);

				Element TaxSubtotal = doc.createElement("cac:TaxSubtotal");
				TaxTotal.appendChild(TaxSubtotal);

				Element TaxableAmount = doc.createElement("cbc:TaxableAmount");
				attrType = doc.createAttribute("currencyID");
				attrType.setValue("SGD");
				TaxableAmount.setAttributeNode(attrType);
				TaxableAmount.appendChild(doc.createTextNode(inclusiveAmt)); // TOTAL TAXABLE AMOUNT
//				TaxableAmount.appendChild(doc.createTextNode("300.00")); // TOTAL TAXABLE AMOUNT
				TaxSubtotal.appendChild(TaxableAmount);

				TaxAmount = doc.createElement("cbc:TaxAmount");
				attrType = doc.createAttribute("currencyID");
				attrType.setValue("SGD");
				TaxAmount.setAttributeNode(attrType);
				TaxAmount.appendChild(doc.createTextNode(tax_amount)); // TOTAL TAX AMOUNT
//				TaxAmount.appendChild(doc.createTextNode("21.00")); // TOTAL TAX AMOUNT
				TaxSubtotal.appendChild(TaxAmount);

				Element TaxCategory = doc.createElement("cac:TaxCategory");
				TaxSubtotal.appendChild(TaxCategory);

				Element TCID = doc.createElement("cbc:ID");
				if (invtaxtype.equalsIgnoreCase("STANDARD RATED")) {
					TCID.appendChild(doc.createTextNode("SR")); // TAX CODE
				} else if (invtaxtype.equalsIgnoreCase("ZERO RATE")) {
					TCID.appendChild(doc.createTextNode("ZR")); // TAX CODE
				} else if (invtaxtype.equalsIgnoreCase("OUT OF SCOPE")) {
					TCID.appendChild(doc.createTextNode("OS")); // TAX CODE
				} else if (invtaxtype.equalsIgnoreCase("EXEMPT")) {
					TCID.appendChild(doc.createTextNode("ES33")); // TAX CODE
				} else {
					TCID.appendChild(doc.createTextNode("ZR")); // TAX CODE
				}
				// TCID.appendChild(doc.createTextNode("SR")); // TAX CODE
				TaxCategory.appendChild(TCID);

				Element TCPERCENT = doc.createElement("cbc:Percent");
				TCPERCENT.appendChild(doc.createTextNode(String.valueOf(outbound_gst))); // TAX PERCENTAGE
//				TCPERCENT.appendChild(doc.createTextNode("7")); // TAX PERCENTAGE
				TaxCategory.appendChild(TCPERCENT);

				Element TCTaxScheme = doc.createElement("cac:TaxScheme");
				TaxCategory.appendChild(TCTaxScheme);

				Element TSID = doc.createElement("cbc:ID");
				TSID.appendChild(doc.createTextNode(taxtype)); // TAX CODE
//				TSID.appendChild(doc.createTextNode("GST")); // TAX CODE
				TCTaxScheme.appendChild(TSID);

				Element LegalMonetaryTotal = doc.createElement("cac:LegalMonetaryTotal");
				rootElement.appendChild(LegalMonetaryTotal);

				Element LineExtensionAmount = doc.createElement("cbc:LineExtensionAmount");
				attrType = doc.createAttribute("currencyID");
				attrType.setValue("SGD");
				LineExtensionAmount.setAttributeNode(attrType);
				LineExtensionAmount
						.appendChild(doc.createTextNode(StrUtils.addZeroes(Double.parseDouble(sub_total), numberOfDecimal))); // LineExtensionAmount
				LegalMonetaryTotal.appendChild(LineExtensionAmount);

				Element TaxExclusiveAmount = doc.createElement("cbc:TaxExclusiveAmount");
				attrType = doc.createAttribute("currencyID");
				attrType.setValue("SGD");
				TaxExclusiveAmount.setAttributeNode(attrType);
				TaxExclusiveAmount.appendChild(doc.createTextNode(inclusiveAmt)); // TaxExclusiveAmount
//				TaxExclusiveAmount.appendChild(doc.createTextNode("300.00")); // TaxExclusiveAmount
				LegalMonetaryTotal.appendChild(TaxExclusiveAmount);

				Element TaxInclusiveAmount = doc.createElement("cbc:TaxInclusiveAmount");
				attrType = doc.createAttribute("currencyID");
				attrType.setValue("SGD");
				TaxInclusiveAmount.setAttributeNode(attrType);
				TaxInclusiveAmount.appendChild(doc.createTextNode(exclusiveAmt)); // TaxInclusiveAmount
//				TaxInclusiveAmount.appendChild(doc.createTextNode("321.00")); // TaxInclusiveAmount
				LegalMonetaryTotal.appendChild(TaxInclusiveAmount);

				Element PayableAmount = doc.createElement("cbc:PayableAmount");
				attrType = doc.createAttribute("currencyID");
				attrType.setValue("SGD");
				PayableAmount.setAttributeNode(attrType);
				PayableAmount.appendChild(doc.createTextNode(total_amount)); // PayableAmount
//				PayableAmount.appendChild(doc.createTextNode("321.00")); // PayableAmount
				LegalMonetaryTotal.appendChild(PayableAmount);

				for (i = 0; i < invoicedet.size(); i++) {
					Map invoicedetmap = (Map) invoicedet.get(i);
					qty = (String) invoicedetmap.get("QTY");
					unitprice = (String) invoicedetmap.get("UNITPRICE");
					item = (String) invoicedetmap.get("ITEM");
					lnno = (String) invoicedetmap.get("LNNO");
					uom = (String) invoicedetmap.get("UOM");
					Double lineAmount = (Double.parseDouble(qty) * Double.parseDouble(unitprice));
					unitprice = StrUtils.addZeroes(Double.parseDouble(unitprice), numberOfDecimal);
					String lineAmt = StrUtils.addZeroes(lineAmount, numberOfDecimal);

					Element InvoiceLine = doc.createElement("cac:InvoiceLine");
					rootElement.appendChild(InvoiceLine);

					Element LNNO = doc.createElement("cbc:ID");
					LNNO.appendChild(doc.createTextNode(String.valueOf(lnno))); // LNNO
//				LNNO.appendChild(doc.createTextNode("1")); // LNNO
					InvoiceLine.appendChild(LNNO);

					Element InvoicedQuantity = doc.createElement("cbc:InvoicedQuantity");
					attrType = doc.createAttribute("unitCode");
					attrType.setValue(uom);
					// attrType.setValue("H87");
					InvoicedQuantity.setAttributeNode(attrType);
					InvoicedQuantity.appendChild(doc.createTextNode(String.valueOf(qty))); // QTY
//				InvoicedQuantity.appendChild(doc.createTextNode("3.00")); // QTY
					InvoiceLine.appendChild(InvoicedQuantity);

					Element ILLineExtensionAmount = doc.createElement("cbc:LineExtensionAmount");
					attrType = doc.createAttribute("currencyID");
					attrType.setValue("SGD");
					ILLineExtensionAmount.setAttributeNode(attrType);
					ILLineExtensionAmount.appendChild(doc.createTextNode(lineAmt)); // PayableAmount
//				ILLineExtensionAmount.appendChild(doc.createTextNode("300.00")); // PayableAmount
					InvoiceLine.appendChild(ILLineExtensionAmount);

					Element Item = doc.createElement("cac:Item");
					InvoiceLine.appendChild(Item);

					Element Name = doc.createElement("cbc:Name");
					Name.appendChild(doc.createTextNode(item)); // ITEM NAME
//				Name.appendChild(doc.createTextNode("PRODUCT1")); // ITEM NAME
					Item.appendChild(Name);

					Element ClassifiedTaxCategory = doc.createElement("cac:ClassifiedTaxCategory");
					Item.appendChild(ClassifiedTaxCategory);

					Element TAXNAME = doc.createElement("cbc:ID");
					if (invtaxtype.equalsIgnoreCase("STANDARD RATED")) {
						TAXNAME.appendChild(doc.createTextNode("SR")); // TAX CODE
					} else if (invtaxtype.equalsIgnoreCase("ZERO RATE")) {
						TAXNAME.appendChild(doc.createTextNode("ZR")); // TAX CODE
					} else if (invtaxtype.equalsIgnoreCase("OUT OF SCOPE")) {
						TAXNAME.appendChild(doc.createTextNode("OS")); // TAX CODE
					} else if (invtaxtype.equalsIgnoreCase("EXEMPT")) {
						TAXNAME.appendChild(doc.createTextNode("ES33")); // TAX CODE
					} else {
						TAXNAME.appendChild(doc.createTextNode("ZR")); // TAX CODE
					}
					// TAXNAME.appendChild(doc.createTextNode("SR")); // TAX NAME
//				TAXNAME.appendChild(doc.createTextNode("SR")); // TAX NAME
					ClassifiedTaxCategory.appendChild(TAXNAME);

					Element TAXPERCENTAGE = doc.createElement("cbc:Percent");
					TAXPERCENTAGE.appendChild(doc.createTextNode(String.valueOf(outbound_gst))); // TAX PERCENTAGE
//				TAXPERCENTAGE.appendChild(doc.createTextNode("7")); // TAX PERCENTAGE
					ClassifiedTaxCategory.appendChild(TAXPERCENTAGE);

					Element TaxScheme = doc.createElement("cac:TaxScheme");
					ClassifiedTaxCategory.appendChild(TaxScheme);

					Element TAXSCHEMENAMEID = doc.createElement("cbc:ID");
					TAXSCHEMENAMEID.appendChild(doc.createTextNode("GST")); // TAX SCHEME NAME
//				TAXSCHEMENAMEID.appendChild(doc.createTextNode("GST")); // TAX SCHEME NAME
					TaxScheme.appendChild(TAXSCHEMENAMEID);

					Element Price = doc.createElement("cac:Price");
					InvoiceLine.appendChild(Price);

					Element PriceAmount = doc.createElement("cbc:PriceAmount");
					attrType = doc.createAttribute("currencyID");
					attrType.setValue("SGD");
					PriceAmount.setAttributeNode(attrType);
					PriceAmount.appendChild(doc.createTextNode(unitprice)); // UNIT PRICE
//				PriceAmount.appendChild(doc.createTextNode("100.00")); // UNIT PRICE
					Price.appendChild(PriceAmount);
				}

				StandardBusinessDocument.appendChild(rootElement);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);

				System.out.println(source.toString());

				StreamResult result = new StreamResult(new File("D:\\Invoice_Outbound.xml"));
				transformer.transform(source, result);

				File xmlFile = new File(xmlFilePath);

				StringWriter writer = new StringWriter();
				StreamResult xmlresult = new StreamResult(writer);
				transformer.transform(source, xmlresult);
				System.out.println("XML IN String format is: \n" + writer.toString());

				//URL obj = new URL("https://api.ap-connect.dev.einvoice.sg/v1/invoice/outbound/upload");
				//URL obj = new URL(outboundurl);
				URI uri = new URI(outboundurl);
				URL obj = uri.toURL();
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("content-type", "text/xml");
				// con.setRequestProperty("api_key","t_nXtH1WueaKaILLfQN2B6QmLmWmMXqCcJ");
				String peppolukey = plantMstUtil.getPeppolUkey(plant);
				con.setRequestProperty("api_key", peppolukey);

				// For POST only - START
				con.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
				wr.write(writer.toString());
				wr.flush();
				wr.close();
				// For POST only - END
				System.out.println(con.getResponseMessage());
				int responseCode = con.getResponseCode();
				System.out.println("POST Response Code :: " + responseCode);

				String msg = "";
				if (responseCode == 200) { // success
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();

					// print result
					System.out.println(oresponse.toString());
					Gson gson = new Gson();
					PeppolInvoiceResult peppolresult = new Gson().fromJson(oresponse.toString(),
							PeppolInvoiceResult.class);
					System.out.println(peppolresult);
					System.out.println(peppolresult.getDocId());
					System.out.println(peppolresult.getStatus());
					new InvoiceDAO().updatepeppolstatus(invoiceId, peppolresult.getDocId(), plant);

					PEPPOL_DOC_IDS peepoldocids = new PEPPOL_DOC_IDS();
					peepoldocids.setDOC_ID(peppolresult.getDocId());
					peepoldocids.setPLANT(plant);
					peepoldocids.setCRAT(username);
					peepoldocids.setCRAT(new DateUtils().getDateTime());

					new PeppolDocIdsDAO().addPeppolDoc(peepoldocids);

					try {

						EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
						Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.INVOICE);
						if (emailMsg.get(IDBConstants.ISAUTOEMAIL).equalsIgnoreCase("Y")) {
							if ("invoice".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								invoicePDFGenration(request, response, invoice_no, Integer.valueOf(invoiceId));
							}

							String compName = new userBean().getCompanyName(plant);
							String custName = new CustMstDAO().getCustomerNameBycode(plant,
									String.valueOf(invoiceHdrmap.get("CUSTNO").toString()));
							String sendTo = new CustMstDAO().getCustomerEmailBycode(plant,
									String.valueOf(invoiceHdrmap.get("CUSTNO").toString()));
							if (sendTo.length() > 0) {

								String sendSubject = (String) emailMsg.get(IDBConstants.SUBJECT);
								sendSubject = sendSubject.replace("{COMPANY_NAME}", compName);
								sendSubject = sendSubject.replace("{INVOICE_NO}", invoice_no);
								String sendBody = (String) emailMsg.get(IDBConstants.BODY1);
								sendBody = sendBody.replace("{CUSTOMER_NAME}", custName);
								sendBody = sendBody.replace("{INVOICE_NO}", invoice_no);

								String sendFrom = (String) emailMsg.get(IDBConstants.EMAIL_FROM);
								if ("invoice".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
									attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + invoice_no
											+ ".pdf";
									if (attachmentLocation != null) {
										attachmentLocations.add(attachmentLocation);
									}
								}

								String fileLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH;
								String fileName = "Invoice_" + invoice_no + ".pdf";
								fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
								File path = new File(fileLocation);
								File uploadedFile = new File(path + "/" + fileName);
								attachmentLocation = uploadedFile.getAbsolutePath();
								attachmentLocations.add(attachmentLocation);

								SendEmail sendMail = new SendEmail();
								String mailResp = sendMail.sendTOMailPdf(sendFrom == null ? sendTo : sendFrom, sendTo,
										"", "", sendSubject, sendBody, attachmentLocations);
							}
						}

						msg = "Invoice Data processed";
					} catch (Exception e) {
						msg = "Invoice Data processed And Enail Not Send";
					} finally {
						// delete temp uploaded file
						File tempPath = new File(attachmentLocation);
						if (tempPath.exists()) {
							tempPath.delete();
						}

					}

				} else {
					System.out.println("POST request not worked");
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer oresponse = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						oresponse.append(inputLine);
					}
					in.close();

					// print result
					System.out.println(response.toString());
					msg = "Unable To Process";
				}
					
					
				msg="Invoice Send To Peppol";

				response.sendRedirect("../peppol/peppolsales?msg=" + msg);

			} catch (Exception e) {
				e.printStackTrace();
				/*
				 * request.setAttribute("Msg", "Unable To Process"); RequestDispatcher rd =
				 * request.getRequestDispatcher("/jsp/peppolSales.jsp"); rd.forward(request,
				 * response);
				 */
				String msg = "";
				response.sendRedirect("../peppol/peppolsales?msg=" + msg);
			}

		}
		


		
		if (action.equalsIgnoreCase("savebill")) {

			String jsonString = "";
			String result = "";
			try {
				BillUtil billUtil = new BillUtil();
				RecvDetDAO recvDao = new RecvDetDAO();
				MovHisDAO movHisDao = new MovHisDAO();

				String id = request.getParameter("ID");
				PEPPOL_RECEIVED_DATA peppolrecvdata = new PeppolReceivedDataDAO().getPeppolReceivedDataById(plant,
						Integer.valueOf(id));

				URL obj1 = new URL(peppolrecvdata.getINVOICEFILEURL());
				HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
				con1.setRequestMethod("GET");
				String peppolukey = plantMstUtil.getPeppolUkey(plant);
				con1.setRequestProperty("api_key", peppolukey);
				System.out.println(con1.getResponseMessage());
				int responseCode1 = con1.getResponseCode();
				System.out.println("POST Response Code :: " + responseCode1);

				String msg1 = "";
				if (responseCode1 == 200) {
					 // success
					BufferedReader in1 = new BufferedReader(new InputStreamReader(con1.getInputStream()));
					String inputLine1;
					StringBuffer oresponse1 = new StringBuffer();

					while ((inputLine1 = in1.readLine()) != null) {
						oresponse1.append(inputLine1);
					}
					in1.close();
					// print result
					System.out.println("-------------xml--------------");
					System.out.println(oresponse1.toString());
					System.out.println("-------------xml--------------");
					

					org.json.JSONObject json = XML.toJSONObject(oresponse1.toString());
					jsonString = json.toString();
					
					
					org.json.JSONObject invoice = null;
					org.json.JSONObject jstrings = null;
					Iterator keys1 = json.keys();
					String key1 ="";
					 while(keys1.hasNext()) {
					   key1 = (String)keys1.next();
					   System.out.println(key1);
					 }
					 jstrings = (org.json.JSONObject) json.get(key1);
					 Iterator keys2 = jstrings.keys();
						String key2 ="";
						 while(keys2.hasNext()) {
						   String keycheck2 = (String)keys2.next();
						   System.out.println(keycheck2);
						   if(keycheck2.contains("Invoice")) {
							   key2 = keycheck2;
						   }
						 }
				     invoice = (org.json.JSONObject) jstrings.get(key2);
					 
					
				/*	boolean getsname = true;
					org.json.JSONObject jstrings = null;
					if(getsname) {
						try {
						jstrings = (org.json.JSONObject) json.get("StandardBusinessDocument");
						getsname = false;
						}catch (Exception e) {
							getsname = true;
							System.out.println("StandardBusinessDocument not available");
						}
					}
					
					if(getsname) {
						try {
							jstrings = (org.json.JSONObject) json.get("sh:StandardBusinessDocument");
							getsname = false;
							}catch (Exception e) {
								getsname = true;
								System.out.println("sh:StandardBusinessDocument not available");
							}
					}
					
					boolean getiname = true;
					org.json.JSONObject invoice = null;
					if(getiname) {
						try {
							invoice = (org.json.JSONObject) jstrings.get("Invoice");
							getiname = false;
						}catch (Exception e) {
							getiname = true;
							System.out.println("Invoice not available");
						}
					}
					if(getiname) {
						try {
							invoice = (org.json.JSONObject) jstrings.get("ns4:Invoice");
							getiname = false;
						}catch (Exception e) {
							getiname = true;
							System.out.println("ns4:Invoice not available");
						}
					}
					if(getiname) {
						try {
							invoice = (org.json.JSONObject) jstrings.get("ns:Invoice");
							getiname = false;
						}catch (Exception e) {
							getiname = true;
							System.out.println("ns:Invoice not available");
						}
					}*/
					
					//org.json.JSONObject jstrings = (org.json.JSONObject) json.get("StandardBusinessDocument");
					//org.json.JSONObject jstring = (org.json.JSONObject) jstrings.get("Invoice");
					System.out.println("-------------json--------------");
					System.out.println(invoice.toString());
					System.out.println("-------------json--------------");

					
					/*try {
						org.json.JSONObject AdditionalDocumentReference = (org.json.JSONObject) invoice.get("cac:AdditionalDocumentReference");
						org.json.JSONObject Attachment = (org.json.JSONObject) AdditionalDocumentReference.get("cac:Attachment");
						org.json.JSONObject EmbeddedDocument = (org.json.JSONObject) Attachment.get("cbc:EmbeddedDocumentBinaryObject");
						String crntImage = EmbeddedDocument.getString("content");
						String dname = EmbeddedDocument.getString("filename");
						savedocument(crntImage, dname);
						}catch (Exception e) {
							System.out.println(e.getMessage());
						}*/									
					
						try {
							org.json.JSONObject AdditionalDocumentReference = (org.json.JSONObject) json.get("cac:AdditionalDocumentReference");
							System.out.println("test");
						}catch (Exception e) {
							
						}
					
					//org.json.JSONObject invoice = (org.json.JSONObject) jstrings.get("Invoice");
					org.json.JSONObject AccountingSupplierParty = (org.json.JSONObject) invoice.get("cac:AccountingSupplierParty");
					org.json.JSONObject Party = (org.json.JSONObject) AccountingSupplierParty.get("cac:Party");
					
					
					org.json.JSONObject PartyLegalEntity = (org.json.JSONObject) Party.get("cac:PartyLegalEntity");

					org.json.JSONObject endpoint = (org.json.JSONObject) Party.get("cbc:EndpointID");
					
					org.json.JSONObject LegalMonetaryTotal = (org.json.JSONObject) invoice
							.get("cac:LegalMonetaryTotal");
					org.json.JSONObject PayableAmount = (org.json.JSONObject) LegalMonetaryTotal
							.get("cbc:PayableAmount");
					org.json.JSONObject LineExtensionAmount = (org.json.JSONObject) LegalMonetaryTotal
							.get("cbc:LineExtensionAmount");

					

					org.json.JSONObject TaxTotal = (org.json.JSONObject) invoice.get("cac:TaxTotal");
					org.json.JSONObject TaxAmount = (org.json.JSONObject) TaxTotal.get("cbc:TaxAmount");

					org.json.JSONObject PostalAddress = (org.json.JSONObject) Party.get("cac:PostalAddress");
					org.json.JSONObject Country = (org.json.JSONObject) PostalAddress.get("cac:Country");
					
					
					String supplieremail = "";
					try {
						org.json.JSONObject suppliercontact = (org.json.JSONObject) Party.get("cac:Contact");
						supplieremail = suppliercontact.getString("cbc:ElectronicMail");
					}catch (Exception e) {
						System.out.println("no supplier email");
					}

					org.json.JSONObject TaxSubtotal = (org.json.JSONObject) TaxTotal.get("cac:TaxSubtotal");
					org.json.JSONObject TaxCategory = (org.json.JSONObject) TaxSubtotal.get("cac:TaxCategory");
					
					String payterms = "";
					try {
					org.json.JSONObject PaymentTerms = (org.json.JSONObject) invoice.get("cac:PaymentTerms");
					payterms = PaymentTerms.getString("cbc:Note");
					}catch (Exception e) {
						System.out.println("payment terms not available");
					}

					String vendname = PartyLegalEntity.getString("cbc:RegistrationName");
					String vendpeppolid = endpoint.getString("content");
					String currency = PayableAmount.getString("currencyID");
					String issuedate = invoice.getString("cbc:IssueDate");
					String countrycode = Country.getString("cbc:IdentificationCode");
					String companyregno = "";
					try{
						org.json.JSONObject PartyTaxScheme = (org.json.JSONObject) Party.get("cac:PartyTaxScheme");
						try {
							org.json.JSONObject compcontent = (org.json.JSONObject) PartyTaxScheme.get("cbc:CompanyID");
							companyregno = compcontent.getString("content");
						} catch (Exception e) {
							companyregno = PartyTaxScheme.getString("cbc:CompanyID");
						}
						
					} catch (Exception e) {
						try {
							org.json.JSONObject compcontent = (org.json.JSONObject) PartyLegalEntity.get("cbc:CompanyID");
							companyregno = compcontent.getString("content");
						} catch (Exception r) {
							companyregno = PartyLegalEntity.getString("cbc:CompanyID");
						}
						
					}
					String add1 = "";
					String add3 = "";
					String state = "";
					String zip = "";
					try {
						add1 = PostalAddress.getString("cbc:StreetName");
					}catch (Exception e) {
						System.out.println("StreetName not available");
					}
					try {
						add3 = PostalAddress.getString("cbc:AdditionalStreetName");
					}catch (Exception e) {
						System.out.println("AdditionalStreetName not available");
					}
					try {
						state = PostalAddress.getString("cbc:CityName");
					}catch (Exception e) {
						System.out.println("CityName not available");
					}
					try {
						zip = PostalAddress.getString("cbc:PostalZone");
					}catch (Exception e) {
						System.out.println("PostalZone not available");
					}
					//String state = PostalAddress.getString("cbc:CityName");
					//String zip = PostalAddress.getString("cbc:PostalZone");
					String otaxcode = TaxCategory.getString("cbc:ID");
					String countryname = new PlantMstDAO().getCOUNTRYNAME(countrycode);
					if(countryname == null) {
						org.json.JSONObject identification = (org.json.JSONObject) Country.get("cbc:IdentificationCode");
						countrycode = identification.getString("content");
						countryname = new PlantMstDAO().getCOUNTRYNAME(countrycode);
					}
					
					String dueDate = "";
					try {
						dueDate = invoice.getString("cbc:DueDate");
						dueDate = new DateUtils().getDateyyyymmdd(dueDate);
					}catch (Exception e) {
						// TODO: handle exception
					}
					
					org.json.JSONArray InvoiceLine = new JSONArray();
					try {
					InvoiceLine = (org.json.JSONArray) invoice.get("cac:InvoiceLine");
					}catch (Exception e) {
						org.json.JSONObject InvoiceLineObject = (org.json.JSONObject) invoice.get("cac:InvoiceLine");
						InvoiceLine.put(InvoiceLineObject);
					}

					/*
					 * for (int i = 0; i < InvoiceLine.length(); i++) { org.json.JSONObject
					 * explrObject = InvoiceLine.getJSONObject(i);
					 * 
					 * org.json.JSONObject Price = (org.json.JSONObject)
					 * explrObject.get("cac:Price"); org.json.JSONObject Item =
					 * (org.json.JSONObject) explrObject.get("cac:Item"); org.json.JSONObject
					 * InvoicedQuantity = (org.json.JSONObject)
					 * explrObject.get("cbc:InvoicedQuantity");
					 * 
					 * org.json.JSONObject PriceAmount = (org.json.JSONObject)
					 * Price.get("cbc:PriceAmount"); org.json.JSONObject ClassifiedTaxCategory =
					 * (org.json.JSONObject) Item.get("cac:ClassifiedTaxCategory");
					 * 
					 * String lnno =explrObject.getString("cbc:ID");; String unitcost
					 * =PriceAmount.getString("content"); String taxcode
					 * =ClassifiedTaxCategory.getString("cbc:ID"); String taxpercentage
					 * =ClassifiedTaxCategory.getString("cbc:Percent"); String item
					 * =Item.getString("cbc:Name"); String uom
					 * =InvoicedQuantity.getString("unitCode"); String qty
					 * =InvoicedQuantity.getString("content");
					 * 
					 * 
					 * 
					 * }
					 */

					List<Hashtable<String, String>> billDetInfoList = null;
					Hashtable<String, String> billDetInfo = null;
					UserTransaction ut = null;
					CustUtil custUtil = new CustUtil();
					ItemUtil itemUtil = new ItemUtil();
					boolean isAdded = false;

					try {

						
						boolean vedboolean = custUtil.isExistVendorName(vendname, plant);
						String vendno = "";
						if(vedboolean) {
							Hashtable vht = new Hashtable();
							vht.put("PLANT", plant);
							vht.put("VNAME", vendname);
							vendno = new VendMstDAO().getVendorNoByName(vht);
						}else {
							vendno = createsupplier(plant, username, vendname,vendpeppolid,companyregno,add1,add3,state,zip,countryname,supplieremail);
						}
						

						String billNo = invoice.getString("cbc:ID");
						String pono = "";
						String grno = "";
						String billDate = new DateUtils().getDateyyyymmdd(issuedate);
						//String dueDate = "";
						String payTerms = payterms;
						String empno = "";
						String itemRates = "0";
						String discountType = currency;
						String discountAccount = "";
						String adjustmentLabel = "Adjustment";
						String adjustment = "0.0";
						String totalAmount = PayableAmount.getString("content");
						String subTotal = LineExtensionAmount.getString("content");
						String billStatus = "Open";
						String shipRef = "";
						String refNum = "";
						String note = "";
						String taxamount = TaxAmount.getString("content");
						String isshtax = "0";
						String purchaseloc = "";
						String taxtreatment = "GST Registered";
						String sREVERSECHARGE = "";
						String sGOODSIMPORT = "";
						String currencyid = currency;
						String currencyuseqt = "1.0";
						String orddiscounttype = "%";
						String taxid = "";
						FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();

						if (otaxcode.equalsIgnoreCase("SR")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "STANDARD RATED");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else if (otaxcode.equalsIgnoreCase("ZR")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "ZERO RATE");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else if (otaxcode.equalsIgnoreCase("OS")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "OUT OF SCOPE");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else if (otaxcode.equalsIgnoreCase("ES33")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "EXEMPT");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "EXEMPT");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						}
						String isdiscounttax = "1";
						String isorderdiscounttax = "1";
						String discount = "0.0";
						String shippingCost = "0.0";
						String orderdiscount = "0.000";
						String gst = TaxCategory.getString("cbc:Percent");
						String projectid = "";
						String transportid = "0";
						String deductInv = "0";
						String billtype = "NON INVENTORY";

						billDetInfoList = new ArrayList<Hashtable<String, String>>();
						Hashtable billHdr = new Hashtable();
						billHdr.put("PLANT", plant);
						billHdr.put("VENDNO", vendno);
						billHdr.put("BILL", billNo);
						billHdr.put("PONO", pono);
						billHdr.put("GRNO", grno);
						billHdr.put("BILL_DATE", billDate);
						billHdr.put("DUE_DATE", dueDate);
						billHdr.put("PAYMENT_TERMS", payTerms);
						billHdr.put("EMPNO", empno);
						billHdr.put("ITEM_RATES", itemRates);
						billHdr.put("DISCOUNT_TYPE", discountType);
						billHdr.put("DISCOUNT_ACCOUNT", discountAccount);
						billHdr.put("ADJUSTMENT_LABEL", adjustmentLabel);
						billHdr.put("ADJUSTMENT", adjustment);
						billHdr.put("SUB_TOTAL", subTotal);
						billHdr.put("TOTAL_AMOUNT", totalAmount);
						billHdr.put("BILL_STATUS", billStatus);
						billHdr.put("SHIPMENT_CODE", shipRef);
						billHdr.put("REFERENCE_NUMBER", refNum);
						billHdr.put("NOTE", note);
						billHdr.put("CRAT", DateUtils.getDateTime());
						billHdr.put("CRBY", username);
						billHdr.put("UPAT", DateUtils.getDateTime());
						billHdr.put("CREDITNOTESSTATUS", "0");
						billHdr.put("TAXAMOUNT", taxamount);
						billHdr.put("ISSHIPPINGTAXABLE", isshtax);
						billHdr.put("PURCHASE_LOCATION", purchaseloc);
						billHdr.put("TAXTREATMENT", taxtreatment);
						billHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
						billHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
						billHdr.put(IDBConstants.CURRENCYID, currencyid);
						billHdr.put("CURRENCYUSEQT", currencyuseqt);
						billHdr.put("ORDERDISCOUNTTYPE", orddiscounttype);
						billHdr.put("TAXID", taxid);
						billHdr.put("ISDISCOUNTTAX", isdiscounttax);
						billHdr.put("ISORDERDISCOUNTTAX", isorderdiscounttax);
						billHdr.put("DISCOUNT", discount);
						billHdr.put("SHIPPINGCOST", shippingCost);
						billHdr.put("ORDER_DISCOUNT", orderdiscount);
						billHdr.put("INBOUND_GST", gst);
						billHdr.put("PROJECTID", projectid);
						billHdr.put("TRANSPORTID", transportid);
						billHdr.put("SHIPCONTACTNAME", "");
						billHdr.put("SHIPDESGINATION", "");
						billHdr.put("SHIPWORKPHONE", "");
						billHdr.put("SHIPHPNO", "");
						billHdr.put("SHIPEMAIL", "");
						billHdr.put("SHIPCOUNTRY", "");
						billHdr.put("SHIPADDR1", "");
						billHdr.put("SHIPADDR2", "");
						billHdr.put("SHIPADDR3", "");
						billHdr.put("SHIPADDR4", "");
						billHdr.put("SHIPSTATE", "");
						billHdr.put("SHIPZIP", "");
						billHdr.put("SHIPPINGID", "");
						billHdr.put("SHIPPINGCUSTOMER", "");
						billHdr.put("ORIGIN", "PEPPOL");
						billHdr.put("DEDUCT_INV", deductInv);
						billHdr.put("BILL_TYPE", billtype);

						ExpensesDAO expenseDao = new ExpensesDAO();
						CoaDAO coaDao = new CoaDAO();
						BillDAO itemCogsDao = new BillDAO();
						List<CostofgoodsLanded> landedCostLst = new ArrayList<>();
//										List<ItemCogs> lstCogs=new ArrayList<>();
						ItemMstDAO itemmstDao = new ItemMstDAO();
						Costofgoods costofGoods = new CostofgoodsImpl();
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
								.trim();
						double expenseAmt = 0.0;
						// Get Transaction object
						ut = DbBean.getUserTranaction();
						// Begin Transaction
						ut.begin();
						int billHdrId = billUtil.addBillHdr(billHdr, plant);
						if (billHdrId > 0) {
							for (int i = 0; i < InvoiceLine.length(); i++) {
								org.json.JSONObject explrObject = InvoiceLine.getJSONObject(i);

								org.json.JSONObject Price = (org.json.JSONObject) explrObject.get("cac:Price");
								org.json.JSONObject Item = (org.json.JSONObject) explrObject.get("cac:Item");
								org.json.JSONObject InvoicedQuantity = (org.json.JSONObject) explrObject
										.get("cbc:InvoicedQuantity");

								org.json.JSONObject PriceAmount = (org.json.JSONObject) Price.get("cbc:PriceAmount");
								org.json.JSONObject ClassifiedTaxCategory = (org.json.JSONObject) Item
										.get("cac:ClassifiedTaxCategory");

								String lnno = explrObject.getString("cbc:ID");
								;
								String unitcost = PriceAmount.getString("content");
								String taxcode = ClassifiedTaxCategory.getString("cbc:ID");
								String taxpercentage = ClassifiedTaxCategory.getString("cbc:Percent");
								String item = Item.getString("cbc:Name");
								String uom = InvoicedQuantity.getString("unitCode");
								String qty = InvoicedQuantity.getString("content");

								double lamount = Double.valueOf(unitcost) * Double.valueOf(qty);
								
								Hashtable uht = new Hashtable();
								uht.put(IDBConstants.PLANT, plant);
								uht.put(IDBConstants.UOMCODE, uom);
								if (!(new UomUtil().isExistsUom(uht))) // if the Item  exists already
								{
									createuom(plant, username, uom);
								}
								
								 if(!(itemUtil.isExistsItemMst(item,plant))) // if the item exists already
								    {
									 createproduct(plant, username, item, uom, unitcost);
								    }

								billDetInfo = new Hashtable<String, String>();
								billDetInfo.put("PLANT", plant);
								billDetInfo.put("LNNO", lnno);
								billDetInfo.put("BILLHDRID", Integer.toString(billHdrId));
								billDetInfo.put("ITEM", item);
								billDetInfo.put("ACCOUNT_NAME", "Inventory Asset");
								billDetInfo.put("QTY", qty);
								billDetInfo.put("COST", unitcost);
								billDetInfo.put("DISCOUNT", "0.0");
								billDetInfo.put("DISCOUNT_TYPE", currency);

								String taxdettype = "";
								double taxper = Double.valueOf(taxpercentage);
								if (taxcode.equalsIgnoreCase("SR")) {
									taxdettype = "STANDARD RATED [" + taxper + "%]";
								} else if (taxcode.equalsIgnoreCase("ZR")) {
									taxdettype = "ZERO RATE [0.0%]";
								} else if (taxcode.equalsIgnoreCase("OS")) {
									taxdettype = "OUT OF SCOPE";
								} else if (taxcode.equalsIgnoreCase("ES33")) {
									taxdettype = "EXEMPT";
								} else {
									taxdettype = "ZERO RATE [0.0%]";
								}

								billDetInfo.put("Tax_Type", taxdettype);
								billDetInfo.put("Amount", String.valueOf(lamount));
								billDetInfo.put("LANDED_COST", "0");
								billDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								billDetInfo.put("LOC", "");
								billDetInfo.put("BATCH", "NOBATCH");
								billDetInfo.put("UOM", uom);
								billDetInfo.put("CRAT", DateUtils.getDateTime());
								billDetInfo.put("CRBY", username);
								billDetInfo.put("UPAT", DateUtils.getDateTime());
								billDetInfoList.add(billDetInfo);

							}
							isAdded = billUtil.addMultipleBillDet(billDetInfoList, plant);
							
							if(isAdded) {
								try {
									org.json.JSONObject AdditionalDocumentReference = (org.json.JSONObject) invoice.get("cac:AdditionalDocumentReference");
									org.json.JSONObject Attachment = (org.json.JSONObject) AdditionalDocumentReference.get("cac:Attachment");
									org.json.JSONObject EmbeddedDocument = (org.json.JSONObject) Attachment.get("cbc:EmbeddedDocumentBinaryObject");
									String crntImage = EmbeddedDocument.getString("content");
									String dname = EmbeddedDocument.getString("filename");
									String dtype = EmbeddedDocument.getString("mimeCode");
									savedocument(plant,crntImage, dname,vendno,invoice.getString("cbc:ID"),dtype,username,billHdrId);
									}catch (Exception e) {
										System.out.println(e.getMessage());
									}		
							}
							
							if (isAdded) {

								System.out.println("billStatus" + billStatus);
								if (!billStatus.equalsIgnoreCase("Draft")) {
									// Journal Entry
									JournalHeader journalHead = new JournalHeader();
									journalHead.setPLANT(plant);
									journalHead.setJOURNAL_DATE(billDate);
									journalHead.setJOURNAL_STATUS("PUBLISHED");
									journalHead.setJOURNAL_TYPE("Cash");
									journalHead.setCURRENCYID(curency);
									journalHead.setTRANSACTION_TYPE("BILL");
									journalHead.setTRANSACTION_ID(billNo);
									journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
									// journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
									journalHead.setCRAT(DateUtils.getDateTime());
									journalHead.setCRBY(username);

									List<JournalDetail> journalDetails = new ArrayList<>();
									List<JournalDetail> journalReversalList = new ArrayList<>();
									CoaDAO coaDAO = new CoaDAO();
									VendMstDAO vendorDAO = new VendMstDAO();
									ItemMstDAO itemMstDAO = new ItemMstDAO();
									Double totalItemNetWeight = 0.00;
									Double totalline = 0.00;
									for (Map billDet : billDetInfoList) {
										Double quantity = Double.parseDouble(billDet.get("QTY").toString());
										totalline++;
										String netWeight = itemMstDAO.getItemNetWeight(plant,
												billDet.get("ITEM").toString());
//														TODO : Ravindra - Get verified
										if (netWeight == null || "".equals(netWeight)) {
											netWeight = "0";
										}
										Double Netweight = quantity * Double.parseDouble(netWeight);
										totalItemNetWeight += Netweight;
										System.out.println("TotalNetWeight:" + totalItemNetWeight);

										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant,
												(String) billDet.get("ACCOUNT_NAME"));
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
										if (!orddiscounttype.toString().equalsIgnoreCase("%")) {
											journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString())
													- Double.parseDouble(orderdiscount.toString())
															/ billDetInfoList.size());
										} else {
											Double jodamt = (Double.parseDouble(billDet.get("Amount").toString()) / 100)
													* Double.parseDouble(orderdiscount.toString());
											journalDetail.setDEBITS(
													Double.parseDouble(billDet.get("Amount").toString()) - jodamt);
										}

										boolean isLoop = false;
										if (journalDetails.size() > 0) {
											int i = 0;
											for (JournalDetail journal : journalDetails) {
												int accountId = journal.getACCOUNT_ID();
												if (accountId == journalDetail.getACCOUNT_ID()) {
													isLoop = true;
													Double sumDetit = journal.getDEBITS() + journalDetail.getDEBITS();
													journalDetail.setDEBITS(sumDetit);
													journalDetails.set(i, journalDetail);
													break;
												}
												i++;

											}
											if (isLoop == false) {
												journalDetails.add(journalDetail);
											}
										} else {
											journalDetails.add(journalDetail);
										}

									}

									JournalDetail journalDetail_1 = new JournalDetail();
									journalDetail_1.setPLANT(plant);
									JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
										JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
										if (!vendorJson.isEmpty()) {
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VNAME"));
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
												coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")
														+ "-" + vendorJson.getString("VNAME"));
											}

										}
									}
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

									} else {
										journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
										if (coaJson1.getString("account_name") != null) {
											journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
										}
										// journalDetail_1.setACCOUNT_NAME((String) vendno);
										journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
										journalDetails.add(journalDetail_1);
									}

									Double taxAmountFrom = Double.parseDouble(taxamount);
									if (taxAmountFrom > 0) {
										JournalDetail journalDetail_2 = new JournalDetail();
										journalDetail_2.setPLANT(plant);
										// JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
										// journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										// journalDetail_2.setACCOUNT_NAME("VAT Input");

										MasterDAO masterDAO = new MasterDAO();
										String planttaxtype = masterDAO.GetTaxType(plant);

										if (planttaxtype.equalsIgnoreCase("TAX")) {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("VAT Input");
										} else if (planttaxtype.equalsIgnoreCase("GST")) {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "GST Receivable");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("GST Receivable");
										} else if (planttaxtype.equalsIgnoreCase("VAT")) {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("VAT Input");
										} else {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("VAT Input");
										}

										journalDetail_2.setDEBITS(taxAmountFrom);
										journalDetails.add(journalDetail_2);
									}

									Double discountFrom = Double.parseDouble(discount);
									Double orderDiscountFrom = 0.00;
									if (!orderdiscount.isEmpty()) {
										orderDiscountFrom = Double.parseDouble(orderdiscount);
										orderDiscountFrom = (Double.parseDouble(subTotal) * orderDiscountFrom) / 100;
									}

									if (discountFrom > 0 || orderDiscountFrom > 0) {
										if (!discountType.isEmpty()) {
											if (discountType.equalsIgnoreCase("%")) {
												Double subTotalAfterOrderDiscount = Double.parseDouble(subTotal)
														- orderDiscountFrom;
												discountFrom = (subTotalAfterOrderDiscount * discountFrom) / 100;
											}
										}
										discountFrom = discountFrom + orderDiscountFrom;
										JournalDetail journalDetail_3 = new JournalDetail();
										journalDetail_3.setPLANT(plant);
										JSONObject coaJson3 = coaDAO.getCOAByName(plant, "Discount Received");
										journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
										journalDetail_3.setACCOUNT_NAME("Discount Received");
										journalDetail_3.setCREDITS(discountFrom);
										journalDetails.add(journalDetail_3);
									}

									if (!shippingCost.isEmpty()) {
										Double shippingCostFrom = Double.parseDouble(shippingCost);
										if (shippingCostFrom > 0) {
											JournalDetail journalDetail_4 = new JournalDetail();
											journalDetail_4.setPLANT(plant);
											JSONObject coaJson4 = coaDAO.getCOAByName(plant,
													"Inward freight & shipping");
											journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
											journalDetail_4.setACCOUNT_NAME("Inward freight & shipping");
											journalDetail_4.setDEBITS(shippingCostFrom);
											journalDetails.add(journalDetail_4);

											for (Map billDet : billDetInfoList) {
												Double quantity = Double.parseDouble(billDet.get("QTY").toString());
												String netWeight = itemMstDAO.getItemNetWeight(plant,
														billDet.get("ITEM").toString());
												Double Netweight = quantity * Double.parseDouble(netWeight);
												Double calculatedShippingCost = 0.0;
												if (totalItemNetWeight > 0) {
													if (Netweight > 0) {
														calculatedShippingCost = (shippingCostFrom * Netweight)
																/ totalItemNetWeight;
													} else {
														calculatedShippingCost = 0.00;
													}
												} else {
													calculatedShippingCost = shippingCostFrom / totalline;
												}
												System.out.println("calculatedShippingCost:" + calculatedShippingCost);

												JournalDetail journalDetail_5 = new JournalDetail();
												journalDetail_5.setPLANT(plant);
												JSONObject coaJson5 = coaDAO.getCOAByName(plant,
														(String) billDet.get("ACCOUNT_NAME"));
												journalDetail_5
														.setACCOUNT_ID(Integer.parseInt(coaJson5.getString("id")));
												journalDetail_5.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
												journalDetail_5.setDEBITS(calculatedShippingCost);
												boolean isLoop = false;
												if (journalReversalList.size() > 0) {
													int i = 0;
													for (JournalDetail journal : journalReversalList) {
														int accountId = journal.getACCOUNT_ID();
														if (accountId == journalDetail_5.getACCOUNT_ID()) {
															isLoop = true;
															Double sumDetit = journal.getDEBITS()
																	+ journalDetail_5.getDEBITS();
															journalDetail_5.setDEBITS(sumDetit);
															journalReversalList.set(i, journalDetail_5);
															break;
														}
														i++;

													}
													if (isLoop == false) {
														journalReversalList.add(journalDetail_5);
													}
												} else {
													journalReversalList.add(journalDetail_5);
												}
											}

											JournalDetail journalDetail_6 = new JournalDetail();
											journalDetail_6.setPLANT(plant);
											JSONObject coaJson6 = coaDAO.getCOAByName(plant,
													"Inward freight & shipping");
											journalDetail_6.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
											journalDetail_6.setACCOUNT_NAME("Inward freight & shipping");
											journalDetail_6.setCREDITS(shippingCostFrom);
											journalReversalList.add(journalDetail_6);
										}

									}
									if (!adjustment.isEmpty()) {
										Double adjustFrom = Double.parseDouble(adjustment);
										if (adjustFrom > 0) {
											JournalDetail journalDetail_7 = new JournalDetail();
											journalDetail_7.setPLANT(plant);
											JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
											journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
											journalDetail_7.setACCOUNT_NAME("Adjustment");
											journalDetail_7.setDEBITS(adjustFrom);
											journalDetails.add(journalDetail_7);
										} else if (adjustFrom < 0) {
											JournalDetail journalDetail_7 = new JournalDetail();
											journalDetail_7.setPLANT(plant);
											JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
											journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
											journalDetail_7.setACCOUNT_NAME("Adjustment");
											adjustFrom = Math.abs(adjustFrom);
											journalDetail_7.setCREDITS(adjustFrom);
											journalDetails.add(journalDetail_7);
										}
									}

									Journal journal = new Journal();
									Double totalDebitAmount = 0.00;
									for (JournalDetail jourDet : journalDetails) {
										totalDebitAmount = totalDebitAmount + jourDet.getDEBITS();
									}
									journalHead.setTOTAL_AMOUNT(totalDebitAmount);
									journal.setJournalHeader(journalHead);
									journal.setJournalDetails(journalDetails);
									JournalService journalService = new JournalEntry();
									journalService.addJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);

									List<JournalDetail> expensepo_journaldetails = new ArrayList<>();

									if (journalReversalList.size() > 0) {
										JournalHeader journalReversalHead = journalHead;
										Double totalDebitReversal = 0.00;
										for (JournalDetail journDet : journalReversalList) {
											totalDebitReversal = totalDebitReversal + journDet.getDEBITS();

										}
										journalReversalHead.setTOTAL_AMOUNT(totalDebitReversal);
										Journal journal_1 = new Journal();
										journalReversalHead.setTRANSACTION_TYPE("BILL_REVERSAL");
										journal_1.setJournalHeader(journalReversalHead);
										journal_1.setJournalDetails(journalReversalList);
										journalService.addJournal(journal_1, username);
										Hashtable jhtMovHis1 = new Hashtable();
										jhtMovHis1.put(IDBConstants.PLANT, plant);
										jhtMovHis1.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
										jhtMovHis1.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										jhtMovHis1.put(IDBConstants.ITEM, "");
										jhtMovHis1.put(IDBConstants.QTY, "0.0");
										jhtMovHis1.put("RECID", "");
										jhtMovHis1.put(IDBConstants.MOVHIS_ORDNUM,
												journal_1.getJournalHeader().getTRANSACTION_TYPE() + " "
														+ journal_1.getJournalHeader().getTRANSACTION_ID());
										jhtMovHis1.put(IDBConstants.CREATED_BY, username);
										jhtMovHis1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										jhtMovHis1.put("REMARKS", "");
										movHisDao.insertIntoMovHis(jhtMovHis1);
									}
								}
							}

						}

						if (isAdded) {
							for (int i = 0; i < InvoiceLine.length(); i++) {
								org.json.JSONObject explrObject = InvoiceLine.getJSONObject(i);

								org.json.JSONObject Price = (org.json.JSONObject) explrObject.get("cac:Price");
								org.json.JSONObject Item = (org.json.JSONObject) explrObject.get("cac:Item");
								org.json.JSONObject InvoicedQuantity = (org.json.JSONObject) explrObject
										.get("cbc:InvoicedQuantity");

								org.json.JSONObject PriceAmount = (org.json.JSONObject) Price.get("cbc:PriceAmount");
								org.json.JSONObject ClassifiedTaxCategory = (org.json.JSONObject) Item
										.get("cac:ClassifiedTaxCategory");

								String item = Item.getString("cbc:Name");
								String qty = InvoicedQuantity.getString("content");

								Hashtable htMovHisq = new Hashtable();
								htMovHisq.clear();
								htMovHisq.put(IDBConstants.PLANT, plant);
								htMovHisq.put("DIRTYPE", TransactionConstants.CREATE_BILL);
								htMovHisq.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billDate));
								htMovHisq.put(IDBConstants.ITEM, item);
								String billqty = qty;
								htMovHisq.put(IDBConstants.QTY, billqty);
								htMovHisq.put("RECID", "");
								htMovHisq.put(IDBConstants.MOVHIS_ORDNUM, billNo);
								htMovHisq.put(IDBConstants.CREATED_BY, username);
								htMovHisq.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHisq.put("REMARKS", pono + "," + grno + "," + refNum);
								isAdded = movHisDao.insertIntoMovHis(htMovHisq); // Insert MOVHIS
							}
						}

						// Added by Abhilash to handle COGS
						for (Map billDet : billDetInfoList) {

							List pendingCogsInvoiceDetails = new InvoiceDAO()
									.invoiceWoCOGS((String) billDet.get("ITEM"), plant);
							if (pendingCogsInvoiceDetails.size() > 0) {
								for (int i = 0; i < pendingCogsInvoiceDetails.size(); i++) {
									Double totalCostofGoodSold = 0.00;
									CoaDAO coaDAO = new CoaDAO();
									Journal journal = new Journal();
									List<JournalDetail> journalDetails = new ArrayList<>();
									Map pendingCogsInvoice = (Map) pendingCogsInvoiceDetails.get(i);

									Map invDetail = new InvMstDAO()
											.getInvDataByProduct((String) pendingCogsInvoice.get("ITEM"), plant);
									double inv_qty = 0, bill_qty = 0, unbill_qty = 0, net_bill_qty = 0,
											invoiced_qty = 0, invoice_quantity = 0, quantity = 0;
									inv_qty = Double.parseDouble((String) invDetail.get("INV_QTY"));
									bill_qty = Double.parseDouble((String) invDetail.get("BILL_QTY"));
									unbill_qty = Double.parseDouble((String) invDetail.get("UNBILL_QTY"));
									invoiced_qty = Double.parseDouble((String) invDetail.get("INVOICE_QTY"));
									invoice_quantity = Double.parseDouble((String) pendingCogsInvoice.get("QTY"));
									quantity = Double.parseDouble(pendingCogsInvoice.get("QTY").toString());

									bill_qty = bill_qty - invoiced_qty;
									net_bill_qty = bill_qty + unbill_qty;

									if ((net_bill_qty != inv_qty) && (bill_qty >= invoice_quantity)) {
										ArrayList invQryList;
										Hashtable ht_cog = new Hashtable();
										ht_cog.put("a.PLANT", plant);
										ht_cog.put("a.ITEM", (String) billDet.get("ITEM"));
										invQryList = new InvUtil().getAverageCost(ht_cog, plant,
												(String) billDet.get("ITEM"), curency, curency);
										if (invQryList != null) {
											if (!invQryList.isEmpty()) {
												Map lineArr = (Map) invQryList.get(0);
												String avg = StrUtils.addZeroes(
														Double.parseDouble((String) lineArr.get("AVERAGE_COST")), "2");
												totalCostofGoodSold += Double.parseDouble(avg) * (quantity);
											}
										}

										JournalService journalService = new JournalEntry();
										Journal journalCOG = journalService.getJournalByTransactionId(plant,
												(String) pendingCogsInvoice.get("INVOICE"), "COSTOFGOODSOLD");

										if (journalCOG.getJournalHeader() != null) {
											if (journalCOG.getJournalHeader().getID() > 0) {
												totalCostofGoodSold += journalCOG.getJournalHeader().getTOTAL_AMOUNT();
											}
										}

										JournalHeader journalHead = new JournalHeader();
										journalHead.setPLANT(plant);
										journalHead.setJOURNAL_DATE(DateUtils.getDate());
										journalHead.setJOURNAL_STATUS("PUBLISHED");
										journalHead.setJOURNAL_TYPE("Cash");
										journalHead.setCURRENCYID(curency);
										journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
										journalHead.setTRANSACTION_ID((String) pendingCogsInvoice.get("INVOICE"));
										journalHead.setSUB_TOTAL(totalCostofGoodSold);
										journalHead.setCRAT(DateUtils.getDateTime());
										journalHead.setCRBY(username);

										JournalDetail journalDetail_InvAsset = new JournalDetail();
										journalDetail_InvAsset.setPLANT(plant);
										JSONObject coaJson7 = coaDAO.getCOAByName(plant, "Inventory Asset");
										System.out.println("Json" + coaJson7.toString());
										journalDetail_InvAsset
												.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
										journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
										journalDetail_InvAsset.setCREDITS(totalCostofGoodSold);
										journalDetails.add(journalDetail_InvAsset);

										JournalDetail journalDetail_COG = new JournalDetail();
										journalDetail_COG.setPLANT(plant);
										JSONObject coaJson8 = coaDAO.getCOAByName(plant, "Cost of goods sold");
										System.out.println("Json" + coaJson8.toString());
										journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
										journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
										journalDetail_COG.setDEBITS(totalCostofGoodSold);
										journalDetails.add(journalDetail_COG);

										journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
										journal.setJournalHeader(journalHead);
										journal.setJournalDetails(journalDetails);

										if (journalCOG.getJournalHeader() != null) {
											if (journalCOG.getJournalHeader().getID() > 0) {
												journalHead.setID(journalCOG.getJournalHeader().getID());
												journalService.updateJournal(journal, username);
												Hashtable jhtMovHis = new Hashtable();
												jhtMovHis.put(IDBConstants.PLANT, plant);
												jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);
												jhtMovHis.put(IDBConstants.TRAN_DATE,
														DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												jhtMovHis.put(IDBConstants.ITEM, "");
												jhtMovHis.put(IDBConstants.QTY, "0.0");
												jhtMovHis.put("RECID", "");
												jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
														journal.getJournalHeader().getTRANSACTION_TYPE() + " "
																+ journal.getJournalHeader().getTRANSACTION_ID());
												jhtMovHis.put(IDBConstants.CREATED_BY, username);
												jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												jhtMovHis.put("REMARKS", "");
												movHisDao.insertIntoMovHis(jhtMovHis);
											} else {
												journalService.addJournal(journal, username);
												Hashtable jhtMovHis = new Hashtable();
												jhtMovHis.put(IDBConstants.PLANT, plant);
												jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
												jhtMovHis.put(IDBConstants.TRAN_DATE,
														DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												jhtMovHis.put(IDBConstants.ITEM, "");
												jhtMovHis.put(IDBConstants.QTY, "0.0");
												jhtMovHis.put("RECID", "");
												jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
														journal.getJournalHeader().getTRANSACTION_TYPE() + " "
																+ journal.getJournalHeader().getTRANSACTION_ID());
												jhtMovHis.put(IDBConstants.CREATED_BY, username);
												jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												jhtMovHis.put("REMARKS", "");
												movHisDao.insertIntoMovHis(jhtMovHis);
											}
										}
										new InvoiceDAO().update_is_cogs_set(
												(String) pendingCogsInvoice.get("INVOICEHDRID"),
												(String) pendingCogsInvoice.get("LNNO"),
												(String) pendingCogsInvoice.get("ITEM"), plant);
									}
								}
							}
						}
						
						if(isAdded) {
							peppolrecvdata.setBILL_STATUS((short) 1);
							peppolrecvdata.setBILLNO(billNo);
							new PeppolReceivedDataDAO().updatePeppolReceivedData(peppolrecvdata, username, peppolrecvdata.getID());
						}

						if (isAdded) {
							DbBean.CommitTran(ut);
							
							result = "Bill created successfully!";
						} else {
							DbBean.RollbackTran(ut);
							result = "Bill not created";
						}

					} catch (Exception e) {
						DbBean.RollbackTran(ut);
						e.printStackTrace();

					}

				
				} else {
					result = "Bill not created";
				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "Bill not created";
			}
			
			response.sendRedirect("../peppol/peppolpurchase?msg=" + result);

			/*
			 * if (result.equalsIgnoreCase("Bill not created")) { JSONObject resultJson =
			 * new JSONObject(); resultJson.put("MESSAGE", result);
			 * resultJson.put("ERROR_CODE", "99");
			 * response.setContentType("application/json");
			 * response.setCharacterEncoding("UTF-8");
			 * response.getWriter().write(resultJson.toString());
			 * response.getWriter().flush(); response.getWriter().close();
			 * 
			 * } else { JSONObject resultJson = new JSONObject(); resultJson.put("MESSAGE",
			 * "Bill Added Successfully"); resultJson.put("ERROR_CODE", "98");
			 * response.setContentType("application/json");
			 * response.setCharacterEncoding("UTF-8");
			 * response.getWriter().write(resultJson.toString());
			 * response.getWriter().flush(); response.getWriter().close();
			 * 
			 * }
			 */

		
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
		if (action.equalsIgnoreCase("savebill")) {
			String jsonString = "";
			String result = "";
			try {

				String id = request.getParameter("ID");
				PEPPOL_RECEIVED_DATA peppolrecvdata = new PeppolReceivedDataDAO().getPeppolReceivedDataById(plant,
						Integer.valueOf(id));

				URL obj1 = new URL(peppolrecvdata.getINVOICEFILEURL());
				HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
				con1.setRequestMethod("GET");
				String peppolukey = plantMstUtil.getPeppolUkey(plant);
				con1.setRequestProperty("api_key", peppolukey);
				System.out.println(con1.getResponseMessage());
				int responseCode1 = con1.getResponseCode();
				System.out.println("POST Response Code :: " + responseCode1);

				String msg1 = "";
				if (responseCode1 == 200) { // success
					BufferedReader in1 = new BufferedReader(new InputStreamReader(con1.getInputStream()));
					String inputLine1;
					StringBuffer oresponse1 = new StringBuffer();

					while ((inputLine1 = in1.readLine()) != null) {
						oresponse1.append(inputLine1);
					}
					in1.close();
					// print result
					System.out.println("-------------xml--------------");
					System.out.println(oresponse1.toString());
					System.out.println("-------------xml--------------");

					org.json.JSONObject json = XML.toJSONObject(oresponse1.toString());
					jsonString = json.toString();
					org.json.JSONObject jstrings = (org.json.JSONObject) json.get("StandardBusinessDocument");
					org.json.JSONObject jstring = (org.json.JSONObject) jstrings.get("Invoice");
					System.out.println("-------------json--------------");
					System.out.println(jstring.toString());
					System.out.println("-------------json--------------");

					org.json.JSONObject invoice = (org.json.JSONObject) jstrings.get("Invoice");
					org.json.JSONObject AccountingSupplierParty = (org.json.JSONObject) invoice
							.get("cac:AccountingSupplierParty");
					org.json.JSONObject Party = (org.json.JSONObject) AccountingSupplierParty.get("cac:Party");
					org.json.JSONObject PartyLegalEntity = (org.json.JSONObject) Party.get("cac:PartyLegalEntity");

					org.json.JSONObject LegalMonetaryTotal = (org.json.JSONObject) invoice
							.get("cac:LegalMonetaryTotal");
					org.json.JSONObject PayableAmount = (org.json.JSONObject) LegalMonetaryTotal
							.get("cbc:PayableAmount");
					org.json.JSONObject LineExtensionAmount = (org.json.JSONObject) LegalMonetaryTotal
							.get("cbc:LineExtensionAmount");

					org.json.JSONObject PaymentTerms = (org.json.JSONObject) invoice.get("cac:PaymentTerms");

					org.json.JSONObject TaxTotal = (org.json.JSONObject) invoice.get("cac:TaxTotal");
					org.json.JSONObject TaxAmount = (org.json.JSONObject) TaxTotal.get("cbc:TaxAmount");

					org.json.JSONObject PostalAddress = (org.json.JSONObject) Party.get("cac:PostalAddress");
					org.json.JSONObject Country = (org.json.JSONObject) PostalAddress.get("cac:Country");

					org.json.JSONObject TaxSubtotal = (org.json.JSONObject) TaxTotal.get("cac:TaxSubtotal");
					org.json.JSONObject TaxCategory = (org.json.JSONObject) TaxSubtotal.get("cac:TaxCategory");

					String vendname = PartyLegalEntity.getString("cbc:RegistrationName");
					String currency = PayableAmount.getString("currencyID");
					String payterms = PaymentTerms.getString("cbc:Note");
					String issuedate = invoice.getString("cbc:IssueDate");
					String countrycode = Country.getString("cbc:IdentificationCode");
					String otaxcode = TaxCategory.getString("cbc:ID");

					org.json.JSONArray InvoiceLine = (org.json.JSONArray) invoice.get("cac:InvoiceLine");

					/*
					 * for (int i = 0; i < InvoiceLine.length(); i++) { org.json.JSONObject
					 * explrObject = InvoiceLine.getJSONObject(i);
					 * 
					 * org.json.JSONObject Price = (org.json.JSONObject)
					 * explrObject.get("cac:Price"); org.json.JSONObject Item =
					 * (org.json.JSONObject) explrObject.get("cac:Item"); org.json.JSONObject
					 * InvoicedQuantity = (org.json.JSONObject)
					 * explrObject.get("cbc:InvoicedQuantity");
					 * 
					 * org.json.JSONObject PriceAmount = (org.json.JSONObject)
					 * Price.get("cbc:PriceAmount"); org.json.JSONObject ClassifiedTaxCategory =
					 * (org.json.JSONObject) Item.get("cac:ClassifiedTaxCategory");
					 * 
					 * String lnno =explrObject.getString("cbc:ID");; String unitcost
					 * =PriceAmount.getString("content"); String taxcode
					 * =ClassifiedTaxCategory.getString("cbc:ID"); String taxpercentage
					 * =ClassifiedTaxCategory.getString("cbc:Percent"); String item
					 * =Item.getString("cbc:Name"); String uom
					 * =InvoicedQuantity.getString("unitCode"); String qty
					 * =InvoicedQuantity.getString("content");
					 * 
					 * 
					 * 
					 * }
					 */

					List<Hashtable<String, String>> billDetInfoList = null;
					Hashtable<String, String> billDetInfo = null;
					UserTransaction ut = null;
					BillUtil billUtil = new BillUtil();
					RecvDetDAO recvDao = new RecvDetDAO();
					MovHisDAO movHisDao = new MovHisDAO();
					boolean isAdded = false;

					try {

						Hashtable ht = new Hashtable();
						ht.put("PLANT", plant);
						ht.put("VNAME", vendname);

						String vendno = new VendMstDAO().getVendorNoByName(ht);
						String billNo = invoice.getString("cbc:ID");
						String pono = "";
						String grno = "";
						String billDate = new DateUtils().getDateyyyymmdd(issuedate);
						String dueDate = "";
						String payTerms = PaymentTerms.getString("cbc:Note");
						String empno = "";
						String itemRates = "0";
						String discountType = currency;
						String discountAccount = "";
						String adjustmentLabel = "Adjustment";
						String adjustment = "0.0";
						String totalAmount = PayableAmount.getString("content");
						String subTotal = LineExtensionAmount.getString("content");
						String billStatus = "Open";
						String shipRef = "";
						String refNum = "";
						String note = "";
						String taxamount = TaxAmount.getString("content");
						String isshtax = "0";
						String purchaseloc = "";
						String taxtreatment = "GST Registered";
						String sREVERSECHARGE = "";
						String sGOODSIMPORT = "";
						String currencyid = currency;
						String currencyuseqt = "1.0";
						String orddiscounttype = "%";
						String taxid = "";
						FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();

						if (otaxcode.equalsIgnoreCase("SR")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "STANDARD RATED");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else if (otaxcode.equalsIgnoreCase("ZR")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "ZERO RATE");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else if (otaxcode.equalsIgnoreCase("OS")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "OUT OF SCOPE");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else if (otaxcode.equalsIgnoreCase("ES33")) {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "EXEMPT");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						} else {
							List<FinCountryTaxType> finCountryTaxType = finCountryTaxTypeDAO
									.getCountryTaxTypes("INBOUND", countrycode, "EXEMPT");
							taxid = String.valueOf(finCountryTaxType.get(0).getID());
						}
						String isdiscounttax = "1";
						String isorderdiscounttax = "1";
						String discount = "0.0";
						String shippingCost = "0.0";
						String orderdiscount = "0.000";
						String gst = TaxCategory.getString("cbc:Percent");
						String projectid = "";
						String transportid = "0";
						String deductInv = "0";
						String billtype = "NON INVENTORY";

						billDetInfoList = new ArrayList<Hashtable<String, String>>();
						Hashtable billHdr = new Hashtable();
						billHdr.put("PLANT", plant);
						billHdr.put("VENDNO", vendno);
						billHdr.put("BILL", billNo);
						billHdr.put("PONO", pono);
						billHdr.put("GRNO", grno);
						billHdr.put("BILL_DATE", billDate);
						billHdr.put("DUE_DATE", dueDate);
						billHdr.put("PAYMENT_TERMS", payTerms);
						billHdr.put("EMPNO", empno);
						billHdr.put("ITEM_RATES", itemRates);
						billHdr.put("DISCOUNT_TYPE", discountType);
						billHdr.put("DISCOUNT_ACCOUNT", discountAccount);
						billHdr.put("ADJUSTMENT_LABEL", adjustmentLabel);
						billHdr.put("ADJUSTMENT", adjustment);
						billHdr.put("SUB_TOTAL", subTotal);
						billHdr.put("TOTAL_AMOUNT", totalAmount);
						billHdr.put("BILL_STATUS", billStatus);
						billHdr.put("SHIPMENT_CODE", shipRef);
						billHdr.put("REFERENCE_NUMBER", refNum);
						billHdr.put("NOTE", note);
						billHdr.put("CRAT", DateUtils.getDateTime());
						billHdr.put("CRBY", username);
						billHdr.put("UPAT", DateUtils.getDateTime());
						billHdr.put("CREDITNOTESSTATUS", "0");
						billHdr.put("TAXAMOUNT", taxamount);
						billHdr.put("ISSHIPPINGTAXABLE", isshtax);
						billHdr.put("PURCHASE_LOCATION", purchaseloc);
						billHdr.put("TAXTREATMENT", taxtreatment);
						billHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
						billHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
						billHdr.put(IDBConstants.CURRENCYID, currencyid);
						billHdr.put("CURRENCYUSEQT", currencyuseqt);
						billHdr.put("ORDERDISCOUNTTYPE", orddiscounttype);
						billHdr.put("TAXID", taxid);
						billHdr.put("ISDISCOUNTTAX", isdiscounttax);
						billHdr.put("ISORDERDISCOUNTTAX", isorderdiscounttax);
						billHdr.put("DISCOUNT", discount);
						billHdr.put("SHIPPINGCOST", shippingCost);
						billHdr.put("ORDER_DISCOUNT", orderdiscount);
						billHdr.put("INBOUND_GST", gst);
						billHdr.put("PROJECTID", projectid);
						billHdr.put("TRANSPORTID", transportid);
						billHdr.put("SHIPCONTACTNAME", "");
						billHdr.put("SHIPDESGINATION", "");
						billHdr.put("SHIPWORKPHONE", "");
						billHdr.put("SHIPHPNO", "");
						billHdr.put("SHIPEMAIL", "");
						billHdr.put("SHIPCOUNTRY", "");
						billHdr.put("SHIPADDR1", "");
						billHdr.put("SHIPADDR2", "");
						billHdr.put("SHIPADDR3", "");
						billHdr.put("SHIPADDR4", "");
						billHdr.put("SHIPSTATE", "");
						billHdr.put("SHIPZIP", "");
						billHdr.put("SHIPPINGID", "");
						billHdr.put("SHIPPINGCUSTOMER", "");
						billHdr.put("ORIGIN", "PEPPOL");
						billHdr.put("DEDUCT_INV", deductInv);
						billHdr.put("BILL_TYPE", billtype);

						ExpensesDAO expenseDao = new ExpensesDAO();
						CoaDAO coaDao = new CoaDAO();
						BillDAO itemCogsDao = new BillDAO();
						List<CostofgoodsLanded> landedCostLst = new ArrayList<>();
//										List<ItemCogs> lstCogs=new ArrayList<>();
						ItemMstDAO itemmstDao = new ItemMstDAO();
						Costofgoods costofGoods = new CostofgoodsImpl();
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
								.trim();
						double expenseAmt = 0.0;
						// Get Transaction object
						ut = DbBean.getUserTranaction();
						// Begin Transaction
						ut.begin();
						int billHdrId = billUtil.addBillHdr(billHdr, plant);
						if (billHdrId > 0) {
							for (int i = 0; i < InvoiceLine.length(); i++) {
								org.json.JSONObject explrObject = InvoiceLine.getJSONObject(i);

								org.json.JSONObject Price = (org.json.JSONObject) explrObject.get("cac:Price");
								org.json.JSONObject Item = (org.json.JSONObject) explrObject.get("cac:Item");
								org.json.JSONObject InvoicedQuantity = (org.json.JSONObject) explrObject
										.get("cbc:InvoicedQuantity");

								org.json.JSONObject PriceAmount = (org.json.JSONObject) Price.get("cbc:PriceAmount");
								org.json.JSONObject ClassifiedTaxCategory = (org.json.JSONObject) Item
										.get("cac:ClassifiedTaxCategory");

								String lnno = explrObject.getString("cbc:ID");
								;
								String unitcost = PriceAmount.getString("content");
								String taxcode = ClassifiedTaxCategory.getString("cbc:ID");
								String taxpercentage = ClassifiedTaxCategory.getString("cbc:Percent");
								String item = Item.getString("cbc:Name");
								String uom = InvoicedQuantity.getString("unitCode");
								String qty = InvoicedQuantity.getString("content");

								double lamount = Double.valueOf(unitcost) * Double.valueOf(qty);

								billDetInfo = new Hashtable<String, String>();
								billDetInfo.put("PLANT", plant);
								billDetInfo.put("LNNO", lnno);
								billDetInfo.put("BILLHDRID", Integer.toString(billHdrId));
								billDetInfo.put("ITEM", item);
								billDetInfo.put("ACCOUNT_NAME", "Inventory Asset");
								billDetInfo.put("QTY", qty);
								billDetInfo.put("COST", unitcost);
								billDetInfo.put("DISCOUNT", "0.0");
								billDetInfo.put("DISCOUNT_TYPE", currency);

								String taxdettype = "";
								double taxper = Double.valueOf(taxpercentage);
								if (taxcode.equalsIgnoreCase("SR")) {
									taxdettype = "STANDARD RATED [" + taxper + "%]";
								} else if (taxcode.equalsIgnoreCase("ZR")) {
									taxdettype = "ZERO RATE [0.0%]";
								} else if (taxcode.equalsIgnoreCase("OS")) {
									taxdettype = "OUT OF SCOPE";
								} else if (taxcode.equalsIgnoreCase("ES33")) {
									taxdettype = "EXEMPT";
								} else {
									taxdettype = "ZERO RATE [0.0%]";
								}

								billDetInfo.put("Tax_Type", taxdettype);
								billDetInfo.put("Amount", String.valueOf(lamount));
								billDetInfo.put("LANDED_COST", "0");
								billDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								billDetInfo.put("LOC", "");
								billDetInfo.put("BATCH", "NOBATCH");
								billDetInfo.put("UOM", uom);
								billDetInfo.put("CRAT", DateUtils.getDateTime());
								billDetInfo.put("CRBY", username);
								billDetInfo.put("UPAT", DateUtils.getDateTime());
								billDetInfoList.add(billDetInfo);

							}
							isAdded = billUtil.addMultipleBillDet(billDetInfoList, plant);
							if (isAdded) {

								System.out.println("billStatus" + billStatus);
								if (!billStatus.equalsIgnoreCase("Draft")) {
									// Journal Entry
									JournalHeader journalHead = new JournalHeader();
									journalHead.setPLANT(plant);
									journalHead.setJOURNAL_DATE(billDate);
									journalHead.setJOURNAL_STATUS("PUBLISHED");
									journalHead.setJOURNAL_TYPE("Cash");
									journalHead.setCURRENCYID(curency);
									journalHead.setTRANSACTION_TYPE("BILL");
									journalHead.setTRANSACTION_ID(billNo);
									journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
									// journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
									journalHead.setCRAT(DateUtils.getDateTime());
									journalHead.setCRBY(username);

									List<JournalDetail> journalDetails = new ArrayList<>();
									List<JournalDetail> journalReversalList = new ArrayList<>();
									CoaDAO coaDAO = new CoaDAO();
									VendMstDAO vendorDAO = new VendMstDAO();
									ItemMstDAO itemMstDAO = new ItemMstDAO();
									Double totalItemNetWeight = 0.00;
									Double totalline = 0.00;
									for (Map billDet : billDetInfoList) {
										Double quantity = Double.parseDouble(billDet.get("QTY").toString());
										totalline++;
										String netWeight = itemMstDAO.getItemNetWeight(plant,
												billDet.get("ITEM").toString());
//														TODO : Ravindra - Get verified
										if (netWeight == null || "".equals(netWeight)) {
											netWeight = "0";
										}
										Double Netweight = quantity * Double.parseDouble(netWeight);
										totalItemNetWeight += Netweight;
										System.out.println("TotalNetWeight:" + totalItemNetWeight);

										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant,
												(String) billDet.get("ACCOUNT_NAME"));
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
										if (!orddiscounttype.toString().equalsIgnoreCase("%")) {
											journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString())
													- Double.parseDouble(orderdiscount.toString())
															/ billDetInfoList.size());
										} else {
											Double jodamt = (Double.parseDouble(billDet.get("Amount").toString()) / 100)
													* Double.parseDouble(orderdiscount.toString());
											journalDetail.setDEBITS(
													Double.parseDouble(billDet.get("Amount").toString()) - jodamt);
										}

										boolean isLoop = false;
										if (journalDetails.size() > 0) {
											int i = 0;
											for (JournalDetail journal : journalDetails) {
												int accountId = journal.getACCOUNT_ID();
												if (accountId == journalDetail.getACCOUNT_ID()) {
													isLoop = true;
													Double sumDetit = journal.getDEBITS() + journalDetail.getDEBITS();
													journalDetail.setDEBITS(sumDetit);
													journalDetails.set(i, journalDetail);
													break;
												}
												i++;

											}
											if (isLoop == false) {
												journalDetails.add(journalDetail);
											}
										} else {
											journalDetails.add(journalDetail);
										}

									}

									JournalDetail journalDetail_1 = new JournalDetail();
									journalDetail_1.setPLANT(plant);
									JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
										JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
										if (!vendorJson.isEmpty()) {
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VNAME"));
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
												coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")
														+ "-" + vendorJson.getString("VNAME"));
											}

										}
									}
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

									} else {
										journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
										if (coaJson1.getString("account_name") != null) {
											journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
										}
										// journalDetail_1.setACCOUNT_NAME((String) vendno);
										journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
										journalDetails.add(journalDetail_1);
									}

									Double taxAmountFrom = Double.parseDouble(taxamount);
									if (taxAmountFrom > 0) {
										JournalDetail journalDetail_2 = new JournalDetail();
										journalDetail_2.setPLANT(plant);
										// JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
										// journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										// journalDetail_2.setACCOUNT_NAME("VAT Input");

										MasterDAO masterDAO = new MasterDAO();
										String planttaxtype = masterDAO.GetTaxType(plant);

										if (planttaxtype.equalsIgnoreCase("TAX")) {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("VAT Input");
										} else if (planttaxtype.equalsIgnoreCase("GST")) {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "GST Receivable");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("GST Receivable");
										} else if (planttaxtype.equalsIgnoreCase("VAT")) {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("VAT Input");
										} else {
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Input");
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("VAT Input");
										}

										journalDetail_2.setDEBITS(taxAmountFrom);
										journalDetails.add(journalDetail_2);
									}

									Double discountFrom = Double.parseDouble(discount);
									Double orderDiscountFrom = 0.00;
									if (!orderdiscount.isEmpty()) {
										orderDiscountFrom = Double.parseDouble(orderdiscount);
										orderDiscountFrom = (Double.parseDouble(subTotal) * orderDiscountFrom) / 100;
									}

									if (discountFrom > 0 || orderDiscountFrom > 0) {
										if (!discountType.isEmpty()) {
											if (discountType.equalsIgnoreCase("%")) {
												Double subTotalAfterOrderDiscount = Double.parseDouble(subTotal)
														- orderDiscountFrom;
												discountFrom = (subTotalAfterOrderDiscount * discountFrom) / 100;
											}
										}
										discountFrom = discountFrom + orderDiscountFrom;
										JournalDetail journalDetail_3 = new JournalDetail();
										journalDetail_3.setPLANT(plant);
										JSONObject coaJson3 = coaDAO.getCOAByName(plant, "Discount Received");
										journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
										journalDetail_3.setACCOUNT_NAME("Discount Received");
										journalDetail_3.setCREDITS(discountFrom);
										journalDetails.add(journalDetail_3);
									}

									if (!shippingCost.isEmpty()) {
										Double shippingCostFrom = Double.parseDouble(shippingCost);
										if (shippingCostFrom > 0) {
											JournalDetail journalDetail_4 = new JournalDetail();
											journalDetail_4.setPLANT(plant);
											JSONObject coaJson4 = coaDAO.getCOAByName(plant,
													"Inward freight & shipping");
											journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
											journalDetail_4.setACCOUNT_NAME("Inward freight & shipping");
											journalDetail_4.setDEBITS(shippingCostFrom);
											journalDetails.add(journalDetail_4);

											for (Map billDet : billDetInfoList) {
												Double quantity = Double.parseDouble(billDet.get("QTY").toString());
												String netWeight = itemMstDAO.getItemNetWeight(plant,
														billDet.get("ITEM").toString());
												Double Netweight = quantity * Double.parseDouble(netWeight);
												Double calculatedShippingCost = 0.0;
												if (totalItemNetWeight > 0) {
													if (Netweight > 0) {
														calculatedShippingCost = (shippingCostFrom * Netweight)
																/ totalItemNetWeight;
													} else {
														calculatedShippingCost = 0.00;
													}
												} else {
													calculatedShippingCost = shippingCostFrom / totalline;
												}
												System.out.println("calculatedShippingCost:" + calculatedShippingCost);

												JournalDetail journalDetail_5 = new JournalDetail();
												journalDetail_5.setPLANT(plant);
												JSONObject coaJson5 = coaDAO.getCOAByName(plant,
														(String) billDet.get("ACCOUNT_NAME"));
												journalDetail_5
														.setACCOUNT_ID(Integer.parseInt(coaJson5.getString("id")));
												journalDetail_5.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
												journalDetail_5.setDEBITS(calculatedShippingCost);
												boolean isLoop = false;
												if (journalReversalList.size() > 0) {
													int i = 0;
													for (JournalDetail journal : journalReversalList) {
														int accountId = journal.getACCOUNT_ID();
														if (accountId == journalDetail_5.getACCOUNT_ID()) {
															isLoop = true;
															Double sumDetit = journal.getDEBITS()
																	+ journalDetail_5.getDEBITS();
															journalDetail_5.setDEBITS(sumDetit);
															journalReversalList.set(i, journalDetail_5);
															break;
														}
														i++;

													}
													if (isLoop == false) {
														journalReversalList.add(journalDetail_5);
													}
												} else {
													journalReversalList.add(journalDetail_5);
												}
											}

											JournalDetail journalDetail_6 = new JournalDetail();
											journalDetail_6.setPLANT(plant);
											JSONObject coaJson6 = coaDAO.getCOAByName(plant,
													"Inward freight & shipping");
											journalDetail_6.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
											journalDetail_6.setACCOUNT_NAME("Inward freight & shipping");
											journalDetail_6.setCREDITS(shippingCostFrom);
											journalReversalList.add(journalDetail_6);
										}

									}
									if (!adjustment.isEmpty()) {
										Double adjustFrom = Double.parseDouble(adjustment);
										if (adjustFrom > 0) {
											JournalDetail journalDetail_7 = new JournalDetail();
											journalDetail_7.setPLANT(plant);
											JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
											journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
											journalDetail_7.setACCOUNT_NAME("Adjustment");
											journalDetail_7.setDEBITS(adjustFrom);
											journalDetails.add(journalDetail_7);
										} else if (adjustFrom < 0) {
											JournalDetail journalDetail_7 = new JournalDetail();
											journalDetail_7.setPLANT(plant);
											JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
											journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
											journalDetail_7.setACCOUNT_NAME("Adjustment");
											adjustFrom = Math.abs(adjustFrom);
											journalDetail_7.setCREDITS(adjustFrom);
											journalDetails.add(journalDetail_7);
										}
									}

									Journal journal = new Journal();
									Double totalDebitAmount = 0.00;
									for (JournalDetail jourDet : journalDetails) {
										totalDebitAmount = totalDebitAmount + jourDet.getDEBITS();
									}
									journalHead.setTOTAL_AMOUNT(totalDebitAmount);
									journal.setJournalHeader(journalHead);
									journal.setJournalDetails(journalDetails);
									JournalService journalService = new JournalEntry();
									journalService.addJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);

									List<JournalDetail> expensepo_journaldetails = new ArrayList<>();

									if (journalReversalList.size() > 0) {
										JournalHeader journalReversalHead = journalHead;
										Double totalDebitReversal = 0.00;
										for (JournalDetail journDet : journalReversalList) {
											totalDebitReversal = totalDebitReversal + journDet.getDEBITS();

										}
										journalReversalHead.setTOTAL_AMOUNT(totalDebitReversal);
										Journal journal_1 = new Journal();
										journalReversalHead.setTRANSACTION_TYPE("BILL_REVERSAL");
										journal_1.setJournalHeader(journalReversalHead);
										journal_1.setJournalDetails(journalReversalList);
										journalService.addJournal(journal_1, username);
										Hashtable jhtMovHis1 = new Hashtable();
										jhtMovHis1.put(IDBConstants.PLANT, plant);
										jhtMovHis1.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
										jhtMovHis1.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										jhtMovHis1.put(IDBConstants.ITEM, "");
										jhtMovHis1.put(IDBConstants.QTY, "0.0");
										jhtMovHis1.put("RECID", "");
										jhtMovHis1.put(IDBConstants.MOVHIS_ORDNUM,
												journal_1.getJournalHeader().getTRANSACTION_TYPE() + " "
														+ journal_1.getJournalHeader().getTRANSACTION_ID());
										jhtMovHis1.put(IDBConstants.CREATED_BY, username);
										jhtMovHis1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										jhtMovHis1.put("REMARKS", "");
										movHisDao.insertIntoMovHis(jhtMovHis1);
									}
								}
							}

						}

						if (isAdded) {
							for (int i = 0; i < InvoiceLine.length(); i++) {
								org.json.JSONObject explrObject = InvoiceLine.getJSONObject(i);

								org.json.JSONObject Price = (org.json.JSONObject) explrObject.get("cac:Price");
								org.json.JSONObject Item = (org.json.JSONObject) explrObject.get("cac:Item");
								org.json.JSONObject InvoicedQuantity = (org.json.JSONObject) explrObject
										.get("cbc:InvoicedQuantity");

								org.json.JSONObject PriceAmount = (org.json.JSONObject) Price.get("cbc:PriceAmount");
								org.json.JSONObject ClassifiedTaxCategory = (org.json.JSONObject) Item
										.get("cac:ClassifiedTaxCategory");

								String item = Item.getString("cbc:Name");
								String qty = InvoicedQuantity.getString("content");

								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_BILL);
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billDate));
								htMovHis.put(IDBConstants.ITEM, item);
								String billqty = qty;
								htMovHis.put(IDBConstants.QTY, billqty);
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, billNo);
								htMovHis.put(IDBConstants.CREATED_BY, username);
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS", pono + "," + grno + "," + refNum);
								isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							}
						}

						// Added by Abhilash to handle COGS
						for (Map billDet : billDetInfoList) {

							List pendingCogsInvoiceDetails = new InvoiceDAO()
									.invoiceWoCOGS((String) billDet.get("ITEM"), plant);
							if (pendingCogsInvoiceDetails.size() > 0) {
								for (int i = 0; i < pendingCogsInvoiceDetails.size(); i++) {
									Double totalCostofGoodSold = 0.00;
									CoaDAO coaDAO = new CoaDAO();
									Journal journal = new Journal();
									List<JournalDetail> journalDetails = new ArrayList<>();
									Map pendingCogsInvoice = (Map) pendingCogsInvoiceDetails.get(i);

									Map invDetail = new InvMstDAO()
											.getInvDataByProduct((String) pendingCogsInvoice.get("ITEM"), plant);
									double inv_qty = 0, bill_qty = 0, unbill_qty = 0, net_bill_qty = 0,
											invoiced_qty = 0, invoice_quantity = 0, quantity = 0;
									inv_qty = Double.parseDouble((String) invDetail.get("INV_QTY"));
									bill_qty = Double.parseDouble((String) invDetail.get("BILL_QTY"));
									unbill_qty = Double.parseDouble((String) invDetail.get("UNBILL_QTY"));
									invoiced_qty = Double.parseDouble((String) invDetail.get("INVOICE_QTY"));
									invoice_quantity = Double.parseDouble((String) pendingCogsInvoice.get("QTY"));
									quantity = Double.parseDouble(pendingCogsInvoice.get("QTY").toString());

									bill_qty = bill_qty - invoiced_qty;
									net_bill_qty = bill_qty + unbill_qty;

									if ((net_bill_qty != inv_qty) && (bill_qty >= invoice_quantity)) {
										ArrayList invQryList;
										Hashtable ht_cog = new Hashtable();
										ht_cog.put("a.PLANT", plant);
										ht_cog.put("a.ITEM", (String) billDet.get("ITEM"));
										invQryList = new InvUtil().getAverageCost(ht_cog, plant,
												(String) billDet.get("ITEM"), curency, curency);
										if (invQryList != null) {
											if (!invQryList.isEmpty()) {
												Map lineArr = (Map) invQryList.get(0);
												String avg = StrUtils.addZeroes(
														Double.parseDouble((String) lineArr.get("AVERAGE_COST")), "2");
												totalCostofGoodSold += Double.parseDouble(avg) * (quantity);
											}
										}

										JournalService journalService = new JournalEntry();
										Journal journalCOG = journalService.getJournalByTransactionId(plant,
												(String) pendingCogsInvoice.get("INVOICE"), "COSTOFGOODSOLD");

										if (journalCOG.getJournalHeader() != null) {
											if (journalCOG.getJournalHeader().getID() > 0) {
												totalCostofGoodSold += journalCOG.getJournalHeader().getTOTAL_AMOUNT();
											}
										}

										JournalHeader journalHead = new JournalHeader();
										journalHead.setPLANT(plant);
										journalHead.setJOURNAL_DATE(DateUtils.getDate());
										journalHead.setJOURNAL_STATUS("PUBLISHED");
										journalHead.setJOURNAL_TYPE("Cash");
										journalHead.setCURRENCYID(curency);
										journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
										journalHead.setTRANSACTION_ID((String) pendingCogsInvoice.get("INVOICE"));
										journalHead.setSUB_TOTAL(totalCostofGoodSold);
										journalHead.setCRAT(DateUtils.getDateTime());
										journalHead.setCRBY(username);

										JournalDetail journalDetail_InvAsset = new JournalDetail();
										journalDetail_InvAsset.setPLANT(plant);
										JSONObject coaJson7 = coaDAO.getCOAByName(plant, "Inventory Asset");
										System.out.println("Json" + coaJson7.toString());
										journalDetail_InvAsset
												.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
										journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
										journalDetail_InvAsset.setCREDITS(totalCostofGoodSold);
										journalDetails.add(journalDetail_InvAsset);

										JournalDetail journalDetail_COG = new JournalDetail();
										journalDetail_COG.setPLANT(plant);
										JSONObject coaJson8 = coaDAO.getCOAByName(plant, "Cost of goods sold");
										System.out.println("Json" + coaJson8.toString());
										journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
										journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
										journalDetail_COG.setDEBITS(totalCostofGoodSold);
										journalDetails.add(journalDetail_COG);

										journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
										journal.setJournalHeader(journalHead);
										journal.setJournalDetails(journalDetails);

										if (journalCOG.getJournalHeader() != null) {
											if (journalCOG.getJournalHeader().getID() > 0) {
												journalHead.setID(journalCOG.getJournalHeader().getID());
												journalService.updateJournal(journal, username);
												Hashtable jhtMovHis = new Hashtable();
												jhtMovHis.put(IDBConstants.PLANT, plant);
												jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);
												jhtMovHis.put(IDBConstants.TRAN_DATE,
														DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												jhtMovHis.put(IDBConstants.ITEM, "");
												jhtMovHis.put(IDBConstants.QTY, "0.0");
												jhtMovHis.put("RECID", "");
												jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
														journal.getJournalHeader().getTRANSACTION_TYPE() + " "
																+ journal.getJournalHeader().getTRANSACTION_ID());
												jhtMovHis.put(IDBConstants.CREATED_BY, username);
												jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												jhtMovHis.put("REMARKS", "");
												movHisDao.insertIntoMovHis(jhtMovHis);
											} else {
												journalService.addJournal(journal, username);
												Hashtable jhtMovHis = new Hashtable();
												jhtMovHis.put(IDBConstants.PLANT, plant);
												jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
												jhtMovHis.put(IDBConstants.TRAN_DATE,
														DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												jhtMovHis.put(IDBConstants.ITEM, "");
												jhtMovHis.put(IDBConstants.QTY, "0.0");
												jhtMovHis.put("RECID", "");
												jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
														journal.getJournalHeader().getTRANSACTION_TYPE() + " "
																+ journal.getJournalHeader().getTRANSACTION_ID());
												jhtMovHis.put(IDBConstants.CREATED_BY, username);
												jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												jhtMovHis.put("REMARKS", "");
												movHisDao.insertIntoMovHis(jhtMovHis);
											}
										}
										new InvoiceDAO().update_is_cogs_set(
												(String) pendingCogsInvoice.get("INVOICEHDRID"),
												(String) pendingCogsInvoice.get("LNNO"),
												(String) pendingCogsInvoice.get("ITEM"), plant);
									}
								}
							}
						}
						
						if(isAdded) {
							peppolrecvdata.setBILL_STATUS((short) 1);
							peppolrecvdata.setBILLNO(billNo);
							new PeppolReceivedDataDAO().updatePeppolReceivedData(peppolrecvdata, username, peppolrecvdata.getID());
						}

						if (isAdded) {
							DbBean.CommitTran(ut);
							
							result = "Bill created successfully!";
						} else {
							DbBean.RollbackTran(ut);
							result = "Bill not created";
						}

					} catch (Exception e) {
						DbBean.RollbackTran(ut);
						e.printStackTrace();

					}

				} else {
					result = "Bill not created";
				}

			} catch (Exception e) {
				e.printStackTrace();
				result = "Bill not created";
			}
			
			response.sendRedirect("../peppol/peppolpurchase?msg=" + result);

			/*
			 * if (result.equalsIgnoreCase("Bill not created")) { JSONObject resultJson =
			 * new JSONObject(); resultJson.put("MESSAGE", result);
			 * resultJson.put("ERROR_CODE", "99");
			 * response.setContentType("application/json");
			 * response.setCharacterEncoding("UTF-8");
			 * response.getWriter().write(resultJson.toString());
			 * response.getWriter().flush(); response.getWriter().close();
			 * 
			 * } else { JSONObject resultJson = new JSONObject(); resultJson.put("MESSAGE",
			 * "Bill Added Successfully"); resultJson.put("ERROR_CODE", "98");
			 * response.setContentType("application/json");
			 * response.setCharacterEncoding("UTF-8");
			 * response.getWriter().write(resultJson.toString());
			 * response.getWriter().flush(); response.getWriter().close();
			 * 
			 * }
			 */

		}
	}

	private void invoicePDFGenration(HttpServletRequest request, HttpServletResponse response, String invoiceNo,
			int invoiceHdrId) throws IOException {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		String rootURI = HttpUtils.getRootURI(request);
		String invoiceDetailURL = rootURI + "/invoice/detail?INVOICE_HDR=" + invoiceHdrId + "&PLANT="
				+ StrUtils.fString((String) session.getAttribute("PLANT")) + "&SYSTEMNOW="
				+ StrUtils.fString((String) session.getAttribute("SYSTEMNOW")) + "&BASE_CURRENCY="
				+ StrUtils.fString((String) session.getAttribute("BASE_CURRENCY")) + "&LOGIN_USER="
				+ StrUtils.fString((String) session.getAttribute("LOGIN_USER")) + "&NOOFPAYMENT="
				+ StrUtils.fString((String) session.getAttribute("NOOFPAYMENT")) + "&LOGIN_USER="
				+ StrUtils.fString((String) session.getAttribute("LOGIN_USER")) + "&INTERNAL_REQUESET=TRUE";
		URL url = new URL(invoiceDetailURL);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		StringBuffer sbData = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			sbData.append(line).append("\n");
		}
		br.close();
		is.close();
		Barcode barcode;
		BufferedImage image;
		try {
			barcode = BarcodeFactory.createCode128(invoiceNo);
			barcode.setDrawingText(false);
			barcode.setBorder(BorderFactory.createEmptyBorder());
			image = BarcodeImageHandler.getImage(barcode);

			String base64 = ImageUtil.imgToBase64String(image, "png");
			String barCodeTag = "<img id=\"barCode\" style=\"width:215px;height:65px;\">";
			int barcodeStartIndex = sbData.indexOf(barCodeTag);
			int barcodeEndIndex = barcodeStartIndex + barCodeTag.length();
			if (barcodeStartIndex != -1 && barcodeEndIndex != -1) {
				sbData.replace(barcodeStartIndex, barcodeEndIndex,
						"<img id=\"barCode\" style=\"width:215px;height:65px;\" src=\"data:image/png;base64," + base64
								+ "\">");
			}
			PdfUtil.createPDF(sbData.toString(), DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + invoiceNo + ".pdf",
					rootURI, PageSize.A4, PageSize.A4.getWidth());
		} catch (BarcodeException e) {
			e.printStackTrace();
		} catch (OutputException e) {
			e.printStackTrace();
		}

	}
	
	public String getsupliercode(String plant,String username) {
		TblControlDAO _TblControlDAO = new TblControlDAO();
		String minseq = "";
		String sBatchSeq = "";
		String sCustCode = "";
		boolean insertFlag = false;
		String sZero = "";
		//TblControlDAO _TblControlDAO = new TblControlDAO();
		//_TblControlDAO.setmLogger(mLogger);
		Hashtable ht = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "SUPPLIER");
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
		} catch (Exception e) {
			//mLogger.exception(true,"ERROR IN JSP PAGE - vender_view.jsp ", e);
		}
		
		return sCustCode;
	
	}
	 
	public String createsupplier(String plant,String username,String sname,String peppolid,String comprgno,String add1,String add3,String state,String zip,String countryname,String email) throws Exception {
		String sCustCode = "";
		try {
		DateUtils dateutils = new DateUtils();
		CustUtil custUtil = new CustUtil();
		MovHisDAO mdao = new MovHisDAO(plant);
		TblControlDAO _TblControlDAO = new TblControlDAO();
		Hashtable ht = new Hashtable();
		ht.put(IConstants.PLANT, plant);
		sCustCode = getsupliercode(plant, username);
		ht.put(IConstants.VENDOR_CODE, sCustCode);
		ht.put(IConstants.VENDOR_NAME, sname);
		ht.put("COMMENT1", "0");
		ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
		ht.put(IConstants.CREATED_BY, username);
		ht.put(IConstants.ISACTIVE, "Y");
		ht.put("ISPEPPOL", "1");
		ht.put("PEPPOL_ID", peppolid);
		ht.put("ADDR1", add1);
		ht.put("ADDR3", add3);
		ht.put("STATE", state);
		ht.put("ZIP", zip);
		ht.put("RCBNO", comprgno);
		ht.put("COMPANYREGNUMBER", comprgno);
		ht.put("COUNTRY", countryname);
		ht.put("EMAIL", email);
		String sysTime = DateUtils.Time();
		String sysDate = DateUtils.getDate();
		sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

		//mdao.setmLogger(mLogger);
		Hashtable htm = new Hashtable();
		htm.put("PLANT", plant);
		htm.put("DIRTYPE", TransactionConstants.ADD_SUP);
		htm.put("RECID", "");
		htm.put("ITEM",sCustCode);
		htm.put(IDBConstants.REMARKS, "");
		htm.put(IDBConstants.CREATED_BY, sname);
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
		}catch (Exception e) {
			 sCustCode = "";
		}
		
		return sCustCode;
	}
	 
	public void createuom(String plant,String username,String uom) {
		DateUtils dateutils = new DateUtils();
		UomUtil uomUtil = new UomUtil();
		boolean result = false;
		try {
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.UOMCODE, uom);
		String sItemDesc =new UomDAO().getUomByUomcodeInPeppoluomMaster(uom);
		ht.put(IDBConstants.UOMDESC, sItemDesc);
		ht.put(IConstants.ISACTIVE, "Y");
		ht.put(IDBConstants.CREATED_AT, new DateUtils()
				.getDateTime());
		ht.put(IDBConstants.LOGIN_USER, username);

		MovHisDAO mdao = new MovHisDAO(plant);

		Hashtable htm = new Hashtable();
		htm.put(IDBConstants.PLANT, plant);
		htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_UOM);
		htm.put("RECID", "");
		htm.put("ITEM",uom);
		htm.put(IDBConstants.UPBY, username);
		htm.put(IDBConstants.REMARKS, sItemDesc);
		htm.put(IDBConstants.CREATED_BY, username);
		htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
		htm.put(IDBConstants.TRAN_DATE, dateutils
				.getDateinyyyy_mm_dd(dateutils.getDate()));	
		ht.put("Display",uom);
		/*if(sQtyPerUom == "")
		{
			sQtyPerUom = "0";
		} */
		ht.put("QPUOM","1");
		ht.put("ISCONVERTIBLE", "0");
		ht.put("CUOM", uom);


		boolean itemInserted = uomUtil.insertUom(ht);
		boolean inserted = mdao.insertIntoMovHis(htm);
		result = true;
		}catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	public void createproduct(String plant,String username,String item,String uom,String cost) {
		try {
		DateUtils dateutils = new DateUtils();
		ItemUtil itemUtil = new ItemUtil();
		Hashtable ht = new Hashtable();
        ht.put(IConstants.PLANT,plant);
        ht.put(IConstants.ITEM,item);
        ht.put(IConstants.ITEM_DESC,item);
        ht.put("STKUOM",uom);
        ht.put(IConstants.PRDCLSID,"NOCLASSIFICATION");
        ht.put(IConstants.ISACTIVE,"Y");
        ht.put(IConstants.COST,cost);
        ht.put("CRBY",username);
        ht.put("CRAT",dateutils.getDateTime());
        ht.put("PURCHASEUOM",uom);
	    ht.put("SALESUOM",uom);
	    ht.put("RENTALUOM",uom);
	    ht.put("SERVICEUOM",uom);
	    ht.put("INVENTORYUOM",uom);
        ht.put("USERFLD1", "N");
        ht.put(IDBConstants.STKQTY, "0");//stkqty
        ht.put(IDBConstants.MAXSTKQTY, "0");
        MovHisDAO mdao = new MovHisDAO(plant);
        Hashtable htm = new Hashtable();
        htm.put("PLANT",plant);
        htm.put("DIRTYPE",TransactionConstants.ADD_ITEM);
        htm.put("RECID","");
        htm.put("ITEM",item);
        htm.put("LOC","");
        htm.put(IDBConstants.REMARKS,"");  
        htm.put("CRBY",username);
        htm.put("CRAT",dateutils.getDateTime());
        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
        boolean  inserted=false; 
        boolean  insertAlternateItem = false; 
        boolean itemInserted = itemUtil.insertItem(ht);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void savedocument(String plant,String b64, String name,String vendno,String billNo,String dtype,String username,int billHdrId) {
		String sfileLocation = "C:/ATTACHMENTS/Bill" + "/"+ vendno + "/"+ billNo;
		File fileLocation = new File("C:/ATTACHMENTS/Bill" + "/"+ vendno + "/"+ billNo);
		if (!fileLocation.exists()) {
			fileLocation.mkdirs();
		}
		
		File uploadedFile = new File(sfileLocation + "/" +name);
		if (uploadedFile.exists()) {
			uploadedFile.delete();
		}
		
		File file = new File(sfileLocation + "/" +name);
	    try ( FileOutputStream fos = new FileOutputStream(file); ) {
	      // To be short I use a corrupted PDF string, so make sure to use a valid one if you want to preview the PDF file
	      byte[] decoder = Base64.decode(b64);
	      fos.write(decoder);
	      
	      File lfile = new File(sfileLocation + "/" +name);
	      long bytes = 0;
	      if (lfile.exists()) {
	          bytes = lfile.length();
	      }

	      Hashtable<String,String> billAttachment = new Hashtable<String, String>();
			billAttachment.put("PLANT", plant);
			billAttachment.put("FILETYPE", dtype);
			billAttachment.put("FILENAME", name);
			billAttachment.put("FILESIZE", String.valueOf(bytes));
			billAttachment.put("FILEPATH", sfileLocation);
			billAttachment.put("CRAT",DateUtils.getDateTime());
			billAttachment.put("CRBY",username);
			billAttachment.put("UPAT",DateUtils.getDateTime());
			billAttachment.put("BILLHDRID", String.valueOf(billHdrId));
			List<Hashtable<String,String>> billAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
			billAttachmentInfoList.add(billAttachment);
			new BillDAO().addBillAttachments(billAttachmentInfoList, plant);
	      
	      System.out.println("File Saved");
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	
	
	 

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {

	}
}