<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.util.StrUtils"%>
<%@ page import="com.track.dao.*"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Chart of Accounts";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	boolean displaySummaryNew=false,displaySummaryEdit=false,displayPrintPdf=false,displayDelete=false;
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryNew = ub.isCheckValAcc("newchartOfAccounts", plant,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editchartOfAccounts", plant,USERID);
	displayPrintPdf = ub.isCheckValAcc("printchartOfAccounts", plant,USERID);
	displayDelete = ub.isCheckValAcc("deletechartOfAccounts", plant,USERID);
	}
%>
<%@include file="sessionCheck.jsp"%>

<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="submenu" value="<%=IConstants.CHART_OF_ACCOUNTS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<link href="../jsp/css/tabulator_simple.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<div class="container-fluid m-t-20">

	<div class="box">
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                   
                <li><label>Chart of Accounts</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">

			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<%if(displaySummaryNew){ %>
			<button type="button" class="btn btn-default pull-right" data-toggle="modal"
				data-target="#create_account_modal">New
				Account</button>
				<%}%>

		</div>

		<div class="row">
		<div class="col-xs-12" >
		<%if(displayPrintPdf){ %>
		<a class="btn btn-default pull-right" href="../banking/chartofaccdetails?TRANID="><i class="fa fa-print"></i></a>
		<%}%>
		<%if(displaySummaryEdit){ %>
		<a class="btn btn-default pull-right" href="../banking/chartofaccedit?TRANID="><i class="fa fa-pencil-square-o"></i></a>
		<%}%>		
		</div>
		</div>
		<div class="box-body">
		<input type="number" id="numberOfDecimal" name="numberOfDecimal"
					style="display: none;" value=<%=numberOfDecimal%>>
			<div id="coa-table"></div>

			<!-- <div class="row" style="margin: 0px;">
				<table id="table"
					class="table table-bordered table-hover dataTable no-footer "
					role="grid" aria-describedby="tableInventorySummary_info">
					<thead style="background: #eaeafa; text-align: center">
						<tr>
							<th>Account Name</th>
							<th>Account Type</th>
							<th>Extented Type</th>
							<th>Balance</th>
							<th>Edit</th>
							<th>Print</th>							
						</tr>
					</thead>
				</table>
			</div> -->
			
			

		</div>


	</div>
	<%@include file="CoaNewAccountModal.jsp"%>
	<%@include file="CoaEditAccountModal.jsp"%>
