<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%@page import="com.track.dao.PlantMstDAO"%>
<%
String title = "Assign User Customer";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  	}
 	function onGo()
	{ 
 		document.form1.action ="../customer/assignuser"
        document.form1.submit();
	}

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
 	
</script>

<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	ArrayList locQryList  = new ArrayList();
	
	_userBean.setmLogger(mLogger);
	String fieldDesc="";
	String PLANT="",CUSTNO ="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",AssignedUser="",AssignedLoc="",chkdDoNo="",USER="",COMPANY,USERId,USER_LEVEL;
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	CUSTNO     = strUtils.fString(request.getParameter("CUSTNO"));
	chkdDoNo     = strUtils.fString(request.getParameter("chkdDoNo"));
	USER     = strUtils.fString(request.getParameter("USER"));
	fieldDesc=StrUtils.fString(request.getParameter("result"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	String ADD1 = (String) map.get("ADD1");
	String ADD2 = (String) map.get("ADD2");
	String ADD3 = (String) map.get("ADD3");
	String ADD4 = (String) map.get("ADD4");
	String STATE = (String) map.get("STATE");
	String COUNTRY = (String) map.get("COUNTY");
	String ZIP = (String) map.get("ZIP");
	String TELNO = (String) map.get("TELNO");

	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;
	

	COMPANY = strUtils.fString(request.getParameter("COMPANY"));
	USERId = strUtils.fString(request.getParameter("USERID"));
	USER_LEVEL = strUtils.fString(request.getParameter("USER_LEVEL"));
 /*  	CustUtil  _custUtil = new CustUtil();
 	_custUtil.setmLogger(mLogger); 
	 if(PGaction.equalsIgnoreCase("View")){
 	try{
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      ht.put("AssignedLoc",AssignedLoc);
      ht.put("AssignedUser",AssignedUser);
      
         locQryList=      _custUtil.getUserCustDetails(CUSTNO,ht,PLANT);
      if(locQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
  
 	}catch(Exception e) {mLogger.exception(true,
			"ERROR IN JSP PAGE - UserCustomer.jsp ", e); }
	}  */
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Assign User Customer</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
         <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>   
		
 <div class="box-body">

<FORM class="form-horizontal" name="form1" method="post" action="UserCustomer.jsp">
  <input type="hidden" name="xlAction" value="">
  <input type="hidden" name="PGaction" value="View">
  <INPUT type = "hidden" name="LOC_ID1" value ="">
  <INPUT type = "hidden" name="LOC_DESC" value ="">
  <INPUT type = "hidden" name="REMARKS" value ="">
  <input type="hidden" name="plant" value="<%=PLANT%>">
  <INPUT type = "hidden" name="COMNAME" value ="">
  <INPUT type = "hidden" name="CHK_ADDRESS" value ="Y">
  <INPUT type = "hidden" name="UI_PKEY" value ="">
  <INPUT name="ACTIVE" type = "hidden" value="Y"   >
  <INPUT name="ACTIVE" type = "hidden" value="N"   >
  <INPUT name="COMPANY" type="hidden" value="<%=PLANT%>" size="20" MAXLENGTH="20"/>
  <INPUT name="USER_LEVEL" type = "hidden" value=""   >
  <INPUT name="chkdDoNo"  name="chkdDoNo" type = "hidden" value=""   >
  
 <div class="form-group">
<label class="control-label col-sm-4" for="User Id">User</label>
<div class="col-sm-3 ac-box" >
<div class="input-group">
   <INPUT class="ac-selected  form-control typeahead" name="USER" ID="USER" type="TEXT" value="" placeholder="select a user">
  <span class="select-icon" onclick="$(this).parent().find('input[name=\'USER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
</div>
</div>

</div>
<div class="form-group" align="center">
<!--   	<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp; -->
  	</div>

  <br>	
  
  	<div class="row">
  		<div class="col-12 col-sm-12">
  			<label>
  			<input type="checkbox" class="form-check-input"  style="border:0;" name = "select" value="select" onclick="return checkAll(this.checked);">
			&nbsp; Select/Unselect All &nbsp;
			</label>
		</div>                        
  	</div>

<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center"> 
          <tr>  
            <th style="font-size: smaller;">Chk</th>  
            <th style="font-size: smaller;">Customer Code</th>
            <th style="font-size: smaller;">Customer Name</th>
            <th style="font-size: smaller;">Customer Type</th>
             <th style="font-size: smaller;">Email</th>
          </tr>  
        </thead> 
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
</table>
</div>  
  </FORM>
 <div class="form-group">
  	<div class="col-sm-12" align="center">
  	      <button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAssign();"><b>Assign</b></button>
  	</div>
  	</div>
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var CUSTNO,CNAME,CUSTOMER_TYPE,EMAIL,USER, groupRowColSpan = 6;

		function onAssign(){
			var checkFound = false;
			var len = document.form1.chkdDoNo.length;
			var USER   = document.form1.USER.value;
			 var orderLNo; 
			if (len == undefined)
				len = 1;
			/* for ( var i = 0; i < len; i++) {
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
			} */
			if(USER == "" || USER == null) {alert("Please Enter/Select User"); return false; }
// 			form1.setAttribute("target", "_blank");
// 			form1.setAttribute("target");
				document.form1.action  = "/track/customer/AssignCus";
		   		document.form1.submit();
		}

		function getParameters(){
			return {
				"CUSTNO":CUSTNO,
				"CNAME":CNAME,
				"CUSTOMER_TYPE":CUSTOMER_TYPE,
				"EMAIL":EMAIL,
				"PLANT":plant,
				"USER":USER,
				"UI_PKEY":UI_PKEY,
				"action":"GET_USERASSIGNCUS_FOR_SUMMARY"
			}
		}
		function onGo(){
			USER=document.form1.USER.value;
			UI_PKEY=document.form1.UI_PKEY.value;	
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
						        	if(typeof data.SUPPLIERTYPELIST[0].CUSTNO === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.SUPPLIERTYPELIST.length; dataIndex ++){
						        				var lineno = data.SUPPLIERTYPELIST[dataIndex].CUSTNO;
						        				var sno=dataIndex+1;
						        				//data.SUPPLIERTYPELIST[dataIndex]['SNO'] = sno;
						        				if (data.SUPPLIERTYPELIST[dataIndex]['CHK'] == 'yes')
						        				data.SUPPLIERTYPELIST[dataIndex]['CHK'] = '<INPUT Type=checkbox style=border: 0; checked name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['CUSTNO']+'" >';
						        				else
						        				data.SUPPLIERTYPELIST[dataIndex]['CHK'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['CUSTNO']+'" >';
						        			}
						        		return data.SUPPLIERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'CHK', "orderable": false},
				        	{"data": 'CUSTNO', "orderable": true},
				        	{"data": 'CUSTDESC', "orderable": true},
				        	{"data": 'CUST_TYPE', "orderable": true},
				        	{"data": 'EMAIL', "orderable": true},
			    			],
						"orderFixed": [ ], 
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
				                    		columns: [0,1,2,3,4,5]
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5]
				                    	},
				                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
				                    	<%} else {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
				                    	<%}%>
			                    		orientation: 'portrait',
				                    	customize: function(doc) {
				                    		doc.defaultStyle.fontSize = 7;
			                     	        doc.styles.tableHeader.fontSize = 7;
			                     	        doc.styles.title.fontSize = 10;
			                     	        doc.styles.tableFooter.fontSize = 7;
				                    	},
			                            pageSize: 'A4'
				                    },
				                    {
				                    	extend : 'pdf',
				                    	footer: true,
				                    	text: 'PDF Landscape',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5]
				                    	},
				                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
				                    	<%} else {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
				                    	<%}%>
			                    		orientation: 'landscape',
			                    		customize: function(doc) {
				                    		doc.defaultStyle.fontSize = 6;
			                     	        doc.styles.tableHeader.fontSize = 6;
			                     	        doc.styles.title.fontSize = 8;                     	       
			                     	        doc.content[1].table.widths = "*";
			                     	       doc.styles.tableFooter.fontSize = 7;
				                    	     },
			                            pageSize: 'A4'
				                    }
				                ]
				            },
				            {
			                    extend: 'colvis',
			                }		                
				        ],
				        "order": [],

					});
			    }
			    
			}

		</script>
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
    var plant= '<%=PLANT%>';

	/* UserId Auto Suggestion */
	$('#USER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'USER_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_USERADMIN_FOR_USERDETAILS",
				USER : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.USERLEVEL);
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
		    return '<p class="item-suggestion">'+ data.USER_ID +'</p>';
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
			$("input[name=UI_PKEY]").val(selection.UI_PKEY);
			onGo();
		}); 

});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>