<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<% String plant = (String)session.getAttribute("PLANT");%>
 <script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
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

function onConfirm()
{
	var custno = document.form.CUSTNO.value;
	var flag = document.form.FLAG.value;
	var item = document.form.PRODUCTID.value;
	if(custno.length>0&&flag==1)
	{
     	 document.form.action="/track/MobileEventRegServlet?Submit=Attendance_Single";
		 document.form.submit();
	}
	else if(item.length<0&&flag==1)
	{		
		alert('Please Scan Register Event');
		return false;
	}
	else if(custno.length<0&&flag==1)
	{
		
		alert('Please Scan CustomerID');
		return false;
	}
	else if(flag==2)
	{
		var checklen = document.form.chkdono.value;
		 var i=0;var ischeck="";var resflag= false;
		 var noofcheckbox = document.form.chkdono.length;
		 if(document.form.chkdono.checked)
		 			resflag= true;
			
		 if(resflag)
		 {
			 document.form.action="/track/MobileEventRegServlet?Submit=Attendance_SingleCheck";
			 document.form.submit();		 }
		 else
		 {
			 alert('Please Select Order Number');
			 return false;
		 }	   	
	}
   
 }
if (document.layers)
	  document.captureEvents(Event.KEYDOWN);
	  document.onkeydown =
	    function (evt) { 
	      var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	      if (keyCode == 13)   //13 = the code for pressing ENTER 
	      {
	    	  onConfirm();
	      }
	    };
function setFocus(){
	var item = document.form.PRODUCTID.value;
	if(item.length==0 || item==null)
	{
		document.form.PRODUCTID.focus();
	}else{
	    document.form.CUSTNO.focus();
	}
}
</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Attendance Tracking Individual</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
String flagVal="1";

session= request.getSession();

