package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BannerDAO;
import com.track.dao.CatalogDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.util.BannerUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class BannerServlet
 */
@WebServlet("/BannerServlet")
public class BannerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.PurchaseOrderServlet_PRINTPLANTMASTERLOG;
	String result = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BannerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		UserTransaction ut = null;

		PrintWriter out = response.getWriter();
		JSONObject objectResult = new JSONObject();
		try {
			String action = StrUtils.fString(request.getParameter("Submit"));
			

			if (action.equalsIgnoreCase("itm_img_delete")) {

				String ITEMS = (request.getParameter("ITEMM"));
				objectResult = itemImageDelete(request, response,ITEMS);
				
			} else if (action.equalsIgnoreCase("itm_img_edit")) {

				String ITEMS = (request.getParameter("ITEMM"));
				objectResult = itemImageUpdate(request, response,ITEMS);
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
			//response.sendRedirect("jsp/createCatalog.jsp?result=" + result);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
		
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
		boolean itemAddImg = false;
		
		if (isMultipart) {
			try {
				UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
				String result = "", strpath = "", catlogpath = "";
				String fileLocation = DbBean.COMPANY_BANNER_PATH + "/" + plant;
				String filetempLocation = DbBean.COMPANY_BANNER_PATH + "/temp" + "/" + plant;
				boolean imageSizeflg = false;
				
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

				
				String img="";
				String ITEM = "";
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					 if (!item.isFormField() && (item.getName().length() > 0)) {

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
							resultObj.put("message", result);
							resultJSON.put("result", resultObj);
							imageSizeflg = true;
							return resultJSON;
						}
						
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Banner Image size greater than 1 MB </font>";

							imageSizeflg = true;
							resultObj.put("message", result);
							resultJSON.put("result", resultObj);
							return resultJSON;
						}
						File path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(ITEM);
						
							uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
							img = uploadedFile.toString();
						

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
							File tempUploadedfile = new File(tempPath + "/" + strUtils.RemoveSlash(ITEMS) + ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
						
					}

				}
				ut.begin();
				String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
				catlogpath = catlogpath.replace('\\', '/');
				
		  	 	Hashtable ht = new Hashtable();
		  	 	String usertime  ="";
				int length = img.length();
				if(length>0) {
				int len = length-4;
				String new_word = img.substring(len - 2);
				char firstChar = new_word.charAt(0);
				usertime  = Character.toString(firstChar);
				}
		  	 	ht.put("PLANT", plant);
	    	 	ht.put("BANNERPATH", catlogpath);
	    	 	ht.put("BANNERURL", catlogpath);
	    	 	ht.put("BANNERLNNO", usertime);
				boolean itemImgDlt = new BannerDAO().delMst(ht);
				Hashtable htBanner = new Hashtable();
				if(length>0) {
				int len = length-4;
				String new_word = img.substring(len - 2);
				char firstChar = new_word.charAt(0);
				usertime  = Character.toString(firstChar);
				
				htBanner.put(IConstants.PLANT, plant);
				htBanner.put("BANNERPATH", catlogpath);
				htBanner.put("BANNERURL", catlogpath);
				htBanner.put("BANNERLNNO", usertime);
				htBanner.put(IConstants.CREATED_BY, userName);
				 itemAddImg =  new BannerUtil().insertMst(htBanner);
				
				}
				//imti end for addiional image in catlogmst
				
				if (itemAddImg) {
					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Banner image changed successfully</font>";
					resultObj.put("product", ITEM);
					resultObj.put("message", result);
				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to change Banner image </font>";
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
			String fileLocation = DbBean.COMPANY_BANNER_PATH + "/" + plant;
			String filetempLocation = DbBean.COMPANY_BANNER_PATH + "/temp" + "/" + plant;
			boolean imageSizeflg = false;

			plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();

			String ITEM = "";
			ITEM = request.getParameter("ITEM");

			File path = new File(fileLocation);
			File uploadedFile = new File(ITEMS);
			String img="";
				uploadedFile = new File(path + "/" + strUtils.fString(ITEMS) + ".JPEG");
				 img = uploadedFile.toString();

			// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
		

			ut.begin();
			String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(ITEM);
			catlogpath = "";
			
			Hashtable ht = new Hashtable();
			
			String usertime  ="";
			boolean itemAddImgDlt = false;
			int length = img.length();
			if(length>0) {
			int len = length-4;
			String new_word = img.substring(len - 2);
			char firstChar = new_word.charAt(0);
			usertime  = Character.toString(firstChar);
			
    	 	ht.put("PLANT", plant);
    	 	ht.put("BANNERLNNO", usertime);
			itemAddImgDlt = new BannerDAO().delMst(ht);
			}
			
			
			if (itemAddImgDlt) {
				DbBean.CommitTran(ut);

				result = "<font color=\"green\"> Banner image deleted successfully</font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			} else {
				DbBean.RollbackTran(ut);
				result = "<font color=\"red\"> Failed to delete Banner image </font>";
				resultObj.put("product", ITEM);
				resultObj.put("message", result);
			}
		} catch (NamingException | NotSupportedException | SystemException e) {
			e.printStackTrace();
		}
		resultJSON.put("result", resultObj);
		return resultJSON;
	}
	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

}
