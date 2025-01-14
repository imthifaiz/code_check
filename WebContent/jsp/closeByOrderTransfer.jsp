<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Close Outstanding Consignment Orders";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>"/>
	<jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT%>"/>
</jsp:include>


<script language="javascript">

function popWin(URL) {

subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');

}

function setfoc() { document.form1.ITEM.focus(); }

function onForceClose(){

      var mes=confirm("Are you sure you want to Close the Orders !");
      if(mes==false)
      {
      return  false;
      }
                      
                      
   var flag = false;
   var MODULE    = document.form1.MODULENAME.value;
   if(MODULE != null    && MODULE != "") { flag = true;}
   if(flag == "false"){ alert("Please Select Order to View Order No's"); return false;}
   
     var ischeck = false;
     var Lot ;
     var concatLotDet="";
 
   var i = 0;
   var noofcheckbox=1;
   
    var noofcheckbox = document.form1.chkdLot.length;
   
    document.form1.cmd.value="CLOSE_ORDER";
    
    if(form1.chkdLot.length == undefined)
    {
             if(form1.chkdLot.checked)
            {
            document.form1.ORDERS.value=form1.chkdLot.value+",";
            document.form1.action = "../consignment/closeorder";
            document.form1.submit();
            }else if(!form1.chkdLot.checked)
            {
             alert("Please Select Orders To Close");
               return false;
            }
    
    }else
    {
    var isSelected=false;
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form1.chkdLot[i].checked;
                   if(ischeck)
                    {
                    Lot=document.form1.chkdLot[i].value;
                    concatLotDet=concatLotDet+Lot+",";
                    isSelected=true;
                    }
                }
                if(isSelected==true){
                  document.form1.ORDERS.value=concatLotDet;
                  document.form1.action = "../consignment/closeorder";
                  document.form1.submit();  
                }else{
                     alert("Please Select Orders To Close");
                     return false;
                }
    }
}


function onView(){
      
    var flag     = "false";
    var MODULE    = document.form1.MODULENAME.value;
    var ORDERNO    = document.form1.ORDERNO.value;
    if(MODULE != null    && MODULE != "") { flag = true;}
 
    if(flag == "false"){ alert("Please Select Order to View Order No's"); return false;}
    document.form1.cmd.value="View" ;
    document.form1.submit();
}



