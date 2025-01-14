<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.DateUtils"%>
<%@ page import="net.sf.jasperreports.engine.*"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrintManager"%>
<%@ page import="javax.print.PrintService"%>
<%@ page import="javax.print.PrintServiceLookup"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporter"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter"%>
<%@ page import="java.awt.print.PrinterJob"%>
<%@page import="java.math.BigDecimal" %>

<%@ page
   import=" net.sf.jasperreports.engine.*,
           net.sf.jasperreports.engine.design.JasperDesign,
            net.sf.jasperreports.engine.design.JRDesignQuery,
           net.sf.jasperreports.engine.xml.JRXmlLoader,
           net.sf.jasperreports.engine.export.*"
%>





<%@ include file="header.jsp"%>

<%
String title = "Rental Order Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


    <script language="javascript">


var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
  
  
  function ExportReport()
  {
     var flag    = "false";
     var  DIRTYPE= document.form1.DIRTYPE.value;
    document.form1.FROM_DATE.value;
    document.form1.TO_DATE.value;
    document.form1.ITEM.value;
    document.form1.ORDERNO.value;
    document.form1.JOBNO.value;
    document.form1.CUST_NAME.value;
    document.form1.action="/track/ReportServlet?action=ReportRentalOrderExportExcel";
    document.form1.submit();
    
  }

function onView(){
      
     var flag    = "false";
     var ORDERNO    = document.form1.ORDERNO.value;
     FROM_DATE     = document.form1.FROM_DATE.value;
     TO_DATE   = document.form1.TO_DATE.value;
    var JOBNO   = document.form1.JOBNO.value;
     var CUST_NAME   = document.form1.CUST_NAME.value;
    var ITEM   = document.form1.ITEM.value;
    var DIRTYPE        = document.form1.DIRTYPE.value;
    
    if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
     if(ITEM != null     && ITEM != "") { flag = true;}
     if(ORDERNO != null    && ORDERNO != "") { flag = true;}
     if(FROM_DATE != null    && FROM_DATE != "") { flag = true;}
     if(TO_DATE != null    && TO_DATE != "") { flag = true;}
     if(JOBNO != null     && JOBNO != "") { flag = true;}
     if(CUST_NAME != null     && CUST_NAME != "") { flag = true;}
    /*  if(flag == "false"){ alert("Please Enter Order No"); return false;} */
     document.form1.cmd.value="View" ;
     document.form1.action="RentalOrderSummary.jsp";
     document.form1.submit();
}




</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
  
  <% 
        StrUtils strUtils     = new StrUtils();
        ArrayList QryList  = new ArrayList();
        LoanUtil loUtil = new LoanUtil();
        DateUtils _dateUtils = new DateUtils();
        Hashtable ht = new Hashtable();
        loUtil.setmLogger(mLogger);
        Map defMap = new HashMap();
        String PLANT        = (String)session.getAttribute("PLANT");
        String USER         = (String)session.getAttribute("LOGIN_USER");
     
        String cmd         = strUtils.fString(request.getParameter("cmd")).trim();
     
        String TRANDT="",ORDERNO="",result="",  chkString= "",FROM_DATE="",TO_DATE="",fdate="",tdate="",JOBNO="",ITEM="",CUST_NAME="",DIRTYPE ="";
        ORDERNO    = strUtils.fString(request.getParameter("ORDERNO"));
      double pickQty = 0;String btnDisabled="disabled";
      FROM_DATE     = strUtils.fString(request.getParameter("FROM_DATE"));
      TO_DATE   = strUtils.fString(request.getParameter("TO_DATE"));
      JOBNO         = strUtils.fString(request.getParameter("JOBNO"));
      ITEM          = strUtils.fString(request.getParameter("ITEM"));
      CUST_NAME     = strUtils.fString(request.getParameter("CUST_NAME"));
      DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE"));
      if(DIRTYPE.length()<=0){
    	  DIRTYPE = "LOANORDER";
    	  }

if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();

String curDate =_dateUtils.getDate();

if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;


if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);


