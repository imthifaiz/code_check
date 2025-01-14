<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.dao.SalesDetailDAO"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.track.constants.TransactionConstants"%><html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'POSReport', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function onGo(){
  document.form.action="posTranMgmtReport.jsp?action=View";
  document.form.submit();

}
function ExportReport()
{
	 document.form.action="/track/PosReport?action=POS_Transaction_Excel";
	 document.form.submit();
  
}
</script>
<title>POS Transaction Management Report</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils     = new StrUtils();
    DateUtils dateutils = new DateUtils();
	SalesDetailDAO salesdao = new SalesDetailDAO();
	List salesList  = new ArrayList();
	String fieldDesc="";float price=0;
	int sNo=0; float total=0;
	String PLANT="",ITEM ="",DESC="",LOC="",TRANTYPE="",recptno="",userid="",STARTDATE="",TODATE="",action="",pGAction="";
	String html = "",PRD_CLS_ID="",PRD_TYPE_ID="",PRD_BRAND_ID= "";
	PLANT = (String)session.getAttribute("PLANT");
	DecimalFormat decformat = new DecimalFormat("#,##0.00");
	STARTDATE=strUtils.fString(request.getParameter("STARTDATE"));
	TODATE=strUtils.fString(request.getParameter("TODATE"));
	ITEM = strUtils.fString(request.getParameter("ITEM")).trim();
    DESC =strUtils.fString(request.getParameter("DESC")).trim();
	recptno = strUtils.fString(request.getParameter("recptno")).trim();
	TRANTYPE = strUtils.fString(request.getParameter("TRANTYPE")).trim();
	userid = strUtils.fString(request.getParameter("USERID")).trim();
	PRD_CLS_ID =  strUtils.fString(request.getParameter("PRD_CLS_ID"));
	PRD_TYPE_ID =  strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID =  strUtils.fString(request.getParameter("PRD_BRAND_ID"));

	action = request.getParameter("action");
	pGAction = request.getParameter("PGaction");
	//pGAction="View";
	String curDate =dateutils.getDate();
	salesdao.setmLogger(mLogger);
	if(STARTDATE.length()<0||STARTDATE==null||STARTDATE.equalsIgnoreCase(""))STARTDATE=curDate;
	/*if (STARTDATE.length()>5)
		STARTDATE    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);
	
	if (TODATE.length()>5)
		TODATE    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);*/
	if(pGAction!=null){
	if(pGAction.equalsIgnoreCase("View")){
        String extraCond ="";
        if(DESC.length()>0){
        //extraCond = " AND REPLACE(ITEMDESC,' ','') LIKE '"+ strUtils.InsertQuotes(DESC.replaceAll(" ","")) + "%' group by tranid,trantype,purchasedate ";
        	extraCond = " AND REPLACE(ITEMDESC,' ','') LIKE '"+ strUtils.InsertQuotes(DESC.replaceAll(" ","")) + "%' group by item,itemdesc,qty,trantype,tranid,purchasedate,remarks,rsncode,crby,batch order by cast(purchasedate as date) ";
        }else{
          extraCond = " group by item,itemdesc,qty,trantype,tranid,purchasedate,remarks,rsncode,crby,batch order by cast(purchasedate as date)";
        }
System.out.println("Logger"+userid);        
		salesList = salesdao.getDistinctSalesForMgmt(PLANT,STARTDATE,TODATE,ITEM,LOC,recptno,userid,TRANTYPE,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,extraCond);
	//else
		//salesList=null;
		 if(salesList.size()<=0)
	     {
	       //cntRec ="true";
	       fieldDesc="Data Not Found";
	     }
	}}
	
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="posTranReport.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="REPORTTYPE" value="POS_SALES_MGMT">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">POS Report</font></TH>
    </TR>
  </TABLE>
    <br>
  <center>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
   </table>
   </center>
  <TABLE border="0" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
   <TR><TH ALIGN="right">Start Date :&nbsp;&nbsp; </TH>
                    <TD align="left"><INPUT name="STARTDATE"  type = "TEXT" value="<%=STARTDATE%>" size="20"  MAXLENGTH=20 >&nbsp;&nbsp;<a href="javascript:show_calendar('form.STARTDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
           <TH ALIGN="right" width="15%">To Date :&nbsp;&nbsp;</TH>
          <TD><INPUT name="TODATE"  type = "TEXT" value="<%=TODATE%>" size="20"  MAXLENGTH=20 >&nbsp;&nbsp;<a href="javascript:show_calendar('form.TODATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
        
                  </TR>
                  <TR><TH ALIGN="right">Product ID :&nbsp;&nbsp; </TH>
                    <TD align="left"><INPUT name="ITEM"  type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50 >&nbsp;&nbsp;</TD>
                    <TH ALIGN="right" width="15%">Product Description :&nbsp;&nbsp; </TH>
       				 <TD><INPUT name="DESC"  type = "TEXT" value="<%=DESC%>" size="20"  MAXLENGTH=100 >&nbsp;&nbsp;</TD>
         
        
       
        </TR>
         <TR>
          <TH ALIGN="right" > Product Class ID: &nbsp;&nbsp; </TH>
          <TD align="left"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('PrdClsIdList.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          
          <TH ALIGN="right" width="15%">Product Type ID : &nbsp;&nbsp;  </TH>
          <TD align="left"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('productTypeIdList.jsp?ITEM_ID='+form.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>
                
          </TD>
         </TR>
        <TR>
         <TH ALIGN="right" > Product Brand ID : &nbsp;&nbsp; </TH>
		        <TD align="left"><INPUT name="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=20>
		       
		           <a href="#" onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form');">           
		           <img src="images/populate.gif" border="0"/>
		          </a>
		          </TD>
		          
		          <TH ALIGN="right" width="15%">&nbsp;&nbsp; User ID : &nbsp;&nbsp;</TH>
		     <TD align="left"><INPUT name="USERID"  type = "TEXT" value="<%=userid%>" size="20"  MAXLENGTH=40 >&nbsp;&nbsp;</TD>
		      <input type="hidden" name="PRD_CLS_DESC" value="">
            <input type="hidden" name="PRD_CLS_ID1" value="">
            <input type="hidden" name="PRD_TYPE_DESC" value="">
            <input type="hidden" name="PRD_TYPE_ID1" value="">
           <input type="hidden" name="PRD_BRAND_DESC" value="">
           <INPUT name="ACTIVE" type = "hidden" value="">
         </TR>
         
       
   
  </TABLE>
  <br>
 <TABLE WIDTH="70%"  border="0" cellspacing="1" cellpadding ="2" align = "center">
     <TR BGCOLOR="#000066">
     <TH><font  align="center" color="#ffffff">S/N</TH>     
    	 <TH><font  align="center" color="#ffffff">Tran ID</TH>
         <TH><font  align="center" color="#ffffff">Receipt No</TH>
          <TH><font  align="center" color="#ffffff">User ID</TH>
          <TH><font  align="center" color="#ffffff">Transaction Date</TH>
          <TH><font color="#ffffff" align="left"><b> Remarks</TH>
           <TH><font  align="center" color="#ffffff">Product ID</TH>
       <TH><font  align="center" color="#ffffff">Description</TH>
       <TH><font  align="center" color="#ffffff">Batch</TH>
        <TH><font  align="center" color="#ffffff">Qty</TH>
          <TH><font color="#ffffff" align="left"><b> Total Price</TH>
           
          
      </TR>
   	  <% int iIndex=0;  int irow = 0;String bgcolor="";Map salemap=null;
   	  if(salesList!=null&&salesList.size()>0){
   		String lastProduct = "";
   		double sumprdQty = 0 ,sumOfTotalPrice = 0;
       		for(int iCnt=0;iCnt<salesList.size();iCnt++){
       			 salemap=(Map)salesList.get(iCnt); 
       				 bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       				 
       			
       		price = Float.parseFloat(strUtils.removeFormat((String)salemap.get("price"))); 
       		price = strUtils.Round(price,2);
       		sNo=sNo+1;
       		total = total+price;
       	 String item =(String)salemap.get("item");
       	String batch =(String)salemap.get("batch");
       	sumprdQty = sumprdQty + Double.parseDouble((String)salemap.get("qty"));
       	sumOfTotalPrice = sumOfTotalPrice + Double.parseDouble(Float.toString(price));
       		if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(item))){
       			
       %>
       
          <TR bgcolor = "<%=bgcolor%>">
          <TD align="center"><%=sNo%></TD>           
           <TD align="center"><%=salemap.get("postranid")%></TD>
 			<TD align="center"><%=salemap.get("tranid")%></TD>
   			<TD align="center"><%=salemap.get("CRBY")%></TD>
	            <TD align="center" class="textbold">&nbsp;<%=salemap.get("purchasedate")%></TD>
	            <TD align="center" class="textbold">&nbsp;<%=salemap.get("remarks")%></TD>
	            <TD align="center"><%=salemap.get("item")%></TD>
            <TD align="center"><%=salemap.get("itemdesc")%></TD>
            <TD align="center"><%=salemap.get("batch")%></TD>
             <TD align="right"><%=salemap.get("qty")%></TD>
	            <TD align="right" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(price)%></TD>
                    
	       </TR>
	       <% if(iIndex+1 == salesList.size()){
                                    irow=irow+1;
                                    bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  %>
			<TR bgcolor = "<%=bgcolor%>">
            <TD colspan=8></TD> <TD align= "right" ><b>Total:</b></td><TD align= "right"><b><%=strUtils.formatNum((new Double(sumprdQty).toString())) %>
            </b></TD><TD align= "right"><b><%=strUtils.currencyWtoutSymbol((new Double(sumOfTotalPrice).toString())) %></b></TD> </TR>
                                       
            <%                 }
	       } else{
	    	   sumprdQty = sumprdQty - Double.parseDouble((String)salemap.get("qty"));
               sumOfTotalPrice  =sumOfTotalPrice - Double.parseDouble(Float.toString(price));%>
               <TR bgcolor = "<%=bgcolor%>">
            <TD colspan=8></TD> <TD align= "right" ><b>Total:</b></td><TD align= "right"><b><%=strUtils.formatNum((new Double(sumprdQty).toString())) %>
            </b></TD><TD align= "right"><b><%=strUtils.currencyWtoutSymbol((new Double(sumOfTotalPrice).toString())) %></b></TD> </TR>
  				<%  irow=irow+1;
                               bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; %>
                                <TR bgcolor = "<%=bgcolor%>">
          <TD align="center"><%=sNo%></TD>           
           <TD align="center"><%=salemap.get("postranid")%></TD>
 			<TD align="center"><%=salemap.get("tranid")%></TD>
   			<TD align="center"><%=salemap.get("CRBY")%></TD>
	            <TD align="center" class="textbold">&nbsp;<%=salemap.get("purchasedate")%></TD>
	            <TD align="center" class="textbold">&nbsp;<%=salemap.get("remarks")%></TD>
	            <TD align="center"><%=salemap.get("item")%></TD>
            <TD align="center"><%=salemap.get("itemdesc")%></TD>
            <TD align="center"><%=salemap.get("batch")%></TD>
             <TD align="right"><%=salemap.get("qty")%></TD>
	            <TD align="right" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(price)%></TD>
                    
	       </TR>
	    <% 
	    sumprdQty =  Double.parseDouble((String)salemap.get("qty"));
        sumOfTotalPrice  = Double.parseDouble(Float.toString(price));
        if(iIndex+1 == salesList.size()){ 
            irow=irow+1;
            irow=irow+1;
            bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  %>
<TR bgcolor = "<%=bgcolor%>">
<TD colspan=8></TD> <TD align= "right" ><b>Total:</b></td><TD align= "right"><b><%=strUtils.formatNum((new Double(sumprdQty).toString())) %>
</b></TD><TD align= "right"><b><%=strUtils.currencyWtoutSymbol((new Double(sumOfTotalPrice).toString())) %></b></TD> </TR>
          <%  }
	       
	       }
	       
	       						 irow=irow+1;
                                 iIndex=iIndex+1;
                                 lastProduct = item;%>
       <%}%>
        <TR >
          <TD align="center"></TD> <TD align="center" colspan="7"></TD>
                   <TD align="center"></TD>
	            <TD align="center"><font class="textbold"><b>Total</b></font></TD>
	           
	            <TD align="right" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(total)%></TD>
	                     </TR>
	     <TR>
		
		<td colspan="9" align="center"><input type="button" value="ExportReport"
			onClick="javascript:ExportReport();"></td>
			
		
	</TR>  
	<%} %>              
       </TABLE>
       
  </FORM>
<%@ include file="footer.jsp"%>