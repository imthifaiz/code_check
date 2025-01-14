/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.track.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.MLoggerConstant;
import com.track.constants.PageConstant;
import com.track.gates.DbBean;
import com.track.util.IMLogger;
import com.track.util.MLogger;

/**
 * 
 * @author Farid
 */
public class FileDownloaderServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.FileDownloaderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.FileDownloaderServlet_PRINTPLANTMASTERINFO;
	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */

	String displayMessage = "";

	String track10_USER_MANUAL = DbBean.UTILITY_DOWNLOAD_PATH+"\\FR6000 Manual.pdf";
	String FR6000_USER_MANUAL = DbBean.UTILITY_DOWNLOAD_PATH+"\\FR6000 Manual.pdf";
	String LS2280_USER_MANUAL = DbBean.UTILITY_DOWNLOAD_PATH+"\\LS2208 Manual.pdf";
	
	//01-08-2011 by farid
	String LS4278_USER_MANUAL =DbBean.UTILITY_DOWNLOAD_PATH+"\\LS4278 Manual.pdf";
	String AP5131_USER_MANUAL = DbBean.UTILITY_DOWNLOAD_PATH+"\\AP5131 Manual.pdf";
	String GK420T_USER_MANUAL = DbBean.UTILITY_DOWNLOAD_PATH+"\\GK420T Manual.pdf";
	

	String track10_PDA_APPLICATION = DbBean.UTILITY_DOWNLOAD_PATH+"\\EzraBarcodePrinting.msi";
	String MICROSOFT_ACTIVE_SYNC = DbBean.UTILITY_DOWNLOAD_PATH+"\\FR6000 Manual.pdf";
	String track10_PRINTING_APPLICATION = DbBean.UTILITY_DOWNLOAD_PATH+"\\EzraBarcodePrinting.msi";
	String track10_BARCODE_FONT = DbBean.UTILITY_DOWNLOAD_PATH+"\\IDAutomationC128XS.ttf";

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			String action = request.getParameter("action");
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("download_fr6000")) {
				download_fr6000(request, response);
				displayMessage = "<font color=\"green\"> Usermanual FR6000 downloaded successfully!  <br><br><center>";
			} else if (action.equalsIgnoreCase("download_printing_application")) {
				download_printing_application(request, response);
			} else if (action.equalsIgnoreCase("download_barcode_font")) {
				download_barcode_font(request, response);
			} else if (action.equalsIgnoreCase("download_ls2280")) {
				download_ls2280(request, response);
			}else if (action.equalsIgnoreCase("download_ls4278")) {
					download_ls4278(request, response);
			}else if (action.equalsIgnoreCase("download_ap5131")) {
						download_ap5131(request, response);
			}else if (action.equalsIgnoreCase("download_gk420t")) {
				download_gk420t(request, response);
			} else {

				throw new Exception("donwload type not implemented");

			}

		} catch (Exception ex) {

			displayMessage = "<font color=\"red\"> " + ex.getMessage()
					+ " <br><br><center>";

			String result = "";

			result = "<font color=\"red\"> "
					+ displayMessage
					+ "<br><br><center>"
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "
					+ "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='"
					+ PageConstant.CancelButton + "'\">";
			result = "Utilities <br><h3>" + result;

			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");

		}

	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);

	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);

	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	public String getServletInfo() {
		return "Short description";
	}

	public void download_fr6000(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File file = new File(FR6000_USER_MANUAL);
		ServletOutputStream out = null;

		try {
			out = response.getOutputStream();
			byte[] writfile = getBytesFromFile(file);
			int bufferSize = 2048;
			int fileLength = writfile.length;
			response.setContentType("application/pdf");
			response.setBufferSize(bufferSize);
			response.setContentLength(fileLength);
			out = response.getOutputStream();
			// without this it will open new window for pdf
			response.setHeader("Content-Disposition",
					"attachment; filename=FR6000 Manual.pdf");
			response.setHeader("Pragma", "public");
			if (fileLength > 0) {
				out.write(writfile, 0, fileLength);
			}

		} catch (FileNotFoundException e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processFR6000() ", e);

			throw new Exception("Unable to download Manual FR6000!");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processFR6000() ", e);
			throw new Exception("Unable to download Manual FR6000!");
		} finally {
			try {

			} catch (Exception e) {

			}
		}

	}

	public void download_ls2280(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File file = new File(LS2280_USER_MANUAL);
		ServletOutputStream out = null;

		try {
			out = response.getOutputStream();
			byte[] writfile = getBytesFromFile(file);
			int bufferSize = 2048;
			int fileLength = writfile.length;
			response.setContentType("application/pdf");
			response.setBufferSize(bufferSize);
			response.setContentLength(fileLength);
			out = response.getOutputStream();
			// without this it will open new window for pdf
			response.setHeader("Content-Disposition",
					"attachment; filename=LS2208 Manual.pdf");
			response.setHeader("Pragma", "public");
			if (fileLength > 0) {
				out.write(writfile, 0, fileLength);
			}

		} catch (FileNotFoundException e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processFR6000() ", e);
			throw new Exception("Unable to download Manual FR6000!");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processFR6000() ", e);
			throw new Exception("Unable to download Manual FR6000!");
		} finally {
			try {
				// out.flush();
				// out.close();
			} catch (Exception e) {

			}
		}

	}
	
	public void download_ls4278(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File file = new File(LS4278_USER_MANUAL);
		ServletOutputStream out = null;

		try {
			out = response.getOutputStream();
			byte[] writfile = getBytesFromFile(file);
			int bufferSize = 2048;
			int fileLength = writfile.length;
			response.setContentType("application/pdf");
			response.setBufferSize(bufferSize);
			response.setContentLength(fileLength);
			out = response.getOutputStream();
			// without this it will open new window for pdf
			response.setHeader("Content-Disposition",
					"attachment; filename=LS4278 Manual.pdf");
			response.setHeader("Pragma", "public");
			if (fileLength > 0) {
				out.write(writfile, 0, fileLength);
			}

		} catch (FileNotFoundException e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processLS4278() ", e);
			throw new Exception("Unable to download Manual LS4278!");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processLS4278() ", e);
			throw new Exception("Unable to download Manual LS4278!");
		} finally {
			try {
				// out.flush();
				// out.close();
			} catch (Exception e) {

			}
		}

	}
	
	public void download_ap5131(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File file = new File(AP5131_USER_MANUAL);
		ServletOutputStream out = null;

		try {
			out = response.getOutputStream();
			byte[] writfile = getBytesFromFile(file);
			int bufferSize = 2048;
			int fileLength = writfile.length;
			response.setContentType("application/pdf");
			response.setBufferSize(bufferSize);
			response.setContentLength(fileLength);
			out = response.getOutputStream();
			// without this it will open new window for pdf
			response.setHeader("Content-Disposition",
					"attachment; filename=AP5131 Manual.pdf");
			response.setHeader("Pragma", "public");
			if (fileLength > 0) {
				out.write(writfile, 0, fileLength);
			}

		} catch (FileNotFoundException e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : download_ap5131() ", e);
			throw new Exception("Unable to download Manual AP5131!");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : download_ap5131() ", e);
			throw new Exception("Unable to download Manual AP5131!");
		} finally {
			try {
				// out.flush();
				// out.close();
			} catch (Exception e) {

			}
		}

	}
	
	public void download_gk420t(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File file = new File(GK420T_USER_MANUAL);
		ServletOutputStream out = null;

		try {
			out = response.getOutputStream();
			byte[] writfile = getBytesFromFile(file);
			int bufferSize = 2048;
			int fileLength = writfile.length;
			response.setContentType("application/pdf");
			response.setBufferSize(bufferSize);
			response.setContentLength(fileLength);
			out = response.getOutputStream();
			// without this it will open new window for pdf
			response.setHeader("Content-Disposition",
					"attachment; filename=GK420t Manual.pdf");
			response.setHeader("Pragma", "public");
			if (fileLength > 0) {
				out.write(writfile, 0, fileLength);
			}

		} catch (FileNotFoundException e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : download_gk420t() ", e);
			throw new Exception("Unable to download Manual GK420T!");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : download_gk420t() ", e);
			throw new Exception("Unable to download Manual GK420T!");
		} finally {
			try {
				// out.flush();
				// out.close();
			} catch (Exception e) {

			}
		}

	}

	public void download_printing_application(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File file = new File(track10_PRINTING_APPLICATION);
		ServletOutputStream out = null;
		try {

			out = response.getOutputStream();
			FileInputStream fileIn = new FileInputStream(file);
			response.setContentType("application/octet-stream");

			byte[] outputByte = new byte[4096];

			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ "Scan2TrackBarcodePrinting.msi");
			response.setHeader("Pragma", "public");

			while (fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);

			}

			fileIn.close();
		} catch (FileNotFoundException e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processUpload_printing_application() ",
					e);

			throw new Exception("Unable to download Printing Application!");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processUpload_printing_application() ",
					e);
			throw new Exception("Unable to download Printing Application!");
		} finally {
			try {
				// out.flush();
				// out.close();
			} catch (Exception e) {

			}
		}

	}

	public void download_barcode_font(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File file = new File(track10_BARCODE_FONT);
		ServletOutputStream out = null;
		try {

			out = response.getOutputStream();
			FileInputStream fileIn = new FileInputStream(file);
			response.setContentType("application/octet-stream");

			byte[] outputByte = new byte[4096];

			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ "IDAutomationC128XS.ttf");
			response.setHeader("Pragma", "public");

			while (fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);

			}

			fileIn.close();
		} catch (FileNotFoundException e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processUpload_printing_application() ",
					e);
			throw new Exception("Unable to download Printing Application!");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog,
					"FileDownloader() : processUpload_printing_application() ",
					e);
			throw new Exception("Unable to download Printing Application!");
		} finally {

		}

	}

	private byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		is.close();
		return bytes;
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
			
		}

	}
}
