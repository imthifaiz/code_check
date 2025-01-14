<%@page import="com.lowagie.text.pdf.PRAcroForm"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@ page import="java.util.*" session="true"%>
<%@ include file="header.jsp"%>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="com.track.tables.ITEMMST"%>
<%@page import="java.math.RoundingMode" %>
<%@page import="java.math.BigDecimal" %>

<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<!-- Not in Use - Menus status 0 -->
<%
String title = "Create Direct Tax Invoice";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_ORDER%>"/>
</jsp:include>

<style type="text/css">
* {
  margin: 0;
  padding: 0;
}
.imgHiper {
     border: 0;
	 margin: 0;
	 padding: 0;
}
</style>
<!-- <script src="assets/js/jquery.min.js"></script> -->
<script src="js/json2.js"></script>
<script src="js/general.js"></script>
<script src="js/calendar.js"></script>
<script type="text/javascript">

 var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(encodeURI(URL), 'POS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
  
	
</script>
<script >
autosize(document.querySelectorAll('textarea'));
document.getElementById("BATCH_0").value="NOBATCH";

</script>
<script>
function headerReadable(){
	if(document.form.DATEFORMAT.checked)
	{
		
		document.form.DELIVERYDATE.value="";
		$('#DELIVERYDATE').attr('readonly',true).datepicker({ dateFormat: 'dd/mm/yy'});
		
	}
	else{
		document.form.DELIVERYDATE.value="";
		 $('#DELIVERYDATE').attr('readonly',false).datepicker("destroy");

	}
}
</script>
<script type="text/javascript">
   var subWinForDiscount = null;
  function popUpWinForDiscount(URL) {
    subWinForDiscount = window.open(URL, 'Discount', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }  
</script>

<title>Goods Issue With Price</title>
</head>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	 StrUtils strUtils     = new StrUtils();
	 Generator generator   = new Generator();
	 userBean _userBean      = new userBean();
	 ITEMMST items = new ITEMMST();
	 PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
	 String plant = (String) session.getAttribute("PLANT");
	 String btnString="";
	 HashMap<String, String> loggerDetailsHasMap1 = new HashMap<String, String>();
	 loggerDetailsHasMap1.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	 loggerDetailsHasMap1.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")));
 	 MLogger mLogger1 = new MLogger();
     DecimalFormat decformat = new DecimalFormat("#,##0.00");
     DecimalFormat decformaDiscount = new DecimalFormat("#,##0.0");
     DecimalFormat fltformat = new DecimalFormat("#,###");
 	 mLogger1.setLoggerConstans(loggerDetailsHasMap1);
 	 String fieldDesc="",cursymbol="",DISCITEM="",discountDesc="",cmd="";
	 String refNO="",PLANT="",ITEM ="",ITEM_DESC="",SCANQTY="",LOCDESC="",iserrorVal="",LOC="",REASONCODE="",EMP_NAME="", TRANSACTIONDATE="",EMP_ID="", disccnt="",STOCKQTY="",REMARKS="",gsttax="",action="",
			  deldate = "", jobNum = "", custName = "",ordertype="", custCode = "CASH", personIncharge = "CASH", contactNum = "",
				remark1 = "", remark2 = "", address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "",
				contactname = "", telno = "", email = "",currencyid="", add1 = "", add2 = "", add3 = "", add4 = "", country = "", zip = "", 
				remarks = "", gst="",deliverydate="",customerstatusid="",customerstatusdesc="", customertypeid="",customertypedesc="",paymenttype="",
				shippingid="",shippingcustomer="",orderdiscount="",shippingcost="",incoterms="",statusID="",cust_name="CASH",priceval="",CashCust="",INVOICENO="",dateformat="";
	 String html = "",BATCH="",CHKBATCH="";float gstf=0;
	 int Total=0; float sumSubTotal=0,pcgsttax=0,sumGsttax=0,totalGsttax=0;
	 String unit_price="",totalprice="",cntlDiscount="",AVAILQTY="",REFERENCENO="",serialized="";
	 PLANT = (String)session.getAttribute("PLANT");
	 String SumColor=""; 
	 Vector poslist=null;
	 ArrayList prdlist=null;
	 boolean flag=false;
	 sb.setmLogger(mLogger1);
	 gst = sb.getGST("POS",PLANT);
	 cursymbol = DbBean.CURRENCYSYMBOL;
	 float unitpc=0,totalpc=0,  gstvalCalc=0, totalsum=0,msprice=0;
	 String sUserId = (String) session.getAttribute("LOGIN_USER");
	 String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	 action = StrUtils.fString(request.getParameter("action")).trim();
	 String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
	 LOCDESC = StrUtils.fString(request.getParameter("LOCDESC")).trim();
	 iserrorVal = StrUtils.fString(request.getParameter("iserrorVal")).trim();
	 //LOC = StrUtils.fString(request.getParameter("LOC")).trim();
	 BATCH=StrUtils.fString(request.getParameter("BATCH")).trim();
     //REMARKS = StrUtils.fString(request.getParameter("REMARKS")).trim();
     CHKBATCH = StrUtils.fString(request.getParameter("CHKBATCH"));
     REASONCODE  = StrUtils.fString(request.getParameter("REASONCODE"));
     refNO=  StrUtils.fString(request.getParameter("REFERENCENO"));
     EMP_NAME  = StrUtils.fString(request.getParameter("EMP_NAME"));
     EMP_ID  = StrUtils.fString(request.getParameter("EMP_ID"));
     TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
     collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
     AVAILQTY=StrUtils.fString(request.getParameter("AVAILQTY"));
     cmd =StrUtils.fString(request.getParameter("cmd"));
     serialized=StrUtils.fString(request.getParameter("serialized"));
     String TRANTYPE=StrUtils.fString(request.getParameter("TRANTYPE"));
     System.out.println("serialized"+serialized);
     String EDIT = StrUtils.fString(request.getParameter("EDIT")).trim();
     System.out.println("EDIT "+ sTranId+ ", " +EDIT);
     INVOICENO = StrUtils.fString(request.getParameter("INVOICENO")).trim();
	 String INVOICECHECK = StrUtils.fString(request.getParameter("INVOICECHECK")).trim();
	 paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
     String result   = StrUtils.fString(request.getParameter("result")).trim();
	 String uom = su.fString(request.getParameter("UOM"));
	 String UNITPRICEDISCOUNT   = StrUtils.fString(request.getParameter("UNITPRICEDISCOUNT")).trim();
	 String DATEFORMAT   = StrUtils.fString(request.getParameter("DATEFORMAT")).trim();
     DateUtils _dateUtils = new DateUtils();
     String curDate =DateUtils.getDate();
     collectionTime = DateUtils.getTimeHHmm();
     String basecurrency=_PlantMstDAO.getBaseCurrency(plant);
     String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
     
 	if(currencyid.length()<0||currencyid==null||currencyid.equalsIgnoreCase(""))currencyid=basecurrency;
 	
 	float gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
  	/* float shipingCostValue ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost); */
  	
  	
  	if(gstVatValue==0f){
  		gst="0.000";
  	}else{
  		gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
  	}/* if(shipingCostValue==0f){
  		shippingcost="0.00000";
  	}else{
  		shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
  	} */
  	
     if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
     String ischecked = "";
     if(REASONCODE=="" || REASONCODE==null){
    	 REASONCODE="NOREASONCODE";
     }
    if(CHKBATCH==null || CHKBATCH=="" ||CHKBATCH.equalsIgnoreCase("true")) 
	{
	  		 ischecked = "checked";
	}
  
	if(sTranId.length()>0){
		poslist = (Vector)session.getValue("poslist");
		prdlist = (ArrayList)session.getValue("prdlist");
	     session.setAttribute("tranid",sTranId);
        }
	else{
		 session.putValue("poslist", null);
		 session.putValue("prdlist", null);
	}
	/* SCANQTY="1"; */
		
	if((String)session.getAttribute("errmsg")!=null)
	{
	   fieldDesc= (String)session.getAttribute("errmsg");
	   session.setAttribute("errmsg","");
	   gstf = Float.parseFloat(gst);
	}
	if(result.equalsIgnoreCase("sucess"))
    {
     fieldDesc=StrUtils.fString((String)session.getAttribute("RESULT"));
     System.out.print(fieldDesc);
     fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
    }
	
	if(prdlist!=null && (prdlist.size()>0)){
		
		for(int k=0;k<prdlist.size();k++)
        {
			Map map = (Map) prdlist.get(k);
			cust_name = (String) map.get("CUST_NAME");
			custCode = (String) map.get("CUST_CODE");			
			personIncharge = (String) map.get("PERSON_INCHARGE");
			contactNum = (String) map.get("CONTACT_NUM");
			remark1 = (String) map.get("REMARK1");
			remarks = (String) map.get("REMARK2");
			remark2 = (String) map.get("REMARK3");			
			add1 = (String) map.get("ADD1");
			add2 = (String) map.get("ADD2");			
			add3 = (String) map.get("ADD3");
			add4 = (String) map.get("ADD4");
			REASONCODE=(String) map.get("REASONCODE");
			shippingid = (String) map.get("SHIPPINGID");
			shippingcustomer = (String) map.get("SHIPPINGCUSTOMER");
			orderdiscount = (String) map.get("ORDERDISCOUNT");
			shippingcost = (String) map.get("SHIPPINGCOST");			
			incoterms = (String) map.get("INCOTERMS");
			gst = (String) map.get("GST");		
			ordertype = (String) map.get("ORDERTYPE");	
			deliverydate = (String) map.get("DELIVERYDATE");	
			EDIT = (String) map.get("EDIT");
			refNO = (String) map.get("REFERENCENO");	
			CashCust = (String) map.get("CashCust");
			customertypedesc = (String) map.get("CUSTOMERTYPEDESC");
			paymenttype = (String) map.get("PAYMENTTYPE");
			currencyid = (String)map.get("currencyid");
			dateformat = (String)map.get("DELIVERYDATEFORMAT");
			System.out.println("Tran "+refNO+",CashCust "+CashCust);
        }
        }
	double shipingCostValue ="".equals(shippingcost) ? 0.0d :  Double.parseDouble(shippingcost);
	float orderDiscountValue ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
	shippingcost = StrUtils.addZeroes(shipingCostValue, numberOfDecimal);
	if(orderDiscountValue==0f){
  		orderdiscount="0.000";
  	}else{
  		orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
  	}
	
%>

<% if(EDIT.equals("EDIT")) { System.out.println("EDIT "+EDIT);%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
document.getElementById("back").style.display = "none";
});
</script>
<%}else{ System.out.println("EDIT 0");%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
document.getElementById("editback").style.display = "none";
document.getElementById("deleteall").style.display = "none";
});
</script>
 	<%}%>
 	
			<% if(CashCust.equals("1")) { %>
			
			<script type="text/javascript">
			document.addEventListener("DOMContentLoaded", function() {
			      document.getElementById("CashCust").checked=true;	
			      Cash();
			});
			      </script>
			      <%}else{ %>
			      
			      <script type="text/javascript">
			      document.addEventListener("DOMContentLoaded", function() {
			       	document.getElementById("CashCust").checked=false;
			       	CashCust="";
			      });
			       	</script>
			       	<%}%>
	<% if(poslist!=null && (poslist.size()>0)){
		if(cmd.equalsIgnoreCase("ADD") || cmd.equalsIgnoreCase("Delete"))
		{ %>		
			 <script type="text/javascript">
		      document.addEventListener("DOMContentLoaded", function() {		   	  
		       document.getElementById("item").focus();
		      });
		       	</script>
		<%}%>
		
		<%if(cmd.equalsIgnoreCase("ViewTran") || cmd.equalsIgnoreCase("EditTran"))
    	 for(int k=0;k<poslist.size();k++)
         {
        	 //pcgsttax=0;
	         ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
	         //LOC = itemord.getLoc();
	         //LOCDESC = itemord.getLocDesc();
	         //System.out.println("items.setLoc1"+LOC);
	         INVOICENO= itemord.getUSERFLD5();
         }
	}
