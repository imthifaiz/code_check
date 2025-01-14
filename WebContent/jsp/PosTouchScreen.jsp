
<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 19-04-2022 -->
<!-- DESC : POS touch screen config-->

<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%

String title = "POS Touch Screen Department / Products Configuration";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<style>
  .pay-select-icon-invoice {
    position: absolute;
    right: 22px;
    top: 12px;
    z-index: 2;
    vertical-align: middle;
    font-size: 10px;
    opacity: 0.5;
}
.extraInfo {
    border: 1px dashed #555;
    background-color: #f9f8f8;
    border-radius: 3px;
    color: #555;
    padding: 15px;
}
.offset-lg-7 {
    margin-left: 58.33333%;
}
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}
/* Style the tab */
.tab {
  overflow: hidden;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  line-height: 0.5;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  background-color: inherit;
  float: left;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 14px 16px;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  display: none;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-top: none;
}
.departmenttab-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.producttab-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -10%;
    top: 18px;
}
</style>
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'POS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onClear()
	{
            document.form1.OUTLET_NAME.value="";
        //    document.form1.PRD_DEP_ID.value="";
            document.form1.PRD_CLS_ID.value="";
            document.form2.OUTLET_NAME1.value="";
            document.form2.PRODUCT.value="";
	}
</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String taxbylabel= ub.getTaxByLable(plant);
	String fieldDesc=StrUtils.fString(request.getParameter("result"));  
	String res = "";
	String sNewEnb = "enabled";
	String action = "";
	String sOutlet = "",sOutCode="";
	String sOutlet1 = "",sOutCode1="";
	String sCls="",sPrd="",stype="";
//	String sDept="";
	String sRED="";
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));
    sOutCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTCODE")));
    sOutCode1 = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTCODE1")));
    sOutlet = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTLET_NAME")));
    sOutlet1 = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTLET_NAME1")));
  //   sDept = strUtils.fString(request.getParameter("PRD_DEP_ID"));
    sCls = strUtils.fString(request.getParameter("PRD_CLS_ID"));
    sPrd = strUtils.fString(request.getParameter("PRODUCT"));
    stype = strUtils.fString(request.getParameter("PRD_TYPE"));
         
	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
%>
<center>
<%-- 	<h2><small class="error-msg"><%=fieldDesc%></small></h2> --%>
	<h2><small class="error-msg" id="ERROR_MSG" style="font-size: 20px;"> </small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div>
	 <input type="number" id="inpayment" name="inpayment" style="display: none;" value="0">
            <div class="tab">
          <!--   <button class="tablinks active" onclick="openPos(event, 'departmenttab')">Departments List</button> -->
            <button class="tablinks active" onclick="openPos(event, 'departmenttab')">Categorys List</button>
 		 	<button class="tablinks" onclick="openPos(event, 'producttab')">Products List</button>
		</div>
	
	<div id="departmenttab" class="tabcontent active" style="display: block;">
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>
				<!-- <li><label>POS Touch Screen Department Configuration</label></li>  -->			
                <li><label>POS Touch Screen Category Configuration</label></li>                                   
            </ul>
	  <div class="box-header menu-drop">
	  		 <!--  <h1 style="font-size: 20px;" class="box-title">POS Touch Screen Department Configuration</h1> -->
              <h1 style="font-size: 20px;" class="box-title">POS Touch Screen Category Configuration</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		<div class="container-fluid">
		<form id="departmentForm" class="form-horizontal" name="form1"  method="post">
		<div id = "ERROR_MSG"></div>
			<div class="form-group">
			<label class="control-label col-sm-2" for="outlet">Outlet</label>
				<INPUT type="hidden" name="RFLAG" value="1">
				<div class="col-sm-4">
				<INPUT class=" form-control" id="OUTLET_NAME" value="<%=sOutlet%>" name="OUTLET_NAME" onchange="checkoutlet(this.value)" onkeypress="return onGo(0)" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Outlet">
    		 	<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i></span>    						
				<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
				<INPUT type="hidden" name="PRD_ID" value="">
   				</div>
     			<INPUT type="hidden" name="SAVE_RED" value="<%=sRED%>">
        	</div>
        	
        	<%-- <div class="form-group">
				<label class="control-label col-sm-2" for="department">Department</label>
				<INPUT type="hidden" name="RFLAG" value="1">
				<div class="col-sm-4">
				<INPUT name="PRD_DEP_ID" id="PRD_DEP_ID" value="<%=sDept%>" type = "TEXT" size="50" MAXLENGTH=100  onchange="checkdept(this.value)" class="form-control"  placeholder="Select Department">
					<span class="select-icon" style="right: 45px;"  onclick="$(this).parent().find('input[name=\'PRD_DEP_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   				</div>
     			<INPUT type="hidden" name="SAVE_RED" value="<%=sRED%>">
        	</div> --%>
        	<div class="form-group">
				<label class="control-label col-sm-2" for="category">Category</label>
				<INPUT type="hidden" name="RFLAG" value="1">
				<div class="col-sm-4">
				<INPUT name="PRD_CLS_ID" id="PRD_CLS_ID" value="<%=sCls%>" type = "TEXT" size="50" MAXLENGTH=100  onchange="checkcls(this.value)" class="form-control"  placeholder="Select Category">
					<span class="select-icon" style="right: 45px;"  onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   				</div>
     			<INPUT type="hidden" name="SAVE_RED" value="<%=sRED%>">
        	</div>
      	   <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
	    <%-- <button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAddDept();">Save</button>&nbsp;&nbsp; --%>
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAddCls();">Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      </div>
    </div>
    
    	<div id="VIEW_RESULT_HERE"></div>
		<div id="spinnerImg" ></div>
		
		</form>
		</div>
		
	</div>	

