<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>


<%
String title = "Create Currency";
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String plant = (String) session.getAttribute("PLANT");
//CREATED BY NAVAS
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<jsp:useBean id="countryNCurrencyDAO"  class="com.track.dao.CountryNCurrencyDAO" />

<SCRIPT LANGUAGE="JavaScript">


function onNew(){
	document.form1.CURRENCY_ID.value = ""; 
	document.form1.DESC.value = "";
	document.form1.DISPLAY.value = ""; 
	document.form1.BASECURRENCY.value = "";
	document.form1.CURREQT.value = ""; 
	document.form1.REMARK.value = "";
  /*  document.form1.action  = "currencyMst.jsp?action=Clear";
   document.form1.submit(); */
}
function onAdd(){
    var CURRENCYID   = document.form1.CURRENCY_ID.value;
    var DISPLAY   = document.form1.DISPLAY.value;
    var CURREQT   = document.form1.CURREQT.value;
    if(CURRENCYID == "0" || CURRENCYID == null) {alert("Please Select Currency ID"); document.form1.CURRENCY_ID.focus();return false; }
    if(DISPLAY == "" || DISPLAY == null) {alert("Please Enter Display for Currency ID"); document.form1.DISPLAY.focus(); return false; }
    if(CURREQT == "" || CURREQT == null) {alert("Please Enter Currency/US Equavalent");document.form1.CURREQT.focus(); return false; }
    if(!IsNumeric(document.form1.CURREQT.value))
   {
     alert(" Please enter valid  Currency Equivalent !");
     document.form1.CURREQT.focus();   document.form1.CURREQT.select(); return false;
   }
    if (CURREQT.indexOf('.') == -1) CURREQT += ".";
	var decNum = CURREQT.substring(CURREQT.indexOf('.')+1, CURREQT.length);
	if (decNum.length > <%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)
	{
		alert("Invalid more than <%=DbBean.NOOFDECIMALPTSFORWEIGHT%> digits after decimal in Currency Equivalent");
		document.form1.CURREQT.focus();
		return false;
		
	}
   document.form1.action  = "../currency/new?action=ADD";
   document.form1.submit();
}
function OnChange(dropdown)
{
	
    var myindex  = dropdown.selectedIndex;
    var SelValue = dropdown.options[myindex].value;
<%--     document.form1.currency.value = SelValue; --%>
		//CREATED BY NAVAS
		var curency = '<%=curency%>';
		document.form1.currency.value ="("+ curency +"/" + SelValue+")";
		//END BY NAVAs
}


