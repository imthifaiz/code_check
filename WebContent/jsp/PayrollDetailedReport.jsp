<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Payroll Report";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",ORDERNO="",CUSTOMER="",PGaction="",invoice="",STATUS="";
PlantMstDAO _PlantMstDAO = new PlantMstDAO();

String systatus = session.getAttribute("SYSTEMNOW").toString();
boolean displaySummaryEmail=false,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("PAYROLL")){
	displaySummaryEmail = ub.isCheckValPay("paysummaryemail", plant,username);
	displaySummaryExport = ub.isCheckValPay("paysummaryexport", plant,username);
	}

String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=du.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");

DateUtils _dateUtils = new DateUtils();
String curDate = _dateUtils.getDate();
String[] splitdate = curDate.split("/");
String cyear = splitdate[2];
String cmonth = splitdate[1];
int cimonth = Integer.valueOf(cmonth);


String curDate1 = _dateUtils.addMonth(curDate, -2);
String[] splitdate1 = curDate1.split("/");
String cyear1 = splitdate1[2];
String cmonth1 = splitdate1[1];
int cimonth1 = Integer.valueOf(cmonth1);



%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_REPORT%>"/>
</jsp:include>
<style type="text/css">
.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}</style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<link rel="stylesheet" type="text/css" href="../jsp/dist/css/bootstrap3-wysihtml5.min.css">
<script type="text/javascript" src="../jsp/dist/js/bootstrap3-wysihtml5.all.min.js"></script>
<link rel="stylesheet" type="text/css" href="../jsp/dist/css/bootstrap-tagsinput.css">
<link rel="stylesheet" type="text/css" href="../jsp/dist/css/bootstrap-multiEmail.css">
<script src="../jsp/dist/js/bootstrap-tagsinput.min.js"></script>	
<script src="dist/js/bootstrap-multiEmail.js"></script>
<script src="dist/js/jquery.toaster.js"></script>

<!-- <script src="https://cdn.jsdelivr.net/alasql/0.3/alasql.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.7.12/xlsx.core.min.js"></script> -->