<div id="producttab" class="tabcontent">
	 	<ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>POS Touch Screen Product Configuration</label></li>                                   
        </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">POS Touch Screen Product Configuration</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
	<div class="container-fluid">	
	<form id="productForm" class="form-horizontal" name="form2" method="post">
	<div id = "ERROR_MSG"></div>
	  		<div class="form-group">
			<label class="control-label col-sm-2" for="outlet">Outlet</label>
				<INPUT type="hidden" name="RFLAG" value="1">
				<div class="col-sm-4">
				<INPUT class=" form-control" id="OUTLET_NAME1" value="<%=sOutlet1%>" name="OUTLET_NAME1" onchange="checkoutlet1(this.value)" onkeypress="return onGo(0)" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Outlet">
    		 	<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME1\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i></span>    						
				<INPUT type="hidden" name="OUTCODE1" value="<%=sOutCode1%>">
   				</div>
     			<INPUT type="hidden" name="SAVE_RED" value="<%=sRED%>">
        	</div>
        	
        	<div class="form-group">
			<label class="control-label col-sm-2" for="Product">Product</label>
				<INPUT type="hidden" name="RFLAG" value="1">
				<INPUT type="hidden" name="ITEM" value="">
				<INPUT type="hidden" name="ITEM_DESC" value="">
				<INPUT type="hidden" name="PRD_DEPT" value="">
				<INPUT type="hidden" name="PRD_BRD" value="">
				<INPUT type="hidden" name="PRD_TYPE" value="">
				<div class="col-sm-4">
				<input type="text" class="ac-selected  form-control typeahead PRODUCT" id="PRODUCT" onchange="checkitems(this.value)"  value="<%=sPrd%>" name="PRODUCT"  placeholder="Select Product">
				<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRODUCT\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   				</div>
     			<INPUT type="hidden" name="SAVE_RED" value="<%=sRED%>">
        	</div>
		
			   <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAddPrd();">Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	
      </div>
    </div>
    
	<div id="VIEW_RESULT_HERE1"></div>
	<div id="spinnerImg1" ></div>

		</form>
		</div>
		</div>
</div>
</div>

