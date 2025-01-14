package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CustomerAttachDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.HrEmpType;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/CustomerAttachmentServlet")
public class CustomerAttachmentServlet extends HttpServlet implements IMLogger  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		CustomerAttachDAO customerAttachDAO= new CustomerAttachDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		JSONObject objectResult = new JSONObject();
		
		if(action.equalsIgnoreCase("downloadAttachmentById")) {
			
			System.out.println("Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			List customerAttachment = null;
			try {
				customerAttachment = customerAttachDAO.getcustomerAttachById(plant, String.valueOf(ID));
				Map customerAttach=(Map)customerAttachment.get(0);
				String filePath=(String) customerAttach.get("FilePath");
				String fileType=(String) customerAttach.get("FileType");
				String fileName=(String) customerAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		
		if(action.equalsIgnoreCase("removeAttachmentById")) {

			System.out.println("Remove Attachments by ID");
			String ID=request.getParameter("removeid");
			try {
				customerAttachDAO.deletecustomerAttachByPrimId(plant, ID);	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		
		}
		if(action.equalsIgnoreCase("add_attachments")) {
			objectResult = customerAttachmentsUpdate(request, response);
		}
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		
		
	}
	
	public JSONObject customerAttachmentsUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultObj = new JSONObject();
		JSONObject resultJSON = new JSONObject();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		StrUtils strUtils = new StrUtils();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				UserTransaction ut = com.track.gates.DbBean.getUserTranaction();
				String result = "", strpath = "", catlogpath = "";
				List<Hashtable<String,String>> customerAttachmentList = null;
				List<Hashtable<String,String>> customerAttachmentInfoList = null;
				DateUtils dateutils = new DateUtils();
				CustomerAttachDAO customerAttachDAO = new CustomerAttachDAO();
				customerAttachmentList = new ArrayList<Hashtable<String,String>>();
				customerAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				boolean imageSizeflg = false;
				List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();

			
				String sCustCode = "";
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName().equalsIgnoreCase("CUST_CODE")) {
							sCustCode = StrUtils.fString(item.getString());
						}
						
						
					} else if (!item.isFormField() && (item.getName().length() > 0)) {
						if(item.getFieldName().equalsIgnoreCase("file")){
							String fileLocationATT = "C:/ATTACHMENTS/CUSTOMER" + "/"+ sCustCode;
							String filetempLocationATT = "C:/ATTACHMENTS/CUSTOMER" + "/temp" + "/"+ sCustCode;
							String fileName = StrUtils.fString(item.getName()).trim();
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
							
							item.write(uploadedFile);
							
							// delete temp uploaded file
							File tempPath = new File(filetempLocationATT);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/"+ fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							Hashtable customerAttachment = new Hashtable<String, String>();
							customerAttachment.put("PLANT", plant);
							customerAttachment.put("FILETYPE", item.getContentType());
							customerAttachment.put("FILENAME", fileName);
							customerAttachment.put("FILESIZE", String.valueOf(item.getSize()));
							customerAttachment.put("FILEPATH", fileLocationATT);
							customerAttachment.put("CRAT",dateutils.getDateTime());
							customerAttachment.put("CRBY",username);
							customerAttachment.put("UPAT",dateutils.getDateTime());
							customerAttachmentList.add(customerAttachment);
						}
					}

				}
				ut.begin();
				
				

				if (sCustCode.equals("")) {
					throw new Exception("");
				}
				
				int attchSize = customerAttachmentList.size();
				  for(int i =0 ; i < attchSize ; i++){
						Hashtable customerAttachmentat = new Hashtable<String, String>();
						customerAttachmentat = customerAttachmentList.get(i);
						customerAttachmentat.put("CUSTNO", sCustCode);
						customerAttachmentInfoList.add(customerAttachmentat);
				  }
				  boolean itemInserted = customerAttachDAO.addcustomerAttachments(customerAttachmentInfoList, plant);
				if (itemInserted) {
					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Customer Attachments successfully</font>";
					resultObj.put("customer", sCustCode);
					resultObj.put("message", result);
				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to upload Attachments </font>";
					resultObj.put("customer", sCustCode);
					resultObj.put("message", result);
				}

			} catch (Exception e) {

			}
			resultJSON.put("result", resultObj);
		}
		return resultJSON;
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