%>


<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<center>
<div id="errorMessage" class="mainred">
 
</div>
</center>
<FORM class="form-horizontal" name="form" method="get" id="dynamicprdswithpriceForm" action="">
<input type="hidden" id="iserrorVal" value="<%=iserrorVal%>">
<input hidden value="1" name="flagwithbatch" id="flagwithbatch">
<input hidden value="1" name="flagissuewithbatch" id="flagissuewithbatch">
<input hidden value="1" name="goodsissuewithbatchprice" id="goodsissuewithbatchprice">
<input type="hidden" name="formtype" value="taxinvoice">
<INPUT type = "hidden" name="SHIPPINGID" id="SHIPPINGID" value ="<%=shippingid%>">
<br>

<center>
<div class="mainred" id="errorMessage">
</div>
</center>
<center class="mainred"><%=fieldDesc%></center>
<center><div style="color:green;font-size: 16px;font-weight:bold;font-family: 'Ubuntu', sans-serif;" id="appenddiv"></div></center>
<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
<INPUT type="hidden" name="RCPNO" value="" />
<INPUT type="hidden" name="TRANTYPE" value="GOODSISSUEWITHBATCHPRICE" />


 		
 	<div class="form-group">
 		<div class="row">
 		
 		<div class="col-sm-2" style="padding:6px">
        <label for="Transaction ID">&nbsp;Transaction ID:</label></div>
        <div class="col-sm-2" style="padding:6px">
      	    <div class="input-group">
    		<INPUT class="form-control" name="TRANID" type="TEXT" id="TRANID" value="<%=sTranId%>" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/posTranIDList.jsp?TYPE=GOODSISSUEWITHBATCHPRICE');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Transaction Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		
  		
  		<div class="col-offset-sm-1 col-sm-4" style="text-align: center;padding:6px" >
		<button type="button" class="Submit btn btn-default"  onClick="onView();"><b>View</b></button>&nbsp;
		<button type="button" class="Submit btn btn-default" id="newGenID" onClick="onGenID();"><b>New</b></button>&nbsp;
		<button type="button" class="Submit btn btn-default"  onClick="onNewPOS();"><b>Clear All</b></button>&nbsp;
		<lable class="checkbox-inline"><INPUT style="visibility: hidden" Type=Checkbox  style="border:0;" name = "CashCust" id="CashCust"  onchange="Cash();">
                     <b></b></lable>   
 		</div> 	
 		
 		<div class="col-sm-2" style="padding:6px"><label for="Customer name">&nbsp;Customer Name/ID:</label></div>
      <div class="col-sm-2" style="padding:6px"><div class="input-group">
    		 <INPUT id="CUST_NAME" class="form-control" name="CUST_NAME" type="TEXT" value="<%=su.forHTMLTag(cust_name)%>" style="width: 100%" MAXLENGTH=100>
    		<span id="sp1" class="input-group-addon"  onClick="javascript:popUpWin('customer_list_order.jsp?CUST_NAME='+form.CUST_NAME.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>" id="custCode"> <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
        <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">	<INPUT   name="TELNO" type="hidden" value="<%=telno%>" >
 		<INPUT	type="hidden" name="EMAIL" value="<%=email%>" /> 		<INPUT  type="hidden"  name="REMARK2" value="<%=remarks%>">
 		<INPUT type="hidden"  name="ZIP"	 value="<%=zip%>" /> 		<INPUT type = "hidden"  name="CUSTOMERSTATUSDESC" value="<%=customerstatusdesc%>">
 		 
 		 
 		 <INPUT type = "hidden"  name="EDIT" value="<%=EDIT%>">
 		
 		 </div>
 		 
 		 </div> 
 		 <div class="row">
 		 
		 <div class="col-sm-2" style="padding:6px"><label for="Invoice No">&nbsp;Invoice NO:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT class="form-control" name="INVOICENO" id="INVOICENO" type="TEXT"  value="<%=INVOICENO%>" style="width: 100%" MAXLENGTH=100>
        <span class="input-group-addon"  onClick="onNew()">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
        </div>
			   </div>
			 
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>			   
			   <div class="col-sm-2" style="padding:6px"><label  for="rcbno" id="TaxLabelOrderManagement"></label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="20" onkeypress="return isNumberKey(event,this,4)" id="GST" MAXLENGTH=20 name="GST"  value="<%=gst%>">
        <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
        </div>
        <INPUT type="hidden" name="COUNTRY" value="<%=country%>" /> 
        <INPUT type = "Hidden" name="CONTACT_NUM" value="<%=contactNum%>">
        </div>
        
 		<div class="col-sm-2" style="padding:6px"><label  for="Person Incharge">Contact Name:</label></div>
      <div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT type = "TEXT" style="width: 100%" class = "form-control" MAXLENGTH=100  name="PERSON_INCHARGE" value="<%=personIncharge%>" readonly>
        </div>
        <INPUT type="hidden"  name="ADD1" value="<%=add1%>" />
        </div>
			   
			   </div>
			   <div class="row">
			  	
		<div class="col-sm-2" style="padding:6px"><label  for="Transaction date">Date:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">		
					<INPUT class="form-control datepicker " name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" readonly MAXLENGTH="80" >
			 <INPUT type="hidden" name="COLLECTION_TIME" value="<%=collectionTime%>" />
			   </div>
			   </div>
			   
			   <div class="col-sm-2" style="padding:6px"><label for="Remarks">Shipping Cost:</label></div>
 		<div class="col-sm-2" style="padding:6px">
 		<div class="input-group">
        <INPUT class="form-control" type = "TEXT" style="width: 100%" onkeypress="return isNumberKey(event,this,6)" id="SHIPPINGCOST" MAXLENGTH=20 name="SHIPPINGCOST" value="<%=new java.math.BigDecimal(shippingcost).toPlainString()%>">
        </div>
        <INPUT type="hidden" name="COUNTRY" value="<%=country%>" /> 
        </div>
  					  
			  <div class="col-sm-2" style="padding:6px"><label for="Customer Type">&nbsp;Customer Type:</label></div> 
			  <div class="col-sm-2" style="padding:6px"><div class="input-group">
			   <INPUT type = "text" class="form-control" name="CUSTOMERTYPEDESC" value="<%=customertypedesc%>" READONLY style="width: 100%" MAXLENGTH=100>
			   </div></div>
			   		   
			   </div>
			   
			   <div class="row">
      	<div class="col-sm-2" style="padding:6px"><label for="incoterm">&nbsp;Delivery Period/Date:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">        
        <INPUT type = "TEXT" size="10" id="DELIVERYDATE"  MAXLENGTH="20" name="DELIVERYDATE" <%if(dateformat.equals("1")) {%>readonly class="form-control datepicker"<%} else {%> class=form-control <%}%> value="<%=deliverydate%>">
		<span class="input-group-addon"> 
        <input type = "checkbox" id = "DATEFORMAT" style="width : 25%" name = "DATEFORMAT" <%if(dateformat.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/><font size="2.9"><b>&nbsp;By Date</b></font>
      	</span>
      	</div>
      	</div>
      	
      	<div class="col-sm-2" style="padding:6px"><label for="Remarks">Order Discount:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT class="form-control" type = "TEXT" style="width: 100%"  onkeypress="return isNumberKey(event,this,4)" id="ORDERDISCOUNT" MAXLENGTH=20 name="ORDERDISCOUNT" value="<%=orderdiscount%>">
        <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
        </div></div>
              
      	<div class="col-sm-2" style="padding:6px"><label for="payment type">Payment Type:</label></div>
      	<div class="col-sm-2" style="padding:6px"><div class="input-group">
      	<INPUT type="text" class="form-control" name="PAYMENTTYPE" value="<%=paymenttype%>" style="width: 100%" MAXLENGTH="100" >
      	<span class="input-group-addon"  onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+form.PAYMENTTYPE.value+'&EDIT=EDIT');">
		   		<a href="#" data-toggle="tooltip" data-placement="top" title="Payment Type">
		   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      	</div></div>      	
      	</div>
 		 		
 		<div class="row">
 		 <div class="col-sm-2" style="padding:6px"><label for="Currency">&nbsp;Currency:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="20" id="currencyid"  MAXLENGTH=20 name="DISPLAY" value="<%=currencyid%>">
        <span class="input-group-addon"  onClick="javascript:popUpWin('list/ordcurencyLst.jsp?DISPLAY='+form.DISPLAY.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Currency Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        <input type="hidden" name="CURRENCY_ID">
        <input type="hidden" name="DESC">
        </div>
        
        <div class="col-sm-2" style="padding:6px"><label for="incoterm">&nbsp;INCOTERM:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT class="form-control" name="INCOTERMS" type="TEXT" value="<%=incoterms%>" style="width: 100%" MAXLENGTH=200>
        <span class="input-group-addon"  onClick="javascript:popUpWin('incoterms_list.jsp?INCOTERMS='+form.INCOTERMS.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="INCOTERM Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div></div>
        
        <div class="col-sm-2" style="padding:6px"><label for="Shipping Customer">&nbsp;Shipping Customer:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT id="SHIPPINGCUSTOMER" class="form-control" name="SHIPPINGCUSTOMER" type="TEXT" value="<%=su.forHTMLTag(shippingcustomer)%>"style="width: 100%" MAXLENGTH=40>
        <span id="sp2" class="input-group-addon"  onClick="javascript:popUpWin('shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Shipping Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div></div>
      	       
 		</div> 	
 		
 			
 		<div class="row">
 		
 		<div class="col-sm-2" style="padding:6px"><label  for="Order Type">Order Type:</label></div>
 		<div class="col-sm-2" style="padding:6px"> <div class="input-group">
        <INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20"	name="ORDERTYPE" value="<%=ordertype%>" />
        <span class="input-group-addon"  onClick="javascript:popUpWin('OrderType_list.jsp?ORDERTYPE='+form.ORDERTYPE.value+'&FORMTYPE='+form.formtype.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div></div>
  		
  		         <div class="col-sm-2" style="padding:6px"><label for="Remarks">Remark1:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT class="form-control" type="TEXT" style="width: 100%" MAXLENGTH="100" name="REMARK1" value="<%=remark1%>"/>
        <span class="input-group-addon"  onClick="javascript:popUpWin('remarks_list.jsp?REMARKS='+form.REMARK1.value+'&TYPE=REMARKS1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Remarks Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        <INPUT type="hidden" name="ADD2" value="<%=add2%>" />
        </div> 	
  		
  		<div class="col-sm-2" style="padding:6px"><label for="Remarks">Remark2:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
        <INPUT class="form-control" type="TEXT" style="width: 100%" MAXLENGTH="100" name="REMARK3" value="<%=remark2%>"/>
        <span class="input-group-addon"  onClick="javascript:popUpWin('remarks_list.jsp?REMARKS='+form.REMARK3.value+'&TYPE=REMARKS2');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Remarks Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        <INPUT type="hidden"  name="ADD3" value="<%=add3%>" />
		<INPUT type="hidden"  name="ADD4" value="<%=add4%>" />
        </div>   
        
        </div> 	 		
 		
	
	<div class="form-group">
	<div class="col-sm-6">
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "serialized" id="serialized" value="1" onchange="serial();">
                     <b>Serialized</b></lable>
	<lable class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name = "defualtQty" id="defualtQty" value="" onchange="DefaultQty();">
                    <b>  Default Quantity</b></lable>
	<INPUT Type=hidden  style="border:0;" name = "bulkcheckout" id="bulkcheckout" value="1" ></input>
        
	</div>
	</div>
	 
	 
	 <div class="row">	 
	 
	 <div class="col-sm-2" style="padding:6px">
	 <label for="Scan Product ID">Scan Product ID:</label></div>
	 <div class="col-sm-2" style="padding:6px">
      	    <div class="input-group">
    		<%-- <INPUT class="form-control" name="ITEM" type = "TEXT" id="item" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"
    		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){return Itemkeypress()}"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="itempopUpwin();"> --%>
			<INPUT class="form-control" name="ITEM" type="TEXT" id="item" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct();}" MAXLENGTH=50>
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('list/itemList.jsp?ITEM='+form.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		
  		
	 <div class="col-sm-2" style="padding:6px"><label for="Location">Location:</label></div>
 		<div class="col-sm-2" style="padding:6px"><div class="input-group">
    		<INPUT class="form-control" name="LOC" id="LOC" type = "TEXT" value="<%=LOC%>" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/locList.jsp?LOC_ID='+ form.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div></div>
  		
		 <div class="col-sm-2" style="padding:6px">
	 <label  for="Batch">Batch:</label></div>
	 <div class="col-sm-2" style="padding:6px">
      	    <div class="input-group">
    		<INPUT class="form-control" name="BATCH_0" id="BATCH_0" type="TEXT"
				value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){Batchkeypress();}"  
				MAXLENGTH="30"><input type="hidden" name="BATCH_ID_0" value="-1"/>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEM.value+'&LOC0='+form.LOC.value+'&BATCH0=&INDEX='+'0'+'&TYPE=GOODSISSUEWITHBATCH'+'&UOM='+form.UOM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Batch Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
    	
	 	</div>	 	
	 	<div class="row">
	 	
	 	
    	
	 	<div class="col-sm-2" style="padding:6px">
  		<label  for="Available Qty">Available Qty:</label></div>
        <div class="col-sm-2" style="padding:6px">
        <INPUT class="form-control" readonly name="AVAILQTY" type="TEXT" id="AVAILQTY"	value="<%=AVAILQTY%>" size="30" maxlength="50">
    	</div>
		<div class="col-sm-2" style="padding:6px">
    	<label  for="Qty">Qty:</label></div>
    	<div class="col-sm-1" style="padding:6px">
        <INPUT class="form-control" name="QTY" type="TEXT" id="qty"	value="" onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;" size="1" maxlength="10">
    	</div>
    	<div class="col-sm-1" style="padding:6px">
    	 <SELECT class="form-control" data-toggle="dropdown" data-placement="left" id="UOM" name="UOM" style="width: 100%" onchange="CheckPriceVal(this.value)">
			
					<%
				  ArrayList ccList = UomDAO.getUOMList(plant);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
	 </div>
	 	<div class="col-sm-2" style="padding:6px">
  		<label  for="Unit Price">Unit Price:</label></div>
  		<div class="col-sm-2" style="padding:6px">
        <div class="input-group">
        <%-- <INPUT class="form-control" name="UNITPRICE" type="TEXT" id="UNITPRICE" value="<%=unit_price%>" onchange="Updateprice()" onkeypress="return isNumberKey(event,this,6)"  MAXLENGTH="20" > --%>
        <INPUT class="form-control" name="UNITPRICE" type="TEXT" id="UNITPRICE" autocomplete="off" value="<%=unit_price%>" onchange="Updateprice(this.value)" onkeypress="return isNumberKey(event,this,6)" MAXLENGTH="20">
		<span class="input-group-btn">
		<button class="Submit btn btn-default" type="button" onClick="if((document.form.UNITPRICEDISCOUNT.value !=0.000 )&& (document.form.UNITPRICE.value <= document.form.UNITPRICEDISCOUNT.value)) {javascript:popUpWin('calcAmt.jsp?CURRENTAMOUNT='+form.UNITPRICE.value);}"><b>Discount</b></button></span>
			</div>
    	</div>
    	
    	<div class="col-sm-3" style="padding:6px;display: none"><label for="Location Description">Location Desc:</label></div>
 		<div class="col-sm-3" style="padding:6px;display: none"><div class="input-group">
        <INPUT class="form-control" name="LOCDESC" id="LOCDESC" type = "TEXT" value="<%=LOCDESC%>" size="30"  MAXLENGTH=100 readonly> 
    	</div> </div>
	 	</div>
	 	<input type="hidden" name="UNITPRICERD" value="<%=unit_price%>" id="UNITPRICERD">
		<!--  -->
	 	<INPUT type="Hidden" name="UNITPRICEDISCOUNT" value="<%=UNITPRICEDISCOUNT%>">
	 	<!--  -->
		<input type="hidden" name="QTY_0" value="">	
		<input type="hidden" name="ITEMDESC" value="">
			<input type="hidden" name="DISCITEM" value="" />
       <input type="hidden" name="INVOICECHECK" id="INVOICECHECK" value="<%=INVOICECHECK%>"/>
       <INPUT type="Hidden" name="DISCOUNTTYPE" id="DISCOUNTTYPE">
       <INPUT  type = "Hidden" name="CUSTOMERDISCOUNT" id="CUSTOMERDISCOUNT" >			

