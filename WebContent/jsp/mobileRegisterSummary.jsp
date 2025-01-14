<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="org.springframework.core.annotation.Order"%><html>
<head>
<% String plant = (String)session.getAttribute("PLANT");%>
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

   var flag    = "false";
   var companySel ='<%=plant%>';
   if (companySel=="track"){
   var COMPANY      = document.form.COMPANY.value;
    if(COMPANY != null     && COMPANY != "") { flag = true;}
     
   if(flag == "false"){ alert("Please Enter/Select Company "); return false;}
 }
   var FROM_DATE      = document.form.FROM_DATE.value;
   var TO_DATE        = document.form.TO_DATE.value;
   var DIRTYPE        = document.form.DIRTYPE.value;
   var USER           = document.form.CUSTOMER.value;
   var ITEMNO         = document.form.PRODUCTID.value;
   var ORDERNO        = document.form.ORDERNO.value;
   var JOBNO          = document.form.JOBNO.value;
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}

   //if(flag == "false"){ alert("Please define any one search criteria"); return false;}

  document.form.action="mobileRegisterSummary.jsp";
  document.form.submit();
}
function onOrderType(index)
{
	var ordIndex = document.getElementById('ORDERTYPE');
	var issueSelect = document.getElementById('ISSUESTATUS');
	  
	  if(ordIndex.options[ordIndex.selectedIndex].Value='Mobile Registration')
	  {
		 
		  registration.style.display="block";
		 
	  }        
}
</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<title>Mobile Registration Summary</title>
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


String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",TRANTYPE="",HPNO="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",ordertype="";

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String COMPANY="",html = "",cntRec ="false";
String deliverydateandtime="";
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE"));
int noofregisters=0;
 
COMPANY=plant;

session.setAttribute("RFLAG","10");
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
TRANTYPE =  _strUtils.fString(request.getParameter("TRANTYPE"));
if(DIRTYPE.length()<=0){
DIRTYPE = "MOBILE_REGISTER";
}

