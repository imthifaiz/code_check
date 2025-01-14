<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%
String fieldDesc=(String)request.getAttribute("result");
String fieldDesc1=(String)request.getAttribute("resultok");
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="Parent Child Company Relation" />
	<jsp:param name="mainmenu" value=""/>
	<jsp:param name="submenu" value="" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<style>
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
.chdcmp-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -10%;
    top: 15px;
}
</style>
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
	<h2><small class="success-msg"><%=fieldDesc1%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box"> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">Create Parent Child Company Relation</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="" class="form-vertical" name="form" action="../Parentchildcmp/save"  method="post" onsubmit="return validateEmployeetype()">
				<div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<div class="col-sm-9">
						<!-- <div class="form-group" style="margin-bottom: 10%;">
							<label class="control-label col-form-label col-sm-4">Parent Company</label>
							<div class="col-sm-8 ac-box">
								<input type="text" class="ac-selected form-control typeahead"
									id="parent_company" name="parent_company"
									placeholder="Select a parent company"> <span
									class="select-icon"
									onclick="$(this).parent().find('input[name=\'parent_company\']').focus()"><i
									class="glyphicon glyphicon-menu-down"></i></span>
							</div>
						</div> -->

						<div class="form-group">
							<!-- <table class="table table-bordered line-item-table childcmp-table"> -->
							<table class="table childcmp-table">
								<thead>
									<tr>
										<th style="font: inherit;vertical-align: top;">Parent Company</th>
										<th class="text-center" style="font: inherit;"><input type="text"
											class="ac-selected form-control typeahead"
											id="parent_company" name="parent_company"
											placeholder="Select Parent Company"></th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Child Company</td>
										<td class="text-center"><input
											class="form-control text-left childCompany"
											name="childCompany" type="text"
											placeholder="Select Child Comany" maxlength="100"></td>
											
											<td class="text-center"> <!-- imti added -->
											<input name="ischildCompany" type="checkbox" onclick="isChecked(this)"><label>&nbsp;&nbsp; is child as parent?</label>  
											<input name="ischild" type="hidden" value="0">
											</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRow()">+ Add another child company</a>
						</div>
					</div>
				</div>
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
						<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success" id="sord">Save</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<h1 style="font-size: 20px;padding-left: 2%;" class="box-title">Parent Child Company Relation Summary</h1>
<div class="container-fluid">
				<input type="text" name="LOGIN_USER" value="<%=username%>" hidden>
				<div id="VIEW_RESULT_HERE" class="table-responsive">
					<table id="tableparentchild"
						class="table table-bordred table-striped">
						<thead>
							<tr>
								<th style="font-size: smaller;">PARENT COMPANY</th>
								<th style="font-size: smaller;">CHILD COMPANIES</th>
							</tr>
						</thead>
						<tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
				</div>
		</div>
				<script LANGUAGE="JavaScript">
		var tableparentchild;
		function getParameters(){
			return {
				"CMD":"GET_PARENT_CHILD_MST"
			}
		}
		function onGo(){
			   var urlStr = "../Parentchildcmp/GET_PARENT_CHILD_MST";
			   var groupColumn = 1;	
			    if (tableparentchild){
			    	tableparentchild.ajax.url( urlStr ).load();
			    }else{
			    	tableparentchild = $('#tableparentchild').DataTable({
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
					        	if(data.PCLIST.length>0){
					        		return data.PCLIST;
					        	}else{
					        		return [];
					        	}
					        }
					    },
				        "columns": [
				        	{"data": 'PARENT_PLANT', "orderable": true},
			    			{"data": 'CHILD_PLANT', "orderable": false},
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
					                        columns: [0,1]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	/* exportOptions: {
				                    		columns: [':visible']
				                    	}, */
				                    	exportOptions: {
				                            columns: [0,1]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	/* exportOptions: {
			    	                    		columns: [':visible']
			    	                    	}, */
			    	                    	exportOptions: {
					                            columns: [0,1]
					                        },
			    	                        title: function () { var dataview = "Parent Child Company List"  ; return dataview },    	                        
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
				        	<%-- <%if(!displaySummaryExport){ %>
				        	$('.buttons-collection')[0].style.display = 'none';
				        	<% } %> --%>
				        	}

					});
			    }
			    
			}

		</script>