</SCRIPT>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	//Start code added by Deen for base Currency inclusion on Aug 15 2012 
	String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
	//End code added by Deen for base Currency inclusion on Aug 15 2012 
	String res = "";

	

	String action = "";
	String sCurrencyId  = "", sCurrencyDesc = "";
	String sDisplay="",sRemark="",sCurreqt="",sStatus="",sSAVE_RED;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	CurrencyUtil currUtil = new CurrencyUtil();
	DateUtils dateutils = new DateUtils();

	currUtil.setmLogger(mLogger);

	action = strUtils.fString(request.getParameter("action"));
	
	//Start code added by Deen for base Currency inclusion on Aug 15 2012 
	sCurrencyId = strUtils.fString(request.getParameter("CURRENCY_ID"));
	//End code added by Deen for base Currency inclusion on Aug 15 2012 
	sCurrencyDesc = strUtils.fString(request.getParameter("DESC"));
	sDisplay = strUtils.fString(request.getParameter("DISPLAY"));
	String result = StrUtils.fString(request.getParameter("result"));
	sRemark = strUtils.fString(request.getParameter("REMARK"));
	sCurreqt = strUtils.fString(request.getParameter("CURREQT"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
//	sStatus = strUtils.fString(request.getParameter("STATUS"));
	//if (sCurrencyId .length() <= 0)
		//sCurrencyId  = strUtils.fString(request.getParameter("ITEM_ID1"));
	
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sCurrencyId  = "";
		sCurrencyDesc = "";
		sCurreqt = "";sDisplay = "";
		sRemark = ""; sStatus="";

	} 
	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		result="";
		Hashtable ht1 = new Hashtable();
		ht1.put(IDBConstants.PLANT, sPlant);
		ht1.put(IDBConstants.CURRENCYID, baseCurrency );
		if(!(currUtil.isExistCurrency(ht1,""))){	
			ht1.clear();
			ht1.put(IDBConstants.CURRENCY_CODE, baseCurrency );
			ArrayList ccList = countryNCurrencyDAO.getCurrencyList(ht1);
			Map m=(Map)ccList.get(0);
			String country = (String)m.get("COUNTRY_NAME");
		       String currency = (String)m.get("CURRENCY_CODE");
	        ht1.clear();
	        ht1.put(IDBConstants.PLANT, sPlant);
	        ht1.put(IDBConstants.CURRENCYID, baseCurrency );
			ht1.put(IDBConstants.CURDESC, country);
			ht1.put(IDBConstants.DISPLAY,currency);
			ht1.put(IDBConstants.REMARK, "");
			ht1.put(IDBConstants.CURRENCYUSEQT, "1");
			ht1.put(IConstants.ISACTIVE, "Y");
			ht1.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());
			ht1.put(IDBConstants.LOGIN_USER, sUserId);
			boolean itemInserted = currUtil.insertCurrency(ht1);
			if(baseCurrency.equalsIgnoreCase(sCurrencyId)){
			if (itemInserted) {
				sSAVE_RED="Currency  Added Successfully";
				
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">Currency  Added Successfully</font>"; */

			} else {
				sSAVE_RED="Failed to add New Currency";
			/* 	res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New Currency </font>"; */

			}
			}
		}
				
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.CURRENCYID, sCurrencyId );
		if(!baseCurrency.equalsIgnoreCase(sCurrencyId)){
		if (!(currUtil.isExistCurrency(ht,""))) // if the Item  exists already
		{
			ht.put(IDBConstants.PLANT, sPlant);
			//Start code added by Deen for base Currency inclusion on Aug 15 2012 
			ht.put(IDBConstants.CURRENCYID, sCurrencyId );
			//End code added by Deen for base Currency inclusion on Aug 15 2012 
			ht.put(IDBConstants.CURDESC, sCurrencyDesc);
			ht.put(IDBConstants.DISPLAY, sDisplay);
			ht.put(IDBConstants.REMARK, sRemark);
			ht.put(IDBConstants.CURRENCYUSEQT, sCurreqt);
			//ht.put(IDBConstants.STATUS, sStatus);
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, sUserId);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, sPlant);
			htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_CURNCY);
			htm.put("RECID","");
			htm.put(IDBConstants.UPBY, sUserId);
			htm.put(IDBConstants.CREATED_BY, sUserId);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean itemInserted = currUtil.insertCurrency(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (itemInserted && inserted) {
				sSAVE_RED = "Update";
				/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">Currency  Added Successfully</font>"; */

			} else {
				sSAVE_RED="Failed to add New Currency";
				/* res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New Currency </font>"; */

			}
		} else {
			sSAVE_RED="Currency  Exists already. Try again";
			/* res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Currency  Exists already. Try again</font>"; */

		}
		}

	}
	if(!result.equalsIgnoreCase("")) {
		sSAVE_RED = "";
		res = "<font class = " + IDBConstants.FAILED_COLOR
		+ ">"+result+"</font>";
		}

	%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../currency/summary"><span class="underline-on-hover">Currency Summary</span></a></li>                       
                <li><label>Create Currency</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../currency/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Currency Master">Currency ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Currency ID:</label> -->
      <div class="col-sm-4">          
        	 			
            <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="CURRENCY_ID" onchange='OnChange(form1.CURRENCY_ID);' MAXLENGTH=15 style="width: 100%">
			<option value= "0" >Select One:</option>	
		<%
		   Hashtable ht = new Hashtable();
		   ArrayList ccList = countryNCurrencyDAO.getCurrencyList(ht);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String country = (String)m.get("COUNTRY_NAME");
		        String currency = (String)m.get("CURRENCY_CODE"); %>
		        <option <%if(sCurrencyId.equalsIgnoreCase(currency)){%>  selected <%} %> value= <%=currency%> ><%=country %>(<%=currency%>) </option>		          
		        <%
       			}
			 %></SELECT>


  	</div>
      </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Currency Description">Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="DESC" type="TEXT" value="<%=sCurrencyDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Currency Display">Display</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Display</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="DISPLAY" type="TEXT" value="<%=sDisplay%>"
			size="50" MAXLENGTH=50>
      </div>
    </div>
    
    <!-- //CREATED BY NAVAS -->
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Base Currency">Base Currency</label>
      <div class="col-sm-4">
      <div class="input-group">    
    		<input name="BASECURRENCY" type="TEXT" value="1.000" 
			size="48" MAXLENGTH=50 class="form-control" readonly>
  		    <span class="input-group-btn"></span>
    		<input name="Basecurrency" type="TEXT" value="<%=curency%>"
			size="6" MAXLENGTH=50  class="form-control" readonly>
   		 	</div>
   		 	</div>
            </div>
  
    <!-- //END BY NAVAS -->
     
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Equivalent Currency">Equivalent Currency</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Equivalent Currency:</label> -->
      <div class="col-sm-4">
      <div class="input-group">    
    		<input name="CURREQT" type="TEXT" value="<%=sCurreqt%>"
			size="48" MAXLENGTH=50 class="form-control">
  		    <span class="input-group-btn"></span>
    		<input name="currency" type="TEXT" value="" size="10" MAXLENGTH=50  class="form-control" readonly>
   		 	</div>
   		 	</div>
            </div>
  
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARK" type="TEXT" value="<%=sRemark%>"
			size="50" MAXLENGTH=50>
      </div>
      <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
    </div>
 

    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
     <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();">Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	

      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form1.SAVE_RED.value!="") {
    if(document.form1.SAVE_RED.value!="") {
	document.form1.action  = "../currency/summary?PGaction=View&result=Currency Added Successfully";	   
    document.form1.submit();
	}
    else{
    	document.form1.action  = "../currency/summary?PGaction=View&result=Currency Added Successfully";	   
        document.form1.submit();
}
    }
});

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>