<script>
$(document).ready(function(){
	onGo(0);
    $('[data-toggle="tooltip"]').tooltip(); 

    $('.PRODUCT').typeahead({
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
				PLANT : "<%=plant%>",
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
			return '<div onclick="setitemData(\''+data.ITEM+'\',\''+data.ITEMDESC+'\',\''+data.PRD_DEPT_ID+'\',\''+data.BRAND+'\',\''+data.ITEMTYPE+'\')"><p class="item-suggestion">' + data.ITEM + '</p><br/><p class="item-suggestion">Desc: ' + data.ITEMDESC + '</p></div>';
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
    
    $('#OUTLET_NAME').typeahead({
  	  hint: true,
  	  minLength:0,  
  	  searchOnFocus: true
  	},
  	{
  	  display: 'OUTLET_NAME',  
  	  async: true,   
  	  source: function (query, process,asyncProcess) {
  		var urlStr = "/track/MasterServlet";
  		$.ajax( {
  		type : "POST",
  		url : urlStr,
  		async : true,
  		data : {
  			PLANT : "<%=plant%>",
  			ACTION : "GET_OUTLET_DATA",
  			QUERY : query
  		},
  		dataType : "json",
  		success : function(data) {
  			return asyncProcess(data.POSOUTLETS);
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
  		 return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
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
  	}).on('typeahead:change',function(event,selection){
  		if($(this).val() == ""){
  			document.form1.OUTCODE.value = "";
  		}
  	});

    /* Product Class Auto Suggestion */
    $('#PRD_CLS_ID').typeahead({
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  display: 'prd_cls_id',  
    	  source: function (query, process,asyncProcess) {
    		var urlStr = "/track/MasterServlet";
    		$.ajax( {
    		type : "POST",
    		url : urlStr,
    		async : true,
    		data : {
    			PLANT : "<%=plant%>",
    			ACTION : "GET_PRODUCTCLASSID_FOR_SUMMARY",
    			PRODUCTCLASSID : query
    		},
    		dataType : "json",
    		success : function(data) {
    			return asyncProcess(data.CUSTOMERTYPELIST);
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
		    return '<div onclick="setPrdData(\''+data.prd_cls_id+'\')"><p class="item-suggestion"> ' + data.prd_cls_id + '</p></div>';
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
    
     /* Product Class Auto Suggestion */
  <%--  /* $('#PRD_DEP_ID').typeahead({
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  display: 'prd_dep_id',  
    	  source: function (query, process,asyncProcess) {
    		var urlStr = "/track/MasterServlet";
    		$.ajax( {
    		type : "POST",
    		url : urlStr,
    		async : true,
    		data : {
    			PLANT : "<%=plant%>",
    			ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
    			PRODUCTDEPARTMENTID : query
    		},
    		dataType : "json",
    		success : function(data) {
    			return asyncProcess(data.CUSTOMERTYPELIST);
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
		    return '<div onclick="setPrdData(\''+data.prd_dep_id+'\')"><p class="item-suggestion"> ' + data.prd_dep_id + '</p></div>';
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
    */  --%>

    $('#OUTLET_NAME1').typeahead({
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  display: 'OUTLET_NAME',  
    	  async: true,   
    	  source: function (query, process,asyncProcess) {
    		var urlStr = "/track/MasterServlet";
    		$.ajax( {
    		type : "POST",
    		url : urlStr,
    		async : true,
    		data : {
    			PLANT : "<%=plant%>",
    			ACTION : "GET_OUTLET_DATA",
    			QUERY : query
    		},
    		dataType : "json",
    		success : function(data) {
    			return asyncProcess(data.POSOUTLETS);
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
    		 return '<div onclick="setOutletDataPrd(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
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
    	}).on('typeahead:change',function(event,selection){
    		if($(this).val() == ""){
    			document.form2.OUTCODE1.value = "";
    		}
    	});
    
});
function openPos(evt, pay) {
	  var i, tabcontent, tablinks;
	  tabcontent = document.getElementsByClassName("tabcontent");
	  for (i = 0; i < tabcontent.length; i++) {
	    tabcontent[i].style.display = "none";
	  }
	  tablinks = document.getElementsByClassName("tablinks");
	  for (i = 0; i < tablinks.length; i++) {
	    tablinks[i].className = tablinks[i].className.replace(" active", "");
	  }
	  document.getElementById(pay).style.display = "block";
	  evt.currentTarget.className += " active";
	  if(pay=="departmenttab")
		  $("input[name ='inpayment']").val("0");
	  else if(pay=="producttab")
		  $("input[name ='inpayment']").val("1");
	}

function setOutletData(OUTLET,OUTLET_NAME){
		$("input[name=OUTCODE]").val(OUTLET);
		$("input[name=OUTLET_NAME]").val(OUTLET_NAME);
	}

function setOutletDataPrd(OUTLET,OUTLET_NAME){
		$("input[name=OUTCODE1]").val(OUTLET);
		$("input[name=OUTLET_NAME1]").val(OUTLET_NAME);
	}

function setitemData(ITEM,ITEMDESC,DEPT,BRAND,TYPE){
		$("input[name=ITEM]").val(ITEM);
		$("input[name=ITEM_DESC]").val(ITEMDESC);
		$("input[name=PRD_DEPT]").val(DEPT);
		$("input[name=PRD_BRD]").val(BRAND);
		$("input[name=PRD_TYPE]").val(TYPE);
	}
function setPrdData(DEPT){
		$("input[name=PRD_ID]").val(DEPT);
	}

function checkoutlet(outlet){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			OUTLETS_CODE : outlet,
			ACTION : "OUTLET_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Outlet Does't Exist");
					document.getElementById("OUTLET_NAME").focus();
					$("#OUTLET_NAME").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

/* function checkdept(dept){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PRD_DEPT_ID : dept,
			ACTION : "DEPT_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Department Does't Exist");
					document.getElementById("PRD_DEP_ID").focus();
					$("#PRD_DEP_ID").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}*/

function checkcls(cat){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PRD_CLS_ID : cat,
			ACTION : "CAT_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Category Does't Exist");
					document.getElementById("PRD_CLS_ID").focus();
					$("#PRD_CLS_ID").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function checkoutlet1(outlet){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			OUTLETS_CODE : outlet,
			ACTION : "OUTLET_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Outlet Does't Exist");
					document.getElementById("OUTLET_NAME1").focus();
					$("#OUTLET_NAME1").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function checkitems(itemvalue){	
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ITEM : itemvalue,
				ACTION : "PRODUCT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Does't Exists");
						document.getElementById("PRODUCT").focus();
						$("#PRODUCT").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function onGo(index) {

	var index = index;
	var outlet = document.form1.OUTLET_NAME.value;
	var outlet1 = document.form2.OUTLET_NAME1.value;
	var prd = document.form2.PRODUCT.value;
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="../jsp/images/spinner.gif"  > </p> ';
    document.getElementById('VIEW_RESULT_HERE1').innerHTML = '';
    document.getElementById('spinnerImg1').innerHTML ='<br><br><p align=center><img src="../jsp/images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
 <%--    $.ajax({type: "POST",url: urlStr, data: {OUTLET:outlet,PLANT:"<%=plant%>",action: "VIEW_POS_DEPT_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback }); --%>
    $.ajax({type: "POST",url: urlStr, data: {OUTLET:outlet,PLANT:"<%=plant%>",action: "VIEW_POS_CLS_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    $.ajax({type: "POST",url: urlStr, data: {OUTLET:outlet1,ITEM:prd,PLANT:"<%=plant%>",action: "VIEW_POS_PRD_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback1 });
  
}

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
		
        $.each(data.clsmaster, function(i,item){
			/*outPutdata = outPutdata+item.DEPTMASTERDATA;*/                   
        	outPutdata = outPutdata+item.CLASSMASTERDATA;
                    	ii++;
            
          });
        
	}
/* 	if(!errorBoo){
		
        $.each(data.prdmaster, function(i,item){
                   
        	outPutdata = outPutdata+item.PRDMASTERDATA;
                    	ii++;
            
          });
        
	} */
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
    
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>"; 
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
    if(errorMsg=="Category Added Successfully")
 	{
 	document.form.submit();
 	}
    /* if(errorMsg=="Department Added Successfully")
 	{
 	document.form.submit();
 	} */
 }

function callback1(data){
	
	var outPutdata = getTable1();
	var ii = 0;
	var errorBoo = false;
	$.each(data.errors, function(i,error){
		if(error.ERROR_CODE=="99"){
			errorBoo = true;
			
		}
	});
	
	if(!errorBoo){
        $.each(data.prdmaster, function(i,item){
        	outPutdata = outPutdata+item.PRDMASTERDATA;
                    	ii++;
          });
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE1').innerHTML = outPutdata;
    document.getElementById('spinnerImg1').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
    
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>"; 
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
    if(errorMsg=="Products Added Successfully")
 	{
 	document.form.submit();
 	}
 }
 
function getTable(){
    return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
	           '<thead>'+
// 	           '<thead style="background:#eaeafa">'+
           '<tr>'+
		       '<th width="5%">S/No</th>'+
		       '<th width="5%">Outlet</th>'+
		       '<th width="5%">Category</th>'+
		       '<th width="5%">Category Name</th>'+
		       '<th width="5%" style="text-align: center;">Remove</th>'+
	           '</tr>'+
	           '</thead>';

}

/* function getTable(){
    return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
	           '<thead>'+
// 	           '<thead style="background:#eaeafa">'+
           '<tr>'+
		       '<th width="5%">S/No</th>'+
		       '<th width="5%">Outlet</th>'+
		       '<th width="5%">Department</th>'+
		       '<th width="5%">Department Name</th>'+
		       '<th width="5%" style="text-align: center;">Remove</th>'+
	           '</tr>'+
	           '</thead>';

} */

function getTable1(){
    return '<TABLE class="table" id="tabledata1" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
	           '<thead>'+
           '<tr>'+
		       '<th width="5%">S/No</th>'+
		       '<th width="5%">Outlet</th>'+
		       '<th width="5%">Product</th>'+
		       '<th width="5%">Description</th>'+
		       '<th width="5%">Department</th>'+
		       '<th width="5%">Category</th>'+
		       '<th width="5%">Sub Category</th>'+
		       '<th width="5%" style="text-align: center;">Remove</th>'+
	           '</tr>'+
	           '</thead>';

}

function deletedeptpos(id) {
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
	<%-- $.ajax({type: "POST",url: urlStr, data: {ID:id,action: "POS_DEPT_DELETE",PLANT:"<%=plant%>"},dataType: "json", success: callback }); --%>
    $.ajax({type: "POST",url: urlStr, data: {ID:id,action: "POS_CLS_DELETE",PLANT:"<%=plant%>"},dataType: "json", success: callback });
}
<%-- function deletedeptpos(id) {
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    $.ajax({type: "POST",url: urlStr, data: {ID:id,action: "POS_DEPT_DELETE",PLANT:"<%=plant%>"},dataType: "json", success: callback });
} --%>

function deleteprdpos(id) {
    document.getElementById('VIEW_RESULT_HERE1').innerHTML = '';
    document.getElementById('spinnerImg1').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    $.ajax({type: "POST",url: urlStr, data: {ID:id,action: "POS_PRODUCT_DELETE",PLANT:"<%=plant%>"},dataType: "json", success: callback1 });
}

function onAddCls() {
	var outlet = document.form1.OUTCODE.value;
	var cat = document.form1.PRD_ID.value;
	var cats = document.form1.PRD_CLS_ID.value;
	if(outlet=="" || outlet.length==0 ) {
		alert("Select Outlet");
		document.getElementById("OUTLET_NAME").focus();
		return false;
	}
	if(cats=="" || cats.length==0 ) {
		alert("Select Category");
		document.getElementById("PRD_CLS_ID").focus();
		return false;
	}
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    $.ajax({type: "POST",url: urlStr, data: {OUTLET:outlet,CAT:cat,action: "ADD_POS_CLS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form1.OUTLET_NAME.value = "";
    document.form1.PRD_CLS_ID.value = "";
          
}

<%-- /* function onAddDept() {
	var outlet = document.form1.OUTCODE.value;
	var dept = document.form1.PRD_ID.value;
	var depts = document.form1.PRD_DEP_ID.value;
	if(outlet=="" || outlet.length==0 ) {
		alert("Select Outlet");
		document.getElementById("OUTLET_NAME").focus();
		return false;
	}
	if(depts=="" || depts.length==0 ) {
		alert("Select Department");
		document.getElementById("PRD_DEP_ID").focus();
		return false;
	}
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    $.ajax({type: "POST",url: urlStr, data: {OUTLET:outlet,DEPT:dept,action: "ADD_POS_DEPT",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form1.OUTLET_NAME.value = "";
    document.form1.PRD_DEP_ID.value = "";
          
} */ --%>

function onAddPrd() {
	var outlet1 = document.form2.OUTCODE1.value;
	var prd = document.form2.ITEM.value;
	if(outlet1=="" || outlet1.length==0 ) {
		alert("Select Outlet");
		document.getElementById("OUTLET_NAME1").focus();
		return false;
	}
	if(prd=="" || prd.length==0 ) {
		alert("Select Product");
		document.getElementById("PRODUCT").focus();
		return false;
	}
    document.getElementById('VIEW_RESULT_HERE1').innerHTML = '';
    document.getElementById('spinnerImg1').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    $.ajax({type: "POST",url: urlStr, data: {OUTLET:outlet1,PRODUCT:prd,action: "ADD_POS_PRD",PLANT:"<%=plant%>"},dataType: "json", success: callback1 });
    document.form2.OUTLET_NAME1.value = "";
    document.form2.PRODUCT.value = "";
          
}

document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
document.getElementById('VIEW_RESULT_HERE1').innerHTML =  getTable1()+'</TABLE>';
</script>



<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