<table>
		<td ALIGN="left" >
			<div id="add">
			<button type="submit" class="Submit btn btn-default"  id="addbtn" name="action"  onClick="return addaction()"><b>Add</b></button>&nbsp;
			
			</div>
			<input type="hidden" name="action1" value="temp">
			</td>
			<td ALIGN="left" >
			<button type="submit" class="Submit btn btn-default" name="action"  onClick="return delaction()"><b>Delete Product</b></button>&nbsp;
			
			</td>
			<td ALIGN="left" style="visibility:hidden;">
			<button type="submit"  class="Submit btn btn-default" name="action" value="Hold" onClick="return holdaction()"><b>Hold</b></button>&nbsp;
			</td>
			
			
</table>
	<div class="row"><div class="col-sm-12">
	<table id="datatable-column-filter" class="table table-bordered table-hover dataTable no-footer">
	<thead>
		<TH style="width:5%;">Chk</TH>
		<TH style="width:8%;">Product ID</TH>
		<TH style="width:12%;">Product Description</TH>
		<TH style="width:6%;">Loaction</TH>
		<TH style="width:6%;">Batch</TH>
		<TH style="width:11%;">Unit Price</TH>
		<TH style="width:7%;">Quantity</TH>
		<TH style="width:5%;">UOM</TH>
		
	</thead>
	<% 
	  // 
	  if(poslist!=null && (poslist.size()>0)){
		  System.out.println("poslist.size"+poslist.size());
    	 for(int k=0;k<poslist.size();k++)
         {
        	 ITEMMST itemord = (ITEMMST)poslist.elementAt(k);
	         STOCKQTY = String.valueOf(itemord.getStkqty());
			 STOCKQTY = StrUtils.formatNum(STOCKQTY);
			 String pric = itemord.getUSERFLD6();
			 if(pric=="")
				 pric="0.00";
			 System.out.println(pric);
			 /*double prval = Double.valueOf(pric); 
			   BigDecimal bd = new BigDecimal(prval);
			 System.out.println(bd);
			 DecimalFormat format = new DecimalFormat("#.#####");		
			 format.setRoundingMode(RoundingMode.FLOOR);
			 priceval = format.format(bd); */
			  priceval=String.valueOf(pric);
			 double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
			 priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
			 
			if(cmd.equalsIgnoreCase("ViewTran") || cmd.equalsIgnoreCase("EditTran"))
			 {
				
				 //LOC = itemord.getLoc();
				 //LOCDESC = itemord.getLocDesc();
				 EMP_ID = itemord.getEmpNo();
				 //refNO = itemord.getRefNo();
				 TRANSACTIONDATE = itemord.getTranDate();
				 REASONCODE = itemord.getReasonCode();
				 //REMARKS = itemord.getRemarks();
				 EMP_NAME = itemord.getEmpName();
				 INVOICENO= itemord.getUSERFLD5();
				 System.out.println("INVOICENO : "+ INVOICENO);
			 }
			 
		%>
	<TR bgcolor="">
		<TD align="left" width="3%">&nbsp;<font class="textbold"><input
			type="checkbox" name="chk" value="<%=k%>"></input></TD>
		<TD align="left" width="7%">&nbsp;<font class="textbold"><%=itemord.getITEM()%></TD>
		<TD align="left" width="19%" class="textbold">&nbsp; <%=itemord.getITEMDESC()%></TD>		
		<TD align="left" width="15%" class="textbold">&nbsp; <%=itemord.getLoc()%></TD>
		<TD align="left" width="15%" class="textbold">&nbsp; <%=itemord.getBATCH()%></TD>
		<TD align="right" width="17%" class="textbold">&nbsp; <%=priceval%></TD>
		<TD align="right" width="8%" class="textbold">&nbsp;<%=STOCKQTY%></TD>
		<TD align="left" width="7%" class="textbold">&nbsp;<%=itemord.getSTKUOM()%></TD>
	</TR>
	<%
         
         }} %>
         </table>
         </div>
         </div>
       
