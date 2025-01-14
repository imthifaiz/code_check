<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Currency";
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
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

<%
session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
	String res = "";

	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	StrUtils strUtils = new StrUtils();
	DateUtils dateutils = new DateUtils();
	String action = "";
	String sLocId = "", sLocDesc = "", sRemarks = "", isActive = "";
	String sCurrencyId  = "", sPrdClsId = "", sCurrencyDesc = "",currsq="";
	String sDisplay="",sRemark="",sCurreqt="",sStatus="",sSAVE_RED;
	// Start code added by Deen for base Currency inclusion on Aug 15 2012 
	sCurrencyId = strUtils.fString(request.getParameter("CURRENCY_ID"));
	currsq  = "("+ baseCurrency +"/"  +sCurrencyId +")";
	// End code added by Deen for base Currency inclusion on Aug 15 2012 
	sCurrencyDesc = strUtils.fString(request.getParameter("DESC"));
	sDisplay = strUtils.fString(request.getParameter("DISPLAY"));
	sRemark = strUtils.fString(request.getParameter("REMARK"));
	String result = StrUtils.fString(request.getParameter("result"));
	sCurreqt = strUtils.fString(request.getParameter("CURREQT"));
	// sStatus = strUtils.fString(request.getParameter("STATUS"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	CurrencyUtil curUtil= new CurrencyUtil();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
%>
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	

	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'LOCATIONS',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function onClear(){
		document.form.CURRENCY_ID.value = ""; 
		document.form.DESC.value = "";
		document.form.DISPLAY.value = ""; 
		document.form.BASECURRENCY.value = "";
		document.form.CURREQT.value = ""; 
		document.form.REMARK.value = "";
		 /*   document.form.action  = "maintCurrency.jsp?action=Clear";
		   document.form.submit(); */
		}


	function onUpdate(){
		     var CURRENCYID   = document.form.CURRENCY_ID.value;
                     var DISPLAY   = document.form.DISPLAY.value;
                     var CURREQT   = document.form.CURREQT.value;
                     if(CURRENCYID == "0" || CURRENCYID == null) {alert("Please Enter Currency ID"); document.form.CURRENCY_ID.focus();return false; }
                     if(DISPLAY == "" || DISPLAY == null) {alert("Please Enter Display for Currency ID"); document.form.DISPLAY.focus(); return false; }
                     if(CURREQT == "" || CURREQT == null) {alert("Please Enter Currency/'<%=baseCurrency%>' Equavalent");document.form.CURREQT.focus(); return false; }
                     if(!IsNumeric(document.form.CURREQT.value))
                       {
                         alert(" Please enter valid  Currency Equivalent !");
                         document.form.CURREQT.focus();   document.form.CURREQT.select(); return false;
                       }
                     if (CURREQT.indexOf('.') == -1) CURREQT += ".";
                 	var decNum = CURREQT.substring(CURREQT.indexOf('.')+1, CURREQT.length);
                 	if (decNum.length > <%=DbBean.NOOFDECIMALPTSFORWEIGHT%>)
                 	{
                 		alert("Invalid more than <%=DbBean.NOOFDECIMALPTSFORWEIGHT%> digits after decimal in Currency Equivalent");
                 		document.form1.CURREQT.focus();
                 		return false;
                 		
                 	}
		    var radio_choice = false;
		    for (i = 0; i < document.form.ACTIVE.length; i++)
		    {
		        if (document.form.ACTIVE[i].checked)
		        radio_choice = true; 
		    }
		    if (!radio_choice)
		    {
		    alert("Please select Active or non Active mode.");
		    return (false);
		    }
		    
		   var chk = confirm("Are you sure you would like to save?");
			if(chk){
		   document.form.action  = "../currency/edit?action=UPDATE";
		   document.form.submit();
			}
			else{
				return false;}	   
		}

		function onView(){
		   var CURRENCYID   = document.form.CURRENCY_ID.value;
		   if(CURRENCYID == "0" || CURRENCYID == null) {alert("Please Enter Currency ID"); return false; }

		   document.form.action  = "../currency/edit?action=View&CURRENCY_ID="+CURRENCYID;
		   document.form.submit();
		}

		function OnChange(dropdown)
		{
		    var myindex  = dropdown.selectedIndex;
		    var SelValue = dropdown.options[myindex].value;
		    document.form.currency.value = SelValue;
		  //CREATED BY NAVAS
			var curency = '<%=curency%>';
			document.form.currency.value ="("+ curency +"/" + SelValue+")";
			//END BY NAVAS
		}

	
	</script>
