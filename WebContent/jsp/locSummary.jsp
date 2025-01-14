<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<!-- resvi -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<!-- resvi -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "Location Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  	}
 	function onGo()
	{ 
 		document.form1.action ="../jsp/locSummary.jsp"
     document.form1.submit();
	}
 	
 	function ExportReport(){
 		document.form1.action = "/track/locmstservlet?action=Loc_Export_Excel";
 		document.form1.submit();
 		
 	} 
</script>

<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	ArrayList locQryList  = new ArrayList();
	
	_userBean.setmLogger(mLogger);
	String fieldDesc="";
	String PLANT="",LOC_ID ="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="";
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	LOC_ID     = StrUtils.fString(request.getParameter("LOC_ID"));
	LOC_TYPE_ID     = StrUtils.fString(request.getParameter("LOC_TP_ID"));
	LOC_TYPE_ID2    = StrUtils.fString(request.getParameter("LOC_TP_ID2"));
	LOC_TYPE_ID3    = StrUtils.fString(request.getParameter("LOC_TP_ID3"));
	fieldDesc=StrUtils.fString(request.getParameter("result"));
	/* resvi */
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String collectionDate=DateUtils.getDate();
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
	/* resvi */
	
	boolean displaySummaryImport=false,displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryLink = ub.isCheckValAcc("summarylnkloc", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editloc", PLANT,USERID);
	displaySummaryNew = ub.isCheckValAcc("newloc", PLANT,USERID);
	displaySummaryImport = ub.isCheckValAcc("importloc", PLANT,USERID);

	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryLink = ub.isCheckValinv("summarylnkloc", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValinv("editloc", PLANT,USERID);
	displaySummaryNew = ub.isCheckValinv("newloc", PLANT,USERID);
	displaySummaryImport = ub.isCheckValinv("importloc", PLANT,USERID);
	}
	
 	LocUtil _LocUtil = new LocUtil();
 	_LocUtil.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
 	try{
      Hashtable ht = new Hashtable();
      if(StrUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
      ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
      
         locQryList=      _LocUtil.getLocDetails(LOC_ID,PLANT," ",ht);
      if(locQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
  
 	}catch(Exception e) {mLogger.exception(true,
			"ERROR IN JSP PAGE - locSummary.jsp ", e); }
	}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Location Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
         <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
               <%if(displaySummaryImport){ %>
              <button type="button" class="btn btn-default"
						onclick="window.location.href='../location/import'">
						Import Location</button>
					&nbsp;
					  <%}%>
				</div>
				
				<div class="btn-group" role="group">
				<%if(displaySummaryNew){ %>
					<button type="button" class="btn btn-default"
						onclick="window.location.href='../location/new'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
					 <%}%>
				</div>
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>   
		
 <div class="box-body">
 
 <%-- <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div> --%>
<FORM class="form-horizontal" name="form1" method="post" action="locSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT type = "hidden" name="LOC_ID1" value ="">
<INPUT type = "hidden" name="LOC_DESC" value ="">
<INPUT type = "hidden" name="REMARKS" value ="">
<input type="hidden" name="plant" value="<%=PLANT%>">
  <!-- below lines added by deen --> 
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
  <!-- end --> 
	<INPUT name="ACTIVE" type = "hidden" value="Y"   >
    <INPUT name="ACTIVE" type = "hidden" value="N"   >
  
 <div class="form-group">
<label class="control-label col-sm-2" for="Location Type">Location Type One</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
   <INPUT class="ac-selected  form-control typeahead" name="LOC_TP_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID%>" size="35" MAXLENGTH=20>
  <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TP_ID.value+'&TYPE=locsummary');"> -->
<!-- <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
</div>
</div>

<label class="control-label col-sm-2" for="Location Type">Location Type Two</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
<INPUT class="ac-selected  form-control typeahead" name="LOC_TP_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=LOC_TYPE_ID%>"  size="35" MAXLENGTH=20>
<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TP_ID2.value+'&TYPE=locsummary');"> -->
<!-- <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
</div>
</div>

</div>
<div class="form-group">

<label class="control-label col-sm-2" for="Location Type">Location Type Three</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
<INPUT class="ac-selected  form-control typeahead" name="LOC_TP_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=LOC_TYPE_ID%>"  size="35" MAXLENGTH=20>
<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
</div>
</div>

<label class="control-label col-sm-2" for="Location">Location</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
 <INPUT class="ac-selected  form-control typeahead" name="LOC_ID" ID="LOC_ID" type ="TEXT" value="<%=LOC_ID%>"  MAXLENGTH=20>
  <span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('Loc_list.jsp?LOC_ID='+form1.LOC_ID.value+'&LOC_TYPE_ID='+form1.LOC_TP_ID.value);"> -->
<!--  <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  	</div>
  	</div>
<!--   	<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp; -->
  	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
  	</div>
  	
  		<div class="row" style="padding:3px">
  		<div class="col-sm-5">
  	</div>
			<div class="col-sm-6 txn-buttons">
			<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
			</div>
		</div>  	
  	
  </FORM>
  
  
  <br>	
		
		
