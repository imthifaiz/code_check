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
String title = "Assign API Inventory Location";
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
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  	}
 	function onGo()
	{ 
 		document.form1.action ="../invlocation/assignInvLocation"
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
	String PLANT="",LOC_ID ="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",AssignedUser="",AssignedLoc="",chkdDoNo="",USER="",COMPANY,USERId,USER_LEVEL;
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	LOC_ID     = strUtils.fString(request.getParameter("LOC_ID"));
	LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOC_TP_ID"));
	chkdDoNo     = strUtils.fString(request.getParameter("chkdDoNo"));
	USER     = strUtils.fString(request.getParameter("USER"));
	fieldDesc=StrUtils.fString(request.getParameter("result"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	

	COMPANY = strUtils.fString(request.getParameter("COMPANY"));
	USERId = strUtils.fString(request.getParameter("USERID"));
	USER_LEVEL = strUtils.fString(request.getParameter("USER_LEVEL"));
 	LocUtil _LocUtil = new LocUtil();
 	_LocUtil.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
 	try{
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      ht.put("AssignedLoc",AssignedLoc);
      ht.put("AssignedUser",AssignedUser);
      
         locQryList=      _LocUtil.getLocDetails(LOC_ID,PLANT," ",ht);
      if(locQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
  
 	}catch(Exception e) {mLogger.exception(true,
			"ERROR IN JSP PAGE - AssignInventoryLocation.jsp ", e); }
	}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
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

<FORM class="form-horizontal" name="form1" method="post" action="locSummary.jsp">
  <input type="hidden" name="xlAction" value="">
  <input type="hidden" name="PGaction" value="View">
  <INPUT type = "hidden" name="LOC_ID1" value ="">
  <INPUT type = "hidden" name="LOC_DESC" value ="">
  <INPUT type = "hidden" name="REMARKS" value ="">
  <input type="hidden" name="plant" value="<%=PLANT%>">
  <INPUT type = "hidden" name="COMNAME" value ="">
  <INPUT type = "hidden" name="RCBNO" value ="">
  <INPUT type = "hidden" name="ADDR1" value ="">
  <INPUT type = "hidden" name="ADDR2" value ="">
  <INPUT type = "hidden" name="ADDR3" value ="">
  <INPUT type = "hidden" name="ADDR4" value ="">
  <INPUT type = "hidden" name="STATE" value ="">
  <INPUT type = "hidden" name="COUNTRY" value ="">
  <INPUT type = "hidden" name="ZIP" value ="">
  <INPUT type = "hidden" name="TELNO" value ="">
  <INPUT type = "hidden" name="FAX" value ="">
  <INPUT type = "hidden" name="CHK_ADDRESS" value ="Y">
  <INPUT type = "hidden" name="LOC_TYPE_ID" value ="">
  <INPUT type = "hidden" name="UI_PKEY" value ="">
  <INPUT name="ACTIVE" type = "hidden" value="Y"   >
  <INPUT name="ACTIVE" type = "hidden" value="N"   >
  <INPUT name="COMPANY" type="hidden" value="<%=PLANT%>" size="20" MAXLENGTH="20"/>
  <INPUT name="USER_LEVEL" type = "hidden" value=""   >
  <INPUT name="LOC_TP_ID"  name="LOC_TYPE_ID" type = "hidden" value=""   >
  <INPUT name="chkdDoNo"  name="chkdDoNo" type = "hidden" value=""   >
  
 <div class="form-group" style="display:none;">
<label class="control-label col-sm-4" for="User Id">User Id</label>
<div class="col-sm-3 ac-box" >
<div class="input-group">
   <INPUT class="ac-selected  form-control typeahead" name="USER" ID="USER" type="TEXT" value="" size="35" MAXLENGTH=20>
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
            <th style="font-size: smaller;">Location</th>
            <th style="font-size: smaller;">Description</th>
            <th style="font-size: smaller;">Location Type One</th>
             <th style="font-size: smaller;">Location Type Two</th>
             <th style="font-size: smaller;">Location Type Three</th>
          </tr>  
        </thead> 
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
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
		var LOCTYPE1,LOCTYPE2,LOCTYPE3,LOCT,USER, groupRowColSpan = 6;

		function onAssign(){
			var checkFound = false;
			var len = document.form1.chkdDoNo.length;
			var USER   = document.form1.USER.value;
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
// 			if(USER == "" || USER == null) {alert("Please Enter/Select User"); return false; }
// 			form1.setAttribute("target", "_blank");
// 			form1.setAttribute("target");
				document.form1.action  = "/track/userlocservlet?action=Assigninv";
		   		document.form1.submit();
		}

		function getParameters(){
			return {
				"LOCTYPE1":LOCTYPE1,
				"LOCTYPE2":LOCTYPE2,
				"LOCTYPE3":LOCTYPE3,
				"LOCT":LOCT,
				"PLANT":plant,
				"USER":USER,
				"UI_PKEY":UI_PKEY,
				"action":"GET_ASSIGNINVLOC_FOR_SUMMARY"
			}
		}
		function onGo(){
			LOCTYPE1=document.form1.LOC_TP_ID.value;
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
					        	
						        	if(typeof data.SUPPLIERTYPELIST[0].LOC === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.SUPPLIERTYPELIST.length; dataIndex ++){
						        				var lineno = data.SUPPLIERTYPELIST[dataIndex].LOC;
						        				var sno=dataIndex+1;
						        				data.SUPPLIERTYPELIST[dataIndex]['SNO'] = sno;
						        				if (data.SUPPLIERTYPELIST[dataIndex]['CHK'] == 'yes')
						        				data.SUPPLIERTYPELIST[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; checked name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['LOC']+'" >';
						        				else
						        				data.SUPPLIERTYPELIST[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['LOC']+'" >';
						        			}
						        		return data.SUPPLIERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'CHKOB', "orderable": false},
				        	{"data": 'LOC', "orderable": true},
				        	{"data": 'LOCDESC', "orderable": true},
				        	{"data": 'LOC_TYPE_ID', "orderable": true},
				        	{"data": 'LOC_TYPE_ID2', "orderable": true},
				        	{"data": 'LOC_TYPE_ID3', "orderable": true},
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
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	exportOptions: {
			    	                    		columns: [0,1,2,3,4,5]
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
			                     	     doc.content[1].table.body[i][1].alignment = 'center';
			  
			                     	     } 
			                     	      doc.styles.tableHeader.color = 'black';
			                     	      
			                     	        doc['footer']=(function(page, pages) {
			                     	            return {
			                     	                columns: [
			                     	                    '',
			                     	                    {
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
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>