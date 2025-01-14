<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@page import="com.track.dao.ItemMstDAO"%><!-- <html> -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />

<%
String title = "Generate Receipt Barcode";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
  <jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

 var postatus =   [{
    "year": "Purchase Order",
    "value": "Purchase Order",
    "tokens": [
      "Purchase Order"
    ]
  },
  /* {
	    "year": "Rental Order",
	    "value": "Rental Order",
	    "tokens": [
	      "OPEN"
	    ]
	  },
	  {
		    "year": "Consignment Order",
		    "value": "Consignment Order",
		    "tokens": [
		      "OPEN"
		    ]
		  }, */
		  {
			    "year": "Goods Receipt",
			    "value": "Goods Receipt",
			    "tokens": [
			      "Goods Receipt"
			    ]
		  },
		  {
			    "year": "Bill",
			    "value": "Bill",
			    "tokens": [
			      "Bill"
			    ]
		  }/* ,
	  {
		    "year": "Tax Invoice",
		    "value": "Tax Invoice",
		    "tokens": [
		      "PAID"
		    ]
		  } */];
			  
var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    // an array that will be populated with substring matches
	    matches = [];
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

function checkAll(isChk)
{
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdDoNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdDoNo[i].checked = isChk;
              	}
            	
        }
    }
}

function checkPOBILL(isChk)
{
	if ( isChk == true ) {
	       document.getElementById('GB50X25').setAttribute("disabled","disabled");
	    } else {
	       document.getElementById('GB50X25').removeAttribute("disabled");
	    }
	if ( isChk == true ) {
	       document.getElementById('GB30X25').setAttribute("disabled","disabled");
	    } else {
	       document.getElementById('GB30X25').removeAttribute("disabled");
	    }
}
function onRePrint(type){
	var checkFound = false;
	 var Traveler ;
	 var concatTraveler="";
	 var j=0;
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdDoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdDoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdDoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
	
	for ( var i = 0; i < len; i++) {
		if (len == 1 && document.form1.chkdDoNo.checked) {
			var barcode="";
			var str = document.form1.chkdDoNo.value;
			Traveler=document.form1.chkdDoNo.value;
            concatTraveler=concatTraveler+Traveler+"="; 			
			
			//alert(barcode);			
		}
		else {
			if (document.form1.chkdDoNo[i].checked) {

				j=j+1;
                Traveler=document.form1.chkdDoNo[i].value;
                concatTraveler=concatTraveler+Traveler+"=";    							
			}
		}
			document.form1.TRAVELER.value=concatTraveler;		
		}
	document.form1.action="/track/inhouse/printbarcode?PAGE_TYPE=RECEIPT&PRINT_TYPE="+type;
    //document.form1.action="/track/DynamicFileServlet?action=printBarcode&Submit="+type;
    document.form1.submit();
    //onGo();
}
</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
	StrUtils _strUtils = new StrUtils();
	Generator generator = new Generator();
	HTReportUtil movHisUtil = new HTReportUtil();
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList = new ArrayList();
	ArrayList movItemQryList = new ArrayList();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	movHisUtil.setmLogger(mLogger);
	
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
        String userID = (String) session.getAttribute("LOGIN_USER");
        String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
        String systatus = session.getAttribute("SYSTEMNOW").toString();
	String FROM_DATE = "", TO_DATE = "", DIRTYPE = "", BATCH = "", USER = "", ITEM = "", 
			fdate = "", tdate = "", JOBNO = "", ITEMNO = "", ORDERNO = "", CUSTOMER = "", PGaction = "",ITEMDESC="";
	String TO_ASSIGNEE="",LOANASSIGNEE="",ORDERTYPE,LOC="",REASONCODE="",FILTER="",LOC_TYPE_ID2="";
	PGaction = _strUtils.fString(request.getParameter("PGaction"))
			.trim();
	String html = "", cntRec = "false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",LOC_TYPE_ID="",allChecked = "";
	boolean displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryExport = ub.isCheckValAcc("exportrecvsummry", plant,LOGIN_USER);
		displaySummaryExport = true;
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryExport = ub.isCheckValinv("exportrecvsummry", plant,LOGIN_USER);
		displaySummaryExport = true;
	}
	
	double ireceivetotal=0;
	double ireversetotal=0;
	int k=0;

	FROM_DATE = _strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = _strUtils.fString(request.getParameter("TO_DATE"));
	String  fieldDesc="";
	fieldDesc = (String)request.getAttribute("Msg");
	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
