<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>

<%@page import="com.track.constants.IDBConstants"%>

<%--New page design begin --%>
<%
String title = "Pick/Issue By Transfer Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>


<!-- <html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script> -->
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<!-- <title>Pick/Issue By Transfer Order </title>
<link rel="stylesheet" href="css/style.css"> -->
<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'TransferOrderSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

	function sf()
	{
		document.form.TONO.focus();
	}
	function validatePO(form)
	{
	if (form.TONO.value.length < 1)
  	{
    	alert("Please Enter TO Number !");
    	form.TONO.focus();
    	return false;
 	 }
 
	}

function onViewToConfirm(form)
{
  var flag    = "false";
  
    var TONO   = document.form.TONO.value;
   var CUST_NAME = document.form.CUST_NAME.value;
    var FROMLOC   = document.form.FROM_WAREHOUSE.value;
   var TOLOC = document.form.TO_WAREHOUSE.value;
   if(TONO == "" || TONO == null) {alert("Please Select Transfer Order "); document.form.TONO.focus(); return false; }

  document.form.action="transOrderReceiptToConfirm.jsp?ORDERNO="+TONO+"&CUSTNAME="+CUST_NAME+"&FROMLOC="+FROMLOC+"&TOLOC="+TOLOC;
  document.form.submit();
 }

	function SetChecked(val)
	{
		dml=document.form;
		len = dml.elements.length;
		var i=0;

		for( i=0; i<len; i++)
 		{
	 		dml.elements[i].checked=val;

 		}
	}

	function popWin(vname){
    	window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
	}

	function onDelete(form)
	{
   
	if (form.DONO.value.length < 1)
    {
    	alert("Please Enter TO Number !");
    	form.DONO.focus();
    	return false;
    }
    else{
     	var mes=confirm("Do you want to delete the Transfer order !");
      	if(mes==true)
      	{
      		return true;
      	}
      	else
      	{  
      		return  false;
      	}
    }
    
	}

	function onUpdate(form)
	{
    	if (form.TONO.value.length < 1)
    	{
    		alert("Please Enter TO Number !");
    		form.DONO.focus();
    		return false;
    	}
    	else
        {
     		var mes=confirm("Do you want to update the  Transfer order !");
      		if(mes==true)
      	{
      		return true;
      	}
      	else
      	{  
      		return  false;
      	}
    }
  
	}
	function onNew(form)
	{
    	document.form.TONO.value   ="DUMMY";
    	document.form.CUST_NAME.value       ="DUMMY";
    	form.JOB_NUM.value         ="";
    	form.PERSON_INCHARGE.value ="";
    	form.CONTACT_NUM.value     ="";
    	form.ADDRESS.value         ="";
    	form.ADDRESS2.value        ="";
    	form.ADDRESS3.value        ="";
    	form.DELDATE.value         ="";
    	form.COLLECTION_TIME.value ="";
    	form.REMARK1.value         ="";
    	form.REMARK2.value         ="";
    	return true;
	}
 
	function onIssue(form){
 		var ischeck = false;
 		var Traveler ;
 		var concatTraveler="";
 		var j=0;
		var pono=document.form.TONO.value;
  		var i = 0;
   		var noofcheckbox=1;
   		if(pono==null||pono=="")
		{
			alert("Please select Transfer Order");	
			return false;
		}    
   	 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
     	{
    	   	alert("No Data's Found For Picking");
    	 	return false;
     	}
   		var noofcheckbox = document.form.chkdDoNo.length;
	
    	if(form.chkdDoNo.length == undefined)
    	{
           if(form.chkdDoNo.checked)
            {
              document.form.TRAVELER.value=form.chkdDoNo.value+"=";
              return true;
            }
            
            else
            {
               alert("Please Select Product For Picking");
               return false;
            }
    
    	}else
    	{
           
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form.chkdDoNo[i].checked;
                   if(ischeck)
                    {
                      j=j+1;
                      Traveler=document.form.chkdDoNo[i].value;
                      concatTraveler=concatTraveler+Traveler+"=";
                    }
   
               }
        	  if(j==0)
              {
                alert("Please Select Product For Picking");
                return false;
              }
        	
              document.form.TRAVELER.value=concatTraveler;
              document.form.action ="/track/TransferOrderServlet?Submit=SingleStepPick/Issue";
              document.form.submit();  
            
   		 }
    return false;
  }