String FROM_DATE ="",  TO_DATE = "",CUSTNAME="", PICKSTATUS="",ORDERTYPE="",HPNO="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",chkString="",itemno="";
Cookie[] cookies = request.getCookies();
if(cookies!=null)
{
for(int i=0;i<cookies.length;i++)
{
if (cookies [i].getName().equals ("REGID"))
{
Cookie selcookie = cookies[i];
if(selcookie.getValue()!=null)
itemno = selcookie.getValue();
break;
}
}
}
PGaction         = _strUtils.fString(request.getParameter("action")).trim();
plant = (String)session.getAttribute("PLANT");
String COMPANY="",html = "",cntRec ="false",fieldDesc="";
CUSTOMERID = _strUtils.fString(request.getParameter("CUSTNO")); 
fieldDesc = StrUtils.fString(request.getParameter("MSG"));
CUSTNAME = StrUtils.fString(request.getParameter("CUST_NAME"));
try{
	if(PGaction.equalsIgnoreCase("viewDet"))
	{
		List dolist =(ArrayList) session.getAttribute("ATTENDANCELST");
		CUSTOMERID = StrUtils.fString(request.getParameter("CUSTNO"));
		Hashtable ht = new Hashtable();
		flagVal="2";
		String donostring="";
		for(int i=0;i<dolist.size();i++)
		{
			Map m = (Map)dolist.get(i);
			donostring = donostring + "'"+(String)m.get("dono")+"',";
		}
		String donoarray=donostring.substring(0,donostring.length()-1);
			movQryList = movHisUtil.getMobileAttendanceList(ht,donoarray,plant);
		
	}
}
catch(Exception e){
	e.printStackTrace();
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="AttendanceTrackingSingle.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="FLAG" value="<%=flagVal%>">
<input type="hidden" name="DESCRIPTION1" value="">
<input type="hidden" name="PRICE" value="">
  <br>  
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Registration Attendance Tracking (Individual) </font></TH>
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
          <TH ALIGN="right" >&nbsp; </TH>
          <TD align="left"></TD>
          <TH ALIGN="left"> </TH>
          <TD>
           &nbsp;
           </TD>
    </TR>	
    <tr>
     <TH WIDTH="35%" ALIGN="RIGHT" > * Registration Events :&nbsp;&nbsp;</TH>
      <TD><INPUT name="PRODUCTID" type = "TEXT" value="<%=itemno%>" size="20"  MAXLENGTH=50>
<a href="#" onClick="javascript:popUpWin('list/catalogList.jsp?ITEM='+form.PRODUCTID.value);">
								<img src="images/populate.gif" border="0" /> </a>           
           </TD>
      </tr>
       <TR>
          <TH ALIGN="right" >&nbsp; </TH>
          <TD align="left"></TD>
          <TH ALIGN="left"> </TH>
          <TD>
           &nbsp;
           </TD>
    </TR>	
       <TR>
          <TH ALIGN="right" >* Customer ID :&nbsp;&nbsp;</TH>
          <TD align="left"><INPUT name="CUSTNO" type = "TEXT" value="<%=CUSTOMERID%>" size="20"  MAXLENGTH=20>
           <INPUT class="Submit" type="BUTTON" value="View" onClick="onViewDetails();">
          </TD>
          <TH ALIGN="left">
         
           </TH>
          <TD>
           
           </TD>
    </TR>
    <TR>
          <TH ALIGN="right" >&nbsp; </TH>
          <TD align="left"></TD>
          <TH ALIGN="left">
                   </TH>
          <TD>
           &nbsp;
           </TD>
    </TR>
    </table>
   <table id="dynamictb" border="0" width="80%" height = "25%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
	<TR>
          <TH ALIGN="right" width="27%">Customer Name :&nbsp;&nbsp;</TH>
          <TD align="left" width="20%"><INPUT name="CUSTNAME" type = "TEXT" value="" size="20"  MAXLENGTH=20>
            </TD>
          <TH ALIGN="left" width="10%">
          </TH>
          <TD width="20%">    &nbsp;       
           </TD>
    </TR>    
  </TABLE>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding ="2" align = "center">
   <%
	       if(movQryList.size()>0){ %>
    <TR BGCOLOR="#000066">
                <TH><font color="#ffffff" align="center">S/N</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Registration Name</TH>
                <TH><font color="#ffffff" align="left"><b>Product ID</TH>
                <TH><font color="#ffffff" align="left"><b>Description</TH>
                <TH><font color="#ffffff" align="left"><b>Status</TH>
	            </tr>
      
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
            
			<TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custname")%></TD>
			<TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
			<TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("itemdesc") %></TD>    
			<TD align= "left">&nbsp;&nbsp;&nbsp;<%=status %></TD> 
      
           </TR>
           
       <%}}%>

  </TABLE>
      <br>
      
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   <input type="button" value="Confirm" onClick="onConfirm();"  > </td>
   
   </TR>
    </table>
  </FORM>
  
<%@ include file="footer.jsp"%>

<script>
setFocus();
window.onload = function hideTable()
{	document.getElementById('dynamictb').style.visibility = "hidden";	
};
function onViewDetails() {
	var custcode = document.form.CUSTNO.value;	
		
	var urlStr = "/track/mobilehandlingservlet";
		
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {				
				PLANT : "<%=plant%>",
				CUSTCODE : custcode,
				ACTION : "VIEW_CUSTDETAIL"
				},
				dataType : "json",
				async: false,
				success : function(data) {
					if (data.status == "101") {
						alert("Session cookies are Disabled/Expired,Please enable cookies to proceed");
                                         
					}
					if (data.status == "100") {
						var resultobj = data.result;
						document.getElementById("CUSTNO").value=resultobj.custcode;
					//addRow('dynamictb');
					document.getElementById('dynamictb').style.visibility = "visible";
					
				document.getElementById("CUSTNAME").value=resultobj.custname;
							}					
				}
		
			});
		}
function addRow(tableID) {
	   
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;     
	  var row = table.insertRow(rowCount);
	  
        var firstCell = row.insertCell(0);
	firstCell.innerHTML ="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	 var secondemptyCell = row.insertCell(1);
	 secondemptyCell.innerHTML ="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	 
	var secondCell = row.insertCell(2);
	secondCell.innerHTML="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Customer Name : </b>&nbsp;<INPUT name=\"CUSTNAME\" id=\"CUSTNAME\" type = \"TEXT\"  size=\"20\" readonly  MAXLENGTH=\"80\" ><b></b> ";
     
}

</script>
