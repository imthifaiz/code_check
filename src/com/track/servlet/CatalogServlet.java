package com.track.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.json.JsonObject;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CatalogDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PrdClassDAO;
import com.track.db.util.CatalogUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.EmployeeUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.tables.CATALOGMST;
import com.track.tranaction.SendEmail;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Servlet implementation class CatalogServlet
 */
public class CatalogServlet extends HttpServlet implements IMLogger,
		Serializable {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.PurchaseOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PurchaseOrderServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private CatalogUtil _catalogUtil;
	private ItemUtil _itemutil;
	private StrUtils _StrUtils = null;
	private DateUtils dateutils = null;
	private CustMstDAO _custmst = null;
	private DOUtil _doutil = null;
	private DOTransferUtil _DOTransferUtil = null;
	private DoHdrDAO _dohdrdao = null;
	boolean flag = false, prdflag = false;
	String description1 = "", description2 = "", qty = "", description3 = "",
			productid = "", price = "", strpath = "", fileLocation = "",
			filetempLocation = "", catlogpath = "";
	String result = "";
	int noofrecords = 0;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CatalogServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		_StrUtils = new StrUtils();
		dateutils = new DateUtils();
		_catalogUtil = new CatalogUtil();
		_itemutil = new ItemUtil();
		_custmst = new CustMstDAO();
		_doutil = new DOUtil();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		UserTransaction ut = null;

		PrintWriter out = response.getWriter();
		JSONObject objectResult = new JSONObject();
		try {
			String action = StrUtils.fString(request.getParameter("Submit"));
			

			if (action.equalsIgnoreCase("CREATE_CATALOG")) {

				process_createCatalog(request, response);

			} else if (action.equalsIgnoreCase("PREVIEW_IMAGE")) {

				process_previewImage(request, response);

			} else if (action.equalsIgnoreCase("EDIT_CATALOG")) {
				prcoess_editCatalog(request, response);
			} else if (action.equalsIgnoreCase("DEL_CATALOG")) {
				process_delCatalog(request, response);
			} else if (action.equalsIgnoreCase("ADDTOCART")) {

				process_addtocart(request, response);
			} else if (action.equalsIgnoreCase("CHECKOUT")) {

				process_checkout(request, response);
			} else if (action.equalsIgnoreCase("REMOVE")) {

				objectResult = process_remove(request, response);
			}
			else if (action.equalsIgnoreCase("VIEW_CATALOG_DETAILS")) {

				objectResult = process_viewCatalogDetails(request, response);
			}
			else if (action.equalsIgnoreCase("UPLOAD_LOGO")) {

				 UploadLogo(request, response);
			} 
			else if (action.equalsIgnoreCase("itm_img_edit")) {

				String ITEMS = (request.getParameter("ITEMM"));
				objectResult = itemImageUpdate(request, response,ITEMS);
			}
			else if (action.equalsIgnoreCase("itm_img_delete")) {

				String ITEMS = (request.getParameter("ITEMM"));
				objectResult = itemImageDelete(request, response,ITEMS);
			}
			else if (action.equalsIgnoreCase("catry_img_edit")) {

				objectResult = catryImageUpdate(request, response);
			}
			else if (action.equalsIgnoreCase("catry_img_delete")) {

				objectResult = catryImageDelete(request, response);
			}
			else if (action.equalsIgnoreCase("logo_img_edit")) {

				objectResult = logoImageUpdate(request, response);
			}
			else if (action.equalsIgnoreCase("app_img_edit")) {

				objectResult = appImageUpdate(request, response);
			}
			else if (action.equalsIgnoreCase("app_img_delete")) {
				
				objectResult = appImageDelete(request, response);
			}
			else if (action.equalsIgnoreCase("seal_img_edit")) {

				objectResult = sealImageUpdate(request, response);
			}
			else if (action.equalsIgnoreCase("sign_img_edit")) {

				objectResult = signImageUpdate(request, response);
			}
			else if (action.equalsIgnoreCase("logo_img_delete")) {

				objectResult = logoImageDelete(request, response);
			}
			else if (action.equalsIgnoreCase("seal_img_delete")) {

				objectResult = sealImageDelete(request, response);
			}
			else if (action.equalsIgnoreCase("sign_img_delete")) {

				objectResult = signImageDelete(request, response);
			}
			else if (action.equalsIgnoreCase("emp_img_edit")) {

				objectResult = empImageUpdate(request, response);
			}
			else if (action.equalsIgnoreCase("emp_img_delete")) {

				objectResult = empImageDelete(request, response);
			}
			else if (action.equalsIgnoreCase("UPLOAD_USER_IMAGE")) {

				objectResult = UploadUserImage(request, response);
			}
			else if (action.equalsIgnoreCase("DELETE_USER_IMAGE")) {

				objectResult = DeleteUserImage(request, response);
			}
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(objectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception ex) {
			// TODO: handle exception
			// DbBean.RollbackTran(ut);
			ex.printStackTrace();
			this.mLogger.exception(this.printLog, "", ex);
			result = "<font class = " + IConstants.FAILED_COLOR + ">Error : "
					+ ex.getMessage() + "</font>";
			response.sendRedirect("jsp/createCatalog.jsp?result=" + result);
		}

	}

	private JSONObject process_viewCatalogDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String sQuery="",  description1="",  description2="",  description3="",isActive="";
		JSONObject resultJSON = new JSONObject();
		String productid = StrUtils.fString(request.getParameter("PRODUCTID"));
		String plant = StrUtils.fString(request.getParameter("PLANT"));
		try{
		Hashtable htsel = new Hashtable();
		htsel.put(IConstants.PRODUCTID,productid);
		htsel.put(IConstants.PLANT,plant);
		sQuery ="ISNULL(DETAIL1,'') DETAIL1,ISNULL(DETAIL2,'') DETAIL2,ISNULL(DETAIL3,'') DETAIL3,ISNULL(DETAIL4,'') DETAIL4,";
		sQuery = sQuery +"ISNULL(DETAIL5,'') DETAIL5,ISNULL(DETAIL6,'') DETAIL6,ISNULL(DETAIL7,'') DETAIL7,CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISACTIVE";
	         List m = _catalogUtil.listCatalogDet(sQuery,htsel,"");	
		
	         for(int i=0;i<m.size();i++)
	         {
	        	Map insmp= (Map)m.get(i);
//	         detail1.append(insmp.get("DETAIL1"));
//	         detail2.append(insmp.get("DETAIL2"));
//	         detail3.append(insmp.get("DETAIL3"));
//	         detail4.append(insmp.get("DETAIL4"));
//	         detail5.append(insmp.get("DETAIL5"));
//	         detail6.append(insmp.get("DETAIL6"));
//	         detail7.append(insmp.get("DETAIL7"));
	         description1 = (String)insmp.get("DESCRIPTION1");
	         description2 = (String)insmp.get("DESCRIPTION2");
	         description3 = (String)insmp.get("DESCRIPTION3");
             description1.trim();
             description2.trim();
             description3.trim();
	         price = StrUtils.currencyWtoutCommSymbol((String)insmp.get(IDBConstants.CATLOGPRICE));
	         isActive= (String)insmp.get(IDBConstants.ISACTIVE);
	         
	         JSONObject resultObjectJson = new JSONObject();
	         
	 		resultObjectJson.put("productid", productid);
	 		resultObjectJson.put("description1", description1);
	 		resultObjectJson.put("description2", description2);
	 		resultObjectJson.put("description3", description3);
	 		
	 		resultJSON.put("result", resultObjectJson);
	 		resultJSON.put("status", "100");
	         
	         }
		}
		catch(Exception e){
			e.printStackTrace();
			resultJSON.put("status", "99");
		}		
		
		return resultJSON;
	}

	private JSONObject process_remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		JSONObject resultJSON = new JSONObject();
		try {
			Vector<CATALOGMST> scanlist = new Vector<CATALOGMST>();
			// System.out.println("remove condtn");
			HttpSession session = request.getSession();
			scanlist = (Vector<CATALOGMST>) session.getAttribute("catloglst");
			String index = request.getParameter("INDEX");
			// System.out.println("Index"+index);
			scanlist.remove(Integer.parseInt(index));
			session.setAttribute("catloglst", scanlist);
			JSONObject resultObjectJson = new JSONObject();
			resultJSON.put("result", resultObjectJson);
			resultObjectJson.put("status", "100");
			resultJSON.put("status", "100");
			resultJSON.put("shoppingbag", scanlist.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJSON;
	}

	private void process_checkout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		try {
			HttpSession session = request.getSession();
			String plant = "", dono = "", loginuser = "";
			String DELIVERYDATE="",TIMESLOTS="";

			Map _mobileshopping = null;
			
			SendEmail sendmail = new SendEmail();
			//String from = "email_notification@track.com.sg"; 
			 String from = ""; 
			Vector<CATALOGMST> catloglist = new Vector<CATALOGMST>();
			_DOTransferUtil = new DOTransferUtil();
			_dohdrdao = new DoHdrDAO();
			catloglist = (Vector) session.getAttribute("catloglst");
			DELIVERYDATE= (String) session.getAttribute("DELIVERYDATE");
		       
			TIMESLOTS= (String) session.getAttribute("TIMESLOTS");
			if (catloglist.size() == 0) {
				throw new Exception("Cart details are already processed ...");
			}

			loginuser = (String) session.getAttribute("LOGIN_USER");

			plant = (String) session.getAttribute(IDBConstants.PLANT);
			String remarks = (String) session.getAttribute("REMARKS");
			String toemail= _custmst.getCustByRow(plant, loginuser,"HPNO", "EMAIL");
			_mobileshopping = new HashMap();
			_mobileshopping.put(IDBConstants.PLANT, plant);
			_mobileshopping.put("shoppingbag", catloglist);
			_mobileshopping.put(IDBConstants.USER_ID, loginuser);
			_mobileshopping.put(IDBConstants.REMARKS, remarks);
			_mobileshopping.put(IDBConstants.DELIVERYDATE, DELIVERYDATE);
			_mobileshopping.put(IDBConstants.TIMESLOTS,TIMESLOTS);

			_dohdrdao.setmLogger(mLogger);
			
			dono = _dohdrdao.getNextOrder(plant);
			_mobileshopping.put(IDBConstants.DODET_DONUM, dono);
			String msg = " Your Confirmation Number is   <b>" + dono + "</b>";
			String subject = "Mobile Order Confirmation";
			String bodyContent="Mobile Order Created Successfully."+" Your order number is "+ dono;
				
			flag = _catalogUtil.process_mobileshopping(_mobileshopping);
			if (flag) {
				try{
			    Map m=new EmailMsgUtil().getEmailMsgDetails(plant,"MOBILE ORDER");
			         
			         if(!m.isEmpty()){
			              from = (String) m.get("EMAIL_FROM");
			              subject = (String) m.get("SUBJECT");
			              String body1=(String)m.get("BODY1");
			              String body2=(String)m.get("BODY2");
			              String webLink=(String) m.get("WEB_LINK");
			              
                                      
                                      String servername=request.getServerName();
			               int port  = request.getServerPort();
			          // servername="192.168.10.108";
			              String qrPrdScanned= (String) session.getAttribute("PRODUCT_QR_SCANNED");
                                      String url="http://"+servername+":"+port+"/track/jsp/mobileShopping.jsp?ID="+plant+"AAABBACCB"+qrPrdScanned+"AAABBACCB"+"Y";
                                      QRServlet qrserv= new QRServlet();
			              String imagePath= qrserv.getQRImagePath(plant,qrPrdScanned,url);
                                      String toreOrderMsg ="To Reorder, Scan the QRCode below :";
			              String htmlmsg = "<html><head><title>" +
			                                "</title></head><body><BR><p>" +
			                                body1 + "<b>"+dono+"</b> <br>"+
			                                "<p>"+ body2 + "<br><p>"+toreOrderMsg+"<br>"+"<a href=\"http://"+webLink+"/\">"+webLink+"</a>"+
			                                " </body></html>";
			              
			             sendmail.sendTOMail(from,   toemail,"","", subject, htmlmsg,imagePath);
			             File tempPath = new File(imagePath);
			             if (tempPath.exists()) {
			               tempPath.delete();
			             }
			              
			              String isccChecked=(String) m.get("IS_CC_CHECKED");
			              if(isccChecked.equalsIgnoreCase("Y")){
			                  //get name,cc eamil address
			                 // String mailtoCopy=new PlantMstUtil().getEmailAddresstoCopyTo(plant);
			                  subject =(String) m.get("CC_SUBJECT");
			                  String ccbody1=(String)m.get("CC_BODY1");
			                 
			                  String ccbody2=(String)m.get("CC_BODY2");
			                  String ccwebLink=(String) m.get("CC_WEB_LINK");
			                  String mailtoCopy=(String) m.get("CC_EMAILTO");
			                  htmlmsg = "<html><head><title>" +
			                                    "</title></head><body><BR><p>" +
			                                    ccbody1 + "<b>"+dono+"</b> <br>"+
			                                    "<p>"+ ccbody2 + "<br><br>"+"<a href="+ccwebLink+">"+ccwebLink+"</a>"+
			                                    " </body></html>";
			                
			                  sendmail.sendTOMail(from,   mailtoCopy,"","", subject, htmlmsg,"");
			              }
			         }
				catloglist.clear();
				session.setAttribute("catloglst", catloglist);
				session.setAttribute("LOGIN_USER", "");
				response.sendRedirect("jsp/cartSuccessmsg.jsp?MSG=" + msg);
				}
				catch(AddressException ex)
				{
					System.out.println("Error Message"+ ex.getMessage());
					catloglist.clear();
					session.setAttribute("catloglst", catloglist);
					session.setAttribute("LOGIN_USER", "");
					response.sendRedirect("jsp/cartSuccessmsg.jsp?MSG=" + msg);
				}
			}
			//else {
//				response.sendRedirect("jsp/cartErrorMsg.jsp?MSG=");
//			}
		} catch (Exception e) {
			response.sendRedirect("jsp/cartErrorMsg.jsp?MSG=" + e.getMessage());
		}

	}

	private void process_addtocart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		JSONObject resultJSON = new JSONObject();
		HttpSession session = request.getSession();
		boolean catlogFound = true, match = false;
		String plant = "", Quantity = "", loginuser = "", productID = "", index = "", scanid = "";

		if (productID == null || productID == "") {
			productID = request.getParameter(IDBConstants.PRODUCTID);
			Quantity = request.getParameter("QTY");
			index = request.getParameter("INDEX");
			scanid = request.getParameter("SCANID");
		} else {
			productID = (String) session.getAttribute(IDBConstants.PRODUCTID);
		}

		if (Quantity == null || Quantity == "")
			Quantity = request.getParameter("QTY");
	
		if (((String) session.getAttribute("PLANT")) != null)
			plant = (String) session.getAttribute("PLANT");
		if (plant == null || plant == "")
			plant = request.getParameter(IDBConstants.PLANT);

		Vector catloglist = (Vector<CATALOGMST>) session
				.getAttribute("catloglst");
		CATALOGMST catalogs = getCatalogDetails(request, productID, Quantity,
				plant);
		if (catloglist == null) {
			match = false;
			catloglist = new Vector<CATALOGMST>();
			catloglist.addElement(catalogs);

		} else {
			for (int i = 0; i < catloglist.size(); i++) {

				CATALOGMST catlogbean = (CATALOGMST) catloglist.elementAt(i);

				if (catlogbean.getProductID().equalsIgnoreCase(
						catalogs.getProductID())) {

					catlogbean.setQuantity((catlogbean.getQuantity() + Integer
							.parseInt(Quantity)));
					catloglist.setElementAt(catlogbean, i);
					match = true;

				}
				// catloglist.remove(i);

			}
			if (!match) {
				// System.out.print("add second elee"+match);
				catloglist.addElement(catalogs);
			}

		}
		System.out.println("list size" + catloglist.size());
		session.setAttribute("catloglst", catloglist);
		// session.setAttribute("LOGIN_USER", loginuser);
		session.setAttribute(IDBConstants.PLANT, plant);
	
		response.sendRedirect("jsp/cartSummary.jsp?INDEX=" + index + "&SCANID="
				+ scanid + "&PLANT=" + plant);
		
	}

	private CATALOGMST getCatalogDetails(HttpServletRequest request,
			String productid2, String qty2, String plant) throws Exception {
		// TODO Auto-generated method stub
		CATALOGMST catlogs = new CATALOGMST();
		HttpSession session1 = request.getSession();
		CatalogUtil _catlogutil = new CatalogUtil();
		String description1 = "", description2 = "", description3 = "";

		java.util.Hashtable ht = new java.util.Hashtable();

		ht.put(IDBConstants.PLANT, plant);

		ht.put(IDBConstants.PRODUCTID, productid2);

		List logslist = _catalogUtil
				.listCatalogs(
						"distinct CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISACTIVE ",
						ht, "");
		for (int i = 0; i < 1; i++) {
			Map logmap = (Map) logslist.get(0);
			catlogs.setProductID((String) logmap.get(IDBConstants.PRODUCTID));
			catlogs.setCatlogPath((String) logmap.get(IDBConstants.CATLOGPATH));
			catlogs.setDescription1((String) logmap
					.get(IDBConstants.DESCRIPTION1));
			catlogs.setDescription2((String) logmap
					.get(IDBConstants.DESCRIPTION2));
			catlogs.setDescription3((String) logmap
					.get(IDBConstants.DESCRIPTION3));
			float pricef = Float.parseFloat((String) logmap
					.get(IDBConstants.CATLOGPRICE));
			catlogs.setPrice(pricef);
			int qty = Integer.parseInt(qty2);
			catlogs.setQuantity(qty);
		}

		return catlogs;
	}

	private void process_createCatalog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		

		StringBuffer detail1, detail2, detail3, detail4, detail5, detail6, detail7;
		String catalogspercompany = "",detailHeading="";
		String action = StrUtils.fString(request.getParameter("Submit"));
		HttpSession session = request.getSession();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		UserTransaction ut = null;
		selectBean sb = new selectBean();
		int iIndex = 1;
		detail1 = new StringBuffer();
		detail2 = new StringBuffer();
		detail3 = new StringBuffer();
		detail4 = new StringBuffer();
		detail5 = new StringBuffer();
		detail6 = new StringBuffer();
		detail7 = new StringBuffer();
		// String fileloc = this.saveImageFile(request, response, plant);
		// System.out.println("FileCatlogpath"+fileloc);

		Hashtable ht = new Hashtable();
		ht.put(IConstants.PLANT, plant);
		noofrecords = _catalogUtil.NoofRecords(ht, "");
		catalogspercompany = sb.getNOfCatalogs(plant);
		// System.out.println("catalogsize"+catalogspercompany);
		int noofcatlogs = Integer.parseInt(catalogspercompany);
		// Check not to exceed 50 records
		if (noofrecords > noofcatlogs) {
			result = "<font color=\"red\"> Catalogs exceeded more than "
					+ noofcatlogs + " </font>";
			response.sendRedirect("jsp/createCatalog.jsp?result=" + result);
		} else {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ plant;
			filetempLocation = DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
			boolean imageSizeflg = false;
			if (!isMultipart) {
				System.out.println("File Not Uploaded");
			} else {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				// upload.setFileSizeMax(1000000);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName()
								.equalsIgnoreCase("DESCRIPTION1")) {
							description1 = item.getString();
						}
						if (item.getFieldName()
								.equalsIgnoreCase("DESCRIPTION2")) {
							description2 = item.getString();
						}
						if (item.getFieldName()
								.equalsIgnoreCase("DESCRIPTION3")) {
							description3 = item.getString();
						}
						if (item.getFieldName().equalsIgnoreCase("PRODUCTID")) {
							productid = item.getString();
							
						}
						if (item.getFieldName().equalsIgnoreCase("PRICE")) {
							price = item.getString();
						}
						if (item.getFieldName().equalsIgnoreCase("DETAIL1")) {
							detail1.append(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DETAIL2")) {
							detail2.append(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DETAIL3")) {
							detail3.append(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DETAIL4")) {
							detail4.append(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DETAIL5")) {
							detail5.append(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DETAIL6")) {
							detail6.append(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DETAIL7")) {
							detail7.append(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("DETAILHEADING")) {
							detailHeading=item.getString().trim();
						}

					} else if (!item.isFormField()
							&& (item.getName().length() > 0)) {
						String fileName = item.getName();
						long size = item.getSize();

						size = size / 1024;
						// size = size / 1000;
						System.out.println("size of the Image imported :::"
								+ size);
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";

							imageSizeflg = true;

						}
						File path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName
								.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" + _StrUtils.RemoveSlash(productid)
								+ ".JPEG");

						// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
						// if (uploadedFile.exists()) {
						// uploadedFile.delete();
						// }
						strpath = path + "/" + fileName;
						catlogpath = uploadedFile.getAbsolutePath();
						if (!imageSizeflg && !uploadedFile.exists())
							item.write(uploadedFile);

						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "/"
									+ _StrUtils.RemoveSlash(productid) + ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}

					}

				}
			}
			Hashtable htCatalog = new Hashtable();
			htCatalog.put(IDBConstants.PRODUCTID, productid);
			htCatalog.put(IConstants.PLANT, plant);
			htCatalog.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htCatalog.put(IDBConstants.DESCRIPTION1, description1);
			htCatalog.put(IDBConstants.DESCRIPTION2, description2);
			htCatalog.put(IDBConstants.DESCRIPTION3, description3);
			htCatalog.put(IDBConstants.DETAILLABEL, detailHeading);//Adding label
			htCatalog.put(IDBConstants.DETAIL1, detail1.toString());
			htCatalog.put(IDBConstants.DETAIL2, detail2.toString());
			htCatalog.put(IDBConstants.DETAIL3, detail3.toString());
			htCatalog.put(IDBConstants.DETAIL4, detail4.toString());
			htCatalog.put(IDBConstants.DETAIL5, detail5.toString());
			htCatalog.put(IDBConstants.DETAIL6, detail6.toString());
			htCatalog.put(IDBConstants.DETAIL7, detail7.toString());
			double dprice = Double.parseDouble(price);
			//dprice = StrUtils.RoundDB(dprice, 2);
			String sprice = String.valueOf(dprice);
			htCatalog.put(IDBConstants.CATLOGPRICE, sprice);
			session.setAttribute("HTCRATLOG", htCatalog);
			if (imageSizeflg) {
				response.sendRedirect("jsp/createCatalog.jsp?result=" + result
						+ "&PRODUCTID=" + productid + "&action=view");
			} else {

				if (_itemutil.isExistsItemMst(productid, plant)) {
					Hashtable htlog = new Hashtable();
					htlog.put(IDBConstants.PRODUCTID, productid);
					htlog.put(IConstants.PLANT, plant);
					// Check ProductId exist in Catlogmst
					prdflag = _catalogUtil.isExistCatalog(htlog, "");

					// catlogpath = catlogpath.replace('\','/');

					if (!prdflag) {
						htCatalog.put(IConstants.CREATED_BY, userName);
						catlogpath = catlogpath.replace('\\', '/');
						htCatalog.put(IDBConstants.CATLOGPATH, catlogpath);
						htCatalog.put(IConstants.ISACTIVE, "Y");
						ut = com.track.gates.DbBean.getUserTranaction();
						ut.begin();
						flag = _catalogUtil.insertMst(htCatalog);
						if (flag) {
							flag = insertMovhis(htCatalog,
									TransactionConstants.CREATE_CATALOG);
						}
						if (flag) {
							DbBean.CommitTran(ut);

							result = "<font color=\"green\"> Catalog created successfully</font>";

							response
									.sendRedirect("jsp/createCatalog.jsp?result="
											+ result + "&action=view");

						} else {
							DbBean.RollbackTran(ut);
							result = "<font color=\"red\"> Failed to create Catalog </font>";
							response
									.sendRedirect("jsp/createCatalog.jsp?result="
											+ result + "&action=view");
						}
					}
					// catlogid exist
					else {

						result = "<font color=\"red\"> Catalog exists for productID </font>";
						response.sendRedirect("jsp/createCatalog.jsp?result="
								+ result + "&action=view");
					}
				} else {
					result = "<font color=\"red\"> Product ID does not exist </font>";
					response.sendRedirect("jsp/createCatalog.jsp?result="
							+ result + "&action=view");
				}

			}
		}
	}

	private boolean insertMovhis(Hashtable htmov, String trantype)
			throws Exception {
		boolean result = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htmovhis = new Hashtable();
			htmovhis.clear();
			htmovhis.put(IDBConstants.PLANT, htmov.get(IConstants.PLANT));
			htmovhis.put("DIRTYPE", trantype);
			htmovhis.put(IDBConstants.ITEM, htmov.get(IDBConstants.PRODUCTID));
			htmovhis.put("MOVTID", "");
			htmovhis.put("RECID", "");
			htmovhis.put(IDBConstants.CREATED_BY, htmov
					.get(IConstants.CREATED_BY));
			htmovhis.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			htmovhis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());

			result = movHisDao.insertIntoMovHis(htmovhis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return result;
	}

	private void process_previewImage(HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
		StringBuffer detail1, detail2, detail3, detail4, detail5, detail6, detail7;

		detail1 = new StringBuffer();
		detail2 = new StringBuffer();
		detail3 = new StringBuffer();
		detail4 = new StringBuffer();
		detail5 = new StringBuffer();
		detail6 = new StringBuffer();
		detail7 = new StringBuffer();
		HttpSession session = request.getSession();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		productid = request.getParameter("PRODUCTID");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		fileLocation =  DbBean.COMPANY_CATALOG_PATH +"/temp"+ "/"+ plant;
		
		if (!isMultipart) {
			System.out.println("File Not Uploaded");
		} else {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {

					if (item.getFieldName().equalsIgnoreCase("DESCRIPTION1")) {
						description1 = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("DESCRIPTION2")) {
						description2 = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("DESCRIPTION3")) {
						description3 = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("PRODUCTID")) {
						productid = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("PRICE")) {
						price = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL1")) {
						detail1.append(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL2")) {
						detail2.append(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL3")) {
						detail3.append(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL4")) {
						detail4.append(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL5")) {
						detail5.append(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL6")) {
						detail6.append(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL7")) {
						detail7.append(item.getString());
					}

				}
				
				if (!item.isFormField() && (item.getName().length() > 0)) {
					String fileName = item.getName();
					
					

					File path = new File(fileLocation);
					
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					fileName = fileName
							.substring(fileName.lastIndexOf("\\") + 1);
					File uploadedFile = new File(path + "/" + _StrUtils.RemoveSlash(productid)
							+ ".JPEG");
					// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
					if (uploadedFile.exists()) {
						uploadedFile.delete();
					}
					strpath = path + "/" + fileName;
					catlogpath = uploadedFile.getAbsolutePath();

					item.write(uploadedFile);

				}
				
			}
		}
		Hashtable htCatalog = new Hashtable();
		htCatalog.put(IDBConstants.DESCRIPTION1, description1);
		htCatalog.put(IDBConstants.DESCRIPTION2, description2);
		htCatalog.put(IDBConstants.DESCRIPTION3, description3);
		htCatalog.put(IDBConstants.PRODUCTID, productid);
		htCatalog.put(IDBConstants.DETAIL1, detail1.toString());
		htCatalog.put(IDBConstants.DETAIL2, detail2.toString());
		htCatalog.put(IDBConstants.DETAIL3, detail3.toString());
		htCatalog.put(IDBConstants.DETAIL4, detail4.toString());
		htCatalog.put(IDBConstants.DETAIL5, detail5.toString());
		htCatalog.put(IDBConstants.DETAIL6, detail6.toString());
		htCatalog.put(IDBConstants.DETAIL7, detail7.toString());
		double dprice = Double.parseDouble(price);
		dprice = StrUtils.RoundDB(dprice, 2);
		String sprice = String.valueOf(dprice);
		htCatalog.put(IDBConstants.CATLOGPRICE, sprice);
		catlogpath = catlogpath.replace('\\', '/');
		session.setAttribute("IMAGEPATH", catlogpath);
		session.setAttribute("HTCRATLOG", htCatalog);
		response.sendRedirect("jsp/previewImage.jsp");

	}

	private void prcoess_editCatalog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String isActive="",detailHeading="",strCatlogpath="";
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		StringBuffer detail1, detail2, detail3, detail4, detail5, detail6, detail7;
		String sprice = "";
		detail1 = new StringBuffer();
		detail2 = new StringBuffer();
		detail3 = new StringBuffer();
		detail4 = new StringBuffer();
		detail5 = new StringBuffer();
		detail6 = new StringBuffer();
		detail7 = new StringBuffer();

		UserTransaction ut = null;
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		boolean imageSizeflg = false;
		fileLocation =  DbBean.COMPANY_CATALOG_PATH + "/" + plant;
		if (!isMultipart) {
		
		} else {
			
			FileItemFactory factory = new DiskFileItemFactory();
			
			ServletFileUpload upload = new ServletFileUpload(factory);

			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				
				if (!item.isFormField() && (item.getName().length() > 0)) {
					String fileName = item.getName();
					long size = item.getSize();
					size = size / 1020;
					// size = size / 1000;
					//checking image size for 2MB
					if (size > 2040) // condtn checking Image size
					{
						result = "<font color=\"red\">  Catalog Image size greater than 2 MB </font>";

						imageSizeflg = true;
						// rd.forward(request, response);
					}
					File path = new File(fileLocation);
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					fileName = fileName
							.substring(fileName.lastIndexOf("\\") + 1);
					File uploadedFile = new File(path + "/" + _StrUtils.RemoveSlash(productid)
							+ ".JPEG");
					// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
					if (uploadedFile.exists()) {
						uploadedFile.delete();
					}
					
					strpath = path + "/" + fileName;
					
					strCatlogpath = uploadedFile.getAbsolutePath();
					
					
					
					
					if (!imageSizeflg)
						item.write(uploadedFile);

				} else if (item.isFormField()) {

					if (item.getFieldName().equalsIgnoreCase("DESCRIPTION1")) {
						description1 = item.getString().trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DESCRIPTION2")) {
						description2 = item.getString().trim();
					}
					if (item.getFieldName().equalsIgnoreCase("DESCRIPTION3")) {
						description3 = item.getString().trim();
					}
					if (item.getFieldName().equalsIgnoreCase("PRODUCTID")) {
						productid = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("PRICE")) {
						price = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL1")) {
						detail1.append(item.getString().trim());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL2")) {
						detail2.append(item.getString().trim());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL3")) {
						detail3.append(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL4")) {
						detail4.append(item.getString().trim());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL5")) {
						detail5.append(item.getString().trim());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL6")) {
						detail6.append(item.getString().trim());
					}
					if (item.getFieldName().equalsIgnoreCase("DETAIL7")) {
						detail7.append(item.getString().trim());
					}
					if (item.getFieldName().equalsIgnoreCase("ISACTIVE")) {
						isActive = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("DETAILHEADING")) {
						detailHeading=item.getString().trim();
					}
				}
			}
		}

		Hashtable htreadval = new Hashtable();
		if (price.length() == 0)
			price = "0";
		double dprice = Double.parseDouble(price);
		dprice = StrUtils.RoundDB(dprice, 2);
		sprice = String.valueOf(dprice);

		htreadval.put(IDBConstants.PRODUCTID, productid);
		htreadval.put(IDBConstants.DESCRIPTION1, description1);
		htreadval.put(IDBConstants.CATLOGPRICE, sprice);
		htreadval.put(IDBConstants.DESCRIPTION2, description2);
		htreadval.put(IDBConstants.DESCRIPTION3, description3);
		htreadval.put(IDBConstants.DETAIL1, detail1);
		htreadval.put(IDBConstants.DETAIL2, detail2);
		htreadval.put(IDBConstants.DETAIL3, detail3);
		htreadval.put(IDBConstants.DETAIL4, detail4);
		htreadval.put(IDBConstants.DETAIL5, detail5);
		htreadval.put(IDBConstants.DETAIL6, detail6);
		htreadval.put(IDBConstants.DETAIL7, detail7);
		htreadval.put(IDBConstants.DETAILLABEL, detailHeading);

		// System.out.println("desc1"+description1+description2+description3+plant+userName+price+productid);
		Hashtable htCatalog = new Hashtable();
		if (imageSizeflg) {
			session.setAttribute("EDITLIST", htreadval);
			response.sendRedirect("jsp/editCatalog.jsp?result=" + result
					+ "&PRODUCTID=" + productid + "&action=editvalues");
		} else {
			if (_itemutil.isExistsItemMst(productid, plant)) {
				Hashtable htCatlogcn = new Hashtable();
				htCatlogcn.put(IDBConstants.PRODUCTID, productid);
				htCatlogcn.put(IConstants.PLANT, plant);
				htCatalog.put(IConstants.PLANT, plant);
				// Check ProductId exist in Catlogmst
				prdflag = _catalogUtil.isExistCatalog(htCatlogcn, "");

				htCatalog.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
				htCatalog.put(IDBConstants.DESCRIPTION1, description1);
				htCatalog.put(IDBConstants.DESCRIPTION2, description2);
				htCatalog.put(IDBConstants.DESCRIPTION3, description3);
				htCatalog.put(IDBConstants.DETAILLABEL, detailHeading); //updating detail label
				htCatalog.put(IDBConstants.DETAIL1, detail1.toString());
				htCatalog.put(IDBConstants.DETAIL2, detail2.toString());
				htCatalog.put(IDBConstants.DETAIL3, detail3.toString());
				htCatalog.put(IDBConstants.DETAIL4, detail4.toString());
				htCatalog.put(IDBConstants.DETAIL5, detail5.toString());
				htCatalog.put(IDBConstants.DETAIL6, detail6.toString());
				htCatalog.put(IDBConstants.DETAIL7, detail7.toString());
				htCatalog.put(IDBConstants.CATLOGPRICE, sprice);
				htCatalog.put(IConstants.UPDATED_BY, userName);
				strCatlogpath = strCatlogpath.replace('\\', '/');
				if (strCatlogpath.length() > 0)
					htCatalog.put(IDBConstants.CATLOGPATH, strCatlogpath);
                                        
                                        System.out.println("strCatlogpath:::::::::::"+strCatlogpath);
				htCatalog.put(IDBConstants.ISACTIVE, isActive);

				ut = com.track.gates.DbBean.getUserTranaction();
				ut.begin();
				flag = _catalogUtil.updateMst(htCatalog, htCatlogcn);
				if (flag) {
					htCatalog.put(IConstants.PLANT, plant);
					htCatalog.put(IConstants.PRODUCTID, productid);
					htCatalog.put(IConstants.CREATED_BY, userName);
					flag = insertMovhis(htCatalog,
					TransactionConstants.EDIT_CATALOG);

				}
				if (flag) {
					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Catalog Updated successfully</font>";

					response.sendRedirect("jsp/editCatalog.jsp?result="
							+ result + "&PRODUCTID=" + productid
							+ "&action=view");

				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to update Catalog </font>";
					response.sendRedirect("jsp/editCatalog.jsp?result="
							+ result);
				}

				// catlogid exist

			} else {
				result = "<font color=\"red\"> Product ID does not exist </font>";
				response.sendRedirect("jsp/editCatalog.jsp?result=" + result);
			}
		}
	}
	/**
	 * Author Bruhan. 12 july 2012
	 * method : process_delCatalog(HttpServletRequest request,
			HttpServletResponse response) description : Deletes the
	 * Calatog from the catalog master)
	 * 
	 * @param : HttpServletRequest request,
			HttpServletResponse response
	 * @return : 
	 * @throws Exception
	 */
	private void process_delCatalog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		String productid = StrUtils.fString(request.getParameter("PRODUCTID"));
		UserTransaction ut = null;
		Hashtable htCatalog = new Hashtable();
		Hashtable htCatlogcn = new Hashtable();
		htCatlogcn.put(IDBConstants.PRODUCTID, productid);
		htCatlogcn.put(IConstants.PLANT, plant);
		// Check ProductId exist in Catlog Master
		prdflag = _catalogUtil.isExistCatalog(htCatlogcn, "");
		if (prdflag) {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			flag = _catalogUtil.delMst(htCatlogcn);
			//Insert into MovHis 
			if (flag) {
				htCatalog.put(IConstants.PLANT, plant);
				htCatalog.put(IConstants.PRODUCTID, productid);
				htCatalog.put(IConstants.CREATED_BY, userName);
				flag = insertMovhis(htCatalog, TransactionConstants.DEL_CATALOG);

			}
			if (flag) {
				DbBean.CommitTran(ut);
				result = "<font color=\"green\"> Catalog Deleted successfully</font>";

				response.sendRedirect("jsp/editCatalog.jsp?result=" + result
						+ "&PRODUCTID=" + productid + "&action=view");

			} else {
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Failed to delete Catalog </font>";
				response.sendRedirect("jsp/editCatalog.jsp?result=" + result);
			}

			// catlogid exist

		} else {
			result = "<font color=\"red\"> Product ID does not exist in Catalog </font>";
			response.sendRedirect("jsp/editCatalog.jsp?result=" + result);
		}
		
	}


	private void UploadLogo(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		String action = StrUtils.fString(request.getParameter("Submit"));
		HttpSession session = request.getSession();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String type = StrUtils.fString(request.getParameter("Type"));
	
		try{
		
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			fileLocation = DbBean.COMPANY_LOGO_PATH ;
			
			boolean imageSizeflg = false;
			if (!isMultipart) {
				System.out.println("File Not Uploaded");
			} else {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						

					} else if (!item.isFormField() && (item.getName().length() > 0)) {
						String fileName = item.getName();
						long size = item.getSize();

						size = size / 1024;
						System.out.println("size of the Image imported :::"
								+ size);
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";

							imageSizeflg = true;

						}
						File path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" + plant+type+ ".GIF");

						if (uploadedFile.exists()) {
						 uploadedFile.delete();
						 }
						strpath = path + "/" + fileName;
						catlogpath = uploadedFile.getAbsolutePath();
						if (!imageSizeflg && !uploadedFile.exists())
							item.write(uploadedFile);
						if(type.equalsIgnoreCase("Logo")){
						result = "<font color=\"green\"> Logo1 uploaded Successfully </font>";
						}else if(type.equalsIgnoreCase("Logo1")){
						result = "<font color=\"green\"> Logo2 uploaded Successfully </font>";
						}
					}

				}
			}
			
			if (imageSizeflg) {
				response.sendRedirect("jsp/uploadLogo.jsp?result=" + result+ "&PRODUCTID=" + productid + "&action=view");
			} else {
					
					response.sendRedirect("jsp/uploadLogo.jsp?result="+ result + "&action=view");
				}
			
		}catch(Exception e){
			result = "<font color=\"red\">" +e.getMessage()+" </font>";
			response.sendRedirect("jsp/createCatalog.jsp?result="+ result + "&action=view");
		}
     }

   
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void process_orderStatus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		DoHdrDAO dohdrdao = new DoHdrDAO();
		List outbndlst = null;
		dohdrdao.setmLogger(mLogger);
		String result = "";
		boolean existFlag = true, existmobflg = true;
		HttpSession session = request.getSession();
		String plant = request.getParameter(IDBConstants.PLANT);
		String mobileno = "", orderno = "";
		mobileno = StrUtils.fString(request.getParameter("MOBILENO"));
		orderno = StrUtils.fString(request.getParameter("ORDERNO"));

		Hashtable htutil = new Hashtable();
		htutil.put("DONO", orderno);
		htutil.put("PLANT", plant);

		// check valid order no
		if (orderno.length() > 0)
			existFlag = dohdrdao.isExisit(htutil, "");

		// check for valid mobile no
		htutil.remove("DONO");
		htutil.put("USER_ID", mobileno);

		if (mobileno.length() > 0)
			existmobflg = _custmst.isExisit(htutil, "");

		if (!existFlag) {
			result = "Not a valid order number";
		} else if (!existmobflg) {
			result = "Not a valid mobile number";
		} else {
			htutil.remove("USER_ID");
			htutil.put("DONO", orderno);
			htutil.put("MOBILENO", mobileno);
			outbndlst = dohdrdao.OrderStatus(htutil, " order by dono desc");
			Map linearr = (Map) outbndlst.get(0);
			if (linearr.size() < 0)
				result = "No data found";
			session.setAttribute("MBORDERLIST", outbndlst);
		}

		response.sendRedirect("jsp/mobileOrderStatus.jsp?PLANT=" + plant
				+ "&result=" + result);

	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
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
			e.printStackTrace();
		}

	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}
	public JSONObject itemImageUpdate(HttpServletRequest request,
			HttpServletResponse response,String ITEMS) {
		String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		StrUtils strUtils = new StrUtils();
		ItemUtil itemUtil = new ItemUtil();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ItemUtil _itemutil = new ItemUtil();
		List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		
		if (isMultipart) {
			try {
				UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
				String result = "", strpath = "", catlogpath = "";
				String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
				String filetempLocation = DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
				boolean imageSizeflg = false;
				String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);//Check Parent Plant or child plant
				boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
				Hashtable htData = new Hashtable();	

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				
				String img="";
				String ITEM = "";
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("ITEM")) {
							ITEM = StrUtils.fString(item.getString());
						}
					} else if (!item.isFormField() && (item.getName().length() > 0)) {

						String fileName = item.getName();
						long size = item.getSize();

						size = size / 1024;
						// size = size / 1000;
						System.out.println("size of the Image imported :::" + size);
						// checking image size for 2MB
						
						String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
						System.out.println("Extensions:::::::" + extension);
						if (!imageFormatsList.contains(extension)) {
							result = "<font color=\"red\"> Image extension not valid </font>";
							resultObj.put("product", ITEM);
							resultObj.put("message", result);
							resultJSON.put("result", resultObj);
							imageSizeflg = true;
							return resultJSON;
						}
						/*if(!(plant.equalsIgnoreCase("C2716640758S2T") || plant.equalsIgnoreCase("C1255800687S2T") || plant.equalsIgnoreCase("C4376460171S2T") 
								|| plant.equalsIgnoreCase("C5500747293S2T") || plant.equalsIgnoreCase("C697464484S2T") || plant.equalsIgnoreCase("C7743535839S2T") 
								|| plant.equalsIgnoreCase("C947002346S2T"))){
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";

							imageSizeflg = true;
							resultObj.put("product", ITEM);
							resultObj.put("message", result);
							resultJSON.put("result", resultObj);
							return resultJSON;
						}}*/
						if(PARENT_PLANT == null){
							String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int i=0; i < arrLists.size(); i++ ) {
				        			  Map ms = (Map) arrLists.get(i);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
				        		  }
				        	  }
						}
						//File path = new File(fileLocation);
						File path = new File(filetempLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(ITEM);
						if(ITEMS!=null) {
							uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
							img = uploadedFile.toString();
						}else {
							uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
						}

						// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						strpath = path + "/" + fileName;
						//catlogpath = uploadedFile.getAbsolutePath();
						filetempLocation = uploadedFile.getAbsolutePath();
						if (!imageSizeflg && !uploadedFile.exists())
							item.write(uploadedFile);
						
						File sourceimage = new File(filetempLocation);
						BufferedImage originalImage = javax.imageio.ImageIO.read(sourceimage);
						path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						//File outputFile = new File(path + "/Com/" + strUtils.RemoveSlash(ITEM)+".JPEG");
						File outputFile = new File(path +"/"+ strUtils.RemoveSlash(ITEM)+".JPEG");
						if (outputFile.exists()) {
							outputFile.delete();
						}
						catlogpath = outputFile.getAbsolutePath();
						saveCompressedImage(originalImage, "JPEG", outputFile);
						if (uploadedFile.exists()) {
							 uploadedFile.delete();
						}

						// delete temp uploaded file
						/*File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}*/
						
						//IMAGE START
						  /*if(PARENT_PLANT != null){
				        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
				        	  if(CHECKplantCompany == null)
				  				CHECKplantCompany = "0";
				        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  Map m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  //fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;--Azees Image to Update only Parent Company 23.09.24
					        			  fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ PARENT_PLANT;
					        			  path = new File(fileLocation);
						  					if (!path.exists()) {
						  						boolean status = path.mkdirs();
						  					}
						  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						  					if(ITEMS!=null) {
												uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
												img = uploadedFile.toString();
											}else {
												uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
											}
//						  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
						  					strpath = path + "/" + fileName;
						  					String childcatlogpath = uploadedFile.getAbsolutePath();
						  					File childpath = new File(childcatlogpath);
//						  					File parentpath = new File(catlogpath);
						  					File parentpath = new File("");
						  					if(ITEMS!=null) {
						  						parentpath = new File(catlogpath);
						  					}else {
						  						parentpath = new File(catlogpath);
						  					}
						  					if (uploadedFile.exists()) {
												uploadedFile.delete();
											}
						  					if (!imageSizeflg && !uploadedFile.exists())
						  					FileUtils.copyFile(parentpath, childpath);
					        		  	}
					        	  	}
				        	  	}
						  }else if(PARENT_PLANT == null){
							  boolean ischild = false;
					        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
					        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
					        	  if (arrLi.size() > 0) {
					        	  Map mst = (Map) arrLi.get(0);
					        	  String parent = (String) mst.get("PARENT_PLANT");
					         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
					        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
					        	  if(Ischildasparent){
					        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        			  ischild = true;
					        		  }
					        	  }else{
					        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  ischild = true;
					        	  }
					        	  }
					        	  if(ischild){
					        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
						        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
						        	  String  parentplant ="";
						        	  if (arrLists.size() > 0) {
						        		  for (int i=0; i < arrLists.size(); i++ ) {
						        			  Map ms = (Map) arrLists.get(i);
						        			  parentplant = (String) ms.get("PARENT_PLANT");
						        			  fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
						        			  path = new File(fileLocation);
							  					if (!path.exists()) {
							  						boolean status = path.mkdirs();
							  					}
							  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
							  					strpath = path + "/" + fileName;
							  					String childcatlogpath = uploadedFile.getAbsolutePath();
							  					File childpath = new File(childcatlogpath);
							  					File parentpath = new File(catlogpath);
							  					if (uploadedFile.exists()) {
													uploadedFile.delete();
												}
							  					if (!imageSizeflg && !uploadedFile.exists())
							  					FileUtils.copyFile(parentpath, childpath);
			
						        		  }
						        	  }
						        	  
						        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  Map m = new HashMap();
						        	  if (arrList.size() > 0) {
						        		  for (int i=0; i < arrList.size(); i++ ) {
						        			  m = (Map) arrList.get(i);
						        			  String childplant = (String) m.get("CHILD_PLANT");
						        			  if(childplant!=plant) {
							        			  //fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;--Azees Image to Update only Parent Company 23.09.24
							        			  fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
							        			  path = new File(fileLocation);
								  					if (!path.exists()) {
								  						boolean status = path.mkdirs();
								  					}
								  					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
								  					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM)+ ".JPEG");
								  					strpath = path + "/" + fileName;
								  					String childcatlogpath = uploadedFile.getAbsolutePath();
								  					File childpath = new File(childcatlogpath);
								  					File parentpath = new File(catlogpath);
								  					if (uploadedFile.exists()) {
														uploadedFile.delete();
													}
								  					if (!imageSizeflg && !uploadedFile.exists())
								  					FileUtils.copyFile(parentpath, childpath);
						        			  }
						        		  	}
						        	  	}
					        	  }	
			             	  }
			             	}*/
						  //IMAGE END
					}

				}
				ut.begin();
				String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
				catlogpath = catlogpath.replace('\\', '/');
				if (ITEM.equals("")) {
					throw new Exception("");
				}
				if (specialcharsnotAllowed.length() > 0) {
					throw new Exception("Product ID  value : '" + ITEM + "' has special characters "
							+ specialcharsnotAllowed + " that are  not allowed ");
				}
				boolean itemInserted =false;
				//imti Start for addiional image in catlogmst
				if(ITEMS==null) {
					itemInserted = itemUtil.updateItemImage(plant, ITEM, catlogpath); //rewite this existing code itemmst save based on condition 
				}
		  	 	Hashtable ht = new Hashtable();
	    	 	ht.put("PRODUCTID", ITEM);
	    	 	ht.put("PLANT", plant);
	    	 	ht.put("CATLOGPATH", img);
				boolean itemImgDlt = new CatalogDAO().delMst(ht);
				Hashtable htCatalog = new Hashtable();
				String usertime  ="";
				int length = img.length();
				if(length>0) {
				int len = length-4;
				String new_word = img.substring(len - 2);
				char firstChar = new_word.charAt(0);
				usertime  = Character.toString(firstChar);
				
				htCatalog.put(IDBConstants.PRODUCTID, ITEM);
				htCatalog.put(IConstants.PLANT, plant);
				htCatalog.put(IConstants.CREATED_BY, userName);
				htCatalog.put(IConstants.ISACTIVE, "Y");
				htCatalog.put(IDBConstants.CATLOGPATH, catlogpath);
				htCatalog.put("USERTIME1", usertime);
				boolean existAdd = new ItemSesBeanDAO().updateAddItemImage(plant, ITEM, catlogpath,usertime);
				boolean itemAddImg = false;
					if(!existAdd) {
						itemAddImg =  _catalogUtil.insertMst(htCatalog);
					}
				if(itemAddImg=true) {
					itemInserted = true;
				}
				}
				//imti end for addiional image in catlogmst
				
				
				//IMAGE START
				  if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  String catpath = catlogpath;
			        			  //catpath = catpath.replace(plant,childplant);
			        				if(ITEMS==null) {
			        					itemInserted = itemUtil.updateItemImage(childplant, ITEM, catpath);
			        				}else {
			        			  	htCatalog.put(IConstants.PLANT, childplant);
			      					htCatalog.put(IDBConstants.CATLOGPATH, catpath);
			      					htCatalog.put("USERTIME1", usertime);
			      					boolean existAdd = new ItemSesBeanDAO().updateAddItemImage(childplant, ITEM, catpath,usertime);
			      						if(!existAdd) {
			      							boolean itemAddImg =  _catalogUtil.insertMst(htCatalog);
			      						}
			        				}
			        		  	}
			        	  	}
		        	  	}
				  }else if(PARENT_PLANT == null){
					  boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int i=0; i < arrLists.size(); i++ ) {
				        			  Map ms = (Map) arrLists.get(i);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  String catpath = catlogpath;
				        			  //catpath = catpath.replace(plant,parentplant);
				        				if(ITEMS==null) {
				        					itemInserted = itemUtil.updateItemImage(parentplant, ITEM, catpath);
				        				}else {
				        				htCatalog.put(IConstants.PLANT, parentplant);
				      					htCatalog.put(IDBConstants.CATLOGPATH, catpath);
				      					htCatalog.put("USERTIME1", usertime);
				      					boolean existAdd = new ItemSesBeanDAO().updateAddItemImage(parentplant, ITEM, catpath,usertime);
				      						if(!existAdd) {
				      							boolean itemAddImg =  _catalogUtil.insertMst(htCatalog);
				      						}
				        				}
	
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  if(childplant!=plant) {
				        				  String catpath = catlogpath;
					        			  //catpath = catpath.replace(plant,childplant);
					        				if(ITEMS==null) {
					        					itemInserted = itemUtil.updateItemImage(childplant, ITEM, catpath);
					        				}else {
					        				htCatalog.put(IConstants.PLANT, childplant);
					      					htCatalog.put(IDBConstants.CATLOGPATH, catpath);
					      					htCatalog.put("USERTIME1", usertime);
					      					boolean existAdd = new ItemSesBeanDAO().updateAddItemImage(childplant, ITEM, catpath,usertime);
					      						if(!existAdd) {
					      							boolean itemAddImg =  _catalogUtil.insertMst(htCatalog);
					      						}
					        				}
				        			  }
				        		  	}
				        	  	}
			        	  }	
	             	  }
	             	}
				  
				  //IMAGE END
				
				if (itemInserted) {
					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Product image changed successfully</font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to change product image </font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				}

			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
			
		}
		return resultJSON;

	}
	public JSONObject itemImageDelete(HttpServletRequest request,
			HttpServletResponse response,String ITEMS) throws Exception {
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils = new StrUtils();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		try {
			String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
			String result = "", strpath = "", catlogpath = "";
			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
			String filetempLocation = DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
			String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);//Check Parent Plant or child plant
			boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent(plant);
			Hashtable htData = new Hashtable();	
			boolean imageSizeflg = false;

			plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();

			String ITEM = "";
			ITEM = request.getParameter("ITEM");

			File path = new File(fileLocation);
			File uploadedFile = new File(ITEM);
			String img="";
			if(ITEMS!=null) {
				uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
				 img = uploadedFile.toString();
			}else {
				uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
			}

			// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
			
			//IMAGE START
			  if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
		        			  path = new File(fileLocations);
		        			  if(ITEMS!=null) {
		        					uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
		        					 img = uploadedFile.toString();
		        				}else {
		        					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
		        				}
//		        			  uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
			  					if (uploadedFile.exists()) {
									uploadedFile.delete();
								}
		        		  	}
		        	  	}
	        	  	}
			  }else if(PARENT_PLANT == null){
				  boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int i=0; i < arrLists.size(); i++ ) {
			        			  Map ms = (Map) arrLists.get(i);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			 String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
			        			  path = new File(fileLocations);
			        			  if(ITEMS!=null) {
			        					uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
			        					 img = uploadedFile.toString();
			        				}else {
			        					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
			        				}
//			        			  uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
				  					if (uploadedFile.exists()) {
										uploadedFile.delete();
									}

			        		  }
			        	  }
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  if(childplant!=plant) {
				        			 String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
				        			  path = new File(fileLocations);
				        			  if(ITEMS!=null) {
				        					uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
				        					 img = uploadedFile.toString();
				        				}else {
				        					uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
				        				}
//				        			  uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
					  					if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
			        			  }
			        		  	}
			        	  	}
		        	  }	
           	  }
           	}
			  
			  //IMAGE END

			ut.begin();
			String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
			catlogpath = "";
			boolean itemInserted = false;
			if(ITEMS==null) {
				itemInserted  = itemUtil.updateItemImage(plant, ITEM, catlogpath);
			}
			Hashtable ht = new Hashtable();
			
			String usertime  ="";
			boolean itemAddImgDlt = false;
			int length = img.length();
			if(length>0) {
			int len = length-4;
			String new_word = img.substring(len - 2);
			char firstChar = new_word.charAt(0);
			usertime  = Character.toString(firstChar);
			
    	 	ht.put("PRODUCTID", ITEM);
    	 	ht.put("PLANT", plant);
    	 	ht.put("USERTIME1", usertime);
			itemAddImgDlt = new CatalogDAO().delMst(ht);
			}
			if(itemAddImgDlt=true) {
				itemInserted = true;
			}
			//IMAGE START
			  if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(plant);
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  if(ITEMS==null) {
		        				  itemInserted = itemUtil.updateItemImage(childplant, ITEM, catlogpath);
		        			  }else {
		        			  	ht.put("PLANT", childplant);
		        	    	 	ht.put("USERTIME1", usertime);
		        				itemAddImgDlt = new CatalogDAO().delMst(ht);
		        			  }
		        		  	}
		        	  	}
	        	  	}
			  }else if(PARENT_PLANT == null){
				  boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int i=0; i < arrLists.size(); i++ ) {
			        			  Map ms = (Map) arrLists.get(i);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  if(ITEMS==null) {
			        				  itemInserted = itemUtil.updateItemImage(parentplant, ITEM, catlogpath);
			        			  }else {
			        			  	ht.put("PLANT", parentplant);
			        	    	 	ht.put("USERTIME1", usertime);
			        				itemAddImgDlt = new CatalogDAO().delMst(ht);
			        			  }

			        		  }
			        	  }
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+plant+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  if(childplant!=plant) {
			        				  catlogpath = catlogpath.replace(plant,childplant);
			        				  if(ITEMS==null) {
			        					  itemInserted = itemUtil.updateItemImage(childplant, ITEM, catlogpath);
			        				  }else {
			        				  	ht.put("PLANT", childplant);
				        	    	 	ht.put("USERTIME1", usertime);
				        				itemAddImgDlt = new CatalogDAO().delMst(ht);
			        				  }
			        			  }
			        		  	}
			        	  	}
		        	  }	
           	  }
           	}
			  
			  //IMAGE END
			
			if (itemInserted) {
				DbBean.CommitTran(ut);

				result = "<font color=\"green\"> Product image deleted successfully</font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			} else {
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Failed to delete product image </font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			}
		} catch (NamingException | NotSupportedException | SystemException e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}
	public JSONObject catryImageUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		StrUtils strUtils = new StrUtils();
		PrdClassDAO _prdClassDAO = new PrdClassDAO();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ItemUtil _itemutil = new ItemUtil();
		List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		if (isMultipart) {
			try {
				UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
				String result = "", strpath = "", catlogpath = "";
				String fileLocation = DbBean.COMPANY_CATAGERY_PATH + "/" + plant;
				String filetempLocation = DbBean.COMPANY_CATAGERY_PATH + "/temp" + "/" + plant;
				boolean imageSizeflg = false;

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				

				String ITEM = "";
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("PRD_CLS_ID")) {
							ITEM = StrUtils.fString(item.getString());
						}
					} else if (!item.isFormField() && (item.getName().length() > 0)) {

						String fileName = item.getName();
						long size = item.getSize();

						size = size / 1024;
						// size = size / 1000;
						System.out.println("size of the Image imported :::" + size);
						// checking image size for 2MB
						
						String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
						System.out.println("Extensions:::::::" + extension);
						if (!imageFormatsList.contains(extension)) {
							result = "<font color=\"red\"> Image extension not valid </font>";
							resultObj.put("product", ITEM);
							resultObj.put("message", result);
							resultJSON.put("result", resultObj);
							imageSizeflg = true;
							return resultJSON;
						}
						
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";

							imageSizeflg = true;
							resultObj.put("product", ITEM);
							resultObj.put("message", result);
							resultJSON.put("result", resultObj);
							return resultJSON;
						}
						File path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");

						// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
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
							File tempUploadedfile = new File(tempPath + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
					}

				}
				ut.begin();
				String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
				catlogpath = catlogpath.replace('\\', '/');
				if (ITEM.equals("")) {
					throw new Exception("");
				}
