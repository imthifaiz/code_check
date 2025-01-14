<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.IDBConstants" %>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Purchase Order Summary Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<script type="text/javascript">
function ExportReport()
{
   document.form1.action = "/track/ReportServlet?action=ExportReceivedOrderDetails";
   document.form1.submit();  
}
</script>

<script type="text/javascript"
	src="js/calendar.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onClick="window.location.href='../purchaseorder/recivedordershistory'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
<div class="box-body">
<form name="form1" method="post">
<div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableReceiveSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer ">
              <div class="row"><div class="col-sm-12">
              	<table id="tableReceiveSummary"
									class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tableReceiveSummary_info">
					<thead>
		                <tr role="row">
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Order No</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Line No</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Description</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Batch</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Loc</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Unit Cost</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">UOM</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Order Qty</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Recv Qty</TH>
                     </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
<script type="text/javascript">
  var tableData = [];
	
 <%
StrUtils _strUtils = new StrUtils();
RecvDetDAO  _RecvDetDAO   = new RecvDetDAO ();
_RecvDetDAO.setmLogger(mLogger);
ArrayList movQryList =  null;

session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String PONO="",PGaction="",fieldDesc="",DATE="",TODATE="";
	PONO = _strUtils.fString(request.getParameter("PONO"));
	DATE = _strUtils.fString(request.getParameter("DATE"));
	//TODATE = _strUtils.fString(request.getParameter("TODATE"));
	
	 double ordqtytotal=0,recqtytotal=0;
	int k=0;
		try{
		PlantMstDAO plantMstDAO = new PlantMstDAO();
        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		movQryList =  _RecvDetDAO.getReceivedInboundOrderDetails(plant,PONO,DATE,TODATE);
	
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd"; 
			
			
			//Float ordqty =  Float.parseFloat(((String) lineArr.get("ordqty").toString())) ;
			//Float recqty =  Float.parseFloat(((String) lineArr.get("recqty").toString())) ;
			String unitCost = _strUtils.fString((String) lineArr.get("unitcost"));
			double unitCostVal ="".equals(unitCost) ? 0.0d :  Double.parseDouble(unitCost);				                
			unitCost = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
			
	%>	
	 var rowData = [];
     rowData[rowData.length] = '<%=(String) lineArr.get("pono")%>';
     rowData[rowData.length] = '<%=(String)lineArr.get("lnno")%>';
     rowData[rowData.length] = '<%=(String)lineArr.get("item")%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("itemdesc")%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("batch")%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("loc")%>';
     rowData[rowData.length] = '<%=unitCost%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("uom")%>';
     rowData[rowData.length] = '<%=_strUtils.formatNum((String)lineArr.get("ordqty"))%>';
     rowData[rowData.length] = '<%=_strUtils.formatNum((String)lineArr.get("recqty"))%>';
     tableData[tableData.length] = rowData;
     <%}%>    
     
     $(document).ready(function(){
    	 $('#tableReceiveSummary').DataTable({
    		 	"lengthMenu": [[5, 10, 15, 20, -1], [5, 10, 15, 20, "All"]],
    		  	data: tableData,
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
    	                    },
    	                    {
    	                    	extend : 'pdf',
    	                    	exportOptions: {
    	                    		columns: [':visible']
    	                    	},
                       		orientation: 'landscape',
                               pageSize: 'A3'
    	                    }
    	                ]
    	            },
    	            {
                       extend: 'colvis'
                   }
    	        ], "drawCallback": function ( settings ) {
    				var groupColumn = 0;
    				var groupRowColSpan= 8;
    			   	var api = this.api();
    	            var rows = api.rows( {page:'current'} ).nodes();
    	            var last=null;
    	            var totalReceiveQty = 0;
    	            var groupTotalReceiveQty = 0;
    	            
    	            var groupEnd = 0;
    	            var currentRow = 0;
    	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
    	                if ( last !== group ) {
    	                	if (i > 0) {
    	                		$(rows).eq( i ).before(
    			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalReceiveQty).toFixed(3) + '</td></tr>'
    			                    );
    	                	}
    	                    last = group;
    	                    groupEnd = i;    
    	                    groupTotalReceiveQty = 0;
    	                   
    	                    	                    
    	                }
    	                groupTotalReceiveQty += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', '').replace('$', ''));
    	                totalReceiveQty += parseFloat($(rows).eq( i ).find('td:last').html().replace(',', '').replace('$', ''));
    	                  	                
    	                currentRow = i;
    	            } );
    	            if (groupEnd > 0 || rows.length == 1){
    	            	$(rows).eq( currentRow ).after(
    	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + parseFloat(totalReceiveQty).toFixed(3) + '</td></tr>'
                       );
                   	$(rows).eq( currentRow ).after(
    	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(groupTotalReceiveQty).toFixed(3) + '</td></tr>'
                       );
                   }
    	        } 
    		  });	 
    });
     <%
		
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
	
%>
     
	<%-- <TR bgcolor="<%=bgcolor%>">
		<TD width="10%"><%=(String) lineArr.get("pono")%></TD>
		<TD align= "center" width="5%"><%=(String)lineArr.get("lnno")%></TD>
		<TD align= "center" width="12%"><%=(String)lineArr.get("item")%></TD>
		<TD align="center" width="15%"><%=(String) lineArr.get("itemdesc")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("batch")%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("loc")%></TD>
		<TD align="right" width="6%"><%=_strUtils.currencyWtoutSymbol((String) lineArr.get("unitcost"))%></TD>
		<TD align="center" width="10%"><%=(String) lineArr.get("uom")%></TD>
		<TD align="right" width="6%"><%=_strUtils.formatNum((String)lineArr.get("ordqty"))%></TD>
		<TD align="right" width="6%"><%=_strUtils.formatNum((String)lineArr.get("recqty"))%></TD>
		
		
	</TR>
 
<%

		//ordqtytotal   = ordqtytotal+ordqty ;
		recqtytotal   = recqtytotal+Double.parseDouble((String)lineArr.get("recqty"));
		
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
	if (movQryList.size() >0)
	{ 
		k=k+1;
		String bgItemcolor = ((k == 0) || (k % 2 == 0)) ? "#FFFFFF"
			: "#dddddd";
%>
	<TR BGCOLOR="<%=bgItemcolor %>">
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align= "right" ></TD>
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align="center"></TD>
	<TD align="right"></TD>
	<TD align= "right" ><b>Total:</b></TD>
	<TD align="right"><b><%=_strUtils.formatNum((new Double(recqtytotal).toString()))%></b></TD>
	

	</TR>
<% 
	}
	//ordqtytotal=0;
	recqtytotal=0;
	if(k==0)
	k=1;


%>

</TABLE> --%>

</script>
<br>
  <INPUT type = "hidden" name="PONO" value = "<%=PONO%>">
 <INPUT type = "hidden" name="DATE" value = "<%=DATE%>">
 <INPUT type = "hidden" name="TODATE" value = "<%=TODATE%>">

<table align="center">
	<TR>
<!-- 		<td><button class="form-control" type="button" onClick="window.location.href='../purchasereports/recivedordershistory'"><b>Back</b></button></td> -->
		<td><input type="hidden" value="ExportReport"
			onClick="javascript:ExportReport();"></td>
	</TR>
</table>
</form>
</div></div></div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