<br>
<table border = "0" width = "100%" cellspacing="0" cellpadding="0" align="center">
	<tr>
	<th  class="productlabel" style="font-size: 15px;" ALIGN="left">Employee ID:</th>
		<td>
			<div class="input-group col-sm-11 productdiv">
					<INPUT class="form-control" name="EMP_ID" id="EMP_ID" type = "TEXT" value="<%=EMP_ID%>" size="20"  MAXLENGTH=20>
		            <span   class="input-group-addon " onClick="javascript:popUpWin('employee_list.jsp?EMP_ID='+form.EMP_ID.value+'&TYPE=KITDEKIT&FORM=form');"> 
		            <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
		      	
		      </div>
		</td>
	<th class="productlabel" style="font-size: 15px;" ALIGN="left">Employee Name:</th>
		<td>
			<div class="input-group col-sm-9 productdiv">
					<INPUT class="form-control" name="EMP_NAME" id="EMP_NAME" type = "TEXT" value="<%=EMP_NAME%>" size="20"  MAXLENGTH=20>
		            <span   class="input-group-addon " onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.EMP_NAME.value+'&TYPE=KITDEKIT&FORM=form');"> 
		            <a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
		            <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
		      		
					<input type="hidden" name="EMP_LNAME" value="" />
		      </div>
		</td>	


<TH class="productlabel" style="font-size: 15px;" ALIGN="right">Reference No:</TH>
<td>
<div class="input-group col-sm-11 productdiv">
<INPUT class="form-control" name="REFERENCENO" type="TEXT" id="REFERENCENO" value="<%=refNO%>" size="20" MAXLENGTH=20>
</div>
</TD>
<th class="productlabel" style="font-size: 15px;" ALIGN="left">Reason Code:</th>
		<td>
			<div class="input-group col-sm-9 productdiv">
				<INPUT class="form-control" name="REASONCODE" id="REASONCODE" type="TEXT" value="<%=REASONCODE%>" size="20" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateRsncode();}"> 
			 <span   class="input-group-addon " onClick="javascript:popUpWin('list/ReasonMstList.jsp?ITEM_ID='+form.REASONCODE.value+'&TYPE=KITDEKIT');">	
			 <a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details">
			 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
			</div>
		</td>	
</tr>

</table>
<br>

		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" id="back" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" id="editback" class="Submit btn btn-default" onClick="window.location.href='viewdynamicprdswithprice.jsp'"><b>Back</b></button>&nbsp;&nbsp;
		<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; --> 
			<button type="button" class="Submit btn btn-default" name="action" onClick="return printaction()"><b>Submit</b></button>&nbsp;&nbsp;
			<button type="submit" id="deleteall" class="Submit btn btn-default" name="action"  onClick="return delallaction()"><b>Delete</b></button>&nbsp;
			</div>
			
			</div>   
</FORM>
</div></div></div>

       	
<% if(serialized.equalsIgnoreCase("1")) {%>
<script type="text/javascript">
      document.getElementById("serialized").checked==true
	  document.getElementById("defualtQty").checked=true
      document.getElementById("defualtQty").disabled = true;
	  document.getElementById("qty").readOnly = true;
	  document.getElementById("qty").value=1;
</script>
<%}else{ %>
<script type="text/javascript">
 	document.getElementById("defualtQty").checked=false;
	document.getElementById("defualtQty").disabled = false;
	document.getElementById("qty").readOnly = false;
	var qtyValue = document.getElementById("qty").value;
	 document.getElementById("qty").innerHTML =qtyValue;
</script>
<%}%>

<script type="text/javascript">
//function itempopUpwin(){
	  //var loc = document.form.LOC.value;	
		
	  
	  /* if(loc==null||loc=="")
	  {
		  alert("Please Enter Location!");
		  document.form.LOC.focus();
		  return false;
	  }else{ */
		
		
		 //popUpWin('list/itemList.jsp?ITEM='+form.ITEM.value+'&LOC0='+loc+'&FORMTYPE=taxinvoice');
		
	  //}
	
	//}
$(document).ready(function() {
// 		$("#UNITPRICE").bind("change paste keyup", function() {
// 			var x = event.keyCode;
// 			if(x != 13)
// 				{
// 					Updateprice($(this).val());
// 				}
// 		});
	document.getElementById("qty").value =1;
	
	if (localStorage.getItem("defQtyChk") == true || localStorage.getItem("defQtyChk") == true){
		document.getElementById("defualtQty").checked=localStorage.getItem("defQtyChk");
		if(document.getElementById("defualtQty").checked==true){
			document.getElementById("qty").readOnly = true;
			
			}
	}
	
	
	<%if ("1".equals(request.getParameter("CashCust"))){%>
	$('#CashCust').prop('checked', 'checked');
	Cash();
<%}%>

	<%if ("1".equals(request.getParameter("serialized"))){%>
		$('#serialized').prop('checked', 'checked');
		serial();
	<%}%>
	<%
	if ("1".equals(request.getParameter("defualtQty"))){%>
		$('#defualtQty').prop('checked', 'checked');
	<%}%>
	<%if ("1".equals(request.getParameter("bulkcheckout"))){%>
		$('#bulkcheckout').prop('checked', 'checked');
	<%}%>	
	$('.datatable').dataTable({
	sDom: "R"+
			
			" <'col-sm-12'f>",
				 "bPaginate": false,
				 "bInfo": false,
				 "oLanguage": {"sEmptyTable": " No data available"}
   	});
   	
   	
   	
$(".resizetable").colResizable({
	liveDrag:true, 
	gripInnerHtml:"<div class='grip'></div>", 
	draggingClass:"dragging", 
    resizeMode:'fit'
});     
});
</script>
<!-- <script src="assets/js/jquery.dataTables.min.js"></script>	 -->
	<script src="assets/js/dataTables.colVis.bootstrap.js"></script>	
	<script src="assets/js/dataTables.colReorder.min.js"></script>	
	<script src="assets/js/dataTables.tableTools.min.js"></script>	
	<script src="assets/js/dataTables.bootstrap.js"></script>
	<script src="assets/js/colResizable-1.6.js"></script>
