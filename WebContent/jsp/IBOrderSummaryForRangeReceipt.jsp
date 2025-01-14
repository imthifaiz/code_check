<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%--New page design begin --%>
<%
String title = " Goods Receipt by Purchase Order (By Serial)";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION%>"/>
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
	}
</style>
<%--New page design end --%>

<!-- <script
	src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>

<SCRIPT>

var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'InboundOrderSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onReceive(form){
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 var j=0;
var pono=document.form.PONO.value;
   var i = 0;
   var noofcheckbox=1;
   if(pono==null||pono=="")
	{
	alert("Please select Order Number");	
	return false;
	}    
    if(document.form.AFLAG.value==null||document.form.AFLAG.value=="" ||document.form.AFLAG.value!="DATA")
    {
	   	alert("No Data's Found For Receiving");
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
               alert("Please Select Product For Receive");
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
                alert("Please Select Product For Receive");
                return false;
              }
              document.form.TRAVELER.value=concatTraveler;
             return true;
            
    }
   
    
    return false;
   }
  
   

    
</SCRIPT>

<%--New page design begin --%>

		
 <div class="box-body">
<%--New page design end --%>

<%-- <title>Goods Receipt by Inbound Order (By Range)</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%> --%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />

<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />

<%
    POUtil _poUtil=new POUtil();
	_poUtil.setmLogger(mLogger);
    session = request.getSession();

    String action   = StrUtils.fString(request.getParameter("action")).trim();
    String pono     = StrUtils.fString(request.getParameter("PONO"));
    String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String AFLAG=    (String) session.getAttribute("AFLAG");

  
    String vend = "",deldate="",jobNum = "",custName = "",personIncharge = "",contactNum = "",chkString ="";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="",
    contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="";
    String sSaveEnb    = "disabled";
    String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
    Map mp= new HashMap();
     if(action.equalsIgnoreCase("View")){
     // try{

   
      Map m=(Map)request.getSession().getAttribute("isummaryvalue");
      mp=(Map)request.getSession().getAttribute("isummaryvalue");  //not by Draft - azees
      //fieldDesc=(String)request.getSession().getAttribute("ISUMMARYRESULT");
       fieldDesc=(String)request.getSession().getAttribute("RESULTINBRECEIVE");
       fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";


       if(m.size()>0){
     
       jobNum=(String)m.get("jobNum");
       custName=(String)m.get("custName");
       personIncharge=(String)m.get("contactname");
      
       contactNum=(String)m.get("contactNum");
       telno=(String)m.get("telno");
       email=(String)m.get("email");
       add1=(String)m.get("add1");
       add2=(String)m.get("add2");
       add3=(String)m.get("add3");
       add4=(String)m.get("add4");
       country=(String)m.get("country"); zip=(String)m.get("zip");
       remarks=(String)m.get("remarks");
       
       address=(String)m.get("address");
       address2=(String)m.get("address2");
       address3=(String)m.get("address3");
       collectionDate=(String)m.get("collectionDate");
       collectionTime=(String)m.get("collectionTime");
       remark1=(String)m.get("remark1");
       remark2=(String)m.get("remark2");
      
       }
      
      
    }
  
    else if(action.equalsIgnoreCase("catcerror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
    if (request.getParameter("msg") != null) {
    	  String msg = request.getParameter("msg");
    	  if (msg != null){
    		  if (msg.contains("not sent")){
    			  fieldDesc="<font class='mainred'>"+msg+"</font>";
    		  }else{
    			  fieldDesc="<font class='maingreen'>"+msg+"</font>";
    		  }
    	  }
      }
    
    request.getSession().setAttribute("RESULTINBRECEIVE","");
    request.getSession().setAttribute("podetVal","");
    request.getSession().setAttribute("CATCHERROR","");
      
  //}
 
%>
<span style="text-align: center;">
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</span>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
             <ol class="breadcrumb backpageul" >      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                  <li><a href="../purchaseTransactionDashboard"><span class="underline-on-hover">Purchase Transaction Dashboard</span></a></li>                  
                  <li>Goods Receipt by Purchase Order (By Serial)</li>                                    
             </ol>   
     <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
			   </h1>
		</div>
		
 <div class="box-body">

<form class="form-horizontal" name="form" method="post" action="/track/OrderReceivingServlet?">
  	
       <div class="form-group">
       <label class="control-label col-sm-4" for="inbound Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH=20 name="PONO" value="<%=pono%>"
    		onkeypress="if((event.keyCode=='13') && ( document.form.PONO.value.length > 0)){loadInbundOrderDetails();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/po_list_po.jsp?PONO='+form.PONO.value+'&OpenForReceipt=yes');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Purchase Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" name="actionButton" onClick="viewInboundOrders();"><b>View</b></button>
  		</div>
  		</div>  
 		</div>
 		
 		<div class="form-group">
  		<label class="control-label col-sm-4" for="supplier name">Supplier Name:</label>
        <div class="col-sm-4">
        <INPUT class="form-control" name="CUST_NAME" type="TEXT" value="<%=StrUtils.forHTMLTag(custName)%>" size="30" MAXLENGTH=80>
    	</div>
 		</div>
 			
 					<INPUT type = "hidden" name="CUST_CODE"  value="">
                    <INPUT type = "hidden" name="CUST_CODE1" value="">
                    <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    
                    
        <div class="form-group">
    	<label class="control-label col-sm-4" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-4">
        <INPUT type = "TEXT" size="30"  MAXLENGTH="20"  class = "form-control" readonly name="PERSON_INCHARGE" value="<%=personIncharge%>">
        </div>
 		</div>            
 			
        <div class="form-group">
        <label class="control-label col-sm-4" for="Reference No">Reference No:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH=20 name="JOB_NUM" class="form-control" readonly value="<%=jobNum%>">
       </div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-4" for="Order Date">Order Date:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="10"	name="DELDATE" class="form-control" readonly	value="<%=collectionDate%>" />
        </div>
      	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT  class="form-control" name="TELNO" type="hidden" value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 
 		<div class="form-group">
        <label class="control-label col-sm-4" for="Time">Order Time:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" readonly MAXLENGTH="20" name="COLLECTION_TIME"	value="<%=collectionTime%>" />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT	type="hidden" size="30" class="form-control" readonly MAXLENGTH="20"name="EMAIL" value="<%=email%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="REMARK1" class="form-control" readonly	value="<%=remark1%>" />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH="20" name="ADD1" value="<%=add1%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		 		
    	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD2" value="<%=add2%>" />
        <!-- </div>
 		</div>
 		</div> -->
 		
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
       <!--  </div>
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
    	<label class="control-label col-sm-8" for="customer remarks">Supplier Remarks:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
        <!-- </div>
 		</div>
 		</div> -->
 			  		
  		 <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table" >
         <thead style="background: #eaeafa; font-size: 15px">
         <tr>
          				<th width="10%">SELECT </th>
						<th width="10%">ORDERLNO </th>
						<th width="15%">PRODUCT ID </th>
						<th width="20%">DESCRIPTION </th>
						<th width="10%">UOM </th>
						<th width="15%">ORDER QUANTITY</th>
						<th width="15%">RECEIVED QUANTITY</th>
						<th width="10%">STATUS </th>
      </tr>
       	 </thead>
       	 <tbody style="font-size: 15px;">
       	
					<% 
					if(mp.size()>0){
       ArrayList al= _poUtil.listInboundSummaryPODET(pono,plant);
       
       if(al.size()==0)
       {
    	   AFLAG="";
       }
      
       if(al.size()>0)
       {
        ItemMstDAO _ItemMstDAO= new ItemMstDAO();
        InvMstDAO _InvMstDAO= new InvMstDAO();
        _ItemMstDAO.setmLogger(mLogger);
        _InvMstDAO.setmLogger(mLogger);
        AFLAG="DATA";
       for(int i=0 ; i<al.size();i++)
       {
          
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          String strpono = (String)m.get("pono");
          String polnno = (String)m.get("polnno");
          String sname= (String)m.get("sname");
          String item= (String)m.get("item");
          String desc= _ItemMstDAO.getItemDesc(plant ,item);
          MLogger.log(0, "Item Desc = " + desc);
          //getting item loc
     
          String qtyor= (String)m.get("qtyor");
          String qtyrc= (String)m.get("qtyrc");
          String ref=(String)m.get("ref");
          String lnstat= (String)m.get("lnstat");
          String uom= (String)m.get("uom");
          String UOMQTY= (String)m.get("UOMQTY");
          chkString  =pono+","+polnno+","+item+","+StrUtils.replaceCharacters2Send(desc)+","+custName+","+uom+","+UOMQTY+","+personIncharge+","+telno+","+email+","+add1+","+add2+","+add3+","+qtyor+","+qtyrc+","+ref;
         
      %>
					<TR bgcolor="<%=bgcolor%>">

						<TD width="10%" align="left"><font color="black"><INPUT
							Type=radio style="border: 0;" name="chkdDoNo"
							value="<%=chkString%>"></font></TD>
						<TD width="10%" align="center"><font color="black"><%=(String)m.get("polnno")%></font></TD>

						<TD align="left" width="15%"><%=(String)m.get("item")%></TD>
						<TD align="left" width="20%"><%=(String)desc%></TD>
						<TD align="left" width="10%"><%=(String)uom%></TD>
						<TD align="center" width="15%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
						<TD align="center" width="15%"><%=StrUtils.formatNum((String)m.get("qtyrc"))%></TD>
						<TD align="left" width="10%"><%=(String)m.get("lnstat")%></TD>
					</TR>
					<%}} else 
    	   {%>
					<TR>
						<td colspan="7" align="center">Data's Not Found</TD>
					</TR>
					<%} }%>
         </tbody>
       </TABLE>
       
       <INPUT type="hidden" size="20" MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
	   <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1"> 
	   <INPUT type="Hidden" name="RFLAG" value="1"> 
	   <INPUT type="Hidden"	name="AFLAG" value="<%=AFLAG%>">
  		 
          		
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="if(onReceive(document.form)) {submitForm();}"><b>Receive</b></button>&nbsp;&nbsp;
      	<INPUT	type="hidden" name="TRAVELER" value="">
      	</div>
        </div>
         		
  		</form>
		</div>
		</div>
		</div>
</div>
<script>

function viewInboundOrders(){
			document.form.action="/track/OrderReceivingServlet?action=ViewByRange";
			//document.form.action.value ="View";
            document.form.submit();
		}
function submitForm(){
	document.form.action ="/track/OrderReceivingServlet?action=ReceiveByRange";
    document.form.submit();  
}
function loadInbundOrderDetails() {
	var inboundOrderNo = document.form.PONO.value;
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : inboundOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_INBOUND_ORDER_DETAILS"
			},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.JOB_NUM.value = resultVal.JOBNUM;
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.action = "/track/OrderReceivingServlet?action=ViewByRange";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.PONO.value = "";
							document.form.JOB_NUM.value = "";
							document.form.CUST_NAME.value = "";
							document.form.PONO.focus();
						}
					}
				});
	}
</script> 
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>