<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../payroll/reports"><span class="underline-on-hover">Payroll Reports</span> </a></li>                                     
                <li>Payroll Detailed Report</li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
        <div class="box-header menu-drop">
            <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
               <% if (displaySummaryEmail) { %>
              	<button type="button" id="sendemail" class="btn btn-default" style="margin-right: 15px;"
								data-toggle="tooltip"  data-placement="bottom" title="Email">
									<i class="fa fa-envelope-o" aria-hidden="true"></i>
								</button>
								<% } %>  
								
								
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../payroll/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
            </h1>
		</div>
		<div class="container-fluid">
			<form class="form-horizontal" name="form1" method="post" action="" >
				<input type="text" name="plant" value="<%=plant%>" hidden>
				<input type="text" name="tabledata" hidden>
				<div id="target" style="display:none;padding-left: 15px;">
				<div class="form-group">
					<div class="row">
						<div class="col-sm-2">		    	 
				  			<select name="selfilter" class="form-control" onclick="searchfilter(this)">
							    <option selected value='1'>Between Months</option>
							    <option value='2'>Single Month</option>
							    <option value='3'>This Month</option>
							    <option value='4'>Last Quarter</option>
							    <option value='5'>This Year</option>
   							</select> 
				  		</div>
				  		<div class="col-sm-2 selfrommonth">
							<select name="from_month" class="form-control" id="frommonth">
							    <option selected value=''>Select From Month</option>
							    <option value='1'>January</option>
							    <option value='2'>February</option>
							    <option value='3'>March</option>
							    <option value='4'>April</option>
							    <option value='5'>May</option>
							    <option value='6'>June</option>
							    <option value='7'>July</option>
							    <option value='8'>August</option>
							    <option value='9'>September</option>
							    <option value='10'>October</option>
							    <option value='11'>November</option>
							    <option value='12'>December</option>
   							</select> 
				  		</div>
				  		<div class="col-sm-2 selfromyear">
				  			<select class="form-control text-left pay_year" name="from_year" id="fromyear">
								<option selected value=''>Select From Year</option>
							</select>
				  		</div>
				  		<div class="col-sm-2 seltomonth">
				  			<select name="to_month" class="form-control" id="tomonth">
							    <option selected value=''>Select To Month</option>
							    <option value='1'>January</option>
							    <option value='2'>February</option>
							    <option value='3'>March</option>
							    <option value='4'>April</option>
							    <option value='5'>May</option>
							    <option value='6'>June</option>
							    <option value='7'>July</option>
							    <option value='8'>August</option>
							    <option value='9'>September</option>
							    <option value='10'>October</option>
							    <option value='11'>November</option>
							    <option value='12'>December</option>
   							</select> 
				  		</div>
				  		<div class="col-sm-2 seltoyear">
				  			<select class="form-control text-left pay_year" name="to_year" id="toyear">
								<option selected value=''>Select To Year</option>
							</select>
				  		</div>
				  		<div class="col-sm-2 ac-box">
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
					<div class="row">
						<div class="col-sm-12">
						<!-- <font face="Proxima Nova" > -->                
				              <table id="tablePayrollReport" class="table table-bordred table-striped">                   
				              </table> 
				              <!-- </font> -->    
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>	
<!----- model-------->
<div id="common_email_modal" class="modal fade" role="dialog">
<input type="hidden" id="asofDate"/>
	<div class="modal-dialog">
		<div class="modal-content">
			<form class="form-horizontal" name="common_email">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Email Report</h3>
				</div>
				<div class="modal-body">	
					<!-- <div class="alert alert-danger">
					  test
					</div> -->
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">To</label>			
	                     <div class="col-sm-12">   
	                     		                  
								<input class="form-control" name="send_to" id="send_to" style="width: 100%"></input>
							</div>
					</div>	
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">CC</label>				
	                     <div class="col-sm-12">                     
								<input class="form-control" name="send_cc" id="send_cc" style="width: 100%"></input>
							</div>
					</div>	
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Subject</label>				
	                     <div class="col-sm-12">                     
								<input class="form-control" name="send_subject" id="send_subject" value="<%=title%>" style="width: 100%"></input>
							</div>
					</div>		
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Body</label>
						<div class="col-sm-12">
							<textarea rows="5" cols="23" class="form-control" name="send_body" id="send_body"></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 " id="attachment_label">Report</label>				
	                     <div class="col-sm-12">  
	                     	<div class="row"> 
	                     		<div class="col-sm-12 form-inline">  
	                     			                
									<input class="form-control" name="send_attachment" id="send_attachment" value="<%=title%>" style="width: 95%"></input>
									<label for="send_attachment">.xlsx</label> 
								</div>	
							</div>	
							</div>
					</div>
				<div class="progress progress-sm active" style="margin-right: -8px;display:none;" id="progressView" >
                <div class="progress-bar progress-bar-success progress-bar-striped" id="progressBar"
                role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" >
                  <span class="sr-only">100% Complete</span>
                </div>
              </div>
				</div>				
				<div class="modal-footer">
					<button type="button" class="btn btn-success pull-right" id="send_email">Send</button>
					<button class="btn btn-default pull-left" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
				<input type="hidden" id="pdfname" value="<%=title%>"/>
				
				
					
			</form>
		</div>
		

	</div>

</div>

<!----- model-------->


