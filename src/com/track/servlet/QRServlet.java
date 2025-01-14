package com.track.servlet;

   import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

import com.track.gates.DbBean;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType; 
   public class QRServlet extends HttpServlet {  
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {     
   String qrtext = request.getParameter("qrtext"); 
   ByteArrayOutputStream out = QRCode.from(qrtext).to(ImageType.PNG).withSize(150, 150).stream(); 
   response.setContentType("image/png");
   response.setContentLength(out.size());   
   OutputStream outStream = response.getOutputStream();
   outStream.write(out.toByteArray());  
   outStream.flush();    
   outStream.close();   
   } 
       protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
               // TODO Auto-generated method stub
               doGet(request, response);
       }
       
       public String getQRImagePath(String plant,String productId,String qrtext) throws IOException{
           String filetempLocation="";
           ByteArrayOutputStream out = QRCode.from(qrtext).to(ImageType.PNG).stream(); 
           try { 
               String dirPath =DbBean.COMPANY_CATALOG_PATH + "/temp" + "/" + plant;
               filetempLocation = dirPath+"/"+productId+".JPG";
               File path = new File(dirPath);
               if (!path.exists()) {
                       boolean status = path.mkdirs();
               }
           FileOutputStream fout = new FileOutputStream(new File(filetempLocation)); 
           fout.write(out.toByteArray());  
           fout.flush();    
           fout.close();  
           } catch (FileNotFoundException e) {          
           // Do Logging  
           } catch (IOException e) { 
           // Do Logging  
           }  
           return filetempLocation;
       }
   }