<script type="text/javascript">

$(document).ready(function(){
	onGo();
	$('#parent_company').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'PLANT',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../Parentchildcmp/GET_PARENT_PLANT_DROPDOWN";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					CMD : "GET_PARENT_PLANT_DROPDOWN",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					//alert(JSON.stringify(data));
					return asyncProcess(data.PLANTMST);
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
		    		return '<div onclick="setparent(this,\''+data.PLANT+'\')"><p class="item-suggestion">CODE :' + data.PLANT + '</p><br><p class="item-suggestion">NAME :' + data.NAME + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			//menuElement.after( '<div class="accountAddBtn footer salarytypepopup"  data-toggle="modal" data-target="#create_salary_type"><a href="#"> + New Salary Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	addparchildrowclasses();
});


function validateEmployeetype(){
	
	var parent = document.form.parent_company.value;
	
	if(parent == ""){
		alert("Please select a Parent Company.");
		document.form.parent_company.focus
		$('input[name="parent_company"]').focus();
		return false;
	}
	
	var isItemValid = true;
	
	$("input[name ='childCompany']").each(function() {
	    if($(this).val() == ""){	
	    	$(this).focus();
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Child Company field cannot be empty.");
		return false;
	}
	

	
	return true;
}

function addRow(){
	var body="";
	body += '<tr>';
	body += '<td>Child Company</td>';
	body += '<td class="text-center" style="position:relative;">';
// 	body += '<span class="glyphicon glyphicon-remove-circle chdcmp-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-left childCompany" name="childCompany" type="text" placeholder="Select Child Comany">';
	body += '</td>';
	
	
	body += '<td class="text-center" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle chdcmp-action" aria-hidden="true"></span>';
	body += '<input name="ischildCompany" type="checkbox" onclick="isChecked(this)">';
	body += '<input name="ischild" value="0" type="hidden">';
	body += '<label>&nbsp;&nbsp; is child as parent?</label>';
	body += '</td>';
// 	<td class="text-center" style="position:relative;">
// <span class="glyphicon glyphicon-remove-circle chdcmp-action" aria-hidden="true"></span>
// <input name="csd" type="checkbox" value=""><label>&nbsp;&nbsp; is child as parent?</label></td>
	body += '</tr>';
	$(".childcmp-table tbody").append(body);
	removeparchildrowclasses();
	addparchildrowclasses();
	$("#sord").html('Save');
}

$(".childcmp-table tbody").on('click','.chdcmp-action',function(){
    $(this).parent().parent().remove();
	var isItemValid = true;
	$("input[name ='childCompany']").each(function() {
		isItemValid = false;
	});
	if(isItemValid){
		$("#sord").html('Delete');
	}else{
		$("#sord").html('Save');
	}
});


function onClear()
{
  document.form.action  = "../Parentchildcmp/new";
  document.form.submit();
}

function setchild(obj,plant){
	var count = "0";
	var parent = document.form.parent_company.value;
	if(parent == plant){
		count = "2";
	}
	$("input[name ='childCompany']").each(function() {
		if($(this).val() == plant){
			count = "1";
	    }
	});
	if(count == "0"){
		$(obj).closest('tr').find("input[name ='childCompany']").val(plant);
	}else if(count == "2"){
		alert("Child company alredy selected as Parent");
		$(obj).closest('tr').remove();
		var body="";
		body += '<tr>';
		body += '<td>Child Company</td>';
		body += '<td class="text-center" style="position:relative;">';
// 		body += '<span class="glyphicon glyphicon-remove-circle chdcmp-action" aria-hidden="true"></span>';
		body += '<input class="form-control text-left childCompany" name="childCompany" type="text" placeholder="Select Child Comany">';
		body += '</td>';
		body += '<td class="text-center" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle chdcmp-action" aria-hidden="true"></span>';
		body += '<input name="ischildCompany" type="checkbox" onclick="isChecked(this)">';
		body += '<input name="ischild" type="hidden" value="0">';
		body += '<label>&nbsp;&nbsp; is child as parent?</label>';
		body += '</td>';
		body += '</tr>';
		$(".childcmp-table tbody").append(body);
		removeparchildrowclasses();
		addparchildrowclasses();
	}else{
		alert("Child company alredy selected");
		$(obj).closest('tr').remove();
	}
}

function setparent(obj,plant){
	var urlStr = "../Parentchildcmp/GET_PARENT_PLANT_DATA";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			CMD : "GET_PARENT_PLANT_DATA",
			parent : plant
		},
		dataType : "json",
		success : function(child) {
			//alert(JSON.stringify(data));
			//console.log(JSON.stringify(child));
			//console.log(child.CHILDLIST.length);
			$(".childcmp-table tbody").html("");
			if(child.CHILDLIST.length > 0){
				var body="";
				$.each(child.CHILDLIST, function( key, data ) {
					console.log(data.CHILD_PLANT);
					var child = data.ISCHILD_AS_PARENT;
					body += '<tr>';
					body += '<td>Child Company</td>';
					body += '<td class="text-center" style="position:relative;">';
// 					body += '<span class="glyphicon glyphicon-remove-circle chdcmp-action" aria-hidden="true"></span>';
					body += '<input class="form-control text-left" name="childCompany" type="text" placeholder="Select Child Comany" value="'+data.CHILD_PLANT+'" readonly>';
					body += '</td>';
					body += '<td class="text-center" style="position:relative;">';
					body += '<span class="glyphicon glyphicon-remove-circle chdcmp-action" aria-hidden="true"></span>';
					 if(child == 1) {
						body += '<input name="ischildCompany" value="ischildCompany" onclick="isChecked(this)" type="checkbox" checked >';
						body += '<input name="ischild" value="1" type="hidden" >';
					 }else{
						body += '<input name="ischildCompany" value="ischildCompany" onclick="isChecked(this)" type="checkbox">';
						body += '<input name="ischild"  value="0" type="hidden" >';
					 }
					body += '<label>&nbsp;&nbsp; is child as parent?</label>';
					body += '</td>';
					body += '</tr>';
				});
				$(".childcmp-table tbody").append(body);
			}else{
				var body="";
				body += '<tr>';
				body += '<td>Child Company</td>';
				body += '<td class="text-center">';
				body += '<input class="form-control text-left childCompany" name="childCompany" type="text" placeholder="Select Child Comany">';
				body += '</td>';
				body += '<td class="text-center">';
				body += '<input name="ischildCompany" onclick="isChecked(this)" type="checkbox"><label>&nbsp;&nbsp; is child as parent?</label>';
				body += '<input name="ischild" value="0" type="hidden">';
				body += '</td>';
				body += '</tr>';
				$(".childcmp-table tbody").append(body);
			}
			removeparchildrowclasses();
			addparchildrowclasses();
			$("#sord").html('Save');
		}
	});
}