<div style="overflow-x:auto;">

<div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		                  </div>
<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center"> 
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">Location</th>
            <th style="font-size: smaller;">Description</th>
            <th style="font-size: smaller;">Location Type One</th>
             <th style="font-size: smaller;">Location Type Two</th>
             <th style="font-size: smaller;">Location Type Three</th>
            <th style="font-size: smaller;">IsActive</th>
            <th style="font-size: smaller;">Edit</th>      
          </tr>  
        </thead> 
         <!-- resvi --> 
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>								
							</tr>
						</tfoot>
						<!-- resvi -->
        
       <%--  <tbody>
 
   	  <%
         for (int iCnt =0; iCnt<locQryList.size(); iCnt++){
			    int iIndex = iCnt + 1;
         		String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           		Map lineArr = (Map) locQryList.get(iCnt);
       %>

          <TR>
	          <TD align="center"><%=iIndex%></TD>
	            <% if (displaySummaryLink) { %>
	           <TD align="center" class="textbold"> &nbsp;
	              <a href ="locDetail.jsp?action=View&LOC=<%=(String)lineArr.get("LOC")%>&LOCDESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("LOCDESC"))%>&USERFLD1=<%=(String)lineArr.get("USERFLD1")%>&COMNAME=<%=(String)lineArr.get("COMNAME")%>&RCBNO=<%=(String)lineArr.get("RCBNO")%>&ADD1=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADD1"))%>&ADD2=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADD2"))%>&ADD3=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADD3"))%>&ADD4=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADD4"))%>&STATE=<%=(String)lineArr.get("STATE")%>&COUNTRY=<%=(String)lineArr.get("COUNTRY")%>&ZIP=<%=(String)lineArr.get("ZIP")%>&TELNO=<%=(String)lineArr.get("TELNO")%>&FAX=<%=(String)lineArr.get("FAX")%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>&LOC_TYPE_ID=<%=(String)lineArr.get("LOC_TYPE_ID")%>&LOC_TYPE_ID2=<%=(String)lineArr.get("LOC_TYPE_ID2")%>"><%=(String)lineArr.get("LOC")%></a></TD>
	    	   <% } else { %>
	    	   <TD align="center" class="textbold"> &nbsp;
	    	   <%=(String)lineArr.get("LOC")%></TD>
	    	    <% }%>  
	    	   <TD align="center" class="textbold">&nbsp; <%=StrUtils.fString((String)lineArr.get("LOCDESC"))%></TD>
	           <TD align="center" class="textbold">&nbsp; <%=StrUtils.fString((String)lineArr.get("LOC_TYPE_ID"))%></TD>
	            <TD align="center" class="textbold">&nbsp; <%=StrUtils.fString((String)lineArr.get("LOC_TYPE_ID2"))%></TD>
	           <TD align="center" class="textbold">&nbsp; <%=StrUtils.fString((String)lineArr.get("ISACTIVE"))%></TD>
	           <% if (displaySummaryEdit) { %>
	           <TD align="center" class="textbold">&nbsp; <a href="maint_loc.jsp?action=View&LOC_ID=<%=(String) lineArr.get("LOC")%>&LOC_TYPE_ID=<%=(String)lineArr.get("LOC_TYPE_ID")%>">
		    <i class="fa fa-pencil-square-o"></i></a></TD>
		    <% } else { %>
		    <TD align="center"><a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD>
	        <% }%> 
	       </TR>
       <%}%>
      </tbody>   --%>