if(PGaction.equalsIgnoreCase("View")){
 
 try{
        Hashtable ht = new Hashtable();
         ORDERTYPE = "Mobile Registration";
        if(ISSUESTATUS.equalsIgnoreCase("R"))
        	ISSUESTATUS="N";
        if(ISSUESTATUS.equalsIgnoreCase("A"))
        	ISSUESTATUS="C";
        if(TRANTYPE.equalsIgnoreCase("0"))
        	TRANTYPE="";
        if(_strUtils.fString(JOBNO).length() > 0)            ht.put("A.JOBNUM",JOBNO);
        if(_strUtils.fString(ITEMNO).length() > 0)           ht.put("B.ITEM",ITEMNO);
        if(_strUtils.fString(ORDERNO).length() > 0)          ht.put("B.DONO",ORDERNO);
        if(_strUtils.fString(CUSTOMER).length() > 0)         ht.put("A.CUSTNAME",CUSTOMER);
        if(_strUtils.fString(CUSTOMERID).length() > 0)       ht.put("A.CUSTCODE",CUSTOMERID);
        if(_strUtils.fString(ORDERTYPE).length() > 0)        ht.put("A.ORDERTYPE",ORDERTYPE);
        if(_strUtils.fString(ISSUESTATUS).length() > 0)      ht.put("B.LNSTAT",ISSUESTATUS);
        if(_strUtils.fString(PICKSTATUS).length() > 0)       ht.put("B.PICKSTATUS",PICKSTATUS);
         if(_strUtils.fString(HPNO).length() > 0)            ht.put("A.CONTACTNUM",HPNO);
         if(_strUtils.fString(TRANTYPE).length() > 0)        ht.put("B.TRANTYPE",TRANTYPE);
       movQryList = movHisUtil.getMobileRegisterSummaryList(ht,fdate,tdate,DIRTYPE,COMPANY,"");
        
		if(movQryList.size()<=0)
			cntRec ="true";

 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="mobileRegisterSummary.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRICE" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Mobile Registration Summary </font></TH>
    </TR>
  </TABLE>
  <br>
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
<a href="#" onClick="javascript:popUpWin('list/catalogList.jsp?ITEM='+form.PRODUCTID.value);">
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
          <TH ALIGN="left"> Customer Name : </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="20"  MAXLENGTH=20></TD>
          
          <TH ALIGN="left"> Handphone : </TH>
          <TD><INPUT name="HPNO" type = "TEXT" value="<%=HPNO%>" size="20"  MAXLENGTH=20></TD>      
           
    </TR>
    
     <TR>
        
            <TH ALIGN="left" >&nbsp;Tran Type : </TH>
          <TD>
           <SELECT NAME ="TRANTYPE" id="tranSelect" size="1" >
           <OPTION   value='' selected="selected" id="Choose">Choose</OPTION>
       		<OPTION   value='Attendance' id='Attendance'>Attendance</OPTION>
     		<OPTION   value='Clockin' id='Clockin'>Clockin</OPTION>
          </SELECT>
          </TD>
      
           <TH ALIGN="left" >Status : </TH>
           <TD>
          <SELECT NAME ="ISSUESTATUS" size="1" id="statusOptions">
        <OPTION   value='' selected="selected">Choose</OPTION>
          <OPTION   value="N">REGISTERED </OPTION>
            <OPTION   value="O">CLOCKIN </OPTION> 
           <OPTION   value="C">CLOCKOUT </OPTION>  
            <OPTION   value="A">ATTENDED </OPTION> 
     
       		          </SELECT></TD>
                   
           <TD ALIGN="left" ><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
    </TR>
   
    <TR>&nbsp;
      </TR>
	
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

                <TH><font color="#ffffff" align="center">S/N</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Order Type</TH>
                <TH><font color="#ffffff" align="left"><b>Ref No</TH>
                <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
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
		  noofregisters = movQryList.size();   
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               Map lineArr = (Map) movQryList.get(iCnt);
               int iIndex = iCnt + 1;
               String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            //   deliverydateandtime=(String)lineArr.get("deliverydate")+ " " +(String)lineArr.get("deliverytime");
            ordertype = (String)lineArr.get("ordertype");
            String status= (String)lineArr.get("lnstat");
 			String trantype= (String)lineArr.get("trantype");
            if(status.equalsIgnoreCase("N"))
            	   status="REGISTERED";
         else if(status.equalsIgnoreCase("C")&&trantype.equalsIgnoreCase("Attendance"))
            	   status = "ATTENDED";
         else if(status.equalsIgnoreCase("O")&&trantype.equalsIgnoreCase("Clockin"))
      	   status = "CLOCK-IN";
         else if(status.equalsIgnoreCase("C")&&trantype.equalsIgnoreCase("Clockin"))
        	   status = "CLOCK-OUT";     
         
       %>

           <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>   
         
      
      <TD><a href="javascript:popUpfullWin('/track/deleveryorderservlet?DONO=<%=(String)lineArr.get("dono")%>&Submit=View&RFLAG=8&COMPANY=<%=COMPANY%>');" ><%=(String)lineArr.get("dono")%></a></TD>
         
     
           <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobNum")%></TD>
            
            <TD align= "left">&nbsp;&nbsp;&nbsp;<a href="javascript:popUpfullWin('customerDetail.jsp?action=view&CUST_CODE=<%=(String)lineArr.get("custcode")%>');"><%=(String)lineArr.get("custname")%></a></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
               <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("itemdesc") %></TD>          
                 <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("TRANDATE") %>&nbsp;<%=(String)lineArr.get("TIME") %></TD>
                  
        <TD align= "left">&nbsp;&nbsp;&nbsp;<%=status%></TD> 
		       
           </TR>
           
       <%}%>
<tr>
	<td colspan="7">	
	</td>	
	<td align= "left"><b>Total</b></TD> 
	<td>&nbsp;&nbsp;<%=noofregisters%>  </td>
	</tr>
  </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" > </td>
   <INPUT type="Hidden" name="DIRTYPE" value="MOBILE_REGISTER">
   </TR>
    </table>
  </FORM>
  <script>
jQuery.fn.filterOn = function(selection, values) {     
	return this.each(function() {         
		    var select = this;      
		           var options = [];   
          $(select).find('option').each(function() {  
               options.push({value: $(this).val(), text: $(this).text()});  });    
                   $(select).data('options', options);  
                    $(selection).change(function(){    
                           $(selection + " option:selected").each(function(){  
                 var options = $(select).empty().data('options');     
                                     var haystack = values[$(this).attr('id')];  
                    $.each(options, function(i){   
                               var option = options[i];    
       if($.inArray(option.value, haystack) !== -1) {  
   $(select).append(     
     $('<option>').text(option.text).val(option.value)  );  
                      }                         });  

                     });   
          });      
                       });   
      }; 
	$(function() {
		$('#statusOptions').filterOn('#tranSelect', {'Attendance' :[ 'N', 'A' ] ,'Clockin' : [ 'N', 'O', 'C' ],'Choose':['N','O','C','A']});
	});
</script>
<%@ include file="footer.jsp"%>