function removeparchildrowclasses(){
	$(".childCompany").typeahead('destroy');
}

function isChecked(obj){
	 if ($(obj).is(":checked")){
		 $(obj).closest('tr').find("input[name=ischild]").val("1");
	 }else{
		 $(obj).closest('tr').find("input[name=ischild]").val("0");
	 }
}

function addparchildrowclasses(){

$('.childCompany').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{		  
	  display: 'PLANT',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "../Parentchildcmp/GET_CHILD_PLANT_DROPDOWN";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CMD : "GET_CHILD_PLANT_DROPDOWN",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				//alert(JSON.stringify(data));
				return asyncProcess(data.PLANTMST);
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
	    		return '<div onclick="setchild(this,\''+data.PLANT+'\')"><p class="item-suggestion">CODE :' + data.PLANT + '</p><br><p class="item-suggestion">NAME :' + data.NAME + '</p></div>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		//menuElement.after( '<div class="accountAddBtn footer salarytypepopup"  data-toggle="modal" data-target="#create_salary_type"><a href="#"> + New Salary Type</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();
	  
	}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		var menuElement = $(this).parent().find(".tt-menu");
		menuElement.next().show();
	}).on('typeahead:close',function(){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
		var menuElement = $(this).parent().find(".tt-menu");
		setTimeout(function(){ menuElement.next().hide();}, 180);
	});
}
</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="Create Employee Type" />
</jsp:include>