</table>
</div>  
<!-- resvi -->
<script>
		var plant = document.form1.plant.value;
		var tabletype;
		var LOCTYPE1,LOCTYPE2,LOCTYPE3,LOCT, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"LOCTYPE1":LOCTYPE1,
				"LOCTYPE2":LOCTYPE2,
				"LOCTYPE3":LOCTYPE3,
				"LOCT":LOCT,
				"PLANT":plant,
				"action":"GET_LOC_FOR_SUMMARY"
			}
		}
		function onGo(){
			LOCTYPE1=document.form1.LOC_TP_ID.value;
			LOCTYPE2=document.form1.LOC_TP_ID2.value;
			LOCTYPE3=document.form1.LOC_TP_ID3.value;
			LOCT=document.form1.LOC_ID.value;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],

						searching: true, // Enable searching
				        search: {
				            regex: true,   // Enable regular expression searching
				            smart: false   // Disable smart searching for custom matching logic
				        },
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
												<%if(displaySummaryEdit){ %>
						        				data.SUPPLIERTYPELIST[dataIndex]['EDIT'] = '<a href="../location/edit?action=View&LOC_ID=' +lineno+'&LOC_TYPE_ID=' +data.SUPPLIERTYPELIST[dataIndex].LOC_TYPE_ID+'&LOC_TYPE_ID2=' +data.SUPPLIERTYPELIST[dataIndex].LOC_TYPE_ID2+'"><i class="fa fa-pencil-square-o"></i></a>';
						        				<%}else{ %>
												data.SUPPLIERTYPELIST[dataIndex]['EDIT'] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href="#" ><i class="fa fa-pencil-square-o"></i></a>';
												<% } %>
												<%if(displaySummaryLink){ %>
												data.SUPPLIERTYPELIST[dataIndex]['LOC'] = '<a href ="../location/detail?action=View&LOC=' +lineno+'&LOCDESC=' +data.SUPPLIERTYPELIST[dataIndex].LOCDESC+'&USERFLD1=' +data.SUPPLIERTYPELIST[dataIndex].USERFLD1+'&COMNAME=' +data.SUPPLIERTYPELIST[dataIndex].COMNAME+'&RCBNO=' +data.SUPPLIERTYPELIST[dataIndex].RCBNO+'&ADD1=' +data.SUPPLIERTYPELIST[dataIndex].ADD1+'&ADD2=' +data.SUPPLIERTYPELIST[dataIndex].ADD2+'&ADD3=' +data.SUPPLIERTYPELIST[dataIndex].ADD3+'&ADD4=' +data.SUPPLIERTYPELIST[dataIndex].ADD4+'&STATE=' +data.SUPPLIERTYPELIST[dataIndex].STATE+'&COUNTRY=' +data.SUPPLIERTYPELIST[dataIndex].COUNTRY+'&ZIP=' +data.SUPPLIERTYPELIST[dataIndex].ZIP+'&TELNO=' +data.SUPPLIERTYPELIST[dataIndex].TELNO+'&FAX=' +data.SUPPLIERTYPELIST[dataIndex].FAX+'&ISACTIVE=' +data.SUPPLIERTYPELIST[dataIndex].ISACTIVE+'&LOC_TYPE_ID=' +data.SUPPLIERTYPELIST[dataIndex].LOC_TYPE_ID+'&LOC_TYPE_ID2=' +data.SUPPLIERTYPELIST[dataIndex].LOC_TYPE_ID2+'&LOC_TYPE_ID3=' +data.SUPPLIERTYPELIST[dataIndex].LOC_TYPE_ID3+'">'+data.SUPPLIERTYPELIST[dataIndex].LOC+'</a>';										
													<% } %>
						        			}
						        		return data.SUPPLIERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'LOC', "orderable": true},
				        	{"data": 'LOCDESC', "orderable": true},
				        	{"data": 'LOC_TYPE_ID', "orderable": true},
				        	{"data": 'LOC_TYPE_ID2', "orderable": true},
				        	{"data": 'LOC_TYPE_ID3', "orderable": true},
				        	{"data": 'ISACTIVE', "orderable": true},					        	
			    			{"data": 'EDIT', "orderable": true},
			    			],
						"orderFixed": [ ], 
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
			                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
			                }		                
				        ],
				        "order": [],

					});
			    	$("#table_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
			            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
			             var searchType = $('#searchType').val(); // Get the selected search type
			            if (!searchTerm) return true; // If no search term, show all rows

			           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
			           var searchPattern;

			            // Define the search pattern based on the selected search type
			            if (searchType === 'first') {
			                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
			            } else if (searchType === 'middle') {
			                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
			            } else if (searchType === 'last') {
			                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
			            }
			            // Check each column in the row for a match
			            for (var i = 0; i < data.length; i++) {
			                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
			                if (columnData.match(searchPattern)) {
			                    return true; // Match found, include this row in results
			                }
			            }
			            return false; // No match, exclude this row from results
			        });

			        // Redraw table when the search input changes
			        $('.dataTables_filter input').on('keyup', function () {
			        	table.draw();
			        });
			    }
			    
			}

		</script>
		<!-- END --> 
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){
/* 	$('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	}); */
	getLocalStorageValue('locSummary_LOC_TYPE_ID', '', 'LOC_TYPE_ID');
	getLocalStorageValue('locSummary_LOC_TYPE_ID2', '', 'LOC_TYPE_ID2');
	getLocalStorageValue('locSummary_LOC_TYPE_ID3', '', 'LOC_TYPE_ID3');
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
    var plant= '<%=PLANT%>'; 

    /* location Auto Suggestion */
	$('#LOC_ID').typeahead({
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
		  display: 'LOC_TYPE_ID2',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETWO_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID2+'</p></div>';
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

	  /* location three Auto Suggestion */
	$('#LOC_TYPE_ID3').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID3',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETHREE_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID3+'</p></div>';
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

$('#LOC_TYPE_ID').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('locSummary_LOC_TYPE_ID', $('#LOC_TYPE_ID').val());	 
});
$('#LOC_TYPE_ID2').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('locSummary_LOC_TYPE_ID2', $('#LOC_TYPE_ID2').val());	 
});
$('#LOC_TYPE_ID3').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('locSummary_LOC_TYPE_ID3', $('#LOC_TYPE_ID3').val());	 
});

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>