//				if (specialcharsnotAllowed.length() > 0) {
//					throw new Exception("Product ID  value : '" + ITEM + "' has special characters "
//							+ specialcharsnotAllowed + " that are  not allowed ");
//				}
				boolean itemInserted = _prdClassDAO.updateCatryImage(plant, ITEM, catlogpath);
				if (itemInserted) {
					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Product class image changed successfully</font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to change product class image </font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				}

			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
			
		}
		return resultJSON;

	}
	public JSONObject catryImageDelete(HttpServletRequest request,
			HttpServletResponse response) {
		PrdClassDAO _prdClassDAO = new PrdClassDAO();
		StrUtils strUtils = new StrUtils();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		try {
			String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
			String result = "", strpath = "", catlogpath = "";
			String fileLocation = DbBean.COMPANY_CATAGERY_PATH + "/" + plant;
			String filetempLocation = DbBean.COMPANY_CATAGERY_PATH+ "/temp" + "/" + plant;
			boolean imageSizeflg = false;

			plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();

			String ITEM = "";
			ITEM = request.getParameter("PRD_CLS_ID");

			File path = new File(fileLocation);

			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(ITEM) + ".JPEG");

			// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}

			ut.begin();
			String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
			catlogpath = "";

			boolean itemInserted = _prdClassDAO.updateCatryImage(plant, ITEM, catlogpath);
			if (itemInserted) {
				DbBean.CommitTran(ut);

				result = "<font color=\"green\"> Product image deleted successfully</font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			} else {
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Failed to delete product image </font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			}
		} catch (NamingException | NotSupportedException | SystemException e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}
	public JSONObject logoImageUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String type = StrUtils.fString(request.getParameter("Type"));
		StrUtils strUtils = new StrUtils();
		ItemUtil itemUtil = new ItemUtil();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ItemUtil _itemutil = new ItemUtil();
		List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		if (isMultipart) {
			try {
				String result = "", strpath = "", catlogpath = "";
				//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
				boolean imageSizeflg = false;

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				//String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;

				String ITEM = "",sLogo="";;
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("ITEM")) {
							/*ITEM = StrUtils.fString(item.getString());*/
						}
					//} else if (!item.isFormField() && (item.getName().length() > 0)) {
					} 
					if (!item.isFormField()) {
						if(item.getFieldName().equalsIgnoreCase("IMAGE_UPLOAD")) 
    					{
    					String fileName = StrUtils.fString(item.getName()).trim();
    					sLogo=fileName;
						
						long size = item.getSize();

						size = size / 1024;
						System.out.println("size of the Image imported :::"
								+ size);
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Logo Image size greater than 1 MB </font>";

							imageSizeflg = true;

						}
						
    					fileLocation = DbBean.COMPANY_LOGO_PATH + "/"+ plant.toLowerCase() + DbBean.LOGO_FILE;
	         			filetempLocation = DbBean.COMPANY_LOGO_PATH + "/temp" + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	         			
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
						
/*						File path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(imagePath);

						if (uploadedFile.exists()) {
						 uploadedFile.delete();
						 }
						strpath = path + "/" + fileName;
						catlogpath = uploadedFile.getAbsolutePath();
						if (!imageSizeflg && !uploadedFile.exists())
							item.write(uploadedFile);*/
						if(type.equalsIgnoreCase("Logo")){
						result = "<font color=\"green\"> Logo uploaded Successfully </font>";
						}
						}
    					
					} 
						

				}
				
				
				catlogpath = catlogpath.replace('\\', '/');
				File imageFile = new File(fileLocation);
				
				boolean itemInserted = imageFile.exists();;
				if (itemInserted) {

					result = "<font color=\"green\"> Logo image Updated successfully</font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				} else {
					result = "<font color=\"red\"> Failed to Update logo image </font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				}


			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
			
		}
		return resultJSON;

	}
	
	public JSONObject appImageUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String type = StrUtils.fString(request.getParameter("Type"));
		String spalnt = StrUtils.fString(request.getParameter("SPlant"));
		StrUtils strUtils = new StrUtils();
		ItemUtil itemUtil = new ItemUtil();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ItemUtil _itemutil = new ItemUtil();
		List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		if (isMultipart) {
			try {
				String result = "", strpath = "", catlogpath = "";
				//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
				boolean imageSizeflg = false;

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				//String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;

				String ITEM = "",sLogo="";;
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("ITEM")) {
							/*ITEM = StrUtils.fString(item.getString());*/
						}
					//} else if (!item.isFormField() && (item.getName().length() > 0)) {
					} 
					if (!item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("APP_IMAGE_UPLOAD")) 
    					{
    					String fileName = StrUtils.fString(item.getName()).trim();
    					sLogo=fileName;
						
						long size = item.getSize();

						size = size / 1024;
						System.out.println("size of the Image imported :::"
								+ size);
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Order App Image size greater than 1 MB </font>";

							imageSizeflg = true;

						}
						
						fileLocation = DbBean.COMPANY_ORDER_APP_BACKGROUND_PATH + "/"+ plant.toLowerCase() + fileName;
	         			filetempLocation = DbBean.COMPANY_ORDER_APP_BACKGROUND_PATH + "/temp" + "/" + plant.toLowerCase() + fileName;
	         			String fname = plant.toLowerCase() + fileName;
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
    					PlantMstDAO _PlantMstDAO = new PlantMstDAO();
    					Hashtable htCond=new Hashtable();
    					htCond.put("PLANT",spalnt);  
    					StringBuffer updateQyery=new StringBuffer("set ");
    					updateQyery.append("APPPATH = '"+fname+ "'");
    					_PlantMstDAO.update(updateQyery.toString(),htCond,""); 

						if(type.equalsIgnoreCase("Logo")){
						result = "<font color=\"green\"> Order App Image uploaded Successfully </font>";
						}
						}
    					
					} 
						

				}
				
				
				catlogpath = catlogpath.replace('\\', '/');
				File imageFile = new File(fileLocation);
				
				boolean itemInserted = imageFile.exists();;
				if (itemInserted) {

					result = "<font color=\"green\"> Order App Image Updated successfully</font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				} else {
					result = "<font color=\"red\"> Failed to Update Order App image </font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				}


			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
			
		}
		return resultJSON;

	}
	
	public JSONObject sealImageUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String type = StrUtils.fString(request.getParameter("Type"));
		String spalnt = StrUtils.fString(request.getParameter("SPlant"));
		StrUtils strUtils = new StrUtils();
		ItemUtil itemUtil = new ItemUtil();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ItemUtil _itemutil = new ItemUtil();
		List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		if (isMultipart) {
			try {
				String result = "", strpath = "", catlogpath = "";
				//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
				boolean imageSizeflg = false;

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				//String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;

				String ITEM = "",sLogo="";;
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("ITEM")) {
							/*ITEM = StrUtils.fString(item.getString());*/
						}
					//} else if (!item.isFormField() && (item.getName().length() > 0)) {
					} 
					if (!item.isFormField()) {
						if(item.getFieldName().equalsIgnoreCase("seal")) 
    					{
    					String fileName = StrUtils.fString(item.getName()).trim();
    					sLogo=fileName;
						
						long size = item.getSize();

						size = size / 1024;
						System.out.println("size of the Image imported :::"
								+ size);
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Logo Image size greater than 1 MB </font>";

							imageSizeflg = true;

						}
						
						fileLocation = DbBean.COMPANY_SEAL_PATH + "/"+ plant.toLowerCase() + fileName;
	         			filetempLocation = DbBean.COMPANY_SEAL_PATH + "/temp" + "/" + plant.toLowerCase() + fileName;
	         			String fname = plant.toLowerCase() + fileName;
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
    					PlantMstDAO _PlantMstDAO = new PlantMstDAO();
    					Hashtable htCond=new Hashtable();
    					htCond.put("PLANT",spalnt);  
    					StringBuffer updateQyery=new StringBuffer("set ");
    					updateQyery.append("SEALNAME = '"+fname+ "'");
    					_PlantMstDAO.update(updateQyery.toString(),htCond,""); 

						if(type.equalsIgnoreCase("Logo")){
						result = "<font color=\"green\"> Seal uploaded Successfully </font>";
						}
						}
    					
					} 
						

				}
				
				
				catlogpath = catlogpath.replace('\\', '/');
				File imageFile = new File(fileLocation);
				
				boolean itemInserted = imageFile.exists();;
				if (itemInserted) {

					result = "<font color=\"green\"> Seal image Updated successfully</font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				} else {
					result = "<font color=\"red\"> Failed to Update Seal image </font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				}


			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
			
		}
		return resultJSON;

	}
	
	public JSONObject signImageUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String type = StrUtils.fString(request.getParameter("Type"));
		StrUtils strUtils = new StrUtils();
		ItemUtil itemUtil = new ItemUtil();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		ItemUtil _itemutil = new ItemUtil();
		List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		if (isMultipart) {
			try {
				String result = "", strpath = "", catlogpath = "";
				//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
				boolean imageSizeflg = false;

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				//String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;

				String ITEM = "",sLogo="";;
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("ITEM")) {
							/*ITEM = StrUtils.fString(item.getString());*/
						}
					//} else if (!item.isFormField() && (item.getName().length() > 0)) {
					} 
					if (!item.isFormField()) {
						if(item.getFieldName().equalsIgnoreCase("dsignature")) 
    					{
    					String fileName = StrUtils.fString(item.getName()).trim();
    					sLogo=fileName;
						
						long size = item.getSize();

						size = size / 1024;
						System.out.println("size of the Image imported :::"
								+ size);
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Signature Image size greater than 1 MB </font>";

							imageSizeflg = true;

						}
						
						fileLocation = DbBean.COMPANY_SIGNATURE_PATH + "/"+ plant.toLowerCase() + fileName;
	         			filetempLocation = DbBean.COMPANY_SIGNATURE_PATH + "/temp" + "/" + plant.toLowerCase() + fileName;
	         			String fname = plant.toLowerCase() + fileName;
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
    					PlantMstDAO _PlantMstDAO = new PlantMstDAO();
    					Hashtable htCond=new Hashtable();
    					htCond.put("PLANT",plant);  
    					StringBuffer updateQyery=new StringBuffer("set ");
    					updateQyery.append("SIGNATURENAME = '"+fname+ "'");
    					_PlantMstDAO.update(updateQyery.toString(),htCond,""); 

						if(type.equalsIgnoreCase("Logo")){
						result = "<font color=\"green\"> Signature uploaded Successfully </font>";
						}
						}
    					
					} 
						

				}
				
				
				catlogpath = catlogpath.replace('\\', '/');
				File imageFile = new File(fileLocation);
				
				boolean itemInserted = imageFile.exists();;
				if (itemInserted) {

					result = "<font color=\"green\"> Signature image Updated successfully</font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				} else {
					result = "<font color=\"red\"> Failed to Update Signature image </font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				}


			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
			
		}
		return resultJSON;

	}
	
	public JSONObject logoImageDelete(HttpServletRequest request,
			HttpServletResponse response) {
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils = new StrUtils();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		try {
			String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String type = StrUtils.fString(request.getParameter("Type"));
			String result = "", strpath = "", catlogpath = "";
			//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
			boolean imageSizeflg = false;

			plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
			String ITEM = "";
			ITEM = plant+type;

			File path = new File(fileLocation);
			//File uploadedFile = new File(path + "/" + plant+type+ ".GIF");
			File uploadedFile = new File(imagePath);

			// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}

			
			File imageFile = new File(imagePath);
			boolean itemInserted = imageFile.exists();
			if (!itemInserted) {

				result = "<font color=\"green\"> Logo image deleted successfully</font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			} else {
				result = "<font color=\"red\"> Failed to delete Logo image </font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}
	
	public JSONObject appImageDelete(HttpServletRequest request,
			HttpServletResponse response) {
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils = new StrUtils();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		try {
			String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String type = StrUtils.fString(request.getParameter("Type"));
			String result = "", strpath = "", catlogpath = "";
			//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
			boolean imageSizeflg = false;
			String fileName = "";
			PlantMstUtil plantmstutil = new PlantMstUtil();
			List viewlistQry = plantmstutil.getPlantMstDetails(plant);
			for (int i = 0; i < viewlistQry.size(); i++) {
				Map map = (Map) viewlistQry.get(i);
				fileName=StrUtils.fString((String)map.get("APPPATH"));
				//SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
			}
			
			plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String imagePath = DbBean.COMPANY_ORDER_APP_BACKGROUND_PATH + "/"+fileName;
			String ITEM = "";
			ITEM = plant+type;
			
			File path = new File(fileLocation);
			//File uploadedFile = new File(path + "/" + plant+type+ ".GIF");
			File uploadedFile = new File(imagePath);
			
			// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
			
			
			File imageFile = new File(imagePath);
			boolean itemInserted = imageFile.exists();
			if (!itemInserted) {
				
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();
				Hashtable htCond=new Hashtable();
				htCond.put("PLANT",plant);  
				StringBuffer updateQyery=new StringBuffer("set ");
				updateQyery.append("APPPATH = ''");
				_PlantMstDAO.update(updateQyery.toString(),htCond,""); 
				
				result = "<font color=\"green\"> Order App Image deleted successfully</font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			} else {
				result = "<font color=\"red\"> Failed to delete Order App Image </font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}
	
	public JSONObject sealImageDelete(HttpServletRequest request,
			HttpServletResponse response) {
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils = new StrUtils();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		try {
			String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String type = StrUtils.fString(request.getParameter("Type"));
			String result = "", strpath = "", catlogpath = "";
			//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
			boolean imageSizeflg = false;
			String fileName = "";
			PlantMstUtil plantmstutil = new PlantMstUtil();
			List viewlistQry = plantmstutil.getPlantMstDetails(plant);
            for (int i = 0; i < viewlistQry.size(); i++) {
                Map map = (Map) viewlistQry.get(i);
                fileName=StrUtils.fString((String)map.get("SEALNAME"));
                //SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
            }

			plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String imagePath = DbBean.COMPANY_SEAL_PATH + "/"+fileName;
			String ITEM = "";
			ITEM = plant+type;

			File path = new File(fileLocation);
			//File uploadedFile = new File(path + "/" + plant+type+ ".GIF");
			File uploadedFile = new File(imagePath);

			// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}

			
			File imageFile = new File(imagePath);
			boolean itemInserted = imageFile.exists();
			if (!itemInserted) {
				
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();
				Hashtable htCond=new Hashtable();
				htCond.put("PLANT",plant);  
				StringBuffer updateQyery=new StringBuffer("set ");
				updateQyery.append("SEALNAME = ''");
				_PlantMstDAO.update(updateQyery.toString(),htCond,""); 
				
				result = "<font color=\"green\"> Seal image deleted successfully</font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			} else {
				result = "<font color=\"red\"> Failed to delete Seal image </font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}
	
	public JSONObject signImageDelete(HttpServletRequest request,
			HttpServletResponse response) {
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils = new StrUtils();
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		try {
			String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String type = StrUtils.fString(request.getParameter("Type"));
			String result = "", strpath = "", catlogpath = "";
			//String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/" + plant;
			boolean imageSizeflg = false;
			String fileName = "";
			PlantMstUtil plantmstutil = new PlantMstUtil();
			List viewlistQry = plantmstutil.getPlantMstDetails(plant);
            for (int i = 0; i < viewlistQry.size(); i++) {
                Map map = (Map) viewlistQry.get(i);
                //fileName=StrUtils.fString((String)map.get("SEALNAME"));
                fileName=StrUtils.fString((String)map.get("SIGNATURENAME"));
            }

			plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String imagePath = DbBean.COMPANY_SIGNATURE_PATH + "/"+fileName;
			String ITEM = "";
			ITEM = plant+type;

			File path = new File(fileLocation);
			//File uploadedFile = new File(path + "/" + plant+type+ ".GIF");
			File uploadedFile = new File(imagePath);

			// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}

			
			File imageFile = new File(imagePath);
			boolean itemInserted = imageFile.exists();
			if (!itemInserted) {
				
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();
				Hashtable htCond=new Hashtable();
				htCond.put("PLANT",plant);  
				StringBuffer updateQyery=new StringBuffer("set ");
				updateQyery.append("SIGNATURENAME = ''");
				_PlantMstDAO.update(updateQyery.toString(),htCond,""); 
				
				result = "<font color=\"green\"> Signature image deleted successfully</font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			} else {
				result = "<font color=\"red\"> Failed to delete Signature image </font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}
	
	public JSONObject empImageUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		String plant = plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		StrUtils strUtils = new StrUtils();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		EmployeeUtil empUtil = new EmployeeUtil();
		if (isMultipart) {
			try {
				UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
				String result = "", strpath = "", catlogpath = "";
				String fileLocation = DbBean.COMPANY_EMPLOYEE_PATH + "/" + plant;
				String filetempLocation = DbBean.COMPANY_EMPLOYEE_PATH + "/temp" + "/" + plant;
				boolean imageSizeflg = false;
				List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				String empID = "";
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {
						System.out.println(item.getFieldName());
						if (item.getFieldName().equalsIgnoreCase("CUST_CODE")) {
							empID = StrUtils.fString(item.getString());
						}
					} else if (!item.isFormField() && (item.getName().length() > 0)) {
						if(item.getFieldName().equalsIgnoreCase("IMAGE_UPLOAD1")){
							String fileName = item.getName();
							long size = item.getSize();
							String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
							System.out.println("Extensions:::::::" + extension);
							if (!imageFormatsList.contains(extension)) {
								result = "<font color=\"red\"> Image extension not valid </font>";
								resultObj.put("employee", empID);
								resultObj.put("message", result);
								resultJSON.put("result", resultObj);
								imageSizeflg = true;
								return resultJSON;
							}
							size = size / 1024;
							// size = size / 1000;
							System.out.println("size of the Image imported :::" + size);
							// checking image size for 2MB
							if (size > 2040) // condtn checking Image size
							{
								result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";
								resultObj.put("employee", empID);
								resultObj.put("message", result);
								resultJSON.put("result", resultObj);
								imageSizeflg = true;
								return resultJSON;
							}
							File path = new File(fileLocation);
							if (!path.exists()) {
								boolean status = path.mkdirs();
							}
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(empID) + ".JPEG");
	
							// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
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
								File tempUploadedfile = new File(tempPath + "/" + strUtils.RemoveSlash(empID) + ".JPEG");
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
						}
					}

				}
				ut.begin();
				String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(empID);
				catlogpath = catlogpath.replace('\\', '/');
				if (empID.equals("")) {
					throw new Exception("");
				}
				if (specialcharsnotAllowed.length() > 0) {
					throw new Exception("Employee ID  value : '" + empID + "' has special characters "
							+ specialcharsnotAllowed + " that are  not allowed ");
				}
				boolean itemInserted = empUtil.updateEmployeeMst(plant, empID, catlogpath);
				if (itemInserted) {
					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Employee image changed successfully</font>";
					resultObj.put("employee", empID);
					resultObj.put("message", result);
				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to change employee image </font>";
					resultObj.put("employee", empID);
					resultObj.put("message", result);
				}

			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
		}
		return resultJSON;
	}
	public JSONObject empImageDelete(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		StrUtils strUtils = new StrUtils();
		EmployeeUtil empUtil = new EmployeeUtil();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
			String result = "", strpath = "", catlogpath = "";
			String fileLocation = DbBean.COMPANY_EMPLOYEE_PATH + "/" + plant;
			String filetempLocation = DbBean.COMPANY_EMPLOYEE_PATH + "/temp" + "/" + plant;
			boolean imageSizeflg = false;



			String empId = request.getParameter("CUST_CODE");



					File path = new File(fileLocation);

					File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(empId) + ".JPEG");

					// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
					if (uploadedFile.exists()) {
						uploadedFile.delete();
					}

			ut.begin();
			String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(empId);
			catlogpath = "";

			boolean itemInserted = empUtil.updateEmployeeMst(plant, empId, catlogpath);
			if (itemInserted) {
				DbBean.CommitTran(ut);

				result = "<font color=\"green\"> Employee image removed successfully</font>";
				resultObj.put("employee", empId);
				resultObj.put("message", result);
			} else {
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Failed to remove employee image </font>";
				resultObj.put("employee", empId);
				resultObj.put("message", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}

	private JSONObject UploadUserImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String catalogspercompany = "", detailHeading = "", userID = "", USER_ID1 = "", userImagePath = "",
				fileLocation = "", filetempLocation = "", result = "", strpath;
		String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		UserTransaction ut = null;
		selectBean sb = new selectBean();
		int iIndex = 1;
		String xmlStr = "";
		Hashtable ht = new Hashtable();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		boolean flag = false;
		JSONObject resultJsonInt = new JSONObject();
		StrUtils strUtil = new StrUtils();
		HttpSession session = request.getSession();
		List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		fileLocation = DbBean.COMPANY_USER_PATH + "/" + plant;
		filetempLocation = DbBean.COMPANY_USER_PATH + "/temp" + "/" + plant;
		boolean imageSizeflg = false;
		if (!isMultipart) {
			System.out.println("File Not Uploaded");
		} else {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("USER_ID")) {
						userID = item.getString();
					} else if (item.getFieldName().equalsIgnoreCase("USER_ID1")) {
						USER_ID1 = item.getString();
					}
				} else if (!item.isFormField() && (item.getName().length() > 0)) {

					String fileName = item.getName();
					long size = item.getSize();
					size = size / 1024;
					System.out.println("size of the Image imported :::" + size);
					// checking image size for 2MB
					
					String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
					System.out.println("Extensions:::::::" + extension);
					if (!imageFormatsList.contains(extension)) {
						result = "<font color=\"red\"> Image extension not valid </font>";
						imageSizeflg = true;
					}
					
					if (size > 2048) // condtn checking Image size
					{
						result = "<font color=\"red\">  User Image size is more than 2 MB </font>";
						imageSizeflg = true;
					} else {
						File path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" + strUtil.RemoveSlash(USER_ID1) + ".JPEG");
						File currentFile = new File(path + "/" + strUtil.RemoveSlash(USER_ID1) + ".JPEG");
						strpath = path + "/" + fileName;
						// catlogpath = uploadedFile.getAbsolutePath();
						userImagePath = currentFile.getAbsolutePath();
						File filePath = new File(userImagePath);
						if (filePath.exists()) {
							filePath.delete();
						}
						if (!imageSizeflg && !currentFile.exists())
							item.write(currentFile);
						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "/" + strUtil.RemoveSlash(userName) + ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
					}
				
						}
					}
		}
					Hashtable htCatalog = new Hashtable();						
					session.setAttribute("HTCRATLOG", htCatalog);
					if (imageSizeflg) {
						resultJsonInt.put("Message",result);
					} else {				
							Hashtable htlog = new Hashtable();
							userImagePath = userImagePath.replace('\\', '/');
								htCatalog.put("IMAGEPATH", userImagePath);
								htCatalog.put("USER_ID", userName);	
								ut = DbBean.getUserTranaction();
								ut.begin();
								Hashtable hupdate = new Hashtable();
								hupdate.put("IMAGEPATH", userImagePath);						
								Hashtable hcondition = new Hashtable();
								hcondition.put("USER_ID", userID);
								hcondition.put("DEPT", plant);
								CatalogDAO dao = new CatalogDAO();
								flag = dao.saveUserImage(htCatalog,hupdate,hcondition);						
								if (flag) {
									DbBean.CommitTran(ut);
									result = "<font color=\"green\">User Image Uploaded successfully</font>";
									resultJsonInt.put("Message",result);
								} else {
									DbBean.RollbackTran(ut);
									result = "<font color=\"red\"> Failed to Upload Image</font>";
									resultJsonInt.put("Message",result);
								}
					}
					return resultJsonInt;
	}
	
	private JSONObject DeleteUserImage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String catalogspercompany = "",detailHeading="",userID="",USER_ID1="",userImagePath="",
			fileLocation="",filetempLocation="",result="",strpath;
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			UserTransaction ut = null;
			selectBean sb = new selectBean();
			int iIndex = 1;		
			String xmlStr ="";
			Hashtable ht = new Hashtable();
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);	
			boolean flag = false;
			JSONObject resultJsonInt = new JSONObject();	
			StrUtils strUtil = new StrUtils();
			HttpSession session = request.getSession();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			fileLocation = DbBean.COMPANY_USER_PATH + "/"+ plant;
			filetempLocation = DbBean.COMPANY_USER_PATH + "/temp" + "/" + plant;		
			boolean imageSizeflg = false;
			USER_ID1 = request.getParameter("USER_ID1");				
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);					
			File path = new File(fileLocation);
			if (!path.exists()) {
				boolean status = path.mkdirs();
			}
			File uploadedFile = new File(path + "/" + strUtil.RemoveSlash(USER_ID1)+ ".JPEG");
			File currentFile = new File(path + "/" + strUtil.RemoveSlash(USER_ID1)+ ".JPEG");

			//catlogpath = uploadedFile.getAbsolutePath();
			userImagePath = currentFile.getAbsolutePath();					
			File filePath = new File(userImagePath);					
			if (filePath.exists()) {
				filePath.delete();
			}	
			File tempPath = new File(filetempLocation);
			if (tempPath.exists()) {
				File tempUploadedfile = new File(tempPath + "/"+ strUtil.RemoveSlash(USER_ID1) + ".JPEG");
				if (tempUploadedfile.exists()) {
					tempUploadedfile.delete();
				}
			}						
			Hashtable htCatalog = new Hashtable();						
			session.setAttribute("HTCRATLOG", htCatalog);									
			Hashtable htlog = new Hashtable();
			userImagePath = userImagePath.replace('\\', '/');
			htCatalog.put("IMAGEPATH", "");
			htCatalog.put("USER_ID", USER_ID1);	
			ut = DbBean.getUserTranaction();
			ut.begin();
			Hashtable hupdate = new Hashtable();
			hupdate.put("IMAGEPATH", "");						
			Hashtable hcondition = new Hashtable();
			hcondition.put("USER_ID", USER_ID1);
			hcondition.put("DEPT", plant);
			CatalogDAO dao = new CatalogDAO();
			flag = dao.saveUserImage(htCatalog,hupdate,hcondition);						
			if (flag) {
				DbBean.CommitTran(ut);
				result = "<font color=\"green\">User Image deleted successfully</font>";
				resultJsonInt.put("Message",result);
			} else {
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Failed to Delete Image</font>";
				resultJsonInt.put("Message",result);
			}
			return resultJsonInt;
	}
	
	private void saveCompressedImage(BufferedImage image, String formatName, File outputFile) throws IOException {
        // Create an ImageWriter for the specified format
        Iterator<javax.imageio.ImageWriter> writers = javax.imageio.ImageIO.getImageWritersByFormatName(formatName);
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for format: " + formatName);
        }
        javax.imageio.ImageWriter writer = writers.next();

        // Create ImageWriteParam to compress the image
        javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.25f); // Set compression quality (0.0 - 1.0)
        }

        // Create the output stream and write the image
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
        		javax.imageio.stream.ImageOutputStream ios = javax.imageio.ImageIO.createImageOutputStream(fos)) {
            writer.setOutput(ios);
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }
	
}