</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.TOHDR" />
<jsp:useBean id="pdb" class="com.track.tables.TODET" />

<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%
   session = request.getSession();
   String plant=(String)session.getAttribute("PLANT");
   String tono     = su.fString(request.getParameter("TONO"));
   String action   = su.fString(request.getParameter("action")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   String AFLAG=    (String) session.getAttribute("AFLAG");
   session.setAttribute("RFLAG","7");
   boolean confirm = false;
    
   TOUtil _TOUtil=new TOUtil();
   ToHdrDAO _ToHdrDAO =new ToHdrDAO(); 
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
 
   _TOUtil.setmLogger(mLogger);
   _ToHdrDAO.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   
   String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="";
   String fromWareHouse="",toWareHouse="";
   String sSaveEnb    = "disabled";
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";

     
   if(action.equalsIgnoreCase("View")){
      Map m=(Map)request.getSession().getAttribute("todetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULTPICKING");
      if(request.getSession().getAttribute("RESULTPICKING")==null )
      {
    	  fieldDesc="";
      }
      request.getSession().setAttribute("RESULTPICKING","");
   
      if(m.size()>0){
         jobNum=(String)m.get("jobNum");
         fromWareHouse=(String)m.get("fromwarehouse");
    	 toWareHouse=(String)m.get("towarehouse");
         custName=(String)m.get("custName");
         custCode=(String)m.get("custCode");

         personIncharge=(String)m.get("contactname");
         contactNum=(String)m.get("contactNum");
         telno=(String)m.get("telno");
         email=(String)m.get("email");
         add1=(String)m.get("add1");
         add2=(String)m.get("add2");
         add3=(String)m.get("add3");
         add4=(String)m.get("add4");
         country=(String)m.get("country"); 
         zip=(String)m.get("zip");
         remarks=(String)m.get("remarks");
         contactNum=(String)m.get("contactNum");
         address=(String)m.get("address");
         address2=(String)m.get("address2");
         address3=(String)m.get("address3");
         deldate=(String)m.get("collectionDate");
         collectionTime=(String)m.get("collectionTime");
         remark1=(String)m.get("remark1");
         remark2=(String)m.get("remarks");
      }
      else 
      {
        fieldDesc="Details not found for transfer order:"+ tono;  
      }
    }
     else if(action.equalsIgnoreCase("result"))
     {
        fieldDesc=(String)request.getSession().getAttribute("RESULT");
        fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      }
     else if(action.equalsIgnoreCase("resulterror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
     }
     else if(action.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
     }
   
%>
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<form class="form-horizontal" name="form" method="post" action="/track/TransferOrderServlet?" onsubmit="return validatePO();">
 
 <center>
   <h2><small><%=fieldDesc%></small></h2>
  </center>
  
   		
       <div class="form-group">
       <label class="control-label col-sm-4" for="outbound Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-4">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="TONO" value="<%=tono%>" 
       onkeypress="if((event.keyCode=='13') && ( document.form.TONO.value.length > 0)){loadTransferOrderDetails();}"/>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('to_list_do.jsp?TONO='+form.TONO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Transfer Order Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" onClick="viewTransferOrders();"><b>View</b></button>
  		</div>
  		</div>  
 		</div>
 		
 		<div class="form-group">
  		<label class="control-label col-sm-4" for="Assignee name">Customer Name:</label>
        <div class="col-sm-4">
        <INPUT name="CUST_NAME"   class="form-control" MAXLENGTH="20" readonly type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
    	</div>
 		</div>
 		
 		<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
        <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
        <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
        
        <div class="form-group">
    	<label class="control-label col-sm-4" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-4">
        <INPUT type = "TEXT" size="30"  MAXLENGTH="20"  class = "form-control" readonly name="PERSON_INCHARGE" value="<%=personIncharge%>">
        </div>
 		</div>
        
        <div class="form-group">
        <label class="control-label col-sm-4" for="From Location">From Location:</label>
        <div class="col-sm-4">
        <INPUT type = "TEXT" size="30"   class="form-control" MAXLENGTH="20" readonly  MAXLENGTH=20 name="FROM_WAREHOUSE" value="<%=fromWareHouse%>">
        </div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-4" for="To Location">To Location:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" MAXLENGTH="20" readonly name="TO_WAREHOUSE" value="<%=toWareHouse%>"/>
        </div>
      	<INPUT type = "Hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT  class="form-control" name="TELNO" type="hidden" value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 
 		<div class="form-group">
        <label class="control-label col-sm-4" for="Order Date">Order Date:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" class="form-control" MAXLENGTH="20" readonly size="30" name="DELDATE" value="<%=deldate%>"/>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT	type="hidden" size="30" class="form-control" readonly MAXLENGTH="20"name="EMAIL" value="<%=email%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-4" for="Time">Order Time:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" MAXLENGTH="20" readonly name="COLLECTION_TIME" value="<%=collectionTime%>"/>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH="20" name="ADD1" value="<%=add1%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		 		
    	<div class="form-group">
    	 <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20" class="form-control" readonly name="REMARK1" value="<%=remark1%>"/>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD2" value="<%=add2%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		
    	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD3" value="<%=add3%>" />
        <!-- </div>
 		</div>
 		 </div> -->
 		 
      	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="City">City:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD4" value="<%=add4%>" />
        <!-- </div>
 		</div>
 		</div> -->
 		
 		<!-- <div class="form-group">
 		<div class="form-inline">
    	<label class="control-label col-sm-8" for="State">State:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="" value="" />
        <!-- </div>
 		</div> 
 	 	</div> -->
 	 	
    	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="country">Country:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="COUNTRY" readonly value="<%=country%>" /> 
        <!-- </div>
 		</div>
 		</div> -->
 	 	 		
        <!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-8" for="postal code">Postal Code:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="ZIP"	readonly value="<%=zip%>" />
        <!-- </div>
 		</div>
 		</div> -->

        <!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-8" for="Assignee remarks">Assignee Remarks:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
        <!-- </div>
 		</div>
 		</div> -->
 			  		
  		 <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table" >
         <thead style="background: #eaeafa; font-size: 15px">
         <tr>
        <th width="8%">Select </th>
         <th width="10%">Order Line No </th>
         <th width="15%">Product ID</th>
         <th width="20%">Description</th>
         <th width="15%">Order Qty</th>
         <th width="14%">Picked Qty</th>
         <th width="10%">Received Qty</th>
         <th width="5%"><%=IDBConstants.UOM_LABEL%></th>
         <th width="5%">Status</th>
         </tr>
       	 </thead>
       	 <tbody style="font-size: 15px;">
       	<% 
      	String fromwarehouse=""; 
      	String towarehouse="";
      	ArrayList al= _TOUtil.listTODETDetails(plant,tono);
      	if(al.size()==0)
        {
     	   AFLAG="";
        }
       if(al.size()>0)
       	{
    	   AFLAG="DATA";
       	for(int i=0 ; i<al.size();i++)
       	{
          
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          tono = (String)m.get("tono");
          String tolnno = (String)m.get("tolnno");
          String custname= (String)m.get("custname");
          String item= (String)m.get("item");
          String loc= (String)m.get("loc");
          String batch= (String)m.get("batch");
          String qtyor= (String)m.get("qtyor");
          String qtyPick= (String)m.get("qtyPick");
          String qtyrc= (String)m.get("qtyrc");
          String desc= _ItemMstDAO.getItemDesc(plant ,item);
          String uom= _ItemMstDAO.getItemUOM(plant ,item);
          ArrayList alLoc= _ToHdrDAO.getTransferOrderLocDetailsByWms(plant,tono);
          if(alLoc.size()>0)
          	{
          		for(int j=0 ;j<alLoc.size();j++)
          		{
          			 MLogger.log(0, "******************get Transfer Order TOHDR Loc Detail : " + i);
          		 	 Map k=(Map)alLoc.get(j);
          		     fromwarehouse = (String)k.get("fromwarehouse");
          		     towarehouse = (String)k.get("towarehouse");
                	  
          		}
           }
         chkString  =tono+","+tolnno+","+item+","+StrUtils.replaceCharacters2Send(desc)+","+qtyor+","+qtyPick+","+qtyrc+","+sUserId+","+loc+","+batch+","+custname+","+fromwarehouse+","+towarehouse;
       %>
        <TR bgcolor = "<%=bgcolor%>">
              <TD width="8%"  align="CENTER"><font color="black"><INPUT Type=radio  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=chkString%>"></font></TD>
               <!--  <TD width="10%" align="center"><font color="black"><a  href="ModifyOutgoingOrderDetail.jsp?TONO=<%=(String)m.get("tono")%>&TOLNNO=<%=(String)m.get("tolnno")%>&CUSTNAME=<%=(String)m.get("custname")%>&ITEM=<%=(String)m.get("item")%>&ITEMDESC=<%=(String)desc%>&ORDERQTY=<%=(String)m.get("qtyor")%>&PICKEDQTY=<%=(String)m.get("qtyPick")%>&ISSUEDQTY=<%=(String)m.get("qtyis")%>")%><%=(String)m.get("tolnno")%></a></font></TD>-->
               <TD width="10%" align="center"><%=(String)m.get("tolnno")%></TD>
              <TD align="left" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="left" width="20%"><%=(String)desc%></TD>
              <TD align="center" width="15%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
              <TD align="center" width="14%"><%=StrUtils.formatNum((String)m.get("qtyPick"))%></TD>
              <TD align="center" width="10%"><%=StrUtils.formatNum((String)m.get("qtyrc"))%></TD>
              <TD align="left" width="5%"><%=uom%></TD>
              <TD align="center" width="5%"><%=(String)m.get("pickstatus")%></TD>
           </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" colspan="9"> Data's Not Found For Order</TD></TR>
       <%}%>
         </tbody>
       </TABLE>
       
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="7">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="if(onIssue(document.form)){submitForm();}"><b>Pick And Issue</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onViewToConfirm(document.form);"><b>Confirm</b></button>&nbsp;&nbsp;
      	<INPUT	type="hidden" name="TRAVELER" value="">
      	<INPUT type="hidden" name="Submit" value="Pick/Issue">
      	<INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 >
      	</div>
        </div>
         		
  		</form>
		</div>



<script>

function viewTransferOrders(){
			document.form.action="/track/TransferOrderServlet?Submit=View";
			//document.form.action.value ="View";
            document.form.submit();
		}
function submitForm(){
	document.form.action="/track/TransferOrderServlet?Submit=SingleStepPick/Issue";
	//document.form.action.value ="View";
    document.form.submit();
}
function loadTransferOrderDetails() {
	var transferOrderNo = document.form.TONO.value;
	var urlStr = "/track/TransferOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : transferOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_TRANSER_ORDER_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.action = "/track/TransferOrderServlet?Submit=View";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.TONO.value = "";
							document.form.CUST_NAME.value = "";
							document.form.TONO.focus();
						}
					}
				});
	}
</script>

</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