<script type="text/javascript" src="//unpkg.com/xlsx/dist/xlsx.full.min.js"></script>
		 <script>
 $(document).ready(function(){
	 	var start1 = new Date().getFullYear();
	 	var start = parseFloat(start1)-parseFloat("10");
	    var end = parseFloat(start1)+parseFloat("100");
	    var options = "";
	    for(var year = start ; year <=end; year++){
	    	  $('.pay_year')
	          .append($("<option></option>")
	                     .attr("value", year)
	                     .text(year)); 
	    }
	 
	var dataSet = [];
	var my_columns = [];
	 var urlStr = "/track/PayrollServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				CMD:"GET_PAYROLL_REPORT"
			},
	        success: function (data) {
	        	var objc = JSON.parse(data);
	        	$('input[name ="tabledata"]').val(data);
	        	dataSet = objc.PAYROLL;
	        	var keys = Object.keys(objc.PAYROLL[0]);
                $.each(keys, function (i, it) {
                	my_columns[i] = { data: it, title: it };
                });
                console.log(dataSet);
                console.log(my_columns);
                $('#tablePayrollReport').DataTable({
                    data: dataSet,
                    "columns": my_columns,
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
				                    /* exportOptions: {
				                        columns: [0,1,2,3,4,5]
				                    } */
			                    },
			                    {
			                    	extend : 'pdf',
			                    	/* exportOptions: {
			                    		columns: [':visible']
			                    	}, */
		                    		orientation: 'landscape',
		                            pageSize: 'A4',
		                            	extend : 'pdfHtml5',
		    	                    	exportOptions: {
		    	                    		columns: [':visible']
		    	                    	},
		    	                    	/* exportOptions: {
				                            columns: [0,1,2,3,4,5]
				                        }, */
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
		                    /* columns: ':not(:eq('+groupColumn+')):not(:last)' */
		                }		                
			        ],
			        "order": [],
			        drawCallback: function() {
			        	<%if(!displaySummaryExport){ %>
			        	$('.buttons-collection')[0].style.display = 'none';
			        	<% } %>
			        	}	
                });
	        }
		});
		
		
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
 
 
 function onGo(){
	var filtervalue = document.form1.selfilter.value;
	var frommonth = document.form1.from_month.value;
	var fromyear = document.form1.from_year.value;
	var tomonth = document.form1.to_month.value;
	var toyear = document.form1.to_year.value;
	
	if(filtervalue == "1"){
		if(frommonth == ""){
			alert("Please select from month");
			document.form1.from_month.focus();
			return false;
		}
		
		if(fromyear == ""){
			alert("Please select from year");
			document.form1.from_year.focus();
			return false;
		}
		
		if(tomonth == ""){
			alert("Please select to month");
			document.form1.to_month.focus();
			return false;
		}
		
		if(toyear == ""){
			alert("Please select to year");
			document.form1.to_year.focus();
			return false;
		}

	}
	if(filtervalue == "2"){
		if(frommonth == ""){
			alert("Please select from month");
			document.form1.from_month.focus();
			return false;
		}
		
		if(fromyear == ""){
			alert("Please select from year");
			document.form1.from_year.focus();
			return false;
		}	
	}
	$("#tablePayrollReport").DataTable().destroy();
	$("#tablePayrollReport > tbody").html("");
	var dataSet1 = [];
	var my_columns1 = [];
	 var urlStr = "/track/PayrollServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				CMD:"GET_PAYROLL_REPORT_FILTER",
				FILTER:filtervalue,
				FM:frommonth,
				FY:fromyear,
				TM:tomonth,
				TY:toyear
			},
	        success: function (data) {
	        	var objc = JSON.parse(data);
	        	$('input[name ="tabledata"]').val(data);
	        	dataSet1 = objc.PAYROLL;
	        	var keys = Object.keys(objc.PAYROLL[0]);
                $.each(keys, function (i, it) {
                	my_columns1[i] = { data: it, title: it };
                });
                console.log(dataSet1);
                console.log(my_columns1);
                $('#tablePayrollReport').DataTable({
                    data: dataSet1,
                    "columns": my_columns1,
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
				                    /* exportOptions: {
				                        columns: [0,1,2,3,4,5]
				                    } */
			                    },
			                    {
			                    	extend : 'pdf',
			                    	/* exportOptions: {
			                    		columns: [':visible']
			                    	}, */
		                    		orientation: 'landscape',
		                            pageSize: 'A4',
		                            	extend : 'pdfHtml5',
		    	                    	exportOptions: {
		    	                    		columns: [':visible']
		    	                    	},
		    	                    	/* exportOptions: {
				                            columns: [0,1,2,3,4,5]
				                        }, */
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
		                    /* columns: ':not(:eq('+groupColumn+')):not(:last)' */
		                }		                
			        ],
                });
	        }
		});
	
 }
 
 function searchfilter(obj){
	 var value = $(obj).val();
	 var cmonth = "<%=cimonth%>";
	 var cyear = "<%=cyear%>";
	 
	 var threemonth = "<%=cimonth1%>";
	 var threeyear = "<%=cyear1%>";
	 
	 var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
	 $('#pizza_kind').prop('disabled', false);
	 if(value == "1"){
		 $('#frommonth').prop('disabled', false);
		 $('#fromyear').prop('disabled', false);
		 $('#tomonth').prop('disabled', false);
		 $('#toyear').prop('disabled', false);
		 
		 $('.selfrommonth option:eq(0)').prop('selected', true);
		 $('.selfromyear option:eq(0)').prop('selected', true);
		 $('.seltoyear option:eq(0)').prop('selected', true);
		 $('.seltomonth option:eq(0)').prop('selected', true);
	 }
	 
	 if(value == "2"){
		 $('#frommonth').prop('disabled', false);
		 $('#fromyear').prop('disabled', false);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
		 $('.selfrommonth option:eq(0)').prop('selected', true);
		 $('.selfromyear option:eq(0)').prop('selected', true);
		 $('.seltoyear option:eq(0)').prop('selected', true);
		 $('.seltomonth option:eq(0)').prop('selected', true);
	 }
	 
	 if(value == "3"){
		 var eq = parseInt(cmonth);
		 console.log(cyear);
		 $('#fromyear').val(cyear);
		 $('.selfrommonth option:eq('+eq+')').prop('selected', true);
		 
		 $('#frommonth').prop('disabled', true);
		 $('#fromyear').prop('disabled', true);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
		 
		 $('.seltoyear option:eq(0)').prop('selected', true);
		 $('.seltomonth option:eq(0)').prop('selected', true);
	 }
	 
	 if(value == "4"){
		 var eq = parseInt(cmonth);
		 var eq3 = parseInt(threemonth);
		 
		 $('#fromyear').val(threeyear);
		 $('.selfrommonth option:eq('+eq3+')').prop('selected', true);
		 
		 $('#toyear').val(cyear);
		 $('.seltomonth option:eq('+eq+')').prop('selected', true);
		 
		 $('#frommonth').prop('disabled', true);
		 $('#fromyear').prop('disabled', true);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
	 }
	 
	 if(value == "5"){
		 var eq = parseInt(cmonth);
		 console.log(cyear);
		 $('#fromyear').val(cyear);
		 $('.selfrommonth option:eq(1)').prop('selected', true);
		 
		 $('#toyear').val(cyear);
		 $('.seltomonth option:eq(12)').prop('selected', true);
		 
		 $('#frommonth').prop('disabled', true);
		 $('#fromyear').prop('disabled', true);
		 $('#tomonth').prop('disabled', true);
		 $('#toyear').prop('disabled', true);
		 
	 }
 }
 
 
	$("#sendemail").click(function(){
		$('#common_email_modal').modal('toggle');
		loadBodyStyle();
	});
 </script>
 
 <script>
