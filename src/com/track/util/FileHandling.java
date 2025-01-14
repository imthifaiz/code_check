package com.track.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;

public class FileHandling {

	public void fileUpload(List<FileItem> itemfiles,String filelocation) throws Exception
	{
		for(FileItem itemFile:itemfiles)
		{
	         String fileName = StrUtils.fString(itemFile.getName()).trim();
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);							
				File path = new File(filelocation);
				if (!path.exists()) {
					path.mkdirs();
				}							
				File uploadedFile = new File(path + "/" +fileName);
				if (uploadedFile.exists()) {
					uploadedFile.delete();
				}							
				itemFile.write(uploadedFile);							
		}
		
	}
	public void fileDelete(String filelocation) throws IOException
	{
		File path = new File(filelocation);
		if (path.exists()) {
			FileUtils.cleanDirectory(path);
		}
		
	}
	
	public void fileDownload(String filePath,String fileName,String fileType,HttpServletResponse response)
	{
		File file = new File(filePath+"/"+fileName);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			byte[] writfile = getBytesFromFile(file);
			int bufferSize = 2048;
			int fileLength = writfile.length;
			response.setContentType(fileType);
			response.setBufferSize(bufferSize);
			response.setContentLength(fileLength);
			response.setHeader("Content-Disposition",
					"attachment; filename="+fileName);
			response.setHeader("Pragma", "public");
			if (fileLength > 0) {
				out.write(writfile, 0, fileLength);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
