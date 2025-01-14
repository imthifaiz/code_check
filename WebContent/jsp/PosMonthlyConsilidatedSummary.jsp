<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<!-- IMTIZIAF -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.OutletBeanDAO"%>
<!-- END -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "POS Revenue Report";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
		<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<style>
/* table thead tr{
    display:block;
}
table th,table td{
    width:100px;
}


table  tbody{
  display:block;
  height:500px;
  overflow:auto;
}  

table  tbody{
  height:500px;
  overflow:auto;
} */
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/ExcelSave.js"></script>
<script src="../jsp/js/exceljs.min.js"></script>


<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();

	String fieldDesc = "";
	String PLANT = "";
	String html = "";
	int Total = 0;
	String SumColor = "";
	
	String FROM_DATE="",TO_DATE="",status="",TERMINAL_CODE ="",sTerminalCode="", OUTLET_CODE="";
	
	
	boolean flag = false;
	session = request.getSession();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg =  StrUtils.fString(request.getParameter("Msg"));
	String types =  StrUtils.fString(request.getParameter("srctype"));
	fieldDesc =  StrUtils.fString(request.getParameter("result"));
	String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
	
	String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
	String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
	Map arrCustLine = new HashMap();
	String finalString="";
	
	/* FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
	String curDate =du.getDateMinusDays();
	FROM_DATE=du.getDateinddmmyyyy(curDate);
	status  = strUtils.fString(request.getParameter("STATUS"));
	sTerminalCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TERMINALCODE"))); */
	
	//IMTIZIAF
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	String collectionDate=DateUtils.getDate();
// 	ArrayList arrCust = new OutletBeanDAO().PaymentModeMstlist(PLANT);
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	//END
	
	int year = Calendar.getInstance().get(Calendar.YEAR);
%>

<script>
	var subWin = null;
	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'GroupSummary',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

	function ExportReport(){
		document.form.action="/track/deleveryorderservlet?Submit=ExportExcelPOSreturnSummary";
		document.form.submit();
	} 
	
/* 	var headerData = ["Header 1", "Header 2", "Header 3"];
	var headerRow = document.querySelector("#headerRow");

	headerData.forEach(header => {
	var th = document.createElement("th");
	th.innerHTML = header;
	headerRow.appendChild(th);
	});
	
	for(let i = 0; i < headerData.length; i++) {
		  let th = document.createElement("th");
		  th.innerHTML = headerData[i];
		  headerRow.appendChild(th);
		} */
</script>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../posreports/reports"><span class="underline-on-hover">POS Reports</span> </a></li>
                <li><label>POS Revenue Report</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
             
				</div>
                            
                          
				</div>

              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../posreports/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
 <div class="box-body">
 
	
<FORM class="form-horizontal" name="form" method="post" action="PosRevenueReportsSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="TERMINAL_CODE" value="">
<INPUT type="hidden" name="TERMINALCODE" value="<%=sTerminalCode%>">
<%-- <input type="hidden" name="srctype" value="<%=searchtype%>"> --%>
<input type="hidden" name="srctype" value="">
</form>
<br>
 


<!-- <div style="overflow-x:auto;"> -->
    <div class="bs-example">

        	<div class="table-responsive tab-pane active" id="salesdetail" style="margin-top: 1%">
        <table id="table" class="table table-bordred table-striped"  style="width: 100%;"> 
        <caption style="display: none"><%=CNAME%></caption> 
     			<thead style="text-align: left">  

		          <tr id="headerRow">  
		             
		            <th style="font-size: smaller;">Terminal</th> 
		            <th style="font-size: smaller;">Terminal name</th> 
		            <th style="font-size: smaller;">Month</th>
		            <th style="font-size: smaller;">Year</th>
		            <%
		            List listQrys =   new OutletBeanDAO().PaymentModeMstlist(PLANT);
		            if (listQrys.size() > 0) {
		            for(int i =0; i<listQrys.size(); i++) {
		            	int iIndex = i + 1;
						Map m=(Map)listQrys.get(i);
						finalString = (String)m.get("PAYMENTMODE");
		
		            %>
		            <th style="font-size: smaller;"><%=finalString%></th>
		            <%
						}}
		            %>
		            <th style="font-size: smaller;">Total Amount</th>
					<th style="font-size: smaller;"></th>
		          </tr>  
		        </thead> 
        </table>
        	</div>
        	
    </div>
<!--     </div> -->
<script>
		var item,fdate,tdate,orderno,terminalname,terminalcode,status,outletcode,outlet_name;
		var tabletype;
		var tablePrint;
		function getParameters(){
			return {
				"action": "POS_SALES_RECIEVE_MONTH_REPORT",
				"PLANT":"<%=PLANT%>"
			}
		}
		function onGo(){
			onGo1();
		}
		function onGo1(){
			 plant = document.form.plant.value;
			   var urlStr = "../PosReport";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[10, 50, 100], [10, 50, 100]],
						"ajax": {
							"type": "POST",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	if(typeof data.POS[0].smonth === 'undefined'){
						        		return [];
						        }else {				        		
						        		for(var dataIndex = 0; dataIndex < data.POS.length; dataIndex ++){
						        			data.POS[dataIndex]['editdata'] = '<button type="button" class="btn btn-default"  onclick="processrecive(\''+data.POS[dataIndex]['outlet']+'\',\''+data.POS[dataIndex]['terminal']+'\',\''+data.POS[dataIndex]['smonth']+'\',\''+data.POS[dataIndex]['syear']+'\')"><i class="fa fa-file-text-o" aria-hidden="true" style="color: #108af4;"></i></button>';
						        		}
						        		return data.POS;
						        }
					        }
					    },
				        "columns": [
				        	{"data": 'terminal', "orderable": true},
			    			{"data": 'terminalname', "orderable": true},
			    			{"data": 'smonth', "orderable": true},
			    			{"data": 'syear', "orderable": true},
			    			 <%
					            if (listQrys.size() > 0) {
					            for(int i =0; i<listQrys.size(); i++) {
					            	int iIndex = i + 1;
									Map m=(Map)listQrys.get(i);
									finalString = (String)m.get("PAYMENTMODE");
					
					            %>
					            {"data": '<%=finalString%>', "orderable": true},
					            <%
									}}
					            %>
			    			
			    			{"data": 'totalamount', "orderable": true},
			    			{"data": 'editdata', "orderable": true}
			    			],
			    			
			    		"columnDefs": [],	
						"orderFixed": [ ], 
						"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
						"<'row'<'col-md-6'><'col-md-6'>>" +
						"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
						buttons: [],
				        "order": []
				       
					});
			                	
			    }
			    
			    //onGo2();
			    
			}
		
		
		
	
		</script>
	  </div>
	  </div>
		  </div>
	
