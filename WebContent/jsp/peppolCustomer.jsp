<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<!-- IMTIZIAF -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "Peppol Customer Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.INTEGRATIONS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PEPPOL%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css">
<script src="../jsp/js/bootstrap-datepicker.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
 
function ExportReport(){
	document.form.action = "/track/ReportServlet?action=Export_Contacts_Excel&ReportType=Customer";
	document.form.submit();
	
} 
</script>

<%


StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
PlantMstDAO plantMstDAO = new PlantMstDAO();

List locQryList  = new ArrayList();
_userBean.setmLogger(mLogger);
String fieldDesc="";
String PLANT="",CUST_CODE ="",CUSTOMER_TYPE_ID="",customerType="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String CUST_NAME     = StrUtils.fString(request.getParameter("CUST_NAME"));
String plant = (String)session.getAttribute("PLANT");
CUSTOMER_TYPE_ID=StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
String msg =  StrUtils.fString(request.getParameter("Msg"));
fieldDesc =  StrUtils.fString(request.getParameter("result"));
String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry

CustUtil custUtils = new CustUtil();
//IMTIZIAF
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String collectionDate=DateUtils.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
//END
custUtils.setmLogger(mLogger);
if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(StrUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
                 
     
 }catch(Exception e) { }
}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../integrations/peppolintegration"><span class="underline-on-hover">Peppol Integration</span> </a></li>
                <li><label>Peppol Customer Summary</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
  	           <div class="btn-group" role="group">
              <!-- <button type="button" class="btn btn-default value='Export Master Data' "
						onClick="ExportReport();">
						Export Customer Master</button> -->
					&nbsp;
				</div>
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../integrations/peppolintegration'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div> 
            
		
 <div class="box-body">
 
	
<FORM class="form-horizontal" name="form" method="post" action="peppolCustomer.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUST_CODE1" value="<%=CUST_CODE%>">
<input type="hidden" name="CUST_CODE" value="<%=CUST_CODE%>">
<input type="hidden" name="L_CUST_NAME" value="">
<input type="hidden" name="plant" value="<%=PLANT%>">
  
         <% if(COMP_INDUSTRY.equals("Education")){%>
<div class="form-group" style="display:none;" >
<label class="control-label col-form-label col-sm-2" for="Customer Type Desc or ID">Customer Type Id/Desc</label>
<div class="col-sm-4" >

<INPUT name="CUSTOMER_TYPE_ID" id="CUSTOMER_TYPE_ID" type = "TEXT" value="<%=CUSTOMER_TYPE_ID%>" size="20"  MAXLENGTH=100 class="ac-selected form-control">
<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  	
  	</div>
  	</div>
  	<%}else{ %>
<div class="form-group">
<label class="control-label col-form-label col-sm-2" for="Customer Type Desc or ID">Customer Type Id/Desc</label>
<div class="col-sm-4" >

<INPUT name="CUSTOMER_TYPE_ID" id="CUSTOMER_TYPE_ID" type = "TEXT" value="<%=CUSTOMER_TYPE_ID%>" size="20"  MAXLENGTH=100 class="ac-selected form-control">
<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  	
  	</div>
  	</div>
  	<%} %>
<div class="form-group">
<label class="control-label col-form-label col-sm-2" for="Customer Name or ID">Customer Name/ID</label>
<div class="col-sm-4 " >
<INPUT name="CUST_NAME" id="CUST_NAME" type = "TEXT" value="<%=CUST_NAME%>" size="20"  MAXLENGTH=100 class="ac-selected form-control">
<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
  	</div>
  	&nbsp;&nbsp;<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
  	</div>
  	
  </FORM>
  	<br>	
		
<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped " > 
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">Customer ID</th>
            <th style="font-size: smaller;">Customer Name</th>
            <% if(!COMP_INDUSTRY.equals("Education")){%>
            <th style="font-size: smaller;">Customer Type</th>
            <th style="font-size: smaller;">Contact Name</th>  
            <th style="font-size: smaller;">Customer Phone</th>
            <%} %>
            <th style="font-size: smaller;">Peppol Id</th>  
            <th style="font-size: smaller;">IsActive</th>
          </tr>  
        </thead> 
        
         <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
							 <% if(!COMP_INDUSTRY.equals("Education")){%>
								<th></th>
								<th></th>
								<th></th>
							<%} %>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						<!-- END -->
      
</table>
</div>  
<script>
		var plant = document.form.plant.value;
		var tabletype;
		var CUSTOMERTYPE,CUSTOMERNAME, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"CUSTOMERTYPE":CUSTOMERTYPE,
				"CUSTOMERNAME":CUSTOMERNAME,
				"PLANT":plant,
				"action":"GET_PEPPOL_CUSTOMER_FOR_SUMMARY"
			}
		}
		function onGo(){
			CUSTOMERTYPE=document.form.CUSTOMER_TYPE_ID.value;
			CUSTOMERNAME=document.form.CUST_NAME.value;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].CUSTNO === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].CUSTNO;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
						        			}
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'CUSTNO', "orderable": true},
				        	{"data": 'CNAME', "orderable": true},
				            <% if(!COMP_INDUSTRY.equals("Education")){%>
				        	{"data": 'CUSTOMERTYPE', "orderable": true},
				        	{"data": 'NAME', "orderable": true},
				        	{"data": 'TELNO', "orderable": true},
				        	<%} %>
			    			{"data": 'PEPPOL_ID', "orderable": true},
				        	{"data": 'ISACTIVE', "orderable": true},				        	
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
				                    	/* exportOptions: {
				    	                	columns: [':visible']
				    	                } */
					                    exportOptions: {
					                    	columns: [0,1,2,3,4,5,6]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	/* exportOptions: {
				                    		columns: [':visible']
				                    	}, */
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5,6]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	/* exportOptions: {
			    	                    		columns: [':visible']
			    	                    	}, */
			    	                    	exportOptions: {
			    	                    		columns: [0,1,2,3,4,5,6]
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
			                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
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
	getLocalStorageValue('custmerSummary_CUSTOMER_TYPE_ID', '', 'CUSTOMER_TYPE_ID');
	onGo();
	 $('[data-toggle="tooltip"]').tooltip();
	var plant= '<%=plant%>';
	/* Customer Type Auto Suggestion */
	$('#CUSTOMER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTOMER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getCustomerListTypeData",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUST_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.CUSTOMER_TYPE_ID+'</p></div>';
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


	$('#CUST_NAME').typeahead({
		  hint: true,
		  minLength:0,
		  searchOnFocus: true
	},
	{
		display: 'CNAME',
		async: true,
		source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax({
				type : "GET",
				url : urlStr,
				async : true,
				data : {
					ACTION : "GET_CUSTOMER_DATA_PEPPOL",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CUSTMST);
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
			   return '<div> <p class="item-suggestion">' + data.CNAME + '</p> </div>';
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

$('#CUSTOMER_TYPE_ID').on('typeahead:selected', function(evt, item) {
	 storeInLocalStorage('custmerSummary_CUSTOMER_TYPE_ID', $('#CUSTOMER_TYPE_ID').val());	 
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>