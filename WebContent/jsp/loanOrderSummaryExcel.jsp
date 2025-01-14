<%@ page import="com.track.db.util.*"%>
<%

java.util.List do_list = new java.util.ArrayList();
com.track.util.StrUtils strUtils     = new com.track.util.StrUtils();
com.track.util.DateUtils dateUtils = new com.track.util.DateUtils();
com.track.db.util.LoanUtil  loanUtil= new com.track.db.util.LoanUtil();
java.util.Hashtable ht = new java.util.Hashtable();


String curDate =dateUtils.getDate();

String FROM_DATE ="",TO_DATE = "", DIRTYPE ="",ITEM="",DESC="",CUST_CODE="",status ="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",xlaction="";
String chkExpirydate="",strEmpty="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="";
String PLANT =(String)session.getAttribute("PLANT");
FROM_DATE=strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE=strUtils.fString(request.getParameter("TO_DATE"));

JOBNO=strUtils.fString(request.getParameter("JOBNO"));
ITEMNO=strUtils.fString(request.getParameter("ITEM"));
DESC = strUtils.fString(request.getParameter("DESC"));

ORDERNO=strUtils.fString(request.getParameter("DONO"));
CUSTOMER=strUtils.fString(request.getParameter("CUST_NAME"));
CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE"));
status = strUtils.fString(request.getParameter("STATUS"));
DIRTYPE=strUtils.fString(request.getParameter("DIRTYPE"));
//Start code added by deen for product brand,type on 2/sep/13
PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
//End code added by deen for product brand,type on 2/sep/13 

if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);


xlaction=strUtils.fString(request.getParameter("xlAction"));

System.out.println("fdate::"+fdate);
System.out.println("tdate::"+tdate);
System.out.println("JOBNO::"+JOBNO);
System.out.println("ITEMNO::"+ITEMNO);
System.out.println("ORDERNO::"+ORDERNO);
System.out.println("CUSTOMER::"+CUSTOMER);

System.out.println("DIRTYPE::"+DIRTYPE);
System.out.println("xlaction::"+xlaction);

      
        if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
        if(strUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
        if(strUtils.fString(ORDERNO).length() > 0)        ht.put("B.ORDNO",ORDERNO);
        //if(strUtils.fString(CUSTOMER).length() > 0)        ht.put("A.CUSTNAME",CUSTOMER);
         if(strUtils.fString(CUST_CODE).length() > 0)        ht.put("A.CUSTCODE",CUST_CODE);
          if(strUtils.fString(status).length() > 0)        ht.put("A.STATUS",status);
          if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
          if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
          if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
      



if(xlaction.equalsIgnoreCase("GenerateXLSheet")){
try{
do_list=loanUtil.getLoanOrderSummary(ht,fdate,tdate,DESC,PLANT,CUSTOMER);
if(do_list.size() > 0)
{ response.setContentType("application/vnd.ms-excel");
  response.setHeader("Content-disposition", "attachment; filename=LoanOrderReport.xls");
  java.util.Vector v =  null;
 %>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

         <TH><font color="#ffffff" align="center">S/N</font></TH>
         <TH><font color="#ffffff" align="center"><b>Loan Order No</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Ref No</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Assignee Name</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Product ID</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Description</b></font></TH>
         <TH><font color="#ffffff" align="left"><b>Class</b></font></TH>
         <TH><font color="#ffffff" align="left"><b>Type</b></font></TH>
         <TH><font color="#ffffff" align="left"><b>Brand</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Order Date</b></font></TH>
          <TH><font color="#ffffff" align="center"><b>Expiry Date</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Order Qty</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Pick Qty</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Receive Qty</b></font></TH>
         <TH><font color="#ffffff" align="center"><b>Pick/Issue Status</b></font></TH>
       </tr>
  <%

  //out.println(" Loan Order Number\tRef\tAssignee Name\tProduct ID\tOrder Date\tOrd Qty\tPick Qty\tRecv QTY\tStatus");
  for(int i = 0; i<do_list.size(); i++){

  
   try{

   java.util.Map  map = (java.util.Map)do_list.get(i);
    int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
   %>
    <TR bgcolor = "<%=bgcolor%>">
    <TD align="center"><%=iIndex%></TD>
            <TD align= "center"><%=(String)map.get("Ordno")%></TD>
            <TD align= "center"><%=(String)map.get("jobNum")%></TD>
            <TD align= "center"><%=(String)map.get("custname")%></TD>
            <TD align= "center"><%=(String)map.get("item") %></TD>
            <TD align= "center"><%=(String)map.get("itemdesc") %></TD>
            <TD align= "right"><%=(String)map.get("prd_cls_id") %></TD>
            <TD align= "right"><%=(String)map.get("itemtype") %></TD>
            <TD align= "right"><%=(String)map.get("prd_brand_id") %></TD>
            <TD align= "center"><%=(String)map.get("collectionDate") %></TD>
            
            <% 
	             // ***To check and display Expiry Date in red color when it's in less then current date ***
	             chkExpirydate=(String)map.get("expiredate");
	             java.util.Date d1 =  dateUtils.parsetwoDate(curDate); 
	             java.util.Date d2 = dateUtils.parsetwoDate(chkExpirydate); 
	             if(!chkExpirydate.equals("")){
	            	 if(dateUtils.compareTo(d1,d2) > 0 ) {%>
	            		<TD align= "center"><font color="red"><%=(String)map.get("expiredate") %></font></TD>
	              	 <% }else{%>
	                	<TD align= "center"><%=(String)map.get("expiredate") %></TD>
	             	 <%}
	          
               }else{%><TD align= "center"><%=strEmpty %></TD> 
           <%}%>
             
            <TD align= "right"><%=(String)map.get("qtyor") %></TD>
            <TD align= "right"><%=(String)map.get("qtyis") %></TD>
             <TD align= "right"><%=(String)map.get("qtyrc") %></TD>
	    <TD align= "center"><%=(String)map.get("lnstat") %></TD> 
            </TR>
   <%
  // out.println((String)map.get("Ordno")+ "\t"+ (String)map.get("jobNum")+ "\t"+ (String)map.get("custname")+"\t"+(String)map.get("item")+"\t"+ (String)map.get("collectionDate")+"\t"+ (String)map.get("qtyor")+"\t"+ (String)map.get("qtyis")+"\t"+ (String)map.get("qtyrc")+"\t" + (String)map.get("lnstat"));
   
  }catch(Exception ee){System.out.println("######################## Loan OrderExcel ################ :"+ee);}
  }%></TABLE>
  <%
}else if(do_list.size() < 1){
response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-disposition", "attachment; filename=LoanOrderReport.xls");

out.println("No Records Found To List");
}

}catch(Exception e){}
}


%>