<script type="text/javascript">
$(window).on('beforeunload', function() {
	
	
	
	localStorage.setItem("defQtyChk",document.getElementById("defualtQty").checked );
	
    
	localStorage.setItem("qtyVal",document.getElementById("qty").value);
	 
});

  setfocus();var index=0;
  /*if( document.form.ITEM.value.length>0)
	{
			alert("setting focus");
		 
	  }*/
	  
	  function validateProduct(){
		  var itemValue = document.getElementById("item").value;
		  var currencyid = document.getElementById("currencyid").value;
		  var custCode = document.getElementById("custCode").value;
		  var urlStr = "/track/ItemMstServlet";
			 var discount;
				$.ajax( {
					type : "POST",
					url : urlStr,
					data : {
						ITEM : itemValue,
						ITEM_DESC:"",
						CURRENCY:currencyid,
						CUST_CODE:custCode,
						PLANT:"<%=PLANT%>",          
						ACTION: "PRODUCT_UNITPRICE"
						},
						dataType : "json",
						success : function(data) {
							//outgoingOBDiscount
							if (data.status == "100") {
								var resultVal = data.result;
								var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
								
								Convertedmprice=resultVal.minSellingConvertedUnitCost;
								
								if(resultVal.UNITPRICE == null || resultVal.UNITPRICE == undefined || resultVal.UNITPRICE == 0){
									document.form.UNITPRICE.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
									document.form.UNITPRICERD.value = "0.00000";
								}else{
									 document.form.UNITPRICE.value = parseFloat(resultVal.UNITPRICE).toFixed(<%=numberOfDecimal%>);
									 document.form.UNITPRICERD.value = resultVal.UNITPRICERD.match(regex)[0];
								}
								
								if(resultVal.outgoingOBDiscount=='' || resultVal.outgoingOBDiscount=='0' ||resultVal.outgoingOBDiscount=='0.00'||resultVal.outgoingOBDiscount==undefined)
								{

									 document.form.CUSTOMERDISCOUNT.value="0.00000";

									document.form.UNITPRICEDISCOUNT.value = resultVal.UNITPRICE;
								}
								else
								{
									if(resultVal.OBDiscountType=="BYPERCENTAGE")
									{

										document.form.DISCOUNTTYPE.value ="BYPERCENTAGE";

										document.form.CUSTOMERDISCOUNT.value = resultVal.outgoingOBDiscount.match(regex)[0];
										discount = parseFloat((resultVal.UNITPRICE*resultVal.outgoingOBDiscount)/100);
										price = parseFloat(resultVal.UNITPRICE-((resultVal.UNITPRICE*resultVal.outgoingOBDiscount)/100));
									}
									else
									{

										document.form.CUSTOMERDISCOUNT.value = resultVal.outgoingOBDiscount.match(regex)[0];

										price = parseFloat(resultVal.outgoingOBDiscount);
									}
									var calAmount = parseFloat(price).toFixed(<%=numberOfDecimal%>);
									document.form.UNITPRICE.value = calAmount.match(regex)[0];
									document.form.UNITPRICERD.value =calAmount.match(regex)[0];
									document.form.UNITPRICEDISCOUNT.value =  calAmount;
								}
							} 
						}
					});
				GetUOM();
		 		 getAvaliableInventoryQty();
			}
  function Updateprice(str){
		if (str.indexOf('.') == -1) str += ".";
		var decNum = str.substring(str.indexOf('.')+1, str.length);
		var disprice = document.form.UNITPRICEDISCOUNT.value;
		if(disprice==0)
		{
			var price = document.form.UNITPRICE.value;
			document.form.UNITPRICEDISCOUNT.value=price;
		}
		if (decNum.length > <%=numberOfDecimal%>)
		{
			alert("Invalid more than <%=numberOfDecimal%> digits after decimal in Unit Price");
			document.form.UNITPRICE.focus();
			return false;
			
		}
		else
		{
		var price = document.form.UNITPRICE.value;		   
		document.getElementById("UNITPRICERD").value =price;
		}
	}
	  
	  /* function Updateprice()
	  {
		  var price = document.form.UNITPRICE.value;
		   
		  document.getElementById("UNITPRICERD").value =price;
	  } */
  function setfocus()
  {	 		  
	  document.form.TRANID.focus();
	 // DisplayBatch();
  }
  function Itemkeypress()
    {
	   
      if(document.getElementById("chkbatch").checked == true){
	  if( document.form.ITEM.value.length>0)
	  {
		  document.getElementById("Add").focus();
	  }
      }
      else{document.form.LOC.focus();
		return false;
      }   

  }
  function addaction()
  {
	 var empname =document.form.EMP_NAME.value;
	  var item = document.form.ITEM.value;	
	  var loc = document.form.LOC.value;	
	  var tranid = document.form.TRANID.value;	
	  var scanqty = document.form.QTY.value;	
	  var availqty = document.form.AVAILQTY.value;
	  var Value=document.form.UNITPRICE.value;
	  var uprice= parseFloat(document.getElementById('UNITPRICE').value);
	  var Convertedmsprice = parseFloat(Convertedmprice);
	  
	  if((tranid==null||tranid=="")&&(loc==null||loc==""))
	  {
		  alert("Please create Transaction ID & select location before adding product!");
		  document.form.TRANID.focus();
		  return false;
	  }
	  if(tranid==null||tranid=="")
	  {
		  alert("Please Enter Tran Id!");
		  document.form.TRANID.focus();
		  return false;
	  }
	  if(loc==null||loc=="")
	  {
		  alert("Please Enter Location!");
		  document.form.LOC.focus();
		  return false;
	  }
	  	  if(document.form.UOM.value == ""){
		    alert("Please enter a UOM value");
		    document.form.UOM.focus();
		    document.form.UOM.select();
		    return false;
	}
	  if(item==null||item=="")
	  {
		  alert("Please Scan Product ID!");
		  document.form.ITEM.focus();
		  return false;
	  }
	  if(scanqty==null||scanqty==""||scanqty=="0")
	  {
		  alert("Please Enter Qty!");
		  document.form.QTY.focus();
		  return false;
	  }
	  if(parseFloat(scanqty)>parseFloat(availqty)){
	     	alert("Scanning Quantity Should not be Greater than Availabe Quantity");
		  	document.form.QTY.focus();
	  	    return false;
	  	   }
	  if(Value==null||Value==""){
	     	alert("Please Enter Unit Price");
		  	document.form.UNITPRICE.focus();
	  	    return false;
	  	   }
		   else if(uprice != '0' && uprice < Convertedmsprice){      
			
			alert("Price should not be less than minimum selling price");
	             document.form.UNITPRICE.focus();
			return false;
		}
		 var priceamt = document.form.UNITPRICE.value;
	  if (priceamt.indexOf('.') == -1) priceamt += ".";
		var decNum = priceamt.substring(priceamt.indexOf('.')+1, priceamt.length);
		if (decNum.length > <%=numberOfDecimal%>)
		{
			alert("Invalid more than <%=numberOfDecimal%> digits after decimal in Unit Price");
			document.form.UNITPRICE.focus();
			return false;
			
		}
	  if (!validToThreeDecimal(document.getElementById("GST").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Vat");
			form.GST.focus();
			return false;
		} 
		else if (!validDecimal(document.getElementById("SHIPPINGCOST").value,<%=numberOfDecimal%>)) {
		alert("Not more than "+<%=numberOfDecimal%>+" digits are allowed after decimal value in Shipping Cost");
		document.form.SHIPPINGCOST.focus();
		return false;
	}
		
		else if (!validToThreeDecimal(document.getElementById("ORDERDISCOUNT").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Order Discount");
			document.form.ORDERDISCOUNT.focus();
			return false;
		}
		 document.form.cmd.value="ADD";
		 document.form.action  = "/track/DynamicProductServlet?cmd=ADD";
		 document.form.submit();
		 return false;
  }
  
	  function Batchkeypress()
	  {
		  if(document.getElementById("chkbatch").checked == false){
			  if( document.form.BATCH_0.value.length>0)
			  {
				  document.getElementById("Add").focus();
			  }
			  
		  }
	  }
	  
 
   function delaction()
  {
	   var item = document.form.chk;
		  var empid =document.getElementById("EMP_ID").value;
		   var empname =document.getElementById("EMP_NAME").value;
			
			  var loc = document.getElementById("LOC").value;	
			  var qty =document.getElementById("qty").value;	
			  var refno = document.getElementById("REFERENCENO").value;
			  var rsncode = document.getElementById("REASONCODE").value;	
			  //var remarks = document.getElementById("REMARKS").value;
			  //var exprydate = document.getElementById("EXPIREDATE").value;
			  var trnsdate = document.getElementById("TRANSACTIONDATE").value;
	
	  var item = document.form.chk;
	  var checkflag = false;
	  if(item != undefined){
		 if(item.length == undefined && item.checked){
		 	 checkflag = true;	  
	    	}
	  	 else if(item.length > 0){
		 	 for(i=0;i<item.length;i++){
			 	 if(item[i].checked){
				  	checkflag = true;	 
			  	}
		 	 }
	       } 
	  }
	  if(!checkflag){
			alert("Must Select at least one Product which you want to Delete");
			return false;
		}
	  document.form.cmd.value="Delete" ;
	  document.form.action  = "/track/DynamicProductServlet?cmd=Delete";
	  /* document.form.submit(); */
	  return true;
	 }     
  
  function printaction(){
	  
	var item=document.form.chk;
	 var itemvalue=document.getElementById("item").value;
	  var RecvtranID =document.getElementById("TRANID").value;
	  var qtyValue=document.form.qty.value;
	  if(item == undefined){
		 alert("Add Product to Print");
		 return false;
	  }
	  var tranid=document.form.TRANID.value;
	  if(tranid==null||tranid==""){
		 alert("Generate Transaction Id");
		 return false;
	  }
	  
	 if(document.getElementById("CashCust").checked)
	 {
		
	 }
	 else
		 { 
		 var custid=document.form.CUST_CODE1.value;
		 
		 if(custid==null||custid==""){
			 
			 alert("Please Enter Customer");
			 return false;
		  
		 }
		 if(custid!="CASH")
			{
			  if (form.CUST_NAME.value.length < 1)
			  {
			    alert("Please Enter Customer !");
			    form.CUST_NAME.focus();
			    return false;
			  }
			}
		 }
	  if(!IsNumeric(form.GST.value))
	  {
	    alert(" Please Enter valid  VAT !");
	    form.GST.focus();  form.GST.select(); return false;
	  }
	   
	  if(!IsNumeric(form.ORDERDISCOUNT.value))
	  {
	    alert(" Please Enter valid Order Discount!");
	    form.ORDERDISCOUNT.focus(); 
	    form.ORDERDISCOUNT.select(); 
	    return false;
	  }
	  if(!IsNumeric(form.SHIPPINGCOST.value))
	  {
	    alert(" Please Enter valid Shipping Cost!");
	    form.SHIPPINGCOST.focus(); 
	    form.SHIPPINGCOST.select(); 
	    return false;
	  }
	  if (!validToThreeDecimal(document.getElementById("GST").value)) {
  		alert("Not more than 3 digits are allowed after decimal value in Vat");
  		form.GST.focus();
  		return false;
  	} 
  	else if (!validDecimal(document.getElementById("SHIPPINGCOST").value)) {
  	alert("Not more than 5 digits are allowed after decimal value in Shipping Cost");
  	document.form.SHIPPINGCOST.focus();
  	return false;
  }
  	else if (!validDecimal(document.getElementById("ORDERDISCOUNT").value)) {
  		alert("Not more than 5 digits are allowed after decimal value in Order Discount");
  		document.form.ORDERDISCOUNT.focus();
  		return false;
  	} 
	  
	        if (form.DISPLAY.value.length < 1)
		    {
		    	alert("Please Select Currency ID");
		    	form.DISPLAY.focus();
	                form.DISPLAY.select();
		    	return false;
				    
		    }

	        var formData = $('form#dynamicprdswithpriceForm').serialize();
	        var invval = document.form.INVOICENO.value.length;
	   	 if(invval>0)
	   	 {
				$.ajax({
					type : 'get',
					url : '/track/DynamicProductServlet?cmd=printproductwp',
					dataType : 'html',
					responseType: 'arraybuffer',
					data : formData,

					success : function(data, status, xhr) {
			var result = data.split(":");
			if(result[0] == "Success" && result[2] == "1")
			{
						document.form.action="/track/DynamicProductServlet?";
		 	         	document.form.cmd.value="printproductwp" ;
		 	         	document.form.RCPNO.value=result[1];
						document.form.submit();
						onNewPOS();
						setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Issue Successfully."); }, 1000);
			}
			else if(result[0] == "Success" && result[2] == "0"){
				onNewPOS();
				setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Issue Successfully."); }, 1000);
			}
			else{
				$('#appendbody').html(data);
				setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Issue Successfully."); }, 1000);
			}
					

					},
					error : function(data) {

						alert(data.responseText);
					}
				});
	 
	  return false;
	   	}
		 else
			 {
			 if (confirm('Are you sure to submit this transaction without InvoiceNo?')) {
				 $.ajax({
						type : 'get',
						url : '/track/DynamicProductServlet?cmd=printproductwp',
						dataType : 'html',
						responseType: 'arraybuffer',
						data : formData,

						success : function(data, status, xhr) {
				var result = data.split(":");
				if(result[0] == "Success" && result[2] == "1")
				{
							document.form.action="/track/DynamicProductServlet?";
			 	         	document.form.cmd.value="printproductwp" ;
			 	         	document.form.RCPNO.value=result[1];
							document.form.submit();
							onNewPOS();
							setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Issue Successfully."); }, 1000);
				}
				else if(result[0] == "Success" && result[2] == "0"){
					onNewPOS();
					setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Issue Successfully."); }, 1000);
				}
				else{
					$('#appendbody').html(data);
					setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " Goods Issue Successfully."); }, 1000);
				}
						

						},
						error : function(data) {

							alert(data.responseText);
						}
					});
		 
		  return false;
			 } else {
				 document.getElementById("INVOICENO").focus();
			 }		 
			 }
	  
  }
				
  function getCookie(cookieName){
	  var cookieArray = document.cookie.split(';');
	  for(var i=0; i<cookieArray.length; i++){
	    var cookie = cookieArray[i];
	    while (cookie.charAt(0)==' '){
	      cookie = cookie.substring(1);
	    }
	    cookieHalves = cookie.split('=');
	    if(cookieHalves[0]== cookieName){
	      return cookieHalves[1];
	    }
	  }
	  return "";
	}

 
	function showObject(obj){
		document.getElementById(obj).style.display = "inline";
	}
	function hideObject(obj){
		document.getElementById(obj).style.display = "none";
	}

	
	function contains(arr, findValue) {
	    var i = arr.length;
	     
	    while (i--) {
	        if (arr[i] === findValue) return true;
	    }
	    return false;
	}
	
	function holdaction(){
		var RecvtranID =document.getElementById("TRANID").value;
		  var item=document.form.chk;
		  if(item == undefined){
			  alert("Add Product to Hold the Transaction");
			  return false;
		  }
		  var formData = $('form#dynamicprdswithpriceForm').serialize(); 
		    $.ajax({
		        type: 'get',
		        url: '/track/DynamicProductServlet?action=Hold',
		      //  url: '/track/DynamicProductServlet?action=ADD&CHKBATCH='+batchstatus,
		        dataType:'html',
		        data:formData,
		       
		        success: function (data) {
		        	$('#appendbody').html(data); 
		        	onNewPOS();
		        	setTimeout(function(){ $('#appenddiv').html("Transaction "+  RecvtranID + " hold successfully."); }, 1000);
		        },
		        error: function (data) {
		        	
		            alert(data.responseText);
		        }
		    });
		    return false;
		  
	  }
 	function onGenID()
		{
		  document.form.cmd.value="TINEWTRANID" ;
		  document.getElementById("qty").value=1;
		  $('#defualtQty').prop('checked', false);
		  document.form.action  = "/track/DynamicProductServlet?cmd=TINEWTRANID";
		  document.form.submit();
		}
 	
	function ClearonNew(){
		 document.form.LOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     //document.form.REASONCODE.value="";
	     //document.form.TRANSACTIONDATE.value="";
	     //document.form.REMARKS.value="";
	    // document.form.EXPIREDATE.value="";
	     document.form.REFERENCENO.value="";
	     document.form.LOCDESC.value="";
	}
	function onNewPOS()
	{    
	     document.form.TRANID.value="";
	     document.form.LOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     document.form.REASONCODE.value="";
	     document.form.TRANSACTIONDATE.value="";
	     //document.form.REMARKS.value="";
	     document.form.REFERENCENO.value="";
	     document.form.LOCDESC.value="";
	     
	     form.PERSON_INCHARGE.value ="";
		  form.CONTACT_NUM.value     ="";
	     form.REMARK1.value         ="";
		  form.REMARK2.value         ="";
		  form.REMARK3.value         ="";
		  form.CUST_NAME.value         ="";		  
		  form.TELNO.value         ="";
		  form.EMAIL.value         ="";
		  form.ADD1.value         ="";
		  form.ADD2.value         ="";
		  form.ADD3.value         ="";
		  form.ADD4.value         ="";
		  form.COUNTRY.value         ="";
		  form.ZIP.value         ="";	      
	      form.ORDERTYPE.value="";	      
	      form.DELIVERYDATE.value="";
		  form.SHIPPINGID.value="";
		  form.SHIPPINGCUSTOMER.value="";
		  form.ORDERDISCOUNT.value="";
		  form.SHIPPINGCOST.value="";
		  form.INCOTERMS.value="";
		  form.EDIT.value="";
		  form.INVOICENO.value="";
		  form.CUSTOMERTYPEDESC.value="";
		  form.PAYMENTTYPE.value="";
		 document.form.cmd.value="NewPOS" ;
	 
			var formData = $('form#dynamicprdswithpriceForm').serialize(); 

		    $.ajax({
		        type: 'get',
		        url: '/track/DynamicProductServlet?cmd=NewPOS',
		        dataType:'html',
		        data:  formData,
		       
		        success: function (data) {
		        	$('#appendbody').html(data); 
		            
		        },
		        error: function (data) {
		        	
		            alert(data.responseText);
		        }
		    });
		    // document.form.submit();
	}

	  function onView()
		{
	       var tranid = document.form.TRANID.value;	 	
		  if(tranid==null||tranid=="")
		  {
			  alert("Please Select TranID");
			  return false;
		  }else{ 
			  
			   document.form.cmd.value="ViewTran" ;
			   document.form.action  = "/track/DynamicProductServlet?cmd=ViewTran";
			   document.form.submit();
		  }
			   
	       
		}
	  function onEdit()
		{
	       var tranid = document.form.TRANID.value;	 	
		  if(tranid==null||tranid=="")
		  {
			  alert("Please Select TranID");
			  return false;
		  }else{ 
			  
			   document.form.cmd.value="EditTran" ;
			   document.form.action  = "/track/DynamicProductServlet?cmd=EditTran";
			   document.form.submit();
		  }
			   
	       
		}
	  $(document).ready(function() {
		  $(document).on('keydown','form input',function(event){
			 // alert("");
		    if(event.keyCode == 13) {
		      event.preventDefault();
		      return false;
		    }
		  });
		 
		  var elemItem = document.getElementById("item");
		  elemItem.onkeyup = function(e){
			  
			  
			  var prodId = document.form.item.value;
			  var key = e.which;
			  if(key == 13)  // the enter key code
			   {
				  
				  if(prodId==null||prodId=="" || prodId.trim()=="")
				  {
					  alert("Please Scan Product ID!");
					  document.getElementById("item").focus();
					  
					
					  return false;
				  }
				  else{
					  //getUNITPRICE();
					  validateProduct();
					  GetUOM();
					  getAvaliableInventoryQty();
					  $( "#LOC" ).focus();
						 $("#LOC").select();
				  }
			   }
			 } 
		  var elemLOC = document.getElementById("LOC");
		  elemLOC.onkeyup = function(e){
			  var loc = document.form.LOC.value;
			  var prodId = document.form.item.value;
			  var key = e.which;
			  if(key == 13)  // the enter key code
			   {
			  if(loc==null||loc==""||loc.trim()=="")
			  {
				  
				  document.getElementById("LOC").focus();
				  alert("Please Enter Location!");
				
				  return false;
			  }
			  else  if(prodId==null||prodId=="" || prodId.trim()=="")
			  {
				  alert("Please Scan Product ID!");
				  document.getElementById("item").focus();
				  
				
				  return false;
			  }
			  else
				  {  
				  get_LOC();
				  $( "#BATCH_0" ).focus();
					$("#BATCH_0").select();
				 
				  }
			   }
		  }
		  var elemBATCH_0 = document.getElementById("BATCH_0");
		  elemBATCH_0.onkeyup = function(e){
			  var loc = document.form.LOC.value; 
			  var prodId = document.form.item.value;
			  var key = e.which;
			  if(key == 13)  // the enter key code
			   {
				  if(prodId==null||prodId=="" || prodId.trim()=="")
				  {
					  alert("Please Scan Product ID!");
					  document.getElementById("item").focus();
					  
					
					  return false;
				  }
				  if(loc==null||loc==""||loc.trim()=="")
				  {
					  
					  document.getElementById("LOC").focus();
					  alert("Please Enter Location!");
					
					  return false;
				  }else{					  
					  GetUOM();
					  getAvaliableInventoryQty();
					  $( "#qty" ).focus();
						$("#qty").select();
				  }
				  }
		  }
		  var elem2 = document.getElementById("qty");
		  elem2.onkeyup = function(e){
		      if(e.keyCode == 13){
		    	 
		    	  $( "#UNITPRICE" ).focus();
					$("#UNITPRICE").select();
		      }
		  }
		  var elemUNITPRICE = document.getElementById("UNITPRICE");
		  elemUNITPRICE.onkeyup = function(e){
			  
			  var loc = document.form.LOC.value; 
			  var prodId = document.form.item.value;
			  var price = document.form.UNITPRICE.value;
			  var key = e.which;
			  if(key == 13)  // the enter key code
			   {
				  if(prodId==null||prodId=="" || prodId.trim()=="")
				  {
					  alert("Please Scan Product ID!");
					  document.getElementById("item").focus();
					  
					
					  return false;
				  }
				  if(price==null||price=="" || price.trim()=="")
				  {
					  alert("Please Enter Unit Price!");
					  document.getElementById("UNITPRICE").focus();
					  
					
					  return false;
				  }
				  if(loc==null||loc==""||loc.trim()=="")
				  {
					  
					  document.getElementById("LOC").focus();
					  alert("Please Enter Location!");
					
					  return false;
				  }else{
					  
					  
				  //if (document.getElementById("defualtQty").checked==true ) {
 					  addaction();
				  /* }else{
					  $( "#qty" ).focus();
					$("#qty").select();
				  } */
				  }
			   }
		  }
	
		 
		  		 
		  
		  
		
		});
	  function DefaultQty(){
			 if (document.getElementById("defualtQty").checked==true ) {
				 document.getElementById("qty").readOnly = true;
				 var qtyValue=document.getElementById("qty").value;
				 document.getElementById("qty").innerHTML =qtyValue;
			 }
			 else{
				 document.getElementById("qty").readOnly = false;
				 document.getElementById("qty").value=1;
			 }
		 }
	  function Cash(){
		  if (document.getElementById("CashCust").checked==true ) {
			  document.getElementById("CUST_NAME").disabled = true;
			  document.getElementById("SHIPPINGCUSTOMER").disabled = true;
			  $("#sp1").css("pointer-events", "none");
			  $("#sp2").css("pointer-events", "none");
			  form.SHIPPINGCUSTOMER.value ="";
			  form.SHIPPINGID.value     ="";
			  form.PERSON_INCHARGE.value ="";
			  form.CONTACT_NUM.value     ="";
			  form.CUST_CODE1.value     ="";
			  form.CUST_NAME.value         ="";		  
			  form.TELNO.value         ="";
			  form.EMAIL.value         ="";
			  form.ADD1.value         ="";
			  form.ADD2.value         ="";
			  form.ADD3.value         ="";
			  form.ADD4.value         ="";
			  form.COUNTRY.value         ="";
			  form.ZIP.value         ="";
			  CashCust ="1";
		  }
		  else{
			  document.getElementById("CUST_NAME").disabled = false;
			  document.getElementById("SHIPPINGCUSTOMER").disabled = false;
			  $("#sp1").css("pointer-events", "auto");
			  $("#sp2").css("pointer-events", "auto");
			  CashCust ="";
		  }
	  }
		  function serial(){

			  if ( document.getElementById("serialized").checked==true ) {
				  
				  document.getElementById("defualtQty").checked=true
			        document.getElementById("defualtQty").disabled = true;
				  document.getElementById("qty").readOnly = true;
				  document.getElementById("qty").value=1;
			    } else {
			    	document.getElementById("defualtQty").checked=false;
			        document.getElementById("defualtQty").disabled = false;
				  document.getElementById("qty").readOnly = false;
				  var qtyValue = document.getElementById("qty").value;
				  document.getElementById("qty").innerHTML =qtyValue;
			    }
		  }
		  
		  function getAvaliableInventoryQty()
		  {
			  var itemValue = document.getElementById("item").value;
			  var locValue = document.getElementById("LOC").value;
			  var batch = document.getElementById("BATCH_0").value;	 
			  var uom = document.getElementById("UOM").value;
				  var urlStr = "/track/ItemMstServlet";
				  
				  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"",LOC:locValue,BATCH:batch,UOM:uom, ACTION: "PRODUCT_LIST_WITH_INVENTORY_QUANTITY_MUTIUOM",PLANT:"<%=PLANT%>"},dataType: "json", success: onGetInventoryQty });
				  
				  

		  }
		  function onGetInventoryQty(data){
			  
			  var errorBoo = false;
				$.each(data.errors, function(i,error){
					if(error.ERROR_CODE=="99"){
						errorBoo = true;
						document.getElementById("AVAILQTY").value=0;
					
					}
				});
				
				if(errorBoo == false){
		        	//console.log(data.result.qty);
		        	document.getElementById("AVAILQTY").value = data.result.QTY;
		        }
			  
		  }
		  function getUNITPRICE()
		  {
			  var itemValue = document.getElementById("item").value;
			 
			  var currencyid = document.getElementById("currencyid").value;
				  var urlStr = "/track/ItemMstServlet";				  
				  
			  
				  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"",CURRENCY:currencyid, ACTION: "PRODUCT_UNITPRICE",PLANT:"<%=PLANT%>"},dataType: "json", success: onGeUNITPRICE });
				  

		  }
 	function onGeUNITPRICE(data){
 		
			  var errorBoo = false;
				$.each(data.errors, function(i,error){					
					if(error.ERROR_CODE=="99"){
						errorBoo = true;
						document.getElementById("UNITPRICE").value=0;
						document.getElementById("UNITPRICERD").value=0;
					
					}
				});
				
				if(errorBoo == false){
					if(data.status=="99"){
						alert("Invalid Scan Product!");
						  document.getElementById("item").focus();	
						  return false;
					}
					else{		        	
		        	var amount= data.result.UNITPRICE;		        	
		        	var amountrd= data.result.UNITPRICERD;
		        	var amountinFloat=parseFloat(amount);
		        	var amountinFloatRD=parseFloat(amountrd);
		        	document.getElementById("UNITPRICE").value =amountinFloat;
		        	document.getElementById("UNITPRICERD").value =amountinFloatRD;
		        }
				}
			  
		  }
 	
 	function get_LOC()
	  {
		  var locc = document.getElementById("LOC").value;
		 
		
			  var urlStr = "/track/ItemMstServlet";				  
			  
		  
			  $.ajax({type: "POST",url: urlStr, data: { LOC: locc , ACTION: "GET_LOC",PLANT:"<%=PLANT%>"},dataType: "json", success: onget_LOC });
			  

	  }
	  function GetUOM()
	  {
		  var itemValue = document.getElementById("item").value;
		 
		
			  var urlStr = "/track/ItemMstServlet";				  
			  
		  
			  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"", ACTION: "PRODUCT_UOM",PLANT:"<%=PLANT%>"},dataType: "json", success: onGetUOM });
			  

	  }
 	 function onGetUOM(data){
  		
		  var errorBoo = false;
			$.each(data.errors, function(i,error){					
				if(error.ERROR_CODE=="99"){
					errorBoo = true;
					document.getElementById("UOM").value=0;
				
				}
			});
			
			if(errorBoo == false){
				if(data.status=="99"){
					alert("Invalid Scan Product!");
					  document.getElementById("item").focus();	
					  return false;
				}
				else{		 
					var salesuom= data.result.SALESUOM;
	        	document.getElementById("UOM").value =salesuom;
	        }
			}
		  
	  }