<%@include file="../jsp/NewChartOfAccountpopup.jsp"%> <!-- imti for add account --> 	  
 <script>
 
$(document).ready(function(){
	 	onGo();
	 	$('[data-toggle="tooltip"]').tooltip();   
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
	   
	$('[data-toggle="tooltip"]').tooltip();	
	  var plant= '<%=PLANT%>';  
	  
	  
		
});

var sts =   [{
    "year": "Open",
    "value": "Open",
    "tokens": [
      "Open"
    ]
  },
	  	{
    "year": "Receive",
    "value": "Receive",
    "tokens": [
      "Receive"
    ]
  },
	  	{
    "year": "Banking",
    "value": "Banking",
    "tokens": [
      "Banking"
    ]
	  }];
		
var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    matches = [];
	    substrRegex = new RegExp(q, 'i');
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

function removeterminaldropdown(){
	$("#TERMINALNAME").typeahead('destroy');
}

function setType(value){
	document.form.srctype.value= value;
	}

function setTerminalData(TERMINAL,TERMINAL_NAME){
	$("input[name=TERMINALCODE]").val(TERMINAL);
}

function PrintTable() {
    var printWindow = window.open('', '', '');
    printWindow.document.write('<html><head><title>Print <%=title %></title>');

    //Print the Table CSS.
    var table_style = document.getElementById("table_style").innerHTML;
    printWindow.document.write('<style type = "text/css">');
    printWindow.document.write(table_style);
    printWindow.document.write('</style>');
    printWindow.document.write('</head>');

    //Print the DIV contents i.e. the HTML Table.
    printWindow.document.write('<body>');
    var divContents = document.getElementById("dvContents").innerHTML;
    printWindow.document.write(divContents);
    printWindow.document.write('</body>');

    printWindow.document.write('</html>');
    printWindow.document.close();
    printWindow.print();
}





function ExportTableToExcel() {
	  var workbook = new ExcelJS.Workbook();
	  var worksheet = workbook.addWorksheet('Sheet 1');

	  var table = document.getElementById('table');
	  var DEDUCT_TAB = document.form.DEDUCT_TAB.value;
	  if(DEDUCT_TAB=='0')
	   table = document.getElementById('tablePrint');

	  var captionText = table.querySelector('caption').innerText;

	  // Set the caption as a header across all columns
	  var numColumns = table.rows[0].cells.length;
	
	  // Merge cells based on the width of the table
	  var mergeRange = 'A1:' + String.fromCharCode(64 + numColumns) + '1';
	  worksheet.mergeCells(mergeRange);	  
	  var captionCell = worksheet.getCell('A1');
	  captionCell.value = captionText;
	  captionCell.alignment = { horizontal: 'center' };
	  captionCell.font = { bold: true, size: 10 };
   

	  for (var i = 0; i < table.rows.length; i++) {
		    var row = table.rows[i];
		    var excelRow = worksheet.addRow();
		   // var excelRow = worksheet.getRow(i + 1);

		    for (var j = 0; j < row.cells.length-1; j++) {
		      var cell = row.cells[j];
		      
		      if (i === table.rows.length) {
			      //console.log(j);
			      //console.log(cell);
		    	  var excelCell = excelRow.getCell(j + 4);
			      }

		      else {
		    	  var excelCell = excelRow.getCell(j + 1);
		    	  }
		      excelCell.value = cell.innerText;

		      if (i === 0) {
		          excelCell.font = { bold: true };
		    }
		      if(DEDUCT_TAB=='1'){
	    	  if (i === table.rows.length-1 && j===2) {
	    		  excelCell.font = { bold: true };
		    	  }
		      }
			    

		    }
		  }
	  worksheet.columns.forEach((column) => {
		    column.width = 15; 
		  });

	  workbook.xlsx.writeBuffer().then(function (buffer) {
	    var blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
	    saveAs(blob, 'POS Revenue Report.xlsx');
	  });
	}

	function processrecive(outlet,terminal,month,year){
		console.log(outlet);
		console.log(terminal);
		console.log(month);
		console.log(year);
		window.location.href = "/track/posreports/revenuereportsmonth?OUTLET="+outlet+"&TERMINAL="+terminal+"&SMONTH="+month+"&SYEAR="+year;
	}



</script>
<!-- <style id="table_style" type="text/css">
.printtable
{
border: 1px solid #ccc;
border-collapse: collapse;
}
.printtable th, .printtable td
{
padding: 5px;
border: 1px solid #ccc;
}
.printtable tr:last-child {
font-weight: bold;
}
.printtable td:nth-last-child(3),
td:nth-last-child(2),
td:nth-last-child(1) {
text-align: right;
}
</style> -->

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>