// 	String curDate = _dateUtils.getDate();
      String curDate =du.getDateMinusDays();//resvi
	if (FROM_DATE.length() < 0 || FROM_DATE == null
			|| FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE = curDate;//resvi
	if (FROM_DATE.length() > 5)

		//fdate = FROM_DATE.substring(6) + "-"+ FROM_DATE.substring(3, 5) + "-"+ FROM_DATE.substring(0, 2);
		fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);

	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)
		// tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5)+ "-" + TO_DATE.substring(0, 2);
		 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);

	DIRTYPE = _strUtils.fString(request.getParameter("DIRTYPE"));
	JOBNO = _strUtils.fString(request.getParameter("JOBNO"));
	USER = _strUtils.fString(request.getParameter("USER"));
	ITEMNO = _strUtils.fString(request.getParameter("ITEM"));
	ORDERNO = _strUtils.fString(request.getParameter("ORDERNO"));
	ORDERTYPE= _strUtils.fString(request.getParameter("ORDERTYPE"));
	CUSTOMER = _strUtils.fString(request.getParameter("CUSTOMER"));
	TO_ASSIGNEE = _strUtils.fString(request.getParameter("TO_ASSIGNEE"));
	LOANASSIGNEE = _strUtils.fString(request.getParameter("LOANASSIGNEE"));
	BATCH = _strUtils.fString(request.getParameter("BATCH"));
	ITEMDESC = _strUtils.fString(request.getParameter("DESC"));
	LOC = _strUtils.fString(request.getParameter("LOC"));
	PRD_TYPE_ID = _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID = _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	PRD_CLS_ID = _strUtils.fString(request.getParameter("PRD_CLS_ID"));
	REASONCODE = _strUtils.fString(request.getParameter("REASONCODE"));
	LOC_TYPE_ID = _strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	LOC_TYPE_ID2 = _strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	FILTER = _strUtils.fString(request.getParameter("FILTER"));
	ItemMstDAO itemdao = new ItemMstDAO();
	itemdao.setmLogger(mLogger);
	String collectionDate=du.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	if (DIRTYPE.length() <= 0) {
		DIRTYPE = "RECEIVE";
	}
	
	
%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                <li><a href="../inhouse/genbarcode"><span class="underline-on-hover">Generate Barcode</span></a></li>	
                <li><label>Generate Receipt Barcode</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                  <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                   				
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../inhouse/genbarcode'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../inhouse/genreceiptbarcode">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">

<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>