function onget_LOC(data){
		  var errorBoo = false;
			$.each(data.errors, function(i,error){					
				if(error.ERROR_CODE=="99"){
					errorBoo = true;
					document.getElementById("LOC").value="";
				
				}
			});
			
			if(errorBoo == false){
				if(data.status=="99"){
					alert("Invalid Location!");
					  document.getElementById("LOC").focus();	
					  return false;
				}
			
			}
		  
	  }
 	
		   var elem3 = document.getElementById("LOC");
			  elem3.onkeyup = function(e){
			      if(e.keyCode == 13){
			    	//  document.getElementById("item").focus(); 
			    		  var loc = document.form.LOC.value;	 
			    	  if(loc==null||loc=="")
			    	  {
			    		  alert("Please Enter Location!");
			    		  document.form.LOC.focus();
			    		  return false;
			    	  }
			    	 getLocation(elem3.value);
			      }
			  }
			   var elem4 = document.getElementById("EMP_ID");
				  elem4.onkeyup = function(e){
				      if(e.keyCode == 13){
				    	  getEmployeeName(elem4.value);
				    	  document.getElementById("REFERENCENO").focus(); 
				      }
				  }
				  var elem5 = document.getElementById("REFERENCENO");
				  elem5.onkeyup = function(e){
				      if(e.keyCode == 13){
				    	  document.getElementById("REASONCODE").focus(); 
				    	  document.getElementById("REASONCODE").select(); 
				      }
				  }
				  /* var elem6 = document.getElementById("REASONCODE");
				  elem6.onkeyup = function(e){
				      if(e.keyCode == 13){
				    	  document.getElementById("REMARKS").focus(); 
				      }
				  } */
				  var elem7 = document.getElementById("TRANID");
				  elem7.onkeyup = function(e){
				      if(e.keyCode == 13){
				    	    var tranid = document.form.TRANID.value;	 	
				  		  if(tranid==null||tranid=="")
				  		  {
				  			  alert("Please Select TranID");
				  			 document.getElementById("TRANID").focus(); 
				  			  return false;
				  		  }else{
				    	  document.getElementById("LOC").focus(); 
				  		  }
				      }
				  }
				  function getEmployeeName(empId)
				  {
					  
					  $.ajax( {
							type : "POST",
							url : "EmployeeName.jsp",
							data : {
							PLANT : "<%=PLANT%>",
							ACTION : "View",
							EMPID:empId
						},
						dataType : "json",
						success : function(data) {
							
							if(data.ERROR_CODE=="99")
								{
								
								$("#errorMessage").html("Employee Id Not exists");
								}
							else
								{
									$("#errorMessage").html("");
									document.getElementById("EMP_NAME").value = data.EMP_NAME;
								}
							
							
							
						},
						error:function(data)
						{
							
						}
					});
				  }
				 
				  function getLocation(loc)
				  {
					  var tranid = document.form.TRANID.value;	
					  $.ajax( {
							type : "get",
							url : '/track/DynamicProductServlet?action=GETLOC',
							data : {
							PLANT : "<%=PLANT%>",
							ACTION : "GETLOC",
							TRANTYPE:"GOODSISSUEWITHBATCHPRICE",
							LOC:loc
						},
						dataType : "html",
						success : function(data) {
							$("#appendbody").html(data);
						var errorValue =document.getElementById("iserrorVal").value;							
						
							document.getElementById("TRANID").value=tranid;
						 if(errorValue){
								 document.getElementById("LOC").focus(); 
							}
						 /* else{
								 document.getElementById("item").focus(); 
							}  */
						},
						error:function(data)
						{
							
						}
					});
				  }
				  
				  function isNumberKey(evt, element, id) {
					  var charCode = (evt.which) ? evt.which : event.keyCode;
					  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
						  {
					    	return false;
						  }
					  
					  		/* else {
					    var len = $(element).val().length;
					    var index = $(element).val().indexOf('.');
					    if (index > 0 && charCode == 46) {
					      return false;
					    }
					    if (index > 0) {
					      var CharAfterdot = (len + 1) - index;
					      if (CharAfterdot > id) {
					        return false;
					      }
					    }

					  } */
					  return true;
					}
				  function validDecimal(str,precision) {
						if (str.indexOf('.') == -1)
						str += ".";
						var declength = parseInt(precision);
						var decNum = str.substring(str.indexOf('.') + 1, str.length);
						if(decNum.length>declength){
							return false;
						}
						return true;
						
					}function validToThreeDecimal(str) {
						if (str.indexOf('.') == -1)
							str += ".";
						var decNum = str.substring(str.indexOf('.') + 1, str.length);
						 if(decNum.length>3){
							return false;
							
						}
						return true;
					}
				  var url_string = window.location.href;
				  var url = new URL(url_string);
				  var c = url.searchParams.get("EDIT");
				 
				  if(c=="EDIT")
					  {	  
					 
					  onEdit();
					  }
				  
				  function onNew()
				  {
				  	
				  	var urlStr = "/track/OutboundOrderHandlerServlet";
				  	$.ajax( {
				  		type : "POST",
				  		url : urlStr,
				  		data : {
				  		PLANT : "<%=plant%>",
				  		ACTION : "INVOICE"
				  		},
				  		dataType : "json",
				  		success : function(data) {
				  			if (data.status == "100") {
				  				var resultVal = data.result;				
				  				var resultV = resultVal.invno;
				  				document.form.INVOICENO.value= resultV;
								document.form.INVOICECHECK.value= resultV;
				  	
				  			} else {
				  				alert("Unable to genarate INVOICE NO");
				  				document.form.INVOICENO.value = "";
				  			}
				  		}
				  	});	
				  	}
				  function delallaction()
				  {
					   var item = document.form.chk;
						  var empid =document.getElementById("EMP_ID").value;
						   var empname =document.getElementById("EMP_NAME").value;
							
							  var loc = document.getElementById("LOC").value;	
							  var qty =document.getElementById("qty").value;	
							  var refno = document.getElementById("REFERENCENO").value;
							  var rsncode = document.getElementById("REASONCODE").value;
							  var trnsdate = document.getElementById("TRANSACTIONDATE").value;					
					  
					  document.form.cmd.value="DeleteAll" ;
					  document.form.action  = "/track/DynamicProductServlet?cmd=DeleteAll";
					  document.form.submit();
					 }
				  function CheckPriceVal(uom) {
						var productId = document.form.ITEM.value;
						var dono = document.form.DISPLAY.value;
					    var desc = document.form.DESC.value;
					    var disc = document.form.CUSTOMERDISCOUNT.value;
					    var Convertedmsprice = parseFloat(Convertedmprice);					    
					if((productId=="" || productId.length==0) && (desc == "" ||desc.length == 0)) {
						alert("Enter Product ID/Description !");
						document.form.ITEM.focus();
					}else{
						var urlStr = "/track/MiscOrderHandlingServlet";
						$.ajax( {
							type : "POST",
							url : urlStr,
							data : {
								ITEM : productId,
								DESC : "",
								PLANT : "<%=plant%>",
								PONO:dono,
					                            UOM:uom,
					                            TYPE:"DirectTax",
					                            DISC:disc,
					                            MINPRICE:Convertedmsprice,
								ACTION : "VALIDATE_PRODUCT_UOM"
								},
								dataType : "json",
								success : function(data) {
									
									if (data.status == "100") {
										var resultVal = data.result;
										var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
										Convertedmprice=resultVal.MinPriceWTC;
										if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
											document.form.UNITPRICE.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
											document.form.UNITPRICERD.value = "0.00000";
										}else{
											document.form.UNITPRICE.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
											document.form.UNITPRICERD.value = resultVal.ConvertedUnitCostWTC.match(regex)[0];
										}
										//alert(disc);
										if(disc!="0.00000")
					                    {
					                    	if(document.form.DISCOUNTTYPE.value=="BYPERCENTAGE")
											{								 
												var getdist = disc;								
												discount = parseFloat((resultVal.ConvertedUnitCost*getdist)/100);
												price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*getdist)/100));
											}
											else
											{								
												price = parseFloat(resultVal.ConvertedDiscWTC);
					 						}
					                    	var calAmount = parseFloat(price).toFixed(<%=numberOfDecimal%>);
											document.form.UNITPRICE.value = calAmount;
											document.form.UNITPRICERD.value =calAmount.match(regex)[0];
											document.form.UNITPRICEDISCOUNT.value =  calAmount;
											
					                    }
									 	document.form.UOM.focus();
									} else {
										document.form.ITEM.value = "";
										document.form.ITEM.focus();
									}
								}
							});
					}
					getAvaliableInventoryQty();
					}
	
</script>
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();

    var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Sales " + d +" :";
});
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>