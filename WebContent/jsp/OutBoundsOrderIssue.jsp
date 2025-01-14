<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<%--New page design begin --%>
<%
String title = "Goods Issue By Sales Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
    <jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
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
	}</style>
<%--New page design end --%>

<!-- <html>
<script
	src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>
<!-- <title>Goods Issue By Outbound Order </title>
<link rel="stylesheet" href="css/style.css"> -->

<script>

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'OutBoundsOrderIssue', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
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

function onIssue(form){
 	var ischeck = false;
 	var Traveler ;
 	var concatTraveler="";
 	var j=0;

  	var i = 0;
    var noofcheckbox=1;
    if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
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
               alert("Please Select Product For Issue");
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
                alert("Please Select Product For Issue");
                return false;
              }
              document.form.TRAVELER.value=concatTraveler;
              return true;
           
    }
  }
</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%

db.setmLogger(mLogger);
vmb.setmLogger(mLogger);
phb.setmLogger(mLogger);
pdb.setmLogger(mLogger);

   String plant=(String)session.getAttribute("PLANT");
   PlantMstDAO plantMstDAO = new PlantMstDAO();
   ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
   Map plntMap = (Map) plntList.get(0);
   String PLNTDESC = (String) plntMap.get("PLNTDESC");
   String DONO     = StrUtils.fString(request.getParameter("DONO"));
   String action   = StrUtils.fString(request.getParameter("action")).trim();
   String result   = StrUtils.fString(request.getParameter("result")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   String userId = (String) session.getAttribute("LOGIN_USER");
   String RFLAG=    (String) session.getAttribute("RFLAG");
   String AFLAG=    (String) session.getAttribute("AFLAG");
   
   com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
   String gino = _TblControlDAO.getNextOrder(plant,userId,"GINO");
   
   boolean confirm = false;
   DOUtil _DOUtil=new DOUtil();
   
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
   
   _DOUtil.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   
   
   String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="";
   String sSaveEnb    = "disabled";
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
  
   if(action.equalsIgnoreCase("View")){
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      
      if(m.size()>0){
       jobNum=(String)m.get("jobNum");
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
        fieldDesc="Details not found for Order:"+ DONO;
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
    }
     
     if(result.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
        fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
     }
 %>
<%--New page design begin --%>
 
<center>
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>                
                <li><a href="../salesTransactionDashboard"><span class="underline-on-hover">Sales Transaction Dashboard</span> </a></li>                
                <li>Goods Issue By Sales Order</li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../salesTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>
<form class="form-horizontal" name="form" id="frmOutBoundOrderIssue" method="post" action="/track/OrderIssuingServlet?">

  
   		
   		
       <div class="form-group">
       <label class="control-label col-sm-4" for="outbound Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-4">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="DONO" id="DONO" value="<%=DONO%>" 
       onkeypress="if((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){loadOutboundOrderDetails();}"/>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/do_list_do.jsp?DONO='+form.DONO.value+'&OpenForPick=yes');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Sales Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" onClick="viewOutboundOrders();"><b>View</b></button>
  		</div>
  		</div>  
 		</div>
 		
 		
 		<div class="form-group">
  		<label class="control-label col-sm-4" for="customer name">Customer Name:</label>
        <div class="col-sm-4">
        <INPUT name="CUST_NAME" id="CUST_NAME"  class="form-control" MAXLENGTH="20" readonly type = "TEXT" value="<%=StrUtils.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
    	</div>
 		</div>
 		
 					<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                    <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
        
        <div class="form-group">
    	<label class="control-label col-sm-4" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-4">
        <INPUT type = "TEXT" size="30"  MAXLENGTH="20"  class = "form-control" readonly name="PERSON_INCHARGE" value="<%=StrUtils.forHTMLTag(personIncharge)%>">
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
        <INPUT type="TEXT" size="30" MAXLENGTH="10"	name="DELDATE" class="form-control" readonly	value="<%=deldate%>" />
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
        <label class="control-label col-sm-4" for="Time">Order Time:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" readonly MAXLENGTH="20" name="COLLECTION_TIME"	value="<%=collectionTime%>" />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT	type="hidden" size="30" class="form-control" readonly MAXLENGTH="20"name="EMAIL" id="EMAIL" value="<%=email%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		
 		<div class="form-group" style="display:none">
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
 		<div class="form-group">
        <label class="control-label col-sm-4" for="GINO">GINO:</label>
        <div class="col-sm-4">
                <div class="input-group">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="INVOICENO" id="INVOICENO" value="<%= gino %>" class="form-control" />
        <span class="input-group-addon"  onClick="onNew()">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
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
    	<label class="control-label col-sm-8" for="customer remarks">Customer Remarks:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
        <!-- </div>
 		</div>
 		</div> -->
 			  		
  		 <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table" >
         <thead style="background: #eaeafa; font-size: 15px">
         <tr>
         <th width="5%">CHK</th>
         <th width="10%">ORDER LINE NO</th>
         <th width="15%">PRODUCT ID</th>
         <th width="20%">DESCRIPTION</th>
         <th width="15%">ORDER QTY </th>
         <th width="14%">PICK QTY </th>
         <th width="8%">ISSUE QTY </th>
         <th width="5%"><%=IDBConstants.UOM_LABEL%></th>
         <th width="5%">STATUS</th>   
         </tr>
       	 </thead>
       	 <tbody style="font-size: 15px;">
        <% 
      ArrayList al= _DOUtil.listOutGoingIssueDODET(plant,DONO);
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
          String dono = (String)m.get("dono");
          String dolnno = (String)m.get("dolnno");
          String custname= (String)m.get("custname");
          String item= (String)m.get("item");
          String loc= (String)m.get("loc");
         
          String batch= (String)m.get("batch");
          String qtyor= (String)m.get("qtyor");
          String qtyPick= (String)m.get("qtyPick");
          String qtyis= (String)m.get("qtyis");
          String desc= _ItemMstDAO.getItemDesc(plant ,item);
          String uom = (String)m.get("UNITMO");
          String UOMQTY= (String)m.get("UOMQTY");
          String unitprice= (String)m.get("unitprice");
          chkString  =dono+","+dolnno+","+item+","+StrUtils.replaceCharacters2Send(desc)+","+qtyor+","+qtyPick+","+qtyis+","+sUserId+","+loc+","+batch+","+UOMQTY+","+unitprice+","+uom;
       %>
        <TR bgcolor = "<%=bgcolor%>">
           
              <TD width="5%"  align="left"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=chkString%>"></font></TD>
              <!--<TD width="10%" align="center"><font color="black"><a  href="ModifyOutgoingOrderDetail.jsp?DONO=<%=(String)m.get("dono")%>&DOLNNO=<%=(String)m.get("dolnno")%>&CUSTNAME=<%=(String)m.get("custname")%>&ITEM=<%=(String)m.get("item")%>&ITEMDESC=<%=(String)desc%>&ORDERQTY=<%=(String)m.get("qtyor")%>&PICKEDQTY=<%=(String)m.get("qtyPick")%>&ISSUEDQTY=<%=(String)m.get("qtyis")%>")%><%=(String)m.get("dolnno")%></a></font></TD>-->
              <TD align="center" width="10%"><%=(String)m.get("dolnno")%></TD>
              <TD align="left" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="left" width="20%"><%=(String)desc%></TD>
              <TD align="center" width="15%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
              <TD align="center" width="14%"><%=StrUtils.formatNum((String)m.get("qtyPick"))%></TD>
              <TD align="center" width="8%"><%=StrUtils.formatNum((String)m.get("qtyis"))%></TD>
              <TD align="left" width="5%"><%=uom%></TD>
              <TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
           </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" colspan="9"> Data's Not Found For Issuing</TD></TR>
       <%}%>

         </tbody>
       </TABLE>
       
       	<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="1">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        
  		<div class="form-group">        
      	<div class="col-sm-12" align="left">
      	<div class="dropup">
		<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
	    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Issue Goods
	    <span class="caret"></span></button>
	    <ul class="dropdown-menu">
	      <li><a id="btnRedeive" href="#" onclick="if(onIssue(document.form)) {submitForm();}">Issue Goods</a></li>
	      <li><a id="btnReceiveEmail" href="#" onclick="if(onIssue(document.form)) {submitFormAndSendEmail();}">Issue Goods and Send Email</a></li>
	    </ul>