<%-- <Center>
<h1><small><%=fieldDesc%></small></h1>

  </Center> --%>
  
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT	type="hidden" name="TRAVELER" value="">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" placeholder="SUPPLIER" name="CUSTOMER" >				
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=_strUtils.forHTMLTag(ORDERNO)%>" placeholder="Order Number" >
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  			<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" name="ITEM" id="ITEM" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(ITEMNO)%>"placeholder="PRODUCT" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>  		
  		</div>				
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  			<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_CLS_ID" name="PRD_CLS_ID" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>"placeholder="PRODUCT CLASS">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="PRD_TYPE_ID" id="PRD_TYPE_ID" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" placeholder="PRODUCT TYPE" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="PRD_BRAND_ID" name="PRD_BRAND_ID" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<!-- <div class="">  -->
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" placeholder="ORDER TYPE" >
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<!-- <span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('displayOrderType.jsp?OTYPE=PURCHASE&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		<!-- </div> -->
  		</div>
  		</div>
<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC" name="LOC" value="<%=StrUtils.forHTMLTag(LOC)%>" placeholder="LOCATION">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?LOC='+form1.LOC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		
  		<input type="text" name="BATCH" id="BATCH" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(BATCH)%>" placeholder="BATCH" > 		
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC_TYPE_ID" name="LOC_TYPE_ID" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" placeholder="LOCATION TYPE 1">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="LOC_TYPE_ID2" name="LOC_TYPE_ID2" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>" placeholder="LOCATION TYPE 2">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID2.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">                       
        <input type="FILTER" name="sortby" id="FILTER" class="ac-selected form-control" placeholder="Sort By" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'FILTER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  			<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" class="ac-selected form-control" id="REASONCODE" name="REASONCODE" value="<%=StrUtils.forHTMLTag(REASONCODE)%>" placeholder="REASON CODE">
  		<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'REASONCODE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/miscreasoncode.jsp?ITEMNO='+form1.ITEM.value+'&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>
  		</div>
  		</div>
        <input type="hidden" name="LOC_DESC" value="">
  		<INPUT type="Hidden" name="DIRTYPE" value="RECEIVE">
     	<INPUT type="Hidden" name="JOBNO" value="">
     	<INPUT name="DESC" type = "Hidden" value="<%=_strUtils.forHTMLTag(ITEMDESC)%>">
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="hidden" class="ac-selected form-control" id="TO_ASSIGNEE" name="TO_ASSIGNEE" value="<%=StrUtils.forHTMLTag(TO_ASSIGNEE)%>" placeholder="To Customer">	
  		</div>
  		<div class="col-sm-4 ac-box">
  		
  		<input type="hidden" name="LOANASSIGNEE" id="LOANASSIGNEE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(LOANASSIGNEE)%>" placeholder="Rental Customer" > 		
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
	  		<div class="col-sm-2">
	  		</div>
  			<div class="col-sm-4 ac-box">  		
	  		<input type="text" class="ac-selected form-control" id="grno" name="grno" placeholder="GRNO" value="">
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'grno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
		</div>
  		</div>
		</div>
   		
   		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
  	</div>
        </div>
       	  </div>     
 	     <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                       &nbsp; Select/Unselect All </strong>&nbsp;<input type="checkbox" class="form-check-input" name="printwithbatch" value="printwithbatch" />&nbsp;Print with Batch
                     &nbsp;<input type="checkbox" class="form-check-input" name="printwithpobill" value="printwithpobill" onclick="return checkPOBILL(this.checked);"/>&nbsp;Print with PO/Bill No.</div>
  </div>
      <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableInventorySummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">Chk</th>
		                	<th style="font-size: smaller;">Order No</th>
		                	<th style="font-size: smaller;">Received Date</th>
		                	<th style="font-size: smaller;">Supplier Name</th>
		                	<th style="font-size: smaller;">Product ID</th>
		                	<th style="font-size: smaller;">Description</th>
		                	<th style="font-size: smaller;">Loc</th>
		                	<th style="font-size: smaller;">Batch</th>
		                	<th style="font-size: smaller;">Expiry Date</th>
		                	<th style="font-size: smaller;">Received Qty</th>
		                	<th style="font-size: smaller;">UOM</th>
		                	<th style="font-size: smaller;">Printed Qty</th>
		                	
		                </tr>
		            </thead>
		            <tfoot align="right" style="
    display: none;">
		<tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr>
	</tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <div id="spinnerImg" ></div>

  	 <div class="form-group">
  	<div class="col-sm-12" align="center"> 
  	    <input type="checkbox" class="form-check-input" name="printwithproduct" value="printwithproduct"/>&nbsp;<label>&nbsp;Select Print by Product Id<br/> Unselect Print by Auto Generated Barcode</label>
  	&nbsp;<input type="checkbox" class="form-check-input" name="printwithlot" value="printwithlot" checked onclick="return checkPOBILL(this.checked);"/>&nbsp;<label>&nbsp;Select Print by Received Qty /<br/> Unselect Print by Each Qty</label>
  	&nbsp;<button type="button" class="Submit btn btn-default"  value="Print" id="GB30X25" name="action" onclick="javascript:return onRePrint('30X25');"><b>Generate Barcode 30X25 mm</b></button>
  	&nbsp;<button type="button" class="Submit btn btn-default"  value="Print" id="GB50X25" name="action" onclick="javascript:return onRePrint('50X25');"><b>Generate Barcode 50X25 mm</b></button>
  	<button type="button" class="Submit btn btn-default"  value="Print"  name="action" onclick="javascript:return onRePrint('100X50');"><b>Generate Barcode 100X50 mm</b></button>
  	 </div>
  	</div>
  	
  </FORM>
  <SCRIPT LANGUAGE="JavaScript">
  var tableInventorySummary;
  var ITEMNO, ITEMDESC, FROM_DATE, TO_DATE, DIRTYPE, SUPPLIER, ORDERNO, JOBNO, ORDERTYPE, PRD_CLS_ID, LOC_TYPE_ID,LOC_TYPE_ID2,
  TO_ASSIGNEE, LOANASSINEE, REASONCODE,BATCH,LOC,FILTER, GRNO, groupRowColSpan = 8;
function getParameters(){
	return {
		"ITEM": ITEMNO,
		"PRD_DESCRIP":ITEMDESC,
		"FDATE":FROM_DATE,
		"TDATE":TO_DATE,
		"DTYPE":DIRTYPE,
		"CNAME":SUPPLIER,
		"ORDERNO":ORDERNO,
		"JOBNO":JOBNO,
		"ORDERTYPE":ORDERTYPE,
		"PRD_BRAND_ID":PRD_BRAND_ID,
		"PRD_TYPE_ID":PRD_TYPE_ID,
		"PRD_CLS_ID":PRD_CLS_ID,
		"LOC_TYPE_ID":LOC_TYPE_ID,
		"TOASSINEE":TO_ASSIGNEE,
		"LOANASSINEE":LOANASSINEE,
		"REASONCODE":REASONCODE,
		"BATCH":BATCH,
		"LOC":LOC,
		"FILTER":FILTER,	
		"LOC_TYPE_ID2":LOC_TYPE_ID2,
		"GRNO":GRNO,
		"ACTION": "VIEW_GOODS_RECIPT_SMRY",
		"PLANT":"<%=plant%>"
	}
}  
function onGo(){
	var flag    = "false";

	    FROM_DATE      = document.form1.FROM_DATE.value;
	    TO_DATE        = document.form1.TO_DATE.value;
	    DIRTYPE        = document.form1.DIRTYPE.value;
	    JOBNO          = document.form1.JOBNO.value;
	    ITEMNO         = document.form1.ITEM.value;
	    ORDERNO        = document.form1.ORDERNO.value;
	   ORDERTYPE      = document.form1.ORDERTYPE.value;
	    SUPPLIER              = document.form1.CUSTOMER.value;
	    TO_ASSIGNEE           = document.form1.TO_ASSIGNEE.value;
	    LOANASSINEE           = document.form1.LOANASSIGNEE.value;
	    BATCH        = document.form1.BATCH.value;
	    LOC          = document.form1.LOC.value;
	    ITEMDESC       = document.form1.DESC .value;
	  
	    PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
	    PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
	    PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
	   REASONCODE      = document.form1.REASONCODE.value;
	    LOC_TYPE_ID         = document.form1.LOC_TYPE_ID.value;
	    FILTER         = document.form1.FILTER.value; 
	    LOC_TYPE_ID2         = document.form1.LOC_TYPE_ID2.value;
	    GRNO = document.form1.grno.value;
	   
	   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
	  // if(flag == "false"){ alert("Please define any one search criteria"); return false;}
	   
	   var urlStr = "../InboundOrderHandlerServlet";
	  	// Call the method of JQuery Ajax provided
	  	var groupColumn = 3;
	  	var totalQty = 0;
	   // End code modified by Deen for product brand on 11/9/12
	   if (tableInventorySummary){
	   	tableInventorySummary.ajax.url( urlStr ).load();
	   }else{
		    tableInventorySummary = $('#tableInventorySummary').DataTable({
				"processing": true,
				"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
				"ajax": {
					"type": "POST",
					"url": urlStr,
					"data": function(d){
						return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
					}, 
					"contentType": "application/x-www-form-urlencoded; charset=utf-8",
			        "dataType": "json",
			        "dataSrc": function(data){
			        	if(typeof data.items[0].pono === 'undefined'){
			        		return [];
			        	}else {
			        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
			        			data.items[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.items[dataIndex]['grno']+','+data.items[dataIndex]['item']+','+data.items[dataIndex]['lnno']+','+data.items[dataIndex]['loc']+','+data.items[dataIndex]['batch']+','+data.items[dataIndex]['recqty']+','+data.items[dataIndex]['uom']+','+data.items[dataIndex]['pono']+','+data.items[dataIndex]['itemdesc']+'" >';
			        			//data.items[dataIndex]['PrintedQty'] ='0.000';
			        		}
			        		return data.items;
			        	}
			        }
			    },
		        "columns": [
		        {"data": 'CHKOB', "orderable": true},
	   			{"data": 'pono', "orderable": true},
	   			{"data": 'strDate', "orderable": true},
	   			{"data": 'cname', "orderable": true},
	   			{"data": 'item', "orderable": true},
	   			{"data": 'itemdesc', "orderable": true},
	   			{"data": 'loc', "orderable": true},
	   			{"data": 'batch', "orderable": true},
	   			{"data": 'expiredate', "orderable": true},
	   			{"data": 'recqty', "orderable": true},
	   			{"data": 'uom', "orderable": true},
	   			{"data": 'PRINTQTY', "orderable": true}
	   			
	   			],
				"columnDefs": [{"className": "t-right", "targets": [9,11]}],
				"orderFixed": [ groupColumn, 'asc' ], 
				/*"dom": 'lBfrtip',*/
				"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
				"<'row'<'col-md-6'><'col-md-6'>>" +
				"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
		        buttons: [
		        	{
		        		 extend: 'collection',
			                text: 'Export',
			                buttons: [
			                    {
			                    	extend : 'excel',
			                    	exportOptions: {
			    	                	columns: [':visible']
			    	                }
			                    },
			                    {
			                    	extend : 'pdfHtml5',
	    	                    	exportOptions: {
	    	                    		columns: [':visible']
	    	                    	},	                    	
	    	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },    	                        
	                     		orientation: 'landscape',
	                     		customize: function (doc) {
	                     			doc.defaultStyle.fontSize = 16;
	                     	        doc.styles.tableHeader.fontSize = 16;
	                     	        doc.styles.title.fontSize = 20;
	                     	       doc.content[1].table.body[0].forEach(function (h) {
	                     	          h.fillColor = '#ECECEC';                 	         
	                     	          alignment: 'center'
	                     	      });
	                     	      var rowCount = doc.content[1].table.body.length;
	                     	     for (i = 1; i < rowCount; i++) {                     	     
	                     	     doc.content[1].table.body[i][8].alignment = 'right';                     	                          	     
	                     	     } 
	                     	      doc.styles.tableHeader.color = 'black';
	                     	      
	                     	        // Create a footer
	                     	        doc['footer']=(function(page, pages) {
	                     	            return {
	                     	                columns: [
	                     	                    '',
	                     	                    {
	                     	                        // This is the right column
	                     	                        alignment: 'right',
	                     	                        text: ['page ', { text: page.toString() },  ' of ', { text: pages.toString() }]
	                     	                    }
	                     	                ],
	                     	                margin: [10, 0]
	                     	            }
	                     	        });
	                     		},
	                             pageSize: 'A2',
	                             footer: true
			                    }
			                ]
		            },
		            {
	                   extend: 'colvis',
	                   columns: ':not(:eq('+groupColumn+')):not(:eq(0))'
	               }
		        ],
		        "createdRow": function(row, data, dataIndex){
		        	
		        },
		        "footerCallback": function ( row, data, start, end, display ) {	            		            
		        },
				"drawCallback": function ( ) {		
					$('.buttons-collection')[0].style.display = 'none';
					$('.buttons-colvis')[0].style.display = 'none';
		        }/**/
			});
	   }
	}

	$('#tableInventorySummary').on('column-visibility.dt', function(e, settings, column, state ){
		if (!state){
			groupRowColSpan = parseInt(groupRowColSpan) - 1;
		}else{
			groupRowColSpan = parseInt(groupRowColSpan) + 1;
		}
		$('#tableInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
		$('#tableInventorySummary').attr('width', '100%');
	});

  function callback(data){
		
		var outPutdata = getTable();
		var ii = 0;
		var errorBoo = false;
		$.each(data.errors, function(i,error){
			if(error.ERROR_CODE=="99"){
				errorBoo = true;
				
			}
		});
		
		if(!errorBoo){
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                   
	        	outPutdata = outPutdata+item.RECVDETAILS
                    	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
    outPutdata = outPutdata +'</TABLE>';
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML ='';

 
}

function getTable(){
 return  '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
         '<TR BGCOLOR="#000066">'+
         '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Received Date</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Supplier Name</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Loc</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Batch</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Expire date</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Receive Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>UOM</TH>'+
         '<TH><font color="#ffffff" align="left"><b>User</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Remarks</TH>'+
       '</tr>';
            
}

//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
          

 </SCRIPT> 
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
 onGo();
 document.getElementById('GB50X25').setAttribute("disabled","disabled");
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 document.getElementById('GB30X25').setAttribute("disabled","disabled");
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('.Show').click();
    }else{
    	$('.Hide').click();
    }
    var plant= '<%=plant%>';  
	/* Supplier Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
			}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')">' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	
	/* Supplier Type Auto Suggestion */
	$('#SUPPLIER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'SUPPLIER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIERTYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.SUPPLIER_TYPE_MST);
			}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<div><p class="item-suggestion">'+data.SUPPLIER_TYPE_ID+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	
	/* Order Number Auto Suggestion */
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_ORDER_RECEIPT_AUTOSUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				PONO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
			}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p>' + data.PONO + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERNO.value = "";
			}
		
		});
	
	/* Order Number Auto Suggestion */
	$('#ORDERTYPE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_TYPE_FOR_AUTO_SUGGESTION",
				OTYPE : "PURCHASE",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.ORDERTYPEMST);
			}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p>' + data.ORDERTYPE + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERTYPE.value = "";
			}
		
		});
	
	/* Product Number Auto Suggestion */
	$('#ITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
	/* Product Number Auto Suggestion */
	$('#PRD_CLS_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTCLASS_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLASS_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* Product Type Number Auto Suggestion */
	$('#PRD_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPE_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* Product Type Number Auto Suggestion */
	$('#PRD_BRAND_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTBRAND_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRAND_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* location Auto Suggestion */
	$('#LOC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID2').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	/* Reason code Auto Suggestion */
	$('#REASONCODE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'RSNCODE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_RSNCODE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.RSNCODE_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.RSNCODE+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
	/* To get the suggestion data for Status */
	$("#FILTER").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'postatus',
	  display: 'value',  
	  source: substringMatcher(postatus),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
	
	$('#grno').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'GRNO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_GRNO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				PONO : document.form1.ORDERNO.value,
				GRNO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
			}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p>' + data.GRNO + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
 });
 </script>

 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>