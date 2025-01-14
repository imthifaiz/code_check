package com.track.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReadFileServlet
 */
public class ReadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadFileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fileLocation="";
		 fileLocation = request.getParameter("fileLocation");
		// File file = new File("D:/ShopNScan/test/Product2.JPEG");
		 File file = new File(fileLocation);
		    ServletOutputStream out = null;
		try {			
			out = response.getOutputStream();
            byte[] writfile = getBytesFromFile(file);
            int bufferSize = 4096;
            int fileLength = writfile.length;
       //     BufferedInputStream in1 = new BufferedInputStream(new FileInputStream("D:/ShopNScan/test/Product2.JPEG"));  
            response.setBufferSize(bufferSize);
            response.setContentLength(fileLength);
            response.setContentType("image/jpeg");
            out = response.getOutputStream();
            if (fileLength > 0) {
                out.write(writfile, 0, fileLength);
 //   byte[] bytess = new byte[in1.available()];
//
//            in1.read(bytess);
//            in1.close();

         // Write image contents to response.
                out.flush();
                out.close();
      
        }
            
		} catch (FileNotFoundException e) {
			try {
                throw new Exception("Unable to Load Image");
            } catch (Exception f) {
                // TODO
            }
        }catch (Exception e) {

            try {
                throw new Exception("Unable to Load Image");
            } catch (Exception f) {
                // TODO
            }
        } finally {
			try {

			} catch (Exception e) {

			}
			// TODO: handle exception
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
 System.out.println("read the bytes");	        
        is.close();
        return bytes;
}

}
