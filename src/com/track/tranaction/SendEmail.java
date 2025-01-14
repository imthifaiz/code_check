package com.track.tranaction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;


public class SendEmail {
	public SendEmail()
	{
		
	}
	private class MyAuthenticator extends javax.mail.Authenticator {
		String User;
		String Password;

		public MyAuthenticator(String user, String password) {
			User = user;
			Password = password;
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new javax.mail.PasswordAuthentication(User, Password);
		}
	}
	public String sendTOMailPdf(String from, String to,String cc,String bcc, String subject, String body,java.util.List<String> fileNames) throws MessagingException{
		Properties props = System.getProperties();
        //track
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "true");
		Date date = new Date();
		MyAuthenticator authenticator = new MyAuthenticator("noreply@u-clo.com", "!2A@reply@Email!");
	
       
		Session mailSession = Session.getInstance(props, authenticator);
       	MimeMessage mailMessage = new MimeMessage(mailSession);
		InternetAddress fromAddr = null;
		InternetAddress[] toAddr = null;
                InternetAddress[] ccAddr = null;
	        InternetAddress[] bccAddr = null;
	        String[] recipientList = null;
	        InternetAddress[] recipientAddress = null;
		try {
			fromAddr = new InternetAddress(from);
		    if(to.length()>0){
		    	if(to.contains(";")){
		    	recipientList = to.split(";");
		    	recipientAddress = new InternetAddress[recipientList.length];
		    	int counter = 0;
		    	for (String recipient : recipientList) {
		    		recipientAddress[counter] = new InternetAddress(recipient.trim());
		    		counter++;
		    	}
		    	} else 
		    		toAddr =InternetAddress.parse(to);
                    }
		    if(cc.length()>0){
		        ccAddr =InternetAddress.parse(cc);
		    }
		    if(bcc.length()>0){
		        bccAddr =  InternetAddress.parse(bcc);
		    }
                    
		       
		} catch (AddressException ex) {
			ex.printStackTrace();
		}
		try {
			mailMessage.setFrom(fromAddr);
			mailMessage.setSender(fromAddr);
			mailMessage.setSentDate(date);
                        if(to.length()>0){
                        	if(to.contains(";"))
                        		mailMessage.setRecipients(RecipientType.TO, recipientAddress);
                        	else
                        		mailMessage.setRecipients(RecipientType.TO, toAddr);
                        }
                        if(cc.length()>0){
                            mailMessage.setRecipients(RecipientType.CC, ccAddr);
                        }
                        if(bcc.length()>0){
                            mailMessage.setRecipients(RecipientType.BCC, bccAddr);
                        }
		      
			mailMessage.setSubject(subject);
			//mailMessage.setText(body);
//			System.out.println("fileNames : " + fileNames);
                     if(!fileNames.isEmpty()){
                    	 setEmbeddedPdf(mailMessage,fileNames,body);
                     }else{
                          setHTMLContent(mailMessage,body);
                     }
                        mailMessage.saveChanges();
			Transport.send(mailMessage);
			
			//	Delete files
			if(!fileNames.isEmpty()){
				for(Object fileName : fileNames.toArray()) {
	            	try {
	            		new File(fileName.toString()).delete();
	            	}catch(Exception e) {
	            		//	Ignore exception if it comes in attachment file deletion
	            	}
	        	}
			}
			
			System.out.println("Mail sent successfully");
			return "Sent";
		} catch (SendFailedException e) {
		    System.out.println("Exception :"+e.getMessage());
                    throw e;
           
			// e.printStackTrace();
			
		}
	}
	public String sendTOMailPdf(String from, String to,String cc,String bcc, String subject, String body,String filename)
			throws MessagingException {
		
		java.util.List<String> fileNames = new ArrayList<>();
	   	 if (filename != null) {
	    	 fileNames.add(filename);
		 }
		return sendTOMailPdf(from, to, cc, bcc, subject, body, fileNames);
	}
	
	public String sendTOMailPdfpayroll(String from, String to,String cc,String bcc, String subject, String body,MimeMultipart attachment)
			throws MessagingException {
		Properties props = System.getProperties();
        //track
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "true");
		Date date = new Date();
		MyAuthenticator authenticator = new MyAuthenticator("noreply@u-clo.com", "!2A@reply@Email!");
	
       
		Session mailSession = Session.getInstance(props, authenticator);
       	MimeMessage mailMessage = new MimeMessage(mailSession);
		InternetAddress fromAddr = null;
		InternetAddress[] toAddr = null;
                InternetAddress[] ccAddr = null;
	        InternetAddress[] bccAddr = null;
	    
		try {
			fromAddr = new InternetAddress(from);
		    if(to.length()>0){
			toAddr =InternetAddress.parse(to);
                    }
		    if(cc.length()>0){
		        ccAddr =InternetAddress.parse(cc);
		    }
		    if(bcc.length()>0){
		        bccAddr =  InternetAddress.parse(bcc);
		    }
                    
		       
		} catch (AddressException ex) {
			ex.printStackTrace();
		}
		try {
			mailMessage.setFrom(fromAddr);
			mailMessage.setSender(fromAddr);
			mailMessage.setSentDate(date);
                        if(to.length()>0){
                            mailMessage.setRecipients(RecipientType.TO, toAddr);
                        }
                        if(cc.length()>0){
                            mailMessage.setRecipients(RecipientType.CC, ccAddr);
                        }
                        if(bcc.length()>0){
                            mailMessage.setRecipients(RecipientType.BCC, bccAddr);
                        }
		      
			mailMessage.setSubject(subject);
			mailMessage.setDataHandler(new DataHandler(new HTMLDataSource(body)));
			mailMessage.setContent(attachment);
            mailMessage.saveChanges();
			Transport.send(mailMessage);
			
			System.out.print("Mail sent successfully");
			return "Sent";
		} catch (SendFailedException e) {
		    System.out.println("Exception :"+e.getMessage());
                    throw e;
           
			// e.printStackTrace();
			
		}

	}
	
	public void sendTOMail(String from, String to,String cc,String bcc, String subject, String body,String filename)
			throws MessagingException {
		Properties props = System.getProperties();
        //track
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "true");
		Date date = new Date();
		MyAuthenticator authenticator = new MyAuthenticator("noreply@u-clo.com", "!2A@reply@Email!");
       
		Session mailSession = Session.getInstance(props, authenticator);
       	MimeMessage mailMessage = new MimeMessage(mailSession);
		InternetAddress fromAddr = null;
		InternetAddress[] toAddr = null;
                InternetAddress[] ccAddr = null;
	        InternetAddress[] bccAddr = null;
	    
		try {
			fromAddr = new InternetAddress(from);
		    if(to.length()>0){
			toAddr =InternetAddress.parse(to);
                    }
		    if(cc.length()>0){
		        ccAddr =InternetAddress.parse(cc);
		    }
		    if(bcc.length()>0){
		        bccAddr =  InternetAddress.parse(bcc);
		    }
                    
		       
		} catch (AddressException ex) {
			ex.printStackTrace();
		}
		try {
			mailMessage.setFrom(fromAddr);
			mailMessage.setSender(fromAddr);
			mailMessage.setSentDate(date);
                        if(to.length()>0){
                            mailMessage.setRecipients(RecipientType.TO, toAddr);
                        }
                        if(cc.length()>0){
                            mailMessage.setRecipients(RecipientType.CC, ccAddr);
                        }
                        if(bcc.length()>0){
                            mailMessage.setRecipients(RecipientType.BCC, bccAddr);
                        }
		      
			mailMessage.setSubject(subject);
			//mailMessage.setText(body);
                     if(filename.length()>0){
                          setEmbeddedImg(mailMessage,filename,body);
                     }else{
                          setHTMLContent(mailMessage,body);
                     }
                        mailMessage.saveChanges();
			Transport.send(mailMessage);

			System.out.print("Mail sent successfully");
		} catch (SendFailedException e) {
		    System.out.println("Exception :"+e.getMessage());
                    throw e;
			// e.printStackTrace();
			
		}

	}
     
 
     // Set a file as an attachment.  Uses JAF FileDataSource.
     public static void setEmbeddedImg(Message msg, String filename ,String htmlmsg)
              throws MessagingException {
             
    
      // FileDataSource fileDs = new FileDataSource("D:/ShopNScan/temp/demo1/TEST.JPG"); 
       MimeBodyPart imageBodypart = new MimeBodyPart();       
       if(filename.length()>0){
         FileDataSource fileDs = new FileDataSource(filename);     
         
         imageBodypart.setDataHandler(new DataHandler(fileDs));    
         imageBodypart.setHeader("Content-ID", "<myimg>");      
         imageBodypart.setDisposition(MimeBodyPart.INLINE);     
       }
         // Handle text      
         String body = htmlmsg;//"<html><body>Message<img src=\"cid:myimg\" width=\"600\" height=\"600\" alt=\"myimg\" />Text After Image</body></html>";     
          if(filename.length()>0){
              //    body=body + " <img src=\"cid:myimg\" width=\"600\" height=\"600\" alt=\"myimg\" /></body></html>";
             body=body + " <img src=\"cid:myimg\" /></body></html>";
              }else{
             body= body +" </body></html>";
          }
         System.out.println(htmlmsg);
         MimeBodyPart textPart = new MimeBodyPart();       
         textPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");       
         textPart.setContent(body, "text/html; charset=utf-8");         
         MimeMultipart multipart = new MimeMultipart("mixed");     
         multipart.addBodyPart(textPart);  
         if(filename.length()>0){
         
         multipart.addBodyPart(imageBodypart);        
         }
         msg.setContent(multipart);     
              
     }
     
     public static void setEmbeddedPdf(Message msg, java.util.List<String> fileNames ,String htmlmsg)
             throws MessagingException {
            
   
     // FileDataSource fileDs = new FileDataSource("D:/ShopNScan/temp/demo1/TEST.JPG"); 
   	 //TODO : Handle multiple files
        // Handle text      
        String body = htmlmsg;//"<html><body>Message<img src=\"cid:myimg\" width=\"600\" height=\"600\" alt=\"myimg\" />Text After Image</body></html>";     
         if(!fileNames.isEmpty()){
             //    body=body + " <img src=\"cid:myimg\" width=\"600\" height=\"600\" alt=\"myimg\" /></body></html>";
            body=body + " <img src=\"cid:myimg\" /></body></html>";
             }else{
            body= body +" </body></html>";
         }
//        System.out.println(htmlmsg);
        MimeBodyPart textPart = new MimeBodyPart();       
        textPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");       
        textPart.setContent(body, "text/html; charset=utf-8");         
        MimeMultipart multipart = new MimeMultipart("mixed");     
        multipart.addBodyPart(textPart);  
        if(!fileNames.isEmpty()){
        	for(Object fileName : fileNames.toArray()) {
//        		System.out.println("fileName : " + fileName);
        		if (fileName == null || "".equals(fileName.toString().trim())) {
        			throw new MessagingException("Could not read Attachment file : " + fileName);
        		}
        		File attachment = new File(fileName.toString());
        		if(!attachment.exists())
        		{
        			throw new MessagingException("Could not read Attachment file : " + fileName);
        		}
            	MimeBodyPart attachPart = new MimeBodyPart();  
                FileDataSource fileDs = new FileDataSource(fileName.toString());     
                attachPart.setDataHandler(new DataHandler(fileDs));    
                attachPart.setFileName(attachment.getName());    
                multipart.addBodyPart(attachPart);        
        	}
        }
        msg.setContent(multipart);     
             
    }
     // Set a file as an attachment.  Uses JAF FileDataSource.
     public static void setEmbeddedPdf(Message msg, String filename ,String htmlmsg)
              throws MessagingException {
             
    	 java.util.List<String> fileNames = new ArrayList<>();
    	 if (filename != null) {
        	 fileNames.add(filename);
    	 }
    	 setEmbeddedPdf(msg, fileNames, htmlmsg);
     }
   
    public static void setHTMLContent(Message msg,String htmlmsg) throws MessagingException {
        msg.setDataHandler(new DataHandler(new HTMLDataSource(htmlmsg)));
    }
    public static boolean validateEmail(String email)
    {
    	boolean valid=true;        	
    	try {
			new InternetAddress(email).validate();
		} catch (AddressException ex) {
			System.out.print("Error"+ex.getMessage());
			return false;
		}
    	return valid;
    }
  
    static class HTMLDataSource implements DataSource {
        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        
        // Return html string in an InputStream.
        // A new stream must be returned each time.
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("Null HTML");
            return new ByteArrayInputStream(html.getBytes());
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        public String getContentType() {
            return "text/html";
        }

        public String getName() {
            return "JAF text/html dataSource to send e-mail only";
        }
    }

}