var i = 0;
function progressBar() {
	$("#progressView").show();
  if (i == 0) {
    i = 1;
    var elem = document.getElementById("progressBar");
    var width = 1;
    var id = setInterval(frame, 50);
    function frame() {
      if (width >= 100) {
        clearInterval(id);
        i = 0;
      } else {
        width++;
        elem.style.width = width + "%";
      }
    }
  }
}
$(document).ready(function($){
	  $("#send_cc").multiEmail();
	  
	});
var emailBody;

$("#send_email").click(function(){
	$('#send_email').prop('disabled', true);
	$('#cancel').prop('disabled', true);
	toDateFormatted   = moment().format('YYYY-MM-DD');
	var date=moment(toDateFormatted).format('MMM DD,YYYY');
	var mailInnerContent=$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html();
	emailBody='<html>'+ 
	'<head> <title>Page Title</title> </head> '+
	'<body> <table width="100%" border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td align="center" valign="top" bgcolor="#FFFFFF"> '+
	'<table width="553" border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td colspan="3" align="left" valign="top" bgcolor="#6e8ba8"> '+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="7" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table>'+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td width="24"></td>'+  
	'<td align="left" style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#ffffff"> <%=CNAME%> </td> </tr> </tbody> </table>'+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="7" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> </td> </tr> <tr> <td width="2" bgcolor="#c0d0e4"></td> <td> <table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td width="549" bgcolor="#e6ecf4"> '+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> <table width="549" border="0" cellpadding="0" cellspacing="0"> <tbody> <tr> <td width="24"></td> '+ 
	'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> <%=title%> </span> <br> '+ 
	'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px">As of '+date+'</span> </td> </tr> </tbody> </table>'+  
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> </td> </tr> </tbody> </table>'+  
	'<table width="549" border="0" cellpadding="0" cellspacing="0"> <tbody> <tr> <td align="left" valign="top" style="padding-left: 23px;"> <table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="20" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table>'+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table>'+  
	'<div style="font-size:14px;color:#000;font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;margin-left:22px;margin-right:22px;line-height:16px">'+mailInnerContent+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> '+ 
	'</td> <td width="2" bgcolor="#c0d0e4"></td> </tr> <tr> <td colspan="3" align="right" bgcolor="#1f3246" style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;color:#ffffff;font-size:11px"> '+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> <table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> '+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> '+ 
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </body> </html> ';
	var isCheck=false;
	if($("#send_to").val()==null || $("#send_to").val()==""){
	    alert("Enter the to email");
		$("#send_to").focus();
		isCheck=true;
		return false;
	}if($("#send_subject").val()==null || $("#send_subject").val()==""){
		alert("Enter the to subject ");
		$("#send_subject").focus();
		isCheck=true;
		return false;
	} if($("#send_body").val()==null || $("#send_body").val()==""){
		alert("Enter the body message ");
		$("#send_body").focus();
		isCheck=true;
		return false;
	} if($("#send_attachment").val()==null || $("#send_attachment").val()==""){
		alert("Enter the to attachment name ");
		$("#send_attachment").focus();
		isCheck=true;
		return false;
	}if($("#send_to").val()!=null){
		var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
		var emailaddressVal=$("#send_to").val();
		if(!emailReg.test(emailaddressVal)){
			alert("Enter a valid sender email id");
			$("#send_to").focus();
			isCheck=true;
			return false;
		}
	}if($("#send_cc").val()!=null && $("#send_cc").val()!=""){
		//var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
		var emailReg=/^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9\-]+\.)+([a-zA-Z0-9\-\.]+)+([,]([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9\-]+\.)+([a-zA-Z0-9\-\.]+))*$/;
		var emailaddressVal=$("#send_cc").val();
		if(!emailReg.test(emailaddressVal)){
			alert("Enter a valid cc email id");
			$("#send_cc").focus();
			isCheck=true;
			return false;
		}
	}
		if(isCheck==false){
	var attachName=$("#send_attachment").val();
	generateEmail(attachName);
	}
	
});