</script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
 <% 
 
        OrderTypeUtil orderType =  new OrderTypeUtil();
        orderType.setmLogger(mLogger);
        StrUtils strUtils     = new StrUtils();
        ArrayList alList  = new ArrayList();
        String PLANT        = (String)session.getAttribute("PLANT");
        String USER         = (String)session.getAttribute("LOGIN_USER");
        String action       = strUtils.fString(request.getParameter("action")).trim();
        String cmd       = strUtils.fString(request.getParameter("cmd")).trim();
      
        String  chkString= "",result="",recStr="", status="";
        String  MODULETYPE="",ORDERNO="",ORDERS="",REMARKS="";
       
        MODULETYPE            = strUtils.fString(request.getParameter("MODULENAME"));
           status  = strUtils.fString(request.getParameter("STATUS"));
        ORDERNO               = strUtils.fString(request.getParameter("ORDERNO"));
        ORDERS                = strUtils.fString(request.getParameter("ORDERS"));
         REMARKS                = strUtils.fString(request.getParameter("REMARKS"));
         if(MODULETYPE.equalsIgnoreCase(""))
         MODULETYPE="TRANSFER";
        result                = strUtils.fString(request.getParameter("result"));
        String CurrentDate    = DateUtils.getDate();
        String closeBtnEnabled="disabled";
        System.out.println("REMARKS :: "+REMARKS);
        
      if(cmd.equalsIgnoreCase("CLOSE_ORDER")){
      try{

          Map map = new HashMap();
          map.put("LOGIN_USER",USER);
          map.put("PLANT",PLANT);
          map.put("MODULE_NAME",MODULETYPE); 
          map.put("ORDERS",ORDERS);
          map.put("REMARKS",REMARKS);
          
         boolean isOrdersClosed=  orderType.process_OrderClose(map);
         if (isOrdersClosed){
               result =  "<div><b><font color='green'><center>Orders Closed successfully</center></font></b></div>";
               cmd="View";
             
                
         }else{
              result =  "<div><b><font  color='red'; align='center';>Failed to Close Orders !</font></b><div>";
           
            cmd="View";
         } 
         
    
        }catch(Exception e){System.out.println("Exception :: "+e.toString());
              result =  "<div><b><font class='mainred'><centre>" + e.getMessage() + "!</centre></font></b></div>";
            cmd="View";
        }
    
     }
 
       
         if(cmd.equalsIgnoreCase("View")){
           try{
           
           alList=  orderType.getOrderHdrDetails(PLANT,MODULETYPE,ORDERNO,status);
           if(alList.size()==0){
           recStr="No Orders Found";
              closeBtnEnabled="disabled";
           }else{
             closeBtnEnabled="";
           }
                  
          }catch(Exception e){
            System.out.println("Exception :: View : "+e.toString());
            result =  "<div><b><font class='mainred'><centre>" + e.getMessage() + "!</centre></font></b></div>";
         }
       }
   
    
   

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><label>Close Outstanding Consignment Orders</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

  <form class="form-horizontal" name="form1" method="post"  action="../consignment/closeorder">
  
  	<INPUT type="hidden" name="cmd" value=""/>
    <INPUT type="hidden" name="ORDERS" value="">
    <INPUT type="hidden" name="CUSTOMER" value="">
  
  <table  class="table" >
      <%= result%>
   </table>
   
      <div class="form-group">
      <label class="control-label col-sm-1" for="Order">Order:</label>
      <div class="col-sm-2">           	
  	  <SELECT class="form-control"  data-toggle="dropdown" data-placement="right" NAME="MODULENAME" style="width: 100%" disabled>
			<option selected value="">Choose</option>
            <option value='INBOUND' <%if(MODULETYPE.equalsIgnoreCase("INBOUND")){ %>selected <%}%>>Purchase</option>
           <!-- <option value='ESTIMATE' <%if(MODULETYPE.equalsIgnoreCase("ESTIMATE")){ %>selected <%}%>>Sales Estimate</option> -->
            <option value='OUTBOUND' <%if(MODULETYPE.equalsIgnoreCase("OUTBOUND")){ %>selected <%}%>>Sales</option>
             <option value='LOAN' <%if(MODULETYPE.equalsIgnoreCase("LOAN")){ %>selected <%}%>>Rental</option>
            <option value='TRANSFER' <%if(MODULETYPE.equalsIgnoreCase("TRANSFER")){ %>selected <%}%>>Consignment</option>
           
            <!--  <option value='WORKORDER' <%if(MODULETYPE.equalsIgnoreCase("WORKORDER")){ %>selected <%}%>>WORKORDER</option> -->
	  </select>
  	  </div>
  	  <div class="form-inline">
      <label class="control-label col-sm-1" for="Order">Status:</label>
      <div class="col-sm-2">           	
  	  <SELECT class="form-control"  data-toggle="dropdown" onChange="javascript:return StatusChange();" data-placement="right" NAME="STATUS" style="width: 100%">
			<OPTION selected  value="">Choose </OPTION>
     		<OPTION   value='O' <%if(status.equalsIgnoreCase("PARTIALLY PROCESSED")){ %>selected<%} %>>PARTIALLY PROCESSED </OPTION>
     		<OPTION   value='N' <%if(status.equalsIgnoreCase("OPEN")){ %>selected<%} %>>OPEN</OPTION>
		</select>
  	</div>
  	  </div>
  	  
      
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Event Master">Order Number:</label>
            <div class="col-sm-1">          
        <div class="input-group">
        <INPUT  class="form-control" name="ORDERNO" id="ORDERNO"type="TEXT" value="<%=ORDERNO%>"
			size="30" MAXLENGTH=30>
		<!-- 	<span class="input-group-addon" 
   		  onClick="javascript:popWin('../jsp/list/orderList.jsp?ORDERNO='+form1.ORDERNO.value +'&MODULENAME='+form1.MODULENAME.value+'&STATUS='+form1.STATUS.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
      </div>
    </div>
   </div>
   </div>
   <div class="form-group">
   <label class="control-label col-sm-5" for="view"></label>
   <div class="col-sm-1">
   <button type="button" class="Submit btn btn-default" onClick="javascript:return onView();"><b>View</b></button>
   </div>
   </div>
   
      <br>
       <TABLE style="font-size:15px" BORDER="0" CELLSPACING="0" WIDTH="100%" class="table">
        <thead style="background: #eaeafa">
        <TR>
    
           <th  align="left">Check</th>
           <th  align="left">Order No</th>
           <td  align="left"><b>Customer Name</b></td>
           <td  align="left"><b>Status</b></td>
          
           
       </TR>
       </thead>
     <tbody> 
     
  <%
         
         
          for (int iCnt =0; iCnt<alList.size(); iCnt++){
          Map lineArr = (Map) alList.get(iCnt);
          int iIndex = iCnt + 1;
           String po =(String)lineArr.get("orderNo");
           String powh =(String)lineArr.get("CustName");
           String item  =(String)lineArr.get("status");
           chkString  =(String)lineArr.get("orderNo");  
           String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
           
        // RESVI
           if(item.equalsIgnoreCase("N"))
			{
        	   item="OPEN";
							
			}
  		       else if(item.equalsIgnoreCase("O"))
  		    	 item="PARTIALLY PROCESSED";
  		  
         //ENDS
        
       %>
       <TR bgcolor = <%=bgcolor%>>
       <TD align="left"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdLot" value="<%=chkString%>"  >  </TD>
        <TD align="left" > <%=(String)lineArr.get("orderNo")%></TD>
          <TD align="left"><%=(String)lineArr.get("CustName")%></TD>
           <TD align="left"><%=item%></TD>
        </TR>
        
      <% 
     
      }%>  
     </tbody>
        </TABLE>
       <table class="table">
        <tr>
        <td align="left">
        <%= recStr%>
        </tr>
        </table>
        
         <div class="form-group">
        <label class="control-label col-sm-4" for="Reference No">Remarks:</label>
        <div class="col-sm-3">
       <TEXTAREA class="for-control" NAME="REMARKS" COLS=40 ROWS=3 wrap="hard" onkeypress="return imposeMaxLength(this, 100);" ></TEXTAREA>
       </div>
       </div>
   

    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
        <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','CO');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="javascript:return onForceClose();"  <%=closeBtnEnabled%>><b>Close Order</b></button>&nbsp;&nbsp;
      	
      </div>
    </div>
  </form>
</div>
</div>
</div>


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>
<script>
var plant= '<%=PLANT%>';
function StatusChange(){
	$("#ORDERNO").typeahead('val', '"');
	$("#ORDERNO").typeahead('val', '');
};
/* Order Number Auto Suggestion */

$('#ORDERNO').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'TONO',  
	  source: function (query, process,asyncProcess) {
		  var urlStr = "/track/TransferOrderServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					Submit : "GET_ORDER_NUM_FOR_FORCE_CLOSE",
					STATUS : document.form1.STATUS.value,
					TONO : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.orders);
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
	    return '<p>' + data.TONO + '</p>';
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


</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