if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

     if(cmd.equalsIgnoreCase("View")){
    	 
        try{
        	
        	
               QryList=  loUtil.listLoanDetilsWithPrice(ITEM,CUST_NAME,JOBNO,PLANT,ORDERNO,fdate,tdate);
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
    <FORM class="form-horizontal" name="form1" method="post" action="RentalOrderSummary.jsp">
    <INPUT type="hidden" name="cmd" value=""/>
    <div id="target" style="display:none">
    <div class="form-group">    	 
  		<label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-3">
        <div class="input-group" > 
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />         
        <INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
      	</div>
    	</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY>
      	</div>
    	</div>
    	</div>
    	</div>    
    	<INPUT type="Hidden" name="DIRTYPE" value="LOANORDER">	
    	    	<div class="form-group">
        <label class="control-label col-sm-2" for="Reference No">Reference No:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="JOBNO" type = "TEXT" value="<%=StrUtils.forHTMLTag(JOBNO)%>" size="30"  MAXLENGTH=20>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product ID">Product ID:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" style="width: 100%"  MAXLENGTH=20>
        <span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);" >
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
        <i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a></span>
   		</div>
   		</div>
 		</div> 
 		</div>
    
 <div class="form-group">
       <label class="control-label col-sm-2" for="Loan Order No">Order Number:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="30">
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('list/loan_order_list.jsp?ORDERNO='+form1.ORDERNO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Rental And Service Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		</div>
  		  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Loan Assignee Name">Customer Name:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="CUST_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUST_NAME)%>" style="width: 100%"  MAXLENGTH=20>
   		</div>
 		</div>
   </div>
   
     <div class="form-group">
   <div class="col-sm-12" align="center">
   <button type="button" class="Submit btn btn-default"  onClick="javascript:return onView();"><b>View</b></button>
   <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; 
   <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
   </div>
   </div>
   </div>
   
   <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
        <button type="button" class="Submit btn btn-default"  onClick="javascript:return onView();"><b>View</b></button>
   <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; 
   <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  	    </div>
         </div>
       	  </div>
  
    <div id="VIEW_RESULT_HERE">
  <div class="table-responsive">
  		<div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableMovementHistory"
									class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tableMovementHistory_info">
					<thead>
		                <tr role="row">
  <th style="background-color: #eaeafa; color:#333; text-align: left;">Line No</th>
  <th style="background-color: #eaeafa; color:#333; text-align: left;">Order No</th>
  <th style="background-color: #eaeafa; color:#333; text-align: left;">Ref No</th>
  <th style="background-color: #eaeafa; color:#333; text-align: left;">Customer Name</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Product ID</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Product Description</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">UOM</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Order Date</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Employee</th>
     <th style="background-color: #eaeafa; color:#333; text-align: left;">Order Quantity</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Issued Quantity</th>
    <th style="background-color: #eaeafa; color:#333; text-align: left;">Received Quantity</th>
    

               
    </tr>
	</thead>
				</table>
            		</div>
						</div>
					</div>
					</div>
					</div>
					
  <script type="text/javascript">
  
  var tableData = []; 
				
     
   <%
 
          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
            Map lineArr = (Map) QryList.get(iCnt);
            BigDecimal  rentalprice =  new BigDecimal(((String) lineArr.get("RENTALPRICE").toString())) ;
		    String rentalpriceValue=String.valueOf(rentalprice);
			float rentalpriceVal= "".equals(rentalpriceValue) ? 0.0f :  Float.parseFloat(rentalpriceValue);
			//System.out.println(subTotalVal);
			if(rentalpriceVal==0f){
				rentalpriceValue="0.00000";
			}else{
				rentalpriceValue=rentalpriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}
			
			 BigDecimal  orderprice =  new BigDecimal(((String) lineArr.get("orderprice").toString())) ;
			    String orderpriceValue=String.valueOf(orderprice);
				float orderpriceVal= "".equals(orderpriceValue) ? 0.0f :  Float.parseFloat(orderpriceValue);
				//System.out.println(subTotalVal);
				if(orderpriceVal==0f){
					orderpriceValue="0.00000";
				}else{
					orderpriceValue=orderpriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				}
				
				 BigDecimal  tax =  new BigDecimal(((String) lineArr.get("taxval").toString())) ;
				    String taxValue=String.valueOf(tax);
					float taxVal = "".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
					//System.out.println(subTotalVal);
					if(taxVal==0f){
						taxValue="0.00000";
					}else{
						taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					 BigDecimal  totalwithtax =  new BigDecimal(((String) lineArr.get("totalwithtax").toString())) ;
					    String totalwithtaxValue=String.valueOf(totalwithtax);
						float totalwithtaxVal = "".equals(totalwithtaxValue) ? 0.0f :  Float.parseFloat(totalwithtaxValue);
						//System.out.println(subTotalVal);
						if(totalwithtaxVal==0f){
							totalwithtaxValue="0.00000";
						}else{
							totalwithtaxValue=totalwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						}

            int iIndex = iCnt + 1;
            String pickedQty = (String)lineArr.get("QTYIS");
            pickQty = pickQty + Double.parseDouble(pickedQty);
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
             %>
             
             
             var rowData = [];
             rowData[rowData.length] = '<%=(String)lineArr.get("ORDLNNO")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("ORDNO")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("refno")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("customername")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("ITEM")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("ITEMDESC")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("UNITMO")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("ORDDATE")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("EMPLOYEE")%>';
             rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYOR"))%>';
             rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYIS"))%>';
             rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYRC"))%>';
            
             
             
             
             tableData[tableData.length] = rowData ; 
             <%
  		}
  	%> 
  	<%  if (pickQty>0){btnDisabled="";}%>
  	 $(document).ready(function(){
	
  		 
  	  	 $('#tableMovementHistory').DataTable({
  	  		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
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
  	  	            	extend: 'colvis',
  	                     
  	                  }
  	  	        ],        
  	  		  	  	       
  	  		  });	 
  	 });
    </script>
            		 
 
     <b>
       <%= result%>
      </b>
          
          
    
  	
  </FORM>
  </div>
  </div>
  </div>
  
     <script>
 $(document).ready(function(){
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
          
 <script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>
   
  <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

