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
import com.track.dao.MovHisDAO;
import com.track.dao.EstimateAttachDAO;
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

@WebServlet("/EstimateAttachmentServlet")
public class EstimateAttachmentServlet extends HttpServlet implements IMLogger  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		EstimateAttachDAO estimateAttachDAO= new EstimateAttachDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		DateUtils dateutils = new DateUtils();
		JSONObject objectResult = new JSONObject();
		
		if(action.equalsIgnoreCase("downloadAttachmentById")) {
			
			System.out.println("Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			List salesAttachment = null;
			try {
				salesAttachment = estimateAttachDAO.getsalesAttachById(plant, String.valueOf(ID));
				Map salesAttach=(Map)salesAttachment.get(0);
				String filePath=(String) salesAttach.get("FilePath");
				String fileType=(String) salesAttach.get("FileType");
				String fileName=(String) salesAttach.get("FileName");
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
				estimateAttachDAO.deletesalesAttachByPrimId(plant, ID);	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		
		}
		if(action.equalsIgnoreCase("add_attachments")) {
			objectResult = salesAttachmentsUpdate(request, response);
		}
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = StrUtils.fString(req.getParameter("CMD")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		
		
	}
	
	public JSONObject salesAttachmentsUpdate(HttpServletRequest request,
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
				List<Hashtable<String,String>> salesAttachmentList = null;
				List<Hashtable<String,String>> salesAttachmentInfoList = null;
				DateUtils dateutils = new DateUtils();
				EstimateAttachDAO estimateAttachDAO = new EstimateAttachDAO();
				salesAttachmentList = new ArrayList<Hashtable<String,String>>();
				salesAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
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

						if (item.getFieldName().equalsIgnoreCase("ESTNO")) {
							sCustCode = StrUtils.fString(item.getString());
						}
						
						
					} else if (!item.isFormField() && (item.getName().length() > 0)) {
						if(item.getFieldName().equalsIgnoreCase("file")){
							String fileLocationATT = "C:/ATTACHMENTS/ESTIMATE" + "/"+ sCustCode;
							String filetempLocationATT = "C:/ATTACHMENTS/ESTIMATE" + "/temp" + "/"+ sCustCode;
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
							Hashtable salesAttachment = new Hashtable<String, String>();
							salesAttachment.put("PLANT", plant);
							salesAttachment.put("FILETYPE", item.getContentType());
							salesAttachment.put("FILENAME", fileName);
							salesAttachment.put("FILESIZE", String.valueOf(item.getSize()));
							salesAttachment.put("FILEPATH", fileLocationATT);
							salesAttachment.put("CRAT",dateutils.getDateTime());
							salesAttachment.put("CRBY",username);
							salesAttachment.put("UPAT",dateutils.getDateTime());
							salesAttachmentList.add(salesAttachment);
						}
					}

				}
				ut.begin();
				
				

				if (sCustCode.equals("")) {
					throw new Exception("");
				}
				
				int attchSize = salesAttachmentList.size();
				  for(int i =0 ; i < attchSize ; i++){
						Hashtable salesAttachmentat = new Hashtable<String, String>();
						salesAttachmentat = salesAttachmentList.get(i);
						salesAttachmentat.put("ESTNO", sCustCode);
						salesAttachmentInfoList.add(salesAttachmentat);
				  }
				  boolean itemInserted = estimateAttachDAO.addsalesAttachments(salesAttachmentInfoList, plant);
				if (itemInserted) {
					DbBean.CommitTran(ut);

					result = "<font color=\"green\"> Estimate Attachments successfully</font>";
					resultObj.put("estimate", sCustCode);
					resultObj.put("message", result);
				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to upload Attachments </font>";
					resultObj.put("estimate", sCustCode);
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