<!-- 	    <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
	  </div>
       	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<INPUT	type="hidden" name="TRAVELER" value="">
      	<INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 >
      	</div>
        </div>
         		
  		</form>
		</div>


<script>
function onNew()
{
	
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "GINO"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.invno;
				document.form.INVOICENO.value= resultV;
	
			} else {
				alert("Unable to genarate GINO");
				document.form.INVOICENO.value = "";
			}
		}
	});	
	}
function viewOutboundOrders(){
	document.form.action="/track/OrderIssuingServlet?action=View";
    document.form.submit();
}
function submitForm(){
	var invval = document.form.INVOICENO.value.length;
	 if(invval>0)
	 {
		 var urlStr = "/track/OutboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
				PLANT : "<%=plant%>",
				ACTION : "VGINO",
				GINO : document.form.INVOICENO.value
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;				
						var resultV = resultVal.invno;
						document.form.INVOICENO.value= resultV;
						document.form.action="/track/OrderIssuingServlet?action=Goods Issue";
					    document.form.submit();
					} else {
						alert("GINO "+document.form.INVOICENO.value+" already converted as Invoice. Please process with the new GINO.");
						document.form.INVOICENO.value = "";
					}
				}
			});	
	
	 }
	 else
		 {
		/*  if (confirm('Are you sure to submit this transaction without InvoiceNo?')) {
			 document.form.action="/track/OrderIssuingServlet?action=Goods Issue";
			    document.form.submit();
		 } else { */
			 alert("You are not able to submit this transaction without GINO");
			 document.getElementById("INVOICENO").focus();
		 //}		 
		 }
}
function submitFormAndSendEmail(){
	var invval = document.form.INVOICENO.value.length;
	 if(invval>0)
	 {
		 var urlStr = "/track/OutboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
				PLANT : "<%=plant%>",
				ACTION : "VGINO",
				GINO : document.form.INVOICENO.value
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;				
						var resultV = resultVal.invno;
						document.form.INVOICENO.value= resultV;
					    urlStr = "/track/OrderIssuingServlet?action=Goods Issue";
						var formData = $('#frmOutBoundOrderIssue').serialize();
						$.ajax( {
							type : "POST",
							url : urlStr,
							data : formData,
							beforeSend: function(){
								showLoader();
							}, 
							complete: function(){
								hideLoader();
							},
							dataType : "json",
							success : function(data) {
								if (data.ERROR_CODE == "100") {
									$('.success-msg').html(data.MESSAGE).css('display', 'inline');
									$('#common_email_modal').modal('toggle');
									$('#send_to').val($('#EMAIL').val()).multiEmail();
									$('#send_subject').val($('#template_subject').val()
															.replace(/\{COMPANY_NAME\}/, $('#plant_desc').val())
															.replace(/\{ORDER_NO\}/, $('#DONO').val())
															.replace(/\{ORDER_NO_2\}/, $('#INVOICENO').val())
															);
									$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
												$('#template_body').val()
												.replace(/\{ORDER_NO\}/, $('#DONO').val())
												.replace(/\{CUSTOMER_NAME\}/, $('#CUST_NAME').val())
												);
									$('#send_attachment').val('Goods Issue');
								} else {
									$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
								}
							}
						});	
					} else {
						alert("GINO "+document.form.INVOICENO.value+" already converted as Invoice. Please process with the new GINO.");
						document.form.INVOICENO.value = "";
					}
				}
			});	
	
	 }
	 else
		 {
		/*  if (confirm('Are you sure to submit this transaction without InvoiceNo?')) {
			 document.form.action="/track/OrderIssuingServlet?action=Goods Issue";
			    document.form.submit();
		 } else { */
			 alert("You are not able to submit this transaction without GINO");
			 document.getElementById("INVOICENO").focus();
		 //}		 
		 }
}
function loadOutboundOrderDetails() {
	var outboundOrderNo = document.form.DONO.value;
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : outboundOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_OUTBOUND_ORDER_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.JOB_NUM.value=resultVal.JOBNUM;
							document.form.action = "/track/OrderIssuingServlet?action=View";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.DONO.value = "";
							document.form.CUST_NAME.value = "";
							document.form.DONO.focus();
						}
					}
				});
	}
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){	
	formData = new FormData();
	formData.append("GINO_DONO", $("#DONO").val());
	progressBar();
	sendMailTemplate(formData);
	  //return formData;
	  
}
function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}
</script>
</div>
</div>
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER_API);
	String template_subject = (String)emailMsg.get("SUBJECT");
	String template_body = (String)emailMsg.get("BODY1");
%>
<input type="hidden" id="plant_desc" value="<%=PLNTDESC %>" />
<input type="hidden" id="template_subject" value="<%=template_subject %>" />
<input type="hidden" id="template_body" value="<%=template_body %>" />
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
