package com.track.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.dao.EmailMsgDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.util.InvUtil;
import com.track.tranaction.SendEmail;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

public class InvMinMaxQtyEmailSendJob implements Runnable {
	
	public InvMinMaxQtyEmailSendJob() {
		String chk="";
	}
	
	@Override
	public void run() {
		System.out.println("InvMinMaxQtyEmailSendJob :: run()"); 
		List<Map<String,String>> plantList = null;
		SimpleDateFormat simpleDateFormat = null;
		  
		try{
			 
			   List<String> attachmentLocations = new ArrayList<>();
		       plantList = new PlantMstDAO().getPlantMstDetails("");
		       Map<String,String> plantMap = null;
		 
		       String sendTo = null;
		       String sendFrom = null;
		       String sendCC=null;
		       String sendSubject = null;
		       String sendBody = null;
		       String plant = null;
		       String authstat = null;
		       String issendMail = null;
		       String EXPTIME = null;
		       
		      for(int i = 0; i < plantList.size() ; i++){
		    	
		    	try{
		    		plantMap = (Map<String,String>)plantList.get(i);
		    		plant = plantMap.get("PLANT");
		    		String SYSTIME=new DateUtils().getDate();
		    		Calendar cal=Calendar.getInstance();
					SimpleDateFormat simpleformat = new SimpleDateFormat("HH:mm");
					SYSTIME= simpleformat.format(cal.getTime());
					
			    	authstat = plantMap.get("AUTHSTAT");
			    	if(authstat.equalsIgnoreCase("1")) {
			    	if(!plant.equalsIgnoreCase("track")) {
		    		StrUtils strUtils = new StrUtils();
		    		Map<String, String> emailMsg = new EmailMsgDAO().getEmailMsgDetails(plant, IConstants.MINMAX);
		    		sendFrom = (String)emailMsg.get(IDBConstants.EMAIL_FROM);
		    		sendTo = (String)emailMsg.get(IDBConstants.EMAIL_TO);
		    		sendSubject = (String)emailMsg.get(IDBConstants.SUBJECT);
		    		sendBody = (String)emailMsg.get(IDBConstants.BODY1);
		    		issendMail = (String)emailMsg.get(IDBConstants.ISAUTOEMAIL);
		    		EXPTIME = (String)emailMsg.get("TIME");
		    		sendCC="";
		    		if(issendMail.equalsIgnoreCase("Y")) {
		    		//if(EXPTIME.equalsIgnoreCase(SYSTIME)) {
					Hashtable ht = new Hashtable();
					boolean isdataMin = false;
					boolean isdataMax = false;
	                if(strUtils.fString(plant).length() > 0)               ht.put("a.PLANT",plant);
					ArrayList invQryList= new InvUtil().getInvListSummaryWithMinStock(ht,plant,"","",false,"","","","","ByMinQty","","");
					if (invQryList.size() > 0) {
						isdataMin=true;
						sendBody+="<br>";
						sendBody+="<div>Minimum:</div>";
						sendBody+="<div>";
						sendBody+="<html>";
						sendBody+="<body>";
						sendBody+="<table border=\"1\">";
						sendBody+="<tr>";
						sendBody+="<th bgcolor=\"cornflowerblue\">PRODUCT </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">DESCRIPTION </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">LOCATION </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">CATEGORY </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">SUB CATEGORY </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">BRAND </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">UOM </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">MIN QTY </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">INV.QTY </th>";
						sendBody+="</tr>";
						
		                 int iIndex = 0;
		                 int irow = 0;
		                 double sumprdQty = 0,stkqty=0;String lastProduct="";double lastProdStkQty=0;
		                    for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
		                                String result="";
		                                Map lineArr = (Map) invQryList.get(iCnt);
		                                String item =(String)lineArr.get("ITEM");
		                                String ITEMDESC =(String)lineArr.get("ITEMDESC");
		                                String PRDCLSID =(String)lineArr.get("PRDCLSID");
		                                String ITEMTYPE =(String)lineArr.get("ITEMTYPE");
		                                String PRD_BRAND_ID =(String)lineArr.get("PRD_BRAND_ID");
		                                String PRD_DEPT_ID =(String)lineArr.get("PRD_DEPT_ID");
		                                String INVENTORYUOM =(String)lineArr.get("INVENTORYUOM");
		                                String LOC = strUtils.fString((String)lineArr.get("LOC"));
		                                
		                                double INVMINQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVMINQTY")));
		                                double INVQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVQTY")));
		                                
		                                sendBody+="<tr>";
		                                sendBody+="<td>"+item+"</td>";
		                                sendBody+="<td>"+ITEMDESC+"</td>";
		                                sendBody+="<td>"+LOC+"</td>";
		                                sendBody+="<td>"+PRDCLSID+"</td>";
		                                sendBody+="<td>"+ITEMTYPE+"</td>";
		                                sendBody+="<td>"+PRD_BRAND_ID+"</td>";
		                                sendBody+="<td>"+INVENTORYUOM+"</td>";
		                                sendBody+="<td>"+StrUtils.addZeroes(INVMINQTY, "3")+"</td>";
		                                sendBody+="<td>"+StrUtils.addZeroes(INVQTY, "3")+"</td>";
		                                sendBody+="</tr>";
		                                
		                    }
		                    sendBody+="</table>";
		                    sendBody+="</body>";
		                    sendBody+="</html>";
		                    sendBody+="</div>";
		                               
		                }
					invQryList= new InvUtil().getInvListSummaryWithMinStock(ht,plant,"","",false,"","","","","ByMaxQty","","");
					if (invQryList.size() > 0) {
						isdataMax=true;
						sendBody+="<br>";
						sendBody+="<div>Maximum:</div>";
						sendBody+="<div>";
						sendBody+="<html>";
						sendBody+="<body>";
						sendBody+="<table border=\"1\">";
						sendBody+="<tr>";
						sendBody+="<th bgcolor=\"cornflowerblue\">PRODUCT </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">DESCRIPTION </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">LOCATION </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">CATEGORY </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">SUB CATEGORY </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">BRAND </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">UOM </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">MAX QTY </th>";
						sendBody+="<th bgcolor=\"cornflowerblue\">INV.QTY </th>";
						sendBody+="</tr>";
						
						int iIndex = 0;
						int irow = 0;
						double sumprdQty = 0,stkqty=0;String lastProduct="";double lastProdStkQty=0;
						for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
							String result="";
							Map lineArr = (Map) invQryList.get(iCnt);
							String item =(String)lineArr.get("ITEM");
							String ITEMDESC =(String)lineArr.get("ITEMDESC");
							String PRDCLSID =(String)lineArr.get("PRDCLSID");
							String ITEMTYPE =(String)lineArr.get("ITEMTYPE");
							String PRD_BRAND_ID =(String)lineArr.get("PRD_BRAND_ID");
							String PRD_DEPT_ID =(String)lineArr.get("PRD_DEPT_ID");
							String INVENTORYUOM =(String)lineArr.get("INVENTORYUOM");
							String LOC = strUtils.fString((String)lineArr.get("LOC"));
							
							double INVMAXQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVMAXQTY")));
							double INVQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVQTY")));
							
							sendBody+="<tr>";
							sendBody+="<td>"+item+"</td>";
							sendBody+="<td>"+ITEMDESC+"</td>";
							sendBody+="<td>"+LOC+"</td>";
							sendBody+="<td>"+PRDCLSID+"</td>";
							sendBody+="<td>"+ITEMTYPE+"</td>";
							sendBody+="<td>"+PRD_BRAND_ID+"</td>";
							sendBody+="<td>"+INVENTORYUOM+"</td>";
							sendBody+="<td>"+StrUtils.addZeroes(INVMAXQTY, "3")+"</td>";
							sendBody+="<td>"+StrUtils.addZeroes(INVQTY, "3")+"</td>";
							sendBody+="</tr>";
							
						}
						sendBody+="</table>";
						sendBody+="</body>";
						sendBody+="</html>";
						sendBody+="</div>";
						
					}
					
					if(isdataMin==true||isdataMax==true) {
					SendEmail sendMail=new SendEmail();
					//mLogger.info("Locations:"+attachmentLocations);
//					mLogger.info("sendCC : " + sendCC);
					String mailResp=null;
					if(issendMail.equalsIgnoreCase("Y")) {
					 mailResp=sendMail.sendTOMailPdf(sendFrom == null ? sendTo : sendFrom,sendTo,sendCC,"", sendSubject, sendBody, attachmentLocations);
					}				
					if(mailResp!=null && mailResp.equals("Sent")) {
						System.out.println("Mail Status :"+mailResp);						   
					  }
			    	//}
		    		}
		    		}
			    	}
			    	}
		    	}catch(Exception e){
			    	System.out.println("Exception Occure due to "+e.getMessage());
			    	System.out.println("Data Use in the request plant :"+plant+
			    			" email to :"+sendTo+" IsAutoEmail :"+issendMail);
				    System.out.println("InvMinMaxQtyEmailSendJob :: run()");
	                  e.printStackTrace();
				  }
		    	System.out.println(plant+" End the execution");
		    }
		   }catch(Exception e){
			    System.out.println("Exception Occure due to "+e.getMessage());
			    System.out.println("InvMinMaxQtyEmailSendJob :: run()");
			    e.printStackTrace();
		}
		
	}
}