function generateEmail(attachName){	
	var tabledata = document.form1.tabledata.value;
	var objc = JSON.parse(tabledata);

    var dataSet1 = [];									
   	var my_columns1 = [];									
    var keys = Object.keys(objc.PAYROLL[0]);
	$.each(keys, function (i, it) {
    	my_columns1[i] = it;
    });									
    var createXLSLFormatObj = [];

    var xlsHeader = my_columns1;
    var xlsRows = objc.PAYROLL;

    createXLSLFormatObj.push(xlsHeader);
    $.each(xlsRows, function(index, value) {
    	var innerRowData = [];
    	$.each(value, function(ind, val) {
			innerRowData.push(val);
    	});
    	createXLSLFormatObj.push(innerRowData);
    });

	var filename = attachName+".xlsx";

    var ws_name = "Sheet1";

    if (typeof console !== 'undefined') console.log(new Date());
	var wb = XLSX.utils.book_new(),
    	ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);
	var wkbook = XLSX.utils.book_append_sheet(wb, ws, ws_name);

    							        
    if (typeof console !== 'undefined') console.log(new Date());
	 //XLSX.writeFile(wb, filename);
    if (typeof console !== 'undefined') console.log(new Date());
   	var wbout = XLSX.write(wb, {bookType:'xlsx', type:'binary'});
  	var PayrollReport = new Blob([s2ab(wbout)],{type:"application/octet-stream"});
	formData = new FormData();

	formData.append("file", PayrollReport);
	formData.append("filename", filename);
	progressBar();
	sendMailTemplate(formData);	  
}

