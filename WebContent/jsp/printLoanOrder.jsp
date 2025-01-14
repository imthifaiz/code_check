<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.util.DateUtils"%>
<%@ page import="net.sf.jasperreports.engine.*"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrintManager"%>
<%@ page import="javax.print.PrintService"%>
<%@ page import="javax.print.PrintServiceLookup"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporter"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter"%>
<%@ page import="java.awt.print.PrinterJob"%>

<%@ page
   import=" net.sf.jasperreports.engine.*,
           net.sf.jasperreports.engine.design.JasperDesign,
            net.sf.jasperreports.engine.design.JRDesignQuery,
           net.sf.jasperreports.engine.xml.JRXmlLoader,
           net.sf.jasperreports.engine.export.*"
%>





<%@ include file="header.jsp"%>

<%
String title = "Rental Order Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.RENTAL_CONSIGNMENT%>"/>
<jsp:param name="submenu" value="<%=IConstants.RENTAL_CONSIGNMENT_REPORTS%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


    <script language="javascript">


var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }

function onView(){
      
     var flag    = "false";
     var ORDERNO    = document.form1.ORDERNO.value;
     if(ORDERNO != null    && ORDERNO != "") { flag = true;}
     if(flag == "false"){ alert("Please Enter Order No"); return false;}
     document.form1.cmd.value="View" ;
     document.form1.action="printLoanOrder.jsp";
     document.form1.submit();
}

function onRePrint(){

    var flag    = "false";
    var ORDERNO    = document.form1.ORDERNO.value;
    if(ORDERNO != null    && ORDERNO != "") { flag = true;}
    if(flag == "false"){ alert("Please Enter Order No"); return false;}
	form1.setAttribute("target", "_blank");
    document.form1.action="/track/DynamicFileServlet?action=printLOANORDWITHBATCH&ORDERNO="+ORDERNO;
    document.form1.submit();
}

function onRePrintWithOutBatch(){

    var flag    = "false";
    var ORDERNO    = document.form1.ORDERNO.value;
    if(ORDERNO != null    && ORDERNO != "") { flag = true;}
    if(flag == "false"){ alert("Please Enter Order No"); return false;}
	form1.setAttribute("target", "_blank");
    document.form1.action="/track/DynamicFileServlet?action=printLOANORDWITHOUTBATCH&ORDERNO="+ORDERNO;
    document.form1.submit();
}



</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
  
  <% 
        StrUtils strUtils     = new StrUtils();
        ArrayList QryList  = new ArrayList();
        LoanUtil loUtil = new LoanUtil();
        loUtil.setmLogger(mLogger);
        Map defMap = new HashMap();
        String PLANT        = (String)session.getAttribute("PLANT");
        String USER         = (String)session.getAttribute("LOGIN_USER");
     
        String cmd         = strUtils.fString(request.getParameter("cmd")).trim();
     
        String TRANDT="",ORDERNO="",result="",  chkString= "",ORDERTYPE="";
        ORDERTYPE    = strUtils.fString(request.getParameter("ORDERTYPE"));
        ORDERNO    = strUtils.fString(request.getParameter("ORDERNO"));
      double pickQty = 0;String btnDisabled="disabled";
    
       
     if(cmd.equalsIgnoreCase("View")){
        try{
        
        
       QryList=  loUtil.listLoanDetilstoPrint(PLANT,ORDERNO);
        System.out.println(" QryList.size() : "+QryList.size());
        
        }catch(Exception e){
        System.out.println("Exception :: View : "+e.toString());
       result =  "<tr><td><font class=mainred><B><h4><centre>" + e.getMessage() + "!</centre></b></font><h4></td></tr>";
        }
        }      
  

%>
    <div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
    <FORM class="form-horizontal" name="form1" method="post" action="printLoanOrder.jsp">
    <INPUT type="hidden" name="cmd" value=""/>
    
 <div class="form-group">
       <label class="control-label col-sm-4" for="Loan Order No">Order Number:</label>
       <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="20">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/loan_order_list.jsp?ORDERNO='+form1.ORDERNO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Rental Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
   <div class="form-inline">
   <div class="col-sm-1">
   <button type="button" class="Submit btn btn-default"  onClick="javascript:return onView();"><b>View</b></button>
   </div>
   </div>
   <INPUT class="form-control" name="ORDERTYPE" type = "hidden" value="<%=ORDERTYPE%>" size="20">
   
    </div>
   
  
    <div id="VIEW_RESULT_HERE">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12" >
              	<table id="tableIssueSummary"
									class="table table-bordered table-hover dataTable no-footer "
									role="grid" aria-describedby="tableIssueSummary_info">
					<thead>
		                <tr role="row">
  <th style="background-color: #eaeafa; color:#333; text-align: left;">Line No</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Product ID</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Product Description</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Order Quantity</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Issue Quantity</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">UOM</th>
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

          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
            Map lineArr = (Map) QryList.get(iCnt);
            int iIndex = iCnt + 1;
            String pickedQty = (String)lineArr.get("QTYIS");
            pickQty = pickQty + Double.parseDouble(pickedQty);
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
             %>
             
             
             var rowData = [];
             rowData[rowData.length] = '<%=(String)lineArr.get("ORDLNNO")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("ITEM")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("ITEMDESC")%>';
             rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYOR"))%>';
             rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYIS"))%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("UNITMO")%>';
             tableData[tableData.length] = rowData ; 
             <%
  		}
  	%> 
  	<%  if (pickQty>0){btnDisabled="";}%>
  	$(document).ready(function(){
     	 $('#tableIssueSummary').DataTable({
     		
     		 "lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
     		  	data: tableData,
     		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
     			"<'row'<'col-md-6'><'col-md-6'>>" +
     			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
     	         buttons: [
     	        	
     	        ] , 
     		  });	 
     });
            
  	$('#tableIssueSummary').on('column-visibility.dt', function(e, settings, column, state ){
  		if (!state){
  			groupRowColSpan = parseInt(groupRowColSpan) - 1;
  		}else{
  			groupRowColSpan = parseInt(groupRowColSpan) + 1;
  		}
  		$('#tableInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
  		$('#tableInventorySummary').attr('width', '100%');
  	});    
            
     
     </script>
            		 
 
     <b>
       <%= result%>
      </b>
          
          
    <div class="form-group">
  	<div class="col-sm-12" align="center">   
  	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="javascript:return onRePrint();" <%=btnDisabled%>><b>PrintWithBatch/Sno</b></button>
  	<button type="button" class="Submit btn btn-default"  value="PrintWithOutBatch/Sno"  name="action" onclick="javascript:return onRePrintWithOutBatch();" <%=btnDisabled%>><b>PrintWithOutBatch/Sno</b></button>
  	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RL');}"> <b>Back</b></button>&nbsp;&nbsp; -->
  	</div>
  	</div> 
  	
  </FORM>
  </div>
  </div>
  </div>
          
 <script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>
   
  <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

