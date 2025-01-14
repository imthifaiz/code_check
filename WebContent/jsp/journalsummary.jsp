<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.util.StrUtils"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Manual Journals Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",PGaction="";
	
	boolean displaySummaryNew=false,displaySummaryLink=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryNew = ub.isCheckValAcc("newjournal", plant,username);
		displaySummaryLink = ub.isCheckValAcc("summarylnkjournal", plant,username);
	}
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String collectionDate=du.getDate();
	ArrayList al = plantMstDAO.getPlantMstDetails(plant);
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
	//RESVI
	String curDate =du.getDateMinusDays();
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
FROM_DATE=du.getDateinddmmyyyy(curDate);
%>
<%@include file="sessionCheck.jsp"%>

<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="submenu" value="<%=IConstants.JOURNAL_ENTRY%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<div class="container-fluid m-t-20">

	<div class="box">
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                         
                <li><label>Manual Journals Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">

			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<% if (displaySummaryNew) { %>
			<a class="btn btn-default pull-right" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" href="../banking/createjournal">+ New</a>
			<% } %>	
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form" method="post" action="" >
		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
		<INPUT type = "hidden" name="NOOFDECIMAL" value="<%=numberOfDecimal%>">
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
  		<input type="text" class="ac-selected form-control" id="REFERENCE" name="REFERENCE"  placeholder="REFERENCE">				
				</div>
			</div>
  		</div>  		
  	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
				
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
      
        </div>
       	  </div>
		
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
		<div class="searchType-filter">
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
            </div>
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableJournalSummary" class="table table-bordred table-striped">                   
                   <thead>
                   <th style="font-size: smaller;">DATE</th>
                    <th style="font-size: smaller;">JOURNAL#</th>
                    <th style="font-size: smaller;">REFERENCE NUMBER#</th>
                     <th style="font-size: smaller;">STATUS</th>
                     <th style="font-size: smaller;">NOTES</th>
                     <th style="font-size: smaller;">AMOUNT</th>
                     <th style="font-size: smaller;">CREATED BY</th>                    
                   </thead>
                   <tfoot align="right" style="display: none;">
		<tr><th></th><th></th><th></th></tr>
	</tfoot>
              </table> 
              <!-- </font> -->    
		</div>
		</div>
		</div>
		</FORM>
		</div>


	</div>
</div>

<script type="text/javascript">

var tableJournalSummary;
var FROM_DATE,TO_DATE,USER,SUPPLIER,CURENCY,ORDERNO,GRNO,STATUS, groupRowColSpan = 6;
function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"REFERENCE":REFERENCE,
		"action": "VIEW_JOURNAL_SUMMARY",
		"PLANT":"<%=plant%>",
		TRAN_TYPE : "JOURNAL"
	}
}  
  function onGo(){
   var flag    = "false";
    FROM_DATE      = document.form.FROM_DATE.value;
    TO_DATE        = document.form.TO_DATE.value;		    
    REFERENCE         = document.form.REFERENCE.value;		    
   	    		   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   
   if(REFERENCE != null    && REFERENCE != "") { flag = true;}

    var urlStr = "../JournalServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableJournalSummary){
    	tableJournalSummary.ajax.url( urlStr ).load();
    }else{
    	tableJournalSummary = $('#tableJournalSummary').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
			searching: true, // Enable searching
            search: {
                regex: true,   // Enable regular expression searching
                smart: false   // Disable smart searching for custom matching logic
            },
//				"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].ID === 'undefined'){
		        		return [];
		        	}else {				        		
		        			for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			<% if (displaySummaryLink) { %>
		        			data.items[dataIndex]['ID'] = '<a href="../banking/journaldetail?ID=' +data.items[dataIndex]['ID']+ '">'+data.items[dataIndex]['ID']+'</a>';
		        			<% }%>
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'JOURNAL_DATE', "orderable": true},
    			{"data": 'ID', "orderable": true},
    			{"data": 'REFERENCE', "orderable": true},
    			{"data": 'JOURNAL_STATUS', "orderable": true},		    			
    			{"data": 'NOTE', "orderable": true},
    			{"data": 'TOTAL_AMOUNT', "orderable": true},
    			{"data": 'CRBY', "orderable": true},
    			
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
	    	                	columns: [':visible']
	    	                },
	    	                title: '<%=title%>',
	    	                footer: true
	                    },
	                    {
	                    	extend : 'pdf',
                            footer: true,
	                    	text: 'PDF Portrait',
	                    	exportOptions: {
	                    		columns: [':visible']
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
	                    		columns: [':visible']
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
    	$("#tableJournalSummary_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
        	tableJournalSummary.draw();
        });
    }
}
var numberOfDecimal = $("#numberOfDecimal").val();
var tableDataNested;
	$(document).ready(function() {
		onGo();
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
					});
	
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>