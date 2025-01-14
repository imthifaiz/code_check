<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.constants.IDBConstants"%><html>
<head>
<% String plant = (String)session.getAttribute("PLANT");%>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function popUpfullWin(URL) {
 // subWin = window.open(URL, 'maintain', 'toolbar=1,scrollbars=yes,location=1,statusbar=1,menubar=1,dependant=1,resizable=1,width='+screen.width+',height='+screen.height+',left = 0,top = 0');
   subWin = window.open(URL, 'maintain', 'toolbar=1,scrollbars=yes,location=1,statusbar=1,menubar=1,dependant=1,resizable=1,width=800,height=800,left = 200,top = 184');
}
function ExportReport()
{
  var flag    = "false";
  document.form.FROM_DATE.value;
  document.form.TO_DATE.value;
  document.form.PRODUCTID.value;
  document.form.ORDERNO.value;
  document.form.JOBNO.value;
  document.form.DIRTYPE.value;
  document.form.CUSTOMER.value;
  document.form.xlAction.value="GenerateXLSheet";
  document.form.action="mobileOrderExcel.jsp";
 
  var  DIRTYPE= document.form.DIRTYPE.value;
      
  document.form.submit();
  
}
function onGo(){
  document.form.action="mobileRegisterFullfillment.jsp?action=View";
  document.form.submit();
}
function checkElements()
{
	var i=0;var ischeck="";var flag= false;
	 var noofcheckbox = document.form.chkdono.length;
	 for(i=0;i<noofcheckbox;i++){
		 
		 ischeck = document.form.chkdDoNo[i].checked;
		 flag= true;
		 }
	 if(flag)
	 {
		 return true;
	 }
	 else
	 {
		 alert('Please Select Order Number');
	 }
}
function onConfirm(buttonVal)
{   
  // if(item == "" || item == null) {alert("Please Select productID "); document.form.ITEM.focus(); return false; }
    var i=0;var ischeck="";var flag= false;
	 var noofcheckbox = document.form.chkdono.length;

 
	 if(noofcheckbox>0){
	 for(i=0;i<noofcheckbox;i++){
		
	 		 if(document.form.chkdono[i].checked){
	 		flag= true;	 		
	 		 }
		 }}
	 if(noofcheckbox='undefined')
	 {
		 if(document.form.chkdono.checked){
		 		flag= true;
				 		 }		 
	 } 
	 if(flag)
	 {
		 document.form.action="/track/MobileEventRegServlet?Submit=MobileRegFullfillment&STATUS="+buttonVal;
		 document.form.submit();
	 }
	 else
	 {
		 alert('Please Select Order Number');
		 return false;
	 }
   
   
 }

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Mobile Registration Order Details</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();


session= request.getSession();

String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",HPNO="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="";

PGaction         = _strUtils.fString(request.getParameter("action")).trim();
String COMPANY="",html = "",cntRec ="false",fieldDesc="";

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = IDBConstants.MOBILE_REGISTRATION;
COMPANY=plant;
fieldDesc = StrUtils.fString(request.getParameter("MSG"));
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);


DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = _strUtils.fString(request.getParameter("JOBNO"));
USER          = _strUtils.fString(request.getParameter("USER"));
ITEMNO        = _strUtils.fString(request.getParameter("PRODUCTID"));
DESC          = _strUtils.fString(request.getParameter("DESCRIPTION1"));
ORDERNO       = _strUtils.fString(request.getParameter("ORDERNO"));
CUSTOMER      = _strUtils.fString(request.getParameter("CUSTOMER"));
HPNO          = _strUtils.fString(request.getParameter("HPNO"));
CUSTOMERID    = _strUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = _strUtils.fString(request.getParameter("PICKSTATUS"));
ISSUESTATUS   = _strUtils.fString(request.getParameter("ISSUESTATUS"));
Hashtable ht = new Hashtable();
String chkString="";
ORDERTYPE = IDBConstants.MOBILE_REGISTRATION;
if(DIRTYPE.length()<=0){
DIRTYPE = "MOBILE_REGISTER";
}
if(PGaction.equalsIgnoreCase("View")){
 
 try{              
        if(_strUtils.fString(JOBNO).length() > 0)            ht.put("A.JOBNUM",JOBNO);
        if(_strUtils.fString(ITEMNO).length() > 0)           ht.put("B.ITEM",ITEMNO);
        if(_strUtils.fString(ORDERNO).length() > 0)          ht.put("B.DONO",ORDERNO);
        if(_strUtils.fString(CUSTOMER).length() > 0)         ht.put("A.CUSTNAME",CUSTOMER);
        if(_strUtils.fString(CUSTOMERID).length() > 0)       ht.put("A.CUSTCODE",CUSTOMERID);
        if(_strUtils.fString(ORDERTYPE).length() > 0)        ht.put("A.ORDERTYPE",ORDERTYPE);
        if(_strUtils.fString(ISSUESTATUS).length() > 0)      ht.put("B.LNSTAT",ISSUESTATUS);
        if(_strUtils.fString(PICKSTATUS).length() > 0)       ht.put("B.PICKSTATUS",PICKSTATUS);
         if(_strUtils.fString(HPNO).length() > 0)            ht.put("A.CONTACTNUM",HPNO);
       ht.put("b.trantype","Attendance");
       
       movQryList = movHisUtil.getAttendanceClockinList(ht,fdate,tdate,"Attendance",COMPANY,DESC);
        
		if(movQryList.size()<=0)
			cntRec ="true";		

 }catch(Exception e) { }
}
else if(PGaction.equalsIgnoreCase("Viewdono")){
	try{
		String donoValues = (String)session.getAttribute("chkdonoval");
		String donovals = donoValues.substring(0,donoValues.length()-1);
	
    movQryList = movHisUtil.getMobileAttendanceList(ht,donovals,plant);
	}catch(Exception e){
		e.printStackTrace();
	}	
}

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="mobileRegisterFullfillment.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
 <input type="hidden" name="PRICE" value="">

  <br>  
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Registration Attendance Tracking (Mass) </font></TH>
    </TR>
  </TABLE>
   <br>
  <%if(fieldDesc.length()>0){ %>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" align="center">
   <font class="maingreen"> <%=fieldDesc%></font>
 </table>
<%} %>
 
  <TABLE border="0" width="80%" height = "25%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
    
       <TR>
          <TH ALIGN="left" >&nbsp;Ref No : </TH>
          <TD><INPUT name="JOBNO" type = "TEXT" value="<%=JOBNO%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left">Product ID : </TH>
          <TD><INPUT name="PRODUCTID" type = "TEXT" value="<%=ITEMNO%>" size="20"  MAXLENGTH=50>
    <a href="#" onClick="javascript:popUpWin('list/mobileItemList.jsp?ITEM='+form.PRODUCTID.value);">
								<img src="images/populate.gif" border="0" /> </a>
           
           </TD>
    </TR>
	
    <TR>
        <TH ALIGN="left" >&nbsp;Order No : </TH>
          <TD><INPUT name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left" >&nbsp;Product Description : </TH>
          <TD><INPUT name="DESCRIPTION1" type = "TEXT" value="<%=_strUtils.forHTMLTag(DESC)%>" size="20"  MAXLENGTH=100></TD>
    </TR>
       <TR>
          <TH ALIGN="left"> Registration Name : </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="20"  MAXLENGTH=20></TD>
          
          <TH ALIGN="left"> Handphone : </TH>
          <TD><INPUT name="HPNO" type = "TEXT" value="<%=HPNO%>" size="20"  MAXLENGTH=20></TD>      
           
    </TR>
         <TR>        
            <TH ALIGN="left" >&nbsp;Status : </TH>
          <TD><SELECT NAME ="ISSUESTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     	
     		<OPTION   value='C' <%if(ISSUESTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>ATTENDED </OPTION>
     		<OPTION   value='N' <%if(ISSUESTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>REGISTERED </OPTION>
          </SELECT></TD>
      
           <TH ALIGN="left" > </TH>
           <TD></TD>  
           
           <TD ALIGN="left" ><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
    </TR>   
    <TR>&nbsp;
  		          
    </TR>
	
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding ="2" align = "center">
    <TR BGCOLOR="#000066">
                <TH><font color="#ffffff" align="center">S/N</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Order Type</TH>
                <TH><font color="#ffffff" align="left"><b>Ref No</TH>
                <TH><font color="#ffffff" align="left"><b>Registration Name</TH>
                <TH><font color="#ffffff" align="left"><b>Product ID</TH>
                <TH><font color="#ffffff" align="left"><b>Description</TH>
            
                <TH><font color="#ffffff" align="left"><b>Order Date & Time</TH>
            
                <TH><font color="#ffffff" align="left"><b>Status</TH>
	     
       </tr>
       <%
	       if(movQryList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
                 
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               Map lineArr = (Map) movQryList.get(iCnt);
               int iIndex = iCnt + 1;
               String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           
               chkString = (String)lineArr.get("dono");
               
               String status= (String)lineArr.get("lnstat");
               if(status.equalsIgnoreCase("N"))
            	   status="REGISTERED";
               else if(status.equalsIgnoreCase("C"))
            	   status = "ATTENDED";
       %>

           <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdono"  value="<%=chkString%>"></font></TD>       
    
      
      <TD><%=(String)lineArr.get("dono")%></TD>
         
           <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobNum")%></TD>
            
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custname")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
               <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("itemdesc") %></TD>    
          <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("TRANDATE") %>&nbsp;<%=(String)lineArr.get("TIME") %></TD>
        <TD align= "left">&nbsp;&nbsp;&nbsp;<%=status %></TD> 
		       
           </TR>
           
       <%}%>
  </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   <input type="button" value="Registered" onClick="onConfirm('NEW');" > </td>
   
     <td>   <input type="button" value="Attended" onClick="onConfirm('CLOSE');" > </td>
   <INPUT type="Hidden" name="DIRTYPE" value="MOBILE_REGISTER">
   </TR>
    </table>
  </FORM>
  
<%@ include file="footer.jsp"%>
<script>
function onConfirm1(btnval)
{
	var item = document.getElementById('PRODUCTID').value;
	var jobno = document.getElementById('JOBNO').value;
	var orderno = document.getElementById('ORDERNO').value;
	var fromdt = document.getElementById('FROM_DATE').value;
	var urlStr = "/track/MobileEventRegServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		Submit : "MobileRegFullfillment",
		STATUS: btnval,
		PRODUCTID:item,
		JOBNO:jobno,
		ORDERNO:orderno,
		FROM_DATE:fromdt		
	},
	dataType : "json",
	success : function(data) {
		if (data.status == "100") {
			
			var resultVal = data.result;			
			//document.form.PRODUCTID.value=resultVal.productid;
			document.getElementById("MSG").value=resultVal.MSG;
			document.getElementById("PRODUCTID").value=resultVal.productid;
			//document.getElementById("DESCRIPTION3").value=resultVal.description3;
			window.location=window.location;
				} 
	}
});	
}
</script>
