<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.util.StrUtils"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Contra Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	
	boolean displaySummaryNew=false,displaySummaryLink=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryNew = ub.isCheckValAcc("newcontra", plant,username);
		displaySummaryLink = ub.isCheckValAcc("summarylnkcontra", plant,username);
	}
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
%>
<%@include file="sessionCheck.jsp"%>

<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="submenu" value="<%=IConstants.CONTRA_ENTRY%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<div class="container-fluid m-t-20">

	<div class="box">

		<div class="box-header menu-drop">
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                         
                <li><label>Contra Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<% if (displaySummaryNew) { %>
			<a class="btn btn-default pull-right" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" href="../banking/createcontra">+ New</a>
			<% } %>
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
		</div>
		<div class="box-body">
		<div class="input-group" style="float: right;">
				<label for="fSearch" style="padding-right: 15px;font-weight: bold;">Search</label> 
				<input type="text" id="fSearch" name="fSearch">
			</div>
			<div id="journal-table"></div>
		</div>


	</div>
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
var table;
var tableJournalSummary;
var numberOfDecimal = $("#numberOfDecimal").val();
var tableDataNested;
	$(document).ready(function() {
		loadJournalData();
		const input = document.getElementById("fSearch");
		input.addEventListener("keyup", function() {
		    table.setFilter(matchAny, { value: input.value });
		    if (input.value == " ") {
		        table.clearFilter()
		    }
		});
		
					});
	
	function matchAny(data, filterParams) {
	      //data - the data for the row being filtered
	      //filterParams - params object passed to the filter
	      //RegExp - modifier "i" - case insensitve

	    var match = false;
	    const regex = RegExp(filterParams.value, 'i');

	    for (var key in data) {
	        if (regex.test(data[key]) == true) {
	            match = true;
	        }
	    }
	    return match;
	}
	
	
	function loadJournalData()
	{
		$.ajax({
			type : "POST",
			url: '/track/JournalServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getAllHeader",
				TRAN_TYPE : "CONTRA",
			},
			success : function(journallist) {
				var journalArray=journallist;
				var myJSON = JSON.stringify(journalArray);
				//alert(myJSON);
				tableDataNested=journalArray;
				loadJournal();
			}
		});
	}
	var openIcon = function(value, data, cell, row, options){ //plain text value
	    return "<i class='fa fa-edit'></i>"
	};
	var deleteIcon = function(value, data, cell, row, options){ //plain text value
	    return "<i class='fa fa-trash'></i>"
	};
	var menuTitleFormatter = function(cell, formatterParams, onRendered){
        var searchNode='<span class="glyphicon glyphicon-search searchAccFilter" aria-hidden="true"></span>';
	    return searchNode;
	};
	function loadJournal()
	{
		 	table = new Tabulator("#journal-table", {
		    height:"711px",
		    data:tableDataNested,
		    layout:"fitColumns",
/* 		    groupBy:"type", */
		    columns:[
		    {title:"DATE", field:"JOURNAL_DATE"},	
		    {title:"CONTRA#", field:"ID",formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	    	
	    		return "<a style='align:center' href='../banking/contradetail?ID="+data.ID+"'>" + data.ID+"</a>";
    }},
		    {title:"REFERENCE NUMBER", field:"REFERENCE",formatter:"textarea",variableHeight:true,formatter:function(cell, formatterParams){
		        var value = cell.getValue();
		        return "<span style='white-space: pre-wrap;word-break: break-word;'>"+value+"</span>";
		    }},
		    {title:"STATUS", field:"JOURNAL_STATUS"},
		    {title:"NOTES", field:"NOTE",tooltip:true,variableHeight:true,formatter:function(cell, formatterParams){
		        var value = cell.getValue();
		        return "<span style='white-space: pre-wrap;word-break: break-word;'>"+value+"</span>";
		    }},
		    {title:"AMOUNT", field:"TOTAL_AMOUNT",formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	    	var amount=parseFloat(data.TOTAL_AMOUNT).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	    		return amount;
    }},
		    {title:"CREATED BY", field:"CRBY"},
		    ],
		});
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
				
	         	
	        },
	        error: function (data) {	        	
	            alert(data.responseText);
	        }
		})
	}

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>