</div>
<style>
.searchAccFilter
{
	background-color: #d9534f;
    border-color: #d43f3a;
    padding: 9px;
    margin-top: 20px;
    margin-left: 12px;
    color: white;
}
</style>
<script type="text/javascript">
var tableAccountSummary;
var tableDataNested;
	$(document).ready(function() {
		loadTable();
		loadCOAData();
		
					});
	
	function loadCOAData()
	{
		$.ajax({
			type : "POST",
			url: '/track/ChartOfAccountServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getSubAccountTypeSummary",
			},
			success : function(datasubitem) {
				var subAccountListFilter=datasubitem.results;
				var myJSON = JSON.stringify(subAccountListFilter);
				//alert(myJSON);
				tableDataNested=subAccountListFilter;
				loadCOA();
			}
		});
	}
	var openIcon = function(value, data, cell, row, options){
		
		var icop="";
		<%if(displaySummaryEdit){ %>
		icop="<i class='fa fa-edit'></i>";
		<% } else { %>
		icop="<i style='pointer-events: none; cursor: default; color: darkgrey;' class='fa fa-edit'></i>";
		<% } %>
		//plain text value
	    return icop;
	};
	var deleteIcon = function(value, data, cell, row, options){ //plain text value
	    
	    var dcop="";
		<%if(displayDelete){ %>
		dcop="<i class='fa fa-trash'></i>";
		<% } else { %>
		dcop="<i style='pointer-events: none; cursor: default; color: darkgrey;' class='fa fa-trash'></i>";
		<% } %>
		//plain text value
	    return dcop;
	};
	var menuTitleFormatter = function(cell, formatterParams, onRendered){
        var searchNode='<span class="glyphicon glyphicon-search searchAccFilter" aria-hidden="true"></span>';
	    return searchNode;
	};
	function loadCOA()
	{
		var table = new Tabulator("#coa-table", {
		    height:"711px",
		    data:tableDataNested,
		    dataTree:true,
		    dataTreeFilter:false,
		    dataTreeBranchElement:false,
		    layout:"fitColumns",
/* 		    groupBy:"type", */
		    dataTreeStartExpanded:true,
		    columns:[
		   /*  {title:"ID", field:"id",width:50,responsive:0}, */
		    {title:"Account Code", field:"accountcode",headerFilter:"input",headerFilterPlaceholder:"Account Code",responsive:0},
		    {title:"Account Name", field:"name", responsive:0,headerFilter:"input",headerFilterPlaceholder:"Account Name"}, //never hide this column
		    {title:"Account Group", field:"type",headerFilter:"input",headerFilterPlaceholder:"Account Group"},
		    {title:"Account Type", field:"detailtype", responsive:2,headerFilter:"input",headerFilterPlaceholder:"Account Type"}, //hide this column first
		    <%if(displaySummaryEdit){ %>
		    {title:"" ,titleFormatter:menuTitleFormatter,headerSort:false, resizable:false,sortable:false,width:60, formatter:openIcon, cellClick:function(e, cell){
		        //e - the click event object
		        //cell - cell component
		        var itemId=cell.getRow().getData().id;
		       // alert(itemId);
		        readTableRecord(itemId);
		        },
		        },
		    <%} else { %>
		    {title:"" ,titleFormatter:menuTitleFormatter,headerSort:false, resizable:false,sortable:false,width:60, formatter:openIcon },
		    <%} %>
		    
		    <%if(displayDelete){ %>
	        {title:"" , sortable:false,width:50,headerSort:false, resizable:false, formatter:deleteIcon, cellClick:function(e, cell){
		        //e - the click event object
		        //cell - cell component
		        var itemId=cell.getRow().getData().id;
		       // alert(itemId);
		        //readTableRecord(itemId);
		        deleteItem(itemId);
		        },},
		        <%} else { %>
		        {title:"" , sortable:false,width:50,headerSort:false, resizable:false, formatter:deleteIcon},
		        <%} %>
		    ],
		    initialSort:[
	            {column:"accountcode", dir:"asc"}, //sort by this first
	        ],
		});
	}
	
	
	
	function loadTable(){
		if (tableAccountSummary){
			tableAccountSummary.DataTable().ajax.reload();
    	}
		else{
			tableAccountSummary  = $('#table').dataTable({
			"processing" : true,
			"lengthMenu" : [
					[ 10, 25, 50, 100, -1 ],
					[ 10, 25, 50, 100, "All" ] ],
			"ajax" : {
				'type': 'post',
		        'url': "/track/ChartOfAccountServlet?action=read",
				"contentType" : "application/x-www-form-urlencoded; charset=utf-8",
				"dataType" : "json",
				"dataSrc" : function(data) {
					console.log('here' +data)
					if (typeof data.data.length == 0) {
						console.log('here')
						return [];
					} else {
						console.log('here else')
						for(var dataIndex = 0; dataIndex < data.data.length; dataIndex ++){
							if(data.data[dataIndex]['issubaccount'] == "1")
								data.data[dataIndex]['account_name'] ='<div style="margin-left: 10px">'+data.data[dataIndex]['account_name']+'</div>';
							else
								data.data[dataIndex]['account_name'] ='<div style="margin-left: 0px">'+data.data[dataIndex]['account_name']+'</div>';
							data.data[dataIndex]['action'] = '<button class="btn btn-sm" id='+data.data[dataIndex]['id']+' onclick="readTableRecord(this)"><i class="fa fa-pencil-square-o"></i></button>';
							//if(data.data[dataIndex]['account_balance'] >0)
								data.data[dataIndex]['print'] = '<a href="../banking/chartofaccdetails?TRANID=' +data.data[dataIndex]['id'] + '"><i class="fa fa-print"></i></a>';
							//else
								//data.data[dataIndex]['print'] = '<a href=""><i class="fa fa-print"></i></a>';
						}
						return data.data;
					}
				}
			},
			"columns" : [ {
				"data" : 'account_name',
				"orderable" : true
			}, {
				"data" : 'account_type',
				"orderable" : true
			}, {
				"data" : 'account_det_type',
				"orderable" : true
			}, /* {
				"data" : 'account_balance',
				"orderable" : true
			}, */{
				"data" : 'action',
				"orderable" : false
			},{
				"data" : 'print',
				"orderable" : false
			}],
			/* "columnDefs" : [ {
				"className" : "t-right",
				"targets" : [ 3 ]
			} ], */
			/*"dom": 'lBfrtip',*/
			"dom" : "<'row'<'col-md-6'l><'col-md-6'Bf>>"
					+ "<'row'<'col-md-6'><'col-md-6'>>"
					+ "<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
			buttons : [
					{
						extend : 'collection',
						text : 'Export',
						buttons : [{
									extend : 'excel',
									exportOptions : {
										columns : [ ':visible' ]
									}
									},
									{
									extend : 'pdf',
									exportOptions : {
										columns : [ ':visible' ]
									},
									orientation : 'landscape',
									pageSize : 'A4'
									} ]
		}]
		});
		}
		$('[data-toggle="tooltip"]').tooltip();		
	}
	function readTableRecord(itemid){
		//console.log($(ele).attr('id'));
		//alert("ReadTable"+itemid);
		$.ajax({
			type: 'post',
	        url: "/track/ChartOfAccountServlet?action=read_record",
	       	dataType:'json',
	   		data:  {
	   			"id":itemid
	   		},
	      
	        success: function (data) {
				console.log("Objects:"+data.results);
				//alert(data.results);
				var numberOfDecimal = $("input[name ='numberOfDecimal']").val();
				$('#edit_account_modal').modal('toggle');
				$('#edit_account_modal').modal('show');
				//selectAccType(data.results.account_id);
				selectEditAcctType(data.results.account_type);
	         	$("#edit_account_modal #acc_id").val(data.results.account_id);
	         	$("#edit_account_modal #eacc_name").val(data.results.account_name);
	         	$("#edit_account_modal #eacc_name").prop('readonly', true);
	         	//$("#edit_account_modal #acc_typeH").val(data.results.account_type);
	         	$("#edit_account_modal #eacc_det_type_id").val(data.results.account_det_id);
	         	$("#edit_account_modal #eacc_det_type").val(data.results.account_det_type);
	         	$("#edit_account_modal #eacc_desc").val(data.results.account_desc);
	         	$("#edit_account_modal #eacc_code_show").val(data.results.account_code);
	         	$("#edit_account_modal #eacc_is_sub").val(data.results.issub_account);
	         	//$("#edit_account_modal #eacc_is_exp_gst").val(data.results.isexp_gst);

	         	if(data.results.isexp_gst=="1"){
	         		checkExpGst();
	         	}else{
	         		uncheckExpGst();
	         	}
	         	
	         	if(data.results.issub_account=="1")
	         		{
	         		$('#edit_account_modal #eacc_subAcct').attr('disabled',false);
	         		checkSubAcc();
	         		$('#edit_account_modal #eacc_subAcct').css("background-color", "transparent");
	         		selectEditSubAcc(data.results.sub_account);
	         		
	         		//$("#edit_account_modal #eacc_subAcct").val(data.results.sub_account);
	         		}
	         	else
	         	{
	         		selectEditSubAcc(null);
	         		$('#edit_account_modal #eacc_subAcct').attr('disabled',true);
	         		  if ($('#edit_account_modal #eacc_is_sub').is(':checked')) {
	         			 	uncheckSubAcc();
	         		  }
	         		$('#edit_account_modal #eacc_subAcct').css("background-color", "#EEEEEE");
	         		}
	         	$("#edit_account_modal #eacc_balance").val(data.results.balance);
	         	$("#edit_account_modal #eacc_balanceDate").val(data.results.balance_date);
	         	$("#edit_account_modal #ecoamainid").val(data.results.mainaccid);
	         	if(data.results.mainaccid=="5")
	         		{
	         			$("#editlandedblock").show();
		         		if(data.results.landedcostcal=="0")
		         		{
		         			$("#editnonewise").prop("checked", true);
		         		}
		         	else if(data.results.landedcostcal=="1")
		         		{
		         			$("#editcostwise").prop("checked", true);
		         		}
		         	else if(data.results.landedcostcal=="2")
		         		{
		         			$("#editweightwise").prop("checked", true);
		         		}
		         		$(".opbal").hide();
		         		$(".opbalshow").hide();
	         		}else if(data.results.mainaccid=="1" || data.results.mainaccid=="3")
	         		{
	         			$("#editlandedblock").hide();
	         			$("label[for='eballable']").text('Balance');
	         			$("label[for='ebalanceDate']").text('Balance Date');
	         			if(parseFloat(data.results.account_balance) == 0){
	         				$("#edit_account_modal #eacc_balance").val(parseFloat("0").toFixed(numberOfDecimal));
	         				$(".opbal").show();
	         				$(".opbalshow").hide();
	         			}else{
	         				$("#edit_account_modal #eacc_balance_show").val(parseFloat(Math.abs(parseFloat(data.results.account_balance))).toFixed(numberOfDecimal));
	         				$(".opbal").hide();
	         				$(".opbalshow").show();
	         			}
	         			
	         		}else if(data.results.mainaccid=="2")
	         		{
	         			$("#editlandedblock").hide();
	         			$("label[for='eballable']").text('Unpaid Balance');
	         			$("label[for='ebalanceDate']").text('Unpaid Balance Date');
	         			if(parseFloat(data.results.account_balance) == 0){
	         				$("#edit_account_modal #eacc_balance").val(parseFloat("0").toFixed(numberOfDecimal));
	         				$(".opbal").show();
	         				$(".opbalshow").hide();
	         			}else{
	         				$("#edit_account_modal #eacc_balance_show").val(parseFloat(Math.abs(parseFloat(data.results.account_balance))).toFixed(numberOfDecimal));
	         				$(".opbal").hide();
	         				$(".opbalshow").show();
	         			}
	         			
	         		}
	         	else
	         		{
	         			$("#editlandedblock").hide();
	         			$(".opbal").hide();
		         		$(".opbalshow").hide();
	         		}
	         	
	         	
	        },
	        error: function (data) {	        	
	            alert(data.responseText);
	        }
		})
	}
	
	function deleteItem(itemid)
	{
		var r = confirm("Are you sure about deleting this account?");
		  if (r == true) {
		  
				$.ajax({
					type: 'post',
			        url: "/track/ChartOfAccountServlet?action=deleteAccount",
			       	dataType:'json',
			   		data:  {
			   			"accid":itemid
			   		},
			        success: function (data) {
			        	if(data.status=="ERROR")
			        		{
			        			alert("Error!");
			        		}
			        	else
			        		{
			        			//alert("Success");
			        			loadCOAData();
			        		}
			        	
					}
				});
		  }
		
	}
	function successAccountCallback(data){
		if(data.STATUS="SUCCESS"){
			//alert(data.MESSAGE);
			loadTable();
		}
	}

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>