function s2ab(s) {
	var buf = new ArrayBuffer(s.length);
	var view = new Uint8Array(buf);
	for (var i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
	return buf;
}
function sendMailTemplate(pdfForm)
{
	var tabledata = document.form1.tabledata.value;
	var sendTo=$("#send_to").val();
	var sendCC=$("#send_cc").val();
	var sendSubject=$("#send_subject").val();
	var sendBody=$("#send_body").val();
	pdfForm.append("send_to",sendTo);
	pdfForm.append("send_cc",sendCC);
	pdfForm.append("send_subject",sendSubject);
	pdfForm.append("send_body",emailBody);
	pdfForm.append("tabledata",tabledata);

	$.ajax({
		type : 'post',
		url : "/track/PayrollServlet?Submit=sendEmail",
		dataType: "json",
	    processData: false,  // Important!
	    contentType: false,
		data : pdfForm,//{key:val}
		success : function(data) {
			if (data.STATUS == "200") {	
				$("#progressView").hide();
				$.toaster({ priority: 'success', title: '', message: 'Email Sent Successfully!' });
				//alert("Email Send Successfully!");
				$('#common_email_modal').modal('toggle');
				clear();
				$('#send_email').prop('disabled', false);
				$('#cancel').prop('disabled', false);
				
			}
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
}
function clear(){
	$("#send_to").val('');
	$("#send_cc").val('');
	$("#send_subject").val('');
	$("#send_attachment").val('');
	$("#send_subject").val($("#pdfname").val());
	$("#send_attachment").val($("#pdfname").val());
}
$(document).ready(function() {
	$('#send_body').wysihtml5();
	 
});
function loadBodyStyle(){
	//$('#send_body').val('');
	var	toDateFormatted   = moment().format('YYYY-MM-DD');
	var date=moment(toDateFormatted).format('MMM DD,YYYY');
	
	var periodlabel="As of "+date;
	var htmlbody="";
	var plant="<%=CNAME%>";
	var title="<%=title%>";
	var user="<%=username%>";


	htmlbody='<div>'+plant+'</div>'+
	'<div>'+title+'</div>'+
	'<div>'+periodlabel+'</div>'+
	'<div>Hello</div><br/>'+
	'<div>Attached is '+title+ ' for '+plant+ '</div><br/>'+
	'<div>Regards</div>'+
	'<div>'+user+'</div><br/>';
	
	$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(htmlbody);
	
}

window.saveFile = function saveFile () {
	var tabledata = document.form1.tabledata.value;
	var objc = JSON.parse(tabledata);
	var sheet_1_data = objc.PAYROLL;
   // var sheet_1_data = [{Col_One:1, Col_Two:11}, {Col_One:2, Col_Two:22}];
    var opts = [{sheetid:'Sheet One',header:true}];
    var result = alasql('SELECT * INTO XLSX("sample_file.xlsx",?) FROM ?', 
    									[opts,[sheet_1_data]]);
}
</script>

<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>