<%
	

	try {

		action = strUtils.fString(request.getParameter("action"));

	

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sCurrencyId  = "";
		sCurrencyDesc = "";
		sCurreqt = "";sDisplay = "";
		sRemark = ""; sStatus="";

	}  else if(action.equalsIgnoreCase("UPDATE"))  {
        result="";
		Hashtable ishashtable = new Hashtable();
		ishashtable.put(IConstants.PLANT,plant);
		ishashtable.put(IConstants.CURRENCYID,sCurrencyId);
		
		   if(curUtil.isExistCurrency(ishashtable,""))
		    {
		          Hashtable htUpdate = new Hashtable();
		          htUpdate.put(IConstants.PLANT,plant);
		          htUpdate.put(IConstants.CURDESC,sCurrencyDesc);
		          htUpdate.put(IConstants.DISPLAY,sDisplay);
		          htUpdate.put(IConstants.CURRENCYUSEQT,sCurreqt);
		          htUpdate.put(IConstants.REMARK,sRemark);
		       //   htUpdate.put(IConstants.STATUS,sStatus);
		          
		            
		        htUpdate.put(IConstants.ISACTIVE,isActive);


                        Hashtable htCondition = new Hashtable();
                        htCondition.put(IConstants.CURRENCYID,sCurrencyId);
                        htCondition.put(IConstants.PLANT,plant);
                        
                        MovHisDAO mdao = new MovHisDAO(plant);
                        mdao.setmLogger(mLogger);
                        Hashtable htm = new Hashtable();
                        htm.put(IDBConstants.PLANT,plant);
                        htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPD_CURNCY);
                        htm.put("RECID","");
                        htm.put(IDBConstants.CREATED_BY,sUserId);   htm.put("CRBY",sUserId);
                        htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
                        htm.put(IDBConstants.REMARKS,sCurrencyId+","+sRemarks);
                        htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
                        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
		          boolean custUpdated = curUtil.updateCurrencyID(htUpdate,htCondition);
		            boolean  inserted = mdao.insertIntoMovHis(htm);
		          if(custUpdated&&inserted) {
		        	        sSAVE_RED = "Update";
		                    /* res = "<font class = "+IConstants.SUCCESS_COLOR+">Currency Updated Successfully</font>"; */
		          } else {
		        	  sSAVE_RED = "Failed to Update Currency";
// 		                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Currency</font>";
		          }
		    }else{
		    	sSAVE_RED = "Currency doesn't not Exists. Try again";
// 		           res = "<font class = "+IConstants.FAILED_COLOR+">Currency doesn't not Exists. Try again</font>";

		    }
		}

		//4. >> View
		
		else if(action.equalsIgnoreCase("VIEW")){
		 List arrCust = curUtil.getCurrencyDetails(sCurrencyId,plant);
		    
		    for(int i=0;i<arrCust.size();i++)
		    {
		    	ArrayList arrayLine = (ArrayList) arrCust.get(i);
                          sCurrencyId   = (String)arrayLine.get(0);
                           sCurrencyDesc   = (String)arrayLine.get(1);
                           sDisplay      = (String)arrayLine.get(2);
                           sCurreqt     = (String)arrayLine.get(3);
                           sRemark      = (String)arrayLine.get(4);
                          // sStatus    = (String)arrayLine.get(5);
                           isActive        = (String)arrayLine.get(6);
		    }
		   }
	}catch(Exception e)
		   {

		       res="No details found for Currency ID : "+  sCurrencyId;
		   }

double dsCurreqt = "".equals(sCurreqt) ? 0.0d :  Double.parseDouble(sCurreqt);
sCurreqt = StrUtils.addZeroes(dsCurreqt, DbBean.NOOFDECIMALPTSFORWEIGHT);

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
                <li><label>Edit Currency</label></li>                                   
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

  <form class="form-horizontal" name="form" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Currency Master">Currency ID</label>
      <!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Currency ID:</label> -->
       <div class="col-sm-4">          
        <!-- <div class="input-group"> --> 	 			
            <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="CURRENCY_ID" onchange='OnChange(form.CURRENCY_ID);' MAXLENGTH=15 style="width: 100%">
			<option value= "0" >Select One : </option>	
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
<!-- <span class="input-group-addon" 
   		  onClick="javascript:popUpWin('list/currencyList.jsp?CURRENCY_ID='+form.CURRENCY_ID.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Currency Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 15px;"></i></a></span> -->
    <!-- </div> -->	
 </div>
  	<div class=form-inline>
   <div class="col-sm-2">
      	<button type="button" class="btn btn-success" onClick="onView();">search</button>&nbsp;&nbsp;
    </div></div>
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
      <!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Display:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="DISPLAY" type="TEXT" value="<%=sDisplay%>"
			size="50" MAXLENGTH=50>
      </div>
      <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
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
      <!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Equivalent Currency:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">   
    		<input name="CURREQT" type="TEXT" value="<%=sCurreqt%>"
			size="48" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-btn"></span>
    		<input name="currency" type="TEXT" value="<%=currsq%>"
			size="10" MAXLENGTH=50  class="form-control" readonly>
   		 	</div>
   		 	</div>
      </div>
  
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARK" type="TEXT" value="<%=sRemark%>"
			size="50" MAXLENGTH=50>
      </div>
    </div>
 <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div> 
</div>

    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
     <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="return onClear();">Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();">Save</button>&nbsp;&nbsp;
      	

      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form.SAVE_RED.value!="") {
    if(document.form.SAVE_RED.value!="") {
	document.form.action  = "../currency/summary?PGaction=View&result=Currency Updated Successfully";	   
    document.form.submit();
	}
    else{
    	document.form.action  = "../currency/summary?PGaction=View&result=Currency Updated Successfully";	   
        document.form.submit();
}
    }
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>